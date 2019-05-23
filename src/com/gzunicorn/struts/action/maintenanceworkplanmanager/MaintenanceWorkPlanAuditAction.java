package com.gzunicorn.struts.action.maintenanceworkplanmanager;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
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
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplandetail.MaintenanceWorkPlanDetail;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplanmaster.MaintenanceWorkPlanMaster;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;


public class MaintenanceWorkPlanAuditAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintenanceWorkPlanAuditAction.class);

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
				SysRightsUtil.NODE_ID_FORWARD + "maintenanceworkplanaudit", null);
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

		request.setAttribute("navigator.location", " 维保合同作业计划审核>> 查询列表");		
			ActionForward forward = null;
			
			HttpSession session = request.getSession();
			ServeTableForm tableForm = (ServeTableForm) form;
			String action = tableForm.getAction();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			request.setAttribute("roleid", userInfo.getRoleID());
			
				HTMLTableCache cache = new HTMLTableCache(session, "maintenanceWorkPlanAuditList");

				DefaultHTMLTable table = new DefaultHTMLTable();
				table.setMapping("fMaintenanceWorkPlanAudit");
				table.setLength(SysConfig.HTML_TABLE_LENGTH);
				cache.updateTable(table);
				table.setSortColumn("contractEdate");
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
				
				String billNo = tableForm.getProperty("billNo");// 流水号
				String maintContractNo = tableForm.getProperty("maintContractNo");// 维保合同号
				String attn = tableForm.getProperty("attn");// 经办人
				String maintDivision = tableForm.getProperty("maintDivision");// 所属维保站			
				String auditStatus = tableForm.getProperty("auditStatus");// 审核状态
				String mainStation = tableForm.getProperty("mainStation");
				if(auditStatus==null)
			    {
			    	auditStatus="N";
			    	tableForm.setProperty("auditStatus", "N");
			    }
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
					
					//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
					String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
					List vmlist=hs.createSQLQuery(sqlss).list();
					
					List mainStationList=new ArrayList();
					//维保站下拉框  A03=维保经理,A48=维保站文员,维保站长 A49 
					if(vmlist!=null && vmlist.size()>0){
						String hql="select a from Storageid a where "
								+ "(a.storageid= '"+userInfo.getStorageId()+"' or "
								+ "a.storageid in(select parentstorageid from Storageid a where a.storageid= '"+userInfo.getStorageId()+"')) "
								+ "and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";	
						mainStationList=hs.createQuery(hql).list();
						
					}else{
						String hql="select a from Storageid a where a.comid like '"+maintDivision+"' " +
								"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
						mainStationList=hs.createQuery(hql).list();
					  
						 Storageid storid=new Storageid();
						 storid.setStorageid("%");
						 storid.setStoragename("全部");
						 mainStationList.add(0,storid);
					}
					request.setAttribute("mainStationList", mainStationList);

					String sql = "select a,b.username as attn,p.pullname as contractStatus," +
							"c.comname as maintDivision,isnull(mwpm.checkflag,'N') as checkflag,isnull(mwpm.submitType,'N') as submitType,s.storagename as storagename"+
							" from MaintContractMaster a,Loginuser b,Company c,Pulldown p,MaintenanceWorkPlanMaster mwpm,Storageid s" + 
							" where a.attn = b.userid"+
							" and a.maintDivision = c.comid and a.maintStation = s.storageid"+
							" and mwpm.rowid=(select MIN(mcd.rowid) from MaintContractDetail mcd where mcd.billNo=a.billNo)"+
							" and a.contractStatus = p.id.pullid"+
							" and mwpm.submitType='Y'"+
							" and p.id.typeflag = 'MaintContractMaster_ContractStatus'"+ 
							" and a.billNo not in (select mcd1.billNo from MaintContractDetail mcd1 where mcd1.assignedSignFlag is null or mcd1.assignedSignFlag='')";
					
					if (billNo != null && !billNo.equals("")) {
						sql += " and a.billNo like '%"+billNo.trim()+"%'";
					}
					if (maintContractNo != null && !maintContractNo.equals("")) {
						sql += " and a.maintContractNo like '%"+maintContractNo.trim()+"%'";
					}
					if (attn != null && !attn.equals("")) {
						sql += " and (a.attn like '%"+attn.trim()+"%' or b.username like '%"+attn.trim()+"%')";
					}
					if (maintDivision != null && !maintDivision.equals("")) {
						sql += " and a.maintDivision like '"+maintDivision.trim()+"'";
					}
					if (mainStation != null && !mainStation.equals("")) {
						sql+=" and a.maintStation like '"+mainStation.trim()+"'";
					}
					if (auditStatus != null && !auditStatus.equals("")) {
						if(auditStatus.equals("Y")){
							sql += " and mwpm.checkflag like '"+auditStatus.trim()+"'";
						}else{
							sql += " and (mwpm.checkflag is null or mwpm.checkflag = '' or mwpm.checkflag = 'N')";
						}
						
					}
					
					String order = " order by  a."+ table.getSortColumn();
					
					if (table.getIsAscending()) {
						sql += order + " asc";
					} else {
						sql += order + " desc";
					}
					query = hs.createQuery(sql);
					table.setVolume(query.list().size());// 查询得出数据记录数;

					// 得出上一页的最后一条记录数号;
					query.setFirstResult(table.getFrom()); // pagefirst
					query.setMaxResults(table.getLength());

					cache.check(table);

					List list = query.list();
					List maintenanceWorkPlanCustomizeList = new ArrayList();
					for (Object object : list) {
						Object[] objs = (Object[])object;
						MaintContractMaster master = (MaintContractMaster) objs[0];
						master.setAttn(String.valueOf(objs[1]));
						master.setContractStatus(String.valueOf(objs[2]));
						master.setMaintDivision(String.valueOf(objs[3]));
						master.setAuditStatus(String.valueOf(objs[4]));
						master.setR1(String.valueOf(objs[5]));
						master.setMaintStation(String.valueOf(objs[6]));
						maintenanceWorkPlanCustomizeList.add(master);
					}

					table.addAll(maintenanceWorkPlanCustomizeList);
					session.setAttribute("maintenanceWorkPlanAuditList", table);
					// 合同性质下拉框列表
					request.setAttribute("contractNatureOfList", bd.getPullDownList("MaintContractMaster_ContractNatureOf"));
					// 合同状态下拉框列表
					request.setAttribute("contractStatusList", bd.getPullDownList("MaintContractMaster_ContractStatus"));
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
				forward = mapping.findForward("maintContractList");
			
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
		request.setAttribute("navigator.location",
				messages.getMessage(locale, navigation));
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
		
		request.setAttribute("navigator.location"," 维保合同作业计划审核>> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		String id = request.getParameter("id");
		if(id==null||id.equals(""))
		{
			id=(String) request.getAttribute("id");
		}		
		Session hs = null;
		MaintContractMaster mcm=null;
		List maintContractDetailList=new ArrayList();
		try{
			hs = HibernateUtil.getSession();
			
			String sql="from MaintContractMaster mcm where mcm.billNo="+id;
			
	        List list = hs.createQuery(sql).list();
	        
	        if(list!=null&&list.size()>0)
	        {
	        	mcm=(MaintContractMaster) list.get(0);
	        	mcm.setAttn(bd.getName(hs, "Loginuser","username", "userid",mcm.getAttn()));
	        	mcm.setMaintDivision(bd.getName(hs, "Company", "comname", "comid",mcm.getMaintDivision()));// 维保分部
	        	mcm.setMaintStation(bd.getName(hs, "StorageID", "StorageName", "StorageID",mcm.getMaintStation()));// 维保站
		
	        }
			String sql1="select mcd,si.storagename,lu.username,mwpm.billno,mwpm.checkoperid,mwpm.checkdate,mwpm.maintLogic " +
					"from MaintContractDetail mcd,Loginuser lu,Storageid si,MaintenanceWorkPlanMaster mwpm "
					+ "where mwpm.rowid=mcd.rowid "
					+ " and mcd.maintPersonnel=lu.userid "
					+ " and mcd.assignedMainStation=si.storageid "
					+ " and mcd.assignedSignFlag='Y' "
					+ " and mcd.billNo="+id;
			List mcdlist = hs.createQuery(sql1).list();
			String checkoperid="";
			String checkoperdate="";
			if(mcdlist!=null&&mcdlist.size()>0)
	        {
	        	for(int i=0;i<mcdlist.size();i++)
	        	{
	        		Object[] objects=(Object[]) mcdlist.get(i);
	        		MaintContractDetail mcd=(MaintContractDetail) objects[0];
	        		mcd.setAssignedMainStation((String)objects[1]);
	        		mcd.setMaintPersonnel((String)objects[2]);
	        		mcd.setR1((String) objects[3]);
	        		mcd.setR2((String) objects[6]);
	        		maintContractDetailList.add(mcd);
	        		
	        		checkoperid=(String) objects[4];
	        		checkoperdate=(String) objects[5];
	        	}
	        }
			checkoperid=bd.getName(hs, "Loginuser","username", "userid",checkoperid);
			
			request.setAttribute("checkoperid", checkoperid);
			request.setAttribute("checkoperdate", checkoperdate);
			
			request.setAttribute("maintContractMasterBean", mcm);
			request.setAttribute("maintContractDetailList", maintContractDetailList);
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
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		
		request.setAttribute("display", "yes");
		
		String typejsp= request.getParameter("typejsp");
		if(typejsp!=null){
			request.setAttribute("typejsp", typejsp);
		}
		
		forward = mapping.findForward("maintContractDisplay");
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
		HttpServletResponse response) throws DataStoreException {
	
	request.setAttribute("navigator.location","维保合同作业计划审核 >> 审核");	
	DynaActionForm dform = (DynaActionForm) form;		
	String id = request.getParameter("id");
	if(id==null||id.equals(""))
	{
		id=(String) request.getAttribute("id");
	}
	ActionErrors errors = new ActionErrors();		
	ActionForward forward = null;
	HttpSession session = request.getSession();
	ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
	Session hs = null;
	MaintContractMaster mcm=null;
	List maintContractDetailList=new ArrayList();
	try{
		hs = HibernateUtil.getSession();
		String sql="from MaintContractMaster mcm where mcm.billNo="+id;
        List list = hs.createQuery(sql).list();
        
        if(list!=null&&list.size()>0)
        {
        	mcm=(MaintContractMaster) list.get(0);
        	mcm.setAttn(bd.getName(hs, "Loginuser","username", "userid",mcm.getAttn()));
        	mcm.setMaintDivision(bd.getName(hs, "Company", "comname", "comid",mcm.getMaintDivision()));// 维保分部
        	mcm.setMaintStation(bd.getName(hs, "StorageID", "StorageName", "StorageID",mcm.getMaintStation()));// 维保站
            mcm.setR1(userInfo.getUserName());
            mcm.setR2(CommonUtil.getNowTime());  
        }
		String sql1="select mcd,si.storagename,lu.username,mwpm.billno," +
				"mwpm.checkflag,mwpm.checkoperid,mwpm.checkdate,mwpm.operid,mwpm.operdate,mwpm.maintLogic " +
				"from MaintContractDetail mcd,Loginuser lu,Storageid si,MaintenanceWorkPlanMaster mwpm "
				+ "where mwpm.rowid=mcd.rowid "
				+ " and mcd.maintPersonnel=lu.userid "
				+ " and mcd.assignedMainStation=si.storageid "
				+ " and mcd.assignedSignFlag='Y' "
				+ " and mcd.billNo="+id;
		List mcdlist = hs.createQuery(sql1).list();
		String ischeckstr="N";
		String checkoperid="";
		String checkoperdate="";
		String operid="";
		String operdate="";
		if(mcdlist!=null&&mcdlist.size()>0)
        {
        	for(int i=0;i<mcdlist.size();i++)
        	{
        		Object[] objects=(Object[]) mcdlist.get(i);
        		MaintContractDetail mcd=(MaintContractDetail) objects[0];
        		mcd.setAssignedMainStation((String)objects[1]);
        		mcd.setMaintPersonnel((String)objects[2]);
        		mcd.setR1((String) objects[3]);
        		mcd.setR2((String) objects[4]);
        		mcd.setR3((String) objects[9]);
        		maintContractDetailList.add(mcd);
        		
        		ischeckstr=(String) objects[4];
        		checkoperid=(String) objects[5];
        		checkoperdate=(String) objects[6];
        		operid=(String) objects[7];
        		operdate=(String) objects[8];
        	}
        }
		
		if(ischeckstr!=null && ischeckstr.trim().equals("Y")){
			checkoperid=bd.getName(hs, "Loginuser","username", "userid",checkoperid);
			mcm.setR1(checkoperid);
            mcm.setR2(checkoperdate); 
		}
		operid=bd.getName(hs, "Loginuser","username", "userid",operid);
		
		request.setAttribute("operidkk", operid);
		request.setAttribute("operdatekk", operdate);
		
		request.setAttribute("ischeckstr", ischeckstr);
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
	
	

	
	if (!errors.isEmpty()) {
		saveErrors(request, errors);
	}
	request.setAttribute("maintContractMasterBean", mcm);
	request.setAttribute("maintContractDetailList", maintContractDetailList);
	
	forward = mapping.findForward("maintContractModify");
	
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
	DynaActionForm dform = (DynaActionForm) form;
	toUpdate(form,request,errors);// 新增或修改记录
	
	
	try {
		if (errors.isEmpty()) {
			String isreturn = request.getParameter("isreturn");
			if(isreturn!=null && isreturn.trim().equals("Y")){
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("auditing.succeed"));
			}else{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("reject.succeed"));
			}
			forward = mapping.findForward("returnList");
		} else {
			request.setAttribute("error", "Yes");

			request.setAttribute("id", dform.get("billNo"));
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
 * 保存数据方法
 * @param form
 * @param request
 * @return ActionErrors
 */
public void toUpdate(ActionForm form, HttpServletRequest request,ActionErrors errors) {
	
	DynaActionForm dform = (DynaActionForm) form;

	HttpSession session = request.getSession();
	ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
	
	String[] wmpmbillnos =request.getParameterValues("wmpmbillno");

	Session hs = null;
	Transaction tx = null;
		
	try {
		hs = HibernateUtil.getSession();
		tx = hs.beginTransaction();
		String isreturn = request.getParameter("isreturn");
		String mbillno="";
		List mlist=null;
		if(wmpmbillnos!=null&&wmpmbillnos.length>0)
		{
			for(int i=0;i<wmpmbillnos.length;i++)
			{
				MaintenanceWorkPlanMaster mwpm = (MaintenanceWorkPlanMaster) hs.get(MaintenanceWorkPlanMaster.class, wmpmbillnos[i].trim());
			
				if(isreturn!=null && isreturn.trim().equals("Y")){
					mwpm.setCheckflag("Y");//审核通过
					mwpm.setCheckoperid(userInfo.getUserID());
					mwpm.setCheckdate(CommonUtil.getNowTime());
					String hql="update MaintenanceWorkPlanDetail mwpd set mwpd.releasedTime = '"+CommonUtil.getNowTime()
							   + "' where mwpd.maintenanceWorkPlanMaster.billno = '"+wmpmbillnos[i].trim()+"'";
					hs.createQuery(hql).executeUpdate();
					
//					String hql2="select a.HistoryBillNo,a.billno from MaintContractMaster a,MaintContractDetail b where b.rowid='"+mwpm.getRowid()+"' and a.billno=b.billno";
//					mlist=hs.createQuery(hql2).list();
//					if(mlist!=null && mlist.size()>0){
//						Object obj[]=(Object[])mlist.get(0);
//						if(obj[0]!=null && !obj[0].equals("")){
//							//删除退保的保养 工作计划
//							CommonUtil.deleMaintenanceWorkPlan(obj[0].toString(), obj[1].toString(), "", userInfo, errors);
//						}
//						
//					}
					
				}else{
					mwpm.setSubmitType("R");//驳回
				}
				hs.update(mwpm);
				hs.flush();
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
			String isreturn = request.getParameter("isreturn");
		} catch (HibernateException hex) {
			log.error(hex.getMessage());
			DebugUtil.print(hex, "Hibernate close error!");
		}
	}
	
 }

/**
 * 维保计划查看方法
 * 
 * @param mapping
 * @param form
 * @param request
 * @param response
 * @return
 */
public ActionForward toMaintenanceWorkPlanDisplayRecord(ActionMapping mapping,
		ActionForm form, HttpServletRequest request,
		HttpServletResponse response) {

	DynaActionForm dform = (DynaActionForm) form;
	ActionErrors errors = new ActionErrors();
	request.setAttribute("navigator.location", "维保作业计划 >> 查看");

	ActionForward forward = null;
	String id = (String) dform.get("id");
	Session hs = null;
	MaintenanceWorkPlanMaster mwp = null;
	HashMap map= new HashMap();
	List mwpList = null;
	Object object=null;
	List mwpdList=new ArrayList();
	String maintPersonnel =null;
	if (id != null) {
		try {
			hs = HibernateUtil.getSession();
			String sql="select mcm,mcd,l.username,si.storagename,c.comname,mwpm.maintLogic"
					+ " from MaintenanceWorkPlanMaster mwpm,MaintContractDetail mcd,"
					+ " MaintContractMaster mcm,Loginuser l,Storageid si,Company c "
					+ " where c.comid=maintDivision"
					+ " and si.storageid=mcd.assignedMainStation"
					+ " and l.userid=mcd.maintPersonnel"
					+ " and mwpm.rowid=mcd.rowid"
					+ " and mcd.billNo=mcm.billNo"
					+ " and mwpm.billno= "+id;
			
			mwpList = hs.createQuery(sql).list();
			if(mwpList!=null&&mwpList.size()>0)
			{
				Object[] objects= (Object[]) mwpList.get(0);
                map.putAll(BeanUtils.describe(objects[0]));
                map.putAll(BeanUtils.describe(objects[1]));
                maintPersonnel=(String) map.get("maintPersonnel");
                map.put("maintPersonnelName", objects[2]);
                map.put("assignedMainStation", objects[3]);
                map.put("maintDivision", objects[4]);
                map.put("maintLogic", objects[5]);
				object=map;
			}
			
			
			
			String sql1="from MaintenanceWorkPlanDetail mwpd where mwpd.maintenanceWorkPlanMaster.billno='"+id+"' order by mwpd.maintDate ";
			List list=hs.createQuery(sql1).list();
			  if(list!=null&&list.size()>0)
			  {
				  for(int i=0;i<list.size();i++)
				  {
					  MaintenanceWorkPlanDetail mwpd= (MaintenanceWorkPlanDetail) list.get(i);
					  HashMap map1 =new HashMap();
					  map1.put("singleno", mwpd.getSingleno());
					  map1.put("maintDate", mwpd.getMaintDate());
					  map1.put("week", mwpd.getWeek());
					  if(mwpd.getMaintType()!=null&&!mwpd.getMaintType().equals(""))
					  {
						  if(mwpd.getMaintType().trim().equals("halfMonth"))
						  {
							  map1.put("maintType", "半月保养"); 						  
						  }
						  if(mwpd.getMaintType().trim().endsWith("quarter"))
						  {
							  map1.put("maintType", "季度保养");
							  map1.put("style","oddRow3");
						  } 
						  if(mwpd.getMaintType().trim().endsWith("halfYear"))
						  {
							  map1.put("maintType", "半年保养");
							  map1.put("style","oddRow3");
						  }
						  if(mwpd.getMaintType().trim().equals("yearDegree"))					          
					      {
							  map1.put("maintType", "年度保养");
							  map1.put("style","oddRow3");
					      }
					  }

					  map1.put("maintDateTime", mwpd.getMaintDateTime());
					  
					  if(mwpd.getMaintEndTime()!=null&&!mwpd.getMaintEndTime().equals(""))
					  {
					  map1.put("maintSETime", mwpd.getMaintStartTime()+"|"+mwpd.getMaintEndTime());
					  }
					  else
					  {
						  map1.put("maintSETime", mwpd.getMaintStartTime()); 
					  }
					  
					  String sql2="select maintDateTime from MaintContractDetail mcd,MaintenanceWorkPlanMaster mwpm,MaintenanceWorkPlanDetail mwpd where mcd.rowid=mwpm.rowid "
					  		+ "and mwpm.billno=mwpd.billno and mcd.MaintPersonnel = '"+maintPersonnel+"' and mwpd.MaintDate ='"+mwpd.getMaintDate()+"'";
					 
					  int sumMaintDateTime=0;
					 List maintDateTimeList=hs.createSQLQuery(sql2).list();
					 if(maintDateTimeList!=null && maintDateTimeList.size()>0)
					 {
						for(int i1=0;i1<maintDateTimeList.size();i1++)
						{
							String  mdt=(String) maintDateTimeList.get(i1);
							sumMaintDateTime+=Integer.valueOf(mdt.trim());
							
						}
					 }
					  
					 map1.put("sumMaintDateTime", sumMaintDateTime);
					  mwpdList.add(map1); 
				  }
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
		
		request.setAttribute("mwpBean",
				object);
		request.setAttribute("mwpList",
				mwpdList);
		forward = mapping.findForward("toMaintenanceWorkPlan");

	}

	if (!errors.isEmpty()) {
		saveErrors(request, errors);
	}
	return forward;
}
/**
 * 重置维保作业计划
 */
public ActionForward tochongzhiRecord(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

	ActionErrors errors = new ActionErrors();				
	ActionForward forward = null;
	DynaActionForm dform = (DynaActionForm) form;
	
	String id=request.getParameter("id");

	Session hs = null;
	Transaction tx = null;
		
	try {
		hs = HibernateUtil.getSession();
		tx = hs.beginTransaction();
		
		String sql="update b set " +
				"b.SubmitType='N',b.checkflag='N',b.checkoperid=null,b.checkdate=null " +
				"from MaintContractDetail a,MaintenanceWorkPlanMaster b " +
				"where a.billno='"+id.trim()+"' and a.rowid=b.rowid";
		hs.connection().prepareStatement(sql).executeUpdate();
		
		tx.commit();

		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","重置计划成功！"));
		forward = mapping.findForward("returnList");
	} catch (Exception e) {
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","重置计划失败！"));
		e.printStackTrace();
	}finally {
		try {
			hs.close();
		} catch (HibernateException hex) {
			DebugUtil.print(hex, "Hibernate close error!");
		}
	}
	
	if (!errors.isEmpty()) {
		saveErrors(request, errors);
	}
	return forward;
}

}


