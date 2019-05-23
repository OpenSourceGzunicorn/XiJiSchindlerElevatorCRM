package com.gzunicorn.struts.action.installationreport;


import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.gzunicorn.hibernate.sysmanager.Company;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

/**
 * 厂检模块=>项目经理管理报表
 * @author Lijun
 *
 */
public class ManagerReportAction extends DispatchAction {

	Log log = LogFactory.getLog(ManagerReportAction.class);

	BaseDataImpl bd = new BaseDataImpl();
	DecimalFormat df = new DecimalFormat("##.##"); 
	
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
				SysRightsUtil.NODE_ID_FORWARD + "managerreport", null);
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

		request.setAttribute("navigator.location", " 项目经理管理报表>> 查询");
		ActionForward forward = null;
		DynaActionForm dform = (DynaActionForm) form;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		Session hs=null;
		try {
			hs=HibernateUtil.getSession();
			String sql1="from Company where (comtype='1' or comtype='2') and enabledflag='Y' and comid='"+userInfo.getComID()+"' order by comid desc";				
			List list1=hs.createQuery(sql1).list();
			
			if(list1!=null && list1.size()>0){
				request.setAttribute("departmentList", list1);
			}else{
				List departmentList=new ArrayList();
				String sql="from Company where (comtype='1' or comtype='2') and enabledflag='Y' order by comid desc";				
				List list=hs.createQuery(sql).list();
				if(list!=null && list.size()>0){
					Map map1=new HashMap();
					map1.put("comid", "");
					map1.put("comfullname", "全部");
					departmentList.add(map1);
					for(Object object : list){
						Company com=(Company)object;
						Map map=new HashMap();
						map.put("comid", com.getComid());
						map.put("comfullname", com.getComfullname());
						departmentList.add(map);
					}
				}
				request.setAttribute("departmentList", departmentList);
			}
			
			String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
			String day1=DateUtil.getDate(day, "MM", -1);//当前日期月份加1 。
			dform.set("sdate1", day1);
			dform.set("edate1", day);

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
		
		forward = mapping.findForward("managerReportSearch");		
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

		request.setAttribute("navigator.location", " 项目经理管理报表>> 查询结果");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
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
			String projectManager = request.getParameter("projectManager");
			String department = request.getParameter("department");
			String sdate1=request.getParameter("sdate1");
			String edate1=request.getParameter("edate1");
			HashMap rmap= new HashMap();			
			rmap.put("projectManager", projectManager);
			rmap.put("department", department);
			rmap.put("sdate1", sdate1);
			rmap.put("edate1", edate1);
			
			String departname="全部";
			if(department!=null && !department.trim().equals("")){
				departname=bd.getName("Company", "comfullname", "comid", department);
			}

			String showstr="[提交厂检时间:"+sdate1+" 至 "+edate1+", 所属部门:"+departname+", 项目经理姓名:"+projectManager.trim()+"]";
			request.setAttribute("showstr", showstr);
			
			Session hs = null;
			Query query = null;
			Connection conn=null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			int maxCheck=0;
			try {
				hs = HibernateUtil.getSession();
				String sql="select max(checkNum) from ElevatorTransferCaseRegister";
				if(department!=null && !"".equals(department)){
					sql+=" where department = '"+department.trim()+"'";
				}
				List list1=hs.createSQLQuery(sql).list();
				if(list1!=null && list1.size()>0){
					maxCheck=(Integer) list1.get(0);
				}
				List maxList=new ArrayList();
				for(int k=0;k<maxCheck;k++){
					maxList.add(k);
				}
				request.setAttribute("maxList",maxList);
				request.setAttribute("length", maxCheck);
				conn=hs.connection();
				
				//审批通过的 或者是 厂检部长审批通过。
				String hql="select a.projectManager as projectManager,a.department as department";
				for(int i=0;i<maxCheck;i++){
					hql+=",COUNT(case when a.CheckNum="+(i+1)+" then a.elevatorNo end) as factory"+(i+1)
						+",isnull(COUNT(case when a.CheckNum="+(i+1)+" and a.factoryCheckResult='合格' then a.elevatorNo end),0) as nofactory"+(i+1);
				}
				hql+=" from ElevatorTransferCaseRegister a where" +
						" (a.Status=1 or a.billno=(select distinct b.billno from ElevatorTransferCaseProcess b " +
						"where b.billno=a.billno and b.TaskName='厂检部长审核')) ";
				
				if(projectManager!=null&&!projectManager.equals(""))
                {
                	hql+=" and a.projectManager like '%"+projectManager.trim()+"%'";
                }
               if(department!=null&&!department.equals(""))
                {
                	hql+=" and a.department like '%"+department.trim()+"%'";
                }

                if (sdate1 != null && !sdate1.equals("")) {
					hql+=" and a.checkTime >= '"+sdate1.trim()+"'";
				}
				if (edate1 != null && !edate1.equals("")) {
					hql+=" and a.checkTime <= '"+edate1.trim()+"'";
				}
                hql+=" group by a.projectManager,a.department ";
                
                //System.out.println(hql);

                ps=conn.prepareStatement(hql);
                rs=ps.executeQuery();
                int a=0;
                List maintenanceReportList=new ArrayList();

                while(rs.next())
                {
                	HashMap map= new HashMap();
                	map.put("projectManager", rs.getString("projectManager"));
                	map.put("department", bd.getName("Company", "comfullname", "comid", rs.getString("department")));
                	List factoryList=new ArrayList();
                	for(int j=0;j<maxCheck;j++){
                		Map map1=new HashMap();
                		map1.put("factory"+j, rs.getInt("factory"+(j+1)));
                		map1.put("nofactory"+j, rs.getInt("nofactory"+(j+1)));
                		double rate=0;
                		if(rs.getInt("factory"+(j+1))!=0){
                			rate=rs.getInt("nofactory"+(j+1))/Double.valueOf(rs.getInt("factory"+(j+1)))*100;
                		}else{
                			rate=0;
                		}
                		map1.put("rate"+j, rate==0 ? rate+"%" : df.format(rate)+"%");

                		factoryList.add(map1);
                	}
                	map.put("factoryList", factoryList);

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
			
		 forward = mapping.findForward("managerReportList");		
		}
		return forward;
	}
	
	public HttpServletResponse toExcelRecord(ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		String naigation = new String();
		ActionForward forward = null;
		HttpSession session = request.getSession();
		String projectManager = request.getParameter("projectManager");
		String department = request.getParameter("department");
		//String maintPersonnel = request.getParameter("maintPersonnel");
		String sdate1=request.getParameter("sdate1");
		String edate1=request.getParameter("edate1");
		/*if(maintDivision.equals("00"))
		{
			maintDivision=null;
		}*/
		Session hs = null;
		Query query = null;
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		XSSFWorkbook wb = new XSSFWorkbook();
		int maxCheck=0;

		try {
			hs = HibernateUtil.getSession();
			
			String departname="全部";
			if(department!=null && !department.trim().equals("")){
				departname=bd.getName("Company", "comfullname", "comid", department);
			}

			String showstr="[提交厂检时间:"+sdate1+" 至 "+edate1+", 所属部门:"+departname+", 项目经理姓名:"+projectManager.trim()+"]";
			
			String sql="select max(checkNum) from ElevatorTransferCaseRegister";
			if(department!=null && !"".equals(department)){
				sql+=" where department = '"+department.trim()+"'";
			}
			List list1=hs.createSQLQuery(sql).list();
			if(list1!=null && list1.size()>0){
				maxCheck=(Integer) list1.get(0);
			}
			
			conn=hs.connection();
			
			String hql="select a.projectManager as projectManager,a.department as department";
			for(int i=0;i<maxCheck;i++){
				hql+=",COUNT(case a.CheckNum when "+(i+1)+" then a.elevatorNo end) as factory"+(i+1)
					+",isnull(COUNT(case when a.CheckNum="+(i+1)+" and a.factoryCheckResult='合格' then a.elevatorNo end),0) as nofactory"+(i+1);
			}
			hql+=" from ElevatorTransferCaseRegister a where " +
					"(a.Status=1 or a.billno=(select distinct b.billno from ElevatorTransferCaseProcess b " +
					"where b.billno=a.billno and b.TaskName='厂检部长审核')) ";

			if(projectManager!=null&&!projectManager.equals(""))
            {
            	hql+=" and a.projectManager like '%"+projectManager.trim()+"%'";
            }
            if(department!=null&&!department.equals(""))
            {
            	hql+=" and a.department like '%"+department.trim()+"%'";
            }
            /*if(maintPersonnel!=null&&!maintPersonnel.equals(""))
            {
            	hql+=" and mwpd.maintPersonnel like '%"+maintPersonnel.trim()+"%'";
            }*/

            if (sdate1 != null && !sdate1.equals("")) {
				hql+=" and a.checkTime >= '"+sdate1.trim()+"'";
			}
			if (edate1 != null && !edate1.equals("")) {
				hql+=" and a.checkTime <= '"+edate1.trim()+"'";
			}
            hql+=" group by a.projectManager,a.department ";
            ps=conn.prepareStatement(hql);
            rs=ps.executeQuery();
           
            List roleList=new ArrayList();
           
            while(rs.next()){
            	HashMap map= new HashMap();
            	map.put("projectManager", rs.getString("projectManager"));
            	map.put("department", bd.getName("Company", "comfullname", "comid", rs.getString("department")));
            	
            	for(int j=0;j<maxCheck;j++){
            		
            		map.put("factory"+j, rs.getInt("factory"+(j+1)));
            		map.put("nofactory"+j, rs.getInt("nofactory"+(j+1)));
            		double rate=0;
            		if(rs.getInt("factory"+(j+1))!=0){
            			rate=rs.getInt("nofactory"+(j+1))/Double.valueOf(rs.getInt("factory"+(j+1)))*100;
            		}else{
            			rate=0;
            		}
            		map.put("rate"+j, rate==0 ? rate+"%" : df.format(rate)+"%");
            		
            	}
            	
            	roleList.add(map);
            }
            
            XSSFSheet sheet = wb.createSheet("项目经理管理报表");
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);
			
			XSSFRow row0 = sheet.createRow( 0);
			XSSFCell cell0 = row0.createCell((short)0);
			cell0.setCellValue(showstr);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				row0 = sheet.createRow(2);
				cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("项目经理姓名");
				
				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("所属部门");
				
				int index=1;
				for(int k=0;k<maxCheck;k++){
					if(k==0){
						cell0 = row0.createCell((short)2);
						//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell0.setCellValue("初检台数");
						
						cell0 = row0.createCell((short)3);
						//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell0.setCellValue("初检合格台数");
						
						cell0 = row0.createCell((short)4);
						//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell0.setCellValue("初检合格率");
						
					}else{
//						//System.out.println(bd.numToChinese(k+1));
						String num=bd.numToChinese(k+1);
						num=num.replaceAll("个", "");
						cell0 = row0.createCell((short)index+1);
						//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell0.setCellValue(num+"检合格率");
						
						cell0 = row0.createCell((short)index+2);
						//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell0.setCellValue(num+"检合格率");
						
						cell0 = row0.createCell((short)index+3);
						//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell0.setCellValue(num+"检合格率");
					}
					index+=3;
				}
				
				for (int i = 0; i < l; i++) {
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+3);
					XSSFCell cell =null;
					
					
					HashMap map=(HashMap) roleList.get(i);
				    cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("projectManager"));
					
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(map.get("department").toString());
					
					int ind=1;
					for(int j=0;j<maxCheck;j++){
						if(j==0){
							cell = row.createCell((short)2);
							//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellValue(Integer.valueOf(map.get("factory0").toString()));
							
							cell = row.createCell((short)3);
							//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellValue(Integer.valueOf(map.get("nofactory0").toString()));
							
							cell = row.createCell((short)4);
							//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellValue(map.get("rate0").toString());
							
						}else{
							cell = row.createCell((short)ind+1);
							//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellValue(Integer.valueOf(map.get("factory"+j).toString()));
							
							cell = row.createCell((short)ind+2);
							//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellValue(Integer.valueOf(map.get("nofactory"+j).toString()));
							
							cell = row.createCell((short)ind+3);
							//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
							cell.setCellValue(map.get("rate"+j).toString());
						}
						ind+=3;
					}
					
					
					
					
					
					
				}
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
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("项目经理管理报表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	
}
