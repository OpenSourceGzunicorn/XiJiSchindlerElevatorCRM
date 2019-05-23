/*
 * Created on 2005-8-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.dataimport;

import java.io.*;
import java.util.*;

import com.gzunicorn.common.util.DebugUtil;


/**
 * 
 * @author rr
 *
 */
public class TextImportImpl extends ImportObject {
	
	/**
	 * 
	 * @param is
	 * @param StrSplit
	 * @param col_count
	 * @param col_row[][]
	 * @return String[][]
	 * @throws ImportException
	 */
    public String[][] getItem(InputStream is,String StrSplit,int col_count,int[][] col_row) throws ImportException {
    	String split=StrSplit;
    	int colCount=col_count;
    	int[][] colRow=col_row;
        String[][] returnItem = null;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(is));
            String lineStr = null; // 一行的字符串
            ArrayList list = new ArrayList();
            int countRow = 0;
            int currentRow = 0;
            // 计算总行数
            while ((lineStr = br.readLine()) != null) {
                list.add(lineStr);
                countRow++;
            } 

            // 构造数组
            String item[][] = new String[countRow][col_count];
            // while ((lineStr = br.readLine()) != null) {
            if (list != null && !list.isEmpty()) {

                for (int j = 0; j < list.size(); j++) {
                    lineStr = ((String) list.get(j)).trim();
                    if (lineStr.equals("")) {
                        // 空行统一赋空值
                        for (int i = 0; i < colCount; i++) {
                            item[currentRow][i] = "";
                        }
                        continue;
                    }

                    String lineArr[] = lineStr.split(split);
                    String newLineArr[] = new String[colCount];

                    if (lineArr.length != newLineArr.length) {
                        // Copy数组
                        if(lineArr.length <= newLineArr.length){
                            System.arraycopy(lineArr, 0, newLineArr, 0,lineArr.length);    
                        }else{
                            System.arraycopy(lineArr, 0, newLineArr, 0,newLineArr.length);
                        }
                        
                        // 当新数组的长度大于旧数组的长度时，补充新数组的值为空
                        for (int i = lineArr.length; i < newLineArr.length; i++) {
                            newLineArr[i] = "";
                        }
                    } else {
                        newLineArr = lineArr;
                    }

                    for (short i = 0; i < colCount; i++) {
                    	for(int k=0;k<colCount;k++){
                    		if(i==colRow[k][0]){
                    			item[currentRow][i] = newLineArr[i].trim();
                    			break;
                    		}
                    	}
                    }
                    currentRow++;
                }
            }
            returnItem = item;
        } catch (IOException e) {
            DebugUtil.println(e.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                DebugUtil.println(e.getMessage());
            }

        }
        return returnItem;
    }
}
