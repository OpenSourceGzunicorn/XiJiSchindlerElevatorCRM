package com.gzunicorn.struts.action.infomanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.pdfprint.BarCodePrint;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.JSONUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.file.HandleFile;
import com.gzunicorn.file.HandleFileImpA;
import com.gzunicorn.hibernate.basedata.Fileinfo;
import com.gzunicorn.hibernate.basedata.markingitems.MarkingItems;
import com.gzunicorn.hibernate.basedata.principal.Principal;
import com.gzunicorn.hibernate.basedata.shouldexamineitems.ShouldExamineItems;
import com.gzunicorn.hibernate.basedata.termsecurityrisks.TermSecurityRisks;
import com.gzunicorn.hibernate.hotlinemanagement.calloutmaster.CalloutMaster;
import com.gzunicorn.hibernate.hotlinemanagement.calloutprocess.CalloutProcess;
import com.gzunicorn.hibernate.basedata.elevatorcoordinatelocation.ElevatorCoordinateLocation;
import com.gzunicorn.hibernate.infomanager.markingitemscomply.MarkingItemsComply;
import com.gzunicorn.hibernate.infomanager.markingscoreregister.MarkingScoreRegister;
import com.gzunicorn.hibernate.infomanager.markingscoreregisterdetail.MarkingScoreRegisterDetail;
import com.gzunicorn.hibernate.infomanager.markingscoreregisterfileinfo.MarkingScoreRegisterFileinfo;
import com.gzunicorn.hibernate.infomanager.qualitycheckmanagement.QualityCheckManagement;
import com.gzunicorn.hibernate.infomanager.qualitycheckprocess.QualityCheckProcess;
import com.gzunicorn.hibernate.infomanager.shouldexamineitemscomply.ShouldExamineItemsComply;
import com.gzunicorn.hibernate.infomanager.termsecurityriskscomply.TermSecurityRisksComply;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.workflow.bo.JbpmExtBridge;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class QualityCheckManagementAction extends DispatchAction {

	Log log = LogFactory.getLog(QualityCheckManagementAction.class);

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

		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		String authority="qualitycheckmanagement";
		if(name != null && name.contains("Registration")){
			authority = "qualitycheckregistration";
		}
		if(name != null && name.contains("Audit")){
			authority = "qualitycheckaudit";
		}
		if(!"toDownloadFileRecord1".equals(name)){
			/** **********开始用户权限过滤*********** */
			SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + authority, null);
			/** **********结束用户权限过滤*********** */
		}
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			ActionForward forward = super.execute(mapping, form, request,
					response);
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

		request.setAttribute("navigator.location", "  维保质量检查管理>> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		request.setAttribute("userroleid", userInfo.getRoleID());

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form, request, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport", "");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session,
					"qualityCheckManagementList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fQualityCheckManagement");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			// table.setLength(1);
			cache.updateTable(table);
			table.setSortColumn("billno");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			}else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String superviseId = tableForm.getProperty("superviseId");//督查人员
			String elevatorNo = tableForm.getProperty("elevatorNo");
			String maintContractNo = tableForm.getProperty("maintContractNo");
			String projectName = tableForm.getProperty("projectName");
			String maintDivision = tableForm.getProperty("maintDivision");
			String maintPersonnel = tableForm.getProperty("maintPersonnel");
			String submitType = tableForm.getProperty("submitType");
			String processStatus = tableForm.getProperty("processStatus");
			String maintStation = tableForm.getProperty("maintStation");
			
