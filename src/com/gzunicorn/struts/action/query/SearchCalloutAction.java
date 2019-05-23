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

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.sysmanager.Company;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

import sun.misc.BASE64Decoder;

public class SearchCalloutAction extends DispatchAction {
Log log = LogFactory.getLog(SearchCalloutAction.class);
	
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

		request.setAttribute("navigator.location","查询 >> 主板类型信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "searchCalloutHmtList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchCalloutHmt");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("hmtId");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String hmtId = tableForm.getProperty("hmtId");
		String hmtName = tableForm.getProperty("hmtName");
			
		Session hs = null;
		Query query = null;
		try {
			
			hs = HibernateUtil.getSession();

			String hql = "from HotlineMotherboardType where enabledflag='Y'";
			

			if (hmtId != null && !hmtId.equals("")) {
				hql += " and hmtId like '%"+hmtId.trim()+"%'";
			}
			if (hmtName != null && !hmtName.equals("")) {
				hql += " and hmtName like '%"+hmtName.trim()+"%'";
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
			table.addAll(company);
			session.setAttribute("searchCalloutHmtList", table);

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
		forward = mapping.findForward("searchCalloutHmtList");
		
		return forward;
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord2(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("navigator.location","查询 >> 故障类型信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "searchCalloutHftList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchCalloutHft");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("hftId");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String hftId = tableForm.getProperty("hftId");
		String hftDesc = tableForm.getProperty("hftDesc");

		Session hs = null;
		Query query = null;
		try {
			
			hs = HibernateUtil.getSession();

			String hql = "from HotlineFaultType where enabledflag='Y'";
			
			if (hftId != null && !hftId.equals("")) {
				hql += " and hftId like '%"+hftId.trim()+"%'";
			}
			if (hftDesc != null && !hftDesc.equals("")) {
				hql += " and hftDesc like '%"+hftDesc.trim()+"%'";
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
			table.addAll(company);
			session.setAttribute("searchCalloutHftList", table);

		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		forward = mapping.findForward("searchCalloutHftList");
		
		return forward;
	}


	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord3(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
	
		request.setAttribute("navigator.location","查询 >> 急修电梯编号");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
	
		HTMLTableCache cache = new HTMLTableCache(session, "searchCalloutElenoList");
	
		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchCalloutEleno");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("elevatorNo");
		table.setIsAscending(true);
		cache.updateTable(table);
	
		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);
	
		String calloutMasterNo = tableForm.getProperty("calloutMasterNo");
		String elevatorNo = tableForm.getProperty("elevatorNo");
	
		Session hs = null;
		Query query = null;
		try {
			
			String companyid = tableForm.getProperty("companyId");
			if(companyid==null || companyid.equals("")){
				//使用URL解码
				companyid=URLDecoder.decode(request.getParameter("companyid"),"UTF-8");
				
				tableForm.setProperty("companyId",companyid);
			}
			
			hs = HibernateUtil.getSession();
			
			//插入急修信息,急修审核或者 急修安全经理审核
			String hql = "select distinct a.elevatorNo,isnull(c.CompanyName,a.companyId) as companyName,a.companyId "
					+ "from CalloutMaster a left join Customer c on a.CompanyID=c.CompanyID "
					+ "where (a.companyId like '%"+companyid+"%' or c.CompanyName like '%"+companyid+"%') "
					+ "and ((ISNULL(isSubSM,'N')='Y' and ISNULL(SMAuditOperid,'')<>'') "
					+ "or (ISNULL(isSubSM,'N')='N' and ISNULL(AuditOperid,'')<>'')) ";
			
			//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
			String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
			List vmlist=hs.createSQLQuery(sqlss).list();
			if(vmlist!=null && vmlist.size()>0){
				hql+=" and a.MaintStation='"+userInfo.getStorageId()+"' ";
			}
			
			if (elevatorNo != null && !elevatorNo.equals("")) {
				hql += " and elevatorNo like '%"+elevatorNo.trim()+"%'";
			}
			if (table.getIsAscending()) {
				hql += " order by a."+ table.getSortColumn() +" asc";
			} else {
				hql += " order by a."+ table.getSortColumn() +" desc";
			}
			
			//System.out.println(">>>>>"+hql);
	
			query = hs.createSQLQuery(hql);
			table.setVolume(query.list().size());// 查询得出数据记录数
	
			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());
	
			cache.check(table);
	
			List company = query.list();
			List relist=new ArrayList();
			if(company!=null && company.size()>0){
				HashMap map=null;
				for(int i=0;i<company.size();i++){
					Object[] obj=(Object[])company.get(i);
					
					map=new HashMap();
					map.put("elevatorNo", obj[0]);
					map.put("companyName", obj[1]);
					
					relist.add(map);
				}
			}
			
			
			table.addAll(relist);
			session.setAttribute("searchCalloutElenoList", table);
	
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		forward = mapping.findForward("searchCalloutElenoList");
		
		return forward;
	}
	
}
