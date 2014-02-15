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
import it.lufraproini.cms.model.Immagine;
import it.lufraproini.cms.model.Utente;
import it.lufraproini.cms.model.impl.CMSDataLayerImpl;
import it.lufraproini.cms.utility.ErroreGrave;
import it.lufraproini.cms.utility.SecurityLayer;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
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
import javax.imageio.ImageIO;
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
public class upload_user_img extends HttpServlet {

    //funzione che prende il campo file dalla form e crea una Map con tutte le informazioni sul file
    //necessarie per effettuare correttamente tutte le operazioni di upload
    private Map prendiInfoFile(HttpServletRequest request) throws ErroreGrave, IOException {
        Map infofile = new HashMap();
        //riutilizzo codice prof. Della Penna per l'upload
        if (ServletFileUpload.isMultipartContent(request)) {
            // Funzioni delle librerie Apache per l'upload
            try {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items;
                FileItem file = null;

                items = upload.parseRequest(request);
                for (FileItem item : items) {
                    String name = item.getFieldName();
                    if (name.equals("file")) {
                        file = item;
                        break;
                    }
                }
                if (file == null) {
                    throw new ErroreGrave("la form non ha inviato il campo file!");
                } else {
                    //informazioni
                    String nome_e_path = file.getName();
                    String estensione = FilenameUtils.getExtension(FilenameUtils.getName(nome_e_path));
                    String nome_senza_estensione = FilenameUtils.getBaseName(FilenameUtils.getName(nome_e_path));
                    infofile.put("nome_completo", nome_senza_estensione + "." + estensione);
                    infofile.put("estensione", estensione);
                    infofile.put("nome_senza_estensione", nome_senza_estensione);
                    infofile.put("dimensione", file.getSize());
                    infofile.put("input_stream", file.getInputStream());
                    infofile.put("content_type", file.getContentType());
                }

            } catch (FileUploadException ex) {
                Logger.getLogger(upload_user_img.class.getName()).log(Level.SEVERE, null, ex);
                throw new ErroreGrave("errore libreria apache!");
            }

        }
        return infofile;
    }