//			if (maintDivision == null || maintDivision == "") {
//				maintDivision = userInfo.getComID();
//				if (maintDivision.equals("00")) {
//					maintDivision = "%";
//				}
//			}
			//分部下拉框列表
			List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
			if (maintDivision == null || maintDivision.trim().equals("")) {
				HashMap hmap=(HashMap)maintDivisionList.get(0);
				maintDivision =(String)hmap.get("grcid");
			}
			request.setAttribute("maintDivisionList", maintDivisionList);
			
            List mslist= bd.getMaintStationList2(userInfo,maintDivision);
			if (maintStation == null || maintStation.trim().equals("")) {
				HashMap hmap=(HashMap)mslist.get(0);
				maintStation =(String)hmap.get("storageid");
			}
			request.setAttribute("mainStationList",mslist);
			
			String showstate="Y";
			//A03 维保经理, A02 维保分部长  
			if(userInfo.getRoleID().equals("A03") || userInfo.getRoleID().equals("A02")){
				showstate="N";
				if(processStatus==null || processStatus.equals("")){
					processStatus="2";
					tableForm.setProperty("processStatus",processStatus);
				}
			}
			request.setAttribute("showstate", showstate);
			
			Session hs = null;
			Query query = null;

			try {

				hs = HibernateUtil.getSession();

				String hql = "select q.billno,q.elevatorNo,q.maintContractNo,q.projectName," +
						"q.maintDivision,q.maintStation,q.maintPersonnel,q.submitType,q.processStatus," +
						"q.status,q.processName,q.superviseId,l.username,l2.username " +
						"from QualityCheckManagement q,Loginuser l,Loginuser l2 ";
				String condition=" where q.superviseId=l.userid and q.maintPersonnel=l2.userid ";
				if (elevatorNo != null && !elevatorNo.equals("")) {
					condition+=" and q.elevatorNo like '%" + elevatorNo.trim()+ "%'";
				}
				if (maintContractNo != null && !maintContractNo.equals("")) {
						condition+=" and q.maintContractNo like '%"+ maintContractNo.trim() + "%'";
				}
				if (projectName != null && !projectName.equals("")) {
						condition+=" and q.projectName like '%" + projectName.trim()+ "%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
						condition+=" and q.maintDivision like '" + maintDivision.trim() + "'";
				}
				if (maintStation != null && !maintStation.equals("")) {
					condition+=" and q.maintStation like '" + maintStation.trim() + "'";
				}
				if (submitType != null && !submitType.equals("")) {
						condition+=" and q.submitType='" + submitType.trim() + "'";
				}
				if (processStatus != null && !processStatus.equals("")) {
					condition+=" and q.processStatus='" + processStatus.trim() + "'";
				}
				if (superviseId != null && !superviseId.equals("")) {
						condition+=" and (q.superviseId like '%" + superviseId.trim()+ "%' or l.username like '%" + superviseId.trim()+ "%')";
				}
				if (maintPersonnel != null && !maintPersonnel.equals("")) {
					condition+=" and (q.maintPersonnel like '%" + maintPersonnel.trim()+ "%' or l2.username like '%" + maintPersonnel.trim()+ "%')";
				}
				
				if (table.getIsAscending()) {
					condition += " order by " + table.getSortColumn() + " desc";
				} else {
					condition += " order by " + table.getSortColumn() + " asc";
				}
				
				//System.out.println(hql+condition);
				
				query = hs.createQuery(hql+condition);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				cache.check(table);

				List list = query.list();
				List qualityCheckManagementList = new ArrayList();
				for (Object object : list) {
					Object[] values = (Object[]) object;
					Map map = new HashMap();
					map.put("billno", values[0]);
					map.put("elevatorNo", values[1]);
					map.put("maintContractNo", values[2]);
					map.put("projectName", values[3]);
					if(values[4]==null){
						map.put("maintDivision", values[4]);
					}else{
						map.put("maintDivision", bd.getName_Sql("Company", "comname", "comid", values[4].toString()));
					}
					if(values[5]==null){
						map.put("maintStation",values[5]);
					}else{
						map.put("maintStation", bd.getName_Sql("Storageid", "storagename", "storageid", values[5].toString()));
					}
					
					map.put("submitType", values[7]);
					map.put("processStatus", values[8]);
					map.put("status", bd.getName_Sql("ViewFlowStatus", "typename", "typeid", String.valueOf(values[9])));
					map.put("processName", values[10]);
					map.put("superviseId", values[12]);
					map.put("maintPersonnel", values[13]);
					
					qualityCheckManagementList.add(map);
				}

				table.addAll(qualityCheckManagementList);
				session.setAttribute("qualityCheckManagementList", table);

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					hs.close();
					// HibernateSessionFactory.closeSession();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			forward = mapping.findForward("qualityCheckManagementList");
		}
		return forward;
	}

	/**
	 * 维保质量检查登记>>查询列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecordRegistration(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "维保质量检查登记>> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form, request, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport", "");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session,
					"qualityCheckRegistrationList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fQualityCheckRegistration");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			// table.setLength(1);
			cache.updateTable(table);
			table.setSortColumn("billno");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String elevatorNo = tableForm.getProperty("elevatorNo");
			String maintContractNo = tableForm.getProperty("maintContractNo");
			String projectName = tableForm.getProperty("projectName");
			String maintDivision = tableForm.getProperty("maintDivision");
			if (maintDivision == null || maintDivision == "") {
				maintDivision = userInfo.getComID();
				if (maintDivision.equals("00")) {
					maintDivision = "%";
				}
			}
			Session hs = null;
			Query query = null;

			try {

				hs = HibernateUtil.getSession();

				String hql = "select q.billno,q.elevatorNo,q.maintContractNo,q.projectName,q.maintDivision,"
						+ "q.maintStation,q.maintPersonnel,q.processStatus,q.status,q.processName,q.superviseId "
						+ "from QualityCheckManagement q "
						+ "where q.submitType='Y' and superviseId='"+userInfo.getUserID()+"'";
				if (elevatorNo != null && !elevatorNo.equals("")) {
					hql += " and q.elevatorNo like '%" + elevatorNo.trim()
							+ "%'";
				}
				if (maintContractNo != null && !maintContractNo.equals("")) {
					hql += " and q.maintContractNo like '%"
							+ maintContractNo.trim() + "%'";
				}
				if (projectName != null && !projectName.equals("")) {
					hql += " and q.projectName like '%" + projectName.trim()
							+ "%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					hql += " and q.maintDivision like '" + maintDivision.trim() + "'";
				}
				if (table.getIsAscending()) {
					hql += " order by " + table.getSortColumn() + " desc";
				} else {
					hql += " order by " + table.getSortColumn() + " asc";
				}
				query = hs.createQuery(hql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				cache.check(table);
				
				List list=query.list();
				List qualityCheckRegistrationList = new ArrayList();
				for (Object object : list) {
					Object[] values = (Object[]) object;
					Map m = new HashMap();
					m.put("billno", values[0]);
					m.put("elevatorNo", values[1]);
					m.put("maintContractNo", values[2]);
					m.put("projectName", values[3]);
					if(values[4]==null){
						m.put("maintDivision", values[4]);
					}else{
						m.put("maintDivision", bd.getName_Sql("Company", "comname", "comid", values[4].toString()));
					}
					if(values[4]==null){
						m.put("maintStation",values[5]);
					}else{
						m.put("maintStation", bd.getName_Sql("Storageid", "storagename", "storageid", values[5].toString()));
					}
					if(values[6]==null){
						m.put("maintPersonnel",values[6]);
					}else{
						m.put("maintPersonnel", bd.getName_Sql("Loginuser", "username", "userid", values[6].toString()));
					}
					m.put("processStatus", values[7]);
					m.put("status",bd.getName_Sql("ViewFlowStatus", "typename", "typeid", String.valueOf(values[8])));
					m.put("processName", values[9]);
					if(values[10]==null){
						m.put("superviseId", values[10]);
					}else{
						m.put("superviseId", bd.getName_Sql("Loginuser", "username", "userid", values[10].toString()));
					}
					qualityCheckRegistrationList.add(m);
				}
				table.addAll(qualityCheckRegistrationList);
				session.setAttribute("qualityCheckRegistrationList", table);
				session.setAttribute("maintDivisionList",
						Grcnamelist1.getgrcnamelist(userInfo.getUserID()));

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					hs.close();
					// HibernateSessionFactory.closeSession();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			forward = mapping.findForward("qualityCheckRegistrationList");
		}
		return forward;
	}
	
	/**
	 * 维保质量检查审核>>查询列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecordAudit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "维保质量检查审核>> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form, request, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport", "");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session,
					"qualityCheckAuditList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fQualityCheckAudit");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			// table.setLength(1);
			cache.updateTable(table);
			table.setSortColumn("billno");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String superviseId = tableForm.getProperty("superviseId");//督查人员
			String elevatorNo = tableForm.getProperty("elevatorNo");
			String maintContractNo = tableForm.getProperty("maintContractNo");
			String projectName = tableForm.getProperty("projectName");
			String maintDivision = tableForm.getProperty("maintDivision");
			if (maintDivision == null || maintDivision == "") {
				maintDivision = userInfo.getComID();
				if (maintDivision.equals("00")) {
					maintDivision = "%";
				}
			}
			Session hs = null;
			Query query = null;

			try {

				hs = HibernateUtil.getSession();

				String hql = "select q.billno,q.elevatorNo,q.maintContractNo,q.projectName," +
						"q.maintDivision,q.maintStation,q.maintPersonnel,q.processStatus," +
						"q.status,q.processName,q.superviseId,q.tokenId,l.username" +
						" from QualityCheckManagement q,Loginuser l " +
						" where q.submitType='Y' and q.processStatus not in('0','1') and q.superviseId=l.userid";
				if (elevatorNo != null && !elevatorNo.equals("")) {
					hql += " and q.elevatorNo like '%" + elevatorNo.trim()
							+ "%'";
				}
				if (maintContractNo != null && !maintContractNo.equals("")) {
					hql += " and q.maintContractNo like '%"
							+ maintContractNo.trim() + "%'";
				}
				if (projectName != null && !projectName.equals("")) {
					hql += " and q.projectName like '%" + projectName.trim()
							+ "%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					hql += " and q.maintDivision like '" + maintDivision.trim() + "'";
				}
				if (superviseId != null && !superviseId.equals("")) {
					hql+=" and (q.superviseId like '%" + superviseId.trim()+ "%' or l.username like '%" + superviseId.trim()+ "%')";
				}
				
				if (table.getIsAscending()) {
					hql += " order by " + table.getSortColumn() + " desc";
				} else {
					hql += " order by " + table.getSortColumn() + " asc";
				}
				query = hs.createQuery(hql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				cache.check(table);

				List list = query.list();
				List qualityCheckRegistrationList = new ArrayList();
				for (Object object : list) {
					Object[] values = (Object[]) object;
					Map m = new HashMap();
					m.put("billno", values[0]);
					m.put("elevatorNo", values[1]);
					m.put("maintContractNo", values[2]);
					m.put("projectName", values[3]);
					if(values[4]==null){
						m.put("maintDivision", values[4]);
					}else{
						m.put("maintDivision", bd.getName_Sql("Company", "comname", "comid", values[4].toString()));
					}
					if(values[4]==null){
						m.put("maintStation",values[5]);
					}else{
						m.put("maintStation", bd.getName_Sql("Storageid", "storagename", "storageid", values[5].toString()));
					}
					if(values[6]==null){
						m.put("maintPersonnel",values[6]);
					}else{
						m.put("maintPersonnel", bd.getName_Sql("Loginuser", "username", "userid", values[6].toString()));
					}
					m.put("processStatus", values[7]);
					m.put("status", values[8]);
					m.put("statusName", bd.getName_Sql("ViewFlowStatus", "typename", "typeid", String.valueOf(values[8])));
					m.put("processName", values[9]);
					m.put("tokenId", values[11]);
					m.put("superviseId", values[12]);
					
					qualityCheckRegistrationList.add(m);
				}

				table.addAll(qualityCheckRegistrationList);
				session.setAttribute("qualityCheckAuditList", table);
				request.setAttribute("maintDivisionList",Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
				request.setAttribute("flowname", WorkFlowConfig.getProcessDefine("qualitycheckmanagementProcessName"));

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					hs.close();
					// HibernateSessionFactory.closeSession();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			forward = mapping.findForward("qualityCheckAuditList");
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

		request.setAttribute("navigator.location", "维保质量检查管理 >> 查看");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;

		String id = (String) dform.get("id");
		display(dform, request, errors, "display");
		
		request.setAttribute("display", "yes");
		forward = mapping.findForward("qualityCheckManagementDisplay");
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	public ActionForward toRegistrationDisplay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "维保质量检查登记 >> 查看");
//		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;

//		String id = (String) dform.get("id");
		display(form, request, errors, "display");
		request.setAttribute("dispose", "dispose");
		request.setAttribute("display", "yes");
		forward = mapping.findForward("qualityCheckRegistrationDisplay");
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	public ActionForward toAuditDisplay(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "维保质量检查审核 >> 查看");
//		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;
		
		display(form, request, errors, "display");

//		String id = (String) dform.get("id");
		/*Session hs = null;
		QualityCheckManagement qualitycheckmanagement = null;
		String hql = null;
		List list = null;
		List processApproveList=null;
//		Map map = null;
		QualityCheckManagement manage=new QualityCheckManagement();
		List mtclist = new ArrayList();
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				hql="select a,b.comname,c.storagename from QualityCheckManagement a,Company b,Storageid c"+
						" where a.maintDivision=b.comid and a.maintStation=c.storageid and a.billno='"+(String)dform.get("id")+"'";
						list=hs.createQuery(hql).list();
						//System.out.println(list.size());
						for(Object object : list){
							Object[] objs=(Object[])object;
							manage=(QualityCheckManagement) objs[0];
							manage.setMaintDivision(objs[1].toString());
							manage.setMaintStation(objs[2].toString());
							manage.setMaintPersonnel(bd.getName("Loginuser", "username", "userid", manage.getMaintPersonnel()));
							manage.setChecksPeople(bd.getName("Loginuser", "username", "userid", manage.getChecksPeople()));
							manage.setPartMinisters(bd.getName("Loginuser", "username", "userid", manage.getPartMinisters()));
							manage.setSuperviseId(bd.getName("Loginuser", "username", "userid", manage.getSuperviseId()));
						}
				hql = "select q.billno,q.elevatorNo,q.maintContractNo,q.salesContractNo,q.projectName,q.maintDivision,q.maintStation,q.maintPersonnel,q.checksPeople,q.checksDateTime,q.totalPoints,q.scoreLevel,q.partMinisters,q.partMinistersRem,q.remDate,q.supervOpinion,q.superviseId,q.supervisePhone,q.assessRem,q.deductMoney from QualityCheckManagement q where q.billno='"
						+ (String) dform.get("id") + "'";
				list = hs.createQuery(hql).list();
				for (Object object : list) {
					Object[] values = (Object[]) object;
					map = new HashMap();
					map.put("billno", values[0]);
					map.put("elevatorNo", values[1]);
					map.put("maintContractNo", values[2]);
					map.put("salesContractNo", values[3]);
					map.put("projectName", values[4]);
					if(values[5]==null || values[5].equals("")){
						map.put("maintDivision", "");
					}else{
						map.put("maintDivision", bd.getName_Sql("Company", "comname", "comid", String.valueOf(values[5])));
					}
					if(values[6]==null || values[6].equals("")){
						map.put("maintStation", "");
					}else{
						map.put("maintStation", bd.getName_Sql("Storageid", "storagename", "storageid", values[6].toString()));
					}
					map.put("maintPersonnel", bd.getName("Loginuser", "username", "userid", values[7].toString()));
					if(values[8]==null || values[8].equals("")){
						map.put("checksPeople", "");
					}else{
						map.put("checksPeople", bd.getName("Loginuser", "username", "userid", values[8].toString()));
					}
					map.put("checksDateTime", values[9]);
					map.put("totalPoints", values[10]);
					map.put("scoreLevel", values[11]);
					if(values[12]==null || values[12].equals("")){
						map.put("partMinisters", "");
					}else{
						map.put("partMinisters", bd.getName("Loginuser", "username", "userid", values[12].toString()));
					}
					map.put("partMinistersRem", values[13]);
					map.put("remDate", values[14]);
					map.put("supervOpinion", values[15]);
					if(values[16]!=null){
						map.put("superviseId", bd.getName_Sql("Loginuser", "username", "userid", values[16].toString()));
					}else{
						map.put("superviseId",values[16]);
					}
					
					map.put("supervisePhone", values[17]);
					map.put("assessRem", values[18]);
					map.put("deductMoney", values[19]);
					
				}
				
				//维保质量评分表
				hql="from MarkingScoreRegister where billno='"+id+"'";
				list=hs.createQuery(hql).list();
				if(list!=null && list.size()>0){
					for(Object object : list){
						MarkingScoreRegister msr=(MarkingScoreRegister)object;
						Map m=new HashMap();
						m.put("jnlno", msr.getJnlno());
						m.put("numno", msr.getNumno());
						m.put("orderBy", msr.getOrderBy());
						m.put("checkItem", msr.getCheckItem());
						m.put("fraction", msr.getFraction());
						List fileList=hs.createQuery("from MarkingScoreRegisterFileinfo where jnlno='"+msr.getJnlno()+"'").list();
						m.put("fileList", fileList);
						mtclist.add(m);
					}
				}
				
				//审批流程信息
				processApproveList = hs.createQuery("from QualityCheckProcess where billno = '"+(String) dform.get("id")+ "' order by itemId").list();
				for (Object object : processApproveList) {
					QualityCheckProcess process = (QualityCheckProcess) object;
					process.setUserId(bd.getName(hs, "Loginuser", "username", "userid", process.getUserId()));
				}

				if (qualitycheckmanagement == null) {
					errors.add(
							ActionErrors.GLOBAL_ERROR,
							new ActionError(
									"qualitycheckmanagement.display.recordnotfounterror"));
				}
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
			request.setAttribute("qualityCheckManagementBean", manage);
			request.setAttribute("markingItemsComplyList", mtclist);
//			request.setAttribute("returnListMethod", returnMethod);
			request.setAttribute("processApproveList", processApproveList);
			forward = mapping.findForward("qualityCheckAuditDisplay");

		}*/
		request.setAttribute("display", "yes");
		forward = mapping.findForward("qualityCheckAuditDisplay");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * 
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

		request.setAttribute("navigator.location", "维保质量检查管理>> 添加");
		DynaActionForm dform = (DynaActionForm) form;

		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}
		
		Session hs = null;
		try {
			hs=HibernateUtil.getSession();

			//只出 维保站长A49，维保工A50,维保经理 A03，维修技术员A53
			String hql="select "
					+"case when s.ParentStorageID<>'0' then s.ParentStorageID else s.storageid end as storageid,"
					+"case when s.ParentStorageID<>'0' then s2.StorageName else s.StorageName end as storagename,"
					+"a.userid,a.username,a.phone "
					+"from loginuser a,StorageID s left join StorageID s2 on s.ParentStorageID=s2.StorageID "
					+"where a.StorageID=s.StorageID and a.enabledflag='Y' and a.roleid in('A49','A50') "
					+"order by a.storageid,a.userid";

			List list=new ArrayList();
			ResultSet rs=hs.connection().prepareStatement(hql).executeQuery();
			while(rs.next()){
				HashMap map=new HashMap();
				map.put("storageid", rs.getString("storageid"));
				map.put("storagename", rs.getString("storagename"));
				map.put("userid", rs.getString("userid"));
				map.put("username", rs.getString("username"));
				map.put("phone", rs.getString("phone"));
				list.add(map);
			}
			
			request.setAttribute("userstorageidlist", list);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}
		
		return mapping.findForward("qualityCheckManagementAdd");
	}

	/**
	 * 
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

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;
		MarkingItemsComply marking = null;
		ShouldExamineItemsComply should = null;
		TermSecurityRisksComply term = null;
		String[] rowids = request.getParameterValues("rowid");// 行号
		String[] elevatorNos = request.getParameterValues("elevatorNo");// 电梯编号
		String[] maintContractNos =request.getParameterValues("maintContractNo");// 维保合同号
		String[] projectNames = request.getParameterValues("projectName");// 项目名称
		String[] maintDivisions = request.getParameterValues("maintDivision");// 维保分部
		String[] maintStations = request.getParameterValues("maintStation");// 维保站
		String[] maintPersonnels = request.getParameterValues("maintPersonnel");// 维保工
		String[] personnelPhones = request.getParameterValues("personnelPhone");// 维保工联系电话
		String[] salesContractNos = request.getParameterValues("salesContractNo");// 销售合同号
		String[] elevatorTypes = request.getParameterValues("elevatorType");// 电梯类型
		String[] checkrem = request.getParameterValues("checkrem");// 电梯类型
		String issubmit = request.getParameter("issubmit");// 提交标志
		String superviseId=(String)dform.get("superviseId");//现场督察人员
		String supervisePhone=(String)dform.get("supervisePhone");//督察人员联系电话

		try {
			String todayDate = CommonUtil.getToday();
			String year = todayDate.substring(2, 4);
			String[] billno = CommonUtil.getBillno(year,
					"QualityCheckManagement", rowids.length);// 流水号
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
            if(rowids!=null&&rowids.length>0){
            	for(int i=0;i<rowids.length;i++){
            	QualityCheckManagement qualitycheckmanagement = new QualityCheckManagement();
    			qualitycheckmanagement.setBillno(billno[i]);
    			qualitycheckmanagement.setElevatorNo(elevatorNos[i]);
    			qualitycheckmanagement.setMaintContractNo(maintContractNos[i]);
    			qualitycheckmanagement.setProjectName(projectNames[i]);
    			qualitycheckmanagement.setMaintDivision(bd.getName("Company","comid", "comname", maintDivisions[i]));
    			qualitycheckmanagement.setMaintStation(bd.getName("Storageid","storageid", "storagename", maintStations[i]));
    			qualitycheckmanagement.setMaintPersonnel(maintPersonnels[i]);
    			qualitycheckmanagement.setPersonnelPhone(personnelPhones[i]);
    			qualitycheckmanagement.setElevatorType(elevatorTypes[i]);
    			qualitycheckmanagement.setSubmitType(issubmit);
    			qualitycheckmanagement.setOperId(userInfo.getUserID());// 录入人
    			qualitycheckmanagement.setOperDate(CommonUtil.getToday());// 录入时间
    			qualitycheckmanagement.setSalesContractNo(salesContractNos[i]);
    			qualitycheckmanagement.setProcessStatus("0");
    			qualitycheckmanagement.setSuperviseId(superviseId);
    			qualitycheckmanagement.setSupervisePhone(supervisePhone);
    			qualitycheckmanagement.setStatus(-1);
    			qualitycheckmanagement.setR4(checkrem[i]);
    			
    			hs.save(qualitycheckmanagement);
    			hs.flush();
            	}
            }

			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"qualitycheckmanagement.insert.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Insert error!");
		} catch (Exception e1) {
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

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"insert.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"insert.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				forward = mapping.findForward("returnAdd");
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
	 * 跳转到修改级别页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "维保质量检查管理 >> 修改");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		ActionForward forward = null;
		String id = null;

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("billno");
		} else {
			id = (String) dform.get("id");
		}

		Session hs = null;
		QualityCheckManagement qualitycheckmanagement = null;
		List<MarkingItems> markingItemsComplyList = new ArrayList<MarkingItems>();
		List<TermSecurityRisks> termSecurityRisksComplyList = new ArrayList<TermSecurityRisks>();
		List<ShouldExamineItems> shouldExamineItemsComplyList = new ArrayList<ShouldExamineItems>();
		if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					
					//只出 维保站长A49，维保工A50,维保经理 A03，维修技术员A53
					String sql="select "
							+"case when s.ParentStorageID<>'0' then s.ParentStorageID else s.storageid end as storageid,"
							+"case when s.ParentStorageID<>'0' then s2.StorageName else s.StorageName end as storagename,"
							+"a.userid,a.username,a.phone "
							+"from loginuser a,StorageID s left join StorageID s2 on s.ParentStorageID=s2.StorageID "
							+"where a.StorageID=s.StorageID and a.enabledflag='Y' and a.roleid in('A49','A50') "
							+"order by a.storageid,a.userid";

					List list=new ArrayList();
					ResultSet rs=hs.connection().prepareStatement(sql).executeQuery();
					while(rs.next()){
						HashMap map=new HashMap();
						map.put("storageid", rs.getString("storageid"));
						map.put("storagename", rs.getString("storagename"));
						map.put("userid", rs.getString("userid"));
						map.put("username", rs.getString("username"));
						map.put("phone", rs.getString("phone"));
						list.add(map);
					}
					
					request.setAttribute("userstorageidlist", list);
					
					qualitycheckmanagement = (QualityCheckManagement) hs.get(QualityCheckManagement.class, id);
					String maintDivision = bd.getName_Sql("Company", "comname","comid", qualitycheckmanagement.getMaintDivision());
					String maintStation = bd.getName("Storageid","storagename", "storageid",qualitycheckmanagement.getMaintStation());
					//String maintPersonnel = bd.getName("Loginuser", "username","userid",qualitycheckmanagement.getMaintPersonnel());
					String supervname=bd.getName("Loginuser", "username","userid",qualitycheckmanagement.getSuperviseId());
					request.setAttribute("supervname", supervname);
					
					String eletype=qualitycheckmanagement.getElevatorType();
					if(eletype!=null && eletype.equals("T")){
						request.setAttribute("elevatorTypeName", "直梯");
					}else if(eletype!=null && eletype.equals("F")){
						request.setAttribute("elevatorTypeName", "扶梯");
					}
					
					//只出 维保站长A49，维保工A50,维保经理 A03，维修技术员A53
					String hql="select a from Loginuser a where a.storageid like '"+qualitycheckmanagement.getMaintStation()+"%' " +
							"and a.enabledflag='Y' and a.roleid in('A49','A50') order by a.roleid,a.userid";
					List list2=hs.createQuery(hql).list();
					request.setAttribute("userstorageidlist2", list2);
					
					qualitycheckmanagement.setMaintDivision(maintDivision);
					qualitycheckmanagement.setMaintStation(maintStation);
					//qualitycheckmanagement.setMaintPersonnel(maintPersonnel);
					//qualitycheckmanagement.setSuperviseId(supervname);
					
					

				} catch (DataStoreException e) {
					e.printStackTrace();
				} catch (HibernateException e1) {
					e1.printStackTrace();
				} catch (SQLException e2) {
					e2.printStackTrace();
				} finally {
					try {
						hs.close();
					} catch (HibernateException hex) {
						log.error(hex.getMessage());
						DebugUtil
								.print(hex, "HibernateUtil Hibernate Session ");
					}
				}
			}
			request.setAttribute("showupdarte", "update");
			request.setAttribute("qualityCheckManagementBean",qualitycheckmanagement);
			forward = mapping.findForward("qualityCheckManagementModify");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * 紧急级别修改
	 * 
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

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String elevatorNo = (String) dform.get("elevatorNo");// 电梯编号
		String superviseId = (String) dform.get("superviseId");//督察人员
		String supervisePhone=(String)dform.get("supervisePhone");//督察人员联系电话
		String maintPersonnel = (String) dform.get("maintPersonnel");// 维保工
		String personnelPhone = (String) dform.get("personnelPhone");// 维保工联系电话
		
		String maintContractNo =(String) dform.get("maintContractNo");// 维保合同号
		String projectName = (String) dform.get("projectName");// 项目名称
		String maintDivision = (String) dform.get("maintDivision");// 维保分部
		String maintStation = (String) dform.get("maintStation");// 维保站
		String salesContractNo = (String) dform.get("salesContractNo");// 销售合同号
		String eletype = (String) dform.get("elevatorType");// 电梯类型
		String r4 = (String) dform.get("r4");//备注
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			QualityCheckManagement qm = (QualityCheckManagement) hs.get(QualityCheckManagement.class, (String) dform.get("id"));

			qm.setElevatorNo(elevatorNo);
			qm.setSuperviseId(superviseId);
			qm.setSupervisePhone(supervisePhone);
			qm.setMaintPersonnel(maintPersonnel);
			qm.setPersonnelPhone(personnelPhone);
			qm.setOperId(userInfo.getUserID());// 录入人
			qm.setOperDate(CommonUtil.getToday());// 录入时间
			
			qm.setMaintContractNo(maintContractNo);
			qm.setProjectName(projectName);
			qm.setMaintDivision(bd.getName("Company","comid", "comname", maintDivision));
			qm.setMaintStation(bd.getName("Storageid","storageid", "storagename", maintStation));
			qm.setSalesContractNo(salesContractNo);
			qm.setElevatorType(eletype);
			qm.setR4(r4);

			hs.update(qm);
			tx.commit();
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"qualitycheckmanagement.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		forward = mapping.findForward("returnList");
		return forward;
	}

	/**
	 * 删除紧急级别
	 * 
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
		int len;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			QualityCheckManagement qualitycheckmanagement = (QualityCheckManagement) hs.get(QualityCheckManagement.class, (String) dform.get("id"));

			if (qualitycheckmanagement != null) {
				//if (qualitycheckmanagement.getSubmitType() == "Y") {
				//	errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.foreignkeyerror"));
				//} else {
				
					//删除维保质量检查管理审批流程相关信息
					String delsql="delete QualityCheckProcess where billno='"+ (String) dform.get("id") + "'";
					hs.connection().prepareStatement(delsql).executeUpdate();
					//删除维保质量评分表登记明细
					delsql="delete a from MarkingScoreRegisterDetail a,MarkingScoreRegister b "
							+ "where a.jnlno=b.jnlno and b.billno='"+ (String) dform.get("id") + "'";
					hs.connection().prepareStatement(delsql).executeUpdate();
					//删除维保质量评分表附件表
					delsql="delete a from MarkingScoreRegisterFileinfo a,MarkingScoreRegister b "
							+ "where a.jnlno=b.jnlno and b.billno='"+ (String) dform.get("id") + "'";
					hs.connection().prepareStatement(delsql).executeUpdate();
					//删除维保质量评分表登记主信息
					delsql="delete MarkingScoreRegister where billno='"+ (String) dform.get("id") + "'";
					hs.connection().prepareStatement(delsql).executeUpdate();
					
					hs.delete(qualitycheckmanagement);
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.succeed"));
				//}
			}
			tx.commit();
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
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
	 * 删除紧急级别
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toSubmitRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		int len;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			QualityCheckManagement qualitycheckmanagement = (QualityCheckManagement) hs
					.get(QualityCheckManagement.class, (String) dform.get("id"));

			if (qualitycheckmanagement != null) {
				if (qualitycheckmanagement.getSubmitType() == "Y") {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"submit.foreignkeyerror"));
				} else {
					String hql="update QualityCheckManagement set submitType='Y' where billno='"+(String)dform.get("id")+"'";
					hs.createQuery(hql).executeUpdate();
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"submit.succeed"));
				}
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"submit.foreignkeyerror"));
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

		String naigation = new String();
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		String elevatorNo = tableForm.getProperty("elevatorNo");
		String maintContractNo = tableForm.getProperty("maintContractNo");
		String projectName = tableForm.getProperty("projectName");
		String maintDivision = tableForm.getProperty("maintDivision");
		String maintStation = tableForm.getProperty("maintStation");
		String maintPersonnel = tableForm.getProperty("maintPersonnel");
		String submitType = tableForm.getProperty("submitType");
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(QualityCheckManagement.class);
			if (elevatorNo != null && !elevatorNo.equals("")) {
				criteria.add(Expression.like("elevatorNo",
						"%" + elevatorNo.trim() + "%"));
			}
			if (maintContractNo != null && !maintContractNo.equals("")) {
				criteria.add(Expression.like("maintContractNo", "%"
						+ maintContractNo.trim() + "%"));
			}
			if (projectName != null && !projectName.equals("")) {
				criteria.add(Expression.like("projectName",
						"%" + projectName.trim() + "%"));
			}

			if (maintDivision != null && !maintDivision.equals("")) {
				criteria.add(Expression.like("maintDivision", "%"
						+ maintDivision.trim() + "%"));
			}
			if (maintStation != null && !maintStation.equals("")) {
				criteria.add(Expression.like("maintStation",
						"%" + maintStation.trim() + "%"));
			}
			if (maintPersonnel != null && !maintPersonnel.equals("")) {
				criteria.add(Expression.like("maintPersonnel", maintPersonnel));
			}
			if (submitType != null && !submitType.equals("")) {
				criteria.add(Expression.eq("submitType", submitType));
			}

			criteria.addOrder(Order.asc("billno"));

			List roleList = criteria.list();

			XSSFSheet sheet = wb.createSheet("维保质量检查管理");

			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			String[] columnNames = { "billno", "maintDivision", "maintStation",
					"maintPersonnel", "projectName", "maintContractNo",
					"elevatorNo", "checksPeople", "checksDateTime",
					"totalPoints", "scoreLevel", "submitType", "supervOpinion",
					"partMinisters", "partMinistersRem", "remDate", "operId",
					"operDate" };

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);

				for (int i = 0; i < columnNames.length; i++) {
					XSSFCell cell0 = row0.createCell((short) i);
					cell0.setCellValue(messages.getMessage(locale,
							"qualityCheckManagement." + columnNames[i]));
				}

				Class<?> superClazz = QualityCheckManagement.class
						.getSuperclass();
				for (int i = 0; i < l; i++) {
					QualityCheckManagement master = (QualityCheckManagement) roleList
							.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i + 1);
					for (int j = 0; j < columnNames.length; j++) {
						// 创建Excel列
						XSSFCell cell = row.createCell((short) j);
						cell.setCellValue(getValue(master, superClazz,
								columnNames[j]));
					}
				}
			}

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

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="
				+ URLEncoder.encode("维保质量检查管理", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());

		return response;
	}

	/**
	 * 获得对象指定的get方法并返回执行该get方法后的值
	 * 
	 * @param object
	 *            javabean对象
	 * @param superClazz
	 *            object的类，子类没有相应get方法时请传入object的父类
	 * @param fieldName
	 *            属性名
	 * @return ActionForward
	 */
	private String getValue(Object object, Class<?> superClazz, String fieldName) {
		String value = null;
		String methodName = "get"
				+ fieldName.replaceFirst(fieldName.substring(0, 1), fieldName
						.substring(0, 1).toUpperCase());
		try {
			Method method = superClazz.getMethod(methodName);
			value = method.invoke(object, null) + "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;

	}

	/**
	 * 跳转到登记页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public ActionForward toPrepareRegistrationRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "维保质量检查登记 >> 登记");
//		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		ActionForward forward = null;
		String id = null;

		display(form, request, errors, "dispose");

		forward = mapping.findForward("qualityCheckRegistrationAdd");
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * 登记
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toRegistrationRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		ActionForward forward = null;
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		JbpmExtBridge jbpmExtBridge=null;
		
		//检查附件不能超过多少M。一次上传的文件总大小不能超过5M,保存失败!
		Boolean maxLengthExceeded = (Boolean) request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);			
		if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())){
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("doc.file.size.check.save"));
			forward = mapping.findForward("returnRegitrationList");
		}else{
		
			String billno=(String)dform.get("billno");
			String elevatorNo = (String) dform.get("elevatorNo");
			String checksDateTime = (String) dform.get("checksDateTime");
			
			String issubmit = (String) dform.get("issubmit");
			String processStatus=(String)dform.get("isdispose");
			String supervOpinion = (String) dform.get("supervOpinion");
			double totalPoints = (Double)dform.get("totalPoints");
			String scoreLevel = (String)dform.get("scoreLevel");
			String userid=userInfo.getUserID();
			
			String[] r5=request.getParameterValues("r5");//选择的维保参与人员
			String r5str="";
			if(r5!=null && r5.length>0){
				for(int r=0;r<r5.length;r++){
   				 if(r==r5.length-1){
   					r5str+=r5[r];
   				 }else{
   					r5str+=r5[r]+",";
   				 }
   			 }
			}
			
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				
				this.dispose(form, request, errors, billno);
				List fileList=this.savePicter(form,request,response,"MTSComply.file.upload.folder", billno);
				boolean iswin=this.savePicterTodb(hs,request,fileList,billno);
				
				QualityCheckManagement manage=(QualityCheckManagement) hs.get(QualityCheckManagement.class, billno);
				manage.setElevatorNo(elevatorNo);//电梯编号
				manage.setChecksPeople(userid);
				manage.setChecksDateTime(checksDateTime);
				manage.setProcessStatus(processStatus);
				manage.setSupervOpinion(supervOpinion);
				manage.setTotalPoints(totalPoints);
				manage.setScoreLevel(scoreLevel);
				manage.setR5(r5str);
				if("2".equals(processStatus)){
					String processDefineID = Grcnamelist1.getProcessDefineID("qualitycheckmanagement", manage.getSuperviseId());// 流程环节id
					if(processDefineID == null || processDefineID.equals("")){
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("error.string","<font color='red'>未配置审批流程信息，不能启动！</font>"));
						throw new Exception();
					}
								
					/**=============== 启动新流程实例开始 ===================**/
					HashMap paraMap = new HashMap();
					jbpmExtBridge=new JbpmExtBridge();
					ProcessBean pd = null;		
					pd = jbpmExtBridge.getPb();

					Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "督查组长审核", manage.getSuperviseId());// 添加审核人
					pd=jbpmExtBridge.startProcess(WorkFlowConfig.getProcessDefine(processDefineID),userid,userid,billno,"",paraMap);
					/**==================== 流程结束 =======================**/
					manage.setStatus(pd.getStatus());
					manage.setTokenId(pd.getToken());
					manage.setProcessName("督查组长审核");
				}
				hs.save(manage);
	
				tx.commit();
			} catch (HibernateException e2) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"qualitycheckmanagement.update.duplicatekeyerror"));
				try {
					tx.rollback();
					if (jbpmExtBridge != null) {
						jbpmExtBridge.setRollBack();
					}
				} catch (HibernateException e3) {
					log.error(e3.getMessage());
					DebugUtil.print(e3, "Hibernate Transaction rollback error!");
				}
				log.error(e2.getMessage());
				DebugUtil.print(e2, "Hibernate region Update error!");
			} catch (Exception e1) {
				e1.printStackTrace();
				log.error(e1.getMessage());
				DebugUtil.print(e1, "Hibernate region Update error!");
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
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.success"));
					forward = mapping.findForward("returnRegitrationList");
				} else {
					// return modify page
					if (errors.isEmpty()) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"update.success"));
					} else {
						request.setAttribute("error", "Yes");
					}
					request.setAttribute("returnMethod", "toSearchRecordRegistration");
					forward = mapping.findForward("returnRegistration");
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
	
	/*
	 * 
	 */
	public void dispose(ActionForm form, HttpServletRequest request,ActionErrors errors,String billno){

		Session hs = null;
		Transaction tx = null;
		MarkingScoreRegister msr=null;
		MarkingScoreRegisterDetail detail=null;
		DynaActionForm dform = (DynaActionForm) form;

		String scores=(String) dform.get("Scores");
		String details=(String) dform.get("details");
		
		try {
			hs=HibernateUtil.getSession();
			tx=hs.beginTransaction();

			String jnlnostr="";
			List list = JSONUtil.jsonToList(scores, "scorce");
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					msr=new MarkingScoreRegister();
					Map m=(Map)list.get(i);

					BeanUtils.populate(msr, (Map)list.get(i));
					msr.setBillno(billno);
					msr.setRem((String)m.get("rem"));
					msr.setIsDelete("N");
					if(msr.getJnlno()==null || "".equals(msr.getJnlno())){
						msr.setJnlno(CommonUtil.getBillno(CommonUtil.getToday().substring(2,4), "MarkingScoreRegister", 1)[0]);
					}else{
						hs.createQuery("delete MarkingScoreRegisterDetail where jnlno='"+msr.getJnlno()+"'").executeUpdate();
						hs.delete(msr);
					}
					hs.save(msr);
					
					if(i==list.size()-1){
						jnlnostr+=msr.getJnlno();
					}else{
						jnlnostr+=msr.getJnlno()+"','";
					}
					
					List detailList=JSONUtil.jsonToList(details.split("\\|")[i],"detail"+i);
					if(detailList!=null && detailList.size()>0){
						for(int j=0;j<detailList.size();j++){
							detail=new MarkingScoreRegisterDetail();
							BeanUtils.populate(detail, (Map)detailList.get(j));
							if(detail.getJnlno()==null || "".equals(detail.getJnlno())){
								detail.setJnlno(msr.getJnlno());
							}
							hs.save(detail);
						}
					}
				}
				
				//删除明细
				String delsql="delete a from MarkingScoreRegisterDetail a,MarkingScoreRegister b"
						+ " where a.jnlno=b.jnlno and  b.billno='"+billno+"' and b.jnlno not in('"+jnlnostr+"')";
				hs.connection().prepareStatement(delsql).execute();
				//删除附件
				delsql="delete a from MarkingScoreRegisterFileinfo a,MarkingScoreRegister b"
						+ " where a.jnlno=b.jnlno and  b.billno='"+billno+"' and b.jnlno not in('"+jnlnostr+"')";
				hs.connection().prepareStatement(delsql).execute();
				//删除主信息
				delsql="delete MarkingScoreRegister where billno='"+billno+"' and jnlno not in('"+jnlnostr+"')";
				hs.connection().prepareStatement(delsql).execute();
			}
			
			
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
		} finally{
			if(hs!=null){
				hs.close();
			}
		}
	
	}
	
	/*修改分数 */
	public static void registration(Session hs, String tablename, String w,
			String[] mac, String field, String[] values,String billno) {
		for (int i = 0; i < values.length; i++) {
			String hql = "update " + tablename + " set " + field + "='"
					+ values[i] + "' where " + w + "='" + mac[i] + "' and billno='"+billno+"'";
			hs.createQuery(hql).executeUpdate();
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
		
		
		FormFile formFile = null;
		Fileinfo file = null;
		//FileItem fileitem=null;
		if (form.getMultipartRequestHandler() != null) {
			Hashtable hash = form.getMultipartRequestHandler().getFileElements();
			
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
								String primary=request.getParameter("primary_"+key);
								String newFileName="MarkingScoreRegisterFileinfo"+"_"+billno+"_"+key+"_"+formFile.getFileName();
								
								map.put("primary", primary);
								map.put("newFileName", newFileName);
								map.put("fileName", formFile.getFileName());
								map.put("fileType", formFile.getContentType());
								map.put("fileSize",formFile.getFileSize());
								map.put("filePath", folder+"MarkingScoreRegisterFileinfo"+"/"+newFileName);
								map.put("billno", billno);
								//保存文件到系统
								hf.createFile(formFile.getInputStream(), folder+"MarkingScoreRegisterFileinfo"+"/", newFileName);
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
	public boolean savePicterTodb(Session hs,HttpServletRequest request,List fileInfoList,String billno){
		boolean saveFlag = true;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		if(null != fileInfoList && !fileInfoList.isEmpty()){
			String sql="";
			MarkingScoreRegister msr=null;
			try {
				int len = fileInfoList.size();
				for(int i = 0 ; i < len ; i++){
					Map map = (Map)fileInfoList.get(i);
					List list=hs.createQuery("from MarkingScoreRegister where billno='"+billno+"' and msId='"+(String)map.get("primary")+"'").list();
//					//System.out.println((String)map.get("primary"));
					if(list!=null && list.size()>0){
						for(int j=0;j<list.size();j++){
							msr=(MarkingScoreRegister) list.get(0);
						}
					}
					String newFilename=(String) map.get("newFileName");
					String primary=(String)map.get("primary");
					 
					MarkingScoreRegisterFileinfo fileInfo=new MarkingScoreRegisterFileinfo();
					fileInfo.setJnlno(msr.getJnlno());
					fileInfo.setOldFileName((String)map.get("fileName"));
					fileInfo.setNewFileName((String)map.get("newFileName"));
					fileInfo.setFileSize(Double.valueOf(map.get("fileSize").toString()));
					fileInfo.setFilePath((String)map.get("filePath"));
					fileInfo.setFileFormat((String)map.get("fileType"));
					fileInfo.setUploadDate(CommonUtil.getNowTime());
					fileInfo.setUploader(userInfo.getUserID());
					
					hs.save(fileInfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
				saveFlag = false;
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
	public ActionForward toDeleteFileRegistration(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) { 
		
		Session hs = null;
		Transaction tx = null;
		String id = request.getParameter("filesid");
		List list=null;
		
//		String tableName=request.getParameter("tablename");
//		String primaryId=request.getParameter("primaryId");
//		String primary=request.getParameter("primary");
//		String billno=request.getParameter("billno");
		String folder = request.getParameter("folder");
//		double totalPoints=Double.valueOf(request.getParameter("totalPoints"));
		String Level="";
		//创建输出流对象
        PrintWriter out=null;
        //依据验证结果输出不同的数据信息	 
		if(null == folder || "".equals(folder)){
			folder ="MTSComply.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if(id!=null && id.length()>0){
				id=URLDecoder.decode(id, "utf-8");
				MarkingScoreRegisterFileinfo fileInfo=(MarkingScoreRegisterFileinfo) hs.get(MarkingScoreRegisterFileinfo.class, Integer.valueOf(id));
				String fileName=fileInfo.getNewFileName();
				if(fileInfo!=null){
					hs.createQuery("delete MarkingScoreRegisterFileinfo where fileSid='"+fileInfo.getFileSid()+"'").executeUpdate();
				}
				
				/*//System.out.println(primary);
				String sql="update "+tableName+" set annexPath=null where billno='"+billno+"' and "+primaryId+"='"+primary+"'";
				hs.connection().prepareStatement(sql).execute();
				hs.flush();*/
				
				HandleFile hf = new HandleFileImpA();
				String localpath=folder+"MarkingScoreRegisterFileinfo"+"/"+fileName;
				hf.delFile(localpath);
			}
			
			tx.commit();
			response.setContentType("text/xml; charset=UTF-8");
			
			out = response.getWriter();
			
			out.println("<response>");
			out.println("<res>" + "Y" + "</res>");
			out.println("</response>");
			
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
	
	/**
	 * 跳转到审核级别页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public ActionForward toPrepareAuditRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "维保质量检查审核 >> 审核");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);

		ActionForward forward = null;
		String id = null;
		String returnMethod=(String)dform.get("returnMethod");

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("billno");
		} else {
			id = (String) dform.get("id");
		}

		Session hs = null;
		Map qualitycheckmanagement = new HashMap();
		List list = null;
		String hql = "";
		List markingItemsComplyList = new ArrayList();
		List termSecurityRisksComplyList = new ArrayList();
		List shouldExamineItemsComplyList = new ArrayList();
		if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					String sql="select sum(fraction) sumf from MarkingItems";
					String fraction="";
					List flist=hs.createSQLQuery(sql).list();
					if(flist!=null){
						fraction=flist.get(0).toString();
					}
					hql = "select q.billno,q.elevatorNo,q.maintContractNo,q.salesContractNo,q.projectName,q.maintDivision,q.maintStation,q.maintPersonnel,q.checksPeople,q.checksDateTime,q.totalPoints,q.scoreLevel,q.supervOpinion from QualityCheckManagement q where billno='"
							+ id + "'";
					list = hs.createQuery(hql).list();
					for (Object object : list) {
						Object[] values = (Object[]) object;
						qualitycheckmanagement.put("billno", values[0]);
						qualitycheckmanagement.put("elevatorNo", values[1]);
						qualitycheckmanagement
								.put("maintContractNo", values[2]);
						qualitycheckmanagement
								.put("salesContractNo", values[3]);
						qualitycheckmanagement.put("projectName", values[4]);
						if(values[5]==null){
							qualitycheckmanagement.put("maintDivision", values[5]);
						}else{
							qualitycheckmanagement.put("maintDivision", bd.getName_Sql("Company", "comname", "comid", values[5].toString()));
						}
						if(values[6]==null){
							qualitycheckmanagement.put("maintStation",values[6]);
						}else{
							qualitycheckmanagement.put("maintStation", bd.getName_Sql("Storageid", "storagename", "storageid", values[6].toString()));
						}
						if(values[7]==null){
							qualitycheckmanagement.put("maintPersonnel",values[7]);
						}else{
							qualitycheckmanagement.put("maintPersonnel", bd.getName_Sql("Loginuser", "username", "userid", values[7].toString()));
						}
						if(values[8]==null || values[8].equals("")){
							qualitycheckmanagement.put("checksPeople", values[8]);
						}else{
							qualitycheckmanagement.put("checksPeople",  bd.getName("Loginuser", "username", "userid", values[8].toString()));
						}
						qualitycheckmanagement.put("checksPeople", bd.getName("Loginuser", "username", "userid", values[8].toString()));
						qualitycheckmanagement.put("checksDateTime",values[9]);
						qualitycheckmanagement.put("totalPoints",values[10]);
						qualitycheckmanagement.put("scoreLevel", values[11]);
						qualitycheckmanagement.put("supervOpinion", values[12]);
						qualitycheckmanagement.put("partMinisters",userInfo.getUserName());
						qualitycheckmanagement.put("remDate", CommonUtil.getToday());
					}

					// 维保质量检查扣分项
					hql = "select mtc.mtId,mt.mtName,mtc.fraction2,mtc.fraction,mtc.appendix from MarkingItemsComply mtc,MarkingItems mt where mtc.mtId=mt.mtId and mtc.billno='"
							+ id + "'";
					list = hs.createQuery(hql).list();
					for (Object object : list) {
						Object[] values = (Object[]) object;
						Map m = new HashMap();
						m.put("mtId", values[0]);
						m.put("mtName", values[1]);
						m.put("fraction2", values[2]);
						m.put("fraction", values[3]);
						m.put("appendix", values[4]);

						markingItemsComplyList.add(m);
					}
					// 维保质量检查安全隐患项
					hql = "select tsrc.tsrId,tsr.tsrDetail,tsrc.appendix from TermSecurityRisksComply tsrc,TermSecurityRisks tsr where tsrc.tsrId=tsr.tsrId and tsrc.billno='"
							+ id + "'";
					list = hs.createQuery(hql).list();
					for (Object object : list) {
						Object[] values = (Object[]) object;
						Map m = new HashMap();
						m.put("tsrId", values[0]);
						m.put("tsrDetail", values[1]);
						m.put("appendix", values[2]);

						termSecurityRisksComplyList.add(m);
					}
					// 维保质量检查考核项
					hql = "select seic.seiid,sei.seiDetail,seic.appendix from ShouldExamineItemsComply seic,ShouldExamineItems sei where seic.seiid=sei.seiid and billno='"
							+ id + "'";
					list = hs.createQuery(hql).list();
					for (Object object : list) {
						Object[] values = (Object[]) object;
						Map m = new HashMap();
						m.put("seiid", values[0]);
						m.put("seiDetail", values[1]);
						m.put("appendix", values[2]);

						shouldExamineItemsComplyList.add(m);
					}

					if (qualitycheckmanagement == null) {
						errors.add(
								ActionErrors.GLOBAL_ERROR,
								new ActionError(
										"qualitycheckmanagement.display.recordnotfounterror"));
					}
				} catch (DataStoreException e) {
					e.printStackTrace();
				} catch (HibernateException e1) {
					e1.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} finally {
					try {
						hs.close();
					} catch (HibernateException hex) {
						log.error(hex.getMessage());
						DebugUtil
								.print(hex, "HibernateUtil Hibernate Session ");
					}
				}
			}
			request.setAttribute("markingItemsComplyList",
					markingItemsComplyList);
			request.setAttribute("termSecurityRisksComplyList",
					termSecurityRisksComplyList);
			request.setAttribute("shouldExamineItemsComplyList",
					shouldExamineItemsComplyList);
			request.setAttribute("qualityCheckManagementBean",
					qualitycheckmanagement);
			request.setAttribute("returnListMethod", returnMethod);
			forward = mapping.findForward("qualityCheckAuditAdd");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * 审核提交
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toAuditRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String partMinistersRem=(String)dform.get("partMinistersRem");

		String hql="";
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			hql="update QualityCheckManagement set partMinisters='"+userInfo.getUserID()+"',remDate='"+CommonUtil.getToday()+"',partMinistersRem='"+partMinistersRem+"',processStatus='3' where billno='"+(String)dform.get("billno")+"'";
			hs.createQuery(hql).executeUpdate();
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"qualitycheckmanagement.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (Exception e1) {
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

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"update.success"));
				forward = mapping.findForward("returnAuditList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				forward = mapping.findForward("returnAudit");
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
	 * 下载附件
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDownloadFileRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

//		FileOperatingUtil uf = new FileOperatingUtil();
//		uf.toDownLoadFiles(mapping, form, request, response);
		String fileOldName=request.getParameter("fileOldName");
		fileOldName=URLDecoder.decode(fileOldName,"utf-8");
		String filename=request.getParameter("filesname");
		filename=URLDecoder.decode(filename,"utf-8");
		String folder=request.getParameter("folder");
		if(folder==null || "".equals(folder)){
			folder="MTSComply.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;

		response.setContentType("application/x-msdownload");
		response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileOldName, "utf-8"));
		

		fis = new FileInputStream(folder+"MarkingScoreRegisterFileinfo"+"/"+filename);
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
	
	public ActionForward toDownloadFileRegistration(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

//		FileOperatingUtil uf = new FileOperatingUtil();
//		uf.toDownLoadFiles(mapping, form, request, response);
		String fileOldName=request.getParameter("fileOldName");
		fileOldName=URLDecoder.decode(fileOldName,"utf-8");
		String filename=request.getParameter("filesname");
		filename=URLDecoder.decode(filename,"utf-8");
		String folder=request.getParameter("folder");
		if(folder==null || "".equals(folder)){
			folder="MTSComply.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;

		response.setContentType("application/x-msdownload");
		response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileOldName, "utf-8"));

		fis = new FileInputStream(folder+"MarkingScoreRegisterFileinfo"+"/"+filename);
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
	
	public ActionForward toDownloadFileAudit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

//		FileOperatingUtil uf = new FileOperatingUtil();
//		uf.toDownLoadFiles(mapping, form, request, response);
		String fileOldName=request.getParameter("fileOldName");
		fileOldName=URLDecoder.decode(fileOldName,"utf-8");
		String filename=request.getParameter("filesname");
		filename=URLDecoder.decode(filename,"utf-8");
		String folder=request.getParameter("folder");
		if(folder==null || "".equals(folder)){
			folder="MTSComply.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;

		response.setContentType("application/x-msdownload");
		response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileOldName, "utf-8"));

		fis = new FileInputStream(folder+"MarkingScoreRegisterFileinfo"+"/"+filename);
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
	
	/**
	 * 获取负责人弹出窗口列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrincipalSelectList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		request.setAttribute("navigator.location", "");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form, request, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport", "");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "principalList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fPrincipalSel");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("principalId");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String principalName = tableForm.getProperty("principalName");
			String principalId = tableForm.getProperty("principalId");
			Session hs = null;
			try {
				hs = HibernateUtil.getSession();

				Criteria criteria = hs.createCriteria(Principal.class);
				criteria.add(Expression.eq("enabledFlag", "Y"));
				if (principalId != null && !principalId.equals("")) {
					criteria.add(Expression.like("principalId", "%"
							+ principalId.trim() + "%"));
				}
				if (principalName != null && !principalName.equals("")) {
					criteria.add(Expression.like("principalName", "%"
							+ principalName.trim() + "%"));
				}
				if (table.getIsAscending()) {
					criteria.addOrder(Order.asc(table.getSortColumn()));
				} else {
					criteria.addOrder(Order.desc(table.getSortColumn()));
				}
				table.setVolume(criteria.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				criteria.setFirstResult(table.getFrom()); // pagefirst
				criteria.setMaxResults(table.getLength());

				cache.check(table);

				List principalList = criteria.list();

				table.addAll(principalList);
				session.setAttribute("principalList", table);

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (HibernateException e1) {

				e1.printStackTrace();
			} finally {
				try {
					hs.close();
					// HibernateSessionFactory.closeSession();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			forward = mapping.findForward("principalSelect");
		}
		return forward;
	}
	
public void display(ActionForm form, HttpServletRequest request ,ActionErrors errors ,String flag){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;		
//		String id = request.getParameter("id");
		String id = (String) dform.get("id");
		if(id==null || "".equals(id)){
			id=(String)dform.get("billno");
		}
		
		String CSheight = PropertiesUtil.getProperty("CSheight");
		String CSwidth = PropertiesUtil.getProperty("CSwidth");
		String CIheight = PropertiesUtil.getProperty("CIheight");
		String CIwidth = PropertiesUtil.getProperty("CIwidth");
		Session hs = null;
		List markingItemsComplyList = new ArrayList(),deleList=new ArrayList();
		QualityCheckManagement manage=new QualityCheckManagement();
		List elevaorTypeList=bd.getPullDownList("ElevatorSalesInfo_type");
		if (id != null && !id.equals("")) {			
			try {
				hs = HibernateUtil.getSession();
				String hql="select a,b.comname,c.storagename from QualityCheckManagement a,Company b,Storageid c"+
						" where a.maintDivision=b.comid and a.maintStation=c.storageid and a.billno='"+id.trim()+"'";
				Query query = hs.createQuery(hql);
				List list = query.list();
				if (list != null && list.size() > 0) {
					String mstation="";
					String r5str="";
					for(Object object : list){
						Object[] objs=(Object[])object;
						manage=(QualityCheckManagement) objs[0];
						mstation=manage.getMaintStation();
						r5str=manage.getR5();//保养参与人员
						manage.setMaintDivision(objs[1].toString());
						manage.setMaintStation(objs[2].toString());
						manage.setMaintPersonnel(bd.getName("Loginuser", "username", "userid", manage.getMaintPersonnel()));
						manage.setChecksPeople(bd.getName("Loginuser", "username", "userid", manage.getChecksPeople()));
						manage.setPartMinisters(bd.getName("Loginuser", "username", "userid", manage.getPartMinisters()));
						manage.setSuperviseId(bd.getName("Loginuser", "username", "userid", manage.getSuperviseId()));
					}
					if("dispose".equals(flag)){
						manage.setChecksDateTime(CommonUtil.getNowTime());
					}
					
					//评分信息
					hql="from MarkingScoreRegister where billno='"+manage.getBillno()+"' and isDelete='N'";
					list=hs.createQuery(hql).list();
					for(Object object : list){
						MarkingScoreRegister msr=(MarkingScoreRegister)object;
						Map map=new HashMap();
						map.put("jnlno", msr.getJnlno());
						map.put("msId", msr.getMsId());
						map.put("msName", msr.getMsName());
						map.put("fraction", msr.getFraction());
						map.put("rem", msr.getRem());
						List detailList=hs.createQuery("from MarkingScoreRegisterDetail where jnlno='"+msr.getJnlno()+"'").list();
						map.put("detailList", detailList);
						List fileList=hs.createQuery("from MarkingScoreRegisterFileinfo where jnlno='"+msr.getJnlno()+"'").list();
						map.put("fileList", fileList);
						markingItemsComplyList.add(map);
					}
					hql="from MarkingScoreRegister where billno='"+manage.getBillno()+"' and isDelete='Y'";
					list=hs.createQuery(hql).list();
					if(list!=null && list.size()>0){
						for(Object object : list){
							MarkingScoreRegister msr=(MarkingScoreRegister)object;
							Map map=new HashMap();
							map.put("jnlno", msr.getJnlno());
							map.put("msId", msr.getMsId());
							map.put("msName", msr.getMsName());
							map.put("fraction", msr.getFraction());
							map.put("rem", msr.getRem());
							List detailList=hs.createQuery("from MarkingScoreRegisterDetail where jnlno='"+msr.getJnlno()+"'").list();
							map.put("detailList", detailList);
							List fileList=hs.createQuery("from MarkingScoreRegisterFileinfo where jnlno='"+msr.getJnlno()+"'").list();
							map.put("fileList", fileList);
							map.put("isDelet", msr.getIsDelete());
							map.put("deleteRem", msr.getDeleteRem());
							deleList.add(map);
						}
					}
					
					//审批流程信息
					List processApproveList = hs.createQuery("from QualityCheckProcess where billno = '"+(String) dform.get("id")+ "' order by itemId").list();
					for (Object object : processApproveList) {
						QualityCheckProcess process = (QualityCheckProcess) object;
						process.setUserId(bd.getName(hs, "Loginuser", "username", "userid", process.getUserId()));
					}
					
					if(manage.getProcessStatus().equals("2")||manage.getProcessStatus().equals("3")){
						request.setAttribute("isStatus", "Y");
					}else{
						request.setAttribute("isStatus", "N");
					}
					

					String r5name="";
					if(r5str!=null && !r5str.trim().equals("")){
						//维保站人员 维保站长A49，维保工A50,维保经理 A03，维修技术员A53
						String whql="select l.userid,l.username,isnull(b.userid,'N') from loginuser l "
								+ "left join (select userid from loginuser "
								+ "where userid in('"+r5str.replaceAll(",", "','")+"')) b on l.userid=b.userid "
								+ "where l.storageid like '"+mstation+"%' "
								+ "and l.enabledflag='Y' and l.RoleID in('A50','A49','A03','A53') "
								+ "order by l.RoleID";
						List msuserlist=hs.createSQLQuery(whql).list();
						List userlist=new ArrayList();
						for(int l=0;l<msuserlist.size();l++){
							Object[] obj=(Object[])msuserlist.get(l);
							HashMap map=new HashMap();
							map.put("userid", obj[0]+"");
							map.put("username", obj[1]+"");
							map.put("isok", obj[2]+"");
							if(!obj[2].toString().equals("N")){
								r5name+=obj[1]+",";
							}
							userlist.add(map);
						}
						request.setAttribute("msUserList", userlist);
					}else{
						//维保站人员 维保站长A49，维保工A50,维保经理 A03，维修技术员A53
						String whql="select l.userid,l.username from loginuser l "
								+ "where l.storageid like '"+mstation+"%' "
								+ "and l.enabledflag='Y' and l.RoleID in('A50','A49','A03','A53') "
								+ "order by l.RoleID";
						List msuserlist=hs.createSQLQuery(whql).list();
						List userlist=new ArrayList();
						for(int l=0;l<msuserlist.size();l++){
							Object[] obj=(Object[])msuserlist.get(l);
							HashMap map=new HashMap();
							map.put("userid", obj[0]+"");
							map.put("username", obj[1]+"");
							map.put("isok", "N");
							userlist.add(map);
						}
						request.setAttribute("msUserList", userlist);
					}
					manage.setR5(r5name);
					
					request.setAttribute("processApproveList", processApproveList);
					request.setAttribute("qualityCheckManagementBean", manage);
					request.setAttribute("elevaorTypeName", bd.getOptionName(manage.getElevatorType(), elevaorTypeList));
					request.setAttribute("markingItemsComplyList", markingItemsComplyList);
					request.setAttribute("deleList", deleList);
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
				request.setAttribute("CSheight",CSheight);
				request.setAttribute("CSwidth", CSwidth);
				request.setAttribute("CIheight", CIheight);
				request.setAttribute("CIwidth", CIwidth);
				
				
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
 * 打印通知单
 * @param mapping
 * @param form
 * @param request
 * @param response
 * @return
 * @throws IOException
 * @throws ServletException
 */

/*@SuppressWarnings("unchecked")*/
public ActionForward toPreparePrintRecord(ActionMapping mapping,ActionForm form,
					HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		    	ActionErrors errors = new ActionErrors();
		    	HttpSession session = request.getSession();
				ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
				Session hs = null;
				Connection conn = null;
				DynaActionForm dform = (DynaActionForm) form;		
				String id = request.getParameter("id");
				List etcpList=new ArrayList();
				List hecirList=new ArrayList();
				if (id != null && !id.equals("")) {			
					try {
						hs = HibernateUtil.getSession();
						DataOperation op = new DataOperation();
						conn = (Connection) hs.connection();
						op.setCon(conn);
						Query query = hs.createQuery("from QualityCheckManagement where billno = '"+id.trim()+"'");
						List list = query.list();
						if (list != null && list.size() > 0) {

							// 主信息
							QualityCheckManagement QM = (QualityCheckManagement) list.get(0);	
							String CompanyID=bd.getName("MaintContractMaster", "companyId", "maintContractNo",QM.getMaintContractNo());
							QM.setMaintPersonnel(bd.getName(hs, "Loginuser","username", "userid",QM.getMaintPersonnel()));// 报修人
							QM.setR2(bd.getName(hs, "ElevatorCoordinateLocation","rem", "elevatorNo",QM.getElevatorNo()));
							QM.setR3(bd.getName(hs, "Customer", "companyName", "companyId",CompanyID));
							QM.setR4(bd.getName(hs, "Loginuser","username", "userid",QM.getChecksPeople()));//督查人员

							String hecirListsql="select * from MarkingScoreRegister where billno = '"+id.trim()+"' and isDelete='N'";
							hecirList =op.queryToList(hecirListsql);
							
							//对barCodeList、noticeList操作
							BarCodePrint dy = new BarCodePrint();
							List barCodeList = new ArrayList();
							barCodeList.add(QM);
							barCodeList.add(hecirList);
							dy.toPrintTwoRecord6(request,response, barCodeList,id);
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


public ActionForward toDownloadFileRecord1(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)	throws IOException, ServletException {
	
	Session hs = null;

	String filepath = request.getParameter("customerSignature");//流水号
	String filepath1 = request.getParameter("customerImage");
	String localPath="";
	String oldname="";
	String folder = request.getParameter("folder");		//文件夹
	if(null == folder || "".equals(folder)){
		folder ="QualityCheckManagement.file.upload.folder";
	}
	folder = PropertiesUtil.getProperty(folder);
	
	try {
		hs = HibernateUtil.getSession();
			String root=folder;//上传目录
			if(filepath != null && !filepath.equals("")){
				localPath = root+filepath;
			}else{
				localPath = root+filepath1;
			}
			
		
			/*localPath = filepath+newnamefile;*/
			
		/*}*/
	
	BufferedInputStream bis = null;
	BufferedOutputStream bos = null;
	OutputStream fos = null;
	InputStream fis = null;

	response.setContentType("application/x-msdownload");
	response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(oldname, "utf-8"));
	//OutputStream os = new FileOutputStream(new File(localPath));
	
	fis = new FileInputStream(localPath);
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
	
	
	} catch (IOException e) {
		e.printStackTrace();
	} catch (DataStoreException e) {
		e.printStackTrace();
	} catch (HibernateException e) {
		e.printStackTrace();
	} catch (Exception e) {
		e.printStackTrace();
	}finally {
		try {				
			hs.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
	return null;
}
/**
 * ajax 级联 获取站下面的维保工
 * @param mapping
 * @param form
 * @param request
 * @param response
 * @return
 * @throws IOException
 * @throws ServletException
 */
public void toUserIDList(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws IOException{
	Session hs=null;
	String storageid=request.getParameter("storageid");
	response.setHeader("Content-Type","text/html; charset=GBK");
	
	StringBuffer sb = new StringBuffer();
	sb.append("<?xml version='1.0' encoding='GBK'?>");
	sb.append("<root>");
	try {
		hs=HibernateUtil.getSession();
		if(storageid!=null && !"".equals(storageid)){
			//只出 维保站长A49，维保工A50,维保经理 A03，维修技术员A53
			String hql="select a from Loginuser a where a.storageid like '"+storageid+"%' " +
					"and a.enabledflag='Y' and a.roleid in('A49','A50') order by a.roleid,a.userid";
			List list=hs.createQuery(hql).list();
			if(list!=null && list.size()>0){
				sb.append("<rows>");
				for(int i=0;i<list.size();i++){
					Loginuser sid=(Loginuser)list.get(i);
					sb.append("<cols name='"+sid.getUsername()+"' value='"+sid.getUserid()+"' phone='"+sid.getPhone()+"'>").append("</cols>");
				}
				sb.append("</rows>");
							
				
			  }
		 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	finally{
		hs.close();
	}
	sb.append("</root>");
	
	response.setCharacterEncoding("gbk"); 
	response.setContentType("text/xml;charset=gbk");
	response.getWriter().write(sb.toString());
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
public void toStorageIDList(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws IOException{
	Session hs=null;
	String comid=request.getParameter("comid");
	response.setHeader("Content-Type","text/html; charset=GBK");
	
	StringBuffer sb = new StringBuffer();
	sb.append("<?xml version='1.0' encoding='GBK'?>");
	sb.append("<root>");
	try {
		hs=HibernateUtil.getSession();
		if(comid!=null && !"".equals(comid)){
			String hql="select a from Storageid a where a.comid='"+comid+"' " +
					"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
			List list=hs.createQuery(hql).list();
			if(list!=null && list.size()>0){
				sb.append("<rows>");
				for(int i=0;i<list.size();i++){
				Storageid sid=(Storageid)list.get(i);
				sb.append("<cols name='"+sid.getStoragename()+"' value='"+sid.getStorageid()+"'>").append("</cols>");
				}
				sb.append("</rows>");
							
				
			  }
		 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	finally{
		hs.close();
	}
	sb.append("</root>");
	
	response.setCharacterEncoding("gbk"); 
	response.setContentType("text/xml;charset=gbk");
	response.getWriter().write(sb.toString());
}

}
