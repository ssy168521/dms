package com.web.thread;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.sasmac.jni.ImageProduce;
import com.sasmac.meta.Meta2Database;
import com.web.common.Constants;
import com.web.common.THREADSTATUS;
import com.web.service.WebService;
import com.web.service.impl.WebServiceImpl;
import com.web.util.DbUtils;
import com.web.util.PropertiesUtil;
/**
 * 分景产品迁移归档
 * @author Administrator
 * @ClassName:SviCopyArchiveThread
 * @Version 版本
 * @ModifiedBy 修改人
 * @date 2018年3月29日 下午10:04:36
 */
public class SviCopyArchiveThread extends BaseThread implements Runnable{
	private Logger myLogger = LogManager.getLogger("mylog");
	private int iCurridx = 0;// 当前归档的充号
	private String archivePath;

	private WebService service = null;

	/** 数据在存储区的存储位置**/
	private String storagePath="";//也就是归档后文件路径
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
	public SviCopyArchiveThread(String strArchivedir) {
        //调用父类BaseThread的构造方法
		super("archive", THREADSTATUS.THREAD_STATUS_UNKNOWN.ordinal(), "数据归档",
				new Date(), null, 0, "");

		archivePath = strArchivedir;
		service = new WebServiceImpl();
		try {
			bIsSMBfile = Constants.AssertFileIsSMBFileDir(strArchivedir);//判断属于哪种归档形式
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SviCopyArchiveThread() {
	}

	@Override
	public void run() {   //多线程
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
				PrintTaskInfo(conn);   //将归档信息写入数据库
				FinishThread();        //结束归档线程
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
				filename = fF.getName().substring(0, fF.getName().lastIndexOf("."));
				myLogger.info("start archive " + Integer.toString(iCurridx)
						+ " file");
				int intNowHour = Calendar.getInstance().get(
						Calendar.HOUR_OF_DAY);

				String satellite = "";
				int flag = 0;
				//正则表达式比较  
				if (filename.substring(0,filename.indexOf("_")+1)   //
						.matches("GF1_")){
					flag = 1;
					satellite = "GF1";
				}else if(filename.substring(0,filename.indexOf("_")+1).  //
						matches("GF2_")){
					flag = 2;
					satellite = "GF2";
				}else if(filename.substring(0,filename.indexOf("_")+1).  //TH_006149_20160204_020842_19_M
						matches("TH_")){
					flag = 3;
					satellite = "TH";
				}else if(filename.substring(0,filename.indexOf("_")+1).  //
						matches("zy301a_")){
					flag = 4;
					satellite = "zy301a";
				}else if(filename.substring(0,filename.indexOf("_")+1).
						matches("zy302a_")){
					flag = 5;
					satellite = "zy302a";
				}else{
					myLogger.info("file format is not support: " + filename);
					continue;
				}
				//表名
				String tablename = "tb_domscene_product";      //所有标准分幅产品信息都存于此表
               // String productName = "分景产品";  //产品名字
				if (service.isFileArchive(conn, tablename, filename)) {
					myLogger.info("file had exist database:" + filename);
					continue;
				}
				if (isFinishCopy(fF.getAbsoluteFile()))
					
					fileArchive(fF);   //文件拷贝

					//geotiff文件全路径
					String tiffpath = myPath.getPath() + fF.separator
							+ fF.getName();
					if (tiffpath.substring(tiffpath.lastIndexOf(".")).equalsIgnoreCase(".tif")) {
						Meta2Database mdb = new Meta2Database();
						mdb.tif2Db(satellite,filename,tablename,tiffpath);
					} else {					
						myLogger.info("文件格式不对,继续！");
					}
				
					// tif重采样
					if (tiffpath.substring(tiffpath.lastIndexOf("."))
							.equalsIgnoreCase(".tif")) {

						// tif图像原路径
						String tifpath = myPath.getPath() + fF.separator
								+ fF.getName();
						String[] ss=filename.split("_");
						String StoragePath = ss[0]+"\\"+ss[1]+"\\"+ss[4];
						String path = Constants.class.getClassLoader()
								.getResource("/").toURI().getPath();
						PropertiesUtil propertiesUtil = new PropertiesUtil(path
								+ Constants.STR_CONF_PATH);
						// 快视图路径
						String OverviewStoragePath = propertiesUtil
								.getProperty("overviewfilepath");

						myLogger.info(" start ImageRectify overiew-png: "
								+ filename + ".png");
						ImageProduce imgprodu = new ImageProduce();
						boolean res = false;
						String destpath = OverviewStoragePath + StoragePath;

						// String destpath1 = OverviewStoragePath + StoragePath;
						File dest = new File(destpath);
						if (!dest.exists()) {
							dest.mkdirs();
						}
						// 图像重采样
						res = imgprodu.ImageRectify(tifpath, destpath
								+ File.separator + filename + ".png", 256, 256);
						if (!res) {
							myLogger.info(filename + " :png overview build error !");
						} else {
							myLogger.info("finish ImageRectify overiew-png: "
									+ filename + ".png");
						}
					}
				
				myLogger.info("finish archive "
						+ Integer.toString(iCurridx) + " file");

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
		/** 绛夊埌鏂囦欢鎷疯礉瀹屾垚鍐嶈繘琛屽睘鎬ц幏鍙栧強鍒ゆ柇绛夋搷浣� */
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

	/**
	 * 计算文件数量（本地归档）
	 * @param strPath
	 * @throws Exception
	 */
	private void calcFileCount(String strPath) throws Exception {
		if (bIsSMBfile == 0) {
			java.io.File myPath = new File(strPath);
			java.io.File[] aF = myPath.listFiles();
			for (java.io.File fF : aF) {
				if (fF.isDirectory()) {    //若是目录返回true
					calcFileCount(fF.getPath());
				} else {
					fileCount++;
				}
			}
		} else {
			myLogger.info("归档文件不存在！");
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
				document = reader.read(path+ Constants.STR_SVICOPY_PATH);
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
		String filename = fF.getName().substring(0, fF.getName().lastIndexOf("."));
		String[] ss=filename.split("_");
		storagePath = storagePath.substring(0, storagePath.lastIndexOf("\\"))+"\\"
		+ss[0]+"\\"+ss[1]+"\\"+ss[4];
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

}
