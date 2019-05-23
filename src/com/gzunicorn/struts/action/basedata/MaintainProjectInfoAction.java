package com.gzunicorn.struts.action.basedata;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.gzunicorn.hibernate.MaintainProjectInfo;
import com.gzunicorn.hibernate.basedata.region.Region;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class MaintainProjectInfoAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintainProjectInfoAction.class);
	
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
				SysRightsUtil.NODE_ID_FORWARD + "MaintainProjectInfo", null);
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

		request.setAttribute("navigator.location","	保养项目信息>> 查询列表");
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
			HTMLTableCache cache = new HTMLTableCache(session, "MaintainProjectInfoList");
		
			
			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fMaintainProjectInfo");
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

			String MaintType = tableForm.getProperty("maintType");
			String MaintItem = tableForm.getProperty("maintItem");
			String MaintContents=tableForm.getProperty("maintContents");
			String elevatorType = tableForm.getProperty("elevatorType");
			String enabledFlag = tableForm.getProperty("enabledFlag");
			Query query =null;
			Session hs = null;

			try {

				hs = HibernateUtil.getSession();
				Criteria criteria = hs.createCriteria(MaintainProjectInfo.class);
				if (MaintType != null && !MaintType.equals("")) {
					criteria.add(Expression.like("maintType", "%" + MaintType.trim()
							+ "%"));
				}
				if (MaintItem != null && !MaintItem.equals("")) {
					criteria.add(Expression.like("maintItem", "%"
							+ MaintItem.trim() + "%"));
				}
				if (MaintContents != null && !MaintContents.equals("")) {
					criteria.add(Expression.like("maintContents", "%"
							+ MaintContents.trim() + "%"));
				}
				if (elevatorType != null && !elevatorType.equals("")) {
					criteria.add(Expression.like("elevatorType", "%"
							+ elevatorType.trim() + "%"));
				}
				
				if (enabledFlag != null && !enabledFlag.equals("")) {
					criteria.add(Expression.eq("enabledFlag", enabledFlag));
				}

				if (table.getIsAscending()) {
					criteria.addOrder(Order.asc(table.getSortColumn()));
				} else {
					criteria.addOrder(Order.desc(table.getSortColumn()));
				}
				// System.out.println(cityid);
				table.setVolume(criteria.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				criteria.setFirstResult(table.getFrom()); // pagefirst
				criteria.setMaxResults(table.getLength());

				cache.check(table);

				List MaintainProjectInfoList = criteria.list();
				BaseDataImpl bd = new BaseDataImpl();
				if (MaintainProjectInfoList != null) {
					for (int i = 0; i < MaintainProjectInfoList.size(); i++) {
						MaintainProjectInfo a = (MaintainProjectInfo) MaintainProjectInfoList.get(i);
						if(a.getElevatorType().equals("T")){
						a.setElevatorType("直梯");
						}else{
						a.setElevatorType("扶梯");
						}
					/*	if(a.getEnabledFlag()=="Y"){
							a.setEnabledFlag("是");
							}else{
							a.setEnabledFlag("否");
							}*/
						if(a.getMaintType().equals("halfMonth")){
							a.setMaintType("半月保养");
							}
						if(a.getMaintType().equals("quarter")){
							a.setMaintType("季度保养");
							}
						if(a.getMaintType().equals("halfYear")){
							a.setMaintType("半年保养");
							}
						if(a.getMaintType().equals("yearDegree")){
							a.setMaintType("年度保养");
							}
					}
				}

				table.addAll(MaintainProjectInfoList);

			

				session.setAttribute("MaintainProjectInfoList", table);
				session.setAttribute("elevatorTypeList", bd.getPullDownList("ElevatorSalesInfo_type"));
				session.setAttribute("maintTypeList", bd.getPullDownList("MaintType"));
				

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
			forward = mapping.findForward("MaintainProjectInfoList");
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
		request.setAttribute("navigator.location","保养项目信息>> 查看");
		
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;
		
		String id = (String) dform.get("id");
		Integer id1=Integer.parseInt(id);
		Session hs = null;
		MaintainProjectInfo mpi = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
					/*String sql2="select * from MaintainProjectInfo where numno = '"+id+"'";
					List list2=hs.createSQLQuery(sql2).list();*/
					mpi=(MaintainProjectInfo) hs.get(MaintainProjectInfo.class, id1);
					if (mpi == null) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"MaintainProjectInfo.display.recordnotfounterror"));
					}else{
					if(mpi.getMaintType().equals("halfMonth")){
						mpi.setMaintType("半月保养");
						}
					if(mpi.getMaintType().equals("quarter")){
						mpi.setMaintType("季度保养");
						}
					if(mpi.getMaintType().equals("halfYear")){
						mpi.setMaintType("半年保养");
						}
					if(mpi.getMaintType().equals("yearDegree")){
						mpi.setMaintType("年度保养");
						}
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
			request.setAttribute("MaintainProjectInfoBean", mpi);
			
			forward = mapping.findForward("MaintainProjectInfoDisplay");

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

		request.setAttribute("navigator.location","保养项目信息 >> 导入");

		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}
		request.setAttribute("elevatorTypeList", bd.getPullDownList("ElevatorSalesInfo_type"));
		request.setAttribute("maintTypeList", bd.getPullDownList("MaintType"));
		return mapping.findForward("MaintainProjectInfoImport");
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
		String maintType = "";
		String elevatorType="";
		maintType = (String) dform.get("maintType");//检查类型
	    elevatorType=(String) dform.get("elevatorType");//电梯类型
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
				MaintainProjectInfo master = null; //交接电梯信息检查表
				List<MaintainProjectInfo> list = new ArrayList<MaintainProjectInfo>();
				
				in = file.getInputStream();
				XSSFWorkbook wb = new XSSFWorkbook(in);
				XSSFSheet sheet = wb.getSheetAt(0);
				XSSFRow row = null;			
				
				int rowSum = sheet.getLastRowNum()+1; //最大行数

				Integer numno = null;
				String MaintItem="";
				String MaintContents="";
				String id1 = "";
				String id2 = "";
				String id3 = "";
				String userid=userInfo.getUserID();
				String today=CommonUtil.getToday();
				String sql = null;
				for(int rowNum = 1; rowNum < rowSum; rowNum++){					
					row = sheet.getRow(rowNum);			
					MaintContents=cellValueToString(row, 2, reStr);							
					MaintItem=cellValueToString(row, 1, reStr);
					if(reStr != null && reStr.length() > 0){
						break;
					}
					sql = "select orderby from MaintainProjectInfo where maintType ='"+maintType+"'and  elevatorType='"+elevatorType+"'and  maintItem='"+MaintItem+"' and maintContents='"+MaintContents+"'";
					Query query1 = hs.createQuery(sql);
					//System.out.print(sql);
					List list1= query1.list();
					if(list1!=null&&!list1.equals("")&&list1.size()>0){																	
							String hql = "update MaintainProjectInfo set orderby='"+cellValueToInt(row, 0, reStr)+"' where maintType ='"+maintType+"' and elevatorType='"+elevatorType+"' and maintItem='"+MaintItem+"' and maintContents='"+MaintContents+"'  "; //删除旧版本数据
							Query query = hs.createQuery(hql); 
							query.executeUpdate();
				}else if (!MaintContents.equals("")||!MaintItem.equals("")){
					master = new MaintainProjectInfo();	
					master.setMaintItem(MaintItem);
					master.setMaintContents(MaintContents);
					master.setOrderby(cellValueToInt(row, 0, reStr));//排序号
					//master.setMaintContents(MaintContents);
					master.setElevatorType(elevatorType);
					master.setMaintType(maintType);
					master.setRem("");
					master.setOperId(userid);
					master.setOperDate(today);
					master.setEnabledFlag("Y");
					hs.save(master);
					//hs.flush();
				}
				//根据 检查类型+检查项目+问题编码 进行删除数据。
				

                     
				}
				
					
				
			
			}else {
				request.setAttribute("reStr", reStr);//错误返回信息
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
			//reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)为空;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
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
			//reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)为空;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			value = (int) cell.getNumericCellValue();
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

		request.setAttribute("navigator.location","保养项目信息>> 添加");
		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
			dform.set("enabledFlag", "Y");
		}
		dform.set("enabledFlag", "Y");
