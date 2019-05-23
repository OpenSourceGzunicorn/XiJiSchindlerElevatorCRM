package com.gzunicorn.struts.action.contractpayment;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.hibernate.cache.QueryKey;
import org.hibernate.engine.QueryParameters;
import org.hibernate.jmx.HibernateServiceMBean;
import org.hibernate.sql.QuerySelect;
import org.jbpm.context.exe.variableinstance.HibernateStringInstance;
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

import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.pdfprint.BarCodePrint;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.JSONUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.basedata.invoicetype.InvoiceType;
import com.gzunicorn.hibernate.basedata.receivablesname.ReceivablesName;
import com.gzunicorn.hibernate.contractpayment.contractinvoicemanage.ContractInvoiceManage;
import com.gzunicorn.hibernate.contractpayment.procontractarfeemaster.ProContractArfeeMaster;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractdetail.EntrustContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster.EntrustContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.hibernate.viewmanager.compact_view.CompactView;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * 开票管理 
 * @author Lijun
 *
 */
public class ContractInvoiceManageAction extends DispatchAction {

	Log log = LogFactory.getLog(ContractInvoiceManageAction.class);
	
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
		String authority="contractinvoicemanage";
		if(name != null && name.contains("Audit")){
			authority = "contractinvoiceaudit";
		}
		
		
		if(!"toPrintRecord".equals(name)){
			/** **********开始用户权限过滤*********** */
			SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + authority, null);
			/** **********结束用户权限过滤*********** */
		}
		
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			
			String warnjnlNo=request.getParameter("warnjnlNo");
			if(warnjnlNo!=null && !warnjnlNo.trim().equals("")){
				name="toPrepareWarnRecord";
			}
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
	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("navigator.location","开票管理 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		request.setAttribute("showroleid", userInfo.getRoleID());

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
			HTMLTableCache cache = new HTMLTableCache(session, "contractInvoiceManageList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fContractInvoiceManage");
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
			
			String jnlNo = tableForm.getProperty("jnlNo");//流水号
			String ARF_JnlNo = tableForm.getProperty("ARF_JnlNo");// 应收款流水号
			String invoiceType = tableForm.getProperty("invoiceType");// 类型
			String invoiceNo = tableForm.getProperty("invoiceNo");// 号
			String istbp = tableForm.getProperty("istbp");// 是否退补票
			String submitType = tableForm.getProperty("submitType");// 提交标志
			String auditStatus = tableForm.getProperty("auditStatus");// 审核状态
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
						"rem,istbp,submitType,auditStatus3,maintDivision,contractNo,contractType " +
						"from ContractInvoiceManage";
				String condition="";
				if (jnlNo != null && !jnlNo.equals("")) {
					if(condition.equals("")){
						condition=" where jnlNo like '%"+jnlNo.trim()+"%'";
					}else{
						condition+=" and jnlNo like '%"+jnlNo.trim()+"%'";
					}
				}
				if (ARF_JnlNo != null && !ARF_JnlNo.equals("")) {
					if(condition.equals("")){
						condition=" where ARF_JnlNo like '%"+ARF_JnlNo.trim()+"%'";
					}else{
						condition+=" and ARF_JnlNo like '%"+ARF_JnlNo.trim()+"%'";
					}
				}
				if (invoiceType != null && !invoiceType.equals("")) {
					if(condition.equals("")){
						condition=" where invoiceType like '%"+invoiceType.trim()+"%'";
					}else{
						condition+=" and invoiceType like '%"+invoiceType.trim()+"%'";
					}
				}
				if (invoiceNo != null && !invoiceNo.equals("")) {
					if(condition.equals("")){
						condition=" where invoiceNo like '%"+invoiceNo.trim()+"%'";
					}else{
						condition+=" and invoiceNo like '%"+invoiceNo.trim()+"%'";
					}
				}
				if (istbp != null && !istbp.equals("")) {
					if(condition.equals("")){
						condition=" where istbp like '%"+istbp.trim()+"%'";
					}else{
						condition+=" and istbp like '%"+istbp.trim()+"%'";
					}
				}
				if (submitType != null && !submitType.equals("")) {
					if(condition.equals("")){
						condition=" where submitType like '%"+submitType.trim()+"%'";
					}else{
						condition+=" and submitType like '%"+submitType.trim()+"%'";
					}
				}
				if (auditStatus != null && !auditStatus.equals("")) {
					if(condition.equals("")){
						condition=" where auditStatus like '%"+auditStatus.trim()+"%'";
					}else{
						condition+=" and auditStatus like '%"+auditStatus.trim()+"%'";
					}
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					if(condition.equals("")){
						condition=" where maintDivision like '"+maintDivision.trim()+"'";
					}else{
						condition+=" and maintDivision like '"+maintDivision.trim()+"'";
					}
				}
				if (contractNo != null && !contractNo.equals("")) {
					if(condition.equals("")){
						condition=" where contractNo like '%"+contractNo.trim()+"%'";
					}else{
						condition+=" and contractNo like '%"+contractNo.trim()+"%'";
					}
				}
				if (contractType != null && !contractType.equals("")) {
					if(condition.equals("")){
						condition=" where contractType like '%"+contractType.trim()+"%'";
					}else{
						condition+=" and contractType like '%"+contractType.trim()+"%'";
					}
				}
				if (table.getIsAscending()) {
					condition += " order by "+ table.getSortColumn() +" asc";
				} else {
					condition += " order by "+ table.getSortColumn() +" desc";
				}
				
				query = hs.createSQLQuery(sql+condition);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List contractInvoiceManageList = new ArrayList();
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
					contractInvoiceManageList.add(master);
				}

				table.addAll(contractInvoiceManageList);
				session.setAttribute("contractInvoiceManageList", table);
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
			forward = mapping.findForward("contractInvoiceManageList");
		}
		return forward;
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
	public ActionForward toSearchNext(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("navigator.location","应收款 >> 查询列表 ");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "contractInvoiceManageNextList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fContractInvoiceManageNext");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("contractNo");
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
			
			String jnlNo = tableForm.getProperty("jnlNo");// 流水号
			String contractNo = tableForm.getProperty("contractNo");// 合同号
			String contractType = tableForm.getProperty("contractType");// 合同类型
			String maintDivision = tableForm.getProperty("maintDivision");// 维保分部
			String salesContractNo = tableForm.getProperty("salesContractNo");// 销售合同号
			String invoiceName = tableForm.getProperty("invoiceName");// 销售合同号
			
			/*if(maintDivision!=null && maintDivision.equals("00")){
				maintDivision="";
			}*/
			String sdate1 = tableForm.getProperty("sdate1");//应收款日期
			if(sdate1==null || sdate1.trim().equals("")){
				sdate1="0000-00-00";
			}
			String edate1 = tableForm.getProperty("edate1");//应收款日期
			if(edate1==null || edate1.trim().equals("")){
				edate1="9999-99-99";
			}
						
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

				String sql = "select a.jnlNo,a.contractNo,a.contractType,a.companyId,a.recName,a.preDate,a.maintDivision," +
						"a.preMoney,isnull(a.warnRem,''),b.invoiceName " +
						"from ProContractArfeeMaster a,Customer b "+
						"where a.auditStatus='Y' and a.companyId=b.companyId " +
						"and a.preDate>='"+sdate1+"' and a.preDate<='"+edate1+"' " +
						"and a.jnlNo not in (select c.ARF_JnlNo from ProContractARFeeMaster p," +
						"(select ARF_JnlNo,SUM(invoiceMoney) invoiceMoney from ContractInvoiceManage group by ARF_JnlNo) c " +
						"where p.jnlno=c.ARF_JnlNo and p.PreMoney=c.invoiceMoney)";

				if(salesContractNo != null && !salesContractNo.equals("")){
                   sql += " and a.billNo in (select d.billNo from MaintContractDetail d where d.salesContractNo like '%"+salesContractNo+"%')";
				}
				if (jnlNo != null && !jnlNo.equals("")) {
					sql += " and a.jnlNo like '%"+jnlNo.trim()+"%'";
				}
				if (contractNo != null && !contractNo.equals("")) {
					sql += " and a.contractNo like '%"+contractNo.trim()+"%'";
				}				
				if (contractType != null && !contractType.equals("")) {
					sql += " and a.contractType = '"+contractType.trim()+"'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and a.maintDivision like '"+maintDivision.trim()+"'";
				}				

				if (invoiceName != null && !invoiceName.equals("")) {
					sql += " and b.invoiceName like '%"+invoiceName.trim()+"%'";
				}
				
				if (table.getIsAscending()) {
					sql += " order by a."+ table.getSortColumn() +" desc";
				} else {
					sql += " order by a."+ table.getSortColumn() +" asc";
				}
				
				//System.out.println(sql);
				
				query = hs.createSQLQuery(sql);
				
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List contractInvoiceManageNextList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map map = new HashMap();
					
					String preMoney=objs[7].toString();
					map.put("jnlNo", objs[0].toString());
					map.put("contractNo", objs[1].toString());
					map.put("contractType", objs[2].toString());
					map.put("companyId", objs[3].toString());
					map.put("companyName",objs[9].toString());
					//map.put("companyName", bd.getName_Sql("Customer", "invoiceName", "companyId", objs[3].toString()));
					map.put("recName",bd.getName_Sql("ReceivablesName", "recName", "recId", objs[4].toString()));
					map.put("preDate", objs[5].toString());
					map.put("maintDivision",bd.getName_Sql("Company", "comname", "comid", objs[6].toString()));
					map.put("preMoney", preMoney);
					map.put("warnRem", objs[8].toString());
					
					String notsql="select isnull(SUM(invoiceMoney),0) invoiceMoney from ContractInvoiceManage where ARF_JnlNo='"+objs[0].toString().trim()+"'";
					List notlist=hs.createSQLQuery(notsql).list();
					String invoiceMoney=notlist.get(0).toString();
					map.put("notPreMoney",Double.parseDouble(preMoney)-Double.parseDouble(invoiceMoney));
					
					contractInvoiceManageNextList.add(map);
				}

				table.addAll(contractInvoiceManageNextList);
				session.setAttribute("contractInvoiceManageNextList", table);
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);
				

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
			forward = mapping.findForward("contractInvoiceManageNextList");
		}
		return forward;
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
		
		request.setAttribute("navigator.location","开票确认>> 查询列表");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "contractInvoiceAuditList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fContractInvoiceManageAudit");
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
						"rem,istbp,submitType,auditStatus,maintDivision,contractNo,contractType " +
						"from ContractInvoiceManage where submitType='Y'";
				
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
					sql += " and auditStatus like '%"+auditStatus.trim()+"%'";
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
				session.setAttribute("contractInvoiceAuditList", table);
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
			forward = mapping.findForward("contractInvoiceManageAuditList");
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
		
		request.setAttribute("navigator.location","开票管理 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		
		display(form, request, errors, "display");
		
		//lijun add 首页转派跳转查看后，不需要再次出现在首页
		String workisdisplay=request.getParameter("workisdisplay");
		if(workisdisplay!=null && workisdisplay.equals("Y")){
			request.setAttribute("workisdisplay", workisdisplay);
			Session hs = null;
			Transaction tx = null;
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				
				String id=request.getParameter("id");
				String upsql="update ContractInvoiceManage set workisdisplay='"+workisdisplay+"' where jnlno='"+id+"'";
				hs.connection().prepareStatement(upsql).executeUpdate();
				
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
				
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		request.setAttribute("display", "yes");
		forward = mapping.findForward("contractInvoiceManageDisplay");
		return forward;
	}
	
	public ActionForward toAuditDisplay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","开票确认 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		
		display(form, request, errors, "display");
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		request.setAttribute("display", "yes");
		forward = mapping.findForward("contractInvoiceAuditDisplay");
		return forward;
	}
	
	/**
	 * 跳转到新建方法
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

		request.setAttribute("navigator.location","开票管理 >> 添加");
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}
		String billno=request.getParameter("billno");
		if(billno==null || "".equals(billno)){
			billno=(String) request.getAttribute("arf_JnlNo");
			//billno=(String)dform.get("ARF_JnlNo");
		}
		Session hs = null;
		Query query = null;
		try {
			hs = HibernateUtil.getSession();
			ContractInvoiceManage master=new ContractInvoiceManage();
			master.setOperId(userInfo.getUserID());
			request.setAttribute("operName", userInfo.getUserName());
			master.setOperDate(CommonUtil.getNowTime());
			master.setInvoiceDate(CommonUtil.getToday());
			Map map=new HashMap();
			ProContractArfeeMaster pro=null;
			String hql="from ProContractArfeeMaster where jnlNo='"+billno+"'";
			
			List list2=hs.createQuery(hql).list();
			if(list2!=null && list2.size()>0){
				pro=(ProContractArfeeMaster)list2.get(0);
			}
			Customer customer=(Customer) hs.get(Customer.class, pro.getCompanyId());
			double bilMoney=0;
			String sql="select sum(invoiceMoney) from ContractInvoiceManage where ARF_JnlNo='"+pro.getJnlNo()+"' group by ARF_JnlNo";
			List list3=hs.createSQLQuery(sql).list();
			if(list3!=null && list3.size()>0){
				bilMoney=Double.valueOf(list3.get(0).toString());
			}
			
			master.setContractType(pro.getContractType());
			master.setContractNo(pro.getContractNo());
			master.setCompanyId(pro.getCompanyId());
			master.setMaintDivision(pro.getMaintDivision());
			master.setMaintStation(pro.getMaintStation());
			master.setInvoiceName(customer.getInvoiceName());
			
			CompactView compact=null;
			hql="from CompactView where id.billno='"+pro.getBillNo()+"'";
			List list1=hs.createQuery(hql).list();
			if(list1!=null && list1.size()>0){
				compact=(CompactView) list1.get(0);
			}
			double invoiceTotal=0;
			sql="select sum(invoiceMoney) from ContractInvoiceManage where contractNo='"+compact.getId().getMaintContractNo()+"' group by contractNo";
			List list4=hs.createSQLQuery(sql).list();
			if(list4!=null && list4.size()>0){
				invoiceTotal=Double.valueOf(list4.get(0).toString());
			}
			
			master.setBillNo(compact.getId().getBillno());
			master.setR9(compact.getId().getNum());
			map.put("contractTotal", compact.getId().getContractTotal());
			map.put("invoiceTotal", invoiceTotal);
			map.put("noInvoiceTotal", df.format(compact.getId().getContractTotal()-invoiceTotal));
			map.put("ARF_JnlNo", pro.getJnlNo());
			map.put("recName", bd.getName_Sql("ReceivablesName", "recName", "recId", pro.getRecName()));
			map.put("preMoney", pro.getPreMoney());
			map.put("preDate", pro.getPreDate());
			map.put("bilMoney", bilMoney);
			map.put("nobilMoney", df.format(pro.getPreMoney()-bilMoney));
			map.put("yskrem", pro.getRem());
			master.setMaintScope(pro.getMaintScope());
			request.setAttribute("contractBean", map);
			request.setAttribute("maintDivisionName", compact.getId().getComName());
			request.setAttribute("maintStationName", compact.getId().getStorageName());
			request.setAttribute("companyName", bd.getName_Sql("Customer", "companyName", "companyId", compact.getId().getCompanyId()));
			request.setAttribute("invoiceTypeList", this.getInvoiceTypeList());
			//request.setAttribute("maintScope", pro.getMaintScope());
			session.setAttribute("contractInvoiceManageBean", master);
			
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
		
		request.setAttribute("returnMethod", "toSearchRecord");
		
		saveToken(request); //生成令牌，防止表单重复提交
		
		return mapping.findForward("contractInvoiceManageAdd");
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
	 * 点击新建的方法
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
		ActionForward forward = null;	

		//防止页面重复提交
		if(isTokenValid(request, true)){
			addOrUpdate(form,request,errors);// 新增或修改记录
		}else{
			saveToken(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("navigator.submit.error"));
		}

		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
				forward = mapping.findForward("returnList");
			} else {
				
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
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
		
		request.setAttribute("navigator.location","开票管理 >> 修改");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		
		display(form, request, errors, "");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
			
		request.setAttribute("returnMetho", "toSearchRecord");
		forward = mapping.findForward("contractInvoiceManageModify");
		
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
		
		addOrUpdate(form,request,errors);// 新增或修改记录
		
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
	
	public ActionForward toReferRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, HibernateException {
		
		ActionErrors errors = new ActionErrors();
		refer(form,request,errors); //提交
		if (errors.isEmpty()){
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","提交成功！"));
		}
		if (!errors.isEmpty()){
			this.saveErrors(request, errors);
		}

		return mapping.findForward("returnList");

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
			
			String id = (String) dform.get("id");
			ContractInvoiceManage master = (ContractInvoiceManage) hs.get(ContractInvoiceManage.class, id);
			if (master != null) {
				hs.delete(master);
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
	 * 退票
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toRefundRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			String id = (String) dform.get("id");
			ContractInvoiceManage master = (ContractInvoiceManage) hs.get(ContractInvoiceManage.class, id);
			if (master != null) {
				//将原记录，改为退票状态
				master.setIstbp("TP");
				hs.save(master);
				
				//退票时，需要新生成一条冲销记录，将退票金额抵消。
				ContractInvoiceManage tp=(ContractInvoiceManage) BeanUtils.cloneBean(master);
				tp.setInvoiceMoney(0-master.getInvoiceMoney());
				tp.setSubmitType("Y");
				tp.setAuditStatus("Y");
				tp.setJnlNo(master.getJnlNo()+"_1");
				tp.setIstbp("CX");
				hs.save(tp);
			}
			tx.commit();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("refund.succeed"));
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"refund.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
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
	 * 补票
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toTicketRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			String id = (String) dform.get("id");
			ContractInvoiceManage master = (ContractInvoiceManage) hs.get(ContractInvoiceManage.class, id);
			if (master != null) {
				//将原记录，改为补票状态
				master.setIstbp("BP");
				hs.save(master);
				
				//补票时，需要新生成一条冲销记录，将补票金额抵消。
				ContractInvoiceManage tp=(ContractInvoiceManage) BeanUtils.cloneBean(master);
				tp.setInvoiceMoney(0-master.getInvoiceMoney());
				tp.setIstbp("CX");
				tp.setSubmitType("Y");
				tp.setAuditStatus("Y");
				tp.setJnlNo(master.getJnlNo()+"_1");
				hs.save(tp);
				
				//补票时，在需要新生成一条开票记录。
				ContractInvoiceManage bp=(ContractInvoiceManage) BeanUtils.cloneBean(master);
				bp.setInvoiceNo("");
				bp.setJnlNo(master.getJnlNo()+"_2");
				hs.save(bp);
			}
			tx.commit();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("ticket.succeed"));
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("ticket.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
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
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareBackfillRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","开票管理 >> 发票号回填");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		
		display(form, request, errors, "display");
		request.setAttribute("Backfill", "backfill");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
			
		request.setAttribute("returnMetho", "toSearchRecord");
		forward = mapping.findForward("contractInvoiceManageBackfill");
		
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
	public ActionForward toBackfillRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;
		DynaActionForm dform = (DynaActionForm) form;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ContractInvoiceManage master = null;
		String id = (String) dform.get("id");
		String invoiceNo=(String)dform.get("invoiceNo");
		String jnlNo = null;

		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			if (id != null && !id.equals("")) {	
				master=(ContractInvoiceManage) hs.get(ContractInvoiceManage.class, id);
				master.setInvoiceNo(invoiceNo);
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
		
			
		try {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				forward = mapping.findForward("returnList");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
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
		
		request.setAttribute("navigator.location","开票确认 >> 确认");	
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
		forward = mapping.findForward("contractInvoiceManageAudit");
		
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
		String auditRem=(String)dform.get("auditRem");
		String auditStatus=(String)dform.get("auditStatus");
		String submitType=(String)dform.get("submitType");

		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			ContractInvoiceManage master=(ContractInvoiceManage) hs.get(ContractInvoiceManage.class, jnlNo.trim());
			if(master!=null){
				master.setSubmitType(submitType);
				master.setAuditOperid(userInfo.getUserID());
				master.setAuditDate(CommonUtil.getNowTime());
				master.setAuditRem(auditRem);
				master.setAuditStatus(auditStatus);
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
				forward = mapping.findForward("returnAuditList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				forward = mapping.findForward("returnAuditList");
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
					map.put("invoiceTotal", df.format(invoiceTotal));
					map.put("noInvoiceTotal", df.format(compact.getId().getContractTotal()-invoiceTotal));
					map.put("ARF_JnlNo", master.getArfJnlNo());
					map.put("recName", bd.getName_Sql("ReceivablesName", "recName", "recId", pro.getRecName()));
					map.put("preMoney", pro.getPreMoney());
					map.put("preDate", pro.getPreDate());
					map.put("bilMoney", df.format(bilMoney));
					map.put("nobilMoney", df.format(nobilMoney));
					map.put("yskrem", pro.getRem());
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
		ContractInvoiceManage master = null;
		String id = (String) dform.get("id");
		//System.out.println(id);
		String ARF_JnlNo=(String)dform.get("ARF_JnlNo");
		String submitType=(String)dform.get("submitType");
		String maintScope=request.getParameter("maintScope");
		String jnlNo = null;

		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			if (id != null && !id.equals("")) { // 修改		
				master = (ContractInvoiceManage) hs.get(ContractInvoiceManage.class, id);
				jnlNo = master.getJnlNo();
			} else { // 新增
				master = new ContractInvoiceManage();
				
				String todayDate = CommonUtil.getToday();
				String year = todayDate.substring(2,4);
				jnlNo = CommonUtil.getBillno(year,"ContractInvoiceManage", 1)[0];// 生成流水号
			}
			
			//ProContractArfeeMaster pro=(ProContractArfeeMaster) hs.get(ProContractArfeeMaster.class, ARF_JnlNo);
			
			BeanUtils.populate(master, dform.getMap()); // 复制所有属性
			
			master.setJnlNo(jnlNo);// 流水号	
			master.setArfJnlNo(ARF_JnlNo);
			master.setSubmitType(submitType);
			master.setMaintScope(maintScope);
			master.setAuditStatus("N");
			master.setAuditStatus2("N");//总部长审核
			master.setAuditStatus4("N");//财务总监审核
			master.setAuditStatus3("N");//财务审核
			hs.save(master);
			request.setAttribute("arf_JnlNo", master.getArfJnlNo());
		
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
		
	public void refer(ActionForm form, HttpServletRequest request,ActionErrors errors){
		
		String id = request.getParameter("id"); 
		
		if(id != null && !id.equals("")){
			
			Session hs = null;
			Transaction tx = null;
			ContractInvoiceManage master = null;				
			
			try {
				
				hs = HibernateUtil.getSession();		
				tx = hs.beginTransaction();
				
				master = (ContractInvoiceManage) hs.get(ContractInvoiceManage.class, id);
				master.setSubmitType("Y");
				hs.save(master);
				
				tx.commit();
			} catch (Exception e) {				
				e.printStackTrace();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("maintContract.recordnotfounterror"));
			} finally {
				if(hs != null){
					hs.close();				
				}				
			}
			
		}		
	}
	
	public void inserInformDate(ActionForm form, HttpServletRequest request,ActionErrors errors,String jnlNo){
		 
		
		if(jnlNo != null && !jnlNo.equals("")){
			
			Session hs = null;
			Transaction tx = null;
			ContractInvoiceManage master = null;				
			
			try {
				
				hs = HibernateUtil.getSession();		
				tx = hs.beginTransaction();
				
				master = (ContractInvoiceManage) hs.get(ContractInvoiceManage.class, jnlNo);
				master.setInformDate(CommonUtil.getToday());
				hs.save(master);
				
				tx.commit();
			} catch (Exception e) {				
				e.printStackTrace();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("maintContract.recordnotfounterror"));
			} finally {
				if(hs != null){
					hs.close();				
				}				
			}
			
		}		
	}
	
	//打印功能
    public ActionForward toPrintRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	ActionErrors errors = new ActionErrors();
    	HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;
		List barCodeList = new ArrayList();
		List noticeList=new ArrayList();
		ContractInvoiceManage manage=null;
		String jnlNo=request.getParameter("id");
		
		inserInformDate(form, request, errors, jnlNo);
		if(jnlNo!=null && !jnlNo.equals("")){
			try {
				hs=HibernateUtil.getSession();
				manage=(ContractInvoiceManage) hs.get(ContractInvoiceManage.class, jnlNo);
				String sql="select c.InvoiceDate,c.ContractNo,c.InvoiceType,c.InvoiceName,c.Rem,c.r9,v.contractSDate,"
						+ "v.contractEDate,v.contractTotal,c.maintStation,c.ARF_JnlNo "
						+ "from ContractInvoiceManage c,Compact_view v where c.BillNo=v.billno and c.jnlNo='"+jnlNo+"'";
				List list=hs.createSQLQuery(sql).list();
				for(Object object : list){
					Map map=new HashMap();
					Object[] objs=(Object[])object;
					map.put("depart", userInfo.getComName());
					map.put("invoiceDate", objs[0]);
					map.put("contractNo", objs[1]);
					map.put("invoiceType", bd.getName("InvoiceType", "inTypeName", "inTypeId", String.valueOf(objs[2])));
					map.put("invoiceName",objs[3]);
					map.put("rem", objs[4]);
					map.put("num", objs[5]);
					map.put("contractSDate", objs[6]);
					map.put("contractEDate", objs[7]);
					map.put("contractTotal", objs[8]);
					map.put("maintStation", bd.getName(hs,"Storageid", "storagename", "storageid", (String)objs[9]));
					String hql="select userName from ViewLoginUserInfo "
							+ "where storageId='"+(String)objs[9]+"' and class1='20' and enabledFlag='Y'";
					List nameList=hs.createQuery(hql).list();
					if(nameList!=null && nameList.size()>0){
						map.put("manager", nameList.get(0).toString());
					}else{
						map.put("manager", "");
					}
					ProContractArfeeMaster pro=(ProContractArfeeMaster) hs.get(ProContractArfeeMaster.class, (String)objs[10]);
					map.put("maintScope", manage.getMaintScope());
					map.put("recName", bd.getName("ReceivablesName", "recName", "recId", pro.getRecName()));
					
					map.put("operId", bd.getName("Loginuser", "username", "userid", manage.getOperId()));
					map.put("auditOperid2", bd.getName("Loginuser", "username", "userid", manage.getAuditOperid2()));
					map.put("auditOperid4", bd.getName("Loginuser", "username", "userid", manage.getAuditOperid4()));
					
					//更新录入人获取分部长
					String sqls="select UserID,UserName from Loginuser "
							+ "where RoleID='A02' and enabledflag='Y' and grcid in ('"+manage.getMaintDivision()+"')";
					List list2=hs.createSQLQuery(sqls).list();
					if(list2!=null && list2.size()>0){
						Object[] obj2=(Object[])list2.get(0);
						map.put("fbzuser", (String)obj2[1]);
					}else{
						map.put("fbzuser", "");
					}
					
					//开票信息
					String cusql=" from Customer where companyid='"+manage.getCompanyId()+"'";
					List cuslist=hs.createQuery(cusql).list();
					if(cuslist!=null && cuslist.size()>0){
						Customer cust=(Customer)cuslist.get(0);
						map.put("invoiceName", cust.getInvoiceName());
						map.put("taxId", cust.getTaxId());
						map.put("accountHolder", cust.getAccountHolder());
						map.put("bank", cust.getBank());
						map.put("account", cust.getAccount());
						map.put("companyname", cust.getCompanyName());
					}else{
						map.put("invoiceName", "");
						map.put("taxId", "");
						map.put("accountHolder", "");
						map.put("bank", "");
						map.put("account", "");
						map.put("companyname", "");
					}
					
					barCodeList.add(map);
				}
				//开票金额及日期，按照日期降序
				Map map0=new HashMap();
				map0.put("InvoiceDate", manage.getInvoiceDate());
				map0.put("InvoiceMoney", manage.getInvoiceMoney());
				noticeList.add(map0);
				
				sql="select invoiceDate,InvoiceMoney from ContractInvoiceManage "
						+ "where ContractNo='"+manage.getContractNo()+"' "
						+ " and invoiceDate<='"+manage.getInvoiceDate()+"' "
						+ " and isnull(istbp,'')='' and jnlno not in('"+manage.getJnlNo()+"')"
						+ " and auditStatus='Y' and auditStatus4='Y' "
						+ " order by invoiceDate desc";
				list =hs.createSQLQuery(sql).list();
				for(Object object : list){
					Map map=new HashMap();
					Object[] objs=(Object[])object;
					map.put("InvoiceDate", objs[0]);
					map.put("InvoiceMoney", objs[1]);
					noticeList.add(map);
				}
				
				//对barCodeList、noticeList操作
				BarCodePrint dy = new BarCodePrint();
				dy.toPrintTwoRecord2(response, barCodeList, noticeList);
			} catch (DataStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
    	return null;
    }
    /**
	 * 提醒备注
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toPrepareWarnRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		request.setAttribute("navigator.location","修改提醒备注");	
		
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		String jnlNo=(String)dform.get("jnlNo");
		if(jnlNo==null || jnlNo.trim().equals("")){
			jnlNo=request.getParameter("warnjnlNo");
		}

		try {
			hs = HibernateUtil.getSession();
			
			ProContractArfeeMaster master=(ProContractArfeeMaster) hs.get(ProContractArfeeMaster.class, jnlNo);
			dform.set("jnlNo",jnlNo);
			dform.set("contractNo",master.getContractNo());
			dform.set("warnRem",master.getWarnRem());
			
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
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
			forward = mapping.findForward("contractInvoiceManageWarnRem");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return forward;
	}
	/**
	 * 提醒备注
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toWarnRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		String jnlNo=(String)dform.get("jnlNo");
		String warnRem=(String)dform.get("warnRem");

		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			ProContractArfeeMaster master=(ProContractArfeeMaster) hs.get(ProContractArfeeMaster.class, jnlNo);
			if(master!=null){
				master.setWarnRem(warnRem);
				hs.save(master);
			}

			tx.commit();
			
			request.setAttribute("isclosed", "Y");
			
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
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
			forward = mapping.findForward("returnWarnRem");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return forward;
	}
	
}	