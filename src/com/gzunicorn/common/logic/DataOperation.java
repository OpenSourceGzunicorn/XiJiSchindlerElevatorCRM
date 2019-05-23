/*
 * 创建日期 2006-2-20
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.gzunicorn.common.logic;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 *  @method:
 *  @method_type:
 *  @param:
 *  @external_call:
 *  @function: 
 *  @author rr
 *  @create_date:
 *
 */
public class DataOperation {
	private Connection con=null;
	private DataSource ds=null;
	
	String columnType = new String();
	String srecordValue="";
	
	int irecordValue;
	long lrecordValue;
	double drecordValue;
	float frecordValue;
	
	float sum=0;
	
	public DataOperation(){
	}
	/************初始化　开始**********************************************************************/
	/**
	 * 检查是否已连接
	 * @return
	 * @throws SQLException
	 */
	
	public boolean isConnection()throws SQLException{
		return !con.isClosed();
	}
	/**
	 * 设置数据源
	 * @param ds
	 */
	public void setDataSource(DataSource ds){
		this.ds=ds;
	}
	/**
	 * 取数据源
	 * @return
	 */
	public DataSource getDataSource(){
			return this.ds;
	}
	/**
	 * 设置连接
	 * @param con
	 * @throws SQLException
	 */
	public void setCon(Connection con)throws SQLException{
		//if(con==null || con.isClosed()){
			this.con=con;
		//}
		////System.out.println("ddddddddddddddddd5555555555");
	}
	/**
	 * 取连接
	 * @return
	 */
	public Connection getConnection(){
		return this.con;
	}
	/************初始化　结束**********************************************************************/
	/************关闭　开始**********************************************************************/
	public void closeCon()throws SQLException{
		if(con!=null && !this.con.isClosed()){
			this.con.close();
		}
	}
	/************关闭　结束**********************************************************************/
	/************操作　开始**********************************************************************/
	/**
	 * 通过一个SQL获得一个结果集的LIST
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public List getSPList(String sql)throws SQLException{
		List rs_list=new ArrayList();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			pst=con.prepareStatement(sql);
			rs=pst.executeQuery();	
			 
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			float total=0;
			float temp=0;
			while(rs.next()){
				HashMap map=new HashMap();
				for(int i=1;i<=columnCount;i++){
					columnType = rsmd.getColumnTypeName(i).trim();
					if (columnType.toUpperCase().equals("CHAR") || columnType.toUpperCase().equals("VARCHAR")){
						//srecordValue = rs.getString(rsmd.getColumnName(i).trim());
						srecordValue = rs.getString(i);
						//map.put(rsmd.getColumnName(i),this.toFormatString(srecordValue));
						this.toSaveCol(rsmd.getColumnName(i),this.toFormatString(srecordValue),map);	
						}
					if (columnType.toUpperCase().equals("INTEGER") || columnType.toUpperCase().equals("SMALLINT") || columnType.toUpperCase().equals("INT") || columnType.toUpperCase().equals("INT IDENTITY")){
						  //irecordValue = rs.getInt(rsmd.getColumnName(i));
						  //map.put(rsmd.getColumnName(i),lrecordValue+"");
						irecordValue = rs.getInt(i);
					      this.toSaveCol(rsmd.getColumnName(i),irecordValue+"",map);
					}
					if (columnType.toUpperCase().equals("BIGINT")){
						 //lrecordValue = rs.getLong(rsmd.getColumnName(i));
						 //map.put(rsmd.getColumnName(i),lrecordValue+"");
						lrecordValue = rs.getLong(i);
						this.toSaveCol(rsmd.getColumnName(i),lrecordValue+"",map);	
					}
					if (columnType.toUpperCase().equals("DOUBLE")){
						 //drecordValue = rs.getDouble(rsmd.getColumnName(i));
						 //map.put(rsmd.getColumnName(i),drecordValue+"");
						drecordValue = rs.getDouble(i);
						this.toSaveCol(rsmd.getColumnName(i),drecordValue+"",map);	
					}
					if (columnType.toUpperCase().equals("FLOAT")){
						 //frecordValue = rs.getFloat(rsmd.getColumnName(i));
						 //map.put(rsmd.getColumnName(i),frecordValue+"");
						frecordValue = rs.getFloat(i);
						this.toSaveCol(rsmd.getColumnName(i),frecordValue+"",map);
						////System.out.println("aaaaaaa---------"+frecordValue);
					}if (columnType.toUpperCase().equals("NUMERIC")){
						 //frecordValue = rs.getFloat(rsmd.getColumnName(i));
						 //map.put(rsmd.getColumnName(i),frecordValue+"");
						drecordValue = rs.getDouble(i);
						this.toSaveCol(rsmd.getColumnName(i),drecordValue+"",map);
						////System.out.println("aaaaaaa---------"+frecordValue);
					}
				
				}
			rs_list.add(map);
			}
		}
			finally{
				try{
					if(rs!=null){rs.close();}
					if(pst!=null){pst.close();}
				}catch(SQLException e){
					//System.out.println(""+e.getMessage());
				}
			}
		return rs_list;
	}


			
	/**
		 * 通过一个SQL获得一个结果集的LIST
		 * @param sql
		 * @return
		 * @throws SQLException
		 */
		public List getSPList(String sql,int sumrow)throws SQLException{
					List rs_list=new ArrayList();
					PreparedStatement pst=null;
					ResultSet rs=null;
					try{
						pst=con.prepareStatement(sql);
						rs=pst.executeQuery();	
						ResultSetMetaData rsmd = rs.getMetaData();
						int columnCount = rsmd.getColumnCount();
						float total=0;
						float temp=0;
						while(rs.next()){
							HashMap map=new HashMap();
							for(int i=1;i<=columnCount;i++){
								columnType = rsmd.getColumnTypeName(i).trim();
								if (columnType.toUpperCase().equals("CHAR") || columnType.toUpperCase().equals("VARCHAR")){
									srecordValue = rs.getString(rsmd.getColumnName(i).trim());
									map.put(rsmd.getColumnName(i),this.toFormatString(srecordValue));
									
									}
								if (columnType.toUpperCase().equals("INTEGER") || columnType.toUpperCase().equals("SMALLINT") || columnType.toUpperCase().equals("INT")){
									  irecordValue = rs.getInt(rsmd.getColumnName(i));
									  map.put(rsmd.getColumnName(i),irecordValue+"");
									 
									  if(this.sumRow(columnType,i,sumrow)){//列合计
									  	this.addSum(irecordValue);
									  }
								}
								if (columnType.toUpperCase().equals("BIGINT")){
									 lrecordValue = rs.getLong(rsmd.getColumnName(i));
									 map.put(rsmd.getColumnName(i),lrecordValue+"");	
									
									if(this.sumRow(columnType,i,sumrow)){//列合计
										this.addSum(lrecordValue);
									 }
								}
								if (columnType.toUpperCase().equals("DOUBLE")){
									 drecordValue = rs.getDouble(rsmd.getColumnName(i));
									 map.put(rsmd.getColumnName(i),drecordValue+"");	
									 
									if(this.sumRow(columnType,i,sumrow)){//列合计
										this.addSum((float)drecordValue);
									 }
								}
								if (columnType.toUpperCase().equals("FLOAT")){
									 frecordValue = rs.getFloat(rsmd.getColumnName(i));
									 map.put(rsmd.getColumnName(i),frecordValue+"");
									 
									if(this.sumRow(columnType,i,sumrow)){//列合计
										this.addSum(frecordValue);
									 }
								}						
						}
						rs_list.add(map);
						}
					}
						finally{
							try{
								if(rs!=null){rs.close();}
								if(pst!=null){pst.close();}
							}catch(SQLException e){
								//System.out.println("ffffff"+e.getMessage());
							}
						}
					return rs_list;
				}
	
