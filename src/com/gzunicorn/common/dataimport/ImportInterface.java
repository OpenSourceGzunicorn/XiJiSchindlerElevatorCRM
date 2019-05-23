/*
 * Created on 2005-8-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.dataimport;

import java.io.*;
/**
 * @author rr
 */
public interface ImportInterface {
	/**
	 * 
	 * @param is
	 * @return
	 * @throws ImportException
	 */
    public String[][] getItem(InputStream is) throws ImportException;
    
    /**
     * 导入文件时不需要指定列的数据类型，可用此方法
     * @param is
     * @param split
     * @param colRow
     * @param colArray
     * @return
     * @throws ImportException
     */
	public String[][] getItem(InputStream is,String split,int colRow,int[] colArray) throws ImportException;
	
	/**
	 * 导入文件时需要指定列数据类型，调用此方法，如在excel里的日期型，则要指定;
	 * @param is	文件流
	 * @param split　　分格符
	 * @param colRow　　列的总数
	 * @param colArray [i][0]--列位置，[i][1]--对应的类型
	 * @return
	 * @throws ImportException
	 */
	public String[][] getItem(InputStream is,String split,int colRow,int[][] colArray) throws ImportException;
   
    
}
