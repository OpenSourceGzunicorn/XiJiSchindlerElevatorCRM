//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.gzunicorn.struts.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.bean.FlowSetBean;
import com.gzunicorn.common.dao.DBInterface;
//import com.gzunicorn.common.dao.ObjectAchieveFactory;
import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.ArrayConfig;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.CryptUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
//import com.gzunicorn.hibernate.Class1;
import com.gzunicorn.hibernate.jbpmmanager.Jbpmdecisionjump;
import com.gzunicorn.hibernate.jbpmmanager.Jbpmnodeext;
import com.gzunicorn.hibernate.jbpmmanager.Jbpmnodeextactors;
import com.gzunicorn.hibernate.jbpmmanager.Jbpmnodeexttarget;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.hibernate.proshr.ProcessSetAuditDetail;
import com.gzunicorn.hibernate.proshr.ProcessSetAuditMaster;
import com.gzunicorn.hibernate.proshr.ProcessSetBranch;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * 登陆用户Action
 * 
 * @version 1.0
 * @author Administrator
 * 
 */
public class FlowAction extends DispatchAction {

	/**
	 * 日期操作对象
	 */
	Log log = LogFactory.getLog("FlowAction.class");

	/**
	 * 基础数据操作对象
	 */
	BaseDataImpl bd = new BaseDataImpl();

	
	/**
	 * Method execute 由 Struts-config.XML 跳转过来; 判断 执行小页面查询 还是 大页面查询； 用户权限控制；
	 * 后台调试： 打印执行的方法
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

		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
		}
		/** **********开始用户权限过滤*********** */

		SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "flow",null);
		/** **********结束用户权限过滤*********** */

		DebugUtil.printDoBaseAction("FlowAction", name, "start");
		ActionForward forward = dispatchMethod(mapping, form, request,
				response, name);
		DebugUtil.printDoBaseAction("FlowAction", name, "end");
		return forward;
	}

	/**
	 * Get the navigation description from the properties file by navigation
	 * key; 导航条
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
	 * Method toPrepareAddRecord execute,prepare data for add page
	 * 进入新增页面，准备保存前要显示的数据或页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toPrepareSetRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		this.setNavigation(request, "navigator.base.flowset.set");

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}

		String pid=request.getParameter("id");
		Connection con=null;
		Statement st=null;
		Session hs = null;
		try {
			hs = HibernateUtil.getSession();
			//con=this.getDataSource(request,SysConfig.DataSource).getConnection();
			
			st=hs.connection().createStatement();
			List tranlist=null,atcorlist=null,decilist=null,targetlist=null,classlist=null;
			List nodeext=this.getNode(st,pid);
			if(nodeext!=null && nodeext.size()>0){
				String nodes=this.getNodes(nodeext);
				if(nodes!=null && nodes.length()>0){
					tranlist=this.getTran(st,nodes);
				}
				if(nodes!=null && nodes.length()>0){
					atcorlist=this.getActors(st,nodes);
				}
				if(nodes!=null && nodes.length()>0){
					decilist=this.getDeci(st,nodes);
				}
				if(nodes!=null && nodes.length()>0){
					targetlist=this.getTarget(st,nodes);
				}
				if(nodes!=null && nodes.length()>0){
					classlist=this.getClass(st);
				}
				request.setAttribute("classlist",classlist);
				
				//新版本流程才起作用
				for(int i = 0; i<nodeext.size(); i++){
					HashMap map = (HashMap)nodeext.get(i);
					String nodeid = (String) map.get("nodeid");
					String nodeidNew = (String) map.get("nodeidNew");
					
					if(nodeidNew == null || nodeidNew.equals("")){
						break;
					}					
					if (!nodeid.equals(nodeidNew)) {
						map.put("nodeid", nodeidNew);
						
						for (Object object : tranlist) {
							HashMap hm = (HashMap) object;
							if (nodeid.equals(hm.get("nodeid"))) {
								hm.put("nodeid", nodeidNew);
							}
						}
						for (Object object : atcorlist) {
							HashMap hm = (HashMap) object;
							if (nodeid.equals(hm.get("nodeid"))) {
								hm.put("nodeid", nodeidNew);
							}
						}
						for (Object object : decilist) {
							HashMap hm = (HashMap) object;
							if (nodeid.equals(hm.get("nodeid"))) {
								hm.put("nodeid", nodeidNew);
							}
						}
						for (Object object : targetlist) {
							HashMap hm = (HashMap) object;
							if (nodeid.equals(hm.get("nodeid"))) {
								hm.put("nodeid", nodeidNew);
							}
						}
						
					}
				}
							
				FlowSetBean fsb=new FlowSetBean(nodeext,atcorlist,targetlist,decilist,tranlist);
				request.setAttribute("flow",fsb);
			}
					
		} catch (Exception e1) {
			DebugUtil.print(e1);
		} finally {
			try {
				if(st!=null){
					st.close();
				}
			} catch (Exception hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex);
			}
			try {
				if(con!=null){
					con.close();
				}
				if(hs!=null){
					hs.close();
				}
			} catch (Exception hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex);
			}
		}

		return mapping.findForward("ToSet");
	}
	/*
	 * 配置审核人权限进去方法
	 * 
	 * */
	public ActionForward toPrepareSetShrRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		request.setAttribute("navigator.location", "配置审核权限>>设置");

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}
		String pid =request.getParameter("id");
		request.setAttribute("pid", pid);
		Connection con=null;
		Statement st=null;
		Session hs=null;
		List list1=new ArrayList();
		List list2=new ArrayList();
		List list3=new ArrayList();
		List list4=new ArrayList();
		String comids="";
		String nodeid="";
		String processdefinename ="";
		String version="";
		String nodename="";
		String processdefinealiasname="";
		try {
			
			hs=HibernateUtil.getSession();
			list1=hs.createQuery("from Company a where enabledflag='Y' and comType in('0','1') order by comid").list();
			//con=this.getDataSource(request,SysConfig.DataSource).getConnection();
			st=hs.connection().createStatement();
			
			List tranlist=null,atcorlist=null,decilist=null,targetlist=null,classlist=null;
			List nodeext=this.getNode(st,pid);
			for(int i=0;i<nodeext.size();i++){
				HashMap hm=(HashMap)nodeext.get(i);
				nodeid=String.valueOf(hm.get("nodeid"));
				nodename=String.valueOf(hm.get("nodename"));
				processdefinename=String.valueOf(hm.get("processdefinename"));
				version=String.valueOf(hm.get("version"));
				processdefinealiasname=String.valueOf(hm.get("ext1"));
				String sql="select distinct b.auditoperid,l.username,b.comid,c.comname"
						+ " from  ProcessSetAuditMaster a,ProcessSetAuditDetail b,Loginuser l,Company c"
						+ " where a.billno=b.billno and "
						+ " a.processDefineId='"+processdefinename+"' "
						+ " and a.nodename='"+nodename+"' "
						+ " and b.auditoperid=l.userid and b.comid=c.comid";
                list3=hs.createQuery(sql).list();
                
				for(int j=0;j<list3.size();j++){
					HashMap hm2=new HashMap();
					Object[] obj=(Object[])list3.get(j);
					hm2.put("userid_"+nodeid, String.valueOf(obj[0]));
					hm2.put("username_"+nodeid,String.valueOf(obj[1]));
					hm2.put("grcid_"+nodeid, String.valueOf(obj[2]));
					hm2.put("grcname_"+nodeid, String.valueOf(obj[3]));
					list4.add(hm2);
				}
				
				hm.put("list3", list4);				
			}
			String sql="select comId from ProcessSetBranch where processDefineId='"+processdefinename+"'";
			list2=hs.createQuery(sql).list();


			request.setAttribute("nodeext",nodeext);
			request.setAttribute("list2",list2);
			request.setAttribute("list4",list4);
			request.setAttribute("Companylist", list1);
			request.setAttribute("processdefinename",processdefinename);
			request.setAttribute("version",version);
			request.setAttribute("processdefinealiasname",processdefinealiasname);
		} catch (Exception e1) {
			DebugUtil.print(e1);
		} finally {
			try {
				if(st!=null){
					st.close();
				}
			} catch (Exception hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex);
			}
			try {
				if(con!=null){
					con.close();
				}

				if(hs!=null){
					hs.close();
				}
			} catch (Exception hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex);
			}
		}

		return mapping.findForward("ToShr");
	}
	/**
	 * Method toAddRecord execute,Add data to database and return list page or
	 * add page 新增页面的保存方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward toSetRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		
		Long processdefineid=(Long)dform.get("processdefineid");
		String processdefinename=(String)dform.get("processdefinename");
		String processdefinealiasname=(String)dform.get("processdefinealiasname");
		Integer version=(Integer)dform.get("version");
		String jump=(String)dform.get("jump");
		
		Long[] nodeid=(Long[])dform.get("nodeid");
		String[] nodeclass=(String[])dform.get("nodeclass");
		String[] nodename=(String[])dform.get("nodename");
		String[] des=(String[])dform.get("des");
		Integer[] orgfilter=(Integer[])dform.get("orgfilter");
		Integer[] indfilter=(Integer[])dform.get("indfilter");
		Integer[] areafilter=(Integer[])dform.get("areafilter");
		Integer[] jumpflag=(Integer[])dform.get("jumpflag");
		String[] rem1=(String[])dform.get("rem1");
		String[] ext1=(String[])dform.get("ext1");
		String[] ext2=(String[])dform.get("ext2");
		String[] ext3=(String[])dform.get("ext3");
		String[] ext4=(String[])dform.get("ext4");
		String[] ext5=(String[])dform.get("ext5");
				
		Session hs = null;
		Transaction tx = null;
		Statement st=null;
		try {
			hs = HibernateUtil.getSession();

			tx = hs.beginTransaction();
			st=hs.connection().createStatement();
			String sql="delete jbpm_nodeexttarget  " +
					"where nodeid in (" +
						"select nodeid " +
						"from jbpm_nodeext " +
						"where processdefineid="+processdefineid+")";
			st.executeUpdate(sql);
			
			sql="delete jbpm_nodeextactors  " +
			"where nodeid in (" +
				"select nodeid " +
				"from jbpm_nodeext " +
				"where processdefineid="+processdefineid+")";
			st.executeUpdate(sql);
			
			sql="delete jbpm_decisionjump  " +
			"where nodeid in (" +
				"select nodeid " +
				"from jbpm_nodeext " +
				"where processdefineid="+processdefineid+")";
			st.executeUpdate(sql);
			
			sql="delete jbpm_nodeext " +
				"where processdefineid="+processdefineid+"";
			st.executeUpdate(sql);
			
			if(nodeid!=null && nodeid.length>0){
				Jbpmnodeext nodeext=null;
				String nid="";
				for(int k=0;k<nodeid.length;k++){
					nodeext=new Jbpmnodeext();
					nodeext.setAreafilter(this.getSel(areafilter,nodeid[k].longValue()));
					nodeext.setDes(des[k]);
					nodeext.setExt1(processdefinealiasname);
					nodeext.setExt2(jump);
					nodeext.setExt3(ext3[k]);
					nodeext.setExt4(ext4[k]);
					nodeext.setExt5(ext5[k]);
					nodeext.setIndfilter(this.getSel(indfilter,nodeid[k].longValue()));
					nodeext.setJumpflag(jumpflag[k]);
					nodeext.setNodeclass(nodeclass[k]);
					nodeext.setNodeid(nodeid[k]);
					nodeext.setNodename(nodename[k]);
					nodeext.setOrgfilter(this.getSel(orgfilter,nodeid[k].longValue()));
					nodeext.setProcessdefineid(processdefineid);
					nodeext.setProcessdefinename(processdefinename);
					nodeext.setRem1(rem1[k]);
					nodeext.setVersion(version);
				
					hs.save(nodeext);
					
					nid=nodeid[k]+"";

					
					String[] dectranpath=request.getParameterValues("dectranpath_"+nid);
					if(dectranpath!=null && dectranpath.length>0){
						String decflag=request.getParameter("decflag_"+nid);
						String[] decselpath=request.getParameterValues("decselpath_"+nid);
						String[] decminqty=request.getParameterValues("decminqty_"+nid);
						String[] decmaxqty=request.getParameterValues("decmaxqty_"+nid);
						String[] decext1=request.getParameterValues("decext1_"+nid);
						String[] decext2=request.getParameterValues("decext2_"+nid);
						String[] decext3=request.getParameterValues("decext3_"+nid);
						String[] decext4=request.getParameterValues("decext4_"+nid);
						String[] decext5=request.getParameterValues("decext5_"+nid);
						String[] decext6=request.getParameterValues("decext6_"+nid);
						String[] decext7=request.getParameterValues("decext7_"+nid);
						String[] decext8=request.getParameterValues("decext8_"+nid);
						String[] decext9=request.getParameterValues("decext9_"+nid);
						String[] decext10=request.getParameterValues("decext10_"+nid);
						
						Jbpmdecisionjump dec=null;
						for(int i=0;i<dectranpath.length;i++){
							if(i==0){
								continue;
							}
							dec=new Jbpmdecisionjump();
							dec.setExt1(decext1[i]);
							dec.setExt2(decext2[i]);
							dec.setExt3(decext3[i]);
							dec.setExt4(decext4[i]);
							dec.setExt5(decext5[i]);
							dec.setExt6(getDouble(decext6[i]));
							dec.setExt7(getDouble(decext7[i]));
							dec.setExt8(getDouble(decext8[i]));
							dec.setExt9(getDouble(decext9[i]));
							dec.setExt10(getDouble(decext10[i]));
							dec.setFlag(getInteger(decflag));
							dec.setMaxqty(getDouble(decmaxqty[i]));
							dec.setMinqty(getDouble(decminqty[i]));
							dec.setNodeid(nodeid[k]);
							dec.setSelpath(decselpath[i]);
							dec.setTranpath(dectranpath[i]);
							
							hs.save(dec);
						}
					}
					
					
					String[] actactors=request.getParameterValues("actactors_"+nid);
					if(actactors!=null && actactors.length>0){
						String[] acttypeid=request.getParameterValues("acttypeid_"+nid);
						String[] acttableid=request.getParameterValues("acttableid_"+nid);
						String[] actext1=request.getParameterValues("actext1_"+nid);
						String[] actext2=request.getParameterValues("actext2_"+nid);
						String[] actext3=request.getParameterValues("actext3_"+nid);
						String[] actext4=request.getParameterValues("actext4_"+nid);
						String[] actext5=request.getParameterValues("actext5_"+nid);
						
						StringBuffer sb=new StringBuffer();
						Jbpmnodeextactors act=null;
						for(int i=0;i<actactors.length;i++){
							if(acttypeid[i]!=null && !acttypeid[i].equals("") && sb.indexOf(nodeid[k]+"_"+actactors[i]+"_"+acttypeid[i])==-1){
								sb.append(nodeid[k]+"_"+actactors[i]+"_"+acttypeid[i]).append("$");
								
								act=new Jbpmnodeextactors();
								act.setActors(actactors[i]);
								act.setExt1(actext1[i]);
								act.setExt2(actext2[i]);
								act.setExt3(actext3[i]);
								act.setExt4(actext4[i]);
								act.setExt5(actext5[i]);
								act.setNodeid(nodeid[k]);
								//act.setTableid(getInteger(acttableid[i]));
								act.setTableid(new Integer(1));
								act.setTypeid(getInteger(acttypeid[i]));
												
								hs.save(act);
							}
						}
					}
					
					String tarurl1=request.getParameter("tarurl1_"+nid);
					if(tarurl1!=null && tarurl1.length()>0){
						String taractionname=request.getParameter("taractionname_"+nid);		
						String tardes=request.getParameter("tardes_"+nid);			
						String tarparam1=request.getParameter("tarparam1_"+nid);	
						String tartarget1=request.getParameter("tartarget1_"+nid);	
						String tarurl2=request.getParameter("tarurl2_"+nid);		
						String tarparam2=request.getParameter("tarparam2_"+nid);		
						String tartarget2=request.getParameter("tartarget2_"+nid);	
						String tarurl3=request.getParameter("tarurl3_"+nid);		
						String tarparam3=request.getParameter("tarparam3_"+nid);		
						String tartarget3=request.getParameter("tartarget3_"+nid);	
						String tarext1=request.getParameter("tarext1_"+nid);	
						String tarext2=request.getParameter("tarext2_"+nid);	
						String tarext3=request.getParameter("tarext3_"+nid);	
						String tarext4=request.getParameter("tarext4_"+nid);	
						String tarext5=request.getParameter("tarext5_"+nid);
						
						Jbpmnodeexttarget tar=new Jbpmnodeexttarget();
						tar.setActionname(taractionname);
						tar.setDes(tardes);
						tar.setEnabledflag("Y");
						tar.setExt1(tarext1);
						tar.setExt2(tarext2);
						tar.setExt3(tarext3);
						tar.setExt4(tarext4);
						tar.setExt5(tarext5);
						tar.setNodeid(nodeid[k]);
						tar.setParam1(tarparam1);
						tar.setParam2(tarparam2);
						tar.setParam3(tarparam3);
						tar.setTarget1(tartarget1);
						tar.setTarget2(tartarget2);
						tar.setTarget3(tartarget3);
						tar.setUrl1(tarurl1);
						tar.setUrl2(tarurl2);
						tar.setUrl3(tarurl3);
										
						hs.save(tar);
					}
				}
			}
			hs.flush();
			tx.commit();
//			tx.rollback();

		} catch (Exception e2) {
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3);
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2);
		} finally {
			try {
				if(st!=null){
					st.close();
				}
				if(hs!=null){
					hs.close();
				}
			} catch (Exception hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				forward = mapping.findForward("ReToList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.success"));
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.error"));
				}
				forward = mapping.findForward("ReToSet");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return forward;
	}

	public ActionForward toAddRecord_old(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		
		Long processdefineid=(Long)dform.get("processdefineid");
		String processdefinename=(String)dform.get("processdefinename");
		Integer version=(Integer)dform.get("version");
		
		Long[] nodeid=(Long[])dform.get("nodeid");
		String[] nodeclass=(String[])dform.get("nodeclass");
		String[] nodename=(String[])dform.get("nodename");
		String[] des=(String[])dform.get("des");
		Integer[] orgfilter=(Integer[])dform.get("orgfilter");
		Integer[] indfilter=(Integer[])dform.get("indfilter");
		Integer[] areafilter=(Integer[])dform.get("areafilter");
		Integer[] jumpflag=(Integer[])dform.get("jumpflag");
		String[] rem1=(String[])dform.get("rem1");
		String[] ext1=(String[])dform.get("ext1");
		String[] ext2=(String[])dform.get("ext2");
		String[] ext3=(String[])dform.get("ext3");
		String[] ext4=(String[])dform.get("ext4");
		String[] ext5=(String[])dform.get("ext5");
			
		Long[] decnodeid=(Long[])dform.get("decnodeid");
		Integer[] decflag=(Integer[])dform.get("decflag");
		String[] decselpath=(String[])dform.get("decselpath");
		Double[] decminqty=(Double[])dform.get("decminqty");
		Double[] decmaxqty=(Double[])dform.get("decmaxqty");
		String[] dectranpath=(String[])dform.get("dectranpath");
		String[] decext1=(String[])dform.get("decext1");
		String[] decext2=(String[])dform.get("decext2");
		String[] decext3=(String[])dform.get("decext3");
		String[] decext4=(String[])dform.get("decext4");
		String[] decext5=(String[])dform.get("decext5");
		Double[] decext6=(Double[])dform.get("decext6");
		Double[] decext7=(Double[])dform.get("decext7");
		Double[] decext8=(Double[])dform.get("decext8");
		Double[] decext9=(Double[])dform.get("decext9");
		Double[] decext10=(Double[])dform.get("decext10");
		
		Long[] actnodeid=(Long[])dform.get("actnodeid");
		String[] actactors=(String[])dform.get("actactors");
		Integer[] acttypeid=(Integer[])dform.get("acttypeid");
		Integer[] acttableid=(Integer[])dform.get("acttableid");
		String[] actext1=(String[])dform.get("actext1");
		String[] actext2=(String[])dform.get("actext2");
		String[] actext3=(String[])dform.get("actext3");
		String[] actext4=(String[])dform.get("actext4");
		String[] actext5=(String[])dform.get("actext5");
				
		Long[] tarnodeid=(Long[])dform.get("tarnodeid");			
		String[] taractionname=(String[])dform.get("taractionname");			
		String[] tardes=(String[])dform.get("tardes");			
		String[] taranabledflag=(String[])dform.get("taranabledflag");			
		String[] tarurl1=(String[])dform.get("tarurl1");	
		String[] tarparam1=(String[])dform.get("tarparam1");	
		String[] tartarget1=(String[])dform.get("tartarget1");
		String[] tarurl2=(String[])dform.get("tarurl2");	
		String[] tarparam2=(String[])dform.get("tarparam2");	
		String[] tartarget2=(String[])dform.get("tartarget2");
		String[] tarurl3=(String[])dform.get("tarurl3");	
		String[] tarparam3=(String[])dform.get("tarparam3");	
		String[] tartarget3=(String[])dform.get("tartarget3");
		String[] tarext1=(String[])dform.get("tarext1");
		String[] tarext2=(String[])dform.get("tarext2");
		String[] tarext3=(String[])dform.get("tarext3");
		String[] tarext4=(String[])dform.get("tarext4");
		String[] tarext5=(String[])dform.get("tarext5");
		
		
		Session hs = null;
		Transaction tx = null;
		Statement st=null;
		try {
			hs = HibernateUtil.getSession();

			tx = hs.beginTransaction();
			st=hs.connection().createStatement();
			String sql="delete jbpm_nodeexttarget  " +
					"where nodeid in (" +
						"select nodeid " +
						"from jbpm_nodeext " +
						"where processdefineid="+processdefineid+")";
			st.executeUpdate(sql);
			
			sql="delete jbpm_nodeextactors  " +
			"where nodeid in (" +
				"select nodeid " +
				"from jbpm_nodeext " +
				"where processdefineid="+processdefineid+")";
			st.executeUpdate(sql);
			
			sql="delete jbpm_decisionjump  " +
			"where nodeid in (" +
				"select nodeid " +
				"from jbpm_nodeext " +
				"where processdefineid="+processdefineid+")";
			st.executeUpdate(sql);
			
			sql="delete jbpm_nodeext " +
				"where processdefineid="+processdefineid+"";
			st.executeUpdate(sql);
			
			if(nodeid!=null && nodeid.length>0){
				Jbpmnodeext nodeext=null;
				for(int i=0;i<nodeid.length;i++){
					nodeext=new Jbpmnodeext();
					nodeext.setAreafilter(this.getSel(areafilter,nodeid[i].longValue()));
					nodeext.setDes(des[i]);
					nodeext.setExt1(ext1[i]);
					nodeext.setExt2(ext2[i]);
					nodeext.setExt3(ext3[i]);
					nodeext.setExt4(ext4[i]);
					nodeext.setExt5(ext5[i]);
					nodeext.setIndfilter(this.getSel(indfilter,nodeid[i].longValue()));
					nodeext.setJumpflag(this.getSel(jumpflag,nodeid[i].longValue()));
					nodeext.setNodeclass(nodeclass[i]);
					nodeext.setNodeid(nodeid[i]);
					nodeext.setNodename(nodename[i]);
					nodeext.setOrgfilter(this.getSel(orgfilter,nodeid[i].longValue()));
					nodeext.setProcessdefineid(processdefineid);
					nodeext.setProcessdefinename(processdefinename);
					nodeext.setRem1(rem1[i]);
					nodeext.setVersion(version);
				
					hs.save(nodeext);
				}
			}
			if(decnodeid!=null && decnodeid.length>0){
				Jbpmdecisionjump dec=null;
				for(int i=0;i<decnodeid.length;i++){
					dec=new Jbpmdecisionjump();
					dec.setExt1(decext1[i]);
					dec.setExt2(decext2[i]);
					dec.setExt3(decext3[i]);
					dec.setExt4(decext4[i]);
					dec.setExt5(decext5[i]);
					dec.setExt6(decext6[i]);
					dec.setExt7(decext7[i]);
					dec.setExt8(decext8[i]);
					dec.setExt9(decext9[i]);
					dec.setExt10(decext10[i]);
					dec.setFlag(decflag[i]);
					dec.setMaxqty(decmaxqty[i]);
					dec.setMinqty(decminqty[i]);
					dec.setNodeid(decnodeid[i]);
					dec.setSelpath(decselpath[i]);
					dec.setTranpath(dectranpath[i]);
					
					hs.save(dec);
				}
			}
			if(actnodeid!=null && actnodeid.length>0){
				Jbpmnodeextactors act=null;
				for(int i=0;i<actnodeid.length;i++){
					act=new Jbpmnodeextactors();
					act.setActors(actactors[i]);
					act.setExt1(actext1[i]);
					act.setExt2(actext2[i]);
					act.setExt3(actext3[i]);
					act.setExt4(actext4[i]);
					act.setExt5(actext5[i]);
					act.setNodeid(actnodeid[i]);
					act.setTableid(acttableid[i]);
					act.setTypeid(acttypeid[i]);
									
					hs.save(act);
				}
			}
			if(tarnodeid!=null && tarnodeid.length>0){
				Jbpmnodeexttarget tar=null;
				for(int i=0;i<tarnodeid.length;i++){
					tar=new Jbpmnodeexttarget();
					tar.setActionname(taractionname[i]);
					tar.setDes(tardes[i]);
					tar.setEnabledflag("Y");
					tar.setExt1(tarext1[i]);
					tar.setExt2(tarext2[i]);
					tar.setExt3(tarext3[i]);
					tar.setExt4(tarext4[i]);
					tar.setExt5(tarext5[i]);
					tar.setNodeid(tarnodeid[i]);
					tar.setParam1(tarparam1[i]);
					tar.setParam2(tarparam2[i]);
					tar.setParam3(tarparam3[i]);
					tar.setTarget1(tartarget1[i]);
					tar.setTarget2(tartarget2[i]);
					tar.setTarget3(tartarget3[i]);
					tar.setUrl1(tarurl1[i]);
					tar.setUrl2(tarurl2[i]);
					tar.setUrl3(tarurl3[i]);
									
					hs.save(tar);
				}
			}
			hs.flush();
			//tx.commit();
			tx.rollback();

		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"loginuser.insert.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Loginuser Insert error!");
		} finally {
			try {
				if(st!=null){
					st.close();
				}
				if(hs!=null){
					hs.close();
				}
			} catch (Exception hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				forward = mapping.findForward("returnList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"insert.success"));
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"insert.error"));
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
	 * Method toDeleteRecord execute, Delete record where roleid = id 删除方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDeleteRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		String id = (String) dform.get("id");
		Connection con=null;
		Session hs = null;
		try {
			//con=this.getDataSource(request,SysConfig.DataSource).getConnection();
			hs = HibernateUtil.getSession();
			con=hs.connection();
			con.setAutoCommit(false);
			Statement st=con.createStatement();
			String sql="delete jbpm_nodeexttarget a " +
						"where a.nodeid in (" +
							"select nodeid " +
							"from jbpm_nodeext " +
							"where processdefineid="+id+")";
			st.executeUpdate(sql);
	
			sql="delete jbpm_nodeextactors a " +
				"where a.nodeid in (" +
					"select nodeid " +
					"from jbpm_nodeext " +
					"where processdefineid="+id+")";
			st.executeUpdate(sql);
			
			sql="delete jbpm_decisionjump a " +
				"where a.nodeid in (" +
					"select nodeid " +
					"from jbpm_nodeext " +
					"where processdefineid="+id+")";
			st.executeUpdate(sql);
			
			sql="delete jbpm_nodeext " +
				"where processdefineid="+id+")";
			st.executeUpdate(sql);
			
			con.commit();

		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"delete.foreignkeyerror"));
			try {
				con.rollback();
			} catch (Exception e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Loginuser Update error!");
		} finally {
			try {
				if(con!=null){
					con.rollback();
				}
				if(hs!=null){
					hs.close();
				}
			} catch (Exception hex) {
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
	 * Method toSearchRecord execute, Search record 查询页面，显示数据，可根据条件进行相应的查询
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		this.setNavigation(request, "navigator.base.flowset.list");
		ActionForward forward = null;
		// listRecord
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		Session hs = null;

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {

//				response = toExcelRecord(form, request, response);
//				forward = mapping.findForward("exportExcel");
//				tableForm.setProperty("genReport", "");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "FlowList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fFlowSearch");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("userid");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String flowid = tableForm.getProperty("flowid");
			String flowname = tableForm.getProperty("flowname");
			String version = tableForm.getProperty("version");
			
			if(flowid!=null && flowid.length()>=0){
				flowid="%"+flowid.trim()+"%";
			}else{
				flowid="%";
			}
			if(flowname!=null && flowname.length()>=0){
				flowname="%"+flowname.trim()+"%";
			}else{
				flowname="%";
			}
			if(version!=null && version.length()>=0){
				version=version.trim();
			}else{
				version="0";
				tableForm.setProperty("version",version);
			}
			
			Connection con=null;
			Statement st=null;
			ResultSet rs=null;
			try {

				hs = HibernateUtil.getSession();
				con=hs.connection();
				//con=this.getDataSource(request,SysConfig.DataSource).getConnection();

				String sql="Sp_JbpmQueryFlowSet '"+flowid+"','"+flowname+"','"+version+"'";
				
				st=con.createStatement();
				rs=st.executeQuery(sql);
				int qty=0,s=table.getFrom(),e=table.getFrom()+table.getLength();
				
				List rslist=new ArrayList();
				HashMap map=null;
				while(rs.next()){
					if(qty>=s && qty<e){
						map=new HashMap();
						map.put("pid",rs.getLong("pid")+"");
						map.put("pname",rs.getString("pname"));
						map.put("ver",rs.getLong("ver")+"");
						map.put("paliasname",rs.getString("paliasname"));
						rslist.add(map);
					}
					qty++;
				}
				table.setVolume(qty);// 查询得出数据记录数;
				cache.check(table);
				table.addAll(rslist);
				session.setAttribute("FlowList", table);
				
			} catch (Exception e1) {
				DebugUtil.print(e1);
			} finally {
				try {
					if(con!=null){
						con.close();
					}
					if(hs!=null){
						hs.close();
					}
				} catch (Exception hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}

			forward = mapping.findForward("ToList");
		}
		return forward;
	}

	/**
	 * Method toDisplayRecord execute,prepare data for update page 查看页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		this.setNavigation(request, "navigator.base.flowset.view");

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}

		String pid=request.getParameter("id");
		Connection con=null;
		Statement st=null;
		Session hs = null;
		try {
			//con=this.getDataSource(request,SysConfig.DataSource).getConnection();
			hs = HibernateUtil.getSession();
			con=hs.connection();
			st=con.createStatement();
			List tranlist=null,atcorlist=null,decilist=null,targetlist=null,classlist=null;
			List nodeext=this.getNodeOld(st,pid);
			if(nodeext!=null && nodeext.size()>0){
				String nodes=this.getNodes(nodeext);
				if(nodes!=null && nodes.length()>0){
					tranlist=this.getTran(st,nodes);
				}
				if(nodes!=null && nodes.length()>0){
					atcorlist=this.getActors(st,nodes);
				}
				if(nodes!=null && nodes.length()>0){
					decilist=this.getDeci(st,nodes);
				}
				if(nodes!=null && nodes.length()>0){
					targetlist=this.getTarget(st,nodes);
				}
//				if(nodes!=null && nodes.length()>0){
//					classlist=this.getClass(st);
//				}
//				request.setAttribute("classlist",classlist);
				FlowSetBean fsb=new FlowSetBean(nodeext,atcorlist,targetlist,decilist,tranlist);
				
				request.setAttribute("flow",fsb);
			}
			
			
		} catch (Exception e1) {
			DebugUtil.print(e1);
		} finally {
			try {
				if(st!=null){
					st.close();
				}
			} catch (Exception hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex);
			}
			try {
				if(con!=null){
					con.close();
				}
				if(hs!=null){
					hs.close();
				}
			} catch (Exception hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex);
			}
		}
		request.setAttribute("display", "yes");
		ActionForward forward = mapping.findForward("ToDisplay");
		return forward;
	}
	public ActionForward toDisplayRecord_old(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		this.setNavigation(request, "navigator.base.loginuser.view");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		ActionForward forward = null;
		String pid=request.getParameter("id");
		Connection con=null;
		Statement st=null;
		Session hs = null;
		try {
			//con=this.getDataSource(request,SysConfig.DataSource).getConnection();
			hs = HibernateUtil.getSession();
			con=hs.connection();
			st=con.createStatement();
//			DBInterface dbi=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
//			String sql="select a.* " +
//					"from jbpm_nodeext a " +
//					"where a.processdefineid='"+pid+"' ";
//			dbi.setCon(con);
//			List nodeext=dbi.queryToList(sql);
			List tranlist=null,atcorlist=null,decilist=null,targetlist=null;
//			if(nodeext==null || nodeext.size()==0){
//				sql="select b.id_ as nodeid,b.class_ as nodeclass,b.name_ as nodename,a.id_ as processdefineid,a.name_ as processdefinename,a.version_ as version,'' as des,0 as orgfilter,0 as indfilter,0 as areafilter,0 as jumflag,'' as rem1,'' as ext1,'' as ext2,'' as ext3,'' as ext4,'' as ext5 "+
//					"from jbpm_processdefinition a,jbpm_node b "+
//					"where a.id_='"+pid+"' " +
//					"and	a.id_=b.processdefinition_ "+				
//					"and	b.class_	in('k','d') " +
//					"order by b.id_  ";
//				
//				nodeext=dbi.queryToList(sql);
//			}
			List nodeext=this.getNode(st,pid);
			if(nodeext!=null && nodeext.size()>0){
				String nodes=this.getNodes(nodeext);
				if(nodes!=null && nodes.length()>0){
					tranlist=this.getTran(st,nodes);
				}
				if(nodes!=null && nodes.length()>0){
					atcorlist=this.getActors(st,nodes);
				}
				if(nodes!=null && nodes.length()>0){
					decilist=this.getDeci(st,nodes);
				}
				if(nodes!=null && nodes.length()>0){
					targetlist=this.getTarget(st,nodes);
				}
			}
			
			request.setAttribute("tranlist",tranlist);
			request.setAttribute("atcorlist",atcorlist);
			request.setAttribute("decilist",decilist);
			request.setAttribute("targetlist",targetlist);
		} catch (Exception e1) {
			DebugUtil.print(e1);
		} finally {
			try {
				if(con!=null){
					con.close();
				}
				if(hs!=null){
					hs.close();
				}
			} catch (Exception hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex);
			}
		}
		request.setAttribute("display", "yes");
		forward = mapping.findForward("ToDisplay");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

//	/**
//	 * Method toSearchRecord execute, to Excel Record 列表查询导出Excel 导出EXCEL方法
//	 * 
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return ActionForward
//	 * @throws IOException
//	 */
//
//	public HttpServletResponse toExcelRecord(ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws IOException {
//
//		ServeTableForm tableForm = (ServeTableForm) form;
//
//		String username = tableForm.getProperty("username");
//		String userid = tableForm.getProperty("userid");
//		String enabledflag = tableForm.getProperty("enabledflag");
//		String loginflag = tableForm.getProperty("loginflag");
//
//		String roleid = tableForm.getProperty("roleid");
//		String class1 = tableForm.getProperty("class1");
////		String dataroleid = tableForm.getProperty("dataroleid");
//		Session hs = null;
//		HSSFWorkbook wb = new HSSFWorkbook();
//
//		try {
//			hs = HibernateUtil.getSession();
//
//			String sql0="Select distinct count(a.userid) ";
//			String sql1="Select a,b ";
//			String sql="From Loginuser a,Class1 b Where 1=1 and a.class1 = b.class1id ";
//			String order="";
//			if(userid!=null && userid.length()>0){
//				sql+="and a.userid like '%"+userid.trim()+"%' ";
//			}
//			if(username!=null && username.length()>0){
//				sql+="and a.username like '%"+username.trim()+"%' ";
//			}
//			if(roleid!=null && roleid.length()>0){
//				sql+="and a.roleid like '%"+roleid.trim()+"%' ";
//			}
////			if(dataroleid!=null && dataroleid.length()>0){
////				sql+="and a.dataroleid like '%"+dataroleid.trim()+"%' ";
////			}
//			if(enabledflag!=null && enabledflag.length()>0){
//				sql+="and a.enabledflag like '%"+enabledflag.trim()+"%' ";
//			}
//			if(loginflag!=null && loginflag.length()>0){
//				sql+="and a.loginflag like '%"+loginflag.trim()+"%' ";
//			}
//			if(class1!=null && class1.length()>0){
//				sql+="and b.class1id like '%"+class1.trim()+"%' ";
//			}
//
//
//			Query query = hs.createQuery(sql1+sql+order);
//			
//			
//
//			List roleList = query.list();
//			
//			HSSFSheet sheet = wb.createSheet("new sheet");
//
//			Locale locale = this.getLocale(request);
//			MessageResources messages = getResources(request);
//
//			if (roleList != null && !roleList.isEmpty()) {
//				int l = roleList.size();
//				HSSFRow row0 = sheet.createRow( 0);
//				HSSFCell cell0 = row0.createCell((short) 0);
//				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
//				cell0.setCellValue(messages.getMessage(locale,
//						"loginuser.userid"));
//
//				cell0 = row0.createCell((short) 1);
//				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
//				cell0.setCellValue(messages.getMessage(locale,
//						"loginuser.username"));
//
//				cell0 = row0.createCell((short) 2);
//				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
//				cell0.setCellValue(messages.getMessage(locale,
//						"loginuser.deptid"));
//
//				// cell0 = row0.createCell((short) 3);
//				// //cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
//				// cell0.setCellValue(messages.getMessage(locale,
//				// "loginuser.class1"));
//
//				cell0 = row0.createCell((short) 3);
//				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
//				cell0.setCellValue(messages.getMessage(locale,
//						"loginuser.roleid"));
//				
//				cell0 = row0.createCell((short) 4);
//				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
//				cell0.setCellValue("职位");
//
////				cell0 = row0.createCell((short) 4);
////				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
////				cell0.setCellValue(messages.getMessage(locale,
////						"loginuser.dataroleid"));
//
//				cell0 = row0.createCell((short) 5);
//				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
//				cell0.setCellValue(messages.getMessage(locale,
//						"loginuser.enabledflag"));
//
//				cell0 = row0.createCell((short) 6);
//				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
//				cell0.setCellValue(messages
//						.getMessage(locale, "loginuser.rem1"));
//				Loginuser loginuser=null;
//				Class1 cl=null;
//				for (int i = 0; i < l; i++) {
//					Object[] obj=(Object[])roleList.get(i);
//					loginuser=(Loginuser)obj[0];
//					cl=(Class1)obj[1];
//					
//					// 创建Excel行，从0行开始
//					HSSFRow row = sheet.createRow( i + 1);
//
//					// 创建Excel列
//					HSSFCell cell = row.createCell((short) 0);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					cell.setCellValue(loginuser.getUserid());
//
//					cell = row.createCell((short) 1);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					cell.setCellValue(loginuser.getUsername());
//
//					cell = row.createCell((short) 2);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					cell.setCellValue(this.getDeptUserid(hs, loginuser
//							.getUserid()));
//
//					// cell = row.createCell((short) 3);
//					// //cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					// cell.setCellValue(loginuser.getClass1());
//
//					cell = row.createCell((short) 3);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					cell.setCellValue(bd.getName("Role", "rolename", "roleid",
//							loginuser.getRoleid()));
//					
//					cell = row.createCell((short) 4);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					cell.setCellValue(cl.getClass1name());
//
////					cell = row.createCell((short) 4);
////					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
////					cell.setCellValue(bd.getName("Datarole", "datarolename",
////							"dataroleid", loginuser.getDataroleid()));
//
//					cell = row.createCell((short) 5);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					cell.setCellValue(CommonUtil.tranEnabledFlag(loginuser
//							.getEnabledflag()));
//
//					cell = row.createCell((short) 6);
//					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
//					cell.setCellValue(loginuser.getRem1());
//				}
//			}
//
//		} catch (HibernateException e1) {
//			e1.printStackTrace();
//		} finally {
//			try {
//				hs.close();
//			} catch (HibernateException hex) {
//				log.error(hex.getMessage());
//				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
//			}
//		}
//		response.setContentType("application/vnd.ms-excel");
//		response.setHeader("Content-disposition",
//				"offline; filename=loginuser.xls");
//		wb.write(response.getOutputStream());
//		return response;
//	}
	/*
	 * 配置审核人
	 * 权限保存方法
	 * 
	 * /
	 */
	public ActionForward toSetShrRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		ActionErrors errors = new ActionErrors();
		String processdefinename=request.getParameter("processdefinename");//流程id
		String processdefinealiasname=request.getParameter("processdefinealiasname");//流程名称
		String version=request.getParameter("version");//版本号
		String processDefine=processdefinename;//流程大组名称
		String[] pfs=processdefinename.split("_");
		if(pfs!=null && pfs.length>0){
			processDefine=pfs[0];
		}
