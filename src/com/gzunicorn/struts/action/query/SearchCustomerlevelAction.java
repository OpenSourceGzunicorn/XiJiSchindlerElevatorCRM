package com.gzunicorn.struts.action.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchCustomerlevelAction extends DispatchAction {

	Log log = LogFactory.getLog(SearchCustomerlevelAction.class);
	
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
		
		request.setAttribute("navigator.location","查 询>>客户等级管理");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		HTMLTableCache cache = new HTMLTableCache(session, "customerLevelSearchList");
         
		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fsearchCustomerlevel");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		cache.updateTable(table);
		table.setSortColumn("orderby");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String companyName = tableForm.getProperty("companyName");	
		String maintDivision = tableForm.getProperty("maintDivision");
		String mainStation =  tableForm.getProperty("assignedMainStation");
		Session hs = null;
		Query query=null;
		String order="";
		try {
            
			hs = HibernateUtil.getSession();
			String sql = "select c.companyName,c.principalName,c.principalPhone,vcl.Orderby orderby,c.operDate,t.maintDivision,"
					+ "c.companyId,t.billNo,t.maintStation from MaintContractMaster t "
					+ ",Customer c left join ViewCustLevel vcl on vcl.custLevel = c.custLevel where c.companyId"
					+ "=t.companyId and t.billNo in(select MAX(mcm.billNo) from MaintContractMaster mcm group by mcm.companyId) "
					+ "and vcl.Orderby is not null ";
					//+ "and t.contractStatus in('ZB','XB') ";
			
			if (companyName != null && !companyName.equals("")) {
				sql += " and c.companyName like '%" + companyName.trim() + "%'";
			}
			if (maintDivision != null && !maintDivision.equals("")) {
				sql += " and t.maintDivision like '" + maintDivision + "'";
			}
			if (mainStation != null && !mainStation.equals("")) {
				sql += " and t.maintStation like '%" + mainStation + "%'";
			}
			if (table.getIsAscending()) {
				order=" order by "+table.getSortColumn();
			} else {
				order=" order by "+table.getSortColumn()+" desc";
			}
			
			String hql="select a from Company a where a.enabledflag='Y' and comType in('0','1')  order by comid";
			List companyList=hs.createQuery(hql).list();
		    if(companyList!=null && companyList.size()>0){
		    	request.setAttribute("CompanyList", companyList);
		    }
		    if (maintDivision != null && !maintDivision.equals("")) {
				hql="select a from Storageid a,Company b where a.comid = b.comid and a.comid="+maintDivision+" and a.storagetype=1";
				List mainStationList=hs.createQuery(hql).list();
				if(mainStationList!=null && mainStationList.size()>0){
					request.setAttribute("mainStationList", mainStationList);
			    }
			}
			
			query = hs.createSQLQuery(sql+order);
			
			
			table.setVolume(query.list().size());// 查询得出数据记录数;
			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());
			cache.check(table);		
			List list=query.list();
			
			List clList= new ArrayList();
			if(list!=null && list.size()>0)
			{
				int len=list.size();
				for(int i=0;i<len;i++)
				{						
				  Object[] object = (Object[])list.get(i); 
				  HashMap map=new HashMap();
				  map.put("companyName", (String) object[0]);
				  map.put("principalName", (String) object[1]);
				  map.put("principalPhone", (String) object[2]);
				  map.put("orderby",bd.getName(hs, "ViewCustLevel", "CustLevel", "Orderby",(String) object[3]));
				  map.put("operDate", (String) object[4]);
				  map.put("maintDivision",  object[5]);
				  map.put("maintDivisionName", bd.getName(hs, "Company", "ComFullName", "ComId",(String) object[5]));
				  map.put("companyId", (String) object[6]);
				  map.put("assignedMainStation", object[8]);
				  map.put("assignedMainStationName", bd.getName(hs, "Storageid", "storagename", "storageid",(String) object[8]));
				  
				  clList.add(map);
				}		
			}				
			
			List customerLevelList =clList;
			table.addAll(customerLevelList);
    		session.setAttribute("customerLevelSearchList", table);

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (HibernateException e1) {

				e1.printStackTrace();
			} finally {
				try {
					hs.close();
					// HibernateSessionFactory.closeSession();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			forward = mapping.findForward("searchCustomerlevelList");
		
		return forward;
	}

	/**
	 * ajax 级联 分部与维保站
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public void toStorageIDList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		Session hs=null;
		String comid=request.getParameter("comid");
		response.setHeader("Content-Type","text/html; charset=GBK");
		List list2=new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='GBK'?>");
		sb.append("<root>");
		try {
			hs=HibernateUtil.getSession();
			if(comid!=null && !"".equals(comid)){
			String hql="select a from Storageid a,Company b where a.comid = b.comid and a.comid="+comid+" and a.storagetype=1 and a.parentstorageid ='0'";
			List list=hs.createQuery(hql).list();
			if(list!=null && list.size()>0){
				sb.append("<rows>");
				for(int i=0;i<list.size();i++){
				Storageid sid=(Storageid)list.get(i);
				sb.append("<cols name='"+sid.getStoragename()+"' value='"+sid.getStorageid()+"'>").append("</cols>");
				}
				sb.append("</rows>");
							
				
			  }
			 }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		finally{
			hs.close();
		}
		sb.append("</root>");
		
		response.setCharacterEncoding("gbk"); 
		response.setContentType("text/xml;charset=gbk");
		response.getWriter().write(sb.toString());
	}
	
	
	
	
}
