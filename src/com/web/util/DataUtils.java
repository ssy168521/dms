package com.web.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

/**


 */
public class DataUtils<T> {
	private Connection connection;

	public DataUtils(Connection connection) {
		this.connection = connection;
	}

	public int insert(String xml) throws Exception {
		Map<String, Object> map = getInsertSql(xml);
		return update((String) map.get("sql"), (Object[]) map.get("params"));
	}

	public int delete(Document document, String deleteID, Object deleteValue)
			throws Exception {
		Map<String, Object> map = getDeleteSql(document, deleteID, deleteValue);
		return update((String) map.get("sql"), (Object[]) map.get("params"));
	}

	public int insert(Document document) throws Exception {
		Map<String, Object> map = getInsertSql(document);
		return update((String) map.get("sql"), (Object[]) map.get("params"));
	}

	public int update(String sql) throws Exception {
		return new QueryRunner().update(connection, sql);
	}

	public int update(String sql, Object[] params) throws Exception {
		return new QueryRunner().update(connection, sql, params);
	}

	public static void main(String[] args) {

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List search(String sql, Object[] params) throws SQLException {
		return (List) new QueryRunner().query(connection, sql, params,
				new MapListHandler());
	}

	@SuppressWarnings("unchecked")
	public List search(String sql) throws SQLException {
		return (List) new QueryRunner().query(connection, sql,
				new MapListHandler());
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public Map searchSingle(String sql, Object[] params) throws SQLException {
		return (Map) new QueryRunner().query(connection, sql, params,
				new MapHandler());
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public int searchForInt(String sql, Object[] params, String key)
			throws SQLException {
		Map map = (Map) new QueryRunner().query(connection, sql, params,
				new MapHandler());
		if (map == null || map.size() == 0) {
			return -1;
		}
		if (map.get(key) == null) {
			return 0;
		}
		return Integer.parseInt(String.valueOf(map.get(key)));
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public String searchForString(String sql, Object[] params, String key)
			throws SQLException {
		Map map = (Map) new QueryRunner().query(connection, sql, params,
				new MapHandler());
		if (map == null || map.size() == 0) {
			return null;
		}
		return (String) map.get(key);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public long searchForLong(String sql, Object[] params, String key)
			throws SQLException {
		Map map = (Map) new QueryRunner().query(connection, sql, params,
				new MapHandler());
		if (map.get(key) == null || map.size() == 0) {
			return 0;
		}
		return ((BigDecimal) map.get(key)).longValue();
	}

	private Map<String, Object> getInsertSql(String xml) throws Exception {
		Document document = DocumentHelper.parseText(xml);
		return getInsertSql(document);
	}

	private Map<String, Object> getDeleteSql(Document document,
			String deleteID, Object deleteValue) throws DocumentException {

		String tableName = document.selectSingleNode("//MAIMS/TableName")
				.getText();
		String sql = "delete " + tableName + " where " + deleteID + " = ?";

		Object param;
		if (deleteValue == null) {
			Node deleteIdField = document
					.selectSingleNode("//MAIMS/Field[FieldName='" + deleteID
							+ "']");
			String fieldType = deleteIdField.selectSingleNode("./FieldType")
					.getText();
			String fieldValue = deleteIdField.selectSingleNode("./FieldValue")
					.getText();
			param = new Object[] { getEntity(fieldType, fieldValue) };
		} else {
			param = deleteValue;
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sql", sql.toString());
		result.put("params", param);
		return result;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getInsertSql(Document document)
			throws DocumentException {
		String tableName = document.selectSingleNode("//MAIMS/TableName")
				.getText();
		List<Element> fieldElements = document.selectNodes("//MAIMS/Field");
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("insert into " + tableName + " (");
		for (Element fieldElement : fieldElements) {
			String fieldName = fieldElement.selectSingleNode("./FieldName")
					.getText();
			String fieldType = fieldElement.selectSingleNode("./FieldType")
					.getText();
			String fieldValue = fieldElement.selectSingleNode("./FieldValue")
					.getText();
			sql.append(fieldName + ",");
			params.add(getEntity(fieldType, fieldValue));
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(") values (");
		for (int j = 0; j < params.size(); j++) {
			sql.append("?,");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sql", sql.toString());
		result.put("params", params.toArray(new Object[params.size()]));
		return result;

	}

	private Object getEntity(String type, String value) {
		if (type.equals("String")) {
			return value;
		} else if (type.equals("int") || type.equals("java.lang.Integer")) {
			return Integer.valueOf(value);
		} else if (type.equals("long") || type.equals("java.lang.Long")) {
			return Long.valueOf(value);
		} else if (type.equals("boolean") || type.equals("java.lang.Boolean")) {
			return Boolean.valueOf(value);
		} else if (type.equals("Timestamp")) {
			return Timestamp.valueOf(value.trim());
		} else {
			return null;
		}
	}

	public List<T> search(String sql, ResultSetHandler<List<T>> handler,
			Object[] params) throws SQLException {
		return new QueryRunner().query(connection, sql, handler, params);
	}
}
