package com.sasmac.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sasmac.usermanager.dao.UserDao;
import com.sasmac.usermanager.service.UserService;

public class Login extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Login() {
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
		/*
		 * out.println(
		 * "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		 * out.println("<HTML>");
		 * out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		 * out.println("  <BODY>");
		 */

		String OperType = request.getParameter("opertype");
		if (OperType == null)
			return;
		if (OperType.compareToIgnoreCase("register") == 0) {
			String username = request.getParameter("regname");
			String userpwd = request.getParameter("regpwd");
			String mail = request.getParameter("mail");
			UserService userservice = new UserService();
			// PrintWriter out=response.getWriter();
			UserDao user = userservice.getUserDao();
			boolean b = user.regNew(username, userpwd);
			if (!b) {
				out.print(false);
			} else {
				out.print(true);
			}
			userservice.closeService();
		} else if (OperType.compareToIgnoreCase("login") == 0) {
			String username = request.getParameter("name");
			String userpwd = request.getParameter("password");
			HttpSession session = null;
			session = request.getSession();
			UserService userservice = new UserService();
			// PrintWriter out=response.getWriter();
			UserDao user = userservice.getUserDao();
			boolean res = user.login(username, userpwd);
			if (!res) {
				request.getSession().setAttribute("logininfo", "用户或密码错误！");
				response.sendRedirect(request.getContextPath() + "/login.jsp");
				out.print("用户或密码错误！");

			} else {
				String strPath = request.getContextPath();
				response.sendRedirect(strPath + "/query.jsp");
				session.setAttribute("username", username);

				// response.sendRedirect("/zy3dms/home.jsp");
				// out.print("true");
			}
			userservice.closeService();
		} else if (OperType.compareToIgnoreCase("logout") == 0) {

			HttpSession session = null;
			session = request.getSession();
			if (session.getAttribute("username") != null) {
				String username = session.getAttribute("username").toString();
				if (!username.isEmpty()) {
					session.setAttribute("username", "");
					UserService userservice = new UserService();
					boolean b = userservice.getUserDao().logout();
					userservice.closeService();
					out.println("已退出登录");
				} else {
					out.println("还未登录");
				}

			} else {
				out.println("还未登录");
			}
		}

		/*
		 * out.println("  </BODY>"); out.println("</HTML>");
		 */
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
