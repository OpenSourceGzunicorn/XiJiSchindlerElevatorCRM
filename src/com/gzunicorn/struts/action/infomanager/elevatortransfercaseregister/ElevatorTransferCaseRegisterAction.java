package com.gzunicorn.struts.action.infomanager.elevatortransfercaseregister;

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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
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
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.pdfprint.BarCodePrint;
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
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.infomanager.elevatortransfercasebhtype.ElevatorTransferCaseBhType;
import com.gzunicorn.hibernate.infomanager.elevatortransfercaseprocess.ElevatorTransferCaseProcess;
import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;
import com.gzunicorn.hibernate.infomanager.handoverelevatorcheckfileinfo.HandoverElevatorCheckFileinfo;
import com.gzunicorn.hibernate.infomanager.handoverelevatorcheckitemregister.HandoverElevatorCheckItemRegister;
import com.gzunicorn.hibernate.infomanager.handoverelevatorspecialregister.HandoverElevatorSpecialRegister;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.workflow.bo.JbpmExtBridge;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class ElevatorTransferCaseRegisterAction extends DispatchAction {

	Log log = LogFactory.getLog(ElevatorTransferCaseRegisterAction.class);
	
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
	 * Method execute
	 * 安装维保交接电梯情况登记=>厂检员登记问题项
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {


		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		String authority="elevatortransfercaseregister";
		if(name != null && name.contains("Dispose")){
			authority = "elevatortransfercaseregisterdispose";
		}
		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + authority, null);
		/** **********结束用户权限过滤*********** */
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
	public ActionForward toSearchRecordDispose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("navigator.location","安装维保交接电梯情况登记 >> 查询列表");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "elevatorTransferCaseRegisterDisposeList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fElevatorTransferCaseRegisterDispose");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("billno");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			}else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			}  else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String billNo = tableForm.getProperty("billno");// 流水号
			String projectName = tableForm.getProperty("projectName");// 项目名称
			/*String staffName = tableForm.getProperty("staffName");// 厂检人员名称
*/			String department = tableForm.getProperty("department");// 所属部门					
			String processStatus = tableForm.getProperty("processStatus");// 
			String salesContractNo = tableForm.getProperty("salesContractNo");// 销售合同号	
			String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号	
			String insCompanyName = tableForm.getProperty("insCompanyName");// 安装单位名称
			/*String factoryCheckResult=tableForm.getProperty("factoryCheckResult");
			String checkNum=tableForm.getProperty("checkNum");*/
			
			
			Session hs = null;
			Query query = null;
			try {

				hs = HibernateUtil.getSession();

				String sql = "select a.billno,a.checkTime,a.checkNum,a.projectName,a.insCompanyName," +
						"a.salesContractNo,a.elevatorNo,lu.username,a.department,a.factoryCheckResult," +
						"a.submitType,a.status,a.processStatus,a.isClose,a.processName " +
						"from ElevatorTransferCaseRegister a,Loginuser lu " +
						"where submitType in ('Y','Z') and a.staffName=lu.userid " +
						"and staffName='"+userInfo.getUserID()+"'";
				
				if (billNo != null && !billNo.equals("")) {
					sql += " and billno like '%"+billNo.trim()+"%'";
				}
				if (projectName != null && !projectName.equals("")) {
					sql += " and projectName like '%"+projectName.trim()+"%'";
				}
				/*if (staffName != null && !staffName.equals("")) {
					sql+=" and (lu.username like '%"+staffName.trim()+"%' or lu.userid like '%"+staffName.trim()+"%' )";
				}*/
				if (department != null && !department.equals("")) {
					sql += " and department like '%"+department.trim()+"%'";
				}
				if (salesContractNo != null && !salesContractNo.equals("")) {
					sql += " and salesContractNo like '%"+salesContractNo.trim()+"%'";
				}
				if (elevatorNo != null && !elevatorNo.equals("")) {
					sql += " and elevatorNo like '%"+elevatorNo.trim()+"%'";
				}
				if (insCompanyName != null && !insCompanyName.equals("")) {
					sql += " and insCompanyName like '%"+insCompanyName.trim()+"%'";
				}
				if (processStatus != null && !processStatus.equals("")) {
					sql += " and processStatus like '%"+processStatus.trim()+"%'";
				}
				/*if (factoryCheckResult != null && !factoryCheckResult.equals("")) {
					sql+=" and etcr.factoryCheckResult='"+factoryCheckResult.trim()+"'";
			    }
				if (checkNum != null && !checkNum.equals("")) {
					if(checkNum.equals("最新厂检"))
					sql+=" and etcr.checkNum=(select max(checkNum) from ElevatorTransferCaseRegister)";
			    }*/
				
				if (table.getIsAscending()) {
					sql += " order by "+ table.getSortColumn() +" desc";
				} else {
					sql += " order by "+ table.getSortColumn() +" asc";		
				}
				
				query = hs.createQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List elevatorTransferCaseRegisterList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map map=new HashMap();
					map.put("billno", objs[0]);
					map.put("checkTime", objs[1]);
					map.put("checkNum", objs[2]);
					map.put("projectName", objs[3]);
					map.put("insCompanyName", objs[4]);
					map.put("salesContractNo", objs[5]);
					map.put("elevatorNo", objs[6]);
					map.put("staffName", objs[7]);
					map.put("department", bd.getName_Sql("Company", "comname", "comid",objs[8].toString()));
					map.put("factoryCheckResult", objs[9]);
					map.put("submitType", objs[10]);
					map.put("status", bd.getName_Sql("ViewFlowStatus", "typename", "typeid", String.valueOf(objs[11])));
					map.put("processStatus", objs[12]);
					map.put("isClose", objs[13]);
					map.put("processName", objs[14]);
					elevatorTransferCaseRegisterList.add(map);
				}

				table.addAll(elevatorTransferCaseRegisterList);
				String sql1="from Company where (comtype='1' or comtype='2') and enabledflag='Y' order by comid desc";				
			    List list1=hs.createQuery(sql1).list();
				session.setAttribute("elevatorTransferCaseRegisterDisposeList", table);
				request.setAttribute("departmentList", list1);
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
			forward = mapping.findForward("elevatorTransferCaseRegisterDisposeList");
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
	
	
	
	public ActionForward toDisposeDisplay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","安装维保交接电梯情况登记 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		String returnMethod=request.getParameter("returnMethod");
		display(form, request, errors, "display");
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		request.setAttribute("display", "yes");
		
		String typejsp= request.getParameter("typejsp");
		if(typejsp!=null)
		{
			request.setAttribute("typejsp", typejsp);
		}else
		{
			request.setAttribute("typejsp", "");
		}
		request.setAttribute("returnMethod", returnMethod);
		forward = mapping.findForward("elevatorTransferCaseRegisterDisposeDisplay");
		return forward;
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
			ElevatorTransferCaseRegister master = (ElevatorTransferCaseRegister) hs.get(ElevatorTransferCaseRegister.class, id);
			if (master != null) {
				hs.createQuery("delete from HandoverElevatorCheckItemRegister where billno='"+id+"'").executeUpdate();
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
	
	
	/**
	 * 跳转到驳回页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareDisposeReject(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","安装维保交接电梯情况登记 >> 驳回");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		display(form, request, errors, "reject");
		request.setAttribute("checkTime", CommonUtil.getNowTime());
		request.setAttribute("bhDate", CommonUtil.getNowTime());
		request.setAttribute("bhTypeList", bd.getPullDownList("ElevatorTransferCaseRegister_BhType"));
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		forward = mapping.findForward("elevatorTransferCaseRegisterReject");
		
		return forward;
	}


	/**
	 * 驳回
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDisposeRejectRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		String bhRem=(String)dform.get("bhRem");
		String bhDate=(String)dform.get("bhDate");
		String bhType=(String)dform.get("bhType");
		
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			String id = (String) dform.get("id");
			ElevatorTransferCaseRegister master = (ElevatorTransferCaseRegister) hs.get(ElevatorTransferCaseRegister.class, id);
			if (master != null) {
				master.setBhDate(bhDate);
				master.setBhRem(bhRem);
				master.setBhType(bhType);
				master.setSubmitType("R");
				master.setProcessStatus("0");
				master.setReceiveDate("");
				master.setWorkisdisplay(null);
				hs.save(master);
				
				//安装维保交接电梯情况驳回记录表
				ElevatorTransferCaseBhType etfcbh=new ElevatorTransferCaseBhType();
				etfcbh.setBillno(id);
				etfcbh.setBhDate(bhDate);
				etfcbh.setBhRem(bhRem);
				etfcbh.setBhType(bhType);
				hs.save(etfcbh);
				
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("reject.succeed"));
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"reject.foreignkeyerror"));
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
			forward = mapping.findForward("returnDisposeList");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return forward;
	}
	
	/**
	 * 跳转到转派页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareDisposeTurn(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","安装维保交接电梯情况登记 >> 转派");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		display(form, request, errors, "tran");
		request.setAttribute("checkTime", CommonUtil.getNowTime());
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		forward = mapping.findForward("elevatorTransferCaseRegisterTurn");
		
		return forward;
	}
	
	/**
	 * 转派
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDisposeTurnRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;
		String staffName=(String)dform.get("staffName");
		String staffLinkPhone=(String)dform.get("staffLinkPhone");
		String transferDate=(String)dform.get("transferDate");
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			String id = (String) dform.get("id");
			ElevatorTransferCaseRegister master = (ElevatorTransferCaseRegister) hs.get(ElevatorTransferCaseRegister.class, id);
			if (master != null) {
				master.setTransferDate(transferDate);
				master.setTransferId(userInfo.getUserID());
				master.setStaffName(staffName);
				master.setStaffLinkPhone(staffLinkPhone);
				master.setSubmitType("Z");
				master.setProcessStatus("0");
				master.setReceiveDate("");
				hs.save(master);
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("turn.succeed"));
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"turn.foreignkeyerror"));
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
			forward = mapping.findForward("returnDisposeList");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return forward;
	}
	
	/**
	 * 跳转到登记页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareDisposeRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","安装维保交接电梯情况登记 >> 登记");	
		request.setAttribute("returnListMethod", "toSearchRecordDispose");
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		display(form, request, errors, "dispose");
		
		request.setAttribute("checkTime", CommonUtil.getNowTime());
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		forward = mapping.findForward("elevatorTransferCaseRegisterDisposeAdd");
		
		return forward;
	}
	
	/**
	 * 点击登记的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDisposeRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		DynaActionForm dform = (DynaActionForm) form;
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		String userid = userInfo.getUserID();

		ElevatorTransferCaseRegister register=null;
		Session hs = null;
		Transaction tx = null;
		JbpmExtBridge jbpmExtBridge=null;
		
		//检查附件不能超过多少M。一次上传的文件总大小不能超过5M,保存失败!
		Boolean maxLengthExceeded = (Boolean) request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);			
		if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())){
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("doc.file.size.check.save"));
			forward = mapping.findForward("returnDisposeList");
		}else{
			String billno=(String)dform.get("billno");
			String factoryCheckResult=(String)dform.get("factoryCheckResult");
			String factoryCheckResult2=(String)dform.get("factoryCheckResult2");
			String r2=(String)dform.get("r2");
			String processStatus=(String)dform.get("processStatus");
			
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				
				dispose(dform, request, errors, billno);
				
				List list=savePicter(dform, request, response, "HandoverElevatorCheckItemRegister.file.upload.folder", billno);
				boolean b=false;
				String processName="";
				if(list!=null || list.size()>0){
					b=savePicterTodb(request, list, billno);
				}
				if("2".equals(processStatus)){
					register=(ElevatorTransferCaseRegister) hs.get(ElevatorTransferCaseRegister.class, billno);
					String processDefineID = Grcnamelist1.getProcessDefineID("elevatortransfercaseregister", register.getStaffName());
					if(processDefineID == null || processDefineID.equals("")){
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("error.string","<font color='red'>未配置审批流程信息，不能启动！</font>"));
						throw new Exception();
					}
					/**=============== 启动新流程实例开始 ===================**/
					HashMap paraMap = new HashMap();
					jbpmExtBridge=new JbpmExtBridge();
					ProcessBean pd = null;		
					pd = jbpmExtBridge.getPb();
					if(factoryCheckResult.equals("合格")){
						pd.setSelpath("Y");
						processName="关闭审核流程";
						Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "关闭审核流程", "%");// 添加审核人
					}else if(factoryCheckResult.equals("不合格")){
						pd.setSelpath("N");
						processName="厂检部长审核";
						Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "厂检部长审核", "%");// 添加审核人
					}
					
					pd=jbpmExtBridge.startProcess(WorkFlowConfig.getProcessDefine(processDefineID),userid,userid,billno,"",paraMap);
					/**==================== 流程结束 =======================**/
