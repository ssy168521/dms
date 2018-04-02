<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" href="css/pintuer.css">
    <link rel="stylesheet" href="css/admin.css">
    <link rel="stylesheet" href="css/font-awesome.css">
     <script src="./lib/jquery.js"></script>
   	<script src="./lib/bootstrap/js/bootstrap.js"></script>	
	<script src="./lib/bootstrap-table/bootstrap-table.js"></script>
	<script src="./lib/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>
	<!-- <script src="./lib/bootstrap-table/bootstrap-table-export.js"></script> -->

    <script src="js/pintuer.js"></script>
	<link rel="stylesheet" href="./lib/bootstrap/css/bootstrap.css" />
	<link rel="stylesheet" href="./lib/bootstrap-table/bootstrap-table.css" />
  </head> 
  <body>
  <!--  <form method="post" action="" id="listform">  -->
  <div class="panel admin-panel">
  <strong class="icon-reorder">归档配置列表</strong> <a href="" style="float:right; display:none;">添加字段</a>
    <div id="toolbar"> <!-- class="padding border-bottom"> -->
      <!-- <ul class="search" style="padding-left:10px;">      </ul> -->
        <li  style="padding-left:20px;">
			<a class="button border-main icon-plus-square-o" href="<%=basePath%>project/configure/dialog/formDig.jsp?Operator=add">增加 </a> 
        	<a class="button border-red icon-trash-o" href="javascript:void(0)" onclick="delSelectArchiveConf()"> 删除</a>
        </li>        
    </div>
    <table id="ArchiveSetTable"></table>
    <form id="Archiveform" method="post">
    	<input type="hidden" id="Archiveconfid" name="Archiveconfid"/>
    	<input type="hidden" id="Archiveconfid1" name="Archiveconfid1"/>
    </form>
<script>
	$(function () {
		$.ajax({
			url:"servlet/PageLoad",
			type:"POST",
			data:eval("("+"{requestSou:\"ArchiveConfPage\"}"+")"),
			async:false,
			error:function(request){
				alert("Network Error 网络异常");
			},
			success:function(data){
				var obj= eval("("+data+")");
				$('#ArchiveSetTable').bootstrapTable({
					method: 'get',
					cache: false,
					height: 708,
					toolbar: '#toolbar',
					striped: true,
					pagination: true,
					pageSize: 10,
					pageNumber: 1,
					pageList: [10, 20, 50, 100, 200, 500],
					showColumns: true,
					showRefresh: true,
					showExport: true,
					exportTypes: ['csv','txt','xml'],
					search: false,
					
					//clickToSelect: true,
					columns: [{field:"select",title:"全选",checkbox:true,width:20,align:"center",valign:"middle"},
					{field:"id",title:"ID",align:"center",valign:"middle",sortable:"true"},
					{field:"satellite",title:"卫星名称",align:"center",valign:"middle",sortable:"true"},
					{field:"archivePath",title:"归档源路径",align:"center",valign:"middle",sortable:"true"},
					{field:"destPath",title:"归档目标路径",align:"center",valign:"middle",sortable:"true"},
					{field:"storageRule",title:"存储规则",align:"center",valign:"middle",sortable:"true"},
					{field:"checkRule",title:"核验规则",align:"center",valign:"middle",sortable:"true"},
					{field:"archiveStart",title:"归档开始时间",align:"center",valign:"middle",sortable:"true"},
					{field:"archiveEnd",title:"归档结束时间",align:"center",valign:"middle",sortable:"true"},
					{field:"action",title:"操作",align:"center",valign:"middle",formatter:"actionFormatter",event:"actionEvents"}],
					data:obj,
					onPageChange: function (size, number) {
						//$("#pageSizeInput").val(size);
						//$("#pageNumberInput").val(number);							
						//var form = $('#tableForm');
						//form.action= '${base}/showReport';
						//form.submit();
	                },
					//onSort: function (name, order) {
	                // },
					//formatShowingRows: function (pageFrom, pageTo, totalRows) {
					//	return '';
	                // },
					//formatRecordsPerPage: function () {
					//	return '';
	              	//  },
	                formatNoMatches: function(){
	                	return '无符合条件的记录';
	                }
				});	
			}
		});
		$(window).resize(function () {
			$('#userTable').bootstrapTable('resetView');
		});
	});
	
	function actionFormatter(value,row,index){
		var resu= '<div class="button-group">'+
					/* '<button class="button border-main icon-book" onclick=viewRole('+JSON.stringify(row)+');></button>'+ */
					'<button class="button border-main icon-edit" onclick=modifyArchiveSet("'+row.id+'","'+row.satellite+'","'+row.archivePath+'");></button>'+
					'<button class="button border-red icon-trash-o" name="btnDelArchiveset" onclick=delArchiveConf("'+row.id+'","'+row.satellite+'","'+row.archivePath+'");></button>'+
					'</div>';
		return resu;
	}
	
	function delSelectArchiveConf(){
		var objSelec=$('#ArchiveSetTable').bootstrapTable('getSelections');
		if(objSelec==null){
			alert("请选择您要删除的内容!");
			return false;
		}else{
			var strtmp=JSON.stringify(objSelec);
			if(confirm("您确定要删除吗?")){
				$.ajax({
					url:"./servlet/ArchiveSetManage",
					type:"POST",
					data:{"objSelec":strtmp,"OperateType":"BatchDel"},
					//dataType:"json",
					async:false,
					error:function(request){
						alert(request);
						alert("Network Error 网络异常");
					},
					success:function(data){		
				 		alert("删除成功！");		
					}
				});		
			}
		}
	}
	function delArchiveConf(id,satellite,archivePath){
			if(confirm("您确定要删除吗?")){
				$.ajax({
					url:"./servlet/ArchiveSetManage",
					type:"POST",
					data:{"id":id,"satellite":satellite,"archivePath":archivePath,"OperateType":"Del"},
					//dataType:"json",
					async:false,
					error:function(request){
						alert(request);
						alert("Network Error 网络异常");
					},
					success:function(data){
							alert("删除成功！");
					}
				});		
			}
		//}
	}
			
	function viewArchiveSet(row){
	
	}
	
	function modifyArchiveSet(id,satellite,archivePath){
	     window.location.href='project/configure/dialog/formDig.jsp?Operator=modify&id='+id;
	  }

</script>
  </body>
</html>
