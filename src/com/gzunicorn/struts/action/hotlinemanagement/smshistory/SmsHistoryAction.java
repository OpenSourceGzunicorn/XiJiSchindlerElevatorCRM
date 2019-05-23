package com.gzunicorn.struts.action.hotlinemanagement.smshistory;

import java.io.IOException;
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
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SmsHistoryAction extends DispatchAction {

	Log log = LogFactory.getLog(SmsHistoryAction.class);
	
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


		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		/*String authority="smshistory";
		if(name != null && name.contains("Audit")){
			authority = "smshistoryaudit";
		}*/
		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "smshistory", null);
		/** **********结束用户权限过滤*********** */
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
		
		request.setAttribute("navigator.location","短信历史信息 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form,request,response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport","");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "smsHistoryList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fSmsHistory");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("id");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			}else if(action.equals("Submit")) {
				cache.loadForm(tableForm);
			}else{
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String smsTel = tableForm.getProperty("smsTel");//短信发送号码
			String flag1 =tableForm.getProperty("flag");//是否发送成功
			String smsSendTime=tableForm.getProperty("smsSendTime");
			String smsSendTime2=tableForm.getProperty("smsSendTime2");
			if(smsSendTime==null || smsSendTime.trim().equals("")){
				smsSendTime="2010-01-01";
			}
			if(smsSendTime2==null || smsSendTime2.trim().equals("")){
				smsSendTime2="9999-99-99";
			}
			
			Integer flag=null;
			if(flag1!=null && !"".equals(flag1))
				flag=Integer.valueOf(flag1);
			
			Session hs = null;
			Query query = null;
			try {

				hs = HibernateUtil.getSession();

				String sql = "select a from SmsHistory a";
				
				String conditon=" where a.smsSendTime>='"+smsSendTime+" 00:00:00' and a.smsSendTime<='"+smsSendTime2+" 99:99:99'";
				
				if (smsTel != null && !smsTel.equals("")) {
					if(conditon!=null && !"".equals(conditon))
						conditon += " and a.smsTel like '%"+smsTel.trim()+"%'";
					else
						conditon=" where a.smsTel like '%"+smsTel.trim()+"%'";
				}
				if (flag != null) {
					if(conditon!=null && !"".equals(conditon))
						conditon += " and a.flag="+flag;
					else
						conditon=" where a.flag="+flag;
				}
//				if(smsSendTime!=null && !"".equals(smsSendTime)){
//					if(conditon!=null && !"".equals(conditon))
//						conditon+=" and a.smsSendTime like '%"+smsSendTime.trim()+"%'";
//					else
//						conditon=" where a.smsSendTime like '%"+smsSendTime.trim()+"%'";
//				}
				if (table.getIsAscending()) {
					conditon += " order by a."+ table.getSortColumn() +" desc";
				} else {
					conditon += " order by a."+ table.getSortColumn() +" asc";
				}
				
				//System.out.println(">>>>>"+sql+conditon);
				
				query = hs.createQuery(sql+conditon);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				

				table.addAll(list);
				session.setAttribute("smsHistoryList", table);
				

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
			forward = mapping.findForward("smsHistoryList");
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
		
		return response;
	}
}	