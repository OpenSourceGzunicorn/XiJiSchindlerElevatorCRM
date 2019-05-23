package com.gzunicorn.struts.action.contractpaymentmanage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
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

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.dao.DBInterface;
import com.gzunicorn.common.dao.ObjectAchieveFactory;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.contractpaymentmanage.contractpaymentmanage.ContractPaymentManage;
import com.gzunicorn.hibernate.contractpaymentmanage.contractpaymentprocess.ContractPaymentProcess;
import com.gzunicorn.hibernate.contractpaymentmanage.contractticketmanage.ContractTicketManage;

import com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster.EntrustContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotedetail.MaintContractQuoteDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquoteprocess.MaintContractQuoteProcess;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.workflow.bo.JbpmExtBridge;

public class PaymentManageJbpmAction extends DispatchAction {

	Log log = LogFactory.getLog(PaymentManageJbpmAction.class);
	
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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "myTaskOA", null);
		/** **********结束用户权限过滤*********** */
		ActionForward forward = super.execute(mapping, form, request,response);
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
	 * 准备审批，注意检查是否已有审批内容，或是否已审
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toPrepareApprove(ActionMapping mapping,
		ActionForm form, HttpServletRequest request,
		HttpServletResponse response) throws IOException, ServletException {

		request.setAttribute("navigator.location", "付款审核 >> 审 批");

		ActionErrors errors = new ActionErrors();

		display(form, request, errors); //查看方法
		
		request.setAttribute("approve", "Y");
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return mapping.findForward("toApprove");
	}
	
	/**
	 * 保存提交审批内容
	 * 此方法将扩展为：保存并提交、或保存不提交，根据提交参数区分
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toSaveApprove(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();

		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) request.getSession().getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;

		String flowname = (String) dform.get("flowname");// 流程名称
		//if (flowname != null && !flowname.trim().equals("")) {
		//	flowname = new String(flowname.getBytes("ISO-8859-1"), "gbk");
		//}
		
		String jnlNo = (String) dform.get("jnlNo");
		String taskname = (String) dform.get("taskname");// 任务名称
		Long taskid = (Long) dform.get("taskid");// 任务名称
		String approveresult = (String) dform.get("approveresult");// 审批结果
		String approverem = (String) dform.get("approverem");// 审批意见
			
		Session hs = null;
		Transaction tx = null;
		JbpmExtBridge jbpmExtBridge = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			if (jnlNo != null && !jnlNo.trim().equals("")) {
				
				ContractPaymentManage master = (ContractPaymentManage) hs.get(ContractPaymentManage.class, jnlNo.trim());
				
				String processDefineID = Grcnamelist1.getProcessDefineID("contractpaymentmanage", master.getOperId());// 流程环节id
				
				/*=============== 流程审批启动开始 =================*/
				jbpmExtBridge = new JbpmExtBridge();
				HashMap paraMap = new HashMap();
				ProcessBean pd = jbpmExtBridge.getProcessBeanUseTI(Long.parseLong(dform.get("taskid").toString()));
				
				pd.addAppointActors("");// 将动态添加的审核人清除
				if("旧件退回情况审核".equals(taskname)){
					//Grcnamelist1.setJbpmAuditopers(pd, processDefineID, "分部长审核", master.getMaintDivision());
					Grcnamelist1.setJbpmAuditopers_roleid(pd,"Y",master.getMaintDivision());
				}else if(taskname.equals("分部长审核")){
					Double debitMoney=(Double)dform.get("debitMoney");
					if(debitMoney>0){
						pd.setSelpath("Y");
						//Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "总部长审核", "%");// 添加审核人
						Grcnamelist1.setJbpmAuditopers_roleid(pd,"N","");
					}else{
						pd.setSelpath("N");//提交关闭流程
						pd.addAppointActors(master.getOperId());
					}
				}else if(taskname.equals("总部长审核")){
					if(approveresult.equals("不通过")){
						//Grcnamelist1.setJbpmAuditopers(pd, processDefineID, "分部长审核", master.getMaintDivision());
						Grcnamelist1.setJbpmAuditopers_roleid(pd,"Y",master.getMaintDivision());
					}else{
						pd.addAppointActors(master.getOperId());//提交关闭流程			
					}
				}else{
					Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, approveresult.replace("提交", ""), "%");	
				}
				
				pd = jbpmExtBridge.goToNext(taskid, approveresult, userInfo.getUserID(), paraMap);// 审核
				/*=============== 流程审批启动结束 =================*/
				
				// 保存主信息
				if(taskname.equals("保养单审核人审核")){
					String bydAuditDate=(String) dform.get("bydAuditDate");
					String bydAuditEvaluate=(String) dform.get("bydAuditEvaluate");
					String bydAuditRem=(String)dform.get("bydAuditRem");
					master.setBydAuditDate(bydAuditDate);
					master.setBydAuditEvaluate(bydAuditEvaluate);
					master.setBydAuditRem(bydAuditRem);
				}
				
				if(taskname.equals("例行回访结果审核")){
					String hfAuditDate=(String)dform.get("hfAuditDate");
					Integer hfAuditNum=(Integer)dform.get("hfAuditNum");
					Integer hfAuditNum2=(Integer)dform.get("hfAuditNum2");
					String hfAuditRem=(String)dform.get("hfAuditRem");
					master.setHfAuditDate(hfAuditDate);
					master.setHfAuditNum(hfAuditNum);
					master.setHfAuditNum2(hfAuditNum2);
					master.setHfAuditRem(hfAuditRem);
				}
				
				if(taskname.equals("热线管理人审核")){
					String rxAuditDate=(String)dform.get("rxAuditDate");
					Integer rxAuditNum=(Integer)dform.get("rxAuditNum");
					Integer rxAuditNum2=(Integer)dform.get("rxAuditNum2");
					String rxAuditRem=(String)dform.get("rxAuditRem");
					master.setRxAuditDate(rxAuditDate);
					master.setRxAuditNum(rxAuditNum);
					master.setRxAuditNum2(rxAuditNum2);
					master.setRxAuditRem(rxAuditRem);
				}
				
				if(taskname.equals("旧件退回情况审核")){
					String jjthAuditDate=(String)dform.get("jjthAuditDate");
					String jjthAuditEvaluate=(String)dform.get("jjthAuditEvaluate");
					String jjthAuditRem=(String)dform.get("jjthAuditRem");
					master.setJjthAuditDate(jjthAuditDate);
					master.setJjthAuditEvaluate(jjthAuditEvaluate);
					master.setJjthAuditRem(jjthAuditRem);
				}
				
				if(taskname.equals("分部长审核")){
					String fbzAuditDate=(String)dform.get("fbzAuditDate");
					String fbzAuditEvaluate=(String)dform.get("fbzAuditEvaluate");
					Double debitMoney=(Double)dform.get("debitMoney");
					String fbzAuditRem=(String)dform.get("fbzAuditRem");
					master.setFbzAuditDate(fbzAuditDate);
					master.setFbzAuditEvaluate(fbzAuditEvaluate);
					master.setDebitMoney(debitMoney);
					master.setFbzAuditRem(fbzAuditRem);
				}
				
				if(taskname.equals("总部长审核")){
					String zbzAuditDate=(String)dform.get("zbzAuditDate");
					String zbzAuditRem=(String)dform.get("zbzAuditRem");
					master.setZbzAuditDate(zbzAuditDate);
					master.setZbzAuditRem(zbzAuditRem);
				}
				
				master.setTokenId(pd.getToken());
				master.setStatus(pd.getStatus());
				master.setProcessName(pd.getNodename());			
				hs.save(master);

				// 保存审批流程相关信息
				ContractPaymentProcess process = new ContractPaymentProcess();
				process.setJnlNo(jnlNo);
				process.setTaskId(pd.getTaskid().intValue());// 任务号
				process.setTaskName(taskname);// 任务名称
				process.setTokenId(pd.getToken());// 流程令牌
				process.setUserId(userInfo.getUserID());
				process.setDate1(CommonUtil.getToday());
				process.setTime1(CommonUtil.getTodayTime());
				process.setApproveResult(approveresult);
				process.setApproveRem(approverem);
				hs.save(process);

				tx.commit();
			}

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			if (jbpmExtBridge != null) {
				jbpmExtBridge.setRollBack();
			}
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("application.submit.fail"));
			e.printStackTrace();
		} finally {
			if (jbpmExtBridge != null) {
				jbpmExtBridge.close();
			}
			if (hs != null) {
				hs.close();
			}
		}

		if (errors.isEmpty()) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("application.submit.sucess"));
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("display", "Y");
		forward = mapping.findForward("returnApprove");
		return forward;
	}
	
	
	/**
	 * 页面查看内容
	 * @param form
	 * @param request
	 * @param errors
	 * @throws UnsupportedEncodingException
	 */
	public void display(ActionForm form, HttpServletRequest request,ActionErrors errors) throws UnsupportedEncodingException{
		
		DynaActionForm dform = (DynaActionForm) form;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String tokenid = request.getParameter("tokenid");// 流程令牌
		if (tokenid == null) {
			tokenid = (String) dform.get("tokenid");
		}
		String taskid = request.getParameter("taskid");// 任务id
		if (taskid == null) {
			taskid = (String) dform.get("taskid");
		}
		String taskname = request.getParameter("taskname");// 当前任务名称
		if (taskname == null) {
			taskname = (String) dform.get("taskname");
		}else{
			taskname = new String(taskname.getBytes("ISO-8859-1"), "gbk");
		}
		String tasktype = request.getParameter("tasktype");// 任务类型
		if (tasktype == null) {
			tasktype = (String) dform.get("tasktype");
		}

		Session hs = null;
		ContractPaymentManage master = null;
		List maintContractQuoteDetailList = null;
		String billNo="";
		Integer status = null;
		
		if (tokenid != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from ContractPaymentManage where tokenId = '"+ tokenid + "'");
				List list = query.list();
				if (list != null && list.size() > 0) {

					master = (ContractPaymentManage) list.get(0);
					billNo=master.getJnlNo();
					ContractTicketManage ctm=(ContractTicketManage) hs.get(ContractTicketManage.class, master.getCtJnlNo());
					EntrustContractMaster ecm=new EntrustContractMaster();
					//System.out.println(master.getBillNo());
					String sql="from EntrustContractMaster where billno='"+master.getBillNo()+"'";
					List list1=hs.createQuery(sql).list();
					if(list1!=null && list1.size()>0){
						ecm=(EntrustContractMaster) list1.get(0);
					}
					request.setAttribute("contractTotal", ecm.getContractTotal());
					double contractTotal =ecm.getContractTotal();
					double invoiceMoney =ctm.getInvoiceMoney();			
					String hql="select isnull(sum(paragraphMoney),0) from ContractPaymentManage where entrustContractNo='"+ecm.getEntrustContractNo()+"'";
					List list2=hs.createSQLQuery(hql).list();
					double invoiceReceivables=0.0;
					if(list2!=null&&list2.size()>0)
					{
						invoiceReceivables=Double.valueOf(list2.get(0).toString());
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
//					//System.out.println(taskname);
					if(taskname.equals("保养单审核人审核"))
						master.setBydAuditDate(CommonUtil.getToday());
					if(taskname.equals("例行回访结果审核"))
						master.setHfAuditDate(CommonUtil.getToday());
					if(taskname.equals("热线管理人审核"))
						master.setRxAuditDate(CommonUtil.getToday());
					if(taskname.equals("旧件退回情况审核"))
						master.setJjthAuditDate(CommonUtil.getToday());
					if(taskname.equals("分部长审核"))
						master.setFbzAuditDate(CommonUtil.getToday());
					if(taskname.equals("总部长审核"))
						master.setZbzAuditDate(CommonUtil.getToday());
					
					request.setAttribute("paymentManageBean", master);	
					request.setAttribute("ctjnlNo", ctm.getJnlNo());
					request.setAttribute("invoiceNo", ctm.getInvoiceNo());
					request.setAttribute("invoiceType", bd.getName(hs, "InvoiceType", "inTypeName", "inTypeId", ctm.getInvoiceType()));
					request.setAttribute("invoiceReceivables", invoiceReceivables);
					request.setAttribute("noInvoiceReceivables", contractTotal-invoiceReceivables);		
					request.setAttribute("builtReceivables", builtReceivables);
					request.setAttribute("noBuiltReceivables", invoiceMoney-builtReceivables);	
					request.setAttribute("invoiceMoney", invoiceMoney);
					
					//审批流程信息
					query = hs.createQuery("from ContractPaymentProcess where jnlNo = '"+ billNo + "' order by itemId");
					List processApproveList = query.list();
					for (Object object : processApproveList) {
						ContractPaymentProcess process = (ContractPaymentProcess) object;
						process.setUserId(bd.getName(hs, "Loginuser", "username", "userid", process.getUserId()));
					}
					request.setAttribute("processApproveList",processApproveList);
					
					//审核下拉框内容
					List tranList=this.getTransition(hs.connection(),3,null,Long.parseLong(taskid));
					request.setAttribute("ResultList",tranList);
					
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
			
			if(request.getAttribute("error") == null || request.getAttribute("error").equals(""))//准备进入审批页面
			{
				dform.set("tokenid",new Long(tokenid));
				dform.set("taskid",new Long(taskid));
				request.setAttribute("taskname", taskname);
				dform.set("taskname",taskname);
				dform.set("flowname", WorkFlowConfig.getProcessDefine("contractpaymentmanageProcessName"));
				dform.set("status", status);
				dform.set("billNo",billNo);
				dform.set("id",billNo);				
				dform.set("tasktype",tasktype);
			}
		
		} 
	}
	
	/**
	 * Type 0:根据流程定义，node start找; 1:根据task id找;2:根据node id 找,3:TaskInstance
	 * @param type
	 * @param process	流程
	 * @param tasknode  task/node
	 * @return
	 * @throws SQLException 
	 */
	public List getTransition(Connection con,int type,String process,long tasknode) throws SQLException{
		DBInterface db=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
		db.setCon(con);
		String sql="Sp_JbpmGetTransition "+type+",'"+process+"',"+tasknode;
		//System.out.println(sql);
		return db.queryToList(sql);
		
	}
	
}
