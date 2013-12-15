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
package it.lufraproini.cms.model.impl;

import it.lufraproini.cms.model.CMSDataLayer;
import it.lufraproini.cms.model.Css;
import it.lufraproini.cms.model.Immagine;
import it.lufraproini.cms.model.Pagina;
import it.lufraproini.cms.model.Sito;
import it.lufraproini.cms.model.Utente;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CMSDataLayerImpl implements CMSDataLayer {
    
    private PreparedStatement gCss, aCss, uCss, dCss;
    private PreparedStatement gUtente, gUtente_by_Username, aUtente, uUtente, dUtente;
    private PreparedStatement gImmagine, gImmagini, aImmagine, uImmagine, dImmagine;

    public CMSDataLayerImpl(Connection c) throws SQLException{
        gCss = c.prepareStatement("SELECT * FROM css WHERE id = ?");
        aCss = c.prepareStatement("INSERT INTO css (nome, descrizione, file) VALUES (?,?,?) RETURNING id");
        uCss = c.prepareStatement("UPDATE css SET nome = ? AND descrizione = ? AND file = ? WHERE id = ?");
        dCss = c.prepareStatement("DELETE FROM css WHERE id = ?");
        gUtente = c.prepareStatement("SELECT * FROM utente WHERE id = ?");
        gUtente_by_Username = c.prepareStatement("SELECT * FROM utente WHERE usename = ?");
        aUtente = c.prepareStatement("INSERT INTO utente (username,password,nome,cognome,email,data_di_nascita,spazio_disp_img) VALUES (?,?,?,?,?,?,?)");
        uUtente = c.prepareStatement("UPDATE utente SET username = ? AND password = ? AND nome = ? AND cognome = ? AND email = ? AND data_di_nascita = ? AND spazio_disp_img = ? WHERE username = ?");
        dUtente = c.prepareStatement("DELETE FROM utente WHERE id = ?");
        gImmagine = c.prepareStatement("SELECT * FROM immagine WHERE id = ?");
        aImmagine = c.prepareStatement("INSERT INTO immagine (nome,dimensione,tipo,file,digest,data_upload,id_utente) VALUES (?,?,?,?,?,?,?)");
        dImmagine = c.prepareStatement("DELETE FROM immagine WHERE id = ?");
        uImmagine = c.prepareStatement("UPDATE immagine SET nome = ? AND dimensione = ? AND tipo = ? AND file = ? AND digest = ? AND data_upload = ? AND id_utente = ?");
        gImmagini = c.prepareStatement("SELECT * FROM immagine WHERE id_utente = ?");
    }
    
    @Override
    public Css createCSS() {
        return new CssImpl(this);
    }
    
    public Css createCSSWithData(String nome, String descrizione, String file){
        Css Obj = new CssImpl(this);
        Obj.setNome(nome);
        Obj.setDescrizione(descrizione);
        Obj.setFile(file);
        return Obj;
    }
    @Override
    public Css getCSS(long i){
        Css ris = null;
        ResultSet rs = null;
        try{
            gCss.setLong(1, i);
            rs = gCss.executeQuery();
            if(rs.next()){
                ris = new CssImpl(this,rs);
            }
        } catch (SQLException ex){
            java.util.logging.Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                rs.close();
            } catch (SQLException ex){
                
            }
        }
        return ris;
    }
    
    @Override
    public Css addCSS(Css O) {
        CssImpl ICss = (CssImpl) O;
        ResultSet chiave = null;
            try{
            aCss.setString(1, ICss.getNome());
            aCss.setString(2, ICss.getDescrizione());
            aCss.setString(3, ICss.getFile());
            chiave = aCss.executeQuery();
                if(chiave.next()){
                    return getCSS(chiave.getLong("id"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try{
                        chiave.close();
                    } catch (SQLException ex){
                    //
                }
            }
        return null;
    }
        

    @Override
    public Css updateCSS(Css O) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //funzione conseguenza di eliminazione di un css dal DB
    private void deleteCSSFile(Css O){
        if(O != null){
            File f = new File(O.getFile());// forse da fare getRealPath
            if(f.exists()){
                f.delete();
            }
        }
    }
    
    @Override
    public Css deleteCSS(Css O) {
        try {
            dCss.setLong(1, O.getID());
            if(dCss.executeUpdate() == 1){
                deleteCSSFile(O);
                return O;
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Utente createUtente() {
        return new UtenteImpl(this);
    }

    public Utente createUtenteWithData(String username, String password, String nome, String cognome, String email, long spazio_disp_img, Date data_di_nascita){
        Utente U = new UtenteImpl(this);
        U.setUsername(username);
        U.setPassword(password);
        U.setNome(nome);
        U.setCognome(cognome);
        U.setEmail(email);
        U.setData_di_nascita(data_di_nascita);
        U.setSpazio_disp_img(spazio_disp_img);
        return U;
    }
    
    public Utente getUtente(long i){
        ResultSet rs = null;
        Utente ris = null;
        try{
            gUtente.setLong(1, i);
            rs = gUtente.executeQuery();
            if(rs.next()){
                ris = new UtenteImpl(this, rs);
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                rs.close();
            } catch (SQLException ex){
                
            }
        }
        return ris;
    }
    
    private Utente getUtentebyUsername(String username){
        ResultSet rs = null;
        Utente ris = null;
        try{
            gUtente_by_Username.setString(1, username);
            rs = gUtente_by_Username.executeQuery();
            if(rs.next()){
                ris = new UtenteImpl(this, rs);
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                rs.close();
            } catch (SQLException ex){
                
            }
        }
        return ris;
    }
    
    @Override
    public Utente addUtente(Utente U) {
        ResultSet chiave = null;
        UtenteImpl IUtente= (UtenteImpl) U;
        try{
            aUtente.setString(1,IUtente.getUsername());
            aUtente.setString(2,IUtente.getPassword());
            aUtente.setString(3,IUtente.getNome());
            aUtente.setString(4,IUtente.getCognome());
            aUtente.setString(5,IUtente.getEmail());
            aUtente.setDate(6,IUtente.getData_di_nascita());
            aUtente.setLong(7,IUtente.getSpazio_disp_img());
            chiave = aUtente.executeQuery();
            if(chiave.next()){
                return getUtente(chiave.getLong("id"));
            } 
        } catch(SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                chiave.close();
            } catch (SQLException ex){
                
            }
        }
        return null;
    }

    @Override
    public Utente deleteUtente(Utente U) {
        try{
            dUtente.setLong(1,U.getID());
            if(dUtente.executeUpdate() == 1){
                return U;
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Utente updateUtente(Utente U) {
        try{
            uUtente.setString(1,U.getUsername());
            uUtente.setString(2,U.getPassword());
            uUtente.setString(3,U.getNome());
            uUtente.setString(4,U.getCognome());
            uUtente.setString(5, U.getEmail());
            uUtente.setDate(6, U.getData_di_nascita());
            uUtente.setLong(7,U.getSpazio_disp_img());
            uUtente.setLong(8,U.getID());
            if(uUtente.executeUpdate() == 1){
                return getUtente(U.getID());
            }
        } catch(SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean checkPassword(Utente U, String pwd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Immagine createImmagine() {
        return new ImmagineImpl(this);
    }

    public Immagine getImmagine(long i){
        ResultSet rs = null;
        Immagine ris = null;
        try{
            gImmagine.setLong(1, i);
            rs = gImmagine.executeQuery();
            if(rs.next()){
                ris = new ImmagineImpl(this, rs);
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                rs.close();
            } catch (SQLException ex){
                
            }
        }
        return ris;
    }
    @Override
    public Immagine addImmagine(Immagine I) {
        Immagine img = (ImmagineImpl) I;
        ResultSet chiave = null;
        try{
            aImmagine.setString(1,img.getNome());
            aImmagine.setLong(2,img.getDimensione());
            aImmagine.setString(3,img.getTipo());
            aImmagine.setString(4,img.getFile());
            aImmagine.setString(5,img.getDigest());
            aImmagine.setTimestamp(6,img.getData_upload());
            aImmagine.setLong(7,img.getUtente().getID());
            chiave = aImmagine.executeQuery();
            if(chiave.next()){
                return getImmagine(chiave.getLong("id"));
            }
        } catch(SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                chiave.close();
            } catch(SQLException ex){
                
            }
        }
        return null;
    }

    private void deleteImmagineFile(Immagine I){
        if(I != null){
            File f = new File(I.getFile());// forse da fare getRealPath
            if(f.exists()){
                f.delete();
            }
        }
    }
    
    @Override
    public Immagine deleteImmagine(Immagine I) {
        try{
            dImmagine.setLong(1,I.getID());
            if(dImmagine.executeUpdate() == 1){
                deleteImmagineFile(I);
                return I;
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Immagine updateImmagine(Immagine I) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Immagine getImgbyID(int id_img) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Immagine> getAllUsersImages(Utente U) {
        List<Immagine> ris = new ArrayList();
        ResultSet rs = null;
        try{
            gImmagini.setLong(1,U.getID());
            rs = gImmagini.executeQuery();
            while(rs.next()){
                ris.add(new ImmagineImpl(this, rs));
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                rs.close();
            } catch (SQLException ex){
                
            }
        }
        return ris;
    }

    @Override
    public Pagina createPagina() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pagina addPagina(Pagina p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pagina deletePagina(Pagina p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pagina updatePagina(Pagina p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Pagina> getFiglie(Pagina p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pagina getHomepage(Sito s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sito createSito() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sito addSito(Sito s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sito deleteSito(Sito s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Sito updateSito(Sito s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Sito> getSitobyUtente(Utente U) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
