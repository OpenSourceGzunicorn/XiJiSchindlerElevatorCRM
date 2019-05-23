package com.gzunicorn.struts.action.hotlinemanagement;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.principal.Principal;
import com.gzunicorn.hibernate.hotlinemanagement.advisorycomplaintsmanage.AdvisoryComplaintsManage;
import com.gzunicorn.hibernate.sysmanager.Getnum;
import com.gzunicorn.hibernate.sysmanager.GetnumKey;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class AdvisoryComplaintsManageAction extends DispatchAction {

	Log log = LogFactory.getLog(AdvisoryComplaintsManageAction.class);

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

		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		String authority="advisorycomplaintsmanage";
		if(name != null && name.contains("Dispose")){
			authority = "advisorycomplaintsdispose";
		}
		if(name != null && name.contains("Audit")){
			authority = "advisorycomplaintsaudit";
		}
		if(name != null && name.contains("Dispatch")){
			authority = "advisorycomplaintsdispatch";
		}
		if(name!=null && name.contains("Reject")){
			authority = "advisorycomplaintsreject";
		}
		
		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,
				SysRightsUtil.NODE_ID_FORWARD + authority, null);
		/** **********结束用户权限过滤*********** */
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

		request.setAttribute("navigator.location", "咨询投诉登记>> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form, request, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport", "");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session,
					"advisoryComplaintsManageList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fAdvisoryComplaintsManage");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			// table.setLength(1);
			cache.updateTable(table);
			table.setSortColumn("processSingleNo");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String processSingleNo = tableForm.getProperty("processSingleNo");
			String messageSource = tableForm.getProperty("messageSource");
			String receivePer = tableForm.getProperty("receivePer");
			String submitType = tableForm.getProperty("submitType");
			String isEntrust = tableForm.getProperty("isEntrust");
			
			Session hs = null;

			try {
				hs = HibernateUtil.getSession();
				
				String hql = "select a.processSingleNo,a.messageSource,l.username,a.receiveDate,a.feedbackPer,a.feedbackTel,a.processType,a.auditType,a.submitType,a.dispatchType,a.isEntrust"+
				" from AdvisoryComplaintsManage a left join Loginuser l on a.receivePer=l.userid where a.receivePer='"+userInfo.getUserID()+"'";
				String condition="";
				if (processSingleNo != null && !processSingleNo.equals("")) {
					/*if(condition==null || "".equals(condition)){
						condition=" where a.processSingleNo like '%" + processSingleNo.trim()
							+ "%'";
					}else{
						condition+=" and a.processSingleNo like '%" + processSingleNo.trim()
							+ "%'";
					}*/
					hql+=" and a.processSingleNo like '%" + processSingleNo.trim()
							+ "%'";
				}
				if (messageSource != null && !messageSource.equals("")) {
					/*if(condition==null || "".equals(condition)){
						condition=" where a.messageSource like '%"
							+ messageSource.trim() + "%'";
					}else{
						condition+=" and a.messageSource like '%"
							+ messageSource.trim() + "%'";
					}*/
					hql+=" and a.messageSource like '%"
							+ messageSource.trim() + "%'";
				}
				if (receivePer != null && !receivePer.equals("")) {
					/*if(condition==null || "".equals(condition)){
						condition=" where l.username like '%" + receivePer.trim()
							+ "%'";
					}else{
						condition+=" and l.username like '%" + receivePer.trim()
							+ "%'";
					}*/
					hql+=" and l.username like '%" + receivePer.trim()
							+ "%'";
				}
				if (submitType != null && !submitType.equals("")) {
					/*if(condition==null || "".equals(condition)){
						condition=" where a.submitType='" + submitType.trim() + "'";
					}else{
						condition+=" and a.submitType='" + submitType.trim() + "'";
					}*/
					hql+=" and a.submitType='" + submitType.trim() + "'";
				}
				if(isEntrust!=null && !isEntrust.equals("")){
					hql+=" and a.isEntrust='"+isEntrust+"'";
				}
				if (table.getIsAscending()) {
					hql += " order by " + table.getSortColumn() + " desc";
				} else {
					hql += " order by " + table.getSortColumn() + " asc";
				}
				Query query = hs.createSQLQuery(hql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				cache.check(table);
				
				List list = hs.createSQLQuery(hql).list();
				List advisoryComplaintsManageList = new ArrayList();
				for (Object object : list) {
					Object[] values = (Object[]) object;
					Map m = new HashMap();
					m.put("processSingleNo", values[0]);
					m.put("messageSource", bd.getName_Sql("Pulldown", "pullname", "pullid", values[1].toString()));
					m.put("receivePer",values[2]);
					m.put("receiveDate", values[3]);
					m.put("feedbackPer", values[4]);
					m.put("feedbackTel", values[5]);
					m.put("processType", values[6]);
					m.put("auditType", values[7]);
					m.put("submitType", values[8]);
					m.put("dispatchType", values[9]);
					m.put("isEntrust", values[10]);
					advisoryComplaintsManageList.add(m);
				}
				
				table.addAll(advisoryComplaintsManageList);
				session.setAttribute("advisoryComplaintsManageList", table);
				request.setAttribute("MessageSourceList", bd.getPullDownList("AdvisoryComplaintsManage_MessageSource"));

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e1) {
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
			forward = mapping.findForward("advisoryComplaintsManageList");
		}
		return forward;
	}

	/**
	 * 维保质量检查登记>>查询列表
	 * advisoryComplaintsDisposeList.jsp
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecordDispose(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "	咨询投诉处理>> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form, request, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport", "");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session,
					"advisoryComplaintsDisposeList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fAdvisoryComplaintsDispose");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			// table.setLength(1);
			cache.updateTable(table);
			table.setSortColumn("processSingleNo");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String processSingleNo = tableForm.getProperty("processSingleNo");
			String messageSource = tableForm.getProperty("messageSource");
			String receivePer = tableForm.getProperty("receivePer");
			String processType = tableForm.getProperty("processType");
			
			Session hs = null;

			try {

				hs = HibernateUtil.getSession();

				String hql = "select a.processSingleNo,a.messageSource,l.username,a.receiveDate,a.feedbackPer,a.feedbackTel,a.processType,a.auditType"+
				" from AdvisoryComplaintsManage a left join Loginuser l on a.receivePer=l.userid"+
				" where a.submitType='Y' and a.processPer='"+userInfo.getUserID()+"'";
				if (processSingleNo != null && !processSingleNo.equals("")) {
					hql += " and a.processSingleNo like '%" + processSingleNo.trim()
							+ "%'";
				}
				if (messageSource != null && !messageSource.equals("")) {
					hql += " and a.messageSource like '%"
							+ messageSource.trim() + "%'";
				}
				if (receivePer != null && !receivePer.equals("")) {
					hql += " and l.username like '%" + receivePer.trim()
							+ "%'";
				}
				if (processType != null && !processType.equals("")) {
					hql += " and a.processType='" + processType.trim() + "'";
				}
				if (table.getIsAscending()) {
					hql += " order by " + table.getSortColumn() + " desc";
				} else {
					hql += " order by " + table.getSortColumn() + " asc";
				}
				Query query = hs.createSQLQuery(hql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				cache.check(table);
				
				List list = hs.createSQLQuery(hql).list();
				List advisoryComplaintsDisposeList = new ArrayList();
				for (Object object : list) {
					Object[] values = (Object[]) object;
					Map m = new HashMap();
					m.put("processSingleNo", values[0]);
					m.put("messageSource",bd.getName_Sql("Pulldown", "pullname", "pullid", values[1].toString()));
					m.put("receivePer",values[2]);
					m.put("receiveDate", values[3]);
					m.put("feedbackPer", values[4]);
					m.put("feedbackTel", values[5]);
					m.put("processType", values[6]);
					m.put("auditType", values[7]);
					advisoryComplaintsDisposeList.add(m);
				}
				table.addAll(advisoryComplaintsDisposeList);
				session.setAttribute("advisoryComplaintsDisposeList", table);
				request.setAttribute("MessageSourceList", bd.getPullDownList("AdvisoryComplaintsManage_MessageSource"));

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e1) {
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
			forward = mapping.findForward("advisoryComplaintsDisposeList");
		}
		return forward;
	}
	
	/**
	 * 维保质量检查登记>>查询列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecordAudit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "咨询投诉分析总结>> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form, request, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport", "");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session,
					"advisoryComplaintsAuditList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fAdvisoryComplaintsAudit");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			// table.setLength(1);
			cache.updateTable(table);
			table.setSortColumn("processSingleNo");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String processSingleNo = tableForm.getProperty("processSingleNo");
			String messageSource = tableForm.getProperty("messageSource");
			String processPer = tableForm.getProperty("processPer");
			String auditType = tableForm.getProperty("auditType");
			
			Session hs = null;

			try {

				hs = HibernateUtil.getSession();

				String hql = "select a.processSingleNo,a.messageSource,l.username,a.processDate,a.feedbackPer,a.feedbackTel,a.auditType,a.isClose"+
				" from AdvisoryComplaintsManage a left join Loginuser l on a.processPer=l.userid"+
				" where a.submitType='Y' and processType='Y'";
				if (processSingleNo != null && !processSingleNo.equals("")) {
					hql += " and a.processSingleNo like '%" + processSingleNo.trim()
							+ "%'";
				}
				if (messageSource != null && !messageSource.equals("")) {
					hql += " and a.messageSource like '%"
							+ messageSource.trim() + "%'";
				}
				if (processPer != null && !processPer.equals("")) {
					hql += " and (l.username like '%" + processPer.trim() + "%' or a.processPer like '%" + processPer.trim() + "%')";
				}
				if (auditType != null && !auditType.equals("")) {
					hql += " and a.auditType='" + auditType.trim() + "'";
				}
				if (table.getIsAscending()) {
					hql += " order by " + table.getSortColumn() + " desc";
				} else {
					hql += " order by " + table.getSortColumn() + " asc";
				}
				Query query = hs.createSQLQuery(hql);
				table.setVolume(query.list().size());// 查询得出数据记录数;
				
				////System.out.println(hql);

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				cache.check(table);
				
				List list = hs.createSQLQuery(hql).list();
				List advisoryComplaintsAuditList = new ArrayList();
				for (Object object : list) {
					Object[] values = (Object[]) object;
					Map m = new HashMap();
					m.put("processSingleNo", values[0]);
					m.put("messageSource", bd.getName_Sql("Pulldown", "pullname", "pullid", values[1].toString()));
					m.put("processPer", values[2]);
					m.put("processDate", values[3]);
					m.put("feedbackPer", values[4]);
					m.put("feedbackTel", values[5]);
					m.put("auditType", values[6]);
					m.put("isClose", values[7]);
					advisoryComplaintsAuditList.add(m);
				}
				table.addAll(advisoryComplaintsAuditList);
				session.setAttribute("advisoryComplaintsAuditList", table);
				request.setAttribute("MessageSourceList", bd.getPullDownList("AdvisoryComplaintsManage_MessageSource"));

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e1) {
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
			forward = mapping.findForward("advisoryComplaintsAuditList");
		}
		return forward;
	}
	
	/**
	 * 咨询投诉驳回>>查询列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecordReject(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "咨询投诉驳回>> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form, request, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport", "");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session,
					"advisoryComplaintsRejectList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fAdvisoryComplaintsReject");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			// table.setLength(1);
			cache.updateTable(table);
			table.setSortColumn("processSingleNo");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String processSingleNo = tableForm.getProperty("processSingleNo");
			String messageSource = tableForm.getProperty("messageSource");
			String processPer = tableForm.getProperty("processPer");
			String isClose=tableForm.getProperty("isClose");
			
			Session hs = null;

			try {

				hs = HibernateUtil.getSession();

				String hql = "select a.processSingleNo,a.messageSource,l.username,a.processDate,a.feedbackPer,a.feedbackTel,a.auditType,a.isClose"+
				" from AdvisoryComplaintsManage a left join Loginuser l on a.processPer=l.userid";
				String condition="";
				if (processSingleNo != null && !processSingleNo.equals("")) {
					if(condition!=null && !"".equals(condition))
						condition += " and a.processSingleNo like '%" + processSingleNo.trim()+ "%'";
					else
						condition=" where a.processSingleNo like '%" + processSingleNo.trim()+ "%'";
				}
				if (messageSource != null && !messageSource.equals("")) {
					if(condition!=null && !"".equals(condition))
						condition += " and a.messageSource like '%"+ messageSource.trim() + "%'";
					else
						condition=" where a.messageSource like '%"+ messageSource.trim() + "%'";
				}
				if (processPer != null && !processPer.equals("")) {
					if(condition!=null && !"".equals(condition))
						condition += " and (l.username like '%" + processPer.trim() + "%' or a.processPer like '%" + processPer.trim() + "%')";
					else
						condition=" where (l.username like '%" + processPer.trim() + "%' or a.processPer like '%" + processPer.trim() + "%')";
				}
				if(isClose!=null && !isClose.equals("")){
					if(condition!=null && !"".equals(condition))
						condition += " and a.isClose like '%" + isClose.trim()+ "%'";
					else
						condition=" where a.isClose like '%" + isClose.trim()+ "%'";
				}
				if (table.getIsAscending()) {
					condition += " order by " + table.getSortColumn() + " desc";
				} else {
					condition += " order by " + table.getSortColumn() + " asc";
				}
				Query query = hs.createSQLQuery(hql+condition);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				//System.out.println(hql+condition);
				
				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				cache.check(table);
				
				List list = hs.createSQLQuery(hql+condition).list();
				List advisoryComplaintsAuditList = new ArrayList();
				for (Object object : list) {
					Object[] values = (Object[]) object;
					Map m = new HashMap();
					m.put("processSingleNo", values[0]);
					m.put("messageSource", bd.getName_Sql("Pulldown", "pullname", "pullid", values[1].toString()));
					m.put("processPer", values[2]);
					m.put("processDate", values[3]);
					m.put("feedbackPer", values[4]);
					m.put("feedbackTel", values[5]);
					m.put("auditType", values[6]);
					m.put("isClose", values[7]);
					advisoryComplaintsAuditList.add(m);
				}
				table.addAll(advisoryComplaintsAuditList);
				session.setAttribute("advisoryComplaintsRejectList", table);
				request.setAttribute("MessageSourceList", bd.getPullDownList("AdvisoryComplaintsManage_MessageSource"));

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e1) {
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
			forward = mapping.findForward("advisoryComplaintsRejectList");
		}
		return forward;
	}
	
	/**
	 * 咨询投诉驳回>>查询列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecordDispatch(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "	咨询投诉派工>> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form, request, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport", "");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session,
					"advisoryComplaintsDispatchList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fAdvisoryComplaintsDispatch");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			// table.setLength(1);
			cache.updateTable(table);
			table.setSortColumn("processSingleNo");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String processSingleNo = tableForm.getProperty("processSingleNo");
			String messageSource = tableForm.getProperty("messageSource");
			String receivePer = tableForm.getProperty("receivePer");
			
			Session hs = null;

			try {

				hs = HibernateUtil.getSession();

				String hql = "select a.processSingleNo,a.messageSource,l.username,a.receiveDate,a.feedbackPer,a.feedbackTel,a.auditType,a.isClose,a.dispatchType,a.processType,a.submitType"+
				" from AdvisoryComplaintsManage a left join Loginuser l on a.receivePer=l.userid where a.dispatchType='N' and a.submitType='Y'";
				if (processSingleNo != null && !processSingleNo.equals("")) {
					hql += " and a.processSingleNo like '%" + processSingleNo.trim()
							+ "%'";
				}
				if (messageSource != null && !messageSource.equals("")) {
					hql += " and a.messageSource like '%"
							+ messageSource.trim() + "%'";
				}
				if (receivePer != null && !receivePer.equals("")) {
					hql += " and l.username like '%" + receivePer.trim()
							+ "%'";
				}
				if (table.getIsAscending()) {
					hql += " order by " + table.getSortColumn() + " desc";
				} else {
					hql += " order by " + table.getSortColumn() + " asc";
				}
				Query query = hs.createSQLQuery(hql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				cache.check(table);
				
				List list = hs.createSQLQuery(hql).list();
				List advisoryComplaintsAuditList = new ArrayList();
				for (Object object : list) {
					Object[] values = (Object[]) object;
					Map m = new HashMap();
					m.put("processSingleNo", values[0]);
					m.put("messageSource", bd.getName_Sql("Pulldown", "pullname", "pullid", values[1].toString()));
					m.put("receivePer", values[2]);
					m.put("receiveDate", values[3]);
					m.put("feedbackPer", values[4]);
					m.put("feedbackTel", values[5]);
					m.put("auditType", values[6]);
					m.put("isClose", values[7]);
					m.put("dispatchType", values[8]);
					m.put("processType", values[9]);
					m.put("submitType", values[10]);
					advisoryComplaintsAuditList.add(m);
				}
				table.addAll(advisoryComplaintsAuditList);
				session.setAttribute("advisoryComplaintsDispatchList", table);
				request.setAttribute("MessageSourceList", bd.getPullDownList("AdvisoryComplaintsManage_MessageSource"));

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e1) {
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
			forward = mapping.findForward("advisoryComplaintsDispatchList");
		}
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

		request.setAttribute("navigator.location", "咨询投诉登记 >> 查看");
//		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;
		display(form, request, errors, "display");

//		String id = (String) dform.get("id");
//		String returnMethod=(String)dform.get("returnMethod");
//		Session hs = null;
//		AdvisoryComplaintsManage advisorycomplaintsmanage = null;
//		Map map=new HashMap();
		/*if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				advisorycomplaintsmanage=(AdvisoryComplaintsManage) hs.get(AdvisoryComplaintsManage.class, id);
				String dutyDepar="";
				if(advisorycomplaintsmanage.getDutyDepar()!=null && advisorycomplaintsmanage.getDutyDepar().contains(",")){
					String[] comids=advisorycomplaintsmanage.getDutyDepar().split(",");
					if(comids!=null && comids.length>0){
						for(int i=0;i<comids.length;i++){
							dutyDepar+=i==comids.length-1 ?  bd.getName("Company", "comfullname", "comid", comids[i]) :  bd.getName("Company", "comfullname", "comid", comids[i])+",";
						}
					}
				}else{
					dutyDepar=bd.getName("Company", "comfullname", "comid", advisorycomplaintsmanage.getDutyDepar());
				}
				
				if(advisorycomplaintsmanage!=null){
					map.put("processSingleNo", advisorycomplaintsmanage.getProcessSingleNo());
					if(advisorycomplaintsmanage.getMessageSource()!=null && advisorycomplaintsmanage.getMessageSource().equals("")){
						map.put("messageSource", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getMessageSource()));
					}else{
						map.put("messageSource",advisorycomplaintsmanage.getMessageSource());
					}
					map.put("receivePer", bd.getName_Sql("Loginuser", "username", "userid", advisorycomplaintsmanage.getReceivePer()));
					map.put("receiveDate", advisorycomplaintsmanage.getReceiveDate());
					map.put("feedbackPer", advisorycomplaintsmanage.getFeedbackPer());
					map.put("feedbackTel", advisorycomplaintsmanage.getFeedbackTel());
					map.put("problemDesc", advisorycomplaintsmanage.getProblemDesc());
					map.put("processPer", bd.getName_Sql("Loginuser", "username", "userid", advisorycomplaintsmanage.getProcessPer()));
					map.put("processDate", advisorycomplaintsmanage.getProcessDate());
					map.put("projectName", advisorycomplaintsmanage.getProjectName());
					map.put("contractNo", advisorycomplaintsmanage.getContractNo());
					map.put("infoNo", advisorycomplaintsmanage.getInfoNo());
					map.put("elevatorNo", advisorycomplaintsmanage.getElevatorNo());
					map.put("factoryName", advisorycomplaintsmanage.getFactoryName());
					map.put("dutyDepar", dutyDepar);
					map.put("carryOutSituat", advisorycomplaintsmanage.getCarryOutSituat());
					
					map.put("issueSort4", advisorycomplaintsmanage.getIssueSort4());
					map.put("partsName", advisorycomplaintsmanage.getPartsName());
					if(advisorycomplaintsmanage.getQualityIndex()!=null && !advisorycomplaintsmanage.getQualityIndex().equals("")){
						map.put("qualityIndex", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getQualityIndex()));
					}else{
						map.put("qualityIndex", advisorycomplaintsmanage.getQualityIndex());
					}
					
					map.put("assessNo", advisorycomplaintsmanage.getAssessNo());
					if(advisorycomplaintsmanage.getProcessDifficulty()!=null && !advisorycomplaintsmanage.getProcessDifficulty().equals("")){
						map.put("processDifficulty", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getProcessDifficulty()));
					}else{
						map.put("processDifficulty", advisorycomplaintsmanage.getProcessDifficulty());
					}
					
					map.put("isClose", advisorycomplaintsmanage.getIsClose());
					map.put("completeDate", advisorycomplaintsmanage.getCompleteDate());
					map.put("processTime", advisorycomplaintsmanage.getProcessTime());
					if(advisorycomplaintsmanage.getAuditOperid()!=null && !advisorycomplaintsmanage.getAuditOperid().equals("")){
						map.put("auditOperid", bd.getName_Sql("Loginuser", "username", "userid", advisorycomplaintsmanage.getAuditOperid()));
					}else{
						map.put("auditOperid", advisorycomplaintsmanage.getAuditOperid());
					}
					map.put("auditDate", advisorycomplaintsmanage.getAuditDate());
					if(advisorycomplaintsmanage.getMessageSource().equals("400")){
						map.put("octId", bd.getName_Sql("OfficeContractTypes", "octName", "octId", advisorycomplaintsmanage.getOctId()));
						map.put("occId", bd.getName_Sql("OfficeComplaintCategory", "occName", "occId", advisorycomplaintsmanage.getOccId()));
					}else{
						map.put("octId", advisorycomplaintsmanage.getOctId());
						map.put("occId", advisorycomplaintsmanage.getOccId());
					}
					
					if(advisorycomplaintsmanage.getIssueSort1()!=null && advisorycomplaintsmanage.getIssueSort1().equals("GS")){
						map.put("issueSort1", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getIssueSort1()));
						map.put("issueSort2", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getIssueSort2()));
						map.put("issueSort3", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getIssueSort3()));
						map.put("ocaId", bd.getName_Sql("OfficeCauseAnalysis", "ocaName", "ocaId", advisorycomplaintsmanage.getOcaId()));
					}else{
						if(advisorycomplaintsmanage.getIssueSort1()!=null && !advisorycomplaintsmanage.getIssueSort1().equals("")){
							map.put("issueSort1", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getIssueSort1()));
						}else{
							map.put("issueSort1", advisorycomplaintsmanage.getIssueSort1());
						}
						map.put("issueSort2", advisorycomplaintsmanage.getIssueSort2());
						map.put("issueSort3", advisorycomplaintsmanage.getIssueSort3());
						map.put("ocaId", advisorycomplaintsmanage.getOcaId());
					}
					
					
					
				}
				
				if (advisorycomplaintsmanage == null) {
					errors.add(
							ActionErrors.GLOBAL_ERROR,
							new ActionError(
									"qualitycheckmanagement.display.recordnotfounterror"));
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

			request.setAttribute("advisoryComplaintsManageBean", map);
			request.setAttribute("returnListMethod", returnMethod);

		}*/
		request.setAttribute("display", "yes");
		forward = mapping.findForward("advisoryComplaintsManageDisplay");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	public ActionForward toDisposeDisplay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "咨询投诉处理 >> 查看");
