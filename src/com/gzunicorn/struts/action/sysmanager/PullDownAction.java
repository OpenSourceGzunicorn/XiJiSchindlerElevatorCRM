package com.gzunicorn.struts.action.sysmanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.pdfprint.BarCodePrint;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.FileOperatingUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.sysmanager.pulldown.Pulldown;
import com.gzunicorn.hibernate.sysmanager.pulldown.PulldownId;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class PullDownAction extends DispatchAction {

	Log log = LogFactory.getLog(PullDownAction.class);

	BaseDataImpl bd = new BaseDataImpl();

	/**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response, SysRightsUtil.NODE_ID_FORWARD + "pulldown", null);
		/** **********结束用户权限过滤*********** */

		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			ActionForward forward = super.execute(mapping, form, request, response);
			return forward;
		}

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
		request.setAttribute("navigator.location", messages.getMessage(locale, navigation));
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

	public ActionForward toPrepareAddRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		request.setAttribute("navigator.location", "下拉框管理 >> 添加");
		
		DynaActionForm dform = (DynaActionForm) form;
		dform.set("enabledflag", "Y"); 
		//dform.set("orderby", 0); 
		return mapping.findForward("pulldownAdd");
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

	public ActionForward toAddRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, DataStoreException, ParseException {

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;

		String pullid = dform.get("pullid").toString().trim();
		String typeflag = dform.get("typeflag").toString().trim();
		String pullname = dform.get("pullname").toString().trim();
		String enabledflag = dform.get("enabledflag").toString();
		String pullrem = dform.get("pullrem").toString().trim();
		Integer orderby = (Integer)dform.get("orderby");		

		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			PulldownId pu = new PulldownId();
			Pulldown de = new Pulldown();
			pu.setPullid(pullid);
			pu.setTypeflag(typeflag);
			de.setId(pu);
			de.setEnabledflag(enabledflag);
			de.setPullname(pullname);
			de.setPullrem(pullrem);
			de.setOrderby(orderby);

			hs.save(de);

			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("unittype.insert.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate UnitType Insert error!");
		} catch (Exception e) {
			e.printStackTrace();
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
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
				} else {
					request.setAttribute("error", "Yes");
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
	public ActionForward toUpdateRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;

		String pullid = dform.get("pullid").toString().trim();
		String typeflag = dform.get("typeflag").toString().trim();
		String pullname = dform.get("pullname").toString().trim();
		String enabledflag = dform.get("enabledflag").toString();
		String pullrem = dform.get("pullrem").toString();
		Integer orderby = (Integer)dform.get("orderby");		

		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			if (pullid != null && pullid.length() > 0) {
				String delsql = "delete from Pulldown where pullid='" + pullid.trim() + "' and typeflag='" + typeflag.trim() + "'";
				hs.connection().prepareStatement(delsql).execute();
			}

			PulldownId pu = new PulldownId();
			Pulldown de = new Pulldown();
			pu.setPullid(pullid);
			pu.setTypeflag(typeflag);
			de.setId(pu);
			de.setEnabledflag(enabledflag);
			de.setPullname(pullname);
			de.setPullrem(pullrem);
			de.setOrderby(orderby);

			hs.save(de);

			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("unittype.insert.duplicatekeyerror"));
			e2.printStackTrace();
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate UnitType Insert error!");
		} catch (Exception e) {
			e.printStackTrace();
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
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));

				} else {
					request.setAttribute("error", "Yes");
				}
				request.setAttribute("pullid", pullid);
				request.setAttribute("type", typeflag);
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

	public ActionForward toPrepareUpdateRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		String naigation = new String();
		request.setAttribute("navigator.location", "下拉框管理 >> 修 改");

		ActionForward forward = null;
		String pullid = "";
		pullid = request.getParameter("pullid");
		if (pullid == null || pullid.trim().equals("")) {
			pullid = request.getAttribute("pullid").toString();
		}
		String typeid = "";
		typeid = request.getParameter("type");
		if (typeid == null || typeid.trim().equals("typeid")) {
			typeid = request.getAttribute("type").toString();
		}

		Session hs = null;
		List tablelist = new ArrayList();
		if (pullid != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from Pulldown a where a.id.pullid = :puid and a.id.typeflag= :typeid");
				query.setString("puid", pullid);
				query.setString("typeid", typeid);
				java.util.List list = query.list();
				if (list != null && list.size() > 0) {
					Pulldown no = (Pulldown) list.get(0);

					dform.set("pullid", no.getId().getPullid());
					dform.set("pullname", no.getPullname());
					dform.set("typeflag", no.getId().getTypeflag());
					dform.set("enabledflag", no.getEnabledflag());
					dform.set("pullrem", no.getPullrem());
					dform.set("orderby", no.getOrderby());

				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}

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

			forward = mapping.findForward("pulldownModify");
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * Method toDeleteRecord execute, Delete record where facid = id
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDeleteRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		String id = request.getParameter("pullid");
		String typeid = request.getParameter("type");

		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if (id != null && id.length() > 0) {
				String delsql = "delete from Pulldown where pullid='" + id.trim() + "' and typeflag='" + typeid.trim() + "'";
				hs.connection().prepareStatement(delsql).execute();

				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.delete.succeed"));
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.delete.failed"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate Commerceassaydetail Delete error!");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate Commerceassaydetail Delete error!");
		} finally {
			try {
				if (hs != null) {
					hs.close();
				}
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

		String naigation = new String();
		request.setAttribute("navigator.location", "下拉框管理 >> 查询列表");
		ActionForward forward = null;
		// listRecord
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null && !tableForm.getProperty("genReport").equals("")) {
			try {

				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "pulldList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fPullDown");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("id.typeflag,orderby asc");
			table.setIsAscending(true);
			cache.updateTable(table);
			
			/** 该方法是记住了历史查询条件*/
			if (action.equals(ServeTableForm.NAVIGATE) || action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			/**
			if (action.equals(ServeTableForm.NAVIGATE) || action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			 */
			
			String pullid = tableForm.getProperty("pullid");
			String typeflag = tableForm.getProperty("typeflag");
			String pullname = tableForm.getProperty("pullname");
			String enabledflag = tableForm.getProperty("enabledflag");
			String pullrem = tableForm.getProperty("pullrem");
			
			//String vcdates = tableForm.getProperty("vcdates");
			////System.out.println("日期："+vcdates);
			String order="";
			Session hs = null;
			Query query = null;
			try {

				hs = HibernateUtil.getSession();

				String sql = "from Pulldown where 1=1 ";

				if (pullid != null && !pullid.equals("")) {
					sql += " and id.pullid like '%" + pullid.trim() + "%'";
				}
				if (pullname != null && !pullname.equals("")) {
					sql += " and pullname like '%" + pullname.trim() + "%'";
				}
				if (typeflag != null && !typeflag.equals("")) {
					sql += " and id.typeflag like '%" + typeflag.trim() + "%'";
				}
				if (enabledflag != null && !enabledflag.equals("")) {
					sql += " and enabledflag like '%" + enabledflag + "%'";
				}
				if (pullrem != null && !pullrem.equals("")) {
					sql += " and pullrem like '%" + pullrem.trim() + "%'";
				}
				
				if (table.getIsAscending()) {
					order="order by "+table.getSortColumn();
				} else {
					order="order by "+table.getSortColumn()+" desc";
				}
				////System.out.println(sql+order);

				query = hs.createQuery(sql+order);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List ele = query.list();

				table.addAll(ele);
				session.setAttribute("pulldList", table);

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

			forward = mapping.findForward("pulldownList");
		}
		return forward;
	}
	
	
	 //打印功能
    public ActionForward toPrintRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    	ActionErrors errors = new ActionErrors();
			Session hs = null;
			Transaction tx = null;
			List barCodeList = new ArrayList();
			
			String pullid =request.getParameter("pullid");
			String typeid = request.getParameter("type");
		
		try {
			hs = HibernateUtil.getSession();
			 
			Query query = hs.createQuery("from Pulldown a where a.id.pullid = :puid and a.id.typeflag= :typeid");
			query.setString("puid", pullid);
			query.setString("typeid", typeid);
			List list = query.list();
			
			if(list!=null && list.size()>0){
				HashMap hmap=null;
				for(int i=0;i<list.size();i++){
					Pulldown pdown=(Pulldown)list.get(i);

					hmap=new HashMap();
					hmap.put("pullid", pdown.getId().getPullid());
					hmap.put("typeflag", pdown.getId().getTypeflag());
					hmap.put("pullname", pdown.getPullname());
					hmap.put("enabledflag", pdown.getEnabledflag());
					hmap.put("pullrem", pdown.getPullrem());
					
					barCodeList.add(hmap);
				}
				
				 //对barCodeList操作
				BarCodePrint dy = new BarCodePrint();
				dy.toPrintTwoRecord(request,response, barCodeList);//打印大标签
				
			}
			 

		} catch (Exception e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","打印失败！"));
			e.printStackTrace();
		}finally {
			try {
				if(hs!=null){
					hs.close();
				}
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}		
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return null;
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

	public ActionForward toPrepareUploadFile(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);

		request.setAttribute("navigator.location", "下拉框管理 >> 上传附件");
		// dform.initialize(mapping);

		ActionForward forward = null;

		Session hs = null;
		
		String billno = "12345678";
		
		try {
			hs = HibernateUtil.getSession();

			// 已上传的附件
			FileOperatingUtil ulf = new FileOperatingUtil();
			List updatefileList = ulf.display(hs, billno,"engineQuoteProcess");
			request.setAttribute("updatefileList", updatefileList);
			
			String hql="select a from Company a where a.enabledflag='Y' order by comid";
			List list=hs.createQuery(hql).list();
		    if(list!=null && list.size()>0){
		    	request.setAttribute("CompanyList", list);
		    }

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			DebugUtil.print(e, "Hibernate Commerceassaydetail Delete error!");
		} finally {
			try {
				if (hs != null) {
					hs.close();
				}
			} catch (HibernateException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				DebugUtil.print(e,
						"Hibernate Commerceassaydetail Delete error!");
			}
		}

		forward = mapping.findForward("pulldownUploadFile");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	public ActionForward toUploadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();
		DynaActionForm dform = (DynaActionForm) form;
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ActionErrors errors = new ActionErrors();

		Session hs = null;
		Transaction tx = null;

		boolean istrue = false;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			// 保存文件
			String path = "engineQuoteProcess.file.upload.folder";
			String tableName = "engineQuoteProcess";//表名
			String billno = "12345678";
			String userid = userInfo.getUserID();

			FileOperatingUtil foutil = new FileOperatingUtil();
			List fileInfoList = foutil.saveFile(dform, request,	response, path, billno);
			istrue=foutil.saveFileInfo(hs, fileInfoList, tableName,	billno, userid);

			if (istrue) {
				tx.commit();
			} else {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
				tx.rollback();
			}
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("insert.fail"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				e3.printStackTrace();
			}
			e2.printStackTrace();
		} finally {
			try {
				if (hs != null) {
					hs.close();
				}
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;
		forward = mapping.findForward("returnUploadFile");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return forward;
	}

	/**
	 * 删除文件
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDelFileRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		FileOperatingUtil uf = new FileOperatingUtil();
		uf.toDeleteFileRecord(mapping, form, request, response);
		return null;
	}
	/**
	 * 下载文件
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDownloadFileRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		FileOperatingUtil uf = new FileOperatingUtil();
		uf.toDownLoadFiles(mapping, form, request, response);
		return null;
	}
	
	
	public void toStorageIDList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		Session hs=null;
		String comid=request.getParameter("comid");
		response.setHeader("Content-Type","text/html; charset=GBK");
		List list2=new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='GBK'?>");
		sb.append("<root>");
		try {
			hs=HibernateUtil.getSession();
			if(comid!=null && !"".equals(comid)){
			String hql="select a from Storageid a,Company b where a.comid = b.comid and storagetype='1' and a.comid="+comid;
			List list=hs.createQuery(hql).list();
			if(list!=null && list.size()>0){
				sb.append("<rows>");
				for(int i=0;i<list.size();i++){
				Storageid sid=(Storageid)list.get(i);
				sb.append("<cols name='"+sid.getStoragename()+"' value='"+sid.getStorageid()+"'>").append("</cols>");
				}
				sb.append("</rows>");
							
				
			  }
			 }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		finally{
			hs.close();
		}
		sb.append("</root>");
		
		response.setCharacterEncoding("gbk"); 
		response.setContentType("text/xml;charset=gbk");
		response.getWriter().write(sb.toString());
			}
	
	
	//生成转化pdf的临时html
	public void createPdfFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String html = request.getParameter("test");
		String top = request.getParameter("top");
		String tabToolBar = request.getParameter("tabToolBar");
		String htmlName = request.getParameter("htmlName");	
		html = html.replace(top, "");
		html = html.replace(tabToolBar, "");
		
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) request.getSession().getAttribute(SysConfig.LOGIN_USER_INFO);
		String userid = userInfo.getUserID();
		saveAsFileWriter(html, userid, PropertiesUtil.getProperty("PdfHtml.file.upload.folder"),htmlName);
	}

	/**
	 * 
	 * @Description: 保存html文件
	 * @param @param content
	 * @param @param userid
	 * @param @param pdfPath   
	 * @return void  
	 * @throws
	 * @author gtc
	 * @date 2015-11-20
	 */
	
	private void saveAsFileWriter(String content, String userid, String pdfPath ,String htmlName) {
		/*
		 * 对html源码进行样式替换
		 */
		content = content.replace("class=table_outline3 height=22 ","class=table_outline3 ");
		content= content.replaceAll(".divtable table", ".tb");
		File file = new File(pdfPath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		String filepath = pdfPath + userid + htmlName;
		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter(filepath);
			fwriter.write(content);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
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

	public ActionForward toDisplayRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		String naigation = new String();
		request.setAttribute("navigator.location", "下拉框管理 >> 查看");

		ActionForward forward = null;
		String pullid = "";
		pullid = request.getParameter("pullid");
		if (pullid == null || pullid.trim().equals("")) {
			pullid = request.getAttribute("pullid").toString();
		}
		String typeid = "";
		typeid = request.getParameter("type");
		if (typeid == null || typeid.trim().equals("typeid")) {
			typeid = request.getAttribute("type").toString();
		}

		Session hs = null;
		List tablelist = new ArrayList();
		if (pullid != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from Pulldown a where a.id.pullid = :puid and a.id.typeflag= :typeid");
				query.setString("puid", pullid);
				query.setString("typeid", typeid);
				java.util.List list = query.list();
				if (list != null && list.size() > 0) {
					Pulldown no = (Pulldown) list.get(0);
                    HashMap map=new HashMap();
                    map.put("pullid", no.getId().getPullid());
                    map.put("pullname", no.getPullname());
                    map.put("typeflag", no.getId().getTypeflag());
                    map.put("enabledflag", no.getEnabledflag());
                    map.put("pullrem", no.getPullrem());
                    map.put("orderby", no.getOrderby());
                    request.setAttribute("pulldownBean", map);

				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}

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

			forward = mapping.findForward("pulldownDisplay");
		}

		request.setAttribute("display", "yes");
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
}
