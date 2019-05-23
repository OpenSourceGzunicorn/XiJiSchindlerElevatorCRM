package com.gzunicorn.struts.action.basedata;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.dataimport.importutil.ComUtil;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.hibernate.basedata.city.City;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.basedata.handoverelevatorcheckitem.HandoverElevatorCheckItem;
import com.gzunicorn.hibernate.basedata.handoverelevatorcheckitem.HandoverElevatorCheckItemId;
import com.gzunicorn.hibernate.basedata.hotlinefaulttype.HotlineFaultType;
import com.gzunicorn.hibernate.basedata.principal.Principal;
import com.gzunicorn.hibernate.basedata.region.Region;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class HandoverElevatorCheckItemAction extends DispatchAction {

	Log log = LogFactory.getLog(HandoverElevatorCheckItemAction.class);
	
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
				SysRightsUtil.NODE_ID_FORWARD + "handoverelevatorcheckitem", null);
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

		request.setAttribute("navigator.location","	交接电梯检查项目>> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm)form;
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
			HTMLTableCache cache = new HTMLTableCache(session, "handoverElevatorCheckItemList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fHandoverElevatorCheckItem");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
//			table.setLength(1);
			cache.updateTable(table);
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

			String examType = tableForm.getProperty("examType");
			String checkItem = tableForm.getProperty("checkItem");
			String issueCoding=tableForm.getProperty("issueCoding");
			String issueContents=tableForm.getProperty("issueContents");
			String elevatorType = tableForm.getProperty("elevatorType");
			String enabledFlag = tableForm.getProperty("enabledFlag");
			
			Session hs = null;

			try {

				hs = HibernateUtil.getSession();

				Criteria criteria = hs.createCriteria(HandoverElevatorCheckItem.class);
				if (examType != null && !examType.equals("")) {
					criteria.add(Expression.like("id.examType", "%" + examType.trim()
							+ "%"));
				}
				if (checkItem != null && !checkItem.equals("")) {
					criteria.add(Expression.like("id.checkItem", "%"
							+ checkItem.trim() + "%"));
				}
				if (issueCoding != null && !issueCoding.equals("")) {
					criteria.add(Expression.like("id.issueCoding", "%"
							+ issueCoding.trim() + "%"));
				}
				if (issueContents != null && !issueContents.equals("")) {
					criteria.add(Expression.like("issueContents", "%"
							+ issueContents.trim() + "%"));
				}
				if (enabledFlag != null && !enabledFlag.equals("")) {
					criteria.add(Expression.eq("enabledFlag", enabledFlag));
				}
				if (elevatorType != null && !elevatorType.equals("")) {
					criteria.add(Expression.eq("elevatorType", elevatorType));
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

				List handoverElevatorCheckItemList = criteria.list();
				if(handoverElevatorCheckItemList!=null){
					int len=handoverElevatorCheckItemList.size();
					for(int i=0;i<len;i++){
						HandoverElevatorCheckItem heci=(HandoverElevatorCheckItem) handoverElevatorCheckItemList.get(i);
//						heci.getId().setExamType(bd.getName("Pulldown", "pullname", "pullid", heci.getId().getExamType()));
						String sql2="select pullname from pulldown where pullid='"+heci.getId().getExamType()+"' and typeflag='HandoverElevatorCheckItem_ExamType'";
						List list2=hs.createSQLQuery(sql2).list();
						if(list2.size()>0){
							heci.getId().setExamType((String)list2.get(0));
						}else{
							heci.getId().setExamType("");
							
						}
					}
				}

				table.addAll(handoverElevatorCheckItemList);
				session.setAttribute("handoverElevatorCheckItemList", table);
				session.setAttribute("examTypeList", bd.getPullDownList("HandoverElevatorCheckItem_ExamType"));
				session.setAttribute("elevatorTypeList", bd.getPullDownList("ElevatorSalesInfo_type"));

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
			forward = mapping.findForward("handoverElevatorCheckItemList");
		}
//		request.setAttribute("examTypeList", bd.getPullDownList("HandoverElevatorCheckItem_ExamType"));
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
		response.setCharacterEncoding("utf-8");
		request.setAttribute("navigator.location","交接电梯检查项目 >> 查看");
		
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;
		
		String id = (String) dform.get("id");
		String examType=(String)dform.get("type");
		String checkItem=(String)dform.get("item");
		try {
			examType = new String(examType.getBytes("ISO-8859-1"),"GBK");
			checkItem=new String(checkItem.getBytes("ISO-8859-1"),"GBK");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
//		String checkItem = request.getParameter("Item");

		
		Session hs = null;
		HandoverElevatorCheckItem handoverelevatorcheckitem = null;
//		HandoverElevatorCheckItemId heciId=new HandoverElevatorCheckItemId();
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				
				String sql1="select pullid from pulldown where pullname='"+examType+"'";
				Query query1=hs.createSQLQuery(sql1);
				List list1=query1.list();
				if(list1.size()>0){
					examType=(String)list1.get(0);
				}
				Query query=hs.createQuery("from HandoverElevatorCheckItem h where h.id.examType=:type and h.id.checkItem=:item and h.id.issueCoding=:issueId");
				query.setString("issueId", (String)dform.get("id"));
				query.setString("type", examType);
				query.setString("item", checkItem);
//				handoverelevatorcheckitem = (HandoverElevatorCheckItem) hs.get(HandoverElevatorCheckItem.class,(String) dform.get("id"));
				java.util.List list=query.list();
				if(list!=null && list.size()>0){
					handoverelevatorcheckitem=(HandoverElevatorCheckItem) list.get(0);
					String sql2="select pullname from pulldown where pullid='"+handoverelevatorcheckitem.getId().getExamType()+"' and typeflag='HandoverElevatorCheckItem_ExamType'";
					List list2=hs.createSQLQuery(sql2).list();
					if(list2.size()>0){
//						//System.out.println(list2.get(0));
						handoverelevatorcheckitem.getId().setExamType((String)list2.get(0));
					}else{
						handoverelevatorcheckitem.getId().setExamType("");
						
					}
				}
				
				if (handoverelevatorcheckitem == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"handoverelevatorcheckitem.display.recordnotfounterror"));
				}
			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			}catch(Exception e2){
				e2.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}

			request.setAttribute("display", "yes");
			request.setAttribute("handoverElevatorCheckItemBean", handoverelevatorcheckitem);
			
			forward = mapping.findForward("handoverElevatorCheckItemDisplay");

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
	public ActionForward toPrepareImportRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		request.setAttribute("navigator.location","交接电梯检查项目 >> 导入");

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}

		return mapping.findForward("handoverElevatorCheckItemImport");
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
		String fileFromt=fileName.substring(
				fileName.lastIndexOf(".")+1,fileName.length()); //获取上传文件的后缀名
		
		InputStream in = null;
		Session hs = null;
		Transaction tx = null;
		StringBuffer reStr = new StringBuffer(); //错误返回信息
	
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();

			if (fileFromt!=null && fileFromt.equals("xlsx")) {//excel 2007
				HandoverElevatorCheckItemId heci=null;
				HandoverElevatorCheckItem master = null; //交接电梯信息检查表
				List<HandoverElevatorCheckItem> list = new ArrayList<HandoverElevatorCheckItem>();
				
				in = file.getInputStream();
				XSSFWorkbook wb = new XSSFWorkbook(in);
				XSSFSheet sheet = wb.getSheetAt(0);
				XSSFRow row = null;			
				
				int rowSum = sheet.getLastRowNum()+1; //最大行数
				String elevatorType = ""; 
				String examType="";
				String checkItem="";
				String issueCoding="";
				String id1 = "";
				String id2 = "";
				String id3 = "";
				String userid=userInfo.getUserID();
				String today=CommonUtil.getToday();
				for(int rowNum = 1; rowNum < rowSum; rowNum++){					
					row = sheet.getRow(rowNum);				
					master = new HandoverElevatorCheckItem();
					heci=new HandoverElevatorCheckItemId();
					elevatorType = cellValueToString(row, 4, reStr); //电梯类型					
					if("直梯".equals(elevatorType)){
						elevatorType = "T";
					} else if("扶梯".equals(elevatorType)) {
						elevatorType = "F";
					}//HandoverElevatorCheckItem_ExamType
					String sql="select pullname from pulldown where pullid='JF' and typeflag='HandoverElevatorCheckItem_ExamType'";
					String sq2="select pullname from pulldown where pullid='YB' and typeflag='HandoverElevatorCheckItem_ExamType'";
					String sq3="select pullname from pulldown where pullid='ZD' and typeflag='HandoverElevatorCheckItem_ExamType'";
					String sq4="select pullname from pulldown where pullid='QT' and typeflag='HandoverElevatorCheckItem_ExamType'";
					Query query1=hs.createSQLQuery(sql);
					Query query2=hs.createSQLQuery(sq2);
					Query query3=hs.createSQLQuery(sq3);
					Query query4=hs.createSQLQuery(sq4);
					examType=cellValueToString(row, 0, reStr);//检查类型
					if(query1.list().get(0).equals(examType)){
						examType="JF";
					}else if(query2.list().get(0).equals(examType)){
						examType="YB";
					}else if(query3.list().get(0).equals(examType)){
						examType="ZD";
					}else if(query4.list().get(0).equals(examType)){
						examType="QT";
					}else{
						reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(0) + "列)检查类型不存在;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
					}
					heci.setExamType(examType);
					//检查项目
					checkItem=cellValueToString(row, 1, reStr);
					heci.setCheckItem(checkItem);
					//问题编码
					issueCoding=cellValueToString(row, 2, reStr);
					heci.setIssueCoding(issueCoding);
					master.setIssueContents(cellValueToString(row, 3, reStr));//问题内容
					master.setOrderby(cellValueToDouble(row, 5, reStr));//排序号
					if(elevatorType.trim().equals("T")){
						//直梯才需要小组编号
						master.setItemgroup(cellValueToString(row, 6, reStr));
					}else{
						//XSSFCell cell = row.getCell(6);
						//if(cell!=null){
						//	master.setItemgroup(cell.getStringCellValue());
						//}
					}
					master.setId(heci);
					master.setElevatorType(elevatorType);
					master.setRem("");
					master.setOperId(userid);
					master.setOperDate(today);
					master.setEnabledFlag("Y");
					if(reStr != null && reStr.length() > 0){
						break;
					}
					list.add(master);	
					
					id1 +=  rowNum < rowSum-1 ? "'" + examType + "'," : "'" + examType + "'";
					id2 +=  rowNum < rowSum-1 ? "'" + checkItem + "'," : "'" + checkItem + "'";
					id3 +=  rowNum < rowSum-1 ? "'" + issueCoding + "'," : "'" + issueCoding + "'";
				}
				//根据 检查类型+检查项目+问题编码 进行删除数据。
				if(reStr == null || reStr.length() == 0){
					String hql = "delete HandoverElevatorCheckItem  where  examType in ("+id1+") and checkItem in ("+id2+") and issueCoding in ("+id3+")"; //删除旧版本数据
					Query query = hs.createQuery(hql); 
					query.executeUpdate();
					
					for (HandoverElevatorCheckItem handoverElevatorCheckItem : list) {						
						hs.save(handoverElevatorCheckItem);
						//hs.flush();
					}
				} else {
					request.setAttribute("reStr", reStr);//错误返回信息
				}
			
			}

			tx.commit();
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","<font color='red'>导入失败！</font>"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Insert error!");
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = mapping.findForward("returnImport");
		
		if (errors.isEmpty() && reStr.length() == 0) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
				
		} else {
			request.setAttribute("error", "Yes");
		}
			
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
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)为空;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			value = cell.getStringCellValue();
		} else {
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
	 * 返回单元格double值
	 * @param XSSFRow 行对象
	 * @param cellNum 所在列数
	 * @param reStr 错误信息  
	 * @return String
	 */
	public double cellValueToDouble(XSSFRow row, int cellNum, StringBuffer reStr){
		double value = 0;
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)为空;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			value =  cell.getNumericCellValue();
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

		request.setAttribute("navigator.location","交接电梯检查项目>> 添加");
		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
			dform.set("enabledFlag", "Y");
		}
		dform.set("enabledFlag", "Y");
