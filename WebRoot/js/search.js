$(function(){
	$("#suggest").css({
			top:$("#searchmain_id").offset().top+$("#searchmain_id").height()-8,
			left:$("#searchmain_id").offset().left-8
		});
    
    /*$("#searchtext").bind("keyup",function(){
        	var jqueryInput = $(this);
	        var searchText = jqueryInput.val();
	       $.ajax({
	       	      url:"http://api.bing.com/qsonhs.aspx?q="+searchText,
	       	      type:"GET",
	       	      dataType:"JSON",
	       	      success:function(data){
                       var d=data.AS.Results[0].Suggests;
                       var html="";
                       for(var i=0;i<d.length;i++)
                    	 html+="<li>"+d[i].Txt+"</li>";
                        $("#ulli").html(html);
                        $("#suggest").show();
	               } 
           });
    }); 
*/

	$("#searchtext").bind("blur",function(){
		$("#suggest").hide();
	});

})