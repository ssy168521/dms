package com.web.thread;

import java.sql.Connection;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.web.common.Constants;
import com.web.form.ConfigureForm;
import com.web.service.WebService;
import com.web.service.impl.WebServiceImpl;
import com.web.util.DbUtils;
import com.web.util.TarUtils;

/**
 * 数据归档线程
 * @author Administrator
 *
 */
public class MyArchiveThread extends MyBaseThread implements Runnable {
	private String satellite;
	private String sensor;
	private String productLevel;
	private String photoDate;
	private int orbitNum;
	private int dataId;
	
	private int start;
	private int end;
	
	/** 数据在生产区的存储路弿**/
	private String archivePath;

	/** 数据存储规则 **/
	private String storageRule;
	
	/** 数据校验规则 **/
	private String[] checkRule;
	
	/** 数据在存储区的存储位绿**/
	private String storagePath;
	
	private String destPath;
	
	/** 记录磁盘文件的后线程**/
	private StringBuilder allFilesSuffix;
	
	/** windows环境下xml文件名称 **/
	private String windowsXMLFile;
	
	private WebService service = new WebServiceImpl();
	
	private Connection conn    = null;
	
	private java.util.List<String> fileList = new java.util.ArrayList<String>();
	com.web.form.DataInfo dataInfo = null;
	
	/**
	 * 构耥½数,传耧³»统配置信息
	 * @param conf
	 */
	public MyArchiveThread(ConfigureForm conf) {
		this.satellite   = conf.getSatellite();
		this.archivePath = conf.getArchivePath();
		this.storageRule = conf.getStorageRule();
		this.checkRule   = conf.getCheckRule().split(",");
		this.destPath	 = conf.getDestPath();
		this.start		 = conf.getArchiveStart();
		this.end 		 = conf.getArchiveEnd();
		
		system = satellite;
	}
	
	public MyArchiveThread() {
	}

	@Override
	public void run() {
		Constants.WriteLog("庿§扫描路径:" + archivePath);
		while(true) {
			try {
		/*		if(Constants.getThreadStatus(satellite) == Constants.THREAD_NO_ACTIVE) {//当前卫星归档线程是否启用
					Constants.WriteLog("*********  System config has bean deleted ****************");
					break;
				}
		*/		
				conn = DbUtils.getConnection(true);
				
				java.io.File rootPath = new java.io.File(archivePath);
				if(!rootPath.exists() || rootPath.isFile()) {
					Constants.WriteLog("该路径不存在");
					return;
				}
				
				ergodicDir(rootPath);
				
				
				Thread.sleep(1000 * 60);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DbUtils.closeQuietlyConnection(conn);
			}
		}
	}

	/**
	 * 遍历归档路径
	 * @param myPath
	 * @throws Exception
	 */
	private void ergodicDir(java.io.File myPath) throws Exception {
		java.io.File[] aF = myPath.listFiles();
		for(java.io.File fF : aF) {
			if(fF.isDirectory()) {
				ergodicDir(fF.getAbsoluteFile());
			} else {
				int intNowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				// 判断当前时间是否为归档时长
				if (!isArchive(start, end, intNowHour)) {
					return;
				}
				
				if(!service.isFileArchive(conn, fF.getAbsolutePath(), fF.length())) {//判断文件是否归档轿
					Constants.WriteLog("File: " + fF.getAbsolutePath());
					if(isFinishCopy(fF.getAbsoluteFile())) {//判断文件是否被占璿
						if(isFinishCompress(fF.getAbsoluteFile())) {//判断文件是否完整
							
							for(String fileName : fileList) {
								/*
								 * 文件拷贝
								 */
								fileArchive(new java.io.File(fileName));
								Constants.WriteLog("    Archive file success.");
								
								dataInfo.setArchiveFile(fileName);
								storagePath = storagePath + fileName.substring(fileName.lastIndexOf("\\") + 1);
								dataInfo.setStorageFile(storagePath);
								
								if(service.insertProduct(conn, dataInfo)) {
									service.updateStaticts(conn, satellite, 1);
									Constants.WriteLog("    Insert file success.");
								} else {
									service.updateStaticts(conn, satellite, 0);
									Constants.WriteLog("    Insert file failed.");
								}
							}
						}
					} else {
						Constants.WriteLog("    This is occupied,read next file.");
					}
				}
			}
		}
	}
	
