package com.gzunicorn.struts.action.contracttransfer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
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
import org.apache.struts.util.MessageResources;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferFileTypes;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferMaster;
import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;



public class ContractTransferFeedBackAction extends DispatchAction {

	Log log = LogFactory.getLog(ContractTransferFeedBackAction.class);
	BaseDataImpl bd=new BaseDataImpl();

	// --------------------------------------------------------- Instance
	// Variables

	// --------------------------------------------------------- Methods

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
		//String flag = request.getParameter("openflag")==null?"":request.getParameter("openflag");
		//if(!"Sale".equals(flag)){
		/************开始用户权限过滤************/
		SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "ContractTransferFeedBack",null);
		/************结束用户权限过滤************/
		//}
		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		//System.out.println(name);
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			ActionForward forward = super.execute(mapping, form, request,
					response);
			//System.out.println(forward.getName());
			return forward;
		}

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
	 * Method toSearchRecord execute, Search record
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("navigator.location", "合同交接资料反馈查询>> 查询列表");
		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {

				response = toExcelRecord(form, request, response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "contractTransferFeedBackList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fContractTransferFeedBack");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("BillNo");
			table.setIsAscending(true);
			cache.updateTable(table);
			
			int location = 0;//当前页码
			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			}else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			
			cache.saveForm(tableForm);
		

			
			String billno = tableForm.getProperty("billno");
			if(billno==null||"null".equals(billno)){
				billno="";
			}
			String maintcontractno = tableForm.getProperty("maintcontractno");
			if(maintcontractno==null||"null".equals(maintcontractno)){
				maintcontractno="";
			}
			String salescontractno = tableForm.getProperty("salescontractno");
			if(salescontractno==null||"null".equals(salescontractno)){
				salescontractno="";
			}
			String companyname = tableForm.getProperty("companyid");
			if(companyname==null||"null".equals(companyname)){
				companyname="";
			}
			String maintdivision = tableForm.getProperty("maintdivision");
			if(maintdivision==null||"null".equals(maintdivision)){
				maintdivision="";
			}
			String maintstation = tableForm.getProperty("maintstation");
			if(maintstation==null||"null".equals(maintstation)){
				maintstation="";
			}
			String elevatorno = tableForm.getProperty("elevatorno");
			if(elevatorno==null||"null".equals(elevatorno)){
				elevatorno="";
			}
			String auditstatus = tableForm.getProperty("auditstatus");
			if(auditstatus==null||"null".equals(auditstatus)){
				auditstatus="";
			}
			
			//第一次进入页面时根据登陆人初始化所属维保分部
			List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
			if(maintdivision == null || maintdivision.equals("")){
				Map map = (Map)maintDivisionList.get(0);
				maintdivision = (String)map.get("grcid");
			}
			
			
			Session hs = null;
			Connection conn = null;
			List li = new ArrayList();

			try {

				hs = HibernateUtil.getSession();
				Query query = null;
				
				String sql = "select cm.billNo,cm.maintContractNo,c.comfullname,s.storagename,cu.companyName,cm.contractSdate,"
				+"cm.contractEdate,cm.salesContractNo,cm.elevatorNo,cm.AuditStatus,cb.OperDate,cb.TransferRem,ct.FeedbackTypeName "
				+"from ContractTransferMaster cm "
				+"join ContractTransferFeedback cb "
				+"on cm.BillNo=cb.BillNo "
				+"and cb.OperDate=(select max(cfb.OperDate) from ContractTransferFeedback cfb where cm.BillNo=cfb.BillNo) "
				+"left join ContractTransferFeedbackType ct "
				+"on cb.FeedbackTypeId=ct.FeedbackTypeId and ct.EnabledFlag='Y' "
				+"join Customer cu "
				+"on cm.companyId=cu.companyId "
				+"join Company c "
				+"on cm.maintDivision=c.comid "
				+"join Storageid s "
				+"on cm.maintStation=s.storageid ";
				
				if (billno != null && !billno.equals("")) {
					sql += " and cm.billNo like '%"+billno.trim()+"%'";
				}
				if (maintcontractno != null && !maintcontractno.equals("")) {
					sql += " and cm.maintcontractno like '%"+maintcontractno.trim()+"%'";
				}				
				if (salescontractno != null && !salescontractno.equals("")) {
					sql += " and cm.salescontractno like '%"+salescontractno.trim()+"%'";
				}				
				if (elevatorno != null && !elevatorno.equals("")) {
					sql += " and cm.elevatorno like '%"+elevatorno.trim()+"%'";
				}				
				if (companyname != null && !companyname.equals("")) {
					sql += " and (cu.companyId like '%"+companyname.trim()+"%' or cu.companyName like '%"+companyname.trim()+"%' )";
				}				
				if (maintdivision != null && !maintdivision.equals("") && !maintdivision.equals("%")) {
					sql += " and cm.maintdivision = '"+maintdivision.trim()+"'";
				}				
				if (maintstation != null && !maintstation.equals("") && !maintstation.equals("%")) {
					sql += " and cm.maintstation = '"+maintstation.trim()+"'";
				}				
				if (auditstatus != null && !auditstatus.equals("")) {
					sql += " and cm.auditstatus = '"+auditstatus.trim()+"'";
				}				
				if (table.getIsAscending()) {
					if("CompanyName".equals(table.getSortColumn())){
						
						sql += " order by cu."+ table.getSortColumn() +" desc";
					}else if("OperDate".equals(table.getSortColumn())){
						
						sql += " order by cb."+ table.getSortColumn() +" desc";
					}else if("FeedbackTypeName".equals(table.getSortColumn())){
						
						sql += " order by ct."+ table.getSortColumn() +" desc";
					}else{
						
						sql += " order by cm."+ table.getSortColumn() +" desc";
					}
				} else {
					if("CompanyName".equals(table.getSortColumn())){
						
						sql += " order by cu."+ table.getSortColumn() +" asc";
					}else if("OperDate".equals(table.getSortColumn())){
						
						sql += " order by cb."+ table.getSortColumn() +" asc";
					}else if("FeedbackTypeName".equals(table.getSortColumn())){
						
						sql += " order by ct."+ table.getSortColumn() +" asc";
					}else{
						
						sql += " order by cm."+ table.getSortColumn() +" asc";
					}
				}
				System.out.println(sql);
				query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list = query.list();
				List contractTransferFeedBackList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map map=new HashMap();
					map.put("billno", objs[0]);
					map.put("maintcontractno", objs[1]);
					map.put("maintdivision", objs[2]);
					map.put("maintstation", objs[3]);
					map.put("companyname", objs[4]);
					map.put("contractsdate", objs[5]);
					map.put("contractedate", objs[6]);
					map.put("salescontractno", objs[7]);
					map.put("elevatorno", objs[8]);
					if("Y".equals(objs[9])){
						
						map.put("auditstatus", "关闭");
					}else if("N".equals(objs[9])){
						
						map.put("auditstatus", "未关闭");
					}
					map.put("operdate", objs[10]);
					map.put("transferrem", objs[11]);
					map.put("feedbacktypename", objs[12]);
					contractTransferFeedBackList.add(map);
				}

				table.addAll(contractTransferFeedBackList);
				session.setAttribute("contractTransferFeedBackList", table);
				
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);
				

				List mainStationList=new ArrayList();
					String hql="select a from Storageid a where a.comid like '"+maintdivision+"' " +
							"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
					mainStationList=hs.createQuery(hql).list();
				  
					 Storageid storid=new Storageid();
					 storid.setStorageid("%");
					 storid.setStoragename("全部");
					 mainStationList.add(0,storid);
				request.setAttribute("mainStationList", mainStationList);

				
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					hs.close();
				} catch (Exception hex) {
					log.error(hex.getMessage());
					hex.printStackTrace();
					//DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
          
			forward = mapping.findForward("contracttransferfeedbackList");
			
		}
		return forward;
	}

	/**
	 * Method toDisplayRecord execute,prepare data for update page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","合同交接资料反馈查看 >> 查看");
		ActionForward forward = null;
		Session hs = null;
		Transaction tx = null;
		ActionErrors errors = new ActionErrors();
		String workisdisplay = request.getParameter("workisdisplay");
		String flag="";
		if(workisdisplay!=null&&workisdisplay.equals("Y")){
			String billno = request.getParameter("id");
			if(billno!=null&&!"".equals(billno)){
				
				try{
					hs = HibernateUtil.getSession();
					tx = hs.beginTransaction();
					
					ContractTransferMaster cm = (ContractTransferMaster) hs.get(ContractTransferMaster.class, billno);
					cm.setWorkisdisplay("Y");
					hs.save(cm);
					
					flag="Y";
					
					tx.commit();
				}catch (Exception e){
					try {
						tx.rollback();
					} catch (HibernateException e3) {
						log.error(e3.getMessage());
						DebugUtil.print(e3, "Hibernate Transaction rollback error!");
					}
					e.printStackTrace();
					log.error(e.getMessage());
					DebugUtil.print(e, "Hibernate ContractTransferMaster Update error!");
				}finally{
					try {
						hs.close();
					} catch (HibernateException hex) {
						log.error(hex.getMessage());
						DebugUtil.print(hex, "Hibernate close error!");
					}
				}
			}
		}
		
		
		Boolean display = bd.contractTransferDisplay(form, request, errors, "display");
		
		request.setAttribute("display", "yes");
		request.setAttribute("workisdisplay", flag);
		
		forward = mapping.findForward("contracttransferfeedbackView");
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
	 * @throws IOException
	 */

	public HttpServletResponse toExcelRecord(ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{

		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		Session hs = null;
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		XSSFWorkbook wb = new XSSFWorkbook();
		
		String billno = tableForm.getProperty("billno");
		if(billno==null||"null".equals(billno)){
			billno="";
		}
		String maintcontractno = tableForm.getProperty("maintcontractno");
		if(maintcontractno==null||"null".equals(maintcontractno)){
			maintcontractno="";
		}
		String salescontractno = tableForm.getProperty("salescontractno");
		if(salescontractno==null||"null".equals(salescontractno)){
			salescontractno="";
		}
		String companyname = tableForm.getProperty("companyid");
		if(companyname==null||"null".equals(companyname)){
			companyname="";
		}
		String maintdivision = tableForm.getProperty("maintdivision");
		if(maintdivision==null||"null".equals(maintdivision)){
			maintdivision="";
		}
		String maintstation = tableForm.getProperty("maintstation");
		if(maintstation==null||"null".equals(maintstation)){
			maintstation="";
		}
		String elevatorno = tableForm.getProperty("elevatorno");
		if(elevatorno==null||"null".equals(elevatorno)){
			elevatorno="";
		}
		String auditstatus = tableForm.getProperty("auditstatus");
		if(auditstatus==null||"null".equals(auditstatus)){
			auditstatus="";
		}
		

		try {
			hs = HibernateUtil.getSession();
			conn=hs.connection();
			
			String sql = "select cm.billNo,cm.maintContractNo,c.comfullname,s.storagename,cu.companyName,cm.contractSdate,"
					+"cm.contractEdate,cm.salesContractNo,cm.elevatorNo,cm.OperDate as OperDate2," 
					
					+"filetype = (stuff((select '，'+b.pullname from ContractTransferFileTypes a,pulldown b " 
					+"where a.FileType = b.pullid and b.typeflag = 'ContractFileTypes_FileType' " 
					+"and billNo = cm.BillNo FOR XML PATH('')),1,1,'')),"
					
					+"cb.OperDate,cb.TransferRem,cm.AuditStatus,ct.FeedbackTypeName "
					+"from ContractTransferMaster cm "
					+"left join ContractTransferFeedback cb "
					+"on cm.BillNo=cb.BillNo "
					+"and cb.OperDate=(select max(cfb.OperDate) from ContractTransferFeedback cfb where cm.BillNo=cfb.BillNo) "
					+"left join ContractTransferFeedbackType ct "
					+"on cb.FeedbackTypeId=ct.FeedbackTypeId and ct.EnabledFlag='Y' "
					+"join Customer cu "
					+"on cm.companyId=cu.companyId "
					+"join Company c "
					+"on cm.maintDivision=c.comid "
					+"join Storageid s "
					+"on cm.maintStation=s.storageid "
					+"where cm.SubmitType = 'Y' ";
			
			if (billno != null && !billno.equals("")) {
				sql += " and cm.billNo like '%"+billno.trim()+"%'";
			}
			if (maintcontractno != null && !maintcontractno.equals("")) {
				sql += " and cm.maintcontractno like '%"+maintcontractno.trim()+"%'";
			}				
			if (salescontractno != null && !salescontractno.equals("")) {
				sql += " and cm.salescontractno like '%"+salescontractno.trim()+"%'";
			}				
			if (elevatorno != null && !elevatorno.equals("")) {
				sql += " and cm.elevatorno like '%"+elevatorno.trim()+"%'";
			}				
			if (companyname != null && !companyname.equals("")) {
				sql += " and (cu.companyId like '%"+companyname.trim()+"%' or cu.companyName like '%"+companyname.trim()+"%' )";
			}				
			if (maintdivision != null && !maintdivision.equals("") && !maintdivision.equals("%")) {
				sql += " and cm.maintdivision = '"+maintdivision.trim()+"'";
			}				
			if (maintstation != null && !maintstation.equals("") && !maintstation.equals("%")) {
				sql += " and cm.maintstation = '"+maintstation.trim()+"'";
			}				
			if (auditstatus != null && !auditstatus.equals("")) {
				sql += " and cm.auditstatus = '"+auditstatus.trim()+"'";
			}				
			sql += " order by cm.billno desc";
			System.out.println(sql);
			ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            
            String[] titlename={"流水号","维保合同号","所属维保分部","所属维保站","甲方单位名称","合同开始日期","合同结束日期",
					"销售合同号","电梯编号","派工日期","交接资料名称","最新反馈日期","最新反馈内容","反馈类型","状态"};
            String[] titleid={"billNo","maintContractNo","comfullname","storagename","companyName","contractSdate","contractEdate",
					"salesContractNo","elevatorNo","OperDate2","filetype","OperDate","TransferRem","FeedbackTypeName","AuditStatus"};

			XSSFSheet sheet = wb.createSheet("合同交接资料反馈查询情况");
			
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
			int j=0;
			while(rs.next()) {
				// 创建Excel行，从0行开始
				row = sheet.createRow(j+1);
				for(int c=0;c<titleid.length;c++){
				    cell = row.createCell((short)c);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);

			    	String valstr=rs.getString(titleid[c]);
				    if(titleid[c].equals("AuditStatus")){
				    	if(valstr!=null && valstr.trim().equals("Y")){
					    	cell.setCellValue("关闭 ");
				    	}else{
				    		cell.setCellValue("未关闭 ");
				    	}
				    }else{
				    	cell.setCellValue(valstr);
				    }
				    
				}
				j++;
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("合同交接资料反馈查询情况", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		return response;
	}
//	
//	public void ajaxShowMass(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws IOException {
//		response.setContentType("text/html;charset=utf-8");
//		String name = request.getParameter("name");
//		String oldid = request.getParameter("id")==null?"":request.getParameter("id");
//		Session hs = null;
//		Connection conn = null;
//		StringBuffer sb = new StringBuffer("{");
//		
//		try {
//			hs = HibernateUtil.getSession();
//			DataOperation op = new DataOperation();
//			conn = (Connection) hs.connection();
//			op.setCon(conn);
//			
//			String sql = "select u.ShopID,t.typename from unitinfomaster u,Unittype t where u.typeid=t.typeid and u.ShopFullName='"+name+"'";
//			List li = op.queryToList(sql);
//			if(li!=null&&li.size()>0){
//				HashMap hm= (HashMap) li.get(0);
//				sb.append("\"shopid\":").append("\"").append(hm.get("shopid")).append("\"").append(",");
//				sb.append("\"typename\":").append("\"").append(hm.get("typename")).append("\"").append(",");
//					
//			}
//			
//			if(oldid!=null&&!"".equals(oldid)){
//				String sql3 = "exec sp_EngUnitInfoAjax '"+oldid+"'";
//				List li3 = op.queryToList(sql3);
//				System.out.println(sql3);
//				System.out.println(li3);
//				if(li3!=null&&li3.size()>0){
//					HashMap hm3= (HashMap) li3.get(0);
//					sb.append("\"firstcooperatetime\":").append("\"").append(hm3.get("firstcooperatetime")).append("\"").append(",");
//					sb.append("\"lastcooperatetime\":").append("\"").append(hm3.get("lastcooperatetime")).append("\"").append(",");
//					sb.append("\"engunityear\":").append("\"").append(hm3.get("engunityear")).append("\"").append(",");
//				}
//			}
//			
//			String sql2 = "select * from EngUnitInfo  where EngUnitName='"+name+"'";
//			List li2 = op.queryToList(sql2);
//			if(li2!=null&&li2.size()>0){
//				String id= (String)((HashMap)li2.get(0)).get("engunitid");
//				if(!oldid.equals(id)){
//					
//					sb.append("\"issame\":").append("\"").append("Y").append("\"").append("}");
//					
//				}else{
//					
//					sb.append("\"issame\":").append("\"").append("N").append("\"").append("}");
//				}
//				
//			}else{
//				
//				sb.append("\"issame\":").append("\"").append("N").append("\"").append("}");
//			}
//		
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		response.getWriter().print(sb+"");
//	}
//
}