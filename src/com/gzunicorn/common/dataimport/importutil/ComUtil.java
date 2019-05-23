/*
 * Created on 2005-8-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.dataimport.importutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通用方法效验
 * @author rr
 *
 */
public class ComUtil {

    private static ComUtil ComUtil = new ComUtil();

    private ComUtil() {
    }

    public static ComUtil getInstance() {
        return ComUtil;
    }


   /*******************公用方法　start*******************************************/
    /**
     * 转换日期类型TO字符串类型
     * 
     * @param date
     *            Date 日期类型
     * @param dateFormat
     *            String 转换后的格式
     * @return String 处理后的日期字符串
     */
    public static String dateToStr(Date date, String dateFormat) {

        // 注意边界值的处理，提高程序的健壮性
        if (date == null) {
            return "";
        }

        if (dateFormat == null || dateFormat.equals("")
                || dateFormat.length() == 0) {
            dateFormat = "yyyy-MM-dd";

        }
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);

        return df.format(date);
    }
    /**
     * 转换字符串类型TO日期类型
     * 
     * @param strDate
     *            String 符串类型
     * @param dateFormat
     *            String 转换后的格式
     * @return date 处理后的日期
     * @throws ParseException
     */
    public static Date formatStrToDate(String strDate, String dateFormat)
            throws ParseException {

        if (dateFormat == null || dateFormat.equals("")
                || dateFormat.length() == 0) {
            dateFormat = "yyyy-MM-dd";
        }
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);

        return df.parse(strDate);
    }
  /**
   * 效验是否是合法的日期，如果是，则将其转换成指定的相应的格式
   * @param strDate
   * @param dateFormat
   * @return
   */
  public static String  strToDate(String strDate, String dateFormat)
          throws ParseException {

      if (dateFormat == null || dateFormat.equals("")
              || dateFormat.length() == 0) {
          dateFormat = "yyyy-MM-dd";
      }
      SimpleDateFormat df = new SimpleDateFormat(dateFormat);

      return df.parse(strDate).toString();
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
   * 效验是否是合法的标识
   * @param id
   * @param flag
   */
  public static boolean isLegalFlag(String id,String[] flag){
	  String[] fl={"Y","N"};
	  if(flag==null||flag.length==0){
		  flag=fl;
	  }
	  for(int i=flag.length-1;i>=0;i--){
		  if(flag[i].equals(id)){
			 return true;
		  }
	  }
	  return false;  
  }
   /**
    * 开始或结束是否是点
    * @param st
    * @param en
    * @return
    */
   public static boolean isStartOrEndIsPoint(char st,char en){
	   if(isPoint(st) || isPoint(en)){
		   return true;
	   }else{
		   return false;
	   }
   }
   /**
    * 效验英文字母
    * @param ch
    * @return
    */
   public static boolean isENChar(char ch){
	   if((ch>='a'&& ch<='z')||(ch>='A'&&ch<='Z')){
		   return true;
	   }else{
		   return false;
	   }
   }
   /**
    * 效验点
    * @param ch
    * @return
    */
   public static boolean isPoint(char ch){
	   if(ch=='.'){
		   return true;
	   }else{
		   return false;
	   }
   }
   /**
    * 效验数字
    * @param ch
    * @return
    */
   
   public static boolean isNum(char ch){
	   if(ch>='0'&&ch<='9'){
		   return true;
	   }else{
		   return false;
	   }
   }
   /**
    * 效验罗马数字
    * @param ch
    * @return
    */
   public static boolean isRomeNum(char ch){//Ⅰ,Ⅱ,Ⅲ,Ⅳ,Ⅴ,Ⅵ,Ⅶ,Ⅷ,Ⅸ,Ⅹ      从excel里取
	   if(ch>='Ⅰ'&& ch<='Ⅹ'){
		   return true;
	   }else{
		   return false;
	   }
   }
   /**
    * 判断是否数值型
    * @param dn
    * @return
    */
   public static boolean isDouNum(String dn){
	   if(dn!=null && dn.length()>0){
		   try{
			   Double.parseDouble(dn);
			   return true;
		   }catch(Exception e){
			   return false;
		   }
	   }else{
		   return false;
	   }
	   
   }
    
  
    /*******************公用方法　end*******************************************/
}
