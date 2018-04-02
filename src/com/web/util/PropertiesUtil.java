package com.web.util;

import java.io.File;  
import java.io.FileInputStream;    
import java.io.FileOutputStream;    
import java.io.IOException;    
import java.io.InputStream;  
import java.io.OutputStream;    
import java.net.URI;  
import java.net.URL;
import java.util.Enumeration;    
import java.util.HashMap;    
import java.util.Map;    
import java.util.Properties;    
import java.util.ResourceBundle;  

import com.web.common.Constants;
  
  
/** 
 *  
 * PropertiesUtil.java 
 * 
 * @desc properties 资源文件解析工具 
 * @author ssy 
 * @datatime Apr 7, 2017 3:58:45 PM 
 * 
 */  
public class PropertiesUtil {    
    
    private Properties props;    
    private URI uri;  
    
    
    public PropertiesUtil(String fileName){    
        readProperties(fileName);    
    }    
    private void readProperties(String fileName) {    
        try {    
            props = new Properties();    
    
            InputStream fis=new FileInputStream(fileName); 
            if(fis == null)
            {
            	Constants.WriteLog(fileName+" FileInputStream  read  error");
            	return ;
            }
            if(fileName.contains(".xml"))
            {
              props.loadFromXML(fis);
            }
            else if(fileName.contains(".properties"))
            {
            	props.load(fis);
            }
            else
            {
            	Constants.WriteLog(fileName+"   read  error");
            }
          
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
    }    
    /**  
     * 获取某个属性  
     */    
    public String getProperty(String key){    
        return props.getProperty(key);    
    }    
    /**  
     * 获取所有属性，返回一个map,不常用  
     * 可以试试props.putAll(t)  
     */    
    public Map getAllProperty(){    
        Map map=new HashMap();    
        Enumeration enu = props.propertyNames();    
        while (enu.hasMoreElements()) {    
            String key = (String) enu.nextElement();    
            String value = props.getProperty(key);    
            map.put(key, value);    
        }    
        return map;    
    }    
    /**  
     * 在控制台上打印出所有属性，调试时用。  
     */    
    public void printProperties(){    
        props.list(System.out);    
    }    
    /**  
     * 写入properties信息  
     */    
    public void writeProperties(String key, String value) {    
        try {    
        OutputStream fos = new FileOutputStream(new File(uri));    
            props.setProperty(key, value);    
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流    
            props.store(fos, "comments Update key：" + key);    
        } catch (Exception e) {    
        e.printStackTrace();  
        }    
    }       
    public static void main(String[] args) {    
        PropertiesUtil util=new PropertiesUtil("src/dbConfig.properties");    
        util.writeProperties("dbtype", "MSSQL");    
    }        
}    