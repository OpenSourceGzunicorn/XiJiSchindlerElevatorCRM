package com.gzunicorn.struts.action.custregistervisitplan;

import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;





import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;





import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
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

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.custregistervisitplan.custreturnregisterdetail.CustReturnRegisterDetail;
import com.gzunicorn.hibernate.custregistervisitplan.custreturnregisterlssue.CustReturnRegisterLssue;
import com.gzunicorn.hibernate.custregistervisitplan.custreturnregistermaster.CustReturnRegisterMaster;
import com.gzunicorn.hibernate.custregistervisitplan.lostelevatorregisterdetail.LostElevatorRegisterDetail;
import com.gzunicorn.hibernate.custregistervisitplan.lostelevatorregistermaster.LostElevatorRegisterMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;


public class LostElevatorRegisterHandleAction extends DispatchAction {

	Log log = LogFactory.getLog(LostElevatorRegisterHandleAction.class);

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
				SysRightsUtil.NODE_ID_FORWARD + "lostelevatorregisterhandle", null);
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

		request.setAttribute("navigator.location", "丢梯客户回访处理>> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
		String action = dform.toString();

		

			
			String companyName = (String) dform.get("companyName");
			String contacts = (String) dform.get("contacts");
//			String isProblem = (String) dform.get("isProblem");
			String maintDivision = (String) dform.get("maintDivision");
			String contactPhone=(String)dform.get("contactPhone");

			
			Session hs = null;
			Query query = null;
			List custReturnRegisterList = new ArrayList();
			try {
				hs = HibernateUtil.getSession();
				String sql = "select * from "
						+ "(select c1.ReOrder,c1.ReMark,c1.CompanyName,c1.Contacts,c1.ContactPhone,c2.OperDate,c2.handleID,c2.handleResult,c2.ReturnResult,c1.CompanyID,c2.billno,c2.MaintDivision,c2.MinisterHandle,isnull(c2.rem,'') rem1 "
						+ "from(select c.companyName,rm.contacts,rm.contactPhone,rm.companyId,rm.reOrder,rm.reMark from LostElevatorMaintain rm ,Customer c where rm.companyId=c.companyId) as c1 "
						+ "left join (select * from (select row_number() over(partition by crrm2.companyId order by crrm2.operDate desc) as rownum,crrm2.* from LostElevatorRegisterMaster crrm2) as T where T.rownum = 1) as c2 "
						+ "on c1.CompanyID=c2.CompanyID) as c3 where isnull(c3.handleId,'')='' and c3.MinisterHandle like 'N' ";				
				if (companyName != null && !companyName.equals("")) {
					sql += " and c3.companyName like '%" + companyName.trim()
							+ "%'";
				}
				if (contacts != null && !contacts.equals("")) {
					sql += " and c3.contacts like '%" + contacts.trim() + "%'";
				}			
				if (contactPhone != null && !contactPhone.equals("")) {
					sql += " and c3.contactPhone like '%" + contactPhone + "%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and c3.maintDivision like '" + maintDivision+ "'";
				}
				String order = " order by c3.ReOrder";
				List list = hs.createSQLQuery(sql + order).list();

				if (list != null && list.size() > 0) {
					int len = list.size();
					for (int i = 0; i < len; i++) {
						Object[] object = (Object[]) list.get(i);
						HashMap map = new HashMap();
						map.put("reOrder", (Integer) object[0]);
						map.put("reMark", (Character) object[1]);// 是否回访
						map.put("companyName", (String) object[2]);// 回访单位
						map.put("contacts", (String) object[3]);// 回访联系人
						map.put("contactPhone", (String) object[4]);// 回访联系电话
						map.put("operDate", (String) object[5]);// 上次回访日期
						map.put("companyId", (String) object[9]);
						/*Clob clob = (Clob)(object[13]);
						String str=clob.getSubString(1, (int)clob.length());*/
						map.put("rem", object[13]);
						String date = (String) object[5];
						if (date != null) {
							/*char cr = (Character) object[6];
							if (cr == 'Y')// 是否有问题
							{
								map.put("isProblem", "是");
							} else {
								map.put("isProblem", "否");
							}*/
							String handleId = (String) object[6];
							if (handleId != null && !handleId.equals("")) {
								map.put("handleId", "是");// 是否处理
							} else {
								map.put("handleId", "否");// 是否处理
							}
							map.put("handleResult", object[7]);// 处理结果

							String returnResult = (String) object[8];
							if (returnResult != null
									&& !returnResult.equals("")) {
								if(returnResult.equals("Y"))
								{
									map.put("returnResult", "是");// 回访结果
								}else
								{
									map.put("returnResult", "否");// 回访结果
								}
							} else {
								map.put("returnResult", null);
							}
							map.put("billno", (String) object[10]);
							String  maintDivision2 = bd.getName("Company", "comname", "comid",
									 (String) object[11]);
							map.put("maintDivision", maintDivision2);
							char cr = (Character) object[12];
							if (cr == 'Y') {
								map.put("ministerHandle", "是");
							} else {
								map.put("ministerHandle", "否");
							}

						} else {
							map.put("isProblem", null);
							map.put("handleId", null);
							map.put("handleResult", null);
							map.put("returnResult", null);
							map.put("billno", null);
							map.put("maintDivision", null);
							map.put("ministerHandle", null);
						}
						if((i+1)%2==0)
						{
							map.put("style", "oddRow3");
						}
						else
						{
							map.put("style", "evenRow3");
						}
						
						
						
						custReturnRegisterList.add(map);
					}
				}
				
				 sql="select ComId,ComFullName from Company where enabledflag='Y' order by ComId";
					List maintList= hs.createSQLQuery(sql).list();
					List maintDivisionList =new ArrayList();
					if(maintList!=null&&maintList.size()>0)
					{
						int leg=maintList.size();
						for(int i=0;i<leg;i++)
						{
							Object[] object = (Object[]) maintList.get(i);
						    HashMap map = new HashMap();
						    map.put("comId", object[0]);
						    map.put("comFullName", (String) object[1]);
							
						    maintDivisionList.add(map);
						}					
					}
				
				
				request.setAttribute("custReturnRegisterListSize", custReturnRegisterList.size());
				request.setAttribute("maintDivisionList",maintDivisionList);
				request.setAttribute("custReturnRegisterList",
						custReturnRegisterList);

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (HibernateException e1) {

				e1.printStackTrace();
			}/* catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/ finally {
				try {
					hs.close();
					// HibernateSessionFactory.closeSession();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			forward = mapping.findForward("lostElevatorRegisterHandleList");
		
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
		request.setAttribute("navigator.location",
				messages.getMessage(locale, navigation));
	}

	
	/**
	 * 跳转到回访处理页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","丢梯客户回访处理 >> 处理");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		ActionForward forward = null;
		String id = null;

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("billno");
		} else
        {
			id = (String) dform.get("id");
		}
		
		Session hs = null;
		LostElevatorRegisterMaster master = null;
		List<LostElevatorRegisterDetail> detailList = null;
		List detailsList=new ArrayList();
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs
						.createQuery("from LostElevatorRegisterMaster crrm where crrm.billno = :billno");
				query.setString("billno", id);
				List list = query.list();
				
				if (list != null && list.size() > 0) {
					master=(LostElevatorRegisterMaster) list.get(0);
					
					String hql2 = "from Customer c where c.companyId='"
							+ master.getCompanyId().trim()
							+ "'";
					List list1 = hs.createQuery(hql2).list();

					if (list1 != null && list1.size() > 0) {
						Customer customer = (Customer) list1.get(0);
						master.setR1(customer
								.getCompanyName());
					}
					
					//master.setR1(bd.getName("Customer", "companyId", "companyName", master.getCompanyId()));
					if(master.getHandleId()!=null && !"".equals(master.getHandleId()))
						master.setR2(bd.getName("Loginuser", "username", "userid", master.getHandleId()));
					else{
						master.setR2(userInfo.getUserName());
						master.setHandleId(userInfo.getUserID());
					}
					if(master.getOperId()!=null && !"".equals(master.getOperId()))
						master.setR3(bd.getName("Loginuser", "username", "userid", master.getOperId()));
					master.setMaintDivision(bd.getName(hs, "Company", "ComFullName", "ComId", master.getMaintDivision()));
					master.setHandleDate(CommonUtil.getToday());
					
					detailList=hs.createQuery("from LostElevatorRegisterDetail where billno='"+master.getBillno()+"'").list();
					String[] type={"a.id.typeflag='LostElevatorReport_CauseAnalysis'"};
					if(detailList!=null && detailList.size()>0){
						for(LostElevatorRegisterDetail details : detailList){
							HashMap map=new HashMap();
							map.put("maintContractNo", details.getMaintContractNo());
							map.put("lostElevatorDate", details.getLostElevatorDate());
							map.put("causeAnalysis", bd.getName("Pulldown", "pullname", "id.pullid", details.getCauseAnalysis(), type));
							map.put("dtJnlno", details.getDtJnlno());
							List lssueList=hs.createQuery("from LostElevatorRegisterLssue where jnlno='"+details.getJnlno()+"'").list();
							map.put("custReturnRegisterLssues",lssueList);
							detailsList.add(map);
						}
					}
				}

				/*if (list != null && list.size() > 0) {
					master = (LostElevatorRegisterMaster) list
							.get(0);
					String operId = bd.getName(hs, "LoginUser", "userName",
							"userId", master.getOperId());
					master.setOperId(operId);
					master.setMaintDivision(bd.getName(hs, "Company", "ComFullName", "ComId", master.getMaintDivision()));
					master.setHandleId(userInfo.getUserName());
					master.setHandleDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
					
					String hql2 = "from Customer c where c.companyId='"
							+ master.getCompanyId().trim()
							+ "'";
					List list1 = hs.createQuery(hql2).list();

					if (list1 != null && list1.size() > 0) {
						Customer customer = (Customer) list1.get(0);
						master.setR1(customer
								.getCompanyName());
					}
					custReturnRegisterDetailList = this.toGetCustReturnRegisterDetail(
							master.getBillno().trim(), hs);

				} */

				if (master == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"display.recordnotfounterror"));
				}
			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			} catch (ParseException e) {
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
			
			request.setAttribute("custReturnRegisterMasterBean",
					master);
			request.setAttribute("custReturnRegisterDetailList",
					detailsList);
			request.setAttribute("ishandle", "Y");
			forward = mapping.findForward("lostElevatorRegisterHandleModify");
}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	
	/**
	 * 处理
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
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String billno = (String) dform.get("billno");
		dform.set("billno", billno);
		String handleResult = (String) dform.get("handleResult");
		String handleId = userInfo.getUserID();
		String handleDate = (String) dform.get("handleDate");
			
		LostElevatorRegisterMaster master=null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if(billno!= null&& !billno.trim().equals(""))
				master = (LostElevatorRegisterMaster) hs.get(LostElevatorRegisterMaster.class, billno);
			if (handleResult!= null&& !handleResult.trim().equals("")) {
				
				master.setHandleDate(handleDate);
				master.setHandleId(handleId);
				master.setHandleResult(handleResult);
				
				hs.update(master);
			}
			
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("custreturnregisterhand.update.duplicatekeyerror"));
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
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("custreturnregisterhand.update.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("custreturnregisterhand.update.success"));
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
	 * toGetCustReturnRegisterDetail, toList
	 * 
	 * @param billno
	 * @param Session
	 * @return toList
	 * @throws IOException
	 */
	/*public List toGetCustReturnRegisterDetail(String billno, Session hs) {
		List list = new ArrayList();
		String sql = "select distinct mcd.projectName,mcm.maintContractNo,mcd.salesContractNo "
				+ "from MaintContractMaster mcm,MaintContractDetail mcd,CustReturnRegisterDetail crrd "
				+ "where mcm.billNo=mcd.billNo and crrd.wb_Billno=mcm.billNo and crrd.billno = '"
				+ billno.trim()+"'";
		String sql1 = "select distinct crrd.jnlno "
				+ "from CustReturnRegisterDetail crrd "
				+ "where crrd.billno = '"
				+ billno.trim()+"'";
		List list1 = hs.createSQLQuery(sql).list();
		List list2 = hs.createSQLQuery(sql1).list();
		String sql="from CustReturnRegisterDetail where billno='"+billno.trim()+"'";
		List list1=hs.createQuery(sql).list();
		
		if (list1 != null && list1.size() > 0) {
			int leg = list1.size();
			for (int i = 0; i < leg; i++) {
				CustReturnRegisterDetail detail = (CustReturnRegisterDetail) list1.get(i);

				HashMap hmap = new HashMap();
				hmap.put("contractSdate", detail.getContractSdate());
				hmap.put("maintContractNo", detail.getMaintContractNo());
				hmap.put("contractEdate", detail.getContractEdate());
				hmap.put("jnlno", detail.getJnlno());

				String sql2 = "from CustReturnRegisterLssue where jnlno= '"
						+ hmap.get("jnlno") + "'";
				String[] condition={"a.id.typeflag='CustReturnRegisterMaster'"};
				List custReturnRegisterLssueList = hs.createQuery(sql2).list();
				if(custReturnRegisterLssueList!=null && custReturnRegisterLssueList.size()>0){
					for(Object object : custReturnRegisterLssueList){
						CustReturnRegisterLssue lssue=(CustReturnRegisterLssue) object;
						lssue.setLssueSort(bd.getName("Pulldown", "pullname", "id.pullid", lssue.getLssueSort(), condition));
					}
				}
				hmap.put("custReturnRegisterLssues",
						custReturnRegisterLssueList);

				list.add(hmap);
			}
		}
		return list;
	}*/

}
