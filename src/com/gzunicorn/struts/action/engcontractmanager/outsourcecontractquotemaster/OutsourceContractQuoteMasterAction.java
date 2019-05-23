package com.gzunicorn.struts.action.engcontractmanager.outsourcecontractquotemaster;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.JSONUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SqlBeanUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.hibernate.basedata.customer.Customer;

import com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster.EntrustContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractquotedetail.EntrustContractQuoteDetail;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractquotemaster.EntrustContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.lostelevatorreport.LostElevatorReport;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.outsourcecontractquotedetail.OutsourceContractQuoteDetail;
import com.gzunicorn.hibernate.engcontractmanager.outsourcecontractquotemaster.OutsourceContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.outsourcecontractquoteprocess.OutsourceContractQuoteProcess;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractdetail.ServicingContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractmaster.ServicingContractMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.workflow.bo.JbpmExtBridge;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class OutsourceContractQuoteMasterAction extends DispatchAction {

	Log log = LogFactory.getLog(OutsourceContractQuoteMasterAction.class);
	
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
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String name = request.getParameter("method");
		
		if(!"toDisplayRecord".equals(name)){
			/** **********开始用户权限过滤*********** */
			SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "outsourcecontractquotemaster", null);
			/** **********结束用户权限过滤*********** */
		}

		// Set default method is toSearchRecord
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
		
		request.setAttribute("navigator.location","维改委托报价管理 >> 查询列表");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "outsourceContractQuoteList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fOutsourceContractQuote");
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
			String companyName = tableForm.getProperty("companyName");//
			String status = tableForm.getProperty("status");// 
			String submitType = tableForm.getProperty("submitType");// 提交标志
			String maintDivision=tableForm.getProperty("maintDivision");
			String maintContractNo=tableForm.getProperty("maintContractNo");

			//第一次进入页面时根据登陆人初始化所属维保分部
			List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
			if(maintDivision == null || maintDivision.equals("")){
				Map map = (Map)maintDivisionList.get(0);
				maintDivision = (String)map.get("grcid");
			}
			
			Session hs = null;
			SQLQuery query = null;
			try {
				//查询维保到期合同   根据合同结束日期提前3个月提醒
				/*String today=DateUtil.getNowTime("yyyy-MM-dd");
				String datestr=DateUtil.getDate(today, "MM", 3);
				tableForm.setProperty("hiddatestr",datestr);*/
				/*String roleid=userInfo.getRoleID();
				String maintStation="%";*/
				//A03  维保经理  只能看自己维保站下面的合同
				/*if(roleid!=null && roleid.trim().equals("A03")){
					maintStation=userInfo.getStorageId();
				}*/

				hs = HibernateUtil.getSession();

				String sql = "select a.*,isnull(b.companyName,'') companyName,c.username from"+
				" OutsourceContractQuoteMaster a left outer join Customer b on a.companyId=b.companyId,Loginuser c "+
				"where a.operId=c.userid";
				
				if (billNo != null && !billNo.equals("")) {
					sql += " and a.billNo like '%"+billNo.trim()+"%'";
				}
				if (companyName != null && !companyName.equals("")) {
					sql += " and b.companyName like '%"+companyName.trim()+"%'";
				}
				if(maintContractNo!=null && !"".equals(maintContractNo)){
					sql+=" and b.maintContractNo like '%"+maintContractNo.trim()+"%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and a.maintDivision like '"+maintDivision.trim()+"'";
				}
				if (status != null && !status.equals("")) {
					sql += " and a.status like '%"+status.trim()+"%'";
				}
				if (submitType != null && !submitType.equals("")) {
					sql += " and a.submitType like '"+submitType.trim()+"'";
				}
				
				if (table.getIsAscending()) {
					sql += " order by a."+table.getSortColumn()+ " desc";
				} else {
					sql += " order by a."+table.getSortColumn()+ " asc";
				}
				
				query = hs.createSQLQuery(sql);
				query.addEntity("a",OutsourceContractQuoteMaster.class);
				query.addScalar("companyName");
				query.addScalar("username");
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List outsourceContractQuoteList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					OutsourceContractQuoteMaster master = (OutsourceContractQuoteMaster) objs[2];
					//master.setCompanyId(bd.getName_Sql("Customer", "companyName", "companyId", master.getCompanyId()));
					master.setCompanyId(objs[0].toString());
					master.setOperId(objs[1].toString());
					master.setR1(bd.getName_Sql("ViewFlowStatus", "typename", "typeid", String.valueOf(master.getStatus())));
					master.setMaintDivision(bd.getName("Company","comname","comid",master.getMaintDivision()));
					outsourceContractQuoteList.add(master);
				}

				table.addAll(outsourceContractQuoteList);
				session.setAttribute("outsourceContractQuoteList", table);
				// 流程状态下拉框列表
				request.setAttribute("processStatusList", bd.getProcessStatusList());
				/*// 获取流程名称
				request.setAttribute("flowname", WorkFlowConfig.getProcessDefine("entrustcontractquotemasterProcessName"));*/
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
			forward = mapping.findForward("outsourceContractQuoteList");
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
		
		request.setAttribute("navigator.location","维改合同 >> 查询列表 ");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "outsourceContractQuoteNextList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fOutsourceContractQuoteNext");
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
			
			String billNo = tableForm.getProperty("billNo");// 报价流水号
			String maintContractNo=tableForm.getProperty("maintContractNo");//
			String attn = tableForm.getProperty("attn");// 
			String busType=tableForm.getProperty("busType");
			String maintDivision = tableForm.getProperty("maintDivision");// 甲方单位id
						
			//第一次进入页面时根据登陆人初始化所属维保分部
			List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
			if(maintDivision == null || maintDivision.equals("")){
				Map map = (Map)maintDivisionList.get(0);
				maintDivision = (String)map.get("grcid");
			}
			
			Session hs = null;
			Query query = null;
			try {
				/*String roleid=userInfo.getRoleID();
				String maintStation="%";
				//A03  维保经理  只能看自己维保站下面的合同
				if(roleid!=null && roleid.trim().equals("A03")){
					maintStation=userInfo.getStorageId();
				}
				*/
				hs = HibernateUtil.getSession();

				String sql = "select s,l.username from ServicingContractMaster s,Loginuser l where l.userid=s.attn and s.auditStatus='Y' "
                    		+ " and s.wgBillno not in (select wgBillno from OutsourceContractMaster) ";
				//+" and billNo not in (select isnull(quoteBillNo,'') from MaintContractMaster)";
				
				if (billNo != null && !billNo.equals("")) {
					sql += " and s.billNo like '%"+billNo.trim()+"%'";
				}
				if(maintContractNo!=null && !maintContractNo.equals("")){
					sql+=" and s.maintContractNo like '%"+maintContractNo.trim()+"%'";
				}
				if (attn != null && !attn.equals("")) {
					sql += " and l.username like '%"+attn.trim()+"%'";
				}
				if(busType!=null && !"".equals(busType)){
					sql+=" and s.busType like '%"+busType.trim()+"%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and s.maintDivision like '"+maintDivision.trim()+"'";
				}				
				if (table.getIsAscending()) {
					sql += " order by s."+ table.getSortColumn() +" asc";
				} else {
					sql += " order by s."+ table.getSortColumn() +" desc";
				}
				
				query = hs.createQuery(sql);
				//System.out.println(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List outsourceContractQuoteNextList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					ServicingContractMaster master=(ServicingContractMaster)objs[0];
					master.setAttn(objs[1].toString());
					master.setMaintDivision(bd.getName(hs, "Company", "comfullname", "comid", master.getMaintDivision()));
					outsourceContractQuoteNextList.add(master);
				}

				table.addAll(outsourceContractQuoteNextList);
				session.setAttribute("outsourceContractQuoteNextList", table);
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
			forward = mapping.findForward("outsourceContractQuoteNextList");
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
		
		request.setAttribute("navigator.location","维改委托报价管理 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		
		//String isOpen=request.getParameter("isOpen");
		
		display(form, request, errors, "display");
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		request.setAttribute("display", "yes");
		//request.setAttribute("isOpen", isOpen);
		
		String typejsp= request.getParameter("typejsp");
		if(typejsp!=null){
			request.setAttribute("typejsp", typejsp);
		}
		
		forward = mapping.findForward("outsourceContractQuoteDisplay");
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

		request.setAttribute("navigator.location","维改委托报价管理 >> 添加");
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}
		String billNo=request.getParameter("billNo");
		ServicingContractMaster master=null;
		OutsourceContractQuoteMaster quote=null;
		List detailList=null;
		
		Session hs = null;
		Query query = null;
		try {
			hs = HibernateUtil.getSession();
			if(billNo!=null && !"".equals(billNo)){
				quote=new OutsourceContractQuoteMaster();
				quote.setOperDate(CommonUtil.getNowTime());
				quote.setR1(userInfo.getUserName());
				quote.setOperId(userInfo.getUserID());
				
				master=(ServicingContractMaster) hs.get(ServicingContractMaster.class, billNo.trim());
				if(master!=null){
					quote.setWgBillno(master.getWgBillno());
					quote.setMaintContractNo(master.getMaintContractNo());
					quote.setMaintDivision(master.getMaintDivision());
					quote.setMaintStation(master.getMaintStation());
					
					master.setMaintDivision(bd.getName(hs, "Company", "comfullname", "comid", master.getMaintDivision()));
					master.setMaintStation(bd.getName("Storageid", "storagename", "storageid", master.getMaintStation()));
					
					String hql="select a from ServicingContractDetail a where a.servicingContractMaster.wgBillno='"+master.getWgBillno()+"'";
					detailList=hs.createQuery(hql).list();
				}
			}
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (HibernateException e1) {
			e1.printStackTrace();
		} /*catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} */ /*catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		
		request.setAttribute("quoteBean", quote);
		request.setAttribute("contractBean", master);
		request.setAttribute("detailList", detailList);
		saveToken(request); //生成令牌，防止表单重复提交
		
		return mapping.findForward("outsourceContractQuoteAdd");
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

		//防止表单重复提交
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
		
		request.setAttribute("navigator.location","维改委托报价管理  >> 修改");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		display(form, request, errors, "");
		
		//request.setAttribute("causeAnalysisList", bd.getPullDownList("LostElevatorReport_CauseAnalysis"));// 签署方式下拉框列表	

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
			
		forward = mapping.findForward("outsourceContractQuoteModify");
		
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
	
	/**
	 * 提交方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws HibernateException
	 */
	public ActionForward toReferRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, HibernateException {
		
		ActionErrors errors = new ActionErrors();
		refer(form,request,errors,request.getParameter("id")); //提交

		if (!errors.isEmpty()){
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.submitToAudit.failed")); //提示“提交审核失败！”
		} else {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.submitToAudit.succeed")); //提示“提交审核成功！”
		}
		
		if (!errors.isEmpty()){
			this.saveErrors(request, errors);
		}

		return mapping.findForward("returnList");

	}
	

	/**
	 * 删除记录
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
			OutsourceContractQuoteMaster master = (OutsourceContractQuoteMaster) hs.get(OutsourceContractQuoteMaster.class, id);
			if (master != null) {
				hs.createQuery("delete OutsourceContractQuoteDetail where billNo='"+master.getBillNo()+"'").executeUpdate();
				hs.createQuery("delete OutsourceContractQuoteProcess where billNo='"+master.getBillNo()+"'").executeUpdate();
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
		/*if(id==null || "".equals(id)){
			id=(String) dform.get("id");
		}*/
		if(id==null || "".equals(id)){
			id=(String) request.getAttribute("id");
		}
		
		Session hs = null;
		OutsourceContractQuoteMaster master=null;
		ServicingContractMaster maint=null;
		ServicingContractDetail detail=null;
		Customer customer=null;
		List detailList=new ArrayList();
	
		if (id != null && !id.equals("")) {			
			try {
				hs = HibernateUtil.getSession();
				master=(OutsourceContractQuoteMaster) hs.get(OutsourceContractQuoteMaster.class, id.trim());
				if (master!=null) {
					master.setR1(bd.getName("Loginuser","username","userid",master.getOperId()));
					
					customer=(Customer) hs.get(Customer.class, master.getCompanyId());
					
					maint=(ServicingContractMaster) hs.get(ServicingContractMaster.class, master.getWgBillno());
					if(maint!=null){
						maint.setMaintDivision(bd.getName("Company","comfullname","comid",maint.getMaintDivision()));
						maint.setMaintStation(bd.getName("Storageid", "storagename", "storageid", maint.getMaintStation()));
					}
					
					String hql="select wgRowid from OutsourceContractQuoteDetail where billNo='"+master.getBillNo()+"'";
					List list=hs.createQuery(hql).list();
					if(list!=null && list.size()>0){
						for(int i=0;i<list.size();i++){
							detail=(ServicingContractDetail) hs.get(ServicingContractDetail.class, Integer.valueOf(list.get(i).toString()));
							detailList.add(detail);
						}
					}
					//审批流程信息
					List processApproveList = hs.createQuery("from OutsourceContractQuoteProcess where billNo = '"+ master.getBillNo() + "' order by itemId").list();
					
					for (Object object : processApproveList) {
						OutsourceContractQuoteProcess process = (OutsourceContractQuoteProcess) object;
						process.setUserId(bd.getName(hs, "Loginuser", "username", "userid", process.getUserId()));
					}
					request.setAttribute("processApproveList",processApproveList);
					
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
				request.setAttribute("quoteBean", master);
				request.setAttribute("contractBean", maint);
				request.setAttribute("detailList", detailList);
				request.setAttribute("customer", customer);
	
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
		OutsourceContractQuoteMaster master = null;
		OutsourceContractQuoteDetail detail=null;
		String id = (String) dform.get("id");
		String billNo = null,submitType=null;
		String[] maintRowid=request.getParameterValues("maintRowid");

		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			
			if (id != null && !id.equals("")) { // 修改		
				master = (OutsourceContractQuoteMaster) hs.get(OutsourceContractQuoteMaster.class, id);
				billNo = master.getBillNo();
				submitType=master.getSubmitType();
//				hs.createQuery("delete from LostElevatorReport where billNo='"+id+"'").executeUpdate();		
				//System.out.println(billNo);
			} else { // 新增
				master = new OutsourceContractQuoteMaster();	
				
				String todayDate = CommonUtil.getToday();
				String year = todayDate.substring(2,4);
				billNo = CommonUtil.getBillno(year,"OutsourceContractQuoteMaster", 1)[0];// 生成流水号		
				submitType="N";
			}
			
			BeanUtils.populate(master, dform.getMap()); // 复制所有属性值
//			dform.set("id", billNo);
			request.setAttribute("id", billNo);
			master.setBillNo(billNo);
			master.setOperId(userInfo.getUserID());// 录入人
			master.setOperDate(CommonUtil.getNowTime());// 录入时间
			master.setSubmitType(submitType);
			master.setStatus(new Integer(WorkFlowConfig.State_NoStart));
			
			hs.save(master);
			if(maintRowid!=null && maintRowid.length>0){
				List list=hs.createQuery("from OutsourceContractQuoteDetail where billNo='"+master.getBillNo()+"'").list();
				if(list!=null && list.size()>0){
					hs.createQuery("delete OutsourceContractQuoteDetail where billNo='"+master.getBillNo()+"'").executeUpdate();
				}
				for(int i=0;i<maintRowid.length;i++){
					detail=new OutsourceContractQuoteDetail();
					detail.setBillNo(master.getBillNo());
					detail.setWgRowid(Integer.valueOf(maintRowid[i]));
					hs.save(detail);
				}
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
				String isreturn = request.getParameter("isreturn");
				if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
					refer(form,request,errors,billNo); //提交
				}
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}
		
	}
		
	public void refer(ActionForm form, HttpServletRequest request,ActionErrors errors, String id){

		HttpSession httpsession = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)httpsession.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		if(id != null && !id.equals("")){
			
			Session hs = null;
			Transaction tx = null;
			JbpmExtBridge jbpmExtBridge=null;
			String userid = userInfo.getUserID();
			OutsourceContractQuoteMaster master = null;				
			
			try {
				
				hs = HibernateUtil.getSession();		
				tx = hs.beginTransaction();
				if(id!=null && !"".equals(id)){
					master = (OutsourceContractQuoteMaster) hs.get(OutsourceContractQuoteMaster.class, id);
					if(!"Y".equals(master.getSubmitType())){
						//tx = hs.beginTransaction();
						
						ServicingContractMaster service=(ServicingContractMaster) hs.get(ServicingContractMaster.class, master.getWgBillno());
						
						String processDefineID = Grcnamelist1.getProcessDefineID("outsourcecontractquotemaster", master.getOperId());// 流程环节id
						if(processDefineID == null || processDefineID.equals("")){
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("error.string","<font color='red'>未配置审批流程信息，不能启动！</font>"));
							throw new Exception();
						}
									
						/**=============== 启动新流程实例开始 ===================**/
						HashMap paraMap = new HashMap();
						jbpmExtBridge=new JbpmExtBridge();
						ProcessBean pd = null;		
						pd = jbpmExtBridge.getPb();
		
						//Grcnamelist1.setJbpmAuditopers(pd, processDefineID, "维保分部长审核", master.getMaintDivision());
						Grcnamelist1.setJbpmAuditopers_roleid(pd,"Y",master.getMaintDivision());
						
						pd=jbpmExtBridge.startProcess(WorkFlowConfig.getProcessDefine(processDefineID),userid,userid,id,"",paraMap);
						/**==================== 流程结束 =======================**/
						
						master.setSubmitType("Y");// 提交标志
						master.setProcessName("维保分部长审核");// 环节名称
						master.setStatus(pd.getStatus()); //流程状态
						master.setTokenId(pd.getToken());//流程令牌
						hs.save(master);
						
						tx.commit();
					}
				}
			} catch (Exception e) {	
				try {
					tx.rollback();
				} catch (HibernateException e3) {
					e3.printStackTrace();
				}
				if (jbpmExtBridge != null) {
					jbpmExtBridge.setRollBack();
				}
				e.printStackTrace();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("maintContract.recordnotfounterror"));
			} finally {
				if(hs != null){
					hs.close();				
				}
				if(jbpmExtBridge!=null){
					jbpmExtBridge.close();
				}
			}
			
		}		
	} 
	
}	