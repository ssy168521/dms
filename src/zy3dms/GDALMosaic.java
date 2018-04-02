package zy3dms;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

public class GDALMosaic {
	static{
		//系统环境变量中或者jre/bin中dll文件
		System.loadLibrary("ImageMosaic");
	}
	
	
	public native int Mosaic(String[] strArrFiles,String strOutFile);

	public static void main(String[] args) {
		GDALMosaic jnItest=new GDALMosaic();
		String[] strArrFiles=new String[2];
		strArrFiles[0]="E:\\TestData\\MosaicImage\\7927.jpg";
		strArrFiles[1]="E:\\TestData\\MosaicImage\\7933.jpg";
				
		String strOutFile="E:\\TestData\\test.png";
		int result=jnItest.Mosaic(strArrFiles,strOutFile);
		System.out.println(result);
		
		String ttString=GDALimage.GetImageExtent(strOutFile);
		System.out.println(ttString);
	}
	
	

}
