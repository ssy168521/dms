/**  
 * @Title: ConfigureForm.java
 * @Package com.web.form
 * @Description: TODO
 * @author zwt
 * @date 2016-12-30
 */
package com.web.form;

/**
 * ClassName: ConfigureForm 
 * @Description: 接收归档配置页面传入的信息
 * @date 2016-12-30
 */
public class ConfigureForm extends BaseForm {

	private int id;//配置项ID号，可用于修改或删除指定的id
	private String archivePath;//归档路径名称
	private String satellite;//卫星名称
	private String destPath;
	private String storageRule;//存储规则
	private String checkRule;
	private int archiveStart;
	private int archiveEnd;
	private int enable;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getArchivePath() {
		return archivePath;
	}
	public void setArchivePath(String archivePath) {
		this.archivePath = archivePath;
	}
	public String getSatellite() {
		return satellite;
	}
	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}
	public String getStorageRule() {
		return storageRule;
	}
	public void setStorageRule(String storageRule) {
		this.storageRule = storageRule;
	}
	public int getArchiveStart() {
		return archiveStart;
	}
	public void setArchiveStart(int archiveStart) {
		this.archiveStart = archiveStart;
	}
	public int getArchiveEnd() {
		return archiveEnd;
	}
	public void setArchiveEnd(int archiveEnd) {
		this.archiveEnd = archiveEnd;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public String getDestPath() {
		return destPath;
	}
	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}
	public String getCheckRule() {
		return checkRule;
	}
	public void setCheckRule(String checkRule) {
		this.checkRule = checkRule;
	}
}
