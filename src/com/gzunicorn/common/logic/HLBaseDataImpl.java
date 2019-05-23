/*
 * Created on 2005-7-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.logic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * @author Administrator 
 * 热线公共方法
 */
public class HLBaseDataImpl extends BaseDataImpl {

	Log log = LogFactory.getLog(HLBaseDataImpl.class);

	/**
	 * 对指定日期增加时间
	 * @param srcDate
	 * @return
	 */
	public Date addMin(Date srcDate, int mins){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(srcDate);
		calendar.add(Calendar.MINUTE, mins);
		return calendar.getTime();
	}
	
	/**
	 * 把日期转换为字符串输出
	 */
	public String getDateTimeStr(Date date) {
		if(date==null){
			return "";
		}
		SimpleDateFormat simpleFormatter;
		simpleFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String dateTimeStr = simpleFormatter.format(date);
		return dateTimeStr;
	}
	
	/**
	 * 取当前日期时间并以字符方式输出
	 * @return
	 */
	public String getNowDateTimeStr(){
		Date nowDate = new Date();
		return getDateTimeStr(nowDate);
	}
	
}
