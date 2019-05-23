package com.gzunicorn.struts.action.MobileOfficing.accessoriesrequisition;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
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

import com.gzunicorn.common.dao.mssql.DataOperation;
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

public class AccessoriesRequisitionStoremanAction extends DispatchAction {

	Log log = LogFactory.getLog(AccessoriesRequisitionStoremanAction.class);
	
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
				SysRightsUtil.NODE_ID_FORWARD + "accessoriesrequisitionstoreman", null);
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

		request.setAttribute("navigator.location", "配件申请表（仓库管理员）处理 >> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		HTMLTableCache cache = new HTMLTableCache(session,"accessoriesRequisitionStoremanList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fAccessoriesRequisitionStoreman");
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
		String ischul = tableForm.getProperty("ischul");
		String elevatorNo = tableForm.getProperty("elevatorNo");
		
		String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
		String day1=DateUtil.getDate(day, "MM", -3);//当前日期月份之前3个月 。
		if(sdate1==null || sdate1.trim().equals("")){
			sdate1=day1;
			tableForm.setProperty("sdate1",sdate1);
		}
		if(edate1==null || edate1.trim().equals("")){
			edate1=day;
			tableForm.setProperty("edate1",edate1);
		}
		if(ischul==null || ischul.trim().equals("")){
			ischul="N";
			tableForm.setProperty("ischul",ischul);
		}
		

		if (!userInfo.getComID().equals("00")) {
			maintDivision = userInfo.getComID();
		}
		
		String wmuserid=userInfo.getUserID();
		if(userInfo.getRoleID().equals("A01")){//管理员查询全部
			wmuserid="%";
		}

		Session hs = null;
		Query query = null;
		String order = "";
		try {
			hs = HibernateUtil.getSession();
			//根据维保站对应配件审批人，与登录人对比出数据。
			String sql = "select ar.appNo,ar.singleNo,ar.elevatorNo,isnull(ar.handleStatus,'') as handleStatus,ar.oldNo,ar.newNo,"
					+ "isnull(ar.personInCharge,'') as personInCharge,isnull(ar.warehouseManager,'') as warehouseManager,ar.operDate,"
					+ "lu.username,c.comfullname,si.storagename "
					+ "from AccessoriesRequisition ar,Loginuser lu,Company c ,Storageid si "
					+ "where c.comid=ar.maintDivision and si.storageid=maintStation "
					+ "and ar.operId=lu.userid "
					+ "and ar.appno in(select ar.AppNo from WarehouseManager a,AccessoriesRequisition ar "
					+ "where (a.MaintStation=ar.MaintStation "
					+ "or a.MaintStation in(select ParentStorageID from StorageID where StorageID=ar.MaintStation)) "
					+ "and a.IsCharges=ar.IsCharges and a.OperId like '"+wmuserid+"')";
					//+ "and ar.wmuserId like '"+wmuserid+"'";
					
			if(ischul!=null && ischul.equals("Y")){
				sql += " and isnull(ar.warehouseManager,'')<>'' ";
			}else{
				sql += " and ar.handleStatus='2' ";
			}
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
				order += " order by ar." + table.getSortColumn();
			} else {
				order += " order by ar." + table.getSortColumn() + " desc";
			}
			
			//System.out.println("==="+sql + order);
			
			query = hs.createSQLQuery(sql + order);
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
					Object[] objects = (Object[]) list.get(i);
					
					HashMap hmap=new HashMap();
					hmap.put("appNo", objects[0]);
					hmap.put("singleNo", objects[1]);
					hmap.put("elevatorNo", objects[2]);
					hmap.put("oldNo", objects[4]);
					hmap.put("newNo", objects[5]);
					hmap.put("personInCharge", bd.getName(hs, "Loginuser", "username", "userid", objects[6].toString()));
					hmap.put("warehouseManager", bd.getName(hs, "Loginuser", "username", "userid", objects[7].toString()));
					hmap.put("operDate", objects[8]);
					hmap.put("operId", objects[9]);
					hmap.put("maintDivision", objects[10]);
					hmap.put("maintStation", objects[11]);
				    
				    String hstatus=objects[3].toString();
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
					hmap.put("handleStatus", hstatusname);
					
                    accessoriesRequisitionList.add(hmap);
				}
			}

			table.addAll(accessoriesRequisitionList);

			request.setAttribute("CompanyList",Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
			session.setAttribute("accessoriesRequisitionStoremanList", table);

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
		forward = mapping.findForward("accessoriesRequisitionStoremanList");

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
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {


		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		request.setAttribute("navigator.location", "配件申请表（仓库管理员）处理 >> 查看");

		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		AccessoriesRequisition a = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from AccessoriesRequisition ar where ar.appNo = :appNo");
				query.setString("appNo", id);
				List list = query.list();
				if (list != null && list.size() > 0) {
					a = (AccessoriesRequisition) list.get(0);

					a.setMaintDivision(bd.getName(hs,"Company", "ComFullName", "ComID",a.getMaintDivision()));
					a.setMaintStation(bd.getName(hs,"StorageID", "StorageName", "StorageID",a.getMaintStation()));
					String username=bd.getName(hs, "LoginUser", "UserName", "UserID",a.getOperId());
                	String phone=bd.getName(hs, "LoginUser", "phone", "UserID",a.getOperId());
                	a.setOperId(username+" "+phone);
                	
                	//项目名称及楼栋号
                	String elerem=bd.getName(hs, "ElevatorCoordinateLocation", "Rem", "ElevatorNo",a.getElevatorNo());
                	request.setAttribute("elerem", elerem);
					
					//维保负责人
					a.setPersonInCharge(bd.getName(hs,"LoginUser", "UserName", "UserID",a.getPersonInCharge()));
					if(a.getIsAgree()!=null&& !a.getIsAgree().equals("")){
						 if(a.getIsAgree().equals("Y")||a.getIsAgree()=="Y"){
							a.setIsAgree("同意");
						 }else{
							a.setIsAgree("不同意");
						 }
					}
					if(a.getIsCharges()!=null&& !a.getIsCharges().equals("")){
						 if(a.getIsCharges().equals("Y") ||a.getIsCharges()=="Y"){
							a.setIsCharges("收费");
						 }else{
							a.setIsCharges("免费");
						 }
					}
					if(a.getR1()!=null&& !a.getR1().equals("")){
						 if(a.getR1().equals("Y") || a.getR1()=="Y"){
							a.setR1("有");
						 }else{
							a.setR1("无");
						 }
					}
					if(a.getR3()!=null&& !a.getR3().equals("")){
						 if(a.getR3().equals("Y") || a.getR3()=="Y"){
							a.setR3("有");
						 }else{
							a.setR3("无");
						 }
					}
					
					//配件库管理员
					a.setWarehouseManager(bd.getName(hs,"LoginUser", "UserName", "UserID",a.getWarehouseManager()));
					if(a.getWmIsAgree()!=null && !a.getWmIsAgree().trim().equals("")){
						a.setWmIsAgree(a.getWmIsAgree().trim().equals("N") ? "不同意" : "同意");
            	  	}
					if(a.getWmIsCharges()!=null && !a.getWmIsCharges().trim().equals("")){
            	  		String wmischargesname="";
            	  		if(a.getWmIsCharges().trim().equals("N")){
            	  			wmischargesname="免费";
            	  		}else if(a.getWmIsCharges().trim().equals("Y")){
            	  			wmischargesname="收费";
            	  		}
            	  		a.setWmIsCharges(wmischargesname);
            	  	}
					//1: 分库直接领取,2: 总库调拨
            	  	if(a.getWmPayment()!=null && !a.getWmPayment().trim().equals("")){
            	  		String wmpaymentname="";
            	  		if(a.getWmPayment().trim().equals("1")){
            	  			wmpaymentname="分库直接领取";
            	  		}else if(a.getWmPayment().trim().equals("2")){
            	  			wmpaymentname="总库调拨";
            	  		}else if(a.getWmPayment().trim().equals("3")){
            	  			wmpaymentname="外购";
            	  		}
            	  		a.setWmPayment(wmpaymentname);
            	  	}
            	  	
            	  	//旧件退回
					a.setJjOperId(bd.getName(hs,"LoginUser", "UserName", "UserID",a.getJjOperId()));
            	  	if(a.getJjReturn()!=null && !a.getJjReturn().trim().equals("")){
            	  		String jjname="";
            	  		if(a.getJjReturn().trim().equals("Y")){
            	  			jjname="已退回";
            	  		}else if(a.getJjReturn().trim().equals("N")){
            	  			jjname="未退回";
            	  		}
            	  		a.setJjReturn(jjname);
            	  	}
            	  	if(a.getIsConfirm()!=null&& !a.getIsConfirm().equals("")){
						 if(a.getIsConfirm().equals("Y") || a.getIsConfirm()=="Y"){
							 a.setIsConfirm("已更换");
						 }else{
							 a.setIsConfirm("入备件库");
						 }
					}
					
            	  	//有偿/免保 [FREE - 免保,PAID - 有偿],
            	  	HashMap hmap=new HashMap();
        			String sqlk="select md.ElevatorNo,mm.MainMode,mm.ContractEDate,mm.BillNo "
        					+ "from MaintContractDetail md ,MaintContractMaster mm "
        					+ "where mm.BillNo=md.BillNo and mm.contractStatus in('XB','ZB') "
        					+ "and md.ElevatorNo='"+a.getElevatorNo()+"'";
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
        			List olgimglist=bd.getWbFileInfoList(hs,"AccessoriesRequisition_OldImage",a.getAppNo());
        			request.setAttribute("olgimglist", olgimglist);
        			//新件图片
        			List newimglist=bd.getWbFileInfoList(hs,"AccessoriesRequisition_NewImage",a.getAppNo());
        			request.setAttribute("newimglist", newimglist);
        			//发票信息
        			List invoiceImagelist=bd.getWbFileInfoList(hs,"AccessoriesRequisition_invoiceImage",a.getAppNo());
        			request.setAttribute("invoiceImagelist", invoiceImagelist);
        			
				}

				if (a == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
			} catch (DataStoreException e) {
				e.printStackTrace();
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
			request.setAttribute("display", "yes");
			request.setAttribute("accessoriesRequisitionBean",a);
			forward = mapping.findForward("accessoriesRequisitionStoremanDisplay");

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	
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
	 * ajax 级联 分部与维保站
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public void toStorageIDList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Session hs = null;
		String comid = request.getParameter("comid");
		response.setHeader("Content-Type", "text/html; charset=GBK");
		List list2 = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='GBK'?>");
		sb.append("<root>");
		try {
			hs = HibernateUtil.getSession();
			if (comid != null && !"".equals(comid)) {
				String hql = "select a from Storageid a,Company b where a.comid = b.comid and a.comid="
						+ comid + " and a.storagetype=1 and a.parentstorageid = '0'";
				List list = hs.createQuery(hql).list();
				if (list != null && list.size() > 0) {
					sb.append("<rows>");
					for (int i = 0; i < list.size(); i++) {
						Storageid sid = (Storageid) list.get(i);
						sb.append(
								"<cols name='" + sid.getStoragename()
										+ "' value='" + sid.getStorageid()
										+ "'>").append("</cols>");
					}
					sb.append("</rows>");

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			hs.close();
		}
		sb.append("</root>");

		response.setCharacterEncoding("gbk");
		response.setContentType("text/xml;charset=gbk");
		response.getWriter().write(sb.toString());
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
		
		request.setAttribute("navigator.location","配件申请表（仓库管理员）处理 >> 处理");
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
				Query query = hs.createQuery("from AccessoriesRequisition ar where ar.appNo = :appNo");
				query.setString("appNo", id);
				List list = query.list();
                if(list!=null&&list.size()>0)
                {
                	ar=(AccessoriesRequisition) list.get(0);
                	
					String r5str=ar.getR5();
					ar.setR5(r5str);
					//备件名称及型号/备注,在页面可以换行。
					r5str=r5str.replaceAll(";", ";<br/>");
					r5str=r5str.replaceAll("；", ";<br/>");
					request.setAttribute("r5str", r5str);
					
                	String username=bd.getName(hs, "LoginUser", "UserName", "UserID",ar.getOperId());
                	String phone=bd.getName(hs, "LoginUser", "phone", "UserID",ar.getOperId());
                	ar.setOperId(username+" "+phone);
                	ar.setMaintDivision(bd.getName(hs, "Company", "ComFullName", "ComID", ar.getMaintDivision()));
                	ar.setMaintStation(bd.getName(hs, "StorageID", "StorageName", "StorageID", ar.getMaintStation()));
                	ar.setWarehouseManager(userInfo.getUserName());
                	ar.setWmdate(CommonUtil.getNowTime());
                	
                	//项目名称及楼栋号
                	String elerem=bd.getName(hs, "ElevatorCoordinateLocation", "Rem", "ElevatorNo",ar.getElevatorNo());
                	request.setAttribute("elerem", elerem);

                	ar.setPersonInCharge(bd.getName(hs, "LoginUser", "UserName", "UserID", ar.getPersonInCharge()));
                	if(ar.getIsAgree()!=null && !ar.getIsAgree().equals("")){
						 if(ar.getIsAgree().equals("Y") || ar.getIsAgree()=="Y"){
							ar.setIsAgree("同意");
						 }else{
							ar.setIsAgree("不同意");
						 }
					}
//					if(ar.getIsCharges()!=null && !ar.getIsCharges().equals("")){
//						 if(ar.getIsCharges().equals("Y") || ar.getIsCharges()=="Y"){
//							ar.setIsCharges("收费");
//						 }else{
//							ar.setIsCharges("免费");
//						 }
//					}
					if(ar.getR1()!=null&& !ar.getR1().equals("")){
						 if(ar.getR1().equals("Y") || ar.getR1()=="Y"){
							ar.setR1("有");
						 }else{
							ar.setR1("无");
						 }
					}
					dform.set("r3", ar.getR3());
					
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
			forward = mapping.findForward("accessoriesRequisitionStoremanModify");
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
		String newNo = (String) dform.get("newNo");
		String wmIsAgree = (String) dform.get("wmIsAgree");
		String wmPayment = (String) dform.get("wmPayment");
//		String isCharges = (String) dform.get("isCharges");//初步判断是否收费
		//String money2 = (String) dform.get("money2");
		String wmRem = (String) dform.get("wmRem");
		String instock = request.getParameter("instock");
//		String r3 = (String) dform.get("r3");
		
		AccessoriesRequisition ar=null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			ar = (AccessoriesRequisition) hs.get(AccessoriesRequisition.class, appNo);
			if (wmIsAgree!= null&& !wmIsAgree.trim().equals("")) {
				ar.setWmIsAgree(wmIsAgree);//仓管是否同意
				ar.setWmRem(wmRem);//WMRem
				ar.setWarehouseManager(userInfo.getUserID());
				ar.setWmdate(CommonUtil.getNowTime());
				
				//1 维保负责人审核，2 配件库管理员审核，3 维保工确认，4 旧件退回，5 已关闭，6 终止
				if(wmIsAgree!=null && wmIsAgree.trim().equals("N")){
					ar.setHandleStatus("6");//不同意终止
				}else{
					ar.setNewNo(newNo);//新件编号
					ar.setWmPayment(wmPayment);//仓管领取方式
					ar.setInstock(instock);//备件库是否有库存
					ar.setHandleStatus("3");//维保工确认
				}
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
	
	/**
	 * 跳转到处理页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toPrepareTrunRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","配件申请表（配件库管理员）处理 >> 转派");
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
				Query query = hs.createQuery("from AccessoriesRequisition ar where ar.appNo = :appNo");
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
                	ar.setWarehouseManager(userInfo.getUserName());
                	ar.setWmdate(CommonUtil.getNowTime());

                	ar.setPersonInCharge(bd.getName(hs, "LoginUser", "UserName", "UserID", ar.getPersonInCharge()));
                	if(ar.getIsAgree()!=null && !ar.getIsAgree().equals("")){
						 if(ar.getIsAgree().equals("Y") || ar.getIsAgree()=="Y"){
							ar.setIsAgree("同意");
						 }else{
							ar.setIsAgree("不同意");
						 }
					}
//					if(ar.getIsCharges()!=null && !ar.getIsCharges().equals("")){
//						 if(ar.getIsCharges().equals("Y") || ar.getIsCharges()=="Y"){
//							ar.setIsCharges("收费");
//						 }else{
//							ar.setIsCharges("免费");
//						 }
//					}
					if(ar.getR1()!=null&& !ar.getR1().equals("")){
						 if(ar.getR1().equals("Y") || ar.getR1()=="Y"){
							ar.setR1("有");
						 }else{
							ar.setR1("无");
						 }
					}
					dform.set("r3", ar.getR3());
					
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
        			
        			//项目名称及楼栋号
                	String elerem=bd.getName(hs, "ElevatorCoordinateLocation", "Rem", "ElevatorNo",ar.getElevatorNo());
                	request.setAttribute("elerem", elerem);
					
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
				
				//查询配件管理员列表
				String sqlc="select distinct b.UserID,b.UserName "
						+ "from WarehouseManager a,loginuser b where a.OperId=b.UserID and b.EnabledFlag='Y'";
				DataOperation op = new DataOperation();
				op.setCon(hs.connection());
				List wmuserIdList = op.queryToList(sqlc);
				request.setAttribute("wmuserIdList", wmuserIdList);
				
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
			forward = mapping.findForward("accessoriesRequisitionStoremanTrun");
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
	public ActionForward toTrunRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String appNo = (String) dform.get("appNo");
		String wmuserId = (String) dform.get("wmuserId");//转派接收人
		
		AccessoriesRequisition ar=null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			ar = (AccessoriesRequisition) hs.get(AccessoriesRequisition.class, appNo);
			if (ar!=null) {
				ar.setWmuserId(wmuserId);
                hs.update(ar);
			}
			
			tx.commit();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","转派成功！"));
		} catch (Exception e1) {
			e1.printStackTrace();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","转派失败！"));
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
		forward = mapping.findForward("returnList");
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	
}
