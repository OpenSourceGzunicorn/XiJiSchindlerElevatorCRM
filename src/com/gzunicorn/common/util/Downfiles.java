package com.gzunicorn.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 读取数据库保存的文件
 */
public class Downfiles {
	/**
	 * 读取数据库保存的文件
	 * @param request
	 * @param response
	 * @param sql 查询数据库的sql语句
	 * @param con 连接池
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void toDownFile(HttpServletRequest request, HttpServletResponse response,
			String sql,Connection con) throws IOException, ServletException {

		PreparedStatement ps = null;
		InputStream in = null; 
		String filename="";
		try
		{
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				try{
					
				filename=rs.getString("FileName");
				if(filename!=null){
					filename=new String(filename.getBytes("gb2312"),"iso-8859-1");
				}
				in = rs.getBinaryStream("UploadFiles");
						
				response.reset();
				
				//response.setContentType("application/x-msdownload");
				response.setContentType("application/msexcel");
				
//				response.setHeader("Content-Disposition", "attachment; filename="+filename);
				StringBuffer arg2=new StringBuffer();
				arg2.append("offline; filename=");
				arg2.append(filename);
				response.setHeader("Content-disposition", arg2.toString());
				//response.setContentLength(in.available());
				OutputStream outStream = response.getOutputStream();
				byte[] P_Buf = new byte[4096];
				int i = 0;
				
				if(in != null)
				{
					while ((i = in.read(P_Buf)) != -1) {
						outStream.write(P_Buf, 0, i);
					}
					in.close();
				}
				
				outStream.flush();
				outStream.close();	
				}
				catch(Throwable e){
				  	//System.out.println(e.toString());
					throw new ServletException(e.toString());
				  }

			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(ps!=null)
			{
				try
				{
					//con.close();
					ps.close();
				}
				catch(SQLException ex)
				{
					ex.printStackTrace();
				}
				
			}
		}
	}

}
