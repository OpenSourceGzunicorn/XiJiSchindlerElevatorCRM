package com.gzunicorn.common.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.gzunicorn.common.util.DateUtil;

public class getImageServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public getImageServlet() {
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

		this.getImageRecord(request,response);
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

		this.getImageRecord(request,response);
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
	 * 生成二维码
	 */
	private void getImageRecord(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		try {
			//获取访问路径
			String path = request.getContextPath();
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
			String pdfstr=basePath+"DownLoadApkServlet";//本地系统
			
			//String pdfstr="http://10.10.0.5:8080/XJSCRM/DownLoadApkServlet";//测试系统
			//String pdfstr="http://www.xjelevator.com:9000/XJSCRM/DownLoadApkServlet";//正式系统
			//System.out.println(">>>>>="+pdfstr);

			//生成二维码图片
			pdfstr = new String(pdfstr.getBytes("UTF-8"), "ISO-8859-1"); //防止二维码出现中文乱码
			BitMatrix byteMatrix = new MultiFormatWriter().encode(pdfstr, BarcodeFormat.QR_CODE, 150, 140);
			
			//需要导入javase.jar包
			BufferedImage image = MatrixToImageWriter.toBufferedImage(byteMatrix);
			
			//将二维码图片背景色透明,并写入文字
			BufferedImage image2=this.transparentImage(image,10);
			
			//将图片读写到返回流
			ImageIO.write(image2, "png", response.getOutputStream());
			
			/**
			//保存图片到本地
			String stodaytime=DateUtil.getNowTime("yyyyMMddHHmmss");
			String savepath="D:\\Download\\"+stodaytime+".png";
			FileOutputStream fos=new FileOutputStream(savepath);
			ImageIO.write(image2, "png", fos);
			fos.close();
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	* 设置源图片为背景透明，并设置透明度 ，增加字体
	* @param srcImage 源图片 
	* @param alpha 透明度 1-10
	* @throws IOException 
	*/  
	private BufferedImage transparentImage(BufferedImage srcImage,int alpha) throws IOException {  
			int imgHeight = srcImage.getHeight();//取得图片的长和宽  
	        int imgWidth = srcImage.getWidth();  
	        int c = srcImage.getRGB(3, 3);  
	        //防止越位  
	        if (alpha < 0) {  
	            alpha = 0;  
	         } else if (alpha > 10) {  
	            alpha = 10;  
	         }  
	        
	        //新建一个类型支持透明的BufferedImage
	        BufferedImage bi = new BufferedImage(imgWidth, imgHeight,BufferedImage.TYPE_4BYTE_ABGR);  
	        //把原图片的内容复制到新的图片，同时把背景设为透明  
	        for(int i = 0; i < imgWidth; ++i){  
	            for(int j = 0; j < imgHeight; ++j){  
	            	//把背景设为透明  
	                if(srcImage.getRGB(i, j) == c){  
	                    bi.setRGB(i, j, c & 0x00ffffff);  
	                }else{  
		                //设置透明度 
	                	int rgb = bi.getRGB(i, j);  
	                    rgb = ((alpha * 255 / 10) << 24) | (rgb & 0x00ffffff);  
	                    bi.setRGB(i, j, rgb);  
	                }  
	            }  
	        }  
	        
	        /**============在图片里面增加字体================*/
			Graphics grap = bi.getGraphics();
			//设置颜色 黑色
			grap.setColor(Color.black);
			//字体、字型、字号
			grap.setFont(new Font("宋体",Font.PLAIN,12)); 
			//输出文字  [显示的内容， 左距离，上距离]
			grap.drawString("请扫描二维码,下载客户端",8,130);
			
	        return bi;
	}
	
}
