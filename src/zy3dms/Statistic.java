package zy3dms;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.sasmac.system.ReadConf;

import sasmac.CalcArea;

public class Statistic {

	public static void main(String[] args) {
		// TODO Auto-generated method stub


	}
	
	private List<Integer> list;
	public static double statisticArea(String conffile,String strStatment,String searchGeoWkt) {
		CalcArea calcArea=new CalcArea();
		String connstring=ReadConf.getGDALConnStr(conffile);
		double dblarea=calcArea.CalcArea(connstring, strStatment, searchGeoWkt, searchGeoWkt);
		BigDecimal bDecimal=new BigDecimal(dblarea);
		System.out.println(bDecimal.toString());
		

		return dblarea;
	}

}
