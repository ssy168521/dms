package com.web.thread;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.sasmac.database.ImportMeta2Database;
import com.sasmac.meta.Meta2Database;
import com.sasmac.util.MetaTableUtil;
import com.sasmac.util.smbAuthUtil;
import com.web.common.Constants;
import com.web.common.THREADSTATUS;
import com.web.service.WebService;
import com.web.service.impl.WebServiceImpl;
import com.web.util.AppUtil;
import com.web.util.DbUtils;
/**
 * 标准分幅产品迁移归档
 * @author Administrator
 * @ClassName:SfiCopyArchiveThread
 * @Version 版本
 * @ModifiedBy 修改人
 * @date 2018年3月23日 下午2:54:00
 */
public class SfiCopyArchiveThread extends BaseThread implements Runnable {
	private Logger myLogger = LogManager.getLogger("mylog");
	private int iCurridx = 0;// 当前归档的充号
	private String archivePath;
	/** 记录磁盘文件的后线程**/
	private StringBuilder allFilesSuffix;

	/** windows环境下xml文件名称 **/
	private String windowsXMLFile;

	private WebService service = null;

	/** 数据在存储区的存储位置**/
	private String storagePath="";//也就是归档后文件路径
	/** 目标路径 **/
//	private String destPath;
	
	private Connection conn = null;
	/**文件数量 **/
	private int fileCount = 0;
	int bIsSMBfile;;
	int archivemode;

