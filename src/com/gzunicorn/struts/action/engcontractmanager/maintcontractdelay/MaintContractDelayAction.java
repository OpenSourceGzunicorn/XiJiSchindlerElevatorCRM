package com.gzunicorn.struts.action.engcontractmanager.maintcontractdelay;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.hibernate.basedata.customer.Customer;

import com.gzunicorn.hibernate.engcontractmanager.maintcontractdelaydetail.MaintContractDelayDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdelaymaster.MaintContractDelayMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdelayprocess.MaintContractDelayProcess;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.workflow.bo.JbpmExtBridge;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class MaintContractDelayAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintContractDelayAction.class);
	
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

		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "maintcontractdelay", null);
		/** **********结束用户权限过滤*********** */

		// Set default method is toSearchRecord
		String name = request.getParameter("method");
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
		
		request.setAttribute("navigator.location","维保合同延保管理 >> 查询列表");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "maintContractDelayList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fMaintContractDelay");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("jnlno");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String jnlno = tableForm.getProperty("jnlno");// 流水号
			String maintContractNo = tableForm.getProperty("maintContractNo");// 维保合同号
			String companyName = tableForm.getProperty("companyName");// 甲方单位
			String operId = tableForm.getProperty("operId");// 录入人
			String maintDivision = tableForm.getProperty("maintDivision");// 所属分部	
			String submitType = tableForm.getProperty("submitType");// 提交标志
			//String status = tableForm.getProperty("status");// 审核状态
			String auditStatus = tableForm.getProperty("auditStatus");// 审核状态

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
				
				String maintStation="%";
				//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
				String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
				List vmlist=hs.createSQLQuery(sqlss).list();
				if(vmlist!=null && vmlist.size()>0){
					maintStation=userInfo.getStorageId();
				}
				
				String[] colNames = {
						"a.jnlno as jnlno",
						"a.billno as billno",
						"f.maintContractNo as maintContractNo",
						"f.contractSdate as contractSdate",
						"f.contractEdate as contractEdate",
						"b.username as operName",
						"a.submitType as submitType",
						"a.auditStatus as auditStatus",
//						"a.processName as processName",	
//						"d.typename as statusName",								
						"e.storagename as maintStationName",
						"c.comname as maintDivisionName",
						"g.companyName as companyName"
					};
				
				String sql = "select "+StringUtils.join(colNames, ",")+
						" from MaintContractDelayMaster a,Loginuser b,Company c," +/*ViewFlowStatus d,*/
						" Storageid e,MaintContractMaster f,Customer g" + 
						" where a.operId = b.userid"+
						" and f.maintDivision = c.comid"+
						" and f.companyId = g.companyId"+
						" and a.maintStation = e.storageid"+
						" and a.billno = f.billNo"+
						" and f.MaintStation like '"+maintStation+"'";
				
				if (jnlno != null && !jnlno.equals("")) {
					sql += " and jnlno like '%"+jnlno.trim()+"%'";
				}
				if (maintContractNo != null && !maintContractNo.equals("")) {
					sql += " and maintContractNo like '%"+maintContractNo.trim()+"%'";
				}
				if (companyName != null && !companyName.equals("")) {
					sql += " and companyName like '%"+companyName.trim()+"%'";
				}
				if (operId != null && !operId.equals("")) {
					sql += " and username like '%"+operId.trim()+"%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and f.maintDivision like '"+maintDivision.trim()+"'";
				}
				if (submitType != null && !submitType.equals("")) {
					sql += " and a.submitType like '"+submitType.trim()+"'";
				}
				/*if (status != null && !status.equals("")) {
					sql += " and status like '"+status.trim()+"'";
				}*/
				if(auditStatus!=null && !auditStatus.equals("")){
					sql+=" and a.auditStatus like '"+auditStatus.trim()+"'";
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
				List maintContractDelayList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map<String,String> map = new HashMap<String,String>();
					for(int i=0; i<colNames.length; i++){
						map.put(colNames[i].split(" as ")[1].trim(), String.valueOf(objs[i]));
					}
					maintContractDelayList.add(map);
				}

				table.addAll(maintContractDelayList);
				session.setAttribute("maintContractDelayList", table);

				// 流程状态下拉框列表
				request.setAttribute("processStatusList", bd.getProcessStatusList());
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);
				// 维保站下拉框列表
				request.setAttribute("maintStationList", bd.getMaintStationList(userInfo.getComID()));// 维保站下拉列表

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
		return mapping.findForward("toList");
	}
	
	/**
	 * 点击新建进入的查询界面方法
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
		
		request.setAttribute("navigator.location","维保合同管理 >> 查询列表 ");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "maintContractDelayNextList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fMaintContractDelayNext");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		cache.updateTable(table);
		table.setSortColumn("contractEdate");
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
		String maintContractNo = tableForm.getProperty("maintContractNo");// 维保合同号
		String maintDivision = tableForm.getProperty("maintDivision");// 所属维保站		
		String companyID = tableForm.getProperty("companyID");//甲方单位	
		String projectName = tableForm.getProperty("projectName");// 项目名称
		String salesContractNo = tableForm.getProperty("salesContractNo");//销售合同号

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
			
			String maintStation="%";
			//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
			String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
			List vmlist=hs.createSQLQuery(sqlss).list();
			if(vmlist!=null && vmlist.size()>0){
				maintStation=userInfo.getStorageId();
			}
			
			String sql = "select a,b.username as attn,c.comname as maintDivision,s.storagename,ct.companyName"+
					" from MaintContractMaster a,Loginuser b,Company c,Storageid s,Customer ct " + 
					" where a.attn = b.userid and a.companyId=ct.companyId " +
					" and a.maintDivision = c.comid" +
					" and a.contractStatus in ('ZB','XB')" + 
					" and a.auditStatus = 'Y'" +
					" and a.taskSubFlag = 'Y'" +
					" and a.maintStation like '"+maintStation+"'" +
					" and a.maintStation=s.storageid ";
			
			if (billNo != null && !billNo.equals("")) {
				sql += " and a.billNo like '%"+billNo.trim()+"%'";
			}
			if (maintContractNo != null && !maintContractNo.equals("")) {
				sql += " and a.maintContractNo like '%"+maintContractNo.trim()+"%'";
			}
			if (companyID != null && !companyID.equals("")) {
				sql += " and ct.companyName like '%"+companyID.trim()+"%'";
			}
			if (maintDivision != null && !maintDivision.equals("")) {
				sql += " and a.maintDivision like '"+maintDivision.trim()+"'";
			}
			
			if (salesContractNo != null && !salesContractNo.equals("")) {
				sql += " and a.billNo in(select distinct billNo from MaintContractDetail where salesContractNo like '%"+salesContractNo.trim()+"%')";
			}
			if (projectName != null && !projectName.equals("")) {
				sql += " and a.billNo in(select distinct billNo from MaintContractDetail where projectName like '%"+projectName.trim()+"%')";
			}
			
			if (table.getIsAscending()) {
				sql += " order by a."+ table.getSortColumn() +" asc";
			} else {
				sql += " order by a."+ table.getSortColumn() +" desc";
			}
			
			//System.out.println(">>>"+sql);
			
			query = hs.createQuery(sql);
			table.setVolume(query.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List list = query.list();
			List maintContractDelayNextList = new ArrayList();
			for (Object object : list) {
				Object[] objs = (Object[])object;
				MaintContractMaster master = (MaintContractMaster) objs[0];
				master.setAttn(String.valueOf(objs[1]));
				master.setMaintDivision(String.valueOf(objs[2]));	
				master.setMaintStation(String.valueOf(objs[3]));
				master.setCompanyId(String.valueOf(objs[4]));
				maintContractDelayNextList.add(master);
			}

			table.addAll(maintContractDelayNextList);
			session.setAttribute("maintContractDelayNextList", table);
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
		forward = mapping.findForward("toNextList");
		
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
		
		request.setAttribute("navigator.location","维保合同延保管理 >> 查看");
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
				String upsql="update MaintContractDelayMaster set workisdisplay='"+workisdisplay+"' where jnlno='"+id+"'";
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

		forward = mapping.findForward("toDisplay");
		return forward;
	}
	
	/**
	 * 跳转到新建页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toPrepareAddRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","维保合同延保管理 >> 新建");	
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		
		display(form, request, errors, "delay");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		dform.set("id",request.getParameter("id"));
		request.setAttribute("doType", "delay");//延保
		saveToken(request); //生成令牌，防止表单重复提交
		
		forward = mapping.findForward("toAdd");	
		return forward;
	}
	
	/**
	 * 新建方法
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
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;

		String jnlnos = ""; //延保流水号
		
		//防止表单重复提交
		if(!isTokenValid(request, true)){
			saveToken(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("navigator.submit.error"));
		} else {					

			String maintStation = request.getParameter("maintStation");// 所属维保站
			String id = (String) dform.get("id");
			String rem = (String) dform.get("rem"); 
			String[] rowids = request.getParameterValues("rowid");// 维保管理合同明细行号
			String[] elevatorNos = request.getParameterValues("elevatorNo");// 电梯编号
			String[] realityEdates = request.getParameterValues("realityEdate");// 实际结束日期
			String[] assignedMainStations = request.getParameterValues("assignedMainStation");// 下达维保站
			
			if(id != null && !id.equals("")){
				
				Session hs = null;
				Transaction tx = null;
				Connection conn = null;
				PreparedStatement ps = null;
				
				MaintContractDelayMaster master = null;		
				MaintContractDelayDetail detail = null;				
				try {
					
					hs = HibernateUtil.getSession();		
					tx = hs.beginTransaction();


					String year = CommonUtil.getToday().substring(2,4);
					jnlnos = "YB"+CommonUtil.getBillno(year,"MaintContractDelayMaster", 1)[0];// 生成流水号	

						
					// 维保延保主信息
					master = new MaintContractDelayMaster();
					master.setJnlno(jnlnos);// 维保延保流水号
					master.setBillno(id);// 维保管理流水号
					master.setOperId(userInfo.getUserID());// 录入人
					master.setOperDate(CommonUtil.getNowTime());// 录入日期
					master.setMaintStation(maintStation);// 所属维保站
					master.setStatus(WorkFlowConfig.State_NoStart);// 流程状态
					master.setSubmitType("N");// 提交标志
					master.setAuditStatus("N");//审核状态
					master.setTokenId(new Long(0));// 流程令牌
					master.setProcessName("");// 环节名称
					master.setRem(rem);
					master.setWorkisdisplay(null);
					master.setWorkisdisplay2(null);
					master.setAuditDate("");
					master.setAuditOperid("");
					master.setAuditRem("");
					hs.save(master);
				
					// 维保延保明细
					for(int j=0; j<assignedMainStations.length; j++){
						detail = new MaintContractDelayDetail();
						detail.setJnlno(jnlnos);// 维保延保流水号
						detail.setRowid(Integer.parseInt(rowids[j]));// 维保管理明细行号
						detail.setDelayEdate(realityEdates[j]);// 延保结束日期
						hs.save(detail);
					}
					request.setAttribute("id", master.getJnlno());
					
					tx.commit();
				} catch (Exception e) {				
					e.printStackTrace();
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
					if(tx != null){
						tx.rollback();
					}
				} finally {
					if(hs != null){
						hs.close();				
					}				
				}
				
			} else {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("maintContract.recordnotfounterror"));
			}	

		}	

		String isreturn = request.getParameter("isreturn");
		
		if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
			refer(form,request,errors,jnlnos); //提交
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
			forward = mapping.findForward("returnList");
		} else {
			
			// return addnew page
			if (errors.isEmpty()) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
			} else {
				request.setAttribute("error", "Yes");
			}

			String urlstr="/"+SysConfig.WEB_APPNAME+"/maintContractDelayAction.do?id="+jnlnos
					+"&method=toPrepareUpdateRecord&issuccess=Y";
			response.sendRedirect(urlstr);
			//forward = mapping.findForward("returnModify");	
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
		
		request.setAttribute("navigator.location","维保合同延保管理 >> 修改");	
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		display(form, request, errors, "update");
		
		request.setAttribute("maintDivisionName", userInfo.getComName()); // 维保分部名称	
		
		String issuccess=request.getParameter("issuccess");
		if(issuccess!=null && issuccess.trim().equals("Y")){
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
		}
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("doType", "delay");//延保
		saveToken(request); //生成令牌，防止表单重复提交	
		
		//首页调整过来的
		String isclosework=request.getParameter("isclosework");
		if(isclosework!=null && isclosework.equals("Y")){
			request.setAttribute("isclosework", isclosework);
		}
		
		forward = mapping.findForward("toModify");
		
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
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;

		String id = (String) dform.get("id"); //维保延保流水号
		String rem=(String) dform.get("rem");
	
		//防止表单重复提交
		if(!isTokenValid(request, true)){
			saveToken(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("navigator.submit.error"));
		} else {					

			dform.set("id", id); //维保延保流水号
			String[] rowids = request.getParameterValues("rowid");// 维保管理明细行号
			String[] realityEdates = request.getParameterValues("realityEdate");// 实际结束日期
			
			if(id != null && !id.equals("")){
				
				Session hs = null;
				Transaction tx = null;
				Connection conn = null;
				PreparedStatement ps = null;
				
				MaintContractDelayMaster master = null;		
//				MaintContractDelayDetail detail = null;				
				try {
					
					hs = HibernateUtil.getSession();		
					tx = hs.beginTransaction();
					
					master = (MaintContractDelayMaster) hs.get(MaintContractDelayMaster.class, id);
					master.setRem(rem);
					master.setOperId(userInfo.getUserID());// 录入人
					master.setOperDate(CommonUtil.getNowTime());// 录入日期
					master.setWorkisdisplay(null);
					master.setWorkisdisplay2(null);
					master.setAuditDate("");
					master.setAuditOperid("");
					master.setAuditRem("");
					
					if(master != null ){
						// 维保延保合同明细					
						String sql = "update MaintContractDelayDetail set delayEdate = ? from MaintContractDelayDetail where rowid=?";									
						conn = hs.connection();
						ps = conn.prepareStatement(sql);	
						for(int i = 0; i < rowids.length; i++){	
							ps.setString(1, realityEdates[i]);
							ps.setString(2, rowids[i]);
							ps.addBatch();
						}
						ps.executeBatch();	
					}
					hs.save(master);

					tx.commit();
					
				} catch (Exception e) {				
					e.printStackTrace();
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
					if(tx != null){
						tx.rollback();
					}
				} finally {
					if(hs != null){
						hs.close();				
					}				
				}
				
			} else {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("maintContract.recordnotfounterror"));
			}	

		}

		try {
			String isreturn = request.getParameter("isreturn");					
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				refer(form, request, errors, id);
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
			MaintContractDelayMaster master = (MaintContractDelayMaster) hs.get(MaintContractDelayMaster.class, id);
			if (master != null && "N".equals(master.getSubmitType())) {
				hs.createQuery("delete from MaintContractDelayDetail where jnlno='"+id+"'").executeUpdate();
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
		if(id==null || "".equals(id)){
			id=(String) request.getAttribute("id");
		}

		String billNo = null;
		Session hs = null;
		List maintContractDetailList = null;

		try {
			hs = HibernateUtil.getSession();
			//维保合同延保主表
			MaintContractDelayMaster delayMaster = (MaintContractDelayMaster) hs.get(MaintContractDelayMaster.class, id);
			
			billNo = delayMaster != null ? delayMaster.getBillno() : id;// 维保合同管理流水号
			

			Query query = hs.createQuery("from MaintContractMaster where billNo = '"+billNo+"'");
			List list = query.list();
			if (list != null && list.size() > 0) {

				// 维保合同管理主信息
				MaintContractMaster master = (MaintContractMaster) list.get(0);															
				master.setAttn(bd.getName(hs, "Loginuser","username", "userid",master.getAttn()));// 经办人
				master.setAuditOperid(bd.getName(hs, "Loginuser", "username", "userid",master.getAuditOperid()));// 审核人
				master.setTaskUserId(bd.getName(hs, "Loginuser", "username", "userid",master.getTaskUserId()));// 下达人	
				master.setMaintDivision(bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision()));// 维保分部													
				master.setOperId(bd.getName(hs, "Loginuser","username", "userid",master.getOperId()));
				request.setAttribute("maintStationName", bd.getName(hs, "Storageid", "storagename", "storageid", master.getMaintStation()));//所属维保站
				
				if(delayMaster != null){
					request.setAttribute("username", bd.getName(hs, "Loginuser","username", "userid",delayMaster.getOperId()));
					request.setAttribute("daytime", delayMaster.getOperDate());
					request.setAttribute("delayrem", delayMaster.getRem());
					dform.set("rem", delayMaster.getRem());
				}else{
					String username=userInfo.getUserName();
					String daytime=CommonUtil.getNowTime();
					request.setAttribute("username", username);
					request.setAttribute("daytime", daytime);
					request.setAttribute("delayrem", "");
				}
				
				//付款方式
				String pmastr=master.getPaymentMethod();
				List pdlist=bd.getPullDownAllList("MaintContractQuoteMaster_PaymentMethodApply");
				String pmaname=bd.getOptionName(pmastr, pdlist);
				master.setR4(pmaname);
				//maintContractBean.setPaymentMethod(pmastr);//付款方式
				//合同附件内容申请
				String ccastrname="";
				String ccastr=master.getContractTerms();
				if(ccastr!=null && !ccastr.trim().equals("")){
					List ccalist=bd.getPullDownAllList("MaintContractQuoteMaster_ContractContentApply");
					String[] ccarr=ccastr.split(",");
					for(int i=0;i<ccarr.length;i++){
						if(i==ccarr.length-1){
							ccastrname+=bd.getOptionName(ccarr[i].trim(), ccalist);
						}else{
							ccastrname+=bd.getOptionName(ccarr[i].trim(), ccalist)+"，";
						}
					}
				}
				master.setR5(ccastrname);
				
				// 甲方单位信息
				Customer companyA = (Customer) hs.get(Customer.class,master.getCompanyId());															
				// 乙方方单位信息
				Customer companyB = (Customer) hs.get(Customer.class,master.getCompanyId2());
									
				// 维保延保管理明细列表
				String sql = "select a,a.realityEdate from MaintContractDetail a where a.billNo = '"+billNo+"'";
				if(delayMaster != null){
					delayMaster.setAuditOperid(bd.getName(hs, "Loginuser","username", "userid",delayMaster.getAuditOperid()));
					sql = "select a,b.delayEdate from MaintContractDetail a,MaintContractDelayDetail b" +
							" where a.rowid = b.rowid" +
							" and b.jnlno = '"+delayMaster.getJnlno()+"'" + 
							" and a.assignedMainStation like '"+delayMaster.getMaintStation()+"'";
				}
				
				query = hs.createQuery(sql);	
				list = query.list();
				maintContractDetailList = new ArrayList();
				List signWayList = bd.getPullDownList("MaintContractDetail_SignWay");// 签署方式下拉列表
				List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型下拉列表
				for (Object object : list) {
					Object[] objs = (Object[]) object;
					MaintContractDetail detail = (MaintContractDetail) objs[0];
					detail.setRealityEdate((String) objs[1]);
					detail.setSignWay(bd.getOptionName(detail.getSignWay(), signWayList));
					detail.setElevatorType(bd.getOptionName(detail.getElevatorType(), elevatorTypeList));
					maintContractDetailList.add(detail);
				}
				
				/*if(delayMaster != null){
					//审批流程信息
					query = hs.createQuery("from MaintContractDelayProcess where jnlno = '"+ delayMaster.getJnlno() + "' order by itemId");
					List processApproveList = query.list();
					for (Object object : processApproveList) {
						MaintContractDelayProcess process = (MaintContractDelayProcess) object;
						process.setUserId(bd.getName(hs, "Loginuser", "username", "userid", process.getUserId()));
					}
					request.setAttribute("processApproveList",processApproveList);
				}*/
				if(delayMaster==null){
					delayMaster=new MaintContractDelayMaster();
					delayMaster.setAuditStatus("N");
					delayMaster.setAuditDate("");
					delayMaster.setAuditOperid("");
					delayMaster.setAuditRem("");
				}

				request.setAttribute("delayBean", delayMaster);
				request.setAttribute("maintContractBean", master);	
				request.setAttribute("companyA",companyA);
				request.setAttribute("companyB",companyB);
				request.setAttribute("maintContractDetailList", maintContractDetailList);
				request.setAttribute("maintStationList", bd.getMaintStationList(userInfo.getComID()));// 维保站下拉列表
						
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

		
	public void refer(ActionForm form, HttpServletRequest request,ActionErrors errors, String id){

		HttpSession httpsession = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)httpsession.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		if(id != null && !id.equals("")){

			Session hs = null;
			Transaction tx = null;
			JbpmExtBridge jbpmExtBridge=null;
			String userid = userInfo.getUserID(); //当前登录用户id
			MaintContractDelayMaster master = null;
			
			try {
				hs = HibernateUtil.getSession();
										
				master = (MaintContractDelayMaster) hs.get(MaintContractDelayMaster.class, id);
				
				if(!"Y".equals(master.getSubmitType())){
					tx = hs.beginTransaction();
					
					/*String processDefineID = Grcnamelist1.getProcessDefineID("maintcontractdelaymaster", master.getOperId());// 流程环节id
					if(processDefineID == null || processDefineID.equals("")){
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("error.string","<font color='red'>未配置审批流程信息，不能启动！</font>"));
						throw new Exception();
					}
								
					//**=============== 启动新流程实例开始 ===================**//*
					HashMap paraMap = new HashMap();
					jbpmExtBridge=new JbpmExtBridge();
					ProcessBean pd = null;		
					pd = jbpmExtBridge.getPb();
					
					Grcnamelist1.setJbpmAuditopers(pd, processDefineID, PropertiesUtil.getProperty("MaintStationManagerJbpm"), userInfo.getComID(), master.getMaintStation());// 添加审核人
					
					pd=jbpmExtBridge.startProcess(WorkFlowConfig.getProcessDefine(processDefineID),userid,userid,id,"",paraMap);
					//**==================== 流程结束 =======================**/
					
					master.setSubmitType("Y");// 提交标志
					/*master.setProcessName(pd.getNodename());// 环节名称
					master.setStatus(pd.getStatus()); //流程状态
					master.setTokenId(pd.getToken());//流程令牌*/
					hs.save(master);
					
					tx.commit();
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
			} finally {
				hs.close();
				if(jbpmExtBridge!=null){
					jbpmExtBridge.close();
				}
				
			}
			
		}		
	} 
	
	/**
	 * ajax 级联 分部与维保站
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public void toAjaxDeleteRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		Session hs=null;
		Transaction tx = null;
		String jnlnostr=request.getParameter("jnlnostr");

		String isdele="Y";
		try {
			hs=HibernateUtil.getSession();
			if(jnlnostr!=null && !"".equals(jnlnostr)){
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				
				MaintContractDelayMaster master = (MaintContractDelayMaster) hs.get(MaintContractDelayMaster.class, jnlnostr);
				if (master != null) {
					hs.createQuery("delete from MaintContractDelayDetail where jnlno='"+jnlnostr+"'").executeUpdate();
					hs.delete(master);
				}
				
				tx.commit();
			}
		} catch (Exception e) {
			isdele="N";
			e.printStackTrace();
		} finally{
			hs.close();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='GBK'?>");
		sb.append("<root>");
		sb.append("<rows>");
		sb.append("<cols name='isdele' value='"+isdele+"'>").append("</cols>");
		sb.append("</rows>");
		sb.append("</root>");

		response.setHeader("Content-Type","text/html; charset=GBK");
		response.setCharacterEncoding("gbk"); 
		response.setContentType("text/xml;charset=gbk");
		response.getWriter().write(sb.toString());
	}
	
}	