//		String pfs=processdefinename.substring(processdefinename.length()-2,processdefinename.length());
//		if(pfs.equals("00") ||pfs.equals("01") ||pfs.equals("02") ||pfs.equals("03") ||pfs.equals("04") ||pfs.equals("05") ){
//			processDefine=processdefinename.substring(0,processdefinename.length()-2);
//		}
		String[] nodeid=request.getParameterValues("nodeid");//节点id
		String[] nodename=request.getParameterValues("nodename");//节点名称
		String[] comids=request.getParameterValues("comid");	//所属分公司	
		String nodeids="";
		String comidss="";
		if(comids !=null && comids.length>0){
			for(int i=0;i<comids.length;i++){
				comidss+="'"+comids[i]+"',";
			}
			comidss=comidss.substring(0,comidss.length()-1);
		}
		if(nodename !=null && nodename.length>0){
			for(int i=0;i<nodename.length;i++){
		         nodeids+="'"+nodename[i]+"',";
			}
			nodeids=nodeids.substring(0,nodeids.length()-1);
		}
		Session hs = null;
		Transaction tx = null;
		Statement st=null;
		String sql="";
		try {
			hs = HibernateUtil.getSession();

			tx = hs.beginTransaction();
			st=hs.connection().createStatement();
			String billno[] = CommonUtil.getJnlNo("00","00Sh",SysConfig.QUOTECONNECT_VERSION_JNLNO_LEN,nodeid.length,"N");	
			sql="delete from ProcessSetBranch where processDefineId='"+processdefinename+"'";
			st.executeUpdate(sql);
			
			sql="select a from ProcessSetBranch  a where a.processDefine='"+processDefine+"'";
			if(comidss !=null && !comidss.equals("")){
				sql+=" and a.comId in("+comidss+")";
			}
			List kk=hs.createQuery(sql).list();
			if(kk!=null && kk.size()>0){
				if(comidss !=null && !comidss.equals("")){
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","<font color='red'>流程已配置相同所属分部，请重新配置！</font>"));// 未有资源
			  throw new Exception();
				}
			}
			sql="delete ProcessSetAuditDetail from ProcessSetAuditMaster a,ProcessSetAuditDetail b where a.billno=b.billno and  a.nodename in("+nodeids+" ) and a.processDefineId"
					+ "='"+processdefinename+"'";
			st.executeUpdate(sql);
			
			sql="delete ProcessSetAuditMaster "+
					" where processDefineID='"+processdefinename+"' and nodename in("+nodeids+" )";
			st.executeUpdate(sql);
			
			
			
			if(comids !=null && comids.length>0){
				ProcessSetBranch Branch=null;
				for(int i=0;i<comids.length;i++){
					Branch=new ProcessSetBranch();
					Branch.setProcessDefine(processDefine);
					Branch.setProcessDefineId(processdefinename);
					Branch.setComId(comids[i]);
					hs.save(Branch);
				}
			}
			
			
			if(nodeid!=null && nodeid.length>0){
				String nid="";
				ProcessSetAuditMaster psm=null;
				for(int k=0;k<nodeid.length;k++){
					nid=nodeid[k]+"";
					psm=new ProcessSetAuditMaster();
					psm.setBillno(billno[k]);
					psm.setProcessDefineId(processdefinename);
					psm.setNodeid(String.valueOf(nodeid[k]));
					psm.setNodename(nodename[k]);
					
					hs.save(psm);

					
					String[] userid=request.getParameterValues("userid_"+nid);
					String[] username=request.getParameterValues("username_"+nid);
					String[] grcid=request.getParameterValues("grcid_"+nid);
					String[] grcname=request.getParameterValues("grcname_"+nid);
					if(userid!=null && userid.length>0){
						ProcessSetAuditDetail psd=null;
						for(int i=1;i<userid.length;i++){
							psd=new ProcessSetAuditDetail();
							psd.setBillno(billno[k]);
							psd.setAuditoperid(userid[i]);
							psd.setComid(grcid[i]);
							hs.save(psd);
						}
					}
				}
			}
			hs.flush();
			tx.commit();
		} catch (Exception e2) {
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3);
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2);
		} finally {
			try {
				if(st!=null){
					st.close();
				}
				if(hs!=null){
					hs.close();
				}
			} catch (Exception hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				forward = mapping.findForward("ReToList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.success"));
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.error"));
				}
				forward = mapping.findForward("ReToShr");
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
	 * 根据用户名称，查询出他所在的部门名称
	 * 
	 * @param hs
	 * @param userid
	 *            用户编号
	 * @return 返回组成的部门名称字符串，多个部门用","隔开。
	 * @throws DataStoreException 
	 */
