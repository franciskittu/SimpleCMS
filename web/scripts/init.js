// importa il modello selezionato nella select attraverso una chiamata ajax asincrona
function importModel(){
    var selected=$(this).children("option:selected");
    var id= selected.val();
    if(id=="0"){
        $("#form_edit input[name=title]").val('');
        CKEDITOR.instances.editor.setData('');
    }else{
        $.ajax({
                url: "edit",
                dataType: 'json',
                data: {
                    id:encodeURIComponent(id)
                },
                success: function(data){$("#form_edit input[name=title]").val(data.title); CKEDITOR.instances.editor.setData('', function(){CKEDITOR.instances.editor.insertHtml(data.body); }); },
                error: function(){alert('Errore trasferimento dati');}
        });
        
    }
    return false;
}

//aggiunge l'immagine selezionata nell'editor

function addImg(e){
    var src=e.data.src;
    var name = e.data.name;
    var editor=e.data.editor;
    editor.insertHtml('<img src="'+src+'" alt="'+name+'" />');
}


//sovrappone al  bottone dell'imput di file uno più gradevole visivamente e lo stesso fa per il bottone di submit rendendolo però anche invisibile

function initButtonUpload(){
    var in_upload=$(' .images input[type=submit] ');
    var in_file=$(' .images input[type=file] ');
    in_upload.css("visibility", "hidden");
    in_file.css("display", "none");
    var button_browse=$('<a href="javascript:void(0)" class="button browse">browse</a>');
    var button_upload=$('<a  href="javascript:void(0)"class="button upload">upload</a>');
    var wrap=$(".wrapper_content");
     
    if(window.ActiveXObject!==undefined){
        in_file.bind("click", function(){
           setTimeout(function(){
                        if(button_browse.next().is("span")){
                            button_browse.next().empty();
                            button_browse.next().text(in_file.val());
                        }else{   
                            $("<span>"+in_file.val()+"</span>").insertAfter(button_browse);
               
                        }
           }, 0);
        });
   }else{  
        in_file.bind("change", function(){
          if(button_browse.next().is("span")){
             button_browse.next().empty();
             button_browse.next().text(in_file.val());
          }else{
              $("<span>"+in_file.val()+"</span>").insertAfter(button_browse);
               
          }
        });
    }
    button_browse.bind("click", function(){in_file.click();});
    button_upload.bind("click", function(){in_upload.click();});
    wrap.append(button_browse);
    wrap.append(button_upload);
     
 
}

//nasconde l'albero e mostra l'editor

function showEditor(){
        
        $(".add_img").css("bottom", "5px");
        $("#tree").css("left", "-100%");
        setTimeout(function(){$("#tree").css("height", "0");$("#form_edit").css("height", "auto");},1000);

}


// nasconde l'editor e mostra l'albero, reimposta tutti i campi di form ai valori iniziali(disabilitandoli e rendendo vuoto il loro valore), l'unico campo che non vien disabilitato è quello dell'editor 


function showTree(){
    
    $("#form_edit").get(0).setAttribute("action", "edit");
    $("#form_edit fieldset:nth-child(2)").css("display", "none");
    $("#form_edit fieldset:nth-child(4)").css("display","none");
    $("#form_edit select").css("display","none");
    $("#form_edit input[name=model]").attr("disabled", "disabled");
    $("#form_edit input[name=model]").removeAttr("checked");
    $("#form_edit input[name=title]").attr("disabled", "disabled");
    $("#form_edit input[name=user]").attr("disabled", "disabled");
    $("#form_edit input[type=hidden]").attr("disabled", "disabled");
    $("#form_edit select").attr("disabled", "disabled");
    $("option[value=0]").attr("selected", "selected");
    $("#form_edit input[name=title]").val('');
    $("#form_edit input[name=id]").val('');
    $("#form_edit input[name=type]").val('');
    CKEDITOR.instances.editor.setData('');
    $("#form_edit").css("height", "0");
    $("#tree").css("height", "auto");
    $("#tree").css("left", "0");
    $(".add_img").css("bottom", "-100%");

    
}


