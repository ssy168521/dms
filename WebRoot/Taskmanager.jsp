<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String username="";
 if(session.getAttribute("username")!=null)
 {
      username=session.getAttribute("username").toString();
 };
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>任务管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="./lib/jquery.js"></script>
    <script src="js/sysmanager.js"></script>
    <link rel="stylesheet" href="css/pintuer.css">
    <link rel="stylesheet" href="css/admin.css">
    <link rel="stylesheet" href="css/font-awesome.css">
    <script type="text/javascript" src="js/logininfo.js"></script>
    <style>
		html,body,#main {
			width: 100%;
			height: 100%;
			margin: 0;
		}
		.headerbtn{background:url(img/page/navbg.jpg);}
		.headerbtn:hover{background:url(img/page/navbgover.jpg);}
	</style>	

  </head>
  
  <body style="background-color:#f2f9fd;" onload="load()">
<div class="header bg-main" style="background-image: url(img/page/bannerbg.jpg);background-size:110px auto;background-repeat:repeat">
  <div class="logo margin-big-left fadein-top">   
   <img src="img/page/logo.png"  height="70"/>
   <div style="float:right;margin-top:30px">
   	<label style="font-size:24px;color:white">任务管理</label>
   </div>
  </div>
  <!-- <div class="head-l">
  <a class="button button-little bg-green" href="./query.jsp.jsp"><span class="icon-home"></span> 首    页</a> &nbsp;&nbsp;
  <a class="button button-little bg-blue" href="" target="_blank"><span class="icon-wrench"></span> 清除缓存</a> &nbsp;&nbsp;
  <a id="btnLogoutid" class="button button-little bg-red" href="./login.jsp"><span class="icon-power-off"></span> 退出登录</a> </div>
 -->	<div style="background:none;position:relative;top:72px;left:-575px;"> 
	   <a href="default.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;">首页</button></a>
	   <a href="query.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-6px;">可视化查询</button></a>
	   <a href="Archive.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-6px;">数据归档</button></a>
	   <a href="Taskmanager.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-6px;">任务管理</button></a>
	   <a href="sysmanager.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-6px;">系统管理</button></a>
	 </div>
	<div style="z-index=9999;float:right; display:inline;position:absolute;top:80px;right:10px;"><!--display:inline-block;  -->
		<table>
			<tr>
			<td><a id="login-id" style="font-size:16px;display:none" href="login.jsp" ><font color="#FFFFFF">登录  </font></a></td>
			<td width=10px></td>
			<td><a id="register-id" style="font-size:16px;display:none"  href="register.jsp" ><font color="#FFFFFF">注册</font></a></td>
			<td><a id="user-id" style="font-size:16px;display:none" > <font color="#FFFFFF"><%=username%>,欢迎您  </font></a></td>
			<td width=10px></td>
			<td><a id="logout-id" style="font-size:16px;display:none;cursor:pointer;" onclick="logout()" ><font color="#FFFFFF">退出</font></a></td>
			</tr>
		</table>
	</div>
</div>
<div class="leftnav">
  <div class="leftnav-title"><strong><span class="icon-list"></span>菜单列表</strong></div> 
    <h2><span class="icon-download"></span>下载任务</h2>
  <ul style="display:block">
    <li><a href="./jsp/TaskInfo.jsp?tasktype=download" target="right"><span class="icon-caret-right"></span>任务信息</a></li>
  </ul> 
  <h2><span class="icon-upload"></span>归档任务</h2>
  <ul style="display:block">
    <li><a href="./jsp/TaskInfo.jsp?tasktype=archive" target="right"><span class="icon-caret-right"></span>任务信息</a></li>
  </ul>
</div>
<script type="text/javascript">
var obj="<%=username%>";
showusername(obj);

$(function(){

  $("#btnLogoutid").click(function(){
      $.ajax({
 
				url:"./servlet/Login",
				type:"POST",
				data:eval("("+"{opertype:\"logout\"}"+")"),
				async:false,
				error:function(request){
					alert("Network Error 网络异常");
				},
				success:function(data){
				alert(data);

				}
			});

	});
	$(".leftnav h2").click(function(){
		$(this).next().slideToggle(200);
		$(this).toggleClass("on"); 
	});
	$(".leftnav ul li a").click(function(){
		$("#a_leader_txt").text($(this).text());
		$(".leftnav ul li a").removeClass("on");
		$(this).addClass("on");
	});
});
</script>
<ul class="bread">
	<li><a href="default.jsp" target="right" class="icon-home"> 首页</a></li>
	<li><a href="##" id="a_leader_txt"> </a></li>
</ul>
<div class="admin">
	<iframe scrolling="auto" rameborder="0" src="./jsp/TaskInfo.jsp" name="right" width="100%" height="99%"></iframe>
</div>

</body>
</html>
