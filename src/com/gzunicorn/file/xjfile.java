package com.gzunicorn.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.hibernate.basedata.Fileinfo;

public class xjfile extends DispatchAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		String name = request.getParameter("method");
		return dispatchMethod(mapping, form, request, response, name);

	}
	
	/**
	 * 保存上传文件
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public List saveFile(ActionForm form,HttpServletRequest request, HttpServletResponse response,
			String path,String billno){
		
		List returnList = new ArrayList();
		int filenum=0;
		int fileCount=0;

		path =  PropertiesUtil.getProperty(path).trim();//上传目录 

		 FormFile formFile = null;
		 Fileinfo file=null;
		 if (form.getMultipartRequestHandler() != null) {
			 Hashtable hash = form.getMultipartRequestHandler().getFileElements();
			 if (hash != null) {
				 
				 Iterator it = hash.values().iterator();
				 HandleFile hf = new HandleFileImpA();
				 while (it.hasNext()) {				 
					 
					 fileCount++;
					 formFile = (FormFile) it.next();
					 if (formFile != null) {
						 try {
							 String today=DateUtil.getCurDate();
							 String time=DateUtil.getDateTime("yyyyMMddHHmmss");
							 Map map = new HashMap();
							 map.put("title", "");
							 map.put("oldfilename", formFile.getFileName());
							 map.put("newfilename", time+"_"+fileCount+"_"+formFile.getFileName());
							 map.put("filepath", today.substring(0,7)+"/");
							 map.put("filesize", new Double(formFile.getFileSize()));
							 map.put("fileformat",formFile.getContentType());
							 map.put("rem","");

							// 保存文件入文件系统

							hf.createFile(formFile.getInputStream(),path+today.substring(0,7)+"/", time+"_"+fileCount+"_"+formFile.getFileName());
							returnList.add(map);
						}catch (Exception e) {
							e.printStackTrace();
						}
						
					 }
				 }
			 }
		 }
		return returnList;
	}
	
	/**
	 * 保存文件信息到数据库
	 * @param fileInfoList
	 */
	public boolean saveFileInfo(Session hs,List fileInfoList,String tableName,String billno,String userid){
		boolean saveFlag = true;
		 Fileinfo file=null;
		if(null != fileInfoList && !fileInfoList.isEmpty()){
			
			try {
				int len = fileInfoList.size();
				for(int i = 0 ; i < len ; i++){
					Map map = (Map)fileInfoList.get(i);
					 String title =(String) map.get("title");
					 String oldfilename =(String) map.get("oldfilename");
					 String newfilename =(String) map.get("newfilename");
					 String filepath =(String) map.get("filepath");
					 Double filesize =(Double) map.get("filesize");
					 String fileformat =(String) map.get("fileformat");
					 String rem =(String) map.get("rem");
					 
					 
					 file = new Fileinfo();
					 file.setMaterSid(billno);
					 file.setBusTable(tableName);
					 file.setFileTitle(title);
					 file.setOldFileName(oldfilename);
					 file.setNewFileName(newfilename);
					 file.setFilePath(filepath);
					 file.setFileSize(filesize);
					 file.setFileFormat(fileformat);
					 file.setUploadDate(CommonUtil.getToday());
					 file.setUploader(userid);
					 file.setRemarks(rem);
					 
					 hs.save(file);
					 hs.flush();
				}
			} catch (ParseException e) {
				e.printStackTrace();
				saveFlag = false;
			} catch (Exception e) {
				e.printStackTrace();
				saveFlag = false;
			}
		}
		return saveFlag;
	}
	
	/**
	 * 获取已上传附件列表
	 * @param hs
	 * @param MaterSid
	 * @return
	 */
	public List display(Session hs, String MaterSid, String BusTable)
	  {
	    List rt = new ArrayList();
	    Connection con = null;
	    try {
	      con = hs.connection();
	      String sql = "exec SP_Fileinfo_DISPLAY '" + MaterSid + "','" + BusTable + "'";
	      DataOperation op = new DataOperation();
	      op.setCon(con);
	      rt = op.queryToList(sql);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return rt;
	  }
	

	/**
	 * 文件删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toDeleteFileRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		Session hs = null;
		Transaction tx = null;
		String id = request.getParameter("filesid");
		String delsql="";
		List list=null;
		String table=request.getParameter("table");
		String folder = request.getParameter("folder");
		if(null == folder || "".equals(folder)){
			folder ="ServicingContractQuoteMaster.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if(id!=null && id.length()>0){
				
				String sql="select a.* from "+table+" a where a.fileSid='"+id.trim()+"' ";
				
				list=hs.createSQLQuery(sql).list();	//取文件信息
				hs.flush();
				
				delsql="delete from "+table+" where fileSid='"+id.trim()+"'";
				hs.connection().prepareStatement(delsql).execute();
				hs.flush();
				
				HandleFile hf = new HandleFileImpA();
				if(list!=null && !list.isEmpty()){
                Object[] fp=(Object[])list.get(0);
				
				String filepath=String.valueOf(fp[5]);
				String newfilename=String.valueOf(fp[4]);
				String localPath = folder+filepath+newfilename;
				hf.delFile(localPath);//删除磁盘中的文件
				}
			}
				
						
	        response.setContentType("text/xml; charset=UTF-8");
	      
			//创建输出流对象
	        PrintWriter out = response.getWriter();
	        //依据验证结果输出不同的数据信息	       
	        out.println("<response>");  
	        int b=list.size();
			if(b==0){
				out.println("<res>" + "N" + "</res>");
			}
			else{
				
				out.println("<res>" + "Y" + "</res>");
				
			}
			 out.println("</response>");
		     out.close();
		     	
		     tx.commit();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				if(hs!=null){
					hs.close();
				}
			} catch (HibernateException hex) {
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}	
		return null;
	}
	
	/*
	 * 删除附件调用方法
	 * 
	 * 
	 */
	public boolean toDeleteFile(Session hs,String id,String table,String folder) {
		Transaction tx = null;
		String delsql="";
		List list=null;
		
		int FileSids=0;
        if(null == folder || "".equals(folder)){
			folder ="ServicingContractQuoteMaster.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		
		try {
			tx = hs.beginTransaction();
			if(id!=null && id.length()>0){
				List list1=hs.createSQLQuery("select FileSid from "+table+" where MaterSid='"+id+"'").list();
				if(list1!=null && list1.size()>0){
					
				    for(int i=0;i<list1.size();i++){
				    	FileSids=(Integer) list1.get(i);
						String sql="select a.* from "+table+" a where a.fileSid="+FileSids+" ";						
						list=hs.createSQLQuery(sql).list();	//取文件信息
						hs.flush();
						
						delsql="delete from "+table+" where fileSid="+FileSids+"";
						hs.connection().prepareStatement(delsql).execute();
						hs.flush();//删除表数据
						
						HandleFile hf = new HandleFileImpA();
						if(list!=null && !list.isEmpty()){
		                Object[] fp=(Object[])list.get(0);
						
						String filepath=String.valueOf(fp[5]);
						String newfilename=String.valueOf(fp[4]);
						String localPath = folder+filepath+newfilename;
						hf.delFile(localPath);//删除磁盘中的文件
						}
					}
			    }
			}
		} catch (Exception e) {
		e.printStackTrace();
		return false;
		}
		return true;
	}
	
	
	
	/**
	 * 文件下载方法
	 * @param response
	 * @param localPath 本地磁盘文件完整路径 如:(D:/WebProjects/xxxxxx2010年节假日.jpg)
	 * @param loname  文件逻辑名称 如:(2010年节假日.jpg)
	 * @throws IOException
	 */
	public void toDownLoadFiles(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) 
		throws IOException {
		/*
		 * 取得文件:文件id+文件路径+文件名+流 文件id=通过formbean取得 文件路径=通过取得配置文件的方法得到
		 * 文件名称=通过数据库得到 流=io
		 */
		Session hs = null;

		String filesid =request.getParameter("filesid");//流水号
		String folder=request.getParameter("folder");//放入硬盘的路径
		String table=request.getParameter("table");//表名
		String localPath="";
		String oldname="";

		if(null == folder || "".equals(folder)){
			folder ="ServicingContractQuoteMaster.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		
		try {
			hs = HibernateUtil.getSession();
			String sqlfile="select a.* from "+table+" a where a.fileSid='"+filesid+"'";
			List list2=hs.createSQLQuery(sqlfile).list();

			if(list2!=null && list2.size()>0){
				Object[] fp=(Object[])list2.get(0);
				
				String filepath=String.valueOf(fp[5]);
				String newnamefile=String.valueOf(fp[4]);
				oldname=String.valueOf(fp[3]);
				String root=folder;//上传目录
				localPath = root+filepath+newnamefile;
				
			}
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;

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
		if (fos != null) {fos.close();}
		if (bos != null) {bos.close();}
		if (fis != null) {fis.close();}
		if (bis != null) {bis.close();}
		
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {				
				hs.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * 读文件方法
	 * 
	 * @param dir
	 *            文件目录
	 * @param fileName
	 *            文件名称
	 * @return InputSteam 返回文件输入流
	 */
	public InputStream readFile(String fileName)
			throws FileNotFoundException {
//		fileName="smb://administrator:gzunicorn12345678@192.168.1.121/2013-12-25/113112564041.png";
//		String np=fileName.substring(6,fileName.indexOf("@"));
//		fileName=fileName.replace(np+"@","");
//		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",
//		    		np.split(":")[0], np.split(":")[1]);
		    
		SmbFile remoteFile=null;
		InputStream in = null;
		try {
			remoteFile = new SmbFile(fileName);
			
			if(remoteFile.exists()){ 
				in = new SmbFileInputStream(fileName);
	        }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SmbException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return in;
	}
	
}
