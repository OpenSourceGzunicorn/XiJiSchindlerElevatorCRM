package com.gzunicorn.struts.action.report.contractreceivables;


import java.io.IOException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

/**
 * 合同收款管理=>平账合同报表
 * @author Lijun
 *
 */
public class FlatBillContractReportAction extends DispatchAction {

	Log log = LogFactory.getLog(FlatBillContractReportAction.class);

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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "flatbillcontractreport", null);
		/** **********结束用户权限过滤*********** */

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

		request.setAttribute("navigator.location", " 平账合同报表 >> 查询");
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
			dform.set("sdate1", day1);
			dform.set("edate1", day);
			
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

		forward = mapping.findForward("fbcReportSearch");		
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

		request.setAttribute("navigator.location", " 平账合同报表 >> 查询结果");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;

		String maintdivision = request.getParameter("maintdivision");
		String maintstation = request.getParameter("maintstation");
		String contractno = request.getParameter("contractno");
		if(contractno==null || contractno.trim().equals("")){
			contractno="%";
		}
		String companyid = request.getParameter("companyid");
		if(companyid==null || companyid.trim().equals("")){
			companyid="%";
		}
		String sdate1 = request.getParameter("sdate1");
		if(sdate1==null || sdate1.trim().equals("")){
			sdate1="0000-00-00";
		}
		String edate1 = request.getParameter("edate1");
		if(edate1==null || edate1.trim().equals("")){
			edate1="9999-99-99";
		}

		HashMap rmap= new HashMap();			
		rmap.put("maintdivision", maintdivision);
		rmap.put("maintstation", maintstation);
		rmap.put("contractno", contractno);
		rmap.put("companyid", companyid);
		rmap.put("sdate1", sdate1);
		rmap.put("edate1", edate1);
		request.setAttribute("rmap", rmap);
		
		Session hs = null;
		ResultSet rs=null;
		try {
			hs = HibernateUtil.getSession();
			
			String hql="exec sp_flatBillContractReport '"+maintdivision.trim()+"','"+maintstation.trim()+"','"
					+contractno.trim()+"','"+companyid.trim()+"','"+sdate1.trim()+"','"+edate1.trim()+"'";
			//System.out.println("平账合同报表>>>>"+hql);
			rs=hs.connection().prepareCall(hql).executeQuery();

            List reportList=new ArrayList();
            int totalnum=0;
            while(rs.next()){
            	HashMap map= new HashMap();

            	map.put("ParagraphDate", rs.getString("ParagraphDate"));//平账日期(最后一次来款日期)
            	map.put("MaintDivision", rs.getString("MaintDivision"));//所属分部门代码
            	map.put("ComName", rs.getString("ComName"));//所属分部门
            	map.put("MaintStation", rs.getString("MaintStation"));//所属维保站代码
            	map.put("StorageName", rs.getString("StorageName"));//所属维保站
            	map.put("ContractNo", rs.getString("ContractNo"));//合同号
            	map.put("num", rs.getString("num"));//合同台量
            	map.put("ContractSDate", rs.getString("ContractSDate"));//合同开始日期
            	map.put("ContractEDate", rs.getString("ContractEDate"));//合同结束日期
            	map.put("CompanyName", rs.getString("CompanyName"));//甲方单位
            	map.put("BillNo", rs.getString("BillNo"));//合同流水号
            	map.put("BusType", rs.getString("BusType"));//合同类型
            	map.put("ContractTotal", rs.getString("ContractTotal"));//合同总额
            	map.put("ParagraphMoney", rs.getString("ParagraphMoney"));//合同来款
            	map.put("ContractTotal2", rs.getString("ContractTotal2"));//合同欠款

            	reportList.add(map);
            }
            
            HashMap hmap=new HashMap();
            hmap.put("totalnum", df2.format(totalnum));
            
            if (dform.get("genReport") != null && dform.get("genReport").equals("Y")) {
            	//导出excel
    			response = toExcelRecord(response,reportList,hmap);
    			forward = mapping.findForward("exportExcel");
    		}else{
                if(reportList.size()>0 && reportList.size()<3001){
                	request.setAttribute("maintenancefbcReportList", reportList);
                }else if(reportList.size()>3000){
                	request.setAttribute("showinfostr", "查询数据超出三千行记录，请导出EXCEL查看。");
                }else{
                	request.setAttribute("showinfostr", "没有记录显示！");
                }
                request.setAttribute("totalhmap", hmap);
                
    			forward = mapping.findForward("fbcReportList");	
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

		String[] titlename={"平账日期","维保分部","维保站","维保合同号","台量","甲方单位","合同开始日期","合同结束日期","合同欠款"};
		String[] titleid={"ParagraphDate","ComName","StorageName","ContractNo","num","CompanyName","ContractSDate","ContractEDate","ContractTotal2"};
		
		XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("平账合同报表");
        
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
		for(int i=0;i<titlename.length;i++){
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
				for(int c=0;c<titleid.length;c++){
				    cell = row.createCell((short)c);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				    if(titleid[c].equals("num")){
				    	cell.setCellValue(Integer.parseInt((String)map.get(titleid[c])));
				    }else{
				    	cell.setCellValue((String)map.get(titleid[c]));
				    }
				}
			}
		}
		//参数：起始行号，终止行号， 起始列号，终止列号
		//sheet.addMergedRegion(new CellRangeAddress(toolnum, toolnum, 0, 8));
		//XSSFRow row1 = sheet.createRow(toolnum);
		//XSSFCell cell1 = row1.createCell((short)0);
		//cell1.setCellValue("退保总台量:"+(String)hmap.get("totalnum"));
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("平账合同报表", "utf-8") + ".xlsx");
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
