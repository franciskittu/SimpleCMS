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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CMSDataLayerImpl implements CMSDataLayer {
    
    private PreparedStatement gCss, aCss, uCss, dCss;

    public CMSDataLayerImpl(Connection c) throws SQLException{
        gCss = c.prepareStatement("SELECT * FROM css WHERE id = ?");
        aCss = c.prepareStatement("INSERT INTO css (nome, descrizione, file) VALUES (?,?,?) RETURNING id");
        uCss = c.prepareStatement("UPDATE css SET nome = ? AND descrizione = ? AND file = ? WHERE id = ?");
        dCss = c.prepareStatement("DELETE FROM css WHERE id = ?");
    }
    
    @Override
    public Css createCSS() {
        return new CssImpl(this);
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
            File f = new File(O.getFile());
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Utente addUtente(Utente U) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Utente deleteUtente(Utente U) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Utente updateUtente(Utente U) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean checkPassword(Utente U, String pwd) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Immagine createImmagine() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Immagine addImmagine(Immagine I) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Immagine deleteImmagine(Immagine I) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
