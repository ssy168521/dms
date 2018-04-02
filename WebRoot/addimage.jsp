<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'addimage.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="http://172.17.3.145:8080/arcgisAPI317/library/3.17/3.17/dijit/themes/tundra/tundra.css"/>
    <link rel="stylesheet" type="text/css" href="http://172.17.3.145:8080/arcgisAPI317/library/3.17/3.17/esri/css/esri.css"/>

    <script src="http://172.17.3.145:8080/arcgisAPI317/library/3.17/3.17/init.js"></script>
	
	<script>
	require([
        "esri/map",        
        "esri/layers/ArcGISDynamicMapServiceLayer",
        "esri/layers/ArcGISImageServiceLayer",
        "esri/layers/MapImageLayer",
        "esri/layers/MapImage",
        "esri/layers/MosaicRule",
        "dojo/on", "dojo/dom",
        "dojo/domReady!"
      ], function (
        Map, ArcGISDynamicMapServiceLayer,ArcGISImageServiceLayer,MapImageLayer,MapImage,MosaicRule,on,dom
      ) {
      
        map = new Map("mapDiv", {
          logo:false,
          center: [123.275, 32.573],
          zoom: 18
        });
        
        var parcelsURL = "http://172.17.3.145:6080/arcgis/rest/services/BaseMap/MapServer";
        //var parcelsURL = "http://localhost:6080/arcgis/rest/services/zy3bm/ImageServer";
        map.addLayer(new ArcGISDynamicMapServiceLayer(parcelsURL,
        { opacity: 0.95 }
        ));
        
        //var ImageLayer=new ArcGISImageServiceLayer("http://localhost:6080/arcgis/rest/services/zy3bm/ImageServer");
        //var ImageLayer=new ArcGISImageServiceLayer("http://localhost:6080/arcgis/rest/services/jiangsu/ImageServer");
        var ImageLayer=new ArcGISImageServiceLayer("http://172.17.3.145:6080/arcgis/rest/services/SC100R8NP/ImageServer");
        on(dom.byId("btnQueryImage"), "click",function (){
        	//ImageLayer.setDefinitionExpression("OBJECTID>=30",true);
       		
        	//"CenterX <117"
        	
           /*  result= ImageLayer.getDefinitionExpression();
            var res=ImageLayer.loaded;
            res=ImageLayer.visible;
            
            var g=ImageLayer.getVisibleRasters();
            ImageLayer.show();
        	ImageLayer.refresh(); */
        	
        	var mr=new MosaicRule();
        	//mr.where="OBJECTID=90 or OBJECTID<89";
        	mr.where="OBJECTID<30";
        	ImageLayer.setMosaicRule(mr); 
        	
        	/* map.on("update-end", function(e) {
						map.setExtent(e.target.extent.expand(1.2));
					}); */
        	map.setExtent(ImageLayer.initialExtent);
        	map.addLayer(ImageLayer);
        	
        });
          
        on(dom.byId("btnAddImage"), "click",function (){
			var mil=new MapImageLayer();
			map.addLayer(mil);
			var mi=new esri.layers.MapImage({
			'extent':{'xmin':115.695,'ymin':34.342,'xmax':116.388,'ymax':34.952,'spatialReference':{'wkid':4326}},
			'href':'./img/test.png' });/* './img/test.png'*/   /* 'E:\\TestData\\test1233.png' */
			mil.addImage(mi);
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
		
		if(<%=session.getAttribute("result")%>==true){
			addpng();
			<%session.setAttribute("result",false);%>
		}
						
		<%
 		String[] strPNGfiles=null;
		strPNGfiles=(String[])request.getAttribute("strPNGfiles");
		String[] strPNGExtents=(String[])request.getAttribute("strPNGExtents");		
		String strtest=(String)request.getAttribute("strtest"); 
		
/* 		String[] strPNGfiles=null;
		strPNGfiles=(String[])session.getAttribute("strPNGfiles");
		String[] strPNGExtents=(String[])request.getAttribute("strPNGExtents");
		String strtest=(String)request.getAttribute("strtest"); */
				
		System.out.println(strtest);
		//System.out.println(strPNGfiles);
		
		
	 	if(strPNGfiles!=null)
	 	{
	 		System.out.println(strPNGfiles.length+"   "+strPNGExtents.length);
		 	for(int i=0;i<strPNGfiles.length;i++)
			{
				String strFile=strPNGfiles[i];
				String strExtent=strPNGExtents[i];
		%>
				/* 注意此处一定要加双引号，否则无法识别 */
				var strfileurl="<%=strFile%>";
				var strfileextent=<%=strExtent%>;
				
				var mil=new MapImageLayer();
				map.addLayer(mil);				
				var mi=new esri.layers.MapImage({
				'extent':strfileextent,
				'href':strfileurl});
				mil.addImage(mi);
				
				
		<%			
			}	 	
	 	}
		%> 
		 var strfileurl="<%=strtest%>";
       }); 
	</script>
  </head>
  
  <body>
    This is my JSP page. <br>
    <div id="mapDiv" style="width:1960px;height:980px"></div>
    <td><%=session.getAttribute("strtest")%></td>
    <input type="button" id="btnAddImage" value="添加影像">
    <input type="button" id="btnQueryImage" value="查询影像">
    <form action="servlet/PNGShow" method="post">
    	<input type="submit" id="btnAddImages" value="批量添加影像">
    </form>
    <form action="servlet/Mosaic" method="post">
    	<input type="submit" id="btnMosaic" value="镶嵌展示">
    </form>
  </body>
</html>
