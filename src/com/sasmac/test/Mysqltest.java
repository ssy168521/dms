/**
 * 
 */
package com.sasmac.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.mysql.MySQLDataStoreFactory;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
//import org.geotools.feature.AttributeType;
//import org.geotools.feature.AttributeTypeFactory;
//import org.geotools.feature.Feature;
//import org.geotools.feature.FeatureType;
//iimport org.geotools.feature.SchemaException;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.jdbc.JDBCDataStore;
//import org.apache.log4j.Logger;
import org.opengis.feature.Feature;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
//import org.geotools.feature.SchemaExceptimport org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.Geometry;

import com.sasmac.util.Layer2GeoJSON;
import com.vividsolutions.jts.geom.LineString;
//import org.opengis.geometry.primitive.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

//import org.geotools.data.FeatureResults;

/**
 * @author ssy
 * 
 */
class Mysqltest {

	static DataStore pgDatastore;
	static MySQLDataStoreFactory factory = new MySQLDataStoreFactory();
	static FeatureSource fsBC;

	public static void topoQueryMethod(Geometry refGeo, String queryName,
			String layerName) {
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
		ArrayList<SimpleFeature> featurelist = new ArrayList<SimpleFeature>();
		SimpleFeatureSource featureSource = null;
		try {
			featureSource = pgDatastore.getFeatureSource(layerName);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		SimpleFeatureType schema = featureSource.getSchema();
		String geometryAttributeName = schema.getGeometryDescriptor()
				.getLocalName();
		Filter filter1 = getGeoFilter(ff, geometryAttributeName, refGeo);
		Filter filter2 = null;

		try {
			filter2 = CQL.toFilter("StandName = '" + queryName + "'");
		} catch (CQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List<Filter> match = new ArrayList<Filter>();
		match.add(filter1);
		match.add(filter2);
		Filter filter = ff.and(match);

		SimpleFeatureCollection result = null;
		try {
			result = featureSource.getFeatures(filter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (result == null)
			return;
		FeatureIterator<SimpleFeature> itertor = result.features();
		while (itertor.hasNext()) {
			SimpleFeature feature = itertor.next();
			featurelist.add(feature);
		}
		return;

	}

	@SuppressWarnings("unchecked")
	private static void ConnPostGis(String dbtype, String URL, int port,
			String database, String user, String password) {
		Map params = new HashMap();
		params.put("dbtype", "mysql");
		params.put("host", URL);
		params.put("port", new Integer(port));
		params.put("database", database);
		params.put("user", user);
		params.put("passwd", password);
		params.put("charset", "UTF-8");
		try {

			pgDatastore = (DataStore) factory.createDataStore(params);

			if (pgDatastore != null) {
				System.out.println("系统连接到位于：" + URL + "的空间数据库" + database
						+ "成功！");
			} else {
				System.out.println("系统连接到位于：" + URL + "的空间数据库" + database
						+ "失败！请检查相关参数");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("系统连接到位于：" + URL + "的空间数据库" + database
					+ "失败！请检查相关参数");
		}
	}

	// 读取指定类型名的地理特征
	public static void getFeatureSource(String sourceName) {
		try {
			fsBC = pgDatastore.getFeatureSource(sourceName);
			// System.out.println(fsBC.getFeatures().size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 取得POSTGIS中所有的地理图层
	public static void getAllLayers() {
		try {
			String[] typeName = pgDatastore.getTypeNames();
			for (int i = 0; i < typeName.length; i++) {
				System.out.println(typeName[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 获取图层地理元素属性（Feature Attribute）
	public static void getAttribute() {
		FeatureType ftBC = fsBC.getSchema();

		/*
		 * for (int i = 0; i < ftBC.getAttributeCount(); i++) { AttributeType at
		 * = ftBC.getAttributeType(i); // 判断属性类型是否为可分配的几何对象 if
		 * (!Geometry.class.isAssignableFrom(at.getType()))
		 * System.out.print(at.getType() + "\t"); } System.out.println(); for
		 * (int i = 0; i < ftBC.getAttributeCount(); i++) { AttributeType at =
		 * ftBC.getAttributeType(i); if
		 * (!Geometry.class.isAssignableFrom(at.getType()))
		 * System.out.print(at.getName() + "\t"); }
		 */
	}

	// 从数据容器中读取所有的特征属性
	@SuppressWarnings("deprecation")
	public static void PostGisReading() {
		try {

			FeatureCollection fsRU = fsBC.getFeatures();
			FeatureIterator iter = fsRU.features();
			Feature feature;

			while (iter.hasNext()) {

				try {
					feature = iter.next();
					Collection<Property> prop = feature.getProperties();

					Iterator<Property> it = prop.iterator();
					while (it.hasNext()) {
						Property pro = it.next();
						if (pro.getValue() instanceof Geometry) {
						} else {
							System.out.println(pro.getName() + " = "
									+ pro.getValue());
						}
					}

				} catch (NoSuchElementException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// 添加特征值到新的特征对象中。等同于新建一个postgis数据表并向其中插入数据
	@SuppressWarnings("deprecation")
	public static void createFeatures() {

		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
		try {
			ShapefileDataStore sds = (ShapefileDataStore) dataStoreFactory
					.createDataStore(new File("D:\\work\\shpdir\\Poi.shp")
							.toURI().toURL());
			sds.setCharset(Charset.forName("UTF8"));
			SimpleFeatureSource featureSource = sds.getFeatureSource();

			SimpleFeatureType schema = featureSource.getSchema();

			JDBCDataStore pgDatastore;
			MySQLDataStoreFactory factory1 = new MySQLDataStoreFactory();
			FeatureSource fsBC1;
			Map params1 = new HashMap();
			params1.put("dbtype", "mysql");
			params1.put("host", "localhost");
			params1.put("port", new Integer(3306));
			params1.put("database", "testdb");
			params1.put("user", "root");
			params1.put("passwd", "123456");
			JDBCDataStore ds;
			try {
				ds = (JDBCDataStore) factory1.createDataStore(params1);

				// 定义图形信息和属性信息
				SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

				ds.createSchema(schema);

				FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds
						.getFeatureWriter(schema.getTypeName(),
								Transaction.AUTO_COMMIT);

				SimpleFeatureIterator itertor = featureSource.getFeatures()
						.features();

				while (itertor.hasNext()) {
					SimpleFeature feature = itertor.next();
					SimpleFeature feature1 = writer.next();
					feature1 = feature;
					writer.write();
				}
				itertor.close();

				writer.close();
				ds.dispose();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 添加Feature到已知的图层之中
	public static void insertFeatures(String featurename) {
		WKTReader wktReader = new WKTReader();
		try {
			LineString geometry = (LineString) wktReader
					.read("LINESTRING (10 10, 20 20)");
			String roadName = "珞瑜路";
			FeatureSource source = pgDatastore.getFeatureSource(featurename);
			FeatureWriter aWriter = pgDatastore.getFeatureWriterAppend(
					featurename, ((FeatureStore) source).getTransaction());
			/** 如有批量导入数据要求，可使用 org.geotools.data.FeatureStore */
			Feature feature = aWriter.next();
			try {
				// feature.setAttribute("the_geom", geometry);
				// feature.setAttribute("name", roadName);
			} catch (IllegalAttributeException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			aWriter.write();
			aWriter.close();
		} catch (ParseException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}

	}

	public static Filter getGeoFilter(FilterFactory2 ff, // 构建拓扑查询的filter
			String geometryAttributeName, Geometry refGeo) {

		return ff.intersects(ff.property(geometryAttributeName),
				ff.literal(refGeo));

	}

	public static void main(String[] args) throws IOException {

		Layer2GeoJSON layer2GeoJson = new Layer2GeoJSON();
		String wkt = "POLYGON((-170 80, 170 80, 170 -80, -170 -80, -170 80))";
		try {
			String json = layer2GeoJson.ToGeoJSON("tb_gf1_level1_pms2", wkt,
					"sensor='PMS2' and productLevel='LEVEL1A'");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ConnPostGis("mysql", "localhost", 3306, "testdb", "root", "123456");
		createFeatures();
		/* 读取空间库中所有图层 */
		getAllLayers();
		/*
		 * 读取roads图层的空间库，取得FeatureSource对象， getAttribute()方法用于读取此图层所定义的所有的属性
		 * 并通过PostGisReading()方法读取此图层中所有信息
		 */
		getFeatureSource("roads");
		getAttribute();
		PostGisReading();
		/* 在空间库中新建一个schema并向表中插入数据 */

		/* 向tem_road图层的空间库中插入一条新的记录 */
		insertFeatures("tem_road");
		/* 修改空间库记录 */
	}
}
