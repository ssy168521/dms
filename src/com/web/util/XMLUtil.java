package com.web.util;

import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;

public class XMLUtil {
	String xmlfilename;

	public XMLUtil(String xmlfilename) {
		this.xmlfilename = xmlfilename;
	}

	public static HashMap<String, String> getMaplistByDiskname(String xmlpath,
			String inputVal) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		SAXReader saxReader = new SAXReader();//xml节点读取
		Document doc = saxReader.read(xmlpath);
		Element root = doc.getRootElement();
		for (Iterator iter = root.elementIterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			QName qname = element.getQName();
			String tagname = qname.getName();
			Attribute name = element.attribute("diskname");//smb本地共享盘符
			if (name == null)
				continue;
			String value = name.getValue();
			if (value != null && inputVal.compareToIgnoreCase(value) == 0) {
				Iterator i = element.elementIterator();
				while (i.hasNext()) {
					Element e = (Element) i.next();
					map.put(e.getName(), e.getText());
				}
			}

		}
		return map;
	}

	public static HashMap<String, String> getMaplistBySmbPath(String xmlpath,
			String inputVal) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(xmlpath);
		Element root = doc.getRootElement();
		for (Iterator iter = root.elementIterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			QName qname = element.getQName();
			String tagname = qname.getName();
			Attribute name = element.attribute("smbpath");
			if (name == null)
				continue;
			String value = name.getValue();
			if (value == null)
				continue;
			if (inputVal.contains(value)) {
				Iterator i = element.elementIterator();
				while (i.hasNext()) {
					Element e = (Element) i.next();
					map.put(e.getName(), e.getText());
				}
			}

		}
		return map;
	}

	/*
	 * public static HashMap<String, String> getMaplistByPartion(String xmlpath,
	 * String key, String inputVal) throws Exception { HashMap<String, String>
	 * map = new HashMap<String, String>(); SAXReader saxReader = new
	 * SAXReader(); Document doc = saxReader.read(xmlpath); Element root =
	 * doc.getRootElement(); for (Iterator iter = root.elementIterator();
	 * iter.hasNext();) { Element element = (Element) iter.next(); QName qname =
	 * element.getQName(); String tagname = qname.getName(); Attribute name =
	 * element.attribute(key); if (name == null) continue; String value =
	 * name.getValue(); if (value != null && inputVal.equals(value)) { Iterator
	 * i = element.elementIterator(); while (i.hasNext()) { Element e =
	 * (Element) i.next(); map.put(e.getName(), e.getText()); } }
	 * 
	 * } return map; }
	 */
	public static String getValueByKey(String xmlpath, String key)
			throws Exception {
		SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(xmlpath);
		Element root = doc.getRootElement();
		for (Iterator iter = root.elementIterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			Attribute name = element.attribute(key);
			if (name != null)
				return name.getValue();
		}
		return "";
	}
}
