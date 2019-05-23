package com.gzunicorn.struts.action.query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.elevatorsales.ElevatorSalesInfo;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchStaffAction extends DispatchAction {

	Log log = LogFactory.getLog(SearchStaffAction.class);
	
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
		
		request.setAttribute("navigator.location","查找 >> 厂检员信息");
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ActionForward forward = null;
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		HTMLTableCache cache = new HTMLTableCache(session, "searchStaffList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchStaff");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("userid");
		table.setIsAscending(true);
		cache.updateTable(table);
		
		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else { 
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String username = tableForm.getProperty("username");
		String userid = tableForm.getProperty("userid");	
		
			
		Session hs = null; 
		Query query = null;
		List grclist1=new ArrayList();
		try {

			hs = HibernateUtil.getSession();
			
			Criteria criteria = hs.createCriteria(Loginuser.class);
			
			criteria.add(Expression.like("roleid", "A51"));
			criteria.add(Expression.like("enabledflag", "Y"));
			if (userid != null && !userid.equals("")) {
				criteria.add(Expression.like("userid", "%" + userid.trim() + "%"));
			}
			if (username != null && !username.equals("")) {
				criteria.add(Expression.like("username", "%" + username.trim()+ "%"));
			}
			if (table.getIsAscending()) {
				criteria.addOrder(Order.asc(table.getSortColumn()));
			} else {
				criteria.addOrder(Order.desc(table.getSortColumn()));
			}

			table.setVolume(criteria.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			criteria.setFirstResult(table.getFrom()); // pagefirst
			criteria.setMaxResults(table.getLength());

			cache.check(table);

			List searchStaffList = criteria.list();
			if(searchStaffList!=null && searchStaffList.size()>0){
				for(int i=0;i<searchStaffList.size();i++){
					Loginuser r = (Loginuser) searchStaffList.get(i);
					r.setRoleid(bd.getName("Role", "rolename","roleid", r.getRoleid()));
					r.setStorageid(bd.getName("Storageid","storagename","storageid",r.getStorageid()));
					r.setClass1id(bd.getName("Class1", "class1name", "class1id", r.getClass1id()));
					String sql2="select grcname from view_LoginUser_grtype where grcid= '"+r.getGrcid()+"'";
                    List list2=hs.createSQLQuery(sql2).list();
                    if(list2.size()>0){
                    r.setGrcid(list2.get(0).toString());
                    }else{
                    r.setGrcid("");
                    }
				}
			}

			table.addAll(searchStaffList);
			request.setAttribute("id", "staffName");
			request.setAttribute("phone", "staffLinkPhone");
			session.setAttribute("searchStaffList", table);
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		forward = mapping.findForward("searchStaffList");
		
		return forward;
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord2(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("navigator.location","查找 >> 督察人员信息");
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ActionForward forward = null;
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		HTMLTableCache cache = new HTMLTableCache(session, "searchStaffList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchStaff2");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("userid");
		table.setIsAscending(true);
		cache.updateTable(table);
		
		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else { 
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String username = tableForm.getProperty("username");
		String userid = tableForm.getProperty("userid");	
		
			
		Session hs = null; 
		Query query = null;
		List grclist1=new ArrayList();
		try {

			hs = HibernateUtil.getSession();
			
			Criteria criteria = hs.createCriteria(Loginuser.class);
			criteria.add(Expression.like("roleid", "A12"));
			criteria.add(Expression.like("enabledflag", "Y"));
			if (userid != null && !userid.equals("")) {
				criteria.add(Expression.like("userid", "%" + userid.trim() + "%"));
			}
			if (username != null && !username.equals("")) {
				criteria.add(Expression.like("username", "%" + username.trim()+ "%"));
			}
			if (table.getIsAscending()) {
				criteria.addOrder(Order.asc(table.getSortColumn()));
			} else {
				criteria.addOrder(Order.desc(table.getSortColumn()));
			}

			table.setVolume(criteria.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			criteria.setFirstResult(table.getFrom()); // pagefirst
			criteria.setMaxResults(table.getLength());

			cache.check(table);

			List searchStaffList = criteria.list();
			if(searchStaffList!=null && searchStaffList.size()>0){
				for(int i=0;i<searchStaffList.size();i++){
					Loginuser r = (Loginuser) searchStaffList.get(i);
					r.setRoleid(bd.getName("Role", "rolename","roleid", r.getRoleid()));
					r.setStorageid(bd.getName("Storageid","storagename","storageid",r.getStorageid()));
					r.setClass1id(bd.getName("Class1", "class1name", "class1id", r.getClass1id()));
					String sql2="select grcname from view_LoginUser_grtype where grcid= '"+r.getGrcid()+"'";
                    List list2=hs.createSQLQuery(sql2).list();
                    if(list2.size()>0){
                    r.setGrcid(list2.get(0).toString());
                    }else{
                    r.setGrcid("");
                    }
				}
			}

			table.addAll(searchStaffList);
			request.setAttribute("id", "superviseId");
			request.setAttribute("phone", "supervisePhone");
			session.setAttribute("searchStaffList", table);
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
		forward = mapping.findForward("searchStaffList");
		
		return forward;
	}
	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord3(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("navigator.location","查找 >> 维保工");
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ActionForward forward = null;
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		HTMLTableCache cache = new HTMLTableCache(session, "searchStaffList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchStaff3");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("userid");
		table.setIsAscending(true);
		cache.updateTable(table);
		
		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else { 
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String username = tableForm.getProperty("username");
		String userid = tableForm.getProperty("userid");
		String mainStation=request.getParameter("mainStation");
		String type=request.getParameter("type");
		
		if(mainStation == null || mainStation.trim().equals("")){
			mainStation=tableForm.getProperty("mainStation");
		}else{
			tableForm.setProperty("mainStation",mainStation);
		}
		
		Session hs = null; 
		Query query = null;
		List grclist1=new ArrayList();
		try {

			hs = HibernateUtil.getSession();
			
			Criteria criteria = hs.createCriteria(Loginuser.class);
			criteria.add(Expression.like("roleid", "A50"));
			criteria.add(Expression.like("enabledflag", "Y"));
			criteria.add(Expression.like("storageid", mainStation.trim()+"%"));
			if (userid != null && !userid.equals("")) {
				criteria.add(Expression.like("userid", "%" + userid.trim() + "%"));
			}
			if (username != null && !username.equals("")) {
				criteria.add(Expression.like("username", "%" + username.trim()+ "%"));
			}
			if (table.getIsAscending()) {
				criteria.addOrder(Order.asc(table.getSortColumn()));
			} else {
				criteria.addOrder(Order.desc(table.getSortColumn()));
			}

			table.setVolume(criteria.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			criteria.setFirstResult(table.getFrom()); // pagefirst
			criteria.setMaxResults(table.getLength());

			cache.check(table);

			List searchStaffList = criteria.list();
			if(searchStaffList!=null && searchStaffList.size()>0){
				for(int i=0;i<searchStaffList.size();i++){
					Loginuser r = (Loginuser) searchStaffList.get(i);
					r.setRoleid(bd.getName("Role", "rolename","roleid", r.getRoleid()));
					r.setStorageid(bd.getName("Storageid","storagename","storageid",r.getStorageid()));
					r.setClass1id(bd.getName("Class1", "class1name", "class1id", r.getClass1id()));
					String sql2="select grcname from view_LoginUser_grtype where grcid= '"+r.getGrcid()+"'";
                    List list2=hs.createSQLQuery(sql2).list();
                    if(list2.size()>0){
                    r.setGrcid(list2.get(0).toString());
                    }else{
                    r.setGrcid("");
                    }
				}
			}

			table.addAll(searchStaffList);
			if(type!=null&&!"".equals(type)){
				request.setAttribute("id", "turnSendId");
				request.setAttribute("phone", "turnSendTel");
			}else
			{
				request.setAttribute("id", "receivingPerson");
				request.setAttribute("phone", "receivingPhone");
			}
			
			session.setAttribute("searchStaffList", table);
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
		forward = mapping.findForward("searchStaffList");
		
		return forward;
	}

}
