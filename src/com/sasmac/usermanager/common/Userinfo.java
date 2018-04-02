package com.sasmac.usermanager.common;

import com.sasmac.util.PasswordUtil;

//sdd548，用户容器
public class Userinfo {
	private String userID;// 用户ID
	private String userNAME;// 用户名
	private String passWORD;// 密码
	private String regTIME;// 注册时间
	private String ifVALID;// 有效性
	private String ifONLINE;// 在线状态

	public Userinfo() {
	}

	public Userinfo(String userid, String username, String pwd, String regtime,
			String ifvalid, String ifonline) {
		this.userID = userid;
		this.userNAME = username;
		this.passWORD = pwd;
		this.regTIME = regtime;
		this.ifVALID = ifvalid;
		this.ifONLINE = ifonline;
	}

	public String getUserid() {
		return userID;
	}

	public void setUserid(String userid) {
		this.userID = userid;
	}

	public String getUsername() {
		return userNAME;
	}

	public void setUsername(String username) {
		this.userNAME = username;
	}

	public String getPwd() {
		return passWORD;
	}

	public void setPwd(String pwd) {
		this.passWORD = PasswordUtil.getBase64(pwd);
	}

	public String getRegtime() {
		return regTIME;
	}

	public void setRegtime(String regtime) {
		this.regTIME = regtime;
	}

	public String getIfvalid() {
		return ifVALID;
	}

	public void setIfvalid(String ifvalid) {
		this.ifVALID = ifvalid;
	}

	public String getIfonline() {
		return ifONLINE;
	}

	public void setIfonline(String ifonline) {
		this.ifONLINE = ifonline;
	}

}
