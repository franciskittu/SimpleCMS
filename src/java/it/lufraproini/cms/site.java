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
package it.lufraproini.cms;

import it.lufraproini.cms.framework.result.FailureResult;
import it.lufraproini.cms.framework.result.TemplateResult;
import it.lufraproini.cms.model.Pagina;
import it.lufraproini.cms.model.Sito;
import it.lufraproini.cms.model.Utente;
import it.lufraproini.cms.model.impl.CMSDataLayerImpl;
import it.lufraproini.cms.security.SecurityLayer;
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
 */
public class site extends HttpServlet {

    private String error_message;
    
    private List<String> componiPagina(Sito s, Pagina p, CMSDataLayerImpl datalayer){
        List<String> title_header_body_footer = new ArrayList();
        if(p.getSito().getID() == s.getID()){
            title_header_body_footer.add(p.getTitolo());
            title_header_body_footer.add(s.getHeader());
            title_header_body_footer.add(p.getBody());
            title_header_body_footer.add(s.getHeader());
        } else{
            error_message = "la pagina "+ p.getTitolo() +" non appartiene al sito "+ s.getUtente().getUsername() +"!";
        }
        return title_header_body_footer;
    }
    
    private List<String> componiMenu(Sito s, Pagina p, CMSDataLayerImpl datalayer){
        List<Pagina> figlie = datalayer.getFiglie(p);
        Pagina homepage = datalayer.getPagina(s.getHomepage().getID());
        List<String> menu_ordinato_per_titolo = new ArrayList();
        for(int i = 0; i < figlie.size(); i++){
            String el = figlie.get(i).getTitolo();
            for(int j = 0; j < figlie.size(); j++){
                String n = figlie.get(j).getTitolo();
                if(el.compareTo(n) < 0){
                    menu_ordinato_per_titolo.add(el);
                    break;
                }
            }
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
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        /*DBMS*/
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        Connection connection = ds.getConnection();
        /**/
        CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);
        Map template_data = new HashMap();
        List<String> menu_ordinato = new ArrayList();
        String site = request.getParameter("user");
        long image = 0;
        image = SecurityLayer.checkNumeric(request.getParameter("image"));
        long page = 0;
        page = SecurityLayer.checkNumeric(request.getParameter("page"));
        try{
            if(site == null){
                error_message = "Non è stato specificato alcun sito su cui accere!";
                site.getBytes();//possibile eccezione
            }
            List<String> title_header_body_footer = new ArrayList();
            Utente U = datalayer.getUtentebyUsername(site);
            List<Sito> sito = datalayer.getSitobyUtente(U);//per il momento è stato solo implementata la possibilità di avere un sito per utente.
            if(sito.size() < 1){
                error_message = "L'utente non ha alcun sito!";
            }
            if(image == 0 && page == 0){
                title_header_body_footer = componiPagina(sito.get(0),sito.get(0).getHomepage(), datalayer);
                title_header_body_footer.get(0).getBytes();//possibile eccezione
                template_data.put("title",title_header_body_footer.get(0));
                template_data.put("header",title_header_body_footer.get(1));
                template_data.put("body",title_header_body_footer.get(2));
                template_data.put("footer",title_header_body_footer.get(3));
                template_data.put("outline_tpl", "");
                menu_ordinato = componiMenu(sito.get(0), sito.get(0).getHomepage(), datalayer);
                menu_ordinato.get(0).getBytes();//possibile eccezione
                template_data.put("menu", menu_ordinato);
                TemplateResult tr = new TemplateResult(getServletContext());
                tr.activate("pagina_costruttore.ftl.html",template_data,response);
            } else if(page != 0){
                title_header_body_footer = componiPagina(sito.get(0), datalayer.getPagina(page), datalayer);
                title_header_body_footer.get(0).getBytes();//possibile eccezione
                template_data.put("title",title_header_body_footer.get(0));
                template_data.put("header",title_header_body_footer.get(1));
                template_data.put("body",title_header_body_footer.get(2));
                template_data.put("footer",title_header_body_footer.get(3));
                template_data.put("outline_tpl", "");
                TemplateResult tr = new TemplateResult(getServletContext());
                tr.activate("pagina_costruttore.ftl.html",template_data,response);
            } else if(image != 0){
                /*download*/
            }
        } catch (NullPointerException ex){
            FailureResult res = new FailureResult(getServletContext());
            res.activate(error_message, request, response);
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
