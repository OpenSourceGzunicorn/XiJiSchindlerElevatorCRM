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
 * 厂检模块=>厂检员管理报表
 * @author Lijun
 *
 */
public class InspectorReportAction extends DispatchAction {

	Log log = LogFactory.getLog(InspectorReportAction.class);

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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "inspectorreport", null);
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

		request.setAttribute("navigator.location", " 厂检员管理报表>> 查询");
		DynaActionForm dform = (DynaActionForm) form;
		ActionForward forward = null;
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
		forward = mapping.findForward("inspectorReportSearch");		
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

		request.setAttribute("navigator.location", " 厂检员管理报表>> 查询结果");
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
			String staffName = request.getParameter("staffName");
			String sdate1=request.getParameter("sdate1");
			String edate1=request.getParameter("edate1");
			HashMap rmap= new HashMap();			
			rmap.put("staffName", staffName);
			rmap.put("sdate1", sdate1);
			rmap.put("edate1", edate1);

			
			Session hs = null;
			Query query = null;
			Connection conn=null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				hs = HibernateUtil.getSession();
				conn=hs.connection();
				
				String[] colNames = {
						"b.UserName as staffName",
						"a.billno as billno",
						"a.CheckTime as checkTime",
						"a.CheckNum as checkNum",
						"a.projectName as projectName",
						"a.SalesContractNo as salesContractNo",
						"a.ElevatorNo as elevatorNo",
						"a.Floor as floor",
						"a.Stage as stage",
						"a.Door as door",
						"a.High as high",
						"a.FactoryCheckResult as factoryCheckResult"
				};
				//审批通过的 或者是 已登记已提交。
				String hql="select "+StringUtils.join(colNames, ",")+" from ElevatorTransferCaseRegister a "
						+"left outer join LoginUser b on a.StaffName=b.UserID " 
						+"where a.processStatus in('2','3') ";
				
				if(staffName!=null && !staffName.equals("")){
                	hql+=" and (b.userId like '%"+staffName.trim()+"%' or b.userName like '%"+staffName.trim()+"%')";
                }
                if (sdate1 != null && !sdate1.equals("")) {
					hql+=" and a.CheckTime >= '"+sdate1.trim()+" 00:00:00'";
				}
				if (edate1 != null && !edate1.equals("")) {
					hql+=" and a.CheckTime <= '"+edate1.trim()+" 99:99:99'";
				}
                hql+=" order by a.StaffName,a.billno ";
                
               //System.out.println(hql);

                ps=conn.prepareStatement(hql);
                rs=ps.executeQuery();
                
                for(int j=0;j<colNames.length;j++){
                	colNames[j]=colNames[j].split(" as ")[1];
                }
               
                List maintenanceReportList=new ArrayList();

                while(rs.next())
                {
                	HashMap map= new HashMap();
                	for(int j=0;j<colNames.length;j++)
					{
                		if(colNames[j].equals("checkNum") || colNames[j].equals("floor") || colNames[j].equals("stage") || colNames[j].equals("door"))
                			map.put(colNames[j],rs.getInt(colNames[j]));
                		else if(colNames[j].equals("high"))
                			map.put(colNames[j],rs.getDouble(colNames[j]));
                		else
                			map.put(colNames[j],rs.getString(colNames[j]));
                		
					}
                	maintenanceReportList.add(map);
                }
                if(maintenanceReportList.size()>0){
                	request.setAttribute("maintenanceReportList", maintenanceReportList);
                }

                //计算厂检合格率
            	String sql="select COUNT(case FactoryCheckResult when '合格' then billno end) as '合格数',"
            			+"COUNT(billno) as '总数' "
            			+"from ElevatorTransferCaseRegister a left outer join LoginUser b on a.StaffName=b.UserID "
            			+"where a.processStatus in('2','3') ";
            	
            	if(staffName!=null && !staffName.equals("")){
            		sql+=" and (b.userId like '%"+staffName.trim()+"%' or b.userName like '%"+staffName.trim()+"%')";
                }
    			if (sdate1 != null && !sdate1.equals("")) {
					sql+=" and a.CheckTime >= '"+sdate1.trim()+" 00:00:00'";
				}
				if (edate1 != null && !edate1.equals("")) {
					sql+=" and a.CheckTime <= '"+edate1.trim()+" 99:99:99'";
				}
            	//sql+="group by a.StaffName,b.username";
            	//System.out.println(">>>"+sql);
            	
            	List list=hs.createSQLQuery(sql).list();
            	double rate=0;
            	if(list!=null && list.size()>0){
            		for(Object object : list){
            			Object[] objs=(Object[])object;
            			rate=Double.valueOf(objs[0].toString())/Double.valueOf(objs[1].toString())*100;
            		}
            	}
            	request.setAttribute("rate", df.format(rate));

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
			
		 forward = mapping.findForward("inspectorReportList");		
		}
		return forward;
	}
	
	public HttpServletResponse toExcelRecord(ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		String naigation = new String();
		ActionForward forward = null;
		HttpSession session = request.getSession();
		String staffName = request.getParameter("staffName");
		//String approveResult = request.getParameter("approveResult");
		//String maintPersonnel = request.getParameter("maintPersonnel");
		String sdate1=request.getParameter("sdate1");
		String edate1=request.getParameter("edate1");
		/*if(maintDivision.equals("00"))
		{
			maintDivision=null;
		}*/
		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();

		try {
			hs = HibernateUtil.getSession();

			String[] colNames = {
					"b.UserName as staffName",
					"a.billno as billno",
					"a.CheckTime as checkTime",
					"a.CheckNum as checkNum",
					"a.projectName as projectName",
					"a.SalesContractNo as salesContractNo",
					"a.ElevatorNo as elevatorNo",
					"a.Floor as floor",
					"a.Stage as stage",
					"a.Door as door",
					"a.High as high",
					"a.FactoryCheckResult as factoryCheckResult"
			};
			//审批通过的 或者是 已登记已提交。
			String hql="select "+StringUtils.join(colNames, ",")+" from ElevatorTransferCaseRegister a "
					+"left outer join LoginUser b on a.StaffName=b.UserID " 
					+"where a.processStatus in('2','3') ";
			
			if(staffName!=null && !staffName.equals("")){
            	hql+=" and (b.userId like '%"+staffName.trim()+"%' or b.userName like '%"+staffName.trim()+"%')";
            }
            if (sdate1 != null && !sdate1.equals("")) {
				hql+=" and a.CheckTime >= '"+sdate1.trim()+" 00:00:00'";
			}
			if (edate1 != null && !edate1.equals("")) {
				hql+=" and a.CheckTime <= '"+edate1.trim()+" 99:99:99'";
			}
            hql+=" order by a.StaffName,a.billno ";
            
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

            //计算厂检合格率
            double rate=0;
        	String sql="select COUNT(case FactoryCheckResult when '合格' then billno end) as '合格数',"
        			+"COUNT(billno) as '总数' "
        			+"from ElevatorTransferCaseRegister a left outer join LoginUser b on a.StaffName=b.UserID "
        			+"where a.processStatus in('2','3') ";
        			
        	if(staffName!=null && !staffName.equals("")){
        		sql+=" and (b.userId like '%"+staffName.trim()+"%' or b.userName like '%"+staffName.trim()+"%')";
            }
			if (sdate1 != null && !sdate1.equals("")) {
				sql+=" and a.CheckTime >= '"+sdate1.trim()+" 00:00:00'";
			}
			if (edate1 != null && !edate1.equals("")) {
				sql+=" and a.CheckTime <= '"+edate1.trim()+" 99:99:99'";
			}
        	//sql+="group by a.StaffName,b.username";
        	
        	List list1=hs.createSQLQuery(sql).list();
        	if(list1!=null && list1.size()>0){
        		for(Object object : list1){
        			Object[] objs=(Object[])object;
        			rate=Double.valueOf(objs[0].toString())/Double.valueOf(objs[1].toString())*100;
        		}
        	}
        	
        	//request.setAttribute("rate", rate);

            XSSFSheet sheet = wb.createSheet("厂检员管理报表");
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("厂检人员姓名");

				cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("流水号");
				
				cell0 = row0.createCell((short)2);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("提交厂检时间");
				
				cell0 = row0.createCell((short)3);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("厂检次数");
				
				cell0 = row0.createCell((short)4);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("项目名称");
				
				cell0 = row0.createCell((short)5);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("销售合同号");
				
				cell0 = row0.createCell((short)6);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("电梯编号");
				
				cell0 = row0.createCell((short)7);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("层");
				
				cell0 = row0.createCell((short)8);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("站");

				cell0 = row0.createCell((short)9);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("门");
				
				cell0 = row0.createCell((short)10);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("提升高度");

				cell0 = row0.createCell((short)11);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("厂检结果");
				
				
				for (int i = 0; i < l; i++) {
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+1);
					XSSFCell cell =null;
					
					
					HashMap map=(HashMap) roleList.get(i);
				    cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("staffName"));
				
					cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("billno"));
					
					cell = row.createCell((short)2);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("checkTime"));
					
					cell = row.createCell((short)3);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((Integer)map.get("checkNum"));
					
					cell = row.createCell((short)4);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("projectName"));
					
					cell = row.createCell((short)5);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("salesContractNo"));
					
					cell = row.createCell((short)6);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("elevatorNo"));
					
					cell = row.createCell((short)7);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((Integer)map.get("floor"));
					
					cell = row.createCell((short)8);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((Integer)map.get("stage"));
					
					cell = row.createCell((short)9);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((Integer)map.get("door"));
					
					cell = row.createCell((short)10);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(Double.valueOf(map.get("high").toString()));
					
					cell = row.createCell((short)11);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("factoryCheckResult"));
					
					
				}
				
				XSSFRow row1 = sheet.createRow(l+1);
				XSSFCell cell1 =null;
				
				cell1 = row1.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell1.setCellValue("厂检合格率");
				
				cell1 = row1.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell1.setCellValue(df.format(rate)+"%");
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("厂检员管理报表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	
}
