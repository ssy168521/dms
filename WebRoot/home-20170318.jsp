<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import = "java.io.*" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
	String rootpath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort();
	System.out.println(path);
	System.out.println(basePath);
	System.out.println(request.getScheme());
	System.out.println(request.getServerName());
	System.out.println(request.getServerPort());
	System.out.println(rootpath);
	com.sasmac.util.AppConfUtil util= com.sasmac.util.AppConfUtil.getInstance();
	util.SetAppconFile("appconf.xml");
	
	String TDTUrl=util.getProperty( "TDTURL");
	String VectBaseMapURL=util.getProperty("VectBaseMapURL");
	System.out.println(TDTUrl);
	System.out.println(VectBaseMapURL);
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>测绘卫星影像产品管理系统</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="initial-scale=1, maximum-scale=1,user-scalable=no">

<link rel="StyleSheet" type="text/css" href="lib/dtree/dtree.css" />
<script type="text/javascript" src="lib/dtree/dtree.js"></script>

<link rel="StyleSheet" type="text/css" href="css/home.css" />

<link rel="stylesheet" type="text/css"
	href="<%=rootpath%>/arcgisAPI317/library/3.17/3.17/dijit/themes/tundra/tundra.css" />
<link rel="stylesheet" type="text/css"
	href="<%=rootpath%>/arcgisAPI317/library/3.17/3.17/esri/css/esri.css" />
<script type="text/javascript"
	src="<%=rootpath%>/arcgisAPI317/library/3.17/3.17/init.js"></script>

<!-- Terraformer reference -->
<script src="./lib/vendor/terraformer/terraformer.min.js"></script>
<script	src="./lib/vendor/terraformer-arcgis-parser/terraformer-arcgis-parser.min.js"></script>
<!-- 创建目录树 -->
<script src="./js/catalog.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="./lib/My97DatePicker/WdatePicker.js"></script>

<link rel="stylesheet" type="text/css" href="css/sidebar.css" /> 
<!-- <script type="text/javascript" src="lib/jquery-1.4.2.min.js"></script> -->


<script src="./lib/jquery.js"></script>
<script src="./lib/bootstrap/js/bootstrap.js"></script>	
<script src="./lib/bootstrap-table/bootstrap-table.js"></script>
<script src="./lib/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>

<link rel="stylesheet" href="./lib/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="./lib/bootstrap-table/bootstrap-table.css" />
<script type="text/javascript" src="lib/sidebar.js"></script>

<link rel="stylesheet" href="css/sidebar.css" type="text/css"/> 
<%-- <jsp:include page="/sidebar11.jsp"/> --%>

<style>
html,body,#main {
	width: 100%;
	height: 100%;
	margin: 0;
}
.headerbtn{background:url(img/page/navbg.jpg);}
.headerbtn:hover{background:url(img/page/navbgover.jpg);}
</style>
<script type="text/javascript">
	dojoConfig = {
		isDebug : true,
		async : true
	};
</script>

