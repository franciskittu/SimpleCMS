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
import it.lufraproini.cms.model.Css;
import it.lufraproini.cms.model.impl.CMSDataLayerImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author fsfskittu
 */
public class AggiungiCSS extends HttpServlet {

    private FileItem fi = null;
    private List<String> prendiInfo(HttpServletRequest request)throws FileUploadException {
        List<String> data = new ArrayList();
        String nome = "";
        String descrizione = "";
        String file = "";

        if (ServletFileUpload.isMultipartContent(request)) {
            //Apache FileUpload
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items;
            items = upload.parseRequest(request);
            for (FileItem item : items) {
                String name = item.getFieldName();
                if (name.equals("nome")) {
                    nome = item.getString();
                } else if(name.equals("descrizione")){
                    descrizione = item.getString();
                } else if(name.equals("file")){
                    file = item.getString();
                } else if(name.equals("file_to_upload")){
                    this.fi = item;
                }
            }
            data.add(nome);
            data.add(descrizione);
            data.add(file);
            return data;
        }
        return null;
    }
    private FileItem prendiFile(HttpServletRequest request) throws FileUploadException {
        if (ServletFileUpload.isMultipartContent(request)) {
            //Apache FileUpload
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items;
            items = upload.parseRequest(request);
            for (FileItem item : items) {
                String nome = item.getFieldName();
                if (!item.isFormField() && nome.equals("file_to_upload") && item.getSize() > 0) {
                    return item;
                }
            }
        }
        return null;
    }
    
    private void action_upload(HttpServletRequest request, HttpServletResponse response, String nomefile) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {

        //creiamo un nuovo file (con nome univoco) e copiamoci il file scaricato
        File temp_file = File.createTempFile("upload_", "_temp", new File(getServletContext().getRealPath("css")));
        InputStream is = fi.getInputStream();
        OutputStream os = new FileOutputStream(temp_file);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = is.read(buffer)) > 0) {
            //durante la copia, aggreghiamo i byte del file nel digest sha-1
            //while copying, we aggregate the file bytes in the sha-1 digest
            os.write(buffer, 0, read);
        }
        is.close();
        os.close();      
        
        File uploaded_file = new File(getServletContext().getRealPath("css") + File.separatorChar + nomefile + ".css");
        if(!uploaded_file.exists()){
            temp_file.renameTo(uploaded_file);
        } else {
            temp_file.delete();
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
            throws ServletException, IOException, Exception {
        
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        Connection connection = ds.getConnection();
        HashMap data = new HashMap();
        CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);
        Css Obj = null;
                
        List<String> info = prendiInfo(request);
        String nomefile= info.get(2);
        //FileItem file_to_upload = prendiFile(request);
        if (fi != null) {
            action_upload(request, response, nomefile);
        }
        nomefile +=".css";
        Obj = datalayer.createCSSWithData(info.get(0),info.get(1),getServletContext().getInitParameter("system.css_directory") + File.separatorChar +nomefile);
        //Obj.setNome(info.get(0));
        //Obj.setDescrizione(info.get(1));
        //Obj.setFile(getServletContext().getInitParameter("system.css_directory") + File.separatorChar +nomefile);
        
        Css U = datalayer.addCSS(Obj);
        data.put("css", U);
        data.put("identifier", U.getID());
        
        //riazzeriamo l'outline
        data.put("outline_tpl","");
        
        TemplateResult tr = new TemplateResult(getServletContext());
        tr.activate("show_css.ftl.html", data, response);
        
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
        } catch (Exception ex) {
            Logger.getLogger(AggiungiCSS.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (Exception ex) {
            Logger.getLogger(AggiungiCSS.class.getName()).log(Level.SEVERE, null, ex);
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
