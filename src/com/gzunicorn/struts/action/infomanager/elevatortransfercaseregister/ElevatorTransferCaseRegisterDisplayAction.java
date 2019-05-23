package com.gzunicorn.struts.action.infomanager.elevatortransfercaseregister;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
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
import org.apache.struts.util.MessageResources;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.pdfprint.BarCodePrint;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.JSONUtil;
import com.gzunicorn.common.util.NumberToCN;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SqlBeanUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.file.HandleFile;
import com.gzunicorn.file.HandleFileImpA;
import com.gzunicorn.hibernate.basedata.Fileinfo;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.basedata.termsecurityrisks.TermSecurityRisks;
import com.gzunicorn.hibernate.contractpaymentmanage.contractticketmanage.ContractTicketManage;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;
import com.gzunicorn.hibernate.infomanager.handoverelevatorcheckitemregister.HandoverElevatorCheckItemRegister;
import com.gzunicorn.hibernate.infomanager.handoverelevatorspecialregister.HandoverElevatorSpecialRegister;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.workflow.bo.JbpmExtBridge;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class ElevatorTransferCaseRegisterDisplayAction extends DispatchAction {

	Log log = LogFactory.getLog(ElevatorTransferCaseRegisterDisplayAction.class);
	
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
		if(!"toDownloadFileRecord1".equals(name)){
			/** **********开始用户权限过滤*********** */
			SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "elevatortransfercaseregisterdisplay", null);
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

	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("navigator.location","安装维保交接电梯情况查看 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form,request,response);
				//forward = mapping.findForward("exportExcel");
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport","");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "elevatorTransferCaseRegisterDisplayList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fElevatorTransferCaseRegisterDisplay");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("billno");
			table.setIsAscending(false);
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
			String staffName = tableForm.getProperty("staffName");// 厂检人员名称
			String department = tableForm.getProperty("department");// 所属部门					
			String submitType = tableForm.getProperty("submitType");// 提交标志
			String salesContractNo = tableForm.getProperty("salesContractNo");// 销售合同号
			String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号
			String insCompanyName = tableForm.getProperty("insCompanyName");// 安装公司名称
			String factoryCheckResult=tableForm.getProperty("factoryCheckResult");
			String checkVersion=tableForm.getProperty("checkVersion");
			String processStatus=tableForm.getProperty("processStatus");
			String projectManager = tableForm.getProperty("projectManager");//项目经理
			String sdate1 = tableForm.getProperty("sdate1");
			String edate1 = tableForm.getProperty("edate1");
			String isClose = tableForm.getProperty("isClose");
			String projectProvince=tableForm.getProperty("projectProvince");//项目省份
			
			//安装部，维保总部
			if(!userInfo.getComID().equals("14") && !userInfo.getComID().equals("00")){
				department=userInfo.getComID();
			}
			
			String isshowrole="N";//只让他们看已经提交的及提交之后的结果
			// A01 管理员,A61  厂检部长  ,A55  厂检文员  
			if(userInfo.getRoleID().equals("A01") || 
					userInfo.getRoleID().equals("A61") || 
					userInfo.getRoleID().equals("A55")){
				isshowrole="Y";
			}
			request.setAttribute("isshowrole", isshowrole);
			
			Session hs = null;
			Query query = null;
			 ElevatorTransferCaseRegister etfcm=null;
			try {

				hs = HibernateUtil.getSession();

				String sql = "select etcr,lu.username from ElevatorTransferCaseRegister etcr,Loginuser lu"+
						" where etcr.staffName=lu.userid";
				
				if (billNo != null && !billNo.trim().equals("")){ 					
					sql+=" and etcr.billno like '%"+billNo.trim()+"%'";
				}
				if (projectName != null && !projectName.trim().equals("")) {					
					sql+=" and etcr.projectName like '%"+projectName.trim()+"%'";
				}
				if (staffName != null && !staffName.trim().equals("")) {
					sql+=" and (lu.username like '%"+staffName.trim()+"%' or lu.userid like '%"+staffName.trim()+"%' )";
				}
				if (department != null && !department.equals("")) {
					sql+=" and etcr.department like '"+department.trim()+"'";
				}
				if (submitType != null && !submitType.equals("")) {
					sql+=" and etcr.submitType like '"+submitType.trim()+"'";
				}
				if (salesContractNo != null && !salesContractNo.trim().equals("")) {
					sql+=" and etcr.salesContractNo like '%"+salesContractNo.trim()+"%'";
			     }
				if (elevatorNo != null && !elevatorNo.trim().equals("")) {
					sql+=" and etcr.elevatorNo like '%"+elevatorNo.trim()+"%'";
			     }
				if (insCompanyName != null && !insCompanyName.trim().equals("")) {
					sql+=" and etcr.insCompanyName like '%"+insCompanyName.trim()+"%'";
			    }
				if (factoryCheckResult != null && !factoryCheckResult.equals("")) {
					sql+=" and etcr.factoryCheckResult='"+factoryCheckResult.trim()+"'";
			    }
				if (checkVersion != null && !checkVersion.trim().equals("")) {
					sql+=" and etcr.checkVersion like '%"+checkVersion.trim()+"%'";
			    }
				
				if(processStatus!=null && !processStatus.trim().equals("")){
					sql+=" and etcr.processStatus like '%"+processStatus.trim()+"%'";
				}else{
					if(isshowrole.equals("N")){
						//只让他们看已经提交的及提交之后的结果
						sql+=" and etcr.processStatus in ('2','3')";
					}
				}
				if(projectManager!=null && !projectManager.trim().equals("")){
					sql+=" and etcr.projectManager like '%"+projectManager.trim()+"%'";
				}
				if(sdate1!=null && !sdate1.equals("")){
					sql+=" and etcr.checkTime >'"+sdate1+" 00:00:00'";
				}
				if(edate1!=null && !edate1.equals("")){
					sql+=" and etcr.checkTime <'"+edate1+" 99:99:99'";
				}

				if (isClose != null && !isClose.equals("")) {
					sql+=" and isnull(etcr.isClose,'N') = '"+isClose+"'";
				}
				if (projectProvince != null && !projectProvince.trim().equals("")) {
					sql+=" and projectProvince like '%"+projectProvince.trim()+"%'";
				}
				
				if (table.getIsAscending()) {
					sql += " order by etcr."+ table.getSortColumn() +" asc";
				} else {
					sql += " order by etcr."+ table.getSortColumn() +" desc";
					
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
					Object[] objs=(Object[])object;
					etfcm=new ElevatorTransferCaseRegister();
					etfcm=(ElevatorTransferCaseRegister) objs[0];
					etfcm.setStaffName(objs[1].toString());
					etfcm.setDepartment(bd.getName_Sql("Company", "comname", "comid", etfcm.getDepartment()));
					etfcm.setR2(bd.getName_Sql("ViewFlowStatus", "typename", "typeid", String.valueOf(etfcm.getStatus())));
					
					String pstaturs=etfcm.getProcessStatus();
					if(pstaturs!=null && pstaturs.trim().equals("1")){
						//如果处理状态为 1 已登记未提交 ，就不显示厂检结果
						etfcm.setFactoryCheckResult("");
					}
					
					elevatorTransferCaseRegisterList.add(etfcm);
				}
				table.addAll(elevatorTransferCaseRegisterList);
				session.setAttribute("elevatorTransferCaseRegisterDisplayList", table);
				
				String sql1="select comid,comfullname from Company where (comtype='1' or comtype='2') and enabledflag='Y' ";				
				if(!userInfo.getComID().equals("14") && !userInfo.getComID().equals("00")){
					sql1+=" and comid='"+userInfo.getComID()+"'";
				}
				sql1+=" order by comid desc";
				List list1=hs.createQuery(sql1).list();
				List departmentList =new ArrayList();
				if(list1!=null&&list1.size()>0){
					//安装部，维保总部
					if(userInfo.getComID().equals("14") || userInfo.getComID().equals("00")){
						HashMap map=new HashMap();
						map.put("comid", "");
						map.put("comfullname", "全部");
						departmentList.add(map);
					}
					for(int i=0;i<list1.size();i++){
						Object[] objects=(Object[]) list1.get(i);
						HashMap map=new HashMap();
						map.put("comid", objects[0]);
						map.put("comfullname", objects[1]);
						departmentList.add(map);
					}
				}
				request.setAttribute("departmentList", departmentList);
			
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
			forward = mapping.findForward("elevatorTransferCaseRegisterDisplayList");
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
	 * 点击查看的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","安装维保交接电梯情况查看 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		String returnMethod=request.getParameter("returnMethod");
		display(form, request, errors, "display");

		//lijun add 首页转派跳转查看后，不需要再次出现在首页
		String workisdisplay=request.getParameter("workisdisplay");
		if(workisdisplay!=null && workisdisplay.equals("Y")){
			request.setAttribute("workisdisplay", workisdisplay);
			Session hs = null;
			Transaction tx = null;
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				
				String id=request.getParameter("id");
				String upsql="update ElevatorTransferCaseRegister set workisdisplay='"+workisdisplay+"' where billno='"+id+"'";
				hs.connection().prepareStatement(upsql).executeUpdate();
				
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
		}
		
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
		forward = mapping.findForward("elevatorTransferCaseRegisterDisplayDisplay");
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
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			String billNo = tableForm.getProperty("billno");// 流水号
			String projectName = tableForm.getProperty("projectName");// 项目名称
			String staffName = tableForm.getProperty("staffName");// 厂检人员名称
			String department = tableForm.getProperty("department");// 所属部门					
			String submitType = tableForm.getProperty("submitType");// 提交标志
			String salesContractNo = tableForm.getProperty("salesContractNo");// 销售合同号
			String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号
			String insCompanyName = tableForm.getProperty("insCompanyName");// 安装公司名称
			String factoryCheckResult=tableForm.getProperty("factoryCheckResult");
			String checkVersion=tableForm.getProperty("checkVersion");
			String processStatus=tableForm.getProperty("processStatus");
			String projectManager = tableForm.getProperty("projectManager");//项目经理
			String sdate1 = tableForm.getProperty("sdate1");
			String edate1 = tableForm.getProperty("edate1");
			String isClose = tableForm.getProperty("isClose");
			String projectProvince=tableForm.getProperty("projectProvince");//项目省份
			
			if(!userInfo.getComID().equals("14") && !userInfo.getComID().equals("00")){
				department=userInfo.getComID();
			}
			
			String isshowrole="N";//只让他们看已经提交的及提交之后的结果
			// A01 管理员,A61  厂检部长  ,A55  厂检文员  
			if(userInfo.getRoleID().equals("A01") || 
					userInfo.getRoleID().equals("A61") || 
					userInfo.getRoleID().equals("A55")){
				isshowrole="Y";
			}


			String sql = "select etcr,lu.username,cy.comname"+
					" from ElevatorTransferCaseRegister etcr,Loginuser lu,Company cy"+
					" where etcr.staffName=lu.userid and etcr.department=cy.comid ";
			if (billNo != null && !billNo.equals("")){ 					
				sql+=" and etcr.billno like '%"+billNo.trim()+"%'";
			}
			if (projectName != null && !projectName.equals("")) {					
				sql+=" and etcr.projectName like '%"+projectName.trim()+"%'";
			}
			if (staffName != null && !staffName.equals("")) {
				sql+=" and (lu.username like '%"+staffName.trim()+"%' or lu.userid like '%"+staffName.trim()+"%' )";
			}
			if (department != null && !department.equals("")) {
				sql+=" and etcr.department like '"+department.trim()+"'";
			}
			if (submitType != null && !submitType.equals("")) {
				sql+=" and etcr.submitType like '"+submitType.trim()+"'";
			}
			if (salesContractNo != null && !salesContractNo.equals("")) {
				sql+=" and etcr.salesContractNo like '%"+salesContractNo.trim()+"%'";
		     }
			if (elevatorNo != null && !elevatorNo.equals("")) {
				sql+=" and etcr.elevatorNo like '%"+elevatorNo.trim()+"%'";
		     }
			if (insCompanyName != null && !insCompanyName.equals("")) {
				sql+=" and etcr.insCompanyName like '%"+insCompanyName.trim()+"%'";
		    }
			if (factoryCheckResult != null && !factoryCheckResult.equals("")) {
				sql+=" and etcr.factoryCheckResult='"+factoryCheckResult.trim()+"'";
		    }
			if (checkVersion != null && !checkVersion.equals("")) {
				sql+=" and etcr.checkVersion like '%"+checkVersion.trim()+"%'";
		    }
			if(processStatus!=null && !processStatus.equals("")){
				sql+=" and etcr.processStatus like '%"+processStatus.trim()+"%'";
			}else{
				if(isshowrole.equals("N")){
					//只让他们看已经提交的及提交之后的结果
					sql+=" and etcr.processStatus in ('2','3')";
				}
			}
			if(projectManager!=null && !projectManager.equals("")){
				sql+=" and etcr.projectManager like '%"+projectManager.trim()+"%'";
			}
			if(sdate1!=null && !sdate1.equals("")){
				sql+=" and etcr.checkTime >'"+sdate1+" 00:00:00'";
			}
			if(edate1!=null && !edate1.equals("")){
				sql+=" and etcr.checkTime <'"+edate1+" 99:99:99'";
			}
			if (isClose != null && !isClose.equals("")) {
				sql+=" and isnull(etcr.isClose,'N') = '"+isClose+"'";
			}
			if (projectProvince != null && !projectProvince.trim().equals("")) {
				sql+=" and projectProvince like '%"+projectProvince.trim()+"%'";
			}
			
			sql += " order by etcr.billno desc";
					
			List roleList =hs.createQuery(sql).list();

			XSSFSheet sheet = wb.createSheet("安装维保交接电梯情况");

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = null;
				
				String[] titlename={"流水号","实际厂检时间","提交厂检时间","厂检次数","项目名称","安装公司名称","销售合同号","电梯编号",
						"厂检人员名称","所属部门","厂检结果","厂检分数","是否扣款","项目经理","调试人员","是否初次安装","厂检结果2"};
				
				for(int j=0;j<titlename.length;j++){
					cell0 = row0.createCell((short)j);
					cell0.setCellValue(titlename[j]);
				}
				for (int i = 0; i < l; i++) {
					Object[] obj=(Object[])roleList.get(i);
					
					ElevatorTransferCaseRegister etfcm=(ElevatorTransferCaseRegister) obj[0];
					String staffnamestr=obj[1].toString();
					String comnamestr=obj[2].toString();
					
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);

					// 创建Excel列
					XSSFCell cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(etfcm.getBillno());
					
					String CheckDate=etfcm.getCheckDate();
					if(CheckDate==null){
						CheckDate="";
					}
					String CheckTime2=etfcm.getCheckTime2();
					if(CheckTime2==null){
						CheckTime2="";
					}
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CheckDate+" "+CheckTime2);
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(etfcm.getCheckTime());
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(etfcm.getCheckNum());
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(etfcm.getProjectName());

					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(etfcm.getInsCompanyName());

					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(etfcm.getSalesContractNo());
					
					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(etfcm.getElevatorNo());
					
					cell = row.createCell((short)8);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(staffnamestr);

					cell = row.createCell((short)9);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(comnamestr);
					
					cell = row.createCell((short)10);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(etfcm.getFactoryCheckResult());
					
					cell = row.createCell((short)11);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(etfcm.getR2());

					cell = row.createCell((short)12);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String isdeduct=etfcm.getIsDeductions();
					if(isdeduct!=null && isdeduct.trim().equals("Y")){
						cell.setCellValue("同意扣款");
					}else if(isdeduct!=null && isdeduct.trim().equals("N")){
						cell.setCellValue("同意不扣款");
					}else{
						cell.setCellValue("");
					}
					
					cell = row.createCell((short)13);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(etfcm.getProjectManager());
					
					cell = row.createCell((short)14);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(etfcm.getDebugPers());
					
					cell = row.createCell((short)15);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String firstInstallation=etfcm.getFirstInstallation();
					if("Y".equals(firstInstallation)){
						cell.setCellValue("是");
					}else if("N".equals(firstInstallation)){
						cell.setCellValue("否");
					}else{
						cell.setCellValue("");
					}
					cell = row.createCell((short)16);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(etfcm.getFactoryCheckResult2());
					
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("安装维保交接电梯情况", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	
	
	/*
	 * 查看方法
	 */
	public void display(ActionForm form, HttpServletRequest request ,ActionErrors errors ,String flag){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		if(id==null || id.equals("")){
			id=(String)dform.get("id");
		}
		if(id==null || id.equals("")){
			id=(String)dform.get("nextId");
		}
		Session hs = null;
		List etcpList=new ArrayList();
		List bhTypeList=bd.getPullDownList("ElevatorTransferCaseRegister_BhType");
		String CSheight = PropertiesUtil.getProperty("CSheight");
		String CSwidth = PropertiesUtil.getProperty("CSwidth");
		String CIheight = PropertiesUtil.getProperty("CIheight");
		String CIwidth = PropertiesUtil.getProperty("CIwidth");
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
					
					if("display".equals(flag)){// 查看		
						register.setDepartment(bd.getName_Sql("Company", "comname", "comid", register.getDepartment()));
						register.setStaffName(bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
						register.setR1(register.getFloor()+"/"+register.getStage()+"/"+register.getDoor());
						register.setTransferId(bd.getName(hs, "Loginuser","username", "userid",register.getTransferId()));
						register.setBhType(bd.getOptionName(register.getBhType(), bhTypeList));
						dform.set("id",id);
						
						String pstaturs=register.getProcessStatus();
						if(pstaturs!=null && pstaturs.trim().equals("1")){
							//如果处理状态为 1 已登记未提交 ，就不显示厂检结果
							register.setFactoryCheckResult("");
						}
						
						request.setAttribute("elevatorTransferCaseRegisterBean", register);						
					}
					//甲方问题
					List hecirList=new ArrayList();
					List hecirDeketeList = new ArrayList();
		
					String sql = "select b.examType examType1,p.pullname examType,b.checkItem checkItem," +
							"b.issueCoding issueCoding,b.issueContents IssueContents1,b.rem ,b.isRecheck isRecheck," +
							"b.isDelete isDelete,b.jnlno jnlno,b.deleteRem,b.isyzg "
							+ "from HandoverElevatorCheckItemRegister b ,pulldown p "
							+ " where p.pullid=b.examType "
							+ " and p.typeflag='HandoverElevatorCheckItem_ExamType' "
							+ " and b.billno='"+id+"' order by b.isRecheck desc,p.orderby,b.issueCoding ";	
					//System.out.println(">>>"+sql);
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
						
						map.put("examType1", values[0]);
						map.put("examType", values[1]);
						map.put("checkItem", values[2]);
						map.put("issueCoding", values[3]);
						map.put("issueContents1", values[4]);
						map.put("rem", values[5]);
						map.put("isRecheck", values[6]);
						map.put("deleteRem", values[9]);
						map.put("isyzg", values[10]);
						String hql="from HandoverElevatorCheckFileinfo h where h.handoverElevatorCheckItemRegister.jnlno ='"+(String)values[8]+"' ";
						List fileList=hs.createQuery(hql).list();
						if(fileList!=null&&fileList.size()>0){
							map.put("fileList", fileList);
						}
						
						
						int cn=register.getCheckNum();
						if(cn>1){
							if(values[6].equals("N"))
							{
							map.put("color", "red");
							}else{
							map.put("color", "black");	
							}
						}
						if(values[7].equals("Y")){
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
							
							hecirDeketeList.add(map);
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
							
							hecirList.add(map);
						}
					 }
					request.setAttribute("hecirList", hecirList);
					if(hecirDeketeList!=null&&hecirDeketeList.size()>0){
					request.setAttribute("hecirDeketeList", hecirDeketeList);
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
					if(list1!=null&&list1.size()>0)
					{
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
					request.setAttribute("etcpList", etcpList);
					}
					
					//厂检部长上传的附件
					String filesql="from Wbfileinfo where busTable='ElevatorTransferCaseRegister' and materSid='"+id+"'";
					List wbfilelist=hs.createQuery(filesql).list();
					if(wbfilelist!=null && wbfilelist.size()>0){
						request.setAttribute("wbfilelist", wbfilelist);
					}
					
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
	
	@SuppressWarnings("unchecked")
	public ActionForward toPreparePrintRecord(ActionMapping mapping,ActionForm form,
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
										+ "from HandoverElevatorCheckItemRegister b,pulldown p "
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
	public ActionForward toDownloadFileRecord1(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)	throws IOException, ServletException {
		
		Session hs = null;

		String filepath = request.getParameter("customerSignature");//流水号
		String filepath1 = request.getParameter("customerImage");
		String localPath="";
		String oldname="";
		String folder = request.getParameter("folder");		//文件夹
		if(null == folder || "".equals(folder)){
			folder ="ElevatorTransferCaseRegister.file.upload.folder";
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

}	