//		request.setAttribute("examTypeList", bd.getPullDownList("HandoverElevatorCheckItem_ExamType"));
		request.setAttribute("elevatorTypeList", bd.getPullDownList("ElevatorSalesInfo_type"));
		request.setAttribute("maintTypeList", bd.getPullDownList("MaintType"));
		return mapping.findForward("MaintainProjectInfoAdd");
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
		String maintType = (String) dform.get("maintType");//检查类型
		String maintItem = (String) dform.get("maintItem");//检查项目
		String maintContents=(String) dform.get("maintContents");//问题编码
		//Integer numno=(Integer) dform.get("numno");//问题内容
		String elevatorType=(String) dform.get("elevatorType");//电梯类型
		String enabledFlag = (String) dform.get("enabledFlag");//启用标志
		Integer orderby=(Integer) dform.get("orderby");//排序号
		String rem = (String) dform.get("rem");//备注
/*		String itemgroup = (String) dform.get("itemgroup");*/
		
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
		
			MaintainProjectInfo MaintainProjectInfo = new MaintainProjectInfo();
			
			MaintainProjectInfo.setMaintType(maintType.trim());
			MaintainProjectInfo.setMaintItem(maintItem.trim());
			MaintainProjectInfo.setMaintContents(maintContents.trim());
			MaintainProjectInfo.setElevatorType(elevatorType.trim());
			MaintainProjectInfo.setEnabledFlag(enabledFlag.trim());
			//MaintainProjectInfo.setNumno(numno);
			MaintainProjectInfo.setRem(rem.trim());	
			MaintainProjectInfo.setElevatorType(elevatorType.trim());
			MaintainProjectInfo.setOperId(userInfo.getUserID().trim());//录入人
			MaintainProjectInfo.setOperDate(CommonUtil.getToday().trim());//录入时间
			MaintainProjectInfo.setOrderby(orderby);//排序号
			//MaintainProjectInfo.setItemgroup(itemgroup);
			
			hs.save(MaintainProjectInfo);
			tx.commit();
		} catch (HibernateException e2) {
		
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("MaintainProjectInfo.insert.duplicatekeyerror"));
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
		
		request.setAttribute("navigator.location","保养项目信息 >> 修改");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
