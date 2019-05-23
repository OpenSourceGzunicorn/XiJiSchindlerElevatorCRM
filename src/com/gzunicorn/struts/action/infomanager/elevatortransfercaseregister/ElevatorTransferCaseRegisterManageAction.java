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
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

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
import com.gzunicorn.hibernate.contractpaymentmanage.contractticketmanage.ContractTicketManage;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;
import com.gzunicorn.hibernate.infomanager.handoverelevatorcheckitemregister.HandoverElevatorCheckItemRegister;
import com.gzunicorn.hibernate.infomanager.handoverelevatorspecialregister.HandoverElevatorSpecialRegister;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.workflow.bo.JbpmExtBridge;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class ElevatorTransferCaseRegisterManageAction extends DispatchAction {

	Log log = LogFactory.getLog(ElevatorTransferCaseRegisterManageAction.class);
	
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
		if(!"toDownloadFileRecord1".equals(name)&&!"toPreparePrintRecord".equals(name)){
			/** **********开始用户权限过滤*********** */
			SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "elevatortransfercaseregistermanage", null);
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
		
		request.setAttribute("navigator.location","安装维保交接电梯情况管理 >> 查询列表");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "elevatorTransferCaseRegisterList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fElevatorTransferCaseRegister");
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
			}else {
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
			String isDeductions=tableForm.getProperty("isDeductions");
			String status = tableForm.getProperty("status");// 流程状态
			String isClose = tableForm.getProperty("isClose");
			
			Session hs = null;
			Query query = null;
			 List departmentList=new ArrayList();
			 ElevatorTransferCaseRegister etfcm=null;
			try {

				hs = HibernateUtil.getSession();

				String sql = "select etcr,lu.username"+
				" from ElevatorTransferCaseRegister etcr,Loginuser lu"+
				" where etcr.staffName=lu.userid";
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
				}
				if(isDeductions!=null && !isDeductions.equals("")){
					sql+=" and etcr.isDeductions like '%"+isDeductions.trim()+"%'";
				}
				if (status != null && !status.equals("")) {
					sql+=" and etcr.status = "+status;
				}
				if (isClose != null && !isClose.equals("")) {
					sql+=" and isnull(etcr.isClose,'N') = '"+isClose+"'";
				}
				if (table.getIsAscending()) {
					sql += " order by "+ table.getSortColumn() +" desc";
				} else {
					sql += " order by "+ table.getSortColumn() +" asc";
				}
				
				//System.out.println(">>>>>>"+sql);
				
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
					//etfcm.setR1();
					etfcm.setR2(bd.getName_Sql("ViewFlowStatus", "typename", "typeid", String.valueOf(etfcm.getStatus())));

					String pstaturs=etfcm.getProcessStatus();
					if(pstaturs!=null && pstaturs.trim().equals("1")){
						//如果处理状态为 1 已登记未提交 ，就不显示厂检结果
						etfcm.setFactoryCheckResult("");
					}
					
					elevatorTransferCaseRegisterList.add(etfcm);
				}
				table.addAll(elevatorTransferCaseRegisterList);
				session.setAttribute("elevatorTransferCaseRegisterList", table);
				String sql1="from Company where (comtype='1' or comtype='2') and enabledflag='Y' order by comid desc";				
				List list1=hs.createQuery(sql1).list();					    
				request.setAttribute("departmentList", list1);
				
				// 流程状态下拉框列表
				request.setAttribute("processStatusList", bd.getProcessStatusList());
			
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
			forward = mapping.findForward("elevatorTransferCaseRegisterList");
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
		
		request.setAttribute("navigator.location","安装维保交接电梯情况管理 >> 查看");
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
		forward = mapping.findForward("elevatorTransferCaseRegisterDisplay");
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

		request.setAttribute("navigator.location", "安装维保交接电梯情况管理>> 添加");
		DynaActionForm dform = (DynaActionForm) form;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ElevatorTransferCaseRegister etcr=new ElevatorTransferCaseRegister();
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			////System.out.println(mapping);
			dform.initialize(mapping);
			// dform.set("enabledFlag", "Y");
			etcr.setCheckNum(1);
			etcr.setCheckTime(CommonUtil.getNowTime());
		}
		
		
		Session hs = null;
		Transaction tx = null;
		List list=null;
		JSONArray jsonArray=new JSONArray();
		try {
			hs = HibernateUtil.getSession();
			String sql="from Company where (comtype='1' or comtype='2') and enabledflag='Y' order by comid desc";
			list=hs.createQuery(sql).list();			
			String hql="from Loginuser l where l.roleid='A51'";
			List list2=hs.createQuery(hql).list();
			if(list2!=null&&list2.size()>0){
				for(int i=0;i<list2.size();i++){
					Loginuser l=(Loginuser) list2.get(i);
					JSONObject object=new JSONObject("{\"username\":\""+l.getUsername().trim()+"\",\"userid\":\""+l.getUserid()+"\"}");
					jsonArray.put(object);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();		
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}
		
		
		etcr.setCheckNum(1);
		etcr.setCheckTime(CommonUtil.getNowTime());
		request.setAttribute("operName", userInfo.getUserName());
		request.setAttribute("operDate", CommonUtil.getNowTime());
		request.setAttribute("departmentList", list);
		request.setAttribute("userList", jsonArray);
		request.setAttribute("elevatorTransferCaseRegisterBean", etcr);
		request.setAttribute("returnListMethod", "toSearchRecord");
		return mapping.findForward("elevatorTransferCaseRegisterAdd");
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
		ElevatorTransferCaseRegister register = null;
		//HandoverElevatorCheckItemRegister hecir = null;
		//String id = request.getParameter("id");
		String isxjs=(String) dform.get("isxjs");
		String submitType=(String)dform.get("submitType");
		String specialClaim=request.getParameter("specialClaim");
		String[] scId=request.getParameterValues("scId");
		String[] scjnlno=request.getParameterValues("scjnlno");
		String historyBillNo=request.getParameter("historyBillNo");
		String firstInstallation=(String) dform.get("firstInstallation");
		
		String billNo = null;

		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			String hecis = (String) dform.get("hecirs");
			//System.out.println(hecis);
			String ElevatorNostr="";
			if(hecis!=null && !"".equals(hecis)){
				List elevatorList=JSONUtil.jsonToList(hecis, "hecis");
				for(int i=0;i<elevatorList.size();i++){

					register = new ElevatorTransferCaseRegister();	
					BeanUtils.populate(register, dform.getMap()); // 复制所有属性值
					BeanUtils.populate(register, (Map)elevatorList.get(i));
					String elenostr=register.getElevatorNo();
					
					//判断是否存在重复的电梯编号，存在就不进行保存。
					if(ElevatorNostr.indexOf(elenostr+",")<0){
						System.out.println(">>>"+i+"; "+elenostr);

						ElevatorNostr+=elenostr+",";
						
						String todayDate = CommonUtil.getToday();
						String year = todayDate.substring(2,4);
						billNo = CommonUtil.getBillno(year,"ElevatorTransferCaseRegister", 1)[0];// 生成流水号
						register.setCheckVersion("Y");
						if(historyBillNo!=null&&!"".equals(historyBillNo)){
							register.setHistoryBillNo(historyBillNo);
						}
						
						register.setFirstInstallation(firstInstallation);//是否初次安装
						register.setIsxjs(isxjs);//是否迅达安装
						register.setBillno(billNo);// 流水号
						register.setOperId(userInfo.getUserID());// 录入人
						register.setOperDate(CommonUtil.getNowTime());// 录入时间
						register.setSubmitType(submitType);// 提交标志
						register.setProcessStatus("0");// 处理状态
						register.setFactoryCheckResult("");
						register.setSpecialClaim(specialClaim);
						register.setIsClose("N");
						register.setAuditor("");
						register.setAopinion("");
						register.setBopinion("");
						register.setCopinion("");
						register.setDopinion("");
						register.setProcessResult("");
						register.setStatus(-1);
						register.setProcessName("");
						register.setTokenId(0L);		
						hs.save(register);
						//hs.flush();
						
						String deletescid="";
						if(scId!=null && scId.length>0){
							if("Y".equals("")){//再次厂检
								String[] isOk=request.getParameterValues("isOk");
								if(isOk!=null&&isOk.length>0){
									for(int j=0 ; j<isOk.length;j++){
										String[] scjnlno1 = CommonUtil.getBillno(CommonUtil.getToday().substring(2,4),"HandoverElevatorSpecialRegister", 1);	
										HandoverElevatorSpecialRegister hesr=new HandoverElevatorSpecialRegister();
										hesr.setJnlno(scjnlno1[0]);
										hesr.setScId(scId[j]);
										hesr.setElevatorTransferCaseRegister(register);
										hesr.setIsOk(isOk[j]);
								        hs.save(hesr);
								        //hs.flush();
									}
								}
							}else{
								for(int j=0;j<scId.length;j++){
									if(scId[j]!=null&&!"".equals(scId))
									deletescid+=i<scId.length-1 ?scId[j]+"','" : scId[j];
								}
							}
							if(!deletescid.equals("")){
								String deleteScId="delete from HandoverElevatorSpecialRegister r where r.elevatorTransferCaseRegister.billno ='"+billNo+"' and r.scId not in ('"+deletescid+"')";	
								hs.createQuery(deleteScId).executeUpdate();
								//hs.flush();
							}
							if(!"Y".equals("")){
							    if(scId!=null && scjnlno.length==scId.length){
								    for(int j=0;j<scId.length;j++){
										HandoverElevatorSpecialRegister hesr=null;
										if(scjnlno[j]==null||scjnlno[j].equals("")){
											String[] scjnlno1 = CommonUtil.getBillno(CommonUtil.getToday().substring(2,4),"HandoverElevatorSpecialRegister", 1);	
											hesr=new HandoverElevatorSpecialRegister();
											hesr.setJnlno(scjnlno1[0]);
											hesr.setScId(scId[j]);
											hesr.setElevatorTransferCaseRegister(register);
											hesr.setIsOk("N");
											hs.save(hesr);
											//hs.flush();
										}
									 }
							    }
							}
						}else{
							String deleteScId="delete from HandoverElevatorSpecialRegister r where r.elevatorTransferCaseRegister.billno ='"+billNo+"'";	
							hs.createQuery(deleteScId).executeUpdate();
							//hs.flush();
						}
					}//end if
				}//end for
			}//end if
			
			dform.set("id", billNo);
			request.setAttribute("id", billNo);
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
//		addOrUpdate(form,request,errors,"");// 新增或修改记录

		String isreturn = request.getParameter("isreturn");
		String nextId = request.getParameter("nextId");
		try {
			
			if(nextId!=null&& !nextId.equals("")){
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
				forward = mapping.findForward("returnCopy");
			}else{
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
		
		request.setAttribute("navigator.location","安装维保交接电梯情况管理 >> 修改");	
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		display(form, request, errors, "update");
		request.setAttribute("operName", userInfo.getUserName());
		request.setAttribute("operDate", CommonUtil.getNowTime());
		request.setAttribute("update", "Y");
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("returnListMethod", "toSearchRecord");
		forward = mapping.findForward("elevatorTransferCaseRegisterModify");
		
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
		
		addOrUpdate(form,request,errors,"");// 新增或修改记录
		
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
	
	/**
	 * 提交方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws HibernateException
	 */
	public ActionForward toReferRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, HibernateException {
		
		ActionErrors errors = new ActionErrors();
		refer(form,request,errors,request.getParameter("id")); //提交

		if (!errors.isEmpty()){
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.submit.succeed")); //提示“提交审核失败！”
		} else {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.submit.succeed")); //提示“提交审核成功！”
		}
		
		if (!errors.isEmpty()){
			this.saveErrors(request, errors);
		}

		return mapping.findForward("returnList");

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
				hs.createQuery("delete from HandoverElevatorSpecialRegister r where r.elevatorTransferCaseRegister.billno='"+id+"'").executeUpdate();
				if(master.getHistoryBillNo()!=null&&!"".equals(master.getHistoryBillNo())){
					hs.createQuery("update ElevatorTransferCaseRegister set checkVersion='Y' where billno='"+master.getHistoryBillNo().trim()+"'").executeUpdate();
				}
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
		JSONArray jsonArray=new JSONArray();
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
					
					}else if("update".equals(flag))//修改
					{
						register.setR2(bd.getOptionName(register.getBhType(), bhTypeList));
						String sql1="from Company where (comtype='1' or comtype='2') and enabledflag='Y' order by comid desc";				
					    List list1=hs.createQuery(sql1).list();
					    request.setAttribute("departmentList", list1);
					    register.setR1(register.getFloor()+"/"+register.getStage()+"/"+register.getDoor());
					    request.setAttribute("username", bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
						dform.set("isxjs",register.getIsxjs());
						dform.set("id",id);
						request.setAttribute("elevatorTransferCaseRegisterBean", register);	
						String hql="from Loginuser l where l.roleid='A51'";
						
						List list2=hs.createQuery(hql).list();
						if(list2!=null&&list2.size()>0){
							for(int i=0;i<list2.size();i++){
								Loginuser l=(Loginuser) list2.get(i);
								JSONObject object=new JSONObject("{\"username\":\""+l.getUsername().trim()+"\",\"userid\":\""+l.getUserid()+"\"}");
								jsonArray.put(object);
							}
						}
						request.setAttribute("userList", jsonArray);
					}else if("print".equals(flag))//打印
					{
						register.setDepartment(bd.getName_Sql("Company", "comname", "comid", register.getDepartment()));
						register.setStaffName(bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
						register.setR1(register.getFloor()+"/"+register.getStage()+"/"+register.getDoor());
						dform.set("id",id);
						request.setAttribute("elevatorTransferCaseRegisterBean", register);
					}
					else
					{
						dform.set("id",null);
						dform.set("nextId",id);
						ElevatorTransferCaseRegister etfcr=new ElevatorTransferCaseRegister();
						etfcr.setDepartment(register.getDepartment());
						etfcr.setStaffName(register.getStaffName());
						etfcr.setIsxjs(register.getIsxjs());
						etfcr.setInsCompanyName(register.getInsCompanyName());
						etfcr.setInsLinkPhone(register.getInsLinkPhone());
						etfcr.setStaffLinkPhone(register.getStaffLinkPhone());
						etfcr.setCheckNum(1);
						request.setAttribute("departname", bd.getName_Sql("Company", "comname", "comid", register.getDepartment()));
						request.setAttribute("username", bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
						request.setAttribute("elevatorTransferCaseRegisterBean", etfcr);						
					}
					//甲方问题
					List hecirList=new ArrayList();
					List hecirDeketeList = new ArrayList();
					
					String sql = "select b.examType examType1,p.pullname examType,b.checkItem checkItem,"
							+ "b.issueCoding issueCoding,b.issueContents IssueContents1,b.rem ,"
							+ "b.isRecheck isRecheck,b.isDelete isDelete,b.jnlno jnlno,b.deleteRem,b.isyzg "
							+ "from HandoverElevatorCheckItemRegister b ,pulldown p "
							+ " where p.pullid=b.examType "
							+ " and p.typeflag='HandoverElevatorCheckItem_ExamType' "
							+ " and b.billno='"+id+"' order by b.isRecheck desc,p.orderby,b.issueCoding ";	
					
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
	 * 保存数据方法
	 * @param form
	 * @param request
	 * @return ActionErrors
	 */
	public void addOrUpdate(ActionForm form, HttpServletRequest request,ActionErrors errors,String version) {
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ElevatorTransferCaseRegister register = null;
		String id = request.getParameter("id");
		String isxjs=(String) dform.get("isxjs");
		String submitType=(String)dform.get("submitType");
		String specialClaim=request.getParameter("specialClaim");
		String[] scId=request.getParameterValues("scId");
		String[] scjnlno=request.getParameterValues("scjnlno");
		String historyBillNo=request.getParameter("historyBillNo");
		String firstInstallation=(String) dform.get("firstInstallation");
		
		String billNo = null;

		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			
			if (id != null && !id.equals("")) { // 修改
				//hs.createQuery("delete from HandoverElevatorCheckItemRegister where billNo='"+id+"'").executeUpdate();		
				register = (ElevatorTransferCaseRegister) hs.get(ElevatorTransferCaseRegister.class, id);
				billNo = register.getBillno();
				register.setCheckVersion("Y");
			} else { // 新增
				register = new ElevatorTransferCaseRegister();	
				String todayDate = CommonUtil.getToday();
				String year = todayDate.substring(2,4);
				billNo = CommonUtil.getBillno(year,"ElevatorTransferCaseRegister", 1)[0];// 生成流水号
				register.setCheckVersion("Y");
			}

			if(historyBillNo!=null && !"".equals(historyBillNo)){
				register.setHistoryBillNo(historyBillNo);
			}
			BeanUtils.populate(register, dform.getMap()); // 复制所有属性值
			register.setFirstInstallation(firstInstallation);//是否初次安装
			register.setIsxjs(isxjs);//是否迅达安装
			register.setBillno(billNo);// 流水号
			register.setOperId(userInfo.getUserID());// 录入人
			register.setOperDate(CommonUtil.getNowTime());// 录入时间
			register.setSubmitType(submitType);// 提交标志
			register.setProcessStatus("0");// 处理状态
			register.setFactoryCheckResult("");
			register.setFactoryCheckResult2("");
			if("Y".equals(version)){
				hs.createQuery("update ElevatorTransferCaseRegister set checkVersion='N' where elevatorNo='"+register.getElevatorNo()+"'").executeUpdate();
				//register.setCheckVersion("Y");
			}
			register.setSpecialClaim(specialClaim);
			register.setIsClose("N");
			register.setAuditor("");
			register.setAopinion("");
			register.setBopinion("");
			register.setCopinion("");
			register.setDopinion("");
			register.setProcessResult("");
			register.setStatus(-1);
			register.setProcessName("");
			register.setTokenId(0L);		
			hs.save(register);
			hs.flush();
			
			/* ************ 特殊要求操作 ************* */
			String deletescid="";
			if(scId!=null && scId.length>0){
				if("Y".equals(version)){//再次厂检
					String[] isOk=request.getParameterValues("isOk");
					if(isOk!=null&&isOk.length>0){
						for(int i=0 ; i<isOk.length;i++){
							String[] scjnlno1 = CommonUtil.getBillno(CommonUtil.getToday().substring(2,4),"HandoverElevatorSpecialRegister", 1);	
							HandoverElevatorSpecialRegister hesr=new HandoverElevatorSpecialRegister();
							hesr.setJnlno(scjnlno1[0]);
							hesr.setScId(scId[i]);
							hesr.setElevatorTransferCaseRegister(register);
							hesr.setIsOk(isOk[i]);
					        hs.save(hesr);
					        //hs.flush();
						}
					}
				}else{
					for(int i=0;i<scId.length;i++){
						if(scId[i]!=null&&!"".equals(scId))
						deletescid+=i<scId.length-1 ?scId[i]+"','" : scId[i];
					}
				}
				if(!deletescid.equals("")){
					String deleteScId="delete from HandoverElevatorSpecialRegister r where r.elevatorTransferCaseRegister.billno ='"+billNo+"' and r.scId not in ('"+deletescid+"')";	
					hs.createQuery(deleteScId).executeUpdate();
					//hs.flush();
				}
				if(!"Y".equals(version)){
				    if(scId!=null && scjnlno.length==scId.length){
					    for(int i=0;i<scId.length;i++){
							HandoverElevatorSpecialRegister hesr=null;
							if(scjnlno[i]==null||scjnlno[i].equals("")){
								String[] scjnlno1 = CommonUtil.getBillno(CommonUtil.getToday().substring(2,4),"HandoverElevatorSpecialRegister", 1);	
								hesr=new HandoverElevatorSpecialRegister();
								hesr.setJnlno(scjnlno1[0]);
								hesr.setScId(scId[i]);
								hesr.setElevatorTransferCaseRegister(register);
								hesr.setIsOk("N");
								hs.save(hesr);
								//hs.flush();
							}
						 }
				    }
				}
			}else{
				String deleteScId="delete from HandoverElevatorSpecialRegister r where r.elevatorTransferCaseRegister.billno ='"+billNo+"'";	
				hs.createQuery(deleteScId).executeUpdate();
				//hs.flush();
			}
		    /* ************ 特殊要求操作 ************* */
			
		    /* ************ 检查项目操作 ************* */
			if("Y".equals(version)){//再次厂检
				String hsql="from HandoverElevatorCheckItemRegister where billNo='"+historyBillNo+"' and isDelete='N'";
				
				System.out.println("再次厂检>>>开始保存。");
				System.out.println("开始复制上一份厂检结果。");
				System.out.println(">>>"+hsql);
				
				List helist=hs.createQuery(hsql).list();
				System.out.println("helist="+helist.size());
				if(helist!=null && helist.size()>0){
					HandoverElevatorCheckItemRegister hecir = null;
					HandoverElevatorCheckItemRegister hdecir=null;
					
					for(int h=0;h<helist.size();h++){
						
						hdecir=(HandoverElevatorCheckItemRegister)helist.get(h);
						
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
						hecir.setIsRecheck("Y");
						hecir.setExamType(hdecir.getExamType());
						hecir.setCheckItem(hdecir.getCheckItem());
						hecir.setIssueCoding(hdecir.getIssueCoding());
						hecir.setIssueContents(hdecir.getIssueContents());
						hecir.setRem(hdecir.getRem());
						
						hecir.setElevatorTransferCaseRegister(register);
						hs.save(hecir);	
					}
				}
				System.out.println("结束复制上一份厂检结果。");
			}
			/* ************ 检查项目操作 ************* */
			
			dform.set("id", billNo);
			request.setAttribute("id", billNo);
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
		
	public void refer(ActionForm form, HttpServletRequest request,ActionErrors errors, String id){

		if(id != null && !id.equals("")){
			
			Session hs = null;
			Transaction tx = null;
			ElevatorTransferCaseRegister master = null;				
			
			try {
				
				hs = HibernateUtil.getSession();		
				tx = hs.beginTransaction();
				
				master = (ElevatorTransferCaseRegister) hs.get(ElevatorTransferCaseRegister.class, id);
				master.setSubmitType("Y");
				hs.save(master);
				
				tx.commit();
			} catch (Exception e) {				
				e.printStackTrace();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("maintContract.recordnotfounterror"));
			} finally {
				if(hs != null){
					hs.close();				
				}				
			}
			
		}		
	} 
	/**
	 * 跳转到再次厂检页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareFactoryRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","安装维保交接电梯情况管理 >> 再次厂检");	
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		String id = request.getParameter("id");
		if(id==null || id.equals("")){
			id=(String)dform.get("id");
		}
		if(id==null || id.equals("")){
			id=(String) request.getAttribute("id");
		}
		Session hs=null;
		
		if(id!=null && !id.equals("")){
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from ElevatorTransferCaseRegister where billNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {
					
					// 主信息
					ElevatorTransferCaseRegister register = (ElevatorTransferCaseRegister) list.get(0);
					//register.setAuditor(bd.getName(hs, "Loginuser","username", "userid",register.getAuditor()));// 审核人
					register.setCheckNum(register.getCheckNum()+1);
					register.setCheckTime(CommonUtil.getNowTime());
					register.setR1(register.getFloor()+"/"+register.getStage()+"/"+register.getDoor());
					//request.setAttribute("departmentList", bd.getPullDownList("ElevatorTransferCaseRegister_Department"));
					
					List hecirList=new ArrayList();
					String sql = "select b.examType examType,b.checkItem checkItem,b.issueCoding issueCoding,b.issueContents IssueContents1,"
							+ "b.rem ,p.pullname "
							+ "from HandoverElevatorCheckItemRegister b,pulldown p "
							+ " where p.pullid=b.examType "
							+ " and p.typeflag='HandoverElevatorCheckItem_ExamType' "
							+ " and IsDelete='N'"
							+ " and b.billno='"+id+"'";					
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
						map.put("rem", values[4]);
						map.put("pullname", values[5]);
						hecirList.add(map);
					 }
					request.setAttribute("hecirList", hecirList);						
					}
					
					sql="select r,c.scName from HandoverElevatorSpecialClaim c,HandoverElevatorSpecialRegister r where r.scId=c.scId and r.elevatorTransferCaseRegister.billno='"+register.getBillno()+"'";
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
					
					
					request.setAttribute("historyBillNo", id);
					request.setAttribute("elevatorTransferCaseRegisterBean", register);
					request.setAttribute("operName", userInfo.getUserName());
					request.setAttribute("operDate", CommonUtil.getNowTime());
					request.setAttribute("username", bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
					request.setAttribute("departmentName", bd.getName_Sql("Company", "comname", "comid", register.getDepartment()));
					
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("returnListMethod", "toSearchRecord");
		forward = mapping.findForward("factoryInspection");
		
		return forward;
	}
	
	/**
	 * 点击再次厂检的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toFactoryRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;

		String isreturn = request.getParameter("isreturn");	
		try {
			addOrUpdate(form,request,errors,"Y");// 新增或修改记录
			
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				forward = mapping.findForward("returnList");
			} else {
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				forward = mapping.findForward("returnfactory");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	/*
	 * 跳转到复制方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareCopyRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		request.setAttribute("navigator.location", "安装维保交接电梯情况管理>> 复制");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		display(form, request, errors, "");
		request.setAttribute("operName", userInfo.getUserName());
		request.setAttribute("operDate", CommonUtil.getNowTime());
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("returnListMethod", "toSearchRecord");
		request.setAttribute("isCopy", "yes");
		forward = mapping.findForward("elevatorTransferCaseRegisterCopy");
		
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
	
	public ActionForward toDownloadFileDispose(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
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
	


		/**
		 * 
		 * @Description: 保存html文件
		 * @param @param content
		 * @param @param userid
		 * @param @param pdfPath   
		 * @return void  
		 * @throws
		 * @author gtc
		 * @date 2015-11-20
		 */
		
		private void saveAsFileWriter(String content, String userid, String pdfPath ,String htmlName) {
			/*
			 * 对html源码进行样式替换
			 */
			content = content.replace("class=table_outline3 height=22 ","class=table_outline3 ");
			content= content.replaceAll(".divtable table", ".tb");
			content= content.replaceAll("#divtable", "#pdfdisplay");
			
			//content= content.replaceAll("display : none", "display : block");
			
			File file = new File(pdfPath);
			if (!file.isDirectory()) {
				file.mkdirs();
			}
			String filepath = pdfPath + userid + htmlName;
			FileWriter fwriter = null;
			try {
				fwriter = new FileWriter(filepath);
				fwriter.write(content);
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				try {
					fwriter.flush();
					fwriter.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
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
						if(list1!=null && list1.size()>0){
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
		 * 关闭厂检
		 * @param mapping
		 * @param form
		 * @param request
		 * @param response
		 * @return
		 * @throws IOException
		 * @throws ServletException
		 */
		public ActionForward toIsCloseRecord(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
		
			ActionErrors errors = new ActionErrors();				
			ActionForward forward = null;
			DynaActionForm dform = (DynaActionForm) form;

			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			
			String billno = request.getParameter("id");
			//String isClose = request.getParameter("isClose");

			Session hs = null;
			Transaction tx = null;
				
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				
				ElevatorTransferCaseRegister register = (ElevatorTransferCaseRegister) hs.get(ElevatorTransferCaseRegister.class, billno);
				register.setIsClose("Y");
				
				tx.commit();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
			} catch (Exception e1) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
				try {
					tx.rollback();
				} catch (HibernateException e2) {
					log.error(e2.getMessage());
				}
				e1.printStackTrace();
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
			forward = mapping.findForward("returnList");
			return forward;
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