package com.gzunicorn.struts.action.query;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.basedata.handoverelevatorcheckitem.HandoverElevatorCheckItem;
import com.gzunicorn.hibernate.basedata.handoverelevatorcheckitem.HandoverElevatorCheckItemId;
import com.gzunicorn.hibernate.infomanager.handoverelevatorcheckitemregister.HandoverElevatorCheckItemRegister;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchHandoverElevatorCheckItemAction extends DispatchAction {

	Log log = LogFactory.getLog(SearchHandoverElevatorCheckItemAction.class);
	
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

		response.setCharacterEncoding("GBK");
		request.setAttribute("navigator.location","查询>> 检查电梯问题");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		HTMLTableCache cache = new HTMLTableCache(session, "searchHandoverElevatorCheckItemList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchHandoverElevatorCheckItem");
		//table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setLength(1000);
		table.setSortColumn("h.orderby");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else if (action.equals("Submit")) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String checkItem = tableForm.getProperty("checkItem");
		String issueCoding = tableForm.getProperty("issueCoding");
		String examType=tableForm.getProperty("examType");
		String issueContents = tableForm.getProperty("issueContents");
		
		String elevatorType=request.getParameter("elevatorType");
		if(elevatorType==null || elevatorType.trim().equals("")){
			elevatorType=tableForm.getProperty("elevatorType");
		}else{
			tableForm.setProperty("elevatorType", elevatorType);
		}
		String issueCodings = request.getParameter("issueCodings");
		if(issueCodings == null || issueCodings.trim().equals("")){
			issueCodings=tableForm.getProperty("issueCodings");
		}else{
			tableForm.setProperty("issueCodings",issueCodings);
		}
		
		String condition="";
		try {
			if(issueCodings!=null && !issueCodings.equals("")){
				issueCodings=URLDecoder.decode(issueCodings, "utf-8");
				String[] codings=issueCodings.split("[|]");
				
				for(int i=0;i<codings.length;i++){
					condition+=i==codings.length-1 ? ("'"+codings[i]+"'") : ("'"+codings[i]+"',");
				}
			}
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		
		Session hs = null;
		Query query = null;
		try {
			
			hs = HibernateUtil.getSession();
			String sql = "select h.examType,h.checkItem,h.issueCoding,h.issueContents,"
					+ "h.elevatorType,p1.pullname as examTypeName,p2.pullname as elevatorTypeName,h.itemgroup "
					+ "from HandoverElevatorCheckItem h "
					+ "left join Pulldown p1 on p1.pullid=h.examType and p1.typeflag='HandoverElevatorCheckItem_ExamType' "
					+ "left join Pulldown p2 on p2.pullid=h.elevatorType and p2.typeflag='ElevatorSalesInfo_type' ";

			String condition1="where h.enabledFlag='Y' ";
			if (condition != null && !condition.equals("")) {
				condition1+= "and h.issueCoding not in ("+condition+")";
			}
			if (examType != null && !examType.equals("")) {
				condition1+=" and h.examType like '%"+examType.trim()+"%'";
			}
			if (checkItem != null && !checkItem.equals("")) {
				condition1+=" and h.checkItem like '%"+checkItem.trim()+"%'";
			}
			if (issueCoding != null && !issueCoding.equals("")) {
				condition1+=" and h.issueCoding like '%"+issueCoding.trim()+"%'";
			}
			if (issueContents != null && !issueContents.equals("")) {
				condition1+=" and h.issueContents like '%"+issueContents.trim()+"%'";
			}
			if (elevatorType != null && !elevatorType.equals("")) {
				condition1+=" and h.elevatorType like '"+elevatorType.trim()+"'";
			}
			
			if (table.getIsAscending()) {
				condition1 += " order by "+ table.getSortColumn() +" asc";
			} else {
				condition1 += " order by "+ table.getSortColumn() +" asc";
				
			}

			////System.out.println(">>>>>>>>"+sql+condition1);
			
			query = hs.createSQLQuery(sql+condition1);
			table.setVolume(query.list().size());// 查询得出数据记录数;
			

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List handoverElevatorCheckItemList=new ArrayList(); 
			List list= query.list();
			for(Object object : list){
				Object[] heci=(Object[])object;
				Map map=new HashMap();
				map.put("examType", heci[0]);
				//map.put("examTypeName", bd.getName("Pulldown", "pullname", "id.pullid", heci[0].toString(), typeflag));
				map.put("examTypeName", heci[5]);
				map.put("checkItem", heci[1]);
				map.put("issueCoding", heci[2]);
				map.put("issueContents1",heci[3]);
				//map.put("elevatorType",bd.getName("Pulldown", "pullname", "id.pullid", heci[4].toString(), flag));
				map.put("elevatorType",heci[6]);
				map.put("itemgroup",heci[7]);
				handoverElevatorCheckItemList.add(map);
			}

			table.addAll(handoverElevatorCheckItemList);
			session.setAttribute("searchHandoverElevatorCheckItemList", table);
			request.setAttribute("examTypeList", bd.getPullDownList("HandoverElevatorCheckItem_ExamType"));

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
		forward = mapping.findForward("searchHandoverElevatorCheckItemList");
		
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

	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("navigator.location","安装维保交接电梯情况登记 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		/*if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form,request,response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport","");

		} else {*/
			HTMLTableCache cache = new HTMLTableCache(session, "elevatorTransferCaseRegisterCopyList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fElevatorTransferCaseRegisterCopy");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("billNo");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			}else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			}  else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String elevatorType=request.getParameter("elevatorType");
			if(elevatorType==null || elevatorType.trim().equals("")){
				elevatorType=tableForm.getProperty("elevatorType");
			}else{
				tableForm.setProperty("elevatorType", elevatorType);
			}
			String billNo = tableForm.getProperty("billno");// 流水号
			String projectName = tableForm.getProperty("projectName");// 项目名称
			/*String staffName = tableForm.getProperty("staffName");// 厂检人员名称
*/			String department = tableForm.getProperty("department");// 所属部门					
			String processStatus = tableForm.getProperty("processStatus");// 
			String salesContractNo = tableForm.getProperty("salesContractNo");// 销售合同号	
			String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号	
			String insCompanyName = tableForm.getProperty("insCompanyName");// 安装单位名称
			/*String factoryCheckResult=tableForm.getProperty("factoryCheckResult");
			String checkNum=tableForm.getProperty("checkNum");*/
			
			
			Session hs = null;
			Query query = null;
			try {

				hs = HibernateUtil.getSession();

				String sql = "select a.billno,a.checkTime,a.checkNum,a.projectName,a.insCompanyName," +
						"a.salesContractNo,a.elevatorNo,lu.username,a.department,a.factoryCheckResult," +
						"a.submitType,a.status,a.processStatus,a.isClose,a.processName"+
						" from ElevatorTransferCaseRegister a,Loginuser lu "+
						" where a.submitType in ('Y','Z') and a.staffName=lu.userid " +
						" and a.staffName='"+userInfo.getUserID()+"'"+
						" and a.processStatus in('1','2','3') and a.elevatorType='"+elevatorType+"' ";
				
				if (billNo != null && !billNo.equals("")) {
					sql += " and billno like '%"+billNo.trim()+"%'";
				}
				if (projectName != null && !projectName.equals("")) {
					sql += " and projectName like '%"+projectName.trim()+"%'";
				}
				/*if (staffName != null && !staffName.equals("")) {
					sql+=" and (lu.username like '%"+staffName.trim()+"%' or lu.userid like '%"+staffName.trim()+"%' )";
				}*/
				if (department != null && !department.equals("")) {
					sql += " and department like '%"+department.trim()+"%'";
				}
				if (salesContractNo != null && !salesContractNo.equals("")) {
					sql += " and salesContractNo like '%"+salesContractNo.trim()+"%'";
				}
				if (elevatorNo != null && !elevatorNo.equals("")) {
					sql += " and elevatorNo like '%"+elevatorNo.trim()+"%'";
				}
				if (insCompanyName != null && !insCompanyName.equals("")) {
					sql += " and insCompanyName like '%"+insCompanyName.trim()+"%'";
				}
				if (processStatus != null && !processStatus.equals("")) {
					sql += " and processStatus like '%"+processStatus.trim()+"%'";
				}
				/*if (factoryCheckResult != null && !factoryCheckResult.equals("")) {
					sql+=" and etcr.factoryCheckResult='"+factoryCheckResult.trim()+"'";
			    }
				if (checkNum != null && !checkNum.equals("")) {
					if(checkNum.equals("最新厂检"))
					sql+=" and etcr.checkNum=(select max(checkNum) from ElevatorTransferCaseRegister)";
			    }*/
				
				if (table.getIsAscending()) {
					sql += " order by "+ table.getSortColumn() +" desc";
				} else {
					sql += " order by "+ table.getSortColumn() +" asc";		
				}
				
				query = hs.createQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List elevatorTransferCaseRegisterList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map map=new HashMap();
					map.put("billno", objs[0]);
					map.put("checkTime", objs[1]);
					map.put("checkNum", objs[2]);
					map.put("projectName", objs[3]);
					map.put("insCompanyName", objs[4]);
					map.put("salesContractNo", objs[5]);
					map.put("elevatorNo", objs[6]);
					map.put("staffName", objs[7]);
					map.put("department", bd.getName_Sql("Company", "comname", "comid",objs[8].toString()));
					map.put("factoryCheckResult", objs[9]);
					map.put("submitType", objs[10]);
					map.put("status", bd.getName_Sql("ViewFlowStatus", "typename", "typeid", String.valueOf(objs[11])));
					map.put("processStatus", objs[12]);
					map.put("isClose", objs[13]);
					map.put("processName", objs[14]);
					elevatorTransferCaseRegisterList.add(map);
				}

				String sql1="from Company where (comtype='1' or comtype='2') and enabledflag='Y' order by comid desc";				
			    List list1=hs.createQuery(sql1).list();
				
				table.addAll(elevatorTransferCaseRegisterList);
				session.setAttribute("elevatorTransferCaseRegisterCopyList", table);
				request.setAttribute("departmentList", list1);
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
			forward = mapping.findForward("searchHandoverElevatorCheckItemCopyList");
		/*}*/
		return forward;
	}
	
	public void toStorageIDList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		Session hs=null;
		String id=request.getParameter("id");
		response.setHeader("Content-Type","text/html; charset=GBK");
		List list2=new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='GBK'?>");
		sb.append("<root>");
		try {
			hs=HibernateUtil.getSession();
			if(id!=null && !"".equals(id)){
			String hql="select a,h.itemgroup from HandoverElevatorCheckItemRegister a,HandoverElevatorCheckItem h " +
					"where a.examType=h.id.examType and a.checkItem=h.id.checkItem and a.issueCoding=h.id.issueCoding " +
					"and a.elevatorTransferCaseRegister.billno='"+id.trim()+"' order by a.examType desc";
			List list=hs.createQuery(hql).list();
			String[] typeflag={"a.id.typeflag='HandoverElevatorCheckItem_ExamType'"};
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					sb.append("<rows>");
					Object[] obj=(Object[])list.get(i);
					HandoverElevatorCheckItemRegister regester=(HandoverElevatorCheckItemRegister)obj[0] ;
					sb.append("<cols name='examType' value='"+regester.getExamType()+"'>").append("</cols>");
					sb.append("<cols name='examTypeName' value='"+bd.getName("Pulldown", "pullname", "id.pullid", regester.getExamType(), typeflag)+"'>").append("</cols>");
					sb.append("<cols name='checkItem' value='"+regester.getCheckItem()+"'>").append("</cols>");
					sb.append("<cols name='issueCoding' value='"+regester.getIssueCoding()+"'>").append("</cols>");
					sb.append("<cols name='issueContents' value='"+regester.getIssueContents()+"'>").append("</cols>");
					sb.append("<cols name='itemgroup' value='"+(String)obj[1]+"'>").append("</cols>");
					sb.append("<cols name='rem' value='"+regester.getRem()+"'>").append("</cols>");
					sb.append("</rows>");
				}
							
				
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

}
