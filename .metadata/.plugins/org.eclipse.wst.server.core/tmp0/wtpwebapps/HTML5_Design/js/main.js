$(function(){
	/*=================================================
		00. 怨듯넻
	=================================================*/
	/////#####===== Wow Slider =====#####/////
	var wow	= new WOW().init();
	
	$(".loading-layer").fadeOut();
	/////#####===== Image Loaded =====#####/////
	/*
	var $imgLoaded	= $(".first-image").imagesLoaded();
	
	$imgLoaded.done(function(instance){
		$(".loading-layer").fadeOut();
	});
	
	$(window).load(function(){
		setTimeout(function(){
			$('html, body').scrollTop(0);
			$(".loading-layer").fadeOut();
		}, 10);
	});
	*/
	
	var section_stay_check;
	var section_stay_time	= 0;
	var section_stay = function(elem){
		clearInterval(section_stay_check);
		section_stay_time	= 0;
		
		var section_id	= elem.id;
		
		section_stay_check	= setInterval(function(){
			section_stay_time++;
			
			if(section_stay_time > 1){
				ga("send", "pageview", "/#"+section_id);
				
				clearInterval(section_stay_check);
				section_stay_time	= 0;
			}
		}, 1000);
	}
	
	$("section").waypoint(function(direction) {
		if(direction=="down") {
			section_stay(this.element);
		}
	}, {
		offset: 10
	});
	
	$("section").waypoint(function(direction){
		if(direction=="up") {
			section_stay(this.element);
		}
	}, {
		offset: 0
	});
	
	/*=================================================
		01. �ㅻ뜑
	=================================================*/
	/////#####===== Revolution Slider =====#####/////
	$(".tp-banner").revolution({
		delay:4000,
		startheight:850,
		minHeight:400,
		keboardNavigation:"on",
		onHoverStop:"off",
		navigationType:"none",
		navigationArrows:"solo",
		navigationStyle:"preview4",
		touchenabled:"on",
		hideArrowsOnMobile:"off",
		swipe_velocity:0.7,
		swipe_min_touches:1,
		swipe_max_touches:1,
		drag_block_vertical:false,
		soloArrowLeftHalign:"left",
		soloArrowLeftValign:"center",
		soloArrowLeftHOffset:20,
		soloArrowLeftVOffset:0,
		soloArrowRightHalign:"right",
		soloArrowRightValign:"center",
		soloArrowRightHOffset:20,
		soloArrowRightVOffset:0,
		hideTimerBar:"on",
		shadow:0,
		fullWidth:"off",
		fullScreen:"on",
		autoHeight:"off",
		forceFullWidth:"off",
		dottedOverlay:"none",
		shuffle:"off",
		parallax:"mouse",
		parallaxLevels:[7,4,3,2,5,4,3,2,1,0]
	});
	
	//$.getScript("//www.youtube.com/iframe_api");
	
	$(".yt-start-btn").on("click", function(){
		$('#yt-video')[0].src += "&autoplay=1";
	});
	
	/*=================================================
		03. Feature
	=================================================*/
	/////#####===== �щ씪�대뜑 =====#####/////
	$("#feature-slider").animateSlider({
		autoplay: true,
		interval: 4000,
		animations: {
			0: {
				".feature-title": {
					show   	  : "fadeInUp",
					hide 	  : "fadeOutUp",
					delayShow : "delay1s"
				},
				".feature-desc": {
					show   	  : "fadeInUp",
					hide 	  : "fadeOutUp",
					delayShow : "delay1-5s"
				}
			},
			1: {
				".feature-title": {
					show   	  : "fadeInUp",
					hide 	  : "fadeOutUp",
					delayShow : "delay1s"
				},
				".feature-desc": {
					show   	  : "fadeInUp",
					hide 	  : "fadeOutUp",
					delayShow : "delay1-5s"
				}
			},
			2: {
				".feature-title": {
					show   	  : "fadeInUp",
					hide 	  : "fadeOutUp",
					delayShow : "delay1s"
				},
				".feature-desc": {
					show   	  : "fadeInUp",
					hide 	  : "fadeOutUp",
					delayShow : "delay1-5s"
				}
			},
			3: {
				".feature-title": {
					show   	  : "fadeInUp",
					hide 	  : "fadeOutUp",
					delayShow : "delay1s"
				},
				".feature-desc": {
					show   	  : "fadeInUp",
					hide 	  : "fadeOutUp",
					delayShow : "delay1-5s"
				}
			},
			4: {
				".feature-title": {
					show   	  : "fadeInUp",
					hide 	  : "fadeOutUp",
					delayShow : "delay1s"
				},
				".feature-desc": {
					show   	  : "fadeInUp",
					hide 	  : "fadeOutUp",
					delayShow : "delay1-5s"
				}
			}
		}
	});
	
	/*=================================================
		05. Responsive Web Design
	=================================================*/
	/////#####===== �щ씪�대뜑 =====#####/////
	$("#responsive-slider").animateSlider({
		autoplay: true,
		interval: 5000,
		animations: {
			0: {
				".pc": {
					show   	  : "bounceIn",
					hide 	  : "fadeOut",
					delayShow : "delay1s"
				},
				".pad": {
					show   	  : "bounceInRight",
					hide 	  : "fadeOutRight",
					delayShow : "delay1-5s"
				},
				".laptop": {
					show   	  : "bounceInLeft",
					hide 	  : "fadeOutLeft",
					delayShow : "delay2s"
				},
				".phone": {
					show   	  : "bounceInRight",
					hide 	  : "fadeOutRight",
					delayShow : "delay2-5s"
				}
			},
			1: {
				".pc": {
					show   	  : "rotateIn",
					hide 	  : "fadeOut",
					delayShow : "delay1s"
				},
				".pad": {
					show   	  : "rotateIn",
					hide 	  : "fadeOutRight",
					delayShow : "delay1-5s"
				},
				".laptop": {
					show   	  : "rotateIn",
					hide 	  : "fadeOutLeft",
					delayShow : "delay2s"
				},
				".phone": {
					show   	  : "rotateIn",
					hide 	  : "fadeOutRight",
					delayShow : "delay2-5s"
				}
			},
			2: {
				".pc": {
					show   	  : "fadeInDown",
					hide 	  : "fadeOut",
					delayShow : "delay1s"
				},
				".pad": {
					show   	  : "fadeInRight",
					hide 	  : "fadeOutRight",
					delayShow : "delay1-5s"
				},
				".laptop": {
					show   	  : "fadeInLeft",
					hide 	  : "fadeOutLeft",
					delayShow : "delay2s"
				},
				".phone": {
					show   	  : "fadeInUp",
					hide 	  : "fadeOutRight",
					delayShow : "delay2-5s"
				}
			}
		}
	});
	
	/*=================================================
		06. Process
	=================================================*/
	/////#####===== �꾨줈�몄뒪 �щ씪�대뜑 =====#####/////
	var process_slider	= $("#process-slider").bxSlider({
		auto: true,
		controls: false,
		hideControlOnEnd: true
	});
	
	/*=================================================
		07. Works
	=================================================*/
	/////#####===== Works Isotope =====#####/////
	$(window).load(function(){
		var $container = $("#works-grid");
		
		$container.isotope({
			itemSelector: '.grid-item',
			percentPosition: true,
			masonry: {
				columnWidth: '.grid-sizer'
			}
		});
		
		$container.isotope({filter:".business"});
		
		$("#works").find(".bcat-btn").on("click", function(){
			$("#works").find(".bcat-btn").removeClass("active");
			$(this).addClass("active");
			
			var filter_value	= $(this).data("filter")
			$container.isotope({filter:filter_value});
		});
	});
	
	/////#####===== Count To =====#####/////
	$(".counter").each(function(){
		var $counter	= $(this);
		
		$counter.waypoint(function(direction){
			if(!$counter.hasClass('animated')){
				$counter.countTo({
					speed: 2000,
					refreshInterval: 100,
					onComplete: function(){
						$counter.addClass("animated");
					}
				});
			} 
		}, {
			offset: '100%'
		});
	});
	
	/*=================================================
			10. Samples
	=================================================*/
	/////#####===== �섑뵆 �щ씪�대뜑 =====#####/////
	var sample_slider	= $("#sample-slider").bxSlider();
	
	/////#####===== �섑뵆 �ъ떆 諛뺤뒪 =====#####/////
	$(".sample-img-wrap").fancybox({
		padding		: 0,
		fitToView	: false,
		width		: '98%',
		height		: '95%',
		autoSize	: false,
		closeClick	: false,
		iframe: {
			preload: false
		}
	});
	
	/////#####===== Samples Board �ㅼ젙 =====#####/////
	$("#sample-board").ajaxPaiging(function(rcv){
		var lists_output	= "";
		
		var sample_idx		= 0;
		var sample_limit	= isMobile.any()?4:8;
		var sample_count	= rcv.lists.length;
		var sample_slide	= Math.ceil(sample_count/sample_limit);
		
		for(var i=0; i<sample_slide; i++){
			lists_output	+=	"<li>";
			lists_output	+=		"<div class='row row_5'>";
			
			for(var j=0; j<sample_limit; j++){
				lists_output	+=		"<div class='col-sm-3 col-xs-6'>";
				lists_output	+=			"<a href='"+rcv.lists[sample_idx].bdata_9+"' class='sample-img-wrap ajax-board-item' data-fancybox-type='iframe'>";
				lists_output	+=				"<img src='"+rcv.lists[sample_idx].bc_list+"' class='width-100'>";
				lists_output	+=			"</a>";
				lists_output	+=		"</div>";
				
				sample_idx++;
				if(sample_idx==sample_count){break;}
			}
			
			lists_output	+=		"</div>";
			lists_output	+=	"</li>";
		}
		
		return lists_output;
	}, function(){
		sample_slider.reloadSlider();
	});
	
	/*=================================================
			11. Q&A
	=================================================*/
	/////#####===== QnA Board �ㅼ젙 =====#####/////
	$("#qna-board").ajaxPaiging(function(rcv){
		var lists_output	= "";
		
		$.each(rcv.lists, function(key, val){
			lists_output	+=	"<tr>";
			lists_output	+=		"<td>"+(parseInt(rcv.index_num)-key)+"</td>";
			lists_output	+=		"<td class='subject'>";
			lists_output	+=			"<span class='fsp-open-btn "+(val.bc_type==2?"lock":"")+"' data-idx='"+val.bc_idx+"' data-popup='board'>"+val.bc_subject+"</span>";
			lists_output	+=			(val.bc_type==2)?" <i class='fa fa-lock'><i>":"";
			lists_output	+=		"</td>";
			lists_output	+=		"<td class='visible'>"+get_bc_status(val.bc_status)+"</td>";
			lists_output	+=		"<td>"+val.bc_writer_name+"</td>";
			lists_output	+=		"<td>"+val.bc_regdate.substr(0, 10)+"</td>";
			lists_output	+=	"</tr>";
		});
		
		return lists_output;
	});
	
	/////#####===== Board �쒕툕諛� =====#####/////
	$(document).on("click", ".board-submit-btn", function(){
		var form	= $(this).data("form")||$(this).parents("form").attr("id");
		var board	= $(this).data("board");
		
		checkFormValid($("#"+form), function(rcv){
			var obj	= $.parseJSON(rcv);
			
			var index	= Number($(board).find("table tbody tr").first().find("td").first().text())+1;
			
			var output	= "";
			output	+=	"<tr>";
			output	+=		"<td>"+index+"</td>";
			output	+=		"<td class='subject'>";
			output	+=			"<span class='fsp-open-btn' data-idx='"+obj.bc_idx+"' data-popup='board'>"+obj.bc_subject+"</span>";
			output	+=		"</td>";
			output	+=		"<td class='visible'>"+get_bc_status(obj.bc_status)+"</td>";
			output	+=		"<td>"+obj.bc_writer_name+"</td>";
			output	+=		"<td>"+obj.bc_regdate.substr(0, 10)+"</td>";
			output	+=	"</tr>";
			
			$(board).find("table tbody").prepend(output);
			$(board).find("table tbody tr").last().remove();
		});
	});
	
	/*=================================================
		12. Contact
	=================================================*/
	/////#####===== 吏��꾩꽕�� =====#####/////
	/*
	var map_canvas	= document.getElementById("map-canvas");
	var myLatlng	= new google.maps.LatLng(37.480362, 126.877106);
	
	var mapOptions	= {
		center		: myLatlng,
		zoom		: 20,
		mapTypeId	: google.maps.MapTypeId.HYBRID
	};
	
	var map = new google.maps.Map(map_canvas, mapOptions);
	
	var marker = new google.maps.Marker({
		position	: myLatlng,
		map			: map
	});
	*/
	var map_canvas = document.getElementById('map-canvas');
	var myLatlng	= new daum.maps.LatLng(36.7216863, 128.8166482);
	
	var mapOptions = {
		center: myLatlng,
		level: 0,
		mapTypeId	: daum.maps.MapTypeId.HYBRID
	};
	
	var map = new daum.maps.Map(map_canvas, mapOptions);
	
	var markerPosition  = new daum.maps.LatLng(37.484538, 126.879055);
	
	var marker = new daum.maps.Marker({
	    position: myLatlng
	});
	
	marker.setMap(map);
	
	var mapTypeControl = new daum.maps.MapTypeControl();
	map.addControl(mapTypeControl, daum.maps.ControlPosition.TOPRIGHT);
	
	var zoomControl = new daum.maps.ZoomControl();
	map.addControl(zoomControl, daum.maps.ControlPosition.RIGHT);
	
	/////#####===== �ㅼ쐞移� �ㅼ젙 =====#####/////
	$(".map-switch").bootstrapSwitch({
		onSwitchChange: function(event, state){
			if(state){
				$("#map-canvas").css("z-index", "10");
				
				$('html, body').stop().animate({
					scrollTop: $("#contact").offset().top
				}, 500, 'easeInOutExpo');
			} else {
				$("#map-canvas").css("z-index", "-100");
				
				$('html, body').stop().animate({
					scrollTop: $("#contact").offset().top
				}, 500, 'easeInOutExpo');
			}
		}
	});
});