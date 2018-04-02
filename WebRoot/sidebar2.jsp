<!-- 火狐浏览器格式显示有问题；谷歌浏览器显示正常，估计是不兼容的问题。 -->
<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@page import ="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>测试页面</title>
  

<style type="text/css"> 

*{margin:0;padding:0;list-style-type:none;}
a,img{border:0;}
body{background:none;}   //控制着网页的背景色
body{font:12px/180% Arial, Helvetica, sans-serif, "新宋体";}

.A{position: fixed;top: 190px;right:0;width:325px;float:left;z-index: 0;border:1px solid #99bbe8;background-color:white;}
.B{position: fixed;top: 171px;right:0;width:325px;float:left;z-index: 0;border:1px solid #99bbe8;background-color:white;}  
.C{position: fixed;bottom:0px;right:0;width:325px;z-index: 0;border:1px solid #99bbe8;background-color:white;} 
.D{display:inline;float:left;margin:0px;} 
           
.rides-cs{background:white;position:fixed;top:210px;right:0px;z-index:999;bottom:20px;overflow-y:auto;}
.rides-cs .floatL{width:0px;float:left;z-index:10;position:fixed;}
.rides-cs .floatL a{font-size:0;text-indent:-999em;display:block;}
.rides-cs .floatR{width:300px;float:left;padding:12px;overflow:hidden;display:none;border:1px solid #99bbe8;}
.rides-cs .floatR .cn{background:#F7F7F7;}
.rides-cs .btnOpen,.rides-cs .btnCtn{position:relative;z-index:9;top:150px;left:-10px;background-image: url(img/icon/btngroup.png);display:block;width:0px;height:64px;overflow:auto;padding:8px;}
.rides-cs .btnOpen{background-position:12px 0px;}
.rides-cs .btnCtn{background-position:-22px 0px;} 
</style>

<!-- <link rel="stylesheet" href="lib/css/sidebar.css" type="text/css"/>  -->
<script type="text/javascript" src="lib/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="lib/sidebar.js"></script>
</head>
<body style="height:auto;">
<div class="rides-cs" > 
	<div class="floatL" style="" >	
		<!--<a style="display:block" id="aFloatTools_Show" class="btnOpen"  href="javascript:void(0);"></a> -->
		<a style="display:none;background-image: url(img/icon/btngroup.png);" id="aFloatTools_Hide" class="btnCtn"  href="javascript:void(0);"></a> 
		<a style="display:block;background-image: url(img/icon/btngroup.png);" id="aFloatTools_Show" class="btnOpen"  ></a>
		<!--<a style="display:block" id="aFloatTools_Hide" class="btnCtn" ></a> -->
	</div>	
	<div id="divFloatToolsView" class="floatR" >
	<div class="B">
	     <div class="D" style="font-size:12px;padding-top:2px;padding-right:3px;">查询结果列表</div>
	     <div class="D" style="float:left;">
	        <button style="border:none;float:left;font:12px;padding-left:3px;padding-right:3px;"><img alt="" src="img/icon/11.gif" >排序<img alt="" src="img/icon/14.gif" ></button>
	     </div> 
         <div class="D" ><input type="button" style="border:none;width:16px;height:16px; background:url(img/icon/12.png);padding-left:3px;padding-right:3px;"/></div>
         <div class="D" ><input type="button" style="border:none;width:16px;height:16px; background:url(img/icon/13.png);padding-left:3px;padding-right:3px;"/></div>
         <div class="D" style="float:left;">
	        <button style="border:none;float:left;font:12px;padding-left:3px;padding-right:3px;"><img alt="" src="img/icon/11.gif" >导出<img alt="" src="img/icon/14.gif" ></button>
	     </div>
	     <div class="D"><img alt="" src="img/icon/111.gif;"></div>
         <div class="D" style="float:left;">
	        <button style="border:none;float:left;font:12px;padding-bottom:0.3px;"><img alt="" src="img/icon/14.png" >加入购物车</button>
	     </div>
	            
	</div>
	
	
	
     <div class="A">
          <div class="D" style="padding:2px;"><input type="checkbox" name="" value=""></div>
          <div class="D" style="font-size:14px;padding-left:4px;padding-top:2px;">一共***条</div>
     </div>
		
		
		
		<div > 
		    <div><h3><br>页面测试：</h3>	 </div>	     
             <%-- <jsp:include page="/Test1205_f.jsp"></jsp:include> --%>                   
        </div> 
      <div class="C" >         
           <div class="D" ><input type="button" style="border:none;width:16px;height:16px; background:url(img/icon/1.gif)"/></div>
           <div class="D"><input type="button" style="border:none;width:16px;height:16px; background:url(img/icon/2.gif)"/></div>
           <div class="D"><input type="button" style="border:none;width:45px;height:20px;" value="当前页"></div>
           <div class="D"><input type="text" style="width:50px;height:16px;border:1px solid #000;" value=""></div>
           <div class="D" style="font-size:14px;padding:1px;">共***页</div>
           <div class="D"><input type="button" style="border:none;width:16px;height:16px; background:url(img/icon/6.gif)"/></div>
           <div class="D"><input type="button" style="border:none;width:16px;height:16px; background:url(img/icon/7.gif)"/></div>
           <div class="D"><input type="button" style="border:none;width:16px;height:16px; background:url(img/icon/8.gif)"/></div>
           <div class="D"><input type="button" style="border:none;width:45px;height:20px;" value="每页"></div>
           <div class="D"><input type="text" style="width:28px;height:16px;border:1px solid #000;" value=""></div>
           <div class="D">条</div>
      </div> 
	</div> 	
</div>


 
</body>
</html>