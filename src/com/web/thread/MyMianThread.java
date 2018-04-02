package com.web.thread;

import java.sql.Connection;
import java.util.List;

import com.web.common.Constants;
import com.web.form.ConfigureForm;
import com.web.service.WebService;
import com.web.service.impl.WebServiceImpl;
import com.web.util.DbUtils;
import com.web.util.Log;

/**
 * 
 * @author Administrator
 *
 */
public class MyMianThread implements Runnable {
	private WebService service = new WebServiceImpl();
	
	private Connection conn    = null;
	
	private String satellite;

	@Override
	public void run() {
		
		while(true) {
			try {
				conn = DbUtils.getConnection(true);
				if(conn ==null ) break;
				
				List<ConfigureForm> sysConfigList = service.getAllSysConfig(conn);
				
				if(sysConfigList != null) {
					for(ConfigureForm conf : sysConfigList) {
						satellite = conf.getSatellite();
						
					/*	Log.println("Get system configration,satellite is: " + satellite);
						
						if(Constants.getThreadStatus(satellite) == Constants.THREAD_NO_ACTIVE) {
							Log.println("Start archive thread,satellite: " + satellite);
							Constants.setThreadStatus(satellite, Constants.THREAD_STATUS_RUNNING);
							
							//MyArchiveThread m = new MyArchiveThread(conf);
							//new Thread(m).start();
						} else {
							Log.println("The thread is running now,satellite: " + satellite);
						}*/
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					Thread.sleep(1000 * 60 * 5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
