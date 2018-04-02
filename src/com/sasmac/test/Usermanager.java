package com.sasmac.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.sasmac.usermanager.common.Privinfo;
import com.sasmac.usermanager.common.Roleinfo;
import com.sasmac.usermanager.common.UserinfoDto;
import com.sasmac.usermanager.service.ManagementService;
import com.sasmac.usermanager.service.UserService;

public class Usermanager {

	public Usermanager() {
		// TODO Auto-generated constructor stub
	}
	
	public static void showUserView(){
		Scanner input = new Scanner(System.in);
		UserService userServ = new UserService();
		while(true){
			boolean rs=false;
			System.out.println("==用户使用界面==");
			System.out.println("======请选择======");
			System.out.println("--1.登录  --2.注册--");
			int c1 = input.nextInt();
			if(c1==2){
				System.out.println("输入用户名 ");
				String name = input.next();
				System.out.println("输入密码");
				String pass = input.next();				 
				rs = userServ.getUserDao().regNew(name, pass);
			}
			else if(c1==1){
				System.out.println("输入用户名 ");
				String name = input.next();
				System.out.println("输入密码");
				String pass = input.next();				  
				rs = userServ.getUserDao().login(name, pass);
				 //显示登录用户信息
				showUserinfo(userServ);
				 //
				System.out.println("======请选择======");
				System.out.println("--1.登出  --2.改密--");
				int c2 = input.nextInt();
				if(c2==2){
					System.out.println("输入新密码"); 
					String newpas = input.next();
					userServ.getUserDao().modifyPsw(newpas);
					userServ.getUserDao().logout();
				}
				else if(c2==1){
					userServ.getUserDao().logout();
				}
			}
				if(rs == true){					
					System.out.println("ok");
					break;
				}
				else{
					System.out.println("err");
					break;
				}					
		     }
		input.close();
	}
	
	public static void showManagementView(){
		Scanner input = new Scanner(System.in);
		ManagementService manaServ = new ManagementService();
		while(true){
			boolean rs=false;
			System.out.println("==管理员使用界面==");
			System.out.println("==请用admin用户登录==");
			System.out.println("输入用户名 ");
			String name = input.next();
			System.out.println("输入密码");
			String pass = input.next();
			rs = manaServ.getUserDao().login(name, pass);	
			if(rs==true){				 
			System.out.println("--1.用户管理  --2.角色管理--");
			int c1 = input.nextInt();
			if(c1==1){
				List<UserinfoDto> userlist=manaServ.getRoleDao().getUserlist();
				List<Roleinfo> rolelist=manaServ.getRoleDao().getRolelist();
				showUserlist(userlist);
				System.out.println("请选择要操作的用户（输入用户ID） ");
				String userid = input.next();//判断userid是否存在？
				System.out.println("--1.删除  --2.重置密码--3.修改有效性--4.修改角色--");
				int c2 = input.nextInt();
				if(c2==1){
					manaServ.getRoleDao().deletUser(userid);
				}
				else if(c2==2){
					manaServ.getRoleDao().resetPsw(userid);
				}
				else if(c2==3){
					System.out.println("--1.有效  --0.无效--");
					int c3 = input.nextInt();
					if(c3==1){
						manaServ.getRoleDao().modifyValid(userid, true);
					}
					else if(c3==0){
						manaServ.getRoleDao().modifyValid(userid, false);
					}
				}
				else if(c2==4){                	 
					showRolelist(rolelist);
					System.out.println("--请选择要赋予的角色（输入角色ID）--");
					String roleid = input.next();
					manaServ.getRoleDao().modifyRole(userid, roleid);
				}
			}
			else if(c1==2){
				List<Roleinfo> rolelist=manaServ.getPrivDao().getRolelist();
				List<Privinfo> privlist=manaServ.getPrivDao().getPrivlist();
				showRolelist(rolelist);
				System.out.println("--1.新建角色  --2.角色管理--");
				int c4 = input.nextInt();
				if(c4==1){
					System.out.println("输入新建角色名 ");
					String rolename = input.next();
					manaServ.getPrivDao().createRole(rolename);
					rolelist=manaServ.getPrivDao().getRolelist();
					showRolelist(rolelist);
				}
				else if(c4==2){
					System.out.println("请选择要操作的角色（输入角色ID） ");
					String roleid = input.next();
					System.out.println("--1.删除  --2.重命名--3.选择权限--");
					int c5 = input.nextInt();
					if(c5==1){
						manaServ.getPrivDao().deletRole(roleid);
						}
					else if(c5==2){
						System.out.println("输新角色名 ");
						String rolename = input.next();
						manaServ.getPrivDao().reName(roleid, rolename);
					}
					else if(c5==3){
						showPrivlist(privlist);						 
						System.out.println("请选择要给当前选中角色赋予的权限（权限ID，用回车隔开，输入“ok”确认）");
						List<String> list = new ArrayList<String>();
						String privid;
						while (!"ok".equals(privid =input.next())) {
							list.add(privid);
						}
						System.out.println("给当前选中角色赋予的权限:");
						if (list!=null){			 
							for(int i=0; i<list.size();i++){
								System.out.println("权限ID:"+list.get(i));
							}
						}
						manaServ.getPrivDao().modifyPriv(roleid, list);
					}
				}				 
			 }
			 }
			 else{
				 System.out.println("==未用admin用户登录，无权限==");
			 }
				if(rs == true){					
					System.out.println("ok");
					break;
				}
				else{
					System.out.println("err");
					break;
				}					
		     }
			manaServ.closeService();
			input.close();
		}

