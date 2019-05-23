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
 * 厂检模块=>厂检不合格项报表
 * @author Lijun
 *
 */
public class InspectionReportAction extends DispatchAction {

	Log log = LogFactory.getLog(InspectionReportAction.class);

	BaseDataImpl bd = new BaseDataImpl();
	DecimalFormat df = new DecimalFormat("##.##"); 

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,
				SysRightsUtil.NODE_ID_FORWARD + "inspectionreport", null);
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

		request.setAttribute("navigator.location", " 厂检不合格项报表>> 查询");
		DynaActionForm dform = (DynaActionForm) form;
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		Session hs=null;
		
		try{
			String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
			String day1=DateUtil.getDate(day, "MM", -1);//当前日期月份加1 。
			dform.set("sdate1", day1);
			dform.set("edate1", day);
			
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		forward = mapping.findForward("inspectionReportSearch");		
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

		request.setAttribute("navigator.location", " 厂检不合格项报表>> 查询结果");
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
			String issueCoding = request.getParameter("issueCoding");
			String elevatorType = request.getParameter("elevatorType");
			String insCompanyName = request.getParameter("insCompanyName");
			String sdate1=request.getParameter("sdate1");
			String edate1=request.getParameter("edate1");
			
			String projectManager = request.getParameter("projectManager");
			String department = request.getParameter("department");
			
			HashMap rmap= new HashMap();			
			rmap.put("issueCoding", issueCoding);
			rmap.put("elevatorType", elevatorType);
			//rmap.put("maintPersonnel", maintPersonnel);
			rmap.put("sdate1", sdate1);
			rmap.put("edate1", edate1);
			rmap.put("insCompanyName", insCompanyName);
			rmap.put("projectManager", projectManager);
			rmap.put("department", department);
			
			String typename="";
			if(elevatorType!=null && elevatorType.trim().equals("T")){
				typename="直梯";
			}else{
				typename="扶梯";
			}
			String showstr="[提交厂检时间:"+sdate1+" 至 "+edate1+", 问题项编号:"+issueCoding+", 电梯类型:"+typename+", 安装单位名称:"+insCompanyName.trim()+"]";
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
				
				String hql="select a.IssueContents as issueCoding";
				String hql1="select ";
				for(int i=0;i<maxCheck;i++){
					hql+=",isnull(COUNT(case b.CheckNum when "+(i+1)+" then b.elevatorNo end),0) as factory"+(i+1);
					if(i==0){
						hql1+="COUNT(case a.CheckNum when "+(i+1)+" then a.elevatorNo end) as factory"+(i+1);
					}else{
						hql1+=",COUNT(case a.CheckNum when "+(i+1)+" then a.elevatorNo end) as factory"+(i+1);
					}
				}
				
				//审批通过的 或者是 厂检部长审批通过。
				hql+=" from HandoverElevatorCheckItemRegister a,ElevatorTransferCaseRegister b " +
						"where a.billno=b.billno " +
						"and (b.Status=1 or b.billno=(select distinct c.billno from ElevatorTransferCaseProcess c where c.billno=b.billno and c.TaskName='厂检部长审核')) ";
				
				hql1+=" from ElevatorTransferCaseRegister a where "+
						"(a.Status=1 or a.billno=(select distinct b.billno from ElevatorTransferCaseProcess b " +
						"where b.billno=a.billno and b.TaskName='厂检部长审核')) ";
				
				if(issueCoding!=null&&!issueCoding.equals(""))
                {
                	hql+=" and a.issueCoding like '%"+issueCoding.trim()+"%'";
                }
               if(elevatorType!=null&&!elevatorType.equals(""))
                {
                	hql+=" and b.elevatorType like '%"+elevatorType.trim()+"%'";
                	hql1+=" and a.elevatorType like '%"+elevatorType.trim()+"%'";
                }
                if(insCompanyName!=null&&!insCompanyName.equals(""))
                {
                	hql+=" and b.insCompanyName like '%"+insCompanyName.trim()+"%'";
                	hql1+=" and a.insCompanyName like '%"+insCompanyName.trim()+"%'";
                }

                if (sdate1 != null && !sdate1.equals("")) {
					hql+=" and b.checkTime >= '"+sdate1.trim()+" 00:00:00'";
					hql1+=" and a.checkTime >= '"+sdate1.trim()+" 00:00:00'";
				}
				if (edate1 != null && !edate1.equals("")) {
					hql+=" and b.checkTime <= '"+edate1.trim()+" 99:99:99'";
					hql1+=" and a.checkTime <= '"+edate1.trim()+" 99:99:99'";
				}
				
				if(department!=null && !department.equals(""))
                {
                	hql+=" and b.department like '%"+department.trim()+"%'";
                	hql1+=" and a.department like '%"+department.trim()+"%'";
                }
                if(projectManager!=null && !projectManager.equals(""))
                {
                	hql+=" and b.projectManager like '%"+projectManager.trim()+"%'";
                	hql1+=" and a.projectManager like '%"+projectManager.trim()+"%'";
                }
				
                hql+=" group by a.IssueContents ";
                
                //System.out.println("hql>>>>"+hql);
                //System.out.println("hql1>>>>"+hql1);
                
                ps=conn.prepareStatement(hql1);
                rs=ps.executeQuery();
                
                HashMap factoryMap=new HashMap();
                while(rs.next()){
                	for(int j=0;j<maxCheck;j++){
                		factoryMap.put("factory"+j, rs.getInt("factory"+(j+1)));
                	}
                }
                request.setAttribute("factoryMap", factoryMap);

                ps=conn.prepareStatement(hql);
                rs=ps.executeQuery();
                int a=0;

                List maintenanceReportList=new ArrayList();

                while(rs.next())
                {
                	HashMap map= new HashMap();
                	map.put("issueCoding", rs.getString("issueCoding"));
                	//map.put("department", bd.getName("Company", "comfullname", "comid", rs.getString("department")));
                	int factory=0;
                	int factory2=0;
                	List factoryList=new ArrayList();
                	for(int j=0;j<maxCheck;j++){
                		Map map1=new HashMap();
                		map1.put("factory"+j, rs.getInt("factory"+(j+1)));
                		//map1.put("nofactory"+j, rs.getInt("nofactory"+(j+1)));
                		factory+=rs.getInt("factory"+(j+1));
                		double rate=0;
                		if((Integer)factoryMap.get("factory"+j)!=0){
                			rate=(double)(rs.getInt("factory"+(j+1))/Double.valueOf(factoryMap.get("factory"+j).toString()))*100;
                			factory2+=(Integer)factoryMap.get("factory"+j);
                		}else{
                			rate=0;
                		}
                		//map1.put("rate"+j, rate==0 ? rate : rate+"%");
                  		map1.put("rate"+j, rate==0 ? rate+"%" : df.format(rate)+"%");
                		
                		factoryList.add(map1);
                	}
                	map.put("factoryList", factoryList);

                	double allrate=0;
                	if(factory!=0){
                		allrate=(double)(Double.valueOf(factory)/factory2)*100;
            		}else{
            			allrate=0;
            		}
                	map.put("allrate", allrate==0 ? allrate+"%" : df.format(allrate)+"%");
                	
                	
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
			
		 forward = mapping.findForward("inspectionReportList");		
		}
		return forward;
	}
	
	public HttpServletResponse toExcelRecord(ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		String naigation = new String();
		ActionForward forward = null;
		HttpSession session = request.getSession();
		String issueCoding = request.getParameter("issueCoding");
		String elevatorType = request.getParameter("elevatorType");
		String insCompanyName = request.getParameter("insCompanyName");
		String sdate1=request.getParameter("sdate1");
		String edate1=request.getParameter("edate1");
		String projectManager = request.getParameter("projectManager");
		String department = request.getParameter("department");

		String typename="";
		if(elevatorType!=null && elevatorType.trim().equals("T")){
			typename="直梯";
		}else{
			typename="扶梯";
		}
		String showstr="[提交厂检时间:"+sdate1+" 至 "+edate1+", 问题内容:"+issueCoding+", 电梯类型:"+typename+", 安装单位名称:"+insCompanyName.trim()+"]";
		
		Session hs = null;
		Query query = null;
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		XSSFWorkbook wb = new XSSFWorkbook();
		int maxCheck=0;

		try {
			hs = HibernateUtil.getSession();
			
			String sql="select max(checkNum) from ElevatorTransferCaseRegister";
			
			List list1=hs.createSQLQuery(sql).list();
			if(list1!=null && list1.size()>0){
				maxCheck=(Integer) list1.get(0);
			}
			
			conn=hs.connection();
			
			String hql="select a.IssueContents as issueCoding ";
			String hql1="select ";
			for(int i=0;i<maxCheck;i++){
				hql+=",COUNT(case b.CheckNum when "+(i+1)+" then b.elevatorNo end) as factory"+(i+1);
				if(i==0)
					hql1+="COUNT(case a.CheckNum when "+(i+1)+" then a.elevatorNo end) as factory"+(i+1);
				else
					hql1+=",COUNT(case a.CheckNum when "+(i+1)+" then a.elevatorNo end) as factory"+(i+1);
			}
			//审批通过的 或者是 厂检部长审批通过。
			hql+=" from HandoverElevatorCheckItemRegister a,ElevatorTransferCaseRegister b " +
					"where a.billno=b.billno " +
					"and (b.Status=1 or b.billno=(select distinct c.billno from ElevatorTransferCaseProcess c where c.billno=b.billno and c.TaskName='厂检部长审核')) ";
			
			hql1+=" from ElevatorTransferCaseRegister a where "+
					"(a.Status=1 or a.billno=(select distinct b.billno from ElevatorTransferCaseProcess b " +
					"where b.billno=a.billno and b.TaskName='厂检部长审核')) ";
			
			if(issueCoding!=null&&!issueCoding.equals(""))
            {
            	hql+=" and a.issueCoding like '%"+issueCoding.trim()+"%'";
            }
            if(elevatorType!=null&&!elevatorType.equals(""))
            {
            	hql+=" and b.elevatorType like '%"+elevatorType.trim()+"%'";
            	hql1+=" and a.elevatorType like '%"+elevatorType.trim()+"%'";
            }
            if(insCompanyName!=null&&!insCompanyName.equals(""))
            {
            	hql+=" and b.insCompanyName like '%"+insCompanyName.trim()+"%'";
            	hql1+=" and a.insCompanyName like '%"+insCompanyName.trim()+"%'";
            }

            if (sdate1 != null && !sdate1.equals("")) {
				hql+=" and b.checkTime >= '"+sdate1.trim()+" 00:00:00'";
				hql1+=" and a.checkTime >= '"+sdate1.trim()+" 00:00:00'";
			}
			if (edate1 != null && !edate1.equals("")) {
				hql+=" and b.checkTime <= '"+edate1.trim()+" 99:99:99'";
				hql1+=" and a.checkTime <= '"+edate1.trim()+" 99:99:99'";
			}
			
			if(department!=null && !department.equals(""))
            {
            	hql+=" and b.department like '%"+department.trim()+"%'";
            	hql1+=" and a.department like '%"+department.trim()+"%'";
            }
            if(projectManager!=null && !projectManager.equals(""))
            {
            	hql+=" and b.projectManager like '%"+projectManager.trim()+"%'";
            	hql1+=" and a.projectManager like '%"+projectManager.trim()+"%'";
            }
			
            hql+=" group by a.IssueContents ";
            
            //System.out.println(hql);
            ps=conn.prepareStatement(hql1);
            rs=ps.executeQuery();
            HashMap factoryMap=new HashMap();
            while(rs.next()){
            	for(int j=0;j<maxCheck;j++){
            		factoryMap.put("factory"+j, rs.getInt("factory"+(j+1)));
            	}
            }
            
            ps=conn.prepareStatement(hql);
            rs=ps.executeQuery();
           
            List roleList=new ArrayList();
           
            while(rs.next()){
            	HashMap map= new HashMap();
            	map.put("issueCoding", rs.getString("issueCoding"));
            	//map.put("department", bd.getName("Company", "comfullname", "comid", rs.getString("department")));
            	int factory=0;
            	int factory2=0;
            	for(int j=0;j<maxCheck;j++){
            		
            		map.put("factory"+j, rs.getInt("factory"+(j+1)));
            		//map.put("nofactory"+j, rs.getInt("nofactory"+(j+1)));
            		factory+=rs.getInt("factory"+(j+1));
            		double rate=0;
            		if((Integer)factoryMap.get("factory"+j)!=0){
            			rate=rs.getInt("factory"+(j+1))/Double.valueOf((Integer)factoryMap.get("factory"+j))*100;
            			factory2+=Double.valueOf((Integer)factoryMap.get("factory"+j));
            		}else{
            			rate=0;
            		}
            		map.put("rate"+j, rate==0 ? rate+"%" : df.format(rate)+"%");
            		
            	}
            	double allRate=0;
            	if(factory2!=0){
            		allRate=(double)(factory/Double.valueOf(factory2))*100;
            	}
            	map.put("allRate", allRate==0 ? allRate+"%" : df.format(allRate)+"%");
            	roleList.add(map);
            }
            
            XSSFSheet sheet = wb.createSheet("厂检不合格项报表");
			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				
				XSSFRow row0 = sheet.createRow(0);
				XSSFCell cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(showstr);
				
				
				int l = roleList.size();
				row0 = sheet.createRow(2);
				cell0 = row0.createCell((short)0);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("问题内容");
				
				/*cell0 = row0.createCell((short)1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("所属部门");*/
				
				int index=0;
				for(int k=0;k<maxCheck;k++){
					if(k==0){
						cell0 = row0.createCell((short)1);
						//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell0.setCellValue("初检总台数");
						
						/*cell0 = row0.createCell((short)3);
						//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell0.setCellValue("初检合格台数");
						
						cell0 = row0.createCell((short)4);
						//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell0.setCellValue("初检合格率");*/
						
					}else{
//						//System.out.println(bd.numToChinese(k+1));
						String num=bd.numToChinese(k+1);
						num=num.replaceAll("个", "");
						cell0 = row0.createCell((short)index+1);
						//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell0.setCellValue(num+"检总台数");
						
					}
					
					cell0 = row0.createCell((short)index+2);
					//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell0.setCellValue("问题项出现次数");
					
					cell0 = row0.createCell((short)index+3);
					//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell0.setCellValue("出现概率(%)");
					index+=3;
				}
				
				cell0 = row0.createCell((short)index+1);
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue("总概率(%)");
				
				
				
				for (int i = 0; i < l; i++) {
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i+3);
					XSSFCell cell =null;
					
					
					HashMap map=(HashMap) roleList.get(i);
				    cell = row.createCell((short)0);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String)map.get("issueCoding"));
					
					/*cell = row.createCell((short)1);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(map.get("department").toString());*/
					
					int ind=0;
					for(int j=0;j<maxCheck;j++){
						cell = row.createCell((short)ind+1);
						//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(Integer.valueOf(factoryMap.get("factory"+j).toString()));
						
						cell = row.createCell((short)ind+2);
						//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(Integer.valueOf(map.get("factory"+j).toString()));
						
						cell = row.createCell((short)ind+3);
						//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(map.get("rate"+j).toString());
						
						ind+=3;
					}
					
					cell = row.createCell((short)ind+1);
					//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(map.get("allRate").toString());
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("厂检不合格项报表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	
}
