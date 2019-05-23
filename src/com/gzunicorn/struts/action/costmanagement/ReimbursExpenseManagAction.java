package com.gzunicorn.struts.action.costmanagement;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cache.QueryKey;
import org.hibernate.engine.QueryParameters;
import org.hibernate.jmx.HibernateServiceMBean;
import org.hibernate.sql.QuerySelect;
import org.jbpm.context.exe.variableinstance.HibernateStringInstance;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.JSONUtil;
import com.gzunicorn.common.util.SqlBeanUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.costmanagement.maintdivisionreimbursement.MaintDivisionReimbursement;
import com.gzunicorn.hibernate.costmanagement.maintdivisionreimbursementdetail.MaintDivisionReimbursementDetail;
import com.gzunicorn.hibernate.costmanagement.maintstationreimbursement.MaintStationReimbursement;
import com.gzunicorn.hibernate.costmanagement.maintstationreimbursementdetail.MaintStationReimbursementDetail;
import com.gzunicorn.hibernate.costmanagement.noreimbursement.NoReimbursement;
import com.gzunicorn.hibernate.costmanagement.projectreimbursement.ProjectReimbursement;
import com.gzunicorn.hibernate.costmanagement.reimbursexpensemanag.ReimbursExpenseManag;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * 报销费用管理
 * @author Lijun
 *
 */
public class ReimbursExpenseManagAction extends DispatchAction {

