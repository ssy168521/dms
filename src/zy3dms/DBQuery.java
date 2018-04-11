package zy3dms;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sasmac.dbconnpool.ConnPoolUtil;
import com.sasmac.util.Layer2GeoJSON;
import com.vividsolutions.jts.io.ParseException;

import com.sasmac.common.DataModel;

public class DBQuery extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public DBQuery() {
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

		// 设置response的编码
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		// 设置request的编码
		request.setCharacterEncoding("utf-8");
		String wktPoly = request.getParameter("wktPoly");
		String[] zy3_sensorradio = request
				.getParameterValues("zy3_sensorradio");

		String[] zy3_radio = request.getParameterValues("zy3_radio");
		String ProductType = request.getParameter("ProductType");

		// 数据库表名
		if (ProductType == null) {
			return;
		}
		String tbname = DataModel.GetProductTabName(ProductType);

		if (tbname == "")
			return;
		// System.out.println(wktPoly);
		String strWherClause = "";

		if (ProductType.compareToIgnoreCase("分幅DOM") == 0) {
			strWherClause = BuildSQL.querySQLbyDOMScene(request);

		} else if (ProductType.compareToIgnoreCase("分景DOM") == 0) {
			strWherClause = BuildSQL.querySQLbySC(request,true);
		}
		else 
		{
			strWherClause = BuildSQL.querySQLbySC(request,false);
		}

		String geojsonstr = "";
		// String geojsonstr2 = "";// 2018\1\2
		Layer2GeoJSON layer2GeoJson = new Layer2GeoJSON();
		// Layer2GeoJSON2 layer2GeoJson2 = new Layer2GeoJSON2();
		int nsensor = 0;
		if (zy3_sensorradio != null) {
			nsensor = zy3_sensorradio.length;
		}

		ArrayList<String> sensorarr = new ArrayList<String>();

		if (nsensor == 1) { // 配套查询
			if (zy3_sensorradio[0].compareToIgnoreCase("NADMUX") == 0) {
				sensorarr.add("NAD");
				sensorarr.add("MUX");
			} else if (zy3_sensorradio[0].compareToIgnoreCase("SXZ") == 0) {
				sensorarr.add("NAD");
				sensorarr.add("FWD");
				sensorarr.add("BWD");
			} else if (zy3_sensorradio[0].compareToIgnoreCase("SS") == 0) {
				sensorarr.add("NAD");
				sensorarr.add("FWD");
				sensorarr.add("BWD");
				sensorarr.add("MUX");
			}
			String sataellite = "";
			if (zy3_radio != null && zy3_radio.length >= 1)
				sataellite = zy3_radio[0];

			try {

				java.sql.Connection conn = ConnPoolUtil.getConnection();
				if (conn == null)
					return;

				geojsonstr = layer2GeoJson.ToGeoJSONJDBC(conn, wktPoly,
						strWherClause, sataellite, (String[]) (sensorarr
								.toArray(new String[sensorarr.size()])));
				ConnPoolUtil.close(conn, null, null);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			try {
				// 获取geojson数据
				geojsonstr = layer2GeoJson.ToGeoJSON(tbname, wktPoly,
						strWherClause);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		PrintWriter out = response.getWriter();
		out.print(geojsonstr);// 在web端绘制

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
