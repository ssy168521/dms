<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import = "java.io.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查询结果栏</title>

<link rel="stylesheet" href="css/sidebar.css" type="text/css"/> 
<script type="text/javascript" src="lib/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="lib/sidebar.js"></script>

</head>
<body>
<div class="rides-cs" > 
	<div class="floatL" >		
		<a style="display:none;" id="aFloatTools_Hide" class="btnCtn"  href="javascript:void(0);"></a> 
		<a style="display:block;" id="aFloatTools_Show" class="btnOpen" href="javascript:void(0);"></a>
	</div>
	<div id="divFloatToolsView" style="position:relative;top:0px;bottom:50px;right:0px;overflow:hidden;display:none;width:auto;height:100%;border:1px solid #99bbe8;background-color:white;">
		<div id="First">
		     <div class="Row" style="font-size:12px;padding-top:2px;padding-right:3px;">查询结果列表</div>
		     <div class="Row" >
		        <button style="border:none;float:left;font:12px;padding-left:3px;padding-right:3px;"><img alt="" src="img/icon/11.gif" >排序<img alt="" src="img/icon/14.gif" ></button>
		     </div> 
	         <div class="Row" ><input type="button" style="border:none;width:16px;height:16px; background:url(img/icon/12.png);padding-left:3px;padding-right:3px;"/></div>
	         <div class="Row" ><input type="button" style="border:none;width:16px;height:16px; background:url(img/icon/13.png);padding-left:3px;padding-right:3px;"/></div>
		     <button id="exportTool" style="border:none;float:left;font:12px;padding-left:3px;padding-right:3px;"><img alt="" src="img/icon/11.gif" >导出<img alt="" src="img/icon/14.gif" ></button>
			 
		     <div class="Row"><img alt="" src="img/icon/111.gif;"></div>
	         <div class="Row">
		        <button style="border:none;float:left;font:12px;padding-bottom:0.3px;"><img alt="" src="img/icon/14.png" >加入购物车</button>
		     </div>
		 </div>
		 <div id="exportToolDiv" style="position:fixed;top:13%;left:92%;z-index:999;border:1px solid #999;border-radius:4px; display:none;z-index:9999;background:white;">                 
			<button id="exportShp">shp文件</button>
			<input id="exportToolClose" type="button" style="right:0px;border:none;background:transparent;" value="×"></input></br>
			<button id="exporttxt">txt文件</button>			    
		 </div>
	     <div id="Second">
	          <div class="Row" style="padding:2px;"><input type="checkbox" name="checkall" id="checkall" value=""></div>
	          <div class="Row" style="font-size:14px;padding-left:4px;padding-top:2px;"><p id="sumnotes">共0条</p></div>
	          
	     	  <div class="Row">	  
 	     	  <form action="servlet/OverviewShow" method="post">
	     	  		<input type="hidden" id="selecteditem" name="selecteditem">
	     	  		<input type="button" id="btnOverviewShow" style="border:none;float:left;font:12px;padding-bottom:0.3px;background:url(img/icon/ddkst.png) no-repeat" value="   叠加快视图">
	     	  </form>
	     	  </div>
	     </div></br>
		 <div id="jsonTip" style="position:absolute;top:60px; width:auto"></div></br>
	     <div id="Final" >         
			<div class="Row"><input type="button" style="border:none;width:16px;height:16px; background:url(img/icon/2.gif)"/></div>
			<div class="Row"><input type="button" id="prepage" style="border:none;width:16px;height:16px; background:url(img/icon/1.gif)"/></div>
			<div class="Row"><input type="button" style="border:none;width:45px;height:20px;" value="当前页"></div>
			<div class="Row"><input type="text" id="pageindex" style="width:30px;height:16px;border:1px solid #000;" value="1"></div>
			<div class="Row" style="font-size:14px;padding:1px;"><p id="sumpages">共0页</p></div>
			<div class="Row"><input type="button" id="nextpage" style="border:none;width:16px;height:16px; background:url(img/icon/6.gif)"/></div>
			<div class="Row"><input type="button" style="border:none;width:16px;height:16px; background:url(img/icon/7.gif)"/></div>
			<div class="Row"><input type="button" style="border:none;width:16px;height:16px; background:url(img/icon/8.gif)"/></div>
			<div class="Row"><input type="button" style="border:none;width:45px;height:20px;" value="每页"></div>
			<div class="Row"><input type="text" id="perpagenum" style="width:28px;height:16px;border:1px solid #000;" value="10"></div>
			<div class="Row">条</div>
	     </div>
	</div>
	<!-- 
	<div id="divFloatToolsView" class="floatR" >
		 
	</div> -->
