<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>卫星影像管理系统-注册</title>
    
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
            <form id="registerform">
            <input type="hidden" name="opertype" value="register" />
            <div class="panel loginbox">
                <div class="text-center margin-big padding-big-top">
                	<h1>用户注册</h1>
                </div>
                <div class="panel-body" style="padding:30px; padding-bottom:10px; padding-top:10px;">
                    <div class="form-group">
                        <div class="field field-icon-right">
                            <input id="regname" type="text" class="input input-big" name="regname" placeholder="请输入用户名" data-validate="required:请填写用户名" />
                            <span class="icon icon-user"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="field field-icon-right">
                            <input  id="regpwd" type="password" class="input input-big" name="regpwd" placeholder="请输入密码" data-validate="required:请填写密码,length#>=6:密码不能小于6位" />
                            <span class="icon icon-key"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="field field-icon-right">
                            <input  id="regpwd2" type="password" class="input input-big" name="regpwd2" placeholder="请确认密码" data-validate="required:请再次填写密码,repeat#regpwd:两次输入的密码不一致" />
                            <span class="icon icon-key"></span>
                        </div>
                    </div>
                    <!-- <div class="form-group">
                        <div class="field">
                            <input type="text" class="input input-big" name="code" placeholder="填写右侧的验证码" data-validate="required:请填写右侧的验证码" />
                           <img src="jsp/CheckCode.jsp" alt="" width="100" height="32" class="passcode" style="height:43px;cursor:pointer;" onclick="this.src=this.src+'?'">                         
                        </div>
                    </div> -->
                </div>
                <div style="padding:30px;">
	                <table>
						<tr style="padding:30px;">
						<td width="200" style="text-align:center; padding-right:0px;">
							<!-- <button id="btn_register"  style="width:100px" class="button bg-main text-big input-big" >注册</button> -->
						</td>
						<td width="200" style="text-align:center; padding-right:0px;">
							<button id="btn_register" type="button" style="width:100px" class="button bg-main text-big input-big"> 注册</button>
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
 $("#btn_register").click(function(){
    $.ajax({
			url:"./servlet/Login",
			type:"POST",
			data:$("#registerform").serialize(),
			async:false,
			error:function(request){
				alert("Network Error 网络异常");
			},
			success:function(data){
				//alert(data);
				if(data=="true")
				{
					alert("注册成功！");
					document.location='./login.jsp';
				}else{
					alert("用户名已存在！");
				}
			}
		});	
});

/* $("#btn_register").click(function(){
     window.location.href = "./jsp/register.html";
}); */

</script>
  </body>
</html>
