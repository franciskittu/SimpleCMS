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

import java.util.List;
/* 
 * Questa interfaccia contiene la lista di funzionalità astratte di cui la
 * logica business dell'applicazione necessita per manipolare i dati e
 * realizzare le operazioni richieste
 */

/**
 *
 * @author fsfskittu
 */
public interface CMSDataLayer {
    
    /* CSS */
    Css createCSS();
    
    Css getFirstCSS();
    
    Css getCSS(long i);
    
    List<Css> getAllCSS();
    
    Css addCSS(Css O);
    
    Css updateCSS(Css O);
    
    Css deleteCSS(Css O);
    
    /* UTENTE */
    
    Utente createUtente();
    
    Utente addUtente(Utente U);
    
    Utente deleteUtente(Utente U);
    
    Utente updateUtente(Utente U);    
    
    
    /* IMMAGINI */
    Immagine createImmagine();
    
    Immagine addImmagine(Immagine I);
    
    Immagine deleteImmagine(Immagine I);
    
    Immagine updateImmagine(Immagine I);
    
    Immagine getImgbyID(int id_img);
    
    List<Immagine> getAllUsersImages(Utente U);
    
    Immagine getCoverUtente(Utente U);
    
    /* PAGINE */
    
    Pagina createPagina();
    
    Pagina addPagina(Pagina p);
    
    Pagina addHomepage(Pagina p);
    
    Pagina deletePagina(Pagina p);
    
    Pagina updatePagina(Pagina p);
    
    Pagina updateHomepage(Pagina p);
    
    List<Pagina> getFiglie(Pagina p);
    
    Pagina getHomepage(long i);
    
    Pagina getPagina(long i);
    
    long getLinkPagebyTitle(Sito s, String titolo);
    
    List<Pagina> getFoglie(Sito s);
    
    List<Pagina> getAntenati(Pagina p);
    
    List<Pagina> getPagineSito(Sito s);
    
    List<Pagina> getPagineModello();
    
    /* SITO */
    
    Sito createSito();
    
    Sito addSito(Sito s);
    
    Sito deleteSito(Sito s);
    
    Sito updateSito(Sito s);
    
    List<Sito> getSitobyUtente(Utente U);//per le url senza specifica del sito si può stabilire un default
    
    Sito getSito(long i);
    
    List<Sito> getSitiDataOrdinati();
    
    /* SLIDES */
    
    Slide createSlide();
    
    Slide addSlide(Slide s);
    
    Slide deleteSlide(Slide s);
    
    Slide updateSlide(Slide s);
    
    Slide getSlide(long i);
}
