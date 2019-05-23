//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.gzunicorn.struts.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.hibernate.Session;

import com.gzunicorn.common.dao.DBInterface;
import com.gzunicorn.common.dao.ObjectAchieveFactory;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

/**
 * MyEclipse Struts Creation date: 07-19-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/roleAction" name="roleForm" scope="request"
 *                validate="true"
 */
public class ViewProcessAction extends DispatchAction {
	Log log = LogFactory.getLog(ViewProcessAction.class);

	BaseDataImpl bd = new BaseDataImpl();

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
//		SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "ViewProcess",null);
		/************结束用户权限过滤************/

		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toViewProcess";
		}
		DebugUtil.printDoBaseAction("ViewProcessAction",name,"start");
		ActionForward forward= dispatchMethod(mapping, form, request, response, name);
		DebugUtil.printDoBaseAction("ViewProcessAction",name,"end");
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
	 * Method toSearchRecord execute, Search record
	 * 查询页面，显示数据，可根据条件进行相应的查询 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toViewProcess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward = null;
		
		request.setAttribute("ViewPower","Y");

		
		String tokenid=request.getParameter("tokenid");
		Session hs = null;
		if(tokenid!=null && !tokenid.trim().equals("")){
			ViewLoginUserInfo userinfo=(ViewLoginUserInfo)request.getSession().getAttribute(SysConfig.LOGIN_USER_INFO);
			Connection con=null;
			try{
				
				String flowname=request.getParameter("flowname");
				if(flowname!=null && !flowname.trim().equals("")){
					flowname=new String(flowname.getBytes("ISO-8859-1"),"gbk");
				}
				request.setAttribute("FlowName",flowname);
				
				hs = HibernateUtil.getSession();
				//con=this.getDataSource(request,SysConfig.DataSource).getConnection();
				con=hs.connection();
				
				con.setAutoCommit(true);
				DBInterface dbi=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp) ;
				dbi.setCon(con);
				String sql="Sp_ViewProcessHandler '"+userinfo.getUserID()+"',"+tokenid;
				
				List list=dbi.queryToList(sql);
				List rslist=null;
				List li0=null,li1=null;
				HashMap map=null,map2=null;
				String taskid="",ntaskid="",taskflag="",taskname="";
				if(list!=null && list.size()>0){
					rslist=new ArrayList();
					for(int i=0;i<list.size();i++){
						map2=(HashMap)list.get(i);
						if(i==0){
							taskid=(String)map2.get("taskid");
							taskname=(String)map2.get("taskname");
							li0=new ArrayList();
							li1=new ArrayList();
							map=new HashMap();
						}
						ntaskid=(String)map2.get("taskid");
						taskflag=(String)map2.get("flag");
						if(taskid.equalsIgnoreCase(ntaskid)){
							if(taskflag.equalsIgnoreCase("0")){
								li0.add(map2);
							}else{
								li1.add(map2);
							}
						}else{
							map.put("taskname",taskname);
							map.put("li0",li0);
							map.put("li1",li1);
							rslist.add(map);
							
							taskid=ntaskid;
							taskname=(String)map2.get("taskname");
							
							li0=new ArrayList();
							li1=new ArrayList();
							map=new HashMap();
							
							if(taskflag.equalsIgnoreCase("0")){
								li0.add(map2);
							}else{
								li1.add(map2);
							}
						}
						
						if(i==(list.size()-1)){
							map.put("taskname",taskname);
							map.put("li0",li0);
							map.put("li1",li1);
							rslist.add(map);
						}
					}
				}
				request.setAttribute("ActorList",rslist);
			}catch(Exception e){
				DebugUtil.print(e);
			}finally{
				if(con!=null){
					try {
						con.setAutoCommit(false);
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
		forward=mapping.findForward("toView");
		return forward;
	}
}