<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'datepicker.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="./lib/My97DatePicker/WdatePicker.js"></script>

  </head>
  
  <body>
    This is my JSP page. <br>
    <center>
    <h2>My97datePicker使用</h2>
    </center>
    
    <input id="" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'whyGreen',readonly:true,maxDate:'%y-%M-%d',minDate:'2012-01-01 00:00:00'})" />
    </br>
            <input id="StartTime" class="Wdate" type="text" width="16" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',readonly:true,minDate:'2012-01-01 00:00:00',maxDate:'#F{$dp.$D(\'EndTime\')||\'new Date()\'}'})"/>
       至<input id="EndTime" class="Wdate" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',readonly:true,minDate:'#F{$dp.$D(\'StartTime\')}',maxDate:'%y-%M-%d'})"/> 
  </br>     
   <input type="number" style="width:40px" id="234" value="20"/> 
   
   
   </br>
    <input id="date11" class="Wdate" onfocus="WdatePicker()"/>
   <input type="button" id="srs" onclick="alert(date11.value)" value="按钮"/>
  </body>
</html>
