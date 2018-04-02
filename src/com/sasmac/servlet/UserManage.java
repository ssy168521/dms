package com.sasmac.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.functors.TruePredicate;

import com.sasmac.test.DebugFun;
import com.sasmac.usermanager.common.Roleinfo;
import com.sasmac.usermanager.common.UserinfoDto;
import com.sasmac.usermanager.service.ManagementService;

public class UserManage extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UserManage() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 设置response的编码
		response.setCharacterEncoding("utf-8");
		// 设置request的编码
		request.setCharacterEncoding("utf-8");
		
		String btnAddUser = request.getParameter("btnAddUser");
		String btnDelSelecUser=request.getParameter("btnDelSelecUser");
		String btnDelUser= request.getParameter("btnDelUser");
		String btnViewUser=request.getParameter("btnViewUser");
		String btnModifyUser=request.getParameter("btnModifyUser");
		PrintWriter out=response.getWriter();		
		//DebugFun.showParams(request);
		
		if(btnAddUser!=null)
		{
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String roleid = request.getParameter("roleid");
			String ifvaild = request.getParameter("ifvaild");
			ManagementService manaSer=new ManagementService();
			boolean regResu=manaSer.getRoleDao().regNew(username, password, ifvaild);
			if(!regResu){
				out.print("用户信息添加出错！");
				System.out.println("用户信息添加出错！");
			}
			String userid=manaSer.getRoleDao().usernameToUid(username);
			boolean modifyRoleResu=manaSer.getRoleDao().modifyRole(userid, roleid);
			if(modifyRoleResu&&regResu){
				out.print(modifyRoleResu&&regResu);
			}else {
				out.print("用户的角色设置出错！");
				System.out.println("用户的角色设置出错！");
			}
			manaSer.closeService();
		}else if(btnDelUser!=null){
			System.out.println("用户删除……");
			String userid= request.getParameter("userid");
			ManagementService manaServ=new ManagementService();
			boolean result= manaServ.getRoleDao().deletUser(userid);
			if(result){
				out.print(result);
			}
			else {
				out.print("用户删除失败！");
				System.out.println("用户删除失败！");
			}
			manaServ.closeService();
		}else if(btnDelSelecUser!=null){
			System.out.println("选中用户删除……");
			String objSelec= request.getParameter("objSelec");
			JSONArray jsonArr=JSONArray.fromObject(objSelec);
			ManagementService manaServ=new ManagementService();
			for (int i=0;i<jsonArr.size();i++)
			{
				JSONObject jsonObj=jsonArr.getJSONObject(i);
				String userid=jsonObj.get("userid").toString();
				boolean result= manaServ.getRoleDao().deletUser(userid);
				if(result){
					out.print(result);
				}
				else {
					out.print("用户"+jsonObj.get("rolename").toString()+"删除失败！");
					System.out.println("用户"+jsonObj.get("rolename").toString()+"删除失败！");
				}
			}
			manaServ.closeService();
		}else if(btnViewUser!=null){
			System.out.println("查看用户详情……");
			String userid= request.getParameter("userid");
			ManagementService manaServ=new ManagementService();
			UserinfoDto userinfodto=manaServ.getRoleDao().getUserinfodto(userid);
			/*JSONArray jsonArray=JSONArray.fromObject(userinfodto);
			String jsonUserList=jsonArray.toString();*/
			JSONObject jsonObj=JSONObject.fromObject(userinfodto);
			String jsonUserinfodto=jsonObj.toString();
			
			out.print(jsonUserinfodto);
			System.out.println(jsonUserinfodto);
			manaServ.closeService();
			//JSONObject jsonObj=JSONObject.fromObject(objSelec);
		}else if(btnModifyUser!=null){
			System.out.println("修改用户……");
			String username=request.getParameter("username");
			String roleid=request.getParameter("roleid");
			String ifvalid=request.getParameter("ifvalid");
			String password=request.getParameter("password");
			ManagementService manaServ=new ManagementService();
			String userid=manaServ.getRoleDao().usernameToUid(username);
			boolean resu1= manaServ.getRoleDao().modifyRole(userid, roleid);
			if(!resu1){
				out.print("修改用户角色出错！");
			}
			boolean resu2=false;
			if(ifvalid.equals("1")){
				resu2= manaServ.getRoleDao().modifyValid(userid, true);
			}else {
				resu2= manaServ.getRoleDao().modifyValid(userid, false);
			}
			if(!resu2){
				out.print("修改用户角色出错！");
			}
			boolean resu3=true;
			if(password!=null)
			{
				resu3=manaServ.getRoleDao().modifyPsw(userid, password);
				if(!resu3){
					out.print("修改用户密码出错！");
				}
			}
			if(resu1&&resu2&&resu3){
				out.print(true);
			}			
			manaServ.closeService();
		}
		

		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
