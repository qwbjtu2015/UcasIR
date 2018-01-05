<%@page import="com.news.search.SearchBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<meta http-equiv="Access-Control-Allow-Origin" content="*">
	<link rel="stylesheet"  type="text/css" href="assets/css/amazeui.min.css"/>
  	<link rel="stylesheet"  type="text/css" href="assets/css/admin.css">
	<link rel="stylesheet" type="text/css" href="css/search_results.css">
	<link rel="stylesheet"  type="text/css" href="css/public.css">
	<script type="text/javascript" src="jquery-1.9.1/jquery.js"></script>
	<script type="text/javascript" src="assets/js/amazeui.min.js"></script>
	<script type="text/javascript" src="js/public.js"></script>
	<script type="text/javascript" src="js/search.js"></script>
</head>
<body>
	<%
		ArrayList<SearchBean> list = (ArrayList<SearchBean>) request.getAttribute("SearchResult");
		for(SearchBean bean : list) {
			out.println("bean.docId: " + bean.getDocId());
			out.println("bean.title: " + bean.getTitle());
			out.println("bean.keyword: " + bean.getKeyword());
			out.println("bean.content: " + bean.getContent());
		}
	 %>
</body>
</html>