//	public String getDeptUserid(Session hs, String userid) {
//		StringBuffer reStr = null;
//		try {
//			Connection conn = hs.connection();
//			String sql = "select b.deptname from userdeptset as a ,dept as b where a.deptid=b.deptid "
//					+ "and a.userid='" + userid + "'";
//			DataOperation op = new DataOperation();
//			op.setCon(conn);
//			List tempList = op.queryToList(sql);
//			for (int i = 0; i < tempList.size(); i++) {
//				if (reStr == null) {
//					reStr = new StringBuffer();
//					HashMap map = (HashMap) tempList.get(i);
//					reStr.append(map.get("deptname"));
//				} else {
//					HashMap map = (HashMap) tempList.get(i);
//					reStr.append(",").append(map.get("deptname"));
//				}
//			}
//		} catch (HibernateException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		if (reStr == null)
//			reStr = new StringBuffer();
//		return reStr.toString();
//	}
	public ActionForward getusershr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{

		this.setNavigation(request, "navigator.base.loginuser.list");
		
		Session hs=null;
		ActionForward forward=null;
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		////System.out.println(userInfo.getComID()+";"+userInfo.getComName()+" ; "+userInfo.getModuleID()+";"+userInfo.getModuleName());
		
		int location = 0;
		HTMLTableCache cache = new HTMLTableCache(session,"mainshrlist");
		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fflowgetshr");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setIsAscending(true);
		cache.updateTable(table);
		
		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
			location = table.getFrom();
		} else {
			table.setFrom(0);
			location = 0;
		}
		cache.saveForm(tableForm);
		String sql="select * from view_LoginUser_grtype where 1=1 and EnabledFlag='Y'";
		String grcid = tableForm.getProperty("grcid");// 所属分部代码
		List grcidlist = Grcnamelist1.getgrcnamelist(userInfo.getUserID()); // 获取所属分部列表
		if (grcid == null && grcidlist.size() >0) {// 根据当前用户初始化所属分部
			Map map = (Map) grcidlist.get(0);
			grcid = (String) map.get("grcid");	
		}
		sql+=" and grcid like  '%"+grcid.trim()+"%'";
		String userid=(String)tableForm.getProperty("userid");//用户id
		if(userid!=null && !userid.equals("")){
			sql+=" and userid like '%"+userid.trim()+"%'";
		}
		String username=(String)tableForm.getProperty("username");//用户名称
		if(username!=null && !username.equals("")){
			sql+=" and username like '%"+username.trim()+"%'";
		}
		List list2=new ArrayList();
		List list3=new ArrayList();
		Query query=null;
		try{
    		 hs=HibernateUtil.getSession();
    		 query=hs.createSQLQuery(sql);
    		 table.setVolume(query.list().size());// 查询得出数据记录数;
    		 query.setFirstResult(location);
    		 query.setMaxResults(table.getLength());
    		 cache.check(table);
    		 list2=query.list();
    		 
    		 for(int i=0;i<list2.size();i++){
    			 Object[] obj=(Object[]) list2.get(i);
    			 HashMap hm=new HashMap();
    			 hm.put("userid", obj[0]);
    			 hm.put("username", obj[1]);
    			 hm.put("grcid", obj[2]);
    			 hm.put("grcname", obj[3]);
    			 list3.add(hm);
    		 }
 			table.addAll(list3);
 			request.setAttribute("grcidlist", grcidlist);
    		 session.setAttribute("mainshrlist", table);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(hs!=null){
				hs.close();
			}
		}
		forward=mapping.findForward("tshr");
		return forward;
	}
	
	private List getNode(Statement st,String pid) throws SQLException{
		
		List rslist=new ArrayList();
		String sql="select a.* " +
					"from jbpm_nodeext a " +
					"where a.processdefineid='"+pid+"' " +
					"order by a.jumpflag ";
		ResultSet rs=st.executeQuery(sql);
		HashMap map=null;
		while(rs.next()){
			map=new HashMap();
			map.put("nodeid",rs.getLong("nodeid")+"");
			map.put("nodeclass",rs.getString("nodeclass"));
			map.put("nodename",rs.getString("nodename"));
			map.put("processdefineid",rs.getLong("processdefineid")+"");
			map.put("processdefinename",rs.getString("processdefinename"));
			map.put("version",rs.getLong("version")+"");
			map.put("des",rs.getString("des"));
			map.put("orgfilter",rs.getString("orgfilter"));
			map.put("indfilter",rs.getString("indfilter"));
			map.put("areafilter",rs.getString("areafilter"));
			map.put("jumpflag",rs.getString("jumpflag"));
			map.put("rem1",rs.getString("rem1"));
			map.put("ext1",rs.getString("ext1"));
			map.put("ext2",rs.getString("ext2"));
			map.put("ext3",rs.getString("ext3"));
			map.put("ext4",rs.getString("ext4"));
			map.put("ext5",rs.getString("ext5"));
			rslist.add(map);
			
		}
		if(rslist==null || rslist.size()==0){
//			sql="select b.id_ as nodeid,b.class_ as nodeclass,b.name_ as nodename,a.id_ as processdefineid,a.name_ as processdefinename,a.version_ as version," +
//					"'' as des,0 as orgfilter,0 as indfilter,0 as areafilter,0 as jumflag,'' as rem1,'' as ext1,'' as ext2,'' as ext3,'' as ext4,'' as ext5 "+
//				"from jbpm_processdefinition a,jbpm_node b "+
//				"where a.id_='"+pid+"' " +
//				"and	a.id_=b.processdefinition_ "+				
//				"and	b.class_	in('k','d') " +
//				"order by b.id_  ";
			
		    sql="Sp_JbpmNodeExtNew "+pid;
			
			rs=st.executeQuery(sql);
			while(rs.next()){
				map=new HashMap();
				map.put("nodeid",rs.getLong("nodeid")+"");
				map.put("nodeidNew",rs.getLong("nodeidNew")+"");
				map.put("nodeclass",rs.getString("nodeclass"));
				map.put("nodename",rs.getString("nodename"));
				map.put("processdefineid",rs.getLong("processdefineid")+"");
				map.put("processdefinename",rs.getString("processdefinename"));
				map.put("version",rs.getLong("version")+"");
				map.put("des",rs.getString("des"));
				map.put("orgfilter",rs.getString("orgfilter"));
				map.put("indfilter",rs.getString("indfilter"));
				map.put("areafilter",rs.getString("areafilter"));
				map.put("jumpflag",rs.getString("jumpflag"));
				map.put("rem1",rs.getString("rem1"));
				map.put("ext1",rs.getString("ext1"));
				map.put("ext2",rs.getString("ext2"));
				map.put("ext3",rs.getString("ext3"));
				map.put("ext4",rs.getString("ext4"));
				map.put("ext5",rs.getString("ext5"));
				rslist.add(map);
			}
		}
		rs=null;
		return rslist;
	}
	
	private List getNodeOld(Statement st,String pid) throws SQLException{
		
		List rslist=new ArrayList();
		String sql="select a.* " +
					"from jbpm_nodeext a " +
					"where a.processdefineid='"+pid+"' " +
					"order by a.jumpflag ";
		ResultSet rs=st.executeQuery(sql);
		HashMap map=null;
		while(rs.next()){
			map=new HashMap();
			map.put("nodeid",rs.getLong("nodeid")+"");
			map.put("nodeclass",rs.getString("nodeclass"));
			map.put("nodename",rs.getString("nodename"));
			map.put("processdefineid",rs.getLong("processdefineid")+"");
			map.put("processdefinename",rs.getString("processdefinename"));
			map.put("version",rs.getLong("version")+"");
			map.put("des",rs.getString("des"));
			map.put("orgfilter",rs.getString("orgfilter"));
			map.put("indfilter",rs.getString("indfilter"));
			map.put("areafilter",rs.getString("areafilter"));
			map.put("jumpflag",rs.getString("jumpflag"));
			map.put("rem1",rs.getString("rem1"));
			map.put("ext1",rs.getString("ext1"));
			map.put("ext2",rs.getString("ext2"));
			map.put("ext3",rs.getString("ext3"));
			map.put("ext4",rs.getString("ext4"));
			map.put("ext5",rs.getString("ext5"));
			rslist.add(map);
			
		}
		if(rslist==null || rslist.size()==0){
			sql="select b.id_ as nodeid,b.class_ as nodeclass,b.name_ as nodename,a.id_ as processdefineid,a.name_ as processdefinename,a.version_ as version," +
					"'' as des,0 as orgfilter,0 as indfilter,0 as areafilter,0 as jumflag,'' as rem1,'' as ext1,'' as ext2,'' as ext3,'' as ext4,'' as ext5 "+
				"from jbpm_processdefinition a,jbpm_node b "+
				"where a.id_='"+pid+"' " +
				"and	a.id_=b.processdefinition_ "+				
				"and	b.class_	in('k','d') " +
				"order by b.id_  ";
			
			rs=st.executeQuery(sql);
			while(rs.next()){
				map=new HashMap();
				map.put("nodeid",rs.getLong("nodeid")+"");
				map.put("nodeclass",rs.getString("nodeclass"));
				map.put("nodename",rs.getString("nodename"));
				map.put("processdefineid",rs.getLong("processdefineid")+"");
				map.put("processdefinename",rs.getString("processdefinename"));
				map.put("version",rs.getLong("version")+"");
				map.put("des",rs.getString("des"));
				map.put("orgfilter",rs.getString("orgfilter"));
				map.put("indfilter",rs.getString("indfilter"));
				map.put("areafilter",rs.getString("areafilter"));
				map.put("jumflag",rs.getString("jumflag"));
				map.put("rem1",rs.getString("rem1"));
				map.put("ext1",rs.getString("ext1"));
				map.put("ext2",rs.getString("ext2"));
				map.put("ext3",rs.getString("ext3"));
				map.put("ext4",rs.getString("ext4"));
				map.put("ext5",rs.getString("ext5"));
				rslist.add(map);
			}
		}
		rs=null;
		return rslist;
	}
	/**
	 * transition
	 * @param dbi
	 * @param nodeid
	 * @return
	 * @throws SQLException
	 */
	private List getTran(Statement st,String nodeid) throws SQLException{
		List rslist=new ArrayList();
		if(nodeid!=null && nodeid.length()>0){
			String sql="select a.from_ as nodeid,a.name_ as tranname " +
					"from JBPM_TRANSITION a " +
					"where a.from_ in("+nodeid+") " +
					"order by a.from_,a.fromindex_ ";
			ResultSet rs=st.executeQuery(sql);
			HashMap map=null;
			while(rs.next()){
				map=new HashMap();
				map.put("nodeid",rs.getLong("nodeid")+"");
				map.put("tranname",rs.getString("tranname"));
				rslist.add(map);
			}
			rs=null;
			
		}
		return rslist;
	}
