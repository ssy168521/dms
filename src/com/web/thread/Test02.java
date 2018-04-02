package com.web.thread;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.opengis.coverage.grid.GridCoordinates;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import java.io .File;

public class Test02 {
    public static void main(String[] args) throws Exception {
        initTif();
  
    }
    /**
     * 注意jar的引用可能引起“Could not initialize class org.hsqldb.lib.FrameworkLogger”这类错误
     * log4j-over-slf4j-1.7.24.jar包与geotoolsjar包冲突。
     * @throws Exception
     */
    private static void initTif() throws Exception {
              //  Tif2DB WriteIn=new Tif2DB();
                File imageFile1=new File("E:\\database\\data\\J46D001004.tif");
                String  Name=imageFile1.getName();
                String fileName=Name.substring(0,Name.lastIndexOf("."));
                String filePath=imageFile1.getParent().replace("\\", "\\\\");//
                //String satellit=fileName.substring(0,fileName.indexOf("_"));
//               WriteIn .setFileName(fileName);//写入文件名
//               WriteIn.setFilePath(filePath);//写入文件路径
//               WriteIn.setSatellite(satellit);//写入卫星
                if (imageFile1.exists() && imageFile1.isFile()){  
                	long fileSize=imageFile1.length()/1024/1024;//
                	System.out.println("文件大小："+fileSize);
             //   	WriteIn .setFileSize(fileSize);//写入文件大小
                }else{  
                	System.out.println("file doesn't exist or is not a file");  
                } 
                GeoTiffReader reader = new GeoTiffReader(imageFile1);
                GridEnvelope dimensions = reader.getOriginalGridRange();
	            GridCoordinates maxDimensions = dimensions.getHigh();
	            
	            GridCoverage2D coverage = reader.read(null);            
	            int numBands = coverage.getNumSampleDimensions();//读取影像波段数
	      //      WriteIn.setBandsNumber(numBands);
	            System.out.println("波段数："+numBands);
	         
	            /*
	             * 影像数据大小
	             */
	            int w = maxDimensions.getCoordinateValue(0)+1;//得到图片宽度像素
	            int h = maxDimensions.getCoordinateValue(1)+1;//得到图片高度像素
	       //     WriteIn.setColumns(w);//写入变量w
	       //     WriteIn.setRows(h);
	            //GridGeometry2D geometry = coverage.getGridGeometry();
                
	            /*
	             * 投影坐标系统
	             */
	            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D(); 
	            String crs0=crs.toString().replace("\"","\\\"");
	            //System.out.println(crs);
	            //WriteIn.setSpatialReference(crs0);
	            /*
	             * 图像范围
	             */
	            Envelope2D coverageEnvelope = coverage.getEnvelope2D();
	            double coverageMinX = coverageEnvelope.getBounds().getMinX();
	            double coverageMaxX = coverageEnvelope.getBounds().getMaxX();
	            double coverageMinY = coverageEnvelope.getBounds().getMinY();
	            double coverageMaxY = coverageEnvelope.getBounds().getMaxY();
	            
	            System.out.println(coverageMinX+" "+coverageMaxX+" "+coverageMinY+" "+coverageMaxY);
	            //像元值
	            int CellSizeX=(int) Math.floor((coverageMaxX-coverageMinX)/w);
	            int CellSizeY=(int) Math.floor((coverageMaxY-coverageMinY)/h);
	            System.out.println(CellSizeX+" "+CellSizeY);
	       //     WriteIn.setCellSizexy(CellSizeX+" "+CellSizeY);	            
	            
	            //创建坐标转换类的实例
	            CoordinateConversion coord=new CoordinateConversion();	 
	            /*
	             * 获取矩形的顶点坐标，顺时针获取，从最高点（左上角）开始
	             */
	            //DecimalFormat df = new DecimalFormat( "0.0000"); //限定小数位数
                String wkt = "GEOMFROMTEXT('polygon((";
        		// 平面坐标转为经纬度
        		wkt += coord.convert(coverageMinX, coverageMaxY, crs) + ",";
        		wkt += coord.convert(coverageMaxX, coverageMaxY, crs) + ",";
        		wkt += coord.convert(coverageMaxX, coverageMinY, crs) + ",";
        		wkt += coord.convert(coverageMinX, coverageMinY, crs) + ",";      		
        		wkt += coord.convert(coverageMinX, coverageMaxY, crs) + "))')";
        		System.out.println(wkt);
        		//WriteIn.setWktstring(wkt);
               

	           }
	    	


    }


