package com.sasmac.dbconnpool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sasmac.dbconnpool.ConnPool;

/***
 *@ClassName DBUtil
 *@Description 数据库的连接的工具类
 *@author SUNJ
 *@date 2017年2月10日下午7:31:40
 */

public class ConnPoolUtil {

	/**
     * 需要一个数据库的连接池的属性 
     */
    private static ConnPool pConnPool = null;
     
    static{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            pConnPool = new ConnPool();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
     
    /**
     * 获取数据库连接
     * @return 返回一个数据源中的对象
     */
    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = pConnPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
     
    /**
     * @param conn 已经和数据库进行连接的connection对象
     * @param sql 需要进行执行的sql语句
     * @return 预处理的对象preparedStatement
     */
    public static PreparedStatement getPreparedStatement(Connection conn,String sql){
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }
     
    /**
     * 数据库连接箱资源释放的操作
     * @param conn 和数据库进行连接的对象
     * @param ps 预处理的preparedStatement对象
     * @param rs sql执行的结果集
     */
    public static void close(Connection conn, Statement ps, ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(ps!=null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                pConnPool.free(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
