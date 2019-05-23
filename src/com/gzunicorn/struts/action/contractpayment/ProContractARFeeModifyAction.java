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
import com.gzunicorn.hibernate.contractpaymentmanage.contractpayablesmaster.ContractPayablesMaster;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractdetail.EntrustContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster.EntrustContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractmaster.ServicingContractMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.hibernate.viewmanager.compact_view.CompactView;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class ProContractARFeeModifyAction extends DispatchAction {

	Log log = LogFactory.getLog(ProContractARFeeModifyAction.class);
	
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
		
		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "proContractARFeeModify", null);
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
		
		request.setAttribute("navigator.location","应收款修改>> 查询列表");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "proContractArfeeModifyList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fProContractARFeeModify");
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
				"v.maintDivision,v.notInvoice,v.submitType,v.auditStatus,v.isInvoice,v.maintStation," +
				"v.isParagraph,v.notParagraph,v.warnRem,p.preMoney "+
				" from ViewProContractARFeeMaster v,Customer c,ProContractArfeeMaster p" +
				" where v.companyId=c.companyId and v.jnlNo=p.jnlNo and v.submitType='Y' and v.auditStatus='Y'";
				
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
					master.put("notInvoice", df.format(objs[7]));
					master.put("submitType", objs[8]);
					master.put("auditStatus", objs[9]);
					master.put("isInvoice", objs[10]);
					if(objs[11]!=null && !"".equals(objs[11]))
						master.put("maintStation", bd.getName("Storageid", "storagename", "storageid",objs[11].toString()));
					else
						master.put("maintStation", objs[11]);
					master.put("isParagraph", objs[12]);
					master.put("notParagraph", df.format(objs[13]));
					master.put("warnRem",objs[14]);
					master.put("preMoney",objs[15]);
					
					proContractArfeeMasterList.add(master);
				}

				table.addAll(proContractArfeeMasterList);
				session.setAttribute("proContractArfeeModifyList", table);
				
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
			forward = mapping.findForward("proContractArfeeModifyList");
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
		
		request.setAttribute("navigator.location","应收款修改 >> 修改");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		
		display(form, request, errors, "display");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		forward = mapping.findForward("proContractArfeeModify");
		
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
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);

		String jnlNo = (String)dform.get("jnlNo");
		String preDate = (String)dform.get("preDate");
		Double preMoney = (Double)dform.get("preMoney");
		String maintScope = (String)dform.get("maintScope");
		String companyId = (String)dform.get("companyId");
		
		if (jnlNo != null && !jnlNo.equals("")) {
			Session hs = null;
			Transaction tx = null;
			try {

				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();

				//应收款
				ProContractArfeeMaster master = (ProContractArfeeMaster) hs.get(ProContractArfeeMaster.class, jnlNo);
				master.setCompanyId(companyId);
				master.setMaintScope(maintScope);
				master.setOldPreMoney(master.getPreMoney());
				master.setOldPreDate(master.getPreDate());
				master.setPreMoney(preMoney);
				master.setPreDate(preDate);
				master.setModifyId(userInfo.getUserID());
				master.setModifyDate(CommonUtil.getNowTime());
				hs.save(master);
				
				//合同开票管理
				String upsql="update ContractInvoiceManage set CompanyID='"+companyId+"' where ARF_JnlNo='"+jnlNo+"'";
				hs.connection().prepareStatement(upsql).executeUpdate();
				
				//合同来款管理
				upsql="update ContractParagraphManage set CompanyID='"+companyId+"' where ARF_JnlNo='"+jnlNo+"'";
				hs.connection().prepareStatement(upsql).executeUpdate();

				tx.commit();

				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
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
						//master.setCompanyId(bd.getName_Sql("Customer", "companyName", "companyId", master.getCompanyId()));
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
					request.setAttribute("companyName", bd.getName_Sql("Customer", "companyName", "companyId", master.getCompanyId()));
					request.setAttribute("proContractArfeeMasterBean", master);	
					
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}

				//应收款名称
				//request.setAttribute("receivablesList", this.getReceivablesNameList());
	
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
	 * 删除
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

			//来款删除
			String sqldel0="delete ContractParagraphManage where ARF_JnlNo='"+id+"'";
			//开票删除
			String sqldel1="delete ContractInvoiceManage where ARF_JnlNo='"+id+"'";
			//应收款删除
			String sqldel2="delete ProContractArfeeMaster where JnlNo='"+id+"'";
			
			hs.connection().prepareStatement(sqldel0).executeUpdate();
			hs.connection().prepareStatement(sqldel1).executeUpdate();
			hs.connection().prepareStatement(sqldel2).executeUpdate();
			
			tx.commit();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.succeed"));
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		}finally {
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
}	