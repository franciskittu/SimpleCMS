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
import it.lufraproini.cms.model.Immagine;
import it.lufraproini.cms.model.Sito;
import it.lufraproini.cms.model.Utente;
import it.lufraproini.cms.model.impl.CMSDataLayerImpl;
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
import javax.sql.DataSource;

/**
 *
 * @author fsfskittu
 *
 * IN QUESTA SERVLET VENGONO GESTITE TUTTE LE PAGINE VISUALIZZABILI DAL BROWSER
 * CHE RICHIEDONO DEI CONTENUTI DA FORNIRE DINAMICAMENTE
 */
public class mostra extends HttpServlet {

    private List<Map> getUltimiSiti(int n, CMSDataLayerImpl datalayer) {
        List<Map> struttura_ultimi_siti = new ArrayList<Map>();
        List<Sito> ultimi_siti = datalayer.getSitiDataOrdinati();
        for (int i = 0; i < n && i < ultimi_siti.size(); i++) {
            Utente U = ultimi_siti.get(i).getUtente();
            Immagine cover_utente = datalayer.getCoverUtente(U);
            Map info_sito = new HashMap();
            info_sito.put("name", ultimi_siti.get(i).getDescrizione());
            info_sito.put("nome_e_cognome", U.getNome()+ " " + U.getCognome());
            info_sito.put("image", cover_utente.getFile());
            info_sito.put("user", U.getUsername());
            struttura_ultimi_siti.add(info_sito);
        }
        return struttura_ultimi_siti;
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
            /*DBMS*/
            DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
            Connection connection = ds.getConnection();
            /**/
            CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);
            String par = request.getParameter("pagina");
            if(par == null){
                par = "home";
            }
            String html = null;
            Map template_data = new HashMap();
            if (par.equals("registrazione")) {
                html = "content.ftl.html";
            } else if (par.equals("home")) {
                List<Map> struttura_ultimi_siti = getUltimiSiti(5, datalayer);
                template_data.put("lastsites", struttura_ultimi_siti);
                template_data.put("lastsites_tpl", "lastsites.ftl.html");
                String err = request.getParameter("err");
                if (err != null) {
                    template_data.put("errore", err);
                }
                html = "homepage.ftl.html";
            } else if (par.equals("account")) {
                template_data.put("content_tpl", "");
                html = "Homepage.ftl.html";
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
