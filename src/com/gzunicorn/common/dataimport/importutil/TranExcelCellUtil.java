/*
 * Created on 2005-8-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.dataimport.importutil;

import java.text.*;
import java.text.ParseException;
import org.apache.poi.hssf.usermodel.HSSFCell;


/**
 * Created on 2006-03-04
 * Title: 通用类
 * Description: 转换Excel文件中的Cell格式(系统级转换，不是逻辑校验),提供单态模式
 * 
 * @author rr
 * @version 1.2
 */
public class TranExcelCellUtil {

    private static TranExcelCellUtil tranExcelCellUtil = new TranExcelCellUtil();

    private TranExcelCellUtil() {
    }

    public static TranExcelCellUtil getInstance() {
        return tranExcelCellUtil;
    }

    /**
     * 通用的Cell的转换方法
     * 暂时是字符串和数字转化
     * @param cell
     * @return String
     */
    public static String tranCell(HSSFCell cell) {
        String returnValue = "";
        if(cell == null){
            return returnValue;
        }
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            // 当Cell的格式为字符型时
            returnValue = cell.getStringCellValue().trim();
        } else {
            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC || cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
                // 当Cell的格式为数字型时
                // 防止出现科学计数法的表示,提供程序容错性
                NumberFormat defForm = new DecimalFormat("#.######");
                returnValue = String.valueOf(defForm.format(cell.getNumericCellValue()));
            }
        }
        return returnValue;
    }



    /**
     * 转换Excel中的日期Cell 支持Cell中的格式为： Cell格式为字符串：20050505,2005-05-12，
     * Cell格式为日期类型的所有格式
     * 
     * @param cell
     * @return String 返回""值是为错误
     * @throws ParseException
     */
    public static String tranDate(HSSFCell cell){
        String strDate = "";
        if(cell == null){
            return strDate;
        }
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            // 当Cell的格式为字符型时
            try{
              
              /*先尝试用dateToStr和strToDate方法去转换，
                格式为：2005-05-12，2004-5-1,2004-5-01,2004-05-15
                的都可以转换成功，提供程序容错性*/
                
                strDate = ComUtil.dateToStr(ComUtil.formatStrToDate(cell.getStringCellValue().trim(), ""), "");    
            }catch(ParseException e){
                //失败再用其它方法转换
                if (cell.getStringCellValue().trim().length()==8 && cell.getStringCellValue().trim().indexOf('-')<0 ) {
                    // 20050505转换为2005-05-12
                    strDate = cell.getStringCellValue().trim().substring(0, 4)
                            + "-"
                            + cell.getStringCellValue().trim().substring(4, 6)
                            + "-"
                            + cell.getStringCellValue().trim().substring(6, 8);
                } else {
                    if (cell.getStringCellValue().trim().length()==10 && cell.getStringCellValue().trim().indexOf('-')>-1) {
                        // 2005-05-05转换为2005-05-12
                        strDate = cell.getStringCellValue().trim();
                    }
                }
            }
        } else {
            // 当Cell的格式为数字型时（日期在Excel中也是数字型的）
            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                // 当Cell的格式为数字型时
                // 防止出现科学计数法的表示
                NumberFormat defForm = new DecimalFormat("#");
                if (String.valueOf(defForm.format(cell.getNumericCellValue()))
                        .trim().length() == 8) {
                    // 处理20050501为数字Cell的情况
                    String str = String.valueOf(
                            defForm.format(cell.getNumericCellValue())).trim();
                    strDate = str.substring(0, 4) + "-" + str.substring(4, 6)
                            + "-" + str.substring(6, 8);
                } else {
                    strDate = ComUtil.dateToStr(cell.getDateCellValue(), "");
                }

            }
        }
        return strDate;
    }

   
}
