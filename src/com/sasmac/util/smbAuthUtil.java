package com.sasmac.util;

import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;

import com.web.common.Constants;
import com.web.util.XMLUtil;

public class smbAuthUtil {
	public static NtlmPasswordAuthentication getsmbAuth(String filepath) {
		if (filepath.isEmpty())
			return null;
		String path = "";
		try {
			path = smbAuthUtil.class.getClassLoader().getResource("/").toURI()
					.getPath();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HashMap<String, String> paramap = null;
		try {
			if (filepath.contains("smb://")) {
				paramap = XMLUtil.getMaplistBySmbPath(path
						+ Constants.STR_MAPDISK_CONF_PATH, filepath);

			} else {
				String Diskroot = filepath.substring(0, filepath.indexOf(":"));
				String str = "";
				paramap = XMLUtil.getMaplistByDiskname(path
						+ Constants.STR_MAPDISK_CONF_PATH, Diskroot);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int n = paramap.size();
		if (n <= 0)
			return null;
		String SMBMapPath = "";
		String username = "";
		String password = "";
		String domain = "";
		Iterator iter = paramap.entrySet().iterator();
		while (iter.hasNext()) {
			HashMap.Entry<String, String> entry = (HashMap.Entry<String, String>) iter
					.next();
			String key = (String) entry.getKey();
			if (key.compareToIgnoreCase("path") == 0) {
				SMBMapPath = (String) entry.getValue();
			} else if (key.compareToIgnoreCase("username") == 0) {
				username = (String) entry.getValue();
			} else if (key.compareToIgnoreCase("passwd") == 0) {
				password = (String) entry.getValue();
			} else if (key.compareToIgnoreCase("domain") == 0) {
				domain = (String) entry.getValue();
			}
		}

		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
				domain, username, password);
		try {
			InetAddress ip = InetAddress.getByName(domain);
			UniAddress myDomain = new UniAddress(ip);
			SmbSession.logon(myDomain, auth);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return auth;
	}
}
