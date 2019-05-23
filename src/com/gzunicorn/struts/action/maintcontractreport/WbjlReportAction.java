package com.gzunicorn.struts.action.maintcontractreport;


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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
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
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.sysmanager.pulldown.Pulldown;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

/**
 * 工程保养报表=>维保销售奖励报表
 * @author Lijun
 *
 */
public class WbjlReportAction extends DispatchAction {

	Log log = LogFactory.getLog(BytlReportAction.class);

	BaseDataImpl bd = new BaseDataImpl();
	DecimalFormat df = new DecimalFormat("##.##"); 
	DecimalFormat df2 = new DecimalFormat("##"); 

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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "wbjlreport", null);
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
	 * Get the navigation description from the properties file by navigation
	 * key;
	 * 
	 * @param request
	 * @param navigation
	 */
	
	private void setNavigation(HttpServletRequest request, String navigation) {
		Locale locale = this.getLocale(request);
		MessageResources messages = getResources(request);
		request.setAttribute("navigator.location",messages.getMessage(locale, navigation));
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

		request.setAttribute("navigator.location", " 维保销售奖励报表 >> 查询");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
		
		Session hs=null;
		try {
			hs=HibernateUtil.getSession();
			
			//分部下拉框列表
			List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
			request.setAttribute("maintDivisionList", maintDivisionList);
			//维保站下拉框
			//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
			String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
			List vmlist=hs.createSQLQuery(sqlss).list();
			if(vmlist!=null && vmlist.size()>0){
				String hql="select a from Storageid a where a.storageid= '"+userInfo.getStorageId()+"' " +
						"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";	
				List mainStationList=hs.createQuery(hql).list();
				request.setAttribute("mainStationList", mainStationList);
			}else{
				String hql="select a from Storageid a where a.comid like '"+userInfo.getComID()+"' " +
						"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
				List mainStationList=hs.createQuery(hql).list();
			  
				 Storageid storid=new Storageid();
				 storid.setStorageid("%");
				 storid.setStoragename("全部");
				 mainStationList.add(0,storid);
				 
				 request.setAttribute("mainStationList", mainStationList);
			}
			
			String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
			String day1=DateUtil.getDate(day, "MM", -1);//当前日期月份加1 。
			dform.set("sdate2", day1);
			dform.set("edate2", day);
			
			//request.setAttribute("causeAnalysisList", bd.getPullDownList("LostElevatorReport_CauseAnalysis"));
			
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

		forward = mapping.findForward("wbjlReportSearch");		
		return forward;
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

		request.setAttribute("navigator.location", " 维保销售奖励报表 >> 查询结果");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;

		String maintdivision = request.getParameter("maintdivision");
		String maintstation = request.getParameter("maintstation");
		String maintcontractno = request.getParameter("maintcontractno");
		String salescontractno = request.getParameter("salescontractno");
		String elevatorno = request.getParameter("elevatorno");
		String sdate1 = request.getParameter("sdate1");
		if(sdate1==null || sdate1.trim().equals("")){
			sdate1="0000-00-00";
		}
		String edate1 = request.getParameter("edate1");
		if(edate1==null || edate1.trim().equals("")){
			edate1="9999-99-99";
		}
		String sdate2 = request.getParameter("sdate2");
		if(sdate2==null || sdate2.trim().equals("")){
			sdate2="0000-00-00";
		}
		String edate2 = request.getParameter("edate2");
		if(edate2==null || edate2.trim().equals("")){
			edate2="9999-99-99";
		}

		HashMap rmap= new HashMap();			
		rmap.put("maintdivision", maintdivision);
		rmap.put("maintstation", maintstation);
		rmap.put("maintcontractno", maintcontractno);
		rmap.put("salescontractno", salescontractno);
		rmap.put("elevatorno", elevatorno);
		rmap.put("sdate1", sdate1);
		rmap.put("edate1", edate1);
		rmap.put("sdate2", sdate2);
		rmap.put("edate2", edate2);
		request.setAttribute("rmap", rmap);
		
		Session hs = null;
		ResultSet rs=null;
		try {
			hs = HibernateUtil.getSession();

			String hql="exec sp_wbjlReport '"+maintdivision.trim()+"','"+maintstation.trim()+"',"
					+ "'"+maintcontractno.trim()+"','"+salescontractno.trim()+"','"+elevatorno.trim()+"',"
					+ "'"+sdate1.trim()+"','"+edate1.trim()+"','"+sdate2.trim()+"','"+edate2.trim()+"'";
			System.out.println("维保销售奖励报表 >>>>"+hql);
			rs=hs.connection().prepareCall(hql).executeQuery();

            List reportList=new ArrayList();
            double totalmoney=0;//实际奖金
            while(rs.next()){
            	HashMap map= new HashMap();
            	
            	map.put("billno", rs.getString("billno"));//维保流水号
            	map.put("maintdivision", rs.getString("maintdivision"));//所属分部门代码    
            	map.put("comname", rs.getString("comname"));//所属分部门    
            	map.put("maintstation", rs.getString("maintstation"));//所属维保站代码    
            	map.put("storagename", rs.getString("storagename"));//所属维保站    
            	map.put("maintcontractno", rs.getString("maintcontractno"));//维保合同号
            	map.put("salescontractno", rs.getString("salescontractno"));//销售合同号
            	map.put("elevatorno", rs.getString("elevatorno"));//电梯编号,
            	map.put("elevatortype", rs.getString("elevatortype"));//电梯类型,
            	map.put("elevatortypename", rs.getString("elevatortypename"));//电梯类型,
            	map.put("floorstage", rs.getString("floor")+"/"+rs.getString("stage")+"/"+rs.getString("door"));//层站门
            	map.put("bymoney", rs.getDouble("bymoney"));//保养费金额
            	map.put("signwayname", rs.getString("signwayname"));//变更方式
            	map.put("contractperiod", rs.getString("contractperiod"));//有效期
            	map.put("jgtzfd2", rs.getString("jgtzfd2"));//价格调整幅度
            	map.put("ywyf", rs.getString("ywyf"));//延误与否
            	map.put("zkl", rs.getString("zkl"));//折扣率
            	map.put("xsjj", rs.getDouble("xsjj"));//销售奖金
            	map.put("lkdate", rs.getString("lkdate"));//回款日期
            	map.put("lkmoney", rs.getDouble("lkmoney"));//回款金额
            	map.put("lkbl", rs.getString("lkbl"));//回款比例
            	map.put("sjjj", rs.getDouble("sjjj"));//实际奖金
            	map.put("lkdate1", rs.getString("lkdate1"));//来款录入日期

            	double sjjj=rs.getDouble("sjjj");
            	totalmoney=totalmoney+sjjj;
            	
            	reportList.add(map);
            }
            
            HashMap hmap=new HashMap();
            hmap.put("totalmoney", df.format(totalmoney));
            
            if (dform.get("genReport") != null && dform.get("genReport").equals("Y")) {
            	//导出excel
    			response = toExcelRecord(response,reportList,hmap);
    			forward = mapping.findForward("exportExcel");
    		}else{
                if(reportList.size()>0 && reportList.size()<3001){
                	request.setAttribute("maintenanceWbjlReportList", reportList);
                }else if(reportList.size()>3000){
                	request.setAttribute("showinfostr", "查询数据超出三千行记录，请导出EXCEL查看。");
                }else{
                	request.setAttribute("showinfostr", "没有记录显示！");
                }
                request.setAttribute("totalhmap", hmap);
                
    			forward = mapping.findForward("wbjlReportList");	
    		}
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
				

		return forward;
	}
	
	public HttpServletResponse toExcelRecord(HttpServletResponse response,
			List ReportList,HashMap hmap) throws IOException {


		String[] titlename={"维保分部","维保站","维保合同号","销售合同号","电梯编号","电梯类型","层站",
				"保养费金额","变更方式","有效期","价格调整幅度","延误与否","折扣率","销售奖金","来款日期","来款录入日期","来款金额","来款比例","实际奖金"};
		String[] titleid={"comname","storagename","maintcontractno","salescontractno","elevatorno","elevatortypename","floorstage",
				"bymoney","signwayname","contractperiod","jgtzfd2","ywyf","zkl","xsjj","lkdate","lkdate1","lkmoney","lkbl","sjjj"};
		
		XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("维保销售奖励报表");
        
        //创建单元格样式
        XSSFCellStyle cs = wb.createCellStyle();
        cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);//左右居中
        cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
        XSSFFont f  = wb.createFont();
        f.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 字体加粗
        cs.setFont(f);
        
        int toolnum=2;
        //创建标题
        XSSFRow row0 = sheet.createRow( 0);
        XSSFCell cell0 = null;
        int ttl=titlename.length;
		for(int i=0;i<ttl;i++){
			cell0 = row0.createCell((short)i);
			//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell0.setCellValue(titlename[i]);
			cell0.setCellStyle(cs);
		}
		
		//创建内容
		if (ReportList != null && ReportList.size()>0) {
			XSSFRow row = null;
			XSSFCell cell =null;
			toolnum=toolnum+ReportList.size();
			for (int j = 0; j < ReportList.size(); j++) {
				HashMap map=(HashMap) ReportList.get(j);
				// 创建Excel行，从0行开始
				row = sheet.createRow(j+1);
				int ttl2=titleid.length;
				for(int c=0;c<ttl2;c++){
				    cell = row.createCell((short)c);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				    if(titleid[c].equals("bymoney") || titleid[c].equals("xsjj")
				    		 || titleid[c].equals("lkmoney") || titleid[c].equals("sjjj")){
				    	cell.setCellValue((Double)map.get(titleid[c]));
				    }else{
				    	cell.setCellValue((String)map.get(titleid[c]));
				    }
				}
				
			}
		}
		//参数：起始行号，终止行号， 起始列号，终止列号
		//sheet.addMergedRegion(new CellRangeAddress(toolnum, toolnum, 0, 8));
		XSSFRow row1 = sheet.createRow(toolnum);
		XSSFCell cell1 = row1.createCell((short)0);
		cell1.setCellValue("合计   实际奖金(元):"+(String)hmap.get("totalmoney"));
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("维保销售奖励报表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	
	/**
	 * ajax 级联 分部与维保站
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public void toStorageIDList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		Session hs=null;
		String comid=request.getParameter("comid");
		response.setHeader("Content-Type","text/html; charset=GBK");
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='GBK'?>");
		sb.append("<root>");
		try {
			hs=HibernateUtil.getSession();
			if(comid!=null && !"".equals(comid)){
				String hql="select a from Storageid a where a.comid='"+comid+"' " +
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
