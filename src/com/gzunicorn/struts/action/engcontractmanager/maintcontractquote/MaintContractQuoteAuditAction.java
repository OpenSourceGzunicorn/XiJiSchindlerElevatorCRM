package com.gzunicorn.struts.action.engcontractmanager.maintcontractquote;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
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

import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotedetail.MaintContractQuoteDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquoteprocess.MaintContractQuoteProcess;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class MaintContractQuoteAuditAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintContractQuoteAuditAction.class);

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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "maintcontractquoteaudit", null);
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

		request.setAttribute("navigator.location", "维保报价申请审核 >> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session,"maintContractQuoteAuditList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fMaintContractQuoteAudit");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		cache.updateTable(table);
		table.setSortColumn("billNo");
		table.setIsAscending(false);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE) || action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else if (action.equals("Submit")) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String billNo = tableForm.getProperty("billNo");// 甲方单位名称
		String companyName = tableForm.getProperty("companyName");// 甲方单位名称
		String status = tableForm.getProperty("status");// 流程状态
		String maintDivision = tableForm.getProperty("maintDivision");// 甲方单位id
		String submitType = tableForm.getProperty("submitType");// 提交标志
		String salesContractNo = tableForm.getProperty("salesContractNo");// 销售合同号
		String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号
		String isscht = tableForm.getProperty("isscht");//是否生成合同
		
		//默认显示已提交记录
		if (submitType == null || submitType.equals("")) {
			submitType = "Y"; // 已提交
			tableForm.setProperty("submitType", submitType);
		}	
		
		//第一次进入页面时根据登陆人初始化所属维保分部
		List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
		if(maintDivision == null || maintDivision.equals("")){
			Map map = (Map)maintDivisionList.get(0);
			maintDivision = (String)map.get("grcid");
		}

		Session hs = null;
		SQLQuery query = null;
		try {
			String roleid=userInfo.getRoleID();
			String maintStation="%";
			//A03  维保经理  只能看自己维保站下面的合同
			if(roleid!=null && roleid.trim().equals("A03")){
				maintStation=userInfo.getStorageId();
			}
			
			hs = HibernateUtil.getSession();

			String sql = "select a.*,b.username,c.comname,e.typename,s.storagename "+
					" from MaintContractQuoteMaster a," +
					" Loginuser b,Company c,ViewFlowStatus e,StorageID s" + 
					" where a.attn = b.userid"+
					" and a.maintDivision = c.comid"+
					" and a.maintStation = s.storageid"+
					" and a.status = e.typeid"+
					" and a.maintStation like '"+maintStation+"'";
			
			if (isscht != null && !isscht.equals("")) {
				//生成合同 是
				if("Y".equals(isscht)){
					sql += " and a.billno in(select isnull(quoteBillNo,'-') from MaintContractMaster)";
				}else if("N".equals(isscht)){
					//生成合同 否
					sql += " and a.billno not in(select isnull(quoteBillNo,'-') from MaintContractMaster)";
				}
			}
			
			if (billNo != null && !billNo.equals("")) {
				sql += " and a.billNo like '%"+billNo.trim()+"%'";
			}	
			if (companyName != null && !companyName.equals("")) {
				sql += " and a.companyName like '%" + companyName.trim() + "%'";
			}
			if (status != null && !status.equals("")) {
				sql += " and a.status = '" + Integer.valueOf(status) + "'";
			}
			if (maintDivision != null && !maintDivision.equals("")) {
				sql += " and c.comid like '" + maintDivision.trim() + "'";
			}
			if (submitType != null && !submitType.equals("")) {
				sql += " and a.submitType like '" + submitType.trim() + "'";
			}
			if (salesContractNo != null && !salesContractNo.equals("")) {
				sql += " and a.billNo in(select distinct billNo from MaintContractQuoteDetail where salesContractNo like '%"+salesContractNo.trim()+"%')";
			}
			if (elevatorNo != null && !elevatorNo.equals("")) {
				sql += " and a.billNo in(select distinct billNo from MaintContractQuoteDetail where elevatorNo like '%"+elevatorNo.trim()+"%')";
			}
			
			if (table.getIsAscending()) {
				sql += " order by " + table.getSortColumn() + " asc";
			} else {
				sql += " order by " + table.getSortColumn() + " desc";
			}
			
			//System.out.println(sql);
			
			query = hs.createSQLQuery(sql);					
			query.addEntity("a",MaintContractQuoteMaster.class);
			query.addScalar("username");
			query.addScalar("comname");
			query.addScalar("typename");
			query.addScalar("storagename");
			
			table.setVolume(query.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List list = query.list();
			List maintContractQuoteAuditList = new ArrayList();
			for (Object object : list) {
				Object[] objs = (Object[])object;
				Map map = BeanUtils.describe(objs[4]); // MaintContractQuoteMaster
				map.put("attnName", String.valueOf(objs[0])); // 经办人名称
				map.put("maintDivisionName", String.valueOf(objs[1])); // 分部名称
				map.put("statusName", String.valueOf(objs[2])); // 流程状态
				map.put("storagename", String.valueOf(objs[3])); // 维保站名称
				
				//生成合同 是
				if("Y".equals(isscht)){
					map.put("iscontract", "是");
				}else if("N".equals(isscht)){
					//生成合同 否
					map.put("iscontract", "否");
				}else{
					String billno=(String)map.get("billNo");
					String sqlmas="select quoteBillNo from MaintContractMaster where quoteBillNo='"+billno.trim()+"'";
					List ecmlist=hs.createSQLQuery(sqlmas).list();
					if(ecmlist!=null && ecmlist.size()>0){
						map.put("iscontract", "是");
					}else{
						map.put("iscontract", "否");
					}
				}
				
				maintContractQuoteAuditList.add(map);
			}

			table.addAll(maintContractQuoteAuditList);
			session.setAttribute("maintContractQuoteAuditList", table);
			// 分部下拉框列表
			request.setAttribute("maintDivisionList",Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
			// 流程状态下拉框列表
			request.setAttribute("processStatusList", bd.getProcessStatusList());
			// 获取流程名称
			request.setAttribute("flowname", WorkFlowConfig.getProcessDefine("enginequotemasterProcessName"));
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
		forward = mapping.findForward("maintContractQuoteAuditList");

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
		request.setAttribute("navigator.location",
				messages.getMessage(locale, navigation));
	}

	/**
	 * 点击查看的方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "维保报价申请审核 >> 查看");
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;

		String id = request.getParameter("id");

		Session hs = null;
		List maintContractQuoteDetailList = null;

		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from MaintContractQuoteMaster where billNo = '"+ id + "'");
				List list = query.list();
				if (list != null && list.size() > 0) {

					// 主信息
					MaintContractQuoteMaster bean = (MaintContractQuoteMaster) list.get(0);
					List pdlist=bd.getPullDownAllList("MaintContractQuoteMaster_PaymentMethodApply");
					String pmaname=bd.getOptionName(bean.getPaymentMethodApply(), pdlist);
					bean.setR4(pmaname);
					//合同附件内容申请
					String ccastrname="";
					String ccastr=bean.getContractContentApply();
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
					bean.setContractContentApply(ccastrname);
					
					request.setAttribute("attnName", bd.getName(hs, "Loginuser","username", "userid",bean.getAttn())); //经办人名称
					request.setAttribute("maintDivisionName", bd.getName(hs, "Company", "comname", "comid",bean.getMaintDivision())); //维保分部名称					
					request.setAttribute("maintStationName", bd.getName(hs, "Storageid","storageName", "storageID", bean.getMaintStation())); //维保站名称
					request.setAttribute("maintContractQuoteBean", bean);

					// 电梯信息明细列表
					query = hs.createQuery("from MaintContractQuoteDetail where billNo = '"+ id + "'");
					List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型列表
					maintContractQuoteDetailList = query.list();
					for (Object object : maintContractQuoteDetailList) {
						MaintContractQuoteDetail detail = (MaintContractQuoteDetail) object;
						detail.setElevatorType(bd.getOptionName(detail.getElevatorType(), elevatorTypeList));
						detail.setR4(bd.getName("Pulldown", "pullname", "id.pullid",detail.getSignWay()));//签署方式
					}
					request.setAttribute("maintContractQuoteDetailList",maintContractQuoteDetailList);
					
					//审批流程信息
					query = hs.createQuery("from MaintContractQuoteProcess where billNo = '"+ id + "' order by itemId");
					List processApproveList = query.list();
					for (Object object : processApproveList) {
						MaintContractQuoteProcess process = (MaintContractQuoteProcess) object;
						process.setUserId(bd.getName(hs, "Loginuser", "username", "userid", process.getUserId()));
					}
					request.setAttribute("processApproveList",processApproveList);
					
					
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
				//获取已经上传的附件
				List filelst=this.FileinfoList(hs, id.trim(), "MaintContractQuoteMaster");
				request.setAttribute("updatefileList", filelst);
				
				request.setAttribute("ContractContentList", bd.getPullDownList("MaintContractQuoteMaster_ContractContentApply"));
				
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

			request.setAttribute("display", "yes");
			forward = mapping.findForward("maintContractQuoteAuditDisplay");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
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
		MaintContractQuoteMaster master = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				
				master = (MaintContractQuoteMaster) hs.get(MaintContractQuoteMaster.class, id);
				if (master == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
				
				// 保存主信息
				master.setSubmitType("N"); //提交标志
				master.setStatus(new Integer(WorkFlowConfig.State_NoStart)); // 流程状态,未启动
				master.setTokenId(new Long(0));// 流程令牌
				master.setProcessName("");// 环节名称
				hs.save(master);
				
				// 保存审批流程相关信息
				MaintContractQuoteProcess process = new MaintContractQuoteProcess();
				process.setBillNo(master.getBillNo());//报价流水号
				process.setTaskId(new Integer(0));//任务号
				process.setTaskName("重启流程");//任务名称
				process.setTokenId(new Long(0));//流程令牌
				process.setUserId(userInfo.getUserID());//操作人
				process.setDate1(CommonUtil.getToday());//操作日期
				process.setTime1(CommonUtil.getTodayTime());//操作时间
				process.setApproveResult("重启流程");
				process.setApproveRem("重新启动流程");
				hs.save(process);
				
				tx.commit();
			} catch (Exception e1) {
				e1.printStackTrace();				
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

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return mapping.findForward("returnList");
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
