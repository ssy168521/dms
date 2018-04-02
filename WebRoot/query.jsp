<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page isELIgnored="true" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String rootpath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort();
	
com.sasmac.util.AppConfUtil util= com.sasmac.util.AppConfUtil.getInstance();
util.SetAppconFile("appconf.xml");

String TDTUrl=util.getProperty( "TDTURL");
String TDTLabelUrl=util.getProperty( "TDTLabelUrl");
String VectBaseMapURL=util.getProperty("VectBaseMapURL");

String username="";
 if(session.getAttribute("username")!=null)
 {
      username=session.getAttribute("username").toString();
 };
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>可视化查询</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<!-- 	<script src="./lib/jquery-3.2.0.js"></script> -->
	<script src="./lib/jquery.js"></script>
	<script src="./lib/bootstrap/js/bootstrap.js"></script>	
	<script src="./lib/bootstrap-table/bootstrap-table.js"></script>	
	<script src="./lib/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>
	<script src="./lib/bootstrap-select/js/bootstrap-select.js"></script>

	<script src="./lib/jquery-ui-1.12.1/jquery-ui.min.js"></script>
	<link rel="StyleSheet" type="text/css" href="./lib/jquery-ui-1.12.1/jquery-ui.min.css" />
	
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
	<script src="./lib/vendor/terraformer-wkt-parser/terraformer-wkt-parser.min.js"></script>
	
	<!-- 创建目录树 -->
	<script type="text/javascript" src="./js/catalog.js" charset="utf-8"></script>
	<script type="text/javascript" src="./lib/My97DatePicker/WdatePicker.js"></script>
	
	<link rel="stylesheet" type="text/css" href="css/sidebar.css" /> 
	<link rel="stylesheet" href="./lib/bootstrap/css/bootstrap.css" />
	<link rel="stylesheet" href="./lib/bootstrap-table/bootstrap-table.css" />
	<link rel="stylesheet" href="./lib/bootstrap-select/css/bootstrap-select.css">
	
	<script type="text/javascript" src="js/logininfo.js"></script>
	<script type="text/javascript" src="js/WktTransform.js"></script>
	<script type="text/javascript" src="js/WKTUtil.js"></script>
	<!-- <script type="text/javascript" src="js/ajaxfileupload.js"></script> -->
	<script type="text/javascript" src="lib/HashMap.js"></script>
	<style>
	html,body,#main {
		width: 100%;
		height: 100%;
		margin: 0;
	}
	.headerbtn{background:url(img/page/navbg.jpg);}
	.headerbtn:hover{background:url(img/page/navbgover.jpg);}

	.Pagination {
	    position: relative !important;
	    display: inline-block  !important;
	    top: 0 !important;
	    bottom: 0 !important;
	    left: 0 !important;
	    right: 0 !important;
    }
	</style>
	
	<script type="text/javascript">
    //控制是否加载overview;
    var bIsLoadoverview ; 
	var map, geoJsonLayer,toolbar,cantonlay,imglayer,handle,lightlay;//handle用来记录map的click事件;
	var imgs=new HashMap();
	require([ "dojo/parser", "esri/map", "esri/dijit/Scalebar",
		"esri/layers/ArcGISDynamicMapServiceLayer","esri/layers/WMSLayer",
		"esri/layers/TiledMapServiceLayer", "lib/fwVecLayer.js","lib/HashMap.js",
		"esri/toolbars/draw","esri/graphic","esri/tasks/FeatureSet",
		"esri/layers/MapImageLayer","esri/layers/MapImage",
		"esri/symbols/SimpleMarkerSymbol",
		"esri/symbols/SimpleLineSymbol",
		"esri/symbols/SimpleFillSymbol", 
		"esri/geometry/jsonUtils","esri/geometry/geometryEngine",		
		"dijit/registry","./lib/geojsonlayer.js", 
		"dojo/on", "dojo/dom","esri/tasks/IdentifyTask","esri/tasks/IdentifyParameters",
		"esri/InfoTemplate","esri/dijit/Popup","dojo/_base/array",
     	"esri/Color","esri/toolbars/navigation","dojo/dom-construct",
		"dijit/layout/BorderContainer", "dijit/layout/ContentPane",
		"dijit/layout/AccordionContainer", 
		"dojo/domReady!" ],
		function(parser, Map, Scalebar, ArcGISDynamicMapServiceLayer,WMSLayer,
				TiledMapServiceLayer,fwVecLayer,HashMap,Draw,Graphic,FeatureSet,MapImageLayer,MapImage,
				SimpleMarkerSymbol, SimpleLineSymbol, SimpleFillSymbol, geometryJsonUtils,geometryEngine,
				registry, GeoJsonLayer, on, dom,IdentifyTask, IdentifyParameters,InfoTemplate,Popup,
				arrayUtils, Color, Navigation,domConstruct) {
				parser.parse();
				
			var popup = new Popup({
	          fillSymbol: new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID,
	            new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID,
	              new Color([255, 0, 0]), 2), new Color([255, 255, 0, 0]))
	        }, domConstruct.create("div"));
			
			map = new Map("mapDiv", {
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
              
			var agsWmsUrl = "<%=TDTLabelUrl%>";
			var agsWmsLayer = new WMSLayer(agsWmsUrl ,{resourceInfo: resourceInfo}); 
			agsWmsLayer.setImageFormat("png");
			agsWmsLayer.setVisibleLayers([0]);
			map.addLayer(agsWmsLayer);
              
			var VectorBaseMapURL =  "<%=VectBaseMapURL%>";
        	map.addLayer(new ArcGISDynamicMapServiceLayer(VectorBaseMapURL ,{ opacity: 0.95 }));	
            
			var point = new esri.geometry.Point([105,33.53],new esri.SpatialReference({ wkid:4326 }));
			map.centerAndZoom(point,0.15);

			on(dom.byId("btnSelect"), "click",SelectGraphic);
			function SelectGraphic(){
			    navToolbar.deactivate();
				if(handle!=null) handle.remove();
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
				map.setMapCursor("url(img/icon/n03.png),auto");
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
	          
				var deferred = identifyTask.execute(identifyParams)
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

				// InfoWindow expects an array of features from each deferred object that you pass. 
				// If the response from the task execution above is not an array of features, 
				// then you need to add a callback like the one above to post-process the response and return an array of features.
				map.infoWindow.setFeatures([deferred]); 
				/* map.infoWindow.show(event.mapPoint);  */
	        }
	        
	        cantonlay=new esri.layers.GraphicsLayer({id:'cantonlay'}); //行政区
			map.addLayer(cantonlay);
	        
	        //快视图层
			imglayer=new MapImageLayer();
 	        map.addLayer(imglayer);
			
			selectlay=new esri.layers.GraphicsLayer({id:'selectlay'});
			map.addLayer(selectlay);
			
			lightlay=new esri.layers.GraphicsLayer({id:'lightlay'});
			map.addLayer(lightlay);
						
			
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
				map.setMapCursor("crosshair");
				navToolbar.deactivate();
			    if(handle!=null) handle.remove();
				toolbar=new Draw(map);
				toolbar.activate(Draw.POLYGON);
				map.hideZoomSlider();
				toolbar.on("draw-end", addToMap);
			});
			$("#drawRECTANGLE").click(function(){
				navToolbar.deactivate();
				map.setMapCursor("crosshair");
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
				map.setMapCursor("default");
				if(cantonlay!=null) cantonlay.clear();
				
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
								esri.symbol.SimpleLineSymbol.STYLE_SOLID,
								new dojo.Color([ 255, 0, 0 ]), 2), new dojo.Color([
								255, 255, 0, 0.05 ]));
					break;
				}
				var graphic = new Graphic(evt.geometry, symbol);
				//map.graphics.add(graphic);
				cantonlay.add(graphic);
				toolbar.finishDrawing();
				//var jsontemp=evt.geometry.toJson();
				//var jsonPoly=JSON.stringify(jsontemp);//将object类型的json转成string					
				var wktPoly=PolygonToWKT(evt.geometry);				
				document.getElementById("wktPoly").value=wktPoly;
			}
			
			//Polygon对象转化成WKT
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
					   if(data!="")		
					   {
						   	var obj= eval("("+data+")");
							/* if() */
							
							geoJsonLayer = new GeoJsonLayer({
								data : obj,
								id :layid
							});
							// Zoom to layer
							geoJsonLayer.on("update-end", function(e) {
								map.setExtent(e.target.extent.expand(1.2));
							});
							map.addLayer(geoJsonLayer,15);
												
						    setQuylist(obj,layid);
					   }
					   else
					   {
					      setQuylist("",layid);
					   }	
					   
					   $('#navtabid a[href="#panel-quyresult"]').tab('show');

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
					height: '600',
					toolbar: '#toolbar',
					striped: true,
					sidePagination:"client",
					showPaginationSwitch:true,
					pagination: true,
					pageSize: 20,
					pageList: [20, 50, 100, 500, 1000],
					maintainSelected:true,
					//search: true,
					showColumns: true,
					showRefresh: true,
					//showToggle: true,
					showExport: true,
					//group-by:true,
					//group-by-field:["orbitID","sceneRow"],
					//clickToSelect: true,
					columns: [{field:"select",title:"全选",checkbox:true,width:50,align:"center",valign:"middle",sortable:"true"},
					          {field:"action",title:"操作",align:"center",valign:"middle",formatter:actionformatter,events: {
							      'click .showoverviewclass': function (e, value, row, index) {
							       showove(e, value,row,index);
							      },
							      'click .matchsearchclass': function (e, value, row, index) {
							        imageMatch(e, value,row,index);
							      }
						      } },
					
						{field:"satellite",title:"卫星",align:"center",valign:"middle",sortable:"true"},
						{field:"sensor",title:"传感器",align:"center",valign:"middle",sortable:"true"},					
						{field:"cloudPercent",title:"云量",align:"center",valign:"middle",sortable:"true"},
						{field:"acquisitionTime",title:"拍摄时间",align:"center",valign:"middle",sortable:"true"},	
						{field:"orbitID",title:"轨道号",align:"center",valign:"middle",sortable:"true",visible:false},	
						{field:"scenePath",title:"PATH",align:"center",valign:"middle",sortable:"true",visible:false},
						{field:"sceneRow",title:"ROW",align:"center",valign:"middle",sortable:"true",visible:false}			
					],
					data:obj,
					rowStyle: function (row, index) {
			                //这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
			                var strclass = "";
			                /* if (row.ORDER_STATUS == "待排产") {
			                    strclass = 'success';//还有一个active
			                }
			                else if (row.ORDER_STATUS == "已删除") {
			                    strclass = 'danger';
			                }
			                else {
			                    return {};
			                } */
			                return { classes: strclass }
			            },
		            formatNoMatches: function(){
		            	return '无符合条件的记录';
		            },		            
		            onDblClickRow:function (row){
		            	//行点击事件
		            	lightClick(layid,row.dataid);
		            },
		            onCheck:function(row){
		            	graphSelect(row.dataid);
		            },
		            onUncheck:function(row){
		            	graphUnselect(row.dataid);
		            }  
				});
								
				$("#resuCount").text(obj.length);
				$("#resuinfo").css({display:"block"});
				$("#toolbar").css({display:"block"});	
				$(window).resize(function () {
					$('#resulist').bootstrapTable('resetView');
				});	
			}
			
			function actionformatter(value,row,index){
			
			var val = JSON.stringify(row);
					var resu = 
					 '<a id="sp1-'+row.dataid+'" class="showoverviewclass " href="javascript:void(0)" title="显示快视图">' 
	                 + '<i class="glyphicon glyphicon-eye-close"></i>' 
	                +'</a>  '
	                +'<a id="sp2-'+row.dataid+'" class="matchsearchclass" href="javascript:void(0)" title="配对查询">'
	                 +'<i class="glyphicon glyphicon-screenshot"></i>' 
	                +'</a>'
	 	            ;						
					return resu;
				
			}
			
         
			function showove(e, value,row,index){				
				if(row==null)	return false;
				var spid='sp1-'+row.dataid;


				var v=document.getElementById(spid).firstChild.className;	
				//document.getElementById(spid).getAttribute("i");
				
				/* var v=$('"#sp"+row.FID').attr("class");	 */	
				var rowstr=JSON.stringify(row);	
				var strtmp="["+rowstr+"]";
				$.ajax({
					url:"./servlet/Loadoverview",
					type:"POST",
					data:{"objSelec":strtmp},
					async:false,
					error:function(request){
						alert("Network Error 网络异常");
					},
					success:function(data){
						/* if(imglayer !=null )
						{
							imglayer.removeAllImages();
						} */
						var obj=eval("("+data+")");
						for (var i=0;i<obj.ext.length;i++)
						{							
							if(v=="glyphicon glyphicon-eye-close")
							{
							   var img=imgs.get(obj.dataid[i]);
							   if(img ==null)
							   {
									   img=new esri.layers.MapImage({
										'extent':obj.ext[i],
										'href':obj.path[i]});									
										imglayer.addImage(img);
										imgs.put(obj.dataid[i],img);
							   }						
								v="glyphicon glyphicon-eye-open";
								document.getElementById(spid).firstChild.className=v;
							}
						    else
							{
							 var img=imgs.get(obj.dataid[i]);
							   if(img !=null)
							   {
							      imglayer.removeImage(img);
							      imgs.remove(obj.dataid[i]);
							   }

							    v="glyphicon glyphicon-eye-close";
							    document.getElementById(spid).firstChild.className=v;
							}
						}
					}
				});	
			};
			
			function changeTableSelect(){
				var data = document.getElementById("resulist").rows;
				//var data2 = $('#resulist').DataTable().rows().data();
				$(data).each(function(index,item){
					//if(data2[index].STAFF_ID==staffId){
						$(item).addClass('selected');
					/* }else{
						$(item).removeClass('selected');
					} */
				});
			}
			
			function graphSelect(dataid){
				if(geoJsonLayer==null){
					return;
				}				
				var graphics=geoJsonLayer.graphics;
				if(selectlay!=null){
					//selectlay.clear();
					//map.removeLayer(lightlay);
				}
				for(var i=0;i<graphics.length;i++){
					var tmpgra=graphics[i];
					var graDataid=tmpgra.attributes.dataid;
					if(graDataid==dataid){
						map.setExtent(tmpgra.geometry.getExtent().expand(1.5));
						var symbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID,
									    new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID,
									    new Color([0,0,255]), 1), new Color([98,194,204,0])
									  );
						var graAdd=new Graphic(tmpgra.geometry,symbol,tmpgra.attributes);
						selectlay.add(graAdd);
					}
				}				
			}
			
			function graphUnselect(dataid){
				if(selectlay==null){
					return;
				}
				var graphics=selectlay.graphics;
				for(var i=0;i<graphics.length;i++){
					var tmpgra=graphics[i];
					var graDataid=tmpgra.attributes.dataid;
					if(graDataid==dataid){
						selectlay.remove(tmpgra);
					}
				}			
			}
			
			$("#btnSeleGraph").click(function(){
				if(handle!=null) handle.remove();
				if(geoJsonLayer!=null){
					handle=geoJsonLayer.on("click", SeleGraphic);
				}
				
				map.setMapCursor("url(img/icon/note03.png),auto");
			});
			
			function SeleGraphic(event){
			    
				var pnt= event.mapPoint;
				if(geoJsonLayer==null){
					return;
				}				
				var graphics=geoJsonLayer.graphics;
				if(lightlay!=null){
					lightlay.clear();
					//map.removeLayer(lightlay);
				}
				//var tab=document.getElementById("resulist");
				//var resutable =document.getElementById("resulist").rows;
				var resudata= $('#resulist').bootstrapTable('getData');
				//var namebox=$("input[name^='btSelectItem']");//获取chkbox
				/* for(var index=0;index<resudata.length;index++){
					var item=$(resutable)[index+1];
					$(item).removeClass('selected');
					namebox[index].checked=false;
				} */
				//$('#resulist').bootstrapTable('uncheckAll');
				
				/* changeTableSelect(); */
				/* var data= */
				
				for(var i=0;i<graphics.length;i++){
					var tmpgra=graphics[i];
					if(tmpgra.geometry.contains(pnt)){
						
						var graDataid=tmpgra.attributes.dataid;
						var size=resudata.length;
						
						$("#resulist").bootstrapTable("checkBy", {field:"dataid", values:[resudata[i].dataid]});
						
/* 						for(var index=0;index<size;index++){
							//var item=$(resutable)[index+1];
							//console.log(index);
							//var ttt=$(item);
							
							var resuDataid=resudata[index].dataid;						
							if(graDataid==resuDataid){
								$('#resulist').bootstrapTable('check',index);
								break;
							}
						} */
					}					
				}
			}
			
			$("#btnDetail").click(function(){
			    navToolbar.deactivate();
				if(handle!=null) handle.remove();
				if(geoJsonLayer!=null){
					handle=geoJsonLayer.on("click", ShowDetail);
				}	
				map.setMapCursor("url(img/icon/Info.png),auto");
				
			});	
			
			//var infoTemplate = new InfoTemplate("数据详情", "${*}");
			var infoTemplate = new InfoTemplate("数据详情", "产品号: ${dataid}<br>文件名: ${FileName}<br>文件路径: ${FilePath}<br>Path: ${scenePath}<br>Row: ${sceneRow}<br>轨道号: ${orbitID}<br>卫星名: ${satellite}<br>传感器: ${sensor}<br>采集日期: ${acquisitionTime}<br>产品级别: ${productLevel}<br>云量: ${cloudPercent}");
			var detaillay=new esri.layers.GraphicsLayer({id:'detaillay',infoTemplate:infoTemplate});
			map.addLayer(detaillay,12);
			function ShowDetail(evt){
				var pnt= evt.mapPoint;
				if(geoJsonLayer==null){
					return;
				}
				var graphics=geoJsonLayer.graphics;
				if(detaillay!=null){
					detaillay.clear();
					//map.removeLayer(detaillay);
				}
				
				for(var i=0;i<graphics.length;i++){
					var tmpgra=graphics[i];
					if(tmpgra.geometry.contains(pnt)){
						map.setExtent(tmpgra.geometry.getExtent().expand(1.5));
						var symbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID,
									    new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID,
									    new Color([255,0,0]), 0.1), new Color([98,194,204,0])
									  );
						var graAdd=new Graphic(tmpgra.geometry,symbol,tmpgra.attributes);
						detaillay.add(graAdd);
					}
				}
			}
			
			var navToolbar=new Navigation(map);
			$("#btnPan").click(function(){
				/* if(handle!=null) handle.remove();
				toolbar.deactivate(); */
				navToolbar.activate(Navigation.PAN);
				map.setMapCursor("pointer");
			});	
			
			$("#btnZoomOut").click(function(){
				/* if(handle!=null) handle.remove();
				toolbar.deactivate(); */
				navToolbar.activate(Navigation.ZOOM_OUT);
				map.setMapCursor("url(img/icon/ZoomOut.png),auto");
			});
			
			$("#btnZoomIn").click(function(){
				/* if(handle!=null) handle.remove();
				toolbar.deactivate(); */
				navToolbar.activate(Navigation.ZOOM_IN);
				map.setMapCursor("url(img/icon/ZoomIn.png),auto");//zoom-in.cur
			});
			 	
			$("#btnFullExtent").click(function(){
				//navToolbar.zoomToFullExtent();
				
				map.centerAt(point);
				map.setScale(20000000);
				map.setMapCursor("default");
				//map.setZoom(4);
				//map.centerAndZoom(point, 1);
			});

			function lightClick(layid,graDataid){
				if(lightlay!=null){
					lightlay.clear();
					//map.removeLayer(lightlay);
				}
				var layer=map.getLayer(layid);
				var laygraphics=layer.graphics;
				for(var i=0;i<laygraphics.length;i++){
					var tmpgra=laygraphics[i];
					if(tmpgra.attributes.dataid==graDataid) {
						map.setExtent(tmpgra.geometry.getExtent().expand(1.5));
						var symbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID,
									    new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID,
									    new Color([255,0,0]), 2), new Color([98,194,204,0])
									  );
						var graAdd=new Graphic(tmpgra.geometry,symbol);
						lightlay.add(graAdd);
					}
				}
