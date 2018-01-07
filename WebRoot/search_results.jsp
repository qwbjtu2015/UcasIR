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
	<% 	int pageIndex;
	   	if(session.getAttribute("pageIndex") != null)
			pageIndex = Integer.valueOf((String)session.getAttribute("pageIndex"));
		else
			pageIndex = 1;
	%>
	<div id="main">
	<% int model = Integer.parseInt((String)session.getAttribute("model")); %>
		<div class="searchbox_2">
		    <form class="searchmain" id="searchmain_id" target="_self"  method="post" action="SearchResult">
			 <div class="am-g am-g-fixed">
			  <input type="text" id="searchtext" class="searchtext" name="searchtext" value="${query}"/>
			  <input type="submit" id="searchbutton" class="searchbutton" value="" />
            </div>
            <div class="am-container search_radio">
              <!-- <label><input name="model" type="radio" value="0" />相关度</label> &nbsp;&nbsp; -->
              <label><input name="model" type="radio" value="0" <%= model==0?"Checked":"" %> />相关度</label> &nbsp;&nbsp;
              <label><input name="model" type="radio" value="1" <%= model==1?"Checked":"" %> />时间</label> &nbsp;&nbsp;
              <label><input name="model" type="radio" value="2" <%= model==2?"Checked":"" %> />热度</label> 
          </div>
			</form> 
		</div> 
    <!--自动补齐和历史查询结果显示 jsz中实现-->
        <div id="suggest" style="display:none">
        	<ul id="ulli">
        	</ul>
        </div>

        <!--查询结果-->
        
<div class="am-g am-container padding-none result_news">
    <div class="am-u-sm-12 am-u-md-12 am-u-lg-8">
        <div data-am-widget="list_news" class="am-list-news am-list-news-default ">
            <div class="am-list-news-bd">
            <p>用时 <font size="2" color="red">${costSecond}</font> 秒</p>
            <br/>
                <ul class="am-list backcolor">
                <c:forEach items="${results}" var="result">
                    <li class="am-g am-list-item-desced am-list-item-thumbed am-list-item-thumb-left"  style="border-top:0px">
                    <div class="am-list-main">
                            <h3 class="am-list-item-hd"><a href="NewsResult?newsId=${result.id}">${result.title}</a></h3>

                            <div class="am-list-item-text" style="max-height: 5.6em;">
                              <p><span>${result.category}</span> &nbsp;&nbsp;&nbsp;&nbsp; <span><i class="am-icon-clock-o">${result.releaseTime}</i></span></p>

                              <a href="NewsResult?newsId=${result.id}" class="news_list" style="color: #757575;" title="${result.content}">${result.snippet}</a>
                     
 
					<br/>
                      
					</div>
					 <div class="am-list-item-text">
					   <p><a style="float:right" href="SimiNews?docId=${result.docId}"> >>&nbsp; 查看相似新闻</a></p>
					   </div>
                  </div>
                    </li>
                    </c:forEach>
                </ul>

                <ul data-am-widget="pagination" class="am-pagination am-pagination-default backcolor center">
					
                    <li class="am-pagination-first">
                        <a href="SearchResult?searchtext=${query}&model=${model}&pageIndex=<%=String.valueOf(1) %>" class="am-hide-sm">第一页</a>
                    </li>

                    <li class="am-pagination-prev ">
                        <a href="SearchResult?searchtext=${query}&model=${model}&pageIndex=<%=String.valueOf(pageIndex-1) %>" class="">上一页</a>
                    </li>
					<li class="am-pagination-last">
                        <a>当前第<%= pageIndex %>页</a>
                    </li>
                    <li class="am-pagination-next ">
                        <a href="SearchResult?searchtext=${query}&model=${model}&pageIndex=<%=String.valueOf(pageIndex+1) %>" class="">下一页</a>
                    </li>
                    <li class="am-pagination-last ">
                        <a href="SearchResult?searchtext=${query}&model=${model}&pageIndex=${totalPage}" class="am-hide-sm">最末页</a>
                    </li>
                    <li class="am-pagination-last">
                        <a>共${totalPage}页</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!--热门标签 不要可以删除-->
    <div class="am-u-sm-0 am-u-md-0 am-u-lg-4 am-hide-sm">
        <div class="tag bgtag">
            <div data-am-widget="titlebar" class="am-titlebar am-titlebar-default" >
                <h2 class="am-titlebar-title ">
                    相关搜索词
                </h2>
            </div>
            <ul>
            <c:forEach items="${relateWords}" var="relateWord">
                <li><a href="SearchResult?searchtext=${relateWord}&model=${model}&pageIndex=1">${relateWord}</a></li>
                </c:forEach>
            </ul>
            <div class="am-cf"></div>
        </div>
    </div>
    <!--热门标签 不要可以删除-->
</div>
</div>

</body>
</html>