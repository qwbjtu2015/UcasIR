package com.news.search;

import com.news.db.JDBCUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;     
import java.util.List;     



/**     
* NewsSearcher.java   
* @version 1.0   
* @createTime Lucene 新闻内容检索   
*/

public class ContentSearcher {     
	
	private static Connection conn = null;     
    private static Statement stmt = null;     
    private static ResultSet rs = null; 
     
    
    
    /**
     * 返回搜索结果列表
     * @param queryStr
     * @return
     * @throws Exception
     */
    public SearchBean getContent(String queryStr) throws Exception{     
    	conn = JDBCUtil.getConnection();     
        if(conn == null) {     
            throw new Exception("数据库连接失败！");     
        }  
        SearchBean news = new SearchBean();
        String sql="SELECT news_id,title,category,content,release_time,keyword,news_url,source,join_num FROM news WHERE news_id= '"+ queryStr+"';";
        try {     
            stmt = conn.createStatement();     
            rs = stmt.executeQuery(sql);
            if(rs.next()){
            	news.setId(rs.getString("news_id"));
            	news.setTitle(rs.getString("title"));
            	news.setContent(rs.getString("content"));
            	news.setKeyword(rs.getString("keyword"));

            	news.setUrl(rs.getString("news_url"));
            	news.setReleaseTime(rs.getString("release_time"));
            	news.setCategory(rs.getString("category"));
            	news.setSource(rs.getString("source"));
            	news.setJoinNum(rs.getInt("join_num"));
            	

            }
                
        } catch(Exception e) {     
            e.printStackTrace();     
            throw new Exception("数据库查询sql出错！ sql : " + sql);     
        } finally {     
            if(rs != null) rs.close();     
            if(stmt != null) stmt.close();     
            if(conn != null) conn.close();     
        }        
        
        return news;
    }    
    
    /**
     * 根据新闻ID获取评论
     * */
    public List<CommentBean> getComments(String newsId) throws Exception{     
        //List<SearchBean> result = null;  
    	conn = JDBCUtil.getConnection();     
        if(conn == null) {     
            throw new Exception("数据库连接失败！");     
        }  
        List<CommentBean> comments=new ArrayList<>();
        String sql="SELECT news_id,comment_id,content,create_time,user_id,user_nickname FROM comments WHERE news_id= '"+ newsId+"';";
        try {     
            stmt = conn.createStatement();     
            rs = stmt.executeQuery(sql);
            while(rs.next()){
            	CommentBean commentBean = new CommentBean();
            	commentBean.setId(rs.getString("comment_id"));
            	commentBean.setNewsId(rs.getString("news_id"));
            	commentBean.setComment(rs.getString("content"));
            	commentBean.setCreateTime(rs.getString("create_time"));
            	commentBean.setUserId(rs.getString("user_id"));
            	commentBean.setUserName(rs.getString("user_nickname"));
            	
//            	褒贬测试
            	commentBean.setPos_or_neg((int)System.currentTimeMillis()%2);
//            	System.out.println(commentBean.getPos_or_neg());

            	
            	comments.add(commentBean);
            }
                
        } catch(Exception e) {     
            e.printStackTrace();     
            throw new Exception("数据库查询sql出错！ sql : " + sql);     
        } finally {     
            if(rs != null) rs.close();     
            if(stmt != null) stmt.close();     
            if(conn != null) conn.close();     
        }        
        
        return comments;
    }     

}
  
