package com.web.thread;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;

import com.web.util.DbUtils;

public class MyBaseThread extends Thread {
	
    protected String system = "";// = TaskType.UPLOAD.getValue();
    protected Connection connection = null;
    public static int iConnectNum=0;
	public static final int stopActiveTaskTime = 2;
	
	public static HashMap<String, String> activeRemoteWaitMap= new HashMap<String,String>();
	
	public MyBaseThread() {
    }
	
    public void initDB() {
        try {
        	if(connection == null){	        	
	        	connection = DbUtils.getConnection(true);
        	}
        } catch (Exception ex) {
            System.out.println(new Date().toString() + "********InitDB():" + ex.toString());
        }
    }
    
    public void closeConnection() {
        try {
        	DbUtils.closeQuietlyConnection(connection);
        	connection = null;
        } catch (Exception ex) {
            System.out.println(new Date().toString() + "********InitDB():" + ex.toString());
        }
    }


	public static boolean readParamters() {
        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder xmlParser = factory.newDocumentBuilder();
//            String szPath;
//            
//            szPath = System.getProperty("catalina.home");
//            String strSeparate = java.io.File.separator;
//            if (strSeparate.equals("\\")) {
//                szPath = "C:/dms";
//            } else {
//                szPath = "/msp";
//            }
//            Constants.dataSourcePath = szPath + "/conf/webapp/dss-dms-dataSource.xml";
//            Document doc = xmlParser.parse(szPath + "/conf/webapp/dss-dms-server.xml");
//            
//            Element root = doc.getDocumentElement();
//            
//            //系统日志路径
//            Constants.szLog = root.getElementsByTagName("WriteLog").item(0).getFirstChild().getNodeValue();
//            //存储区文件存放路径
//            Constants.szOnlineDir = root.getElementsByTagName("OnlineDir").item(0).getFirstChild().getNodeValue();
//            //MSP存储位置名称
            
        } catch (Exception e) {
            System.out.println(new Date().toString() + "****************Read Parameters:" + e.toString());
            return false;
        }
        return true;
    }

	
    
	
}