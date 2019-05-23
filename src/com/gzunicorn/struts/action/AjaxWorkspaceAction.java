package com.gzunicorn.struts.action;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.contractpayment.contractinvoicemanage.ContractInvoiceManage;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferMaster;
import com.gzunicorn.hibernate.custregistervisitplan.custreturnregistermaster.CustReturnRegisterMaster;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster.EntrustContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.lostelevatorreport.LostElevatorReport;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdelaymaster.MaintContractDelayMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.hotlinemanagement.advisorycomplaintsmanage.AdvisoryComplaintsManage;
import com.gzunicorn.hibernate.hotlinemanagement.calloutmaster.CalloutMaster;
import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;
import com.gzunicorn.hibernate.infomanager.qualitycheckmanagement.QualityCheckManagement;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

/**
 * 工作区统一读数据Action
 * 
 * @author FeiGe
 * 
 */
public class AjaxWorkspaceAction extends DispatchAction {

	Log log = LogFactory.getLog(AjaxWorkspaceAction.class);

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

		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchRecord";

		}

		DebugUtil.printDoBaseAction("AjaxWorkspaceAction", name, "start");
		ActionForward forward = dispatchMethod(mapping, form, request,
				response, name);
		DebugUtil.printDoBaseAction("AjaxWorkspaceAction", name, "end");
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
	 * 通过Ajax进行数据读取
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	
	public String toAjaxRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		String divid = request.getParameter("id");
		ActionForward forward = mapping.findForward("ajax");

		List list = new ArrayList();
		Session hs = null;
		try {
			hs = HibernateUtil.getSession();
			list = MethodDistributed(divid, hs, request);
			
			request.setAttribute("divid", divid);
			response.setContentType("application/xml;charset=GBK");
			response.getWriter().write(this.toXML(list));
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/***************************************************************************
	 * 分发方法
	 * 
	 * @param key
	 *            关键KEY
	 * @param hs
	 * @throws HibernateException
	 */
	private List MethodDistributed(String key, Session hs,
			HttpServletRequest request) throws HibernateException {
		HttpSession httpsession = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)httpsession.getAttribute(SysConfig.LOGIN_USER_INFO);
		String userid = userInfo.getUserID();
		String roleid=userInfo.getRoleID();
		String comid=userInfo.getComID();
		String storageid=userInfo.getStorageId();
		
		if (key.equalsIgnoreCase(SysConfig.TOGET_DUTYS)) {
			//我的待办任务
			return this.getMyDutysInfo(request, hs, userid, roleid,storageid);
			
		}else if (key.equalsIgnoreCase(SysConfig.TOGET_Fault)) {
			//急修流程
			return this.getCalloutMasterInfo(request, hs, userid, roleid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_Complaints)) {
			//全质办咨询投诉管理 [驳回,派工,处理]
			return this.getAdvisoryComplaints(request, hs, userid, roleid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_Contract)) {
			//维保合同到期
			return this.getDueMaintContract(request, hs, comid, roleid,storageid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_QualityCheck)) {
			//维保质量检查  
			return this.getQualityCheck(request, hs, userid, roleid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_ElevatorTrans)) {
			//安装维保交接电梯情况  [驳回，接收, 转派]
			return this.getElevatorTrans(request, hs, userid, roleid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_CustomerVisit)) {
			//客户拜访反馈
			return this.getCustomerVisitPlanDetail(request, hs, userid, roleid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_ContractMaster)) {
			//有可读写权限 维保合同管理，维保委托合同管理 [审核,驳回]
			return this.getContractMaster(request, hs, comid, roleid,storageid,userid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_ContractDelay)) {
			//有可读写权限 维保合同延保管理 [审核,驳回]
			return this.getContractDelay(request, hs, comid, roleid,storageid,userid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_LostElevator)) {
			//有可读写权限 维保合同退保管理 [审核,驳回]
			return this.getLostElevator(request, hs, comid, roleid,storageid,userid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_MaintContractQuoteMaster)) {
			//有可读写权限维保报价管理节点功能
			return this.getMaintContractQuote(request, hs, comid, roleid,storageid,userid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_ContractInvoice)) {
			//有可读写 开票分部长审核  节点功能
			return this.getContractInvoice(request, hs, comid, roleid,storageid,userid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_CustReturnRegister)) {
			//有可读写 客户回访分部长处理  节点功能
			return this.getCustReturnRegister(request, hs, comid, roleid,storageid,userid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_ContractAssigned)) {
			//有可读写 维保合同下达签收  节点功能
			return this.getContractAssigned(request, hs, comid, roleid,storageid,userid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_LoginUserAudit)) {
			//有可读写 用户信息审核  节点功能
			return this.getLoginUserAudit(request, hs, comid, roleid,storageid,userid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_HotCalloutAudit)) {
			//有可读写 急修安全经理审核  节点功能
			return this.getHotCalloutAudit(request, hs, comid, roleid,storageid,userid);
			
		} else if (key.equalsIgnoreCase(SysConfig.TOGET_AccessoriesRequisition)) {
			//有可读写 配件申请单(仓库管理员)处理  节点功能
			return this.getAccessoriesRequisition(request, hs, comid, roleid,storageid,userid);

		} else if (key.equalsIgnoreCase(SysConfig.TOGET_AccessoriesRequisitionCkbl)) {
			//有可读写 配件申请单出库办理   节点功能
			return this.getAccessoriesRequisitionCkbl(request, hs, comid, roleid,storageid,userid);
			
		} else if (key.equalsIgnoreCase(SysConfig.TOGET_TransferAssign)) {
			//有可读写 合同交接资料派工   节点功能
			return this.getTransferAssign(request, hs, comid, roleid,storageid,userid);
			
		} else if (key.equalsIgnoreCase(SysConfig.TOGET_TransferUpload)) {
			//有可读写 合同交接资料上传   节点功能
			return this.getTransferUpload(request, hs, comid, roleid,storageid,userid);
			
		} else if (key.equalsIgnoreCase(SysConfig.TOGET_TransferAudit)) {
			//有可读写 合同交接资料审核   节点功能
			return this.getTransferAudit(request, hs, comid, roleid,storageid,userid);
			
		} else if (key.equalsIgnoreCase(SysConfig.TOGET_TransferManagerAudit)) {
			//有可读写 合同交接资料经理审核   节点功能
			return this.getTransferManagerAudit(request, hs, comid, roleid,storageid,userid);
			
		} else if (key.equalsIgnoreCase(SysConfig.TOGET_TransferFeedBack)) {
			//有可读写 合同交接资料反馈查看   节点功能
			return this.getTransferFeedBack(request, hs, comid, roleid,storageid,userid);

		} else {
			return null;
		}
	}
	/**
	 * 首页=菜单待办列表
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getMyDutysInfo(HttpServletRequest request,
			Session hs, String userid,String roleid,String storageid) throws HibernateException {
		List reList = null;
	
		Connection conn = null;
		try {
	
			reList = new ArrayList();
			
			/**===============审批流程，待办任务===============*/
			conn = hs.connection();
			String sql = "exec Sp_FetchToDoList '" + userid + "'";
			//System.out.println(sql);
			DataOperation op = new DataOperation();
			op.setCon(conn);
			
			List list = op.queryToList(sql);
			if (list != null && !list.isEmpty()) {
				HashMap map = null;
				String url = "";
				for (int i = 0; i < list.size(); i++) {
					url = "";
					map = new HashMap();
					HashMap obj = (HashMap) list.get(i);
					map.put("title",obj.get("showinfo"));
					map.put("sid",obj.get("taskid"));
					if(obj.get("actorid") != null && obj.get("actorid").toString().length()>0){
						url =SysConfig.WEB_APPNAME+"/"+obj.get("flowurl")+"&tokenid="+obj.get("tokenid")+"&taskid="+obj.get("taskid")+"&taskname="+obj.get("taskname")+"&taskname2="+obj.get("taskname2")+"&flowname="+obj.get("flowname")+"&tasktype=2";
					}else{
						url =SysConfig.WEB_APPNAME+"/"+obj.get("flowurl")+"&tokenid="+obj.get("tokenid")+"&taskid="+obj.get("taskid")+"&taskname="+obj.get("taskname")+"&taskname2="+obj.get("taskname2")+"&flowname="+obj.get("flowname")+"&tasktype=1";
					}
					map.put("url",url);
					map.put("date",obj.get("createdate").toString().substring(0,10));
					map.put("title2", obj.get("showinfo"));
					reList.add(map);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}

	/**
	 * 全质办咨询投诉管理 派工
	 * 0601	咨询投诉登记
	   0605   咨询投诉派工
	   0602  咨询投诉处理
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getAdvisoryComplaints(HttpServletRequest request,
			Session hs, String userid,String roleid) throws HibernateException {
		
		List reList = null;
		HashMap map = null;	
		try {
			reList = new ArrayList();
			
			if(!roleid.equals("A01")){

				/**===============全质办咨询投诉登记 驳回 可读写权限===============*/
				String sql1="select * from RoleNode where NodeID in('0601') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist1=hs.createSQLQuery(sql1).list();
				if(rlist1!=null && rlist1.size()>0){
					//驳回的
					String sql="from AdvisoryComplaintsManage where submitType in('R') and receivePer='"+userid+"' " +
							" order by processSingleNo desc";
					List frlist=hs.createQuery(sql).list();
					if(frlist!=null && frlist.size()>0){
						AdvisoryComplaintsManage master=null;
						for(int i=0;i<frlist.size();i++){
							String submittypename="";
							master=(AdvisoryComplaintsManage)frlist.get(i);
							String singleno = master.getProcessSingleNo();
							String submittype=master.getSubmitType();
							if(submittype!=null && submittype.trim().equals("N")){
								submittypename="未提交";
							}else if(submittype!=null && submittype.trim().equals("R")){
								submittypename="驳回";
							}
							//需要增加跳转路径 问题接收日期
							String urlstr=SysConfig.WEB_APPNAME+"/advisoryComplaintsManageAction.do?id="+singleno+"&method=toPrepareUpdateRecord";
						
							String descstr="处理单编号："+singleno+
								" | 提交标志："+submittypename+
								" | 问题接收日期："+master.getReceiveDate();
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  singleno+String.valueOf(i));
							map.put("url", urlstr);
							map.put("date",  master.getReceiveDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
	
				/**===============全质办咨询投诉派工 可读写权限===============*/
				String sql2="select * from RoleNode where NodeID in('0605') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist2=hs.createSQLQuery(sql2).list();
				if(rlist2!=null && rlist2.size()>0){
					//提交的，委托处理的，未派工的
					String sql="from AdvisoryComplaintsManage where submitType='Y' " +
							" and isEntrust='Y' and dispatchType in('N') order by processSingleNo desc";
					List frlist=hs.createQuery(sql).list();
					
					if(frlist!=null && frlist.size()>0){
						AdvisoryComplaintsManage master=null;
						for(int i=0;i<frlist.size();i++){
							master=(AdvisoryComplaintsManage)frlist.get(i);
							String singleno = master.getProcessSingleNo();
		
							//需要增加跳转路径 问题接收日期
							String urlstr=SysConfig.WEB_APPNAME+"/advisoryComplaintsManageAction.do?id="+singleno+"&method=toPrepareDispatchRecord";
												
							String descstr="处理单编号："+singleno+
								" | 派工标志：未派工"+
								" | 问题接收日期："+master.getReceiveDate();
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  singleno+String.valueOf(i));
							map.put("url", urlstr);
							map.put("date",  master.getReceiveDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				
				/**===============全质办咨询投诉处理 可读写权限===============*/
				String sql3="select * from RoleNode where NodeID in('0602') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist3=hs.createSQLQuery(sql3).list();
				if(rlist3!=null && rlist3.size()>0){
					//提交的，已派工已提交，未处理和驳回的
					String sql="from AdvisoryComplaintsManage where submitType='Y' " +
							" and dispatchType in('Y') and processType in('N','R') and processPer='"+userid+"'"+
							" order by processSingleNo desc";
					List frlist=hs.createQuery(sql).list();
					if(frlist!=null && frlist.size()>0){
						AdvisoryComplaintsManage master=null;
						for(int i=0;i<frlist.size();i++){
							String processtypename="";
							master=(AdvisoryComplaintsManage)frlist.get(i);
							String singleno = master.getProcessSingleNo();
							String processType=master.getProcessType();
							if(processType!=null && processType.trim().equals("N")){
								processtypename="未处理";
							}else if(processType!=null && processType.trim().equals("R")){
								processtypename="驳回";
							}
							//需要增加跳转路径
							String urlstr=SysConfig.WEB_APPNAME+"/advisoryComplaintsManageAction.do?id="+singleno+"" +
									"&method=toPrepareDisposeRecord&returnMethod=toSearchRecordDispose" +
									"&authority=advisorycomplaintsdispose";
						
							String descstr="处理单编号："+singleno+
								" | 处理标志："+processtypename+
								" | 问题接收日期："+master.getReceiveDate();
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  singleno+String.valueOf(i));
							map.put("url", urlstr);
							map.put("date",  master.getReceiveDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reList;
	}


	/**
	 * 急修流程管理处理列表
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getCalloutMasterInfo(HttpServletRequest request,
			Session hs, String userid,String roleid) throws HibernateException {
		
		List reList = null;
		HashMap map = null;	
		String urlstr="";
		String statusname="";
		try {
			reList = new ArrayList();			
			
			if(!roleid.equals("A01")){

				//查询未处理的故障报修录入
				String sql="from CalloutMaster a where a.handleStatus in('5','6') order by a.calloutMasterNo desc";
				List frlist=hs.createQuery(sql).list();
				
				if(frlist!=null && frlist.size()>0){
					CalloutMaster cm=null;
					for(int i=0;i<frlist.size();i++){
						cm=(CalloutMaster)frlist.get(i);
						String appno=cm.getCalloutMasterNo();
						String istiring=cm.getIsTrap();
						String isprocess=cm.getHandleStatus();
						//需要增加跳转路径
						if(isprocess!=null && "5".equals(isprocess)){
							statusname="急修审核";
							urlstr=SysConfig.WEB_APPNAME+"/hotphoneAction.do?id="+appno+"&typejsp=sh";
						}else if(isprocess!=null && "6".equals(isprocess)){
							statusname="回访评审";
							urlstr=SysConfig.WEB_APPNAME+"/hotphoneAction.do?id="+appno+"&typejsp=ps";
						}
						
						String companyname=bd.getName("Customer", "companyName", "companyId", cm.getCompanyId());
						if(companyname==null || companyname.trim().equals("")){
							companyname=cm.getCompanyId();
						}
						String descstr="急修编号："+appno+
							" | 处理状态："+statusname+
							//" | 困人："+CommonUtil.tranEnabledFlag(istiring)+
							" | 报修单位："+companyname;
						
						map = new HashMap();
						map.put("title", descstr);
						map.put("sid",  appno+String.valueOf(i));
						map.put("url", urlstr);
						map.put("date",  cm.getOperDate());
						map.put("title2", descstr);
						
						reList.add(map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reList;
	}
	/**
	 * 可读写权限 维保质量检查  登记
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getQualityCheck(HttpServletRequest request,
			Session hs, String userid,String roleid) throws HibernateException {
		List reList = null;

		try {

			reList = new ArrayList();
			
			if(!roleid.equals("A01")){

				/**===============未登记的 维保质量检查登记  可读写权限===============*/
				String sqlaa="from QualityCheckManagement where SubmitType='Y' " +
						"and ProcessStatus='0' and SuperviseId='" + userid + "' order by billno desc";
				List etfclistkk3=hs.createQuery(sqlaa).list();
				if(etfclistkk3!=null && etfclistkk3.size()>0){
					HashMap map = null;
					String url = "";
					for(int e=0;e<etfclistkk3.size();e++){
						QualityCheckManagement qcm=(QualityCheckManagement)etfclistkk3.get(e);
						
						String billno = qcm.getBillno();
	
						//需要增加跳转路径 
						String urlstr=SysConfig.WEB_APPNAME+"/qualityCheckManagementAction.do?id="+billno+"&method=toPrepareRegistrationRecord";
											
						String descstr="维保质量检查流水号："+billno +
							" | 处理状态：未登记"+
							" | 电梯编号："+qcm.getElevatorNo();
						
						map = new HashMap();
						map.put("title", descstr);
						map.put("sid",  billno+String.valueOf(e));
						map.put("url", urlstr);
						map.put("date",  qcm.getOperDate());
						map.put("title2", descstr);
						
						reList.add(map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}

	/**
	 * 可读写权限 安装维保交接电梯情况  驳回，接收
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getElevatorTrans(HttpServletRequest request,
			Session hs, String userid,String roleid) throws HibernateException {
		List reList = null;
		try {

			reList = new ArrayList();
			
			if(!roleid.equals("A01")){
			
				/**===============被驳回的,转派的 安装维保交接电梯情况管理 可读写权限===============*/
				String sqlkk="select * from RoleNode where NodeID in('0406') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sqlkk).list();
				if(rlist!=null && rlist.size()>0){
					String sqlaa="from ElevatorTransferCaseRegister where SubmitType in ('R','Z') " +
							"and isnull(workisdisplay,'N')='N' order by billno desc";
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						HashMap map = null;
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							ElevatorTransferCaseRegister etfc=(ElevatorTransferCaseRegister)etfclist.get(j);
							
							String billno = etfc.getBillno();
							String submittype=etfc.getSubmitType();
		
							String urlstr="";
							urlstr=SysConfig.WEB_APPNAME+"/elevatorTransferCaseRegisterManageAction.do?id="+billno+"&method=toDisplayRecord&workisdisplay=Y";
							
							String descstr="";
							if(submittype!=null && submittype.equals("Z")){
								//需要增加跳转路径 
								//urlstr=SysConfig.WEB_APPNAME+"/elevatorTransferCaseRegisterManageAction.do?id="+billno+"&method=toDisplayRecord&workisdisplay=Y";
													
								descstr="安装维保交接流水号："+billno+
									" | 提交标志：转派"+
									" | 电梯编号："+etfc.getElevatorNo();
							}else{
								//需要增加跳转路径
								//urlstr=SysConfig.WEB_APPNAME+"/elevatorTransferCaseRegisterManageAction.do?id="+billno+"&method=toPrepareUpdateRecord";
													
								descstr="安装维保交接流水号："+billno+
									" | 提交标志：驳回"+
									" | 电梯编号："+etfc.getElevatorNo();
							}
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  billno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  etfc.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
					
				}
				
				/**===============未接收的 安装维保交接电梯情况登记  可读写权限===============*/
				String sqlkk2="select * from RoleNode where NodeID in('7110') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlistkk2=hs.createSQLQuery(sqlkk2).list();
				if(rlist!=null && rlistkk2.size()>0){
					String sqlaa="from ElevatorTransferCaseRegister where SubmitType in('Y','Z') " +
							"and ProcessStatus='0' and StaffName='" + userid + "' order by billno desc";
					List etfclistkk2=hs.createQuery(sqlaa).list();
					if(etfclistkk2!=null && etfclistkk2.size()>0){
						HashMap map = null;
						String url = "";
						for(int e=0;e<etfclistkk2.size();e++){
							ElevatorTransferCaseRegister etfc=(ElevatorTransferCaseRegister)etfclistkk2.get(e);
							
							String billno = etfc.getBillno();
		
							//需要增加跳转路径
							String urlstr=SysConfig.WEB_APPNAME+"/elevatorTransferCaseRegisterAction.do?id="+billno+"&method=toReceiveDisposeRecord";
												
							String descstr="安装维保交接流水号："+billno+
								" | 处理状态：未接收"+
								" | 电梯编号："+etfc.getElevatorNo();
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  billno+String.valueOf(e));
							map.put("url", urlstr);
							map.put("date",  etfc.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}

	/**
	 * 首页=维保到期合同列表
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getDueMaintContract(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid) throws HibernateException {
		
		List reList = null;
		HashMap map = null;		
		String maintStation="%";
		try {
			reList = new ArrayList();
			
			//A37  财务合同文员 ,A35  行政合同文员  
			if("A35".equals(roleid) || "A37".equals(roleid)){
				
				String maintDivision = "00".equals(comid) ? "%" : comid;
				//String maintDivision = comid;
				
				/**
				//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
				String sqlss="select * from view_mainstation where roleid='"+roleid+"'";
				List vmlist=hs.createSQLQuery(sqlss).list();
				if(vmlist!=null && vmlist.size()>0){
					maintStation=storageid;
				}
				*/
				
				//查询维保到期合同   根据合同结束日期提前3个月提醒 委托合同在保的不提醒。已下达的才提醒
				String today=DateUtil.getNowTime("yyyy-MM-dd");
				String datestr=DateUtil.getDate(today, "MM", 3);
				
				String sql="from MaintContractMaster where contractEdate <= '"+datestr+"' " +
						" and maintDivision like '"+maintDivision+"'" +
						" and maintStation like '"+maintStation+"'"+
						" and contractStatus in ('XB','ZB') " +
						//" and warningStatus not in('S','Y')"+
						" and isnull(warningStatus,'')='' "+//未建拜访计划
						" and isnull(TaskSubFlag,'')='Y' "+//已下达的
						//" and billNo not in(select maintBillNo from EntrustContractMaster where r1='ZB') "+
						" order by contractEdate asc";
				List mlist=hs.createQuery(sql).list();
				//System.out.println(sql);
				if(mlist!=null && mlist.size()>0){
					MaintContractMaster master=null;
					for(int i=0;i<mlist.size();i++){
						master=(MaintContractMaster)mlist.get(i);
						String billNo = master.getBillNo();
						String warningStatus=master.getWarningStatus();
						String warname="未建拜访计划";
	
						//需要增加跳转路径
						String urlstr=SysConfig.WEB_APPNAME+"/maintContractAction.do?id="+billNo+"&method=toPrepareExpireRecord";
											
						String descstr="维保合同号："+master.getMaintContractNo() +
							//" | 开始日期："+master.getContractSdate() +
							" | 结束日期："+master.getContractEdate()+
							" | 提醒状态："+warname;
						
						map = new HashMap();
						map.put("title", descstr);
						map.put("sid",  billNo+String.valueOf(i));
						map.put("url", urlstr);
						map.put("date",  master.getOperDate());
						map.put("title2", descstr);
						
						reList.add(map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reList;
	}
	/**
	 * 可读写权限 客户拜访反馈 登记
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getCustomerVisitPlanDetail(HttpServletRequest request,
			Session hs, String userid,String roleid) throws HibernateException {
		List reList = null;

		try {

			reList = new ArrayList();
			
			if(!roleid.equals("A01")){
			
				/**===============未反馈的或驳回的  客户拜访反馈 可读写权限===============*/
				String sqlaa="select b.jnlno,a.CompanyName,b.VisitDate,ISNULL(b.SubmitType,'') as SubmitType,a.OperDate " +
						"from CustomerVisitPlanDetail b,CustomerVisitPlanMaster a " +
						"where b.billno=a.billno and ISNULL(b.SubmitType,'') in('','R') and b.VisitStaff='"+userid+"' " +
						"order by b.VisitDate";
				List etfclistkk3=hs.createSQLQuery(sqlaa).list();
				if(etfclistkk3!=null && etfclistkk3.size()>0){
					HashMap map = null;
					String url = "";
					for(int e=0;e<etfclistkk3.size();e++){
						Object[] obj=(Object[])etfclistkk3.get(e);
						String jnlno = (String)obj[0];
						String CompanyName = (String)obj[1];
						String VisitDate = (String)obj[2];
						String SubmitType = (String)obj[3];
						String OperDate=(String)obj[4];
	
						//需要增加跳转路径 
						String urlstr=SysConfig.WEB_APPNAME+"/customerVisitFeedbackAction.do?id="+jnlno+"&method=toPrepareAddRecord";
						
						String descstr="";
						if(SubmitType!=null && SubmitType.trim().equals("R")){
							descstr="拜访单位名称："+CompanyName+
									" | 拜访日期："+VisitDate+
									" | 状态：驳回";
						}else{
							descstr="拜访单位名称："+CompanyName+
									" | 拜访日期："+VisitDate+
									" | 状态：未拜访";
						}
						
						map = new HashMap();
						map.put("title", descstr);
						map.put("sid",  jnlno+String.valueOf(e));
						map.put("url", urlstr);
						map.put("date",  OperDate);
						map.put("title2", descstr);
						
						reList.add(map);
					}
				}
				
				//[A13  维保总部长  ,A02  维保分部长 ，A03  维保经理]
				/**===============客户拜访反馈 维保经理跟进 [维保站长 22 和维保销售专员 100 的反馈]===============*/
				if(roleid.equals("A03")){
					String hql="select b.jnlno,a.CompanyName,b.VisitDate,b.fkOperDate " +
							"from CustomerVisitPlanMaster a,CustomerVisitPlanDetail b,loginuser l " +
							"where a.billno=b.billno and b.VisitPosition in('22','100') " +
							"and isnull(b.fkOperId,'')<>'' and ISNULL(b.ManagerFollow,'')='' " +
							"and l.storageid = a.MaintStation and l.userid  like '%"+userid+"%' "+
							"order by b.VisitDate";
					List jllist=hs.createSQLQuery(hql).list();
					if(jllist!=null && jllist.size()>0){
						HashMap map = null;
						String url = "";
						for(int e=0;e<jllist.size();e++){
							Object[] obj=(Object[])jllist.get(e);
							String jnlno = (String)obj[0];
							String CompanyName = (String)obj[1];
							String VisitDate = (String)obj[2];
							String fkOperDate=(String)obj[3];
		
							//需要增加跳转路径 
							String urlstr=SysConfig.WEB_APPNAME+"/MaintManagerFollowAction.do?id="+jnlno+"&method=toPrepareUpdateRecord";
							
							String descstr="拜访单位名称："+CompanyName+
										" | 拜访日期："+VisitDate+
										" | 状态：未跟进";
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(e));
							map.put("url", urlstr);
							map.put("date",  fkOperDate);
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				/**===============客户拜访反馈 维保分部长跟进 [维保经理 20  维保站长 22 和维保销售专员 100 的反馈]===============*/
				if(roleid.equals("A02")){
					String hql2="select b.jnlno,a.CompanyName,b.VisitDate,b.fkOperDate " +
							"from CustomerVisitPlanMaster a,CustomerVisitPlanDetail b,loginuser l " +
							"where a.billno=b.billno " +
							"and ( (b.VisitPosition in('20') and isnull(b.fkOperId,'')<>'')  " +
							"      or  (b.VisitPosition in('22','100') and ISNULL(b.ManagerFollow,'')<>'') ) " +
							"and ISNULL(b.FMinisterFollow,'')='' " +
							"and l.grcid = a.MaintDivision and l.userid  like '%"+userid+"%'" +
							"order by b.VisitDate";
					List fbzlist=hs.createSQLQuery(hql2).list();
					if(fbzlist!=null && fbzlist.size()>0){
						HashMap map = null;
						String url = "";
						for(int e=0;e<fbzlist.size();e++){
							Object[] obj=(Object[])fbzlist.get(e);
							String jnlno = (String)obj[0];
							String CompanyName = (String)obj[1];
							String VisitDate = (String)obj[2];
							String fkOperDate=(String)obj[3];
		
							//需要增加跳转路径 
							String urlstr=SysConfig.WEB_APPNAME+"/MaintFMinisterFollowAction.do?id="+jnlno+"&method=toPrepareUpdateRecord";
							
							String descstr="拜访单位名称："+CompanyName+
										" | 拜访日期："+VisitDate+
										" | 状态：未跟进";
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(e));
							map.put("url", urlstr);
							map.put("date",  fkOperDate);
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				/**===============客户拜访反馈 维保总部长跟进 [维保分部长 17 的反馈]===============*/
				if(roleid.equals("A13")){
					String hql3="select b.jnlno,a.CompanyName,b.VisitDate,b.fkOperDate " +
							"from CustomerVisitPlanMaster a,CustomerVisitPlanDetail b " +
							"where a.billno=b.billno and b.VisitPosition='17' " +
							"and isnull(b.fkOperId,'')<>'' and ISNULL(b.ZMinisterFollow,'')='' " +
							"order by b.VisitDate";
					List zbzlist=hs.createSQLQuery(hql3).list();
					if(zbzlist!=null && zbzlist.size()>0){
						HashMap map = null;
						String url = "";
						for(int e=0;e<zbzlist.size();e++){
							Object[] obj=(Object[])zbzlist.get(e);
							String jnlno = (String)obj[0];
							String CompanyName = (String)obj[1];
							String VisitDate = (String)obj[2];
							String fkOperDate=(String)obj[3];
		
							//需要增加跳转路径 
							String urlstr=SysConfig.WEB_APPNAME+"/MaintZMinisterFollowAction.do?id="+jnlno+"&method=toPrepareUpdateRecord";
							
							String descstr="拜访单位名称："+CompanyName+
										" | 拜访日期："+VisitDate+
										" | 状态：未跟进";
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(e));
							map.put("url", urlstr);
							map.put("date",  fkOperDate);
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}
	/**
	 * 首页= 维保合同管理，维保委托合同管理 [审核,驳回]
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getContractMaster(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		
		List reList = null;
		HashMap map = null;
		try {
			reList = new ArrayList();
			
			if(!roleid.equals("A01")){
			
				/**===============被驳回的,维保合同管理  可读写权限===============*/
				String sqlkk="select * from RoleNode where NodeID in('0304') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sqlkk).list();
				if(rlist!=null && rlist.size()>0){
					String sqlaa="from MaintContractMaster " +
							"where submitType in ('R') and isnull(workisdisplay,'N')='N' and operId='"+userid+"' " +
							"order by billNo ";
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							MaintContractMaster mcm=(MaintContractMaster)etfclist.get(j);
							
							String billno=mcm.getBillNo();
							String maintContractNo=mcm.getMaintContractNo();
		
							String urlstr=SysConfig.WEB_APPNAME+"/maintContractAction.do?id="+billno+"&method=toDisplayRecord&workisdisplay=Y";
							
							String descstr="流水号："+billno+
									" | 维保合同号 ："+maintContractNo+
									" | 提交标志：驳回";
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  billno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  mcm.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
					
				}
				
				/**===============被驳回的,维保委托合同管理  可读写权限===============*/
				String sqlkk2="select * from RoleNode where NodeID in('0309') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist2=hs.createSQLQuery(sqlkk2).list();
				if(rlist2!=null && rlist2.size()>0){
					String sqlaa="from EntrustContractMaster " +
							"where submitType in ('R') and isnull(workisdisplay,'N')='N' and operId='"+userid+"' " +
							"order by billNo ";
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							EntrustContractMaster mcdm=(EntrustContractMaster)etfclist.get(j);
							
							String billno=mcdm.getBillNo();
							String EntrustContractNo=mcdm.getEntrustContractNo();
		
							String urlstr=SysConfig.WEB_APPNAME+"/entrustContractMasterAction.do?id="+billno+"&method=toDisplayRecord&workisdisplay=Y";
							
							String descstr="流水号："+billno+
									" | 委托合同号 ："+EntrustContractNo+
									" | 提交标志：驳回";
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  billno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  mcdm.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reList;
	}
	/**
	 * 首页=维保合同延保管理 [审核,驳回]
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getContractDelay(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		
		List reList = null;
		HashMap map = null;
		try {
			reList = new ArrayList();
			if(!roleid.equals("A01")){
				
				//String maintDivision = "00".equals(comid) ? "%" : comid;
				String maintDivision = comid;
				
				/**===============维保合同延保审核  可读写权限===============*/
				String sqlkk="select * from RoleNode where NodeID in('0312') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sqlkk).list();
				if(rlist!=null && rlist.size()>0){
					String sqlaa="select a from MaintContractDelayMaster a,MaintContractMaster b " +
							"where a.billno=b.billNo and a.submitType in ('Y') and isnull(a.auditStatus,'N')='N' " +
							"and b.maintDivision like '"+maintDivision+"' " +
							"order by jnlno ";
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							MaintContractDelayMaster mcdm=(MaintContractDelayMaster)etfclist.get(j);
							
							String jnlno = mcdm.getJnlno();
							String billno=mcdm.getBillno();
		
							String urlstr=SysConfig.WEB_APPNAME+"/maintContractDelayAuditAction.do?id="+jnlno+"&method=toDisplayRecord";
							
							String descstr="流水号："+jnlno+
									" | 维保合同号 ："+bd.getName(hs, "MaintContractMaster", "maintContractNo", "billNo",billno)+
									" | 审核状态：未审核";
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  mcdm.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
					
				}
				
				/**===============被驳回的 审核通过的,维保合同延保管理  可读写权限===============*/
				String sqlkk2="select * from RoleNode where NodeID in('0311') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist2=hs.createSQLQuery(sqlkk2).list();
				if(rlist2!=null && rlist2.size()>0){
					String sqlaa="from MaintContractDelayMaster " +
							"where (submitType='R' or auditStatus='Y') and isnull(workisdisplay,'N')='N'" +
							" and operId='"+userid+"' order by jnlno ";
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							MaintContractDelayMaster mcdm=(MaintContractDelayMaster)etfclist.get(j);
							
							String jnlno = mcdm.getJnlno();
							String billno=mcdm.getBillno();
							
							String submit=mcdm.getSubmitType();
							
							//需要增加跳转路径
							String title="审核状态：审核通过";
							String urlstr=SysConfig.WEB_APPNAME+"/maintContractDelayAction.do?id="+jnlno+"&method=toDisplayRecord&workisdisplay=Y";
							if(submit!=null && submit.trim().equals("R")){
								urlstr=SysConfig.WEB_APPNAME+"/maintContractDelayAction.do?id="+jnlno+"&method=toPrepareUpdateRecord&isclosework=Y";
								title="提交标志：驳回";
							}
		
							String descstr="流水号："+jnlno+
									" | 维保合同号 ："+bd.getName(hs, "MaintContractMaster", "maintContractNo", "billNo",billno)+
									" | "+title;
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  mcdm.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reList;
	}
	/**
	 * 首页=维保合同退保管理 [审核,驳回]
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getLostElevator(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		
		List reList = null;
		HashMap map = null;
		try {
			reList = new ArrayList();
			
			if(!roleid.equals("A01")){
				//String maintDivision = "00".equals(comid) ? "%" : comid;
				String maintDivision = comid;
				
				//先分部长审核，在文员审核
				/**===============被驳回的和审核通过的,维保合同退保管理  可写权限===============*/
				String sqlkk="select * from RoleNode where NodeID in('0313') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sqlkk).list();
				if(rlist!=null && rlist.size()>0){
					String sqlaa="from LostElevatorReport " +
							"where (submitType='R' or AuditStatus='Y') and isnull(workisdisplay,'N')='N' " +
							"and operId='"+userid+"' order by jnlno ";
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							LostElevatorReport mcdm=(LostElevatorReport)etfclist.get(j);
							
							String jnlno = mcdm.getJnlno();
							String submittype=mcdm.getSubmitType();
							String auditstatus=mcdm.getAuditStatus();
							
							String typename="审核状态：审核通过";
							String urlstr=SysConfig.WEB_APPNAME+"/lostElevatorReportAction.do?id="+jnlno+"&method=toDisplayRecord&workisdisplay=Y";
							
							if(submittype!=null && submittype.trim().equals("R")){
								typename="提交标志：驳回";
								urlstr=SysConfig.WEB_APPNAME+"/lostElevatorReportAction.do?id="+jnlno+"&method=toPrepareUpdateRecord&isclosework=Y";
							}
							
							String descstr="流水号："+jnlno+
									" | 维保合同号 ："+mcdm.getMaintContractNo()+
									" | "+typename;
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  mcdm.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				
				/**===============未审核的,维保合同退保文员审核  可写权限===============*/
				String sqlkk2="select * from RoleNode where NodeID in('0314') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist2=hs.createSQLQuery(sqlkk2).list();
				if(rlist2!=null && rlist2.size()>0){
					String sqlaa="from LostElevatorReport  " +
							"where submitType='Y' and isnull(auditStatus,'N')='N' " +
							//" and (isnull(isCharge,'')='Y' or isnull(auditStatus3,'N')='Y') "+ //扣款 或者 总部长审核通过
							" and isnull(auditStatus3,'N')='Y'"+ // 总部长审核通过
							"and maintDivision like '"+maintDivision+"' order by jnlno ";
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							LostElevatorReport mcdm=(LostElevatorReport)etfclist.get(j);
							
							String jnlno = mcdm.getJnlno();
		
							String urlstr=SysConfig.WEB_APPNAME+"/lostElevatorReportAuditAction.do?id="+jnlno+"&method=toDisplayRecord";
							
							String descstr="流水号："+jnlno+
									" | 维保合同号 ："+mcdm.getMaintContractNo()+
									" | 文员审核状态：未审核";
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  mcdm.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				
				/**===============未审核的,维保合同退保总部长审核  可写权限===============*/
				String sqlkk4="select * from RoleNode where NodeID in('0324') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist4=hs.createSQLQuery(sqlkk4).list();
				if(rlist4!=null && rlist4.size()>0){
					String sqlaa="from LostElevatorReport  " +
							"where submitType='Y' and isnull(auditStatus3,'N')='N' " +
							//"and isnull(isCharge,'')='N' and isnull(auditStatus2,'N')='Y' "+//不扣款需要总部长审核
							" and isnull(auditStatus2,'N')='Y'"+//总部长审核
							"and maintDivision like '%' order by jnlno ";
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							LostElevatorReport mcdm=(LostElevatorReport)etfclist.get(j);
							
							String jnlno = mcdm.getJnlno();
		
							String urlstr=SysConfig.WEB_APPNAME+"/lostElevatorReportAudit3Action.do?id="+jnlno+"&method=toDisplayRecord";
							
							String descstr="流水号："+jnlno+
									" | 维保合同号 ："+mcdm.getMaintContractNo()+
									" | 总部长审核状态：未审核";
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  mcdm.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				
				/**===============未审核的,维保合同退保分部长审核  可写权限===============*/
				String sqlkk3="select * from RoleNode where NodeID in('0321') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist3=hs.createSQLQuery(sqlkk3).list();
				if(rlist3!=null && rlist3.size()>0){
					String sqlaa="from LostElevatorReport  " +
							"where submitType='Y' and isnull(auditStatus2,'N')='N' " +
							" and isnull(fhRem,'')<>'' "+//提交的并且有回访结果的
							"and maintDivision like '"+maintDivision+"' order by jnlno ";
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							LostElevatorReport mcdm=(LostElevatorReport)etfclist.get(j);
							
							String jnlno = mcdm.getJnlno();
		
							String urlstr=SysConfig.WEB_APPNAME+"/lostElevatorReportAudit2Action.do?id="+jnlno+"&method=toDisplayRecord";
							
							String descstr="流水号："+jnlno+
									" | 维保合同号 ："+mcdm.getMaintContractNo()+
									" | 分部长审核状态：未审核";
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  mcdm.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				
				/**===============未审核的,维保合同退保回访  可写权限===============*/
				String sqlkk5="select * from RoleNode where NodeID in('0325') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist5=hs.createSQLQuery(sqlkk5).list();
				if(rlist5!=null && rlist5.size()>0){
					String sqlaa="from LostElevatorReport  " +
							"where submitType='Y' and isnull(fhRem,'')='' "+//提交的并且有回访结果的
							"and maintDivision like '%' order by jnlno ";
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							LostElevatorReport mcdm=(LostElevatorReport)etfclist.get(j);
							
							String jnlno = mcdm.getJnlno();
		
							String urlstr=SysConfig.WEB_APPNAME+"/lostElevatorReportHfAction.do?id="+jnlno+"&method=toDisplayRecord";
							
							String descstr="流水号："+jnlno+
									" | 维保合同号 ："+mcdm.getMaintContractNo()+
									" | 回访状态：未回访";
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  mcdm.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reList;
	}
	/**
	 * 首页=维保报价管理[审核通过]提醒
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getMaintContractQuote(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		List reList = null;

		try {

			reList = new ArrayList();
			
			if(!roleid.equals("A01")){

				/**===============维保报价管理审核通过  可读写权限===============*/
				String sqlaa="from MaintContractQuoteMaster where status=1 and isnull(workisdisplay,'N')='N' " +
						"and attn='" + userid + "' order by billNo";
				List etfclistkk3=hs.createQuery(sqlaa).list();
				if(etfclistkk3!=null && etfclistkk3.size()>0){
					HashMap map = null;
					String url = "";
					for(int e=0;e<etfclistkk3.size();e++){
						MaintContractQuoteMaster qcm=(MaintContractQuoteMaster)etfclistkk3.get(e);
						
						String billno = qcm.getBillNo();
	
						//需要增加跳转路径
						String urlstr=SysConfig.WEB_APPNAME+"/maintContractQuoteAction.do?id="+billno+"&method=toDisplayRecord&workisdisplay=Y";
											
						String descstr="维保报价流水号："+billno +
							" | 甲方单位："+qcm.getCompanyName();
							//" | 申请日期："+qcm.getApplyDate();
						
						map = new HashMap();
						map.put("title", descstr);
						map.put("sid",  billno+String.valueOf(e));
						map.put("url", urlstr);
						map.put("date",  qcm.getOperDate());
						map.put("title2", descstr);
						
						reList.add(map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}
	/**
	 * 首页=开票分部长审核 提醒
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getContractInvoice(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		List reList = null;

		try {

			reList = new ArrayList();
			
			if(!roleid.equals("A01")){
			
				String maintDivision = "00".equals(comid) ? "%" : comid;
				//String maintDivision = comid;
	
				/**===============开票总部长审核  可读写权限===============*/
				String sqlkk="select * from RoleNode where NodeID in('0908') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sqlkk).list();
				if(rlist!=null && rlist.size()>0){
					String sqlaa=" from ContractInvoiceManage " +
							"where submitType='Y' and isnull(auditStatus,'N')='Y' " +
							"and isnull(auditStatus2,'N')='N' and maintDivision like '"+maintDivision+"' order by jnlNo";
					List etfclistkk3=hs.createQuery(sqlaa).list();
					if(etfclistkk3!=null && etfclistkk3.size()>0){
						HashMap map = null;
						String url = "";
						for(int e=0;e<etfclistkk3.size();e++){
							ContractInvoiceManage cim=(ContractInvoiceManage)etfclistkk3.get(e);
							
							String jnlno = cim.getJnlNo();
		
							//需要增加跳转路径
							String urlstr=SysConfig.WEB_APPNAME+"/contractInvoiceManageAudit2Action.do?id="+jnlno+"&method=toPrepareAuditRecord";
												
							String descstr="开票流水号："+jnlno +
								" | 合同号 ："+cim.getContractNo()+
								" | 审核状态：总部长未审核";
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(e));
							map.put("url", urlstr);
							map.put("date",  cim.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				
				/**===============开票财务总监审核  可读写权限===============*/
				String sqlkk4="select * from RoleNode where NodeID in('0911') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist4=hs.createSQLQuery(sqlkk4).list();
				if(rlist4!=null && rlist4.size()>0){
					String sqlaa=" from ContractInvoiceManage " +
							"where submitType='Y' and isnull(auditStatus,'N')='Y' " +
							"and isnull(auditStatus2,'N')='Y' "+
							"and isnull(auditStatus4,'N')='N' " +
							"and maintDivision like '"+maintDivision+"' order by jnlNo";
					List etfclistkk3=hs.createQuery(sqlaa).list();
					if(etfclistkk3!=null && etfclistkk3.size()>0){
						HashMap map = null;
						String url = "";
						for(int e=0;e<etfclistkk3.size();e++){
							ContractInvoiceManage cim=(ContractInvoiceManage)etfclistkk3.get(e);
							
							String jnlno = cim.getJnlNo();
		
							//需要增加跳转路径
							String urlstr=SysConfig.WEB_APPNAME+"/contractInvoiceManageAudit4Action.do?id="+jnlno+"&method=toPrepareAuditRecord";
												
							String descstr="开票流水号："+jnlno +
								" | 合同号 ："+cim.getContractNo()+
								" | 审核状态：财务总监未审核";
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(e));
							map.put("url", urlstr);
							map.put("date",  cim.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				/**===============开票财务审核  可读写权限===============*/
				String sqlkk2="select * from RoleNode where NodeID in('0909') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist2=hs.createSQLQuery(sqlkk2).list();
				if(rlist2!=null && rlist2.size()>0){
					String sqlaa=" from ContractInvoiceManage " +
							"where submitType='Y' and isnull(auditStatus,'N')='Y' " +
							"and isnull(auditStatus4,'N')='Y' "+
							"and isnull(auditStatus3,'N')='N' " +
							"and maintDivision like '"+maintDivision+"' order by jnlNo";
					List etfclistkk3=hs.createQuery(sqlaa).list();
					if(etfclistkk3!=null && etfclistkk3.size()>0){
						HashMap map = null;
						String url = "";
						for(int e=0;e<etfclistkk3.size();e++){
							ContractInvoiceManage cim=(ContractInvoiceManage)etfclistkk3.get(e);
							
							String jnlno = cim.getJnlNo();
		
							//需要增加跳转路径
							String urlstr=SysConfig.WEB_APPNAME+"/contractInvoiceManageAudit3Action.do?id="+jnlno+"&method=toPrepareAuditRecord";
												
							String descstr="开票流水号："+jnlno +
								" | 合同号 ："+cim.getContractNo()+
								" | 审核状态：财务未审核";
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(e));
							map.put("url", urlstr);
							map.put("date",  cim.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				
				/**===============开票管理  可读写权限===============*/
				String sqlkk3="select * from RoleNode where NodeID in('0903') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist3=hs.createSQLQuery(sqlkk3).list();
				if(rlist3!=null && rlist3.size()>0){
					String sqlaa=" from ContractInvoiceManage " +
							"where (submitType='R' or (submitType='Y' and  isnull(auditStatus3,'N')='Y')) " +
							"and isnull(workisdisplay,'N')='N' and operId='"+userid+"' order by jnlNo";
					List etfclistkk3=hs.createQuery(sqlaa).list();
					if(etfclistkk3!=null && etfclistkk3.size()>0){
						HashMap map = null;
						String url = "";
						for(int e=0;e<etfclistkk3.size();e++){
							ContractInvoiceManage cim=(ContractInvoiceManage)etfclistkk3.get(e);
							
							String jnlno = cim.getJnlNo();
							String submit=cim.getSubmitType();
							
							//需要增加跳转路径
							String title="审核通过";
							String urlstr=SysConfig.WEB_APPNAME+"/contractInvoiceManageAction.do?id="+jnlno+"&method=toDisplayRecord&workisdisplay=Y";
							if(submit!=null && submit.trim().equals("R")){
								urlstr=SysConfig.WEB_APPNAME+"/contractInvoiceManageAction.do?id="+jnlno+"&method=toPrepareUpdateRecord";
								title="驳回";
							}
												
							String descstr="开票流水号："+jnlno +
								" | 合同号 ："+cim.getContractNo()+
								" | 审核状态："+title;
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  jnlno+String.valueOf(e));
							map.put("url", urlstr);
							map.put("date",  cim.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}
	/**
	 * 首页=客户回访分部长处理 提醒
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getCustReturnRegister(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		List reList = null;

		try {

			reList = new ArrayList();
			
			if(!roleid.equals("A01")){
			
				//String maintDivision = "00".equals(comid) ? "%" : comid;
				String maintDivision = comid;
				
				/**===============客户回访分部长处理  可读写权限===============*/
				String sqlkk="select * from RoleNode where NodeID in('0204') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sqlkk).list();
				if(rlist!=null && rlist.size()>0){
					String sqlaa=" from CustReturnRegisterMaster " +
							"where ministerHandle='Y' and isProblem='Y' and ISNULL(handleId,'')='' " +
							"and maintDivision like '"+maintDivision+"' order by billno";
					List etfclistkk3=hs.createQuery(sqlaa).list();
					if(etfclistkk3!=null && etfclistkk3.size()>0){
						HashMap map = null;
						String url = "";
						for(int e=0;e<etfclistkk3.size();e++){
							CustReturnRegisterMaster crrm=(CustReturnRegisterMaster)etfclistkk3.get(e);
							
							String billno = crrm.getBillno();
		
							//需要增加跳转路径 
							String urlstr=SysConfig.WEB_APPNAME+"/custReturnRegisterMinisterHandleAction.do?id="+billno+"&method=toPrepareUpdateRecord";
												
							String descstr="联系人："+crrm.getContacts() +
								" | 联系人号码  ："+crrm.getContactPhone()+
								" | 处理状态：分部长未处理";
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  billno+String.valueOf(e));
							map.put("url", urlstr);
							map.put("date",  crrm.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				/**===============回访专员提醒  可读写权限===============*/
				String sqlkk2="select * from RoleNode where NodeID in('0202') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist2=hs.createSQLQuery(sqlkk2).list();
				if(rlist2!=null && rlist2.size()>0){
					String sqlaa=" from CustReturnRegisterMaster " +
							"where ministerHandle='Y' and isProblem='Y' and ISNULL(handleId,'')<>'' " +
							"and operId='"+userid+"' " +
							"and isnull(workisdisplay,'N')='N' order by billno";
					List etfclistkk3=hs.createQuery(sqlaa).list();
					if(etfclistkk3!=null && etfclistkk3.size()>0){
						HashMap map = null;
						String url = "";
						for(int e=0;e<etfclistkk3.size();e++){
							CustReturnRegisterMaster crrm=(CustReturnRegisterMaster)etfclistkk3.get(e);
							
							String billno = crrm.getBillno();
		
							//需要增加跳转路径 
							String urlstr=SysConfig.WEB_APPNAME+"/custReturnRegisterAction.do?id="+billno+"&method=toDisplayRecord&workisdisplay=Y";
												
							String descstr="联系人："+crrm.getContacts() +
								" | 联系人号码  ："+crrm.getContactPhone()+
								" | 处理状态：分部长已处理";
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  billno+String.valueOf(e));
							map.put("url", urlstr);
							map.put("date",  crrm.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}
	/**
	 * 首页=维保合同下达签收
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getContractAssigned(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		
		List reList = new ArrayList();
		Connection conn=null;
		
		try {
			conn = hs.connection();
			DataOperation op = new DataOperation();
			op.setCon(conn);
			
			if(!roleid.equals("A01")){
			
				/**===============维保经理下达签收  可读写权限===============*/
				String sql="select * from RoleNode where NodeID in('0501') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sql).list();
				if(rlist!=null && rlist.size()>0){
						String edate1=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
						String sdate1=DateUtil.getDate(edate1, "MM", -1);//当前日期月份减1 。
	
						String sqlkk="select mcm.maintContractNo as maintContractNo,mcd.salesContractNo as salesContractNo," +
								"mcm.taskSubDate as taskSubDate,mcd.elevatorNo as elevatorNo,mcm.taskSubDate as taskSubDate,mcd.rowid " +
								"from MaintContractMaster mcm,MaintContractDetail mcd " +
								"left join LoginUser b  on b.UserID=mcd.MaintPersonnel " +
								"left join LoginUser c on c.UserID=mcd.assignedSign " +
								"left join MaintenanceWorkPlanMaster mwpm  on mwpm.rowid=mcd.rowid," +
								"StorageID si   where mcd.billNo=mcm.billNo and si.StorageID=mcd.assignedMainStation " +
								"and mcd.assignedMainStation is not null and (mcm.contractStatus = 'ZB' or mcm.contractStatus='XB') " +
								"and mcm.taskSubDate >= '"+sdate1+" 00:00:00' and mcm.taskSubDate <= '"+edate1+" 23:59:59' " +
								"and (mcd.assignedSignFlag = 'N' or mcd.assignedSignFlag is null)  " +
								"and mcd.assignedMainStation like '"+storageid+"%' order by mcd.BillNo,mcd.rowid";
						//System.out.println(">>>>"+sqlkk);
						List listkk = op.queryToList(sqlkk);
						if(listkk!=null && listkk.size()>0){
							HashMap map=null;
							for(int i=0;i<listkk.size();i++){
								map=(HashMap)listkk.get(i);
								
								//需要增加跳转路径
								String urlstr=SysConfig.WEB_APPNAME+"/maintenanceReceiptAction.do?method=toSearchRecord&elevatorNo="+(String)map.get("elevatorno");
								
								String descstr="维保合同号："+(String)map.get("maintcontractno")+
									" | 电梯编号："+(String)map.get("elevatorno")+
									" | 签收状态：未签收";
								
								map = new HashMap();
								map.put("title", descstr);
								map.put("sid",  (String)map.get("rowid")+String.valueOf(i));
								map.put("url", urlstr);
								map.put("date",  (String)map.get("tasksubdate"));
								map.put("title2", descstr);
								
								reList.add(map);
							}
						}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}
	/**
	 * 首页=用户信息审核
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getLoginUserAudit(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		
		List reList = new ArrayList();
		Connection conn=null;
		
		try {
			conn = hs.connection();
			DataOperation op = new DataOperation();
			op.setCon(conn);
			
			if(comid.equals("00")){
				comid="%";
			}
			
			/**===============用户信息审核  可读写权限===============*/
			String sql="select * from RoleNode where NodeID in('7016') and WriteFlag='Y' and RoleID='"+roleid+"'";
			List rlist=hs.createSQLQuery(sql).list();
			if(rlist!=null && rlist.size()>0){
				//String edate1=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
				//String sdate1=DateUtil.getDate(edate1, "MM", -1);//当前日期月份减1 。

				String sqlkk = "select a.userid,a.username,b.ComName from Loginuser a,Company b "
						+ "where a.grcid=b.ComID and isnull(a.auditoperid,'')='' "
						+ "and (isnull(a.newphone,'')<>'' or isnull(a.newemail,'')<>'' or isnull(a.nweidcardno,'')<>'') "
						+ "and a.grcid like '"+comid+"'";
				
				List listkk = op.queryToList(sqlkk);
				if(listkk!=null && listkk.size()>0){
					HashMap map=null;
					for(int i=0;i<listkk.size();i++){
						map=(HashMap)listkk.get(i);
						
						//需要增加跳转路径
						String urlstr=SysConfig.WEB_APPNAME+"/loginUserAuditAction.do?method=toDisplayRecord&id="+(String)map.get("userid");
						
						String descstr="用户名称："+(String)map.get("username")+
							" | 所属分部："+(String)map.get("comname")+
							" | 用户资料有更新,请审核。";
						
						map = new HashMap();
						map.put("title", descstr);
						map.put("sid",  (String)map.get("rowid")+String.valueOf(i));
						map.put("url", urlstr);
						map.put("date",  (String)map.get("tasksubdate"));
						map.put("title2", descstr);
						
						reList.add(map);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}
	/**
	 * 首页=急修安全经理审核
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getHotCalloutAudit(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		
		List reList = new ArrayList();
		Connection conn=null;
		
		try {
			conn = hs.connection();
			DataOperation op = new DataOperation();
			op.setCon(conn);
			
			if(!roleid.equals("A01")){
				/**===============急修安全经理审核  可读写权限===============*/
				String sql="select * from RoleNode where NodeID in('0609') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sql).list();
				if(rlist!=null && rlist.size()>0){
					//String edate1=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
					//String sdate1=DateUtil.getDate(edate1, "MM", -1);//当前日期月份减1 。
	
					//if("00".equals(comid)){
					//	comid="%";
					//}
					
					String orderby=" order by calloutMasterNo asc";
					String sqlkk="exec HL_callhotsearch '%','%','%','8','Y','%','%','%','"+orderby+"','1','%','"+comid+"'";
					
					List listkk = op.queryToList(sqlkk);
					if(listkk!=null && listkk.size()>0){
						HashMap map=null;
						for(int i=0;i<listkk.size();i++){
							map=(HashMap)listkk.get(i);
							
							//需要增加跳转路径
							String urlstr=SysConfig.WEB_APPNAME+"/hotCalloutAuditAction.do?method=toDisplayRecord&typejsp=sh&id="+(String)map.get("calloutmasterno");
							
							String descstr="急修编号："+(String)map.get("calloutmasterno")+
								" | 所属维保站："+(String)map.get("maintstation")+
								" | 审核状态：未审核。";
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  (String)map.get("rowid")+String.valueOf(i));
							map.put("url", urlstr);
							map.put("date",  (String)map.get("tasksubdate"));
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}
	/**
	 * 首页=配件申请单(仓库管理员)处理 
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getAccessoriesRequisition(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		
		List reList = new ArrayList();
		Connection conn=null;
		
		try {
			conn = hs.connection();
			DataOperation op = new DataOperation();
			op.setCon(conn);
			
			if(!roleid.equals("A01")){
				/**===============配件申请单(仓库管理员)  可读写权限===============*/
				String sql="select * from RoleNode where NodeID in('7108') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sql).list();
				if(rlist!=null && rlist.size()>0){
					
					String sqlkstr="select ar.AppNo,ar.isCharges,ar.OperDate,lu.username,si.storagename,c.comfullname "
							+ "from AccessoriesRequisition ar,Loginuser lu,Company c ,Storageid si "
							+ "where c.comid=ar.maintDivision and si.storageid=maintStation "
							+ "and ar.operId=lu.userid and ar.handleStatus='2'"
							+ "order by ar.operdate";
					//System.out.println(sqlkstr);
					List listkk = op.queryToList(sqlkstr);
					if(listkk!=null && listkk.size()>0){
						HashMap map=null;
						for(int i=0;i<listkk.size();i++){
							map=(HashMap)listkk.get(i);
							
							//需要增加跳转路径
							String urlstr=SysConfig.WEB_APPNAME+"/accessoriesRequisitionStoremanAction.do?method=toPrepareUpdateRecord&id="+(String)map.get("appno");
							
							String ischarges=(String)map.get("ischarges");
							if(ischarges!=null && ischarges.trim().equals("Y")){
								ischarges="收费";
							}else{
								ischarges="免费";
							}
							String descstr="初步判断是否收费："+ischarges+
									" | 所属维保站："+(String)map.get("storagename")+
									" | 所属维保分部："+(String)map.get("comfullname");
									//" | 处理状态：未处理。";
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  (String)map.get("appno")+String.valueOf(i));
							map.put("url", urlstr);
							map.put("date",  (String)map.get("operdate"));
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}
	/**
	 * 首页=配件申请单出库办理 
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getAccessoriesRequisitionCkbl(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		
		List reList = new ArrayList();
		Connection conn=null;
		
		try {
			conn = hs.connection();
			DataOperation op = new DataOperation();
			op.setCon(conn);
			
			if(!roleid.equals("A01")){
				String MaintStation="";
				//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
				String sqlss="select * from view_mainstation where roleid='"+roleid+"'";
				List vmlist=hs.createSQLQuery(sqlss).list();
				if(vmlist!=null && vmlist.size()>0){
					MaintStation=storageid;
				}
				/**===============配件申请单出库办理  可读写权限===============*/
				String sql="select * from RoleNode where NodeID in('7115') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sql).list();
				if(rlist!=null && rlist.size()>0){
					
					String sqlkstr="select ar.AppNo,ar.ckiswc,ar.SingleNo,ar.OldNo "
							+ "from AccessoriesRequisition ar "
							+ "where ar.handleStatus in('4','5')"
							+ "and isnull(ar.ckiswc,'N')='N' "
							+ "and ar.MaintStation like '" + MaintStation.trim() + "%'"
							+ "order by ar.operdate";
					System.out.println(sqlkstr);
					List listkk = op.queryToList(sqlkstr);
					if(listkk!=null && listkk.size()>0){
						HashMap map=null;
						for(int i=0;i<listkk.size();i++){
							map=(HashMap)listkk.get(i);
							String appno=(String)map.get("appno");
							String singleno=(String)map.get("singleno");
							String oldno=(String)map.get("oldno");
							
							//需要增加跳转路径
							String urlstr=SysConfig.WEB_APPNAME+"/accessoriesRequisitionCkblAction.do?method=toPrepareUpdateRecord&id="+appno;

							String descstr="急修/保养单号："+singleno+
									" | 出库办理：未完成"+
									" | 旧件编号："+oldno; 
							
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  (String)map.get("appno")+String.valueOf(i));
							map.put("url", urlstr);
							map.put("date",  (String)map.get("operdate"));
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reList;
	}
	
	
	/**
	 * 首页=合同交接资料派工 [驳回]
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getTransferAssign(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		
		List reList = null;
		HashMap map = null;
		try {
			reList = new ArrayList();
			
			if(!roleid.equals("A01")){
				//String maintDivision = "00".equals(comid) ? "%" : comid;
				String maintDivision = comid;
				
				/**===============被驳回的,合同交接资料派工  可写权限===============*/
				String sqlkk="select * from RoleNode where NodeID in('1101') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sqlkk).list();
				if(rlist!=null && rlist.size()>0){
					String sqlaa="from ContractTransferMaster " +
							"where submitType='R' and maintDivision='"+maintDivision+"' order by billno ";
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							ContractTransferMaster cm=(ContractTransferMaster)etfclist.get(j);
							
							String billno = cm.getBillNo();
							String elevatorno=cm.getElevatorNo();
							
							String typename="派工提交标志：驳回";
							String urlstr=SysConfig.WEB_APPNAME+"/ContractTransferAssignAction.do?id="+billno+"&method=toPrepareUpdateRecord";
							
							String descstr="流水号："+billno+
									" | 电梯编号 ："+elevatorno+
									" | "+typename;
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  billno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  cm.getOperDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reList;
	}
	
	/**
	 * 首页=合同交接资料上传 [驳回，未上传]
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getTransferUpload(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		
		List reList = null;
		HashMap map = null;
		try {
			reList = new ArrayList();
			
			if(!roleid.equals("A01")){
				//String maintDivision = "00".equals(comid) ? "%" : comid;
				String maintStation = storageid;
				
				String sqlkk="select * from RoleNode where NodeID in('1102') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sqlkk).list();
				if(rlist!=null && rlist.size()>0){
					String sqlaa="from ContractTransferMaster " +
							"where transfeSubmitType<>'Y' and submitType='Y'  ";
					
					if ("A03".equals(roleid)||"A48".equals(roleid)) {
						sqlaa += " and maintStation='"+maintStation+"' and isnull(isTrans,'N') = 'N' ";
					}else {
						sqlaa += " and isTrans = 'Y' and wbgTransfeId = '"+userid+"' ";
					}
					
					sqlaa += " order by billno ";
					
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							ContractTransferMaster cm=(ContractTransferMaster)etfclist.get(j);
							
							String billno = cm.getBillNo();
							String elevatorno=cm.getElevatorNo();
							String typename="";
							
							if(cm.getTransfeSubmitType()!=null&&"N".equals(cm.getTransfeSubmitType())){
								
								typename="上传提交标志：未上传";
							}else if(cm.getTransfeSubmitType()!=null&&"R".equals(cm.getTransfeSubmitType())){
								
								typename="上传提交标志：驳回";
							}
							
							
							String urlstr=SysConfig.WEB_APPNAME+"/ContractTransferUploadAction.do?id="+billno+"&method=toPrepareAddRecord&mainenter=Y";
							
							String descstr="流水号："+billno+
									" | 电梯编号 ："+elevatorno+
									" | "+typename;
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  billno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  cm.getTransfeDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return reList;
	}
	
	/**
	 * 首页=合同交接资料审核[经理已审核，已上传]
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getTransferAudit(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		
		List reList = null;
		HashMap map = null;
		try {
			reList = new ArrayList();
			
			if(!roleid.equals("A01")){
				String maintDivision = "00".equals(comid) ? "%" : comid;
				//String maintDivision = comid;
				
				String sqlkk="select * from RoleNode where NodeID in('1103') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sqlkk).list();
				if(rlist!=null && rlist.size()>0){
					String sqlaa="from ContractTransferMaster " +
							"where isnull(AuditStatus,'N')<>'Y' and ((transfeSubmitType = 'Y' and isnull(isTrans,'N') = 'N') or (transfeSubmitType = 'Y' and isnull(isTrans,'N') = 'Y' and auditStatus2='Y')) and submitType='Y' "
							+ "and maintDivision like '"+maintDivision+"' order by billno ";
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							ContractTransferMaster cm=(ContractTransferMaster)etfclist.get(j);
							
							String billno = cm.getBillNo();
							String elevatorno=cm.getElevatorNo();
							String typename="审核状态：未审核";
								
							
							
							String urlstr=SysConfig.WEB_APPNAME+"/ContractTransferAuditAction.do?id="+billno+"&method=toPrepareAddRecord";
							
							String descstr="流水号："+billno+
									" | 电梯编号 ："+elevatorno+
									" | "+typename;
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  billno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  cm.getAuditDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return reList;
	}
	
	/**
	 * 首页=合同交接资料反馈查看[反馈日期]
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getTransferFeedBack(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		
		List reList = null;
		HashMap map = null;
		try {
			reList = new ArrayList();
			
			if(!roleid.equals("A01")){
				String maintDivision = "00".equals(comid) ? "%" : comid;
				//String maintDivision = comid;
				
				String sqlkk="select * from RoleNode where NodeID in('1104') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sqlkk).list();
				if(rlist!=null && rlist.size()>0){
					String sqlaa="select cm.billNo,cm.elevatorNo,cb.OperDate "
					+"from ContractTransferMaster cm "
					+"join ContractTransferFeedback cb "
					+"on cm.BillNo=cb.BillNo and cm.workisdisplay<>'Y' and cm.maintDivision like '"+maintDivision+"' "
					+"and cb.OperDate=(select max(cfb.OperDate) from ContractTransferFeedback cfb where cm.BillNo=cfb.BillNo) ";
					List etfclist=hs.createSQLQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							Object[] objs=(Object[])((Object)etfclist.get(j));
							
							String billno = (String) objs[0];
							String elevatorno=(String) objs[1];
							String operdate=(String) objs[2];
							
							
							
							String urlstr=SysConfig.WEB_APPNAME+"/ContractTransferFeedBackAction.do?id="+billno+"&method=toDisplayRecord&workisdisplay=Y";
							
							String descstr="流水号："+billno+
									" | 电梯编号 ："+elevatorno+
									" | 反馈日期： "+operdate;
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  billno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  operdate);
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return reList;
	}
	
	/**
	 * 首页=合同交接资料经理审核[已上传，转派，经理未审核]
	 * 
	 * @param key
	 * @param hs
	 * @return
	 * @throws HibernateException
	 */
	private List getTransferManagerAudit(HttpServletRequest request,
			Session hs, String comid,String roleid,String storageid,String userid) throws HibernateException {
		
		List reList = null;
		HashMap map = null;
		try {
			reList = new ArrayList();
			
			if(!roleid.equals("A01")){
				String maintDivision = "00".equals(comid) ? "%" : comid;
				//String maintDivision = comid;
				
				String sqlkk="select * from RoleNode where NodeID in('1105') and WriteFlag='Y' and RoleID='"+roleid+"'";
				List rlist=hs.createSQLQuery(sqlkk).list();
				if(rlist!=null && rlist.size()>0){
					String sqlaa="from ContractTransferMaster " +
							"where isnull(AuditStatus2,'N')<>'Y' and transfeSubmitType = 'Y' and isTrans = 'Y' "
							+ "and MaintStation like '"+storageid+"' order by billno ";
					List etfclist=hs.createQuery(sqlaa).list();
					if(etfclist!=null && etfclist.size()>0){
						String url = "";
						for(int j=0;j<etfclist.size();j++){
							ContractTransferMaster cm=(ContractTransferMaster)etfclist.get(j);
							
							String billno = cm.getBillNo();
							String elevatorno=cm.getElevatorNo();
							String typename="审核状态：未审核";
								
							
							
							String urlstr=SysConfig.WEB_APPNAME+"/ContractTransferManagerAuditAction.do?id="+billno+"&method=toPrepareAddRecord";
							
							String descstr="流水号："+billno+
									" | 电梯编号 ："+elevatorno+
									" | "+typename;
							map = new HashMap();
							map.put("title", descstr);
							map.put("sid",  billno+String.valueOf(j));
							map.put("url", urlstr);
							map.put("date",  cm.getAuditDate());
							map.put("title2", descstr);
							
							reList.add(map);
						}
					}
				}
				
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return reList;
	}
	
	
	public String toXML(List list) {
		StringBuffer sb=new StringBuffer();
		if(list!=null && list.size()>0){
			sb.append("<?xml version='1.0' encoding='gbk'?>");
			sb.append("<dataset>");
			
			int len=list.size();
			for(int i=0;i<len;i++){
				HashMap mp=(HashMap)list.get(i);
				sb.append("<row>");
				sb.append("<sid>").append((String)mp.get("sid")).append("</sid>");
				sb.append("<title>").append(escapeXML((String)mp.get("title"))).append("</title>");
				sb.append("<url>").append(escapeXML((String)mp.get("url"))).append("</url>");
				sb.append("<date>").append((String)mp.get("date")).append("</date>");
				sb.append("<title2>").append(escapeXML((String)mp.get("title2"))).append("</title2>");
				sb.append("</row>");
			}
			sb.append("</dataset>");
		}
		////System.out.println(sb.toString());
		return sb.toString();
	}
	
	public static String escapeXML (String dangerous){
		if(dangerous==null){
			return dangerous;
		}
		if( dangerous.indexOf("&")  == -1 && 
            dangerous.indexOf("\"") == -1 && 
            dangerous.indexOf("'") == -1 && 
            dangerous.indexOf("<")  == -1 && 
            dangerous.indexOf(">") == -1    
        ){
            return dangerous;
        } else {
            dangerous = dangerous.replaceAll("&" , "&amp;" );
            dangerous = dangerous.replaceAll("\"", "&quot;");
            dangerous = dangerous.replaceAll("'" , "&apos;");
            dangerous = dangerous.replaceAll("<" , "&lt;"  );
            dangerous = dangerous.replaceAll(">" , "&gt;"  );
            return dangerous;
        }
    }

}