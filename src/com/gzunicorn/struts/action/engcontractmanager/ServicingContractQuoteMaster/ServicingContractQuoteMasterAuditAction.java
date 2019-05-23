package com.gzunicorn.struts.action.engcontractmanager.ServicingContractQuoteMaster;

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

	public class ServicingContractQuoteMasterAuditAction extends DispatchAction {

		Log log = LogFactory.getLog(ServicingContractQuoteMasterAuditAction.class);
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
					SysRightsUtil.NODE_ID_FORWARD + "wgchangemasteraudit", null);
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
			
			request.setAttribute("navigator.location","维改报价申请审核 >> 查询列表");		
			ActionForward forward = null;
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			ServeTableForm tableForm = (ServeTableForm) form;
			String action = tableForm.getAction();
            List list2=new ArrayList();

				HTMLTableCache cache = new HTMLTableCache(session, "wgchangeContractAuQuoteList");

				DefaultHTMLTable table = new DefaultHTMLTable();
				table.setMapping("fwgchangeAuditSearch");
				table.setLength(SysConfig.HTML_TABLE_LENGTH);
				cache.updateTable(table);
				table.setSortColumn("a.billNo");
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
				String status = tableForm.getProperty("status");// 流程状态
				if(status==null){
					status="%";
				}
				String submitType = tableForm.getProperty("submittype");// 提交标志
				String billNo = tableForm.getProperty("billNo");// 流水号
				String attn = tableForm.getProperty("attn");//经办人
				String busType = tableForm.getProperty("busType");//业务类别
				if(maintDivision==null){
                 	List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
         			if(maintDivision == null || maintDivision.equals("")){
         				Map map = (Map)maintDivisionList.get(0);
         				maintDivision = (String)map.get("grcid");
         			}
                 }
				 if(submitType==null){
					 submitType="Y";
	                 tableForm.setProperty("submittype", "Y");
				 }
				Session hs = null;
                Query qu=null;
				try {

					hs = HibernateUtil.getSession();
                    String sql=" select a,b,c,d from ServicingContractQuoteMaster a,Loginuser b,Customer c,Company d where 1=1"
                    		+ " and a.attn=b.userid and a.companyId =c.companyId and a.maintDivision=d.comid";
                    sql+=xj.getsql("a.billNo", billNo);
                    sql+=" and a.status like '"+status+"' " ;
                    sql+=xj.getsql("a.submitType", submitType);
                    if(companyId==null){
                    	companyId="%";
                    }
                    sql+=" and (c.companyName like '%"+companyId+"%' or c.companyId ='"+companyId+"')";
                    sql+=xj.getsql("d.comid", maintDivision);
                    sql+=xj.getsql("a.busType", busType);
                    if(attn!=null && !"".equals(attn)){
            			sql+=" and  (a.attn like '%"+attn+"%' or b.username like '%"+attn+"%')";
            		}else{
            			sql+=" and  a.attn like '%'";
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

					List wgchangeContractQuoteList = qu.list();
					if(wgchangeContractQuoteList!=null && wgchangeContractQuoteList.size()>0){
						for(int i=0;i<wgchangeContractQuoteList.size();i++){
							Object[] obj=(Object[])wgchangeContractQuoteList.get(i);
							ServicingContractQuoteMaster scqm=(ServicingContractQuoteMaster)obj[0];
							Loginuser lo=(Loginuser)obj[1];
							Customer  ct=(Customer)obj[2];
							Company  cpy=(Company)obj[3];
							scqm.setAttn(lo.getUsername());
							scqm.setBusType(scqm.getBusType().equals("G")?"改造":"维修");
							scqm.setSubmitType(scqm.getSubmitType().equals("Y")?"已提交":"未提交");
							scqm.setR1(xj.getValue(hs,"ViewFlowStatus", "typename", "typeid",String.valueOf(scqm.getStatus())));
						    scqm.setCompanyId(ct.getCompanyName());
						    scqm.setMaintDivision(cpy.getComname());
						    list2.add(scqm);
						}
	
					}					
					table.addAll(list2);
					session.setAttribute("wgchangeContractAuQuoteList", table);
					request.setAttribute("flowname", WorkFlowConfig.getProcessDefine("servicingcontractquotemasterProcessName"));
					request.setAttribute("maintDivisionList", Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
			        request.setAttribute("processStateList", xj.getProcessState(hs));
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
				forward = mapping.findForward("toWGchangeList");
			
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
			
			request.setAttribute("navigator.location","维改报价申请审核 >> 查看");
			DynaActionForm dform = (DynaActionForm) form;
			ActionErrors errors = new ActionErrors();
			ActionForward forward = null;
			
			String id = (String) dform.get("id");
			Session hs = null;
			ServicingContractQuoteMaster scqm=null;
			List scqodList = null;
			List scqdList = null;
			List scqmdList = null;
		   if (id != null) {				
			try {
				hs = HibernateUtil.getSession();
				scqm=(ServicingContractQuoteMaster)hs.get(ServicingContractQuoteMaster.class,id);
				scqm.setBusType(scqm.getBusType().equals("W")?"维修":"改造");
				scqm.setAttn(bd.getName("Loginuser", "username", "userid", scqm.getAttn()));
				List scqmList2=xj.getClasses(hs, "Customer", "companyId", scqm.getCompanyId());
                if(scqmList2!=null && scqmList2.size()>0){
                	Customer ct=(Customer)scqmList2.get(0);
                	scqm.setCompanyId(ct.getCompanyName());
                	scqm.setMaintDivision(bd.getName("Company", "comname", "comid", scqm.getMaintDivision()));
                	request.setAttribute("contacts", ct.getContacts());
                	request.setAttribute("contactPhone", ct.getContactPhone());
                	request.setAttribute("maintStationName", bd.getName(hs, "Storageid","storageName", "storageID", scqm.getMaintStation())); //维保站名称
                }
				scqodList=xj.getClasses(hs, "ServicingContractQuoteOtherDetail", "servicingContractQuoteMaster.billNo", id);
				if(scqodList!=null && scqodList.size()>0){
					for(int i=0;i<scqodList.size();i++){
						ServicingContractQuoteOtherDetail sc=(ServicingContractQuoteOtherDetail)scqodList.get(i);
						sc.setFeeName(xj.getValue(hs, "Pulldown", "pullname", "pullid", sc.getFeeName()));
					}
				}
				scqdList=xj.getClasses(hs, "ServicingContractQuoteDetail", "servicingContractQuoteMaster.billNo", id);
				scqmdList=xj.getClasses(hs, "ServicingContractQuoteMaterialsDetail", "servicingContractQuoteMaster.billNo", id);			
				//附件

				List updatefileList2 = xj.getfile(hs,"ContractFileinfo","ServicingContractQuoteMaster",id);
				List updatefileList=new ArrayList();
				if(updatefileList2!=null && updatefileList2.size()>0){
					for(int i=0;i<updatefileList2.size();i++){
						Object[] obj=(Object[])updatefileList2.get(i);
						HashMap hm=new HashMap();
						hm.put("oldfilename", obj[3]);
						hm.put("uploadername", obj[21]);
						hm.put("uploaddate", obj[8]);
						hm.put("filesid", obj[0]);
						updatefileList.add(hm);
					}
				request.setAttribute("updatefileList", updatefileList);			
				}
				//审批流程信息
				Query query = hs.createQuery("from ServicingContractQuoteProcess where billNo = '"+ id + "' order by itemId");
				List processApproveList = query.list();
				for (Object object : processApproveList) {
					ServicingContractQuoteProcess process = (ServicingContractQuoteProcess) object;
					process.setUserId(bd.getName(hs, "Loginuser", "username", "userid", process.getUserId()));
				}
				request.setAttribute("processApproveList",processApproveList);
				
			} catch (DataStoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		    request.setAttribute("ServicingContractQuoteMaster", scqm);
		    request.setAttribute("scqodList", scqodList);
		    request.setAttribute("scqdList", scqdList);
		    request.setAttribute("scqmdList", scqmdList);
			request.setAttribute("display", "yes");
			request.setAttribute("typejsp", "display");
			forward = mapping.findForward("toWGchangeAuditDisplay");
			return forward;
		}
		/*
		 * 
		 * 重启流程
		 * 
		 * 
		 */
		public ActionForward toReStartProcess(ActionMapping mapping,
				ActionForm form, HttpServletRequest request,
				HttpServletResponse response) {

			ActionErrors errors = new ActionErrors();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo) request.getSession().getAttribute(SysConfig.LOGIN_USER_INFO);

			String id = request.getParameter("id");
			
			Session hs = null;
			Transaction tx = null;
			ServicingContractQuoteMaster master = null;
			if (id != null) {
				try {
					hs = HibernateUtil.getSession();
					tx = hs.beginTransaction();
					
					master = (ServicingContractQuoteMaster) hs.get(ServicingContractQuoteMaster.class, id);
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
					ServicingContractQuoteProcess process = new ServicingContractQuoteProcess();
				    process.setServicingContractQuoteMaster(master);  
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

