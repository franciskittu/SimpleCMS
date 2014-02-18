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
 */
public class cover extends HttpServlet {

    private List cambia_cover(CMSDataLayerImpl datalayer, Utente U, long id) throws ErroreGrave {
        List id_old__id_new = new ArrayList();
        List<Immagine> user_imgs = datalayer.getAllUsersImages(U);
        Immagine cover_nuova = null;
        Immagine cover_vecchia = null;
        for (Immagine img : user_imgs) {
            if (img.getCover()) {
                //vecchia cover
                id_old__id_new.add(img.getId());
                cover_vecchia=img;
                cover_vecchia.setCover(false);
            }
            if (img.getId() == id) {
                cover_nuova = img;
                cover_nuova.setCover(true);
            }
        }
        if (cover_vecchia == null) {
            id_old__id_new.add(0);
        }
        if (cover_nuova != null) {
            id_old__id_new.add(id);
            datalayer.updateImmagine(cover_nuova);
            if(cover_vecchia != null){
                datalayer.updateImmagine(cover_vecchia);
            }
        } else {
            throw new ErroreGrave("L'immagine non appartiene all'utente " + U.getUsername() + " !");
        }

        return id_old__id_new;
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

                Map template_data = new HashMap();

                if (request.getParameter("id") != null) {
                    long id;
                    try {
                        id = SecurityLayer.checkNumeric(request.getParameter("id"));
                    } catch (NumberFormatException ex) {
                        throw new ErroreGrave("il parametro inviato non è un numero!");
                    }
                    Utente U = datalayer.getUtente(SecurityLayer.checkNumeric(s.getAttribute("userid").toString()));
                    List ids = cambia_cover(datalayer, U, id);
                    if (request.getParameter("json") != null) {
                        template_data.put("outline_tpl", "");
                        template_data.put("img_old", ids.get(0));
                        template_data.put("img_current", ids.get(1));
                        TemplateResult tr = new TemplateResult(getServletContext());
                        tr.activate("account_ajax.ftl.json", template_data, response);
                    } else {
                        response.sendRedirect("visualizza?pagina=account");
                    }
                }
            } catch (ErroreGrave ex) {
                Logger.getLogger(cover.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            } catch (SQLException ex) {
                Logger.getLogger(cover.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            }
        } else {
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
