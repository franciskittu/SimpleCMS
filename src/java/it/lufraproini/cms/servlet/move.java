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
import it.lufraproini.cms.model.Pagina;
import it.lufraproini.cms.model.Sito;
import it.lufraproini.cms.model.Utente;
import it.lufraproini.cms.model.impl.CMSDataLayerImpl;
import it.lufraproini.cms.utility.ErroreGrave;
import it.lufraproini.cms.utility.SecurityLayer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
 * 
 * SERVLET GRUPPO ACCOUNT
 */
public class move extends HttpServlet {

    private void cambia_padre(CMSDataLayerImpl datalayer, long id_pagina, long id_padre, Utente U) throws ErroreGrave {
        Pagina pag = datalayer.getPagina(id_pagina);
        Pagina nuovo_padre = datalayer.getPagina(id_padre);
        List<Sito> sito = datalayer.getSitobyUtente(U);
        
        if (pag == null || nuovo_padre == null) {
            throw new ErroreGrave("Le pagine specificate nella richiesta non sono state trovate!");
        }
        if(sito.get(0).getID() != nuovo_padre.getID()){
            throw new ErroreGrave("Le pagine non appartengono al sito dell'utente "+ U.getUsername() + " !");
        }
        pag.setPadre(nuovo_padre);
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
        //verifica validità sessione
        HttpSession s = SecurityLayer.checkSession(request);
        if (s != null) {
            try {
                //DBMS
                DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
                Connection connection = ds.getConnection();
                //
                CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);

                Map parametri = request.getParameterMap();
                Utente U = datalayer.getUtente(SecurityLayer.checkNumeric(s.getAttribute("userid").toString()));
                
                if (parametri.containsKey("id_pagina") && parametri.containsKey("id_padre")) {
                    long id_pagina, id_padre;
                    try {
                        id_pagina = SecurityLayer.checkNumeric(parametri.get("id_pagina").toString());
                        id_padre = SecurityLayer.checkNumeric(parametri.get("id_padre").toString());
                    } catch (NumberFormatException ex) {
                        throw new ErroreGrave("I parametri non sono validi per l'operazione richiesta!");
                    }
                    cambia_padre(datalayer, id_pagina, id_padre, U);
                    response.sendRedirect("visualizza?pagina=account");
                }
            } catch (SQLException ex) {
                Logger.getLogger(move.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            } catch (ErroreGrave ex) {
                Logger.getLogger(move.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            }

        }
        else {
            response.sendRedirect("visualizza?pagina=home");
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
