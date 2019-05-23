package com.gzunicorn.struts.action.report.contractpaymentmanagereport;


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
 * 合同付款报表=>委托单位付款审核评价表
 * @author Lwj
 *
 */
public class CompanyPaymentReportAction extends DispatchAction {

	Log log = LogFactory.getLog(CompanyPaymentReportAction.class);

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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "companyPaymentReport", null);
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

		request.setAttribute("navigator.location", " 委托单位付款审核评价表 >> 查询");
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
			
			String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
			String day1=DateUtil.getDate(day, "yyyy", -1);//当前日期年份加1 。
			dform.set("paragraphdate1", day1);
			dform.set("paragraphdate2", day);
			
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

		forward = mapping.findForward("companyPaymentReportSearch");		
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

		request.setAttribute("navigator.location", " 委托单位付款审核评价表 >> 查询结果");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;

		String maintdivision = request.getParameter("maintdivision");
		String contractno = request.getParameter("contractno");
		if(contractno==null || contractno.trim().equals("")){
			contractno="";
		}
		String companyname = request.getParameter("companyname");
		if(companyname==null || companyname.trim().equals("")){
			companyname="";
		}
		String paragraphdate1 = request.getParameter("paragraphdate1");
		if(paragraphdate1==null || paragraphdate1.trim().equals("")){
			paragraphdate1="0000-00-00";
		}
		String paragraphdate2 = request.getParameter("paragraphdate2");
		if(paragraphdate2==null || paragraphdate2.trim().equals("")){
			paragraphdate2="9999-99-99";
		}

		HashMap rmap= new HashMap();			
		rmap.put("maintdivision", maintdivision);
		rmap.put("contractno", contractno);
		rmap.put("companyname", companyname);
		rmap.put("paragraphdate1", paragraphdate1);
		rmap.put("paragraphdate2", paragraphdate2);
		request.setAttribute("rmap", rmap);
		
		Session hs = null;
		ResultSet rs=null;
		try {
			hs = HibernateUtil.getSession();
			
			String hql="exec sp_companyPaymentReport '"+maintdivision+"','"+contractno+"','" 
				+companyname+"','"+paragraphdate1+"','"+paragraphdate2+"'";
			//System.out.println("平账合同报表>>>>"+hql);
			rs=hs.connection().prepareCall(hql).executeQuery();

            List reportList=new ArrayList();
            int totalnum=0;
            while(rs.next()){
            	HashMap map= new HashMap();

            	map.put("MaintDivision", rs.getString("MaintDivision"));//所属部门
            	map.put("EntrustContractNo", rs.getString("EntrustContractNo"));//委托合同号
            	map.put("CompanyName", rs.getString("CompanyName"));//委托单位名称
            	map.put("elenum", rs.getString("elenum"));//委托台量
            	map.put("ParagraphNo", rs.getString("ParagraphNo"));//凭证号
            	map.put("ParagraphMoney", rs.getString("ParagraphMoney"));//付款金额
            	map.put("DebitMoney", rs.getString("DebitMoney"));//扣款金额
            	map.put("ParagraphDate", rs.getString("ParagraphDate"));//付款日期
            	map.put("BydAuditEvaluate", rs.getString("BydAuditEvaluate"));//保养单审核人评价
            	map.put("HfAuditNum", rs.getString("HfAuditNum"));//因技术问题引起的负面意见次数
            	map.put("HfAuditNum2", rs.getString("HfAuditNum2"));//因非技术问题引起的负面意见次数
            	map.put("RxAuditNum", rs.getString("RxAuditNum"));//因技术问题引起的投诉次数
            	map.put("RxAuditNum2", rs.getString("RxAuditNum2"));//因非技术问题引起的投诉次数
            	map.put("JjthAuditEvaluate", rs.getString("JjthAuditEvaluate"));//旧件退回审核评价
            	map.put("FbzAuditEvaluate", rs.getString("FbzAuditEvaluate"));//分部长审核评价
            	map.put("ZbzAuditRem", rs.getString("ZbzAuditRem"));//总部长审核意见
            	map.put("CustLevel", rs.getString("CustLevel"));//维保资质级别

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
                	request.setAttribute("companyPaymentReportList", reportList);
                }else if(reportList.size()>3000){
                	request.setAttribute("showinfostr", "查询数据超出三千行记录，请导出EXCEL查看。");
                }else{
                	request.setAttribute("showinfostr", "没有记录显示！");
                }
                request.setAttribute("totalhmap", hmap);
                
    			forward = mapping.findForward("companyPaymentReportList");	
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

		String[] titlename={"所属部门","委托合同号","委托单位名称","委托台量","凭证号","付款金额","扣款金额","付款日期","保养单审核人评价",
				"因技术问题引起的负面意见次数","因非技术问题引起的负面意见次数","因技术问题引起的投诉次数","因非技术问题引起的投诉次数","旧件退回审核评价",
				"分部长审核评价","总部长审核意见","维保资质级别"};
		String[] titleid={"MaintDivision","EntrustContractNo","CompanyName","elenum","ParagraphNo","ParagraphMoney",
				"DebitMoney","ParagraphDate","BydAuditEvaluate","HfAuditNum","HfAuditNum2","RxAuditNum",
				"RxAuditNum2","JjthAuditEvaluate","FbzAuditEvaluate","ZbzAuditRem","CustLevel"};
		
		XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("委托单位付款审核评价表");
        
        //创建单元格样式
        XSSFCellStyle cs = wb.createCellStyle();
        cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);//左右居中
        cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
        XSSFFont f  = wb.createFont();
        f.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 字体加粗
        cs.setFont(f);
        
        int toolnum=2;
        //创建标题
        XSSFRow row0 = sheet.createRow(0);
        XSSFCell cell0 = null;
        int k = 0;
        int l = 0;
		for(int i=0;i<titlename.length;i++){
			if ("因技术问题引起的负面意见次数".equals(titlename[i]) || "因非技术问题引起的负面意见次数".equals(titlename[i])) {
				if (k==0) {
					k = i;
					cell0 = row0.createCell((short)(i));
					cell0.setCellValue("例行回访结果审核");
					sheet.addMergedRegion(new CellRangeAddress(0, 0, k, k+1));
					cell0.setCellStyle(cs);
				}
				row0 = sheet.getRow(1);
				if (row0 == null) {
					row0 = sheet.createRow(1);
				}
				cell0 = row0.createCell((short)(i));
				cell0.setCellValue(titlename[i]);
				cell0.setCellStyle(cs);
				
			}else if ("因技术问题引起的投诉次数".equals(titlename[i]) || "因非技术问题引起的投诉次数".equals(titlename[i])) {
				if (l==0) {
					l = i;
					row0 = sheet.getRow(0);
					cell0 = row0.createCell((short)(i));
					cell0.setCellValue("热线管理人");
					sheet.addMergedRegion(new CellRangeAddress(0, 0, l, l+1));
					cell0.setCellStyle(cs);
				}
				row0 = sheet.getRow(1);
				if (row0 == null) {
					row0 = sheet.createRow(1);
				}
				cell0 = row0.createCell((short)(i));
				cell0.setCellValue(titlename[i]);
				cell0.setCellStyle(cs);
				
			}else {
				row0 = sheet.getRow(0);
				cell0 = row0.createCell((short)(i));
				//cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(titlename[i]);
				sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
				cell0.setCellStyle(cs);
			}
		}
		//创建内容
		if (ReportList != null && ReportList.size()>0) {
			XSSFRow row = null;
			XSSFCell cell =null;
			toolnum=toolnum+ReportList.size();
			for (int j = 0; j < ReportList.size(); j++) {
				HashMap map=(HashMap) ReportList.get(j);
				// 创建Excel行，从0行开始
				row = sheet.createRow(j+2);
				for(int c=0;c<titleid.length;c++){
				    cell = row.createCell((short)c);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				    if(titleid[c].equals("elenum") 
				    		|| titleid[c].equals("HfAuditNum") || titleid[c].equals("HfAuditNum2")
				    		|| titleid[c].equals("RxAuditNum") || titleid[c].equals("RxAuditNum2")){
				    	cell.setCellValue(Integer.parseInt((String)map.get(titleid[c])));
				    }else if (titleid[c].equals("ParagraphMoney") || titleid[c].equals("DebitMoney")) {
				    	cell.setCellValue(Double.parseDouble((String)map.get(titleid[c])));
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
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("委托单位付款审核评价表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	
}
