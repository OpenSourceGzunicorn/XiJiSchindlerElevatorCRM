package com.gzunicorn.struts.action.contracttransfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
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
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;
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
import com.gzunicorn.file.HandleFile;
import com.gzunicorn.file.HandleFileImpA;
import com.gzunicorn.hibernate.basedata.Fileinfo;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.basedata.elevatorsales.ElevatorSalesInfo;
import com.gzunicorn.hibernate.contractpaymentmanage.contractpaymentmanage.ContractPaymentManage;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferFeedback;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferFeedbackFileinfo;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferFileTypes;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferFileinfo;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferMaster;
import com.gzunicorn.hibernate.custregistervisitplan.customervisitplandetail.CustomerVisitPlanDetail;
import com.gzunicorn.hibernate.custregistervisitplan.customervisitplanmaster.CustomerVisitPlanMaster;
import com.gzunicorn.hibernate.engcontractmanager.ContractFileinfo.ContractFileinfo;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotedetail.MaintContractQuoteDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.infomanager.handoverelevatorcheckfileinfo.HandoverElevatorCheckFileinfo;
import com.gzunicorn.hibernate.infomanager.handoverelevatorcheckitemregister.HandoverElevatorCheckItemRegister;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.struts.action.query.SearchElevatorSaleAction;
import com.gzunicorn.workflow.bo.JbpmExtBridge;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class ContractTransferManagerAuditAction extends DispatchAction {

	Log log = LogFactory.getLog(ContractTransferManagerAuditAction.class);
	
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
	 * 合同交接资料经理审核
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
			SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "ContractTransferManagerAudit", null);
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
	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("navigator.location","合同交接资料经理审核 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
//			try {
//				response = toExcelRecord(form,request,response);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			forward = mapping.findForward("exportExcel");
//			tableForm.setProperty("genReport","");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "contractTransferManagerAuditList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fContractTransferManagerAudit");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("cm.billNo");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			}else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			}else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String billNo = tableForm.getProperty("billNo");// 流水号
			String maintContractNo = tableForm.getProperty("maintContractNo");// 维保合同号
			String salesContractNo = tableForm.getProperty("salesContractNo");// 销售合同号
			String companyName = tableForm.getProperty("companyId");// 甲方单位
			String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号
			String maintDivision = tableForm.getProperty("maintDivision");// 所属维保部		
			String maintstation = tableForm.getProperty("maintStation");// 所属维保站	
			String auditStatus2 = tableForm.getProperty("auditStatus2");// 审核状态
			
			if(auditStatus2 == null){
				auditStatus2="N";
				tableForm.setProperty("auditStatus2","N");
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

				hs = HibernateUtil.getSession();
				
				//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
				String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
				List vmlist=hs.createSQLQuery(sqlss).list();
				if(vmlist!=null && vmlist.size()>0){
					if(maintstation == null || maintstation.trim().equals("")){
						maintstation=userInfo.getStorageId();
					}
				}

				List mainStationList=new ArrayList();
				//维保站下拉框  A03=维保经理,A48=维保站文员,维保站长 A49 
				if(vmlist!=null && vmlist.size()>0){
					String hql="select a from Storageid a where "
							+ "(a.storageid= '"+userInfo.getStorageId()+"' or "
							+ "a.storageid in(select parentstorageid from Storageid a where a.storageid= '"+userInfo.getStorageId()+"')) "
							+ "and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";	
					mainStationList=hs.createQuery(hql).list();
					
				}else{
					String hql="select a from Storageid a where a.comid like '"+maintDivision+"' " +
							"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
					mainStationList=hs.createQuery(hql).list();
				  
					 Storageid storid=new Storageid();
					 storid.setStorageid("%");
					 storid.setStoragename("全部");
					 mainStationList.add(0,storid);
				}
				request.setAttribute("mainStationList", mainStationList);
				
				String sql = "select cm.BillNo,cu.CompanyName,cm.MaintContractNo,cm.SalesContractNo," 
					+" cm.ElevatorNo,c.ComFullName,s.StorageName,cm.ContractSDate,cm.ContractEDate," 
					+" cm.auditStatus2,isnull(er.billno,'') as billNo2,isnull(cm.AuditRem,'') " 
					+"from ContractTransferMaster cm " 
					+" left join ElevatorTransferCaseRegister er on cm.elevatorNo = er.elevatorNo " 
					+" and er.checkNum = (select MAX(ina.checkNum) from ElevatorTransferCaseRegister ina " 
					+" where ina.elevatorNo = er.elevatorNo)"
					+" join Customer cu on cm.CompanyID = cu.CompanyID " 
					+" join Company c on cm.MaintDivision = c.ComID " 
					+" join StorageID s on cm.MaintStation = s.StorageID" 
					+" where cm.TransfeSubmitType = 'Y' and cm.isTrans = 'Y' ";
				
				if (billNo != null && !billNo.equals("")) {
					sql += " and cm.billNo like '%"+billNo.trim()+"%'";
				}
				if (maintContractNo != null && !maintContractNo.equals("")) {
					sql += " and cm.maintContractNo like '%"+maintContractNo.trim()+"%'";
				}
				if (companyName != null && !companyName.equals("")) {
					sql += " and (cu.companyName like '%"+companyName.trim()+"%' or cm.companyId like '%"+companyName.trim()+"%')";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and cm.maintDivision like '"+maintDivision.trim()+"'";
				}
				if (maintstation != null && !maintstation.equals("")) {
					sql += " and cm.maintStation like '"+maintstation.trim()+"'";
				}
				if (auditStatus2 != null && !auditStatus2.equals("")) {
					sql += " and isnull(cm.auditStatus2,'N') like '"+auditStatus2.trim()+"'";
				}
				if (salesContractNo != null && !salesContractNo.equals("")) {
					sql += " and cm.SalesContractNo like '%"+salesContractNo.trim()+"%'";
				}
				if (elevatorNo != null && !elevatorNo.equals("")) {
					sql += " and cm.ElevatorNo like '%"+elevatorNo.trim()+"%'";
				}
				
				String order = " order by "+table.getSortColumn();
				
				if (table.getIsAscending()) {
					sql += order;
				} else {
					sql += order + " desc";
				}
				
//				System.out.println(">>>"+sql);
				
				query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;
                
				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List contractTransferAuditList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					
					Map master=new HashMap();
					master.put("billNo",String.valueOf(objs[0]));
					master.put("companyId",String.valueOf(objs[1]));
					master.put("maintContractNo",String.valueOf(objs[2]));
					master.put("salesContractNo",String.valueOf(objs[3]));
					master.put("elevatorNo",String.valueOf(objs[4]));
					master.put("maintDivision",String.valueOf(objs[5]));
					master.put("maintStation",String.valueOf(objs[6]));
					master.put("contractSdate",String.valueOf(objs[7]));
					master.put("contractEdate", String.valueOf(objs[8]));
					master.put("auditStatus2", String.valueOf(objs[9]));
					master.put("billNo2", String.valueOf(objs[10]));
					master.put("auditRem", String.valueOf(objs[11]));
					
					contractTransferAuditList.add(master);
				}

				table.addAll(contractTransferAuditList);
				session.setAttribute("contractTransferManagerAuditList", table);
				
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
			forward = mapping.findForward("contractTransferManagerAuditList");
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

		request.setAttribute("navigator.location","合同交接资料经理审核 >> 查看");
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}
		
		String id=(String)dform.get("id");
		if(id==null || id.trim().equals("")){
			id = request.getParameter("id");
		}
		
		String auditStatus2 = request.getParameter("auditStatus2");
		if ("Y".equals(auditStatus2)) {
			request.setAttribute("addflag2", "N");
			request.setAttribute("addflag", "N");
		}else {
			request.setAttribute("addflag2", "Y");
			request.setAttribute("addflag", "Y");
		}
		
		ActionErrors errors = new ActionErrors();
		Boolean display = bd.contractTransferDisplay(form, request, errors, "display");
			
		request.setAttribute("billNo", id);
		request.setAttribute("auditOperid2", userInfo.getUserName());
		try {
			request.setAttribute("auditDate2", CommonUtil.getToday());
		} catch (ParseException e) {
			e.printStackTrace();
		}
									
		
		saveToken(request); //生成令牌，防止表单重复提交
		
		return mapping.findForward("ContractTransferManagerAuditAdd");
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
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String id = (String) dform.get("id");
		String auditStatus2 = (String)dform.get("auditStatus2");
		String auditRem2 = (String)dform.get("auditRem2");
		
		Session hs = null;
		Transaction tx = null;
		
		String billNo = request.getParameter("billNo");
		
		try {
			//防止表单重复提交
			if(isTokenValid(request, true)){
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				
				ContractTransferMaster master = (ContractTransferMaster) hs.get(ContractTransferMaster.class, billNo);
				master.setAuditOperid2(userInfo.getUserID());
				master.setAuditDate2(CommonUtil.getNowTime());
				master.setAuditStatus2(auditStatus2);
				master.setAuditRem2(auditRem2);
				
				if ("Y".equals(auditStatus2)) {
					//master.setTransferComplete("");
				}else {
					master.setTransfeSubmitType("R");
				}
				
				hs.save(master);
				
				tx.commit();
			}else{
				saveToken(request);
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("navigator.submit.error"));
			}
		} catch (HibernateException e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			try {
				tx.rollback();
			} catch (HibernateException e2) {
				log.error(e2.getMessage());
				DebugUtil.print(e2, "Hibernate Transaction rollback error!");
			}
			e.printStackTrace();
		} catch (DataStoreException e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			try {
				tx.rollback();
			} catch (HibernateException e2) {
				log.error(e2.getMessage());
				DebugUtil.print(e2, "Hibernate Transaction rollback error!");
			}
			e.printStackTrace();
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
				if (hs!=null) {
					hs.close();
				}
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}
				
		if (errors.isEmpty()) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
		} else {
			request.setAttribute("error", "Yes");
		}
		
		forward = mapping.findForward("returnList");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return forward;
	}
}	