//		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;
		
		display(form, request, errors, "display");

		/*String id = (String) dform.get("id");
		String returnMethod=(String)dform.get("returnMethod");
		Session hs = null;
		AdvisoryComplaintsManage advisorycomplaintsmanage = null;
		Map map=new HashMap();
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				advisorycomplaintsmanage=(AdvisoryComplaintsManage) hs.get(AdvisoryComplaintsManage.class, id);
				String dutyDepar="";
				if(advisorycomplaintsmanage.getDutyDepar().contains(",")){
					String[] comids=advisorycomplaintsmanage.getDutyDepar().split(",");
					if(comids!=null && comids.length>0){
						for(int i=0;i<comids.length;i++){
							dutyDepar+=i==comids.length-1 ?  bd.getName("Company", "comfullname", "comid", comids[i]) :  bd.getName("Company", "comfullname", "comid", comids[i])+",";
						}
					}
				}else{
					dutyDepar=bd.getName("Company", "comfullname", "comid", advisorycomplaintsmanage.getDutyDepar());
				}
				if(advisorycomplaintsmanage!=null){
					map.put("processSingleNo", advisorycomplaintsmanage.getProcessSingleNo());
					if(advisorycomplaintsmanage.getMessageSource()!=null && advisorycomplaintsmanage.getMessageSource().equals("")){
						map.put("messageSource", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getMessageSource()));
					}else{
						map.put("messageSource",advisorycomplaintsmanage.getMessageSource());
					}					
					map.put("receivePer", bd.getName_Sql("Loginuser", "username", "userid", advisorycomplaintsmanage.getReceivePer()));
					map.put("receiveDate", advisorycomplaintsmanage.getReceiveDate());
					map.put("feedbackPer", advisorycomplaintsmanage.getFeedbackPer());
					map.put("feedbackTel", advisorycomplaintsmanage.getFeedbackTel());
					map.put("problemDesc", advisorycomplaintsmanage.getProblemDesc());
					map.put("processPer", bd.getName_Sql("Loginuser", "username", "userid", advisorycomplaintsmanage.getProcessPer()));
					map.put("processDate", advisorycomplaintsmanage.getProcessDate());
					map.put("projectName", advisorycomplaintsmanage.getProjectName());
					map.put("contractNo", advisorycomplaintsmanage.getContractNo());
					map.put("infoNo", advisorycomplaintsmanage.getInfoNo());
					map.put("elevatorNo", advisorycomplaintsmanage.getElevatorNo());
					map.put("factoryName", advisorycomplaintsmanage.getFactoryName());
					map.put("dutyDepar", dutyDepar);
					map.put("carryOutSituat", advisorycomplaintsmanage.getCarryOutSituat());
					
					map.put("issueSort4", advisorycomplaintsmanage.getIssueSort4());
					map.put("partsName", advisorycomplaintsmanage.getPartsName());
					if(advisorycomplaintsmanage.getQualityIndex()!=null && !advisorycomplaintsmanage.getQualityIndex().equals("")){
						map.put("qualityIndex", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getQualityIndex()));
					}else{
						map.put("qualityIndex", advisorycomplaintsmanage.getQualityIndex());
					}
					
					map.put("assessNo", advisorycomplaintsmanage.getAssessNo());
					if(advisorycomplaintsmanage.getProcessDifficulty()!=null && !advisorycomplaintsmanage.getProcessDifficulty().equals("")){
						map.put("processDifficulty", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getProcessDifficulty()));
					}else{
						map.put("processDifficulty", advisorycomplaintsmanage.getProcessDifficulty());
					}
					
					map.put("isClose", advisorycomplaintsmanage.getIsClose());
					map.put("completeDate", advisorycomplaintsmanage.getCompleteDate());
					map.put("processTime", advisorycomplaintsmanage.getProcessTime());
					if(advisorycomplaintsmanage.getAuditOperid()!=null && !advisorycomplaintsmanage.getAuditOperid().equals("")){
						map.put("auditOperid", bd.getName_Sql("Loginuser", "username", "userid", advisorycomplaintsmanage.getAuditOperid()));
					}else{
						map.put("auditOperid", advisorycomplaintsmanage.getAuditOperid());
					}
					map.put("auditDate", advisorycomplaintsmanage.getAuditDate());
					if(advisorycomplaintsmanage.getMessageSource().equals("400")){
						map.put("octId", bd.getName_Sql("OfficeContractTypes", "octName", "octId", advisorycomplaintsmanage.getOctId()));
						map.put("occId", bd.getName_Sql("OfficeComplaintCategory", "occName", "occId", advisorycomplaintsmanage.getOccId()));
					}else{
						map.put("octId", advisorycomplaintsmanage.getOctId());
						map.put("occId", advisorycomplaintsmanage.getOccId());
					}
					
					if(advisorycomplaintsmanage.getIssueSort1()!=null && advisorycomplaintsmanage.getIssueSort1().equals("GS")){
						map.put("issueSort1", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getIssueSort1()));
						map.put("issueSort2", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getIssueSort2()));
						map.put("issueSort3", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getIssueSort3()));
						map.put("ocaId", bd.getName_Sql("OfficeCauseAnalysis", "ocaName", "ocaId", advisorycomplaintsmanage.getOcaId()));
					}else{
						if(advisorycomplaintsmanage.getIssueSort1()!=null && !advisorycomplaintsmanage.getIssueSort1().equals("")){
							map.put("issueSort1", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getIssueSort1()));
						}else{
							map.put("issueSort1", advisorycomplaintsmanage.getIssueSort1());
						}
						map.put("issueSort2", advisorycomplaintsmanage.getIssueSort2());
						map.put("issueSort3", advisorycomplaintsmanage.getIssueSort3());
						map.put("ocaId", advisorycomplaintsmanage.getOcaId());
					}
					
					
					
				}
				
				if (advisorycomplaintsmanage == null) {
					errors.add(
							ActionErrors.GLOBAL_ERROR,
							new ActionError(
									"qualitycheckmanagement.display.recordnotfounterror"));
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

			request.setAttribute("advisoryComplaintsManageBean", map);
			request.setAttribute("returnListMethod", returnMethod);

		}*/
		request.setAttribute("display", "yes");
		forward = mapping.findForward("advisoryComplaintsDisposeDisplay");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	public ActionForward toAuditDisplay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "咨询投诉分析总结 >> 查看");
