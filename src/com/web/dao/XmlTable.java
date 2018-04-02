package com.web.dao;

/**
 * 
 * @author Administrator
 * @ClassName:XmlTable
 * @Version 版本
 * @ModifiedBy han
 * @date 2018年3月8日 下午6:19:37
 */
public class XmlTable {
	   //读xml获取节点
		private String nodecontent;
		private String nodepath;
		private String nodeName;       
		private String fieldName; 

		public XmlTable(String fieldName,String nodeName,String nodecontent,String nodepath){
			super();
			this.setFieldName(fieldName);
			this.setNodeName(nodeName);
			this.setNodecontent(nodecontent);
			this.setNodepath(nodepath);
		}
		public XmlTable() {
			super();
			// TODO Auto-generated constructor stub
		}
		public String getNodecontent() {
			return nodecontent;
		}
		public void setNodecontent(String nodecontent) {
			this.nodecontent = nodecontent;
		}
		public String getNodepath() {
			return nodepath;
		}
		public void setNodepath(String nodepath) {
			this.nodepath = nodepath;
		}
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public String getNodeName() {
			return nodeName;
		}
		public void setNodeName(String nodeName) {
			this.nodeName = nodeName;
		}
		
}
