$(function(){
	$("#suggest").css({
			top:$("#searchmain_id").offset().top+$("#searchmain_id").height()-8,
			left:$("#searchmain_id").offset().left-8
		});
    

	$("#searchtext").bind("blur",function(){
		$("#suggest").hide();
	});

})