//		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;
		
		display(form, request, errors, "display");

		/*String id = (String) dform.get("id");
		String returnMethod=(String)dform.get("returnMethod");
		Session hs = null;
		AdvisoryComplaintsManage advisorycomplaintsmanage = null;
		Map map=new HashMap();
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				advisorycomplaintsmanage=(AdvisoryComplaintsManage) hs.get(AdvisoryComplaintsManage.class, id);
				String dutyDepar="";
				if(advisorycomplaintsmanage.getDutyDepar().contains(",")){
					String[] comids=advisorycomplaintsmanage.getDutyDepar().split(",");
					if(comids!=null && comids.length>0){
						for(int i=0;i<comids.length;i++){
							dutyDepar+=i==comids.length-1 ?  bd.getName("Company", "comfullname", "comid", comids[i]) :  bd.getName("Company", "comfullname", "comid", comids[i])+",";
						}
					}
				}else{
					dutyDepar=bd.getName("Company", "comfullname", "comid", advisorycomplaintsmanage.getDutyDepar());
				}
				if(advisorycomplaintsmanage!=null){
					map.put("processSingleNo", advisorycomplaintsmanage.getProcessSingleNo());
					if(advisorycomplaintsmanage.getMessageSource()!=null && advisorycomplaintsmanage.getMessageSource().equals("")){
						map.put("messageSource", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getMessageSource()));
					}else{
						map.put("messageSource",advisorycomplaintsmanage.getMessageSource());
					}					
					map.put("receivePer", bd.getName_Sql("Loginuser", "username", "userid", advisorycomplaintsmanage.getReceivePer()));
					map.put("receiveDate", advisorycomplaintsmanage.getReceiveDate());
					map.put("feedbackPer", advisorycomplaintsmanage.getFeedbackPer());
					map.put("feedbackTel", advisorycomplaintsmanage.getFeedbackTel());
					map.put("problemDesc", advisorycomplaintsmanage.getProblemDesc());
					map.put("processPer", bd.getName_Sql("Loginuser", "username", "userid", advisorycomplaintsmanage.getProcessPer()));
					map.put("processDate", advisorycomplaintsmanage.getProcessDate());
					map.put("projectName", advisorycomplaintsmanage.getProjectName());
					map.put("contractNo", advisorycomplaintsmanage.getContractNo());
					map.put("infoNo", advisorycomplaintsmanage.getInfoNo());
					map.put("elevatorNo", advisorycomplaintsmanage.getElevatorNo());
					map.put("factoryName", advisorycomplaintsmanage.getFactoryName());
					map.put("dutyDepar", dutyDepar);
					map.put("carryOutSituat", advisorycomplaintsmanage.getCarryOutSituat());
					
					map.put("issueSort4", advisorycomplaintsmanage.getIssueSort4());
					map.put("partsName", advisorycomplaintsmanage.getPartsName());
					if(advisorycomplaintsmanage.getQualityIndex()!=null && !advisorycomplaintsmanage.getQualityIndex().equals("")){
						map.put("qualityIndex", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getQualityIndex()));
					}else{
						map.put("qualityIndex", advisorycomplaintsmanage.getQualityIndex());
					}
					
					map.put("assessNo", advisorycomplaintsmanage.getAssessNo());
					if(advisorycomplaintsmanage.getProcessDifficulty()!=null && !advisorycomplaintsmanage.getProcessDifficulty().equals("")){
						map.put("processDifficulty", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getProcessDifficulty()));
					}else{
						map.put("processDifficulty", advisorycomplaintsmanage.getProcessDifficulty());
					}
					
					map.put("isClose", advisorycomplaintsmanage.getIsClose());
					map.put("completeDate", advisorycomplaintsmanage.getCompleteDate());
					map.put("processTime", advisorycomplaintsmanage.getProcessTime());
					if(advisorycomplaintsmanage.getAuditOperid()!=null && !advisorycomplaintsmanage.getAuditOperid().equals("")){
						map.put("auditOperid", bd.getName_Sql("Loginuser", "username", "userid", advisorycomplaintsmanage.getAuditOperid()));
					}else{
						map.put("auditOperid", advisorycomplaintsmanage.getAuditOperid());
					}
					map.put("auditDate", advisorycomplaintsmanage.getAuditDate());
					if(advisorycomplaintsmanage.getMessageSource().equals("400")){
						map.put("octId", bd.getName_Sql("OfficeContractTypes", "octName", "octId", advisorycomplaintsmanage.getOctId()));
						map.put("occId", bd.getName_Sql("OfficeComplaintCategory", "occName", "occId", advisorycomplaintsmanage.getOccId()));
					}else{
						map.put("octId", advisorycomplaintsmanage.getOctId());
						map.put("occId", advisorycomplaintsmanage.getOccId());
					}
					
					if(advisorycomplaintsmanage.getIssueSort1()!=null && advisorycomplaintsmanage.getIssueSort1().equals("GS")){
						map.put("issueSort1", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getIssueSort1()));
						map.put("issueSort2", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getIssueSort2()));
						map.put("issueSort3", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getIssueSort3()));
						map.put("ocaId", bd.getName_Sql("OfficeCauseAnalysis", "ocaName", "ocaId", advisorycomplaintsmanage.getOcaId()));
					}else{
						if(advisorycomplaintsmanage.getIssueSort1()!=null && !advisorycomplaintsmanage.getIssueSort1().equals("")){
							map.put("issueSort1", bd.getName_Sql("Pulldown", "pullname", "pullid", advisorycomplaintsmanage.getIssueSort1()));
						}else{
							map.put("issueSort1", advisorycomplaintsmanage.getIssueSort1());
						}
						map.put("issueSort2", advisorycomplaintsmanage.getIssueSort2());
						map.put("issueSort3", advisorycomplaintsmanage.getIssueSort3());
						map.put("ocaId", advisorycomplaintsmanage.getOcaId());
					}
					
					
					
				}
				
				if (advisorycomplaintsmanage == null) {
					errors.add(
							ActionErrors.GLOBAL_ERROR,
							new ActionError(
									"qualitycheckmanagement.display.recordnotfounterror"));
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

			request.setAttribute("advisoryComplaintsManageBean", map);
			request.setAttribute("returnListMethod", returnMethod);

		}*/
		request.setAttribute("display", "yes");
		forward = mapping.findForward("advisoryComplaintsAuditDisplay");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	public ActionForward toDispatchDisplay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "咨询投诉派工 >> 查看");
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;

