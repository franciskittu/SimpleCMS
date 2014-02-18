/*   SimpleCMS - un semplice content management system di pagine statiche
 *    Copyright (C) 2013  Francesco Proietti, Luca Traini
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package it.lufraproini.cms.servlet;

import it.lufraproini.cms.framework.result.FailureResult;
import it.lufraproini.cms.framework.result.TemplateResult;
import it.lufraproini.cms.model.Pagina;
import it.lufraproini.cms.model.Sito;
import it.lufraproini.cms.model.Utente;
import it.lufraproini.cms.model.impl.CMSDataLayerImpl;
import it.lufraproini.cms.utility.ErroreGrave;
import it.lufraproini.cms.utility.SecurityLayer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author fsfskittu
 * 
 * Questa classe serve a far visualizzare i vari siti ospitati dagli utenti alle richieste provenienti
 * da utenti registrati e non.
 */
public class site extends HttpServlet {

    /*la funzione ritorna in una lista tutte le componenti della pagina, in ordine: titolo,header body e footer*/
    private List<String> componiPagina(Sito s, Pagina p) throws ErroreGrave {
        List<String> title_header_body_footer = new ArrayList();
        //controllo se la pagina richiesta è nell'albero del sito richiesto
        if (p.getSito().getId() == s.getId()) {
            title_header_body_footer.add(SecurityLayer.stripSlashes(s.getNome()+" - "+p.getTitolo()));
            title_header_body_footer.add(SecurityLayer.stripSlashes(s.getHeader()));
            title_header_body_footer.add(SecurityLayer.stripSlashes(p.getBody()));
            title_header_body_footer.add(SecurityLayer.stripSlashes(s.getFooter()));
        } else {
            throw new ErroreGrave("la pagina " + p.getTitolo() + " non appartiene al sito " + s.getUtente().getUsername() + "!");
        }
        return title_header_body_footer;
    }
    
    /*la seguente funzione ordina alfabeticamente per titolo il menu della pagina*/
    private List<Map> componiMenu(Sito s, Pagina p, CMSDataLayerImpl datalayer) {
        List<Pagina> figlie = datalayer.getFiglie(p);
        Pagina homepage = datalayer.getPagina(s.getHomepage().getId());
        List<Map> menu_ordinato_per_titolo = new ArrayList();//valore di ritorno
        /*se la pagina non è l'homepage allora deve contenere un link ad essa ed al suo padre*/
        if(homepage.getId() != p.getId()){
            Map home = new HashMap();
            home.put("id", homepage.getId());
            home.put("titolo", homepage.getTitolo());
            menu_ordinato_per_titolo.add(home);
            if(homepage.getId() != p.getPadre().getId()){
                Map pag = new HashMap();
                pag.put("id", p.getPadre().getId());
                pag.put("titolo", p.getPadre().getTitolo());
                menu_ordinato_per_titolo.add(pag);
            }
        }
        /*ordinamento
        tempo O(n^2)*/
        for (int i = 0; i < figlie.size(); i++) {
            int min = -1;
            Map item = new HashMap();
            for (int j = 0; j < figlie.size(); j++) {
                if(figlie.get(j) != null){
                String el = figlie.get(j).getTitolo();//elemento di confronto
                if (min == -1 || (figlie.get(min) != null && el.compareTo(figlie.get(min).getTitolo()) < 0)) {
                    /*in questo caso la stringa confrontata viene alfabeticamente prima di quella finora considerata minima e l'elemento minimo và quindi aggiornato*/
                    min = j;
                    
                }
                }
            }
            /*inserimento elemento in lista*/
            Pagina el = figlie.get(min);
            figlie.set(min, null);
            item.put("id", el.getId());
            item.put("titolo",el.getTitolo());
            menu_ordinato_per_titolo.add(item);
        }
        return menu_ordinato_per_titolo;
    }
    

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        /*DBMS*/
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        Connection connection = ds.getConnection();
        /**/
        CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);

        Map template_data = new HashMap();
        
        /*controllo parametri*/
        String site = request.getParameter("user");
        long page = 0;//valore non valido
        if(request.getParameter("page") != null){
            page = SecurityLayer.checkNumeric(request.getParameter("page"));
        }
        /**/
        try {
            if (site == null) {
                throw new ErroreGrave("Non è stato specificato alcun sito su cui accedere!");
            }
            List<String> title_header_body_footer = null;//pagina html
            List<Map> menu_ordinato = null;//menu della pagina
            Utente U = datalayer.getUtentebyUsername(site);//creatore del sito
            List<Sito> sito = datalayer.getSitobyUtente(U);//per il momento è stato solo implementata la possibilità di avere un sito per utente.
            if (sito.size() < 1) {
                throw new ErroreGrave("L'utente non ha alcun sito!");
            }
            if (page == 0) {
                /*in questo caso si sta accedendo alla homepage del sito in quanto non è stata specificata una pagina precisa nell'URL*/
                title_header_body_footer = componiPagina(sito.get(0)/*primo sito*/, sito.get(0).getHomepage()/*homepage del sito*/);
                /*preparo l'output inserendo tutti gli elementi della pagina tranne il menu*/
                template_data.put("nomeutente", site);
                template_data.put("title", title_header_body_footer.get(0));
                template_data.put("header", title_header_body_footer.get(1));
                template_data.put("body", title_header_body_footer.get(2));
                template_data.put("footer", title_header_body_footer.get(3));
                template_data.put("style", sito.get(0).getCss().getFile());
                template_data.put("outline_tpl", "");//nessun outline
                /*preparo il menu*/
                menu_ordinato = componiMenu(sito.get(0), sito.get(0).getHomepage(), datalayer);
                template_data.put("menu", menu_ordinato);// se il menu non contiene elementi questa cosa sarà gestita dalla pagina freemarker
                /*il menu è stato caricato e adesso siamo pronti per l'output*/
                TemplateResult tr = new TemplateResult(getServletContext());
                tr.activate("pagina_costruttore.ftl.html", template_data, response);
            } else {
                title_header_body_footer = componiPagina(sito.get(0), datalayer.getPagina(page));
                template_data.put("nomeutente", site);
                template_data.put("title", title_header_body_footer.get(0));
                template_data.put("header", title_header_body_footer.get(1));
                template_data.put("body", title_header_body_footer.get(2));
                template_data.put("footer", title_header_body_footer.get(3));
                template_data.put("outline_tpl", "");
                template_data.put("style", sito.get(0).getCss().getFile());
                /*preparo il menu*/
                menu_ordinato = componiMenu(sito.get(0), datalayer.getPagina(page)/*pagina richiesta*/, datalayer);
                template_data.put("menu", menu_ordinato);
                /*il menu è stato caricato e adesso siamo pronti per l'output*/
                TemplateResult tr = new TemplateResult(getServletContext());
                tr.activate("pagina_costruttore.ftl.html", template_data, response);
            }
        } catch (ErroreGrave ex) {
            Logger.getLogger(site.class.getName()).log(Level.SEVERE, null, ex);
            /*visualizzazione della pagina di errore predefinita con il messaggio di errore riscontrato*/
            FailureResult res = new FailureResult(getServletContext());
            res.activate(ex.getMessage(), request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(site.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(site.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
