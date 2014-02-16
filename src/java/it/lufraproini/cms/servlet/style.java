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
import it.lufraproini.cms.model.Css;
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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author fsfskittu
 * SERVLET GRUPPO ACCOUNT
 */
public class style extends HttpServlet {

    //la funzione cambia lo stile del sito dell'utente sul database e ritorna una lista contenente l'id del vecchio css e l'id del nuovo in quest'ordine
    private List cambiaStile(CMSDataLayerImpl datalayer, Utente U, long id_new_css) throws ErroreGrave{
        List id_old_css__id_new_css = new ArrayList();
        List<Sito> sito = datalayer.getSitobyUtente(U);
        Css nuovo = datalayer.getCSS(id_new_css);
        if(nuovo == null){
            throw new ErroreGrave("Il foglio di stile non è stato trovato nel DataBase!");
        }
        
        id_old_css__id_new_css.add(sito.get(0).getCss().getID());
        sito.get(0).setCss(nuovo);
        id_old_css__id_new_css.add(id_new_css);
        if(datalayer.updateSito(sito.get(0)) == null){
            throw new ErroreGrave("Impossibile modificare la caratteristica del sito nel database!");
        }
        return id_old_css__id_new_css;
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
        //controllo autenticazione
        HttpSession s = SecurityLayer.checkSession(request);
        if (s != null) {
            try {
                //DBMS
                DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
                Connection connection = ds.getConnection();
                //
                CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);
                
                Utente U = datalayer.getUtente(SecurityLayer.checkNumeric(s.getAttribute("userid").toString()));
                long id_nuovo_css = SecurityLayer.checkNumeric(request.getParameter("id"));// se non è un numero verrà comunque lanciata un'eccezione in cambiaStile
                Map template_data = new HashMap();
                
                List ids = cambiaStile(datalayer, U, id_nuovo_css);
                //output
                template_data.put("outline_tpl", "");
                template_data.put("css_old", ids.get(0));
                template_data.put("css_current", ids.get(1));
                TemplateResult tr = new TemplateResult(getServletContext());
                tr.activate("account_ajax.ftl.json", template_data, response);
            } catch (SQLException ex){
                Logger.getLogger(add.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            } catch (ErroreGrave ex){
                Logger.getLogger(add.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            }
        } else {
            response.sendRedirect("visualizza?pagina=home&err_auth=");
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
