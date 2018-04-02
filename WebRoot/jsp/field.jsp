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
	<script src="./lib/bootstrap-editable/js/bootstrap-editable.js"></script>
	<!-- <script src="./lib/bootstrap-table/bootstrap-table-export.js"></script> -->

    <script src="js/pintuer.js"></script>
    <script src="js/user.js"></script>
	<link rel="stylesheet" href="./lib/bootstrap/css/bootstrap.css" />
	<link rel="stylesheet" href="./lib/bootstrap-table/bootstrap-table.css" />
	<link rel="stylesheet" href="./lib/bootstrap-editable/css/bootstrap-editable.css" />
    
  </head> 
<body> 
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder">表字段</strong></div>
   <div id="toolbar">
    	<li style="padding-left:20px;"> 
    	<a href="./jsp/fieldadd.html" class="button border-main icon-plus-square-o"> 添加字段</a>
        <a href="javascript:void(0)" class="button border-red icon-trash-o" onclick="delSelectUsers()"> 删除</a>
        <!--  <a href="javascript:void(0)" class="button border-main icon-plus-square-o" onclick="createNewTable()">创建新表</a>  -->  
        <form id="table" method="post" action="" style="display:inline"> 
           <label>新数据表：</label>
           <input id="createtable" type="text" name="ntablename" placeholder="请输入新表名" data-validate="required:请输入表名" />                                   
    	   <input type="button" name="submit" class="button border-main icon-plus-square-o" value="创建新表" onclick="createNewTable()"/>
    	</form> 
    	</li>
   </div>
    <table id="userTable"></table>
    <form id="subform" method="post">
        <input type="hidden" id="subfieldid" name="subid"/>
    	<input type="hidden" id="subfieldName" name="subuserid"/>
    	<input type="hidden" id="subfieldTypeName" name="subusername"/>
    	
    </form>
    
    <script type="text/javascript">
		var obj;
		$(function () {
			$.ajax({
				url:"servlet/tableField",
				type:"POST",
				async:false,
				error:function(request){
					alert("Network Error 网络异常");
				},
				success:function(data){
					var obj= eval("("+data+")");
					$('#userTable').bootstrapTable({
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
						columns: [{field:"select",title:"全选",checkbox:true,width:20,align:"center",valign:"middle"},
						{field:"fieldid",title:"字段id",align:"center",valign:"middle",sortable:"true"},
						{field:"fieldName",title:"字段名称",align:"center",valign:"middle",sortable:"true"},
						{field:"fieldTypeName",title:"字段数据类型",align:"center",valign:"middle",sortable:"true"},
						/*{field:"isAutoInctement",title:"是否自动递增",align:"center",valign:"middle",sortable:"true"},
						{field:"constraint",title:"约束条件",align:"center",valign:"middle",sortable:"true"},*/
						{field:"action",title:"操作",align:"center",valign:"middle",formatter:"actionFormatter",event:"actionEvents"}],
						
						data:obj,
						onPageChange: function (size, number) {	
		                },
						
		                formatNoMatches: function(){
		                	return '无符合条件的记录';
		                }
					});	
				}
			});
			$(window).resize(function () {
				$('#userTable').bootstrapTable('resetView');//移除表数据
			});
		});
		function actionFormatter(value,row,index){
			
			var resu= '<div class="button-group">'+
						
						'<button class="button border-main icon-edit" onclick=modifyUser('+JSON.stringify(row)+');>修改</button>'+
						'<button class="button border-red icon-trash-o" name="btnDelUser" onclick=delUser('+JSON.stringify(row)+');>删除</button>'+
						'</div>';
			
			return resu;
		}

		function delSelectUsers(){
			var objSelec=$('#userTable').bootstrapTable('getSelections');
			if(objSelec==null){
				alert("请选择您要删除的内容!");
				return false;
			}else{
				var strtmp=JSON.stringify(objSelec);
				if(confirm("您确定要删除吗?")){
					$.ajax({
						url:"./servlet/deleteSelectFiled",
						type:"POST",
						data:{"objSelec":strtmp},
						//dataType:"json",
						async:false,
						error:function(request){
							alert("Network Error 网络异常");
						},
						success:function(data){
							if(data=="true"){
								alert("删除成功！");
								document.location="<%=basePath%>"+'/jsp/field.jsp';
							}else{
								alert("删除成功！");
								document.location="<%=basePath%>"+'/jsp/field.jsp';
							}
						}
					});
				}
			}
		}
		
		function createNewTable(){
			var objSelec=$('#userTable').bootstrapTable('getSelections');
			var tabName=$('#createtable').val();
			
			if(objSelec == null){
				alter("请选择要建表的字段!");
				return false;
			}else if(tabName==null){
			   alter("请输入表名！");
			   return false;	   
			}else{
				var ntabName=JSON.stringify(tabName);
				var strtmp=JSON.stringify(objSelec);
				if(confirm("您确定要创建新表吗?")){
					$.ajax({
						url:"./servlet/tableCreate",
						type:"POST",
						data:{"objSelec":strtmp,"tabName":ntabName},
						//dataType:"json",
						async:false,
						error:function(request){
							alert("Network Error 网络异常");
						},
						success:function(data){
							if(data=="true"){
								alert("创建成功！");
								document.location="<%=basePath%>"+'/jsp/field.jsp';
							}else{
								alert("创建成功！");
								document.location="<%=basePath%>"+'/jsp/field.jsp';
							}
						}
					});
				}
			}		
		}
				
		function modifyUser(row){
			document.getElementById("subform").action="./jsp/fieldmodify.jsp?subfieldName="+row.fieldName+"&"+"subfieldid="+row.fieldid;
			$("#subfieldName").val(row.fieldName);
			$("#subfieldTypeName").val(row.fieldTypeName);
			$("#subform").submit();
			
		}
		
		function delUser(row){
			
			if(confirm("您确定要删除吗?")){
				$.ajax({
					url:"./servlet/deleteFiled",
					type:"POST",
					data:{"fieldName":row.fieldName},
					async:false,
					error:function(request){
						alert("Network Error 网络异常");
					},
					success:function(data){
						if(data=="true"){
							alert("删除成功！");
							document.location="<%=basePath%>"+'/jsp/field.jsp';
						}else{
							alert("删除失败！");
						}
					}
				});		
			}		
		}
	</script>  
   

  </div>
<script type="text/javascript">

//单个删除
function del(id,mid,iscid){
	if(confirm("您确定要删除吗?")){
		
	}
}

//全选
$("#checkall").click(function(){ 
  $("input[name='id[]']").each(function(){
	  if (this.checked) {
		  this.checked = false;
	  }
	  else {
		  this.checked = true;
	  }
  });
});


</script>
</body>
</html>
