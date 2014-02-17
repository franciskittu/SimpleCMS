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

import it.lufraproini.cms.model.Immagine;
import it.lufraproini.cms.model.Utente;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class ImmagineImpl implements Immagine {
    
    private CMSDataLayerImpl datalayer;
    private long id, dimensione, id_utente/*chiave*/;
    private boolean cover;
    private String nome, file, digest, tipo, thumb;
    private Timestamp data_upload;
    
    private Utente utente;
    
    public ImmagineImpl(CMSDataLayerImpl datalayer){
        id = 0;
        dimensione = 0;
        id_utente = 0;
        tipo = "";
        nome = "";
        file = "";
        digest = "";
        thumb = "";
        cover = false;
        data_upload = null;
        this.datalayer = datalayer;
        
    }
    
    public ImmagineImpl(CMSDataLayerImpl datalayer, ResultSet rs) throws SQLException{
        id = rs.getLong("id");
        dimensione = rs.getLong("dimensione");
        id_utente = rs.getLong("id_utente");
        tipo = rs.getString("tipo");
        nome = rs.getString("nome");
        file = rs.getString("file");
        digest = rs.getString("digest");
        data_upload = rs.getTimestamp("data_upload");
        thumb = rs.getString("thumb");
        cover = rs.getBoolean("cover");
        this.datalayer = datalayer;
    }
    
    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String s) {
        nome = s;
    }

    @Override
    public long getDimensione() {
        return dimensione;
    }

    @Override
    public void setDimensione(long i) {
        dimensione = i;
    }
    
    @Override
    public String getTipo(){
        return tipo;
    }
    
    @Override
    public void setTipo(String s){
        tipo = s;
    }

    @Override
    public String getFile() {
        return file;
    }

    @Override
    public void setFile(String s) {
        file = s;
    }

    @Override
    public String getDigest() {
        return digest;
    }

    @Override
    public void setDigest(String s) {
        digest = s;
    }

    @Override
    public Timestamp getData_upload() {
        return data_upload;
    }

    @Override
    public void setData_upload(Timestamp t) {
        data_upload = t;
    }

    @Override
    public Utente getUtente() {
        if(utente == null){
            utente = datalayer.getUtente(id_utente);
        }
        return utente;
    }

    @Override
    public void setUtente(Utente U) {
        utente = U;
    }
    
    @Override
    public String getThumb(){
        return thumb;
    }
    
    @Override
    public void setThumb(String s){
        thumb = s;
    }
    
    @Override
    public boolean getCover(){
        return cover;
    }
    
    @Override
    public void setCover(boolean b){
        cover = b;
    }
}
