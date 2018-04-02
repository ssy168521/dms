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
  <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>修改字段</strong></div>
  <div class="body-content">
    <form id="modifyuserform" method="post" class="form-x">  
    
     <div class="form-group">
        <div class="label">
          <label>字段id：</label>
        </div>
        <div class="field">
          <input type="text" id="fieldid" name="fieldid" readonly="readonly" class="input w50"/>
        </div>
      </div>  
	  <div class="form-group">
        <div class="label">
          <label>旧字段名：</label>
        </div>
        <div class="field">
          <input type="text" id="fieldname" name="fieldname" readonly="readonly" class="input w50"/>
        </div>
      </div>
      <div class="form-group">
        <div class="label">
          <label>新字段名：</label>
        </div>
        <div class="field">
          <input type="text" id="nfieldname" name="nfieldname" class="input w50" placeholder="请输入新字段名称" data-validate="required:请输入新字段名"/>
        </div>
      </div>
      <div class="form-group">
          <div class="label">
            <label>字段类型：</label>
          </div>
          <div class="field">
            <select name="dataType" id="dataType" class="input w50">
              <option value="">请选择字段数据类型</option>
              <option value="Integer">整型</option>
              <option value="String">字符型</option>              
              <option value="Double">浮点数</option>
              <option value="Timestamp">日期时间</option>
              <option value="Polygon">几何面</option>
              <option value="Point">几何点</option>
              <option value="LineString">几何线</option>
            </select>
          </div>
      </div>
     <!--
      <div class="form-group">
          <div class="label">
            <label>约束：</label>
          </div>
          <div class="field">
            <select name="constraint" id="constraint" class="input w50">
              <option value="">请选择约束类型：</option>
              <option value=" ">default</option>
              <option value="primarykey">primary key</option>              
              <option value="uniquekey">unique</option>              
              <option value="notnull">not null</option>
              
            </select>
           <div class="tips"></div>
          </div>
      </div>
  		-->
       <div class="field" style="left:120px;">
          <input class="button bg-main icon-check-square-o" style="width:100px;text-align:center" id="btnModifyUser" name="btnModifyUser" value="更新"/>
       </div>
    </form>
<script>
	$(function () {
		var fieldName="<%=request.getParameter("subfieldName")%>";
		var fieldid = "<%=request.getParameter("subfieldid")%>";
		//alert(columnName);
		$("#fieldname").val(fieldName);
		$("#fieldid").val(fieldid);
		
	});
	
	$("#btnModifyUser").click(function(){			
		var dataType=$("#dataType").val();
		if(dataType == "") {
			alert("请选择字段类型！！");
			return false;
		}
		
		$.ajax({
			url:"./servlet/fieldModify",
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
					document.location="<%=basePath%>"+"jsp/field.jsp";
				}else{
					alert(data);
				}
			}
		});
	});
	
	
	</script>
  </body>
</html>
