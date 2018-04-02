package com.sasmac.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.dbutils.QueryRunner;

import com.sasmac.dbconnpool.ConnPoolUtil;
import com.web.common.Constants;
import com.web.util.FileUtil;
import com.web.util.PropertiesUtil;

public class DataManager extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public DataManager() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		PrintWriter out = response.getWriter();

		String objSelec = request.getParameter("objSelec");
		JSONArray jsonArr;
		if (objSelec.startsWith("[")) {
			jsonArr = JSONArray.fromObject(objSelec);
		} else {
			JSONObject obj = JSONObject.fromObject(objSelec);
			jsonArr = new JSONArray();
			jsonArr.add(obj);
		}
		String FileName = "";
		String FilePath = "";
		String satellite = "";
		String storagePath = "";
		String ProductLevel = "";
		String suffix = ".tar";
		String conffilepath = "";
		String photoDate = "";
		String orbit = "";
		String Sensor = "";
		String overviewpath = "";
		String tbname = request.getParameter("tablename");
		if (tbname == null || tbname.isEmpty())
			return;
		try {
			conffilepath = Loadoverview.class.getClassLoader().getResource("/")
					.toURI().getPath();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PropertiesUtil propertiesUtil = new PropertiesUtil(conffilepath
				+ Constants.STR_CONF_PATH);
		String stroverviewfilepath = propertiesUtil
				.getProperty("overviewfilepath");

		int num = jsonArr.size();
		Object[][] param = new Object[num][1];

		String strSQL = "delete from " + tbname;
		String where = " where dataid=?";
		String sql = strSQL + where;

		List<String> arrlistFiles = new ArrayList<String>();
		List<String> overviewFilelist = new ArrayList<String>();
		for (int i = 0; i < num; i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			FileName = jsonObj.get("FileName").toString();
			FilePath = jsonObj.get("FilePath").toString();
			satellite = jsonObj.get("satellite").toString();
			ProductLevel = jsonObj.get("productLevel").toString();
			photoDate = jsonObj.get("acquisitionTime").toString();
			orbit = String.format("%06d", jsonObj.get("orbitID"));
			Sensor = jsonObj.get("sensor").toString();
			photoDate = photoDate.replace("/", "-");
			if (satellite.contains("GF")) {
				if (ProductLevel.contains("LEVEL1A")) {
					suffix = ".tar.gz";
				}
			} else if (satellite.contains("ZY3")) {
				if (ProductLevel.contains("SC")) {
					suffix = ".tar";
				}
			}
			storagePath = FilePath + File.separator + FileName + suffix;
			arrlistFiles.add(storagePath);

			overviewpath = File.separator + satellite + File.separator
					+ ProductLevel + File.separator + photoDate
					+ File.separator + orbit + File.separator + Sensor
					+ File.separator + jsonObj.get("FileName").toString()
					+ ".png";

			overviewFilelist.add(stroverviewfilepath + overviewpath);

			param[i][0] = jsonObj.get("dataid");

		}
		int testflag = 0;
		int n = arrlistFiles.size();
		// 删除文件实体 和文件实体
		try {
			String tmpstr = "";
			for (int i = 0; i < n; i++) {
				FileUtil.deletefile(arrlistFiles.get(i));
				FileUtil.deletefile(overviewFilelist.get(i));
				tmpstr = overviewFilelist.get(i);
				tmpstr = tmpstr.replace(".png", ".xml");
				FileUtil.deletefile(tmpstr);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Connection conn = ConnPoolUtil.getConnection();
			if (conn == null)
				return;
			QueryRunner qr = new QueryRunner();
			int[] ret = qr.batch(conn, sql, param);
			ConnPoolUtil.close(conn, null, null);

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		out.print("删除成功！");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
