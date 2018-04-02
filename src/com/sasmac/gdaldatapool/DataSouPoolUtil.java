package com.sasmac.gdaldatapool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.gdal.ogr.DataSource;
import com.sasmac.gdaldatapool.DataSouPool;

public class DataSouPoolUtil {
	/**
     * 需要一个数据库的连接池的属性 
     */
    private static DataSouPool pDataSouPool = null;
     
    static{
        try {
            Class.forName("org.gdal.ogr.DataSource");
            pDataSouPool = new DataSouPool();
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();
        }
    }
     
    /**
     * 获取数据库连接
     * @return 返回一个数据源中的对象
     */
    public static DataSource getDataSource(){
    	DataSource pDataSource = null;
        try {
        	pDataSource = pDataSouPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pDataSource;
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
    public static void close(DataSource pDataSource){
        if(pDataSource!=null){
            try {
                pDataSouPool.free(pDataSource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	public DataSouPoolUtil() {
		// TODO Auto-generated constructor stub
	}

}
