package zy3dms;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

public class GDALimage {

	public GDALimage() {

	}

	public static void main(String[] args) {
		//GetImageExtent("E:\\database\\test\\J46D001001.png");
	}
    //初始点坐标（geomatrix[0]、geomatrix[3])
	//旋转角度（geomatrix[2]、geomatrix[4])、像素分辨率（geomatrix[1]、geomatrix[5])
	public static String GetImageExtent(String strImageUrl) {
		String strExtent = "";
		gdal.AllRegister();
		Dataset hDataset = gdal.Open(strImageUrl,
				gdalconstConstants.GA_ReadOnly);
		if (hDataset == null)
			return "";
		int xSize = hDataset.GetRasterXSize();
		int ySize = hDataset.GetRasterYSize();
		double[] geoTransform = hDataset.GetGeoTransform();
		
		double xmin = geoTransform[0];
		double ymax = geoTransform[3];
		double xmax = geoTransform[0] + xSize * geoTransform[1];
		double ymin = geoTransform[3] + ySize * geoTransform[5];
		// 'xmin':115.695,'ymin':34.342,'xmax':116.388,'ymax':34.952
    	//spatialReference要根据图像所具有的投影信息,若得到的是投影坐标，则去掉空间参考。经纬度是地理坐标，故需投影。	
		strExtent = "{'xmin':" + xmin + ",'ymin':" + ymin + ",'xmax':" + xmax
				+ ",'ymax':" + ymax + ",'spatialReference':{'wkid':4326}}";
		  
        hDataset.delete();
        //System.out.println(strExtent);
		return strExtent;
	}

}
