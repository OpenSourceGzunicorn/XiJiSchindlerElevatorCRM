package com.gzunicorn.struts.action.MobileOfficing.accessoriesrequisition;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.mobileofficeplatform.accessoriesrequisition.AccessoriesRequisition;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class AccessoriesRequisitionReturnAction extends DispatchAction {

	Log log = LogFactory.getLog(AccessoriesRequisitionReturnAction.class);
	
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
	 * 配件申请表（旧件退回）处理
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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "accessoriesrequisitionreturn", null);
		/** **********结束用户权限过滤*********** */

		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			ActionForward forward = super.execute(mapping, form, request,response);
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

		request.setAttribute("navigator.location", "配件申请单(旧件退回)处理 >> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		HTMLTableCache cache = new HTMLTableCache(session,"accessoriesRequisitionReturnList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fAccessoriesRequisitionReturn");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		cache.updateTable(table);
		table.setSortColumn("operDate");
		table.setIsAscending(false);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);
		String singleNo = tableForm.getProperty("singleNo");
		String maintDivision = tableForm.getProperty("maintDivision");
		String operId = tableForm.getProperty("operId");
		String sdate1 = tableForm.getProperty("sdate1");
		String edate1 = tableForm.getProperty("edate1");
		String elevatorNo = tableForm.getProperty("elevatorNo");
		
		String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
		String day1=DateUtil.getDate(day, "MM", -3);//当前日期月份之前3个月  。
		if(sdate1==null || sdate1.trim().equals("")){
			sdate1=day1;
			tableForm.setProperty("sdate1",sdate1);
		}
		if(edate1==null || edate1.trim().equals("")){
			edate1=day;
			tableForm.setProperty("edate1",edate1);
		}

		if (!userInfo.getComID().equals("00")) {
			maintDivision = userInfo.getComID();
		}

		Session hs = null;
		Query query = null;
		String order = "";
		try {
			hs = HibernateUtil.getSession();
			
			//1 维保负责人审核，2 配件库管理员审核，3 维保工确认，4 旧件退回，5 已关闭
			String sql = "select ar,lu.username,si.storagename,c.comfullname "
					+ "from AccessoriesRequisition ar,Loginuser lu,Company c ,Storageid si "
					+ "where c.comid=ar.maintDivision and si.storageid=maintStation "
					+ "and ar.operId=lu.userid and ar.handleStatus='4' ";
			if (singleNo != null && !singleNo.equals("")) {
				sql += " and ar.singleNo like '%" + singleNo.trim() + "%'";
			}
			if (elevatorNo != null && !elevatorNo.equals("")) {
				sql += " and ar.elevatorNo like '%" + elevatorNo.trim() + "%'";
			}
			if (maintDivision != null && !maintDivision.equals("")) {
				sql += " and ar.maintDivision like '" + maintDivision.trim()+ "'";
			}

			if (sdate1 != null && !sdate1.equals("")) {
				sql += " and ar.operDate >= '" + sdate1.trim() + " 00:00:00"
						+ "'";
			}
			if (edate1 != null && !edate1.equals("")) {
				sql += " and ar.operDate <= '" + edate1.trim() + " 23:59:59"
						+ "'";
			}
			if (operId != null && !operId.equals("")) {
				sql += " and (lu.userid like '" + operId.trim()
						+ "%' or lu.username like '%" + operId.trim() + "%')";
			}
			if (table.getIsAscending()) {
				order += " order by " + table.getSortColumn();
			} else {
				order += " order by " + table.getSortColumn() + " desc";
			}

			query = hs.createQuery(sql + order);
			table.setVolume(query.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List list = query.list();
			List accessoriesRequisitionList = new ArrayList();

			if (list != null && list.size() > 0) {
				int j = list.size();
				for (int i = 0; i < j; i++) {
					Object[] ts = (Object[]) list.get(i);
					Object[] objects = (Object[]) list.get(i);
					AccessoriesRequisition ar=(AccessoriesRequisition) objects[0];
                    ar.setMaintDivision((String) objects[3]);
                    ar.setMaintStation((String) objects[2]);
                    ar.setOperId((String) objects[1]);
				    ar.setPersonInCharge(bd.getName(hs, "Loginuser", "username", "userid", ar.getPersonInCharge()));
				    ar.setWarehouseManager(bd.getName(hs, "Loginuser", "username", "userid", ar.getWarehouseManager()));
				    
				    String hstatus=ar.getHandleStatus();
            	  	String hstatusname="";
            	  	//处理状态 【1 维保负责人审核，2 配件库管理员审核，3 维保工确认，4 旧件退回，5 已关闭】
            	  	if(hstatus.trim().equals("1")){
            	  		hstatusname="维保负责人审核";
            	  	}else if(hstatus.trim().equals("2")){
            	  		hstatusname="配件库管理员审核";
            	  	}else if(hstatus.trim().equals("3")){
            	  		hstatusname="维保工确认";
            	  	}else if(hstatus.trim().equals("4")){
            	  		hstatusname="旧件退回";
            	  	}else if(hstatus.trim().equals("5")){
            	  		hstatusname="已关闭";
            	  	}else if(hstatus.trim().equals("6")){
            	  		hstatusname="终止";
            	  	}
					ar.setHandleStatus(hstatusname);
					
                    accessoriesRequisitionList.add(ar);
				}
			}

			table.addAll(accessoriesRequisitionList);

			request.setAttribute("CompanyList",Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
			session.setAttribute("accessoriesRequisitionReturnList", table);

		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (HibernateException e1) {

			e1.printStackTrace();
		} finally {
			try {
				hs.close();
				// HibernateSessionFactory.closeSession();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		forward = mapping.findForward("accessoriesRequisitionReturnList");

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
	 * 下载附件
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDownLoadFiles(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String filename = request.getParameter("filename");
		String folder = request.getParameter("folder");
		if (folder == null || "".equals(folder)) {
			folder = "AccessoriesRequisition.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		filename=URLDecoder.decode(filename, "utf-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;

		response.setContentType("application/x-msdownload");
		response.setHeader("Content-disposition", "attachment;filename="
				+ URLEncoder.encode(filename, "utf-8"));

		fis = new FileInputStream(folder + "/" + filename);
		bis = new BufferedInputStream(fis);
		fos = response.getOutputStream();
		bos = new BufferedOutputStream(fos);

		int bytesRead = 0;
		byte[] buffer = new byte[5 * 1024];
		while ((bytesRead = bis.read(buffer)) != -1) {
			bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
			bos.flush();
		}
		if (fos != null) {
			fos.close();
		}
		if (bos != null) {
			bos.close();
		}
		if (fis != null) {
			fis.close();
		}
		if (bis != null) {
			bis.close();
		}
		return null;
	}

	/**
	 * 跳转到处理页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","配件申请单(旧件退回)处理 >> 处理");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		ActionForward forward = null;
		String id = null;

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("appNo");
		} else
        {
			id = (String) dform.get("id");
		}
		
		Session hs = null;
		AccessoriesRequisition ar=null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs
						.createQuery("from AccessoriesRequisition ar where ar.appNo = :appNo");
				query.setString("appNo", id);
				List list = query.list();
                if(list!=null&&list.size()>0)
                {
                	ar=(AccessoriesRequisition) list.get(0);
                	
                	//备件名称及型号/备注,在页面可以换行。
					String r5str=ar.getR5();
					r5str=r5str.replaceAll(";", ";<br/>");
					r5str=r5str.replaceAll("；", ";<br/>");
					ar.setR5(r5str);
					
                	String username=bd.getName(hs, "LoginUser", "UserName", "UserID",ar.getOperId());
                	String phone=bd.getName(hs, "LoginUser", "phone", "UserID",ar.getOperId());
                	ar.setOperId(username+" "+phone);
                	
                	ar.setMaintDivision(bd.getName(hs, "Company", "ComFullName", "ComID", ar.getMaintDivision()));
                	ar.setMaintStation(bd.getName(hs, "StorageID", "StorageName", "StorageID", ar.getMaintStation()));
                	
                	ar.setJjOperId(userInfo.getUserName());
                	ar.setJjOperDate(CommonUtil.getNowTime());
                	
                	//项目名称及楼栋号
                	String elerem=bd.getName(hs, "ElevatorCoordinateLocation", "Rem", "ElevatorNo",ar.getElevatorNo());
                	request.setAttribute("elerem", elerem);

                	//维保负责人
                	ar.setPersonInCharge(bd.getName(hs, "LoginUser", "UserName", "UserID", ar.getPersonInCharge()));
                	if(ar.getIsAgree()!=null && !ar.getIsAgree().equals("")){
						 if(ar.getIsAgree().equals("Y") || ar.getIsAgree()=="Y"){
							ar.setIsAgree("同意");
						 }else if(ar.getIsAgree().equals("N") || ar.getIsAgree()=="N"){
							ar.setIsAgree("不同意");
						 }
					}
					if(ar.getIsCharges()!=null && !ar.getIsCharges().equals("")){
						 if(ar.getIsCharges().equals("Y") || ar.getIsCharges()=="Y"){
							ar.setIsCharges("收费");
						 }else if(ar.getIsCharges().equals("N") || ar.getIsCharges()=="N"){
							ar.setIsCharges("免费");
						 }
					}
					//配件库管理员
					ar.setWarehouseManager(bd.getName(hs, "LoginUser", "UserName", "UserID", ar.getWarehouseManager()));
                	if(ar.getWmIsAgree()!=null && !ar.getWmIsAgree().equals("")){
						 if(ar.getWmIsAgree().equals("Y") || ar.getWmIsAgree()=="Y"){
							ar.setWmIsAgree("同意");
						 }else if(ar.getWmIsAgree().equals("N") || ar.getWmIsAgree()=="N"){
							ar.setWmIsAgree("不同意");
						 }
					}
					if(ar.getWmIsCharges()!=null && !ar.getWmIsCharges().equals("")){
						 if(ar.getWmIsCharges().equals("Y") || ar.getWmIsCharges()=="Y"){
							ar.setWmIsCharges("收费");
						 }else if(ar.getWmIsCharges().equals("N") || ar.getWmIsCharges()=="N"){
							ar.setWmIsCharges("免费");
						 }
					}
					if(ar.getWmPayment()!=null && !ar.getWmPayment().equals("")){
						 if(ar.getWmPayment().equals("1") || ar.getWmPayment()=="1"){
							ar.setWmPayment("分库直接领取");
						 }else if(ar.getWmPayment().equals("2") || ar.getWmPayment()=="2"){
							ar.setWmPayment("总库调拨");
						 }else if(ar.getWmPayment().equals("3") || ar.getWmPayment()=="3"){
            	  			ar.setWmPayment("外购");
            	  		}
						 
					}
					if(ar.getIsConfirm()!=null&& !ar.getIsConfirm().equals("")){
						 if(ar.getIsConfirm().equals("Y") || ar.getIsConfirm()=="Y"){
							 ar.setIsConfirm("已更换");
						 }else{
							 ar.setIsConfirm("入备件库");
						 }
					}
					if(ar.getR1()!=null&& !ar.getR1().equals("")){
						 if(ar.getR1().equals("Y") || ar.getR1()=="Y"){
							ar.setR1("有");
						 }else{
							ar.setR1("无");
						 }
					}
					if(ar.getR3()!=null&& !ar.getR3().equals("")){
						 if(ar.getR3().equals("Y") || ar.getR3()=="Y"){
							ar.setR3("有");
						 }else{
							ar.setR3("无");
						 }
					}
					
					//有偿/免保 [FREE - 免保,PAID - 有偿],
            	  	HashMap hmap=new HashMap();
        			String sqlk="select md.ElevatorNo,mm.MainMode,mm.ContractEDate,mm.BillNo "
        					+ "from MaintContractDetail md ,MaintContractMaster mm "
        					+ "where mm.BillNo=md.BillNo and mm.contractStatus in('XB','ZB') "
        					+ "and md.ElevatorNo='"+ar.getElevatorNo()+"'";
        			List krelist=hs.createSQLQuery(sqlk).list();
        			if(krelist!=null && krelist.size()>0){
        				Object[] obj=(Object[])krelist.get(0);
            			hmap.put("mainmode", (String)obj[1]);//有偿/免保
            			hmap.put("contractedate", (String)obj[2]);//合同到期日期
            			hmap.put("billno", (String)obj[3]);
        			}else{
            			hmap.put("mainmode", "");//有偿/免保
            			hmap.put("contractedate", "");//合同到期日期
            			hmap.put("billno", "");
        			}
        			request.setAttribute("contracthmap", hmap);//合同到期日期
        			
        			//旧件图片
        			List olgimglist=bd.getWbFileInfoList(hs,"AccessoriesRequisition_OldImage",ar.getAppNo());
        			request.setAttribute("olgimglist", olgimglist);
        			//新件图片
        			List newimglist=bd.getWbFileInfoList(hs,"AccessoriesRequisition_NewImage",ar.getAppNo());
        			request.setAttribute("newimglist", newimglist);
        			//发票信息
        			List invoiceImagelist=bd.getWbFileInfoList(hs,"AccessoriesRequisition_invoiceImage",ar.getAppNo());
        			request.setAttribute("invoiceImagelist", invoiceImagelist);
                	
                }
				if (ar == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}			
				request.setAttribute("accessoriesRequisitionBean", ar);
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			forward = mapping.findForward("accessoriesRequisitionReturnModify");
     }

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	
	/**
	 * 处理
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

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String appNo = (String) dform.get("appNo");
		String jjReturn = (String) dform.get("jjReturn");
		String jjResult = (String) dform.get("jjResult");
		
		AccessoriesRequisition ar=null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			ar = (AccessoriesRequisition) hs.get(AccessoriesRequisition.class, appNo);
			if (jjReturn!= null&& !jjReturn.trim().equals("")) {
				ar.setJjReturn(jjReturn);
				ar.setJjResult(jjResult);
				ar.setJjOperId(userInfo.getUserID());
				ar.setJjOperDate(CommonUtil.getNowTime());
				
				//1 维保负责人审核，2 配件库管理员审核，3 维保工确认，4 旧件退回，5 已关闭，6 终止
				ar.setHandleStatus("5");//关闭

                hs.update(ar);
			}
			
			tx.commit();
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");
			
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("technologySupport.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("technologySupport.update.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("technologySupport.update.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				
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
	
	
	
}
