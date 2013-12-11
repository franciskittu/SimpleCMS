/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.lufraproini.cms.bean;

/**
 *
 * @author fsfskittu
 */
public class Tab {
    private Integer chiave;
    private String nome;
    private String cognome;
    
    public Tab(Integer i, String s1, String s2){
        this.chiave=i;
        this.nome=s1;
        this.cognome=s2;
    }
    
    public Integer getChiave(){
        return chiave;
    }
    
    public String getNome(){
        return nome;
    }
    
    public String getCognome(){
        return cognome;
    }
    
    public void setChiave(Integer i){
        this.chiave = i;
    }
    
    public void setNome(String s){
        this.nome = s;
    }
    
    public void setCognome(String s){
        this.cognome = s;
    }
}
