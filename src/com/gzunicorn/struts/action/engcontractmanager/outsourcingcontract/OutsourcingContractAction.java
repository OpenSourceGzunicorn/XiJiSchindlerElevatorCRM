package com.gzunicorn.struts.action.engcontractmanager.outsourcingcontract;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
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
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.FileOperatingUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.file.HandleFile;
import com.gzunicorn.file.HandleFileImpA;
import com.gzunicorn.file.xjfile;
import com.gzunicorn.hibernate.basedata.Fileinfo;
import com.gzunicorn.hibernate.basedata.certificateexam.CertificateExam;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.basedata.jobhistory.JobHistory;
import com.gzunicorn.hibernate.basedata.personnelmanage.PersonnelManageMaster;
import com.gzunicorn.hibernate.basedata.traininghistory.TrainingHistory;
import com.gzunicorn.hibernate.engcontractmanager.ContractFileinfo.ContractFileinfo;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquotemaster.MaintContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.outsourcecontractdetail.OutsourceContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.outsourcecontractmaster.OutsourceContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.outsourcecontractquotemaster.OutsourceContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractdetail.ServicingContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractmaster.ServicingContractMaster;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractquotedetail.ServicingContractQuoteDetail;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractquotemaster.ServicingContractQuoteMaster;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractquotematerialsdetail.ServicingContractQuoteMaterialsDetail;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractquoteotherdetail.ServicingContractQuoteOtherDetail;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractquoteprocess.ServicingContractQuoteProcess;
import com.gzunicorn.hibernate.sysmanager.Company;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.struts.action.xjsgg.XjsggAction;
import com.gzunicorn.workflow.bo.JbpmExtBridge;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

	public class OutsourcingContractAction extends DispatchAction {

		Log log = LogFactory.getLog(OutsourcingContractAction.class);
		XjsggAction xj=new XjsggAction();
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

			/** **********开始用户权限过滤*********** */
			SysRightsUtil.filterModuleRight(request, response,
					SysRightsUtil.NODE_ID_FORWARD + "outsourcingContract", null);
			/** **********结束用户权限过滤*********** */

			// Set default method is toSearchRecord
			String name = request.getParameter("method");
			String typejsp=request.getParameter("typejsp");
			if ((typejsp==null || typejsp.equals("")) && (name == null || name.equals(""))) {
				name = "toSearchRecord";
				return dispatchMethod(mapping, form, request, response, name);
			} else {
				if( "add".equals(typejsp)){
					name="toAddRecord";
					return dispatchMethod(mapping, form, request, response, name);
				}else if("mondity".equals(typejsp)){
					name="toUpdateRecord";
					return dispatchMethod(mapping, form, request, response, name);
				}else if("totask".equals(typejsp)){
					name="tosaveTask";
					return dispatchMethod(mapping, form, request, response, name);
				}
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
			
			request.setAttribute("navigator.location","维改委托合同管理 >> 查询列表");		
			ActionForward forward = null;
			ActionErrors error=new ActionErrors();
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			ServeTableForm tableForm = (ServeTableForm) form;
			String action = tableForm.getAction();
            List list2=new ArrayList();
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
				HTMLTableCache cache = new HTMLTableCache(session, "outsourcingContractList");

				DefaultHTMLTable table = new DefaultHTMLTable();
				table.setMapping("fOutsourcingContract");
				table.setLength(SysConfig.HTML_TABLE_LENGTH);
				cache.updateTable(table);
				table.setSortColumn("billno");
				table.setIsAscending(false);
				cache.updateTable(table);

				if (action.equals(ServeTableForm.NAVIGATE)
						|| action.equals(ServeTableForm.SORT)) {
					cache.loadForm(tableForm);
				} else {
					table.setFrom(0);
				}
				cache.saveForm(tableForm);
				String companyId = tableForm.getProperty("companyname");// 甲方单位名称
				String maintDivision = tableForm.getProperty("maintDivision");//所属维保分部
				String billno = tableForm.getProperty("billno");//流水号
				String MaintContractNo = tableForm.getProperty("MaintContractNo");//外包合同号
				String attn = tableForm.getProperty("attn");//经办人
				String submitType = tableForm.getProperty("submitType");//提交标志
				String auditStatus = tableForm.getProperty("auditStatus");//审核状态

				String outContractNo=tableForm.getProperty("outContractNo");//外包合同
				String busType = tableForm.getProperty("busType");//业务类别
				 if(maintDivision==null){
                 	List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
         			if(maintDivision == null || maintDivision.equals("")){
         				Map map = (Map)maintDivisionList.get(0);
         				maintDivision = (String)map.get("grcid");
         			}
                 }
				Session hs = null;
                Query qu=null;
				try {

					hs = HibernateUtil.getSession();
                    String sql=" select a,b,c,d from OutsourceContractMaster a,Loginuser b,Customer c,Company d where 1=1"
                    		+ " and a.attn=b.userid and a.companyId2 =c.companyId and a.maintDivision=d.comid";
                    sql+=xj.getsql("a.billno", billno);
                    sql+=xj.getnullsql("a.submitType", submitType);                    
                    if(companyId==null){
                    	companyId="%";
                    }
                    sql+=" and (c.companyName like '%"+companyId+"%' or c.companyId  like '%"+companyId+"%')";
                    sql+=xj.getsql("d.comid", maintDivision);
                    sql+=xj.getsql("a.busType", busType);
                    sql+=xj.getsql("a.maintContractNo", MaintContractNo);
                    sql+=xj.getsql("a.outContractNo", outContractNo);
                    
                    if(attn!=null && !"".equals(attn)){
            			sql+=" and  (a.attn like '%"+attn+"%' or b.username like '%"+attn+"%')";
            		}else{
            			sql+=" and  a.attn like '%'";
            		}
                    if(auditStatus!=null && !"".equals(auditStatus)){
            			sql+=" and  isnull(a.auditStatus,'N') like '%"+auditStatus.trim()+"%'";
            		}else{
            			sql+=" and  isnull(a.auditStatus,'N') like '%'";
            		}

                    
                    if(table.getIsAscending()){
                    	sql+=" order by a."+table.getSortColumn()+"";
                    }else{
                    	sql+=" order by a."+table.getSortColumn()+" desc";
                    }
                    //System.out.println(sql);
                    qu=hs.createQuery(sql);
					table.setVolume(qu.list().size());// 查询得出数据记录数;

					// 得出上一页的最后一条记录数号;
					qu.setFirstResult(table.getFrom()); // pagefirst
					qu.setMaxResults(table.getLength());

					cache.check(table);

					List outsourcingContractList = qu.list();
					if(outsourcingContractList!=null && outsourcingContractList.size()>0){
						for(int i=0;i<outsourcingContractList.size();i++){
							Object[] obj=(Object[])outsourcingContractList.get(i);
							OutsourceContractMaster scqm=(OutsourceContractMaster)obj[0];
							Loginuser lo=(Loginuser)obj[1];
							Customer  ct=(Customer)obj[2];
							Company  cpy=(Company)obj[3];
							scqm.setAttn(lo.getUsername());
                            scqm.setCompanyId(ct.getCompanyName());
						    scqm.setMaintDivision(cpy.getComname());
						    scqm.setBusType(scqm.getBusType().equals("G")?"改造":"维修");
						    if(scqm.getSubmitType().equals("R")){
						    	scqm.setSubmitType("驳回");
						    }else{
						    	scqm.setSubmitType(scqm.getSubmitType().equals("N")?"未提交":"已提交");
						    }
						    scqm.setAuditStatus(scqm.getAuditStatus().equals("Y")?"已审核":"未审核");
						    scqm.setCompanyId(scqm.getCompanyId());
						    list2.add(scqm);
						}
	
					}					
					table.addAll(list2);
					session.setAttribute("outsourcingContractList", table);
					request.setAttribute("maintDivisionList", Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
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
				forward = mapping.findForward("outsourcingContractList");
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
			
			request.setAttribute("navigator.location","维改委托合同管理 >> 查看");
			DynaActionForm dform = (DynaActionForm) form;
			ActionErrors errors = new ActionErrors();
			ActionForward forward = null;			
			String id = (String) dform.get("id");
			Session hs = null;
			OutsourceContractMaster scm=null;
			Customer ctj=null;
			Customer cty=null;
			List servicingContractDetailList = null;
		   if (id != null) {				
			try {
				hs = HibernateUtil.getSession();
				scm=(OutsourceContractMaster)hs.get(OutsourceContractMaster.class, id);
				scm.setAttn(bd.getName(hs,"Loginuser", "username", "userid", scm.getAttn()));
				scm.setMaintDivision(bd.getName(hs,"Company", "comname", "comid", scm.getMaintDivision()));
			    scm.setAuditOperid(bd.getName(hs,"Loginuser", "username", "userid", scm.getAuditOperid()));
			    scm.setAuditStatus(scm.getAuditStatus().equals("N")?"未审核":"已审核");
			    request.setAttribute("maintStationName", bd.getName(hs, "Storageid","storageName", "storageID", scm.getMaintStation())); //维保站名称
			    //甲方单位
				String companyId=scm.getCompanyId();
				if(companyId!=null && !"".equals(companyId)){
					ctj=(Customer)hs.get(Customer.class, companyId);
				}
				//乙方单位
				String companyId2=scm.getCompanyId2();
				if(companyId2!=null && !"".equals(companyId2)){
					cty=(Customer)hs.get(Customer.class, companyId2);
				}
				
				servicingContractDetailList=hs.createQuery("from OutsourceContractDetail a where a.outsourceContractMaster.billno='"+id+"'").list();	
				
			} catch (DataStoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			
		}else{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
		}
		request.setAttribute("ServicingContractMasterList", scm);
		request.setAttribute("CustomerList", ctj);
		request.setAttribute("CustomerList2", cty);
		request.setAttribute("ServicingContractDetailList", servicingContractDetailList);
		String typejsp=request.getParameter("typejsp");
		if(typejsp!=null && !"".equals(typejsp)){
			request.setAttribute("type", typejsp);
		}
		request.setAttribute("typejsp", "display");			
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
			forward = mapping.findForward("toContractDisplayAddMondity");
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

			request.setAttribute("navigator.location","维改委托合同管理 >> 添加");
			DynaActionForm dform = (DynaActionForm) form;
			HttpSession session = request.getSession();
			ActionErrors errors = new ActionErrors();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

			String billNo=request.getParameter("billNo");
			
            Session hs=null;
            List PulldownList=null;			
			Customer ct=null;
			OutsourceContractQuoteMaster quote=null;
			ServicingContractMaster scm=null;
			ServicingContractDetail scd=null;
			List list2=null;
			if(billNo!=null && !"".equals(billNo)){
				try {
					hs=HibernateUtil.getSession();
					quote=(OutsourceContractQuoteMaster) hs.get(OutsourceContractQuoteMaster.class, billNo.trim());
					scm=(ServicingContractMaster)hs.get(ServicingContractMaster.class, quote.getWgBillno());
					scm.setContractTotal(quote.getRealPrice());
					scm.setCompanyId("xjs000");
					scm.setCompanyId2(quote.getCompanyId());
					request.setAttribute("quoteBillNo", quote.getBillNo());
					request.setAttribute("maintStationName", bd.getName(hs, "Storageid", "storagename", "storageid", scm.getMaintStation()));
					ct=(Customer)hs.get(Customer.class, "xjs000");
					Customer companyB=(Customer) hs.get(Customer.class, quote.getCompanyId());
					//电梯明细
					list2=hs.createQuery("select a from ServicingContractDetail a where a.servicingContractMaster.wgBillno='"+quote.getWgBillno()+"'").list();
					request.setAttribute("ServicingContractMasterList", scm);
					request.setAttribute("ServicingContractDetailList", list2);
					request.setAttribute("userInfoList", userInfo);
					request.setAttribute("CustomerList", ct);
					request.setAttribute("companyB", companyB);
	                request.setAttribute("typejsp", "add");	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","选取数据失败！"));
				
			}
			
			
			return mapping.findForward("toContractDisplayAddMondity");
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
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			Session hs = null;
			Transaction tx = null;
			boolean istrue = false;
			OutsourceContractMaster ocm = null;
			String isreturn=request.getParameter("isreturn");
			String billNo=(String)dform.get("billNo");
			String maintStation=(String)dform.get("maintStation");
			String wgbillno="";
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				ocm = new OutsourceContractMaster();
				BeanUtils.copyProperties(ocm, dform); // 复制属性值				
				
				
				String billno = CommonUtil.getBillno(CommonUtil.getToday().substring(2,4),"OutsourceContractMaster", 1)[0];		
				
				
				/* 生成外 合同号 */
				String year = CommonUtil.getNowTime("yyyy");
				String prefix = year+"-";
				String suffix ="W";
				String outContractNo = CommonUtil.genNo("OutsourceContractMaster", "OutContractNo", prefix, suffix, 3,"OutContractNo");
				
				ocm.setBillno("W"+billno);
				ocm.setOutContractNo(outContractNo);
				ocm.setOperId(userInfo.getUserID());// 录入人
				ocm.setAttn(userInfo.getUserID());//经办人
				ocm.setCompanyId("xjs000");
				ocm.setOperDate(CommonUtil.getNowTime());// 录入时间
				ocm.setAuditStatus("N");//未审核
				if(isreturn!=null && "Y".equals(isreturn)){
					ocm.setSubmitType("Y");
				}else{
					ocm.setSubmitType("N");	
				}
				hs.save(ocm);	
				//保存电梯信息
				
				List list=hs.createQuery("select a from ServicingContractDetail a where a.servicingContractMaster.wgBillno='"+ocm.getWgBillno().trim()+"'").list();
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						OutsourceContractDetail scd=new OutsourceContractDetail();
						ServicingContractDetail scqd=(ServicingContractDetail)list.get(i);
						scd.setWgRowid(scqd.getRowid());
						scd.setContents(scqd.getContents());
						scd.setElevatorNo(scqd.getElevatorNo());
						scd.setProjectName(scqd.getProjectName());
						scd.setSalesContractNo(scqd.getSalesContractNo());
						scd.setOutsourceContractMaster(ocm);
						hs.save(scd);
					}
				}
				tx.commit();		
			} catch (Exception e1) {
				tx.rollback();
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
			try {				
					if (errors.isEmpty()) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"insert.success"));
					} else {
						request.setAttribute("error", "Yes");
					}
					if(isreturn!=null && "Y".equals(isreturn)){
					forward = mapping.findForward("returnList");	
					}else{
						request.setAttribute("billno", ocm.getBillno());
					forward = mapping.findForward("returnMondity");		
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
			
			request.setAttribute("navigator.location","维改委托合同管理 >> 修改");	
			DynaActionForm dform = (DynaActionForm) form;
			HttpSession session=request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			ActionErrors errors = new ActionErrors();		
			ActionForward forward = null;
			String id = request.getParameter("id");
			if(id==null){
				id=(String) request.getAttribute("billno");
			}
			String error = (String) request.getAttribute("error");
			Session hs = null;
			OutsourceContractMaster ocm=null;
			Customer ctj=null;
			Customer cty=null;
			List outsourceContractDetailList = null;
			if (id != null) {
					try {
						hs = HibernateUtil.getSession();
						ocm=(OutsourceContractMaster)hs.get(OutsourceContractMaster.class, id);
						//甲方单位
						String companyId=ocm.getCompanyId();
						if(companyId!=null && !"".equals(companyId)){
							ctj=(Customer)hs.get(Customer.class, companyId);
						}
						//乙方单位
						String companyId2=ocm.getCompanyId2();
						if(companyId2!=null && !"".equals(companyId2)){
							cty=(Customer)hs.get(Customer.class, companyId2);
						}
						request.setAttribute("maintStationName", bd.getName(hs, "Storageid", "storagename", "storageid", ocm.getMaintStation()));
						outsourceContractDetailList=hs.createQuery("from OutsourceContractDetail a where a.outsourceContractMaster.billno='"+id+"'").list();	
						
                     
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				
			}else{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
			}
			request.setAttribute("ServicingContractMasterList", ocm);
			request.setAttribute("CustomerList", ctj);
			request.setAttribute("CustomerList2", cty);
			request.setAttribute("ServicingContractDetailList", outsourceContractDetailList);
			request.setAttribute("userInfoList", userInfo);
			request.setAttribute("typejsp", "mondity");
			request.setAttribute("maintStationList", bd.getMaintStationList(userInfo.getComID()));
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
			forward = mapping.findForward("toContractDisplayAddMondity");
			return forward;
		}
		
		/**
		 * 紧急级别修改
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
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			Session hs = null;
			Transaction tx = null;
			String id = request.getParameter("billno");
			String companyId2 = request.getParameter("companyId2");
			String signingDate = request.getParameter("signingDate");
			String contractTotal = request.getParameter("contractTotal");
			
			
			String otherFee = request.getParameter("otherFee");
			String paymentMethod = request.getParameter("paymentMethod");
			String contractTerms = request.getParameter("contractTerms");
			request.setAttribute("billno",id);
			boolean istrue = false;
			OutsourceContractMaster ocm = null;
			String isreturn=request.getParameter("isreturn");
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				ocm =(OutsourceContractMaster)hs.get(OutsourceContractMaster.class, id);
				ocm.setCompanyId2(companyId2);
				ocm.setSigningDate(signingDate);
				ocm.setContractTotal(Double.valueOf(contractTotal));
				ocm.setOtherFee(Double.valueOf(otherFee));
				ocm.setPaymentMethod(paymentMethod);
				ocm.setContractTerms(contractTerms);
				ocm.setOperDate(CommonUtil.getNowTime());
				ocm.setOperId(userInfo.getUserID());// 录入人		
				ocm.setAttn(userInfo.getUserID());//经办人
				
		        if(isreturn!=null && "Y".equals(isreturn)){
					ocm.setSubmitType("Y");
					ocm.setAuditOperid("");
					ocm.setAuditDate("");
					ocm.setAuditRem("");
					}else{
					ocm.setSubmitType("N");	
					}
				hs.save(ocm);	
				tx.commit();
		
			} catch (Exception e1) {
				tx.rollback();
				e1.printStackTrace();
				log.error(e1.getMessage());
				DebugUtil.print(e1, "Hibernate region Insert error!");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"error.string","<font color='red'>保存失败！</font>"));
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "Hibernate close error!");
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"error.string","<font color='red'>数据异常！</font>"));
				}
			}

			ActionForward forward = null;
			try {
				
					if (errors.isEmpty()) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"insert.success"));
					} else {
						request.setAttribute("error", "Yes");
					}
					
					if(isreturn!=null && "Y".equals(isreturn)){
						forward = mapping.findForward("returnList");	
					}else{
						forward = mapping.findForward("returnMondity");		
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
				
				String id = request.getParameter("id");
				OutsourceContractMaster scqm = (OutsourceContractMaster) hs.get(OutsourceContractMaster.class, id);
				if (scqm != null) {
					hs.createQuery("delete from OutsourceContractDetail a where a.outsourceContractMaster.billno='"+id+"'").executeUpdate();
					hs.delete(scqm);					
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
					criteria.add(Expression.like("contractNo", "%" + contractNo.trim() + "%"));
				}
				if (name != null && !name.equals("")) {
					criteria.add(Expression.like("name", "%" + name.trim() + "%"));
				}
				if (startDates != null && !startDates.equals("")) {
					criteria.add(Expression.ge("startDate", startDates.trim()));
				}
				if (startDatee != null && !startDatee.equals("")) {
					criteria.add(Expression.le("startDate", startDatee.trim()));
				}
				if (enabledFlag != null && !enabledFlag.equals("")) {
					criteria.add(Expression.eq("enabledFlag", enabledFlag));
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
							cell.setCellValue(getValue(master, superClazz, columnNames[j]));						
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
		 * 提交审核走流程
		 * 
		 * @param mapping
		 * @param form
		 * @param request
		 * @param response
		 * @return ActionForward
		 */

		public ActionForward toReferRecord(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {

			DynaActionForm dform = (DynaActionForm) form;
			ActionErrors errors = new ActionErrors();
			Session hs = null;
			Transaction tx = null;
			ActionForward forward = null;
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
			OutsourceContractMaster ocm=null;
			String id = request.getParameter("id");
			if (id != null && id.length() > 0) {
				try {
					hs=HibernateUtil.getSession();	
					tx=hs.getTransaction();
					tx.begin();
					ocm=(OutsourceContractMaster)hs.get(OutsourceContractMaster.class, id);
					ocm.setSubmitType("Y");
					ocm.setAuditOperid("");
					ocm.setAuditDate("");
					ocm.setAuditRem("");
					hs.saveOrUpdate(ocm);
					tx.commit();
				} catch (Exception e) {
					tx.rollback();
					e.printStackTrace();
				} finally {
                    hs.close();		
			    }			
			}else{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","提交失败！"));
			}
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
			forward = mapping.findForward("returnList");
			return forward;
		}
		
		/**
		 * 获得对象指定的get方法并返回执行该get方法后的值
		 * @param object javabean对象
		 * @param superClazz object的类，子类没有相应get方法时请传入object的父类
		 * @param fieldName 属性名
		 * @return ActionForward
		 */
		private String getValue(Object object, Class<?> superClazz, String fieldName){
			String value = null;	
			String methodName = "get" + fieldName.replaceFirst(fieldName.substring(0, 1),fieldName.substring(0, 1).toUpperCase());
			try {
				Method method = superClazz.getMethod(methodName);
				value = method.invoke(object, null) + "";
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return value;

		}
		/**
		 * 保存文件信息到数据库
		 * @param fileInfoList
		 */
		public boolean saveFileInfo(Session hs,List fileInfoList,String tableName,String billno,String userid){
			boolean saveFlag = true;
			ContractFileinfo file=null;
			if(null != fileInfoList && !fileInfoList.isEmpty()){
				
				try {
					int len = fileInfoList.size();
					for(int i = 0 ; i < len ; i++){
						Map map = (Map)fileInfoList.get(i);
						 String title =(String) map.get("title");
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
						 file.setUploadDate(CommonUtil.getToday());
						 file.setUploader(userid);
						 file.setRemarks(rem);
						 
						 hs.save(file);
						 hs.flush();
					}
				} catch (ParseException e) {
					e.printStackTrace();
					saveFlag = false;
				} catch (Exception e) {
					e.printStackTrace();
					saveFlag = false;
				}
			}
			return saveFlag;
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
		public ActionForward toNextSearchRecord(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) {
			
			request.setAttribute("navigator.location","维改委托合同管理  >> 维改合同选取录入");		
			ActionForward forward = null;
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			ServeTableForm tableForm = (ServeTableForm) form;
			String action = tableForm.getAction();
            List list2=new ArrayList();
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
				HTMLTableCache cache = new HTMLTableCache(session, "outsourcingContractList2");

				DefaultHTMLTable table = new DefaultHTMLTable();
				table.setMapping("fOutsourcingContractNextSearch");
				table.setLength(SysConfig.HTML_TABLE_LENGTH);
				cache.updateTable(table);
				table.setSortColumn("billNo");
				table.setIsAscending(false);
				cache.updateTable(table);

				if (action.equals(ServeTableForm.NAVIGATE)
						|| action.equals(ServeTableForm.SORT)) {
					cache.loadForm(tableForm);
				} else {
					table.setFrom(0);
				}
				cache.saveForm(tableForm);				
				String billNo = tableForm.getProperty("billNo");// 业务类型
				String companyName = tableForm.getProperty("companyName");// 经办人
				String maintContractNo = tableForm.getProperty("maintContractNo");// 维改合同号
				//String billNo = tableForm.getProperty("billNo");// 维改流水号
				String maintDivision = tableForm.getProperty("maintDivision");//所属维保分部
				if(maintDivision==null){
                 	List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
         			if(maintDivision == null || maintDivision.equals("")){
         				Map map = (Map)maintDivisionList.get(0);
         				maintDivision = (String)map.get("grcid");
         			}
                 }				 
				Session hs = null;
                SQLQuery qu=null;
				try {
					hs = HibernateUtil.getSession();
                    String sql="select a.*,isnull(b.companyName,'') companyName,c.username from"+
                    " OutsourceContractQuoteMaster a left outer join Customer b on a.companyId=b.companyId,Loginuser c "+
                    "where a.operId=c.userid and status=1"+
                    " and a.billNo not in(select distinct isnull(Quote_BillNo,'') from OutsourceContractMaster)";
                   /* if(busType!=null&&!busType.equals("")){
                    	sql+=" and s.busType='"+busType+"'";
                    }*/
                    if(billNo!=null&&!billNo.equals("")){
                    	sql+=" and a.billNo like '%"+billNo+"%'";
                    }
                    if(companyName!=null&&!companyName.equals("")){
                    	sql+=" and (b.companyName like '%"+companyName+"%' or b.companyId like '%"+companyName+"%')";
                    }
                    if(maintContractNo!=null&&!maintContractNo.equals("")){
                    	sql+=" and a.maintContractNo like '%"+maintContractNo.trim()+"%'";
                    }
                    if(maintDivision!=null&&!maintDivision.equals("")){
                    	sql+=" and a.maintDivision like '"+maintDivision+"'";
                    }
                    if(table.getIsAscending()){
                    	sql+=" order by a."+table.getSortColumn()+"";
                    }else{
                    	sql+=" order by a."+table.getSortColumn()+" desc";
                    }
                    qu=hs.createSQLQuery(sql);
                    qu.addEntity("a",OutsourceContractQuoteMaster.class);
    				qu.addScalar("companyName");
    				qu.addScalar("username");
					table.setVolume(qu.list().size());// 查询得出数据记录数;

					// 得出上一页的最后一条记录数号;
					qu.setFirstResult(table.getFrom()); // pagefirst
					qu.setMaxResults(table.getLength());

					cache.check(table);

					List servicingContractMasterList = qu.list();
					if(servicingContractMasterList!=null && servicingContractMasterList.size()>0){
						for (Object object : servicingContractMasterList) {
							Object[] objs = (Object[])object;
							OutsourceContractQuoteMaster master = (OutsourceContractQuoteMaster) objs[2];
							//master.setCompanyId(bd.getName_Sql("Customer", "companyName", "companyId", master.getCompanyId()));
							master.setCompanyId(objs[0].toString());
							master.setOperId(objs[1].toString());
							//master.setR1(bd.getName_Sql("ViewFlowStatus", "typename", "typeid", String.valueOf(master.getStatus())));
							master.setMaintDivision(bd.getName("Company","comname","comid",master.getMaintDivision()));
							list2.add(master);
						}
						/*for(int i=0;i<servicingContractMasterList.size();i++){
							ServicingContractMaster sm=(ServicingContractMaster)servicingContractMasterList.get(i);
							sm.setAttn(bd.getName(hs, "Loginuser","username", "userid", sm.getAttn()));
							sm.setMaintDivision(bd.getName(hs, "Company", "comfullname", "comid", sm.getMaintDivision()));
							sm.setBusType(sm.getBusType().equals("G")?"改造":"维修");
							list2.add(sm);
						}*/
	
					}					
					table.addAll(list2);
					session.setAttribute("outsourcingContractList2", table);
					request.setAttribute("maintDivisionList", Grcnamelist1.getgrcnamelist(userInfo.getUserID()));

			
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
				forward = mapping.findForward("tonextList");
			}
			return forward;
		}

		
}

