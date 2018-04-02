package com.sasmac.util;

import java.sql.Connection;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class MetaTableUtil {
	public static String GetTableName(Connection conn, String satellite,
			String productLevel) {
		if (conn == null)
			return "";
		String strTableName = "";

		String Sql = "select tablename from tb_metamanager where satellite=?  and productLevel=?";
		Object[] params = { satellite, productLevel };

		try {
			QueryRunner QR = new QueryRunner();
			strTableName = QR.query(conn, Sql, new ScalarHandler<String>(),
					params);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return strTableName;
	}

}
