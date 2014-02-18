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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CMSDataLayerImpl implements CMSDataLayer {
    
    private final PreparedStatement gCss, aCss, uCss, dCss;
    private final Statement gManualCss, gSitiDataOrdinati, gPagineModello;
    private final PreparedStatement gUtente, gUtente_by_Username, aUtente, uUtente, dUtente;
    private final PreparedStatement gImmagine, gImmagini, aImmagine, uImmagine, dImmagine;
    private final PreparedStatement gPagina, aPagina, dPagina, gFiglie, dSottoAlbero, gPaginabyTitolo, gHome, uPagina, aHome, uHome;
    private final PreparedStatement gSito, aSito, dSito, gSitobyUtente, uSito;
    private final PreparedStatement gSlide, aSlide, uSlide, dSlide;
    private final PreparedStatement gAntenati, gFoglie, gPagineSito, gCoverUtente;
    
    public CMSDataLayerImpl(Connection c) throws SQLException{
        gManualCss = c.createStatement();
        gSitiDataOrdinati = c.createStatement();
        gPagineModello = c.createStatement();
        gHome = c.prepareStatement("SELECT pagina.* FROM sito, pagina WHERE sito.id = ? AND pagina.id = homepage;");
        aHome = c.prepareStatement("INSERT INTO pagina (titolo, body, id_sito) VALUES(?,?,?) RETURNING id");
        uHome = c.prepareStatement("UPDATE pagina SET titolo = ?, body = ?, id_sito = ?, modello = ? WHERE id = ?");
        gCss = c.prepareStatement("SELECT * FROM css WHERE id = ?");
        aCss = c.prepareStatement("INSERT INTO css (nome, descrizione, file) VALUES (?,?,?) RETURNING id");
        uCss = c.prepareStatement("UPDATE css SET nome=?, descrizione=?,file=? WHERE id = ?");
        dCss = c.prepareStatement("DELETE FROM css WHERE id = ?");
        gUtente = c.prepareStatement("SELECT * FROM utente WHERE id = ?");
        gUtente_by_Username = c.prepareStatement("SELECT * FROM utente WHERE username = ?");
        aUtente = c.prepareStatement("INSERT INTO utente (username,password,nome,cognome,email,spazio_disp_img,codice_attivazione,attivato) VALUES (?,?,?,?,?,?,?,?) RETURNING id");
        uUtente = c.prepareStatement("UPDATE utente SET username=?, password=?, nome=?, cognome=?, email=?, spazio_disp_img=?, codice_attivazione=?, attivato=? WHERE id = ?");
        dUtente = c.prepareStatement("DELETE FROM utente WHERE id = ?");
        gImmagine = c.prepareStatement("SELECT * FROM immagine WHERE id = ?");
        aImmagine = c.prepareStatement("INSERT INTO immagine (nome,dimensione,tipo,file,digest,data_upload,id_utente,thumb) VALUES (?,?,?,?,?,?,?,?) RETURNING id");
        dImmagine = c.prepareStatement("DELETE FROM immagine WHERE id = ?");
        uImmagine = c.prepareStatement("UPDATE immagine SET nome = ?, dimensione = ?, tipo = ?, file = ?, digest = ?, data_upload = ?, id_utente = ?, thumb = ?, cover =? WHERE id = ?");
        gImmagini = c.prepareStatement("SELECT * FROM immagine WHERE id_utente = ?");
        gPagina = c.prepareStatement("SELECT * FROM pagina WHERE id = ?");
        aPagina = c.prepareStatement("INSERT INTO pagina (titolo, body, id_padre, id_sito, modello) VALUES(?,?,?,?,?) RETURNING id");
        uPagina = c.prepareStatement("UPDATE pagina SET titolo = ?, body = ?, id_padre = ?, id_sito = ?, modello = ? WHERE id = ?");
        dPagina = c.prepareStatement("DELETE FROM pagina WHERE id = ?");
        aSito = c.prepareStatement("INSERT INTO sito (header, footer, id_utente, nome, id_css) VALUES (?,?,?,?,?)RETURNING id");
        gSito = c.prepareStatement("SELECT * FROM sito WHERE id = ?");
        uSito = c.prepareStatement("UPDATE sito SET nome = ?, header = ?, footer = ?, id_utente = ?, homepage = ?, id_css = ?, data_creazione = ? WHERE id = ?");
        dSito = c.prepareStatement("DELETE FROM sito WHERE id = ?");
        gSlide = c.prepareStatement("SELECT * FROM slide WHERE id = ?");
        aSlide = c.prepareStatement("INSERT INTO slide (descrizione, posizione, file, nome) VALUES(?,?,?,?) RETURNING id");
        uSlide = c.prepareStatement("UPDATE slide SET descrizione = ?, posizione = ?, file = ?, nome=?");
        dSlide = c.prepareStatement("DELETE FROM slide WHERE id = ?");
        gSitobyUtente = c.prepareStatement("SELECT * FROM sito WHERE id_utente = ?");
        gFiglie = c.prepareStatement("SELECT * FROM pagina WHERE id_padre = ?");
        gPaginabyTitolo = c.prepareStatement("SELECT id FROM pagina WHERE titolo = ? AND id_sito = ?");
        gFoglie = c.prepareStatement("SELECT * FROM pagina WHERE id_sito = ? AND id_padre IS NOT NULL AND id NOT IN "
                + "(SELECT DISTINCT id_padre FROM pagina WHERE id_padre IS NOT NULL);");
        gAntenati = c.prepareStatement("WITH RECURSIVE func(id) AS ( "
                + "SELECT id FROM pagina WHERE id = ? "
                + "UNION ALL " 
                + "SELECT p.id_padre FROM func f, pagina p WHERE f.id = p.id) " 
                + "SELECT id FROM func;");
        gPagineSito = c.prepareStatement("SELECT * FROM pagina WHERE id_sito = ?");
        dSottoAlbero = c.prepareStatement("WITH ");
        gCoverUtente = c.prepareStatement("SELECT * FROM immagine WHERE immagine.cover = true AND immagine.id_utente = ? ");
    }
    
    @Override
    public Css createCSS() {
        return new CssImpl(this);
    }
    
    @Override
    public Css getFirstCSS(){
        ResultSet rs = null;
        try{
            rs = gManualCss.executeQuery("SELECT * FROM css LIMIT 1");
            if(rs.next()){
                return new CssImpl(this, rs);
            }
        } catch (SQLException ex){
            java.util.logging.Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                rs.close();
            } catch (SQLException ex){
                //
            }
        }
        return null;
    }
    
    @Override
    public List<Css> getAllCSS(){
        ResultSet rs = null;
        List<Css> ris = new ArrayList<Css>();
        try{
            rs = gManualCss.executeQuery("SELECT * FROM css");
            while(rs.next()){
                ris.add(new CssImpl(this, rs));
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
            dCss.setLong(1, O.getId());
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
            aUtente.setLong(6,IUtente.getSpazio_disp_img());
            aUtente.setString(7, IUtente.getCodiceAttivazione());
            aUtente.setBoolean(8, IUtente.getAttivato());
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
            dUtente.setLong(1,U.getId());
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
            uUtente.setLong(6,U.getSpazio_disp_img());
            uUtente.setString(7, U.getCodiceAttivazione());
            uUtente.setBoolean(8, U.getAttivato());
            uUtente.setLong(9,U.getId());
            if(uUtente.executeUpdate() == 1){
                return getUtente(U.getId());
            }
        } catch(SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
    public Immagine getCoverUtente(Utente U){
        ResultSet rs = null;
        try {
            
            gCoverUtente.setLong(1, U.getId());
            rs = gCoverUtente.executeQuery();
            if(rs.next()){
                return new ImmagineImpl(this, rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                rs.close();
            } catch (SQLException ex){
                //
            }
        }
        return null;
    }
    
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
            aImmagine.setLong(7,img.getUtente().getId());
            aImmagine.setString(8,img.getThumb());
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

    private void deleteImmagineFiles(Immagine I){
        if(I != null){
            File f = new File(I.getFile());// forse da fare getRealPath
            if(f.exists()){
                f.delete();
            }
            f = new File(I.getThumb());
            if(f.exists()){
                f.delete();
            }
        }
    }
    
    @Override
    public Immagine deleteImmagine(Immagine I) {
        try{
            dImmagine.setLong(1,I.getId());
            if(dImmagine.executeUpdate() == 1){
                deleteImmagineFiles(I);
                return I;
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Immagine updateImmagine(Immagine I) {
        try{
            uImmagine.setString(1, I.getNome());
            uImmagine.setLong(2,I.getDimensione());
            uImmagine.setString(3, I.getTipo());
            uImmagine.setString(4,I.getFile());
            uImmagine.setString(5,I.getDigest());
            uImmagine.setTimestamp(6, I.getData_upload());
            uImmagine.setLong(7, I.getUtente().getId());
            uImmagine.setString(8, I.getThumb());
            uImmagine.setBoolean(9, I.getCover());
            uImmagine.setLong(10, I.getId());
            if(uImmagine.executeUpdate() == 1){
                return getImmagine(I.getId());
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return null;
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
            gImmagini.setLong(1,U.getId());
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
    public Pagina addHomepage(Pagina p){
        ResultSet chiave = null;
        try{
            aHome.setString(1, p.getTitolo());
            aHome.setString(2, p.getBody());
            aHome.setLong(3, p.getSito().getId());
            chiave = aHome.executeQuery();
            if(chiave.next()){
                return getPagina(chiave.getLong("id"));
            }
        } catch (SQLException ex){
            
        } finally {
            try{
                chiave.close();
            } catch (SQLException ex){
                
            }
        }
        return null;
    }
    
    @Override
    public Pagina addPagina(Pagina p) {
        ResultSet chiave = null;
        PaginaImpl p_agg = (PaginaImpl) p;
        try{
            aPagina.setString(1, p_agg.getTitolo());
            aPagina.setString(2,p_agg.getBody());
            aPagina.setLong(3,p_agg.getPadre().getId());
            aPagina.setLong(4,p_agg.getSito().getId());
            aPagina.setBoolean(5,p_agg.getModello());
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
    
    //dal più recente
    @Override
    public List<Sito> getSitiDataOrdinati(){
        List<Sito> ris = new ArrayList<Sito>();
        ResultSet rs = null;
        try{
            rs = gSitiDataOrdinati.executeQuery("SELECT * FROM sito ORDER BY(data_creazione) DESC");
            while(rs.next()){
                ris.add(new SitoImpl(this, rs));
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                rs.close();
            } catch (SQLException ex){
                //
            }
        }
        return ris;
    }

    @Override
    public Pagina deletePagina(Pagina p) {
        try{
            dPagina.setLong(1,p.getId());
            if(dPagina.executeUpdate() == 1){
                return p;
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Pagina updateHomepage(Pagina p){
        try{
            uHome.setString(1, p.getTitolo());
            uHome.setString(2, p.getBody());
            uHome.setLong(3, p.getSito().getId());
            uHome.setBoolean(4, p.getModello());
            uHome.setLong(5, p.getId());
            if(uHome.executeUpdate() == 1){
                return getPagina(p.getId());
            }
        } catch (SQLException ex){
            //
        }
        return null;
        }
    @Override
    public Pagina updatePagina(Pagina p) {
        try{
            uPagina.setString(1, p.getTitolo());
            uPagina.setString(2, p.getBody());
            uPagina.setLong(3, p.getPadre().getId());
            uPagina.setLong(4, p.getSito().getId());
            uPagina.setBoolean(5, p.getModello());
            uPagina.setLong(6, p.getId());
            if(uPagina.executeUpdate() == 1){
                return getPagina(p.getId());
            }
        } catch (SQLException ex){
            //
        }
        return null;
    }

    @Override
    public List<Pagina> getFiglie(Pagina p) {
        PaginaImpl padre = (PaginaImpl) p;
        List<Pagina> ris = new ArrayList();
        ResultSet rs = null;
        try{
            gFiglie.setLong(1, padre.getId());
            rs = gFiglie.executeQuery();
            while(rs.next()){
                ris.add(new PaginaImpl(this,rs));
            }
        } catch(SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try{
                rs.close();
            } catch(SQLException ex){
                //
            }
        }
        return ris;
    }

    @Override
    public Pagina getHomepage(long i) {
        ResultSet rs = null;
        try{
            gHome.setLong(1,i);
            rs = gHome.executeQuery();
            if(rs.next()){
                return new PaginaImpl(this,rs);
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                rs.close();
            } catch (SQLException ex){
                
            }
        }
        return null;
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
    public List<Pagina> getPagineModello(){
        ResultSet rs = null;
        List<Pagina> ris = new ArrayList<Pagina>();
        try{
            rs = gPagineModello.executeQuery("SELECT * FROM pagina WHERE modello = true");
            while (rs.next()){
                ris.add(new PaginaImpl(this, rs));
            }
        } catch(SQLException ex){
            //
        } finally {
            try{
                rs.close();
            } catch (SQLException ex){
                //
            }
        }
        return ris;
    }
    
    @Override
    public long getLinkPagebyTitle(Sito s, String titolo){
        SitoImpl sito = (SitoImpl) s;
        ResultSet rs = null;
        try{
            gPaginabyTitolo.setString(1, titolo);
            gPaginabyTitolo.setLong(2,s.getId());
            rs = gPaginabyTitolo.executeQuery();
            if(rs.next()){
                return rs.getLong("id");
            }
        } catch (SQLException ex){
            
        } finally {
            try{
                rs.close();
            } catch (SQLException ex){
                
            }
        }
        return 0;
    }
    
    @Override
    public List<Pagina> getFoglie(Sito s){
        List<Pagina> ris = new ArrayList<Pagina>();
        ResultSet rs = null;
        try{
            gFoglie.setLong(1,s.getId());
            rs = gFoglie.executeQuery();
            while(rs.next()){
                ris.add(new PaginaImpl(this, rs));
            }
        }catch(SQLException ex){
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
    public List<Pagina> getAntenati(Pagina foglia){
        List<Pagina> ris = new ArrayList<Pagina>();
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        try{
            gAntenati.setLong(1, foglia.getId());
            rs1 = gAntenati.executeQuery();
            while(rs1.next()){
                gPagina.setLong(1, rs1.getLong("id"));
                rs2 = gPagina.executeQuery();
                if(rs2.next()){
                    ris.add(new PaginaImpl(this, rs2));
                }
            }
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                rs1.close();
                rs2.close();
            } catch (Exception ex){
                
            }
        }
        return ris;
    }
    
    @Override
    public List<Pagina> getPagineSito(Sito s){
        List<Pagina> ris = new ArrayList<Pagina>();
        ResultSet rs = null;
        try{
            gPagineSito.setLong(1,s.getId());
            rs = gPagineSito.executeQuery();
            while(rs.next()){
                ris.add(new PaginaImpl(this, rs));
            }
            return ris;
        } catch (SQLException ex){
            Logger.getLogger(CMSDataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try{
                rs.close();
            } catch (Exception ex){
                
            }
        }
        return null;
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
            aSito.setLong(3,s_agg.getUtente().getId());
            aSito.setString(4,s_agg.getNome());
            aSito.setLong(5,s_agg.getCss().getId());
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
                //
            }
        }
        return null;
    }

    @Override
    public Sito deleteSito(Sito s) {
        try{
            dSito.setLong(1,s.getId());
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
        try{
            uSito.setString(1, s.getNome());
            uSito.setString(2, s.getHeader());
            uSito.setString(3, s.getFooter());
            uSito.setLong(4, s.getUtente().getId());
            uSito.setLong(5, s.getHomepage().getId());
            uSito.setLong(6, s.getCss().getId());
            uSito.setDate(7, s.getDataCreazione());
            uSito.setLong(8, s.getId());
            if(uSito.executeUpdate() == 1){
                return getSito(s.getId());
            }
        } catch (SQLException ex){
            //
        }
        return null;
    }

    @Override
    public List<Sito> getSitobyUtente(Utente U) {
        List<Sito> ris = new ArrayList();
        ResultSet rs = null;
        try{
            gSitobyUtente.setLong(1, U.getId());
            rs = gSitobyUtente.executeQuery();
            while(rs.next()){
                Sito s = new SitoImpl(this,rs);
                ris.add(s);
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
            aSlide.setString(4,s_agg.getNome());
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
            dSlide.setLong(1, s.getId());
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
