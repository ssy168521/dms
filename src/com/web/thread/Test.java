package com.web.thread;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.opengis.coverage.grid.GridCoordinates;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class Test {
    public static void main(String[] args) throws Exception {
        initTif();
   
    }

    private static void initTif() throws Exception {
        Tif2DB WriteIn=new Tif2DB();
        File imageFile1=new File("E:\\database\\data\\J46D001010.tif");
        String  Name=imageFile1.getName();
        String fileName=Name.substring(0,Name.lastIndexOf("."));
        String filePath=imageFile1.getParent().replace("\\", "\\\\");//
        String RasterFormat=Name.substring(Name.lastIndexOf(".")+1);
        String satellite=" ";                   //卫星名设为空
       WriteIn .setFileName(fileName);          //写入文件名
       WriteIn.setFilePath(filePath);          //写入文件路径
       WriteIn.setSatellite(satellite);        //写入卫星
       WriteIn.setRasterFormat(RasterFormat);  //写入影像类型
        if (imageFile1.exists() && imageFile1.isFile()){  
        	long fileSize=imageFile1.length()/1024/1024;//
        	System.out.println("文件大小："+fileSize);
        	WriteIn .setFileSize(fileSize);//写入文件大小
        }else{  
        	System.out.println("file doesn't exist or is not a file");  
        } 
        GeoTiffReader reader = new GeoTiffReader(imageFile1);
        GridEnvelope dimensions = reader.getOriginalGridRange();
        GridCoordinates maxDimensions = dimensions.getHigh();
        
        GridCoverage2D coverage = reader.read(null);            
        int numBands = coverage.getNumSampleDimensions();//读取影像波段数
        WriteIn.setBandsNumber(numBands);
        System.out.println("波段数："+numBands);
     
        /*
         * 影像大小
         */
        int w = maxDimensions.getCoordinateValue(0)+1;//得到图片宽度像素
        int h = maxDimensions.getCoordinateValue(1)+1;//得到图片高度像素
        WriteIn.setColumns(w);//写入变量w
        WriteIn.setRows(h);
        
        /*
         * 投影坐标系统
         */
        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D(); 

        /*
         * 影像范围
         */
        Envelope2D coverageEnvelope = coverage.getEnvelope2D();
        double coverageMinX = Math.round(coverageEnvelope.getBounds().getMinX()*100)/100;
        double coverageMaxX = Math.round(coverageEnvelope.getBounds().getMaxX()*100)/100;
        double coverageMinY = Math.round(coverageEnvelope.getBounds().getMinY()*100)/100;
        double coverageMaxY = Math.round(coverageEnvelope.getBounds().getMaxY()*100)/100;
        WriteIn.setExtentRight(coverageMaxX);
        WriteIn.setExtentLeft(coverageMinX);
        WriteIn.setExtentTop(coverageMaxY);
        WriteIn .setExtentBottom(coverageMinY);
        System.out.println(coverageMinX+" "+coverageMaxX+" "+coverageMinY+" "+coverageMaxY);
        //像元值
        int CellSizeX=(int) Math.floor((coverageMaxX-coverageMinX)/w);
        int CellSizeY=(int) Math.floor((coverageMaxY-coverageMinY)/h);
        System.out.println(CellSizeX+" "+CellSizeY);
        WriteIn.setCellSizexy(CellSizeX+","+CellSizeY);	            
        
        //创建坐标转换类的实例
        CoordinateConversion coord=new CoordinateConversion();	 
        
          //获取矩形的顶点坐标，顺时针获取，从最高点（左上角）开始
         
        //DecimalFormat df = new DecimalFormat( "0.0000"); //限定小数位数
        String wkt = "GEOMFROMTEXT('polygon((";
		// 平面坐标转为经纬度
		wkt += coord.convert(coverageMinX, coverageMaxY, crs) + ",";
		wkt += coord.convert(coverageMaxX, coverageMaxY, crs) + ",";
		wkt += coord.convert(coverageMaxX, coverageMinY, crs) + ",";
		wkt += coord.convert(coverageMinX, coverageMinY, crs) + ",";      		
		wkt += coord.convert(coverageMinX, coverageMaxY, crs) + "))')";
		System.out.println(wkt);
		WriteIn.setWktString(wkt);
         
                /*
                 * 插入数据库
                 */      
        		String sql = "insert into tif2db1";
                //String sql="insert into tif2db1 values(";

         	sql += "(FileName, FilePath, FileSize, satellite, acquisitionTime, archiveTime, CellSizexy,"
         		+ "Columns,Rows, BandsNumber,RasterFormat,ExtentTop,ExtentLeft,ExtentBottom,ExtentRight,Shape)"
        		+ " values(";
        		sql += "'" + WriteIn.getFileName() + "',";
        		sql += "'" + WriteIn.getFilePath() + "',";
        		sql += "'" + WriteIn.getFileSize() + "',";
        		sql += "'" +WriteIn.getSatellite() + "',";
             DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             String str1 = formatter.format(new Date());
        		sql += "'" +str1+ "',";
            	String str2 = formatter.format(new Date());
        		sql += "'" +str2+ "',";
        		sql += "'"+WriteIn.getCellSizexy() + "',";      	
        		sql += "'" + WriteIn.getColumns()+ "',";
        		sql += "'" + WriteIn.getRows()+ "',";
        		sql += "'" + WriteIn.getBandsNumber()+ "',";
        		sql += "'"+WriteIn.getRasterFormat()+ "',";     		
        		sql += "'"+WriteIn.getExtentTop()+"',";
        		sql += "'"+WriteIn.getExtentLeft()+"',";
        		sql += "'"+WriteIn.getExtentBottom()+"',";
        		sql += "'"+WriteIn.getExtentRight()+"',"; 
        		sql += WriteIn.getWktString();
        		sql += ");";		
        		System.out.println(sql);
//        		try {
//        	    	Dbconnect ed=new Dbconnect();//声明DBconnect类的成员变量
//        	    	//连接MySQL
//        	    	ed.initParam("E:\\MyEclipse2014 workspace\\archives\\libs\\mysql.ini");
//        	    	//插入数据库
//        			ed.Insert(sql);
//        		} catch (Exception e) {
//        			e.printStackTrace();
//        		} finally {
//
//        		}
	           }
	    	


    }


