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
import it.lufraproini.cms.framework.result.StreamResult;
import it.lufraproini.cms.model.Immagine;
import it.lufraproini.cms.model.impl.CMSDataLayerImpl;
import it.lufraproini.cms.security.SecurityLayer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
public class image extends HttpServlet {
    
    private String error_message;
    
    /*funzione del prof. della Penna del file Download.java del progetto Esempio_Uploader modificata*/
    private void action_download(HttpServletRequest request, HttpServletResponse response, Immagine img) throws IOException{
        StreamResult result = new StreamResult(getServletContext());
        InputStream is;
        String dir = getServletContext().getInitParameter("system.image_directory");//bisogna decidere se le immagini vanno memorizzate nelle sottocartelle degli utenti
        //try {
            is = new FileInputStream(getServletContext().getRealPath("."/*o dir*/) + File.separatorChar + img.getFile()/*al momento contiene il path alla cartella delle immagini*/);//possibile FileNotFound gestita in processRequest
            request.setAttribute("contentType", img.getTipo());
            result.activate(is, img.getDimensione(), img.getNome(), request, response);
        //} catch (FileNotFoundException ex) {
            //error_message = "file non trovato!";
            //throw new NullPointerException();
        //} finally {
            try{
                is.close();
            } catch (Exception ex){
                
            }
        //}
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws SQLException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        /*DBMS*/
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        Connection connection = ds.getConnection();
        /**/
        CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);
        
        /*verifica parametri*/
        long id_img_par = SecurityLayer.checkNumeric(request.getParameter("image"));
        String utente = request.getParameter("user");
        
        /*controllo se l'immagine appartiene all'utente specificato*/
        List<Immagine> immagini = datalayer.getAllUsersImages(datalayer.getUtentebyUsername(utente));
        Immagine img_download = null;
        
        try{
            for(Immagine img:immagini){
                if(img.getID() == id_img_par){
                    img_download = img;
                    break;
                }
            }
        
            if(img_download == null){
                error_message = "L'immagine specificata non appartiene all'utente "+ utente +"!";
                img_download.getID();//eccezione
            }
                action_download(request, response, img_download);//potrebbe sollevare FileNotFound
                
        } catch (NullPointerException ex){
            /*visualizzazione della pagina di errore predefinita con il messaggio di errore riscontrato*/
            FailureResult res = new FailureResult(getServletContext());
            res.activate(error_message, request, response);
        } catch (FileNotFoundException ex){
            /*visualizzazione della pagina di errore predefinita con il messaggio di errore riscontrato*/
            FailureResult res = new FailureResult(getServletContext());
            res.activate("file non trovato!", request, response);
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
            Logger.getLogger(image.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(image.class.getName()).log(Level.SEVERE, null, ex);
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
