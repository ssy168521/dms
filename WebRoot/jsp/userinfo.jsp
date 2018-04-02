<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'userinfo.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="./css/pintuer.css">
	<link rel="stylesheet" href="./css/admin.css">
	<link rel="stylesheet" href="./css/font-awesome.css">
	<script src="./lib/jquery.js"></script>
	<script src="./js/pintuer.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
<body>
	<div class="panel admin-panel">
	  <div class="panel-head" id="add"><strong><span class="icon-book"></span>用户信息详情</strong></div>
	  <div class="body-content" style="padding-left:100px;padding-top:50px;">
		<table >
			<tr height="45">
				<td width="100" style="text-align:right; padding-right:0px;">
					<label>用户ID：</label>
				</td>
				<td width="500" style="text-align:left; padding-left:10px;">
					<label id="labuserid">null</label>
				</td>
			</tr>
			<tr height="45">
				<td width="100" style="text-align:right; padding-right:0px;">
					<label>用户名：</label>
				</td>
				<td width="500" style="text-align:left; padding-left:10px;">
					<label id="labusername">null</label>
				</td>
			</tr>
			<tr height="45">
				<td width="100" style="text-align:right; padding-right:0px;">
					<label>角色名称：</label>
				</td>
				<td width="500" style="text-align:left; padding-left:10px;">
					<label id="labrolename">null</label>
				</td>
			</tr>
			<tr height="45">
				<td width="100" style="text-align:right; padding-right:0px;">
					<label>有效性：</label>
				</td>
				<td width="500" style="text-align:left; padding-left:10px;">
					<label id="labifvalid">null</label>
				</td>
			</tr>
			<tr height="45">
				<td width="100" style="text-align:right; padding-right:0px;">
					<label>在线状态：</label>
				</td>
				<td width="500" style="text-align:left; padding-left:10px;">
					<label id="labifonline">null</label>
				</td>
			</tr>
			<tr height="45">
				<td width="100" style="text-align:right; padding-right:0px;">
					<label>注册时间：</label>
				</td>
				<td width="500" style="text-align:left; padding-left:10px;">
					<label id="labregtime">null</label>
				</td>
			</tr>
			<tr height="45">
				<td width="100" style="text-align:right; padding-right:0px;">
					<label>权限信息：</label>
				</td>
				<td width="500" style="text-align:left; padding-left:10px;">
					<label id="labprivlist">null</label>
				</td>
			</tr>
		</table>
		
		
		  
	  </div>
	</div>
</body>
  
  <script>
  $(function () {
  	var userid="<%=request.getParameter("subuserid")%>";
  	var obj=eval("("+"{\"btnViewUser\":\"btnViewUser\",\"userid\":"+userid+"}"+")");
  	$.ajax({
		url:"servlet/UserManage",
		type:"POST",
		data:obj,
		async:false,
		error:function(request,textstatus,errorThrown){
			/* alert(errorThrown); */
			alert("Network Error 网络异常");
		},
		success:function(data){
			var obj=eval("("+data+")");
			$("#labuserid").text(obj.userid);
			$("#labusername").text(obj.username);
			$("#labrolename").text(obj.rolename);
			if(obj.ifvalid==0){
				$("#labifvalid").text("无效");
			}else{
				$("#labifvalid").text("有效");
			}
			if(obj.ifonline==0){
				$("#labifonline").text("离线");
			}else{
				$("#labifonline").text("在线");
			}
			$("#labregtime").text(obj.regtime);
			var objprivlist=obj.privlist;
			var strprivs="";
			for(var i=0;i<objprivlist.length;i++){
				strprivs=strprivs+objprivlist[i].privname+"<br>";				
			}
			$("#labprivlist").html(strprivs);
		}
	});
  });
  </script>
</html>
