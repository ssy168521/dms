package zy3dms;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

/**
 * 读取图像范围
 * @author Administrator
 * @ClassName:GDALimage2
 * @date 2018年1月12日 下午3:15:58
 */
public class GDALimage2 {

	public GDALimage2() {

	}

	public static void main(String[] args) {
		GetImageExtent("E:\\database\\overview\\J46\\J46D001001.png");
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
		String crsString=hDataset.GetProjectionRef();//获取crs
		//System.out.println(crsString);
		SpatialReference Crs=new SpatialReference(crsString);//构造投影坐标系统的空间参考(wkt)，
		//System.out.println(Crs);
		SpatialReference oLatLong; 	
        oLatLong = Crs.CloneGeogCS(); //获取该投影坐标系统中的地理坐标系   
        //System.out.println(oLatLong);
        
        //构造一个从投影坐标系统到地理坐标系统的转换关系  
        CoordinateTransformation ct = new CoordinateTransformation(Crs, oLatLong); 
        
		int xSize = hDataset.GetRasterXSize();
		int ySize = hDataset.GetRasterYSize();
		double[] geoTransform = hDataset.GetGeoTransform();
		
		double xmin = geoTransform[0];
		double ymax = geoTransform[3];
		double xmax = geoTransform[0] + xSize * geoTransform[1];
		double ymin = geoTransform[3] + ySize * geoTransform[5];
		// 'xmin':115.695,'ymin':34.342,'xmax':116.388,'ymax':34.952
		//ct.TransformPoint();投影转换为经纬度
		double a[]= ct.TransformPoint(xmin, ymax) ;
		double b[]= ct.TransformPoint(xmax, ymax) ;
		double c[]= ct.TransformPoint(xmax, ymin) ;
		double d[]= ct.TransformPoint(xmin, ymin) ;     
		double dbX[]={a[0],b[0],c[0],d[0]};
		double dbY[]={a[1],b[1],c[1],d[1]};
		
	    ImageExt test=new ImageExt();
        test.res(dbX);//按从小到大排列
        double Xmin=dbX[0];
        double Xmax=dbX[3];
        test.res(dbY);
        double Ymin=dbY[0];
        double Ymax=dbY[3];

		// 'xmin':115.695,'ymin':34.342,'xmax':116.388,'ymax':34.952
    	//经纬度是地理坐标，故需投影。	
		strExtent = "{'xmin':" + Xmin + ",'ymin':" + Ymin + ",'xmax':" + Xmax
				+ ",'ymax':" + Ymax + "}";
		//,'spatialReference':{'wkid':4326}
        hDataset.delete();
        //System.out.println(strExtent);
		return strExtent;
	}
	/**
	 * 比较大小，得到最大值与最小值
	 * @param arr
	 * @return
	 */
	  public double[] res(double[] arr){
	        for(int i=0;i<arr.length;i++){
	            for(int j=0;j<arr.length-i-1;j++){
	                 
	                if(arr[j]>arr[j+1]){
	                    double temp=arr[j];
	                    arr[j]=arr[j+1];
	                    arr[j+1]=temp;
	                }
	            }
	        }	             
	        return arr;         
	    }

}
