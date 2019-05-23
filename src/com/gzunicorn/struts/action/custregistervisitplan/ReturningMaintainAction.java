package com.gzunicorn.struts.action.custregistervisitplan;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
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
import com.gzunicorn.common.util.JSONUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.custregistervisitplan.returningmaintain.ReturningMaintain;
import com.gzunicorn.hibernate.custregistervisitplan.returningmaintaindetail.ReturningMaintainDetail;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractdetail.EntrustContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster.EntrustContractMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class ReturningMaintainAction extends DispatchAction {

	Log log = LogFactory.getLog(ReturningMaintainAction.class);
	
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
				SysRightsUtil.NODE_ID_FORWARD + "returningmaintain", null);
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
		
		request.setAttribute("navigator.location","客户回访维护>> 查询列表");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "returningMaintainList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fReturningMaintain");
			table.setLength(3000);
			cache.updateTable(table);
			table.setSortColumn("r.companyId");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			}else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String reMark = tableForm.getProperty("reMark");
			String enabledFlag = tableForm.getProperty("enabledFlag");
			String contacts = tableForm.getProperty("contacts");
			String contactPhone = tableForm.getProperty("contactPhone");
			String maintContractNo = tableForm.getProperty("maintContractNo");
			
			Session hs = null;
			Query query=null;
			String order="";
			try {
                 
				hs = HibernateUtil.getSession();
				String sql = "select r from ReturningMaintain r where 1=1";
				
				if (reMark != null && !reMark.equals("")) {
					sql += " and r.reMark like '%" + reMark.trim() + "%'";
				}
				if (enabledFlag != null && !enabledFlag.equals("")) {
					sql += " and r.enabledFlag like '%" + enabledFlag + "%'";
				}
				if (contacts != null && !contacts.equals("")) {
					sql += " and r.contacts like '%" + contacts.trim() + "%'";
				}
				if (contactPhone != null && !contactPhone.equals("")) {
					sql += " and r.contactPhone like '%" + contactPhone.trim() + "%'";
				}
				if (maintContractNo != null && !maintContractNo.equals("")) {
					sql += " and r.billno in(select distinct billno from ReturningMaintainDetail where maintContractNo like '%" + maintContractNo.trim() + "%') ";
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
				
				List list=query.list();

				cache.check(table);
				List returningMaintainList =list;
				table.addAll(returningMaintainList);
				session.setAttribute("returningMaintainList", table);

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
			forward = mapping.findForward("returningMaintainList");
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
		request.setAttribute("navigator.location","客户回访维护 >> 查看");
		
		ActionForward forward = null;
		
		String id =  (String) dform.get("id");
		Session hs = null;
		Transaction tx = null;
		
		ReturningMaintain returningMaintain = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();//lijun add 20160430
				
				Query query = hs.createQuery("from ReturningMaintain r where r.billno = :billno");
				query.setString("billno", id);
				List list = query.list();
				
				if (list != null && list.size() > 0) {
					returningMaintain = (ReturningMaintain) list.get(0);
					String hql2="from Customer c where c.companyId='" + returningMaintain.getCompanyId().trim() + "'";
				      List list2=hs.createQuery(hql2).list();
					
				      if(list2!=null && list2.size()>0){
				    	Customer customer = (Customer) list2.get(0);
				    	returningMaintain.setR1(customer.getCompanyName());
				      }
				      
				      List detailList=hs.createQuery("from ReturningMaintainDetail where billno='"+returningMaintain.getBillno()+"'").list();
						request.setAttribute("returningMaintainDetailList", detailList);
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}

				if (returningMaintain == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"display.recordnotfounterror"));
				}

				request.setAttribute("returningMaintainBean", returningMaintain);
				
			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				if(tx!=null){
					tx.rollback();//lijun add 20160430
				}
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}

			request.setAttribute("display", "yes");
			forward = mapping.findForward("returningMaintainDisplay");

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

		request.setAttribute("navigator.location","客户回访维护>> 添加");

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
			dform.set("reMark", "Y");
			dform.set("enabledFlag", "Y");
		}
		
		dform.set("reOrder", 1);
		Session hs = null;
		try {
			hs = HibernateUtil.getSession();
			String sql="from ReturningMaintain order by reOrder desc";
			List rmlist=hs.createQuery(sql).list();
			if(rmlist!=null && rmlist.size()>0){
				ReturningMaintain rm=(ReturningMaintain)rmlist.get(0);
				dform.set("reOrder", rm.getReOrder()+1);
			}		
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}
		
		dform.set("reMark", "Y");
		dform.set("enabledFlag", "Y");

		return mapping.findForward("returningMaintainAdd");
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

		ActionErrors errors = new ActionErrors();


		addOrUpdate(form, request, errors);

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
		
		request.setAttribute("navigator.location","客户回访维护 >> 修改");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		ActionForward forward = null;
		String id = null;

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("billno");
		} else{
			id = (String) dform.get("id");
		}
		
		Session hs = null;
		Transaction tx = null;
		ReturningMaintain returningMaintain = null;
		if (id != null) {
			//if (request.getAttribute("error") == null
			//		|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					tx = hs.beginTransaction();//lijun add 20160430
					
					returningMaintain = (ReturningMaintain) hs.get(ReturningMaintain.class, id);
					if(returningMaintain!=null){
						String hql="from Customer c where c.companyId='" + returningMaintain.getCompanyId().trim() + "'";
					      List customerList=hs.createQuery(hql).list();
					      if(customerList!=null && customerList.size()>0){
					    	Customer customer = (Customer) customerList.get(0);
					    	returningMaintain.setR1(customer.getCompanyName());
					      }	
						List list=hs.createQuery("from ReturningMaintainDetail where billno='"+returningMaintain.getBillno()+"'").list();
						request.setAttribute("returningMaintainDetailList", list);
					}
					//if (returningMaintain == null) {
					//	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					//			"returningmaintain.display.recordnotfounterror"
					//			));
					//}
					request.setAttribute("returningMaintainBean", returningMaintain);
					
				} catch (DataStoreException e) {
					e.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					if(tx!=null){
						tx.rollback();//lijun add 20160430
					}
					try {
						hs.close();
					} catch (HibernateException hex) {
						log.error(hex.getMessage());
						DebugUtil
								.print(hex, "HibernateUtil Hibernate Session ");
					}
				}
			//}
			forward = mapping.findForward("returningMaintainModify");
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

		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
	
		addOrUpdate(form, request, errors);

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

			ReturningMaintain returningMaintain = (ReturningMaintain) hs.get(ReturningMaintain.class, (String) dform.get("id"));
			if (returningMaintain != null) {
				hs.createQuery("delete ReturningMaintainDetail where billno='"+returningMaintain.getBillno()+"'").executeUpdate();
				hs.delete(returningMaintain);
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
		String r1 = tableForm.getProperty("r1");	
		String reMark = tableForm.getProperty("reMark");
		String enabledFlag = tableForm.getProperty("enabledFlag");
		
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(ReturningMaintain.class);
			if (reMark != null && !reMark.equals("")) {
				criteria.add(Expression.like("reMark", "%" + reMark.trim()
						+ "%"));
			}
			if (enabledFlag != null && !enabledFlag.equals("")) {
				criteria.add(Expression.eq("enabledFlag", enabledFlag));
			}

			criteria.addOrder(Order.asc("companyId"));

			List roleList = criteria.list();

			XSSFSheet sheet = wb.createSheet("客户回访维护");
			
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short)0);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.contacts"));
				
				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.contactPhone"));
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.reOrder"));
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.reMark"));
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.enabledflag"));
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.rem"));
				
				cell0 = row0.createCell((short)6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.operId"));
				
				cell0 = row0.createCell((short)7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.operDate"));
				
				for (int i = 0; i < l; i++) {
					ReturningMaintain returningMaintain = (ReturningMaintain) roleList.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);
	
					
					// 创建Excel列
					XSSFCell cell = row.createCell((short)0);
					cell.setCellValue(returningMaintain.getContacts());
					
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(returningMaintain.getContactPhone());
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(returningMaintain.getReOrder());
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(returningMaintain.getReMark()));
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(returningMaintain.getEnabledFlag()));

					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(returningMaintain.getRem());

					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName(hs, "LoginUser", "userName", "userId",returningMaintain.getOperId()));
					
					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(returningMaintain.getOperDate());

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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("客户回访维护", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	
	/**
	 * 保存数据方法
	 * @param form
	 * @param request
	 * @return ActionErrors
	 */
	public void addOrUpdate(ActionForm form, HttpServletRequest request,ActionErrors errors) {
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ReturningMaintain master = null;
		ReturningMaintainDetail detail = null;
		String id = (String) dform.get("id");
	
		String contactPhone=(String)dform.get("contactPhone");
		String billNo = null;

		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			boolean flag = true;// 是否生成维保合同号标志

			if (id != null && !id.equals("")) { // 修改		
					
				master = (ReturningMaintain) hs.get(ReturningMaintain.class, id);
				billNo = master.getBillno();
				if(!contactPhone.equals(master.getContactPhone())){
					List contactPhoneList=hs.createQuery("from ReturningMaintain where contactPhone='"+contactPhone+"' and billno!='"+master.getBillno()+"'").list();
					if(contactPhoneList!=null && contactPhoneList.size()>0){
						flag=false;
					}
				}
				//flag = !master.getContractNatureOf().equals(dform.get("contractNatureOf"));
				
			} else { // 新增
				master = new ReturningMaintain();	
				String todayDate = CommonUtil.getToday();
				String year = todayDate.substring(2,4);
				billNo = CommonUtil.getBillno(year,"ReturningMaintain", 1)[0];// 生成流水号		
				id=billNo;
				List contactPhoneList=hs.createQuery("from ReturningMaintain where contactPhone='"+contactPhone+"'").list();
				if(contactPhoneList!=null && contactPhoneList.size()>0){
					flag = false;
				}
			}
			/*List contactPhoneList=hs.createQuery("from ReturningMaintain where contactPhone='"+contactPhone+"'").list();
			if(contactPhoneList!=null && contactPhoneList.size()>0){
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("合同联系人电话已存在！"));
			}else{*/
			if(flag){
				BeanUtils.populate(master, dform.getMap()); // 复制所有属性值
				
				master.setBillno(billNo);// 流水号
				master.setOperDate(CommonUtil.getNowTime());
				master.setOperId(userInfo.getUserID());
				hs.save(master);
	
				// 合同明细
				hs.createQuery("delete from ReturningMaintainDetail where billNo='"+id+"'").executeUpdate();	
				
				String details = (String) dform.get("contracts");
				List list = JSONUtil.jsonToList(details, "contracts");
				//List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型列表
				for (Object object : list) {
					detail = new ReturningMaintainDetail();
					BeanUtils.populate(detail, (Map)object);
					detail.setBillno(billNo);
					////System.out.println(detail.getNumno());
					hs.save(detail);				
				}
			
				tx.commit();
	
				request.setAttribute("id", id);
				dform.set("id", id);
			}else{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("合同联系人电话已存在！"));
				request.setAttribute("errorstrinfo", "合同联系人电话已存在！");
			}
		} catch (Exception e1) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			try {
				tx.rollback();
			} catch (HibernateException e2) {
				log.error(e2.getMessage());
				DebugUtil.print(e2, "Hibernate Transaction rollback error!");
			}
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
		
	}

}
