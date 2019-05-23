package com.gzunicorn.struts.action;

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

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

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

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.sysmanager.Role;
import com.gzunicorn.hibernate.sysmanager.Workspacebaseproperty;
import com.gzunicorn.hibernate.sysmanager.Wsroleconfig;
import com.gzunicorn.hibernate.sysmanager.WsroleconfigKey;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;
 
/**
 * 代理商级别CURD处理类
 * 
 * @version 1.0
 * @author Administrator
 * 
 */
public class WorkSpaceRoleConfigAction extends DispatchAction {
	Log log = LogFactory.getLog(WorkSpaceRoleConfigAction.class);

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
			if (uri.indexOf("WorkSpaceRoleConfigActionMiniSearchAction") != -1) {
				name = "toSearchMiniRecord";
			} else {
				name = "toSearchRecord";
			}
		}
		DebugUtil.printDoBaseAction("WorkSpaceRoleConfigAction", name, "start");
		ActionForward forward = dispatchMethod(mapping, form, request,
				response, name);
		DebugUtil.printDoBaseAction("WorkSpaceRoleConfigAction", name, "end");
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
	 * Method toUpdateRecord execute,Update data to database and return list
	 * page or modifiy page 修改页面的保存方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toUpdateRecord(ActionMapping mapping, ActionForm form,
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
			String[] wsid = (String[]) dform.get("wsid");
			//先删除表中存在的数据
			String sqldel = "select a from Wsroleconfig a where a.id.roleid = '"+id+"'";
			List listdel = hs.createQuery(sqldel).list();
			if(listdel != null && listdel.size()>0){
				for(int i=0;i<listdel.size();i++){
					Wsroleconfig wsroleconfig = (Wsroleconfig)listdel.get(i);
					hs.delete(wsroleconfig);
				}
			}
			
			List wsList=new ArrayList();
			
			//去掉那些选择了重复了的。
			HashMap map=new HashMap();
			for(int i=1;i<wsid.length;i++){
				boolean flag=map.containsKey(wsid[i]);
				if(flag!=true){
					map.put(wsid[i],wsid[i]);
					wsList.add(wsid[i]);
				}
			}
			
			for(int i=0;i<wsList.size();i++){
				 String _wsid=(String)wsList.get(i);
				 //System.out.println(_wsid);
			}
			
			for(int i=0;i<wsList.size();i++){
				
				String _wsid=(String)wsList.get(i);
				
				WsroleconfigKey wsroleconfigkey = new WsroleconfigKey();
				Wsroleconfig wsroleconfig = new Wsroleconfig();
				wsroleconfigkey.setWsid(_wsid);
				wsroleconfigkey.setRoleid(id);
				wsroleconfig.setId(wsroleconfigkey);
				hs.save(wsroleconfig);
			}

			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"proc.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3);
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2);
		} catch (DataStoreException e1) {
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3);
			}
			log.error(e1.getMessage());
			DebugUtil.print(e1);
		}  finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex);
			}
		}

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					dform.set("id", dform.get("id"));
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.success"));
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
	 * Method toPrepareUpdateRecord execute,prepare data for update page
	 * 进入修改页面，准备保存前要显示的数据或页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		this.setNavigation(request, "navigator.base.workspaceroleconfig.modify");
		ActionForward forward = null;
		String id = (String) dform.get("id");
		Map map = new HashMap();
		Map map1 = null;
		java.util.List rolelist = new ArrayList();
		Session hs = null;
		List initlist = new ArrayList();
		if (id != null) {
			try {
					hs = HibernateUtil.getSession();
					String sql = "Select a From Role a where a.roleid=:levelid ";

					Query query = hs.createQuery(sql);
					query.setString("levelid", id);
					//System.out.println(">>"+query.list().size());
					rolelist = query.list();
					if(rolelist != null && rolelist.size()>0){
						Role role = (Role)rolelist.get(0);
						map.put("roleid",role.getRoleid());
						map.put("rolename",role.getRolename());
					}
					
					String sql1 = "select b from Wsroleconfig a,Workspacebaseproperty b" +
							" where a.id.roleid = '"+ id +"' and a.id.wsid = b.wsid";
					Query query1 = hs.createQuery(sql1);
					List list = query1.list();
					if(list != null && list.size()>0){
						for(int i=0;i<list.size();i++){
							map1 = new HashMap();
							Workspacebaseproperty workspacebaseproperty = (Workspacebaseproperty)list.get(i);
							map1.put("wsid",workspacebaseproperty.getWsid());
							map1.put("title",workspacebaseproperty.getTitle());
							initlist.add(map1);
						}
					}
					
				} catch (DataStoreException e) {
					log.error(e.getMessage());
					DebugUtil.print(e, "HibernateUtil Hibernate Session ");
				} catch (HibernateException e1) {
					log.error(e1.getMessage());
					DebugUtil.print(e1, "HibernateUtil Hibernate Session ");
				} finally {
					try {
						hs.close();
					} catch (HibernateException hex) {
						log.error(hex.getMessage());
						DebugUtil
								.print(hex, "HibernateUtil Hibernate Session ");
					}
				}
			}
			request.setAttribute("role",map);
			request.setAttribute("initlist",initlist);
			forward = mapping.findForward("WorkSpaceRoleConfigModify");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * Method toDeleteRecord execute, Delete record where procid = id 删除方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
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

			hs.delete("Select a from Agentlevel a where a.levelid='" + id
					+ "' ");
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
			DebugUtil.print(e2, "Hibernate Role Update error!");
		} catch (DataStoreException e1) {
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Role Update error!");

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
			log.error(e.getMessage());
			DebugUtil.print(e);
		}

		return forward;
	}


	/**
	 * Method toDisplayRecord execute,prepare data for update page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		this.setNavigation(request, "navigator.base.workspaceroleconfig.view");

		ActionForward forward = null;
		String id = (String) dform.get("id");
		java.util.List rolelist = new ArrayList();
		Session hs = null;
		List initlist = new ArrayList();
		Map map1 = null;
		Map map = new HashMap();
		if (id != null) {
			try {
					hs = HibernateUtil.getSession();
					String sql = "Select a From Role a where a.roleid=:levelid ";

					Query query = hs.createQuery(sql);
					query.setString("levelid", id);
					//System.out.println(">>"+query.list().size());
					rolelist = query.list();
					if(rolelist != null && rolelist.size()>0){
						Role role = (Role)rolelist.get(0);
						map.put("roleid",role.getRoleid());
						map.put("rolename",role.getRolename());
					}
					
					String sql1 = "select b from Wsroleconfig a,Workspacebaseproperty b" +
							" where a.id.roleid = '"+ id +"' and a.id.wsid = b.wsid";
					Query query1 = hs.createQuery(sql1);
					List list = query1.list();
					if(list != null && list.size()>0){
						for(int i=0;i<list.size();i++){
							map1 = new HashMap();
							Workspacebaseproperty workspacebaseproperty = (Workspacebaseproperty)list.get(i);
							map1.put("wsid",workspacebaseproperty.getWsid());
							map1.put("title",workspacebaseproperty.getTitle());
							initlist.add(map1);
						}
					}
					
				} catch (DataStoreException e) {
					log.error(e.getMessage());
					DebugUtil.print(e, "HibernateUtil Hibernate Session ");
				} catch (HibernateException e1) {
					log.error(e1.getMessage());
					DebugUtil.print(e1, "HibernateUtil Hibernate Session ");
				} finally {
					try {
						hs.close();
					} catch (HibernateException hex) {
						log.error(hex.getMessage());
						DebugUtil
								.print(hex, "HibernateUtil Hibernate Session ");
					}
				}

			// if display = yes then the page just show the return button;
			String type = request.getParameter("type");
			if (type != null && type.equalsIgnoreCase("mini")) {
				request.setAttribute("display", "yes");
				forward = mapping.findForward("roleMiniDisplay");
			} else {
				request.setAttribute("display", "yes");
				forward = mapping.findForward("WorkSpaceRoleConfigDisplay");
			}

			request.setAttribute("role",map);
			request.setAttribute("initlist",initlist);
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * Method toSearchRecord execute, to Excel Record 列表查询导出Excel 导出EXCEL方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException
	 */

