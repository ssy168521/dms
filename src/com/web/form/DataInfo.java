package com.web.form;

public class DataInfo {
	private int dataId;
	private String satellite;
	private String sensor;
	private int orbitID;
	private String productLevel;
	private int cloudPercent;
	private int productQuality;
	private java.util.Date acquisitionTime;
	private java.util.Date archiveTime;
	private int scenePath;
	private int sceneRow;
	private String archiveFile;
	private String storageFile;
	private long fileSize;
	
	public int getDataId() {
		return dataId;
	}
	public void setDataId(int dataId) {
		this.dataId = dataId;
	}
	public String getSatellite() {
		return satellite;
	}
	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}
	public String getSensor() {
		return sensor;
	}
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	public int getOrbitID() {
		return orbitID;
	}
	public void setOrbitID(int orbitID) {
		this.orbitID = orbitID;
	}
	public String getProductLevel() {
		return productLevel;
	}
	public void setProductLevel(String productLevel) {
		this.productLevel = productLevel;
	}
	public int getCloudPercent() {
		return cloudPercent;
	}
	public void setCloudPercent(int cloudPercent) {
		this.cloudPercent = cloudPercent;
	}
	public int getProductQuality() {
		return productQuality;
	}
	public void setProductQuality(int productQuality) {
		this.productQuality = productQuality;
	}
	public java.util.Date getAcquisitionTime() {
		return acquisitionTime;
	}
	public void setAcquisitionTime(java.util.Date acquisitionTime) {
		this.acquisitionTime = acquisitionTime;
	}
	public java.util.Date getArchiveTime() {
		return archiveTime;
	}
	public void setArchiveTime(java.util.Date archiveTime) {
		this.archiveTime = archiveTime;
	}
	public int getScenePath() {
		return scenePath;
	}
	public void setScenePath(int scenePath) {
		this.scenePath = scenePath;
	}
	public int getSceneRow() {
		return sceneRow;
	}
	public void setSceneRow(int sceneRow) {
		this.sceneRow = sceneRow;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getArchiveFile() {
		return archiveFile;
	}
	public void setArchiveFile(String archiveFile) {
		this.archiveFile = archiveFile;
	}
	public String getStorageFile() {
		return storageFile;
	}
	public void setStorageFile(String storageFile) {
		this.storageFile = storageFile;
	}
}
