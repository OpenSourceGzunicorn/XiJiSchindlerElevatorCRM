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

import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.Class1;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * MyEclipse Struts Creation date: 2005-10-31
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/unittypeAction" name="unittypeForm" scope="request"
 *                validate="true"
 */
public class Class1Action extends DispatchAction {

	Log log = LogFactory.getLog(Class1Action.class);

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
		SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "class1",null);
		/** **********结束用户权限过滤*********** */

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

	public ActionForward toPrepareAddClass1(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		String naigation = new String();
		this.setNavigation(request, "navigator.base.class1.add");
		DynaActionForm dform = (DynaActionForm) form;

		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
			dform.set("enabledflag", "Y");
			dform.set("r1", "N");
		}
		java.util.List result = new java.util.ArrayList();
		return mapping.findForward("returnAdd");
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

	public ActionForward toAddClass1(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			Class1 class1 = new Class1();
			class1.setClass1id((String) dform.get("class1id"));
			class1.setClass1name((String) dform.get("class1name"));
			class1.setLevelid((Integer) dform.get("levelid"));
			class1.setRem1((String) dform.get("rem1"));
			class1.setR1((String) dform.get("r1"));
			class1.setEnabledFlag((String) dform.get("enabledflag"));
			hs.save(class1);
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"unittype.insert.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate UnitType Insert error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate UnitType Insert error!");
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
	 * Method toUpdateRecord execute,Update data to database and return list
	 * page or modifiy page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward toUpdateClass1(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			Class1 class1 = (Class1) hs.get(Class1.class, (String) dform
					.get("id"));
			if (dform.get("id") != null
					&& dform.get("class1id") != null
					&& !((String) dform.get("id")).equals((String) dform
							.get("class1id"))) {
				hs.delete(class1);
				class1 = new Class1();
			}

			class1.setClass1id((String) dform.get("class1id"));
			class1.setClass1name((String) dform.get("class1name"));
			class1.setLevelid((Integer) dform.get("levelid"));
			class1.setRem1((String) dform.get("rem1"));
			class1.setR1((String) dform.get("r1"));
			class1.setEnabledFlag((String) dform.get("enabledflag"));

			hs.save(class1);

			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"unittype.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Unittype Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Unittype Update error!");
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
				forward = mapping.findForward("class1Modify");
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

	public ActionForward toPrepareUpdateClass1(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		// set navigation;
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		String naigation = new String();
		this.setNavigation(request, "navigator.base.class1.modify");
		ActionForward forward = null;
		String id = null;

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("class1id");
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
							.createQuery("from Class1 a where a.class1id = :class1id");
					query.setString("class1id", id);
					java.util.List list = query.list();
					if (list != null && list.size() > 0) {
						Class1 class1 = (Class1) list.get(0);
						dform.set("id", class1.getClass1id());
						dform.set("class1id", class1.getClass1id());
						dform.set("class1name", class1.getClass1name());
						dform.set("levelid", class1.getLevelid());
						dform.set("rem1", class1.getRem1());
						dform.set("r1", class1.getR1());
						dform.set("enabledflag", class1.getEnabledFlag());
					} else {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
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
			forward = mapping.findForward("class1Modify");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * Method toDeleteRecord execute, Delete record where typeid = id
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDeleteClass1(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			Class1 class1 = (Class1) hs.get(Class1.class, (String) dform
					.get("id"));
			////System.out.println(class1 + "nnn");
			if (class1 != null) {
				hs.delete(class1);
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
			DebugUtil.print(e2, "Hibernate Unittype Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Unittype Update error!");

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
		this.setNavigation(request, "navigator.base.class1.list");
		ActionForward forward = null;
		// listRecord
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {

				response = toExcelClass1(form, request, response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "class1List");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fClass1");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("class1id");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String class1id = tableForm.getProperty("class1id");
			String class1name = tableForm.getProperty("class1name");
			String enabledflag = tableForm.getProperty("enabledflag");
			Session hs = null;

			try {

				hs = HibernateUtil.getSession();
				Criteria criteria = hs.createCriteria(Class1.class);
				if (class1id != null && !class1id.equals("")) {
					criteria.add(Expression.like("class1id", "%"
							+ class1id.trim() + "%"));
				}
				if (class1name != null && !class1name.equals("")) {
					criteria.add(Expression.like("class1name", "%"
							+ class1name.trim() + "%"));
				}
				if (enabledflag != null && !enabledflag.equals("")) {
					criteria.add(Expression.eq("enabledFlag", enabledflag));
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

				List class1 = criteria.list();

				table.addAll(class1);
				session.setAttribute("class1List", table);

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

			forward = mapping.findForward("class1List");
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

	public ActionForward toDisplayClass1(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		String naigation = new String();
		this.setNavigation(request, "navigator.base.class1.view");

		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		Class1 class1 = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				class1 = (Class1) hs
						.get(Class1.class, (String) dform.get("id"));

				if (class1 == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("unittype.display.recordnotfounterror"));
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

			request.setAttribute("display", "yes");
			request.setAttribute("class1Bean", class1);
			forward = mapping.findForward("class1Display");
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

	public HttpServletResponse toExcelClass1(ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String naigation = new String();
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		String class1id = tableForm.getProperty("class1id");
		String class1name = tableForm.getProperty("class1name");
		String levelid = tableForm.getProperty("levelid");
		String rem1 = tableForm.getProperty("rem1");
		String enabledFlag = tableForm.getProperty("enabledFlag");
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(Class1.class);
			if (class1id != null && !class1id.equals("")) {
				criteria.add(Expression.like("class1id", "%" + class1id.trim()
						+ "%"));
			}
			if (class1name != null && !class1name.equals("")) {
				criteria.add(Expression.like("class1name", "%"
						+ class1name.trim() + "%"));
			}
			if (levelid != null && !levelid.equals("")) {
				criteria.add(Expression.like("levelid", "%" + levelid.trim()
						+ "%"));
			}
			if (rem1 != null && !rem1.equals("")) {
				criteria.add(Expression.like("rem1", "%" + rem1.trim() + "%"));
			}
			if (enabledFlag != null && !enabledFlag.equals("")) {
				criteria.add(Expression.eq("enabledFlag", enabledFlag));
			}

			criteria.addOrder(Order.asc("class1id"));

			List List = criteria.list();

			XSSFSheet sheet = wb.createSheet("职位信息");

			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (List != null && !List.isEmpty()) {
				int l = List.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = null;
				cell0 = row0.createCell((short) 0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"customer.placeid"));

				cell0 = row0.createCell((short) 1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"customer.placename"));

				cell0 = row0.createCell((short) 2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"customer.placelevel"));

				cell0 = row0.createCell((short) 3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"customer.placereml"));

				cell0 = row0.createCell((short) 4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"customer.placestart"));

				for (int i = 0; i < l; i++) {
					Class1 obj = (Class1) List.get(i);
					XSSFRow row = sheet.createRow( i + 1);
					XSSFCell cell = null;
					cell = row.createCell((short) 0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getClass1id());

					cell = row.createCell((short) 1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getClass1name());

					cell = row.createCell((short) 2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getLevelid().toString());

					cell = row.createCell((short) 3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getRem1());

					cell = row.createCell((short) 4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(obj.getEnabledFlag()));

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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("职位信息", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}

}