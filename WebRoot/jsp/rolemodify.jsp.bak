<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'rolemodify.jsp' starting page</title>
    
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
	  <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>修改角色</strong></div>
	  <div class="body-content">
	    <form id="addmodifyform" method="post" class="form-x">  
	        <div class="form-group">
	          <div class="label">
	            <label>角色名称：</label>
	          </div>
	          <div class="field">
	            <input type="text" id="rolename" name="rolename"class="input w50" readonly="readonly"/>
	          </div>
	        </div>
	        
	        <div class="form-group">
	          <div class="label">
	            <label>选择权限：</label>
	          </div>
	          <table align="left" width="" border="0" cellpadding="0" cellspacing="0">  
			    <tr>  
			        <td colspan="4">  
			            <select name="unSelePrivs" id="unSelePrivs" multiple="multiple" size="10" style="width:180px;"> 
			            </select>  
			        </td>  
			        <td align="center" style="width:100px;vertical-align:top;">  
			           <br/><input type="button" id="addAll" value=" >> " style="width:50px;" /><br/><br/> 
			            <input type="button" id="addOne" value=" > " style="width:50px;" /><br /><br/>  
			            <input type="button" id="removeOne" value="&lt;" style="width:50px;" /><br /><br/>  
			            <input type="button" id="removeAll" value="&lt;&lt;" style="width:50px;" /><br />  
			        </td>  
			        <td colspan="4">  
			            <select name="SelePrivs" id="SelePrivs" multiple="multiple" size="10" style="width:180px;">  
			            </select>  
			        </td>  
			       
			    </tr>  
			</table>  
		  </div>
	        
	      <div class="form-group">
	        <div class="label">
	          <label>角色说明：</label>
	        </div>
	        <div class="field">
	          <textarea class="input" id="roleinfo" name="roleinfo" style="width:60%;height:150px;"></textarea>
	        </div>
	      </div>
	      <div class="form-group">
	        <div class="label">
	          <label>备注：</label>
	        </div>
	        <div class="field">
	          <textarea class="input" id="remark" name="remark" style="width:60%;height:150px;"></textarea>
	        </div>
	      </div>
	    
	      <div class="form-group">
	        <div class="label">
	          <label></label>
	        </div>
	        <div class="field">
	          <input class="button bg-main icon-check-square-o" style="width:100px;text-align:center" id="btnModifyRole" name="btnModifyRole" value="提交"/>
	        </div>
	      </div>
	    </form>
	  </div>
	</div>
<script type="text/javascript">
	$(function () {
		var roleid="<%=request.getParameter("subroleid")%>";
		var rolename="<%=request.getParameter("subrolename")%>";
		$("#rolename").val(rolename);		
		$.ajax({
			url:"./servlet/PageLoad",
			type:"POST",
			data:eval("("+"{requestSou:\"addrolePage\"}"+")"),
			async:false,
			error:function(request){
				alert("Network Error 网络异常");
			},
			success:function(data){
				var objdata= eval("("+data+")");
				var objSelect=document.getElementById("unSelePrivs");
				for(var i=0;i<objdata.length;i++){
					var option=new Option(objdata[i].privname,objdata[i].privid);
					objSelect.options.add(option);
				}
			}
		});	
	});
	
	$("#btnModifyRole").click(function(){
		var SelePrivs=null;
		$("#SelePrivs option").each(function(){
			$(this).attr("selected",true);
			SelePrivs=$(this).attr("value");
		});		
		
		if(SelePrivs==null){
			alert("请选择权限！！");
			return false;
		}
		
		$.ajax({
			url:"./servlet/RoleManage",
			type:"POST",
			data:$("#addmodifyform").serialize(),
			async:false,
			error:function(request){
				alert("Network Error 网络异常");
			},
			success:function(data){
				$("#SelePrivs option").each(function(){
					$(this).attr("selected",false);
				});
				
				//alert(data);
				if(data=="true")
				{
					alert("添加角色成功！");
					document.location='./jsp/role.jsp';
				}else{
					alert(data);
				}
			}
		});	
		
		
	});
	
	
	//选择器
	$(function(){  
	    //选择一项  
	    $("#addOne").click(function(){  
	        $("#unSelePrivs option:selected").clone().appendTo("#SelePrivs");  
	        $("#unSelePrivs option:selected").remove();
	    });	  
	    $("#addAll").click(function(){  
	        $("#unSelePrivs option").clone().appendTo("#SelePrivs");  
	        $("#unSelePrivs option").remove();  
	    });	        
	    $("#removeOne").click(function(){  
	        $("#SelePrivs option:selected").clone().appendTo("#unSelePrivs");  
	        $("#SelePrivs option:selected").remove();  
	    }); 
	    $("#removeAll").click(function(){  
	        $("#SelePrivs option").clone().appendTo("#unSelePrivs");  
	        $("#SelePrivs option").remove();  
	    });	    
	    //双击选择
	    $("#unSelePrivs").dblclick(function(){ //绑定双击事件
	    //获取全部的选项,删除并追加给对方
	        $("option:selected").clone().appendTo("#SelePrivs"); //追加给对方
	        $("#unSelePrivs option:selected").remove(); 
	    });	 
	   //双击移除
	    $("#SelePrivs").dblclick(function(){
	        $("option:selected").clone().appendTo("#unSelePrivs");
	        $("#SelePrivs option:selected").remove(); 
	    });
	});  

</script>


  </body>
</html>
