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

import com.sasmac.dbconnpool.ConnPoolUtil;
import com.sasmac.usermanager.common.Privinfo;
import com.sasmac.usermanager.common.Roleinfo;
import com.sasmac.usermanager.common.UserinfoDto;
import com.sasmac.usermanager.service.ManagementService;
import com.web.dao.WebDao;
import com.web.dao.impl.WebDaoImpl;
import com.web.form.ConfigureForm;

public class PageLoad extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PageLoad() {
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
		String requestSou = request.getParameter("requestSou");
		// System.out.println(requestSou);
		PrintWriter out = response.getWriter();
		String outputjson = null;

		if (requestSou.equals("userPage")) {
			ManagementService manaServ = new ManagementService();
			List<UserinfoDto> userlist = manaServ.getRoleDao().getUserlist();
			JSONArray jsonArray = JSONArray.fromObject(userlist);
			String jsonUserList = jsonArray.toString();
			// System.out.println(jsonUserList);
			manaServ.closeService();

			out.print(jsonUserList);
		} else if (requestSou.equals("rolePage")) {
			ManagementService manaServ = new ManagementService();
			List<Roleinfo> roleinfo = manaServ.getRoleDao().getRolelist();
			JSONArray jsonArray = JSONArray.fromObject(roleinfo);
			String jsonPrivList = jsonArray.toString();
			System.out.println(jsonPrivList);
			manaServ.closeService();
			out.print(jsonPrivList);

		} else if (requestSou.equals("privPage")) {
			ManagementService manaServ = new ManagementService();
			List<Privinfo> privlist = manaServ.getPrivDao().getPrivlist();
			JSONArray jsonArray = JSONArray.fromObject(privlist);
			String jsonPrivList = jsonArray.toString();
			// System.out.println(jsonPrivList);
			manaServ.closeService();
			out.print(jsonPrivList);
		} else if (requestSou.equals("adduserPage")) {
			ManagementService manaServ = new ManagementService();
			List<Roleinfo> rolelist = manaServ.getPrivDao().getRolelist();
			JSONArray jsonArray = JSONArray.fromObject(rolelist);
			String jsonRoleList = jsonArray.toString();
			// System.out.println(jsonRoleList);
			manaServ.closeService();
			out.print(jsonRoleList);
		} else if (requestSou.equals("addrolePage")) {
			ManagementService manaServ = new ManagementService();
			List<Privinfo> privlist = manaServ.getPrivDao().getPrivlist();
			JSONArray jsonArray = JSONArray.fromObject(privlist);
			String jsonPrivList = jsonArray.toString();
			// System.out.println(jsonPrivList);
			manaServ.closeService();
			out.print(jsonPrivList);
		} else if (requestSou.equals("ArchiveConfPage")) {
			WebDao webdao = new WebDaoImpl();

			List<ConfigureForm> confForm;
			try {
				Connection con = ConnPoolUtil.getConnection();
				confForm = webdao.getAllSysConfig(con);
				JSONArray jsonArray = JSONArray.fromObject(confForm);
				String jsonList = jsonArray.toString();
				// System.out.println(jsonList);
				out.print(jsonList);
				ConnPoolUtil.close(con, null, null);
			} catch (Exception e) {
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
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
