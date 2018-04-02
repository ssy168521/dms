package com.web.util;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;

import com.web.common.Constants;

public class AppUtil {
	public static String localFilePathToSMBFilePath(String strFilePath) {
		if (strFilePath == null)
			return "";
		if (strFilePath.isEmpty())
			return "";
		String Diskroot = strFilePath.substring(0, strFilePath.indexOf(":"));
		if (Diskroot.compareToIgnoreCase("smb") == 0)
			return strFilePath;
		if (Diskroot.isEmpty())
			return "";
		String SmbMapFilePath = getSMBFilePath(Diskroot);
		return CombinePath(SmbMapFilePath, strFilePath);
	}

	public static String localFileNameToSMBFileName(String strFilePath) {
		if (strFilePath == null)
			return "";
		if (strFilePath.isEmpty())
			return "";
		String Diskroot = strFilePath.substring(0, strFilePath.indexOf(":"));
		if (Diskroot.isEmpty())
			return "";
		String SmbMapFilePath = getSMBFilePath(Diskroot);
		return CombineSMBFile(SmbMapFilePath, strFilePath);
	}

	// 获取本地映射的smb文件路径
	public static String getSMBFilePath(String localDiskroot) {
		if (localDiskroot == null)
			return "";
		if (localDiskroot.isEmpty())
			return "";
		String SMBMapPath = "";
		String path;
		try {
			path = Constants.class.getClassLoader().getResource("/").toURI()
					.getPath();
			HashMap<String, String> paramap;

			paramap = XMLUtil.getMaplistByDiskname(path
					+ Constants.STR_MAPDISK_CONF_PATH, localDiskroot);

			Iterator iter = paramap.entrySet().iterator();
			while (iter.hasNext()) {
				HashMap.Entry<String, String> entry = (HashMap.Entry<String, String>) iter
						.next();
				String key = (String) entry.getKey();
				if (key.compareToIgnoreCase("path") == 0) {
					SMBMapPath = (String) entry.getValue();
					return SMBMapPath;
				}
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";

	}

	public static String CombinePath(String SMBMapPath, String strLocaldir) {
		strLocaldir = strLocaldir.replace("\\", "/");
		int ipos = 2;
		if (SMBMapPath.endsWith("/")) {
			ipos = 3;
		}
		String RemoteFilePath = SMBMapPath + strLocaldir.substring(ipos);
		if (!RemoteFilePath.endsWith("/")) {
			RemoteFilePath += "/";
		}
		// StringEscapeUtils.escapeJavaScript(RemoteFilePath);
		return RemoteFilePath;
	}

	public static String CombineSMBFile(String SMBMapPath, String strLocaldir) {
		strLocaldir = strLocaldir.replace("\\", "/");
		int pos = 2;
		if (SMBMapPath.endsWith("/")) {
			pos = 3;
		}
		String RemoteFilePath = SMBMapPath + strLocaldir.substring(pos);
		// StringEscapeUtils.escapeJavaScript(RemoteFilePath);
		return RemoteFilePath;
	}
}
