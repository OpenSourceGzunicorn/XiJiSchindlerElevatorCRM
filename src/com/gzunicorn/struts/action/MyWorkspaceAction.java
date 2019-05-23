package com.gzunicorn.struts.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.SysConfig;

import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.hibernate.sysmanager.Workspacebaseproperty;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

/**
 * 我的工作区编码
 * 
 * @version 1.0
 * @author Feige
 * 
 */
public class MyWorkspaceAction extends DispatchAction {

	Log log = LogFactory.getLog(MyWorkspaceAction.class);

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
			HttpServletRequest request, HttpServletResponse response) {

		ActionForward forward = null;

		String name = request.getParameter("method");
		if (name == null || name.equals("") || "toSearchRecord".equals(name)) {
			name = "toIndex";
		}

		DebugUtil.printDoCommonAction("MyWorkspaceAction", name, "start");
		try {
			forward = dispatchMethod(mapping, form, request, response, name);
		} catch (Exception e) {
			DebugUtil.print(e);
		}

		DebugUtil.printDoCommonAction("MyWorkspaceAction", name, "end");

		return forward;
	}

	/**
	 * 查询记录的FORWARD，也是默认的forward
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toIndex(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, HibernateException {

		request.setAttribute("navigator.location", "我的工作区");
		Session hs = null;
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		ViewLoginUserInfo info = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);

		String roleid = info.getRoleID();

		//根据角色配置列出
		//String sql = "select b from Wsroleconfig as a,Workspacebaseproperty as b "
		//		+ "where b.enabledflag='Y' and a.id.wsid=b.wsid and a.id.roleid='" + roleid + "'";

		//全部列出
		//String sql = "select b from Workspacebaseproperty as b where b.enabledflag='Y'";


		List jsList=null;
		List propertyList = null;
		List divList = null;
		Map divmap=null;

		try {
			hs = HibernateUtil.getSession();
			
			String wskeys=SysConfig.TOGET_DUTYS;

			//有可写权限 节点功能
			/**
			0604 	急修流程管理
			0601	咨询投诉登记
			0605 	咨询投诉派工
			0602  	咨询投诉处理
			0406	安装维保交接电梯情况管理
			7110 	安装维保交接电梯情况登记
			7111	维保质量检查登记
			
			0207	客户拜访反馈
			0202	客户回访管理
			0204	客户回访分部长处理
			
			0304	维保合同管理
			--0307	维保合同审核
			0309	维保委托合同管理
			--0310	维保委托合同审核
			0311	维保合同延保管理
			0312	维保合同延保审核
			
			0313 	维保合同退保管理
			0314 	维保合同退保审核
			0321 	维保合同退保分部长审核
			0324 	维保合同退保总部长审核
			0325 	维保合同退保回访
			
			0301	维保报价管理
			
			0903	开票管理
			0908	开票总部长审核
			0911	财务总监审核
			0909	开票财务审核
			
			0501	维保合同下达签收
			7016	用户信息审核
			0609 	急修安全经理审核
			
			7108	配件申请单(仓库管理员)处理
			7115	配件申请单出库办理
			1101              合同交接资料派工 
			1102              合同交接资料上传 
			1103              合同交接资料审核 
			1104              合同交接资料反馈查看 
			1105              合同交接资料经理审核 
			*/
			
			String sql2="select NodeID,WriteFlag from RoleNode " +
					"where NodeID in('0601','0602','0604','0605','0406','7110','7111','0207','0202','0204'," +
					"'0304','0309','0311','0312','0313','0314','0321','0324','0325','0301','0903','0908','0909','0911','0501',"+
					"'7016','0609','7108','7115','1101','1102','1103','1104','1105') " +
					"and WriteFlag='Y' and RoleID='"+roleid+"'";
			List rlist2=hs.createSQLQuery(sql2).list();
			if(rlist2!=null && rlist2.size()>0){
				Object[] objs=null;
				String nodeid="";
				for(int n=0;n<rlist2.size();n++){
					objs=(Object[])rlist2.get(n);
					nodeid=(String)objs[0];
					
					//有可读写 配件申请单出库办理   节点功能
					if(nodeid!=null && (nodeid.trim().equals("7115"))){
						wskeys+="','"+SysConfig.TOGET_AccessoriesRequisitionCkbl;
					}
					//有可读写 配件申请单(仓库管理员)处理  节点功能
					if(nodeid!=null && (nodeid.trim().equals("7108"))){
						wskeys+="','"+SysConfig.TOGET_AccessoriesRequisition;
					}
					//有可读写 急修安全经理审核  节点功能
					if(nodeid!=null && (nodeid.trim().equals("0609"))){
						wskeys+="','"+SysConfig.TOGET_HotCalloutAudit;
					}
					//有可读写 用户信息审核 节点功能
					if(nodeid!=null && (nodeid.trim().equals("7016"))){
						wskeys+="','"+SysConfig.TOGET_LoginUserAudit;
					}
					//有可读写 维保合同下达签收 节点功能
					if(nodeid!=null && (nodeid.trim().equals("0501"))){
						wskeys+="','"+SysConfig.TOGET_ContractAssigned;
					}
					//有可读写 客户回访分部长处理 节点功能
					if(nodeid!=null && (nodeid.trim().equals("0202") || nodeid.trim().equals("0204"))){
						wskeys+="','"+SysConfig.TOGET_CustReturnRegister;
					}
					//有可读写 开票分部长审核 节点功能
					if(nodeid!=null && (nodeid.trim().equals("0903") || nodeid.trim().equals("0908") 
							|| nodeid.trim().equals("0909") || nodeid.trim().equals("0911"))){
						wskeys+="','"+SysConfig.TOGET_ContractInvoice;
					}
					//有可读写权限维保报价管理节点功能
					if(nodeid!=null && nodeid.trim().equals("0301")){
						wskeys+="','"+SysConfig.TOGET_MaintContractQuoteMaster;
					}
					//有可写权限 维保合同退保管理 [审核,驳回]
					if(nodeid!=null && (nodeid.trim().equals("0313") 
							|| nodeid.trim().equals("0314") || nodeid.trim().equals("0321")
							|| nodeid.trim().equals("0324") || nodeid.trim().equals("0325") )){
						wskeys+="','"+SysConfig.TOGET_LostElevator;
					}
					//有可读写权限 维保合同管理，维保委托合同管理 [审核,驳回]
					if(nodeid!=null && (nodeid.trim().equals("0304") || nodeid.trim().equals("0307")
							|| nodeid.trim().equals("0309") || nodeid.trim().equals("0310"))){
						//A55  厂检文员  不显示
						if(!"A55".equals(roleid)){
							wskeys+="','"+SysConfig.TOGET_ContractMaster;
						}
					}
					//有可读写权限 维保合同延保管理 [审核,驳回]
					if(nodeid!=null && (nodeid.trim().equals("0311") || nodeid.trim().equals("0312"))){
						wskeys+="','"+SysConfig.TOGET_ContractDelay;
					}
					//有可读写权限急修管理节点功能
					if(nodeid!=null && nodeid.trim().equals("0604")){
						//A55  厂检文员  不显示
						if(!"A55".equals(roleid)){
							wskeys+="','"+SysConfig.TOGET_Fault;
						}
					}
					//有可读写权限 全质办咨询投诉管理 [驳回,派工,处理]
					if(nodeid!=null && (nodeid.trim().equals("0605") 
							|| nodeid.trim().equals("0602") || nodeid.trim().equals("0601"))){
						wskeys+="','"+SysConfig.TOGET_Complaints;
					}
					//有可读写权限 维保质量检查  登记
					if(nodeid!=null && nodeid.trim().equals("7111")){
						wskeys+="','"+SysConfig.TOGET_QualityCheck;
					}
					//有可读写权限 安装维保交接电梯情况 [驳回，接收]
					if(nodeid!=null && (nodeid.trim().equals("0406") || nodeid.trim().equals("7110"))){
						wskeys+="','"+SysConfig.TOGET_ElevatorTrans;
					}
					//有可读写客户拜访反馈节点功能
					if(nodeid!=null && nodeid.trim().equals("0207")){
						wskeys+="','"+SysConfig.TOGET_CustomerVisit;
					}
					//有可读写合同交接资料派工节点功能
					if(nodeid!=null && nodeid.trim().equals("1101")){
						wskeys+="','"+SysConfig.TOGET_TransferAssign;
					}
					//有可读写合同交接资料上传节点功能
					if(nodeid!=null && nodeid.trim().equals("1102")){
						wskeys+="','"+SysConfig.TOGET_TransferUpload;
					}
					//有可读写合同交接资料审核节点功能
					if(nodeid!=null && nodeid.trim().equals("1103")){
						wskeys+="','"+SysConfig.TOGET_TransferAudit;
					}
					//有可读写合同交接资料反馈查看节点功能
					if(nodeid!=null && nodeid.trim().equals("1104")){
						wskeys+="','"+SysConfig.TOGET_TransferFeedBack;
					}
					//有可读写合同交接资料经理审核节点功能
					if(nodeid!=null && nodeid.trim().equals("1105")){
						wskeys+="','"+SysConfig.TOGET_TransferManagerAudit;
					}
				}
			}
			
			//有可读写权限维保合同节点功能 0304 维保合同到期提醒,不提醒 A02  维保分部长 ，A03  维保经理  
			String sql3="select NodeID,WriteFlag from RoleNode where NodeID in('0304') " +
					"and RoleID not in('A02','A03') " +
					"and RoleID='"+roleid+"'";
			List rlist3=hs.createSQLQuery(sql3).list();
			if(rlist3!=null && rlist3.size()>0){
				//A55  厂检文员  不显示
				if(!"A55".equals(roleid)){
					wskeys+="','"+SysConfig.TOGET_Contract;
				}
			}
			
			//根据有节点可读写权限，进行显示
			String sql = "select b from Workspacebaseproperty as b " +
					"where b.enabledflag='Y' and b.wskey in('"+wskeys+"') order by numno";
			propertyList = hs.createQuery(sql).list();
			if (propertyList != null && !propertyList.isEmpty()) {
				jsList=new ArrayList();
				divList=new ArrayList();
				for (int i = 0; i < propertyList.size(); i++) {
					divmap = new HashMap();
					Workspacebaseproperty config=(Workspacebaseproperty)propertyList.get(i);
					jsList.add(config.getJsfuname());
					divmap.put("id", config.getWsid());
					divmap.put("wsid", config.getWsid());
					divmap.put("wskey", config.getWskey());
					divmap.put("title", config.getTitle());
					divmap.put("link", config.getLink());
					divmap.put("tip", config.getTip());
					divmap.put("divid", config.getDivid());
					divmap.put("jsfullname", config.getJsfuname());
	
					if(i%2==0){
						divmap.put("float", "left");
					}else{
						divmap.put("float", "right");
					}
					divList.add(divmap);
				}
			}
			
			request.setAttribute("jsList", jsList);
			request.setAttribute("divList", divList);

			forward = mapping.findForward("toIndex");
		} catch (DataStoreException e) {
			e.printStackTrace();
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
		return forward;
	}
	
	
	public ActionForward toIndexMain(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, HibernateException {

		request.setAttribute("navigator.location", "我的工作区");
		Session hs = null;
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		ViewLoginUserInfo info = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);

		String roleid = info.getRoleID();

		String sql = "select b from Wsroleconfig as a,Workspacebaseproperty as b "
				+ "where b.enabledflag='Y' and a.id.wsid=b.wsid and a.id.roleid='" + roleid + "'";

		//System.out.println(sql);

	 
		List jsList=null;
		List propertyList = null;
		List divList = null;
		Map divmap=null;

		try {
			hs = HibernateUtil.getSession();
			propertyList = hs.createQuery(sql).list();
			if (propertyList != null && !propertyList.isEmpty()) {
				jsList=new ArrayList();
				divList=new ArrayList();
				
				for (int i = 0; i < propertyList.size(); i++) {
					divmap = new HashMap();
					Workspacebaseproperty config=(Workspacebaseproperty)propertyList.get(i);
					jsList.add(config.getJsfuname());
					divmap.put("id", config.getWsid());
					divmap.put("wsid", config.getWsid());
					divmap.put("wskey", config.getWskey());
					divmap.put("title", config.getTitle());
					divmap.put("link", config.getLink());
					divmap.put("tip", config.getTip());
					divmap.put("divid", config.getDivid());
					divmap.put("jsfullname", config.getJsfuname());
	
					
					if(i%2==0){
						divmap.put("float", "left");
					}else{
						divmap.put("float", "right");
					}
					divList.add(divmap);
				}
			}
			
			request.setAttribute("jsList", jsList);
			request.setAttribute("divList", divList);
			
			//System.out.println(">>>>>>>>>>");
			//System.out.println(jsList);
			//System.out.println(divList);
			//System.out.println(">>>>>>>>>>");
			
			forward = mapping.findForward("toIndexMain");
		} catch (DataStoreException e) {
			e.printStackTrace();
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
		return forward;
	}
}
