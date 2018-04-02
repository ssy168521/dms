package zy3dms;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Mosaic extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Mosaic() {
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

		// 设置response的编码
		response.setCharacterEncoding("utf-8");
		// 设置request的编码
		request.setCharacterEncoding("utf-8");

		GDALMosaic jnItest = new GDALMosaic();
		String strfoler = "E:\\TestData\\JS111R4";
		// String strfoler="E:\\TestData\\JS111";
		File file = new File(strfoler);
		File[] tempList = file.listFiles();
		ArrayList<String> strArr = new ArrayList<String>();
		int nJPGcount = 0;
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				// 读取某个文件夹下的所有文件
				String strName = tempList[i].getName();
				String prefixString = strName.substring(strName
						.lastIndexOf(".") + 1);
				if (prefixString.compareToIgnoreCase("tif") == 0) {
					strArr.add(tempList[i].getPath());
					nJPGcount++;
				}
			}
		}

		// String[] strArrFiles=new String[2];
		// strArrFiles[0]="E:\\TestData\\MosaicImage\\7927.jpg";
		// strArrFiles[1]="E:\\TestData\\MosaicImage\\7933.jpg";

		// servlet运行时的路径并不是在项目路径，而是tomcat的webapp下
		// String
		// filepath=getServletContext().getRealPath("/")+"data/temp.geojson";

		String[] strArrFiles = new String[nJPGcount];
		strArr.toArray(strArrFiles);

		String strOutFile = getServletContext().getRealPath("/")
				+ "img/test.png";
		int result = jnItest.Mosaic(strArrFiles, strOutFile);
		System.out.println(result);

		String strExtent = GDALimage.GetImageExtent(strOutFile);
		System.out.println(strExtent);

		/*
		 * String
		 * strXmlFile=getServletContext().getRealPath("/")+"img/test.png.aux.xml"
		 * ; //File xmlFile=new File(strXmlFile); DocumentBuilderFactory
		 * domfactory=DocumentBuilderFactory.newInstance(); try {
		 * DocumentBuilder dombuilder=domfactory.newDocumentBuilder(); Document
		 * doc =dombuilder.parse(strXmlFile); String
		 * str=doc.getElementsByTagName
		 * ("GeoTransform").item(0).getFirstChild().getNodeValue();
		 * System.out.println(str); String
		 * str2=doc.getElementsByTagName("PAMDataset"
		 * ).item(0).getFirstChild().getNodeValue(); System.out.println(str2);
		 * 
		 * String[] strTransform=str.split(","); for (int i = 0; i <
		 * strTransform.length; i++) { System.out.println(strTransform[i]); } }
		 * catch (ParserConfigurationException e) { e.printStackTrace(); } catch
		 * (SAXException e) { e.printStackTrace(); }
		 */

		request.getSession().setAttribute("strExtent", strExtent);
		request.getSession().setAttribute("resultMosaic", result);
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
