/*
 * 创建日期 2006-2-20
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */

package com.gzunicorn.common.dao.mssql;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gzunicorn.common.dao.*;
import com.gzunicorn.common.util.DebugUtil;

/**
 * @method:
 * @method_type:
 * @param:
 * @external_call:
 * @function:
 * @author rr
 * @create_date:
 * 
 */
public class DataOperation extends DBObject {
	private Connection con = null;

	private String columnType = "";

	// 处理的数据类型
	private String string_value = "";

	private int int_value = 0;

	private long long_value = 0;

	private float float_value = 0;

	private double double_value = 0;

	private int count=0;//结果集行数
	
	// 反射用的列名
	private HashMap colName = new HashMap();

	public DataOperation() {
	}

	public void setCon(Connection con) throws SQLException {
		this.con = con;

	}

	public boolean isConOrClose() throws SQLException {
		if (con == null || con.isClosed()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * **********关闭
	 * 结束*********************************************************************
	 */
	/**
	 * 执行更新，返回一个影响数据库行数的int
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int updateToInt(String sql) throws SQLException {
		Statement st = null;
		int rs = 0;
		try {
			st = con.createStatement();
			rs = st.executeUpdate(sql);
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
				DebugUtil
						.print("DataOperation.updateToInt() " + e.getMessage());
			}
		}
		return rs;
	}

	/**
	 * 执行查询，返回一个list的结果集 多用于不用分页
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List queryToList(String sql) throws SQLException {
		List rs_list = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				HashMap map = new HashMap();
				for (int i = 1; i <= columnCount; i++) {
					columnType = rsmd.getColumnTypeName(i).trim();
					if (columnType.toUpperCase().equals("CHAR")
							|| columnType.toUpperCase().equals("TEXT")
							|| columnType.toUpperCase().equals("VARCHAR")) {
						string_value = rs.getString(i);
						this.setColName(rsmd.getColumnName(i), this
								.toFormatString(string_value), map);
					}
					if (columnType.toUpperCase().equals("INTEGER")
							|| columnType.toUpperCase().equals("SMALLINT")
							|| columnType.toUpperCase().equals("INT")) {
						int_value = rs.getInt(i);
						this.setColName(rsmd.getColumnName(i), int_value + "",
								map);
					}

					if (columnType.toUpperCase().equals("INT IDENTITY")) {
						int_value = Integer.parseInt(rs.getObject(i).toString());
						this.setColName(rsmd.getColumnName(i), int_value + "",map);
					}
					if (columnType.toUpperCase().equals("BIGINT")) {
						long_value = rs.getLong(i);
						this.setColName(rsmd.getColumnName(i), long_value + "",
								map);
					}
					if (columnType.toUpperCase().equals("DOUBLE")) {
						double_value = rs.getDouble(i);
						this.setColName(rsmd.getColumnName(i), double_value
								+ "", map);
					}
					
					if ( columnType.toUpperCase().equals("NUMERIC")) {
						double_value = rs.getDouble(i);
						this.setColName(rsmd.getColumnName(i), double_value
								+ "", map);
					}

					if (columnType.toUpperCase().equals("DECIMAL")) {
						double_value = rs.getBigDecimal(i).doubleValue();
						this.setColName(rsmd.getColumnName(i), double_value
								+ "", map);
					}
					if (columnType.toUpperCase().equals("FLOAT")) {
						float_value = rs.getFloat(i);
						this.setColName(rsmd.getColumnName(i),
								float_value + "", map);
					}

				}
				rs_list.add(map);
			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				DebugUtil
						.print("DataOperation.queryToList() " + e.getMessage());
			}
		}
		return rs_list;
	}
	
	/**
	 * 执行查询，返回一个list的结果集 多用于不用分页
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List queryToList1(String sql) throws SQLException {
		List rs_list = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				HashMap map = new HashMap();
				for (int i = 1; i <= columnCount; i++) {
					columnType = rsmd.getColumnTypeName(i).trim();
					if (columnType.toUpperCase().equals("CHAR")
							|| columnType.toUpperCase().equals("TEXT")
							|| columnType.toUpperCase().equals("VARCHAR")) {
						string_value = rs.getString(i);
						this.setColName(rsmd.getColumnName(i), this
								.toFormatString(string_value), map);
					}
					if (columnType.toUpperCase().equals("INTEGER")
							|| columnType.toUpperCase().equals("SMALLINT")
							|| columnType.toUpperCase().equals("INT")) {
						int_value = rs.getInt(i);
						this.setColName(rsmd.getColumnName(i), int_value + "",
								map);
					}

					if (columnType.toUpperCase().equals("INT IDENTITY")) {
						int_value = Integer.parseInt(rs.getObject(i).toString());
						this.setColName(rsmd.getColumnName(i), int_value + "",map);
					}
					if (columnType.toUpperCase().equals("BIGINT")) {
						long_value = rs.getLong(i);
						this.setColName(rsmd.getColumnName(i), long_value + "",
								map);
					}
					if (columnType.toUpperCase().equals("DOUBLE")) {
						double_value = rs.getDouble(i);
						this.setColName(rsmd.getColumnName(i), double_value
								+ "", map);
					}
					
					if ( columnType.toUpperCase().equals("NUMERIC")) {
						double_value = rs.getDouble(i);
						this.setColName(rsmd.getColumnName(i), double_value
								+ "", map);
					}

					if (columnType.toUpperCase().equals("DECIMAL")) {
						double_value = rs.getBigDecimal(i).doubleValue();
						this.setColName(rsmd.getColumnName(i), double_value
								+ "", map);
					}
					if (columnType.toUpperCase().equals("FLOAT")) {
						float_value = rs.getFloat(i);
						this.setColName(rsmd.getColumnName(i),
								float_value + "", map);
					}

				}
				rs_list.add(map);
			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				DebugUtil
						.print("DataOperation.queryToList() " + e.getMessage());
			}
		}
		return rs_list;
	}
	/**
	 * 执行查询，返回一个list的结果集 多用于不用分页
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List callToList(String sql,String[] a) throws SQLException {
		List rs_list = new ArrayList();
		CallableStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareCall(sql);
			for(int i=1;i<=a.length;i++){
				pst.setString(i,a[i-1].trim());//
			}
			rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				HashMap map = new HashMap();
				for (int i = 1; i <= columnCount; i++) {
					columnType = rsmd.getColumnTypeName(i).trim();
					if (columnType.toUpperCase().equals("CHAR")
							|| columnType.toUpperCase().equals("TEXT")
							|| columnType.toUpperCase().equals("VARCHAR")) {
						string_value = rs.getString(i);
						this.setColName(rsmd.getColumnName(i), this
								.toFormatString(string_value), map);
					}
					if (columnType.toUpperCase().equals("INTEGER")
							|| columnType.toUpperCase().equals("SMALLINT")
							|| columnType.toUpperCase().equals("NUMERIC")
							|| columnType.toUpperCase().equals("INT")) {
						int_value = rs.getInt(i);
						this.setColName(rsmd.getColumnName(i), int_value + "",
								map);
					}

					if (columnType.toUpperCase().equals("INT IDENTITY")) {
						int_value = Integer.parseInt(rs.getObject(i).toString());
						this.setColName(rsmd.getColumnName(i), int_value + "",map);
					}
					if (columnType.toUpperCase().equals("BIGINT")) {
						long_value = rs.getLong(i);
						this.setColName(rsmd.getColumnName(i), long_value + "",
								map);
					}
					if (columnType.toUpperCase().equals("DOUBLE")) {
						double_value = rs.getDouble(i);
						this.setColName(rsmd.getColumnName(i), double_value
								+ "", map);
					}

					if (columnType.toUpperCase().equals("DECIMAL")) {
						double_value = rs.getBigDecimal(i).doubleValue();
						this.setColName(rsmd.getColumnName(i), double_value
								+ "", map);
					}
					if (columnType.toUpperCase().equals("FLOAT")) {
						float_value = rs.getFloat(i);
						this.setColName(rsmd.getColumnName(i),
								float_value + "", map);
					}

				}
				rs_list.add(map);
			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				DebugUtil
						.print("DataOperation.queryToList() " + e.getMessage());
			}
		}
		return rs_list;
	}
	/**
	 * 执行查询，返回一个指定结果集的子集 多适用于分页的
	 * 
	 * @param sql
	 * @param start
	 * @param end
	 * @return
	 * @throws SQLException
	 */
	public List queryToList2(String sql, int start, int end) throws SQLException {
		List rs_list=new ArrayList();
		PreparedStatement pst=null;
		ResultSet rs=null;
		int cur_loc=0;
		try{
			pst=con.prepareStatement(sql);
			rs=pst.executeQuery();	
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while(rs.next()){
				
				if(start<=cur_loc && cur_loc<end){//取指定范围内的行
				HashMap map=new HashMap();
				for(int i=1;i<=columnCount;i++){
					columnType = rsmd.getColumnTypeName(i).trim();
					if (columnType.toUpperCase().equals("CHAR") || columnType.toUpperCase().equals("VARCHAR")||columnType.toUpperCase().equals("NTEXT")||columnType.toUpperCase().equals("TEXT")){
						string_value = rs.getString(i);
						this.setColName(rsmd.getColumnName(i),this.toFormatString(string_value),map);	
						}
					if (columnType.toUpperCase().equals("INTEGER") || columnType.toUpperCase().equals("SMALLINT") || columnType.toUpperCase().equals("INT") || columnType.toUpperCase().equals("INT IDENTITY")){
						int_value = rs.getInt(i);
					    this.setColName(rsmd.getColumnName(i),int_value+"",map);
					}
					if (columnType.toUpperCase().equals("BIGINT")){
						long_value = rs.getLong(i);
						this.setColName(rsmd.getColumnName(i),long_value+"",map);	
					}
					if (columnType.toUpperCase().equals("DOUBLE")){
						double_value = rs.getDouble(i);
						this.setColName(rsmd.getColumnName(i),double_value+"",map);	
					}
					if (columnType.toUpperCase().equals("FLOAT")){
						float_value = rs.getFloat(i);
						this.setColName(rsmd.getColumnName(i),float_value+"",map);
					}
					if (columnType.toUpperCase().equals("NUMERIC") || columnType.toUpperCase().equals("DECIMAL")){
						double_value = rs.getDouble(i);
						this.setColName(rsmd.getColumnName(i),double_value+"",map);
					}
					
					
				}
				rs_list.add(map);
				}
				cur_loc++;
			}//end while
			
		}
			finally{
				try{
					if(rs!=null){rs.close();}
					if(pst!=null){pst.close();}
				}catch(SQLException e){
					DebugUtil.print("DataOperation.queryToList() "+e.getMessage());
				}
			}
		this.setCount(cur_loc);
		return rs_list;
	}
	/**
	 * 执行查询，返回一个指定结果集的子集 多适用于分页的
	 * 
	 * @param sql
	 * @param start
	 * @param end
	 * @return
	 * @throws SQLException
	 */
	public List queryToList(String sql, int start, int end) throws SQLException {
		List rs_list = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			if (rs.absolute(start)) {
				do {

					HashMap map = new HashMap();
					for (int i = 1; i <= columnCount; i++) {
						columnType = rsmd.getColumnTypeName(i).trim();
						if (columnType.toUpperCase().equals("CHAR")
								|| columnType.toUpperCase().equals("VARCHAR")) {
							string_value = rs.getString(i);
							this.setColName(rsmd.getColumnName(i), this
									.toFormatString(string_value), map);
						}
						if (columnType.toUpperCase().equals("INTEGER")
								|| columnType.toUpperCase().equals("SMALLINT")
								|| columnType.toUpperCase().equals("INT")) {
							int_value = rs.getInt(i);
							this.setColName(rsmd.getColumnName(i), int_value
									+ "", map);
						}
						if (columnType.toUpperCase().equals("BIGINT")) {
							long_value = rs.getLong(i);
							this.setColName(rsmd.getColumnName(i), long_value
									+ "", map);
						}
						if (columnType.toUpperCase().equals("DOUBLE")) {
							double_value = rs.getDouble(i);
							this.setColName(rsmd.getColumnName(i), double_value
									+ "", map);
						}
						if (columnType.toUpperCase().equals("FLOAT")) {
							float_value = rs.getFloat(i);
							this.setColName(rsmd.getColumnName(i), float_value
									+ "", map);
						}

					}
					rs_list.add(map);
					start++;
				} while (rs.next() && start < end);

			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				DebugUtil
						.print("DataOperation.queryToList() " + e.getMessage());
			}
		}
		return rs_list;
	}

	/**
	 * 多用于执行存储过程，但返回时没有列名的
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet queryToResultSet(String sql) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException e) {
				DebugUtil.print("DataOperation.queryToResultSet() "
						+ e.getMessage());
			}
		}

		return rs;
	}

	/**
	 * 执行查询，返回一个bean_list 利用反射
	 * 
	 * @param sql
	 * @param className
	 *            bean 的类名
	 * @param map
	 *            －－bean 的属性与返回结果集的列名的对照表
	 * @return
	 */
	public List queryToBean(String sql, String className, HashMap map)
			throws Exception {
		List rs_list = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();

			Object obj = null;
			rs_list = new ArrayList();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			while (rs.next()) {
				obj = Class.forName(className).newInstance();
				// 初始化列名;
				for (int i = 1; i <= columnCount; i++) {
					columnType = rsmd.getColumnTypeName(i).trim();
					if (columnType.toUpperCase().equals("CHAR")
							|| columnType.toUpperCase().equals("VARCHAR")) {
						string_value = rs.getString(rsmd.getColumnName(i)
								.trim());
						if (string_value != null) {
							Method m = obj.getClass().getMethod(
									getSetMethodName(this.getMappingName(map,
											colName, i)),
									new Class[] { string_value.getClass() });
							m.invoke(obj, new Object[] { string_value });
							// //System.out.println(srecordValue);
						}
					} else if (columnType.toUpperCase().equals("INTEGER")
							|| columnType.toUpperCase().equals("SMALLINT")
							|| columnType.toUpperCase().equals("INT")) {
						int_value = rs.getInt(rsmd.getColumnName(i));

						Method m = obj.getClass()
								.getMethod(
										getSetMethodName(this.getMappingName(
												map, colName, i)),
										new Class[] { new Integer(int_value)
												.getClass() });
						m.invoke(obj, new Object[] { new Integer(int_value) });

					} else if (columnType.toUpperCase().equals("BIGINT")) {

						long_value = rs.getLong(rsmd.getColumnName(i));
						Method m = obj.getClass()
								.getMethod(
										getSetMethodName(this.getMappingName(
												map, colName, i)),
										new Class[] { new Long(long_value)
												.getClass() });
						m.invoke(obj, new Object[] { new Long(long_value) });

					} else if (columnType.toUpperCase().equals("DOUBLE")) {

						double_value = rs.getDouble(rsmd.getColumnName(i));
						Method m = obj.getClass().getMethod(
								getSetMethodName(this.getMappingName(map,
										colName, i)),
								new Class[] { new Double(double_value)
										.getClass() });
						m
								.invoke(obj, new Object[] { new Double(
										double_value) });
						// //System.out.println(drecordValue);

					} else if (columnType.toUpperCase().equals("FLOAT")) {

						float_value = rs.getFloat(rsmd.getColumnName(i));
						Method m = obj.getClass()
								.getMethod(
										getSetMethodName(this.getMappingName(
												map, colName, i)),
										new Class[] { new Float(float_value)
												.getClass() });

						m.invoke(obj, new Object[] { new Float(float_value) });

					}
				}

				// //System.out.println("test3");
				rs_list.add(obj);

			}

		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (SQLException e) {
				DebugUtil
						.print("DataOperation.queryToBean() " + e.getMessage());
			}

		}
		return rs_list;
	}

