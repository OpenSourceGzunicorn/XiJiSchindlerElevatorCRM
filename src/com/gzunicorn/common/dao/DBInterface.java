 
package com.gzunicorn.common.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
 
/**
 * 数据库访问统一接口
 * @author FeGe
 *
 */
public interface DBInterface{
	 
	/**
	 * 初始化连接
	 * @param con
	 * @throws SQLException
	 */
	public void setCon(Connection con) throws SQLException;
	
	/**
	 * 判断是否已连接
	 * @return
	 * @throws SQLException
	 */
	public boolean isConOrClose()throws SQLException;
		
	/**
	 * 取连接
	 * @return
	 */
	public Connection getConnection();
	//---------初始化　结束
	//---------关闭　开始
	/**
	 *关闭连接 
	 */
	public void closeCon()throws SQLException;
	//---------关闭　结束
	
	//---------操作　开始
	/**
	 * 执行更新，返回一个影响数据库行数的int
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int updateToInt(String sql)throws SQLException;
	/**
	 * 执行查询，返回一个list的结果集
	 * 多用于不用分页
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List queryToList(String sql)throws SQLException;
	/**
	 * 执行查询，返回一个指定结果集的子集
	 * 多适用于分页的
	 * @param sql
	 * @param start
	 * @param end
	 * @return
	 * @throws SQLException
	 */
	public List queryToList(String sql,int start,int end) throws SQLException;
	/**
	 * 多用于执行存储过程，但返回时没有列名的
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet queryToResultSet(String sql)throws SQLException;
	/**
	 * 执行查询，返回一个bean_list
	 * 利用反射
	 * @param sql
	 * @param className  bean 的类名
	 * @param map　－－bean 的属性与返回结果集的列名的对照表
	 * @return
	 */
	public List queryToBean(String sql,String className,HashMap map)throws Exception;
	/**
	 * 执行查询，返回一个指定子结果集的bean_list
	 * 利用反射
	 * @param sql
	 * @param className　bean 的类名
	 * @param map　－－bean 的属性与返回结果集的列名的对照表
	 * @param start
	 * @param end
	 * @return
	 */
	public List queryToBean(String sql,String className,HashMap map,int start,int end)throws Exception;
	
	
	/**
	 * 返回当前结果集的行数
	 * @return
	 */
	public int getCount();
	
	
	//---------操作　结束
	
}
