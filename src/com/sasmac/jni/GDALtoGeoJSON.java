package com.sasmac.jni;

public class GDALtoGeoJSON {
	static {
		System.loadLibrary("GDALtoGeoJSON");
	}

	public native String Layer2GeoJSON(String connectstring, String strSQL,
			String wktPoly, String dialect);

}