//crea uno oggetto che associa al parametro una funzione per la gestione dell'evento click , la funzione effettua una chiamata ajax per ricevere il corpo dell'header/footer

function smartButtonEditHF(button){

    var type=button.attr("id");
    
    
    this.success=function(data){
        
        $("#form_edit input[name=type]").removeAttr("disabled");
        $("#form_edit input[name=type]").val(type);
        alert(data.body+" "+data.title+" "+data.checked);
        CKEDITOR.instances.editor.insertHtml(data.body);
        showEditor();
    };
    
    this.error=function(){alert("Errore trasferimento dati");};
    
    var success=this.success;
    var error=this.error;

    
    this.send=function(e){
        if( type){
            $.ajax({
                url: "edit",
                dataType: 'json',
                data: {
                    type:encodeURIComponent(type)
                },
                success: success,
                error: error
        });
        }
        return false;
    };
    
    button.bind("click", this.send);
}


//crea uno oggetto che associa al parametro una funzione per la gestione dell'evento click , la funzione effettua una chiamata ajax per ricevere i dati relativi alla pagina

function smartButtonEdit(button){
    
    var id=button.attr("id").replace("edit", "");
    
    this.success=function(data){
        
        $("#form_edit fieldset:nth-child(2)").css("display", "block");
        $("#form_edit fieldset:nth-child(4)").css("display", "block");
        $("#form_edit input[name=title]").removeAttr("disabled");
        $("#form_edit input[name=model]").removeAttr("disabled");
        $("#form_edit input[name=id]").removeAttr("disabled");
        $("#form_edit input[name=title]").val(data.title);
        if(data.checked !== "0"){$("#form_edit input[type=checkbox]").attr(data.checked, data.checked);}
        $("#form_edit input[name=id]").val(id);
        CKEDITOR.instances.editor.insertHtml(data.body);
        
        showEditor();
        
    }
    
    this.error=function(){alert("Errore trasferimento dati");}
    
    var success=this.success;
    var error=this.error;
    
    this.send=function(e){
        alert(id);
        if( id){
            $.ajax({
                url: "edit",
                dataType: 'json',
                data: {
                    id:encodeURIComponent(id)
                },
                success: success,
                error: error
        });
        }
        return false;
    }
    
    button.bind("click", this.send);
}


//mostra l'editor e setta il campo id della form a quella del padre della pagina che verrà creata 

function smartButtonAdd(button){        
    var id=button.attr("id").replace("add", "");
    
    this.activate=function(){
        
        $("#form_edit").get(0).setAttribute("action", "add");
        $("#form_edit fieldset:nth-child(2)").css("display", "block");
        $("#form_edit fieldset:nth-child(4)").css("display", "block");
        $("#form_edit select").css("display", "inline");
        $("#form_edit span.select").css("display", "inline");
        $("#form_edit input[name=title]").removeAttr("disabled");
        $("#form_edit input[name=model]").removeAttr("disabled");
        $("#form_edit input[name=id]").removeAttr("disabled");
        $("#form_edit input[name=id]").val(id);
        $("#form_edit select").removeAttr("disabled");
        
        showEditor();
        
    }
        
        
    button.bind("click", this.activate);
}
    
    
//crea uno oggetto che associa al parametro una funzione per la gestione dell'evento click , la funzione effettua una chiamata ajax in cui chiede la modifica della cover del sito
    
    function smartButtonCover( button ){
    this.id= button.attr("id").replace("img","");
    this.href=button.attr("href");
    
    this.success=function(data){
                            var old= $("#img"+data.img_old);
                            var curr=$("#img"+data.img_current);
                            old.removeClass("set");
                            curr.addClass("set");}
    
    var href=this.href;
    
    this.error=function(){ location.href=href; }
    var id=this.id;
    var success=this.success;
    var error=this.error;
    
    this.send= function(e){
        if(id){
            
	$.ajax({
            url: "cover",
            dataType: 'json',
            data: {
                id:encodeURIComponent(id),
                json:encodeURIComponent("true")
            }
            ,
            success: success,
            error: error
            });          
        }
        
        
        return false;
      } 
      
      button.attr("href", "javascript:void(0)");
      button.bind("click", this.send);
    
}

