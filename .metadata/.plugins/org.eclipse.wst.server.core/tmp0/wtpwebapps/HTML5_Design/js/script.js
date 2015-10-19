/*=================================================
	1 - �ㅽ겕濡� �대룞 
=================================================*/
$("a.page-scroll").on('click', function(event) {
	event.preventDefault();
	
	var $anchor 	= $(this);
	var nav_height	= $(".main-navbar").outerHeight();
	var go_postion	= $($anchor.attr('href')).offset().top;
	
	$('html, body').stop().animate({
		scrollTop: go_postion
	}, 1500, 'easeInOutExpo');
});

/*=================================================
	2 - 硫붿씤 �ㅻ퉬寃뚯씠�� �ㅼ젙
=================================================*/
$(window).scroll(function(){
	if($(document).scrollTop() > $("#main-gnb").outerHeight()){
		$("#main-gnb").addClass("float");
	} else {
		$("#main-gnb").removeClass("float");
	}
});


/*=================================================
	2 - �ㅽ겕濡� �� 踰꾪듉 �ㅼ젙
=================================================*/
$(window).scroll(function(){
	if($(document).scrollTop() > 200){
		$(".scroll-up").fadeIn("slow");
		if(isMobile.any()){
			$(".fixed-call-btn").fadeIn("slow");
		}
	} else {
		$(".scroll-up").fadeOut("slow");
		if(isMobile.any()){
			$(".fixed-call-btn").fadeOut("slow");
		}
		
	}
});

$(".scroll-up").on("click", function(){
	$("html, body").animate({ scrollTop: 0 }, 1500, "easeInOutExpo");
	return false;
});

window.onbeforeunload = function() {
    //return "�� �섏씠吏�瑜� 踰쀬뼱�� �ㅻⅨ �섏씠吏�濡� �대룞�⑸땲��.";
};