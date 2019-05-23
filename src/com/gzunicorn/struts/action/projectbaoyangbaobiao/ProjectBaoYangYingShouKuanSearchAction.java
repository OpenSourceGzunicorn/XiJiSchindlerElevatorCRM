package com.gzunicorn.struts.action.projectbaoyangbaobiao;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
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

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.logic.DataOperation;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SqlBeanUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.action.ServeTableForm;

public class ProjectBaoYangYingShouKuanSearchAction extends DispatchAction {
	/**
	 * 电梯保养收款查询报表
	 * 
	 * @author Administrator
	 * 
	 */
		Log log = LogFactory.getLog("ProjectBaoYangYingShouKuanSearchAction.class");

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
			request.setAttribute("navigator.location", "收款查询报表 >> 查询");
			
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
			String maintDivision = tableForm.getProperty("maintDivision"); //所属维保站
			
			maintContractNo = maintContractNo == null || "".equals(maintContractNo) ? "%" : "%"+maintContractNo.trim()+"%";
			startDate = startDate == null || "".equals(startDate) ? "0000-00-00" : startDate.trim();	
			endDate = endDate == null || "".equals(endDate) ? "9999-99-99" : endDate.trim();	
			companyName = companyName == null || "".equals(companyName) ? "%" : "%"+companyName.trim()+"%";
			maintDivision = maintDivision == null || "".equals(maintDivision) ? "%" : maintDivision.trim();
		
			List<Map<String,String>> reportList = null;
			Double premoney=0.0;//应收金额
			Double nowfee=0.0;//已开票金额
			Double nonowfee=0.0;//未开票金额
			Double billatm=0.0;//已开票已收款金额
			Double billnoatm=0.0;//已开票到期欠款金额
			Double nobillatm=0.0;//未开票到期欠款金额
			Double nobillnoatm=0.0;//已开票非欠款金额 
		