//	private List getTran_old(DBInterface dbi,String nodeid) throws SQLException{
//		List rslist=null;
//		if(nodeid!=null && nodeid.length()>0){
//			String sql="select a.from_ as nodeid,a.name_ as tranname " +
//					"from JBPM_TRANSITION a " +
//					"where a.from_ in("+nodeid+") " +
//					"order by a.from_,a.fromindex_ ";
//			rslist=dbi.queryToList(sql);
//		}
//		return rslist;
//	}
	/**
	 * actors
	 * @param dbi
	 * @param nodeid
	 * @return
	 * @throws SQLException
	 */
	private List getActors(Statement st,String nodeid) throws SQLException{
		List rslist=new ArrayList();
		if(nodeid!=null && nodeid.length()>0){
			String sql="Sp_JbpmNodeExtActorSet '" +nodeid+"'";
			
			ResultSet rs=st.executeQuery(sql);
			HashMap map=null;
			while(rs.next()){
				map=new HashMap();
				map.put("nodeid",rs.getLong("nodeid")+"");
				map.put("actors",rs.getString("actors"));
				map.put("typeid",rs.getString("typeid"));
				map.put("tableid",rs.getString("tableid"));
				map.put("ext1",rs.getString("ext1"));
				map.put("ext2",rs.getString("ext2"));
				map.put("ext3",rs.getString("ext3"));
				map.put("ext4",rs.getString("ext4"));
				map.put("ext5",rs.getString("ext5"));
				map.put("typename",rs.getString("typename"));
				map.put("actorsname",rs.getString("actorsname"));
				rslist.add(map);
			}
			rs=null;
		}
		return rslist;
	}
