package zy3dms;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gdal.ogr.Layer;

/*
 * servlet调用java类时，当java类中调用了dll时，servlet也会调用失败
 解决方案：
 方法一、将java类中所需的dll放置在javahome/jre/bin中，注意：如果使用myeclipse编写代码时，myeclipse会自带jre，需要放置在其jre/bin中
 方法二、将所需的dll放置在tomcat/bin中
 */

public class Query extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Query() {
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
	 * The doDelete method of the servlet. <br>
	 * 
	 * This method is called when a HTTP delete request is received.
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
	public void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

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

		/*
		 * response.setContentType("text/html"); PrintWriter out =
		 * response.getWriter(); out.println(
		 * "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		 * out.println("<HTML>");
		 * out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		 * out.println("  <BODY>"); out.print("    This is ");
		 * out.print(this.getClass()); out.println(", using the GET method");
		 * out.println("  </BODY>"); out.println("</HTML>"); out.flush();
		 * out.close();
		 */

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

		/*
		 * response.setContentType("text/html"); PrintWriter out =
		 * response.getWriter(); out.println(
		 * "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		 * out.println("<HTML>");
		 * out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		 * out.println("  <BODY>"); out.print("    This is ");
		 * out.print(this.getClass()); out.println(", using the POST method");
		 * out.println("  </BODY>"); out.println("</HTML>"); out.flush();
		 * out.close();
		 */

		// 设置response的编码
		response.setCharacterEncoding("utf-8");
		// 设置request的编码
		request.setCharacterEncoding("utf-8");

		String wktPoly = request.getParameter("wktPoly");
		System.out.println(wktPoly);

		String strSQL = BuildSQL.querySQL(request);
		System.out.println(strSQL);

		String conffilepath = getServletContext().getRealPath("/")
				+ "WEB-INF/classes/com/sasmac/conf/dbConnConf.properties";
		String path3 = request.getSession().getServletContext()
				.getRealPath(request.getRequestURL().toString());

		String query = request.getParameter("query");
		String statistic = request.getParameter("statistic");
		String submitType = request.getParameter("submitType");

		if (query != null) {
			Layer pLayer = GDALmysql
					.ExecuteQuery(strSQL, wktPoly, conffilepath);
			/*
			 * String quyResultJson=pLayer.exporttojson();
			 * request.getSession().setAttribute("quyresult", quyResultJson);
			 */
			// String result = GDALmysql.LayerToGeoJSONFile(pLayer,
			// queryfilepath);
			// request.getSession().setAttribute("result", result);
		} else if (statistic != null) {
			// System.out.println("统计");
			double dblresult = Statistic.statisticArea(conffilepath, strSQL,
					wktPoly);
			BigDecimal bDecimal = new BigDecimal(dblresult);
			request.getSession().setAttribute("resultstatistic",
					bDecimal.toString());
		} else if (submitType.equals("exportShp")) {
			/*
			 * int exportShpResult = GDALmysql.ExportShp(strSQL, wktPoly,
			 * exportShpFile, conffilepath);
			 * request.getSession().setAttribute("exportShpResult",
			 * exportShpResult);
			 */
		}

		response.sendRedirect("../query.jsp");
	}

	/**
	 * The doPut method of the servlet. <br>
	 * 
	 * This method is called when a HTTP put request is received.
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
	public void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Put your code here
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		String path = getServletContext().getRealPath("/");

	}

}
