package com.gzunicorn.struts.action.contractpaymentmanage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.commons.lang.StringUtils;
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
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.contractpaymentmanage.contractpayablesmaster.ContractPayablesMaster;
import com.gzunicorn.hibernate.contractpaymentmanage.contractpaymentmanage.ContractPaymentManage;
import com.gzunicorn.hibernate.contractpaymentmanage.contractticketmanage.ContractTicketManage;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class ContractPaymentEnquiryAction extends DispatchAction {

	Log log = LogFactory.getLog(ContractPaymentEnquiryAction.class);
	
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
	 *  合同付款查询 
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
				SysRightsUtil.NODE_ID_FORWARD + "contractpaymentenquiry", null);
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
		
		request.setAttribute("navigator.location","合同付款查询 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

			HTMLTableCache cache = new HTMLTableCache(session, "contractPaymentEnquiryList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fContractPaymentEnquiry");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("entrustContractNo");
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
			
			String contractNo = tableForm.getProperty("contractNo");// 委托合同号
			String companyName = tableForm.getProperty("companyName");// 委托单位
			String contractTotal = tableForm.getProperty("contractTotal");// 合同金额
			String maintDivision=tableForm.getProperty("maintDivision");//所属维保分部
			String salesContractNo=tableForm.getProperty("salesContractNo");//销售合同号
			String salesContractNoStr="";
			//第一次进入页面时根据登陆人初始化所属维保分部
			List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
			if(maintDivision == null || maintDivision.equals("")){
				Map map = (Map)maintDivisionList.get(0);
				maintDivision = (String)map.get("grcid");
			}
			
			Session hs = null;
			Query query = null;
			try {
				hs = HibernateUtil.getSession();
				String[] colNames = {						
						"ecm.contractNo as entrustContractNo",
						"ecm.CompanyName as companyName",
						"ecm.ContractTotal as contractTotal",
						"(select isnull(sum(PreMoney),0) from ContractPayablesMaster where EntrustContractNo=ecm.contractNo and (auditStatus='N' or auditStatus='Y')) as accountsPayable",
						"(select isnull(sum(invoiceMoney),0) from ContractTicketManage where entrustContractNo=ecm.contractNo and (auditStatus='N' or auditStatus='Y')) as invoice",
						"(select isnull(sum(paragraphMoney),0) from ContractPaymentManage where entrustContractNo=ecm.contractNo and ProcessName='流程通过') as payment",
						"(select isnull(sum(invoiceMoney),0) from ContractTicketManage where entrustContractNo=ecm.contractNo and (auditStatus='N' or auditStatus='Y'))-"
						+ "(select isnull(sum(paragraphMoney),0) from ContractPaymentManage where entrustContractNo=ecm.contractNo and ProcessName='流程通过') as noPayment",
						"ecm.ComFullName as maintDivision",
						"(ecm.ContractTotal - (select isnull(sum(paragraphMoney),0) from ContractPaymentManage where entrustContractNo=ecm.contractNo and ProcessName='流程通过')) as contractnoPayment",
                        "(select isnull(sum(debitMoney),0) from ContractPaymentManage where entrustContractNo=ecm.contractNo and ProcessName='流程通过') as debitMoney"
				};
				
				if (salesContractNo != null && !salesContractNo.equals("")) {
					String hql = "select distinct ecd.billNo from EntrustContractDetail ecd where ecd.salesContractNo like '%"+salesContractNo.trim()+"%'"
							   + " UNION ALL "
							   + "select distinct ocd.billno from OutsourceContractDetail ocd where ocd.salesContractNo like '%"+salesContractNo.trim()+"%'";
					List salesContractNoList=hs.createSQLQuery(hql).list();
				   if(salesContractNoList!=null&&salesContractNoList.size()>0){
					   for(int i=0;i<salesContractNoList.size();i++){
						   salesContractNoStr+=i==salesContractNoList.size()-1?"'"+salesContractNoList.get(i)+"'":"'"+salesContractNoList.get(i)+"',";
					   }
				   }
				}
				String sql = "select "+StringUtils.join(colNames, ",")+
						 " from Payables_view ecm where 1=1 ";
				if(contractNo!=null && !"".equals(contractNo)){
					sql+=" and ecm.contractNo like '%"+contractNo.trim()+"%' ";
				}
				if(companyName!=null && !"".equals(companyName)){
					sql+=" and customer.CompanyName like '%"+companyName.trim()+"%'";
				}
				if(contractTotal!=null && !"".equals(contractTotal)){
					sql+=" and ecm.ContractTotal like '%"+contractTotal.trim()+"%' ";
				}
				if(maintDivision!=null && !"".equals(maintDivision)){
					sql+=" and ecm.MaintDivision like '"+maintDivision.trim()+"' ";
				}
				
				if(salesContractNo != null && !salesContractNo.equals("")){
					if (salesContractNoStr != null && !salesContractNoStr.equals("")) {
						sql += " and ecm.billNo in ("+salesContractNoStr+")";
					}else{
						sql += " and ecm.billNo = ''";
					}
				}

				if (table.getIsAscending()) {
					sql+=" order by "+table.getSortColumn()+" desc";
				} else {
					sql+=" order by "+table.getSortColumn();
				}
				
				//System.out.println(">>>>"+sql);
				
				query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List contractPaymentEnquiryList = new ArrayList();
				double accountsPayable=0;
				double invoice=0;
				double payment=0;
				double noPayment=0;
				double contractnoPayment=0;
				if(list!=null&&list.size()>0)
				{   
					for(int i=0;i<list.size();i++){
					Object[] object=(Object[]) list.get(i);
					HashMap map= new HashMap();
					for(int j=0;j<colNames.length;j++)
					{
						map.put(colNames[j].split(" as ")[1].trim(), object[j]);
					    if(colNames[j].split(" as ")[1].trim().equals("accountsPayable")){
					    	BigDecimal a=(BigDecimal)object[j];
					    	accountsPayable+=a.doubleValue();
					    }
					    if(colNames[j].split(" as ")[1].trim().equals("invoice")){
					    	BigDecimal a=(BigDecimal)object[j];
					    	invoice+=a.doubleValue();
					    }
					    if(colNames[j].split(" as ")[1].trim().equals("payment")){
					    	BigDecimal a=(BigDecimal)object[j];
					    	payment+=a.doubleValue();
					    }
					    if(colNames[j].split(" as ")[1].trim().equals("noPayment")){
					    	BigDecimal a=(BigDecimal)object[j];
					    	noPayment+=a.doubleValue();
					    }
					    if(colNames[j].split(" as ")[1].trim().equals("contractnoPayment")){
					    	BigDecimal a=(BigDecimal)object[j];
					    	contractnoPayment+=a.doubleValue();
					    }
					}
					contractPaymentEnquiryList.add(map);
					}
				}
				table.addAll(contractPaymentEnquiryList);
				session.setAttribute("contractPaymentEnquiryList", table);
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);
				request.setAttribute("accountsPayable", accountsPayable);
				request.setAttribute("invoice", invoice);
				request.setAttribute("payment", payment);
				request.setAttribute("noPayment", noPayment);
				request.setAttribute("contractnoPayment", contractnoPayment);

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
		return mapping.findForward("contractPaymentEnquiryList");
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
		
		request.setAttribute("navigator.location","合同付款查询 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
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
				//转换中文乱码
				id=new String(id.getBytes("ISO-8859-1"),"gbk");

					hs = HibernateUtil.getSession();
					String[] colNames = {						
							"ecm.contractNo as entrustContractNo",
							"ecm.CompanyName as companyName",
							"ecm.ContractTotal as contractTotal",
							"(select isnull(sum(PreMoney),0) from ContractPayablesMaster where EntrustContractNo=ecm.contractNo and (auditStatus='N' or auditStatus='Y')) as accountsPayable",
							"(select isnull(sum(invoiceMoney),0) from ContractTicketManage where entrustContractNo=ecm.contractNo and (auditStatus='N' or auditStatus='Y')) as invoice",
							"(select ecm.ContractTotal-isnull(sum(invoiceMoney),0) from ContractTicketManage where entrustContractNo=ecm.contractNo and (auditStatus='N' or auditStatus='Y')) as noInvoice",
							"(select isnull(sum(paragraphMoney),0) from ContractPaymentManage where entrustContractNo=ecm.contractNo and ProcessName='流程通过') as payment",
							"(select ecm.ContractTotal-isnull(sum(paragraphMoney),0) from ContractPaymentManage where entrustContractNo=ecm.contractNo and ProcessName='流程通过') as noPayment",
							"ecm.ComFullName as maintDivision"
					};
					
					String sql = "select "+StringUtils.join(colNames, ",")+
							 " from Payables_view ecm "
							+ "where ecm.contractNo='"+id.trim()+"'";
					Query query = hs.createSQLQuery(sql);
					List list = query.list();
					if (list != null && list.size() > 0) {

						Object[] object=(Object[]) list.get(0);
						HashMap map= new HashMap();
						for(int j=0;j<colNames.length;j++)
						{
							map.put(colNames[j].split(" as ")[1].trim(), object[j]);
						}		    
						//应付款
						String hql="select cpm,rn.recName from ContractPayablesMaster cpm,ReceivablesName rn where rn.recId=cpm.recName and cpm.entrustContractNo = '"+id.trim()+"' ";
						list =hs.createQuery(hql).list();
						List contractPayablesMasterList=new ArrayList();
						if (list != null && list.size() > 0) {
							
							for(int i=0;i<list.size();i++){
							Object[] objects=(Object[]) list.get(i);
							ContractPayablesMaster cpm=(ContractPayablesMaster) objects[0];
							cpm.setRecName((String) objects[1]);
							contractPayablesMasterList.add(cpm);
							}
						}
						//收票
						request.setAttribute("contractPayablesMasterList", contractPayablesMasterList);
						hql="select ctm,it.inTypeName from ContractTicketManage ctm,InvoiceType it where it.inTypeId=ctm.invoiceType and ctm.entrustContractNo = '"+id.trim()+"' ";
						list =hs.createQuery(hql).list();
						List contractTicketManageList=new ArrayList();
						if (list != null && list.size() > 0) {
							for(int i=0;i<list.size();i++){
							Object[] objects=(Object[]) list.get(i);
							ContractTicketManage cpm=(ContractTicketManage) objects[0];
							cpm.setInvoiceType((String) objects[1]);
							contractTicketManageList.add(cpm);
							}
						}
						//付款
						request.setAttribute("contractTicketManageList", contractTicketManageList);
						hql="from ContractPaymentManage cpm where cpm.entrustContractNo ='"+id.trim()+"' and cpm.processName='流程通过' ";
						list =hs.createQuery(hql).list();
						/**
						List contractPaymentManageList=new ArrayList();
						if (list != null && list.size() > 0) {
							for(int i=0;i<list.size();i++){
							ContractPaymentManage cpm=(ContractPaymentManage) list.get(i);
							//cpm.setR1(cpm.getJnlNo());
							contractPaymentManageList.add(cpm);
							}
						}
						*/
						request.setAttribute("contractPaymentManageList", list);
						request.setAttribute("contractPaymentEnquirybean", map);
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
		
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("display", "yes");
		forward = mapping.findForward("contractPaymentEnquiryDisplay");
		return forward;
	}
}	