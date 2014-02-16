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

import it.lufraproini.cms.framework.result.TemplateResult;
import it.lufraproini.cms.model.Css;
import it.lufraproini.cms.model.Immagine;
import it.lufraproini.cms.model.Pagina;
import it.lufraproini.cms.model.Sito;
import it.lufraproini.cms.model.Utente;
import it.lufraproini.cms.model.impl.CMSDataLayerImpl;
import it.lufraproini.cms.utility.Node;
import it.lufraproini.cms.utility.SecurityLayer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author fsfskittu
 *
 * IN QUESTA SERVLET VENGONO GESTITE TUTTE LE PAGINE VISUALIZZABILI DAL BROWSER
 * CHE RICHIEDONO DEI CONTENUTI DA FORNIRE DINAMICAMENTE
 */
public class mostra extends HttpServlet {

    private List<Map> getAllImages(CMSDataLayerImpl datalayer, Utente U){
        List<Immagine> images = datalayer.getAllUsersImages(U);
        List<Map> lista_struttura_immagini = new ArrayList<Map>();
        for(Immagine img:images){
            Map info_img = new HashMap();
            info_img.put("id", img.getID());
            info_img.put("cover", img.getCover());
            info_img.put("thumb", img.getThumb());
            info_img.put("nome", img.getNome());
            lista_struttura_immagini.add(info_img);
        }
        return lista_struttura_immagini;
    }
    private List<Map> getAllCSS(CMSDataLayerImpl datalayer, Utente U){
        List<Css> fogli_di_stile = datalayer.getAllCSS();
        List<Map> struttura_stili = new ArrayList<Map>();
        List<Sito> siti_utente = datalayer.getSitobyUtente(U);
        for(Css foglio:fogli_di_stile){
            Map info = new HashMap();
            info.put("id", foglio.getID());
            info.put("name", foglio.getNome());
            info.put("active",0);
            if(foglio.getID() == siti_utente.get(0).getCss().getID()){
                info.put("active",1);
            }
            struttura_stili.add(info);
        }
        return struttura_stili;
    }
    
    private List<Map> getUltimiSiti(int n, CMSDataLayerImpl datalayer) {
        List<Map> struttura_ultimi_siti = new ArrayList<Map>();
        List<Sito> ultimi_siti = datalayer.getSitiDataOrdinati();
        for (int i = 0; i < n && i < ultimi_siti.size(); i++) {
            Utente U = ultimi_siti.get(i).getUtente();
            Immagine cover_utente = datalayer.getCoverUtente(U);
            if (cover_utente == null) {
                continue;
            }
            Map info_sito = new HashMap();
            info_sito.put("name", ultimi_siti.get(i).getDescrizione());
            info_sito.put("nome_e_cognome", U.getNome() + " " + U.getCognome());
            info_sito.put("image", cover_utente.getFile());
            info_sito.put("user", U.getUsername());
            struttura_ultimi_siti.add(info_sito);
        }
        return struttura_ultimi_siti;
    }

    /*funzione ricorsiva che aggiunge in modo corretto tutti i figli di un nodo.*/
    private Node creaAlberoRicorsione(CMSDataLayerImpl datalayer, Pagina pagina) {
        //condizione di uscita
        if (pagina == null) {
            return null;
        }

        Node n = new Node(pagina);
        List<Pagina> figlie = datalayer.getFiglie(pagina);
        //ricorsione
        for (Pagina figlia : figlie) {
            n.addChild(creaAlberoRicorsione(datalayer, figlia));
        }
        return n;

    }

    /*funzione che si serve della ricorsione per creare l'albero del sito a partire dalla homepage*/
    private Node creaAlbero(CMSDataLayerImpl datalayer, Sito sito) {
        Pagina home = datalayer.getHomepage(sito.getID());
        if (home == null) {
            return null;
        }
        return creaAlberoRicorsione(datalayer, home);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            /*DBMS*/
            DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
            Connection connection = ds.getConnection();
            /**/
            CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);
            String par = request.getParameter("pagina");
            if (par == null) {
                par = "home";
            }
            String html = null;
            String err = null;
            boolean permesso = false;
            Map template_data = new HashMap();
            Utente U = null;
            if(s != null){
                U = datalayer.getUtente(SecurityLayer.checkNumeric(s.getAttribute("userid").toString()));
                template_data.put("utente", U.getUsername());
            }
            while (permesso == false) {
                if (par.equals("registrazione")) {
                    permesso = true;
                    html = "content.ftl.html";
                } else if (par.equals("home")) {
                    permesso = true;
                    List<Map> struttura_ultimi_siti = getUltimiSiti(5, datalayer);
                    template_data.put("lastsites", struttura_ultimi_siti);
                    template_data.put("lastsites_tpl", "lastsites.ftl.html");
                    if(err == null){
                        err = request.getParameter("err");
                    }
                    if (err != null) {
                        template_data.put("errore", err);
                    }
                    html = "homepage.ftl.html";
                } else if (par.equals("account")) {
                    if (s != null) {
                        permesso = true;
                        //editor
                        template_data.put("editor", "editor.ftl.html");
                        //albero
                        template_data.put("tree", "tree.ftl.html");
                        List<Sito> siti = datalayer.getSitobyUtente(U);
                        Node home = creaAlbero(datalayer, siti.get(0));
                        template_data.put("home", home);
                        //immagini
                        template_data.put("images_tpl", "images.ftl.html");
                        
                        template_data.put("images", getAllImages(datalayer, U));
                        //css
                        template_data.put("styles", getAllCSS(datalayer, U));
                        html = "sitemanager.ftl.html";
                    } else {
                        err = "non si possiedono i permessi per accedere al contenuto!";
                        par = "home";
                    }
                } else if (par.equals("login")) {
                    permesso = true;
                    String err_sign_up = request.getParameter("errors_sign_up");
                    String err_login = request.getParameter("errors_login");
                    String act_ok = request.getParameter("attivazione");
                    String reg_ok = request.getParameter("registrazione");
                    if (err_sign_up != null) {
                        String[] lista_errori = err_sign_up.split(",");
                        template_data.put("errors_sign_up", lista_errori);
                    } else if (err_login != null) {
                        template_data.put("errors_login", err_login);
                    }
                    if (act_ok != null) {
                        template_data.put("mex_attivazione", "Complimenti, il suo account è stato attivato!");
                    }
                    if (reg_ok != null) {
                        template_data.put("mex_registrazione", "Complimenti, la registrazione è stata effettuata con successo!<br/>Controlli la sua e-mail per coompletare la procedura di attivazione!");
                    }
                    html = "login.ftl.html";
                }
            }
            TemplateResult tr = new TemplateResult(getServletContext());
            tr.activate(html, template_data, response);
        } catch (SQLException ex) {
            //
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
        processRequest(request, response);
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
        processRequest(request, response);
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
