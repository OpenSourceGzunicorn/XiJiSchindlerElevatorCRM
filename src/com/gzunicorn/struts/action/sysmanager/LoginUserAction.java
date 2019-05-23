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
public class LoginUserAction extends DispatchAction{
	
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
	 * Method toPrepareAddRecord execute,prepare data for add page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionForward toPrepareAddRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		String naigation = new String();
		this.setNavigation(request, "navigator.base.loginuser.add");

		DynaActionForm dform = (DynaActionForm) form;
		if(request.getAttribute("error") == null || request.getAttribute("error").equals(""))
		{
			dform.initialize(mapping);
			dform.set("enabledflag","Y");
		}
		//java.util.List aresult = new java.util.ArrayList();
		java.util.List rresult = new java.util.ArrayList();

		try {
			//aresult = bd.getAreaList("Y");
			rresult = bd.getRoleList("Y");
		} catch (Exception e) {
			log.error(e.getMessage());
			DebugUtil.print(e, "Get PageList error!");
		}
		//request.setAttribute("areaList", aresult);
		
		request.setAttribute("roleList", rresult);
		request.setAttribute("viewdutyList",bd.getViewDutyList());
		Session hs1 = null;
		Query query1=null;
		try {
			hs1 = HibernateUtil.getSession();
		String sql="from Class1 a where a.enabledFlag='Y' order by levelid";
		query1=hs1.createQuery(sql);
		List list=query1.list();
		List grclist1=new ArrayList();
		String sql1="select ComID,ComFullName from Company where enabledflag='Y' order by comId";
        List grclist=hs1.createSQLQuery(sql1).list();
        HashMap hm1 = new HashMap();
        
        if(grclist != null){
	        for(int i=0;i<grclist.size();i++){
	        	Object[] obj=(Object[])grclist.get(i);
	        	hm1=new HashMap();
	        	hm1.put("grcid", String.valueOf(obj[0]));
	        	hm1.put("grcname",String.valueOf(obj[1]));
	        	grclist1.add(hm1);	
	        }
        	hm1 = (HashMap) grclist1.get(0);
        }

		request.setAttribute("class1List",list);
		request.setAttribute("grcidlist",grclist1);
		request.setAttribute("storageidList",bd.getStorageIDList(String.valueOf(hm1.get("grcid"))));
		
		} catch (DataStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			hs1.close();
		}
		return mapping.findForward("loginuserAdd");
	}
	
	/**
	 * Method toAddRecord execute,Add data to database and return list page or
	 * add page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toAddRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			Loginuser loginuser = new Loginuser();
			loginuser.setUserid((String) dform.get("userid").toString().trim());
			loginuser.setUsername((String) dform.get("username").toString().trim());
			loginuser.setPasswd(new CryptUtil().decode((String) dform.get("passwd"), "LO"));
			loginuser.setStorageid((String)dform.get("storageid"));
			loginuser.setClass1id((String) dform.get("class1id"));
			loginuser.setRoleid((String)dform.get("roleid"));
			loginuser.setDutyid("");//(String)dform.get("dutyid")
			//loginuser.setOperref((String) dform.get("operref"));
			loginuser.setPhone((String)dform.get("phone").toString().trim());
			loginuser.setFax((String)dform.get("fax").toString().trim());
			loginuser.setEmail((String)dform.get("email").toString().trim());
			loginuser.setEnabledflag((String) dform.get("enabledflag"));
			loginuser.setRem1((String) dform.get("rem1").toString().trim());
			loginuser.setGrcid((String)dform.get("grcid"));
			loginuser.setIdcardno((String)dform.get("idcardno").toString().trim());
			loginuser.setPhoneimei((String)dform.get("phoneimei").toString().trim());
			hs.save(loginuser);
			
			//更新人事档案管理
			String sqlup="from PersonnelManageMaster where ygid='"+loginuser.getUserid()+"'";
			List pmmlist=hs.createQuery(sqlup).list();
			if (pmmlist!=null && pmmlist.size()>0) {
				PersonnelManageMaster master=(PersonnelManageMaster)pmmlist.get(0);
				//master.setIdCardNo(loginuser.getIdcardno()); //身份证号 IdCardNo
				//master.setPhone(loginuser.getPhone()); //联系电话 phone
				master.setSector(bd.getName("Company","comname","comid",loginuser.getGrcid())); //所属分部 Sector
				master.setMaintStation(bd.getName("Storageid","storagename","storageid",loginuser.getStorageid())); //所属部门
				master.setEnabledFlag(loginuser.getEnabledflag()); //启用标志 EnabledFlag
				hs.save(master);
			}
			
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"loginuser.insert.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Loginuser Insert error!");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Loginuser Insert error!");
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
				// return addnew page
				if(errors.isEmpty())
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"insert.success"));
				}
				else
				{
					request.setAttribute("error","Yes");
				}
				forward = mapping.findForward("returnAdd");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return forward;
	}
	
	
	/**
	 * Method toUpdateRecord execute,Update data to database and return list
	 * page or modifiy page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward toUpdateRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			Loginuser loginuser = (Loginuser) hs.get(Loginuser.class, (String) dform.get("id"));
			if(dform.get("id") != null && dform.get("userid") != null && !((String)dform.get("id")).equals((String)dform.get("userid")))
			{
				hs.delete(loginuser);
				loginuser = new Loginuser();
			}
			loginuser.setUserid((String) dform.get("userid").toString().trim());
			loginuser.setUsername((String) dform.get("username").toString().trim());
			//loginuser.setPasswd(new CryptUtil().decode((String) dform.get("passwd"), "LO"));
			String ispasswd=(String)dform.get("ispasswd");
			if("Y".equalsIgnoreCase(ispasswd)){
				loginuser.setPasswd(new CryptUtil().decode((String) dform.get("passwd"), "LO"));
			}
			
			loginuser.setStorageid((String)dform.get("storageid"));
			loginuser.setClass1id((String) dform.get("class1id"));
			loginuser.setRoleid((String)dform.get("roleid"));
			//loginuser.setDutyid((String)dform.get("dutyid"));
			//loginuser.setOperref((String) dform.get("operref"));
			loginuser.setPhone((String)dform.get("phone").toString().trim());
			loginuser.setFax((String)dform.get("fax").toString().trim());
			loginuser.setEmail((String)dform.get("email").toString().trim());
			loginuser.setEnabledflag((String) dform.get("enabledflag"));
			loginuser.setRem1((String) dform.get("rem1").toString());
			loginuser.setGrcid(request.getParameter("grcid"));
			loginuser.setIdcardno((String)dform.get("idcardno").toString().trim());
			loginuser.setPhoneimei((String)dform.get("phoneimei").toString().trim());
			hs.save(loginuser);
			
			//更新人事档案管理
			String sqlup="from PersonnelManageMaster where ygid='"+loginuser.getUserid()+"'";
			List pmmlist=hs.createQuery(sqlup).list();
			if (pmmlist!=null && pmmlist.size()>0) {
				PersonnelManageMaster master=(PersonnelManageMaster)pmmlist.get(0);
				//master.setIdCardNo(loginuser.getIdcardno()); //身份证号 IdCardNo
				//master.setPhone(loginuser.getPhone()); //联系电话 phone
				master.setSector(bd.getName("Company","comname","comid",loginuser.getGrcid())); //所属分部 Sector
				master.setMaintStation(bd.getName("Storageid","storagename","storageid",loginuser.getStorageid())); //所属部门
				master.setEnabledFlag(loginuser.getEnabledflag()); //启用标志 EnabledFlag
				hs.save(master);
			}
			
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"loginuser.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Loginuser Update error!");
		}
		catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Loginuser Update error!");
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
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
				"update.success"));
				}
				else
				{
					request.setAttribute("error","Yes");
				}
				forward = mapping.findForward("returnModify");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
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

	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		// set navigation;
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		String naigation = new String();
		this.setNavigation(request, "navigator.base.loginuser.modify");
		ActionForward forward = null;
		String id = null;
		if(dform.get("isreturn") != null && ((String)dform.get("isreturn")).equals("N"))
		{
			id = (String) dform.get("userid");	
		}
		else
		{
			id = (String) dform.get("id");
		}
		Session hs = null;
		Loginuser loginuser = new Loginuser();
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs
						.createQuery("from Loginuser a where a.userid = :userid");
				query.setString("userid", id);
				java.util.List list = query.list();
				if (list != null && list.size() > 0) {
					loginuser = (Loginuser) list.get(0);
					dform.set("id", loginuser.getUserid());
					dform.set("userid", loginuser.getUserid());
					dform.set("username", loginuser.getUsername());
					dform.set("passwd", loginuser.getPasswd());
					dform.set("storageid", loginuser.getStorageid());
					dform.set("class1id", loginuser.getClass1id());
					dform.set("roleid", loginuser.getRoleid());
					dform.set("ispasswd","");
					dform.set("dutyid", loginuser.getDutyid());
					//dform.set("operref", loginuser.getOperref());
					dform.set("phone", loginuser.getPhone());
					dform.set("fax", loginuser.getFax());
					dform.set("email", loginuser.getEmail());
					dform.set("rem1", loginuser.getRem1());
					dform.set("enabledflag", loginuser.getEnabledflag());
					dform.set("grcid", loginuser.getGrcid());
					dform.set("idcardno", loginuser.getIdcardno());
					dform.set("phoneimei", loginuser.getPhoneimei());
					
					List grclist1=new ArrayList();
					String sql1="select ComID,ComFullName from Company where enabledflag='Y' order by comId";
	                List grclist=hs.createSQLQuery(sql1).list();
	                for(int i=0;i<grclist.size();i++){
	                	Object[] obj=(Object[])grclist.get(i);
	                	HashMap hm1=new HashMap();
	                	hm1.put("grcid", String.valueOf(obj[0]));
	                	hm1.put("grcname",String.valueOf(obj[1]));
	                	grclist1.add(hm1);
	                }
					request.setAttribute("grcidlist", grclist1);
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"loginuser.display.recordnotfounterror"));
				}

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}

			request.setAttribute("storageidList",bd.getStorageIDList(String.valueOf(loginuser.getGrcid())));
			request.setAttribute("roleList", bd.getRoleList("Y"));
			request.setAttribute("viewdutyList",bd.getViewDutyList());

			Session hs1 = null;
			Query query1=null;
			try {
				hs1 = HibernateUtil.getSession();
			String sql="from Class1 a where a.enabledFlag='Y' order by levelid";
			query1=hs1.createQuery(sql);
			////System.out.println(query1.list().size());
			List list=query1.list();
			request.setAttribute("class1List",list);
			} catch (DataStoreException e) {
				e.printStackTrace();
			}finally{
				
				hs1.close();
			}
			forward = mapping.findForward("loginuserModify");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	
	/**
	 * Method toDeleteRecord execute, Delete record where roleid = id
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDeleteRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			Loginuser loginuser = (Loginuser) hs.get(Loginuser.class, (String) dform.get("id"));
			if(loginuser != null)
			{
			 hs.delete(loginuser);
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"loginuser.delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Loginuser Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Loginuser Update error!");

		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		ActionForward forward = null;
		try {
			forward = mapping.findForward("returnList");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return forward;
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
		String naigation = new String();
		this.setNavigation(request, "navigator.base.loginuser.list");
		ActionForward forward = null;
		// listRecord
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				
				response = toExcelRecord(form,request,response);
				forward = mapping.findForward("exportExcel");
				tableForm.setProperty("genReport","");
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "loginuserList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fLoginUser");
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
			String phone=tableForm.getProperty("phone");
			
			Session hs = null;
            List grclist1=new ArrayList();
			try {

				hs = HibernateUtil.getSession();
				grclist1=Grcnamelist1.getgrcnamelist2(hs,userInfo.getUserID());//所属分部
				Criteria criteria = hs.createCriteria(Loginuser.class);
				if (userid != null && !userid.equals("")) {
					criteria.add(Expression.like("userid", "%" + userid.trim() + "%"));
				}
				if (username != null && !username.equals("")) {
					criteria.add(Expression.like("username", "%" + username.trim()
							+ "%"));
				}

				if (phone != null && !phone.equals("")) {
					criteria.add(Expression.like("phone", "%" + phone.trim()
							+ "%"));
				}
				
				if (enabledflag != null && !enabledflag.equals("")) {
					criteria.add(Expression.eq("enabledflag", enabledflag));
				}
				/*if (storageid != null && !storageid.equals("")) {
					criteria.add(Expression.eq("storageid", storageid.trim()));
				}
				if (roleid != null && !roleid.equals("")) {
					criteria.add(Expression.eq("roleid", roleid.trim()));
				}*/
				if (storageid != null && !storageid.equals("")) {
					criteria.add(Expression.like("storageid", storageid.trim()+"%"));
				}
				if (roleid != null && !roleid.equals("")) {
					criteria.add(Expression.like("roleid", roleid.trim()));
				}
				if(grclist1!=null && grclist1.size()>1){
                 if(grcid != null && !grcid.equals("")){
                	 criteria.add(Expression.like("grcid", grcid.trim()));
                 }
                 }else{
                	 HashMap hm=new HashMap();
                	 hm=(HashMap)grclist1.get(0);
                	 grcid=String.valueOf(hm.get("grcid"));
                	 criteria.add(Expression.like("grcid", grcid.trim()));
                 }
				if (table.getIsAscending()) {
					criteria.addOrder(Order.asc(table.getSortColumn()));
				} else {
					criteria.addOrder(Order.desc(table.getSortColumn()));
				}

				table.setVolume(criteria.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				criteria.setFirstResult(table.getFrom()); // pagefirst
				criteria.setMaxResults(table.getLength());

				cache.check(table);

				List loginuser = criteria.list();
				BaseDataImpl bd = new BaseDataImpl();

				if (loginuser != null) {
					int len = loginuser.size();
					for (int i = 0; i < len; i++) {
						Loginuser r = (Loginuser) loginuser.get(i);
						/*r.setCompanyid(bd.getName("Company", "companyname",
								"companyid", r.getCompanyid()));*/
						r.setRoleid(bd.getName("Role", "rolename",
								"roleid", r.getRoleid()));
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
				table.addAll(loginuser);
		
				session.setAttribute("loginuserList", table);
				request.setAttribute("grclist", grclist1);

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

			java.util.List rresult = new java.util.ArrayList();

			try {
				
				rresult = bd.getRoleList("Y");
			} catch (Exception e) {
				log.error(e.getMessage());
				DebugUtil.print(e, "Get PageList error!");
			}
			
			request.setAttribute("storageidList",bd.getStorageIDList());
			request.setAttribute("roleList", rresult);
			forward = mapping.findForward("loginuserList");
		}
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
		String naigation = new String();
		this.setNavigation(request, "navigator.base.loginuser.view");

		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		Loginuser loginuser = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				loginuser = (Loginuser) hs.get(Loginuser.class, (String) dform.get("id"));

				if (loginuser == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"loginuser.display.recordnotfounterror"));
				} else {
					
					loginuser.setRoleid(bd.getName("Role", "rolename","roleid", loginuser.getRoleid()));
					loginuser.setStorageid(bd.getName("Storageid","storagename","storageid",loginuser.getStorageid()));
					loginuser.setDutyid(bd.getName("Viewduty","dutyname","dutyid",loginuser.getDutyid()));
					loginuser.setClass1id(bd.getName("Class1", "class1name", "class1id", loginuser.getClass1id()));
					loginuser.setOperref(bd.getName("Loginuser","username","userid",loginuser.getOperref()));
					String sql2="select comname from Company where comid= '"+loginuser.getGrcid()+"' order by comId";
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

			// if display = yes then the page just show the return button;
			request.setAttribute("display", "yes");
			request.setAttribute("loginuserBean", loginuser);
			forward = mapping.findForward("loginuserDisplay");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * Method toSearchRecord execute, to Excel Record 列表查询导出Excel
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException
	 */

	public HttpServletResponse toExcelRecord(ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		//HttpSession session = request.getSession();
		//ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		//String action = tableForm.getAction();

		String userid = tableForm.getProperty("userid");
		String username = tableForm.getProperty("username");
		String enabledflag = tableForm.getProperty("enabledflag");
		String storageid = tableForm.getProperty("storageid");
		String roleid = tableForm.getProperty("roleid");
		String grcid = tableForm.getProperty("grcid");
		String phone=tableForm.getProperty("phone");
		
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(Loginuser.class);
			if (userid != null && !userid.equals("")) {
				criteria.add(Expression.like("userid", "%" + userid.trim() + "%"));
			}
			if (username != null && !username.equals("")) {
				criteria.add(Expression.like("username", "%" + username.trim()+ "%"));
			}

			if (phone != null && !phone.equals("")) {
				criteria.add(Expression.like("phone", "%" + phone.trim()+ "%"));
			}
			
			if (enabledflag != null && !enabledflag.equals("")) {
				criteria.add(Expression.eq("enabledflag", enabledflag));
			}
			if (storageid != null && !storageid.equals("")) {
				criteria.add(Expression.like("storageid", storageid.trim()+"%"));
			}
			if (roleid != null && !roleid.equals("")) {
				criteria.add(Expression.like("roleid", roleid.trim()));
			}
             if(grcid != null && !grcid.equals("")){
            	 criteria.add(Expression.like("grcid", grcid.trim()));
             }

			criteria.addOrder(Order.asc("userid"));

			List roleList = criteria.list();

			XSSFSheet sheet = wb.createSheet("用户信息");
			
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"loginuser.userid"));
				
				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"loginuser.username"));
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"loginuser.storageid"));
		
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"loginuser.roleid"));
		
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"loginuser.dutyid"));
		
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"loginuser.phone"));
				
				cell0 = row0.createCell((short)6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"loginuser.fax"));

				cell0 = row0.createCell((short)7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"loginuser.email"));
				
				cell0 = row0.createCell((short)8);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("身份证号码");
				
				cell0 = row0.createCell((short)9);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("手机IMEI码");
				
				cell0 = row0.createCell((short)10);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("启用标志");
				
				cell0 = row0.createCell((short)11);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("所属分部");
				
				cell0 = row0.createCell((short)12);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"loginuser.rem1"));
				
				cell0 = row0.createCell((short)13);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"loginuser.class1id"));
				
				for (int i = 0; i < l; i++) {
					Loginuser loginuser = (Loginuser) roleList.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);
	
					// 创建Excel列
					XSSFCell cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(loginuser.getUserid());
					
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(loginuser.getUsername());
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					
					if(loginuser.getStorageid()!=null){
						cell.setCellValue(bd.getName("Storageid","storagename","storageid",loginuser.getStorageid()));
					}else{
						cell.setCellValue(loginuser.getStorageid());
					}
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					if(loginuser.getRoleid()!=null){
						cell.setCellValue(bd.getName("Role", "rolename","roleid", loginuser.getRoleid()));
					}else{
					cell.setCellValue(loginuser.getRoleid());
					}
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					
					if(loginuser.getDutyid()!=null){
						cell.setCellValue(bd.getName("Viewduty","dutyname","dutyid",loginuser.getDutyid()));
					}else{
						cell.setCellValue(loginuser.getDutyid());
					}
					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(loginuser.getPhone());
					
					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(loginuser.getFax());
					
					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(loginuser.getEmail());
					
					cell = row.createCell((short)8);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(loginuser.getIdcardno());

					cell = row.createCell((short)9);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(loginuser.getPhoneimei());
					
					cell = row.createCell((short)10);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(loginuser.getEnabledflag()));
					
					cell = row.createCell((short)11);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String sql2="select grcname from view_LoginUser_grtype  where grcid= '"+loginuser.getGrcid()+"'";
                    List list2=hs.createSQLQuery(sql2).list();
                    if(list2.size()>0){
                    	cell.setCellValue(list2.get(0).toString());
                    }else{
                    	cell.setCellValue("");
                    }
                    
					cell = row.createCell((short)12);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(loginuser.getRem1());
					
					cell = row.createCell((short)13);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					
					if(loginuser.getClass1id()!=null){
						cell.setCellValue(bd.getName("Class1", "class1name", "class1id", loginuser.getClass1id()));
					}else{
						cell.setCellValue(loginuser.getClass1id());
					}
				}
			}
			try {
			} catch (Exception e) {

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
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("用户信息", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	
	public void getStorageidList(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String maintDivision = request.getParameter("maintDivision"); // 所属分部

		if (maintDivision != null && !maintDivision.equals("")) {
			try {
				List storageidList = bd.getStorageIDList(maintDivision); 
				
				JSONArray jsonArr = new JSONArray();
		
				Storageid storageid = null;
				JSONObject jsonObj = null;
				for (Object object : storageidList) {
					storageid = (Storageid) object;
					jsonObj = new JSONObject();
					jsonObj.put("storageid", storageid.getStorageid());
					jsonObj.put("storagename", storageid.getStoragename());
					jsonArr.put(jsonObj);
				}
				
				ServletOutputStream stream = response.getOutputStream();
				stream.write(jsonArr.toString().getBytes("UTF-8"));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

	}
	/**
	 * 同步方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toSynchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, HibernateException {
		
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();

		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
			
		Session hs = null;
		Transaction tx = null;
			
		try {
			
			hs = HibernateUtil.getSession();		
			tx = hs.beginTransaction();

			//同步所属分部，所属部门 到人事档案
			String upsql="exec sp_SynchPersonnelManageMaster '"+userInfo.getUserID()+"'";
			hs.connection().prepareCall(upsql).execute();
			
			tx.commit();
			
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","同步成功！"));

		} catch (Exception e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","同步失败！"));
			
			if(tx!=null){
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if(hs != null){
				hs.close();				
			}				
		}
			
		if (!errors.isEmpty()){
			this.saveErrors(request, errors);
		}

		return mapping.findForward("returnList");

	}
	

}