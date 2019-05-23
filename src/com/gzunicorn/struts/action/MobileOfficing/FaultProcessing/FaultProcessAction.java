package com.gzunicorn.struts.action.MobileOfficing.FaultProcessing;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
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
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.mobileofficeplatform.faultrepairentrymaster.FaultRepairEntryMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;


	/**
 * MyEclipse Struts Creation date: 08-09-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/elevatorflagAction" name="elevatorflagForm" scope="request"
 *                validate="true"
 */
public class FaultProcessAction extends DispatchAction {

	Log log = LogFactory.getLog(FaultProcessAction.class);
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
	
	/************开始用户权限过滤************/
	SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "FaultProcessing",null);
	/************结束用户权限过滤************/

	String name = request.getParameter("method");
	if (name == null || name.equals("")) {
		name = "toSearchRecord";
		return dispatchMethod(mapping, form, request, response, name);
	} else {
		ActionForward forward = super.execute(mapping, form, request,
				response);
		////System.out.println(forward.getName());
		return forward;
	}

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
	request.setAttribute("navigator.location", messages.getMessage(locale,
			navigation));
}

/**
 * Method toUpdateRecord execute,Update data to database and return list
 * page or modifiy page
 * 
 * @param mapping
 * @param form
 * @param request
 * @param response
 * @return ActionForward
 */
public ActionForward toUpdateRecord(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

	HttpSession httpsession=request.getSession();
	ViewLoginUserInfo userinfo=(ViewLoginUserInfo)httpsession.getAttribute(SysConfig.LOGIN_USER_INFO);

	String operid = userinfo.getUserID();
	String username = userinfo.getUserName();
	String todaydate = "";
	try {
		todaydate = CommonUtil.getToday()+" "+CommonUtil.getTodayTime();
	} catch (ParseException e4) {
		log.error(e4.getMessage());
		DebugUtil.print(e4, "Get today date error!");
	}
	
	DynaActionForm dform = (DynaActionForm) form;
	ActionErrors errors = new ActionErrors();
	Session hs = null;
	Transaction tx = null;
	try {
		hs = HibernateUtil.getSession();
		tx = hs.beginTransaction();
		FaultRepairEntryMaster frem = (FaultRepairEntryMaster) hs.get(FaultRepairEntryMaster.class, (String) dform.get("appNo"));
        frem.setIsProcess("Y");
		frem.setDealId(operid);
		frem.setDealDate(todaydate);
		frem.setRem((String)dform.get("rem"));
		hs.save(frem);
		tx.commit();
	} catch (Exception e2) {
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","处理失败！"));
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
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
			"update.success"));
			forward = mapping.findForward("returnList");
		} else {
			// return modify page
			if (errors.isEmpty()) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"update.success"));
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
 * Method toPrepareUpdateRecord execute,prepare data for update page
 * 
 * @param mapping
 * @param form
 * @param request
 * @param response
 * @return ActionForward
 */

public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
		ActionForm form, HttpServletRequest request,
		HttpServletResponse response) {
	DynaActionForm dform = (DynaActionForm) form;
	ActionErrors errors = new ActionErrors();
	String naigation = new String();
	request.setAttribute("navigator.location", "故障报修处理 >> 处理");
	ActionForward forward = null;
	String id = null;
	FaultRepairEntryMaster frem=null;
    List list2=new ArrayList();
	if (dform.get("isreturn") != null
			&& ((String) dform.get("isreturn")).equals("N")) {
		id = (String) dform.get("appNo");
	} else {
		id = (String) dform.get("id");
	}

	Session hs = null;
	if (id != null) {
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			try {
				hs = HibernateUtil.getSession();
                String hql="select a from FaultRepairEntryMaster a where a.appNo='"+id+"'";
				List list1 = hs.createQuery(hql).list();
				if (list1 != null && list1.size() > 0) {
					 frem = (FaultRepairEntryMaster) list1.get(0);	
					 frem.setOperId(bd.getName("Loginuser", "username", "userid", frem.getOperId()));
					 frem.setDealId(bd.getName("Loginuser", "username", "userid", frem.getDealId()));
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"display.recordnotfounterror"));
				}
            request.setAttribute("list2", frem);
			} catch (DataStoreException e) {
				e.printStackTrace();
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
		}
		forward = mapping.findForward("toFaultProcessModify");
	}

	if (!errors.isEmpty()) {
		saveErrors(request, errors);
	}
	return forward;
}

