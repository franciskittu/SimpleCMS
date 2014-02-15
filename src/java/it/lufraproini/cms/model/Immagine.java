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

import java.sql.Timestamp;

/**
 *
 * @author fsfskittu
 */
public interface Immagine {
    long getID();
    
    String getNome();
    void setNome(String s);
    
    long getDimensione();
    void setDimensione(long i);
    
    String getTipo();
    void setTipo(String s);
    
    String getFile();
    void setFile(String s);
    
    String getDigest();
    void setDigest(String s);
    
    Timestamp getData_upload();//da recuperare con java.sql.Date.valueOf( String s ); es: java.sql.Date jsqlD = java.sql.Date.valueOf( "2008-12-15" );/*http://www.javahelp.it/index.php?java-e-le-date*/
    void setData_upload(Timestamp t);
    
    Utente getUtente();
    void setUtente(Utente U);
    
    String getThumb();
    void setThumb(String s);
}
