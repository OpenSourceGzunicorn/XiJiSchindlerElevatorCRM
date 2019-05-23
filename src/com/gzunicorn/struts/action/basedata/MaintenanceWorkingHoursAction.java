package com.gzunicorn.struts.action.basedata;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.apache.poi.ss.usermodel.Cell;
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
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.elevatorcoordinatelocation.ElevatorCoordinateLocation;
import com.gzunicorn.hibernate.basedata.maintenanceworkinghours.MaintenanceWorkingHours;
import com.gzunicorn.hibernate.basedata.maintenanceworkinghours.MaintenanceWorkingHoursId;
import com.gzunicorn.hibernate.sysmanager.pulldown.Pulldown;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class MaintenanceWorkingHoursAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintenanceWorkingHoursAction.class);
	
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
				SysRightsUtil.NODE_ID_FORWARD + "maintenanceworkinghours", null);
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
		
		request.setAttribute("navigator.location","电梯类型保养工时>> 查询列表");		
		ActionForward forward = null;
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
			HTMLTableCache cache = new HTMLTableCache(session, "maintenanceWorkingHoursList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fMaintenanceWorkingHours");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("id.elevatorType");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			
			cache.saveForm(tableForm);

			String elevatorType = tableForm.getProperty("elevatorType");
			String enabledFlag = tableForm.getProperty("enabledFlag");
			String floor =tableForm.getProperty("floor");
			
			String order="";
			Session hs = null;
			Query query = null;
			try {

				hs = HibernateUtil.getSession();
				String sql = "from MaintenanceWorkingHours where 1=1";

				if (elevatorType != null && !elevatorType.equals("")) {
					sql += " and id.elevatorType like '%" + elevatorType.trim() + "%'";
				}
				if (floor != null && !floor.equals("")) {
					sql += " and id.floor like '%" + floor.trim() + "%'";
				}
				if (enabledFlag != null && !enabledFlag.equals("")) {
					sql += " and enabledFlag like '%" + enabledFlag + "%'";
				}
				if (table.getIsAscending()) {
					order="order by "+table.getSortColumn();
				} else {
					order="order by "+table.getSortColumn()+" desc";
				}
				

				query = hs.createQuery(sql+order);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List maintenanceWorkingHoursList = query.list();
				
				if(maintenanceWorkingHoursList.size()>0){
					int len=maintenanceWorkingHoursList.size();
					for(int i=0;i<len;i++){
						MaintenanceWorkingHours mwh=(MaintenanceWorkingHours) maintenanceWorkingHoursList.get(i);
						String sql2="select pullname from pulldown where pullid='"+mwh.getId().getElevatorType()+"' and typeflag='ElevatorSalesInfo_type'";
						List list=hs.createSQLQuery(sql2).list();
						if(list!=null){
							mwh.setR1((String)list.get(0));
						}else{
							mwh.setR1("");
						}
					}
				}
				
				table.addAll(maintenanceWorkingHoursList);
				session.setAttribute("maintenanceWorkingHoursTypeList",bd.getPullDownList("ElevatorSalesInfo_type"));
				session.setAttribute("maintenanceWorkingHoursList", table);

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
			forward = mapping.findForward("maintenanceWorkingHoursList");
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
		
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		request.setAttribute("navigator.location","电梯类型保养工时 >> 查看");
		List list =null;
		ActionForward forward = null;
		String elevatorType =(String) dform.get("id");
	
		
		Session hs = null;		
		Integer floor = (Integer) dform.get("floor");
		
		MaintenanceWorkingHours maintenanceWorkingHours = null;
		if (elevatorType != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from MaintenanceWorkingHours  where id.elevatorType = :elevatorType and id.floor = :floor");
				query.setString("elevatorType", elevatorType);
				query.setInteger("floor", floor);
			    list = query.list();
				
			    if(list.size()>0){
					int len=list.size();
					for(int i=0;i<len;i++){
						MaintenanceWorkingHours mwh=(MaintenanceWorkingHours) list.get(i);
						String sql2="select pullname from pulldown where pullid='"+mwh.getId().getElevatorType()+"' and typeflag='ElevatorSalesInfo_type'";
						List list2=hs.createSQLQuery(sql2).list();
						if(list2!=null){
							mwh.setR1((String)list2.get(0));
						}else{
							mwh.setR1("");
						}
					}
				} 
			    
				if (list != null && list.size() > 0) {
					maintenanceWorkingHours = (MaintenanceWorkingHours) list.get(0);
					dform.set("elevatorType", maintenanceWorkingHours.getId().getElevatorType());
					dform.set("floor", maintenanceWorkingHours.getId().getFloor());
					dform.set("halfMonth", maintenanceWorkingHours.getHalfMonth());
					dform.set("quarter", maintenanceWorkingHours.getQuarter());
					dform.set("halfYear", maintenanceWorkingHours.getHalfYear());
					dform.set("yearDegree", maintenanceWorkingHours.getYearDegree());
					dform.set("enabledFlag", maintenanceWorkingHours.getEnabledFlag());
					dform.set("rem", maintenanceWorkingHours.getRem());
					dform.set("r1", maintenanceWorkingHours.getR1());
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}

				if (maintenanceWorkingHours == null) {
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
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}

			request.setAttribute("display", "yes");
			request.setAttribute("maintenanceWorkingHoursBean", maintenanceWorkingHours);
			forward = mapping.findForward("maintenanceWorkingHoursDisplay");

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

		request.setAttribute("navigator.location","电梯类型保养工时>> 添加");

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
			dform.set("enabledFlag", "Y");
		}
		dform.set("enabledFlag", "Y");

		//request.setAttribute("maintenanceWorkingHoursList",bd.getPullDownList("enabledflag"));

		return mapping.findForward("maintenanceWorkingHoursAdd");
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
		
		String elevatorType = (String) dform.get("elevatorType");
		Integer floor = (Integer) dform.get("floor");
		Integer halfMonth = (Integer) dform.get("halfMonth");
		Integer quarter = (Integer) dform.get("quarter");
		Integer halfYear = (Integer) dform.get("halfYear");
		Integer yearDegree = (Integer) dform.get("yearDegree");
		String enabledFlag = (String) dform.get("enabledFlag");
		String rem = (String) dform.get("rem");
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			MaintenanceWorkingHours maintenanceWorkingHours=new MaintenanceWorkingHours();
			MaintenanceWorkingHoursId maintenanceWorkingHoursId =new MaintenanceWorkingHoursId();
			maintenanceWorkingHoursId.setElevatorType(elevatorType.trim());
			maintenanceWorkingHoursId.setFloor(floor);
			maintenanceWorkingHours.setId(maintenanceWorkingHoursId);
			maintenanceWorkingHours.setHalfMonth(halfMonth);
			maintenanceWorkingHours.setQuarter(quarter);
			maintenanceWorkingHours.setHalfYear(halfYear);
			maintenanceWorkingHours.setYearDegree(yearDegree);
			maintenanceWorkingHours.setEnabledFlag(enabledFlag.trim());
			maintenanceWorkingHours.setRem(rem.trim());
			
			maintenanceWorkingHours.setOperId(userInfo.getUserID());//录入人
			maintenanceWorkingHours.setOperDate(CommonUtil.getToday());//录入时间

			hs.save(maintenanceWorkingHours);
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("maintenanceworkinghours.insert.duplicatekeyerror"));
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
		
		request.setAttribute("navigator.location","电梯类型保养工时 >> 修改");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		ActionForward forward = null;
		
		String elevatorType = "";
		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			elevatorType = (String) dform.get("elevatorType");
		} else

      {
			elevatorType = (String) dform.get("id");
		}
		
		
		Integer floor = (Integer) dform.get("floor");
		
		Session hs = null;
		MaintenanceWorkingHours maintenanceWorkingHours = null;
		if (elevatorType != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					Query query = hs.createQuery("from MaintenanceWorkingHours  where id.elevatorType = :elevatorType and id.floor = :floor");
					query.setString("elevatorType", elevatorType);
					query.setInteger("floor", floor);
				
					 if(query.list().size()>0){
							int len=query.list().size();
							for(int i=0;i<len;i++){
								MaintenanceWorkingHours mwh=(MaintenanceWorkingHours) query.list().get(i);
								String sql2="select pullname from pulldown where pullid='"+mwh.getId().getElevatorType()+"' and typeflag='ElevatorSalesInfo_type'";
								List list2=hs.createSQLQuery(sql2).list();
								if(list2!=null){
									mwh.setR1((String)list2.get(0));
								}else{
									mwh.setR1("");
								}
							}
						} 
					

					maintenanceWorkingHours = (MaintenanceWorkingHours)query.list().get(0);

					
					
					if (maintenanceWorkingHours == null) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"termsecurityrisks.display.recordnotfounterror"
								));
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
			request.setAttribute("maintenanceWorkingHoursIdBean", maintenanceWorkingHours.getId());
			request.setAttribute("maintenanceWorkingHoursBean", maintenanceWorkingHours);
			forward = mapping.findForward("maintenanceWorkingHoursModify");
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
		
		String elevatorType = (String) dform.get("elevatorType");
		Integer floor = (Integer) dform.get("floor");
		Integer halfMonth = (Integer) dform.get("halfMonth");
		Integer quarter = (Integer) dform.get("quarter");
		Integer halfYear = (Integer) dform.get("halfYear");
		Integer yearDegree = (Integer) dform.get("yearDegree");
		String enabledFlag = (String) dform.get("enabledFlag");
		String rem = (String) dform.get("rem");
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			Query query = hs.createQuery("from MaintenanceWorkingHours  where id.elevatorType = :elevatorType and id.floor = :floor");
			query.setString("elevatorType", elevatorType);
			query.setInteger("floor", floor);
			MaintenanceWorkingHours maintenanceWorkingHours = (MaintenanceWorkingHours) query.list().get(0);
			
			if (dform.get("id") != null
					&& dform.get("elevatorType") != null
					&& !((String) dform.get("id")).equals((String) dform.get("elevatorType"))) {
				hs.delete(maintenanceWorkingHours);
				maintenanceWorkingHours = new MaintenanceWorkingHours();
			}
			
			
			MaintenanceWorkingHoursId maintenanceWorkingHoursId =new MaintenanceWorkingHoursId();
			maintenanceWorkingHoursId.setElevatorType(elevatorType.trim());
			maintenanceWorkingHoursId.setFloor(floor);
			maintenanceWorkingHours.setId(maintenanceWorkingHoursId);
			maintenanceWorkingHours.setHalfMonth(halfMonth);
			maintenanceWorkingHours.setQuarter(quarter);
			maintenanceWorkingHours.setHalfYear(halfYear);
			maintenanceWorkingHours.setYearDegree(yearDegree);
			maintenanceWorkingHours.setEnabledFlag(enabledFlag.trim());
			maintenanceWorkingHours.setRem(rem.trim());
			
			maintenanceWorkingHours.setOperId(userInfo.getUserID());//录入人
			maintenanceWorkingHours.setOperDate(CommonUtil.getToday());//录入时间

			hs.save(maintenanceWorkingHours);
			tx.commit();
			
			//更新维保作业计划书的标准保养时间
			bd.updateMaintDateTime();
			
			
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("maintenanceworkinghours.update.duplicatekeyerror"));
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
			String elevatorType = (String) dform.get("id");
			Integer floor = (Integer) dform.get("floor");
			
			
			Query query = hs.createQuery("from MaintenanceWorkingHours  where id.elevatorType = :elevatorType and id.floor = :floor");
			query.setString("elevatorType", elevatorType);
			query.setInteger("floor", floor);
			MaintenanceWorkingHours maintenanceWorkingHours = (MaintenanceWorkingHours) query.list().get(0);
			
			
			
			if (maintenanceWorkingHours != null) {
				hs.delete(maintenanceWorkingHours);
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


        String elevatorType = tableForm.getProperty("elevatorType");
		String enabledFlag = tableForm.getProperty("enabledFlag");
		String floor =tableForm.getProperty("floor");
		
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(MaintenanceWorkingHours.class);
			if (elevatorType != null && !elevatorType.equals("")) {
				criteria.add(Expression.like("id.elevatorType", "%" + elevatorType.trim() + "%"));
			}
			if (floor != null && !floor.equals("")) {
				criteria.add(Expression.like("id.floor", "%" + floor.trim()
						+ "%"));
			}
			if (enabledFlag != null && !enabledFlag.equals("")) {
				criteria.add(Expression.eq("enabledFlag", enabledFlag));
			}

			criteria.addOrder(Order.asc("id.elevatorType"));

			List roleList = criteria.list();

			XSSFSheet sheet = wb.createSheet("电梯类型保养工时");
			
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"maintenanceworkinghours.elevatorType"));

				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"maintenanceworkinghours.floor"));
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"maintenanceworkinghours.halfMonth"));
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"maintenanceworkinghours.quarter"));
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"maintenanceworkinghours.halfYear"));
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"maintenanceworkinghours.yearDegree"));
				
				cell0 = row0.createCell((short)6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"maintenanceworkinghours.enabledflag"));
				
				cell0 = row0.createCell((short)7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"maintenanceworkinghours.rem"));
				
				cell0 = row0.createCell((short)8);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"maintenanceworkinghours.operId"));
				
				cell0 = row0.createCell((short)9);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"maintenanceworkinghours.operDate"));
			
				for (int i = 0; i < l; i++) {
					MaintenanceWorkingHours maintenanceWorkingHours = (MaintenanceWorkingHours) roleList.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);
	
					// 创建Excel列
					XSSFCell cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String sql2="select  pullname from pulldown where pullid='"+maintenanceWorkingHours.getId().getElevatorType()+"'";
					List list=hs.createSQLQuery(sql2).list();
					
					cell.setCellValue((String)list.get(0));
					
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(maintenanceWorkingHours.getId().getFloor());
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(maintenanceWorkingHours.getHalfMonth());
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(maintenanceWorkingHours.getQuarter());
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(maintenanceWorkingHours.getHalfYear());
					
					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(maintenanceWorkingHours.getYearDegree());
					
					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(maintenanceWorkingHours.getEnabledFlag()));
					
					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(maintenanceWorkingHours.getRem());

					cell = row.createCell((short)8);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName(hs, "LoginUser", "userName", "userId",maintenanceWorkingHours.getOperId()));

					cell = row.createCell((short)9);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(maintenanceWorkingHours.getOperDate());
					

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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("电梯类型保养工时", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
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
	public ActionForward toPrepareImportRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		request.setAttribute("navigator.location","  电梯类型保养工时>> 导入");

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}

		return mapping.findForward("maintenanceWorkingHoursImport");
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
	
	public ActionForward toImportRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		FormFile file = (FormFile) dform.get("file"); //获取上传文件
		String fileName = file.getFileName();
		String fileFromt=fileName.substring(
				fileName.lastIndexOf(".")+1,fileName.length()); //获取上传文件的后缀名
		
		InputStream in = null;
		Session hs = null;
		Transaction tx = null;
		StringBuffer reStr = new StringBuffer(); //错误返回信息
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			if (fileFromt!=null && fileFromt.equals("xlsx")) {//excel 2007
				
				MaintenanceWorkingHours master = null; //电梯位置信息表
				List<MaintenanceWorkingHours> list = new ArrayList<MaintenanceWorkingHours>();
				
				in = file.getInputStream();
				XSSFWorkbook wb = new XSSFWorkbook(in);
				XSSFSheet sheet = wb.getSheetAt(0);
				XSSFRow row = null;			
				
				int rowSum = sheet.getLastRowNum() +1; //最大行数
				String  elevatorType = ""; //电梯类型
				int  floor = 0;//电梯层数
				int  halfMonth = 0;//半月保养工时
				int  quarter = 0;//季度保养工时
				int  halfYear = 0;//半年保养工时
				int  yearDegree = 0;//年度保养工时
				
				List ids = new ArrayList();
				List fools=new ArrayList();
				String userid=userInfo.getUserID();
				String today=CommonUtil.getToday();
				for(int rowNum = 1; rowNum < rowSum; rowNum++){					
					row = sheet.getRow(rowNum);									
					if(cellValueToString(row, 0, reStr).equals("直梯"))
					{
						elevatorType = "T"; //电梯编号
					}
					if(cellValueToString(row, 0, reStr).equals("扶梯"))
					{
						elevatorType = "F"; //电梯编号
					}
					floor =cellValueToInt(row, 1, reStr);//电梯位置
					halfMonth =cellValueToInt(row, 2, reStr);//开始经度
					quarter =cellValueToInt(row, 3, reStr);//结束经度
					halfYear=cellValueToInt(row, 4, reStr);//开始维度
					yearDegree=cellValueToInt(row, 5, reStr);//结束维度

					master = new MaintenanceWorkingHours();
					MaintenanceWorkingHoursId id=new MaintenanceWorkingHoursId();
					id.setElevatorType(elevatorType);
					id.setFloor(floor);
					master.setId(id);
					master.setHalfMonth(halfMonth);
					master.setHalfYear(halfYear);
					master.setQuarter(quarter);
				    master.setYearDegree(yearDegree);

					master.setOperId(userid);
					master.setOperDate(today);
					master.setEnabledFlag("Y");
					
					if(reStr != null && reStr.length() > 0){
						break;
					}
					
					list.add(master);	
					if(elevatorType.trim().equals("F"))
					{
						ids.add("F");
						fools.add(0);
					}else
					{
						ids.add(elevatorType);
						fools.add(floor);
					}
					
				}
				if(reStr == null || reStr.length() == 0){
					String hql = "delete MaintenanceWorkingHours where "; //删除旧版本数据
					for(int i=0;i<ids.size();i++)
					{
						hql+= i<ids.size()-1?" (id.elevatorType = '"+ids.get(i)+"' and id.floor ='"+fools.get(i)+"') or ":" (id.elevatorType = '"+ids.get(i)+"' and id.floor ='"+fools.get(i)+"')";
					}
					Query query = hs.createQuery(hql); 
					query.executeUpdate();
					
					for (MaintenanceWorkingHours mwh : list) {						
						
						hs.save(mwh);
						//hs.flush();
					}
				
				} else {
					request.setAttribute("reStr", reStr);//错误返回信息
				}
				
			}

			tx.commit();
			
			//更新维保作业计划书的标准保养时间
			bd.updateMaintDateTime();
			
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("elevatorSale.insert.duplicatekeyerror"));
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
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("elevatorSale.insert.duplicatekeyerror"));
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = mapping.findForward("returnImport");
		
		if (errors.isEmpty() && reStr.length() == 0) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
				
		} else {
			request.setAttribute("error", "Yes");
		}
			
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return forward;
	}

	/**
	 * 返回单元格字符串值
	 * @param XSSFRow 行对象
	 * @param cellNum 所在列数
	 * @param reStr 错误信息  
	 * @return String
	 */
	public String cellValueToString(XSSFRow row, int cellNum, StringBuffer reStr){
		String value = "";
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) { 
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)为空;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			value = cell.getStringCellValue();
		} else {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)数据格式必须为字符串;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		return value.trim();
	}
	
	/**
	 * 返回单元格字符串值（空值时不添加错误信息）
	 * @param XSSFRow 行对象
	 * @param cellNum 所在列数
	 * @param reStr 错误信息  
	 * @return String
	 */
	public String cellValueToString2(XSSFRow row, int cellNum, StringBuffer reStr){
		String value = "";
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) { 
			
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			value = cell.getStringCellValue();
		} else {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)数据格式必须为字符串;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		return value.trim();
	}
		
	/**
	 * 单元格日期格式的值转为字符串值并返回
	 * @param XSSFRow 行对象
	 * @param cellNum 所在列数
	 * @param reStr 错误信息  
	 * @return String
	 */
	public String cellDateValueToString(XSSFRow row, int cellNum, StringBuffer reStr){
		String value = "";
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)为空;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");   
			value = sdf.format(cell.getDateCellValue());  
		} else {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)数据格式必须为日期;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		return value.trim();		
	}
	
	/**
	 * 返回单元格整型值
	 * @param XSSFRow 行对象
	 * @param cellNum 所在列数
	 * @param reStr 错误信息  
	 * @return String
	 */
	public int cellValueToInt(XSSFRow row, int cellNum, StringBuffer reStr){
		int value = 0;
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)为空;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			value = (int) cell.getNumericCellValue();
		} else {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)数据格式必须为数值;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		return value;
	}
	
	/**
	 * 返回单元格double值
	 * @param XSSFRow 行对象
	 * @param cellNum 所在列数
	 * @param reStr 错误信息  
	 * @return String
	 */
	public double cellValueToDouble(XSSFRow row, int cellNum, StringBuffer reStr){
		double value = 0;
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)为空;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			value =  cell.getNumericCellValue();
		} else {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)数据格式必须为数值;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		return value;
	}
	
	/**
	 * 将单元格列数转换为大写字母
	 * @param cellNum 列数 
	 * @return char
	 */
	public char getCellChar(int cellNum){
		return (char) (cellNum + 65);
	}

	
	
	
}
