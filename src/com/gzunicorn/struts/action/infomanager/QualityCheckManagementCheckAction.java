package com.gzunicorn.struts.action.infomanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.util.MessageResources;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.pdfprint.BarCodePrint;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.JSONUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.file.HandleFile;
import com.gzunicorn.file.HandleFileImpA;
import com.gzunicorn.hibernate.basedata.Fileinfo;
import com.gzunicorn.hibernate.basedata.markingitems.MarkingItems;
import com.gzunicorn.hibernate.basedata.principal.Principal;
import com.gzunicorn.hibernate.basedata.shouldexamineitems.ShouldExamineItems;
import com.gzunicorn.hibernate.basedata.termsecurityrisks.TermSecurityRisks;
import com.gzunicorn.hibernate.hotlinemanagement.calloutmaster.CalloutMaster;
import com.gzunicorn.hibernate.hotlinemanagement.calloutprocess.CalloutProcess;
import com.gzunicorn.hibernate.basedata.elevatorcoordinatelocation.ElevatorCoordinateLocation;
import com.gzunicorn.hibernate.infomanager.markingitemscomply.MarkingItemsComply;
import com.gzunicorn.hibernate.infomanager.markingscoreregister.MarkingScoreRegister;
import com.gzunicorn.hibernate.infomanager.markingscoreregisterdetail.MarkingScoreRegisterDetail;
import com.gzunicorn.hibernate.infomanager.markingscoreregisterfileinfo.MarkingScoreRegisterFileinfo;
import com.gzunicorn.hibernate.infomanager.qualitycheckmanagement.QualityCheckManagement;
import com.gzunicorn.hibernate.infomanager.qualitycheckprocess.QualityCheckProcess;
import com.gzunicorn.hibernate.infomanager.shouldexamineitemscomply.ShouldExamineItemsComply;
import com.gzunicorn.hibernate.infomanager.termsecurityriskscomply.TermSecurityRisksComply;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.workflow.bo.JbpmExtBridge;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class QualityCheckManagementCheckAction extends DispatchAction {

	Log log = LogFactory.getLog(QualityCheckManagementCheckAction.class);

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

		// Set default method is toSearchRecord
		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "qualityCheckManagementCheck", null);
		/** **********结束用户权限过滤*********** */
		
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

		request.setAttribute("navigator.location", "  维保质量检查查看>> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		request.setAttribute("userroleid", userInfo.getRoleID());

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form, request, response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport", "");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session,"qualityCheckManagementCheckList");
			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fQualityCheckManagementCheck");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			// table.setLength(1);
			cache.updateTable(table);
			table.setSortColumn("billno");
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

			String superviseId = tableForm.getProperty("superviseId");//督查人员
			String elevatorNo = tableForm.getProperty("elevatorNo");
			String maintContractNo = tableForm.getProperty("maintContractNo");
			String projectName = tableForm.getProperty("projectName");
			String maintDivision = tableForm.getProperty("maintDivision");
			String maintPersonnel = tableForm.getProperty("maintPersonnel");
			String submitType = tableForm.getProperty("submitType");
			String processStatus = tableForm.getProperty("processStatus");
			String maintStation = tableForm.getProperty("maintStation");
			
//			if (maintDivision == null || maintDivision == "") {
//				maintDivision = userInfo.getComID();
//				if (maintDivision.equals("00")) {
//					maintDivision = "%";
//				}
//			}
			//分部下拉框列表
			List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
			if (maintDivision == null || maintDivision.trim().equals("")) {
				HashMap hmap=(HashMap)maintDivisionList.get(0);
				maintDivision =(String)hmap.get("grcid");
			}
			request.setAttribute("maintDivisionList", maintDivisionList);
			
            List mslist= bd.getMaintStationList2(userInfo,maintDivision);
			if (maintStation == null || maintStation.trim().equals("")) {
				HashMap hmap=(HashMap)mslist.get(0);
				maintStation =(String)hmap.get("storageid");
			}
			request.setAttribute("mainStationList",mslist);
			
			String showstate="Y";
			//A03 维保经理, A02 维保分部长  
//			if(userInfo.getRoleID().equals("A03") || userInfo.getRoleID().equals("A02")){
//				showstate="N";
//				if(processStatus==null || processStatus.equals("")){
//					processStatus="2";
//					tableForm.setProperty("processStatus",processStatus);
//				}
//			}
			request.setAttribute("showstate", showstate);
			
			Session hs = null;
			Query query = null;

			try {

				hs = HibernateUtil.getSession();

				String hql = "select q.billno,q.elevatorNo,q.maintContractNo,q.projectName," +
						"q.maintDivision,q.maintStation,q.maintPersonnel,q.submitType,q.processStatus," +
						"q.status,q.processName,q.superviseId,l.username,l2.username " +
						"from QualityCheckManagement q,Loginuser l,Loginuser l2 ";
				String condition=" where q.superviseId=l.userid and q.maintPersonnel=l2.userid "
						+" and processStatus in ('2','3')";
				if (elevatorNo != null && !elevatorNo.equals("")) {
					condition+=" and q.elevatorNo like '%" + elevatorNo.trim()+ "%'";
				}
				if (maintContractNo != null && !maintContractNo.equals("")) {
						condition+=" and q.maintContractNo like '%"+ maintContractNo.trim() + "%'";
				}
				if (projectName != null && !projectName.equals("")) {
						condition+=" and q.projectName like '%" + projectName.trim()+ "%'";
				}
				if (maintDivision != null && !maintDivision.equals("")) {
						condition+=" and q.maintDivision like '" + maintDivision.trim() + "'";
				}
				if (maintStation != null && !maintStation.equals("")) {
					condition+=" and q.maintStation like '" + maintStation.trim() + "'";
				}
//				if (submitType != null && !submitType.equals("")) {
//						condition+=" and q.submitType='" + submitType.trim() + "'";
//				}
//				if (processStatus != null && !processStatus.equals("")) {
//					condition+=" and q.processStatus='" + processStatus.trim() + "'";
//				}
				if (superviseId != null && !superviseId.equals("")) {
						condition+=" and (q.superviseId like '%" + superviseId.trim()+ "%' or l.username like '%" + superviseId.trim()+ "%')";
				}
				if (maintPersonnel != null && !maintPersonnel.equals("")) {
					condition+=" and (q.maintPersonnel like '%" + maintPersonnel.trim()+ "%' or l2.username like '%" + maintPersonnel.trim()+ "%')";
				}
				
				if (table.getIsAscending()) {
					condition += " order by " + table.getSortColumn() + " desc";
				} else {
					condition += " order by " + table.getSortColumn() + " asc";
				}
				
				//System.out.println(hql+condition);
				
				query = hs.createQuery(hql+condition);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());
				cache.check(table);

				List list = query.list();
				List qualityCheckManagementCheckList = new ArrayList();
				for (Object object : list) {
					Object[] values = (Object[]) object;
					Map map = new HashMap();
					map.put("billno", values[0]);
					map.put("elevatorNo", values[1]);
					map.put("maintContractNo", values[2]);
					map.put("projectName", values[3]);
					if(values[4]==null){
						map.put("maintDivision", values[4]);
					}else{
						map.put("maintDivision", bd.getName_Sql("Company", "comname", "comid", values[4].toString()));
					}
					if(values[5]==null){
						map.put("maintStation",values[5]);
					}else{
						map.put("maintStation", bd.getName_Sql("Storageid", "storagename", "storageid", values[5].toString()));
					}
					
					map.put("submitType", values[7]);
					map.put("processStatus", values[8]);
					map.put("status", bd.getName_Sql("ViewFlowStatus", "typename", "typeid", String.valueOf(values[9])));
					map.put("processName", values[10]);
					map.put("superviseId", values[12]);
					map.put("maintPersonnel", values[13]);
					
					qualityCheckManagementCheckList.add(map);
				}

				table.addAll(qualityCheckManagementCheckList);
				session.setAttribute("qualityCheckManagementCheckList", table);

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
			forward = mapping.findForward("qualityCheckManagementCheckList");
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

		String elevatorNo = tableForm.getProperty("elevatorNo");
		String maintContractNo = tableForm.getProperty("maintContractNo");
		String projectName = tableForm.getProperty("projectName");
		String maintDivision = tableForm.getProperty("maintDivision");
		String maintStation = tableForm.getProperty("maintStation");
		String maintPersonnel = tableForm.getProperty("maintPersonnel");
		String submitType = tableForm.getProperty("submitType");
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			Criteria criteria = hs.createCriteria(QualityCheckManagement.class);
			if (elevatorNo != null && !elevatorNo.equals("")) {
				criteria.add(Expression.like("elevatorNo",
						"%" + elevatorNo.trim() + "%"));
			}
			if (maintContractNo != null && !maintContractNo.equals("")) {
				criteria.add(Expression.like("maintContractNo", "%"
						+ maintContractNo.trim() + "%"));
			}
			if (projectName != null && !projectName.equals("")) {
				criteria.add(Expression.like("projectName",
						"%" + projectName.trim() + "%"));
			}

			if (maintDivision != null && !maintDivision.equals("")) {
				criteria.add(Expression.like("maintDivision", "%"
						+ maintDivision.trim() + "%"));
			}
			if (maintStation != null && !maintStation.equals("")) {
				criteria.add(Expression.like("maintStation",
						"%" + maintStation.trim() + "%"));
			}
			if (maintPersonnel != null && !maintPersonnel.equals("")) {
				criteria.add(Expression.like("maintPersonnel", maintPersonnel));
			}
			if (submitType != null && !submitType.equals("")) {
				criteria.add(Expression.eq("submitType", submitType));
			}

			criteria.addOrder(Order.asc("billno"));

			List roleList = criteria.list();

			XSSFSheet sheet = wb.createSheet("维保质量检查管理");

			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			String[] columnNames = { "billno", "maintDivision", "maintStation",
					"maintPersonnel", "projectName", "maintContractNo",
					"elevatorNo", "checksPeople", "checksDateTime",
					"totalPoints", "scoreLevel", "submitType", "supervOpinion",
					"partMinisters", "partMinistersRem", "remDate", "operId",
					"operDate" };

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);

				for (int i = 0; i < columnNames.length; i++) {
					XSSFCell cell0 = row0.createCell((short) i);
					cell0.setCellValue(messages.getMessage(locale,
							"qualityCheckManagement." + columnNames[i]));
				}

				Class<?> superClazz = QualityCheckManagement.class
						.getSuperclass();
				for (int i = 0; i < l; i++) {
					QualityCheckManagement master = (QualityCheckManagement) roleList
							.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i + 1);
					for (int j = 0; j < columnNames.length; j++) {
						// 创建Excel列
						XSSFCell cell = row.createCell((short) j);
						cell.setCellValue(getValue(master, superClazz,
								columnNames[j]));
					}
				}
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

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="
				+ URLEncoder.encode("维保质量检查管理", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());

		return response;
	}
	
	/**
	 * 获得对象指定的get方法并返回执行该get方法后的值
	 * 
	 * @param object
	 *            javabean对象
	 * @param superClazz
	 *            object的类，子类没有相应get方法时请传入object的父类
	 * @param fieldName
	 *            属性名
	 * @return ActionForward
	 */
	private String getValue(Object object, Class<?> superClazz, String fieldName) {
		String value = null;
		String methodName = "get"
				+ fieldName.replaceFirst(fieldName.substring(0, 1), fieldName
						.substring(0, 1).toUpperCase());
		try {
			Method method = superClazz.getMethod(methodName);
			value = method.invoke(object, null) + "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;

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

		request.setAttribute("navigator.location", "维保质量检查查看 >> 查看");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;

		String id = (String) dform.get("id");
		display(dform, request, errors, "display");
		
		request.setAttribute("display", "yes");
		forward = mapping.findForward("qualityCheckManagementCheckDisplay");
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	public void display(ActionForm form, HttpServletRequest request ,ActionErrors errors ,String flag){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;		
//		String id = request.getParameter("id");
		String id = (String) dform.get("id");
		if(id==null || "".equals(id)){
			id=(String)dform.get("billno");
		}
		
		String CSheight = PropertiesUtil.getProperty("CSheight");
		String CSwidth = PropertiesUtil.getProperty("CSwidth");
		String CIheight = PropertiesUtil.getProperty("CIheight");
		String CIwidth = PropertiesUtil.getProperty("CIwidth");
		Session hs = null;
		List markingItemsComplyList = new ArrayList(),deleList=new ArrayList();
		QualityCheckManagement manage=new QualityCheckManagement();
		List elevaorTypeList=bd.getPullDownList("ElevatorSalesInfo_type");
		if (id != null && !id.equals("")) {			
			try {
				hs = HibernateUtil.getSession();
				String hql="select a,b.comname,c.storagename from QualityCheckManagement a,Company b,Storageid c"+
						" where a.maintDivision=b.comid and a.maintStation=c.storageid and a.billno='"+id.trim()+"'";
				Query query = hs.createQuery(hql);
				List list = query.list();
				if (list != null && list.size() > 0) {
					String mstation="";
					String r5str="";
					for(Object object : list){
						Object[] objs=(Object[])object;
						manage=(QualityCheckManagement) objs[0];
						mstation=manage.getMaintStation();
						r5str=manage.getR5();//保养参与人员
						manage.setMaintDivision(objs[1].toString());
						manage.setMaintStation(objs[2].toString());
						manage.setMaintPersonnel(bd.getName("Loginuser", "username", "userid", manage.getMaintPersonnel()));
						manage.setChecksPeople(bd.getName("Loginuser", "username", "userid", manage.getChecksPeople()));
						manage.setPartMinisters(bd.getName("Loginuser", "username", "userid", manage.getPartMinisters()));
						manage.setSuperviseId(bd.getName("Loginuser", "username", "userid", manage.getSuperviseId()));
					}
					if("dispose".equals(flag)){
						manage.setChecksDateTime(CommonUtil.getNowTime());
					}
					
					//评分信息
					hql="from MarkingScoreRegister where billno='"+manage.getBillno()+"' and isDelete='N'";
					list=hs.createQuery(hql).list();
					for(Object object : list){
						MarkingScoreRegister msr=(MarkingScoreRegister)object;
						Map map=new HashMap();
						map.put("jnlno", msr.getJnlno());
						map.put("msId", msr.getMsId());
						map.put("msName", msr.getMsName());
						map.put("fraction", msr.getFraction());
						map.put("rem", msr.getRem());
						List detailList=hs.createQuery("from MarkingScoreRegisterDetail where jnlno='"+msr.getJnlno()+"'").list();
						map.put("detailList", detailList);
						List fileList=hs.createQuery("from MarkingScoreRegisterFileinfo where jnlno='"+msr.getJnlno()+"'").list();
						map.put("fileList", fileList);
						markingItemsComplyList.add(map);
					}
					hql="from MarkingScoreRegister where billno='"+manage.getBillno()+"' and isDelete='Y'";
					list=hs.createQuery(hql).list();
					if(list!=null && list.size()>0){
						for(Object object : list){
							MarkingScoreRegister msr=(MarkingScoreRegister)object;
							Map map=new HashMap();
							map.put("jnlno", msr.getJnlno());
							map.put("msId", msr.getMsId());
							map.put("msName", msr.getMsName());
							map.put("fraction", msr.getFraction());
							map.put("rem", msr.getRem());
							List detailList=hs.createQuery("from MarkingScoreRegisterDetail where jnlno='"+msr.getJnlno()+"'").list();
							map.put("detailList", detailList);
							List fileList=hs.createQuery("from MarkingScoreRegisterFileinfo where jnlno='"+msr.getJnlno()+"'").list();
							map.put("fileList", fileList);
							map.put("isDelet", msr.getIsDelete());
							map.put("deleteRem", msr.getDeleteRem());
							deleList.add(map);
						}
					}
					
					//审批流程信息
					List processApproveList = hs.createQuery("from QualityCheckProcess where billno = '"+(String) dform.get("id")+ "' order by itemId").list();
					for (Object object : processApproveList) {
						QualityCheckProcess process = (QualityCheckProcess) object;
						process.setUserId(bd.getName(hs, "Loginuser", "username", "userid", process.getUserId()));
					}
					
					if(manage.getProcessStatus().equals("2")||manage.getProcessStatus().equals("3")){
						request.setAttribute("isStatus", "Y");
					}else{
						request.setAttribute("isStatus", "N");
					}
					

					String r5name="";
					if(r5str!=null && !r5str.trim().equals("")){
						//维保站人员 维保站长A49，维保工A50,维保经理 A03，维修技术员A53
						String whql="select l.userid,l.username,isnull(b.userid,'N') from loginuser l "
								+ "left join (select userid from loginuser "
								+ "where userid in('"+r5str.replaceAll(",", "','")+"')) b on l.userid=b.userid "
								+ "where l.storageid like '"+mstation+"%' "
								+ "and l.enabledflag='Y' and l.RoleID in('A50','A49','A03','A53') "
								+ "order by l.RoleID";
						List msuserlist=hs.createSQLQuery(whql).list();
						List userlist=new ArrayList();
						for(int l=0;l<msuserlist.size();l++){
							Object[] obj=(Object[])msuserlist.get(l);
							HashMap map=new HashMap();
							map.put("userid", obj[0]+"");
							map.put("username", obj[1]+"");
							map.put("isok", obj[2]+"");
							if(!obj[2].toString().equals("N")){
								r5name+=obj[1]+",";
							}
							userlist.add(map);
						}
						request.setAttribute("msUserList", userlist);
					}else{
						//维保站人员 维保站长A49，维保工A50,维保经理 A03，维修技术员A53
						String whql="select l.userid,l.username from loginuser l "
								+ "where l.storageid like '"+mstation+"%' "
								+ "and l.enabledflag='Y' and l.RoleID in('A50','A49','A03','A53') "
								+ "order by l.RoleID";
						List msuserlist=hs.createSQLQuery(whql).list();
						List userlist=new ArrayList();
						for(int l=0;l<msuserlist.size();l++){
							Object[] obj=(Object[])msuserlist.get(l);
							HashMap map=new HashMap();
							map.put("userid", obj[0]+"");
							map.put("username", obj[1]+"");
							map.put("isok", "N");
							userlist.add(map);
						}
						request.setAttribute("msUserList", userlist);
					}
					manage.setR5(r5name);
					
					request.setAttribute("processApproveList", processApproveList);
					request.setAttribute("qualityCheckManagementBean", manage);
					request.setAttribute("elevaorTypeName", bd.getOptionName(manage.getElevatorType(), elevaorTypeList));
					request.setAttribute("markingItemsComplyList", markingItemsComplyList);
					request.setAttribute("deleList", deleList);
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
				request.setAttribute("CSheight",CSheight);
				request.setAttribute("CSwidth", CSwidth);
				request.setAttribute("CIheight", CIheight);
				request.setAttribute("CIwidth", CIwidth);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			
		}		
		
	}
	
	/**
	 * 打印通知单
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */

	/* @SuppressWarnings("unchecked") */
	public ActionForward toPreparePrintRecord(ActionMapping mapping,ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Connection conn = null;
		DynaActionForm dform = (DynaActionForm) form;
		String id = request.getParameter("id");
		List etcpList = new ArrayList();
		List hecirList = new ArrayList();
		if (id != null && !id.equals("")) {
			try {
				hs = HibernateUtil.getSession();
				DataOperation op = new DataOperation();
				conn = (Connection) hs.connection();
				op.setCon(conn);
				Query query = hs
						.createQuery("from QualityCheckManagement where billno = '"
								+ id.trim() + "'");
				List list = query.list();
				if (list != null && list.size() > 0) {

					// 主信息
					QualityCheckManagement QM = (QualityCheckManagement) list
							.get(0);
					String CompanyID = bd.getName("MaintContractMaster","companyId", "maintContractNo", QM.getMaintContractNo());
					QM.setMaintPersonnel(bd.getName(hs, "Loginuser","username", "userid", QM.getMaintPersonnel()));// 报修人
					QM.setR2(bd.getName(hs, "ElevatorCoordinateLocation","rem", "elevatorNo", QM.getElevatorNo()));
					QM.setR3(bd.getName(hs, "Customer", "companyName","companyId", CompanyID));
					QM.setR4(bd.getName(hs, "Loginuser", "username", "userid",QM.getChecksPeople()));// 督查人员

					String hecirListsql = "select * from MarkingScoreRegister where billno = '"
							+ id.trim() + "' and isDelete='N'";
					hecirList = op.queryToList(hecirListsql);

					// 对barCodeList、noticeList操作
					BarCodePrint dy = new BarCodePrint();
					List barCodeList = new ArrayList();
					barCodeList.add(QM);
					barCodeList.add(hecirList);
					dy.toPrintTwoRecord6(request, response, barCodeList, id);
					// register hecirList

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return null;
	}
	
	/**
	 * 下载附件
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDownloadFileRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

//		FileOperatingUtil uf = new FileOperatingUtil();
//		uf.toDownLoadFiles(mapping, form, request, response);
		String fileOldName=request.getParameter("fileOldName");
		fileOldName=URLDecoder.decode(fileOldName,"utf-8");
		String filename=request.getParameter("filesname");
		filename=URLDecoder.decode(filename,"utf-8");
		String folder=request.getParameter("folder");
		if(folder==null || "".equals(folder)){
			folder="MTSComply.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;

		response.setContentType("application/x-msdownload");
		response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileOldName, "utf-8"));
		

		fis = new FileInputStream(folder+"MarkingScoreRegisterFileinfo"+"/"+filename);
		bis = new BufferedInputStream(fis);
		fos = response.getOutputStream();
		bos = new BufferedOutputStream(fos);

		int bytesRead = 0;
		byte[] buffer = new byte[5 * 1024];
		while ((bytesRead = bis.read(buffer)) != -1) {
			bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
			bos.flush();
		}
		if (fos != null) {fos.close();}
		if (bos != null) {bos.close();}
		if (fis != null) {fis.close();}
		if (bis != null) {bis.close();}
		return null;
	}
}
