package com.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sasmac.util.smbAuthUtil;
import com.web.common.Constants;

public class FileUtil {

	public static int BUFFER_SIZE = 1024 * 1024 * 8;
	private static Logger myLogger = LogManager.getLogger("mylog");

	public static boolean deletefile(String sourceFile) throws Exception {
		int flag = Constants.AssertFileIsSMBFileDir(sourceFile);
		if (flag == 0) {
			File localFile = new File(sourceFile);
			if (localFile.exists()) {
				localFile.delete();
			}

		} else {

			NtlmPasswordAuthentication auth = smbAuthUtil
					.getsmbAuth(sourceFile);
			if (auth == null) {
				myLogger.error(" smb:user password auth error! ");
				return false;
			}

			try {
				if (flag == 1)
					sourceFile = AppUtil.localFileNameToSMBFileName(sourceFile);

				SmbFile localFile = new SmbFile(sourceFile, auth);
				int len = localFile.getContentLength();
				localFile.connect();

				if (localFile.exists()) {
					localFile.delete();
				}

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		}
		return true;
	}

	public static void fileCopy(String sourceFile, String destpath)
			throws Exception {
		InputStream in = null;
		OutputStream out = null;
		String strFileName = "";
		System.setProperty("jcifs.smb.client.dfs.disabled", "true");
		try {
			int flag = Constants.AssertFileIsSMBFileDir(sourceFile);
			if (flag == 0) {
				File localFile = new File(sourceFile);
				in = new BufferedInputStream(new FileInputStream(localFile));
				strFileName = localFile.getName();
			} else {
				NtlmPasswordAuthentication auth = smbAuthUtil
						.getsmbAuth(sourceFile);
				if (auth == null) {
					myLogger.error(" smb:user password auth error! ");
					return;
				}
				if (flag == 1)
					sourceFile = AppUtil.localFilePathToSMBFilePath(sourceFile);
				SmbFile localFile = new SmbFile(sourceFile, auth);
				in = new BufferedInputStream(new SmbFileInputStream(localFile));
				strFileName = localFile.getName();
			}

			TarUtils.fileProber(destpath);
			int flag1 = Constants.AssertFileIsSMBFileDir(destpath);
			if (flag1 == 0) {
				File remoteFile = new File(destpath + File.separator
						+ strFileName);
				out = new BufferedOutputStream(new FileOutputStream(remoteFile));
			} else {

				NtlmPasswordAuthentication auth = smbAuthUtil
						.getsmbAuth(destpath);
				if (auth == null) {
					myLogger.error(" smb:user password auth error! ");
					return;
				}

				if (flag1 == 1)
					destpath = AppUtil.localFilePathToSMBFilePath(destpath);
				SmbFile remoteFile = new SmbFile(destpath + strFileName, auth);
				out = new BufferedOutputStream(new SmbFileOutputStream(
						remoteFile));
			}

			// org.apache.commons.io.IOUtils.copyLarge(in, out);
			int count = 0;
			byte[] buffer = new byte[BUFFER_SIZE];

			while ((count = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
				out.write(buffer, 0, count);
			}

			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<String> unTar(InputStream inputStream, String destDir)
			throws Exception {
		List<String> fileNames = new ArrayList<String>();
		TarArchiveInputStream tarIn = new TarArchiveInputStream(inputStream,
				BUFFER_SIZE);
		TarArchiveEntry entry = null;
		try {
			while ((entry = tarIn.getNextTarEntry()) != null) {
				fileNames.add(entry.getName());
				if (entry.isDirectory()) {// 鏄洰褰?
					createDirectory(destDir, entry.getName());// 鍒涘缓绌虹洰褰?
				} else {// 鏄枃浠?
					File tmpFile = new File(destDir + File.separator
							+ entry.getName());
					createDirectory(tmpFile.getParent() + File.separator, null);// 鍒涘缓杈撳嚭鐩綍
					OutputStream out = null;
					try {
						out = new FileOutputStream(tmpFile);
						int length = 0;
						byte[] b = new byte[2048];
						while ((length = tarIn.read(b)) != -1) {
							out.write(b, 0, length);
						}
					} finally {
						// IOUtils.closeQuietly(out);
						out.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// IOUtils.closeQuietly(tarIn);
			tarIn.close();
		}

		return fileNames;
	}

	public static List<String> unTar(String tarFile, String destDir)
			throws Exception {
		File file = new File(tarFile);
		return unTar(file, destDir);
	}

	public static List<String> unTar(File tarFile, String destDir)
			throws Exception {
		if (StringUtils.isBlank(destDir)) {
			destDir = tarFile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir
				+ File.separator;
		return unTar(new FileInputStream(tarFile), destDir);
	}

	public static List<String> unTarBZip2(File tarFile, String destDir)
			throws Exception {
		if (StringUtils.isBlank(destDir)) {
			destDir = tarFile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir
				+ File.separator;
		return unTar(new BZip2CompressorInputStream(
				new FileInputStream(tarFile)), destDir);
	}

	public static List<String> unTarBZip2(String file, String destDir)
			throws Exception {
		File tarFile = new File(file);
		return unTarBZip2(tarFile, destDir);
	}

	public static List<String> unBZip2(String bzip2File, String destDir)
			throws IOException {
		File file = new File(bzip2File);
		return unBZip2(file, destDir);
	}

	public static List<String> unBZip2(File srcFile, String destDir)
			throws IOException {
		if (org.apache.commons.lang.StringUtils.isBlank(destDir)) {
			destDir = srcFile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir
				+ File.separator;
		List<String> fileNames = new ArrayList<String>();
		InputStream is = null;
		OutputStream os = null;
		try {
			File destFile = new File(destDir, FilenameUtils.getBaseName(srcFile
					.toString()));
			fileNames.add(FilenameUtils.getBaseName(srcFile.toString()));
			is = new BZip2CompressorInputStream(new BufferedInputStream(
					new FileInputStream(srcFile), BUFFER_SIZE));
			os = new BufferedOutputStream(new FileOutputStream(destFile),
					BUFFER_SIZE);
			IOUtils.copy(is, os);
		} finally {
			os.close();
			is.close();
			// IOUtils.closeQuietly(os);
			// IOUtils.closeQuietly(is);
		}
		return fileNames;
	}

	public static List<String> unGZ(String gzFile, String destDir)
			throws IOException {
		File file = new File(gzFile);
		return unGZ(file, destDir);
	}

	public static List<String> unGZ(File srcFile, String destDir)
			throws IOException {
		if (StringUtils.isBlank(destDir)) {
			destDir = srcFile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir
				+ File.separator;
		List<String> fileNames = new ArrayList<String>();
		InputStream is = null;
		OutputStream os = null;
		try {
			File destFile = new File(destDir, FilenameUtils.getBaseName(srcFile
					.toString()));
			fileNames.add(FilenameUtils.getBaseName(srcFile.toString()));
			is = new GzipCompressorInputStream(new BufferedInputStream(
					new FileInputStream(srcFile), BUFFER_SIZE));
			os = new BufferedOutputStream(new FileOutputStream(destFile),
					BUFFER_SIZE);
			IOUtils.copy(is, os);
		} finally {
			os.close();
			is.close();
			// IOUtils.closeQuietly(os);
			// IOUtils.closeQuietly(is);
		}
		return fileNames;
	}

	public static List<String> unTarGZ(File tarFile, String destDir)
			throws Exception {
		if (StringUtils.isBlank(destDir)) {
			destDir = tarFile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir
				+ File.separator;
		return unTar(
				new GzipCompressorInputStream(new FileInputStream(tarFile)),
				destDir);
	}

	public static List<String> unTarGZ(String file, String destDir)
			throws Exception {
		File tarFile = new File(file);
		return unTarGZ(tarFile, destDir);
	}

	public static void createDirectory(String outputDir, String subDir) {
		File file = new File(outputDir);
		if (!(subDir == null || subDir.trim().equals(""))) {// 瀛愮洰褰曚笉涓虹┖
			file = new File(outputDir + File.separator + subDir);
		}
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static List<String> unZip(File zipfile, String destDir)
			throws Exception {
		if (StringUtils.isBlank(destDir)) {
			destDir = zipfile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir
				+ File.separator;
		ZipArchiveInputStream is = null;
		List<String> fileNames = new ArrayList<String>();

		try {
			is = new ZipArchiveInputStream(new BufferedInputStream(
					new FileInputStream(zipfile), BUFFER_SIZE));
			ZipArchiveEntry entry = null;
			while ((entry = is.getNextZipEntry()) != null) {
				fileNames.add(entry.getName());
				if (entry.isDirectory()) {
					File directory = new File(destDir, entry.getName());
					directory.mkdirs();
				} else {
					OutputStream os = null;
					try {
						os = new BufferedOutputStream(new FileOutputStream(
								new File(destDir, entry.getName())),
								BUFFER_SIZE);
						IOUtils.copy(is, os);
					} finally {
						// IOUtils.closeQuietly(os);
						os.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			// IOUtils.closeQuietly(is);
			is.close();
		}

		return fileNames;
	}

	public static List<String> unZip(String zipfile, String destDir)
			throws Exception {
		File zipFile = new File(zipfile);
		return unZip(zipFile, destDir);
	}

	public static List<String> unCompress(String compressFile, String destDir)
			throws Exception {
		String upperName = compressFile.toUpperCase();
		List<String> ret = null;
		if (upperName.endsWith(".ZIP")) {
			ret = unZip(compressFile, destDir);
		} else if (upperName.endsWith(".TAR")) {
			ret = unTar(compressFile, destDir);
		} else if (upperName.endsWith(".TAR.BZ2")) {
			ret = unTarBZip2(compressFile, destDir);
		} else if (upperName.endsWith(".BZ2")) {
			ret = unBZip2(compressFile, destDir);
		} else if (upperName.endsWith(".TAR.GZ")) {
			ret = unTarGZ(compressFile, destDir);
		} else if (upperName.endsWith(".GZ")) {
			ret = unGZ(compressFile, destDir);
		}
		return ret;
	}

	public static void zipFile(List<File> files, ZipOutputStream outputStream) {
		int size = files.size();
		for (int i = 0; i < size; i++) {
			File file = (File) files.get(i);
			zipFile(file, outputStream);
		}
	}

	/**
	 * 根据输入的文件与输出流对文件进行打包
	 * 
	 * @param File
	 * @param ZipOutputStream
	 */
	public static void zipFile(File inputFile, ZipOutputStream ouputStream) {
		try {
			if (!inputFile.exists())
				return;
			if (inputFile.isFile()) {
				FileInputStream IN = new FileInputStream(inputFile);
				BufferedInputStream bins = new BufferedInputStream(IN, 512);
				// org.apache.tools.zip.ZipEntry
				ZipEntry entry = new ZipEntry(inputFile.getName());
				ouputStream.putNextEntry(entry);
				// 向压缩文件中输出数据
				int nNumber;
				byte[] buffer = new byte[512];
				while ((nNumber = bins.read(buffer)) != -1) {
					ouputStream.write(buffer, 0, nNumber);
				}
				// 关闭创建的流对象
				bins.close();
				IN.close();
			} else {
				try {
					File[] files = inputFile.listFiles();
					for (int i = 0; i < files.length; i++) {
						zipFile(files[i], ouputStream);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

	}
}