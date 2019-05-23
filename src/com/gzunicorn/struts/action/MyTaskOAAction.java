//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.gzunicorn.struts.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

import com.gzunicorn.bean.TaskBean;
import com.gzunicorn.common.dao.DBInterface;
import com.gzunicorn.common.dao.ObjectAchieveFactory;
import com.gzunicorn.common.logic.MakeUpXML4;
import com.gzunicorn.common.logic.MakeUpXML4.RowBean;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.Msg;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;

import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

/**
 * MyEclipse Struts Creation date: 08-09-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/cityAction" name="cityForm" scope="request"
 *                validate="true"
 */
public class MyTaskOAAction extends DispatchAction {

	Log log = LogFactory.getLog(MyTaskOAAction.class);

	// --------------------------------------------------------- Instance
	// Variables

	// --------------------------------------------------------- Methods

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

		/************开始用户权限过滤************/
		SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "myTaskOA",null);
		/************结束用户权限过滤************/
		
		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			return null;
			
		}
		DebugUtil.printDoBaseAction("MyTaskOAAction",name,"start");
		ActionForward forward= dispatchMethod(mapping, form, request, response, name);
		DebugUtil.printDoBaseAction("MyTaskOAAction",name,"end");
		return forward;

	}
	
	
	public ActionForward toMain(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward=null;
		forward=mapping.findForward("toMain");
		return forward;
	}
	public ActionForward toMyTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		HttpSession httpSession = request.getSession();
		ViewLoginUserInfo userinfo = (ViewLoginUserInfo)httpSession.getAttribute(SysConfig.LOGIN_USER_INFO);
		
