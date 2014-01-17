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

import it.lufraproini.cms.model.Slide;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author fsfskittu
 */
public class SlideImpl implements Slide{
    
    private CMSDataLayerImpl datalayer;
    private long id;
    private String descrizione, file, nome;
    private int posizione;
    
    public SlideImpl(CMSDataLayerImpl datalayer){
        id = 0;
        posizione = 0;
        file = "";
        descrizione = "";
        nome = "";
        this.datalayer = datalayer;
    }
    
    public SlideImpl(CMSDataLayerImpl datalayer, ResultSet rs) throws SQLException{
        id = rs.getLong("id");
        posizione = rs.getInt("posizione");
        file = rs.getString("file");
        descrizione = rs.getString("descrizione");
        nome = rs.getString("nome");
        this.datalayer = datalayer;
    }
    
    @Override
    public long getID(){
        return id;
    }
    
    @Override
    public String getDescrizione(){
        return descrizione;
    }
    
    @Override
    public void setDescrizione(String s){
        descrizione = s;
    }
    
    @Override
    public int getPosizione(){
        return posizione;
    }
    
    @Override
    public void setPosizione(int i){
        posizione = i;
    }
    
    @Override
    public String getFile(){
        return file;
    }
    
    @Override
    public void setFile(String s){
        file = s;
    }
    
    @Override
    public String getNome(){
        return nome;
    }
    
    @Override
    public void setNome(String s){
        nome = s;
    }
}
