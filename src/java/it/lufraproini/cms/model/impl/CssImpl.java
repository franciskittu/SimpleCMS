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

import it.lufraproini.cms.model.Css;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CssImpl implements Css {

    private long id;
    private String nome, descrizione, file;
    
    private CMSDataLayerImpl datalayer;
    //costruttore base
    public CssImpl(CMSDataLayerImpl datalayer){
        id = 0;
        nome = "";
        descrizione = "";
        file = "";
        this.datalayer = datalayer;
    }
    
    //costruttore helper che costruisce l'oggetto dai risultati di una query
    public CssImpl(CMSDataLayerImpl datalayer, ResultSet data) throws SQLException {
        id = data.getLong("id");
        nome = data.getString("nome");
        descrizione = data.getString("descrizione");
        file = data.getString("file");
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
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String getFile() {
        return file;
    }

    @Override
    public void setFile(String path_to_file) {
        file = path_to_file;
    }
    
}
