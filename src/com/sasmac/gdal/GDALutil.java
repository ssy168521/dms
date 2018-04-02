package com.sasmac.gdal;

import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;

import com.sasmac.gdaldatapool.DataSouPoolUtil;

public class GDALutil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ogr.RegisterAll();
        org.gdal.ogr.Driver dbDriver =ogr.GetDriverByName("MySQL");
		GDALutil gdaLutil=new GDALutil();
		/*String wwwString=gdaLutil.ShpToWkt("D:\\tomcat-7.0.70\\webapps\\zy3dms\\upload\\testshp.shp");
		System.out.println(wwwString);*/
		String connstring="MySQL:testdb,user=root,port=3306,password=123456,host=localhost";
		DataSource pDataSource = dbDriver.Open(connstring,0);
		gdaLutil.ShpToMySQL("E:\\TestData\\InputData2\\World.shp", pDataSource);

	}
	/**
	 * 
	 * @param shpfile
	 * @return
	 */
	public String ShpToWkt(String shpfile) {
		String wkt="";
		ogr.RegisterAll();
		org.gdal.ogr.Driver pDriver = ogr.GetDriverByName("ESRI Shapefile");
        if (pDriver == null)
        {
            System.out.println("Driver Open failed");
        }
		DataSource pDataSource=pDriver.Open(shpfile);
		Layer pLayer=pDataSource.GetLayer(0);
		/*wkt=pLayer.exporttojson();*/
		Feature pfeature=null;
		Geometry pGeom=pLayer.GetFeature(0).GetGeometryRef();
		while((pfeature=pLayer.GetNextFeature()) != null){
			Geometry pGeometry=pfeature.GetGeometryRef();
			/*System.out.println(pGeometry.ExportToWkt());*/
			pGeom=pGeom.Union(pGeometry); //面融合
		}
		wkt=pGeom.ExportToWkt();
		
		return wkt;
	}
	
	public void ShpToMySQL(String shpfile,DataSource mysqlDataSou) {
		ogr.RegisterAll();
		org.gdal.ogr.Driver pDriver =ogr.GetDriverByName("ESRI Shapefile");
        if (pDriver == null)
        {
            System.out.println("Driver Open failed");
        }
		DataSource pDataSource=pDriver.Open(shpfile);
		Layer pLayer=pDataSource.GetLayer(0);
		mysqlDataSou.CopyLayer(pLayer, "testworld");		
	}
	
	

}
