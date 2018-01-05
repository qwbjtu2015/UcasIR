package com.news.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.news.search.CommentBean;
import com.news.search.ContentSearcher;
import com.news.search.NewsSearcher;
import com.news.search.RecentHotNews;
import com.news.search.SearchBean;

public class RecentNews extends HttpServlet {
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
        //PrintWriter out = response.getWriter();
		RecentHotNews recentHotNews = new RecentHotNews();
		try {
			List<SearchBean> resultBeans = recentHotNews.getHotnews();
			HttpSession session = request.getSession();
			
			session.setAttribute("resultBeans",resultBeans);
			
			String url = "/UcasIR/search.jsp";
			response.sendRedirect(url);
		} catch (Exception e) {
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
