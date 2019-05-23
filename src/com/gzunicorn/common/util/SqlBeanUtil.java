package com.gzunicorn.common.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;

public class SqlBeanUtil {
		
	public static String[] getColumnNames(Class<?> clazz){
		Class<?> superClazz = clazz.getSuperclass();
		Field[] fields= superClazz.getDeclaredFields();
		String[] names = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			names[i] = fields[i].getName();
		}
		return names;		
	}
	
	public static String getColumnNamesStr(Class<?> clazz,String separator){
		Class<?> superClazz = clazz.getSuperclass();
		Field[] fields= superClazz.getDeclaredFields();
		StringBuilder names = new StringBuilder();
		int len = fields.length - 1;
		for (int i = 0; i < len; i++) {
			names.append(fields[i].getName()).append(separator);
		}
		names.append(fields[len].getName());
		return names.toString();		
	}
	
	public static Set<String> getColumnNamesSet(Class<?> clazz){
		Class<?> superClazz = clazz.getSuperclass();
		Field[] fields= superClazz.getDeclaredFields();
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < fields.length; i++) {
			set.add(fields[i].getName());
		}
		return set;		
	}
	
	public static String[] getColumnNames(ResultSet rs) throws SQLException{	
		String[] columnNames = null;
		if(rs != null){
			ResultSetMetaData rsmd = rs.getMetaData();
			int col = rsmd.getColumnCount();
			columnNames = new String[col];	

			for(int i = 0; i < col; i++){
				columnNames[i] = formatName(rsmd.getColumnName(i+1));
			}
		}

		return columnNames;
	}
	
	public static String formatName (String name){
		char[] newName = name.toCharArray();
		char[] c = name.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if(i==0 || (c[i] >='A' && c[i]<='Z' && c[i-1] >='A' && c[i-1]<='Z')){
				newName[i] = (char) (name.charAt(i) + 32);
			}
		}
		return new String(newName);
	}

}