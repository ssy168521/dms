package com.sasmac.usermanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sasmac.usermanager.common.Privinfo;
import com.sasmac.usermanager.common.Roleinfo;
import com.sasmac.usermanager.common.Userinfo;
import com.sasmac.usermanager.common.UserinfoDto;
import com.sasmac.util.PasswordUtil;

//sdd548,用户角色管理
public class RoleDao {
	private Connection userCN = null;
	private List<UserinfoDto> allUserdto;
	private List<Roleinfo> allRole;

	/**
	 * SUNJ 2017.2.11修改 修改构造函数 RoleDao(ConnectionMyDB cndb)
	 * 使用jdbc的Connection替换这个ConnectionMyDB类
	 */
	public RoleDao(Connection cndb) {

		this.userCN = cndb;
		this.allUserdto = new ArrayList<UserinfoDto>();
		this.allRole = new ArrayList<Roleinfo>();
	}

	// 更新所有角色列表
	public boolean upAllRole() {
		boolean result = false;
		allRole.clear();
		String sql = "select ID,ROLENAME from TB_ROLE";
		try {
			PreparedStatement psmt = userCN.prepareStatement(sql);//
			ResultSet rs = psmt.executeQuery();
			while (rs.next()) {
				Roleinfo myrole = new Roleinfo();
				myrole.setRoleid(rs.getString(1));
				myrole.setRolename(rs.getString(2));
				allRole.add(myrole);
			}
			psmt.close();
			rs.close();
			result = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public List<Roleinfo> getRolelist() {
		upAllRole();
		return allRole;
	}

	// 更新所有用户列表
	public boolean upAllUser() {
		boolean result = false;
		allUserdto.clear();// 清空用户列表
		String sqlU = "select ID,USERNAME,REGTIME,VALID,ONLINE from TB_USER";
		String sqlA = "select ROLEID from TB_USERROLE where USERID=?";
		String sqlR = "select ROLENAME from TB_ROLE where ID=?";
		try {
			// 查用户表
			PreparedStatement psmtU = userCN.prepareStatement(sqlU);//
			ResultSet rsU = psmtU.executeQuery();
			while (rsU.next()) {
				// 用户角色关联
				UserinfoDto myUserinfoDto = new UserinfoDto();
				Userinfo myuser = new Userinfo();
				Roleinfo myrole = new Roleinfo();
				// 读用户信息
				myuser.setUserid(rsU.getString(1));
				myuser.setUsername(rsU.getString(2));
				myuser.setRegtime(rsU.getString(3));
				myuser.setIfvalid(rsU.getString(4));
				myuser.setIfonline(rsU.getString(5));
				// 查关联表
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

					}
					psmtR.close();
					rsR.close();
				}
				myUserinfoDto.changeUser(myuser);
				myUserinfoDto.changeRole(myrole);
				// 加入列表
				allUserdto.add(myUserinfoDto);
				psmtA.close();
				rsA.close();
			}
			psmtU.close();
			rsU.close();
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<UserinfoDto> getUserlist() {
		upAllUser();
		return allUserdto;
	}

	// 重置密码
	public boolean resetPsw(String thisuserid) {
		boolean result = false;
		// 重置密码为1234abcd
		String sql = "update TB_USER set PASSWORD=? where id=?";
		try {
			PreparedStatement psmt;
			psmt = userCN.prepareStatement(sql);
			psmt.setString(1, "1234abcd");
			psmt.setString(2, thisuserid);
			int rs = psmt.executeUpdate();
			if (rs > 0) {
				result = true;
			}
			psmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 修改密码
	public boolean modifyPsw(String thisuserid, String newPsw) {
		boolean result = false;
		String sql = "update TB_USER set PASSWORD=? where id=?";
		try {
			PreparedStatement psmt;
			psmt = userCN.prepareStatement(sql);
			newPsw = PasswordUtil.getBase64(newPsw);
			psmt.setString(1, newPsw);
			psmt.setString(2, thisuserid);
			int rs = psmt.executeUpdate();
			if (rs > 0) {
				result = true;
			}
			psmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 修改有效性
	public boolean modifyValid(String thisuserid, boolean ifvalid) {
		boolean result = false;
		// 修改有效性
		String sql = "update TB_USER set VALID=? where id=?";
		try {
			PreparedStatement psmt;
			psmt = userCN.prepareStatement(sql);
			psmt.setBoolean(1, ifvalid);
			psmt.setString(2, thisuserid);
			int rs = psmt.executeUpdate();
			if (rs > 0) {
				result = true;
			}
			psmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 修改用户角色
	public boolean modifyRole(String thisuserid, String roleid) {
		boolean result = false;
		// 修改用户角色
		String sqlD = "delete from TB_USERROLE where USERID=?";
		String sqlI = "insert into TB_USERROLE values(null,?,?)";// 自增长
		try {
			PreparedStatement psmtD;
			psmtD = userCN.prepareStatement(sqlD);
			psmtD.setString(1, thisuserid);
			psmtD.executeUpdate();
			PreparedStatement psmtI;
			psmtI = userCN.prepareStatement(sqlI);
			psmtI.setString(1, thisuserid);
			psmtI.setString(2, roleid);
			int rs = psmtI.executeUpdate();
			if (rs > 0) {
				result = true;
			}
			psmtI.close();
			psmtD.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 新建用户
	public boolean regNew(String uname, String pword, String ifvaild) {
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
					myuser.setIfvalid(ifvaild);
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

	// 写用户表
	public boolean insertUser(Userinfo user) {
		boolean result = false;
		String name = user.getUsername();
		String pass = user.getPwd();
		String regtime = user.getRegtime();
		String ifvaild = user.getIfvalid();
		String sql = "insert into TB_USER values(null,?,?,?,?,false)";// 写用户表,ID自增长
		try {
			PreparedStatement psmt = userCN.prepareStatement(sql);
			psmt.setString(1, name);
			psmt.setString(2, pass);
			psmt.setString(3, regtime);
			psmt.setString(4, ifvaild);
			int rs = psmt.executeUpdate();
			if (rs > 0) {
				System.out.println(name + "添加成功");
				result = true;
			}
			psmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//
		return result;
	}

	// 删除用户
	public boolean deletUser(String thisuserid) {
		boolean result = false;
		// 删除用户及用户角色
		String sqlU = "delete from TB_USER where ID=?";
		String sqlA = "delete from TB_USERROLE where USERID=?";
		try {
			PreparedStatement psmtU;
			psmtU = userCN.prepareStatement(sqlU);
			psmtU.setString(1, thisuserid);
			int rs = psmtU.executeUpdate();
			if (rs > 0) {
				result = true;
			}
			PreparedStatement psmtA;
			psmtA = userCN.prepareStatement(sqlA);
			psmtA.setString(1, thisuserid);
			psmtA.executeUpdate();
			psmtA.close();
			psmtU.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 通过用户名获取用户id
	public String usernameToUid(String uname) {
		String uid = "";
		String sql = "select ID from TB_USER where USERNAME=?";
		try {
			PreparedStatement psmt = userCN.prepareStatement(sql);
			psmt.setString(1, uname);
			ResultSet rs = psmt.executeQuery();
			if (rs.next()) {
				uid = rs.getString(1);
			} else {
				System.out.println("用户名不存在 ");
			}
			psmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return uid;
	}

	// 获取指定用户详情
	public UserinfoDto getUserinfodto(String thisuserid) {

		UserinfoDto myUserdto = new UserinfoDto();
		Userinfo myuser = new Userinfo();
		Roleinfo myrole = new Roleinfo();
		String sqlU = "select ID,USERNAME,REGTIME,VALID,ONLINE from TB_USER where ID=?";
		String sqlA = "select ROLEID from TB_USERROLE where USERID=?";
		String sqlR = "select ROLENAME from TB_ROLE where ID=?";
		String sqlL = "select PRIVILEDGEID from TB_ROLEPRIVILEDGE where ROLEID=?";
		String sqlP = "select ID,PRIVILEDGENAME from TB_PRIVILEDGE where ID=?";

		try {
			// 查用户表
			PreparedStatement psmtU = userCN.prepareStatement(sqlU);//
			psmtU.setString(1, thisuserid);
			ResultSet rsU = psmtU.executeQuery();
			if (rsU.next()) {
				myuser.setUserid(rsU.getString(1));
				myuser.setUsername(rsU.getString(2));
				myuser.setRegtime(rsU.getString(3));
				myuser.setIfvalid(rsU.getString(4));
				myuser.setIfonline(rsU.getString(5));
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

				psmtA.close();
				rsA.close();
			} else {

			}
			psmtU.close();
			rsU.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return myUserdto;
	}

}
