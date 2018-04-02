package com.web.thread;

public class Tif2DB {
	private String FileName; //  文件名
	private String FilePath; //  路径
	private String Satellite;//  卫星
	private long FileSize; //    影像大小
	private int BandsNumber; //  影像波段
	private String CellSizexy;// 像元大小
	private double Columns;//    分辨率列
	private double Rows;//       行
	private double ExtentRight;//影像右边界
	private double ExtentLeft;// 左边界
	private double ExtentTop;//  上边界
	private double ExtentBottom;//下边界
	private String RasterFormat;//栅格类型
	private String wktString;//  范围适量
	private java.util.Date acquisitionTime;// 获得点
	private java.util.Date archiveTime;  //  归档时间点
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public String getFilePath() {
		return FilePath;
	}
	public void setFilePath(String filePath) {
		FilePath = filePath;
	}
	public String getSatellite() {
		return Satellite;
	}
	public void setSatellite(String satellite) {
		Satellite = satellite;
	}
	public long getFileSize() {
		return FileSize;
	}
	public void setFileSize(long fileSize) {
		FileSize = fileSize;
	}
	public int getBandsNumber() {
		return BandsNumber;
	}
	public void setBandsNumber(int bandsNumber) {
		BandsNumber = bandsNumber;
	}

	public double getColumns() {
		return Columns;
	}
	public void setColumns(double columns) {
		Columns = columns;
	}
	public double getRows() {
		return Rows;
	}
	public void setRows(double rows) {
		Rows = rows;
	}
	public double getExtentRight() {
		return ExtentRight;
	}
	public void setExtentRight(double extentRight) {
		ExtentRight = extentRight;
	}
	public double getExtentLeft() {
		return ExtentLeft;
	}
	public void setExtentLeft(double extentLeft) {
		ExtentLeft = extentLeft;
	}
	public double getExtentTop() {
		return ExtentTop;
	}
	public void setExtentTop(double extentTop) {
		ExtentTop = extentTop;
	}
	public double getExtentBottom() {
		return ExtentBottom;
	}
	public void setExtentBottom(double extentBottom) {
		ExtentBottom = extentBottom;
	}
	public String getRasterFormat() {
		return RasterFormat;
	}
	public void setRasterFormat(String rasterFormat) {
		RasterFormat = rasterFormat;
	}
	public String getWktString() {
		return wktString;
	}
	public void setWktString(String wktString) {
		this.wktString = wktString;
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
	public String getCellSizexy() {
		return CellSizexy;
	}
	public void setCellSizexy(String cellSizexy) {
		CellSizexy = cellSizexy;
	}
	

}
