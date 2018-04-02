<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
			
	String rootpath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort();
    String operator= request.getParameter("Operator");
    String oid=request.getParameter("id");
%>
<script src="../../../lib/jquery.js"></script>

<div class="easyui-panel" border=false style="width:100%;padding:30px 60px;">
	<form id="configureForm" method="post">
	    <!-- 隐藏值ID，记录编辑的信息的id号 -->
		<input type="hidden" name="id" value="<%=oid%>">
	    <input class="easyui-textbox" name="Operator" type="hidden" value="<%=operator%>">
		<p>卫星名称</p>
		<select id="satellite" name="satellite" class="easyui-combobox" style="width:50%;height:32px">
			<option value="ZY3-1">ZY3-1</option>
			<option value="ZY3-2">ZY3-2</option>
			<option value="GF1">GF1</option>
			<option value="GF2">GF2</option>
		</select>
		<p>归档路径</p>
		<input class="easyui-textbox" name="archivePath" data-options="prompt:'数据在生产区的存放路径'" style="width:50%;height:32px">
		<p>存储路径</p>
		<input class="easyui-textbox" name="destPath" data-options="prompt:'数据在存储区的存放路径'" style="width:50%;height:32px">
		<p>存储规则</p>
		<input class="easyui-textbox" name="storageRule" data-options="prompt:'Enter something here...'" style="width:50%;height:32px">
		<p>校验规则</p>
		<input class="easyui-textbox" name="checkRule" data-options="prompt:'以逗号为分隔输入文件后缀(小写)'" style="width:50%;height:32px">
		<p>归档开始时间</p>
		<input class="easyui-textbox" name="archiveStart" data-options="prompt:'Enter something here...'" style="width:50%;height:32px">
		<p>归档结束时间</p>
		<input class="easyui-textbox" name="archiveEnd" data-options="prompt:'Enter something here...'" style="width:50%;height:32px">
		<div>
        <a href="#" class="easyui-linkbutton" id="btnAddconf"  data-options="iconCls:'icon-save'">提交 </a>
          &nbsp;&nbsp;  &nbsp;&nbsp;  &nbsp;&nbsp;
        <a href="#" class="easyui-linkbutton"  data-options="iconCls:'icon-save'">取消</a>
        </div>
	</form>
	
</div>

<script type="text/javascript">
	$("#btnAddconf").click(function(){
		
		var strSatname=$("input[name='satellite']").val();
		var strArchivePath=$("input[name='archivePath']").val();
		var strdestPath=$("input[name='destPath']").val();
		var strstorageRule=$("input[name='storageRule']").val();
		var strcheckRule=$("input[name='checkRule']").val();
		var strarchiveStart=$("input[name='archiveStart']").val();
		var strarchiveEnd=$("input[name='archiveEnd']").val();
		if(strSatname==""){
			alert("请输入卫星名称！！");
			return false;
		}
		if(strArchivePath==""){
			alert("请输入归档源文件夹路径！！");
			return false;
		}
		
		if(strdestPath==""){
			alert("请输入归档目标文件夹路径！！");
			return false;
		}

		
		$.ajax({
			url:"../../../servlet/AutoArchive",
			type:"POST",
			data:$("#configureForm").serialize(),
			async:false,
			error:function(request){
				alert("Network Error 网络异常");
			},
			success:function(data){
					alert("配置成功！");
			}
		});	
	});
</script>