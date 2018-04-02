package com.sasmac.usermanager.service;



import java.sql.Connection;
import java.sql.SQLException;

import com.sasmac.dbconnpool.ConnPoolUtil;
import com.sasmac.usermanager.common.ConnectionMyDB;
import com.sasmac.usermanager.dao.UserDao;

public class UserService {

	private UserDao ServuserDao;
	/** SUNJ 2017.2.11修改
	 * 修改成员 private ConnectionMyDB cndb;
	 * 使用jdbc的Connection替换这个ConnectionMyDB类
	 */
	private Connection conn;
	
	public UserService(){
		this.conn = ConnPoolUtil.getConnection();
		this.ServuserDao=new UserDao(conn);
	}
	
	public Connection getCONN(){
		return conn;
	}
	
	public UserDao getUserDao(){
		return ServuserDao;
	}
	
	public void closeService(){
		/** SUNJ 2017.2.11修改
		 * 修改成员cndb.closeCONN();
		 * 使用ConnPoolUtil.close()替换这个ConnectionMyDB类的closeCONN();
		 */		
		ConnPoolUtil.close(this.conn, null, null);
		
	}
}
