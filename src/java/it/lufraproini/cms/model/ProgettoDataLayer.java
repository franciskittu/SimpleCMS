/* 
 * Questa interfaccia contiene la lista di funzionalità astratte di cui la
 * logica business dell'applicazione necessita per manipolare i dati e
 * realizzare le operazioni richieste
 * .
 */

package it.lufraproini.cms.model;

import java.util.List;
/**
 *
 * @author fsfskittu
 */
public interface ProgettoDataLayer {
    //metodi factory per gli oggetti del datamodel
    Tab createTab();
    
    Tab getTab(Integer chiave);
    Tab addTab(Tab tab);
    Tab deleteTab(Tab tab);
    List<Tab> getAllTab();//restituisce tutta la tabella!
}
