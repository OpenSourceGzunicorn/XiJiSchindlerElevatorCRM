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
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.JSONUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.basedata.receivablesname.ReceivablesName;
import com.gzunicorn.hibernate.contractpayment.procontractarfeemaster.ProContractArfeeMaster;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractdetail.EntrustContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster.EntrustContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractmaster.ServicingContractMaster;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.hibernate.viewmanager.compact_view.CompactView;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class ProContractARFeeMasterAction extends DispatchAction {

	Log log = LogFactory.getLog(ProContractARFeeMasterAction.class);
	
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
		String authority="procontractarfeemaster";
		if(name != null && name.contains("Audit")){
			authority = "procontractarfeemasteraudit";
		}
		
		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + authority, null);
		/** **********结束用户权限过滤*********** */
		
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
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
		
		request.setAttribute("navigator.location","应收款管理 >> 查询列表");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "proContractArfeeMasterList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fProContractARFeeMaster");
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
			
			String jnlNo = tableForm.getProperty("jnlNo");// 应收款流水号
			String contractType = tableForm.getProperty("contractType");// 合同类型
			String contractNo = tableForm.getProperty("contractNo");// 合同号
			String companyId = tableForm.getProperty("companyId");// 甲方单位
			String isInvoice = tableForm.getProperty("isInvoice");// 是否开票
			String submitType = tableForm.getProperty("submitType");// 提交标志
			String auditStatus = tableForm.getProperty("auditStatus");// 审核状态
			String maintDivision=tableForm.getProperty("maintDivision");//
			String isParagraph=tableForm.getProperty("isParagraph");
			String maintStation=tableForm.getProperty("maintStation");
			String invoiceName=tableForm.getProperty("invoiceName");//开票名称
			
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
				
				//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
				String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
				List vmlist=hs.createSQLQuery(sqlss).list();
				if(vmlist!=null && vmlist.size()>0){
					if(maintStation == null || maintStation.trim().equals("")){
						maintStation=userInfo.getStorageId();
					}
				}
				List mainStationList=new ArrayList();
				//维保站下拉框  A03=维保经理,A48=维保站文员,维保站长 A49 
				if(vmlist!=null && vmlist.size()>0){
					String hql="select a from Storageid a where "
							+ "(a.storageid= '"+userInfo.getStorageId()+"' or "
							+ "a.storageid in(select parentstorageid from Storageid a where a.storageid= '"+userInfo.getStorageId()+"')) "
							+ "and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";	
					mainStationList=hs.createQuery(hql).list();
					
				}else{
					String hql="select a from Storageid a where a.comid like '"+maintDivision+"' " +
							"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
					mainStationList=hs.createQuery(hql).list();
				  
					 Storageid storid=new Storageid();
					 storid.setStorageid("");
					 storid.setStoragename("全部");
					 mainStationList.add(0,storid);
				}

				String sql = "select v.jnlNo,v.contractType,v.contractNo,c.companyName,v.preDate,"+
				"v.rem,v.maintDivision,v.notInvoice,v.submitType,v.auditStatus,v.isInvoice,v.maintStation,v.isParagraph,v.notParagraph,v.warnRem,p.preMoney "+
				" from ViewProContractARFeeMaster v,Customer c,ProContractArfeeMaster p where v.companyId=c.companyId and v.jnlNo=p.jnlNo";
				
				if (jnlNo != null && !jnlNo.equals("")) {
					sql += " and v.jnlNo like '%"+jnlNo.trim()+"%'";
				}
				if (contractType != null && !contractType.equals("")) {
					sql += " and v.contractType like '%"+contractType.trim()+"%'";
				}
				if (contractNo != null && !contractNo.equals("")) {
					sql += " and v.contractNo like '%"+contractNo.trim()+"%'";
				}
				if (companyId != null && !companyId.equals("")) {
					sql += " and c.companyName like '%"+companyId.trim()+"%'";
				}
				if (isInvoice != null && !isInvoice.equals("")) {
					sql+=" and v.isInvoice like '%"+isInvoice.trim()+"%'";
				}
				if (submitType != null && !submitType.equals("")) {
					sql += " and v.submitType like '"+submitType.trim()+"'";
				}
				if (auditStatus != null && !auditStatus.equals("")) {
					sql += " and v.auditStatus like '"+auditStatus.trim()+"'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and v.maintDivision like '"+maintDivision.trim()+"'";
				}
				if(isParagraph!=null && !isParagraph.equals("")){
					sql+=" and v.isParagraph like '"+isParagraph.trim()+"'";
				}
				if(maintStation!=null && !maintStation.equals("")){
					sql+=" and v.maintStation like '"+maintStation.trim()+"'";
				}
				if(invoiceName!=null && !invoiceName.equals("")){
					sql+=" and c.invoiceName like '%"+invoiceName.trim()+"%'";
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
				List proContractARFeeMasterList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map master=new HashMap();
					master.put("jnlNo", objs[0]);
					master.put("contractType", objs[1]);
					master.put("contractNo", objs[2]);
					master.put("companyId", objs[3]);
					master.put("preDate", objs[4]);
					master.put("rem", objs[5]);
					master.put("maintDivision", bd.getName("Company", "comname", "comid",objs[6].toString()));
					master.put("notInvoice", df.format(objs[7]));//未开票金额
					master.put("submitType", objs[8]);
					master.put("auditStatus", objs[9]);
					master.put("isInvoice", objs[10]);
					if(objs[11]!=null && !"".equals(objs[11])){
						master.put("maintStation", bd.getName("Storageid", "storagename", "storageid",objs[11].toString()));
					}else{
						master.put("maintStation", objs[11]);
					}
					master.put("isParagraph", objs[12]);
					master.put("notParagraph", df.format(objs[13]));//未来款金额 
					master.put("warnRem", objs[14]);
					master.put("preMoney", objs[15]);
					proContractARFeeMasterList.add(master);
				}

				table.addAll(proContractARFeeMasterList);
				session.setAttribute("proContractArfeeMasterList", table);
				/*// 合同性质下拉框列表
				request.setAttribute("contractNatureOfList", bd.getPullDownList("MaintContractMaster_ContractNatureOf"));*/
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);
				// 维保站下拉框列表
				request.setAttribute("stationList", mainStationList);

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
			forward = mapping.findForward("proContractArfeeMasterList");
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
		
		request.setAttribute("navigator.location","合同 >> 查询列表 ");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "proContractARFeeMasterNextList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fProContractARFeeMasterNext");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("billno");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String billno = tableForm.getProperty("billno");// 流水号
			String maintContractNo = tableForm.getProperty("maintContractNo");// 合同号
			String busType = tableForm.getProperty("busType");// 合同类型
			String maintDivision =tableForm.getProperty("maintDivision");// 维保分部
			String salesContractNo = tableForm.getProperty("salesContractNo");// 销售合同号
			/*if(maintDivision!=null && maintDivision.equals("00")){
				maintDivision="";
			}*/
						
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
				//应收款只出审核日期为2016-08-05之后的合同,去掉委托合同的限制,20180525去掉合同状态限制
				String sql = "select billno,maintContractNo,busType,num,maintDivision,companyID "+
						"from Compact_view where "+
						" maintContractNo not in (select maintContractNo from Compact_view a,(select ContractNo,SUM(PreMoney) preMoney from ProContractARFeeMaster group by ContractNo) b where a.maintContractNo=b.ContractNo and a.contractTotal=b.preMoney)"+
						//" and maintContractNo not in (select maintContractNo from EntrustContractMaster)"+
						//" and contractStatus in('ZB','XB') "+
						" and contractTotal>0 and auditdate>'2016-08-05 00:00:00' ";
				
				if(salesContractNo!=null && !salesContractNo.equals("")){
					sql+=" and billno in (select billNo2 from Compact_view_Detail where salesContractNo2 like '%"+salesContractNo.trim()+"%')";
				}
				if (billno != null && !billno.equals("")) {
					sql += " and billno like '%"+billno.trim()+"%'";
				}
				if (maintContractNo != null && !maintContractNo.equals("")) {
					sql += " and maintContractNo like '%"+maintContractNo.trim()+"%'";
				}				
				if (busType != null && !busType.equals("")) {
					sql += " and busType = '"+busType.trim()+"'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and maintDivision like '"+maintDivision.trim()+"'";
				}				
				if (table.getIsAscending()) {
					sql += " order by "+ table.getSortColumn() +" desc";
				} else {
					sql += " order by "+ table.getSortColumn() +" asc";
				}
				
				//System.out.println("应付款=="+sql);
				query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List proContractARFeeMasterNextList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map map = new HashMap();
					map.put("billno", objs[0]);
					map.put("maintContractNo", objs[1]);
					map.put("busType", objs[2]);
					map.put("num", objs[3]);
					map.put("maintDivision",bd.getName_Sql("Company", "comname", "comid", objs[4].toString()));
					map.put("companyID", bd.getName_Sql("Customer", "companyName", "companyId", objs[5].toString()));
					proContractARFeeMasterNextList.add(map);
				}

				table.addAll(proContractARFeeMasterNextList);
				session.setAttribute("proContractARFeeMasterNextList", table);
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
			forward = mapping.findForward("proContractArfeeMasterNextList");
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
		
		request.setAttribute("navigator.location","应收款确认>> 查询列表");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "proContractArfeeMasterAuditList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fProContractARFeeMasterAudit");
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
			
			String jnlNo = tableForm.getProperty("jnlNo");// 应收款流水号
			String contractType = tableForm.getProperty("contractType");// 合同类型
			String contractNo = tableForm.getProperty("contractNo");// 合同号
			String companyId = tableForm.getProperty("companyId");// 甲方单位
			String isInvoice = tableForm.getProperty("isInvoice");// 是否开票
			String auditStatus = tableForm.getProperty("auditStatus");// 审核状态
			if(auditStatus==null || auditStatus.trim().equals("")){
				auditStatus="N";
				tableForm.setProperty("auditStatus",auditStatus);
			}
			String maintDivision=tableForm.getProperty("maintDivision");//
			String isParagraph=tableForm.getProperty("isParagraph");
			String maintStation=tableForm.getProperty("maintStation");

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

				String sql = "select v.jnlNo,v.contractType,v.contractNo,c.companyName,v.preDate,v.rem,"+
				"v.maintDivision,v.notInvoice,v.submitType,v.auditStatus,v.isInvoice,v.maintStation,v.isParagraph,v.notParagraph,v.warnRem,p.preMoney "+
				" from ViewProContractARFeeMaster v,Customer c,ProContractArfeeMaster p where v.companyId=c.companyId and v.jnlNo=p.jnlNo and v.submitType='Y'";
				
				if (jnlNo != null && !jnlNo.equals("")) {
					sql += " and v.jnlNo like '%"+jnlNo.trim()+"%'";
				}
				if (contractType != null && !contractType.equals("")) {
					sql += " and v.contractType like '%"+contractType.trim()+"%'";
				}
				if (contractNo != null && !contractNo.equals("")) {
					sql += " and v.contractNo like '%"+contractNo.trim()+"%'";
				}
				if (companyId != null && !companyId.equals("")) {
					sql += " and c.companyName like '%"+companyId.trim()+"%'";
				}
				if (isInvoice != null && !isInvoice.equals("")) {
					sql+=" and v.isInvoice like '%"+isInvoice.trim()+"%'";
				}
				if (auditStatus != null && !auditStatus.equals("")) {
					sql += " and v.auditStatus like '"+auditStatus.trim()+"'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and v.maintDivision like '"+maintDivision.trim()+"'";
				}
				if(isParagraph!=null && !isParagraph.equals("")){
					sql+=" and v.isParagraph like '"+isParagraph.trim()+"'";
				}
				if(maintStation!=null && !maintStation.equals("")){
					sql+=" and v.maintStation like '"+maintStation.trim()+"'";
				}
				if (table.getIsAscending()) {
					sql += " order by "+ table.getSortColumn() +" asc";
				} else {
					sql += " order by "+ table.getSortColumn() +" desc";
				}
				//System.out.println(sql);
				query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List proContractArfeeMasterList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map master=new HashMap();
					master.put("jnlNo", objs[0]);
					master.put("contractType", objs[1]);
					master.put("contractNo", objs[2]);
					master.put("companyId", objs[3]);
					master.put("preDate", objs[4]);
					master.put("rem", objs[5]);
					master.put("maintDivision", bd.getName("Company", "comname", "comid",objs[6].toString()));
					master.put("notInvoice", df.format(objs[7]));//未开票金额
					master.put("submitType", objs[8]);
					master.put("auditStatus", objs[9]);
					master.put("isInvoice", objs[10]);
					if(objs[11]!=null && !"".equals(objs[11]))
						master.put("maintStation", bd.getName("Storageid", "storagename", "storageid",objs[11].toString()));
					else
						master.put("maintStation", objs[11]);
					master.put("isParagraph", objs[12]);
					master.put("notParagraph", df.format(objs[13]));//未来款金额 
					master.put("warnRem",objs[14]);
					master.put("preMoney",objs[15]);
					
					proContractArfeeMasterList.add(master);
				}

				table.addAll(proContractArfeeMasterList);
				session.setAttribute("proContractArfeeMasterAuditList", table);
				
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);
				// 维保站下拉框列表
				request.setAttribute("stationList", bd.getMaintStationList(userInfo.getComID()));
				
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
			forward = mapping.findForward("proContractArfeeMasterAuditList");
		}
		return forward;
	}
	
	public List getStation(ViewLoginUserInfo user){
		Session hs=null;
		List stationList=new ArrayList();
		String division=user.getComID();
		if(division!=null && "00".equals(division))
			division="";
		try {
			hs=HibernateUtil.getSession();
			String hql="from Storageid where comid like'%"+division+"%'";
			stationList=hs.createQuery(hql).list();
		} catch (DataStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(hs!=null)
				hs.close();
		}
		return stationList;
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
		
		request.setAttribute("navigator.location","应收款管理 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		
		display(form, request, errors, "display");
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("display", "yes");
		forward = mapping.findForward("proContractArfeeMasterDisplay");
		return forward;
	}
	
	public ActionForward toAuditDisplay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","应收款确认 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		
		display(form, request, errors, "display");
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		request.setAttribute("display", "yes");
		forward = mapping.findForward("proContractArfeeAuditDisplay");
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

		request.setAttribute("navigator.location","应收款管理 >> 添加");
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}
		String billno=request.getParameter("billno");
		if(billno==null || "".equals(billno))
			billno=(String)request.getAttribute("billno");
			//billno=(String) dform.get("billno");
		
		Session hs = null;
		Query query = null;
		try {
			hs = HibernateUtil.getSession();
			ProContractArfeeMaster master=new ProContractArfeeMaster();
			master.setOperId(userInfo.getUserID());
			request.setAttribute("operName", userInfo.getUserName());
			master.setOperDate(CommonUtil.getNowTime());
			master.setPreDate(CommonUtil.getToday());
			CompactView compact=new CompactView();
			String sql="from CompactView where id.billno='"+billno+"'";
			List list1=hs.createQuery(sql).list();
			if(list1!=null && list1.size()>0){
				compact=(CompactView) list1.get(0);
				
				master.setBillNo(compact.getId().getBillno());
				master.setContractNo(compact.getId().getMaintContractNo());
				master.setCompanyId(compact.getId().getCompanyId());
				master.setContractType(compact.getId().getBusType());
				master.setMaintDivision(compact.getId().getMaintDivision());
				master.setMaintStation(compact.getId().getMaintStation());
				request.setAttribute("contractTotal", compact.getId().getContractTotal());
				String hql="select sum(preMoney) from ProContractArfeeMaster where contractNo='"+compact.getId().getMaintContractNo()+"' group by contractNo";
				List list=hs.createSQLQuery(hql).list();
				double builtReceivables=0;
				if(list!=null && list.size()>0){
					builtReceivables=Double.valueOf(list.get(0).toString());
				}
				List proList=hs.createQuery("from ProContractArfeeMaster where contractNo='"+master.getContractNo()+"'").list();
				if(proList!=null && proList.size()>0){
					for(Object object : proList){
						ProContractArfeeMaster pro=(ProContractArfeeMaster)object;
						pro.setRecName(bd.getName_Sql("ReceivablesName", "recName", "recId", pro.getRecName()));
					}
				}
				request.setAttribute("proList", proList);
				double notBuiltReceivables=compact.getId().getContractTotal()-builtReceivables;
				request.setAttribute("builtReceivables", builtReceivables);
				request.setAttribute("noBuiltReceivables", df.format(notBuiltReceivables));
				request.setAttribute("maintDivisionName", compact.getId().getComName());
				request.setAttribute("maintStationName", compact.getId().getStorageName());
				request.setAttribute("companyName", bd.getName_Sql("Customer", "companyName", "companyId", compact.getId().getCompanyId()));
				request.setAttribute("receivablesList", this.getReceivablesNameList());
				session.setAttribute("proContractArfeeMasterBean", master);
			}
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (HibernateException e1) {
			e1.printStackTrace();
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
		
		request.setAttribute("returnMethod", "toSearchRecord");
		
		saveToken(request); //生成令牌，防止表单重复提交
		
		return mapping.findForward("proContractArfeeMasterAdd");
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
		
		request.setAttribute("navigator.location","应收款管理 >> 修改");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		
		display(form, request, errors, "");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
			
		request.setAttribute("returnMetho", "toSearchRecord");
		forward = mapping.findForward("proContractArfeeMasterModify");
		
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
			ProContractArfeeMaster master = (ProContractArfeeMaster) hs.get(ProContractArfeeMaster.class, id);
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
	 * 跳转到提醒备注页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareWarnRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","应收款管理 >> 提醒备注");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		
		display(form, request, errors, "display");
		request.setAttribute("doType", "warn");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
			
//		request.setAttribute("returnMetho", "toSearchRecord");
		forward = mapping.findForward("proContractArfeeMasterWarn");
		
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
		String jnlNo=(String) dform.get("jnlNo");
		String warnRem=(String)dform.get("warnRem");
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			ProContractArfeeMaster master=(ProContractArfeeMaster) hs.get(ProContractArfeeMaster.class, jnlNo);
			if(master!=null){
				master.setWarnRem(warnRem);
				hs.save(master);
			}
			/*String id = (String) dform.get("id");
			ProContractArfeeMaster master = (ProContractArfeeMaster) hs.get(ProContractArfeeMaster.class, id);
			if (master != null) {
				hs.delete(master);
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.succeed"));
			}*/
			tx.commit();
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
			forward = mapping.findForward("returnList");
		} catch (Exception e) {
			e.printStackTrace();
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
		
		request.setAttribute("navigator.location","应收款确认 >> 确认");	
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
		forward = mapping.findForward("proContractArfeeMasterAudit");
		
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
			ProContractArfeeMaster master=(ProContractArfeeMaster) hs.get(ProContractArfeeMaster.class, jnlNo.trim());
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
				Query query = hs.createQuery("from ProContractArfeeMaster where jnlNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {					
					// 主信息
					ProContractArfeeMaster master = (ProContractArfeeMaster) list.get(0);
					MaintContractMaster maint=new MaintContractMaster();
					if(master.getContractType().equals("B")){
						maint=(MaintContractMaster) hs.get(MaintContractMaster.class, master.getBillNo());
						request.setAttribute("contractTotal", maint.getContractTotal());
						String hql="select sum(preMoney) from ProContractArfeeMaster where contractNo='"+maint.getMaintContractNo()+"' group by contractNo";
						List list2=hs.createSQLQuery(hql).list();
						double builtReceivables=0;
						if(list!=null && list2.size()>0){
							builtReceivables=Double.valueOf(list2.get(0).toString());
						}
						double notBuiltReceivables=maint.getContractTotal()-builtReceivables+master.getPreMoney();
						if("display".equals(flag)){
							notBuiltReceivables=maint.getContractTotal()-builtReceivables;
						}
						request.setAttribute("builtReceivables", builtReceivables);
						request.setAttribute("noBuiltReceivables", df.format(notBuiltReceivables));
					}
					ServicingContractMaster service=new ServicingContractMaster();
					if(master.getContractType().equals("G") || master.getContractType().equals("W")){
						service=(ServicingContractMaster) hs.get(ServicingContractMaster.class, master.getBillNo());
						request.setAttribute("contractTotal", service.getContractTotal());
						String hql="select sum(preMoney) from ProContractArfeeMaster where contractNo='"+service.getMaintContractNo()+"' group by contractNo";
						List list2=hs.createSQLQuery(hql).list();
						double builtReceivables=0;
						if(list!=null && list2.size()>0){
							builtReceivables=Double.valueOf(list2.get(0).toString());
						}
						double notBuiltReceivables=service.getContractTotal()-builtReceivables+master.getPreMoney();
						if("display".equals(flag)){
							notBuiltReceivables=service.getContractTotal()-builtReceivables;
						}
						request.setAttribute("builtReceivables", builtReceivables);
						request.setAttribute("noBuiltReceivables", df.format(notBuiltReceivables));
					}
					
					if(master.getAuditOperid()!=null && !master.getAuditOperid().equals("")){
						master.setAuditOperid(bd.getName(hs, "Loginuser", "username", "userid", master.getAuditOperid()));
					}
														
					if("display".equals(flag)){
						master.setMaintDivision(bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision()));// 维保分部
						master.setMaintStation(bd.getName(hs, "Storageid", "storagename", "storageid", master.getMaintStation()));
						master.setCompanyId(bd.getName_Sql("Customer", "companyName", "companyId", master.getCompanyId()));
						master.setOperId(bd.getName(hs, "Loginuser", "username", "userid", master.getOperId()));
						master.setRecName(bd.getName_Sql("ReceivablesName", "recName", "recId", master.getRecName()));
						
					} else {						
						request.setAttribute("companyName", bd.getName(hs,"Customer", "companyName", "companyId", master.getCompanyId()));
						request.setAttribute("maintDivisionName",bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision())); // 维保分部名称
						request.setAttribute("maintStationName", bd.getName(hs, "Storageid", "storagename", "storageid", master.getMaintStation()));
						request.setAttribute("operName", bd.getName(hs, "Loginuser", "username", "userid", master.getOperId()));
						request.setAttribute("receivablesList", this.getReceivablesNameList());
						
					}
					/*List proList=hs.createQuery("from ProContractArfeeMaster where auditStatus='Y' and contractNo='"+master.getContractNo()+"'").list();
					//System.out.println(proList.size());
					request.setAttribute("proList", proList);*/
					request.setAttribute("proContractArfeeMasterBean", master);	
					
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
		ProContractArfeeMaster master = null;
		String id = (String) dform.get("id");
		String submitType=(String)dform.get("submitType");
		String jnlNo = null;

		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			if (id != null && !id.equals("")) { // 修改		
				master = (ProContractArfeeMaster) hs.get(ProContractArfeeMaster.class, id);
				jnlNo = master.getJnlNo();
			} else { // 新增
				master = new ProContractArfeeMaster();
				
				String todayDate = CommonUtil.getToday();
				String year = todayDate.substring(2,4);
				jnlNo = CommonUtil.getBillno(year,"ProContractArfeeMaster", 1)[0];// 生成流水号
			}
			
			BeanUtils.populate(master, dform.getMap()); // 复制所有属性
//			dform.set("billno", master.getBillNo());
			request.setAttribute("billno", master.getBillNo());
			
			master.setJnlNo(jnlNo);// 流水号	
			master.setSubmitType(submitType);
			master.setAuditStatus("N");
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
		
	public void refer(ActionForm form, HttpServletRequest request,ActionErrors errors){
		
		String id = request.getParameter("id"); 
		
		if(id != null && !id.equals("")){
			
			Session hs = null;
			Transaction tx = null;
			ProContractArfeeMaster master = null;				
			
			try {
				
				hs = HibernateUtil.getSession();		
				tx = hs.beginTransaction();
				
				master = (ProContractArfeeMaster) hs.get(ProContractArfeeMaster.class, id);
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
}	