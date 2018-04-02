package com.web.thread;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.opengis.coverage.grid.GridCoordinates;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class Gdal_Image2Db {

	public Gdal_Image2Db() {

	}

	public static void main(String[] args) {

		Image2Db("E:\\database\\data\\J46D001004.tif");
	}

	public static void Image2Db(String strImageUrl) {
		Tif2DB WriteIn=new Tif2DB();
        File imageFile1=new File(strImageUrl);
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

		gdal.AllRegister();
		//读取图像
		Dataset hDataset = gdal.Open(strImageUrl,
				gdalconstConstants.GA_ReadOnly);
		if (hDataset == null)
			return;
		int numBands=hDataset.GetRasterCount();
		
		System.out.println(numBands);     //读取影像波段数
		String crsString=hDataset.GetProjectionRef();//获取crs
		//System.out.println(crsString);
		SpatialReference Crs=new SpatialReference(crsString);//构造投影坐标系统的空间参考(wkt)，
		//System.out.println(Crs);
		
		SpatialReference oLatLong; 	
        oLatLong = Crs.CloneGeogCS(); //获取该投影坐标系统中的地理坐标系   

        //构造一个从投影坐标系统到地理坐标系统的转换关系  
        CoordinateTransformation ct = new CoordinateTransformation(Crs, oLatLong);
        
		int xSize = hDataset.GetRasterXSize();//图像宽度
		int ySize = hDataset.GetRasterYSize();//图像高度
		WriteIn.setColumns(xSize);
		WriteIn.setRows(ySize);
		
		double[] geoTransform = hDataset.GetGeoTransform();
        
//		double dbX[]=new double[4];
//	    double dbY[]=new double[4];
//	    dbX[0]=geoTransform[0];
//	    dbY[0]=geoTransform[3];//左上角点坐标
//	    //右上角坐标
//	    dbX[1]= geoTransform[0] + xSize * geoTransform[1];
//	    dbY[1]= geoTransform[3];
//	    //右下角点坐标
//	    dbX[2]=geoTransform[0] + xSize * geoTransform[1] + ySize * geoTransform[2];
//	    dbY[2]=geoTransform[3] + xSize * geoTransform[4] + ySize * geoTransform[5];
//	    //左下角坐标
//	    dbX[3]=geoTransform[0];
//	    dbY[3]=geoTransform[3] + ySize * geoTransform[5];
		
	    //图像范围
		double xmin = geoTransform[0];
		double ymax = geoTransform[3];
		double xmax = geoTransform[0] + xSize * geoTransform[1];
		double ymin = geoTransform[3] + ySize * geoTransform[5];
		WriteIn.setExtentRight(xmax);
	    WriteIn.setExtentLeft(xmin);
	    WriteIn.setExtentTop(ymax);
	    WriteIn.setExtentBottom(ymax);
	    
	    double CellSizeX=geoTransform[1]; //x方向像元值
	    double CellSizeY=geoTransform[5]; //y方向像元值
	    WriteIn.setCellSizexy(CellSizeX+" "+CellSizeY);
       
		//ct.TransformPoint()，投影坐标转地理坐标
		double a[]= ct.TransformPoint(xmin, ymax) ;
		double b[]= ct.TransformPoint(xmax, ymax) ;
		double c[]= ct.TransformPoint(xmax, ymin) ;
		double d[]= ct.TransformPoint(xmin, ymin) ;  
		
        // 获取矩形的顶点坐标，顺时针获取，从最高点（左上角）开始
        DecimalFormat df = new DecimalFormat( "0.0000"); //限定小数位数
        String wkt = "GEOMFROMTEXT('polygon((";
		// 平面投影坐标转为经纬度
		wkt += df.format(a[0])+" "+df.format(a[1]) + ",";
		wkt += df.format(b[0])+" "+df.format(b[1]) + ",";
		wkt += df.format(c[0])+" "+df.format(c[1]) + ",";
		wkt += df.format(d[0])+" "+df.format(d[1]) + ",";      		
		wkt += df.format(a[0])+" "+df.format(a[1]) + "))')";
		System.out.println(wkt);
		WriteIn.setWktString(wkt);
		
     //System.out.println(dbX[0]+" "+dbY[0]+", "+dbX[1]+" "+dbY[1]+", "+dbX[2]+" "+dbY[2]+", "+dbX[3]+" "+dbY[3]);
     //System.out.println(a[0]+" "+a[1]+" "+b[0]+" "+b[1]+" "+c[0]+" "+c[1]+" "+d[0]+" "+d[1]);
	 hDataset.delete();  



//         
//            /*
//             * 插入数据库
//             */      
//    		String sql = "insert into tif2db1";
//            //String sql="insert into tif2db1 values(";
//
//     	sql += "(FileName, FilePath, FileSize, satellite, acquisitionTime, archiveTime, CellSizexy,"
//     		+ "Columns,Rows, BandsNumber,RasterFormat,ExtentTop,ExtentLeft,ExtentBottom,ExtentRight,Shape)"
//    		+ " values(";
//    		sql += "'" + WriteIn.getFileName() + "',";
//    		sql += "'" + WriteIn.getFilePath() + "',";
//    		sql += "'" + WriteIn.getFileSize() + "',";
//    		sql += "'" +WriteIn.getSatellite() + "',";
//         DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//         String str1 = formatter.format(new Date());
//    		sql += "'" +str1+ "',";
//        	String str2 = formatter.format(new Date());
//    		sql += "'" +str2+ "',";
//    		sql += "'"+WriteIn.getCellSizexy() + "',";      	
//    		sql += "'" + WriteIn.getColumns()+ "',";
//    		sql += "'" + WriteIn.getRows()+ "',";
//    		sql += "'" + WriteIn.getBandsNumber()+ "',";
//    		sql += "'"+WriteIn.getRasterFormat()+ "',";     		
//    		sql += "'"+WriteIn.getExtentTop()+"',";
//    		sql += "'"+WriteIn.getExtentLeft()+"',";
//    		sql += "'"+WriteIn.getExtentBottom()+"',";
//    		sql += "'"+WriteIn.getExtentRight()+"',"; 
//    		sql += WriteIn.getWktString();
//    		sql += ");";		
//    		System.out.println(sql);
//    		try {
//    	    	Dbconnect ed=new Dbconnect();//声明DBconnect类的成员变量
//    	    	//连接MySQL
//    	    	ed.initParam("E:\\MyEclipse2014 workspace\\archives\\libs\\mysql.ini");
//    	    	//插入数据库
//    			ed.Insert(sql);
//    		} catch (Exception e) {
//    			e.printStackTrace();
//    		} finally {
//
//        		}
//    	
		
		
	}

}