	Log log = LogFactory.getLog(ReimbursExpenseManagAction.class);
	
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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "reimbursexpensemanag", null);
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

	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("navigator.location","报销费用管理 >> 查询列表");		
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
			HTMLTableCache cache = new HTMLTableCache(session, "reimbursExpenseManagList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fReimbursExpenseManag");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("billno");
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
			
			String billNo = tableForm.getProperty("billno");// 流水号
			String reimbursPeople = tableForm.getProperty("reimbursPeople");// 报销人
			String totalAmount = tableForm.getProperty("totalAmount");// 报销总额
			String maintDivision = tableForm.getProperty("maintDivision");// 所属维保站			

			//第一次进入页面时根据登陆人初始化所属维保分部
			List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
			if(maintDivision == null || maintDivision.equals("")){
				Map map = (Map)maintDivisionList.get(0);
				maintDivision = (String)map.get("grcid");
			}
			
			Session hs = null;
			Query query = null;
			try {

				hs = HibernateUtil.getSession();

				String sql = "select rem.billno,c.comname,s.storagename,l.username,rem.reimbursDate,rem.totalAmount " +
						"from ReimbursExpenseManag rem,Company c,Storageid s,Loginuser l " +
						"where rem.maintDivision=c.comid and rem.maintStation=s.storageid and rem.reimbursPeople=l.userid";
				
				if (billNo != null && !billNo.equals("")) {
					sql += " and rem.billno like '%"+billNo.trim()+"%'";
				}
				if (reimbursPeople != null && !reimbursPeople.equals("")) {
					sql += " and l.username like '%"+reimbursPeople.trim()+"%'";
				}
				if (totalAmount != null && !totalAmount.equals("")) {
					sql += " and rem.totalAmount like '%"+totalAmount.trim()+"%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and c.comid like '%"+maintDivision.trim()+"%'";
				}
				
				if (table.getIsAscending()) {
					sql += " order by rem."+ table.getSortColumn() +" desc";
				} else {
					sql += " order by rem."+ table.getSortColumn() +" asc";
				}
				
				query = hs.createQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List reimbursExpenseManagList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map map=new HashMap();
					map.put("billno", objs[0]);
					map.put("maintDivision", objs[1]);
					map.put("maintStation", objs[2]);
					map.put("reimbursPeople", objs[3]);
					map.put("reimbursDate", objs[4]);
					map.put("totalAmount", objs[5]);
					reimbursExpenseManagList.add(map);
				}

				table.addAll(reimbursExpenseManagList);
				session.setAttribute("reimbursExpenseManagList", table);
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);

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
			forward = mapping.findForward("reimbursExpenseManagList");
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
		
		request.setAttribute("navigator.location","报销费用管理 >> 查看");
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
		
		forward = mapping.findForward("reimbursExpenseManagDisplay");
		return forward;
	}
	
	/**
	 * 删除记录
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDeleteRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			String id = (String) dform.get("id");
			ReimbursExpenseManag master = (ReimbursExpenseManag) hs.get(ReimbursExpenseManag.class, id);
			if (master != null) {
				hs.createQuery("delete from ProjectReimbursement p where p.reimbursExpenseManag.billno='"+id+"'").executeUpdate();
				hs.createQuery("delete from MaintStationReimbursementDetail sd where sd.maintStationReimbursement.jnlno in (select m.jnlno from MaintStationReimbursement m where m.reimbursExpenseManag.billno='"+id+"')").executeUpdate();
				hs.createQuery("delete from MaintDivisionReimbursementDetail dd where dd.maintDivisionReimbursement.jnlno in (select m.jnlno from MaintDivisionReimbursement m where m.reimbursExpenseManag.billno='"+id+"')").executeUpdate();
				hs.createQuery("delete from MaintStationReimbursement m where m.reimbursExpenseManag.billno='"+id+"'").executeUpdate();
				hs.createQuery("delete from MaintDivisionReimbursement m where m.reimbursExpenseManag.billno='"+id+"'").executeUpdate();
				hs.createQuery("delete from NoReimbursement m where m.reimbursExpenseManag.billno='"+id+"'").executeUpdate();
				hs.delete(master);
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.succeed"));
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (DataStoreException e1) {
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
	
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
	
		ActionForward forward = null;
		try {
			forward = mapping.findForward("returnList");
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return forward;
	}


	/**
	 * 跳转到新建方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws ParseException 
	 */
	
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareAddRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, ParseException {

		
		request.setAttribute("navigator.location", "报销费用管理>> 添加");
		DynaActionForm dform = (DynaActionForm) form;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
			
		}
		request.setAttribute("operName", userInfo.getUserName());
		ReimbursExpenseManag rem=new ReimbursExpenseManag();
		rem.setMaintDivision(userInfo.getComID());
		rem.setOperId(userInfo.getUserID());
		rem.setOperDate(CommonUtil.getNowTime());
		rem.setReimbursDate(CommonUtil.getToday());
		request.setAttribute("reimbursExpenseManagBean", rem);
		request.setAttribute("prTypeList", bd.getPullDownList("ProjectReimbursement_ReimburType"));
		request.setAttribute("mrTypeList", bd.getPullDownList("MaintStationReimbursement_ReimburType"));
