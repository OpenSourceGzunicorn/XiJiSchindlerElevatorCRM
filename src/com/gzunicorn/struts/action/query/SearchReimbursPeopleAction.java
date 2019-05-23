package com.gzunicorn.struts.action.query;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.sysmanager.Company;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchReimbursPeopleAction extends DispatchAction {
Log log = LogFactory.getLog(SearchReimbursPeopleAction.class);
	
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

		request.setAttribute("navigator.location","查询 >> 报销人信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		HTMLTableCache cache = new HTMLTableCache(session, "searchReimbursPeopleList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchReimbursPeople");
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
		String storage = tableForm.getProperty("storage");
		String user=tableForm.getProperty("user");
		Session hs = null;
		Query query = null;
		try {
			
			hs = HibernateUtil.getSession();

			String hql = "select l.* from ViewParentStorageLogin l where 1=1 ";
			
			
			if (comid != null && !comid.equals("")) {
				hql += " and comid like '%"+comid.trim()+"%'";
			}
			if (storage != null && !storage.equals("")) {
				hql += " and (newStorageId like '%"+storage.trim()+"%' or storagename like '%"+storage.trim()+"%')";
			}
			if (user != null && !user.equals("")) {
				hql += " and (userid like '%"+user.trim()+"%' or username like '%"+user.trim()+"%')";
			}
			
			if (table.getIsAscending()) {
				hql += " order by "+ table.getSortColumn() +" asc";
			} else {
				hql += " order by "+ table.getSortColumn() +" desc";
			}

			query = hs.createSQLQuery(hql);
			table.setVolume(query.list().size());// 查询得出数据记录数

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List list = query.list();
			List company=new ArrayList();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Object[] objects=(Object[]) list.get(i);
					HashMap map=new HashMap();
					map.put("userId", objects[0]);
					map.put("userName", objects[1]);
					map.put("comId",objects[2]);
					map.put("comName", objects[3]);
					map.put("storageId", objects[4]);
					map.put("storageName", objects[5]);
					company.add(map);
				}
			}
			table.addAll(company);
			request.setAttribute("maintDivisionList", Grcnamelist1.getgrcnamelist("admin"));
			session.setAttribute("searchReimbursPeopleList", table);

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
		forward = mapping.findForward("searchReimbursPeopleList");
		
		return forward;
	}
}
