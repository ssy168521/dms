/**  
 * @Title: BaseForm.java
 * @Package com.web.form
 * @Description: TODO
 * @author zwt
 * @date 2016-12-30
 */
package com.web.form;

/**
 * ClassName: BaseForm
 * 
 * @Description: 所有接收前台参数类的父类
 * @author zwt
 * @date 2016-12-30
 */
public class BaseForm {
	// for web
	//分页
	private int page;//页码
	private int rows;//显示多少行
	//排序
	private String sort;
	private String order;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}
