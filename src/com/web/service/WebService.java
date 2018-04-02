package com.web.service;

import java.sql.Connection;
import java.util.List;

import com.web.form.ConfigureForm;
import com.web.form.DataInfo;
import com.web.form.Staticstis;
import com.web.form.TaskData;
import com.web.form.TaskInfo;

public interface WebService {
	/**
	 * 添加系统配置
	 * 
	 * @param conn
	 * @param item
	 * @return
	 * @throws Exception
	 */
	public boolean addSysConfig(Connection conn, ConfigureForm item)
			throws Exception;

	public boolean updateSysConfig(Connection conn, ConfigureForm item)
			throws Exception;

	public boolean deleteSysConfig(Connection conn, ConfigureForm item)
			throws Exception;

	/**
	 * 获取全部系统配置信息
	 * 
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public List<ConfigureForm> getAllSysConfig(Connection conn)
			throws Exception;

	public ConfigureForm getConfigById(Connection conn, int id)
			throws Exception;

	/**
	 * 文件是否归档
	 * 
	 * @param conn
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public boolean isFileArchive(Connection conn, String fileName, long fileSize)
			throws Exception;

	public boolean isFileArchive(Connection conn, String tablename,
			String fileName) throws Exception;

	/**
	 * 获取产品表最大数据ID
	 * 
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public int getMaxDataId(Connection conn) throws Exception;

	public boolean insertProduct(Connection conn, DataInfo dataInfo)
			throws Exception;

	/**
	 * 查询数据信息
	 * 
	 * @param conn
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public List<DataInfo> getDataInfo(Connection conn, DataInfo data)
			throws Exception;

	/**
	 * 每一条数据操作完成后进行结果记录
	 * 
	 * @param conn
	 * @param satellite
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public void updateStaticts(Connection conn, String satellite, int status)
			throws Exception;

	public List<Staticstis> getStaticstisInfo(Connection conn, Staticstis s)
			throws Exception;

	public int addTask(Connection conn, String destPath, int... ids)
			throws Exception;

	public List<TaskInfo> getTask(Connection conn) throws Exception;

	public List<TaskData> getTaskData(Connection con, int taskId)
			throws Exception;

	public void updataTaskStatus(Connection conn, int taskId, String status)
			throws Exception;
}
