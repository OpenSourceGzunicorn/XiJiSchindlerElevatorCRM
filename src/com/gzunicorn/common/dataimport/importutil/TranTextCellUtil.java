/*
 * Created on 2005-8-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.dataimport.importutil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;


/**
 * Title: 通用类
 * Description: 转换Text文件中的Cell格式(系统级转换，不是逻辑校验),提供单态模式 
 * @author rr
 * @version 1.2
 */
public class TranTextCellUtil {

    private static TranTextCellUtil tranTextCellUtil = new TranTextCellUtil();

    private TranTextCellUtil() {
    }

    public static TranTextCellUtil getInstance() {
        return tranTextCellUtil;
    }
    
    public static String tranStr(String str){
    	if(str==null){
    		return "";
    	}else{
    		return str.trim();
    	}
    }

    /**
     * 转换Text中的日期Cell 支持Cell中的格式为：
     * 20050505,2005-05-12，2004-5-1,
     * 2004-5-01,2004-05-1
     * @param cell
     * @return String
     */
    public static String tranDate(String tranDate) {

        String strDate = tranDate;
        if (tranDate != null && tranDate.trim().length() >= 8) {
            try {
                strDate = ComUtil.dateToStr(ComUtil.formatStrToDate(tranDate.trim(), ""), "");
            } catch (ParseException e) {
                // 失败再用其它方法转换，提高程序容错性
                if (tranDate.trim().length() == 8 && tranDate.trim().indexOf('-') < 0) {
                    // 20050505转换为2005-05-12
                    strDate = tranDate.trim().substring(0, 4) + "-"
                            + tranDate.trim().substring(4, 6) + "-"
                            + tranDate.trim().substring(6, 8);
                } else {
                    if (tranDate.trim().length() == 10
                            && tranDate.trim().indexOf('-') > -1) {
                        // 2005-05-05转换为2005-05-12
                        strDate = tranDate.trim();
                    }
                }

            }
        }
        return strDate;
    }

    /**
     * 数字字符串转换成整数方法
     * 把123.80转换成123
     * @param cell
     * @return String
     */
    public static String tranInteger(String tranStr) {

        String returnValue = "";
        if(ComUtil.isAllDigits(tranStr.trim())){
            double d = new Double(tranStr).doubleValue();
            // 防止出现科学计数法的表示
            NumberFormat defForm = new DecimalFormat("#");
            returnValue = String.valueOf(defForm.format(d));
            
        }
        return returnValue;
    }
}
