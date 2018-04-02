package com.web.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Log {
	public static String errorMessage;
	public static String strPath = "D:\\amp\\";
	public static boolean bDebug   = true;

	public static synchronized void writeTaskLog(int taskID, String strLog) {
		if(taskID<0||taskID==0){
			return;
		}
		FileOutputStream fos = null;
		PrintStream ps = null;
		String strFileName = "";
		try {
			File f = new File(strPath);
			if (!f.exists() && !f.isDirectory()) {
				f.mkdirs();
			}

			strFileName = strPath.concat("Task_"+taskID+".log");
			File file = new File(strFileName);
			if (file.exists()) {
				fos = new FileOutputStream(strFileName, true);
			} else {
				fos = new FileOutputStream(strFileName);
			}

			ps = new PrintStream(fos);
			Calendar dNow = Calendar.getInstance();
			strLog = dNow.getTime().toString().concat("        ".concat(strLog));
			ps.println(strLog);
			ps.flush();
		} catch (Exception ex) {
			System.out.println("Errors occured in log.writeTaskLog():" + ex.getMessage() + "strFileName=" + strFileName);
		} finally {
			closeIO(ps, fos);
		}
	}
	
	public static String getExceptionMsg(Exception e) {
		StringBuffer sb = null;
		Throwable te = null;
		String strCause = "";
		try {
			StackTraceElement[] ste = e.getStackTrace();
			te = e.getCause();
			if (te != null) {
				strCause = te.getLocalizedMessage();
			}
			String errorMessage = e.getMessage();
			sb = new StringBuffer();
			sb.append("Exception--->>" + e + "\n   error:'" + ":" + errorMessage + "\n  StackTrace:");
			if (strCause != null && !strCause.equals("")) {
				sb.append("caused by:'" + strCause + "'.");
			}
			int count = ste.length;
			if (count > 6) {
				count = 6;
			}
			for (int i = 0; i < count; i++) {
				sb.append(ste[i].toString() + "\n");
			}
		} catch (Exception e1) {
			System.out.println("log.getExceptionMsg() error:" + e1.getMessage());
		}
		return sb.toString();
	}

	/**
	 * 按日期输出日志
	 * 2013-10-22 zhang.daping
	 * @param strLog
	 */
	public static void println(String strLog) {
		FileOutputStream fos = null;
		PrintStream ps = null;
		try {
			File f = new File(strPath);

			if (!f.exists()) {
				f.mkdirs();
			}
			String strDate = getDate() + "";
			String strFileName = strPath.concat(strDate + ".log");
			File file = new File(strFileName);
			if (file.exists()) {
				fos = new FileOutputStream(strFileName, true);
			} else {
				fos = new FileOutputStream(strFileName);
			}

			ps = new PrintStream(fos);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = simpleDateFormat.format(new Date());

			strLog = date.toString().concat("    ".concat(strLog));
			ps.println(strLog);
			ps.flush();
		} catch (Exception ex) {
			System.out.println("write log error:" + ex.getMessage());
		} finally {
			closeIO(ps, fos);
		}
	}
	
	public static void println(String type, String strLog, String methodName) {
		FileOutputStream fos = null;
		PrintStream ps = null;
		String strPath = "";
		try {
			File f = new File(strPath);

			if (!f.exists()) {
				f.mkdirs();
			}
			String strDate = getDate() + "";
			String strFileName = "";
			if (type.equalsIgnoreCase("interface")) {
				strFileName = strPath.concat(strDate + "_daimsDSS_interface.log");
			}
			if (type.equalsIgnoreCase("dao")) {
				strFileName = strPath.concat(strDate + "_daimsDSS_dao.log");
			}
			if (type.equalsIgnoreCase("connection")) {
				strFileName = strPath.concat(strDate + "_daimsDSS_connection.log");
			}
			File file = new File(strFileName);
			if (file.exists()) {
				fos = new FileOutputStream(strFileName, true);
			} else {
				fos = new FileOutputStream(strFileName);
			}

			ps = new PrintStream(fos);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = simpleDateFormat.format(new Date());

			StackTraceElement[] stack = (new Throwable()).getStackTrace();
			if (stack.length >= 2) {
				StackTraceElement stackElement = stack[1];
				methodName = stackElement.getClassName() + "." + stackElement.getMethodName();
				if ("writeMessage".equalsIgnoreCase(stackElement.getMethodName())) {
					StackTraceElement stackElement2 = stack[2];
					methodName = stackElement2.getClassName() + "." + stackElement2.getMethodName();
				}
			}

			strLog = date.toString().concat("        ".concat("method :" + "[" + methodName + "]   ").concat(strLog) + "\n");
			ps.println(strLog);
			ps.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			closeIO(ps, fos);
		}
	}

	public static int getDate() {
		try {
			String sDate = "";
			String sYear = Calendar.getInstance().get(Calendar.YEAR) + "";
			String sMonth = (Calendar.getInstance().get(Calendar.MONTH) + 1) + "";
			String sDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
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

	@SuppressWarnings("unchecked")
	public static void writeMessage(String type, Class c, String method, Object obj) {
		println(type, obj.toString(), c.getName() + method);
	}

	/**
	 * 
	 * @param strMsg
	 * @param format
	 */
	public static String println(String strMsg,String format) {
		FileOutputStream fos = null;
		PrintStream ps = null;
		String strPath = "";
		String strFileName = "";
		try {
			File f = new File(strPath);

			if (!f.exists()) {
				f.mkdirs();
			}
			strFileName = strPath.concat(format);
			
			File file = new File(strFileName);
			if (file.exists()) {
				fos = new FileOutputStream(strFileName, true);
			} else {
				fos = new FileOutputStream(strFileName);
			}

			ps = new PrintStream(fos);

			ps.println(strMsg);
			ps.flush();
		} catch (Exception ex) {
			System.out.println("write log error:" + ex.getMessage());
		} finally {
			closeIO(ps, fos);
		}
		
		return strFileName;
	}
	
	public static synchronized void writeTaskLog(int taskID, String extTaskId, int dataId, String strLog) {
		if(taskID<0||taskID==0){
			return;
		}
		FileOutputStream fos = null;
		PrintStream ps = null;
		String strFileName = "";
		try {
			File f = new File(strPath);
			if (!f.exists() && !f.isDirectory()) {
				f.mkdirs();
			}

			strFileName = strPath.concat("Task_" + taskID + "_" + extTaskId + "_" + dataId +".log");
			File file = new File(strFileName);
			if (file.exists()) {
				fos = new FileOutputStream(strFileName, true);
			} else {
				fos = new FileOutputStream(strFileName);
			}

			ps = new PrintStream(fos);
			Calendar dNow = Calendar.getInstance();
			strLog = dNow.getTime().toString().concat("        ".concat(strLog));
			ps.println(strLog);
			ps.flush();
		} catch (Exception ex) {
			System.out.println("Errors occured in log.writeTaskLog():" + ex.getMessage() + "strFileName=" + strFileName);
		} finally {
			closeIO(ps, fos);
		}
	}
	
	private static void closeIO(PrintStream ps, FileOutputStream fos) {
		try {
			if (ps != null) {
				ps.close();
			}
			if (fos != null) {
				fos.close();
			}
		} catch (Exception ex) {
		}
	}
}