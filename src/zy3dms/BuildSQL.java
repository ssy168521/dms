package zy3dms;

import javax.servlet.http.HttpServletRequest;

public class BuildSQL {

	public BuildSQL() {

	}

	public static void main(String[] args) {

	}

	public static String BuildWhereClause(HttpServletRequest request) {
		String cloud1 = request.getParameter("cloud1");
		String cloud2 = request.getParameter("cloud2");
		String path1 = request.getParameter("path1");
		String path2 = request.getParameter("path2");
		String row1 = request.getParameter("row1");
		String row2 = request.getParameter("row2");
		String date1 = request.getParameter("date1");
		String date2 = request.getParameter("date2");
		String orbitid = request.getParameter("orbitid");

		String strSQL = "";

		if (orbitid != "") {
			strSQL += "orbitID=" + orbitid;
			strSQL += " and ";
		}
		if (cloud1 != "") {
			strSQL += "cloudPercent>=" + cloud1;
			strSQL += " and ";
		}
		if (cloud2 != "") {
			strSQL += "cloudPercent<=" + cloud2;
			strSQL += " and ";
		}
		if (path1 != "") {
			strSQL += "scenePath>=" + path1;
			strSQL += " and ";
		}
		if (path2 != "") {
			strSQL += "scenePath<=" + path2;
			strSQL += " and ";
		}
		if (row1 != "") {
			strSQL += "sceneRow>=" + row1;
			strSQL += " and ";
		}
		if (row2 != "") {
			strSQL += "sceneRow>=" + row2;
			strSQL += " and ";
		}
		if (date1 != "") {
			strSQL += "acquisitionTime>='" + date1 + "'";
			strSQL += " and ";
		}
		if (date2 != "") {
			strSQL += "acquisitionTime<='" + date2 + "'";
			strSQL += " and ";
		}

		int nstrSQLlength = strSQL.length();
		// 判断最后一个单词是否为where
		if (strSQL.indexOf("where") > nstrSQLlength - 6) {
			strSQL = strSQL.substring(0, strSQL.lastIndexOf("where") - 1);
		}
		// 判断最后一个单词是否为and
		if (strSQL.lastIndexOf("and") > nstrSQLlength - 5) {
			strSQL = strSQL.substring(0, strSQL.lastIndexOf("and") - 1);
		}
		return strSQL;
	}

	public static String TxtDataidWhereClause(String dataids) {
		String strWhereSQL = "";
		String[] dataidArray = dataids.split(",|，| ");
		// strWhereSQL+="dataid in(";
		for (int i = 0; i < dataidArray.length; i++) {
			if (dataidArray[i] != "") {
				// strWhereSQL+="'"+dataidArray[i]+"',";
				strWhereSQL += "dataid='" + dataidArray[i] + "' or ";

			}
		}
		// 去掉最后一个逗号
		/*
		 * if (strWhereSQL.lastIndexOf(",") == strWhereSQL.length() - 1) {
		 * strWhereSQL = strWhereSQL.substring(0, strWhereSQL.lastIndexOf(","));
		 * } strWhereSQL+=")";
		 */
		// 判断最后一个单词是否为Or
		if (strWhereSQL.lastIndexOf("or") > strWhereSQL.length() - 4) {
			strWhereSQL = strWhereSQL.substring(0,
					strWhereSQL.lastIndexOf("or"));
		}

		return strWhereSQL;
	}

