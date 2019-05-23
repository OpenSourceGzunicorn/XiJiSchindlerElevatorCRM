/*
 * Created on 2005-8-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.util;

import java.text.ParseException;

/**
 * Created on 2005-7-12
 * <p>Title:	通用校验方法</p>
 * <p>Description:	提供单态设计模式</p>
 * <p>Copyright: Copyright (c) 2005
 * <p>Company:友联科技</p>
 * @author wyj
 * @version	1.0
 */
public class ValidatorUtil {

    private static ValidatorUtil validatorUtil = new ValidatorUtil();

    private ValidatorUtil() {
    }

    public static ValidatorUtil getInstance() {
        return validatorUtil;
    }

    /**
     * 校验是否是传入的字符串是否只含有数字（0到9）
     * 当参数为空或长度为0时均返回false
     * @param argValue
     * @return boolean
     * 
     */
    public static boolean isAllDigits(String argValue){
        
        String validChars = "0123456789";
        
        if(argValue == null || argValue.length() == 0) return false;
        
        for(int i=0; i<argValue.length(); i++){
            if(validChars.indexOf(argValue.charAt(i)) == -1) return false;    
        }

        return true;
    }

    
    /**
     * 校验是否是传入的字符串是否是大于或等于0的整数(正整数)
     * 
     * @param argValue
     * @return boolean
     * 
     */
    public static boolean isPlusInteger(String argValue){
        
        if(!isAllDigits(argValue)) return false;
        
        long iValue = Long.parseLong(argValue);
        //if(!(iValue >= -2147483648 && iValue <= 2147483647)) return false;
        if(!(iValue >= 0 && iValue <= 2147483647)) return false;
        return true;
    }

    /**
     * 校验是否是传入的字符串是否是小于或等于0的整数(负整数)
     * 
     * @param argValue
     * @return boolean
     * 
     */
    
    public static boolean isNegativeInteger(String argValue){
        
        if(argValue == null || argValue.length() == 0) return false;
        
        if(argValue.charAt(0) != '-'){
            if(argValue.charAt(0) != '0'){
                return false;                
            }
        }
        
        if(argValue.charAt(0) == '0'){
            if(!isAllDigits(argValue)) return false;    
        }else{
            if(!isAllDigits(argValue.substring(1))) return false;
        }
        
        long iValue = Long.parseLong(argValue);
        if(!(iValue >= -2147483648 && iValue <= 0)) return false;

        return true;
    }

    /**
     * 校验是否是传入的字符串是否是整数,包括负数
     * 
     * @param argValue
     * @return boolean
     * @throws IOException
     */
    public static boolean isInteger(String argValue){
        
        if(!isPlusInteger(argValue)){
            if(!isNegativeInteger(argValue)){
                return false;
            }
        }
        return true;
    }

    /**
     * 校验日期
     * 
     * @param argValue
     * @return boolean
     * @throws IOException
     */
    public static boolean isDate(String argValue){
        
        if(argValue==null || argValue.length()==0){
            return false;
        }
        
        try {
            CommonUtil.dateToStr(CommonUtil.strToDate(argValue, ""), "");
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
    
    /**
     * 判断当前的文件名的后缀名是为argValue名
     * @param fileName 要比较的文件名
     * @param argvalue 要比较的文件名的后缀名
     * @return boolean
     */
    public static boolean isFileName(String fileName,String argValue){
        if(fileName == null || fileName.length() < 5 || argValue == null){
            return false;
        }else{
            if(argValue.equals(fileName.substring(fileName.length()-3,fileName.length()))){
                return true;                
            }else{
                return false;
            }
        }
    }
    
}
