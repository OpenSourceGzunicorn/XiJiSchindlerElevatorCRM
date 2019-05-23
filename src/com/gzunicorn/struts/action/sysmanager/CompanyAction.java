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
import com.gzunicorn.hibernate.sysmanager.Company;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * MyEclipse Struts Creation date: 08-09-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/companyAction" name="companyForm" scope="request"
 *                validate="true"
 */
public class CompanyAction extends DispatchAction {

	Log log = LogFactory.getLog(CompanyAction.class);
	
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
		SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "company",null);
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
		this.setNavigation(request, "navigator.base.company.add");

		DynaActionForm dform = (DynaActionForm) form;
		// dform.reset(mapping,null);
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
			dform.set("enabledflag", "Y");
		}

		//java.util.List result = new java.util.ArrayList();
		//request.setAttribute("viewcustomertypeList",bd.getViewCustomerTypeList());
		//request.setAttribute("viewcustomerlevelList",bd.getViewCustomerLevelList());
		//request.setAttribute("viewcreditlevelList",bd.getViewCreditLevelList());
		request.setAttribute("viewcompanytypeList",bd.getViewCompnayTypeList());
		return mapping.findForward("companyAdd");
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
			Company company = new Company();
			company.setComid((String) dform.get("comid"));
			company.setComname((String) dform.get("comname"));
			company.setComfullname((String) dform.get("comfullname"));
			company.setComtype((String) dform.get("comtype"));
			company.setComtel((String) dform.get("comtel"));
			company.setLinkman((String) dform.get("linkman"));
			company.setLinkmantel((String) dform.get("linkmantel"));
			company.setFax((String) dform.get("fax"));
			company.setArtperson((String) dform.get("artperson"));
			company.setBank((String) dform.get("bank"));
			company.setAccount((String) dform.get("account"));
			company.setAddress((String) dform.get("address"));
			company.setEnabledflag((String) dform.get("enabledflag"));
			company.setRem((String) dform.get("rem"));
			hs.save(company);
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"company.insert.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Company Insert error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Company Insert error!");
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
			Company company = (Company) hs.get(Company.class, (String) dform.get("id"));
			if (dform.get("id") != null
					&& dform.get("comid") != null
					&& !((String) dform.get("id")).equals((String) dform
							.get("comid"))) {
				hs.delete(company);
				company = new Company();
			}
			company.setComid((String) dform.get("comid"));
			company.setComname((String) dform.get("comname"));
			company.setComfullname((String) dform.get("comfullname"));
			company.setComtype((String) dform.get("comtype"));
			company.setComtel((String) dform.get("comtel"));
			company.setLinkman((String) dform.get("linkman"));
			company.setLinkmantel((String) dform.get("linkmantel"));
			company.setFax((String) dform.get("fax"));
			company.setArtperson((String) dform.get("artperson"));
			company.setBank((String) dform.get("bank"));
			company.setAccount((String) dform.get("account"));
			company.setAddress((String) dform.get("address"));
			company.setEnabledflag((String) dform.get("enabledflag"));
			company.setRem((String) dform.get("rem"));
			hs.save(company);
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"company.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Company Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Company Update error!");
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
		this.setNavigation(request, "navigator.base.company.modify");
		ActionForward forward = null;
		String id = null;

		//BaseDataImpl bdi = new BaseDataImpl();
		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("comid");
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
							.createQuery("from Company a where a.comid = :comid");
					query.setString("comid", id);
					java.util.List list = query.list();
					if (list != null && list.size() > 0) {
						Company company = (Company) list.get(0);
						dform.set("id", company.getComid());
						dform.set("comid", company.getComid());
						dform.set("comname",company.getComname());
						dform.set("comfullname", company.getComfullname());
						dform.set("comtype", company.getComtype());
						dform.set("comtel", company.getComtel());
						dform.set("linkman", company.getLinkman());
						dform.set("linkmantel", company.getLinkmantel());
						dform.set("fax", company.getFax());
						dform.set("artperson", company.getArtperson());
						dform.set("bank", company.getBank());
						dform.set("account", company.getAccount());
						dform.set("address", company.getAddress());
						dform.set("rem", company.getRem());
						dform.set("enabledflag", company.getEnabledflag());
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
			//request.setAttribute("viewcustomertypeList",bd.getViewCustomerTypeList());
			//request.setAttribute("viewcustomerlevelList",bd.getViewCustomerLevelList());
			//request.setAttribute("viewcreditlevelList",bd.getViewCreditLevelList());
			request.setAttribute("viewcompanytypeList",bd.getViewCompnayTypeList());
			forward = mapping.findForward("companyModify");
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
			Company company = (Company) hs.get(Company.class, (String) dform.get("id"));
			if(company != null)
			{
			 hs.delete(company);
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
			DebugUtil.print(e2, "Hibernate Company Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Company Update error!");

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
		this.setNavigation(request, "navigator.base.company.list");
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
			HTMLTableCache cache = new HTMLTableCache(session, "companyList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fCompany");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("comid");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String comfullname = tableForm.getProperty("comfullname");
			String comid = tableForm.getProperty("comid");
			String enabledflag = tableForm.getProperty("enabledflag");
			Session hs = null;

			try {

				hs = HibernateUtil.getSession();

				Criteria criteria = hs.createCriteria(Company.class);
				if (comid != null && !comid.equals("")) {
					criteria.add(Expression.like("comid", "%" + comid.trim() + "%"));
				}
				if (comfullname != null && !comfullname.equals("")) {
					criteria.add(Expression.like("comfullname", "%" + comfullname.trim()
							+ "%"));
				}
				if (enabledflag != null && !enabledflag.equals("")){
					criteria.add(Expression.eq("enabledflag", enabledflag));
				}

				if (table.getIsAscending()) {
					criteria.addOrder(Order.asc(table.getSortColumn()));
				} else {
					criteria.addOrder(Order.desc(table.getSortColumn()));
				}
                ////System.out.println(cityid);
				table.setVolume(criteria.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				criteria.setFirstResult(table.getFrom()); // pagefirst
				criteria.setMaxResults(table.getLength());

				cache.check(table);

				List company = criteria.list();
				BaseDataImpl bd = new BaseDataImpl();
				if(company !=null){
					for(int i=0;i<company.size();i++)
					{
						Company cp=(Company)company.get(i);
						cp.setComtype(bd.getName("Viewcompanytype","comtypename","comtypeid",cp.getComtype()));
					}	
				}

				table.addAll(company);
				session.setAttribute("companyList", table);

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
             
			forward = mapping.findForward("companyList");
			////System.out.println(cityid);
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
		this.setNavigation(request, "navigator.base.company.modify");

		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		Company company = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				company = (Company) hs.get(Company.class, (String) dform.get("id"));

				if (company == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("company.display.recordnotfounterror"));
				}
				else
				{
					company.setComtype(bd.getName("Viewcompanytype","comtypename","comtypeid",company.getComtype()));
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
			request.setAttribute("companyBean", company);
			forward = mapping.findForward("companyDisplay");
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

		String comid = tableForm.getProperty("comid");
		String comfullname = tableForm.getProperty("comfullname");
		String enabledflag = tableForm.getProperty("enabledflag");
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(Company.class);
			if (comid != null && !comid.equals("")) {
				criteria.add(Expression.like("comid", "%" + comid.trim() + "%"));
			}
			if (comfullname != null && !comfullname.equals("")) {
				criteria.add(Expression.like("comfullname", "%" + comfullname.trim() + "%"));
			}
			if (enabledflag != null && !enabledflag.equals("")) {
				criteria.add(Expression.eq("enabledflag", enabledflag));
			}

			criteria.addOrder(Order.asc("comid"));

			List List = criteria.list();

			XSSFSheet sheet = wb.createSheet("分部信息");

			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (List != null && !List.isEmpty()) {
				int l = List.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = null;
				cell0 = row0.createCell((short) 0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale, "company.comid"));

				cell0 = row0.createCell((short) 1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"company.comname"));
				
				cell0 = row0.createCell((short) 2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"company.comfullname"));
				
				cell0 = row0.createCell((short) 3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"company.comtyped"));
				
				cell0 = row0.createCell((short) 4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"company.linkman"));
				
				cell0 = row0.createCell((short) 5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"company.linkmantel"));
				
				cell0 = row0.createCell((short) 6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"company.address"));

				cell0 = row0.createCell((short) 7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"company.enabledflag"));

				cell0 = row0.createCell((short) 8);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale, "company.rem"));

				for (int i = 0; i < l; i++) {
					Company obj = (Company) List.get(i);
					XSSFRow row = sheet.createRow( i + 1);
					XSSFCell cell = null;
					cell = row.createCell((short) 0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getComid());

					cell = row.createCell((short) 1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getComname());
					
					cell = row.createCell((short) 2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getComfullname());

					cell = row.createCell((short) 3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String comtypename="";
					if(obj.getComtype()!=null && !obj.getComtype().trim().equals("")){
						comtypename=bd.getName("Viewcompanytype","comtypename","comtypeid",obj.getComtype());
					}
					cell.setCellValue(comtypename);
										
					cell = row.createCell((short) 4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getLinkman());
					
					cell = row.createCell((short) 5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getLinkmantel());
										
					cell = row.createCell((short) 6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getAddress());
					
					cell = row.createCell((short) 7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(obj
							.getEnabledflag()));

					cell = row.createCell((short) 8);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj.getRem());
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("分部信息", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}

}