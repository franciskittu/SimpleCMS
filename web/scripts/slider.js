

function eliminateOpacity(curr, next){
	curr.css("opacity", 0);
	next.css("opacity", 0);
}

function changeCurrent(curr, next){
	curr.removeClass("current");
	next.addClass("current");

}

function restoreOpacity(curr, next){
        
	curr.css("opacity", 1);
	next.css("opacity", 1);
}


function hideSlide(first){

	var curr= $(".current");
		
	var next= curr.next();
	
	var control=next.is(".slide");
	
	if( !control){  
		 next= first;
	}
	eliminateOpacity(curr, next);
	
	setTimeout(changeCurrent,550 , curr, next);
	
	setTimeout(restoreOpacity, 750,curr, next);
	
		
}
function initSlider(){

	var first=$(".slide").first();
        if(first.length>0){
            setInterval(hideSlide,12000, first);
        }
}





