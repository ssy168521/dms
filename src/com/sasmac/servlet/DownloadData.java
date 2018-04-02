package com.sasmac.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sasmac.common.ThreadManager;
import com.web.thread.MyDownloadThread1;

public class DownloadData extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public DownloadData() {
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
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");

		String objSelec = request.getParameter("objSelec");
		JSONArray jsonArr = JSONArray.fromObject(objSelec);
		String FileName = "";
		String FilePath = "";
		String satellite = "";
		String storagePath = "";
		String ProductLevel = "";
		String suffix = ".tar";
		List<String> arrlistFiles = new ArrayList<String>();
		for (int i = 0; i < jsonArr.size(); i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			FileName = jsonObj.get("FileName").toString();
			FilePath = jsonObj.get("FilePath").toString();
			satellite = jsonObj.get("satellite").toString();
			ProductLevel = jsonObj.get("productLevel").toString();
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
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		java.util.Date date = new java.util.Date();
		String str = sdf.format(date);

		MyDownloadThread1 pThread = new MyDownloadThread1(arrlistFiles, "");
		String taskName = "download_" + str;
		pThread.setTaskName(taskName);
		ThreadManager pthreadpool = new ThreadManager();
		pthreadpool.submmitJob(pThread);

		// new Thread(pThread).start();
		out.print("downloading");

		out.println("  </BODY>");
		out.println("</HTML>");
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
