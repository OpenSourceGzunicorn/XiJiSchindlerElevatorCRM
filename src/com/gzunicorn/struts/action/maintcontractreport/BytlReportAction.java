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
 * 工程保养报表=>保养台量奖计算报表
 * @author Lijun
 *
 */
public class BytlReportAction extends DispatchAction {

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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "bytlreport", null);
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

		request.setAttribute("navigator.location", " 保养台量奖报表 >> 查询");
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
			
			//String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
			//String day1=DateUtil.getDate(day, "MM", -1);//当前日期月份加1 。
			//dform.set("sdate1", day1);
			//dform.set("edate1", day);
			
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

		forward = mapping.findForward("bytlReportSearch");		
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

		request.setAttribute("navigator.location", " 保养台量奖报表 >> 查询结果");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;

		String maintdivision = request.getParameter("maintdivision");
		String maintstation = request.getParameter("maintstation");
		String maintcontractno = request.getParameter("maintcontractno");
		String salescontractno = request.getParameter("salescontractno");
		String elevatorno = request.getParameter("elevatorno");
		String maintpersonnel = request.getParameter("maintpersonnel");
		/**
		String sdate1 = request.getParameter("sdate1");
		if(sdate1==null || sdate1.trim().equals("")){
			sdate1="0000-00-00";
		}
		String edate1 = request.getParameter("edate1");
		if(edate1==null || edate1.trim().equals("")){
			edate1="9999-99-99";
		}
		 */
		HashMap rmap= new HashMap();			
		rmap.put("maintdivision", maintdivision);
		rmap.put("maintstation", maintstation);
		rmap.put("maintcontractno", maintcontractno);
		rmap.put("salescontractno", salescontractno);
		rmap.put("elevatorno", elevatorno);
		rmap.put("maintpersonnel", maintpersonnel);
		//rmap.put("sdate1", sdate1);
		//rmap.put("edate1", edate1);
		request.setAttribute("rmap", rmap);
		
		Session hs = null;
		ResultSet rs=null;
		try {
			hs = HibernateUtil.getSession();

			String hql="exec sp_bytlReport '"+maintdivision.trim()+"','"+maintstation.trim()+"',"
					+ "'"+maintcontractno.trim()+"','"+salescontractno.trim()+"','"+elevatorno.trim()+"',"
					+ "'"+maintpersonnel.trim()+"'";
			System.out.println("保养台量奖计算报表>>>>"+hql);
			rs=hs.connection().prepareCall(hql).executeQuery();

            List reportList=new ArrayList();
            double totalmoney=0;
            while(rs.next()){
            	HashMap map= new HashMap();
            	
            	String elevatortype=rs.getString("elevatortype");
            	map.put("billno", rs.getString("billno"));//维保流水号
            	map.put("maintdivision", rs.getString("maintdivision"));//所属分部门代码    
            	map.put("comname", rs.getString("comname"));//所属分部门    
            	map.put("maintstation", rs.getString("maintstation"));//所属维保站代码    
            	map.put("storagename", rs.getString("storagename"));//所属维保站    
            	map.put("maintcontractno", rs.getString("maintcontractno"));//维保合同号
            	map.put("salescontractno", rs.getString("salescontractno"));//销售合同号
            	map.put("elevatorno", rs.getString("elevatorno"));//电梯编号,
            	map.put("elevatortype", elevatortype);//电梯类型,
            	map.put("elevatortypename", rs.getString("elevatortypename"));//电梯类型,
            	map.put("floorstage", rs.getString("floor")+"/"+rs.getString("stage")+"/"+rs.getString("door"));//层站门
            	map.put("elevatornature", rs.getString("elevatornature"));//电梯性质代码
            	map.put("elevatornaturename", rs.getString("elevatornaturename"));//电梯性质名称
            	map.put("elevatornaturexs", rs.getString("elevatornaturexs"));//电梯性质系数
            	map.put("nummoney", rs.getDouble("nummoney"));//台量奖,
            	map.put("maintpersonnel", rs.getString("maintpersonnel"));//维保工  
            	map.put("maintpersonnelname", rs.getString("maintpersonnelname"));//维保工名称
            	map.put("idcardno", rs.getString("idCardNo"));//身份证
            	//map.put("tasksubdate", rs.getString("tasksubdate"));//下达日期

            	if(elevatortype!=null && elevatortype.trim().equals("T")){
                	map.put("floorstagexs", rs.getString("floorstagexs"));//--层站系数,
                	map.put("seriesid", "");//扶梯类型
                	map.put("seriesidname", "");//扶梯类型名称
                	map.put("seriesidxs", "");//扶梯系数,
            	}else{
                	map.put("floorstagexs", "");//--层站系数,
                	map.put("seriesid", rs.getString("seriesid"));//扶梯类型
                	map.put("seriesidname", rs.getString("seriesidname"));//扶梯类型名称
                	map.put("seriesidxs", rs.getString("seriesidxs"));//扶梯系数,
            	}
            	map.put("weight", rs.getString("weight"));//载重,
            	map.put("weightxs", rs.getString("weightxs"));//载重系数,
            	map.put("tlnum", rs.getString("tlnum"));//台量,
            	map.put("tlxs", rs.getString("tlxs"));//台量系数,
            	map.put("transfercomplete", rs.getString("transfercomplete"));//交接是否完成,
            	map.put("r4", rs.getString("r4"));//是否发台量奖,

            	double nummoney=rs.getDouble("nummoney");
            	totalmoney=totalmoney+nummoney;
            	
            	reportList.add(map);
            }
            
            HashMap hmap=new HashMap();
            hmap.put("totalmoney", df.format(totalmoney));

            List ElevatorNatureList=bd.getPullDownList("MaintContractDetail_ElevatorNature");
            request.setAttribute("ElevatorNatureList",ElevatorNatureList);
            request.setAttribute("colnum", 14+ElevatorNatureList.size());
            
            if (dform.get("genReport") != null && dform.get("genReport").equals("Y")) {
            	//导出excel
    			response = toExcelRecord(response,reportList,ElevatorNatureList,hmap);
    			forward = mapping.findForward("exportExcel");
    		}else{
                if(reportList.size()>0 && reportList.size()<3001){
                	request.setAttribute("maintenanceBytlReportList", reportList);
                }else if(reportList.size()>3000){
                	request.setAttribute("showinfostr", "查询数据超出三千行记录，请导出EXCEL查看。");
                }else{
                	request.setAttribute("showinfostr", "没有记录显示！");
                }
                request.setAttribute("totalhmap", hmap);
                
    			forward = mapping.findForward("bytlReportList");	
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
			List ReportList,List ElevatorNatureList,HashMap hmap) throws IOException {

		String[] titlename={"维保分部","维保站","维保合同号","销售合同号","电梯编号","电梯类型","层站","层站系数","扶梯类型","扶梯系数","载重","载重系数","是否发台量奖"};
		String[] titleid={"comname","storagename","maintcontractno","salescontractno","elevatorno","elevatortypename","floorstage","floorstagexs","seriesidname","seriesidxs","weight","weightxs","r4"};
		
		XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("保养台量奖报表");
        
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
		int enl=ElevatorNatureList.size();
		for(int e=0;e<enl;e++){
			Pulldown p=(Pulldown)ElevatorNatureList.get(e);//电梯性质
			cell0 = row0.createCell((short)ttl+e);
			//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell0.setCellValue(p.getPullname());
			cell0.setCellStyle(cs);
		}
		
		cell0 = row0.createCell((short)ttl+enl);
		//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell0.setCellValue("台量");
		cell0.setCellStyle(cs);
		cell0 = row0.createCell((short)ttl+enl+1);
		//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell0.setCellValue("台量系数");
		cell0.setCellStyle(cs);
		
		cell0 = row0.createCell((short)ttl+enl+2);
		//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell0.setCellValue("台量奖(元)");
		cell0.setCellStyle(cs);
		
		cell0 = row0.createCell((short)ttl+enl+3);
		//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell0.setCellValue("维保工身份证号");
		cell0.setCellStyle(cs);
		cell0 = row0.createCell((short)ttl+enl+4);
		//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell0.setCellValue("维保工");
		cell0.setCellStyle(cs);
		
		cell0 = row0.createCell((short)ttl+enl+5);
		//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell0.setCellValue("交接是否完成");
		cell0.setCellStyle(cs);
		/**
		cell0 = row0.createCell((short)ttl+enl+2);
		//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell0.setCellValue("下达日期");
		cell0.setCellStyle(cs);
		*/
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
				    cell.setCellValue((String)map.get(titleid[c]));
				}
				String elevatornature=(String)map.get("elevatornature");
				for(int e=0;e<enl;e++){
					Pulldown p=(Pulldown)ElevatorNatureList.get(e);//电梯性质
					String pullid=p.getId().getPullid();
					cell0 = row.createCell((short)ttl2+e);
					//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
					if(pullid.equals(elevatornature)){
						cell0.setCellValue((String)map.get("elevatornaturexs"));
					}else{
						cell0.setCellValue("");
					}
				}
				
				cell = row.createCell((short)ttl2+enl);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue((String)map.get("tlnum"));
				cell = row.createCell((short)ttl2+enl+1);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue((String)map.get("tlxs"));
				
				cell = row.createCell((short)ttl2+enl+2);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue((Double)map.get("nummoney"));
				cell = row.createCell((short)ttl2+enl+3);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue((String)map.get("idcardno"));
				
				cell = row.createCell((short)ttl2+enl+4);
				cell.setCellValue((String)map.get("maintpersonnelname"));
				
				cell = row.createCell((short)ttl2+enl+5);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue((String)map.get("transfercomplete"));
				/**
				cell = row.createCell((short)ttl2+enl+2);
				//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue((String)map.get("tasksubdate"));
				*/
				
			}
		}
		//参数：起始行号，终止行号， 起始列号，终止列号
		//sheet.addMergedRegion(new CellRangeAddress(toolnum, toolnum, 0, 8));
		XSSFRow row1 = sheet.createRow(toolnum);
		XSSFCell cell1 = row1.createCell((short)0);
		cell1.setCellValue("台量奖合计(元):"+(String)hmap.get("totalmoney"));
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("保养台量奖报表", "utf-8") + ".xlsx");
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
