package com.gzunicorn.struts.action.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/** 
* MyEclipse Struts
* Creation date: 08-18-2005
* 
* XDoclet definition:
* @struts:action validate="true"
*/
public class SearchViewLoginUserInfoAction extends DispatchAction {
	Log log = LogFactory.getLog(SearchViewLoginUserInfoAction.class);
	BaseDataImpl bd = new BaseDataImpl();

	// --------------------------------------------------------- Instance Variables

	// --------------------------------------------------------- Methods
	
	
	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)throws Exception {
		
		/************开始用户权限过滤************/
		//SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "quotePre",null);
		/************结束用户权限过滤************/
		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			
		}
		DebugUtil.printDoBaseAction("SearchViewLoginUserInfoAction",name,"start");
		ActionForward forward= dispatchMethod(mapping, form, request, response, name);
		DebugUtil.printDoBaseAction("SearchViewLoginUserInfoAction",name,"end");
		return forward;
	}

	/**
	 * Method toSearchRecord execute, Search record
	 * 查询页面，显示数据，可根据条件进行相应的查询 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		this.setNavigation(request, "navigator.base.searchviewloginuserinfo.list");
		ActionForward forward = null;
		// listRecord
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				
				//response = toExcelRecord(form,request,response);
				//forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "searchViewLoginUserInfoList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fSearchViewLoginUserInfo");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			table.setSortColumn("a.userID");
			table.setIsAscending(true);
			cache.updateTable(table);
			
			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String userid = tableForm.getProperty("userid");
			String username = tableForm.getProperty("username");

			String sql0="Select distinct count(a.userID) ";
			String sql1="Select a ";
			String sql="From ViewLoginUserInfo a " +
			           "Where 1=1 " +
			           "and a.enabledFlag='Y' ";
			String order="";
			if(userid!=null && userid.length()>0){
				sql+="and a.userID like '%"+userid.trim()+"%' ";
			}
			if(username!=null && username.length()>0){
				sql+="and a.userName like '%"+username.trim()+"%' ";
			}
			
			if (table.getIsAscending()) {
				order="order by "+table.getSortColumn();
			} else {
				order="order by "+table.getSortColumn()+" desc";
			}
			Session hs = null;

			try {

				hs = HibernateUtil.getSession();
				
				Query query = hs.createQuery(sql0+sql);
				
				int count=Integer.parseInt(query.list().get(0).toString());
				table.setVolume(count);// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query = hs.createQuery(sql1+sql+order);
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				
				List rslist=new ArrayList();
					
				if(list!=null && list.size()>0){
					int len=list.size();
					ViewLoginUserInfo viewLoginUserInfo=null;
					HashMap map;
					for(int i=0;i<len;i++){
						//Object[] obj=(Object[])list.get(i);
						viewLoginUserInfo=(ViewLoginUserInfo)list.get(i);
						map=new HashMap();
						
						map.put("userid",viewLoginUserInfo.getUserID());
						map.put("username",viewLoginUserInfo.getUserName());
						map.put("enabledflag",viewLoginUserInfo.getEnabledFlag());
						map.put("rem1",viewLoginUserInfo.getRem1());
						map.put("rolename",viewLoginUserInfo.getRoleName());
						
						rslist.add(map);
					}
				}
				
				table.addAll(rslist);
				session.setAttribute("searchViewLoginUserInfoList", table);

			} catch (HibernateException e1) {
				log.error(e1.getMessage());
				DebugUtil.print(e1);
			} catch (Exception e1) {
				log.error(e1.getMessage());
				DebugUtil.print(e1);
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}

			forward = mapping.findForward("searchViewLoginUserInfoList");
		}
		
		return forward;
	}

	/**
	 * Get the navigation description from the properties file by navigation key;
	 * @param request
	 * @param navigation
	 */

	private void setNavigation(HttpServletRequest request, String navigation) {
		Locale locale = this.getLocale(request);
		MessageResources messages = getResources(request);
		request.setAttribute("navigator.location", messages.getMessage(locale,
				navigation));
	}
}