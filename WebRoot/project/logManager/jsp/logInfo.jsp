<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/project/logManager/js/logInfo.js"></script>
<div id="" class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false,title:'显示列表'">
		<table id="logInfo_table"></table>
	</div>
	<div style="width: 320px" data-options="region:'west',border:false,title:'查询条件',iconCls:'icon-search'">
		<div class="easyui-layout" data-options="fit:true,border:false">
			<div data-options="region:'center',border:true" class="MyCondiPanel">
				<form id="loginfo_form" method="post" style="margin-top: 5px">
					<table class="tableForm-3 Mytranscolor">
						<tr>
							<th>日志类型：</th>
							<td><input id="log_loginfo_logtype" name="logtype" style="width:199px;" editable="false" class="easyui-combobox" data-options="
								valueField: 'value',
								textField: 'label',
								height:30,
								data: [{
									label: '请选择',
									value: '',
									selected:true
								},{
									label: '操作日志',
									value: 'log'
								},{
									label: '系统日志',
									value: 'syslog'
								}]" /></td>
						</tr>
						<tr>
							<th>日志级别：</th>
							<td><input id="log_loginfo_loglevel" name="loglevel" style="width:199px;" editable="false" class="easyui-combobox" data-options="
								valueField: 'value',
								textField: 'label',
								height:30,
								data: [{
									label: '请选择',
									value: '',
									selected:true
								},{
									label: '正常信息',
									value: 'info'
								},{
									label: '错误信息',
									value: 'error'
								},{
									label: '警告信息',
									value: 'warning'
								}]" /></td>
						</tr>
						<tr>
							<th>记录时间(起始)：</th>
							<td><input class="easyui-validatebox Wdate" style="width:194px" onClick="WdatePicker({dateFmt:'yyyy年M月d日',realDateFmt:'yyyy-MM-dd',vel:'logSt'})" readonly="readonly" /><input id="logSt" name="starttime" type="hidden" /></td>
						</tr>
						<tr>
							<th>记录时间(截止)：</th>
							<td><input class="easyui-validatebox Wdate" style="width:194px" onClick="WdatePicker({dateFmt:'yyyy年M月d日',realDateFmt:'yyyy-MM-dd',vel:'logEd'})" readonly="readonly" /><input id="logEd" name="endtime" type="hidden" /></td>
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
