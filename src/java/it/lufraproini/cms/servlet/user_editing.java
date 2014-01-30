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
import it.lufraproini.cms.model.Pagina;
import it.lufraproini.cms.model.Sito;
import it.lufraproini.cms.model.Utente;
import it.lufraproini.cms.model.impl.CMSDataLayerImpl;
import it.lufraproini.cms.utility.Nodo;
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
public class user_editing extends HttpServlet {

    /*l'albero che ritorna la funzione è sottoforma di lista ed è ordinato secondo una 
    visita in profondità
    questo è stato fatto per visualizzare l'albero del sito tramite freemarker senza troppe
    complicazioni*/
    private List<Nodo> creaAlbero(CMSDataLayerImpl datalayer, Utente U){
        List<Nodo> albero = new ArrayList<Nodo>();
        List<Sito> s = datalayer.getSitobyUtente(U);
        Sito sito = s.get(0);
        
        
        //la homepage è la radice dell'albero
        albero.add(new Nodo(datalayer.getHomepage(sito.getHomepage().getID()), 0));
        List<Pagina> foglie = datalayer.getFoglie(sito);
        for(int i = 0; i < foglie.size(); i++){
            List<Pagina> antenati_cresc = datalayer.getAntenati(foglie.get(i));
            int k = 1;//livello nodo
            int j = antenati_cresc.size()-2;//poichè il risultato è ordinato in modo crescente di parentela l'ultimo padre è la homepage, quindi non và considerata
            while(j >= 0){
                boolean inserito = false;
                Nodo n = new Nodo(antenati_cresc.get(j),k);
                //verifico se il nodo è già stato inserito nella struttura
                for(int z = 0; z < albero.size(); z++){
                    if(n.getLivello() == albero.get(z).getLivello()
                            && n.getID() == albero.get(z).getID()){
                        inserito = true;
                        break;
                    }
                }
                if(!inserito){
                    albero.add(n);
                }
                
                k++;//il livello aumenta allo scorrere della lista
                j--;
            }
        }
        
        return albero;
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
        
        Utente U = datalayer.getUtente(2);
        List<Nodo> albero = creaAlbero(datalayer, U);
        Map template_data = new HashMap();
        template_data.put("alberoprofondita", albero);
        template_data.put("outline_tpl", "");
        TemplateResult tr = new TemplateResult(getServletContext());
        tr.activate("albero_sito.ftl.html", template_data, response);
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
            Logger.getLogger(user_editing.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(user_editing.class.getName()).log(Level.SEVERE, null, ex);
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
