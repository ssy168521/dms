package com.web.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

public class XMLUtil1 {
	/**
	 * @param document sqlxml
	 * @param barcodeSqlList 每盘磁带对应的所有sql
	 * @param barcode 磁带条码
	 * @param tableName 磁带sql中的表名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getSqlList(Document document,List<String> barcodeSqlList,String barcode,String tableName){
		List<Node> tmpList = document.selectNodes("//root/barcode[@value='"+barcode+"']/table[@tableName='"+tableName+"']/value");
		for (Node node : tmpList) {
			String sql = node.getText();
			if(StringUtils.isNotEmpty(sql)){
				barcodeSqlList.add(StringUtils.removeEndIgnoreCase(sql, ";"));
			}
		}
		return barcodeSqlList;
	}
	
	// 根据传入的参数取出对应参数的便
	public static String getValueByParameter(String xmlFile, String parameter) throws DocumentException {
		Document document = DocumentHelper.parseText(xmlFile);
		Node node = document.selectSingleNode("//InterfaceFile/FileBody/" + parameter);
		if (node == null) {
			throw new RuntimeException(parameter + " no exist.");
		}
		return node.getText();
	}
	
	/**
	 * 
	 * @param xmlFile
	 * @param parameter
	 * @return
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getValueByParameterEx(String xmlFile, String parameter) throws DocumentException {
		Document document = DocumentHelper.parseText(xmlFile);
		List<Node> nodeList = document.selectNodes("//InterfaceFile/FileBody/FileList/" + parameter);
		List<String> list = new ArrayList<String>();
		if (nodeList == null) {
			throw new RuntimeException(parameter + " no exist.");
		}
		
		for(Node node : nodeList) {
			list.add(node.getText());
		}
		
		return list;
	}
	
	public static String getValueByParameterII(String xmlFile, String parameter) throws DocumentException {
		Document document = DocumentHelper.parseText(xmlFile);
		Node node = document.selectSingleNode("//InterfaceFile/FileBody/" + parameter);
		if (node!= null) {
			return node.getText();
		}
		return "";
	}

	public static String returnXML(String flowTaskId, int dataId, int status, String barcode, String remark) {
		Document root = DocumentHelper.createDocument();
		root.setXMLEncoding("UTF-8");
		Element interfaceFileElement = root.addElement("InterfaceFile");
		
		Element fileBodyElement = interfaceFileElement.addElement("FileBody");
		fileBodyElement.addElement("TaskName").setText(flowTaskId);
		fileBodyElement.addElement("DataID").setText(String.valueOf(dataId));
		fileBodyElement.addElement("DownLoadStatus").setText(String.valueOf(status));
		fileBodyElement.addElement("Barcode").setText(barcode);
		fileBodyElement.addElement("Remark").setText(remark);
		
		return root.asXML();
	}
	
	public static List<String> getValueByParameter(String xmlFile) throws DocumentException {
		Document document = DocumentHelper.parseText(xmlFile);
		List<Node> nodeList = document.selectNodes("//files/file/name");
		List<String> list = new ArrayList<String>();
		if (nodeList == null) {
			throw new RuntimeException("parameter no exist.");
		}
		
		for(Node node : nodeList) {
			list.add(node.getText());
		}
		
		return list;
	}
}