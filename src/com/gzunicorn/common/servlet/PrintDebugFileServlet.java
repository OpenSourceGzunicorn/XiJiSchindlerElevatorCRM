package com.gzunicorn.common.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONObject;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.pdfprint.BarCodePrint;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferDebugFileinfo;
import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;
import com.gzunicorn.hibernate.infomanager.handoverelevatorspecialregister.HandoverElevatorSpecialRegister;
import com.gzunicorn.struts.action.xjsgg.SendURLFile;

/**
 * 下载合同交接资料调试单
 */
public class PrintDebugFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PrintDebugFileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println(">>>>doGet");
		this.toPrintDebugFileRecord(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println(">>>>doPost");
		this.toPrintDebugFileRecord(request, response);
		//http://10.10.0.5:8080/XJSCRM/PrintDebugFileServlet?id=2&filetype=2
		//http://10.10.0.5:8080/XJSCRM/PrintDebugFileServlet?id=2&filetype=1
	}
	
	/**
	 * 下载合同交接资料调试单
	 */
	private void toPrintDebugFileRecord(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		BaseDataImpl bd = new BaseDataImpl();
		
		Session hs = null;	
		String id = request.getParameter("id");
		String filetype = request.getParameter("filetype");
		System.out.println(">>>>开始下载调试单 id="+id+";filetype="+filetype);
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		if (id != null && !id.equals("")) {			
			try {
				hs = HibernateUtil.getSession();
				
				String filepath="";
				String filename="";
				String fileid="";
				if("1".equals(filetype)){
					//文件保存路径
					String folder="ContractTransferDebugFileinfo.file.upload.folder";
					folder = PropertiesUtil.getProperty(folder).trim();
					
					//派工上传的调试单
					String hql="from ContractTransferDebugFileinfo where fileSid='"+id.trim()+"'";
					List fileList=hs.createQuery(hql).list();
					//System.out.println(">>>"+hql);
					if(fileList!=null && fileList.size()>0){
						ContractTransferDebugFileinfo ctfdf=(ContractTransferDebugFileinfo)fileList.get(0);

						filename=ctfdf.getOldFileName();
						filepath=folder+ctfdf.getFilePath()+ctfdf.getNewFileName();
					}
					
					//开始下载文件,本地下载
					if(!"".equals(filepath)){
						// path是指欲下载的文件的路径。
			            File file = new File(filepath);
			            
			            //以流的形式下载文件。
			            bis = new BufferedInputStream(new FileInputStream(filepath));
			            byte[] buffer = new byte[bis.available()];
			            bis.read(buffer);
			            bis.close();
			            
			            //清空response
			            response.reset();
			            
			            //设置response的Header
			            response.setContentType("application/octet-stream");
			            //response.setContentType("application/x-msdownload");
			            response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(filename, "utf-8"));
			            response.addHeader("Content-Length", "" + file.length());
			    		
			            bos = new BufferedOutputStream(response.getOutputStream());
			            bos.write(buffer);
			            bos.flush();
			            bos.close();
					}
					
				}else{
					//其他系统同步的调试单
					String sql="select a.FileSid,a.OldFileName,a.FileId from DebugSheetFileInfo a where a.fileSid='"+id.trim()+"'";
					List fileList2=hs.createSQLQuery(sql).list();
					//System.out.println(">>>"+sql);
					if(fileList2!=null && fileList2.size()>0){
						Object[] objs=(Object[])fileList2.get(0);

						filename=objs[1].toString();
						fileid=objs[2].toString();
					}
					
					//开始下载文件,远程下载
					if(!"".equals(fileid)){
						//判断文件是否存在
						boolean isstr=SendURLFile.isFileExist(fileid);
						if(isstr){
							//下载远程文件
							SendURLFile.loadFile(fileid, response, filename);
						}
					}
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally { 
				try {
					//if (fos != null) {fos.close();}
					if (bos != null) {bos.close();}
					//if (fis != null) {fis.close();}
					if (bis != null) {bis.close();}
					
					if(hs!=null){
	            		hs.close();
	            	}
				} catch (Exception e) { 
					e.printStackTrace(); 
				}
			}
			
		}
	}

}
