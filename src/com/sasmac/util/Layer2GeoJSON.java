package com.sasmac.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JavaIdentifierTransformer;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.mysql.MySQLDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.jdbc.JDBCDataStore;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import com.sasmac.meta.spatialmetadata;
//import org.opengis.geometry.Geometry;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.web.util.PropertiesUtil;

public class Layer2GeoJSON {

	private Logger myLogger = LogManager.getLogger("mylog");

	public Filter getGeoFilter(FilterFactory2 ff, // 构建拓扑查询的filter
			String geometryAttributeName, Geometry refGeo) {

		return ff.intersects(ff.property(geometryAttributeName),
				ff.literal(refGeo));

	}

	public String ToGeoJSONJDBC2(java.sql.Connection conn, String wkt,
			String whereClause) throws ParseException {

		QueryRunner qr = new QueryRunner();
		ResultSetHandler<List<spatialmetadata>> rsh = new BeanListHandler<spatialmetadata>(
				spatialmetadata.class);

		String strWhereGeo = "";
		if (!wkt.isEmpty())
			strWhereGeo = "ST_Intersects(shape,ST_GeomFromText('" + wkt + "')";
		if (!strWhereGeo.isEmpty()) {
			if (!whereClause.isEmpty()) {
				whereClause += " and " + strWhereGeo;
			} else {
				whereClause += strWhereGeo;
			}
		}

		String strSQL = "select astext(shape) as wktstring,dataid,FileName,"
				+ "ArchiveTime from " + "tb_sc_product " + whereClause;

		List<spatialmetadata> SMDlist = null;
		try {
			SMDlist = qr.query(conn, strSQL, rsh);
			if (SMDlist == null)
				return "";
			int num = SMDlist.size();
			if (num <= 0)
				return "";

			// JSONArray jsonArray = JSONArray.fromObject(SMDlist);

			String strLayerJSON = "{\"type\" :\"FeatureCollection\",\"features\" :[";
			boolean b = false;
			String json = null;
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new JsonDateValueProcessor());
			JsonJavaIdentifierTransformer a = new JsonJavaIdentifierTransformer();
			jsonConfig
					.setJavaIdentifierTransformer(new JavaIdentifierTransformer() {

						@Override
						public String transformToJavaIdentifier(String key) {
							if (key.compareToIgnoreCase("fileName") == 0) {
								key = "FileName";
							}
							// TODO Auto-generated method stub
							return null;
						}
					});
			jsonConfig.setExcludes(new String[] { "wktstring" });
			// jsonConfig.
			for (int i = 0; i < num; i++) {
				if (b) {
					strLayerJSON += ",";
				}
				b = true;
				String feature = "{\"type\":\"Feature\",\"geometry\":";
				spatialmetadata md = SMDlist.get(i);

				try {
					WKTReader reader = new WKTReader();
					Geometry geometry = reader.read(md.getwktString());
					StringWriter writer = new StringWriter();
					GeometryJSON g = new GeometryJSON();
					g.write(geometry, writer);
					json = writer.toString();
					feature += json;
					feature += ",\"properties\":";
					JSONObject json1 = JSONObject.fromObject(md, jsonConfig);
					String property = json1.toString();
					feature += property;
					feature += "}";
					strLayerJSON += feature;

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			strLayerJSON += "]}";
			return strLayerJSON;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	public String ToGeoJSONJDBC(java.sql.Connection conn, String wkt,
			String whereClause, String satellite, String[] sensorarr)
			throws ParseException {
		int ncnt = sensorarr.length;
		if (sensorarr.length <= 1)
			return "";
		QueryRunner qr = new QueryRunner();
		ResultSetHandler<List<spatialmetadata>> rsh = new BeanListHandler<spatialmetadata>(
				spatialmetadata.class);
		String sumsql1 = "";
		String sumsql2 = "";
		String strSensorwhere = "(";

		for (int i = 0; i < ncnt; i++) {
			sumsql1 += "sum(sensor='" + sensorarr[i] + "') as " + sensorarr[i];
			strSensorwhere += "sensor='" + sensorarr[i] + "'";
			if (i != ncnt - 1)
				strSensorwhere += " or ";
			if (i != ncnt - 1)
				sumsql1 += ",";

			sumsql2 += "tmp." + sensorarr[i] + ">=1";
			if (i != ncnt - 1)
				sumsql2 += " and ";
		}
		strSensorwhere += ")";

		String strWheresatellite = "";// (sensor='NAD' or sensor='MUX')

		if (satellite == "") {
			strWheresatellite = " (satellite = 'zy3-1' or satellite = 'zy3-2') ";
		} else {
			strWheresatellite = " (satellite = '" + satellite + "') ";
		}

		if (!strWheresatellite.isEmpty()) {
			if (!whereClause.isEmpty()) {
				whereClause += " and " + strWheresatellite;
			} else {
				whereClause += strWheresatellite;
			}
		}

		String strWhereGeo = "";
		if (!wkt.isEmpty())
			strWhereGeo = "ST_Intersects(shape,ST_GeomFromText('" + wkt + "')";
		if (!strWhereGeo.isEmpty()) {
			if (!whereClause.isEmpty()) {
				whereClause += " and " + strWhereGeo;
			} else {
				whereClause += strWhereGeo;
			}
		}
		if (!whereClause.isEmpty()) {
			whereClause += " and ";
			whereClause += strSensorwhere;
		}
		String whereClause1 = "";
		if (!whereClause.isEmpty()) {
			if (!whereClause.startsWith(" where")) {
				whereClause = " where " + whereClause;
			}
			;
			whereClause1 = whereClause;
			whereClause += " and ";
		}

		String strSQL = "select astext(shape) as wktstring,dataid,FileName,FilePath,sceneRow,scenepath,orbitid,satellite,sensor,acquisitionTime,"
				+ "ArchiveTime,productLevel,cloudPercent from "
				+ "tb_sc_product "
				+ whereClause
				+ " (satellite,orbitID,sceneRow) in (select satellite,orbitID,sceneRow from "
				+ "(select satellite,orbitID,sceneRow,"
				+ sumsql1
				+ " from tb_sc_product "
				+ whereClause1
				+ " group by satellite,orbitID,sceneRow having count(orbitID) >1)as tmp where "
				+ sumsql2 + ")";

		List<spatialmetadata> SMDlist = null;
		try {
			SMDlist = qr.query(conn, strSQL, rsh);
			if (SMDlist == null)
				return "";
			int num = SMDlist.size();
			if (num <= 0)
				return "";

			// JSONArray jsonArray = JSONArray.fromObject(SMDlist);

			String strLayerJSON = "{\"type\" :\"FeatureCollection\",\"features\" :[";
			boolean b = false;
			String json = null;
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new JsonDateValueProcessor());
			JsonJavaIdentifierTransformer a = new JsonJavaIdentifierTransformer();
			jsonConfig
					.setJavaIdentifierTransformer(new JavaIdentifierTransformer() {

						@Override
						public String transformToJavaIdentifier(String key) {
							if (key.compareToIgnoreCase("fileName") == 0) {
								key = "FileName";
							}
							// TODO Auto-generated method stub
							return null;
						}
					});
			jsonConfig.setExcludes(new String[] { "wktstring" });
			// jsonConfig.
			for (int i = 0; i < num; i++) {
				if (b) {
					strLayerJSON += ",";
				}
				b = true;
				String feature = "{\"type\":\"Feature\",\"geometry\":";
				spatialmetadata md = SMDlist.get(i);

				try {
					WKTReader reader = new WKTReader();
					Geometry geometry = reader.read(md.getwktString());
					StringWriter writer = new StringWriter();
					GeometryJSON g = new GeometryJSON();
					g.write(geometry, writer);
					json = writer.toString();
					feature += json;
					feature += ",\"properties\":";
					JSONObject json1 = JSONObject.fromObject(md, jsonConfig);
					String property = json1.toString();
					feature += property;
					feature += "}";
					strLayerJSON += feature;

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			strLayerJSON += "]}";
			return strLayerJSON;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 获取geojson
	 * 
	 * @param LayerName
	 *            表名
	 * @param wkt
	 * @param WhereClause
	 * @return
	 * @throws ParseException
	 */
	public String ToGeoJSON(String LayerName, String wkt, String WhereClause)
			throws ParseException {

		JDBCDataStore pgDatastore = null;
		MySQLDataStoreFactory factory1 = new MySQLDataStoreFactory();
		FeatureSource fsBC1;

		String conffilepath = "";

		try {
			conffilepath = Layer2GeoJSON.class.getClassLoader()
					.getResource("/").toURI().getPath();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		}
		conffilepath += "com/sasmac/conf/dbConnConf.properties";

		PropertiesUtil util = new PropertiesUtil(conffilepath);

		Map params1 = new HashMap();
		params1.put("dbtype", util.getProperty("dbtype"));
		params1.put("host", util.getProperty("host"));
		params1.put("port", util.getProperty("port"));
		params1.put("database", util.getProperty("database"));
		params1.put("user", util.getProperty("user"));
		params1.put("passwd", util.getProperty("password"));

		try {
			JDBCDataStore ds = (JDBCDataStore) factory1
					.createDataStore(params1);
			// SQLDialect p=ds.getSQLDialect();

			LayerName = LayerName.toLowerCase();// 大写转小写
			FeatureSource fs = ds.getFeatureSource(LayerName);

			FeatureType schema = fs.getSchema(); // 表的所有字段
			// 数据类型shape
			String geometryAttributeName = schema.getGeometryDescriptor()
					.getLocalName();

			GeometryFactory geometryFactory = JTSFactoryFinder
					.getGeometryFactory();

			FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
			Filter Spatialfilter = null;
			if (wkt != null && !wkt.isEmpty()) {
				WKTReader reader = new WKTReader(geometryFactory);
				MultiPolygon polygon = (MultiPolygon) reader.read(wkt);
				Spatialfilter = getGeoFilter(ff, geometryAttributeName, polygon);
			}
			Filter filter2 = null;

			try {
				if(!WhereClause.isEmpty())
				  filter2 = CQL.toFilter(WhereClause);// 过滤器（云量>=0且<=20）
			} catch (CQLException e1) {

				e1.printStackTrace();
			}

			List<Filter> match = new ArrayList<Filter>();
			if (Spatialfilter != null)
				match.add(Spatialfilter);
			match.add(filter2);

			Filter filter = ff.and(match);
			SimpleFeatureCollection result = null;
			String[] fields = null;
			if (LayerName.compareToIgnoreCase("TB_DOMFRAME_PRODUCT") == 0) {//标准分幅

				String[] fields1 = { geometryAttributeName, "dataid",
						"FileName", "FilePath", "archiveTime" };
				fields = fields1;

			} else if (LayerName.compareToIgnoreCase("TB_DOMSCENE_PRODUCT") == 0) {//分景
				String[] fields1 = { geometryAttributeName, "dataid",
						"FileName", "FilePath","satellite"};
				fields = fields1;
			} else if (LayerName.compareToIgnoreCase("TB_SC_PRODUCT") == 0) {//SC产品
				String[] fields1 = { geometryAttributeName, "dataid",
						"FileName", "FilePath", "scenePath", "sceneRow",
						"orbitID", "satellite", "sensor", "acquisitionTime",
						"ArchiveTime", "productLevel", "cloudPercent" };
				fields = fields1;
			}

			if (fields == null) {
				myLogger.error("ToGeoJSON 没有合适的查询字段");
				return "";
			}

			Query query = new Query(LayerName, filter, fields);
			try {
				result = (SimpleFeatureCollection) fs.getFeatures(query);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int nFeatureCount = result.size();
			if (nFeatureCount <= 0)
				return "";
			// JSONArray.fromObject(object);
			// geojson数据的格式
			String strLayerJSON = "{\"type\" :\"FeatureCollection\",\"features\" :[";
			boolean b = false;
			FeatureIterator itertor = result.features();

			//
			while (itertor.hasNext()) {
				if (b) {
					strLayerJSON += ",";
				}
				b = true;
				Feature feature = itertor.next();
				FeatureJSON fj = new FeatureJSON();

				ByteArrayOutputStream os = new ByteArrayOutputStream();
				fj.writeFeature((SimpleFeature) feature, os);

				os.close();
				strLayerJSON += os.toString();
			}
			itertor.close();
			strLayerJSON += "]}";
			ds.dispose();
			// System.out.println(strLayerJSON);
			return strLayerJSON;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
