package com.gzunicorn.struts.action.MobileOfficing.logmanagement;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.hotlinemotherboardtype.HotlineMotherboardType;
import com.gzunicorn.hibernate.mobileofficeplatform.accessoriesrequisition.AccessoriesRequisition;
import com.gzunicorn.hibernate.mobileofficeplatform.logmanagement.LogManagement;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;


/**
 * 厂检厂检日志管理
 */
public class EtcLogManagementAction extends DispatchAction {

	Log log = LogFactory.getLog(EtcLogManagementAction.class);
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
	
	/************开始用户权限过滤************/
	SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "etclogmanagement",null);
	/************结束用户权限过滤************/

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

	request.setAttribute("navigator.location", " 厂检日志管理 >> 查询列表");
	ActionForward forward = null;
	HttpSession session = request.getSession();
	ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
	ServeTableForm tableForm = (ServeTableForm) form;
	String action = tableForm.getAction();
	
	if (tableForm.getProperty("genReport") != null
			&& !tableForm.getProperty("genReport").equals("")) {
		try {
			response = toExcelRecord(form,request,response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		forward = mapping.findForward("exportExcel");
		tableForm.setProperty("genReport","");

	} else {
		
		HTMLTableCache cache = new HTMLTableCache(session,"etclogManagementList");
	
		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fEtcLogManagement");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		cache.updateTable(table);
		table.setSortColumn("rowid");
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
		
		String insCompanyName = tableForm.getProperty("insCompanyName");   		
		String contractNo = tableForm.getProperty("contractNo");
		String projectName = tableForm.getProperty("projectName");                              
		String operId = tableForm.getProperty("openid");
		String sdate1 = tableForm.getProperty("sdate1");
		String edate1 = tableForm.getProperty("edate1");
		
		String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
		String day1=DateUtil.getDate(day, "MM", -1);//当前日期月份加1 。
		if(sdate1==null || sdate1.trim().equals("")){
			sdate1=day1;
			tableForm.setProperty("sdate1",sdate1);
		}
		if(edate1==null || edate1.trim().equals("")){
			edate1=day;
			tableForm.setProperty("edate1",edate1);
		}


	
		Session hs = null;
		Query query = null;
		String order = "";
		try {
			hs = HibernateUtil.getSession();
			String sql = "select lm.rowid,lm.operdate,lm.contractno,lm.projectname,lm.inscompanyname,lu.username "
					+ "from TransferCaseRegisterLog lm,LoginUser lu where lm.OperId=lu.UserID ";
			
			//厂检员只能看自己的记录
			if(userInfo.getRoleID().equals("A51")){
				sql += " and lm.OperId = '" + userInfo.getUserID()+"'";
				
				request.setAttribute("addflag", "true");
				request.setAttribute("delflag", "yes");
			}else if (userInfo.getRoleID().equals("A01")) {
				request.setAttribute("delflag", "yes");
			}
			
			if (insCompanyName != null && !insCompanyName.equals("")) {
				sql += " and lm.insCompanyName like '%" + insCompanyName.trim() + "%'";
			}
			if (contractNo != null && !contractNo.equals("")) {
				sql += " and lm.contractNo  like '%" + contractNo.trim() + "%'";
			}
			if (projectName != null && !projectName.equals("")) {
				sql += " and lm.projectName  like '%" + projectName.trim() + "%'";
			}
			if (sdate1 != null && !sdate1.equals("")) {
				sql += " and lm.operDate >= '" + sdate1.trim() + " 00:00:00"
						+ "'";
			}
			if (edate1 != null && !edate1.equals("")) {
				sql += " and lm.operDate <= '" + edate1.trim() + " 99:99:99"
						+ "'";
			}
			if (operId != null && !operId.equals("")) {
				sql += " and (lu.UserID like '"+operId.trim()+"' or lu.UserName like '%" + operId.trim() + "%')";
			}
			if (table.getIsAscending()) {
				order += " order by " + table.getSortColumn();
			} else {
				order += " order by " + table.getSortColumn() + " desc";
			}
	
			query = hs.createSQLQuery(sql + order);
			table.setVolume(query.list().size());// 查询得出数据记录数;
	
			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());
	
			cache.check(table);
	
			List list = query.list();
			List logManagementList = new ArrayList();
	
			if (list != null && list.size() > 0) {
				int j = list.size();
				for (int i = 0; i < j; i++) {
					Object[] ts = (Object[]) list.get(i);
					HashMap map = new HashMap();
					map.put("rowid", ts[0]);
					map.put("operId", ts[5]);	
					map.put("operDate", ts[1]);
					map.put("contractNo", ts[2]);
					map.put("projectName", ts[3]);
					map.put("insCompanyName", ts[4]);
	
					logManagementList.add(map);
				}
			}
	
			table.addAll(logManagementList);
			session.setAttribute("etclogManagementList", table);

		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (Exception e1) {
	
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
		forward = mapping.findForward("etclogManagementList");
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
	request.setAttribute("navigator.location",
			messages.getMessage(locale, navigation));
}

/**
 * 点击查看的方法
 * 
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

	request.setAttribute("navigator.location", " 厂检日志管理  >> 查看");

	ActionForward forward = null;
	String id = (String) dform.get("id");
	Session hs = null;
	LogManagement logManagement = null;
	if (id != null) {
		try {
			hs = HibernateUtil.getSession();

			String sql ="select operid,operdate,contractno,projectname,"
					+ "inscompanyname,phnum,iscjwx,iszj,xcfkwt,workcontent,rem"
					+ " from TransferCaseRegisterLog  where rowid="+id;

			ResultSet rs=hs.connection().prepareStatement(sql).executeQuery();
			if(rs.next()){
				HashMap hmap=new HashMap();
				hmap.put("operId", bd.getName(hs,"LoginUser", "UserName", "UserID",rs.getString("operid")));
				hmap.put("operDate", rs.getString("operdate"));
				hmap.put("contractNo", rs.getString("contractno"));
				hmap.put("projectName", rs.getString("projectname"));
				hmap.put("insCompanyName", rs.getString("inscompanyname"));
				hmap.put("phnum", rs.getString("phnum"));
				hmap.put("iscjwx", rs.getString("iscjwx"));
				hmap.put("iszj", rs.getString("iszj"));
				hmap.put("xcfkwt", rs.getString("xcfkwt"));
				hmap.put("workContent", rs.getString("workcontent"));
				hmap.put("rem", rs.getString("rem"));
				
				request.setAttribute("etclogManagementBean", hmap);
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

		request.setAttribute("display", "yes");
		forward = mapping.findForward("etclogManagementDisplay");

	}

	if (!errors.isEmpty()) {
		saveErrors(request, errors);
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
	List list2=new ArrayList();
	StringBuffer sb = new StringBuffer();
	sb.append("<?xml version='1.0' encoding='GBK'?>");
	sb.append("<root>");
	try {
		hs=HibernateUtil.getSession();
		if(comid!=null && !"".equals(comid)){
		String hql="select a from Storageid a,Company b where a.comid = b.comid and a.comid="+comid+" and a.storagetype=1 and a.parentstorageid ='0'";
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
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		                               
		String insCompanyName = tableForm.getProperty("insCompanyName");   		
		String contractNo = tableForm.getProperty("contractNo");
		String projectName = tableForm.getProperty("projectName");                              
		String operId = tableForm.getProperty("openid");
		String sdate1 = tableForm.getProperty("sdate1");
		String edate1 = tableForm.getProperty("edate1");	
		
		if(sdate1==null || sdate1.trim().equals("")){
			sdate1="0000-00-00";
		}
		if(edate1==null || edate1.trim().equals("")){
			edate1="9999-99-99";
		}
		
		

		
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();
		String sheetname="厂检日志管理";
		try {
			hs = HibernateUtil.getSession();
			
			String[] titlename={"录入人","录入日期","合同号","项目名称","安装单位","配合人数","厂检/维修","是否自检","现场反馈问题","厂检完成情况汇总","备注"};
			String[] titleid={"username","OperDate","ContractNo","ProjectName","InsCompanyName","phnum","Iscjwx","iszj","xcfkwt","WorkContent","Rem"};
	
			String sql = "select lm.*,lu.username "
					+ "from TransferCaseRegisterLog lm,LoginUser lu where lm.OperId=lu.UserID ";
			//厂检员只能看自己的记录
			if(userInfo.getRoleID().equals("A51")){
				sql += " and lm.OperId = '" + userInfo.getUserID()+"'";
			}
			if (insCompanyName != null && !insCompanyName.equals("")) {
				sql += " and lm.insCompanyName like '%" + insCompanyName.trim() + "%'";
			}
			if (contractNo != null && !contractNo.equals("")) {
				sql += " and lm.contractNo  like '%" + contractNo.trim() + "%'";
			}
			if (projectName != null && !projectName.equals("")) {
				sql += " and lm.projectName  like '%" + projectName.trim() + "%'";
			}
			if (sdate1 != null && !sdate1.equals("")) {
				sql += " and lm.operDate >= '" + sdate1.trim() + " 00:00:00"+ "'";
			}
			if (edate1 != null && !edate1.equals("")) {
				sql += " and lm.operDate <= '" + edate1.trim() + " 99:99:99"+ "'";
			}
			if (operId != null && !operId.equals("")) {
				sql += " and (lu.UserID like '"+operId.trim()+"' or lu.UserName like '%" + operId.trim() + "%')";
			}
			sql+=" order by lm.OperId,lm.OperDate desc ";
			
			//System.out.println(">>>"+sql);
	
	        XSSFSheet sheet = wb.createSheet(sheetname);
	        
	        //创建单元格样式
	        XSSFCellStyle cs = wb.createCellStyle();
	        cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);//左右居中
	        cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
	        XSSFFont f  = wb.createFont();
	        f.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 字体加粗
	        cs.setFont(f);
	        
	        //创建标题
	        XSSFRow row0 = sheet.createRow( 0);
	        XSSFCell cell0 = null;
			for(int i=0;i<titlename.length;i++){
				cell0 = row0.createCell((short)i);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(titlename[i]);
				cell0.setCellStyle(cs);
			}
	
			//创建内容
			XSSFRow row = null;
			XSSFCell cell =null;
			ResultSet rs=hs.connection().prepareStatement(sql).executeQuery();
			int num=0;
			while(rs.next()){
				// 创建Excel行，从0行开始
				row = sheet.createRow(num+1);
				for(int c=0;c<titleid.length;c++){
				    cell = row.createCell((short)c);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);  
				    cell.setCellValue(rs.getString(titleid[c]));
				}
				num++;
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
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode(sheetname, "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	
	/**
	 * 进入新建页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toPrepareAddRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("navigator.location", " 厂检日志管理 >> 新建");
		ActionForward forward = null;
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		request.setAttribute("operid", userInfo.getUserID());
		request.setAttribute("opername", userInfo.getUserName());
		request.setAttribute("operdate", CommonUtil.getNowTime());
		request.setAttribute("addflag", "true");
		
		forward =mapping.findForward("etclogManagementAdd"); 
		return forward;
	}
	
	/**
	 * 新建方法
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
		ActionErrors errors = new ActionErrors();
		
		Session session = null;
		Transaction tx = null;
		
		String contractno = request.getParameter("contractno");
		String projectname = request.getParameter("projectname");
		String inscompanyname = request.getParameter("inscompanyname");
		String phnum = request.getParameter("phnum");
		String iscjwx = request.getParameter("iscjwx");
		String iszj = request.getParameter("iszj");
		String xcfkwt = request.getParameter("xcfkwt");
		String workcontent = request.getParameter("workcontent");
		String rem = request.getParameter("rem");
		String operid = request.getParameter("operid");
		String operdate = request.getParameter("operdate");
		
		try {
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			String sqlins="insert into TransferCaseRegisterLog(operid,operdate,contractno,projectname,"
				+ "inscompanyname,phnum,iscjwx,iszj,xcfkwt,workcontent,rem) "
				+ "values('"+operid+"','"+operdate+"','"+contractno+"','"+projectname+"',"
				+ "'"+inscompanyname+"','"+phnum+"','"+iscjwx+"','"+iszj+"','"+xcfkwt+"','"+workcontent+"','"+rem+"')";
			session.connection().prepareStatement(sqlins).execute();
		
			tx.commit();
			
		} catch (Exception e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			tx.rollback();
			e.printStackTrace();
		} finally {
            try {
            	if(session!=null){
            		session.close();
            	}
            } catch (HibernateException hex) {
                DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
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
	 * 删除方法
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

		ActionForward forward = null;
		
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		
		String id = request.getParameter("id");
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			String sql = "delete from TransferCaseRegisterLog where rowid="+id;
			hs.connection().prepareStatement(sql).execute();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.succeed"));

			tx.commit();
		} catch (Exception e2) {
			e2.printStackTrace();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
			}
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
		
		forward = mapping.findForward("returnList");
		return forward;
	}
}

