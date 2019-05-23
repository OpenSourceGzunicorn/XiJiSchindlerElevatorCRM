package com.gzunicorn.struts.action.engcontractmanager.maintcontractquote;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
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
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.dao.DBInterface;
import com.gzunicorn.common.dao.ObjectAchieveFactory;
import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.file.HandleFile;
import com.gzunicorn.file.HandleFileImpA;
import com.gzunicorn.hibernate.basedata.Fileinfo;
import com.gzunicorn.hibernate.basedata.customer.Customer;

import com.gzunicorn.hibernate.engcontractmanager.ContractFileinfo.ContractFileinfo;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotedetail.MaintContractQuoteDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquoteprocess.MaintContractQuoteProcess;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.workflow.bo.JbpmExtBridge;

public class MaintContractQuoteJbpmAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintContractQuoteJbpmAction.class);
	
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

		request.setAttribute("navigator.location", "维保报价申请表审核 >> 修  改");
		ActionErrors errors = new ActionErrors();
		
		display(form, request, errors,"Y");
		request.setAttribute("approve", "N");
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

		String flowname=(String)dform.get("flowname");
		//if(flowname!=null && !flowname.trim().equals("")){
		//	flowname=new String(flowname.getBytes("ISO-8859-1"),"gbk"); 
		//}

		JbpmExtBridge jbpmExtBridge=null;
		
		try {
			
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();			

			MaintContractQuoteMaster master = (MaintContractQuoteMaster) hs.get(MaintContractQuoteMaster.class, billNo);
			hs.createQuery("delete from MaintContractQuoteDetail where billNo='"+billNo+"'").executeUpdate();//删除该流水号所有明细信息	
		
			String processDefineID = Grcnamelist1.getProcessDefineID("enginequotemaster", master.getOperId());// 流程环节id

    		/**======== 流程审批启动开始 ========*/
			jbpmExtBridge=new JbpmExtBridge();
	    	ProcessBean pd=jbpmExtBridge.getProcessBeanUseTI(Long.parseLong(dform.get("taskid").toString()));
	    	
	    	pd.addAppointActors("");// 将动态添加的审核人清除。
	    	//Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "维保分部长审核", master.getOperId());// 添加审核人
			//Grcnamelist1.setJbpmAuditopers_class(pd, processDefineID, PropertiesUtil.getProperty("MaintStationManagerJbpm"), master.getMaintDivision(), master.getMaintStation());// 添加审核人
	    	Grcnamelist1.setJbpmAuditopers_roleid(pd,"Y",master.getMaintDivision());
	    	
	    	pd=jbpmExtBridge.goToNext(Long.parseLong(dform.get("taskid").toString()),approveresult,userInfo.getUserID(),null);//审核
	    	/**======== 流程审批启动结束 ========*/
	    	
	    	//保存审批流程相关信息
	    	MaintContractQuoteProcess process=new MaintContractQuoteProcess();
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
			
			/** 流程终止，暂时不改变上一份合同的状态
			if(approveresult!=null && approveresult.trim().equals("终止流程")){
				String hcs=master.getHistContractStatus();
				if(hcs!=null && !hcs.trim().equals("")){
					MaintContractMaster master2 = (MaintContractMaster) hs.get(MaintContractMaster.class, master.getHistoryBillNo());
					master2.setContractStatus(hcs.trim());
					hs.save(master2);
				}
	    	}
			 */
	    	//保存主信息
			String ccastr="";
			String[] ccarr=request.getParameterValues("contractContentApply");
			if(ccarr!=null && ccarr.length>0){
				ccastr=Arrays.toString(ccarr);
				ccastr=ccastr.replaceAll("\\[", "");
				ccastr=ccastr.replaceAll("\\]", "");
			}

			//String customerArea=request.getParameter("customerArea");//客户区域
			
			BeanUtils.copyProperties(master, dform); // 复制所有属性值
			master.setContractContentApply(ccastr);
			master.setCustomerArea("");
			master.setBillNo(billNo);
			master.setSubmitType("Y");// 提交标志
	    	master.setTokenId(pd.getToken());
	    	master.setStatus(pd.getStatus());
	    	master.setProcessName(pd.getNodename());
			master.setOperId(userInfo.getUserID());// 录入人
			master.setOperDate(CommonUtil.getNowTime());// 录入时间
			hs.saveOrUpdate(master);
			
			String ccapply=master.getContractContentApply();//合同包含配件及服务
					
			// 电梯信息
			MaintContractQuoteDetail detail = null;
			
			String[] elevatorNo = request.getParameterValues("elevatorNo");// 电梯编号
			String[] elevatorType = request.getParameterValues("elevatorType");// 电梯类型
			String[] floor = request.getParameterValues("floor");// 层
			String[] stage = request.getParameterValues("stage");// 站
			String[] door = request.getParameterValues("door");// 门
			String[] high = request.getParameterValues("high");// 提升高度
			//String[] num = request.getParameterValues("num");// 台数
			String[] weight = request.getParameterValues("weight");// 载重
			String[] speed = request.getParameterValues("speed");// 速度
			String[] elevatorAge = request.getParameterValues("elevatorAge");// 电梯年龄
			String[] jyMoney = request.getParameterValues("jyMoney");//年检费
			String[] standardQuoteDis = request.getParameterValues("standardQuoteDis");// 标准报价计算描述
			//String[] customerArea = request.getParameterValues("customerArea");// 客户区域
			String[] standardQuote = request.getParameterValues("standardQuote");// 标准报价
			String[] finallyQuote = request.getParameterValues("finallyQuote");// 最终报价
			String[] salesContractNo = request.getParameterValues("salesContractNo");// 销售合同号
			String[] projectName = request.getParameterValues("projectName");// 项目名称
			String[] projectAddress = request.getParameterValues("projectAddress");// 项目地址
			String[] contractPeriod = request.getParameterValues("contractPeriod");//合同有效期
			String[] elevatorParam = request.getParameterValues("elevatorParam");
			String[] signWay = request.getParameterValues("signWay");
			
			List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型列表
						
			if(elevatorNo != null && elevatorNo.length > 0){								
				for (int i = 0; i < elevatorNo.length; i++) {
					detail = new MaintContractQuoteDetail();
					detail.setBillNo(billNo);
					detail.setElevatorNo(elevatorNo[i]);					
					detail.setElevatorType(bd.getOptionId(elevatorType[i], elevatorTypeList));//电梯类型代码	
					detail.setFloor(Integer.valueOf(floor[i]));
					detail.setStage(Integer.valueOf(stage[i]));
					detail.setDoor(Integer.valueOf(door[i]));					
					detail.setHigh(Double.valueOf(high[i]));
					detail.setElevatorParam(elevatorParam[i]);
					detail.setNum(1);		
					//detail.setElevatorAge(Double.valueOf(0));
					//detail.setCustomerArea(customerArea[i]);
					detail.setStandardQuote(Double.valueOf(standardQuote[i]));
					detail.setFinallyQuote(Double.valueOf(finallyQuote[i]));	
					detail.setSalesContractNo(salesContractNo[i]);
					detail.setProjectName(projectName[i]);
					detail.setProjectAddress(projectAddress[i]);
					detail.setContractPeriod(contractPeriod[i]);
					detail.setSignWay(signWay[i]);
					
					detail.setWeight(weight[i]);
					detail.setSpeed(speed[i]);
					detail.setElevatorAge(Double.parseDouble(elevatorAge[i]));
					detail.setStandardQuoteDis(standardQuoteDis[i]);
					if(jyMoney!=null && !jyMoney[i].trim().equals("")){
						detail.setJyMoney(Double.parseDouble(jyMoney[i]));
					}
					
					//合同包含配件及服务包含‘代理商委托我方做免保’，那么签署方式修改为‘新免保’
					if(ccapply.indexOf("100")>-1){
						detail.setSignWay("XQ");
					}
					
					hs.save(detail);
				}
			}	
			
			// 保存文件
			String path = "MaintContractQuoteMaster.file.upload.folder";
			String tableName = "MaintContractQuoteMaster";//表名 维保报价申请表
			String userid = userInfo.getUserID();

			List fileInfoList = this.saveFile(dform,request,response, path, billNo);
			boolean istrue=this.saveFileInfo(hs,fileInfoList,tableName,billNo, userid);

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

		request.setAttribute("navigator.location", "维保报价申请表审核 >> 审 批");

		ActionErrors errors = new ActionErrors();

		display(form, request, errors,"N"); //查看方法
		
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
		String taskid = (String) dform.get("taskid");// 任务名称
		String approveresult = (String) dform.get("approveresult");// 审批结果
		String approverem = (String) dform.get("approverem");// 审批意见
			
		Session hs = null;
		Transaction tx = null;
		JbpmExtBridge jbpmExtBridge = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			if (billNo != null && !billNo.trim().equals("")) {
				
				MaintContractQuoteMaster master = (MaintContractQuoteMaster) hs.get(MaintContractQuoteMaster.class, billNo.trim());
				
				String processDefineID = Grcnamelist1.getProcessDefineID("enginequotemaster", master.getOperId());// 流程环节id
				
				/*=============== 流程审批启动开始 =================*/
				jbpmExtBridge = new JbpmExtBridge();
				HashMap paraMap = new HashMap();
				ProcessBean pd = jbpmExtBridge.getProcessBeanUseTI(Long.parseLong(dform.get("taskid").toString()));
				
				pd.addAppointActors("");// 将动态添加的审核人清除。				
				if(approveresult!=null && approveresult.trim().equals("不同意")){
					pd.addAppointActors(master.getOperId());
				}else if("维保分部长审核".equals(taskname)){
					if(master.getBusinessCosts()>0){
						pd.setSelpath("Y");
						//Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "维保总部长审核", master.getOperId());// 添加审核人
						Grcnamelist1.setJbpmAuditopers_roleid(pd,"N","");
					}else{
						pd.setSelpath("N");
					}					
				}else{
					Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, approveresult.replace("提交", ""), master.getOperId());// 添加审核人
				}
				
				pd = jbpmExtBridge.goToNext(Long.parseLong(dform.get("taskid").toString()), approveresult, userInfo.getUserID(), paraMap);// 审核
				/*=============== 流程审批启动结束 =================*/
				
				// 保存主信息
				master.setTokenId(pd.getToken());
				master.setStatus(pd.getStatus());
				master.setProcessName(pd.getNodename());			
				hs.save(master);

				// 保存审批流程相关信息
				MaintContractQuoteProcess process = new MaintContractQuoteProcess();
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
	public void display(ActionForm form, HttpServletRequest request,ActionErrors errors,String isupdate)
			throws UnsupportedEncodingException{
		
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
		}else {
			taskname = new String(taskname.getBytes("ISO-8859-1"), "gbk");
		}
		String tasktype = request.getParameter("tasktype");// 任务类型
		if (tasktype == null) {
			tasktype = (String) dform.get("tasktype");
		}

		Session hs = null;
		MaintContractQuoteMaster master = null;
		List maintContractQuoteDetailList = null;
		String billNo="";
		Integer status = null;
		
		if (tokenid != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from MaintContractQuoteMaster where tokenId = '"+ tokenid + "'");
				List list = query.list();
				if (list != null && list.size() > 0) {

					// 主信息
					master = (MaintContractQuoteMaster) list.get(0);
					
					//是否修改
					if(isupdate!=null && isupdate.trim().equals("N")){
						List pdlist=bd.getPullDownAllList("MaintContractQuoteMaster_PaymentMethodApply");
						String pmaname=bd.getOptionName(master.getPaymentMethodApply(), pdlist);
						master.setR4(pmaname);
						
						//合同附件内容申请
						String ccastrname="";
						String ccastr=master.getContractContentApply();
						if(ccastr!=null &&!ccastr.trim().equals("")){
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
						master.setContractContentApply(ccastrname);
					}else{
						request.setAttribute("PaymentMethodList", bd.getPullDownList("MaintContractQuoteMaster_PaymentMethodApply"));
						request.setAttribute("ContractContentList", bd.getPullDownList("MaintContractQuoteMaster_ContractContentApply"));
					}
					
					//A03  维保经理  只能看自己维保站下面的合同
					String roleid=userInfo.getRoleID();
					if(roleid!=null && roleid.trim().equals("A03")){
						master.setR2(bd.getName(hs, "Storageid","storageName", "storageID", master.getMaintStation()));
						master.setR1(roleid);
					}
					
					billNo = master.getBillNo();
					status = master.getStatus();
					request.setAttribute("maintContractQuoteBean", master);
					request.setAttribute("attnName", bd.getName(hs, "Loginuser","username", "userid",master.getAttn())); //经办人名称
					request.setAttribute("maintDivisionName", bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision())); //维保分部名称
					if(!"toPrepareUpdateApprove".equals(request.getParameter("method"))){
						request.setAttribute("maintStationName", bd.getName(hs, "Storageid","storageName", "storageID", master.getMaintStation())); //维保站名称
					}
					
					// 所属维保站列表	
					request.setAttribute("maintStationList", bd.getMaintStationList(userInfo.getComID()));	

					// 电梯信息明细列表
					query = hs.createQuery("from MaintContractQuoteDetail where billNo = '"+ billNo + "'");
					List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型列表
					maintContractQuoteDetailList = query.list();
					for (Object object : maintContractQuoteDetailList) {
						MaintContractQuoteDetail detail = (MaintContractQuoteDetail) object;
						detail.setElevatorType(bd.getOptionName(detail.getElevatorType(), elevatorTypeList));
						detail.setR4(bd.getName("Pulldown", "pullname", "id.pullid",detail.getSignWay()));//签署方式
					}
					request.setAttribute("maintContractQuoteDetailList",maintContractQuoteDetailList);
					
					//审批流程信息
					query = hs.createQuery("from MaintContractQuoteProcess where billNo = '"+ billNo + "' order by itemId");
					List processApproveList = query.list();
					for (Object object : processApproveList) {
						MaintContractQuoteProcess process = (MaintContractQuoteProcess) object;
						process.setUserId(bd.getName(hs, "Loginuser", "username", "userid", process.getUserId()));
					}
					request.setAttribute("processApproveList",processApproveList);
					
					//审核下拉框内容
					List tranList=this.getTransition(hs.connection(),3,null,Long.parseLong(taskid));
					request.setAttribute("ResultList",tranList);
					
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
				
				//获取已经上传的附件
				List filelst=this.FileinfoList(hs, billNo, "MaintContractQuoteMaster");
				request.setAttribute("updatefileList", filelst);
				
				//获取计算标准报价的相关系数
				request.setAttribute("returnhmap", bd.getCoefficientMap());
				
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
				dform.set("tokenid",tokenid);
				dform.set("taskid",taskid);				
				dform.set("taskname",taskname);
				dform.set("flowname", WorkFlowConfig.getProcessDefine("enginequotemasterProcessName"));
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

	/**
	 * 保存上传文件
	 */
	public List saveFile(ActionForm form,HttpServletRequest request, HttpServletResponse response,
			String path,String billno){
		List returnList = new ArrayList();
		int filenum=0;
		int fileCount=0;

		path =  PropertiesUtil.getProperty(path).trim();//上传目录 

		 FormFile formFile = null;
		 Fileinfo file=null;
		 if (form.getMultipartRequestHandler() != null) {
			 Hashtable hash = form.getMultipartRequestHandler().getFileElements();
			 if (hash != null) {
				 
				 Iterator it = hash.values().iterator();
				 HandleFile hf = new HandleFileImpA();
				 while (it.hasNext()) {				 
					 
					 fileCount++;
					 formFile = (FormFile) it.next();
					 if (formFile != null) {
						 try {
							 String today=DateUtil.getCurDate();
							 String time=DateUtil.getDateTime("yyyyMMddHHmmss");
							 String newfilename=time+"_"+fileCount+"_"+formFile.getFileName();
							 Map map = new HashMap();
							 map.put("oldfilename", formFile.getFileName());
							 map.put("newfilename",newfilename);
							 map.put("filepath", today.substring(0,7)+"/");
							 map.put("filesize", new Double(formFile.getFileSize()));
							 map.put("fileformat",formFile.getContentType());
							 map.put("rem","");

							// 保存文件入文件系统
							hf.createFile(formFile.getInputStream(),path+today.substring(0,7)+"/", newfilename);
							returnList.add(map);
						}catch (Exception e) {
							e.printStackTrace();
						}
						
					 }
				 }
			 }
		 }
		return returnList;
	}
	
	/**
	 * 保存文件信息到数据库
	 */
	public boolean saveFileInfo(Session hs,List fileInfoList,String tableName,String billno,String userid){
		boolean saveFlag = true;
		ContractFileinfo file=null;
		if(null != fileInfoList && !fileInfoList.isEmpty()){
			
			try {
				int len = fileInfoList.size();
				for(int i = 0 ; i < len ; i++){
					Map map = (Map)fileInfoList.get(i);
					 String oldfilename =(String) map.get("oldfilename");
					 String newfilename =(String) map.get("newfilename");
					 String filepath =(String) map.get("filepath");
					 Double filesize =(Double) map.get("filesize");
					 String fileformat =(String) map.get("fileformat");
					 String rem =(String) map.get("rem");
					 
					 
					 file = new ContractFileinfo();
					 file.setMaterSid(billno);
					 file.setBusTable(tableName);
					 file.setOldFileName(oldfilename);
					 file.setNewFileName(newfilename);
					 file.setFilePath(filepath);
					 file.setFileSize(filesize);
					 file.setFileFormat(fileformat);
					 file.setUploadDate(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
					 file.setUploader(userid);
					 file.setRemarks(rem);
					 
					 hs.save(file);
					 hs.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
				saveFlag = false;
			}
		}
		return saveFlag;
	}
	/**
	 * 获取已上传附件列表
	 */
	public List FileinfoList(Session hs, String MaterSid, String BusTable)
	  {
	    List rt = new ArrayList();
	    Connection con = null;
	    try {
	      con = hs.connection();
	      String sql = "select a.*,b.username as UploaderName  from  ContractFileinfo a ,loginuser b " +
	      		"where a.Uploader = b.userid and a.MaterSid = '" + MaterSid + "'  and a.BusTable = '" + BusTable + "'";
	      DataOperation op = new DataOperation();
	      op.setCon(con);
	      rt = op.queryToList(sql);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return rt;
	  }

	
}
