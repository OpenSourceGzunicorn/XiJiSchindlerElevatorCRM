package com.gzunicorn.common.web.listener;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import java.util.*;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.bean.UserInfoBean;

import org.hibernate.*;

/**
 * 
 * Created on 2005-11-21
 * <p>
 * Title: 公共程序
 * </p>
 * <p>
 * Description: 监听用户登录类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * <p>
 * Company:广州友联
 * </p>
 * 
 * @author wyj
 * @version 1.0
 */
public class OnLineUserListener implements HttpSessionListener,
		ServletContextListener, ServletContextAttributeListener {

	private ArrayList loginUserList = null;

	private ServletContext context = null;

	// 创建一个新的Session时触发
	public void sessionCreated(HttpSessionEvent hse) {

//		ViewLoginUserInfo userInfo = new ViewLoginUserInfo();
//		UserInfoBean userInfoBean = new UserInfoBean(); 
//		ArrayList AllUserInfo = new ArrayList();
//
//		if (hse.getSession().getAttribute(SysConfig.LOGIN_USER_INFO) != null) {
//			
//			userInfo = (ViewLoginUserInfo) hse.getSession().getAttribute(
//					SysConfig.LOGIN_USER_INFO);
//			
//			userInfoBean.setSessionID(hse.getSession().getId());
//			userInfoBean.setUserID(userInfo.getUserID());
//			userInfoBean.setUserName(userInfo.getUserName());
//			userInfoBean.setRoleName(userInfo.getRoleName());
//			userInfoBean.setAreaName(userInfo.getAreaName());
//			
//			AllUserInfo = (ArrayList) hse.getSession().getServletContext()
//					.getAttribute(SysConfig.ALL_LOGIN_USER_INFO);
//			AllUserInfo.add(userInfoBean);
//			DebugUtil.println("当前ServletContext增加了一个用户Session: " + userInfoBean.getSessionID());
//		}

	}

	// 销毁一个新的Session时触发
	public void sessionDestroyed(HttpSessionEvent hse) {
		
		String sessionID;
		ArrayList AllUserInfo = new ArrayList();
		ArrayList AllUserInfoNew = new ArrayList();

		//取当前销毁的Session ID
			sessionID = hse.getSession().getId();
			//取当前Context 的所有 用户Session信息(UserInfoBean)
			AllUserInfo = (ArrayList) hse.getSession().getServletContext()
					.getAttribute(SysConfig.ALL_LOGIN_USER_INFO);
			
			if(AllUserInfo != null && !AllUserInfo.isEmpty()){
				for(int i=0; i<AllUserInfo.size(); i++){
					
					UserInfoBean userInfoBean = (UserInfoBean)AllUserInfo.get(i);
					
					if(userInfoBean != null){
						if(!sessionID.equals(userInfoBean.getSessionID())){
							//取和当前销毁不同的Session UserInfoBean,放到新的List中
							AllUserInfoNew.add(userInfoBean);
						}
					}
				}
				hse.getSession().getServletContext().setAttribute(SysConfig.ALL_LOGIN_USER_INFO,AllUserInfoNew);
				if(AllUserInfo.size() > AllUserInfoNew.size()){
					DebugUtil.println("当前ServletContext销毁了一个用户Session: " + sessionID);	
				}
				
			}
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void attributeAdded(ServletContextAttributeEvent arg0) {
		
		////System.out.println(arg0.getName());
		// TODO Auto-generated method stub

	}

	public void attributeRemoved(ServletContextAttributeEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void attributeReplaced(ServletContextAttributeEvent arg0) {
		// TODO Auto-generated method stub
		////System.out.println(arg0.getName());
	}

}
