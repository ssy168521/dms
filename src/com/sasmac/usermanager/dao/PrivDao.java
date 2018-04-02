package com.sasmac.usermanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sasmac.usermanager.common.Privinfo;
import com.sasmac.usermanager.common.Roleinfo;
import com.sasmac.usermanager.common.Userinfo;
import com.sasmac.usermanager.common.UserinfoDto;

//sdd548,角色权限管理
public class PrivDao {
	private Connection userCN=null;
	private List<Privinfo>  allPriv;
	private List<Roleinfo>  allRole;

	/** SUNJ 2017.2.11修改
	 * 修改构造函数 PrivDao(ConnectionMyDB cndb)
	 * 使用jdbc的Connection替换这个ConnectionMyDB类
	 */
	public PrivDao(Connection cndb){
		
		this.userCN=cndb;
		this.allPriv=new ArrayList<Privinfo>();
		this.allRole=new ArrayList<Roleinfo>();
	}
	
	//更新所有权限列表	
	public boolean upAllPrive(){
		boolean result=false;
		allPriv.clear();
		String sql = "select ID,PRIVILEDGENAME from TB_PRIVILEDGE";
		try {
		    PreparedStatement psmt = userCN.prepareStatement(sql);//
		    ResultSet rs;
			rs = psmt.executeQuery();
			while(rs.next()){
				Privinfo mypriv=new Privinfo();	
				mypriv.setPrivid(rs.getString(1));		
				mypriv.setPrivname(rs.getString(2));
				allPriv.add(mypriv);				
			}
			psmt.close();
			rs.close();
			result=true;
		} 
			catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		}		
		return result;		
	}

	public List<Roleinfo> getRolelist() {
		upAllRole();
		return allRole;
	}	
	
