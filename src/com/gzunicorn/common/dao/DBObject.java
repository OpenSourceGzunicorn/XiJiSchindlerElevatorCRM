
package com.gzunicorn.common.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;


/**
 * implements　DBInterface　类是为了在以后接口扩展之后，其它子类用到这个接口的其中一些方法时，不用将接口的所有方法都实现
 * 避免直接继承接口而修改现有程序
 * @author rr
 *
 */
public abstract class DBObject implements DBInterface{

//	---------初始化　开始
	/**
	 * 初始化连接
	 * @param con
	 * @throws SQLException
	 */
	public abstract void setCon(Connection con) throws SQLException;
	
	/**
	 * 判断是否已连接
	 * @return
	 * @throws SQLException
	 */
	public abstract boolean isCloseCon()throws SQLException;
		
	/**
	 * 取连接
	 * @return
	 */
	public abstract Connection getConnection();
	
	//---------初始化　结束
	//---------关闭　开始
	/**
	 *关闭连接 
	 * @throws SQLException 
	 */
	public abstract void closeCon()throws SQLException;
	//---------关闭　结束
	
	//---------操作　开始
	/**
	 * 执行更新，返回一个影响数据库行数的int
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int updateToInt(String sqls)throws SQLException{
		return -1;
	}
	/**
	 * 执行查询，返回一个list的结果集
	 * 多用于不用分页
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List queryToList(String sql)throws SQLException{
		return null;
	}
	/**
	 * 执行查询，返回一个指定结果集的子集
	 * 多适用于分页的
	 * @param sql
	 * @param start
	 * @param end
	 * @return
	 * @throws SQLException
	 */
	public List queryToList(String sql,int start,int end) throws SQLException{
		return null;
	}
	/**
	 * 多用于执行存储过程，但返回时没有列名的
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet queryToResultSet(String sql)throws SQLException{
		return null;
	}
	/**
	 * 执行查询，返回一个bean_list
	 * 利用反射
	 * @param sql
	 * @param className  bean 的类名
	 * @param map　－－bean 的属性与返回结果集的列名的对照表
	 * @return
	 */
	public List queryToBean(String sql,String className,HashMap map)throws Exception{
		return null;
	}

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
	public List queryToBean(String sql,String className,HashMap map,int start,int end)throws Exception{
		return null;
	}
	
	
	/**
	 * 返回当前结果集的行数
	 * @return
	 */
	public int getCount(){
		return 0;
	}
	
}
