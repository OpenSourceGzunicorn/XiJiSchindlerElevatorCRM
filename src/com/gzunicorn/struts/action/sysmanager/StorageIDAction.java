//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.gzunicorn.struts.action.sysmanager;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

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
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * MyEclipse Struts Creation date: 08-09-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/storageidAction" name="storageidForm" scope="request"
 *                validate="true"
 */
public class StorageIDAction extends DispatchAction {

	Log log = LogFactory.getLog(StorageIDAction.class);
	
	BaseDataImpl bd = new BaseDataImpl();

	// --------------------------------------------------------- Instance
	// Variables

	// --------------------------------------------------------- Methods

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
		SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "storageId",null);
		/************结束用户权限过滤************/

		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		////System.out.println(name);
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
	 * Method toPrepareAddRecord execute,prepare data for add page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toPrepareAddRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
        
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		if(userInfo.getModuleID() !=null || !userInfo.getModuleID().equals("")){
			request.setAttribute("canchange","Yes");
		}
		String naigation = new String();
		this.setNavigation(request, "navigator.base.storageid.add");

		DynaActionForm dform = (DynaActionForm) form;
		// dform.reset(mapping,null);
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
			dform.set("enabledflag", "Y");
		}

		request.setAttribute("viewstoragerefList",bd.getViewStorageRefList());
		request.setAttribute("viewcompanyList",bd.getCompanyList());
		request.setAttribute("viewstoragetypeList",bd.getViewStorageType());
		return mapping.findForward("storageidAdd");
	}

	/**
	 * Method toAddRecord execute,Add data to database and return list page or
	 * add page
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
		String parentstorageid=(String) dform.get("parentstorageid");
		String[] sid=new String[1];
		if(parentstorageid.equals("0")){
			sid=CommonUtil.getJnlNo("00","1",4,1,"Y");
		}
		else{
			sid=CommonUtil.getJnlNo("00",parentstorageid,2,1,"Y");
		}
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			Storageid storageid = new Storageid();
			String staid=new String(sid[0].toString().getBytes("iso-8859-1"),"GBK");
			storageid.setStorageid((String) staid);
			storageid.setStoragename((String) dform.get("storagename"));
			storageid.setComid((String) dform.get("comid"));
			storageid.setParentstorageid((String) dform.get("parentstorageid"));
			storageid.setStoragehead((String) dform.get("storagehead"));
			storageid.setStoragetype((String) dform.get("storagetype"));
			storageid.setPhone((String) dform.get("phone"));
			storageid.setFax((String) dform.get("fax"));
			storageid.setEmail((String) dform.get("email"));
			storageid.setEnabledflag((String) dform.get("enabledflag"));
			storageid.setRem((String) dform.get("rem"));
			hs.save(storageid);
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"storageid.insert.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Storageid Insert error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Storageid Insert error!");
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

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			Storageid storageid = (Storageid) hs.get(Storageid.class, (String) dform.get("id"));
			if (dform.get("id") != null
					&& dform.get("storageid") != null
					&& !((String) dform.get("id")).equals((String) dform
							.get("storageid"))) {
				hs.delete(storageid);
				storageid = new Storageid();
			}
			storageid.setStorageid((String) dform.get("storageid"));
			storageid.setStoragename((String) dform.get("storagename"));
			storageid.setComid((String) dform.get("comid"));
			storageid.setParentstorageid((String) dform.get("parentstorageid"));
			storageid.setStoragehead((String) dform.get("storagehead"));
			storageid.setStoragetype((String) dform.get("storagetype"));
			storageid.setPhone((String) dform.get("phone"));
			storageid.setFax((String) dform.get("fax"));
			storageid.setEmail((String) dform.get("email"));
			storageid.setEnabledflag((String) dform.get("enabledflag"));
			storageid.setRem((String) dform.get("rem"));
			hs.save(storageid);

			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"storageid.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Storageid Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Storageid Update error!");
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
		// set navigation;
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		String naigation = new String();
		this.setNavigation(request, "navigator.base.storageid.modify");
		ActionForward forward = null;
		String id = null;

		BaseDataImpl bdi = new BaseDataImpl();
		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("storageid");
		} else {
			id = (String) dform.get("id");
		}

		Session hs = null;
		if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					Query query = hs
							.createQuery("from Storageid a where a.storageid = :storageid");
					query.setString("storageid", id);
					java.util.List list = query.list();
					if (list != null && list.size() > 0) {
						Storageid storageid = (Storageid) list.get(0);
						dform.set("id", storageid.getStorageid());
						dform.set("storageid", storageid.getStorageid());
						dform.set("storagename",storageid.getStoragename());
						dform.set("comid", storageid.getComid());
						dform.set("parentstorageid", storageid.getParentstorageid());
						dform.set("storagehead", storageid.getStoragehead());
						dform.set("storagetype", storageid.getStoragetype());
						dform.set("phone", storageid.getPhone());
						dform.set("fax", storageid.getFax());
						dform.set("email", storageid.getEmail());
						dform.set("rem", storageid.getRem());
						dform.set("enabledflag", storageid.getEnabledflag());
					} else {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"display.recordnotfounterror"));
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
			request.setAttribute("viewstoragerefList",bd.getViewStorageRefList());
			request.setAttribute("viewcompanyList",bd.getCompanyList());
			request.setAttribute("viewstoragetypeList",bd.getViewStorageType());
			forward = mapping.findForward("storageidModify");
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
			Storageid storageid = (Storageid) hs.get(Storageid.class, (String) dform.get("id"));
			if(storageid != null)
			{
			 hs.delete(storageid);
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
			DebugUtil.print(e2, "Hibernate Storageid Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Storageid Update error!");

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
		this.setNavigation(request, "navigator.base.storageid.list");
		ActionForward forward = null;
		// listRecord
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		ViewLoginUserInfo userinfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

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
			HTMLTableCache cache = new HTMLTableCache(session, "storageidList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fStorageID");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("storageid");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String storagename = tableForm.getProperty("storagename");
			String storageid = tableForm.getProperty("storageid");
			String enabledflag = tableForm.getProperty("enabledflag");
			String comid = tableForm.getProperty("grcid");// 所属分部代码

			Session hs = null;

			try {

				hs = HibernateUtil.getSession();
								
				StringBuffer columnNames = new StringBuffer();
				columnNames.append("a.StorageID,");
				columnNames.append("a.StorageName,");
				columnNames.append("a.StorageHead,");
				columnNames.append("a.Phone,");
				columnNames.append("b.ComName,");
				columnNames.append("a.EnabledFlag");
				
				String sql = "select "+columnNames+" from StorageID a,Company b where a.ComID = b.ComID";
				String order = "";
				
				if (storagename != null && !storagename.equals("")) {
					sql+= " and a.StorageName like '%"+storagename.trim()+"%'";
				}
				
				if (storageid != null && !storageid.equals("")) {
					sql+= " and a.StorageID like '%"+storageid.trim()+"%'";
				}
				
				if (enabledflag != null && !enabledflag.equals("")) {
					sql += " and a.EnabledFlag = '"+enabledflag.trim()+"'";
				}
				
				if (comid != null && !comid.equals("")) {
					//System.out.println(comid);
					sql += " and b.comid like '%"+comid.trim()+"%'";
				}
				
				
				
				if (table.getIsAscending()) {
					order = table.getSortColumn();
				} else {
					order = table.getSortColumn() + " desc";
				}
				
				sql += " order by "+order;
				//System.out.println(sql);
				
//				Criteria criteria = hs.createCriteria(Storageid.class);
//				if (storageid != null && !storageid.equals("")) {
//					criteria.add(Expression.like("storageid", "%" + storageid.trim() + "%"));
//				}
//				if (storagename != null && !storagename.equals("")) {
//					criteria.add(Expression.like("storagename", "%" + storagename.trim()
//							+ "%"));
//				}
//				if (enabledflag != null && !enabledflag.equals("")){
//					criteria.add(Expression.eq("enabledflag", enabledflag));
//				}
//
//				if (table.getIsAscending()) {
//					criteria.addOrder(Order.asc(table.getSortColumn()));
//				} else {
//					criteria.addOrder(Order.desc(table.getSortColumn()));
//				}
//				table.setVolume(criteria.list().size());// 查询得出数据记录数;
//
//				// 得出上一页的最后一条记录数号;
//				criteria.setFirstResult(table.getFrom()); // pagefirst
//				criteria.setMaxResults(table.getLength());

				Query query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());
				query.setFirstResult(table.getFrom());
				query.setMaxResults(table.getLength());
				cache.check(table);

//				List storage = criteria.list();
				List resultList = query.list();
				List storage = new ArrayList();
				
				for (Object object : resultList) {
					Object[] obj = (Object[]) object;
					Map map = new HashMap();
					map.put("storageid", obj[0]+"");
					map.put("storagename", obj[1]+"");
					map.put("storagehead", obj[2]+"");
					map.put("phone", obj[3]+"");
					map.put("comname", obj[4]+"");
					map.put("enabledflag", obj[5]+"");	
					storage.add(map);
				}
				table.addAll(storage);
				request.setAttribute("grcidlist",Grcnamelist1.getgrcnamelist2(hs, userinfo.getUserID()));
				session.setAttribute("storageidList", table);

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (HibernateException e1) {

				e1.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
             
			forward = mapping.findForward("storageidList");
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

	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		// set navigation;
		String naigation = new String();
		this.setNavigation(request, "navigator.base.storageid.view");

		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		Storageid storageid = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				storageid = (Storageid) hs.get(Storageid.class, (String) dform.get("id"));

				if (storageid == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"storageid.display.recordnotfounterror"));
				}
				else
				{
					storageid.setComid(bd.getName("Company","comname","comid",storageid.getComid()));
					storageid.setParentstorageid(bd.getName("Viewstorageref","storagename","storageid",storageid.getParentstorageid()));
					storageid.setStoragetype(bd.getName("Viewstoragetype","typename","typeid",storageid.getStoragetype()));
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

			// if display = yes then the page just show the return button;
			request.setAttribute("display", "yes");
			request.setAttribute("storageidBean", storageid);
			forward = mapping.findForward("storageidDisplay");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
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

		String storageid = tableForm.getProperty("storageid");
		String storagename = tableForm.getProperty("storagename");
		String enabledflag = tableForm.getProperty("enabledflag");
		String comid = tableForm.getProperty("grcid");// 所属分部代码
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();
			
			String hql = "select a,b.comname from Storageid as a,Company as b where a.comid=b.comid";
			
			if (storagename != null && !storagename.equals("")) {
				hql+= " and a.storagename like '%"+storagename.trim()+"%'";
			}
			
			if (storageid != null && !storageid.equals("")) {
				hql+= " and a.storageid like '%"+storageid.trim()+"%'";
			}
			
			if (enabledflag != null && !enabledflag.equals("")) {
				hql += " and a.enabledflag = '"+enabledflag.trim()+"'";
			}
			
			if (comid != null && !comid.equals("")) {
				//System.out.println(comid);
				hql += " and b.comid like '%"+comid.trim()+"%'";
			}
			hql += " order by storageid";
			
			Query query = hs.createQuery(hql);
			
			List List = query.list();
			
			XSSFSheet sheet = wb.createSheet("部门信息");

			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (List != null && !List.isEmpty()) {
				int l = List.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = null;
				cell0 = row0.createCell((short) 0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale, "storageid.storageid"));

				cell0 = row0.createCell((short) 1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"storageid.storagename"));
				
				cell0 = row0.createCell((short) 2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"storageid.comid"));
				
				cell0 = row0.createCell((short) 3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"storageid.parentstorageid"));
				
				cell0 = row0.createCell((short) 4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"storageid.storagehead"));
				
				cell0 = row0.createCell((short) 5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"storageid.storagetype"));
				
				cell0 = row0.createCell((short) 6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"storageid.phone"));
				
				cell0 = row0.createCell((short) 7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"storageid.fax"));
				
				cell0 = row0.createCell((short) 8);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"storageid.email"));

				cell0 = row0.createCell((short) 9);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"storageid.enabledflag"));

				cell0 = row0.createCell((short) 10);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale, "storageid.rem"));

				for (int i = 0; i < l; i++) {
					Object[] object = (Object[]) List.get(i);//object[0]是Storageid对象,object[1]是所属分部名称
					Storageid obj = (Storageid) object[0];
					XSSFRow row = sheet.createRow( i + 1);
					XSSFCell cell = null;
					cell = row.createCell((short) 0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getStorageid());

					cell = row.createCell((short) 1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getStoragename());
					
					cell = row.createCell((short) 2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(object[1]+"");
					
					cell = row.createCell((short) 3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String parentname="";
					if(obj.getParentstorageid()!=null && !obj.getParentstorageid().trim().equals("")){
						parentname=bd.getName("Viewstorageref","storagename","storageid",obj.getParentstorageid());
					}
					cell.setCellValue(parentname);
					
					cell = row.createCell((short) 4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getStoragehead());
					
					cell = row.createCell((short) 5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String storagetypename="";
					if(obj.getStoragetype()!=null && !obj.getStoragetype().trim().equals("")){
						storagetypename=bd.getName("Viewstoragetype","typename","typeid",obj.getStoragetype());
					}
					cell.setCellValue(storagetypename);
					
					cell = row.createCell((short) 6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getPhone());
					
					cell = row.createCell((short) 7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getFax());
					
					cell = row.createCell((short) 8);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getEmail());
					
					cell = row.createCell((short) 9);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(obj
							.getEnabledflag()));

					cell = row.createCell((short) 10);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("部门信息", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}

}