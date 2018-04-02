package com.web.service;

import java.sql.Connection;

import com.web.service.impl.WebServiceImpl;
import com.web.util.DbUtils;

public class DataService {
	private WebService service = new WebServiceImpl();
	
	public int downloadData(String destPath, int...ids) throws Exception {
		java.io.File fDest = new java.io.File(destPath);
		if(!fDest.exists())
			throw new Exception("Dest path not exsits.");
		
		int taskId = 0;
		
		Connection conn = DbUtils.getConnection(true);
		
		taskId = service.addTask(conn, destPath, ids);
		
		return taskId;
	}
}
