package com.gzunicorn.struts.action.engcontractmanager.servicingcontract;

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
import com.gzunicorn.hibernate.engcontractmanager.maintcontractquoteprocess.MaintContractQuoteProcess;
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

	public class ServicingContractAuditAction extends DispatchAction {

		Log log = LogFactory.getLog(ServicingContractAuditAction.class);
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
					SysRightsUtil.NODE_ID_FORWARD + "wgchangeContractAudit", null);
			/** **********结束用户权限过滤*********** */

			// Set default method is toSearchRecord
			String name = request.getParameter("method");
			if (name == null || name.equals("")) {
				name = "toSearchRecord";
				return dispatchMethod(mapping, form, request, response, name);
			} else{
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
			
			request.setAttribute("navigator.location","维改合同审核 >> 查询列表");		
			ActionForward forward = null;
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			ServeTableForm tableForm = (ServeTableForm) form;
			String action = tableForm.getAction();
            List list2=new ArrayList();			
				HTMLTableCache cache = new HTMLTableCache(session, "wgchangeContractAuList");

				DefaultHTMLTable table = new DefaultHTMLTable();
				table.setMapping("fwgchangeContractAuditSearch");
				table.setLength(SysConfig.HTML_TABLE_LENGTH);
				cache.updateTable(table);
				table.setSortColumn("a.wgBillno");
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
				String wgBillno = tableForm.getProperty("wgBillno");//流水号
				String MaintContractNo = tableForm.getProperty("MaintContractNo");//维改合同号
				String attn = tableForm.getProperty("attn");//经办人
				String submitType = tableForm.getProperty("submitType");//提交标志
				String auditStatus = tableForm.getProperty("auditStatus");//审核状态
				String taskSubFlag = tableForm.getProperty("taskSubFlag");//任务下达状态
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
                    String sql=" select a,b,c,d from ServicingContractMaster a,Loginuser b,Customer c,Company d where 1=1"
                    		+ " and a.attn=b.userid and a.companyId =c.companyId and a.maintDivision=d.comid";
                    sql+=xj.getsql("a.wgBillno", wgBillno);
                    //sql+=xj.getnullsql("a.submitType", submitType);                    
                    sql+=xj.getnullsql("a.taskSubFlag", taskSubFlag);
                    if(companyId==null){
                    	companyId="%";
                    }
                    sql+=" and (c.companyName like '%"+companyId+"%' or c.companyId ='"+companyId+"')";
                    sql+=xj.getsql("d.comid", maintDivision);
                    sql+=xj.getsql("a.busType", busType);
                    sql+=xj.getsql("a.maintContractNo", MaintContractNo);
                    if(attn!=null && !"".equals(attn)){
            			sql+=" and  (a.attn like '%"+attn+"%' or b.username like '%"+attn+"%')";
            		}else{
            			sql+=" and  a.attn like '%'";
            		}
                    if(submitType!=null && !"".equals(submitType)){
            			sql+=" and  a.submitType like '%"+submitType+"%'";
            		}else{
            			tableForm.setProperty("submitType", "Y");
            			sql+=" and  a.submitType like 'Y'";
            		}
                    if(auditStatus!=null && !"".equals(auditStatus)){
            			sql+=" and  isnull(a.auditStatus,'N') like '%"+auditStatus.trim()+"%'";
            		}else{
            			tableForm.setProperty("auditStatus", "N");
            			sql+=" and  isnull(a.auditStatus,'N') like 'N'";
            		}
                    if(table.getIsAscending()){
                    	sql+=" order by "+table.getSortColumn()+"";
                    }else{
                    	sql+=" order by "+table.getSortColumn()+" desc";
                    }
                    qu=hs.createQuery(sql);
					table.setVolume(qu.list().size());// 查询得出数据记录数;

					// 得出上一页的最后一条记录数号;
					qu.setFirstResult(table.getFrom()); // pagefirst
					qu.setMaxResults(table.getLength());

					cache.check(table);

					List wgchangeContractList = qu.list();
					if(wgchangeContractList!=null && wgchangeContractList.size()>0){
						for(int i=0;i<wgchangeContractList.size();i++){
							Object[] obj=(Object[])wgchangeContractList.get(i);
							ServicingContractMaster scqm=(ServicingContractMaster)obj[0];
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
						    scqm.setTaskSubFlag(scqm.getTaskSubFlag().equals("Y")?"已下达":"未下达");
						    String AuditStatus2=scqm.getAuditStatus();
						    scqm.setAuditStatus(AuditStatus2.equals("Y")?"已审核":"未审核");
						    scqm.setCompanyId(scqm.getCompanyId());
						    list2.add(scqm);
						}
	
					}					
					table.addAll(list2);
					session.setAttribute("wgchangeContractAuList", table);
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
				forward = mapping.findForward("toList");
			
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
			
			request.setAttribute("navigator.location","维改合同审核 >> 查看与审核");
			DynaActionForm dform = (DynaActionForm) form;
			ActionErrors errors = new ActionErrors();
			ActionForward forward = null;			
			String id = (String) dform.get("id");
			if(id==null){
				id=request.getParameter("id");
			}
			Session hs = null;
			ServicingContractMaster scm=null;
			Customer ctj=null;
			Customer cty=null;
			List ServicingContractDetailList = new ArrayList();
		   if (id != null) {				
			try {
				hs = HibernateUtil.getSession();
				scm=(ServicingContractMaster)hs.get(ServicingContractMaster.class, id);
				scm.setAttn(bd.getName("Loginuser", "username", "userid", scm.getAttn()));
				scm.setMaintDivision(bd.getName("Company", "comname", "comid", scm.getMaintDivision()));
				scm.setAuditOperid(bd.getName("Loginuser", "username", "userid", scm.getAuditOperid()));
				String AuditStatus2=scm.getAuditStatus();
				dform.set("auditStatus", AuditStatus2);				
			    scm.setAuditStatus(AuditStatus2.equals("Y")?"已审核":"未审核");
			    scm.setTaskUserId(bd.getName("Loginuser", "username", "userid", scm.getTaskUserId()));					
			    scm.setTaskSubFlag(scm.getTaskSubFlag().equals("N")?"未下达":"已下达");
			    request.setAttribute("maintStationName", bd.getName(hs, "Storageid","storageName", "storageID", scm.getMaintStation()));
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
				
				List list=hs.createQuery("select a from ServicingContractDetail a where a.servicingContractMaster.wgBillno='"+id+"'").list();	
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						ServicingContractDetail scd=(ServicingContractDetail)list.get(i);
						ServicingContractDetailList.add(scd);
					}
				}
				
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
		request.setAttribute("ServicingContractDetailList", ServicingContractDetailList);
		request.setAttribute("typejsp", "Auditing");
		dform.set("id", id);		
		dform.set("submitType", scm.getSubmitType());
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
			forward = mapping.findForward("toContractDisplayAudit");
			return forward;
		}
		/**
		 * 点击查看的方法
		 * @param mapping
		 * @param form
		 * @param request
		 * @param response
		 * @return
		 */
		public ActionForward tosaveAuditing(ActionMapping mapping,
				ActionForm form, HttpServletRequest request,
				HttpServletResponse response) {
			ActionForward forward = null;
			DynaActionForm dform = (DynaActionForm) form;
			ActionErrors errors = new ActionErrors();
			HttpSession session=request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			String id=(String)dform.get("id");
			String auditStatus=(String)dform.get("auditStatus");//审核状态
			String auditRem=request.getParameter("auditRem");//审核意见
			ServicingContractMaster scm=null;
		    Session hs=null;
			Transaction tx=null;			
			try {
				hs=HibernateUtil.getSession();
				tx=hs.beginTransaction();
				scm=(ServicingContractMaster)hs.get(ServicingContractMaster.class, id);
				String auditDate=xj.getdatetime();//审核时间
				String auditOperid=userInfo.getUserID();//审核人
				scm.setAuditOperid(auditOperid);
				if(auditStatus!=null && "Y".equals(auditStatus)){
				scm.setAuditStatus(auditStatus);
				}else if(auditStatus!=null && "N".equals(auditStatus)){
				scm.setSubmitType("R");	
				}
				scm.setAuditDate(auditDate);
				scm.setAuditRem(auditRem);
				hs.save(scm);
				tx.commit();
				
			} catch (Exception e) {
				tx.rollback();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","<font color='red'>审核失败！</font>"));
				e.printStackTrace();
			}finally{
				hs.close();
			}
			if (errors.isEmpty()) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","审核成功！"));
			} else {
				request.setAttribute("error", "Yes");
			}
			if(!errors.isEmpty()){
				saveErrors(request,errors);
			}
			forward=mapping.findForward("returnAudit");
			return forward;
		}
	}
