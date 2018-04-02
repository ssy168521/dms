package com.sasmac.gdaldatapool;

import java.sql.SQLException;
import java.util.LinkedList;

import org.gdal.ogr.DataSource;
import org.gdal.ogr.ogr;

import com.web.util.PropertiesUtil;
import com.web.common.Constants;
public class DataSouPool {

	/**
     * 用一个链表进行数据库连接的容器，
     * 因为在操作的时候我们使用数据库的连接和它的释放是一个频繁的插入和删除的操作，
     * 所以在这里最好的最大就是使用链表进行操作 
     */
    private LinkedList<DataSource> pDataSourePool = new LinkedList<DataSource>();

//    private static String connstring = "MySQL:testdb,user=root,port=3306,password=123456,host=localhost";
    
    //设置连接池的常用连接数目
    private Integer connectionsNum = 5;

    // 数据库连接池中的最大负荷
    private Integer connectionMax = 20;
     
    // 当前的已经创建连接数
    private Integer currentCount = 0;
    /**
     * 类在构造的时候按照给定的连接池的大小进行数据源的创建操作，
     * 连接创建以后，把创建的对象放置到list中去
     */
    public DataSouPool() {
        if(connectionsNum > connectionMax){
            throw new ExceptionInInitializerError("数据库连接池初始化参数异常！！！");
        }
        for(int i=0; i<connectionsNum; i++){
        	DataSource pDataSource = createDataSource();
            if(pDataSource!=null){
                this.currentCount++;
                pDataSourePool.addLast(pDataSource);
            }else {
                throw new ExceptionInInitializerError("数据库连接池初始化时候出现异常！！！");
            }
        }
    }
     
    /**
     * 创建一个connection对象
     * @return 和数据库进行连接的connection对象，异常的时候是一个null
     * @throws SQLException
     */
    public static DataSource createDataSource(){
    	DataSource pDataSou=null;
		ogr.RegisterAll();
        org.gdal.ogr.Driver dbDriver =ogr.GetDriverByName("MySQL");
        if (dbDriver == null)
        {
            System.out.println("Driver Open failed");
        }
        try {
			String path = DataSouPool.class.getClassLoader().getResource("/").toURI().getPath();		
			PropertiesUtil propertiesUtil = new PropertiesUtil(path+Constants.STR_DATABASE_CONF);
			if(propertiesUtil == null) return null;
			String host=propertiesUtil.getProperty("host");
        	String database=propertiesUtil.getProperty("database");
        	String port=propertiesUtil.getProperty("port");
        	//String url="mysql://"+host+":"+port+"/"+database;
        	String user=propertiesUtil.getProperty("user");
        	String password=propertiesUtil.getProperty("password");
        	
        	String connstring ="MySQL:"+database+",user="+user+",port="+port+",password="+password+",host="+host;
        	//connstring = "MySQL:testdb,user=root,port=3306,password=123456,host=localhost";
        	pDataSou = dbDriver.Open(connstring,0);
        	if (pDataSou !=null) {
        		System.out.println("Connect Successed!!!");
			}else {
				System.out.println("DataSource Open failed");
			}
		} catch (Exception e) {
			System.out.println("DataSource Open failed");
			System.out.println(e.toString());
		}		
    	return pDataSou;
    }
     
    /**
     * 从池中获取对象的操作
     * @return 链表的第一个元素
     * @throws SQLException 
     */
    public DataSource getConnection() throws SQLException{
        //加锁的处理，保证不同的线程在去连接的时候拿到的连接是不同的
        synchronized (pDataSourePool) {
            //表示的是连接池中的资源还够使用
            if(this.pDataSourePool.size()>0){
                return pDataSourePool.removeFirst();
            }
            //资源不够用，但是创建的对象数目小于我们设定的最大的时候，可以进行进一步的创建的操作
            if(currentCount < connectionMax){
            	DataSource pDataSource = createDataSource();
                if(pDataSource!=null){
                    currentCount++;
                }
                return pDataSource;
            }
            throw new SQLException("没有空闲的连接对象了");
        }
    }

    /**
     * 释放时候的操作就是把连接的对象放置在链表的末尾的操作
     * @param pDataSource 使用完毕的DataSource对象
     */
    public void free(DataSource pDataSource){
        pDataSourePool.addLast(pDataSource);
    }
    
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
