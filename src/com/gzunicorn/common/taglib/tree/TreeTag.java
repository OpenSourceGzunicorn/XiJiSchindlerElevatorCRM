/*
 * Created on 2005-8-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.taglib.tree;

/**
 * Created on 2005-7-12
 * <p>
 * Title: 角色模块权限
 * </p>
 * <p>
 * Description: 角色模块权限设置类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * <p>
 * Company:友联科技
 * </p>
 * 
 * @author wyj
 * @version 1.0
 */


import com.gzunicorn.bean.RoleNodeBean;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.basedata.Class1;
import com.gzunicorn.hibernate.sysmanager.Storageid;
//import com.gzunicorn.hibernate.Docmanagertree;
//import com.gzunicorn.hibernate.Docmanagertreepurview;
import com.gzunicorn.hibernate.sysmanager.Module;
import com.gzunicorn.hibernate.sysmanager.Role;
//import com.gzunicorn.hibernate.Userdeptset;
import com.gzunicorn.hibernate.viewmanager.ViewClassUser;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.hibernate.viewmanager.ViewUserDept;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import javax.sql.DataSource;

import org.hibernate.*;

import com.gzunicorn.common.util.*;

public class TreeTag extends TagSupport {
    private String userid;
    private String url1;//跳转url
    private String type1;//不同树
    private String param1;//参数
    private String param2;//参数
    private String param3;//参数
    
    private String folder="/"+SysConfig.WEB_APPNAME+"/common/images/tree/folder.gif";
    private String folderopen="/"+SysConfig.WEB_APPNAME+"/common/images/tree/folderopen.gif";
    private String doc_folder="/"+SysConfig.WEB_APPNAME+"/common/images/tree/doc_folder.gif";
    
  	public TreeTag() {
    }

    public int doStartTag() {
    	JspWriter out = this.pageContext.getOut();
        try {
        	String restr="";
        	//根据不同的类型取不同的构造树
//        	if(type1.equalsIgnoreCase("DeptUser")){
//        		restr=getDeptTreeShowHtml();
//        	}
//        	if(type1.equalsIgnoreCase("Organize")){
//        		restr=getDeptTree();//组织架构树
//        	}
//        	if(type1.equalsIgnoreCase("DataRole")){
//        		restr=DataRoleTree();//数据角色设置
//        	}
//        	if(type1.equalsIgnoreCase("DocTree")){
//        		restr=DocManagerTree();//文档管理
//        	}
//        	if(type1.equalsIgnoreCase("DocSelTree")){
//        		restr=DocManagerSelTree();//指定查询权限的文档树
//        	}
        	if(type1.equalsIgnoreCase("SelDeptUser")){
        		restr=DeptUserTree();//选择部门用户树
        	}
        	if(type1.equalsIgnoreCase("SelClassUser")){
        		restr=ClassUserTree();//选择群组用户树
        	}
//        	if(type1.equalsIgnoreCase("WorkLog")){
//        		restr=getLogDeptTree();//组织架构树
//        	}
        	if(restr.equals("0")){
        		return 0;
        	}
			out.println(restr);
		} catch (IOException e) {
			DebugUtil.print(e);
		} catch (Exception e) {
			DebugUtil.print(e);
		}
        return 1;
    }

    public int doEndTag() {
        return 6;
    }
	
	/**
	 * 部门和人
	 * @param list1　部门
	 * @param list2　所属部门的人
	 * @param url
	 * @return
	 * @throws TreeShowException
	 
    private String getDeptTreeShowHtml()
            throws TreeException{
    	

        PageContext pageContext = this.pageContext;
        HttpSession httpSession = pageContext.getSession();
        ViewLoginUserInfo userInfo = (ViewLoginUserInfo) httpSession
                .getAttribute(SysConfig.LOGIN_USER_INFO);
        List list1 = null;
        List list2 = null;
        Session session = null;
        if (userInfo == null)
            return "0";
        try {
            session = HibernateUtil.getSession();
            String sql1 = "";
            String sql2 = "";
            if (userInfo.getRoleID().equals("A0")) {
                sql1="FROM Dept where enabledflag='Y'";
                sql2="Select a,b " +
                	"FRom Userdeptset AS a ,ViewLoginUserInfo as b " +
                	"WHERE a.flag='W' and a.id.userid=b.userID ";
                            
                Query query = session.createQuery(sql1);
                Query query2 = session.createQuery(sql2);
                list1 = query.list();
                list2 = query2.list();
                
            } else {
                DebugUtil.println("当前用户不是管理员用户，不能使用此功能！");
                return "当前用户不是管理员用户，不能使用此功能！";
            }
        } catch (Exception e) {
            DebugUtil.print(e);
        } finally {
            try {
                session.close();
            } catch (HibernateException hex) {
                DebugUtil.print(hex);
            }
        }
    	
    	
    	
        StringBuffer treeShowHtml = new StringBuffer();       
        treeShowHtml.append("d = new dTree('d');\n");
        treeShowHtml.append(SysConfig.TREE_ROOT_NODE);
        
        String URL = url1;//"../infoNodeAction.do?method=toRightFrm";//url
        
        int treeid=1;
        int deptid;
        if(list1!=null && list1.size()>0){
	        for (int i = 0; i < list1.size(); i++) {
	        	Storageid dept = (Storageid) list1.get(i);
	        	//将部门加入树
	            treeShowHtml.append("d.add(").append(treeid).append(",")
	            			.append("0").append(",")
	            			.append("'").append(dept.getStoragename()).append("',")
	            			.append("'").append(SysConfig.NULL_URL).append("',")
	            			.append("'").append(dept.getStoragename()).append("',")
	            			.append("'").append("',")//target
	            			.append("'").append(folder).append("',")//icon
	            			.append("'").append(folderopen)
	            			//.append("',")//iconOpen
	            			//.append("'").append(folderopen)
	            			.append("');\n");//open
	  
	            deptid=treeid;
	            treeid++;
	            if(list2!=null && list2.size()>0){
		            for(int j=(list2.size()-1);j>=0;j--){
		            	Object[] obj=(Object[])list2.get(j);
		            	Userdeptset a=(Userdeptset)obj[0];
		            	ViewLoginUserInfo b=(ViewLoginUserInfo)obj[1];
		            	
		            	if(a.getId().getDeptid().equalsIgnoreCase(dept.getDeptid())){            		
		            	    //将所属部门的人加入树
		            		treeShowHtml.append("d.add(").append(treeid).append(",")
		        			.append(deptid).append(",")
		        			.append("'").append(b.getUserID()+"("+b.getUserName()+")").append("',")
		        			.append("'").append(URL).append("&userid=").append(b.getUserID()).append("',")
		        			.append("'").append(b.getUserName()).append("',")
		        			.append("'").append("rightFrm").append("',")//target
		        			.append("'").append(doc_folder).append("',")//icon
		        			.append("'").append(doc_folder)
		        			//.append("',")//iconOpen
		        			//.append("'").append(doc_folder)
		        			.append("');\n");//open	
		            	    
		            		list2.remove(j);
		            		treeid++;
		            	}
		            }
	            }
        }
        }
        return treeShowHtml.toString();
    }
    */

