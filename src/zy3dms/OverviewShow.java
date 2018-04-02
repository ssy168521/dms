package zy3dms;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gdal.ogr.Feature;
import org.gdal.ogr.Layer;

/**
 * 
 * @ClassName OverviewShow
 * @Description ����ͼչʾ
 * @author SUNJ
 * @date 2017��1��16������2:14:46
 */

public class OverviewShow extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public OverviewShow() {
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

		// ����response�ı���
		response.setCharacterEncoding("utf-8");
		// ����request�ı���
		request.setCharacterEncoding("utf-8");
		String wktPoly = request.getParameter("wktPoly");
		System.out.println(wktPoly);

		String strSQL = BuildSQL.querySQL(request);
		System.out.println(strSQL);

		String conffilepath = getServletContext().getRealPath("/")
				+ "conf/dbConnConf.properties";
		Layer pLayer = GDALmysql.ExecuteQuery(strSQL, wktPoly, conffilepath);
		Feature pFeature = null;
		pLayer.ResetReading();

		ArrayList<String> arrlistFiles = new ArrayList<String>();
		while ((pFeature = pLayer.GetNextFeature()) != null) {
			String strDataName = pFeature.GetFieldAsString("F_DATANAME");
			String strDataID = pFeature.GetFieldAsString("F_DATAID");
			System.out.println(strDataName);
			String[] strtmp = strDataName.split("-");
			String strpath = "E:\\TestData\\ZY3BM\\" + strtmp[6] + "\\"
					+ strDataName + strDataID + ".png";
			File pFile = new File(strpath);
			if (pFile.exists()) {
				arrlistFiles.add(strpath);
			} else {
				continue;
			}
			// System.out.println(strpath);
		}
		int nExistFileCount = arrlistFiles.size();
		String[] strOverviewfiles = new String[nExistFileCount];
		String[] strOverviewExtents = new String[nExistFileCount];
		for (int i = 0; i < nExistFileCount; i++) {
			String strpath = arrlistFiles.get(i);
			// �˴���windows·��ת����webӦ��·��
			// ��E:\\TestData\\123.pngת����/TestData/123.png��������webӦ�õ���
			// ��Ҫע�������Ҫ��tomcat��������·����service.xml������host-----context
			String strtmp = strpath.substring(strpath.indexOf("\\"));
			String strnewpath = strtmp.replaceAll("\\\\", "/");
			// System.out.println(strnewpath);

			strOverviewfiles[i] = strnewpath;
			strOverviewExtents[i] = GDALimage.GetImageExtent(strpath);
			// System.out.println(strOverviewExtents[i]);
		}

		request.getSession().setAttribute("strOverviewfiles", strOverviewfiles);
		request.getSession().setAttribute("strOverviewExtents",
				strOverviewExtents);
		// request.getSession().setAttribute("resultMosaic", true);
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
	}

}
