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
import it.lufraproini.cms.utility.Node;
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
public class provaAlbero extends HttpServlet {

    /*funzione ricorsiva che aggiunge in modo corretto tutti i figli di un nodo.*/
    private Node creaAlberoRicorsione(CMSDataLayerImpl datalayer, Pagina pagina){
        //condizione di uscita
        if(pagina == null){
            return null;
        }
        
        Node n = new Node(pagina);
        List<Pagina> figlie = datalayer.getFiglie(pagina);
        //ricorsione
        for(Pagina figlia:figlie){
            n.addChild(creaAlberoRicorsione(datalayer,figlia));
        }
        return n;
        
    }
    
    /*funzione che si serve della ricorsione per creare l'albero del sito a partire dalla homepage*/
    private Node creaAlbero(CMSDataLayerImpl datalayer, Sito sito) throws ErroreGrave {
        Pagina home = datalayer.getHomepage(sito.getID());
        if(home == null){
            throw new ErroreGrave("il sito non ha una homepage!");
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
            throws ServletException, IOException, SQLException {
        /*DBMS*/
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        Connection connection = ds.getConnection();
        /**/
        CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);
        Map template_data = new HashMap();
        Utente U = datalayer.getUtente(2);
        List<Sito> siti = datalayer.getSitobyUtente(U);
        try {
            Node home = creaAlbero(datalayer, siti.get(0));
            template_data.put("home", home);
        } catch (ErroreGrave ex) {
            Logger.getLogger(registration.class.getName()).log(Level.SEVERE, null, ex);
            FailureResult res = new FailureResult(getServletContext());
            res.activate(ex.getMessage(), request, response);
        }
        template_data.put("content_tpl", "");
        TemplateResult tr = new TemplateResult(getServletContext());
        tr.activate("albero_macro.ftl.html", template_data, response);
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
            Logger.getLogger(provaAlbero.class.getName()).log(Level.SEVERE, null, ex);
            FailureResult res = new FailureResult(getServletContext());
            res.activate(ex.getMessage(), request, response);
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
            Logger.getLogger(provaAlbero.class.getName()).log(Level.SEVERE, null, ex);
            FailureResult res = new FailureResult(getServletContext());
            res.activate(ex.getMessage(), request, response);

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
