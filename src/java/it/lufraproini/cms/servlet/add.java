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
 * SERVLET GRUPPO ACCOUNT
 */
public class add extends HttpServlet {

    //la funzione di seguito controlla i parametri ricevuti dalla servlet e verificando che la pagina padre
    //appartenga allo stesso sito dell'utente crea una nuova pagina e l'aggiunge all'albero
    private void aggiungi_pagina(CMSDataLayerImpl datalayer, String titolo,String id,String contenuto, String modello, Utente U) throws ErroreGrave{
        List<Sito> sito = datalayer.getSitobyUtente(U);
        long id_padre;
        if(titolo == null || id == null || contenuto == null){
            throw new ErroreGrave("I parametri inviati non sono validi per soddisfare la richiesta!");
        }
        try{
             id_padre = SecurityLayer.checkNumeric(id);
        } catch (NumberFormatException ex){
            throw new ErroreGrave("I parametri inviati non sono validi per soddisfare la richiesta!");
        }
        Pagina padre = datalayer.getPagina(id_padre);
        Pagina p = datalayer.createPagina();
        if(padre == null){
            throw new ErroreGrave("La pagina padre non è stata trovata nel database!");
        }
        if(p == null){
            throw new ErroreGrave("Non è stato possibile creare una nuova pagina nel modello!");
        }
        p.setSito(sito.get(0));
        p.setBody(SecurityLayer.addSlashes(contenuto));
        p.setTitolo(SecurityLayer.addSlashes(titolo));
        if(modello != null){
            //default false
            p.setModello(true);
        }
        p.setPadre(padre);
        if(datalayer.addPagina(p) == null){
            throw new ErroreGrave("Impossibile aggiungere la pagina nel database!");
        }
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
        HttpSession s = SecurityLayer.checkSession(request);
        if (s != null) {
            try {
                //DBMS
                DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
                Connection connection = ds.getConnection();
                //
                CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);
                
                Utente U = datalayer.getUtente(SecurityLayer.checkNumeric(s.getAttribute("userid").toString()));
  
                aggiungi_pagina(datalayer, request.getParameter("title"),request.getParameter("id"),request.getParameter("editor"),request.getParameter("model"), U);
                response.sendRedirect("visualizza?pagina=account");
            } catch (SQLException ex) {
                Logger.getLogger(add.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            } catch (ErroreGrave ex) {
                Logger.getLogger(add.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            }
        }
        else {
            response.sendRedirect("visualizza?pagina=home&auth=");
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
