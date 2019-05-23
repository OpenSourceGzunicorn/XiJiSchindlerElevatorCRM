package com.gzunicorn.struts.action.custregistervisitplan;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class CustomerLevelAction extends DispatchAction {

	Log log = LogFactory.getLog(CustomerLevelAction.class);
	
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
		SysRightsUtil.filterModuleRight(request, response,
				SysRightsUtil.NODE_ID_FORWARD + "customerlevel", null);
		/** **********结束用户权限过滤*********** */

		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			ActionForward forward = super.execute(mapping, form, request,
					response);
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
		
		request.setAttribute("navigator.location","客户等级管理>> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
	
		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form,request,response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport","");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "customerLevelList");
         
			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fCustomerLevel");
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
			//第一次进入页面时根据登陆人初始化所属维保分部
			if(!userInfo.getComID().equals("00")){
				if(maintDivision == null || maintDivision.trim().equals("")){
					maintDivision = userInfo.getComID();
			    }
			}
			//第一次进入页面时登录人为维保经理时，固定维保站
			if(userInfo.getClass1().equals("20"))
			{
				if(mainStation == null || mainStation.trim().equals("")){
					mainStation = userInfo.getStorageId();
			    }
			}
			
			Session hs = null;
			Query query=null;
			String order="";
			try {
                
				hs = HibernateUtil.getSession();
				//只显示在保合同的客户信息
				String sql = "select T1.* from "
						+ "(select c.CompanyID,c.CompanyName,c.PrincipalName,"
						+ "c.PrincipalPhone,vcl.Orderby,c.OperDate,T.MaintDivision,"
						+ "T.BillNo,T.MaintStation from(select row_number() over"
						+ "(partition by mcm.CompanyID order by mcm.BillNo desc) as rownum,mcm.MaintDivision"
						+ ",mcm.BillNo,mcm.CompanyID,mcm.MaintStation from MaintContractMaster mcm) as T,Customer c left join"
						+ " ViewCustLevel vcl on vcl.custLevel = c.custLevel where T.rownum=1 and T.CompanyID=c.CompanyID "
						+ "and c.CompanyID in(select CompanyID from MaintContractMaster where ContractStatus in('ZB','XB') )) as T1  "
						+ "where 1=1";
				
				if (companyName != null && !companyName.equals("")) {
					sql += " and T1.companyName like '%" + companyName.trim() + "%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and T1.maintDivision like '" + maintDivision.trim()+ "'";
				}
				if (mainStation != null && !mainStation.equals("")) {
					sql += " and T1.maintStation like '" + mainStation.trim()+ "'";
				}
				if (table.getIsAscending()) {
					order=" order by "+table.getSortColumn();
				} else {
					order=" order by "+table.getSortColumn()+" desc";
				}

		        request.setAttribute("CompanyList", Grcnamelist1.getgrcnamelist(userInfo.getUserID()));

				if(userInfo.getClass1().equals("20"))//判断登录人职位
				{
				String hql="select a from Storageid a where a.storageid= '"+mainStation+"' " +
						"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";	
				List mainStationList=hs.createQuery(hql).list();
				request.setAttribute("mainStationList", mainStationList);
				}else
				{
		    	  String hql="select a from Storageid a,Company b where a.comid = b.comid and a.comid like '"+maintDivision+"' " +
		    	  		"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
				  List mainStationList=hs.createQuery(hql).list();
				  if(mainStationList!=null && mainStationList.size()>0){
					request.setAttribute("mainStationList", mainStationList);
			      }
				}
					
				//System.out.println(sql);
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
					  map.put("companyName", (String) object[1]);
					  map.put("principalName", (String) object[2]);
					  map.put("principalPhone", (String) object[3]);
					  map.put("orderby",bd.getName(hs, "ViewCustLevel", "CustLevel", "Orderby",(String) object[4]));
					  map.put("operDate", (String) object[5]);
					  map.put("maintDivision", bd.getName(hs, "Company", "ComFullName", "ComId",(String) object[6]));
					  map.put("companyId", (String) object[0]);
					  map.put("assignedMainStation", bd.getName(hs, "Storageid", "storagename", "storageid",(String) object[8]));
					  clList.add(map);
					}		
				}				
					
				List customerLevelList =clList;
				table.addAll(customerLevelList);
        		session.setAttribute("customerLevelList", table);

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
			forward = mapping.findForward("customerLevelList");
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
	 * 跳转到修改级别页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","客户等级管理 >> 等级维护");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		ActionForward forward = null;
		String id = null;

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("companyId");
		} else

 {
			id = (String) dform.get("id");
		}
		
		Session hs = null;
		List customerLevelList= new ArrayList();
		List levelList=new ArrayList();
		Customer customer=null;
		if (id != null) {
			
				try {
hs = HibernateUtil.getSession();
					
					customer =(Customer) hs.get(Customer.class,id);	
					dform.set("custLevel", customer.getCustLevel());
					String hql="select mcm.billNo,mcm.contractTotal,mcm.contractEdate,mcm.maintDivision,mcm.maintStation,mcm.maintContractNo "
							+ "from  Customer c, MaintContractMaster mcm "
							+ "where c.companyId=mcm.companyId "
							+ " and c.companyId ='"+customer.getCompanyId().trim()+"' "
							+ " and (mcm.contractStatus = 'ZB' or mcm.contractStatus='XB')"
							+ " order by mcm.billNo desc";				
					List list=hs.createQuery(hql).list();
					if(list!=null&&list.size()>0)
					{
						int leg=list.size();
						for(int i=0;i<leg;i++)
						{
						   Object[] obj=(Object[]) list.get(i);
						   HashMap map =new HashMap();
						   map.put("billNo", (String) obj[0]);
						   map.put("contractTotal", obj[1]);
						   map.put("contractEDate", (String) obj[2]);
						   map.put("maintDivision", bd.getName(hs, "Company", "ComFullName", "ComId",(String) obj[3]));
						   map.put("assignedMainStation", bd.getName(hs, "Storageid", "storagename", "storageid",(String) obj[4]));
						   map.put("maintContractNo", obj[5]);
						   String hql2="select COUNT(*) from MaintContractDetail mcd,MaintContractMaster mcm where mcm.billNo=mcd.billNo and mcm.billNo='"+(String) obj[0]+"'";
							List list2=hs.createQuery(hql2).list();
							if(list2!=null&&list2.size()>0)
							{
								int obj2= (Integer)list2.get(0);
								
								map.put("no", obj2);
							}
						   
							if((i+1)%2==0)
							{
								map.put("style", "oddRow3");
							}
							else
							{
								map.put("style", "evenRow3");
							}

							customerLevelList.add(map); 
						}	
					}
					
					if (customer == null) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"returningmaintain.display.recordnotfounterror"
								));
					}
					request.setAttribute("levelSize", list.size());	
				List list2=hs.createSQLQuery("select CustLevel,Orderby from ViewCustLevel order by Orderby").list();
				if(list2!=null&&list2.size()>0)
				{
					for(int i=0;i<list2.size();i++){
					Object[] objects=(Object[]) list2.get(i);
					HashMap map =new HashMap();
					map.put("custLevel", objects[0]);
					map.put("orderby", objects[1]);
					
					levelList.add(map);
					}
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
						DebugUtil
								.print(hex, "HibernateUtil Hibernate Session ");
					}
				}
			request.setAttribute("levelList", levelList);	
			request.setAttribute("customerLevelList", customerLevelList);
			request.setAttribute("customerBean", customer);
			forward = mapping.findForward("customerLevelModify");
}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	
	/**
	 * 修改客户等级
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toUpdateRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String companyId = (String) dform.get("companyId");
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			String custLevel = (String) dform.get("custLevel");
			Customer customer = (Customer) hs.get(Customer.class, (String) dform.get("companyId"));
			if (dform.get("companyId") != null) {
				customer.setCustLevel(custLevel);
				customer.setOperId(userInfo.getUserID());//录入人
				customer.setOperDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));//录入时间
		
				hs.update(customer);
			
			}	
            tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("customerlevel.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
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
	 * 重新计算等级
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toALLUpdateRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		//修改方法
		toUpDateCustomer(userInfo, errors);
		
		ActionForward forward = mapping.findForward("returnList");
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
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
			String hql="select a from Storageid a,Company b where a.comid = b.comid and a.comid='"+comid+"' " +
					"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
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
	 
	/**
	 * 重置所有有项目客户等级
	 * @param ViewLoginUserInfo
	 * @param ActionErrors
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public void toUpDateCustomer(ViewLoginUserInfo userInfo,ActionErrors errors) 
	{
		Session hs = null;
		Transaction tx = null;
		
		
		try {
			
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			//获取等级视图
			List list=hs.createSQLQuery("select * from ViewCustLevel").list();
			HashMap map =new HashMap();
		    if(list!=null&&list.size()>0)
			{
				for(int i=0;i<list.size();i++)
				{
					Object[] objects=(Object[]) list.get(i);
					map.put((String) objects[2], (String) objects[0]);
					map.put((String) objects[0], Double.parseDouble((String) objects[1]));
				}
			  }
			//计算客户顺序  【在保合同，电梯不是退保的，电梯不是历史的】
	        String sql="select  T.CompanyID,CAST(T.rownum *0.6+T.rownum2*0.4 as numeric(10,2))as 'orderno' "
	        		+ "from (select mcm.CompanyID,row_number() over(order by sum(mcm.ContractTotal) desc) as rownum,row_number() "
	        		+ "over(order by count(mcd.rowid) desc) as rownum2 from MaintContractMaster mcm,MaintContractDetail mcd "
	        		+ "where mcm.BillNo=mcd.BillNo "
	        		+ "and mcm.ContractStatus in('ZB','XB') "//在保合同
	        		+ "and isnull(mcd.issurrender,'N')<>'Y' "//电梯不是退保的
	        		+ "and isnull(mcd.ElevatorStatus,'')='' "//电梯不是历史的
	        		+ "group by mcm.CompanyID) as T order by orderno";
	        System.out.println(">>>"+sql);
	        list=hs.createSQLQuery(sql).list();
	        
	        
	        if(list!=null && list.size()>0){
					//获得每个等级的人数
				 int leg =list.size();
				 int ka,a,b;
				 ka=(int) Math.ceil(leg*(Double) map.get("KA"));
				 a= (int) Math.ceil(leg*(Double) map.get("A"));
				 b= (int) Math.ceil(leg*(Double) map.get("B"));
	             
						 
				 for(int i=0;i<leg;i++)
					{
						Object[] objects=(Object[]) list.get(i);
						Customer customer=(Customer) hs.get(Customer.class, (String) objects[0]);
						if(customer!=null){
							//判断
							if(i+1<=ka)
							{
								customer.setCustLevel("KA");
							
							}else if(i+1>ka && (i+1)<=(ka+a))
							{
								customer.setCustLevel("A");
							
							}else if(i+1>(ka+a) && (i+1)<=(ka+a+b))
							{
								customer.setCustLevel("B");
							}else
							{
								customer.setCustLevel("C");	
							}	
							
							customer.setOperId(userInfo.getUserID());//录入人
							customer.setOperDate(DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));//录入时间
							
							hs.update(customer);
						}
					}
				}	
		
			  tx.commit();
			  errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","计算等级成功！"));
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","计算等级失败！"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}
	
	}

	public HttpServletResponse toExcelRecord(ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ServeTableForm tableForm = (ServeTableForm) form;
		
		String companyName = tableForm.getProperty("companyName");	
		String maintDivision = tableForm.getProperty("maintDivision");
		String mainStation =  tableForm.getProperty("assignedMainStation");
		
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();
		
		try{
			hs = HibernateUtil.getSession();
			//只显示在保合同的客户信息
			String sql = "select T1.*,vc.CustLevel,cy.ComName,sd.storagename from "
					+ "(select c.CompanyID,c.CompanyName,c.PrincipalName,"
					+ "c.PrincipalPhone,vcl.Orderby,c.OperDate,T.MaintDivision,"
					+ "T.BillNo,T.MaintStation from(select row_number() over"
					+ "(partition by mcm.CompanyID order by mcm.BillNo desc) as rownum,mcm.MaintDivision"
					+ ",mcm.BillNo,mcm.CompanyID,mcm.MaintStation from MaintContractMaster mcm) as T,Customer c left join"
					+ " ViewCustLevel vcl on vcl.custLevel = c.custLevel where T.rownum=1 and T.CompanyID=c.CompanyID "
					+ "and c.CompanyID in(select CompanyID from MaintContractMaster where ContractStatus in('ZB','XB') )) as T1 "
					+ "left join ViewCustLevel vc on vc.orderby=T1.orderby "
					+ "left join Company cy on cy.comId=T1.maintDivision "
					+ "left join Storageid sd on sd.storageid=T1.maintStation "
					+ "where 1=1";

			if (companyName != null && !companyName.equals("")) {
				sql += " and T1.companyName like '%" + companyName.trim() + "%'";
			}
			if (maintDivision != null && !maintDivision.equals("")) {
				sql += " and T1.maintDivision like '" + maintDivision.trim()+ "'";
			}
			if (mainStation != null && !mainStation.equals("")) {
				sql += " and T1.maintStation like '" + mainStation.trim()+ "'";
			}
			sql+=" order by Orderby";
			
			ResultSet rs=hs.connection().prepareStatement(sql).executeQuery();
			
	
			String[] titlename={"单位名称","负责人","联系电话","客户等级"," 维保分部","维保站","更新日期"};
			String[] titleid={"companyName","principalName","principalPhone",
					"custLevel","comName","storagename","operDate"};
			
	        XSSFSheet sheet = wb.createSheet("客户等级管理");
	        
	        //创建单元格样式
	        XSSFCellStyle cs = wb.createCellStyle();
	        cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);//左右居中
	        cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
	        XSSFFont f  = wb.createFont();
	        f.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 字体加粗
	        cs.setFont(f);
	        
	        //创建标题
	        XSSFRow row0 = sheet.createRow( 0);
	        XSSFCell cell0 = null;
			for(int i=0;i<titlename.length;i++){
				cell0 = row0.createCell((short)i);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(titlename[i]);
				cell0.setCellStyle(cs);
			}
			//创建内容
			XSSFRow row = null;
			XSSFCell cell =null;
			int j=0;
			while(rs.next()) {
				// 创建Excel行，从0行开始
				row = sheet.createRow(j+1);
				for(int c=0;c<titleid.length;c++){
				    cell = row.createCell((short)c);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				    cell.setCellValue(rs.getString(titleid[c]));
				}
				j++;
			}

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
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("客户等级管理", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}



}
