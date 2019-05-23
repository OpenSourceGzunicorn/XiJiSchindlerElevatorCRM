package com.gzunicorn.struts.action.engcontractmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractdetail.EntrustContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractmaster.EntrustContractMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class EntrustContractModifyAction extends DispatchAction {

	Log log = LogFactory.getLog(EntrustContractModifyAction.class);
	
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
	 * 维保委托合同修改
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
		
		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		
		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "entrustContractModify", null);
		/** **********结束用户权限过滤*********** */
		
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
		
		request.setAttribute("navigator.location","维保委托合同修改>> 查询列表");		
		ActionForward forward = null;
		String authority=request.getParameter("authority");
		request.setAttribute("authority", authority);
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			//try {
				//response = toExcelRecord(form,request,response);
			//} catch (IOException e) {
			//	e.printStackTrace();
			//}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport","");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "entrustContractModifyList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fEntrustContractModify");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("billNo");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String billNo = tableForm.getProperty("billNo");// 流水号
			String entrustContractNo = tableForm.getProperty("entrustContractNo");// 委托合同号
			String companyName = tableForm.getProperty("companyName");// 甲方单位
			String attn = tableForm.getProperty("attn");// 经办人
			String maintDivision = tableForm.getProperty("maintDivision");// 所属维保站		
			String maintContractNo = tableForm.getProperty("maintContractNo");// 维保合同
			String contractNatureOf = tableForm.getProperty("contractNatureOf");// 合同性质
			//第一次进入页面时根据登陆人初始化所属维保分部
			List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
			if(maintDivision == null || maintDivision.equals("")){
				Map map = (Map)maintDivisionList.get(0);
				maintDivision = (String)map.get("grcid");
			}
			
			Session hs = null;
			Query query = null;
			try {

				hs = HibernateUtil.getSession();
				//在保，审核通过
				String sql = "select e.billNo,e.entrustContractNo,e.contractSdate,e.contractEdate," +
						" l.username,c.comname,e.auditStatus,"+ 
						" d.companyName,e.maintContractNo,e.contractNatureOf,e.r1" +
						" from EntrustContractMaster e left outer join Customer d on e.companyId2 = d.companyId,Loginuser l,Company c" +
						" where e.attn=l.userid" +
						" and e.maintDivision=c.comid and e.r1 in('ZB','END')" +
						" and e.submitType='Y' and e.auditStatus='Y' ";
				
				if (billNo != null && !billNo.equals("")) {
					sql += " and e.billNo like '%"+billNo.trim()+"%'";
				}
				if (entrustContractNo != null && !entrustContractNo.equals("")) {
					sql += " and e.entrustContractNo like '%"+entrustContractNo.trim()+"%'";
				}
				if (companyName != null && !companyName.equals("")) {
					sql += " and companyName like '%"+companyName.trim()+"%'";
				}
				if (attn != null && !attn.equals("")) {
					sql += " and l.username like '%"+attn.trim()+"%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and c.comid like '"+maintDivision.trim()+"'";
				}
				if (maintContractNo != null && !maintContractNo.equals("")) {
					sql += " and e.maintContractNo like '"+maintContractNo.trim()+"'";
				}
				if (contractNatureOf != null && !contractNatureOf.equals("")) {
					sql += " and e.contractNatureOf = '"+contractNatureOf.trim()+"'";
				}
				if (table.getIsAscending()) {
					sql += " order by "+ table.getSortColumn() +" desc";
				} else {
					sql += " order by "+ table.getSortColumn() +" asc";
				}
				
				query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List entrustContractList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map master=new HashMap();
					master.put("billNo", objs[0]);
					master.put("entrustContractNo", objs[1]);
					master.put("contractSdate", objs[2]);
					master.put("contractEdate", objs[3]);
					master.put("attn", objs[4]);
					master.put("maintDivision", objs[5]);
					master.put("auditStatus", objs[6]);
					master.put("companyName", objs[7]);
					master.put("maintContractNo", objs[8]);
					master.put("contractNatureOf", objs[9]);
					master.put("r1", objs[10]);
					entrustContractList.add(master);
				}

				table.addAll(entrustContractList);
				session.setAttribute("entrustContractModifyList", table);
				// 合同性质下拉框列表
				request.setAttribute("contractNatureOfList", bd.getPullDownList("MaintContractMaster_ContractNatureOf"));
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);

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
			forward = mapping.findForward("entrustContractModifyList");
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
	 * 跳转到审核页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareAuditRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","维保委托合同修改 >> 修 改");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		Session hs = null;
		List maintContractDetailList = null;
	
		if (id != null && !id.equals("")) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from EntrustContractMaster where billNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {
	
					List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型下拉列表
					
					// 主信息
					EntrustContractMaster master = (EntrustContractMaster) list.get(0);															
					master.setAttn(bd.getName(hs, "Loginuser","username", "userid",master.getAttn()));// 经办人
					master.setAuditOperid(userInfo.getUserName());
					
					// 甲方单位信息
					Customer companyA = (Customer) hs.get(Customer.class,master.getCompanyId());															
					// 乙方方单位信息
					Customer companyB = (Customer) hs.get(Customer.class,master.getCompanyId2());
										
					// 明细列表
					String sql = "from EntrustContractDetail where billNo = '"+id+"'";					
					query = hs.createQuery(sql);	
					maintContractDetailList = query.list();				
					for (Object object : maintContractDetailList) {
						EntrustContractDetail detail = (EntrustContractDetail) object;
						detail.setElevatorType(bd.getOptionName(detail.getElevatorType(), elevatorTypeList));
					}					
							
					master.setMaintDivision(bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision()));// 维保分部	
					master.setMaintStation(bd.getName(hs, "Storageid", "storagename", "storageid", master.getMaintStation()));// 维保站	
					master.setOperId(bd.getName("Loginuser", "username", "userid", master.getOperId()));
					master.setContractNatureOf(bd.getName_Sql("Pulldown", "pullname", "pullid", master.getContractNatureOf()));
						
					master.setEndOperId(bd.getName("Loginuser", "username", "userid", master.getEndOperId()));
					
					request.setAttribute("entrustContractMasterBean", master);	
					request.setAttribute("companyA",companyA);
					request.setAttribute("companyB",companyB);
					request.setAttribute("entrustContractDetailList", maintContractDetailList);
					request.setAttribute("auditOperid",userInfo.getUserName());
					request.setAttribute("auditDate", CommonUtil.getNowTime());
							
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
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
		forward = mapping.findForward("entrustContractModifyDisplay");
		
		return forward;
	}
	
	/**
	 * 点击审核的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toAuditRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

		String billNo = (String) dform.get("billNo");
		Double contractTotal=(Double)dform.get("contractTotal");
		Double otherFee=(Double)dform.get("otherFee");

		String contractSdate=(String)dform.get("contractSdate");
		String contractEdate=(String)dform.get("contractEdate");
		String contractPeriod=(String)dform.get("contractPeriod");
		String paymentMethod = (String) dform.get("paymentMethod");
		String contractTerms = (String) dform.get("contractTerms");
		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			EntrustContractMaster master=(EntrustContractMaster) hs.get(EntrustContractMaster.class, billNo.trim());
			master.setContractSdate(contractSdate);
			master.setContractEdate(contractEdate);
			master.setContractPeriod(contractPeriod);
			master.setOldContractTotal(master.getContractTotal());
			master.setOldOtherFee(master.getOtherFee());
			master.setContractTotal(contractTotal);
			master.setOtherFee(otherFee);
			master.setModifyId(userInfo.getUserID());
			master.setModifyDate(CommonUtil.getNowTime());
			master.setPaymentMethod(paymentMethod);
			master.setContractTerms(contractTerms);
			hs.save(master);
			
			String[] rowid=request.getParameterValues("rowid");
			String[] mainEdate=request.getParameterValues("mainEdate");
			for(int i=0;i<rowid.length;i++){
				EntrustContractDetail ecd=(EntrustContractDetail)hs.get(EntrustContractDetail.class, Integer.valueOf(rowid[i]));
				ecd.setMainEdate(mainEdate[i]);
				hs.save(ecd);
			}
			
			tx.commit();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
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

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		forward = mapping.findForward("returnList");
		return forward;
	}
	
}	