package com.gzunicorn.struts.action.hotlinemanagement.hotcalloutmaster;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
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
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.basedata.elevatorsales.ElevatorSalesInfo;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.struts.action.xjsgg.XjsggAction;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class hotphonexdwjx extends DispatchAction {

	Log log = LogFactory.getLog(hotphonexdwjx.class);
	XjsggAction xj=new XjsggAction();
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
		
		/*String htmlTableDesc=request.getParameter("htmlTableDesc");//分页找到对应的查询条件
		String serviceObjects = request.getParameter("serviceObjects");
		if ((name == null || name.equals("") )&& htmlTableDesc==null) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else if( htmlTableDesc==null && !"".equals(serviceObjects)){
			if(serviceObjects.equals("3")){
				name = "toElevatorSale";
			}else{
				name = "toSearchRecord";
			}
			return dispatchMethod(mapping, form, request, response, name);
		}else if(htmlTableDesc!=null){
			if(htmlTableDesc.equals("MaintContractList")){
				name = "toSearchRecord";
			}else if(htmlTableDesc.equals("MaintContractList2")){
				name = "toElevatorSale";
			}
			return dispatchMethod(mapping, form, request, response, name);
		}else{
			ActionForward forward = super.execute(mapping, form, request, response);
			return forward;
		}*/

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

		request.setAttribute("navigator.location","维保合同>> 查询 ");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "MaintContractList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchhotphone");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("b.elevatorNo");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String salesContractNo = tableForm.getProperty("salesContractNo");//销售合同号
		String companyName = tableForm.getProperty("companyName");//保修单位名称
		String elevatorNo = tableForm.getProperty("elevatorNo");//电梯编号
		Session hs = null;
		Query query = null;
		List list=new ArrayList();
		try {
			hs = HibernateUtil.getSession();
			String sql = "select a.companyId,c.companyName,isnull(b.MaintAddress,'') as MaintAddress,b.salesContractNo,"
					+ "b.elevatorNo,b.elevatorParam,b.assignedMainStation,d.storageName,"
					+ "b.maintPersonnel,f.username,f.phone " +
					"from MaintContractMaster as a "
					+ " left join Customer as c on a.companyId =c.companyId," +
					"MaintContractDetail as b "
					+ " left join Storageid as d on b.assignedMainStation=d.storageId "
					+ " left join Loginuser as f on b.maintPersonnel=f.userid"
					+ " where a.billNo=b.billNo and (a.contractStatus='XB'or a.contractStatus='ZB')"
					+ " and isnull(b.maintPersonnel,'') <> ''"
					+ " and a.ContractStatus not in('LS','TB')";
			sql+=xj.getsql("b.salesContractNo", salesContractNo);
			sql+=xj.getsql("c.companyName", companyName);
			sql+=xj.getsql("b.elevatorNo", elevatorNo);
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

			List customerList = query.list();
            if(customerList!=null && customerList.size()>0){
            	for(int i=0;i<customerList.size();i++){
            		Object[] obj=(Object[])customerList.get(i);
            		HashMap hm=new HashMap();
            		hm.put("companyId", String.valueOf(obj[0]));//报修单位代码
            		hm.put("companyName",String.valueOf(obj[1]));//报修单位名称
            		hm.put("address", String.valueOf(obj[2]));//项目地址
            		hm.put("salesContractNo", String.valueOf(obj[3]));//销售合同号
            		hm.put("elevatorNo", String.valueOf(obj[4]));//电梯编号
            		hm.put("elevatorParam",String.valueOf(obj[5]));//规格型号
            		hm.put("storageId", String.valueOf(obj[6]).replace("null", ""));//下达维保站代码
            		hm.put("storageName",String.valueOf(obj[7]).replace("null", ""));//维保站名称
            		hm.put("maintPersonnel",String.valueOf(obj[8]).replace("null", ""));//派工对象代码
            		hm.put("username", String.valueOf(obj[9]).replace("null", ""));//派工对象名称
            		hm.put("phone", String.valueOf(obj[10]).replace("null", ""));//派工对象电话
            		list.add(hm);
            	}
            }
			table.addAll(list);
			session.setAttribute("MaintContractList", table);

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
		forward = mapping.findForward("toList");
		
		return forward;
	}
	@SuppressWarnings("unchecked")
	public ActionForward toElevatorSale(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("navigator.location","电梯信息>> 查询 ");
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
        HttpSession session =request.getSession();
        ActionForward forward=null;
		HTMLTableCache cache = new HTMLTableCache(session, "MaintContractList2");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchhotphone");
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

		String salesContractNo = tableForm.getProperty("salesContractNo");//销售合同号
		String companyName = tableForm.getProperty("companyName");//保修单位名称
		String elevatorNo = tableForm.getProperty("elevatorNo");//电梯编号
		Session hs = null;
		Query query = null;
		List list=new ArrayList();
	try {
		hs=HibernateUtil.getSession();
		String sql="select a from ElevatorSalesInfo a where 1=1 ";
		sql+=xj.getsql("a.salesContractNo", salesContractNo);
		sql+=xj.getsql("a.useUnit", companyName);
		sql+=xj.getsql("a.elevatorNo", elevatorNo);
		if (table.getIsAscending()) {
			sql += " order by "+ table.getSortColumn() +" asc";
		} else {
			sql += " order by "+ table.getSortColumn() +" desc";
		}
		////System.out.println(sql);
		query = hs.createQuery(sql);
		table.setVolume(query.list().size());// 查询得出数据记录数;

		// 得出上一页的最后一条记录数号;
		query.setFirstResult(table.getFrom()); // pagefirst
		query.setMaxResults(table.getLength());

		cache.check(table);

		List customerList = query.list();
        if(customerList!=null && customerList.size()>0){
        	for(int i=0;i<customerList.size();i++){
        		ElevatorSalesInfo es=(ElevatorSalesInfo)customerList.get(i);
        		HashMap hm=new HashMap();
        		hm.put("companyId", es.getUseUnit());//报修单位代码
        		hm.put("companyName",es.getUseUnit());//报修单位名称
        		hm.put("address", es.getDeliveryAddress());//项目地址
        		hm.put("salesContractNo", es.getSalesContractNo());//销售合同号
        		hm.put("elevatorNo", es.getElevatorNo());//电梯编号
        		hm.put("elevatorParam",es.getElevatorParam());//规格型号
        		hm.put("storageId", "");//下达维保站代码
        		hm.put("storageName","");//维保站名称
        		hm.put("maintPersonnel","");//派工对象代码
        		hm.put("username", "");//派工对象名称
        		hm.put("phone","");//派工对象电话
        		list.add(hm);
        	}
        }
		table.addAll(list);
		session.setAttribute("MaintContractList2", table);
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
	forward = mapping.findForward("toList2");
	
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

	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord2(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("navigator.location","电梯信息>> 查询 ");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "MaintContractList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchhotphone");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("a.elevatorNo");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String salesContractNo = tableForm.getProperty("salesContractNo");//销售合同号
		String salesContractName = tableForm.getProperty("salesContractName");//保修单位名称
		String elevatorNo = tableForm.getProperty("elevatorNo");//电梯编号
		String projectAddress=tableForm.getProperty("projectAddress");
		Session hs = null;
		Query query = null;
		List list=new ArrayList();
		try {
			hs = HibernateUtil.getSession();
			
			//在保合同并且已下达的维保合同，才有维保站出现。
			String sql = "select a.salesContractNo,a.ElevatorNo,a.ElevatorParam,a.salesContractName,"+
				"case when isnull(b.CompanyID,'')='' then a.UseUnit else ISNULL(b.companyId,'') end companyId,"+
				"case when ISNULL(b.companyName,'')='' then a.UseUnit else isnull(b.CompanyName,'') end companyName,"+
				"isnull(ec.rem,'') address,b.maintPersonnel,b.maintStation,b.storageName,b.userName,b.phone," +
				"isnull(a.isOutsideFootball,'N') isOutsideFootball from ElevatorSalesInfo a " +
				"left join ElevatorCoordinateLocation ec on ec.ElevatorNo=a.ElevatorNo "+
				"left join "+
				"(select md.ElevatorNo,md.MaintPersonnel,md.assignedMainStation as MaintStation," +
				"mm.CompanyID,ct.companyName,st.StorageName,f.UserName,f.Phone "+
				"from MaintContractDetail md left join Storageid st on md.assignedMainStation=st.storageId"+
				" left join Loginuser f on md.maintPersonnel=f.userid,"+
				"MaintContractMaster mm left outer join Customer ct on mm.CompanyID=ct.CompanyID where md.BillNo=mm.BillNo"+
				" and mm.ContractStatus not in('LS','TB') and (mm.contractStatus='XB'or mm.contractStatus='ZB') and mm.TaskSubFlag='Y') b"+
				" on a.ElevatorNo=b.ElevatorNo where 1=1";

			if(salesContractNo!=null && !"".equals(salesContractNo))
				sql+=" and a.salesContractNo like '%"+salesContractNo.trim()+"%'";
			if(salesContractName!=null && !"".equals(salesContractName))
				sql+=" and a.salesContractName like '%"+salesContractName.trim()+"%'";
			if(elevatorNo!=null && !"".equals(elevatorNo))
				sql+=" and a.elevatorNo like '%"+elevatorNo.trim()+"%'";
			if(projectAddress!=null && !"".equals(projectAddress))
				sql+=" and a.deliveryAddress like '%"+projectAddress.trim()+"%'";
			if (table.getIsAscending()) {
				sql += " order by "+ table.getSortColumn() +" asc";
			} else {
				sql += " order by "+ table.getSortColumn() +" desc";
			}
			
			//System.out.println(">>>>>>>>> "+sql);
			
			query = hs.createSQLQuery(sql);
			table.setVolume(query.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List customerList = query.list();
            if(customerList!=null && customerList.size()>0){
            	for(Object object : customerList){
            		Object[] objs=(Object[])object;
            		Map map=new HashMap();
            		map.put("salesContractNo",objs[0]);
            		map.put("elevatorNo",objs[1]);
            		map.put("elevatorParam",objs[2]);
            		map.put("salesContractName",objs[3]);
            		map.put("companyId",objs[4]);
            		map.put("companyName",objs[5]);
            		map.put("address",objs[6]);
            		map.put("maintPersonnel",objs[7]);
            		map.put("storageId",objs[8]);
            		map.put("storageName",objs[9]);
            		map.put("username",objs[10]);
            		map.put("phone",objs[11]);
            		map.put("isOutsideFootball",objs[12]);
            		list.add(map);
            	}
            	/*for(int i=0;i<customerList.size();i++){
            		Object[] obj=(Object[])customerList.get(i);
            		HashMap hm=new HashMap();
            		hm.put("companyId", String.valueOf(obj[0]));//报修单位代码
            		hm.put("companyName",String.valueOf(obj[1]));//报修单位名称
            		hm.put("address", String.valueOf(obj[2]));//项目地址
            		hm.put("salesContractNo", String.valueOf(obj[3]));//销售合同号
            		hm.put("elevatorNo", String.valueOf(obj[4]));//电梯编号
            		hm.put("elevatorParam",String.valueOf(obj[5]));//规格型号
            		hm.put("storageId", String.valueOf(obj[6]).replace("null", ""));//下达维保站代码
            		hm.put("storageName",String.valueOf(obj[7]).replace("null", ""));//维保站名称
            		hm.put("maintPersonnel",String.valueOf(obj[8]).replace("null", ""));//派工对象代码
            		hm.put("username", String.valueOf(obj[9]).replace("null", ""));//派工对象名称
            		hm.put("phone", String.valueOf(obj[10]).replace("null", ""));//派工对象电话
            		list.add(hm);
            	}*/
            }
			table.addAll(list);
			session.setAttribute("MaintContractList", table);

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
		forward = mapping.findForward("toList");
		
		return forward;
	}
}
