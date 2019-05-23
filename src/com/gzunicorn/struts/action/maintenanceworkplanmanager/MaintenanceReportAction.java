package com.gzunicorn.struts.action.maintenanceworkplanmanager;


import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;


public class MaintenanceReportAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintenanceReportAction.class);

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
				SysRightsUtil.NODE_ID_FORWARD + "maintenancereport", null);
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

		request.setAttribute("navigator.location", " 维保保养时工报表>> 查询");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
		
		Session hs = null;
		List maintDivisionList =null;
		try {
			hs = HibernateUtil.getSession();
			 
			maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
			//if(maintDivisionList!=null && maintDivisionList.size()>1){
			//	maintDivisionList.remove(0);
			//}
			
			//维保站下拉框
			//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
			String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
			List vmlist=hs.createSQLQuery(sqlss).list();
			if(vmlist!=null && vmlist.size()>0){
				String hql="select a from Storageid a where a.storageid= '"+userInfo.getStorageId()+"' " +
						"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";	
				List mainStationList=hs.createQuery(hql).list();
				request.setAttribute("mainStationList", mainStationList);
			}else if ("A02".equals(userInfo.getRoleID())) {
				String hql="select a from Storageid a where a.comid= '"+userInfo.getComID()+"' " +
						"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";	
				List mainStationList=hs.createQuery(hql).list();
				
				Storageid storid=new Storageid();
				 storid.setStorageid("");
				 storid.setStoragename("全部");
				 mainStationList.add(0,storid);
				
				request.setAttribute("mainStationList", mainStationList);
			}else{
				Storageid storid=new Storageid();
				 storid.setStorageid("");
				 storid.setStoragename("全部");
				 
				 List mainStationList=new ArrayList();
				 mainStationList.add(0,storid);
				 
				 request.setAttribute("mainStationList", mainStationList);
			}
			
			
			
			String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
			String day1=DateUtil.getDate(day, "MM", -1);//当前日期月份加1 。
			dform.set("sdate1", day1);
			dform.set("edate1", day);
			
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				hs.close();
				// HibernateSessionFactory.closeSession();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		
		request.setAttribute("CompanyList", maintDivisionList);	
		forward = mapping.findForward("maintenanceReportSearch");		
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
	 * Method toSearchRecord execute, Search record
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toSearchResults(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("navigator.location", " 维保保养时工报表>> 查询结果");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
		String action = dform.toString();
		if (dform.get("genReport") != null
				&& !dform.get("genReport").equals("")) {
			try {

				response = toExcelRecord(form, request, response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			String maintDivision = request.getParameter("maintDivision");
			String mainStation = request.getParameter("mainStation");
			String maintPersonnel = request.getParameter("maintPersonnel");
			String sdate1=request.getParameter("sdate1");
			String edate1=request.getParameter("edate1");
			HashMap rmap= new HashMap();			
			rmap.put("maintDivision", maintDivision);
			rmap.put("mainStation", mainStation);
			rmap.put("maintPersonnel", maintPersonnel);
			rmap.put("sdate1", sdate1);
			rmap.put("edate1", edate1);
			if(maintDivision.equals("00"))
			{
				maintDivision=null;
			}
			
			Session hs = null;
			Query query = null;
			Connection conn=null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				hs = HibernateUtil.getSession();
				conn=hs.connection();
				
				String[] colNames = {
						"lu.username as username",
						"lu.userid as userid",
						"c.comfullname as comname",
						"si.storagename as storagename",
						"mcd.projectName as projectName",
						"mcm.maintContractNo as maintContractNo",
						"mcd.salesContractNo as salesContractNo",
						"mcd.elevatorNo as elevatorNo",
						"mcd.elevatorType as elevatorType",
						"mcd.elevatorParam as elevatorParam",
						"mwpd.maintDate as maintDate",
						"mwpd.week as week",
						"mwpd.maintType as maintType",
						"mwpd.maintDateTime as maintDateTime",
						"mwpd.maintStartTime as maintStartTime",
						"mwpd.maintEndTime as maintEndTime"
				};
				String hql="select "+StringUtils.join(colNames, ",")+" from MaintenanceWorkPlanDetail mwpd,MaintenanceWorkPlanMaster mwpm,MaintContractDetail mcd,MaintContractMaster mcm,LoginUser lu,Company c,Storageid si "
						+ " where si.storageid=mcd.assignedMainStation "
						+ " and c.comid=mcm.maintDivision "
						+ " and lu.userid=mwpd.maintPersonnel "
						+ " and mwpd.billno=mwpm.billno "
						+ " and mwpm.rowid=mcd.rowid "
						+ " and mcd.billno=mcm.billno";
				if(maintDivision!=null&&!maintDivision.equals(""))
                {
                	hql+=" and mcm.maintDivision like '"+maintDivision.trim()+"'";
                }
               if(mainStation!=null&&!mainStation.equals(""))
                {
                	hql+=" and mcd.assignedMainStation like '"+mainStation.trim()+"'";
                }
                if(maintPersonnel!=null&&!maintPersonnel.equals(""))
                {
                	hql+=" and mwpd.maintPersonnel like '%"+maintPersonnel.trim()+"%'";
                }

                if (sdate1 != null && !sdate1.equals("")) {
					hql+=" and mwpd.maintDate >= '"+sdate1.trim()+"'";
				}
				if (edate1 != null && !edate1.equals("")) {
					hql+=" and mwpd.maintDate <= '"+edate1.trim()+"'";
				}
                hql+=" order by mwpd.maintDate ";

                ps=conn.prepareStatement(hql);
                rs=ps.executeQuery();
                int a=0;
                for(int j=0;j<colNames.length;j++)
                {
                	colNames[j]=colNames[j].split(" as ")[1];
                }
               
                List maintenanceReportList=new ArrayList();

                while(rs.next())
                {
                	HashMap map= new HashMap();
                	for(int j=0;j<colNames.length;j++)
					{
                		map.put(colNames[j],rs.getString(colNames[j]));
                		String maintEndTime=(String) map.get("maintEndTime");
                		if(maintEndTime!=null&&!"".equals(maintEndTime))
                		{
                			map.put("maintEndTime","~"+maintEndTime);
                		}
                		String maintType=(String) map.get("maintType");
                		if(maintType!=null){
                		if(maintType.equals("yearDegree"))
                		{
                			map.put("maintType","年度保养");
                		}  
                		if(maintType.equals("halfMonth"))
                		{
                			map.put("maintType","半月保养");
                		}  
                		if(maintType.equals("quarter"))
                		{
                			map.put("maintType","季度保养");
                		}  
                		if(maintType.equals("halfYear"))
                		{
                			map.put("maintType","半年保养");
                		}
                		}
					}
                	maintenanceReportList.add(map);
                }
                if(maintenanceReportList.size()>0){
                request.setAttribute("maintenanceReportList", maintenanceReportList);
                }
			    request.setAttribute("rmap", rmap);
			}catch(Exception e) {
				e.printStackTrace();
			} finally {
				try {
					hs.close();
					// HibernateSessionFactory.closeSession();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			
		 forward = mapping.findForward("maintenanceReportList");		
		}
		return forward;
	}
	
	public HttpServletResponse toExcelRecord(ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		String naigation = new String();
		ActionForward forward = null;
		HttpSession session = request.getSession();
		String maintDivision = request.getParameter("maintDivision");
		String mainStation = request.getParameter("mainStation");
		String maintPersonnel = request.getParameter("maintPersonnel");
		String sdate1=request.getParameter("sdate1");
		String edate1=request.getParameter("edate1");
		if(maintDivision.equals("00"))
		{
			maintDivision=null;
		}
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			String[] colNames = {
					"lu.username as username",
					"lu.userid as userid",
					"c.comfullname as comname",
					"si.storagename as storagename",
					"mcd.projectName as projectName",
					"mcm.maintContractNo as maintContractNo",
					"mcd.salesContractNo as salesContractNo",
					"mcd.elevatorNo as elevatorNo",
					"mcd.elevatorType as elevatorType",
					"mcd.elevatorParam as elevatorParam",
					"mwpd.maintDate as maintDate",
					"mwpd.week as week",
					"mwpd.maintType as maintType",
					"mwpd.maintDateTime as maintDateTime",
					"mwpd.maintStartTime as maintStartTime",
					"mwpd.maintEndTime as maintEndTime"
			};
			String hql="select "+StringUtils.join(colNames, ",")+" from MaintenanceWorkPlanDetail mwpd,MaintenanceWorkPlanMaster mwpm,MaintContractDetail mcd,MaintContractMaster mcm,LoginUser lu,Company c,Storageid si "
					+ " where si.storageid=mcd.assignedMainStation "
					+ " and c.comid=mcm.maintDivision "
					+ " and lu.userid=mwpd.maintPersonnel "
					+ " and mwpd.billno=mwpm.billno "
					+ " and mwpm.rowid=mcd.rowid "
					+ " and mcd.billno=mcm.billno";
			if(maintDivision!=null&&!maintDivision.equals(""))
            {
            	hql+=" and mcm.maintDivision like '"+maintDivision.trim()+"'";
            }
           if(mainStation!=null&&!mainStation.equals(""))
            {
            	hql+=" and mcd.assignedMainStation like '"+mainStation.trim()+"'";
            }
            if(maintPersonnel!=null&&!maintPersonnel.equals(""))
            {
            	hql+=" and mwpd.maintPersonnel like '%"+maintPersonnel.trim()+"%'";
            }

            if (sdate1 != null && !sdate1.equals("")) {
				hql+=" and mwpd.maintDate >= '"+sdate1.trim()+"'";
			}
			if (edate1 != null && !edate1.equals("")) {
				hql+=" and mwpd.maintDate <= '"+edate1.trim()+"'";
			}
            hql+=" order by mwpd.maintDate ";
            List list= hs.createSQLQuery(hql).list();
            List roleList=new ArrayList();
            for(int j=0;j<colNames.length;j++)
            {
            	colNames[j]=colNames[j].split(" as ")[1];
            }
            
            if(list!=null&&list.size()>0)
            {
            	for(int i=0 ;i<list.size();i++)
            	{
            		Object[] objects=(Object[])list.get(i);
            		HashMap map= new HashMap();
                	for(int j=0;j<colNames.length;j++)
					{
                		map.put(colNames[j],objects[j]);
                		String maintEndTime=(String) map.get("maintEndTime");
                		String maintStartTime=(String) map.get("maintStartTime");
                		if(maintEndTime!=null&&!"".equals(maintEndTime))
                		{
                			map.put("maintEndTime","~"+maintEndTime);
                		}
                		if(maintEndTime!=null&&!"".equals(maintEndTime)&&maintStartTime!=null&&!"".equals(maintStartTime))
                		{
                			map.put("maintStartEndTime", (String)map.get("maintStartTime")+(String)map.get("maintEndTime"));
                		}else
                		{
                			map.put("maintStartEndTime","");
                		}
                		
                		String maintType=(String) map.get("maintType");
                		if(maintType!=null&&!maintType.equals("")){
                		if(maintType.equals("yearDegree"))
                		{
                			map.put("maintType","年度保养");
                		}  
                		if(maintType.equals("halfMonth"))
                		{
                			map.put("maintType","半月保养");
                		}  
                		if(maintType.equals("quarter"))
                		{
                			map.put("maintType","季度保养");
                		}  
                		if(maintType.equals("halfYear"))
                		{
                			map.put("maintType","半年保养");
                		}
                		}           		
					}
                	map.remove("userid");
                	map.remove("maintStartTime");
                	map.remove("maintEndTime");
                	roleList.add(map);
            	}
            }
            XSSFSheet sheet = wb.createSheet("维保保养时工报表");
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("维保工");

				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("维保分部");
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("维保站");
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("项目名称");
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("维保合同号");
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("销售合同号");
				
				cell0 = row0.createCell((short)6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("电梯编号");
				
				cell0 = row0.createCell((short)7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("电梯类型");
				
				cell0 = row0.createCell((short)8);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("规格型号");
				
				cell0 = row0.createCell((short)9);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("保养时间");
				
				cell0 = row0.createCell((short)10);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("星期");
				
				cell0 = row0.createCell((short)11);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("保养类型");
				
				cell0 = row0.createCell((short)12);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("准保养时间(分钟)");
				
				cell0 = row0.createCell((short)13);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("作业时间段");
				
				for (int i = 0; i < l; i++) {
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);
					XSSFCell cell =null;
					
					
					HashMap map=(HashMap) roleList.get(i);
				    cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("username"));
				
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("comname"));
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("storagename"));
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("projectName"));
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("maintContractNo"));
					
					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("salesContractNo"));
					
					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("elevatorNo"));

					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					String elevatorType =(String)map.get("elevatorType");
					if(elevatorType.trim().equals("T"))
					{
						cell.setCellValue("直梯");
					}else
					{
						cell.setCellValue("扶梯");
					}
					
					
					cell = row.createCell((short)8);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("elevatorParam"));
					
					cell = row.createCell((short)9);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("maintDate"));
					
					cell = row.createCell((short)10);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("week"));
					
					cell = row.createCell((short)11);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("maintType"));
					
					cell = row.createCell((short)12);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("maintDateTime"));
					
					cell = row.createCell((short)13);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("maintStartEndTime"));
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("维保保养时工报表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}



	
	public void toMainStationList(ActionMapping mapping, ActionForm form,
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
				if(comid.equals("00"))
				{
					comid="%";
				}
			String hql="select a from Storageid a,Company b where a.comid = b.comid and a.comid like '"+comid+"' " +
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
	
	public void toMaintPersonnelList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		Session hs=null;
		String mainStation=request.getParameter("mainStation");
		response.setHeader("Content-Type","text/html; charset=GBK");
		List list2=new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='GBK'?>");
		sb.append("<root>");
		try {
			hs=HibernateUtil.getSession();
			if(mainStation!=null && !"".equals(mainStation)){
				//A03  维保经理 ,A50  维保工  ,A49  维保站长  
				String hql="select lu from Loginuser lu where lu.storageid like '"+mainStation+"%' "
						+ "and lu.roleid in('A50','A49','A03') and lu.enabledflag='Y' order by lu.roleid";
				List list=hs.createQuery(hql).list();
				if(list!=null && list.size()>0){
					sb.append("<rows>");
					for(int i=0;i<list.size();i++){
						Loginuser lu=(Loginuser)list.get(i);
					sb.append("<cols name='"+lu.getUsername()+"' value='"+lu.getUserid()+"'>").append("</cols>");
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
	
}