//	private List getActors_old(DBInterface dbi,String nodeid) throws SQLException{
//		List rslist=null;
//		if(nodeid!=null && nodeid.length()>0){
//			String sql="select a.* " +
//					"from JBPM_nodeextactors a " +
//					"where a.nodeid in("+nodeid+") " +
//					"order by a.nodeid ";
//			rslist=dbi.queryToList(sql);
//		}
//		return rslist;
//	}
	/**
	 * decision
	 * @param dbi
	 * @param nodeid
	 * @return
	 * @throws SQLException
	 */
	private List getDeci(Statement st,String nodeid) throws SQLException{
		List rslist=new ArrayList();
		if(nodeid!=null && nodeid.length()>0){
			String sql="select a.* " +
					"from Jbpm_DecisionJump a " +
					"where a.nodeid in("+nodeid+") " +
					"order by a.nodeid ";
			ResultSet rs=st.executeQuery(sql);
			HashMap map=null;
			while(rs.next()){
				map=new HashMap();
				map.put("nodeid",rs.getLong("nodeid")+"");
				map.put("flag",rs.getString("flag"));
				map.put("selpath",rs.getString("selpath"));
				map.put("minqty",rs.getDouble("minqty")+"");
				map.put("maxqty",rs.getDouble("maxqty")+"");
				map.put("tranpath",rs.getString("tranpath"));
				map.put("ext1",rs.getString("ext1"));
				map.put("ext2",rs.getString("ext2"));
				map.put("ext3",rs.getString("ext3"));
				map.put("ext4",rs.getString("ext4"));
				map.put("ext5",rs.getString("ext5"));
				map.put("ext6",rs.getString("ext6"));
				map.put("ext7",rs.getString("ext7"));
				map.put("ext8",rs.getString("ext8"));
				map.put("ext9",rs.getString("ext9"));
				map.put("ext10",rs.getString("ext10"));
				rslist.add(map);
			}
			rs=null;
		}
		return rslist;
	}
