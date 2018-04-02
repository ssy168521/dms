package com.web.dao;

import org.apache.logging.log4j.message.StringFormattedMessage;

/**
 * @author Administrator
 * @ClassName:Table
 * @Version 版本
 * @ModifiedBy 修改人
 * @date 2018年1月29日 下午10:01:16
 */
public class Table {
	//手动添加
	private String fieldName;       
	private String fieldTypeName;                  
	private int fieldid;
	//有参构造
	public Table(int fieldid,String fieldName, String fieldTypeName) {
		super();
		this.setFieldid(fieldid);
		this.setFieldName(fieldName);//将局部变量的值传递给成员变量
		this.setFieldTypeName(fieldTypeName);
	}

	public Table() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getFieldid() {
		return fieldid;
	}
	public void setFieldid(int fieldid) {
		this.fieldid = fieldid;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldTypeName() {
		return fieldTypeName;
	}
	public void setFieldTypeName(String fieldTypeName) {
		this.fieldTypeName = fieldTypeName;
	}

	
}
