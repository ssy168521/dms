var G_configure = $.extend({}, G_configure);/* 定义全局对象，类似于命名空间或包的作用 */
G_configure.grid;
$(function(){
	createConfigureGrid();
});


/**
 * 生数据分类datagrid
 */
function createConfigureGrid() {
	G_configure.grid = $('#configure_grid').datagrid({
		border : false,
		fit : true,
		fitColumns : true,
		striped : true,
		idField : 'id',
		singleSelect : true,
		checkOnSelect : false,
		selectOnCheck : false,
		scrollbarSize : 0,
		pagination : true,
		pageNumber : 1,
		pageSize : 20,
		url : myProjectPath + '/configure/dataGridInfo?t='+new Date().getTime(),
//		url : myProjectPath + '/common/jsonForTest/datagrid_data1.json?t='+new Date().getTime(),
		rowStyler : function(index, row) {
			return 'height:35px';
		},
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'id',
			title : 'id',
			width : 10,
			align : 'center'
		}, {
			field : 'satellite',
			title : '卫星名称',
			width : 20,
			align : 'center'
		}, {
			field : 'archivePath',
			title : '归档路径',
			width : 30,
			align : 'center'
		}, {
			field : 'destPath',
			title : '存储路径',
			width : 30,
			align : 'center'
		}, {
			field : 'storageRule',
			title : '存储规则',
			width : 80,
			align : 'center'
		}, {
			field : 'archiveStart',
			title : '归档开始时间',
			width : 20,
			align : 'center'
		}, {
			field : 'archiveEnd',
			title : '归档结束时间',
			width : 20,
			align : 'center'
		}, {
			field : 'dd',
			title : '操作',
			width : 30,
			align : 'center',
			formatter : function(value, row, index) {
				var str;
				var id = row.id;
				str = "<a href='javascript:void(0);' style='margin-right:20px;' class='configure_edit grid-handler-hover' onclick='editConfigure(" + index + ")'>编辑</a>" + "<a href='javascript:void(0);' class='configure_cut grid-handler-hover' onclick='delConfigure(" + id + ")'>删除</a>";
				return str;
			}
		} ] ],
		onLoadSuccess : function(data) {

			if (data.rows.length == 0) {
				var body = $(this).data().datagrid.dc.body2;
				uj.noData(body);
			} else {
				// datagrid渲染完毕后渲染自定义的linkbutton
				$('.configure_edit').linkbutton({
					plain : true,
					iconCls : 'icon-edit'
				});
				$('.configure_cut').linkbutton({
					plain : true,
					iconCls : 'Myicon-bin-empty'
				});
			}
			// 清除选择行
			G_configure.grid.datagrid('clearChecked');
		},
		onLoadError : function() {
			var body = $(this).data().datagrid.dc.body2;
			uj.errorData(body);
		},
		onRowContextMenu : function(e, rowIndex, rowData) {
			// datagrid中取消右键点击行
			e.preventDefault();
		}
	});
}


/**
 * 添加
 */
function addConfigure() {
	// 动态创建dialog
	var p = Mydialog({
		modal : true,
		width : 460,
		shadow : false,
		href : myProjectPath + '/project/configure/dialog/formDig.jsp',
		title : '添加配置',
		closable : false,
		iconCls : 'Myicon-cup-edit',
		buttons : [ {
			text : '保存',
			iconCls : 'icon-ok',
			handler : function() {
				var f = p.find('form');
				f.form('submit', {
					url : myProjectPath + '/configure/addInfo?t=' + new Date().getTime(),
					onSubmit : function() {
						//校验表单中信息是否合法
						if ($(this).form('validate')) {
							$.messager.progress();
						} else {
							return false;
						}
					},
					success : function(d) {
						$.messager.progress('close');
						var json = $.parseJSON(d);
						if (json.success) {
							G_configure.grid.datagrid('reload');
							p.dialog('close');
						}
						zwtAlert('提示', json.msg, json.returnType);
					}
				});
			}
		}, {
			text : '取消',
			iconCls : 'Myicon-door-in',
			handler : function() {
				p.dialog('close');
			}
		} ],
		onLoad : function() {
			setTimeout(function() {
				$("#strategy_dataCategory_typename").focus();
			}, 0);
		}
	}).dialog("move",{top:$(document).scrollTop() + ($(window).height()-600) * 0.5});
};

/**
 * 编辑
 */
function editConfigure(index) {
	var rows = G_configure.grid.datagrid('getRows');
	var p = Mydialog({
		modal : true,
		width : 460,
		shadow : false,
		href : myProjectPath + '/project/configure/dialog/formDig.jsp',
		title : '编辑配置',
		closable : false,
		iconCls : 'Myicon-cup-edit',
		buttons : [ {
			text : '保存',
			iconCls : 'icon-ok',
			handler : function() {
				var f = p.find('form');
				f.form('submit', {
					url : myProjectPath + '/configure/editInfo?t=' + new Date().getTime(),
					onSubmit : function() {
						//校验表单中信息是否合法
						if ($(this).form('validate')) {
							$.messager.progress();
						} else {
							return false;
						}
					},
					success : function(d) {
						$.messager.progress('close');
						var json = $.parseJSON(d);
						if (json.success) {
							G_configure.grid.datagrid('reload');
							p.dialog('close');
						}
						zwtAlert('提示', json.msg, json.returnType);
					}
				});
			}
		}, {
			text : '取消',
			iconCls : 'Myicon-door-in',
			handler : function() {
				p.dialog('close');
			}
		} ],
		onLoad : function() {
			var f = p.find('form');
			f.form('load', rows[index]);
		}
	}).dialog("move",{top:$(document).scrollTop() + ($(window).height()-600) * 0.5});
};

/**
 * 删除一条记录
 */
function delConfigure(id) {
	zwtConfirm('提示', '是否删除?', function(r) {
		if (r) {
			$.messager.progress();
			$.ajax({
				type : "get",
				url : myProjectPath + '/configure/deleteInfo?t=' + new Date().getTime(),
				data : {
					id : id
				},
				cache : false,
				dataType : "json",
				success : function(data) {
					$.messager.progress('close');
					G_configure.grid.datagrid('reload');
					zwtAlert('提示', data.msg, data.returnType);
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					$.messager.progress('close');
					// 通常情况下textStatus和errorThrown只有其中一个包含信息
					// this; //调用本次ajax请求时传递的options参数
					zwtAlert('提示', '请求发送失败,失败原因[' + errorThrown + ']', 'error');
				}
			});
		}
	});
};