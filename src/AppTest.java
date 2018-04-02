import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

import com.web.common.Constants;
import com.web.form.ConfigureForm;
import com.web.service.WebService;
import com.web.service.impl.WebServiceImpl;
import com.web.thread.*;
import com.web.util.DbUtils;

public class AppTest {
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		Connection conn=null;
		try {
			conn = DbUtils.getConnection(true);
			if(conn == null) return ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebService service = new WebServiceImpl();

		List<ConfigureForm> sysConfigList = service.getAllSysConfig(conn);
		String ArchivePath ="d:\\test";
		if(sysConfigList == null) return;
		/*
		for(ConfigureForm conf : sysConfigList) {
			satellite = conf.getSatellite();
			
			Log.println("Get system configration,satellite is: " + satellite);
			
			if(Constants.getThreadStatus(satellite) == Constants.THREAD_NO_ACTIVE) {
				Log.println("Start archive thread,satellite: " + satellite);
				Constants.setThreadStatus(satellite, Constants.THREAD_STATUS_RUNNING);
				
				MyHmArchiveThread m = new MyHmArchiveThread(conf,ArchivePath);
				new Thread(m).start();
			} else {
				Log.println("The thread is running now,satellite: " + satellite);
			}
		}
    	*/
		
		MyHmArchiveThread m = new MyHmArchiveThread(ArchivePath);
		new Thread(m).start();
		
	
	}
}