<script type="text/javascript">
    //控制是否加载overview;
    var bIsLoadoverview ; 
	var map, geoJsonLayer,toolbar,graLayer,imglayer,handle,lightlay;//handle用来记录map的click事件;
	require([ "dojo/parser", "esri/map", "esri/dijit/Scalebar",
		"esri/layers/ArcGISDynamicMapServiceLayer","esri/layers/WMSLayer",
		"esri/layers/TiledMapServiceLayer", "./lib/fwVecLayer.js",
		"esri/toolbars/draw","esri/graphic","esri/tasks/FeatureSet",
		"esri/layers/MapImageLayer",
        "esri/layers/MapImage",
		"esri/symbols/SimpleMarkerSymbol",
		"esri/symbols/SimpleLineSymbol",
		"esri/symbols/SimpleFillSymbol", "dijit/registry",
		"./lib/geojsonlayer.js", "dojo/on", "dojo/dom","esri/tasks/IdentifyTask","esri/tasks/IdentifyParameters",
		"esri/InfoTemplate","esri/dijit/Popup","dojo/_base/array",
     	"esri/Color","dojo/dom-construct",
		"dijit/layout/BorderContainer", "dijit/layout/ContentPane",
		"dijit/layout/AccordionContainer", 
		"dojo/domReady!" ],
		function(parser, Map, Scalebar, ArcGISDynamicMapServiceLayer,WMSLayer,
				TiledMapServiceLayer,fwVecLayer,Draw,Graphic,FeatureSet,MapImageLayer,MapImage,
				SimpleMarkerSymbol, SimpleLineSymbol, SimpleFillSymbol,
				registry, GeoJsonLayer, on, dom,IdentifyTask, IdentifyParameters,InfoTemplate,Popup,
				arrayUtils, Color, domConstruct) {
				parser.parse();
				
				var popup = new Popup({
		          fillSymbol: new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID,
		            new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID,
		              new Color([255, 0, 0]), 2), new Color([255, 255, 0, 0.25]))
		        }, domConstruct.create("div"));
				
				map = new Map("mapDiv", {
					center : [ 100, 35 ],
					zoom : 5,
					logo : false,
					infoWindow: popup
				});
				
				var scalebar = new Scalebar({
					map : map,
					attachTo : "bottom-left",
					scalebarUnit:"dual",
					scalebarStyle : "ruler"
				});
				
				var resourceInfo = {  
                    extent: new esri.geometry.Extent(-180,-90,180,90,{wkid: 4326}),  
                    layerInfos: [],  
                    version : '1.1.1'
                }; 
                
                var agsWmsUrl = "<%=TDTUrl%>";
                var agsWmsLayer = new WMSLayer(agsWmsUrl ,{resourceInfo: resourceInfo}); 
                agsWmsLayer.setImageFormat("png");
                agsWmsLayer.setVisibleLayers([0]);
                map.addLayer(agsWmsLayer);
				
				//map.on("load", mapReady);
				on(dom.byId("btnSelect"), "click",mapReady);
				
		        var VectorBaseMapURL =  "<%=VectBaseMapURL%>";
		        map.addLayer(new ArcGISDynamicMapServiceLayer(VectorBaseMapURL,{ opacity: 0.95 }));
		        
		        //快视图层
				imglayer=new MapImageLayer();
	 	                 map.addLayer(imglayer);
		        
				function mapReady () {
		          handle=map.on("click", executeIdentifyTask);
		          //create identify tasks and setup parameters
		          identifyTask = new IdentifyTask(VectorBaseMapURL);
		
		          identifyParams = new IdentifyParameters();
		          identifyParams.tolerance = 3;
		          identifyParams.returnGeometry = true;
		          //identifyParams.layerIds = [0,1,2,3];//设置进行identify的图层，一个服务中的多个图层可以设置不同图层
		          identifyParams.layerOption = IdentifyParameters.LAYER_OPTION_ALL;
		          identifyParams.width = map.width;
		          identifyParams.height = map.height;
		    	}
		
	        function executeIdentifyTask (event) {
	          identifyParams.geometry = event.mapPoint;
	          identifyParams.mapExtent = map.extent;
	          
	          //设置查询的图层
	          var scale=map.getScale();
	          if(scale>25000000){
	          	identifyParams.layerIds = [3];
	          }else if(scale>6000000&&scale<=25000000){		   
	          	identifyParams.layerIds = [2];		          
	          }else if(scale>2000000&&scale<=6000000){
	          	identifyParams.layerIds = [1];
	          }else{
	          //scale<=2000000
	          	identifyParams.layerIds = [0];
	          }
	          
	          var deferred = identifyTask
	            .execute(identifyParams)
	            .addCallback(function (response) {
	              // response is an array of identify result objects
	              // Let's return an array of features.
	              return arrayUtils.map(response, function (result) {
	                var feature = result.feature;
	                var wktPoly=PolygonToWKT(feature.geometry);
	                document.getElementById("wktPoly").value=wktPoly;
	                return feature;
	              });
	            });

	          // InfoWindow expects an array of features from each deferred
	          // object that you pass. If the response from the task execution
	          // above is not an array of features, then you need to add a callback
	          // like the one above to post-process the response and return an
	          // array of features.
	          map.infoWindow.setFeatures([deferred]); 
	          /* map.infoWindow.show(event.mapPoint);  */
	        }
		
			on(dom.byId("btnClearDraw"), "click",clearDraw);
			//清除绘制的图形
			function clearDraw(){
				if(handle!=null) handle.remove();
				if(graLayer!=null) graLayer.clear();
				if(geoJsonLayer!=null) geoJsonLayer.clear();
				if(imglayer!=null) imglayer.removeAllImages();
				map.graphics.clear();
				document.getElementById("wktPoly").value=null;		
				$('#resulist').bootstrapTable('removeAll');	
			}
			
			$("#drawPOINT").click(function(){
		    	if(handle!=null) handle.remove();
				toolbar=new Draw(map);
				toolbar.activate(Draw.POINT);
				map.hideZoomSlider();
				toolbar.on("draw-end", addToMap);
			});
			$("#drawPOLYLINE").click(function(){
			    if(handle!=null) handle.remove();
				toolbar=new Draw(map);
				toolbar.activate(Draw.POLYLINE);
				map.hideZoomSlider();
				toolbar.on("draw-end", addToMap);
			});
			$("#drawPOLYGON").click(function(){
			    sif(handle!=null) handle.remove();
				toolbar=new Draw(map);
				toolbar.activate(Draw.POLYGON);
				map.hideZoomSlider();
				toolbar.on("draw-end", addToMap);
			});
			$("#drawRECTANGLE").click(function(){
		    	if(handle!=null) handle.remove();
				toolbar=new Draw(map);
				toolbar.activate(Draw.RECTANGLE);
				map.hideZoomSlider();
				toolbar.on("draw-end", addToMap);
			});
			
			function addToMap(evt) {
				var symbol;
				toolbar.deactivate();
				map.showZoomSlider();
				switch (evt.geometry.type) {
				case "point":
				case "multipoint":
					symbol = new SimpleMarkerSymbol();
					break;
				case "polyline":
					symbol = new SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_DASHDOT,
								new dojo.Color([ 155, 0, 255 ]), 2);
					break;
				default:
					symbol = new SimpleFillSymbol(
						esri.symbol.SimpleFillSymbol.STYLE_SOLID,
						new esri.symbol.SimpleLineSymbol(
								esri.symbol.SimpleLineSymbol.STYLE_DASHDOT,
								new dojo.Color([ 255, 0, 0 ]), 1), new dojo.Color([
								255, 255, 0, 0.25 ]));
					break;
				}
				var graphic = new Graphic(evt.geometry, symbol);
				map.graphics.add(graphic);					
				//var jsontemp=evt.geometry.toJson();
				//var jsonPoly=JSON.stringify(jsontemp);//将object类型的json转成string					
				var wktPoly=PolygonToWKT(evt.geometry);
				
				document.getElementById("wktPoly").value=wktPoly;
			}
			
			/**
			 * @Polygon对象转化成WKT
			*/
			function PolygonToWKT(geometry){
				var wkt = [];
				var rings = geometry.rings;
				for(var i in rings){
					var tmp=[];
					var ring = rings[i];
					for(var j in ring){
						var p = ring[j];
						tmp.push(p.join(" "));
					}
					wkt.push("(("+tmp.join(",")+"))");
				}
				return "MULTIPOLYGON("+wkt.join(",")+")";
			}
			
			
			map.infoWindow.domNode.className += " light";
		
			function addGeoJsonLayer() {
				var url="data/temp.geojson";
				geoJsonLayer = new GeoJsonLayer({
					url : url
				});
				// Zoom to layer
				geoJsonLayer.on("update-end", function(e) {
					map.setExtent(e.target.extent.expand(1.2));
				});
				map.addLayer(geoJsonLayer);
			}
			if(<%=session.getAttribute("result")%>==true){
				addGeoJsonLayer();
				initisidebar();
				<%
				session.setAttribute("result",false);
				%>
			}

				
			$("#testsub").click(function(){
			    bIsLoadoverview=true;
			   var tablename= $("#tbname").val();
			   if(tablename=="")
			   {
			      alert("请选择要查询的影像产品类型");
			      return;
			   }
				var layid='quyresulayer';
				//clearMouse();				
				if(geoJsonLayer!=null){
					geoJsonLayer.clear();
					map.removeLayer(geoJsonLayer);
				}
				$.ajax({
					url:"servlet/DBQuery",
					type:"POST",					
					data:$("#qureyform").serialize(),
					//async:false,
					error:function(request){
						alert("网络异常");
					},
					success:function(data){						
						var obj= eval("("+data+")");
						geoJsonLayer = new GeoJsonLayer({
							data : obj,
							id :layid
						});
						// Zoom to layer
						geoJsonLayer.on("update-end", function(e) {
							map.setExtent(e.target.extent.expand(1.2));
						});
						map.addLayer(geoJsonLayer);
						//initisidebar2(obj);
						
						setQuylist(obj,layid);
					}
				});
			});
		
			function setQuylist(data,layid){
			
			
				var features=data.features;
				var obj=new Array();
				for(i in features){
					var tmp= features[i].properties.acquisitionTime;
					tmp=tmp.substring(0,10);
					features[i].properties.acquisitionTime=tmp;
					obj.push(features[i].properties);
				}
				if($('#resulist').bootstrapTable!=null)
					$('#resulist').bootstrapTable('destroy');		
				$('#resulist').bootstrapTable('removeAll');
				$('#resulist').bootstrapTable({
					method: 'get',
					cache: false,
					height: 600,
					toolbar: '#toolbar',
					striped: true,
					//pagination: true,
					pageSize: 10,
					pageNumber: 1,
					//pageList: [5,10, 20, 50, 100],
					search: true,
					showColumns: true,
					showRefresh: true,
					showToggle: true,
					showExport: true,
					exportTypes: ['csv','txt','xml'],
					search: true,			
					//cardView:true,
					//clickToSelect: true,
					columns: [{field:"select",title:"全选",checkbox:true,width:20,align:"center",valign:"middle"},
					{field:"scenePath",title:"PATH",align:"center",valign:"middle",sortable:"true"},
					{field:"sceneRow",title:"ROW",align:"center",valign:"middle",sortable:"true"},
					{field:"orbitID",title:"轨道号",align:"center",valign:"middle",sortable:"true"},
					{field:"cloudPercent",title:"云量",align:"center",valign:"middle",sortable:"true"},
					{field:"acquisitionTime",title:"拍摄时间",align:"center",valign:"middle",sortable:"true"},
					],
					data:obj,
		            formatNoMatches: function(){
		            	return '无符合条件的记录';
		            },
		            
		            onClickRow:function (row){
		            	//行点击事件
		            	//alert(JSON.stringify(row));
		            	lightSelect(layid,row.FID);
		            }            
				});
				$("#toolbar").css({display:"block"});	
				$(window).resize(function () {
					$('#resulist').bootstrapTable('resetView');
				});
			}

			lightlay=new esri.layers.GraphicsLayer({id:'lightlay'});
			function lightSelect(layid,graFID){
				if(lightlay!=null){
					lightlay.clear();
					map.removeLayer(lightlay);
				}
				var layer=map.getLayer(layid);
				var laygraphics=layer.graphics;
				for(var i=0;i<laygraphics.length;i++){
					var tmpgra=laygraphics[i];
					if(tmpgra.attributes.FID==graFID) {
						map.setExtent(tmpgra.geometry.getExtent().expand(1.5));
						var symbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID,
									    new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID,
									    new Color([255,0,0]), 2), new Color([98,194,204,0])
									  );
						var graAdd=new Graphic(tmpgra.geometry,symbol);
						lightlay.add(graAdd);
					}
				}
				map.addLayer(lightlay,10);
				//lightlay.redraw();
			}
			
			
			
			$("#resulist").on("change",function(){
				$('#resulist').bootstrapTable('resetView');
			});
			
			
			
			
			function createGraphic(arcgisJson) {
				var graphic;
				// This magically sets geometry type!
				graphic = new Graphic(arcgisJson);
/* 				// Set the correct symbol based on type and render - NOTE: Only supports simple renderers
				if (this.renderer && this.renderer.symbol) {
					//graphic.setSymbol(this.render.getSymbol(graphic));  // use for complex renderers
					graphic.setSymbol(this.renderer.symbol);
				} else {
					graphic.setSymbol(this._getEsriSymbol(graphic.geometry.type));
				} */
				var symbol = new SimpleFillSymbol();;
				graphic.setSymbol(symbol);
				// Update SR because terraformer sets incorrect spatial reference
				//graphic.geometry.setSpatialReference(this._inSpatialReference); // NOTE: Has to match features!
				return graphic;
			}
			
			
			if(<%=session.getAttribute("quyresult")%>!=null){
				
				<%session.setAttribute("result",null);%>
			}
			
			document.getElementById("btnInputXY").onclick=function(){
				
				var pntX1=document.getElementById("PntX1").value;
				var pntY1=document.getElementById("PntY1").value;
				var pntX2=document.getElementById("PntX2").value;
				var pntY2=document.getElementById("PntY2").value;
				if(pntX1==""||pntY1==""||pntX2==""||pntY2=="")
				{
					alert("请输入对角点坐标");
					return false;
				}
				if(pntX1==pntX2||pntY1==pntY2)
				{
					alert("输入坐标有误，请重新输入对角点坐标！");
					return false;
				}
				var jsonPoly="{\"rings\":[[";
				jsonPoly=jsonPoly+"["+pntX1+","+pntY1+"],";
				jsonPoly=jsonPoly+"["+pntX1+","+pntY2+"],";
				jsonPoly=jsonPoly+"["+pntX2+","+pntY2+"],";
				jsonPoly=jsonPoly+"["+pntX2+","+pntY1+"],";
				jsonPoly=jsonPoly+"["+pntX1+","+pntY1+"]]],\"spatialReference\":{\"wkid\":4326}}";
				var poly = new esri.geometry.Polygon(eval("("+jsonPoly+")"));
				/* var poly = new esri.geometry.Polygon([[20,30],[20,50],[40,50],[40,30],[20,30]]); */
	
				PolygonSymbol = new esri.symbol.SimpleFillSymbol(
						esri.symbol.SimpleFillSymbol.STYLE_SOLID,
						new esri.symbol.SimpleLineSymbol(
								esri.symbol.SimpleLineSymbol.STYLE_DASHDOT,
								new dojo.Color([ 255, 0, 0 ]), 1), new dojo.Color([
								255, 255, 0, 0.25 ]));			
				var graphic = new esri.Graphic(poly, PolygonSymbol);
				clearDraw();//清除地图上绘制的图形
				graLayer = new esri.layers.GraphicsLayer();
				graLayer.add(graphic);
				map.addLayer(graLayer);
				map.setExtent(poly.getExtent().expand(1.5));
				var wktPoly=PolygonToWKT(graphic.geometry);
				document.getElementById("wktPoly").value=wktPoly;
				/* return jsonPoly; */
				/* map.removeLayer(graLayer); */
			};
			$("#downloadId").click(function(){
			
			var objSelec=$('#resulist').bootstrapTable('getSelections');
			if(objSelec==null)	return false;
			var strtmp=JSON.stringify(objSelec);
			$.ajax({
						url:"./servlet/DownloadData",
						type:"POST",
						data:{"objSelec":strtmp},
						//dataType:"json",
						async:false,
						error:function(request){
							alert("Network Error 网络异常");
						},
						success:function(data){

						}
					});		
			
			});
		
		$("#exportshpId").click(function(){
			
			var objSelec=$('#resulist').bootstrapTable('getSelections');
			if(objSelec==null)	return false;
			var strtmp=JSON.stringify(objSelec);
			$.ajax({
						url:"./servlet/ExportSHP",
						type:"POST",
						data:{"objSelec":strtmp},
						//dataType:"json",
						async:false,
						error:function(request){
							alert("Network Error 网络异常");
						},
						success:function(data){
                            window.location.href="./servlet/ExportSHP";
						}
					});		
			
			});
		 $("#showoverviewId").click(function(){		
			var v=$("#sp").attr("class");
			
			if(v=="glyphicon glyphicon glyphicon-eye-close")
			{
			   imglayer.setVisibility(true);
			   v="glyphicon glyphicon glyphicon-eye-open";
			}
		    else
			{
			    imglayer.setVisibility(false);
			   
			    v="glyphicon glyphicon glyphicon-eye-close";
			}
			$("#sp").attr("class",v);
			
			if(bIsLoadoverview==false) return;
			
			var objSelec=$('#resulist').bootstrapTable('getSelections');
			if(objSelec==null)	return false;
			var strtmp=JSON.stringify(objSelec);
			$.ajax({
						url:"./servlet/Loadoverview",
						type:"POST",
						data:{"objSelec":strtmp},
						//dataType:"json",
						async:false,
						error:function(request){
							alert("Network Error 网络异常");
						},
						success:function(data){

		                  if(imglayer !=null )
		                  {
		                      imglayer.removeAllImages();
		                  }
		                 
		                 var obj=eval("("+data+")");
		                 for (var i=0;i<obj.ext.length;i++)
		                 {	             
		                     var img=new esri.layers.MapImage({
							 'extent':obj.ext[i],
							 'href':obj.path[i]});
							  imglayer.addImage(img);	
		                 }
		                 bIsLoadoverview=false;
						}
					});		

		}); 
			
			function addpng(){
				var mil=new MapImageLayer();
				map.addLayer(mil);
				var strExtent=<%=session.getAttribute("strExtent")%>;
				
				var mi=new esri.layers.MapImage({
				'extent':strExtent,
				'href':'./img/test.png'});
				mil.addImage(mi);
			}
			
			if(<%=session.getAttribute("resultMosaic")%>==true){
				alert('sdsdsd');
				addpng();
				<%session.setAttribute("resultMosaic",false);%>
			}
			

			if(<%=session.getAttribute("resultstatistic")%>!=null){
				alert("面积为："+<%=session.getAttribute("resultstatistic")%>+"平方米");
				<%session.setAttribute("resultstatistic",null);%>				
			}
						
			<%
 	 		String[] strPNGfiles=null;
			strPNGfiles=(String[])request.getAttribute("strPNGfiles");
			String[] strPNGExtents=(String[])request.getAttribute("strPNGExtents");	


		 	if(strPNGfiles!=null)
		 	{
		 	%>
		 		imglayer=new MapImageLayer();
		 		imglayer.on("update-end", function(e) {
					map.setExtent(e.target.extent.expand(1.2));
				});
		 		map.addLayer(imglayer);
		 	<%		 		
			 	for(int i=0;i<strPNGfiles.length;i++)
				{
					String strFile=strPNGfiles[i];
					String strExtent=strPNGExtents[i];
			%>
					/* 注意此处一定要加双引号，否则无法识别 */
					var strfileurl="<%=strFile%>";
					var strfileextent=<%=strExtent%>;
					
					var img=new esri.layers.MapImage({
					'extent':strfileextent,
					'href':strfileurl});
					imglayer.addImage(img);
			<%			
				}
		 	}
			%>
			
		});
