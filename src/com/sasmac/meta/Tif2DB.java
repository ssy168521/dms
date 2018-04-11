package com.sasmac.meta;
/**
 * tif2db用于接收xml节点内或geotiff数据内容、NodePathParser.java
 * @author Administrator
 * @ClassName:Tif2DB
 * @Version 版本
 * @ModifiedBy 修改人
 * @date 2018年3月20日 下午4:19:15
 */
public class Tif2DB {
	private String FileName; //  文件名
	private String FilePath; //  路径
	private String satellite;//  卫星
	private int FileSize; //    影像大小
	private int BandsNumber; //  影像波段
	private String CellSizexy;// 像元大小
	private double Columns;//    分辨率列
	private double Rows;//       行
	private double ExtentRight;//影像右边界
	private double ExtentLeft;// 左边界
	private double ExtentTop;//  上边界
	private double ExtentBottom;//下边界
	private String DataType;//栅格类型
	private String wktString;//  范围适量
	private String acquisitionTime;// 获得点
	private String archiveTime;  //  归档时间点
	private long dataid;       //数据id，唯一标识
	private int orbitID;      // 轨道ID号
	private int scenePath;    //场景轨道
	private int sceneRow;     //场景行
	private String sensor;    //传感器
	private int productQuality;   //产品质量
	private long productid;        //产品id
	private int productLevel;     //产品级别
	
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
	public int getFileSize() {
		return FileSize;
	}
	public void setFileSize(int fileSize) {
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
	public String getWktString() {
		return wktString;
	}
	public void setWktString(String wktString) {
		this.wktString = wktString;
	}
	public String getCellSizexy() {
		return CellSizexy;
	}
	public void setCellSizexy(String cellSizexy) {
		CellSizexy = cellSizexy;
	}
	public String getSatellite() {
		return satellite;
	}
	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}
	public String getDataType() {
		return DataType;
	}
	public void setDataType(String dataType) {
		DataType = dataType;
	}
	public long getDataid() {
		return dataid;
	}
	public void setDataid(long dataid2) {
		this.dataid = dataid2;
	}
	public int getOrbitID() {
		return orbitID;
	}
	public void setOrbitID(int orbitID) {
		this.orbitID = orbitID;
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
	public String getSensor() {
		return sensor;
	}
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	public int getProductQuality() {
		return productQuality;
	}
	public void setProductQuality(int productQuality) {
		this.productQuality = productQuality;
	}
	public long getProductid() {
		return productid;
	}
	public void setProductid(long dataid2) {
		this.productid = dataid2;
	}
	public int getProductLevel() {
		return productLevel;
	}
	public void setProductLevel(int productLevel) {
		this.productLevel = productLevel;
	}
	public String getAcquisitionTime() {
		return acquisitionTime;
	}
	public void setAcquisitionTime(String acquisitionTime) {
		this.acquisitionTime = acquisitionTime;
	}
	public String getArchiveTime() {
		return archiveTime;
	}
	public void setArchiveTime(String archiveTime) {
		this.archiveTime = archiveTime;
	}
	

}
