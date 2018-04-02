package com.sasmac.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import zy3dms.CountStatistic;
import zy3dms.ImageSize;

import com.sasmac.dbconnpool.ConnPoolUtil;
import com.sasmac.test.DebugFun;

public class CountStatisticservlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public CountStatisticservlet() {
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
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
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
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 设置response的编码
		response.setCharacterEncoding("utf-8");
		// 设置request的编码
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();	
		//DebugFun.showParams(request);
		
		String operate = request.getParameter("operate");
		List<ImageSize>listImg=null;
		if(operate.equals("Size"))
		{
			listImg = CountStatistic.ImgSizeStatistic();
		}else if (operate.equals("Count")) {
			listImg = CountStatistic.ImgCountStatistic();
		}
		
		JSONArray jsonArray=JSONArray.fromObject(listImg);
		String jsonlistImg=jsonArray.toString();
		System.out.println(jsonlistImg);
		out.print(jsonlistImg);

		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
