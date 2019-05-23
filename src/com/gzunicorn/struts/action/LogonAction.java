//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.gzunicorn.struts.action;
 

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.hibernate.*;

import com.gzunicorn.bean.UserInfoBean;
import com.gzunicorn.common.util.*;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

import com.gzunicorn.struts.form.LogonForm;

/**
 * MyEclipse Struts Creation date: 07-10-2005 用户登录 XDoclet definition:
 *								   2007-11-27 cwy 修改 
 * @struts:action path="/logon" name="logonForm" input="/logon/logon.jsp"
 *                scope="request" validate="true"
 * @struts:action-forward name="error" path="/logon/logon.jsp"
 * @struts:action-forward name="success" path="/main/main.jsp"
 */
public class LogonAction extends Action {

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
            HttpServletRequest request, HttpServletResponse response) {

        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);
        HttpSession httpSession = request.getSession();
        ActionErrors errors = new ActionErrors();
        Log log = LogFactory.getLog(LogonAction.class);
        LogonForm logonForm = (LogonForm) form;
        ViewLoginUserInfo UserInfo = new ViewLoginUserInfo();
        Session session = null;
        ActionForward forward = null;

        String userID = request.getParameter("userID");
        String passwd = request.getParameter("passwd");
        String logflag = "1";//判定登录用户是以什么方式登录,其中"1"为系统登录,"2"为OA单点登录
        
        ArrayList userList = null;
        
        //系统原来登录部分开始
        try {
        	String gruser = "gruser";//oa登录公共用户,不能在CRM登录页面登录
            session = HibernateUtil.getSession();
            
            Query query = session.createQuery("from ViewLoginUserInfo a where a.userID = :userID and a.userID <> :gruser and a.enabledFlag = :enabledFlag");
            query.setString("userID", userID);
            query.setString("gruser",gruser);
            query.setString("enabledFlag","Y");
            userList = (ArrayList) query.list();
            // //判断用户是否正确
            if (userList == null || userList.isEmpty() || userList.size() == 0) {
                DebugUtil.println("非法用户登录!");
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("logon.user.error"));
            } else {
            	ViewLoginUserInfo userInfo = (ViewLoginUserInfo) userList.get(0);
                // 判断密码是否正确
                if ( passwd == null || !new CryptUtil().decode(passwd, "LO").equals(userInfo.getPasswd())) {
                    DebugUtil.println("用户密码输入错误!");
                    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("logon.passwd.error"));
                } else {
                    httpSession.setAttribute(SysConfig.LOGIN_USER_INFO,userInfo);
                    DebugUtil.print(userInfo.getUserName()+ "用户登录登录成功，对应SessionID:" + httpSession.getId());
                    
                    //向ServletContext 添加登录用户
					UserInfoBean userInfoBean = new UserInfoBean();
					ArrayList AllUserInfo = new ArrayList();

					userInfoBean.setSessionID(httpSession.getId());
					userInfoBean.setUserID(userInfo.getUserID());
					userInfoBean.setUserName(userInfo.getUserName());
					userInfoBean.setRoleName(userInfo.getRoleName());
					userInfoBean.setStorageName(userInfo.getStorageName());
					userInfoBean.setComName(userInfo.getComName());
					
					try {
						userInfoBean.setLoginDate(CommonUtil.getToday());
					} catch (ParseException e) {
						DebugUtil.println(e.getMessage());
					}
					userInfoBean.setLoginTime(CommonUtil.getTodayTime());
					userInfoBean.setIpAddress(request.getRemoteAddr());

					AllUserInfo = (ArrayList) httpSession.getServletContext()
							.getAttribute(SysConfig.ALL_LOGIN_USER_INFO);
					if (AllUserInfo == null || AllUserInfo.isEmpty()) {
						AllUserInfo = new ArrayList();
						AllUserInfo.add(userInfoBean);
						httpSession.getServletContext().setAttribute(SysConfig.ALL_LOGIN_USER_INFO, AllUserInfo);
						DebugUtil.println("当前ServletContext增加了一个用户Session: "+ userInfoBean.getSessionID());
					}else{
						int cyc = 0;
						for (int i = 0; i < AllUserInfo.size(); i++) {
							//当 SessionID 不存在才在 ServletContext 中增加了一个用户Session ID
							if (httpSession.getId().equals(((UserInfoBean) AllUserInfo.get(i)).getSessionID())) {
								cyc++;
							}
						}
						if(cyc == 0){
							AllUserInfo.add(userInfoBean);
							httpSession.getServletContext().setAttribute(SysConfig.ALL_LOGIN_USER_INFO, AllUserInfo);
							DebugUtil.println("当前ServletContext增加了一个用户Session: "+ userInfoBean.getSessionID());

						}
					}  
                }                       
            }

        } catch (DataStoreException dex) {
            log.error(dex.getMessage());
            DebugUtil.print(dex, "HibernateUtil：Hibernate 连接 出错！");
        } catch (Exception hex) {
            log.error(hex.getMessage());
            DebugUtil.print(hex, "HibernateUtil：Hibernate Session 打开出错！");
        } finally {
            try {
            	if(session!=null){
            		session.close();
            	}
            } catch (HibernateException hex) {
                log.error(hex.getMessage());
                DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
            }

        }

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (mapping.getInputForward());
        }

	    //forward = mapping.findForward("success");
	    forward = mapping.findForward("success_2");
        //系统原来登录部分结束  
        
        httpSession.setAttribute("logflag",logflag);
        return forward;
    }
    
}