    /**
     * 适合工作日志的部门
     * 
	 * @param list1　部门
	 * @param list2　所属部门的人
	 * @param url
	 * @return
	 * @throws TreeShowException
     
    private String getLogDeptTree()throws TreeException{

    	

        PageContext pageContext = this.pageContext;
        HttpSession httpSession = pageContext.getSession();
        ViewLoginUserInfo userInfo = (ViewLoginUserInfo) httpSession
                .getAttribute(SysConfig.LOGIN_USER_INFO);
        List list1 = null,list2=null;
        Session session = null;
        if (userInfo == null)
            return "0";
        try {
            session = HibernateUtil.getSession();
            String sql1 = "";
          
        	sql1="select distinct d FROM Dept d,Viewuserrefuserread ref where d.enabledflag='Y' and d.flag in "+SysConfig.WORK_LOG_DEPT_FLAG_IN+" and d.depttype in "+SysConfig.WORK_LOG_DEPT_DEPTTYPE_IN+" and ref.id.refdeptid = d.deptid and ref.id.userid='"+userInfo.getUserID()+"' and d.deptid not in"+SysConfig.WORK_LOG_DEPT_DEPTID_NOT_IN+" order by layer ";//排序不可省，是后面树的算法的基础
            //System.out.println(sql1);
        	Query query = session.createQuery(sql1);
            list1 = query.list();
            
            if(list1!=null && list1.size()>0){
            	list2=new ArrayList();
            	Dept dept=null;
            	HashMap node=null;
            	//转换成tree
            	for(int i=0;i<list1.size();i++){
            		dept=(Dept)list1.get(i);

                	node=new HashMap();     	                    	
                	//node.put("nodeid",dept.getDeptid());
                	node.put("nodeid",dept.getLayer());
                	node.put("nodeObj",dept);
                	list2.add(node);

            	}
            }
                
 
        } catch (Exception e) {
            DebugUtil.print(e);
        } finally {
            try {
                session.close();
            } catch (HibernateException hex) {
                DebugUtil.print(hex);
            }
        }
    	
    	
    	
        StringBuffer treeShowHtml = new StringBuffer();       
        treeShowHtml.append("d = new dTree('d');\n");
        treeShowHtml.append(SysConfig.TREE_WORKLOG_ROOT_NODE);
        
        String URL = url1;//"../infoNodeAction.do?method=toRightFrm";//url
        //String icon=doc_folder;
       
        List list=buildTree(list2,".",1,0);
        HashMap node;
        Dept dept=null;
        if(list!=null && list.size()>0){
	         for (int i = 0; i < list.size(); i++) {
	        	 node = (HashMap) list.get(i);
	        	 dept=(Dept)node.get("nodeObj"); 
	             // 因节点代码只能是数字，不能含有因为字母roleNodeBean.getRoleID()
	             treeShowHtml.append("d.add(").append(node.get("id")).append(",")
	             			.append(node.get("pid")).append(",")
	             			.append("'").append(dept.getDeptname()).append("',")
	             			//.append("'").append(URL).append("&deptid=").append(dept.getDeptid()).append("',")
	             			.append("'").append("javascript:searchLog(\\'"+dept.getDeptid()+"\\')").append("',")
	             			.append("'").append(dept.getDeptname()).append("',")
	             			.append("'").append("rightFrm").append("',")
	             			.append("'").append("").append("',")//icon
	            			
	             			.append("'").append(folder).append("',")//icon
	             			.append("'").append(folderopen).append("',")
	            			.append("'")
	            			.append("');\n");//open

	         }
        }
        return treeShowHtml.toString();
    
    }
    */
    
	/**
	 * 将部门树变成dtree
	 * @param list1　部门
	 * @param list2　所属部门的人
	 * @param url
	 * @return
	 * @throws TreeShowException
	 
    private String getDeptTree()throws TreeException{
    	

        PageContext pageContext = this.pageContext;
        HttpSession httpSession = pageContext.getSession();
        ViewLoginUserInfo userInfo = (ViewLoginUserInfo) httpSession
                .getAttribute(SysConfig.LOGIN_USER_INFO);
        List list1 = null,list2=null;
        Session session = null;
        if (userInfo == null)
            return "0";
        try {
            session = HibernateUtil.getSession();
            String sql1 = "";
            if (userInfo.getRoleID().equals("A0")) {
                //sql1="FROM Dept where enabledflag='Y' order by deptid ";//排序不可省，是后面树的算法的基础
            	sql1="FROM Dept where enabledflag='Y' order by layer ";//排序不可省，是后面树的算法的基础
                Query query = session.createQuery(sql1);
                list1 = query.list();
                
                if(list1!=null && list1.size()>0){
                	list2=new ArrayList();
                	Dept dept=null;
                	HashMap node=null;
                	//转换成tree
                	for(int i=0;i<list1.size();i++){
                		dept=(Dept)list1.get(i);

                    	node=new HashMap();     	                    	
                    	//node.put("nodeid",dept.getDeptid());
                    	node.put("nodeid",dept.getLayer());
                    	node.put("nodeObj",dept);
                    	list2.add(node);

                	}
                }
                
            } else {
                DebugUtil.println("当前用户不是管理员用户，不能使用此功能！");
                return "当前用户不是管理员用户，不能使用此功能！";
            }
        } catch (Exception e) {
            DebugUtil.print(e);
        } finally {
            try {
                session.close();
            } catch (HibernateException hex) {
                DebugUtil.print(hex);
            }
        }
    	
    	
    	
        StringBuffer treeShowHtml = new StringBuffer();       
        treeShowHtml.append("d = new dTree('d');\n");
        treeShowHtml.append(SysConfig.TREE_ROOT_NODE);
        
        String URL = url1;//"../infoNodeAction.do?method=toRightFrm";//url
        //String icon=doc_folder;
       
        List list=buildTree(list2,".",1,0);
        HashMap node;
        Dept dept=null;
        if(list!=null && list.size()>0){
	         for (int i = 0; i < list.size(); i++) {
	        	 node = (HashMap) list.get(i);
	        	 dept=(Dept)node.get("nodeObj"); 
	             // 因节点代码只能是数字，不能含有因为字母roleNodeBean.getRoleID()
	             treeShowHtml.append("d.add(").append(node.get("id")).append(",")
	             			.append(node.get("pid")).append(",")
	             			.append("'").append(dept.getDeptname()).append("',")
	             			.append("'").append(URL).append("&deptid=").append(dept.getDeptid()).append("',")
	             			.append("'").append(dept.getDeptname()).append("',")
	             			.append("'").append("rightFrm").append("',")
	             			.append("'").append(folder).append("',")//icon
	            			.append("'").append(folderopen)
	            			.append("');\n");//open

	         }
        }
        return treeShowHtml.toString();
    }
    */
    
