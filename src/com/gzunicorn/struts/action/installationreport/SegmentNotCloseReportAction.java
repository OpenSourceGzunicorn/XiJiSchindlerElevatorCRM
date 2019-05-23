package com.gzunicorn.struts.action.installationreport;


import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
 * 厂检模块=>各分部未关闭记录报表
 * @author Lijun
 *
 */
public class SegmentNotCloseReportAction extends DispatchAction {

	Log log = LogFactory.getLog(SegmentNotCloseReportAction.class);

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
				SysRightsUtil.NODE_ID_FORWARD + "segmentnotclosereport", null);
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

		request.setAttribute("navigator.location", " 各分部未关闭记录报表>> 查询");
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
		/*List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
		if(maintDivisionList!=null&&maintDivisionList.size()>1){
			maintDivisionList.remove(0);
		}
		request.setAttribute("CompanyList", maintDivisionList);	*/
		forward = mapping.findForward("segmentNotCloseReportSearch");		
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

		request.setAttribute("navigator.location", " 各分部未关闭记录报表>> 查询结果");
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
				e.printStackTrace();
			}
		} else {
			String projectName = request.getParameter("projectName");
			String insCompanyName = request.getParameter("insCompanyName");
			String salesContractNo = request.getParameter("salesContractNo");
			String projectManager=request.getParameter("projectManager");
			String elevatorNo=request.getParameter("elevatorNo");
			String department=request.getParameter("department");
			String sdate1=request.getParameter("sdate1");
			String edate1=request.getParameter("edate1");
			HashMap rmap= new HashMap();			
			rmap.put("projectName", projectName);
			rmap.put("insCompanyName", insCompanyName);
			rmap.put("salesContractNo", salesContractNo);
			rmap.put("projectManager", projectManager);
			rmap.put("elevatorNo", elevatorNo);
			rmap.put("department", department);
			rmap.put("sdate1", sdate1);
			rmap.put("edate1", edate1);

			String departname="全部";
			if(department!=null && !department.trim().equals("")){
				departname=bd.getName("Company", "comfullname", "comid", department);
			}

			String showstr="[提交厂检时间:"+sdate1+" 至 "+edate1+", 项目名称:"+projectName.trim()
					+", 安装单位:"+insCompanyName.trim()+", 合同号:"+salesContractNo.trim()
					+", 项目经理姓名:"+projectManager.trim()+", 电梯编号:"+elevatorNo.trim()+", 所属部门:"+departname+"]";
			request.setAttribute("showstr", showstr);
			
			Session hs = null;
			Query query = null;
			Connection conn=null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				hs = HibernateUtil.getSession();
				conn=hs.connection();
				
				String[] colNames = {
						"a.CheckTime as checkTime",
						"c.ComFullName as department",
						"a.projectName as projectName",
						"a.SalesContractNo as salesContractNo",
						"a.InsCompanyName as insCompanyName",
						"b.checknum as checkNum",
						"a.projectManager as projectManager",
						"a.ElevatorNo as elevatorNo"
						
				};
				//审批通过的 或者是 厂检部长审批通,未关闭厂检的。
				String hql="select distinct "+StringUtils.join(colNames, ",")+"  from ElevatorTransferCaseRegister a"
						+" left outer join (select MAX(checknum) checknum,ElevatorNo from ElevatorTransferCaseRegister group by ElevatorNo) b on a.ElevatorNo=b.ElevatorNo"
						+" left outer join Company c on a.Department=c.ComID "
						+"where a.CheckNum=1 and a.CheckTime !='' "
						+"and a.ElevatorNo not in(select distinct elevatorno from ElevatorTransferCaseRegister where (FactoryCheckResult='合格' or isclose='Y')) "
						+"and (a.Status=1 or a.billno=(select distinct b.billno from ElevatorTransferCaseProcess b where b.billno=a.billno and b.TaskName='厂检部长审核' )) ";
				
				if(projectName!=null&&!projectName.equals(""))
                {
                	hql+=" and a.projectName like '%"+projectName.trim()+"%'";
                }
               if(insCompanyName!=null&&!insCompanyName.equals(""))
                {
                	hql+=" and a.insCompanyName like '%"+insCompanyName.trim()+"%'";
                }
                if(salesContractNo!=null&&!salesContractNo.equals(""))
                {
                	hql+=" and a.salesContractNo like '%"+salesContractNo.trim()+"%'";
                }
                if(department!=null&&!department.equals(""))
                {
                	hql+=" and a.department like '%"+department.trim()+"%'";
                }
                if(projectManager!=null&&!projectManager.equals(""))
                {
                	hql+=" and a.projectManager like '%"+projectManager.trim()+"%'";
                }
                if(elevatorNo!=null&&!elevatorNo.equals(""))
                {
                	hql+=" and a.elevatorNo like '%"+elevatorNo.trim()+"%'";
                }

                if (sdate1 != null && !sdate1.equals("")) {
					hql+=" and a.checkTime >= '"+sdate1.trim()+"'";
				}
				if (edate1 != null && !edate1.equals("")) {
					hql+=" and a.checkTime <= '"+edate1.trim()+"'";
				}
                hql+=" order by a.checkTime ";

                //System.out.println(hql);
                
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
			
		 forward = mapping.findForward("segmentNotCloseReportList");		
		}
		return forward;
	}
	
	public HttpServletResponse toExcelRecord(ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		String naigation = new String();
		ActionForward forward = null;
		HttpSession session = request.getSession();
		
		String projectName = request.getParameter("projectName");
		String insCompanyName = request.getParameter("insCompanyName");
		String salesContractNo = request.getParameter("salesContractNo");
		String projectManager = request.getParameter("projectManager");
		String elevatorNo = request.getParameter("elevatorNo");
		String sdate1=request.getParameter("sdate1");
		String edate1=request.getParameter("edate1");
		String department=request.getParameter("department");

		String departname="全部";
		if(department!=null && !department.trim().equals("")){
			departname=bd.getName("Company", "comfullname", "comid", department);
		}

		String showstr="[提交厂检时间:"+sdate1+" 至 "+edate1+", 项目名称:"+projectName.trim()
				+", 安装单位:"+insCompanyName.trim()+", 合同号:"+salesContractNo.trim()
				+", 项目经理姓名:"+projectManager.trim()+", 电梯编号:"+elevatorNo.trim()+", 所属部门:"+departname+"]";
		
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			String[] colNames = {
					"a.CheckTime as checkTime",
					"c.ComFullName as department",
					"a.projectName as projectName",
					"a.SalesContractNo as salesContractNo",
					"a.InsCompanyName as insCompanyName",
					"b.checknum as checkNum",
					"a.projectManager as projectManager",
					"a.ElevatorNo as elevatorNo"
			};
			String hql="select distinct "+StringUtils.join(colNames, ",")+"  from ElevatorTransferCaseRegister a"
						+" left outer join (select MAX(checknum) checknum,ElevatorNo from ElevatorTransferCaseRegister group by ElevatorNo) b on a.ElevatorNo=b.ElevatorNo"
						+" left outer join Company c on a.Department=c.ComID "
						+"where a.CheckNum=1 and a.CheckTime !='' "
						+"and a.ElevatorNo not in(select elevatorno from ElevatorTransferCaseRegister where FactoryCheckResult='合格') "
						+"and (a.Status=1 or a.billno=(select distinct b.billno from ElevatorTransferCaseProcess b where b.billno=a.billno and b.TaskName='厂检部长审核')) ";
			if(projectName!=null&&!projectName.equals(""))
            {
            	hql+=" and a.projectName like '%"+projectName.trim()+"%'";
            }
           if(insCompanyName!=null&&!insCompanyName.equals(""))
            {
            	hql+=" and a.insCompanyName like '%"+insCompanyName.trim()+"%'";
            }
            if(salesContractNo!=null&&!salesContractNo.equals(""))
            {
            	hql+=" and a.salesContractNo like '%"+salesContractNo.trim()+"%'";
            }
            if(department!=null&&!department.equals(""))
            {
            	hql+=" and a.department like '%"+department.trim()+"%'";
            }
            if(projectManager!=null&&!projectManager.equals(""))
            {
            	hql+=" and a.projectManager like '%"+projectManager.trim()+"%'";
            }
            if(elevatorNo!=null&&!elevatorNo.equals(""))
            {
            	hql+=" and a.elevatorNo like '%"+elevatorNo.trim()+"%'";
            }

            if (sdate1 != null && !sdate1.equals("")) {
				hql+=" and a.checkTime >= '"+sdate1.trim()+"'";
			}
			if (edate1 != null && !edate1.equals("")) {
				hql+=" and a.checkTime <= '"+edate1.trim()+"'";
			}
            hql+=" order by a.checkTime ";
            
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
					}
                	roleList.add(map);
            	}
            }
            XSSFSheet sheet = wb.createSheet("各分部未关闭记录报表");
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			XSSFRow row0 = sheet.createRow( 0);
			XSSFCell cell0 = row0.createCell((short)0);
			//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell0.setCellValue(showstr);
			
			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				row0 = sheet.createRow(2);
				cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("初检时间");

				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("所属部门");
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("项目名称");
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("合同号");
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("安装单位名称");
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("厂检次数");
				
				cell0 = row0.createCell((short)6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("项目经理");
				
				cell0 = row0.createCell((short)7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("电梯编号");
				
				
				
				for (int i = 0; i < l; i++) {
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+3);
					XSSFCell cell =null;
					
					
					HashMap map=(HashMap) roleList.get(i);
				    cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("checkTime"));
				
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("department"));
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("projectName"));
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("salesContractNo"));
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("insCompanyName"));
					
					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((Integer)map.get("checkNum"));
					
					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("projectManager"));
					
					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("elevatorNo"));
					
					
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("各分部未关闭记录报表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	
}
