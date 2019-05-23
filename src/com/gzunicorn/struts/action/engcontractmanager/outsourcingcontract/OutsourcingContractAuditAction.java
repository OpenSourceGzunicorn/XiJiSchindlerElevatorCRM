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
import com.gzunicorn.hibernate.engcontractmanager.outsourcecontractmaster.OutsourceContractMaster;
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

	public class OutsourcingContractAuditAction extends DispatchAction {

		Log log = LogFactory.getLog(OutsourcingContractAuditAction.class);
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
					SysRightsUtil.NODE_ID_FORWARD + "outsourcingContractAudit", null);
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
			
			request.setAttribute("navigator.location","维改委托合同审核 >> 查询列表");		
			ActionForward forward = null;
			ActionErrors error=new ActionErrors();
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			ServeTableForm tableForm = (ServeTableForm) form;
			String action = tableForm.getAction();
            List list2=new ArrayList();
				HTMLTableCache cache = new HTMLTableCache(session, "outsourcingContractAuditList");

				DefaultHTMLTable table = new DefaultHTMLTable();
				table.setMapping("fOutsourcingContractAudit");
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
                    sql+=xj.getnullsql("a.submitType", "Y");                    
                    if(companyId==null){
                    	companyId="%";
                    }
                    sql+=" and (c.companyName like '%"+companyId+"%' or c.companyId ='"+companyId+"')";
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
            			tableForm.setProperty("auditStatus", "N");
            			sql+=" and  isnull(a.auditStatus,'N') like 'N'";
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
					session.setAttribute("outsourcingContractAuditList", table);
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
				forward = mapping.findForward("toOutsourcingList");
			
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
			
			request.setAttribute("navigator.location","维改委托合同审核 >> 查看与审核 ");
			DynaActionForm dform = (DynaActionForm) form;
			ActionErrors errors = new ActionErrors();
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
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
				
				if(scm.getAuditStatus().equals("未审核"))
				{
					if(scm.getSubmitType().equals("Y")){
					request.setAttribute("typejsp", "Auditing");
					scm.setAuditOperid(userInfo.getUserName());
					scm.setAuditDate(CommonUtil.getNowTime());
					}else{
						request.setAttribute("typejsp", "display");
					}
				}else{
					request.setAttribute("typejsp", "display");
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
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
			forward = mapping.findForward("toOutsourcingAuditDisplay");
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
			String auditDate=request.getParameter("auditDate");//审核时间
			OutsourceContractMaster ocm=null;
			ServicingContractMaster scm=null;
		    Session hs=null;
			Transaction tx=null;			
			try {
				hs=HibernateUtil.getSession();
				tx=hs.beginTransaction();
				ocm=(OutsourceContractMaster)hs.get(OutsourceContractMaster.class, id);
				ocm.setAuditOperid(userInfo.getUserID());
				if(auditStatus!=null && "Y".equals(auditStatus)){
				ocm.setAuditStatus(auditStatus);
				scm = (ServicingContractMaster)hs.get(ServicingContractMaster.class, ocm.getWgBillno());
				scm.setEpibolyFlag("Y");
				}else if(auditStatus!=null && "N".equals(auditStatus)){
				ocm.setSubmitType("R");	
				}
				ocm.setAuditDate(auditDate);
				ocm.setAuditRem(auditRem);
				hs.save(ocm);
				hs.update(scm);
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
			forward=mapping.findForward("returnList");
			return forward;
		}
	}