    /**
     * 建立部门树
     * @param list
     * @param split
     * @param url
     * @param target
     * @param icon
     * @param iconOpen
     * @param open
     * @return
    
//    private List buildDeptTree_(List list,String split,String url,String target,String icon,String iconOpen,String open){
//    	int preid=0;
//    	int curid=1;
//    	List rslist=new ArrayList();
//    	//function Node(id, pid, name, url, title, target, icon, iconOpen, open) {
//    	HashMap node;
//    	Hashtable ht=new Hashtable();
//    	split=split==null||split.trim().length()==0 ?".":split;
//    	if(list!=null && list.size()>0){
//	    	for (int i = 0; i < list.size(); i++) {
//	        	Dept dept = (Dept) list.get(i);
//	        	String deptid=dept.getDeptid();
//	        	String[] dpid=CommonUtil.getFliter(deptid,split);
//	        	int len=dpid!=null?dpid.length:0;
//	        	String curpix="";
//	        	for(int j=0;j<len;j++){
//	        		if(j>0){curpix+=split;}
//	        		curpix+=dpid[j];
//	        		if(ht.containsKey(curpix)){
//	        			preid=Integer.parseInt(ht.get(curpix).toString());
//	        		}else{
//	        			if(j<(len-1)){//未完整
//	        				ht.put(curpix,curid+"");
//	        				preid=curid;
//	        				curid++;
//	        			}else{//完整
//	        				node=new HashMap();
//	    		        	node.put("deptid",dept.getDeptid());
//	    		        	node.put("id",curid+"");
//	    		        	node.put("pid",preid+"");
//	    		        	node.put("name",dept.getDeptname());
//	    		        	node.put("url",url);
//	    		        	node.put("title",dept.getDeptname());
//	    		        	node.put("target",target);
//	    		        	node.put("icon",icon);
//	    		        	node.put("iconOpen",iconOpen);
//	    		        	node.put("open",open);
//	    		        	
//	    		        	rslist.add(node);
//	    		        	
//	    		        	ht.put(curpix,curid+"");
//	        				preid=curid;
//	    		        	curid++;
//	        			}
//	        		}
//	        	}         
//	        }
//    	}
//    	return rslist;
//    }
 */
    /**
     * 数据角色
     * @return
     
    public String DataRoleTree(){

        PageContext pageContext = this.pageContext;
        HttpSession httpSession = pageContext.getSession();   
        ViewLoginUserInfo userInfo = (ViewLoginUserInfo) httpSession
                .getAttribute(SysConfig.LOGIN_USER_INFO);
        ArrayList arrayList = null;
        Session session = null;
        if (userInfo == null)
            return "0";
        try {
            session = HibernateUtil.getSession();
            StringBuffer HSQL = new StringBuffer("");
            if (userInfo.getRoleID().equals("A0")) {
                HSQL.append("SELECT NEW com.gzunicorn.bean.RoleNodeBean(b.dataroleid,b.datarolename,b.moduleid,a.modulename) "
                                + "FROM Module AS a,Datarole AS b WHERE b.enabledflag='Y' and a.moduleid = b.moduleid");

            } else {
                DebugUtil.println("当前用户不是管理员用户，不能使用此功能！");
            }
            Query query = session.createQuery(HSQL.toString());
            arrayList = (ArrayList) query.list();

            
        } catch (Exception e) {
            DebugUtil.print(e);
        } finally {
            try {
                session.close();
            } catch (HibernateException hex) {
                DebugUtil.print(hex);
            }
        }
        
        
        
        StringBuffer treeShowHtml = new StringBuffer();
        
        treeShowHtml.append("d = new dTree('d');\n");
        //treeShowHtml.append(SysConfig.TREE_ROOT_NODE);
        treeShowHtml.append("d.add(-2,-1,'"+SysConfig.WEB_APPNAME+"',null,'"+SysConfig.WEB_APPNAME+"','main');\n");
        treeShowHtml=this.RootNode(treeShowHtml,1,-2,"数据角色",SysConfig.NULL_URL,"数据角色","",folder,folderopen,folderopen);
        String URLTemp = url1;
        StringBuffer URL = null;
        
	         if(arrayList!=null && arrayList.size()>0){
		         for (int i = 0; i < arrayList.size(); i++) {
		             URL = new StringBuffer(URLTemp);
		             RoleNodeBean roleNodeBean = (RoleNodeBean) arrayList.get(i);
		             if (roleNodeBean.getRoleID() != null
		                     && roleNodeBean.getRoleID().length() > 0)
		
		                 URL.append("&").append("roleID=").append(
		                         roleNodeBean.getRoleID()).append("&moduleID=").append(
		                         roleNodeBean.getModuleID());
		
		             // 因节点代码只能是数字，不能含有因为字母roleNodeBean.getRoleID()
		             treeShowHtml.append("d.add(").append(i + 100)
		             			.append(",").append(1)
		             			.append(",'").append(roleNodeBean.getRoleName())
		             			.append("','").append(URL.toString())
		             			.append("','").append(roleNodeBean.getRoleName())
		             			.append("','").append("rightFrm").append("',")
		             			.append("'").append(doc_folder).append("',")//icon
		            			.append("'").append(doc_folder)
		            			//.append("',")//iconOpen
		            			//.append("'").append(doc_folder)
		            			.append("');\n");//open
		         }
	         }
       
        ////System.out.println(treeShowHtml.toString());
        return treeShowHtml.toString();
        
        
        
   }
    */
  /**
   * 文档管理树
   * @return
   
    public String DocManagerTree(){
    	PageContext pageContext = this.pageContext;
        HttpSession httpSession = pageContext.getSession();
        
        ViewLoginUserInfo userInfo = (ViewLoginUserInfo) httpSession
                .getAttribute(SysConfig.LOGIN_USER_INFO);
        
        DataSource ds=null;
        Connection con=null;
        ResultSet rs=null;
        
        HashMap nodemap;
    	HashMap node;
        //PreparedStatment pst=null;
        List list = new ArrayList();
        if (userInfo == null)
            return "0";
        try {
               	ds=(DataSource)pageContext.getServletContext().getAttribute(SysConfig.TAGLIB_GET_DATA_SOURCE_KEY);
               	con=ds.getConnection();
               	//初始化文档管理结点
               	this.InitDocManager(con);
               	//取文档管理结点
               	String sql="sp_DocTreeToUser '"+userInfo.getUserID()+"'";
            	rs=con.prepareStatement(sql).executeQuery();
            	
//            	CallableStatement call = con.prepareCall("{Call sp_DocTreeToUser(?)}");
//
//                call.setString(1, userInfo.getUserID());
//                rs = call.executeQuery();
//            	
            	//转换成tree
            	while(rs.next()){
            		nodemap=new HashMap();
            		node=new HashMap();
            		//node.put("nodeid",rs.getString("DMTId"));
            		node.put("userid",rs.getString("UserID"));
            		node.put("dmtid",rs.getString("DMTId"));
            		node.put("dmtname",rs.getString("DMTName"));
            		node.put("flag",rs.getString("Allow"));
            		
            		nodemap.put("nodeid",rs.getString("DMTId"));
            		nodemap.put("nodeObj",node);
            		list.add(nodemap);
            	}
            	
            
        } catch (SQLException e) {
            DebugUtil.print(e);
        } finally {
           try{
        	   if(rs!=null){rs.close();}
        	   if(con!=null){con.close();}
           }catch(SQLException e){
        	   DebugUtil.print(e);
           }
        }
        
        StringBuffer treeShowHtml = new StringBuffer();
        
        treeShowHtml.append("d = new dTree('d');\n");
        treeShowHtml.append(SysConfig.TREE_ROOT_NODE);
        String URL = url1;
//        String rootUrl=URL+"&nodeid=root";
        //treeShowHtml=this.RootNode(treeShowHtml,1,0,"文档管理",rootUrl,"文档管理","rightFrame",folder,folderopen,folderopen);
        if(list!=null && list.size()>0){
        	
        	list=this.buildTree(list,".",1,0);
        	
        	HashMap doctree;
        	String icon="";
        	String iconopen="";
        	String flag="";
	         if(list!=null && list.size()>0){
		         for (int i = 0; i < list.size(); i++) {
		             nodemap = (HashMap) list.get(i);
		             doctree=(HashMap)nodemap.get("nodeObj");
		             flag=doctree.get("flag").toString();
		             if(flag.equalsIgnoreCase(SysConfig.DOC_TREE_PURVIEW_ADMIN)){
		            	 icon=SysConfig.DOC_TREE_ICON_ADMIN;
		            	 iconopen=SysConfig.DOC_TREE_ICON_ADMIN_OPEN;
		             }else if(flag.equalsIgnoreCase(SysConfig.DOC_TREE_PURVIEW_WRITER)){
		            	 icon=SysConfig.DOC_TREE_ICON_WRITER;
		            	 iconopen=SysConfig.DOC_TREE_ICON_WRITER_OPEN;
		             }else if(flag.equalsIgnoreCase(SysConfig.DOC_TREE_PURVIEW_READER)){
		            	 icon=SysConfig.DOC_TREE_ICON_READER;
		            	 iconopen=SysConfig.DOC_TREE_ICON_READER_OPEN;
		             }else if(flag.equalsIgnoreCase(SysConfig.DOC_TREE_PURVIEW_NOTHING)){
		            	 icon=SysConfig.DOC_TREE_ICON_NOTHING;
		            	 iconopen=SysConfig.DOC_TREE_ICON_NOTHING_OPEN;
		             }   
		             // 因节点代码只能是数字，不能含有因为字母roleNodeBean.getRoleID()
		             treeShowHtml.append("d.add(").append(nodemap.get("id")).append(",")
		             			.append(nodemap.get("pid")).append(",")
		             			.append("'").append(doctree.get("dmtname")).append("',")
		             			.append("'").append(URL).append("&nodeid=").append(doctree.get("dmtid")).append("',")
		             			.append("'").append(doctree.get("dmtname")).append("',")
		             			.append("'").append("rightFrm").append("',")
		             			.append("'").append(icon).append("',")//icon
		            			.append("'").append(iconopen)
		            			//.append("',")//iconOpen
		            			//.append("'").append(iconopen)
		            			.append("');\n");//open

	         }
	         }
        }
        ////System.out.println(treeShowHtml.toString());
        return treeShowHtml.toString();
    	
    }
    */
    