//		request.setAttribute("userList", this.getUserList(userInfo.getComID()));
		//request.setAttribute("maintStationList", bd.getMaintStationList(userInfo.getComID()));
		return mapping.findForward("reimbursExpenseManagAdd");
	}
	
	public List getUserList(String comid){
		Session session=null;
		List userList=new ArrayList();
		try {
			session=HibernateUtil.getSession();
			String hql="select userID,userName from ViewLoginUserInfo where comID='"+comid+"'";
			List list=session.createQuery(hql).list();
			for(Object object : list){
				Object[] value=(Object[])object;
				Map map=new HashMap();
				map.put("userID", value[0]);
				map.put("userName", value[1]);
				userList.add(map);
			}
			
		} catch (DataStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userList;
	}
	
	/**
	 * 点击新建的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	public ActionForward toAddRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	
		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;	
		//System.out.println(001);
		addOrUpdate(form,request,errors);// 新增或修改记录
		
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
				forward = mapping.findForward("returnList");
			} else {
				
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				
				forward = mapping.findForward("returnAdd");
				
				
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
	 * 跳转到修改页面
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
		
		request.setAttribute("navigator.location","维保合同管理 >> 修改");	
		//DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		display(form, request, errors, "");
		
		request.setAttribute("maintDivisionName", userInfo.getComName()); // 维保分部名称
		request.setAttribute("signWayList", bd.getPullDownList("MaintContractDetail_SignWay"));// 签署方式下拉框列表	
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
			
		forward = mapping.findForward("reimbursExpenseManagModify");
		
		return forward;
	}

	/**
	 * 点击修改的方法
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

		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;
		
		addOrUpdate(form,request,errors);// 新增或修改记录
		
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

	public void display(ActionForm form, HttpServletRequest request ,ActionErrors errors ,String flag){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		
		Session hs = null;
		List projectList = new ArrayList();
		List stationList=null;
		List divisionList=null;
		List noReimList=null;
	
		if (id != null && !id.equals("")) {			
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from ReimbursExpenseManag where billNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {

					// 主信息
					ReimbursExpenseManag master = (ReimbursExpenseManag) list.get(0);															
					dform.set("id",id);
					if("display".equals(flag)){// 查看
						master.setReimbursPeople(bd.getName(hs, "Loginuser", "username", "userid", master.getReimbursPeople()));
						master.setMaintDivision(bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision()));// 维保分部	
						master.setMaintStation(bd.getName(hs,"Storageid", "storagename", "storageid", master.getMaintStation()));
						master.setOperId(bd.getName(hs, "Loginuser", "username", "userid", master.getOperId()));
					}else{
						request.setAttribute("maintDivisionName", bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision()));
						request.setAttribute("storageName", bd.getName(hs,"Storageid", "storagename", "storageid", master.getMaintStation()));
						request.setAttribute("reimbursPeopleName", bd.getName(hs,"LoginUser", "username", "userid", master.getReimbursPeople()));
						request.setAttribute("prTypeList", bd.getPullDownList("ProjectReimbursement_ReimburType"));
						request.setAttribute("mrTypeList", bd.getPullDownList("MaintStationReimbursement_ReimburType"));
						request.setAttribute("operName", userInfo.getUserName());
						request.setAttribute("ismchang", "yes");
					}
										
					//合同报销
					String sql = "from ProjectReimbursement p where p.reimbursExpenseManag.billno = '"+id+"'";
					query = hs.createQuery(sql);	
					List list2 = query.list();
					for (Object object : list2) {
						ProjectReimbursement detail = (ProjectReimbursement) object;
						String sql1="select billno from Compact_view where ltrim(rtrim(maintContractNo))='"+detail.getMaintContractNo()+"'";
						String billno="";
						List list1=hs.createSQLQuery(sql1).list();
						if(list1!=null && list1.size()>0){
							billno=String.valueOf(list1.get(0));
						}
						if("display".equals(flag)){
							String[] where= {"a.id.typeflag='ProjectReimbursement_ReimburType'"};
							detail.setReimburType(bd.getName("Pulldown", "pullname", "id.pullid",  detail.getReimburType(),where));
						}
						Map map=new HashMap();
						map.put("billno", billno);
						map.put("maintContractNo", detail.getMaintContractNo());
						map.put("busType", detail.getBusType());
						map.put("money", detail.getMoney());
						map.put("num", detail.getNum());
						map.put("reimburType", detail.getReimburType());
						map.put("rem", detail.getRem());
						map.put("maintDivisionBx", detail.getMaintDivisionBx());
						map.put("comName", bd.getName(hs, "Company", "comname", "comid",detail.getMaintDivisionBx()));
						map.put("maintStationBx", detail.getMaintStationBx());
						map.put("storageName",  bd.getName(hs,"Storageid", "storagename", "storageid",detail.getMaintStationBx()));
						projectList.add(map);
					}
					
					//站内报销
					String sql1 = "from MaintStationReimbursement m where m.reimbursExpenseManag.billno = '"+id+"'";
					
					Query query1 = hs.createQuery(sql1);	
					stationList = query1.list();
					for (Object object : stationList) {
						MaintStationReimbursement station = (MaintStationReimbursement) object;
						station.setR1(bd.getName(hs, "Company", "comname", "comid",station.getMaintDivisionBx()));
						station.setR2( bd.getName(hs,"Storageid", "storagename", "storageid",station.getMaintStationBx()));
						if("display".equals(flag)){
							String[] where= {"a.id.typeflag='MaintStationReimbursement_ReimburType'"};
							station.setReimburType(bd.getName("Pulldown", "pullname", "id.pullid",  station.getReimburType(),where));
						}
					}
					
					//分部报销
					String sql2 = "from MaintDivisionReimbursement m where m.reimbursExpenseManag.billno = '"+id+"'";
					
					Query query2 = hs.createQuery(sql2);
					divisionList = query2.list();
					for (Object object : divisionList) {
						MaintDivisionReimbursement division = (MaintDivisionReimbursement) object;
						division.setR1(bd.getName(hs, "Company", "comname", "comid",division.getMaintDivisionBx()));
						if("display".equals(flag)){
							String[] where= {"a.id.typeflag='MaintStationReimbursement_ReimburType'"};
							division.setReimburType(bd.getName("Pulldown", "pullname", "id.pullid",  division.getReimburType(),where));
						}
					}
					
					//非维保报销
					String sql3 = "from NoReimbursement m where m.reimbursExpenseManag.billno = '"+id+"'";
					
					Query query3 = hs.createQuery(sql3);
					noReimList = query3.list();
					for (Object object : noReimList) {
						NoReimbursement nor = (NoReimbursement) object;
						nor.setR1(bd.getName(hs, "Company", "comname", "comid",nor.getBelongsDepart()));
						if("display".equals(flag)){
							String[] where= {"a.id.typeflag='MaintStationReimbursement_ReimburType'"};
							nor.setReimburType(bd.getName("Pulldown", "pullname", "id.pullid",  nor.getReimburType(),where));
						}
					}
					
					request.setAttribute("reimbursExpenseManagBean", master);	
					request.setAttribute("divisionList", divisionList);
					request.setAttribute("noReimList", noReimList);
					request.setAttribute("projectList", projectList);
					request.setAttribute("stationList", stationList);
							
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


	/**
	 * 保存数据方法
	 * @param form
	 * @param request
	 * @return ActionErrors
	 */
	public void addOrUpdate(ActionForm form, HttpServletRequest request,ActionErrors errors) {
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ReimbursExpenseManag master = null;
		ProjectReimbursement project = null;
		MaintStationReimbursement station=null;
		MaintStationReimbursementDetail stationDetail=null;
		MaintDivisionReimbursement division=null;
		MaintDivisionReimbursementDetail divisionDetail=null;
		NoReimbursement noReim=null;
		String id = (String) dform.get("id");
		String billNo = null;

		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			boolean flag = false;// 是否生成维保合同号标志
			
			if (id != null && !id.equals("")) { // 修改		
				hs.createQuery("delete from ProjectReimbursement p where p.reimbursExpenseManag.billno='"+id+"'").executeUpdate();
				hs.createQuery("delete from MaintStationReimbursementDetail sd where sd.maintStationReimbursement.jnlno in (select m.jnlno from MaintStationReimbursement m where m.reimbursExpenseManag.billno='"+id+"')").executeUpdate();
				hs.createQuery("delete from MaintDivisionReimbursementDetail dd where dd.maintDivisionReimbursement.jnlno in (select m.jnlno from MaintDivisionReimbursement m where m.reimbursExpenseManag.billno='"+id+"')").executeUpdate();
				hs.createQuery("delete from MaintStationReimbursement m where m.reimbursExpenseManag.billno='"+id+"'").executeUpdate();
				hs.createQuery("delete from MaintDivisionReimbursement m where m.reimbursExpenseManag.billno='"+id+"'").executeUpdate();
				hs.createQuery("delete from NoReimbursement m where m.reimbursExpenseManag.billno='"+id+"'").executeUpdate();
				master = (ReimbursExpenseManag) hs.get(ReimbursExpenseManag.class, id);
				billNo = master.getBillno();
				
			} else { // 新增
				master = new ReimbursExpenseManag();	
				
				String todayDate = CommonUtil.getToday();
				String year = todayDate.substring(2,4);
				billNo = CommonUtil.getBillno(year,"ReimbursExpenseManag", 1)[0];// 生成流水号		
				
			}
				
			BeanUtils.populate(master, dform.getMap()); // 复制所有属性值
			
			master.setBillno(billNo);// 流水号
//			master.setMaintDivision(userInfo.getComID()); //维保分部id						
//			master.setOperId(userInfo.getUserID());// 录入人
//			master.setOperDate(CommonUtil.getNowTime());// 录入时间
			
			hs.save(master);

			// 项目报销
			String details = (String) dform.get("prds");
			List list1 = JSONUtil.jsonToList(details, "prds");
			for (Object object : list1) {
				project = new ProjectReimbursement();
				BeanUtils.populate(project, (Map)object);
				project.setReimbursExpenseManag(master);
				project.setProjectName("");
				hs.save(project);				
			}
			
			//非维保报销
			String noReims = (String) dform.get("nrms");
			List list4 = JSONUtil.jsonToList(noReims, "nrms");
			for (Object object : list4) {
				noReim = new NoReimbursement();
				BeanUtils.populate(noReim, (Map)object);
				noReim.setReimbursExpenseManag(master);
				hs.save(noReim);				
			}
			
			//站内报销
			String stations = (String) dform.get("msrs");
			List list2 = JSONUtil.jsonToList(stations, "msrs");
			if(list2.size()>0&&list2!=null){
			String[] stationbillno = CommonUtil.getBillno(
					CommonUtil.getToday().substring(2,4),"MaintStationReimbursement", list2.size());
				for(int i=0;i<list2.size();i++){
					station = new MaintStationReimbursement();
					Object object=list2.get(i);
					BeanUtils.populate(station, (Map)object);
					station.setJnlno(stationbillno[i]);
					station.setReimbursExpenseManag(master);
					hs.save(station);
					hs.flush();
					String stationSql="select COUNT(mcd.rowid) as Num,mcm.MaintContractNo from MaintContractDetail mcd,MaintContractMaster mcm where mcd.BillNo in ("+
                                      "select m.BillNo from MaintContractMaster m where m.MaintDivision='"+station.getMaintDivisionBx()+"'"+ 
							          " and m.MaintStation='"+station.getMaintStationBx()+"' and m.ContractStatus in ('ZB','XB')) "+
                                      " and mcm.BillNo=mcd.BillNo group by mcm.MaintContractNo";
					List detailList=hs.createSQLQuery(stationSql).list();
					if(detailList!=null&&detailList.size()>0){
						int num=0;
						for(int y=0;y<detailList.size();y++){
							Object[] detailObject=(Object[]) detailList.get(y);
							num+=(Integer)detailObject[0];
						}
						if(num>0){
							double avgmoney=station.getMoney()/num;
							//System.out.println(station.getMoney()+"/"+num+"="+station.getMoney()/num);
							for(int y=0;y<detailList.size();y++){
								Object[] detailObject=(Object[]) detailList.get(y);
								stationDetail=new MaintStationReimbursementDetail();
								stationDetail.setMaintContractNo((String)detailObject[1]);
								stationDetail.setMaintDivisionBx(station.getMaintDivisionBx());
								stationDetail.setMaintStationBx(station.getMaintStationBx());
								stationDetail.setMaintStationReimbursement(station);
								stationDetail.setNum((Integer)detailObject[0]);
								stationDetail.setMoney(avgmoney*stationDetail.getNum());
								hs.save(stationDetail);
							}
						}
					}
				}
			}
			
			
			//分部报销
			String divisions = (String) dform.get("mdrs");
			List list3 = JSONUtil.jsonToList(divisions, "mdrs");
			if(list3.size()>0&&list3!=null){
				String[] stationbillno = CommonUtil.getBillno(
						CommonUtil.getToday().substring(2,4),"MaintDivisionReimbursement", list3.size());
					for(int i=0;i<list3.size();i++){
						division = new MaintDivisionReimbursement();
						Object object=list3.get(i);
						BeanUtils.populate(division, (Map)object);
						division.setJnlno(stationbillno[i]);
						division.setReimbursExpenseManag(master);
						hs.save(division);
						hs.flush();
						
                        String stationSql="select COUNT(mcd.rowid)as Num,mcm.MaintContractNo,mcm.MaintStation from MaintContractDetail mcd,MaintContractMaster mcm where mcd.BillNo in ("+
	                                      "select m.BillNo from MaintContractMaster m where m.MaintDivision='"+division.getMaintDivisionBx()+"'"+ 
								          " and m.ContractStatus in ('ZB','XB')) "+
	                                      " and mcm.BillNo=mcd.BillNo group by mcm.MaintContractNo,mcm.MaintStation";
						List detailList=hs.createSQLQuery(stationSql).list();
						if(detailList!=null&&detailList.size()>0){
							int num=0;
							for(int y=0;y<detailList.size();y++){
								Object[] detailObject=(Object[]) detailList.get(y);
								num+=(Integer)detailObject[0];
							}
							if(num>0){
								double avgmoney=division.getMoney()/num;
								//System.out.println("DivsionMoney "+division.getMoney()+"/"+num+"="+division.getMoney()/num);
								for(int y=0;y<detailList.size();y++){
									Object[] detailObject=(Object[]) detailList.get(y);
									divisionDetail=new MaintDivisionReimbursementDetail();
									divisionDetail.setMaintContractNo((String)detailObject[1]);
									divisionDetail.setMaintDivisionReimbursement(division);
									divisionDetail.setMaintDivisionBx(division.getMaintDivisionBx());
									divisionDetail.setMaintStationBx((String)detailObject[2]);
									divisionDetail.setNum((Integer)detailObject[0]);
									divisionDetail.setMoney(avgmoney*divisionDetail.getNum());
									hs.save(divisionDetail);
								}
							}
						}
						
					}
				}
			
			
			tx.commit();

		} catch (Exception e1) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			try {
				tx.rollback();
			} catch (HibernateException e2) {
				log.error(e2.getMessage());
				DebugUtil.print(e2, "Hibernate Transaction rollback error!");
			}
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Insert error!");
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}
		
	}
	/**
	 * ajax 级联 分部与维保站
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
//	public void toStorageIDList(ActionMapping mapping, ActionForm form,
// 			HttpServletRequest request, HttpServletResponse response)
//			throws IOException {
//		Session hs = null;
//		String storageid = request.getParameter("storageid");
//		response.setHeader("Content-Type", "text/html; charset=GBK");
//		List list2 = new ArrayList();
//		StringBuffer sb = new StringBuffer();
//		sb.append("<?xml version='1.0' encoding='GBK'?>");
//		sb.append("<root>");
//		try {
//			hs = HibernateUtil.getSession();
//			if (storageid != null && !"".equals(storageid)) {
//				String hql = "select a from Loginuser a where a.storageid like '"
//						+ storageid + "%' and a.enabledflag='Y'";
//				List list = hs.createQuery(hql).list();
//				if (list != null && list.size() > 0) {
//					sb.append("<rows>");
//					for (int i = 0; i < list.size(); i++) {
//						Loginuser user = (Loginuser) list.get(i);
//						sb.append(
//								"<cols name='" + user.getUsername()
//										+ "' value='" + user.getUserid()
//										+ "'>").append("</cols>");
//					}
//					sb.append("</rows>");
//
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		finally {
//			hs.close();
//		}
//		sb.append("</root>");
//
//		response.setCharacterEncoding("gbk");
//		response.setContentType("text/xml;charset=gbk");
//		response.getWriter().write(sb.toString());
//	}
}	