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
import it.lufraproini.cms.model.Immagine;
import it.lufraproini.cms.model.Pagina;
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
 */
public class delete extends HttpServlet {

    //la funzione elimina la pagina ed il sottoalbero di cui è la radice
    //nel DB c'è un vincolo di integrità referenziale per cui i record coinvolti nella modifica della chiave referenziale
    //vengono eliminati
    private void elimina_sottoalbero(CMSDataLayerImpl datalayer, long radice) throws ErroreGrave {
        Pagina p = datalayer.getPagina(radice);
        if (p == null) {
            throw new ErroreGrave("La pagina non è stata trovata!");
        }
        //a cascata verranno eliminate tutte le pagine figlie della radice, nel DB infatti è stata impostata la clausula ON DELETE CASCADE
        if (datalayer.deletePagina(p) == null) {
            throw new ErroreGrave("Impossibile cancellare la pagina dal DataBase!");
        }
    }

    //la funzione elimina solo il nodo selezionato impostando il suo padre come padre dei suoi figli
    private void elimina_nodo(CMSDataLayerImpl datalayer, long id) throws ErroreGrave {
        Pagina p = datalayer.getPagina(id);
        if (p == null) {
            throw new ErroreGrave("La pagina non è stata trovata!");
        }
        List<Pagina> figlie = datalayer.getFiglie(p);
        //aggiorno il padre di tutti i figli della pagina selezionata
        for (Pagina figlia : figlie) {
            figlia.setPadre(p.getPadre());
            if (datalayer.updatePagina(figlia) == null) {
                throw new ErroreGrave("Impossibile aggiornare in modo corretto l'albero del sito! Errore nell'aggiornamento della pagina '" + figlia.getTitolo() + "' !");
            }
        }
        //infine elimino la pagina
        datalayer.deletePagina(p);
    }
    
    private void elimina_immagine(CMSDataLayerImpl datalayer, long id_image, long id_utente) throws ErroreGrave{
        Immagine img = datalayer.getImmagine(id_image);
        if(img == null){
            throw new ErroreGrave("L'immagine non è presente nel DB!");
        }
        Utente U = datalayer.getUtente(id_utente);
        long size_image = img.getDimensione();
        
        if(datalayer.deleteImmagine(img) == null){
            throw new ErroreGrave("Impossibile rimuovere l'immagine "+img.getFile()+" !");
        }
        
        U.setSpazio_disp_img(U.getSpazio_disp_img()+size_image);
        if(datalayer.updateUtente(U) == null){
            throw new ErroreGrave("Impossibile aggiornare lo spazio disponibile per le immagine dell'utente "+U.getUsername()+" !");
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
                //
                CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);

                Map parametri = request.getParameterMap();

                if (parametri.containsKey("id")) {
                    long id;
                    try {
                        String[] non_so_xke = (String[]) parametri.get("id");
                        id = SecurityLayer.checkNumeric(non_so_xke[0]);
                    } catch (NumberFormatException ex) {
                        throw new ErroreGrave("I parametri della richiesta non risultano corretti!");
                    }
                    //elimina pagina
                    
                        if (parametri.containsKey("all")) {
                            elimina_sottoalbero(datalayer, id);
                        } else {
                            elimina_nodo(datalayer, id);
                        }
                    
                    //elimina immagine
                    if (parametri.containsKey("image")) {
                        
                        elimina_immagine(datalayer, id, SecurityLayer.checkNumeric(s.getAttribute("userid").toString()));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(delete.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            } catch (ErroreGrave ex) {
                Logger.getLogger(delete.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            }
            response.sendRedirect("visualizza?pagina=account");
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
