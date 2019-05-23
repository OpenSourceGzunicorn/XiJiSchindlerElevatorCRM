package com.gzunicorn.common.util;

import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gzunicorn.common.logic.*;
import com.gzunicorn.hibernate.*;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

/**
 * Created on 2010-11-24
 * Title: CRM系统
 * Description: 基础数据操作日志记录
 * Copyright: Copyright (c) 2010
 * Company:友联科技
 * @author Dicky
 * @version 1.0
 */
public class DataBaseLogUtil {
	Log log = LogFactory.getLog(DataBaseLogUtil.class);
	/**
	 * 基础数据增加
	 * @param con 连接CONNECTION
	 * @param table 操作表
	 * @param key 数组，操作表KEY值
	 * @param tablename 操作表名称
	 */
	public void BaseAddData(HttpServletRequest request,String table,String[] key,String tablename){
		Session hs = null;
		Transaction tx = null;
		Connection con =null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		String sql = "";
		String msql = "";
		try {
			hs = HibernateUtil.getSession();
			tx=hs.beginTransaction();
			con = hs.connection();
			stmt = con.createStatement();
			tablename=table+"("+tablename+")";
			String keystr=KeyToString(key);
			String ip=request.getRemoteAddr();
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
			Timestamp now = new Timestamp(new Date().getTime());
			
			// 查询基础数据表
			sql = "SELECT * FROM " + table + " (nolock) where "+keystr;
			rs = stmt.executeQuery(sql);
			msql="insert into DATABASELOG " +
				"(tablename,action,ipaddress,userid,date1,log1,log2) " +
				"values(?,?,?,?,?,?,?)";
			
			// 取表结构
			rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				String values = "";
				for (int j = 1; j <= columnCount; j++) {
					String columnType = rsmd.getColumnTypeName(j).trim();
					values += rsmd.getColumnName(j) + "=";
					if (columnType.toUpperCase().equalsIgnoreCase("CHAR")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"VARCHAR")) {
						if (j == columnCount) {
							values += "'"+ DBUtil.sqlStrFormat(rs.getString(j))+ "'";
						} else {
							values += "'"+ DBUtil.sqlStrFormat(rs.getString(j))+ "',";
						}
					}
					if (columnType.toUpperCase()
							.equalsIgnoreCase("INTEGER")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"SMALLINT")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"INT")) {
						if (j == columnCount) {
							values += "'"+ rs.getInt(j)+ "'";
						} else {
							values += "'"+ rs.getInt(j)+ "',";
						}
					}
					if (columnType.toUpperCase().equalsIgnoreCase("BIGINT")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"LONG")) {
						if (j == columnCount) {
							values+="'"+rs.getLong(j)+"'";
						} else {
							values+="'"+rs.getLong(j)+"',";
						}
					}
					if (columnType.toUpperCase().equalsIgnoreCase("DOUBLE")) {
						if (j == columnCount) {
							values+="'"+rs.getDouble(j)+"'";
						} else {
							values+="'"+rs.getDouble(j)+"',";
						}
					}
					if (columnType.toUpperCase().equalsIgnoreCase("FLOAT")) {
						if (j == columnCount) {
							values+="'"+rs.getFloat(j)+"'";
						} else {
							values+="'"+rs.getFloat(j)+"',";
						}
					}
				}
				pstmt=con.prepareStatement(msql);
				pstmt.setString(1, tablename);
				pstmt.setString(2, "ADD");
				pstmt.setString(3, ip);
				pstmt.setString(4, userInfo.getUserID());
				pstmt.setTimestamp(5, now);
				pstmt.setString(6, "");
				pstmt.setString(7, values);
				pstmt.executeUpdate();
			}
			tx.commit();
		} catch (Exception e) {
			log.error(e.getMessage());
			DebugUtil.print(e);
			try {
				if (tx !=null){
					tx.rollback();
				}
			} catch (HibernateException e1) {
				log.error(e1.getMessage());
				DebugUtil.println(e1.getMessage());
			}
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
				if (stmt != null){
					stmt = null;
				}
				if (hs!=null){
					hs.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				DebugUtil.print(e);
			}
		}
	}

	/**
	 * 基础数据修改（前）
	 * @param con 连接CONNECTION
	 * @param table 操作表
	 * @param key 数组，操作表KEY值
	 * @param tablename 操作表名称
	 * @param now 操作时间（Timestamp类型）
	 */
	public void BaseBeforUpdateData(HttpServletRequest request,String table,String[] key,String tablename,Timestamp now){
		Session hs = null;
		Transaction tx = null;
		Connection con =null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		String sql = "";
		String msql = "";
		try {
			hs = HibernateUtil.getSession();
			tx=hs.beginTransaction();
			con = hs.connection();
			stmt = con.createStatement();
			tablename=table+"("+tablename+")";
			String keystr=KeyToString(key);
			String ip=request.getRemoteAddr();
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
			
			// 查询基础数据表
			sql = "SELECT * FROM " + table + " (nolock) where "+keystr;
			rs = stmt.executeQuery(sql);
			msql="insert into DATABASELOG " +
				"(tablename,action,ipaddress,userid,date1,log1,log2) " +
				"values(?,?,?,?,?,?,?)";
			
			// 取表结构
			rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				String values = "";
				for (int j = 1; j <= columnCount; j++) {
					String columnType = rsmd.getColumnTypeName(j).trim();
					values += rsmd.getColumnName(j) + "=";
					if (columnType.toUpperCase().equalsIgnoreCase("CHAR")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"VARCHAR")) {
						if (j == columnCount) {
							values += "'"+ DBUtil.sqlStrFormat(rs.getString(j))+ "'";
						} else {
							values += "'"+ DBUtil.sqlStrFormat(rs.getString(j))+ "',";
						}
					}
					if (columnType.toUpperCase()
							.equalsIgnoreCase("INTEGER")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"SMALLINT")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"INT")) {
						if (j == columnCount) {
							values += "'"+ rs.getInt(j)+ "'";
						} else {
							values += "'"+ rs.getInt(j)+ "',";
						}
					}
					if (columnType.toUpperCase().equalsIgnoreCase("BIGINT")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"LONG")) {
						if (j == columnCount) {
							values+="'"+rs.getLong(j)+"'";
						} else {
							values+="'"+rs.getLong(j)+"',";
						}
					}
					if (columnType.toUpperCase().equalsIgnoreCase("DOUBLE")) {
						if (j == columnCount) {
							values+="'"+rs.getDouble(j)+"'";
						} else {
							values+="'"+rs.getDouble(j)+"',";
						}
					}
					if (columnType.toUpperCase().equalsIgnoreCase("FLOAT")) {
						if (j == columnCount) {
							values+="'"+rs.getFloat(j)+"'";
						} else {
							values+="'"+rs.getFloat(j)+"',";
						}
					}
				}
				pstmt=con.prepareStatement(msql);
				pstmt.setString(1, tablename);
				pstmt.setString(2, "UPDATE BEFORE");
				pstmt.setString(3, ip);
				pstmt.setString(4, userInfo.getUserID());
				pstmt.setTimestamp(5, now);
				pstmt.setString(6, values);
				pstmt.setString(7, "");
				pstmt.executeUpdate();
			}
			tx.commit();
		} catch (Exception e) {
			log.error(e.getMessage());
			DebugUtil.print(e);
			try {
				if (tx !=null){
					tx.rollback();
				}
			} catch (HibernateException e1) {
				log.error(e1.getMessage());
				DebugUtil.println(e1.getMessage());
			}
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
				if (stmt != null){
					stmt = null;
				}
				if (hs!=null){
					hs.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				DebugUtil.print(e);
			}
		}
	}

	/**
	 * 基础数据修改（反）
	 * @param con 连接CONNECTION
	 * @param table 操作表
	 * @param key 数组，操作表KEY值
	 * @param tablename 操作表名称
	 * @param now 操作时间（Timestamp类型）
	 */
	public void BaseAfterUpdateData(HttpServletRequest request,String table,String[] key,String tablename,Timestamp now){
		Session hs = null;
		Transaction tx = null;
		Connection con =null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		String sql = "";
		String msql = "";
		try {
			hs = HibernateUtil.getSession();
			tx=hs.beginTransaction();
			con = hs.connection();
			stmt = con.createStatement();
			tablename=table+"("+tablename+")";
			String keystr=KeyToString(key);
			String ip=request.getRemoteAddr();
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
			
			// 查询基础数据表
			sql = "SELECT * FROM " + table + " (nolock) where "+keystr;
			rs = stmt.executeQuery(sql);
			msql="insert into DATABASELOG " +
				"(tablename,action,ipaddress,userid,date1,log1,log2) " +
				"values(?,?,?,?,?,?,?)";
			
			// 取表结构
			rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				String values = "";
				for (int j = 1; j <= columnCount; j++) {
					String columnType = rsmd.getColumnTypeName(j).trim();
					values += rsmd.getColumnName(j) + "=";
					if (columnType.toUpperCase().equalsIgnoreCase("CHAR")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"VARCHAR")) {
						if (j == columnCount) {
							values += "'"+ DBUtil.sqlStrFormat(rs.getString(j))+ "'";
						} else {
							values += "'"+ DBUtil.sqlStrFormat(rs.getString(j))+ "',";
						}
					}
					if (columnType.toUpperCase()
							.equalsIgnoreCase("INTEGER")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"SMALLINT")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"INT")) {
						if (j == columnCount) {
							values += "'"+ rs.getInt(j)+ "'";
						} else {
							values += "'"+ rs.getInt(j)+ "',";
						}
					}
					if (columnType.toUpperCase().equalsIgnoreCase("BIGINT")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"LONG")) {
						if (j == columnCount) {
							values+="'"+rs.getLong(j)+"'";
						} else {
							values+="'"+rs.getLong(j)+"',";
						}
					}
					if (columnType.toUpperCase().equalsIgnoreCase("DOUBLE")) {
						if (j == columnCount) {
							values+="'"+rs.getDouble(j)+"'";
						} else {
							values+="'"+rs.getDouble(j)+"',";
						}
					}
					if (columnType.toUpperCase().equalsIgnoreCase("FLOAT")) {
						if (j == columnCount) {
							values+="'"+rs.getFloat(j)+"'";
						} else {
							values+="'"+rs.getFloat(j)+"',";
						}
					}
				}
				pstmt=con.prepareStatement(msql);
				pstmt.setString(1, tablename);
				pstmt.setString(2, "UPDATE AFTER");
				pstmt.setString(3, ip);
				pstmt.setString(4, userInfo.getUserID());
				pstmt.setTimestamp(5, now);
				pstmt.setString(6, "");
				pstmt.setString(7, values);
				pstmt.executeUpdate();
			}
			tx.commit();
		} catch (Exception e) {
			log.error(e.getMessage());
			DebugUtil.print(e);
			try {
				if (tx !=null){
					tx.rollback();
				}
			} catch (HibernateException e1) {
				log.error(e1.getMessage());
				DebugUtil.println(e1.getMessage());
			}
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
				if (stmt != null){
					stmt = null;
				}
				if (hs!=null){
					hs.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				DebugUtil.print(e);
			}
		}
	}
	
	/**
	 * 基础数据删除
	 * @param con 连接CONNECTION
	 * @param table 操作表
	 * @param key 数组，操作表KEY值
	 * @param tablename 操作表名称
	 */
	public void BaseDeleteData(HttpServletRequest request,String table,String[] key,String tablename){
		Session hs = null;
		Transaction tx = null;
		Connection con =null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		String sql = "";
		String msql = "";
		try {
			hs = HibernateUtil.getSession();
			tx=hs.beginTransaction();
			con = hs.connection();
			stmt = con.createStatement();
			tablename=table+"("+tablename+")";
			String keystr=KeyToString(key);
			String ip=request.getRemoteAddr();
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
			Timestamp now = new Timestamp(new Date().getTime());
			
			// 查询基础数据表
			sql = "SELECT * FROM " + table + " (nolock) where "+keystr;
			rs = stmt.executeQuery(sql);
			msql="insert into DATABASELOG " +
				"(tablename,action,ipaddress,userid,date1,log1,log2) " +
				"values(?,?,?,?,?,?,?)";
			
			// 取表结构
			rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				String values = "";
				for (int j = 1; j <= columnCount; j++) {
					String columnType = rsmd.getColumnTypeName(j).trim();
					values += rsmd.getColumnName(j) + "=";
					if (columnType.toUpperCase().equalsIgnoreCase("CHAR")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"VARCHAR")) {
						if (j == columnCount) {
							values += "'"+ DBUtil.sqlStrFormat(rs.getString(j))+ "'";
						} else {
							values += "'"+ DBUtil.sqlStrFormat(rs.getString(j))+ "',";
						}
					}
					if (columnType.toUpperCase()
							.equalsIgnoreCase("INTEGER")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"SMALLINT")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"INT")) {
						if (j == columnCount) {
							values += "'"+ rs.getInt(j)+ "'";
						} else {
							values += "'"+ rs.getInt(j)+ "',";
						}
					}
					if (columnType.toUpperCase().equalsIgnoreCase("BIGINT")
							|| columnType.toUpperCase().equalsIgnoreCase(
									"LONG")) {
						if (j == columnCount) {
							values+="'"+rs.getLong(j)+"'";
						} else {
							values+="'"+rs.getLong(j)+"',";
						}
					}
					if (columnType.toUpperCase().equalsIgnoreCase("DOUBLE")) {
						if (j == columnCount) {
							values+="'"+rs.getDouble(j)+"'";
						} else {
							values+="'"+rs.getDouble(j)+"',";
						}
					}
					if (columnType.toUpperCase().equalsIgnoreCase("FLOAT")) {
						if (j == columnCount) {
							values+="'"+rs.getFloat(j)+"'";
						} else {
							values+="'"+rs.getFloat(j)+"',";
						}
					}
				}
				pstmt=con.prepareStatement(msql);
				pstmt.setString(1, tablename);
				pstmt.setString(2, "DELETE");
				pstmt.setString(3, ip);
				pstmt.setString(4, userInfo.getUserID());
				pstmt.setTimestamp(5, now);
				pstmt.setString(6, values);
				pstmt.setString(7, "");
				pstmt.executeUpdate();
			}
			tx.commit();
		} catch (Exception e) {
			log.error(e.getMessage());
			DebugUtil.print(e);
			try {
				if (tx !=null){
					tx.rollback();
				}
			} catch (HibernateException e1) {
				log.error(e1.getMessage());
				DebugUtil.println(e1.getMessage());
			}
		} finally {
			try {
				if (rs != null){
					rs.close();
				}
				if (stmt != null){
					stmt = null;
				}
				if (hs!=null){
					hs.close();
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				DebugUtil.print(e);
			}
		}
	}

	public String KeyToString(String[] key){
		String str="";
		for (int i=0;i<key.length;i++){
			str+="".equals(str)?key[i]:" and "+key[i];
		}
		return str;
	}
}