package com.web.thread;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class Dbconnect {
	    private String driver;//数据库驱动
	    private String url;
	    private String user;
	    private String pass;
	    public Connection conn;
	    public Statement stmt;
	  /**
	   * 加载mysql驱动  
	   */
    public void initParam(String paramFile) throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream(paramFile));
        driver = props.getProperty("driver");
        url = props.getProperty("url");
        user = props.getProperty("user");
        pass = props.getProperty("pass");       
    }
     /**
      * 将数据插入mysql
      */
    public void Insert(String sql) throws Exception{
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,pass);
            stmt = conn.createStatement();// 实例化Statement对象
            stmt.executeUpdate(sql);
    	  } finally	{
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
    /**
     * 连接
     * @return 
     * @return 
     */
    public Object getsql(String sql,String params)throws Exception{
	   Class.forName(driver);
       conn = DriverManager.getConnection(url,user,pass);

	   QueryRunner QR = new QueryRunner();
	   Object strExtent = QR.query(conn, sql, new ScalarHandler<String>(),
			params);  
	   System.out.println(strExtent);
    	return strExtent;
    }
    
 }

