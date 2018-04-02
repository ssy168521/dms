<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>    
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
  <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>修改用户</strong></div>
  <div class="body-content">
    <form id="modifyuserform" method="post" class="form-x">  
    
      <div class="form-group">
        <div class="label">
          <label>用户名：</label>
        </div>
        <div class="field">
          <input type="text" id="username" name="username" class="input w50" readonly="readonly"/>
        </div>
      </div>
        <div class="form-group">
          <div class="label">
            <label>角色名称：</label>
          </div>
          <div class="field">
            <select name="roleid" id="roleid" class="input w50">
              <!-- <option value="">请选择角色</option> -->
            </select>
          </div>
        </div>
     
      	<div class="form-group">
          <div class="label">
            <label>有效性：</label>
          </div>
           <div class="field">
            <select name="ifvalid" id="ifvalid" class="input w50">
              <!-- <option value="">请选择有效性</option> -->
              <option value="1">有效</option>
              <option value="0">无效</option>
              
            </select>
            <div class="tips"></div>
          </div>
        </div>
  		<div class="form-group">
          <div class="label">
            <label>是否修改密码：</label>
          </div>
          <div class="field" style="padding-top:8px;"> 
             <input id="open" name="ifmodifypwd" type="radio" value="true" /> 是
             <input id="close" name="ifmodifypwd" type="radio" value="false" checked="checked"/> 否  
          </div>
        </div>
        
      <div id="openDiv" class="panel admin-panel" style=" position:relitive;display:none;background:white;">
		  <div class="panel-head"><strong><span class="icon-key"></span> 修改用户密码</strong></div>
		  <div class="body-content">
		    <div class="form-x"> 
		      <div class="form-group">
		        <div class="label">
		          <label for="sitename">新密码：</label>
		        </div>
		        <div class="field">
		          <input type="password" class="input w50" id="password" name="password" size="50" placeholder="请输入新密码" data-validate="required:请输入新密码,length#>=6:新密码不能小于6位" />         
		        </div>
		      </div>
		      <div class="form-group">
		        <div class="label">
		          <label for="sitename">确认新密码：</label>
		        </div>
		        <div class="field">
		          <input type="password" class="input w50" id="rpassword" name="rpassword" size="50" placeholder="请再次输入新密码" data-validate="required:请再次输入新密码,repeat#password:两次输入的密码不一致" />          
		        </div>
		      </div>
		    </div>
		  </div>
		</div>
       <div class="field" style="left:120px;">
          <input class="button bg-main icon-check-square-o" style="width:100px;text-align:center" id="btnModifyUser" name="btnModifyUser" value="提交"/>
        </div>
    </form>
<script>
	$(function () {
		var userid="<%=request.getParameter("subuserid")%>";
		var username="<%=request.getParameter("subusername")%>";
		var roleid="<%=request.getParameter("subroleid")%>";
		var ifvalid="<%=request.getParameter("subifvalid")%>";
		//var obj=eval("("+subrow+")");
		$.ajax({
			url:"./servlet/PageLoad",
			type:"POST",
			data:eval("("+"{requestSou:\"adduserPage\"}"+")"),
			async:false,
			error:function(request){
				alert("Network Error 网络异常");
			},
			success:function(data){
				var objdata= eval("("+data+")");
				var objSelect=document.getElementById("roleid");
				for(var i=0;i<objdata.length;i++){
					var option=new Option(objdata[i].rolename,objdata[i].roleid);
					objSelect.options.add(option);
				}
			}
		});
		$("#username").val(username);
		$("#roleid").val(roleid);
		$("#ifvalid").val(ifvalid);
	});
	
	$("#btnModifyUser").click(function(){		
		var chkresu=$("input:radio[name='ifmodifypwd']:checked").val();
		var psw=$("#password").val();
		var rpsw=$("#password").val();
		if(chkresu=="true"&&(psw==""||rpsw==""))
		{
			alert("请输入新密码！！！");
			return false;
		}
	
		$.ajax({
			url:"./servlet/UserManage",
			type:"POST",
			data:$("#modifyuserform").serialize(),
			async:false,
			error:function(request){
				alert("Network Error 网络异常");
			},
			success:function(data){
				if(data=="true")
				{
					alert("修改成功！");
					document.location="<%=basePath%>"+"jsp/user.jsp";
				}else{
					alert(data);
				}
			}
		});
	});
	
	
	
	
	$("#open").click(function(){
		document.getElementById("openDiv").style.display="block";
	});
	$("#close").click(function(){
		$("#password").val("");
		$("#rpassword").val("");
		document.getElementById("openDiv").style.display="none";		
	});
	</script>
  </body>
</html>
