package com.web.thread;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.web.form.TaskData;
import com.web.form.TaskInfo;
import com.web.service.WebService;
import com.web.service.impl.WebServiceImpl;
import com.web.util.DbUtils;

public class MyDownloadThread implements Runnable {
	private Connection conn = null;
	private WebService service = new WebServiceImpl();

	@Override
	public void run() {
		while(true) {
			try {
				conn = DbUtils.getConnection(true);
				
				List<TaskInfo> list = service.getTask(conn);
				
				if(list != null) {
					for(TaskInfo task : list) {
						int taskId = 1;//task.getTaskId();
						//String destPath = task.getDestPath();
						
						List<TaskData> lData = service.getTaskData(conn, taskId);
						
						service.updataTaskStatus(conn, taskId, "EXECUTING");
						
						for(TaskData data : lData) {
				//			FileUtils.copyFileToDirectory(new java.io.File(data.getFileName()), new java.io.File(destPath));
						}
						
						service.updataTaskStatus(conn, taskId, "COMPLETED");
						
						/*
						 * 数据下载完成后的反馈
						 */
					}
				}
				
				Thread.sleep(1000 * 60);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DbUtils.closeQuietlyConnection(conn);
			}
		}
	}

}
