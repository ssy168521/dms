package com.web.common;

/**
 * 
 * ClassName: Json 
 * @Description: 用来封装给界面的反馈信息
 * @date 2016-12-30
 */
@SuppressWarnings("serial")
public class Json implements java.io.Serializable {

	private boolean success = false;

	private String msg = "错误";

	private Object obj = null;

	private String returnType = "error";//用来标记前台提示框显示图标类型

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

}
