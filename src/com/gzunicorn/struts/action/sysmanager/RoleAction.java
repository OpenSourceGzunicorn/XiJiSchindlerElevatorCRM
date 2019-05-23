//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.gzunicorn.struts.action.sysmanager;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

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

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.sysmanager.Role;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * MyEclipse Struts Creation date: 07-19-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/roleAction" name="roleForm" scope="request"
 *                validate="true"
 */
public class RoleAction extends DispatchAction {
	Log log = LogFactory.getLog(RoleAction.class);

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

		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "role", null);
		/** **********结束用户权限过滤*********** */
		
		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		//System.out.println(name);
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			ActionForward forward = super.execute(mapping, form, request,
					response);
			//System.out.println(forward.getName());
			return forward;
		}

	}
	
	/**
	 * Get the navigation description from the properties file by navigation key;
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

		String naigation = new String();
		this.setNavigation(request, "navigator.base.role.add");

		DynaActionForm dform = (DynaActionForm) form;
		// dform.reset(mapping,null);
		if(request.getAttribute("error") == null || request.getAttribute("error").equals(""))
		{
			dform.initialize(mapping);
			dform.set("enabledflag","Y");
		}
		dform.set("enabledflag","Y");
		java.util.List result = new java.util.ArrayList();

		try {
			result = bd.getModuleList();
		} catch (Exception e) {
			log.error(e.getMessage());
			DebugUtil.print(e, "Get ModuleList error!");
		}
		request.setAttribute("moduleList", result);
		request.setAttribute("viewallowflagList",bd.getViewAllowFlagList());
		return mapping.findForward("roleAdd");
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
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			Role role = new Role();
			role.setRoleid((String) dform.get("roleid"));
			role.setRolename((String) dform.get("rolename"));
			role.setModuleid((String) dform.get("moduleid"));
			role.setAllowflag((String) dform.get("allowflag"));
			role.setEnabledflag((String) dform.get("enabledflag"));
			role.setRem1((String) dform.get("rem1"));
			hs.save(role);
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"role.insert.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Role Insert error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Role Insert error!");
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
				if(errors.isEmpty())
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"insert.success"));
				}
				else
				{
					request.setAttribute("error","Yes");
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
			Role role = (Role) hs.get(Role.class, (String) dform.get("id"));
			if(dform.get("id") != null && dform.get("roleid") != null && !((String)dform.get("id")).equals((String)dform.get("roleid")))
			{
				hs.delete(role);
				role = new Role();
			}
			role.setRoleid((String) dform.get("roleid"));
			role.setRolename((String) dform.get("rolename"));
			role.setModuleid((String) dform.get("moduleid"));
			role.setAllowflag((String) dform.get("allowflag"));
			role.setEnabledflag((String) dform.get("enabledflag"));
			role.setRem1((String) dform.get("rem1"));
			hs.save(role);
			
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"role.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Role Update error!");
		}
		catch (DataStoreException e1) {
			e1.printStackTrace();
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

		//if (!errors.isEmpty()) {
		//	saveErrors(request, errors);
		//}

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if(errors.isEmpty())
				{
				dform.set("id", dform.get("roleid"));
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
				"update.success"));
				}
				else
				{
					request.setAttribute("error","Yes");
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
		this.setNavigation(request, "navigator.base.role.modify");
		ActionForward forward = null;
		String id = null;
		if(dform.get("isreturn") != null && dform.get("isreturn").equals("N"))
		{
			id = (String) dform.get("roleid");
		}
		else
		{
			id = (String) dform.get("id");
		}
		
		Session hs = null;
		if (id != null) {
			if(request.getAttribute("error") == null || request.getAttribute("error").equals(""))
			{
			try {
				hs = HibernateUtil.getSession();
				Query query = hs
						.createQuery("from Role a where a.roleid = :roleid");
				query.setString("roleid", id);
				java.util.List list = query.list();
				if (list != null && list.size() > 0) {
					Role role = (Role) list.get(0);
					dform.set("id", role.getRoleid());
					dform.set("roleid", role.getRoleid());
					dform.set("rolename", role.getRolename());
					dform.set("moduleid", role.getModuleid());
					dform.set("allowflag", role.getAllowflag());
					dform.set("rem1", role.getRem1());
					dform.set("enabledflag", role.getEnabledflag());
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"role.display.recordnotfounterror"));
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
			}

			request.setAttribute("moduleList", bd.getModuleList());
			request.setAttribute("viewallowflagList",bd.getViewAllowFlagList());
			forward = mapping.findForward("roleModify");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * Method toDeleteRecord execute, Delete record where roleid = id
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
			Role role = (Role) hs.get(Role.class, (String) dform.get("id"));
			if(role != null)
			{
			 hs.delete(role);
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"role.delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Role Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
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
		this.setNavigation(request, "navigator.base.role.list");
		ActionForward forward = null;
		// listRecord
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				
				response = toExcelRecord(form,request,response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "roleList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fRole");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("roleid");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String rolename = tableForm.getProperty("rolename");
			String roleid = tableForm.getProperty("roleid");
			String enabledflag = tableForm.getProperty("enabledflag");
			Session hs = null;
			try {

				hs = HibernateUtil.getSession();

				Criteria criteria = hs.createCriteria(Role.class);
				if (roleid != null && !roleid.equals("")) {
					criteria.add(Expression.like("roleid", "%" + roleid.trim() + "%"));
				}
				if (rolename != null && !rolename.equals("")) {
					criteria.add(Expression.like("rolename", "%" + rolename.trim()
							+ "%"));
				}
				if (enabledflag != null && !enabledflag.equals("")) {
					criteria.add(Expression.eq("enabledflag", enabledflag));
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

				List role = criteria.list();
				BaseDataImpl bd = new BaseDataImpl();

				if (role != null) {
					int len = role.size();
					for (int i = 0; i < len; i++) {
						Role r = (Role) role.get(i);
						r.setModuleid(bd.getName("Module", "modulename","moduleid", r.getModuleid()));
						r.setAllowflag(bd.getName("Viewallowflag","flagname","flagid",r.getAllowflag()));
					}
				}

				table.addAll(role);
				session.setAttribute("roleList", table);

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

			forward = mapping.findForward("roleList");
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
		this.setNavigation(request, "navigator.base.role.view");

		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		Role role = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				role = (Role) hs.get(Role.class, (String) dform.get("id"));

				if (role == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"role.display.recordnotfounterror"));
				} else {
					role.setModuleid(bd.getName("Module", "modulename","moduleid", role.getModuleid()));
					role.setAllowflag(bd.getName("Viewallowflag","flagname","flagid",role.getAllowflag()));
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
			request.setAttribute("roleBean", role);
			forward = mapping.findForward("roleDisplay");
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


		String roleid = tableForm.getProperty("roleid");
		String rolename = tableForm.getProperty("rolename");
		String enabledflag = tableForm.getProperty("enabledflag");
		Session hs = null;

		XSSFWorkbook xwb = new XSSFWorkbook();

		try {
			
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(Role.class);
			if (roleid != null && !roleid.equals("")) {
				criteria.add(Expression.like("roleid", "%" + roleid.trim() + "%"));
			}
			if (rolename != null && !rolename.equals("")) {
				criteria.add(Expression.like("rolename", "%" + rolename.trim() + "%"));
			}
			if (enabledflag != null && !enabledflag.equals("")) {
				criteria.add(Expression.eq("enabledflag", enabledflag));
			}

			criteria.addOrder(Order.asc("roleid"));

			List roleList = criteria.list();
			
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			XSSFSheet sheet = xwb.createSheet("用户角色");
			
			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"role.roleid"));
				
				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"role.rolename"));
		
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"role.moduleid"));
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"role.allowflag"));
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"role.enabledflag"));
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"role.rem1"));
				
				for (int i = 0; i < l; i++) {
					Role role = (Role) roleList.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);
	
					// 创建Excel列
					XSSFCell cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(role.getRoleid());
					
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(role.getRolename());
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String modulename="";
					if(role.getModuleid()!=null){
						modulename=bd.getName("Module", "modulename","moduleid", role.getModuleid());
					}
					cell.setCellValue(modulename);

					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String allowname="";
					if(role.getAllowflag()!=null){
						allowname=bd.getName("Viewallowflag","flagname","flagid",role.getAllowflag());
					}
					cell.setCellValue(allowname);
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(role.getEnabledflag()));
					
					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(role.getRem1());

				}
			}
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				if(hs!=null){
					hs.close();
				}
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("用户角色", "utf-8") + ".xlsx");
		xwb.write(response.getOutputStream());
				
		return response;
	}

}