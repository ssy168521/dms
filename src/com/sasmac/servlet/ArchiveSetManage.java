package com.sasmac.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.sasmac.dbconnpool.ConnPoolUtil;
import com.sasmac.gdaldatapool.DataSouPool;
import com.sasmac.test.DebugFun;

import org.apache.commons.dbutils.*;

public class ArchiveSetManage extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ArchiveSetManage() {
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
		PrintWriter out=response.getWriter();
		String OperateType = request.getParameter("OperateType");
		if(OperateType == null) return;
		
		DebugFun.showParams(request);
       if(OperateType.compareToIgnoreCase("Del")==0)
       {
			String id= request.getParameter("id");
			String satellite= request.getParameter("satellite");
			String archivePath= request.getParameter("archivePath");
	
			Connection conn =ConnPoolUtil.getConnection(); 
			if(conn==null) return;
			QueryRunner qr = new QueryRunner();
			String sql="delete from sysconfig where id=? and satellite=?";
		    Object para[]=new Object[2];
			para[0]=id;
			para[1]=satellite;
		//	para[2]=archivePath;
			try {
				qr.update(conn,sql, para);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ConnPoolUtil.close(conn, null, null);
	
			
		}
       else  if(OperateType.compareToIgnoreCase("BatchDel")==0)
       {
    	    String  objSelec = request.getParameter("objSelec");
    		JSONArray jsonArr=JSONArray.fromObject(objSelec);
			int n= jsonArr.size();
			if(n<=0) return;	
            Connection conn =ConnPoolUtil.getConnection(); 
			if(conn==null) return;
			QueryRunner qr = new QueryRunner();
			String sql="delete from sysconfig where id=? and satellite=? and archivePath=?";
		    Object para[][]=new Object[n][3];
			for (int i=0;i<n;i++)
			{
				JSONObject jsonObj=jsonArr.getJSONObject(i);
				String id=jsonObj.get("id").toString();
				String satellite=jsonObj.get("satellite").toString();
				String archivePath=jsonObj.get("archivePath").toString();
				para[i][0]=id;
				para[i][1]=satellite;
				para[i][2]=archivePath;
			}
			try {
				qr.batch(conn,sql, para);
				ConnPoolUtil.close(conn, null, null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       }
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

	public static void main(String[] args) {
		
	}

}
