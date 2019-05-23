package com.gzunicorn.struts.action.basedata;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
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
import org.apache.struts.upload.FormFile;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class mbfImportAction extends DispatchAction {

	Log log = LogFactory.getLog(mbfImportAction.class);

	BaseDataImpl bd = new BaseDataImpl();

	/**
	 * 电梯免保费
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "mbfimport", null);
		/** **********结束用户权限过滤*********** */

		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			ActionForward forward = super.execute(mapping, form, request,response);
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
		
		request.setAttribute("navigator.location","电梯免保费导入 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "mbfImporModifyList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fmbfImportSearch");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		cache.updateTable(table);
		table.setSortColumn("a.maintDivision");
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
		
		String maintDivision = tableForm.getProperty("maintDivision");
		String maintstation = tableForm.getProperty("maintstation");
		String yearMonth = tableForm.getProperty("yearMonth");

		//第一次进入页面时根据登陆人初始化所属维保分部
		List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
		if(maintDivision == null || maintDivision.equals("")){
			Map map = (Map)maintDivisionList.get(0);
			maintDivision = (String)map.get("grcid");
		}
		
		Session hs = null;
		Query query = null;
		try {
			
			hs = HibernateUtil.getSession();
			
			String sql = "select a.numno,c.comname,s.storagename,a.years,a.mbfMoney,a.dayMoney,b.username,a.OperDate "
					+ "from mbfImportInfo a,Loginuser b,Company c,storageid s "
					+ "where a.operid=b.userid "
					+ "and a.maintDivision = c.comid "
					+ "and a.maintStation = s.storageid ";
			
			if (maintDivision != null && !maintDivision.equals("")) {
				sql += " and a.maintDivision like '"+maintDivision.trim()+"'";
			}
			if (maintstation != null && !maintstation.equals("")) {
				sql += " and a.maintstation like '"+maintstation.trim()+"'";
			}
			if (yearMonth != null && !yearMonth.equals("")) {
				sql += " and a.years like '"+yearMonth.trim()+"'";
			}
			
			String order = " order by "+table.getSortColumn();
			
			if (table.getIsAscending()) {
				sql += order + " asc";
			} else {
				sql += order + " desc";
			}
			
			//System.out.println(">>>>>>>"+sql);
			
			query = hs.createSQLQuery(sql);
			table.setVolume(query.list().size());// 查询得出数据记录数;
			
			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom());
			query.setMaxResults(table.getLength());

			cache.check(table);

			List list = query.list();
			List ImportList = new ArrayList();
			for (Object object : list) {
				Object[] objs = (Object[])object;
				
				Map master=new HashMap();
				master.put("numno",objs[0]);
				master.put("maintDivision",objs[1]);
				master.put("maintStation",objs[2]);
				master.put("years",objs[3]);
				master.put("mbfMoney",objs[4]);
				master.put("dayMoney",objs[5]);
				master.put("OperId",objs[6]);
				master.put("OperDate",objs[7]);
				
				ImportList.add(master);
			}

			table.addAll(ImportList);
			session.setAttribute("mbfImporModifyList", table);

			//所属维保站
			String hql="select a from Storageid a where a.comid like '"+maintDivision+"' " +
					"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
			List mainStationList=hs.createQuery(hql).list();
		  
			 Storageid storid=new Storageid();
			 storid.setStorageid("%");
			 storid.setStoragename("全部");
			 mainStationList.add(0,storid);
			 request.setAttribute("mainStationList", mainStationList);
			// 分部下拉框列表
			request.setAttribute("maintDivisionList", maintDivisionList);

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
		forward = mapping.findForward("mbfImportList");
		
		return forward;
	}
	
	/**
	 * 删除记录
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
			
			String id = (String) dform.get("id");

			String delsql="delete from mbfImportInfo where numno="+id;
			hs.connection().prepareStatement(delsql).execute();
				
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.succeed"));

			tx.commit();
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			e2.printStackTrace();
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
	public ActionForward toPrepareImportRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		request.setAttribute("navigator.location","电梯免保费导入 >> 导入");
		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}
		return mapping.findForward("mbfImportImport");
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
	
	public ActionForward toImportRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		FormFile file = (FormFile) dform.get("file"); //获取上传文件
		String fileName = file.getFileName();
		String fileFromt=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()); //获取上传文件的后缀名
		
		InputStream in = null;
		Session hs = null;
		Transaction tx = null;
		PreparedStatement pstmt=null;
		PreparedStatement pstmtdel=null;
		
		StringBuffer reStr = new StringBuffer(); //错误返回信息
	
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if (fileFromt!=null && fileFromt.equals("xlsx")) {//excel 2007
				
				in = file.getInputStream();
				XSSFWorkbook wb = new XSSFWorkbook(in);
				XSSFSheet sheet = wb.getSheetAt(0);
				XSSFRow row = null;			
				
				int rowSum = sheet.getLastRowNum()+1; //最大行数
				
				String userid=userInfo.getUserID();
				String today=CommonUtil.getNowTime();

				List implist=new ArrayList();
				HashMap hmap=null;
				for(int rowNum = 1; rowNum < rowSum; rowNum++){					
					row = sheet.getRow(rowNum);	
					
				    String MaintDivision=cellValueToString(row, 0, reStr);//维保分部
				    String MaintStation=cellValueToString(row, 1, reStr);//维保站
				    String yearMonth=cellValueToString(row, 2, reStr);//月份
				    Double gzMoney=cellValueToDouble(row, 3, reStr);//工资金额
				    Double dayMoney=cellValueToDouble(row, 4, reStr);//平均每日工资

				    if(reStr != null && reStr.length() > 0){
						break;
					}
				    
				    hmap=new HashMap();
				    hmap.put("MaintDivision", MaintDivision);
				    hmap.put("MaintStation", MaintStation);
				    hmap.put("yearMonth", yearMonth);
				    hmap.put("gzMoney", gzMoney);
				    hmap.put("dayMoney", dayMoney);
				    implist.add(hmap);
				}
	
				if(reStr == null || reStr.length() == 0){
					String sqldel="delete mbfImportInfo where MaintDivision=? and MaintStation=? and years=?";
					pstmtdel=hs.connection().prepareStatement(sqldel);
					String sqlins="insert into mbfImportInfo(OperId,OperDate,MaintDivision,MaintStation,years,mbfMoney,dayMoney) "
							+ "values(?,?,?,?,?,?,?)";
					pstmt=hs.connection().prepareStatement(sqlins);
					for(int i=0;i<implist.size();i++){
						hmap=(HashMap)implist.get(i);

						pstmtdel.setString(1, (String)hmap.get("MaintDivision")); 
						pstmtdel.setString(2, (String)hmap.get("MaintStation")); 
						pstmtdel.setString(3, (String)hmap.get("yearMonth")); 
						pstmtdel.addBatch(); 
						
						pstmt.setString(1, userid); 
						pstmt.setString(2, today); 
						pstmt.setString(3, (String)hmap.get("MaintDivision")); 
						pstmt.setString(4, (String)hmap.get("MaintStation")); 
						pstmt.setString(5, (String)hmap.get("yearMonth")); 
						pstmt.setDouble(6, (Double)hmap.get("gzMoney")); 
						pstmt.setDouble(7, (Double)hmap.get("dayMoney")); 
						pstmt.addBatch(); 
					}
					//先删除
					pstmtdel.executeBatch();
					hs.flush();
					//在增加
					pstmt.executeBatch();
					hs.flush();
					

					tx.commit();
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","上传成功！"));
				} else {
					request.setAttribute("reStr", reStr);//错误返回信息
				}
			
			}

		} catch (Exception e2) {
			e2.printStackTrace();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","上传失败！"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = mapping.findForward("returnImport");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return forward;
	}
	
	
	
	/**
	 * 返回单元格字符串值
	 * @param XSSFRow 行对象
	 * @param cellNum 所在列数
	 * @param reStr 错误信息  
	 * @return String
	 */
	public String cellValueToString(XSSFRow row, int cellNum, StringBuffer reStr){
		String value = "";
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) { 
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)不能为空;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			value = cell.getStringCellValue();
		} else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			value = String.valueOf((int) cell.getNumericCellValue());
		}else {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)数据格式必须为字符串;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		return value.trim();
	}

	/**
	 * 返回单元格整型值
	 * @param XSSFRow 行对象
	 * @param cellNum 所在列数
	 * @param reStr 错误信息  
	 * @return String
	 */
	public int cellValueToInt(XSSFRow row, int cellNum, StringBuffer reStr){
		int value = 0;
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)为空;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			value = (int) cell.getNumericCellValue();
		} else {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)数据格式必须为数值;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		return value;
	}
	/**
	 * 返回单元格浮点型
	 * @param XSSFRow 行对象
	 * @param cellNum 所在列数
	 * @param reStr 错误信息  
	 * @return String
	 */
	public Double cellValueToDouble(XSSFRow row, int cellNum, StringBuffer reStr){
		Double value = 0d;
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)为空;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			value = (Double) cell.getNumericCellValue();
		} else {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)数据格式必须为数值;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		return value;
	}

	/**
	 * 将单元格列数转换为大写字母
	 * @param cellNum 列数 
	 * @return char
	 */
	public char getCellChar(int cellNum){
		return (char) (cellNum + 65);
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
