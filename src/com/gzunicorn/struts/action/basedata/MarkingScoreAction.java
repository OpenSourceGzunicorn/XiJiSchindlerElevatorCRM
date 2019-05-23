package com.gzunicorn.struts.action.basedata;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
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
import org.hibernate.criterion.Restrictions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.markingscore.MarkingScore;
import com.gzunicorn.hibernate.basedata.markingscoredetail.MarkingScoreDetail;
import com.gzunicorn.hibernate.custregistervisitplan.customervisitplanmaster.CustomerVisitPlanMaster;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class MarkingScoreAction extends DispatchAction {

	Log log = LogFactory.getLog(MarkingScoreAction.class);
	
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
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,
				SysRightsUtil.NODE_ID_FORWARD + "markingscore", null);
		/** **********结束用户权限过滤*********** */

		// Set default method is toSearchRecord
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
	 * Method toSearchRecord execute, Search record
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("navigator.location","维保质量评分表>> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {

				response = toExcelRecord(form, request, response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "markingScoreList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fMarkingScore");//分页用的
			table.setLength(SysConfig.HTML_TABLE_LENGTH);//每页显示的行数
			cache.updateTable(table);
			table.setSortColumn("orderby");//默认的排序字段
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			
			cache.saveForm(tableForm);

			String msId = tableForm.getProperty("msId");
			String msName = tableForm.getProperty("msName");
			String enabledFlag = tableForm.getProperty("enabledFlag");
			String elevatorType = tableForm.getProperty("elevatorType");
			
			
			List elevaorTypeList=bd.getPullDownList("ElevatorSalesInfo_type");
			Session hs = null;

			try {
				hs = HibernateUtil.getSession();
				Criteria criteria = hs.createCriteria(MarkingScore.class);
				if (msId != null && !msId.equals("")) {
					criteria.add(Expression.like("msId", "%" + msId.trim()
							+ "%"));
				}
				if (msName != null && !msName.equals("")) {
					criteria.add(Expression.like("msName", "%"
							+ msName.trim() + "%"));
				}
				if (elevatorType != null && !elevatorType.equals("")) {
					criteria.add(Expression.eq("elevatorType", elevatorType));
				}
				if (enabledFlag != null && !enabledFlag.equals("")) {
					criteria.add(Expression.eq("enabledFlag", enabledFlag));
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

				List markingItemsList = new ArrayList();
				List list=criteria.list();
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						MarkingScore markingScore=(MarkingScore) list.get(i);
						markingScore.setElevatorType(bd.getOptionName(markingScore.getElevatorType(), elevaorTypeList));
						markingItemsList.add(markingScore);
					}
				}
				table.addAll(markingItemsList);
				session.setAttribute("markingScoreList", table);
				request.setAttribute("elevaorTypeList", elevaorTypeList);
				
			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (HibernateException e1) {

				e1.printStackTrace();
			} finally {
				try {
					hs.close();
					// HibernateSessionFactory.closeSession();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			forward = mapping.findForward("markingScoreList");
		}
		return forward;
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
	 * 点击查看的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		request.setAttribute("navigator.location","维保质量评分表 >> 查看");
		
		ActionForward forward = null;
		
		String id =  (String) dform.get("id");
		Session hs = null;
		MarkingScore markingScore = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("select m,p.pullname from MarkingScore m,Pulldown p where m.elevatorType=p.id.pullid and p.id.typeflag='ElevatorSalesInfo_type' and m.msId = :msId");
				query.setString("msId", id);
				List list = query.list();
				
				if (list != null && list.size() > 0) {
					Object[] objects=(Object[]) list.get(0);
					markingScore = (MarkingScore) objects[0];
					markingScore.setElevatorType((String) objects[1]);
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}

				if (markingScore == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"display.recordnotfounterror"));
				}
				 String hql="from MarkingScoreDetail msd where msd.markingScore.msId='"+id+"'";
				   List list2=hs.createQuery(hql).list();
				   List msdList=new ArrayList();
				   if(list2!=null&&list2.size()>0)
				   {
					   for(int i=0;i<list2.size();i++){
					   MarkingScoreDetail msd= (MarkingScoreDetail) list2.get(i);
                       msdList.add(msd);
					   }						   						 						   
				   }
				   request.setAttribute("msdList", msdList);
				   		
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

			request.setAttribute("display", "yes");
			request.setAttribute("markingScoreBean", markingScore);
			
			forward = mapping.findForward("markingScoreDisplay");

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareAddRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		request.setAttribute("navigator.location","维保质量评分表>> 添加");

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
			dform.set("enabledFlag", "Y");
		}
		dform.set("enabledFlag", "Y");

		//request.setAttribute("markingItemsList",bd.getPullDownList("enabledflag"));
		 request.setAttribute("elevaorTypeList", bd.getPullDownList("ElevatorSalesInfo_type"));
		return mapping.findForward("markingScoreAdd");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	
	public ActionForward toAddRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
 
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;

		String msId = (String) dform.get("msId");
		String msName = (String) dform.get("msName");
		double fraction =  (Double) dform.get("fraction");
		String enabledFlag = (String) dform.get("enabledFlag");
		String rem = (String) dform.get("rem");
		String elevatorType = (String) dform.get("elevatorType");
		Integer orderby=(Integer) dform.get("orderby");//排序号
		
	
		MarkingScore markingScore = new MarkingScore();
		String[] detailName=request.getParameterValues("detailName");
		String[] detailId=request.getParameterValues("detailId");
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			markingScore.setMsId(msId.trim());
			markingScore.setMsName(msName.trim());
			markingScore.setFraction(fraction);
			markingScore.setEnabledFlag(enabledFlag.trim());
			markingScore.setRem(rem.trim());
			markingScore.setElevatorType(elevatorType);
			markingScore.setOperId(userInfo.getUserID());//录入人
			markingScore.setOperDate(CommonUtil.getNowTime());//录入时间
			markingScore.setOrderby(orderby);//排序号
			hs.save(markingScore);
			if(detailId!=null && !"".equals(detailId)){
				for (int i = 0; i < detailId.length; i++) {
				MarkingScoreDetail markingScoreDetail=new MarkingScoreDetail();
				markingScoreDetail.setDetailId(detailId[i]);
				markingScoreDetail.setDetailName(detailName[i]);
				markingScoreDetail.setMsId(markingScore.getMsId());
				markingScoreDetail.setMarkingScore(markingScore);
				hs.save(markingScoreDetail);
				}
			}
			tx.commit();
		
		}catch(HibernateException e2){
			String sql="select ms.msId from MarkingScore ms";
			List list = hs.createSQLQuery(sql).list();
			if(list.contains(msId)==true&&detailId!=null){
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("markingitems.insert.duplicatekeyerror2"));
				
			}else{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("markingitems.insert.duplicatekeyerror1"));
				
			}
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Insert error!");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Insert error!");
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
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
				"insert.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"insert.success"));
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
	 * 跳转到修改级别页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","维保质量评分表 >> 修改");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		ActionForward forward = null;
		String id = null;

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id =  dform.get("msId")+"";
		} else

 {
			id = (String) dform.get("id");
		}
		
		Session hs = null;
		MarkingScore markingScore = null;
	
		if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					markingScore = (MarkingScore) hs.get(MarkingScore.class, (String) dform.get("id"));
					String sql ="from MarkingScoreDetail msd where msd.markingScore.msId in(select max(ms.msId) from MarkingScore ms where ms.msId ='"+id+"')";
					List list2=hs.createQuery(sql).list();
					   List msdList=new ArrayList();
					   if(list2!=null&&list2.size()>0)
					   {
						   for(int i=0;i<list2.size();i++){
						MarkingScoreDetail msd= (MarkingScoreDetail) list2.get(i);
						   msdList.add(msd);
						   }						   						 						   
					   }
					   request.setAttribute("msdList", msdList);
					   request.setAttribute("elevaorTypeList", bd.getPullDownList("ElevatorSalesInfo_type"));
					if (markingScore == null) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"markingitems.display.recordnotfounterror"
								));
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
						DebugUtil
								.print(hex, "HibernateUtil Hibernate Session ");
					}
				}
			}
			request.setAttribute("markingScoreBean", markingScore);
			forward = mapping.findForward("markingScoreModify");
}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	
	/**
	 * 紧急级别修改
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toUpdateRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String msId = (String) dform.get("msId");
		String msName = (String) dform.get("msName");
		Double fraction =  (Double) dform.get("fraction");
		String enabledFlag = (String) dform.get("enabledFlag");
		String rem = (String) dform.get("rem");
		String elevatorType = (String) dform.get("elevatorType");
		Integer  orderby=(Integer) dform.get("orderby");
		
		
		
		String[] detailName=request.getParameterValues("detailName");
		String[] detailId=request.getParameterValues("detailId");
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			MarkingScore markingScore = (MarkingScore) hs.get(MarkingScore.class,(String) dform.get("msId"));
			
			if(markingScore!=null){
			markingScore.setMsId(msId.trim());
			markingScore.setMsName(msName.trim());
			markingScore.setFraction(fraction);
			markingScore.setEnabledFlag(enabledFlag.trim());
			markingScore.setRem(rem.trim());
			markingScore.setElevatorType(elevatorType);
			markingScore.setOperId(userInfo.getUserID());//录入人
			markingScore.setOperDate(CommonUtil.getNowTime());//录入时间
			markingScore.setOrderby(orderby);
			hs.update(markingScore);
			}

			
			if(detailId!=null&&detailId.length>0){
				for (int i = 0; i < detailId.length; i++) {
					MarkingScoreDetail marksd=new MarkingScoreDetail();
					marksd.setDetailId(detailId[i]);
					marksd.setDetailName(detailName[i]);
					marksd.setMsId(markingScore.getMsId());
					marksd.setMarkingScore(markingScore);
				hs.update(marksd);
				}
			}
			
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("markingitems.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (Exception e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");
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
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
				} else {
					request.setAttribute("error", "Yes");
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
	 * 删除紧急级别
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
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
            String id= request.getParameter("id");
			MarkingScore markingScore = (MarkingScore) hs.get(MarkingScore.class, (String) dform.get("id"));
			
			if (markingScore != null) {
			  String sql="select * from MarkingScoreDetail msd where msd.msId='"+markingScore.getMsId().trim()+"'";
          	  List list = hs.createSQLQuery(sql).list();
          	if(list!=null&&list.size()>0)
          	{
          	
          		hs.createQuery("delete MarkingScoreDetail msd where msd.markingScore.msId='"+markingScore.getMsId()+"'").executeUpdate();
          		hs.flush();
  				hs.delete(markingScore);
          		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
  					"delete.succeed"));
          	}else{
          		hs.delete(markingScore);
				 errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"delete.succeed"));
			}
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");

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

		String naigation = new String();
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		String msId = tableForm.getProperty("msId");
		String msName = tableForm.getProperty("msName");
		String enabledFlag = tableForm.getProperty("enabledFlag");
		List elevatorList=bd.getPullDownList("ElevatorSalesInfo_type");
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(MarkingScore.class);
			if (msId != null && !msId.equals("")) {
				criteria.add(Expression.like("msId", "%" + msId.trim() + "%"));
			}
			if (msName != null && !msName.equals("")) {
				criteria.add(Expression.like("msId", "%" + msName.trim()
						+ "%"));
			}
			if (enabledFlag != null && !enabledFlag.equals("")) {
				criteria.add(Expression.eq("enabledFlag", enabledFlag));
			}

			criteria.addOrder(Order.asc("msId"));

			List roleList = criteria.list();

			XSSFSheet sheet = wb.createSheet("维保质量评分表");
			
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"markingitems.mtId"));

				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"markingitems.mtName"));
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"markingitems.fraction"));
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("电梯类型");
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"markingitems.enabledflag"));
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"markingitems.rem"));
				
				cell0 = row0.createCell((short)6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"markingitems.operId"));
				
				cell0 = row0.createCell((short)7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"markingitems.operDate"));
				
				for (int i = 0; i < l; i++) {
					MarkingScore markingScore = (MarkingScore)roleList.get(i); 
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);
	
					// 创建Excel列
					XSSFCell cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(markingScore.getMsId());
					
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(markingScore.getMsName());
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(markingScore.getFraction());
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getOptionName(markingScore.getElevatorType(), elevatorList));

					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(markingScore.getEnabledFlag()));
					
					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(markingScore.getRem());

					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName(hs, "LoginUser", "userName", "userId",markingScore.getOperId()));
					
					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(markingScore.getOperDate());

				}
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
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("维保质量评分表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}

}