/* 				map.addLayer(lightlay,8); */
				//lightlay.redraw();	
			}
			
			function wktToGeom(wkt){
				// convert the wkt to internal representation
				var primitive = Terraformer.WKT.parse(wkt);
			    // convert the geojson object to a arcgis json representation
			    var arcgis = Terraformer.ArcGIS.convert(primitive);			
			    // create a new geometry object from json
			    var geometry = geometryJsonUtils.fromJson(arcgis);
				return geometry;
			}
				
			window.selectCanton=function(graWkt){
				if(cantonlay!=null){
					cantonlay.clear();
					//map.removeLayer(cantonlay);
				}
				
				var symbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID,
							    new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID,
							    new Color([255,0,0]), 2), new Color([98,194,204,0])
							  );
				var geoms = new Array();
			
				for(var i=0;i<graWkt.length;i++){
					var geometry=wktToGeom(graWkt[i]);
					geoms[i]=geometry;					
					var graAdd=new Graphic(geometry,symbol);
					cantonlay.add(graAdd);
				}
				
				var geomsresu= geometryEngine.union(geoms);
				var geomwkt=PolygonToWKT(geomsresu);
				document.getElementById("wktPoly").value=geomwkt;
				map.setExtent(geomsresu.getExtent().expand(1.5));				
				//lightlay.redraw();
			};
			
			window.ShowWktGraphics=function(graWkt){
				if(cantonlay!=null){
					cantonlay.clear();
					//map.removeLayer(cantonlay);
				}
				
				var symbol = new SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID,
							    new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID,
							    new Color([255,0,0]), 2), new Color([98,194,204,0])
							  );

				var geometry=wktToGeom(graWkt);
				map.setExtent(geometry.getExtent().expand(1.5));				
				var graAdd=new Graphic(geometry,symbol);
				cantonlay.add(graAdd);
								
				//map.addLayer(cantonlay,9);
				
				document.getElementById("wktPoly").value=graWkt;
				//lightlay.redraw();
			};
						
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
			

			document.getElementById("btnInputXY").onclick=function(){
				clearDraw();//清除地图上绘制的图形
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
								esri.symbol.SimpleLineSymbol.STYLE_SOLID,
								new dojo.Color([ 255, 0, 0 ]), 2), new dojo.Color([255, 255, 0, 0.05 ]));		
				var graphic = new esri.Graphic(poly, PolygonSymbol);
				cantonlay.add(graphic);
				map.setExtent(poly.getExtent().expand(1.5));
				var wktPoly=PolygonToWKT(graphic.geometry);
				document.getElementById("wktPoly").value=wktPoly;
			};
			
			on(dom.byId("btnClearDraw"), "click",clearDraw);
			//清除绘制的图形
			function clearDraw(){
				if(handle!=null) handle.remove();
				if(cantonlay!=null) cantonlay.clear();
				if(selectlay!=null) selectlay.clear();
				if(detaillay!=null) detaillay.clear();
				if(geoJsonLayer!=null) geoJsonLayer.clear();
				if(lightlay!=null) lightlay.clear();
				if(imglayer!=null) imglayer.removeAllImages();
				map.setMapCursor("default");
				map.infoWindow.hide();
				document.getElementById("wktPoly").value=null;		
				$('#resulist').bootstrapTable('removeAll');
				$("#resuinfo").css({display:"none"});
			}
			
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
						alert("已加入下载队列！请在任务管理中查看");
					}
				});		
			
			});

        	$("#DeleteDataId").click(function(){        
	        	var objSelec=$('#resulist').bootstrapTable('getSelections');
				if(objSelec==null)	return false;
				
				 var ids = $.map($('#resulist').bootstrapTable('getSelections'), function (row) {
	                return row.id;
	            });
	            $('#resulist').bootstrapTable('remove', {
	                field: 'id',
	                values: ids
	            });
				
			    var tablename= $("#tbname").val();
				var strtmp=JSON.stringify(objSelec);
				$.ajax({
					url:"./servlet/DataManager",
					type:"POST",
					data:{"objSelec":strtmp,"tablename":tablename},
					//dataType:"json",
					async:false,
					error:function(request){
						alert("Network Error 网络异常");
					},
					success:function(data){
					  alert(data);
                          // window.location.href="./servlet/DataManager";
					}
				});		
        	});
        	
			$("#exportshpId").click(function(){			
				var objSelec=$('#resulist').bootstrapTable('getSelections');
				if(objSelec==null)	return false;
				var strtmp=JSON.stringify(objSelec);
				var tablename= $("#tbname").val();
				$.ajax({
					url:"./servlet/ExportSHP",
					type:"POST",
					data:{"objSelec":strtmp,"tablename":tablename},
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
				if(v=="glyphicon glyphicon-eye-close")
				{
					imglayer.setVisibility(true);
					v="glyphicon glyphicon-eye-open";
					$("#sp").attr("class",v);
				}
			    else
				{
				    imglayer.setVisibility(false);
				    v="glyphicon glyphicon-eye-close";
				    $("#sp").attr("class",v);
				    return;
				}
				
				
				//if(bIsLoadoverview==false) return;
				
				var objSelec=$('#resulist').bootstrapTable('getSelections');
				if(objSelec==null)	return false;		
				 var ids = $.map($('#resulist').bootstrapTable('getSelections'), function (row) {
	                return row.dataid;
	            });
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
							
							imgs.clear();
						}
						var obj=eval("("+data+")");
						for (var i=0;i<obj.ext.length;i++)
						{
						     var img=imgs.get(obj.dataid[i]);
						    if(img ==null) 
						    {
							    img=new esri.layers.MapImage({
								'extent':obj.ext[i],
								'href':obj.path[i]});
								imglayer.addImage(img);
								imgs.put(obj.dataid[i],img);
						    }
						}
				     	bIsLoadoverview=false;
					}
				});	
			}); 
			
	});
