package com.sasmac.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.FeatureDefn;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.gdal.osr.SpatialReference;

import com.sasmac.dbconnpool.ConnPoolUtil;
import com.sasmac.meta.spatialmetadata;
import com.web.util.FileUtil;

public class ExportSHP extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public ExportSHP() {
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
		// doPost(request, response);
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
		// download file

		String downloadFilePath = this.getServletContext().getRealPath(
				"/download");
		File file = new File(downloadFilePath + "\\download.rar");
		response = downloadZip(file, response);
		file.deleteOnExit();

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
		request.setCharacterEncoding("utf-8");
		String objSelec = request.getParameter("objSelec");
		if (objSelec != null) {
			JSONArray jsonArr = JSONArray.fromObject(objSelec);
			int n = jsonArr.size();
			if (n <= 0)
				return;
			Connection conn = ConnPoolUtil.getConnection();
			if (conn == null)
				return;

			String tbname = "tb_sc_product";// request.getParameter("tablename");
			if (tbname == null || tbname.isEmpty())
				return;
			// Object[] param = new Object[3];
			int param;

			QueryRunner qr = new QueryRunner();
			ResultSetHandler<List<spatialmetadata>> rsh = new BeanListHandler<spatialmetadata>(
					spatialmetadata.class);
			String strSQL = "select dataid,id,FileName,FilePath,scenePath,sceneRow,orbitID,satellite,sensor,acquisitionTime,productLevel,cloudPercent,astext(shape) as wktstring from ";
			strSQL += tbname;
			String where = " where dataid=? ";
			// String where = " where satellite=? and productLevel=?";
			String sql = strSQL + where;
			List<spatialmetadata> AllSMDlist = new ArrayList<spatialmetadata>();
			List<spatialmetadata> SMDlist;
			try {
				for (int i = 0; i < n; i++) {
					JSONObject jsonObj = jsonArr.getJSONObject(i);
					param = Integer.parseInt(jsonObj.get("dataid").toString());
					// param[1] = jsonObj.get("scenePath");
					// param[2] = jsonObj.get("sceneRow");
					SMDlist = qr.query(conn, sql, rsh, param);
					if (SMDlist != null)
						AllSMDlist.addAll(SMDlist);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ConnPoolUtil.close(conn, null, null);

			int num = AllSMDlist.size();
			if (num <= 0)
				return;
			String downloadFilePath = this.getServletContext().getRealPath(
					"/download");

			String filename = downloadFilePath + "\\download.shp";
			File tmpfile = new File(filename);
			if (tmpfile.exists())
				tmpfile.delete();
			tmpfile = new File(downloadFilePath + "\\download.shx");
			if (tmpfile.exists())
				tmpfile.delete();
			tmpfile = new File(downloadFilePath + "\\download.prj");
			if (tmpfile.exists())
				tmpfile.delete();
			tmpfile = new File(downloadFilePath + "\\download.dbf");
			if (tmpfile.exists())
				tmpfile.delete();

			boolean b = CreateSHP(AllSMDlist, downloadFilePath
					+ "\\download.shp");
			if (b == false)
				return;

			File file = new File(downloadFilePath + "\\download.rar");
			if (!file.exists()) {
				if (!file.createNewFile())
					return;
			}
			try {
				//
				FileOutputStream fous = new FileOutputStream(file);
				ZipOutputStream zipOut = new ZipOutputStream(fous);

				List<File> files = new ArrayList<File>();
				filename = downloadFilePath + "\\download.shp";
				tmpfile = new File(filename);
				if (tmpfile.exists())
					files.add(tmpfile);
				tmpfile = new File(downloadFilePath + "\\download.shx");
				if (tmpfile.exists())
					files.add(tmpfile);
				tmpfile = new File(downloadFilePath + "\\download.prj");
				if (tmpfile.exists())
					files.add(tmpfile);
				tmpfile = new File(downloadFilePath + "\\download.dbf");
				if (tmpfile.exists())
					files.add(tmpfile);

				FileUtil.zipFile(files, zipOut);
				zipOut.close();
				fous.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			// request.getRequestDispatcher("./servlet/ExportSHP").forward(request,
			// response);
		} else {
			// download file
			/*
			 * String downloadFilePath = this.getServletContext().getRealPath(
			 * "/download"); File file = new File(downloadFilePath +
			 * "\\download.rar"); response = downloadZip(file, response);
			 * file.deleteOnExit();
			 */
		}

		return;
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

	boolean CreateSHP(List<spatialmetadata> SMDList, String shpfilename) {
		if (SMDList == null)
			return false;
		int n = SMDList.size();
		if (n <= 0)
			return false;
		if (shpfilename == null)
			return false;
		if (shpfilename.isEmpty())
			return false;

		ogr.RegisterAll();
		gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "NO");
		gdal.SetConfigOption("SHAPE_ENCODING", "CP936");

		String strDriverName = "ESRI Shapefile";
		org.gdal.ogr.Driver oDriver = ogr.GetDriverByName(strDriverName);
		if (oDriver == null) {
			System.out.println(shpfilename + " ??????\n");
			return false;
		}
		DataSource oDS1 = oDriver.Open(shpfilename);
		if (oDS1 != null)
			oDS1.delete();

		DataSource oDS = oDriver.CreateDataSource(shpfilename, null);
		if (oDS == null) {
			System.out.println("error:" + shpfilename + "创建失败！！");
			return false;
		}

		SpatialReference sr = new SpatialReference();
		// sr.ImportFromEPSG(4326);
		// sr.ImportFromESRI(ppszInput)
		sr.ImportFromWkt("GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]]");
		Layer oLayer = oDS.CreateLayer("Polygon", sr, ogr.wkbPolygon, null);
		if (oLayer == null) {
			System.out.println("\n");
			return false;
		}

		try {
			FieldDefn oField = new FieldDefn("dataid", ogr.OFTInteger);
			oLayer.CreateField(oField);
			// FeatureName
			oField = new FieldDefn("FieldName", ogr.OFTString);
			oField.SetWidth(100);
			oLayer.CreateField(oField);
			oField = new FieldDefn("FilePath", ogr.OFTString);
			oField.SetWidth(254);
			oLayer.CreateField(oField);
			oField = new FieldDefn("scenePath", ogr.OFTInteger);
			oLayer.CreateField(oField);
			oField = new FieldDefn("sceneRow", ogr.OFTInteger);
			oLayer.CreateField(oField);
			oField = new FieldDefn("orbitID", ogr.OFTInteger);
			oLayer.CreateField(oField);
			oField = new FieldDefn("satellite", ogr.OFTString);
			oField.SetWidth(16);
			oLayer.CreateField(oField);
			oField = new FieldDefn("sensor", ogr.OFTString);
			oField.SetWidth(16);
			oLayer.CreateField(oField);
			oField = new FieldDefn("obtainTime", ogr.OFTString);
			oField.SetWidth(20);
			oLayer.CreateField(oField);
			oField = new FieldDefn("cloud", ogr.OFTInteger);
			oLayer.CreateField(oField);
			oField = new FieldDefn("Level", ogr.OFTString);
			oField.SetWidth(16);
			oLayer.CreateField(oField);

			FeatureDefn oDefn = oLayer.GetLayerDefn();
			spatialmetadata md;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i = 0; i < n; i++) {
				md = SMDList.get(i);
				md.getAcquisitionTime();

				Feature oFeature = new Feature(oDefn);

				oFeature.SetField(0, md.getDataId());
				oFeature.SetField(1, md.getFileName());
				oFeature.SetField(2, md.getFilePath());
				oFeature.SetField(3, md.getScenePath());
				oFeature.SetField(4, md.getSceneRow());
				oFeature.SetField(5, md.getOrbitID());
				oFeature.SetField(6, md.getSatellite());
				oFeature.SetField(7, md.getSensor());
				oFeature.SetField(8, formatter.format(md.getAcquisitionTime()));
				oFeature.SetField(9, md.getCloudPercent());
				oFeature.SetField(10, md.getProductLevel());
				Geometry geo = Geometry.CreateFromWkt(md.getwktString());
				oFeature.SetGeometry(geo);
				oLayer.CreateFeature(oFeature);
			}
			oDS.SyncToDisk();
			oDS.delete();
			gdal.GDALDestroyDriverManager();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {

		}

		return true;
	}

	public HttpServletResponse downloadZip(File file,
			HttpServletResponse response) {
		try {
			// ??????????????????
			InputStream fis = new BufferedInputStream(new FileInputStream(
					file.getPath()));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();

			response.reset();

			OutputStream toClient = new BufferedOutputStream(
					response.getOutputStream());
			response.setContentType("application/octet-stream");

			// ???????????????????????????????URLEncoder.encode???????д???
			response.setHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(file.getName(), "UTF-8"));
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				File f = new File(file.getPath());
				f.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return response;
	}
}
