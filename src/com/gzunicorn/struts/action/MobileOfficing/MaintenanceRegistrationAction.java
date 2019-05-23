package com.gzunicorn.struts.action.MobileOfficing;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.file.HandleFile;
import com.gzunicorn.file.HandleFileImpA;
import com.gzunicorn.hibernate.basedata.Fileinfo;
import com.gzunicorn.hibernate.infomanager.handoverelevatorcheckfileinfo.HandoverElevatorCheckFileinfo;
import com.gzunicorn.hibernate.infomanager.handoverelevatorcheckitemregister.HandoverElevatorCheckItemRegister;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplandetail.MaintenanceWorkPlanDetail;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplanfileinfo.MaintenanceWorkPlanFileinfo;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplanmaster.MaintenanceWorkPlanMaster;
import com.gzunicorn.hibernate.mobileofficeplatform.accessoriesrequisition.AccessoriesRequisition;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;


public class MaintenanceRegistrationAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintenanceRegistrationAction.class);

	BaseDataImpl bd = new BaseDataImpl();

	/**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,
				SysRightsUtil.NODE_ID_FORWARD + "maintenanceregistration", null);
		/** **********结束用户权限过滤*********** */

		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			ActionForward forward = super.execute(mapping, form, request,
					response);
			return forward;
		}

	}

	/**
	 * Method toSearchRecord execute, Search record
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("navigator.location", " 保养计划登记>> 查询列表");		
			ActionForward forward = null;
			
			HttpSession session = request.getSession();
			ServeTableForm tableForm = (ServeTableForm) form;
			String action = tableForm.getAction();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
				HTMLTableCache cache = new HTMLTableCache(session, "maintenanceRegistrationList");
				DefaultHTMLTable table = new DefaultHTMLTable();
				table.setMapping("fMaintenanceRegistration");
				table.setLength(SysConfig.HTML_TABLE_LENGTH);
				cache.updateTable(table);
				table.setSortColumn("mwpd.singleno");
				table.setIsAscending(true);
				cache.updateTable(table);
				if (action.equals(ServeTableForm.NAVIGATE)
						|| action.equals(ServeTableForm.SORT)) {
					cache.loadForm(tableForm);
				} else if (action.equals("Submit")) {
					cache.loadForm(tableForm);
				} else {
					table.setFrom(0);
				}
				cache.saveForm(tableForm);
				
				String singleno = tableForm.getProperty("singleno");//保养单号
				String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号
				String sdate = tableForm.getProperty("sdate");//开始日期
				String edate = tableForm.getProperty("edate");//结束日期
				String handleStatus = tableForm.getProperty("handleStatus");//处理状态
				
				if(handleStatus==null||"".equals(handleStatus)){
					handleStatus="Y";
					tableForm.setProperty("handleStatus", handleStatus);
				}
				if(sdate==null||"".equals(sdate)){
					sdate=CommonUtil.getNowTime("yyyy-MM-dd");
					tableForm.setProperty("sdate", sdate);
				}
				if(edate==null||"".equals(edate)){
					edate=CommonUtil.getNowTime("yyyy-MM-dd");
					tableForm.setProperty("edate", sdate);
				}
				
				
				
				Session hs = null;
				Query query = null;
				String order="";
				try {

					hs = HibernateUtil.getSession();
					String[] colNames = {						
							"mwpd.numno as numno",
							"mwpd.singleno as singleno",
							"mcd.elevatorNo as elevatorNo",
							"si.storagename as storagename",
							"mcd.projectName as projectName",
							"mwpd.maintType as maintType",
							"mwpd.releasedTime as releasedTime",
							"mwpd.receivingTime as receivingTime",
							"mwpd.isTransfer as isTransfer",
							"mwpd.handleStatus as handleStatus",
							"mwpd.maintDate as maintDate"
							
					};
					
					String sql = "select "+StringUtils.join(colNames, ",")+" from MaintenanceWorkPlanDetail mwpd,"
							+ "MaintenanceWorkPlanMaster mwpm,MaintContractDetail mcd,Storageid si "
							+ "where si.storageid=mcd.assignedMainStation "
							+ " and mwpd.billno=mwpm.billno "
							+ " and mwpm.rowid=mcd.rowid "
							+ " and mwpm.checkflag='Y' "
							+ " and mwpd.maintPersonnel='"+userInfo.getUserID()+"'";
					if (singleno != null && !singleno.equals("")) {
						sql += " and mwpd.singleno like '%"+singleno.trim()+"%'";
					}
					if (elevatorNo != null && !elevatorNo.equals("")) {
						sql += " and mcd.elevatorNo like '%"+elevatorNo.trim()+"%'";
					}
					if (sdate != null && !sdate.equals("")) {
						sql += " and mwpd.maintDate >='"+sdate.trim()+"'";
					}
					if (edate != null && !edate.equals("")) {
						sql += " and mwpd.maintDate <= '"+edate.trim()+"'";
					}
					if (handleStatus != null && !handleStatus.equals("")) {
						if(handleStatus.equals("Y")){
							sql += " and isnull(mwpd.handleStatus,'') !='3' ";
						}else if(handleStatus.equals("N"))
						{
							sql += " and isnull(mwpd.handleStatus,'') ='3' ";
						}
					}
					
					if (table.getIsAscending()) {	
        				sql+=" order by " + table.getSortColumn() + " asc ";
        			} else {
        				sql+=" order by " + table.getSortColumn() + " desc ";
        			}
					
					//System.out.println(sql);
					query = hs.createSQLQuery(sql);
					table.setVolume(query.list().size());// 查询得出数据记录数;

					// 得出上一页的最后一条记录数号;
					query.setFirstResult(table.getFrom()); // pagefirst
					query.setMaxResults(table.getLength());

					cache.check(table);

					List list = query.list();
					List maintenanceRegistrationList = new ArrayList();
					if(list!=null&&list.size()>0)
					{   
						for(int i=0;i<list.size();i++){
						Object[] object=(Object[]) list.get(i);
						HashMap map= new HashMap();
						for(int j=0;j<colNames.length;j++)
						{
							map.put(colNames[j].split(" as ")[1].trim(), object[j]);

						}
						map.put("receivingPerson", bd.getName(hs, "Loginuser", "username", "userid", (String) map.get("receivingPerson")));	
						maintenanceRegistrationList.add(map);
						}
					}
					table.addAll(maintenanceRegistrationList);
					session.setAttribute("maintenanceRegistrationList", table);
					
				} catch (DataStoreException e) {
					e.printStackTrace();
				} catch (HibernateException e1) {

					e1.printStackTrace();
				} finally {
					try {
						hs.close();
					} catch (HibernateException hex) {
						log.error(hex.getMessage());
						DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
					}
				}
				
				forward = mapping.findForward("maintenanceRegistrationList");
			return forward;
		}

	/**
	 * Get the navigation description from the properties file by navigation
	 * key;
	 * 
	 * @param request
	 * @param navigation
	 */

	private void setNavigation(HttpServletRequest request, String navigation) {
		Locale locale = this.getLocale(request);
		MessageResources messages = getResources(request);
		request.setAttribute("navigator.location",
				messages.getMessage(locale, navigation));
	}
	/**
	 * 点击查看的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location"," 保养计划登记>> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		String id = request.getParameter("id");	

		Session hs = null;
		MaintenanceWorkPlanDetail mwpd=null;
		HashMap map=new HashMap();
		try{
			hs = HibernateUtil.getSession();
			
			String sql="select mwpd,mcm.maintContractNo,mcd.projectName,si.storagename,lu.username,lu.phone,mcm.billNo,mcd.elevatorNo,mwpm.billno,mcd.maintAddress "
					+ "from  MaintenanceWorkPlanDetail mwpd,MaintenanceWorkPlanMaster mwpm,MaintContractDetail mcd,MaintContractMaster mcm,Loginuser lu,Storageid si "
					+ "where mcd.billNo=mcm.billNo "
					+ " and si.storageid=mcd.assignedMainStation "
					+ " and lu.userid=mwpd.maintPersonnel "
					+ " and mcd.rowid=mwpm.rowid "
					+ " and mwpd.maintenanceWorkPlanMaster.billno=mwpm.billno "
					+ " and mwpd.numno ="+id;
	        List list = hs.createQuery(sql).list();
	        
	       if(list!=null&&list.size()>0)
	       {
	    	   Object[] objects=(Object[]) list.get(0);
	    	   mwpd=(MaintenanceWorkPlanDetail) objects[0];
	    	   String sMaintEndTime=togetAuditCircu(hs,id,(String)objects[8]);
	    	   mwpd.setByAuditOperid(bd.getName(hs, "Loginuser", "username", "userid", mwpd.getByAuditOperid()));
	    	   map.put("sMaintEndTime", sMaintEndTime);
	    	   map.put("maintContractNo", (String)objects[1]);
	    	   map.put("projectName", (String)objects[2]);
	    	   map.put("storagename", (String)objects[3]);
	    	   map.put("username", (String)objects[4]);
	    	   map.put("phone", (String)objects[5]);
	    	   map.put("billno", (String)objects[6]);
	    	   map.put("elevatorNo", (String)objects[7]);
	    	   map.put("maintAddress", (String)objects[9]);
	    	   
	    	   
	    	   if(mwpd.getReceivingPerson()!=null&&!"".equals(mwpd.getReceivingPerson())){
	    		   mwpd.setR1(bd.getName(hs, "Loginuser", "username", "userid", mwpd.getReceivingPerson()));
	    	   }
	    	   
	    	   String hql ="from  MaintenanceWorkPlanFileinfo mwpf where mwpf.singleno="+mwpd.getSingleno();			
			    List fileSidList=hs.createQuery(hql).list();
			    if(fileSidList!=null&&fileSidList.size()>0)
				{
					request.setAttribute("fileSidList", fileSidList);
				}	
	       }	
	        request.setAttribute("mapBean", map);
			request.setAttribute("maintenanceWorkPlanDetailBean", mwpd);
			
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("display", "yes");
		forward = mapping.findForward("maintenanceRegistrationDisplay");
		return forward;
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
		
		String localPath="";
		String oldname="";
		
		String folder = request.getParameter("folder");		//文件夹
		if(null == folder || "".equals(folder)){
			folder ="MaintenanceWorkPlan.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		
		try {
			hs = HibernateUtil.getSession();
			String sqlfile="select a from MaintenanceWorkPlanFileinfo a where a.fileSid='"+filesid+"'";
			List list2=hs.createQuery(sqlfile).list();
            
			if(list2!=null && list2.size()>0){
				MaintenanceWorkPlanFileinfo fp=(MaintenanceWorkPlanFileinfo)list2.get(0);
				String newName=fp.getNewFileName();
				oldname=fp.getOldFileName();
				String root=folder;//上传目录
				localPath = root+"/"+fp.getFilePath()+"/"+newName;
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
	 * 跳转到修改页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws DataStoreException {
		
		request.setAttribute("navigator.location","保养计划登记 >> 处理");	
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		if(id==null||id.equals(""))
		{
			id=(String) request.getAttribute("numno");
		}
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		MaintenanceWorkPlanDetail mwpd=null;
		HashMap map=new HashMap();
		try{
			hs = HibernateUtil.getSession();
			String sql="select mwpd,mcm.maintContractNo,mcd.projectName,si.storagename,lu.username,lu.phone,mcm.billNo,mcd.elevatorNo,mwpm.billno,mcd.assignedMainStation,mcd.maintAddress "
					+ "from  MaintenanceWorkPlanDetail mwpd,MaintenanceWorkPlanMaster mwpm,MaintContractDetail mcd,MaintContractMaster mcm,Loginuser lu,Storageid si "
					+ "where mcd.billNo=mcm.billNo "
					+ " and si.storageid=mcd.assignedMainStation "
					+ " and lu.userid=mwpd.maintPersonnel "
					+ " and mcd.rowid=mwpm.rowid "
					+ " and mwpd.maintenanceWorkPlanMaster.billno=mwpm.billno "
					+ " and mwpd.numno ="+id;
			List list = hs.createQuery(sql).list();
	        
	       if(list!=null&&list.size()>0)
	       {
	    	   Object[] objects=(Object[]) list.get(0);
	    	   mwpd=(MaintenanceWorkPlanDetail) objects[0]; 	    	   
	    	   String sMaintEndTime=togetAuditCircu(hs,id,(String)objects[8]);
	    	   String str=mwpd.getIsInvoice();
	    	   if(str==null||str.equals(""))
	    	   {
	    		   mwpd.setIsInvoice("N");
	    	   }
	    	   map.put("sMaintEndTime", sMaintEndTime);
	    	   map.put("maintContractNo", (String)objects[1]);
	    	   map.put("projectName", (String)objects[2]);
	    	   map.put("storagename", (String)objects[3]);
	    	   map.put("username", (String)objects[4]);
	    	   map.put("phone", (String)objects[5]);
	    	   map.put("billno", (String)objects[6]);
	    	   map.put("elevatorNo", (String)objects[7]);
	    	   map.put("mainStation", (String)objects[9]);
	    	   map.put("maintAddress", (String)objects[10]);
	    	   if(mwpd.getReceivingPerson()!=null&&!"".equals(mwpd.getReceivingPerson())){
	    		   mwpd.setR1(bd.getName(hs, "Loginuser", "username", "userid", mwpd.getReceivingPerson()));
	    	   }
	    	   String hql ="from  MaintenanceWorkPlanFileinfo mwpf where mwpf.singleno="+mwpd.getSingleno();			
			    List fileSidList=hs.createQuery(hql).list();
			    if(fileSidList!=null&&fileSidList.size()>0)
				{
					request.setAttribute("fileSidList", fileSidList);
				}	
			    String btn1=null;
			       String btn2=null;
			       if(mwpd.getHandleStatus()!=null&& !mwpd.getHandleStatus().equals("")){
		          	 if(mwpd.getHandleStatus().equals("1")||mwpd.getHandleStatus()=="1"){
		          		btn1="到 场";
		     	        btn2=null;
		     	        request.setAttribute("maintStartTime", CommonUtil.getNowTime());
		          	 }else if(mwpd.getHandleStatus().equals("2")||mwpd.getHandleStatus()=="2"){
		          		btn1="完 工";
		     	        btn2=null;
		     	        request.setAttribute("maintEndTime", CommonUtil.getNowTime());
		          	 }else if(mwpd.getHandleStatus().equals("3")||mwpd.getHandleStatus()=="3"){
			          	btn1=null;
			     	    btn2=null;
			         }else{
		          		btn1="接 收";
		     	        btn2="转 派";
		     	       request.setAttribute("receivingTime", CommonUtil.getNowTime());
		          	 }
			       }else{
		          		btn1="接 收";
		     	        btn2="转 派";
		     	       request.setAttribute("receivingTime", CommonUtil.getNowTime());
		     	       
		     	       mwpd.setHandleStatus("0");
		          	 }
			        request.setAttribute("btn1",btn1);
			        request.setAttribute("btn2",btn2);
			        request.setAttribute("mapBean", map);
					request.setAttribute("maintenanceWorkPlanDetailBean", mwpd);
	       }
	       
		} catch (HibernateException e1) {
			e1.printStackTrace();
	     } finally {
			try {
				hs.close();
		 } catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil
						.print(hex, "HibernateUtil Hibernate Session ");
			}
	    }	
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}		
		
		forward = mapping.findForward("maintenanceRegistrationModify");
		
		return forward;
	}

	

	/**
	 * 点击修改的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toUpdateRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;
		DynaActionForm dform = (DynaActionForm) form;
		toUpdate(form,request,response,errors);// 新增或修改记录
		String btn =request.getParameter("btn");
		try {
			if(btn.equals("0")){
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.succeed"));
				forward = mapping.findForward("returnList");
			}else{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.succeed"));
				forward = mapping.findForward("returnModify");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}


	/**
	 * 保存数据方法
	 * @param form
	 * @param request
	 * @return ActionErrors
	 */
	public void toUpdate(ActionForm form, HttpServletRequest request,HttpServletResponse response,ActionErrors errors) {
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);	
		String numno =request.getParameter("numno");
		request.setAttribute("numno", numno);
		String btn =request.getParameter("btn");
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if(numno!=null&&!numno.equals(""))
			{
				MaintenanceWorkPlanDetail mwpd=(MaintenanceWorkPlanDetail) hs.get(MaintenanceWorkPlanDetail.class, Integer.valueOf(numno));
				if(mwpd!=null)
				{
					if(btn.equals("1")){
						String receivingTime =request.getParameter("receivingTime");
						String singleno=CommonUtil.getNewSingleno(hs, userInfo.getUserID());//生成单号
						mwpd.setSingleno(singleno);
						mwpd.setReceivingTime(receivingTime); 
						mwpd.setHandleStatus("1");
					}else if(btn.equals("0")){
						String isTransfer =request.getParameter("isTransfer");
						String receivingPerson =request.getParameter("receivingPerson");
						String receivingPhone =request.getParameter("receivingPhone");
						if(isTransfer=="N"){
							mwpd.setIsTransfer(isTransfer);
						}else
						{
							mwpd.setIsTransfer(isTransfer);
							mwpd.setReceivingPerson(receivingPerson);
							mwpd.setReceivingPhone(receivingPhone);
							mwpd.setTransferDate(CommonUtil.getNowTime());
							mwpd.setMaintPersonnel(receivingPerson);
							mwpd.setHandleStatus("0");
						}
					}else if(btn.equals("2")){
						String maintStartTime =request.getParameter("maintStartTime");
						String maintStartAddres =request.getParameter("maintStartAddres");
						mwpd.setMaintStartTime(maintStartTime);
						mwpd.setMaintStartAddres(maintStartAddres);
						mwpd.setHandleStatus("2");
					}else if(btn.equals("3")){
						List list=savePicter(dform, request, response, "MaintenanceWorkPlan.file.upload.folder", mwpd.getSingleno());
						boolean b=false;
						if(list!=null&&list.size()>0){
						b=savePicterTodb(request, list, mwpd.getSingleno());
						}else{
							b=true;
						}
						if(b){
						String maintEndTime =request.getParameter("maintEndTime");
						String maintEndAddres =request.getParameter("maintEndAddres");
						mwpd.setMaintEndTime(maintEndTime);
						mwpd.setMaintEndAddres(maintEndAddres);
						mwpd.setHandleStatus("3");
						}
					}
					hs.update(mwpd);
				}
			}
			tx.commit();
		} catch (Exception e1) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			try {
				tx.rollback();
			} catch (HibernateException e2) {
				log.error(e2.getMessage());
				DebugUtil.print(e2, "Hibernate Transaction rollback error!");
			}
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Insert error!");
		} finally {
			try {
				hs.close();
				String isreturn = request.getParameter("isreturn");
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}
		
	 }

	
	//获取上一次保养时间和判断审批结果
	private String togetAuditCircu(Session hs,String id,
			String str) {
		 String sMaintEndTime="";
		String sql="select mwpd.maintEndTime from MaintenanceWorkPlanDetail mwpd where mwpd.numno=(select numno-1 from MaintenanceWorkPlanDetail where numno='"+id+"' and billno='"+str+"' and numno !=(select MIN(numno)from MaintenanceWorkPlanDetail where billno='"+str+"'))";
		List list=hs.createSQLQuery(sql).list();
		if(list!=null&&list.size()>0)
	    {
			sMaintEndTime=(String) list.get(0);
			if(sMaintEndTime!=null&&!"".equals(sMaintEndTime)){
			sMaintEndTime=sMaintEndTime.substring(0, 10);
			}
	    }
	    return sMaintEndTime;
	}	
	
	/**
	 * 上传保存附件
	 * @param form
	 * @param request
	 * @param response
	 * @param folder
	 * @param billno
	 * @return
	 */
	public List savePicter(ActionForm form, HttpServletRequest request,
			HttpServletResponse response, String folder, String singleno) {
		List returnList = new ArrayList();
		folder = PropertiesUtil.getProperty(folder).trim();//
		if(null == folder || "".equals(folder)){
			folder ="MaintenanceWorkPlan.file.upload.folder";
		}
		DynaActionForm dform = (DynaActionForm) form;
		FormFile formFile = null;
		Fileinfo file = null;
		if (form.getMultipartRequestHandler() != null) {
			Hashtable hash = form.getMultipartRequestHandler().getFileElements();
			if (hash != null) {
				HandleFile hf = new HandleFileImpA();
				for(Iterator it = hash.keySet().iterator(); it.hasNext();){
					String key=(String)it.next();
					formFile=(FormFile)hash.get(key);
					Map map = new HashMap();
					if(formFile!=null){
						try {
							if(!formFile.getFileName().equals("")){
								String newFileName="MaintenanceWorkPlanDetail"+"_"+singleno+"_"+key+"_"+formFile.getFileName();
								map.put("oldfilename", formFile.getFileName());
								map.put("filepath", CommonUtil.getNowTime("yyyy-MM-dd")+"/");
								map.put("fileSize", new Double(formFile.getFileSize()));
								map.put("fileformat",formFile.getContentType());
								map.put("newFileName", newFileName);
								//保存文件到系统
								hf.createFile(formFile.getInputStream(), folder+"/"+CommonUtil.getNowTime("yyyy-MM-dd")+"/", newFileName);
								returnList.add(map);
							}else{
								continue;
							}
							
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}
			}
		}
		return returnList;
	}
		
	/**
	 * 保存附件信息到数据库
	 * @param hs
	 * @param request
	 * @param fileInfoList
	 * @param mtcId
	 * @param trsId
	 * @param seiid
	 * @param billno
	 * @return
	 */
	public boolean savePicterTodb(HttpServletRequest request,List fileInfoList,String singleno){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;
		boolean saveFlag = true;
		if(null != fileInfoList && fileInfoList.size()>0 && singleno!=null && !"".equals(singleno)){
			String sql="";
			try {
				hs=HibernateUtil.getSession();
				tx=hs.beginTransaction();
				int len = fileInfoList.size();
				for(int i = 0 ; i < len ; i++){
					Map map = (Map)fileInfoList.get(i);
					String newFilename=(String) map.get("newFileName");
					MaintenanceWorkPlanFileinfo fileInfo=new MaintenanceWorkPlanFileinfo();
					fileInfo.setOldFileName((String)map.get("oldfilename"));
					fileInfo.setNewFileName((String)map.get("newFileName"));
					fileInfo.setSingleno(singleno);
					fileInfo.setFileSize(Double.valueOf(map.get("fileSize").toString()));
					fileInfo.setFilePath((String)map.get("filepath"));
					fileInfo.setFileFormat((String)map.get("fileformat"));
					fileInfo.setUploadDate(CommonUtil.getNowTime());
					fileInfo.setUploader(userInfo.getUserID());
					hs.save(fileInfo);
				}
				tx.commit();
			} catch (Exception e) {
				e.printStackTrace();
				saveFlag = false;
				try {
					tx.rollback();
				} catch (HibernateException e2) {
					log.error(e2.getMessage());
				}
			}finally{
				if(hs!=null){
					hs.close();
				}
			}
		}
		return saveFlag;
	}
	
	/**
	 * 跳转至配件申请
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toAccessoriesRequisition(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.setAttribute("navigator.location", " 保养计划登记>> 配件申请");	
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ActionErrors errors = new ActionErrors();				
		DynaActionForm dform = (DynaActionForm) form;
		String numno=request.getParameter("numno"); 
		if(null == numno || "".equals(numno)){
			numno =(String) request.getAttribute("numno"); 
		}
		String singleno=request.getParameter("singleno");
		Session hs = null;
		try {
			    hs = HibernateUtil.getSession();
			    request.setAttribute("action", "maintenanceRegistrationAction");
			    request.setAttribute("id", numno);
				HashMap map =new HashMap();
			    map.put("singleNo", singleno);
			    map.put("operId", userInfo.getUserName());
			    map.put("operDate", CommonUtil.getNowTime());
			    map.put("maintDivision", bd.getName(hs, "Company", "ComFullName", "ComID", userInfo.getComID()));
			    map.put("maintStation",  bd.getName(hs, "StorageID", "StorageName", "StorageID", userInfo.getStorageId()));
			    request.setAttribute("mapBean", map);	
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(hs!=null){
				hs.close();
			}
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return mapping.findForward("toAccessoriesRequisition");
		
	}
	
	/**
	 * 保存配件申请单
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toAccessoriesRequisitionRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;
		DynaActionForm dform = (DynaActionForm) form;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		String numno=request.getParameter("id");
		String operDate=request.getParameter("operDate");
		String oldNo=request.getParameter("oldNo");
		String singleNo=request.getParameter("singleNo");
		Session hs = null;
		 Transaction tx = null;
		FormFile formFile = null;
		Fileinfo file = null;
		try {
			   hs = HibernateUtil.getSession();
			  
				tx=hs.beginTransaction();
			   String year1=CommonUtil.getToday().substring(2,4);
			   String billno1 = CommonUtil.getBillno(year1,"AccessoriesRequisition", 1)[0];	
			   String folder="AccessoriesRequisition.file.upload.folder";
			   folder = PropertiesUtil.getProperty(folder).trim();//
			 //  Hashtable hash = form.getMultipartRequestHandler().getFileElements();
			   formFile=(FormFile) dform.get("newfile");
			   HandleFile hf = new HandleFileImpA();
			   String newFileName=billno1+"_"+formFile.getFileName();			//保存文件到系统
			   hf.createFile(formFile.getInputStream(), folder,newFileName);
			   
			   AccessoriesRequisition ar=new AccessoriesRequisition();
			   ar.setAppNo(billno1);
			   ar.setSingleNo(singleNo);
			   ar.setOldNo(oldNo);
			   ar.setOldImage(newFileName);
			   ar.setOperId(userInfo.getUserID());
			   ar.setOperDate(operDate);
			   ar.setMaintDivision(userInfo.getComID());
			   ar.setMaintStation(userInfo.getStorageId());
			   hs.save(ar);
			   tx.commit();
               request.setAttribute("numno", numno);
		} catch (Exception e) {
			tx.rollback();
			e.printStackTrace();
		}finally{
			if(hs!=null){
				hs.close();
			}
		}
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("accessoriesRequisition.success"));
				forward = mapping.findForward("returnModify");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("accessoriesRequisition.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				forward = mapping.findForward("returnAccessoriesRequisition");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	
}
