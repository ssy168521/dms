package com.web.service.impl;

import java.sql.Connection;
import java.util.List;

import com.web.dao.WebDao;
import com.web.dao.impl.WebDaoImpl;
import com.web.form.ConfigureForm;
import com.web.form.DataInfo;
import com.web.form.Staticstis;
import com.web.form.TaskData;
import com.web.form.TaskInfo;
import com.web.service.WebService;

public class WebServiceImpl implements WebService {
	private WebDao dao = new WebDaoImpl();

	@Override
	public boolean addSysConfig(Connection conn, ConfigureForm item)
			throws Exception {
		return dao.addSysConfig(conn, item);
	}

	@Override
	public List<ConfigureForm> getAllSysConfig(Connection conn)
			throws Exception {
		if (conn != null)
			return dao.getAllSysConfig(conn);
		else
			return null;
	}

	@Override
	public boolean isFileArchive(Connection conn, String fileName, long fileSize)
			throws Exception {
		return dao.isFileArchive(conn, fileName, fileSize);
	}

	@Override
	public boolean isFileArchive(Connection conn, String tablename,
			String fileName) throws Exception {
		return dao.isFileArchive(conn, tablename, fileName);
	}

	@Override
	public int getMaxDataId(Connection conn) throws Exception {
		return dao.getMaxDataId(conn);
	}

	@Override
	public boolean insertProduct(Connection conn, DataInfo dataInfo)
			throws Exception {
		return dao.insertProduct(conn, dataInfo);
	}

	@Override
	public List<DataInfo> getDataInfo(Connection conn, DataInfo data)
			throws Exception {
		return dao.getDataInfo(conn, data);
	}

	@Override
	public boolean updateSysConfig(Connection conn, ConfigureForm item)
			throws Exception {
		return dao.updateSysConfig(conn, item);
	}

	@Override
	public boolean deleteSysConfig(Connection conn, ConfigureForm item)
			throws Exception {
		return dao.deleteSysConfig(conn, item);
	}

	@Override
	public ConfigureForm getConfigById(Connection conn, int id)
			throws Exception {
		// TODO Auto-generated method stub
		return dao.getConfigById(conn, id);
	}

	@Override
	public void updateStaticts(Connection conn, String satellite, int status)
			throws Exception {
		dao.updateStaticts(conn, satellite, status);
	}

	@Override
	public List<Staticstis> getStaticstisInfo(Connection conn, Staticstis s)
			throws Exception {
		return dao.getStaticstisInfo(conn, s);
	}

	@Override
	public int addTask(Connection conn, String destPath, int... ids)
			throws Exception {
		return dao.addTask(conn, destPath, ids);
	}

	@Override
	public List<TaskInfo> getTask(Connection conn) throws Exception {
		return dao.getTask(conn);
	}

	@Override
	public List<TaskData> getTaskData(Connection con, int taskId)
			throws Exception {
		return null;
	}

	@Override
	public void updataTaskStatus(Connection conn, int taskId, String status)
			throws Exception {
		dao.updataTaskStatus(conn, taskId, status);
	}

}