//		String id = (String) dform.get("id");
//		Session hs = null;
//		AdvisoryComplaintsManage advisorycomplaintsmanage = null;
//		Map map=new HashMap();
		display(form, request, errors, "display");

		/*	request.setAttribute("advisoryComplaintsManageBean", map);
			request.setAttribute("returnListMethod", returnMethod);

		}*/
		request.setAttribute("display", "yes");
		forward = mapping.findForward("advisoryComplaintsDispatchDisplay");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	public ActionForward toRejectDisplay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "咨询投诉驳回 >> 查看");
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;

//		String id = (String) dform.get("id");
//		Session hs = null;
//		AdvisoryComplaintsManage advisorycomplaintsmanage = null;
//		Map map=new HashMap();
		display(form, request, errors, "display");

		/*	request.setAttribute("advisoryComplaintsManageBean", map);
			request.setAttribute("returnListMethod", returnMethod);

		}*/
		request.setAttribute("display", "yes");
//		forward = mapping.findForward("advisoryComplaintsReject");
		forward = mapping.findForward("advisoryComplaintsRejectDisplay");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */

	@SuppressWarnings("unchecked")
	public ActionForward toPrepareAddRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		request.setAttribute("navigator.location", "咨询投诉登记>> 添加");
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
//		String returnMethod=(String)dform.get("returnMethod");
		
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
			dform.set("receivePer", userInfo.getUserName());
			request.setAttribute("processList", getProcessList());
			request.setAttribute("receiveDate", CommonUtil.getNowTime());
			//System.out.println(CommonUtil.getNowTime());
		}
		dform.set("receivePer", userInfo.getUserName());
		request.setAttribute("processPer", userInfo.getUserName());
		request.setAttribute("receiveDate", CommonUtil.getNowTime());
