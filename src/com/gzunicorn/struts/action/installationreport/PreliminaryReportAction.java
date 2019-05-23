package com.gzunicorn.struts.action.installationreport;


import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

/**
 * 厂检模块=>初审驳回报表
 * @author Lijun
 *
 */
public class PreliminaryReportAction extends DispatchAction {

	Log log = LogFactory.getLog(PreliminaryReportAction.class);

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
				SysRightsUtil.NODE_ID_FORWARD + "preliminaryreport", null);
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

		request.setAttribute("navigator.location", " 初审驳回报表>> 查询");
		ActionForward forward = null;
		DynaActionForm dform = (DynaActionForm) form;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		try{
			String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
			String day1=DateUtil.getDate(day, "MM", -1);//当前日期月份加1 。
			dform.set("sdate1", day1);
			dform.set("edate1", day);
		} catch (Exception e) {
			e.printStackTrace();
		}
		forward = mapping.findForward("preliminaryReportSearch");		
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

		request.setAttribute("navigator.location", " 初审驳回报表>> 查询结果");
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
			String staffName = request.getParameter("staffName");
			String approveResult = request.getParameter("approveResult");
//			String maintPersonnel = request.getParameter("maintPersonnel");
			String sdate1=request.getParameter("sdate1");
			String edate1=request.getParameter("edate1");
			HashMap rmap= new HashMap();			
			rmap.put("staffName", staffName);
			rmap.put("approveResult", approveResult);
			//rmap.put("maintPersonnel", maintPersonnel);
			rmap.put("sdate1", sdate1);
			rmap.put("edate1", edate1);

			String showstr="[审核时间:"+sdate1+" 至 "+edate1+", 厂检员:"+staffName+", 审批结果:"+approveResult+"]";
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
						"c.UserName as staffName",
						"a.projectName as projectName",
						"a.SalesContractNo as salesContractNo",
						"a.ElevatorNo as elevatorNo",
						"b.ApproveRem as approveRem",
						"d.UserName as userName",
						"b.Date1+' '+b.Time1 as date1",
						"b.approveResult as approveResult"
				};
				String hql="select "+StringUtils.join(colNames, ",")+" from"
						+" ElevatorTransferCaseRegister a right outer join  ElevatorTransferCaseProcess b on a.billno=b.billno"
						+" left outer join LoginUser c on a.StaffName=c.UserID"
						+" left outer join LoginUser d on  b.UserID=d.UserID"
						+" where b.approveResult in('驳回','不通过')";
				if(staffName!=null&&!staffName.equals(""))
                {
                	hql+=" and c.userName like '%"+staffName.trim()+"%'";
                }
               if(approveResult!=null&&!approveResult.equals(""))
                {
                	hql+=" and b.approveResult like '%"+approveResult.trim()+"%'";
                }

                if (sdate1 != null && !sdate1.equals("")) {
					hql+=" and b.Date1 >= '"+sdate1.trim()+" 00:00:00'";
				}
				if (edate1 != null && !edate1.equals("")) {
					hql+=" and b.Date1 <= '"+edate1.trim()+" 99:99:99'";
				}
                hql+=" order by b.Date1+' '+b.Time1 ";
                
                System.out.println(">>>>"+hql);

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
			
		 forward = mapping.findForward("preliminaryReportList");		
		}
		return forward;
	}
	
	public HttpServletResponse toExcelRecord(ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		String naigation = new String();
		ActionForward forward = null;
		HttpSession session = request.getSession();
		String staffName = request.getParameter("staffName");
		String approveResult = request.getParameter("approveResult");
		//String maintPersonnel = request.getParameter("maintPersonnel");
		String sdate1=request.getParameter("sdate1");
		String edate1=request.getParameter("edate1");

		String showstr="[审核时间:"+sdate1+" 至 "+edate1+", 厂检员:"+staffName+", 审批结果:"+approveResult+"]";
		
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			String[] colNames = {
					"a.CheckTime as checkTime",
					"c.UserName as staffName",
					"a.projectName as projectName",
					"a.SalesContractNo as salesContractNo",
					"a.ElevatorNo as elevatorNo",
					"b.ApproveRem as approveRem",
					"d.UserName as userName",
					"b.Date1+' '+b.Time1 as date1",
					"b.approveResult as approveResult"
			};
			String hql="select "+StringUtils.join(colNames, ",")+" from"
					+" ElevatorTransferCaseRegister a right outer join  ElevatorTransferCaseProcess b on a.billno=b.billno"
					+" left outer join LoginUser c on a.StaffName=c.UserID"
					+" left outer join LoginUser d on  b.UserID=d.UserID"
					+" where b.approveResult in('驳回','不通过')";
			if(staffName!=null&&!staffName.equals(""))
            {
            	hql+=" and c.userName like '%"+staffName.trim()+"%'";
            }
           if(approveResult!=null&&!approveResult.equals(""))
            {
            	hql+=" and b.approveResult like '%"+approveResult.trim()+"%'";
            }
            /*if(maintPersonnel!=null&&!maintPersonnel.equals(""))
            {
            	hql+=" and mwpd.maintPersonnel like '%"+maintPersonnel.trim()+"%'";
            }*/

            if (sdate1 != null && !sdate1.equals("")) {
				hql+=" and b.Date1 >= '"+sdate1.trim()+" 00:00:00'";
			}
			if (edate1 != null && !edate1.equals("")) {
				hql+=" and b.Date1 <= '"+edate1.trim()+" 99:99:99'";
			}
            hql+=" order by b.Date1+' '+b.Time1 ";
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
            XSSFSheet sheet = wb.createSheet("初审驳回报表");
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
				cell0.setCellValue("厂检时间");

				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("厂检员");
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("项目名称");
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("销售合同号");
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("电梯编号");
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("审批意见");
				
				cell0 = row0.createCell((short)6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("审核人");
				
				cell0 = row0.createCell((short)7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("审核时间");
				
				cell0 = row0.createCell((short)8);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("审批结果");
				
				
				
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
					cell.setCellValue((String)map.get("staffName"));
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("projectName"));
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("salesContractNo"));
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("elevatorNo"));
					
					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("approveRem"));
					
					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("userName"));
					
					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("date1"));
					
					cell = row.createCell((short)8);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					Clob clob = (Clob)(map.get("approveResult"));
					String str=clob.getSubString(1, (int)clob.length());
					cell.setCellValue(str);
					
					
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("初审驳回报表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	
}
