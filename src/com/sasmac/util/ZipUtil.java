package com.sasmac.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {

    public static List<String> unZipFiles(File zipFile,String descDir)throws IOException{
        File pathFile = new File(descDir);
        List<String> outfiles=new ArrayList<String>();
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        for(Enumeration entries = zip.entries();entries.hasMoreElements();){
            ZipEntry entry = (ZipEntry)entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir+zipEntryName).replaceAll("\\\\", "/");
            //判断路径是否存在,不存在则创建文件路径
            String tmpString=outPath.substring(0, outPath.lastIndexOf('/'));
            File file = new File(tmpString);
            if(!file.exists()){
                file.mkdirs(); 
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压  
            if(new File(outPath).isDirectory()){
                continue;
            }
            //输出文件路径信息  
            System.out.println(outPath); 
            outfiles.add(outPath);
            
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while((len=in.read(buf1))>0){
                out.write(buf1,0,len); 
            }
            in.close();
            out.close();
        }
        zip.close();
        return outfiles;  
    }
    
    public static void main(String[] args) throws IOException {
    	List<String> outfiles=unZipFiles(new File("D:\\tomcat-7.0.70\\webapps\\zy3dms\\upload\\14933632.zip"),"D:\\tomcat-7.0.70\\webapps\\zy3dms\\upload\\");
    	System.out.println(outfiles.size());
    }
    
}