//		request.setAttribute("processList", getProcessList());
//		request.setAttribute("returnListMethod", returnMethod);
		request.setAttribute("MessageSourceList", bd.getPullDownList("AdvisoryComplaintsManage_MessageSource"));
		return mapping.findForward("advisoryComplaintsManageAdd");
		
	}
	
	public static List getProcessList(){
		List processList=new ArrayList();
		Session hs=null;
		try {
			hs=HibernateUtil.getSession();
			String hql="select userid,username from Loginuser where class1id='97'";
			List list=hs.createQuery(hql).list();
			for(Object object : list){
				Object[] values=(Object[])object;
				Map map=new HashMap();
				map.put("userid", values[0]);
				map.put("username", values[1]);
				processList.add(map);
			}
		} catch (DataStoreException e) {
			e.printStackTrace();
		}
		return processList;
	}

	public static String getDisposeNum(){
		Session hs=null;
		Transaction tx=null;
		Getnum getnum = new Getnum();
		GetnumKey getnumkey = new GetnumKey();
		String processSingleNo="";
		int nextNo=1;
		try {
			hs = HibernateUtil.getSession();
			tx=hs.beginTransaction();
			String year1=CommonUtil.getToday().substring(2, 4);
			String year=CommonUtil.getToday().substring(0,4);
			String month=CommonUtil.getToday().substring(5, 7);
			String hql="from Getnum as a WHERE a.id.year1='"+year1+"' AND a.id.deptflag ='AdvisoryComplaintsManage'";
			List list=hs.createQuery(hql).list();
			if(list==null || list.isEmpty() || list.size()==0){
				processSingleNo=year+month+String.format("%03d", nextNo);
				nextNo++;
				getnumkey.setYear1(year1);
				getnumkey.setDeptflag("AdvisoryComplaintsManage");
				getnum.setId(getnumkey);
			}else{
				getnum=(Getnum) list.get(0);
				nextNo=getnum.getNextno();
				processSingleNo=year+month+String.format("%03d", nextNo);
				nextNo++;
			}
			getnum.setNextno(new Integer(nextNo));
			hs.save(getnum);
			tx.commit();
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processSingleNo;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */

	public ActionForward toAddRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;

		String processSingleNo = getDisposeNum();// 处理单编号
		String messageSource = (String) dform.get("messageSource");// 信息来源
		String receiveDate = (String) dform.get("receiveDate");// 问题接收日期
		String feedbackPer = (String) dform.get("feedbackPer");// 问题反馈人姓名
		String feedbackTel = (String) dform.get("feedbackTel");// 问题反馈人电话
//		String processPer=(String)dform.get("processPer");//问题处理人
		String problemDesc = request.getParameter("problemDesc");//问题描述
		String submitType = (String) dform.get("submitType");// 提交标志
		String isEntrust=(String)dform.get("isEntrust");
		String dispatchType=(String)dform.get("dispatchType");

		try {
			/*String todayDate = CommonUtil.getToday();
			String year = todayDate.substring(2, 4);
			String billno = CommonUtil.getBillno(year,
					"QualityCheckManagement", 1)[0];// 流水号
*/			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			
			AdvisoryComplaintsManage advisorycomplaintsmanage = new AdvisoryComplaintsManage();
			advisorycomplaintsmanage.setProcessSingleNo(processSingleNo);
			advisorycomplaintsmanage.setMessageSource(messageSource);
			advisorycomplaintsmanage.setReceivePer(userInfo.getUserID());
			advisorycomplaintsmanage.setReceiveDate(receiveDate);
			advisorycomplaintsmanage.setFeedbackPer(feedbackPer);
			advisorycomplaintsmanage.setFeedbackTel(feedbackTel);
			if(isEntrust!=null && !isEntrust.equals("") && isEntrust.equals("N")){
				advisorycomplaintsmanage.setProcessPer(userInfo.getUserID());
				advisorycomplaintsmanage.setDispatchType(dispatchType);
			}else{
				advisorycomplaintsmanage.setProcessPer("");
				advisorycomplaintsmanage.setDispatchType("N");
			}
			advisorycomplaintsmanage.setIsEntrust(isEntrust);
			advisorycomplaintsmanage.setProblemDesc(problemDesc);
			advisorycomplaintsmanage.setSubmitType(submitType);
			advisorycomplaintsmanage.setProcessType("N");
			advisorycomplaintsmanage.setAuditType("N");
			advisorycomplaintsmanage.setIsClose("");

			hs.save(advisorycomplaintsmanage);
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"qualitycheckmanagement.insert.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Insert error!");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Insert error!");
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
						"insert.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"insert.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				forward = mapping.findForward("returnAdd");
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
	 * 跳转到修改级别页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "咨询投诉登记 >> 修改");
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
//		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		ActionForward forward = null;
		String id = null;

		/*if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("processSingleNo");
		} else {
			id = (String) dform.get("id");
		}*/
		display(form, request, errors, "");

//		Session hs = null;
//		AdvisoryComplaintsManage advisorycomplaintsmanage = null;
		/*if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					advisorycomplaintsmanage = (AdvisoryComplaintsManage) hs.get(
							AdvisoryComplaintsManage.class, id);
					advisorycomplaintsmanage.setReceivePer(bd.getName_Sql("Loginuser", "username", "userid", advisorycomplaintsmanage.getReceivePer()));
					String dutyDepar="";
					if(advisorycomplaintsmanage.getDutyDepar().contains(",")){
						String[] comids=advisorycomplaintsmanage.getDutyDepar().split(",");
						if(comids!=null && comids.length>0){
							for(int i=0;i<comids.length;i++){
								dutyDepar+=i==comids.length-1 ?  bd.getName("Company", "comfullname", "comid", comids[i]) :  bd.getName("Company", "comfullname", "comid", comids[i])+",";
							}
						}
					}else{
						dutyDepar=bd.getName("Company", "comfullname", "comid", advisorycomplaintsmanage.getDutyDepar());
					}

					if (advisorycomplaintsmanage == null) {
						errors.add(
								ActionErrors.GLOBAL_ERROR,
								new ActionError(
										"qualitycheckmanagement.display.recordnotfounterror"));
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
						DebugUtil
								.print(hex, "HibernateUtil Hibernate Session ");
					}
				}
			}
			request.setAttribute("advisoryComplaintsManageBean",
					advisorycomplaintsmanage);
			
		}*/
		request.setAttribute("MessageSourceList", bd.getPullDownList("AdvisoryComplaintsManage_MessageSource"));
		request.setAttribute("processList", getProcessList());
		forward = mapping.findForward("advisoryComplaintsManageModify");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * 紧急级别修改
	 * 
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
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		String processSingleNo = (String) dform.get("processSingleNo");
		String messageSource = (String) dform.get("messageSource");// 信息来源
		String receiveDate = (String) dform.get("receiveDate");
		String feedbackPer = (String) dform.get("feedbackPer");
		String feedbackTel = (String) dform.get("feedbackTel");
//		String processPer=(String)dform.get("processPer");
		String problemDesc = (String) dform.get("problemDesc");
		String submitType = (String) dform.get("submitType");
		String isEntrust=(String)dform.get("isEntrust");

		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			AdvisoryComplaintsManage advisorycomplaintsmanage = (AdvisoryComplaintsManage) hs
					.get(AdvisoryComplaintsManage.class, (String) dform.get("id"));
			if(advisorycomplaintsmanage!=null){
				advisorycomplaintsmanage.setProcessSingleNo(processSingleNo);
				advisorycomplaintsmanage.setMessageSource(messageSource);
				advisorycomplaintsmanage.setReceivePer(userInfo.getUserID());
				advisorycomplaintsmanage.setReceiveDate(receiveDate);
				advisorycomplaintsmanage.setFeedbackPer(feedbackPer);
				advisorycomplaintsmanage.setFeedbackTel(feedbackTel);
				if(isEntrust!=null && !isEntrust.equals("") && isEntrust.equals("N")){
					advisorycomplaintsmanage.setProcessPer(userInfo.getUserID());
					advisorycomplaintsmanage.setDispatchType("Y");
				}else{
					advisorycomplaintsmanage.setProcessPer("");
					advisorycomplaintsmanage.setDispatchType("N");
				}
				advisorycomplaintsmanage.setIsEntrust(isEntrust);
				advisorycomplaintsmanage.setProblemDesc(problemDesc);
				advisorycomplaintsmanage.setSubmitType(submitType);

				hs.save(advisorycomplaintsmanage);
				/*String sql="update AdvisoryComplaintsManage set receivePer='"+userInfo.getUserID()+"',receiveDate='"+receiveDate+"',feedbackPer='"+feedbackPer+"',feedbackTel='"+feedbackTel+"',processPer='"+processPer+"',problemDesc='"+problemDesc+"',submitType='"+submitType+"' where processSingleNo='"+(String)dform.get("id")+"'";
				hs.connection().prepareStatement(sql).execute();
				hs.flush();*/
			}
			
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"qualitycheckmanagement.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");
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
	 * 删除紧急级别
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDeleteRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		int len;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			AdvisoryComplaintsManage advisorycomplaintsmanage = (AdvisoryComplaintsManage) hs
					.get(AdvisoryComplaintsManage.class, (String) dform.get("id"));

			if (advisorycomplaintsmanage != null) {
				if (advisorycomplaintsmanage.getSubmitType() == "Y") {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"delete.foreignkeyerror"));
				} else {

					hs.delete(advisorycomplaintsmanage);
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"delete.succeed"));
				}
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
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");

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
	 * 提交
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toSubmitRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		int len;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			AdvisoryComplaintsManage advisorycomplaintsmanage = (AdvisoryComplaintsManage) hs
					.get(AdvisoryComplaintsManage.class, (String) dform.get("id"));

			if (advisorycomplaintsmanage != null) {
				if (advisorycomplaintsmanage.getSubmitType() == "Y") {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"submit.foreignkeyerror"));
				} else {
					advisorycomplaintsmanage.setSubmitType("Y");
					if(advisorycomplaintsmanage.getIsEntrust()!=null && "N".equals(advisorycomplaintsmanage.getIsEntrust())){
						advisorycomplaintsmanage.setDispatchType("Y");
					}
					hs.save(advisorycomplaintsmanage);
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"submit.succeed"));
				}
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"submit.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");

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

		String processSingleNo = tableForm.getProperty("processSingleNo");
		String messageSource = tableForm.getProperty("messageSource");
		String receivePer = tableForm.getProperty("receivePer");
		String receiveDate = tableForm.getProperty("receiveDate");
		String feedbackPer = tableForm.getProperty("feedbackPer");
		String feedbackTel = tableForm.getProperty("feedbackTel");
		String problemDesc = tableForm.getProperty("problemDesc");
		String submitType = tableForm.getProperty("submitType");
		
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(AdvisoryComplaintsManage.class);
			if (processSingleNo != null && !processSingleNo.equals("")) {
				criteria.add(Expression.like("processSingleNo",
						"%" + processSingleNo.trim() + "%"));
			}
			if (messageSource != null && !messageSource.equals("")) {
				criteria.add(Expression.like("messageSource", "%"
						+ messageSource.trim() + "%"));
			}
			if (receivePer != null && !receivePer.equals("")) {
				criteria.add(Expression.like("receivePer",
						"%" + receivePer.trim() + "%"));
			}

			if (receivePer != null && !receivePer.equals("")) {
				criteria.add(Expression.like("receivePer", "%"
						+ receivePer.trim() + "%"));
			}
			if (submitType != null && !submitType.equals("")) {
				criteria.add(Expression.eq("submitType", submitType));
			}

			criteria.addOrder(Order.asc("processSingleNo"));

			List roleList = criteria.list();

			XSSFSheet sheet = wb.createSheet("维保质量检查管理");

			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			String[] columnNames = { "processSingleNo", "messageSource", "receivePer",
					"receiveDate", "feedbackPer", "feedbackTel",
					"problemDesc", "submitType", "processPer",
					"projectName", "contractNo", "infoNo", "elevatorNo",
					"octId", "factoryName", "dutyDepar", "carryOutSituat",
					"processType","issueSort1","issueSort2","issueSort3",
					"issueSort4","partsName","qualityIndex","occId",
					"assessNo","ocaId","processDifficulty","isClose",
					"completeDate","processTime","auditType","auditOperid",
					"auditDate"};

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);

				for (int i = 0; i < columnNames.length; i++) {
					XSSFCell cell0 = row0.createCell((short) i);
					cell0.setCellValue(messages.getMessage(locale,
							"advisoryComplaintsManage." + columnNames[i]));
				}

				Class<?> superClazz = AdvisoryComplaintsManage.class
						.getSuperclass();
				for (int i = 0; i < l; i++) {
					AdvisoryComplaintsManage master = (AdvisoryComplaintsManage) roleList
							.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i + 1);
					for (int j = 0; j < columnNames.length; j++) {
						// 创建Excel列
						XSSFCell cell = row.createCell((short) j);
						cell.setCellValue(getValue(master, superClazz,
								columnNames[j]));
					}
				}
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
		response.setHeader("Content-disposition", "offline; filename="
				+ URLEncoder.encode("咨询投诉管理", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());

		return response;
	}

	/**
	 * 获得对象指定的get方法并返回执行该get方法后的值
	 * 
	 * @param object
	 *            javabean对象
	 * @param superClazz
	 *            object的类，子类没有相应get方法时请传入object的父类
	 * @param fieldName
	 *            属性名
	 * @return ActionForward
	 */
	private String getValue(Object object, Class<?> superClazz, String fieldName) {
		String value = null;
		String methodName = "get"
				+ fieldName.replaceFirst(fieldName.substring(0, 1), fieldName
						.substring(0, 1).toUpperCase());
		try {
			Method method = superClazz.getMethod(methodName);
			value = method.invoke(object, null) + "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;

	}

	/**
	 * 跳转到登记级别页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public ActionForward toPrepareDisposeRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "咨询投诉处理 >> 处理");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);

		ActionForward forward = null;
		String id = null;
		String returnMethod=(String)dform.get("returnMethod");
		if(returnMethod==null || returnMethod.equals("")){
			returnMethod=request.getAttribute("returnMethod").toString();
		}

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("processSingleNo");
		} else {
			id = (String) dform.get("id");
		}

		String fraction="";
		Session hs = null;
		AdvisoryComplaintsManage manage=null;
		List octList=null;
		List dutyDepar=new ArrayList();
		String hql = "";
		if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					manage=(AdvisoryComplaintsManage) hs.get(AdvisoryComplaintsManage.class, id);
					if(manage.getMessageSource()!=null && !manage.getMessageSource().equals("")){
						manage.setMessageSource(bd.getName_Sql("Pulldown", "pullname", "pullid", manage.getMessageSource()));
					}
					manage.setReceivePer(bd.getName_Sql("Loginuser", "username", "userid", manage.getReceivePer()));
					hql="from OfficeContractTypes order by octId";
					octList=hs.createQuery(hql).list();
					String[] dutyDepars=null;
					if(manage.getDutyDepar()!=null && !"".equals(manage.getDutyDepar())){
						if(manage.getDutyDepar().contains(",")){
							dutyDepars=manage.getDutyDepar().split(",");						
						}else{
							Map m=new HashMap();
							m.put("comid", manage.getDutyDepar());
							m.put("comfullname", bd.getName("Company", "comfullname", "comid", manage.getDutyDepar()));
							dutyDepar.add(m);
						}
						if(dutyDepars!=null){
							for(int i=0;i<dutyDepars.length;i++){
								Map m=new HashMap();
								m.put("comid", dutyDepars[i]);
								m.put("comfullname", bd.getName("Company", "comfullname", "comid", dutyDepars[i]));
								dutyDepar.add(m);
							}
						}
					}
					

					if (manage == null) {
						errors.add(
								ActionErrors.GLOBAL_ERROR,
								new ActionError(
										"qualitycheckmanagement.display.recordnotfounterror"));
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
						DebugUtil
								.print(hex, "HibernateUtil Hibernate Session ");
					}
				}
			}
			request.setAttribute("advisoryComplaintsManageBean",manage);
			request.setAttribute("processPerName", bd.getName("Loginuser", "username", "userid", manage.getProcessPer()));
			request.setAttribute("octList", octList);
			request.setAttribute("dutyDepar", dutyDepar);
//			request.setAttribute("returnListMethod", returnMethod);
			forward = mapping.findForward("advisoryComplaintsDisposeAdd");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * 紧急级别登记
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDisposeRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		String processSingleNo = (String) dform.get("processSingleNo");
		String projectName = (String) dform.get("projectName");
		String contractNo = (String) dform.get("contractNo");
		String infoNo = (String) dform.get("infoNo");
		String elevatorNo = (String) dform.get("elevatorNo");
		String octId = (String) dform.get("octId");
		String factoryName = (String) dform.get("factoryName");
//		String dutyDepar = (String) dform.get("dutyDepar");
		String carryOutSituat = (String) dform.get("carryOutSituat");
		String processType = (String) dform.get("processType");
		String[] comids=request.getParameterValues("comid");
		String comid="";
		if(comids!=null && comids.length>0){
			for(int i=0;i<comids.length;i++){
				comid+=i==comids.length-1 ? comids[i] : comids[i]+",";
			}
		}
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			AdvisoryComplaintsManage advisorycomplaintsmanage = (AdvisoryComplaintsManage) hs
					.get(AdvisoryComplaintsManage.class, processSingleNo);
			String processDate=CommonUtil.getToday()+" "+CommonUtil.getTodayTime();
			if(advisorycomplaintsmanage!=null){
				String hql = "update AdvisoryComplaintsManage set processDate='"+processDate+"',projectName='"+projectName+"',contractNo='"+contractNo+"',infoNo='"+infoNo+"',elevatorNo='"+elevatorNo+"',octId='"+octId+"',factoryName='"+factoryName+"',dutyDepar='"+comid+"',carryOutSituat='"+carryOutSituat+"',processType='"+processType+"' where processSingleNo='"+processSingleNo+"'";
				hs.createQuery(hql).executeUpdate();
			}
			
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"qualitycheckmanagement.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");
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
				forward = mapping.findForward("returnDisposeList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				request.setAttribute("returnMethod", "toSearchRecordDispose");
				forward = mapping.findForward("returnDispose");
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
	 * 跳转到审核级别页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public ActionForward toPrepareAuditRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "咨询投诉分析总结 >> 分析总结");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);

		ActionForward forward = null;
		String id = null;
		String returnMethod=(String)dform.get("returnMethod");

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("processSingleNo");
		} else {
			id = (String) dform.get("id");
		}

		Session hs = null;
		AdvisoryComplaintsManage manage=null;
		String type="AdvisoryComplaintsManage_IssueSort4";
		List occList = null;
		List ocaList=null;
		List is4List=null;
		String hql = "";
		if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					manage=(AdvisoryComplaintsManage) hs.get(AdvisoryComplaintsManage.class, id);
					hql="from OfficeComplaintCategory order by occId";
					occList=hs.createQuery(hql).list();
					hql="from OfficeCauseAnalysis order by ocaId";
					ocaList=hs.createQuery(hql).list();
					manage.setReceivePer(bd.getName_Sql("Loginuser", "username", "userid", manage.getReceivePer()));
					manage.setProcessPer(bd.getName_Sql("Loginuser", "username", "userid", manage.getProcessPer()));
					manage.setAuditOperid(userInfo.getUserName());
					if(manage.getOctId()!=null && !manage.getOctId().equals("")){
						manage.setOctId(bd.getName_Sql("OfficeContractTypes", "octName", "octId", manage.getOctId()));
					}
					if(manage.getMessageSource()!=null && manage.getMessageSource().equals("")){
						manage.setMessageSource(bd.getName_Sql("Pulldown", "pullname", "pullid", manage.getMessageSource()));
					}
					String dutyDepar="";
					if(manage.getDutyDepar().contains(",")){
						String[] comids=manage.getDutyDepar().split(",");
						if(comids!=null && comids.length>0){
							for(int i=0;i<comids.length;i++){
								dutyDepar+=i==comids.length-1 ?  bd.getName("Company", "comfullname", "comid", comids[i]) :  bd.getName("Company", "comfullname", "comid", comids[i])+",";
							}
						}
					}else{
						dutyDepar=bd.getName("Company", "comfullname", "comid", manage.getDutyDepar());
					}
					if(manage.getQualityIndex()!=null && manage.getQualityIndex().contains(",")){
						String[] qualistyIndexs=manage.getQualityIndex().split(",");
						List list2=new ArrayList();
						if(qualistyIndexs!=null && qualistyIndexs.length>0){
							for(int i=0;i<qualistyIndexs.length;i++){
								list2.add(qualistyIndexs[i]);
							}
						}else{
							list2.add(manage.getQualityIndex());
						}
						request.setAttribute("list2", list2);
					}
					manage.setDutyDepar(dutyDepar);
					if(manage.getIssueSort4()!=null && !"".equals(manage.getIssueSort4())){
						if(manage.getIssueSort1()!=null && !"".equals(manage.getIssueSort1())){
							if("GS".equals(manage.getIssueSort1())){
								if(manage.getIssueSort2()!=null && !"".equals(manage.getIssueSort2())){
									if(!manage.getIssueSort2().equals("-")){
										type+="_"+manage.getIssueSort1()+"_"+manage.getIssueSort2();						
									}else{
										type="AdvisoryComplaintsManage_IssueSort4--";
									}
								}
							}else if(!manage.getIssueSort1().equals("-")){
								type+="_"+manage.getIssueSort1()+"-";
							}else{
								type="AdvisoryComplaintsManage_IssueSort4--";
							}
						}
						is4List=hs.createQuery("from Pulldown where typeflag like '%"+type+"%' and enabledflag='Y' order by orderby").list();
					}
					
					if (manage == null) {
						errors.add(
								ActionErrors.GLOBAL_ERROR,
								new ActionError(
										"qualitycheckmanagement.display.recordnotfounterror"));
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
						DebugUtil
								.print(hex, "HibernateUtil Hibernate Session ");
					}
				}
			}
			request.setAttribute("advisoryComplaintsManageBean",
					manage);
			request.setAttribute("occList", occList);
			request.setAttribute("ocaList", ocaList);
			request.setAttribute("pdList", bd.getPullDownList("AdvisoryComplaintsManage_ProcessDifficulty"));
			request.setAttribute("qiList", bd.getPullDownList("AdvisoryComplaintsManage_QualityIndex"));
			request.setAttribute("IS1List", bd.getPullDownList("AdvisoryComplaintsManage_IssueSort1"));
			request.setAttribute("IS2List", bd.getPullDownList("AdvisoryComplaintsManage_IssueSort2"));
			request.setAttribute("IS3List", bd.getPullDownList("AdvisoryComplaintsManage_IssueSort3"));
			request.setAttribute("IS4List", is4List);
			request.setAttribute("returnListMethod", returnMethod);
			forward = mapping.findForward("advisoryComplaintsAuditAdd");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * 审核提交
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toAuditRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String issueSort1=(String)dform.get("issueSort1");
		String issueSort2="";
		String issueSort3="";
		String issueSort4=(String)dform.get("issueSort4");
		String partsName=(String)dform.get("partsName");
//		String qualityIndex=(String)dform.get("qualityIndex");
		String occId=(String)dform.get("occId");
		String assessNo=(String)dform.get("assessNo");
		String ocaId="";
		String processDifficulty=(String)dform.get("processDifficulty");
		String isClose=(String)dform.get("isClose");
		String completeDate=(String)dform.get("completeDate");
		String processTime=(String)dform.get("processTime");
		String auditType=(String)dform.get("auditType");
		String[] qualityIndexs=request.getParameterValues("qualityIndex");
		String qualityIndex="";
		if(qualityIndexs!=null && qualityIndexs.length>0){
			for(int i=0;i<qualityIndexs.length;i++){
				qualityIndex+=i==qualityIndexs.length-1 ? qualityIndexs[i] : qualityIndexs[i]+",";
			}
		}
		String processType=(String)dform.get("processType");
		
		if(issueSort1.equals("GS")){
			issueSort2=(String)dform.get("issueSort2");
			issueSort3=(String)dform.get("issueSort3");
			ocaId=(String)dform.get("ocaId");
		}else{
			issueSort2="-";
			issueSort3="-";
			ocaId="-";
		}

		String hql="";
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			AdvisoryComplaintsManage advisorycomplaintsmanage=(AdvisoryComplaintsManage) hs.get(AdvisoryComplaintsManage.class, (String)dform.get("processSingleNo"));
			String auditDate=CommonUtil.getToday()+" "+CommonUtil.getTodayTime();
			if(advisorycomplaintsmanage!=null){
				if(processType==null || processType.equals("")){
					hql="update AdvisoryComplaintsManage set issueSort1='"+issueSort1+"',issueSort2='"+issueSort2+"',issueSort3='"+issueSort3+"',issueSort4='"+issueSort4+"',partsName='"+partsName+"',qualityIndex='"+qualityIndex+"',occId='"+occId+"',assessNo='"+assessNo+"',ocaId='"+ocaId+"',processDifficulty='"+processDifficulty+"',isClose='"+isClose+"',completeDate='"+completeDate+"',processTime='"+processTime+"',auditType='"+auditType+"',auditOperid='"+userInfo.getUserID()+"',auditDate='"+auditDate+"' where processSingleNo='"+(String)dform.get("processSingleNo")+"'";
					hs.createQuery(hql).executeUpdate();
				}else{
					advisorycomplaintsmanage.setProcessType(processType);
					advisorycomplaintsmanage.setAuditType(auditType);
					hs.save(advisorycomplaintsmanage);
				}
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"qualitycheckmanagement.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");
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
				forward = mapping.findForward("returnAuditList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				forward = mapping.findForward("returnAudit");
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
	 * ajax 级联 维保部与维保站
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public void toAuditSort4List(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Session hs = null;
		String sort1 = request.getParameter("sort1");
		String sort2 = request.getParameter("sort2");
		response.setHeader("Content-Type", "text/html; charset=GBK");
		List list2 = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='GBK'?>");
		sb.append("<root>");
		
		String type="AdvisoryComplaintsManage_IssueSort4";
		if(sort1!=null && !"".equals(sort1)){
			if("GS".equals(sort1)){
				if(sort2!=null && !"".equals(sort2)){
					if(!sort2.equals("-")){
						type+="_"+sort1+"_"+sort2;						
					}else{
						type="AdvisoryComplaintsManage_IssueSort4--";
					}
				}
			}else if(!sort1.equals("-")){
				type+="_"+sort1+"-";
			}else{
				type="AdvisoryComplaintsManage_IssueSort4--";
			}
		}
		
		try {
			hs = HibernateUtil.getSession();
			if(type!=null && !"".equals(type)){
				String sql="select pullid,pullname from Pulldown where typeflag like '%"+type+"%' and enabledflag='Y' order by orderby";
				List list=hs.createSQLQuery(sql).list();
				if(list!=null && list.size()>0){
					sb.append("<rows>");
					for(Object object : list){
						Object[] pulls=(Object[])object;
						sb.append("<cols name='"+pulls[1]+"' value='"+pulls[0]+"'>").append("</cols>");
					}
					sb.append("</rows>");
				}
			}
			/*if (storageid != null && !"".equals(storageid)) {
				String hql = "select a from Loginuser a where a.storageid like '"
						+ storageid + "%' and a.enabledflag='Y'";
				List list = hs.createQuery(hql).list();
				if (list != null && list.size() > 0) {
					sb.append("<rows>");
					for (int i = 0; i < list.size(); i++) {
						Loginuser user = (Loginuser) list.get(i);
						sb.append(
								"<cols name='" + user.getUsername()
										+ "' value='" + user.getUserid()
										+ "'>").append("</cols>");
					}
					sb.append("</rows>");

				}
			}*/
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
	 * 跳转到派工页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public ActionForward toPrepareDispatchRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "咨询投诉派工 >> 派工");
