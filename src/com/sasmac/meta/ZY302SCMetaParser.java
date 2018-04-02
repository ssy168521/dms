package com.sasmac.meta;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ZY302SCMetaParser implements MetaParser {

	@Override
	public spatialmetadata ParseMeta(String strxmlFile) throws Exception {
		// TODO Auto-generated method stub

		File xmlFile = new File(strxmlFile);

		ZY302SpatialMeta m_SpatialMeta = new ZY302SpatialMeta();
		org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
		org.dom4j.Document doc = reader.read(xmlFile);

		org.dom4j.Node node = null;

		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productComponents/file/Filename");
		if (node != null) {
			String filename = node.getText();
			String str = filename.substring(0, filename.lastIndexOf("."));
			m_SpatialMeta.setFileName(str);
		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/ProductLevel");
		if (node != null) {
			String productlevel = node.getText();
			m_SpatialMeta.setProductLevel(productlevel);
		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo//ProductID");
		if (node != null) {
			Integer iProductId = Integer.parseInt(node.getText());
			m_SpatialMeta.setProductId(iProductId);
		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/SatelliteID");
		if (node != null) {
			String satellite = node.getText();
			m_SpatialMeta.setSatellite(satellite);
		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/SensorID");
		if (node != null) {
			String sensor = node.getText();
			m_SpatialMeta.setSensor(sensor);
		}

		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/AcquisitionTime");
		if (node != null) {
			String AcquisitionTime = node.getText();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = (Date) formatter.parse(AcquisitionTime);
			m_SpatialMeta.setAcquisitionTime(startDate);
		}

		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/OrbitID");
		if (node != null) {
			Integer orbitNum = Integer.parseInt(node.getText());
			m_SpatialMeta.setOrbitID(orbitNum);
		}

		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/ScenePath");
		if (node != null) {
			Integer Path = Integer.parseInt(node.getText());
			m_SpatialMeta.setScenePath(Path);
		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/SceneRow");
		if (node != null) {
			Integer Row = Integer.parseInt(node.getText());
			m_SpatialMeta.setSceneRow(Row);
		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/SunAltitude");
		if (node != null) {
			double SunAltitude = Double.parseDouble(node.getText());
			m_SpatialMeta.setSunAltitude(SunAltitude);
		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/SunAzimuth");
		if (node != null) {
			double SunAzimuth = Double.parseDouble(node.getText());
			m_SpatialMeta.setSunAzimuth(SunAzimuth);
		}

		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/SatAzimuth");
		if (node != null) {
			double SatAzimuth = Double.parseDouble(node.getText());
			m_SpatialMeta.setSatAzimuth(SatAzimuth);
		}

		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/SatAltitude");
		if (node != null) {
			double SatAltitude = Double.parseDouble(node.getText());
			m_SpatialMeta.setSatAltitude(SatAltitude);
		}

		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/SwingSatelliteAngle");
		if (node != null) {
			double SwingSatelliteAngle = Double.parseDouble(node.getText());
			m_SpatialMeta.setSwingSatelliteAngle(SwingSatelliteAngle);
		}

		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/RollViewingAngle");
		if (node != null) {
			double RollViewingAngle = Double.parseDouble(node.getText());
			m_SpatialMeta.setRollViewingAngle(RollViewingAngle);
		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/PitchViewingAngle");
		if (node != null) {
			double PitchViewingAngle = Double.parseDouble(node.getText());
			m_SpatialMeta.setPitchViewingAngle(PitchViewingAngle);
		}

		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/CloudPercent");
		if (node != null) {
			Integer CloudPercent = Integer.parseInt(node.getText());
			m_SpatialMeta.setProductQuality(CloudPercent);
		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/ProductQuality");
		if (node != null) {
			Integer ProductQuality = Integer.parseInt(node.getText());
			m_SpatialMeta.setProductQuality(ProductQuality);
		}

		String TopLeftlong = "";
		String TopLeftlat = "";
		String TopRightLong = "";
		String TopRightLat = "";
		String BottomRightLong = "";
		String BottomRightLat = "";
		String BottomLeftlong = "";
		String BottomLeftlat = "";

		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/ProductGeographicRange/LeftTopPoint/Longtitude");
		if (node != null) {
			TopLeftlong = node.getText();
			m_SpatialMeta.setTopLeftlong(Double.parseDouble(TopLeftlong));

		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/ProductGeographicRange/LeftTopPoint/Latitude");
		if (node != null) {
			TopLeftlat = node.getText();
			m_SpatialMeta.setTopLeftlat(Double.parseDouble(TopLeftlat));
		}

		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/ProductGeographicRange/RightTopPoint/Longtitude");
		if (node != null) {
			TopRightLong = node.getText();
			m_SpatialMeta.setTopRightLong(Double.parseDouble(TopRightLong));
		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/ProductGeographicRange/RightTopPoint/Latitude");
		if (node != null) {
			TopRightLat = node.getText();
			m_SpatialMeta.setTopRightLat(Double.parseDouble(TopRightLat));
		}

		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/ProductGeographicRange/RightBottomPoint/Longtitude");
		if (node != null) {
			BottomRightLong = node.getText();
			m_SpatialMeta.setBottomRightLong(Double
					.parseDouble(BottomRightLong));
		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/ProductGeographicRange/RightBottomPoint/Latitude");
		if (node != null) {
			BottomRightLat = node.getText();
			m_SpatialMeta.setBottomRightLat(Double.parseDouble(BottomRightLat));
		}

		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/ProductGeographicRange/LeftBottomPoint/Longtitude");
		if (node != null) {
			BottomLeftlong = node.getText();
			m_SpatialMeta.setBottomLeftlong(Double.parseDouble(BottomLeftlong));
		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/productInfo/ProductGeographicRange/LeftBottomPoint/Latitude");
		if (node != null) {
			BottomLeftlat = node.getText();
			m_SpatialMeta.setBottomLeftlat(Double.parseDouble(BottomLeftlat));
		}
		node = doc
				.selectSingleNode("//sensor_corrected_metadata/processInfo/ProduceID");
		if (node != null) {
			Integer dataId = Integer.parseInt(node.getText());
			m_SpatialMeta.setDataId(dataId);
		}
		String wkt = "GEOMFROMTEXT('polygon((";
		// String wkt = "polygon((";
		wkt += TopLeftlong + " ";
		wkt += TopLeftlat + ",";
		wkt += TopRightLong + " ";
		wkt += TopRightLat + ",";
		wkt += BottomRightLong + " ";
		wkt += BottomRightLat + ",";
		wkt += BottomLeftlong + " ";
		wkt += BottomLeftlat + ",";
		wkt += TopLeftlong + " ";
		// wkt += TopLeftlat + "))";
		wkt += TopLeftlat + "))')";
		m_SpatialMeta.setwktString(wkt);
		Date archiveTime = new Date();

		m_SpatialMeta.setArchiveTime(archiveTime);

		return m_SpatialMeta;
	}

}
