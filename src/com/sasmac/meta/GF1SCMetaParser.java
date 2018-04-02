package com.sasmac.meta;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GF1SCMetaParser implements MetaParser {

	@Override
	public spatialmetadata ParseMeta(String strxmlFile) throws Exception {
		// TODO Auto-generated method stub

		File xmlFile = new File(strxmlFile);
		if (!xmlFile.exists())
			return null;
		GF1SpatialMeta m_SpatialMeta = new GF1SpatialMeta();
		org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
		org.dom4j.Document doc = reader.read(xmlFile);

		org.dom4j.Node node = null;

		node = doc.selectSingleNode("//ProductMetaData/SatelliteID");
		if (node != null) {
			String satellite = node.getText();
			m_SpatialMeta.setSatellite(satellite);
		}
		node = doc.selectSingleNode("//ProductMetaData/SensorID");
		if (node != null) {
			String sensor = node.getText();
			m_SpatialMeta.setSensor(sensor);
		}
		node = doc.selectSingleNode("//ProductMetaData/ProductID");
		if (node != null) {
			Integer iProductId = Integer.parseInt(node.getText());
			m_SpatialMeta.setProductId(iProductId);
		}
		node = doc.selectSingleNode("//ProductMetaData/ReceiveTime");
		if (node != null) {
			String AcquisitionTime = node.getText();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = (Date) formatter.parse(AcquisitionTime);
			m_SpatialMeta.setAcquisitionTime(startDate);
		}
		node = doc.selectSingleNode("//ProductMetaData/OrbitID");
		if (node != null) {
			Integer orbitNum = Integer.parseInt(node.getText());
			m_SpatialMeta.setOrbitID(orbitNum);
		}
		node = doc.selectSingleNode("//ProductMetaData/ProductID");
		if (node != null) {
			Integer dataId = Integer.parseInt(node.getText());
			m_SpatialMeta.setDataId(dataId);
		}
		node = doc.selectSingleNode("//ProductMetaData/ProductLevel");
		if (node != null) {
			String productlevel = node.getText();
			m_SpatialMeta.setProductLevel(productlevel);
		}

		node = doc.selectSingleNode("//ProductMetaData/ScenePath");
		if (node != null) {
			Integer Path = Integer.parseInt(node.getText());
			m_SpatialMeta.setScenePath(Path);
		}
		node = doc.selectSingleNode("//ProductMetaData/SceneRow");
		if (node != null) {
			Integer Row = Integer.parseInt(node.getText());
			m_SpatialMeta.setSceneRow(Row);
		}
		node = doc.selectSingleNode("//ProductMetaData/CloudPercent");
		if (node != null) {
			Integer CloudPercent = Integer.parseInt(node.getText());
			m_SpatialMeta.setProductQuality(CloudPercent);
		}

		node = doc.selectSingleNode("//ProductMetaData/QualityInfo");
		if (node != null) {
			String strQuality = node.getText();
			if (!strQuality.isEmpty()) {
				Integer ProductQuality = Integer.parseInt(node.getText());
				m_SpatialMeta.setProductQuality(ProductQuality);
			}
		}

		String TopLeftlong = "";
		String TopLeftlat = "";
		String TopRightLong = "";
		String TopRightLat = "";
		String BottomRightLong = "";
		String BottomRightLat = "";
		String BottomLeftlong = "";
		String BottomLeftlat = "";

		node = doc.selectSingleNode("//ProductMetaData/TopLeftLatitude");
		if (node != null) {
			TopLeftlat = node.getText();
			m_SpatialMeta.setTopLeftlat(Double.parseDouble(TopLeftlat));
		}

		node = doc.selectSingleNode("//ProductMetaData/TopLeftLongitude");
		if (node != null) {
			TopLeftlong = node.getText();
			m_SpatialMeta.setTopLeftlong(Double.parseDouble(TopLeftlong));
		}

		node = doc.selectSingleNode("//ProductMetaData/TopRightLatitude");
		if (node != null) {
			TopRightLat = node.getText();
			m_SpatialMeta.setTopRightLat(Double.parseDouble(TopRightLat));
		}

		node = doc.selectSingleNode("//ProductMetaData/TopRightLongitude");
		if (node != null) {
			TopRightLong = node.getText();
			m_SpatialMeta.setTopRightLong(Double.parseDouble(TopRightLong));
		}

		node = doc.selectSingleNode("//ProductMetaData/BottomRightLongitude");
		if (node != null) {
			BottomRightLong = node.getText();
			m_SpatialMeta.setBottomRightLong(Double
					.parseDouble(BottomRightLong));
		}
		node = doc.selectSingleNode("//ProductMetaData/BottomRightLatitude");
		if (node != null) {
			BottomRightLat = node.getText();
			m_SpatialMeta.setBottomRightLat(Double.parseDouble(BottomRightLat));
		}

		node = doc.selectSingleNode("//ProductMetaData/BottomLeftLongitude");
		if (node != null) {

			BottomLeftlong = node.getText();
			m_SpatialMeta.setBottomLeftlong(Double.parseDouble(BottomLeftlong));
		}
		node = doc.selectSingleNode("//ProductMetaData/BottomLeftLatitude");
		if (node != null) {
			BottomLeftlat = node.getText();
			m_SpatialMeta.setBottomLeftlat(Double.parseDouble(BottomLeftlat));
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
