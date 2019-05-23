/*
 * Created on 2005-7-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created on 2005-7-12
 * <p>Title:	直接连接数据库公用</p>
 * <p>Description:	没有用的连接池，以供调试用</p>
 * <p>Copyright: Copyright (c) 2005
 * <p>Company:友联科技</p>
 * @author wyj
 * @version	1.0
 */
public class DBUtil {
    
    /**
     * 获得MS SQL SERVER 2000数据库的JDBC连接
     * @return Connection
     */
    public static Connection getMSSQLConnection(){
        Connection conn = null;
        try{
            Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver").newInstance();
            conn = DriverManager.getConnection("jdbc:microsoft:sqlserver://192.168.1.9:1433","iscm","iscmgzunicorn");
        }catch(Exception e){
            DebugUtil.print("得到MS SQL SERVER 2000 的调试连接出错!");
        }
        return conn;
    }
    
    /**
     * 获得DB2 8U数据库的JDBC连接
     * @return Connection
     */
    public static Connection getDB2Connection(){
        Connection conn = null;
        try{
            Class.forName("COM.ibm.db2.jdbc.net.DB2Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:db2://localhost:6789/数据库名","用户名","密码");
        }catch(Exception e){
            DebugUtil.print("得到DB2的调试连接出错!");
        }
        return conn;
    }
    
    /**
     * 获得Oracle9i数据库的JDBC连接
     * @return Connection
     */
    public static Connection getOracleConnection(){
        Connection conn = null;
        try{
          Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
          return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:alex", "archive",
              "11");
        }catch(Exception e){
            DebugUtil.print("得到Oracle的调试连接出错!");
        }
        return conn;
      } 
    /**
     * 格式化sql的字符
     * @param sqlStr
     * @return
     */
    public static String sqlStrFormat(String sqlStr){
    	String returnStr = sqlStr;
    	if(sqlStr != null){
        	if(sqlStr.equalsIgnoreCase("null")) returnStr="";
        	if(sqlStr.equalsIgnoreCase("'")) returnStr="\"";
    	}else{
    		returnStr = "";
    	}
    	
    	return returnStr;
    }
}