  public static void main(String[] args){
/*	  ManagementService manageServ=new ManagementService();
	  UserinfoDto myuseDto=manageServ.getRoleDao().getUserinfodto("8");
	  
	  
	  */
	  Scanner maininput = new Scanner(System.in);
	  System.out.println("--1.用户 操作--2.管理员操作--");
	  int c = maininput.nextInt();
	  if(c==1){
	  showUserView();
	  }
	  else if(c==2){
	  showManagementView();
	  }
	  maininput.close();
  }
  
  public static void showUserinfo(UserService userServ){
	     System.out.println("在线状态:"+userServ.getUserDao().getUserdto().getIfonline());
		 System.out.println("有效状态:"+userServ.getUserDao().getUserdto().getIfvalid());				 
		 System.out.println("角色ID:"+userServ.getUserDao().getUserdto().getRoleid());
		 System.out.println("角色名:"+userServ.getUserDao().getUserdto().getRolename());
		 System.out.println("用户ID:"+userServ.getUserDao().getUserdto().getUserid());
		 System.out.println("用户名:"+userServ.getUserDao().getUserdto().getUsername());
		 List<Privinfo> prividlist=userServ.getUserDao().getUserdto().getPrivlist();
		 if (prividlist!=null){			 
		 for(int i=0; i<prividlist.size();i++){
			System.out.println("权限ID:"+prividlist.get(i).getPrivid()+"---权限名:"+prividlist.get(i).getPrivname());
		      }
		  }
  }
  
  public static void showUserlist(List<UserinfoDto> userlist){
	  
	     System.out.println("--用户ID---"+"--用户名---"+"--有效状态---"+"--在线状态---"+"--角色名--");	     
    	 if (userlist!=null){
			 for(int i=0; i<userlist.size();i++){
				System.out.println("-"+userlist.get(i).getUserid()+"---"+userlist.get(i).getUsername()+"---"
			                       +userlist.get(i).getIfvalid()+"---"+userlist.get(i).getIfonline()+"---"
			                       +userlist.get(i).getRolename()+"-");
			 }
		  }
    	 System.out.println("----------------------endline---------------------------");	
}
  
  public static void showRolelist(List<Roleinfo> rolelist){
	  
	     System.out.println("--角色ID---"+"--角色名--");	     
 	 if (rolelist!=null){			 
		 for(int i=0; i<rolelist.size();i++){
			System.out.println("-"+rolelist.get(i).getRoleid()+"---"+rolelist.get(i).getRolename()+"-");
		      }
		  }
 	 System.out.println("----------------------endline---------------------------");	
}
  
  public static void showPrivlist(List<Privinfo> privlist){
	  
	     System.out.println("--权限ID---"+"--权限名--");	     
	 if (privlist!=null){			 
		 for(int i=0; i<privlist.size();i++){
			System.out.println("-"+privlist.get(i).getPrivid()+"---"+privlist.get(i).getPrivname()+"-");
		      }
		  }
	 System.out.println("----------------------endline---------------------------");	
}

}
