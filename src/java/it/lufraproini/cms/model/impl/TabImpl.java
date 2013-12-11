/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.lufraproini.cms.model.impl;

import it.lufraproini.cms.model.Tab;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author fsfskittu
 */
public class TabImpl implements Tab{
    private Integer chiave;
    private String nome;
    private String cognome;
    
    private ProgettoDataLayerImpl datalayer;
    //notare che i costruttori che seguono sono entrambi
    //package-private, cioè possono essere richiamati
    //solo da classi all'interno dello stesso package
    //(tra cui le API del modello dati), ma risultano
    //invisibili all'utente (che non può usare quindi la
    //new per creare istanze di questa classe)
    //costrutture "vuoto" usato per inizializzare gli oggetti
    //notare che queste classi hanno bisogno di un riferimento
    //alle API del modello dati per eseguire alcune operazioni
    
    TabImpl(ProgettoDataLayerImpl datalayer){
        chiave = 0;
        nome = "";
        cognome = "";
        this.datalayer = datalayer;
    }
    
    //costrutture "helper" che crea un oggetto e lo imposta
    //in base ai valori dei campi nel record corrente di
    //un ResultSet - utile per creare oggetti a partire
    //dai risultati di una query
    TabImpl(ProgettoDataLayerImpl datalayer, ResultSet data) throws SQLException{
        this.datalayer = datalayer;
        chiave = data.getInt("chiave");
        nome = data.getString("nome");
        cognome = data.getString("cognome");
    }
    
    @Override
    public Integer getChiave(){
        return this.chiave;
    }
    @Override
    public String getNome(){
        return this.nome;
    }
    @Override
    public String getCognome(){
        return this.cognome;
    }
    @Override
    public void setNome(String s){
        this.nome = s;
    }
    @Override
    public void setCognome(String s){
        this.cognome = s;
    }
}
