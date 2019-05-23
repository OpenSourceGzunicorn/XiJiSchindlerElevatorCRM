package com.gzunicorn.common.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.pdf.Pdf;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
/**
 * 
 * ClassName: PdfServlet
 * @Description: 进去pdf类的入口
 * @author gtc
 * @date 2015-11-20
 */
public class ContractPdfServlet extends HttpServlet {

	
	public ContractPdfServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		 this.doPost(request, response);
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//String filePath=request.getSession().getServletContext().getRealPath("/");
		 ViewLoginUserInfo userInfo = (ViewLoginUserInfo) request.getSession()
					.getAttribute(SysConfig.LOGIN_USER_INFO);
		 String userid = userInfo.getUserID();
		
		 Pdf p=new Pdf();
		 String flag=request.getParameter("flag");//得到是哪个要导出
		 String htmlName=request.getParameter("htmlName");//得到是那个文件名
		 String htmlPath="";
		 if("PullDown".equals(flag)){
			 htmlPath=PropertiesUtil.getProperty("PdfHtml.file.upload.folder")+userid+htmlName;
			 p.runConverter(htmlPath, "下拉框管理", response); 
		 }
		 if("ElevatorTransferCaseRegister".equals(flag)){
			 htmlPath=PropertiesUtil.getProperty("PdfHtml.file.upload.folder")+userid+htmlName;
			 p.runConverter(htmlPath, "安装维保交接电梯情况", response); 
		 }
		 p.deleHtmlFile(htmlPath);
	}

	
	public void init() throws ServletException {
		// Put your code here
	}

}