//		request.setAttribute("examTypeList", bd.getPullDownList("HandoverElevatorCheckItem_ExamType"));
		request.setAttribute("elevatorTypeList", bd.getPullDownList("ElevatorSalesInfo_type"));
		return mapping.findForward("handoverElevatorCheckItemAdd");
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
		String examType = (String) dform.get("examType");//检查类型
		String checkItem = (String) dform.get("checkItem");//检查项目
		String issueCoding=(String) dform.get("issueCoding");//问题编码
		String issueContents=(String) dform.get("issueContents");//问题内容
		String elevatorType=(String) dform.get("elevatorType");//电梯类型
		String enabledFlag = (String) dform.get("enabledFlag");//启用标志
		Double orderby=(Double) dform.get("orderby");//排序号
		String rem = (String) dform.get("rem");//备注
		String itemgroup = (String) dform.get("itemgroup");
		
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
		
			HandoverElevatorCheckItem handoverelevatorcheckitem = new HandoverElevatorCheckItem();
			HandoverElevatorCheckItemId heciId=new HandoverElevatorCheckItemId();
			heciId.setCheckItem(checkItem.trim());
			heciId.setExamType(examType.trim());
			heciId.setIssueCoding(issueCoding.trim());
			handoverelevatorcheckitem.setId(heciId);
			handoverelevatorcheckitem.setIssueContents(issueContents.trim());
			handoverelevatorcheckitem.setEnabledFlag(enabledFlag.trim());
			handoverelevatorcheckitem.setRem(rem.trim());	
			handoverelevatorcheckitem.setElevatorType(elevatorType.trim());
			handoverelevatorcheckitem.setOperId(userInfo.getUserID().trim());//录入人
			handoverelevatorcheckitem.setOperDate(CommonUtil.getToday().trim());//录入时间
			handoverelevatorcheckitem.setOrderby(orderby);//排序号
			handoverelevatorcheckitem.setItemgroup(itemgroup);
			
			hs.save(handoverelevatorcheckitem);
			tx.commit();
		} catch (HibernateException e2) {
		
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("handoverelevatorcheckitem.insert.duplicatekeyerror"));
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
		
		request.setAttribute("navigator.location","交接电梯检查项目 >> 修改");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
