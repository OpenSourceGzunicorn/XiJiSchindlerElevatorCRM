//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.gzunicorn.struts.action.taskexamplesset;

 import java.io.IOException;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.taskmgmt.exe.TaskInstance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.logic.HLBaseDataImpl;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * MyEclipse Struts Creation date: 07-19-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/roleAction" name="roleForm" scope="request"
 *                validate="true"
 */
public class TaskExamplesSetAction extends DispatchAction {
	Log log = LogFactory.getLog(TaskExamplesSetAction.class);

	BaseDataImpl bd = new BaseDataImpl();

	// pango20120716
	HLBaseDataImpl hlbd = new HLBaseDataImpl();

	// --------------------------------------------------------- Instance
	// Variables

	// --------------------------------------------------------- Methods

	/**
	 * Method execute
	 * 由 Struts-config.XML 跳转过来;
	 * 判断 执行小页面查询 还是 大页面查询；
	 * 用户权限控制；
	 * 后台调试： 打印执行的方法 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		/************开始用户权限过滤************/
		SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "taskexamplesset",null);
		/************结束用户权限过滤************/

		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchRecord";	
		}
		DebugUtil.printDoBaseAction("TaskExamplesSetAction",name,"start");
		ActionForward forward= dispatchMethod(mapping, form, request, response, name);
		DebugUtil.printDoBaseAction("TaskExamplesSetAction",name,"end");
		return forward;
	}
	
	/**
	 * Get the navigation description from the properties file by navigation key;
	 * 导航条
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
	 * Method toPrepareUpdateRecord execute,prepare data for update page
	 * 进入修改页面，准备保存前要显示的数据或页面 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		this.setNavigation(request, "navigator.base.TaskExamplesSet.modify");
		
		DynaActionForm dform = (DynaActionForm) form;
		DecimalFormat mat = new DecimalFormat("0.00");		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		
		ActionErrors errors = new ActionErrors();
	
		ActionForward forward = null;
		String id = null;
		if(dform.get("isreturn") != null && dform.get("isreturn").equals("N"))
		{
			id = (String) dform.get("taskid");
		}
		else
		{
			id = (String) dform.get("id");
		}
		
		Session hs = null;
		List rslist=new ArrayList();
		HashMap remap=null;
		HashMap resultmap=new HashMap();;
		ResultSet resultset=null;
		if (id != null) {
			if(request.getAttribute("error") == null || request.getAttribute("error").equals(""))
			{
				dform.set("taskid",id);
				try {
					String sql="exec Sp_JbpmTaskInstanceset '%','"+id+"','%',1";
					hs = HibernateUtil.getSession();
					resultset=hs.connection().createStatement().executeQuery(sql);	
					String userid="";
					if(resultset.next()){								
						resultmap.put("taskid",resultset.getString("taskid"));
						resultmap.put("taskname",resultset.getString("taskname"));
						userid=resultset.getString("actorid");
						resultmap.put("actorid",userid);
						resultmap.put("creatdate",resultset.getString("creatdate"));						
						resultmap.put("enddate",resultset.getString("enddate"));
						resultmap.put("isopenid",resultset.getString("isopen"));
						resultmap.put("token",resultset.getString("token"));
						resultmap.put("nodename",resultset.getString("nodename"));						
						resultmap.put("actorname",resultset.getString("actorname"));
					}
	
					if(userid!=null && userid.length()>0){
						dform.set("userid",userid);
						dform.set("username",bd.getName("ViewLoginUserInfo","userName","userID",userid));
					}
					
					String sql2="select b.actorid_ as actorid,v.username as username" +
							"	from JBPM_POOLEDACTOR b" +
							"	inner join	JBPM_TASKACTORPOOL c on b.ID_=c.POOLEDACTOR_" +
							"  	inner join      JBPM_TASKINSTANCE a  on c.TASKINSTANCE_=a.ID_" +
							"	inner join      viewloginuserinfo v  on v.userid=b.actorid_ " +
							"    	where	a.id_='"+id+"'" +
							"        and 	a.ISSUSPENDED_	<>	1" +
							"        and 	a.ISOPEN_	=	1" +
							"		order by b.actorid_";
					resultset=hs.connection().createStatement().executeQuery(sql2);
					while(resultset.next()){
						remap=new HashMap();
						remap.put("userid2",resultset.getString("actorid"));
						remap.put("username2",resultset.getString("username"));
						rslist.add(remap);
					}
				} catch (Exception e1) {
					log.error(e1.getMessage());
					DebugUtil.print(e1, "HibernateUtil Hibernate Session ");
				}finally {
					try {
						hs.close();
					} catch (HibernateException hex) {
						log.error(hex.getMessage());
						DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
					}
				}

			forward = mapping.findForward("toSet");
			}
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("TaskInstancesetresultmap",resultmap);
		request.setAttribute("TaskInstancesetList",rslist);
		return forward;
	}
	/**
	 * Method toUpdateRecord execute,Update data to database and return list
	 * page or modifiy page
	 * 修改页面的保存方法 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toUpdateRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		String taskid=(String)dform.get("taskid");
		long taskid2=Long.parseLong(taskid);
		String actorid=(String)dform.get("userid");
		String[] actors=request.getParameterValues("userid2");
		String[] actors2=new String[actors.length-1];
		int k=0;
		for(int i=0;i<actors.length;i++){
			if(actors[i]!=null && !actors[i].equals("")){
				actors2[k]=actors[i];
				k++;
			}
		}
		
		JbpmConfiguration config=JbpmConfiguration.getInstance();
		JbpmContext jbpmContext=null;
		try{
		   jbpmContext=config.createJbpmContext();
		   TaskInstance ti=jbpmContext.getTaskInstance(taskid2);
		   if(actors2.length>0){
			   ti.setPooledActors(actors2);
		   }
		   if(actorid!=null && !actorid.equals("")){
			   ti.setActorId(actorid);
		   }else{
			   ti.setActorId(null);
		   }
		 }catch(Exception e){
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.record.exist"));
		    jbpmContext.setRollbackOnly();
		 }finally{
		   	if(jbpmContext!=null){
		   		jbpmContext.close();
		   	}
		}
		
		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				forward = mapping.findForward("reList");
			} else {
				if(errors.isEmpty())
				{
				dform.set("id", dform.get("taskid"));
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				}
				forward = mapping.findForward("reSet");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			DebugUtil.print(e, "HibernateUtil Hibernate Session ");
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	
	/**
	 * Method toDisplayRecord execute,prepare data for update page
	 * 查看页面 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		this.setNavigation(request, "navigator.base.TaskExamplesSet.view");
		
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		
		//DecimalFormat mat = new DecimalFormat("00.00");
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);

		ActionForward forward = null;
		String id = (String) dform.get("id");
		String isopenid=request.getParameter("isopenid");
		
		Session hs = null;
		List rslist=new ArrayList();
		HashMap remap=null;
		HashMap resultmap=new HashMap();;
		ResultSet resultset=null;
		int num=0;
		if (id != null) {
			if(request.getAttribute("error") == null || request.getAttribute("error").equals(""))
			{
				try {
					String sql="exec Sp_JbpmTaskInstanceset '%','"+id+"','%',"+isopenid;
					
					hs = HibernateUtil.getSession();
					resultset=hs.connection().createStatement().executeQuery(sql);	
					String userid="";
					if(resultset.next()){								
						resultmap.put("taskid",resultset.getString("taskid"));
						resultmap.put("taskname",resultset.getString("taskname"));
						userid=resultset.getString("actorid");
						resultmap.put("actorid",userid);
						resultmap.put("username",bd.getName("ViewLoginUserInfo","userName","userID",userid));
						resultmap.put("creatdate",resultset.getString("creatdate"));						
						resultmap.put("enddate",resultset.getString("enddate"));
						resultmap.put("isopenid",resultset.getString("isopen"));
						resultmap.put("token",resultset.getString("token"));
						resultmap.put("nodename",resultset.getString("nodename"));						
						resultmap.put("actorname",resultset.getString("actorname"));
					}
					
					String sql2="select b.actorid_ as actorid,v.username as username" +
							"	from JBPM_POOLEDACTOR b" +
							"	inner join	JBPM_TASKACTORPOOL c on b.ID_=c.POOLEDACTOR_" +
							"  	inner join      JBPM_TASKINSTANCE a  on c.TASKINSTANCE_=a.ID_" +
							"	inner join      viewloginuserinfo v  on v.userid=b.actorid_ " +
							"    	where	a.id_='"+id+"'" +
							"        and 	a.ISSUSPENDED_	<>	1" +
							"        and 	a.ISOPEN_	=	1" +
							"		order by b.actorid_";
					resultset=hs.connection().createStatement().executeQuery(sql2);
					while(resultset.next()){
						remap=new HashMap();
						remap.put("userid2",resultset.getString("actorid"));
						remap.put("username2",resultset.getString("username"));
						rslist.add(remap);
						num++;
					}
				} catch (Exception e1) {
					log.error(e1.getMessage());
					DebugUtil.print(e1, "HibernateUtil Hibernate Session ");
				}finally {
					try {
						hs.close();
					} catch (HibernateException hex) {
						log.error(hex.getMessage());
						DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
					}
				}

			forward = mapping.findForward("toView");
			}
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("display","yes");
		
		if(num>0){
			request.setAttribute("TaskInstancesetList",rslist);
		}
		request.setAttribute("TaskInstancesetresultmap",resultmap);
		return forward;
	}

	/**
	 * Method toSearchRecord execute, Search record
	 * 查询页面，显示数据，可根据条件进行相应的查询 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		this.setNavigation(request, "navigator.base.TaskExamplesSet.list");
		ActionForward forward = null;

		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		
		int len=0;//结果集长度
		int navigatelen=SysConfig.HTML_TABLE_LENGTH;//分页步长
		int location=0;
		ResultSet resultset=null;
		
			HTMLTableCache cache = new HTMLTableCache(session, "TaskExamplesSetList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fTaskExamplesSet");
			table.setLength(navigatelen);
			table.setSortColumn("a.deptid");
			table.setIsAscending(true);
			cache.updateTable(table);
			
			if (action.equals(ServeTableForm.NAVIGATE) ||
					action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
				location=table.getFrom();
			} else {
				table.setFrom(0);
				location=0;
			}
			cache.saveForm(tableForm);
			
			if (action.equals(ServeTableForm.NAVIGATE) || action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
				location=table.getFrom();
			} else {
				table.setFrom(0);
				location=0;
			}
			cache.saveForm(tableForm);

			String taskid = tableForm.getProperty("taskid");
			String token = tableForm.getProperty("token");
			String nodename=tableForm.getProperty("nodename");
			String isopen=tableForm.getProperty("isopen");			

			if(taskid!=null && taskid.length()>0){
				taskid=taskid.trim();
			}else{
				taskid="%";
			}			
			if(token!=null && token.length()>0){
				token=token.trim();
			}else{
				token="%";
			}
			if(nodename!=null && nodename.length()>0){
				nodename=nodename.trim();
			}else{
				nodename="%";
			}
			if(isopen!=null && isopen.length()>0){
				isopen=isopen.trim();
			}else{
				isopen="-1";
			}

			String sql="exec Sp_JbpmTaskInstanceset '"+token+"','"+taskid+"','"+nodename+"',"+isopen;
			//System.out.println(sql);
			Session hs = null;
			List rslist=new ArrayList();
			HashMap map=null;
			try {
				hs = HibernateUtil.getSession();
				resultset=hs.connection().createStatement().executeQuery(sql);
				
				while(resultset.next()){
					map=new HashMap();					
					map.put("taskid",resultset.getString("taskid"));
					map.put("taskname",resultset.getString("taskname"));
					map.put("actorid",resultset.getString("actorid"));
					map.put("creatdate",resultset.getString("creatdate"));						
					map.put("enddate",resultset.getString("enddate"));
					map.put("isopenid",resultset.getString("isopen"));
					map.put("token",resultset.getString("token"));
					map.put("nodename",resultset.getString("nodename"));						
					map.put("actorname",resultset.getString("actorname"));
					rslist.add(map);
				}

				len=rslist.size();
	 			List navigatelist=null;
	 			if(len>=location && len>(location+navigatelen)) {
	 				navigatelist=rslist.subList(location,location+navigatelen);
	 			}else if(len>=location && len<=(location+navigatelen)){
	 				navigatelist=new ArrayList();
	 				for(int i=location;i<len;i++){
	 					navigatelist.add(rslist.get(i));
	 				}
	 			}
				
				table.setVolume(len);// 查询得出数据记录数;
				cache.check(table);		
				table.addAll(navigatelist);				
				session.setAttribute("TaskExamplesSetList", table);
			} catch (Exception e1) {
				log.error(e1.getMessage());
				DebugUtil.print(e1);
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
	
	public ActionForward toCloseRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		
		//DecimalFormat mat = new DecimalFormat("00.00");
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);

		ActionForward forward = null;
		String id = (String) dform.get("id");
		long tid=Long.parseLong(id);
		if (id != null) {
			JbpmConfiguration config=JbpmConfiguration.getInstance();
			JbpmContext jbpmContext=null;
			try{
				jbpmContext=config.createJbpmContext();
				TaskInstance ti=jbpmContext.getTaskInstance(tid);
				if(ti!=null){
					ti.cancel();
				}
			}catch(Exception e){
				jbpmContext.setRollbackOnly();
			}finally{
				if(jbpmContext!=null){
					jbpmContext.close();
				}
			}

			
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		forward = mapping.findForward("reList");
		return forward;
	}
}