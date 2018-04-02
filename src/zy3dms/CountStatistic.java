package zy3dms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.sasmac.dbconnpool.ConnPoolUtil;
import com.sasmac.usermanager.common.Privinfo;



public class CountStatistic {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//List<ImageSize>listimg = ImgSizeStatistic();
		List<ImageSize>listimgcount=ImgCountStatistic();
		int a=0;
		a=2;

	}
	
	public static List<ImageSize> ImgSizeStatistic() {

		Connection conn=ConnPoolUtil.getConnection();
		List<ImageSize> listImgSize=new ArrayList<ImageSize>();
		DecimalFormat dFormat=new DecimalFormat("#.00");
		
		try {
			String tablename="TB_SC_PRODUCT";
			String[] satellitename={"ZY3-1","ZY3-2","GF1","GF2"};
			for(int i=0;i<satellitename.length;i++)
			{
				double dsize=0;
				
				String sqlString="select sum(fileSize) from "+tablename+" where satellite='"+satellitename[i]+"'";
				PreparedStatement psmt2 = conn.prepareStatement(sqlString);
				ResultSet rs2 = psmt2.executeQuery();
				while(rs2.next()){
					if (rs2.getString(1)==null) continue;
					dsize=dsize+Double.parseDouble(rs2.getString(1));
				}
				rs2.close();
				
				ImageSize objsize =new ImageSize();
				objsize.setName(satellitename[i]);			
				objsize.sety(Double.parseDouble(dFormat.format(dsize/1024)));	
				listImgSize.add(objsize);
			}
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ConnPoolUtil.close(conn, null, null);
		return listImgSize;
		
	}
	
	
	public static List<ImageSize> ImgCountStatistic() {

		Connection conn=ConnPoolUtil.getConnection();
		List<ImageSize> listImgSize=new ArrayList<ImageSize>();
		DecimalFormat dFormat=new DecimalFormat("#.00");
		
		try {
			String tablename="TB_SC_PRODUCT";
			String[] satellitename={"ZY3-1","ZY3-2","GF1","GF2"};
			for(int i=0;i<satellitename.length;i++)
			{
				int cnt=0;
				String sqlString="select count(fileSize) from "+tablename+" where satellite='"+satellitename[i]+"'";
				PreparedStatement psmt2 = conn.prepareStatement(sqlString);
				ResultSet rs2 = psmt2.executeQuery();
				while(rs2.next()){
					 cnt+=rs2.getInt(1);
				}
				rs2.close();
				
				ImageSize objsize =new ImageSize();
				objsize.setName(satellitename[i]);			
				objsize.sety(cnt);	
				listImgSize.add(objsize);
			}
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ConnPoolUtil.close(conn, null, null);
		return listImgSize;
		
	}

}
