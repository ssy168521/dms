package com.sasmac.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import zy3dms.GDALimage;
import zy3dms.GDALimage2;
import zy3dms.ImageExt;

import com.sun.prism.Image;
import com.web.common.Constants;
import com.web.util.DbUtils;
import com.web.util.PropertiesUtil;

public class Loadoverview extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Loadoverview() {
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
		// 设置request编码
		request.setCharacterEncoding("utf-8");
		// String
		// conffilepath=ClassLoader();getServletContext().getRealPath("/");
		String conffilepath = "";
		try {
			conffilepath = Loadoverview.class.getClassLoader().getResource("/")
					.toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		PropertiesUtil propertiesUtil = new PropertiesUtil(conffilepath
				+ Constants.STR_CONF_PATH);
		String stroverviewfilepath = propertiesUtil //存放快视图路径  E:\database\overview\
				.getProperty("overviewfilepath");

		String objSelec = request.getParameter("objSelec"); //从jsp页面获取传递参数 
		JSONArray jsonArr = JSONArray.fromObject(objSelec); //objSelec 转 json
		//17年
		String satellite = "";
		String ProductLevel = "";
		String photoDate = "";
		String orbit = "";
		String Sensor = "";		 
		// String.format("%06d", spatialmeta.getOrbitID())
		String storagePath = "";
		String filename=""; 
		List<String> arrlistFiles = new ArrayList<String>();
		List<String> extarr = new ArrayList<String>();
		List<String> dataidarr = new ArrayList<String>();
		for (int i = 0; i < jsonArr.size(); i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			if(jsonObj.get("productLevel")!= null){//判断是否是表tif2db1的数据,tif2db1没有该字段
				satellite = jsonObj.get("satellite").toString();//从json数据中获取键为satellite的值 
				ProductLevel = jsonObj.get("productLevel").toString();
				photoDate = jsonObj.get("acquisitionTime").toString();
				filename=jsonObj.get("FileName").toString();
				photoDate = photoDate.replace("/", "-");
				orbit = String.format("%06d", jsonObj.get("orbitID"));
				Sensor = jsonObj.get("sensor").toString();
				//storagePath为快视图路径
				storagePath = File.separator + satellite + File.separator 
						+ ProductLevel + File.separator + photoDate
						+ File.separator + orbit + File.separator + Sensor
						+ File.separator + filename
						+ ".png";					
	            //图像范围和空间参考（'wkid':4326）
				String strExt = GDALimage.GetImageExtent(stroverviewfilepath
						+ storagePath);
				extarr.add(strExt);					
			}else{
				filename=jsonObj.get("FileName").toString();
				satellite = jsonObj.get("FileName").toString().substring(0,3);
				//storagePath为快视图路径
				storagePath = File.separator + satellite
						+ File.separator + filename
						+ ".png";	
	            //图像范围和空间参考（'wkid':4326）
				String strExt = GDALimage2.GetImageExtent(stroverviewfilepath
						+ storagePath);
				extarr.add(strExt);	
			}	
			dataidarr.add(jsonObj.get("dataid").toString());
			//快视图路径
			String strnewpath = ( storagePath).replaceAll("\\\\", "/");
			arrlistFiles.add(strnewpath);		
		}
		JSONArray jsonArray1 = JSONArray.fromObject(arrlistFiles);//路径
		JSONArray jsonArray2 = JSONArray.fromObject(extarr);     //数组转为json数据，范围
		JSONArray jsonArray3 = JSONArray.fromObject(dataidarr);  //日期id
		// {"path":[],"ext":[],"dataid":[]}
		String strobj = "{\"path\":" + jsonArray1.toString() + "," + "\"ext\":"
				+ jsonArray2.toString() + "," + "\"dataid\":"
				+ jsonArray3.toString() + "}";

		PrintWriter out = response.getWriter();
		out.print(strobj);

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
