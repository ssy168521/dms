package com.sasmac.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gdal.ogr.DataSource;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.mysql.MySQLDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.jdbc.JDBCDataStore;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import com.sasmac.gdaldatapool.DataSouPool;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.web.util.PropertiesUtil;

public class Layer2GeoJSON2 {
	
	public Filter getGeoFilter(FilterFactory2 ff, // 构建拓扑查询的filter
			String geometryAttributeName, Geometry refGeo) {

		return ff.intersects(ff.property(geometryAttributeName),
				ff.literal(refGeo));
	}
	/**
	 * @param LayerName 表名
	 * @param wkt
	 * @return
	 * @throws ParseException
	 */
	public String ToGeoJSON2(String LayerName2, String wkt2)
			throws ParseException {

		//JDBCDataStore pgDatastore2=null;
		MySQLDataStoreFactory factory2 = new MySQLDataStoreFactory();
		//FeatureSource fsBC2;
		String conffilepath2 = "";
		//String conffilepath2 = "E:/MyEclipse2014 workspace/archives/libs/dbConnConf.properties";

		try {
			conffilepath2 = Layer2GeoJSON2.class.getClassLoader()
					.getResource("").toURI().getPath();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		}
		conffilepath2 += "com/sasmac/conf/dbConnConf.properties";

		PropertiesUtil util = new PropertiesUtil(conffilepath2);

		Map params2 = new HashMap();
		params2.put("dbtype", util.getProperty("dbtype"));
		params2.put("host", util.getProperty("host"));
		params2.put("port", util.getProperty("port"));
		params2.put("database", util.getProperty("database"));
		params2.put("user", util.getProperty("user"));
		params2.put("passwd", util.getProperty("password"));

		try {
			JDBCDataStore ds2 = (JDBCDataStore) factory2
					.createDataStore(params2);
			
			//LayerName = LayerName.toLowerCase();//大写转小写
			FeatureSource fs2 = ds2.getFeatureSource(LayerName2);
		
			FeatureType schema2 = fs2.getSchema(); //表的所有字段
			//数据类型shape
			String geometryAttributeName2 = schema2.getGeometryDescriptor().getLocalName();

			GeometryFactory geometryFactory2 = JTSFactoryFinder
					.getGeometryFactory();

			FilterFactory2 ff2 = CommonFactoryFinder.getFilterFactory2(null);
			Filter Spatialfilter2 = null;
			if (wkt2 != null && !wkt2.isEmpty()) {
				WKTReader reader = new WKTReader(geometryFactory2);
				MultiPolygon polygon = (MultiPolygon) reader.read(wkt2);
				Spatialfilter2 = getGeoFilter(ff2, geometryAttributeName2, polygon);
			}


			List<Filter> match = new ArrayList<Filter>();
			match.add(Spatialfilter2);

			Filter filter2 = ff2.and(match);
			SimpleFeatureCollection result2 = null;
			String[] fields2 = { geometryAttributeName2, "dataid", "FileName",
					"FilePath", "FileSize", "CellSizexy", "Columns","Rows",
					"BandsNumber", "DataType", "acquisitionTime", "archiveTime" };
			Query query2 = new Query(LayerName2, filter2, fields2);
			try {
				result2 = (SimpleFeatureCollection) fs2.getFeatures(query2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int nFeatureCount2 = result2.size();//933,筛选出表中933个符合条件的数据
			if (nFeatureCount2 <= 0)
				return "";
			// JSONArray.fromObject(object);
			//geojson数据的格式
			String strLayerJSON2="";
			//String strLayerJSON2 = "{\"type\" :\"FeatureCollection\",\"features\" :[";
			boolean b2 = false;
			FeatureIterator itertor2 = result2.features();

			//
			while (itertor2.hasNext()) {
				if (b2) {
					strLayerJSON2 += ",";
				}
				b2 = true;
				Feature feature2 = itertor2.next();
				FeatureJSON fj2 = new FeatureJSON();

				ByteArrayOutputStream os2 = new ByteArrayOutputStream();
				fj2.writeFeature((SimpleFeature) feature2, os2);
				
				os2.close();
				strLayerJSON2 += os2.toString();
			}
			itertor2.close();
			strLayerJSON2 += "]}";
			ds2.dispose();//使用后关闭dataSource
			//System.out.println(strLayerJSON2);
			return strLayerJSON2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
         
		}
		
		return "";
	}
}
