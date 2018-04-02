package com.sasmac.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sasmac.dbconnpool.ConnPoolUtil;

public class RegionManage {

	public static void main(String[] args) {
		//

	}
	
	public enum RegionType{
		World,
		Province,
		City,
		County
	}
	
	public List<String> get2ndRegByRegion(String RegionName){
		//provinceName="sdsdfsf";
		List<String> SecondRegNames=new ArrayList<String>();
		Connection connection=ConnPoolUtil.getConnection();
		String sql = "select XZQMC from tb_xzq_link where XZQMC1=?";
		try {
		    PreparedStatement psmt = connection.prepareStatement(sql);//		   
		    psmt.setString(1,RegionName);
		    ResultSet rs = psmt.executeQuery();
		    while(rs.next()){
				SecondRegNames.add(rs.getString(1));
			}
			psmt.close();
			rs.close();
			ConnPoolUtil.close(connection, psmt, rs);
		} 
		catch (SQLException e) {
			e.printStackTrace();		
		}		
		return SecondRegNames;
	}
	
	public String[] getRegionWkt(String[] RegionNames,RegionType type){
		//List<String> cityNames=new ArrayList<String>();
		//RegionType type=RegionType.City;
		String tbname="";
		switch (type) {
		case World:
			tbname="tb_xzq_world";
			break;
		case Province:
			tbname="tb_xzq_sj";
			break;			
		case City:
			tbname="tb_xzq_dsj";
			break;
		case County:
			tbname="tb_xzq_xj";
			break;
		}		
		
		String[] regWkts=new String[RegionNames.length];
		for (int i = 0; i < RegionNames.length; i++) {
			Connection connection=ConnPoolUtil.getConnection();
			String sql = "select astext(the_geom) from "+tbname+" where XZQMC=?";
			try {
			    PreparedStatement psmt = connection.prepareStatement(sql);//		   
			    psmt.setString(1,RegionNames[i]);
			    ResultSet rs = psmt.executeQuery();
			    while(rs.next()){
			    	regWkts[i]=rs.getString(1);
				}
				psmt.close();
				rs.close();
				ConnPoolUtil.close(connection, psmt, rs);
			} 
			catch (SQLException e) {
				e.printStackTrace();		
			}		
		}
		
		return regWkts;
	}

}
