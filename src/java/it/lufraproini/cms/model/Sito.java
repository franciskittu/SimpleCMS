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
public interface Sito {
    long getID();
    
    String getDescrizione();
    void setDescrizione(String s);
    
    String getHeader();
    void setHeader(String s);
    
    String getFooter();
    void setFooter(String s);
    
    Utente getUtente();
    void setUtente(Utente U);
    
    Pagina getHomepage();
    void setHomepage(Pagina p);
    
    Css getCss();
    void setCss(Css stile);
    
}
