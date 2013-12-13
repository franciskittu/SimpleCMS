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
public interface Pagina {
    long getID();
    
    String getTitolo();
    void setTitolo(String s);
    
    String getBody();
    void setBody(String s);
    
    boolean getModello();
    void setModello(boolean b);
    
    Css getCss();
    void setCss(Css style_sheet);
    
    Pagina getPadre();
    void setPadre(Pagina p);
}
