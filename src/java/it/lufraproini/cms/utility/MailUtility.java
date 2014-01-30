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
package it.lufraproini.cms.utility;


import java.io.UnsupportedEncodingException;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author fsfskittu
 */
public class MailUtility {
    public static void sendMail(String mitt, String nome, String dest, String oggetto, String testo) throws MessagingException{
        //Creazione di una Mail Session
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.googlemail.com");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth", "true");
        /*Authenticator è astratta perciò va ridefinita*/
        Authenticator auth = new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication("franciskittu","ciccupampano");
            }
        };
        Session session = Session.getDefaultInstance(props, auth);
        
        //Creazione del messaggio da inviare
        
        MimeMessage message = new MimeMessage(session);
        message.setSubject(oggetto);
        /*message.setText(testo);*/
        message.setContent(testo, "text/html");

        //Aggiunta degli indirizzi del mittente e del destinatario
        InternetAddress from = null;
        try {
            if(!nome.equals("")){
                from = new InternetAddress(mitt, nome);
            } else {
                from = new InternetAddress(mitt);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MailUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        InternetAddress to = new InternetAddress(dest);
        message.setFrom(from);
        message.setRecipient(Message.RecipientType.TO, to);
        
        //invio
        Transport.send(message);
    }
    
}