</script>

  </head>
  
  <body>  	
    <div class="container-fluid" style="padding:0px;height:100%"> <!-- style="width:100%" -->
		<div class="row-fluid clearfix">
			<div class="col-md-12 column" style="padding:0px">
				<!-- <h3>
					h3. 这是一套可视化布局系统.
				</h3> -->
				<div style="z-index: 0;background-image: url(img/page/bannerbg.jpg);background-size:auto auto;background-repeat:repeat">
					<a href="query.jsp"><img src="img/page/logo.png" style="padding-left:5px;background:none;"></a>
					<div style="background:none;">
				     <a href="default.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;">首页</button></a>
				     <a href="query.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-6px;">可视化查询</button></a>
				     <a href="Archive.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-6px;">数据归档</button></a>
				     <a href="Taskmanager.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-6px;">任务管理</button></a>
		   			 <a href="sysmanager.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-6px;">系统管理</button></a><!-- <a href="login.jsp"><button class="headerbtn" style="border:none;width:100px;height:36px;color:white;margin-left:-7px;">登录</button></a>   -->
			         <!-- <button style="border:none;width:100px;onmouseover="this.style.background:url(img/navbg.jpg);" onmouseout="this.style.background=url(img/navbgover.jpg);"color:white;">首页</button>  -->   
				     </div>
					 <div style="z-index:999;float:right; display:inline;position:absolute;top:80px;right:10px;"><!--display:inline-block;  -->
						<table>
							<tr>
							<td><a href="login.jsp"><button id="login-id" style="background:transparent;border:none;font-size:16px;display:none" ><font color="#FFFFFF">登录  </font></button></a></td>
							<td width=10px></td>
							<td><a href="register.jsp"><button id="register-id" style="background:transparent;border:none;font-size:16px;display:none" ><font color="#FFFFFF">注册</font></button></a></td>
							<td><a id="user-id" > <font color="#FFFFFF" ><%=username%>,欢迎您  </font></a></td> <!--  style="font-size:16px;display:none"  -->
							<td width=10px></td>
							<td><button id="logout-id" style="background:transparent;border:none;font-size:16px;display:none;cursor:pointer;" onclick="logout()" ><font color="#FFFFFF">退出</font></button></td>
							</tr>
						</table>
					</div>
				</div>
							
				
				<div class="row-fluid clearfix" style="height:100%;padding:-5px">
					<div class="col-md-8 column"  style="padding:10px;height:100%;">
						<!-- <button type="button" style="background:transparent;border:none;"><img alt="" src="img/icon/note03.png" >选要素</button> -->
						
						<div class="btn-group" style="position:absolute;right:20px;top:20px;z-index:1000" >
							<button id="btnSelect" class="btn btn-default"  title="点选行政区"><img alt="" src="img/icon/n03.png"></button>
							<button id="btnPan" class="btn btn-default"  title="漫游"><img alt="" src="img/icon/note02.png"></button>
							<button id="btnZoomIn" class="btn btn-default"  title="拉框放大"><img alt="" src="img/icon/ZoomIn.png"></button>
							<button id="btnZoomOut" class="btn btn-default"  title="拉框缩小"><img alt="" src="img/icon/ZoomOut.png"></button>
							<button id="btnFullExtent" class="btn btn-default"  title="全图"><img alt="" src="img/icon/n02.png"></button>
							<button id="btnSeleGraph" class="btn btn-default"  title="选择要素"><img alt="" src="img/icon/note03.png"></button>
							<!-- <button id="drawPOINT" class="btn btn-default"  title="漫游">点</button>
				        	<button id="drawPOLYLINE" class="btn btn-default"  title="漫游">折线</button> -->
				        	<!-- <button id="drawPOLYGON" class="btn btn-default" style="height:30px" title="绘制多边形">多边形</button>
				        	<button id="drawRECTANGLE" class="btn btn-default" style="height:30px"  title="绘制矩形">矩形</button> -->
				        	
				        	<div class="btn-group">
								<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
									<img alt="" src="img/icon/n04.png"><span class="caret"></span>
								</button>
								<ul class="dropdown-menu" style="min-width:100px">
									<li width=100px style="width:100px"><a id="drawPOLYGON" style="width:100px" href="javascript:void(0)">绘制多边形</a></li>
									<li width=100px style="width:100px"><a id="drawRECTANGLE" style="width:100px" href="javascript:void(0)">绘制矩形</a></li>
									<!-- <li><a id="drawRECTANGLE" href="javascript:void(0)">绘制矩形</a></li> -->
									<!-- <li role="separator" class="divider"></li> -->
									<!-- <li><a href="#">Separated link</a></li> -->
								</ul>
							</div>
							<button id="btnClearDraw" class="btn btn-default"  title="清除"><img alt="" src="img/icon/n08.png"></button>
							<button id="btnDetail" class="btn btn-default" style="height:30px" title="详情"><img alt="" src="img/icon/n05.png"></button>
						</div>
						
						<div id="mapDiv" style="height:100%; background-color: #AAC5EE; padding: 3px;">
						</div>
					</div>
                    <div class="col-md-4 column" style="height:50%;">
						   <div class="tabbable" id="tabs-724236">
							 <ul class="nav nav-tabs">
								<li class="active" style="width:25%">
									 <a href="#panel-region" data-toggle="tab">行政区</a>
								</li>
								<li style="width:25%">
									 <a href="#panel-importshp" data-toggle="tab">导入Shp</a>
								</li>
								<li style="width:25%">
									 <a href="#panel-132828" data-toggle="tab">坐标串</a>
								</li>
								<li style="width:25%">
									 <a href="#panel-importTxt" data-toggle="tab">导入TXT</a>
								</li>
							</ul>
							<div class="tab-content">
								<div class="tab-pane active" id="panel-region" height=30%>
									<table width=100% height=40% style="padding-top:15px">
										<tr height=30%>
											<td width=40% style="text-align:right;padding-right:15px">
												省/市/自治区 
											</td>
											<td width=60%>
												<select id="province" class="selectpicker" data-done-button="true" onchange="selectprovince(this);" style=" width:95px;" multiple runat="server" ></select>
											</td>															
										</tr>
										<tr height=30%>
											<td width=40% style="text-align:right;padding-right:15px">
												地级市/区 
											</td>
											<td width=60%>
												<select id="city" class="selectpicker" data-done-button="true" onchange="selectcity(this);" style="width:95px;" multiple runat="server"></select>
											</td>															
										</tr>
										<tr height=30%>
											<td width=40% style="text-align:right;padding-right:15px">
												县
											</td>
											<td width=60%>
												<select id="county" class="selectpicker" data-done-button="true" onchange="selectcounty(this);" style="width:95px;" multiple runat="server"></select>
											</td>
										</tr>
									</table>
								</div>
								<div class="tab-pane" id="panel-importshp" height=40%>
									<!-- <input id="uploadSHPfile" type="file" size="45" accept="application/zip" name="uploadSHPfile" class="btn btn-default btn-sm">
									<button class="btn btn-default" onclick="ajaxFileUpload()">上传</button> -->
									<div style="text-align:center; padding-top:15px">
									<input id="uploadSHPfile" type="file" size="45" accept="application/zip" name="uploadSHPfile" style="display:none">
									<div class="input-append" >  
									    文件：
									    <input id="selectedfile" class="input-large" type="text" style="height:33px;">  
									    <a class="btn btn-default" style="margin-left:-5px;margin-top:-1px" onclick="$('input[id=uploadSHPfile]').click();">浏览</a>  
										<button class="btn btn-primary" onclick="ajaxFileUpload()" style="margin-left:26px;">上传</button>
									</div>
									
									<br>
									<p>上传zip格式的压缩文件，包含*.shp *.shx *.dbf文件</p>
									<p>地理坐标系：GCS_WGS_1984</p>
									</div>
									<script type="text/javascript">
									$('input[id=uploadSHPfile]').change(function() { 
										$('#selectedfile').val($(this).val());  
									});  
									</script>
								</div>
								<div class="tab-pane" id="panel-132828" height=40%>
	
									<div style="text-align:center;width:100%;height:40%;margin-top:12px;">
										<div class="form-group" >
											经度:<input type="text" id="PntX1" class="InputXYbox" style="text-align:center;width:25%;" onkeyup="checkNum(this,-180,180);"/>
											纬度:<input type="text" id="PntY1" class="InputXYbox" style="text-align:center;width:25%;" onkeyup="checkNum(this,-90,90);">	
										</div>
										<div class="form-group" style="margin-top:20px;">
											经度:<input type="text" id="PntX2" class="InputXYbox" style="text-align:center;width: 25%;" onkeyup="checkNum(this,-180,180);"/>
											纬度:<input type="text" id="PntY2" class="InputXYbox" style="text-align:center;width:25%;" onkeyup="checkNum(this,-90,90);"/>
										</div>
								   </div>
