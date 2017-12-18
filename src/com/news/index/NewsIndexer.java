package com.news.index;

import com.news.db.*;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class NewsIndexer {
	
	public static final String searchDir = "G:\\MySQLData\\Index";
	private static Analyzer analyzer = null; 
	private static Connection conn = null;     
    private static Statement stmt = null;     
    private static ResultSet rs = null; 
    private static File indexFile = null;     

	
	/**   
	    * 获取数据库数据   
	    * @return ResultSet   
	    * @throws Exception   
	    */    
	    private void readDatabase() throws Exception {     
	        conn = JDBCUtil.getConnection();     
	        if(conn == null) {     
	            throw new Exception("数据库连接失败！");     
	        }     
	        String sql = "SELECT title, content, keyword FROM news";     
	        try {     
	            stmt = conn.createStatement();     
	            rs = stmt.executeQuery(sql);     
	            this.createIndex(rs);   //给数据库创建索引,此处执行一次，不要每次运行都创建索引，以后数据有更新可以后台调用更新索引     
	                
	        } catch(Exception e) {     
	            e.printStackTrace();     
	            throw new Exception("数据库查询sql出错！ sql : " + sql);     
	        } finally {     
	            if(rs != null) rs.close();     
	            if(stmt != null) stmt.close();     
	            if(conn != null) conn.close();     
	        }              
	    }     
	
	
	/**
     * 为数据库创建索引
     * @param rs
     */
    private void createIndex(ResultSet rs) throws Exception {
    	Directory directory = null;
    	IndexWriter indexWriter = null;
    	analyzer = new IKAnalyzer();
    	IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_43, analyzer);
    	//索引的打开方式，没有索引文件就新建，有就打开  
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND); 
    	
    	try {
    		
			indexFile = new File(searchDir);
			directory = FSDirectory.open(indexFile);
			
			//如果索引处于锁定状态，则解锁  
            if (IndexWriter.isLocked(directory)){  
                IndexWriter.unlock(directory);  
            }
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			
			Document document = null;
			while(rs.next()) {
				document = new Document();
				TextField title = new TextField("title", rs.getString("title"), Field.Store.YES);
				TextField content = new TextField("content", rs.getString("content"), Field.Store.YES);
				TextField keyword = new TextField("keyword", rs.getString("keyword"), Field.Store.YES);
				document.add(title);
				document.add(content);
				document.add(keyword);
				indexWriter.addDocument(document);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	//将indexWrite操作提交，如果不提交，之前的操作将不会保存到硬盘  
        try {  
            //这一步很消耗系统资源，所以commit操作需要有一定的策略  
            indexWriter.commit();  
            //关闭资源  
            indexWriter.close();  
            directory.close();  
            System.out.println("索引创建完成");
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    
    public static void main(String[] args) {
    	NewsIndexer newsIndexer = new NewsIndexer();
    	try {
    		newsIndexer.readDatabase();
    	}catch (Exception e) {
			e.getSuppressed();
		}
    }
}