//		HttpSession s=request.getSession();
//		//System.out.println(s.getMaxInactiveInterval());
		Session hs = null;
		Connection con=null;

		String pubUrl="/"+SysConfig.WEB_APPNAME+"/myTaskOaSearchAction.do?method=toHandleTask";
		String processUrl="/"+SysConfig.WEB_APPNAME+"/viewProcessAction.do?method=toViewProcess";
		try {
			//con=this.getDataSource(request,"ds").getConnection();	
			hs = HibernateUtil.getSession();
			con=hs.connection();
			if(con!=null){
				DBInterface dp=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
				dp.setCon(con);
				String sql="Sp_FetchToDoList '"+userinfo.getUserID()+"' ";
				
				List list = dp.queryToList(sql);
				List rslist=new ArrayList();
				StringBuffer sb=new StringBuffer();
				if(list!=null && list.size()>0){
					TaskBean tb=new TaskBean();
					String curflowid="";
					boolean isfirst=true;
					HashMap task=null;
					for(int i=0;i<list.size();i++){
						task=(HashMap)list.get(i);
						sb.append(task.get("taskid"));
						curflowid=(String)task.get("flowid");
						if(isfirst){
							isfirst=false;
							tb.setFlow(curflowid,(String)task.get("flowname"));
							tb.addTask(task,pubUrl,processUrl);	
						}else{
							if(curflowid!=null && curflowid.equalsIgnoreCase(tb.getFlowid())){
								tb.addTask(task,pubUrl,processUrl);	
							}else{
								rslist.add(tb);
								
								tb=new TaskBean();
								tb.setFlow(curflowid,(String)task.get("flowname"));
								tb.addTask(task,pubUrl,processUrl);	
							}
						}
						
						if(i==(list.size()-1)){
							rslist.add(tb);
						}else{
							sb.append(",");
						}
					}
					request.setAttribute("showTask","Y");
					request.setAttribute("TaskList",rslist);
					request.setAttribute("Taskes",sb.toString());
				}else{
					//显示　无待办任务
					request.setAttribute("showTask","N");
					request.setAttribute("NoTaskTip","无待办任务");
				}
				
			}else{
				log.error("can't get connection from datasource");
			}
	
		} catch (HibernateException e1) {
			DebugUtil.print(e1);
		} catch (Exception e) {
			DebugUtil.print(e);
		} finally {
			try {
				if(con!=null){
					con.close();
				}
				if(hs!=null){
					hs.close();
				}
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			} catch (SQLException e) {
				DebugUtil.print(e);
			}
		}
		return mapping.findForward("ToDoList");
	}
	/**
	 * 返回待办列表的xml
	 * 
	 * 待办工作编号,待办工作标题,待办工作的连接url,日期,是否为new待办.可以传参数知道是那个人的待办工作
	 */
	public ActionForward toMyTaskUseXml(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		HttpSession httpSession = request.getSession();
		ViewLoginUserInfo userinfo = (ViewLoginUserInfo)httpSession.getAttribute(SysConfig.LOGIN_USER_INFO);
		
//		HttpSession s=request.getSession();
//		//System.out.println(s.getMaxInactiveInterval());
		Session hs = null;
		Connection con=null;

		String pubUrl="/"+SysConfig.WEB_APPNAME+"/myTaskOaSearchAction.do?method=toHandleTask";
		String processUrl="/"+SysConfig.WEB_APPNAME+"/viewProcessAction.do?method=toViewProcess";
		String xml="";
		MakeUpXML4 mux=new MakeUpXML4("root","1.0","GBK");
		try {
			//con=this.getDataSource(request,"ds").getConnection();	
			hs = HibernateUtil.getSession();
			con=hs.connection();
			
			if(con!=null){
				DBInterface dp=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
				dp.setCon(con);
				String sql="Sp_FetchToDoList '"+userinfo.getUserID()+"' ";
				
				List list = dp.queryToList(sql);

				
				if(list!=null && list.size()>0){
					HashMap map=null;
					for(int i=0;i<list.size();i++){
						map=(HashMap)list.get(i);
						if(map.get("flowurl2")!=null && !map.get("flowurl2").equals("")){
						RowBean rb=mux.newRowBean("tasklist");
						
						rb.addCol("taskid",map.get("taskid")+"");
						rb.addCol("flowname",(String)map.get("flowname"));
						rb.addCol("taskname2",(String)map.get("taskname2"));
						rb.addCol("url",map.get("flowurl2")+"&tokenid="+map.get("token")+"&taskid="+map.get("taskid")+"&taskname="+map.get("taskname")+"&taskname2="+map.get("taskname2")+"&flowname="+map.get("flowname"));
						rb.addCol("createdate",(String)map.get("createdate"));
						
									
						mux.addRow(rb);
						}
					}
				}else{
					//显示　无待办任务
					RowBean rb=mux.newRowBean("tasklist");
					rb.addCol("taskid","");
					rb.addCol("flowname","无待办任务");
					rb.addCol("taskname2","");
					rb.addCol("url","");
					rb.addCol("createdate","");
					
				
					mux.addRow(rb);
				}
				xml=mux.getXml("0","");
			}else{
				log.error("can't get connection from datasource");
				xml=mux.getXml("1","can't get connection from datasource");
			}
	
		} catch (HibernateException e1) {
			DebugUtil.print(e1);
		} catch (Exception e) {
			DebugUtil.print(e);
		} finally {
			try {
				if(con!=null){
					con.close();
				}
				if(hs!=null){
					hs.close();
				}
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			} catch (SQLException e) {
				DebugUtil.print(e);
			}
		}
		
		try {
			response.setHeader("Content-Type","text/xml; charset=GBK");//content-type 设置会影响到xml结果的解释
			response.getOutputStream().write(xml.getBytes());
		} catch (IOException e) {
			DebugUtil.print(e);
		}
		return null;
	}
	/**
	 * Method toSearchRecord execute, Search record 调用存储过程查询待办工作的任务
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDoList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		HttpSession httpSession = request.getSession();
		ViewLoginUserInfo userinfo = (ViewLoginUserInfo)httpSession.getAttribute(SysConfig.LOGIN_USER_INFO);
		//判断
		String showtype=request.getParameter("showtype")!=null && request.getParameter("showtype").length()!=0?request.getParameter("showtype"):"";
		String jumpto=request.getParameter("jumpto")!=null && request.getParameter("jumpto").length()!=0?request.getParameter("jumpto"):"";
		request.setAttribute("jumpto",jumpto);
		MakeUpXML4 mux=new MakeUpXML4("root","1.0","GBK");
		String msg="";
		Connection con=null;
		Session hs = null;
		List moduleList=new ArrayList();
		try {
			//con=this.getDataSource(request,"ds").getConnection();	
			hs = HibernateUtil.getSession();
			con=hs.connection();
			if(con!=null){
				DBInterface dp=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
				dp.setCon(con);
				String sql="Sp_FetchToDoList '"+userinfo.getUserID()+"','-1' ";
				DebugUtil.println(sql);
				List list = dp.queryToList(sql);

				if(list!=null && list.size()>0){
					HashMap task=null;
					String cfid=null,nfid=null,cfname="";
					List rslist=new ArrayList(),itemlist=null;
					HashMap item=null;
					int qty=0;
					for(int i=0;i<list.size();i++){
						task=(HashMap)list.get(i);
						if(i==0){
							cfid=(String)task.get("flowid");
							cfname=(String)task.get("flowname");
							itemlist=new ArrayList();
							item=new HashMap();
						}
						
						nfid=(String)task.get("flowid");
						if(cfid!=null && cfid.equalsIgnoreCase(nfid)){
							itemlist.add(task);
							qty++;
						}else{
							item.put("qty",new Integer(qty));
							item.put("flowid",cfid);
							item.put("flowname",cfname);
							item.put("list",itemlist);
							qty=0;
							
							rslist.add(item);
							
							itemlist=new ArrayList();
							item=new HashMap();
							cfid=nfid;
							cfname=(String)task.get("flowname");
							itemlist.add(task);
							qty++;
						}
						if(i==list.size()-1){
							item.put("qty",new Integer(qty));
							item.put("flowid",cfid);
							item.put("flowname",cfname);
							item.put("list",itemlist);
							
							rslist.add(item);
						}
					}
					//模块查询条件下拉
					String lastlistid = "";//当前流程号
					String nextlistid = "";//下一流程号
					if(rslist!=null && rslist.size()>0){
						int rslen=rslist.size();
						HashMap md=null;
						HashMap opmap=null;
						boolean istrue=true;
						for(int w=0;w<rslen;w++){
							md=(HashMap)rslist.get(w);
							if(istrue){
								opmap=new HashMap();
								opmap.put("listid", "");
								opmap.put("listname", "全部");
								moduleList.add(opmap);
								lastlistid = "";
								istrue=false;
							}
							nextlistid = String.valueOf(md.get("flowid"));
							if(!nextlistid.equals(lastlistid))
							{
								opmap=new HashMap();
								opmap.put("listid", nextlistid);
								opmap.put("listname", (String)md.get("flowname"));
								
								lastlistid = nextlistid;
								moduleList.add(opmap);
							}
							
						}
					}
					request.setAttribute("flowList", moduleList);
					
					if(showtype.equalsIgnoreCase("XML")){
						if(rslist!=null && rslist.size()>0){
							int len=rslist.size();
							HashMap m=null;
							RowBean rb=null;
							StringBuffer sbs=new StringBuffer();
							for(int k=0;k<len;k++){
								m=(HashMap)rslist.get(k);
								rb=mux.newRowBean("tasklist");
								
								rb.addCol("qty",String.valueOf(m.get("qty")));
								rb.addCol("flowid",String.valueOf(m.get("flowid")));
								rb.addCol("flowname",(String)m.get("flowname"));
								
								List itemList=(ArrayList)m.get("list");
								if(itemList!=null && itemList.size()>0){
									int lens=itemList.size();
									HashMap m1=null;
									String flowurl2=null;
									String showinfo=null;
									String url=null;
									for(int j=0;j<lens;j++){
										m1=(HashMap)itemList.get(j);
										flowurl2=m1.get("flowurl2").toString().replaceAll("&", "&amp;");
										showinfo=m1.get("showinfo").toString().replaceAll("&nbsp", " ").replaceAll(";", "");
										
										if(m1.get("actors")!=null && m1.get("actors").toString().length()>0){
											url=flowurl2+"&amp;tokenid="+m1.get("token")+"&amp;taskid="+m1.get("taskid")+"&amp;taskname="+m1.get("taskname")+"&amp;taskname2="+m1.get("taskname2")+"&amp;flowname="+m1.get("flowname")+"&amp;tasktype=0";
										}else{
											url=flowurl2+"&amp;tokenid="+m1.get("token")+"&amp;taskid="+m1.get("taskid")+"&amp;taskname="+m1.get("taskname")+"&amp;taskname2="+m1.get("taskname2")+"&amp;flowname="+m1.get("flowname")+"&amp;tasktype=1";
										}
										sbs.append(showinfo).append("!#")
										   .append(url).append("!#")
										   .append(m1.get("tipinfo").toString()).append("!#")
										   .append(m1.get("viewurl")).append("!#")
										   .append(m1.get("tokenid")).append("$%$");
									}
								}
								if(sbs.toString().length()>0){
									rb.addCol("alldata",sbs.toString().substring(0,(sbs.toString().length()-3)));
								}
								mux.addRow(rb);
								sbs=new StringBuffer();
							}
						}
					}else{
						request.setAttribute("TaskList",rslist);
					}
				}else{
					msg="^_^,无待办任务!";
					request.setAttribute("Msg",msg);
				}
			}else{
				log.error("can't get connection from datasource");
			}
	
		}catch (Exception e) {
			DebugUtil.print(e);
		} finally {
			try {
				if(con!=null){
					con.close();
				}
				if(hs!=null){
					hs.close();
				}
			}catch (SQLException e) {
				DebugUtil.print(e);
			}
		}
		
		if(showtype.equalsIgnoreCase("XML")){
			try {
				response.setHeader("Content-Type","text/xml; charset=GBK");//content-type 设置会影响到xml结果的解释
				response.getOutputStream().write(mux.getXml(Msg.msg_suc+"",msg).getBytes());
			} catch (IOException e) {
				DebugUtil.print(e);
			}
			return null;
		}else{
			return mapping.findForward("ToDoList2");
		}
	}
	/**
	 * Method toSearchRecord execute, Search record 调用存储过程查询待办工作的任务
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDoList_old(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		
		HttpSession httpSession = request.getSession();
		ViewLoginUserInfo userinfo = (ViewLoginUserInfo)httpSession.getAttribute(SysConfig.LOGIN_USER_INFO);
		//判断
		String showtype=request.getParameter("showtype")!=null && request.getParameter("showtype").length()!=0?request.getParameter("showtype"):"";
		String jumpto=request.getParameter("jumpto")!=null && request.getParameter("jumpto").length()!=0?request.getParameter("jumpto"):"";
		request.setAttribute("jumpto",jumpto);
		MakeUpXML4 mux=new MakeUpXML4("root","1.0","GBK");
		if(userinfo==null){
			if(showtype.equalsIgnoreCase("XML")){
				try {
					response.setHeader("Content-Type","text/xml; charset=GBK");//content-type 设置会影响到xml结果的解释
					response.getOutputStream().write(mux.getXml(Msg.msg_fal+"","当前用户尚登录！").getBytes());
				} catch (IOException e) {
					DebugUtil.print(e);
				}
				return null;
			}else{
				request.setAttribute("Msg","当前用户尚登录！");
				return mapping.findForward("ToDoList2");
			}
		}
		String msg="";
		Connection con=null;
		Session hs = null;
		try {
			//con=this.getDataSource(request,"ds").getConnection();	
			hs = HibernateUtil.getSession();
			con=hs.connection();
			
			if(con!=null){
				DBInterface dp=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
				dp.setCon(con);
				String sql="Sp_FetchToDoList '"+userinfo.getUserID()+"','-1' ";
				
				List list = dp.queryToList(sql);

				if(list!=null && list.size()>0){
					HashMap task=null;
					if(showtype.equalsIgnoreCase("XML")){
						Iterator it=null;
						String mid="";
						for(int i=0;i<list.size();i++){
							task=(HashMap)list.get(i);
							RowBean rb=mux.newRowBean("tasklist");
							
							it=task.keySet().iterator();
							while(it.hasNext()){
								mid=it.next().toString();
								rb.addCol(mid,task.get(mid)+"");
							}
							if(task.get("actors")!=null && task.get("actors").toString().length()>0){
							rb.addCol("url",task.get("flowurl2")+"&tokenid="+task.get("token")+"&taskid="+task.get("taskid")+"&taskname="+task.get("taskname")+"&taskname2="+task.get("taskname2")+"&flowname="+task.get("flowname")+"&tasktype=0");
							}else{
								rb.addCol("url",task.get("flowurl2")+"&tokenid="+task.get("token")+"&taskid="+task.get("taskid")+"&taskname="+task.get("taskname")+"&taskname2="+task.get("taskname2")+"&flowname="+task.get("flowname")+"&tasktype=1");
							}
							mux.addRow(rb);
						}
					}else{
						String cfid=null,nfid=null,cfname="";
						List rslist=new ArrayList(),itemlist=null;
						HashMap item=null;
						int qty=0;
						for(int i=0;i<list.size();i++){
							task=(HashMap)list.get(i);
							if(i==0){
								cfid=(String)task.get("flowid");
								cfname=(String)task.get("flowname");
								itemlist=new ArrayList();
								item=new HashMap();
							}
							
							nfid=(String)task.get("flowid");
							if(cfid!=null && cfid.equalsIgnoreCase(nfid)){
								itemlist.add(task);
								qty++;
							}else{
								item.put("qty",new Integer(qty));
								item.put("flowid",cfid);
								item.put("flowname",cfname);
								item.put("list",itemlist);
								qty=0;
								
								rslist.add(item);
								
								itemlist=new ArrayList();
								item=new HashMap();
								cfid=nfid;
								cfname=(String)task.get("flowname");
								itemlist.add(task);
								qty++;
							}
							if(i==list.size()-1){
								item.put("qty",new Integer(qty));
								item.put("flowid",cfid);
								item.put("flowname",cfname);
								item.put("list",itemlist);
								
								rslist.add(item);
							}
						}
						request.setAttribute("TaskList",rslist);
					}
				}else{
					msg="^_^,无待办任务!";
					request.setAttribute("Msg",msg);
				}
			}else{
				log.error("can't get connection from datasource");
			}
	
		}catch (Exception e) {
			DebugUtil.print(e);
		} finally {
			try {
				if(con!=null){
					con.close();
				}
				if(hs!=null){
					hs.close();
				}
			}catch (SQLException e) {
				DebugUtil.print(e);
			}
		}
		
		if(showtype.equalsIgnoreCase("XML")){
			try {
				response.setHeader("Content-Type","text/xml; charset=GBK");//content-type 设置会影响到xml结果的解释
				response.getOutputStream().write(mux.getXml(Msg.msg_suc+"",msg).getBytes());
			} catch (IOException e) {
				DebugUtil.print(e);
			}
			return null;
		}else{
			return mapping.findForward("ToDoList2");
		}
	}
	public ActionForward toHandTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward=null;
		long taskid=-1;
		int tasktype=-1;
		boolean tag=true;
		try{
			taskid=Long.parseLong(request.getParameter("taskid"));
			tasktype=Integer.parseInt(request.getParameter("tasktype"));
		}catch(Exception e){
			tag=false;
			DebugUtil.print(e);
		}
		String flowid=request.getParameter("flowid");
		//操作标志,操作成功：0,操作失败：1,操作成功：2
		
		String taskop="";
		HashMap map=new HashMap();
		String msg="";
		if(tag && taskid!=-1 && tasktype!=-1){
			
			ViewLoginUserInfo UserInfo=(ViewLoginUserInfo)request.getSession().getAttribute(SysConfig.LOGIN_USER_INFO);
			JbpmConfiguration config=JbpmConfiguration.getInstance();
			JbpmContext jbpmContext=config.createJbpmContext();
			try{
				switch(tasktype){
					case 1://共享－》个人
						TaskInstance ti=jbpmContext.getTaskInstance(taskid);
						if(ti!=null && ti.isOpen() && (ti.getActorId()==null || ti.getActorId().equalsIgnoreCase(UserInfo.getUserID()))){
							ti.setActorId(UserInfo.getUserID());
							jbpmContext.save(ti);
						}else{
							msg="任务已被别人先受理！";
						}
						break;
					case 2://个人－》共享
//						TaskInstance ti2=jbpmContext.getTaskInstance(taskid);
//						if(ti2!=null && ti2.isOpen() && UserInfo.getUserID().equalsIgnoreCase(ti2.getActorId())){
//							ti2.setActorId(null);
//							jbpmContext.save(ti2);
//						}else{						
//							msg="任务已处理完成，不能变成共享任务！";
//						}
						break;
					//暂不考虑这两点，原因所有人删除后，任务无人处理，需增加功能结点设置任务实例处理人
					case 3://撤消共享，从任务候选人中删除
						break;
					case 4://撤消个人及共享，从任务候选人中删除
						break;
				}
			}catch(Exception e){
				tag=false;
				jbpmContext.setRollbackOnly();
				DebugUtil.print(e);
			}finally{
				jbpmContext.close();
			}
		}
		//request.setAttribute("isreload", "Y");
		String ajax=request.getParameter("ajax");
		if(ajax!=null && ajax.equalsIgnoreCase("1")){
			String xml=toMakeUpXml(null,Msg.msg_suc+"",msg);
			try {
				response.setHeader("Content-Type","text/xml; charset=GBK");//content-type 设置会影响到xml结果的解释
				response.getOutputStream().write(xml.getBytes());
			} catch (IOException e) {
				DebugUtil.print(e);
			}
			
		}else{
			try {
				response.setCharacterEncoding("GBK"); 
				response.setContentType("text/xml;charset=GBK"); 
				response.getWriter().write(msg);
			} catch (IOException e) {
				DebugUtil.print(e);
			}
		}
		return forward;
	}
	/**
	 * 将任务属性由共享任务－>我的任务
	 * 权限由jbpm控制
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toHandleTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward=null;
		long taskid=-1;
		int tasktype=-1;
		boolean tag=true;
		try{
			taskid=Long.parseLong(request.getParameter("taskid"));
			tasktype=Integer.parseInt(request.getParameter("tasktype"));
		}catch(Exception e){
			tag=false;
			DebugUtil.print(e);
		}
		String flowid=request.getParameter("flowid");
		//操作标志,操作成功：0,操作失败：1,操作成功：2
		
		String taskop="";
		HashMap map=new HashMap();
		if(taskid!=-1 && tasktype!=-1){
			
			ViewLoginUserInfo UserInfo=(ViewLoginUserInfo)request.getSession().getAttribute(SysConfig.LOGIN_USER_INFO);
			JbpmConfiguration config=JbpmConfiguration.getInstance();
			JbpmContext jbpmContext=config.createJbpmContext();
			try{
				switch(tasktype){
					case 1://共享－》个人
						TaskInstance ti=jbpmContext.getTaskInstance(taskid);
						if(ti!=null && ti.isOpen() && ti.getActorId()==null){
							ti.setActorId(UserInfo.getUserID());
							jbpmContext.save(ti);
							
							map.put("taskop",tasktype+"");
							map.put("taskrs","topri");
							map.put("isshow","N");
							map.put("taskinfo","");
						}else{
							map.put("taskop",tasktype+"");
							map.put("taskrs","todel");
							map.put("isshow","Y");
							map.put("taskinfo","任务已被别人先受理！");//任务已处理或已关闭
						}
						break;
					case 2://个人－》共享
						TaskInstance ti2=jbpmContext.getTaskInstance(taskid);
						if(ti2!=null && ti2.isOpen() && UserInfo.getUserID().equalsIgnoreCase(ti2.getActorId())){
							ti2.setActorId(null);
							jbpmContext.save(ti2);
							
							map.put("taskop",tasktype+"");
							map.put("taskrs","topub");
							map.put("isshow","N");
							map.put("taskinfo","");
						}else{
							map.put("taskop",tasktype+"");
							map.put("taskrs","todel");
							map.put("isshow","Y");
							map.put("taskinfo","任务已处理完成，不能变成共享任务！");//任务已处理完成或已关闭
						}
						break;
					//暂不考虑这两点，原因所有人删除后，任务无人处理，需增加功能结点设置任务实例处理人
					case 3://撤消共享，从任务候选人中删除
						break;
					case 4://撤消个人及共享，从任务候选人中删除
						break;
				}
			}catch(Exception e){
				tag=false;
				jbpmContext.setRollbackOnly();
				DebugUtil.print(e);
			}finally{
				jbpmContext.close();
			}
		}
		//request.setAttribute("isreload", "Y");
		String ajax=request.getParameter("ajax");
		if(ajax!=null && ajax.equalsIgnoreCase("1")){
			//make up xml and return 
			//handleTask(tid,flow,top,trs,tinfo,isshow,tdetail)
			List list=new ArrayList();
			map.put("taskid",new Long(taskid));
			map.put("flowid",flowid);
			map.put("taskdetail","taskdetail");
			list.add(map);
			String xml=toMakeUpXml(list,Msg.msg_suc+"","");
			try {
				response.setHeader("Content-Type","text/xml; charset=GBK");//content-type 设置会影响到xml结果的解释
				response.getOutputStream().write(xml.getBytes());
			} catch (IOException e) {
				DebugUtil.print(e);
			}
			forward=null;
		}else{
			forward=mapping.findForward("ReToDoList");
		}
		return forward;
	}
//	/**
//	 * 更新任务列表信息
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	public ActionForward toUpdateTaskList(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) {
//		ActionForward forward=null;
//		return forward;
//	}
	/**
	 * 更新任务列表信息
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward getTaskDetailList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward=null;
		//要取明细信息的任务列表
		String[] tasklist=request.getParameterValues("taskid");
//		String[] flowlist=request.getParameterValues("flowlist");
		List list=null;
		if(tasklist!=null && tasklist.length>0){
			String insql="";
			for(int i=0;i<tasklist.length;i++){
				insql+=tasklist[i];
				if(i<tasklist.length-1){
					insql+=",";
				}
			}
			Connection con=null;
			Session hs = null;
			try{
				//con=this.getDataSource(request,"ds").getConnection();	
				hs = HibernateUtil.getSession();
				con=hs.connection();
				
				if(con!=null){
					DBInterface dp=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
					dp.setCon(con);
					String sql="Sp_GetTodoListDetail '"+insql+"'";
					list = dp.queryToList(sql);
					if(list!=null){
						HashMap map=null;
						for(int i=0;i<list.size();i++){
							map=(HashMap)list.get(i);
							map.put("taskop",request.getParameter("tasktype"));
							map.put("taskrs","todetail");
							map.put("isshow","N");
							map.put("taskinfo","taskinfo");
							//map.put("taskdetail","taskdetail");
						}
					}
				}
			}catch(Exception e){
				DebugUtil.print(e);
			}finally{
				if(con!=null){
					try {
						con.close();
					} catch (SQLException e) {
						DebugUtil.print(e);
					}
				}
				if(hs!=null){
					hs.close();
				}
			}
			
		}
		String xml="";
		if(list!=null){
			xml=toMakeUpXml(list,Msg.msg_suc+"","");
		}else{
			xml=toMakeUpXml(list,Msg.msg_fal+"","");
		}
		try {
			response.setHeader("Content-Type","text/xml; charset=GBK");//content-type 设置会影响到xml结果的解释
			response.getOutputStream().write(xml.getBytes());
		} catch (IOException e) {
			DebugUtil.print(e);
		}
		forward=null;
		return forward;
	}
	/**
	 * 整合成xml
	 * @param xml
	 * @param msgid
	 * @param msginfo
	 * @return
	 */
	private String toMakeUpXml(List xml,String msgid,String msginfo){
		MakeUpXML4 mux=new MakeUpXML4("root","1.0","GBK");
		if(xml!=null && xml.size()>0){
			HashMap map;
			for(int i=0;i<xml.size();i++){
				map=(HashMap)xml.get(i);
				RowBean rb=mux.newRowBean("tasklist");
				
				rb.addCol("taskid",map.get("taskid")+"");
				rb.addCol("flowid",(String)map.get("flowid"));
				rb.addCol("taskop",(String)map.get("taskop"));
				rb.addCol("taskrs",(String)map.get("taskrs"));
				rb.addCol("taskinfo",(String)map.get("taskinfo"));
				rb.addCol("isshow",(String)map.get("isshow"));
				rb.addCol("taskdetail",(String)map.get("taskdetail"));
								
				mux.addRow(rb);
			}
		}
		
		////System.out.println(mux.getXml(msgid,msginfo));
		return mux.getXml(msgid,msginfo);
	}
	
	
	
	
}