<!-- 								   <div class="form-group" style="text-align:center;">
											<button id="btnClearXY" class="btn btn-default btn-sm col-lg-6" style="margin-left:150;margin-top:15;width:80px;">清  空</button>
											<button id="btnInputXY" class="btn btn-default btn-sm col-lg-6" style="margin-left:100;margin-top:15;width:80px;">确 定</button>
	
									</div> -->
								</div>
								
								<div class="tab-pane" id="panel-importTxt" height=40%>
									<div style="text-align:center; padding-top:15px">
										<input id="uploadtxt" type="file" size="45" accept="text/plain" name="uploadSHPfile" style="display:none">
										<div class="input-append" >  
										    文件：
										    <input id="selectedtxt" class="input-large" type="text" style="height:33px;">  
										    <a class="btn btn-default" style="margin-left:-5px;margin-top:-1px" onclick="$('input[id=uploadtxt]').click();">Browse</a>  
											<button class="btn btn-primary" onclick="txtUpload()" style="margin-left:26px;">上传</button>
										</div>
										<div class="input-append" style="margin-left:-100px;padding-top:10px">  
										    产品号：
										    <input id="importDataid" class="input-large" readonly="readonly" type="text" style="height:33px;width:227px">  
										</div>
										
										<br>
										<p>上传含有产品号的txt文件，不同产品号用逗号隔开</p>
										<p></p>
									</div>
									<script type="text/javascript">  
									$('input[id=uploadtxt]').change(function() { 
										$('#selectedtxt').val($(this).val());  
									});  
									</script>				
								</div>
							</div>		
						</div>		
						<div class="tabbable" id="tabs-237916" height=60%>
							<ul id="navtabid" class="nav nav-tabs">
								<li style="width:25%">
									 <a href="#panel-quyterm" data-toggle="tab">SC产品</a>
								</li>
								<li  style="width:25%">
									 <a href="#panel-quyterm" data-toggle="tab">分景DOM</a>
								</li>
								<li  style="width:25%">
									 <a href="#panel-frame" data-toggle="tab">分幅DOM</a>
								</li>
								<li style="width:25%">
									 <a href="#panel-quyresult" data-toggle="tab">结果列表</a>
								</li>
							</ul>
							<div class="tab-content">
								<div class="tab-pane active" id="panel-quyterm" style="height:100%">
									<!-- 第二级选项卡 -->
									<div class="row-fluid clearfix" style="height:100%">
										<div class="col-md-12 column">
									<form id="qureyform">
										<input type="hidden" id="wktPoly" name="wktPoly" >
										<input type="hidden" id="txtDataids" name="txtDataids" >
										<input type="hidden" id="ProductType" name="ProductType" >
										<table style="width:100%;height:350px">
											<tr height="10%">
											<td width="30%" style="text-align:right; padding-right:0px;">
												采集日期
											</td>
											<td width="70%" style="text-align:center; padding-right:0px;">
												<input id="acquiredate1" name="acquiredate1" class="Wdate" type="text" style="width:40%;font-size:13px" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'2012-01-01 00:00:00',maxDate:'#F{$dp.$D(\'acquiredate2\')||\'new Date()\'}'})"/>
					   							  	至 <input id="acquiredate2" name="acquiredate2" class="Wdate" type="text" style="width:40%;font-size:13px" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'acquiredate1\')}',maxDate:'%y-%M-%d'})"/> 
											</td>
											</tr>
											<tr height="10%">
											<td width="30%" style="text-align:right; padding-right:0px;">
												卫星传感器
											</td>
											<td width="70%" style="text-lign:center; padding-left:15px;">
											 <!-- <button id="AdanvceQueryID" class="btn btn-default btn-sm col-lg-6" onclick="advancequery()" style="margin-left:150;width:80px;" >高级查询</button> -->
											 <input type="button" id="AdanvceQueryID" class="btn btn-default btn-sm col-lg-6" onclick="advancequery()"  value="高级查询"/>
											 </td>
											  
											</tr>
											<tr class="generalquerycls" height="10%">
											<td width="30%" style="text-align:right; padding-right:0px;">
												ZY3-01
											</td>
											<td width="70%" style="text-align:left; padding-left:15px;">
											   <div  id="generalQuery-divid">
												<label title="2.1米正视相机">
													<input id="zy301nad" name="zy301sensor" value="NAD" type="checkbox">
													NAD
												</label>
												<label title="5.8米多光谱相机">
													<input id="zy301mux" name="zy301sensor" value="MUX" type="checkbox">
													MUX
												</label>
												<label title="3.5米前视相机">
													<input id="zy301fwd" name="zy301sensor" value="FWD" type="checkbox">
													FWD
												</label>
												<label title="3.5米后视相机">
													<input id="zy301bwd" name="zy301sensor" value="BWD" type="checkbox">
													BWD
												</label>
												</div>
												
											</td>
											</tr>
											<tr class="generalquerycls" height="10%">
											<td width="30%" style="text-align:right; padding-right:0px;">
												ZY3-02
											</td>
											
											<td width="70%" style="text-align:left; padding-left:15px;">
											  <div id="zy302sensor-divid">
												<label title="2.1米正视相机">
													<input id="zy302nad" name="zy302sensor" value="NAD" type="checkbox">
													NAD
												</label>
												<label title="5.8米多光谱相机">
													<input id="zy302mux" name="zy302sensor" value="MUX" type="checkbox">
													MUX
												</label>
												<label title="3.5米前视相机">
													<input id="zy302fwd" name="zy302sensor" value="FWD" type="checkbox">
													FWD
												</label>
												<label title="3.5米后视相机">
													<input id="zy302bwd" name="zy302sensor" value="BWD" type="checkbox">
													BWD
												</label>
												</div>
											</td>
											</tr>
											<tr class="generalquerycls" height="10%">
											<td width="30%" style="text-align:right; padding-right:0px;">
												GF1
											</td>
											<td width="70%" style="text-align:left; padding-left:15px;">
												<label title="2米全色和8米多光谱">
													<input id="gf1PMS1" name="gf1sensor" value="PMS1" type="checkbox">
													PMS1
												</label>
												<label title="XXXXX">
													<input id="gf1PMS2" name="gf1sensor" value="PMS2" type="checkbox">
													PMS2
												</label>
											</td>
											</tr>
											<tr class="generalquerycls" height="10%">
											<td width="30%" style="text-align:right; padding-right:0px;">
												GF2
											</td>
											<td width="70%" style="text-align:left; padding-left:15px;">
												<label title="2米全色和8米多光谱">
													<input id="gf2PMS1" name="gf2sensor" value="PMS1" type="checkbox">
													PMS1
												</label>
												<label title="XXXXX">
													<input id="gf2PMS2" name="gf2sensor" value="PMS2" type="checkbox">
													PMS2
												</label>
											</td>
											</tr>
								
											<tr class="advancequerycls" height="10%">
											<td width="30%" style="text-align:right; padding-left:0px;">
												<label title="ZY3-1">
													<input id="zy3-1id" name="zy3_radio" value="ZY3-1" type="radio">
													ZY3-1
												</label>
												<label title="ZY3-2">
													<input id="zy3-2id" name="zy3_radio" value="ZY3-2" type="radio">
													ZY3-2
												</label>
											</td>
											<td width="70%" style="text-align:left; padding-left:15px;">
												<label title="正视多光谱配套">
													<input id="zy301nadmux" name="zy3_sensorradio" value="NADMUX" type="radio">
													NADMUX
												</label>
												<label title="三线阵">
													<input id="zy301SXZ" name="zy3_sensorradio" value="SXZ" type="radio">
													三线阵
												</label>
												<label title="四视">
													<input id="zy301SS" name="zy3_sensorradio" value="SS" type="radio">
													四视
												</label>
											</td>
											</tr>
											<tr height="10%">
											<td width="30%" style="text-align:right; padding-right:0px;">
												云量
											</td>
											<td width="70%" style="text-align:center; padding-left:15px;">
												<div style="float:left;padding-right:15px;"><label id="couldmin" >0</label></div>
												<div style="padding-top:8px;width:60%;float:left;"><div id="slider" style=""></div></div>
												<div style="float:left;padding-left:10px;"><label id="couldmax">100</label></div>
												<input type="hidden" id="minCloud" name="minCloud" >
												<input type="hidden" id="maxCloud" name="maxCloud" >
												<script>
													$( "#slider").slider({
														range: true,
														values: [ 0,20 ],
														slide:function(event,ui){															
															$("#couldmin").text(ui.values[0]);
															$("#couldmax").text(ui.values[1]);
															$("#minCloud").val(ui.values[0]);
															$("#maxCloud").val(ui.values[1]);
														}
													});
													$("#couldmin").text($("#slider").slider("values",0));
													$("#couldmax").text($("#slider").slider("values",1));
													$("#minCloud").val($("#slider").slider("values",0));
													$("#maxCloud").val($("#slider").slider("values",1));
												</script>
											</td>
											</tr>
											<tr height="10%">
											<td width="30%" style="text-align:right; padding-right:0px;">
												其他条件：
											</td>
											<td width="70%" style="text-align:left; padding-left:15px;">
											</td>
											</tr >
											<tr height="10%">
											<td width="30%">												
											</td>
											<td width="70%">
												<div class="form-group">
												    <div> <span>轨道号</span><input type="text" id="orbitid" name="orbitid"  style="text-align:center;width:60%;"></div>
													<div> <span>产品号</span><input type="text" id="dataid"  name="dataid"   style="text-align:center;width:60%;"></div>
												</div>
											</td>
											</tr>
											<tr height="10%">
											<td width="30%">												
											</td>
											<td width="70%" >
												<div class="form-group" style="padding-left:15px;">
													 <div><span>PATH</span> <input type="text" id="scenerow"  name="scenerow"   style="text-align:center;width:60%;"/></div>  
													 <div><span>ROW</span>  <input type="text" id="scenepath" name="scenepath"  style="text-align:center;width:60%;"/></div>
												</div>
					
											</td>
											</tr>
										</table>
										
									</form>
										</div>
									</div>
									
								</div>
								<div class="tab-pane" id="panel-quyresult">
								    <div class="bs-bars pull-left">
										<div id="toolbar">
									      	<button id="showoverviewId" type="button" class="btn btn-default" title="显示快视图" href="javascript:void(0)">
											<span id="sp" class="glyphicon glyphicon-eye-close"></span>
											</button>
											<button id="downloadId" type="button" class="btn btn-default" title="下载" href="javascript:void(0)">
												<span id="sp-dl" class="glyphicon glyphicon-download-alt"></span>
											</button>
											<button id="exportshpId" type="button" class="btn btn-default" title="导出SHP" href="javascript:void(0)">
												<span id="sp-dl" class="glyphicon glyphicon-export"></span>
											</button>
										    <!-- 	<button id="DeleteDataId" type="button" class="btn btn-default" title="删除" href="javascript:void(0)">
												<span  class="glyphicon glyphicon-remove"></span>
											</button>
											 -->  
									    </div>
									</div>
									<table id="resulist" class="table table-striped table-hover table-condensed" style="height:100%;"></table>
									<!-- <p id="resuinfo" style="display:none;margin-top:40px">共查询到<label id="resuCount"></label>条记录</p> -->
								</div>
	                            <div class="tab-pane" id="panel-frame">
	                                <!--  <iframe src="query_frame.jsp" frameborder="0" scrolling="yes" width=100% height=80%></iframe> -->
	      	              		        <span>分幅号：</span> 
		                            <input id="fenfuid" class="input-large" readonly="readonly" type="text" style="height:33px;width:227px">  
	                            </div>
							</div>
							<table style="width:80%;bottom:30px;">
										<tr>
										<td width="50%" style="text-align:right; padding-right:0px;">
											<button id="resetQuy" class="btn btn-primary btn-sm" style="width:80px">重 置</button>
										</td>
										<td width="50%" style="text-align:right; padding-right:0px;">
											<button id="testsub" class="btn btn-primary btn-sm" style="width:80px">数据查询</button>
										</td>
										</tr>
									</table>
						</div>						
					</div>
				</div>				
				<div style="position:fixed;bottom:0px;margin-bottom:0px;background-color: #2153A3; height: 30px;width:100%;color:white;text-align:center;">
					<label>国家测绘地理信息局卫星测绘应用中心 版权所有</label>
				</div>				
			</div>			
		</div>
	</div>
	
	<script type="text/javascript">
	function advancequery()
		{
			var value=$("#AdanvceQueryID").val();
			if(value=="高级查询")
			{
			   var tab = document.getElementById("qureyform");
		       var y = tab.getElementsByClassName("generalquerycls");
				var i;
				for (i = 0; i < y.length; i++) {
				  y[i].style.display = "none";  
				  y[i].checked=false;
				}
				
				 y = tab.getElementsByClassName("advancequerycls");
				for (i = 0; i < y.length; i++) {
				  y[i].style.display = "table-row";  
				}
				
				var sensor=document.getElementsByName("zy301sensor");
				for (i = 0; i < sensor.length; i++) {
				  sensor[i].checked=false;  
				}
				sensor=document.getElementsByName("zy302sensor");
				for (i = 0; i < sensor.length; i++) {
				  sensor[i].checked=false;  
				}
				sensor=document.getElementsByName("gf1sensor");
				for (i = 0; i < sensor.length; i++) {
				  sensor[i].checked=false;  
				}
				sensor=document.getElementsByName("gf2sensor");
				for (i = 0; i < sensor.length; i++) {
				  sensor[i].checked=false;  
				}
				
				sensor=document.getElementsByName("zy3_sensorradio");			
			    sensor[0].checked=true;  

				$("#AdanvceQueryID").val("一般查询");
			}
			else
			{
			   var tab = document.getElementById("qureyform");
		       var y = tab.getElementsByClassName("generalquerycls");
				var i;
				for (i = 0; i < y.length; i++) {
				  y[i].style.display = "table-row";  
				}
				
				 y = tab.getElementsByClassName("advancequerycls");
				for (i = 0; i < y.length; i++) {
				  y[i].style.display = "none";  
				  y[i].checked=false;
				}
				
				var 	sensor=document.getElementsByName("zy3_radio");
				for (i = 0; i < sensor.length; i++) {
				  sensor[i].checked=false;  
				}
				sensor=document.getElementsByName("zy3_sensorradio");
				for (i = 0; i < sensor.length; i++) {
				  sensor[i].checked=false;  
				}
				$("#AdanvceQueryID").val("高级查询");
			}
			
		}
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
		
    	var ddlProvince = document.getElementById("province");
	    var ddlCity = document.getElementById("city");
	    var ddlCounty = document.getElementById("county");
	    
	    $(function(){
	    	var obj="<%=username%>";
			showusername(obj);
	    
		    var list1 = new Array;		    
		    list1[list1.length] = "北京市";
		    list1[list1.length] = "天津市";
 		    list1[list1.length] = "上海市";
 		    list1[list1.length] = "重庆市";
 		    list1[list1.length] = "辽宁省";
 		    list1[list1.length] = "吉林省";
		    list1[list1.length] = "黑龙江省";
		    list1[list1.length] = "河北省";
		    list1[list1.length] = "山西省";
		    list1[list1.length] = "内蒙古自治区";
		    list1[list1.length] = "江苏省";
		    list1[list1.length] = "浙江省";
		    list1[list1.length] = "安徽省";
		    list1[list1.length] = "山东省";
		    list1[list1.length] = "河南省";
		    list1[list1.length] = "福建省";
		    list1[list1.length] = "江西省";
		    list1[list1.length] = "湖北省";
		    list1[list1.length] = "湖南省";
		    list1[list1.length] = "广东省";
		    list1[list1.length] = "广西壮族自治区";
		    list1[list1.length] = "海南省";
		    list1[list1.length] = "四川省";
		    list1[list1.length] = "贵州省";
		    list1[list1.length] = "云南省";
		    list1[list1.length] = "西藏自治区";
		    list1[list1.length] = "陕西省";
		    list1[list1.length] = "甘肃省";
		    list1[list1.length] = "青海省";
		    list1[list1.length] = "宁夏回族自治区";
		    list1[list1.length] = "新疆维吾尔自治区";
		    list1[list1.length] = "香港特别行政区";
		    list1[list1.length] = "澳门特别行政区";
		    list1[list1.length] = "台湾省";
	    	
		    for(var i =0;i<list1.length; i++)
		    {
		        var option = document.createElement("option");
		        option.appendChild(document.createTextNode(list1[i]));
		        option.value = list1[i];
		        ddlProvince.appendChild(option);
		    }
		});
			    
	    function indexof(obj,value)
	    {
	        var k=0;
	        for(;k<obj.length;k++)
	        {
	            if(obj[k] == value)
	            return k;
	        }
	        return k;
	    }
	    
	    function selectprovince(obj) {	    	    	
	        ddlCity.options.length = 0;//clear
	        ddlCounty.options.length = 0;

	        var seleOpt=[];
	        for(var i=0;i<obj.length;i++){
	        	if(obj.options[i].selected){
	        		seleOpt.push(obj[i].value);
	        	}
	        }
	        
	        //多选框联动
	        var seleProvince=JSON.stringify(seleOpt);
	        var datata=eval("("+"{\"seleProvince[]\":"+seleProvince+"}"+")");
	        $.ajax({
				url:"./servlet/RegionSelect",
				type:"POST",
				data:datata,
				async:false,
				error:function(request){
					alert("Network Error 网络异常");
				},
				success:function(data){
					var objdata= eval("("+data+")");
					var RegNames=objdata.RegionNames;
					for(var j=0;j<seleOpt.length;j++){
						var optgroup = document.createElement("optgroup");
			            optgroup.appendChild(document.createTextNode(seleOpt[j]));
			            optgroup.label = seleOpt[j];
			            ddlCity.appendChild(optgroup);
			            
			            var cityOpts=RegNames[seleOpt[j]];
						for(var i =0;i<cityOpts.length; i++)
				        {
				        	var option = document.createElement("option");
				            option.appendChild(document.createTextNode(cityOpts[i]));
				            option.value = cityOpts[i];
				            ddlCity.appendChild(option);
				        }					
					}
					$('.selectpicker').selectpicker('refresh');
					
					var RegWkts=objdata.RegionWkts;
					selectCanton(RegWkts);
				}
			});
			      
	    }
	    
	    function selectcity(obj){
	    	ddlCounty.options.length = 0;//clear

	        var seleOpt=[];
	        for(var i=0;i<obj.length;i++){
	        	if(obj.options[i].selected){
	        		seleOpt.push(obj[i].value);
	        	}
	        }
	        
	        //多选框联动
	        var seleCity=JSON.stringify(seleOpt);
	        var datata=eval("("+"{\"seleCity[]\":"+seleCity+"}"+")");
	        $.ajax({
				url:"./servlet/RegionSelect",
				type:"POST",
				data:datata,
				async:false,
				error:function(request){
					alert("Network Error 网络异常");
				},
				success:function(data){
					var objdata= eval("("+data+")");
					var RegNames=objdata.RegionNames;
					for(var j=0;j<seleOpt.length;j++){
						var optgroup = document.createElement("optgroup");
			            optgroup.appendChild(document.createTextNode(seleOpt[j]));
			            optgroup.label = seleOpt[j];
			            ddlCounty.appendChild(optgroup);
			            
			            var countyOpts=RegNames[seleOpt[j]];
						for(var i =0;i<countyOpts.length; i++)
				        {
				        	var option = document.createElement("option");
				            option.appendChild(document.createTextNode(countyOpts[i]));
				            option.value = countyOpts[i];
				            ddlCounty.appendChild(option);
				        }					
					}
					$('.selectpicker').selectpicker('refresh');
					
					var RegWkts=objdata.RegionWkts;
					selectCanton(RegWkts);
					/* 
					var ttmp=objdata[0];
					var ts=seleOpt[0];
					var sds=objdata[ts];
					alert(data); */
				}
			});	    
	    }
	    
	    function selectcounty(obj){
	    	var seleOpt=[];
	        for(var i=0;i<obj.length;i++){
	        	if(obj.options[i].selected){
	        		seleOpt.push(obj[i].value);
	        	}
	        }	        
	        //多选框联动
	        var seleCounty=JSON.stringify(seleOpt);
	        var datata=eval("("+"{\"seleCounty[]\":"+seleCounty+"}"+")");
	        $.ajax({
				url:"./servlet/RegionSelect",
				type:"POST",
				data:datata,
				async:false,
				error:function(request){
					alert("Network Error 网络异常");
				},
				success:function(data){
					var objdata= eval("("+data+")");
					
					var RegWkts=objdata.RegionWkts;
					selectCanton(RegWkts);
				}
	    	});
	    }
	    
	    //查询条件重置
	    $("#resetQuy").click(function(){
	    	$("#date1").val("");
	    	$("#date2").val("");
	    	//$("[name='zy301sensor']").attr("checked",false);
	    	$("[type='checkbox']").attr("checked",false);
	    	$("#orbitid").val("");
	    	$("#productid").val("");
	    	$("#scenepath").val("");
	    	$("#scenerow").val("");
	    	$("#wktPoly").val("");
	    	
	    });
	    $('#navtabid a').click(function (e) {
		  e.preventDefault();
		  $(this).tab('show');
		  var activeTab = $(e.target).text();
		  document.getElementById("ProductType").value=activeTab;  
		});
	</script>
                                                         
