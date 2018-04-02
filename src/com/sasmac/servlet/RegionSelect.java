package com.sasmac.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.sasmac.common.RegionManage;
import com.sasmac.common.RegionManage.RegionType;

public class RegionSelect extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public RegionSelect() {
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

		request.setCharacterEncoding("utf-8"); // 设置编码
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String[] seleProvince = request.getParameterValues("seleProvince[]"); // 注意参数带上[]
		String[] seleCity = request.getParameterValues("seleCity[]");
		String[] seleCounty = request.getParameterValues("seleCounty[]");
		// DebugFun.showParams(request);
		PrintWriter out = response.getWriter();
		if (seleCounty != null) {
			String strOut = "{";
			RegionManage regimana = new RegionManage();
			String[] wkts = regimana
					.getRegionWkt(seleCounty, RegionType.County);//
			JSONArray jsonArraywkt = JSONArray.fromObject(wkts);
			String wktString = jsonArraywkt.toString();
			strOut += "\"RegionWkts\":" + wktString + "}";

			out.println(strOut);

		} else if (seleCity != null) {
			String strOut = "{";
			RegionManage regimana = new RegionManage();
			for (int i = 0; i < seleCity.length; i++) {
				List<String> countyList = regimana
						.get2ndRegByRegion(seleCity[i]);
				JSONArray jsonArray = JSONArray.fromObject(countyList);
				String jsonsString = jsonArray.toString();
				strOut += "\"" + seleCity[i] + "\":" + jsonsString + ",";
			}
			strOut = strOut.substring(0, strOut.lastIndexOf(",")) + "}";
			strOut = "{\"RegionNames\":" + strOut + ",";
			String[] wkts = regimana.getRegionWkt(seleCity, RegionType.City);//
			JSONArray jsonArraywkt = JSONArray.fromObject(wkts);
			String wktString = jsonArraywkt.toString();
			strOut += "\"RegionWkts\":" + wktString + "}";

			out.println(strOut);
		} else if (seleProvince != null && seleCity == null) {
			String strOut = "{";
			RegionManage regimana = new RegionManage();
			for (int i = 0; i < seleProvince.length; i++) {
				List<String> cityList = regimana
						.get2ndRegByRegion(seleProvince[i]);
				JSONArray jsonArray = JSONArray.fromObject(cityList);
				String jsonsString = jsonArray.toString();
				strOut += "\"" + seleProvince[i] + "\":" + jsonsString + ",";
			}
			strOut = strOut.substring(0, strOut.lastIndexOf(",")) + "}";

			strOut = "{\"RegionNames\":" + strOut + ",";
			String[] wkts = regimana.getRegionWkt(seleProvince,
					RegionType.Province);//
			JSONArray jsonArraywkt = JSONArray.fromObject(wkts);
			String wktString = jsonArraywkt.toString();
			strOut += "\"RegionWkts\":" + wktString + "}";
			out.println(strOut);
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