//		request.setAttribute("examTypeList", bd.getPullDownList("HandoverElevatorCheckItem_ExamType"));

		ActionForward forward = null;
		String id = null;
		Session hs = null;
		
		String examType=(String)dform.get("type");
		String checkItem=(String)dform.get("item");
		String issueCoding=(String)dform.get("id");
		
		try {
			if(examType==null || examType.equals("")){
				examType=request.getAttribute("type").toString();
			}else{
				examType = new String(examType.getBytes("ISO-8859-1"),"GBK");
			}
			if(checkItem==null || checkItem.equals("")){
				checkItem=request.getAttribute("item").toString();
			}else{
				checkItem=new String(checkItem.getBytes("ISO-8859-1"),"GBK");
			}
			if(issueCoding==null || issueCoding.equals("")){
				issueCoding=request.getAttribute("id").toString();
			}
		
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("issueCoding");
		} else {
			id = (String) dform.get("id");
		}
		
		HandoverElevatorCheckItem handoverelevatorcheckitem = null;
		if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					String sql1="select pullid from pulldown where pullname='"+examType+"'";
					Query query1=hs.createSQLQuery(sql1);
					List list1=query1.list();
					if(list1.size()>0){
						examType=(String)list1.get(0);
					}
					Query query=hs.createQuery("from HandoverElevatorCheckItem h where h.id.examType=:type and h.id.checkItem=:item and h.id.issueCoding=:issueId");
					query.setString("issueId", issueCoding);
					query.setString("type", examType);
					query.setString("item", checkItem);
					List list=query.list();
					if(list!=null && list.size()>0){
						handoverelevatorcheckitem=(HandoverElevatorCheckItem) list.get(0);
						String sql2="select pullname from pulldown where pullid='"+handoverelevatorcheckitem.getId().getExamType()+"'";
						Query query2=hs.createSQLQuery(sql2);
						List list2=query2.list();
						if(list2.size()>0){
							handoverelevatorcheckitem.getId().setExamType((String)list2.get(0));;
						}
						dform.set("examType", handoverelevatorcheckitem.getId().getExamType());
						dform.set("checkItem", handoverelevatorcheckitem.getId().getCheckItem());
						dform.set("issueCoding", handoverelevatorcheckitem.getId().getIssueCoding());
						dform.set("issueContents", handoverelevatorcheckitem.getIssueContents());
						dform.set("enabledFlag", handoverelevatorcheckitem.getEnabledFlag());
						dform.set("elevatorType",handoverelevatorcheckitem.getElevatorType());
						dform.set("orderby", handoverelevatorcheckitem.getOrderby());
						dform.set("rem", handoverelevatorcheckitem.getRem());
						dform.set("itemgroup", handoverelevatorcheckitem.getItemgroup());
						
					}
