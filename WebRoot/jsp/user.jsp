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
    <script src="js/user.js"></script>
	<link rel="stylesheet" href="./lib/bootstrap/css/bootstrap.css" />
	<link rel="stylesheet" href="./lib/bootstrap-table/bootstrap-table.css" />
    
  </head> 
<body> <!-- onload="userPageLoad()"> -->
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder"> 用户列表</strong></div>
   <div id="toolbar">
    	<li style="padding-left:20px;"> 
    	<a href="./jsp/useradd.html" class="button border-main icon-plus-square-o"> 添加用户</a>
    	<a href="javascript:void(0)" class="button border-red icon-trash-o" onclick="delSelectUsers()"> 删除</a>
    	</li>
   </div>
    <table id="userTable"></table>
    <form id="subform" method="post">
    	<input type="hidden" id="subuserid" name="subuserid"/>
    	<input type="hidden" id="subusername" name="subusername"/>
    	<input type="hidden" id="subroleid" name="subroleid"/>
    	<input type="hidden" id="subifvalid" name="subifvalid"/>
    </form>
    
    <script type="text/javascript">
		var obj;
		$(function () {
			$.ajax({
				url:"servlet/PageLoad",
				type:"POST",
				data:eval("("+"{requestSou:\"userPage\"}"+")"),
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
						{field:"userid",title:"用户ID",align:"center",valign:"middle",sortable:"true"},
						{field:"username",title:"用户名",align:"center",valign:"middle",sortable:"true"},
						{field:"rolename",title:"角色名称",align:"center",valign:"middle",sortable:"true"},
						{field:"ifvalid",title:"有效性",align:"center",valign:"middle",sortable:"true"},
						{field:"ifonline",title:"在线状态",align:"center",valign:"middle",sortable:"true"},
						{field:"regtime",title:"注册时间",align:"center",valign:"middle",sortable:"true"},
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
			//return '<a class="mod">修改</a>'+'<a class="delete">删除</a>';
			var resu= '<div class="button-group">'+
						'<button class="button border-main icon-book" onclick=viewUser('+JSON.stringify(row)+');>详情</button>'+
						'<button class="button border-main icon-edit" onclick=modifyUser('+JSON.stringify(row)+');>修改</button>'+
						'<button class="button border-red icon-trash-o" name="btnDelUser" onclick=delUser('+JSON.stringify(row)+');>删除</button>'+
						'</div>';
			/* var resu1='<div class="button-group"><a class="button border-main" href="./jsp/userinfo.html"><span class="icon-book"></span>详情</a>'+
              		'<a class="button border-main" href="./jsp/userchange.html"><span class="icon-edit"></span> 修改</a>'+
              		'<a class="button border-red" href="javascript:void(0)" onclick="return del(1,1,1)"><span class="icon-trash-o"></span> 删除</a></div>';
			 */
			return resu;
		}
		
		function delSelectUsers(){
			var objSelec=$('#userTable').bootstrapTable('getSelections');
			if(objSelec==null){
				alert("请选择您要删除的内容!");
				return false;
			}else{
				var strtmp=JSON.stringify(objSelec);
				//var strobj="{\"btnDelSelecUser\":\"btnDelSelecUser\",\"objSelec\":"+strtmp+"}";
				//var obj=eval("("+strobj+")");
				if(confirm("您确定要删除吗?")){
					$.ajax({
						url:"./servlet/UserManage",
						type:"POST",
						data:{"objSelec":strtmp,"btnDelSelecUser":"DelSelecUser"},
						//dataType:"json",
						async:false,
						error:function(request){
							alert("Network Error 网络异常");
						},
						success:function(data){
							if(data=="true"){
								alert("删除成功！");
								document.location="<%=basePath%>"+'/jsp/user.jsp';
							}else{
								alert("删除失败！");
								document.location="<%=basePath%>"+'/jsp/user.jsp';
							}
						}
					});
				}
			}
		}

		function viewUser(row){
			document.getElementById("subform").action="./jsp/userinfo.jsp";
			$("#subuserid").val(row.userid);
			$("#subform").submit();
			/* var strobj="{\"btnViewUser\":\"btnViewUser\",\"userid\":"+row.userid+"}";
			var obj=eval("("+strobj+")");
			$.ajax({
				url:"./servlet/UserManage",
				type:"POST",
				data:obj,
				async:false,
				error:function(request){
					alert("Network Error 网络异常");
				},
				success:function(data){
					
								
				}
			});	 */
		}
		
		function modifyUser(row){
			document.getElementById("subform").action="./jsp/usermodify.jsp";
			$("#subuserid").val(row.userid);
			$("#subusername").val(row.username);
			$("#subroleid").val(row.roleid);
			$("#subifvalid").val(row.ifvalid);
			$("#subform").submit();
			
			<%-- alert(JSON.stringify(row));
			var strobj="{\"btnModifyUser\":\"btnModifyUser\",\"userinfo\":"+JSON.stringify(row)+"}";
			var obj=eval("("+strobj+")");
			$.ajax({
				url:"./jsp/usermodify.jsp",
				type:"POST",
				data:obj,
				//async:false,
				error:function(request){
					alert("Network Error 网络异常");
				},
				success:function(data){
					document.location="<%=basePath%>"+'/jsp/usermodify.jsp'+'?strobj='+strobj;
				}
			});	 --%>
		}
		
		function delUser(row){
			//alert(JSON.stringify(row));
			var strobj="{\"btnDelUser\":\"btnDelUser\",\"userid\":"+row.userid+"}";
			var obj=eval("("+strobj+")");
			if(confirm("您确定要删除吗?")){
				$.ajax({
					url:"./servlet/UserManage",
					type:"POST",
					data:obj,
					async:false,
					error:function(request){
						alert("Network Error 网络异常");
					},
					success:function(data){
						if(data=="true"){
							alert("删除成功！");
							document.location="<%=basePath%>"+'/jsp/user.jsp';
						}else{
							alert("删除失败！");
						}
					}
				});		
			}		
		}
	</script>  
   
<%--    <table class="table table-hover text-center">
      <tr>
        <th width="100" style="text-align:left; padding-left:20px;">用户ID</th>  
        <th>用户名</th>
        <th>角色名称</th>
        <th>有效性</th>
        <th>在线状态</th>
        <th width="10%">注册时间</th>
        <th width="310">操作</th>
      </tr>
     <!--  <volist name="list" id="vo"> -->
        <tr>
         
          <td style="text-align:left; padding-left:20px;">
          	<input type="checkbox" name="id[]" value="" /><%=j %>
          </td>
          
          <td>用户</td>
          <td>角色</td>  
          <td>有效</td>
          <td>离线</td>
          <td>2017-01-01</td>
          <td>
              <div class="button-group"> 
              <a class="button border-main" href="./jsp/userinfo.html"><span class="icon-book"></span>详情</a>
              <a class="button border-main" href="./jsp/userchange.html"><span class="icon-edit"></span> 修改</a> 
              <a class="button border-red" href="javascript:void(0)" onclick="return del(1,1,1)"><span class="icon-trash-o"></span> 删除</a> 
              </div>
          </td>
        </tr> 
      <tr>
        <td style="text-align:left; padding:19px 0;padding-left:20px;">
            <input type="checkbox" id="checkall"/>全选
        </td>
        <td colspan="7" style="text-align:left;padding-left:20px;">
            <a href="javascript:void(0)" class="button border-red icon-trash-o" style="padding:5px 15px;" onclick="DelSelect()"> 删除</a>  
        </td>
      </tr>
      <tr>
        <td colspan="8">
            <div class="pagelist"> 
                <a >上一页</a> 
                <span class="current">1</span>
                <a >下一页</a>
                <a >尾页</a> 
            </div>
        </td>
      </tr>
    </table> --%>
  </div>
<script type="text/javascript">
//搜索
function changesearch(){	
	
}

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

/* //批量删除
function DelSelect(){
	var Checkbox=false;
	 $("input[name='id[]']").each(function(){
	  if (this.checked==true) {		
		Checkbox=true;	
	  }
	});
	if (Checkbox){
		var t=confirm("您确认要删除选中的内容吗？");
		if (t==false) return false;		
		$("#listform").submit();		
	}
	else{
		alert("请选择您要删除的内容!");
		return false;
	}
} */
</script>
</body>
</html>
