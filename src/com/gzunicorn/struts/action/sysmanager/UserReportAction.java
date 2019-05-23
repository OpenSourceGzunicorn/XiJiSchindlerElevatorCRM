//Created by MyEclipse Struts
//XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl
//朱伟吉


package com.gzunicorn.struts.action.sysmanager;

import java.io.IOException;
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


import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.logic.BaseDataImpl;
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
* MyEclipse Struts Creation date: 08-09-2005
* 
* XDoclet definition:
* 
* @struts:action path="/cityAction" name="cityForm" scope="request"
*                validate="true"
*/
public class UserReportAction extends DispatchAction {

	Log log = LogFactory.getLog(UserReportAction.class);

	// --------------------------------------------------------- Instance
	// Variables

	// --------------------------------------------------------- Methods

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

		/************开始用户权限过滤************/
		SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "onLineUser",null);
		/************结束用户权限过滤************/
		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		////System.out.println(name);
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			ActionForward forward = super.execute(mapping, form, request,
					response);
			////System.out.println(forward.getName());
			return forward;
		}

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
	public ActionForward onLineCheckRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		this.setNavigation(request, "navigator.base.useronline.list");
		
		HttpSession httpSession = request.getSession();
		
		List AllUserInfo = (ArrayList) httpSession.getServletContext().getAttribute(SysConfig.ALL_LOGIN_USER_INFO);
		
		int l = 0;
		
		if(AllUserInfo != null)
		{
			l = AllUserInfo.size();
			//System.out.println("hello");
			request.setAttribute("userList",AllUserInfo);
		}
		request.setAttribute("count",String.valueOf(l));
		return mapping.findForward("logincheck");
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

	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		this.setNavigation(request, "navigator.base.shopusereport.useshoplist");
		ActionForward forward = null;
		// listRecord
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {

				response = toExcelRecord(form, request, response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			
			HTMLTableCache cache = new HTMLTableCache(session, "shopList");
			
			DefaultHTMLTable table = new DefaultHTMLTable();

			table.setMapping("fUserReport");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
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

			Session hs = null;

			try {

				hs = HibernateUtil.getSession();

				String sql = "select distinct vli.userid , vli.username ,vli.rolename from Viewloginuserinfo as vli";
				
				Query query = hs.createQuery(sql);

				if(query.list() != null)
				{
				table.setVolume(query.list().size());// 查询得出数据记录数;
				//得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				
				
				List result = query.list();
				int l = result.size();
				BaseDataImpl bd = new BaseDataImpl();
				for(int i=0;i<l;i++)
				{
					Object[] obj = (Object[])result.get(i);
					Map map = new HashMap();
					map.put("userid",obj[0].toString());
					map.put("username",obj[1].toString());
					map.put("roleName",obj[2].toString());
					//map.put("areaname",bd.getName("Salearea","areaname","areaid",obj[2].toString()));
					table.add(map);
				}
				
				request.setAttribute("hadusercount",String.valueOf(table.getVolume()));
				
				}else
				{
					table.setVolume(0);// 查询得出数据记录数;
				}
				
				cache.check(table);
				session.setAttribute("userList", table);

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
			request.setAttribute("userlist","Y");
			forward = mapping.findForward("userreport");
		}
		return forward;
	}

	/**
	 * Method toSearchRecord execute, to Excel Record 列表查询导出Excel
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException
	 */

	public HttpServletResponse toExcelRecord(ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String naigation = new String();
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		Session hs = null;
		HSSFWorkbook wb = new HSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			String sql = "select distinct vli.userID , vli.username ,vli.rolename  from Viewloginuserinfo as vli";
			
			
			Query query = hs.createQuery(sql);
			List List = query.list();

			HSSFSheet sheet = wb.createSheet("new sheet");

			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (List != null && !List.isEmpty()) {
				int l = List.size();
				HSSFRow row0 = sheet.createRow( 0);
				HSSFCell cell0 = null;
				cell0 = row0.createCell((short) 0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale, "shop.shopid"));

				cell0 = row0.createCell((short) 1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"shop.shopname"));

				cell0 = row0.createCell((short) 2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0
						.setCellValue(messages.getMessage(locale,
								"haduseshop.areaid"));

				BaseDataImpl bd = new BaseDataImpl();
				for (int i = 0; i < l; i++) {
					Object[] obj = (Object[]) List.get(i);
					HSSFRow row = sheet.createRow( i + 1);
					HSSFCell cell = null;
					cell = row.createCell((short) 0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj[0].toString());

					cell = row.createCell((short) 1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(obj[1].toString());

					cell = row.createCell((short) 2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName("Salearea","areaname","areaid",obj[2].toString()));
				}

			}
			try {
			} catch (Exception e) {

			}
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
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "offline; filename=city.xls");
		wb.write(response.getOutputStream());
		return response;
	}

}

