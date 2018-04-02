package com.web.dao.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.web.dao.WebDao;
import com.web.form.ConfigureForm;
import com.web.form.DataInfo;
import com.web.form.Staticstis;
import com.web.form.TaskData;
import com.web.form.TaskInfo;
import com.web.util.DataUtils;

public class WebDaoImpl implements WebDao {

	@Override
	public boolean addSysConfig(Connection conn, ConfigureForm item)
			throws Exception {
		String sql = "insert into SysConfig(satellite, archivePath, destPath, storageRule, checkRule, archiveStart, archiveEnd)"
				+ " values(?,?,?,?,?,?,?)";

		Object[] params = { item.getSatellite(), item.getArchivePath(),
				item.getDestPath(), item.getStorageRule(), item.getCheckRule(),
				item.getArchiveStart(), item.getArchiveEnd() };

		return new DataUtils(conn).update(sql, params) > 0 ? true : false;
	}

	@Override
	public List<ConfigureForm> getAllSysConfig(Connection conn)
			throws Exception {
		String sql = "select id,satellite, archivePath, destPath, storageRule, archiveStart, archiveEnd, enable, modifyTime, checkRule from SysConfig";

		ResultSetHandler<List<ConfigureForm>> handler = new BeanListHandler<ConfigureForm>(
				ConfigureForm.class);
		return new DataUtils(conn).search(sql, handler, null);
	}

	@Override
	public boolean isFileArchive(Connection conn, String tablename,
			String fileName) throws Exception {
		String sql = "select count(1) as mycount from " + tablename
				+ " where filename = ?";
		Object[] params = { fileName };
		return new DataUtils(conn).searchForInt(sql, params, "mycount") > 0 ? true
				: false;
	}

	@Override
	public boolean insertProduct(Connection conn, DataInfo dataInfo)
			throws Exception {
		String sql = "insert into ProductInfo(archiveFile, storageFile, dataid, orbitID, scenePath, sceneRow, satellite, sensor, "
				+ "acquisitionTime, productLevel, cloudPercent, productQuality, fileSize)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Object[] params = { dataInfo.getArchiveFile(),
				dataInfo.getStorageFile(), dataInfo.getDataId(),
				dataInfo.getOrbitID(), dataInfo.getScenePath(),
				dataInfo.getSceneRow(), dataInfo.getSatellite(),
				dataInfo.getSensor(), dataInfo.getAcquisitionTime(),
				dataInfo.getProductLevel(), dataInfo.getCloudPercent(),
				dataInfo.getProductQuality(), dataInfo.getFileSize() };