	/**
	 * 判断文件是否拷贝完成
	 * @param fileName
	 * @return
	 */
	private boolean isFinishCopy(java.io.File fileName) {
		/** 等到文件拷贝完成再进行属性获取及判断等操仿*/
		boolean bFlag = false;
		java.io.RandomAccessFile raf = null;
//		while(!bFlag) {
//			try {
//				raf = new java.io.RandomAccessFile(fileName,"rw");
//				raf.seek(fileName.length());
//			} catch (Exception e) {
//				try {
//					Thread.sleep(100*5);
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//				continue;
//			}
//			
//			bFlag = true;
//		}
		try {
			raf = new java.io.RandomAccessFile(fileName,"rw");
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
	 * 判断文件是否打包完整
	 * <p>判断方法:解析xml文件中的filename信息,判定相同路径下所有的filename是否存在</p>
	 */
	private boolean isFinishCompress(java.io.File fileName) {
		boolean bFlag = false;
		try {
			if(fileName.getName().toLowerCase().endsWith("tar")) {//如果文件为tar势解析出其中的xml文件
				/*
				 * 解析xml文件名称
				 */
				String xmlName = fileName.getName();
				windowsXMLFile = xmlName.replace("tar", "xml");
				
				if(Constants.IS_LINUX) {
					String[] cmd = {"/bin/sh", "-c", "tar tvf " + fileName.getAbsolutePath()};

					java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
					Process p = runtime.exec(cmd);
					//p.wait(1000);

					java.io.InputStream is = p.getInputStream();

					java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(is));
					String line = null;
					
					while ((line = br.readLine()) != null) {
						System.out.println(line);
						if (line.contains("."))
						{
							allFilesSuffix.append(line.substring(line.lastIndexOf(".") + 1)).append(",");
						}
					}
					br.close();
					is.close();
					
					String myFileCheck = StringUtils.removeEnd(allFilesSuffix.toString(), ",");
					
					for(String rule : checkRule) {
						if(!myFileCheck.contains(rule))
							return false;
					}
					
					return true;
				} else {
					/*
					 * 组织解压后目标路径
					 */
					String destPath = "e:\\temp\\";//fileName.getAbsolutePath();
					//destPath = destPath.substring(0, destPath.lastIndexOf("\\") + 1) + "temp";
					java.io.File fDest = new java.io.File(destPath);
					if(!fDest.exists())
						fDest.mkdir();
					
					/*
					 * windows环境先将tar包解压缩
					 */
					windowsXMLFile = TarUtils.dearchive(fileName, fDest, windowsXMLFile);
					Constants.WriteLog("    XML file: " + windowsXMLFile);
					
					/*
					 * 获取xml文件中的内容
					 */
					getMetaDataFromXML(new java.io.File(windowsXMLFile));
					Constants.WriteLog("    Analyze xml file success.");
					
					fileList.clear();
					
					/*
					 * 生成数据信息对象
					 */
					dataInfo = new com.web.form.DataInfo();
//					dataInfo.setCloudPercent(CloudPercent);
					dataInfo.setOrbitID(orbitNum);
					dataInfo.setProductLevel(productLevel);
//					dataInfo.setScenePath(ScenePath);
//					dataInfo.setSceneRow(SceneRow);
//					dataInfo.setProductQuality(ProductQuality);
					dataInfo.setSatellite(satellite);
					dataInfo.setSensor(sensor);
					dataInfo.setFileSize(fileName.length());
					dataInfo.setDataId(dataId);
					
					fileList.add(fileName.getAbsolutePath());
					
					/*
					 * 校验tar包完整位
					 */
//					if(windowsCheck(destPath)) {
//						System.out.println("文件校验完成.");
//						
//						getMetaDataFromXML(new java.io.File(windowsXMLFile));
//					}
					
					bFlag = true;
				}
			} else if(fileName.getName().toLowerCase().endsWith("xml")) {//如果不是tar包则判断是否为xml文件
				getMetaDataFromXML(new java.io.File(windowsXMLFile));
				bFlag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return bFlag;
	}
	
	/**
	 * windows系统数据完整性校騿只根据后线称校验文件是否完擿
	 * @param path
	 * @return
	 */
	private boolean windowsCheck(String path) {
		allFilesSuffix = new StringBuilder();
		
		getAllFiles(new java.io.File(path));
		
		String myFileCheck = StringUtils.removeEnd(allFilesSuffix.toString(), ",");
		
		for(String rule : checkRule) {
			if(!myFileCheck.contains(rule))
				return false;
		}
		
		return true;
	}
	
	/**
	 * 遍历并记录所有文件的后缀名称
	 * @param path
	 */
	private void getAllFiles(java.io.File path) {
		java.io.File[] aF = path.listFiles();
		for(java.io.File fF : aF) {
			if(fF.isDirectory()) {
				getAllFiles(fF.getAbsoluteFile());
			} else {
				String fileName = fF.getAbsolutePath().substring(fF.getAbsolutePath().lastIndexOf("\\") + 1);
				if(fileName.equalsIgnoreCase(windowsXMLFile)) {
					windowsXMLFile = fF.getAbsolutePath();
				}
				allFilesSuffix.append(fF.getAbsolutePath().substring(fF.getAbsolutePath().lastIndexOf(".") + 1)).append(",");
			}
		}
	}
	
	/**
	 * 解析xml文件获取元数据信忿
	 * @param xmlFile
	 * @throws Exception
	 */
	private void getMetaDataFromXML(java.io.File xmlFile) throws Exception {
		org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
		org.dom4j.Document doc = reader.read(xmlFile);
		
		org.dom4j.Node node = null;
		orbitNum  = Integer.parseInt(doc.selectSingleNode("//sensor_corrected_metadata/productInfo/OrbitID").getText());
		node = doc.selectSingleNode("//sensor_corrected_metadata/productInfo/ProductLevel");
		if(node != null)
			productLevel = node.getText();
		sensor = doc.selectSingleNode("//sensor_corrected_metadata/productInfo/SensorID").getText();
		
		node = doc.selectSingleNode("//sensor_corrected_metadata/processInfo/ProduceID");
		if(node != null)
			dataId = Integer.parseInt(node.getText());
		
//		int ScenePath = Integer.parseInt(doc.selectSingleNode("//sensor_corrected_metadata/productInfo/ScenePath").getText());
//		int SceneRow  = Integer.parseInt(doc.selectSingleNode("//sensor_corrected_metadata/productInfo/SceneRow").getText());
		
//		int CloudPercent = Integer.parseInt(doc.selectSingleNode("//sensor_corrected_metadata/productInfo/CloudPercent").getText());
//		int ProductQuality = 0;
//		node = doc.selectSingleNode("//sensor_corrected_metadata/ProductQualityInfo/RadiometricCheck/ProductQuality");
//		if(node == null)
//			node = doc.selectSingleNode("//sensor_corrected_metadata/productInfo/ProductQuality");
//		if(node != null)
//			ProductQuality = Integer.parseInt(node.getText());
		
//		System.out.println("productId = " + productId);
//		java.util.List<org.dom4j.Node> list = doc.selectNodes("sensor_corrected_metadata//Filename");
//		for(org.dom4j.Node n : list) {
//			fileList.add(n.getText());
//		}
	}
	
	/**
	 * 根据存储规则生成存储路径
	 * @return
	 */
	private void genertateStoragePath() {
		if("ZY301".equalsIgnoreCase(this.satellite)) {
			if(!"DOM".equalsIgnoreCase(this.sensor)) {
				this.storagePath = satellite + "\\" + productLevel + "\\" + photoDate + "\\" + 
					String.format("%06d", orbitNum) + "\\" + sensor + "\\";
			} else {
				this.storagePath = satellite + "\\" + productLevel + "\\" + sensor + "\\";
			}
		}
		
		this.storagePath = this.destPath + "\\" + this.storagePath;
	}
	
	/**
	 * 数据归档
	 * @return
	 */
	private void fileArchive(java.io.File sourceFile) throws Exception {
		genertateStoragePath();
		
		FileUtils.copyFileToDirectory(sourceFile, new java.io.File(this.storagePath));
	}
	
	/**
	 * startTime 䶿endTime 相同敿24小时归档
	 */
	private boolean isArchive(int startTime, int endTime, int hour) {
		if (startTime < endTime) {
			if (startTime <= hour && hour < endTime) {
				return true;
			} else {
				return false;
			}
		} else if (startTime > endTime) {
			if (endTime < hour && hour < startTime) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	public static void main(String args[]) {
		ConfigureForm conf = new ConfigureForm();
		conf.setSatellite("zy301");
		conf.setArchivePath("E:\\SRC");
		conf.setCheckRule("jpg,xml,shp,tif,txt");
		
		MyArchiveThread m = new MyArchiveThread(conf);
		try {
		//	Constants.setThreadStatus("zy301", Constants.THREAD_STATUS_RUNNING);
			new Thread(m).start();
			//m.ergodicDir(new java.io.File("E:\\SRC"));//.isFinishCompress(new java.io.File("E:\\SRC\\zy301a_bwd_026084_026105_20160919112839_01_sec_0001_1609208009.tar"));
			//new Thread(m).start();
//			m.getMetaDataFromXML(new java.io.File("E:\\工作\\测绘寿\史绍雨\\ZY3_01a_mynnavp_890153_20120725_110058_0007_SASMAC_CHN_sec_rel_001_1208063670.xml"));
			//m.getMetaDataFromXML(new java.io.File("E:\\工作\\测绘寿\史绍雨\\zy302a_nad_000944_006157_20160731111715_01_sec_0001_1609284788.xml"));
//			org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
//			org.dom4j.Document doc = reader.read(new java.io.File("E:\\工作\\测绘寿\史绍雨\\ZY3_01a_mynnavp_890153_20120725_110058_0007_SASMAC_CHN_sec_rel_001_1208063670.xml"));
//			java.util.List<org.dom4j.Node> list = doc.selectNodes("sensor_corrected_metadata//path", "sensor_corrected_metadata//filename");
//			for(org.dom4j.Node node : list) {
//				System.out.println(node.getText());
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}