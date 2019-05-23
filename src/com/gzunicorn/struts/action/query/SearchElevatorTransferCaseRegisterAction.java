package com.gzunicorn.struts.action.query;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.elevatorsales.ElevatorSalesInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchElevatorTransferCaseRegisterAction extends DispatchAction {

	Log log = LogFactory.getLog(SearchElevatorTransferCaseRegisterAction.class);
	
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
	
	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
		
				
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		HTMLTableCache cache = new HTMLTableCache(session, "searchElevatorTransferCaseRegisterList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchElevatorTransferCaseRegister");
		//table.setLength(20);
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("elevatorNo");
		table.setIsAscending(true);
		cache.updateTable(table);
		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		}else if (action.equals("Submit")) {
			cache.loadForm(tableForm);
		}else { 
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String elevatorNo = tableForm.getProperty("elevatorNo"); //电梯编号
		String elevatorType = tableForm.getProperty("elevatorType"); //电梯类型
		String isOutsideFootball = tableForm.getProperty("isOutsideFootball"); //是否外揽
		String deliveryAddress=tableForm.getProperty("deliveryAddress");
		String idType=request.getParameter("idType");
		if(idType==null||"".equals(idType))
		{
			idType=(String) session.getAttribute("idType");
		}
		String elevatorNos=request.getParameter("QualityNos");
		if(elevatorNos==null || "".equals(elevatorNos)){
			elevatorNos=tableForm.getProperty("elevatorNos");
		}else{
			tableForm.setProperty("elevatorNos", elevatorNos);
		}
		
		
		Session hs = null; 
		Query query = null;
	if(idType!=null&&!"".equals(idType)){
		if(idType=="XS"||idType.equals("XS")){
			request.setAttribute("navigator.location","查找 >> 电梯销售信息");
		}else{
			request.setAttribute("navigator.location","查找 >> 维改电梯信息");
		}
		try {      
			String sql="";
			hs = HibernateUtil.getSession();
            if(idType=="XS"||idType.equals("XS")){
			sql = "select * from ElevatorSalesInfo a " +
					" where enabledFlag='Y'"+
        			" and a.elevatorNo not in (select elevatorNo from ElevatorTransferCaseRegister)";
            }else
            {
            	sql = "select a.* from ElevatorSalesInfo a,ServicingContractMaster scm,ServicingContractDetail scd "+
            			"where scm.wg_Billno=scd.wg_Billno and scd.elevatorNo=a.elevatorNo and scm.AuditStatus='Y' and a.enabledFlag='Y' "+
            			"and a.elevatorNo not in (select elevatorNo from ElevatorTransferCaseRegister)";
            }
            if(elevatorNos!=null && !elevatorNos.equals("")){
            	elevatorNos=URLDecoder.decode(elevatorNos, "utf-8");
//            	//System.out.println(elevatorNos);
            	sql+=" and a.elevatorNo not in("+elevatorNos.replace("|", "'")+")";
            }
			if (elevatorNo != null && !elevatorNo.equals("")) {
				sql += " and a.elevatorNo like '%"+elevatorNo.trim()+"%'";
			}
			if (elevatorType != null && !elevatorType.equals("")) {
				sql += " and a.elevatorType = '"+elevatorType.trim()+"'";
			}
            if (isOutsideFootball != null && !isOutsideFootball.equals("")) {
				
				if(isOutsideFootball.trim()=="Y"||"Y".equals(isOutsideFootball.trim()))
				{
					sql += " and a.isOutsideFootball = '"+isOutsideFootball.trim()+"'";
				}else {
					sql += " and (a.isOutsideFootball is null or a.isOutsideFootball = 'N')";
				}
			}
			if(deliveryAddress!=null && !deliveryAddress.equals("")){
				sql+=" and a.deliveryAddress like '%"+deliveryAddress.trim()+"%'";
			}
			
			if (table.getIsAscending()) {
				sql += " order by "+ table.getSortColumn() +" asc";
			} else {
				sql += " order by "+ table.getSortColumn() +" desc";
			}
			
			query = hs.createSQLQuery(sql).addEntity("a",ElevatorSalesInfo.class);

			table.setVolume(query.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List searchElevatorTransferCaseRegisterList = query.list();

			table.addAll(searchElevatorTransferCaseRegisterList);
			session.setAttribute("searchElevatorTransferCaseRegisterList", table);
			session.setAttribute("idType", idType);
			request.setAttribute("elevatorTypeList", bd.getPullDownList("ElevatorSalesInfo_type"));
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (HibernateException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		forward = mapping.findForward("searchElevatorTransferCaseRegisterList");
	  }
		return forward;
	}
	
	

}
