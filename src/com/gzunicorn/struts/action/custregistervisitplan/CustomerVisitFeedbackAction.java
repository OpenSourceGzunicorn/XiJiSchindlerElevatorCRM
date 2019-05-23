package com.gzunicorn.struts.action.custregistervisitplan;

import java.io.IOException;
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

public class CustomerVisitFeedbackAction extends DispatchAction {

	Log log = LogFactory.getLog(CustomerVisitFeedbackAction.class);
	
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
				SysRightsUtil.NODE_ID_FORWARD + "customercisitfeedback", null);
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

		request.setAttribute("navigator.location","	客户拜访反馈>> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
			HTMLTableCache cache = new HTMLTableCache(session, "customerVisitFeedbackList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fCustomerVisitFeedback");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("custLevel");
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
			String principalName = tableForm.getProperty("principalName");
			String sdate1=tableForm.getProperty("sdate1");
			String edate1=tableForm.getProperty("edate1");
			String isSubmitType=tableForm.getProperty("isSubmitType");
			String maintDivision = null;
			String mainStation = null;
			String isVisit = tableForm.getProperty("isVisit");
			if(isSubmitType==null){
				isSubmitType="N";
				tableForm.setProperty("isSubmitType", isSubmitType);
			}
			Session hs = null;
			Query query =null;
			String order="";
			try {
				hs = HibernateUtil.getSession();
                String hql="select vcl.Orderby as custLevel,cvpm.companyName,cvpm.principalName,cvpm.principalPhone,"
                		+ "cvpm.maintDivision,cvpm.maintStation,cvpd.visitDate,cvpd.visitStaff,cvpd.rem,"
                		+ "cvpd.jnlno,cvf.numno as isVisit,cvpd.visitPosition,cvpd.submitType "
                		+ "from CustomerVisitPlanMaster cvpm left join "
                		+ "ViewCustLevel vcl on vcl.custLevel = cvpm.custLevel,CustomerVisitPlanDetail cvpd "
                		+ "left join CustomerVisitFeedback cvf on cvf.jnlno=cvpd.jnlno and  "
                		+ "cvf.numno in (select MAX(numno) from CustomerVisitFeedback group by jnlno) "
                		+ "where cvpm.billno=cvpd.billno and cvpd.visitStaff='"+userInfo.getUserID()+"' ";	 
				if (companyName != null && !companyName.equals("")) {
					hql+=" and cvpm.companyName like '%"+companyName.trim()+"%'";
				}
				
				if (principalName != null && !principalName.equals("")) {
					hql+=" and cvpm.principalName like '%"+principalName.trim()+"%'";
				}
				
				if (maintDivision != null && !maintDivision.equals("")) {
					hql+=" and cvpm.maintDivision like '" + maintDivision + "'";
				}
				
				if(isSubmitType !=null && !isSubmitType.equals("")){
					if(isSubmitType.trim().equals("Y")||isSubmitType=="Y"){
						hql+=" and cvpd.submitType = '" + isSubmitType.trim() + "'";
					}else if(isSubmitType.trim().equals("R")||isSubmitType=="R"){
						hql+=" and cvpd.submitType = '" + isSubmitType.trim() + "'";
					}else{
					    hql+=" and (cvpd.submitType !='Y' or cvpd.submitType is null)";
					}
				}
				
				if (mainStation != null && !mainStation.equals("")) {
					hql+=" and cvpm.maintStation like '"+mainStation.trim()+"' ";
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
					order=" order by "+table.getSortColumn()+" desc";
				} else {
					order=" order by "+table.getSortColumn();
				}
				
				////System.out.println(">>>>>>>>>>>"+hql+order);

				query = hs.createSQLQuery(hql+order);
				
				table.setVolume(query.list().size());// 查询得出数据记录数;
				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				cache.check(table);
				
				List customerVisitFeedbackList =new ArrayList();
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
						String submitType=(String) objects[12];
						if(submitType!=null&&submitType.trim().equals("Y")){
							 map.put("submitType", "是");
						}else if(submitType!=null&&submitType.trim().equals("R")){
							map.put("submitType", "驳回");
						}else{
							map.put("submitType", "否");
						}
						
						customerVisitFeedbackList.add(map);
					}
				}
				

				table.addAll(customerVisitFeedbackList);
			    	request.setAttribute("CompanyList", Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
			    
			    if (maintDivision != null && !maintDivision.equals("")&&!maintDivision.equals("%")) {
				 String hql1="select a from Storageid a,Company b where a.comid = b.comid and a.comid = '"+maintDivision+"' " +
				 		"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
					List mainStationList=hs.createQuery(hql1).list();
					if(mainStationList!=null && mainStationList.size()>0){
						request.setAttribute("mainStationList", mainStationList);
				    }
				}
			    String sql="from Class1 c where c.r1='Y'and c.enabledFlag='Y'";
				   List class1List=hs.createQuery(sql).list();
				
				   
				session.setAttribute("class1List", class1List);
			    
			    
				session.setAttribute("customerVisitFeedbackList", table);

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
			forward = mapping.findForward("customerVisitFeedbackList");
		
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
	 * 跳转到修改级别页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareAddRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		request.setAttribute("navigator.location","	客户拜访反馈 >> 拜访反馈");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ActionForward forward = null;
		String id = null;

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("jnlno");
		} else {
			id = (String) dform.get("id");
		}
		List projectNameList=new ArrayList();
		Session hs = null;
		if(id!=null){
		try {
			hs= HibernateUtil.getSession();
		
		   String sql="select cvpm,cvpd from CustomerVisitPlanMaster cvpm,CustomerVisitPlanDetail cvpd " +
		   		"where cvpm.billno=cvpd.billno and cvpd.jnlno='"+id+"'";
		   List list=hs.createQuery(sql).list();		
		    
		   CustomerVisitPlanDetail cvpd=null;
		   CustomerVisitPlanMaster cvpm=null;
		   if(list!=null&&list.size()>0){
			  Object[] objects=(Object[]) list.get(0);
			  cvpm=(CustomerVisitPlanMaster) objects[0];
			  cvpd=(CustomerVisitPlanDetail) objects[1];
			  cvpm.setMaintDivision(bd.getName(hs, "company", "ComName", "ComID", cvpm.getMaintDivision()));
			  cvpm.setMaintStation(bd.getName(hs, "StorageID", "StorageName", "StorageID",cvpm.getMaintStation()));
			  
			  cvpd.setBillno(cvpm.getBillno());
		  }
		   
		   String hql="from CustomerVisitFeedback cvf where cvf.customerVisitPlanDetail.jnlno='"+id+"'";
		   List cvpfList=hs.createQuery(hql).list();
		   if(cvpfList!=null && cvpfList.size()>0){
			   request.setAttribute("cvpfList", cvpfList);
		   }else{
			   if(cvpm.getMaintContractNo()!=null){
				   String hqlk="select distinct b.projectName from MaintContractMaster a,MaintContractDetail b where a.billNo=b.billNo and a.maintContractNo='"+cvpm.getMaintContractNo()+"'";
				   projectNameList=hs.createQuery(hqlk).list();
				   request.setAttribute("projectNameList2", projectNameList);
			  }
		   }
		   
		   
		   String toToday=DateUtil.getNowTime("yyyy-MM-dd");
		   
		   request.setAttribute("submit", "yes");
		   request.setAttribute("cvpmbean", cvpm);
		   request.setAttribute("cvpdbean", cvpd);
		   request.setAttribute("toToday", toToday);
		   request.setAttribute("userName", userInfo.getUserName());
		   request.setAttribute("class1", bd.getName(hs, "Class1", "Class1Name", "Class1ID", cvpd.getVisitPosition()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		
		}
		
		}
		
    	return mapping.findForward("customerVisitFeedbackAdd");
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
	public ActionForward toAddRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
				
		ActionErrors errors = new ActionErrors();
		DynaActionForm dform = (DynaActionForm) form;
		
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String[] projectName=request.getParameterValues("projectName");
		String[] realvisitDate=request.getParameterValues("realvisitDate");
		String[] visitContent=request.getParameterValues("visitContent");
		String isSubmitType=request.getParameter("isSubmitType");
		String jnlno= (String) dform.get("jnlno");
		String visitPeople= (String) dform.get("visitPeople");
		String visitPeoplePhone= (String) dform.get("visitPeoplePhone");
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			List list=hs.createQuery("from CustomerVisitFeedback where jnlno='"+jnlno.trim()+"'").list();
			if(list!=null && list.size()>0){
				Query q= hs.createQuery("delete CustomerVisitFeedback where jnlno = '"+jnlno.trim()+"'");
				q.executeUpdate();
				hs.flush();
			}
				
				 CustomerVisitPlanDetail cvpd= (CustomerVisitPlanDetail) hs.get(CustomerVisitPlanDetail.class, jnlno);
				 if(cvpd!=null)
				 {
					 CustomerVisitPlanMaster cvpm=(CustomerVisitPlanMaster) hs.get(CustomerVisitPlanMaster.class, cvpd.getBillno());
					 cvpd.setFkOperId(userInfo.getUserID());
					 cvpd.setFkOperDate(CommonUtil.getNowTime());
					 cvpd.setSubmitType(isSubmitType);
					 if(isSubmitType.trim().equals("Y")||isSubmitType.trim()=="Y"){
						 cvpd.setBhRem("");
						 cvpd.setBhDate(""); 
					 }
					 cvpd.setVisitPeople(visitPeople);
					 cvpd.setVisitPeoplePhone(visitPeoplePhone);
					 hs.update(cvpd);
					 hs.flush();
					 
					 if(cvpm.getMaintContractNo()!=null && !"".equals(cvpm.getMaintContractNo())){
						 hs.createQuery("update MaintContractMaster set warningStatus='S' where maintContractNo='"+cvpm.getMaintContractNo()+"'").executeUpdate();
					 }
				if(projectName!=null&&projectName.length>0)
				{ 
				 for(int i=0;i<projectName.length;i++)
				{
					CustomerVisitFeedback visitFeedback = new CustomerVisitFeedback();
					CustomerVisitPlanDetail customerVisitPlanDetail = new CustomerVisitPlanDetail();
					customerVisitPlanDetail.setJnlno(jnlno);
					visitFeedback.setCustomerVisitPlanDetail(customerVisitPlanDetail);
					visitFeedback.setVisitProject(projectName[i]);
					visitFeedback.setRealVisitDate(realvisitDate[i]);
					visitFeedback.setVisitContent(visitContent[i]);
                    hs.save(visitFeedback);
				}
			    }
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("customervisitfeedback.insert.duplicatekeyerror"));
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
		
		request.setAttribute("navigator.location"," 客户拜访反馈 >> 转派");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		CustomerVisitPlanDetail cvpd=null;
	    CustomerVisitPlanMaster cvpm=null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				   String sql="select cvpm,cvpd from CustomerVisitPlanMaster cvpm,CustomerVisitPlanDetail cvpd where cvpm.billno=cvpd.billno and cvpd.jnlno='"+id+"'";
					   List list=hs.createQuery(sql).list();
					   if(list!=null&&list.size()>0){
						  Object[] objects=(Object[]) list.get(0);
						  cvpm=(CustomerVisitPlanMaster) objects[0];
						  cvpd=(CustomerVisitPlanDetail) objects[1];
						  cvpd.setBillno(cvpm.getBillno());
						  cvpd.setVisitStaff(bd.getName(hs, "Loginuser", "username", "userid", cvpd.getVisitStaff()));
						  cvpd.setFkOperId(bd.getName(hs, "Loginuser", "username", "userid", cvpd.getFkOperId()));
						  request.setAttribute("visitPositionName", bd.getName(hs, "Class1", "Class1Name", "Class1ID", cvpd.getVisitPosition()));
						  request.setAttribute("maintDivisionName", bd.getName(hs, "company", "ComName", "ComID", cvpm.getMaintDivision()));
						  request.setAttribute("maintStationName", bd.getName(hs, "StorageID", "StorageName", "StorageID", cvpm.getMaintStation()));
						  String vp=cvpd.getVisitPosition();
						  request.setAttribute("currvpt", vp);
						  
						  if(vp.equals("27")){
								cvpd.setR1("维保分部长");
								cvpd.setVisitPosition("17");
							}else if(vp.equals("17")){
								cvpd.setR1("维保经理");
								cvpd.setVisitPosition("20");
							}else if(vp.equals("20")){
								cvpd.setR1("维保站长");
								cvpd.setVisitPosition("22");
							}
					  }

					    String maintDivision = cvpm.getMaintDivision();
						String mainStation = cvpm.getMaintStation();
						String visitPosition = cvpd.getVisitPosition();
						String sql1="";
						if(visitPosition!=null&&!visitPosition.trim().equals("")){
							if(visitPosition.trim().equals("17")){//维保分部长
								sql1="from Loginuser l where l.class1id='17' and l.enabledflag='Y' and l.grcid ='"+maintDivision+"'";
							}else if(visitPosition.trim().equals("20")){//维保经理
								sql1="from Loginuser l where l.class1id='20' and l.enabledflag='Y' and l.grcid ='"+maintDivision+"' and l.storageid like '"+mainStation+"%'";
							}else if(visitPosition.trim().equals("22")){//维保站长，服务销售专员 
								sql1="from Loginuser l where ((l.class1id ='22' and l.storageid like '"+mainStation+"%') or l.class1id ='100') and l.enabledflag='Y' and l.grcid ='"+maintDivision+"'";
							}else {
								sql1="from Loginuser l where l.class1id='27' and l.enabledflag='Y'";
							}
						}
						System.out.println(sql1);
						String toToday=DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss");
						List loginuserList =hs.createQuery(sql1).list();
                        request.setAttribute("loginuserList", loginuserList);
                        request.setAttribute("toToday", toToday);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			request.setAttribute("cvpdbean", cvpd);
			request.setAttribute("cvpmbean", cvpm);
			forward = mapping.findForward("customerVisitFeedbackModify");
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
		
		String jnlno = request.getParameter("jnlno");
		String visitPosition = request.getParameter("visitPosition");
		String visitStaff = request.getParameter("visitStaff");
		String transferDate = request.getParameter("transferDate");
		String transfeRem = request.getParameter("transfeRem");
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			CustomerVisitPlanDetail customerVisitPlanDetail = (CustomerVisitPlanDetail) hs.get(CustomerVisitPlanDetail.class, jnlno.trim());
		    customerVisitPlanDetail.setVisitStaff(visitStaff);
		    customerVisitPlanDetail.setVisitPosition(visitPosition);
			hs.update(customerVisitPlanDetail);
			hs.flush();
			CustomerVisitDispatching cvd =new CustomerVisitDispatching();
			cvd.setCustomerVisitPlanDetail(customerVisitPlanDetail);
			cvd.setTransferId(userInfo.getUserID());
			cvd.setTransferDate(transferDate);
			cvd.setTransfeRem(transfeRem);
			cvd.setVisitPosition(visitPosition);
			cvd.setVisitStaff(visitStaff);
			hs.save(cvd);
			tx.commit();
		} catch (Exception e1) {
			tx.rollback();
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
        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
		forward = mapping.findForward("returnList");
			
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
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
			String hql="select a from Storageid a,Company b where a.comid = b.comid and a.comid='"+comid+"' " +
					"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
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
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		
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
						  cvpd.setVisitStaff(bd.getName(hs, "Loginuser", "username", "userid", cvpd.getVisitStaff()));
						  cvpd.setFkOperId(bd.getName(hs, "Loginuser", "username", "userid", cvpd.getFkOperId()));
						  cvpd.setVisitPosition(bd.getName(hs, "Class1", "Class1Name", "Class1ID", cvpd.getVisitPosition()));
						  
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
			forward = mapping.findForward("customerVisitFeedbackDisplay");

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	
	/**
	 * 点击提交方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toSubmitRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String jnlno = null;
		
		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			jnlno = (String) dform.get("jnlno");
		} else {
			jnlno = (String) dform.get("id");
		}
		
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			CustomerVisitPlanDetail customerVisitPlanDetail = (CustomerVisitPlanDetail) hs.get(CustomerVisitPlanDetail.class, jnlno.trim());
			if(customerVisitPlanDetail!=null){
				customerVisitPlanDetail.setSubmitType("Y");
				customerVisitPlanDetail.setBhRem("");
				customerVisitPlanDetail.setBhDate("");
				hs.update(customerVisitPlanDetail);
			}
			tx.commit();
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

		
		return mapping.findForward("returnList");
	}

	
	
}
