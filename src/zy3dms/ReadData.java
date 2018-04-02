package zy3dms;

import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.mysql.MySQLDataStoreFactory;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.jdbc.JDBCDataStore;

public class ReadData {
	public void read() throws IOException {
		java.util.Map params = new java.util.HashMap();
		params.put(MySQLDataStoreFactory.DBTYPE.key, "mysql");
		params.put(MySQLDataStoreFactory.HOST.key, "localhost");
		params.put(MySQLDataStoreFactory.PORT.key, 3306);
		params.put(MySQLDataStoreFactory.DATABASE.key, "test");
		params.put(MySQLDataStoreFactory.USER.key, "root");
		params.put(MySQLDataStoreFactory.PASSWD.key, "123456");
		params.put(MySQLDataStoreFactory.STORAGE_ENGINE, "MyISAM");

		DataStore dataStore = DataStoreFinder.getDataStore(params);

		MySQLDataStoreFactory mySQLFactory = new MySQLDataStoreFactory();

		JDBCDataStore mySQLDatastore = (JDBCDataStore) mySQLFactory
				.createDataStore(params);

		// Prepare feature sources
		ContentFeatureSource fsStreets = mySQLDatastore
				.getFeatureSource("streets");
	}
}