<script type="text/javascript">
	
	function imageMatch(e, value,row,index){
		//$('#resulist').bootstrapTable('uncheckAll');
		var resudata= $('#resulist').bootstrapTable('getData');
		var itemRow=row.sceneRow;
		var itemOrbitid=row.orbitID;
		var size=resudata.length;
		var num=0;
		for(var idx=0;idx<size;idx++){
			var sceneRow=resudata[idx].sceneRow;
			var orbitID=resudata[idx].orbitID;
			if(itemRow==sceneRow&&itemOrbitid==orbitID){				
				$('#resulist').bootstrapTable('check',idx);
				num=num+1;
			}
		}
		if(num<=1)
		{
		  alert("没有匹配的数据");
		}
	}
                                                                               
	function ajaxFileUpload() {
		$.ajaxFileUpload({
			url : 'servlet/UploadSHP',
			secureuri : false,
			fileElementId : 'uploadSHPfile',
			dataType : 'json',
			//data : {username : $("#username").val()},
			success : function(data, status) {
				//$('#viewImg').attr('src',data.picUrl);
				//alert(data.shpWKT);
				ShowWktGraphics(data.shpWKT);
				
			},
			error : function(data, status, e) {
				alert('上传出错');
			}
		});

		return false;
	}
	
	function txtUpload() {
		$.ajaxFileUpload({
			url : 'servlet/UploadTXT',
			secureuri : false,
			fileElementId : 'uploadtxt',
			dataType : 'json',
			//data : {username : $("#username").val()},
			success : function(data, status) {
				//alert('上传成功'+data.resu);
				$('#importDataid').val(data.resu);
				$('#txtDataids').val(data.resu);				
			},
			error : function(data, status, e) {
				alert('上传出错');
			}
		});

		return false;
	}
	
	
	
 </script>                                                                                                         
	
	
  </body>
   
</html>
