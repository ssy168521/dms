<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/project/dataManager/js/dataQuery.js"></script>
<div id="dataQuery_accod" class="displayTable easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false,title:'显示列表'">
		<table id="data_dataQuery_table"></table>
	</div>
	<div style="width: 320px" data-options="region:'west',border:false,title:'查询条件',iconCls:'icon-search'">
		<div class="easyui-layout" data-options="fit:true,border:false">
			<div data-options="region:'center',border:true" class="MyCondiPanel">
				<form id="dataQuery_Form" method="post" style="margin-top: 5px">
					<table class="tableForm-3 Mytranscolor">
						<tr>
							<th>卫星名称：</th>
							<td><select  class="easyui-combobox" editable="false" style="width:199px" data-options="height:30">
									<option value="">请选择</option>
									<option value="XXXX">XXXX</option>
							</select></td>
						</tr>
						<tr>
							<th>产品级别：</th>
							<td><select id="" class="easyui-combobox" editable="false" url='' style="width:199px" data-options="height:30">
							</select></td>
						</tr>
						<tr>
							<th>轨道号：</th>
							<td><select name="" editable="false"  class="easyui-combobox" url='' style="width:199px" data-options="height:30"></select></td>
						</tr><!-- 
						<tr>
							<th>条件4：</th>
							<td><select class="easyui-combobox" editable="false" style="width:199px" data-options="height:30"><option value="">请选择</option>
								<option value="0">否</option>
								<option value="1">是</option>
							</select></td>
						</tr> -->
						<tr>
							<th>时间(起)：</th>
							<td><input id="st" type="text" onClick="WdatePicker({dateFmt:'yyyy年M月d日',realDateFmt:'yyyy-MM-dd HH:mm:ss',vel:'starttime',isShowClear:'true'})" class="easyui-validatebox Wdate" style="width:194px;cursor: pointer;" readonly="readonly" /> <input id="starttime" type="hidden"></input></td>
						</tr>
						<tr>
							<th>时间(止)：</th>
							<td><input id="et" type="text" onClick="WdatePicker({dateFmt:'yyyy年M月d日',realDateFmt:'yyyy-MM-dd HH:mm:ss',vel:'starttime',isShowClear:'true'})" class="easyui-validatebox Wdate" style="width:194px;cursor: pointer;" readonly="readonly" /> <input id="endtime" type="hidden"></input></td>
						</tr>

					</table>
				</form>
			</div>
			<div data-options="region:'south',border:true" class="Mytranscolor MyCondiPanelSouth" style="height: 120px;">
				<button class="button-yes" name="submit">查询</button>
				<button class="button-no" name="submit">重置</button>
			</div>
		</div>
	</div>
</div>
