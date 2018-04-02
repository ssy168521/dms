<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'json.jsp' starting page</title>
    <script type="text/javascript" src="lib/jquery-1.4.2.min.js"></script>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<script type="text/javascript">
$(function(){
	$("#btn").click(function(){
        $.getJSON("data/temp.geojson",function(data){
        	$("#type").append(data.type+"<hr/>");
        	var $jsontip = $("#jsonTip");
        	var strHtml = "123";
        	$jsontip.empty();
        	$.each(data.features,function(infoIndex2,info2){
        		$("ifff").append(
        		"<div>"+info2.id+"</div>"+
        		"<div>"+info2.type+"</div><hr/>");
        		strHtml+= "id"+info2.id+"<br>";
        		strHtml+= "数据ID:"+info2.properties["F_SCENEID"]+"<br>";
        		strHtml+= "PATH:"+info2.properties.F_SCENEPATH+"<br>";
       			strHtml+= "ROW:"+info2.properties.F_SCENEROW+"<br>";
       			strHtml+= "轨道号:"+info2.properties.F_ORBITID+"<br>";
       			strHtml+= "人工云量:"+info2.properties.F_OVERALLDATAQUALITY+"<br>";
        		
        		strHtml += "<hr> ";
        		
        		
        		
        		/* $.each(info2.properties,function(infoIndex3,propertyinfo){
        			/* propertyinfo.FID */
        			/* strHtml+= "FID:"+propertyinfo.FID+"<br>"; */
        	/* 		strHtml+= "数据ID:"+propertyinfo.F_SCENEID+"<br>";
        			strHtml+= "PATH:"+propertyinfo.F_SCENEPATH+"<br>";
        			strHtml+= "ROW:"+propertyinfo.F_SCENEROW+"<br>";
        			strHtml+= "轨道号:"+propertyinfo.F_ORBITID+"<br>";
        			strHtml+= "人工云量:"+propertyinfo.F_OVERALLDATAQUALITY+"<br>";
        		strHtml += "<hr> "; */
        		
        		/* }); */
        	
        	});
        	$jsontip.html(strHtml);
			
			
			/* var $jsontip = $("#jsonTip");
			var strHtml = "123";//´洢˽¾ݵı偿
			$jsontip.empty();//ȥ¿քۈۍ
			$.each(data,function(infoIndex,info){
				  strHtml += "姓名"+info["type"]+"<br>";
				  strHtml += "性别"+info["features"]+"<br>";
				  strHtml += "邮箱"+info["email"]+"<br>";
				  strHtml += "<hr> ";
				});
			$jsontip.html(strHtml);//Дʾ´¦mº󶅊� */
			});
		});
	
	});
</script>

  </head>
  
  <body>
    This is my JSP page. <br>
    <input type="button" id="btn" name="btn">
      <div id="jsonTip">
  </div>
  </body>
</html>