	/**
	 * 归档线程
	 * 
	 * @param conf
	 */
	public SfiCopyArchiveThread(String strArchivedir) {
		//调用父类BaseThread的构造方法
		super("archive", THREADSTATUS.THREAD_STATUS_UNKNOWN.ordinal(), "数据归档",
				new Date(), null, 0, "");

		archivePath = strArchivedir;
		service = new WebServiceImpl();
		try {
			bIsSMBfile = Constants.AssertFileIsSMBFileDir(strArchivedir);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SfiCopyArchiveThread() {
	}

	@Override
	public void run() { //多线程
		setTaskStartTime(new Date());

		myLogger.info("start archive:" + archivePath);
		try {
			conn = DbUtils.getConnection(true);
			if (bIsSMBfile == 0) {
				java.io.File rootPath = new java.io.File(archivePath);
				if (!rootPath.exists() || rootPath.isFile()) {
					myLogger.info("归档路径不存在！");
					return;
				}
				calcFileCount(archivePath);

				myLogger.info("All archive files count: "
						+ Integer.toString(fileCount));

				ergodicDir(rootPath);
				
			} else {
				myLogger.info("请检查是否是本地归档！");
			}

			if (!bStopThread) {
				myLogger.info("finish archive " + archivePath);
				setTaskStatus(THREADSTATUS.THREAD_STATUS_FINISHED.ordinal());
				setTaskEndTime(new Date());
				setTaskMarkinfo(archivePath + " " + Integer.toString(fileCount)
						+ " files archive");
				setTaskProgress(100);
				PrintTaskInfo(conn);
				FinishThread();
			} else {
				myLogger.info(archivePath + " :archive task is stoped");
				setTaskStatus(THREADSTATUS.THREAD_STATUS_STOPED.ordinal());
				setTaskEndTime(new Date());
				setTaskMarkinfo(archivePath + ": archive task is stoped");
				PrintTaskInfo(conn);
				StopThread();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietlyConnection(conn);
		}

	}
	
	/**
	 * 本机归档 
	 * @param myPath
	 * @throws Exception
	 */
	private void ergodicDir(java.io.File myPath) throws Exception{

		setTaskStatus(THREADSTATUS.THREAD_STATUS_RUNNING.ordinal());
		java.io.File[] aF = myPath.listFiles();
		String filename = "";
		for (java.io.File fF : aF) {

			if (fF.isDirectory()) {
				ergodicDir(fF.getAbsoluteFile());
			} else {
				iCurridx++;
				if (bStopThread) {
					setTaskStatus(THREADSTATUS.THREAD_STATUS_STOPED.ordinal());
					setTaskEndTime(new Date());
					myLogger.info(" archive  task is Stoped");
					int iProgress = (int) ((double) iCurridx
							/ (double) fileCount * 100);
					setTaskProgress(iProgress);
					break;// 如果中断线程
				}
                 //无后缀名的文件名
				filename = fF.getName().substring(0, fF.getName().indexOf("."));
				myLogger.info("start archive " + Integer.toString(iCurridx)
						+ " file");
				int intNowHour = Calendar.getInstance().get(
						Calendar.HOUR_OF_DAY);

				String satellite = "";
				String productLevel = "";
				int flag = 0;
				//正则表达式比较  J46D001001
				boolean bmatch = filename
						.matches("J[0-9]{2}D[0-9]{3}[0-9]{3}"); //D代表1：10万分幅比例尺
				if (bmatch) {
					flag = 1;
					satellite = "ZY3-1";
					productLevel = "SC";

				}else {
					myLogger.info("file format is not support: " + filename);
					continue;
				}
				//表名
				String tablename = "tb_domframe_product";      //所有标准分幅产品信息都存于此表
                String productName = "标准分幅产品";  //产品名字
				if (service.isFileArchive(conn, tablename, filename)) {
					myLogger.info("file had exist database:" + filename);
					continue;
				}
				if (isFinishCopy(fF.getAbsoluteFile()))					
					//continue;
				   fileArchive(fF);   //文件拷贝
					//xml文件全路径
					String xmlName = myPath.getPath() + fF.separator
							+ fF.getName();
					if (xmlName.substring(xmlName.lastIndexOf(".")-3).equalsIgnoreCase("tif.xml")) {
						Meta2Database mdb = new Meta2Database();
						mdb.xml2Db(filename,productName,tablename,xmlName);
					} else {					
						myLogger.info("xml文件格式不对,继续！");
					}
				
				myLogger.info("finish archive "
						+ Integer.toString(iCurridx + 1) + " file");

				int iProgress = (int) ((double) iCurridx / (double) fileCount * 100);
				setTaskProgress(iProgress);

			}
		}
	}
	/**
	 * 文件是否拷贝中 
	 * @param fileName
	 * @return
	 */
	private boolean isFinishCopy(java.io.File fileName) {
		boolean bFlag = false;
		java.io.RandomAccessFile raf = null;
		try {
			raf = new java.io.RandomAccessFile(fileName, "rw");
			raf.seek(fileName.length());
			bFlag = true;
		} catch (Exception e) {
			System.out.println("");
			myLogger.error(e);
		} finally {
			try {
				raf.close();
			} catch (Exception e) {
				e.printStackTrace();
				myLogger.error(e);
			}
		}

		return bFlag;
	}
	//数据校验规则
	private boolean CheckValid(java.io.File fileName) {

		boolean bFlag = false;
		try {
			if (fileName.getName().toLowerCase().endsWith(".tar")
					|| fileName.getName().toLowerCase().endsWith(".tar.gz")) {// 濡傛灉鏂囦欢涓簍ar鍖�瑙ｆ瀽鍑哄叾涓殑xml鏂囦欢
				/*
				 * 瑙ｆ瀽xml鏂囦欢鍚嶇О
				 */
				String xmlName = fileName.getName();
				windowsXMLFile = xmlName.replace("tar", "xml");

				if (Constants.IS_LINUX) {
					String[] cmd = { "/bin/sh", "-c",
							"tar tvf " + fileName.getAbsolutePath() };

					java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
					Process p = runtime.exec(cmd);
					// p.wait(1000);

					java.io.InputStream is = p.getInputStream();

					java.io.BufferedReader br = new java.io.BufferedReader(
							new java.io.InputStreamReader(is));
					String line = null;

					while ((line = br.readLine()) != null) {

						if (line.contains(".")) {
							allFilesSuffix.append(
									line.substring(line.lastIndexOf(".") + 1))
									.append(",");
						}
					}
					br.close();
					is.close();

					String myFileCheck = StringUtils.removeEnd(
							allFilesSuffix.toString(), ",");

					/*
					 * for(String rule : checkRule) {
					 * if(!myFileCheck.contains(rule)) return false; }
					 */
					bFlag = true;
					return bFlag;
				} else {
					myLogger.info("  no implements windows tar check valid .");
					bFlag = true;
				}
			} else if (fileName.getName().toLowerCase().endsWith("xml")) {// 濡傛灉涓嶆槸tar鍖呭垯鍒ゆ柇鏄惁涓簒ml鏂囦欢
																			// bFlag
																			// =windowsCheck(fileName.getPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			myLogger.error(e);
			return false;
		}
		return bFlag;

	}

	/**
	 * 閬嶅巻骞惰褰曟墍鏈夋枃浠剁殑鍚庣紑鍚嶇О
	 * 
	 * @param path
	 */
	private void getAllFiles(java.io.File path) {
		java.io.File[] aF = path.listFiles();
		for (java.io.File fF : aF) {
			if (fF.isDirectory()) {
				getAllFiles(fF.getAbsoluteFile());
			} else {
				String fileName = fF.getAbsolutePath().substring(
						fF.getAbsolutePath().lastIndexOf("\\") + 1);
				if (fileName.equalsIgnoreCase(windowsXMLFile)) {
					windowsXMLFile = fF.getAbsolutePath();
				}
				allFilesSuffix.append(
						fF.getAbsolutePath().substring(
								fF.getAbsolutePath().lastIndexOf(".") + 1))
						.append(",");
			}
		}
	}

	/**
	 * 文件数量
	 * @param strPath
	 * @throws Exception
	 */
	private void calcFileCount(String strPath) throws Exception {
		if (bIsSMBfile == 0) {
			java.io.File myPath = new File(strPath);
			java.io.File[] aF = myPath.listFiles();
			for (java.io.File fF : aF) {
				if (fF.isDirectory()) {
					calcFileCount(fF.getPath());
				} else {
					fileCount++;
				}
			}
		} else {
			String SmbFilepath = strPath;

			NtlmPasswordAuthentication auth = smbAuthUtil
					.getsmbAuth(SmbFilepath);
			if (auth == null) {
				Constants.WriteLog(" smb:user password auth error! ");
				return;
			}
			if (bIsSMBfile == 1)
				SmbFilepath = AppUtil.localFilePathToSMBFilePath(strPath);

			SmbFile smbf = new SmbFile(SmbFilepath, auth);
			if (!smbf.exists()) {
				System.out.print(SmbFilepath);
			}
			if (smbf == null)
				return;
			SmbFile[] SmbFilelist = smbf.listFiles();
			for (int i = 0; i < SmbFilelist.length; i++) {
				if (SmbFilelist[i].isDirectory()) {
					calcFileCount(SmbFilelist[i].getPath());
				} else {
					fileCount++;
				}
			}

		}
	}
	/**
	 * 根据存储规则生成存储路径
	 * @return
	 */
	private void genertateStoragePath(java.io.File fF) {
		//获取Constants类路径 E:\MyEclipse2014 workspace\zy3dms\src
		String path=null;
		try {
			path = Constants.class.getClassLoader().getResource("/")
					.toURI().getPath();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SAXReader reader = new SAXReader();
		Document document = null;
			try {
				document = reader.read(path+ Constants.STR_SFICOPY_PATH);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//读取conf文件下存储路径
	
		//xml设置的归档路径最多7个层级，不到7层级的不填。
		Node node01 = document.selectSingleNode("/StoragePath/diskname");
		String diskname = node01.getText();
		File f=new File(diskname+"\\");
		if(!f.exists()){
			myLogger.info("磁盘不存在，请在StoragePath.xml配置新盘符！");
		}
		Node node02 = document.selectSingleNode("/StoragePath/Levelone");		
		String Levelone = node02.getText();
		Node node03 = document.selectSingleNode("/StoragePath/Leveltwo");		
		String Leveltwo = node03.getText();
		Node node04 = document.selectSingleNode("/StoragePath/Levelthree");		
		String Levelthree = node04.getText();
		Node node05 = document.selectSingleNode("/StoragePath/Levelfour");		
		String Levelfour = node05.getText();
		Node node06 = document.selectSingleNode("/StoragePath/Levelfive");		
		String Levelfive = node06.getText();
		Node node07 = document.selectSingleNode("/StoragePath/Levelsix");		
		String Levelsix = node07.getText();
		String[] Path = {diskname,Levelone,Leveltwo,Levelthree,Levelfour,Levelfive,Levelsix};
		storagePath="";
		for(String s:Path){
			if(s!=null && s.trim()!=""){
				storagePath = storagePath+s+"\\";
			}
		}
		String filename = fF.getName().substring(0, fF.getName().indexOf("."));
		storagePath = storagePath.substring(0, storagePath.lastIndexOf("\\"))+"\\"
		+filename.substring(0,4)+"\\"+filename.substring(4,7)+"\\"+filename.substring(7,10);
		myLogger.info("归档路径："+storagePath);
		//storagePath = "E:\\database\\标准分幅" + "\\" + "J46" ;
	}
	
	/**
	 * 数据归档 
	 * @return
	 */
	private void fileArchive(java.io.File sourceFile) throws Exception {
		genertateStoragePath(sourceFile);
		//copyFileToDirectory()拷贝文件到指定目录文件
		FileUtils.copyFileToDirectory(sourceFile, new java.io.File(this.storagePath));
	}
	/**
	 * startTime 涓�endTime 鐩稿悓鏃�24灏忔椂褰掓。
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

	}
}