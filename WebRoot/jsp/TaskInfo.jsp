<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String tasktype= request.getParameter("tasktype");
	//System.out.println(tasktype);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>任务管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script src="./lib/jquery.js"></script>
<link href="./lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="./lib/bootstrap-table/bootstrap-table.css" />
<script src="./lib/bootstrap/js/bootstrap.js"></script>
<script src="./lib/bootstrap-table/bootstrap-table.js"></script>
<script src="./lib/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>
</head>

<body>
	<div class="control-group" style="display:inline ">
		<div style="display:inline ">
			<form id="formid" class="form-horizontal">
				<label>状态信息：</label> <input type="checkbox"
					name="taskrunning-checkbox" checked> <label>正在执行</label> <input
					type="checkbox" name="taskfinished-checkbox"> <label>已完成</label>
				<input type="checkbox" name="taskstop-checkbox"> <label>已中止</label>
				<input name="tasktypename" type="hidden" value="<%=tasktype%>">
<!-- 				<label>任务类型：</label>
				<select class="selectpicker">
				  <option>数据归档</option>
				  <option>数据下载</option>
				</select> -->
				
				
			</form>
		</div>
		<div style="display:inline ">
			<button id="btn-search" class="btn btn-default" type="submit">查询</button>
		</div>
	</div>
	<table id="TaskTable"></table>
	<script>
	
	$(document).ready(function(){
	   //    document.getElementsByName("tasktypename").value="<%=tasktype%>";
	   //    alert(document.getElementsByName("tasktypename").value);
		   quyTaskInfo();
 
});


		function quyTaskInfo() {

			$.ajax({
				url : "servlet/ShowTaskInfoservlet",
				type : "POST",
				data : $("#formid").serialize(),
				async : false,
				error : function(request) {
					alert("Network Error 网络异常");
				},
				success : function(data) {
					if ($('#TaskTable').bootstrapTable != null)
						$('#TaskTable').bootstrapTable('destroy');
					$('#TaskTable').bootstrapTable('removeAll');
					//  data="[{\"taskEndTime\":\"2017-03-14 14:24:16\",\"taskId\":42,\"taskMarkinfo\":\"d:\\test 0 files archive\",\"taskName\":\"数据归档\",\"taskProgress\":0,\"taskStartTime\":\"2017-03-14 14:24:15\",\"taskStatus\":\"finish\",\"taskType\":\"数据归档\"}]";
					if (data == "") {
						return '无符合条件的记录';
						;
					}
					var obj = eval("(" + data + ")");
					$('#TaskTable').bootstrapTable({
						method : 'get',
						cache : false,
						height : 600,
						striped : true,
						//pagination: true,
						pageSize : 10,
						pageNumber : 1,
						//pageList: [5,10, 20, 50, 100],
						search : true,
						showColumns : true,
						showRefresh : true,
						showToggle : true,
						showExport : true,
						exportTypes : [ 'csv', 'txt', 'xml' ],
						search : true,

						//clickToSelect: true,
						columns : [ {
							field : "select",
							title : "全选",
							checkbox : true,
							width : 20,
							align : "center",
							valign : "middle"
						}, {
							field : "taskId",
							title : "任务ID",
							align : "center",
							valign : "middle",
							sortable : "true"
						}, {
							field : "taskName",
							title : "任务名称",
							align : "center",
							valign : "middle",
							sortable : "true"
						}, {
							field : "taskStatus",
							title : "任务状态",
							align : "center",
							valign : "middle",
							sortable : "true"
						}, {
							field : "taskType",
							title : "任务类型",
							align : "center",
							valign : "middle",
							sortable : "true"
						}, {
							field : "startTime",
							title : "开始时间",
							align : "center",
							valign : "middle",
							sortable : "true"
						}, {
							field : "finishTime",
							title : "结束时间",
							align : "center",
							valign : "middle",
							sortable : "true"
						}, {
							field : "progress",
							title : "任务进度",
							align : "center",
							valign : "middle",
							sortable : "true"
						}, {
							field : "markinfo",
							title : "任务备注",
							align : "center",
							valign : "middle",
							sortable : "true"
						}, {
							field : "action",
							title : "操作",
							align : "center",
							valign : "middle",
							formatter : "actionFormatter",
							event : "actionEvents"
						} ],
						data : obj,

						formatNoMatches : function() {
							return '无符合条件的记录';
						}

					});
					$(window).resize(function() {
						$('#TaskTable').bootstrapTable('resetView');
					});
				}
			});
		}

		$("#btn-search").click(quyTaskInfo);

		function actionFormatter(value, row, index) {
			var id = row.taskId;
			var status = row.taskStatus;
			var resu = '<div >'
					+
					//'<button class="button border-main icon-book" >详情</button>'+  class="btn btn-default"
					//'<button class="button border-main icon-edit" >修改</button>'+
					'<button class="btn btn-default"  onclick=StopTask('
					+ id
					+ ','
					+ status
					+ ');><span class="glyphicon glyphicon-trash"></span>中止任务</button>'
					+ '</div>';
			return resu;
		}

		function StopTask(id, status) {

			if (status != 1) {
				alert("该任务没有在运行！");
				return true;
			}

			if (id == null || id == "") {
				alert("请选择您要中止的任务!");
				return false;
			}

			var strobj = "{\"TaskID\":" + id + "}";
			var obj = eval("(" + strobj + ")");
			{
				//var strtmp=JSON.stringify(objSelec);
				if (confirm("您确定要中止吗?")) {
					$.ajax({
						url : "./servlet/TaskHandleservlet",
						type : "POST",
						data : obj,
						async : false,
						error : function(request) {
							alert(request);
							alert("Network Error 网络异常");
						},
						success : function(data) {
							if (data == "true") {
								alert("删除成功！");
							} else {
								alert(data);
							}
						}
					});
				}
			}
		}
	</script>
</body>

</html>
