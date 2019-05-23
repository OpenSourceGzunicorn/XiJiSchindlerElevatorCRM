/*
 * Created on 2005-7-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Properties;
import java.util.Calendar;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.LabelValueBean;

import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplandetail.MaintenanceWorkPlanDetail;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplanmaster.MaintenanceWorkPlanMaster;
import com.gzunicorn.hibernate.sysmanager.Getnum;
import com.gzunicorn.hibernate.sysmanager.GetnumKey;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on 2005-7-12
 * <p>
 * Title: 通用程序处理
 * </p>
 * <p>
 * Description:提供单态设计模式
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * <p>
 * Company:友联科技
 * </p>
 * 
 * @author wyj
 * @version 1.0
 */
public class CommonUtil {

    private static CommonUtil commonUtil = new CommonUtil();
    private static final long MSECONDS_OF_ONE_DAY = 60 * 60 * 1000 * 24;
    public final static DateFormat commonFormat = new SimpleDateFormat("yyyy-MM-dd");

    private CommonUtil() {
    }

    public static CommonUtil getInstance() {
        return commonUtil;
    }

    /**
     * 将对象序列化成二进制byte[]
     * 
     * @param obj
     *            Object
     * @throws IOException
     * @return byte[]
     */
    public static byte[] serializeObject(Object obj) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes);
        out.writeObject(obj);
        out.flush();
        out.close();
        return bytes.toByteArray();
    }

    /**
     * 将byte[]反序列化为对象 需要将涉及到的class在import中引入,否则抛出ClassNotFoundException
     * 
     * @param serializeObject
     *            byte[]
     * @throws ClassNotFoundException
     * @throws IOException
     * @return Object
     */
    public static Object deSerializeObject(byte[] serializeObject)
            throws ClassNotFoundException, IOException {
        ByteArrayInputStream bytes = new ByteArrayInputStream(serializeObject);
        ObjectInputStream in = new ObjectInputStream(bytes);
        return in.readObject();
    }

    /**
     * 把字符串分拆到List中
     * 
     * @param str
     *            String 被分拆的字符串
     * @param sep
     *            String 分拆符
     * @return List 返回分拆后存放内容的List
     */
    public static List sepString(String str, String sep) {
        List list = new ArrayList();

        StringTokenizer st = new StringTokenizer(str, sep);
        try {
            list.add(st.nextToken());
            while (st.hasMoreElements()) {
                list.add(leftTrim(st.nextToken()));
            }
        } catch (Exception e) {
            //System.out.println("StringTokenizer分拆字符串失败!");
        }
        return list;
    }

    public static String leftTrim(String str) {
        int i = 0;
        for (; i < str.length(); i++) {
            if (str.charAt(i) != 32) {
                break;
            }
        }
        return str.substring(i);
    }

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
    public static Date strToDate(String strDate, String dateFormat)
            throws ParseException {

        if (dateFormat == null || dateFormat.equals("")
                || dateFormat.length() == 0) {
            dateFormat = "yyyy-MM-dd";
        }
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);

        return df.parse(strDate);
    }

    /**
     * 转换字符串类型TO SQL日期类型
     * 
     * @param strDate
     *            String 符串类型
     * @param dateFormat
     *            String 转换后的格式
     * @return String 处理后的日期字符串
     * @throws ParseException
     */
    public static java.sql.Date strToSQLDate(String strDate, String dateFormat)
            throws ParseException {
        if (strDate == null || strDate.trim().equals("")) {
            return null;
        }
        return new java.sql.Date(strToDate(strDate, dateFormat).getTime());
    }

    public static Time strToSQLTime(String time) {
        return Time.valueOf(time);
    }

    public static long subDate(Date from, Date to) throws ParseException {
        long value = Math.abs(to.getTime() - from.getTime());
        return value / MSECONDS_OF_ONE_DAY + 1;
    }

    public static long subDate(String from, String to) throws ParseException {
        return subDate(commonFormat.parse(from), commonFormat.parse(to));
    }

    public static String getToday() throws ParseException {

        return commonFormat.format(new Date());

    }

    /**
     * @param records
     *            String
     * @param sep
     *            String
     * @return String 返回选中的第一条记录的关键字
     */
    public static String getFirstRecord(String records, String sep) {
        String record = null;
        if (records == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(records, sep);
        try {
            record = st.nextToken();

        } catch (Exception e) {
            //System.out.println("StringTokenizer分拆字符串失败!");
        }
        return record;
    }

    public static String concatByComma(String str1, String str2)
            throws Exception {
        if (str2 == null) {
            throw new Exception(
                    "The second parameter of concatByComma method cannot be null.");
        }
        if (str1 == null) {
            return str2;
        } else {
            return str1 + "," + str2;
        }
    }

    public static String listToStr(List seqs, String separator) {

        if (seqs == null || seqs.size() == 0)
            return null;
        StringBuffer str = new StringBuffer();
        if (seqs.size() > 1)
            for (int i = 0, n = seqs.size(); i < n - 1; i++)
                str.append((String) seqs.get(i) + separator);
        str.append((String) seqs.get(seqs.size() - 1));

        return str.toString();
    }

    /**
     * 将通过Java Script传过来以"5, 6,....."形式记录字符串转 换成为Oracle中设定范围的字符串，此方法主要是针对传递的记
     * 录数为字符串形式的情况。 for example: input as : ta1, tb2, tb3, tb4 output as :
     * {'ta1', 'tb2', tb3', 'tb4'}
     * 
     * @param recordStr
     *            String
     * @return String
     */
    public static String strToLocation(String recordStr) {
        StringBuffer buf = null;
        StringTokenizer location = null;

        buf = new StringBuffer();
        location = new StringTokenizer(recordStr, " ,");
        while (location.hasMoreTokens()) {
            buf.append("'");
            buf.append(location.nextToken());
            buf.append("'");
            buf.append(",");
        }
        return buf.toString().substring(0, (buf.length() - 1));
    }

    public static String getProperty(String fileName, String name) {
        Properties properties = null;
        try {
            properties = new Properties();
            InputStream in = commonUtil.getClass().getClassLoader()
                    .getResourceAsStream(fileName);
            properties.load(in);
            in.close();
        } catch (IOException e) {
            System.err.println("load properties file error!");
            e.printStackTrace();
        }

        return properties.getProperty(name);
    }

    /**
     * @param date1
     *            Date
     * @param date2
     *            Date
     * @return int 0:date1==date2, <0:date1 <date2,>0:date1>date2
     */

    public static int compareDateWithoutTime(Date date1, Date date2) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date1);
        ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca
                .get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        date1 = ca.getTime();
        ca.setTime(date2);
        ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca
                .get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        date2 = ca.getTime();
        return date1.compareTo(date2);

    }

    public static long getLongTime(Date date) throws Exception {
        if (date == null)
            throw new Exception();
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int day = ca.get(Calendar.DAY_OF_MONTH);
        Date current = ca.getTime();
        ca.setTime(date);
        ca.set(Calendar.YEAR, year);
        ca.set(Calendar.MONTH, month);
        ca.set(Calendar.DAY_OF_MONTH, day + 1);

        date = ca.getTime();
        return date.getTime() - current.getTime();
    }

    public static long getLongTimeByStrDate(String date) throws Exception {
        DateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
        Date dDate = null;
        if (date == null || date == "")
            date = "00:00:00:000";
        try {
            dDate = format.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            dDate = new Date();
        }

        return getLongTime(dDate);
    }

    /**
     * 转换是否状态
     * 
     * @param enabledFlag
     * @return
     */
    public static String tranEnabledFlag(String enabledFlag) {
        return enabledFlag.equals("Y") ? "是" : "否";
    }
    
    /**
     * 转换有无状态
     * 
     * @param enabledFlag
     * @return
     */
    public static String tranHaveFlag(String enabledFlag) {
        return enabledFlag.equals("Y") ? "有" : "无";
    }
    
    /**
     * 转换电梯产品功能标准或非标准组件
     * 
     * @param flag
     */
    public static String tranFlag(String flag) 
    {
    	return flag.equals("0") ? "功能要求" : "装饰要求";
    }
    
  
    /**
     * 转换电梯产品功能标准或非标准组件
     * 
     * @param flag
     */
    public static String tranStandardFlag(String standardflag) 
    {
    	return standardflag.equals("0") ? "标准" : "非标准";
    }
    
    /**
     * 转换电梯产品功能标准或非标准组件
     * 
     * @param flag
     */
    public static String tranViewBusinessType(String viewbusinesstype) 
    {
    	return viewbusinesstype.equals("0") ? "工厂" : "贸易";
    }
    
    public static void main(String[] agrs) {
        //System.out.println(strToSQLTime("11:11:11"));

    }

    /**
     * 中文转换字符集;
     * 
     * @param argString
     * @return
     */
    public static String setChinese(String argString) {
        String returnString = argString;
        try {
            returnString = new String(argString.getBytes("UTF-16"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnString;

    }

    /**
     * 随机生成数字格式的字符串数组
     * 
     * @param randomLen
     *            需要生成单个字符串的长度
     * @param argLen
     *            需要生成字符串的数组的长度
     * @return String[] 返回字符串数组
     */
    public static String[] randomInt(int randomLen, int argLen) {

        if (argLen < 0) {
            DebugUtil.println("数组长度参数不能小于0!");
            return null;
        }

        if (randomLen <= 0) {
            DebugUtil.println("随机生成的字符串长度参数不能小于或等于0!");
            return null;
        }

        String randomInt[] = new String[argLen];
        int tempInt = 0;
        Random r = new Random();
        for (int i = 0; i < argLen; i++) {
            StringBuffer tempStr = new StringBuffer("");
            for (int j = 0; j < randomLen; j++) {
                tempStr = tempStr.append(r.nextInt(10));
            }
            randomInt[i] = tempStr.toString();
        }
        return randomInt;
    }

    /**
     * 生成流水号，二位年份＋序号（0500000010）
     * @param year1
     * 			当前年份，二位长度（05）
     * @param deptFlag
     * 			流水号标志（字符型）    
     * @param jnlLen
     *            需要生成单个字符串的长度
     * @param argLen
     *            需要生成流水号的数组的长度（序列号的个数）
     *         dataBaseFlag 
     *            如果是基础数据,则是Y , N为顺序号   
     * @return String[] 返回字符串数组
     * 
     */
    public synchronized static String[] getJnlNo(String year1, String deptFlag, int jnlLen,
            int argLen,String dataBaseFlag ) {
    	
    	String vlaueString = new String();
    	  
        if (argLen < 0) {
            DebugUtil.println("数组长度参数不能小于0!");
            return null;
        }
    
       if  (dataBaseFlag.equals("N")){
    	   vlaueString = year1;
        if (jnlLen <= 5) {
            DebugUtil.println("随机生成的字符串长度参数不能小于或等于4!");
            return null;
            
        }
        
       }else{
    	   vlaueString = deptFlag;
    	   
       }
      
       
       
    if (jnlLen >= 9) {
 	   DebugUtil.println("随机生成的字符串长度参数不能大于9!");
        return null; 
      }
    
        if (year1 == null || year1.length() != 2) {
            DebugUtil.println("年份参数长度错误!");
            return null;
        }

        
        String jnlNo[] = new String[argLen];
        int tempInt = 100000000;
        int nextNo = 0;
        int tempNum  = 0;
       
        tempNum = 9 - jnlLen ;
        
        ArrayList list = null;
        Session session = null;
        
        Getnum getnum = new Getnum();
    	GetnumKey  getnumkey = new GetnumKey();
    	Transaction tx = null;
        try {
        	
            session = HibernateUtil.getSession();
            Query query = session
                    .createQuery("FROM Getnum as a WHERE a.id.year1=:year1 AND a.id.deptflag = :deptflag");
            query.setString("year1", year1);
            query.setString("deptflag", deptFlag);
            
            tx = session.beginTransaction();
            list = (ArrayList) query.list();
          
            if (list == null || list.isEmpty() || list.size() == 0) {
                // 还没有记录，序列号从1开始
                for (int i = 0; i < argLen; i++) {
                    jnlNo[i] = vlaueString + String.valueOf(tempInt + nextNo + 1).substring(tempNum,9).trim();
                    nextNo++;
                }
                
                getnumkey.setYear1(year1);
                getnumkey.setDeptflag(deptFlag);
                getnum.setId(getnumkey);
               
            } else {
                getnum = (Getnum) list.get(0);
                nextNo = getnum.getNextno().intValue();
                if (nextNo == 1 ){
                	nextNo = 2 ; 
                }
                
                for (int i = 0; i < argLen; i++) {
                    jnlNo[i] = vlaueString + String.valueOf(tempInt + nextNo).substring(tempNum,9).trim();
                    nextNo++;
                }
               
            }
            getnum.setNextno(new Integer(nextNo));
            session.save(getnum);
            tx.commit();
        } catch (DataStoreException dex) {
            try {
                tx.rollback();
            } catch (HibernateException e) {
                // TODO Auto-generated catch block
                DebugUtil.print(e, "Hibernate Rolenode Save error!");
            }
            DebugUtil.print(dex, "HibernateUtil：Hibernate 连接 出错！");
        } catch (HibernateException hex) {
            try {
                tx.rollback();
            } catch (HibernateException e) {
                DebugUtil.print(e, "Hibernate Rolenode Save error!");
            }
            DebugUtil.print(hex, "HibernateUtil：Hibernate Session 打开出错！");
        } finally {
            try {
                session.close();
            } catch (HibernateException hex) {
                DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
            }
        }
        return jnlNo;
    }
    /**
     * 从输入流中复制文件
     * @param is 输入流
     * @param os 输出流
     * @param isClose 复制完后是否关闭输入流
     * @param osClose 复制完后是否关闭输出流
     * @throws IOException
     */
    public static void copyFile(InputStream is,OutputStream os,boolean isClose,boolean osClose)
        throws IOException{
            int b;//从文件读入的字节
            while((b = is.read()) != -1){
                os.write(b);
            }
            
            if(is != null && isClose){
                is.close();
            }
            
            if(os != null && osClose){
                os.close();
            }
    }

    /**
     * 取服务器的当前时间
     * @return hh:mm:ss 格式的时间
     */
    public static String getTodayTime(){
            String time = new Timestamp(System.currentTimeMillis()).toString();
    		return time.substring(11,19);
    }
    /**
     * 取服务器的当前时间
     * @return hhmmss 格式的时间
     */
    public static String getTodayTimeFormat(){
            String time = new Timestamp(System.currentTimeMillis()).toString();
    		time =  time.substring(11,19);
    		return time.substring(0,2) + time.substring(3,5) + time.substring(6,8);
    }	
    
    /**
     * 取后一天的日期
     * @param date
     * @return yyyy-mm-dd 格式的日期
     * @throws ParseException 
     */
    public static Date getNextDay(Date date) throws ParseException{
            String Sdate = CommonUtil.dateToStr(date,"");
    		try
			{
    			String day =  Sdate.substring(8,10);
    			int Iday = Integer.parseInt(day);
    			Iday = Iday + 1;
    			Sdate = Sdate.substring(0,8)+Iday;
			}catch(Exception e)
			{
				DebugUtil.print(e, "?????????í?ó??");
			}
    		
 			return strToDate(Sdate,"");

    }
    
 /**
     * 取前一天的日期
     * @param date
     * @return yyyy-mm-dd 格式的日期
     * @throws ParseException 
     */
    public static Date getBeforeDay(Date date) throws ParseException{
            String Sdate = CommonUtil.dateToStr(date,"");
    		try
			{
    			String day =  Sdate.substring(8,10);
    			int Iday = Integer.parseInt(day);
    			Iday = Iday - 1;
    			Sdate = Sdate.substring(0,8)+Iday;
			}catch(Exception e)
			{
				DebugUtil.print(e, "?????????í?ó??");
			}
    		
 			return strToDate(Sdate,"");
    }
    
    /**
     * 
     * 规范float类型数据超过百万时出现E的科学计数法的方法
     * create by cwy
     */
 
    public static String formatprice (double num)
    {
    	String price = "";
    	try
    	{
    	  DecimalFormat mat = new DecimalFormat("##.#");
    	  price = mat.format(num);
    	  
    	}
    	catch(Exception e)
    	{
    		DebugUtil.print(e,"数据格式化异常!");
    	}
    	
    	return price;
    }
    public static String formatprice (double num,String format)
    {
    	String price = "";
    	try
    	{
    		if(null == format || "".equals(format)){
    			format = "##.#";
    		}
    		DecimalFormat mat = new DecimalFormat(format);
    		price = mat.format(num);
    		
    	}
    	catch(Exception e)
    	{
    		DebugUtil.print(e,"数据格式化异常!");
    	}
    	
    	return price;
    }
    /**
     * 
     * 规范double类型数据转成中文大写
     * create by zzg
     */
    
    public static String formatPriceUpCase (Double num,Integer bit)
    {
    	int maxlen = 12;
    	String unit = "仟,佰,拾,亿,仟,佰,拾,万,仟,佰,拾,元,角,分";
    	String unitArr[] = unit.split(",");
    	String upcase = "零,壹,贰,叁,肆,伍,陆,柒,捌,玖";
    	String upcaseArr[] =upcase.split(",");
    	DecimalFormat mat = new DecimalFormat("##.##");
    	String price = "";
    	String rt ="";
    	try
    	{
    		if(null != bit && null != num && bit > 0 && bit <13){
    			String numStr = mat.format(num);
    			numStr=FormatNumber(numStr,2);
    			String numArr[] =numStr.split("\\.");
    			String prefix = numArr[0];
    			String backfix = numArr[1];
//    			//System.out.println(prefix);
//    			//System.out.println(backfix);
    			//12345678900
//    			for(int i = (12-bit) ;i < 14; i++ ){
//    				//System.out.print(unitArr[i]);
//    			}
    			price =prefix+backfix;
    			//System.out.println(price);
    			int len = price.length();
    			String zero = "";
    		
    			if(bit+2 > len){
    				for(int i = 0 ; i < bit+2 -len; i++){
    					zero +="0";
    				}
    			}
    			
    			price = zero+price;
    			len = price.length();
    			if(bit+2 < len){
    				bit = len;
    			}
    			for(int i = 0 ; i < len ; i++){
    				String temp = upcaseArr[Integer.parseInt(price.charAt(i)+"")];
    				String temp2 = unitArr[(maxlen-bit+i)];
//    				rt = rt+" "+temp+" "+temp2;
    				rt = rt+temp;
    			}
//    			//System.out.println(price);
    		}else{
    			
    		}
    		
//    		DecimalFormat mat = new DecimalFormat("##.#");
//    		price = mat.format(num);
    		
    	}
    	catch(Exception e)
    	{
    		DebugUtil.print(e,"数据格式化异常!");
    	}
    	
    	return rt;
    }
    /**
     * 
     * 规范double类型数据转成中文大写并带仟佰拾万仟佰拾元角分 单位
     * 最大提供12位数的转化,即仟亿
     * 两位小数
     * create by zzg
     */
    
    public static String formatPriceUpCaseWithUnit(Double num,Integer bit)
    {
    	int maxlen = 12;
    	String unit = "仟,佰,拾,亿,仟,佰,拾,万,仟,佰,拾,元,角,分";
    	String unitArr[] = unit.split(",");
    	String upcase = "零,壹,贰,叁,肆,伍,陆,柒,捌,玖";
    	String upcaseArr[] =upcase.split(",");
    	DecimalFormat mat = new DecimalFormat("##.##");
    	String price = "";
    	String rt ="";
    	try
    	{
    		if(null != bit && null != num && bit > 0 && bit <13){
    			String numStr = mat.format(num);
    			numStr=FormatNumber(numStr,2);
    			String numArr[] =numStr.split("\\.");
    			String prefix = numArr[0];
    			String backfix = numArr[1];
//    			//System.out.println(prefix);
//    			//System.out.println(backfix);
    			//12345678900
//    			for(int i = (12-bit) ;i < 14; i++ ){
//    				//System.out.print(unitArr[i]);
//    			}
    			price =prefix+backfix;

    			int len = price.length();
    			String zero = "";
    		
    			if(bit+2 > len){
    				for(int i = 0 ; i < bit+2 -len; i++){
    					zero +="0";
    				}
    			}
    			
    			price = zero+price;
    			len = price.length();
    			if(bit+2 < len){
    				bit = len;
    			}
    			for(int i = 0 ; i < len ; i++){
    				String temp = upcaseArr[Integer.parseInt(price.charAt(i)+"")];
    				String temp2 = unitArr[(maxlen-bit+i)];
    				rt = rt+" "+temp+" "+temp2;
    			}
//    			//System.out.println(price);
    		}else{
    			
    		}
    		
//    		DecimalFormat mat = new DecimalFormat("##.#");
//    		price = mat.format(num);
    		
    	}
    	catch(Exception e)
    	{
    		DebugUtil.print(e,"数据格式化异常!");
    	}
    	
    	return rt;
    }
    
    /**
     * 打印列表
     * @param title
     * @param list
     */

    public static void toPrint(String title,List list){ 
		////System.out.println("----"+title+"----start----");
		if(list!=null){ 
			int len=list.size();
			for(int i=0;i<len;i++){
				//System.out.println("--"+i+"--"+list.get(i));
			}
		}
		////System.out.println("----"+title+"----end----");
	}
    
    

    /**
     * 
     * @param year1:年份
     * @param deptFlag:传入来的流水号的开始字符
     * @param jnlLen:流水号的长度
     * @param dataBaseFlag:是否把deptFlag作为流水号的开始字符的标志，Y代表是，N代表否(用传入的年份)
     * @param firstFlag:是否第一次生成流水号
     * @param verLen:版本号的长度
     * @return
     */
    public static String getQuoteNO(String year1, String deptFlag, int jnlLen,
    								String dataBaseFlag, String firstFlag,int verLen) {
    	
    	String vlaueString = new String();
    	
    	dataBaseFlag = "Y";
    	
    	 
    	String str_cut_before = "";
    	if(firstFlag.equals("N")){
    		String str_cut[] = deptFlag.split("_");
    		if(str_cut.length>0){
    			str_cut_before = str_cut[0];
//        		cutLen = str_cut_before.length();
        		deptFlag = str_cut_before;
    		}
    		
    	}

        if (jnlLen <= 5) {
            DebugUtil.println("随机生成的字符串长度参数不能小于或等于4!");
            return null;
            
        }
        
        if (dataBaseFlag.equals("N")){
    	   vlaueString = year1;
 
        }else{
    	   vlaueString = deptFlag;
    	   
        }
         
        if (jnlLen >= 9) {
        	DebugUtil.println("随机生成的字符串长度参数不能大于9!");
        	return null; 
        }
    
        if (year1 == null || year1.length() != 2) {
            DebugUtil.println("年份参数长度错误!");
            return null;
        }

        
//        String jnlNo[] = new String[argLen];
        String quoteNO = new String("");
        int tempInt = 100000000;
        int nextNo = 0;
        int tempNum  = 0;
       
        tempNum = 9 - jnlLen ;
        
        ArrayList list = null;
        Session session = null;
        
        Getnum getnum = new Getnum();
    	GetnumKey  getnumkey = new GetnumKey();
    	Transaction tx = null;
        try {
        	
            session = HibernateUtil.getSession();
            
            
            
            Query query = session
                    .createQuery("FROM Getnum as a WHERE a.id.year1=:year1 AND a.id.deptflag = :deptflag");
            query.setString("year1", year1);
            query.setString("deptflag", deptFlag);
            
            tx = session.beginTransaction();
            list = (ArrayList) query.list();
          
            
            //如果是对某一个项目第一次生成的话
            if(firstFlag.equals("Y")){
            	
            	if (list == null || list.isEmpty() || list.size() == 0) {
                    // 还没有记录，序列号从1开始
            		
            		String para = "1";
            		String verNO = "";
            		while(verLen-para.length()>0){
            			para = "0"+para;
            		}
            		verNO = para;
            		
            		nextNo = nextNo + 1;
            		
                	quoteNO = vlaueString + String.valueOf(tempInt + nextNo).substring(tempNum,9).trim() + "_" + verNO;
                    
                    getnumkey.setYear1(year1);
                    getnumkey.setDeptflag(deptFlag);
                    getnum.setId(getnumkey);
                   
                } else {
                    getnum = (Getnum) list.get(0);
                    nextNo = getnum.getNextno().intValue();
                    if (nextNo == 1 ){
                    	nextNo = 2 ; 
                    }else{
                    	nextNo = nextNo + 1;
                    }
                    
                    String para1 = "1";
            		String verNO1 = "";
            		while(verLen-para1.length()>0){
            			para1 = "0"+para1;
            		}
            		verNO1 = para1;
                    
                    
                    quoteNO = vlaueString + String.valueOf(tempInt + nextNo).substring(tempNum,9).trim() + "_" + verNO1;
                   
                }
            }
            //否则对已经曾经生成过的
            else{
            	
            	if (list == null || list.isEmpty() || list.size() == 0) {
                    // 还没有记录，版本号从2开始
            		nextNo = nextNo + 2;
            		String versionno = "";
            		String nextNo_str = String.valueOf(nextNo);
            		while(verLen-nextNo_str.length()>0){
            			nextNo_str = "0"+nextNo_str;
            		}
            		versionno = nextNo_str;
            		
                	quoteNO = vlaueString + "_" + versionno;
                    
                    getnumkey.setYear1(year1);
                    getnumkey.setDeptflag(deptFlag);
                    getnum.setId(getnumkey);
                   
                } else {
                    getnum = (Getnum) list.get(0);
                    nextNo = getnum.getNextno().intValue();
                    if (nextNo == 2 ){
                    	nextNo = 3 ; 
                    }else{
                    	nextNo = nextNo + 1;
                    }
                    
                    String versionno1 = "";
                    String nextNo_str1 = String.valueOf(nextNo);
                    while(verLen-nextNo_str1.length()>0){
                    	nextNo_str1 = "0" + nextNo_str1;
                    }
                    
                    versionno1 = nextNo_str1;
                    
                    quoteNO = vlaueString + "_" + versionno1;
                   
                }
            }
            
            
            getnum.setNextno(new Integer(nextNo));
            session.save(getnum);
            tx.commit();
        } catch (DataStoreException dex) {
            try {
                tx.rollback();
            } catch (HibernateException e) {
                // TODO Auto-generated catch block
                DebugUtil.print(e, "Hibernate Rolenode Save error!");
            }
            DebugUtil.print(dex, "HibernateUtil：Hibernate 连接 出错！");
        } catch (HibernateException hex) {
            try {
                tx.rollback();
            } catch (HibernateException e) {
                DebugUtil.print(e, "Hibernate Rolenode Save error!");
            }
            DebugUtil.print(hex, "HibernateUtil：Hibernate Session 打开出错！");
        } finally {
            try {
                session.close();
            } catch (HibernateException hex) {
                DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
            }
        }
//        return jnlNo;
        return quoteNO;
    }
    
    
    
    
    
    
    
    
    public static String  getQuotaNO(String year1, String deptFlag, int jnlLen,String dataBaseFlag ,int verLen ,String jnlno) {
    	
    	String vlaueString = new String();
    	
    	String quotano = new String ("");
    	
    	dataBaseFlag = "Y";//限制一定以传入的参数为流水号的开头字符

    	
    	if (jnlLen <= 5) {
            DebugUtil.println("随机生成的字符串长度参数不能小于或等于4!");
            return null;  
        }
    	if (jnlLen >= 9) {
    	 	   DebugUtil.println("随机生成的字符串长度参数不能大于9!");
    	        return null; 
    	}
    	
    	
    	//如果传入来的deptFlag为空，则赋一个初值
    	if(deptFlag == null || deptFlag.equals("")){
    		try {
				deptFlag = CommonUtil.getToday().substring(2,4)+"A";
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			
			if  (dataBaseFlag.equals("N")){
		    	   vlaueString = year1;
		        
		        
		       }else{
		    	   vlaueString = deptFlag;
		    	   
		       }
			
			
	        int tempInt = 100000000;
	        int nextNo = 0;
	        int tempNum  = 0;
	       
	        tempNum = 9 - jnlLen ;
	        
	        ArrayList list = null;
	        Session session = null;
	        
	        Getnum getnum = new Getnum();
	    	GetnumKey  getnumkey = new GetnumKey();
	    	Transaction tx = null;
	        try {
	        	
	            session = HibernateUtil.getSession();
	            Query query = session
	                    .createQuery("FROM Getnum as a WHERE a.id.year1=:year1 AND a.id.deptflag = :deptflag");
	            query.setString("year1", year1);
	            query.setString("deptflag", deptFlag);
	            
	            tx = session.beginTransaction();
	            list = (ArrayList) query.list();
	          
	            if (list == null || list.isEmpty() || list.size() == 0) {
	                // 还没有记录，序列号从1开始
	                
	            	
            		String verNO = "";
//            		String[] ver_arr = year1.split("_");
//            		if(ver_arr.length>0){
//            			verNO = ver_arr[1];
//            		}else{
//            			String para = "1";
//                		while(verLen-para.length()>0){
//                			para = "0"+para;
//                		}
//                		verNO = para;
//            		}
            		
            		String para = "1";
            		while(verLen-para.length()>0){
            			para = "0"+para;
            		}
            		verNO = para;
            		
            		
            		nextNo = nextNo + 1;
            		
                	quotano = vlaueString + String.valueOf(tempInt + nextNo).substring(tempNum,9).trim() + "_" + verNO;
                    
                    getnumkey.setYear1(year1);
                    getnumkey.setDeptflag(deptFlag);
                    getnum.setId(getnumkey);
	                
	                
	               
	            } else {
	            	getnum = (Getnum) list.get(0);
                    nextNo = getnum.getNextno().intValue();
                    if (nextNo == 1 ){
                    	nextNo = 2 ; 
                    }else{
                    	nextNo = nextNo + 1;
                    }
                    
                    
                    String verNO1 = "";
//            		String[] ver_arr1 = year1.split("_");
//            		if(ver_arr1.length>0){
//            			verNO1 = ver_arr1[1];
//            		}else{
//            			String para1 = "1";
//                		while(verLen-para1.length()>0){
//                			para1 = "0"+para1;
//                		}
//                		verNO1 = para1;
//            		}
                    String para1 = "1";
            		while(verLen-para1.length()>0){
            			para1 = "0"+para1;
            		}
            		verNO1 = para1;
            		
                    
                    
                    quotano = vlaueString + String.valueOf(tempInt + nextNo).substring(tempNum,9).trim() + "_" + verNO1;
	               
	            }
	            getnum.setNextno(new Integer(nextNo));
	            session.save(getnum);
	            tx.commit();
	        } catch (DataStoreException dex) {
	            try {
	                tx.rollback();
	            } catch (HibernateException e) {
	                // TODO Auto-generated catch block
	                DebugUtil.print(e, "Hibernate Rolenode Save error!");
	            }
	            DebugUtil.print(dex, "HibernateUtil：Hibernate 连接 出错！");
	        } catch (HibernateException hex) {
	            try {
	                tx.rollback();
	            } catch (HibernateException e) {
	                DebugUtil.print(e, "Hibernate Rolenode Save error!");
	            }
	            DebugUtil.print(hex, "HibernateUtil：Hibernate Session 打开出错！");
	        } finally {
	            try {
	                session.close();
	            } catch (HibernateException hex) {
	                DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
	            }
	        }
		
    	}
    	
    	//否则以传入的为参数
    	else{
    		
    		String[] arr =  jnlno.split("_");
    		String str_after = "";
    		
    		if(arr.length>0){
    			str_after = arr[1];
    		}
    		
    		quotano = deptFlag + "_" + str_after;
    		
    		
    		
    		
    	}

        return quotano;
	
    }
    /*
     * 取当天的上一个月的日期
     * create by cwr  2006-05-19
     * @parm   当前日期 
     */
    public static String getPreMonDay(String today)throws ParseException{
		if(today==null || today.length()==0){
			today=CommonUtil.getToday();
		}
		String[] temp=today.split("-");
		int mm=Integer.parseInt(temp[1]);
		if(mm==1){
			int yy=Integer.parseInt(temp[0]);
			temp[0]=(yy-1)+"";
			temp[1]="12";
		}else{
			temp[1]=(mm-1)<10?"0"+(mm-1):(mm-1)+"";
		}
		return temp[0]+"-"+temp[1]+"-"+temp[2];
	}
    
    /*
     * 截取字符"-",返回字符"-"后的面的字符,主要用来截取合同技术参数序号(parmid)
     * @parm:  parmid：序号
     * create by cwr 2006-08-06
     */
    public static String subParmID(String parmid)
    {
    	String subparmid = "";
    	if(parmid != null && !parmid.equals(""))
    	{
    		String[] temp = parmid.split("-");
    		subparmid = temp[temp.length - 1];
    	}
    	return subparmid;
    }
    
    /*
     * 日期格式化方法,主要把用户输入不规范的日期格式规范化成统一格式
     * @parm:  date:用户输入的日期    dateFormat:要格式化成的日期格式,默认格式为"yyyy-MM-dd"
     * create by cwr 2006-09-20
     */
    public static String FormatDate(String date,String dateFormat) throws ParseException{
		if(date==null || date.trim().length()==0){
			return "";
		}
		if (dateFormat == null || dateFormat.equals("")
		         || dateFormat.length() == 0) {
		     dateFormat = "yyyy-MM-dd";
		 }
		 SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		return df.format(df.parse(date));
	}

    /**
     * 
     * @param f 要处理的数字
     * @param i 要保留的小数位数
     * @return fn
     */
   /* public  static String FormatNumber(float f,int i){
		 String fn="";
	   	 BigDecimal b1 = new BigDecimal(Float.toString(f)); 
		 BigDecimal b2 = new BigDecimal(Double.toString(1));
	     fn=b1.divide(b2, i, BigDecimal.ROUND_HALF_UP).toString();
		 return fn;
    }
    */
    
    /**
     * 
     * @param str_num 要处理的数字
     * @param i 要保留的小数位数
     * @return fn
     */
    public  static String FormatNumber(String str_num,int i){
		 String d="0.00";
	   	 BigDecimal b1 = new BigDecimal(str_num); 
		 BigDecimal b2 = new BigDecimal(Double.toString(1));
	     d=b1.divide(b2, i, BigDecimal.ROUND_HALF_UP).toString();
		 return d;
}
    

	/**
	 * 转换成千分符 000，000.00的形式
	 * @param str_num 要转换的数字 str
	 * @param i 保留小数位数
	 * @return
	 */
    public static String formatThousand(String str_num,int i){
		 String d="0.00";
		 String price="0";
		 double num=0.00;
		 NumberFormat format=NumberFormat.getInstance();
	   	 BigDecimal b1 = new BigDecimal(str_num); 
		 BigDecimal b2 = new BigDecimal(Double.toString(1));
	     d=b1.divide(b2, i, BigDecimal.ROUND_HALF_UP).toString();
//	     //System.out.println("str_num>>>"+str_num);
//	     //System.out.println("d>>>"+d);
	     try {
			num=format.parse(d).doubleValue();//讲String 型转换成double型
		} catch (ParseException e) {
			DebugUtil.print("CommonUtil formatThousand is error!");
			e.printStackTrace();
		}
         price=format.format(num);
//         //System.out.println("num>>>"+num);
//         //System.out.println("price>>>"+price);
    	return price;
    }
	/**
		 * 四舍五入的通用程序
		 * 
		 * @param Dight:要处理的数字
		 * @param How:保留的小数位
		 * @return
		 */
	public static float getRound(float Dight, int How) {

			Dight = (float) (Math.round(Dight * Math.pow(10, How)) / Math.pow(10,
					How));
			return Dight;

	}
	
	/**
	 * 根据分隔符分解字符串 对于某些特殊的分隔符如‘.’不可用split分隔的情况，使用此函数来拆成String[]
	 * 
	 * @param arg
	 * @param split
	 * @return
	 */
	public static String[] getFliter(String arg, String split) {
		if (arg != null) {
			split = split == null || split.trim().length() == 0 ? "." : split;
			StringTokenizer fen = new StringTokenizer(arg, split);
			String[] temp = new String[fen.countTokens()];
			int i = 0;
			while (fen.hasMoreTokens()) {
				temp[i] = fen.nextToken();
				i++;
			}
			return temp;
		} else {
			return null;
		}
	}
	
	/**
	 * 格式化转换 modulenode 里的 url
	 * 
	 * @param url
	 * @return
	 */
	public static String FormatUrl(String url) {
		if (url != null && url.trim().length() > 0) {
			return "/" + SysConfig.WEB_APPNAME + url.substring(2, url.length());
		} else {
			return "";
		}
	}
	
	/**
	 * 将url编码
	 * @param url
	 * @param chartset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getUrlEncode(String url,String chartset) throws UnsupportedEncodingException{
		if(url!=null && url.length()>0){
			chartset=chartset==null||chartset.length()==0?"utf-8":chartset.trim();
			return URLEncoder.encode(url,chartset);
		}else{
			return "";
		}
		
	}
	/**
	 * 将url解码
	 * @param url
	 * @param chartset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getUrlDecode(String url,String chartset) throws UnsupportedEncodingException{
		if(url!=null && url.length()>0){
			chartset=chartset==null||chartset.length()==0?"utf-8":chartset.trim();
			return URLDecoder.decode(url,chartset);
		}else{
			return "";
		}
		
	}
	
	/**
	 * 取N天后的日期
	 * 
	 * @param date
	 * @return yyyy-mm-dd 格式的日期
	 * @throws ParseException
	 */
	public static String getNDay(String date,int days) throws ParseException {
		Date sdate = null;
		try {
			String day = date.substring(8, 10);
			int Iday = Integer.parseInt(day);
			Iday = Iday + days;
			date = date.substring(0, 8) + Iday;
			sdate=strToDate(date, "");
			
		} catch (Exception e) {
			DebugUtil.print(e, "?????????í?ó??");
		}

		return dateToStr(sdate,"");
		

	}

	public static String strToSQLLikeCondition(String colmun) {
		String rt = "%";
		if(null != colmun && !"".equals(colmun)){
			rt = "%"+colmun.trim()+"%";
		}
		return rt;
	}
	

	/*
	 * 数组列表转为bean列表
	 */
	public static List<LabelValueBean> toLableValuebean(List<Object[]> listofarray){
	    List<LabelValueBean> colorList = new ArrayList<LabelValueBean>();
	    for(Object[] o:listofarray){
	    	colorList.add(new LabelValueBean((String)o[1],(String)o[0]));
	    }
	    return colorList;
	}

	public static String getName(HttpServletRequest request,String listattrname,String value) {
		List list=(List)request.getAttribute(listattrname);
		for(Object o:list){
			LabelValueBean lvb=(LabelValueBean)o;
			if(lvb.getValue().equals(value)){
				return lvb.getLabel().toString();
			}
		}
		return "";
	}
	
	public static String formatNum(int point, double arg) {
		String rs = "";
		DecimalFormat nf = new DecimalFormat("##.##");
		nf.setMaximumFractionDigits(point);
		nf.setMinimumFractionDigits(point);
		rs = nf.format(arg);
		return rs;
	}
	
	/**
	 * 流水号生成方法 = 默认流水号长度为12位
	 * @param year1  两位年份(15)
	 * @param tableName 表名称
	 * @param count  一次生成流水号的数量
	 * @return
	 */
	public synchronized static String[] getBillno(String year1,String tableName, int count) {

		if (count < 1) {
			count = 1;
		}
		
		if (year1 == null || year1.length() != 2) {
            DebugUtil.println("年份参数长度错误!");
            return null;
        }

		int argLen = count;
		String jnlNo[] = new String[argLen];
		double tempInt = 10000000000D;
		int nextNo = 1;
		ArrayList list = null;
		Session session = null;
		Getnum getnum = new Getnum();
		GetnumKey getnumkey = new GetnumKey();
		Transaction tx = null;
		try {
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM Getnum as a WHERE a.id.year1=:year1 AND a.id.deptflag = :deptflag");
			query.setString("year1", year1);
			query.setString("deptflag", tableName);

			list = (ArrayList) query.list();
			if (list == null || list.isEmpty() || list.size() == 0) {
				// 还没有记录，序列号从1开始
				for (int i = 0; i < argLen; i++) {
					jnlNo[i] = year1+CommonUtil.formatDigitsNum(0, tempInt + nextNo).substring(1).replaceAll(",", "");
					nextNo++;
				}
				getnumkey.setYear1(year1);
                getnumkey.setDeptflag(tableName);
                getnum.setId(getnumkey);
			} else {
				getnum = (Getnum) list.get(0);
				nextNo = getnum.getNextno().intValue();
				for (int i = 0; i < argLen; i++) {
					jnlNo[i] = year1+CommonUtil.formatDigitsNum(0, tempInt + nextNo).substring(1).replaceAll(",", "");
					nextNo++;
				}
			}
			getnum.setNextno(new Integer(nextNo));
			session.save(getnum);
			tx.commit();
			
		} catch (Exception hex) {
			try {
				tx.rollback();
			} catch (HibernateException e) {
				DebugUtil.print(e, "Hibernate Rolenode Save error!");
			}
			DebugUtil.print(hex, "HibernateUtil：Hibernate Session 打开出错！");
		} finally {
			try {
				if(session!=null){
					session.close();
				}
			} catch (HibernateException hex) {
				DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
			}
		}
		return jnlNo;
	}
	
	/**
	 * 流水号生成方法
	 * @param year1  两位年份(15)
	 * @param tableName 表名称
	 * @param count  一次生成流水号的数量
	 * @param len   流水号的长度(小于等于10位)
	 * @return
	 */
	public synchronized static String[] getBillno(String year1,String tableName, int count,int len) {

		if (count < 1) {
			count = 1;
		}
		
		if(len<1){
			len = 1;
		}

		if(len >10){
			len = 10;
		}
		
		if (year1 == null || year1.length() != 2) {
            DebugUtil.println("年份参数长度错误!");
            return null;
        }
		
		int argLen = count;
		String jnlNo[] = new String[argLen];
		double tempInt = 10000000000D;
		int nextNo = 1;
		ArrayList list = null;
		Session session = null;
		Getnum getnum = new Getnum();
		GetnumKey getnumkey = new GetnumKey();
		Transaction tx = null;
		try {
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM Getnum as a WHERE a.id.year1=:year1 AND a.id.deptflag = :deptflag");
			query.setString("year1", year1);
			query.setString("deptflag", tableName);

			list = (ArrayList) query.list();
			if (list == null || list.isEmpty() || list.size() == 0) {
				// 还没有记录，序列号从1开始
				for (int i = 0; i < argLen; i++) {
					jnlNo[i] = year1+CommonUtil.formatDigitsNum(0, tempInt + nextNo).substring(1).replaceAll(",", "").substring(10-len);
					nextNo++;
				}
				getnumkey.setYear1(year1);
                getnumkey.setDeptflag(tableName);
                getnum.setId(getnumkey);
			} else {
				getnum = (Getnum) list.get(0);
				nextNo = getnum.getNextno().intValue();
				for (int i = 0; i < argLen; i++) {
					jnlNo[i] = year1+CommonUtil.formatDigitsNum(0, tempInt + nextNo).substring(1).replaceAll(",", "").substring(10-len);
					nextNo++;
				}
			}
			getnum.setNextno(new Integer(nextNo));
			session.save(getnum);
			tx.commit();
		} catch (Exception hex) {
			try {
				tx.rollback();
			} catch (HibernateException e) {
				DebugUtil.print(e, "Hibernate Rolenode Save error!");
			}
			DebugUtil.print(hex, "HibernateUtil：Hibernate Session 打开出错！");
		} finally {
			try {
				session.close();
			} catch (HibernateException hex) {
				DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
			}
		}
		return jnlNo;
	}
	/**
	 * 将科学记数转为double型(如：1.00585E01) point 参数 是控制小数点后的位数 arg 参数 是要转化的参数
	 * 
	 * @param point
	 * @param arg
	 * @return
	 */
	public static String formatDigitsNum(int point, double arg) {
		String rs = "";
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(point);
		rs = nf.format(arg);
		return rs;
	}
	
	/**
	 * 生成编号 
	 * 序号按最大记录+1
	 * 例如生成合同号：B005A B006A
	 * @param tableName 表名
	 * @param colName 编号列名
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param len 序号长度
	 * @param orderstr 排序降序
	 * @return
	 */	 
	public static String genNo(String tableName, String colName, String prefix, String suffix, int len,String orderstr){
				
		String resultNo = null;
		
		if(tableName != null && !tableName.equals("") 
				&& colName !=null && !colName.equals("")){
			
			Session session = null;
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;
						
			char[] middle = new char[len];
			Arrays.fill(middle, '_');
			
			//有偿和续签 的序号需要顺延下去。
			String prefix2=prefix.replaceAll("XB", "");
			
			String sql = "select " + colName + " from " + tableName + 
					" where replace("+ colName +",'XB','') like '"+ prefix2 + String.valueOf(middle) + suffix +"'" + 
					" order by replace("+orderstr+",'XB','') desc";

			try {
				
				session = HibernateUtil.getSession();
				con = session.connection();
				stmt = con.createStatement();
				rs = stmt.executeQuery(sql);
				
				//System.out.println(">>>"+sql);
				
				String currentNum = "";
				String currentNum2 = "";
				if(rs.next()){					
					currentNum = rs.getString(colName);
					currentNum2 = currentNum.replaceAll("XB", "");
					currentNum = currentNum2.replace(prefix2, "").replace(suffix, "");
					currentNum = String.format("%0"+len+"d",Integer.parseInt(currentNum) + 1);					
				}else{
					currentNum = String.format("%0"+len+"d",1);
				}
				
				resultNo = prefix + currentNum + suffix;
	
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (stmt != null) {
						stmt.close();
					}
					if (con != null) {
						session.close();
					}
					if (session != null) {
						session.close();
					}
				} catch (Exception e) {
					DebugUtil.print(e, "HibernateUtil：Hibernate Session 关闭出错！");
				}
			}
		}
		
		return resultNo;
	}

    public static String getNowTime(String formatStr){
    	DateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(new Date());
    }
    
    public static String getNowTime(){
    	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
    
    public static String getDateFormatStr(String strDate, String dateFormat){
    	try {
	    	if(strDate == null || strDate.trim().equals("")){
	    		strDate = "";
	    	} else {
	    		if (dateFormat == null || dateFormat.trim().equals("")) {	           
		        	dateFormat = "yyyy-MM-dd";
		        }
	    		DateFormat df = new SimpleDateFormat(dateFormat);	               
		        strDate = df.format(df.parse(strDate));	
	    	} 	        
    	} catch (Exception e) {
    		strDate = "";		
		}       
        return strDate;
    }
    /**
     * 生成保养计划
     * @param mcdRowid 维保明细行号
     * @param String maintPersonnel 维保工
     * @param ViewLoginUserInfo userInfo
     * @param ActionErrors errors
     * @return
     */	
    public static void  toMaintenanceWorkPlan(String mcdRowid,String maintPersonnel,
    		ViewLoginUserInfo userInfo,ActionErrors errors){
    	Session hs=null;
    	Transaction tx=null;
    	try{
	    	hs = HibernateUtil.getSession();
	    	tx =hs.beginTransaction();
			MaintContractDetail mcd = (MaintContractDetail) hs.get(MaintContractDetail.class, Integer.valueOf(mcdRowid));
	
			String sql="from MaintenanceWorkPlanMaster where rowid= "+mcd.getRowid();
			List mwpmList=hs.createQuery(sql).list();
			if(mcd.getMaintPersonnel()!=null&&!mcd.getMaintPersonnel().equals(""))
			{
				maintPersonnel=mcd.getMaintPersonnel();
			}
		    MaintenanceWorkPlanMaster mwpm=null;
		    if(mwpmList!=null && mwpmList.size()>0)
			{
				mwpm= (MaintenanceWorkPlanMaster) mwpmList.get(0);
		    
				Integer halfMonth=null;
				Integer quarter=null;
				Integer halfYear=null;
				Integer yearDegree=null;
	
				//检查层站是否有保养工时
				String hql="select mwh.halfMonth,mwh.quarter,mwh.halfYear,mwh.yearDegree " +
						"from MaintenanceWorkingHours mwh where mwh.id.elevatorType= '"+mcd.getElevatorType()+"'" +
						" and mwh.id.floor = '"+mcd.getFloor()+"'";
				List list=hs.createQuery(hql).list();
				if(list!=null && list.size()>0)
				{
					String maintlogic=mwpm.getMaintLogic();//保养逻辑
					//hs.save(mwpm); 
					//hs.flush();
					
					Object[] objects= (Object[]) list.get(0);
					 halfMonth=(Integer) objects[0];
					 quarter=(Integer) objects[1];
					 halfYear=(Integer) objects[2];
					 yearDegree=(Integer) objects[3];
					 
					if(mcd.getDelayEDate()!=null && !mcd.getDelayEDate().equals("")){
						//延长保养计划,固定为半月保养(分钟)
						String eDate=mcd.getDelayEDate();//延保结束日期
						String date="";
						String sql1="select max(mwpd.maintDate) from MaintenanceWorkPlanDetail mwpd" +
								" where mwpd.maintenanceWorkPlanMaster.billno ='"+mwpm.getBillno()+"'";
						List mwpdList=hs.createQuery(sql1).list();
						if(mwpdList!=null&&mwpdList.size()>0)
						{
						   date=(String) mwpdList.get(0);
						}
						
						if(date!=null && !date.trim().equals("")){
							//延保的统一为半月保养
							date=DateUtil.getDate(date, "dd", 14);
							for(int a=1;DateUtil.compareDay(date,eDate)>0;a++)
							{
								MaintenanceWorkPlanDetail mwpd=new MaintenanceWorkPlanDetail();
								mwpd.setMaintType("halfMonth");//半月保养(分钟)
								mwpd.setMaintDateTime(halfMonth.toString());
								mwpd.setMaintenanceWorkPlanMaster(mwpm);
								mwpd.setMaintDate(date);
							    mwpd.setOperId(userInfo.getUserID());
							    mwpd.setOperDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
							    mwpd.setWeek(DateUtil.getWeek(date).substring(2, 3));
							    mwpd.setMaintPersonnel(mcd.getMaintPersonnel());
							    hs.save(mwpd);
							    hs.flush();
							    date=DateUtil.getDate(date, "dd", 14);
								/**
								 if(DateUtil.getWeek(date).equals("星期日")){
									date=DateUtil.getDate(date, "dd", -1);
							 	 }		
								*/		
							}
						}
					
					}else{
						//创建保养计划
						//String date=mcd.getMainSdate();//维保开始日期
						String date=mcd.getShippedDate();//维保计划开始日期
						String eDate=mcd.getMainEdate();//维保结束日期
						
						//删除保养计划，排除已经有保养单号的
						String sql1="delete MaintenanceWorkPlanDetail mwpd " +
								"where mwpd.maintenanceWorkPlanMaster.billno ='"+mwpm.getBillno()+"' " +
								"and isnull(mwpd.singleno,'')=''"; 
						hs.createQuery(sql1).executeUpdate();
						hs.flush();
						
						//根据维保开始日期，删除电梯编号，之前的保养计划，排除已经有保养单号的
						String delete="from MaintenanceWorkPlanDetail as mwpd " +
								"where mwpd.maintenanceWorkPlanMaster.elevatorNo='"+mcd.getElevatorNo()+"' " +
										"and mwpd.maintDate >= '"+date+"' and isnull(mwpd.singleno,'')=''"; 
						//System.out.println(delete);
						List deleteList=hs.createQuery(delete).list();
						if(deleteList!=null&&deleteList.size()>0){
							for(int i=0;i<deleteList.size();i++){
								hs.delete((MaintenanceWorkPlanDetail)deleteList.get(i));
							}
						}
						hs.flush();
						
						//保养逻辑2,3,4,5 增加n个半月保养
						if(maintlogic!=null && !maintlogic.trim().equals("")){
							int loc=Integer.parseInt(maintlogic);//保养逻辑选择
							for(int a3=0;a3<loc-1;a3++){
								MaintenanceWorkPlanDetail mwpd=new MaintenanceWorkPlanDetail();
								mwpd.setMaintType("halfMonth");//半月保养(分钟)
								mwpd.setMaintDateTime(halfMonth.toString());
								mwpd.setMaintenanceWorkPlanMaster(mwpm);
							    mwpd.setMaintDate(date);
							    mwpd.setOperId(userInfo.getUserID());
							    mwpd.setOperDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
							    mwpd.setWeek(DateUtil.getWeek(date).substring(2, 3));
							    mwpd.setMaintPersonnel(maintPersonnel);
							    hs.save(mwpd); 
							    hs.flush();
				                date=DateUtil.getDate(date, "dd", 14);
							}
						}
						
						//保养逻辑1
						int bylen=53;//两年的保养计划循环
						for(int a=1;DateUtil.compareDay(date,eDate)>0;a++)
						{
							MaintenanceWorkPlanDetail mwpd=new MaintenanceWorkPlanDetail();
							if(a%bylen==1 || a%bylen==28){
								//1,28 年度
								mwpd.setMaintType("yearDegree");//年度保养(分钟)
								mwpd.setMaintDateTime(yearDegree.toString());
							}else if(a%bylen==14 || a%bylen==41){
								//14,41 半年
								mwpd.setMaintType("halfYear");//半年保养(分钟)
								mwpd.setMaintDateTime(halfYear.toString());
							}else if(a%bylen==8 || a%bylen==21 || a%bylen==34 || a%bylen==47){
								//8,21,34,47 季度
								mwpd.setMaintType("quarter");//季度保养(分钟)
								mwpd.setMaintDateTime(quarter.toString());
							}else{
								//半月
								mwpd.setMaintType("halfMonth");//半月保养(分钟)
								mwpd.setMaintDateTime(halfMonth.toString());
							}
						    mwpd.setMaintenanceWorkPlanMaster(mwpm);
						    mwpd.setMaintDate(date);
						    mwpd.setOperId(userInfo.getUserID());
						    mwpd.setOperDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
						    mwpd.setWeek(DateUtil.getWeek(date).substring(2, 3));
						    mwpd.setMaintPersonnel(maintPersonnel);
						    hs.save(mwpd); 
						    hs.flush();
			                date=DateUtil.getDate(date, "dd", 14);
							/**
							 if(DateUtil.getWeek(date).equals("星期日"))
							 {
								date=DateUtil.getDate(date, "dd", -1);
							 }
							*/
							
						}
						
						//默认新建维保作业计划，增加3个月的维保期
						String eDate2=DateUtil.getDate(eDate, "MM", 3);//当前日期月份加1 。
						for(int a2=1;DateUtil.compareDay(date,eDate2)>0;a2++)
						{
							MaintenanceWorkPlanDetail mwpd=new MaintenanceWorkPlanDetail();
							mwpd.setMaintType("halfMonth");//半月保养(分钟)
							mwpd.setMaintDateTime(halfMonth.toString());
							mwpd.setMaintenanceWorkPlanMaster(mwpm);
						    mwpd.setMaintDate(date);
						    mwpd.setOperId(userInfo.getUserID());
						    mwpd.setOperDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
						    mwpd.setWeek(DateUtil.getWeek(date).substring(2, 3));
						    mwpd.setMaintPersonnel(maintPersonnel);
						    hs.save(mwpd); 
						    hs.flush();
			                date=DateUtil.getDate(date, "dd", 14);
							/**
							 if(DateUtil.getWeek(date).equals("星期日")){
								date=DateUtil.getDate(date, "dd", -1);
						 	 }		
							*/		
						}
						
					}	
				}else{
					if(errors.isEmpty()){
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","<font color='red'>&nbsp;&nbsp;层:"+mcd.getFloor()+", 找不到该电梯层数的保养时工,不能保存!</font>"));
					}
				}
			}
			 tx.commit();
    	}catch(Exception e){
    		e.printStackTrace();
    		if(errors.isEmpty()){
    			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","生成维保作业计划失败！"));
    		}
    		if(tx!=null){tx.rollback();}
     	}finally{
    		if(hs!=null){hs.close();}
    	}

		 
    }
    /**
     * 生成保养计划
     * @param mcdRowid 维保明细行号
     * @param String maintPersonnel 维保工
     * @param ViewLoginUserInfo userInfo
     * @param ActionErrors errors
     * @return
     */	
    public static void  toMaintenanceWorkPlan_old(String mcdRowid,String maintPersonnel,
    		ViewLoginUserInfo userInfo,ActionErrors errors) 
    {
    	Session hs=null;
    	Transaction tx=null;
    	try{
	    	hs = HibernateUtil.getSession();
	    	tx =hs.beginTransaction();
			MaintContractDetail mcd = (MaintContractDetail) hs.get(MaintContractDetail.class, Integer.valueOf(mcdRowid));
	
			String sql="from MaintenanceWorkPlanMaster where rowid= "+mcd.getRowid();
			List mwpmList=hs.createQuery(sql).list();
			if(mcd.getMaintPersonnel()!=null&&!mcd.getMaintPersonnel().equals(""))
			{
				maintPersonnel=mcd.getMaintPersonnel();
			}
		    MaintenanceWorkPlanMaster mwpm=null;
		    if(mwpmList!=null && mwpmList.size()>0)
			{
				mwpm= (MaintenanceWorkPlanMaster) mwpmList.get(0);
		    
				Integer halfMonth=null;
				Integer quarter=null;
				Integer halfYear=null;
				Integer yearDegree=null;
	
				//检查层站是否有保养工时
				String hql="select mwh.halfMonth,mwh.quarter,mwh.halfYear,mwh.yearDegree " +
						"from MaintenanceWorkingHours mwh where mwh.id.elevatorType= '"+mcd.getElevatorType()+"'" +
						" and mwh.id.floor = '"+mcd.getFloor()+"'";
				List list=hs.createQuery(hql).list();
				if(list!=null && list.size()>0)
				{
					String maintlogic=mwpm.getMaintLogic();//保养逻辑
					//hs.save(mwpm); 
					//hs.flush();
					
					Object[] objects= (Object[]) list.get(0);
					 halfMonth=(Integer) objects[0];
					 quarter=(Integer) objects[1];
					 halfYear=(Integer) objects[2];
					 yearDegree=(Integer) objects[3];
					 
					if(mcd.getDelayEDate()!=null && !mcd.getDelayEDate().equals("")){
						//延长保养计划,固定为半月保养(分钟)
						String eDate=mcd.getDelayEDate();//延保结束日期
						String date="";
						String sql1="select max(mwpd.maintDate) from MaintenanceWorkPlanDetail mwpd" +
								" where mwpd.maintenanceWorkPlanMaster.billno ='"+mwpm.getBillno()+"'";
						List mwpdList=hs.createQuery(sql1).list();
						if(mwpdList!=null&&mwpdList.size()>0)
						{
						   date=(String) mwpdList.get(0);
						}
						
						if(date!=null && !date.trim().equals("")){
							//延保的统一为半月保养
							date=DateUtil.getDate(date, "dd", 15);
							for(int a=1;DateUtil.compareDay(date,eDate)>0;a++)
							{
								MaintenanceWorkPlanDetail mwpd=new MaintenanceWorkPlanDetail();
								mwpd.setMaintType("halfMonth");//半月保养(分钟)
								mwpd.setMaintDateTime(halfMonth.toString());
								mwpd.setMaintenanceWorkPlanMaster(mwpm);
								mwpd.setMaintDate(date);
							    mwpd.setOperId(userInfo.getUserID());
							    mwpd.setOperDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
							    mwpd.setWeek(DateUtil.getWeek(date).substring(2, 3));
							    mwpd.setMaintPersonnel(mcd.getMaintPersonnel());
							    hs.save(mwpd);
							    hs.flush();
							    date=DateUtil.getDate(date, "dd", 15);
								/**
								 if(DateUtil.getWeek(date).equals("星期日")){
									date=DateUtil.getDate(date, "dd", -1);
							 	 }		
								*/		
							}
						}
					
					}else{
						//创建保养计划
						//String date=mcd.getMainSdate();//维保开始日期
						String date=mcd.getShippedDate();//维保计划开始日期
						String eDate=mcd.getMainEdate();//维保结束日期
						
						//删除保养计划，排除已经有保养单号的
						String sql1="delete MaintenanceWorkPlanDetail mwpd " +
								"where mwpd.maintenanceWorkPlanMaster.billno ='"+mwpm.getBillno()+"' " +
								"and isnull(mwpd.singleno,'')=''"; 
						hs.createQuery(sql1).executeUpdate();
						hs.flush();
						
						//根据维保开始日期，删除电梯编号，之前的保养计划，排除已经有保养单号的
						String delete="from MaintenanceWorkPlanDetail as mwpd " +
								"where mwpd.maintenanceWorkPlanMaster.elevatorNo='"+mcd.getElevatorNo()+"' " +
										"and mwpd.maintDate >= '"+date+"' and isnull(mwpd.singleno,'')=''"; 
						//System.out.println(delete);
						List deleteList=hs.createQuery(delete).list();
						if(deleteList!=null&&deleteList.size()>0){
							for(int i=0;i<deleteList.size();i++){
								hs.delete((MaintenanceWorkPlanDetail)deleteList.get(i));
							}
						}
						hs.flush();
						
						//保养逻辑2,3,4,5 增加n个半月保养
						if(maintlogic!=null && !maintlogic.trim().equals("")){
							int loc=Integer.parseInt(maintlogic);//保养逻辑选择
							for(int a3=0;a3<loc-1;a3++){
								MaintenanceWorkPlanDetail mwpd=new MaintenanceWorkPlanDetail();
								mwpd.setMaintType("halfMonth");//半月保养(分钟)
								mwpd.setMaintDateTime(halfMonth.toString());
								mwpd.setMaintenanceWorkPlanMaster(mwpm);
							    mwpd.setMaintDate(date);
							    mwpd.setOperId(userInfo.getUserID());
							    mwpd.setOperDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
							    mwpd.setWeek(DateUtil.getWeek(date).substring(2, 3));
							    mwpd.setMaintPersonnel(maintPersonnel);
							    hs.save(mwpd); 
							    hs.flush();
				                date=DateUtil.getDate(date, "dd", 15);
							}
						}
						
						//保养逻辑1 原来的保养逻辑
						for(int a=1;DateUtil.compareDay(date,eDate)>0;a++)
						{
							MaintenanceWorkPlanDetail mwpd=new MaintenanceWorkPlanDetail();
							if(a%24==1){
								//如果当前类型为年度保养，并且下一个15天合同未到期的，就为年度保养否则为半月保养
								String datek=DateUtil.getDate(date, "dd", 15);
								if(DateUtil.compareDay(datek,eDate)>0){
									mwpd.setMaintType("yearDegree");//年度保养(分钟)
									mwpd.setMaintDateTime(yearDegree.toString());
								}else{
									mwpd.setMaintType("halfMonth");//半月保养(分钟)
									mwpd.setMaintDateTime(halfMonth.toString());
								}
							}else if(a%24==7){
								mwpd.setMaintType("quarter");//季度保养(分钟)
								mwpd.setMaintDateTime(quarter.toString());
							}else if(a%24==13){
								mwpd.setMaintType("halfYear");//半年保养(分钟)
								mwpd.setMaintDateTime(halfYear.toString());
							}else if(a%24==19){
								mwpd.setMaintType("quarter");//季度保养(分钟)
								mwpd.setMaintDateTime(quarter.toString());
							}else{
								mwpd.setMaintType("halfMonth");//半月保养(分钟)
								mwpd.setMaintDateTime(halfMonth.toString());
							}
						    mwpd.setMaintenanceWorkPlanMaster(mwpm);
						    mwpd.setMaintDate(date);
						    mwpd.setOperId(userInfo.getUserID());
						    mwpd.setOperDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
						    mwpd.setWeek(DateUtil.getWeek(date).substring(2, 3));
						    mwpd.setMaintPersonnel(maintPersonnel);
						    hs.save(mwpd); 
						    hs.flush();
			                date=DateUtil.getDate(date, "dd", 15);
							/**
							 if(DateUtil.getWeek(date).equals("星期日"))
							 {
								date=DateUtil.getDate(date, "dd", -1);
							 }
							*/
							
						}
						
						//默认新建维保作业计划，增加3个月的维保期
						String eDate2=DateUtil.getDate(eDate, "MM", 3);//当前日期月份加1 。
						for(int a2=1;DateUtil.compareDay(date,eDate2)>0;a2++)
						{
							MaintenanceWorkPlanDetail mwpd=new MaintenanceWorkPlanDetail();
							mwpd.setMaintType("halfMonth");//半月保养(分钟)
							mwpd.setMaintDateTime(halfMonth.toString());
							mwpd.setMaintenanceWorkPlanMaster(mwpm);
						    mwpd.setMaintDate(date);
						    mwpd.setOperId(userInfo.getUserID());
						    mwpd.setOperDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
						    mwpd.setWeek(DateUtil.getWeek(date).substring(2, 3));
						    mwpd.setMaintPersonnel(maintPersonnel);
						    hs.save(mwpd); 
						    hs.flush();
			                date=DateUtil.getDate(date, "dd", 15);
							/**
							 if(DateUtil.getWeek(date).equals("星期日")){
								date=DateUtil.getDate(date, "dd", -1);
						 	 }		
							*/		
						}
						
					}	
				}else{
					if(errors.isEmpty()){
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","<font color='red'>&nbsp;&nbsp;层:"+mcd.getFloor()+", 找不到该电梯层数的保养时工,不能保存!</font>"));
					}
				}
				//if(mcd.getMaintPersonnel()==null||mcd.getMaintPersonnel().equals("")){
				//	if(errors.isEmpty()) {  
				//		mcd.setMaintPersonnel(maintPersonnel);
				//		mcd.setAssignedSignFlag("Y");
				//		mcd.setAssignedSign(userInfo.getUserID());
				//		mcd.setAssignedSignDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
				//		hs.update(mcd);
				//	  }		
				//}
			}else{
				//String year1=CommonUtil.getToday().substring(2,4);
				//String[] billno=CommonUtil.getBillno(year1, "MaintenanceWorkPlanMaster", 1);
				//mwpm=new MaintenanceWorkPlanMaster();
				//mwpm.setRowid(mcd.getRowid());
				//mwpm.setBillno(billno[0]);
				//mwpm.setElevatorNo(mcd.getElevatorNo());
				//mwpm.setMaintLogic("1");
				
				//if(errors.isEmpty()){
				//	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","<font color='red'>没有维保作业计划!</font>"));
				//}
			}
			 tx.commit();
    	}catch(Exception e){
    		e.printStackTrace();
    		if(errors.isEmpty()){
    			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","生成维保作业计划失败！"));
    		}
    		if(tx!=null){tx.rollback();}
     	}finally{
    		if(hs!=null){hs.close();}
    	}

		 
    }
    /**
     * 生成保养计划
     * @param mcdRowid 维保明细行号
     * @param String maintPersonnel 维保工
     * @param ViewLoginUserInfo userInfo
     * @param ActionErrors errors
     * @param mainEdate 新维保合同结束日期
     * @return
     */	
    public static void  toMaintenanceWorkPlan2(String mcdRowid,String maintPersonnel,
    		ViewLoginUserInfo userInfo,ActionErrors errors,String mainEdate) 
    {
    	Session hs=null;
    	Transaction tx=null;
    	try{
	    	hs = HibernateUtil.getSession();
	    	tx =hs.beginTransaction();
			MaintContractDetail mcd = (MaintContractDetail) hs.get(MaintContractDetail.class, Integer.valueOf(mcdRowid));
	
			String sql="from MaintenanceWorkPlanMaster where rowid= "+mcd.getRowid();
			List mwpmList=hs.createQuery(sql).list();
			if(mcd.getMaintPersonnel()!=null&&!mcd.getMaintPersonnel().equals(""))
			{
				maintPersonnel=mcd.getMaintPersonnel();
			}
		    MaintenanceWorkPlanMaster mwpm=null;
		    if(mwpmList!=null && mwpmList.size()>0)
			{
		    	mwpm= (MaintenanceWorkPlanMaster) mwpmList.get(0);
		    	
				Integer halfMonth=null;
				Integer quarter=null;
				Integer halfYear=null;
				Integer yearDegree=null;
	
				//检查层站是否有保养工时
				String hql="select mwh.halfMonth,mwh.quarter,mwh.halfYear,mwh.yearDegree " +
						"from MaintenanceWorkingHours mwh where mwh.id.elevatorType= '"+mcd.getElevatorType()+"'" +
						" and mwh.id.floor = '"+mcd.getFloor()+"'";
				List list=hs.createQuery(hql).list();
				if(list!=null && list.size()>0)
				{
					
					Object[] objects= (Object[]) list.get(0);
					 halfMonth=(Integer) objects[0];
					 quarter=(Integer) objects[1];
					 halfYear=(Integer) objects[2];
					 yearDegree=(Integer) objects[3];

					//删除维保开始日期之后的保养计划  【20180605保留已经保养的计划】
					String delete="from MaintenanceWorkPlanDetail as mwpd " +
							"where mwpd.maintenanceWorkPlanMaster.rowid="+mcdRowid+
									" and mwpd.maintDate>'"+mainEdate+"' and isnull(mwpd.singleno,'')=''"; 
					//System.out.println("1>>>"+delete);
					List deleteList=hs.createQuery(delete).list();
					if(deleteList!=null && deleteList.size()>0){
						for(int i=0;i<deleteList.size();i++){
							hs.delete((MaintenanceWorkPlanDetail)deleteList.get(i));
						}
					}
					hs.flush();
					
					//获取最大的维保计划日期
					boolean ismaintdate=false;
					String date=mainEdate;//维保计划开始日期
					String sql1="select isnull(max(mwpd.maintDate),'') from MaintenanceWorkPlanDetail mwpd" +
							" where mwpd.maintenanceWorkPlanMaster.rowid="+mcdRowid+" and mwpd.maintDate<='"+mainEdate+"'";
					//System.out.println("2>>>"+sql1);
					List mwpdList=hs.createQuery(sql1).list();
					if(mwpdList!=null && mwpdList.size()>0)
					{
						String datek=(String) mwpdList.get(0);
						if(!"".equals(datek)){
						   date=datek;
						   date=DateUtil.getDate(date, "dd", 14);
						   ismaintdate=true;
						}
					}
					
					if(ismaintdate){
						//已经存在保养计划，创建保养计划
						 /**
							从合同修改界面修改合同结束日期（保养计划结束日期为合同结束日期+3个月）
						   	通过修改验收合格日期修改合同结束日期（保养计划结束日期为合同结束日期+3个月）
						 */
						//默认新建维保作业计划，增加3个月的维保期
						String eDate2=DateUtil.getDate(mainEdate, "MM", 3);//当前日期月份加1 。
						//System.out.println(mainEdate+">>>"+date+">>>"+eDate2);
						for(int a2=1;DateUtil.compareDay(date,eDate2)>0;a2++)
						{
							MaintenanceWorkPlanDetail mwpd=new MaintenanceWorkPlanDetail();
							mwpd.setMaintType("halfMonth");//半月保养(分钟)
							mwpd.setMaintDateTime(halfMonth.toString());
							mwpd.setMaintenanceWorkPlanMaster(mwpm);
						    mwpd.setMaintDate(date);
						    mwpd.setOperId(userInfo.getUserID());
						    mwpd.setOperDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
						    mwpd.setWeek(DateUtil.getWeek(date).substring(2, 3));
						    mwpd.setMaintPersonnel(maintPersonnel);
						    hs.save(mwpd); 
						    hs.flush();
			                date=DateUtil.getDate(date, "dd", 14);
							/**
							 if(DateUtil.getWeek(date).equals("星期日")){
								date=DateUtil.getDate(date, "dd", -1);
						 	 }		
							*/		
						}
					}else{
						//if(errors.isEmpty()){
						//	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","电梯编号："+mcd.getElevatorNo()+"，找不到保养计划！"));
						//}
						System.out.println("维保合同修改 >>>电梯编号："+mcd.getElevatorNo()+"，找不到保养计划！");
					}

				}else{
					if(errors.isEmpty()){
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","<font color='red'>&nbsp;&nbsp;层:"+mcd.getFloor()+", 找不到该电梯层数的保养时工,不能保存!</font>"));
					}
				}
			}
			 tx.commit();
    	}catch(Exception e){
    		e.printStackTrace();
    		if(errors.isEmpty()){
    			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","生成维保作业计划失败！"));
    		}
    		if(tx!=null){tx.rollback();}
     	}finally{
    		if(hs!=null){hs.close();}
    	}

		 
    }
    
    public static void deleMaintenanceWorkPlan(String historyBillNo,String billNo,String date,
    		ViewLoginUserInfo userInfo,ActionErrors errors){
    	Session hs=null;
    	Transaction tx=null;
    	try {
			hs=HibernateUtil.getSession();
			tx=hs.beginTransaction();
			
			if(historyBillNo!=null && !"".equals(historyBillNo)){
				System.out.println(">>>>续签合同审核时：退保的电梯编号，删除掉维保工作计划");
				
				//退保的电梯编号，删除掉维保工作计划
				String hql="delete a from MaintenanceWorkPlanDetail a," +
						"(select a.billno,b.RealityEDate " +
						"from MaintenanceWorkPlanMaster a,MaintContractDetail b " +
						"where a.rowid=b.rowid and b.billNo in ('"+historyBillNo+"') " +
						"and isnull(IsSurrender,'N')='Y') b " +
						//"and a.elevatorNo not in(select elevatorNo from MaintContractDetail where billNo='"+billNo+"')) b " +
						"where a.billno=b.billno and a.MaintDate>b.RealityEDate";
				//System.out.println(">>>>>>>>>>>>>>"+hql);
				int delnum=hs.connection().prepareStatement(hql).executeUpdate();
				//System.out.println(">>>>>删除行数："+delnum);
			}else{
				System.out.println(">>>>退保审核时：退保的电梯编号，删除掉维保工作计划");
				
				//退保时，删除退保日期之后的维保计划
				String hqlkk="delete a from MaintenanceWorkPlanDetail a," +
						"(select a.billno from MaintenanceWorkPlanMaster a,MaintContractDetail b " +
						"where a.rowid=b.rowid and b.billNo='"+billNo+"') b " +
						"where a.billno=b.billno and a.MaintDate>'"+date+"'";
				//System.out.println(">>>>>>>>>>>>>>"+hqlkk);
				int delnum=hs.connection().prepareStatement(hqlkk).executeUpdate();
				//System.out.println(">>>>>删除行数："+delnum);
	    	}
			tx.commit();
			
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();
			}
			e.printStackTrace();
			if(errors.isEmpty()){
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","删除保养作业计划失败！"));
			}
		}finally{
			if(hs!=null){
				hs.close();
			}
		}
    }
    //按照合同号删除保养计划
    public static void deleMaintenanceWorkPlan2(String rowid,String mainedate,
    		ViewLoginUserInfo userInfo,ActionErrors errors){
    	Session hs=null;
    	Transaction tx=null;
    	try {
			hs=HibernateUtil.getSession();
			tx=hs.beginTransaction();
			
			if(rowid!=null && !"".equals(rowid)){
				String delsql="delete b from MaintenanceWorkPlanMaster a, MaintenanceWorkPlanDetail b " +
						"where a.billno=b.billno and a.rowid="+rowid+" and b.MaintDate>'"+mainedate+"'";
				int delnum=hs.connection().prepareStatement(delsql).executeUpdate();
			}
			tx.commit();
			
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();
			}
			e.printStackTrace();
			if(errors.isEmpty()){
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","删除保养作业计划失败！"));
			}
		}finally{
			if(hs!=null){
				hs.close();
			}
		}
    }
    
    /**
   	 * 计算两个日期相隔的分钟
   	 * @param String
   	 * @param String
   	 * @return int
   	 * @throws Exception
   	 */
    public static double getMinute(String str1,String str2)
   	{
   	 SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     long minute = 0;
     try {
         java.util.Date date = myFormatter.parse(str1);
         java.util.Date mydate = myFormatter.parse(str2);
         minute = (date.getTime() - mydate.getTime()) / (60 * 1000);
     } catch (Exception e) {
    	 return 0;
     }
     
     return (double) Math.abs(minute);
   		
   	}
    
    /**
	 * 生成保养计划单号
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return String
     * @throws Exception 
	 */
	public static String getNewSingleno(Session hs,String userId) throws Exception
	{
		   String sigleno ="";
		   String hql="from ViewLoginUserInfo where userID='"+userId.trim()+"'";
		   List userList=hs.createQuery(hql).list();
	       if(userList!=null&&userList.size()>0){
	    	   ViewLoginUserInfo user=(ViewLoginUserInfo) userList.get(0);
			   final String STR_FORMAT = "00000";   
		       sigleno =DateUtil.getNowTime("yyyyMM")+user.getComID();
		       //_____ 5个下划线
			   String sql="select MAX(mwpd.singleno) from MaintenanceWorkPlanDetail mwpd where mwpd.singleno like '"+sigleno+"_____' ";
			   List list=hs.createSQLQuery(sql).list();
			   String no=(String) list.get(0);
			   if(no!=null&&!no.equals(""))
			   { 
				   Integer intHao = Integer.parseInt(no.substring(no.length()-5,no.length()));
				   intHao++;  
				   DecimalFormat df = new DecimalFormat(STR_FORMAT);  
				   sigleno+=df.format(intHao);
			   }else
			   {
				   sigleno+="00001";
			   }  
	       }else{
	    	   throw new Exception();
	       }
	    return sigleno;	
	}
    
	/**
	 * 获取上一次保养计划日期
	 * @param String MaintenanceWorkPlanDetail.numno
	 * @param String MaintenanceWorkPlanMaster.billno
	 * @return String 
	 */
	public static String togetAuditCircu(String str1,String str2)
   	{
     Session hs = null;
     String sMaintEndTime="";
    try {
		hs = HibernateUtil.getSession();
		String sql="select mwpd.maintEndTime from MaintenanceWorkPlanDetail mwpd where mwpd.numno=(select numno-1 from MaintenanceWorkPlanDetail where numno='"+str1+"' and billno='"+str2+"' and numno !=(select MIN(numno)from MaintenanceWorkPlanDetail where billno='"+str2+"'))";
		List list=hs.createSQLQuery(sql).list();
	    if(list!=null&&list.size()>0){
	    	sMaintEndTime=(String) list.get(0);
			sMaintEndTime=sMaintEndTime.substring(0, 10);	
	    }
    } catch (DataStoreException e) {
		e.printStackTrace();
	 }finally {
		 if(hs!=null){
			hs.close();
		 }
	 }
		return sMaintEndTime;
   	}
	
	/**
	 * 手机App传中文转码
	 * @param str1 请求参数
	 * @return String 
	 */
	public static String URLDecoder_decode(String str1)
   	{
		String decode="";
      try {
    	  decode=URLDecoder.decode(URLDecoder.decode(str1, "UTF-8"),"UTF-8");
    	  byte[] b = decode.getBytes("gbk");//编码  
    	  decode = new String(b, "gbk");	  
      } catch (Exception e) {
		e.printStackTrace();
	 }
	return decode;
   	}
	
	/**
	 * 手机App传中文转码
	 * @param str1 请求参数
	 *  @param str2 转码格式
	 * @return String 
	 */
	public static String URLDecoder_decode(String str1,String str2)
   	{
		String decode="";
      try {
    	  decode=URLDecoder.decode(URLDecoder.decode(str1, str2),str2);
	 } catch (Exception e) {
		e.printStackTrace();
	 }
	return decode;
   	}
	
	/**
	 * 手机App 点击"更多"功能
	 * @param data 手机请求json
	 *  @param jobiArray listJson
	 * @return 
	 * @return String 
	 * @throws JSONException 
	 */
	public static JSONArray Pagination(JSONObject data,JSONArray jobiArray) throws JSONException
   	{
		int pagelen=50; //(Integer) data.get("pagelen");//每一页的数量
        int pageno= (Integer) data.get("pageno");  //当前总数量
        int start = pageno, end = pageno + pagelen - 1;
	      end = end>jobiArray.length()? jobiArray.length():end;
	      JSONArray newJobiArray = new JSONArray();//代表数组 []
	      int ii=0;
	      for(int i=start; i<=end; i++) {
	    	  if(i<jobiArray.length()){
	    		  newJobiArray.put(ii,jobiArray.get(i));
	    		  ii++;
	    	  }
	     }

  		return newJobiArray;
   	}
	
	/**
	 * 图片到byte数组
	 * @param path
	 * @return
	 */
	  public static byte[] imageToByte(String path){
	    byte[] data = null;
	    FileImageInputStream input = null;
	    ByteArrayOutputStream output = null;
	    try {
	      input = new FileImageInputStream(new File(path));
	      output = new ByteArrayOutputStream();
	      
	      byte[] buf = new byte[1024];
	      int numBytesRead = 0;
	      while ((numBytesRead = input.read(buf)) != -1) {
	    	  output.write(buf, 0, numBytesRead);
	      }
	      data = output.toByteArray();
	      output.close();
	      input.close();
	    }catch (FileNotFoundException ex1) {
	      ex1.printStackTrace();
	    }catch (Exception ex1) {
	      ex1.printStackTrace();
	    }
	    return data;
	  }

	
}




