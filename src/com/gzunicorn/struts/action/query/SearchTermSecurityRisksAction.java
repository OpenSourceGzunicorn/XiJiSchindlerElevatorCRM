package com.gzunicorn.struts.action.query;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.basedata.markingitems.MarkingItems;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchTermSecurityRisksAction extends DispatchAction {
Log log = LogFactory.getLog(SearchTermSecurityRisksAction.class);
	
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
			ActionForward forward = super.execute(mapping, form, request, response);
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

		request.setAttribute("navigator.location","查询 >> 维保质量检查安全隐患项");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "searchTermSecurityRisksList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchTermSecurityRisks");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("tsrId");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String tsrId = tableForm.getProperty("tsrId");
		String tsrDetail = tableForm.getProperty("tsrDetail");
		
		String tsrIds=request.getParameter("QualityNos");
		if(tsrIds==null || tsrIds.trim().equals("")){
			tsrIds=tableForm.getProperty("QualityNos");
		}else{
			tableForm.setProperty("QualityNos", tsrIds);
		}
		
		Session hs = null;
		Query query = null;
		try {

			hs = HibernateUtil.getSession();

			String hql = "from TermSecurityRisks where enabledFlag='Y'";
			
			if(tsrIds!=null && !tsrIds.equals("")){
				hql+="and tsrId not in ("+tsrIds.replace("|", "'")+")";
			}
			if (tsrId != null && !tsrId.equals("")) {
				hql += " and tsrId like '%"+tsrId.trim()+"%'";
			}
			if (tsrDetail != null && !tsrDetail.equals("")) {
				hql += " and tsrDetail like '%"+tsrDetail.trim()+"%'";
			}
			
			//System.out.println(table.getSortColumn());
			if (table.getIsAscending()) {
				hql += " order by "+ table.getSortColumn() +" asc";
			} else {
				hql += " order by "+ table.getSortColumn() +" desc";
			}

			query = hs.createQuery(hql);
			table.setVolume(query.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List termSecurityRisksList = query.list();
			table.addAll(termSecurityRisksList);
			session.setAttribute("searchTermSecurityRisksList", table);

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
		forward = mapping.findForward("searchTermSecurityRisksList");
		
		return forward;
	}
}
