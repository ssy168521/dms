package com.sasmac.meta;

import java.io.Serializable;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.sf.json.JSONString;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sasmac.util.MetaTableUtil;
import com.web.util.DataUtils;

public class spatialmetadata implements metadata , JSONString, Serializable{
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
	private String FileName;
	private String FilePath;
	private long fileSize;
	private String wktstring;
	private double SunAltitude;
	private double SunAzimuth;
	private double SatAzimuth;
	private double SatAltitude;
	private double SwingSatelliteAngle;
	private double RollViewingAngle;
	private double PitchViewingAngle;
	private int nRowCount;
	private int nColCount;
	private double TopLeftlong;
	private double TopLeftlat;
	private double TopRightLong;
	private double TopRightLat;
	private double BottomRightLong;
	private double BottomRightLat;
	private double BottomLeftlong;
	private double BottomLeftlat;
	private int ProductId;

	private Logger myLogger = LogManager.getLogger("mylog");

	public int getRowCount() {
		return nRowCount;
	}

	public void setRowCount(int RowCount) {
		this.nRowCount = RowCount;
	}

	public int getProductId() {
		return ProductId;
	}

	public void setProductId(int iProductId) {
		this.ProductId = iProductId;
	}

	public int getColCount() {
		return nColCount;
	}

	public void setColCount(int ColCount) {
		this.nColCount = ColCount;
	}

	public String getwktString() {
		return wktstring;
	}

