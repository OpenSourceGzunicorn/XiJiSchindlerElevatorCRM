package com.gzunicorn.struts.action.engcontractmanager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cache.QueryKey;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.engine.QueryParameters;
import org.hibernate.jmx.HibernateServiceMBean;
import org.hibernate.sql.QuerySelect;
import org.jbpm.context.exe.variableinstance.HibernateStringInstance;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
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

import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.JSONUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractdetail.EntrustContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster.EntrustContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractquotedetail.EntrustContractQuoteDetail;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractquotemaster.EntrustContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class EntrustContractMasterAction extends DispatchAction {

	Log log = LogFactory.getLog(EntrustContractMasterAction.class);
	
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
		
		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		String authority="entrustcontractmaster";
		if(name != null && name.contains("Audit")){
			authority = "entrustcontractmasteraudit";
		}
		/*if(name!=null && name.contains("Display")){
			authority="entrustcontractmaster";
		}*/
		
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
		
		request.setAttribute("navigator.location","维保委托合同管理 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form,request,response);
				forward = mapping.findForward("exportExcel");
				tableForm.setProperty("genReport","");
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport","");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "entrustContractMasterList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fEntrustContractMaster");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("billNo");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			}else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String billNo = tableForm.getProperty("billNo");// 流水号
			String entrustContractNo = tableForm.getProperty("entrustContractNo");// 委托合同号
			String companyName = tableForm.getProperty("companyName");// 甲方单位
			String attn = tableForm.getProperty("attn");// 经办人
			String maintDivision = tableForm.getProperty("maintDivision");// 所属维保站			
			String submitType = tableForm.getProperty("submitType");// 提交标志
			String auditStatus = tableForm.getProperty("auditStatus");// 审核状态
			String maintContractNo = tableForm.getProperty("maintContractNo");// 维保合同
			String contractNatureOf = tableForm.getProperty("contractNatureOf");// 合同性质
			String salesContractNo=tableForm.getProperty("salesContractNo");//销售合同号
			String r1=tableForm.getProperty("r1");//合同状态
			String mainmode=tableForm.getProperty("mainmode");//维保合同是否收费
			

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

				String sql = "select e.billNo,e.entrustContractNo,e.contractSdate,e.contractEdate," +
						" l.username,c.comname,e.submitType,e.auditStatus,"+ 
						" d.companyName,e.maintContractNo,e.contractNatureOf,e.r1"+
						" from EntrustContractMaster e left outer join Customer d on e.companyId2 = d.companyId,Loginuser l,Company c" +
						" where e.attn=l.userid" +
						" and e.maintDivision=c.comid";

				if (mainmode != null && !mainmode.equals("")) {
					sql += " and e.mainMode like '"+mainmode.trim()+"'";
				}
				if (billNo != null && !billNo.equals("")) {
					sql += " and e.billNo like '%"+billNo.trim()+"%'";
				}
				if (entrustContractNo != null && !entrustContractNo.equals("")) {
					sql += " and e.entrustContractNo like '%"+entrustContractNo.trim()+"%'";
				}
				if (companyName != null && !companyName.equals("")) {
					sql += " and companyName like '%"+companyName.trim()+"%'";
				}
				if (attn != null && !attn.equals("")) {
					sql += " and l.username like '%"+attn.trim()+"%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and c.comid like '"+maintDivision.trim()+"'";
				}
				if (submitType != null && !submitType.equals("")) {
					sql += " and e.submitType like '"+submitType.trim()+"'";
				}
				if (auditStatus != null && !auditStatus.equals("")) {
					sql += " and e.auditStatus like '"+auditStatus.trim()+"'";
				}
				if (maintContractNo != null && !maintContractNo.equals("")) {
					sql += " and e.maintContractNo like '"+maintContractNo.trim()+"'";
				}
				if (contractNatureOf != null && !contractNatureOf.equals("")) {
					sql += " and e.contractNatureOf = '"+contractNatureOf.trim()+"'";
				}
				if (salesContractNo != null && !salesContractNo.equals("")) {
					sql += " and e.BillNo in (select BillNo from EntrustContractDetail where SalesContractNo like '%"+salesContractNo.trim()+"%')";
				}
				
				if(r1!=null && !r1.equals("")){
					sql+=" and e.r1 like '"+r1.trim()+"'";
				}
				if (table.getIsAscending()) {
					sql += " order by "+ table.getSortColumn() +" desc";
				} else {
					sql += " order by "+ table.getSortColumn() +" asc";
				}
				
				query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List entrustContractList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map master=new HashMap();
					master.put("billNo", objs[0]);
					master.put("entrustContractNo", objs[1]);
					master.put("contractSdate", objs[2]);
					master.put("contractEdate", objs[3]);
					master.put("attn", objs[4]);
					master.put("maintDivision", objs[5]);
					master.put("submitType", objs[6]);
					master.put("auditStatus", objs[7]);
					master.put("companyName", objs[8]);
					master.put("maintContractNo", objs[9]);
					master.put("contractNatureOf", objs[10]);
					master.put("r1", objs[11]);
					entrustContractList.add(master);
				}

				table.addAll(entrustContractList);
				session.setAttribute("entrustContractMasterList", table);
				// 合同性质下拉框列表
				request.setAttribute("contractNatureOfList", bd.getPullDownList("MaintContractMaster_ContractNatureOf"));
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
			forward = mapping.findForward("entrustContractMasterList");
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
		
		request.setAttribute("navigator.location","维保合同 >> 查询列表 ");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "entrustContractMasterNextList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fEntrustContractMasterNext");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("billNo");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String billNo = tableForm.getProperty("billNo");// 维保流水号
			String companyName = tableForm.getProperty("companyName");// 经办人
