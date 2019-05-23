package com.gzunicorn.struts.action.engcontractmanager.outsourcecontractquotemaster;

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

import com.gzunicorn.hibernate.engcontractmanager.entrustcontractquotedetail.EntrustContractQuoteDetail;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractquotemaster.EntrustContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractquoteprocess.EntrustContractQuoteProcess;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotedetail.MaintContractQuoteDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquoteprocess.MaintContractQuoteProcess;
import com.gzunicorn.hibernate.engcontractmanager.outsourcecontractquotedetail.OutsourceContractQuoteDetail;
import com.gzunicorn.hibernate.engcontractmanager.outsourcecontractquotemaster.OutsourceContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.outsourcecontractquoteprocess.OutsourceContractQuoteProcess;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractdetail.ServicingContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractmaster.ServicingContractMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.workflow.bo.JbpmExtBridge;

public class OutsourceContractQuoteJbpmAction extends DispatchAction {

	Log log = LogFactory.getLog(OutsourceContractQuoteJbpmAction.class);
	
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
	 * 进入修改界面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward  toPrepareUpdateApprove(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response)  throws IOException, ServletException{

		request.setAttribute("navigator.location", "维改报价申请表审核 >> 修  改");
		ActionErrors errors = new ActionErrors();
		
		display(form, request, errors);

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return mapping.findForward("toApproveModify");
	}
	/**
	 * 保存修改
	 * page or modifiy page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward toSaveUpdateApprove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		HttpSession session = request.getSession();
		DynaActionForm dform = (DynaActionForm)form;
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ActionErrors errors = new ActionErrors();
		
		Session hs = null;
		Transaction tx = null;
		
		String approveresult=(String)dform.get("approveresult");
		String taskname=(String)dform.get("taskname");
		String billNo=(String)dform.get("billNo");

		String flowname=(String) dform.get("flowname");
		//if(flowname!=null && !flowname.trim().equals("")){
		//	flowname=new String(flowname.getBytes("ISO-8859-1"),"gbk");
		//}

		JbpmExtBridge jbpmExtBridge=null;
		
		try {
			
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();			

			OutsourceContractQuoteMaster master = (OutsourceContractQuoteMaster) hs.get(OutsourceContractQuoteMaster.class, billNo);
			hs.createQuery("delete from OutsourceContractQuoteDetail where billNo='"+billNo+"'").executeUpdate();//删除该流水号所有明细信息	
			
			ServicingContractMaster maint=(ServicingContractMaster) hs.get(ServicingContractMaster.class, master.getWgBillno());
			
			String operid = master.getOperId();
			String operDate = master.getOperDate();
		
			BeanUtils.copyProperties(master, dform); // 复制所有属性值
			
			String processDefineID = Grcnamelist1.getProcessDefineID("outsourcecontractquotemaster", master.getOperId());// 流程环节id

    		/**======== 流程审批启动开始 ========*/
			jbpmExtBridge=new JbpmExtBridge();
	    	ProcessBean pd=jbpmExtBridge.getProcessBeanUseTI(Long.parseLong(dform.get("taskid").toString()));
	    	
	    	pd.addAppointActors("");// 将动态添加的审核人清除。
			//Grcnamelist1.setJbpmAuditopers(pd, processDefineID, "维保分部长审核", master.getMaintDivision());
			Grcnamelist1.setJbpmAuditopers_roleid(pd,"Y",master.getMaintDivision());
			
	    	pd=jbpmExtBridge.goToNext(Long.parseLong(dform.get("taskid").toString()),approveresult,userInfo.getUserID(),null);//审核
	    	/**======== 流程审批启动结束 ========*/
	    	
	    	//保存审批流程相关信息
	    	OutsourceContractQuoteProcess process=new OutsourceContractQuoteProcess();
			process.setBillNo(billNo.trim());
			process.setTaskId(new Integer(pd.getTaskid().intValue()));//任务号
			process.setTaskName(taskname);//任务名称
			process.setTokenId(pd.getToken());//流程令牌
			process.setUserId(userInfo.getUserID());
			process.setDate1(CommonUtil.getToday());
			process.setTime1(CommonUtil.getTodayTime());
			process.setApproveResult(approveresult);
			process.setApproveRem((String)dform.get("approverem"));
			hs.save(process);
			
			//String customerArea=request.getParameter("customerArea");//客户区域

	    	//保存主信息
			BeanUtils.populate(master, dform.getMap());
			master.setBillNo(billNo);
			master.setSubmitType("Y");// 提交标志
	    	master.setTokenId(pd.getToken());
	    	master.setStatus(pd.getStatus());
	    	master.setProcessName(pd.getNodename());
			master.setOperId(operid);// 录入人
			master.setOperDate(operDate);// 录入时间
			hs.saveOrUpdate(master);
					
			// 电梯信息
			OutsourceContractQuoteDetail detail = null;
			String[] maintRowid=request.getParameterValues("maintRowid");
			if(maintRowid!=null && maintRowid.length>0){
				for(int i=0;i<maintRowid.length;i++){
					detail=new OutsourceContractQuoteDetail();
					detail.setBillNo(master.getBillNo());
					detail.setWgRowid(Integer.valueOf(maintRowid[i]));
					hs.save(detail);
				}
			}
				

