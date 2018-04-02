package com.sasmac.test;

import org.gdal.ogr.DataSource;

import com.sasmac.gdaldatapool.DataSouPoolUtil;

public class GDALDataPooltest {

	public GDALDataPooltest() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		/*DBConn connte=DBConn.createconn();*/
		/*DBConn connte=new DBConn();
		DataSource pdDataSource= connte.getpDataSou();
		Layer pLayer=pdDataSource.ExecuteSQL("select FID,F_DATANAME,F_SCENEID,F_SCENEPATH,F_SCENEROW,F_ORBITID,F_SCENEDATE,F_OVERALLDATAQUALITY from tbs_zy3_bm where F_OVERALLDATAQUALITY<=20 and F_OVERALLDATAQUALITY<=20 and F_SCENEDATE>='2015-11-01'");
		if(pLayer!=null) System.out.println(pLayer.GetFeatureCount());
		
		DataSource pDataSource2=connte.getpDataSou();*/
		//System.out.print("123");
		
		/**
         * 需要资源6个 ，
         * 超过常用的5个的时候，
         * 会出现一个复用的现象，正确的操作
         */
        for(int i=0;i<6;i++){
            DataSource pDataSou = DataSouPoolUtil.getDataSource();
            System.out.println(pDataSou);
            DataSouPoolUtil.close(pDataSou);
        }
        System.out.println("******");
        /**
         * 需要资源6个，
         * 超过常用的设置的5个，且没有进行资源的释放操作，
         * 没有超过最大的设置，会进一步的连接的创建
         */
        for(int i=0;i<6;i++){
        	DataSource pDataSou = DataSouPoolUtil.getDataSource();
            System.out.println(pDataSou);
        }
        System.out.println("******");
        /**
         * 资源超过最大的20个的时候，
         * 由于我们没有进行资源的释放的操作的时候，
         * 出现异常，如下测试
         */
        /*for(int i=0;i<21;i++){
            Connection conn = DataSouPoolUtil.getConnection();
            System.out.println(conn);
        }*/
		

	}

}