    /**
     * 判断是否文档管理是否有根结点，若没有则插入根结点
     * @param con
     * @throws SQLException
     */
    private void InitDocManager(Connection con){
    	String sql="select count(*) as num from docmanagertree where dmtid='D001'";
    	ResultSet rs=null;
    	try{
	    	rs=con.prepareStatement(sql).executeQuery();
	    	if(rs.next()){
	    		int count=rs.getInt("num");
	    		if(count==0){
	    			//文档管理初始化结点
	    			con.setAutoCommit(false);
	    			sql="insert into docmanagertree select 'D001','文档管理','D001','admin','2006-10-01','00:00:00','','admin','2006-10-01','00:00:00',1000";
	    			con.prepareStatement(sql).executeUpdate();
	    			con.commit();
	    		}
	    	}
    	}catch(SQLException e){
    		DebugUtil.print(e);
    		try {
				con.rollback();
			} catch (SQLException e1) {
				DebugUtil.print(e1);
			}
    	}finally{
    		try{
    			if(rs!=null){rs.close();}
    		}catch(SQLException e){
    			DebugUtil.print(e);
    		}
    	}
    }
    /**
     * 文档管理移动文档时，选择目标目录树
     * @return
     
    public String DocManagerSelTree(){
    	PageContext pageContext = this.pageContext;
        HttpSession httpSession = pageContext.getSession();
        
        ViewLoginUserInfo userInfo = (ViewLoginUserInfo) httpSession
                .getAttribute(SysConfig.LOGIN_USER_INFO);
        
        DataSource ds=null;
        Connection con=null;
        ResultSet rs=null;
        
        HashMap nodemap;
    	HashMap node;
        //PreparedStatment pst=null;
        List list = new ArrayList();
        if (userInfo == null)
            return "0";
        try {
               	ds=(DataSource)pageContext.getServletContext().getAttribute(SysConfig.TAGLIB_GET_DATA_SOURCE_KEY);
               	con=ds.getConnection();
               	//初始化文档管理结点
               	this.InitDocManager(con);
               	//取文档管理结点
               	//String sql="SP_DocUserAppointPurviewTree '"+userInfo.getUserID()+"','''"+SysConfig.DOC_TREE_PURVIEW_ADMIN+"'',''"+SysConfig.DOC_TREE_PURVIEW_WRITER+"'''";
               	String sql="SP_DocUserAppointPurviewTree '"+userInfo.getUserID()+"','"+this.param1+"','"+this.param2+"'";
            	rs=con.prepareStatement(sql).executeQuery();
            	
            	//转换成tree
            	while(rs.next()){
            		nodemap=new HashMap();
            		node=new HashMap();
            		//node.put("nodeid",rs.getString("DMTId"));
            		
            		node.put("dmtid",rs.getString("DMTId"));
            		node.put("dmtname",rs.getString("DMTName"));
            		node.put("flag",rs.getString("flag"));
            		
            		nodemap.put("nodeid",rs.getString("DMTId"));
            		nodemap.put("nodeObj",node);
            		list.add(nodemap);
            	}
            	
            
        } catch (SQLException e) {
            DebugUtil.print(e);
        } finally {
           try{
        	   if(rs!=null){rs.close();}
        	   if(con!=null){con.close();}
           }catch(SQLException e){
        	   DebugUtil.print(e);
           }
        }
        
        StringBuffer treeShowHtml = new StringBuffer();
        
        treeShowHtml.append("d = new dTree('d');\n");
        treeShowHtml.append(SysConfig.TREE_ROOT_NODE);
        String URL = url1;
//        String rootUrl=URL+"&nodeid=root";
        //treeShowHtml=this.RootNode(treeShowHtml,1,0,"文档管理",rootUrl,"文档管理","rightFrame",folder,folderopen,folderopen);
        if(list!=null && list.size()>0){
        	
        	list=this.buildTree(list,".",1,0);
        	
        	HashMap doctree;
        	String icon="";
        	String iconopen="";
        	String dmtid="";
        	String dmtname="";
        	String flag="";
        	String label="";//
	         if(list!=null && list.size()>0){
		         for (int i = 0; i < list.size(); i++) {
		             nodemap = (HashMap) list.get(i);
		             doctree=(HashMap)nodemap.get("nodeObj");
		             dmtid=doctree.get("dmtid").toString();
		             dmtname=doctree.get("dmtname").toString();
		             flag=doctree.get("flag").toString();
		             
		             if(flag.equalsIgnoreCase(SysConfig.DOC_TREE_PURVIEW_ADMIN)){
		            	 icon=SysConfig.DOC_TREE_ICON_ADMIN;
		            	 iconopen=SysConfig.DOC_TREE_ICON_ADMIN_OPEN;
		             }else if(flag.equalsIgnoreCase(SysConfig.DOC_TREE_PURVIEW_WRITER)){
		            	 icon=SysConfig.DOC_TREE_ICON_WRITER;
		            	 iconopen=SysConfig.DOC_TREE_ICON_WRITER_OPEN;
		             }else if(flag.equalsIgnoreCase(SysConfig.DOC_TREE_PURVIEW_READER)){
		            	 icon=SysConfig.DOC_TREE_ICON_READER;
		            	 iconopen=SysConfig.DOC_TREE_ICON_READER_OPEN;
		             }else if(flag.equalsIgnoreCase(SysConfig.DOC_TREE_PURVIEW_NOTHING)){
		            	 icon=SysConfig.DOC_TREE_ICON_NOTHING;
		            	 iconopen=SysConfig.DOC_TREE_ICON_NOTHING_OPEN;
		             }   
		             
		             if(flag.equalsIgnoreCase(SysConfig.DOC_TREE_PURVIEW_ADMIN) || flag.equalsIgnoreCase(SysConfig.DOC_TREE_PURVIEW_WRITER)){
		            	 label="<input type=\\'radio\\' name=\\'dmt\\' value=\\'"+dmtid+"\\' id=\\'"+nodemap.get("id")+"\\' onclick=\"selNode(this,\\'"+dmtname+"\\',\\'"+SysConfig.DOC_PURVIEW_TYPE_DEPT+"\\',\\'X\\')\">";//
		             }
		             //label+="<img src="+icon+" border=0/>";
		             treeShowHtml.append("d.add(").append(nodemap.get("id")).append(",")
	         			.append(nodemap.get("pid")).append(",")
	         			.append("'").append(doctree.get("dmtname")).append("',")
	         			.append("'").append(URL).append("',")//url
	         			.append("'").append(doctree.get("dmtname")).append("',")
	         			.append("'").append("rightFrm").append("',")
	         			.append("'").append(label).append("',")
	         			.append("'").append(icon).append("',")//icon
	        			.append("'").append(iconopen)
	        			//.append("',")//iconOpen
	        			//.append("'").append(doc_folder)
	         			.append("');\n");//open
		          }
	         }
        }
        ////System.out.println(treeShowHtml.toString());
        return treeShowHtml.toString();
    	
    }
    */
    
    
    /**
     * 建立以点分隔的通用树
     * 注意：构建树的字段必须order by nodeid asc
     * @param list
     * @param split
     * @param preid
     * @param curid
     * @return
     */
    private List buildTree(List list,String split,int curid,int preid){
    	int root=preid;
    	List rslist=new ArrayList();
    	HashMap node;
    	Hashtable ht=new Hashtable();
    	split=split==null||split.trim().length()==0 ?".":split;
    	if(list!=null && list.size()>0){
	    	for (int i = 0; i < list.size(); i++) {
	        	HashMap NodeMap = (HashMap) list.get(i);
	        	String nodeid=NodeMap.get("nodeid").toString();
	        	String[] nid=CommonUtil.getFliter(nodeid,split);
	        	
	        	int len=nid!=null?nid.length:0;
	        	String curpix="";
	        	preid=root;
	        	for(int j=len-1;j>=0;j--){
	        		curpix="";
	        		if(j>0){
	        			for(int k=0;k<j;k++){
	        				curpix+=nid[k]+split;
	        			}
	        		}
	        		curpix+=nid[j];
	        		boolean tag =false;
	        		if(ht.containsKey(curpix)){
	        			preid=Integer.parseInt(ht.get(curpix).toString());
	        			tag=true;
	        		}
	        		if(tag){
	        			node=new HashMap();
    		        	node.put("nodeid",nodeid);
    		        	node.put("id",curid+"");
    		        	node.put("pid",preid+"");
    		        	node.put("nodeObj",NodeMap.get("nodeObj"));	    		        	
    		        	rslist.add(node);
    		        	
    		        	ht.put(nodeid,curid+"");
        				preid=curid;
    		        	curid++;
    		        	break;
	        		}
	        		if(j>0){//未匹配完
	        				//ht.put(curpix,curid+"");
	        				//preid=curid;
	        				//curid++;
	        			continue;
	        		}else{//完整
	        				node=new HashMap();
	    		        	node.put("nodeid",nodeid);
	    		        	node.put("id",curid+"");
	    		        	node.put("pid",preid+"");
	    		        	node.put("nodeObj",NodeMap.get("nodeObj"));	    		        	
	    		        	rslist.add(node);
	    		        	
	    		        	ht.put(nodeid,curid+"");
	        				preid=curid;
	    		        	curid++;
	        			}
	  
	        	}         
	        }
    	}
    	return rslist;
    }
    /**
     * 增加一个结点
     * @param treeShowHtml
     * @param curid
     * @param pid
     * @param nodename
     * @param url
     * @param alt
     * @param target
     * @param icon
     * @param iconOpen
     * @param open
     * @return
     */
    public StringBuffer RootNode(StringBuffer treeShowHtml,int curid,int pid,String nodename,String url,String alt,String target,String icon,String iconOpen,String open){
    	
    	treeShowHtml.append("d.add(").append(curid).append(",")
    		.append(pid).append(",")
			.append("'").append(nodename).append("',")
			.append("'").append(url).append("',")
			.append("'").append(alt).append("',")
			.append("'").append(target).append("',")
		    .append("'").append(icon).append("',")//icon
		    .append("'").append(iconOpen)
		    //.append("',")//iconOpen
		    //.append("'").append(open)
		    .append("');\n");//open
    	return treeShowHtml;
    }
    /**
     * 根据param1 的参数值，0：部门和人可选；1：只是部门可选；2：只是人可选
     * dtreeVJ function Node(id, pid, name, url, title, target,box, icon, iconOpen, open)
     * @return
     */
    public String DeptUserTree(){
    	List list1 = null;
        List list2 = null;
        Session session = null;
        StringBuffer treeShowHtml = new StringBuffer();  
        try {
            session = HibernateUtil.getSession();          
            String sql1="FROM Storageid where enabledflag='Y' order by storageid ";
            String sql2="Select a,b.userName " +
                	"From ViewUserDept AS a ,ViewLoginUserInfo as b " +
                	"WHERE a.id.flag='W' and a.id.userId=b.userID and b.moduleID = '0' and b.enabledFlag = 'Y' "; 
            Query query = session.createQuery(sql1);
            Query query2 = session.createQuery(sql2);
            list1 = query.list();
            list2 = query2.list();         
  
	        treeShowHtml.append("d = new dTree('d');\n");
	        treeShowHtml.append(SysConfig.TREE_ROOT_NODE);
	        String URL = url1;//页面传递过来的url
	        int childid=1000;
	        if(list1!=null && list1.size()>0){
	        	int len=list1.size();
	        	List depttree=new ArrayList();
	        	HashMap node;
	        	for(int i=0;i<len;i++){
	        		Storageid dept = (Storageid) list1.get(i);
	        		node=new HashMap();
	        		//node.put("nodeid",dept.getDeptid());
	        		node.put("nodeid",dept.getStorageid());
	        		node.put("nodeObj",dept);
	        		depttree.add(node);
	        	}
	            depttree=this.buildTree(depttree,".",1,0);
	            len=depttree.size();
	            String flag=param1;
	            flag=flag==null?"0":flag.trim();//设置flag的默认值
	            String box="";
	            for(int j=0;j<len;j++){
	            	node=(HashMap)depttree.get(j);
	            	Storageid dept=(Storageid)node.get("nodeObj");
	            	int nodeid=Integer.parseInt((String)node.get("id"));
	            	//function Node(id, pid, name, url, title, target,box, icon, iconOpen, open)
	            	if(flag.equalsIgnoreCase("0")||flag.equalsIgnoreCase("1")){//0或1部门可选
	            		box="<input type=\\'checkbox\\' name=\\'user\\' value=\\'"+dept.getStorageid()+"\\' id=\\'"+nodeid+"\\' onclick=\"selNode(this,\\'"+dept.getStoragename()+"\\',\\'"+SysConfig.DOC_PURVIEW_TYPE_DEPT+"\\',\\'X\\')\">";
	            	}else{
	            		box="";
	            	}
	            	treeShowHtml.append("d.add(").append(nodeid).append(",")
	     			.append(node.get("pid")).append(",")
	     			.append("'").append(dept.getStoragename()).append("',")
	     			.append("'").append(URL).append("',")//url
	     			.append("'").append(dept.getStoragename()).append("',")
	     			.append("'").append("rightFrm").append("',")
	     			.append("'").append(box).append("',")
	     			.append("'").append(folder).append("',")//icon
	    			.append("'").append(folderopen)
	    			//.append("',")//iconOpen
	    			//.append("'").append(folderopen)
	    			.append("');\n");//open
	            	
	            	if(list2!=null && list2.size()>0){
		            	for(int k=list2.size()-1;k>=0;k--){
		            		Object[] obj=(Object[])list2.get(k);
		            		ViewUserDept a=(ViewUserDept)obj[0];
		            		String uname =(String)obj[1];
		            		if(a.getId().getDeptId().equalsIgnoreCase(dept.getStorageid())){
		            			if(flag.equalsIgnoreCase("0")||flag.equalsIgnoreCase("2")){//0或2用户可选
		                    		box="<input type=\\'checkbox\\' name=\\'user\\' value=\\'"+a.getId().getUserId()+"\\' id=\\'"+childid+"\\' onclick=\"selNode(this,\\'"+uname+"\\',\\'"+SysConfig.DOC_PURVIEW_TYPE_USER+"\\',\\'X\\')\">";
		                    	}else{
		                    		box="";
		                    	}
			            		treeShowHtml.append("d.add(").append(childid++).append(",")
			         			.append(nodeid).append(",")
			         			.append("'").append(uname).append("',")
			         			.append("'").append(URL).append("',")//url
			         			.append("'").append(uname).append("',")
			         			.append("'").append("rightFrm").append("',")
			         			.append("'").append(box).append("',")
			         			.append("'").append(doc_folder).append("',")//icon
			        			.append("'").append(doc_folder)
			        			//.append("',")//iconOpen
			        			//.append("'").append(doc_folder)
			        			.append("');\n");//open
			            		
			            		list2.remove(k);//删除多余的结点，以减小循环次数
		            		}
		            	}
	            	}
	            }
	        }
        } catch (Exception e) {
            DebugUtil.print(e);
        } finally {
            try {
                if(session!=null){session.close();}
            } catch (HibernateException hex) {
                DebugUtil.print(hex);
            }
        }
        return treeShowHtml.toString();

    }
    /**
     * 根据param1 的参数值，0：群组和人可选；1：只是群组可选；2：只是人可选
     * dtreeVJ function Node(id, pid, name, url, title, target,box, icon, iconOpen, open)
     * @return
     */
    public String ClassUserTree(){
    	List list0 = null;
    	List list1 = null;
        Session session = null;
        StringBuffer treeShowHtml = new StringBuffer();  
        try {
            session = HibernateUtil.getSession();          
            String sql0="from Class1 a order by a.class1id "; 
            Query query = session.createQuery(sql0);
            list0 = query.list();

            String sql1="select a FROM ViewClassUser a,Loginuser b " +
            		"where a.id.userId = b.userid " +
            		"and b.enabledflag = 'Y' " +
            		"order by a.id.typeId,a.id.classId,a.id.userId";
            query = session.createQuery(sql1);
            list1 = query.list();          
     
	        treeShowHtml.append("cut = new dTree('cut');\n");
	        treeShowHtml.append("cut.add(0,-1,'XJSCRM',null,'XJSCRM','main');\n");
	        String URL = url1;//页面传递过来的url
	        int childid=1000;
	        if(list0!=null && list0.size()>0 && list1!=null && list1.size()>0){
	        	/*
	        	Class1 c0=new Class1();
	        	c0.setClass1id("0");
	        	c0.setClass1name("申请人［流程使用］");
	        	c0.setLevelid(new Integer(0));
	        	c0.setEnabledFlag("Y");
	        	c0.setRem1("");
	        	list0.add(0,c0);
	        	c0=new Class1();
	        	c0.setClass1id("99999999");
	        	c0.setClass1name("代理商");
	        	c0.setLevelid(new Integer(0));
	        	c0.setEnabledFlag("Y");
	        	c0.setRem1("");
	        	list0.add(1,c0);
	        	*/
	        	int len=list0.size();
	        	List classusertree=new ArrayList();
	            String flag=param1;
	            flag=flag==null?"0":flag.trim();//设置flag的默认值
	            String box="";
	            int pid=10000;
	            int cid=20000;
	            Class1 pvcu=null;
	            ViewClassUser cvcu=null;
	            boolean isfirst=true;
	            for(int j=0;j<len;j++){
	           		pvcu=(Class1)list0.get(j);
	
	//        		function Node(id, pid, name, url, title, target,box, icon, iconOpen, open)
	            	if(flag.equalsIgnoreCase("0")||flag.equalsIgnoreCase("1")){//0或1部门可选
	            		box="<input type=\\'checkbox\\' name=\\'user\\' value=\\'"+pvcu.getClass1id()+"\\' id=\\'"+"C_"+pvcu.getClass1id()+"\\' onclick=\"selNode(this,\\'"+pvcu.getClass1name()+"\\',\\'"+SysConfig.DOC_PURVIEW_TYPE_CLASS+"\\',\\'X\\')\">";
	            	}else{
	            		box="";
	            	}
	            	
	            	treeShowHtml.append("cut.add(").append(pid).append(",")
	     			.append(0).append(",")
	     			.append("'").append(pvcu.getClass1name()).append("',")
	     			.append("'").append(URL).append("',")//url
	     			.append("'").append(pvcu.getClass1name()).append("',")
	     			.append("'").append("rightFrm").append("',")
	     			.append("'").append(box).append("',")
	     			.append("'").append(folder).append("',")//icon
	    			.append("'").append(folderopen)
	    			//.append("',")//iconOpen
	    			//.append("'").append(folderopen)
	    			.append("');\n");//open
	            	
	            	for(int k=list1.size()-1;k>=0;k--){
	            		cvcu=(ViewClassUser)list1.get(k);
	            		if(pvcu.getClass1id().equalsIgnoreCase(cvcu.getId().getClassId())){
	            			if(flag.equalsIgnoreCase("0")||flag.equalsIgnoreCase("2")){//0或2用户可选
	                    		box="<input type=\\'checkbox\\' name=\\'user\\' value=\\'"+cvcu.getId().getUserId()+"\\' id=\\'"+cid+"\\' onclick=\"selNode(this,\\'"+cvcu.getId().getUserName()+"\\',\\'"+SysConfig.DOC_PURVIEW_TYPE_USER+"\\',\\'X\\')\">";
	                    	}else{
	                    		box="";
	                    	}
		            		treeShowHtml.append("cut.add(").append(cid++).append(",")
		         			.append(pid).append(",")
		         			.append("'").append(cvcu.getId().getUserName()).append("',")
		         			.append("'").append(URL).append("',")//url
		         			.append("'").append(cvcu.getId().getUserName()).append("',")
		         			.append("'").append("rightFrm").append("',")
		         			.append("'").append(box).append("',")
		         			.append("'").append(doc_folder).append("',")//icon
		        			.append("'").append(doc_folder)
		        			//.append("',")//iconOpen
		        			//.append("'").append(doc_folder)
		        			.append("');\n");//open
		            		
		            		list1.remove(k);
	            		}
	            	}
	            	pid++;
	            }
	        }
        } catch (Exception e) {
            DebugUtil.print(e);
        } finally {
            try {
                if(session!=null){session.close();}
            } catch (HibernateException hex) {
                DebugUtil.print(hex);
            }
        }
        return treeShowHtml.toString();

    }
    /*
    public String ClassUserTree_old(){
    	List list1 = null;
        Session session = null;
        try {
            session = HibernateUtil.getSession();          
            String sql1="FROM Viewclassuser a order by a.id.typeid,a.id.classid,a.id.userid";
            Query query = session.createQuery(sql1);
            list1 = query.list();          
        } catch (Exception e) {
            DebugUtil.print(e);
        } finally {
            try {
                if(session!=null){session.close();}
            } catch (HibernateException hex) {
                DebugUtil.print(hex);
            }
        }

        StringBuffer treeShowHtml = new StringBuffer();       
        treeShowHtml.append("cut = new dTree('cut');\n");
        treeShowHtml.append("cut.add(0,-1,'DRP',null,'DRP','main');\n");
        String URL = url1;//页面传递过来的url
        int childid=1000;
        if(list1!=null && list1.size()>0){
        	int len=list1.size();
        	List classusertree=new ArrayList();
            String flag=param1;
            flag=flag==null?"0":flag.trim();//设置flag的默认值
            String box="";
            int pid=10000;
            int cid=20000;
            Viewclassuser pvcu=null,cvcu=null;
            boolean isfirst=true;
            for(int j=0;j<len;j++){
           		pvcu=(Viewclassuser)list1.get(j);

//        		function Node(id, pid, name, url, title, target,box, icon, iconOpen, open)
            	if(flag.equalsIgnoreCase("0")||flag.equalsIgnoreCase("1")){//0或1部门可选
            		box="<input type=\\'checkbox\\' name=\\'user\\' value=\\'"+pvcu.getClassid()+"\\' id=\\'"+"C_"+pvcu.getClassid()+"\\' onclick=\"selNode(this,\\'"+pvcu.getClassname()+"\\',\\'"+SysConfig.DOC_PURVIEW_TYPE_CLASS+"\\',\\'X\\')\">";
            	}else{
            		box="";
            	}
            	
            	treeShowHtml.append("cut.add(").append(pid).append(",")
     			.append(0).append(",")
     			.append("'").append(pvcu.getClassname()).append("',")
     			.append("'").append(URL).append("',")//url
     			.append("'").append(pvcu.getClassname()).append("',")
     			.append("'").append("rightFrm").append("',")
     			.append("'").append(box).append("',")
     			.append("'").append(folder).append("',")//icon
    			.append("'").append(folderopen)
    			//.append("',")//iconOpen
    			//.append("'").append(folderopen)
    			.append("');\n");//open
            	
            	for(int k=j;k<len;k++){
            		cvcu=(Viewclassuser)list1.get(k);
            		if(pvcu.getClassid().equalsIgnoreCase(cvcu.getClassid())){
            			if(flag.equalsIgnoreCase("0")||flag.equalsIgnoreCase("2")){//0或2用户可选
                    		box="<input type=\\'checkbox\\' name=\\'user\\' value=\\'"+cvcu.getUserid()+"\\' id=\\'"+cid+"\\' onclick=\"selNode(this,\\'"+cvcu.getUsername()+"\\',\\'"+SysConfig.DOC_PURVIEW_TYPE_USER+"\\',\\'X\\')\">";
                    	}else{
                    		box="";
                    	}
	            		treeShowHtml.append("cut.add(").append(cid++).append(",")
	         			.append(pid).append(",")
	         			.append("'").append(cvcu.getUsername()).append("',")
	         			.append("'").append(URL).append("',")//url
	         			.append("'").append(cvcu.getUsername()).append("',")
	         			.append("'").append("rightFrm").append("',")
	         			.append("'").append(box).append("',")
	         			.append("'").append(doc_folder).append("',")//icon
	        			.append("'").append(doc_folder)
	        			//.append("',")//iconOpen
	        			//.append("'").append(doc_folder)
	        			.append("');\n");//open
            		}else{
            			j=k-1;
            			break;
            		}
            		if(k==(len-1)){
            			j=len;
            		}
            	}
            	pid++;
            }
        }
        return treeShowHtml.toString();

    }
    */
    /*
    public String getPurview(List list,String type,String id){
    	if(list!=null && list.size()>0){
    		for(int i=list.size()-1;i>=0;i--){
    			Docmanagertreepurview a=(Docmanagertreepurview)list.get(i);
    			if(a.getId().getType().equalsIgnoreCase(type) && a.getId().getUser1().equalsIgnoreCase(id)){
    				return a.getFlag();
    			}
    		}
    	}
    	return "";
    }
    **/
    
    
    /**
     * 构造流水号
     * @param s
     * @return
     */
    private final String contrustStr(String s){
    	String str = "";
    	str = s;
    	int len = str.length();
    	while(len<4){
    		str = "0" + str;
    		len = len + 1;
    	}
    	return str;
    }
    
    
    
    

    
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getUrl1() {
		return url1;
	}

	public void setUrl1(String url1) {
		this.url1 = url1;
	}
	
	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

}