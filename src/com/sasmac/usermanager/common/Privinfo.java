package com.sasmac.usermanager.common;
//sdd548,权限容器
public class Privinfo {
	private String privID;//权限ID
	private String privNAME;//权限名
	public Privinfo(){}
	public Privinfo(String privid,String privname){
		this.privID = privid;
		this.privNAME = privname;
	}
	public String getPrivid() {
		return privID;
	}
	public void setPrivid(String privid) {
		this.privID = privid;
	}
	public String getPrivname() {
		return privNAME;
	}
	public void setPrivname(String privname) {
		this.privNAME = privname;
	}

}