</script>

</head>

<body class="tundra">
	<div data-dojo-type="dijit/layout/BorderContainer"
		data-dojo-props="design:'headline',gutters:false" id="main">
		<div data-dojo-type="dijit/layout/ContentPane"
			data-dojo-props="region:'top'"
			style="z-index: 0;background-image: url(img/page/bannerbg.jpg);background-size:auto auto;background-repeat:repeat">
			
			<div data-dojo-type="dijit/layout/ContentPane"
			     data-dojo-props="region:'left',splitter:'true'"
			     style="float:left;"><a href="home.jsp"><img src="img/page/logo.png" style="padding-left:15px;background:none;"></a>
			     <div style="background:none;">
			     <a href=""><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;">首页</button></a>
			     <a href="home.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-7px;">可视化查询</button></a>
			     <a href="Archive.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-7px;">数据归档</button></a>
			     <a href="jsp/statistics.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-7px;">数据统计</button></a>
			     <a href="sysmanager.html"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-7px;">系统管理</button></a>
			     <a href="login.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-7px;">登录</button></a>  
		         <!-- <button style="border:none;width:100px;onmouseover="this.style.background:url(img/navbg.jpg);" onmouseout="this.style.background=url(img/navbgover.jpg);"color:white;">首页</button>  -->   
			     </div>
			</div>			 
			<div data-dojo-type="dijit/layout/ContentPane"
			     data-dojo-props="region:'right',splitter:'true'"
			     style="float:right;"><img src="img/page/banner.jpg">
			</div>
		</div>

		<div data-dojo-type="dijit/layout/ContentPane"
			data-dojo-props="region:'left', splitter:'true'"
			style="background-color: #e0ffff;width:330px;">

			<div data-dojo-type="dijit/layout/ContentPane"
				data-dojo-props="region:'top', splitter:'true'"
				style="background-color: white;height:50%;">				
				<div data-dojo-type="dijit/layout/ContentPane"
				data-dojo-props="region:'top',splitter:'true'"
				style="background-color: #ffffff;height:10%;">
				   <div style="background:#F5F5F5;padding-left:20px;font-color:blue;font-size:14px;border:1px solid #99bbe8;">
				                查询目录树
				   </div>
			    </div>
				<div class="dtree" style="padding-left:15px;">
					<script type="text/javascript">
						var CataTree = new dTree('CataTree');
						GetCatalog(CataTree);
					</script>
					<!-- <table id="resulist"></table> -->
				</div>
			</div>
			<div data-dojo-type="dijit/layout/ContentPane"
				data-dojo-props="region:'bottom', splitter:'true'"
				style="background-color: #ffffff;height:50%;">
				<div data-dojo-type="dijit/layout/ContentPane"
					data-dojo-props="region:'top'"
					style="background-color: #ffffff;height:10%;">
					<div style="background:#F5F5F5;padding-left:20px;font-color:blue;font-size:14px;border:1px solid #99bbe8;">
					             查询条件
					</div>
				</div>
				<div style="position:relative;bottom:10px;height:90%;font:normal 12px tahoma,arial,helvetica,sans-serif;">
					<form id="qureyform" action="servlet/Query" method="post">
						<table width="100%">
							<tr height="40" >
								<td width="30%" style="text-align:right;padding-right:0px;">
									轨道号：
								</td>
								<td width="65%" style="text-align:left;padding-right:0px;">
									<input name="orbitid" type="text" style="width:204px;text-align:right;" >
								</td>
							</tr>
							<tr height="40" >
								<td width="30%" style="text-align:right;padding-right:0px;">
									人工云量：
								</td>
								<td style="text-align:left;padding-right:0px;">
									<input name="cloud1" type="number" value=0 min=0 max=100 style="width:90px;text-align:right;" >
									至 <input name="cloud2" type="number" min=0 max=100  value=20 style="width:90px;text-align:right;">
								</td>
							</tr>
							<tr height="40" >
								<td width="30%" style="text-align:right;padding-right:0px;">
									景 PATH：
								</td>
								<td style="text-align:left;padding-right:0px;">
									<input name="path1" type="number" style="width:90px;text-align:right;">
									 至 <input name="path2" type="number" style="width:90px;text-align:right;">
								</td>
							</tr>
							<tr height="40" >
								<td width="30%" style="text-align:right;padding-right:0px;">					
									景 ROW：
								</td>
								<td style="text-align:left;padding-right:0px;">
									<input name="row1" type="number" style="width:90px;text-align:right;"> 至 <input name="row2" type="number" style="width:90px;text-align:right;">
								</td>
							</tr>
							<tr height="40" >
								<td width="30%" style="text-align:right;padding-right:0px;">
									采集日期：
								</td>
								<td style="text-align:left;padding-right:0px;">
									<input id="date1" name="date1" class="Wdate" type="text" style="width:90px;" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'2012-01-01 00:00:00',maxDate:'#F{$dp.$D(\'date2\')||\'new Date()\'}'})"/>
	     							  至 <input id="date2" name="date2" class="Wdate" type="text" style="width:90px;" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'date1\')}',maxDate:'%y-%M-%d'})"/> 
								</td>
							</tr>
						</table>
						
						<input type="hidden" id="tbname" name="tbname">
						<input type="hidden" id="submitType" name="submitType" >
						<input type="hidden" id="wktPoly" name="wktPoly" >
						<div>
						<div style="positon:absolute;bottom:10px;display:inline;padding-left:52px;width:120px;">
							<!-- <button type="submit" name="query" value="查询"><img alt="" src="img/icon/search.png" >查 询</button> -->
							<button type="submit" name="statistic" value="面积统计"><img alt="" src="img/icon/search.png" >面积统计</button>
							<input type="button" id="testsub" value="数据查询">
							<!-- <input type="button" id="testmap" value="ssss"> -->
                        </div>
					</form>
			        <!-- <div style="font-size:20px;">
					   <button id="btnAdd"  type="button"> Load</button>class="btn btn-success"
					</div> -->
				</div><br>

			</div>
		</div>
		

	</div>
		
	
	<div id="mapDiv" data-dojo-type="dijit/layout/ContentPane"
		data-dojo-props="region:'center'" style="background-color: #87CEFA; padding: 3px;"> 
		<div style="position:fixed;z-index:998;border:none;display:inline;float:left;background:white;font-size:12px;width:100%">
			<button style="background:transparent;border:none;"><img alt="" src="img/icon/note01.png" >区域切换</button>
			<button style="background:transparent;border:none;"><img alt="" src="img/icon/note02.png" >漫游</button>
		<!-- 	<button style="background:transparent;border:none;"><img alt="" src="img/icon/n07.png" >卷帘<img alt="" src="img/icon/14.gif" ></button> -->
	
			<button id="btnSelect" type="button" style="background:transparent;border:none;"><img alt="" src="img/icon/note03.png" >选要素</button>
			<button id="drawtool" style="background:transparent;border:none;"><img alt="" src="img/icon/n04.png" >绘制<img alt="" src="img/icon/14.gif" ></button>
		    <div id="drawtoolDiv" style="position:absolute;left:200px;border:1px solid #999;border-radius:4px; display:none;z-index:9999;background:white;">                 
	        	<button data-dojo-type="dijit/form/Button" id="drawPOINT">点</button>
	        	<button data-dojo-type="dijit/form/Button" id="drawPOLYLINE">折线</button>
	        	<button data-dojo-type="dijit/form/Button" id="drawPOLYGON">多边形</button>
	        	<button data-dojo-type="dijit/form/Button" id="drawRECTANGLE">矩形</button>
			    <input id="drawtoolclose" type="button" style="right:0px;border:none;background:transparent;" value="×"></input>
	        </div>
			<button id="InputXY" style="border:none;background:transparent;"><img alt="" src="img/icon/n06.png" >坐标串<img alt="" src="img/icon/14.gif" ></button>
			<div id="InputXYDiv" style="width:300px;height:80px;position:absolute;left:200px;border:1px solid #999;border-radius:4px; display:none;z-index:9999;background:white;">
				<div style="text-align:center;width:300px;height:40px;position:absolute;margin-top:12px;">
				对角点1坐标 X:<input type="text" class="InputXYbox" id="PntX1" onkeyup="checkNum(this,-180,180);"/>
						Y:<input type="text" class="InputXYbox" id="PntY1" onkeyup="checkNum(this,-90,90);"/></br>
				对角点2坐标 X:<input type="text" class="InputXYbox" id="PntX2" onkeyup="checkNum(this,-180,180);"/>
						Y:<input type="text" class="InputXYbox" id="PntY2" onkeyup="checkNum(this,-90,90);"/>
				</div></br>
				<input id="InputXYclose" type="button" style="right:5px;top:-2px;border:none;background:transparent;position: absolute;" value="×"></input></br>
				<input id="btnInputXY" type="button" value="确定" style="width:100px;right:20px;bottom:5px;border:none;background:#F5F5F5;position:absolute;"/>
			</div>
			<button id="btnImportShp" style="border:none;background:transparent;">导入shp</button>
			<button id="btnClearDraw" type="button" style="background:transparent;border:none;"><img src="img/icon/n08.png" >清除</button>		
			
			<!-- <button id="btnquylistResize" >113</button> -->
			<div id="quylistDiv" style="position:absolute;right:370px;top:33px;border:1px solid #999;border-radius:4px; display:block;z-index:9999;background:white;">
			<!-- 	<button class="btn" >快视图</button> -->
				<div id="toolbar" style="display:none;" > <!-- class="padding border-bottom"> -->
			      <!-- <ul class="search" style="padding-left:10px;">      </ul> -->
			        <li style="padding-left:20px;">
			        	<!-- <button id="showoverviewId" class="btn"  href="javascript:void(0)"> 快视图</button> -->
			        		<button id="showoverviewId" type="button" class="btn btn-default" title="显示快视图" href="javascript:void(0)">
									<span id="sp" class="glyphicon glyphicon glyphicon-eye-close"></span>
							</button>
							<button id="downloadId" type="button" class="btn btn-default" title="下载" href="javascript:void(0)">
									<span id="sp-dl" class="glyphicon glyphicon glyphicon-download-alt"></span>
							</button>
							<button id="exportshpId" type="button" class="btn btn-default" title="导出SHP" href="javascript:void(0)">
									<span id="sp-dl" class="glyphicon glyphicon glyphicon-export"></span>
							</button>
							
			        </li>        
			    </div>
				<table id="resulist"></table>
			</div>
			<!-- <input id="btntest" type="button" value="test"> -->
			<!-- <input id="btnClearDraw" type="button" value="清除"> -->
        </div>
        <input type="button" id="btnquylist" style="width:70px;border:none;background:#87CEFA;position:absolute;right:30px;top:20px;z-index:999" value="关闭列表"/>
	</div>

	<div data-dojo-type="dijit/layout/ContentPane"
		data-dojo-props="region:'bottom'"
		style="background-color: #2153A3; height: 30px;color:white;text-align:center;">
		<a>国家测绘地理信息局卫星测绘应用中心 版权所有</a>
	</div>
	
	<!-- <div id="mapDiv" style="width:800px;height:500px"></div> -->
	<script type="text/javascript">
	$("#btnquylist").click(function(){
			if($("#quylistDiv").css("display")=="none"){
				$("#quylistDiv").css({display:"block"});
				$("#btnquylist").val("关闭列表");
				$("#toolbar").css({display:"block"});
				
			}else{
				$("#quylistDiv").css({display:"none"});
				$("#btnquylist").val("打开列表");
				$("#toolbar").css({display:"none"});
			}		
		});
	
		document.getElementById("drawtool").onclick=function(){
			document.getElementById("drawtoolDiv").style.display="block";
			clearMouse();
		};
		document.getElementById("drawtoolclose").onclick=function(){
			document.getElementById("drawtoolDiv").style.display="none";
		};
		document.getElementById("InputXY").onclick=function(){
			document.getElementById("InputXYDiv").style.display="block";
			clearMouse();
		};
		document.getElementById("InputXYclose").onclick=function(){
			document.getElementById("InputXYDiv").style.display="none";
		};
		function clearMouse()
		{
			if(handle!=null) handle.remove();
		};
		function checkNum(obj,minValue,maxValue)
		{
			//去掉非数字，只保留数字和. -
			 obj.value = obj.value.replace(/[^\-\d.]/g,"");
	        //必须保证第一个为数字而不是.
	        obj.value = obj.value.replace(/^\./g,"");
/* 	        //保证只有出现一个.而没有多个.
	        obj.value = obj.value.replace(/\.{2,}/g,"."); */
 	        //保证.只出现一次，而不能出现两次以上
	        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	        //保证-只出现一次，而不能出现两次以上
	        obj.value = obj.value.replace("-","$#$").replace(/\-/g,"").replace("$#$","-");
	        //检查最大最小值
	        if(obj.value<minValue||obj.value>maxValue)
	        {
	        	alert('输入数据超过范围,请输入('+minValue+','+maxValue+')之间的数');
	        	obj.value='';
	        }
		};

	</script>
	
	
</body>

</html>
