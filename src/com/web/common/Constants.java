package com.web.common;

import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.web.util.PropertiesUtil;
import com.web.util.XMLUtil;

public class Constants {

	public static final int THREAD_STATUS_RUNNING = 1;
	/** 中止 **/
	public static final int THREAD_STATUS_STOPED = 0;
	/** 无效 **/
	public static final int THREAD_STATUS_NOACTIVE = 2;
	public static final int THREAD_STATUS_FINISHED = 3;
	public static final int THREAD_STATUS_UNKNOWN = -1;

	public static String STR_DATASOURCE_PATH_WIN = "com/sasmac/conf/dataSource.xml";
	public static String STR_DATASOURCE_PATH_LIN = "/usr/local/cms/cms-dataSource.xml";
	public static String STR_DATABASE_CONF = "com/sasmac/conf/dbConnConf.properties";
	public static String STR_CONF_PATH = "com/sasmac/conf/sysconf.xml";
	public static String STR_MAPDISK_CONF_PATH = "com/sasmac/conf/DiskMap.xml";
	public static String STR_SFICOPY_PATH = "com/sasmac/conf/SfiPath.xml";
	public static String STR_SVICOPY_PATH = "com/sasmac/conf/SviPath.xml";
	public static String logPath;
	/** 缁捐法鈻煎锝呮躬鏉╂劘顢� **/

	public static boolean IS_LINUX = false;

	public static PropertiesUtil confUtil;
	private static HashMap<String, Integer> hMap = new HashMap<String, Integer>();

	static {

		try {
			String path = Constants.class.getClassLoader().getResource("/")
					.toURI().getPath();
			confUtil = new PropertiesUtil(path + STR_CONF_PATH);
			logPath = confUtil.getProperty("logfilepath");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    /**
     * 
     * @param strArchivedir 归档源路径
     * @return
     * @throws Exception
     */
	public static int AssertFileIsSMBFileDir(String strArchivedir)throws Exception {
		try {
			if (strArchivedir == null)
				return -1;
			if (strArchivedir.isEmpty())
				return -1;
			int idx = strArchivedir.indexOf(":");//C:\Users\Administrator\Desktop,http://localhost:8080/zy3dms/upl
			if (idx <= -1)
				return -1;
            //磁盘  
			String Diskroot = strArchivedir.substring(0, idx);
			if (Diskroot.isEmpty())
				return -1;
			if (Diskroot.compareToIgnoreCase("smb") == 0)
				return 2;
            //获取Constants类路径 E:\MyEclipse2014 workspace\zy3dms\src
			String path = Constants.class.getClassLoader().getResource("/")
					.toURI().getPath();
			//解析 DiskMap.xml，获取smb参数
			HashMap<String, String> paramap = XMLUtil.getMaplistByDiskname(path
					+ Constants.STR_MAPDISK_CONF_PATH, Diskroot);
			int n = paramap.size();
			if (n <= 0)
				return 0;

			Iterator<Entry<String, String>> iter = paramap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				HashMap.Entry<String, String> entry = (HashMap.Entry<String, String>) iter
						.next();
				String key = (String) entry.getKey();
				if (key.compareToIgnoreCase("path") == 0) {

					return 1;
				}
			}

		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return 0;
	}

	private static int getDate() {
		try {
			String sDate = "";
			String sYear = Calendar.getInstance().get(Calendar.YEAR) + "";
			String sMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1)
					+ "";
			String sDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
					+ "";
			if (sMonth.length() == 1) {
				sMonth = "0" + sMonth;
			}
			if (sDay.length() == 1) {
				sDay = "0" + sDay;
			}
			sDate = sYear + sMonth + sDay;

			return Integer.parseInt(sDate);
		} catch (Exception e) {
			return 0;
		}
	}

	public synchronized static void WriteLog(String szStr) {
		FileWriter LogStream = null;
		String szLogFile = logPath;
		try {
			File f = new File(szLogFile);
			if (!f.exists() && !f.isDirectory()) {
				f.mkdirs();
				String Path = f.getAbsolutePath();
			}
			String Path = f.getAbsolutePath();
			String strDate = getDate() + "_"; // Utility.getDate()+"";
			String strFileName = "";
			strFileName = szLogFile.concat(strDate + ".log");

			LogStream = new FileWriter(strFileName, true);
			SimpleDateFormat DateStr = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			LogStream
					.write(DateStr.format(new Date(System.currentTimeMillis()))
							+ ": " + szStr + " \r\n");
		} catch (Exception e) {
		}
		try {
			if (LogStream != null) {
				LogStream.close();
			}
		} catch (Exception e) {
		}
		return;
	}
}
