package com.sasmac.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geotools.filter.IsBetweenImpl;

import com.sasmac.jni.ImageProduce;
import com.sasmac.meta.GF1SCMetaParser;
import com.sasmac.meta.GF2SCMetaParser;
import com.sasmac.meta.MetaParser;
import com.sasmac.meta.ZY301SCMetaParser;
import com.sasmac.meta.ZY302SCMetaParser;
import com.sasmac.meta.spatialmetadata;
import com.sasmac.service.FileCopy;
import com.sasmac.service.FileCopyPortType;
import com.sasmac.util.smbAuthUtil;
import com.web.common.Constants;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
import com.web.form.ConfigureForm;
import com.web.service.WebService;
import com.web.service.impl.WebServiceImpl;
import com.web.util.AppUtil;
import com.web.util.DbUtils;
import com.web.util.FileUtil;
import com.web.util.PropertiesUtil;
import com.web.util.TarUtils;

public class ImportMeta2Database {
	String strfile;
	Connection pConn;
	List<ConfigureForm> sysConfigList;
	private Logger myLogger = LogManager.getLogger("mylog");

	// 归档模式 ：0为扫描归档 1为迁移归档

	private ConfigureForm getconfigure(String satellite) {
		if (sysConfigList == null)
			return null;
		String str = null;
		for (ConfigureForm conf : sysConfigList) {
			str = conf.getSatellite();
			if (str.compareToIgnoreCase(satellite) == 0) {
				return conf;
			}
		}
		return null;

	}

