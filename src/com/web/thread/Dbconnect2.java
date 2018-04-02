package com.web.thread;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.mysql.MySQLDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.jdbc.JDBCDataStore;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.web.util.PropertiesUtil;

public class Dbconnect2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		try {
//			ToReadDb("tif2db1","FileName='J46D001003'");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
/**
 * 类已弃用
 * @param LayerName
 * @param WhereClause
 * @return
 * @throws ParseException
 */
	public static String ToReadDb(String LayerName,String WhereClause)
			throws ParseException {

		JDBCDataStore pgDatastore=null;
		MySQLDataStoreFactory factory2 = new MySQLDataStoreFactory();
		FeatureSource fsBC1;
        //mysql连接信息
		String conffilepath = "H\\myeclipse_work\\zy3dms\\src\\com\\sasmac\\conf\\dbConnConf.properties";

		PropertiesUtil util = new PropertiesUtil(conffilepath);

		Map params1 = new HashMap();
		params1.put("dbtype", util.getProperty("dbtype"));
		params1.put("host", util.getProperty("host"));
		params1.put("port", util.getProperty("port"));
		params1.put("database", util.getProperty("database"));
		params1.put("user", util.getProperty("user"));
		params1.put("passwd", util.getProperty("password"));

		try {
			JDBCDataStore ds2 = (JDBCDataStore) factory2
					.createDataStore(params1);

			FeatureSource fs2 = ds2.getFeatureSource(LayerName);
		
			FeatureType schema2 = fs2.getSchema(); //表的所有字段
			//数据类型shape
			String geometryAttributeName2 = schema2.getGeometryDescriptor().getLocalName();
//			GeometryFactory geometryFactory2 = JTSFactoryFinder
//					.getGeometryFactory();
			Filter filter = null;
            //String WhereClause="FileName='J46D001001'";
			try {
				filter = CQL.toFilter(WhereClause);//过滤器（云量>=0且<=20）
			} catch (CQLException e1) {

				e1.printStackTrace();
			}

			SimpleFeatureCollection result2 = null;
			String[] fields2 = {geometryAttributeName2};//字段Shape
			Query query2 = new Query(LayerName, filter, fields2);
			try {
				result2 = (SimpleFeatureCollection) fs2.getFeatures(query2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int nFeatureCount2 = result2.size();//933,筛选出表中933个符合条件的数据
			if (nFeatureCount2 <= 0)
				return "";
			//geojson数据的格式
			String strLayerJSON2="";
			boolean b2 = false;
			FeatureIterator itertor2 = result2.features();			
			Feature feature2 = itertor2.next();
			FeatureJSON fj2 = new FeatureJSON();

			ByteArrayOutputStream os2 = new ByteArrayOutputStream();
			fj2.writeFeature((SimpleFeature) feature2, os2);
			
			os2.close();
			strLayerJSON2 = os2.toString();
			
			itertor2.close();
			ds2.dispose();//使用后关闭dataSource
			strLayerJSON2=strLayerJSON2.substring(strLayerJSON2.indexOf("[")+3,
					strLayerJSON2.lastIndexOf("[")-2);
			//System.out.println(strLayerJSON2);
			return strLayerJSON2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}
