<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'roledetail.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" href="./css/pintuer.css">
	<link rel="stylesheet" href="./css/admin.css">
	<link rel="stylesheet" href="./css/font-awesome.css">
	<script src="./lib/jquery.js"></script>
	<script src="./js/pintuer.js"></script>

  </head>
  
  <body>
    <div class="panel admin-panel">
	  <div class="panel-head" id="add"><strong><span class="icon-book"></span>角色信息详情</strong></div>
	  <div class="body-content" style="padding-left:100px;padding-top:50px;">
	    <table >
			<tr height="45">
				<td width="100" style="text-align:right; padding-right:0px;">
					<label>角色ID：</label>
				</td>
				<td width="500" style="text-align:left; padding-left:10px;">
					<label id="labroleid"> </label>
				</td>
			</tr>
			<tr height="45">
				<td width="100" style="text-align:right; padding-right:0px;">
					<label>角色名：</label>
				</td>
				<td width="500" style="text-align:left; padding-left:10px;">
					<label id="labrolename"> </label>
				</td>
			</tr>					
			<tr height="45">
				<td width="100" style="text-align:right; padding-right:0px;">
					<label>权限信息：</label>
				</td>
				<td width="500" style="text-align:left; padding-left:10px;">
					<label id="labprivlist"> </label>
				</td>
			</tr>
			<tr height="45">
				<td width="100" style="text-align:right; padding-right:0px;">
					<label>角色说明：</label>
				</td>
				<td width="500" style="text-align:left; padding-left:10px;">
					<label id="labrolename"> </label>
				</td>
			</tr>
			<tr height="45">
				<td width="100" style="text-align:right; padding-right:0px;">
					<label>备 注：</label>
				</td>
				<td width="500" style="text-align:left; padding-left:10px;">
					<label id="labifvalid"> </label>
				</td>
			</tr>	
		</table>
	  </div>
	</div>
	<script>
	  $(function () {
	  	var roleid="<%=request.getParameter("subroleid")%>";
	  	var obj=eval("("+"{\"btnViewRole\":\"btnViewRole\",\"roleid\":"+roleid+"}"+")");
	  	$.ajax({
			url:"servlet/RoleManage",
			type:"POST",
			data:obj,
			async:false,
			error:function(request){
				alert("Network Error 网络异常");
			},
			success:function(data){
				//alert(data);
				var obj=eval("("+data+")");
				$("#labroleid").text(obj.roleid);
				$("#labrolename").text(obj.rolename);
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
	
  </body>
</html>
