package com.web.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ClassName: DataGrid 
 * @Description: 封装界面显示的datagrid表格数据的对象
 * @param total:数据总量
 * @param rows:显示的数据信息
 * @author zwt
 * @date 2016-6-14
 */
public class DataGrid {

	private Long total = 0L;
	@SuppressWarnings("unchecked")
	private List rows = new ArrayList();

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

}