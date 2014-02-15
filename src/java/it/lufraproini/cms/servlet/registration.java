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
import it.lufraproini.cms.framework.result.TemplateResult;
import it.lufraproini.cms.model.Css;
import it.lufraproini.cms.model.Pagina;
import it.lufraproini.cms.model.Sito;
import it.lufraproini.cms.model.Utente;
import it.lufraproini.cms.model.impl.CMSDataLayerImpl;
import it.lufraproini.cms.utility.FormUtility;
import it.lufraproini.cms.utility.MailUtility;
import it.lufraproini.cms.utility.SecurityLayer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    private Map<String, String> regex;// "['\"/\\\\]";
    private String rand_passwd_in_chiaro;
    

    private List<String> creaUtente(Utente U, Map campi_corretti, CMSDataLayerImpl datalayer) {
        List<String> campi_errati = new ArrayList();
        //verifica presenza di tutti i campi necessari
        if (campi_corretti.containsKey("username") && datalayer.getUtentebyUsername(SecurityLayer.addSlashes(campi_corretti.get("username").toString())) == null) {
            U.setUsername(SecurityLayer.addSlashes(campi_corretti.get("username").toString()));
            U.setCodiceAttivazione(SecurityLayer.randPassword("", 20));
            rand_passwd_in_chiaro = SecurityLayer.randPassword(U.getUsername(), 10);
            U.setPassword(SecurityLayer.criptaPassword(rand_passwd_in_chiaro, U.getUsername()));
        } else {
            campi_errati.add("campo Username non valido!");
        }

        if (campi_corretti.containsKey("nome")) {
            U.setNome(SecurityLayer.addSlashes(campi_corretti.get("nome").toString()));
        } else {
            campi_errati.add("campo Nome non valido!");
        }
        if (campi_corretti.containsKey("cognome")) {
            U.setCognome(SecurityLayer.addSlashes(campi_corretti.get("cognome").toString()));
        } else {
            campi_errati.add("campo Cognome non valido!");
        }
        if (campi_corretti.containsKey("email")) {
            U.setEmail(SecurityLayer.addSlashes(campi_corretti.get("email").toString()));
        } else {
            campi_errati.add("campo Email non valido!");
        }

        U.setSpazio_disp_img(10000000);
        return campi_errati;
    }

    private void action_registration(CMSDataLayerImpl datalayer, HttpServletRequest request, Map template_data) throws ErroreGrave {
        regex = new HashMap();
        regex.put("username", "\\w*[-'\"\\\\/]*\\w*");
        regex.put("email", "\\w*[^;,]+[\\.\\w]*@[a-z0-9]+\\.[a-z]{2,4}");
        regex.put("cognome", "[a-z][A-Z]*[\\s]?[a-zA-Z]*[\\s]?[a-zA-Z]*");
        regex.put("nome", "[a-zA-Z]*[\\s]?[a-zA-Z]*[\\s]?[a-zA-Z]*");
        Map campi_corretti = FormUtility.verificaCampiUrlEncoded(request, regex);
        Utente U = datalayer.createUtente();
        List<String> campi_errati = creaUtente(U, campi_corretti, datalayer);

        if (!campi_errati.isEmpty()) {
            try {
                Utente nuovoUtente = datalayer.addUtente(U);

                if (nuovoUtente == null) {
                    throw new ErroreGrave("Non è stato possibile memorizzare i dati dell'utente nel DB!");
                }
                String messaggio_mail = "<html><head><title>attivazione account</title></head>"
                        + "<body><p>Complimenti " + nuovoUtente.getNome() + " " + nuovoUtente.getCognome() + " la registrazione &egrave; stata effettuata con successo! <br />"
                        + "Queste sono le sue credenziali di accesso:<br />USERNAME: " + nuovoUtente.getUsername() + "<br />PASSWORD:" + rand_passwd_in_chiaro + "<br /><br />"
                        + "La password potr&agrave; cambiarla in qualsiasi momento nella pagina di gestione del suo account<br />"
                        + "L'ultimo passo da fare per attivare l'account " + nuovoUtente.getUsername() + " &egrave; cliccare sul seguente link "
                        + "<a href='localhost:8484/SimpleCMS/registration?att=" + nuovoUtente.getCodiceAttivazione() + "&id=" + U.getID() + "'>link</a></p></body></html>";
                
                MailUtility.sendMail("franciskittu@gmail.com", "CMS", nuovoUtente.getEmail(), "attivazione account", messaggio_mail);

            } catch (MessagingException ex) {
                throw new ErroreGrave("impossibile inviare email di conferma!");
            }

        } else {
            template_data.put("errori_registrazione", campi_errati);
            /*String[] array = campi_errati.split("\\s+");
            for (String campo : array) {
                if (!campo.isEmpty() && !campo.equals(" ")) {
                    template_data.put("err_" + campo, "display" /*classe css);
                }
            }*/
        }
    }
    
    private void creaSito(CMSDataLayerImpl datalayer, Utente U) throws ErroreGrave{
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        java.sql.Date data_db = new java.sql.Date(now.getTime());
        String default_header = "<p>Questa è il contenuto di default dell'homepage del sito di "+U.getUsername()+"!</p>";
        String default_footer = "<p>Il sito è stato creato il"+now.toString()+"</p>";
        String default_body = "<p>Scrivere contenuto!</p>";
        
        Css default_css = datalayer.getFirstCSS();
        if(default_css == null){
            throw new ErroreGrave("impossibile reperire un foglio di stile!");
        }
        
        Sito nuovo_sito = datalayer.createSito();
        nuovo_sito.setCss(default_css);
        nuovo_sito.setDescrizione(U.getUsername());
        nuovo_sito.setFooter(default_footer);
        nuovo_sito.setHeader(default_header);
        nuovo_sito.setUtente(U);
        nuovo_sito.setDataCreazione(data_db);
        nuovo_sito = datalayer.addSito(nuovo_sito);
        if(nuovo_sito == null){
            throw new ErroreGrave("impossibile aggiungere sito al DB!");
        }
        Pagina home_nuovo_sito = datalayer.createPagina();
        home_nuovo_sito.setBody(default_body);
        home_nuovo_sito.setTitolo("Homepage");
        home_nuovo_sito.setSito(nuovo_sito);
        home_nuovo_sito = datalayer.addPagina(home_nuovo_sito);
        if(home_nuovo_sito == null){
            throw new ErroreGrave("impossibile aggiungere homepage al DB!");
        }
        nuovo_sito.setHomepage(home_nuovo_sito);
        
        nuovo_sito = datalayer.updateSito(nuovo_sito);
        if(nuovo_sito == null){
            throw new ErroreGrave("errore nell'associare l'homepage al sito di "+U.getUsername());
        }
        home_nuovo_sito.setSito(nuovo_sito);
        home_nuovo_sito = datalayer.updatePagina(home_nuovo_sito);
        if(home_nuovo_sito == null){
            throw new ErroreGrave("errore nell'associare il sito all'homepage");
        }
    }

    private void action_activation(CMSDataLayerImpl datalayer, String id_utente, String cod_att) throws ErroreGrave {
        long id;
        Utente U;
        
        try {
            id = SecurityLayer.checkNumeric(id_utente);
        } catch (NumberFormatException ex) {
            throw new ErroreGrave("non è stato fornito l'id dell'utente!");
        }
        U = datalayer.getUtente(id);
        if (cod_att.equals(U.getCodiceAttivazione())) {
            U.setAttivato(true);
        }
        if (datalayer.updateUtente(U) == null) {
            throw new ErroreGrave("non è stato possibile aggiornare l'account utente!");
        }
        creaSito(datalayer, U);
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

        Map template_data = new HashMap();

        if (request.getParameter("att") == null) {
            try {
                action_registration(datalayer, request, template_data);
            } catch (ErroreGrave ex) {
                Logger.getLogger(registration.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            }
            TemplateResult tr = new TemplateResult(getServletContext());
            tr.activate("content.ftl.html", template_data, response);
        } else {
            try {
                action_activation(datalayer, request.getParameter("id"), request.getParameter("att"));
            } catch (ErroreGrave ex) {
                Logger.getLogger(registration.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            }
            response.sendRedirect("Homepage.html");/*visualizza?pagina=home*/

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
