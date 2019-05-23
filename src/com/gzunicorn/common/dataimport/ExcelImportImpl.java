/*
 * Created on 2005-8-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.dataimport;

import java.io.*;
import java.util.*;
import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.hssf.usermodel.*;

import com.gzunicorn.common.dataimport.importutil.*;
import com.gzunicorn.common.util.DebugUtil;



public class ExcelImportImpl extends ImportObject {
	private int t_str=ImpConfig.t_str;
	private int t_date=ImpConfig.t_date;
	private int t_num=ImpConfig.t_num;

	
   
	public String[][] getItem(InputStream is,String StrSplit,int col_count,int[][] col_row) throws ImportException {
    	String split=StrSplit;
    	int colCount=col_count;
    	int[][] colRow=col_row;
        String[][] returnItem = null;
        try {

            if(is == null){
                DebugUtil.println("导入Excel文件的为空,请检查Excel文件！");
                return null;
            }
            
            POIFSFileSystem fs = new POIFSFileSystem(is);    
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);//取Excel文件的第一个Sheet，Sheet(0)
            
            if(sheet == null || sheet.getLastRowNum() < 0){
                DebugUtil.println("导入Excel文件的Sheet为空,请检查Excel文件！");
                return null;
            }

            HSSFRow row = null;
            HSSFCell cell = null;
            int currentRow = 0;
            
            Iterator it = sheet.rowIterator();

            //计算Excel文件的实际行数
            int countRow = 0; 
            countRow = sheet.getLastRowNum() - sheet.getFirstRowNum() + 1;

            //构造对应于需要导入Excel文件的数据的二维数组
           String item[][] = new String[countRow][colCount];
            
            while (it.hasNext()) {
                row = (HSSFRow) it.next();
                for (short i=0; i<colCount; i++) {
                    cell = row.getCell(i);
                    
                    for(int k=0;k<colCount;k++){
                		if(i==colRow[k][0]){
                			if(colRow[k][1]==t_str || colRow[k][1]==t_num){
                				item[currentRow][i] = TranExcelCellUtil.tranCell(cell);
                			}else if(colRow[k][1]==t_date){
                				item[currentRow][i] = TranExcelCellUtil.tranDate(cell);
                			}
                			break;
                		}
                	}
                }
                currentRow++;
            }
            returnItem = item;
        } catch (Exception e) {
            DebugUtil.println(e.getMessage());
       } finally {

        }

        return returnItem;

    }

}