	public static String querySQL2(HttpServletRequest request) {
		String[] zy301sensor = request.getParameterValues("zy301sensor");
		String[] zy302sensor = request.getParameterValues("zy302sensor");
		String[] gf1sensor = request.getParameterValues("gf1sensor");
		String[] gf2sensor = request.getParameterValues("gf2sensor");
		String cloud1 = request.getParameter("minCloud");
		String cloud2 = request.getParameter("maxCloud");
		String path = request.getParameter("path1");
		String row = request.getParameter("row1");
		String acquiredate1 = request.getParameter("acquiredate1");
		String acquiredate2 = request.getParameter("acquiredate2");
		String archivedate1 = request.getParameter("archivedate1");
		String archivedate2 = request.getParameter("archivedate2");
		String orbitid = request.getParameter("orbitid");
		String dataid = request.getParameter("dataid");

		String[] zy3_sensorradio = request
				.getParameterValues("zy3_sensorradio");

		String[] zy3_radio = request.getParameterValues("zy3_radio");

		// String tbname = "TB_ZY301_SC";

		// String strSQL =
		// "select FID,FileName,FilePath,scenePath,sceneRow,orbitID,satellite,sensor,acquisitionTime,productLevel,cloudPercent from "+
		// tbname;
		// strSQL += " where ";

		String strSQL = "";

		if (cloud1 != "" && cloud1 != null) {
			strSQL += "cloudPercent>=" + cloud1;
			strSQL += " and ";
		}
		if (cloud2 != "" && cloud2 != null) {
			strSQL += "cloudPercent<=" + cloud2;
			strSQL += " and ";
		}
		if (orbitid != "" && orbitid != null) {
			strSQL += "orbitID=" + orbitid;
			strSQL += " and ";
		}
		if (dataid != "" && dataid != null) {
			strSQL += "dataid=" + dataid;
			strSQL += " and ";
		}
		if (path != "" && path != null) {
			strSQL += "scenePath=" + path;
			strSQL += " and ";
		}
		if (row != "" && row != null) {
			strSQL += "sceneRow=" + row;
			strSQL += " and ";
		}
		if (acquiredate1 != "" && acquiredate1 != null) {
			strSQL += "acquisitionTime>='" + acquiredate1 + "'";
			strSQL += " and ";
		}
		if (acquiredate2 != "" && acquiredate2 != null) {
			strSQL += "acquisitionTime<='" + acquiredate2 + "'";
			strSQL += " and ";
		}

		if (archivedate1 != "" && archivedate1 != null) {
			strSQL += "ArchiveTime>='" + archivedate1 + "'";
			strSQL += " and ";

		}
		if (archivedate2 != "" && archivedate2 != null) {
			strSQL += "ArchiveTime<='" + archivedate2 + "'";
			strSQL += " and ";
		}
		if (zy3_sensorradio==null ||zy3_sensorradio.length <= 0) // 一般查询
		{
			if (zy301sensor != null) {
				strSQL += "satellite='ZY3-1'";
				strSQL += " and ";
				strSQL += "(";
				for (int j = 0; j < zy301sensor.length - 1; j++) {
					strSQL += "sensor='" + zy301sensor[j] + "'";
					strSQL += " or ";
				}
				strSQL += "sensor='" + zy301sensor[zy301sensor.length - 1]
						+ "')";
				strSQL += " or ";
			}
			if (zy302sensor != null) {
				strSQL += "satellite='ZY3-2'";
				strSQL += " and ";
				strSQL += "(";
				for (int j = 0; j < zy302sensor.length - 1; j++) {
					strSQL += "sensor='" + zy302sensor[j] + "'";
					strSQL += " or ";
				}
				strSQL += "sensor='" + zy302sensor[zy302sensor.length - 1]
						+ "')";
				strSQL += " or ";
			}
			if (gf1sensor != null) {
				strSQL += "satellite='GF1'";
				strSQL += " and ";
				strSQL += "(";
				for (int j = 0; j < gf1sensor.length - 1; j++) {
					strSQL += "sensor='" + gf1sensor[j] + "'";
					strSQL += " or ";
				}
				strSQL += "sensor='" + gf1sensor[gf1sensor.length - 1] + "')";
				strSQL += " or ";
			}
			if (gf2sensor != null) {
				strSQL += "satellite='GF2'";
				strSQL += " and ";
				strSQL += "(";
				for (int j = 0; j < gf2sensor.length - 1; j++) {
					strSQL += "sensor='" + gf2sensor[j] + "'";
					strSQL += " or ";
				}
				strSQL += "sensor='" + gf2sensor[gf2sensor.length - 1] + "')";
				strSQL += " or ";
			}

		}

		int nstrSQLlength = strSQL.length();

		// 判断最后一个单词是否为where
		if (strSQL.indexOf("where") >= nstrSQLlength - 6) {
			strSQL = strSQL.substring(0, strSQL.lastIndexOf("where") - 1);
		}
		// 判断最后一个单词是否为and
		if (strSQL.lastIndexOf("and") > nstrSQLlength - 5) {
			strSQL = strSQL.substring(0, strSQL.lastIndexOf("and") - 1);
		}
		// 判断最后一个单词是否为or
		if (strSQL.lastIndexOf("or") > nstrSQLlength - 4) {
			strSQL = strSQL.substring(0, strSQL.lastIndexOf("or") - 1);
		}
		return strSQL;

	}

	public static String querySQL(HttpServletRequest request) {
		String cloud1 = request.getParameter("cloud1");
		String cloud2 = request.getParameter("cloud2");
		String path1 = request.getParameter("path1");
		String path2 = request.getParameter("path2");
		String row1 = request.getParameter("row1");
		String row2 = request.getParameter("row2");
		String date1 = request.getParameter("date1");
		String date2 = request.getParameter("date2");
		String orbitid = request.getParameter("orbitid");

		String tbname = request.getParameter("tbname");

		String strSQL = "select FID,FileName,FilePath,scenePath,sceneRow,orbitID,satellite,sensor,acquisitionTime,productLevel,cloudPercent from "
				+ tbname;
		strSQL += " where ";

		if (orbitid != "") {
			strSQL += "orbitID=" + orbitid;
			strSQL += " and ";
		}
		if (cloud1 != "") {
			strSQL += "cloudPercent>=" + cloud1;
			strSQL += " and ";
		}
		if (cloud2 != "") {
			strSQL += "cloudPercent<=" + cloud2;
			strSQL += " and ";
		}
		if (path1 != "") {
			strSQL += "scenePath>=" + path1;
			strSQL += " and ";
		}
		if (path2 != "") {
			strSQL += "scenePath<=" + path2;
			strSQL += " and ";
		}
		if (row1 != "") {
			strSQL += "sceneRow>=" + row1;
			strSQL += " and ";
		}
		if (row2 != "") {
			strSQL += "sceneRow>=" + row2;
			strSQL += " and ";
		}
		if (date1 != "") {
			strSQL += "acquisitionTime>='" + date1 + "'";
			strSQL += " and ";
		}
		if (date2 != "") {
			strSQL += "acquisitionTime<='" + date2 + "'";
			strSQL += " and ";
		}

		int nstrSQLlength = strSQL.length();
		// 判断最后一个单词是否为where
		if (strSQL.indexOf("where") > nstrSQLlength - 6) {
			strSQL = strSQL.substring(0, strSQL.lastIndexOf("where") - 1);
		}
		// 判断最后一个单词是否为and
		if (strSQL.lastIndexOf("and") > nstrSQLlength - 5) {
			strSQL = strSQL.substring(0, strSQL.lastIndexOf("and") - 1);
		}
		return strSQL;
	}

}
