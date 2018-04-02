package com.sasmac.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.sasmac.usermanager.dao.UserDao;
import com.sasmac.usermanager.service.UserService;

public class PermissionFilter implements Filter {

	private HttpServletRequest request;

	// private HttpServletResponse response;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	/***
	 * ????????
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		this.request = (HttpServletRequest) request;

		// this.response = (HttpServletResponse) response;
		// ??????????????��??:
		String currentURL = this.request.getRequestURI();
		// ???????????????????
		String targetURL = currentURL.substring(currentURL.indexOf("/", 1),
				currentURL.length());

		if ("/login.jsp".equals(targetURL) || "/query.jsp".equals(targetURL)
				|| targetURL.contains("/default.jsp")
				|| targetURL.contains("/servlet/Login")
				|| targetURL.contains("/servlet/RegionSelect")
				|| targetURL.contains("/servlet/DBQuery")
				|| targetURL.contains("/servlet/Loadoverview")) {
			chain.doFilter(request, response);
			return;
		}

		HttpSession session = this.request.getSession(false);
		if (session == null) {

			this.request.getRequestDispatcher("/login.jsp").forward(request,
					response);

			return;
		}

		String username = "";
		Object obj = session.getAttribute("username");
		if (obj != null) {
			username = session.getAttribute("username").toString();
			if (username.isEmpty()) {
				this.request.getRequestDispatcher("/login.jsp").forward(
						request, response);

				return;
			}
		}
		boolean bIslogin = true;
		boolean bHavePriveledge = true;

		UserService userservice = new UserService();
		UserDao user = userservice.getUserDao();

		Map<String, String> mapobj = new HashMap<String, String>();

		if (username.isEmpty()) {
			mapobj.put("CODE", "NOLOGIN");
			mapobj.put("message", "还未登录！");
			bIslogin = false;
			bHavePriveledge = false;
		}
		if ("/Taskmanager.jsp".equals(targetURL)) {
			if (!bIslogin) {
				this.request.getRequestDispatcher("/login.jsp").forward(
						request, response);
				return;
			}
			if (!user.IsContainPriv(username, "dataarchive")) {

				mapobj.put("CODE", "NOPIVELEDGE");
				bHavePriveledge = false;
			}
		} else if ("/sysmanager.jsp".equals(targetURL)) {
			if (!bIslogin) {
				this.request.getRequestDispatcher("/login.jsp").forward(
						request, response);
				return;
			}
			if (!user.IsContainPriv(username, "systemmanage")) {

				mapobj.put("CODE", "NOPIVELEDGE");
				bHavePriveledge = false;
			}
		} else if ("/Archive.jsp".equals(targetURL)) {
			if (!bIslogin) {
				this.request.getRequestDispatcher("/login.jsp").forward(
						request, response);
				return;
			}
			if (!user.IsContainPriv(username, "dataarchive")) {

				bHavePriveledge = false;

			}

		} else if (targetURL.contains("/DownloadData")) {
			if (!bIslogin) {
				this.request.getRequestDispatcher("/login.jsp").forward(
						request, response);
				return;
			}
			if (!user.IsContainPriv(username, "datadownload")) {

				mapobj.put("CODE", "NOPIVELEDGE");
				bHavePriveledge = false;

			}
		} else if (targetURL.contains("/HandArchive")) {
			if (!bIslogin) {
				this.request.getRequestDispatcher("/login.jsp").forward(
						request, response);
				return;
			}
			if (!user.IsContainPriv(username, "dataarchive")) {
				mapobj.put("CODE", "NOPIVELEDGE");
				bHavePriveledge = false;
			}
		} else if (targetURL.contains("/DataManager")) {
			if (!bIslogin) {
				this.request.getRequestDispatcher("/login.jsp").forward(
						request, response);
				return;
			}
			if (!user.IsContainPriv(username, "dataoperater")) {

				mapobj.put("CODE", "NOPIVELEDGE");
				bHavePriveledge = false;

			}
		} else {
			bHavePriveledge = true;
		}
		userservice.closeService();

		if (!bHavePriveledge) {

			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			JSONObject obj1 = JSONObject.fromObject(mapobj);
			out.write(obj1.toString());
			out.flush();
			out.close();

			return;
		}

		chain.doFilter(request, response);
	}

	private PrintWriter println(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	/***
	 * 
	 */
	@Override
	public void destroy() {
		// this.request.getSession(false).setAttribute(Constants.UserLogin.LOGINUSERMSG,null);

	}

}
