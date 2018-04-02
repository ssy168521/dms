package com.sasmac.meta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sasmac.dbconnpool.ConnPoolUtil;
/**
 * 得到字段所对应的节点路径
 * @author Administrator
 * @ClassName:NodePathParser
 * @Version 版本
 * @ModifiedBy 修改人
 * @date 2018年3月20日 下午4:39:23
 */
public class NodePathParser {
	private Logger myLogger = LogManager.getLogger("mylog");

	/**
	 * 由产品、产品表、字段得到字段所对应的节点路径，节点路径用于读取与字段对应的节点内容，并插入数据库
	 * @param productName 产品名
	 * @param productTable 产品信息表
	 * @param fieldname  字段
	 * @return
	 */
	public String getNodePath(String productName,String productTable,String fieldName){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String nodepath ="";
		String sql="select nodepath from archiveconfig where fieldName=\""+fieldName+"\" and "
				+ "productName=\""+productName+"\" and productTable=\""+productTable+"\";";	
		try {
			conn = ConnPoolUtil.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
		   if(rs.next()){		 
			   nodepath = rs.getString("nodepath");
		   }else{
			   nodepath=null;
			   myLogger.info("未查询到相关节点路径，不存在字段节点对应关系！");
		   }
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			ConnPoolUtil.close(conn, null, null);
		} 
		return nodepath;
		
	}
}