		return new DataUtils(conn).update(sql, params) > 0 ? true : false;
	}

	@Override
	public List<DataInfo> getDataInfo(Connection conn, DataInfo data)
			throws Exception {
		String sql = "select dataid, orbitID, scenePath, sceneRow, satellite, sensor, acquisitionTime, productLevel, "
				+ "cloudPercent, productQuality, archiveFile, storageFile from ProductInfo";

		ResultSetHandler<List<DataInfo>> handler = new BeanListHandler<DataInfo>(
				DataInfo.class);
		return new DataUtils(conn).search(sql, handler, null);
	}

	@Override
	public int getMaxDataId(Connection conn) throws Exception {
		String sql = "select max(dataid) as maxid from ProductInfo";
		List<Map> list = new DataUtils(conn).search(sql);
		if (list.size() > 0) {
			return Integer.parseInt(list.get(0).get("maxid").toString());
		}
		return 0;
	}

	@Override
	public boolean updateSysConfig(Connection conn, ConfigureForm item)
			throws Exception {
		String sql = "update SysConfig set satellite=?, archivePath=?, destPath=?, storageRule=?, archiveStart=?, archiveEnd=? where id=?";
		Object[] params = { item.getSatellite(), item.getArchivePath(),
				item.getDestPath(), item.getStorageRule(),
				item.getArchiveStart(), item.getArchiveEnd(), item.getId() };
		return new DataUtils(conn).update(sql, params) > 0 ? true : false;
	}

	@Override
	public boolean deleteSysConfig(Connection conn, ConfigureForm item)
			throws Exception {
		String sql = "delete from SysConfig where id=?";
		Object[] params = { item.getId() };
		return new DataUtils(conn).update(sql, params) > 0 ? true : false;
	}

	@Override
	public boolean isFileArchive(Connection conn, String fileName, long fileSize)
			throws Exception {
		String sql = "select count(1) as mycount from ProductInfo where FileName = ? and filesize = ?";

		Object[] params = { fileName, fileSize };

		ResultSetHandler<List<DataInfo>> handler = new BeanListHandler<DataInfo>(
				DataInfo.class);

		List<Map> list = new DataUtils(conn).search(sql, params);
		if (list.size() > 0) {
			return Integer.parseInt(list.get(0).get("mycount").toString()) > 0 ? true
					: false;
		}
		return false;
	}

	@Override
	public ConfigureForm getConfigById(Connection conn, int id)
			throws Exception {
		String sql = "select id,satellite, archivePath, destPath, storageRule, archiveStart, "
				+ "archiveEnd, enable, modifyTime, checkRule from SysConfig where id = ?";

		Object[] params = { id };

		List<Map> list = new DataUtils(conn).search(sql, params);

		if (list == null || list.size() < 1)
			return null;

		for (int i = 0; i < list.size(); i++) {
			ConfigureForm c = new ConfigureForm();
			c.setSatellite(list.get(i).get("satellite").toString());

			return c;
		}

		return null;
	}

	@Override
	public void updateStaticts(Connection conn, String satellite, int status)
			throws Exception {
		String sql = "";
		if (status == 1) {
			sql = "update statistics set sucess_count = sucess_count +1 where satellite = ?";
		} else {
			sql = "update statistics set failed_count = failed_count +1 where satellite = ?";
		}
		Object[] params = { satellite };

		new DataUtils(conn).update(sql, params);
	}

	@Override
	public List<Staticstis> getStaticstisInfo(Connection conn, Staticstis s)
			throws Exception {
		String sql = "select id,satellite,sensor,sucess_count,failed_count from statistics";

		ResultSetHandler<List<Staticstis>> handler = new BeanListHandler<Staticstis>(
				Staticstis.class);
		return new DataUtils(conn).search(sql, handler, null);
	}

	@Override
	public int addTask(Connection conn, String destPath, int... ids)
			throws Exception {
		String sql = "select seqvalue from sequence where seqname='TASK_ID'";

		int taskId = new DataUtils(conn).searchForInt(sql, null, "seqvalue") + 1;

		sql = "insert into taskInfo(taskType, taskStatus, destPath) values(?,?,?)";

		Object[] params = { "RESTORE", "PENDING", destPath };

		if (new DataUtils(conn).update(sql, params) > 0) {

			for (int id : ids) {
				sql = "insert into taskData(taskId, dataId) values(" + taskId
						+ "," + id + ")";
				new DataUtils(conn).update(sql, null);
			}
		}
		return taskId;
	}

	@Override
	public List<TaskInfo> getTask(Connection conn) throws Exception {
		String sql = "select id,taskType, taskStatus, destPath from TaskInfo";

		ResultSetHandler<List<TaskInfo>> handler = new BeanListHandler<TaskInfo>(
				TaskInfo.class);
		return new DataUtils(conn).search(sql, handler, null);
	}

	@Override
	public List<TaskData> getTaskData(Connection con, int taskId)
			throws Exception {
		return null;
	}

	@Override
	public void updataTaskStatus(Connection conn, int taskId, String status)
			throws Exception {
		String sql = "update taskinfo set taskstatus = ? where taskid = ?";

		Object[] params = { status, taskId };

		new DataUtils(conn).update(sql, params);
	}

}
