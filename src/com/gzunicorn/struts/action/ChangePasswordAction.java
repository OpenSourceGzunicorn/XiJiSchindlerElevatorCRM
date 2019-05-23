//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.gzunicorn.struts.action;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.util.CryptUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

/**
 * MyEclipse Struts Creation date: 03-16-2006
 * 
 * XDoclet definition:
 * 
 * @struts:action validate="true"
 */
public class ChangePasswordAction extends DispatchAction {
	Log log = LogFactory.getLog(ChangePasswordAction.class);

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
		SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "changePassword",null);
		/************结束用户权限过滤************/
		// Set default method is toSearchRecord

		ActionForward forward = super.execute(mapping, form, request, response);
		return forward;

	}

	/**
	 * Method toPrepareModify execute,prepare data for add page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toPrepareModifyRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		String naigation = new String();
		this.setNavigation(request, "navigator.base.loginuser.modify");

		DynaActionForm dform = (DynaActionForm) form;
		// dform.reset(mapping,null);
		dform.initialize(mapping);

		java.util.List result = new java.util.ArrayList();
		return mapping.findForward("prepareModify");
	}

	/**
	 * Get the navigation description from the properties file by navigation
	 * key;
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
	 * Method toPrepareModify execute,prepare data for add page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toModifyRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String naigation = new String();
		this.setNavigation(request, "navigator.base.loginuser.modify");

		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();
		ViewLoginUserInfo userinfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);

		if (userinfo != null) {
			DynaActionForm dform = (DynaActionForm) form;
			String userID = userinfo.getUserID();
			String oldpasswd = (String) dform.get("oldpasswd");
			String passwd = (String) dform.get("passwd");

			Session hs = null;
			Transaction tx = null;
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
				Loginuser lu = (Loginuser) hs.get(Loginuser.class, userID);

				// //判断用户是否正确
				if (lu == null) {
					DebugUtil.println("非法用户登录!");
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"logon.user.error"));
				} else {
					// 判断密码是否正确
					if (oldpasswd == null
							|| !new CryptUtil().decode(oldpasswd, "LO").equals(
									lu.getPasswd())) {
						DebugUtil.println("用户密码输入错误!");
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"logon.passwd.error"));
					} else {
						lu.setPasswd(new CryptUtil().decode(passwd, "LO"));
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"update.success"));
						hs.save(lu);
						tx.commit();
						dform.initialize(mapping);
					}
				}
			} catch (HibernateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();

			} catch (DataStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "Hibernate close error!");
				}
			}

		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return mapping.findForward("prepareModify");
	}

}