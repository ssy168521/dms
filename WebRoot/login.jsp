<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>测绘卫星影像产品管理系统-登录</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
   
    <link rel="stylesheet" href="css/pintuer.css">
    <link rel="stylesheet" href="css/admin.css">
    <script src="lib/jquery.js"></script>
    <script src="js/pintuer.js"></script>  
  </head>
  
  <body>
    <div class="bg"></div>
<div class="container">
    <div class="line bouncein">
        <div class="xs6 xm4 xs3-move xm4-move">
            <div style="height:180px;"></div>
            <div class="media media-y margin-big-bottom">           
            </div>         
            <form id="loginform" method="post" action="./servlet/Login">
            <input type="hidden" name="opertype" value="login" />
            <div class="panel loginbox">
                <div class="text-center margin-big padding-big-top">
                <!-- <h1>测绘卫星影像产品管理系统</h1> -->
                	<h1>用户登录</h1>
                </div>
                <div class="panel-body" style="padding:30px; padding-bottom:10px; padding-top:10px;">
                    <div class="form-group">
                        <div class="field field-icon-right">
                            <input id="loginname" type="text" class="input input-big" name="name" placeholder="登录账号" data-validate="required:请填写账号" />
                            <span class="icon icon-user"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="field field-icon-right">
                            <input  id="loginpwd" type="password" class="input input-big" name="password" placeholder="登录密码" data-validate="required:请填写密码" />
                            <span class="icon icon-key"></span>
                        </div>
                    </div>
                </div>
                <div style="padding:30px;">
	                <table>
						<tr style="padding:30px;">
						<td width="200" style="text-align:center; padding-right:0px;">
							<button id="btn_register"  style="width:100px" class="button bg-main text-big input-big" >注册</button>
						</td>
						<td width="200" style="text-align:center; padding-right:0px;">
							<button id="btn_login" type="submit" style="width:100px" class="button bg-main text-big input-big" value="登录"> 登录</button>
						</td>
						</tr>
					</table>
                </div>
                <!-- <div style="padding:30px;">
                
                <button id="btn_login" type="submit" class="button bg-main text-big input-big" value="登录"> 登录</button>
                <button id="btn_register"  class="button bg-main text-big input-big" >注册</button>
                </div> -->
            </div>
            </form>          
        </div>
    </div>
</div>
<script type="text/javascript">

     var message= <%=session.getAttribute("logininfo")%>;
     if(message != null)
     {
         alert(message);
         <%session.removeAttribute("logininfo");%>;
     }
  
/* $("#btn_login").click(function(){
     $("#btn_login").submit();
}); */

$("#btn_register").click(function(){
     window.location.href = "./register.jsp";
});
</script>
  </body>
</html>
