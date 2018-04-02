package com.sasmac.dbconnpool;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

import com.sasmac.system.ReadConf;
import com.web.common.Constants;
import com.web.util.DbUtils;
import com.web.util.PropertiesUtil;

/***
 *@ClassName ConnPool
 *@Description 数据库模拟连接池
 *@author SUNJ
 *@date 2017年2月10日下午7:25:48
 */
import com.web.common.Constants;
public class ConnPool {
	
	/**
     * 用一个链表进行数据库连接的容器，
     * 因为在操作的时候我们使用数据库的连接和它的释放是一个频繁的插入和删除的操作，
     * 所以在这里最好的最大就是使用链表进行操作 
     */
    private LinkedList<Connection> connectionsPool = new LinkedList<Connection>();

 //   private static String url = "jdbc:mysql://192.168.1.177:3306/USERMANAGER";
    //private static String url =ReadConf.getJDBCConnUrl("WebRoot/conf/sysDbConnConf.properties");
  //  private static String user = "root";
     
 //   private static String password = "123456";

    //设置连接池的数目
    private Integer connectionsNum = 5;
     
    //数据库连接池中的最大负荷
    private Integer connectionMax = 20;
     
    //当前的已经创建连接数
    private Integer currentCount = 0;
    /**
     * 类在构造的时候按照给定的连接池的大小进行数据源的创建操作，
     * 连接创建以后，把创建的对象放置到list中去
     */
	public ConnPool() {
		try {
			if(connectionsNum > connectionMax){
	            throw new ExceptionInInitializerError("数据库连接池初始化参数异常！！！");
	        }
	        for(int i=0; i<connectionsNum; i++)
	        {
	            Connection conn;			
	            conn = createConnection();
	            if(conn!=null){
	                this.currentCount++;
	                connectionsPool.addLast(createConnection());
	            }else {
	                throw new ExceptionInInitializerError("数据库连接池初始化时候出现异常！！！");
	            }
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
     * 创建一个connection对象
     * @return 和数据库进行连接的connection对象，异常的时候是一个null
	 * @throws SQLException 
     */
    public static Connection createConnection() throws SQLException{
  
        try {
        	    Connection conn = null;
				String path = ConnPool.class.getClassLoader().getResource("/").toURI().getPath();		
				PropertiesUtil propertiesUtil = new PropertiesUtil(path+Constants.STR_DATABASE_CONF);
				if(propertiesUtil == null) return null;
				String host=propertiesUtil.getProperty("host");
	        	String database=propertiesUtil.getProperty("database");
	        	String port=propertiesUtil.getProperty("port");
	        	String url="jdbc:mysql://"+host+":"+port+"/"+database+"?characterEncoding=utf8";
	        	String user=propertiesUtil.getProperty("user");
	        	String password=propertiesUtil.getProperty("password");
				conn = DriverManager.getConnection(url, user,password);
				return conn;
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        	return null;
      

      	 /*  	
            conn = DriverManager.getConnection(url, user, password);*/
        	//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "123456");
        	
     
       
    }
     
    /**
     * 从池中获取对象的操作
     * @return 链表的第一个元素
     */
    public Connection getConnection() throws SQLException{
        //加锁的处理，保证不同的线程在去连接的时候拿到的连接是不同的
        synchronized (connectionsPool) {
            //表示的是连接池中的资源还够使用
            if(this.connectionsPool.size()>0){
                return connectionsPool.removeFirst();
            }
            //资源不够用，但是创建的对象数目小于我们设定的最大的时候，可以进行进一步的创建的操作
            if(currentCount < connectionMax){
                Connection conn = createConnection();
                if(conn!=null){
                    currentCount++;
                }
                return conn;
            }
            throw new SQLException("没有空闲的连接对象了");
        }
    }
     
    /**
     * 释放时候的操作就是把连接的对象放置在链表的末尾的操作
     * @param conn 使用完毕的connection对象
     */
    public void free(Connection conn){
        connectionsPool.addLast(conn);
    }
	
}
