package com.gzunicorn.common.util.pdf;

import java.awt.Dimension;
import java.awt.Insets;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;

/**
 * 
 * ClassName: Pdf
 * @Description: pdf文件导出功能
 * @author gtc
 * @date 2015-11-20
 */
public class Pdf {

	protected Dimension format = PD4Constants.A3;

	protected boolean landscapeValue = false;

	protected int topValue = 0;

	protected int leftValue = 0;

	protected int rightValue = 0;

	protected int bottomValue = 0;

	protected String unitsValue = "mm";

	protected String proxyHost = "";

	protected int proxyPort = 0;

	protected int userSpaceWidth = 1000;
	/**
	 * 
	 * @Description: 生成pdf文件
	 * @param @param htmlPath
	 * @param @param fileName
	 * @param @param response
	 * @param @throws IOException   
	 * @return void  
	 * @throws
	 * @author gtc
	 * @date 2015-11-20
	 */
	public void runConverter(String htmlPath, String fileName,
			HttpServletResponse response) throws IOException {
	
		if (htmlPath.length() > 0) {
			
			ByteArrayOutputStream ba = new ByteArrayOutputStream();

			if (proxyHost != null && proxyHost.length() != 0 && proxyPort != 0) {

				System.getProperties().setProperty("proxySet", "true");

				System.getProperties().setProperty("proxyHost", proxyHost);

				System.getProperties().setProperty("proxyPort", "" + proxyPort);

			}

			PD4ML pd4ml = new PD4ML();
			pd4ml.setPageSize(new java.awt.Dimension(450, 450));
			pd4ml.setPageInsets(new java.awt.Insets(20, 50, 10, 10));
			pd4ml.enableImgSplit(false);
			pd4ml.useTTF("java:/com/gzunicorn/common/util/pdf/fonts", true);
			pd4ml.enableDebugInfo();

			try {

				pd4ml.setPageSize(landscapeValue ? pd4ml
						.changePageOrientation(format) : format);

			} catch (Exception e) {

				e.printStackTrace();

			}

			if (unitsValue.equals("mm")) {

				pd4ml.setPageInsetsMM(new Insets(topValue, leftValue,

				bottomValue, rightValue));

			} else {

				pd4ml.setPageInsets(new Insets(topValue, leftValue,

				bottomValue, rightValue));

			}
			
			if(fileName.equals("安装维保交接电梯情况")){
				pd4ml.setHtmlWidth(1100);
			}else{
				pd4ml.setHtmlWidth(userSpaceWidth);
			}
			pd4ml.render("file:"+htmlPath, ba);
			
			if(fileName.lastIndexOf(".pdf")==-1)
				fileName = fileName + ".pdf";
			try {
				
				response.setHeader("Content-disposition",
						"attachment; filename="
								+ new String(fileName.getBytes("gb2312"),
										"iso8859-1"));
			
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setContentType("application/pdf");
			response.setContentLength(ba.size());
			
			try {
				ServletOutputStream out = response.getOutputStream();
				ba.writeTo(out);
				out.flush();
				out.close();
				ba.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
	}
	/**
	 * 
	 * @Description: 删除临时生成的html文件
	 * @param @param htmlPath   
	 * @return void  
	 * @throws
	 * @author gtc
	 * @date 2015-11-20
	 */
	public void deleHtmlFile(String htmlPath){
		File file=new File(htmlPath);
		if(file.isFile() && file.exists()){
			file.delete();
		}
	}
}