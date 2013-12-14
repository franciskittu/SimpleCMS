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
public interface Css {
    long getID();
    
    String getNome();
    void setNome(String nome);
    
    String getDescrizione();
    void setDescrizione(String descrizione);
    
    String getFile();
    void setFile(String path_to_file);
}
