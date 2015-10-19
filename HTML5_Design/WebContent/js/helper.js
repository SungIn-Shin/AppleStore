/*=================================================
	1 - �쒕툕諛� 踰꾪듉
=================================================*/
$(".submit_btn").on("click", function(){
	var form			= $(this).data("form")||$(this).parents("form").attr("id");
	var confirm_text	= $(this).data("confirm")||"";
	
	if(confirm_text!=""){
		if(confirm(confirm_text)){
			checkFormValid($("#"+form));
		}
	} else {
		checkFormValid($("#"+form));
	}
});

/*=================================================
	1 - �낅젰 蹂�寃�
=================================================*/
$(".replace_input").keyup(function(){
	var value	= $(this).val();
	var target	= $(this).data("target");
	var content	= $(this).data("content").replace("{value}", value);
	
	$(this).parents("form").find(target).val(content);
});

$(".replace_select").change(function(){
	var value	= $(this).find("option:selected").text();
	var target	= $(this).data("target");
	var content	= $(this).data("content").replace("{value}", value);
	
	$(this).parents("form").find(target).val(content);
});


/*=================================================
	2 - �� �좏슚�� 寃���
=================================================*/
function checkFormValid(form){
	var result		= true;
	var callback	= arguments[1]||false;

	$(form).find(".valid_input").not(".valid_skip").each(function(key, val){
		//##### �쇰꺼 �뺣낫 �ㅼ젙 #####//
		var label	= $(val).data("label")?$(val).data("label"):"";
		
		if(label==undefined || label==""){
			if(label==""){
				label	= $(val).parents(".form-group").find(".control-label").text()
			}
			if(label==""){
				label	= $(val).parents("tr").find("th").text();
			}
			if(label==""){
				label	= "�꾩닔媛�";
			}
		}
		
		label	= $.trim(label);
		
		var action	= "�낅젰";
		
		//##### �좏슚�� �뺣낫 �ㅼ젙 #####//
		//-- 鍮덇컪 泥댄겕 --//
		var value	= $(val).val()||"";
		var name	= ($(val).data("name")!="" && $(val).data("name")!=undefined)?$(val).data("name"):$(val).attr("name");

		if($(val).get(0).tagName.toUpperCase()=="SELECT"){
			value = $(val).find("option:selected").val();
			
			action	= "�좏깮";
		}
		else if($(val).attr("type")=="radio"){
			var radio_name	= $(val).attr("name");
			value = $(form).find("[name="+radio_name+"]").is(":checked");
			
			action	= "�좏깮";
		}
		else if($(val).attr("type")=="checkbox"){
			value 	= $(val).prop("checked");
			
			action	= "泥댄겕";
		}
		
		//-- 湲고� �좏슚�� 泥댄겕 --//
		var required= $(val).data("required");
		var min_len	= $(val).data("minlen")||"";
		var max_len	= $(val).data("maxlen")||"";
		var exp		= $(val).data("exp")||"";
		var re_pw	= $(val).data("repw")||"";
		var table	= $(val).data("table")||"";	
		var output	= "";
		
		
		//##### 鍮덇컪 �뺤씤 #####//
		if(required!=false && ((value==false) || (value=="") || ($(val).get(0).tagName.toUpperCase()=="SELECT" && value==0))){
			output	= label+getPostposition(label,"��")+" "+action+"�� 二쇱꽭��";
			
			alert(output);
			$(val).focus();
			
			result = false;
			return false;
		}
		
		//##### 理쒖냼 湲��� �뺤씤 #####//
		else if(min_len!="" && value.length<min_len){
			output	= label+getPostposition(label,"��")+" "+min_len+" 湲��� �댁긽�댁뼱�� �⑸땲��";
			
			alert(output);
			$(val).focus();
			
			result = false;
			return false;
		}
		
		//##### 理쒕� 湲��� �뺤씤 #####//
		else if(max_len!="" && value.length>max_len){
			output	=  label+getPostposition(label,"��")+" "+min_len+"湲��� �댄븯�댁뼱�� �⑸땲��";
			
			alert(output);
			$(val).focus();
			
			result = false;
			return false;
		}
		
		//##### 鍮꾨�踰덊샇 �뺤씤 #####//
		else if(re_pw!="" && $("[name="+re_pw+"]").val()!=value){
			output	=  "鍮꾨�踰덊샇媛� �쇱튂�섏� �딆뒿�덈떎.";
			
			alert(output);
			$(val).focus();
			
			result = false;
			return false;
		}
		
		//##### �뺢퇋�� �뺤씤 #####//
		else if(value!="" && exp!="" && testRegExp(exp, value)!=true){
			output	= label+" "+testRegExp(exp, value);
			
			alert(output);
			$(val).focus();
			
			result = false;
			return false;
		}
		
		//##### 以묐났 �뺤씤 #####//
		else if(name!="" && table!=""){
			var snd	= {
				table	: table,
				where	: "`"+name+"`='"+value+"'",
				ajax	: "true"
			}
			
			$.ajax({
				type: "POST",
				async: false,
				url: "/_crud/select",
				data: $.param(snd),
				success: function(rcv){
					var obj	= $.parseJSON(rcv);
					
					if(obj.length > 0){
						alert(label+"�� 以묐났�� 媛믪씠 議댁옱�⑸땲��.");
						$(val).focus();
						
						result = false;
						return false;
					}
				}
			});
			
			if(!result){
				return false;
			}
		}
	});
	
	//##### AJAX �� �뺤씤 #####//
	var is_ajax_form	= ($(form).find("input[name=ajax]").val()=="true")?true:false;
	
	//##### Valid 寃곌낵 #####//
	if(result){
		//##### Callback �ㅽ뻾 #####//
		if(is_ajax_form){
			var action		= $(form).attr("action");
			var ajax_alert	= $(form).find("input[name=ajax_alert]").val();
			var ajax_empty	= $(form).find("input[name=ajax_empty]").val();
			var close_modal	= $(form).find("input[name=close_modal]").val();
			
			$.post(action, $(form).serialize(), function(rcv){
				if(rcv.trim()!="ok"){
					var obj	= $.parseJSON(rcv);
				}
				
				//== �� 鍮꾩슦湲� ==//
				if(ajax_empty!="false"){
					$(form).find(".form-control").val("");
				}
				
				//== Modal �リ린 ==//
				if(close_modal){
					$(close_modal).modal('hide');
				}
				
				//== �뚮┝李� ==//
				if(ajax_alert){
					alert(ajax_alert);
				}
				
				//== 肄쒕갚 ==//
				if(typeof(callback) == "function"){
					callback(rcv);
				}
			});
		}
		else {
			if(typeof(callback) == "function"){
				callback();
			} else {
				$(form).submit();
			}
		}
	}
}

