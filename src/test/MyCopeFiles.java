package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MyCopeFiles {

	/**lijun add 20140324
	 * @param args
	 */
	public static void main(String[] args) {

		copeFile();
		
		/**  更新文件路径.txt 里面的路径格式：
		 
		  src\com\gzunicorn\hibernate\contractmaster\SaleLfttecParm.java
		  
		  WebRoot\salecontract\ContractGroundAddT.jsp
		 */

	}

	public static void copeFile(){

		String infileurl = "D:\\Download\\XJSCRM更新文件路径.txt";//读取文件的路径
		String tofileurl = "D:\\Download\\XJSCRM\\";//输出文件所在的文件夹
		
		String fromfileurl = "D:\\workprojects\\XJSCRM\\";//项目的路径
		String outf = "WebRoot\\WEB-INF\\classes";
		
		File file = new File(infileurl);
		FileInputStream fis = null;
		BufferedReader br = null;
		//FileInputStream fis1 = null;
		InputStream In = null;
		
		try {
			fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis));
			String lineData = null; 
			
			String fileurl="";
			String fileurl1="";
			String fileurl2="";
			String fileurl3="";
			int flen=0;
			System.out.println("复制文件开始");
			int flent=0;
			while((lineData = br.readLine()) != null && !lineData.trim().equals("")){
				flent++;
				if(lineData.substring(0, 3).equals("src")){
					lineData = outf + lineData.substring(3,lineData.length());
					int index = lineData.lastIndexOf(".") + 1;
					if(lineData.substring(index).equals("java")){
						lineData = lineData.substring(0, index) + "class";
					}
				}
				flen=lineData.lastIndexOf("\\");
				fileurl=tofileurl+lineData.substring(0, flen);
				fileurl1=fromfileurl+lineData.substring(0, flen);
				fileurl2=fromfileurl+lineData;
				fileurl3=tofileurl+lineData;
				
				OutputStream out =null;
				File f2 = null;
				File f1 = new File(fileurl2);   
				if (!f1.isDirectory()) {// 如果不是一个文件夹
				    File f = new File(fileurl);//创建文件夹
					f.mkdirs();
				    
				    out = new FileOutputStream(fileurl+"\\"+f1.getName());
				    f2=new File(fileurl1+"\\"+f1.getName());
				    In = new FileInputStream(f2);
					
					byte[] buf = new byte[8192];
					int len = 0;
					while ((len = In.read(buf)) != -1) {
						out.write(buf, 0, len);
					}
					out.flush();
					out.close();
					In.close();
					
				 } else if (f1.isDirectory()) {// 如果是个文件夹
				    File f = new File(fileurl3);//创建文件夹
					f.mkdirs();
					
				    String[] filelist = f1.list();// 得到下面所有文件（包括文件夹）   
				    for (int i = 0; i < filelist.length; i++) {// 循环每一个文件
					     File readfile = new File(fileurl2 + "\\" + filelist[i]);
					     if (!readfile.isDirectory()) {// 如果不是文件夹 同上面的操作   
						      	out = new FileOutputStream(fileurl3+"\\"+readfile.getName());
							    f2=new File(fileurl2+"\\"+readfile.getName());
							    In = new FileInputStream(f2);
								
								byte[] buf = new byte[8192];
								int len = 0;
								while ((len = In.read(buf)) != -1) {
									out.write(buf, 0, len);
								}
								out.flush();
								out.close();
								In.close();
					     }   
				    }   
				} 
			}
			System.out.println(">>>>>>行数："+flent);
			System.out.println("复制文件结束");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
