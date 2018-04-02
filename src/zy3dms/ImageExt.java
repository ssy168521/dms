package zy3dms;

import org.opengis.referencing.FactoryException;

import com.vividsolutions.jts.io.ParseException;
import com.web.thread.Dbconnect2;
/**
 * 弃用类
 * @author Administrator
 * @ClassName:ImageExt
 * @date 2018年1月12日 下午3:08:02
 */
public class ImageExt {
//	public static void main(String[] args) throws FactoryException {
//		GetImageExt("J46D001003");
//	}
	//为加快显示速度，直接从数据库表中读取图像范围
	public static String GetImageExt(String filename) {
		String strExtent ="";
		String WhereClause="";
		WhereClause="FileName='"+filename+"'";
		try {
			//90.9108,39.9913],[91.486,40.0732],[91.5866,39.6747],[91.0145,39.5933
			strExtent=Dbconnect2.ToReadDb("tif2db1",WhereClause);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String a1=strExtent.replace("],[", ",");
        String data[]=a1.split(",");
//        for (int i = 0; i < data.length; i++) { 
//            System.out.println(data[i]);  
//        }
        //将字符串数组转化为double类型
        double xstr[]={Double.parseDouble(data[0]),Double.parseDouble(data[2]),
        		Double.parseDouble(data[4]),Double.parseDouble(data[6])};
        double ystr[]={Double.parseDouble(data[1]),Double.parseDouble(data[3]),
        		Double.parseDouble(data[5]),Double.parseDouble(data[7])};
     // 'xmin':115.695,'ymin':34.342,'xmax':116.388,'ymax':34.952
        ImageExt test=new ImageExt();
        test.res(xstr);
        double xmin=(xstr[1]+xstr[0])/2;
        double xmax=(xstr[3]+xstr[2])/2;
        test.res(ystr);
        double ymin=(ystr[1]+ystr[0])/2;
        double ymax=(ystr[3]+ystr[2])/2;
        strExtent = "{'xmin':" + xmin + ",'ymin':" + ymin + ",'xmax':" + xmax
				+ ",'ymax':" + ymax + ",'spatialReference':{'wkid':4326}}";
        System.out.println(strExtent);
		
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