	/**
	 * 执行查询，返回一个指定子结果集的bean_list 利用反射
	 * 
	 * @param sql
	 * @param className
	 *            bean 的类名
	 * @param map
	 *            －－bean 的属性与返回结果集的列名的对照表
	 * @param start
	 * @param end
	 * @return
	 */
	public List queryToBean(String sql, String className, HashMap map,
			int start, int end) throws Exception {
		List rs_list = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();

			Object obj = null;
			rs_list = new ArrayList();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			if (rs.absolute(start)) {
				do {
					obj = Class.forName(className).newInstance();
					// 初始化列名;
					for (int i = 1; i <= columnCount; i++) {
						columnType = rsmd.getColumnTypeName(i).trim();
						if (columnType.toUpperCase().equals("CHAR")
								|| columnType.toUpperCase().equals("VARCHAR")) {
							string_value = rs.getString(rsmd.getColumnName(i)
									.trim());
							if (string_value != null) {
								Method m = obj.getClass()
										.getMethod(
												getSetMethodName(this
														.getMappingName(map,
																colName, i)),
												new Class[] { string_value
														.getClass() });
								m.invoke(obj, new Object[] { string_value });
								// //System.out.println(srecordValue);
							}
						} else if (columnType.toUpperCase().equals("INTEGER")
								|| columnType.toUpperCase().equals("SMALLINT")
								|| columnType.toUpperCase().equals("INT")) {
							int_value = rs.getInt(rsmd.getColumnName(i));

							Method m = obj.getClass().getMethod(
									getSetMethodName(this.getMappingName(map,
											colName, i)),
									new Class[] { new Integer(int_value)
											.getClass() });
							m.invoke(obj,
									new Object[] { new Integer(int_value) });

						} else if (columnType.toUpperCase().equals("BIGINT")) {

							long_value = rs.getLong(rsmd.getColumnName(i));
							Method m = obj.getClass().getMethod(
									getSetMethodName(this.getMappingName(map,
											colName, i)),
									new Class[] { new Long(long_value)
											.getClass() });
							m
									.invoke(obj, new Object[] { new Long(
											long_value) });

						} else if (columnType.toUpperCase().equals("DOUBLE")) {

							double_value = rs.getDouble(rsmd.getColumnName(i));
							Method m = obj.getClass().getMethod(
									getSetMethodName(this.getMappingName(map,
											colName, i)),
									new Class[] { new Double(double_value)
											.getClass() });
							m.invoke(obj, new Object[] { new Double(
									double_value) });
							// //System.out.println(drecordValue);

						} else if (columnType.toUpperCase().equals("FLOAT")) {

							float_value = rs.getFloat(rsmd.getColumnName(i));
							Method m = obj.getClass().getMethod(
									getSetMethodName(this.getMappingName(map,
											colName, i)),
									new Class[] { new Float(float_value)
											.getClass() });

							m.invoke(obj,
									new Object[] { new Float(float_value) });

						}
					}

					// //System.out.println("test3");
					rs_list.add(obj);

					start++;
				} while (rs.next() && start < end);

			}
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (SQLException e) {
				DebugUtil.print("DataOperation.queryToBean() :"
						+ e.getMessage());
			}

		}
		return rs_list;
	}

	/** ************公共处理函数 start*********************************** */
	private double getRound(double dou, int bit) {
		double b = 10;
		b = Math.pow(b, bit);
		return ((double) Math.round(dou * b)) / b;
	}

	private String toFormatString(String par) {
		if (par == null || par.equals("NULL") || par.equals("null")) {
			return "";
		} else {
			return par;
		}
	}

	/**
	 * 设置同列名自动递增
	 * 
	 * @param key
	 * @param keyValue
	 * @param map
	 */
	private void setColName(String key, String keyValue, HashMap map) {
		// if(org!=null)
		int i = 1;
		key = key == null ? "" : key.toLowerCase();
		String temp = key;
		while (map.containsKey(temp)) {
			temp = key + i;
			i++;
		}
		map.put(temp, keyValue);

	}

	private String getSetMethodName(String columnName) {
		String reSetMethod = new String();
		String firstChar = new String();
		String otherChar = new String();

		firstChar = columnName.substring(0, 1).toUpperCase();
		otherChar = columnName.substring(1);

		reSetMethod = "set" + firstChar + otherChar;
		return reSetMethod;
	}

	private String getMappingName(HashMap colName, HashMap colName2, int i) {
		String re_str = "";
		re_str = colName.get(colName2.get(i + "")).toString();
		return re_str;
	}

	private void setInivColName(ResultSetMetaData rsmd, HashMap colName)
			throws SQLException {
		int count = rsmd.getColumnCount();

		String temp = "";
		for (int i = 1; i < count; i++) {
			temp = rsmd.getColumnName(i).toLowerCase();
			int j = 1;
			while (colName.containsKey(temp)) {
				temp += j;
				j++;
			}
			colName.put(i + "", temp);
		}
	}

	public boolean isCloseCon() throws SQLException {
		if (this.con != null) {
			return this.con.isClosed();
		} else {
			return false;
		}
	}

	public Connection getConnection() {
		return this.con;
	}

	public void closeCon() throws SQLException {
		this.con.close();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	/** ************公共处理函数 end*********************************** */

	/**
	 * **********操作
	 * 结束*********************************************************************
	 */

}