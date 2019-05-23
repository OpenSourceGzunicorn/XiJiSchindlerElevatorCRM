package com.gzunicorn.struts.action.MobileOfficing.logmanagement;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.json.JSONArray;
import org.json.JSONException;
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

import com.gzunicorn.common.dao.mssql.DataOperation;
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
import com.gzunicorn.hibernate.mobileofficeplatform.logmanagement.LogManagement;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * 督查督查日志管理
 */
public class QcLogManagementAction extends DispatchAction {

	Log log = LogFactory.getLog(QcLogManagementAction.class);
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
	SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "qclogmanagement",null);
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

	request.setAttribute("navigator.location", " 督查日志管理 >> 查询列表");
	ActionForward forward = null;
	HttpSession session = request.getSession();
	ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
	ServeTableForm tableForm = (ServeTableForm) form;
	String action = tableForm.getAction();
	
	String maintDivision = "";
	//第一次进入页面时根据登陆人初始化所属维保分部
	List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
	if(maintDivision == null || maintDivision.equals("")){
		Map map = (Map)maintDivisionList.get(0);
		maintDivision = (String)map.get("grcid");
	}
	
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
		
		HTMLTableCache cache = new HTMLTableCache(session,"qclogManagementList");
	
		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fQcLogManagement");
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
		
		String elevatorNo = tableForm.getProperty("elevatorNo");
		String maintStation = tableForm.getProperty("maintStation");                                 
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
			String sql = "select lm.rowid,lm.OperDate,lm.MaintStation,"
					+ "lm.MaintPersonnel,lm.ElevatorNo,lu.UserName "
					+ "from QualityCheckLog lm,LoginUser lu where lm.OperId=lu.UserID ";
			//督查员只能看自己的记录
			if(userInfo.getRoleID().equals("A12")){
				sql += " and lm.OperId = '" + userInfo.getUserID()+"'";
				
				request.setAttribute("addflag", "true");
				request.setAttribute("delflag", "yes");
			}else if (userInfo.getRoleID().equals("A01")) {
				request.setAttribute("delflag", "yes");
			}
			
			if (elevatorNo != null && !elevatorNo.equals("")) {
				sql += " and lm.elevatorNo like '%" + elevatorNo.trim()+ "%'";
			}
			if (maintStation != null && !maintStation.equals("")) {
				sql += " and lm.maintStation like '" + maintStation.trim() + "%'";
			}
			if (sdate1 != null && !sdate1.equals("")) {
				sql += " and lm.operDate >= '" + sdate1.trim() + " 00:00:00"+ "'";
			}
			if (edate1 != null && !edate1.equals("")) {
				sql += " and lm.operDate <= '" + edate1.trim() + " 23:59:59"+ "'";
			}
			if (operId != null && !operId.equals("")) {
				sql += " and (lu.UserID like '" + operId.trim()+ "' or lu.UserName like '%" + operId.trim() + "%')";
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
					map.put("maintStation", bd.getName(hs, "Storageid","storagename", "storageid", (String) ts[2]));
					map.put("maintPersonnel", ts[3]);
					map.put("elevatorNo", ts[4]);

					logManagementList.add(map);
				}
			}
	
			table.addAll(logManagementList);
			session.setAttribute("qclogManagementList", table);

			//维保站下拉框
