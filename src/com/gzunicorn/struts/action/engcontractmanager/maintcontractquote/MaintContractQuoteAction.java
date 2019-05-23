package com.gzunicorn.struts.action.engcontractmanager.maintcontractquote;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.ParseException;
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

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

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
import org.apache.struts.util.MessageResources;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
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

import com.gzunicorn.hibernate.basedata.personnelmanage.PersonnelManageMaster;
import com.gzunicorn.hibernate.engcontractmanager.ContractFileinfo.ContractFileinfo;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotedetail.MaintContractQuoteDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquoteprocess.MaintContractQuoteProcess;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.workflow.bo.JbpmExtBridge;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class MaintContractQuoteAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintContractQuoteAction.class);
	
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

		String name = request.getParameter("method");
		if(!"toDelFileRecord".equals(name) && !"toDownloadFileRecord".equals(name) ){
			/** **********开始用户权限过滤*********** */
			SysRightsUtil.filterModuleRight(request, response,
					SysRightsUtil.NODE_ID_FORWARD + "maintcontractquote", null);
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
		
		request.setAttribute("navigator.location","维保报价申请管理  >> 查询列表");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "maintContractQuoteList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fMaintContractQuote");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("billNo");
			table.setIsAscending(false);
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
			
			String billNo = tableForm.getProperty("billNo");// 报价流水号
			String companyName = tableForm.getProperty("companyName");// 甲方单位名称
			String status = tableForm.getProperty("status");// 流程状态
			String maintDivision = tableForm.getProperty("maintDivision");// 甲方单位id
			String submitType = tableForm.getProperty("submitType");// 提交标志
			String salesContractNo = tableForm.getProperty("salesContractNo");// 销售合同号
			String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号
			String iscont = tableForm.getProperty("iscont");// 生成合同
			
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
				
				String maintStation="%";
				//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
				String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
				List vmlist=hs.createSQLQuery(sqlss).list();
				if(vmlist!=null && vmlist.size()>0){
					maintStation=userInfo.getStorageId();
				}
				
				String sql = "select a.*,b.username,c.comname,e.typename,s.storagename,"
						+ "isnull((select distinct quoteBillNo from MaintContractMaster where quoteBillNo=a.BillNo),'') as quoteBillNo"+
						" from MaintContractQuoteMaster a,Loginuser b,Company c,ViewFlowStatus e,StorageID s" + 
						" where a.attn = b.userid"+
						" and a.maintDivision = c.comid"+
						" and a.maintStation = s.storageid"+
						" and a.status = e.typeid" +
						" and a.maintStation like '"+maintStation+"'";

				if (billNo != null && !billNo.equals("")) {
					sql += " and a.billNo like '%"+billNo.trim()+"%'";
				}	
				if (companyName != null && !companyName.equals("")) {
					sql += " and a.companyName like '%"+companyName.trim()+"%'";
				}				
				if (status != null && !status.equals("")) {
					sql += " and a.status = '"+Integer.valueOf(status)+"'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and c.comid like '"+maintDivision.trim()+"'";
				}	
				if (submitType != null && !submitType.equals("")) {
					sql += " and a.submitType like '"+submitType.trim()+"'";
				}

				if (salesContractNo != null && !salesContractNo.equals("")) {
					sql += " and a.billNo in(select distinct billNo from MaintContractQuoteDetail where salesContractNo like '%"+salesContractNo.trim()+"%')";
				}
				if (elevatorNo != null && !elevatorNo.equals("")) {
					sql += " and a.billNo in(select distinct billNo from MaintContractQuoteDetail where elevatorNo like '%"+elevatorNo.trim()+"%')";
				}
				if (iscont != null && !iscont.equals("")) {
					if(iscont.equals("Y")){
						sql += " and a.billNo in(select distinct isnull(m.quoteBillNo,'')  from MaintContractMaster m,MaintContractQuoteMaster mm where m.quoteBillNo=mm.BillNo)";
					}else{
						sql += " and a.billNo not in(select distinct isnull(m.quoteBillNo,'')  from MaintContractMaster m,MaintContractQuoteMaster mm where m.quoteBillNo=mm.BillNo)";
						
					}
				}
				
				if (table.getIsAscending()) {
					sql += " order by "+ table.getSortColumn() +" asc";
				} else {
					sql += " order by "+ table.getSortColumn() +" desc";
				}
				
				//System.out.println(sql);
				
				query = hs.createSQLQuery(sql);					
				query.addEntity("a",MaintContractQuoteMaster.class);
				query.addScalar("username");
				query.addScalar("comname");
				query.addScalar("typename");
				query.addScalar("storagename");
				query.addScalar("quoteBillNo");
				
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List maintContractQuoteList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map map = BeanUtils.describe(objs[5]); // MaintContractQuoteMaster
					map.put("attnName", String.valueOf(objs[0])); // 经办人名称
					map.put("maintDivisionName", String.valueOf(objs[1])); // 分部名称
					map.put("statusName", String.valueOf(objs[2])); // 流程状态
					map.put("storagename", String.valueOf(objs[3])); // 维保站名称
					
					String quoteBillNo=String.valueOf(objs[4]);
					if(quoteBillNo!=null && !quoteBillNo.trim().equals("")){
						map.put("iscontract", "是");
					}else{
						map.put("iscontract", "否");
					}
					/**
					String billno=(String)map.get("billNo");
					String sqlmas="select quoteBillNo from MaintContractMaster where quoteBillNo='"+billno.trim()+"'";
					List ecmlist=hs.createSQLQuery(sqlmas).list();
					if(ecmlist!=null && ecmlist.size()>0){
						map.put("iscontract", "是");
					}else{
						map.put("iscontract", "否");
					}
					*/
					maintContractQuoteList.add(map);
				}

				table.addAll(maintContractQuoteList);
				session.setAttribute("maintContractQuoteList", table);
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);
				// 流程状态下拉框列表
				request.setAttribute("processStatusList", bd.getProcessStatusList());

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
			forward = mapping.findForward("maintContractQuoteList");
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
		
		request.setAttribute("navigator.location","维保报价申请管理  >> 查看");
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;
		
		String id = request.getParameter("id");
		
		Session hs = null;
		List maintContractQuoteDetailList = null;

		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from MaintContractQuoteMaster where billNo = '"+id.trim()+"'");
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
					query = hs.createQuery("from MaintContractQuoteDetail where billNo = '"+id+"'");
					List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型列表
					maintContractQuoteDetailList = query.list();
					for (Object object : maintContractQuoteDetailList) {
						MaintContractQuoteDetail detail = (MaintContractQuoteDetail)object;
						detail.setElevatorType(bd.getOptionName(detail.getElevatorType(), elevatorTypeList));
						detail.setR4(bd.getName("Pulldown", "pullname", "id.pullid",detail.getSignWay()));//签署方式
					}
					request.setAttribute("maintContractQuoteDetailList", maintContractQuoteDetailList);
					
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
				
				//获取计算标准报价的相关系数
				request.setAttribute("returnhmap", bd.getCoefficientMap());
				
			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			
			//lijun add 首页转派跳转查看后，不需要再次出现在首页
			String workisdisplay=request.getParameter("workisdisplay");
			if(workisdisplay!=null && workisdisplay.equals("Y")){
				request.setAttribute("workisdisplay", workisdisplay);
				Session hs2 = null;
				Transaction tx2 = null;
				try {
					hs2 = HibernateUtil.getSession();
					tx2 = hs2.beginTransaction();
					
					String upsql="update MaintContractQuoteMaster set workisdisplay='"+workisdisplay+"' where billNo='"+id+"'";
					hs2.connection().prepareStatement(upsql).executeUpdate();
					
					tx2.commit();
				} catch (Exception e1) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
					try {
						tx2.rollback();
					} catch (HibernateException e2) {
						log.error(e2.getMessage());
						DebugUtil.print(e2, "Hibernate Transaction rollback error!");
					}
					e1.printStackTrace();
					log.error(e1.getMessage());
					DebugUtil.print(e1, "Hibernate region Insert error!");
				} finally {
					try {
						hs2.close();
					} catch (HibernateException hex) {
						log.error(hex.getMessage());
						DebugUtil.print(hex, "Hibernate close error!");
					}
				}
			}

			request.setAttribute("display", "yes");
			forward = mapping.findForward("maintContractQuoteDisplay");
		}		
		
		
				
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
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

		request.setAttribute("navigator.location","维保报价申请管理  >> 添加");
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) { 
			dform.initialize(mapping);
		}
		MaintContractQuoteMaster maintContractQuoteBean = new MaintContractQuoteMaster();
		try {
			maintContractQuoteBean.setAttn(userInfo.getUserID()); //经办人代码
			maintContractQuoteBean.setApplyDate(CommonUtil.getNowTime()); //申请时间
			maintContractQuoteBean.setMaintDivision(userInfo.getComID()); //维保分部代码
			maintContractQuoteBean.setQuoteSignWay("ZB"); //报价签署方式 新签
			
			//A03  维保经理  只能看自己维保站下面的合同
			String roleid=userInfo.getRoleID();
			if(roleid!=null && roleid.trim().equals("A03")){
				maintContractQuoteBean.setR2(userInfo.getStorageName());
				maintContractQuoteBean.setMaintStation(userInfo.getStorageId());
				maintContractQuoteBean.setR1(roleid);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("PaymentMethodList", bd.getPullDownList("MaintContractQuoteMaster_PaymentMethodApply"));
		request.setAttribute("ContractContentList", bd.getPullDownList("MaintContractQuoteMaster_ContractContentApply"));
		request.setAttribute("attnName", userInfo.getUserName()); //经办人名称
		request.setAttribute("maintDivisionName", userInfo.getComName()); //维保分部名称
		request.setAttribute("maintContractQuoteBean", maintContractQuoteBean);
		
		request.setAttribute("maintStationList", bd.getMaintStationList(userInfo.getComID()));// 所属维保站列表
		
		//获取计算标准报价的相关系数
		request.setAttribute("returnhmap", bd.getCoefficientMap());
		
		saveToken(request); //生成令牌，防止表单重复提交
		
		return mapping.findForward("maintContractQuoteAdd");
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
		
		//防止表单重复提交
		if(isTokenValid(request, true)){
			addOrUpdate(form,request,response,errors);// 新增或修改记录			
		}else{
			saveToken(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("navigator.submit.error"));
		}

		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
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
	 * 跳转到修改页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","维保报价申请管理  >> 修改");	
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

		String id = (String) dform.get("id");

		Session hs = null;
		List maintContractQuoteDetailList = null;
		if (id != null && !id.equals("")) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from MaintContractQuoteMaster where billNo = :billNo");
				query.setString("billNo", id);
				List list = query.list();
				if (list != null && list.size() > 0) {
					
					//主信息
					MaintContractQuoteMaster maintContractQuoteBean = (MaintContractQuoteMaster) list.get(0);
					request.setAttribute("attnName", userInfo.getUserName()); //经办人名称
					request.setAttribute("maintDivisionName", userInfo.getComName()); //维保分部名称
					maintContractQuoteBean.setApplyDate(CommonUtil.getNowTime()); //申请日期
					maintContractQuoteBean.setAttn(userInfo.getUserID());//经办人
					
					//A03  维保经理  只能看自己维保站下面的合同
					String roleid=userInfo.getRoleID();
					if(roleid!=null && roleid.trim().equals("A03")){
						maintContractQuoteBean.setR2(bd.getName(hs, "Storageid","storageName", "storageID", maintContractQuoteBean.getMaintStation()));
						maintContractQuoteBean.setR1(roleid);
					}
					
					request.setAttribute("maintContractQuoteBean", maintContractQuoteBean);	
					
					// 电梯信息明细列表
					query = hs.createQuery("from MaintContractQuoteDetail where billNo = '"+id+"'");
					List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型列表
					maintContractQuoteDetailList = query.list();
					for (Object object : maintContractQuoteDetailList) {
						MaintContractQuoteDetail detail = (MaintContractQuoteDetail)object;
						detail.setElevatorType(bd.getOptionName(detail.getElevatorType(), elevatorTypeList));
						detail.setR4(bd.getName("Pulldown", "pullname", "id.pullid",detail.getSignWay()));//签署方式
					}					
					request.setAttribute("maintContractQuoteDetailList", maintContractQuoteDetailList);
						
					// 所属维保站列表	
					request.setAttribute("maintStationList", bd.getMaintStationList(userInfo.getComID()));						
				} else {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}

				//获取已经上传的附件
				List filelst=this.FileinfoList(hs, id.trim(), "MaintContractQuoteMaster");
				request.setAttribute("updatefileList", filelst);
				
				request.setAttribute("PaymentMethodList", bd.getPullDownList("MaintContractQuoteMaster_PaymentMethodApply"));
				request.setAttribute("ContractContentList", bd.getPullDownList("MaintContractQuoteMaster_ContractContentApply"));
				
				//获取计算标准报价的相关系数
				request.setAttribute("returnhmap", bd.getCoefficientMap());
				
			}catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			forward = mapping.findForward("maintContractQuoteModify");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	/**
	 * 点击修改的方法
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

		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;
		
		addOrUpdate(form,request,response,errors);// 新增或修改记录
		
		String isreturn = request.getParameter("isreturn");		
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				} else {
					request.setAttribute("error", "Yes");
				}

				forward = mapping.findForward("returnModify");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	public ActionForward toReferRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, HibernateException {
		
		ActionErrors errors = new ActionErrors();
		
		String id = request.getParameter("id"); 
		
		refer(form,request,errors,id); //提交

		if (!errors.isEmpty()){
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.submitToAudit.failed")); //提示“提交审核失败！”
		} else {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.submitToAudit.succeed")); //提示“提交审核成功！”
		}
		
		if (!errors.isEmpty()){
			this.saveErrors(request, errors);
		}

		return mapping.findForward("returnList");

	}

	/**
	 * 删除紧急级别
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
			MaintContractQuoteMaster master = (MaintContractQuoteMaster) hs.get(MaintContractQuoteMaster.class, id);
			if (master != null) {
				hs.createQuery("delete from MaintContractQuoteDetail where billno='"+id+"'").executeUpdate();
				hs.createQuery("delete from MaintContractQuoteProcess where billno='"+id+"'").executeUpdate();
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

		String naigation = new String();
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		String name = tableForm.getProperty("name");// 姓名
		String contractNo = tableForm.getProperty("contractNo");// 合同号
		String startDates = tableForm.getProperty("startDates");// 起始入厂日期
		String startDatee = tableForm.getProperty("startDatee");// 结束入厂日期
		String enabledFlag = tableForm.getProperty("enabledFlag");// 启用标志
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(PersonnelManageMaster.class);
			if (contractNo != null && !contractNo.equals("")) {
				criteria.add(Restrictions.like("contractNo", "%" + contractNo.trim() + "%"));
			}
			if (name != null && !name.equals("")) {
				criteria.add(Restrictions.like("name", "%" + name.trim() + "%"));
			}
			if (startDates != null && !startDates.equals("")) {
				criteria.add(Restrictions.ge("startDate", startDates.trim()));
			}
			if (startDatee != null && !startDatee.equals("")) {
				criteria.add(Restrictions.le("startDate", startDatee.trim()));
			}
			if (enabledFlag != null && !enabledFlag.equals("")) {
				criteria.add(Restrictions.eq("enabledFlag", enabledFlag));
			}

			criteria.addOrder(Order.asc("billno"));

			List roleList = criteria.list();

			XSSFSheet sheet = wb.createSheet("维保报价申请表信息");
			
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);
			
			String[] columnNames = { "billno", "name", "education", "idCardNo",
					"familyAddress", "phone", "contractProperties",
					"contractNo", "startDate", "endDate", "years", "sector",
					"workAddress", "jobTitle", "level", "enabledFlag", "rem",
					"operId", "operDate" };

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
								
				for (int i = 0; i < columnNames.length; i++) {
					XSSFCell cell0 = row0.createCell((short)i);
					cell0.setCellValue(messages.getMessage(locale,"personnelManage." + columnNames[i]));
				}
				
				Class<?> superClazz = PersonnelManageMaster.class.getSuperclass();
				for (int i = 0; i < l; i++) {
					PersonnelManageMaster master = (PersonnelManageMaster) roleList.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);					
					for (int j = 0; j < columnNames.length; j++) {
						// 创建Excel列
						XSSFCell cell = row.createCell((short)j);
						cell.setCellValue(columnNames[j]);						
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("维保报价申请表信息", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}	
	
	/**
	 * 保存数据方法
	 * @param form
	 * @param request
	 * @return ActionErrors
	 */
	public void addOrUpdate(ActionForm form, HttpServletRequest request,HttpServletResponse response,ActionErrors errors) {
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		MaintContractQuoteMaster master = null;
		MaintContractQuoteDetail detail = null;
		String id = (String) dform.get("id");
		String billNo = null;
		Session hs = null;
		Transaction tx = null;
		try {
		hs = HibernateUtil.getSession();
		tx = hs.beginTransaction();
		
		if (id != null && !id.equals("")) { // 修改		
			hs.createQuery("delete from MaintContractQuoteDetail where billNo='"+id+"'").executeUpdate();		
			master = (MaintContractQuoteMaster) hs.get(MaintContractQuoteMaster.class, id);
			billNo = master.getBillNo();
			
		}else{ // 新增
			master = new MaintContractQuoteMaster();
			String todayDate = CommonUtil.getToday();
			String year = todayDate.substring(2,4);
			billNo = CommonUtil.getBillno(year,"MaintContractQuoteMaster", 1)[0];// 生成流水号		
			
		}
	
		if(!"Y".equals(master.getSubmitType())){
			
			String ccastr="";
			String[] ccarr=request.getParameterValues("contractContentApply");
			if(ccarr!=null && ccarr.length>0){
				ccastr=Arrays.toString(ccarr);
				ccastr=ccastr.replaceAll("\\[", "");
				ccastr=ccastr.replaceAll("\\]", "");
			}
			
			BeanUtils.copyProperties(master, dform); // 复制所有属性值
			master.setBillNo(billNo);// 流水号
			master.setSubmitType("N");// 提交标志
			master.setStatus(new Integer(WorkFlowConfig.State_NoStart));// 流程状态
			master.setOperId(userInfo.getUserID());// 录入人
			master.setOperDate(CommonUtil.getNowTime());// 录入时间
			master.setContractContentApply(ccastr);
			
			hs.save(master);
			String ccapply=master.getContractContentApply();//合同包含配件及服务
			
			// 合同明细
			String details = (String) dform.get("maintContractQuoteDetails");
			List list = JSONUtil.jsonToList(details, "details");
			List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型列表
			
			for (Object object : list) {
				detail = new MaintContractQuoteDetail();					
				BeanUtils.copyProperties(detail, object);
				detail.setBillNo(billNo);
				detail.setNum(1);
				detail.setElevatorType(bd.getOptionId(detail.getElevatorType(), elevatorTypeList));//把电梯类型名称转换为代码	
				
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
				String isreturn = request.getParameter("isreturn");
				if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
					refer(form,request,errors,master.getBillNo()); //提交
				}
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}
		
	}
		
	public void refer(ActionForm form, HttpServletRequest request,ActionErrors errors,String id){
		
		HttpSession httpsession = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)httpsession.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		if(id != null && !id.equals("")){
			
			Session hs = null;
			Transaction tx = null;
			JbpmExtBridge jbpmExtBridge=null;
			String userid = userInfo.getUserID(); //当前登录用户id
			MaintContractQuoteMaster master = null;				
			
			try {
				hs = HibernateUtil.getSession();

				master = (MaintContractQuoteMaster) hs.get(MaintContractQuoteMaster.class, id);
				
				if(!"Y".equals(master.getSubmitType())){
					tx = hs.beginTransaction();
					
					String processDefineID = Grcnamelist1.getProcessDefineID("enginequotemaster", master.getAttn());// 流程环节id
					if(processDefineID == null || processDefineID.equals("")){
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("error.string","<font color='red'>未配置审批流程信息，不能启动！</font>"));
						throw new Exception();
					}
								
					/**=============== 启动新流程实例开始 ===================**/
					HashMap paraMap = new HashMap();
					jbpmExtBridge=new JbpmExtBridge();
					ProcessBean pd = null;		
					pd = jbpmExtBridge.getPb();

			    	pd.addAppointActors("");// 将动态添加的审核人清除。
			    	//Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "维保分部长审核", master.getOperId());// 添加审核人
					//Grcnamelist1.setJbpmAuditopers_class(pd, processDefineID, PropertiesUtil.getProperty("MaintStationManagerJbpm"), userInfo.getComID(), master.getMaintStation());// 添加审核人
			    	Grcnamelist1.setJbpmAuditopers_roleid(pd,"Y",master.getMaintDivision());
			    	
					pd=jbpmExtBridge.startProcess(WorkFlowConfig.getProcessDefine(processDefineID),userid,userid,id,"",paraMap);
					/**==================== 流程结束 =======================**/
					
					master.setSubmitType("Y");// 提交标志
					master.setProcessName(pd.getNodename());// 环节名称
					master.setStatus(pd.getStatus()); //流程状态
					master.setTokenId(pd.getToken());//流程令牌
					hs.save(master);
					
					tx.commit();
				}
				
			} catch (Exception e) {				
				try {
					tx.rollback();
				} catch (HibernateException e3) {
					e3.printStackTrace();
				}
				if (jbpmExtBridge != null) {
					jbpmExtBridge.setRollBack();
				}
				e.printStackTrace();
			} finally {
				hs.close();
				if(jbpmExtBridge!=null){
					jbpmExtBridge.close();
				}
				
			}
			
		}
		
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
	 * 删除文件
	 */
	public ActionForward toDelFileRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		Session hs = null;
		Transaction tx = null;
		String id = request.getParameter("filesid");
		String delsql="";
		List list=null;
		
		String folder = PropertiesUtil.getProperty("MaintContractQuoteMaster.file.upload.folder");
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if(id!=null && id.length()>0){
				
				String sql="select a from ContractFileinfo a where a.fileSid='"+id.trim()+"'";
				list=hs.createQuery(sql).list();	//取文件信息
				hs.flush();
				
				delsql="delete from ContractFileinfo where fileSid='"+id.trim()+"'";
				hs.connection().prepareStatement(delsql).execute();
				hs.flush();
				
				HandleFile hf = new HandleFileImpA();
				if(list!=null && !list.isEmpty()){
					ContractFileinfo fp=(ContractFileinfo)list.get(0);
				
					String newfilename=fp.getNewFileName();
					String filepath=fp.getFilePath();
					String localPath = folder+filepath+newfilename;
					hf.delFile(localPath);//删除磁盘中的文件
				}
			}
			
	        response.setContentType("text/xml; charset=UTF-8");
	      
			//创建输出流对象
	        PrintWriter out = response.getWriter();
	        //依据验证结果输出不同的数据信息	       
	        out.println("<response>");  
	        int b=list.size();
			if(b==0){
				out.println("<res>" + "N" + "</res>");
			}else{
				out.println("<res>" + "Y" + "</res>");
			}
			 out.println("</response>");
		     out.close();
		     	
		     tx.commit();
		} catch (Exception e) {
			try {
				tx.rollback();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
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
	 * 下载文件
	 */
	public ActionForward toDownloadFileRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		/*
		 * 取得文件:文件id+文件路径+文件名+流 文件id=通过formbean取得 文件路径=通过取得配置文件的方法得到
		 * 文件名称=通过数据库得到 流=io
		 */
		Session hs = null;

		String filesid =request.getParameter("filesid");//流水号

		String localPath="";
		String oldname="";
		
		String folder = PropertiesUtil.getProperty("MaintContractQuoteMaster.file.upload.folder");
		
		try {
			hs = HibernateUtil.getSession();
			String sqlfile="select a from ContractFileinfo a where a.fileSid='"+filesid+"'";
			List list2=hs.createQuery(sqlfile).list();

			if(list2!=null && list2.size()>0){
				ContractFileinfo fp=(ContractFileinfo)list2.get(0);
				
				String filepath=fp.getFilePath();
				String newnamefile=fp.getNewFileName();
				oldname=fp.getOldFileName();
				String root=folder;//上传目录
				localPath = root+filepath+newnamefile;
				
			}
		
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			OutputStream fos = null;
			InputStream fis = null;
	
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(oldname, "utf-8"));
	
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
		}catch (Exception e) {
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
		
	
}
