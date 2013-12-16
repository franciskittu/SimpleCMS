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

import it.lufraproini.cms.framework.result.TemplateResult;
import it.lufraproini.cms.model.Immagine;
import it.lufraproini.cms.model.Utente;
import it.lufraproini.cms.model.impl.CMSDataLayerImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author fsfskittu
 */
public class Upload extends HttpServlet {

    private String estensione;
    /*FUNZIONE GENERICA PER PRENDERE TUTTE LE INFORMAZIONI E IL FILE NELLE FORM DI UPLOAD*/
    private Map prendiInfo(HttpServletRequest request) throws FileUploadException{
        Map info = new HashMap();
        Map files = new HashMap();
        
        /*riutilizzo codice prof. Della Penna per l'upload*/
        if (ServletFileUpload.isMultipartContent(request)) {
            /* Funzioni delle librerie Apache per l'upload*/
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items;
            items = upload.parseRequest(request);
            /**/
            for (FileItem item : items) {
                String name = item.getFieldName();
                if(name.startsWith("file_to_upload")){/*le form che prevedono l'upload di un file devono avere il campo del file chiamato in questo modo*/
                    files.put(name, item);
                } else {
                    info.put(name, item.getString());
                }
            }
            info.put("files", files);
            return info;
        }
        return null;
    }
    
    private String action_upload(HttpServletRequest request, Map data) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {

        FileItem fi = null;
        String sdigest = "";
        String cartella = "";
        String tipo_file = data.get("submit").toString();
        String estensione = "";
        Map files = new HashMap();
        files = (HashMap) data.get("files");
        
        if(tipo_file.equals("immagine")){
            cartella = getServletContext().getInitParameter("system.image_directory");
        } else if (tipo_file.equals("css")){
            cartella = getServletContext().getInitParameter("system.css_directory");
        } else {
            throw new UnsupportedOperationException("i tipi di file che si vogliono inviare al server non sono ancora supportati");
        }
        
        fi = (FileItem) files.get("file_to_upload");
        String nome = fi.getName();
        if(nome != null){
             estensione = FilenameUtils.getExtension(FilenameUtils.getName(nome));
        }            
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        File temp_file = File.createTempFile("upload_", "_temp", new File(getServletContext().getRealPath(cartella)));
        InputStream is = fi.getInputStream();
        OutputStream os = new FileOutputStream(temp_file);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = is.read(buffer)) > 0) {
            //durante la copia, aggreghiamo i byte del file nel digest sha-1
            os.write(buffer, 0, read);
            md.update(buffer, 0, read);
        }
        is.close();
        os.close();      
        
        //covertiamo il digest in una stringa
        byte[] digest = md.digest();
        for (byte b : digest) {
            sdigest += String.valueOf(b);
        }
            
        String nomefile = "";
        this.estensione = estensione;
        if(estensione.equals("css")){
            nomefile = data.get("nome").toString();
        } else {
            nomefile = sdigest;
        }
        File uploaded_file = new File(getServletContext().getRealPath(cartella) + File.separatorChar + nomefile + "."+estensione);
        if(!uploaded_file.exists()){
            temp_file.renameTo(uploaded_file);
        } else {
            temp_file.delete();
        }
        return sdigest;
    }
    
    private Immagine memorizzaImmagine(Map data, CMSDataLayerImpl datalayer, String digest) throws SQLException{
        Map files = new HashMap();
        FileItem fi = null;
        files = (HashMap) data.get("files");
        fi = (FileItem) files.get("file_to_upload");
        /*costruzione current timestamp*/
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp current_timestamp = new Timestamp(now.getTime());
        /**/
        Utente U = datalayer.getUtentebyUsername("franciskittu");
        Immagine img_upload = datalayer.createImmagine();
        Immagine img_result;
        //se l'utente non ha spazio a sufficienza per fare l'upload questo viene rifiutato
        long spazio_risultante = U.getSpazio_disp_img() -fi.getSize();
        if(spazio_risultante < 0){
            return null;
        }
        img_upload.setNome(data.get("nome").toString());
        img_upload.setDimensione(fi.getSize());
        img_upload.setFile(getServletContext().getInitParameter("system.image_directory") + File.separatorChar + digest + "." + estensione);
        img_upload.setDigest(digest);
        img_upload.setData_upload(current_timestamp);
        img_upload.setUtente(U);
        img_result = datalayer.addImmagine(img_upload);
        if(img_result != null){
            U.setSpazio_disp_img(spazio_risultante);
            U = datalayer.updateUtente(U);
            if(U != null){
                return img_result;
            } else {
                datalayer.deleteImmagine(img_result);
                return null;
            }
        }
        return null;
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
            throws ServletException, IOException, FileUploadException, SQLException, Exception {
        /*DBMS*/
        DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        Connection connection = ds.getConnection();
        /**/
        CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);
        String digest = "";
        String html = "";
        String tipo = "";
        Map template_data = new HashMap();
        Map info = new HashMap();
        info = prendiInfo(request);
        tipo = info.get("submit").toString();
        if(tipo.equals("immagine")){
            Immagine img;
            html = "show_immagine.ftl.html";
            digest = action_upload(request, info);
            img = memorizzaImmagine(info, datalayer, digest);
            if(img != null){
                template_data.put("immagine",img);
                template_data.put("id", img.getID());
                template_data.put("id_utente", 2);
            }
        } else {
            html = "show_css.ftl.html";
            //inserisciCss();
        }
        template_data.put("outline_tpl", "");
        TemplateResult tr = new TemplateResult(getServletContext());
        tr.activate(html, template_data, response);
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
            Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
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