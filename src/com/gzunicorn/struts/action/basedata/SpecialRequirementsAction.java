package com.gzunicorn.struts.action.basedata;

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
import org.hibernate.criterion.Restrictions;
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

import com.gzunicorn.common.dataimport.importutil.ComUtil;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.hibernate.basedata.city.City;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.basedata.handoverelevatorspecialclaim.HandoverElevatorSpecialClaim;
import com.gzunicorn.hibernate.basedata.hotlinefaulttype.HotlineFaultType;
import com.gzunicorn.hibernate.basedata.hotlinemotherboardtype.HotlineMotherboardType;
import com.gzunicorn.hibernate.basedata.principal.Principal;
import com.gzunicorn.hibernate.basedata.region.Region;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SpecialRequirementsAction extends DispatchAction {

	Log log = LogFactory.getLog(SpecialRequirementsAction.class);
	
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
				SysRightsUtil.NODE_ID_FORWARD + "specialrequirements", null);
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

		request.setAttribute("navigator.location","	交接电梯特殊要求>> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form,request,response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport","");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "handoverElevatorSpecialClaimList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fHandoverElevatorSpecialClaim");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
//			table.setLength(1);
			cache.updateTable(table);
			table.setSortColumn("scId");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String scId = tableForm.getProperty("scId");
			String scName = tableForm.getProperty("scName");
			String enabledFlag = tableForm.getProperty("enabledFlag");
			Session hs = null;

			try {

				hs = HibernateUtil.getSession();

				Criteria criteria = hs.createCriteria(HandoverElevatorSpecialClaim.class);
				if (scId != null && !scId.equals("")) {
					criteria.add(Expression.like("scId", "%" + scId.trim()
							+ "%"));
				}
				if (scName != null && !scName.equals("")) {
					criteria.add(Expression.like("scName", "%"
							+ scName.trim() + "%"));
				}
				if (enabledFlag != null && !enabledFlag.equals("")) {
					criteria.add(Expression.eq("enabledFlag", enabledFlag));
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

				List handoverElevatorSpecialClaimList = criteria.list();

				table.addAll(handoverElevatorSpecialClaimList);
				session.setAttribute("handoverElevatorSpecialClaimList", table);

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
			forward = mapping.findForward("handoverElevatorSpecialClaimList");
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
		request.setAttribute("navigator.location", messages.getMessage(locale,
				navigation));
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
		
		request.setAttribute("navigator.location","交接电梯特殊要求 >> 查看");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;
		
		String id = (String) dform.get("id");
		Session hs = null;
		HandoverElevatorSpecialClaim handoverElevatorSpecialClaim = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				handoverElevatorSpecialClaim = (HandoverElevatorSpecialClaim) hs.get(HandoverElevatorSpecialClaim.class, id);
				
				if (handoverElevatorSpecialClaim == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"hotlinemotherboardtype.display.recordnotfounterror"));
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
			request.setAttribute("handoverElevatorSpecialClaimBean", handoverElevatorSpecialClaim);
			forward = mapping.findForward("handoverElevatorSpecialClaimDisplay");

		}

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

		request.setAttribute("navigator.location","交接电梯特殊要求 >> 添加");
		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
			dform.set("enabledFlag", "Y");
		}
		dform.set("enabledFlag", "Y");

		return mapping.findForward("handoverElevatorSpecialClaimAdd");
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
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;
		
		String scId = (String) dform.get("scId");//故障代码
		String scName = (String) dform.get("scName");//问题描述
		String enabledFlag = (String) dform.get("enabledFlag");//启用标志
		String rem = (String) dform.get("rem");//备注

		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			HandoverElevatorSpecialClaim handoverElevatorSpecialClaim = new HandoverElevatorSpecialClaim();
			handoverElevatorSpecialClaim.setScId(scId);
			handoverElevatorSpecialClaim.setScName(scName);
			handoverElevatorSpecialClaim.setEnabledFlag(enabledFlag);
			handoverElevatorSpecialClaim.setRem(rem);			
			handoverElevatorSpecialClaim.setOperId(userInfo.getUserID());//录入人
			handoverElevatorSpecialClaim.setOperDate(CommonUtil.getToday());//录入时间

			hs.save(handoverElevatorSpecialClaim);
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("hotlinemotherboardtype.insert.duplicatekeyerror"));
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
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","交接电梯特殊要求 >> 修改");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		ActionForward forward = null;
		String id = null;

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("scId");
		} else {
			id = (String) dform.get("id");
		}
		//System.out.println(id);
		Session hs = null;
		HandoverElevatorSpecialClaim handoverElevatorSpecialClaim = null;
		if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					handoverElevatorSpecialClaim = (HandoverElevatorSpecialClaim) hs.get(HandoverElevatorSpecialClaim.class, id);

					if (handoverElevatorSpecialClaim == null) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"hotlinemotherboardtype.display.recordnotfounterror"));
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
			request.setAttribute("handoverElevatorSpecialClaimBean", handoverElevatorSpecialClaim);
			forward = mapping.findForward("handoverElevatorSpecialClaimModify");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	/**
	 * 紧急级别修改
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
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String scId = (String) dform.get("scId");//故障代码
		String scName = (String) dform.get("scName");//问题描述
		String enabledFlag = (String) dform.get("enabledFlag");//启用标志
		String rem = (String) dform.get("rem");//备注
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			HandoverElevatorSpecialClaim handoverElevatorSpecialClaim = (HandoverElevatorSpecialClaim) hs.get(HandoverElevatorSpecialClaim.class, (String) dform.get("id"));
			if (dform.get("id") != null
					&& dform.get("scId") != null
					&& !((String) dform.get("id")).equals((String) dform.get("scId"))) {
				hs.delete(handoverElevatorSpecialClaim);
				handoverElevatorSpecialClaim = new HandoverElevatorSpecialClaim();
			}
			
			handoverElevatorSpecialClaim.setScId(scId);
			handoverElevatorSpecialClaim.setScName(scName);
			handoverElevatorSpecialClaim.setEnabledFlag(enabledFlag);
			handoverElevatorSpecialClaim.setRem(rem);			
			handoverElevatorSpecialClaim.setOperId(userInfo.getUserID());//录入人
			handoverElevatorSpecialClaim.setOperDate(CommonUtil.getToday());//录入时间

			hs.save(handoverElevatorSpecialClaim);

			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("hotlinemotherboardtype.update.duplicatekeyerror"));
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
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
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
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			HandoverElevatorSpecialClaim handoverElevatorSpecialClaim = (HandoverElevatorSpecialClaim) hs.get(HandoverElevatorSpecialClaim.class, (String) dform.get("id"));
			if (handoverElevatorSpecialClaim != null) {
				hs.delete(handoverElevatorSpecialClaim);
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

		String scId = tableForm.getProperty("scId");//单位代码
		String scName = tableForm.getProperty("scName");//单位名称
		String enabledFlag = tableForm.getProperty("enabledFlag");//启用标志
		
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(HandoverElevatorSpecialClaim.class);
			if (scId != null && !scId.equals("")) {
				criteria.add(Expression.like("scId", "%" + scId.trim()
						+ "%"));
			}
			if (scName != null && !scName.equals("")) {
				criteria.add(Expression.like("scName", "%"
						+ scName.trim() + "%"));
			}
			if (enabledFlag != null && !enabledFlag.equals("")) {
				criteria.add(Expression.eq("enabledFlag", enabledFlag));
			}

			criteria.addOrder(Order.asc("scId"));

			List roleList = criteria.list();

			XSSFSheet sheet = wb.createSheet("交接电梯特殊要求");
			
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("特殊要求代码");
				
				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("特殊要求名称");
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"hotlinemotherboardtype.enabledFlag"));
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"hotlinemotherboardtype.rem"));
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"hotlinemotherboardtype.operId"));
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"hotlinemotherboardtype.operDate"));
				
				for (int i = 0; i < l; i++) {
					HandoverElevatorSpecialClaim handoverElevatorSpecialClaim = (HandoverElevatorSpecialClaim) roleList.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);
	
					// 创建Excel列
					XSSFCell cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(handoverElevatorSpecialClaim.getScId());
					
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(handoverElevatorSpecialClaim.getScName());
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(handoverElevatorSpecialClaim.getEnabledFlag()));
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(handoverElevatorSpecialClaim.getRem());

					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName(hs, "LoginUser", "userName", "userId",handoverElevatorSpecialClaim.getOperId()));

					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(handoverElevatorSpecialClaim.getOperDate());

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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("交接电梯特殊要求", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
}