			tx.commit();
	
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			if (jbpmExtBridge != null) {
				jbpmExtBridge.setRollBack();
			}
			if (tx != null) {
				tx.rollback();
			}
			e2.printStackTrace();
			log.error(e2.getMessage());
			
		} finally {
			if(jbpmExtBridge!=null){				
				jbpmExtBridge.close();
			}
			try {
				if(hs!=null){
					hs.close();
				}
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;

		if(errors.isEmpty()){
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("application.submit.sucess"));
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("display","Y");
		forward=mapping.findForward("returnApprove");
		
		return forward;
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

		request.setAttribute("navigator.location", "维改报价申请表审核 >> 审 批");

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
		
		String billNo = (String) dform.get("billNo");
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
			
			if (billNo != null && !billNo.trim().equals("")) {
				
				OutsourceContractQuoteMaster master = (OutsourceContractQuoteMaster) hs.get(OutsourceContractQuoteMaster.class, billNo.trim());
				
				String processDefineID = Grcnamelist1.getProcessDefineID("outsourcecontractquotemaster", master.getOperId());// 流程环节id
				
				/*=============== 流程审批启动开始 =================*/
				jbpmExtBridge = new JbpmExtBridge();
				HashMap paraMap = new HashMap();
				ProcessBean pd = jbpmExtBridge.getProcessBeanUseTI(Long.parseLong(dform.get("taskid").toString()));
				
				pd.addAppointActors("");// 将动态添加的审核人清除。				
				if(approveresult!=null && approveresult.trim().equals("不同意")){
					pd.addAppointActors(master.getOperId());
				}else if("维保分部长审核".equals(taskname)){
					if(master.getMarkups()>15){
						pd.setSelpath("Y");
						//Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "维保总部长审核", master.getOperId());// 添加审核人
						Grcnamelist1.setJbpmAuditopers_roleid(pd,"N","");
					}else{
						pd.setSelpath("N");
						pd.addAppointActors(master.getOperId());
					}					
				}else if("维保总部长审核".equals(taskname)){
					pd.addAppointActors(master.getOperId());
				}else{
					Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, approveresult.replace("提交", ""), master.getOperId());// 添加审核人
				}
				
				pd = jbpmExtBridge.goToNext(taskid, approveresult, userInfo.getUserID(), paraMap);// 审核
				/*=============== 流程审批启动结束 =================*/
				
				// 保存主信息
				master.setTokenId(pd.getToken());
				master.setStatus(pd.getStatus());
				master.setProcessName(pd.getNodename());			
				hs.save(master);

				// 保存审批流程相关信息
				OutsourceContractQuoteProcess process = new OutsourceContractQuoteProcess();
				process.setBillNo(billNo);
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
		OutsourceContractQuoteMaster master = null;
		ServicingContractMaster maint=null;
		ServicingContractDetail detail=null;
		Customer customer=null;
		List detailList = new ArrayList();
		String billNo="";
		Integer status = null;
		
		if (tokenid != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from OutsourceContractQuoteMaster where tokenId = '"+ tokenid + "'");
				List list = query.list();
				if (list != null && list.size() > 0) {

					// 主信息
					master = (OutsourceContractQuoteMaster) list.get(0);
					billNo = master.getBillNo();
					status = master.getStatus();
					master.setR1(bd.getName(hs, "Loginuser", "username", "userid", master.getOperId()));
					
					customer=(Customer) hs.get(Customer.class, master.getCompanyId());
					
					maint=(ServicingContractMaster) hs.get(ServicingContractMaster.class, master.getWgBillno());
					if(maint!=null){
						maint.setMaintDivision(bd.getName("Company","comname","comid",maint.getMaintDivision()));
						maint.setMaintStation(bd.getName("Storageid", "storagename", "storageid", maint.getMaintStation()));
						String hql="select wgRowid from OutsourceContractQuoteDetail where billNo='"+master.getBillNo()+"'";
						List list1=hs.createQuery(hql).list();
						if(list1!=null && list1.size()>0){
							for(int i=0;i<list1.size();i++){
								detail=(ServicingContractDetail) hs.get(ServicingContractDetail.class, Integer.valueOf(list1.get(i).toString()));
								detailList.add(detail);
							}
						}
					}
					request.setAttribute("contractBean", maint);
					request.setAttribute("customer", customer);
					request.setAttribute("quoteBean", master);
					request.setAttribute("detailList", detailList);
					
					//审批流程信息
					query = hs.createQuery("from OutsourceContractQuoteProcess where billNo = '"+ billNo + "' order by itemId");
					List processApproveList = query.list();
					for (Object object : processApproveList) {
						OutsourceContractQuoteProcess process = (OutsourceContractQuoteProcess) object;
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
				dform.set("taskname",taskname);
				dform.set("flowname", WorkFlowConfig.getProcessDefine("outsourcecontractquotemasterProcessName"));
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
