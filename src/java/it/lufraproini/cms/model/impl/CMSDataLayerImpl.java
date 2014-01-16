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
import it.lufraproini.cms.model.Slide;
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
    private PreparedStatement gPagina, aPagina, dPagina;
    private PreparedStatement gSito, aSito, dSito;
    private PreparedStatement gSlide, aSlide, uSlide, dSlide;
    
    public CMSDataLayerImpl(Connection c) throws SQLException{
        gCss = c.prepareStatement("SELECT * FROM css WHERE id = ?");
        aCss = c.prepareStatement("INSERT INTO css (nome, descrizione, file) VALUES (?,?,?) RETURNING id");
        uCss = c.prepareStatement("UPDATE css SET nome=?, descrizione=?,file=? WHERE id = ?");
        dCss = c.prepareStatement("DELETE FROM css WHERE id = ?");
        gUtente = c.prepareStatement("SELECT * FROM utente WHERE id = ?");
        gUtente_by_Username = c.prepareStatement("SELECT * FROM utente WHERE username = ?");
        aUtente = c.prepareStatement("INSERT INTO utente (username,password,nome,cognome,email,data_di_nascita,spazio_disp_img) VALUES (?,?,?,?,?,?,?) RETURNING id");
        uUtente = c.prepareStatement("UPDATE utente SET username=?, password=?, nome=?, cognome=?, email=?, data_di_nascita=?, spazio_disp_img=? WHERE id = ?");
        dUtente = c.prepareStatement("DELETE FROM utente WHERE id = ?");
        gImmagine = c.prepareStatement("SELECT * FROM immagine WHERE id = ?");
        aImmagine = c.prepareStatement("INSERT INTO immagine (nome,dimensione,file,digest,data_upload,id_utente) VALUES (?,?,?,?,?,?) RETURNING id");
        dImmagine = c.prepareStatement("DELETE FROM immagine WHERE id = ?");
        uImmagine = c.prepareStatement("UPDATE immagine SET nome = ?, dimensione = ?, file = ?, digest = ?, data_upload = ?, id_utente = ?");
        gImmagini = c.prepareStatement("SELECT * FROM immagine WHERE id_utente = ?");
        gPagina = c.prepareStatement("SELECT * FROM pagina WHERE id = ?");
        aPagina = c.prepareStatement("INSERT INTO pagina (titolo, body, id_padre, id_sito, modello) VALUES(?,?,?,?,?) RETURNING id");
        dPagina = c.prepareStatement("DELETE FROM pagina WHERE id = ?");
        aSito = c.prepareStatement("INSERT INTO sito (descrizione, header, footer, id_utente, homepage, id_css) VALUES (?,?,?,?,?,?)RETURNING id");
        gSito = c.prepareStatement("SELECT * FROM sito WHERE id = ?");
        gSlide = c.prepareStatement("SELECT * FROM slide WHERE id = ?");
        aSlide = c.prepareStatement("INSERT INTO slide (descrizione, posizione, file) VALUES(?,?,?) RETURNING id");
        uSlide = c.prepareStatement("UPDATE slide SET descrizione = ?, posizione = ?, file = ?");
        dSlide = c.prepareStatement("DELETE FROM slide WHERE id = ?");
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
    
    public Utente getUtentebyUsername(String username){
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
            aImmagine.setString(3,img.getFile());
            aImmagine.setString(4,img.getDigest());
            aImmagine.setTimestamp(5,img.getData_upload());
            aImmagine.setLong(6,img.getUtente().getID());
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
        return new PaginaImpl(this);
    }

    @Override
    public Pagina addPagina(Pagina p) {
        ResultSet chiave = null;
        PaginaImpl p_agg = (PaginaImpl) p;
        try{
            aPagina.setString(1, p_agg.getTitolo());
            aPagina.setString(2,p_agg.getBody());
            aPagina.setLong(3,p_agg.getCss().getID());
            aPagina.setLong(4,p_agg.getPadre().getID());
            aPagina.setLong(5,p_agg.getSito().getID());
            aPagina.setBoolean(6,p_agg.getModello());
            chiave = aPagina.executeQuery();
            if(chiave.next()){
                return getPagina(chiave.getLong("id"));
            }
        } catch (SQLException ex){
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
    public Pagina deletePagina(Pagina p) {
        try{
            dPagina.setLong(1,p.getID());
            if(dPagina.executeUpdate() == 1){
                return p;
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
    public Pagina getHomepage(long i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pagina getPagina(long i){
        ResultSet rs = null;
        Pagina ris = null;
        try{
            gPagina.setLong(1, i);
            rs = gPagina.executeQuery();
            if(rs.next()){
                ris = new PaginaImpl(this, rs);
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
    public Sito createSito() {
        return new SitoImpl(this);
    }

    @Override
    public Sito addSito(Sito s) {
        SitoImpl s_agg = (SitoImpl) s;
        ResultSet chiave = null;
        try{
            aSito.setString(1,s_agg.getHeader());
            aSito.setString(2,s_agg.getFooter());
            aSito.setLong(3,s_agg.getUtente().getID());
            aSito.setLong(4,s_agg.getHomepage().getID());
            chiave = aSito.executeQuery();
            if(chiave.next()){
                return getSito(chiave.getLong("id"));
            }
        } catch (SQLException ex){
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
    public Sito deleteSito(Sito s) {
        try{
            dSito.setLong(1,s.getID());
            if(dSito.executeUpdate() == 1){// il DBMS eliminerà le tuple corrispondenti alle pagine del sito
                return s;
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Sito updateSito(Sito s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Sito> getSitobyUtente(Utente U) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Sito getSito(long i){
        ResultSet rs = null;
        Sito ris = null;
        try{
            gSito.setLong(1, i);
            rs = gSito.executeQuery();
            if(rs.next()){
                ris = new SitoImpl(this, rs);
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
    public Slide createSlide(){
        return new SlideImpl(this);
    }
    
    @Override
    public Slide addSlide(Slide s){
        ResultSet chiave = null;
        SlideImpl s_agg = (SlideImpl) s;
        try{
            aSlide.setString(1,s_agg.getDescrizione());
            aSlide.setLong(2, s_agg.getPosizione());
            aSlide.setString(3,s_agg.getFile());
            chiave = aSlide.executeQuery();
            if(chiave.next()){
                return getSlide(chiave.getLong("id"));
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
    
    private void deleteSlideFile(Slide S){
        if(S != null){
            File f = new File(S.getFile());// forse da fare getRealPath
            if(f.exists()){
                f.delete();
            }
        }
    }
    
    @Override
    public Slide deleteSlide(Slide s){
        try{
            dSlide.setLong(1, s.getID());
            if(dSlide.executeUpdate() == 1){
                deleteSlideFile(s);
                return s;
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    @Override
    public Slide updateSlide(Slide s){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Slide getSlide(long i){
        ResultSet rs = null;
        Slide ris = null;
        try{
            gSlide.setLong(1, i);
            rs = gSlide.executeQuery();
            if(rs.next()){
                ris = new SlideImpl(this, rs);
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
    
}