//	private List getDeci_old(DBInterface dbi,String nodeid) throws SQLException{
//		List rslist=null;
//		if(nodeid!=null && nodeid.length()>0){
//			String sql="select a.* " +
//					"from Jbpm_DecisionJump a " +
//					"where a.nodeid in("+nodeid+") " +
//					"order by a.nodeid ";
//			rslist=dbi.queryToList(sql);
//		}
//		return rslist;
//	}
	/**
	 * url
	 * @param dbi
	 * @param nodeid
	 * @return
	 * @throws SQLException
	 */
	private List getTarget(Statement st,String nodeid) throws SQLException{
		List rslist=new ArrayList();
		if(nodeid!=null && nodeid.length()>0){
			String sql="select a.* " +
					"from JBPM_NodeExtTarget a " +
					"where a.nodeid in("+nodeid+") " +
					"order by a.nodeid ";
			ResultSet rs=st.executeQuery(sql);
			HashMap map=null;
			while(rs.next()){
				map=new HashMap();
				map.put("nodeid",rs.getLong("nodeid")+"");
				map.put("actionname",rs.getString("actionname"));
				map.put("des",rs.getString("des"));
				map.put("url1",rs.getString("url1"));
				map.put("param1",rs.getString("param1"));
				map.put("target1",rs.getString("target1"));
				map.put("url2",rs.getString("url2"));
				map.put("param2",rs.getString("param2"));
				map.put("target2",rs.getString("target2"));
				map.put("url3",rs.getString("url3"));
				map.put("param3",rs.getString("param3"));
				map.put("target3",rs.getString("target3"));
				map.put("ext1",rs.getString("ext1"));
				map.put("ext2",rs.getString("ext2"));
				map.put("ext3",rs.getString("ext3"));
				map.put("ext4",rs.getString("ext4"));
				map.put("ext5",rs.getString("ext5"));
				
				rslist.add(map);
			}
			rs=null;
		}
		return rslist;
	}
	/**
	 * 职位
	 * @param st
	 * @return
	 * @throws SQLException
	 */
	private List getClass(Statement st) throws SQLException{
		List rslist=new ArrayList();
		HashMap map=new HashMap();
		map.put("class1id","0");
		map.put("class1name","申请人");
		map.put("levelid","0");
		map.put("enabledflag","Y");
		map.put("rem1","");
		rslist.add(map);
		
		String sql="select a.* " +
				"from class1 a " +
				"where a.enabledflag='Y' " +
				"order by a.levelid ";
		ResultSet rs=st.executeQuery(sql);
		while(rs.next()){
			map=new HashMap();
			map.put("class1id",rs.getString("class1id"));
			map.put("class1name",rs.getString("class1name"));
			map.put("levelid",rs.getString("levelid"));
			map.put("enabledflag",rs.getString("enabledflag"));
			map.put("rem1",rs.getString("rem1"));
						
			rslist.add(map);
		}
		rs=null;
		return rslist;
	}
