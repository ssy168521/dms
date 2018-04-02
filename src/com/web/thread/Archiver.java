package com.web.thread;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;
import org.opengis.coverage.grid.GridCoordinates;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class Archiver{
	private String satellite;
	private String sensor;
	private String productLevel;
	private String photoDate;
	private int orbitNum;
	/** 数据在生产区的存储路径**/
	private String archivePath="D:\\database\\环境测试数据";//也就是文件原始路径
	/** 数据在存储区的存储位置**/
	private String storagePath;//也就是归档后文件路径
	/** 目标路径 **/
	private String destPath="GF1";
	
	private List<String> fileList = new ArrayList<String>();
	Tif2DB dataInfo =new Tif2DB();
	
public void run() {
		//Constants.WriteLog("归档扫描路径:" + archivePath);
		//while(true) {
			try {
		/*		if(Constants.getThreadStatus(satellite) == Constants.THREAD_NO_ACTIVE) {//当前卫星归档线程是否启用
					Constants.WriteLog("*********  System config has bean deleted ****************");
					break;
				}
		*/		
				//conn = DbUtils.getConnection(true);
				//创建rootPath实例
				java.io.File rootPath = new java.io.File(archivePath);
				if(!rootPath.exists() || rootPath.isFile()) {
					//Constants.WriteLog("该路径不存在");
					System.out.println("该路径不存在");
					return;
				}
				ergodicDir(rootPath);			
				//线程休眠60秒
				//Thread.sleep(1000 * 5);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				//DbUtils.closeQuietlyConnection(conn);
			}
		//}
	}

	/**
	 * 遍历归档路径下的所有文件
	 * @param myPath
	 * @throws Exception
	 */                                  
	private void ergodicDir(java.io.File myPath) throws Exception {
		java.io.File[] aF = myPath.listFiles();
		for(java.io.File fF : aF) {
			if(fF.isDirectory()) {//判断是否是一个目录
				ergodicDir(fF.getAbsoluteFile());//最终进行文件的单个读取
			} else {
				//int intNowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				// 判断当前时间是否为归档时长，start与end由页面获取
//				if (!isArchive(start, end, intNowHour)) {
//					return;
//				}
				
			//	if(!service.isFileArchive(conn, fF.getAbsolutePath(), fF.length())) {//判断文件是否归档
					//Constants.WriteLog("File: " + fF.getAbsolutePath());
				System.out.println("File: " + fF.getAbsolutePath());;
					if(isFinishCopy(fF.getAbsoluteFile())) {//判断文件是否被占用
						//if(isFinishCompress(fF.getAbsoluteFile())) {//判断文件是否完整
							
							//for(String fName : fileList) {
								/*
								 * 文件拷贝
								 */
								fileArchive(fF);
								System.out.println("    Archive file success.");
								String suffix = fF.getAbsolutePath().substring(fF.getAbsolutePath().lastIndexOf(".") + 1);
								if("tif".equalsIgnoreCase(suffix)) {
								getMetaDataFromTiff(fF);
								System.out.println("  insert database success");
								}
							//	dataInfo.setArchiveFile(fF);
								//更改文件的存储路径
							//	storagePath = storagePath + fF.substring(fF.lastIndexOf("\\") + 1);
							//	dataInfo.setStorageFile(storagePath);
								
//								if(service.insertProduct(conn, dataInfo)) {
//									service.updateStaticts(conn, satellite, 1);
//									System.out.println("    Insert file success.");
//								} else {
//									service.updateStaticts(conn, satellite, 0);
//									System.out.println("    Insert file failed.");
//								}
							//}
						//}
					} else {
						System.out.println("    This is occupied,read next file.");
					}
				//}
			}
		}
	}
	/**
	 * 判断文件是否拷贝完成
	 * @param fileName
	 * @return
	 */
	private boolean isFinishCopy(java.io.File fileName) {
		/** 等到文件拷贝完成再进行属性获取及判断等操作**/
		boolean bFlag = false;
		java.io.RandomAccessFile raf = null;

		try {
			//rw以读写方式打开指定文件
			raf = new java.io.RandomAccessFile(fileName,"rw");
			//将文件记录指针定位到 file.length() 的位置
			raf.seek(fileName.length());
			bFlag = true;
		} catch (Exception e) {
			System.out.println("文件正在被占用");
		} finally {
			try {
				raf.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return bFlag;
	}

	/**
	 * 遍历并记录所有文件的后缀名称
	 * @param path
	 */
//	private void getAllFiles(java.io.File path) {
//		java.io.File[] aF = path.listFiles();
//		for(java.io.File fF : aF) {
//			if(fF.isDirectory()) {
//				getAllFiles(fF.getAbsoluteFile());
//			} else {
//				//返回后缀名
//				String suffix = fF.getAbsolutePath().substring(fF.getAbsolutePath().lastIndexOf("\\") + 1);	
//			}
//		}
//		
//	}
	
	/**
	 * 数据拷贝完成后进行tif影像元数据读取入库操作
	 * @author computer
	 * @since 2017-11-16 08:59:51
	 */
	public void getMetaDataFromTiff(java.io.File imageFile)throws Exception{
        String Name=imageFile.getName();
        String fileName=Name.substring(0,Name.lastIndexOf("."));
        String filePath=imageFile.getParent().replace("\\", "/");//在写入数据库时将’\\’替换为’/’，在读取时再将’/’替换为’\\’
        String satellit=fileName.substring(0,fileName.indexOf("_"));
       dataInfo.setFileName(fileName);//写入文件名
       dataInfo.setFilePath(filePath);//写入文件路径
       dataInfo.setSatellite(satellit);//写入卫星
        if (imageFile.exists() && imageFile.isFile()){  
        	long fileSize=imageFile.length()/1024/1024;//
        	System.out.println("文件大小："+fileSize);
        	dataInfo .setFileSize(fileSize);//写入文件大小
        }else{  
        	System.out.println("file doesn't exist or is not a file");  
        } 
        GeoTiffReader reader = new GeoTiffReader(imageFile);
        GridEnvelope dimensions = reader.getOriginalGridRange();
        GridCoordinates maxDimensions = dimensions.getHigh();
        
        GridCoverage2D coverage = reader.read(null);            
        int numBands = coverage.getNumSampleDimensions();//读取影像波段数
        dataInfo.setBandsNumber(numBands);
        System.out.println("波段数："+numBands);
     
        /*
         * 影像数据大小
         */
        int w = maxDimensions.getCoordinateValue(0)+1;//得到图片宽度像素
        int h = maxDimensions.getCoordinateValue(1)+1;//得到图片高度像素
        dataInfo.setColumns(w);//写入变量w
        dataInfo.setRows(h);
        GridGeometry2D geometry = coverage.getGridGeometry();
        
        /*
         * 投影坐标系统
         */
        CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D(); 
//        String crs0=crs.toString().replace("\"","\\\"");
//        System.out.println(crs0);
//        dataInfo.setSpatialReference(crs0);
        /*
         * 影像的顶点坐标
         */
       
        Envelope2D pixelEnvelop = geometry.gridToWorld(new GridEnvelope2D(0, 0, w, h));//笛卡尔直角坐标系
        //影像范围
        DecimalFormat df = new DecimalFormat( "0.00000 "); //限定小数位数
        String Right = df.format(pixelEnvelop.getMaxX());
        String Left=df.format(pixelEnvelop.getMinX());
        String Top=df.format(pixelEnvelop.getMaxY());
        String Bottom=df.format(pixelEnvelop.getMinY());
//        dataInfo.setExtentRight(Right);
//        dataInfo.setExtentLeft(Left);
//        dataInfo.setExtentTop(Top);
//        dataInfo .setExtentBottom(Bottom);
        /*
         * 插入数据库
         */
        		//String TableName=dataInfo.getFileName();
        		//String sql = "insert into " + TableName;
                String sql="insert into tif2db3";

        		sql += "(FileName, FilePath, FileSize, satellite, acquisitionTime, archiveTime, CellSizeX,"
        				+ "CellSizeY,Columns,Rows, BandsNumber, format, SpatialReference, Datum,OrbitID,ExtentTop,ExtentLeft,ExtentBottom,ExtentRight)"
        				+ " values(";
        		sql += "'" + dataInfo.getFileName() + "',";
        		sql += "'" + dataInfo.getFilePath() + "',";
        		sql +=dataInfo.getFileSize() + ",";
        		sql += "'" +dataInfo.getSatellite() + "',";
//        		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        		String str = formatter.format(dataInfo.getAcquisitionTime());
        		sql += "'" +dataInfo.getAcquisitionTime() + "',";
//        	    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        		str = formatter.format(dataInfo.getArchiveTime());
        		sql += "'" +dataInfo.getArchiveTime()+ "',";
        		sql += dataInfo.getCellSizexy() + ",";      	
        		sql += dataInfo.getColumns()+ ",";
        		sql += dataInfo.getRows()+ ",";
        		sql += dataInfo.getBandsNumber()+ ",";
        		sql += "'"+dataInfo.getRasterFormat()+ "',";
        		sql += "'"+dataInfo.getWktString() + "',";
        		sql += "'"+dataInfo.getExtentTop()+"',";
        		sql += "'"+dataInfo.getExtentLeft()+"',";
        		sql += "'"+dataInfo.getExtentBottom()+"',";
        		sql += "'"+dataInfo.getExtentRight()+"'";   
        		sql += ");";		
        		System.out.println(sql);
        		try {
        	    	Dbconnect ed=new Dbconnect();//声明DBconnect类的成员变量
        	    	//连接MySQL
        	    	ed.initParam("C:\\Users\\computer\\eclipse-workspace\\geodemo\\src\\geodemo\\mysql.ini");
        	    	//插入数据库
        			ed.Insert(sql);
        		} catch (Exception e) {
        			e.printStackTrace();
        		} finally {

        		}
	}
	/**
	 * 根据存储规则生成存储路径
	 * @return
	 */
	private void genertateStoragePath() {
		//equalsIgnoreCase()忽略大小写
//		if("ZY301".equalsIgnoreCase(this.satellite)) {
//			if(!"DOM".equalsIgnoreCase(this.sensor)) {
//				this.storagePath = satellite + "\\" + productLevel + "\\" + photoDate + "\\" + 
//					String.format("%06d", orbitNum) + "\\" + sensor + "\\";
//			} else {
//				this.storagePath = satellite + "\\" + productLevel + "\\" + sensor + "\\";
//			}
//		}
		
		storagePath = "D:\\database\\test" + "\\" + destPath ;
	}
	
	/**
	 * 数据归档 
	 * @return
	 */
	private void fileArchive(java.io.File sourceFile) throws Exception {
		genertateStoragePath();
		//copyFileToDirectory()拷贝文件到指定目录文件
		FileUtils.copyFileToDirectory(sourceFile, new java.io.File(this.storagePath));
	}
	
	/**
	 * startTime endTime 相同敿24小时归档
	 */
//	private boolean isArchive(int startTime, int endTime, int hour) {
//		if (startTime < endTime) {
//			if (startTime <= hour && hour < endTime) {
//				return true;
//			} else {
//				return false;
//			}
//		} else if (startTime > endTime) {
//			if (endTime < hour && hour < startTime) {
//				return false;
//			} else {
//				return true;
//			}
//		} else {
//			return false;
//		}
//	}
	public static void main(String args[]) {
		Archiver m=new Archiver();
		m.run();
	}
}
	