package com.sasmac.usermanager.service;



import java.sql.Connection;
import java.sql.SQLException;

import com.sasmac.dbconnpool.ConnPoolUtil;
import com.sasmac.usermanager.common.ConnectionMyDB;
import com.sasmac.usermanager.dao.PrivDao;
import com.sasmac.usermanager.dao.RoleDao;
import com.sasmac.usermanager.dao.UserDao;

public class ManagementService {

	private PrivDao ServprivDao;
	private RoleDao ServroleDao;
	private UserDao ServuserDao;
	/** SUNJ 2017.2.11修改
	 * 修改成员 private ConnectionMyDB cndb;
	 * 使用jdbc的Connection替换这个ConnectionMyDB类
	 */
	private Connection conn;
	
	public ManagementService(){
		
		//this.cndb=new ConnectionMyDB("jdbc:mysql://localhost:3306/test","root","111111");
		//this.cndb=new ConnectionMyDB("jdbc:mysql://192.168.1.177:3306/USERMANAGER","root","123456");
		this.conn= ConnPoolUtil.getConnection();		
		this.ServprivDao=new PrivDao(conn);
		this.ServroleDao=new RoleDao(conn);
		this.ServuserDao=new UserDao(conn);
/*		System.out.println(this.conn);
		System.out.println(this.ServprivDao);
		System.out.println(this.ServroleDao);
		System.out.println(this.ServuserDao);*/
	}
	
	public Connection getCNDB(){
		return conn;
	}
	
	public PrivDao getPrivDao(){
		return ServprivDao;
	}
	public RoleDao getRoleDao(){
		return ServroleDao;
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
		this.ServprivDao=null;
		this.ServroleDao=null;
		this.ServuserDao=null;
	}
}