//					handoverelevatorcheckitem = (HandoverElevatorCheckItem) hs.get(HandoverElevatorCheckItem.class, id);
					request.setAttribute("elevatorTypeList", bd.getPullDownList("ElevatorSalesInfo_type"));
					if (handoverelevatorcheckitem == null) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"handoverelevatorcheckitem.display.recordnotfounterror"));
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
			request.setAttribute("handoverElevatorCheckItemBean", handoverelevatorcheckitem);
	//		request.setAttribute("handoverElevatorCheckItemBean", handoverelevatorcheckitem);
			forward = mapping.findForward("handoverElevatorCheckItemModify");
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
		
		String examType = dform.get("examType").toString().trim();//检查类型
		String checkItem = dform.get("checkItem").toString().trim();//检查项目
		String issueCoding=dform.get("issueCoding").toString().trim();//问题编码
		String issueContents=dform.get("issueContents").toString().trim();//问题内容
		String elevatorType =dform.get("elevatorType").toString().trim();//电梯类型
		String enabledFlag =dform.get("enabledFlag").toString().trim();//启用标志
		Double orderby=(Double) dform.get("orderby");
		String rem =dform.get("rem").toString().trim();//备注
		String itemgroup = (String) dform.get("itemgroup");
		
		String examType2 =examType;
		
		HandoverElevatorCheckItem handoverelevatorcheckitem = null;
		try {
			hs=HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			String sql1="select pullid from pulldown where pullname='"+examType+"'";
			Query query1=hs.createSQLQuery(sql1);
			List list1=query1.list();
			if(list1.size()>0){
				examType=(String)list1.get(0);
			}

			if(issueCoding!=null && issueCoding.length()>0){
				List list=hs.createQuery("from HandoverElevatorCheckItem h where h.id.examType='"+examType+"' and h.id.checkItem='"+checkItem+"' and h.id.issueCoding='"+issueCoding+"'").list();
				if(list.size()>0){
					handoverelevatorcheckitem=(HandoverElevatorCheckItem) list.get(0);
				}
				//hs.delete(handoverelevatorcheckitem);
			}
			//HandoverElevatorCheckItemId heciId=new HandoverElevatorCheckItemId();
			//handoverelevatorcheckitem = new HandoverElevatorCheckItem();
			//heciId.setCheckItem(checkItem.trim());
			//heciId.setExamType(examType.trim());
			//heciId.setIssueCoding(issueCoding.trim());
			//handoverelevatorcheckitem.setId(heciId);
			handoverelevatorcheckitem.setOrderby(orderby);
			handoverelevatorcheckitem.setIssueContents(issueContents.trim());
			handoverelevatorcheckitem.setEnabledFlag(enabledFlag.trim());
			handoverelevatorcheckitem.setRem(rem.trim());	
			handoverelevatorcheckitem.setElevatorType(elevatorType.trim());		
			handoverelevatorcheckitem.setOperId(userInfo.getUserID().trim());//录入人
			handoverelevatorcheckitem.setOperDate(CommonUtil.getToday().trim());//录入时间
			handoverelevatorcheckitem.setItemgroup(itemgroup);
			
			hs.update(handoverelevatorcheckitem);

			tx.commit();
		}catch (HibernateException e2) {
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
				request.setAttribute("type", examType2);
				request.setAttribute("id", issueCoding);
				request.setAttribute("item", checkItem);
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
		String examType=(String)dform.get("type");
		String checkItem=(String)dform.get("item");
		
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			examType=new String(examType.getBytes("ISO-8859-1"),"GBK");
			checkItem=new String(checkItem.getBytes("ISO-8859-1"),"GBK");
			String sql="select pullid from pulldown where pullname='"+examType+"'";
			List list1=hs.createSQLQuery(sql).list();
			if(list1.size()>0){
				examType=(String)list1.get(0);
			}

			HandoverElevatorCheckItem handoverelevatorcheckitem=null;
			Query query=hs.createQuery("from HandoverElevatorCheckItem h where h.id.examType=:type and h.id.checkItem=:item and h.id.issueCoding=:issueId");
			query.setString("type", examType);
			query.setString("item", checkItem);
			query.setString("issueId", (String)dform.get("id"));
			List list=query.list();
			if(list!=null && list.size()>0){
				handoverelevatorcheckitem=(HandoverElevatorCheckItem) list.get(0);
			}
//			HandoverElevatorCheckItem handoverelevatorcheckitem = (HandoverElevatorCheckItem) hs.get(HandoverElevatorCheckItem.class, (String) dform.get("id"));
			if (handoverelevatorcheckitem != null) {
				hs.delete(handoverelevatorcheckitem);
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

		String examType = tableForm.getProperty("examType");//检查类型
		String checkItem = tableForm.getProperty("checkItem");//检查项目
		String issueCoding=tableForm.getProperty("issueCoding");//问题编码
		String issueContents=tableForm.getProperty("issueContents");//问题内容
		String elevatorType=tableForm.getProperty("elevatorType");//问题内容
		String enabledFlag = tableForm.getProperty("enabledFlag");//启用标志
		String orderby=tableForm.getProperty("orderby");//序号
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(HandoverElevatorCheckItem.class);
			if (issueCoding != null && !issueCoding.equals("")) {
				criteria.add(Expression.like("id.issueCoding", "%" + issueCoding.trim() + "%"));
			}
			if (issueContents != null && !issueContents.equals("")) {
				criteria.add(Expression.like("issueContents", "%" + issueContents.trim() + "%"));
			}
			if (examType != null && !examType.equals("")) {
				criteria.add(Expression.like("id.examType", "%" + examType.trim() + "%"));
			}
			if (checkItem != null && !checkItem.equals("")) {
				criteria.add(Expression.like("id.checkItem", "%" + checkItem.trim()
						+ "%"));
			}
			if (elevatorType != null && !elevatorType.equals("")) {
				criteria.add(Expression.eq("elevatorType", elevatorType));
			}
			if (orderby != null && !orderby.equals("")) {
				criteria.add(Expression.eq("orderby", orderby));
			}
			
			if (enabledFlag != null && !enabledFlag.equals("")) {
				criteria.add(Expression.eq("enabledFlag", enabledFlag));
			}

			criteria.addOrder(Order.asc("id.issueCoding"));

			List roleList = criteria.list();

			XSSFSheet sheet = wb.createSheet("交接电梯检查项目"
					+ ".");
			
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"handoverelevatorcheckitem.examType"));
				
				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"handoverelevatorcheckitem.checkItem"));
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"handoverelevatorcheckitem.issueCoding"));
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"handoverelevatorcheckitem.issueContents"));
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"handoverelevatorcheckitem.enabledFlag"));
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("电梯类型");
				
				cell0 = row0.createCell((short)6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("排序号");
				
				cell0 = row0.createCell((short)7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("小组编号");
					
				cell0 = row0.createCell((short)8);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"handoverelevatorcheckitem.rem"));
				
				cell0 = row0.createCell((short)9);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"handoverelevatorcheckitem.operId"));
				
				cell0 = row0.createCell((short)10);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,"handoverelevatorcheckitem.operDate"));
				
				for (int i = 0; i < l; i++) {
					HandoverElevatorCheckItem handoverelevatorcheckitem = (HandoverElevatorCheckItem) roleList.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);
	
					// 创建Excel列
					XSSFCell cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName(hs, "Pulldown", "pullname", "pullid",handoverelevatorcheckitem.getId().getExamType()));
					
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(handoverelevatorcheckitem.getId().getCheckItem());
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(handoverelevatorcheckitem.getId().getIssueCoding());
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(handoverelevatorcheckitem.getIssueContents());
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(handoverelevatorcheckitem.getEnabledFlag()));
					
					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String elevatorType1=handoverelevatorcheckitem.getElevatorType().trim();
					if(elevatorType1.trim().equals("T"))
					{
						cell.setCellValue("直梯");
					}else
					{
						cell.setCellValue("扶梯");
					}

					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(handoverelevatorcheckitem.getOrderby());
					
					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(handoverelevatorcheckitem.getItemgroup());
					
					cell = row.createCell((short)8);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(handoverelevatorcheckitem.getRem());

					cell = row.createCell((short)9);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName(hs, "LoginUser", "userName", "userId",handoverelevatorcheckitem.getOperId()));

					cell = row.createCell((short)10);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(handoverelevatorcheckitem.getOperDate());

				}
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
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("交接电梯检查项目", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	
	/**
	 * 获取负责人弹出窗口列表
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrincipalSelectList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("navigator.location","");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
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
			HTMLTableCache cache = new HTMLTableCache(session, "principalList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fPrincipalSel");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("principalId");
			table.setIsAscending(true);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);

			String principalName = tableForm.getProperty("principalName");
			String principalId = tableForm.getProperty("principalId");
			Session hs = null;
			try {
				hs = HibernateUtil.getSession();

				Criteria criteria = hs.createCriteria(Principal.class);
				criteria.add(Expression.eq("enabledFlag", "Y"));
				if (principalId != null && !principalId.equals("")) {
					criteria.add(Expression.like("principalId", "%" + principalId.trim()
							+ "%"));
				}
				if (principalName != null && !principalName.equals("")) {
					criteria.add(Expression.like("principalName", "%"
							+ principalName.trim() + "%"));
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

				List principalList = criteria.list();

				table.addAll(principalList);
				session.setAttribute("principalList", table);

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
			forward = mapping.findForward("principalSelect");
		}
		return forward;
	}

}
