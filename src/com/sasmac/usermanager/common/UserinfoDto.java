package com.sasmac.usermanager.common;
import java.util.ArrayList;
import java.util.List;
//sdd548,用户信息管理容器
public class UserinfoDto {
	private String userID;//用户ID
	private String userNAME;//用户名
	private String ifVALID;//有效性
	private String ifONLINE;//有效性
	private String roleID;//角色ID
	private String roleNAME;//角色名
	private String regTIME;//注册时间
	private List<Privinfo> privList;//权限列表
	public UserinfoDto(){
		this.privList=new ArrayList<Privinfo>(); 
	}
	public UserinfoDto(Userinfo user,Roleinfo role){
		this.userID = user.getUserid();
		this.userNAME=user.getUsername();
		this.ifVALID=user.getIfvalid();
		this.ifONLINE=user.getIfonline();
		this.regTIME=user.getRegtime();
		this.roleNAME=role.getRolename();
		this.roleID=role.getRoleid();
		this.privList=role.getPrivlist();
	}
	public boolean IsContainPriv(String PrivName)
	{
		if(PrivName.isEmpty()) return false;
		int isize = privList.size();
		if(isize <=0) return false;
		for(int i=0;i<isize;i++)
		{
			if(privList.get(i).getPrivname().compareToIgnoreCase(PrivName)==0)
			{
				return true;
			}
		}
		return false;
	}
	/**
	 *@Description 修改以下两个函数名，setUser改为changeUser
	 *			 为解决java对象转json存在问题
	 *@author SUNJ
	 *@date 2017年2月13日下午2:13:13
	 */
	public void changeUser(Userinfo user){
		this.userID = user.getUserid();
		this.userNAME=user.getUsername();
		this.regTIME=user.getRegtime();
		this.ifVALID=user.getIfvalid();
		this.ifONLINE=user.getIfonline();
	}
	public void changeRole(Roleinfo role){
		this.roleNAME=role.getRolename();
		this.roleID=role.getRoleid();
		this.privList=role.getPrivlist();
	}
	
	public String getRegtime() {
		return regTIME;
	}
	
	public String getUserid() {
		return userID;
	}
	public String getUsername() {
		return userNAME;
	}

	public String getIfvalid() {
		return ifVALID;
	}
	public String getIfonline() {
		return ifONLINE;
	}

	public String getRolename() {
		return roleNAME;
	}
	public String getRoleid() {
		return roleID;
	}
	public List<Privinfo> getPrivlist() {
		return privList;
	}
}
