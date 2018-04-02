<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>系统管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
    <link rel="stylesheet" type="text/css" href="css/statistics.css">
    <style>
    .btn{background:url(img/page/navbg.jpg);}
    .btn:hover{background:url(img/page/navbgover.jpg);}
    </style>
  </head>
  
  <body>
    <!-- <div id="header"> -->
    <div id="header">
         <img src="img/page/logo.png" alt="数管分包">
         <!-- <div style="padding-left:10px;background:none;font-size:14px;"> -->
           <ul>
			 <li class="btn" ><a href="home.jsp">首页</a>
			 <li class="btn" ><a href="home.jsp">可视化查询</a>
			 <li class="btn" ><a href="jsp/statistics.jsp">数据统计</a>
			 <li class="btn" style="font-size:15px;">系统管理 
			 <li class="btn" ><a href="jsp/denglu.jsp">登录</a>
		  </ul> 
		     <!--  <input class="btn" type="button"  value="首页" />
		      <input class="btn" type="button"  value="可视化查询" />
		      <input class="btn" type="button"  value="数据统计" />
		      <input class="btn" type="button" value="系统监控" />
		      <input id="test" class="btn" type="button"  value="登录" />	 -->	      
		<!-- </div> -->		  
    </div>
    <div id="tableContainer">
       <div id="tableRow" style="display:inline;">
        <div id="sidebar">
         <table>
            <tr>
             <th ><button style="font-color:white;font-size:20px;border:none;width:220px;background:url(img/icon/tit05.jpg) repeat-x;">用户管理</button></th>
            </tr>
            <!-- <tr>
              <td>
               <button style="border:none;width:220px;">数据分类统计</button>
              </td>
            </tr>
             <tr>
              <td> <button style="border:none;width:220px;">资源三号数据统计</button></td>
            </tr> -->
         </table>
         <table>
            <tr>
             <th ><button style="font-color:white;font-size:20px;border:none;width:220px;background:url(img/icon/tit05.jpg) repeat-x;">配置管理</button></th>
            </tr>
            <!-- <tr>
             <td> <button style="border:none;width:220px;">覆盖面积统计</button></td>
            </tr>
             <tr>
              <td>覆盖图统计</td>
            </tr>
             <tr>
              <td>覆盖图统计</td>
            </tr> -->
         </table>
         <table>
            <tr>
             <th ><button style="font-color:white;font-size:20px;border:none;width:220px;background:url(img/icon/tit05.jpg) repeat-x;">权限管理</button></th>
            </tr>
            
         </table>
        </div>
       
      </div>
       <div id="main">
         <!-- <div style="width:100%;border-bottom:1px solid #99bbe8;float:right;">
             <div style="display:inline;float:right;"><button style="border:none;backgroung-color:transparent;"><img alt="导出结果" src="img/export.png" >导出结果</button></div>
              <div style="float:right;"><button style="border:none;backgroung-color:transparent;"><img alt="查询条件" src="img/search.png" >查询条件</button></div>
             
         </div> -->
        主界面
        </div>
   </div>
  </body>
</html>
