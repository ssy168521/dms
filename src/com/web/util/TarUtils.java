package com.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.lang.StringUtils;

import com.sasmac.util.smbAuthUtil;
import com.web.common.Constants;

/**
 * TAR
 * 
 * @author
 * @since 1.0
 */
public abstract class TarUtils {

	private static final String BASE_DIR = "D:\\";

	// 符号"/"用来作为目录标识判断�?
	private static final String PATH = "/";
	public static int BUFFER_SIZE = 81960000;
	private static final String EXT = ".tar";

	/**
	 * 归档
	 * 
	 * @param srcPath
	 * @param destPath
	 * @throws Exception
	 */
	public static void archive(String srcPath, String destPath)
			throws Exception {

		File srcFile = new File(srcPath);

		archive(srcFile, destPath);

	}

	/**
	 * 归档
	 * 
	 * @param srcFile
	 *            源路�?
	 * @param destPath
	 *            目标路径
	 * @throws Exception
	 */
	public static void archive(File srcFile, File destFile) throws Exception {
		TarArchiveOutputStream taos = new TarArchiveOutputStream(
				new FileOutputStream(destFile));

		archive(srcFile, taos, BASE_DIR);

		taos.flush();
		taos.close();
	}

	/**
	 * 归档
	 * 
	 * @param srcFile
	 * @throws Exception
	 */
	public static void archive(File srcFile) throws Exception {
		String name = srcFile.getName();
		String basePath = srcFile.getParent() + "\\";
		String destPath = basePath + name + EXT;
		archive(srcFile, destPath);
	}

	/**
	 * 归档文件
	 * 
	 * @param srcFile
	 * @param destPath
	 * @throws Exception
	 */
	public static void archive(File srcFile, String destPath) throws Exception {
		archive(srcFile, new File(destPath));
	}

	/**
	 * 归档
	 * 
	 * @param srcPath
	 * @throws Exception
	 */
	public static void archive(String srcPath) throws Exception {
		File srcFile = new File(srcPath);

		archive(srcFile);
	}

	/**
	 * 归档
	 * 
	 * @param srcFile
	 *            源路�?
	 * @param taos
	 *            TarArchiveOutputStream
	 * @param basePath
	 *            归档包内相对路径
	 * @throws Exception
	 */
	private static void archive(File srcFile, TarArchiveOutputStream taos,
			String basePath) throws Exception {
		if (srcFile.isDirectory()) {
			archiveDir(srcFile, taos, basePath);
		} else {
			archiveFile(srcFile, taos, basePath);
		}
	}

	/**
	 * 目录归档
	 * 
	 * @param dir
	 * @param taos
	 *            TarArchiveOutputStream
	 * @param basePath
	 * @throws Exception
	 */
	private static void archiveDir(File dir, TarArchiveOutputStream taos,
			String basePath) throws Exception {
		File[] files = dir.listFiles();

		if (files.length < 1) {
			TarArchiveEntry entry = new TarArchiveEntry(basePath
					+ dir.getName() + PATH);

			taos.putArchiveEntry(entry);
			taos.closeArchiveEntry();
		}

		for (File file : files) {
			// 递归归档
			archive(file, taos, basePath + dir.getName() + PATH);
		}
	}

	/**
	 * 数据归档
	 * 
	 * @param data
	 *            待归档数�?
	 * @param path
	 *            归档数据的当前路�?
	 * @param name
	 *            归档文件�?
	 * @param taos
	 *            TarArchiveOutputStream
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	private static void archiveFile(File file, TarArchiveOutputStream taos,
			String dir) throws Exception {
		/**
		 * 
		 * <pre>
		 * 
		 * </pre>
		 */
		TarArchiveEntry entry = new TarArchiveEntry(dir + file.getName());

		entry.setSize(file.length());

