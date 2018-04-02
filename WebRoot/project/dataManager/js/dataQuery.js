var dataQuery = $.extend({}, dataQuery);/* 定义全局对象，类似于命名空间或包的作用 */
$(function() {


	/**
	 * 初始化datagrid
	 */
	dataQuery.grid = $('#data_dataQuery_table').datagrid({
		fit : true,
		border : true,
		fitColumns : true,
		striped : true,
		nowrap : false,
		idField : 'dataid',//主键
		autoRowHeight : false,
		pagination : true,
		pageNumber : 1,
		pageSize : 20,
		url : myProjectPath + '/data/dataGridInfo?t='+new Date().getTime(),
		rowStyler : function(index, row) {
			return 'height:35px';
		},
		columns : [ [ {
			field : 'dataId',
			title : '数据ID',
			width : 10,
			align : 'center'
		}, {
			field : 'satellite',
			title : '卫星',
			width : 15,
			align : 'center'
		}, {
			field : 'sensor',
			title : '传感器',
			width : 15,
			align : 'center'
		}, {
			field : 'storageFile',
			title : '文件名称',
			width : 150,
			align : 'center'
		}, {
			field : 'orbitID',
			title : '轨道号',
			width : 15,
			align : 'center'
		}, {
			field : 'productLevel',
			title : '产品级别',
			width : 15,
			align : 'center'
		}, {
			field : 'cloudPercent',
			title : '云量',
			width : 10,
			align : 'center'
		}, {
			field : 'acquisitionTime',
			title : '接收时间',
			
			width : 30,
			align : 'center',
			formatter:function(value,row,index){
                 var unixTimestamp = new Date(value);
                 return unixTimestamp.toLocaleString();
            }
		}] ],
		onLoadSuccess : function(data) {
			// datagrid渲染完毕后渲染自定义的linkbutton
			if (data.rows.length == 0) {
				var body = $(this).data().datagrid.dc.body2;
				uj.noData(body);
			} else {

			}
			// 清除选择行
			dataQuery.grid.datagrid('clearChecked');
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
});
