package com.news.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.news.search.SearchBean;
import com.news.search.SimilarNews;

public class SimiNews extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 处理get请求
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet方法执行");
		// 设置响应内容类型
        response.setContentType("text/html;charset=UTF-8");
		try {
            int docId = Integer.parseInt(request.getParameter("docId"));
            
            System.out.println(docId);
            
            SimilarNews similarNews = new SimilarNews();
            List<SearchBean> result = similarNews.getSimilarNews(docId);
			HttpSession session = request.getSession();
			session.setAttribute("results",result);
			String url="/UcasIR/similar.jsp";
			response.sendRedirect(url);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 处理POST请求
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}

