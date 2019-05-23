package com.gzunicorn.struts.action.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchLostElevatorMaintainAction extends DispatchAction {

	Log log = LogFactory.getLog(SearchLostElevatorMaintainAction.class);
	
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

		request.setAttribute("navigator.location","查询 >> 丢梯客户信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "searchLostElevatorList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fLostElevator");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("companyId");
		table.setIsAscending(true);
		cache.updateTable(table);
		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);
		String contacts = tableForm.getProperty("contacts");
		String contactPhone = tableForm.getProperty("contactPhone");
		String companyName = tableForm.getProperty("companyName");
		String enabledFlag = tableForm.getProperty("enabledFlag");
		Session hs = null;
		Query query = null;
		try {

			hs = HibernateUtil.getSession();

			String hql="select distinct a.Contacts,a.ContactPhone,b.CompanyID,c.CompanyName "
						+"from LostElevatorReport a,MaintContractMaster b,Customer c "
						+"where a.BillNo=b.BillNo and b.CompanyID=c.CompanyID "
						+ "and a.ContactPhone not in(select ContactPhone from LostElevatorMaintain) "
						+ "and a.AuditStatus='Y'";
			
			if (contacts != null && !contacts.equals("")) {
				hql += " and a.contacts like '%"+contacts.trim()+"%'";
			}
			if (contactPhone != null && !contactPhone.equals("")) {
				hql += " and a.contactPhone like '%"+contactPhone.trim()+"%'";
			}
			if (companyName != null && !companyName.equals("")) {
				hql += " and c.companyName like '%"+companyName.trim()+"%'";
			}
			if (enabledFlag != null && !enabledFlag.equals("")) {
				hql += " and enabledFlag = '%"+enabledFlag.trim()+"%'";
			}
		
			
			if (table.getIsAscending()) {
				hql += " order by "+ table.getSortColumn() +" asc";
			} else {
				hql += " order by "+ table.getSortColumn() +" desc";
			}

			query = hs.createSQLQuery(hql);
			table.setVolume(query.list().size());// 查询得出数据记录数;
			
			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			List searchLostElevatorList = new ArrayList(); 
			List list= query.list();
			if(list!=null&&list.size()>0){
				
			for(int i=0;i<list.size();i++){
				Object[] object = (Object[])list.get(i); 
				
				Map map=new HashMap();
				map.put("contacts", object[0]);
				map.put("contactPhone", object[1]);
				map.put("companyId", object[2]);
				map.put("companyName", object[3]);
				searchLostElevatorList.add(map);
			}
			}

			cache.check(table);
			table.addAll(searchLostElevatorList);
			session.setAttribute("searchLostElevatorList", table);

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
		forward = mapping.findForward("searchLostElevatorList");
		
		return forward;
	}

}
