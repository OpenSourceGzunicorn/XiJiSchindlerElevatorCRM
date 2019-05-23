package com.gzunicorn.struts.action.query;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.gzunicorn.hibernate.basedata.markingscoredetail.MarkingScoreDetail;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchMarkingScoreRegisterDetailAction extends DispatchAction {
Log log = LogFactory.getLog(SearchMarkingScoreRegisterDetailAction.class);
	
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

		request.setAttribute("navigator.location","查询 >> 维保质量评分明细表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "searchMarkingScoreRegisterDetailList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchMarkingScoreRegisterDetail");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("detailId");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String detailId = tableForm.getProperty("detailId");
		String detailName = tableForm.getProperty("detailName");
		
		String msId=request.getParameter("msId");
		if(msId==null || "".equals(msId)){
			msId=tableForm.getProperty("msIds");
		}else{
			tableForm.setProperty("msIds", msId);
		}
		String detailIds=request.getParameter("QualityNos");
		
		if(detailIds==null || detailIds.trim().equals("")){
			detailIds=tableForm.getProperty("QualityNos");
		}else{
			tableForm.setProperty("QualityNos", detailIds);
		}
		
		
		Session hs = null;
		Query query = null;
		try {
			
			hs = HibernateUtil.getSession();
			String hql = "from MarkingScoreDetail where msId='"+msId.trim()+"'";
			
			if(detailIds!=null && !detailIds.equals("")){
				detailIds=URLDecoder.decode(detailIds, "utf-8");
				hql+=" and detailId not in ("+detailIds.replace("|", "'")+")";
			}
			if (detailId != null && !detailId.equals("")) {
				hql += " and detailId like '%"+detailId.trim()+"%'";
			}
			if (detailName != null && !detailName.equals("")) {
				hql += " and detailName like '%"+detailName.trim()+"%'";
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

			List markingItemsList = query.list();
//			MarkingScoreDetail mark=(MarkingScoreDetail) markingItemsList.get(0);
			

			table.addAll(markingItemsList);
			session.setAttribute("searchMarkingScoreRegisterDetailList", table);

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
		forward = mapping.findForward("searchMarkingScoreRegisterDetailList");
		
		return forward;
	}
}