//			String status = tableForm.getProperty("status");// 流程状态
			String maintDivision = tableForm.getProperty("maintDivision");// 维保分部
			String maintContractNo = tableForm.getProperty("maintContractNo");// 维保合同号
			String salesContractNo=tableForm.getProperty("salesContractNo");//销售合同号
			String salesContractNoStr="";
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
				
				if (salesContractNo != null && !salesContractNo.equals("")) {
					String salesContractNohql = "select ecqd.billNo from EntrustContractQuoteDetail ecqd,MaintContractDetail mcd where mcd.rowid=ecqd.wbRowid and mcd.salesContractNo like '%"+salesContractNo.trim()+"%'";
					List salesContractNoList=hs.createQuery(salesContractNohql).list();
				   if(salesContractNoList!=null&&salesContractNoList.size()>0){
					   for(int i=0;i<salesContractNoList.size();i++){
						   salesContractNoStr+=i==salesContractNoList.size()-1?"'"+salesContractNoList.get(i)+"'":"'"+salesContractNoList.get(i)+"',";
					   }
				   }
				
				}
				//只显示在保，续保的数据
				String sql = "select a.*,isnull(b.companyName,'') companyName,c.username from EntrustContractQuoteMaster a"+
				" left outer join Customer b on a.companyId=b.companyId,"+
				"Loginuser c where a.operId=c.userId and status=1 "+
				"and billNo not in(select distinct isnull(Quote_BillNo,'') from EntrustContractMaster)";
				
				if (billNo != null && !billNo.equals("")) {
					sql += " and a.billNo like '%"+billNo.trim()+"%'";
				}
				if (companyName != null && !companyName.equals("")) {
					sql += " and b.companyName like '%"+companyName.trim()+"%'";
				}				
