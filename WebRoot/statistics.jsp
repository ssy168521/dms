<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>数据统计</title>
    
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
<!--     <div style="height:14%;background-color: #2153A3;">
         <img src="img/page/logo.png" alt="数管分包">
         <div style="padding-left:10px;background:none;font-size:14px;">
           <ul style="">
			 <li class="btn" style="display:inline;float:left;text-align:center;"><a style="color:white;text-decoration:none;" href="query.jsp">首页</a>
			 <li class="btn" style="display:inline;float:left;text-align:center;"><a style="color:white;text-decoration:none;" href="query.jsp">可视化查询</a>
			 <li class="btn" style="display:inline;float:left;text-align:center;">数据统计 
			 <li class="btn" style="display:inline;float:left;text-align:center;"><a style="color:white;text-decoration:none;" href="jsp/SysMan.jsp">系统管理</a>
			 <li class="btn" style="display:inline;float:left;text-align:center;"><a style="color:white;text-decoration:none;" href="jsp/denglu.jsp">登录</a>
		  </ul> 
		      <input class="btn" type="button"  value="首页" />
		      <input class="btn" type="button"  value="可视化查询" />
		      <input class="btn" type="button"  value="数据统计" />
		      <input class="btn" type="button" value="系统监控" />
		     
		      <input id="test" class="btn" type="button"  value="登录" />		      
		</div>		  
    </div> -->
    	<div data-dojo-type="dijit/layout/BorderContainer"
		data-dojo-props="design:'headline',gutters:false" id="main">
    <div data-dojo-type="dijit/layout/ContentPane"
			data-dojo-props="region:'top'"
			style="background-image: url(img/page/bannerbg.jpg);background-size:auto auto;background-repeat:repeat">
			
			<div data-dojo-type="dijit/layout/ContentPane"
			     data-dojo-props="region:'left',splitter:'true'"
			     style="float:left;"><a href="query.jsp"><img src="img/page/logo.png" style="padding-left:15px;background:none;"></a>
			     <div style="background:none;">
			     <a href="query.jsp"><button class="btn" style="border:none;width:100px;height:36px;color:white;">首页</button></a>
			     <a href="query.jsp"><button class="btn" style="border:none;width:100px;height:36px;color:white;">可视化查询</button></a>
			     <a href="jsp/statistics.jsp"><button class="btn" style="border:none;width:100px;height:36px;color:white;">数据统计</button></a>
			     <a href="jsp/SysMan.jsp"><button class="btn" style="border:none;width:100px;height:36px;color:white;">系统管理</button></a>
			     <a href=""><button class="btn" style="border:none;width:100px;height:36px;color:white;">归档</button></a>
			     <a href="jsp/denglu.jsp"><button class="btn" style="border:none;width:100px;height:36px;color:white;">登录</button></a>  
		         <!-- <button style="border:none;width:100px;onmouseover="this.style.background:url(img/navbg.jpg);" onmouseout="this.style.background=url(img/navbgover.jpg);"color:white;">首页</button>  -->   
			     </div>
			</div>			 
			<div data-dojo-type="dijit/layout/ContentPane"
			     data-dojo-props="region:'right',splitter:'true'"
			     style="float:right;"><img src="img/page/banner.jpg">
			</div>					  
		</div>
	</div>
    <div id="tableContainer">
       <div id="tableRow" style="display:inline;">
        <div id="sidebar">
         <table>
            <tr>
              <th style="background:url(img/icon/tit05.jpg) repeat-x;font-color:white;">数据量统计</th>
            </tr>
            <tr>
              <td>
               <button style="border:none;width:220px;">数据分类统计</button>
              </td>
            </tr>
             <tr>
              <td> <button style="border:none;width:220px;">资源三号数据统计</button></td>
            </tr>
         </table>
         <table>
            <tr>
              <th style="background:url(img/icon/tit05.jpg) repeat-x;font-color:white;">覆盖图统计</th>
            </tr>
            <tr>
             <td> <button style="border:none;width:220px;">覆盖面积统计</button></td>
            </tr>
            <!--  <tr>
              <td>覆盖图统计</td>
            </tr>
             <tr>
              <td>覆盖图统计</td>
            </tr> -->
         </table>
        </div>
       
      </div>
       <div id="main">
         <div style="width:100%;border-bottom:1px solid #99bbe8;float:right;">
             <div style="display:inline;float:right;"><button style="border:none;backgroung-color:transparent;"><img alt="导出结果" src="img/export.png" >导出结果</button></div>
              <div style="float:right;"><button style="border:none;backgroung-color:transparent;"><img alt="查询条件" src="img/search.png" >查询条件</button></div>
             
         </div>
        主界面
        </div>
   </div>
  </body>
</html>