/*=================================================
	3 - �뺢퇋�� 泥댄겕
=================================================*/
function testRegExp(type, str){
	var regExp;
	var errText;
	
	switch(type){
		case "num":
			regExp	= /^[0-9]*$/;
			errText	= "�レ옄留� �낅젰 媛��ν빀�덈떎.";
			break;
		case "dashnum":
			regExp	= /^[0-9-]*$/;
			errText	= "�レ옄�� ����(-)留� �낅젰 媛��ν빀�덈떎.";
			break;
		case "low_eng":
			regExp	= /^[a-z]*$/;
			errText	= "�곸뼱 �뚮Ц�먮쭔 �낅젰 媛��ν빀�덈떎.";
			break;
		case "up_eng":
			regExp	= /^[A-Z]*$/;
			errText	= "�곸뼱 ��臾몄옄留� �낅젰 媛��ν빀�덈떎.";
			break;
		case "eng":
			regExp	= /^[A-Za-z]*$/;
			errText	= "�곸뼱留� �낅젰 媛��ν빀�덈떎.";
			break;
		case "low_eng_num":
			regExp	= /^[a-z0-9_]*$/;
			errText	= "�곸뼱 �뚮Ц�먯� �レ옄留� �낅젰 媛��ν빀�덈떎.";
			break;
		case "up_eng_num":
			regExp	= /^[A-Z0-9_]*$/;
			errText	= "�곸뼱 ��臾몄옄�� �レ옄留� �낅젰 媛��ν빀�덈떎.";
			break;
		case "eng_num":
			regExp	= /^[A-Za-z0-9_]*$/;
			errText	= "�곸뼱�� �レ옄留� �낅젰 媛��ν빀�덈떎.";
			break;
		case "email":
			regExp	= /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
			errText	= "�щ컮瑜� �대찓�� 二쇱냼留� �낅젰 媛��ν빀�덈떎.";
			break;
		case "mobile":
			regExp	= /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;
			errText	= "�щ컮瑜� �대��� 踰덊샇留� �낅젰 媛��ν빀�덈떎.";
			break;
		case "phone":
			regExp	= /^\d{2,3}-\d{3,4}-\d{4}$/;
			errText	= "�щ컮瑜� �꾪솕踰덊샇留� �낅젰 媛��ν빀�덈떎.";
			break;
		case "url":
			regExp	= /^http/;
			errText	= "�щ컮瑜� �꾨찓�� �뺣낫留� �낅젰 媛��ν빀�덈떎.";
			break;
	}
	
	var result	= regExp.test(str)?true:errText;
	
	return result;
}

