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

import it.lufraproini.cms.model.Utente;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UtenteImpl implements Utente {
    private long id, spazio_disp_img;
    private String username, password, nome, cognome, email, codice_attivazione;
    private boolean attivato;
    
    private CMSDataLayerImpl datalayer;
    
    public UtenteImpl(CMSDataLayerImpl datalayer){
        id = 0;
        username = "";
        password = "";
        nome = "";
        cognome = "";
        email = "";
        spazio_disp_img = 0;
        codice_attivazione = "";
        attivato = false;
        this.datalayer = datalayer;
    }
    
    public UtenteImpl(CMSDataLayerImpl datalayer, ResultSet data) throws SQLException {
        id = data.getLong("id");
        username = data.getString("username");
        password = data.getString("password");
        nome = data.getString("nome");
        cognome = data.getString("cognome");
        email = data.getString("email");
        spazio_disp_img = data.getLong("spazio_disp_img");
        codice_attivazione = data.getString("codice_attivazione");
        attivato = data.getBoolean("attivato");
        this.datalayer = datalayer;
    }
    
    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String s) {
        this.username = s;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String s) {
        //this.password = SecurityLayer.encryptPassword(s);
        this.password = s;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String s) {
        this.nome = s;
    }

    @Override
    public String getCognome() {
        return cognome;
    }

    @Override
    public void setCognome(String s) {
        this.cognome = s;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String s) {
        this.email = s;
    }

    @Override
    public long getSpazio_disp_img() {
        return this.spazio_disp_img;
    }

    @Override
    public void setSpazio_disp_img(long i) {
        this.spazio_disp_img = i;
    }
    
    @Override
    public String getCodiceAttivazione(){
        return codice_attivazione;
    }
    
    @Override
    public void setCodiceAttivazione(String s){
        codice_attivazione = s;
    }
    
    @Override
    public boolean getAttivato(){
        return attivato;
    }
    
    @Override
    public void setAttivato(boolean b){
        attivato = b;
    }
    
}
