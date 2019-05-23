package com.gzunicorn.common.taglib.treeshow;

/**
 * Created on 2005-7-12
 * <p>Title:	功能树DAO</p>
 * <p>Description:	根据用户提取不同的功能树节点</p>
 * <p>Copyright: Copyright (c) 2005
 * <p>Company:友联科技</p>
 * @author wyj
 * @version	1.0
 */
import java.util.*;
import org.hibernate.*;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.*;
import javax.servlet.ServletRequest;
//import javax.servlet.jsp.tagext.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gzunicorn.common.util.*;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.hibernate.viewmanager.Viewuserrolenode;


public class TreeShowTag extends TagSupport {

  private String userID;
  
  public TreeShowTag() {
  }


  public int doStartTag(){
    
    JspWriter out = pageContext.getOut();
    PageContext pageContext =  this.pageContext;
    ServletRequest servletRequest = pageContext.getRequest();
    HttpSession httpSession = pageContext.getSession();
    Locale locale = servletRequest.getLocale();
    
    ViewLoginUserInfo userInfo = (ViewLoginUserInfo)httpSession.getAttribute(SysConfig.LOGIN_USER_INFO);
//    //System.out.println("userID="+userInfo.getUserID());
    ArrayList userNodeList = null;
    Session session  = null;
    String hsql = "";
    String logflag = (String)httpSession.getAttribute("logflag");//判断登录方式标志
    //如果是通过OA登录系统的,就不出注销菜单
    if(logflag != null && logflag.equals("2"))
    {
    	String nodeid = "99";
    	hsql = "from Viewuserrolenode a where a.userid = :userID and a.nodeid <> '"+nodeid+"'"+" order by a.nodesort asc";
    }
    //CRM系统URL访问
    else
    {
    	hsql = "from Viewuserrolenode a where a.userid = :userID order by a.nodesort asc";
    }
    if(userInfo == null){
        return 0;
    }
   try{
       session = HibernateUtil.getSession();
       Query query = session
       .createQuery(hsql);
       //当Tld中的UserID参数不存在时，取当前用户
       if(this.userID == null || userID.length() == 0){
           query.setString("userID", userInfo.getUserID());    
       }else{
           query.setString("userID", this.getUserID());
       }
       
       
       userNodeList = (ArrayList) query.list();
//       //System.out.println("userNodeList.length="+userNodeList.size());
       out.println(this.getTreeShowHtml(userNodeList));
       
    }catch(TreeShowException ose){
      DebugUtil.println("TreeShowTag.doStartTag() " + ose.toString());
    }catch(Exception e){
      DebugUtil.print(e);
    }finally{
        try{
            session.close();    
        }catch(HibernateException hex){
            DebugUtil.print(hex);
        }
        
        
    }
    return this.EVAL_BODY_INCLUDE ;
  }

  public int doEndTag(){
    return this.EVAL_PAGE ;
  }
  public String getUserID() {
    return userID;
  }
  
  public void setUserID(String userID) {
    this.userID = userID;
  }

/**
 * 根据userID 参数获取功能树显示html
 * @return String 节点显示html
 * @throws TreeShowException
 */
private String getTreeShowHtml(List userNodeList) throws TreeShowException {

    StringBuffer treeShowHtml = new StringBuffer("");
    treeShowHtml.append("d = new dTree('d');\n");
    treeShowHtml.append(SysConfig.TREE_ROOT_NODE);
    if(userNodeList != null)
    {
    for (int i = 0; i < userNodeList.size(); i++) {
      
      Viewuserrolenode userNode = (Viewuserrolenode)userNodeList.get(i);
      ////System.out.println("userNode.getNodeid="+userNode.getNodeid());
      if(userNode.getNodeid() != null && userNode.getNodeid().length()==2)
      {
    	treeShowHtml.append("d.add(").append(userNode.getNodeid()).append(",0,'").append(userNode.getNodename()).append("','").append(userNode.getNodeurl()).append("','").append(userNode.getNodename()).append("','").append(userNode.getNodetarget()).append("','").append(userNode.getNodeimage()).append("');\n");	
    	////System.out.println("sss");
//    	//System.out.println("lwngth=2 d write:"+treeShowHtml);
      }
      else
      {
      	treeShowHtml.append("d.add(").append(userNode.getNodeid()).append(",").append(this.getParaNode(userNode.getNodeid())).append(",'").append(userNode.getNodename()).append("','").append(userNode.getNodeurl()).append("','").append(userNode.getNodename()).append("','").append(userNode.getNodetarget()).append("','").append(userNode.getNodeimage()).append("');\n");
      	////System.out.println("bbb");
//      	 //System.out.println("d write :"+treeShowHtml);  
      	 
      }
      /*treeShowHtml.append("oArray = new tArray();\n");
      treeShowHtml.append("oArray.Add('cate_desc','").append(userNode.getNodeName()).append("');\n");
      treeShowHtml.append("oArray.Add('cate_value','").append(userNode.getNodeID()).append("');\n");
      treeShowHtml.append("oArray.Add('cate_link','").append(userNode.getNodeURL()).append("');\n");
      treeShowHtml.append("oArray.Add('cate_target','").append(userNode.getNodeTarget()).append("');\n");
      treeShowHtml.append("oArray.Add('cate_data','").append(userNode.getNodeData()).append("');\n");
      treeShowHtml.append("oArray.Add('node_type','").append("1").append( "');\n");
      treeShowHtml.append("oTree.Add('").append(userNode.getNodeID()).
      append("','").append(this.getParaNode(userNode.getNodeID())).append("',oArray);\n");*/
   
    }
   }
    return treeShowHtml.toString();
}
  
  /**
   * 根据当前节点取它的父节点
   * @param node
   * @return
   */
  private String getParaNode(String node){
      String paraNode = new String("");
      if(node == null || node.length() < SysConfig.TREE_NODE_LEVEL){
          DebugUtil.println(node + "节点编码有误！");
          return paraNode;
      }else{
          //判断是否是根节点
          if(node.length() == SysConfig.TREE_NODE_LEVEL){
             paraNode = ""; 
          }else{
              paraNode = node.substring(0,node.length()-2);
          }
          return paraNode;
      }
      
  }
  
} 