</div>

<script type="text/javascript">
	//导出工具栏
	document.getElementById("exportTool").onclick=function(){
		document.getElementById("exportToolDiv").style.display="block";
	};
	document.getElementById("exportToolClose").onclick=function(){
		document.getElementById("exportToolDiv").style.display="none";
	};
	
$(function(){
	$("#exportShp").click(function(){
		$("#submitType").val('exportShp');
		qureyform.submit();	
	});	
});

$(function(){
	$("#btnOverviewShow").click(function(){
		/* $("#submitType").val('exportShp');
		qureyform.submit();	 */
		document.getElementById('qureyform').action="servlet/OverviewShow";
		qureyform.submit();		
	});	
});

	
</script>


<script type="text/javascript">
	var chkallbox=$("#checkall").val();
	if(chkallbox){
		$("#selecteditem").val("all");
	}
	
	


</script>

<script type="text/javascript">
function initisidebar(){
    $.getJSON("data/temp.geojson",function(data){
	   	$("#pageindex").val(1);
	   	var listnum=data.features.length;
	   	/* $("#sumnotes").val("共"+listnum+"条"); */
	   	var pageidx=Number($("#pageindex").val());
	   	var perpagenum=Number($("#perpagenum").val());
	   	document.getElementById("sumnotes").innerHTML="共"+listnum+"条";
	   	document.getElementById("sumpages").innerHTML="共"+Math.ceil(listnum/perpagenum)+"页";
	   	var $jsontip = $("#jsonTip");
       	$jsontip.empty();
       	var startidx=perpagenum*(pageidx-1);
       	var endidx=perpagenum*(pageidx);
       	if(endidx>listnum) endidx=listnum; 
       	var strHtml =listhtml(data,startidx,endidx);
       	$jsontip.html(strHtml);
   	});
}
function listhtml(data,start,end){
	var strHtml = "";
	for (var i=start;i<end;i++)
	{
		strHtml+= "<div class=\"Row\" style=\"font-size:12px;padding:1px;\">";	
		strHtml+= "<input type=\"checkbox\" id=\""+data.features[i].properties.F_DATAID+"\">";		
		strHtml+= data.features[i].properties.F_DATANAME+"</br>";
		strHtml+= "</div></br>";
		strHtml+= "数据ID:"+data.features[i].properties["F_DATAID"]+"<br>";
		
		strHtml+= "PATH:"+data.features[i].properties.F_SCENEPATH;
		strHtml+= "  ROW:"+data.features[i].properties.F_SCENEROW+"<br>";
		strHtml+= "轨道号:"+data.features[i].properties.F_ORBITID;
		strHtml+= "  人工云量:"+data.features[i].properties.F_OVERALLDATAQUALITY+"<br>";
		strHtml+= "<hr> ";
	}
	return strHtml;
}

$(function(){
	$("#nextpage").click(function(){
		/* document.getElementbyId("pageindex") */
		var pageidx=Number($("#pageindex").val());
		pageidx++;
		$("#pageindex").val(pageidx);
		var perpagenum=Number($("#perpagenum").val());
		
        $.getJSON("data/temp.geojson",function(data){
        	var $jsontip = $("#jsonTip");        	
        	$jsontip.empty();
        	var listnum=data.features.length;
        	/* $("#sumnotes").val("共"+listnum+"条"); */
/*         	document.getElementById("sumnotes").innerHTML="共"+listnum+"条";
        	document.getElementById("sumpages").innerHTML="共"+parseInt(listnum/perpagenum)+"页"; */
        	var startidx=perpagenum*(pageidx-1);
        	var endidx=perpagenum*(pageidx);
        	if(endidx>listnum) endidx=listnum; 
        	var strHtml =listhtml(data,startidx,endidx);
        	
        	$jsontip.html(strHtml);
		});
	});	
});
$(function(){
	$("#prepage").click(function(){
		/* document.getElementbyId("pageindex") */
		var pageidx=Number($("#pageindex").val());
		pageidx--;
		$("#pageindex").val(pageidx);
		var perpagenum=Number($("#perpagenum").val());
		
        $.getJSON("data/temp.geojson",function(data){
        	var $jsontip = $("#jsonTip");
        	$jsontip.empty();
        	var listnum=data.features.length;
        	var startidx=perpagenum*(pageidx-1);
        	var endidx=perpagenum*(pageidx);
        	if(endidx>listnum) endidx=listnum; 
        	var strHtml =listhtml(data,startidx,endidx);
        	$jsontip.html(strHtml);
		});
	});	
});

</script>

</body>
</html>