/*=================================================
	4 - 議곗궗 異쒕젰
=================================================*/
function getPostposition(str, pp){
	var final_str			= str.charAt(str.length-1);
	var is_final_consonant	= checkConsonant(final_str).length==3?true:false;
	
	if(pp=='��' || pp=='瑜�') return (is_final_consonant?'��':'瑜�');
	if(pp=='��' || pp=='媛�') return (is_final_consonant?'��':'媛�');
	if(pp=='��' || pp=='��') return (is_final_consonant?'��':'��');
	if(pp=='��' || pp=='怨�') return (is_final_consonant?'��':'怨�');
}

/*=================================================
	5 - 珥� / 以� / 醫낆꽦 泥댄겕
=================================================*/
function checkConsonant(str){
	var cCho  = [ '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��' ],
        cJung = [ '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��' ],
        cJong = [ '', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��' ],
        cho, jung, jong;

    var str = str,
        cnt = str.length,
        chars = [],
        cCode;

    for (var i = 0; i < cnt; i++) {
        cCode = str.charCodeAt(i);
        
        if (cCode == 32) { continue; }

        // �쒓��� �꾨땶 寃쎌슦
        if (cCode < 0xAC00 || cCode > 0xD7A3) {
            chars.push(str.charAt(i));
            continue;
        }

        cCode  = str.charCodeAt(i) - 0xAC00;

        jong = cCode % 28; // 醫낆꽦
        jung = ((cCode - jong) / 28 ) % 21; // 以묒꽦
        cho  = (((cCode - jong) / 28 ) - jung ) / 21; // 珥덉꽦

        chars.push(cCho[cho], cJung[jung]);
        if (cJong[jong] !== '') { chars.push(cJong[jong]); }
    }

    return chars;
}

/*=================================================
	2 - 寃뚯떆臾� �곹깭
=================================================*/
function get_bc_status(key){
	var array	= ["","�깅줉","�듬���湲�","�듬��꾨즺","��湲곗쨷","吏꾪뻾以�","�꾨즺","誘몄듅��"];
	
	if(key!=""){
		return array[key];
	} else {
		return array;
	}
}

/*=================================================
	2 - 諛곗뿴 �덉뿉 苑됱감寃� �덈뒗吏� �뺤씤
=================================================*/
function isFullArray(arr, cnt){
	var is_full	= true;
	
	for($i=1; $i<cnt+1; $i++){
		if(arr[$i]==undefined || arr[$i]=="" || arr[$i]==0){is_full	= false;}
	}
	
	return is_full;
}

/*=================================================
	2 - 諛곗뿴 �덉쓽 �レ옄 �⑹튂湲�
=================================================*/
function sumArray(arr){
	var sum	= 0
	
	$.each(arr, function(idx, val){
		if($.isNumeric(val)){sum+=Number(val);}
	});
	
	return sum;
}

/*=================================================
	2 - �レ옄 肄ㅻ쭏 �ㅼ젙
=================================================*/
function setComma(n){
	var reg = /(^[+-]?\d+)(\d{3})/;
	n += '';
	while (reg.test(n)){
		n = n.replace(reg, '$1' + ',' + '$2');
	}			
	return n;
}

/*=================================================
	6 - �뷀꽣 �대┃
=================================================*/
$(".enter_submit").on("keyup", function(event){
	var input	= $(this);
	var target	= $(this).data("target")?$($(this).data("target")):$(this).parents("form").find(".submit_btn");
	
	if(event.which==13){
		event.preventDefault();
		target.trigger("click");
	}
});

/*=================================================
	7 - �뷀꽣 �대┃ (��)
=================================================*/
$(".enter_form").find("input").on("keyup", function(event){
	var input	= $(this);
	var target	= $(this).data("target")?$($(this).data("target")):$(this).parents("form").find(".submit_btn");
	
	if(event.which==13){
		event.preventDefault();
		target.trigger("click");
	}
});

/*=================================================
	8 - �⑥씪 �명뭼
=================================================*/
$(".only_input").keypress(function(e){
	var input	= $(this);
	var target	= $(this).data("target")?$($(this).data("target")):$(this).parents("form").find(".submit_btn");
	
	if(e.which==13){
		e.preventDefault();
		target.trigger("click");
	}
});

/*=================================================
	9 - 鍮꾨�踰덊샇 蹂�寃�
=================================================*/
$(".readonly_pw").on("click", function(){
	var input	= $(this);
	
	if(input.prop("readonly")){
		if(confirm("鍮꾨�踰덊샇瑜� �섏젙�섏떆寃좎뒿�덇퉴?")){
			input.attr("readonly", false);
			input.focus();
		}
	}
});

/*=================================================
	10 - �ㅼ슫濡쒕뱶 留곹겕
=================================================*/
$(".download_link").on("click", function(){
	var name	= $(this).data("name");
	var path	= $(this).data("path");
	
	$("#download_form").find("input[name=name]").val(name);
	$("#download_form").find("input[name=path]").val(path);
	
	$("#download_form").submit();
});

/*=================================================
	11 - �щ씪�대뱶 �뚯씠釉�
=================================================*/
$(".slide-table").find(".subject").on("click", function(){
	var $subject	= $(this);
	
	if($subject.hasClass("lock")){
		var bc_idx	= $subject.data("idx");
		
		$("#pw_prompt").modal('show');
		
		$(".pw_submit_btn").on("click", function(){
			var pw		= $("#pw_prompt").find(".pw_input").val();
			var bc_pw	= sha512(pw);
			
			var snd	= {
				table: "ui_board_content",
				where: "`bc_idx`='"+bc_idx+"' AND `bc_pw`='"+bc_pw+"'",
				ajax: "true"
			}
			
			$.post("/_crud/select", $.param(snd), function(rcv){
				$("#pw_prompt").modal('hide');
				
				var obj	= $.parseJSON(rcv);
				
				if(obj.length < 1){
					alert("鍮꾨�踰덊샇媛� �щ컮瑜댁� �딆뒿�덈떎.");
					return false;
				} else {
					$(".subject").removeClass("active");
					$(".content").removeClass("active");
					
					$subject.addClass("active");
					$subject.next(".content").addClass("active");
				}
			});
		});
		
		return true;
	}
	
	if($subject.hasClass("active")){
		$(".subject").removeClass("active");
		$(".content").removeClass("active");
	} else {
		$(".subject").removeClass("active");
		$(".content").removeClass("active");
		
		$subject.addClass("active");
		$subject.next(".content").addClass("active");
	}
});

/*=================================================
	12 - �댄똻 �ㅼ젙
=================================================*/
$('[data-toggle="tooltip"]').tooltip()

/*=================================================
	14- �몃쾭 �덉씠��
=================================================*/
$(".hover-layer-wrap").hover(function(){
	$(this).find(".hover-sublayer").slideDown("slow");
}, function(){
	$(this).find(".hover-sublayer").slideUp("slow");
});

/*=================================================
	紐⑤떖李� �ㅼ젙
=================================================*/
$(".modal").on('hidden.bs.modal', function(e){
	if(!$(this).hasClass("empty-false")){
		$(this).find("input").val("");
	}
});

/*=================================================
	�몃쾭 �대�吏� �대┃
=================================================*/
$(".hover-wrap").on("click", function(){
	var $this	= $(this);
	
	if($this.hasClass("radio")){
		$this.siblings().removeClass("active");
		$this.addClass("active");
	} else {
		if($this.hasClass("not-off")){
			$this.addClass("active");
		} else {
			$this.toggleClass("active");
		}
	}
});

/*=================================================
	13 - Ajax Paging
=================================================*/
(function($){
	$.fn.ajaxPaiging = function(board_lists){
		var $board			= $(this);
		var $section		= $board.is("section")?$board:$board.parents("section");
		var bd_id			= $board.data("bd");
		var bcat			= $board.data("bcat");
		var $page_wrap		= $board.find(".pagination-wrap");
		var $lists_wrap		= $board.find(".lists-wrap");
		var $cate_btn		= $board.find(".bcat-btn");
		var list_callback	= arguments[1]||false;
		
		/////#####===== 由ъ뒪�� �ㅼ젙 =====#####/////
		var setAjaxLists	= function(rcv){
			var lists_output	= board_lists(rcv);
			
			$lists_wrap.html(lists_output);
			
			if(typeof(list_callback) == "function"){
				list_callback();
			}
			
			$('html, body').stop().animate({
				scrollTop: $section.offset().top
			}, 500, 'easeInOutExpo');
			
			$lists_wrap.find(".ajax-board-item").each(function(key, val){
				setTimeout(function(){
					$(val).animate({'opacity':1.0}, 450);
				}, key*200);
			});
		}
		
		/////#####===== 寃뚯떆臾� �뺣낫 =====#####/////
		var getAjaxLists	= function(page){
			var bd_id	= $board.data("bd");
			var bcat_id	= $board.data("bcat")?$board.data("bcat"):"";
			var page	= page?page:1;
			
			var snd = {
				bd_id: bd_id,
				bcat_id: bcat_id,
				page: page,
				is_ajax: true
			}
			
			$.post("/_board/get_lists", $.param(snd), function(rcv){
				var obj		= $.parseJSON(rcv);
				
				//##### �섏씠吏��ㅼ씠�� #####//
				$page_wrap.html(obj.pagination);
				
				setAjaxLists(obj);
			});
		}
		
		/////#####===== 移댄뀒怨좊━ �대┃ =====#####/////
		$cate_btn.on("click", function(e){
			e.preventDefault();
			
			$cate_btn.removeClass("active");
			$(this).addClass("active");
			
			$board.data("bcat", $(this).data("bcat"));
			
			getAjaxLists();
			
			return false;
		});
		
		/////#####===== �섏씠吏� �대┃ =====#####/////
		$page_wrap.on("click", ".page-num", function(e){
			e.preventDefault();
			
			var page		= $(this).text();
			
			getAjaxLists(page);
			
			return false;
		});
	}
})(jQuery);

/*=================================================
	13 - Full Screen Popup
=================================================*/
$(document).on("click", ".fsp-open-btn", function(){
	var popup	= $(this).data("popup");
	var idx		= $(this).data("idx");
	var black	= $(this).hasClass("black")?"black":"";
	var is_lock	= $(this).hasClass("lock")?true:false;
	
	
	if(is_lock){
		var result	= true;
		var pw		= prompt("鍮꾨�踰덊샇瑜� �낅젰�� 二쇱꽭��");
		
		if(pw==null || pw==""){
			alert("�щ컮瑜� 鍮꾨�踰덊샇瑜� �낅젰�� 二쇱꽭��");
			return false;
		} else {
			var snd	= {
				bc_idx: idx,
				bc_pw: sha512(pw)
			}
			
			$.ajax({
				type: "POST",
				async: false,
				url: "/_board/get_pw",
				data: snd,
				success: function(rcv){
					if(rcv!="ok"){
						alert("�щ컮瑜댁� �딆� 鍮꾨�踰덊샇 �낅땲��.");
						result	= false;
						return false;
					}
				}
			});
		}
		
		if(!result){
			return false;
		}
	}
	
	/////#####===== �쇰컲 �앹뾽 �ㅼ젙 =====#####/////
	if(popup=="" || popup==undefined || popup==null){
		var popup_output	= "";
		popup_output	+=	"<div id='tmp-popup' class='fsp-popup "+black+"'>";
		popup_output	+=		"<div class='fsp-header'>";
		popup_output	+=			"<div class='container'>";
		popup_output	+=				"<div class='fsp-header-wrap'>";
		popup_output	+=					"<h3 class='fsp-subject ellipsis'></h3>";
		popup_output	+=					"<i class='fa fa-times fa-border fsp-close-btn'></i>";
		popup_output	+=				"</div>";
		popup_output	+=			"</div>";
		popup_output	+=		"</div>";
		popup_output	+=		"<div class='container'>";
		popup_output	+=			"<div class='fsp-body-wrap'>";
		popup_output	+=				"<div class='fsp-content'></div>";
		popup_output	+=			"</div>";
		popup_output	+=		"</div>";
		popup_output	+=	"</div>";
		
		$("body").append(popup_output);
		
		popup	= "#tmp-popup";
	}
	/////#####===== 寃뚯떆�� �앹뾽 �ㅼ젙 =====#####/////
	else if(popup=="board"){
		var popup_output	= "";
		popup_output	+=	"<div id='board-popup' class='fsp-popup "+black+"'>";
		popup_output	+=		"<div class='fsp-header'>";
		popup_output	+=			"<div class='container'>";
		popup_output	+=				"<div class='fsp-header-wrap'>";
		popup_output	+=					"<h3 class='fsp-subject ellipsis'></h3>";
		popup_output	+=					"<i class='fa fa-times fa-border fsp-close-btn'></i>";
		popup_output	+=				"</div>";
		popup_output	+=			"</div>";
		popup_output	+=		"</div>";
		popup_output	+=		"<div class='container'>";
		popup_output	+=			"<div class='row'>";
		popup_output	+=				"<div class='col-lg-10 col-lg-offset-1 col-sm-12'>";
		popup_output	+=					"<div class='fsp-body-wrap'>";
		popup_output	+=						"<h2 class='bc-subject ellipsis'></h2>";
		popup_output	+=						"<div class='fsp-info'>";
		popup_output	+=							"<div class='pull-left info'>";
		popup_output	+=								"<span>�묒꽦�� : </span><span class='bc-writer-name'></span>";
		popup_output	+=							"</div>";
		popup_output	+=							"<div class='pull-right info'>";
		popup_output	+=								"<span>�깅줉�� : </span><span class='bc-regdate'></span>";
		popup_output	+=							"</div>";
		popup_output	+=							"<div class='clearfix'></div>";
		popup_output	+=						"</div>";
		popup_output	+=						"<div class='fsp-content'></div>";
		popup_output	+=						"<ul class='cmnt-list'>";
		popup_output	+=						"</ul>";
		popup_output	+=					"</div>";
		popup_output	+=				"</div>";
		popup_output	+=			"</div>";
		popup_output	+=		"</div>";
		popup_output	+=	"</div>";
		
		$("body").append(popup_output);
		
		popup	= "#board-popup";
	}
	
	/////#####===== 寃뚯떆臾� �뺣낫 �ㅼ젙 =====#####/////
	var snd	= {
		bc_idx: idx,
		is_ajax: "true"
	}
	
	$.post("/_board/get_view", $.param(snd), function(rcv){
		var obj	= $.parseJSON(rcv);
		
		//##### 寃뚯떆臾� �묒꽦 �뺣낫 �ㅼ젙 #####//
		$(popup).find(".fsp-subject").html(obj.view.bc_subject);
		$(popup).find(".bc-subject").html(obj.view.bc_subject);
		$(popup).find(".bc-writer-name").html(obj.view.bc_writer_name);
		$(popup).find(".bc-regdate").html(obj.view.bc_regdate);
		
		//##### 寃뚯떆臾� 而⑦뀗痢� �ㅼ젙 #####//
		var output="";
		
		//== �뚯씪 �ㅼ젙 ==//
		if(obj.file.length > 0){
			$.each(obj.file, function(key, val){
				// �대�吏� �뚯씪 //
				if(val.file_is_image){
					output	+= "<img class='img-responsive center-block' src='"+val.file_html_full_path+"'>";
				}
				// �쇰컲 �뚯씪 //
				else {
					
				}
			});
		}
		
		//== 而⑦뀗痢� �ㅼ젙 ==//
		if(/<[a-z][\s\S]*>/i.test(obj.view.bc_content)){
			output	+= obj.view.bc_content;
		} else{
			output	+= obj.view.bc_content.replace(/\n/g,"<br>");
		}
		
		$(popup).find(".fsp-content").html(output);
		
		//##### 寃뚯떆臾� �볤� �ㅼ젙 #####//
		var cmnt	= "";
		
		if(obj.cmnt.length > 0){
			$.each(obj.cmnt, function(key, val){
				cmnt	+=	"<li>";
				cmnt	+=		"<p class='cmnt-info'>";
				cmnt	+=			"<strong>"+val.cmnt_writer_name+"</strong>";
				cmnt	+=			"<small> ("+val.cmnt_regdate+")</small>";
				cmnt	+=		"</p>";
				cmnt	+= 		"<div class='cmnt-content'>"+val.cmnt_content.replace(/\n/g,"<br>");+"</div>";
				cmnt	+=	"</li>";
			});
		}
		
		$(popup).find(".cmnt-list").html(cmnt);
		
		//##### �앹뾽 �섑��닿린 #####//
		$(popup).show();
		
		$("body").css("overflow", "hidden");
	});
});

$(document).on("click", ".fsp-close-btn", function(){
	$(this).parents(".fsp-popup").hide();
	
	$("body").css("overflow", "visible");
	
	$("#tmp-popup").remove();
});

/*=================================================
	12 - Plugin �ㅼ젙
=================================================*/
//##### iCheck �ㅼ젙 #####//
if(jQuery().iCheck){
	$(".icheck-square-grey").iCheck({checkboxClass:'icheckbox_square-grey', radioClass:'iradio_square-grey'});
	$(".icheck-square-aero").iCheck({checkboxClass:'icheckbox_square-aero', radioClass:'iradio_square-aero'});
	$(".icheck-square-blue").iCheck({checkboxClass:'icheckbox_square-blue', radioClass:'iradio_square-blue'});
	$(".icheck-square-black").iCheck({checkboxClass:'icheckbox_square-black', radioClass:'iradio_square-black'});
	
	// �꾩껜 �좏깮 iCheck //
	$(".all-icheck").on("ifChecked", function(){
		$(".each-icheck").iCheck('check');
	});
	$(".all-icheck").on("ifUnchecked", function(){
		$(".each-icheck").iCheck('uncheck');
	});
}

//##### 遺��몄뒪�몃옪 �ㅼ쐞移� #####//
if(jQuery().bootstrapSwitch){
	$(".bootstrap-switch").bootstrapSwitch();
}

//##### Raty #####//
if(jQuery().raty){
	$(".raty").raty({
		score: function(){return $(this).data("score");},
		space: false,
		readOnly: function(){return $(this).data("readonly");},
		click: function(score, evt){
			var target	= $(this).data("target");
			$(target).val(score);
		}
	});
}

//##### �ъ떆諛뺤뒪 �ㅼ젙 #####//
if(jQuery().fancybox){
	$(".fancybox").fancybox({
		padding: 0,
		helpers: {
			overlay: {
				locked: false
			}
		}
	});
	$(".fancybox_media").fancybox({
		padding		: 0,
		openEffect  : 'none',
		closeEffect : 'none',
		helpers 	: {
			media : {}
		}
	});
	$(".fancybox_iframe").fancybox({
		padding		: 0,
		fitToView	: false,
		width		: '90%',
		height		: '90%',
		autoSize	: false,
		closeClick	: false
	});
}

//##### Select2 �ㅼ젙 #####//
if(jQuery().select2){
	//-- Select2 --//
	$(".select2").select2();
	
	//-- Select2 Tags --//
	var tags	= Array();
	if($(".select2-tags").data("tags")){
		tags = $(".select2-tags").data("tags").split(",");	
	}
	$(".select2-tags").select2({tags:tags});
	
	//-- select2 �대�吏� --//
	var img_tag	= $(".select2-tags").data("tag");
	
	$(".select2-img").val(img_tag).select2({
		formatResult: format,
		formatSelection: format,
		escapeMarkup: function(m) { return m; }
	});
	
	//-- select2 �대�吏� 硫��� --//
	var multi_tags	= Array();

	if($(".select2-img-multi").data("tags")){
		multi_tags = $(".select2-img-multi").data("tags").split(",");
	}

	$(".select2-img-multi").val(multi_tags).select2({
		formatResult: format,
		formatSelection: format,
		escapeMarkup: function(m) { return m; }
	});

	function format(item) {
		var option	= item.element;
		return "<img src='"+$(option).data('img')+"' width='50px'>" + item.text;
	}	
}

//##### word breake �ㅼ젙 #####//
if(jQuery().wordBreakKeepAll){
	$('.word-break').wordBreakKeepAll();
}

//##### draggable �ㅼ젙 #####//
if(jQuery().draggable){
	$(".draggable").draggable();
}

//##### �곗씠�명��� �쇱빱 �ㅼ젙 #####//
if(jQuery().datetimepicker){
	$(".datetimepicker").datetimepicker({
		monthNamesShort: ['1��','2��','3��','4��','5��','6��','7��','8��','9��','10��','11��','12��'],
		dayNamesMin: ['��','��','��','��','紐�','湲�','��'],
		weekHeader: 'Wk',
		dateFormat: 'yy-mm-dd',
		timeFormat: "HH:mm:ss",
		autoSize: false,
		changeMonth: true,
		changeYear: true,
		showMonthAfterYear: true,
		currentText: "�꾩옱",
		closeText: "�뺤씤",
		timeText: "�쒓컙",
		hourText: "��",
		minuteText: "遺�",
		secondText: "珥�"
	});
}

//##### �곗씠�� �쇱빱 �ㅼ젙 #####//
if(jQuery().datepicker){
	$(".datepicker").datepicker({
		monthNamesShort: ['1��','2��','3��','4��','5��','6��','7��','8��','9��','10��','11��','12��'],
		dayNamesMin: ['��','��','��','��','紐�','湲�','��'],
		weekHeader: 'Wk',
		dateFormat: 'yy-mm-dd',
		autoSize: false,
		changeMonth: true,
		changeYear: true,
		showMonthAfterYear: true
	});
}

/*=================================================
	12 - 遺��몄뒪�몃옪 洹몃━�� 遺덈윭�ㅺ린
=================================================*/
function getGrid() {
    var envs = ['xs', 'sm', 'md', 'lg'];

    $el = $('<div>');
    $el.appendTo($('body'));

    for (var i = envs.length - 1; i >= 0; i--) {
        var env = envs[i];

        $el.addClass('hidden-'+env);
        if ($el.is(':hidden')) {
            $el.remove();
            return env
        }
    };
}

function getViewport(){
	var e = window, a = 'inner';
    if (!('innerWidth' in window )) {
        a = 'client';
        e = document.documentElement || document.body;
    }
    return { width : e[ a+'Width' ] , height : e[ a+'Height' ] };
}

function showGrid(){
	var grid	= getGrid();
	var width	= getViewport().width;
	var height	= getViewport().height;
	
	$grid	= $("<div></div>");
	$grid.css({"position":"absolute","top":0,"left":0,"z-index":9999,"padding":"5px","background-color":"rgba(0,0,0,0.3)","color":"#FFFFFF"})
	$grid.html(grid+"("+width+"*"+height+")");
	$grid.prependTo("body");
	
	$(window).resize(function(){
		grid	= getGrid();
		width	= getViewport().width;
		height	= getViewport().height;
	
		$grid.html(grid+"("+width+"*"+height+")");
	});
}

/*=================================================
	12 - 紐⑤컮�� 泥댄겕
=================================================*/
var isMobile = {
	Android: function() {
		return navigator.userAgent.match(/Android/i);
	},
	BlackBerry: function() {
		return navigator.userAgent.match(/BlackBerry/i);
	},
	iOS: function() {
		return navigator.userAgent.match(/iPhone|iPad|iPod/i);
	},
	Opera: function() {
		return navigator.userAgent.match(/Opera Mini/i);
	},
	Windows: function() {
		return navigator.userAgent.match(/IEMobile/i);
	},
	any: function() {
		return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
	}
};

/*=================================================
	13 - �꾨줈�� ����
=================================================*/
//##### 諛곗뿴 鍮덇컪 �쒓굅 #####//
Array.prototype.removeEmpty	= function(){
	var arr	= this;
	
	arr = jQuery.grep(arr, function(n, i){
		return (n !== "" && n != null);
	});
	
	return arr;
}

//##### 湲��� �먮Ⅴ湲� #####//
String.prototype.kmCut = function(len) {
	var str = this;
	var l = 0;
	
	for (var i=0; i<str.length; i++) {
		l += (str.charCodeAt(i) > 128) ? 2 : 1;
		if(l > len){
			return str.substring(0,i);
		}
	}
	return str;
}

//##### 湲��� 諛붿씠�� 怨꾩궛 #####//
String.prototype.kmBytes = function() {
var str = this;
	var l = 0;
	for(var i=0; i<str.length; i++){
		l += (str.charCodeAt(i) > 128) ? 2 : 1;
	}
	return l;
}