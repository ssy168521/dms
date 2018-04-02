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

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.sasmac.common.ThreadManager;
import com.sasmac.dbconnpool.ConnPoolUtil;
import com.web.common.THREADSTATUS;
import com.web.form.TaskInfo;
import com.web.util.DataUtils;

public class ShowTaskInfoservlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ShowTaskInfoservlet() {
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
		response.setContentType("text/html; charset=utf-8");

		String taskrunning = request.getParameter("taskrunning-checkbox");
		String taskfinished = request.getParameter("taskfinished-checkbox");
		String taskstop = request.getParameter("taskstop-checkbox");
		String tasktype = request.getParameter("tasktypename");
		PrintWriter out = response.getWriter();
		String outputjson = null;
		List<TaskInfo> pTaskInfoList = new ArrayList<TaskInfo>();

		if (taskrunning != null) {
			ThreadManager pThreadManager = new ThreadManager();
			pTaskInfoList = pThreadManager.getAllTaskInfoByTaskType(tasktype);
		}
		String strtaskstop = "";
		String strtaskfinished = "";
		if (taskstop != null) {
			int j = THREADSTATUS.THREAD_STATUS_STOPED.ordinal();
			strtaskstop = Integer.toString(j);
		}
		if (taskfinished != null) {
			int i = THREADSTATUS.THREAD_STATUS_FINISHED.ordinal();
			strtaskfinished = Integer.toString(i);
		}
		List<TaskInfo> pTaskInfo1 = null;
		if (strtaskfinished != "" || strtaskstop != "") {
			String where = "where ";
			String sql = "";

			if (strtaskfinished != "" && strtaskstop != "")
				where = where + "(taskStatus=" + strtaskfinished
						+ " or taskStatus=" + strtaskstop + ")";
			else if (strtaskfinished != "")
				where = where + "taskStatus=" + strtaskfinished;
			else
				where = where + "taskStatus=" + strtaskstop;
			if (where.length() > 7 && tasktype != null && !tasktype.isEmpty()) {
				where += " and ";
				where += " tasktype like \"%" + tasktype + "%\"";
			}

			sql = "select taskId,taskName,taskType,taskStatus,starttime,finishtime,Progress,markinfo from taskinfo "
					+ where;
			ConnPoolUtil pconnpool = new ConnPoolUtil();
			Connection conn = pconnpool.getConnection();
			if (conn != null) {
				DataUtils datautils = new DataUtils(conn);
				try {

					ResultSetHandler<List<TaskInfo>> rsh = new BeanListHandler<TaskInfo>(
							TaskInfo.class);
					pTaskInfo1 = new QueryRunner().query(conn, sql, rsh);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			pconnpool.close(conn, null, null);
		}

		if (pTaskInfo1 != null)
			pTaskInfoList.addAll(pTaskInfo1);

		try {
			if (pTaskInfoList != null) {
				JSONArray jsonArray = JSONArray.fromObject(pTaskInfoList);
				String jsonList = jsonArray.toString();
				out.print(jsonList);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
