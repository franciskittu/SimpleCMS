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
package it.lufraproini.cms.model;

/**
 *
 * @author fsfskittu
 */
public interface Utente {
    long getId();
    
    String getUsername();
    void setUsername(String s);
    
    String getPassword();
    void setPassword(String s);
    
    String getNome();
    void setNome(String s);
    
    String getCognome();
    void setCognome(String s);
    
    String getEmail();
    void setEmail(String s);
      
    long getSpazio_disp_img();
    void setSpazio_disp_img(long i);
    
    String getCodiceAttivazione();
    void setCodiceAttivazione(String s);
    
    boolean getAttivato();
    void setAttivato(boolean b);
}
