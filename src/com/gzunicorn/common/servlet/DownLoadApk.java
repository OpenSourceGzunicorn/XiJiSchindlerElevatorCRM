package com.gzunicorn.common.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownLoadApk extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public DownLoadApk() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		this.toDownloadFileRecord(request,response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		this.toDownloadFileRecord(request,response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	/**
	 * 下载安卓apk文件
	 */
	private void toDownloadFileRecord(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		//OutputStream fos = null;
		//InputStream fis = null;
		
		//String oldname="XJSMobileAndroid.apk";
		try {
			//文件保存路径
			String filepath="D:\\contract\\XJSMobileAndroid.apk";
			// path是指欲下载的文件的路径。
            File file = new File(filepath);
            // 取得文件名。
            String filename = file.getName();
            System.out.println(">>>>下载 "+filename);
            //以流的形式下载文件。
            bis = new BufferedInputStream(new FileInputStream(filepath));
            byte[] buffer = new byte[bis.available()];
            bis.read(buffer);
            bis.close();
            
            //清空response
            response.reset();
            
            //设置response的Header
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition","attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            
            bos = new BufferedOutputStream(response.getOutputStream());
            bos.write(buffer);
            bos.flush();
            bos.close();
            
			/**
			String localPath=request.getSession().getServletContext().getRealPath("");
			localPath=localPath+"\\uploadFile\\XJSMobileAndroid.apk";
			System.out.println(">>>>"+localPath);
	
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(oldname, "utf-8"));
	
			fis = new FileInputStream(localPath);
			bis = new BufferedInputStream(fis);
			fos = response.getOutputStream();
			bos = new BufferedOutputStream(fos);
	
			int bytesRead = 0;
			byte[] buffer = new byte[5 * 1024];
			while ((bytesRead = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
				bos.flush();
			}
			*/
		} catch (Exception e) {
			//e.printStackTrace();
		}finally { 
			try {
				//if (fos != null) {fos.close();}
				if (bos != null) {bos.close();}
				//if (fis != null) {fis.close();}
				if (bis != null) {bis.close();}
			} catch (IOException e) { 
				//e.printStackTrace(); 
			}
		}
	}
}
