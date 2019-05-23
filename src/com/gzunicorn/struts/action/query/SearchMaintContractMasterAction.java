package com.gzunicorn.struts.action.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
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

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.basedata.markingitems.MarkingItems;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchMaintContractMasterAction extends DispatchAction {
Log log = LogFactory.getLog(SearchMaintContractMasterAction.class);
	
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

		request.setAttribute("navigator.location","查询 >> 维保合同信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "maintContractMasterList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchMaintContractMaster");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("elevatorNo");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		}else if (action.equals("Submit")) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String elevatorNo = tableForm.getProperty("elevatorNo");
		String maintContractNo = tableForm.getProperty("maintContractNo");
		String salesContractNo = tableForm.getProperty("salesContractNo");
		String projectName = tableForm.getProperty("projectName");
		String maintDivision = tableForm.getProperty("maintDivision");
		String maintPersonnel=tableForm.getProperty("maintPersonnel");
		String maintStation=tableForm.getProperty("maintStation");
		List elevaorTypeList=bd.getPullDownList("ElevatorSalesInfo_type");
		if(maintDivision == null || maintDivision==""){
			maintDivision = userInfo.getComID();
			if(maintDivision.equals("00")){
				maintDivision="%";
			}
		}
		String rowid = request.getParameter("rowidstr"); //已选择的电梯编号集合	
		
		if(rowid == null || rowid.trim().equals("")){
			rowid=tableForm.getProperty("rowidstr");
		}else{
			tableForm.setProperty("rowidstr",rowid);
		}
		
		
		Session hs = null;
		Query query = null;
		try {

			hs = HibernateUtil.getSession();

			//出已经下达的合同，是否签收不控制
			String sql="select m.maintContractNo,d.salesContractNo,d.projectName,m.maintDivision,"
					+ "d.assignedMainStation,d.elevatorNo,"
					+ "isnull(d.maintPersonnel,'') as maintPersonnel,"
					+ "isnull(l.username,'') as username,isnull(l.phone,'') as phone,"
					+ "d.rowid,d.elevatorType,s.storagename,c.comname, "
					+ "(select isnull(max(q.ChecksDateTime),'') from QualityCheckManagement q where q.elevatorNo=d.elevatorNo) as checksDateTime "
					+ "from MaintContractMaster m,"
					+ "MaintContractDetail d left join Loginuser l on d.maintPersonnel=l.userid,"
					+ "StorageID s,Company c "
					+ " where m.billNo=d.billNo and m.taskSubFlag='Y'"
					+ " and s.StorageID=d.assignedMainStation and m.maintDivision=c.comid "
					+ " and contractStatus in('ZB','XB') ";//ZB: 在保 XB: 续保
			
			if (rowid != null && !rowid.equals("")) {
				sql += " and d.rowid not in ("+rowid.replace("|", "'")+")";
			}	
			if(elevatorNo!=null && !elevatorNo.equals("")){
				sql+=" and d.elevatorNo like '%"+elevatorNo.trim()+"%'";
			}
			if(maintContractNo!=null && !maintContractNo.equals("")){
				sql+=" and m.maintContractNo like '%"+maintContractNo.trim()+"%'";
			}
			if(salesContractNo!=null && !salesContractNo.equals("")){
				sql+=" and d.salesContractNo like '%"+salesContractNo.trim()+"%'";
			}
			if(projectName!=null && !projectName.equals("")){
				sql+=" and d.projectName like '%"+projectName.trim()+"%'";
			}
			if(maintDivision!=null && !maintDivision.equals("")){
				sql+=" and m.maintDivision like '"+maintDivision.trim()+"'";
			}
			
			if(maintStation!=null && !maintStation.equals("")){
				sql+=" and d.assignedMainStation like '"+maintStation.trim()+"'";
			}
			
			if(maintPersonnel!=null && !maintPersonnel.equals("")){
				sql+=" and (d.maintPersonnel like '%"+maintPersonnel.trim()+"%' or l.username like '%"+maintPersonnel.trim()+"%')";
			}
			if (table.getIsAscending()) {
				sql += " order by "+ table.getSortColumn() +" asc";
			} else {
				sql += " order by "+ table.getSortColumn() +" desc";
			}
			
			//System.out.println(sql);
			
			query = hs.createSQLQuery(sql);
			table.setVolume(query.list().size());// 查询得出数据记录数;
			

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);
			List list=query.list();

			List masters=new ArrayList();
			for(Object object : list){
				Object[] values=(Object[])object;
				Map m=new HashMap();
				m.put("maintContractNo", values[0]);
				m.put("salesContractNo", values[1]);
				m.put("projectName", values[2]);
				m.put("maintDivision", values[12]);
				m.put("maintStation",values[11]);
				m.put("elevatorNo", values[5]);
				m.put("maintPersonnel", values[6]);
				m.put("maintPersonnelName", values[7]);
				m.put("personnelPhone", values[8]);
				m.put("rowid", values[9]);
				m.put("elevatorType", values[10]);
				m.put("elevatorTypeName", bd.getOptionName((String) values[10], elevaorTypeList));
				
				//上次督查日期
				m.put("checksDateTime", values[13]);
				
				masters.add(m);
			}

			table.addAll(masters);
			session.setAttribute("maintContractMasterList", table);
			request.setAttribute("maintDivisionList",Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
			
			if(maintDivision!=null && !maintDivision.equals("")){
				String hql="select a from Storageid a where a.comid= '"+maintDivision+"' " +
						"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";	
				//System.out.println(">>>"+hql);
				List mainStationList=hs.createQuery(hql).list();
				request.setAttribute("mainStationList", mainStationList);
			}

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
		
		String flag = request.getParameter("flag");
		if ("update".equals(flag)) {
			forward = mapping.findForward("searchMaintContractMasterList2");
		}else {
			forward = mapping.findForward("searchMaintContractMasterList");
		}
		
		return forward;
	}
	/**
	 * ajax 级联 分部与维保站
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public void toStorageIDList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		Session hs=null;
		String comid=request.getParameter("comid");
		response.setHeader("Content-Type","text/html; charset=GBK");
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='GBK'?>");
		sb.append("<root>");
		try {
			hs=HibernateUtil.getSession();
			if(comid!=null && !"".equals(comid)){
				String hql="select a from Storageid a where a.comid='"+comid+"' " +
						"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
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
}
