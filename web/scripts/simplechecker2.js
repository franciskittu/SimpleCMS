/*

WEB EGINEERING COURSE - University of L'Aquila 

Thi example shows how to automate form field checking using advanced
object-based rules. Fields are checked dynamically and form cannot be
submitted if an error occurs.
See the course homepage: http://www.di.univaq.it/gdellape/students.php

*/

//gli oggetti di controllo hanno un metodo check, che prende 
//come input una stringa e resituisce un booleano, e una
//propriet� message, che restituisce il messaggio di errore
//associato

//check objects have a check method that takes a string as
//input and returns a boolean, and a message property that
//returns the associated error message

//oggetto di controllo per il predicato REQUIRED
//check object for the REQUIRED predicate
function ObjCheckRequired() {
	this.check = function(s) {
		if (s==null) return false;
		else return (s.replace(/^\s+|\s+$/g,"") !="");
	};
	this.message = "campo obbligatorio";
	
}




//oggetto di controllo per il predicato IS A NUMBER
//check object for the IS A NUMBER predicate
function ObjCheckNumber() {
	this.check = function(s,bint) {
		if (bint) return (s == parseInt(s));
		else return (s == parseFloat(s));
	};
	this.message = "Value must be a number";
}

//oggetto di controllo per il predicato IS IN RANGE (min,max)
//check object for the IS IN RANGE (min,max) predicate
function ObjRangeChecker(min,max) {

	this.check = function(s) {
		return(s>=min && s<=max)
	};
	this.message =  "Value must be between "+min+" and "+max;
}

//oggetto di controllo per il predicato MATCHES (rx)
//check object for the MATCHES (x) predicate
function ObjRegexpChecker(rx,insensitive) {
	rx = "^"+rx+"$";
	var orx = new RegExp(rx,(insensitive?"i":""));
	
	this.check =  function(s) {	return orx.test(s);	};
	this.message = "Value must match regular expression /"+rx+"/";
	
}	

//oggetto di controllo per il predicato DOES NOT CONTAIN A CHAR IN (set)
//check object for the DOES NOT CONTAIN A CHAR IN (set) predicate
function ObjDisallowedChecker(set) {

	this.check =  function(s) {
		var i;
		for (i = 0; i <set.length; ++i) {
			if (s.indexOf(set.charAt(i)) != -1) return false;
		}
		return true;	
	};
	this.message = "Value must not contain any of the characters "+set;
}

//oggetto di controllo che NEGA un altro aggetto di controllo
//check object that NEGATES another check object
function ObjNegatedChecker(c1) {

	this.check =  function(s) {	return !c1.check(s);	};
	this.message = "NOT ("+c1.message+")";

}

//oggetto di controllo che COMBINA (in AND o OR) altri aggetti di controllo	
//check object that COMBINES (AND or OR) other check objects
//modifica effettuata nella chiamata ad un unico parametro , si fa in modo che venga visualizzato solo il primo degli errori riscontrati
function ObjCombinedChecker(arrC,bOr) {
	var j;
	this.message = "";
	
	if (!bOr) {
		this.check = function(s) {	
			var i;
			for(i=0; i<arrC.length; ++i) if (!arrC[i].check(s)){this.message =arrC[i].message; return false;}
			return true;
		};
	}
	else {
		this.check = function(s) {	
			var i;
			for(i=0; i<arrC.length; ++i) if (arrC[i].check(s)) return true;
			return false;
		};
		for(j=0; j<arrC.length; ++j) this.message+=(this.message!=""?" o":"")+arrC[j].message;
	}
}


//oggetto di controllo per gli indirizzi email 
//check object for email addresses
// modifica alla checkemail in modo da dare un messaggio sufficientemente chiaro
function CheckEmail(){
    
    var regexp=new RegExp("^([a-z0-9_\.-])+@(([a-z0-9_-])+\.)+[a-z]{2,6}$","i"); 
    
    this.check=function(s){ return regexp.test(s);}
    
    this.message="email non valida";
      
    }
    
    
  //oggetto di controllo per l'unicità dell'indirizzo email   
    function CheckEmailUnique(){
     var result;
     this.check=function(s){ 
        
        $.ajax({
            url:"test", dataType:"json", data:{ email:s}, async:false, success: function(data){result=data.result;}
            
        });

        if(result=="true")
            return false;
        else
            return true;
    }
    
    this.message="email associata ad utente";
 }
    
    
   //oggetto di controllo per username
   
 function CheckUsername(){
     var result;
     this.check=function(s){ 
        
        $.ajax({
            url:"test", dataType:"json", data:{ username:s}, async:false, success: function(data){result=data.result;}
            
        });

        if(result=="true")
            return false;
        else
            return true;
    }
    
    this.message="username esistente";
 }


