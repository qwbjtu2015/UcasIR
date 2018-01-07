package com.news.search;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.news.db.JDBCUtil;

public class RecentHotNews {
	private static Connection conn = null;     
    private static Statement stmt = null;     
    private static ResultSet rs = null; 
    private static final int HOT_NEWS_NUM = 6;
    private static final int PRE_DAY_NUM = 3;
	public List<SearchBean> getHotnews() throws Exception {
		List<SearchBean> result = new ArrayList<SearchBean>();
		
		conn = JDBCUtil.getConnection();     
        if(conn == null) {     
            throw new Exception("数据库连接失败！");     
        }     
        
        Calendar calendar = Calendar.getInstance();//获取系统当前时间
        calendar.add(Calendar.DATE, -PRE_DAY_NUM);    //得到距当前三天的日期
        String  preDay = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String sql = "SELECT title,news_id FROM news WHERE release_time > '"+ preDay +"' ORDER BY join_num DESC LIMIT " + HOT_NEWS_NUM + ";";  
        try {     
            stmt = conn.createStatement();     
            rs = stmt.executeQuery(sql);     
            while(rs.next()) {
            	SearchBean searchBean = new SearchBean();
            	searchBean.setTitle(rs.getString("title"));
            	searchBean.setId(rs.getString("news_id"));
            	result.add(searchBean);
            }
                
        } catch(Exception e) {     
            e.printStackTrace();     
            throw new Exception("数据库查询sql出错！ sql : " + sql);     
        } finally {     
            if(rs != null) rs.close();     
            if(stmt != null) stmt.close();     
            if(conn != null) conn.close();     
        }              

		return result;
	} 
	
	public static void main(String[] args) {
		RecentHotNews recentHotNews = new RecentHotNews();
		try {
			List<SearchBean> resultBeans = recentHotNews.getHotnews();
			for (SearchBean searchBean : resultBeans) {
				System.out.println(searchBean.getTitle());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
