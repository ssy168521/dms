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

import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Transaction;
import org.geotools.data.mysql.MySQLDataStoreFactory;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentEntry;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.styling.Font.Style;
import org.geotools.styling.SLD;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class SHP2MySql {
	public static void createFeatures() {

		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
		try {
			ShapefileDataStore sds = (ShapefileDataStore) dataStoreFactory
					.createDataStore(new File("D:\\sharedisk\\world_sj.shp")
							.toURI().toURL());
			sds.setCharset(Charset.forName("UTF-8"));
			SimpleFeatureSource featureSource = sds.getFeatureSource();

			SimpleFeatureType schema = featureSource.getSchema();

			JDBCDataStore pgDatastore;
			MySQLDataStoreFactory factory1 = new MySQLDataStoreFactory();
			FeatureSource fsBC1;
			Map params1 = new HashMap();
			params1.put("dbtype", "mysql");
			params1.put("host", "localhost");
			params1.put("port", new Integer(3306));
			params1.put("database", "geotool");
			params1.put("user", "root");
			params1.put("passwd", "123456");
			JDBCDataStore ds;
			try {
				ds = (JDBCDataStore) factory1.createDataStore(params1);
				ContentEntry entry = ds.getEntry(new NameImpl(null, schema
						.getTypeName()));
				if (entry != null) {
					ds.removeSchema(schema.getTypeName().toLowerCase());
				}
				// 定义图形信息和属性信息
				SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();

				ds.createSchema(schema);

				FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds
						.getFeatureWriter(schema.getTypeName().toLowerCase(),
								Transaction.AUTO_COMMIT);

				SimpleFeatureIterator itertor = featureSource.getFeatures()
						.features();

				while (itertor.hasNext()) {
					SimpleFeature feature = itertor.next();
					writer.hasNext();
					SimpleFeature feature1 = writer.next();
					feature1.setAttributes(feature.getAttributes());
					// feature1 = feature;
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

	public static void main(String[] args) throws Exception {
		createFeatures();
		// display a data store file chooser dialog for shapefiles
		File file = JFileDataStoreChooser.showOpenFile("shp", null);
		if (file == null) {
			return;
		}

		FileDataStore store = FileDataStoreFinder.getDataStore(file);
		SimpleFeatureSource featureSource = store.getFeatureSource();

		// Create a map content and add our shapefile to it
		MapContent map = new MapContent();
		map.setTitle("Quickstart");

		Style style = (Style) SLD.createSimpleStyle(featureSource.getSchema());
		FeatureLayer layer = new FeatureLayer(featureSource,
				(org.geotools.styling.Style) style);

		map.addLayer(layer);

		// Now display the map
		JMapFrame.showMap(map);

		String shpPath = "D:\\data\\china\\" + layer + ".shp";
		System.out.println(shpPath);
		ShapefileDataStore shpDataStore = null;

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		File file1 = new File(shpPath);
		shpDataStore = new ShapefileDataStore(file1.toURL());
		// 设置字符编码
		Charset charset = Charset.forName("GBK");
		shpDataStore.setCharset(charset);
		String typeName = shpDataStore.getTypeNames()[0];
		SimpleFeatureSource featureSource1 = null;
		featureSource1 = shpDataStore.getFeatureSource(typeName);
		SimpleFeatureCollection result = featureSource1.getFeatures();
		SimpleFeatureIterator itertor = result.features();
		while (itertor.hasNext()) {
			Map<String, Object> data = new HashMap<String, Object>();
			SimpleFeature feature = itertor.next();
			Collection<Property> p = feature.getProperties();
			Iterator<Property> it = p.iterator();
			while (it.hasNext()) {
				Property pro = it.next();
				String field = pro.getName().toString();
				String value = pro.getValue().toString();
				field = field.equals("the_geom") ? "wkt" : field;
				data.put(field, value);
			}
			list.add(data);
		}
	}

}
