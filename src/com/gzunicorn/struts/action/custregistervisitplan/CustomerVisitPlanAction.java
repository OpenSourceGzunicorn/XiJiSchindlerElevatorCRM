package com.gzunicorn.struts.action.custregistervisitplan;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
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

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.hibernate.custregistervisitplan.customervisitdispatching.CustomerVisitDispatching;
import com.gzunicorn.hibernate.custregistervisitplan.customervisitfeedback.CustomerVisitFeedback;
import com.gzunicorn.hibernate.custregistervisitplan.customervisitplandetail.CustomerVisitPlanDetail;
import com.gzunicorn.hibernate.custregistervisitplan.customervisitplanmaster.CustomerVisitPlanMaster;
import com.gzunicorn.hibernate.custregistervisitplan.custreturnregistermaster.CustReturnRegisterMaster;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class CustomerVisitPlanAction extends DispatchAction {

	Log log = LogFactory.getLog(CustomerVisitPlanAction.class);
	
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
				SysRightsUtil.NODE_ID_FORWARD + "customervisitplan", null);
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

		request.setAttribute("navigator.location","	客户拜访计划管理>> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		
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
		
			HTMLTableCache cache = new HTMLTableCache(session, "customerVisitList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fCustomerVisitPlan");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("custLevel");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			String companyName = tableForm.getProperty("companyName");
			String principalName = tableForm.getProperty("principalName");
			String sdate1=tableForm.getProperty("sdate1");
			String edate1=tableForm.getProperty("edate1");
			String maintDivision = tableForm.getProperty("maintDivision");
			String mainStation = tableForm.getProperty("assignedMainStation");
			String visitPosition = tableForm.getProperty("visitPosition");
			String visitStaff = tableForm.getProperty("visitStaff");
			String isVisit = tableForm.getProperty("isVisit");
			
			
			if(!userInfo.getComID().equals("00")){
				if(maintDivision == null || maintDivision.trim().equals("")){
					maintDivision = userInfo.getComID();
				}
			}
			
			Session hs = null;
			Query query =null;
			String order="";
			try {
				hs = HibernateUtil.getSession();
				
				//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
				String roleidkk="N";
				String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
				List vmlist=hs.createSQLQuery(sqlss).list();
				if(vmlist!=null && vmlist.size()>0){
					roleidkk="Y";
					if(mainStation == null || mainStation.trim().equals("")){
						mainStation=userInfo.getStorageId();
					}
				}
				
                String hql="select vcl.Orderby as custLevel,cvpm.companyName,cvpm.principalName,cvpm.principalPhone,"
                		+ "cvpm.maintDivision,cvpm.maintStation,cvpd.visitDate,cvpd.visitStaff,cvpd.rem,"
                		+ "cvpd.jnlno,cvf.numno as isVisit,cvpd.visitPosition "
                		+ "from CustomerVisitPlanMaster cvpm "
                		+ "left join ViewCustLevel vcl on vcl.custLevel = cvpm.custLevel,"
                		+ "CustomerVisitPlanDetail cvpd "
                		+ "left join CustomerVisitFeedback cvf on cvf.jnlno=cvpd.jnlno and  "
                		+ "cvf.numno in (select MAX(numno) from CustomerVisitFeedback group by jnlno),"
                		+ "loginuser l "
                		+ "where cvpm.billno=cvpd.billno and cvpd.visitStaff=l.userid ";	 
				if (companyName != null && !companyName.equals("")) {
					hql+=" and cvpm.companyName like '%"+companyName.trim()+"%'";
				}
				
				if (principalName != null && !principalName.equals("")) {
					hql+=" and cvpm.principalName like '%"+principalName.trim()+"%'";
				}
				
				if (maintDivision != null && !maintDivision.equals("")) {
					hql+=" and cvpm.maintDivision like '" + maintDivision + "'";
				}
				
				if (mainStation != null && !mainStation.equals("")) {
					hql+=" and cvpm.maintStation like '"+mainStation.trim()+"' ";
				}
				
				if (visitPosition != null && !visitPosition.equals("")) {
					hql+=" and cvpd.visitPosition= '"+visitPosition.trim()+"'";
				}
				if (visitStaff != null && !visitStaff.equals("")) {
					hql+=" and (l.userid like '%"+visitStaff.trim()+"%' or l.username like '%"+visitStaff.trim()+"%')";
				}
				if (isVisit != null && !isVisit.equals("")) {
					if(isVisit.equals("Y"))
						{
						hql+=" and cvf.numno not like '' and cvf.numno is not  null";
						}
					else
						{
						hql+=" and cvf.numno is null";	
						}
				 }
				
				if (sdate1 != null && !sdate1.equals("")) {
					hql+=" and cvpd.visitDate >= '"+sdate1.trim()+"'";
				}
				if (edate1 != null && !edate1.equals("")) {
					hql+=" and cvpd.visitDate <= '"+edate1.trim()+"'";
				}

				if (table.getIsAscending()) {
					order=" order by "+table.getSortColumn()+" desc,cvpd.jnlno desc";
				} else {
					order=" order by "+table.getSortColumn()+",cvpd.jnlno desc";
				}
				//System.out.println(hql+order);
				query = hs.createSQLQuery(hql+order);
				
				table.setVolume(query.list().size());// 查询得出数据记录数;
				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				cache.check(table);
				List customerVisitPlanList =new ArrayList();
				List list=query.list();
				if(list!=null&list.size()>0)
				{
					int length=list.size();
					for(int i=0;i<length;i++)
					{
						Object[] objects=(Object[]) list.get(i);
						HashMap map=new HashMap();
						map.put("custLevel", bd.getName(hs, "ViewCustLevel", "CustLevel", "Orderby",(String) objects[0]));
						map.put("companyName", objects[1]);
						map.put("principalName", objects[2]);
						map.put("principalPhone", objects[3]);
						map.put("maintDivision", bd.getName(hs, "company", "ComName", "ComID", (String) objects[4]));
						map.put("maintStation", bd.getName(hs, "StorageID", "StorageName", "StorageID", (String) objects[5]));
						map.put("visitDate", objects[6]);
						map.put("visitPosition",bd.getName(hs, "Class1", "Class1Name", "Class1ID",(String) objects[11]));						
						map.put("visitStaff", bd.getName(hs, "Loginuser", "username", "userid", (String) objects[7]));
						map.put("rem", objects[8]);
						map.put("jnlno", objects[9]);
						Integer isVisit1=(Integer)objects[10];
						if(isVisit1!=null&&isVisit1>0){
						 map.put("isVisit", "是");
						}
						else{
						 map.put("isVisit", "否");
						}
						customerVisitPlanList.add(map);
					}
				}
				

				table.addAll(customerVisitPlanList);
			    request.setAttribute("CompanyList", Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
			    if (maintDivision != null && !maintDivision.equals("") && !maintDivision.equals("%")) {
				 String hql1="select a from Storageid a,Company b where a.comid = b.comid"
				 		+" and a.comid = '"+maintDivision+"' and a.storagetype=1 "
				 		+ "and a.parentstorageid='0' and a.enabledflag='Y' ";
				 	//A03  维保经理  只能看自己维保站下面的合同，维保站文员 A48, 安装维保分总  068 
				 	if(roleidkk.equals("Y")){
					 	hql1+=" and a.storageid='"+userInfo.getStorageId()+"' ";
					}
					hql1+=" order by a.storageid";
					List mainStationList=hs.createQuery(hql1).list();
					if(mainStationList!=null && mainStationList.size()>0){
						request.setAttribute("mainStationList", mainStationList);
				    }
				}
			    request.setAttribute("isselectval", roleidkk);
			    String sql="from Class1 c where c.r1='Y'and c.enabledFlag='Y'";
				List class1List=hs.createQuery(sql).list();
				
				   
				session.setAttribute("class1List", class1List);
			    
			    
				session.setAttribute("customerVisitList", table);

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
			forward = mapping.findForward("customerVisitPlanList");
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
		HttpSession session = request.getSession();
		request.setAttribute("navigator.location","客户拜访计划管理 >> 添加");
		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);	
		}
		Session hs = null;
		
		try {
			hs= HibernateUtil.getSession();
		
		   String sql="from Class1 c where c.r1='Y'and c.enabledFlag='Y'";
		   List class1List=hs.createQuery(sql).list();
		
		   String toToday=DateUtil.getNowTime("yyyy-MM-dd");
		   request.setAttribute("class1List", class1List);
		   request.setAttribute("toToday", toToday);
		} catch (DataStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		

		
    	return mapping.findForward("customerVisitPlanAdd");
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
		
		String companyId = (String) dform.get("companyId");
		String principalName = (String) dform.get("principalName");
		String principalPhone = (String) dform.get("principalPhone");
		String maintDivision = (String) dform.get("maintDivision");	
		String maintStation = (String) dform.get("maintStation");
		
		String[] visitDate=request.getParameterValues("visitDate");
		String[] visitStaff=request.getParameterValues("visitStaff");
		String[] visitPosition=request.getParameterValues("visitPosition");
		String[] rem=request.getParameterValues("rem");

		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			CustomerVisitPlanMaster customerVisitPlanMaster=new CustomerVisitPlanMaster();
			
			
			String year1=CommonUtil.getToday().substring(2,4);
			String[] billno1 = CommonUtil.getBillno(year1,"CustomerVisitPlanMaster", 1);
			
				customerVisitPlanMaster.setBillno(billno1[0]);
				customerVisitPlanMaster.setCompanyId(companyId);
				customerVisitPlanMaster.setCompanyName(bd.getName(hs, "Customer", "CompanyName", "CompanyId", companyId));
				customerVisitPlanMaster.setCustLevel(bd.getName(hs, "Customer", "CustLevel", "CompanyId", companyId));
				customerVisitPlanMaster.setPrincipalName(principalName);
				customerVisitPlanMaster.setPrincipalPhone(principalPhone);
				customerVisitPlanMaster.setMaintDivision(maintDivision.trim());
				customerVisitPlanMaster.setMaintStation(maintStation.trim());
				customerVisitPlanMaster.setOperId(userInfo.getUserID());//录入人
				customerVisitPlanMaster.setOperDate(CommonUtil.getToday());//录入时间
				hs.save(customerVisitPlanMaster);
			if(visitDate!=null&&visitDate.length >0){
			String[] billno2 = CommonUtil.getBillno(year1,"CustomerVisitPlanDetail", visitDate.length);
				for(int i=0;i<visitDate.length;i++)
				{
					CustomerVisitPlanDetail customerVisitPlanDetail=new CustomerVisitPlanDetail();
					customerVisitPlanDetail.setJnlno(billno2[i]);
					customerVisitPlanDetail.setVisitDate(visitDate[i]);
					customerVisitPlanDetail.setVisitStaff(visitStaff[i]);
					customerVisitPlanDetail.setVisitPosition(visitPosition[i]);
					customerVisitPlanDetail.setRem(rem[i]);
					customerVisitPlanDetail.setBillno(customerVisitPlanMaster.getBillno());
	
					hs.save(customerVisitPlanDetail);	
				}
			}
		tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("customervisitplan.insert.duplicatekeyerror"));
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
		
		request.setAttribute("navigator.location","客户拜访计划管理 >> 修改");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		ActionForward forward = null;
		String id = null;

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("jnlno");
		} else {
			id = (String) dform.get("id");
		}
		
		Session hs = null;
		CustomerVisitPlanDetail customerVisitPlanDetail = null;
		CustomerVisitPlanMaster customerVisitPlanMaster = null;
		if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					customerVisitPlanDetail = (CustomerVisitPlanDetail) hs.get(CustomerVisitPlanDetail.class, id);
					String sql ="from CustomerVisitPlanMaster cvpm where cvpm.billno in(select max(cvpd.billno) from CustomerVisitPlanDetail cvpd where cvpd.jnlno ='"+id+"')";
					customerVisitPlanMaster = (CustomerVisitPlanMaster) hs.createQuery(sql).list().get(0);
					
					request.setAttribute("comName", bd.getName(hs, "company", "ComName", "ComID",customerVisitPlanMaster.getMaintDivision()));
					request.setAttribute("storageName",bd.getName(hs, "StorageID", "StorageName", "StorageID",customerVisitPlanMaster.getMaintStation()));
					
					if (customerVisitPlanDetail == null) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"hotlinemotherboardtype.display.recordnotfounterror"));
					}
					sql="from Class1 c where c.r1='Y'and c.enabledFlag='Y'";
					   List class1List=hs.createQuery(sql).list();
					   request.setAttribute("class1List", class1List);
					   
				
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
			
			 
			
			request.setAttribute("customerVisitPlanDetailBean", customerVisitPlanDetail);
			request.setAttribute("cvpm", customerVisitPlanMaster);
			forward = mapping.findForward("customerVisitPlanModify");
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
		
		String jnlno = (String) dform.get("jnlno");
		String visitPosition = (String) dform.get("visitPosition");
		String visitStaff = (String) dform.get("visitStaff");
		String visitDate = request.getParameter("visitDate");
		String rem = (String) dform.get("rem");
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			CustomerVisitPlanDetail customerVisitPlanDetail = (CustomerVisitPlanDetail) hs.get(CustomerVisitPlanDetail.class, jnlno.trim());
			if (dform.get("id") != null
					&& jnlno != null
					&& ((String) dform.get("id")).equals(jnlno.trim())) {
				
				customerVisitPlanDetail.setVisitDate(visitDate);
				customerVisitPlanDetail.setVisitStaff(visitStaff);
				customerVisitPlanDetail.setVisitPosition(visitPosition);
				customerVisitPlanDetail.setRem(rem);
				hs.update(customerVisitPlanDetail);
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("customervisitplan.update.duplicatekeyerror"));
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
		Connection conn = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			conn = hs.connection();
			
			CustomerVisitPlanDetail customerVisitPlanDetail = (CustomerVisitPlanDetail) hs.get(CustomerVisitPlanDetail.class, (String) dform.get("id"));
            if (customerVisitPlanDetail != null) { 
            		
            		String sql2 ="delete CustomerVisitDispatching  where jnlno = '"+customerVisitPlanDetail.getJnlno()+"'";
        		    //System.out.println(sql2);
            		conn.prepareStatement(sql2).executeUpdate();
        		    String hql ="delete CustomerVisitFeedback where jnlno = '"+customerVisitPlanDetail.getJnlno()+"'";
        		    //System.out.println(hql);
        		    conn.prepareStatement(hql).executeUpdate();
            		hs.delete(customerVisitPlanDetail);
    				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.succeed"));
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

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

        String elevatorType = tableForm.getProperty("elevatorType");
		String enabledFlag = tableForm.getProperty("enabledFlag");
		String floor =tableForm.getProperty("floor");
		
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();
		
		String companyName = tableForm.getProperty("companyName");
		String principalName = tableForm.getProperty("principalName");
		String sdate1=tableForm.getProperty("sdate1");
		String edate1=tableForm.getProperty("edate1");
		String maintDivision = tableForm.getProperty("maintDivision");
		String mainStation = tableForm.getProperty("assignedMainStation");
		String visitStaff = tableForm.getProperty("visitStaff");
		String isVisit = tableForm.getProperty("isVisit");
		
		
		if(!userInfo.getComID().equals("00")){
			if(maintDivision == null || maintDivision.trim().equals("")){
				maintDivision = userInfo.getComID();
			}
			}
		Query query =null;
		String order="";
		try {
			hs = HibernateUtil.getSession();
            String hql="select vcl.Orderby as custLevel,cvpm.companyName,cvpm.principalName,cvpm.principalPhone,"
            		+ "cvpm.maintDivision,cvpm.maintStation,cvpd.visitDate,cvpd.visitStaff,cvpd.rem,"
            		+ "cvpd.jnlno,cvf.numno as isVisit,cvpd.visitPosition,cvf.VisitProject,cvf.RealVisitDate,cvf.VisitContent " 
            		+ "from CustomerVisitPlanMaster cvpm left join "
            		+ "ViewCustLevel vcl on vcl.custLevel = cvpm.custLevel,CustomerVisitPlanDetail cvpd "
            		+ "left join CustomerVisitFeedback cvf on cvf.jnlno=cvpd.jnlno "
            		+ "where cvpm.billno=cvpd.billno ";	 
			if (companyName != null && !companyName.equals("")) {
				hql+=" and cvpm.companyName like '%"+companyName.trim()+"%'";
			}
			
			if (principalName != null && !principalName.equals("")) {
				hql+=" and cvpm.principalName like '%"+principalName.trim()+"%'";
			}
			
			if (maintDivision != null && !maintDivision.equals("")) {
				hql+=" and cvpm.maintDivision like '" + maintDivision + "'";
			}
			
			if (mainStation != null && !mainStation.equals("")) {
				hql+=" and cvpm.maintStation like '"+mainStation.trim()+"' ";
			}
			
			if (visitStaff != null && !visitStaff.equals("")) {
				hql+=" and cvpd.visitStaff= '"+visitStaff.trim()+"'";
			}
			
			if (isVisit != null && !isVisit.equals("")) {
				if(isVisit.equals("Y"))
					{
						hql+=" and cvf.numno is null";
					}
				else
					{
						hql+=" and cvf.numno not like '' and cvf.numno is not  null";
					}
			 }
			if (sdate1 != null && !sdate1.equals("")) {
				hql+=" and cvpd.visitDate >= '"+sdate1.trim()+"'";
			}
			if (edate1 != null && !edate1.equals("")) {
				hql+=" and cvpd.visitDate <= '"+edate1.trim()+"'";
			}
				order=" order by cvpm.custLevel desc,cvpd.jnlno,cvf.RealVisitDate desc";
			
			query = hs.createSQLQuery(hql+order);
			List roleList = query.list();
			XSSFSheet sheet = wb.createSheet("客户拜访计划");
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);
			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"customerVisitPlan.customerlevel"));

				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"customerlevel.companyName"));
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"customerlevel.principalName"));
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"customerlevel.principalPhone"));
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"customerlevel.maintDivision"));
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"customerlevel.mainStation"));
				
				cell0 = row0.createCell((short)6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"customerVisitPlan.visitDate"));
				
				cell0 = row0.createCell((short)7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("拜访职位");
				
				cell0 = row0.createCell((short)8);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"customerVisitPlan.visitStaff"));
				
				cell0 = row0.createCell((short)9);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"customerVisitPlan.rem"));
				
				cell0 = row0.createCell((short)10);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"customerVisitPlan.isVisit"));
				
				cell0 = row0.createCell((short)11);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("拜访项目");
				
				cell0 = row0.createCell((short)12);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("实际拜访日期");
				
				cell0 = row0.createCell((short)13);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("拜访内容/收获");
			
				for (int i = 0; i < l; i++) {
					Object[] objects = (Object[]) roleList.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);
					// 创建Excel列
					XSSFCell cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);			
					cell.setCellValue(bd.getName(hs, "ViewCustLevel", "CustLevel", "Orderby",(String) objects[0]));
					
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) objects[1]);
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) objects[2]);
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) objects[3]);
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName(hs, "company", "ComName", "ComID", (String) objects[4]));
					
					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName(hs, "StorageID", "StorageName", "StorageID", (String) objects[5]));
					
					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) objects[6]);
					
					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName(hs, "Class1", "Class1Name", "Class1ID",(String) objects[11]));
					
					cell = row.createCell((short)8);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName(hs, "Loginuser", "username", "userid", (String) objects[7]));
					
					cell = row.createCell((short)9);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) objects[8]);

					cell = row.createCell((short)10);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					Integer isVisit2=(Integer)objects[10];
					if(isVisit2!=null&&isVisit2>0){
						cell.setCellValue("否");
					}
					else{
						cell.setCellValue("是");
					}
					
					cell = row.createCell((short)11);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) objects[12]);
					
					cell = row.createCell((short)12);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) objects[13]);
					
					cell = row.createCell((short)13);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) objects[14]);
					
					

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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("客户拜访计划", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}

	/**
	 * ajax 级联 分部与维保站
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public void toStorageIDList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		Session hs=null;
		String comid=request.getParameter("comid");
		response.setHeader("Content-Type","text/html; charset=GBK");
		List list2=new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='GBK'?>");
		sb.append("<root>");
		try {
			hs=HibernateUtil.getSession();
			if(comid!=null && !"".equals(comid)){
			String hql="select a from Storageid a,Company b where a.comid = b.comid and a.comid='"+comid+"'" +
					" and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
			List list=hs.createQuery(hql).list();
			if(list!=null && list.size()>0){
				sb.append("<rows>");
				for(int i=0;i<list.size();i++){
				Storageid sid=(Storageid)list.get(i);
				sb.append("<cols name='"+sid.getStoragename()+"' value='"+sid.getStorageid()+"'>").append("</cols>");
				}
				sb.append("</rows>");
							
				
			  }
			 }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		finally{
			hs.close();
		}
		sb.append("</root>");
		
		response.setCharacterEncoding("gbk"); 
		response.setContentType("text/xml;charset=gbk");
		response.getWriter().write(sb.toString());
			}
	 
	
	/**
	 * 点击查看方法
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

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		request.setAttribute("navigator.location", "客户拜访计划 >> 查看");

		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		CustReturnRegisterMaster custReturnRegisterMaster = null;
		List custReturnRegisterDetailList = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				   String sql="select cvpm,cvpd from CustomerVisitPlanMaster cvpm,CustomerVisitPlanDetail cvpd where cvpm.billno=cvpd.billno and cvpd.jnlno='"+id+"'";
					   List list=hs.createQuery(sql).list();		
					    
					   CustomerVisitPlanDetail cvpd=null;
					   CustomerVisitPlanMaster cvpm=null;
					   if(list!=null&&list.size()>0){
						  Object[] objects=(Object[]) list.get(0);
						  cvpm=(CustomerVisitPlanMaster) objects[0];
						  cvpd=(CustomerVisitPlanDetail) objects[1];
						  cvpm.setMaintDivision(bd.getName(hs, "company", "ComName", "ComID", cvpm.getMaintDivision()));
						  cvpm.setMaintStation(bd.getName(hs, "StorageID", "StorageName", "StorageID", cvpm.getMaintStation()));
						  cvpd.setBillno(cvpm.getBillno());
						  cvpd.setVisitStaff(bd.getName(hs, "Loginuser", "username", "userid",cvpd.getVisitStaff()));
						  cvpd.setVisitPosition(bd.getName(hs, "Class1", "Class1Name", "Class1ID", cvpd.getVisitPosition()));
						  cvpd.setfMinisterFollow(bd.getName(hs, "Loginuser", "username", "userid",cvpd.getfMinisterFollow()));
						  cvpd.setManagerFollow(bd.getName(hs, "Loginuser", "username", "userid",cvpd.getManagerFollow()));
						  cvpd.setzMinisterFollow(bd.getName(hs, "Loginuser", "username", "userid",cvpd.getzMinisterFollow()));
						  cvpd.setAuditOperid(bd.getName(hs, "Loginuser", "username", "userid",cvpd.getAuditOperid()));
					  }
					   
					   String hql="from CustomerVisitFeedback cvf where cvf.customerVisitPlanDetail.jnlno='"+id+"'";
					   List list2=hs.createQuery(hql).list();
					   List cvpfList=new ArrayList();
					   if(list2!=null&&list2.size()>0)
					   {
						   for(int i=0;i<list2.size();i++){
						   CustomerVisitFeedback cvpf= (CustomerVisitFeedback) list2.get(i);
						   cvpfList.add(cvpf);
						   }						   						 						   
						   request.setAttribute("cvpfList", cvpfList);
					   }
					   
					   String hql1="from CustomerVisitDispatching cvd where cvd.customerVisitPlanDetail.jnlno='"+id+"'";
					   List tocvdlist=hs.createQuery(hql1).list();
					   List cvdlist=new ArrayList();
					   if(tocvdlist!=null&&tocvdlist.size()>0){
					     for(int i=0;i<tocvdlist.size();i++){
					    	 CustomerVisitDispatching cvd=(CustomerVisitDispatching) tocvdlist.get(i);
					    	 cvd.setTransferId(bd.getName(hs, "Loginuser", "username", "userid", cvd.getTransferId()));
					    	 cvd.setVisitStaff(bd.getName(hs, "Loginuser", "username", "userid", cvd.getVisitStaff()));
					    	 //System.out.println(bd.getName(hs, "Loginuser", "username", "userid", cvd.getVisitStaff()));
					    	 
					    	 cvd.setVisitPosition(bd.getName(hs, "Class1", "Class1Name", "Class1ID", cvd.getVisitPosition()));
					    	 cvdlist.add(cvd);
					     }
					     request.setAttribute("cvdList", cvdlist);
					   }
					   
					   request.setAttribute("cvpmbean", cvpm);
					   request.setAttribute("cvpdbean", cvpd);
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
			forward = mapping.findForward("customerVisitPlanDisplay");

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	@SuppressWarnings("unchecked")
	public void getPersonnel(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String maintDivision = request.getParameter("maintDivision"); //维保分部
		String mainStation = request.getParameter("mainStation"); //维保站
		String visitPosition = request.getParameter("visitPosition"); //职位
		JSONArray jsonArr = new JSONArray();
		JSONObject json = null;
		if(mainStation != null && !mainStation.equals("")){

			Session hs = null;
			Query query = null;
			try {
				hs = HibernateUtil.getSession();
				String sql="";
				if(visitPosition!=null && !visitPosition.trim().equals("")){
					/**
					if(visitPosition.trim().equals("17")){//维保分部长
						sql="from Loginuser l where l.class1id='17' and l.enabledflag='Y' and l.grcid ='"+maintDivision+"'";
					}else if(visitPosition.trim().equals("20")){//维保经理
						sql="from Loginuser l where l.class1id='20' and l.enabledflag='Y' and l.grcid ='"+maintDivision+"' and l.storageid like '"+mainStation+"%'";
					}else if(visitPosition.trim().equals("22")){//维保站长
						sql="from Loginuser l where l.class1id='22' and l.enabledflag='Y' and l.grcid ='"+maintDivision+"' and l.storageid like '"+mainStation+"%'";
					}else if(visitPosition.trim().equals("27")){//维保总部长
						sql="from Loginuser l where l.class1id='27' and l.enabledflag='Y'";
					}*/
					if(visitPosition.trim().equals("27")){//维保总部长
						maintDivision="%";
						mainStation="%";
					}else if(visitPosition.trim().equals("17")){//维保分部长
						mainStation="%";
					}else if(visitPosition.trim().equals("100")){//服务销售专员
						mainStation="%";
					}
					   
					sql="from Loginuser l where l.class1id='"+visitPosition.trim()+"' and l.enabledflag='Y' " +
							"and l.grcid like '"+maintDivision+"' and l.storageid like '"+mainStation+"%'";
				}
				List list =hs.createQuery(sql).list();
				if(list!=null&&list.size()>0)
				{
					for(int j = 0;j<list.size();j++)
					{
						Loginuser l = (Loginuser)list.get(j);
						json = new JSONObject();
						json.put("id", l.getUserid());//id
						json.put("name", l.getUsername());// name
						jsonArr.put(json);	
					}						
				}

				ServletOutputStream stream = response.getOutputStream();
				stream.write(jsonArr.toString().replace("null", "").getBytes("UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			} finally { 
				if(hs != null){
					hs.close();
				}
			}
			

			
		}

	}
	/**
	 * 复制拜访计划
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toCopyRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		//HttpSession session = request.getSession();
		//ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String idstr = request.getParameter("idstr");
		//System.out.println(">>>>"+idstr);
		
		try {
			if(!"".equals(idstr)){
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				
				String curdate=DateUtil.getNowTime("yyyy-MM-dd");//yyyyMMddHHmmss
				
				String[] jnlnoarr=idstr.split(",");
				String[] jnlnos = CommonUtil.getBillno(CommonUtil.getToday().substring(2,4),"CustomerVisitPlanDetail", jnlnoarr.length);
				
				CustomerVisitPlanDetail cvpd = null;
				for(int i=0;i<jnlnoarr.length;i++){
					cvpd = (CustomerVisitPlanDetail) hs.get(CustomerVisitPlanDetail.class, jnlnoarr[i].trim());
					if(cvpd!=null){
						CustomerVisitPlanDetail newcvp=new CustomerVisitPlanDetail();
						newcvp.setJnlno(jnlnos[i]);
						newcvp.setBillno(cvpd.getBillno());
						newcvp.setVisitDate(curdate);//预计拜访日期
						newcvp.setVisitStaff(cvpd.getVisitStaff());//拜访人员
						newcvp.setVisitPosition(cvpd.getVisitPosition());//拜访职位 
						newcvp.setRem(cvpd.getRem());//备注
						hs.save(newcvp);
					}
				}
				tx.commit();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","复制拜访计划成功!"));
			}else{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","<font color='red'>复制拜访计划失败!</font>"));
			}
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","<font color='red'>复制拜访计划失败!</font>"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				e3.printStackTrace();
			}
			e2.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;
		forward = mapping.findForward("returnList");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		return forward;
	}
	
}
