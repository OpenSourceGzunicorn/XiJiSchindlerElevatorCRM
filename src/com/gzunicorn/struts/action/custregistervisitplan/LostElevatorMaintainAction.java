package com.gzunicorn.struts.action.custregistervisitplan;

import java.io.IOException;
import java.net.URLEncoder;
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

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.JSONUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.custregistervisitplan.LostElevatorMaintain.LostElevatorMaintain;
import com.gzunicorn.hibernate.custregistervisitplan.LostElevatorMaintainDetail.LostElevatorMaintainDetail;
import com.gzunicorn.hibernate.custregistervisitplan.returningmaintain.ReturningMaintain;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class LostElevatorMaintainAction extends DispatchAction {

	Log log = LogFactory.getLog(LostElevatorMaintainAction.class);
	
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
				SysRightsUtil.NODE_ID_FORWARD + "lostElevatorMaintain", null);
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
		
		request.setAttribute("navigator.location","丢梯客户维护>> 查询列表");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "lostElevatorMaintainList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fLostElevatorMaintain");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("r.companyId");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String companyName = tableForm.getProperty("companyName");	
			String reMark = tableForm.getProperty("reMark");
			String enabledFlag = tableForm.getProperty("enabledFlag");
			String contacts = tableForm.getProperty("contacts");
			String contactPhone = tableForm.getProperty("contactPhone");
			
			Session hs = null;
			Query query=null;
			String order="";
			try {
                 
				hs = HibernateUtil.getSession();
				String sql = "select r,c.companyName from LostElevatorMaintain r,Customer c where r.companyId=c.companyId ";
				
				if (companyName != null && !companyName.equals("")) {
					sql += " and c.companyName like '%" + companyName.trim() + "%'";
				}
				if (reMark != null && !reMark.equals("")) {
					sql += " and r.reMark like '%" + reMark + "%'";
				}
				if (enabledFlag != null && !enabledFlag.equals("")) {
					sql += " and r.enabledFlag like '%" + enabledFlag + "%'";
				}
				if (contacts != null && !contacts.equals("")) {
					sql += " and r.contacts like '%" + contacts + "%'";
				}
				if (contactPhone != null && !contactPhone.equals("")) {
					sql += " and r.contactPhone like '%" + contactPhone + "%'";
				}
				if (table.getIsAscending()) {
					order="order by "+table.getSortColumn();
				} else {
					order="order by "+table.getSortColumn()+" desc";
				}
				
				
				query = hs.createQuery(sql+order);
				table.setVolume(query.list().size());// 查询得出数据记录数;
				List list=query.list();
				
				List<LostElevatorMaintain> rmlist= new ArrayList<LostElevatorMaintain>();
				if(list!=null && list.size()>0)
				{
					for(Object object : list){
						Object[] objs=(Object[])object;
						LostElevatorMaintain ret =(LostElevatorMaintain) objs[0];
						ret.setR1(objs[1].toString());
					
					 rmlist.add(ret);
				      
				      
					}		
				}
				
				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);
				
				
				
				List returningMaintainList =rmlist;
				table.addAll(returningMaintainList);
				
				
						
				
				session.setAttribute("lostElevatorMaintainList", table);

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
			forward = mapping.findForward("lostElevatorMaintainList");
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
		request.setAttribute("navigator.location","丢梯客户维护 >> 查看");
		
		ActionForward forward = null;
		
		String id =  (String) dform.get("id");
		Session hs = null;
		LostElevatorMaintain lostElevatorMaintain = null;
	
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from LostElevatorMaintain r where r.billno = :billno");
				query.setString("billno", id);
				
				List list = query.list();
				
				if (list != null && list.size() > 0) {
					lostElevatorMaintain = (LostElevatorMaintain) list.get(0);
					String hql2="from Customer c where c.companyId='" + lostElevatorMaintain.getCompanyId().trim() + "'";
				      List list2=hs.createQuery(hql2).list();
					
				      if(list2!=null && list2.size()>0){
				    	Customer customer = (Customer) list2.get(0);
				    	lostElevatorMaintain.setR1(customer.getCompanyName());
				      }
			
				     String sql="select a.maintContractNo,a.lostElevatorDate,b.pullname from "
				    		  	+"LostElevatorMaintainDetail a,pulldown b where a.CauseAnalysis=b.pullid "
				    		  	+"and a.billno='"+lostElevatorMaintain.getBillno()+"'";
				     
				      List detailList=hs.createSQLQuery(sql).list();
				      List detailsList=new ArrayList();
				      if(detailList!=null && detailList.size()>0){
				    	  for(Object object : detailList){
				    		  Object[] objs=(Object[])object;
				    		  Map map=new HashMap();
				    		  map.put("maintContractNo", objs[0]);
				    		  map.put("lostElevatorDate", objs[1]);
				    		  map.put("causeAnalysis", objs[2]);
				    		  detailsList.add(map);
				    	  }
				      }
				
				     // List detailList=hs.createQuery("from LostElevatorMaintainDetail  where  billno='"+lostElevatorMaintain.getBillno()+"'").list();
						request.setAttribute("lostElevatorMaintainDetailList", detailsList);
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}

				
				
				if (lostElevatorMaintain == null) {
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
			request.setAttribute("lostElevatorMaintainBean", lostElevatorMaintain);
			forward = mapping.findForward("lostElevatorMaintainDisplay");

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

		request.setAttribute("navigator.location","丢梯客户维护>> 添加");

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
			String sql="from LostElevatorMaintain order by reOrder desc";
			List rmlist=hs.createQuery(sql).list();
			if(rmlist!=null && rmlist.size()>0){
				LostElevatorMaintain rm=(LostElevatorMaintain)rmlist.get(0);
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


		return mapping.findForward("lostElevatorMaintainAdd");
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
		
		String contacts=(String) dform.get("contacts");
		String contactPhone=(String) dform.get("contactPhone");
		String companyId = (String) dform.get("companyId");
		Integer reOrder = (Integer) dform.get("reOrder");
		String reMark =  (String) dform.get("reMark");
		String enabledFlag = (String) dform.get("enabledFlag");
		String rem = (String) dform.get("rem");
		String[] maintContractNo=request.getParameterValues("maintContractNo");
		String[] lostElevatorDate=request.getParameterValues("lostElevatorDate");
		String[] causeAnalysis=request.getParameterValues("causeAnalysis");
		String[] jnlno=request.getParameterValues("jnlno");
		String[] wb_billno=request.getParameterValues("wb_billno");
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			LostElevatorMaintain lostElevatorMaintain = new LostElevatorMaintain();
			String year1=CommonUtil.getToday().substring(2,4);
			String[] billno1 = CommonUtil.getBillno(year1,"LostElevatorMaintain", 1);
			lostElevatorMaintain.setBillno(billno1[0]);
			lostElevatorMaintain.setContacts(contacts);
			lostElevatorMaintain.setContactPhone(contactPhone);
			lostElevatorMaintain.setCompanyId(companyId);
			lostElevatorMaintain.setReOrder(reOrder);
			lostElevatorMaintain.setReMark(reMark);
			lostElevatorMaintain.setEnabledFlag(enabledFlag.trim());
			lostElevatorMaintain.setRem(rem.trim());
			lostElevatorMaintain.setOperId(userInfo.getUserID());//录入人
			lostElevatorMaintain.setOperDate(CommonUtil.getToday());//录入时间
			hs.save(lostElevatorMaintain);
			
				if(maintContractNo!=null && !"".equals(maintContractNo)){
					for (int i = 0; i < maintContractNo.length; i++) {
						LostElevatorMaintainDetail lostElevatorMaintainDetail=new LostElevatorMaintainDetail();
						lostElevatorMaintainDetail.setMaintContractNo(maintContractNo[i]);
						lostElevatorMaintainDetail.setLostElevatorDate(lostElevatorDate[i]);
						lostElevatorMaintainDetail.setCauseAnalysis(causeAnalysis[i]);
						lostElevatorMaintainDetail.setWbBillno(wb_billno[i]);
						lostElevatorMaintainDetail.setJnlno(jnlno[i]);
						lostElevatorMaintainDetail.setLostElevatorMaintain(lostElevatorMaintain);
						hs.save(lostElevatorMaintainDetail);
					}
					
				}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("LostElevatorMaintain.insert.duplicatekeyerror"));
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
		
		request.setAttribute("navigator.location","丢梯客户维护 >> 修改");
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
		LostElevatorMaintain lostElevatorMaintain = null;
		
		if (id != null) {
	
				try {
					hs = HibernateUtil.getSession();
					tx = hs.beginTransaction();
					
					lostElevatorMaintain = (LostElevatorMaintain) hs.get(LostElevatorMaintain.class, id);
					if(lostElevatorMaintain!=null){
						String hql="from Customer c where c.companyId='" + lostElevatorMaintain.getCompanyId().trim() + "'";
					      List customerList=hs.createQuery(hql).list();
					      if(customerList!=null && customerList.size()>0){
					    	Customer customer = (Customer) customerList.get(0);
					    	lostElevatorMaintain.setR1(customer.getCompanyName());
					      }
					      String sql="select a.maintContractNo,a.lostElevatorDate,b.pullname,a.jnlno,a.CauseAnalysis,a.wb_billno from "
					    		  	+"LostElevatorMaintainDetail a,pulldown b where a.CauseAnalysis=b.pullid "
					    		  	+"and a.billno='"+lostElevatorMaintain.getBillno()+"'";
					     
					      List detailList=hs.createSQLQuery(sql).list();
					      List detailsList=new ArrayList();
					      if(detailList!=null && detailList.size()>0){
					    	  for(Object object : detailList){
					    		  Object[] objs=(Object[])object;
					    		  Map map=new HashMap();
					    		  map.put("maintContractNo", objs[0]);
					    		  map.put("lostElevatorDate", objs[1]);
					    		  map.put("pullname", objs[2]);
					    		  map.put("jnlno", objs[3]);
					    		  map.put("causeAnalysis", objs[4]);
					    		  map.put("wb_billno", objs[5]);
					    		  
					    		
					    		
					    		  detailsList.add(map);
					    	  }
					      }
					
					      request.setAttribute("lostElevatorMaintainDetailList", detailsList);
						/*List list=hs.createQuery("from LostElevatorMaintainDetail where billno='"+lostElevatorMaintain.getBillno()+"'").list();
						request.setAttribute("lostElevatorMaintainDetailList", list);*/
					}
				
			
					request.setAttribute("lostElevatorMaintainBean", lostElevatorMaintain);
					
				} catch (DataStoreException e) {
					e.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					if(tx!=null){
						tx.rollback();
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
			forward = mapping.findForward("lostElevatorMaintainModify");
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

			LostElevatorMaintain lostElevatorMaintain = (LostElevatorMaintain) hs.get(LostElevatorMaintain.class, (String) dform.get("id"));
			if (lostElevatorMaintain != null) {
				hs.createQuery("delete LostElevatorMaintainDetail where billno='"+lostElevatorMaintain.getBillno()+"'").executeUpdate();
				hs.delete(lostElevatorMaintain);
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

		String companyId = tableForm.getProperty("companyId");	
		String r1 = tableForm.getProperty("r1");	
		String reMark = tableForm.getProperty("reMark");
		String enabledFlag = tableForm.getProperty("enabledFlag");
		
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(LostElevatorMaintain.class);
			if (r1 != null && !r1.equals("")) {
				criteria.add(Expression.like("companyId", "%" + companyId.trim() + "%"));
			}
			if (reMark != null && !reMark.equals("")) {
				criteria.add(Expression.like("reMark", "%" + reMark.trim()
						+ "%"));
			}
			if (enabledFlag != null && !enabledFlag.equals("")) {
				criteria.add(Expression.eq("enabledFlag", enabledFlag));
			}

			criteria.addOrder(Order.asc("companyId"));

			List roleList = criteria.list();

			XSSFSheet sheet = wb.createSheet("丢梯客户维护");
			
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.companyName"));
				
				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.contacts"));
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.contactPhone"));
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.reOrder"));
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.reMark"));
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.enabledflag"));
				
				cell0 = row0.createCell((short)6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.rem"));
				
				cell0 = row0.createCell((short)7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.operId"));
				
				cell0 = row0.createCell((short)8);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"returningmaintain.operDate"));
				
				for (int i = 0; i < l; i++) {
					LostElevatorMaintain lostElevatorMaintain = (LostElevatorMaintain) roleList.get(i);
					Customer customer =null;
					String hql2="from Customer c where c.companyId='" + lostElevatorMaintain.getCompanyId().trim() + "'";
				      List list2=hs.createQuery(hql2).list();
					
				      if(list2!=null && list2.size()>0){
				    	 customer = (Customer) list2.get(0);
				      }
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);
	
					
					// 创建Excel列
					XSSFCell cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(customer.getCompanyName());
					
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(lostElevatorMaintain.getContacts());
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(lostElevatorMaintain.getContactPhone());
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(lostElevatorMaintain.getReOrder());
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(lostElevatorMaintain.getReMark()));
					
					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(lostElevatorMaintain.getEnabledFlag()));

					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(lostElevatorMaintain.getRem());

					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName(hs, "LoginUser", "userName", "userId",lostElevatorMaintain.getOperId()));
					
					cell = row.createCell((short)8);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(lostElevatorMaintain.getOperDate());

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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("丢梯客户维护", "utf-8") + ".xlsx");
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
		LostElevatorMaintain master = null;
		LostElevatorMaintainDetail detail = null;
		String id = (String) dform.get("id");
		String[] wb_billno=request.getParameterValues("wb_billno");
		String contactPhone=(String)dform.get("contactPhone");
		String billNo = null;

		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			boolean flag = true;// 是否生成维保合同号标志

			if (id != null && !id.equals("")) { // 修改		
					
				master = (LostElevatorMaintain) hs.get(LostElevatorMaintain.class, id);
				billNo = master.getBillno();
				if(!contactPhone.equals(master.getContactPhone())){
					List contactPhoneList=hs.createQuery("from LostElevatorMaintain where contactPhone='"+contactPhone+"'").list();
					if(contactPhoneList!=null && contactPhoneList.size()>0){
						flag=false;
					}
				}
				
			} else { // 新增
				master = new LostElevatorMaintain();	
				String todayDate = CommonUtil.getToday();
				String year = todayDate.substring(2,4);
				billNo = CommonUtil.getBillno(year,"LostElevatorMaintain", 1)[0];// 生成流水号		
				id=billNo;
				List contactPhoneList=hs.createQuery("from LostElevatorMaintain where contactPhone='"+contactPhone+"'").list();
				if(contactPhoneList!=null && contactPhoneList.size()>0){
					flag = false;
				}
			}
		
			if(flag){
				BeanUtils.populate(master, dform.getMap()); // 复制所有属性值
				
				master.setBillno(billNo);// 流水号
				master.setOperDate(CommonUtil.getNowTime());
				master.setOperId(userInfo.getUserID());
				hs.save(master);
	
				// 合同明细
				hs.createQuery("delete from LostElevatorMaintainDetail where billNo='"+id+"'").executeUpdate();	
			
				String details = (String) dform.get("contracts");
				List list = JSONUtil.jsonToList(details, "contracts");
				int i=0;
				for (Object object : list) {
					detail = new LostElevatorMaintainDetail();
					BeanUtils.populate(detail, (Map)object);
					detail.setWbBillno(wb_billno[i]);
					detail.setLostElevatorMaintain(master);
					hs.save(detail);				
					i=i+1;
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
