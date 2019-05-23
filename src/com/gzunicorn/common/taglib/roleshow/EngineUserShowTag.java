/*
 * Created on 2011-3-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.common.taglib.roleshow;

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
 * @author Lee Yang
 * @version 1.0
 */

import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.hibernate.viewmanager.Viewroleallowflagy;
import com.gzunicorn.hibernate.viewmanager.Viewuserallowflagy;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.hibernate.*;
import com.gzunicorn.common.util.*;

public class EngineUserShowTag extends TagSupport {

    public EngineUserShowTag() {
    }

    public int doStartTag() {
        JspWriter out = this.pageContext.getOut();
        PageContext pageContext = this.pageContext;
        ServletRequest servletRequest = pageContext.getRequest();
        HttpSession httpSession = pageContext.getSession();
        java.util.Locale locale = servletRequest.getLocale();
        
//        ViewLoginUserInfo userInfo = (ViewLoginUserInfo) httpSession
//                .getAttribute(SysConfig.LOGIN_USER_INFO);
        ArrayList role = null;
        ArrayList roleUser = null;
        Session session = null;

        try {
            session = HibernateUtil.getSession();

            Query role_query = session.createQuery("from Viewroleallowflagy");
            Query user_query = session.createQuery("from Viewuserallowflagy");
            
            role = (ArrayList) role_query.list();
            roleUser = (ArrayList) user_query.list();
            
       //     //System.out.println(" == 角色用户 role.size() ：" + role.size() + "\n"
		//			+ "roleUser.size()" + roleUser.size());
            
            out.println(getTreeShowHtml(roleUser, role));
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private String getTreeShowHtml(List arrayList, List arrayList2)
            throws RoleShowException {
        StringBuffer treeShowHtml = new StringBuffer();
        
        treeShowHtml.append("d = new dTree('d');\n");
        treeShowHtml.append("d.add(-2,-1,'XJSCRM',null,'XJSCRM','assignMain');\n");
        String URLTemp = "../EngineeringSalesmanAction.do?method=toEngineUserRightFrm";
        StringBuffer URL = null;        
        for (int i = 0; i < arrayList2.size(); i++) {
			URL = new StringBuffer(URLTemp);
			Viewroleallowflagy role = (Viewroleallowflagy) arrayList2.get(i);
			treeShowHtml.append("d.add('").append(role.getRoleid()).append(
					"',-2,'").append(role.getRolename()).append("','").append(
					SysConfig.NULL_URL).append("','")
					.append(role.getRolename()).append("','").append("")
					.append("','").append("").append("');\n");
		}

        for (int i = 0; i < arrayList.size(); i++) {
            URL = new StringBuffer(URLTemp);
            Viewuserallowflagy users = (Viewuserallowflagy) arrayList.get(i);
            if (users.getUserid() != null)
                URL.append("&").append("headerId=").append(
                		users.getUserid());

            // 因节点代码只能是数字，不能含有因为字母roleNodeBean.getRoleID()
            treeShowHtml.append("d.add(").append(i + 100).append(",'").append(
					users.getRoleid()).append("','").append(users.getUsername())
					.append("','").append(URL.toString()).append("','").append("','").append(
							"rightFrm").append("','").append("")
					.append("');\n");
        }
        // //System.out.println(treeShowHtml.toString());
        return treeShowHtml.toString();
    }

    private String userID;
}