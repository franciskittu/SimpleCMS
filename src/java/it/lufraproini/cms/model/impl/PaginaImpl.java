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

import it.lufraproini.cms.model.Pagina;
import it.lufraproini.cms.model.Sito;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PaginaImpl implements Pagina {

    private long id, id_padre, id_sito;
    private String titolo, body;
    private Sito sito;
    private Pagina padre;
    private boolean modello;
    private CMSDataLayerImpl datalayer;
    
    public PaginaImpl (CMSDataLayerImpl datalayer){
        id = 0;
        id_padre = 0;
        id_sito = 0;
        titolo = "";
        body = "";
        modello = false;
        this.datalayer = datalayer;
    }
    
    public PaginaImpl (CMSDataLayerImpl datalayer, ResultSet data) throws SQLException{
        id = data.getLong("id");
        id_padre = data.getLong("id_padre");
        id_sito = data.getLong("id_sito");
        titolo = data.getString("titolo");
        body = data.getString("body");
        modello = data.getBoolean("modello");
        this.datalayer = datalayer;
    }
    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getTitolo() {
        return titolo;
    }

    @Override
    public void setTitolo(String s) {
        titolo = s;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String s) {
        body = s;
    }

    @Override
    public boolean getModello() {
        return modello;
    }

    @Override
    public void setModello(boolean b) {
        modello = b;
    }
    
    @Override
    public Pagina getPadre() {
        if(padre == null){
            padre = datalayer.getPagina(id_padre);
        }
        return padre;
    }

    @Override
    public void setPadre(Pagina p) {
        padre = p;
    }
    
    @Override
    public Sito getSito(){
        if(sito == null){
            sito = datalayer.getSito(id_sito);
        }
        return sito;
    }
    
    @Override
    public void setSito(Sito s){
        sito = s;
    }
}
