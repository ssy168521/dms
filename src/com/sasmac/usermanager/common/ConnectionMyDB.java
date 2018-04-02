package com.sasmac.usermanager.common;
//sdd548,数据库连接器 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectionMyDB {

	private Connection conn=null;//链接成员
	//初始化，链接上数据库，保持链接 
	public ConnectionMyDB(String  url,String user,String password){
		try {
			//1、注册MySQL驱动程序
			System.out.println("数据库连接中 ！ ");	
			Class.forName("com.mysql.jdbc.Driver");			
			//2、连接数据库，通过driver manager连接	
			// url="jdbc:mysql://localhost:3306/test";
			// user="root";
			// password ="111111";			
			this.conn=DriverManager.getConnection(url, user, password);
			System.out.println("数据库连接成功！ "+conn);					
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("加载驱动失败！");
			e.printStackTrace();//打印错误信息
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("数据库连接失败！");
			e.printStackTrace();
		}
	}
	//获取链接 
	public Connection getConnection(){
		return conn;
	}
	//关闭链接 
	public void closeCONN(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//查
    public ResultSet querySQL(String sql) {
        ResultSet result = null;
        try
        {
        	Statement state = conn.createStatement();
        	result = state.executeQuery(sql);
        	state.close();
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    //增删改
    public int executeSQL(String sql) {
        	try
        {
        	Statement state = conn.createStatement();
        	state.executeUpdate(sql);
        	state.close();
        	return 1; 
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0; 
        }
    }

}
