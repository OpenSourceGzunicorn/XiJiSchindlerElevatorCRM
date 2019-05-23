package com.gzunicorn.struts.action.query;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.gzunicorn.hibernate.sysmanager.Company;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchCompanyAction extends DispatchAction {
Log log = LogFactory.getLog(SearchCompanyAction.class);
	
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

		request.setAttribute("navigator.location","查询 >> 部门信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "searchCompanyList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchCompany");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("comid");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String comid = tableForm.getProperty("comid");
		String comfullname = tableForm.getProperty("comfullname");
		
		String mtIds=request.getParameter("QualityNos");
		
		if(mtIds==null || mtIds.trim().equals("")){
			mtIds=tableForm.getProperty("QualityNos");
		}else{
			tableForm.setProperty("QualityNos", mtIds);
		}
		
		
		Session hs = null;
		Query query = null;
		try {
			
			hs = HibernateUtil.getSession();

			String hql = "from Company where enabledflag='Y'";
			
			
			if(mtIds!=null && !mtIds.equals("")){
				mtIds=URLDecoder.decode(mtIds, "utf-8");
				hql+=" and comid not in ("+mtIds.replace("|", "'")+")";
			}
			if (comid != null && !comid.equals("")) {
				hql += " and comid like '%"+comid.trim()+"%'";
			}
			if (comfullname != null && !comfullname.equals("")) {
				hql += " and comfullname like '%"+comfullname.trim()+"%'";
			}
			
			if (table.getIsAscending()) {
				hql += " order by "+ table.getSortColumn() +" asc";
			} else {
				hql += " order by "+ table.getSortColumn() +" desc";
			}

			query = hs.createQuery(hql);
			table.setVolume(query.list().size());// 查询得出数据记录数

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List company = query.list();
			BaseDataImpl bd = new BaseDataImpl();
			if(company !=null){
				for(int i=0;i<company.size();i++)
				{
					Company cp=(Company)company.get(i);
					cp.setComtype(bd.getName("Viewcompanytype","comtypename","comtypeid",cp.getComtype()));
				}	
			}
			

			table.addAll(company);
			session.setAttribute("searchCompanyList", table);

		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (HibernateException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		forward = mapping.findForward("searchCompanyList");
		
		return forward;
	}
	
	
	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord2(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("navigator.location","查询 >> 部门信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "searchCompanyList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchCompany2");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("comid");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String comid = tableForm.getProperty("comid");
		String comfullname = tableForm.getProperty("comfullname");
		
		String srt = request.getParameter("srt");
		
		if(srt == null ){
			srt=tableForm.getProperty("srt");
		}else{
			tableForm.setProperty("srt",srt);
		}
		
		
		Session hs = null;
		Query query = null;
		try {
			
			hs = HibernateUtil.getSession();

			String hql = "from Company where enabledflag='Y'";
			
			if(srt != null && !srt.equals("")){
				if(srt.trim().equals("mdr")){
					hql += " and comtype in('0','1')";
				}else if(srt.trim().equals("nrm")){
					hql += " and comtype not in('0','1')";
				}
			}
			
			if (comid != null && !comid.equals("")) {
				hql += " and comid like '%"+comid.trim()+"%'";
			}
			if (comfullname != null && !comfullname.equals("")) {
				hql += " and comfullname like '%"+comfullname.trim()+"%'";
			}
			
			if (table.getIsAscending()) {
				hql += " order by "+ table.getSortColumn() +" asc";
			} else {
				hql += " order by "+ table.getSortColumn() +" desc";
			}

			query = hs.createQuery(hql);
			table.setVolume(query.list().size());// 查询得出数据记录数

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List company = query.list();
			BaseDataImpl bd = new BaseDataImpl();
			if(company !=null){
				for(int i=0;i<company.size();i++)
				{
					Company cp=(Company)company.get(i);
					cp.setComtype(bd.getName("Viewcompanytype","comtypename","comtypeid",cp.getComtype()));
				}	
			}
			

			table.addAll(company);
			session.setAttribute("searchCompanyList", table);

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		forward = mapping.findForward("searchCompanyList");
		
		return forward;
	}
	
}
