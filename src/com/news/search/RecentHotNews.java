package com.news.search;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.news.db.JDBCUtil;

public class RecentHotNews {
	private static Connection conn = null;     
    private static Statement stmt = null;     
    private static ResultSet rs = null; 
    private static final int HOT_NEWS_NUM = 10;
	public List<SearchBean> getHotnews() throws Exception {
		List<SearchBean> result = new ArrayList<SearchBean>();
		
		conn = JDBCUtil.getConnection();     
        if(conn == null) {     
            throw new Exception("数据库连接失败！");     
        }     
        String sql = "SELECT * FROM news WHERE release_time > '2017-12-20' AND release_time < '2017-12-24' ORDER BY join_num DESC LIMIT " + HOT_NEWS_NUM + ";";     
        try {     
            stmt = conn.createStatement();     
            rs = stmt.executeQuery(sql);     
            while(rs.next()) {
            	SearchBean searchBean = new SearchBean();
            	searchBean.setTitle(rs.getString("title"));
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
	
//	public static void main(String[] args) {
//		RecentHotNews recentHotNews = new RecentHotNews();
//		try {
//			List<SearchBean> resultBeans = recentHotNews.getHotnews();
//			for (SearchBean searchBean : resultBeans) {
//				System.out.println(searchBean.getTitle());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
