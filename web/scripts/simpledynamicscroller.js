/*

WEB EGINEERING COURSE - University of L'Aquila 

This example shows how to implement a simple "paged scrolling" with
Javascript. As the user scrolls down the area using its scroll bar,
the script loads in background the following text, so that the
text is only loaded from the server if the user tries to read it. 
The script has also a simple "non javascript" solution which invokes 
some server-side pagination system.

THIS SCRIPT IS STILL IN BETA, AND HAS NOT BEEN FULLY TESTED

See the course homepage: http://www.di.univaq.it/gdellape/students.php

*/

//funzione di supporto che carica le pagine di dati
//implementazione di prova: in realt� qui sarebbe opportuno
//usare ajax per caricare i dati dal server!
//support function to load the data pages
//test implementation: actually we should use ajax to 
//load the text from the server
function dummyLoader(page) {
	if (page<10) {
		var data =  document.createElement("div");
                
		var i;
		for (i=0;i<3;++i) {
                        var row= document.createElement("div");
                        row.className="row";
			var item1 = document.createElement("div");
                        item1.className="eight columns item";
			var item2 = document.createElement("div");
                        item2.className="eight columns item";
			row.appendChild(item1);
			row.appendChild(item2 );
                        data.appendChild(row);
		}
		return data;
	} else {
		return null;
	}
}

//funzione costrutture dell'oggetto SmartScroller che gestisce lo scrolling paginato
//ogni oggetto SmartScroller � legato a un elemento html di cui gestisce lo scrolling
//this constructor creates a SmartScroller object handling the pages scrolling
//each SmartScroller is linked to a specific html scrollable element
function SmartScroller(scroller,loaderFunction) {
		//verifichiamo se siamo in Internet Explorer
		//check for IE
		this.IEmode = (document.all)?true:false;
		//assegniamo a delle propriet� dell'oggetto alcuni parametri del costruttore
		//e altri valori calcolati
		//assign some object properties with constructor parameters
		this.scroller = scroller;	
		this.loaderFunction=loaderFunction;		
		this.triggerline = null;
		//la pagina di dati attualmente visualizzata
		//the cuurrently displayed data page
		this.page = 0;
		
		//creaimo un metodo nell'oggetto
		//questo metodo restituisce l'event handler che si occupa di gestire
		//lo scorrimento dell'elemento associato
		
		//create object methods
		//this method returns the handler which manages the element scrolling
		
		this.makeScrollListener = function() {
			//usiamo l'effetto closure per fare in modo che l'handler restituito
			//abbia un riferimento all'oggetto che lo ha generato (questo) e quindi
			//possa leggerne le propriet�
			//the closure effect is used to embed in the returned handler a reference
			//the its owner object (this), to read its properties
			var THIS=this;
			return function() {
                               
				//l'handler di scorrimento verifica se la riga "trigger" (cio� la riga che attiva 
				//il caricamento degli elementi successivi) � visibile e, finch� lo �,
				//invoca la funzione esterna per recuperare nuovi elementi e li accoda a quelli gi�
				//visualzzati, mantenendo la riga trigger all'ultimo posto				
				//the scroll handler checks if the "trigger line" (i.e., the invisible element which triggers
				//the loading of another block of text) is visible and, as long as it reamins visible,
				//calls an external function to load another block of data and appends them to the current
				//element text, leaving the trigger line at the bottom of the content.
				while(THIS.scroller.scrollTop+THIS.scroller.offsetHeight >= THIS.triggerLine.offsetTop) {
					//per verificare se la triggerLine � visibile, si confronta la sua posizione verticale
					//con l'origine verticale dello scroll corrente nell'elemento, tenendo anche conto della sua altezza
					//to check is triggerLine is visible, we compare its vertical position with the current vertical
					//scroll offset, taking into account also the viewport height
					var data = THIS.loaderFunction(++THIS.page);
					if (data!=null) {
						THIS.scroller.insertBefore(data,THIS.triggerLine);
					} else {
						//se non ci sono pi� pagine, rimuoviamo la triggerLine e l'event handler
						//if there is no more data, remove the triggerLine and the scroll handler
						THIS.scroller.onscroll=function(){}; 
						//assegnarlo a null potrebbe causare problemi
						//setting the handler to null may cause problems...
						THIS.scroller.removeChild(THIS.triggerLine);						
						THIS.triggerLine = null;
						break;						
					}
				}	
			
			};
		};		
		
		//verifichiamo se l'elemento da scrollare contiene gi� la riga "trigger"
		//cio� la riga che permette di caricare gli elementi successivi
		//check if the element to scroll already contains the trigger line
		var innerdivs = this.scroller.getElementsByTagName("div");		
		var i;
		for(i = 0; i<innerdivs.length; ++i) {
			if (innerdivs.item(i).className.indexOf("smart-scroll-trigger-line")>=0) {
				this.triggerLine = innerdivs.item(i); 
				//N.B.: se c'era gi� la riga di trigger, supponiamo che ci fosse gi� anche la prima pagina di dati
				//if the triggel line is there, we sippose that also the first page of data has been already loaded
				break;
			}
		}
		
		//se non c'� la creiamo noi
		//if the triggerLine is not in the element, create it
		if (this.triggerLine==null) {
			this.triggerLine =  document.createElement("div");		
			this.triggerLine.className="smart-scroll-trigger-line";	
			this.scroller.appendChild(this.triggerLine);				
		}
		
		//inseriamo nella riga trigger il testo appropriato
		//add a suitable text to the triggerLine
		if (this.IEmode) this.triggerLine.innerText = "loading...";
		else this.triggerLine.textContent = "loading...";
		
		//resettiamo la posizione di scorrimento
		//reset the scroll position
		this.scroller.scrollTop=0;
		//impostiamo l'handler preparato dal nostro metodo per l'evento scroll
		//add our scroll handler
		this.scroller.onscroll = this.makeScrollListener();

		//chiamiamo manualmente l'event handler dello scorrimento
		//lo facciamo in maniera impropria, ma solo perch� sappiamo
		//che questo handler NON utilizza il parametro "evento" che 
		//gli viene passato quando � invocato a seguito di un vero
		//evento di scrolling
		//manual call to the scroll event handler
		//this is not a safe way to call an event handler, since
		//we should pass to it an event object. However, in this 
		//case we know that the handler will never use such an object
		this.scroller.onscroll();	
		//in questo modo, la lista verr� riempita nel caso sia vuota o semivuota
		//in this way, the list is filled if it is empty
}

function init() {
	//attiviamo lo smart scrolling sull'elemento "scroller1" usando dummyLoader per caricare i dati
	//activate the smart scolling on the "scroller1" element, using the dummyLoader to get the data
	new SmartScroller(document.getElementById("last_sites"),dummyLoader);
        
        init_slider();
}

window.onload=init;