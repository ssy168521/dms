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
    <link rel="stylesheet" href="css/field-autobuildtable.css">

     <!--<script src="./lib/jquery.js"></script> -->
    <script src="./lib/jquery-3.2.0.min.js"></script>
   	<script src="./lib/bootstrap/js/bootstrap.min.js"></script>	
	<script src="./lib/bootstrap-table/bootstrap-table.js"></script>
	<script src="./lib/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>
	<script src="./lib/bootstrap3-editable/js/bootstrap-editable.js"></script>
	<script src="./lib/bootstrap-table/extensions/editable/bootstrap-table-editable.js"></script>
    
    <script src="js/pintuer.js"></script>
    <script src="js/user.js"></script>
	<link rel="stylesheet" href="./lib/bootstrap/css/bootstrap.min.css" />
	<link rel="stylesheet" href="./lib/bootstrap-table/bootstrap-table.min.css" />
	<link rel="stylesheet" href="./lib/bootstrap3-editable/css/bootstrap-editable.css" />
    
  </head> 
<body> 
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder">新建库方案设计</strong></div>

    <div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<div class="row-fluid">
				<div class="span4" id="product">
					<form class="form-inline">
						<fieldset>
							 <label>产品名称 : </label><input type="text" id="producttype"/> <span class="help-block">输入归档产品类型.</span>
						</fieldset>
					</form>
				</div>
				<div class="span4" id="table">
					<form class="form-inline">
						<fieldset>
							 <label>数据库表名 : </label><input type="text" id="tablename" /> <span class="help-block">输入要建库的数据表名</span>
						</fieldset>
			       </form>
				</div>
				<div class="span4" id="panel-importshp">
					<form name="myForm"  enctype="multipart/form-data" >
			                      <input type="button" class="btn btn-primary" id="btn" value="确定" >    
			                 <span style="display:inline-block;"> 
					              <input type="file" accept="text/xml" name="xmlfile" >	 
					        </span>  
				        </form>
				  </div>
			</div>
		</div>
	</div>
</div>
    <div id="toolbar">
    	<li style="padding-left:20px;"> 
        
        <span class="help-block">说明：在表中选择合适的xml节点，然后输入新的字段名，建立字段节点连接关系</span>
    	</li>
   </div>
    <table id="userTable" class="table-userTable" style="height:100%;"></table>   
    <form id="subform" method="post">
        <input type="hidden" id="subfieldid" name="subid"/>
    	<input type="hidden" id="subfieldName" name="subuserid"/>
    	<input type="hidden" id="subfieldTypeName" name="subusername"/>
    	
    </form>    
  </div>
    <script type="text/javascript">

    $('#btn').click(function () {

        var xmlfile = document.myForm.xmlfile.files[0]; 
        var fm = new FormData();
        fm.append('xmlfile', xmlfile);
        $.ajax({
                url: 'servlet/UploadXML',
                type: 'POST',
                async: false,
                data: fm,
                contentType: false, //禁止设置请求类型
                processData: false, //禁止jquery对DAta数据的处理,默认会处理
                //禁止的原因是,FormData已经帮我们做了处理
                success: function (result) {
                	var obj=null;
                    //测试是否成功
                    //但需要你后端有返回值
                    //alert(result);
                    obj= eval("("+result+")");
    				 $('#userTable').bootstrapTable({
    					method : 'get',
    					cache : false,
    					height : 600,
    					toolbar: '#toolbar',                //工具按钮用哪个容器
    		            striped: true,                      //是否显示行间隔色
    					pagination: true,
    					pageSize : 10,
    					pageNumber : 1,
    					pageList: [5,10, 20, 50, 100],
    					showColumns : true,
    					showRefresh : true,
    					showToggle : true,
    					showExport : true,
    					//clickToSelect: true, //是否启用点击选中行
    					exportTypes : [ 'csv', 'txt', 'xml' ],
    					search : true,
    					
    					columns: [
                        //{field:"action",title:"操作",align:"center",valign:"middle",formatter:"actionFormatter",event:"actionEvents"},
    					{field:"select",title:"全选",checkbox:true,width:20,align:"center",valign:"middle"},					
    					{field:"fieldName",
    						title:"字段输入",
    						align:"center",
    						valign:"middle",
    						sortable:"true",
    					    editable: {
    		                    type: 'text',
    		                    title: '字段名',
    		                    validate: function (v) {
    		                        if (!v) return '用户名不能为空';

    		                    }
    		                }
    						},
    					{field:"nodeName",title:"节点名称",align:"center",valign:"middle",sortable:"true"},
    					{field:"nodecontent",title:"节点内容",align:"center",valign:"middle",sortable:"true"},
    					{field:"nodepath",title:"节点路径",align:"center",valign:"middle",sortable:"true"}
    					
    					],
    					data:obj,
    					onEditableSave: function (field, row, oldValue, $el) {
    				    	var productName=$('#producttype').val();
    				    	var productTable=$('#tablename').val();	
    			                $.ajax({
    			                    type: "post",
    			                    url: "./servlet/fieldedit",
    			                    data: {"objSelec":JSON.stringify(row),"fileName":JSON.stringify(xmlfile.name),
    			                    	"productName":JSON.stringify(productName),"productTable":JSON.stringify(productTable)},//json序列化，不能直接传送json对象
    			                    dataType: 'JSON',
    			                    success: function (data, status) {
    			                        if (status == "success") {
    			                            alert('提交数据成功');
    			                        }
    			                    },
    			                    error: function () {
    			                        alert('编辑失败');
    			                    },
    			                    complete: function () {

    			                    }

    			                });
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
    						
    						'<button class="button border-main icon-edit" onclick=modifyUser('+JSON.stringify(row)+');>创建连接</button>'+
    						
    						'</div>';
    			
    			return resu;
    		}
    		
    		function createNewTable(){
    			var objSelec=$('#userTable').bootstrapTable('getSelections');
    			var tabName=$('#createntable').val();
    			if(objSelec == null){
    				alter("请选择要建表的字段!");
    				return false;
    			}else if(tabName==null){
    			   alter("请输入表名！");
    			   return false;	   
    			}
    				else{
    			
    				var strtmp=JSON.stringify(objSelec);
    				if(confirm("您确定要创建新表吗?")){
    					$.ajax({
    						url:"./servlet/tableCreate",
    						type:"POST",
    						data:{"objSelec":strtmp},
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
    		
    		/*
    		function modifyUser(row){
    			document.getElementById("subform").action="./jsp/fieldmodify.jsp?subfieldName="+row.fieldName+"&"+"subfieldid="+row.fieldid;
    			$("#subfieldName").val(row.fieldName);
    			$("#subfieldTypeName").val(row.fieldTypeName);
    			$("#subform").submit();
    			
    		}*/
    		
	</script>  


</body>
</html>
