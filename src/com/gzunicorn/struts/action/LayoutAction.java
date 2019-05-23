//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.gzunicorn.struts.action;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;

import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;

import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.hibernate.viewmanager.Viewuserrolenode;

/**
 * MyEclipse Struts Creation date: 08-09-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/cityAction" name="cityForm" scope="request"
 *                validate="true"
 */
public class LayoutAction extends DispatchAction {

	Log log = LogFactory.getLog(LayoutAction.class);

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
		//SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "mainWin",null);
		/************结束用户权限过滤************/
		
		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			return null;
			
		}
		DebugUtil.printDoBaseAction("LayoutAction",name,"start");
		ActionForward forward= dispatchMethod(mapping, form, request, response, name);
		DebugUtil.printDoBaseAction("LayoutAction",name,"end");
		return forward;

	}
	/**
	 * to FilterTree
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toFilterTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List funnode=this.getFunctionNode2(form,request,2,"asc");
		List funnode4=this.getFunctionNode(form,request,4,"desc");
		String logonoutname = "";
		String logonoutid = "";
		String logonouturl = "";
		String modifypwname = "";
		String modifypwid = "";
		String modifypwurl = "";
		StringBuffer sb=new StringBuffer();
		StringBuffer icons=new StringBuffer();
		String taskURL="";
		String taskNodeID="";
		String taskName="";
		sb.append("\"id\":\"").append("N0").append("\",");
		sb.append("\"iconCls\":\"").append("icon-docs").append("\",");
		sb.append("\"text\":\"").append("XJSCRM").append("\",");
		sb.append("\"singleClickExpand\":").append("true").append(",");
		sb.append("\"pcount\":").append("0").append(",");
		
		icons.append("\"0\":\"icon-cls\"");
		if(funnode!=null && funnode.size()>0){
			sb.append("\"children\":[");
			Viewuserrolenode node=null;
			Viewuserrolenode nodeleaf=null;
			int flag=0,flag0=0;
			boolean isfirst=true,isleaf=true;
			
			for(int i=0;i<funnode.size();i++){
				
				node=(Viewuserrolenode)funnode.get(i);
				
				if (node.getNodeid().equals("98") || node.getNodeid().equals("99")) {// 更改密码或退出系统					
					if (node.getNodeid().equals("98")) {// 修改密码
						modifypwname = node.getNodename();
						modifypwid = "N" + node.getNodeid();
						modifypwurl = getFilterTreeURL(format("../layout/MainFrame.jsp", 1), format(node.getNodeurl(), 1), node.getNodeid());
					} else if (node.getNodeid().equals("99")) {//退出系统
						logonoutname = node.getNodename();
						logonoutid = "N" + node.getNodeid();
						logonouturl = format(node.getNodeurl(), 1);
					}
				} else {
					isleaf=true;
					if(isfirst){
						isfirst=false;
					}else{
						sb.append(",");
					}
					
					icons.append(",").append("\""+node.getNodeid()+"\":\"icon-cls\"");
					
					flag0=1;
					
					sb.append("{")
					.append("\"id\":\"").append("N"+node.getNodeid()).append("\",")//.append("\r\n")
					.append("\"iconCls\":\"").append("icon-pkg").append("\",")//.append("\r\n")
					.append("\"text\":\"").append(node.getNodename()).append("\",")//.append("\r\n")
					.append("\"singleClickExpand\":").append("true").append(",")//.append("\r\n")
					.append("\"pcount\":").append("0");//.append("\r\n");
					if(node.getNodedata()!=null && node.getNodedata().equalsIgnoreCase("myTaskOA")){
						taskURL=getFilterTreeURL(format("../layout/MainFrame.jsp",1),format(node.getNodeurl(),1),node.getNodeid());
						taskNodeID="N"+node.getNodeid();
						taskName=node.getNodename();
					}
					if(funnode4!=null && funnode4.size()>0){
						sb.append(",").append("\"children\":[");
						for(int j=funnode4.size()-1;j>=0;j--){
							nodeleaf=(Viewuserrolenode)funnode4.get(j);
							if(nodeleaf.getNodeid().startsWith(node.getNodeid())){
								isleaf=false;
								icons.append(",").append("\""+nodeleaf.getNodeid()+"\":\"icon-cmp\"");
								if(flag==1){
									sb.append(",");
								}
								sb.append("{")
								.append("\"href\":\"").append(getFilterTreeURL(format("../layout/MainFrame.jsp",1),format(nodeleaf.getNodeurl(),1),nodeleaf.getNodeid())).append("\",")//.append("\r\n")
								.append("\"text\":\"").append(nodeleaf.getNodename()).append("\",")
								.append("\"id\":\"").append("N"+nodeleaf.getNodeid()).append("\",")
								.append("\"isClass\":").append("true").append(",")
								.append("\"iconCls\":\"").append("icon-static").append("\",")
								.append("\"cls\":\"").append("cls").append("\",")
								.append("\"leaf\":").append("true")
								.append("}");
								
								flag=1;
								 
								funnode4.remove(j);
							}
							
						}
						sb.append("]");	
						flag=0;
					}
					if(isleaf){
						sb.append(",").append("\"href\":\"").append(getFilterTreeURL(format("../layout/MainFrame.jsp",1),format(node.getNodeurl(),1),node.getNodeid())).append("\",")
						.append("\"leaf\":").append("true");
					}
					sb.append("}");
				}
			}
				
			sb.append("]");	
			
		}

		request.setAttribute("ClassData",sb.toString());
		request.setAttribute("Icons",icons.toString());
		request.setAttribute("taskUrl", taskURL);
		request.setAttribute("taskNodeID", taskNodeID);
		request.setAttribute("taskName", taskName);
		
		// 获取服务器时间
		String tempServerDateTime = this.getServerDateTime();
		String year = tempServerDateTime.substring(0, 4) + "年";
		String month = Integer.parseInt(tempServerDateTime.substring(5, 7).toString()) + "月";
		String today = Integer.parseInt(tempServerDateTime.substring(8, 10).toString()) + "日";
		String week = tempServerDateTime.substring(10, tempServerDateTime.length());
		String returnServerDateTime = year + month + today + " " + week;

		request.setAttribute("returnServerDateTime", returnServerDateTime);

		request.setAttribute("ModifyPwName", modifypwname);
		request.setAttribute("ModifyPwID", modifypwid);
		request.setAttribute("ModifyPwUrl", modifypwurl);
		request.setAttribute("LogonOutName", logonoutname);
		request.setAttribute("LogonOutID", logonoutid);
		request.setAttribute("LogonOutUrl", logonouturl);
		
		ActionForward forward=mapping.findForward("tree");
		return forward;
	}
	
	
	public ActionForward toFilterTree_2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List funnode=this.getFunctionNode(form,request,2,"asc");
		List funnode4=this.getFunctionNode(form,request,4,"asc");
		StringBuffer sb=new StringBuffer();
		StringBuffer icons=new StringBuffer();
		
		sb.append("\"id\":\"").append("N0").append("\",");
		sb.append("\"iconCls\":\"").append("icon-docs").append("\",");
		sb.append("\"text\":\"").append("tree").append("\",");
		sb.append("\"singleClickExpand\":").append("true").append(",");
		sb.append("\"pcount\":").append("0").append(",");
		
		icons.append("\"0\":\"icon-cls\"");
		if(funnode!=null && funnode.size()>0){
			sb.append("\"children\":[");
			Viewuserrolenode node=null;
			Viewuserrolenode nodeleaf=null;
			int flag=0,flag0=0;
			boolean isfirst=true,isleaf=true;
			
			for(int i=0;i<funnode.size();i++){
				isleaf=true;
				if(isfirst){
					isfirst=false;
				}else{
					sb.append(",");
				}
				node=(Viewuserrolenode)funnode.get(i);
								
				icons.append(",").append("\""+node.getNodeid()+"\":\"icon-cls\"");
				
				flag0=1;
				
				sb.append("{")
				.append("\"id\":\"").append("N"+node.getNodeid()).append("\",")//.append("\r\n")
				.append("\"iconCls\":\"").append("icon-pkg").append("\",")//.append("\r\n")
				.append("\"text\":\"").append(node.getNodename()).append("\",")//.append("\r\n")
				.append("\"singleClickExpand\":").append("true").append(",")//.append("\r\n")
				.append("\"pcount\":").append("0");//.append("\r\n");
				
				if(funnode4!=null && funnode4.size()>0){
					sb.append(",").append("\"children\":[");
					for(int j=funnode4.size()-1;j>=0;j--){
						nodeleaf=(Viewuserrolenode)funnode4.get(j);
						if(nodeleaf.getNodeid().startsWith(node.getNodeid())){
							isleaf=false;
							icons.append(",").append("\""+nodeleaf.getNodeid()+"\":\"icon-cmp\"");
							if(flag==1){
								sb.append(",");
							}
							sb.append("{")
							.append("\"href\":\"").append(format(nodeleaf.getNodeurl(),1)).append("\",")//.append("\r\n")
							.append("\"text\":\"").append(nodeleaf.getNodename()).append("\",")//.append("\r\n")
							.append("\"id\":\"").append("N"+nodeleaf.getNodeid()).append("\",")//.append("\r\n")
							.append("\"isClass\":").append("true").append(",")//.append("\r\n")
							.append("\"iconCls\":\"").append("icon-static").append("\",")//.append("\r\n")
							.append("\"cls\":\"").append("cls").append("\",")//.append("\r\n")
							.append("\"leaf\":").append("true")
							.append("}");
							
							flag=1;
							
							funnode4.remove(j);
						}
					}
					sb.append("]");	
					flag=0;
				}
				if(isleaf){
					sb.append(",").append("\"href\":\"").append(format(node.getNodeurl(),1)).append("\",")
					.append("\"leaf\":").append("true");
				}
				sb.append("}");
//				if(i<funnode.size()-1){
//					icons.append(",");
//				}
			}
			
			sb.append("]");	
			
		}
//		//System.out.println(sb.toString());
//		//System.out.println(icons.toString());
		request.setAttribute("ClassData",sb.toString());
		request.setAttribute("Icons",icons.toString());

		ActionForward forward=mapping.findForward("tree");
		return forward;
	}
	
	public ActionForward toPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward=null;
		forward=mapping.findForward("toPortal");
		return forward;
	}
	
	/**
	 * main window left menu
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toMainLeftItemMenu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward=null;
		ViewLoginUserInfo userinfo=(ViewLoginUserInfo)request.getSession().getAttribute(SysConfig.LOGIN_USER_INFO);
		String moduleItem=request.getParameter("ModuleItem");
		moduleItem=moduleItem==null?"01":moduleItem;
		request.setAttribute("ModuleItem",moduleItem);
		Session hs=null;
		try{
			hs=HibernateUtil.getSession();
			String sql="from Viewuserrolenode a " +
					"where a.userid = :userid " +
					"and a.nodeid like :item " +
					"and len(a.nodeid)=4 " +
					"order by a.nodesort desc";
			Query query = hs.createQuery(sql);
			query.setString("item",moduleItem+"%");
			query.setString("userid",userinfo.getUserID());
			List list=query.list();
			
			if(list!=null && list.size()>0){
				int len=list.size();
				for(int i=0;i<len;i++){
					Viewuserrolenode node=(Viewuserrolenode)list.get(i);
					//node.setNodeURL("/"+SysConfig.WEB_APPNAME+node.getNodeURL().substring(2,node.getNodeURL().length()));
					node.setNodeurl(CommonUtil.FormatUrl(node.getNodeurl()));
				}
			}
			
			request.setAttribute("LeftMenu",list);
		}catch(HibernateException e){
			DebugUtil.print(e);
		} catch (DataStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
			if(hs!=null){hs.close();}
			}catch(HibernateException e){
				DebugUtil.print(e);
			}
		}
		forward=mapping.findForward("mainleftitemmenu");
		return forward;
	}

	private String format(String arg,int ftype){
		String rs=null;
		switch (ftype){
		case 1:
			rs=CommonUtil.FormatUrl(arg);
		}
		return rs;
	}
	private String getFilterTreeURL(String baseurl,String jumpurl,String nodeid){
			try {
				baseurl+="?NodeId="+nodeid+"&JumpURL="+CommonUtil.getUrlEncode(jumpurl,null);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
		return baseurl;
	}
	private List getFunctionNode(ActionForm form,HttpServletRequest request,int level,String isASC){
		
		ViewLoginUserInfo userinfo=(ViewLoginUserInfo)request.getSession().getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs=null;
		List list=null;
		try{
			hs=HibernateUtil.getSession();
			String sql="Select a from Viewuserrolenode a,Modulenode b " +
					"where a.userid = :userid " +
					"and len(a.nodeid)=:level " +
					"and a.nodeid	=	b.nodeid ";
			
			sql+="order by a.nodesort "+isASC+",a.nodeid desc";
			Query query = hs.createQuery(sql);
			query.setString("userid",userinfo.getUserID());
			query.setInteger("level",level);
			
			list=query.list();
		}catch(Exception e){
			DebugUtil.print(e);
		}finally{
			if(hs!=null){
				try {
					hs.close();
				} catch (HibernateException e) {
					DebugUtil.print(e);
				}
			}
		}
		return list;
	}
private List getFunctionNode2(ActionForm form,HttpServletRequest request,int level,String isASC){
		
		ViewLoginUserInfo userinfo=(ViewLoginUserInfo)request.getSession().getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs=null;
		List list=null;
		try{
			hs=HibernateUtil.getSession();
			String sql="Select a from Viewuserrolenode a,Modulenode b " +
					"where a.userid = :userid " +
					"and len(a.nodeid)=:level " +
					"and a.nodeid	=	b.nodeid ";
			
			sql+="order by a.nodeid "+isASC;
			Query query = hs.createQuery(sql);
			query.setString("userid",userinfo.getUserID());
			query.setInteger("level",level);
			
			list=query.list();
		}catch(Exception e){
			DebugUtil.print(e);
		}finally{
			if(hs!=null){
				try {
					hs.close();
				} catch (HibernateException e) {
					DebugUtil.print(e);
				}
			}
		}
		return list;
	}
	/**
	 * 获取当前服务器的时间 格式: 年 月 日 星期几
	 * 
	 * @return
	 */
	public String getServerDateTime() {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd EEEE ");
		Date date = new Date();
		String strToday = bartDateFormat.format(date).toString();
		return strToday;
	}
}