//	private List getTarget_old(DBInterface dbi,String nodeid) throws SQLException{
//		List rslist=null;
//		if(nodeid!=null && nodeid.length()>0){
//			String sql="select a.* " +
//					"from JBPM_NodeExtTarget a " +
//					"where a.nodeid in("+nodeid+") " +
//					"order by a.nodeid ";
//			rslist=dbi.queryToList(sql);
//		}
//		return rslist;
//	}
	/**
	 * 取结点列表
	 * @param list
	 * @return
	 */
	private String getNodes(List list){
		if(list!=null && list.size()>0){
			HashMap map=null;
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<list.size();i++){
				map=(HashMap)list.get(i);
				sb.append(map.get("nodeid"));
				if(i<(list.size()-1)){
					sb.append(",");
				}
			}
			return sb.toString();
		}
		return null;
	}
	/**
	 * 返回页面上checkbox选择的值
	 * @param arg
	 * @param nid
	 * @return
	 */
	private Integer getSel(Integer[] arg,long nid){
		Integer rs=null;
		if(arg!=null && arg.length>0){
			for(int i=0;i<arg.length;i++){
				if(arg[i].longValue()==nid){
					rs=new Integer(1);
					break;
				}
			}
			if(rs==null){
				rs= new Integer(0);
			}
		}else{
			rs= new Integer(0);
		}
		return rs;
	}
	
	private Double getDouble(String dou){
		Double d=null;
		if(dou!=null && dou.length()>0){
			try{
				d=new Double(dou);
			}catch(Exception e){
				d=new Double(0);
			}
		}else{
			d=new Double(0);
		}
		return d;
	}
	
	private Integer getInteger(String in){
		Integer d=null;
		if(in!=null && in.length()>0){
			try{
				d=new Integer(in);
			}catch(Exception e){
				d=new Integer(0);
			}
		}else{
			d=new Integer(0);
		}
		return d;
	}
	
}