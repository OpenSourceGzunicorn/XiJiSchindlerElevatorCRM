package com.gzunicorn.struts.action.projectbaoyangbaobiao;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
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
import org.apache.struts.actions.DispatchAction;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.logic.DataOperation;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SqlBeanUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.action.ServeTableForm;

public class ProjectBaoYangAction extends DispatchAction {
	/**
	 * 电梯保养当期收款查询报表
	 * 
	 * @author Administrator
	 * 
	 */
	Log log = LogFactory.getLog("ProjectBaoYangAction.class");

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
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchCondition";
		}
		ActionForward forward = dispatchMethod(mapping, form, request,
				response, name);
		return forward;
	}

	/**
	 * 显示查询条件内容
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toSearchCondition(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("navigator.location", "当期收款查询报表 >> 查询");
		
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

		request.setAttribute("maintDivisionList", Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
		
		return mapping.findForward("condition");
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

	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		ActionForward forward = null;
		ServeTableForm tableForm = (ServeTableForm) form;
        //HttpSession session = request.getSession();
		//ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		String genReport=tableForm.getProperty("genReport");//导出excel标志						
		String maintContractNo = tableForm.getProperty("maintContractNo"); // 维保合同号
		String startDate = tableForm.getProperty("startDate"); // 查询开始时期
		String endDate = tableForm.getProperty("endDate"); // 查询结束日期
		String companyName = tableForm.getProperty("companyName"); // 甲方单位
		String contractStatus = tableForm.getProperty("contractStatus"); // 续保标志
		String maintDivision = tableForm.getProperty("maintDivision"); //所属维保站
		
		maintContractNo = maintContractNo == null || "".equals(maintContractNo) ? "%" : "%"+maintContractNo.trim()+"%";
		startDate = startDate == null || "".equals(startDate) ? "0000-00-00" : startDate.trim();	
		endDate = endDate == null || "".equals(endDate) ? "9999-99-99" : endDate.trim();	
		companyName = companyName == null || "".equals(companyName) ? "%" : "%"+companyName.trim()+"%";
		contractStatus = contractStatus == null || "".equals(contractStatus) ? "%" : contractStatus.trim();
		maintDivision = maintDivision == null || "".equals(maintDivision) ? "%" : maintDivision.trim();

		List<Map<String,String>> reportList = null;
		float totalPrice = 0;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
        Session hs = null;
		try {
			hs = HibernateUtil.getSession();
			conn = hs.connection();
			cs = conn.prepareCall("{call SP_ENG_REFEE_NONCE_QUERY (?,?,?,?,?,?)}");
			cs.setString(1, maintContractNo);
			cs.setString(2, companyName);
			cs.setString(3, startDate);
			cs.setString(4, endDate);
			cs.setString(5, contractStatus);
			cs.setObject(6, maintDivision);
			cs.executeQuery();
			rs = cs.getResultSet();

			String[] colNames = SqlBeanUtil.getColumnNames(rs);
			
			reportList = new ArrayList<Map<String,String>>();
			HashMap<String,String> map = null;
			while(rs.next()){
				map = new HashMap<String,String>();
				for(int i = 0; i < colNames.length; i++){
					if(!"Y".equals(genReport) && colNames[i].contains("Money")){
						//map.put(colNames[i], CommonUtil.formatThousand(rs.getString(i+1),2));
					}else{
						map.put(colNames[i], rs.getString(i+1));						
					}					
				}					
				totalPrice += rs.getFloat("paragraphMoney");
				reportList.add(map);
			}

			request.setAttribute("reportList", reportList);// 查询结果列表
			request.setAttribute("totalPrice", String.valueOf(totalPrice));// 收款总额
			request.setAttribute("rowNums", reportList.size());// 查询结果总行数
			request.setAttribute("search", tableForm.getProperties());// 查询参数
		
			if ("Y".equals(genReport)) {
				response = toExcelRecord(reportList, request, response);				
			} else {
				forward = mapping.findForward("returnList");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				hs.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return forward;
	}		
	
	/**
	 * 导出查询数据到Excel
	 * @param list
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public HttpServletResponse toExcelRecord(List list,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
	
		XSSFWorkbook wb = new XSSFWorkbook();	
		XSSFSheet sheet = wb.createSheet();
		wb.setSheetName(0, "当期收款查询报表");
		int listLen = 0;
	
		if(list != null && list.size() > 0){
			
			listLen=list.size();

			//配置表头的共用单元格样式
			XSSFCellStyle cs = wb.createCellStyle();
			cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);//左右居中
			cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
			cs.setBorderTop(XSSFCellStyle.BORDER_THIN);
			cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cs.setBorderRight(XSSFCellStyle.BORDER_THIN);
			
			XSSFFont font  = wb.createFont();
			font.setFontHeightInPoints((short) 11);//字号
			font.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);//加粗
			cs.setFont(font);
				
			String headStr="维保合同号,甲方单位,收款金额,收款日期,应收款日期,续保标志,付款条件,所属分部";
			String keyStr="maintContractNo,companyName,paragraphMoney,paragraphDate,preDate,contractStatus,rem,maintDivisionName";
			String[] headNames = headStr.split(",");
			String[] keyNames = keyStr.split(",");

			/*输出第一行表头开始*/
			XSSFRow row = sheet.createRow( 0); // 创建第一行
			XSSFCell cell=null;
			for (int i = 0; i < headNames.length; i++) {
				cell = row.createCell((short) i);
				cell.setCellValue(headNames[i]);
				cell.setCellStyle(cs);
			}
							
			/*输出对应数据行*/
			for (int r = 0 ;  r < listLen; r++) {
				row = sheet.createRow( r+1);
				HashMap rowMap = (HashMap) list.get(r);	
				for (int c = 0; c < keyNames.length; c++) {
					
					cell = row.createCell((short) c);	
					cell.setCellStyle(cs);						
					Object cellValue = rowMap.get(keyNames[c]);
					
					if ("paragraphMoney".equals(keyNames[c])) {
						cell.setCellValue(Double.valueOf(cellValue+""));
					} else if ("contractStatus".equals(keyNames[c])){
						cell.setCellValue("ZB".equals(cellValue) ? "新签" : "续保");
					} else{
						cell.setCellValue(String.valueOf(cellValue));
					}			
					
				}
			}
			
			/*输出尾行*/
			String footStr="统计总记录数"+listLen+"条,收款金额总计为:"+  CommonUtil.formatThousand(request.getAttribute("totalPrice")+"",2)+"（元）";
			row = sheet.createRow( (sheet.getLastRowNum()+1)); // 创建最后一行
			cs.setAlignment(XSSFCellStyle.ALIGN_LEFT);			
			for (int i = 0; i < keyNames.length; i++) {
				row.createCell(i).setCellStyle(cs);
			}
			row.getCell(0).setCellValue(footStr);
			sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(),row.getRowNum(),0,row.getLastCellNum()-1));//跨列

		}			
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="
				+ URLEncoder.encode("当期收款查询报表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());	
		return response;	
		
	}
		
				
}