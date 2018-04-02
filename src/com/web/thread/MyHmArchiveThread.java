package com.web.thread;

import java.io.File;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sasmac.database.ImportMeta2Database;
import com.sasmac.util.MetaTableUtil;
import com.sasmac.util.smbAuthUtil;
import com.web.common.Constants;
import com.web.common.THREADSTATUS;
import com.web.service.WebService;
import com.web.service.impl.WebServiceImpl;
import com.web.util.AppUtil;
import com.web.util.DbUtils;
/**
 * 多线程归档
 * @author Administrator
 * @ClassName:MyHmArchiveThread
 * @date 2018年3月13日 上午10:05:28
 */
public class MyHmArchiveThread extends BaseThread implements Runnable {
	private Logger myLogger = LogManager.getLogger("mylog");
	private int iCurridx = 0;// 当前归档的充号
	private String archivePath;
	/** 记录磁盘文件的后线程**/
	private StringBuilder allFilesSuffix;

	/** windows环境下xml文件名称 **/
	private String windowsXMLFile;

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
	public MyHmArchiveThread(String strArchivedir) {

		super("archive", THREADSTATUS.THREAD_STATUS_UNKNOWN.ordinal(), "数据归档",
				new Date(), null, 0, "");

		archivePath = strArchivedir;
		service = new WebServiceImpl();
		try {
			bIsSMBfile = Constants.AssertFileIsSMBFileDir(strArchivedir);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public MyHmArchiveThread() {
	}

	@Override
	public void run() {
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

				calcFileCount(archivePath);
				myLogger.info("All archive files count: "
						+ Integer.toString(fileCount));

				ergodicDir(archivePath);

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
	 * 归档
	 * @param myPath
	 * @throws Exception
	 */
	private void ergodicDir(String FilePath) throws Exception {

		String SMBFilePath = FilePath;
		setTaskStatus(THREADSTATUS.THREAD_STATUS_RUNNING.ordinal());
		int flag1 = Constants.AssertFileIsSMBFileDir(FilePath);
		if (flag1 == 1) {
			SMBFilePath = AppUtil.localFilePathToSMBFilePath(FilePath);
		} else if (flag1 == 0) {
			return;
		}
		NtlmPasswordAuthentication auth = smbAuthUtil.getsmbAuth(SMBFilePath);
		if (auth == null) {
			myLogger.error(" smb:user password auth error! ");
			return;
		}
		SmbFile smbfile = new SmbFile(SMBFilePath, auth);

		SmbFile[] aF = smbfile.listFiles();
		for (SmbFile fF : aF) {
			if (fF.isDirectory()) {
				ergodicDir(fF.getPath());
			} else {
				if (bStopThread) {
					setTaskStatus(THREADSTATUS.THREAD_STATUS_STOPED.ordinal());
					setTaskEndTime(new Date());
					myLogger.info(" archive  task is Stoped");
					int iProgress = (int) ((double) iCurridx
							/ (double) fileCount * 100);
					setTaskProgress(iProgress);
					break;// 如果中断线程
				}
				myLogger.info("start archive " + Integer.toString(iCurridx + 1)
						+ " file");
				int intNowHour = Calendar.getInstance().get(
						Calendar.HOUR_OF_DAY);
				String satellite = "";
				String productLevel = "";
				int flag = 0;

				String filename = fF.getName();
				boolean bmatch = filename
						.matches("zy301a_[a-z]{3}_[0-9]{6}_[0-9]{6}_[0-9]{14}_[0-9]{2}_sec_[0-9]{4}_[0-9]{10}.tar");
				if (bmatch) {
					flag = 1;
					satellite = "ZY3-1";
					productLevel = "SC";
					int idx = filename.lastIndexOf(".tar");
					filename = filename.substring(0, idx);
				} else if (filename
						.matches("zy302a_[a-z]{3}_[0-9]{6}_[0-9]{6}_[0-9]{14}_[0-9]{2}_sec_[0-9]{4}_[0-9]{10}.tar")) {
					flag = 2;
					satellite = "ZY3-2";
					productLevel = "SC";
					int idx = filename.lastIndexOf(".tar");
					filename = filename.substring(0, idx);
				} else if (filename.startsWith("GF1_PMS")) {
					flag = 3;
					satellite = "GF1";
					productLevel = "LEVEL1A";
					int idx = filename.lastIndexOf(".tar.gz");
					filename = filename.substring(0, idx);
				} else if (filename.startsWith("GF2_PMS")) {
					flag = 4;
					satellite = "GF2";
					productLevel = "LEVEL1A";
					int idx = filename.lastIndexOf(".tar.gz");
					filename = filename.substring(0, idx);
				} else {
					myLogger.info("file format is not support: " + filename);
					continue;
				}
				String tablename = MetaTableUtil.GetTableName(conn, satellite,
						productLevel);

				if (service.isFileArchive(conn, tablename, filename)) {
					myLogger.info("file had exist database:" + filename);
					continue;
				}
				// if(!isFinishCopy(fF.getAbsoluteFile())) return;
				// //鍒ゆ柇鏂囦欢鏄惁琚崰鐢�
				// if(!CheckValid(fF.getAbsoluteFile())) return ;//鍒ゆ柇鏂囦欢鏄惁瀹屾暣

				if (fF.getName().toLowerCase().endsWith(".tar")
						|| fF.getName().toLowerCase().endsWith(".tar.gz")) {

					String productfileName = fF.getParent() + fF.getName();
					if (Constants.IS_LINUX) {
						myLogger.info("LINUX environment parse is not implement XML file: "
								+ windowsXMLFile);
					} else {
						ImportMeta2Database pImp = new ImportMeta2Database(
								productfileName, conn);
						boolean b = pImp.Import2Database();

						if (b)
							myLogger.info("Insert database success."
									+ productfileName);
						else
							myLogger.info("Insert database failure."
									+ productfileName);
					}
				} else {

				}
				myLogger.info("finish archive "
						+ Integer.toString(iCurridx + 1) + " file");
				iCurridx++;
				int iProgress = (int) ((double) iCurridx / (double) fileCount * 100);
				setTaskProgress(iProgress);

			}
		}
	}

	/**
	 * 归档
	 * 
	 * @param myPath
	 * @throws Exception
	 */
	private void ergodicDir(java.io.File myPath) throws Exception {

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
                 //文件名
				filename = fF.getName();
				myLogger.info("start archive " + Integer.toString(iCurridx)
						+ " file");
				int intNowHour = Calendar.getInstance().get(
						Calendar.HOUR_OF_DAY);

				String satellite = "";
				String productLevel = "";
				int flag = 0;
				//正则表达式比较
				boolean bmatch = filename
						.matches("zy301a_[a-z]{3}_[0-9]{6}_[0-9]{6}_[0-9]{14}_[0-9]{2}_sec_[0-9]{4}_[0-9]{10}.tar");
				if (bmatch) {
					flag = 1;
					satellite = "ZY3-1";
					productLevel = "SC";

					int idx = filename.lastIndexOf(".tar");
					if (idx == -1)
						continue;
					filename = filename.substring(0, idx);
				} else if (filename
						.matches("zy302a_[a-z]{3}_[0-9]{6}_[0-9]{6}_[0-9]{14}_[0-9]{2}_sec_[0-9]{4}_[0-9]{10}.tar")) {
					flag = 2;
					int idx = filename.lastIndexOf(".tar");
					if (idx == -1)
						continue;
					satellite = "ZY3-2";
					productLevel = "SC";
					filename = filename.substring(0, idx);
				} else if (filename.startsWith("GF1_PMS")) {
					flag = 3;
					int idx = filename.lastIndexOf(".tar.gz");
					if (idx == -1)
						continue;
					filename = filename.substring(0, idx);
					satellite = "GF1";
					productLevel = "LEVEL1A";
				} else if (filename.startsWith("GF2_PMS")) {
					flag = 4;
					int idx = filename.lastIndexOf(".tar.gz");
					if (idx == -1)
						continue;
					satellite = "GF2";
					productLevel = "LEVEL1A";
					filename = filename.substring(0, idx);
				} else {
					myLogger.info("file format is not support: " + filename);
					continue;
				}
				//表名
				String tablename = MetaTableUtil.GetTableName(conn, satellite,
						productLevel);

				if (service.isFileArchive(conn, tablename, filename)) {
					myLogger.info("file had exist database:" + filename);
					continue;
				}
				if (!isFinishCopy(fF.getAbsoluteFile()))
					continue;

				if (fF.getName().toLowerCase().endsWith(".tar")
						|| fF.getName().toLowerCase().endsWith(".tar.gz")) {
					//xml文件全路径
					String xmlName = myPath.getPath() + fF.separator
							+ fF.getName();
					// windowsXMLFile = xmlName.replace("tar", "xml");
					if (Constants.IS_LINUX) {
						myLogger.info("  LINUX environment parse is not implement XML file: "
								+ windowsXMLFile);
					} else {
						ImportMeta2Database pImp = new ImportMeta2Database(
								xmlName, conn);
						boolean b = pImp.Import2Database();

						if (b)
							myLogger.info("Insert database success." + xmlName);
					}
				} else {

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