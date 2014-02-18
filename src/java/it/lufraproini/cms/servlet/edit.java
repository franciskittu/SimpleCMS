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
//la servlet permette l'editing di pagine preesistenti nel sito
//la pagina è accessibile solo dagli utenti loggati al sistema
public class edit extends HttpServlet {

    //funzione chiamata per caricare i dati utili alla richiesta AJAX per il precaricamento della form di editing
    private List<String> caricamento_pagina_esistente(CMSDataLayerImpl datalayer, long id) throws ErroreGrave {
        List<String> title_body_checked = new ArrayList();
        Pagina p = datalayer.getPagina(id);
        if (p != null) {
            title_body_checked.add(SecurityLayer.stripSlashes(p.getTitolo().replaceAll("(\r\n|\n)", "")));
            title_body_checked.add(SecurityLayer.stripSlashes(p.getBody().replaceAll("(\r\n|\n)", "")));
            if (p.getModello()) {
                title_body_checked.add("1");
            } else {
                title_body_checked.add("0");
            }
        } else {
            throw new ErroreGrave("la pagina richiesta non &egrave; stata trovata!");
        }
        return title_body_checked;
    }

    //funzione che aggiorna la pagina in seguito al submit della form dell'editing
    private void aggiorna_pagina(CMSDataLayerImpl datalayer, Map parametri, long id) throws ErroreGrave{
        Pagina p = datalayer.getPagina(id);
        String [] val = (String []) parametri.get("editor");
        p.setBody(SecurityLayer.addSlashes(val[0].replaceAll("(\r\n|\n)", "")));
        val = (String[]) parametri.get("title");
        p.setTitolo(SecurityLayer.addSlashes(val[0].replaceAll("(\r\n|\n)", "")));
        if(parametri.containsKey("model")){
            p.setModello(true);
        } else {
            p.setModello(false);
        }
        Pagina res;
        if(p.getPadre()==null){
            res = datalayer.updateHomepage(p);
        }
        else {
            res = datalayer.updatePagina(p);
        }
        if( res == null){
            throw new ErroreGrave("impossibile aggiornare la pagina sul DataBase!");
        }
    }
    
    //funzione chiamata per caricare il contenuto dell'header o del footer (richiesta AJAX)
    private String caricamento_header_e_footer(CMSDataLayerImpl datalayer, String [] tipo, Utente U) throws ErroreGrave{
        List<Sito> sito = datalayer.getSitobyUtente(U);
        if(sito.size() < 1){
            throw new ErroreGrave("Sembra che l'utente non abbia alcun sito!");
        }
        if(tipo[0].equals("footer")){
            return sito.get(0).getFooter().replaceAll("(\r\n|\n)", "");
        }
        return sito.get(0).getHeader().replaceAll("(\r\n|\n)", "");
    }
    
    private void aggiorna_header_e_footer(CMSDataLayerImpl datalayer, String[] tipo, String[] contenuto, Utente U) throws ErroreGrave{
        List<Sito> sito = datalayer.getSitobyUtente(U);
        
        if(sito.size() < 1){
            throw new ErroreGrave("Sembra che l'utente non abbia alcun sito!");
        }
        
        if(tipo[0].equals("footer")){
            sito.get(0).setFooter(SecurityLayer.addSlashes(contenuto[0].replaceAll("(\r\n|\n)", "")));
        } else {
            sito.get(0).setHeader(SecurityLayer.addSlashes(contenuto[0].replaceAll("(\r\n|\n)", "")));
        }
        if(datalayer.updateSito(sito.get(0)) == null){
            throw new ErroreGrave("impossibile aggiornare il DataBase!");
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
        //verifica validità sessione
        HttpSession s = SecurityLayer.checkSession(request);
        if (s != null) {
            try {
                //DBMS
                DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
                Connection connection = ds.getConnection();

                CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);
                
                Map parametri = request.getParameterMap();
                Map template_data = new HashMap();
                
                Utente U = datalayer.getUtente(SecurityLayer.checkNumeric(s.getAttribute("userid").toString()));
                long id_pagina;
                    
                //non effettuo alcun controllo (eccetto l'id) sui valori dei parametri poichè le stringhe possono contenere qualsiasi carattere
                //gestione FORM
                if (parametri.containsKey("title") && parametri.containsKey("editor") && parametri.containsKey("id")) {
                    try {
                        String[] val = (String [])parametri.get("id");
                        id_pagina = SecurityLayer.checkNumeric(val[0]);
                    } catch (NumberFormatException ex) {
                        throw new ErroreGrave("Il parametro inviato non contiene valori compatibili per la richiesta!");
                    }
                    aggiorna_pagina(datalayer, parametri, id_pagina);
                    response.sendRedirect("visualizza?pagina=account");
                }
                //richiesta AJAX pagina
                else if (parametri.size() == 1 && parametri.containsKey("id")) {
                    try {
                        String[] val = (String [])parametri.get("id");
                        id_pagina = SecurityLayer.checkNumeric(val[0]);
                    } catch (NumberFormatException ex) {
                        throw new ErroreGrave("Il parametro inviato non contiene valori compatibili per la richiesta!");
                    }
                    List<String> dati_pagina = caricamento_pagina_esistente(datalayer, id_pagina);
                    template_data.put("outline_tpl", "");
                    template_data.put("title", dati_pagina.get(0));
                    template_data.put("body", dati_pagina.get(1));
                    template_data.put("checked", dati_pagina.get(2));
                    template_data.put("css_old","");
                    template_data.put("css_current","");
                    template_data.put("img_old", "");
                    template_data.put("img_current", "");
                    TemplateResult tr = new TemplateResult(getServletContext());
                    tr.activate("account_ajax.ftl.json", template_data, response);
                }
                //richiesta AJAX header e footer
                else if(parametri.size() == 1 && parametri.containsKey("type")){
                    String body = caricamento_header_e_footer(datalayer, (String[]) parametri.get("type"), U);
                    template_data.put("outline_tpl", "");
                    template_data.put("body", body);     
                    template_data.put("title", "");
                    template_data.put("checked", "");
                    template_data.put("css_old","");
                    template_data.put("css_current", "");
                    TemplateResult tr = new TemplateResult(getServletContext());
                    tr.activate("account_ajax.ftl.json", template_data, response);
                }
                else if(parametri.containsKey("type") && parametri.containsKey("editor")){
                    aggiorna_header_e_footer(datalayer, (String []) parametri.get("type"), (String [])parametri.get("editor"), U);
                    response.sendRedirect("visualizza?pagina=account");
                }
                //update nome del sito
                else if(parametri.size() == 2 && parametri.containsKey("nome_sito")){
                    List<Sito> sito = datalayer.getSitobyUtente(U);
                    String[] val = (String[])parametri.get("nome_sito");
                    sito.get(0).setNome(SecurityLayer.addSlashes(val[0]));
                    datalayer.updateSito(sito.get(0));
                    response.sendRedirect("visualizza?pagina=account");
                }
                //richiesta non gestita
                else {
                    throw new ErroreGrave("I parametri non sono compatibili con alcuna richiesta disponibile!");
                }
            } catch (ErroreGrave ex) {
                Logger.getLogger(edit.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            } catch (SQLException ex) {
                Logger.getLogger(edit.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate("problemi con la connessione al DataBase!", request, response);
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