    //funzione (richiamata da memorizzaImmagine) che calcola il digest del file
    //e lo memorizza nel file system
    private void uploadImmagine(Map info) throws ErroreGrave {
        String cartella = getServletContext().getInitParameter("system.image_directory");
        String sdigest = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            File temp_file = File.createTempFile("upload_", "_temp", new File(getServletContext().getRealPath(cartella)));
            InputStream is = (InputStream) info.get("input_stream");
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
            //aggiungo il digest alle informazioni sul file
            info.put("digest", sdigest);
            //L'immagine sarà salvata con il digest + l'estensione del file
            File uploaded_file = new File(getServletContext().getRealPath(cartella) + File.separatorChar + sdigest + "." + info.get("estensione"));
            if (!uploaded_file.exists()) {
                temp_file.renameTo(uploaded_file);
            } else {
                temp_file.delete();
            }
        } catch (Exception ex) {
            throw new ErroreGrave("operazioni sul file!");
        }
    }

    //
    private Boolean creaThumbnail(Immagine img, Map info) {
        String cartella_img = getServletContext().getInitParameter("system.image_directory");
        String file_img = img.getFile().substring(img.getFile().indexOf("/"));
        String path = getServletContext().getRealPath(cartella_img);
        String cartella_thumb = getServletContext().getInitParameter("system.thumb_directory");
        int altezza_thumb = 120;
        int larghezza_thumb = 120;
        BufferedImage thumb = new BufferedImage(larghezza_thumb, altezza_thumb, BufferedImage.TYPE_INT_RGB);
        String file_thumb = info.get("digest")+"_thumbnail." + info.get("estensione");
        
        try {
            if (ImageIO.read(new File(path + file_img)).getWidth() >= larghezza_thumb) {
                thumb.createGraphics().drawImage(ImageIO.read(new File(path + file_img)).getScaledInstance(-1, altezza_thumb, Image.SCALE_SMOOTH), 0, 0, null);
            } else {
                thumb.createGraphics().drawImage(ImageIO.read(new File(path + file_img)).getScaledInstance(larghezza_thumb, altezza_thumb, Image.SCALE_SMOOTH), 0, 0, null);
            }
            
            ImageIO.write(thumb, info.get("estensione").toString(), new File(path + File.separatorChar + info.get("digest")+"_thumbnail." + info.get("estensione")));
        } catch (IOException ex) {
            return false;
        }
        img.setThumb(cartella_thumb + File.separatorChar + file_thumb);
        return true;
    }

    //funzione che memorizza l'immagine sul DB ritornando null se l'utente ha spazio sufficiente,
    //altrimenti ritorna una stringa di errore senza fare alcuna operazione
    private String memorizzaImmagine(CMSDataLayerImpl datalayer, Map info, Utente U) throws ErroreGrave {
        //controllo spazio disponibile per l'utente
        long spazio_risultante = U.getSpazio_disp_img() - (Long) info.get("dimensione");
        if (spazio_risultante < 0) {
            return "Non si ha abbastanzia spazio disponibile per caricare l'immagine!"
                    + " Byte disponibili: " + U.getSpazio_disp_img()
                    + " - Byte immagine: " + info.get("dimensione");
        }
        //memorizzazione dell'immagine su file system e calcolo digest
        uploadImmagine(info);

        //costruzione current timestamp
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp current_timestamp = new Timestamp(now.getTime());
        //
        Immagine img_upload = datalayer.createImmagine();
        Immagine result;
        //creazione thumbnail
        creaThumbnail(img_upload,info);
        
        img_upload.setNome(SecurityLayer.addSlashes(info.get("nome_senza_estensione").toString()));
        img_upload.setDimensione((Long) info.get("dimensione"));
        img_upload.setTipo(info.get("content_type").toString());
        img_upload.setFile(getServletContext().getInitParameter("system.image_directory") + File.separatorChar + info.get("digest") + "." + info.get("estensione"));
        img_upload.setDigest(info.get("digest").toString());
        img_upload.setData_upload(current_timestamp);
        img_upload.setUtente(U);
        //memorizazione su DB
        result = datalayer.addImmagine(img_upload);
        if (result == null) {
            throw new ErroreGrave("Impossibile inserire i dati dell'immagine nel DB!");
        }
        U.setSpazio_disp_img(spazio_risultante);
        Utente ris = datalayer.updateUtente(U);
        if (ris == null) {
            //elimina l'immagine con la thumbnail sul file system e sul DB
            if (datalayer.deleteImmagine(result) == null) {
                throw new ErroreGrave("impossibile eliminare il record dell'immagine dal database!");
            }
            throw new ErroreGrave("Non è stato possibile aggiornare lo spazio immagine dell'utente!");
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
            throws ServletException, IOException {

        //verifica validità sessione
        HttpSession s = SecurityLayer.checkSession(request);
        if (s != null) {
            try {
                //DBMS
                DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
                Connection connection = ds.getConnection();

                CMSDataLayerImpl datalayer = new CMSDataLayerImpl(connection);

                Utente U = datalayer.getUtente(SecurityLayer.checkNumeric(s.getAttribute("userid").toString()));
                Map infoFile = prendiInfoFile(request);
                String spazio_non_sufficiente = memorizzaImmagine(datalayer, infoFile, U);
                if (spazio_non_sufficiente == null) {
                    response.sendRedirect("visualizza?pagina=account");
                } else {
                    response.sendRedirect("visualizza?pagina=account&err=" + spazio_non_sufficiente);
                }

            } catch (ErroreGrave ex) {
                Logger.getLogger(upload_user_img.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            } catch (SQLException ex) {
                Logger.getLogger(upload_user_img.class.getName()).log(Level.SEVERE, null, ex);
                FailureResult res = new FailureResult(getServletContext());
                res.activate(ex.getMessage(), request, response);
            }
        } else {
            response.sendRedirect("visualizza?pagina=home&err=auth");
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