		taos.setLongFileMode(taos.LONGFILE_GNU);
		taos.putArchiveEntry(entry);

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				file));
		int count;
		byte data[] = new byte[BUFFER_SIZE];
		while ((count = bis.read(data, 0, BUFFER_SIZE)) != -1) {
			taos.write(data, 0, count);
		}

		bis.close();

		taos.closeArchiveEntry();
	}

	/**
	 * 解归�?
	 * 
	 * @param srcFile
	 * @throws Exception
	 */
	public static void dearchive(File srcFile) throws Exception {
		String basePath = srcFile.getParent();
		dearchive(srcFile, basePath);
	}

	/**
	 * 解归�?
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws Exception
	 */
	public static void dearchive(File srcFile, File destFile) throws Exception {
		TarArchiveInputStream tais = new TarArchiveInputStream(
				new FileInputStream(srcFile));
		dearchive(destFile, tais);

		tais.close();
	}

	/**
	 * 解归档指定的文件
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws Exception
	 */
	public static String dearchive(File srcFile, File destFile, String fileName)
			throws Exception {
		TarArchiveInputStream tais = new TarArchiveInputStream(
				new FileInputStream(srcFile));
		String xmlNam = dearchive(destFile, tais, fileName);

		tais.close();

		return xmlNam;
	}

	public static String dearchive(String srctarFile, String destFilePath,
			String fileName) throws Exception {
		int bIsSmbFile = Constants.AssertFileIsSMBFileDir(srctarFile);
		InputStream is = null;
		if (bIsSmbFile == 2) {
			NtlmPasswordAuthentication auth = smbAuthUtil
					.getsmbAuth(srctarFile);
			if (auth == null) {
				Constants.WriteLog(" smb:user password auth error! ");
				return "";
			}

			SmbFile smbFile = new SmbFile(srctarFile, auth);
			SmbFileInputStream in = new SmbFileInputStream(smbFile);
			is = in;
		} else if (bIsSmbFile == 1) {
			NtlmPasswordAuthentication auth = smbAuthUtil
					.getsmbAuth(srctarFile);
			if (auth == null) {
				Constants.WriteLog(" smb:user password auth error! ");
				return "";
			}

			srctarFile = AppUtil.localFilePathToSMBFilePath(srctarFile);
			SmbFile smbFile = new SmbFile(srctarFile, auth);
			SmbFileInputStream in = new SmbFileInputStream(smbFile);
			is = in;
		} else {
			File pf = new File(srctarFile);
			is = new FileInputStream(pf);
		}
		TarArchiveInputStream tais = new TarArchiveInputStream(is);
		String xmlName = dearchive(destFilePath, tais, fileName);

		tais.close();

		return xmlName;
	}

	/**
	 * 解归�?
	 * 
	 * @param srcFile
	 * @param destPath
	 * @throws Exception
	 */
	public static void dearchive(File srcFile, String destPath)
			throws Exception {
		dearchive(srcFile, new File(destPath));
	}

	/**
	 * 文件 解归�?
	 * 
	 * @param destFile
	 *            目标文件
	 * @param tais
	 *            TarArchiveInputStream
	 * @throws Exception
	 */
	private static void dearchive(File destFile, TarArchiveInputStream tais)
			throws Exception {
		TarArchiveEntry entry = null;
		while ((entry = tais.getNextTarEntry()) != null) {
			// 文件
			String dir = destFile.getPath() + File.separator + entry.getName();

			File dirFile = new File(dir);

			// 文件�?��
			fileProber(dirFile);

			if (entry.isDirectory()) {
				dirFile.mkdirs();
			} else {
				dearchiveFile(dirFile, tais);
			}
		}
	}

	/**
	 * 
	 * 
	 * @param destFile
	 * 
	 * @param tais
	 *            TarArchiveInputStream
	 * @throws Exception
	 */
	private static String dearchive(File destFile, TarArchiveInputStream tais,
			String fileName) throws Exception {
		TarArchiveEntry entry = null;
		String xmlName = "";
		String strName = "";
		while ((entry = tais.getNextTarEntry()) != null) {
			strName = entry.getName();
			if (strName.compareToIgnoreCase(fileName) != 0)
				continue;
			// if(!(entry.getName().contains("zy301a_bwd_026084_026105_20160919112839_01_sec_0001_1609208009.xml")))//fileName)))
			// continue;
			// 文件
			xmlName = destFile.getPath() + File.separator + entry.getName();

			File dirFile = new File(xmlName);

			if (dirFile.exists())
				dirFile.delete();

			// 文件�?��
			fileProber(dirFile);

			if (entry.isDirectory()) {
				dirFile.mkdirs();
			} else {
				dearchiveFile(dirFile, tais);
			}
			break;
		}

		return xmlName;
	}

	private static String dearchive(String destFilePath,
			TarArchiveInputStream tais, String fileName) throws Exception {

		TarArchiveEntry entry = null;
		String xmlName = "";
		String strName = "";
		while ((entry = tais.getNextTarEntry()) != null) {
			strName = entry.getName();
			if (strName.compareToIgnoreCase(fileName) != 0)
				continue;

			xmlName = destFilePath + entry.getName();
			fileProber(destFilePath);
			dearchiveFile(xmlName, tais);
			break;
		}

		return xmlName;
	}

	/**
	 * 文件 解归�?
	 * 
	 * @param srcPath
	 *            源文件路�?
	 * 
	 * @throws Exception
	 */
	public static void dearchive(String srcPath) throws Exception {
		File srcFile = new File(srcPath);

		dearchive(srcFile);
	}

	/**
	 * 文件 解归�?
	 * 
	 * @param srcPath
	 *            源文件路�?
	 * @param destPath
	 *            目标文件路径
	 * @throws Exception
	 */
	public static void dearchive(String srcPath, String destPath)
			throws Exception {
		File srcFile = new File(srcPath);
		dearchive(srcFile, destPath);
	}

	/**
	 * 文件解归�?
	 * 
	 * @param destFile
	 *            目标文件
	 * @param tais
	 *            TarArchiveInputStream
	 * @throws Exception
	 */
	private static void dearchiveFile(File destFile, TarArchiveInputStream tais)
			throws Exception {
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(destFile));

		int count;
		byte data[] = new byte[BUFFER_SIZE];
		while ((count = tais.read(data, 0, BUFFER_SIZE)) != -1) {
			bos.write(data, 0, count);
		}

		bos.close();
	}

	private static void dearchiveFile(String destFile,
			TarArchiveInputStream tais) throws Exception {
		int flag = Constants.AssertFileIsSMBFileDir(destFile);
		if (flag == 2) {
			NtlmPasswordAuthentication auth = smbAuthUtil.getsmbAuth(destFile);
			if (auth == null) {
				Constants.WriteLog(" smb:user password auth error! ");
				return;
			}
			SmbFile remoteFile = new SmbFile(destFile);
			BufferedOutputStream bos = new BufferedOutputStream(
					new SmbFileOutputStream(remoteFile));

			int count;
			byte data[] = new byte[BUFFER_SIZE];
			while ((count = tais.read(data, 0, BUFFER_SIZE)) != -1) {
				bos.write(data, 0, count);
			}

			bos.close();
		} else if (flag == 1) {
			NtlmPasswordAuthentication auth = smbAuthUtil.getsmbAuth(destFile);
			if (auth == null) {
				Constants.WriteLog(" smb:user password auth error! ");
				return;
			}
			destFile = AppUtil.localFileNameToSMBFileName(destFile);
			SmbFile remoteFile = new SmbFile(destFile, auth);
			BufferedOutputStream bos = new BufferedOutputStream(
					new SmbFileOutputStream(remoteFile));

			int count;
			byte data[] = new byte[BUFFER_SIZE];
			while ((count = tais.read(data, 0, BUFFER_SIZE)) != -1) {
				bos.write(data, 0, count);
			}

			bos.close();
		} else {
			File df = new File(destFile);
			if (df.exists())
				df.delete();
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(df));

			int count;
			byte data[] = new byte[BUFFER_SIZE];
			while ((count = tais.read(data, 0, BUFFER_SIZE)) != -1) {
				bos.write(data, 0, count);
			}

			bos.close();
		}

	}

	/**
	 * 文件探针
	 * 
	 * <pre>
	 * 当父目录不存在时，创建目录！
	 * </pre>
	 * 
	 * @param dirFile
	 */
	private static void fileProber(File dirFile) {
		if (dirFile.isFile()) {
			File parentFile = dirFile.getParentFile();
			parentFile.mkdirs();
		} else {
			dirFile.mkdirs();
		}

		/*
		 * if (!parentFile.exists()) {
		 * 
		 * // 递归寻找上级目录 fileProber(parentFile);
		 * 
		 * parentFile.mkdir(); }
		 */
	}

	private static void smbfileProber(SmbFile dirFile) throws SmbException {

		try {

			if (!dirFile.exists())
				dirFile.mkdirs();
			/*
			 * if (dirFile.isFile()) { SmbFile parentFile = new
			 * SmbFile(dirFile.getParent()); parentFile.mkdirs(); } else {
			 * dirFile.mkdirs(); }
			 */
			/*
			 * if (!parentFile.exists()) {
			 * 
			 * // 递归寻找上级目录 smbfileProber(parentFile);
			 * 
			 * parentFile.mkdir(); }
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void fileProber(String dirFile) throws Exception,
			SmbException {
		int flag = Constants.AssertFileIsSMBFileDir(dirFile);
		if (flag == 1) {
			NtlmPasswordAuthentication auth = smbAuthUtil.getsmbAuth(dirFile);
			if (auth == null) {
				Constants.WriteLog(" smb:user password auth error! ");
				return;
			}
			dirFile = AppUtil.localFilePathToSMBFilePath(dirFile);
			SmbFile pf = new SmbFile(dirFile, auth);
			smbfileProber(pf);
		} else if (flag == 2) {
			NtlmPasswordAuthentication auth = smbAuthUtil.getsmbAuth(dirFile);
			if (auth == null) {
				Constants.WriteLog(" smb:user password auth error! ");
				return;
			}
			SmbFile pf = new SmbFile(dirFile, auth);
			smbfileProber(pf);
		} else {
			File pf = new File(dirFile);
			fileProber(pf);
		}
	}

	public static void main(String args[]) {
		String path = "D:\\workspace\\datacopy\\WebRoot\\WEB-INF\\classes\\com\\sasmac\\dts\\copy\\";// adckik890skcosik8193872103821034skfjiwqkdfjiskyuvhaleiekso\\";
		try {
			// archive(path);

			dearchive(
					path
							+ "adckik890skcosik8193872103821034skfjiwqkdfjiskyuvhaleiekso.tar",
					"D:\\workspace\\jwDmsServer\\WebRoot\\WEB-INF\\classes\\com\\marstor\\dms\\dss\\");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /////////
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

	private static List<String> unTar(InputStream inputStream, String destDir)
			throws Exception {
		List<String> fileNames = new ArrayList<String>();
		TarArchiveInputStream tarIn = new TarArchiveInputStream(inputStream,
				BUFFER_SIZE);
		TarArchiveEntry entry = null;
		try {
			while ((entry = tarIn.getNextTarEntry()) != null) {
				fileNames.add(entry.getName());
				if (entry.isDirectory()) {
					createDirectory(destDir, entry.getName());//
				} else {// 是文�?
					File tmpFile = new File(destDir + File.separator
							+ entry.getName());
					createDirectory(tmpFile.getParent() + File.separator, null);//
					OutputStream out = null;
					try {
						out = new FileOutputStream(tmpFile);
						int length = 0;
						byte[] b = new byte[BUFFER_SIZE];
						while ((length = tarIn.read(b)) != -1) {
							out.write(b, 0, length);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					} finally {
						out.close();
						// IOUtils.closeQuietly(out);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			tarIn.close();
			// IOUtils.closeQuietly(tarIn);

		}
		return fileNames;
	}

	public static void createDirectory(String outputDir, String subDir) {
		File file = new File(outputDir);
		if (!(subDir == null || subDir.trim().equals(""))) {// 子目录不为空
			file = new File(outputDir + File.separator + subDir);
		}
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static boolean unTarGZsinglefile(String targzfile, String destDir,
			String toUntarfilename) throws Exception {
		int flag = Constants.AssertFileIsSMBFileDir(targzfile);
		if (flag == 1) {
			NtlmPasswordAuthentication auth = smbAuthUtil.getsmbAuth(targzfile);
			if (auth == null) {
				Constants.WriteLog(" smb:user password auth error! ");
				return false;
			}
			targzfile = AppUtil.localFilePathToSMBFilePath(targzfile);
			SmbFile tarFile = new SmbFile(targzfile, auth);
			if (StringUtils.isBlank(destDir)) {
				destDir = tarFile.getParent();
			}
			destDir = destDir.endsWith(File.separator) ? destDir : destDir
					+ File.separator;
			return unTarsinglefile(new GzipCompressorInputStream(
					new SmbFileInputStream(tarFile)), destDir, toUntarfilename);

		} else if (flag == 2) {
			NtlmPasswordAuthentication auth = smbAuthUtil.getsmbAuth(targzfile);
			if (auth == null) {
				Constants.WriteLog(" smb:user password auth error! ");
				return false;
			}
			SmbFile tarFile = new SmbFile(targzfile, auth);
			if (StringUtils.isBlank(destDir)) {
				destDir = tarFile.getParent();
			}
			destDir = destDir.endsWith(File.separator) ? destDir : destDir
					+ File.separator;
			return unTarsinglefile(new GzipCompressorInputStream(
					new SmbFileInputStream(tarFile)), destDir, toUntarfilename);

		} else {
			File tarFile = new File(targzfile);
			if (StringUtils.isBlank(destDir)) {
				destDir = tarFile.getParent();
			}
			destDir = destDir.endsWith(File.separator) ? destDir : destDir
					+ File.separator;
			return unTarsinglefile(new GzipCompressorInputStream(
					new FileInputStream(tarFile)), destDir, toUntarfilename);

		}

	}

	public static boolean unTarGZsinglefile(File tarFile, String destDir,
			String toUntarfilename) throws Exception {
		if (StringUtils.isBlank(destDir)) {
			destDir = tarFile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir
				+ File.separator;
		return unTarsinglefile(new GzipCompressorInputStream(
				new FileInputStream(tarFile)), destDir, toUntarfilename);
	}

	private static boolean unTarsinglefile(InputStream inputStream,
			String destDir, String toUntarfilename) throws Exception {
		int flag = Constants.AssertFileIsSMBFileDir(destDir);

		TarArchiveInputStream tarIn = new TarArchiveInputStream(inputStream,
				BUFFER_SIZE);
		TarArchiveEntry entry = null;
		boolean bret = false;
		try {
			while ((entry = tarIn.getNextTarEntry()) != null) {
				String filename = entry.getName();
				if (filename.compareToIgnoreCase(toUntarfilename) != 0)
					continue;
				if (entry.isDirectory()) {
					fileProber(destDir);
				} else {
					if (flag == 1) {
						String path = destDir + File.separator
								+ entry.getName();
						NtlmPasswordAuthentication auth = smbAuthUtil
								.getsmbAuth(path);
						if (auth == null) {
							Constants
									.WriteLog(" smb:user password auth error! ");
							return false;
						}
						SmbFile tmpFile = new SmbFile(path, auth);
						fileProber(destDir);
						OutputStream out = null;
						try {
							out = new SmbFileOutputStream(tmpFile);
							int length = 0;
							byte[] b = new byte[BUFFER_SIZE];
							while ((length = tarIn.read(b)) != -1) {
								out.write(b, 0, length);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						} finally {
							out.close();
						}
					} else if (flag == 2) {
						String path = destDir + File.separator
								+ entry.getName();
						NtlmPasswordAuthentication auth = smbAuthUtil
								.getsmbAuth(path);
						if (auth == null) {
							Constants
									.WriteLog(" smb:user password auth error! ");
							return false;
						}
						SmbFile tmpFile = new SmbFile(path, auth);

						fileProber(destDir);
						OutputStream out = null;
						try {
							out = new SmbFileOutputStream(tmpFile);
							int length = 0;
							byte[] b = new byte[BUFFER_SIZE];
							while ((length = tarIn.read(b)) != -1) {
								out.write(b, 0, length);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						} finally {
							out.close();
						}
					} else {
						File tmpFile = new File(destDir + File.separator
								+ entry.getName());
						createDirectory(tmpFile.getParent() + File.separator,
								null);// 创建输出目录
						OutputStream out = null;
						try {
							out = new FileOutputStream(tmpFile);
							int length = 0;
							byte[] b = new byte[BUFFER_SIZE];
							while ((length = tarIn.read(b)) != -1) {
								out.write(b, 0, length);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						} finally {

							out.close();
						}
					}

				}
				bret = true;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			tarIn.close();
			// IOUtils.closeQuietly(tarIn);

		}
		return bret;
	}
}
