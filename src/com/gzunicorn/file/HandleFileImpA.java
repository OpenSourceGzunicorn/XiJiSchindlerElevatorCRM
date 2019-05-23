package com.gzunicorn.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.gzunicorn.common.util.DebugUtil;

/**
 * 文件操作类
 * 
 * @version 1.0
 * @author Administrator
 * 
 */
public class HandleFileImpA implements HandleFile {

	/**
	 * 创建一个新文件。
	 * 
	 * @param in
	 *            文件输入流
	 * @param dir
	 *            文件存放目录
	 * @param fileName
	 *            文件名称
	 */
	public void createFile(InputStream in, String dir, String fileName)
			throws IOException {

		File f = new File(dir);
		f.mkdirs();
		OutputStream out = new FileOutputStream(dir + fileName);
		byte[] buf = new byte[8192];
		int len = 0;
		while ((len = in.read(buf)) != -1) {
			out.write(buf, 0, len);
		}
		out.flush();
		out.close();
		in.close();
	}

	public boolean writeFile(String dir, String fileName, StringBuffer content)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 读文件方法
	 * 
	 * @param dir
	 *            文件目录
	 * @param fileName
	 *            文件名称
	 * @return InputSteam 返回文件输入流
	 */
	public InputStream readFile(String dir, String fileName)
			throws FileNotFoundException {
		File f = new File(dir + fileName);
		InputStream in = null;
		if (f.exists()) {
			in = new FileInputStream(dir + fileName);
		}
		return in;
	}

	public void modFile(InputStream in, String dir, String fileName)
			throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * 删除文件
	 * 
	 * @param dir
	 *            文件目录
	 * @param fileName
	 *            文件名称
	 */
	public void delFile(String dir, String fileName)
			throws FileNotFoundException {
		File f = new File(dir + fileName);
		if (f.exists()) {
			f.delete();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            文件路径+文件名称。
	 */
	public void delFile(String filePathAndName) throws FileNotFoundException {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePathAndName);
			myDelFile.delete();
		} catch (Exception e) {
			DebugUtil.print(e);
			e.printStackTrace();
		}
	}

	/**
	 * 获取某个目录下的所有文件列表
	 * 
	 * @param dir
	 *            目录
	 * @return String[] 文件列表数组
	 */
	public String[] getFileList(String dir) throws FileNotFoundException {
		File file = new File(dir);
		File list[] = file.listFiles();
		String[] filelist = null;
		if (list != null) {
			int len = list.length;
			filelist = new String[len];
			for (int i = 0; i < len; i++) {
				filelist[i] = list[i].getName();
			}
		}
		return filelist;
	}

	/**
	 * 删除文件夹，和包括删除文件夹下的所有内容。 删除时，先删除文件夹下的文件，再删除文件夹下的内容。
	 * 
	 * @param folderPath
	 * @return 删除成功返回true,否则返回false
	 */
	public boolean delFolder(String folderPath) throws FileNotFoundException {
		boolean tag = true;
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹

		} catch (Exception e) {
			tag = false;
			DebugUtil.print(e);
		}
		return tag;
	}

	/**
	 * 复制文件
	 * 
	 * @param oldPath
	 *            源文件,包括其完整路径
	 * @param newPath
	 *            目标文件
	 * @return boolean,操作成功返回true,失败返回false
	 */
	public boolean copyFile(String oldPath, String newPath)
			throws FileNotFoundException {
		boolean tag = true;
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[8196];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			tag = false;
			DebugUtil.print(e);
		}
		return tag;
	}

	/**
	 * 复制文件夹，包括文件夹下的所有文件信息
	 * 
	 * @param oldPath
	 *            源文件目录
	 * @param newPath
	 *            目标文件目录
	 */
	public boolean copyFolder(String oldPath, String newPath)
			throws FileNotFoundException {
		boolean tag = true;
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			tag = false;
			DebugUtil.print(e);

		}
		return tag;
	}

	/**
	 * 移动文件
	 * 
	 * @param oldPath
	 *            旧目录
	 * @param newPath
	 *            新目录
	 */
	public void moveFile(String oldPath, String newPath)
			throws FileNotFoundException {
		copyFile(oldPath, newPath);
		delFile(oldPath);
	}

	/**
	 * 移动文件夹
	 * 
	 * @param oldPath
	 *            旧目录
	 * @param newPath
	 *            新目录
	 */
	public void moveFolder(String oldPath, String newPath)
			throws FileNotFoundException {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 * @throws FileNotFoundException
	 */
	public void delAllFile(String path) throws FileNotFoundException {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}
}
