package com.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.web.action.ConfigureAction;
import com.web.form.ConfigureForm;

public class AutoArchive extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public AutoArchive() {
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

		// 设置response的编码
		response.setCharacterEncoding("utf-8");
		// 设置request的编码
		request.setCharacterEncoding("utf-8");
		// 设置response的编码

		PrintWriter out = response.getWriter();

		String strsatellite = request.getParameter("satellite");
		String strArchivePath = request.getParameter("archivePath");
		String strDestpath = request.getParameter("destPath");
		String strStorageRule = request.getParameter("storageRule");
		String strCheckRule = request.getParameter("checkRule");
		String strArchiveStart = request.getParameter("archiveStart");
		String strArchiveEnd = request.getParameter("archiveEnd");
		String strOperator = request.getParameter("Operator");
		String strOid = request.getParameter("id");

		ConfigureAction act = new ConfigureAction();
		ConfigureForm conf = new ConfigureForm();
		conf.setSatellite(strsatellite);
		conf.setArchivePath(strArchivePath);
		conf.setDestPath(strDestpath);
		conf.setArchiveEnd(Integer.parseInt(strArchiveEnd));
		conf.setArchiveStart(Integer.parseInt(strArchiveStart));
		conf.setCheckRule(strCheckRule);
		conf.setStorageRule(strStorageRule);
		if (strOid != null) {
			conf.setId(Integer.parseInt(strOid));
		}

		if (strOperator.compareToIgnoreCase("add") == 0) {
			act.addInfo(conf);
		} else if (strOperator.compareToIgnoreCase("modify") == 0) {
			act.editInfo(conf);
		} else if (strOperator.compareToIgnoreCase("delete") == 0) {
			act.deleteInfo(conf);
		} else {
			out.print("");
		}

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
