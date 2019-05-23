package com.gzunicorn.struts.action.query;

import java.net.URLDecoder;
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
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.engcontractmanager.lostelevatorreport.LostElevatorReport;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchLostElevatorReportAction extends DispatchAction {

	Log log = LogFactory.getLog(SearchLostElevatorReportAction.class);
	
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

		request.setAttribute("navigator.location","查询 >> 丢梯合同信息");		
		ActionForward forward = null;
		
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "searchLostElevatorReportList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fLostReport");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("jnlno");
		table.setIsAscending(true);
		cache.updateTable(table);
		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);
		String maintContractNo = tableForm.getProperty("MaintContractNo");
		String sdate1=tableForm.getProperty("sdate1");
		String edate1=tableForm.getProperty("edate1");
		String enabledFlag = tableForm.getProperty("enabledFlag");
		String salesContractNo=tableForm.getProperty("salesContractNo");//销售合同号
		
		String wbBillnos=request.getParameter("wbBillnos");
		if(wbBillnos==null || wbBillnos.trim().equals("")){
			wbBillnos=tableForm.getProperty("wbBillnos");
		}else{
			tableForm.setProperty("wbBillnos", wbBillnos);
		}
		String contactPhone=request.getParameter("contactPhone");
		
		Session hs = null;
		Query query = null;
		try {

			//System.out.println("contactPhone=="+contactPhone);
			if(contactPhone==null || "".equals(contactPhone.trim())){
				contactPhone=tableForm.getProperty("contactPhone");
			}else{
				//使用URL解码
				contactPhone=URLDecoder.decode(contactPhone,"UTF-8");
				tableForm.setProperty("contactPhone", contactPhone);
			}
			//System.out.println("contactPhone>>>=="+contactPhone);

			hs = HibernateUtil.getSession();

			String salesContractNoStr="";
			if (salesContractNo != null && !salesContractNo.equals("")) {
				String salesContractNohql = "select distinct mcd.billNo from MaintContractDetail mcd where mcd.salesContractNo like '%"+salesContractNo.trim()+"%'";
				List salesContractNoList=hs.createQuery(salesContractNohql).list();
			   if(salesContractNoList!=null&&salesContractNoList.size()>0){
				   for(int i=0;i<salesContractNoList.size();i++){
					   salesContractNoStr+=i==salesContractNoList.size()-1?"'"+salesContractNoList.get(i)+"'":"'"+salesContractNoList.get(i)+"',";
				   }
			   }
			}
			String hql="select a.jnlno,a.BillNo,a.MaintContractNo,a.LostElevatorDate,"
						+"a.CauseAnalysis,b.pullname "
					  +"from LostElevatorReport a,pulldown b "
					  +"where a.CauseAnalysis=b.pullid and "
					 +"b.typeflag='LostElevatorReport_CauseAnalysis' "
				+" and a.AuditStatus='Y' "
				+"and a.MaintContractNo not in(select MaintContractNo from LostElevatorMaintainDetail) and "
					   +"a.ContactPhone='"+contactPhone.trim()+"'";
			if(wbBillnos!=null && !wbBillnos.equals("")){
				hql+=" and a.billNo not in ("+wbBillnos.replace("|", "'")+")";
			}
	        if (maintContractNo != null && !maintContractNo.equals("")) {
				hql += " and a.MaintContractNo like '%"+maintContractNo.trim()+"%'";
			}
		
			if (enabledFlag != null && !enabledFlag.equals("")) {
				hql += " and c.enabledFlag = '%"+enabledFlag.trim()+"%'";
			}
	
			if (sdate1 != null && !sdate1.equals("")) {
				hql+=" and a.LostElevatorDate >= '"+sdate1.trim()+"'";
			}
			if (edate1 != null && !edate1.equals("")) {
				hql+=" and a.LostElevatorDate <= '"+edate1.trim()+"'";
			}
			if(salesContractNo != null && !salesContractNo.equals("")){
				if (salesContractNoStr != null && !salesContractNoStr.equals("")) {
					hql += " and a.billNo in ("+salesContractNoStr+")";
				}else{
					hql += " and a.billNo = ''";
				}
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

			List searchLostElevatorReportList = new ArrayList(); 
			List list= query.list();
			if(list!=null&&list.size()>0){
				
			for(int i=0;i<list.size();i++){
				Object[] object = (Object[])list.get(i); 
				Map map=new HashMap();
				map.put("jnlno", object[0]);
				map.put("billNo", object[1]);
				map.put("maintContractNo", object[2]);
				map.put("lostElevatorDate", object[3]);
				map.put("causeAnalysis", object[4]);
				map.put("pullname", object[5]);
				searchLostElevatorReportList.add(map);
			}
			}

			cache.check(table);
			table.addAll(searchLostElevatorReportList);
			session.setAttribute("searchLostElevatorReportList", table);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		
		
		
		
		
		forward = mapping.findForward("searchLostElevatorReportList");
		
		return forward;
	}

	/**
	 * 点击查看的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","维保合同退保管理 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		display(form, request, errors, "display");
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		request.setAttribute("display", "yes");
		
		String typejsp= request.getParameter("typejsp");
		if(typejsp!=null){
			request.setAttribute("typejsp", typejsp);
		}
		
		forward = mapping.findForward("searchLostElevatorMaintainDetailDisplay");
		return forward;
	}
	
public void display(ActionForm form, HttpServletRequest request ,ActionErrors errors ,String flag){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		
		if(id==null || "".equals(id)){
			id=(String) request.getAttribute("id");
		}
		
		Session hs = null;
		LostElevatorReport report=new LostElevatorReport();
	
		if (id != null && !id.equals("")) {			
			try {
				hs = HibernateUtil.getSession();
				
				//转换中文乱码
             	id=new String(id.getBytes("ISO-8859-1"),"gbk");
				
				String sql="select a,b.comname,c.storagename "+
				"from LostElevatorReport a,Company b,Storageid c "+ 
			
				"where a.maintDivision=b.comid and a.maintStation=c.storageid "+ 
			
				" and a.maintContractNo='"+id+"'";
				Query query = hs.createQuery(sql);
				List list = query.list();
				String division="",station="",nature="",cause="";
				if (list != null && list.size() > 0) {
					for(Object object : list){
						Object[] objs=(Object[])object;
						report=(LostElevatorReport) objs[0];
						division=objs[1].toString();
						station=objs[2].toString();
						
						String[] a={"a.id.typeflag='LostElevatorReport_ContractNatureOf'"};
						nature=bd.getName("Pulldown", "pullname", "id.pullid", report.getContractNatureOf(), a);
						String[] b={"a.id.typeflag='LostElevatorReport_CauseAnalysis'"};
						cause=bd.getName("Pulldown", "pullname", "id.pullid", report.getCauseAnalysis(), b);
					}
					dform.set("id", report.getJnlno());
					if("display".equals(flag)){
						report.setAuditOperid(bd.getName("Loginuser", "username", "userid", report.getAuditOperid()));
						report.setOperId(bd.getName("Loginuser", "username", "userid", report.getOperId()));
						report.setMaintDivision(division);
						report.setMaintStation(station);
						report.setContractNatureOf(nature);
						report.setCauseAnalysis(cause);
					}else{
						request.setAttribute("maintDivisionName", division);
						request.setAttribute("maintStationName", station);
						request.setAttribute("contractNatureOfName", nature);
						request.setAttribute("causeName", cause);
					}
					request.setAttribute("lostElevatorReportBean", report);
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
	
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			
		}		
		
	}
	
	
	
}