//	public HttpServletResponse toExcelRecord(ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws IOException {
//
//		ServeTableForm tableForm = (ServeTableForm) form;
//
//		String levelid = tableForm.getProperty("levelid");
//		String levelname = tableForm.getProperty("levelname");
//		String agentlevelflag = tableForm.getProperty("agentlevelflag");
//		String enabledflag = tableForm.getProperty("enabledflag");
//
//		String sql = "Select a From Agentlevel a  Where 1=1 ";
//		String order = "";
//		if (levelid != null && levelid.length() > 0) {
//			sql += "and a.levelid like '%" + levelid.trim() + "%' ";
//		}
//		if (enabledflag != null && enabledflag.length() > 0) {
//			sql += "and a.enabledflag like '%" + enabledflag.trim() + "%' ";
//		}
//		if (levelname != null && levelname.length() > 0) {
//			sql += "and a.levelname like '%" + levelname.trim() + "%' ";
//		}
//		if (agentlevelflag != null && agentlevelflag.length() > 0) {
//			sql += "and a.agentlevelflag like '%" + agentlevelflag.trim()
//					+ "%' ";
//		}
//
//		order = "order by a.levelid asc";
//
//		Session hs = null;
//		HSSFWorkbook wb = new HSSFWorkbook();
//
//		try {
//			hs = HibernateUtil.getSession();
//
//			Query query = hs.createQuery(sql + order);
//
//			List list = query.list();
//			List rslist = new ArrayList();
//
//			if (list != null && list.size() > 0) {
//				int len = list.size();
//				for (int i = 0; i < len; i++) {
//					Agentlevel agentlevel = (Agentlevel) list.get(i);
//					HashMap map = new HashMap();
//
//					map.put("levelid", agentlevel.getLevelid());
//					map.put("levelname", agentlevel.getLevelname());
//					map.put("enabledflag", agentlevel.getEnabledflag());
//					map.put("rem1", agentlevel.getRem1());
//
//					rslist.add(map);
//				}
//			}
//
//			HSSFSheet sheet = wb.createSheet("new sheet");
//
//			Locale locale = this.getLocale(request);
//			MessageResources messages = getResources(request);
//
//			if (rslist != null && !rslist.isEmpty()) {
//				int l = rslist.size();
//				HSSFRow row0 = sheet.createRow( 0);
//				HSSFCell cell0 = row0.createCell((short) 0);
//				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
//				cell0.setCellValue(messages.getMessage(locale,
//						"agentlevel.levelid"));
//
//				cell0 = row0.createCell((short) 1);
//				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
//				cell0.setCellValue(messages.getMessage(locale,
//						"agentlevel.levelname"));
//
//				cell0 = row0.createCell((short) 2);
//				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
//				cell0.setCellValue(messages.getMessage(locale, "enabledflag"));
//
//				cell0 = row0.createCell((short) 3);
//				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
//				cell0.setCellValue(messages.getMessage(locale, "rem1"));
//
//				for (int i = 0; i < l; i++) {
//					HashMap map = (HashMap) rslist.get(i);
//					// 创建Excel行，从0行开始
//					HSSFRow row = sheet.createRow( i + 1);
//
//					// 创建Excel列
//					HSSFCell cell = row.createCell((short) 0);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					cell.setCellValue((String) map.get("levelid"));
//
//					cell = row.createCell((short) 1);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					cell.setCellValue((String) map.get("levelname"));
//
//					cell = row.createCell((short) 2);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					cell.setCellValue((String) map.get("enabledflag"));
//
//					cell = row.createCell((short) 3);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					cell.setCellValue((String) map.get("rem1"));
//				}
//			}
//		} catch (DataStoreException e) {
//			e.printStackTrace();
//		} catch (HibernateException e1) {
//			e1.printStackTrace();
//		} finally {
//			try {
//				hs.close();
//			} catch (HibernateException hex) {
//				log.error(hex.getMessage());
//				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
//			}
//		}
//		response.setContentType("application/vnd.ms-excel");
//		response.setHeader("Content-disposition",
//				"offline; filename=AgentLevel.xls");
//		wb.write(response.getOutputStream());
//		return response;
//	}

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
		this.setNavigation(request, "navigator.base.workspaceroleconfig.list");
		ActionForward forward = null;
		// listRecord
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {

				//response = toExcelRecord(form, request, response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "roleList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fworkspaceroleconfig");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("roleid");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String rolename = tableForm.getProperty("rolename");
			String roleid = tableForm.getProperty("roleid");
			String enabledflag = tableForm.getProperty("enabledflag");
			Session hs = null;

			try {

				hs = HibernateUtil.getSession();

				Criteria criteria = hs.createCriteria(Role.class);
				if (roleid != null && !roleid.equals("")) {
					criteria.add(Expression.like("roleid", "%" + roleid.trim()
							+ "%"));
				}
				if (rolename != null && !rolename.equals("")) {
					criteria.add(Expression.like("rolename", "%"
							+ rolename.trim() + "%"));
				}
				if (enabledflag != null && !enabledflag.equals("")) {
					criteria.add(Expression.eq("enabledflag", enabledflag));
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

				List role = criteria.list();
				BaseDataImpl bd = new BaseDataImpl();

				if (role != null) {
					int len = role.size();
					for (int i = 0; i < len; i++) {
						Role r = (Role) role.get(i);
						r.setModuleid(bd.getName("Module", "modulename",
								"moduleid", r.getModuleid()));
					}
				}

				table.addAll(role);
				session.setAttribute("roleList", table);

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

			forward = mapping.findForward("WorkSpaceRoleConfigList");
		}
		return forward;
	}

}