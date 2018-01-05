<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
	<meta http-equiv="Access-Control-Allow-Origin" content="*"/>
	<title>果壳搜索</title>
	<link rel="stylesheet" href="assets/css/amazeui.min.css"/>
  	<link rel="stylesheet" href="assets/css/admin.css"/>
  	<link rel="stylesheet" type="text/css" href="css/search.css"/>
	<script type="text/javascript" src="jquery-1.9.1/jquery.js"></script>
	<script type="text/javascript" src="js/search.js"></script>
	<script type="text/javascript"> 
		function test(){ 
		/* alert("hello"); */
		window.location.href="RecentNews";
		} 
	</script> 

  	
</head>
<body onLoad="test()">
	<div id="bd_img">
		<div class="searchbox">
			
		  <form class="searchmain" id="searchmain_id" target="_blank" action="SearchResult">
			  <input type="text" id="searchtext" name="searchtext" />
			  <input type="submit" id="searchbutton" value="" />
		</form> 
		</div> 
        <div id="suggest" style="display:none">
        	<ul id="ulli">
        	</ul>
        </div>

        <!--热门新闻-->

        <div class="am-g hot_news">
        <div class="am-u-sm-12">
            <table class="am-table table-main" id="table_news">
              <thead>
              <tr>
                <th class="table-id">热门推荐</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach items="${resultBeans }" var="resultBeans">
              <tr>
                
                <td><a href="#">${resultBeans.title }</a></td>
                
              </tr>
              </c:forEach>
             
              <!--  <tr>
                
                <td><a href="#">韩国总统在北京喝豆浆吃油条 店家：咱店小本想推辞</a></td>
                
              </tr> 
              <tr>
                
                <td><a href="#">张一山一人扛起七个角色，《柒个我》上演“戏精”的诞生！</a></td>
                
              </tr>
               <tr>
                
                <td><a href="#">死缓犯梦到儿时被拐 狱中寻亲成功</a></td>
                
              </tr> 
              <tr>
                
                <td><a href="#">刘德华获颁文学博士学位 发表感言只说了7个字</a></td>
                
              </tr> -->
             

              </tbody>
            </table>
        </div>

      </div>
	</div>

</body>
</html>