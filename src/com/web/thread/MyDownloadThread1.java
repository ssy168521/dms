package com.web.thread;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import jcifs.smb.SmbException;

import com.sasmac.service.FileCopy;
import com.sasmac.service.FileCopyPortType;
import com.sasmac.servlet.Loadoverview;
import com.web.common.Constants;
import com.web.common.THREADSTATUS;
import com.web.util.AppUtil;
import com.web.util.DbUtils;
import com.web.util.FileUtil;
import com.web.util.PropertiesUtil;
import com.web.util.TarUtils;

public class MyDownloadThread1 extends BaseThread implements Runnable {
	// private Connection conn = null;
	// private WebService service = new WebServiceImpl();
	String[] srcFileArr;
	String strDestpath;

	public MyDownloadThread1(List<String> SrcFileArr, String Destpath) {
		super("download", THREADSTATUS.THREAD_STATUS_UNKNOWN.ordinal(), "数据下载",
				new Date(), null, 0, "");
		int n = SrcFileArr.size();
		srcFileArr = new String[n];
		SrcFileArr.toArray(srcFileArr);
		strDestpath = Destpath;

	}

	@Override
	public void run() {
		String conffilepath = "";
		try {
			conffilepath = Loadoverview.class.getClassLoader().getResource("/")
					.toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		PropertiesUtil propertiesUtil = new PropertiesUtil(conffilepath
				+ Constants.STR_CONF_PATH);
		String strDestpathconf = propertiesUtil.getProperty("downloadpath");
		if (strDestpath == "")
			strDestpath = strDestpathconf;
		if (srcFileArr == null)
			return;
		int ncount = srcFileArr.length;
		if (ncount <= 0)
			return;
		if (strDestpath.isEmpty())
			return;

		strDestpath = strDestpath + File.separator + getTaskName();
		try {
			TarUtils.fileProber(strDestpath);
		} catch (SmbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String srcFile = "";
		try {
			setTaskStatus(THREADSTATUS.THREAD_STATUS_RUNNING.ordinal());
			for (int i = 0; i < ncount; i++) {
				if (bStopThread) {
					setTaskStatus(THREADSTATUS.THREAD_STATUS_STOPED.ordinal());
					setTaskEndTime(new Date());
					break;//
				}
				srcFile = srcFileArr[i];

				com.sasmac.util.AppConfUtil util = com.sasmac.util.AppConfUtil
						.getInstance();
				util.SetAppconFile("appconf.xml");
				String filecopywebservice = util.getProperty("filecopy_ws");
				if (!filecopywebservice.isEmpty()) {
					FileCopy s = new FileCopy(new java.net.URL(
							filecopywebservice));
					FileCopyPortType server = s.getFileCopyPort();

					srcFile = AppUtil.localFilePathToSMBFilePath(srcFile);
					int ret = server.filetransaction(srcFile, strDestpath);
					if (ret != 0) {

					}
				} else {
					FileUtil.fileCopy(srcFile, strDestpath);
				}

				setTaskProgress((int) Math.rint((float) (i + 1)
						/ (float) ncount * 100));

			}

			if (!bStopThread) {
				setTaskStatus(THREADSTATUS.THREAD_STATUS_FINISHED.ordinal());
				setTaskEndTime(new Date());
				setTaskMarkinfo(strDestpathconf + " "
						+ Integer.toString(ncount) + " files download");
				setTaskProgress(100);
			} else {
				setTaskStatus(THREADSTATUS.THREAD_STATUS_STOPED.ordinal());
				setTaskEndTime(new Date());
				setTaskMarkinfo(strDestpathconf + " :download task is stoped  ");
			}
			Connection conn = DbUtils.getConnection();
			PrintTaskInfo(conn);
			DbUtils.completeAndCloseQuietlyTransaction(conn);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// DbUtils.closeQuietlyConnection(conn);
		}
	}
}
