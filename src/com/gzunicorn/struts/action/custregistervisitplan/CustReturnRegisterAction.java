package com.gzunicorn.struts.action.custregistervisitplan;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Clob;
import java.sql.SQLException;
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
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.custregistervisitplan.custreturnregisterdetail.CustReturnRegisterDetail;
import com.gzunicorn.hibernate.custregistervisitplan.custreturnregisterlssue.CustReturnRegisterLssue;
import com.gzunicorn.hibernate.custregistervisitplan.custreturnregistermaster.CustReturnRegisterMaster;
import com.gzunicorn.hibernate.custregistervisitplan.returningmaintain.ReturningMaintain;
import com.gzunicorn.hibernate.custregistervisitplan.returningmaintaindetail.ReturningMaintainDetail;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster.EntrustContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;


public class CustReturnRegisterAction extends DispatchAction {

	Log log = LogFactory.getLog(CustReturnRegisterAction.class);

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
				SysRightsUtil.NODE_ID_FORWARD + "custreturnregister", null);
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

		request.setAttribute("navigator.location", "客户回访管理>> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {

				response = toExcelRecord(form, request, response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			
			HTMLTableCache cache = new HTMLTableCache(session, "custReturnRegisterList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fCustReturnRegister");
			//table.setLength(3000);
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
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

			
			String contacts = tableForm.getProperty("contacts");
			String isProblem = tableForm.getProperty("isProblem");
			String isHandle = tableForm.getProperty("handleId");
			String maintDivision = tableForm.getProperty("maintDivision");
			String contactPhone=tableForm.getProperty("contactPhone");
			String selmaintContractNo=tableForm.getProperty("selmaintContractNo");//委托合同号
			String reMark=tableForm.getProperty("reMark");//回访标记
			
			if(!userInfo.getComID().equals("00")){
				if(maintDivision == null || maintDivision.trim().equals("")){
					maintDivision = userInfo.getComID();
				}
			}
			
			Session hs = null;
			Query query = null;
			List custReturnRegisterList = new ArrayList();
			try {
				hs = HibernateUtil.getSession();
				String sql = "select * from "
						+ "(select c1.billno1,c1.ReOrder,c1.ReMark,c1.Contacts,c1.ContactPhone,c2.OperDate,c2.IsProblem,c2.handleID," 
						+ "c2.handleResult,c2.ReturnResult,c1.CompanyID,c2.billno,c2.MaintDivision,c2.MinisterHandle,isnull(c2.Rem,'') rem1 "
						+ "from(" +
						"select rm.contacts,rm.contactPhone,rm.companyId,rm.reOrder,rm.reMark,rm.billno billno1 " +
						"from ReturningMaintain rm where rm.enabledFlag='Y' ";
				if (selmaintContractNo != null && !selmaintContractNo.equals("")) {
					sql += " and rm.billno in(select distinct billno from ReturningMaintainDetail" +
						   " where maintContractNo like '%" + selmaintContractNo.trim() + "%') ";
				}
					sql+=") as c1 "
						+ "left join (select * from (select row_number() over(partition by crrm2.ContactPhone order by crrm2.operDate desc) as rownum,"
						+ "crrm2.* from CustReturnRegisterMaster crrm2) as T where T.rownum = 1) as c2 "
						+ "on c1.ContactPhone=c2.ContactPhone) as c3 where 1=1";				

				if (contacts != null && !contacts.equals("")) {
					sql += " and isnull(c3.contacts,'') like '%" + contacts.trim() + "%'";
				}
				if (contactPhone != null && !contactPhone.equals("")) {
					sql += " and isnull(c3.contactPhone,'') like '%" + contactPhone.trim() + "%'";
				}
				if (isHandle != null && !isHandle.equals("")) {
					if (isHandle.equals("Y")) {
						sql += " and c3.handleId not like '' and c3.handleId is not  null";
					}
					if (isHandle.equals("N")) {
						sql += " and c3.handleId is null";
					}
				}
				if (isProblem != null && !isProblem.equals("")) {
					sql += " and isnull(c3.isProblem,'') like '%" + isProblem + "%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and isnull(c3.maintDivision,'') like '" + maintDivision + "'";
				}
				if (reMark != null && !reMark.equals("")) {
					sql += " and isnull(c3.reMark,'N') = '" + reMark + "'";
				}

				String order = " order by c3.ReOrder";
				System.out.println(sql + order);
//				hs.connection().prepareStatement(sql).executeQuery()
				
				query = hs.createSQLQuery(sql + order);
				table.setVolume(query.list().size());// 查询得出数据记录数;
				
				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				
				cache.check(table);
				
				List list = query.list();
				if (list != null && list.size() > 0) {
					int len = list.size();
					for (int i = 0; i < len; i++) {
						Object[] object = (Object[]) list.get(i);
						HashMap map = new HashMap();
						map.put("billno1", object[0]);
						map.put("reOrder", (Integer) object[1]);
						map.put("reMark", (Character) object[2]);// 是否回访
					
						map.put("contacts", (String) object[3]);// 回访联系人
						map.put("contactPhone", (String) object[4]);// 回访联系电话
						map.put("operDate", (String) object[5]);// 上次回访日期
						map.put("companyId", (String) object[10]);
						Clob clob = (Clob)(object[14]);
						String str=clob.getSubString(1, (int)clob.length());
						map.put("rem", str);
						////System.out.println(object[15]);
						String date = (String) object[5];
						if (date != null) {
							char cr = (Character) object[6];
							if (cr == 'Y')// 是否有问题
							{
								map.put("isProblemName", "是");
								map.put("isProblem", "Y");
							} else {
								map.put("isProblemName", "否");
								map.put("isProblem", "N");
							}
							String handleId = (String) object[7];
							if (handleId != null && !handleId.equals("")) {
								map.put("handleId", "是");// 是否处理
							} else {
								map.put("handleId", "否");// 是否处理
							}
							map.put("handleResult", object[8]);// 处理结果

							String returnResult = (String) object[9];
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
								map.put("returnResult", "");
							}
							map.put("billno", (String) object[11]);
							String  maintDivision2 = bd.getName("Company", "comname", "comid",
									 (String) object[12]);
							map.put("maintDivision", maintDivision2);
							cr = (Character) object[13];
							if (cr == 'Y') {
								map.put("ministerHandle", "是");
							} else {
								map.put("ministerHandle", "否");
							}

						} else {
							map.put("isProblem", "");
							map.put("handleId", "");
							map.put("handleResult", "");
							map.put("returnResult", "");
							map.put("billno", "");
							map.put("maintDivision", "");
							map.put("ministerHandle", "");
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
				//cache.check(table);
				table.addAll(custReturnRegisterList);
				session.setAttribute("custReturnRegisterList", table);

                sql="select ComId,ComFullName from Company where enabledflag='Y' order by ComId";
				List maintList= hs.createSQLQuery(sql).list();
				
                
                request.setAttribute("custReturnRegisterListSize", custReturnRegisterList.size());
				request.setAttribute("maintDivisionList",Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
				//request.setAttribute("custReturnRegisterList",custReturnRegisterList);

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
			forward = mapping.findForward("custReturnRegisterList");
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
		request.setAttribute("navigator.location",
				messages.getMessage(locale, navigation));
	}

	/**
	 * 点击查看历史方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toHistoryDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		request.setAttribute("navigator.location", "客户回访管理 >> 历史查看");

		ActionForward forward = null;

		String id = (String) dform.get("id");
		String operDate = (String) dform.get("operDate");
		String initdate=request.getParameter("initdate");
		
		if(initdate!=null && initdate.trim().equals("Y")){
			operDate=DateUtil.getNowTime("yyyy-MM");
			dform.set("operDate",operDate);
		}
		
		Session hs = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();

				String sql="from CustReturnRegisterMaster crrm where crrm.r3 = '"+id+"' ";
				if (operDate != null && !operDate.equals("")) {
					sql += " and crrm.operDate like '"+operDate.trim()+"%'";
				}
				sql+=" order by crrm.operDate desc";
				
				List list = hs.createQuery(sql).list();
				List<CustReturnRegisterMaster> crrmList = new ArrayList<CustReturnRegisterMaster>();

				if (list != null && list.size() > 0) {
					int len = list.size();
					for (int i = 0; i < len; i++) {
						CustReturnRegisterMaster crrm = (CustReturnRegisterMaster) list.get(i);
						String hql2 = "from Customer c where c.companyId = '"+ crrm.getCompanyId().trim() + "'";
						List list2 = hs.createQuery(hql2).list();
						String ishandle = crrm.getHandleId();
						String maintDivision=bd.getName(hs, "Company", "comfullname", "comid",crrm.getMaintDivision());
						crrm.setMaintDivision(maintDivision);
						if (ishandle != null && !"".equals(ishandle)) {
							crrm.setHandleId("Y");// 是否处理
						} else {
							crrm.setHandleId("N");
						}
						String returnResult = crrm.getReturnResult();
						if(returnResult==null || returnResult.equals(""))
						{
							crrm.setReturnResult("N");
						}
						if (list2 != null && list2.size() > 0) {
							Customer customer = (Customer) list2.get(0);
							crrm.setR1(customer.getCompanyName());
						}
						if((i+1)%2==0)
						{
							crrm.setR2("oddRow3");
							
						}
						else
						{
							crrm.setR2("evenRow3");
						}
						
						crrmList.add(crrm);

					}
				}
				 request.setAttribute("custReturnRegisterHistoryListSize", list.size());
				 request.setAttribute("custReturnRegisterHistoryList", crrmList);

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
			forward = mapping.findForward("custReturnRegisterHistoryDisplay");
		}
		return forward;
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
		request.setAttribute("navigator.location", "客户回访管理 >> 查看");

		ActionForward forward = null;
		String id = (String) dform.get("id");
		String isreturn = (String) dform.get("isreturn");
		Session hs = null;
		Transaction tx = null;
		CustReturnRegisterMaster custReturnRegisterMaster = null;
		List listdetail = null;
		List custReturnRegisterDetailList = null;
		List custReturnRegisterLssues = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();//lijun add 20160430
				
				Query query = hs.createQuery("from CustReturnRegisterMaster crrm where crrm.billno = :billno");
				query.setString("billno", id);
				List list = query.list();

				if (list != null && list.size() > 0) {
					custReturnRegisterMaster = (CustReturnRegisterMaster) list.get(0);
					String operId = bd.getName(hs, "LoginUser", "userName","userId", custReturnRegisterMaster.getOperId());
					
					custReturnRegisterMaster.setOperId(operId);
					custReturnRegisterMaster.setHandleId(bd.getName(hs, "LoginUser", "userName","userId", custReturnRegisterMaster.getHandleId()));
					custReturnRegisterMaster.setMaintDivision(bd.getName(hs, "Company", "ComFullName", "ComId", custReturnRegisterMaster.getMaintDivision()));
					String i=custReturnRegisterMaster.getReturnResult();
					if(i!=null&&!i.equals("")){
					   if(i.equals("Y")){
						custReturnRegisterMaster.setReturnResult("解决");
					   }
					   else 
					   {
						custReturnRegisterMaster.setReturnResult("未解决");
					   }
					}
					String hql2 = "from Customer c where c.companyId = '"+ custReturnRegisterMaster.getCompanyId().trim()+ "'";
					List list1 = hs.createQuery(hql2).list();
					
					

					if (list1 != null && list1.size() > 0) {
						Customer customer = (Customer) list1.get(0);
						custReturnRegisterMaster.setR1(customer.getCompanyName());
					}
					if (isreturn != null) {
						custReturnRegisterMaster.setR2(isreturn);
					}
					
					listdetail = this.toGetCustReturnRegisterDetail(
							custReturnRegisterMaster.getBillno().trim(), hs);
					if(listdetail!=null&&listdetail.size()>0){
						custReturnRegisterDetailList=(List) listdetail.get(0);
						custReturnRegisterLssues=(List) listdetail.get(1);
					}

				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}

				if (custReturnRegisterMaster == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}

				request.setAttribute("custReturnRegisterMasterBean",custReturnRegisterMaster);
				request.setAttribute("custReturnRegisterDetailList",
						custReturnRegisterDetailList);
				request.setAttribute("custReturnRegisterLssues",
						custReturnRegisterLssues);
				
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
			
			//lijun add 首页转派跳转查看后，不需要再次出现在首页
			String workisdisplay=request.getParameter("workisdisplay");
			if(workisdisplay!=null && workisdisplay.equals("Y")){
				request.setAttribute("workisdisplay", workisdisplay);
				Session hs2 = null;
				Transaction tx2 = null;
				try {
					hs2 = HibernateUtil.getSession();
					tx2 = hs2.beginTransaction();
					
					String upsql="update CustReturnRegisterMaster set workisdisplay='"+workisdisplay+"' where billno='"+id+"'";
					hs2.connection().prepareStatement(upsql).executeUpdate();
					
					tx2.commit();
				} catch (Exception e1) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
					try {
						tx2.rollback();
					} catch (HibernateException e2) {
						log.error(e2.getMessage());
						DebugUtil.print(e2, "Hibernate Transaction rollback error!");
					}
					e1.printStackTrace();
					log.error(e1.getMessage());
					DebugUtil.print(e1, "Hibernate region Insert error!");
				} finally {
					try {
						hs2.close();
					} catch (HibernateException hex) {
						log.error(hex.getMessage());
						DebugUtil.print(hex, "Hibernate close error!");
					}
				}
			}
			
			request.setAttribute("display", "yes");
			// CustReturnRegisterLssues
			forward = mapping.findForward("custReturnRegisterDisplay");

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * 跳转到开始回访页面
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

		DynaActionForm dform = (DynaActionForm) form;

		ActionErrors errors = new ActionErrors();
		request.setAttribute("navigator.location", "客户回访管理 >> 开始回访");

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);

		ActionForward forward = null;

		String id = (String) dform.get("id");
		
		List custReturnRegisterList = new ArrayList();

		Session hs = null;
		List custReturnRegisterLssueLssueSort = null;
		ReturningMaintain maintain=null;
		EntrustContractMaster contract=null;
		MaintContractMaster mcontract=null;
		String successflag="N";

		if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					 hs = HibernateUtil.getSession();
					 maintain=(ReturningMaintain) hs.get(ReturningMaintain.class, id);

					if(maintain!=null){
						String sql="";
						Query query=null;
	
						if(maintain!=null){
							HashMap map = new HashMap();
							map.put("username", userInfo.getUserName());
							map.put("operdate",DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
							map.put("contacts", maintain.getContacts());
							map.put("contactPhone", maintain.getContactPhone());
							map.put("mainrem", maintain.getRem());
							map.put("companyId",maintain.getCompanyId());
							
							//lijun update 20160430 维保合同是在保，续保的。
							sql="select a from ReturningMaintainDetail a, MaintContractMaster b "
									+ " where a.wbBillno=b.billNo and b.contractStatus in('ZB','XB') and a.billno='"+maintain.getBillno()+"' and a.r4='维保'  order by a.wbBillno desc";
							//System.out.println(sql);
							List list1=hs.createQuery(sql).list();
							List detailList = new ArrayList();
							if(list1!=null&&list1.size()>0)
							{	
								successflag="Y";
								ReturningMaintainDetail detail1=(ReturningMaintainDetail) list1.get(0);
								mcontract=(MaintContractMaster) hs.get(MaintContractMaster.class, detail1.getWbBillno());
								map.put("comId",  mcontract.getMaintDivision());
								map.put("comname", bd.getName(hs, "Company", "ComFullName", "ComId", mcontract.getMaintDivision()));
								
								int leg=list1.size();
								for(int j=0;j<leg;j++){
									ReturningMaintainDetail detail=(ReturningMaintainDetail) list1.get(j);
									HashMap map1=new HashMap();
									map1.put("maintContractNo", detail.getMaintContractNo());
									map1.put("contractSdate", detail.getContractSdate());
									map1.put("contractEdate", detail.getContractEdate());
									map1.put("billNo", detail.getWbBillno());
									map1.put("r4", detail.getR4());
									detailList.add(map1);
								}	
								
							}
							
								//lijun update 20160430 维保合同是在保，续保的。
								sql="select a from ReturningMaintainDetail a, EntrustContractMaster b "
										+ " where a.wbBillno=b.billNo and b.r1 in('ZB','XB') and a.billno='"+maintain.getBillno()+"' and a.r4='委托'  order by a.wbBillno desc";
								//System.out.println(sql);
								List list2=hs.createQuery(sql).list();
								List detailList2 = new ArrayList();
								if(list2!=null&&list2.size()>0)
								{
									successflag="Y";
									ReturningMaintainDetail detail2=(ReturningMaintainDetail) list2.get(0);
									contract=(EntrustContractMaster) hs.get(EntrustContractMaster.class, detail2.getWbBillno());
									map.put("comId",  contract.getMaintDivision());
									map.put("comname", bd.getName(hs, "Company", "ComFullName", "ComId", contract.getMaintDivision()));
									
									int leg=list2.size();
									for(int j=0;j<leg;j++){
										ReturningMaintainDetail detail=(ReturningMaintainDetail) list2.get(j);
										HashMap map2=new HashMap();
										map2.put("maintContractNo", detail.getMaintContractNo());
										map2.put("contractSdate", detail.getContractSdate());
										map2.put("contractEdate", detail.getContractEdate());
										map2.put("billNo", detail.getWbBillno());
										map2.put("r4", detail.getR4());
									    detailList.add(map2);
									}	
									
								}
							
							map.put("detailList", detailList);
							String sql2 = "from Pulldown where  id.typeflag='CustReturnRegisterMaster'";
							custReturnRegisterLssueLssueSort = hs.createQuery(sql2).list();
							custReturnRegisterList.add(map);
						}
	
						if (maintain == null) {
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
						}else if(successflag.equals("N")){
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror.contract"));
						}
					}else
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
					}
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
			}
			
			request.setAttribute("rmbillno",id);
			request.setAttribute("custReturnRegisterList2",custReturnRegisterList);
			request.setAttribute("custReturnRegisterLssueLssueSort",custReturnRegisterLssueLssueSort);
			request.setAttribute("successflag", successflag);
			forward = mapping.findForward("custReturnRegisterAdd");

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

	public ActionForward toAddRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;

		String contacts = request.getParameter("contacts1");
		String contactPhone = request.getParameter("contactPhone1");
		String comId =request.getParameter("comId");
		String startResult =request.getParameter("startResult");
		String ministerHandle = (String) dform.get("ministerHandle");
		String rem = (String) dform.get("rem");
		
		String[] billNo = request.getParameterValues("billNo");
		String[] maintContractNo=request.getParameterValues("maintContractNo");
		String[] contractSdate=request.getParameterValues("contractSdate");
		String[] contractEdate=request.getParameterValues("contractEdate");
		String[] lssueSort = request.getParameterValues("id.pullid");
		String[] lssueDetail = request.getParameterValues("lssueDetail");
		String[] r4 = request.getParameterValues("r4");
		String rmbillno = request.getParameter("rmbillno");
		
		String isProblem = "";
		int billNoIntger = billNo.length;
		for (int i = 0; i < lssueDetail.length; i++) {
			isProblem += lssueDetail[i];
		}
		if (isProblem.trim().equals("")) {
			isProblem = "N";
		} else {
			isProblem = "Y";
		}
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			//tx.begin(); //lijun update 20160430
			
			String year1 = CommonUtil.getToday().substring(2, 4);
			String[] billno1 = CommonUtil.getBillno(year1,"CustReturnRegisterMaster", 1);
			String[] billno2 = CommonUtil.getBillno(year1,"CustReturnRegisterDetail", billNoIntger);
			CustReturnRegisterMaster crrm = new CustReturnRegisterMaster();
			String crrmBnillno = billno1[0];
			crrm.setBillno(crrmBnillno);
			crrm.setCompanyId(contactPhone);
			crrm.setContacts(contacts);
			crrm.setContactPhone(contactPhone);
			crrm.setMinisterHandle(ministerHandle);
			crrm.setIsProblem(isProblem);
			crrm.setMaintDivision(comId);
			crrm.setRem(rem);
			crrm.setOperId(userInfo.getUserID());
			crrm.setOperDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
			crrm.setR3(rmbillno);
			crrm.setStartResult(startResult);
			hs.save(crrm);
			for (int i = 0; i < billNoIntger; i++) {
				CustReturnRegisterDetail crrd = new CustReturnRegisterDetail();
				crrd.setJnlno(billno2[i]);
				crrd.setCustReturnRegisterMaster(crrm);
				crrd.setWbBillno(billNo[i]);
				crrd.setMaintContractNo(maintContractNo[i]);
				crrd.setContractSdate(contractSdate[i]);
				crrd.setContractEdate(contractEdate[i]);
				crrd.setR4(r4[i]);
				//System.out.println(maintContractNo[i]+contractSdate[i]+contractEdate[i]);
				hs.save(crrd);
				for (int j = 0; j < 3; j++) {
					CustReturnRegisterLssue crrl = new CustReturnRegisterLssue();
					crrl.setCustReturnRegisterDetail(crrd);
					crrl.setLssueSort(lssueSort[j]);
					crrl.setLssueDetail(lssueDetail[j]);
					hs.save(crrl);
				}
			}
			 
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(					
					"custreturnregistercheck.insert.duplicatekeyerror"));
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
						"custreturnregister.insert.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"custreturnregister.insert.success"));
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
	 * 跳转到回访反馈页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","客户回访管理 >> 回访反馈");
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
		Transaction tx = null;
		CustReturnRegisterMaster custReturnRegisterMaster = null;
		List listdetail = null;
		List custReturnRegisterDetailList = null;
		List custReturnRegisterLssues = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();//lijun add 20160430
				
				Query query = hs
						.createQuery("from CustReturnRegisterMaster crrm where crrm.billno = :billno");
				query.setString("billno", id);
				List list = query.list();

				if (list != null && list.size() > 0) {
					custReturnRegisterMaster = (CustReturnRegisterMaster) list
							.get(0);
					String operId = bd.getName(hs, "LoginUser", "userName",
							"userId", custReturnRegisterMaster.getOperId());
					custReturnRegisterMaster.setOperId(operId);
					custReturnRegisterMaster.setMaintDivision(bd.getName(hs, "Company", "ComFullName", "ComId", custReturnRegisterMaster.getMaintDivision()));
					custReturnRegisterMaster.setHandleId(bd.getName(hs, "LoginUser", "userName", "userId", custReturnRegisterMaster.getHandleId()));
					String rr=custReturnRegisterMaster.getReturnResult();
					if(rr==null || rr.trim().equals(""))
					{
						custReturnRegisterMaster.setReturnResult("Y");
					}
					
					String hql2 = "from Customer c where c.companyId like '"
							+ custReturnRegisterMaster.getCompanyId().trim()
							+ "'";
					List list1 = hs.createQuery(hql2).list();

					if (list1 != null && list1.size() > 0) {
						Customer customer = (Customer) list1.get(0);
						custReturnRegisterMaster.setR1(customer
								.getCompanyName());
					}
					listdetail = this.toGetCustReturnRegisterDetail(
							custReturnRegisterMaster.getBillno().trim(), hs);
					if(listdetail!=null&&listdetail.size()>0){
						custReturnRegisterDetailList=(List) listdetail.get(0);
						custReturnRegisterLssues=(List) listdetail.get(1);
					}
				} 

				if (custReturnRegisterMaster == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"display.recordnotfounterror"));
				}
				

				request.setAttribute("custReturnRegisterMasterBean",
						custReturnRegisterMaster);
				request.setAttribute("custReturnRegisterDetailList",
						custReturnRegisterDetailList);
				request.setAttribute("custReturnRegisterLssues",
						custReturnRegisterLssues);
				request.setAttribute("contract", "Y");
				
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
			
			forward = mapping.findForward("custReturnRegisterModify");
}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	
	/**
	 * 回访反馈
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
		
		String billno = (String) dform.get("billno");
		dform.set("billno", billno);
		String returnResult = (String) dform.get("returnResult");
		String returnRem =request.getParameter("returnRem");
		CustReturnRegisterMaster custReturnRegisterMaster=null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if(billno!= null&& !billno.trim().equals(""))
				custReturnRegisterMaster = (CustReturnRegisterMaster) hs.get(CustReturnRegisterMaster.class, billno);
			if (returnResult!= null&& !returnResult.trim().equals("")) {
				custReturnRegisterMaster.setReturnResult(returnResult);
				custReturnRegisterMaster.setReturnRem(returnRem);
				hs.update(custReturnRegisterMaster);//lijun add 20160430
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("custreturnregistercheck.update.duplicatekeyerror"));
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
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("custreturnregister.update.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("custreturnregister.update.success"));
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
		XSSFWorkbook wb = new XSSFWorkbook();
		List roleList = new ArrayList();

		String companyName = tableForm.getProperty("companyName");
		String contacts = tableForm.getProperty("contacts");
		String isProblem = tableForm.getProperty("isProblem");
		String isHandle = tableForm.getProperty("handleId");
		String maintDivision = tableForm.getProperty("maintDivision");
		String contactPhone=tableForm.getProperty("contactPhone");
		maintDivision = bd
				.getName("Company", "comname", "comid", maintDivision);

		Session hs = null;
		Query query = null;
		List custReturnRegisterList = new ArrayList();
		try {

			hs = HibernateUtil.getSession();
			String sql = "select * from"
					+ "(select c1.ReOrder,c1.ReMark,c1.Contacts,c1.contactPhone,c2.OperDate,c2.IsProblem,c2.handleID,c2.handleResult,c2.ReturnResult,c1.CompanyID,c2.billno,c2.MaintDivision,c2.MinisterHandle,isnull(c2.Rem,'') rem1 "
					+ "from(select rm.contacts,rm.contactPhone,rm.companyId,rm.reOrder,rm.reMark from ReturningMaintain rm where rm.enabledFlag='Y' ) as c1 "
					+"left join (select * from (select row_number() over(partition by crrm2.ContactPhone order by crrm2.operDate desc) as rownum, "
							+ "crrm2.* from CustReturnRegisterMaster crrm2) as T where T.rownum = 1) as c2 "
							+ "on c1.contactPhone=c2.ContactPhone) as c3 where 1=1";
//			if (companyName != null && !companyName.equals("")) {
//				sql += " and c2.companyName like '%" + companyName.trim()
//						+ "%'";
//			}
			if (contacts != null && !contacts.equals("")) {
				sql += " and c3.contacts like '%" + contacts.trim() + "%'";
			}
			if(contactPhone!=null && !contactPhone.equals("")){
				sql+=" and c3.contactPhone like '%"+contactPhone.trim()+"%'";
			}
			if (isHandle != null && !isHandle.equals("")) {
				if (isHandle.equals("Y")) {
					sql += " and c3.handleId not like '' and c3.handleId is not  null";

				}
				if (isHandle.equals("N")) {
					sql += " and c3.handleId is null";

				}
			}
			if (isProblem != null && !isProblem.equals("")) {
				sql += " and c3.isProblem like '%" + isProblem + "%'";
			}
			if (maintDivision != null && !maintDivision.equals("")) {
				sql += " and c3.maintDivision like '" + maintDivision + "'";
			}

			String order = " order by c3.ReOrder";
			System.out.println(sql+order);
			List list = hs.createSQLQuery(sql + order).list();

			if (list != null && list.size() > 0) {
				int len = list.size();
				for (int i = 0; i < len; i++) {

					Object[] object = (Object[]) list.get(i);
					HashMap map = new HashMap();
					map.put("reOrder", (Integer) object[0]);
					map.put("reMark", (Character) object[1]);// 是否回访
					//map.put("companyName", (String) object[2]);// 回访单位
					map.put("contacts", (String) object[2]);// 回访联系人
					map.put("contactPhone", (String) object[3]);// 回访联系电话
					map.put("operDate", (String) object[4]);// 上次回访日期
					map.put("companyId", (String) object[9]);
					Clob clob = (Clob)(object[13]);
					String str=clob.getSubString(1, (int)clob.length());
					map.put("rem", str);
					String date = (String) object[4];
					if (date != null) {
						char cr = (Character) object[5];
						if (cr == 'Y')// 是否有问题
						{
							map.put("isProblem", "是");
						} else {
							map.put("isProblem", "否");
						}
						String handleId = (String) object[6];
						if (handleId != null && !handleId.equals("")) {
							map.put("handleId", "是");// 是否处理
						} else {
							map.put("handleId", "否");// 是否处理
						}
						Object hr = object[7];
						if (hr != null && !hr.equals("")) {
							map.put("handleResult", "是");// 是否解决
						} else {
							map.put("handleResult", "否");
						}
						String returnResult = (String) object[8];
						if (returnResult != null && !returnResult.equals("")) {
							map.put("returnResult", "是");// 回访结果
						} else {
							map.put("returnResult", "否");
						}
						map.put("billno", (String) object[10]);
						map.put("maintDivision", (String) object[11]);
						cr = (Character) object[12];
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

					custReturnRegisterList.add(map);
				}
			}

			roleList = custReturnRegisterList;

			XSSFSheet sheet = wb.createSheet("客户回访管理");

			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short) 0);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.reOrder"));

				cell0 = row0.createCell((short) 1);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.reMark"));

//				cell0 = row0.createCell((short) 2);
//				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
//				cell0.setCellValue(messages.getMessage(locale,
//						"custreturnregister.companyName"));

				cell0 = row0.createCell((short) 2);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.contacts"));

				cell0 = row0.createCell((short) 3);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.contactPhone"));

				cell0 = row0.createCell((short) 4);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.maxoperDate"));

				cell0 = row0.createCell((short) 5);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.isProblem"));

				cell0 = row0.createCell((short) 6);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.isHandleResult"));

				cell0 = row0.createCell((short) 7);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.isHandle"));

				cell0 = row0.createCell((short) 8);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.ministerHandle"));

				cell0 = row0.createCell((short) 9);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.maintDivision"));
				
				cell0 = row0.createCell((short) 10);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.rem"));

				for (int i = 0; i < l; i++) {
					HashMap map = (HashMap) roleList.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i + 1);

					// 创建Excel列
					XSSFCell cell = row.createCell((short) 0);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((Integer) map.get("reOrder"));

					cell = row.createCell((short) 1);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					char cr = (Character) map.get("reMark");
					String str = cr + "";
					cell.setCellValue(CommonUtil.tranEnabledFlag(str.trim()));

//					cell = row.createCell((short) 2);
//					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					cell.setCellValue((String) map.get("companyName"));

					cell = row.createCell((short) 2);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("contacts"));

					cell = row.createCell((short) 3);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("contactPhone"));

					cell = row.createCell((short) 4);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("operDate"));

					cell = row.createCell((short) 5);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("isProblem"));

					cell = row.createCell((short) 6);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("returnResult"));

					cell = row.createCell((short) 7);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("handleId"));

					cell = row.createCell((short) 8);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("ministerHandle"));

					cell = row.createCell((short) 9);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName(hs, "Company", "comfullname", "comid", (String) map.get("maintDivision")));
					
					cell = row.createCell((short) 10);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("rem"));

				}
			}

		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (HibernateException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
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

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="
				+ URLEncoder.encode("客户回访管理", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		return response;
	}

	/**
	 * toGetCustReturnRegisterDetail, toList
	 * 
	 * @param billno
	 * @param Session
	 * @return toList
	 * @throws IOException
	 */
	public List toGetCustReturnRegisterDetail(String billno, Session hs) {
		List list = new ArrayList();
		List listtotal = new ArrayList();
		/*String sql = "select distinct mcd.projectName,mcm.maintContractNo,mcd.salesContractNo "
				+ "from MaintContractMaster mcm,MaintContractDetail mcd,CustReturnRegisterDetail crrd "
				+ "where mcm.billNo=mcd.billNo and crrd.wb_Billno=mcm.billNo and crrd.billno = '"
				+ billno.trim()+"'";
		String sql1 = "select distinct crrd.jnlno "
				+ "from CustReturnRegisterDetail crrd "
				+ "where crrd.billno = '"
				+ billno.trim()+"'";
		List list1 = hs.createSQLQuery(sql).list();
		List list2 = hs.createSQLQuery(sql1).list();*/
		String sql="from CustReturnRegisterDetail where billno='"+billno.trim()+"'";
		List list1=hs.createQuery(sql).list();
//		if (list1 != null && list1.size() > 0 && list2 != null && list2.size() > 0 && list2.size()==list1.size()) {
		if(list1!=null && list1.size()>0){
			int leg = list1.size();
			for (int i = 0; i < leg; i++) {
				CustReturnRegisterDetail detail = (CustReturnRegisterDetail) list1.get(i);

				HashMap hmap = new HashMap();
				hmap.put("contractSdate", detail.getContractSdate());
				hmap.put("maintContractNo", detail.getMaintContractNo());
				hmap.put("contractEdate", detail.getContractEdate());
				hmap.put("jnlno", detail.getJnlno());
				hmap.put("billno", detail.getWbBillno());
				hmap.put("r4", detail.getR4());
				list.add(hmap);
			}
			CustReturnRegisterDetail detail = (CustReturnRegisterDetail) list1.get(0);
			String sql2 = "from CustReturnRegisterLssue where jnlno= '"
					+ detail.getJnlno() + "'";
			List custReturnRegisterLssueList = hs.createQuery(sql2).list();
			String[] condition={"a.id.typeflag='CustReturnRegisterMaster'"};
			if(custReturnRegisterLssueList!=null && custReturnRegisterLssueList.size()>0){
				for(Object object : custReturnRegisterLssueList){
					CustReturnRegisterLssue lssue=(CustReturnRegisterLssue) object;
					lssue.setLssueSort(bd.getName("Pulldown", "pullname", "id.pullid", lssue.getLssueSort(), condition));
				}
			}
			
			listtotal.add(0, list);
			listtotal.add(1, custReturnRegisterLssueList);
		}
		return listtotal;
	}

}