//		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);

		ActionForward forward = null;
		display(form, request, errors, "");

		request.setAttribute("processList", getProcessList());
		forward = mapping.findForward("advisoryComplaintsDispatch");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	/**
	 * 派工
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDispatchRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String processPer=(String)dform.get("processPer");
		String submitType=(String)dform.get("submitType");
		String dispatchType=(String)dform.get("dispatchType");
		

		String hql="";
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			AdvisoryComplaintsManage advisorycomplaintsmanage=(AdvisoryComplaintsManage) hs.get(AdvisoryComplaintsManage.class, (String)dform.get("processSingleNo"));
//			String auditDate=CommonUtil.getToday()+" "+CommonUtil.getTodayTime();
			if(advisorycomplaintsmanage!=null){
				if(submitType!=null && !"R".equals(submitType))
					advisorycomplaintsmanage.setProcessPer(processPer);
				
				advisorycomplaintsmanage.setSubmitType(submitType);
				advisorycomplaintsmanage.setDispatchType(dispatchType);
				hs.save(advisorycomplaintsmanage);
				
				/*hql="update AdvisoryComplaintsManage set issueSort1='"+issueSort1+"',issueSort2='"+issueSort2+"',issueSort3='"+issueSort3+"',issueSort4='"+issueSort4+"',partsName='"+partsName+"',qualityIndex='"+qualityIndex+"',occId='"+occId+"',assessNo='"+assessNo+"',ocaId='"+ocaId+"',processDifficulty='"+processDifficulty+"',isClose='"+isClose+"',completeDate='"+completeDate+"',processTime='"+processTime+"',auditType='"+auditType+"',auditOperid='"+userInfo.getUserID()+"',auditDate='"+auditDate+"' where processSingleNo='"+(String)dform.get("processSingleNo")+"'";
				hs.createQuery(hql).executeUpdate();*/
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"qualitycheckmanagement.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");
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
				forward = mapping.findForward("returnDispatchList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				forward = mapping.findForward("returnDispatch");
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
	 * 跳转到驳回页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public ActionForward toPrepareRejectRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "咨询投诉驳回 >> 驳回");
