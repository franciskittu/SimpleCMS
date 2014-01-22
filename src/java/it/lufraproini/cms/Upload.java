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
import it.lufraproini.cms.framework.result.TemplateResult;
import it.lufraproini.cms.model.Css;
import it.lufraproini.cms.model.Immagine;
import it.lufraproini.cms.model.Slide;
import it.lufraproini.cms.model.Utente;
import it.lufraproini.cms.model.impl.CMSDataLayerImpl;
import it.lufraproini.cms.security.SecurityLayer;
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
import javax.servlet.http.HttpSession;
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
    private String nomefile;
    private String error_message;
    private final String caratteri_non_ammessi = "['\"/\\\\]";//slash, apici singoli e apici doppi

    /*FUNZIONE GENERICA PER PRENDERE TUTTI I CAMPI E IL FILE NELLE FORM DI UPLOAD*/
    /*IL CAMPO DEL FILE DEVE INIZIARE PER file_to_upload*/
    private Map prendiInfo(HttpServletRequest request) throws FileUploadException {
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
                /*le form che prevedono l'upload di un file devono avere il campo del file chiamato in questo modo*/
                if (name.startsWith("file_to_upload")) {
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

    /*questa funzione*/
    private String action_upload(HttpServletRequest request, Map data) throws SQLException, IOException, NamingException, NoSuchAlgorithmException, Exception {

        FileItem fi = null;
        String sdigest = "";
        String cartella = "";
        String tipo_file = data.get("submit").toString();
        String estensione = "";
        String campi_errati = "";
        Map files = new HashMap();
        files = (HashMap) data.get("files");
        campi_errati = checkfields(data);
        if (!campi_errati.equals("")) {
            error_message = "I seguenti campi contengono valori non ammessi: " + campi_errati;
            return null;
        }

        if (tipo_file.equals("immagine")) {
            cartella = getServletContext().getInitParameter("system.image_directory");
        } else if (tipo_file.equals("css")) {
            cartella = getServletContext().getInitParameter("system.css_directory");
        } else if (tipo_file.equals("slide")) {
            cartella = getServletContext().getInitParameter("system.slide_directory");
        } else {
            throw new UnsupportedOperationException("i tipi di file che si vogliono inviare al server non sono ancora supportati");
        }

        fi = (FileItem) files.get("file_to_upload");
        String nome = fi.getName();
        if (nome != null) {
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

        this.estensione = estensione;
        if (estensione.equals("css")) {
            this.nomefile = data.get("nome").toString();
        } else {
            this.nomefile = sdigest;
        }
        //L'immagine sarà salvata con il digest + l'estensione del file mentre il css sarà salvato con il nome inserito nella form
        File uploaded_file = new File(getServletContext().getRealPath(cartella) + File.separatorChar + this.nomefile + "." + estensione);
        if (!uploaded_file.exists()) {
            temp_file.renameTo(uploaded_file);
        } else {
            temp_file.delete();
        }
        return sdigest;
    }

    private Immagine memorizzaImmagine(Map data, CMSDataLayerImpl datalayer, String digest, long id_user) throws SQLException {
        Map files = new HashMap();
        FileItem fi = null;
        files = (HashMap) data.get("files");
        fi = (FileItem) files.get("file_to_upload");
        /*costruzione current timestamp*/
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp current_timestamp = new Timestamp(now.getTime());
        /**/
        Utente U = datalayer.getUtente(id_user);
        Immagine img_upload = datalayer.createImmagine();
        Immagine img_result;
        //se l'utente non ha spazio a sufficienza per fare l'upload questo viene rifiutato
        long spazio_risultante = U.getSpazio_disp_img() - fi.getSize();
        if (spazio_risultante < 0) {
            //viene eliminato il file appena memorizzato in action_upload
            File uploaded_file = new File(getServletContext().getRealPath(getServletContext().getInitParameter("system.image_directory")) + File.separatorChar + this.nomefile + "." + this.estensione);
            if (uploaded_file.exists()) {
                uploaded_file.delete();
            }
            error_message = "L'utente non ha spazio disponibile sufficiente per caricare l'immagine!";
            error_message += " Byte disponibili: " + U.getSpazio_disp_img();
            error_message += " - Byte immagine: " + fi.getSize();
            return null;
        }
        img_upload.setNome(SecurityLayer.addSlashes(data.get("nome").toString()));
        img_upload.setDimensione(fi.getSize());
        img_upload.setTipo(fi.getContentType());
        img_upload.setFile(getServletContext().getInitParameter("system.image_directory") + File.separatorChar + digest + "." + estensione);
        img_upload.setDigest(digest);
        img_upload.setData_upload(current_timestamp);
        img_upload.setUtente(U);
        img_result = datalayer.addImmagine(img_upload);
        // aggiornamento spazio immagini utente
        if (img_result != null) {
            U.setSpazio_disp_img(spazio_risultante);
            U = datalayer.updateUtente(U);
            if (U != null) {
                return img_result;
            } else {
                datalayer.deleteImmagine(img_result);
                error_message = "Non è stato possibile aggiornare lo spazio immagine dell'utente!";
                return null;
            }
        }
        error_message = "Impossibile inserire i dati dell'immagine nel DB!";
        return null;
    }

    private Css memorizzaCss(Map data, CMSDataLayerImpl datalayer) {
        Map files = new HashMap();
        FileItem fi = null;
        files = (HashMap) data.get("files");
        fi = (FileItem) files.get("file_to_upload");
        Css css_upload = datalayer.createCSS();
        Css css_result;

        css_upload.setFile(getServletContext().getInitParameter("system.css_directory") + File.separatorChar + data.get("nome").toString() + "." + estensione);
        css_upload.setNome(SecurityLayer.addSlashes(data.get("nome").toString()));
        if (data.get("descrizione").toString() != null) {
            css_upload.setDescrizione(data.get("descrizione").toString());
        }
        css_result = datalayer.addCSS(css_upload);
        if (css_result == null) {
            error_message = "Impossibile aggiungere dati del file css al DB!";
        }
        return css_result;
    }

    private Slide memorizzaSlide(Map data, CMSDataLayerImpl datalayer, String digest){
        Map files = new HashMap();
        FileItem fi = null;
        files = (HashMap) data.get("files");
        fi = (FileItem) files.get("file_to_upload");
        Slide slide_upload = datalayer.createSlide();
        Slide slide_result;
        /*i controlli sulla validità dei campi sono già stati effettuati dalla funzione checkfields, pertanto è necessario soltanto utilizzare le funzioni per l'SQL INJECTION*/
        slide_upload.setFile(getServletContext().getInitParameter("system.slide_directory") + File.separatorChar + digest + "." + estensione);
        slide_upload.setDescrizione(SecurityLayer.addSlashes(data.get("descrizione").toString()));
        slide_upload.setPosizione(Integer.parseInt(data.get("posizione").toString()));
        slide_upload.setNome(SecurityLayer.addSlashes(data.get("nome").toString()));
        
        slide_result = datalayer.addSlide(slide_upload);
        if(slide_result == null){
            error_message = "Impossibile aggiungere dati del file css al DB!";
        }
        return slide_result;
    }
    
    /*funzione che ritorna una stringa contenente tutti i campi considerati errati nella form*/
    private String checkfields(Map fields) {
        String campi_errati = "";
        /*immagini, css e slide*/
        if (fields.containsKey("nome") && (
                fields.get("nome") == null || fields.get("nome").toString().matches(".*" + caratteri_non_ammessi + ".*")
                ) 
           ) {
            campi_errati += " nome ";
        }
        /*slide e css*/
        if (fields.containsKey("descrizione")) {
            if (fields.get("descrizione").toString().equals("")) {
                campi_errati += " descrizione ";
            }
        }
        /*necessario per slide*/
        if (fields.containsKey("posizione")){
            if(!fields.get("posizione").toString().equals("")){
                try{
                    SecurityLayer.checkNumeric(fields.get("posizione").toString());
                } catch (NumberFormatException ex){
                    campi_errati += " posizione ";
                }
            } else {
                campi_errati += " posizione ";
            }
        }
        return campi_errati;
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws org.apache.commons.fileupload.FileUploadException
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileUploadException, SQLException, Exception {
        /*verifica validità sessione*/
        HttpSession s = SecurityLayer.checkSession(request);
        if (s == null) {

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
            /*per prevedere l'upload di un nuovo tipo di file basta dare un valore personalizzato al submit nella form
            e creare un if con il flusso relativo all'oggetto da caricare nel file system e nel database*/
            tipo = info.get("submit").toString();
            try {
                /*flusso immagine*/
                if (tipo.equals("immagine")) {
                    Immagine img;
                    html = "show_immagine.ftl.html";
                    digest = action_upload(request, info);
                    digest.getBytes(); //potrebbe generare una nullpointerexception
                    img = memorizzaImmagine(info, datalayer, digest, (Long) s.getAttribute("userid"));
                    img.setNome(SecurityLayer.stripSlashes(img.getNome()));//potrebbe causare eccezione su img è null
                    template_data.put("immagine", img);
                    template_data.put("id", img.getID());
                    template_data.put("id_utente", 2);
                } /*flusso css*/ else if(tipo.equals("css")){
                    Css css = null;
                    html = "show_css.ftl.html";
                    action_upload(request, info).getBytes();//potrebbe generare una nullpointerexception
                    css = memorizzaCss(info, datalayer);
                    css.setDescrizione(SecurityLayer.stripSlashes(css.getDescrizione()));//potrebbe generare una nullpointerexception
                    css.setNome(SecurityLayer.stripSlashes(css.getNome()));
                    template_data.put("css", css);
                    template_data.put("identifier", css.getID());
                } /*flusso slide*/else if(tipo.equals("slide")){
                    Slide img = null;
                    html = "show_slide.ftl.html";
                    digest = action_upload(request, info);
                    digest.getBytes();//potrebbe generare una nullpointerexception
                    img = memorizzaSlide(info, datalayer, digest);
                    /*visualizzazione della slide appena inserita*/
                    img.setDescrizione(SecurityLayer.stripSlashes(img.getDescrizione()));
                    img.setNome(SecurityLayer.stripSlashes(img.getNome()));
                    template_data.put("slide", img);
                    template_data.put("id", img.getID());
                }
                template_data.put("outline_tpl", "");
                TemplateResult tr = new TemplateResult(getServletContext());
                tr.activate(html, template_data, response);
            } catch (NullPointerException ex) {
                FailureResult res = new FailureResult(getServletContext());
                res.activate(error_message, request, response);
            }
        } else {
            response.sendRedirect("Homepage.html");
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
            Logger.getLogger(Upload.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Upload.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Upload.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Upload.class
                    .getName()).log(Level.SEVERE, null, ex);
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
