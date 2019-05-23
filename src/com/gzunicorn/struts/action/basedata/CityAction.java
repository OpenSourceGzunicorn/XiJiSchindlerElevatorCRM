package com.gzunicorn.struts.action.basedata;
import java.io.IOException;
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
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.logic.MakeUpXML;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.city.City;
import com.gzunicorn.hibernate.basedata.province.Province;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class CityAction extends DispatchAction {

	Log log = LogFactory.getLog(CityAction.class);
	
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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "city", null);
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
//		this.setNavigation(request, "navigator.base.city.list");
		request.setAttribute("navigator.location", "区域维护-城市 >> 查询列表");
		ActionForward forward = null;

		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			/*try {

				response = toExcelRecord(form, request, response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "cityList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fCity");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("cityId");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String cityName = tableForm.getProperty("cityName");
			String cityId = tableForm.getProperty("cityId");
			String enabledflag = tableForm.getProperty("enabledflag");
			String rem = tableForm.getProperty("rem");
			String provinceName = tableForm.getProperty("province");
			Session hs = null;

			try {

				hs = HibernateUtil.getSession();

				Criteria criteria = hs.createCriteria(City.class);
				if (cityId != null && !cityId.equals("")) {
					criteria.add(Expression.like("cityId", "%" + cityId.trim()
							+ "%"));
				}
				if (cityName != null && !cityName.equals("")) {
					criteria.add(Expression.like("cityName", "%"
							+ cityName.trim() + "%"));
				}
				if (enabledflag != null && !enabledflag.equals("")) {
					criteria.add(Expression.eq("enabledFlag", enabledflag));
				}
				if (rem != null && !rem.equals("")) {
					criteria.add(Expression.eq("rem", rem));
				}
				if (provinceName != null && !provinceName.equals("")) {
					//criteria.add(Expression.eq("provinceName", provinceName));
					criteria.createAlias("province", "c").add(Restrictions.like("c.provinceName","%"+ provinceName + "%")); 
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

				List cityList = criteria.list();
				if (cityList != null) {
					for (int i = 0; i < cityList.size(); i++) {
						City c = (City)cityList.get(i);
						c.getProvince().getProvinceName();
//						//System.out.println(c.getProvince().getProvinceName());
					}
				}

				table.addAll(cityList);
				session.setAttribute("cityList", table);

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
			forward = mapping.findForward("cityList");
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

//		this.setNavigation(request, "navigator.base.city.view");
		request.setAttribute("navigator.location", "区域维护-城市 >> 查看");
		
		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		City city = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				city = (City) hs.get(City.class, (String) dform.get("id"));

				if (city == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"city.display.recordnotfounterror"));
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

			request.setAttribute("display", "yes");
			request.setAttribute("cityBean", city);
			forward = mapping.findForward("cityDisplay");

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

//		this.setNavigation(request, "navigator.base.city.add");
		request.setAttribute("navigator.location", "区域维护-城市 >> 添加");
		
		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
			dform.set("enabledflag", "Y");
		}
		dform.set("enabledflag", "Y");
		java.util.List cfresult = new java.util.ArrayList();
		try{
			cfresult=bd.getProvinceList("Y"); //省份
		}
		catch (Exception e) {
			log.error(e.getMessage());
			DebugUtil.print(e, "Get ProvinceList error!");
		}
		request.setAttribute("provinceList",cfresult);

		return mapping.findForward("cityAdd");
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
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
//			Province province = new Province();
			City city = new City();
			String cityId = bd.genCityNum((String) dform.get("provinceId"));
			city.setCityId(cityId);
			city.setCityName((String) dform.get("cityName"));
			city.setEnabledFlag((String) dform.get("enabledflag"));
			city.setRem((String) dform.get("rem"));
			Province province = (Province) hs.get(Province.class, (String) dform.get("provinceId"));
			city.setProvince(province);
			hs.save(city);
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"city.insert.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate City Insert error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate City Insert error!");
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
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
//		this.setNavigation(request, "navigator.base.city.modify");
		request.setAttribute("navigator.location", "区域维护-城市 >> 修改");
		ActionForward forward = null;
		String id = null;

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("cityId");
		} else {
			id = (String) dform.get("id");
		}

		Session hs = null;
		if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					Query query = hs
							.createQuery("from City c where c.cityId = :cityId");
					query.setString("cityId", id);
					java.util.List list = query.list();
					if (list != null && list.size() > 0) {
						City city = (City) list.get(0);
						dform.set("id", city.getCityId());
						dform.set("cityId", city.getCityId());
						dform.set("cityName", city.getCityName());
						dform.set("provinceId",city.getProvince().getProvinceId());
						dform.set("enabledflag", city.getEnabledFlag());
						dform.set("rem", city.getRem());
					} else {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"display.recordnotfounterror"));
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
			request.setAttribute("provinceList", bd.getProvinceList("Y"));
			forward = mapping.findForward("cityModify");
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
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			City city = (City) hs.get(City.class, (String) dform.get("id"));
			if (dform.get("id") != null
					&& dform.get("cityId") != null
					&& !((String) dform.get("id")).equals((String) dform.get("cityId"))) {
				hs.delete(city);
				city = new City();
			}
			city.setCityId((String) dform.get("cityId"));
			city.setCityName((String) dform.get("cityName"));
			city.setEnabledFlag((String) dform.get("enabledflag"));
			city.setRem((String) dform.get("rem"));
			Province province = (Province) hs.get(Province.class, (String) dform.get("provinceId"));
			city.setProvince(province);
			hs.save(city);

			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"city.update.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate City Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate City Update error!");
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
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
				"update.success"));
				forward = mapping.findForward("returnList");
			} else {
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"update.success"));
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
			City city = (City) hs.get(City.class, (String) dform.get("id"));
			if (city != null) {
				hs.delete(city);
				 errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"delete.succeed"));
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
			DebugUtil.print(e2, "Hibernate City Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate City Update error!");

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
	
	@SuppressWarnings("unchecked")
	/**
	 * ajax根据省份ID生成省份列表
	 */
	public ActionForward toCitySort(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String roots = "tables";

		MakeUpXML makexml = new MakeUpXML(roots);
		String key = request.getParameter("key");
		if (key == null || "".equals(key))
			return null;

		List cityList = bd.getCityList("Y", key);
		for (int i = 0; i < cityList.size(); i++) {
			City city = (City) cityList.get(i);
			if (city != null) {
				makexml.setCol(String.valueOf(i), "cityId", city.getCityId());
				makexml.setCol(String.valueOf(i), "cityName", city.getCityName());
			}
		}
		String xmlStr = makexml.getXml();
		response.setContentType("application/xml;charset=GBK");
		try {
			response.getWriter().write(xmlStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