//更新所有角色列表	
	public boolean upAllRole(){
		boolean result=false;
		allRole.clear();//清空角色列表 
		String sqlR = "select ID,ROLENAME from TB_ROLE";
		String sqlL = "select PRIVILEDGEID from TB_ROLEPRIVILEDGE where ROLEID=?";
		String sqlP = "select ID,PRIVILEDGENAME from TB_PRIVILEDGE where ID=?";
		try {
			//查角色表 
			PreparedStatement psmtR = userCN.prepareStatement(sqlR);//
			ResultSet rsR = psmtR.executeQuery();			
			while(rsR.next()){				
				Roleinfo myrole=new Roleinfo();	
				//读角色信息
				myrole.setRoleid(rsR.getString(1));
				myrole.setRolename(rsR.getString(2));
				//查关联表
				PreparedStatement psmtL=userCN.prepareStatement(sqlL);
				psmtL.setString(1,rsR.getString(1));
				ResultSet rsL = psmtL.executeQuery();//列
				while(rsL.next()){
					//循环角色关联的 权限ID，查权限表，读权限信息 
					PreparedStatement psmtP=userCN.prepareStatement(sqlP);
					psmtP.setString(1,rsL.getString(1));
					ResultSet rsP = psmtP.executeQuery();//列
					Privinfo mypriv=new Privinfo();	
					if(rsP.next()){
					 	mypriv.setPrivid(rsP.getString(1));		
				     	mypriv.setPrivname(rsP.getString(2));
					    myrole.getPrivlist().add(mypriv);
				     }
					psmtP.close();	
					rsP.close();						
				}	
				//加入列表
				allRole.add(myrole);
				psmtL.close();	
				rsL.close();				
			}
			psmtR.close();			
			rsR.close();
			result=true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}			
		return result;	
	}
	
	public List<Privinfo> getPrivlist() {
		upAllPrive();
		return allPriv;
	}
	
	//新增角色
	public boolean createRole(String rolename){		
		boolean result =false;
		if(rolename!=null)
		{
		    String sqlC="select ROLENAME from TB_ROLE where ROLENAME=?";
			String sql = "insert into TB_ROLE values(null,?)";//写表,ID自增长 
			try{
				PreparedStatement psmtC = userCN.prepareStatement(sqlC);
				psmtC.setString(1,rolename);
				ResultSet rs=psmtC.executeQuery();
				if(rs.next()==false){
					PreparedStatement psmt = userCN.prepareStatement(sql);
					psmt.setString(1,rolename);
					int resu =psmt.executeUpdate();
					if(resu>0){
						System.out.println(rolename+"创建成功");
						result=true;
					}else{
						System.out.println(rolename+"创建失败");
					}
					psmt.close();
				}else{
					System.out.println("角色名已存在 ");
				}			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		return result;
    }
	
	//删除角色
	public boolean deletRole(String thisroleid){
		boolean result =false;
		//删除用户及用户角色
		String sqlR = "delete from TB_ROLE where ID=?";
		String sqlA = "delete from TB_USERROLE where ROLEID=?";		
		String sqlL = "delete from TB_ROLEPRIVILEDGE where ROLEID=?";		
		try {
			PreparedStatement psmtR;
			psmtR = userCN.prepareStatement(sqlR);
			psmtR.setString(1, thisroleid);
			int rs=psmtR.executeUpdate();		
			if(rs>0){
				result=true;
			}
			PreparedStatement psmtA;
			psmtA = userCN.prepareStatement(sqlA);
			psmtA.setString(1, thisroleid);
			psmtA.executeUpdate();
			PreparedStatement psmtL;
			psmtL = userCN.prepareStatement(sqlL);
			psmtL.setString(1, thisroleid);
			psmtL.executeUpdate();
			psmtL.close();
			psmtA.close();
			psmtR.close();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }		
		return result;
	}
		
	//重命名
	public boolean reName(String thisroleid,String rename){
		boolean result =false;
		//修改有效性
		String sql = "update TB_ROLE set ROLENAME=? where id=?";		
		try {
			PreparedStatement psmt;
			psmt = userCN.prepareStatement(sql);
			psmt.setString(1, rename);
			psmt.setString(2, thisroleid);
			int rs=psmt.executeUpdate();		
			if(rs>0){
				result=true;
			}				
			psmt.close();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	       }		
		return result;
	}
	
	//修改角色权限
	public boolean modifyPriv(String thisroleid,List<String> prividlist){
		boolean result =false;
		//修改用户角色
		String sqlD = "delete from TB_ROLEPRIVILEDGE where ROLEID=?";	
		String sqlI = "insert into TB_ROLEPRIVILEDGE values(null,?,?)";		
		try {
			int js=0;
			PreparedStatement psmtD;
			psmtD = userCN.prepareStatement(sqlD);
			psmtD.setString(1, thisroleid);
			psmtD.executeUpdate();
			psmtD.close();
			for(int i=0; i<prividlist.size();i++){
				PreparedStatement psmtI;
				psmtI = userCN.prepareStatement(sqlI);
				psmtI.setString(1, thisroleid);
				psmtI.setString(2, prividlist.get(i));				
				int rs=	psmtI.executeUpdate();	
				if(rs>0){
					js=js+1;
				}
				psmtI.close();
			}
			if(js==prividlist.size()) result=true;
			psmtD.close();	
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return result;
	}
	
	//获取指定用户详情
	public Roleinfo getRoleinfo(String roleid){

		/*UserinfoDto myUserdto=new UserinfoDto();
		Userinfo myuser=new Userinfo();*/
		Roleinfo myrole=new Roleinfo();		
		/*String sqlU = "select ID,USERNAME,REGTIME,VALID,ONLINE from TB_USER where ID=?";
		String sqlA = "select ROLEID from TB_USERROLE where USERID=?";*/
		String sqlR = "select ROLENAME from TB_ROLE where ID=?";		
		String sqlL = "select PRIVILEDGEID from TB_ROLEPRIVILEDGE where ROLEID=?";
		String sqlP = "select ID,PRIVILEDGENAME from TB_PRIVILEDGE where ID=?";
		//有角色，读角色信息
		myrole.setRoleid(roleid);
		//查角色表，读角色名
		PreparedStatement psmtR;
		try {
			psmtR = userCN.prepareStatement(sqlR);
			psmtR.setString(1,roleid);
			ResultSet rsR = psmtR.executeQuery();
			if(rsR.next()){
				myrole.setRolename(rsR.getString(1));
				//查角色权限表
				PreparedStatement psmtL=userCN.prepareStatement(sqlL);
				psmtL.setString(1,roleid);
				ResultSet rsL = psmtL.executeQuery();//列
				while(rsL.next()){
					//循环角色关联的 权限ID，查权限表，读权限信息 
					PreparedStatement psmtP=userCN.prepareStatement(sqlP);
					psmtP.setString(1,rsL.getString(1));
					ResultSet rsP = psmtP.executeQuery();//列
					if(rsP.next()){
						Privinfo myPri=new Privinfo(rsP.getString(1),rsP.getString(2));
						myrole.getPrivlist().add(myPri);
					}
					psmtP.close();	
					rsP.close();						
				}	
				psmtL.close();			
				rsL.close();
			}
			psmtR.close();			
			rsR.close();	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   return myrole;
	}
	
	//通过角色名获取角色id
	public String rolenameToRid(String rname){
		String rid="";
		String sql="select ID from TB_ROLE where ROLENAME=?";
		try {
			PreparedStatement psmt = userCN.prepareStatement(sql);
			psmt.setString(1,rname);
			ResultSet rs=psmt.executeQuery();
			if(rs.next()){
				rid=rs.getString(1);
			}
			else{
				System.out.println("角色名不存在 ");
			}
			psmt.close();
			rs.close();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return rid;
	}

}
