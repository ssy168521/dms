package com.sasmac.usermanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sasmac.usermanager.common.Privinfo;
import com.sasmac.usermanager.common.Roleinfo;
import com.sasmac.usermanager.common.Userinfo;
import com.sasmac.usermanager.common.UserinfoDto;
import com.sasmac.util.PasswordUtil;

//用户操作 
public class UserDao {
	private Connection userCN = null;
	private UserinfoDto myUserdto;

	/**
	 * SUNJ 2017.2.11修改 修改构造函数 UserDao(ConnectionMyDB cndb)
	 * 使用jdbc的Connection替换这个ConnectionMyDB类
	 */
	public UserDao(Connection cndb) {
		this.userCN = cndb;
		this.myUserdto = new UserinfoDto();
	}

	public UserinfoDto getUserdto() {
		return myUserdto;
	}

	// 注册
	public boolean regNew(String uname, String pword) {
		boolean result = false;
		Userinfo myuser = new Userinfo();
		// 获取当前时间为注册时间
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String nowTime = "";
		nowTime = df.format(now);
		// 查用户名合法性
		if (uname != null) {
			String sql = "select USERNAME from TB_USER where USERNAME=?";
			try {
				PreparedStatement psmt = userCN.prepareStatement(sql);
				psmt.setString(1, uname);
				ResultSet rs = psmt.executeQuery();
				if (rs.next() == false) {
					myuser.setUsername(uname);
					myuser.setPwd(pword);
					myuser.setRegtime(nowTime);
					// 写用户表，注册成功
					result = insertUser(myuser);
				} else {
					// 返回用户名已存在
					System.out.println("用户名已存在 ");
				}
				psmt.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//
		}
		return result;
	}

	// 注册写用户表
	public boolean insertUser(Userinfo user) {
		boolean result = false;
		String name = user.getUsername();
		String pass = user.getPwd();
		String regtime = user.getRegtime();
		String sql = "insert into TB_USER values(null,?,?,?,true,false)";// 写用户表,ID自增长
		try {
			PreparedStatement psmt = userCN.prepareStatement(sql);
			psmt.setString(1, name);
			psmt.setString(2, pass);
			psmt.setString(3, regtime);
			int rs = psmt.executeUpdate();
			if (rs > 0) {
				System.out.println(name + "注册成功");
				result = true;
			}
			psmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//
		return result;
	}

	// 登录
	public boolean login(String name, String pass) {
		if (userCN == null)
			return false;
		boolean result = false;
		Userinfo myuser = new Userinfo();
		Roleinfo myrole = new Roleinfo();
		String sqlU = "select ID,USERNAME,VALID,ONLINE from TB_USER where USERNAME=? and PASSWORD=?";
		String sqlA = "select ROLEID from TB_USERROLE where USERID=?";
		String sqlR = "select ROLENAME from TB_ROLE where ID=?";
		String sqlL = "select PRIVILEDGEID from TB_ROLEPRIVILEDGE where ROLEID=?";
		String sqlP = "select ID,PRIVILEDGENAME from TB_PRIVILEDGE where ID=?";
		String sqlO = "update TB_USER set ONLINE=? where id=?";
		try {
			pass = PasswordUtil.getBase64(pass);
			// 查用户表
			PreparedStatement psmtU = userCN.prepareStatement(sqlU);//
			psmtU.setString(1, name);
			psmtU.setString(2, pass);
			ResultSet rsU = psmtU.executeQuery();
			if (rsU.next()) {
				// 登录成功，读用户信息
				myuser.setUserid(rsU.getString(1));
				myuser.setUsername(rsU.getString(2));
				myuser.setIfvalid(rsU.getString(3));
				boolean bisLogined = rsU.getBoolean(4);
				// 查用户角色表
				PreparedStatement psmtA = userCN.prepareStatement(sqlA);
				psmtA.setString(1, rsU.getString(1));
				ResultSet rsA = psmtA.executeQuery();
				if (rsA.next()) {
					// 有角色，读角色信息
					myrole.setRoleid(rsA.getString(1));
					// 查角色表，读角色名
					PreparedStatement psmtR = userCN.prepareStatement(sqlR);
					psmtR.setString(1, rsA.getString(1));
					ResultSet rsR = psmtR.executeQuery();
					if (rsR.next()) {
						myrole.setRolename(rsR.getString(1));
						// 查角色权限表
						PreparedStatement psmtL = userCN.prepareStatement(sqlL);
						psmtL.setString(1, rsA.getString(1));
						ResultSet rsL = psmtL.executeQuery();// 列
						while (rsL.next()) {
							// 循环角色关联的 权限ID，查权限表，读权限信息
							PreparedStatement psmtP = userCN
									.prepareStatement(sqlP);
							psmtP.setString(1, rsL.getString(1));
							ResultSet rsP = psmtP.executeQuery();// 列
							if (rsP.next()) {
								Privinfo myPri = new Privinfo(rsP.getString(1),
										rsP.getString(2));
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
				}
				// 构造当前用户dto
				myUserdto.changeUser(myuser);
				myUserdto.changeRole(myrole);
				result = true;
				// 修改用户表在线状态
				PreparedStatement psmtO = userCN.prepareStatement(sqlO);
				psmtO.setBoolean(1, true);
				psmtO.setString(2, rsU.getString(1));
				psmtO.executeUpdate();
				psmtO.close();
				psmtA.close();
				rsA.close();
				System.out.println(myUserdto.getUsername() + "登录成功！");
			} else {
				// 返回用户名密码错误
				System.out.println("用户名密码错误");
			}
			psmtU.close();
			rsU.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 登出
	public boolean logout() {
		boolean result = false;
		// 修改用户表在线状态
		String sql = "update TB_USER set ONLINE=? where id=?";
		try {
			PreparedStatement psmt;
			psmt = userCN.prepareStatement(sql);
			psmt.setBoolean(1, false);
			psmt.setString(2, myUserdto.getUserid());
			int rs = psmt.executeUpdate();
			if (rs > 0) {
				result = true;
			}
			psmt.close();
			System.out.println(myUserdto.getUsername() + "已退出");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public boolean modifyPsw(String newpass) {
		boolean result = false;
		// 修改密码
		String sql = "update TB_USER set PASSWORD=? where id=?";
		try {
			PreparedStatement psmt;
			psmt = userCN.prepareStatement(sql);
			newpass = PasswordUtil.getBase64(newpass);
			psmt.setString(1, newpass);
			psmt.setString(2, myUserdto.getUserid());
			int rs = psmt.executeUpdate();
			if (rs > 0) {
				result = true;
				System.out.println(myUserdto.getUsername() + "密码修改成功");
			}
			psmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 判断是否具有某一权限
	public boolean IsContainPriv(String username, String privname) {

		// String
		// sql="select t1.username,t3.ROLEID ,t4.PRIVILEDGEID,t5.PRIVILEDGENAME from  tb_user t1, tb_userrole t3 , tb_rolepriviledge t4 ,tb_priviledge t5 where  t1.username=? and t1.id=t3.USERID and t4.ROLEID=t3.ROLEID and t4.PRIVILEDGEID=t5.id";
		String sql = "select t1.username,t3.ROLEID ,t4.PRIVILEDGEID,t5.PRIVILEDGENAME from  tb_user t1, tb_userrole t3 , tb_rolepriviledge t4 ,tb_priviledge t5 where  t1.username=? and t1.id=t3.USERID and t4.ROLEID=t3.ROLEID and t4.PRIVILEDGEID=t5.id and t5.PRIVILEDGENAME=?";
		boolean result = false;
		try {
			PreparedStatement psmtU = userCN.prepareStatement(sql);//
			psmtU.setString(1, username);
			psmtU.setString(2, privname);
			ResultSet rsU = psmtU.executeQuery();
			int cunt = 0;
			// rsU.
			if (rsU.next()) {
				cunt++;
			} else {

				System.out.println("没有该权限！");
			}

			psmtU.close();
			rsU.close();

			if (cunt > 0)
				result = true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