	/**************统计某列的值　start************************************/
	public boolean sumRow(String type,int currow,int sumrow){
		if(currow==sumrow){
			if(type.toUpperCase().equals("INTEGER") || type.toUpperCase().equals("SMALLINT") 
			|| type.toUpperCase().equals("INT") || type.toUpperCase().equals("BIGINT")
			|| type.toUpperCase().equals("DOUBLE") || type.toUpperCase().equals("FLOAT")){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public void addSum(float value){
		this.sum+=value;
	}
	public void reSetSum(){
			this.sum=0;
	}
	public float getSum(int roundbit){
		
		return this.getRound(this.sum,roundbit);
	}
	
	public float getRound(float f,int bit){
			int b=1;
			for(int i=0;i<bit;i++){
				b=b*10;
			}
			return ((float)Math.round(f*b))/b;
	}
	/**************统计某列的值　end************************************/
	/**************公共处理函数　start************************************/
		public String toFormatString(String par){
			if(par==null||par.equals("NULL")||par.equals("null")){
				return "";
			}else{
				return par;
			}
		}
		/**
		 * 设置同列名自动递增
		 * @param key
		 * @param keyValue
		 * @param map
		 */
		public void toSaveCol(String key,String keyValue,HashMap map){
			//if(org!=null)
			int i=1;
			String temp=key.toLowerCase();
			while(map.containsKey(temp)){
				temp=key+i;
				i++;
			}
			map.put(temp,keyValue);
			
		}


	/**************公共处理函数　end************************************/
			
	/**
	 * 插入一条记录
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public boolean insertRecord(String sql)throws SQLException{
			Statement st=null;
			int rs=0;
			try{
				st=con.createStatement();
				rs=st.executeUpdate(sql);	
				}
			finally{
				try{
					if(st!=null){st.close();}
					}catch(SQLException e){
						//System.out.println("ffffff"+e.getMessage());
					}
			}
			if(rs>0){
				return true;
			}else{
				return false;
			}
	}
	
	public ResultSet executeSQLQuery(String sql)throws SQLException{
				PreparedStatement pst=null;
				ResultSet rs=null;
				
					pst=con.prepareStatement(sql);
					rs=pst.executeQuery();	
					//System.out.println("executesqlquery");
				return rs;
		}
	
	public String getNum(String ls_type,String operid)throws SQLException{
		
			String glideno="";
			ResultSet rs=null;
			CallableStatement cstmt=null;
			try{
				cstmt = this.con.prepareCall("{call sp_get_num(?,?)}");
				cstmt.setString(1,ls_type);
				cstmt.setString(2,operid);
				rs=cstmt.executeQuery();
				if(rs.next()){
					glideno=rs.getString(1);
				}
			}
			finally{
				try{
				if(rs!=null){ rs.close(); }
				if(cstmt!=null){ cstmt.close(); }
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		
		return glideno;
		}
	/************操作　结束**********************************************************************/
    public String getNameBySQL (String tablename,String column1,String column2,Object value){
		PreparedStatement pst=null;
		ResultSet rs=null;
			
    	String sql = null;
    	String name = null;
    	try{
    		sql = "select "+column2+" from "+tablename+" where "+column1+"='" + value+"'";
    		////System.out.println(sql);
    		pst=con.prepareStatement(sql);
    		rs=pst.executeQuery();

			while (rs.next()) {
				name = rs.getString(1);
			}
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				try{
					if(rs!=null){rs.close();}
					if(pst!=null){pst.close();}
				}catch(SQLException e){
					//System.out.println("ffffff"+e.getMessage());
				}
			}
    	
    	return name;
    }
	
	
}


