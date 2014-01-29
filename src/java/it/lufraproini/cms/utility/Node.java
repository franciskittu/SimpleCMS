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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucatraini
 */
public class Node {
    
    private Pagina page;
    
    private List<Node> children;
    
    
    public Node(Pagina page){
    
        this.page=page;
        
        this.children = new ArrayList<Node>();
    
    }
    
    public void setPage(Pagina page){
    
     this.page = page;
    
    }
    
    public void setChildren(List<Node> children){
    
        this.children=children;
    
    }
    
    public void addChild(Node node ){
        
        if(this.children==null)
            this.children=new ArrayList<Node>();
        
        children.add(node);
    }    
    
    public Pagina getPage(){
    
        return this.page;
    }
    
    public List getChildren(){
    
        return this.children;
    }
    
    public boolean isFather(){
    
        return !(this.children.isEmpty());
    }    
}
