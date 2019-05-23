package com.gzunicorn.struts.action.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchProjectNameAction extends DispatchAction {

	Log log = LogFactory.getLog(SearchProjectNameAction.class);
	
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
		//SysRightsUtil.filterModuleRight(request, response, SysRightsUtil.NODE_ID_FORWARD + "", null);
		/** **********结束用户权限过滤*********** */

		// Set default method is toSearchRecord
		
		String name = request.getParameter("method");	
	
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
		
		request.setAttribute("navigator.location","查找 >> 合同信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		HTMLTableCache cache = new HTMLTableCache(session, "projectNameList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fProjectName");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("projectName");
		table.setIsAscending(true);
		cache.updateTable(table);
		
		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else { 
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String projectName = tableForm.getProperty("projectName"); 
		String projectAddress = tableForm.getProperty("projectAddress");
		
		String jnlno = request.getParameter("jnlno"); 		
		String billNos = request.getParameter("billNos"); 	
		
		if(jnlno == null || jnlno.trim().equals("")){
			jnlno=tableForm.getProperty("jnlno");
		}else{
			tableForm.setProperty("jnlno",jnlno);
		}				
		if(billNos == null || billNos.trim().equals("")){
			billNos=tableForm.getProperty("billNos");
		}else{
			tableForm.setProperty("billNos",billNos);
		}
			
		Session hs = null; 
		Query query = null;
		try {

			hs = HibernateUtil.getSession();
			String sql ="select mcm.billNo,mcd.projectName,mcd.maintAddress," +
					"mcm.contractSdate,mcm.contractEdate "
					+ "from MaintContractMaster mcm,MaintContractDetail mcd,CustomerVisitPlanMaster cvpm," +
					"CustomerVisitPlanDetail cvpd"
					+ " where mcm.billNo=mcd.billNo "
					+ " and cvpm.billno=cvpd.billno "
					+ " and mcd.rowid in (select max(mcd1.rowid) from MaintContractDetail mcd1 where mcd1.billNo = mcm.billNo group by mcd1.projectName)"
					+ " and (mcm.contractStatus = 'ZB' or mcm.contractStatus='XB')"//新签和续签
					+ " and cvpm.companyId=mcm.companyId "
					+ " and cvpd.jnlno='"+jnlno+"'";
			
			if (billNos != null && !billNos.equals("")) {
				sql += " and mcm.billNo not in ("+billNos.replace("|", "'")+")";
			}
			if (projectName != null && !projectName.equals("")) {
				sql += " and mcd.projectName like '%"+projectName.trim()+"%'";
			}
			if (projectAddress != null && !projectAddress.equals("")) {
				sql += " and mcd.maintAddress like '%"+projectAddress.trim()+"%'";
			}
					
			if (table.getIsAscending()) {
				sql += " order by "+ table.getSortColumn() +" asc";
			} else {
				sql += " order by "+ table.getSortColumn() +" desc";
			}
			
			////System.out.println("====="+sql);
			
			query = hs.createQuery(sql);
			table.setVolume(query.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List list = query.list();
			List projectNameList = new ArrayList();

			if(list!=null&&list.size()>0)
			{
				int length=list.size();
				for(int i=0;i<length;i++)
				{
				    Object[] objects=(Object[]) list.get(i);
				    HashMap map=new HashMap();
				    map.put("billNo", objects[0]);
				    map.put("projectName", objects[1]);
				    map.put("projectAddress", objects[2]);
				    map.put("contractSdate", objects[3]);
				    map.put("contractEdate", objects[4]);
				    projectNameList.add(map);
				}
			}
			table.addAll(projectNameList);
			session.setAttribute("projectNameList", table);
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
		forward = mapping.findForward("projectNameList");
		
		return forward;
	}
	
	

}