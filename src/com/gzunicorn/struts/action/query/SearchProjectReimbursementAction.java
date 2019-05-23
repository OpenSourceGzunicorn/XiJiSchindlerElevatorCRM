package com.gzunicorn.struts.action.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchProjectReimbursementAction extends DispatchAction {
Log log = LogFactory.getLog(SearchProjectReimbursementAction.class);
	
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

		request.setAttribute("navigator.location","查询 >> 合同信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "searchProjectReimbursementList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchProjectReimbursement");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("busType");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String billno = tableForm.getProperty("billno");
		String maintContractNo = tableForm.getProperty("maintContractNo");
		String busType = tableForm.getProperty("busType");
		String comid=tableForm.getProperty("comid");
		String storage=tableForm.getProperty("storage");
		String billnostr = request.getParameter("billnostr");
		String reimbursPeople=request.getParameter("reimbursPeople");
		String salesContractNo = tableForm.getProperty("salesContractNo");// 销售合同号
		String projectName = tableForm.getProperty("projectName");// 销售合同号
		String isjoin=tableForm.getProperty("isjoin");//是否参与合同

		if(isjoin==null||isjoin.trim().equals("")){
			isjoin="%";
			tableForm.setProperty("isjoin",isjoin);
		}
		if(reimbursPeople == null ){
			reimbursPeople=tableForm.getProperty("reimbursPeople");
		}else{
			tableForm.setProperty("reimbursPeople",reimbursPeople);
		}
		
		if(billnostr == null || billnostr.trim().equals("")){
			billnostr=tableForm.getProperty("billnostr");
		}else{
			tableForm.setProperty("billnostr",billnostr);
		}
		
		Session hs = null;
		Query query = null;
		try {

			hs = HibernateUtil.getSession();

			String sql="select a.billno,a.maintContractNo,a.busType,a.num,a.maintDivision,a.comName,a.maintStation," +
					"a.storageName,p.pullname " +
					"from Compact_view a,Pulldown p " +
					"where a.contractStatus = p.pullid " +
					"and p.typeflag = 'MaintContractMaster_ContractStatus'";
	
			if (billnostr != null && !billnostr.equals("")) {
					sql += " and a.billno not in ('"+billnostr.replace(",", "','")+"')";
			}
			if(billno!=null && !billno.equals("")){
					sql+=" and a.billno like '%"+billno.trim()+"%'";
			}
			if(maintContractNo!=null && !maintContractNo.equals("")){
					sql+=" and a.maintContractNo like '%"+maintContractNo.trim()+"%'";
			}
			if(busType!=null && !busType.equals("")){
					sql+=" and a.busType like '%"+busType.trim()+"%'";					
			}
			
			if (comid != null && !comid.equals("")) {
				sql += " and a.maintDivision like '"+comid.trim()+"'";
			}
			if (storage != null && !storage.equals("")) {
				sql += " and (a.maintStation like '%"+storage.trim()+"%' or a.storageName like '%"+storage.trim()+"%')";
			}
			
			if(salesContractNo!=null && !salesContractNo.equals("")){
				sql+=" and a.billno in (select billNo2 from Compact_view_Detail where salesContractNo2 like '%"+salesContractNo.trim()+"%')";
			}
			if(projectName!=null && !projectName.equals("")){
				sql+=" and a.billno in (select billNo2 from Compact_view_Detail where ProjectName2 like '%"+projectName.trim()+"%')";
			}
			
			if (isjoin != null && !isjoin.equals("")) {
				if(isjoin.equals("Y")){
					//参与合同状态为 新签，续签的合同
					sql +=" and a.contractStatus in('ZB','XB') " +
						"and a.billno in (select distinct BillNo from MaintContractDetail where maintpersonnel='"+reimbursPeople+"')";
				}else if(isjoin.equals("N")){
					//不参与的合同，以及 历史，退保的维保合同出现在没有参与合同项
					sql +=" and (a.contractStatus in('LS','TB') or " +
						"a.billno not in (select distinct BillNo from MaintContractDetail where maintpersonnel='"+reimbursPeople+"'))";
				}
			}
			
			if (table.getIsAscending()) {
				sql += " order by a."+ table.getSortColumn() +" asc";
			} else {
				sql += " order by a."+ table.getSortColumn() +" desc";
			}
			
			//System.out.println(">>>>"+sql);
			
			query = hs.createSQLQuery(sql);
			table.setVolume(query.list().size());// 查询得出数据记录数;
			
			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);
			List list=query.list();

			List searchProjectReimbursementList=new ArrayList();
			for(Object object : list){
				Object[] values=(Object[])object;
				Map m=new HashMap();
				m.put("billno", values[0].toString());
				m.put("maintContractNo", values[1].toString().trim());
				m.put("busType", values[2].toString());
				m.put("num", values[3].toString());
				m.put("maintDivision", values[4].toString());
				m.put("comName",  values[5].toString());
				m.put("maintStation", values[6].toString());
				m.put("storageName",values[7].toString());
				m.put("contractStatus",values[8].toString());
				
				
				searchProjectReimbursementList.add(m);
			}

			table.addAll(searchProjectReimbursementList);
			session.setAttribute("searchProjectReimbursementList", table);
			request.setAttribute("maintDivisionList", Grcnamelist1.getgrcnamelist("admin"));
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		forward = mapping.findForward("searchProjectReimbursementList");
		
		return forward;
	}

	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord2(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("navigator.location","查询 >> 合同信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "searchProjectReimbursementList2");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchProjectReimbursement2");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("billno");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);
		
		String billno = tableForm.getProperty("billno");
		String maintContractNo = tableForm.getProperty("maintContractNo");
		String busType = tableForm.getProperty("busType");
		String maintContractNos = request.getParameter("maintContractNos");
		String maintStation = request.getParameter("maintStation");
		
		
		
		if(maintContractNos == null || maintContractNos.trim().equals("")){
			maintContractNos=tableForm.getProperty("maintContractNos");
		}else{
			tableForm.setProperty("maintContractNos",maintContractNos);
		}
		
		if(maintStation == null || maintStation.trim().equals("")){
			maintStation=tableForm.getProperty("maintStation");
		}else{
			tableForm.setProperty("maintStation",maintStation);
		}
		
		Session hs = null;
		Query query = null;
		try {
			hs = HibernateUtil.getSession();
			String sql="select billno,maintContractNo,busType,num,comName,storageName " +
					"from Compact_view where maintDivision like '"+userInfo.getComID()+"' " +
					"and maintStation like '%"+maintStation.trim()+"%'";
				if (maintContractNos != null && !maintContractNos.equals("")) {
					sql += " and maintContractNo not in ("+maintContractNos.replace("|", "'")+")";
				}
				if(billno!=null && !billno.equals("")){
						sql+=" and billno like '%"+billno.trim()+"%'";
				}
				if(maintContractNo!=null && !maintContractNo.equals("")){
						sql+=" and maintContractNo like '%"+maintContractNo.trim()+"%'";
				}
				if(busType!=null && !busType.equals("")){
						sql+=" and busType like '%"+busType.trim()+"%'";					
				}
				if (table.getIsAscending()) {
					sql += " order by "+ table.getSortColumn() +" asc";
				} else {
					sql += " order by "+ table.getSortColumn() +" desc";
				}
	
				query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;
				
				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
	
				cache.check(table);
				List list=query.list();
	
				List searchProjectReimbursementList2=new ArrayList();
				for(Object object : list){
					Object[] values=(Object[])object;
					Map m=new HashMap();
					m.put("billno", values[0]);
					m.put("maintContractNo", values[1]);
					m.put("busType", values[2]);
					m.put("num", values[3]);
					m.put("maintDivision", values[4]);
					m.put("storageName", values[5]);
					
					searchProjectReimbursementList2.add(m);
				}
	
				table.addAll(searchProjectReimbursementList2);
				session.setAttribute("searchProjectReimbursementList2", table);
				request.setAttribute("maintDivisionList", Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
			 
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
		forward = mapping.findForward("searchProjectReimbursementList2");
		
		return forward;
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
	public ActionForward toSearchRecord3(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("navigator.location","查询 >> 维保合同信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "searchMaintContractMinList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchProjectReimbursement3");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("billno");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String billno = tableForm.getProperty("billno");
		String maintContractNo = tableForm.getProperty("maintContractNo");
		String comid=tableForm.getProperty("comid");
		String salesContractNo = tableForm.getProperty("salesContractNo");// 销售合同号
		String companyId = tableForm.getProperty("companyId");// 销售合同号

		String billnostr = request.getParameter("billnostr");
		if(billnostr == null || billnostr.trim().equals("")){
			billnostr=tableForm.getProperty("billnostr");
		}else{
			tableForm.setProperty("billnostr",billnostr);
		}

		String maintStation="%";
		
		//第一次进入页面时根据登陆人初始化所属维保分部
		List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
		if(comid == null || comid.equals("")){
			Map map = (Map)maintDivisionList.get(0);
			comid = (String)map.get("grcid");
		}
		
		Session hs = null;
		Query query = null;
		try {

			hs = HibernateUtil.getSession();
			

			//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
			String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
			List vmlist=hs.createSQLQuery(sqlss).list();
			if(vmlist!=null && vmlist.size()>0){
				maintStation=userInfo.getStorageId();
			}

			String sql="select a.billno,a.maintContractNo,c.comname,d.storageName,a.contractStatus,p.pullname,ct.companyName " +
					"from MaintContractMaster a,Pulldown p,Company c,StorageID d,Customer ct " +
					"where a.contractStatus = p.pullid "+ 
					"and a.companyId = ct.companyId " +
					"and a.maintDivision = c.comid " +
					"and a.maintStation = d.storageid " +
					"and p.typeflag = 'MaintContractMaster_ContractStatus' " +
					"and a.contractStatus in('ZB','XB') " +
					"and a.maintStation like '"+maintStation+"'";
			
			if (companyId != null && !companyId.equals("")) {
					sql+=" and (a.companyId like '%"+companyId.trim()+"%' or ct.companyName like '%"+companyId.trim()+"%') ";
			}
			if (billnostr != null && !billnostr.equals("")) {
					sql += " and a.billno not in ('"+billnostr.replace(",", "','")+"')";
			}
			if(billno!=null && !billno.equals("")){
					sql+=" and a.billno like '%"+billno.trim()+"%'";
			}
			if(maintContractNo!=null && !maintContractNo.equals("")){
					sql+=" and a.maintContractNo like '%"+maintContractNo.trim()+"%'";
			}
			if (comid != null && !comid.equals("")) {
					sql += " and a.maintDivision like '"+comid.trim()+"'";
			}

			if (salesContractNo != null && !salesContractNo.equals("")) {
				String dsql="select distinct billNo from MaintContractDetail where salesContractNo like '%"+salesContractNo.trim()+"%'";
				List dlist=hs.createQuery(dsql).list();
				String ddstr="";
				if(dlist!=null && dlist.size()>0){
					Object obj=null;
					for(int d=0;d<dlist.size();d++){
						obj=(Object)dlist.get(d);
						if(d==dlist.size()-1){
							ddstr+=(String)obj;
						}else{
							ddstr+=(String)obj+"','";
						}
					}
				}
				sql += " and a.billNo in('"+ddstr+"')";
			}
			
			if (table.getIsAscending()) {
				sql += " order by a."+ table.getSortColumn() +" asc";
			} else {
				sql += " order by a."+ table.getSortColumn() +" desc";
			}
			
			//System.out.println(">>>>"+sql);
			
			query = hs.createSQLQuery(sql);
			table.setVolume(query.list().size());// 查询得出数据记录数;
			
			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);
			List list=query.list();

			List searchProjectReimbursementList=new ArrayList();
			for(Object object : list){
				Object[] values=(Object[])object;
				Map m=new HashMap();
				String billnokk=values[0].toString();
				m.put("billno", billnokk);
				m.put("maintContractNo", values[1].toString().trim());
				m.put("comName",  values[2].toString());
				m.put("storageName",values[3].toString());
				m.put("contractStatus",values[4].toString());
				m.put("contractStatusName",values[5].toString());
				m.put("companyName",values[6].toString());
				
				String sqlmas2="select count(BillNo) from MaintContractDetail where BillNo='"+billnokk+"'";
				List mcdlist=hs.createSQLQuery(sqlmas2).list();
				if(mcdlist!=null && mcdlist.size()>0){
					Object obj2=(Object)mcdlist.get(0);
					m.put("connum", String.valueOf(obj2));
				}else{
					m.put("connum", "0");
				}
				
				searchProjectReimbursementList.add(m);
			}

			table.addAll(searchProjectReimbursementList);
			session.setAttribute("searchMaintContractMinList", table);
			request.setAttribute("maintDivisionList", maintDivisionList);
			
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		forward = mapping.findForward("searchMaintContractList");
		
		return forward;
	}
	
}
