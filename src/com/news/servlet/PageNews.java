package com.news.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.news.search.PageNewsBean;

public class PageNews extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PageNews() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
			PageNewsBean pagenewsbean = (PageNewsBean) request.getAttribute("pagenewsbean");
			PageNewsBean pagenewsbean2 = pagenewsbean.getResult((String)request.getParameter("jumpPage"));
            //把PageBean保存到request对象中
            request.setAttribute("pagenewsbean",pagenewsbean2);
		}
		catch(Exception e)
		{
			e.printStackTrace();
     	}

          /**
       *把视图派发到viewForum.jsp
       */
		javax.servlet.RequestDispatcher dis=request.getRequestDispatcher("/business/business_pre_index.jsp");
		dis.forward(request,response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request,response);
		
	}
}
