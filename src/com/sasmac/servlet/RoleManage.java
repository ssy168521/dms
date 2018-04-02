package com.sasmac.servlet;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sasmac.test.DebugFun;
import com.sasmac.usermanager.common.Roleinfo;
import com.sasmac.usermanager.service.ManagementService;

public class RoleManage extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public RoleManage() {
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
		PrintWriter out=response.getWriter();
		String btnAddRole = request.getParameter("btnAddRole");
		String btnDelSelecRole=request.getParameter("btnDelSelecRole");
		String btnDelRole = request.getParameter("btnDelRole");
		String btnModifyRole = request.getParameter("btnModifyRole");
		String btnViewRole = request.getParameter("btnViewRole");
		
		//DebugFun.showParams(request);
		if(btnAddRole!=null){
			String rolename = request.getParameter("rolename");
			String[] SelePrivs = request.getParameterValues("SelePrivs");			
			List<String> privIdlist = new ArrayList<String>();
			for(int i=0;i<SelePrivs.length;i++)
				privIdlist.add(SelePrivs[i]);
			
			ManagementService manaSer=new ManagementService();
			boolean createResu= manaSer.getPrivDao().createRole(rolename);
			if(!createResu){
				out.print("角色创建出错！");
				System.out.println("角色创建出错！");
			}
			String roleid=manaSer.getPrivDao().rolenameToRid(rolename);
			boolean modifyRoleResu=manaSer.getPrivDao().modifyPriv(roleid, privIdlist);
			if(modifyRoleResu&&createResu){
				out.print(modifyRoleResu&&createResu);
			}else {
				out.print("角色权限设置出错！");
				System.out.println("角色权限设置出错！");
			}
			manaSer.closeService();
		}else if(btnDelSelecRole!=null){
			System.out.println("选中角色删除……");
			String objSelec= request.getParameter("objSelec");
			JSONArray jsonArr=JSONArray.fromObject(objSelec);
			ManagementService manaServ=new ManagementService();
			//boolean result= manaServ.getPrivDao().deletRole(roleid);
			for (int i=0;i<jsonArr.size();i++)
			{
				JSONObject jsonObj=jsonArr.getJSONObject(i);
				String roleid=jsonObj.get("roleid").toString();
				boolean result= manaServ.getPrivDao().deletRole(roleid);
				if(result){
					out.print(result);
				}
				else {
					out.print("角色"+jsonObj.get("rolename").toString()+"删除失败！");
					System.out.println("角色"+jsonObj.get("rolename").toString()+"删除失败！");
				}
			}
			manaServ.closeService();
		}else if(btnDelRole!=null){
			System.out.println("角色删除……");
			String roleid= request.getParameter("roleid");
			ManagementService manaServ=new ManagementService();
			boolean result= manaServ.getPrivDao().deletRole(roleid);
			if(result){
				out.print(result);
			}
			else {
				out.print("角色删除失败！");
				System.out.println("角色删除失败！");
			}
			manaServ.closeService();
		}else if(btnModifyRole!=null){
			String rolename = request.getParameter("rolename");
			String[] SelePrivs = request.getParameterValues("SelePrivs");			
			List<String> privIdlist = new ArrayList<String>();
			for(int i=0;i<SelePrivs.length;i++)
				privIdlist.add(SelePrivs[i]);
			ManagementService manaSer=new ManagementService();
			String roleid=manaSer.getPrivDao().rolenameToRid(rolename);			
			boolean modifyRoleResu=manaSer.getPrivDao().modifyPriv(roleid, privIdlist);			
			if(modifyRoleResu){
				out.print(modifyRoleResu);
			}else {
				out.print("角色权限设置出错！");
				System.out.println("角色权限设置出错！");
			}
			manaSer.closeService();
		}else if(btnViewRole!=null){
			String roleid = request.getParameter("roleid");			
			ManagementService manaSer=new ManagementService();
			Roleinfo roleinfo=manaSer.getPrivDao().getRoleinfo(roleid);
			JSONObject jsonObj=JSONObject.fromObject(roleinfo);
			String jsonRoleInfo=jsonObj.toString();
			
			out.print(jsonRoleInfo);
			System.out.println(jsonRoleInfo);
			manaSer.closeService();
			/*if(modifyRoleResu){
				out.print(modifyRoleResu);
			}else {
				out.print("角色权限设置出错！");
				System.out.println("角色权限设置出错！");
			}*/
			manaSer.closeService();
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
