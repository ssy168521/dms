package com.sasmac.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geotools.data.mysql.MySQLDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStore;
import org.opengis.feature.simple.SimpleFeatureType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sasmac.dbconnpool.ConnPoolUtil;
import com.sasmac.util.Layer2GeoJSON2;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.web.dao.Table;
import com.web.util.PropertiesUtil;

public class TableField extends HttpServlet {

/**
 * 包括表格显示、字段添加、修改、删除、字段编辑操作
 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		String requestURI = request.getRequestURI();
		
		if(requestURI.contains("/tableField")) {//显示数据表
			Connection conn = null;
			String sql = "select * from field_meta01";
			PreparedStatement stmt = null;
			ResultSet rs = null;
			List<Table> tables = new ArrayList<Table>();
			try {
				conn = ConnPoolUtil.getConnection();
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery(sql);
				
				 while(rs.next()){
					Table table = new Table();
					int fieldid=rs.getInt("id");
	                String fieldName=rs.getString("fieldName");
	                String fieldTypeName=rs.getString("fieldTypeName");                
	                //构造json
	                table.setFieldid(fieldid);
	                table.setFieldName(fieldName);
	                table.setFieldTypeName(fieldTypeName);
	                tables.add(table);
		            }
				JSONArray jsonArray = JSONArray.fromObject(tables);
				String jsonTableList = jsonArray.toString();
				// String jsonTableList ="[{\"fieldName\":\"CreaDate\",\"fieldTypeName\":\"20170821\",\"fieldid\":\"/metadata/Esri/CreaDate\"},{\"fieldName\":\"Process\",\"fieldTypeName\":\"BuildPyramids H:\\水利部第五六批成果\\第五六批成果\\J46D001001.TIF -1 NONE NEAREST DEFAULT 75 OVERWRITE\",\"fieldid\":\"/metadata/Esri/DataProperties/lineage/Process\"}]";
				response.getWriter().print(jsonTableList);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ConnPoolUtil.close(conn, null, null);
			}
			
		} else if(requestURI.contains("addFiled1")) {//增加字段
			Connection conn = null;
			Statement stmt = null;
			PrintWriter out = response.getWriter();
			String fieldName = request.getParameter("fieldName");
			String fieldType = request.getParameter("fieldType");
			String sql1 = "insert into field_meta01 values( " +null+",\""+fieldName+"\","
					+"\""+fieldType+"\");";		
			try {
				conn = ConnPoolUtil.getConnection();
				stmt = conn.createStatement();
				boolean b1 = stmt.execute(sql1);
				//boolean b2 = stmt.execute(sql2);
				if(!b1 ){
					out.print("true");
				}else{
					out.print("添加字段出错！！");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				ConnPoolUtil.close(conn, null, null);
			}
		}else if(requestURI.contains("tableCreate")){//创建新数据表
			Connection conn = null;
		    Statement stmt = null;
		 try {
			conn = ConnPoolUtil.getConnection();
			stmt = conn.createStatement();
			PrintWriter out = response.getWriter();
			
			String tabName = request.getParameter("tabName");
			String tabName1=tabName.substring(1,tabName.length()-1);
			//判断表名是否重复
			String sql = "SELECT table_name FROM information_schema.TABLES WHERE table_name =\""+tabName1+"\";";
			ResultSet result= stmt.executeQuery(sql);
			if(result.next()){
				out.print("表名重复，请重新输入！");
			} else {
				out.print("表名正确！");
			}
			//简单要素类型用于设置名称、增加属性和几何属性
			SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
			//设置表名
			b.setName(tabName1);
			String objSelec = request.getParameter("objSelec");
			JSONArray jsonArr =JSONArray.fromObject(objSelec);
			for (int i=0;i < jsonArr.size();i++) {
				JSONObject jsonObj=jsonArr.getJSONObject(i);
				String fieldName=jsonObj.get("fieldName").toString(); //字段名
				String fieldTypeName=jsonObj.get("fieldTypeName").toString(); //数据类型
				if(fieldTypeName.contains("Integer")){
					b.add(fieldName,Integer.class);
				}else if(fieldTypeName.contains("String")){
					b.add(fieldName,String.class);
				}else if(fieldTypeName.contains("Double")){
					b.add(fieldName,Double.class);
				}else if(fieldTypeName.contains("Timestamp")){
					b.add(fieldName,Timestamp.class);
				}else if(fieldTypeName.contains("Polygon")){
					b.add(fieldName,Polygon.class);
				}else if(fieldTypeName.contains("Point")){
					b.add(fieldName,Point.class);
				}else if(fieldTypeName.contains("LineString")){
					b.add(fieldName,LineString.class);
				}
			}
			//建立类型
			final SimpleFeatureType FLAG = b.buildFeatureType();
			SimpleFeatureType schema = FLAG;
			MySQLDataStoreFactory factory1 = new MySQLDataStoreFactory();
			String conffilepath = "";
			//String conffilepath2 = "E:/MyEclipse2014 workspace/archives/libs/dbConnConf.properties";

			try {
				conffilepath = Layer2GeoJSON2.class.getClassLoader()
						.getResource("").toURI().getPath();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block e.printStackTrace();
			}
			conffilepath += "com/sasmac/conf/dbConnConf.properties";
			PropertiesUtil util = new PropertiesUtil(conffilepath);
			Map<String,Object> params2 = new HashMap<String, Object>();
			params2.put("dbtype", util.getProperty("dbtype"));
			params2.put("host", util.getProperty("host"));
			params2.put("port", util.getProperty("port"));
			params2.put("database", util.getProperty("database"));
			params2.put("user", util.getProperty("user"));
			params2.put("passwd", util.getProperty("password"));
			JDBCDataStore ds;
			try {
				ds = (JDBCDataStore) factory1.createDataStore(params2);
				if(ds !=null){
					out.print("系统连接到位于localhost的空间数据库成功！");
				}else{
					out.print("系统连接到位于localhost的空间数据库失败！请检查相关参数！");
				}
				ds.createSchema(schema);//在mysql创建表
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnPoolUtil.close(conn, null, null);
		}
			
		}else if(requestURI.contains("deleteSelectFiled")) {//删除选择的行
		
			Connection conn = null;
			Statement stmt = null;
			try {
				conn = ConnPoolUtil.getConnection();
				stmt = conn.createStatement();
				PrintWriter out = response.getWriter();
				String objSelec = request.getParameter("objSelec");
				JSONArray jsonArr =JSONArray.fromObject(objSelec);
				//String columnName= request.getParameter("columnName");
				for (int i=0;i < jsonArr.size();i++) {
					JSONObject jsonObj=jsonArr.getJSONObject(i);
					String fieldName=jsonObj.get("fieldName").toString();
					String sql =  "delete from field_meta01 where fieldName =\""+fieldName+"\"";
					boolean result= stmt.execute(sql);
					if(!result){
						out.print("true");
					} else {
						out.print("字段"+jsonObj.get("fieldName").toString()+"删除失败！");
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				ConnPoolUtil.close(conn, null, null);
			}
				
		} else if(requestURI.contains("deleteFiled")) {//删除一行数据
			Connection conn = null;
			Statement stmt = null;
			try {
				conn = ConnPoolUtil.getConnection();
				stmt = conn.createStatement();
				PrintWriter out = response.getWriter();
				String fieldName= request.getParameter("fieldName");
				String sql = "delete from field_meta01 where fieldName =\""+fieldName+"\"";
				boolean result= stmt.execute(sql);
				if(!result) {
					out.print("true");
				} else {
					out.print("用户删除失败！");
					System.out.println("用户删除失败！");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				ConnPoolUtil.close(conn, null, null);
			}
		}else if(requestURI.contains("producttype")){//下拉框显示产品类型
			Connection conn = null;
			Statement stmt = null;
			PrintWriter out = response.getWriter();	
			//构造list数组，接收数据库数据
			List<String> list = new ArrayList<String>();
			try {  // 数据库连接池
				conn = ConnPoolUtil.getConnection();
				stmt = conn.createStatement();
				String sql3="select distinct productName from archiveconfig;";
				ResultSet result= stmt.executeQuery(sql3);
				//构造json字符串

				while(result.next()){
					String productName = result.getString("productName");
					list.add("{\"productName\":\"");
					list.add(productName);
					list.add("\"}");
				}
				//JSONArray jsonArray = JSONArray.fromObject(list);
				//String jsonTable = jsonArray.toString();
				String jsonTable = "[{\"productName\":\"分景产品\"},{\"productName\":\"标准分幅产品\"},{\"productName\":\"sc产品\"}]";
				out.print(jsonTable);
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				ConnPoolUtil.close(conn, null, null);
			}
		}else if(requestURI.contains("fieldedit")){	//字段编辑，添加字段与节点想对应，并将对应关系存于数据库	
			Connection conn = null;
			Statement stmt = null;
			PrintWriter out = response.getWriter();
			//从前端获取序列化json数据
			String json = request.getParameter("objSelec");
			String productName1 = request.getParameter("productName");
			String productTable1 = request.getParameter("productTable");
			String productName = productName1.substring(1,productName1.length()-1);
			String productTable = productTable1.substring(1,productTable1.length()-1);
			JSONObject j = JSONObject.fromObject(json);
		    //根据key获取相应的value
			String fieldName = j.getString("fieldName");
			String nodeName =j.getString("nodeName");
			String nodepath = j.getString("nodepath");
			String xmlfile1 = request.getParameter("fileName");
			String xmlfile=xmlfile1.substring(1,xmlfile1.length()-1);
			String sql2 = "insert into archiveconfig(fid,fieldName,productName,productTable,nodeName,nodepath,xmlfile) "
					+ "values( " +null+",\""+fieldName+"\",\""+productName+"\",\""+productTable+"\",\""+nodeName+"\",\""+nodepath+"\",\""+xmlfile+"\");";		
			try {
				conn = ConnPoolUtil.getConnection();
				stmt = conn.createStatement();
				boolean b1 = stmt.execute(sql2);
				if(!b1 ){
					out.print("true");
				} else {
					out.print("添加字段出错！！");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				ConnPoolUtil.close(conn, null, null);
			}
		}else if(requestURI.contains("fieldModify")) {//字段修改	
		
			String fieldid = request.getParameter("fieldid");
			String nfieldName = request.getParameter("nfieldname");
			String dataType = request.getParameter("dataType");

			Connection conn = null;
			Statement stmt = null;
			PrintWriter out = response.getWriter();
			String sql1 = "update field_meta01 set `fieldName` = "+"\""+nfieldName+"\""+",`fieldTypeName` ="
			        +"\""+dataType+"\""+" where id="+fieldid;
			System.out.println(sql1);
			try {
				conn = ConnPoolUtil.getConnection();
				stmt = conn.createStatement();
				boolean b1 = stmt.execute(sql1);
				
				if(!b1){
					out.print("true");
				} else {
					out.print("修改字段出错！！");
				}
			} catch (SQLException e) {
				out.print("主键必须唯一！！");
				e.printStackTrace();
			} finally {
				ConnPoolUtil.close(conn, null, null);
			}
		}
	}

//	public String toJson(List<String> str){
//        StringBuilder json = new StringBuilder();
//
//        if (str == null)
//        {
//            return "null";
//
//        }
//        json.append("[");
//        for(String item:str)
//        {
//            json.append("{\"Name\":\"");
//            json.append(item);
//            json.append("\"},");
//        }
//
//        return json.toString().substring(0, json.toString().lastIndexOf(",")) + "]";
//    }
	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
		
	}

}
