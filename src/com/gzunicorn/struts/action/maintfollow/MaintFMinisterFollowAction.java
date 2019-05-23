package com.gzunicorn.struts.action.maintfollow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
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

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.hibernate.custregistervisitplan.customervisitdispatching.CustomerVisitDispatching;
import com.gzunicorn.hibernate.custregistervisitplan.customervisitfeedback.CustomerVisitFeedback;
import com.gzunicorn.hibernate.custregistervisitplan.customervisitplandetail.CustomerVisitPlanDetail;
import com.gzunicorn.hibernate.custregistervisitplan.customervisitplanmaster.CustomerVisitPlanMaster;
import com.gzunicorn.hibernate.custregistervisitplan.custreturnregistermaster.CustReturnRegisterMaster;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 *  维保分部长跟进
 */
public class MaintFMinisterFollowAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintFMinisterFollowAction.class);
	
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
				SysRightsUtil.NODE_ID_FORWARD + "MaintFMinisterFollow", null);
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

		request.setAttribute("navigator.location","	维保分部长跟进>> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
			HTMLTableCache cache = new HTMLTableCache(session, "MaintFMinisterFollowList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fMaintFMinisterFollow");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("custLevel");
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
			String principalName = tableForm.getProperty("principalName");
			String sdate1=tableForm.getProperty("sdate1");
			String edate1=tableForm.getProperty("edate1");
			String userid= userInfo.getUserID();
			String maintDivision = null;
			String mainStation = null;
			Session hs = null;
			Query query =null;
			String order="";
			try {
				hs = HibernateUtil.getSession();
                String hql="select vcl.Orderby as custLevel,cvpm.companyName,cvpm.principalName,cvpm.principalPhone,"
                		+ "cvpm.maintDivision,cvpm.maintStation,cvpd.visitDate,cvpd.visitStaff,cvpd.rem,"
                		+ "cvpd.jnlno,cvf.numno as isVisit,cvpd.visitPosition,cvpd.submitType,cvpd.auditStatus from CustomerVisitPlanMaster cvpm left join "
                		+ "ViewCustLevel vcl on vcl.custLevel = cvpm.custLevel, loginuser l left join storageid s on l.storageid = s.storageid, company c ,CustomerVisitPlanDetail cvpd "
                		+ "left join CustomerVisitFeedback cvf on cvf.jnlno=cvpd.jnlno and  "
                		+ "cvf.numno in (select MAX(numno) from CustomerVisitFeedback group by jnlno)"
                		+ "where cvpm.billno=cvpd.billno and cvpd.submitType='Y' and cvpd.VisitPosition ='20' " +
                		"and isnull(cvpd.fkOperId,'')<>'' and ISNULL(cvpd.fMinisterFollow,'')=''" +
                		"and c.comid = s.comid  and cvpm.MaintDivision = c.comid  and  l.userid  like '%"+userid+"%'";
                //System.out.println(">>>>"+hql);
				if (companyName != null && !companyName.equals("")) {
					hql+=" and cvpm.companyName like '%"+companyName.trim()+"%'";
				}
				
				if (principalName != null && !principalName.equals("")) {
					hql+=" and cvpm.principalName like '%"+principalName.trim()+"%'";
				}
														
				if (sdate1 != null && !sdate1.equals("")) {
					hql+=" and cvpd.visitDate >= '"+sdate1.trim()+"'";
				}
				
				if (edate1 != null && !edate1.equals("")) {
					hql+=" and cvpd.visitDate <= '"+edate1.trim()+"'";
				}
				
				         hql+="UNION " +
				         "select vcl.Orderby as custLevel,cvpm.companyName,cvpm.principalName,cvpm.principalPhone" +
				         ",cvpm.maintDivision,cvpm.maintStation,cvpd.visitDate,cvpd.visitStaff,cvpd.rem," +
				         "cvpd.jnlno,cvf.numno as isVisit,cvpd.visitPosition,cvpd.submitType,cvpd.auditStatus " +
				         "from   CustomerVisitPlanMaster cvpm join loginuser l on l.grcid=cvpm.MaintDivision and l.userid  like '%"+userid+"%' " +
				         "left join ViewCustLevel vcl on vcl.custLevel = cvpm.custLevel,CustomerVisitPlanDetail cvpd " +
				         "left join CustomerVisitFeedback cvf on cvf.jnlno=cvpd.jnlno " +
				         "and  cvf.numno in (select MAX(numno) from CustomerVisitFeedback group by jnlno) " +
				         "where cvpm.billno=cvpd.billno and cvpd.submitType='Y' and cvpd.VisitPosition in('22','100') " +
				         "and managerfollow <>''and isnull(cvpd.fkOperId,'')<>'' and ISNULL(cvpd.fMinisterFollow,'')=''";
				         
				 		if (companyName != null && !companyName.equals("")) {
							hql+=" and cvpm.companyName like '%"+companyName.trim()+"%'";
						}
						
						if (principalName != null && !principalName.equals("")) {
							hql+=" and cvpm.principalName like '%"+principalName.trim()+"%'";
						}
																
						if (sdate1 != null && !sdate1.equals("")) {
							hql+=" and cvpd.visitDate >= '"+sdate1.trim()+"'";
						}
						
						if (edate1 != null && !edate1.equals("")) {
							hql+=" and cvpd.visitDate <= '"+edate1.trim()+"'";
						}
				 
				if (table.getIsAscending()) {
					order=" order by "+table.getSortColumn()+" desc";
				} else {
					order=" order by "+table.getSortColumn();
				}
				//System.out.println(hql+order);
				query = hs.createSQLQuery(hql+order);
				
				table.setVolume(query.list().size());// 查询得出数据记录数;
				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				cache.check(table);
				List MaintFMinisterFollowList =new ArrayList();
				List list=query.list();
				if(list!=null&list.size()>0)
				{
					int length=list.size();
					for(int i=0;i<length;i++)
					{
						Object[] objects=(Object[]) list.get(i);
						HashMap map=new HashMap();
						map.put("custLevel", bd.getName(hs, "ViewCustLevel", "CustLevel", "Orderby",(String) objects[0]));
						map.put("companyName", objects[1]);
						map.put("principalName", objects[2]);
						map.put("principalPhone", objects[3]);
						map.put("maintDivision", bd.getName(hs, "company", "ComName", "ComID", (String) objects[4]));
						map.put("maintStation", bd.getName(hs, "StorageID", "StorageName", "StorageID", (String) objects[5]));
						map.put("visitDate", objects[6]);
						map.put("visitPosition",bd.getName(hs, "Class1", "Class1Name", "Class1ID",(String) objects[11]));						
						map.put("visitStaff", bd.getName(hs, "Loginuser", "username", "userid", (String) objects[7]));
						
						
						map.put("rem", objects[8]);
						map.put("jnlno", objects[9]);
						
						
						String submitType=(String) objects[12];
						if(submitType!=null&&submitType.trim().equals("Y")){
							 map.put("submitType", "是");
						}else{
							 map.put("submitType", "否");
						}
						

						MaintFMinisterFollowList.add(map);
					}
				}
				

				table.addAll(MaintFMinisterFollowList);
			    	request.setAttribute("CompanyList", Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
			    
			    if (maintDivision != null && !maintDivision.equals("")&&!maintDivision.equals("%")) {
				 String hql1="select a from Storageid a,Company b where a.comid = b.comid and a.comid = '"+maintDivision+"'" +
				 		" and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
					List mainStationList=hs.createQuery(hql1).list();
					if(mainStationList!=null && mainStationList.size()>0){
						request.setAttribute("mainStationList", mainStationList);
				    }
				}
			    String sql="from Class1 c where c.r1='Y'and c.enabledFlag='Y'";
				   List class1List=hs.createQuery(sql).list();
				
				   
				session.setAttribute("class1List", class1List);
			    
			    
				session.setAttribute("MaintFMinisterFollowList", table);

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e1) {
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
			forward = mapping.findForward("MaintFMinisterFollowList");
		
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

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		request.setAttribute("navigator.location", "维保分部长跟进 >> 跟进");
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				   String sql="select cvpm,cvpd from CustomerVisitPlanMaster cvpm,CustomerVisitPlanDetail cvpd where cvpm.billno=cvpd.billno and cvpd.jnlno='"+id+"'";
					   List list=hs.createQuery(sql).list();		
					    
					   CustomerVisitPlanDetail cvpd=null;
					   CustomerVisitPlanMaster cvpm=null;
					   if(list!=null&&list.size()>0){
						  Object[] objects=(Object[]) list.get(0);
						  cvpm=(CustomerVisitPlanMaster) objects[0];
						  cvpd=(CustomerVisitPlanDetail) objects[1];
						  cvpm.setMaintDivision(bd.getName(hs, "company", "ComName", "ComID", cvpm.getMaintDivision()));
						  cvpm.setMaintStation(bd.getName(hs, "StorageID", "StorageName", "StorageID", cvpm.getMaintStation()));
						  cvpd.setBillno(cvpm.getBillno());
						  cvpd.setVisitStaff(bd.getName(hs, "Loginuser", "username", "userid", cvpd.getVisitStaff()));
						  cvpd.setFkOperId(bd.getName(hs, "Loginuser", "username", "userid", cvpd.getFkOperId()));
						  cvpd.setVisitPosition(bd.getName(hs, "Class1", "Class1Name", "Class1ID", cvpd.getVisitPosition()));
						  cvpd.setAuditOperid(bd.getName(hs, "Loginuser", "username", "userid", cvpd.getAuditOperid()));
						  /*cvpd.setfMinisterFollow(bd.getName(hs, "Loginuser", "username", "userid",userInfo.getUserName()));*/
						  cvpd.setManagerFollow(bd.getName(hs, "Loginuser", "username", "userid",cvpd.getManagerFollow()));
						  cvpd.setzMinisterFollow(bd.getName(hs, "Loginuser", "username", "userid",cvpd.getzMinisterFollow()));
						  cvpd.setfMinisterFollow(userInfo.getUserName());
                          cvpd.setfMinisterFollowDate(CommonUtil.getNowTime());
                          /*cvpd.setBhDate(CommonUtil.getNowTime());*/
						 }
					   
					   String hql="from CustomerVisitFeedback cvf where cvf.customerVisitPlanDetail.jnlno='"+id+"'";
					   List list2=hs.createQuery(hql).list();
					   List cvpfList=new ArrayList();
					   if(list2!=null&&list2.size()>0)
					   {  						 						   
						   request.setAttribute("cvpfList", list2);
					   }
					   
					   String hql1="from CustomerVisitDispatching cvd where cvd.customerVisitPlanDetail.jnlno='"+id+"'";
					   List tocvdlist=hs.createQuery(hql1).list();
					   List cvdlist=new ArrayList();
					   if(tocvdlist!=null&&tocvdlist.size()>0){
					     for(int i=0;i<tocvdlist.size();i++){
					    	 CustomerVisitDispatching cvd=(CustomerVisitDispatching) tocvdlist.get(i);
					    	 cvd.setTransferId(bd.getName(hs, "Loginuser", "username", "userid", cvd.getTransferId()));
					    	 cvd.setVisitStaff(bd.getName(hs, "Loginuser", "username", "userid", cvd.getVisitStaff()));
					    	 //System.out.println(bd.getName(hs, "Loginuser", "username", "userid", cvd.getVisitStaff()));
					    	 
					    	 cvd.setVisitPosition(bd.getName(hs, "Class1", "Class1Name", "Class1ID", cvd.getVisitPosition()));
					    	 cvdlist.add(cvd);
					     }
					     request.setAttribute("cvdList", cvdlist);
					   }
					  
					   request.setAttribute("cvpmbean", cvpm);
					   request.setAttribute("cvpdbean", cvpd);
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
			forward = mapping.findForward("MaintFMinisterFollowAdd");

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	/**
	 * 紧急级别修改
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
		
		String jnlno = request.getParameter("jnlno");
		String fministerFollowDate = request.getParameter("fMinisterFollowDate");
		String fministerFollowRem = request.getParameter("fministerFollowRem");
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			CustomerVisitPlanDetail cvpd = (CustomerVisitPlanDetail) hs.get(CustomerVisitPlanDetail.class, jnlno.trim());
				cvpd.setfMinisterFollow(userInfo.getUserID());
				cvpd.setfMinisterFollowDate(fministerFollowDate);
				cvpd.setfMinisterFollowRem(fministerFollowRem);						
			hs.update(cvpd);
			tx.commit();
		} catch (Exception e1) {
			tx.rollback();
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
        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
		forward = mapping.findForward("returnList");
			
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
			
	
}
