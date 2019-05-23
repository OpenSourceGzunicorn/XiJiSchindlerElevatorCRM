package com.gzunicorn.struts.action.engcontractmanager.maintcontractdelay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.commons.beanutils.BeanUtils;
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
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdelaydetail.MaintContractDelayDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdelaymaster.MaintContractDelayMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdelayprocess.MaintContractDelayProcess;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.infomanager.elevatorarchivesinfo.ElevatorArchivesInfo;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class MaintContractDelayAuditAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintContractDelayAuditAction.class);

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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "maintcontractdelayaudit", null);
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
		
		request.setAttribute("navigator.location","维保合同延保审核 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "maintContractDelayAuditList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fMaintContractDelayAudit");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		cache.updateTable(table);
		table.setSortColumn("jnlno");
		table.setIsAscending(false);
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
		//String submitType = tableForm.getProperty("submitType");// 提交标志
		//String status = tableForm.getProperty("status");// 审核状态
		String auditStatus = tableForm.getProperty("auditStatus");// 审核状态

		if(auditStatus == null || auditStatus.equals("")){
			auditStatus = "N";
			tableForm.setProperty("auditStatus", "N");
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
			
			String[] colNames = {
					"a.jnlno as jnlno",
					"a.billno as billno",
					"f.maintContractNo as maintContractNo",
					"f.contractSdate as contractSdate",
					"f.contractEdate as contractEdate",
					"b.username as operName",
					"a.submitType as submitType",
					"a.auditStatus as auditStatus",
					/*"a.processName as processName",	
					"d.typename as statusName",*/								
					"e.storagename as maintStationName",
					"c.comname as maintDivisionName",	
					"a.status as status",
					"a.tokenId as tokenId",
					"g.companyName as companyName"
				};
			
			String sql = "select "+StringUtils.join(colNames, ",")+
					" from MaintContractDelayMaster a,Loginuser b,Company c," +/*ViewFlowStatus d,*/
					" Storageid e,MaintContractMaster f,Customer g" + 
					" where a.operId = b.userid"+
					" and b.grcid = c.comid"+
					" and f.companyId = g.companyId"+
					" and a.maintStation = e.storageid"+
					" and a.billno = f.billNo"+
					" and a.submitType = 'Y'";
			
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
				sql += " and maintDivision like '"+maintDivision.trim()+"'";
			}
			if(auditStatus!=null && !auditStatus.equals("")){
				sql+=" and a.auditStatus like '"+auditStatus.trim()+"'";
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
			List maintContractDelayAuditList = new ArrayList();
			for (Object object : list) {
				Object[] objs = (Object[])object;
				Map<String,String> map = new HashMap<String,String>();
				for(int i=0; i<colNames.length; i++){
					map.put(colNames[i].split(" as ")[1].trim(), String.valueOf(objs[i]));
				}
				maintContractDelayAuditList.add(map);
			}

			table.addAll(maintContractDelayAuditList);
			session.setAttribute("maintContractDelayAuditList", table);

			// 流程状态下拉框列表
			request.setAttribute("processStatusList", bd.getProcessStatusList());
			// 分部下拉框列表
			request.setAttribute("maintDivisionList", maintDivisionList);
			// 维保站下拉框列表
			request.setAttribute("maintStationList", bd.getMaintStationList(userInfo.getComID()));			
			// 获取流程名称
			//request.setAttribute("flowname", WorkFlowConfig.getProcessDefine("enginequotemasterProcessName"));

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
			
		
		return mapping.findForward("toList");
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
		
		request.setAttribute("navigator.location","维保合同延保审核 >> 查看");
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
				String upsql="update MaintContractDelayMaster set workisdisplay2='"+workisdisplay+"' where jnlno='"+id+"'";
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
	 * 审核方法
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
			throws IOException, ServletException, HibernateException {
		
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		
/*		if(isTokenValid(request, true)){*/
			
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			DynaActionForm dform = (DynaActionForm) form;				
			String id = request.getParameter("id");
			String auditStatus = String.valueOf(dform.get("auditStatus")); // 审核状态
			String submitType = "N".equals(auditStatus) ? "R" : "Y"; // 是否驳回
			String auditRem = String.valueOf(dform.get("auditRem")); // 审核意见
			String[] rowids = request.getParameterValues("rowid"); // 合同电梯明细行号
			if(id != null && !id.equals("")){
				
				Session hs = null;
				Transaction tx = null;
				Query query = null;
				
				MaintContractDelayMaster master = null;						
				try {
					
					hs = HibernateUtil.getSession();		
					tx = hs.beginTransaction();
	
					master=(MaintContractDelayMaster) hs.get(MaintContractDelayMaster.class, id);
					master.setSubmitType(submitType); // 提交标志
					master.setWorkisdisplay(null);
					master.setWorkisdisplay2(null);
					if("Y".equals(submitType)){
						String auditdate=CommonUtil.getNowTime();
						master.setAuditStatus(auditStatus); // 审核状态
						master.setAuditOperid(userInfo.getUserID()); // 审核人
						master.setAuditDate(auditdate); // 审核时间
						master.setAuditRem(auditRem); // 审核意见
						List<MaintContractDelayDetail> list=hs.createQuery("from MaintContractDelayDetail where jnlno='"+master.getJnlno()+"'").list();
						if(list!=null && list.size()>0){
							for(MaintContractDelayDetail detail : list){
								hs.createQuery("update MaintContractDetail set "
										//+ "mainEdate='"+detail.getDelayEdate()+"',"//不覆盖维保结束日期
										+ "delayEDate='"+detail.getDelayEdate()+"' "
										+ "where rowid='"+detail.getRowid()+"'").executeUpdate();
							}
						}
					}
					
					hs.save(master);
					
					tx.commit();
				} catch (Exception e) {				
					e.printStackTrace();
					tx.rollback();
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("maintContract.recordnotfounterror"));
				} finally {
					if(hs != null){
						hs.close();				
					}				
				}
				
			} else {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("maintContract.recordnotfounterror"));
			}	
			
			if(errors.isEmpty()){			
				if("R".equals(submitType)){
					//提示“合同驳回成功!”
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("contract.toreback.success")); 
				}else{
					//生成作业计划
					for (String rowid : rowids) {
						CommonUtil.toMaintenanceWorkPlan(rowid, null, userInfo, errors);
					}
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("auditing.succeed")); //提示“审核成功！”
				}
			}
		
		/*}else{
			saveToken(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("navigator.submit.error"));
		}*/
		
		if (!errors.isEmpty()){
			this.saveErrors(request, errors);
		}

		return mapping.findForward("returnList");

	}
	
	// 重启流程功能
	public ActionForward toReStartProcess(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ActionErrors errors = new ActionErrors();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) request.getSession().getAttribute(SysConfig.LOGIN_USER_INFO);

		String id = request.getParameter("id");
		
		Session hs = null;
		Transaction tx = null;
		MaintContractDelayMaster master = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				
				master = (MaintContractDelayMaster) hs.get(MaintContractDelayMaster.class, id);
				
				if (master != null && (master.getStatus() == 1 || master.getStatus() == 2)) {// 流程状态为终止或通过的时候

					// 保存主信息
					master.setSubmitType("N"); //提交标志
					master.setStatus(new Integer(WorkFlowConfig.State_NoStart)); // 流程状态,未启动
					master.setTokenId(new Long(0));// 流程令牌
					master.setProcessName("");// 环节名称
					hs.save(master);
					
					// 保存审批流程相关信息
					MaintContractDelayProcess process = new MaintContractDelayProcess();
					process.setJnlno(master.getJnlno());//报价流水号
					process.setTaskId(new Integer(0));//任务号
					process.setTaskName("重启流程");//任务名称
					process.setTokenId(new Long(0));//流程令牌
					process.setUserId(userInfo.getUserID());//操作人
					process.setDate1(CommonUtil.getToday());//操作日期
					process.setTime1(CommonUtil.getTodayTime());//操作时间
					process.setApproveResult("重启流程");
					process.setApproveRem("重新启动流程");
					hs.save(process);
					
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
				
				tx.commit();
			} catch (Exception e1) {
				e1.printStackTrace();		
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.reStartProcess.failed"));// 重启流程失败
				try{
					tx.rollback();
				} catch (HibernateException e2){
					e2.printStackTrace();
				}
				
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
		}

		if (errors.isEmpty()) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.reStartProcess.succeed"));// 重启流程成功
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return mapping.findForward("returnList");
	}
	
	
	public void display(ActionForm form, HttpServletRequest request ,ActionErrors errors ,String flag){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
		String id = request.getParameter("id");

		String billNo = null;
		Session hs = null;
		List maintContractDetailList = null;

		try {
			hs = HibernateUtil.getSession();
			//维保合同延保主表
			MaintContractDelayMaster delayMaster = (MaintContractDelayMaster) hs.get(MaintContractDelayMaster.class, id);
			if(delayMaster.getAuditStatus()==null || delayMaster.getAuditStatus().equals("N")){
				delayMaster.setAuditDate(CommonUtil.getNowTime());
				delayMaster.setAuditOperid(userInfo.getUserName());
				
			}else{
				delayMaster.setAuditOperid(bd.getName(hs, "Loginuser","username", "userid",delayMaster.getAuditOperid()));
			}
			request.setAttribute("delayrem", delayMaster.getRem());
			
			String jnlno = delayMaster.getJnlno(); //延保流水号
			billNo = delayMaster != null ? delayMaster.getBillno() : id;// 维保合同管理流水号

			Query query = hs.createQuery("from MaintContractMaster where billNo = '"+billNo+"'");
			List list = query.list();
			if (list != null && list.size() > 0) {
				// 延保主信息
				request.setAttribute("delayMaster", delayMaster);

				// 维保合同管理主信息
				MaintContractMaster master = (MaintContractMaster) list.get(0);															
				master.setAttn(bd.getName(hs, "Loginuser","username", "userid",master.getAttn()));// 经办人
				master.setAuditOperid(bd.getName(hs, "Loginuser", "username", "userid",master.getAuditOperid()));// 审核人
				master.setTaskUserId(bd.getName(hs, "Loginuser", "username", "userid",master.getTaskUserId()));// 下达人	
				master.setMaintDivision(bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision()));// 维保分部														
				master.setOperId(bd.getName(hs, "Loginuser","username", "userid",master.getOperId()));
				request.setAttribute("maintStationName", bd.getName(hs, "Storageid", "storagename", "storageid", master.getMaintStation()));//所属维保站
				
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
					sql = "select a,b.delayEdate from MaintContractDetail a,MaintContractDelayDetail b" +
							" where a.rowid = b.rowid" +
							" and b.jnlno = '"+delayMaster.getJnlno()+"'" + 
							" and a.assignedMainStation like '"+delayMaster.getMaintStation()+"'";
				}
				
				if(delayMaster != null){
					request.setAttribute("username", bd.getName(hs, "Loginuser","username", "userid",delayMaster.getOperId()));
					request.setAttribute("daytime", delayMaster.getOperDate());
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
				
				/*//审批流程信息
				query = hs.createQuery("from MaintContractDelayProcess where jnlno = '"+ jnlno + "' order by itemId");
				List processApproveList = query.list();
				for (Object object : processApproveList) {
					MaintContractDelayProcess process = (MaintContractDelayProcess) object;
					process.setUserId(bd.getName(hs, "Loginuser", "username", "userid", process.getUserId()));
				}
				request.setAttribute("processApproveList",processApproveList);*/
				
				request.setAttribute("delayBean", delayMaster);
				request.setAttribute("maintContractBean", master);
				request.setAttribute("companyA",companyA);
				request.setAttribute("companyB",companyB);
				request.setAttribute("maintContractDetailList", maintContractDetailList);
				request.setAttribute("maintStationList", bd.getMaintStationList(userInfo.getComID()));// 维保站下拉列表
				// 获取流程名称
				request.setAttribute("flowname", WorkFlowConfig.getProcessDefine("enginequotemasterProcessName"));
				
						
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