//					String hql="update ElevatorTransferCaseRegister set checkTime='"+CommonUtil.getNowTime()+"',factoryCheckResult='"+factoryCheckResult+"',processStatus='"+processStatus+"',processName='"+processName+"',status='"+pd.getStatus()+"',tokenId='"+pd.getToken()+"' where billno='"+billno+"'";
					register.setCheckTime(request.getParameter("checkTime"));
					register.setFactoryCheckResult(factoryCheckResult);
					register.setFactoryCheckResult2(factoryCheckResult2);
					register.setR2(r2);
					register.setProcessStatus(processStatus);					
					register.setStatus(pd.getStatus());
					register.setTokenId(pd.getToken());
					register.setProcessName(pd.getNodename());
					hs.save(register);
//					hs.createQuery(hql).executeUpdate();
				}else{
					String hql="update ElevatorTransferCaseRegister "
							+ "set checkTime='"+request.getParameter("checkTime")+"',"
							+ "factoryCheckResult='"+factoryCheckResult+"',"
							+ "factoryCheckResult2='"+factoryCheckResult2+"',"
							+ "r2='"+r2+"',"
							+ "processStatus='"+processStatus+"' "
							+ "where billno='"+billno+"'";
					hs.createQuery(hql).executeUpdate();
				}
			
				tx.commit();			
	
			} catch (Exception e1) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
				try {
					tx.rollback();
					if (jbpmExtBridge != null) {
						jbpmExtBridge.setRollBack();
					}
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
					if(jbpmExtBridge!=null){
						jbpmExtBridge.close();
					}
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "Hibernate close error!");
				}
			}
			String isreturn = request.getParameter("isreturn");		
			try {
				if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
					// return list page
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","记录登记成功！"));
					forward = mapping.findForward("returnDisposeList");
				} else {
					// return modify page
					if (errors.isEmpty()) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","记录登记成功！"));
					} else {
						request.setAttribute("error", "Yes");
					}
					
					forward = mapping.findForward("returnDispose");			
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		return forward;
	}
	
	public void dispose(ActionForm form, HttpServletRequest request,ActionErrors errors,String billno){
		Session hs = null;
		Transaction tx = null;
		
		String[] hecirJnlno=request.getParameterValues("jnlno");
		String[] examType=request.getParameterValues("examType");
		String[] checkItem=request.getParameterValues("checkItem");
		String[] issueCoding=request.getParameterValues("issueCoding");
		String[] issueContents=request.getParameterValues("issueContents");
		String[] rem=request.getParameterValues("rem");
		String[] isRecheck=request.getParameterValues("isRecheck");
		
		String[] isOk=request.getParameterValues("isOk");
		String[] scjnlno=request.getParameterValues("scjnlno");
		String[] isyzg=request.getParameterValues("isyzg");//是否已整改
		
		String elevatorAddress=request.getParameter("elevatorAddress");
		if(elevatorAddress==null){
			elevatorAddress="";
		}
		
		String hecirJnlnos="";
		if(hecirJnlno!=null && !hecirJnlno.equals("")){
			for(int i=0;i<hecirJnlno.length;i++){
				if(hecirJnlno[i]!=null&&!"".equals(hecirJnlno[i])){
				hecirJnlnos+=i==0 ? hecirJnlno[i] : "','"+hecirJnlno[i];
			    }
			}
		}
		
		HandoverElevatorCheckItemRegister hecir = null;
		try {
			hs=HibernateUtil.getSession();
			tx=hs.beginTransaction();
			ElevatorTransferCaseRegister register=(ElevatorTransferCaseRegister) hs.get(ElevatorTransferCaseRegister.class, billno);
			if(register!=null){
				if(!hecirJnlnos.equals("")){
					hs.createQuery("delete HandoverElevatorCheckFileinfo h where h.handoverElevatorCheckItemRegister.jnlno in(select jnlno from HandoverElevatorCheckItemRegister h where h.elevatorTransferCaseRegister.billno='"+billno+"' and h.jnlno not in('"+hecirJnlnos+"'))").executeUpdate();
					hs.createQuery("delete HandoverElevatorCheckItemRegister h where h.elevatorTransferCaseRegister.billno='"+billno+"' and h.jnlno not in('"+hecirJnlnos+"')").executeUpdate();
				}else
				{
					hs.createQuery("delete HandoverElevatorCheckFileinfo h where h.handoverElevatorCheckItemRegister.jnlno in(select jnlno from HandoverElevatorCheckItemRegister h where h.elevatorTransferCaseRegister.billno='"+billno+"')").executeUpdate();
					hs.createQuery("delete HandoverElevatorCheckItemRegister h where h.elevatorTransferCaseRegister.billno='"+billno+"'").executeUpdate();
				}
				register.setElevatorAddress(elevatorAddress);
				hs.update(register);
				hs.flush();
				
				if(examType!=null && examType.length>0){
		            for(int i=0;i<examType.length;i++){
		               if(hecirJnlno[i]!=null && !hecirJnlno[i].equals("")){
	                		hecir = (HandoverElevatorCheckItemRegister) hs.get(HandoverElevatorCheckItemRegister.class, hecirJnlno[i]);	
	                    	hecir.setRem(rem[i]);
	                    	hecir.setIsyzg(isyzg[i]);//是否已整改
	                    	hs.update(hecir);
		                }else{
			                hecir = new HandoverElevatorCheckItemRegister();
							String jnlno="";
							try {
								String[] billno1 = CommonUtil.getBillno(CommonUtil.getToday().substring(2,4),"HandoverElevatorCheckItemRegister", 1);	
								jnlno=billno1[0];
								}catch (Exception e) {
									e.printStackTrace();
								}
							hecir.setJnlno(jnlno);
							hecir.setIsDelete("N");
							hecir.setExamType(examType[i]);
							hecir.setIssueCoding(issueCoding[i]);
							hecir.setCheckItem(checkItem[i]);
							hecir.setIsRecheck(isRecheck[i]);
							hecir.setIssueContents(issueContents[i]);
							hecir.setRem(rem[i]);
							hecir.setIsyzg(isyzg[i]);//是否已整改
							hecir.setElevatorTransferCaseRegister(register);
							hs.save(hecir);
							hs.flush();
		                }
					} 
				}
	             if(isOk!=null&&isOk.length>0){
	            	 for(int i=0 ; i<isOk.length;i++){
	            		 HandoverElevatorSpecialRegister r=(HandoverElevatorSpecialRegister) hs.get(HandoverElevatorSpecialRegister.class,scjnlno[i]) ;
	            	     r.setIsOk(isOk[i]);
	            	     hs.update(r);
	            	     hs.flush();
	            	 }
	             }  
				//tx.rollback();
				tx.commit();
			}else{
				System.out.println("安装维保交接电梯情况登记=>厂检员登记问题项>>>>找不到相关记录（ElevatorTransferCaseRegister）+"+billno);
			}
		} catch (Exception e1) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			try {
				tx.rollback();
			} catch (HibernateException e2) {
				log.error(e2.getMessage());
				DebugUtil.print(e2, "Hibernate Transaction rollback error!");
			}
			e1.printStackTrace();
		}finally{
			if(hs!=null){
				hs.close();
			}
		}
	}
	
	/**
	 * 上传保存附件
	 * @param form
	 * @param request
	 * @param response
	 * @param folder
	 * @param billno
	 * @return
	 */
	public List savePicter(ActionForm form, HttpServletRequest request,
			HttpServletResponse response, String folder, String billno) {
		List returnList = new ArrayList();
		folder = PropertiesUtil.getProperty(folder).trim();//
		DynaActionForm dform = (DynaActionForm) form;
		Integer checkNum=(Integer) dform.get("checkNum");
		
		FormFile formFile = null;
		Fileinfo file = null;
		//FileItem fileitem=null;
		if (form.getMultipartRequestHandler() != null) {
			Hashtable hash = form.getMultipartRequestHandler().getFileElements();
			
//			//System.out.println(hash.keys().toString());
			if (hash != null) {
//				Iterator it = hash.values().iterator();
				HandleFile hf = new HandleFileImpA();
//				while (it.hasNext()) {
				for(Iterator it = hash.keySet().iterator(); it.hasNext();){
//					formFile = (FormFile) it.next();
					String key=(String)it.next();
					formFile=(FormFile)hash.get(key);
					Map map = new HashMap();
					if(formFile!=null){
						try {
							if(!formFile.getFileName().equals("")){
								String checkItem=request.getParameter("item_"+key);
								String examType=request.getParameter("type_"+key);
								String issueCoding=request.getParameter("coding_"+key);
								String newFileName="HandoverElevatorCheckItemRegister"+"_"+billno+"_"+checkNum.toString()+"_"+key+"_"+formFile.getFileName();
								map.put("checkItem", checkItem);
								map.put("oldfilename", formFile.getFileName());
								map.put("filepath", CommonUtil.getNowTime("yyyy-MM-dd")+"/");
								map.put("fileSize", new Double(formFile.getFileSize()));
								map.put("fileformat",formFile.getContentType());
								map.put("examType", examType);
								map.put("issueCoding", issueCoding);
								map.put("newFileName", newFileName);
								//保存文件到系统
								hf.createFile(formFile.getInputStream(), folder+"/"+CommonUtil.getNowTime("yyyy-MM-dd")+"/", newFileName);
								returnList.add(map);
							}else{
								continue;
							}
							
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}
			}
		}
		return returnList;
	}
		
	/**
	 * 保存附件信息到数据库
	 * @param hs
	 * @param request
	 * @param fileInfoList
	 * @param mtcId
	 * @param trsId
	 * @param seiid
	 * @param billno
	 * @return
	 */
	public boolean savePicterTodb(HttpServletRequest request,List fileInfoList,String billno){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;
		boolean saveFlag = true;
		if(null != fileInfoList && fileInfoList.size()>0){
			String sql="";
			HandoverElevatorCheckItemRegister msr=null;
			try {
				hs=HibernateUtil.getSession();
				tx=hs.beginTransaction();
				int len = fileInfoList.size();
				for(int i = 0 ; i < len ; i++){
					Map map = (Map)fileInfoList.get(i);
					String hql="from HandoverElevatorCheckItemRegister hecir where hecir.elevatorTransferCaseRegister.billno='"+billno+"' and hecir.checkItem='"+(String)map.get("checkItem")+"' and hecir.issueCoding ='"+(String)map.get("issueCoding")+"' and hecir.examType ='"+(String)map.get("examType")+"'";
					List list=hs.createQuery(hql).list();
					////System.out.println(hql);
					if(list!=null && list.size()>0){
						msr=(HandoverElevatorCheckItemRegister) list.get(0);
						String newFilename=(String) map.get("newFileName");
						HandoverElevatorCheckFileinfo fileInfo=new HandoverElevatorCheckFileinfo();
						fileInfo.setHandoverElevatorCheckItemRegister(msr);
						fileInfo.setOldFileName((String)map.get("oldfilename"));
						fileInfo.setNewFileName((String)map.get("newFileName"));
						fileInfo.setFileSize(Double.valueOf(map.get("fileSize").toString()));
						fileInfo.setFilePath((String)map.get("filepath"));
						fileInfo.setFileFormat((String)map.get("fileformat"));
						fileInfo.setUploadDate(CommonUtil.getNowTime());
						fileInfo.setUploader(userInfo.getUserID());
						fileInfo.setExt1("N");
						hs.save(fileInfo);
					}
				}
				tx.commit();
			} catch (Exception e) {
				e.printStackTrace();
				saveFlag = false;
				try {
					tx.rollback();
				} catch (HibernateException e2) {
					log.error(e2.getMessage());
				}
			}finally{
				if(hs!=null){
					hs.close();
				}
			}
		}
		return saveFlag;
	}
	
	/**
	 * 文件删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toDeleteFileDispose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) { 
		
		Session hs = null;
		Transaction tx = null;
		String id = request.getParameter("filesid");
		String filePath=request.getParameter("filePath");
		try {
			id=URLDecoder.decode(id, "utf-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		List list=null;
		String folder = request.getParameter("folder");
		//创建输出流对象
        PrintWriter out=null;
        //依据验证结果输出不同的数据信息	 
		if(null == folder || "".equals(folder)){
			folder ="HandoverElevatorCheckItemRegister.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		int zNum=0,yNum=0;
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if(id!=null && id.length()>0){
				
				HandoverElevatorCheckFileinfo hecf=(HandoverElevatorCheckFileinfo) hs.get(HandoverElevatorCheckFileinfo.class,Integer.valueOf(id));
				String fileName=hecf.getNewFileName();
				hs.delete(hecf);
				hs.flush();
				
				HandleFile hf = new HandleFileImpA();
				String localpath=folder+"/"+filePath+fileName;
				hf.delFile(localpath);
			}
			
			response.setContentType("text/xml; charset=UTF-8");
			
			out = response.getWriter();
			
			out.println("<response>");
			out.println("<res>" + "Y" + "</res>");
			out.println("</response>");
			
		     tx.commit();
		} catch (Exception e) {
			try {
				out.println("<response>");
				out.println("<res>" + "N" + "</res>");
				out.println("</response>");
				tx.rollback();
				
			} catch (HibernateException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				if(out!=null){
					out.close();					
				}
				if(hs!=null){
					hs.close();
				}
			} catch (HibernateException hex) {
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}	
		return null;
	}
	
	public ActionForward toDownloadFileDispose(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

//		FileOperatingUtil uf = new FileOperatingUtil();
//		uf.toDownLoadFiles(mapping, form, request, response);
		String filename=request.getParameter("filesname");
		String fileOldName=request.getParameter("fileOldName");
		String filePath=request.getParameter("filePath");
		String folder=request.getParameter("folder");
		if(folder==null || "".equals(folder)){
			folder="HandoverElevatorCheckItemRegister.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		filename=URLDecoder.decode(filename, "utf-8");
		filePath=URLDecoder.decode(filePath, "utf-8");
		fileOldName=URLDecoder.decode(fileOldName, "utf-8");
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;

		response.setContentType("application/x-msdownload");
		response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileOldName, "utf-8"));

		fis = new FileInputStream(folder+"/"+filePath+filename);
		bis = new BufferedInputStream(fis);
		fos = response.getOutputStream();
		bos = new BufferedOutputStream(fos);

		int bytesRead = 0;
		byte[] buffer = new byte[5 * 1024];
		while ((bytesRead = bis.read(buffer)) != -1) {
			bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
			bos.flush();
		}
		if (fos != null) {fos.close();}
		if (bos != null) {bos.close();}
		if (fis != null) {fis.close();}
		if (bis != null) {bis.close();}
		return null;
	}
	


	public void display(ActionForm form, HttpServletRequest request ,ActionErrors errors ,String flag){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		if(id==null || id.equals("")){
			id=(String)dform.get("id");
		}
		
		Session hs = null;
		String[] typeflag={"a.id.typeflag='HandoverElevatorCheckItem_ExamType'"};
		List partyList = new ArrayList();
		List partyDeleteList = new ArrayList();
		List importantList = new ArrayList();
		List gemeralList = new ArrayList();
		List etcpList=new ArrayList();
		List bhTypeList=bd.getPullDownList("ElevatorTransferCaseRegister_BhType");
		if (id != null && !id.equals("")) {			
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from ElevatorTransferCaseRegister where billNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {

					// 主信息
					ElevatorTransferCaseRegister register = (ElevatorTransferCaseRegister) list.get(0);															
					register.setAuditor(bd.getName(hs, "Loginuser","username", "userid",register.getAuditor()));// 经办人
					register.setOperId(bd.getName(hs, "Loginuser","username", "userid",register.getOperId()));
					String[] flag1={"a.id.typeflag='ElevatorSalesInfo_type'"};
					String elevatorTypeName=bd.getName("Pulldown", "pullname", "id.pullid", register.getElevatorType(), flag1);
					dform.set("id",id);
					if("display".equals(flag)){// 查看	
						register.setDepartment(bd.getName_Sql("Company", "comname", "comid",register.getDepartment()));// 													
						register.setStaffName(bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
						register.setTransferId(bd.getName(hs, "Loginuser","username", "userid",register.getTransferId()));
						register.setR1(register.getFloor()+"/"+register.getStage()+"/"+register.getDoor());
						register.setBhType(bd.getOptionName(register.getBhType(), bhTypeList));
					}else if("tran".equals(flag)){
						request.setAttribute("username", bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
						register.setTransferDate(CommonUtil.getNowTime());
						register.setTransferId(userInfo.getUserName());
						register.setDepartment(bd.getName_Sql("Company", "comname", "comid",register.getDepartment()));
					}else if("dispose".equals(flag)){
						register.setDepartment(bd.getName_Sql("Company", "comname", "comid",register.getDepartment()));// 													
						register.setStaffName(bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
						register.setTransferId(bd.getName(hs, "Loginuser","username", "userid",register.getTransferId()));
						register.setCheckTime(CommonUtil.getNowTime());
						register.setBhType(bd.getOptionName(register.getBhType(), bhTypeList));
					}else if("reject".equals(flag)){
						register.setDepartment(bd.getName_Sql("Company", "comname", "comid",register.getDepartment()));
						register.setStaffName(bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
					}else if("receive".equals(flag)){
						register.setDepartment(bd.getName_Sql("Company", "comname", "comid",register.getDepartment()));// 													
						register.setStaffName(bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
						register.setTransferId(bd.getName(hs, "Loginuser","username", "userid",register.getTransferId()));
						register.setR1(register.getFloor()+"/"+register.getStage()+"/"+register.getDoor());
						if(!register.getProcessStatus().equals("0")){
							request.setAttribute("display", "yes");
							request.setAttribute("receiveDate", register.getReceiveDate());
						}else{
							request.setAttribute("receiveDate", CommonUtil.getNowTime());
						}
					}	
					else{
						String sql1="from Company where (comtype='1' or comtype='2') and enabledflag='Y' order by comid desc";				
					    List list1=hs.createQuery(sql1).list();
						request.setAttribute("departmentList", list1);
						request.setAttribute("username", bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
						request.setAttribute("elevatorTypeName", elevatorTypeName);
						dform.set("isxjs",register.getIsxjs());
						register.setTransferId(bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
					}
					
					// 甲方问题
					String sql = "select b.ExamType,b.CheckItem,b.IssueCoding,b.IssueContents,b.rem,"
							+ "b.isRecheck,b.isDelete,b.jnlno,b.deleteRem,h.itemgroup,p.pullname,b.isyzg  "
							+ "from HandoverElevatorCheckItemRegister b ,HandoverElevatorCheckItem h,Pulldown p"
							+ " where b.ExamType=h.ExamType and b.CheckItem=h.CheckItem and b.IssueCoding=h.IssueCoding"
							+ " and b.billno='"+register.getBillno()+"' "
							+ " and p.pullid=b.examType and p.typeflag='HandoverElevatorCheckItem_ExamType' "
							+ " order by b.isRecheck desc, p.orderby,b.issueCoding";					
					query = hs.createSQLQuery(sql);	
					List list1 = query.list();
					if(list1!=null&&list1.size()>0){
						String examTypeName="";
						String examTypeName1="";
						int etnum=0;
						int etnum1=0;
						boolean is0=true;
						boolean is01=true;
					for (Object object : list1) {
						Object[] values=(Object[])object;
						Map map=new HashMap();
						
						String examType=(String)values[0];
						
						if("display".equals(flag)){
							map.put("examType", values[10]);
						}else{
							map.put("examTypeName",  values[10]);
							map.put("examType", values[0]);
						}
						
						map.put("checkItem", values[1]);
						map.put("issueCoding", values[2]);
						map.put("issueContents1", values[3]);
						map.put("rem", values[4]);
						map.put("isRecheck", values[5]);
						map.put("deleteRem", values[8]);
						map.put("itemgroup", values[9]);
						map.put("isyzg",  values[11]);
						
						int cn=register.getCheckNum();
						if(cn>1){
							if(map.get("isRecheck").toString().equals("N"))
							{
							map.put("color", "red");
							}else{
							map.put("color", "black");	
							}
						}
						map.put("jnlno", values[7]);
						
						String hql="from HandoverElevatorCheckFileinfo h where h.handoverElevatorCheckItemRegister.jnlno ='"+(String)values[7]+"' ";
						List fileList=hs.createQuery(hql).list();
						if(fileList!=null&&fileList.size()>0){
							map.put("fileList", fileList);
						}
						
						if(values[6].equals("Y")){
							//增加显示序号
							if(is01){
								examTypeName1=examType;
								is01=false;
							}
							if(examTypeName1.equals(examType)){
								etnum1++;
							}else{
								etnum1=1;
								examTypeName1=examType;
							}
							map.put("examTypeNum", etnum1+"");
							
							partyDeleteList.add(map);
						}else{
							//增加显示序号
							if(is0){
								examTypeName=examType;
								is0=false;
							}
							if(examTypeName.equals(examType)){
								etnum++;
							}else{
								etnum=1;
								examTypeName=examType;
							}
							map.put("examTypeNum", etnum+"");
							
							partyList.add(map);
						}
					}
					}
					sql="select r,c.scName from HandoverElevatorSpecialClaim c,HandoverElevatorSpecialRegister r where r.scId=c.scId and r.elevatorTransferCaseRegister.billno='"+id+"'";
					list1=hs.createQuery(sql).list();
					List specialRegister =new ArrayList();
					if(list1!=null&&list1.size()>0)
					{
						for(int i=0;i<list1.size();i++){
							Object[] objects=(Object[]) list1.get(i);
							HandoverElevatorSpecialRegister r=(HandoverElevatorSpecialRegister) objects[0];
							r.setR1((String)objects[1]);
							specialRegister.add(r);
						}
						if(specialRegister!=null&&specialRegister.size()>0){
							request.setAttribute("specialRegister", specialRegister);
						}
					}
					
					sql="select taskId,taskName,userId,date1,time1,approveResult,approveRem from ElevatorTransferCaseProcess where billno='"+id+"' order by itemId";
					list1=hs.createQuery(sql).list();
					for(Object object : list1){
						Object[] values=(Object[])object;
						Map map=new HashMap();
						map.put("taskId", values[0]);
						map.put("taskName", values[1]);
						map.put("userId", bd.getName(hs, "Loginuser","username", "userid",String.valueOf(values[2])));
						map.put("date", values[3]+" "+values[4]);
						map.put("approveResult", String.valueOf(values[5]));
						map.put("approveRem", values[6]);
						etcpList.add(map);
					}
					
					request.setAttribute("elevatorTransferCaseRegisterBean", register);	
					request.setAttribute("hecirList", partyList);
					if(partyDeleteList!=null&&partyDeleteList.size()>0){
					request.setAttribute("hecirDeketeList", partyDeleteList);
					}
					/*request.setAttribute("importantList", importantList);
					request.setAttribute("gemeralList", gemeralList);*/
					request.setAttribute("etcpList", etcpList);
							
					//厂检部长上传的附件
					String filesql="from Wbfileinfo where busTable='ElevatorTransferCaseRegister' and materSid='"+id+"'";
					List wbfilelist=hs.createQuery(filesql).list();
					if(wbfilelist!=null && wbfilelist.size()>0){
						request.setAttribute("wbfilelist", wbfilelist);
					}
					
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

	/**
	 * 跳转到接收页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toReceiveDisposeRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("navigator.location","安装维保交接电梯情况登记 >> 接收");	
		request.setAttribute("returnListMethod", "toSearchRecordDispose");
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		display(form, request, errors, "receive");
		
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		forward = mapping.findForward("elevatorTransferCaseRegisterDisposeReceive");
		
		return forward;
	}
	
	/**
	 * 点击接收的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	public ActionForward toReceiveDispose(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	
		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;	
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ElevatorTransferCaseRegister register = null;
		String id = request.getParameter("id");
		String receiveDate = request.getParameter("receiveDate");
		//String isreturn = request.getParameter("isreturn");
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();	
		    register = (ElevatorTransferCaseRegister) hs.get(ElevatorTransferCaseRegister.class, id);
		    register.setReceiveDate(receiveDate);
		    register.setProcessStatus("4");
		    hs.update(register);
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
		
		forward = mapping.findForward("returnDisposeList");
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return forward;
	}
	
	/*
	 * 跳转到打印
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	@SuppressWarnings("unchecked")
	public ActionForward toPreparePrintDisposeRecord(ActionMapping mapping,ActionForm form,
						HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	ActionErrors errors = new ActionErrors();
    	HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		List etcpList=new ArrayList();
		if (id != null && !id.equals("")) {			
			try {
				hs = HibernateUtil.getSession();
				
				Query query = hs.createQuery("from ElevatorTransferCaseRegister where billNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {

					// 主信息
					ElevatorTransferCaseRegister register = (ElevatorTransferCaseRegister) list.get(0);															
					register.setAuditor(bd.getName(hs, "Loginuser","username", "userid",register.getAuditor()));// 经办人
					register.setOperId(bd.getName(hs, "Loginuser","username", "userid",register.getOperId()));
					register.setDepartment(bd.getName_Sql("Company", "comname", "comid", register.getDepartment()));
					register.setStaffName(bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
					register.setR1(register.getFloor()+"/"+register.getStage()+"/"+register.getDoor());
					//甲方问题
					List hecirList=new ArrayList();
					String sql = "select p.pullname examType,b.checkItem checkItem,b.issueCoding issueCoding,b.issueContents IssueContents1,"
							+ "b.rem rem,b.isRecheck isRecheck,b.isDelete isDelete,b.jnlno jnlno,b.deleteRem,isnull(b.isyzg,'') "
							+ "from HandoverElevatorCheckItemRegister b ,pulldown p "
							+ "where p.pullid=b.examType "
							+ " and p.typeflag='HandoverElevatorCheckItem_ExamType' "
							+ " and b.billno='"+id+"' order by b.isRecheck desc,p.orderby,b.issueCoding ";					
					query = hs.createSQLQuery(sql);	
					List list1 = query.list();
					if(list1!=null&&list1.size()>0){
					for (Object object : list1) {
						Object[] values=(Object[])object;
						Map map=new HashMap();
						map.put("examType", values[0]);
						map.put("checkItem", values[1]);
						map.put("issueCoding", values[2]);
						map.put("issueContents1", values[3]);
						map.put("issueContents", values[4]);
						map.put("isRecheck", values[5]);
						map.put("deleteRem", values[8]);
						if("Y".equals(values[9])){
							map.put("isyzg","已整改");
						}else{
							map.put("isyzg", "");
						}
						
						String hql="from HandoverElevatorCheckFileinfo h where h.handoverElevatorCheckItemRegister.jnlno ='"+(String)values[7]+"' ";
						List fileList=hs.createQuery(hql).list();
						if(fileList!=null&&fileList.size()>0){
							map.put("fileList", fileList);
						}
						if(!values[6].equals("Y")){
							hecirList.add(map);
						}
					 }
					}
					sql="select r,c.scName from HandoverElevatorSpecialClaim c,HandoverElevatorSpecialRegister r where r.scId=c.scId and r.elevatorTransferCaseRegister.billno='"+id+"'";
					List isok_scNameList=hs.createQuery(sql).list();
					List specialRegisters =new ArrayList();
					if(isok_scNameList!=null&&list1.size()>0)
					{
						for(int i=0;i<isok_scNameList.size();i++){
							Object[] objects=(Object[]) isok_scNameList.get(i);
							HandoverElevatorSpecialRegister r=(HandoverElevatorSpecialRegister) objects[0];
							if(r.getIsOk().trim().equals("Y")){
								specialRegisters.add((String)objects[1]);	
							}
						}
					}
					
					
					//对barCodeList、noticeList操作
					BarCodePrint dy = new BarCodePrint();
					List barCodeList = new ArrayList();
					barCodeList.add(register);
					barCodeList.add(hecirList);
					barCodeList.add(specialRegisters);
					dy.toPrintTwoRecord4(request,response, barCodeList,id,"N"); 
					//register hecirList
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
		
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
    	return null;
    }
	
	
}	