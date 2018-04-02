<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'drawPoly.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		

  </head>
  
  <body>
    This is my JSP page. <br>
    <script type="text/javascript">
		require([ "esri/geometry/Polygon", "esri/map",
				"esri/layers/GraphicsLayer", "esri/graphic" ]);
		function setPolygon() {
			var poly = new esri.geometry.Polygon([ [ -50, -50 ], [ -50, 50 ],
					[ 50, 50 ], [ 50, -50 ], [ -50, -50 ] ]);

			PolygonSymbol = new esri.symbol.SimpleFillSymbol(
					esri.symbol.SimpleFillSymbol.STYLE_SOLID,
					new esri.symbol.SimpleLineSymbol(
							esri.symbol.SimpleLineSymbol.STYLE_DASHDOT,
							new dojo.Color([ 255, 0, 0 ]), 1), new dojo.Color([
							255, 255, 0, 0.25 ]));

			var graphic = new esri.Graphic(poly, PolygonSymbol);

			graLayer = new esri.layers.GraphicsLayer();
			graLayer.add(graphic);
			map.addLayer(graLayer);
		}
	</script>

	<input type="button" value="绘制多边形" onClick="setPolygon();">
    
  </body>
</html>
