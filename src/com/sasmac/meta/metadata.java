package com.sasmac.meta;

import java.sql.Connection;
import java.util.Date;


public interface metadata {
	public boolean insertmeta(Connection conn) throws Exception;
	public long getFileSize() ;
	public void setFileSize(long fileSize);
	public String getFileName() ;
	public void setFileName(String storageFile);
	public void setArchiveTime(Date archiveTime) ;
}
