package com.sasmac.meta;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.DocumentException;
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
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.sasmac.dbconnpool.ConnPoolUtil;
import com.web.thread.CoordinateConversion;
import com.web.thread.Dbconnect;

/**
 * 读取xml信息插入数据库
 * @author Administrator
 * @ClassName:Meta2Database
 * @Version 版本
 * @ModifiedBy 修改人
 * @date 2018年3月20日 下午4:42:50
 */
public class Meta2Database {
	private Logger myLogger = LogManager.getLogger("mylog");

	/**
	 * 读取xml信息插入数据库
	 * @throws DocumentException 
	 * @throws ParseException 
	 */
	public void xml2Db(String filename,String productName,String productTable,String xmlName) throws DocumentException{
		File xmlFile = new File(xmlName);
		org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
		org.dom4j.Document doc = reader.read(xmlFile);
		org.dom4j.Node node = null;
		Tif2DB tif2db =new Tif2DB();		
		Connection conn = null;
		Statement stmt = null;
		NodePathParser meta=new NodePathParser();
		
       //文件名
		 tif2db.setFileName(filename);
	   //dataid编号：去掉文件名中的字符，将数字组合再一起构成新的数字码，具有唯一性，J46D001001,46是100万比例尺列号码，dataid：46001001
		 filename=filename.trim();
		 String iddata="";
		 if(filename != null && !"".equals(filename)){
		 for(int i=0;i<filename.length();i++){
		 if(filename.charAt(i)>=48 && filename.charAt(i)<=57){
			 iddata+=filename.charAt(i);
		         }
		     }
		 }
	     int dataid=Integer.parseInt(iddata);  //唯一标识dataid
		 tif2db.setDataid(dataid);
         //产品路径
		 xmlName=xmlName.replace("\\", "\\\\");
		 tif2db.setFilePath(xmlName);
	     //当前系统时间就是归档时间
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		 String archiveTime = df.format(new Date());  // new Date()为获取当前系统时间
		 tif2db.setArchiveTime(archiveTime);

		if(meta.getNodePath(productName,productTable,"FileSize")!=null){
			node = doc.selectSingleNode(meta.getNodePath(productName,productTable,"FileSize"));
			if (node != null) {
				Integer str = Integer.parseInt(node.getText());
				tif2db.setFileSize(str);
			}
		}
		if(meta.getNodePath(productName,productTable,"BandsNumber")!=null){
			node = doc.selectSingleNode(meta.getNodePath(productName,productTable,"BandsNumber"));
			if (node != null) {
				Integer str = Integer.parseInt(node.getText());
				tif2db.setBandsNumber(str);
			}
		}
		if(meta.getNodePath(productName,productTable,"CellSizexy")!=null){
			node = doc.selectSingleNode(meta.getNodePath(productName,productTable,"CellSizexy"));
			if (node != null) {
				String str = node.getText();
				tif2db.setCellSizexy(str);
			}
		}
		if(meta.getNodePath(productName,productTable,"Columns")!=null){
			node = doc.selectSingleNode(meta.getNodePath(productName,productTable,"Columns"));
			if (node != null) {
				double str = Double.parseDouble(node.getText());
				tif2db.setColumns(str);
			}
		}
		if(meta.getNodePath(productName,productTable,"Rows")!=null){
			node = doc.selectSingleNode(meta.getNodePath(productName,productTable,"Rows"));
			if (node != null) {
				double str = Double.parseDouble(node.getText());
				tif2db.setRows(str);
			}
		}
		if(meta.getNodePath(productName,productTable,"DataType")!=null){
			node = doc.selectSingleNode(meta.getNodePath(productName,productTable,"DataType"));
			if (node != null) {
				String str = node.getText();
				tif2db.setDataType(str);
			}
		}
             			
		if(meta.getNodePath(productName,productTable,"acquisitionTime")!=null){
			node = doc.selectSingleNode(meta.getNodePath(productName,productTable,"acquisitionTime"));						
			if (node != null) {
				String str = node.getText();
			    tif2db.setAcquisitionTime(str);		
			}
		}

		if(meta.getNodePath(productName,productTable,"productid")!=null){
			node = doc.selectSingleNode(meta.getNodePath(productName,productTable,"productid"));
			if (node != null) {
				Integer str = Integer.parseInt(node.getText());
				tif2db.setProductid(str);
			}
		}
		if(meta.getNodePath(productName,productTable,"productLevel")!=null){
			node = doc.selectSingleNode(meta.getNodePath(productName,productTable,"productLevel"));
			if (node != null) {
				Integer str = Integer.parseInt(node.getText());
				tif2db.setProductLevel(str);
			}
		}
			String ExtentRight="";
			String ExtentLeft="";
			String ExtentTop="";
			String ExtentBottom="";
		 if(meta.getNodePath(productName,productTable,"ExtentRight")!=null){
			node = doc.selectSingleNode(meta.getNodePath(productName,productTable,"ExtentRight"));
			if (node != null) {
				ExtentRight = node.getText();
				tif2db.setExtentRight(Double.parseDouble(ExtentRight));
			}
		 }
		 if(meta.getNodePath(productName,productTable,"ExtentLeft")!=null){
			node = doc.selectSingleNode(meta.getNodePath(productName,productTable,"ExtentLeft"));
			if (node != null) {
				ExtentLeft = node.getText();
				tif2db.setExtentLeft(Double.parseDouble(ExtentLeft));
			}
		 }
		 if(meta.getNodePath(productName,productTable,"ExtentTop")!=null){
			node = doc.selectSingleNode(meta.getNodePath(productName,productTable,"ExtentTop"));
			if (node != null) {
				ExtentTop = node.getText();
				tif2db.setExtentTop(Double.parseDouble(ExtentTop));
			}
		 }
		 if(meta.getNodePath(productName,productTable,"ExtentBottom")!=null){
			node = doc.selectSingleNode(meta.getNodePath(productName,productTable,"ExtentBottom"));
			if (node != null) {
				ExtentBottom = node.getText();
				tif2db.setExtentBottom(Double.parseDouble(ExtentBottom));
			}
		 }
			String wkt = "GEOMFROMTEXT('polygon((";
			wkt += ExtentLeft + " ";
			wkt += ExtentTop + ",";
			wkt += ExtentRight + " ";
			wkt += ExtentTop + ",";
			wkt += ExtentRight + " ";
			wkt += ExtentBottom + ",";
			wkt += ExtentLeft + " ";
			wkt += ExtentBottom + ",";
			wkt += ExtentLeft + " ";
			wkt += ExtentTop + "))')";
			tif2db.setWktString(wkt);

			 String sql="insert into "+productTable;

     		sql += "(fid,dataid,FileName,FilePath,FileSize,acquisitionTime,archiveTime,"
     				+ "productid,productLevel,CellSizexy,Columns,Rows,BandsNumber,DataType,ExtentTop,ExtentLeft,"
     				+ "ExtentBottom,ExtentRight,Shape)"
     				+ " values(";
     		sql += null+",";
     		sql += tif2db.getDataid()+",";
     		
     		sql += "'" + tif2db.getFileName() + "',";
     		sql += "'" + tif2db.getFilePath() + "',";
     		sql += tif2db.getFileSize() + ",";

     		sql += "'" +tif2db.getAcquisitionTime() + "',";
     		sql += "'" +tif2db.getArchiveTime()+ "',";

     		sql += tif2db.getProductid()  +",";
     		sql += tif2db.getProductLevel()+",";
     		sql += "'"+tif2db.getCellSizexy() + "',";      	
     		sql += tif2db.getColumns()+ ",";
     		sql += tif2db.getRows()+ ",";
     		sql += tif2db.getBandsNumber()+ ",";
     		sql += "'"+tif2db.getDataType()+ "',";
     		sql += "'"+tif2db.getExtentTop()+"',";
     		sql += "'"+tif2db.getExtentLeft()+"',";
     		sql += "'"+tif2db.getExtentBottom()+"',";
     		sql += "'"+tif2db.getExtentRight()+"',"; 
     		sql += tif2db.getWktString();
     		sql += ");";		
     		try {
     			conn = ConnPoolUtil.getConnection();
				stmt = conn.createStatement();
				boolean b1 = stmt.execute(sql);
				//boolean b2 = stmt.execute(sql2);
				if(!b1 ){
					myLogger.info("导入数据库成功！");
				}else{
					myLogger.info("导入数据库出错！");
				}
     		} catch (Exception e) {
     			e.printStackTrace();
     		} finally {
     			ConnPoolUtil.close(conn, null, null);
     		}
		
	}
	/**
	 * 读取 分景 tif影像元数据并入库
	 * @param filename  J46D001005.tif
	 * @param tablename
	 * @param tiffpath
	 * @throws IOException 
	 */
	public void tif2Db(String satellite, String filename, String tablename, String tiffpath) throws IOException {
		Connection conn = null;
		Statement stmt = null;
		File imageFile=new File(tiffpath);  
		Tif2DB dataInfo=new Tif2DB();
        String filePath=imageFile.getParent().replace("\\", "\\\\");
        String RasterFormat=tiffpath.substring(tiffpath.lastIndexOf(".")+1);
        //split切分字符串，循环求出第三个各字符串即为日期
        String[] ss=filename.split("_");
        String acquisitionTime=ss[2];        //产品制作时间
    	int dataid = 0;

		conn = ConnPoolUtil.getConnection();
		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //判断dataid是否已存在，已有则另加一
        if(satellite.contains("GF")){
        	int dataid1 = Integer.parseInt(ss[5].substring(6,13));
        	//判断dataid是否重复
    		String sql = "SELECT *FROM "+ tablename +" WHERE dataid =\""+dataid1+"\";";
    		ResultSet result = null;
			try {
				result = stmt.executeQuery(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		try {
				if(result.next()){
					dataid=Integer.parseInt(dataid1+"11");
				} else {
					dataid=dataid1;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }else if(satellite.contains("zy")){
        	dataid = Integer.parseInt(ss[8]);
        }else {
        	myLogger.info(" 错误：dataid未能正确提取！");
		}
            
        //当前系统时间就是归档时间
		 SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		 String archiveTime = dft.format(new Date());  // new Date()为获取当前系统时间
		 dataInfo.setArchiveTime(archiveTime);
	        dataInfo.setProductid(dataid);
	        dataInfo.setDataid(dataid);
	        dataInfo.setAcquisitionTime(acquisitionTime);
	        dataInfo.setFileName(filename);          //写入文件名
	        dataInfo.setFilePath(filePath);          //写入文件路径
	        dataInfo.setSatellite(satellite);        //写入卫星
	        dataInfo.setDataType(RasterFormat);  //写入影像类型
        if (imageFile.exists() && imageFile.isFile()){  
        	int fileSize=(int) (imageFile.length()/1024/1024);//
        	dataInfo.setFileSize(fileSize);//写入文件大小
        }else{  
        	myLogger.info("file doesn't exist or is not a file");  
        } 

		gdal.AllRegister();
		//读取图像
		Dataset hDataset = gdal.Open(tiffpath,
				gdalconstConstants.GA_ReadOnly);
		if (hDataset == null)
			return;
		int numBands=hDataset.GetRasterCount();
		dataInfo.setBandsNumber(numBands);
		String crsString=hDataset.GetProjectionRef();//获取crs
		SpatialReference Crs=new SpatialReference(crsString);//构造投影坐标系统的空间参考(wkt)，
		
		SpatialReference oLatLong; 	
        oLatLong = Crs.CloneGeogCS(); //获取该投影坐标系统中的地理坐标系   

        //构造一个从投影坐标系统到地理坐标系统的转换关系  
        CoordinateTransformation ct = new CoordinateTransformation(Crs, oLatLong);
        
		int xSize = hDataset.GetRasterXSize();//图像宽度
		int ySize = hDataset.GetRasterYSize();//图像高度
		dataInfo.setColumns(xSize);
		dataInfo.setRows(ySize);
		
		double[] geoTransform = hDataset.GetGeoTransform();
        		
	    //图像范围
		double xmin = geoTransform[0];
		double ymax = geoTransform[3];
		double xmax = geoTransform[0] + xSize * geoTransform[1];
		double ymin = geoTransform[3] + ySize * geoTransform[5];
		dataInfo.setExtentRight(xmax);
	    dataInfo.setExtentLeft(xmin);
	    dataInfo.setExtentTop(ymax);
	    dataInfo.setExtentBottom(ymax);
	    
	    double CellSizeX=geoTransform[1]; //x方向像元值
	    double CellSizeY=geoTransform[5]; //y方向像元值
	    dataInfo.setCellSizexy(CellSizeX+" "+CellSizeY);
       
		//ct.TransformPoint()，投影坐标转地理坐标
		double a[]= ct.TransformPoint(xmin, ymax) ;
		double b[]= ct.TransformPoint(xmax, ymax) ;
		double c[]= ct.TransformPoint(xmax, ymin) ;
		double d[]= ct.TransformPoint(xmin, ymin) ;  
		
        // 获取矩形的顶点坐标，顺时针获取，从最高点（左上角）开始
        DecimalFormat df = new DecimalFormat( "0.00000"); //限定小数位数
        String wkt = "GEOMFROMTEXT('polygon((";
		// 平面投影坐标转为经纬度
		wkt += df.format(a[0])+" "+df.format(a[1]) + ",";
		wkt += df.format(b[0])+" "+df.format(b[1]) + ",";
		wkt += df.format(c[0])+" "+df.format(c[1]) + ",";
		wkt += df.format(d[0])+" "+df.format(d[1]) + ",";      		
		wkt += df.format(a[0])+" "+df.format(a[1]) + "))')";
		dataInfo.setWktString(wkt);
		
	    hDataset.delete();  
		//执行数据插入操作
	    String sql="insert into "+tablename;

  		sql += "(fid,dataid,FileName,FilePath,FileSize,satellite,acquisitionTime,archiveTime,"
  				+ "productid,productLevel,CellSizexy,Columns,Rows,BandsNumber,DataType,ExtentTop,ExtentLeft,"
  				+ "ExtentBottom,ExtentRight,Shape)"
  				+ " values(";
  		sql += null+",";
  		sql += dataInfo.getDataid()+",";
  		
  		sql += "'" + dataInfo.getFileName() + "',";
  		sql += "'" + dataInfo.getFilePath() + "',";
  		sql += dataInfo.getFileSize() + ",";
        sql += "'" +dataInfo.getSatellite()+ "',";
  		sql += "'" +dataInfo.getAcquisitionTime() + "',";
  		sql += "'" +dataInfo.getArchiveTime()+ "',";

  		sql += dataInfo.getProductid()  +",";
  		sql += dataInfo.getProductLevel()+",";
  		sql += "'"+dataInfo.getCellSizexy() + "',";      	
  		sql += dataInfo.getColumns()+ ",";
  		sql += dataInfo.getRows()+ ",";
  		sql += dataInfo.getBandsNumber()+ ",";
  		sql += "'"+dataInfo.getDataType()+ "',";
  		sql += "'"+dataInfo.getExtentTop()+"',";
  		sql += "'"+dataInfo.getExtentLeft()+"',";
  		sql += "'"+dataInfo.getExtentBottom()+"',";
  		sql += "'"+dataInfo.getExtentRight()+"',"; 
  		sql += dataInfo.getWktString();
  		sql += ");";		
  		try {
//  			conn = ConnPoolUtil.getConnection();
//				stmt = conn.createStatement();
				boolean b1 = stmt.execute(sql);
				//boolean b2 = stmt.execute(sql2);
				if(!b1 ){
					myLogger.info("geotiff元数据导入数据库成功！");
				}else{
					myLogger.info("geotiff元数据导入数据库出错！");
				}
  		} catch (Exception e) {
  			e.printStackTrace();
  		} finally {
  			ConnPoolUtil.close(conn, null, null);
  		}
		
	}
	/**
	 * 分幅 tif读取并入库
	 * @param filename
	 * @param tablename
	 * @param tiffpath
	 */
	public void tifToDb(String filename, String tablename, String tiffpath) {
		Connection conn = null;
		Statement stmt = null;
		File imageFile=new File(tiffpath);  
		Tif2DB dataInfo=new Tif2DB();
        String filePath=imageFile.getParent().replace("\\", "\\\\");
        String RasterFormat=tiffpath.substring(tiffpath.lastIndexOf(".")+1);
        
		 filename=filename.trim();
		 String iddata="";
		 if(filename != null && !"".equals(filename)){
		 for(int i=0;i<filename.length();i++){
		 if(filename.charAt(i)>=48 && filename.charAt(i)<=57){
			 iddata+=filename.charAt(i);
		         }
		     }
		 }
	     int dataid=Integer.parseInt(iddata);  //唯一标识dataid
        String acquisitionTime="";        //产品制作时间    	
            
        //当前系统时间就是归档时间
		 SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		 String archiveTime = dft.format(new Date());  // new Date()为获取当前系统时间
		 dataInfo.setArchiveTime(archiveTime);
	        dataInfo.setProductid(dataid);
	        dataInfo.setDataid(dataid);
	        dataInfo.setAcquisitionTime(acquisitionTime);
	        dataInfo.setFileName(filename);          //写入文件名
	        dataInfo.setFilePath(filePath);          //写入文件路径
	        dataInfo.setDataType(RasterFormat);  //写入影像类型
        if (imageFile.exists() && imageFile.isFile()){  
        	int fileSize=(int) (imageFile.length()/1024/1024);//
        	dataInfo.setFileSize(fileSize);//写入文件大小
        }else{  
        	myLogger.info("file doesn't exist or is not a file");  
        } 

		gdal.AllRegister();
		//读取图像
		Dataset hDataset = gdal.Open(tiffpath,
				gdalconstConstants.GA_ReadOnly);
		if (hDataset == null)
			return;
		int numBands=hDataset.GetRasterCount();
		dataInfo.setBandsNumber(numBands);
		String crsString=hDataset.GetProjectionRef();//获取crs
		SpatialReference Crs=new SpatialReference(crsString);//构造投影坐标系统的空间参考(wkt)，
		
		SpatialReference oLatLong; 	
        oLatLong = Crs.CloneGeogCS(); //获取该投影坐标系统中的地理坐标系   

        //构造一个从投影坐标系统到地理坐标系统的转换关系  
        CoordinateTransformation ct = new CoordinateTransformation(Crs, oLatLong);
        
		int xSize = hDataset.GetRasterXSize();//图像宽度
		int ySize = hDataset.GetRasterYSize();//图像高度
		dataInfo.setColumns(xSize);
		dataInfo.setRows(ySize);
		
		double[] geoTransform = hDataset.GetGeoTransform();
        		
	    //图像范围
		double xmin = geoTransform[0];
		double ymax = geoTransform[3];
		double xmax = geoTransform[0] + xSize * geoTransform[1];
		double ymin = geoTransform[3] + ySize * geoTransform[5];
		dataInfo.setExtentRight(xmax);
	    dataInfo.setExtentLeft(xmin);
	    dataInfo.setExtentTop(ymax);
	    dataInfo.setExtentBottom(ymax);
	    
	    double CellSizeX=geoTransform[1]; //x方向像元值
	    double CellSizeY=geoTransform[5]; //y方向像元值
	    dataInfo.setCellSizexy(CellSizeX+" "+CellSizeY);
       
		//ct.TransformPoint()，投影坐标转地理坐标
		double a[]= ct.TransformPoint(xmin, ymax) ;
		double b[]= ct.TransformPoint(xmax, ymax) ;
		double c[]= ct.TransformPoint(xmax, ymin) ;
		double d[]= ct.TransformPoint(xmin, ymin) ;  
		
        // 获取矩形的顶点坐标，顺时针获取，从最高点（左上角）开始
        DecimalFormat df = new DecimalFormat( "0.00000"); //限定小数位数
        String wkt = "GEOMFROMTEXT('polygon((";
		// 平面投影坐标转为经纬度
		wkt += df.format(a[0])+" "+df.format(a[1]) + ",";
		wkt += df.format(b[0])+" "+df.format(b[1]) + ",";
		wkt += df.format(c[0])+" "+df.format(c[1]) + ",";
		wkt += df.format(d[0])+" "+df.format(d[1]) + ",";      		
		wkt += df.format(a[0])+" "+df.format(a[1]) + "))')";
		dataInfo.setWktString(wkt);
		
	    hDataset.delete();  
		//执行数据插入操作
	    String sql="insert into "+tablename;
       /**
        * fid,dataid,FileName,FilePath,FileSize,acquisitionTime,archiveTime,"
     				+ "productid,productLevel,CellSizexy,Columns,Rows,BandsNumber,DataType,ExtentTop,ExtentLeft,"
     				+ "ExtentBottom,ExtentRight,Shape)"
        */
  		sql += "(fid,dataid,FileName,FilePath,FileSize,acquisitionTime,archiveTime,"
  				+ "productid,productLevel,CellSizexy,Columns,Rows,BandsNumber,DataType,ExtentTop,ExtentLeft,"
  				+ "ExtentBottom,ExtentRight,Shape)"
  				+ " values(";
  		sql += null+",";
  		sql += dataInfo.getDataid()+",";
  		
  		sql += "'" + dataInfo.getFileName() + "',";
  		sql += "'" + dataInfo.getFilePath() + "',";
  		sql += dataInfo.getFileSize() + ",";

  		sql += "'" +dataInfo.getAcquisitionTime() + "',";
  		sql += "'" +dataInfo.getArchiveTime()+ "',";

  		sql += dataInfo.getProductid()  +",";
  		sql += dataInfo.getProductLevel()+",";
  		sql += "'"+dataInfo.getCellSizexy() + "',";      	
  		sql += dataInfo.getColumns()+ ",";
  		sql += dataInfo.getRows()+ ",";
  		sql += dataInfo.getBandsNumber()+ ",";
  		sql += "'"+dataInfo.getDataType()+ "',";
  		sql += "'"+dataInfo.getExtentTop()+"',";
  		sql += "'"+dataInfo.getExtentLeft()+"',";
  		sql += "'"+dataInfo.getExtentBottom()+"',";
  		sql += "'"+dataInfo.getExtentRight()+"',"; 
  		sql += dataInfo.getWktString();
  		sql += ");";		
  		try {
  			conn = ConnPoolUtil.getConnection();
				stmt = conn.createStatement();
				boolean b1 = stmt.execute(sql);
				//boolean b2 = stmt.execute(sql2);
				if(!b1 ){
					myLogger.info("geotiff元数据导入数据库成功！");
				}else{
					myLogger.info("geotiff元数据导入数据库出错！");
				}
  		} catch (Exception e) {
  			e.printStackTrace();
  		} finally {
  			ConnPoolUtil.close(conn, null, null);
  		}
		
	}

}
