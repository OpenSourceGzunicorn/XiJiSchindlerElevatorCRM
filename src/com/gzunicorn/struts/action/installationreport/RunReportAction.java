package com.gzunicorn.struts.action.installationreport;


import java.io.IOException;
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
 * 厂检模块=>空跑报表
 * @author Lijun
 *
 */
public class RunReportAction extends DispatchAction {

	Log log = LogFactory.getLog(RunReportAction.class);

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
				SysRightsUtil.NODE_ID_FORWARD + "runreport", null);
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

		request.setAttribute("navigator.location", " 空跑报表>> 查询");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
		
		Session hs=null;
		try {
			hs=HibernateUtil.getSession();
			String sql1="from Company where (comtype='1' or comtype='2') and enabledflag='Y' and comid='"+userInfo.getComID()+"' order by comid desc";				
			List list1=hs.createQuery(sql1).list();
			
			String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
			String day1=DateUtil.getDate(day, "MM", -1);//当前日期月份加1 。
			dform.set("sdate1", day1);
			dform.set("edate1", day);
			
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
		} catch (DataStoreException e) {
			e.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		request.setAttribute("bhTypeList", bd.getPullDownList("ElevatorTransferCaseRegister_BhType"));
		forward = mapping.findForward("runReportSearch");		
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

		request.setAttribute("navigator.location", " 空跑报表>> 查询结果");
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
				e.printStackTrace();
			}
		} else {
			String projectName = request.getParameter("projectName");
			String insCompanyName = request.getParameter("insCompanyName");
			String salesContractNo = request.getParameter("salesContractNo");
			String projectManager=request.getParameter("projectManager");
			String bhType=request.getParameter("bhType");
			String department=request.getParameter("department");
			String sdate1=request.getParameter("sdate1");
			String edate1=request.getParameter("edate1");
			String elevatorNo=request.getParameter("elevatorNo");
			
			
			HashMap rmap= new HashMap();			
			rmap.put("projectName", projectName);
			rmap.put("insCompanyName", insCompanyName);
			rmap.put("salesContractNo", salesContractNo);
			rmap.put("projectManager", projectManager);
			rmap.put("bhType", bhType);
			rmap.put("department", department);
			rmap.put("sdate1", sdate1);
			rmap.put("edate1", edate1);
			rmap.put("elevatorNo", elevatorNo);
			/*if(maintDivision.equals("00"))
			{
				maintDivision=null;
			}*/
			
			Session hs = null;
			Query query = null;
			Connection conn=null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				hs = HibernateUtil.getSession();
				conn=hs.connection();
				
				String[] colNames = {
						"a.billno as billno",
						"b.ComFullName as department",
						"a.SalesContractType as salesContractType",
						"a.SalesContractNo as salesContractNo",
						"a.projectName as projectName",
						"a.ElevatorNo as elevatorNo",
						"a.projectManager as projectManager",
						"a.InsCompanyName as insCompanyName",
						"c.pullname as bhType",
						"t.BhRem as bhRem",
						"t.BhDate as bhDate"
						
				};
				String hql="select "+StringUtils.join(colNames, ",")+" from ElevatorTransferCaseRegister a"
						+" left join Company b on a.Department=b.ComID,"
						+ "elevatortransfercasebhtype t "
						+" left join (select pullid,pullname from pulldown where typeflag='ElevatorTransferCaseRegister_BhType') c on t.BhType=c.pullid"
						//+" where ISNULL(a.BhDate,'')<>'' and a.billno=t.billno ";
						+" where a.billno=t.billno ";
				if(projectName!=null && !projectName.equals(""))
                {
                	hql+=" and a.projectName like '%"+projectName.trim()+"%'";
                }
               if(insCompanyName!=null && !insCompanyName.equals(""))
                {
                	hql+=" and a.insCompanyName like '%"+insCompanyName.trim()+"%'";
                }
                if(salesContractNo!=null && !salesContractNo.equals(""))
                {
                	hql+=" and a.salesContractNo like '%"+salesContractNo.trim()+"%'";
                }
                if(department!=null && !department.equals(""))
                {
                	hql+=" and a.department like '%"+department.trim()+"%'";
                }
                if(projectManager!=null && !projectManager.equals(""))
                {
                	hql+=" and a.projectManager like '%"+projectManager.trim()+"%'";
                }
                if(elevatorNo!=null && !elevatorNo.equals(""))
                {
                	hql+=" and a.elevatorNo like '%"+elevatorNo.trim()+"%'";
                }
                if(bhType!=null && !bhType.equals("")){
                	hql+=" and t.bhType like '"+bhType.trim()+"'";
                }
                if (sdate1 != null && !sdate1.equals("")) {
					hql+=" and t.BhDate >= '"+sdate1.trim()+" 00:00:00'";
				}
				if (edate1 != null && !edate1.equals("")) {
					hql+=" and t.BhDate <= '"+edate1.trim()+" 99:99:99'";
				}
                hql+=" order by a.SalesContractNo,a.billno,t.BhDate desc ";
                
                //System.out.println("空跑报表>>>"+hql);
                
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
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			
		 forward = mapping.findForward("runReportList");		
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
		String department=request.getParameter("department");
		String bhType = request.getParameter("bhType");
		String sdate1=request.getParameter("sdate1");
		String edate1=request.getParameter("edate1");
		String elevatorNo=request.getParameter("elevatorNo");
		/*if(maintDivision.equals("00"))
		{
			maintDivision=null;
		}*/
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			String[] colNames = {
					"a.billno as billno",
					"b.ComFullName as department",
					"a.SalesContractType as salesContractType",
					"a.SalesContractNo as salesContractNo",
					"a.projectName as projectName",
					"a.ElevatorNo as elevatorNo",
					"a.projectManager as projectManager",
					"a.InsCompanyName as insCompanyName",
					"c.pullname as bhType",
					"t.BhRem as bhRem",
					"t.BhDate as bhDate"
					
			};
			String hql="select "+StringUtils.join(colNames, ",")+" from ElevatorTransferCaseRegister a"
					+" left join Company b on a.Department=b.ComID,"
					+ "elevatortransfercasebhtype t "
					+" left join (select pullid,pullname from pulldown where typeflag='ElevatorTransferCaseRegister_BhType') c on t.BhType=c.pullid"
					//+" where ISNULL(a.BhDate,'')<>'' and a.billno=t.billno ";
					+" where a.billno=t.billno ";
			if(projectName!=null && !projectName.equals(""))
            {
            	hql+=" and a.projectName like '%"+projectName.trim()+"%'";
            }
           if(insCompanyName!=null && !insCompanyName.equals(""))
            {
            	hql+=" and a.insCompanyName like '%"+insCompanyName.trim()+"%'";
            }
            if(salesContractNo!=null && !salesContractNo.equals(""))
            {
            	hql+=" and a.salesContractNo like '%"+salesContractNo.trim()+"%'";
            }
            if(department!=null && !department.equals(""))
            {
            	hql+=" and a.department like '%"+department.trim()+"%'";
            }
            if(projectManager!=null && !projectManager.equals(""))
            {
            	hql+=" and a.projectManager like '%"+projectManager.trim()+"%'";
            }
            if(elevatorNo!=null && !elevatorNo.equals(""))
            {
            	hql+=" and a.elevatorNo like '%"+elevatorNo.trim()+"%'";
            }
            if(bhType!=null && !bhType.equals("")){
            	hql+=" and t.bhType like '"+bhType.trim()+"'";
            }
            if (sdate1 != null && !sdate1.equals("")) {
				hql+=" and t.BhDate >= '"+sdate1.trim()+" 00:00:00'";
			}
			if (edate1 != null && !edate1.equals("")) {
				hql+=" and t.BhDate <= '"+edate1.trim()+" 99:99:99'";
			}
            hql+=" order by a.SalesContractNo,a.billno,t.BhDate desc ";
            
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
            XSSFSheet sheet = wb.createSheet("空跑报表");
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("流水号");

				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("所属部门");
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("合同性质");
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("销售合同号");
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("项目名称");
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("电梯编号");
				
				cell0 = row0.createCell((short)6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("项目经理");
				
				cell0 = row0.createCell((short)7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("安装单位名称");
				
				cell0 = row0.createCell((short)8);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("驳回分类");
				
				cell0 = row0.createCell((short)9);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("驳回原因内容");
				
				cell0 = row0.createCell((short)10);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("驳回日期");
				
				
				
				for (int i = 0; i < l; i++) {
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);
					XSSFCell cell =null;
					
					
					HashMap map=(HashMap) roleList.get(i);
				    cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("billno"));
				
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("department"));
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("salesContractType"));
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("salesContractNo"));
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("projectName"));
					
					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("elevatorNo"));
					
					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("projectManager"));
					
					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("insCompanyName"));
					
					cell = row.createCell((short)8);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("bhType"));
					
					cell = row.createCell((short)9);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("bhRem"));
					
					cell = row.createCell((short)10);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("bhDate"));
					
					
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("空跑报表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	
}