//				if (status != null && !status.equals("")) {
//					sql += " and status = '"+Integer.valueOf(status)+"'";
//				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and a.maintDivision like '"+maintDivision.trim()+"'";
				}
				
				if (maintContractNo != null && !maintContractNo.equals("")) {
					sql += " and a.maintContractNo like '%"+maintContractNo.trim()+"%'";
				}
				
				if(salesContractNo != null && !salesContractNo.equals("")){
					if (salesContractNoStr != null && !salesContractNoStr.equals("")) {
						sql += " and a.billNo in ("+salesContractNoStr+")";
					}else{
						sql += " and a.billNo = ''";
					}
				}
				
				
				if (table.getIsAscending()) {
					sql += " order by a."+ table.getSortColumn() +" desc";
				} else {
					sql += " order by a."+ table.getSortColumn() +" asc";
				}
				
				query = hs.createSQLQuery(sql);
				query.addEntity("a",EntrustContractQuoteMaster.class);
				query.addScalar("companyName");
				query.addScalar("username");
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List maintContractQuoteList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					EntrustContractQuoteMaster master = (EntrustContractQuoteMaster) objs[2];
					
					//master.setCompanyId(bd.getName_Sql("Customer", "companyName", "companyId", master.getCompanyId()));
					master.setCompanyId(objs[0].toString());
					master.setOperId(objs[1].toString());
					//master.setR1(bd.getName_Sql("ViewFlowStatus", "typename", "typeid", String.valueOf(master.getStatus())));
					master.setMaintDivision(bd.getName("Company","comname","comid",master.getMaintDivision()));
					maintContractQuoteList.add(master);
				}

				table.addAll(maintContractQuoteList);
				session.setAttribute("entrustContractMasterNextList", table);
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
			forward = mapping.findForward("entrustContractMasterNextList");
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
		
		request.setAttribute("navigator.location","维保委托合同审核>> 查询列表");		
		ActionForward forward = null;
		String authority=request.getParameter("authority");
		request.setAttribute("authority", authority);
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
			HTMLTableCache cache = new HTMLTableCache(session, "entrustContractMasterAuditList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fEntrustContractMasterAudit");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("billNo");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String billNo = tableForm.getProperty("billNo");// 流水号
			String entrustContractNo = tableForm.getProperty("entrustContractNo");// 委托合同号
			String companyName = tableForm.getProperty("companyName");// 甲方单位
			String attn = tableForm.getProperty("attn");// 经办人
			String maintDivision = tableForm.getProperty("maintDivision");// 所属维保站			
			String auditStatus = tableForm.getProperty("auditStatus");// 审核状态
			String maintContractNo = tableForm.getProperty("maintContractNo");// 维保合同
			String contractNatureOf = tableForm.getProperty("contractNatureOf");// 合同性质
			String r1=tableForm.getProperty("r1");//
			//第一次进入页面时根据登陆人初始化所属维保分部
			List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
			if(maintDivision == null || maintDivision.equals("")){
				Map map = (Map)maintDivisionList.get(0);
				maintDivision = (String)map.get("grcid");
			}
			
			if(auditStatus==null){
				auditStatus="N";
				tableForm.setProperty("auditStatus", auditStatus);
			}
			
			Session hs = null;
			Query query = null;
			try {

				hs = HibernateUtil.getSession();

				String sql = "select e.billNo,e.entrustContractNo,e.contractSdate,e.contractEdate," +
						" l.username,c.comname,e.auditStatus,"+ 
						" d.companyName,e.maintContractNo,e.contractNatureOf,e.r1" +
						" from EntrustContractMaster e left outer join Customer d on e.companyId2 = d.companyId,Loginuser l,Company c" +
						" where e.attn=l.userid" +
						" and e.maintDivision=c.comid" +
						" and e.submitType='Y'";
				
				if (billNo != null && !billNo.equals("")) {
					sql += " and e.billNo like '%"+billNo.trim()+"%'";
				}
				if (entrustContractNo != null && !entrustContractNo.equals("")) {
					sql += " and e.entrustContractNo like '%"+entrustContractNo.trim()+"%'";
				}
				if (companyName != null && !companyName.equals("")) {
					sql += " and companyName like '%"+companyName.trim()+"%'";
				}
				if (attn != null && !attn.equals("")) {
					sql += " and l.username like '%"+attn.trim()+"%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and c.comid like '"+maintDivision.trim()+"'";
				}
				if (auditStatus != null && !auditStatus.equals("")) {
					sql += " and e.auditStatus like '"+auditStatus.trim()+"'";
				}
				if (maintContractNo != null && !maintContractNo.equals("")) {
					sql += " and e.maintContractNo like '"+maintContractNo.trim()+"'";
				}
				if (contractNatureOf != null && !contractNatureOf.equals("")) {
					sql += " and e.contractNatureOf = '"+contractNatureOf.trim()+"'";
				}
				if(r1!=null && !r1.equals("")){
					sql+=" and e.r1 like '"+r1.trim()+"'";
				}
				if (table.getIsAscending()) {
					sql += " order by "+ table.getSortColumn() +" desc";
				} else {
					sql += " order by "+ table.getSortColumn() +" asc";
				}
				
				query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List entrustContractList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map master=new HashMap();
					master.put("billNo", objs[0]);
					master.put("entrustContractNo", objs[1]);
					master.put("contractSdate", objs[2]);
					master.put("contractEdate", objs[3]);
					master.put("attn", objs[4]);
					master.put("maintDivision", objs[5]);
					master.put("auditStatus", objs[6]);
					master.put("companyName", objs[7]);
					master.put("maintContractNo", objs[8]);
					master.put("contractNatureOf", objs[9]);
					master.put("r1", objs[10]);
					entrustContractList.add(master);
				}

				table.addAll(entrustContractList);
				session.setAttribute("entrustContractMasterAuditList", table);
				// 合同性质下拉框列表
				request.setAttribute("contractNatureOfList", bd.getPullDownList("MaintContractMaster_ContractNatureOf"));
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
			forward = mapping.findForward("entrustContractMasterAuditList");
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
		
		request.setAttribute("navigator.location","维保委托合同管理 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		String returnMethod=request.getParameter("returnMethod");
		
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
				String upsql="update EntrustContractMaster set workisdisplay='"+workisdisplay+"' where billno='"+id+"'";
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
		
		request.setAttribute("returnMethod", returnMethod);
		request.setAttribute("display", "yes");
		forward = mapping.findForward("entrustContractMasterDisplay");
		return forward;
	}
	
	public ActionForward toAuditDisplay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response){
		request.setAttribute("navigator.location","维保委托合同审核 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		String returnMethod=request.getParameter("returnMethod");
		
		display(form, request, errors, "display");
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		request.setAttribute("returnMethod", returnMethod);
		request.setAttribute("display", "yes");
		forward = mapping.findForward("entrustContractMasterAuditDisplay");
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

		request.setAttribute("navigator.location","维保委托合同管理 >> 添加");
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}
		
		
		Session hs = null;
		Query query = null;
		try {
			hs = HibernateUtil.getSession();

			// 主信息
			EntrustContractMaster entrustContractMasterBean = new EntrustContractMaster();
			entrustContractMasterBean.setAttn(userInfo.getUserName()); //经办人
			entrustContractMasterBean.setMaintDivision(userInfo.getComID()); //维保分部

			
			// 选择新建
			EntrustContractQuoteMaster quote=(EntrustContractQuoteMaster) hs.get(EntrustContractQuoteMaster.class, request.getParameter("billNo").trim());
			
			MaintContractMaster maintContract = (MaintContractMaster) hs.get(MaintContractMaster.class, quote.getMaintBillNo());
			
			entrustContractMasterBean.setCompanyId(maintContract.getCompanyId2()); //甲方单位代码
			entrustContractMasterBean.setContractTotal(quote.getRealPrice()); //合同总额
			entrustContractMasterBean.setContractSdate(quote.getContractSdate());
			entrustContractMasterBean.setContractEdate(quote.getContractEdate());
			entrustContractMasterBean.setContractPeriod(quote.getContractPeriod());
			entrustContractMasterBean.setMaintBillNo(maintContract.getBillNo());
			entrustContractMasterBean.setContractNatureOf(quote.getContractNatureOf());
			entrustContractMasterBean.setMaintContractNo(maintContract.getMaintContractNo());
			//entrustContractMasterBean.setContractNatureOf(maintContract.getContractNatureOf());
			entrustContractMasterBean.setMainMode(maintContract.getMainMode());
			//entrustContractMasterBean.setOtherFee(maintContract.getOtherFee());
			entrustContractMasterBean.setMaintStation(maintContract.getMaintStation());
			entrustContractMasterBean.setMaintDivision(maintContract.getMaintDivision());
			entrustContractMasterBean.setQuoteBillNo(quote.getBillNo());
			request.setAttribute("maintStationName", bd.getName(hs, "Storageid", "storagename", "storageid", maintContract.getMaintStation()));
			request.setAttribute("maintDivisionName", bd.getName(hs, "Company", "comname", "comid",maintContract.getMaintDivision()));
			request.setAttribute("contractNatureOfName", bd.getOptionName(quote.getContractNatureOf(), bd.getPullDownList("MaintContractMaster_ContractNatureOf")));
			// 甲方单位信息
			Customer companyA = (Customer) hs.get(Customer.class,maintContract.getCompanyId2());
			request.setAttribute("companyA",companyA);
			//乙方单位信息
			entrustContractMasterBean.setCompanyId2(quote.getCompanyId());
			Customer companyB=(Customer)hs.get(Customer.class, quote.getCompanyId());
			request.setAttribute("companyB", companyB);
			
			// 报价申请电梯信息明细列表
			String hql="from EntrustContractQuoteDetail where billNo='"+quote.getBillNo()+"'";
			List list = hs.createQuery(hql).list();
			List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型下拉列表
			List entrustContractDetailList = new ArrayList();
			MaintContractDetail deatail = null;
			EntrustContractDetail entrust=null;
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					EntrustContractQuoteDetail ecqd=(EntrustContractQuoteDetail) list.get(i);
					deatail=(MaintContractDetail) hs.get(MaintContractDetail.class, Integer.valueOf(ecqd.getWbRowid()));
					deatail.setElevatorType(bd.getOptionName(deatail.getElevatorType()+"", elevatorTypeList));
					deatail.setMainSdate(ecqd.getMainSdate());
					deatail.setMainEdate(ecqd.getMainEdate());
					entrustContractDetailList.add(deatail);
				}
			}
			
			request.setAttribute("entrustContractDetailList", entrustContractDetailList);		
			request.setAttribute("entrustContractMasterBean", entrustContractMasterBean);
			//维保合同合同性质