			Connection conn = null;
			CallableStatement cs = null;
			ResultSet rs = null;
		    Session hs = null;
		    HashMap map = null;
			try {
				hs = HibernateUtil.getSession();
				conn = hs.connection();
				cs = conn.prepareCall("{call SP_ENG_ARREBLANCFEE_QUERY (?,?,?,?,?)}");
				cs.setString(1, maintContractNo);
				cs.setString(2, companyName);				
				cs.setString(3, startDate);
				cs.setString(4, endDate);
				cs.setObject(5, maintDivision);
				cs.executeQuery();
				rs = cs.getResultSet();					
				reportList = new ArrayList<Map<String,String>>();
		
				while(rs.next()){
					map = new HashMap();
					map.put("contractid", rs.getString("contractid"));
					map.put("custname", rs.getString("custname"));
					map.put("predate", rs.getString("predate"));
					map.put("premoney", rs.getString("premoney"));
					map.put("nowfee", rs.getString("nowfee"));
					map.put("nonowfee", rs.getString("nonowfee"));
					map.put("billatm", rs.getString("billatm"));
					map.put("billnoatm", rs.getString("billnoatm"));
					map.put("nobillatm", rs.getString("nobillatm"));
					map.put("nobillnoatm", rs.getString("nobillnoatm"));
					map.put("grcname", rs.getString("grcname"));
					
					premoney+=Double.valueOf(rs.getString("premoney")).doubleValue();
					nowfee+=Double.valueOf(rs.getString("nowfee")).doubleValue();
					nonowfee+=Double.valueOf(rs.getString("nonowfee")).doubleValue();
					billatm+=Double.valueOf(rs.getString("billatm")).doubleValue();
					billnoatm+=Double.valueOf(rs.getString("billnoatm")).doubleValue();
					nobillatm+=Double.valueOf(rs.getString("nobillatm")).doubleValue();
					nobillnoatm+=Double.valueOf(rs.getString("nobillnoatm")).doubleValue();
					reportList.add(map);
				}
				DecimalFormat df=new DecimalFormat("#,##0.00");
				String sj="统计：记录数"+reportList.size()+"条, "
						+ "应收款金额总计为："+df.format(premoney)+"（元）,"
						+ "已开票金额总计为："+df.format(nowfee)+"（元), "
						+ "未开票金额总计为："+df.format(nonowfee)+"（元）, "
						+ "已开票已收款金额总计为："+df.format(billatm)+"（元）, "
						+ "已开票到期欠款金额总计为："+df.format(billnoatm)+"（元）, "
				        + "未开票到期欠款金额总计为："+df.format(nobillatm)+"（元）, "
				        + "已开票非欠款金额总计为："+df.format(nobillnoatm)+"（元）. ";
				request.setAttribute("sj", sj);
				request.setAttribute("reportList", reportList);// 查询结果列表
				request.setAttribute("rowNums", reportList.size());// 查询结果总行数
				request.setAttribute("search", tableForm.getProperties());// 查询参数
				
				
				if ("Y".equals(genReport)) {
					response = toExcelRecord(reportList,sj, request, response);				
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
		 * @param resultList
		 * @param request
		 * @param response
		 * @return
		 * @throws IOException
		 */
		public HttpServletResponse toExcelRecord(List resultList,String sj,
				HttpServletRequest request, HttpServletResponse response) throws IOException {
			
			XSSFWorkbook wb = new XSSFWorkbook();		
			//配置表头的共用单元格样式
			XSSFCellStyle cs = wb.createCellStyle();
			cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);//左右居中
			cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
			XSSFFont f  = wb.createFont();
			f.setFontHeightInPoints((short) 11);//字号
			f.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);//加粗
			cs.setFont(f);
			cs.setBorderTop(XSSFCellStyle.BORDER_THIN);//设置上边框显示
			cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);//设置下边框显示
			cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);//设置左边框显示
			cs.setBorderRight(XSSFCellStyle.BORDER_THIN);//设置右边框显示

			//配置表单内容的单元格样式
			XSSFCellStyle cc = wb.createCellStyle();
			int rowlistLen=resultList.size();
			try{
				String headstr="序号,合同号,甲方单位,应收日期,应收金额," +
						"已开票金额,未开票金额,已开票已收款金额,已开票到期欠款金额,未开票到期欠款金额,已开票非欠款金额,所属维保分部";
				String key1str="xuhao,contractid,custname,predate,premoney," +
						"nowfee,nonowfee,billatm,billnoatm,nobillatm,nobillnoatm,grcname";		
				String[] headName = headstr.split(",");
				int headNameLen = headName.length;//表头个数		
				int rowno=0;
				XSSFSheet sheet = wb.createSheet();	
				wb.setSheetName(0,"保养收款查询表");
				/*输出第一行表头开始*/
				XSSFRow row0 = sheet.createRow( rowno); // 创建第一行
				XSSFCell cell0=null;
				for (int i = 0; i < headNameLen; i++) {				
					cell0 = row0.createCell((short) i);
					cell0.setCellValue(headName[i]);
					cell0.setCellStyle(cs);					
				}
				rowno++;
				String[] key1Name = key1str.split(",");
				int key1NameLen = key1Name.length;
				/*输出对应表头的数值*/
				HashMap rowMap=null;
				int rownlineNo=0;
				if(rowlistLen>0){				
					for(int i=0;i<resultList.size();i++,rowno++){
						HashMap hm=(HashMap)resultList.get(i);
						  row0=sheet.createRow(rowno);
						for(int j=0;j<key1NameLen;j++){
							cell0=row0.createCell((short)j);
							if(j==0){
								cell0.setCellValue(i+1);
							}else{			
								String value=String.valueOf(hm.get(key1Name[j]));
								cell0.setCellValue(value);							
							}
							cell0.setCellStyle(cs);
						}
					}	
				}
				//输出尾部
				XSSFCellStyle cellstyle=wb.createCellStyle();
				cellstyle.setWrapText(true);
				sheet.addMergedRegion(new CellRangeAddress(rowno,rowno+4,0,headNameLen-1));
				row0=sheet.createRow(rowno);
				cell0=row0.createCell(0);
				cell0.setCellValue(sj);
				cell0.setCellStyle(cellstyle);
		}catch(Exception e){
			e.printStackTrace();
		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="
				+ URLEncoder.encode("收款查询报表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());			
		return response;	
		}
		
		
		
	}