package com.sasmac.usermanager.common;
//sdd548,用户角色关联容器
public class Assinfo {
	private String assID;//关联ID
	private String assUID;//用户ID
	private String assRID;//角色ID
	public Assinfo(){}
	public Assinfo(String assid,String assuid,String assrid){
		this.assID = assid;
		this.assUID = assuid;
		this.assRID = assrid;
	
	}
	public String getAssid() {
		return assID;
	}
	public void setAssid(String assid) {
		this.assID = assid;
	}
	public String getAssuid() {
		return assUID;
	}
	public void setAssuid(String assuid) {
		this.assUID = assuid;
	}
	public String getAssprid() {
		return assRID;
	}
	public void setAssprid(String assrid) {
		this.assRID = assrid;
	}
	

}
