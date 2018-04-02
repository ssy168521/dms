package zy3dms.test;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import com.sasmac.util.smbAuthUtil;
import com.web.common.Constants;

public class SmbTest {

	public static void main(String[] args) {
		try {
			String SmbFilepath = "smb://172.20.3.241/Multimedia/test/";

			NtlmPasswordAuthentication auth = smbAuthUtil
					.getsmbAuth(SmbFilepath);
			if (auth == null) {
				Constants.WriteLog(" smb:user password auth error! ");
				return;
			}

			SmbFile smbf = new SmbFile(SmbFilepath, auth);

			String[] all = smbf.list();

			for (String s : all) {
				System.out.println(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
