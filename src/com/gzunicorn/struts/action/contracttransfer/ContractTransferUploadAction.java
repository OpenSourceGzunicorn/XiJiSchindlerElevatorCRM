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
import java.sql.ResultSet;
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
import com.gzunicorn.hibernate.contracttransfer.ContractTransferDebugFileinfo;
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
import com.gzunicorn.struts.action.xjsgg.SendURLFile;
import com.gzunicorn.workflow.bo.JbpmExtBridge;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class ContractTransferUploadAction extends DispatchAction {

	Log log = LogFactory.getLog(ContractTransferUploadAction.class);
	
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
	 * 合同交接资料上传
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
		
		if(!"toDisplayRecord".equals(name)&&!"toDownloadFile".equals(name)){
			/** **********开始用户权限过滤*********** */
			SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "ContractTransferUpload", null);
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
		
		request.setAttribute("navigator.location","合同交接资料上传 >> 查询列表");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "contractTransferUploadList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fContractTransferUpload");
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
			String transfeSubmitType = tableForm.getProperty("transfeSubmitType");// 提交标志
			
			String transferflag = "0";
			
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
				String sqlss="select * from view_mainstation where roleid in ('A03','A48') and roleid = '"+userInfo.getRoleID()+"'";
				List vmlist=hs.createSQLQuery(sqlss).list();
				if(vmlist!=null && vmlist.size()>0){
					if(maintstation == null || maintstation.trim().equals("")){
						maintstation=userInfo.getStorageId();
					}
				}

				List mainStationList=new ArrayList();
				//维保站下拉框  A03=维保经理,A48=维保站文员,维保站长 A49
				
				if ("A01".equals(userInfo.getRoleID())) {
					String hql="select a from Storageid a where a.comid like '"+maintDivision+"' " +
							"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
					mainStationList=hs.createQuery(hql).list();
					  
					Storageid storid=new Storageid();
					storid.setStorageid("%");
					storid.setStoragename("全部");
					mainStationList.add(0,storid);
				}else {
					String hql="select a from Storageid a where "
							+ "(a.storageid= '"+userInfo.getStorageId()+"' or "
							+ "a.storageid in(select parentstorageid from Storageid a where a.storageid= '"+userInfo.getStorageId()+"')) "
							+ "and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";	
					mainStationList=hs.createQuery(hql).list();
				}
				
				request.setAttribute("mainStationList", mainStationList);
				
				String sql = "select cm.BillNo,cu.CompanyName,cm.MaintContractNo,cm.SalesContractNo," 
					+" cm.ElevatorNo,c.ComFullName,s.StorageName,cm.ContractSDate,cm.ContractEDate," 
					+" cm.TransfeSubmitType,isnull(er.billno,'') as billNo2,isnull(cm.AuditRem,''),isnull(cb.OperDate,'') as cbOperDate " 
					+"from ContractTransferMaster cm " 
					+" left join ElevatorTransferCaseRegister er on cm.elevatorNo = er.elevatorNo "
					+" and er.ProcessStatus in('2','3') "
					+" and er.checkNum = (select MAX(ina.checkNum) from ElevatorTransferCaseRegister ina " 
					+" where ina.elevatorNo = er.elevatorNo)"
					+" join Customer cu on cm.CompanyID = cu.CompanyID " 
					+" join Company c on cm.MaintDivision = c.ComID " 
					+" join StorageID s on cm.MaintStation = s.StorageID" 
					+" left join ContractTransferFeedback cb "
					+" on cm.BillNo=cb.BillNo "
					+" and cb.OperDate=(select max(cfb.OperDate) from ContractTransferFeedback cfb where cm.BillNo=cfb.BillNo) "
					+" where cm.submitType = 'Y' ";
				
				if (!"A01".equals(userInfo.getRoleID())) {
					if (vmlist!=null && vmlist.size()>0) {
						sql += " and isnull(cm.isTrans,'N') = 'N'";
					}else {
						sql += " and isnull(cm.isTrans,'N') = 'Y' and cm.wbgTransfeId = '"+userInfo.getUserID()+"'";
						transferflag = "1";
					}
				}
				
				
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
				if (transfeSubmitType != null && !transfeSubmitType.equals("")) {
					sql += " and isnull(cm.TransfeSubmitType,'N') like '"+transfeSubmitType.trim()+"'";
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
				List contractTransferUploadList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					
					String FileType = "";
					String fileTypesSql = "select FileType from ContractTransferFileTypes " 
						+"where BillNo = '"+String.valueOf(objs[0])+"' order by FileType";
					List fileTypesList=hs.createSQLQuery(fileTypesSql).list(); 
					for (int i = 0; i < fileTypesList.size(); i++) {
						FileType +=  fileTypesList.get(i)+",";
					}
					
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
					master.put("transfeSubmitType", String.valueOf(objs[9]));
					master.put("billNo2", String.valueOf(objs[10]));
					master.put("auditRem", String.valueOf(objs[11]));
					master.put("operdate", String.valueOf(objs[12]));
					master.put("fileType", FileType);
					
					String filesql = "select * from ContractTransferFileinfo " 
						+" where jnlno in (select jnlno from ContractTransferFileTypes " 
						+" where BillNo = '"+String.valueOf(objs[0])+"')";
					List fList = hs.createSQLQuery(filesql).list();
					
					if (fList!=null&&fList.size()>0) {
						master.put("fFlag", "Y");
					}else{
						master.put("fFlag", "N");
					}
					
					List fileList = new ArrayList();
					
					String debugfilehql = "from ContractTransferDebugFileinfo where billNo='"+String.valueOf(objs[0])+"' ";
					List debugfileList = hs.createQuery(debugfilehql).list();
					if (debugfileList!=null&&debugfileList.size()>0) {
						for (int i = 0; i < debugfileList.size(); i++) {
							ContractTransferDebugFileinfo debugFileinfo = (ContractTransferDebugFileinfo) debugfileList.get(i);
							HashMap map = new HashMap();
							map.put("fileSid", debugFileinfo.getFileSid());
							map.put("oldFileName", debugFileinfo.getOldFileName());
							map.put("newFileName", debugFileinfo.getNewFileName());
							map.put("filePath", debugFileinfo.getFilePath());
							map.put("folder", "ContractTransferDebugFileinfo.file.upload.folder");
							fileList.add(map);
						}
					}
					
					String dfilesql = "select FileSid,OldFileName,FileId from DebugSheetFileInfo where ElevatorNo = '"+String.valueOf(objs[4])+"'";
					List dfileList = hs.createSQLQuery(dfilesql).list();
					if (dfileList!=null&&dfileList.size()>0) {
						for (int i = 0; i < dfileList.size(); i++) {
							Object[] oj = (Object[]) dfileList.get(i);
							HashMap map = new HashMap();
							map.put("fileSid", oj[0]);
							map.put("oldFileName", oj[1]);
							map.put("filePath", oj[2]);
							map.put("newFileName", "DebugSheetFileInfo");
							map.put("folder", "DebugSheetFileInfo");
							fileList.add(map);
						}
					}
					
					for (int i = 0; i < fileList.size(); i++) {
						HashMap map = (HashMap) fileList.get(i);
						if(i%2==0){
							map.put("trflag1", "Y");
						}else if ((i+1)%2==0) {
							map.put("trflag2", "Y");
						}
					}
					
					master.put("fileList", fileList);
					
					contractTransferUploadList.add(master);
				}

				table.addAll(contractTransferUploadList);
				session.setAttribute("contractTransferUploadList", table);
				
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);
				request.setAttribute("transferflag", transferflag);
				
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
			forward = mapping.findForward("contractTransferUploadList");
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
		
		request.setAttribute("navigator.location","合同交接资料上传 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		
		Boolean display = bd.contractTransferDisplay(form, request, errors, "display");
		
		request.setAttribute("display", "yes");
		
		forward = mapping.findForward("ContractTransferUploadDisplay");
		return forward;
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

		request.setAttribute("navigator.location","合同交接资料上传 >> 上传");
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
		
		dform.set("id",id);
		
		String mainenter = request.getParameter("mainenter");
		request.setAttribute("mainenter", mainenter);
		
		String isflag = request.getParameter("isflag");
		request.setAttribute("isflag", isflag);
		
		Session hs = null;
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Query query = null;
		try {
			hs = HibernateUtil.getSession();
			conn=hs.connection();
			
			id = id.replace(",", "','");
			
			String hql = "from ContractTransferMaster where billNo in ('"+id.trim()+"') ";
			List list = hs.createQuery(hql).list();
			
			if (list!=null && list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					ContractTransferMaster master = (ContractTransferMaster) list.get(i);
					
					master.setCompanyId(bd.getName("Customer", "companyName", "companyId", master.getCompanyId()));
					master.setMaintDivision(bd.getName("Company", "comfullname", "comid", master.getMaintDivision()));
					master.setMaintStation(bd.getName("Storageid", "storagename", "storageid", master.getMaintStation()));
				}
				
			}
			
			List ContractTransferFileTypesList = new ArrayList();
			
			String sql ="select jnlno= stuff((select ',' +jnlno from ContractTransferFileTypes t " 
				+" where t.FileType = a.FileType and t.BillNo in('"+id.trim()+"') for xml path('')),1,1,'')," 
				+"a.FileType,b.pullname from ContractTransferFileTypes a,pulldown b " 
				+" where a.FileType = b.pullid and b.typeflag = 'ContractFileTypes_FileType' " 
				+" and billNo in('"+id.trim()+"') group by FileType,b.pullname";
			
			ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            
            while(rs.next()){
            	HashMap map = new HashMap();
				map.put("jnlno", rs.getString("jnlno"));
				map.put("fileType", rs.getString("jnlno"));
				map.put("fileTypeName", rs.getString("pullname"));
				
				String fileHql="from ContractTransferFileinfo c where c.jnlno ='"+rs.getString("jnlno")+"' ";
				List fileList=hs.createQuery(fileHql).list();
				if(fileList!=null&&fileList.size()>0){
					map.put("fileFlag", "Y");
					map.put("fileList", fileList);
				}else {
					map.put("fileFlag", "N");
				}
				
				ContractTransferFileTypesList.add(map);
            }
			
			request.setAttribute("addflag", "Y");
			request.setAttribute("fileTypes", ContractTransferFileTypesList);
			request.setAttribute("masterList", list);	
			request.setAttribute("billNo", id.replace("','", ","));
									
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
		
		saveToken(request); //生成令牌，防止表单重复提交
		
		return mapping.findForward("ContractTransferUploadAdd");
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
		String transfeSubmitType = (String)dform.get("transfeSubmitType");
		
		Session hs = null;
		Transaction tx = null;
		
		String billNo = request.getParameter("billNo");
		
		try {
			//防止表单重复提交
			if(isTokenValid(request, true)){
				Boolean maxLengthExceeded = (Boolean) request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);			
				if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())){
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("doc.file.size.check.save"));
					forward = mapping.findForward("returnList");
				}else {
					List list = savePicter(form, request, response, "ContractTransferFileinfo.file.upload.folder", billNo,"ContractTransferFileTypes");
					boolean b=false;
					if(list!=null || list.size()>0){
						b=savePicterTodb(request, list, billNo);
					}
					
					if (!b) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
					}else {
						hs = HibernateUtil.getSession();
						tx = hs.beginTransaction();
						
						String[] billNoarr = billNo.split(",");
						
						for (int i = 0; i < billNoarr.length; i++) {
							ContractTransferMaster master = (ContractTransferMaster) hs.get(ContractTransferMaster.class, billNoarr[i]);
							master.setTransfeId(userInfo.getUserID());
							master.setTransfeDate(CommonUtil.getNowTime());
							
							master.setAuditOperid("");
							master.setAuditDate("");
							master.setAuditStatus("N");
							master.setAuditRem("");
							
							master.setAuditOperid2("");
							master.setAuditDate2("");
							master.setAuditStatus2("N");
							master.setAuditRem2("");
							
							if ("Y".equals(transfeSubmitType)) {
								master.setTransfeSubmitType(transfeSubmitType);
							}
							hs.save(master);
						}
						
						tx.commit();
					}
					
				}
			}else{
				saveToken(request);
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("navigator.submit.error"));
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
	
	/**
	 * 跳转到反馈方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareFeedBackRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		request.setAttribute("navigator.location","合同交接资料上传 >> 反馈");
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
		
		dform.set("id",id);
		
		Session hs = null;
		Query query = null;
		try {
			hs = HibernateUtil.getSession();
			
			id = id.replace(",", "','");
			
			String hql = "from ContractTransferMaster where billNo in ('"+id.trim()+"') ";
			List list = hs.createQuery(hql).list();
			
			if (list!=null && list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					ContractTransferMaster master = (ContractTransferMaster) list.get(i);
					
					master.setCompanyId(bd.getName("Customer", "companyName", "companyId", master.getCompanyId()));
					master.setMaintDivision(bd.getName("Company", "comfullname", "comid", master.getMaintDivision()));
					master.setMaintStation(bd.getName("Storageid", "storagename", "storageid", master.getMaintStation()));
				}
				
			}
			
			String sql = "select distinct a.FileType,b.pullname " 
				+" from ContractTransferFileTypes a,pulldown b " 
				+" where a.FileType = b.pullid and b.typeflag = 'ContractFileTypes_FileType' " 
				+" and billNo in ('"+id.trim()+"')";
			List fileTypesList = hs.createSQLQuery(sql).list();
			List ContractTransferFileTypesList = new ArrayList();
			
			for (Object object : fileTypesList) {
				
				Object[] objs = (Object[])object;
				
				HashMap map = new HashMap();
				map.put("fileType", objs[0]);
				map.put("fileTypeName", objs[1]);
				
				ContractTransferFileTypesList.add(map);
			}
			
			String typehql = "from ContractTransferFeedbackType where enabledFlag = 'Y' ";
			List typeList = hs.createQuery(typehql).list();
			
//			String hql = "from ContractTransferFeedback where billNo = '"+id.trim()+"' order by operDate desc ";
//			List list = hs.createQuery(hql).list();
//			List feedbackList = new ArrayList();
//			
//			if (list!=null && list.size()>0) {
//				for (int i = 0; i < list.size(); i++) {
//					ContractTransferFeedback feedback = (ContractTransferFeedback) list.get(i);
//					HashMap map = new HashMap();
//					map.put("jnlno", feedback.getJnlno());
//					map.put("operId", bd.getName("Loginuser", "username", "userid", feedback.getOperId()));
//					map.put("operDate", feedback.getOperDate());
//					map.put("transferRem", feedback.getTransferRem());
//					
//					String fileHql="from ContractTransferFeedbackFileinfo c where c.jnlno ='"+feedback.getJnlno()+"' ";
//					List fileList=hs.createQuery(fileHql).list();
//					if(fileList!=null&&fileList.size()>0){
//						map.put("fileList", fileList);
//					}
//					
//					feedbackList.add(map);
//				}
//			}
			
			request.setAttribute("typeList", typeList);
			request.setAttribute("operid", userInfo.getUserName());
			request.setAttribute("operdate", CommonUtil.getToday());
			request.setAttribute("feedbackflag", "Y");
//			request.setAttribute("feedbackList", feedbackList);
			request.setAttribute("fileTypes", ContractTransferFileTypesList);
			request.setAttribute("masterList", list);	
			request.setAttribute("billNo", id.replace("','", ","));
									
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
		
		saveToken(request); //生成令牌，防止表单重复提交
		
		return mapping.findForward("ContractTransferUploadFeedBack");
	}
	
	/**
	 * 点击反馈的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	public ActionForward toFeedBackRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	
		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;	
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String id = (String) dform.get("id");
		String transferRem = (String)dform.get("transferRem");
		String feedbacktypeid = (String)dform.get("feedbacktypeid");
		
		Session hs = null;
		Transaction tx = null;
		
		String billNo = request.getParameter("billNo");
		
		try {
			//防止表单重复提交
			if(isTokenValid(request, true)){
				Boolean maxLengthExceeded = (Boolean) request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);			
				if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue())){
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("doc.file.size.check.save"));
					forward = mapping.findForward("returnList");
				}else {
					hs = HibernateUtil.getSession();
					tx = hs.beginTransaction();
					
					String[] billNoarr = billNo.split(",");
					String jnlnoStr = "";
					
					for (int i = 0; i < billNoarr.length; i++) {
						ContractTransferMaster master = (ContractTransferMaster) hs.get(ContractTransferMaster.class, billNoarr[i]);
						master.setWorkisdisplay("N");
						hs.save(master);
						
						ContractTransferFeedback feedback = new ContractTransferFeedback();
						
						String[] jnlnoarr = CommonUtil.getBillno(CommonUtil.getToday().substring(2,4),"ContractTransferFeedback", 1);
						jnlnoStr +=	jnlnoarr[0] + ",";					
						
						feedback.setJnlno(jnlnoarr[0]);
						feedback.setBillNo(billNoarr[i]);
						feedback.setOperId(userInfo.getUserID());
						feedback.setOperDate(CommonUtil.getNowTime());
						feedback.setFeedbacktypeid(feedbacktypeid);
						feedback.setTransferRem(transferRem);
						hs.save(feedback);
					}
					
					tx.commit();
					
					List list = savePicter(form, request, response, "ContractTransferFeedbackFileinfo.file.upload.folder", billNo,"ContractTransferFeedback");
					boolean b=false;
					if(list!=null || list.size()>0){
						b=savePicterTodb2(request, list, jnlnoStr.substring(0, jnlnoStr.length()-1));
					}
					
					
				}
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
	
	/**
	 * 跳转到驳回方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareOutRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		request.setAttribute("navigator.location","合同交接资料上传 >> 驳回");
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
		
		dform.set("id",id);
		
		Session hs = null;
		Query query = null;
		try {
			hs = HibernateUtil.getSession();
			
			id = id.replace(",", "','");
			
			String hql = "from ContractTransferMaster where billNo in ('"+id.trim()+"') ";
			List list = hs.createQuery(hql).list();
			
			if (list!=null && list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					ContractTransferMaster master = (ContractTransferMaster) list.get(i);
					
					master.setCompanyId(bd.getName("Customer", "companyName", "companyId", master.getCompanyId()));
					master.setMaintDivision(bd.getName("Company", "comfullname", "comid", master.getMaintDivision()));
					master.setMaintStation(bd.getName("Storageid", "storagename", "storageid", master.getMaintStation()));
				}
				
			}
			
//			String sql = "select a.jnlno,a.FileType,b.pullname from ContractTransferFileTypes a,pulldown b " 
//				+" where a.FileType = b.pullid and b.typeflag = 'ContractFileTypes_FileType' and billNo = '"+id.trim()+"' ";
//			List fileTypesList = hs.createSQLQuery(sql).list();
//			List ContractTransferFileTypesList = new ArrayList();
//			
//			for (Object object : fileTypesList) {
//				
//				Object[] objs = (Object[])object;
//				
//				HashMap map = new HashMap();
//				map.put("jnlno", objs[0]);
//				map.put("fileType", objs[1]);
//				map.put("fileTypeName", objs[2]);
//				
//				ContractTransferFileTypesList.add(map);
//			}
			
//			request.setAttribute("fileTypes", ContractTransferFileTypesList);
			request.setAttribute("outflag", "Y");
			request.setAttribute("masterList", list);	
			request.setAttribute("billNo", id.replace("','", ","));
									
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
		
		saveToken(request); //生成令牌，防止表单重复提交
		
		return mapping.findForward("ContractTransferUploadOut");
	}
	
	/**
	 * 点击驳回的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	public ActionForward toOutRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	
		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;	
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String id = (String) dform.get("id");
		String transferRem = (String)dform.get("transferRem");
		
		Session hs = null;
		Transaction tx = null;
		
		String billNo = request.getParameter("billNo");
		
		try {
			//防止表单重复提交
			if(isTokenValid(request, true)){
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				
				String[] billnoarr = billNo.split(",");
				
				for (int i = 0; i < billnoarr.length; i++) {
					ContractTransferMaster master = (ContractTransferMaster) hs.get(ContractTransferMaster.class, billnoarr[i]);
					master.setSubmitType("R");
					master.setTransfeId(userInfo.getUserID());
					master.setTransfeDate(CommonUtil.getNowTime());
					master.setTransferRem(transferRem.trim());
					
					master.setIsTrans("N");
					master.setIsTransId("");
					master.setIsTransDate("");
					master.setWbgTransfeId("");
					
					hs.save(master);
					
					String ftHql = "from ContractTransferFileTypes where billNo = '"+billnoarr[i]+"'";
					List ftList = hs.createQuery(ftHql).list();
					
					String delHql = "";
					
					if (ftList!=null&&ftList.size()>0) {
						for (int j = 0; j < ftList.size(); j++) {
							ContractTransferFileTypes fileTypes = (ContractTransferFileTypes) ftList.get(j);
							
							delHql = "delete from ContractTransferFileinfo where jnlno = '"+fileTypes.getJnlno()+"'";
							hs.createQuery(delHql).executeUpdate();
							hs.flush();
							
//							hs.delete(fileTypes);
//							hs.flush();
						}
					}
					
					
					String fbHql = "from ContractTransferFeedback where billNo = '"+billnoarr[i]+"'";
					List fbList = hs.createQuery(fbHql).list();
					
					if (fbList!=null&&fbList.size()>0) {
						for (int j = 0; j < fbList.size(); j++) {
							
							ContractTransferFeedback feedback = (ContractTransferFeedback) fbList.get(j);
							
							delHql = "delete from ContractTransferFeedbackFileinfo where jnlno = '"+feedback.getJnlno()+"'";
							hs.createQuery(delHql).executeUpdate();
							hs.flush();
							
							hs.delete(feedback);
							hs.flush();
						}
					}
				}
				
				tx.commit();
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
	
	/**
	 * 跳转到转派方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareTransferRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		request.setAttribute("navigator.location","合同交接资料上传 >> 转派");
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
		
		dform.set("id",id);
		
		Session hs = null;
		Query query = null;
		try {
			hs = HibernateUtil.getSession();
			
			id = id.replace(",", "','");
			
			String hql = "from ContractTransferMaster where billNo in ('"+id.trim()+"') ";
			List list = hs.createQuery(hql).list();
			
			if (list!=null && list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					ContractTransferMaster master = (ContractTransferMaster) list.get(i);
					
					master.setCompanyId(bd.getName("Customer", "companyName", "companyId", master.getCompanyId()));
					master.setMaintDivision(bd.getName("Company", "comfullname", "comid", master.getMaintDivision()));
					master.setMaintStation(bd.getName("Storageid", "storagename", "storageid", master.getMaintStation()));
				}
				
			}
			
			//维保工A50,维保站长A49,维修技术员A53 
			String sql = "select UserID,UserName from Loginuser " 
			+"where StorageID like '"+userInfo.getStorageId()+"%' and EnabledFlag='Y' and RoleID in('A50','A49','A53')";
			
			List list2 = hs.createSQLQuery(sql).list();
			List wbgList = new ArrayList();
			
			if (list2!=null&&list2.size()>0) {
				for (int i = 0; i < list2.size(); i++) {
					Object[] obj = (Object[]) list2.get(i);
					
					HashMap map = new HashMap();
					map.put("wbgId", obj[0]);
					map.put("wbgName", obj[1]);
					
					wbgList.add(map);		
				}
			}
			
			request.setAttribute("wbgList", wbgList);
			request.setAttribute("tranflag", "Y");
			request.setAttribute("masterList", list);	
			request.setAttribute("billNo", id.replace("','", ","));
			request.setAttribute("isTransId", userInfo.getUserName());
			request.setAttribute("isTransDate", DateUtil.getCurDate());
									
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
		
		saveToken(request); //生成令牌，防止表单重复提交
		
		return mapping.findForward("ContractTransferUploadTransfer");
	}
	
	/**
	 * 点击转派的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	public ActionForward toTransferRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	
		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;	
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String id = (String) dform.get("id");
		String transferRem = (String)dform.get("transferRem");
		
		Session hs = null;
		Transaction tx = null;
		
		String billNo = request.getParameter("billNo");
		String wbgTransfeId = request.getParameter("wbgTransfeId");
		
		try {
			//防止表单重复提交
			if(isTokenValid(request, true)){
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				
				String[] billnoarr = billNo.split(",");
				
				for (int i = 0; i < billnoarr.length; i++) {
					ContractTransferMaster master = (ContractTransferMaster) hs.get(ContractTransferMaster.class, billnoarr[i]);
					
					master.setIsTrans("Y");
					master.setIsTransId(userInfo.getUserID());
					master.setIsTransDate(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss"));
					master.setWbgTransfeId(wbgTransfeId);
					
					hs.save(master);
					
				}
				
				tx.commit();
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
	public boolean savePicterTodb(HttpServletRequest request,List fileInfoList,String billno){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;
		boolean saveFlag = true;
		if(null != fileInfoList && fileInfoList.size()>0){
			String sql="";
			ContractTransferFileinfo ctFileinfo=null;
			try {
				hs=HibernateUtil.getSession();
				tx=hs.beginTransaction();
				int len = fileInfoList.size();
				for(int i = 0 ; i < len ; i++){
					Map map = (Map)fileInfoList.get(i);
					
					String jnlnostr = (String)map.get("jnlno");
					String[] jnlnoarr = jnlnostr.split(",");
					
					for (int j = 0; j < jnlnoarr.length; j++) {
						ctFileinfo=new ContractTransferFileinfo();
						ctFileinfo.setJnlno(jnlnoarr[j]);
						ctFileinfo.setOldFileName((String)map.get("oldfilename"));
						ctFileinfo.setNewFileName((String)map.get("newFileName"));
						ctFileinfo.setFileSize(Double.valueOf(map.get("fileSize").toString()));
						ctFileinfo.setFilePath((String)map.get("filepath"));
						ctFileinfo.setFileFormat((String)map.get("fileformat"));
						ctFileinfo.setUploadDate(CommonUtil.getNowTime());
						ctFileinfo.setUploader(userInfo.getUserID());
						hs.save(ctFileinfo);
						hs.flush();
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
	
	public boolean savePicterTodb2(HttpServletRequest request,List fileInfoList,String jnlnostr){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;
		boolean saveFlag = true;
		if(null != fileInfoList && fileInfoList.size()>0){
			String sql="";
			ContractTransferFeedbackFileinfo fbFileinfo=null;
			try {
				hs=HibernateUtil.getSession();
				tx=hs.beginTransaction();
				int len = fileInfoList.size();
				for(int i = 0 ; i < len ; i++){
					Map map = (Map)fileInfoList.get(i);
					
					String[] jnlnoarr = jnlnostr.split(",");
					
					for (int j = 0; j < jnlnoarr.length; j++) {
						fbFileinfo=new ContractTransferFeedbackFileinfo();
						fbFileinfo.setJnlno(jnlnoarr[j]);
						fbFileinfo.setOldFileName((String)map.get("oldfilename"));
						fbFileinfo.setNewFileName((String)map.get("newFileName"));
						fbFileinfo.setFileSize(Double.valueOf(map.get("fileSize").toString()));
						fbFileinfo.setFilePath((String)map.get("filepath"));
						fbFileinfo.setFileFormat((String)map.get("fileformat"));
						fbFileinfo.setUploadDate(CommonUtil.getNowTime());
						fbFileinfo.setUploader(userInfo.getUserID());
						hs.save(fbFileinfo);
						hs.flush();
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
		String path = "";
		if(folder==null || "".equals(folder)){
			folder="ContractTransferFileinfo.file.upload.folder";
		}
		filename=URLDecoder.decode(filename, "utf-8");
		filePath=URLDecoder.decode(filePath, "utf-8");
		fileOldName=URLDecoder.decode(fileOldName, "utf-8");
		if ("DebugSheetFileInfo".equals(folder)) {
			String fileid = filePath;
			boolean isstr=SendURLFile.isFileExist(fileid);
			if(isstr){
				//下载远程文件
				SendURLFile.loadFile(fileid, response, fileOldName);
			}
		}else {
			folder = PropertiesUtil.getProperty(folder);
			path = folder+"/"+filePath+filename;
			
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			OutputStream fos = null;
			InputStream fis = null;

			response.setContentType("application/x-msdownload");
			response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileOldName, "utf-8"));

			fis = new FileInputStream(path);
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
		}
		
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
			folder ="ContractTransferFileinfo.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		int zNum=0,yNum=0;
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if(id!=null && id.length()>0){
				
				ContractTransferFileinfo ctFileinfo = (ContractTransferFileinfo) hs.get(ContractTransferFileinfo.class, Integer.valueOf(id));
				String fileName=ctFileinfo.getNewFileName();
				hs.delete(ctFileinfo);
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