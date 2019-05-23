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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.file.HandleFile;
import com.gzunicorn.file.HandleFileImpA;
import com.gzunicorn.hibernate.basedata.Fileinfo;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferDebugFileinfo;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferFileTypes;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferFileinfo;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferMaster;
import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;



public class ContractTransferAssignAction extends DispatchAction {

	Log log = LogFactory.getLog(ContractTransferAssignAction.class);
	BaseDataImpl bd=new BaseDataImpl();

	// --------------------------------------------------------- Instance
	// Variables

	// --------------------------------------------------------- Methods

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
		//String flag = request.getParameter("openflag")==null?"":request.getParameter("openflag");
		//if(!"Sale".equals(flag)){
		/************开始用户权限过滤************/
		SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "ContractTransferAssign",null);
		/************结束用户权限过滤************/
		//}
		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		//System.out.println(name);
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			ActionForward forward = super.execute(mapping, form, request,
					response);
			//System.out.println(forward.getName());
			return forward;
		}

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
	 * Method toPrepareAddRecord execute,prepare data for add page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toPrepareAddRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response){

		request.setAttribute("navigator.location", "合同交接资料派工>> 新建");

		DynaActionForm dform = (DynaActionForm) form;
		List filetypelist = bd.getPullDownList("ContractFileTypes_FileType");
		
		request.setAttribute("filetypelist", filetypelist);
		request.setAttribute("returnListMethod", "toSearchRecord");
		return mapping.findForward("contracttransferassignAdd");
	}

	/**
	 * Method toAddRecord execute,Add data to database and return list page or
	 * add page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toAddRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		System.out.println(dform);
		Transaction tx = null;
		String time = CommonUtil.getNowTime();
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String maintcontractno[] = (String[])dform.get("maintcontractno");
		String salescontractno[] = (String[])dform.get("salescontractno");
		String elevatorno[] = (String[])dform.get("elevatorno");
		String companyid[] = (String[])dform.get("companyid");
		String maintdivision[] = (String[])dform.get("maintdivision");
		String maintstation[] = (String[])dform.get("maintstation");
		String contractsdate[] = (String[])dform.get("contractsdate");
		String contractedate[] = (String[])dform.get("contractedate");
		String filetype[] = (String[])dform.get("filetype");
		String isreturn = (String)dform.get("isreturn");
		String transfercomplete="";
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			String todayDate = CommonUtil.getToday();
			String year = todayDate.substring(2,4);
			
			List list = savePicter(form, request, response, "ContractTransferDebugFileinfo.file.upload.folder", "","ContractTransferDebugFileinfo");
			System.out.println(list);
			
			if(maintcontractno!=null){
				for(int j=0;j<filetype.length;j++){
					if("A".equals(filetype[j])||"B".equals(filetype[j])){
						transfercomplete="交接未完成";
					}
				}
				
				for(int i=0;i<maintcontractno.length;i++){
					ContractTransferMaster cm = new ContractTransferMaster();
					String  billno=CommonUtil.getBillno(year,"ContractTransferMaster", 1)[0];
					cm.setBillNo(billno);
					cm.setCompanyId(companyid[i]);
					cm.setMaintContractNo(maintcontractno[i]);
					cm.setSalesContractNo(salescontractno[i]);
					cm.setElevatorNo(elevatorno[i]);
					cm.setMaintDivision(maintdivision[i]);
					cm.setMaintStation(maintstation[i]);
					cm.setContractSdate(contractsdate[i]);
					cm.setContractEdate(contractedate[i]);
					if("Y".equals(isreturn)){
						cm.setSubmitType("Y");
					}else{
						cm.setSubmitType("N");
					}
					cm.setOperId(userInfo.getUserID());
					cm.setOperDate(time);
					cm.setTransfeSubmitType("N");
					cm.setAuditStatus("N");
					cm.setTransferComplete(transfercomplete);
					cm.setWorkisdisplay("N");
					hs.save(cm);
					hs.flush();
					
					for(int k=0;k<filetype.length;k++){
						ContractTransferFileTypes cf = new ContractTransferFileTypes();
						String  jnlno=CommonUtil.getBillno(year,"ContractTransferFileTypes", 1)[0];
						cf.setJnlno(jnlno);
						cf.setBillNo(billno);
						cf.setFileType(filetype[k]);
						hs.save(cf);
						hs.flush();
					}
					
					
					boolean b=false;
					if(list!=null || list.size()>0){
						b=savePicterTodb(request, list, billno,hs);
					}
					if (!b) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
						break;
					}
				}
			}
			if (errors.isEmpty()) {
				tx.commit();
			}else{
				tx.rollback();
			}
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			e2.printStackTrace();
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate ContractTransferMaster Insert error!");
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"insert.success"));
				}
				forward = mapping.findForward("returnList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"insert.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				forward = toPrepareAddRecord(mapping, dform, request, response);
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
	 * Method toUpdateRecord execute,Update data to database and return list
	 * page or modifiy page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward toUpdateRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		String billno = (String) request.getParameter("billno");
		String submitflag = (String) request.getParameter("submitflag");
		String isreturn = (String)dform.get("isreturn");
		if(isreturn==null||"".equals(isreturn)){
			isreturn=(String) request.getParameter("isreturn");
		}
		List list = savePicter(form, request, response, "ContractTransferDebugFileinfo.file.upload.folder", "","ContractTransferDebugFileinfo");
		System.out.println(list);
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			String todayDate = CommonUtil.getToday();
			String year = todayDate.substring(2,4);
			String time = CommonUtil.getNowTime();
			if(billno!=null&&!"".equals(billno)){
				ContractTransferMaster cm = (ContractTransferMaster) hs.get(ContractTransferMaster.class, billno);
				cm.setOperId(userInfo.getUserID());
				cm.setOperDate(time);
				//cm.setTransfeSubmitType("N");
				cm.setTransfeId(null);
				cm.setTransfeDate(null);
				cm.setTransferRem(null);
				if(isreturn!=null&&"Y".equals(isreturn)){
					cm.setSubmitType("Y");
				}else{
					cm.setSubmitType("N");
				}
				
				if(submitflag==null||!"Y".equals(submitflag)){
					
					String delsql = "delete from ContractTransferFileTypes where billno='" + billno + "'";
					
					hs.createQuery(delsql).executeUpdate();
					
					String transfercomplete="";
					String filetype[] = (String[])dform.get("filetype");
					if(filetype!=null){
						for(int j=0;j<filetype.length;j++){
							if("A".equals(filetype[j])||"B".equals(filetype[j])){
								transfercomplete="交接未完成";
							}
						}
					}
					cm.setTransferComplete(transfercomplete);
					
					for(int k=0;k<filetype.length;k++){
						ContractTransferFileTypes cf = new ContractTransferFileTypes();
						String  jnlno=CommonUtil.getBillno(year,"ContractTransferFileTypes", 1)[0];
						cf.setJnlno(jnlno);
						cf.setBillNo(billno);
						cf.setFileType(filetype[k]);
						hs.save(cf);
					}
				}
				hs.save(cm);
				
				boolean b=false;
				if(list!=null || list.size()>0){
					b=savePicterTodb(request, list, billno,hs);
				}
				if (!b) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
				}
			}
			if (errors.isEmpty()) {
				tx.commit();
			}else{
				tx.rollback();
			}
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.fail"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			e2.printStackTrace();
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate ContractTransferMaster Upadte error!");
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"update.success"));
				}
				forward = mapping.findForward("returnList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				dform.set("billno", new String[]{billno});
				forward = toPrepareUpdateRecord(mapping, dform, request, response);
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
	 * Method toPrepareUpdateRecord execute,prepare data for update page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		// set navigation;
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		request.setAttribute("navigator.location", "合同交接资料派工>> 修改");
		ActionForward forward = null;
		String id =request.getParameter("id");
		if(id==null||"".equals(id)){
			id=((String[]) dform.get("billno"))[0];
		}
		Session hs = null;
		Connection conn=null;

		if (id != null) {
				try {
					hs = HibernateUtil.getSession();
					DataOperation op = new DataOperation();
					conn = (Connection) hs.connection();
					op.setCon(conn);
					
					String sql = "select cm.billNo,cu.companyName,cm.maintContractNo,cm.salesContractNo,cm.elevatorNo,c.comfullname,s.storagename,cm.contractSdate,"
					+"cm.contractEdate,cm.submitType,l.username as opname,cm.OperDate,u.username as upname,cm.TransfeDate,cm.TransferRem " 
					+"from ContractTransferMaster cm "
					+"join Customer cu "
					+"on cm.companyId=cu.companyId and cm.billno='"+id+"' "
					+"join Company c "
					+"on cm.maintDivision=c.comid "
					+"join Storageid s "
					+"on cm.maintStation=s.storageid "
					+"left join LoginUser l "
					+"on cm.OperId=l.userid "
					+"left join LoginUser u "
					+"on cm.TransfeId=u.userid";
					
					System.out.println(sql);
					List cmlist = op.queryToList(sql);
					
					String sql1 = "select * from ContractTransferFileTypes where billno='"+id+"' ";
					System.out.println(sql1);
					List cflist = op.queryToList(sql1);
					
					List filetypelist = bd.getPullDownList("ContractFileTypes_FileType");
					
					String sql2 = "select * from ContractTransferDebugFileinfo where billno='"+id+"' ";
					System.out.println(sql2);
					List fileList = op.queryToList(sql2);
					
					request.setAttribute("cmlist", cmlist);
					request.setAttribute("cflist", cflist);
					request.setAttribute("fileList", fileList);
					request.setAttribute("filetypelist", filetypelist);
					request.setAttribute("returnListMethod", "toSearchRecord");
					
				} catch (Exception e) {
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
		forward = mapping.findForward("contracttransferassignUpdate");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		return forward;
	}

	/**
	 * Method toDeleteRecord execute, Delete record where facid = id
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDeleteRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			{

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		String id =request.getParameter("id");
		if(id==null||"".equals(id)){
			id=((String[]) dform.get("billno"))[0];
		}
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

				//删除 合同交接资料附件表
	            String delsql="delete ContractTransferFileinfo h "
	            		+ "where h.jnlno in(select jnlno from ContractTransferFileTypes h where h.billNo='"+id+"')";
	            hs.createQuery(delsql).executeUpdate();
	            //删除 合同交接资料反馈附件表
	            String delsql2="delete ContractTransferFeedbackFileinfo h "
	            		+ "where h.jnlno in(select jnlno from ContractTransferFeedback h where h.billNo='"+id+"')";
	            hs.createQuery(delsql2).executeUpdate();
	            //删除 合同交接资料反馈表
	            String delsql3="delete ContractTransferFeedback h where h.billNo='"+id+"'";
	            hs.createQuery(delsql3).executeUpdate();
			
	            //合同交接资料附件类型表
				String delsqld = "delete from ContractTransferFileTypes where billno='"+ id + "'";
				hs.createQuery(delsqld).executeUpdate();
				//合同交接资料管理
				String delsqlt = "delete from ContractTransferMaster where billno='"+ id + "'";
				hs.createQuery(delsqlt).executeUpdate();
			
			tx.commit();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.succeed"));
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate ContractTransferMaster Delete error!");
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

		request.setAttribute("navigator.location", "合同交接资料派工>> 查询列表");
		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		request.setAttribute("showroleid", userInfo.getRoleID());

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {

				response = toExcelRecord(form, request, response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "contractTransferAssignList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fContractTransferAssign");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("BillNo");
			table.setIsAscending(true);
			cache.updateTable(table);
			
			int location = 0;//当前页码
			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			}else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			
			cache.saveForm(tableForm);
		

			
			String billno = tableForm.getProperty("billno");
			if(billno==null||"null".equals(billno)){
				billno="";
			}
			String maintcontractno = tableForm.getProperty("maintcontractno");
			if(maintcontractno==null||"null".equals(maintcontractno)){
				maintcontractno="";
			}
			String salescontractno = tableForm.getProperty("salescontractno");
			if(salescontractno==null||"null".equals(salescontractno)){
				salescontractno="";
			}
			String companyname = tableForm.getProperty("companyid");
			if(companyname==null||"null".equals(companyname)){
				companyname="";
			}
			String maintdivision = tableForm.getProperty("maintdivision");
			if(maintdivision==null||"null".equals(maintdivision)){
				maintdivision="";
			}
			String maintstation = tableForm.getProperty("maintstation");
			if(maintstation==null||"null".equals(maintstation)){
				maintstation="";
			}
			String elevatorno = tableForm.getProperty("elevatorno");
			if(elevatorno==null||"null".equals(elevatorno)){
				elevatorno="";
			}
			String submittype = tableForm.getProperty("submittype");
			if(submittype==null||"null".equals(submittype)){
				submittype="";
			}
			
			//第一次进入页面时根据登陆人初始化所属维保分部
			List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
			if(maintdivision == null || maintdivision.equals("")){
				Map map = (Map)maintDivisionList.get(0);
				maintdivision = (String)map.get("grcid");
			}
			
			
			Session hs = null;
			Connection conn = null;
			List li = new ArrayList();

			try {

				hs = HibernateUtil.getSession();
				Query query = null;
				
				String sql = "select cm.billNo,cu.companyName,cm.maintContractNo,cm.salesContractNo,cm.elevatorNo,c.comfullname,s.storagename,cm.contractSdate,cm.contractEdate,cm.submitType " 
				+"from ContractTransferMaster cm "
				+"join Customer cu "
				+"on cm.companyId=cu.companyId "
				+"join Company c "
				+"on cm.maintDivision=c.comid "
				+"join Storageid s "
				+"on cm.maintStation=s.storageid";
				
				if (billno != null && !billno.equals("")) {
					sql += " and cm.billNo like '%"+billno.trim()+"%'";
				}
				if (maintcontractno != null && !maintcontractno.equals("")) {
					sql += " and cm.maintcontractno like '%"+maintcontractno.trim()+"%'";
				}				
				if (salescontractno != null && !salescontractno.equals("")) {
					sql += " and cm.salescontractno like '%"+salescontractno.trim()+"%'";
				}				
				if (elevatorno != null && !elevatorno.equals("")) {
					sql += " and cm.elevatorno like '%"+elevatorno.trim()+"%'";
				}				
				if (companyname != null && !companyname.equals("")) {
					sql += " and (cu.companyId like '%"+companyname.trim()+"%' or cu.companyName like '%"+companyname.trim()+"%' )";
				}				
				if (maintdivision != null && !maintdivision.equals("") && !maintdivision.equals("%")) {
					sql += " and cm.maintdivision = '"+maintdivision.trim()+"'";
				}				
				if (maintstation != null && !maintstation.equals("") && !maintstation.equals("%")) {
					sql += " and cm.maintstation = '"+maintstation.trim()+"'";
				}				
				if (submittype != null && !submittype.equals("")) {
					sql += " and cm.submittype = '"+submittype.trim()+"'";
				}				
				if (table.getIsAscending()) {
					if("CompanyName".equals(table.getSortColumn())){
						
						sql += " order by cu."+ table.getSortColumn() +" desc";
					}else{
						
						sql += " order by cm."+ table.getSortColumn() +" desc";
					}
				} else {
					if("CompanyName".equals(table.getSortColumn())){
						
						sql += " order by cu."+ table.getSortColumn() +" asc";
					}else{
						
						sql += " order by cm."+ table.getSortColumn() +" asc";
					}
				}
				System.out.println(sql);
				query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List contractTransferAssignList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map map=new HashMap();
					map.put("billno", objs[0]);
					map.put("companyname", objs[1]);
					map.put("maintcontractno", objs[2]);
					map.put("salescontractno", objs[3]);
					map.put("elevatorno", objs[4]);
					map.put("maintdivision", objs[5]);
					map.put("maintstation", objs[6]);
					map.put("contractsdate", objs[7]);
					map.put("contractedate", objs[8]);
					if("Y".equals(objs[9])){
						
						map.put("submittype", "已提交");
					}else if("N".equals(objs[9])){
						
						map.put("submittype", "未提交");
					}else if("R".equals(objs[9])){
						
						map.put("submittype", "驳回");
					}
					contractTransferAssignList.add(map);
				}

				table.addAll(contractTransferAssignList);
				session.setAttribute("contractTransferAssignList", table);
				
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);
				

				List mainStationList=new ArrayList();
					String hql="select a from Storageid a where a.comid like '"+maintdivision+"' " +
							"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
					mainStationList=hs.createQuery(hql).list();
				  
					 Storageid storid=new Storageid();
					 storid.setStorageid("%");
					 storid.setStoragename("全部");
					 mainStationList.add(0,storid);
				request.setAttribute("mainStationList", mainStationList);

				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					hs.close();
				} catch (Exception hex) {
					log.error(hex.getMessage());
					hex.printStackTrace();
					//DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
          
			forward = mapping.findForward("contracttransferassignList");
			
		}
		return forward;
	}

	/**
	 * Method toDisplayRecord execute,prepare data for update page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		request.setAttribute("navigator.location", "合同交接资料派工>> 查看");
		ActionForward forward = null;
		String id =request.getParameter("id");
		if(id==null||"".equals(id)){
			id=((String[]) dform.get("billno"))[0];
		}
		Session hs = null;
		Connection conn=null;

		if (id != null) {
				try {
					hs = HibernateUtil.getSession();
					DataOperation op = new DataOperation();
					conn = (Connection) hs.connection();
					op.setCon(conn);
					
					String sql = "select cm.billNo,cu.companyName,cm.maintContractNo,cm.salesContractNo,cm.elevatorNo,c.comfullname,s.storagename,cm.contractSdate,"
							+"cm.contractEdate,cm.submitType,l.username as opname,cm.OperDate,u.username as upname,cm.TransfeDate,cm.TransferRem " 
							+"from ContractTransferMaster cm "
							+"join Customer cu "
							+"on cm.companyId=cu.companyId and cm.billno='"+id+"' "
							+"join Company c "
							+"on cm.maintDivision=c.comid "
							+"join Storageid s "
							+"on cm.maintStation=s.storageid "
							+"left join LoginUser l "
							+"on cm.OperId=l.userid "
							+"left join LoginUser u "
							+"on cm.TransfeId=u.userid";
					
					System.out.println(sql);
					List cmlist = op.queryToList(sql);
					
					String sql1 = "select p.pullname from ContractTransferFileTypes cf,pulldown p where billno='"+id+"' and p.typeflag='ContractFileTypes_FileType' and p.enabledflag='Y' and cf.FileType=p.pullid"
							+ " order by pullid";
					System.out.println(sql1);
					List cflist = op.queryToList(sql1);
					
					String sql2 = "select * from ContractTransferDebugFileinfo where billno='"+id+"' ";
					System.out.println(sql2);
					List fileList = op.queryToList(sql2);
					
					
					request.setAttribute("cmlist", cmlist);
					request.setAttribute("cflist", cflist);
					request.setAttribute("fileList", fileList);
					request.setAttribute("returnListMethod", "toSearchRecord");
					
				} catch (Exception e) {
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
			forward = mapping.findForward("contracttransferassignView");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
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
	 * @throws IOException
	 */

	public HttpServletResponse toExcelRecord(ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{

		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();
		
		String billno = tableForm.getProperty("billno");
		if(billno==null||"null".equals(billno)){
			billno="";
		}
		String maintcontractno = tableForm.getProperty("maintcontractno");
		if(maintcontractno==null||"null".equals(maintcontractno)){
			maintcontractno="";
		}
		String salescontractno = tableForm.getProperty("salescontractno");
		if(salescontractno==null||"null".equals(salescontractno)){
			salescontractno="";
		}
		String companyname = tableForm.getProperty("companyid");
		if(companyname==null||"null".equals(companyname)){
			companyname="";
		}
		String maintdivision = tableForm.getProperty("maintdivision");
		if(maintdivision==null||"null".equals(maintdivision)){
			maintdivision="";
		}
		String maintstation = tableForm.getProperty("maintstation");
		if(maintstation==null||"null".equals(maintstation)){
			maintstation="";
		}
		String elevatorno = tableForm.getProperty("elevatorno");
		if(elevatorno==null||"null".equals(elevatorno)){
			elevatorno="";
		}
		String submittype = tableForm.getProperty("submittype");
		if(submittype==null||"null".equals(submittype)){
			submittype="";
		}
		

		try {
			hs = HibernateUtil.getSession();
			Query query = null;
			
			String sql = "select cm.billNo,cu.companyName,cm.maintContractNo,cm.salesContractNo,cm.elevatorNo,c.comfullname,s.storagename,cm.contractSdate,cm.contractEdate,cm.submitType " 
			+"from ContractTransferMaster cm "
			+"join Customer cu "
			+"on cm.companyId=cu.companyId "
			+"join Company c "
			+"on cm.maintDivision=c.comid "
			+"join Storageid s "
			+"on cm.maintStation=s.storageid";
			
			if (billno != null && !billno.equals("")) {
				sql += " and cm.billNo like '%"+billno.trim()+"%'";
			}
			if (maintcontractno != null && !maintcontractno.equals("")) {
				sql += " and cm.maintcontractno like '%"+maintcontractno.trim()+"%'";
			}				
			if (salescontractno != null && !salescontractno.equals("")) {
				sql += " and cm.salescontractno like '%"+salescontractno.trim()+"%'";
			}				
			if (elevatorno != null && !elevatorno.equals("")) {
				sql += " and cm.elevatorno like '%"+elevatorno.trim()+"%'";
			}				
			if (companyname != null && !companyname.equals("")) {
				sql += " and (cu.companyId like '%"+companyname.trim()+"%' or cu.companyName like '%"+companyname.trim()+"%' )";
			}				
			if (maintdivision != null && !maintdivision.equals("") && !maintdivision.equals("%")) {
				sql += " and cm.maintdivision = '"+maintdivision.trim()+"'";
			}				
			if (maintstation != null && !maintstation.equals("") && !maintstation.equals("%")) {
				sql += " and cm.maintstation = '"+maintstation.trim()+"'";
			}				
			if (submittype != null && !submittype.equals("")) {
				sql += " and cm.submittype = '"+submittype.trim()+"'";
			}				
			sql += " order by cm.billno desc";
			System.out.println(sql);
			query = hs.createSQLQuery(sql);

			List List = query.list();

			XSSFSheet sheet = wb.createSheet("合同交接资料派工情况");

			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (List != null && !List.isEmpty()) {
				int l = List.size();
				XSSFRow row0 = sheet.createRow((short) 0);
				XSSFCell cell0 = null;
				
				String[] titlename={"流水号","甲方单位名称","维保合同号","销售合同号","电梯编号","所属维保分部","所属维保站","合同开始日期",
						"合同结束日期","提交标志"};
				for(int j=0;j<titlename.length;j++){
					cell0 = row0.createCell((short)j);
					cell0.setCellValue(titlename[j]);
				}
				
				for (int i = 0; i < l; i++) {
					Object[] obj=(Object[])List.get(i);
					
					
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);

					// 创建Excel列
					XSSFCell cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj[0].toString());
					
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj[1].toString());
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj[2].toString());
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj[3].toString());
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj[4].toString());

					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj[5].toString());

					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj[6].toString());
					
					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj[7].toString());
					
					cell = row.createCell((short)8);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj[8].toString());

					cell = row.createCell((short)9);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					
					if("Y".equals(obj[9].toString())){
						
						cell.setCellValue("已提交");
					}else if("N".equals(obj[9].toString())){
						
						cell.setCellValue("未提交");
					}else if("R".equals(obj[9].toString())){
						
						cell.setCellValue("驳回");
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("合同交接资料派工情况", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		return response;
	}
//	
//	public void ajaxShowMass(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws IOException {
//		response.setContentType("text/html;charset=utf-8");
//		String name = request.getParameter("name");
//		String oldid = request.getParameter("id")==null?"":request.getParameter("id");
//		Session hs = null;
//		Connection conn = null;
//		StringBuffer sb = new StringBuffer("{");
//		
//		try {
//			hs = HibernateUtil.getSession();
//			DataOperation op = new DataOperation();
//			conn = (Connection) hs.connection();
//			op.setCon(conn);
//			
//			String sql = "select u.ShopID,t.typename from unitinfomaster u,Unittype t where u.typeid=t.typeid and u.ShopFullName='"+name+"'";
//			List li = op.queryToList(sql);
//			if(li!=null&&li.size()>0){
//				HashMap hm= (HashMap) li.get(0);
//				sb.append("\"shopid\":").append("\"").append(hm.get("shopid")).append("\"").append(",");
//				sb.append("\"typename\":").append("\"").append(hm.get("typename")).append("\"").append(",");
//					
//			}
//			
//			if(oldid!=null&&!"".equals(oldid)){
//				String sql3 = "exec sp_EngUnitInfoAjax '"+oldid+"'";
//				List li3 = op.queryToList(sql3);
//				System.out.println(sql3);
//				System.out.println(li3);
//				if(li3!=null&&li3.size()>0){
//					HashMap hm3= (HashMap) li3.get(0);
//					sb.append("\"firstcooperatetime\":").append("\"").append(hm3.get("firstcooperatetime")).append("\"").append(",");
//					sb.append("\"lastcooperatetime\":").append("\"").append(hm3.get("lastcooperatetime")).append("\"").append(",");
//					sb.append("\"engunityear\":").append("\"").append(hm3.get("engunityear")).append("\"").append(",");
//				}
//			}
//			
//			String sql2 = "select * from EngUnitInfo  where EngUnitName='"+name+"'";
//			List li2 = op.queryToList(sql2);
//			if(li2!=null&&li2.size()>0){
//				String id= (String)((HashMap)li2.get(0)).get("engunitid");
//				if(!oldid.equals(id)){
//					
//					sb.append("\"issame\":").append("\"").append("Y").append("\"").append("}");
//					
//				}else{
//					
//					sb.append("\"issame\":").append("\"").append("N").append("\"").append("}");
//				}
//				
//			}else{
//				
//				sb.append("\"issame\":").append("\"").append("N").append("\"").append("}");
//			}
//		
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		response.getWriter().print(sb+"");
//	}
//
	public ActionForward addElevator(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ActionErrors errors = new ActionErrors();
		request.setAttribute("navigator.location", "合同交接资料派工>> 选择电梯");
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		ActionForward forward = null;
		Session hs = null;
			try {
				
				HTMLTableCache cache = new HTMLTableCache(session, "contractTransferAssignAjax");

				DefaultHTMLTable table = new DefaultHTMLTable();
				table.setMapping("fContractTransferAjax");
				table.setLength(SysConfig.HTML_TABLE_LENGTH);
				cache.updateTable(table);
				table.setSortColumn("BillNo");
				table.setIsAscending(true);
				cache.updateTable(table);
				
				int location = 0;//当前页码
				if (action.equals(ServeTableForm.NAVIGATE)
						|| action.equals(ServeTableForm.SORT)) {
					cache.loadForm(tableForm);
					//location = table.getFrom();
					//System.out.println("location=" + location);
				} else {
					table.setFrom(0);
					//location = 0;
				}
				
				cache.saveForm(tableForm);
				
				
				String billno = tableForm.getProperty("billno");
				if(billno==null||"null".equals(billno)){
					billno="";
				}
				String maintcontractno = tableForm.getProperty("maintcontractno");
				if(maintcontractno==null||"null".equals(maintcontractno)){
					maintcontractno="";
				}
				String salescontractno = tableForm.getProperty("salescontractno");
				if(salescontractno==null||"null".equals(salescontractno)){
					salescontractno="";
				}
				String companyname = tableForm.getProperty("companyid");
				if(companyname==null||"null".equals(companyname)){
					companyname="";
				}
				String maintdivision = tableForm.getProperty("maintdivision");
				if(maintdivision==null||"null".equals(maintdivision)){
					maintdivision="";
				}
				String maintstation = tableForm.getProperty("maintstation");
				if(maintstation==null||"null".equals(maintstation)){
					maintstation="";
				}
				String elevatorno = tableForm.getProperty("elevatorno");
				if(elevatorno==null||"null".equals(elevatorno)){
					elevatorno="";
				}
				
				
				//第一次进入页面时根据登陆人初始化所属维保分部
				List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
				if(maintdivision == null || maintdivision.equals("")){
					Map map = (Map)maintDivisionList.get(0);
					maintdivision = (String)map.get("grcid");
				}
				
				hs = HibernateUtil.getSession();
				Query query = null;
				
				String sql = "select mc.billNo,cu.companyName,mc.maintContractNo,md.salesContractNo,md.elevatorNo,c.comfullname,s.storagename,mc.contractSdate,mc.contractEdate,cu.companyid,mc.maintDivision,mc.maintStation " 
				+"from MaintContractMaster mc "
				+"join MaintContractDetail md "
				+"on mc.BillNo=md.BillNo and mc.AuditStatus='Y' and isnull(md.IsSurrender,'N')='N' and (mc.ContractStatus='ZB' or mc.ContractStatus='XB') "
				+"join Customer cu "
				+"on mc.companyId=cu.companyId "
				+"join Company c "
				+"on mc.maintDivision=c.comid "
				+"join Storageid s "
				+"on mc.maintStation=s.storageid";
				
				if (billno != null && !billno.equals("")) {
					sql += " and mc.billNo like '%"+billno.trim()+"%'";
				}
				if (maintcontractno != null && !maintcontractno.equals("")) {
					sql += " and mc.maintcontractno like '%"+maintcontractno.trim()+"%'";
				}				
				if (salescontractno != null && !salescontractno.equals("")) {
					sql += " and md.salescontractno like '%"+salescontractno.trim()+"%'";
				}				
				if (elevatorno != null && !elevatorno.equals("")) {
					sql += " and md.elevatorno like '%"+elevatorno.trim()+"%'";
				}				
				if (companyname != null && !companyname.equals("")) {
					sql += " and (cu.companyId like '%"+companyname.trim()+"%' or cu.companyName like '%"+companyname.trim()+"%' )";
				}				
				if (maintdivision != null && !maintdivision.equals("") && !maintdivision.equals("%")) {
					sql += " and mc.maintdivision = '"+maintdivision.trim()+"'";
				}				
				if (maintstation != null && !maintstation.equals("") && !maintstation.equals("%")) {
					sql += " and mc.maintstation = '"+maintstation.trim()+"'";
				}				
				if (table.getIsAscending()) {
					if("CompanyName".equals(table.getSortColumn())){
						
						sql += " order by cu."+ table.getSortColumn() +" desc";
					}else if("SalesContractNo".equals(table.getSortColumn())||"ElevatorNo".equals(table.getSortColumn())){

						sql += " order by md."+ table.getSortColumn() +" desc";
					}else{
						
						sql += " order by mc."+ table.getSortColumn() +" desc";
					}
				} else {
					if("CompanyName".equals(table.getSortColumn())){
						
						sql += " order by cu."+ table.getSortColumn() +" asc";
					}else if("SalesContractNo".equals(table.getSortColumn())||"ElevatorNo".equals(table.getSortColumn())){
						
						sql += " order by md."+ table.getSortColumn() +" asc";
					}else{
						
						sql += " order by mc."+ table.getSortColumn() +" asc";
					}
				}
				System.out.println(sql);
				query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List contractTransferAssignList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map map=new HashMap();
					map.put("billno", objs[0]);
					map.put("companyname", objs[1]);
					map.put("maintcontractno", objs[2]);
					map.put("salescontractno", objs[3]);
					map.put("elevatorno", objs[4]);
					map.put("maintdivisionname", objs[5]);
					map.put("maintstationname", objs[6]);
					map.put("contractsdate", objs[7]);
					map.put("contractedate", objs[8]);
					map.put("companyid", objs[9]);
					map.put("maintdivision", objs[10]);
					map.put("maintstation", objs[11]);
					contractTransferAssignList.add(map);
				}

				table.addAll(contractTransferAssignList);
				session.setAttribute("contractTransferAssignAjax", table);
				
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);
				

				List mainStationList=new ArrayList();
					String hql="select a from Storageid a where a.comid like '"+maintdivision+"' " +
							"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
					mainStationList=hs.createQuery(hql).list();
				  
					 Storageid storid=new Storageid();
					 storid.setStorageid("%");
					 storid.setStoragename("全部");
					 mainStationList.add(0,storid);
				request.setAttribute("mainStationList", mainStationList);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}

			forward = mapping.findForward("contracttransferassignAjax");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
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
			HttpServletResponse response, String folder, String billNo,String filename) {
		List returnList = new ArrayList();
		folder = PropertiesUtil.getProperty(folder).trim();//
		DynaActionForm dform = (DynaActionForm) form;
		
		FormFile formFile = null;
		Fileinfo file = null;
		if (form.getMultipartRequestHandler() != null) {
			Hashtable hash = form.getMultipartRequestHandler().getFileElements();
			
			if (hash != null) {
				HandleFile hf = new HandleFileImpA();
				for(Iterator it = hash.keySet().iterator(); it.hasNext();){
					String key=(String)it.next();
					String[] keyarr = key.split("_");
					String jnlno = "";
					if (keyarr!=null) {
						jnlno = keyarr[0];
					}
					formFile=(FormFile)hash.get(key);
					Map map = new HashMap();
					if(formFile!=null){
						try {
							if(!formFile.getFileName().equals("")){
								String newFileName=filename+"_"+key+"_"+formFile.getFileName();
								map.put("oldfilename", formFile.getFileName());
								map.put("filepath", CommonUtil.getNowTime("yyyy-MM-dd")+"/");
								map.put("fileSize", new Double(formFile.getFileSize()));
								map.put("fileformat",formFile.getContentType());
								map.put("newFileName", newFileName);
								map.put("jnlno", jnlno);
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
	public boolean savePicterTodb(HttpServletRequest request,List fileInfoList,String billno,Session hs){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		boolean saveFlag = true;
		if(null != fileInfoList && fileInfoList.size()>0){
			String sql="";
			ContractTransferDebugFileinfo ctdFileinfo=null;
			try {
				int len = fileInfoList.size();
				for(int i = 0 ; i < len ; i++){
					Map map = (Map)fileInfoList.get(i);
					
					String jnlnostr = (String)map.get("jnlno");
					String[] jnlnoarr = jnlnostr.split(",");
					
					for (int j = 0; j < jnlnoarr.length; j++) {
						ctdFileinfo=new ContractTransferDebugFileinfo();
						ctdFileinfo.setBillNo(billno);
						ctdFileinfo.setOldFileName((String)map.get("oldfilename"));
						ctdFileinfo.setNewFileName((String)map.get("newFileName"));
						ctdFileinfo.setFileSize(Double.valueOf(map.get("fileSize").toString()));
						ctdFileinfo.setFilePath((String)map.get("filepath"));
						ctdFileinfo.setFileFormat((String)map.get("fileformat"));
						ctdFileinfo.setUploadDate(CommonUtil.getNowTime());
						ctdFileinfo.setUploader(userInfo.getUserID());
						hs.save(ctdFileinfo);
						hs.flush();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				saveFlag = false;
				
			}
		}
		return saveFlag;
	}
	/**
	 * 文件下载
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDownloadFile(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		String filename=request.getParameter("filesname");
		String fileOldName=request.getParameter("fileOldName");
		String filePath=request.getParameter("filePath");
		String folder=request.getParameter("folder");
		if(folder==null || "".equals(folder)){
			folder="ContractTransferDebugFileinfo.file.upload.folder";
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
	
	/**
	 * 文件删除
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toDeleteFile(ActionMapping mapping, ActionForm form,
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
			folder ="ContractTransferDebugFileinfo.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		int zNum=0,yNum=0;
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if(id!=null && id.length()>0){
				
				ContractTransferDebugFileinfo ctFdileinfo = (ContractTransferDebugFileinfo) hs.get(ContractTransferDebugFileinfo.class, Integer.valueOf(id));
				String fileName=ctFdileinfo.getNewFileName();
				hs.delete(ctFdileinfo);
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
}