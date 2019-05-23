package com.gzunicorn.struts.action.contractpaymentmanage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.commons.beanutils.BeanUtils;
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
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.hibernate.basedata.receivablesname.ReceivablesName;
import com.gzunicorn.hibernate.contractpaymentmanage.contractpaymentmanage.ContractPaymentManage;
import com.gzunicorn.hibernate.contractpaymentmanage.contractpaymentprocess.ContractPaymentProcess;
import com.gzunicorn.hibernate.contractpaymentmanage.contractticketmanage.ContractTicketManage;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster.EntrustContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class PaymentAuditAction extends DispatchAction {

	Log log = LogFactory.getLog(PaymentAuditAction.class);
	
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
				SysRightsUtil.NODE_ID_FORWARD + "paymentaudit", null);
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
		
		request.setAttribute("navigator.location","付款审核 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
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
			HTMLTableCache cache = new HTMLTableCache(session, "paymentAuditList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fPaymentAudit");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("jnlNo");
			table.setIsAscending(true);
			cache.updateTable(table);
			/** 该方法是记住了历史查询条件*/
			if (action.equals(ServeTableForm.NAVIGATE) || action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String jnlNo = tableForm.getProperty("jnlNo");// 收票流水号
			String contractNo = tableForm.getProperty("contractNo");// 合同号
			String companyName = tableForm.getProperty("companyName");// 甲方单位
			String status = tableForm.getProperty("status");//
			String maintDivision=tableForm.getProperty("maintDivision");//所属维保分部

			//第一次进入页面时根据登陆人初始化所属维保分部
			List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
			if(maintDivision == null || maintDivision.equals("")){
				Map map = (Map)maintDivisionList.get(0);
				maintDivision = (String)map.get("grcid");
			}
			
			Session hs = null;
			SQLQuery query = null;
			try {

				hs = HibernateUtil.getSession();

				String sql = "select cpm.*,c.companyName,compay.comfullname,ctm.jnlNo,e.typename "+
				"from ContractPaymentManage cpm,ContractTicketManage ctm,Customer c,Company compay,ViewFlowStatus e"+
				" where compay.comid=cpm.maintDivision and cpm.companyId=c.companyId and ctm.jnlNo=cpm.CT_JnlNo and cpm.submitType='Y'"+
				" and cpm.status = e.typeid";
				
				if (jnlNo != null && !jnlNo.equals("")) {
					sql += " and ctm.jnlNo like '%"+jnlNo.trim()+"%'";
				}
				if (contractNo != null && !contractNo.equals("")) {
					sql += " and cpm.entrustContractNo like '%"+contractNo.trim()+"%'";
				}
				if (companyName != null && !companyName.equals("")) {
					sql += " and (c.companyName like '%"+companyName.trim()+"%' or c.companyId like '%"+companyName.trim()+"%')";
				}
				if (status != null && !status.equals("")) {
					sql += " and cpm.status like '"+status.trim()+"'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and cpm.maintDivision like '"+maintDivision.trim()+"'";
				}
				if (table.getIsAscending()) {
					sql += " order by cpm."+ table.getSortColumn() +" desc";
				} else {
					sql += " order by cpm."+ table.getSortColumn() +" asc";
				}
				
				
				
				query = hs.createSQLQuery(sql);
				query.addEntity("cpm",ContractPaymentManage.class);
				query.addScalar("companyName");
				query.addScalar("comfullname");
				query.addScalar("ct_JnlNo");
				query.addScalar("typename");
				
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List paymentAuditList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map cpm=BeanUtils.describe(objs[4]);
					cpm.put("companyId", objs[0]);
					cpm.put("maintDivision", objs[1]);
					cpm.put("r1", objs[2]);
					cpm.put("statusName",objs[3]);
					/*ContractPaymentManage cpm=(ContractPaymentManage) objs[0];
					cpm.setCompanyId((String) objs[1]);
					cpm.setMaintDivision((String) objs[2]);
					cpm.setR1((String) objs[3]);
					cpm.setR2((String)objs[4]);*/
					paymentAuditList.add(cpm);
				}

				table.addAll(paymentAuditList);
				session.setAttribute("paymentAuditList", table);
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);
				// 流程状态下拉框列表
				request.setAttribute("processStatusList", bd.getProcessStatusList());
				// 获取流程名称
				request.setAttribute("flowname", WorkFlowConfig.getProcessDefine("contractpaymentmanageProcessName"));

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (HibernateException e1) {

				e1.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
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
			forward = mapping.findForward("paymentAuditList");
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
	
	public List getReceivablesNameList(){
		Session hs = null;
		Query query = null;
		List list=null;
		try {
			hs=HibernateUtil.getSession();
			String hql="from ReceivablesName where enabledFlag='Y'";
			list=hs.createQuery(hql).list();
			for(Object object : list){
				ReceivablesName receivables=(ReceivablesName)object;
			}
		} catch (DataStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}	
	/**
	 * 跳转到修改页面
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
		
		request.setAttribute("navigator.location","付款审核 >> 查看与审核");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		
		display(form, request, errors, "");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
			
		request.setAttribute("returnMetho", "toSearchRecord");
		forward = mapping.findForward("paymentAuditModify");
		
		return forward;
	}
	
	/**
	 * 点击修改的方法
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

		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);

		String id = request.getParameter("id");
		String auditStatus = request.getParameter("auditStatus");
		
		String date = request.getParameter("auditDate");
		String rem = request.getParameter("auditRem");
		if (id != null && !id.equals("")) {
			Session hs = null;
			Transaction tx = null;
			try {

				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();

				ContractPaymentManage master = (ContractPaymentManage) hs
						.get(ContractPaymentManage.class, id);
				if(auditStatus=="N"||auditStatus.equals("N")){
					master.setSubmitType("R");
				}
				/*master.setAuditOperid(userInfo.getUserID());
				master.setAuditDate(date);
				master.setAuditRem(rem);			
				master.setAuditStatus(auditStatus);*/
				hs.save(master);

				tx.commit();

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
		forward = mapping.findForward("returnList");			
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
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
		
		request.setAttribute("navigator.location","付款审核 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		
		display(form, request, errors, "display");
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("display", "yes");
		forward = mapping.findForward("paymentAuditModify");
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
		
		return response;
	}

public void display(ActionForm form, HttpServletRequest request ,ActionErrors errors ,String flag){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		if(id==null || "".equals(id)){
			id=(String) request.getAttribute("id");
		}
		Session hs = null;
		
		if (id != null && !id.equals("")) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from ContractPaymentManage where jnlNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {					
					// 主信息
					ContractPaymentManage master = (ContractPaymentManage) list.get(0);															
					ContractTicketManage ctm=(ContractTicketManage) hs.get(ContractTicketManage.class, master.getCtJnlNo());
					EntrustContractMaster ecm=new EntrustContractMaster();
					String sql="from EntrustContractMaster where billno='"+master.getBillNo()+"'";
					List list1=hs.createQuery(sql).list();
					double contractTotal =0;
					if(list1!=null && list1.size()>0){
						ecm=(EntrustContractMaster) list1.get(0);
						contractTotal =ecm.getContractTotal();
					}
					request.setAttribute("contractTotal", contractTotal);
					double invoiceMoney =ctm.getInvoiceMoney();			
					String hql="select isnull(sum(paragraphMoney),0) from ContractPaymentManage where entrustContractNo='"+ecm.getEntrustContractNo()+"'";
					List list2=hs.createSQLQuery(hql).list();
					double invoiceReceivables=0.0;
					if(list2!=null&&list2.size()>0)
					{
						invoiceReceivables=Double.valueOf(list2.get(0).toString());
					}	
					
					
					
					if("display".equals(flag)){
						/*if(master.getAuditOperid()!=null && !master.getAuditOperid().equals("")){
							master.setAuditOperid(bd.getName(hs, "Loginuser", "username", "userid", master.getAuditOperid()));
							request.setAttribute("display", "yes");
							request.setAttribute("isAudit", "Y");
						}*/
						request.setAttribute("isAudit", "Y");
					}else
					{
						/*if(master.getAuditOperid()!=null && !master.getAuditOperid().equals("")){
							master.setAuditOperid(bd.getName(hs, "Loginuser", "username", "userid", master.getAuditOperid()));
							request.setAttribute("display", "yes");
							request.setAttribute("isAudit", "Y");
						}else
						{
							master.setAuditOperid(userInfo.getUserName());
							master.setAuditDate(CommonUtil.getNowTime());
							request.setAttribute("isAudit", "N");
						}*/
					}
					
					master.setMaintDivision(bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision()));// 维保分部	
					master.setCompanyId(bd.getName_Sql("Customer", "companyName", "companyId", master.getCompanyId()));
					master.setOperId(bd.getName(hs, "Loginuser", "username", "userid", master.getOperId()));
					hql="select isnull(sum(paragraphMoney),0) from ContractPaymentManage where entrustContractNo='"+ecm.getEntrustContractNo()+"'";
					double builtReceivables=0.0;
					List preMoneyList=hs.createSQLQuery(hql).list();
					if(preMoneyList!=null&&preMoneyList.size()>0)
					{
					  builtReceivables=Double.valueOf(preMoneyList.get(0).toString());
					}
					
					//审批流程信息
					query = hs.createQuery("from ContractPaymentProcess where jnlNo = '"+ master.getJnlNo() + "' order by itemId");
					List processApproveList = query.list();
					for (Object object : processApproveList) {
						ContractPaymentProcess process = (ContractPaymentProcess) object;
						process.setUserId(bd.getName(hs, "Loginuser", "username", "userid", process.getUserId()));
					}
					request.setAttribute("processApproveList",processApproveList);
					
					request.setAttribute("paymentAuditBean", master);	
					request.setAttribute("ctjnlNo", ctm.getJnlNo());
					request.setAttribute("invoiceNo", ctm.getInvoiceNo());
					request.setAttribute("invoiceType", bd.getName(hs, "InvoiceType", "inTypeName", "inTypeId", ctm.getInvoiceType()));
					request.setAttribute("invoiceReceivables", invoiceReceivables);
					request.setAttribute("noInvoiceReceivables", contractTotal-invoiceReceivables);		
					request.setAttribute("builtReceivables", builtReceivables);
					request.setAttribute("noBuiltReceivables", invoiceMoney-builtReceivables);	
					request.setAttribute("invoiceMoney", invoiceMoney);
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
	
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
			
		}		
		
	}

}	