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

	public class ServicingContractQuoteMasterAction extends DispatchAction {

		Log log = LogFactory.getLog(ServicingContractQuoteMasterAction.class);
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
					SysRightsUtil.NODE_ID_FORWARD + "wgchangemaster", null);
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
			
			request.setAttribute("navigator.location","维改报价申请管理 >> 查询列表");		
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
				HTMLTableCache cache = new HTMLTableCache(session, "wgchangeContractQuoteList");

				DefaultHTMLTable table = new DefaultHTMLTable();
				table.setMapping("fwgchangeSearch");
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
					 submitType="N";
	                 tableForm.setProperty("submittype", "N");
				 }
				Session hs = null;
                Query qu=null;
				try {

					hs = HibernateUtil.getSession();
                    String sql=" select a,b,c,d from ServicingContractQuoteMaster a,Loginuser b,Customer c,Company d where 1=1"
                    		+ " and a.attn=b.userid and a.companyId =c.companyId and a.maintDivision=d.comid";
                    sql+=" and a.status like '"+status+"' ";
                    sql+=xj.getsql("a.billNo", billNo);
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
                    //System.out.println(sql);
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
					session.setAttribute("wgchangeContractQuoteList", table);
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
			
			request.setAttribute("navigator.location","维改报价申请管理 >> 查看");
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
			forward = mapping.findForward("toWGchangeModifyAddDisplay");
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

			request.setAttribute("navigator.location","维改报价申请管理 >> 添加");
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
            Session hs=null;
            List PulldownList=null;
			DynaActionForm dform = (DynaActionForm) form;
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				dform.initialize(mapping);
			}
			ServicingContractQuoteMaster scqm = new ServicingContractQuoteMaster();
			try {
				hs=HibernateUtil.getSession();
				String hql="from Pulldown where typeflag='ServicingContractQuoteOtherDetail_FeeName' and enabledflag='Y' order by orderby";
				PulldownList=hs.createQuery(hql).list();
				scqm.setAttn(userInfo.getUserName()); //经办人
				scqm.setApplyDate(CommonUtil.getToday()); //申请时间
				scqm.setOperId(userInfo.getUserName());//录入人
				scqm.setOperDate(CommonUtil.getToday());//入录时间
				scqm.setMaintDivision(userInfo.getComName()); //维保分部
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("ServicingContractQuoteMaster", scqm);
			//request.setAttribute("maintStationList", bd.getMaintStationList(userInfo.getComID()));// 所属维保站列表
           request.setAttribute("PulldownList", PulldownList);
           request.setAttribute("typejsp", "add");			
           request.setAttribute("maintStationList", bd.getMaintStationList(userInfo.getComID()));// 所属维保站列表
			return mapping.findForward("toWGchangeModifyAddDisplay");
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
			ServicingContractQuoteMaster scqm = null;
			TrainingHistory trainingHistory = null;
			CertificateExam certificateExam = null;
			String isreturn=request.getParameter("isreturn");
			String billno="";
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();

				scqm = new ServicingContractQuoteMaster();

				BeanUtils.copyProperties(scqm, dform); // 复制属性值
				
				String todayDate = xj.getdatetime();
				String year = todayDate.substring(2,4);
				 billno = CommonUtil.getBillno(year,"ServicingContractQuoteMaster", 1)[0];// 生成流水号	
				
				scqm.setBillNo(billno);// 流水号
				scqm.setOperId(userInfo.getUserID());// 录入人
				scqm.setAttn(userInfo.getUserID());//经办人
				scqm.setOperDate(todayDate);// 录入时间
				scqm.setApplyDate(todayDate);//申请日期
				scqm.setMaintDivision(bd.getName("Company", "comid", "comname", scqm.getMaintDivision()));//所属维保分部
				if(isreturn!=null && "Y".equals(isreturn)){
				scqm.setSubmitType("Y");
				}else{
				scqm.setSubmitType("N");	
				}
				scqm.setStatus(new Integer(WorkFlowConfig.State_NoStart));
				hs.save(scqm);	
				//保存电梯信息
				saveServicingContractQuoteDetail(request,hs,billno);
				//保存物料明细
				saveServicingContractQuoteMaterialsDetail(request,hs,billno);
				//其他费用明细
				saveServicingContractQuoteOtherDetail(request,hs,billno);
				//保存附件
				String path = "ServicingContractQuoteMaster.file.upload.folder";
				String tableName = "ServicingContractQuoteMaster";//表名
				String userid = userInfo.getUserID();

				FileOperatingUtil foutil = new FileOperatingUtil();
				List fileInfoList = foutil.saveFile(dform, request,	response, path, billno);
				istrue=this.saveFileInfo(hs, fileInfoList, tableName,	billno, userid);
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
				if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"insert.success"));
					dform.set("id",billno);
					forward=this.toReferRecord(mapping, dform, request, response);
				} else {
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
			
			request.setAttribute("navigator.location","维改报价申请管理 >> 修改");	
			DynaActionForm dform = (DynaActionForm) form;
			ActionErrors errors = new ActionErrors();		
			ActionForward forward = null;
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			String id  = request.getParameter("id");
			if(id==null){
				id = (String) dform.get("id");
			}
			String error = (String) request.getAttribute("error");
			Session hs = null;
			ServicingContractQuoteMaster ServicingContractQuoteMaster=null;
			List scqodList = null;
			List scqdList = null;
			List scqmdList = null;
			if (id != null) {
					try {
						hs = HibernateUtil.getSession();
						Query query = hs.createQuery("from ServicingContractQuoteMaster p where p.billNo = :billno");
						query.setString("billno", id);
						List list = query.list();
						if (list != null && list.size() > 0) {
							ServicingContractQuoteMaster = (ServicingContractQuoteMaster) list.get(0);
							ServicingContractQuoteMaster.setAttn(bd.getName("Loginuser", "username", "userid", ServicingContractQuoteMaster.getAttn()));
							ServicingContractQuoteMaster.setMaintDivision(bd.getName("Company", "comname", "comid", ServicingContractQuoteMaster.getMaintDivision()));
							List scqmList2=xj.getClasses(hs, "Customer", "companyId", ServicingContractQuoteMaster.getCompanyId());
			                if(scqmList2!=null && scqmList2.size()>0){
			                	Customer ct=(Customer)scqmList2.get(0);
			                	request.setAttribute("companyName", ct.getCompanyName());
			                	request.setAttribute("contacts", ct.getContacts());
			                	request.setAttribute("contactPhone", ct.getContactPhone());
			                	request.setAttribute("maintStationList", bd.getMaintStationList(userInfo.getComID()));						
			                }	
			                String hql="from Pulldown where typeflag='ServicingContractQuoteOtherDetail_FeeName' and enabledflag='Y' order by orderby";
							
							query = hs.createQuery("from ServicingContractQuoteDetail p where p.servicingContractQuoteMaster.billNo = '"+id+"'");
							scqdList = query.list();//报价明细
							
							query = hs.createQuery("from ServicingContractQuoteMaterialsDetail p where p.servicingContractQuoteMaster.billNo = '"+id+"'");
							scqmdList = query.list();//物料明细
							
							query = hs.createQuery("from ServicingContractQuoteOtherDetail p where p.servicingContractQuoteMaster.billNo = '"+id+"'order by p.rowid");
							scqodList = query.list();//其他费用明细
							if(scqodList!=null && scqodList.size()>0){
								int j=1;
								for(int i=0;i<scqodList.size();i++){
									ServicingContractQuoteOtherDetail scqod=(ServicingContractQuoteOtherDetail)scqodList.get(i);
									scqod.setR4(xj.getValue(hs, "Pulldown", "pullname", "pullid", scqod.getFeeName()));
									scqod.setR9(j);
									j++;
								}
							}
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
							
							
						} else {
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
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
							DebugUtil
									.print(hex, "HibernateUtil Hibernate Session ");
						}
					}
				forward = mapping.findForward("toWGchangeModifyAddDisplay");
			}
			request.setAttribute("ServicingContractQuoteMaster", ServicingContractQuoteMaster);
			request.setAttribute("scqodList", scqodList);
			request.setAttribute("scqdList", scqdList);
			request.setAttribute("scqmdList", scqmdList);
			request.setAttribute("typejsp", "mondity");
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
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
			String attn=request.getParameter("attn");
			String id = (String) dform.get("id");
			boolean istrue = false;
			ServicingContractQuoteMaster scqm = null;
			String isreturn=request.getParameter("isreturn");
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				hs.createQuery("delete from ServicingContractQuoteMaterialsDetail a where a.servicingContractQuoteMaster.billNo='"+id+"'").executeUpdate();
				hs.createQuery("delete from ServicingContractQuoteDetail a where a.servicingContractQuoteMaster.billNo='"+id+"'").executeUpdate();
				hs.createQuery("delete from ServicingContractQuoteOtherDetail a where a.servicingContractQuoteMaster.billNo='"+id+"'").executeUpdate();				
				hs.flush();
				scqm =(ServicingContractQuoteMaster)hs.get(ServicingContractQuoteMaster.class, id);
				BeanUtils.copyProperties(scqm, dform); // 复制属性值

				String todayDate = xj.getdatetime();
				String year = todayDate.substring(2,4);	
				scqm.setStatus(new Integer(WorkFlowConfig.State_NoStart));
				scqm.setOperId(userInfo.getUserID());// 录入人
				scqm.setOperDate(todayDate);// 录入时间		
				scqm.setAttn(userInfo.getUserID());//经办人
				scqm.setApplyDate(todayDate);//申请日期
				scqm.setMaintDivision(bd.getName("Company", "comid", "comname", scqm.getMaintDivision()));//所属维保分部
				if(isreturn!=null && "Y".equals(isreturn)){
					scqm.setSubmitType("Y");
					}else{
					scqm.setSubmitType("N");	
					}
				hs.save(scqm);	
				//保存电梯信息
				saveServicingContractQuoteDetail(request,hs,id);
				//保存物料明细
				saveServicingContractQuoteMaterialsDetail(request,hs,id);
				//其他费用明细
				saveServicingContractQuoteOtherDetail(request,hs,id);
				//保存附件
				String path = "ServicingContractQuoteMaster.file.upload.folder";
				String tableName = "ServicingContractQuoteMaster";//表名
				String userid = userInfo.getUserID();

				FileOperatingUtil foutil = new FileOperatingUtil();
				List fileInfoList = foutil.saveFile(dform, request,	response, path, id);
				istrue=this.saveFileInfo(hs, fileInfoList, tableName,	id, userid);
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
				if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"insert.success"));
					forward=this.toReferRecord(mapping, dform, request, response);
				} else {
					// return addnew page
					if (errors.isEmpty()) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"insert.success"));
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
				ServicingContractQuoteMaster scqm = (ServicingContractQuoteMaster) hs.get(ServicingContractQuoteMaster.class, id);
				if (scqm != null) {
					hs.createQuery("delete from ServicingContractQuoteMaterialsDetail a where a.servicingContractQuoteMaster.billNo='"+id+"'").executeUpdate();
					hs.createQuery("delete from ServicingContractQuoteDetail a where a.servicingContractQuoteMaster.billNo='"+id+"'").executeUpdate();
					hs.createQuery("delete from ServicingContractQuoteOtherDetail a where a.servicingContractQuoteMaster.billNo='"+id+"'").executeUpdate();
					hs.delete(scqm);
					//删除附件
					xjfile xjf=new xjfile();
                    xjf.toDeleteFile(hs, id, "ContractFileinfo", "ServicingContractQuoteMaster.file.upload.folder");

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
		/*
		 * 保存电梯明细
		 * 
		 * 
		 */
		public void saveServicingContractQuoteDetail(HttpServletRequest request,Session hs,String billno){
            String[] elevatorNos=request.getParameterValues("elevatorNo");
            String[] salesContractNos=request.getParameterValues("salesContractNo");
            String[] projectNames=request.getParameterValues("projectName");
			String[] contentss=request.getParameterValues("contents");
             for(int i=0;i<elevatorNos.length;i++){
				ServicingContractQuoteDetail scq=new ServicingContractQuoteDetail();
				scq.setServicingContractQuoteMaster((ServicingContractQuoteMaster)hs.get(ServicingContractQuoteMaster.class,billno));
				scq.setElevatorNo(elevatorNos[i]);
				scq.setSalesContractNo(salesContractNos[i]);
				scq.setProjectName(projectNames[i]);
				scq.setContents(contentss[i]);
				hs.saveOrUpdate(scq);
             }		
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
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
			String id = (String) dform.get("id");
			JbpmExtBridge jbpmExtBridge = null;
			if (id != null && id.length() > 0) {
				try {
					String ApplyId = userInfo.getUserID();// 当前登录人
					String applyDate = CommonUtil.getToday();// 当前日期
					String applytime = CommonUtil.getTodayTime();// 当前时间
					String datetime = applyDate + " " + applytime;
					hs = HibernateUtil.getSession();
					tx = hs.beginTransaction();
					String sql = "select a from ServicingContractQuoteMaster a where a.billNo='"+ id.trim() + "'";
					List qlist = hs.createQuery(sql).list();
					if (qlist != null && qlist.size() > 0) {
						ServicingContractQuoteMaster scqm = (ServicingContractQuoteMaster) qlist.get(0);
						scqm.setSubmitType("Y");// Y表示已提交，N表示未提交

						/** ======== 启动新流程实例 开始 ============= */
						HashMap paraMap = new HashMap();
						jbpmExtBridge = new JbpmExtBridge();
						ProcessBean pd = jbpmExtBridge.getPb();
						// 经办人
						String attn = scqm.getAttn();	
						// 环节id
						String processDefineID = Grcnamelist1.getProcessDefineID("servicingcontractquotemaster",attn);
						if(processDefineID ==null ||processDefineID.equals("") ){
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","<font color='red'>未配置审批流程信息，不能启动！</font>"));// 未有资源
						}
						//设置审核人
						Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "维保分部长审核", attn);//添加审核人
						
						pd = jbpmExtBridge.startProcess(WorkFlowConfig.getProcessDefine(processDefineID),ApplyId, ApplyId, id.trim(), "", paraMap);

						scqm.setStatus(pd.getStatus()); // 流程状态
						scqm.setTokenId(Long.valueOf(pd.getToken()));// 流程令牌
						scqm.setProcessName(pd.getNodename());// 环节名称
						/** ======== 启动新流程实例 结束 ============= */

						hs.save(scqm);
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"application.submit.sucess"));
					}
					tx.commit();
				} catch (Exception e) {
					if (tx != null) {
						tx.rollback();
					}
					if (jbpmExtBridge != null) {
						jbpmExtBridge.setRollBack();
					}
					e.printStackTrace();
				} finally {
					try {
						if (jbpmExtBridge != null) {
							jbpmExtBridge.close();
						}
						if (hs != null) {
							hs.close();
						}
					} catch (HibernateException hex) {
						log.error(hex.getMessage());
						DebugUtil.print(hex, "Hibernate close error!");
					}
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
		/*
		 * 保存物料明细
		 * 
		 * 
		 * 
		 */
		public void saveServicingContractQuoteMaterialsDetail(HttpServletRequest request,Session hs,String billno){
            String[] materialName=request.getParameterValues("materialName");
			String[] materialsStandard=request.getParameterValues("materialsStandard");
			String[] quantity=request.getParameterValues("quantity");
			String[] unit=request.getParameterValues("unit");
			String[] unitPrice=request.getParameterValues("unitPrice");
			String[] finalPrice2=request.getParameterValues("finalPrice2");
			String[] price2=request.getParameterValues("price2");
			if(materialName!=null && materialName.length>0){
			 for(int i=0;i<materialName.length;i++){
				 ServicingContractQuoteMaterialsDetail scq=new ServicingContractQuoteMaterialsDetail();
					scq.setServicingContractQuoteMaster((ServicingContractQuoteMaster)hs.get(ServicingContractQuoteMaster.class,billno));
					scq.setMaterialName(materialName[i]);
					scq.setMaterialsStandard(materialsStandard[i]);
					scq.setQuantity(Double.valueOf(quantity[i]));
					scq.setUnit(unit[i]);
					scq.setUnitPrice(Double.valueOf(unitPrice[i]));
					scq.setFinalPrice(Double.valueOf(finalPrice2[i]));
					scq.setPrice(Double.valueOf(price2[i]));
					hs.saveOrUpdate(scq);
	             }	
			}
		}
		/*
		 * 
		 * 其他费用明细
		 * 
		 */
		public void saveServicingContractQuoteOtherDetail(HttpServletRequest request,Session hs,String billno){
            String[] pullid=request.getParameterValues("pullid");
            String[] price=request.getParameterValues("price");
            String[] finalPrice=request.getParameterValues("finalPrice");
           if(pullid!=null && pullid.length>0){
			 for(int i=0;i<pullid.length;i++){
				 ServicingContractQuoteOtherDetail scq=new ServicingContractQuoteOtherDetail();
					scq.setServicingContractQuoteMaster((ServicingContractQuoteMaster)hs.get(ServicingContractQuoteMaster.class,billno));
					scq.setPrice(Double.valueOf(price[i]));
					scq.setFeeName(pullid[i]);
					scq.setFinalPrice(Double.valueOf(finalPrice[i]));
					hs.saveOrUpdate(scq);
			 }
          }
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
	}

