/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.lufraproini.cms.model;

/**
 *
 * @author fsfskittu
 */
public interface Tab {
    
    /*
    * Ritorna la chiave
    */
    Integer getChiave();
    
    String getNome();
    
    void setNome(String nome);
    
    String getCognome();
    
    void setCognome(String nome);
    
}
