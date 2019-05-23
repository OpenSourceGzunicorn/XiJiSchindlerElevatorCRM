//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package com.gzunicorn.struts.action.sysmanager;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.CryptUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.personnelmanage.PersonnelManageMaster;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/** 
 * MyEclipse Struts
 * Creation date: 07-29-2005
 * 
 * XDoclet definition:
 * @struts:action validate="true"
 */
public class LoginUserAuditAction extends DispatchAction{
	
	Log log = LogFactory.getLog("LogicUserAction.class");
	BaseDataImpl bd = new BaseDataImpl();
	// --------------------------------------------------------- Instance Variables

	// --------------------------------------------------------- Methods

	/** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)throws Exception {

		//** **********开始用户权限过滤*********** *//*
		SysRightsUtil.filterModuleRight(request, response, SysRightsUtil.NODE_ID_FORWARD + "LoginUserAudit", null);
		//** **********结束用户权限过滤*********** */

		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			ActionForward forward = super.execute(mapping, form, request,
					response);
			return forward;
		}
	}
	
	/**
	 * Get the navigation description from the properties file by navigation key;
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
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

		request.setAttribute("navigator.location","	用户信息审核>>查询列表");	
		ActionForward forward = null;
		// listRecord
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
			HTMLTableCache cache = new HTMLTableCache(session, "loginuserauditList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fLoginUserAudit");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("userid");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			}else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			}else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String username = tableForm.getProperty("username");
			String userid = tableForm.getProperty("userid");
			String enabledflag = tableForm.getProperty("enabledflag");
			String storageid = tableForm.getProperty("storageid");
			String roleid = tableForm.getProperty("roleid");
			String grcid=tableForm.getProperty("grcid");
			
			if(grcid==null){
				grcid=userInfo.getComID();
			}
			
			Session hs = null;
			Query query = null;
			
			try {

				hs = HibernateUtil.getSession();
				
				String sql = "from Loginuser where (isnull(newphone,'')<>'' or isnull(newemail,'')<>'' "
						+ "or isnull(nweidcardno,'')<>'') and isnull(auditoperid,'')='' ";
				if (username != null && !username.equals("")) {
					sql += " and username like '"+username.trim()+"'";
				}
				if (userid != null && !userid.equals("")) {
					sql += " and userid like '"+userid.trim()+"'";
				}
				if (enabledflag != null && !enabledflag.equals("")) {
					sql += " and enabledflag like '"+enabledflag.trim()+"'";
				}
				if (storageid != null && !storageid.equals("")) {
					sql += " and storageid like '"+storageid.trim()+"'";
				}
				if (roleid != null && !roleid.equals("")) {
					sql += " and roleid like '"+roleid.trim()+"'";
				}
				if (grcid != null && !grcid.equals("")) {
					sql += " and grcid like '"+grcid.trim()+"'";
				}
				if (table.getIsAscending()) {
					sql += " order by "+table.getSortColumn()+ " asc";
				} else {
					sql += " order by "+table.getSortColumn()+" desc";
				}
				
				query = hs.createQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;
                
				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);
				
				List loglist =query.list();
				BaseDataImpl bd = new BaseDataImpl();

				if (loglist != null) {
					int len = loglist.size();
					for (int i = 0; i < len; i++) {
						Loginuser r = (Loginuser) loglist.get(i);
						r.setRoleid(bd.getName("Role", "rolename","roleid", r.getRoleid()));
						r.setStorageid(bd.getName("Storageid","storagename","storageid",r.getStorageid()));
						r.setDutyid(bd.getName("Viewduty","dutyname","dutyid",r.getDutyid()));
						r.setClass1id(bd.getName("Class1", "class1name", "class1id", r.getClass1id()));
						String sql2="select grcname from view_LoginUser_grtype where grcid= '"+r.getGrcid()+"'";
                        List list2=hs.createSQLQuery(sql2).list();
                        if(list2.size()>0){
                        r.setGrcid(list2.get(0).toString());
                        }else{
                        r.setGrcid("");
                        }
					}
				}
				table.addAll(loglist);

				session.setAttribute("loginuserauditList", table);
				

				List grclist1=Grcnamelist1.getgrcnamelist2(hs,userInfo.getUserID());//所属分部
				List rresult = bd.getRoleList("Y");
				request.setAttribute("storageidList",bd.getStorageIDList());
				request.setAttribute("roleList", rresult);
				request.setAttribute("grclist", grclist1);

			}catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}

			
			forward = mapping.findForward("loginuserauditList");
		
		return forward;
	}
	
	/**
	 * Method toPrepareUpdateRecord execute,prepare data for update page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		// set navigation;
		/*String naigation = new String();*/
		/*this.setNavigation(request, "用户信息审核：查看");*/
		request.setAttribute("navigator.location","	用户信息审核>>查看");	
		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		
		
		Loginuser loginuser = null;
		if (id != null) {
			try {
				String auditdate=CommonUtil.getNowTime();
				
				hs = HibernateUtil.getSession();
				HttpSession session = request.getSession();
				loginuser = (Loginuser) hs.get(Loginuser.class, (String) dform.get("id"));
				ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
				if (loginuser == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("loginuser.display.recordnotfounterror"));
				} else {
					loginuser.setAuditdate(auditdate);
					loginuser.setAuditoperid(userInfo.getUserName());
					loginuser.setRoleid(bd.getName("Role", "rolename","roleid", loginuser.getRoleid()));
					loginuser.setStorageid(bd.getName("Storageid","storagename","storageid",loginuser.getStorageid()));
					loginuser.setDutyid(bd.getName("Viewduty","dutyname","dutyid",loginuser.getDutyid()));
					loginuser.setClass1id(bd.getName("Class1", "class1name", "class1id", loginuser.getClass1id()));
					//loginuser.setOperref(bd.getName("Loginuser","username","userid",loginuser.getOperref()));
					String sql2="select comname from company where comid= '"+loginuser.getGrcid()+"' order by comId";
                    List list2=hs.createSQLQuery(sql2).list();
                    if(list2.size()>0){
                    loginuser.setGrcid(list2.get(0).toString());
                    }else{
                    loginuser.setGrcid("");
                    }

				}
			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}

			request.setAttribute("loginuserBean", loginuser);
			forward = mapping.findForward("loginuserauditDisplay");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	public ActionForward toAuditRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;

		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			String date = CommonUtil.getNowTime();

			Loginuser loginuser = (Loginuser) hs.get(Loginuser.class, (String) dform.get("userid"));
			
			
			loginuser.setPhone(loginuser.getNewphone());
			loginuser.setEmail(loginuser.getNewemail());
			loginuser.setIdcardno(loginuser.getNweidcardno());
            loginuser.setAuditdate(date);
            loginuser.setAuditoperid(userInfo.getUserID());
			hs.save(loginuser);
			
			//更新人事档案管理
			String sqlup="from PersonnelManageMaster where ygid='"+loginuser.getUserid()+"'";
			List pmmlist=hs.createQuery(sqlup).list();
			if (pmmlist!=null && pmmlist.size()>0) {
				PersonnelManageMaster master=(PersonnelManageMaster)pmmlist.get(0);
				master.setIdCardNo(loginuser.getIdcardno()); //身份证号 IdCardNo
				master.setPhone(loginuser.getPhone()); //联系电话 phone
				master.setSector(bd.getName("Company","comname","comid",loginuser.getGrcid())); //所属分部 Sector
				master.setMaintStation(bd.getName("Storageid","storagename","storageid",loginuser.getStorageid())); //所属部门
				master.setEnabledFlag(loginuser.getEnabledflag()); //启用标志 EnabledFlag
				hs.save(master);
			}
			
			tx.commit();
		} catch (Exception e1) {
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			e1.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if(errors.isEmpty())
				{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				}
				else
				{
					request.setAttribute("error","Yes");
				}
				forward = mapping.findForward("returnList");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

}