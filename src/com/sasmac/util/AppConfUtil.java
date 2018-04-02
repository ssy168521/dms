package com.sasmac.util;
import java.net.URISyntaxException;

import com.web.util.DbUtils;
import com.web.util.PropertiesUtil;
public class AppConfUtil {

	private PropertiesUtil pUtil;
	public static AppConfUtil getInstance()
	{
		 return new  AppConfUtil();
	}
	public void SetAppconFile(String ConfFileName)
	{
		try{
			String path = AppConfUtil.class.getClassLoader().getResource("/").toURI().getPath();
			 pUtil = new PropertiesUtil(path+"com/sasmac/conf/"+ConfFileName);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	 public String getProperty(String key){  
		 if(pUtil==null) return "";
		 return  pUtil.getProperty(key);
	 }
	 public String getProperty(String ConfFileName,String key){    
		
		try {
			String path = AppConfUtil.class.getClassLoader().getResource("/").toURI().getPath();
			PropertiesUtil pUtil = new PropertiesUtil(path+"com/sasmac/conf/"+ConfFileName);
			 return pUtil.getProperty(key);
		   
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return "";
	 }
	
}
