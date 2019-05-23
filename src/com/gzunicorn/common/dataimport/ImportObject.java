
package com.gzunicorn.common.dataimport;

import java.io.*;
/**
 * implements　importInterface　类是为了在以后接口扩展之后，其它子类用到这个接口的其中一些方法时，不用将接口的所有方法都实现
 * 避免直接继承接口而修改现有程序
 * @author rr
 *
 */
public class ImportObject implements ImportInterface{

    public String[][] getItem(InputStream is) throws ImportException{
    	return null;
    }

	public String[][] getItem(InputStream is,String split,int colRow,int[] colArray) throws ImportException{
		return null;
	}
	
	public String[][] getItem(InputStream is,String split,int colRow,int[][] colArray) throws ImportException{
		return null;
	}
   
    
}