	public ImportMeta2Database(String ImageProductfile, Connection conn) {
		strfile = ImageProductfile;
		pConn = conn;

		WebService service = new WebServiceImpl();

		try {
			sysConfigList = service.getAllSysConfig(conn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			myLogger.error(e);

		}
	}

	/**
	 * xml元数据插入数据库
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean Import2Database() throws Exception {
		try {
			String path = DbUtils.class.getClassLoader().getResource("/")
					.toURI().getPath();
			PropertiesUtil propertiesUtil = new PropertiesUtil(path
					+ Constants.STR_CONF_PATH);
			// 快视图路径
			String stroverviewfilepath = propertiesUtil
					.getProperty("overviewfilepath");
			String tmpfilepath = propertiesUtil.getProperty("tmpfilepath");
			// 归档方式（0 为扫描归档， 1 为迁移归档）
			String strArchiveMode = propertiesUtil.getProperty("ArchiveMode");
			int ArchiveMode = 1;
			if (!strArchiveMode.isEmpty()) {
				ArchiveMode = Integer.parseInt(strArchiveMode);
			}
			int overviewDirIsSMBPath = Constants
					.AssertFileIsSMBFileDir(stroverviewfilepath);
			int srcDataDirIsSMBPath = Constants.AssertFileIsSMBFileDir(strfile);

			String srcFilePath = "";
			String strName = "";
			String strFilename = "";
			String destPath = tmpfilepath;
			long fileSize = 0;
			if (srcDataDirIsSMBPath == 0) {
				File fF = new File(strfile);
				strName = fF.getName();
				strFilename = strName;
				srcFilePath = fF.getParent();
				fileSize = fF.length() / 1024 / 1024;
			} else if (srcDataDirIsSMBPath == 1) {
				strfile = AppUtil.localFilePathToSMBFilePath(strfile);
				NtlmPasswordAuthentication auth = smbAuthUtil
						.getsmbAuth(strfile);
				if (auth == null) {
					myLogger.error(" smb:user password auth error! ");
					return false;
				}
				SmbFile fF = new SmbFile(strfile, auth);
				strName = fF.getName();
				fileSize = fF.getContentLength() / 1024 / 1024;
				srcFilePath = fF.getParent();
			} else if (srcDataDirIsSMBPath == 2) {
				NtlmPasswordAuthentication auth = smbAuthUtil
						.getsmbAuth(strfile);
				if (auth == null) {
					myLogger.error(" smb:user password auth error! ");
					return false;
				}
				SmbFile fF = new SmbFile(strfile, auth);
				strName = fF.getName();
				fileSize = fF.getContentLength() / 1024 / 1024;
				srcFilePath = fF.getParent();
			}
			int flag = 0;

			boolean bmatch = strName
					.matches("zy301a_[a-z]{3}_[0-9]{6}_[0-9]{6}_[0-9]{14}_[0-9]{2}_sec_[0-9]{4}_[0-9]{10}.tar");
			if (bmatch) {
				flag = 1;
			} else if (strName
					.matches("zy302a_[a-z]{3}_[0-9]{6}_[0-9]{6}_[0-9]{14}_[0-9]{2}_sec_[0-9]{4}_[0-9]{10}.tar")) {
				flag = 2;
			} else if (strName.startsWith("GF1_PMS")) {
				flag = 3;
			} else if (strName.startsWith("GF2_PMS")) {
				flag = 4;
			} else {
				myLogger.info("XML file: " + strName
						+ "  meta xml format is not support");

				return false;
			}

			strFilename = strName;
			// java.io.File fDest = new java.io.File(destPath);
			// if(!fDest.exists())fDest.mkdir();
			String windowsXMLFile = "";
			String strwindowsXMLFile = "";
			spatialmetadata m_metadata = null;

			// java.io.File fOverviewFile = new
			// java.io.File(stroverviewfilepath);

			if (flag == 1 || flag == 2) {
				MetaParser parser = null;
				if (flag == 1)
					parser = new ZY301SCMetaParser();
				else if (flag == 2)
					parser = new ZY302SCMetaParser();
				strFilename = strFilename.substring(0,
						strFilename.indexOf(".tar"));
				windowsXMLFile = strName.replace(".tar", ".xml");
				myLogger.info("start dearchive: " + windowsXMLFile);

				strwindowsXMLFile = TarUtils.dearchive(strfile, destPath,
						windowsXMLFile);
				myLogger.info("finish dearchive: " + windowsXMLFile);

				myLogger.info("start ParseMeta: " + windowsXMLFile);
				m_metadata = parser.ParseMeta(strwindowsXMLFile);
				myLogger.info("finish ParseMeta: " + windowsXMLFile);
				windowsXMLFile = windowsXMLFile.replace(".xml", "_pre.jpg");
				myLogger.info("start dearchive: " + windowsXMLFile);
				TarUtils.dearchive(strfile, stroverviewfilepath, windowsXMLFile);
				myLogger.info("finish dearchive: " + windowsXMLFile);
			} else if (flag == 3 || flag == 4) {
				MetaParser parser = null;
				if (flag == 3)
					parser = new GF1SCMetaParser();
				else if (flag == 4)
					parser = new GF2SCMetaParser();

				strFilename = strFilename.substring(0,
						strFilename.indexOf(".tar.gz"));
				if (strName.startsWith("GF1_PMS1"))
					windowsXMLFile = strName.replace(".tar.gz", "-MSS1.xml");
				else if (strName.startsWith("GF1_PMS2"))
					windowsXMLFile = strName.replace(".tar.gz", "-MSS2.xml");
				else if (strName.startsWith("GF2_PMS1"))
					windowsXMLFile = strName.replace(".tar.gz", "-MSS1.xml");
				else if (strName.startsWith("GF2_PMS2"))
					windowsXMLFile = strName.replace(".tar.gz", "-MSS2.xml");
				else {
					myLogger.info(strName + "  meta file name no matches ! ");
					return false;
				}
				myLogger.info("start dearchive: " + windowsXMLFile);
				if (!TarUtils.unTarGZsinglefile(strfile, destPath,
						windowsXMLFile)) {
					myLogger.info("XML file: " + windowsXMLFile
							+ " untar error");
					return false;
				}
				myLogger.info("   finish dearchive: " + windowsXMLFile);
				strwindowsXMLFile = destPath + windowsXMLFile;
				windowsXMLFile = windowsXMLFile.replace(".xml", ".jpg");

				myLogger.info("start dearchive: " + windowsXMLFile);
				if (!TarUtils.unTarGZsinglefile(strfile, stroverviewfilepath,
						windowsXMLFile)) {
					myLogger.info("jpg file: " + windowsXMLFile
							+ " untar error");
					return false;
				}
				myLogger.info("finish dearchive: " + windowsXMLFile);

				myLogger.info("start parser: " + strwindowsXMLFile);
				m_metadata = parser.ParseMeta(strwindowsXMLFile);
				myLogger.info("finish parser: " + strwindowsXMLFile);
			} else {

			}

			if (m_metadata == null) {
				myLogger.info(windowsXMLFile + "  meta file parser error ! ");
				return false;
			}

			String StoragePath = "";
			ConfigureForm conf = null;
			if (ArchiveMode == 1) // 迁移归档
			{
				conf = getconfigure(m_metadata.getSatellite());
				if (conf == null) {
					myLogger.info(" no exist configure  file: "
							+ m_metadata.getSatellite());
					return false;
				}
                //strStoragePath 为目标路径，从sysconfig中读取
				String strStoragePath = conf.getDestPath();
				if (strStoragePath.isEmpty()) {
					myLogger.info(m_metadata.getSatellite()
							+ "archive target path is no set ");
					return false;
				}

				StoragePath = genertateStoragePath(strStoragePath, m_metadata);
			} else // 扫描归档
			{
				StoragePath = srcFilePath;
			}

			double[] pointlist = new double[8];
			pointlist[0] = m_metadata.getTopLeftlong();
			pointlist[1] = m_metadata.getTopLeftlat();
			pointlist[2] = m_metadata.getTopRightLong();
			pointlist[3] = m_metadata.getTopRightLat();
			pointlist[4] = m_metadata.getBottomRightLong();
			pointlist[5] = m_metadata.getBottomRightLat();
			pointlist[6] = m_metadata.getBottomLeftlong();
			pointlist[7] = m_metadata.getBottomLeftlat();
			/**
			 * strjpgfile 为源图像所在路径
			 */
			String strjpgfile = stroverviewfilepath + windowsXMLFile;
			File jpgfile = new File(strjpgfile);

			String worldfilePath = strjpgfile.replace(".jpg", ".jgw");
			try {
				Iterator<ImageReader> readers = ImageIO
						.getImageReadersByFormatName("jpg");
				ImageReader reader = (ImageReader) readers.next();
				ImageInputStream iis = ImageIO.createImageInputStream(jpgfile);
				reader.setInput(iis, true);
				generatejgwfile(reader.getHeight(0), reader.getWidth(0),
						pointlist, worldfilePath);
				iis.close();

			} catch (IOException e) {
				e.printStackTrace();
				myLogger.error(e);
			}

			String OverviewStoragePath = genertateStoragePath(
					stroverviewfilepath, m_metadata);

			TarUtils.fileProber(OverviewStoragePath);

			myLogger.info("   start ImageRectify overiew-png: " + strFilename
					+ ".png");
			ImageProduce imgprodu = new ImageProduce();
			boolean res = false;
			// 图像重采样
			res = imgprodu
					.ImageRectify(stroverviewfilepath + windowsXMLFile,
							OverviewStoragePath + File.separator + strFilename
									+ ".png", 256, 256);
			if (!res) {
				myLogger.info(windowsXMLFile + " :png overview build error !");
			} else {
				myLogger.info("finish ImageRectify overiew-png: " + strFilename
						+ ".png");
				FileUtil.deletefile(stroverviewfilepath + windowsXMLFile);//删除已有jpg快视图
				FileUtil.deletefile(worldfilePath);   //删除已有jgw快视图

			}

			m_metadata.setFileName(strFilename);
			Date archiveTime = new Date();
			m_metadata.setArchiveTime(archiveTime);

			m_metadata.setFileSize(fileSize);

			if (ArchiveMode == 1) // 迁移归档
			{
				com.sasmac.util.AppConfUtil util = com.sasmac.util.AppConfUtil
						.getInstance();

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				java.util.Date date = new java.util.Date();
				String str = sdf.format(date);
				myLogger.info("start copy archive data: " + strFilename+ " time:" + str);
				
//				if(!new File(StoragePath).exists()){  //判断文件是否存在，没有就新建一个
//					new File(StoragePath).mkdir();
//				}
//				fileArchive(srcFilePath+"\\"+strName,StoragePath+"\\"+strName);//文件拷贝

				util.SetAppconFile("appconf.xml");
				String filecopywebservice = util.getProperty("filecopy_ws");
			
				if (filecopywebservice == null ||filecopywebservice.isEmpty()) {
					myLogger.info("file copy -mappedBuffer mode !");
					
					TarUtils.fileProber(StoragePath);
					int ret = mappedBuffer(strfile, StoragePath +"\\"+ strName);
				} else {
					myLogger.info("file copy -file copy webservice mode !");
					FileCopy s = new FileCopy(new java.net.URL(
							filecopywebservice));
					FileCopyPortType server = s.getFileCopyPort();
					// 转换到服务器本地盘符
					
					String serverStoragePath = AppUtil
							.localFilePathToSMBFilePath(StoragePath);
					strfile = AppUtil.localFilePathToSMBFilePath(strfile);

					
					int ret = server.filetransaction(strfile, serverStoragePath
							+"\\"+ strName);
				}

				java.util.Date date1 = new java.util.Date();
				str = sdf.format(date1);
				myLogger.info("finish copy archive data: " + strFilename
						+ " time:" + str);
				m_metadata.setFilePath(StoragePath);
			} else {
				m_metadata.setFilePath(srcFilePath);
			}

			myLogger.info("start insert meta to database ");
			boolean b = m_metadata.insertmeta(pConn);
			myLogger.info("finish  insert meta to database");
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			myLogger.error(e);
			return false;
		} finally {

		}
	}

	public int mappedBuffer(String srcfile, String destFile) throws IOException {
	
		FileChannel read = new FileInputStream(srcfile).getChannel();
		FileChannel writer = new RandomAccessFile(destFile, "rw").getChannel();
		long i = 0;
		long size = read.size() / 30;
		ByteBuffer bb, cc = null;
		while (i < read.size() && (read.size() - i) > size) {
			bb = read.map(FileChannel.MapMode.READ_ONLY, i, size);
			cc = writer.map(FileChannel.MapMode.READ_WRITE, i, size);
			cc.put(bb);
			i += size;
			bb.clear();
			cc.clear();
		}
		bb = read.map(FileChannel.MapMode.READ_ONLY, i, read.size() - i);
		cc.put(bb);
		bb.clear();
		cc.clear();
		read.close();
		writer.close();
		return 1;
	}

	public String genertateStoragePath(String destpath,
			spatialmetadata spatialmeta) {

		if (spatialmeta == null)
			return "";
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String photoDate = formatter.format(spatialmeta.getAcquisitionTime());
		String str = destpath;
		if (!destpath.endsWith(File.separator)) {
			str += File.separator;
		}

		String storagePath = str + spatialmeta.getSatellite() + File.separator
				+ spatialmeta.getProductLevel() + File.separator + photoDate
				+ File.separator
				+ String.format("%06d", spatialmeta.getOrbitID())
				+ File.separator + spatialmeta.getSensor();
		return storagePath;
	}

	/**
	 * 文件拷贝
	 * @param sourceFile 源文件
	 * @param destpath  目标文件
	 * @throws Exception
	 */
	private void fileArchive(String sourceFile, String destpath)
			throws Exception {
		FileUtils.copyFileToDirectory(new java.io.File(sourceFile), new java.io.File(destpath));
	}

	private boolean generatejgwfile(int nRow, int nCol, double[] pointlist,
			String worldfilePath) {
		// pointlis 为左上角点经纬度坐标，顺时针依次赋㽍
		boolean bret = false;
		int begin = 0;

		double xMid = (pointlist[0] + pointlist[2] + pointlist[4] + pointlist[6]) / 4;
		double yMid = (pointlist[1] + pointlist[3] + pointlist[5] + pointlist[7]) / 4;

		for (int i = 0; i < 4; i++) {

			if (pointlist[2 * i] < xMid && pointlist[2 * i + 1] > yMid) {
				begin = i;
				break;
			}
		}

		int i = 2 * begin;

		double x1 = pointlist[i];
		double y1 = pointlist[i + 1];
		double x2 = 0;
		if (i + 2 > 7)
			x2 = pointlist[i + 2 - 8];
		else
			x2 = pointlist[i + 2];

		double y2 = 0;
		if (i + 3 > 7)
			y2 = pointlist[i + 3 - 8];
		else
			y2 = pointlist[i + 3];

		double x3 = pointlist[i + 4];
		if (i + 4 > 7)
			x3 = pointlist[i + 4 - 8];
		double y3 = 0;
		if (i + 5 > 7)
			y3 = pointlist[i + 5 - 8];
		else
			y3 = pointlist[i + 5];
		double x4 = 0;
		if (i + 6 > 7)
			x4 = pointlist[i + 6 - 8];
		else
			x4 = pointlist[i + 6];
		double y4 = 0;
		if (i + 7 > 7)
			y4 = pointlist[i + 7 - 8];
		else
			y4 = pointlist[i + 7];

		double line1Value = (x2 - x1) / nCol;
		// double line2Value = (y3 - y4) / rowCount;
		// double line3Value = (x3 - x2) / ColumnCount;
		double line2Value = (y3 - y4) / nCol;
		double line3Value = (x3 - x2) / nRow;

		double line4Value = (y4 - y1) / nRow;
		double line5Value = x1;
		double line6Value = y1;

		FileWriter fileStream = null;
		try {
			File pFile = new File(worldfilePath);
			if (pFile.exists())
				pFile.delete();
			fileStream = new FileWriter(worldfilePath, true);

			fileStream.write(Double.toString(line1Value) + "\r\n");
			fileStream.write(Double.toString(line2Value) + "\r\n");
			fileStream.write(Double.toString(line3Value) + "\r\n");
			fileStream.write(Double.toString(line4Value) + "\r\n");
			fileStream.write(Double.toString(line5Value) + "\r\n");
			fileStream.write(Double.toString(line6Value) + "\r\n");
		} catch (Exception e) {
		}
		try {
			if (fileStream != null) {
				fileStream.close();
			}
		} catch (Exception e) {
		}

		return bret;
	}
}
