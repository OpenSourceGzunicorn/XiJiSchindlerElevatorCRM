package com.gzunicorn.struts.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.ArrayConfig;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.sysmanager.Workspacebaseproperty;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * 设备属性信息表表操作类
 * 
 * @version 1.0
 * @author Administrator
 * 
 */
public class WorkSpaceBaseFitAction extends DispatchAction {
	/**
	 * 日志类对象
	 */
	Log log = LogFactory.getLog(WorkSpaceBaseFitAction.class);

	/**
	 * 基础数据操作对象
	 */
	BaseDataImpl bd = new BaseDataImpl();

	/**
	 * Method execute 由 Struts-config.XML 跳转过来; 判断 执行小页面查询 还是 大页面查询； 用户权限控制；
	 * 后台调试： 打印执行的方法
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
				SysRightsUtil.NODE_ID_FORWARD + "workSpaceBaseFitSearch", null);
		/** **********结束用户权限过滤*********** */

		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			String uri = request.getRequestURI();
			if (uri.indexOf("WorkSpaceBaseFitMiniSearchAction") != -1) {
				name = "toSearchMiniRecord";
			} else {
				name = "toSearchRecord";
			}
		}
		DebugUtil.printDoBaseAction("WorkSpaceBaseFitSearchAction", name, "start");
		ActionForward forward = dispatchMethod(mapping, form, request,
				response, name);
		DebugUtil.printDoBaseAction("WorkSpaceBaseFitSearchAction", name, "end");
		return forward;
	}

	/**
	 * Get the navigation description from the properties file by navigation
	 * key; 导航条
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
	 * Method toPrepareAddRecord execute,prepare data for add page
	 * 进入新增页面，准备保存前要显示的数据或页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws HibernateException 
	 */

	public ActionForward toPrepareAddRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, HibernateException {

		this.setNavigation(request, "navigator.base.workspacebasefit.add");
		DynaActionForm dform = (DynaActionForm) form;
		dform.set("enabledflag","Y");
		
		Session hs = null;
		String sql = "From Workspacebaseproperty order by numno desc";
		try {
			hs = HibernateUtil.getSession();
			List list = hs.createQuery(sql).list();
			HashMap map = new HashMap();
			if(list!=null && list.size()>0){
				Workspacebaseproperty workspacebaseproperty = (Workspacebaseproperty) list.get(0);
				dform.set("numno", workspacebaseproperty.getNumno()+1);
			}else{
				dform.set("numno", 1);
			}
		} catch (DataStoreException e) {
			log.error(e.getMessage());
			DebugUtil.print(e);
		} catch (Exception e1) {
			log.error(e1.getMessage());
			DebugUtil.print(e1);
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex);
			}
		}


		return mapping.findForward("workspacebasefitAdd");
	}

	/**
	 * Method toAddRecord execute,Add data to database and return list page or
	 * add page 新增页面的保存方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toAddRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		
		String today = "";
		String year ="";
		try {
			today = CommonUtil.getToday();
			year = today.substring(2,4);
		} catch (ParseException e2) {
			e2.printStackTrace();
		} 
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			String jnlno = CommonUtil.getQuoteNO(year,year+SysConfig.WORKSPACEBASEFIT_JNLNO_FLAG,6,"Y","Y",4);
			//System.out.println(">>>"+jnlno);
			
			Workspacebaseproperty workspacebaseproperty = new Workspacebaseproperty();
			workspacebaseproperty.setWsid((String) jnlno);
			workspacebaseproperty.setWskey(dform.get("wskey").toString().trim());
			workspacebaseproperty.setTitle(dform.get("title").toString().trim());
			workspacebaseproperty.setLink(dform.get("link").toString().trim());
			workspacebaseproperty.setTip(dform.get("tip").toString().trim());
			workspacebaseproperty.setDivid(dform.get("divid").toString().trim());
			workspacebaseproperty.setJsfuname(dform.get("jsfuname").toString().trim());
			workspacebaseproperty.setEnabledflag(dform.get("enabledflag").toString().trim());
			workspacebaseproperty.setNumno((Integer) dform.get("numno"));
			
			hs.save(workspacebaseproperty);

			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"proc.insert.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3);
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2);
		} catch (DataStoreException e1) {
			log.error(e1.getMessage());
			DebugUtil.print(e1);
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex);
			}
		}

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				forward = mapping.findForward("returnList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"insert.success"));
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"insert.error"));
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
	 * Method toUpdateRecord execute,Update data to database and return list
	 * page or modifiy page 修改页面的保存方法
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

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		String id = (String) dform.get("id");
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			Workspacebaseproperty workspacebaseproperty = (Workspacebaseproperty) hs.get(Workspacebaseproperty.class, id);

			if(workspacebaseproperty != null){
				workspacebaseproperty.setWskey(dform.get("wskey").toString().trim());
				workspacebaseproperty.setTitle(dform.get("title").toString().trim());
				workspacebaseproperty.setLink(dform.get("link").toString().trim());
				workspacebaseproperty.setTip(dform.get("tip").toString().trim());
				workspacebaseproperty.setDivid(dform.get("divid").toString().trim());
				workspacebaseproperty.setJsfuname(dform.get("jsfuname").toString().trim());
				workspacebaseproperty.setEnabledflag(dform.get("enabledflag").toString().trim());
				workspacebaseproperty.setNumno((Integer) dform.get("numno"));
			
				hs.save(workspacebaseproperty);
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3);
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2);
		} catch (DataStoreException e1) {
			log.error(e1.getMessage());
			DebugUtil.print(e1);
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex);
			}
		}

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				} else {
					request.setAttribute("error", "Yes");
				}

				dform.set("wsid", id);
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
	 * 进入修改页面，准备保存前要显示的数据或页面
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
		this.setNavigation(request, "navigator.base.workspacebasefit.modify");
		ActionForward forward = null;
		String id = null;
		if (dform.get("isreturn") != null && dform.get("isreturn").equals("N")) {
			id = (String) dform.get("wsid");
		} else {
			id = (String) dform.get("id");
		}
		
		Session hs = null;
		if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					Map map1 = new HashMap();
					List listshow = new ArrayList();
					String sql = "Select a From Workspacebaseproperty a where a.wsid=:baseid ";

					Query query = hs.createQuery(sql);
					query.setString("baseid", id);
					java.util.List list = query.list();
					if (list != null && list.size() > 0) {
						Workspacebaseproperty workspacebaseproperty = (Workspacebaseproperty) list.get(0);
						String ii = workspacebaseproperty.getDivid();
						dform.set("wsid", workspacebaseproperty.getWsid());
						dform.set("wskey", workspacebaseproperty.getWskey());
						dform.set("title", workspacebaseproperty.getTitle());
						dform.set("link", workspacebaseproperty.getLink());
						dform.set("tip", workspacebaseproperty.getTip());
						dform.set("divid", workspacebaseproperty.getDivid());
						dform.set("jsfuname", workspacebaseproperty.getJsfuname());
						dform.set("enabledflag", workspacebaseproperty.getEnabledflag());
						dform.set("numno", workspacebaseproperty.getNumno());
					} else {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
					}


				} catch (DataStoreException e) {
					log.error(e.getMessage());
					DebugUtil.print(e, "HibernateUtil Hibernate Session ");
				} catch (Exception e1) {
					log.error(e1.getMessage());
					DebugUtil.print(e1, "HibernateUtil Hibernate Session ");
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

			forward = mapping.findForward("workspacebasefitModify");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * Method toDeleteRecord execute, Delete record where procid = id 删除方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDeleteRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			Workspacebaseproperty wsb = (Workspacebaseproperty) hs.get(Workspacebaseproperty.class, (String) dform.get("id"));
			if(wsb!=null){
				hs.delete(wsb);
			}
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.succeed"));
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Role Update error!");
		} catch (DataStoreException e1) {
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Role Update error!");

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
			log.error(e.getMessage());
			DebugUtil.print(e);
		}

		return forward;
	}

//	/**
//	 * Method toSearchMiniRecord execute, Search record 查询小页面
//	 * 
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return ActionForward
//	 */
//
//	public ActionForward toSearchMiniRecord(ActionMapping mapping,
//			ActionForm form, HttpServletRequest request,
//			HttpServletResponse response) {
//		this.setNavigation(request, "navigator.base.equipmentproperty.list");
//		ActionForward forward = null;
//		// listRecord
//		HttpSession session = request.getSession();
//		ServeTableForm tableForm = (ServeTableForm) form;
//		String action = tableForm.getAction();
//
//		if (tableForm.getProperty("genReport") != null
//				&& !tableForm.getProperty("genReport").equals("")) {
//			try {
//
//				response = toExcelRecord(form, request, response);
//				forward = mapping.findForward("exportExcel");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		} else {
//			HTMLTableCache cache = new HTMLTableCache(session, "equipmentpropertyMiniList");
//
//			DefaultHTMLTable table = new DefaultHTMLTable();
//			table.setMapping("fSearchEquipmentpropertyMinilist");
//			table.setLength(SysConfig.HTML_TABLE_MINI_LENGTH);
//			table.setSortColumn("a.psid");
//			table.setIsAscending(true);
//			cache.updateTable(table);
//
//			if (action.equals(ServeTableForm.NAVIGATE)
//					|| action.equals(ServeTableForm.SORT)) {
//				cache.loadForm(tableForm);
//			} else {
//				table.setFrom(0);
//			}
//			cache.saveForm(tableForm);
//
//			String psid = tableForm.getProperty("psid");
//			String propername = tableForm.getProperty("propername");
//			String enabledflag = tableForm.getProperty("enabledflag");
//			String intype = tableForm.getProperty("intype");
//
//			String sql0 = "Select distinct count(a.sid) ";
//			String sql1 = "Select a ";
//			String sql = "From Equipmentproperty a Where 1=1 ";
//			String order = "";
//			if (psid != null && psid.length() > 0) {
//				sql += "and a.sid like '%" + psid.trim() + "%' ";
//			}
//			if (enabledflag != null && enabledflag.length() > 0) {
//				sql += "and a.enabledflag like '%" + enabledflag.trim() + "%' ";
//			}
//			if (propername != null && propername.length() > 0) {
//				sql += "and a.propername like '%" + propername.trim() + "%' ";
//			}
//			if (intype != null && intype.length() > 0) {
//				sql += "and a.intype like '%" + intype.trim() + "%' ";
//			}
//
//			if (table.getIsAscending()) {
//				order = "order by " + table.getSortColumn();
//			} else {
//				order = "order by " + table.getSortColumn() + " desc";
//			}
//			Session hs = null;
//
//			try {
//
//				hs = HibernateUtil.getSession();
//
//				Query query = hs.createQuery(sql0 + sql);
//
//				int count = Integer.parseInt(query.list().get(0).toString());
//				table.setVolume(count);// 查询得出数据记录数;
//
//				// 得出上一页的最后一条记录数号;
//				query = hs.createQuery(sql1 + sql + order);
//				query.setFirstResult(table.getFrom()); // pagefirst
//				query.setMaxResults(table.getLength());
//
//				cache.check(table);
//
//				List list = query.list();
//				List rslist = new ArrayList();
//
//				table.addAll(list);
//				//System.out.println(list.size());
//				session.setAttribute("BasedatalistMiniList", table);
//
//			} catch (DataStoreException e) {
//				log.error(e.getMessage());
//				DebugUtil.print(e);
//			} catch (HibernateException e1) {
//				log.error(e1.getMessage());
//				DebugUtil.print(e1);
//			} finally {
//				try {
//					hs.close();
//				} catch (HibernateException hex) {
//					log.error(hex.getMessage());
//					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
//				}
//			}
//
//			forward = mapping.findForward("BaseDataListMiniList");
//		}
//
//		return forward;
//	}

	/**
	 * Method toDisplayRecord execute,prepare data for update page 查看页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		// set navigation;
		this.setNavigation(request, "navigator.base.workspacebasefit.view");

		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		if (id != null) {
			String sql = "Select a From Workspacebaseproperty a where a.wsid=:wsid ";
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery(sql);
				query.setString("wsid", id);
				List list = query.list();
				HashMap map = new HashMap();
				
				if (list == null || list.size() == 0) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"role.display.recordnotfounterror"));
				} else {
					Workspacebaseproperty workspacebaseproperty = (Workspacebaseproperty) list.get(0);
					map.put("wsid", workspacebaseproperty.getWsid());
					map.put("wskey", workspacebaseproperty.getWskey());

					map.put("title", workspacebaseproperty.getTitle());
					map.put("link", workspacebaseproperty.getLink());
					map.put("tip", workspacebaseproperty.getTip());
					map.put("enabledflag", workspacebaseproperty.getEnabledflag());
					map.put("divid", workspacebaseproperty.getDivid());
					map.put("jsfuname", workspacebaseproperty.getJsfuname());
					map.put("numno", workspacebaseproperty.getNumno());

				}
				request.setAttribute("basedatabean", map);
			} catch (DataStoreException e) {
				log.error(e.getMessage());
				DebugUtil.print(e);
			} catch (Exception e1) {
				log.error(e1.getMessage());
				DebugUtil.print(e1);
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex);
				}
			}

			// if display = yes then the page just show the return button;
			String type = request.getParameter("type");
			if (type != null && type.equalsIgnoreCase("mini")) {
				request.setAttribute("display", "yes");
				forward = mapping.findForward("workspacebasefitMiniDisplay");
			} else {
				request.setAttribute("display", "yes");
				forward = mapping.findForward("workspacebasefitDisplay");
			}

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * Method toSearchRecord execute, to Excel Record 列表查询导出Excel 导出EXCEL方法
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

		ServeTableForm tableForm = (ServeTableForm) form;

		String wsid = tableForm.getProperty("wsid");
		String wskey = tableForm.getProperty("wskey");
		String title = tableForm.getProperty("title");
		String link = tableForm.getProperty("link");
		String enabledflag = tableForm.getProperty("enabledflag");
		String divid = tableForm.getProperty("divid");
		String jsfuname = tableForm.getProperty("jsfuname");

		String sql = "Select a From Workspacebaseproperty a  Where 1=1 ";
		String order = "";
		if (wsid != null && wsid.length() > 0) {
			sql += "and a.wsid like '%" + wsid.trim() + "%' ";
		}
		if (enabledflag != null && enabledflag.length() > 0) {
			sql += "and a.enabledflag like '%" + enabledflag.trim() + "%' ";
		}
		if (wskey != null && wskey.length() > 0) {
			sql += "and a.wskey like '%" + wskey.trim() + "%' ";
		}
		if (title != null && title.length() > 0) {
			sql += "and a.title like '" + title.trim() +"'";
		}
		if (link != null && link.length() > 0) {
			sql += "and a.link like '" + link.trim() +"'";
		}
		if (divid != null && divid.length() > 0) {
			sql += "and a.divid like '" + divid.trim() +"'";
		}
		if (jsfuname != null && jsfuname.length() > 0) {
			sql += "and a.jsfuname like '" + jsfuname.trim() +"'";
		}


		order = "order by a.wsid asc";

		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Query query = hs.createQuery(sql + order);

			List list = query.list();

			XSSFSheet sheet = wb.createSheet("工作区基本属性");

			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			XSSFRow row0 = sheet.createRow( 0);
			XSSFCell cell0 = row0.createCell((short) 0);
			//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell0.setCellValue(messages.getMessage(locale, "Track.jnlno"));

			cell0 = row0.createCell((short) 1);
			//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell0.setCellValue(messages.getMessage(locale,"workspacebasefit.wskey"));

			cell0 = row0.createCell((short) 2);
			//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell0.setCellValue(messages.getMessage(locale, "randommaintaskmaster.title"));

			cell0 = row0.createCell((short) 3);
			//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell0.setCellValue(messages.getMessage(locale, "workspacebasefit.link"));

			cell0 = row0.createCell((short) 4);
			//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell0.setCellValue(messages.getMessage(locale, "workspacebasefit.tip"));

			cell0 = row0.createCell((short) 5);
			//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell0.setCellValue(messages.getMessage(locale, "DivId"));
			
			cell0 = row0.createCell((short) 6);
			//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell0.setCellValue(messages.getMessage(locale, "JSfuName"));
			
			cell0 = row0.createCell((short) 7);
			//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell0.setCellValue(messages.getMessage(locale, "role.enabledflag"));
			
			if (list != null && !list.isEmpty()) {
				int l = list.size();
				for (int i = 0; i < l; i++) {
					Workspacebaseproperty map = (Workspacebaseproperty) list.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i + 1);

					// 创建Excel列
					XSSFCell cell = row.createCell((short) 0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.getWsid());

					cell = row.createCell((short) 1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.getWskey());

					cell = row.createCell((short) 2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.getTitle());

					cell = row.createCell((short) 3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.getLink());

					cell = row.createCell((short) 4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.getTip());

					cell = row.createCell((short) 5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.getDivid());
					
					cell = row.createCell((short) 6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.getJsfuname());
					
					cell = row.createCell((short) 7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String enabledname="否";
					if(map.getEnabledflag()!=null && !map.getEnabledflag().trim().equals("")){
						enabledname="是";
					}
					cell.setCellValue(enabledname);
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("工作区基本属性", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}

	/**
	 * Method toSearchRecord execute, Search record 查询页面，显示数据，可根据条件进行相应的查询
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		this.setNavigation(request, "navigator.base.workspacebasefitsearch.list");
		ActionForward forward = null;
		// listRecord
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {

				response = toExcelRecord(form, request, response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "workspacebasefitList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fSearchworkspacebasefitlist");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			table.setSortColumn("wsid");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String wsid = tableForm.getProperty("wsid");
			String wskey = tableForm.getProperty("wskey");
			String title = tableForm.getProperty("title");
			String link = tableForm.getProperty("link");
			String tip = tableForm.getProperty("tip");
			String enabledflag = tableForm.getProperty("enabledflag");
			String divid = tableForm.getProperty("divid");
			String jsfuname = tableForm.getProperty("jsfuname");
			
			String sql0 = "select count(*) ";
			String sql1 = "select a ";
			String sql = "From Workspacebaseproperty a Where 1=1 ";
			String order = "";
			if (wsid != null && wsid.length() > 0) {
				sql += "and a.wsid like '%" + wsid.trim() + "%' ";
			}
			if (enabledflag != null && enabledflag.length() > 0) {
				sql += "and a.enabledflag like '%" + enabledflag.trim() + "%' ";
			}
			if (wskey != null && wskey.length() > 0) {
				sql += "and a.wskey like '%" + wskey.trim() + "%' ";
			}
			if (title != null && title.length() > 0) {
				sql += "and a.title like '%" + title.trim() +"%' ";
			}
			if (tip != null && tip.length() > 0) {
				sql += "and a.tip like '%" + tip.trim() +"%' ";
			}
			if (link != null && link.length() > 0) {
				sql += "and a.link like '%" + link.trim() +"%' ";
			}
			if (link != null && link.length() > 0) {
				sql += "and a.divid like '%" + divid.trim() +"%' ";
			}
			if (link != null && link.length() > 0) {
				sql += "and a.jsfuname like '%" + jsfuname.trim() +"%' ";
			}

			if (table.getIsAscending()) {
				order = "order by " + table.getSortColumn();
			} else {
				order = "order by " + table.getSortColumn() + " desc";
			}
			Session hs = null;

			try {
				
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("select count(*) From Workspacebaseproperty ");
				
				List listq = query.list();
				if(listq!= null && listq.size() > 0){
					table.setVolume(Integer.parseInt(listq.get(0).toString()));// 查询得出数据记录数;
				}else{
					table.setVolume(0);// 查询得出数据记录数;
				}

				// 得出上一页的最后一条记录数号;
				query = hs.createQuery(sql1 + sql + order);
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();

				table.addAll(list);
				
				session.setAttribute("workspacebasefitList", table);

			} catch (DataStoreException e) {
				log.error(e.getMessage());
				DebugUtil.print(e);
			} catch (HibernateException e1) {
				log.error(e1.getMessage());
				DebugUtil.print(e1);
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}

			forward = mapping.findForward("workspacebasefitlist");
		}

		return forward;
	}
}