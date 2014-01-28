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
package it.lufraproini.cms.utility;

import it.lufraproini.cms.model.Pagina;

/**
 *
 * @author fsfskittu
 */
public class Nodo {
    private String titolo;
    private int livello;
    private long id;
    
    public Nodo(Pagina P, int livello){
        this.titolo = P.getTitolo();
        this.id = P.getID();
        this.livello = livello;
    }
    
    public void setLivello(int livello){
        this.livello = livello;
    }
    
    public int getLivello(){
        return livello;
    }
    
    public void setTitolo(String s){
        this.titolo = s;
    }
    
    public String getTitolo(){
        return titolo;
    }
    
    public void setID(long id){
        this.id = id;
    }
    public long getID(){
        return id;
    }
}
