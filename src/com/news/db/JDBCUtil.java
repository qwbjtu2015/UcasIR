package com.news.db;

import java.sql.Connection;     
import java.sql.DriverManager;       
import java.sql.SQLException;


public class JDBCUtil {
	private static Connection conn = null;
	private static final String URL = "jdbc:mysql:"
			+ "//127.0.0.1/news?autoReconnect=true&characterEncoding=utf8";
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "qin123456";
	
	public static Connection getConnection() {
		try {
			Class.forName(JDBC_DRIVER);
			System.out.println("数据库驱动加载成功！");
			conn = DriverManager.getConnection(URL,USER_NAME,PASSWORD);
			System.out.println("数据库连接成功！");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 数据库连接测试
	 */
//	public static void main(String[] args) {
//		Connection connection = JDBCUtil.getConnection();//调用连接数据库的方法
//		try {
//			connection.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//	}
}