	public void setwktString(String wkt) {
		this.wktstring = wkt;
	}

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

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String Filename) {
		this.FileName = Filename;
	}

	public String getFilePath() {
		return FilePath;
	}

	public void setFilePath(String FilePath) {
		this.FilePath = FilePath;
	}

	public double getSunAltitude() {
		return SunAltitude;
	}

	public void setSunAltitude(double dSunAltitude) {
		this.SunAltitude = dSunAltitude;
	}

	public double getSunAzimuth() {
		return SunAzimuth;
	}

	public void setSunAzimuth(double dSunAzimuth) {
		this.SunAzimuth = dSunAzimuth;
	}

	public double getSatAzimuth() {
		return SatAzimuth;
	}

	public void setSatAzimuth(double dSatAzimuth) {
		this.SatAzimuth = dSatAzimuth;
	}

	public double getSatAltitude() {
		return SatAltitude;
	}

	public void setSatAltitude(double dSatAltitude) {
		this.SatAltitude = dSatAltitude;
	}

	public double getSwingSatelliteAngle() {
		return SwingSatelliteAngle;
	}

	public void setSwingSatelliteAngle(double dSwingSatelliteAngle) {
		this.SwingSatelliteAngle = dSwingSatelliteAngle;
	}

	public double getRollViewingAngle() {
		return RollViewingAngle;
	}

	public void setRollViewingAngle(double dRollViewingAngle) {
		this.RollViewingAngle = dRollViewingAngle;
	}

	public double getPitchViewingAngle() {
		return PitchViewingAngle;
	}

	public void setPitchViewingAngle(double dPitchViewingAngle) {
		this.PitchViewingAngle = dPitchViewingAngle;
	}

	public double getTopLeftlong() {
		return TopLeftlong;
	}

	public void setTopLeftlong(double dTopLeftlong) {
		this.TopLeftlong = dTopLeftlong;
	}

	public double getTopLeftlat() {
		return TopLeftlat;
	}

	public void setTopLeftlat(double dTopLeftlat) {
		this.TopLeftlat = dTopLeftlat;
	}

	public double getTopRightLong() {
		return TopRightLong;
	}

	public void setTopRightLong(double dTopRightLong) {
		this.TopRightLong = dTopRightLong;
	}

	public double getTopRightLat() {
		return TopRightLat;
	}

	public void setTopRightLat(double dTopRightLat) {
		this.TopRightLat = dTopRightLat;
	}

	public double getBottomRightLong() {
		return BottomRightLong;
	}

	public void setBottomRightLong(double dBottomRightLong) {
		this.BottomRightLong = dBottomRightLong;
	}

	public double getBottomRightLat() {
		return BottomRightLat;
	}

	public void setBottomRightLat(double dBottomRightLat) {
		this.BottomRightLat = dBottomRightLat;
	}

	public double getBottomLeftlong() {
		return BottomLeftlong;
	}

	public void setBottomLeftlong(double dBottomLeftlong) {
		this.BottomLeftlong = dBottomLeftlong;
	}

	public double getBottomLeftlat() {
		return BottomLeftlat;
	}

	public void setBottomLeftlat(double dBottomLeftlat) {
		this.BottomLeftlat = dBottomLeftlat;
	}

	@Override
	public boolean insertmeta(Connection conn) throws Exception {
		if (conn == null)
			return false;
		// String TableName ="TB_ZY301_SC_"+getSensor();
		String TableName = MetaTableUtil.GetTableName(conn, satellite,
				productLevel);
		if (TableName == null) {
			myLogger.error(" can not find the meta table: " + satellite + "  "
					+ productLevel);
			return false;
		}
		if (TableName == "") {
			myLogger.error(" can not find the meta table !");
			return false;
		}

		String sql = "insert into " + TableName;
		String storagefile = getFilePath();
		storagefile = storagefile.replace("\\", "\\\\");
		sql += "(FileName, FilePath, dataid, orbitID, scenePath, sceneRow, satellite, sensor, "
				+ "acquisitionTime,ArchiveTime, productLevel, cloudPercent, productQuality, SunAltitude,SunAzimuth,SatAzimuth,SatAltitude,SwingSatelliteAngle,RollViewingAngle,PitchViewingAngle,ProductId,fileSize,shape)"
				+ " values(";
		sql += "'" + getFileName() + "',";
		sql += "'" + storagefile + "',";
		sql += getDataId() + ",";
		sql += getOrbitID() + ",";
		sql += getScenePath() + ",";
		sql += getSceneRow() + ",";
		sql += "'" + getSatellite() + "',";
		sql += "'" + getSensor() + "',";
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = formatter.format(getAcquisitionTime());
		sql += "'" + str + "',";
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		str = formatter.format(getArchiveTime());
		sql += "'" + str + "',";
		sql += "'" + getProductLevel() + "',";
		sql += getCloudPercent() + ",";
		sql += getProductQuality() + ",";
		sql += getSunAltitude() + ",";
		sql += getSunAzimuth() + ",";
		sql += getSatAzimuth() + ",";
		sql += getSatAltitude() + ",";
		sql += getSwingSatelliteAngle() + ",";
		sql += getRollViewingAngle() + ",";
		sql += getPitchViewingAngle() + ",";
		sql += getProductId() + ",";
		sql += getFileSize() + ",";
		sql += getwktString();
		sql += ")";
		/*
		 * Object[] params = {getArchiveFile(), getStorageFile(), getDataId(),
		 * getOrbitID(), getScenePath(), getSceneRow(), getSatellite(),
		 * getSensor(), getAcquisitionTime(), getProductLevel(),
		 * getCloudPercent(), getProductQuality(),
		 * getFileSize(),getSpatialWktString()};
		 * 
		 * sql+=
		 * "(archiveFile, storageFile, dataid, orbitID, scenePath, sceneRow, satellite, sensor, "
		 * +
		 * "acquisitionTime, productLevel, cloudPercent, productQuality, fileSize)"
		 * + " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		 * 
		 * Object[] params = {getArchiveFile(), getStorageFile(), getDataId(),
		 * getOrbitID(), getScenePath(), getSceneRow(), getSatellite(),
		 * getSensor(), getAcquisitionTime(), getProductLevel(),
		 * getCloudPercent(), getProductQuality(), getFileSize()};
		 */
		try {
			DataUtils datautils = new DataUtils(conn);
			int ret = datautils.update(sql);
			// int ret=datautils.update(sql, params);
			if (ret > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return false;

	}

	@Override
	public String toJSONString() {
		String json="{\"dataid\":" + this.dataId + ",\"satellite\":\"" + this.satellite 
				+"\",\"sensor\":\"" + this.sensor + "\",\"orbitID\":" + this.orbitID 
				+",\"productLevel\":\"" + this.productLevel + "\",\"cloudPercent\":" + this.cloudPercent 
				+",\"productQuality\":" + this.productQuality + ",\"acquisitionTime\":\"" + this.acquisitionTime
				+"\",\"archiveTime\":\"" + this.archiveTime + "\",\"FileName\":\"" + this.FileName
				+"\",\"FilePath\":\"" + this.FilePath + "\",\"fileSize\":" + this.fileSize
				+ "}";
		return json;
	
	}
}
