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
//import it.lufraproini.cms.security.SecurityLayer FACCIAMO GESTIRE LA CRITTOGRAFIA E LE VARIE FUNZIONI DI SICUREZZA AL CONTROLLER
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UtenteImpl implements Utente {
    private long id, spazio_disp_img;
    private String username, password, nome, cognome, email;
    private Date data_di_nascita;
    
    private CMSDataLayerImpl datalayer;
    
    public UtenteImpl(CMSDataLayerImpl datalayer){
        id = 0;
        username = "";
        password = "";
        nome = "";
        cognome = "";
        email = "";
        data_di_nascita = null;
        spazio_disp_img = 0;
        this.datalayer = datalayer;
    }
    
    public UtenteImpl(CMSDataLayerImpl datalayer, ResultSet data) throws SQLException {
        id = data.getLong("id");
        username = data.getString("username");
        password = data.getString("password");
        nome = data.getString("nome");
        cognome = data.getString("cognome");
        email = data.getString("email");
        data_di_nascita = data.getDate("data_di_nascita");//data.getDate(email, null)
        spazio_disp_img = data.getLong("spazio_disp_img");
        this.datalayer = datalayer;
    }
    
    @Override
    public long getID() {
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
    public Date getData_di_nascita() {
        return data_di_nascita;
    }

    @Override
    public void setData_di_nascita(Date d) {
        this.data_di_nascita = d;
    }

    @Override
    public long getSpazio_disp_img() {
        return this.spazio_disp_img;
    }

    @Override
    public void setSpazio_disp_img(long i) {
        this.spazio_disp_img = i;
    }
 
}
