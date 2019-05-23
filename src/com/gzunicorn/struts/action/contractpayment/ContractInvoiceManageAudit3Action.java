package com.gzunicorn.struts.action.contractpayment;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.basedata.invoicetype.InvoiceType;
import com.gzunicorn.hibernate.contractpayment.contractinvoicemanage.ContractInvoiceManage;
import com.gzunicorn.hibernate.contractpayment.procontractarfeemaster.ProContractArfeeMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.hibernate.viewmanager.compact_view.CompactView;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * 开票财务审核
 * @author Lijun
 *
 */
public class ContractInvoiceManageAudit3Action extends DispatchAction {

	Log log = LogFactory.getLog(ContractInvoiceManageAudit3Action.class);
	
	BaseDataImpl bd = new BaseDataImpl();
	DecimalFormat df = new DecimalFormat("##.##"); 
	
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


		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD +"contractinvoiceaudit3", null);
		/** **********结束用户权限过滤*********** */
		if (name == null || name.equals("")) {
			name = "toSearchRecordAudit";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			ActionForward forward = super.execute(mapping, form, request,response);
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
	public ActionForward toSearchRecordAudit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("navigator.location","开票财务审核>> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			//try {
			//	response = toExcelRecord(form,request,response);
			//} catch (IOException e) {
			//	e.printStackTrace();
			//}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport","");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "contractInvoiceAuditList3");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fContractInvoiceManageAudit3");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("jnlNo");
			table.setIsAscending(false);
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
			
			String ARF_JnlNo = tableForm.getProperty("ARF_JnlNo");// 应收款流水号
			String invoiceType = tableForm.getProperty("invoiceType");// 类型
			String invoiceNo = tableForm.getProperty("invoiceNo");// 号
			String istbp = tableForm.getProperty("istbp");// 是否退补票
			String submitType = tableForm.getProperty("submitType");// 提交标志
			String auditStatus = tableForm.getProperty("auditStatus");// 审核状态
			if(auditStatus==null || auditStatus.trim().equals("")){
				auditStatus="N";
				tableForm.setProperty("auditStatus",auditStatus);
			}
			String maintDivision=tableForm.getProperty("maintDivision");
			String contractNo=tableForm.getProperty("contractNo");
			String contractType=tableForm.getProperty("contractType");

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

				String sql = "select jnlNo,ARF_JnlNo,invoiceNo,invoiceDate,invoiceType,invoiceMoney," +
						"rem,istbp,submitType,isnull(auditStatus3,'N'),maintDivision,contractNo,contractType " +
						"from ContractInvoiceManage where submitType='Y' " +
						"and isnull(auditStatus,'N')='Y'"+
						//"and isnull(auditStatus2,'N')='Y'";//总部长审核
						"and isnull(auditStatus4,'N')='Y'";//财务总监审核
				
				if (ARF_JnlNo != null && !ARF_JnlNo.equals("")) {
					sql += " and ARF_JnlNo like '%"+ARF_JnlNo.trim()+"%'";
				}
				if (invoiceType != null && !invoiceType.equals("")) {
					sql += " and invoiceType like '%"+invoiceType.trim()+"%'";
				}
				if (invoiceNo != null && !invoiceNo.equals("")) {
					sql += " and invoiceNo like '%"+invoiceNo.trim()+"%'";
				}
				if (istbp != null && !istbp.equals("")) {
					sql += " and istbp like '%"+istbp.trim()+"%'";
				}
				if (submitType != null && !submitType.equals("")) {
					sql += " and submitType like '%"+submitType.trim()+"%'";
				}
				if (auditStatus != null && !auditStatus.equals("")) {
					sql += " and isnull(auditStatus3,'N') like '"+auditStatus.trim()+"'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and maintDivision like '"+maintDivision.trim()+"'";
				}
				if (contractNo != null && !contractNo.equals("")) {
					sql += " and contractNo like '%"+contractNo.trim()+"%'";
				}
				if (contractType != null && !contractType.equals("")) {
					sql += " and contractType like '%"+contractType.trim()+"%'";
				}
				if (table.getIsAscending()) {
					sql += " order by "+ table.getSortColumn() +" asc";
				} else {
					sql += " order by "+ table.getSortColumn() +" desc";
				}
				
				//System.out.println(">>>>>>"+sql);
				
				query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List contractInvoiceManageAuditList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map master=new HashMap();
					master.put("jnlNo", objs[0]);
					master.put("ARF_JnlNo", objs[1]);
					master.put("invoiceNo", objs[2]);
					master.put("invoiceDate", objs[3]);
					master.put("invoiceType", bd.getName("InvoiceType", "inTypeName", "inTypeId", String.valueOf(objs[4])));
					master.put("invoiceMoney", objs[5]);
					master.put("rem", objs[6]);
					master.put("istbp", objs[7]);
					master.put("submitType", objs[8]);
					master.put("auditStatus", objs[9]);
					master.put("maintDivision", bd.getName("Company", "comname", "comid",objs[10].toString()));
					master.put("contractNo", objs[11]);
					master.put("contractType", objs[12]);
					contractInvoiceManageAuditList.add(master);
				}

				table.addAll(contractInvoiceManageAuditList);
				session.setAttribute("contractInvoiceAuditList3", table);
				//发票类型下拉框列表
				request.setAttribute("invoiceTypeList", this.getInvoiceTypeList());
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);
				
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
			forward = mapping.findForward("contractInvoiceManageAuditList3");
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
	
	public ActionForward toAuditDisplay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","开票财务审核 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		
		display(form, request, errors, "display");
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		request.setAttribute("display", "yes");
		forward = mapping.findForward("contractInvoiceAuditDisplay3");
		return forward;
	}
	
	public List getInvoiceTypeList(){
		Session hs = null;
		Query query = null;
		List list=null;
		try {
			hs=HibernateUtil.getSession();
			String hql="from InvoiceType where enabledFlag='Y'";
			list=hs.createQuery(hql).list();
			for(Object object : list){
				InvoiceType invoicetype=(InvoiceType)object;
			}
		} catch (DataStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 跳转到审核页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareAuditRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","开票财务审核 >> 审核");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		display(form, request, errors, "display");
		request.setAttribute("auditOpername", userInfo.getUserName());
		request.setAttribute("auditDate", CommonUtil.getNowTime());

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
			
		request.setAttribute("returnMetho", "toSearchRecordAudit");
		forward = mapping.findForward("contractInvoiceManageAudit3");
		
		return forward;
	}
	
	/**
	 * 点击审核的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toAuditRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

		String jnlNo = (String) dform.get("jnlNo");
		String auditRem=(String)dform.get("auditRem3");
		String auditStatus=(String)dform.get("auditStatus3");
		String submitType=(String)dform.get("submitType");

		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			ContractInvoiceManage master=(ContractInvoiceManage) hs.get(ContractInvoiceManage.class, jnlNo.trim());
			if(master!=null){
				master.setSubmitType(submitType);
				master.setAuditStatus3(auditStatus);
				if(submitType!=null && submitType.trim().equals("R")){
					master.setAuditOperid(null);
					master.setAuditDate(null);
					master.setAuditRem(null);
					master.setAuditStatus("N");
					master.setAuditOperid2(null);
					master.setAuditDate2(null);
					master.setAuditRem2(null);
					master.setAuditStatus2("N");
					master.setAuditOperid4(null);
					master.setAuditDate4(null);
					master.setAuditRem4(null);
					master.setAuditStatus4("N");
				}else{
					master.setAuditOperid3(userInfo.getUserID());
					master.setAuditDate3(CommonUtil.getNowTime());
					master.setAuditRem3(auditRem);
				}
				hs.save(master);
			}
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
		String isreturn = request.getParameter("isreturn");		
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				forward = mapping.findForward("returnAuditList3");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				forward = mapping.findForward("returnAuditList3");
				/*request.setAttribute("returnMethod", "toSearchRecordAudit");
				request.setAttribute("id", jnlNo);
				forward = mapping.findForward("returnAuditDisplay");*/			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
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
				Query query = hs.createQuery("from ContractInvoiceManage where jnlNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {					
					// 主信息
					ContractInvoiceManage master = (ContractInvoiceManage) list.get(0);
					dform.set("id",id);
					CompactView compact=null;
					String hql="from CompactView where id.billno='"+master.getBillNo()+"'";
					List list1=hs.createQuery(hql).list();
					if(list1!=null && list1.size()>0){
						compact=(CompactView) list1.get(0);
					}
					String sql="select sum(invoiceMoney) from ContractInvoiceManage where contractNo='"+master.getContractNo()+"' group by contractNo";
					List list2=hs.createSQLQuery(sql).list();
					double invoiceTotal=0;
					if(list!=null && list2.size()>0){
						invoiceTotal=Double.valueOf(list2.get(0).toString());
					}
					double bilMoney=0;
					sql="select sum(invoiceMoney) from ContractInvoiceManage where ARF_JnlNo='"+master.getArfJnlNo()+"' group by ARF_JnlNo";
					List list3=hs.createSQLQuery(sql).list();
					if(list3!=null && list3.size()>0){
						bilMoney=Double.valueOf(list3.get(0).toString());
					}
					ProContractArfeeMaster pro=(ProContractArfeeMaster) hs.get(ProContractArfeeMaster.class, master.getArfJnlNo());
					double nobilMoney=pro.getPreMoney()-bilMoney+master.getInvoiceMoney();
					if("display".equals(flag)){
						nobilMoney=pro.getPreMoney()-bilMoney;
					}

					if(master.getR9()==null){
						master.setR9(compact.getId().getNum());
					}
					
					Map map=new HashMap();
					map.put("num", compact.getId().getNum());
					map.put("contractTotal", compact.getId().getContractTotal());
					map.put("invoiceTotal", invoiceTotal);
					map.put("noInvoiceTotal", df.format(compact.getId().getContractTotal()-invoiceTotal));
					map.put("ARF_JnlNo", master.getArfJnlNo());
					map.put("recName", bd.getName_Sql("ReceivablesName", "recName", "recId", pro.getRecName()));
					map.put("preMoney", pro.getPreMoney());
					map.put("preDate", pro.getPreDate());
					map.put("bilMoney", bilMoney);
					map.put("nobilMoney", df.format(nobilMoney));
					request.setAttribute("contractBean", map);
					
					//审核人
					if(master.getAuditOperid()!=null && !master.getAuditOperid().equals("")){
						master.setAuditOperid(bd.getName(hs, "Loginuser", "username", "userid", master.getAuditOperid()));
					}
					if(master.getAuditOperid2()!=null && !master.getAuditOperid2().equals("")){
						master.setAuditOperid2(bd.getName(hs, "Loginuser", "username", "userid", master.getAuditOperid2()));
					}
					if(master.getAuditOperid3()!=null && !master.getAuditOperid3().equals("")){
						master.setAuditOperid3(bd.getName(hs, "Loginuser", "username", "userid", master.getAuditOperid3()));
					}
					if(master.getAuditOperid4()!=null && !master.getAuditOperid4().equals("")){
						master.setAuditOperid4(bd.getName(hs, "Loginuser", "username", "userid", master.getAuditOperid4()));
					}
														
					if("display".equals(flag)){
						String companyid=master.getCompanyId();
						master.setMaintDivision(bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision()));// 维保分部
						master.setMaintStation(bd.getName(hs,"Storageid", "storagename", "storageid", master.getMaintStation()));
						master.setCompanyId(bd.getName_Sql("Customer", "companyName", "companyId", master.getCompanyId()));
						master.setOperId(bd.getName(hs, "Loginuser", "username", "userid", master.getOperId()));
						master.setInvoiceType(bd.getName("InvoiceType", "inTypeName", "inTypeId", master.getInvoiceType()));
					
						String cusql=" from Customer where companyid='"+companyid+"'";
						List cuslist=hs.createQuery(cusql).list();
						Customer cust=(Customer)cuslist.get(0);
						request.setAttribute("CustomerBean", cust);
					
					} else {						
						request.setAttribute("companyName", bd.getName(hs,"Customer", "companyName", "companyId", master.getCompanyId()));
						request.setAttribute("maintDivisionName",bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision())); // 维保分部名称
						request.setAttribute("maintStationName", bd.getName(hs,"Storageid", "storagename", "storageid", master.getMaintStation()));
						request.setAttribute("operName", bd.getName(hs, "Loginuser", "username", "userid", master.getOperId()));
						request.setAttribute("invoiceTypeList", this.getInvoiceTypeList());
					}
					request.setAttribute("contractInvoiceManageBean", master);	
					
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