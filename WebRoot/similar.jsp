<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.news.search.*"%>
<jsp:useBean id="SearchBean" class="com.news.search.SearchBean" scope="application"/>
 <%
/* String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; */
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
	<meta http-equiv="Access-Control-Allow-Origin" content="*"/>
	<title>果壳搜索</title>
	<link rel="stylesheet"  type="text/css" href="assets/css/amazeui.min.css"/>
    <link rel="stylesheet"  type="text/css" href="assets/css/admin.css"/>
    <link rel="stylesheet" type="text/css" href="css/search_results.css"/>
    <link rel="stylesheet"  type="text/css" href="css/public.css"/>
    <script type="text/javascript" src="jquery-1.9.1/jquery.js"></script>
    <script type="text/javascript" src="assets/js/amazeui.min.js"></script>
    <script type="text/javascript" src="js/public.js"></script>
    <script type="text/javascript" src="js/search.js"></script>

   
</head>
<body>
        
<div class="am-g am-container padding-none result_news">
    <div class="am-u-sm-12 am-u-md-12 am-u-lg-8">
        <div data-am-widget="list_news" class="am-list-news am-list-news-default ">
            <div class="am-list-news-bd">
            <br/>
                <ul class="am-list backcolor">
                <c:forEach items="${results}" var="result">
                    <li class="am-g am-list-item-desced am-list-item-thumbed am-list-item-thumb-left"  style="border-top:0px">
                    <div class="am-list-main">
                            <h3 class="am-list-item-hd"><a href="NewsResult?newsId=${result.id}">${result.title}</a></h3>

                            <div class="am-list-item-text" style="max-height: 5.6em;">
                              <p><span>${result.keyword}</span> &nbsp;&nbsp;&nbsp;&nbsp; <span><i class="am-icon-clock-o">2016/11/11</i></span></p>

                              <a href="NewsResult?newsId=${result.id}" class="news_list" style="color: #757575;" title="${result.title}">${result.snippet}</a>
                     
 
					<br/>
                      
					</div>
                  </div>
                    </li>
                    </c:forEach>
                </ul>

            </div>
        </div>
    </div>
   
</div>
</div>

</body>
</html>