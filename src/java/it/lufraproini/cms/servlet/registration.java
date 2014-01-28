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

import it.lufraproini.cms.utility.ErroreGrave;
import it.lufraproini.cms.framework.result.FailureResult;
import it.lufraproini.cms.model.Utente;
import it.lufraproini.cms.model.impl.CMSDataLayerImpl;
import it.lufraproini.cms.utility.FormUtility;
import it.lufraproini.cms.utility.MailUtility;
import it.lufraproini.cms.utility.SecurityLayer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author fsfskittu
 */
public class registration extends HttpServlet {

    private Map<String, String> caratteri_non_ammessi;// "['\"/\\\\]";

    private String creaUtente(Utente U, Map campi_corretti) {
        String campi_errati = "";
        //verifica presenza di tutti i campi necessari
        if (campi_corretti.containsKey("username")) {
            U.setUsername(campi_corretti.get("username").toString());
            U.setCodiceAttivazione(SecurityLayer.randPassword("", 20));
        } else {
            campi_errati += " username ";
        }
        if (campi_corretti.containsKey("password")) {
            U.setPassword(SecurityLayer.criptaPassword(campi_corretti.get("password").toString(), U.getUsername()));
        } else {
            campi_errati += " password ";
        }
        if (campi_corretti.containsKey("nome")) {
            U.setNome(campi_corretti.get("nome").toString());
        } else {
            campi_errati += " nome ";
        }
        if (campi_corretti.containsKey("cognome")) {
            U.setCognome(campi_corretti.get("cognome").toString());
        } else {
            campi_errati += " cognome ";
        }
        if (campi_corretti.containsKey("email")) {
            U.setEmail(campi_corretti.get("email").toString());
        } else {
            campi_errati += " email ";
        }

        U.setSpazio_disp_img(10000000);
        return campi_errati;
    }

    private void action_registration(CMSDataLayerImpl datalayer, HttpServletRequest request) throws ErroreGrave {
        caratteri_non_ammessi = new HashMap();
        caratteri_non_ammessi.put("username", "['\",;]");
        caratteri_non_ammessi.put("email", "['\",;[^@]]");
        caratteri_non_ammessi.put("cognome", "[[\\s]{3,}[0-9][a-zA-Z]]");
        caratteri_non_ammessi.put("nome", "[[\\s]{3,}[0-9][a-zA-Z]]");
        Map campi_corretti = FormUtility.verificaCampiUrlEncoded(request, caratteri_non_ammessi);
        Utente U = datalayer.createUtente();
        String campi_errati = creaUtente(U, campi_corretti);

        if (campi_errati.equals("")) {
            try{
                String messaggio_mail = "<html><head><title>attivazione account</title></head>"
                        + "<body><p>Complimenti " + U.getNome() + U.getCognome() + " la registrazione è stata effettuata con successo! <br />"
                        + "Queste sono le sue credenziali di accesso:<br />USERNAME: " + U.getUsername() + "<br />PASSWORD:" + SecurityLayer.randPassword(U.getUsername(), 10) + "<br />br />"
                        + "La password potrà cambiarla in qualsiasi momento nella pagina di gestione del suo account<br />"
                        + "L'ultimo passo da fare per attivare l'account " + U.getUsername() + "è cliccare il seguente link "
                        + "<a href='localhost:8484/SimpleCMS/registration?att=" + U.getCodiceAttivazione() + "'>link</a></p></body></html>";
                MailUtility.sendMail("franciskittu@gmail.com", "CMS s.r.l.", U.getEmail(), "attivazione account", messaggio_mail);

            Utente nuovoUtente = datalayer.addUtente(U);

            if (nuovoUtente == null) {
                throw new ErroreGrave("Non è stato possibile memorizzare i dati dell'utente nel DB!");
            }
            } catch (MessagingException ex){
                throw new ErroreGrave("impossibile inviare email di conferma!");
            }
        
            } else {
            //messaggio di errore con i campi errati NO FAILURERESULT
        }
    }
    
    private void action_activation(CMSDataLayerImpl datalayer, String id_utente, String cod_att) throws ErroreGrave{
        long id;
        Utente U;
        
        try{
            id = SecurityLayer.checkNumeric(id_utente);
        } catch (NumberFormatException ex){
            throw new ErroreGrave("non è stato fornito l'id dell'utente!");
        }
        U = datalayer.getUtente(id);
        if(cod_att.equals(U.getCodiceAttivazione())){
            U.setAttivato(true);
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
     * @throws SQLException errore DB
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        /*DBMS*/
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        Connection connection = ds.getConnection();
        /**/
        CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);

        if (request.getParameter("att") == null) {
            try {
                action_registration(datalayer, request);
            } catch (ErroreGrave ex) {
                Logger.getLogger(registration.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            }
        }
        else{
            try {
                action_activation(datalayer, request.getParameter("id"), request.getParameter("att"));
            } catch (ErroreGrave ex) {
                Logger.getLogger(registration.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            }
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
            Logger.getLogger(registration.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(registration.class.getName()).log(Level.SEVERE, null, ex);
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
