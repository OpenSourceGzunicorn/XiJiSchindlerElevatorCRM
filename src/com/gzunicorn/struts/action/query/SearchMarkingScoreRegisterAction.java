package com.gzunicorn.struts.action.query;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.basedata.markingscore.MarkingScore;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchMarkingScoreRegisterAction extends DispatchAction {
Log log = LogFactory.getLog(SearchMarkingScoreRegisterAction.class);
	
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
		//SysRightsUtil.filterModuleRight(request, response, SysRightsUtil.NODE_ID_FORWARD + "", null);
		/** **********结束用户权限过滤*********** */

		// Set default method is toSearchRecord
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

		request.setAttribute("navigator.location","查询 >> 维保质量评分表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "searchMarkingScoreRegisterList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchMarkingScoreRegister");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("orderby");
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
		String elevatorType = request.getParameter("elevatorType");
		String mtIds=request.getParameter("QualityNos");
		List elevaorTypeList=bd.getPullDownList("ElevatorSalesInfo_type");
		if(mtIds==null || mtIds.trim().equals("")){
			mtIds=tableForm.getProperty("QualityNos");
		}else{
			tableForm.setProperty("QualityNos", mtIds);
		}
		
		if(elevatorType==null || elevatorType.trim().equals("")){
			elevatorType=tableForm.getProperty("elevatorType");
		}else{
			tableForm.setProperty("elevatorType", elevatorType);
		}
		Session hs = null;
		Query query = null;
		try {
			
			hs = HibernateUtil.getSession();

			String hql = "from MarkingScore where enabledFlag='Y' and elevatorType='"+elevatorType+"' ";
			
			
			if(mtIds!=null && !mtIds.equals("")){
				mtIds=URLDecoder.decode(mtIds, "utf-8");
				//System.out.println(mtIds);
				hql+=" and msId not in ("+mtIds.replace("|", "'")+")";
			}
			if (msId != null && !msId.equals("")) {
				hql += " and msId like '%"+msId.trim()+"%'";
			}
			if (msName != null && !msName.equals("")) {
				hql += " and msName like '%"+msName.trim()+"%'";
			}
			
			if (table.getIsAscending()) {
				hql += " order by "+ table.getSortColumn() +" asc";
			} else {
				hql += " order by "+ table.getSortColumn() +" desc";
			}

			query = hs.createQuery(hql);
			table.setVolume(query.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List list = query.list();
			List markingItemsList = new ArrayList();
			
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					MarkingScore mark=(MarkingScore) list.get(i);
					mark.setElevatorType(bd.getOptionName(mark.getElevatorType(), elevaorTypeList));
					markingItemsList.add(mark);
				}
			}
			table.addAll(markingItemsList);
			session.setAttribute("searchMarkingScoreRegisterList", table);

		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (HibernateException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		forward = mapping.findForward("searchMarkingScoreRegisterList");
		
		return forward;
	}
}