//			String hsql="select a from Storageid a where " +
//					" a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
//			List mainStationList=hs.createQuery(hsql).list();
//		  
//			 Storageid storid=new Storageid();
//			 storid.setStorageid("%");
//			 storid.setStoragename("全部");
//			 mainStationList.add(0,storid);
//			request.setAttribute("mainStationList", mainStationList);
			
			//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
			String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
			List vmlist=hs.createSQLQuery(sqlss).list();
			if(vmlist!=null && vmlist.size()>0){
				String hql="select a from Storageid a where a.storageid= '"+userInfo.getStorageId()+"' " +
						"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";	
				List mainStationList=hs.createQuery(hql).list();
				request.setAttribute("mainStationList", mainStationList);
			}else{
				String hql="select a from Storageid a where a.comid like '"+maintDivision+"' " +
						"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
				List mainStationList=hs.createQuery(hql).list();
			  
				 Storageid storid=new Storageid();
				 storid.setStorageid("%");
				 storid.setStoragename("全部");
				 mainStationList.add(0,storid);
				 
				 request.setAttribute("mainStationList", mainStationList);
			}
			 
			 

	
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
		forward = mapping.findForward("qclogManagementList");
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

	request.setAttribute("navigator.location", " 督查日志管理  >> 查看");

	ActionForward forward = null;
	String id = (String) dform.get("id");
	Session hs = null;
	LogManagement logManagement = null;
	if (id != null) {
		try {
			hs = HibernateUtil.getSession();

			String sql ="select OperId,OperDate,MaintStation,MaintPersonnel,"
					+ "ElevatorNo,ydlh,isgzfz,iszfwt,jffkwt,ycjkwt,Rem"
					+ " from QualityCheckLog  where rowid="+id;

			ResultSet rs=hs.connection().prepareStatement(sql).executeQuery();
			if(rs.next()){
				HashMap hmap=new HashMap();
				hmap.put("operId", bd.getName(hs,"LoginUser", "UserName", "UserID",rs.getString("operid")));
				hmap.put("operDate", rs.getString("operdate"));
				hmap.put("maintStation", bd.getName(hs, "Storageid","storagename", "storageid", rs.getString("MaintStation")));
				hmap.put("maintPersonnel", rs.getString("MaintPersonnel"));
				hmap.put("elevatorNo", rs.getString("ElevatorNo"));
				hmap.put("ydlh", rs.getString("ydlh"));
				hmap.put("isgzfz", rs.getString("isgzfz"));
				hmap.put("iszfwt", rs.getString("iszfwt"));
				hmap.put("jffkwt", rs.getString("jffkwt"));
				hmap.put("ycjkwt", rs.getString("ycjkwt"));
				hmap.put("rem", rs.getString("rem"));
				
				request.setAttribute("qclogManagementBean", hmap);
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
		forward = mapping.findForward("qclogManagementDisplay");

	}

	if (!errors.isEmpty()) {
		saveErrors(request, errors);
	}
	return forward;
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

	request.setAttribute("navigator.location", " 督查日志管理 >> 新建");
	ActionForward forward = null;
	Session hs = null;
	Connection con = null;
	DataOperation op = new DataOperation();
	
	HttpSession session = request.getSession();
	ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
	
	try {
		hs = HibernateUtil.getSession();
		con = hs.connection();
		op.setCon(con);
		String sql ="select StorageID,StorageName from Storageid "
			+ "where Storagetype='1' and ParentStorageID='0' and EnabledFlag='Y'";

		List maintStationList=op.queryToList(sql);
		
		request.setAttribute("maintStationList", maintStationList);
		
	} catch (Exception e) {
		e.printStackTrace();
	}	finally{
		try {
			if (hs != null) {
				hs.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	request.setAttribute("operid", userInfo.getUserID());
	request.setAttribute("opername", userInfo.getUserName());
	request.setAttribute("operdate", CommonUtil.getNowTime());
	request.setAttribute("addflag", "true");
	
	forward =mapping.findForward("qcLogManagementAdd"); 
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
		
		String operid = request.getParameter("operid");
		String operdate = request.getParameter("operdate");
		String maintStation = request.getParameter("maintStation");
		String MaintPersonnel = request.getParameter("MaintPersonnel");
		String ElevatorNo = request.getParameter("ElevatorNo");
		String ydlh = request.getParameter("ydlh");
		String isgzfz = request.getParameter("isgzfz");
		String iszfwt = request.getParameter("iszfwt");
		String jffkwt = request.getParameter("jffkwt");
		String ycjkwt = request.getParameter("ycjkwt");
		String rem = request.getParameter("rem");
		
		try {
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			String sqlins="insert into QualityCheckLog(operid,operdate,maintstation,maintpersonnel,"
				+ "elevatorno,ydlh,isgzfz,iszfwt,jffkwt,ycjkwt,rem) "
				+ "values('"+operid+"','"+operdate+"','"+maintStation+"','"+MaintPersonnel+"','"
				+ElevatorNo+"','"+ydlh+"','"+isgzfz+"','"+iszfwt+"','"+jffkwt+"','"+ycjkwt+"','"+rem+"')";
			session.connection().prepareStatement(sqlins).execute();
			
			tx.commit();
			
			
		}catch (Exception e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			tx.rollback();
			e.printStackTrace();
		}finally {
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
		
		String elevatorNo = tableForm.getProperty("elevatorNo");
		String maintStation = tableForm.getProperty("maintStation");                                 
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
		String sheetname="督查日志管理";
		try {
			hs = HibernateUtil.getSession();
			
			String[] titlename={"录入人","录入日期","维保站","维保工","电梯编号","月度例会","维保负责人是否对工作负责","维保负责人是否存在作风问题","甲方反馈问题","远程监控问题","备注"};
			String[] titleid={"username","operdate","storagename","maintpersonnel","elevatorno","ydlh","isgzfz","iszfwt","jffkwt","ycjkwt","rem"};

			String sql = "select lm.OperId,lm.OperDate,lm.MaintStation,lm.MaintPersonnel,"
					+ "lm.ElevatorNo,lm.ydlh,lm.isgzfz,lm.iszfwt,lm.jffkwt,"
					+ "lm.ycjkwt,lm.Rem,lu.UserName,s.storagename "
					+ "from QualityCheckLog lm,LoginUser lu,Storageid s "
					+ "where lm.OperId=lu.UserID and lm.MaintStation=s.storageid";
	
			//督查员只能看自己的记录
			if(userInfo.getRoleID().equals("A12")){
				sql += " and lm.OperId = '" + userInfo.getUserID()+"'";
			}
			if (elevatorNo != null && !elevatorNo.equals("")) {
				sql += " and lm.elevatorNo like '%" + elevatorNo.trim()+ "%'";
			}
			if (maintStation != null && !maintStation.equals("")) {
				sql += " and lm.maintStation like '" + maintStation.trim() + "%'";
			}
			if (sdate1 != null && !sdate1.equals("")) {
				sql += " and lm.operDate >= '" + sdate1.trim() + " 00:00:00"+ "'";
			}
			if (edate1 != null && !edate1.equals("")) {
				sql += " and lm.operDate <= '" + edate1.trim() + " 23:59:59"+ "'";
			}
			if (operId != null && !operId.equals("")) {
				sql += " and (lu.UserID like '" + operId.trim()+ "' or lu.UserName like '%" + operId.trim() + "%')";
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
			
			String sql = "delete from QualityCheckLog where rowid="+id;
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
	
	/**
	 * 获取维保工
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ActionForward toGetMaintPersonnel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		request.setAttribute("navigator.location", " 维保工 >> 查询列表");
		
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
	
		HTMLTableCache cache = new HTMLTableCache(session, "searchMaintPersonnelList");
		
		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setLength(10000);
		table.setIsAscending(true);
		cache.updateTable(table);
		
		cache.saveForm(tableForm);
		
		ActionForward forward = null;
		Session s = null;
		Connection con = null;
		DataOperation op = new DataOperation();
		
		String maintStation = request.getParameter("maintStation");
		
		try {
			s = HibernateUtil.getSession();
			con = s.connection();
			op.setCon(con);
			
			//维保工A50,维保站长A49,维保经理 A03，维修技术员A53 
			String sql ="select UserID,UserName,phone from LoginUser "
					+ "where StorageID like '"+maintStation+"%' and EnabledFlag='Y' "
					+ "and RoleID in('A50','A49','A03','A53')";
			//System.out.println(">>>>"+sql); 
			List selectList=op.queryToList(sql);
			
			if (selectList!= null && selectList.size()>0) {
				for (int i = 0; i < selectList.size(); i++) {
					HashMap map = (HashMap) selectList.get(i);
					String username = (String) map.get("username");
					String phone = (String) map.get("phone");
					String MaintPersonnel = username+"("+phone+")";
					
					map.put("MaintPersonnel", MaintPersonnel);
				}
			}
			
			table.setVolume(selectList.size());
			
			cache.check(table);
			
			table.addAll(selectList);
			
			session.setAttribute("searchMaintPersonnelList", table);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if (s != null) {
					s.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		forward = mapping.findForward("getMaintPersonnel");
		
		return forward;
	}
		
		

}