/**
 * Method toDeleteRecord execute, Delete record where facid = id
 * 
 * @param mapping
 * @param form
 * @param request
 * @param response
 * @return ActionForward
 */

	public ActionForward toDeleteRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			FaultRepairEntryMaster frem = (FaultRepairEntryMaster) hs.get(FaultRepairEntryMaster.class, (String) dform.get("id"));
			if(frem != null)
			{
			 hs.delete(frem);
			 errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
				"delete.succeed"));
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate LocalSpeQueAppMaster Delete error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate LocalSpeQueAppMaster Delete error!");

		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		ActionForward forward = null;
		try {
			forward = mapping.findForward("returnList");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return forward;
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
public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {

	String naigation = new String();
	request.setAttribute("navigator.location", "故障报修处理 >> 查询列表");
	ActionForward forward = null;
	HttpSession session = request.getSession();
	ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
	ServeTableForm tableForm = (ServeTableForm) form;
	
	String action = tableForm.getAction();
    List list1=new ArrayList();
	if (tableForm.getProperty("genReport") != null
			&& !tableForm.getProperty("genReport").equals("")) {
		try {

			response = toExcelRecord(form, request, response);
			forward = mapping.findForward("exportExcel");
		} catch (Exception e) {
			e.printStackTrace();
		}

	} else {
		HTMLTableCache cache = new HTMLTableCache(session, "faultList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fFaultProcess");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		cache.updateTable(table);
		table.setSortColumn("a.isTiring desc,a.operDate");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String repairName = tableForm.getProperty("repairName");
		String repairPhone = tableForm.getProperty("repairPhone");
		String projectName = tableForm.getProperty("projectName");
		String projectAddress = tableForm.getProperty("projectAddress");
		String isTiring = tableForm.getProperty("isTiring");
		String isProcess = tableForm.getProperty("isProcess");
		if(isProcess==null || isProcess.trim().equals("")){
			tableForm.setProperty("isProcess","N");
			isProcess="N";
		}
		Session hs = null;
		List grcidlist=new ArrayList();
		try {

			hs = HibernateUtil.getSession();
			
			String hql="select a from FaultRepairEntryMaster a where 1=1";		
			
			if (repairName != null && !repairName.equals("")) {
				hql=hql+" and a.repairName like '%" + repairName.trim() + "%'";
			}
			if (repairPhone != null && !repairPhone.equals("")) {
				hql=hql+" and a.repairPhone like '%" + repairPhone.trim() + "%'";
			}
			if (projectName != null && !projectName.equals("")) {
				hql=hql+" and a.projectName like '%" + projectName.trim() + "%'";
			}
			if (projectAddress != null && !projectAddress.equals("")) {
				hql=hql+" and a.projectAddress like '%" + projectAddress.trim() + "%'";
			}
			if (isTiring != null && !isTiring.equals("")) {
				hql=hql+" and isnull(a.isTiring,'N') like '%" + isTiring.trim() + "%'";
			}
			if (isProcess != null && !isProcess.equals("")) {
				hql=hql+" and isnull(a.isProcess,'N') like '%"+isProcess.trim()+"%'";
			}
			if (table.getIsAscending()) {	
				hql=hql+" order by " + table.getSortColumn() + " asc ";
			} else {
				hql=hql+" order by " + table.getSortColumn() + " desc ";
			}

			Query query=hs.createQuery(hql);
            int count=query.list().size();
			table.setVolume(count);// 查询得出数据总记录数;		
			// 得出上一页的最后一条记录数号;
            query.setFirstResult(table.getFrom());// pagefirst
            query.setMaxResults(table.getLength());//页长
            
			List fremList=query.list();
			for(int i=0;i<fremList.size();i++){
				FaultRepairEntryMaster frem=(FaultRepairEntryMaster)fremList.get(i);
				HashMap hm=new HashMap();
				hm.put("appNo", frem.getAppNo());
				hm.put("repairName", frem.getRepairName());
				hm.put("repairPhone", frem.getRepairPhone());
				hm.put("projectName", frem.getProjectName());
				hm.put("projectAddress", frem.getProjectAddress());
				hm.put("isTiring", frem.getIsTiring());
				hm.put("operId", bd.getName("Loginuser", "username", "userid", frem.getOperId()));
				hm.put("operDate", frem.getOperDate());
				hm.put("isProcess", frem.getIsProcess());
				if("Y".equals(frem.getIsTiring()) && ("".equals(frem.getIsProcess()) || "N".equals(frem.getIsProcess()) || frem.getIsProcess()==null)){
				hm.put("isTiring2", "Y");	
				}else{
				hm.put("isTiring2", "N");
				}
				list1.add(hm);
			}	
			cache.check(table);
			table.addAll(list1);
			session.setAttribute("faultList", table);
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
         
		forward = mapping.findForward("FaultProcessList");
	}
	return forward;
}

/**
 * Method toSearchRecord execute, to Excel Record 列表查询导出Excel
 * 
 * @param mapping
 * @param form
 * @param request
 * @param response
 * @return ActionForward
 * @throws IOException
 */

public HttpServletResponse toExcelRecord(ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws IOException {

	String naigation = new String();
	ActionForward forward = null;
	HttpSession session = request.getSession();
	ServeTableForm tableForm = (ServeTableForm) form;
	String action = tableForm.getAction();

	String repairName = tableForm.getProperty("repairName");
	String repairPhone = tableForm.getProperty("repairPhone");
	String projectName = tableForm.getProperty("projectName");
	String projectAddress = tableForm.getProperty("projectAddress");
	String isTiring = tableForm.getProperty("isTiring");
	String isProcess = tableForm.getProperty("isProcess");
//	HSSFWorkbook wb = new HSSFWorkbook(); 2003word
	Session hs = null;
	XSSFWorkbook wb = new XSSFWorkbook();//2007word
	try {
		hs = HibernateUtil.getSession();

		String hql="select a from FaultRepairEntryMaster a where 1=1";		
		
		if (repairName != null && !repairName.equals("")) {
			hql=hql+" and a.repairName like '%" + repairName.trim() + "%'";
		}
		if (repairPhone != null && !repairPhone.equals("")) {
			hql=hql+" and a.repairPhone like '%" + repairPhone.trim() + "%'";
		}
		if (projectName != null && !projectName.equals("")) {
			hql=hql+" and a.projectName like '%" + projectName.trim() + "%'";
		}
		if (projectAddress != null && !projectAddress.equals("")) {
			hql=hql+" and a.projectAddress like '%" + projectAddress.trim() + "%'";
		}
		if (isTiring != null && !isTiring.equals("")) {
			hql=hql+" and isnull(a.isTiring,'N') like '%" + isTiring.trim() + "%'";
		}else{
			tableForm.setProperty("isTiring", "Y");
			hql=hql+" and isnull(a.isTiring,'N') like '%Y%'";
		}
		if (isProcess != null && !isProcess.equals("")) {
			hql=hql+" and isnull(a.isProcess,'N') like '%"+isProcess.trim()+"%'";
		}
		
		////System.out.println(hql);
		Query query=hs.createQuery(hql);

		List roleList = query.list();

		XSSFSheet sheet = wb.createSheet("故障报修处理");

		Locale locale = this.getLocale(request);
		MessageResources messages = getResources(request);

		if (roleList != null && !roleList.isEmpty()) {
			int l = roleList.size();
			XSSFRow row0 = sheet.createRow( 0);
			XSSFCell cell0 = null;
			cell0 = row0.createCell((short) 0);
			cell0.setCellValue( "报修流水号");
			cell0 = row0.createCell((short) 1);
			cell0.setCellValue("报修人");
			cell0 = row0.createCell((short) 2);
			cell0.setCellValue("报修人电话");
			cell0 = row0.createCell((short) 3);
			cell0.setCellValue( "报修描述");
			cell0 = row0.createCell((short) 4);
			cell0.setCellValue( "项目名称");
			cell0 = row0.createCell((short) 5);
			cell0.setCellValue( "项目地址");
			cell0 = row0.createCell((short) 6);
			cell0.setCellValue( "是否困人");
			cell0 = row0.createCell((short) 7);
			cell0.setCellValue( "录入人");
			cell0 = row0.createCell((short) 8);
			cell0.setCellValue( "录入日期");
			cell0 = row0.createCell((short) 9);
			cell0.setCellValue( "是否处理");
			cell0 = row0.createCell((short) 10);
			cell0.setCellValue("处理人");
			cell0 = row0.createCell((short) 11);
			cell0.setCellValue("处理日期");
			cell0 = row0.createCell((short) 12);
			cell0.setCellValue( "备注");
	

			for (int i = 0; i < l; i++) {
				FaultRepairEntryMaster obj = (FaultRepairEntryMaster) roleList.get(i);
				XSSFRow row = sheet.createRow( i + 1);
				XSSFCell cell = null;
				cell = row.createCell((short) 0);
				cell.setCellValue(obj.getAppNo());
				cell = row.createCell((short) 1);
				cell.setCellValue(obj.getRepairName());
				cell = row.createCell((short) 2);
				cell.setCellValue(obj.getRepairPhone());
				cell = row.createCell((short) 3);
				cell.setCellValue(obj.getRepairDesc());
				cell = row.createCell((short) 4);
				cell.setCellValue(obj.getProjectName());
				cell = row.createCell((short) 5);
				cell.setCellValue(obj.getProjectAddress());
				cell = row.createCell((short) 6);
				if("Y".equals(obj.getIsTiring())){
				cell.setCellValue("是");
				}else{
				cell.setCellValue("否");
				}
				cell = row.createCell((short) 7);
				cell.setCellValue(bd.getName("Loginuser", "username", "userid", obj.getOperId()));
				cell = row.createCell((short) 8);
				cell.setCellValue(obj.getOperDate());
				cell = row.createCell((short) 9);
				if("Y".equals(obj.getIsProcess())){
					cell.setCellValue("是");
					}else{
					cell.setCellValue("否");
					}
				cell = row.createCell((short) 10);
				cell.setCellValue(bd.getName("Loginuser", "username", "userid", obj.getDealId()));
				cell = row.createCell((short) 11);
				cell.setCellValue(obj.getDealDate());
				cell = row.createCell((short) 12);
				cell.setCellValue(obj.getRem());			
			}

		}
		try {
		} catch (Exception e) {

		}
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
	response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("故障报修处理", "utf-8") + ".xlsx");
	wb.write(response.getOutputStream());
	
	return response;
}

}