//			request.setAttribute("maintContractNatureOf", maintContract.getContractNatureOf());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		
		request.setAttribute("returnMethod", "toSearchRecord");
		// 维保分部名称
		saveToken(request); //生成令牌，防止表单重复提交
		
		return mapping.findForward("entrustContractMasterAdd");
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
		
		request.setAttribute("navigator.location","维保委托合同管理 >> 修改");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		
		display(form, request, errors, "");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
			
		request.setAttribute("returnMetho", "toSearchRecord");
		forward = mapping.findForward("entrustContractMasterModify");
		
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
			EntrustContractMaster master = (EntrustContractMaster) hs.get(EntrustContractMaster.class, id);
			if (master != null) {
				hs.createQuery("delete from EntrustContractDetail where billno='"+id+"'").executeUpdate();
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
	 * 退保
	 */
	public ActionForward toSurrenderRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, HibernateException {
		
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		String id = request.getParameter("id"); 

		if(id != null && !id.equals("")){
			
			Session hs = null;
			Transaction tx = null;
			EntrustContractMaster master = null;				
			
			try {
				
				hs = HibernateUtil.getSession();		
				tx = hs.beginTransaction();
				
				master = (EntrustContractMaster) hs.get(EntrustContractMaster.class, id);
				master.setR1("TB");
				master.setTbDate(CommonUtil.getToday());
				master.setTbOperId(userInfo.getUserID());
				master.setTbOperDate(CommonUtil.getNowTime());
				
				if(master.getMaintBillNo()!=null && !"".equals(master.getMaintBillNo())&& !"-".equals(master.getMaintBillNo())){
					MaintContractMaster maint=(MaintContractMaster) hs.get(MaintContractMaster.class, master.getMaintBillNo());
					maint.setContractStatus("TB");
					hs.save(maint);
				}
				hs.save(master);
				
				tx.commit();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","委托合同退保成功！"));
			} catch (Exception e) {
				e.printStackTrace();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","委托合同退保失败！"));
			} finally {
				if(hs != null){
					hs.close();				
				}				
			}
			
		}

		if (!errors.isEmpty()){
			this.saveErrors(request, errors);
		}

		return mapping.findForward("returnList");

	}
	/**
	 * 跳转到 维保委托合同终止
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareEndRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","维保委托合同 >> 合同终止");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		Session hs = null;
		List maintContractDetailList = null;
	
		if (id != null && !id.equals("")) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from EntrustContractMaster where billNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {
	
					List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型下拉列表
					
					// 主信息
					EntrustContractMaster master = (EntrustContractMaster) list.get(0);															
					master.setAttn(bd.getName(hs, "Loginuser","username", "userid",master.getAttn()));// 经办人
					master.setAuditOperid(userInfo.getUserName());
					
					// 甲方单位信息
					Customer companyA = (Customer) hs.get(Customer.class,master.getCompanyId());															
					// 乙方方单位信息
					Customer companyB = (Customer) hs.get(Customer.class,master.getCompanyId2());
										
					// 明细列表
					String sql = "from EntrustContractDetail where billNo = '"+id+"'";					
					query = hs.createQuery(sql);	
					maintContractDetailList = query.list();				
					for (Object object : maintContractDetailList) {
						EntrustContractDetail detail = (EntrustContractDetail) object;
						detail.setElevatorType(bd.getOptionName(detail.getElevatorType(), elevatorTypeList));
					}					
							
					master.setMaintDivision(bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision()));// 维保分部	
					master.setMaintStation(bd.getName(hs, "Storageid", "storagename", "storageid", master.getMaintStation()));// 维保站	
					master.setOperId(bd.getName("Loginuser", "username", "userid", master.getOperId()));
					master.setContractNatureOf(bd.getName_Sql("Pulldown", "pullname", "pullid", master.getContractNatureOf()));
					
					master.setEndDate(CommonUtil.getToday());
					master.setEndOperId(userInfo.getUserName());
					master.setEndOperDate(CommonUtil.getNowTime());
					
					request.setAttribute("entrustContractMasterBean", master);	
					request.setAttribute("companyA",companyA);
					request.setAttribute("companyB",companyB);
					request.setAttribute("entrustContractDetailList", maintContractDetailList);
					request.setAttribute("auditOperid",userInfo.getUserName());
					request.setAttribute("auditDate", CommonUtil.getNowTime());
							
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
		forward = mapping.findForward("entrustContractMasterEnd");
		
		return forward;
	}
	/**
	 * 合同终止
	 */
	public ActionForward toEndRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, HibernateException {
		
		ActionErrors errors = new ActionErrors();
		DynaActionForm dform = (DynaActionForm) form;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String id = (String)dform.get("billNo"); 
		String enddate = (String)dform.get("endDate"); 

		if(id != null && !id.equals("")){
			
			Session hs = null;
			Transaction tx = null;
			EntrustContractMaster master = null;				
			
			try {
				
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				
				master = (EntrustContractMaster) hs.get(EntrustContractMaster.class, id);
				master.setR1("END");
				master.setEndDate(enddate);
				master.setEndOperId(userInfo.getUserID());
				master.setEndOperDate(CommonUtil.getNowTime());
				
				if(master.getQuoteBillNo()!=null && !"".equals(master.getQuoteBillNo()) 
						&& !"-".equals(master.getQuoteBillNo())){
					EntrustContractQuoteMaster maint=(EntrustContractQuoteMaster) hs.get(EntrustContractQuoteMaster.class, master.getQuoteBillNo());
					maint.setIsEnd("Y");
					hs.save(maint);
				}

				hs.save(master);
				
				//修改维保合同的是否委托
				MaintContractMaster mcm = (MaintContractMaster) hs.get(MaintContractMaster.class, master.getMaintBillNo());
				mcm.setR2("END");
				hs.save(mcm);
				
				tx.commit();
				
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","委托合同终止成功！"));
			} catch (Exception e) {				
				e.printStackTrace();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","委托合同终止失败！"));
			} finally {
				if(hs != null){
					hs.close();				
				}				
			}
			
		}

		if (!errors.isEmpty()){
			this.saveErrors(request, errors);
		}

		return mapping.findForward("returnList");

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
		
		request.setAttribute("navigator.location","维保委托合同审核 >> 审核");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		Session hs = null;
		List maintContractDetailList = null;
	
		if (id != null && !id.equals("")) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from EntrustContractMaster where billNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {
	
					List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型下拉列表
					
					// 主信息
					EntrustContractMaster master = (EntrustContractMaster) list.get(0);															
					master.setAttn(bd.getName(hs, "Loginuser","username", "userid",master.getAttn()));// 经办人
					master.setAuditOperid(userInfo.getUserName());
					
					// 甲方单位信息
					Customer companyA = (Customer) hs.get(Customer.class,master.getCompanyId());															
					// 乙方方单位信息
					Customer companyB = (Customer) hs.get(Customer.class,master.getCompanyId2());
										
					// 明细列表
					String sql = "from EntrustContractDetail where billNo = '"+id+"'";					
					query = hs.createQuery(sql);	
					maintContractDetailList = query.list();				
					for (Object object : maintContractDetailList) {
						EntrustContractDetail detail = (EntrustContractDetail) object;
						detail.setElevatorType(bd.getOptionName(detail.getElevatorType(), elevatorTypeList));
					}					
							
					master.setMaintDivision(bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision()));// 维保分部	
					master.setMaintStation(bd.getName(hs, "Storageid", "storagename", "storageid", master.getMaintStation()));// 维保站	
					master.setOperId(bd.getName("Loginuser", "username", "userid", master.getOperId()));
					master.setContractNatureOf(bd.getName_Sql("Pulldown", "pullname", "pullid", master.getContractNatureOf()));
						
					request.setAttribute("entrustContractMasterBean", master);	
					request.setAttribute("companyA",companyA);
					request.setAttribute("companyB",companyB);
					request.setAttribute("entrustContractDetailList", maintContractDetailList);
					request.setAttribute("auditOperid",userInfo.getUserName());
					request.setAttribute("auditDate", CommonUtil.getNowTime());
							
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
		forward = mapping.findForward("entrustContractMasterAudit");
		
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

		String billNo = (String) dform.get("billNo");
		String auditRem=(String)dform.get("auditRem");
		String auditStatus=(String)dform.get("auditStatus");
		String submitType=(String)dform.get("submitType");

		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			EntrustContractMaster master=(EntrustContractMaster) hs.get(EntrustContractMaster.class, billNo.trim());
			if(master!=null){
				String hql="update EntrustContractMaster set auditOperid='"+userInfo.getUserID()+"'," +
						"auditDate='"+CommonUtil.getNowTime()+"',auditRem='"+auditRem+"',auditStatus='"+auditStatus+"'," +
						"submitType='"+submitType+"',workisdisplay=null,workisdisplay2=null" +
						" where billNo='"+billNo+"'";
				hs.createQuery(hql).executeUpdate();
				
				//修改维保合同的是否委托
				MaintContractMaster mcm = (MaintContractMaster) hs.get(MaintContractMaster.class, master.getMaintBillNo());
				mcm.setR2("Y");
				hs.save(mcm);
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
				
				forward = mapping.findForward("returnAudit");			
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

		ServeTableForm tableForm = (ServeTableForm) form;

		String billNo = tableForm.getProperty("billNo");// 流水号
		String entrustContractNo = tableForm.getProperty("entrustContractNo");// 委托合同号
		String companyName = tableForm.getProperty("companyName");// 甲方单位
		String attn = tableForm.getProperty("attn");// 经办人
		String maintDivision = tableForm.getProperty("maintDivision");// 所属维保站			
		String submitType = tableForm.getProperty("submitType");// 提交标志
		String auditStatus = tableForm.getProperty("auditStatus");// 审核状态
		String maintContractNo = tableForm.getProperty("maintContractNo");// 维保合同
		String contractNatureOf = tableForm.getProperty("contractNatureOf");// 合同性质
		String salesContractNo=tableForm.getProperty("salesContractNo");//销售合同号
		String r1=tableForm.getProperty("r1");//合同状态
		String mainmode=tableForm.getProperty("mainmode");//维保合同是否收费
		
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("维保委托合同管理");
        
		try {
			hs = HibernateUtil.getSession();

			String sql = "select e.billNo,e.entrustContractNo,e.contractSdate,e.contractEdate,"
					+" l.username,c.comname,e.submitType,e.auditStatus,"
					+" d.companyName,e.maintContractNo,e.contractNatureOf,e.r1,e.contractTotal,"
					+"(select count(md.billNo) from EntrustContractDetail md where md.billNo=e.billno) as tlnum,"
					+"isnull(cm.CompanyName,'') as companyName2 "
					+" from EntrustContractMaster e "
					+ "left join Customer d on e.companyId2 = d.companyId "
					+ "left join MaintContractMaster m on m.billno=e.Maint_BillNo "
					+ "left join Customer cm on m.companyId = cm.companyId,"
					+ "Loginuser l,Company c"
					+" where e.attn=l.userid"
					+" and e.maintDivision=c.comid";

			if (mainmode != null && !mainmode.equals("")) {
				sql += " and e.mainMode like '"+mainmode.trim()+"'";
			}
			if (billNo != null && !billNo.equals("")) {
				sql += " and e.billNo like '%"+billNo.trim()+"%'";
			}
			if (entrustContractNo != null && !entrustContractNo.equals("")) {
				sql += " and e.entrustContractNo like '%"+entrustContractNo.trim()+"%'";
			}
			if (companyName != null && !companyName.equals("")) {
				sql += " and d.companyName like '%"+companyName.trim()+"%'";
			}
			if (attn != null && !attn.equals("")) {
				sql += " and l.username like '%"+attn.trim()+"%'";
			}
			if (maintDivision != null && !maintDivision.equals("")) {
				sql += " and c.comid like '"+maintDivision.trim()+"'";
			}
			if (submitType != null && !submitType.equals("")) {
				sql += " and e.submitType like '"+submitType.trim()+"'";
			}
			if (auditStatus != null && !auditStatus.equals("")) {
				sql += " and e.auditStatus like '"+auditStatus.trim()+"'";
			}
			if (maintContractNo != null && !maintContractNo.equals("")) {
				sql += " and e.maintContractNo like '"+maintContractNo.trim()+"'";
			}
			if (contractNatureOf != null && !contractNatureOf.equals("")) {
				sql += " and e.contractNatureOf = '"+contractNatureOf.trim()+"'";
			}
			if (salesContractNo != null && !salesContractNo.equals("")) {
				sql += " and e.BillNo in (select BillNo from EntrustContractDetail where SalesContractNo like '%"+salesContractNo.trim()+"%')";
			}
			if(r1!=null && !r1.equals("")){
				sql+=" and e.r1 like '"+r1.trim()+"'";
			}

			sql += " order by billno asc";
			
			System.out.println(sql);

			ResultSet rs=hs.connection().prepareStatement(sql).executeQuery();

			String[] titlename={"流水号","合同状态","委托合同号","维保合同号","合同性质",
					"甲方名称（维保合同）","乙方单位","合同开始日期","合同结束日期","台量","委托金额","经办人","所属部门","提交标志","审核状态"};
			String[] titleid={"billNo","r1","entrustContractNo","maintContractNo","contractNatureOf",
					"companyName2","companyName","contractSdate","contractEdate","tlnum","contractTotal",
					"username","comname","submitType","auditStatus"};
	        
			//创建单元格样式
	        XSSFCellStyle cs = wb.createCellStyle();
	        cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);//左右居中
	        cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
	        XSSFFont f  = wb.createFont();
	        f.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 字体加粗
	        cs.setFont(f);
	        
	        //创建标题
	        XSSFRow row0 = sheet.createRow( 0);
	        XSSFCell cell0 = null;
	        int ttl=titlename.length;
			for(int i=0;i<ttl;i++){
				cell0 = row0.createCell((short)i);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(titlename[i]);
				cell0.setCellStyle(cs);
			}
			
			//创建内容
			XSSFRow row = null;
			XSSFCell cell =null;
			int j=1;
			while(rs.next()){
				// 创建Excel行，从0行开始
				row = sheet.createRow(j);
				int ttl2=titleid.length;
				for(int c=0;c<ttl2;c++){
				    cell = row.createCell((short)c);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				    String valstr=rs.getString(titleid[c]);
				    if(titleid[c].equals("tlnum") || titleid[c].equals("contractTotal")){
				    	cell.setCellValue(Double.parseDouble(valstr));
				    }else{
				    	if(titleid[c].equals("r1")){
				    		if("ZB".equals(valstr)){
				    			valstr="在保";
				    		}else if("TB".equals(valstr)){
				    			valstr="退保";
				    		}else if("END".equals(valstr)){
				    			valstr="合同终止";
				    		}
				    	}
				    	if(titleid[c].equals("contractNatureOf")){
				    		if("PY".equals(valstr)){
				    			valstr="平移";
				    		}else if("WT".equals(valstr)){
				    			valstr="委托";
				    		}
				    	}
				    	if(titleid[c].equals("submitType")){
				    		if("Y".equals(valstr)){
				    			valstr="已提交";
				    		}else if("N".equals(valstr)){
				    			valstr="未提交";
				    		}else if("R".equals(valstr)){
				    			valstr="驳回";
				    		}
				    	}
				    	if(titleid[c].equals("auditStatus")){
				    		if("Y".equals(valstr)){
				    			valstr="已审核";
				    		}else if("N".equals(valstr)){
				    			valstr="未审核";
				    		}
				    	}
				    	
				    	cell.setCellValue(valstr);
				    }
				}
				j++;
			}
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
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("维保委托合同管理", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}

	public void display(ActionForm form, HttpServletRequest request ,ActionErrors errors ,String flag){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		if(id==null||id=="")
		{
			id=(String) request.getAttribute("id");
		}
		
		
		Session hs = null;
		List maintContractDetailList = null;
	    ////System.out.println("id:"+id);
		if (id != null && !id.equals("")) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from EntrustContractMaster where billNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {
	
					List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型下拉列表
					
					// 主信息
					EntrustContractMaster master = (EntrustContractMaster) list.get(0);															
					master.setAttn(bd.getName(hs, "Loginuser","username", "userid",master.getAttn()));// 经办人
					if(master.getAuditOperid()!=null && !master.getAuditOperid().equals("")){
						master.setAuditOperid(bd.getName(hs, "Loginuser", "username", "userid", master.getAuditOperid()));
					}
					
					// 甲方单位信息
					Customer companyA = (Customer) hs.get(Customer.class,master.getCompanyId());															
					// 乙方方单位信息
					Customer companyB = (Customer) hs.get(Customer.class,master.getCompanyId2());
										
					// 明细列表
					String sql = "from EntrustContractDetail where billNo = '"+id+"'";					
					query = hs.createQuery(sql);	
					maintContractDetailList = query.list();				
					for (Object object : maintContractDetailList) {
						EntrustContractDetail detail = (EntrustContractDetail) object;
						detail.setElevatorType(bd.getOptionName(detail.getElevatorType(), elevatorTypeList));
					}					
									
					if("display".equals(flag)){		
						master.setOperId(bd.getName("Loginuser", "username", "userid", master.getOperId()));
						master.setMaintDivision(bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision()));// 维保分部	
						master.setMaintStation(bd.getName(hs, "Storageid", "storagename", "storageid", master.getMaintStation()));
						master.setContractNatureOf(bd.getName_Sql("Pulldown", "pullname", "pullid", master.getContractNatureOf()));
					
						master.setTbOperId(bd.getName("Loginuser", "username", "userid", master.getTbOperId()));
						master.setEndOperId(bd.getName("Loginuser", "username", "userid", master.getEndOperId()));
					} else {						
						request.setAttribute("maintDivisionName", bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision())); // 维保分部名称
						request.setAttribute("maintStationName", bd.getName(hs, "Storageid", "storagename", "storageid", master.getMaintStation())); // 维保站名称
						request.setAttribute("contractNatureOfList", bd.getPullDownList("MaintContractMaster_ContractNatureOf"));
					}
					
					request.setAttribute("entrustContractMasterBean", master);	
					request.setAttribute("companyA",companyA);
					request.setAttribute("companyB",companyB);
					request.setAttribute("entrustContractDetailList", maintContractDetailList);
					request.setAttribute("contractNatureOfName", bd.getOptionName(master.getContractNatureOf(), bd.getPullDownList("MaintContractMaster_ContractNatureOf")));		
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
		EntrustContractMaster master = null;
		EntrustContractDetail detail = null;
		String id = (String) dform.get("id");
		if(id==null || "".equals(id))
			id=request.getParameter("id1");
		////System.out.println(id);
		String submitType=(String)dform.get("submitType");
		String billNo = null;

		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			boolean flag = false;// 是否生成维保合同号标志
			
			
			if (id != null && !id.equals("")) { // 修改		
				hs.createQuery("delete from EntrustContractDetail where billNo='"+id+"'").executeUpdate();		
				master = (EntrustContractMaster) hs.get(EntrustContractMaster.class, id);
				billNo = master.getBillNo();
				flag = !master.getContractNatureOf().equals(dform.get("contractNatureOf"));
				
			} else { // 新增
				master = new EntrustContractMaster();	
				String todayDate = CommonUtil.getToday();
				String year = todayDate.substring(2,4);
				billNo = CommonUtil.getBillno(year,"EntrustContractMaster", 1)[0];// 生成流水号		
				id=billNo;
				flag = true;
			}
			
			BeanUtils.populate(master, dform.getMap()); // 复制所有属性值
			
			master.setBillNo(billNo);// 流水号
			master.setAttn(userInfo.getUserID());// 经办人id
			master.setOperId(userInfo.getUserID());// 录入人
			master.setOperDate(CommonUtil.getNowTime());// 录入时间
			master.setSubmitType(submitType);// 提交标志
			master.setAuditStatus("N");// 审核状态
//			master.setContractNatureOf("ZB");// 合同性质
			if(flag==true){ // 生成维保合同号
				master.setEntrustContractNo(this.genEntrustContractNo((String)dform.get("contractNatureOf")));
			}
			master.setR1("ZB");//在保
			hs.save(master);

			// 合同明细
			String details = (String) dform.get("maintContractDetails");
			List list = JSONUtil.jsonToList(details, "details");
			List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型列表
			for (Object object : list) {
				detail = new EntrustContractDetail();
				BeanUtils.populate(detail, (Map)object);
				detail.setEntrustContractMaster(master);
				detail.setElevatorType(bd.getOptionId(detail.getElevatorType(), elevatorTypeList));//把电梯类型名称转换为代码				
				hs.save(detail);				
			}
		
			tx.commit();

			request.setAttribute("id", id);
			dform.set("id", id);
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
			EntrustContractMaster master = null;				
			
			try {
				
				hs = HibernateUtil.getSession();		
				tx = hs.beginTransaction();
				
				master = (EntrustContractMaster) hs.get(EntrustContractMaster.class, id);
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
	
	/**
	 * 生成维保合同号
	 * @param SalesContractNo
	 * @param ContractNatureOf
	 * @param MainMode
	 * @param MaintDivision
	 * @return
	 */
	public String genEntrustContractNo(String mainMode){
		
		String entrustContractNo = "";
		String prefix = "";
		String suffix = "";
		String year = CommonUtil.getNowTime("yyyy");
		
		prefix = year+"-";
		if(mainMode.equals("PY")){
			suffix="P";
		}
		entrustContractNo = CommonUtil.genNo("EntrustContractMaster", "entrustContractNo", prefix, suffix, 3,"entrustContractNo");
						
		return entrustContractNo;		
	}
	
}	