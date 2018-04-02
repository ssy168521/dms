package com.sasmac.usermanager.common;

import java.util.ArrayList;
import java.util.List;


//sdd548,角色容器
public class Roleinfo {
	private String roleID;//角色ID
	private String roleNAME;//角色名
	private List<Privinfo> privList;//权限列表
	public Roleinfo(){
		this.privList=new ArrayList<Privinfo>();	
	}
	public Roleinfo(String roleid,String rolename){
		this.roleID = roleid;
		this.roleNAME = rolename;
		this.privList=new ArrayList<Privinfo>();		
	}
	public String getRoleid() {
		return roleID;
	}
	public void setRoleid(String roleid) {
		this.roleID = roleid;
	}
	public String getRolename() {
		return roleNAME;
	}
	public void setRolename(String rolename) {
		this.roleNAME = rolename;
	}
	public List<Privinfo> getPrivlist() {
		return privList;
	}
	public void addPrivlist(Privinfo priv) {
		this.privList.add(priv);
	}
	
}