//		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);

		ActionForward forward = null;
		display(form, request, errors, "display");

	 forward = mapping.findForward("advisoryComplaintsReject");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	/**
	 * 驳回
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toRejectRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String hql="";
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			AdvisoryComplaintsManage advisorycomplaintsmanage=(AdvisoryComplaintsManage) hs.get(AdvisoryComplaintsManage.class, (String)dform.get("processSingleNo"));
			String returnpath=request.getParameter("returnpath");
			if(advisorycomplaintsmanage!=null){
				if(returnpath!=null && returnpath.trim().equals("1")){
					//驳回到分析总结
					advisorycomplaintsmanage.setAuditType("N");//分析总结标志
					advisorycomplaintsmanage.setIsClose("N");//是否关闭
					advisorycomplaintsmanage.setCompleteDate("");//完成日期
					advisorycomplaintsmanage.setProcessTime("");//处理用时
				}else if(returnpath!=null && returnpath.trim().equals("2")){
					//驳回到处理
					advisorycomplaintsmanage.setProcessType("R");//处理标志
					advisorycomplaintsmanage.setAuditType("N");//分析总结标志
					advisorycomplaintsmanage.setIsClose("N");//是否关闭
					advisorycomplaintsmanage.setCompleteDate("");//完成日期
					advisorycomplaintsmanage.setProcessTime("");//处理用时
				}
				
				hs.save(advisorycomplaintsmanage);
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"qualitycheckmanagement.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;
//		String isreturn = request.getParameter("isreturn");
		try {
			forward = mapping.findForward("returnRejectList");
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"update.success"));
			/*if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"update.success"));
				forward = mapping.findForward("returnRejectList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				forward = mapping.findForward("returnReject");
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	public ActionForward toReject(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		int len;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			AdvisoryComplaintsManage advisorycomplaintsmanage = (AdvisoryComplaintsManage) hs
					.get(AdvisoryComplaintsManage.class, (String) dform.get("id"));

			if (advisorycomplaintsmanage != null) {
				advisorycomplaintsmanage.setProcessType("R");
				advisorycomplaintsmanage.setAuditType("N");
				advisorycomplaintsmanage.setIsClose("N");
				hs.save(advisorycomplaintsmanage);
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.success"));
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"qualitycheckmanagement.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");

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
			forward = mapping.findForward("returnRejectList");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return forward;
	}
	
	/**
	 * 获取负责人弹出窗口列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrincipalSelectList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form, request, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport", "");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "principalList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fPrincipalSel");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("principalId");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String principalName = tableForm.getProperty("principalName");
			String principalId = tableForm.getProperty("principalId");
			Session hs = null;
			try {
				hs = HibernateUtil.getSession();

				Criteria criteria = hs.createCriteria(Principal.class);
				criteria.add(Expression.eq("enabledFlag", "Y"));
				if (principalId != null && !principalId.equals("")) {
					criteria.add(Expression.like("principalId", "%"
							+ principalId.trim() + "%"));
				}
				if (principalName != null && !principalName.equals("")) {
					criteria.add(Expression.like("principalName", "%"
							+ principalName.trim() + "%"));
				}
				if (table.getIsAscending()) {
					criteria.addOrder(Order.asc(table.getSortColumn()));
				} else {
					criteria.addOrder(Order.desc(table.getSortColumn()));
				}
				table.setVolume(criteria.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				criteria.setFirstResult(table.getFrom()); // pagefirst
				criteria.setMaxResults(table.getLength());

				cache.check(table);

				List principalList = criteria.list();

				table.addAll(principalList);
				session.setAttribute("principalList", table);

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
			forward = mapping.findForward("principalSelect");
		}
		return forward;
	}
	
	public void display(ActionForm form, HttpServletRequest request ,ActionErrors errors ,String flag){
		DynaActionForm dform = (DynaActionForm) form;

		String id =null;
		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("processSingleNo");
		} else {
			id = (String) dform.get("id");
		}
		Session hs = null;
		AdvisoryComplaintsManage manage = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				manage=(AdvisoryComplaintsManage) hs.get(AdvisoryComplaintsManage.class, id);
				String dutyDepar="";
				
				if(manage!=null){
					if(manage.getDutyDepar()!=null && manage.getDutyDepar().contains(",")){
						String[] comids=manage.getDutyDepar().split(",");
						if(comids!=null && comids.length>0){
							for(int i=0;i<comids.length;i++){
								dutyDepar+=i==comids.length-1 ?  bd.getName("Company", "comfullname", "comid", comids[i]) :  bd.getName("Company", "comfullname", "comid", comids[i])+",";
							}
						}
					}else{
						dutyDepar=bd.getName("Company", "comfullname", "comid", manage.getDutyDepar());
					}
					manage.setDutyDepar(dutyDepar);
					String qualityIndex="";
					if(manage.getQualityIndex()!=null && manage.getQualityIndex().contains(",")){
						String[] qualityIndexs=manage.getQualityIndex().split(",");
						if(qualityIndexs!=null && qualityIndexs.length>0){
							for(int i=0;i<qualityIndexs.length;i++){
								qualityIndex+=i==qualityIndexs.length-1 ? bd.getName_Sql("Pulldown", "pullname", "pullid", qualityIndexs[i]) : bd.getName_Sql("Pulldown", "pullname", "pullid", qualityIndexs[i])+",";
							}
						}
					}else{
						qualityIndex=bd.getName_Sql("Pulldown", "pullname", "pullid", manage.getQualityIndex());
					}
					manage.setQualityIndex(qualityIndex);
					manage.setReceivePer(bd.getName_Sql("Loginuser", "username", "userid", manage.getReceivePer()));
					manage.setProcessPer( bd.getName_Sql("Loginuser", "username", "userid", manage.getProcessPer()));
					if(manage.getAuditOperid()!=null && !manage.getAuditOperid().equals("")){
						manage.setAuditOperid(bd.getName_Sql("Loginuser", "username", "userid", manage.getAuditOperid()));
					}
					
					if(manage.getMessageSource().equals("400")){
						manage.setOctId( bd.getName_Sql("OfficeContractTypes", "octName", "octId", manage.getOctId()));
						manage.setOccId(bd.getName_Sql("OfficeComplaintCategory", "occName", "occId", manage.getOccId()));
					}
					
					if(manage.getMessageSource()!=null && manage.getMessageSource().equals("")){
						manage.setMessageSource(bd.getName_Sql("Pulldown", "pullname", "pullid", manage.getMessageSource()));
					}
					if(manage.getProcessDifficulty()!=null && !manage.getProcessDifficulty().equals("")){
						manage.setProcessDifficulty(bd.getName_Sql("Pulldown", "pullname", "pullid", manage.getProcessDifficulty()));
					}
					
					if(manage.getIssueSort1()!=null && manage.getIssueSort1().equals("GS")){
						manage.setIssueSort1(bd.getName_Sql("Pulldown", "pullname", "pullid", manage.getIssueSort1()));
						manage.setIssueSort2(bd.getName_Sql("Pulldown", "pullname", "pullid", manage.getIssueSort2()));
						manage.setIssueSort3(bd.getName_Sql("Pulldown", "pullname", "pullid", manage.getIssueSort3()));
						manage.setOcaId(bd.getName_Sql("OfficeCauseAnalysis", "ocaName", "ocaId", manage.getOcaId()));
					}else{
						if(manage.getIssueSort1()!=null && !manage.getIssueSort1().equals("")){
							manage.setIssueSort1(bd.getName_Sql("Pulldown", "pullname", "pullid", manage.getIssueSort1()));
						}
					}
					
					if(manage.getIssueSort4()!=null && !"".equals(manage.getIssueSort4())){
						manage.setIssueSort4(bd.getName_Sql("Pulldown", "pullname", "pullid", manage.getIssueSort4()));
					}
					
					if("display".equals(flag)){
						manage.setMessageSource(bd.getName_Sql("Pulldown", "pullname", "pullid", manage.getMessageSource()));
					}
				}
				
				if (manage == null) {
					errors.add(
							ActionErrors.GLOBAL_ERROR,
							new ActionError(
									"qualitycheckmanagement.display.recordnotfounterror"));
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

			request.setAttribute("advisoryComplaintsManageBean", manage);
		}
	}

}
