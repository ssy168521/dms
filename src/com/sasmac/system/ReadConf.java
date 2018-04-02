package com.sasmac.system;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public class ReadConf {

	public ReadConf() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String connStrg=getGDALConnStr("WebRoot/conf/dbConnConf.properties");
		System.out.println(connStrg);
		String[] connStr=getJDBCConnStr("WebRoot/conf/sysDbConnConf.properties");
		for (int i = 0; i < connStr.length; i++) {
			System.out.println(connStr[i]);
		}

	}
	
	public static String getGDALConnStr(String conffile)
	{
		String connConf="";
		Properties prop = new Properties();     
		try{
            //读取属性文件conf.properties
            InputStream inStream = new BufferedInputStream (new FileInputStream(conffile));
            prop.load(inStream);     ///加载属性列表
            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                //System.out.println(key+":"+prop.getProperty(key));
                if (key.equals("database")) {
					connConf=prop.getProperty(key)+","+connConf;
				}
                else {
					connConf+=key+"="+prop.getProperty(key)+",";
				}
            }
            inStream.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
		connConf=connConf.substring(0, connConf.length()-1);
		//System.out.println(connConf);
		connConf="MySQL:"+connConf;
		return connConf;		
	}
	
	public static String[] getJDBCConnStr(String conffile)
	{
		//返回一个有3个元素的数组，依次包括url，user，password
		String[] connStr=new String[3];
		String url=null;
		String user=null;
		String password=null;
		String port=null;
		String database=null;
		Properties prop = new Properties();
		try{
            //读取属性文件conf.properties
            InputStream inStream = new BufferedInputStream (new FileInputStream(conffile));
            prop.load(inStream);     ///加载属性列表
            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                //System.out.println(key+":"+prop.getProperty(key));
                if (key.equals("user")) {
					user=prop.getProperty(key);
				}else if(key.equals("password")) {
					password=prop.getProperty(key);
				}else if(key.equals("host")) {
					url="jdbc:mysql://"+prop.getProperty(key);
				}else if(key.equals("port")) {
					port=prop.getProperty(key);
				}else if(key.equals("database")) {
					database=prop.getProperty(key);
				}
            }
            inStream.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
		url=url+":"+port+"/"+database;
		connStr[0]=url;
		connStr[1]=user;
		connStr[2]=password;
		return connStr;
	}
	
	public static String getJDBCConnUrl(String connfile) {
		String[] connStr=getJDBCConnStr(connfile);
		String url=connStr[0];
		return url;
	}
}
