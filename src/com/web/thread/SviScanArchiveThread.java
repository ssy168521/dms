package com.web.thread;

import java.io.File;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sasmac.meta.Meta2Database;
import com.web.common.Constants;
import com.web.common.THREADSTATUS;
import com.web.service.WebService;
import com.web.service.impl.WebServiceImpl;
import com.web.util.DbUtils;
/**
 * 分景产品扫描归档
 * @author Administrator
 * @ClassName:SviScanArchiveThread
 * @Version 版本
 * @ModifiedBy 修改人
 * @date 2018年3月29日 下午3:14:14
 */
public class SviScanArchiveThread extends BaseThread implements Runnable {
	private Logger myLogger = LogManager.getLogger("mylog");
	private int iCurridx = 0;// 当前归档的充号
	private String archivePath;

	private WebService service = null;

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
	public SviScanArchiveThread(String strArchivedir) {
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

	public SviScanArchiveThread() {
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
				filename = fF.getName().substring(0, fF.getName().indexOf("."));
				myLogger.info("start archive " + Integer.toString(iCurridx)
						+ " file");
				int intNowHour = Calendar.getInstance().get(
						Calendar.HOUR_OF_DAY);

				String satellite = "";
				int flag = 0;
				//正则表达式比较  
				if (filename   //GF1_E1089N274_20160517_016482_19_M
						.matches("GF1_E[0-9]{4}N[0-9]{3}_[0-9]{8}_[0-9]{6}_[0-9]{2}_M")) {
					flag = 1;
					satellite = "GF1";
				}else if(filename.  //GF2_E1100N381_20160412_008910_19_M
						matches("GF2_E[0-9]{4}N[0-9]{3}_[0-9]{8}_[0-9]{6}_[0-9]{2}_M")){
					flag = 2;
					satellite = "GF2";
				}else if(filename.  //TH_006149_20160204_020842_19_M
						matches("TH_[0-9]{6}_[0-9]{8}_[0-9]{6}_[0-9]{2}_M")){
					flag = 3;
					satellite = "TH";
				}else if(filename.  //ZY3_010149_20160207_022661_19_M
						matches("ZY3_[0-9]{6}_[0-9]{8}_[0-9]{6}_[0-9]{2}_M")){
					flag = 4;
					satellite = "ZY3";
				}else {
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
				if (!isFinishCopy(fF.getAbsoluteFile()))
					continue;

					//geotiff文件全路径
					String tiffpath = myPath.getPath() + fF.separator
							+ fF.getName();
					if (tiffpath.substring(tiffpath.lastIndexOf(".")).equalsIgnoreCase(".tif")) {
						Meta2Database mdb = new Meta2Database();
						mdb.tif2Db(satellite,filename,tablename,tiffpath);
					} else {					
						myLogger.info("文件格式不对,继续！");
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

}
