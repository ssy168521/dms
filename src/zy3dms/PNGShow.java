package zy3dms;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PNGShow extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PNGShow() {
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

		// ����response�ı���
		response.setCharacterEncoding("utf-8");
		// ����request�ı���
		request.setCharacterEncoding("utf-8");

		// GDALMosaic jnItest=new GDALMosaic();
		// String strfoler="E:\\TestData\\JS111R4_GeomRect";
		String strfoler = "E:\\TestData\\JS111_GeomRect";
		File file = new File(strfoler);
		File[] tempList = file.listFiles();
		ArrayList<String> strArr = new ArrayList<String>();
		int nPNGcount = 0;
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				// ��ȡĳ���ļ����µ������ļ�
				String strName = tempList[i].getName();
				String prefixString = strName.substring(strName
						.lastIndexOf(".") + 1);
				if (prefixString.compareToIgnoreCase("PNG") == 0) {
					strArr.add(tempList[i].getPath());
					nPNGcount++;
				}
			}
		}
		String[] strPNGfiles = new String[nPNGcount];
		String[] strPNGExtents = new String[nPNGcount];
		for (int i = 0; i < nPNGcount; i++) {
			String strpath = strArr.get(i);
			// �˴���windows·��ת����webӦ��·��
			// ��E:\\TestData\\123.pngת����/TestData/123.png��������webӦ�õ���
			// ��Ҫע�������Ҫ��tomcat��������·����service.xml������host-----context
			String strtmp = strpath.substring(strpath.indexOf("\\"));
			String strnewpath = strtmp.replaceAll("\\\\", "/");
			System.out.println(strnewpath);

			strPNGfiles[i] = strnewpath;
			strPNGExtents[i] = GDALimage.GetImageExtent(strpath);
		}
		String strtest = "ssdfadadf";
		request.setAttribute("strtest", strtest);
		request.setAttribute("strPNGfiles", strPNGfiles);
		request.setAttribute("strPNGExtents", strPNGExtents);
		request.getRequestDispatcher("../query.jsp").forward(request, response);

		/*
		 * String[] strfileurl=new String[2]; String[] strfileextent=new
		 * String[2]; strfileurl[0]=
		 * "/TestData/JS111_Rect/ZY3-FWD-E116.0-N34.6-896-138-20151206-464867933.png"
		 * ; strfileextent[0]=
		 * "{'xmin':115.69457726605002,'ymin':34.341948550349585,'xmax':116.38871572516727,'ymax':34.9517620411,'spatialReference':{'wkid':4326}}"
		 * ; strfileurl[1]=
		 * "/TestData/JS111_Rect/ZY3-FWD-E116.1-N35.0-896-137-20151206-454867927.png"
		 * ; strfileextent[1]=
		 * "{'xmin':115.79473141884999,'ymin':34.73755279689772,'xmax':116.49252364386103,'ymax':35.3477991339,'spatialReference':{'wkid':4326}}"
		 * ;
		 * 
		 * request.setAttribute("strPNGfiles", strfileurl);
		 * request.setAttribute("strPNGExtents", strfileextent);
		 */

		/*
		 * request.getSession().setAttribute("strtest", strtest);
		 * request.getSession().setAttribute("strPNGfiles", strPNGfiles);
		 * request.getSession().setAttribute("strPNGExtents", strPNGExtents);
		 * response.sendRedirect("../addimage.jsp");
		 */

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
