package com.gzunicorn.struts.action.xjsgg;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * 远程下载文件，使用URL进行下载，HttpURLConnection
 * @author Lijun
 *
 */
public class SendURLFile {
	 
	public static void main(String[] args) {

	}
	
	/**
	 * 确认文件是否存在
	 * @param fileid 文件ID
	 * @return status 返回状态 【2001：文件存在】
	 * @throws IOException
	 */
	public static boolean isFileExist(String fileid){
		boolean finash=false;
		BufferedReader br =null;
		try{
			String urlStr = "http://10.10.0.6:9097/PM/crm/isFileExist.do?fileId="+fileid;   
			URL url = new URL(urlStr);   
			HttpURLConnection hconn = (HttpURLConnection) url.openConnection(); //获取连接   
			hconn.setRequestMethod("GET"); //设置请求方法为POST, 也可以为GET
			hconn.setConnectTimeout(60*1000);//设置连接主机超时（单位：毫秒）
			hconn.setReadTimeout(120*1000);//设置从主机读取数据超时（单位：毫秒）
			hconn.setDoOutput(true);// 使用 URL 连接进行输出
			hconn.setDoInput(true);// 使用 URL 连接进行输入
			hconn.setRequestProperty("Charset", "utf-8");//设置字符编码
			hconn.connect();
			
			InputStream is = hconn.getInputStream(); 
			br = new BufferedReader(new InputStreamReader(is)); 
			String sbstr=br.readLine();
//			StringBuilder sb = new StringBuilder();   
//			sb.append("{");
//			while (br.read() != -1) {   
//				sb.append(br.readLine());   
//			}
			br.close();
			
			System.out.println("判断文件是否存在; fileid="+fileid+"; 返回状态="+sbstr);
			
			JSONObject jsStr = new JSONObject(sbstr);
			String status= String.valueOf(jsStr.getInt("status"));//获取id的值
			
			if("2001".equals(status)){
				/**
				 	操作状态：
					2001：文件存在
					2000：文件不存在
					9000：参数异常
					9009：未知异常
				 * */
				finash=true;
			}else{
				finash=false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			try {
				if (br != null) {br.close();}
			} catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
		
		return finash; 
	}

	/**
	 * 远程下载文件
	 * @param fileid 文件ID
	 * @param response
	 * @param filename 文件名称
	 * @throws Exception
	 */
	public static void loadFile(String fileid,HttpServletResponse response,String filename){
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			String urlStr = "http://10.10.0.6:9097/PM/crm/loadFile.do?fileId="+fileid;
			URL url = new URL(urlStr);
			  
			HttpURLConnection hconn = (HttpURLConnection) url.openConnection(); //获取连接  		
			hconn.setRequestMethod("GET"); //设置请求方法为POST, 也可以为GET
			hconn.setConnectTimeout(60*1000);//设置连接主机超时（单位：毫秒）
			hconn.setReadTimeout(120*1000);//设置从主机读取数据超时（单位：毫秒）
			hconn.setDoOutput(true);// 使用 URL 连接进行输出
			hconn.setDoInput(true);// 使用 URL 连接进行输入
			hconn.setRequestProperty("Charset", "GBK");//设置字符编码
			hconn.connect();
			
			System.out.println("开始下载远程文件>>>>fileid="+fileid);
			
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(filename, "utf-8"));

			bis = new BufferedInputStream(hconn.getInputStream());
			bos = new BufferedOutputStream(response.getOutputStream());

			int bytesRead = 0;
			byte[] buffer = new byte[5 * 1024];
			while ((bytesRead = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
				bos.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			try {
				
				if (bos != null) {bos.close();}
				if (bis != null) {bis.close();}
				
			} catch (Exception e) { 
				e.printStackTrace(); 
			}
		}
        
	}

}
