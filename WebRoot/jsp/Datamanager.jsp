<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>数据管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<script src="./lib/jquery-3.2.0.js"></script>
	<script src="./lib/jquery.js"></script>
	<script src="./lib/bootstrap/js/bootstrap.js"></script>	
	<script src="./lib/bootstrap-table/bootstrap-table.js"></script>	
	<script src="./lib/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>
	<script src="./lib/bootstrap-select/js/bootstrap-select.js"></script>

	<script src="./lib/jquery-ui-1.12.1/jquery-ui.min.js"></script>
	<link rel="StyleSheet" type="text/css" href="./lib/jquery-ui-1.12.1/jquery-ui.min.css" />
	
	<link rel="StyleSheet" type="text/css" href="css/home.css" />
	

	<script type="text/javascript" src="./lib/My97DatePicker/WdatePicker.js"></script>
	
	<link rel="stylesheet" type="text/css" href="css/sidebar.css" /> 
	<link rel="stylesheet" href="./lib/bootstrap/css/bootstrap.css" />
	<link rel="stylesheet" href="./lib/bootstrap-table/bootstrap-table.css" />
	<link rel="stylesheet" href="./lib/bootstrap-select/css/bootstrap-select.css">
	<link rel="stylesheet" href="css/sidebar.css" type="text/css"/>  	
	<link rel="stylesheet" href="css/pintuer.css">
    <link rel="stylesheet" href="css/admin.css">
    <link rel="stylesheet" href="css/font-awesome.css">
    
	<script type="text/javascript" src="js/logininfo.js"></script>
	<style type="text/css">		
			.leftcol {width:100px;text-align:right; padding-right:0px;}
			.rightcol { width:200px;text-align:left; padding-right:0px;}
		</style> 
  </head>
  
  <body>
  

      <!--  <div class="panel-head"><strong class="icon-reorder">数据管理</strong></div> -->
       <div id="toolbar">
	    	<li style="padding-left:10px;"> 
	    <!-- 	<a href="./jsp/useradd.html" class="button border-main icon-plus-square-o"> 添加用户</a> -->
	    	<a href="javascript:void(0)" class="button border-red icon-trash-o" onclick="delSelectdata()">数据删除</a>
	    	</li>
      </div>
      <div style="float:left;height:500px;display:inline" >
          <table id="MetaDataTable" ></table>
      </div>
      <div  style="float:left;;height:500px;display:inline">
                              查询条件：
        <form id="qureyform" >
	    <table >
			<tr height="10%">
				<td class="leftcol">
					采集日期：
				</td>
				<td class="rightcol">
					<input id="acquiredate1" name="acquiredate1" class="Wdate" type="text" style="width:150px;font-size:13px" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'2012-01-01 00:00:00',maxDate:'#F{$dp.$D(\'acquiredate2\')||\'new Date()\'}'})"/>
								  	至 <input id="acquiredate2" name="acquiredate2" class="Wdate" type="text" style="width:150px;font-size:13px" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'acquiredate1\')}',maxDate:'%y-%M-%d'})"/> 
				</td>
			</tr>
			<tr height="10%">
				<td class="leftcol">
					归档日期：
				</td>
				<td class="rightcol">
					<input id="archivedate1" name="archivedate1" class="Wdate" type="text" style="width:150px;font-size:13px" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'2012-01-01 00:00:00',maxDate:'#F{$dp.$D(\'archivedate2\')||\'new Date()\'}'})"/>
								  	至 <input id="archivedate2" name="archivedate2" class="Wdate" type="text" style="width:150px;font-size:13px" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'archivedate1\')}',maxDate:'%y-%M-%d'})"/> 
				</td>
			</tr>
			<tr height="10%">
				<td class="leftcol">
					卫星传感器
				</td>
				<td class="rightcol">
				</td>
			</tr>
			<tr height="10%">
				<td class="leftcol">
					资源三号01星
				</td>
				<td class="rightcol">
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
				</td>
			</tr>
			<tr height="10%">
				<td class="leftcol">
					资源三号02星
				</td>
				<td class="rightcol">
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
				</td>
			</tr>
			<tr height="10%">
				<td class="leftcol">
					高分一号
				</td>
				<td class="rightcol">
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
			<tr height="10%">
				<td class="leftcol">
					高分二号
				</td>
				<td class="rightcol">
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
			<tr height="10%">
				<td class="leftcol">
					影像云量：
				</td>
				<td class="rightcol">
					<div style="float:left;padding-right:15px;"><label id="couldmin" >0</label></div>
					<div style="padding-top:8px;width:50%;float:left;"><div id="slider" style=""></div></div>
					<div style="float:left;padding-left:10px;"><label id="couldmax">100</label></div>
					<input type="hidden" id="minCloud" name="minCloud" >
					<input type="hidden" id="maxCloud" name="maxCloud" >
					<script>
						$( "#slider" ).slider({
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
				<td class="leftcol">
					其他条件：
				</td>
				<td class="rightcol">
					轨道号：   <input type="text" name="orbitid" style="width: 60px;">
				</td>
			</tr >
			<tr>
				<td class="leftcol">												
				</td>
			    <td class="rightcol">
				   PATH：&nbsp;&nbsp; <input type="text" name="scenerow" style="width: 60px;">  
				</td>
			</tr>
			<tr>
				<td class="leftcol">												
				</td>
			    <td class="rightcol">
					ROW：&nbsp;&nbsp; <input type="text" name="scenepath" style="width:60px;">
				</td>
			</tr>
			<tr height="10%">
				<td class="leftcol">												
				</td>
				<td class="rightcol">	
					产品号：<input type="text" name="dataid"  style="width: 60px; ">								
				</td>
			</tr>
			<tr>
				<td style="text-align:right; padding-right:0px;">
					<button type="submit" name="clear"  class="btn btn-primary btn-sm">清  空</button>
				</td>
				<td  style="text-align:center; padding-right:0px;">
					<button id="querybtn" type="button" class="btn btn-primary btn-sm">查询	</button>
				</td>
			</tr>
		</table>
	 </form>
    </div>

     

  </body>
  <script type="text/javascript">
       $(document).ready(function () {
       		     if($('#MetaDataTable').bootstrapTable!=null)
			    	$('#MetaDataTable').bootstrapTable('destroy');		
				$('#MetaDataTable').bootstrapTable('removeAll');
				$('#MetaDataTable').bootstrapTable({
					method: 'get',
					cache: false,
					height: '600',
					toolbar: '#toolbar',
					striped: true,
					sidePagination:"client",
					//pagination: true,
					pageSize: 20,
					pageList: [20, 50, 100, 500, 1000],
					search: true,
					showColumns: true,
					showRefresh: true,
					showToggle: true,
					showExport: true,
					clickToSelect: true,
					columns: [{field:"select",title:"全选",checkbox:true,width:50,align:"center",valign:"middle",sortable:"true"},
			    	{field:"satellite",title:"卫星",align:"center",valign:"middle",sortable:"true"},
			    	{field:"sensor",title:"传感器",align:"center",valign:"middle",sortable:"true"},
			    	{field:"cloudPercent",title:"云量",align:"center",valign:"middle",sortable:"true"},
					{field:"acquisitionTime",title:"拍摄时间",align:"center",valign:"middle",sortable:"true"},	
					{field:"orbitID",title:"轨道号",align:"center",valign:"middle",sortable:"true"},
					{field:"scenePath",title:"PATH",align:"center",valign:"middle",sortable:"true"},
					{field:"sceneRow",title:"ROW",align:"center",valign:"middle",sortable:"true"}, 
					{field:"ArchiveTime",title:"归档时间",align:"center",valign:"middle",sortable:"true"},
					{field:"action",title:"操作",align:"center",valign:"middle",formatter:actionformatter,
						events: {
						      'click .edit': function (e, value, row, index) {
						       ModifyData(row,index);
						      },
						      'click .remove': function (e, value, row, index) {
						        DeleteData(row,index);
						      }
					      } 
					  }				
					],
					data:"",
		            formatNoMatches: function(){
		            	return '无符合条件的记录';
		            },		            
		            onClickRow:function (row){
		            	//行点击事件
		       //       DeleteData(row);
		            }
				});

    });
  		function delSelectdata(){
          	var objSelec=$('#MetaDataTable').bootstrapTable('getSelections');
			if(objSelec==null)	return false;
			if(!confirm("确定要删除数据吗？")) return;
			var strtmp=JSON.stringify(objSelec);			
	        deldata(strtmp,"tb_sc_product");
	        
			 var ids = $.map($('#MetaDataTable').bootstrapTable('getSelections'), function (row) {
                return row.id;
            });
            $('#MetaDataTable').bootstrapTable('remove', {
                field: 'id',
                values: ids
            });
			
		}
		$("#querybtn").click(function(){
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
					
											
					setQuylist(obj);

				}
			});
		});
		function setQuylist(data){			
				var features=data.features;
				var obj=new Array();
				for(i in features){
					var tmp= features[i].properties.acquisitionTime;
					tmp=tmp.substring(0,10);
					features[i].properties.acquisitionTime=tmp;
					obj.push(features[i].properties);
				}
				if($('#MetaDataTable').bootstrapTable!=null)
					$('#MetaDataTable').bootstrapTable('destroy');		
				$('#MetaDataTable').bootstrapTable('removeAll');
				$('#MetaDataTable').bootstrapTable({
					method: 'get',
					cache: false,
					height: '600',
					toolbar: '#toolbar',
					striped: true,
					sidePagination:"client",
					//pagination: true,
					pageSize: 20,
					pageList: [20, 50, 100, 500, 1000],
					search: true,
					showColumns: true,
					showRefresh: true,
					showToggle: true,
					showExport: true,
					clickToSelect: true,
					showFooter:true,
                    columns: [{field:"select",title:"全选",checkbox:true,width:50,align:"center",valign:"middle",sortable:"true"},
			    	{field:"satellite",title:"卫星",align:"center",valign:"middle",sortable:"true"},
			    	{field:"sensor",title:"传感器",align:"center",valign:"middle",sortable:"true"},
			    	{field:"cloudPercent",title:"云量",align:"center",valign:"middle",sortable:"true"},
					{field:"acquisitionTime",title:"拍摄时间",align:"center",valign:"middle",sortable:"true"},	
					{field:"orbitID",title:"轨道号",align:"center",valign:"middle",sortable:"true"},
					{field:"scenePath",title:"PATH",align:"center",valign:"middle",sortable:"true"},
					{field:"sceneRow",title:"ROW",align:"center",valign:"middle",sortable:"true"}, 
					{field:"ArchiveTime",title:"归档时间",align:"center",valign:"middle",sortable:"true"},
					{field:"action",title:"操作",align:"center",valign:"middle",formatter:actionformatter,
						events: {
						      'click .edit': function (e, value, row, index) {
						       ModifyData(row,index);
						      },
						      'click .remove': function (e, value, row, index) {
						        DeleteData(row,index);
						      }
					      } 
					  }				
					],
					data:obj,
		            formatNoMatches: function(){
		            	return '无符合条件的记录';
		            },		            
		            onClickRow:function (row){
		            	//行点击事件
		       //       DeleteData(row);
		            }
				});
			/* 	$("#toolbar").css({display:"block"});	 */
				$(window).resize(function () {
					$('#MetaDataTable').bootstrapTable('resetView');
				});	
			}
			
 			function actionformatter(value,row,index){
				    var val = JSON.stringify(row);
					var resu = 
	/* 					+'<button class="btn btn-default"  onclick="DeleteData(\''+JSON.stringify(val)+'\')";><span class="glyphicon glyphicon-trash"></span></button>'
						+'<button class="btn btn-default"  onclick="ModifyData(\''+JSON.stringify(val)+'\')";><span class="glyphicon glyphicon-edit"></span></button>'
	 */				 '<a class="edit" href="javascript:void(0)" title="编辑">' 
	                 + '<i class="glyphicon glyphicon-edit"></i>' 
	                +'</a>  '
	                +'<a class="remove" href="javascript:void(0)" title="删除">'
	                 +'<i class="glyphicon glyphicon-trash"></i>' 
	                +'</a>'
	 	            ;						
					return resu;
				};
				function deldata( obj, tablename)
				{
				   // var strtmp=JSON.stringify(obj);
					$.ajax({
						url:"./servlet/DataManager",
						type:"POST",
						data:{"objSelec":obj,"tablename":tablename},
						//dataType:"json",
						async:false,
						error:function(request){
							alert("Network Error 网络异常");
						},
						success:function(data){
						 // alert(data);
						  //var obj=eval("("+data+")");
						  var code=data.CODE;
						  if(code=="NOPIVELEDGE")
						  {
						    	alert('没有权限！');
						  }
						  else  if(code=="NOLOGIN")
						  {
						     alert('请先登录！');
						  }
						  else
						  {
					    	 alert(data);
						  }
						 
						
						}
					});	 
				};
			function DeleteData(row,index){				
				if(row==null)	return false;
				var rowstr=JSON.stringify(row);	
					
				if(!confirm("数据删除操作将会把数据从磁盘和数据库中清除,确定要删除数据吗？")) return;
				if(!confirm("数据删除操作将会把数据从磁盘和数据库中清除,确定要删除数据吗？")) return;
				rowstr='['+rowstr+']';
	            deldata(rowstr,"tb_sc_product");
			};
			function ModifyData(row,index){				
				if(row==null)	return false;
									
				var rowstr=JSON.stringify(row);	
				
				
			};
  </script>
</html>
