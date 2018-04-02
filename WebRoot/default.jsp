<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

    <title>测绘卫星影像产品管理系统-首页</title>
	

    <script src="./lib/Highcharts-5.0.10/code/highcharts.js"></script>
	<script src="./lib/Highcharts-5.0.10/code/modules/exporting.js"></script>
	<script type="text/javascript" src="lib/jquery.js"></script>
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->

<style type="text/css">
/* Reset style */
* { margin:0; padding:0; word-break:break-all; }
body {height:100%; background:#FFF; color:#333; font:12px/1.6em Helvetica, Arial, sans-serif; }
/* h1, h2, h3, h4, h5, h6 { font-size:1em; } */
a { color:#0287CA; text-decoration:none; }
	a:hover { text-decoration:underline; }
ul, li { list-style:none; }
fieldset, img { border:none; }
legend { display:none; }
em, strong, cite, th { font-style:normal; font-weight:normal; }
input, textarea, select/* , button  */{ font:12px Helvetica, Arial, sans-serif; }
table { border-collapse:collapse; }
html { overflow:-moz-scrollbars-vertical; } /*Always show Firefox scrollbar*/

/* iFocus style */
#ifocus { width:750px; height:620px; margin:20px; border:1px solid #DEDEDE; background:#F8F8F8;display:inline-table}
	#ifocus_pic { display:inline; position:relative; float:left; width:600px; height:600px; overflow:hidden; margin:10px 0 0 10px; }
		#ifocus_piclist { position:absolute; }
		#ifocus_piclist li { width:600px; height:600px; overflow:hidden; }
		#ifocus_piclist img { width:600px; height:600px; }
	#ifocus_btn { display:inline; float:right; width:91px; margin:9px 9px 0 0; }
		#ifocus_btn li { width:91px; height:85px; cursor:pointer; opacity:0.5; -moz-opacity:0.5; filter:alpha(opacity=50); }
		#ifocus_btn img { width:75px; height:75px; margin:7px 0 0 11px; }
		#ifocus_btn .current { background: url(images/ifocus_btn_bg.gif) no-repeat; opacity:1; -moz-opacity:1; filter:alpha(opacity=100); }
	#ifocus_opdiv { position:absolute; left:0; bottom:0; width:600px; height:35px; background:#000; opacity:0.5; -moz-opacity:0.5; filter:alpha(opacity=50); }
	#ifocus_tx { position:absolute; left:8px; bottom:8px; color:#FFF; }
		#ifocus_tx .normal { display:none; }

/* 按钮 */
.headerbtn{background:url(img/page/navbg.jpg);}
.headerbtn:hover{background:url(img/page/navbgover.jpg);}
</style>

  </head>
  
  <body>
    <div style="height:100%;padding:140px 0 0;box-sizing:border-box;">
		<div style=" height:110px;margin:-140px 0 0;background-image: url(img/page/bannerbg.jpg);background-repeat:repeat">
    		<div class="logo margin-big-left fadein-top">   
				<img src="img/page/logo.png"  height="70"/>
			</div>
			<div style="background:none;position:relative;top:4px;left:0px;"> 
				<a href="default.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;">首页</button></a>
				<a href="query.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-6px;">可视化查询</button></a>
				<a href="Archive.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-6px;">数据归档</button></a>
				<a href="Taskmanager.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-6px;">任务管理</button></a>
				<a href="sysmanager.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-6px;">系统管理</button></a> 
			</div>
    	</div>
    	<div align="center" style="height:100%;">
    		<table>
				<tr height="50px">
					<td style="font-size:20px;background:#87CEFA; padding-left:15px; " > <font color="#FFFFFF"><h3>样例数据</h3></font></td>
					<td width="2px"></td>
					<td style="font-size:20px;background:#87CEFA; padding-left:15px; " > <font color="#FFFFFF"><h3>数据统计</h3></font></td>
				</tr>
				<tr height="700px">
					<td width="880px">
						<div id="ifocus" >
							<div id="ifocus_pic">
								<div id="ifocus_piclist" style="left:0; top:0;">
									<ul>
										<li><img src="images/001.jpg" alt="阿里西西" /></li>
										<li><img src="images/002.jpg" alt="阿里西西" /></li>
										<li><img src="images/003.jpg" alt="阿里西西" /></li>
										<li><!-- <a href="http://www.alixixi.com/" target="_blank"> --><img src="images/004.jpg" alt="阿里西西" /></a></li>
									</ul>
								</div>
								<div id="ifocus_opdiv"></div>
								<div id="ifocus_tx">
									<ul>
										<li class="current">资源三号融合影像(南京)</li>
										<li class="normal">资源三号02星正射纠正影像</li>
										<li class="normal">资源三号融合影像(三亚)</li>
										<li class="normal">资源三号棕榈岛</li>
									</ul>
								</div>
							</div>
							<div id="ifocus_btn">
								<ul>
									<li class="current"><img src="images/001.jpg" alt="" /></li>
									<li class="normal"><img src="images/002.jpg" alt="" /></li>
									<li class="normal"><img src="images/003.jpg" alt="" /></li>
									<li class="normal"><img src="images/004.jpg" alt="" /></li>
								</ul>
							</div>
						</div>  
					</td>
					<td></td>
					<td width="580px">
						<div id="container" style="min-width: 310px; height: 330px; max-width: 600px; margin: 0 auto display:inline-table"></div>
						<div style="height:30px"></div>
						<div id="container2" style="min-width: 310px; height: 330px; max-width: 600px; margin: 0 auto display:inline-table"></div>
			
					</td>
				</tr>
				<tr>
				<td></td>
				<td></td>
				<td></td>
				</tr>
			</table>
			
    	</div>
    	<div style="padding-bottom:0px;background-color: #2153A3; height: 30px;width:100%;color:white;text-align:center;">
			<label>国家测绘地理信息局卫星测绘应用中心 版权所有</label>
		</div>
    </div>
    
    
    <script type="text/javascript">

	$.ajax({
		url:"./servlet/CountStatisticservlet",
		type:"POST",
		data:{"operate":"Size"},
		//dataType:"json",
		async:false,
		error:function(request){
			alert("Network Error 网络异常");
		},
		success:function(data){
			var data1=eval("("+data+")");
			Highcharts.chart('container', {
			    chart: {
			        plotBackgroundColor: null,
			        plotBorderWidth: null,
			        plotShadow: false,
			        type: 'pie'
			    },
			    title: {
			        text: '影像数据量统计'
			    },
		        credits:{
		        	enabled:false
		        },
		        exporting:{
		        	enabled:false
		        },
			    tooltip: {
			        pointFormat: '{point.y}GB(<b>{point.percentage:.1f}%</b>)'
			    },
			    plotOptions: {
			        pie: {
			            allowPointSelect: true,
			            cursor: 'pointer',
			            dataLabels: {
			                enabled: true,
			                format: '{point.name}<br>{point.y}GB',
			                style: {
			                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
			                }
			            }
			        }
			    },
			    series: [{
			        name: 'Brands',
			        center: [null, 100],
			        size:200,
			        colorByPoint: true,
			        data: data1
			    }]
			});
			
		}
	});	
	
	$.ajax({
		url:"./servlet/CountStatisticservlet",
		type:"POST",
		data:{"operate":"Count"},
		//dataType:"json",
		async:false,
		error:function(request){
			alert("Network Error 网络异常");
		},
		success:function(data){
			var data1=eval("("+data+")");
			Highcharts.chart('container2', {
			    chart: {
			        plotBackgroundColor: null,
			        plotBorderWidth: null,
			        plotShadow: false,
			        type: 'pie'
			    },
			    title: {
			        text: '影像数据景数统计'
			    },
			    tooltip: {
			        pointFormat: '{point.y}景(<b>{point.percentage:.1f}%</b>)'
			    },
			    credits:{
		        	enabled:false
		        },
		        exporting:{
		        	enabled:false
		        },
			    plotOptions: {
			        pie: {
			            allowPointSelect: true,
			            cursor: 'pointer',
			            dataLabels: {
			                enabled: true,
			                format: '{point.name}<br>{point.y}景',
			                style: {
			                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
			                }
			            }
			        }
			    },
			    series: [{
			        name: 'Brands',
			        center: [null, 100],
			        size:200,
			        colorByPoint: true,
			        data: data1
			    }]
			});			
		}
	});	
		

function addLoadEvent(func){
	var oldonload = window.onload;
	if (typeof window.onload != 'function') {
		window.onload = func;
	} else {
		window.onload = function(){
			oldonload();
			func();
		};
	}
}

function moveElement(elementID,final_x,final_y,interval) {
  if (!document.getElementById) return false;
  if (!document.getElementById(elementID)) return false;
  var elem = document.getElementById(elementID);
  if (elem.movement) {
    clearTimeout(elem.movement);
  }
  if (!elem.style.left) {
    elem.style.left = "0px";
  }
  if (!elem.style.top) {
    elem.style.top = "0px";
  }
  var xpos = parseInt(elem.style.left);
  var ypos = parseInt(elem.style.top);
  if (xpos == final_x && ypos == final_y) {
		return true;
  }
  if (xpos < final_x) {
    var dist = Math.ceil((final_x - xpos)/10);
    xpos = xpos + dist;
  }
  if (xpos > final_x) {
    var dist = Math.ceil((xpos - final_x)/10);
    xpos = xpos - dist;
  }
  if (ypos < final_y) {
    var dist = Math.ceil((final_y - ypos)/10);
    ypos = ypos + dist;
  }
  if (ypos > final_y) {
    var dist = Math.ceil((ypos - final_y)/10);
    ypos = ypos - dist;
  }
  elem.style.left = xpos + "px";
  elem.style.top = ypos + "px";
  var repeat = "moveElement('"+elementID+"',"+final_x+","+final_y+","+interval+")";
  elem.movement = setTimeout(repeat,interval);
}

function classNormal(iFocusBtnID,iFocusTxID){
	var iFocusBtns= document.getElementById(iFocusBtnID).getElementsByTagName('li');
	var iFocusTxs = document.getElementById(iFocusTxID).getElementsByTagName('li');
	for(var i=0; i<iFocusBtns.length; i++) {
		iFocusBtns[i].className='normal';
		iFocusTxs[i].className='normal';
	}
}

function classCurrent(iFocusBtnID,iFocusTxID,n){
	var iFocusBtns= document.getElementById(iFocusBtnID).getElementsByTagName('li');
	var iFocusTxs = document.getElementById(iFocusTxID).getElementsByTagName('li');
	iFocusBtns[n].className='current';
	iFocusTxs[n].className='current';
}

function iFocusChange() {
	if(!document.getElementById('ifocus_btn')) return false;
	document.getElementById('ifocus_btn').onmouseover = function(){atuokey = true;};
	document.getElementById('ifocus_btn').onmouseout = function(){atuokey = false;};
	var iFocusBtns = document.getElementById('ifocus_btn').getElementsByTagName('li');
	var listLength = iFocusBtns.length;
	iFocusBtns[0].onmouseover = function() {
		moveElement('ifocus_piclist',0,0,5);
		classNormal('ifocus_btn','ifocus_tx');
		classCurrent('ifocus_btn','ifocus_tx',0);
	};
	if (listLength>=2) {
		iFocusBtns[1].onmouseover = function() {
			moveElement('ifocus_piclist',0,-600,5);
			classNormal('ifocus_btn','ifocus_tx');
			classCurrent('ifocus_btn','ifocus_tx',1);
		};
	}
	if (listLength>=3) {
		iFocusBtns[2].onmouseover = function() {
			moveElement('ifocus_piclist',0,-1200,5);
			classNormal('ifocus_btn','ifocus_tx');
			classCurrent('ifocus_btn','ifocus_tx',2);
		};
	}
	if (listLength>=4) {
		iFocusBtns[3].onmouseover = function() {
			moveElement('ifocus_piclist',0,-1800,5);
			classNormal('ifocus_btn','ifocus_tx');
			classCurrent('ifocus_btn','ifocus_tx',3);
		};
	}
}

setInterval('autoiFocus()',3500);
var atuokey = false;
function autoiFocus() {
	if(!document.getElementById('ifocus_btn')) return false;
	if(atuokey) return false;
	var focusBtnList = document.getElementById('ifocus_btn').getElementsByTagName('li');
	var listLength = focusBtnList.length;
	for(var i=0; i<listLength; i++) {
		if (focusBtnList[i].className == 'current') var currentNum = i;
	}
	if (currentNum==0&&listLength!=1 ){
		moveElement('ifocus_piclist',0,-600,5);
		classNormal('ifocus_btn','ifocus_tx');
		classCurrent('ifocus_btn','ifocus_tx',1);
	}
	if (currentNum==1&&listLength!=2 ){
		moveElement('ifocus_piclist',0,-1200,5);
		classNormal('ifocus_btn','ifocus_tx');
		classCurrent('ifocus_btn','ifocus_tx',2);
	}
	if (currentNum==2&&listLength!=3 ){
		moveElement('ifocus_piclist',0,-1800,5);
		classNormal('ifocus_btn','ifocus_tx');
		classCurrent('ifocus_btn','ifocus_tx',3);
	}
	if (currentNum==3 ){
		moveElement('ifocus_piclist',0,0,5);
		classNormal('ifocus_btn','ifocus_tx');
		classCurrent('ifocus_btn','ifocus_tx',0);
	}
	if (currentNum==1&&listLength==2 ){
		moveElement('ifocus_piclist',0,0,5);
		classNormal('ifocus_btn','ifocus_tx');
		classCurrent('ifocus_btn','ifocus_tx',0);
	}
	if (currentNum==2&&listLength==3 ){
		moveElement('ifocus_piclist',0,0,5);
		classNormal('ifocus_btn','ifocus_tx');
		classCurrent('ifocus_btn','ifocus_tx',0);
	}
}
addLoadEvent(iFocusChange);
</script>
    
  </body>
</html>
