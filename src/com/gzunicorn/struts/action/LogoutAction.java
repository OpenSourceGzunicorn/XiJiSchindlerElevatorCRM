/*
 * Created on 2005-8-5
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.gzunicorn.struts.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gzunicorn.common.util.SysConfig;


/**
 * Created on 2005-7-12
 * <p>Title:	</p>
 * <p>Description:	退出系统</p>
 * <p>Copyright: Copyright (c) 2005
 * <p>Company:友联科技</p>
 * @author wyj
 * @version	1.0
 * 2007-11-28 cwy 修改(如果是通过OA登录系统方式的,不作注销)
 */
public final class LogoutAction extends Action {

	
// --------------------------------------------------------- Public Methods


public ActionForward execute(ActionMapping mapping,
							 ActionForm form,
							 HttpServletRequest request,
							 HttpServletResponse response)
	throws IOException, ServletException {



	// Invalidate the current session and create a new one
	HttpSession session = request.getSession();
	//String logflag = (String)session.getAttribute("logflag");//判断登录方式标志,其中"1"为系统直接登录方式,"2"通过OA登录)
	//如果是OA登录的,用户不用注销
	//if(logflag != null && logflag.equals("2"))
	//{
		
	//}
	//如果是系统直接登录的,要注销
	//else{
		
		session.invalidate();
		session = request.getSession(true);
		session.removeAttribute(SysConfig.LOGIN_USER_INFO);
	//}
	// Forward control back to the logout.jsp
	return (mapping.findForward("success"));
	

}


}
