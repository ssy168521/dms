package com.web.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.io.IOUtils;

import com.web.common.Constants;

/**
 * 
 * @author Administrator
 */
public class DbUtils {
	private static BasicDataSource dataSource = null;
	private static int count;
	private static Object conn_block = new Object();

	public static Connection getConnection() throws SQLException {
		return getConnection(false);
	}

	public static Connection getConnection(boolean autoCommit) throws SQLException {
		Connection result = null;
		synchronized(conn_block){
			if (dataSource == null) {
				init();
			}
			if (dataSource != null) {
				result = dataSource.getConnection();
				result.setAutoCommit(autoCommit);
			}
		}
		StackTraceElement[] stack = (new Throwable()).getStackTrace();
		if (stack.length >= 2) {
			count++;
//			String message = "get connection : count is " + count + " hashcode is ," + result.hashCode() + " ,call is "
//				+ stackElement.getClassName() + "." + stackElement.getMethodName();
//			Log.println(Constants.CONNECTION, message, "getConnection");
		}
		return result;
	}

	public static void rollbackTransaction(Connection connection) throws SQLException {
		org.apache.commons.dbutils.DbUtils.rollback(connection);
	}

	public static void rollbackTransactionQuietly(Connection connection) {
		try {
			org.apache.commons.dbutils.DbUtils.rollback(connection);
		} catch (SQLException e) {
		}
	}

	public static void rollbackAndCloseQuietlyTransaction(Connection connection) {
		org.apache.commons.dbutils.DbUtils.rollbackAndCloseQuietly(connection);
	}

	public static void completeTransaction(Connection connection)
			throws SQLException {
		connection.commit();
	}

	public static void completeAndCloseQuietlyTransaction(Connection connection) {
		if (connection == null) {
			return;
		}

		StackTraceElement[] stack = (new Throwable()).getStackTrace();
		if (stack.length >= 2) {
			count--;
//			String message = "close connection : count is " + count
//					+ " hashcode is ," + connection.hashCode() + " ,call is "
//					+ stackElement.getClassName() + "."
//					+ stackElement.getMethodName();
//			Log.println(Constants.CONNECTION, message, "getConnection");
		}
		org.apache.commons.dbutils.DbUtils.commitAndCloseQuietly(connection);
	}

	public static synchronized void closeQuietlyConnection(Connection connection) {
		if (connection == null) {
			return;
		}
		try {
			StackTraceElement[] stack = (new Throwable()).getStackTrace();
			if (stack.length >= 2) {
				count--;
//				String message = "close connection : count is " + count
//						+ " hashcode is ," + connection.hashCode()
//						+ " ,call is " + stackElement.getClassName() + "."
//						+ stackElement.getMethodName();
//				Log.println(Constants.CONNECTION, message, "getConnection");
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeQuietlyTransaction(Connection connection) {
		if (connection == null) {
			return;
		}

		StackTraceElement[] stack = (new Throwable()).getStackTrace();
		if (stack.length >= 2) {
			count--;
//			String message = "close connection : count is " + count
//					+ " hashcode is ," + connection.hashCode() + " ,call is "
//					+ stackElement.getClassName() + "."
//					+ stackElement.getMethodName();
//			Log.println(Constants.CONNECTION, message, "getConnection");
		}
		org.apache.commons.dbutils.DbUtils.rollbackAndCloseQuietly(connection);
	}

	public static void init() {
		if (dataSource != null) {
			try {
				dataSource.close();
			} catch (Exception e) {
			}
			dataSource = null;
		}
		InputStream inputStream = null;
		try {
			String path = DbUtils.class.getClassLoader().getResource("/").toURI().getPath(); 
		
			//InputStream path1=DbUtils.class.getClass().getResourceAsStream(path+Constants.STR_DATASOURCE_PATH_WIN);
			//InputStream fis=new FileInputStream(path+Constants.STR_DATASOURCE_PATH_WIN); 
			String osName = System.getProperty("os.name");
			if(osName.toLowerCase().contains("win"))
				inputStream = new FileInputStream(path+Constants.STR_DATASOURCE_PATH_WIN);
			else
				inputStream = new FileInputStream(path+Constants.STR_DATASOURCE_PATH_LIN);

			Properties properties = new Properties();
			properties.loadFromXML(inputStream);
			properties.list(System.out);
			
			dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(properties);
		} catch (Exception ex) {
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
}
