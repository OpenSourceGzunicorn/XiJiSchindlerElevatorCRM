
package com.gzunicorn.struts.action.query;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

/*import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.City;
import com.gzunicorn.hibernate.basedata.Basedatalist;
import com.gzunicorn.hibernate.basedata.Equipmentproperty;
import com.gzunicorn.hibernate.basedata.Viewgetbasedatalisttype;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;
*/



import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.logic.DataOperation;
import com.gzunicorn.common.logic.MakeUpXML;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * 设备属性信息表表操作类
 * 
 * @version 1.0
 * @author Administrator
 * 
 */
public class SearchWorkSpaceBasePropertySelectAction extends DispatchAction {
	/**
	 * 日志类对象
	 */
	Log log = LogFactory.getLog(SearchWorkSpaceBasePropertySelectAction.class);

	/**
	 * 基础数据操作对象
	 */
	BaseDataImpl bd = new BaseDataImpl();

	/**
	 * Method execute 由 Struts-config.XML 跳转过来; 判断 执行小页面查询 还是 大页面查询； 用户权限控制；
	 * 后台调试： 打印执行的方法
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
				SysRightsUtil.NODE_ID_FORWARD + "workSpaceRoleConfig", null);
		/** **********结束用户权限过滤*********** */

		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			String uri = request.getRequestURI();
			if (uri.indexOf("SearchWorkSpaceBasePropertySelectMiniSearchAction") != -1) {
				name = "toSearchMiniRecord";
			} else {
				name = "toSearchRecord";
			}
		}
		DebugUtil.printDoBaseAction("SearchWorkSpaceBasePropertySelectAction", name, "start");
		ActionForward forward = dispatchMethod(mapping, form, request,
				response, name);
		DebugUtil.printDoBaseAction("SearchWorkSpaceBasePropertySelectAction", name, "end");
		return forward;
	}

	/**
	 * Get the navigation description from the properties file by navigation
	 * key; 导航条
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
	 * Method toSearchRecord execute, Search record 查询页面，显示数据，可根据条件进行相应的查询
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		this.setNavigation(request, "navigator.base.workspacebasefitsearch.list");
		
		ActionForward forward = null;
		// listRecord
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		
			HTMLTableCache cache = new HTMLTableCache(session, "workspacebasefitList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fsearchworkspacebasepropertylist");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			table.setSortColumn("a.wsid");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String wsid = tableForm.getProperty("wsid");
			String wskey = tableForm.getProperty("wskey");
			String title = tableForm.getProperty("title");
			String link = tableForm.getProperty("link");
			String tip = tableForm.getProperty("tip");
			String enabledflag = tableForm.getProperty("enabledflag");
			
			String sql0 = "select count(*) ";
			String sql1 = "select a ";
			String sql = "From Workspacebaseproperty a Where enabledflag='Y' ";
			String order = "";
			if (wsid != null && wsid.length() > 0) {
				sql += "and a.wsid like '%" + wsid.trim() + "%' ";
			}
			if (enabledflag != null && enabledflag.length() > 0) {
				sql += "and a.enabledflag like '%" + enabledflag.trim() + "%' ";
			}
			if (wskey != null && wskey.length() > 0) {
				sql += "and a.wskey like '%" + wskey.trim() + "%' ";
			}
			if (title != null && title.length() > 0) {
				sql += "and a.title like '%" + title.trim() +"%' ";
			}
			if (tip != null && tip.length() > 0) {
				sql += "and a.tip like '%" + tip.trim() +"%' ";
			}
			if (link != null && link.length() > 0) {
				sql += "and a.link like '%" + link.trim() +"%' ";
			}

			if (table.getIsAscending()) {
				order = "order by " + table.getSortColumn();
			} else {
				order = "order by " + table.getSortColumn() + " desc";
			}
			Session hs = null;

			try {
				
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("select count(*) From Workspacebaseproperty ");
				
				List listq = query.list();
				if(listq!= null && listq.size() > 0){
					table.setVolume(Integer.parseInt(listq.get(0).toString()));// 查询得出数据记录数;
				}else{
					table.setVolume(0);// 查询得出数据记录数;
				}

				// 得出上一页的最后一条记录数号;
				query = hs.createQuery(sql1 + sql + order);
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();

				table.addAll(list);
				

				session.setAttribute("workspacebasefitList", table);

			} catch (DataStoreException e) {
				log.error(e.getMessage());
				DebugUtil.print(e);
			} catch (HibernateException e1) {
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

			forward = mapping.findForward("searchWorkSpaceBasePropertyList");
		
		return forward;
	}
}