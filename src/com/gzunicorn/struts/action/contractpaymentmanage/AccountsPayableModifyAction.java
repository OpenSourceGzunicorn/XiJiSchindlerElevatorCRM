package com.gzunicorn.struts.action.contractpaymentmanage;

import java.io.IOException;
import java.math.BigDecimal;
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
import com.gzunicorn.hibernate.basedata.receivablesname.ReceivablesName;
import com.gzunicorn.hibernate.contractpaymentmanage.contractpayablesmaster.ContractPayablesMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class AccountsPayableModifyAction extends DispatchAction {

	Log log = LogFactory.getLog(AccountsPayableModifyAction.class);
	
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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "accountsPayableModify", null);
		/** **********结束用户权限过滤*********** */

		// Set default method is toSearchRecord
		String name = request.getParameter("method");
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
		
		request.setAttribute("navigator.location","应付款修改 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			//try {
			//	response = toExcelRecord(form,request,response);
			//} catch (IOException e) {
			//	e.printStackTrace();
			//}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport","");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "accountsPayableModifyList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fAccountsPayableModify");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("jnlNo");
			table.setIsAscending(true);
			cache.updateTable(table);
			/** 该方法是记住了历史查询条件*/
			if (action.equals(ServeTableForm.NAVIGATE) || action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String jnlNo = tableForm.getProperty("jnlNo");// 应收款流水号
			String contractNo = tableForm.getProperty("contractNo");// 合同号
			String companyName = tableForm.getProperty("companyName");// 甲方单位
			String maintDivision=tableForm.getProperty("maintDivision");//所属维保分部
			
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
				
                String sql = "select cpm,c.companyName,compay.comfullname,rn.recName " +
                		"from ContractPayablesMaster cpm,Customer c,Company compay,ReceivablesName rn " +
                		"where rn.recId=cpm.recName and compay.comid=cpm.maintDivision " +
                		"and cpm.companyId=c.companyId and cpm.submitType='Y' and cpm.auditStatus='Y' ";
				
				if (jnlNo != null && !jnlNo.equals("")) {
					sql += " and cpm.jnlNo like '%"+jnlNo.trim()+"%'";
				}
				if (contractNo != null && !contractNo.equals("")) {
					sql += " and cpm.entrustContractNo like '%"+contractNo.trim()+"%'";
				}
				if (companyName != null && !companyName.equals("")) {
					sql += " and (c.companyName like '%"+companyName.trim()+"%' or c.companyId like '%"+companyName.trim()+"%')";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and cpm.maintDivision like '"+maintDivision.trim()+"'";
				}
				if (table.getIsAscending()) {
					sql += " order by cpm."+ table.getSortColumn() +" desc";
				} else {
					sql += " order by cpm."+ table.getSortColumn() +" asc";
				}
				
				
				query = hs.createQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List accountsPayableAuditList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					ContractPayablesMaster cpm=(ContractPayablesMaster) objs[0];
					cpm.setCompanyId((String) objs[1]);
					cpm.setMaintDivision((String) objs[2]);
					cpm.setRecName((String)objs[3]);
					accountsPayableAuditList.add(cpm);
				}

				table.addAll(accountsPayableAuditList);
				session.setAttribute("accountsPayableModifyList", table);
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
			forward = mapping.findForward("accountsPayableModifyList");
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
	
	public List getReceivablesNameList(){
		Session hs = null;
		Query query = null;
		List list=null;
		try {
			hs=HibernateUtil.getSession();
			String hql="from ReceivablesName where enabledFlag='Y'";
			list=hs.createQuery(hql).list();
			for(Object object : list){
				ReceivablesName receivables=(ReceivablesName)object;
			}
		} catch (DataStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
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
		
		request.setAttribute("navigator.location","应付款修改>> 修改");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		
		display(form, request, errors, "");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
			
		request.setAttribute("returnMetho", "toSearchRecord");
		forward = mapping.findForward("accountsPayableModify");
		
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
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);

		String jnlNo = (String)dform.get("jnlNo");
		String preDate = (String)dform.get("preDate");
		Double preMoney = (Double)dform.get("preMoney");
		if (jnlNo != null && !jnlNo.equals("")) {
			Session hs = null;
			Transaction tx = null;
			try {

				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();

				ContractPayablesMaster master = (ContractPayablesMaster) hs.get(ContractPayablesMaster.class, jnlNo);

				master.setOldPreMoney(master.getPreMoney());
				master.setOldPreDate(master.getPreDate());
				master.setPreMoney(preMoney);
				master.setPreDate(preDate);
				master.setModifyId(userInfo.getUserID());
				master.setModifyDate(CommonUtil.getNowTime());
				
				hs.save(master);

				tx.commit();

				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
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
		forward = mapping.findForward("returnList");			
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	public void display(ActionForm form, HttpServletRequest request ,ActionErrors errors ,String flag){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		if(id==null || "".equals(id)){
			id=(String) request.getAttribute("id");
		}
		Session hs = null;
		
		if (id != null && !id.equals("")) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from ContractPayablesMaster where jnlNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {					
					// 主信息
					ContractPayablesMaster master = (ContractPayablesMaster) list.get(0);															
					HashMap ecm=new HashMap();
					String sql="select e.billno,e.contractNo,e.num,e.comfullname,e.companyName,e.maintDivision,e.contractTotal,e.companyId2 from Payables_view e where  e.billno='"+master.getBillNo()+"'";
					List list1=hs.createSQLQuery(sql).list();
					if(list1!=null && list1.size()>0){
						Object[] obj=(Object[]) list1.get(0); 				
						ecm.put("billno", obj[0]);
						ecm.put("contractNo", obj[1]);
						ecm.put("num", obj[2]);
						ecm.put("comfullname", obj[3]);
						ecm.put("companyName", obj[4]);
						ecm.put("maintDivision", obj[5]);
						ecm.put("contractTotal", obj[6]);
						ecm.put("companyId2", obj[7]);
					}
					
					
					request.setAttribute("contractTotal", ecm.get("contractTotal"));
					String hql="select isnull(sum(preMoney),0) from ContractPayablesMaster where entrustContractNo='"+ecm.get("contractNo")+"' and (auditStatus='N' or auditStatus='Y') group by entrustContractNo";
					List preMoneylist=hs.createSQLQuery(hql).list();
					double builtReceivables=0.0;
					if(preMoneylist!=null&&preMoneylist.size()>0)
					{
					  builtReceivables=Double.valueOf(preMoneylist.get(0).toString());
					}			
					request.setAttribute("builtReceivables", builtReceivables);
					request.setAttribute("noBuiltReceivables", ((BigDecimal) ecm.get("contractTotal")).doubleValue()-builtReceivables);		
					
					master.setAuditOperid(bd.getName(hs, "Loginuser", "username", "userid", master.getAuditOperid()));
					master.setMaintDivision(bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision()));// 维保分部	
					master.setCompanyId(bd.getName_Sql("Customer", "companyName", "companyId", master.getCompanyId()));
					master.setOperId(bd.getName(hs, "Loginuser", "username", "userid", master.getOperId()));
					master.setRecName(bd.getName_Sql("ReceivablesName", "recName", "recId", master.getRecName()));
										
					request.setAttribute("accountsPayableAuditBean", master);	
					
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
		
	}
	/**
	 * 删除
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
			ContractPayablesMaster master = (ContractPayablesMaster) hs.get(ContractPayablesMaster.class, id);
			if (master != null) {
				hs.delete(master);
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.succeed"));
			}
			tx.commit();
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
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
	
}	