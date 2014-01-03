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
import it.lufraproini.cms.model.Utente;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SitoImpl implements Sito {

    private CMSDataLayerImpl datalayer;
    private long id, id_utente/*chiave*/, id_homepage/*chiave*/;
    private String header, footer;
    
    private Pagina homepage;
    private Utente utente;
    
    public SitoImpl(CMSDataLayerImpl datalayer){
        id = 0;
        id_utente = 0;
        id_homepage = 0;
        header = "";
        footer = "";
        this.datalayer = datalayer;
    }
    
    public SitoImpl(CMSDataLayerImpl datalayer, ResultSet data) throws SQLException{
        id = data.getLong("id");
        id_utente = data.getLong("id_utente");
        id_homepage = data.getLong("homepage");
        header = data.getString("header");
        footer = data.getString("footer");
        this.datalayer = datalayer;
    }
    
    @Override
    public long getID() {
        return id;
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public void setHeader(String s) {
        header = s;
    }

    @Override
    public String getFooter() {
        return footer;
    }

    @Override
    public void setFooter(String s) {
        footer = s;
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
    public void setHomepage(Pagina p) {
        homepage = p;
    }

    @Override
    public Pagina getHomepage() {
        if(homepage == null){
            homepage = datalayer.getHomepage(id_homepage);
        }
        return homepage;
    }
    
}