//crea uno oggetto che associa al parametro una funzione per la gestione dell'evento click , la funzione effettua una chiamata ajax in cui chiede la modifica dello stile del sito

function smartButtonCss( button ){
    var href= button.attr("href");
    var id=button.attr("id").replace("css","");
 
    
    this.success=function(data){
                            alert(data.css_old+" "+data.css_current);
                            var old= $("#css"+data.css_old);
                            var curr=$("#css"+data.css_current);
                            old.removeClass("active");
                            curr.addClass("active");}
    
    
    this.error=function(){ location.href=href; }
    
    var success=this.success;
    var error=this.error;
    
    this.send= function(e){

        if(id){
	$.ajax({
            url: "style",
            dataType: 'json',
            data: {
                id:encodeURIComponent(id),
                json:encodeURIComponent("true")
            }
            ,
            success: success,
            error: error
            });          
        }
        
        return false;
      } 
      
      button.attr("href", "javascript:void(0)");
      button.bind("click", this.send);
    
}

// inizializza tutti gli oggetti e chiama tutte funzioni necessarie al site manager per fornire i propri servizi

function initSmartButtons(){
    
    CKEDITOR.replace("editor",
  { allowedContent:
                    'h1 h2 h3 p pre[align]; ' +
                    'blockquote code kbd samp var del ins cite q b i u strike ul ol li hr table tbody tr td th caption; ' +
                    'img[!src,alt,align,width,height]; font[!face]; font[!family]; font[!color]; font[!size]; font{!background-color}; a[!href]; a[!name]'});
    var editor=CKEDITOR.instances.editor;
    editor.on('instanceReady',function(){editor.setData('');});
    
    
    
    $("#back").bind("click",showTree);
    
    var buttons_css=$(".style .button");
    buttons_css.each( function(){
                 this.button= new smartButtonCss($(this));   
    });
    
    var buttons_cover=$(".cover");
    buttons_cover.each( function(){
                 this.button= new smartButtonCover($(this));   
    });
    
    var buttons_edit=$(".edit");
    buttons_edit.css("display", "block");
    buttons_edit.each(function(){
        
        this.button=new smartButtonEdit($(this));
    });
    
    var buttons_edit_hf=$("#header, #footer");
    buttons_edit_hf.css("display", "initial");
    
    buttons_edit_hf.each(function(){
        this.button=new smartButtonEditHF($(this));
    });
    
    var buttons_add=$(".add");
    buttons_add.css("display", "block");
    
    buttons_add.each(function(){
        this.button=new smartButtonAdd($(this));
    });
    
    
    
    var buttons_add_img=$(".add_img");
    buttons_add_img.each(function(){ 
        var img=$(this).siblings("img").attr("src").replace("/thumbs","");
        var nome_img=$(this).siblings("img").attr("alt");
        $(this).bind("click",{src:img, editor:editor,name:nome_img}, addImg);
    });
    
    
    var select_model=$("select[name=impmodel]");
    select_model.bind("change", importModel);
    $("option[value=0]").attr("selected", "selected");
    
    initButtonUpload();
    
}



function initJSMode(){
    var sitemap=$(".sitemap");
    if(sitemap.length>0){
        
        $("#avviso").css("display", "none");
        initSmartButtons();
    }
}




function init(){
    
    initSlider();
    initJSMode();
    
}

$(init);


