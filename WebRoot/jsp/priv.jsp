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
   <form method="post" action="" id="listform">  
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder"> 权限列表</strong> <a href="" style="float:right; display:none;">添加字段</a></div>
    <div id="toolbar">
        <!-- <li style="padding-left:10px;">
			<a class="button border-main icon-plus-square-o" href="./jsp/privadd.html"> 添加权限</a>
        	<a href="javascript:void(0)" class="button border-red icon-trash-o" onclick="delSelectRoles()"> 删除</a> 
        </li> -->
    </div>    
    <table id="privTable"></table>    
  </div>
</form>
<script type="text/javascript">
	$(function () {
		$.ajax({
			url:"servlet/PageLoad",
			type:"POST",
			data:eval("("+"{requestSou:\"privPage\"}"+")"),
			async:false,
			error:function(request){
				alert("Network Error 网络异常");
			},
			success:function(data){
				//alert(data);
				var obj= eval("("+data+")");
				$('#privTable').bootstrapTable({
					method: 'get',
					cache: false,
					height: 708,
					toolbar: '#toolbar',
					striped: true,
					pagination: true,
					pageSize: 10,
					pageNumber: 1,
					pageList: [10, 20, 50, 100, 200, 500],
					search: true,
					showColumns: true,
					showRefresh: true,
					showExport: true,
					exportTypes: ['csv','txt','xml'],
					search: true,
					//clickToSelect: true,
					columns: [
					//{field:"select",title:"全选",checkbox:true,width:20,align:"center",valign:"middle"},
					{field:"privid",title:"权限ID",align:"center",valign:"middle",sortable:"true"},
					{field:"privname",title:"权限名称",align:"center",valign:"middle",sortable:"true"},
					{field:"privinfo",title:"权限说明",align:"center",valign:"middle",sortable:"true"},
					{field:"remark",title:"备注",align:"center",valign:"middle",sortable:"true"}
					//,{field:"action",title:"操作",align:"center",valign:"middle",formatter:"actionFormatter",event:"actionEvents"}
					],
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
					'<button class="button border-main icon-book" onclick=viewPriv('+JSON.stringify(row)+');>详情</button>'+
					'<button class="button border-main icon-edit" onclick=modifyPriv('+JSON.stringify(row)+');>修改</button>'+
					'<button class="button border-red icon-trash-o" name="btnDelPriv" onclick=delPriv('+JSON.stringify(row)+');>删除</button>'+
					'</div>';
		return resu;
	}
	
	function delPriv(row){
		alert(JSON.stringify(row));
		var strobj="{\"btnDelPriv\":\"btnDelPriv\",\"privid\":"+row.privid+"}";
		var obj=eval("("+strobj+")");
		if(confirm("您确定要删除吗?")){
			$.ajax({
				url:"./servlet/PrivManage",
				type:"POST",
				data:obj,
				async:false,
				error:function(request){
					alert("Network Error 网络异常");
				},
				success:function(data){
					if(data=="true"){
						alert("删除成功！");
						<%-- document.location="<%=basePath%>"+'/jsp/role.jsp'; --%>
					}else{
						alert("删除失败！");
					}
				}
			});	
		}
	}

</script>
  </body>
</html>
