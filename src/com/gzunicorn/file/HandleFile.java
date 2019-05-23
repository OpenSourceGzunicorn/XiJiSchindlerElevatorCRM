package com.gzunicorn.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface HandleFile {
	/**
	 * 新建保存一个文件
	 * 
	 * @param in
	 * @param dir
	 * @param fileName
	 * @throws IOException
	 */
	public void createFile(InputStream in, String dir, String fileName)
			throws IOException;

	/**
	 * 文件内追加内容
	 * 
	 * @param dir
	 * @param fileName
	 * @param content
	 * @return boolean true
	 * @throws FileNotFoundException
	 */
	public boolean writeFile(String dir, String fileName, StringBuffer content)
			throws FileNotFoundException;

	/**
	 * 读文件
	 * 
	 * @param dir
	 * @param fileName
	 * @return 文件输入流
	 * @throws FileNotFoundException
	 */
	public InputStream readFile(String dir, String fileName)
			throws FileNotFoundException;

	/**
	 * 修改文件
	 * 
	 * @param in
	 * @param dir
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void modFile(InputStream in, String dir, String fileName)
			throws FileNotFoundException, IOException;

	/**
	 * 删除文件
	 * 
	 * @param dir
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void delFile(String dir, String fileName)
			throws FileNotFoundException;

	/**
	 * 删除文件
	 * 
	 * @param fileFullName
	 * @throws FileNotFoundException
	 */
	public void delFile(String fileFullName) throws FileNotFoundException;

	/**
	 * 取目录下的文件列表
	 * 
	 * @param dir
	 * @return 文件列表
	 * @throws FileNotFoundException
	 */
	public String[] getFileList(String dir) throws FileNotFoundException;

	/**
	 * 删除文件夹(java里的文件的删除方法，只是删除文件和空文件夹，所以需要递归)
	 * 
	 * @param folderPath
	 *            String 文件夹路径及名称 如c:/fqf
	 * @return boolean
	 */
	public boolean delFolder(String folderPath) throws FileNotFoundException;

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return boolean
	 * @throws FileNotFoundException
	 */
	public boolean copyFile(String oldPath, String newPath)
			throws FileNotFoundException;

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public boolean copyFolder(String oldPath, String newPath)
			throws FileNotFoundException;

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 * @return boolean
	 */
	public void moveFile(String oldPath, String newPath)
			throws FileNotFoundException;

	/**
	 * 移动文件夹到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf
	 * @param newPath
	 *            String 如：d:/fff
	 * @return boolean
	 */
	public void moveFolder(String oldPath, String newPath)
			throws FileNotFoundException;

}