//esempi di oggetti di controllo derivati:
//examples of derived check objects:



//oggetto di controllo per i caratteri pi� pericolosi 
//check object for dangerous characters
var CheckStrangeChars = new ObjDisallowedChecker("!@#$%^&*()+=-[]\\\';,./{}|\":<>?");

//oggetto di controllo combinato: controlla che il valore sia non nullo, sia un numero e NON sia nell'intervallo (1,20)
//combined check object: checks that the value is not empty, is a number and in NOT in the range (1,20)
var CheckMyNumber = new ObjCombinedChecker(
	[new ObjCheckRequired(),	new ObjCheckNumber(), new ObjNegatedChecker(new ObjRangeChecker(1,20))]
);

//Questo costruttore crea oggetti InputFieldChecker che associano a un elemento input (controllo form)
//un oggetto di controllo ottenuto come visto sopra. Il controllo avverr� automaticamente ad ogni
//variazione nel contenuto della casella di input.
//This constructor creates InputFieldChecker objects that associate a check object (as the ones above)
//to an input element (form control). The check is automatically triggered when the input value changes.
function InputFieldChecker(element,check) {
	if (check==null || typeof(check)!="object" || typeof(check.check)!="function") throw("Parameter 2 of the InputFieldChecker constructor has wrong type");
	if (element==null || typeof(element)!="object" || typeof(element.value)=="undefined") throw("Parameter 1 of the InputFieldChecker constructor has wrong type");

	this.element = element;
	this.objcheck = check;		
	this.saveborder = element.style.border;
	this.messagearea = element.ownerDocument.createElement("span");
	this.messagearea.className="messagearea";
	if (element.nextSibling) element.parentNode.insertBefore(this.messagearea,element.nextSibling);
	else element.parentNode.appendChild(this.messagearea);
	
	this.check = function() {
		if (!this.objcheck.check(this.element.value)) {
			this.element.style.border = "1px solid rgba(255,0,0,0.6)";			
			this.messagearea.textContent=this.objcheck.message;
			return false;
		} else {
			this.element.style.border = this.saveborder;
			this.messagearea.textContent="";
			return true;
		}
	}
	var THIS = this;
	this.element.onchange = function() {
		THIS.check();
	}
}

//Questo costruttore crea oggetti FormChecker che associano a una form una serie di
//InputFieldChecker. Questi vengono creati automaticamente in base al contenuto dell'attributo 
//class dei singoli input, possono essere anche aggiunti via codice tramite il metodo addCheck.
//Il controllo avverr� automaticamente alla submission della form.
//This constructor creates FormChecker objects that associate a set of InputFieldChecker to
//a form. Such checkers are automatically created from the input classes, but can be also added
//manually using the addcheck method. The checks are triggeret automatically during the form
//submission.
function FormChecker(form) {
	this.form =form;
	this.checks = Array();
	this.errors = Array();

	this.addCheck = function(check) {
		this.checks.push(check);
	}
	
	this.scanStdChecks = function() {
		var inputs = this.form.getElementsByTagName("input");
		var i,j;
		for(i=0; i<inputs.length; ++i) {
			if (inputs[i].type=="text") {
				var fldchecks = Array();
				var classes = inputs[i].className.split(/\s+/);
				for(j=0; j<classes.length;++j) {
					switch(classes[j]) {
						case "number":	fldchecks.push(new ObjCheckNumber()); break;
                                                case "emailunique":fldchecks.push(new CheckEmailUnique());  break;
						case "email":	fldchecks.push(new CheckEmail());  break;
						case "username":	fldchecks.push(new CheckUsername());  break;
						case "required":	fldchecks.push(new ObjCheckRequired());  break;
					}	
	
				}
				if (fldchecks.length>0)	{
					this.addCheck(new InputFieldChecker(inputs[i],new ObjCombinedChecker(fldchecks)));
				
				}
			}
		}
	}
	
	this.checkAll = function() {
		var i;
		this.errors = Array();
		for(i=0; i<this.checks.length; ++i) {
			if (!this.checks[i].check()) {
				this.errors.push(this.checks[i]);
			}
		}
		return (this.errors.length==0);
	}
	
	this.getLastErrors = function() { return this.errors; }
	
	var THIS = this;
	this.form.onsubmit = function() {
		return THIS.checkAll();
	}
        
	
	this.scanStdChecks();

}

//Associamo automaticamente un FormChecker a ciascuna form
//Automatically create a Formchecker for each document form
function init() {
	var i;
	for(i=0; i<document.forms.length; ++i) {
		document.forms[i].checker = new FormChecker(document.forms[i]);
	}	
}

window.onload = init;