package com.gzunicorn.common.taglib.roleshow;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.gzunicorn.bean.RoleNodeBean;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.sysmanager.Module;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

public class RoleShowNostatusTag extends TagSupport{
	 private String userID;
	public RoleShowNostatusTag() {
		
	}
	 public int doStartTag() {
	        JspWriter out = this.pageContext.getOut();
	        PageContext pageContext = this.pageContext;
	        ServletRequest servletRequest = pageContext.getRequest();
	        HttpSession httpSession = pageContext.getSession();
	        java.util.Locale locale = servletRequest.getLocale();
	        ViewLoginUserInfo userInfo = (ViewLoginUserInfo) httpSession
	                .getAttribute(SysConfig.LOGIN_USER_INFO);
	        ArrayList arrayList = null;
	        ArrayList arrayList2 = null;
	        Session session = null;
	        if (userInfo == null)
	            return 0;
	        try {
	            session = HibernateUtil.getSession();
	            StringBuffer HSQL = new StringBuffer("");
	            StringBuffer HSQL2 = new StringBuffer("");
	            if (userInfo.getModuleID().equals("0")) {
	                HSQL2.append("FROM Module AS a WHERE a.moduleid IN (0,1)");
	                HSQL
	                        .append("SELECT NEW com.gzunicorn.bean.RoleNodeBean(b.roleid,b.rolename,b.moduleid,a.modulename) "
	                                + "FROM Module AS a,Role AS b WHERE a.moduleid IN (0,1) AND b.enabledflag='Y' and a.moduleid = b.moduleid");

	            } else {
	                DebugUtil.println("当前用户不是本部用户，不能使用此功能！");
	            }
	            Query query = session.createQuery(HSQL.toString());
	            Query query2 = session.createQuery(HSQL2.toString());
	            arrayList = (ArrayList) query.list();
	            arrayList2 = (ArrayList) query2.list();
	            out.println(getTreeShowHtml(arrayList, arrayList2));
	        } catch (Exception e) {
	            DebugUtil.print(e);
	        } finally {
	            try {
	                session.close();
	            } catch (HibernateException hex) {
	                DebugUtil.print(hex);
	            }
	        }
	        return 1;
	    }
	 public int doEndTag() {
	        return 6;
	    }
//
//	    public String getUserID() {
//	        return userID;
//	    }
//
//	    public void setUserID(String userID) {
//	        this.userID = userID;
//	    }

	    private String getTreeShowHtml(List arrayList, List arrayList2)
	            throws RoleShowException {
	        StringBuffer treeShowHtml = new StringBuffer();
	        
	        treeShowHtml.append("d = new dTree('d');\n");
	        treeShowHtml.append("d.add(-2,-1,'XJSCRM',null,'XJSCRM','main');\n");
	        String URLTemp = "../nostatusAction.do?method=toRightFrm";
	        StringBuffer URL = null;        
	        for (int i = 0; i < arrayList2.size(); i++) {
	            URL = new StringBuffer(URLTemp);
	            Module module = (Module) arrayList2.get(i);
	            treeShowHtml.append("d.add(").append(module.getModuleid()).append(
	                    ",-2,'").append(module.getModulename()).append("','")
	                    .append(SysConfig.NULL_URL).append("','").append(module.getModulename())
	                    .append("','").append("").append("','").append(
	                            "").append(
	                            "');\n");
	        }

	        for (int i = 0; i < arrayList.size(); i++) {
	            URL = new StringBuffer(URLTemp);
	            RoleNodeBean roleNodeBean = (RoleNodeBean) arrayList.get(i);
	            if (roleNodeBean.getRoleID() != null
	                    && roleNodeBean.getRoleID().length() > 0)

	                URL.append("&").append("roleID=").append(
	                        roleNodeBean.getRoleID()).append("&moduleID=").append(
	                        roleNodeBean.getModuleID());

	            // 因节点代码只能是数字，不能含有因为字母roleNodeBean.getRoleID()
	            treeShowHtml.append("d.add(").append(i + 100).append(",").append(
	                    roleNodeBean.getModuleID()).append(",'").append(
	                    roleNodeBean.getRoleName()).append("','").append(
	                    URL.toString()).append("','").append(
	                    roleNodeBean.getRoleName()).append("','")
	                    .append("rightFrm").append("','").append("")
	                    .append("');\n");
	        }
	        ////System.out.println(treeShowHtml.toString());
	        return treeShowHtml.toString();
	    }

	   
	
}
