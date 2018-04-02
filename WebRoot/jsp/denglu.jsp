<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>登录界面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
   <link href="css/denglu.css" type="text/css" rel="stylesheet" rev="stylesheet"/>
  </head>
  
  <body class="denglu02" >
	<div class="dl" >
	  <div class="biaoti" ><img src="img/page/logo.png" /></div>
		<!-- <div class="biaoti"><img src="css/images/ico02.png" /></div> -->
		<div class="log">
			 <ul class="xuzhi02">
				 <li class="xz">登录说明</li>
				 <li>1.佛啊的商品房控股佛法是咳咳咳</li>
				 <li>2.佛啊的商品房控股佛法是咳房控股佛法是咳咳咳</li>
				 <li>3.佛啊的商品房控房控股佛法是咳股佛法是咳咳咳</li>
				 <li>4.佛啊的商品房控股房控股佛法是咳佛法是咳咳咳</li>
			</ul>
			  <ul class="deng02">
				<li style=" width:100%; height:60px;">
					<p style="float:left;font-size:18px; color:#666;line-height:30px; ">用户名:</p> 
					<input id="username" class="i-text" type="text" errormsg="用户名至少6个字符,最多18个字符！" datatype="s6-18" ajaxurl="demo/valid.jsp" maxlength="100" value="输入账号">
				</li>
		
				<li style=" width:100%; height:60px;"> 
					<p style="float:left;font-size:18px; color:#666;line-height:30px; ">密&nbsp;&nbsp;&nbsp;码:</p> 
					<input id="username" class="i-text" type="text" errormsg="用户名至少6个字符,最多18个字符！" datatype="s6-18" ajaxurl="demo/valid.jsp" maxlength="100" value="输入密码">
				</li>
				<!-- <div style="clear:both;"></div>
				<li style=" width:100%; height:60px;">
					<p style="float:left;font-size:18px; color:#666;line-height:30px; ">验证码:</p> 
					<input id="username" class="i-text" type="text" errormsg="用户名至少6个字符,最多18个字符！" datatype="s6-18" ajaxurl="demo/valid.jsp" maxlength="100" value="输入验证码">
				</li> -->
				<li style=" width:100%; height:60px;">
				 
					<button id="logonbtn" class="btn-login02" onclick="LoginPage.gotoLogin();" type="button" > 
					<span>登&nbsp;&nbsp;&nbsp;&nbsp;录</span>
				  </button>
				  <a href="jsp/zhuce.html">
				  <button id="logonbtn" class="btn-login02" onclick="LoginPage.gotoLogin();" type="button" > 
					<span>注&nbsp;&nbsp;&nbsp;&nbsp;册</span>
				  </button>
				  </a>
				</li>
			</ul>
			
		</div>
	</div>
</body>

</html>