//		request.setAttribute("examTypeList", bd.getPullDownList("HandoverElevatorCheckItem_ExamType"));

		ActionForward forward = null;
		String id = null;
		Session hs = null;
		id = (String) dform.get("id");

		MaintainProjectInfo mpi = null;
		if (id != null) {
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					hs = HibernateUtil.getSession();
					String sql = "from MaintainProjectInfo h where h.numno = '"+id+"'";
					Query query=hs.createQuery(sql);
					//query.setString("issueId", issueCoding);
					//query.setString("type", examType);
					//query.setString("item", checkItem);
					List list=query.list();
					if(list!=null && list.size()>0){
						mpi=(MaintainProjectInfo) list.get(0);					
						dform.set("maintType", mpi.getMaintType());
						dform.set("maintItem", mpi.getMaintItem());
						dform.set("maintContents", mpi.getMaintContents());
						dform.set("numno", mpi.getNumno());
						dform.set("enabledFlag", mpi.getEnabledFlag());
						dform.set("elevatorType",mpi.getElevatorType());
						dform.set("orderby", mpi.getOrderby());
						dform.set("rem", mpi.getRem());
						//dform.set("itemgroup", handoverelevatorcheckitem.getItemgroup());
						
					}
//					handoverelevatorcheckitem = (HandoverElevatorCheckItem) hs.get(HandoverElevatorCheckItem.class, id);
					request.setAttribute("elevatorTypeList", bd.getPullDownList("ElevatorSalesInfo_type"));
					request.setAttribute("maintTypeList", bd.getPullDownList("maintType"));
					if (mpi == null) {
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"MaintainProjectInfo.display.recordnotfounterror"));
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
			request.setAttribute("MaintainProjectInfoBean", mpi);
			request.setAttribute("id", id);
	//		request.setAttribute("handoverElevatorCheckItemBean", handoverelevatorcheckitem);
			forward = mapping.findForward("MaintainProjectInfoModify");
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
		String id = (String) dform.get("id");
		Integer id1=Integer.parseInt(id);
		String maintType = dform.get("maintType").toString().trim();//检查类型
		String maintItem = dform.get("maintItem").toString().trim();//检查项目
		String maintContents=dform.get("maintContents").toString().trim();//问题编码
		Integer Numno=(Integer)dform.get("numno");//问题内容
		String elevatorType =dform.get("elevatorType").toString().trim();//电梯类型
		String enabledFlag =dform.get("enabledFlag").toString().trim();//启用标志
		Integer  orderby=(Integer) dform.get("orderby");
		String rem =dform.get("rem").toString().trim();//备注
		//String itemgroup = (String) dform.get("itemgroup");
		
		MaintainProjectInfo mpi = null;
		try {
			hs=HibernateUtil.getSession();
			tx = hs.beginTransaction();
			

//			if(id!=null && id.length()>0){
//				List list=hs.createQuery("from MaintainProjectInfo h where h.numno='"+id.trim()+"'").list();
//				if(list.size()>0){
//					MaintainProjectInfo=(MaintainProjectInfo) list.get(0);
//				}
//				hs.delete(MaintainProjectInfo);
//			}
			mpi=(MaintainProjectInfo)hs.get(MaintainProjectInfo.class, id1);
			mpi.setOrderby(orderby);
			mpi.setMaintContents(maintContents.trim());
			mpi.setEnabledFlag(enabledFlag.trim());
			mpi.setRem(rem.trim());	
			mpi.setElevatorType(elevatorType.trim());		
			mpi.setOperId(userInfo.getUserID().trim());//录入人
			mpi.setOperDate(CommonUtil.getToday().trim());//录入时间
			mpi.setMaintType(maintType.trim());
			mpi.setMaintItem(maintItem.trim());
			
			hs.save(mpi);

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
		String id = (String) dform.get("id");
		Integer id1=Integer.parseInt(id);
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();


			MaintainProjectInfo mpi=null;
			mpi=(MaintainProjectInfo) hs.get(MaintainProjectInfo.class, id1);
//			HandoverElevatorCheckItem handoverelevatorcheckitem = (HandoverElevatorCheckItem) hs.get(HandoverElevatorCheckItem.class, (String) dform.get("id"));
			if (mpi != null) {
				hs.delete(mpi);
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

		String maintType = tableForm.getProperty("maintType");//检查类型
		String maintItem = tableForm.getProperty("maintItem");//检查项目
		//String issueCoding=tableForm.getProperty("issueCoding");//问题编码
		String maintContents=tableForm.getProperty("maintContents");//问题内容
		String elevatorType=tableForm.getProperty("elevatorType");//问题内容
		String enabledFlag = tableForm.getProperty("enabledFlag");//启用标志
		String orderby=tableForm.getProperty("orderby");//序号
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(MaintainProjectInfo.class);
			/*if (issueCoding != null && !issueCoding.equals("")) {
				criteria.add(Expression.like("id.issueCoding", "%" + issueCoding.trim() + "%"));
			}*/
			if (maintType != null && !maintType.equals("")) {
				criteria.add(Expression.like("maintType", "%" + maintType.trim() + "%"));
			}
			if (maintItem != null && !maintItem.equals("")) {
				criteria.add(Expression.like("maintItem", "%" + maintItem.trim() + "%"));
			}
			if (maintContents != null && !maintContents.equals("")) {
				criteria.add(Expression.like("maintContents", "%" + maintContents.trim()
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
			criteria.addOrder(Order.asc("orderby"));
			criteria.addOrder(Order.asc("elevatorType"));

			List roleList = criteria.list();

			XSSFSheet sheet = wb.createSheet("保养项目信息"
					+ ".");
			
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("排序号");
				
				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("电梯类型");
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("保养类型");
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("维保项目");
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("维保基本要求");
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("启用标志");
				
				
				for (int i = 0; i < l; i++) {
					MaintainProjectInfo mpi = (MaintainProjectInfo) roleList.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);
	
					// 创建Excel列
					XSSFCell cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(mpi.getOrderby());
					
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String elevatorType1=mpi.getElevatorType().trim();
					if(elevatorType1.trim().equals("T"))
					{
						cell.setCellValue("直梯");
					}else
					{
						cell.setCellValue("扶梯");
					}
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String maintType1=mpi.getMaintType().trim();
					if(maintType1.trim().equals("halfMonth"))
					{
						cell.setCellValue("半月保养");
					}
					if(maintType1.trim().equals("quarter"))
					{
						cell.setCellValue("季度保养");
					}
					if(maintType1.trim().equals("halfYear"))
					{
						cell.setCellValue("半年保养");
					}
					if(maintType1.trim().equals("yearDegree"))
					{
						cell.setCellValue("年度保养");
					}
					
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(mpi.getMaintItem());
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(mpi.getMaintContents());
					
					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(CommonUtil.tranEnabledFlag(mpi.getEnabledFlag()));
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("保养项目信息", "utf-8") + ".xlsx");
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
