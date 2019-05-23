package com.gzunicorn.struts.action.projectbaoyangbaobiao;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.poi.hssf.util.CellRangeAddress;
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
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.action.ServeTableForm;



public class BaoYangQingKuangTotalAction extends DispatchAction {
	/**
	 * 电梯保养情况汇总表
	 * 
	 * @author Administrator
	 * 
	 */
		Log log = LogFactory.getLog("BaoYangQingKuangTotalAction.class");

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
			request.setAttribute("navigator.location", "电梯保养情况汇总表 >> 查询");
			HttpSession session=request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			ServeTableForm tableForm = (ServeTableForm) form;
			List mugStorages = null;
			Session hs = null;
			List grcidlist=new ArrayList();
			//System.out.println(request.getParameter("ContentPath"));
			try {
				hs = HibernateUtil.getSession();
				mugStorages=Grcnamelist1.getStorageName(hs,userInfo.getUserID());//保养站
				grcidlist=Grcnamelist1.getgrcnamelist(hs,userInfo.getUserID());
			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException e) {
					e.printStackTrace();
				}
			}
			
			Date date = new Date();
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			String str = f.format(date);
			String selectyear = str.substring(0,4);
			String selectmonth = str.substring(5,7);
			tableForm.setProperty("selectyear",selectyear);
			tableForm.setProperty("selectmonth",selectmonth);
	
			int year = 2011;
			List yearlist = new ArrayList();
			List monthlist = new ArrayList();
			////System.out.println(year);
			Map yearmap = null;
			 for (int i = year; i <year+20; i++) {
				 yearmap = new HashMap();
				 yearmap.put("year", i);
				 yearlist.add(yearmap);			
			}
			 for (int i = 1; i<13; i++) {
				 Map monthmap = new HashMap();
				 if (i<10) {
					 monthmap.put("month", "0"+String.valueOf(i));
					 if (monthmap.containsValue("0"+new SimpleDateFormat("MM").format(new Date()))) {
						 monthmap.remove("month");
					}else{
						monthlist.add(monthmap);
					}
				}else{
				 monthmap.put("month", String.valueOf(i));
				 if (monthmap.containsValue(new SimpleDateFormat("MM").format(new Date()))) {
					 monthmap.remove("month");
				}else{
					monthlist.add(monthmap);
				}
				}				 
				}			
			request.setAttribute("grcidlist",grcidlist);
			request.setAttribute("yearlist", yearlist);
			request.setAttribute("monthlist", monthlist);
			request.setAttribute("mugStorages", mugStorages);
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
			ServeTableForm tableForm = (ServeTableForm) form;
            HttpSession session = request.getSession();
			// 保存是不是从查询后点击导出excel,如果是的话,flag中的值应该为Y
			String flag = tableForm.getProperty("genReport");			
			// 查询日期的年份
			String selectyear = tableForm.getProperty("selectyear");
			if (selectyear == null || "".equals(selectyear)) {
				selectyear = "0000";
			} else {
				selectyear = selectyear.trim();
			}  
			session.setAttribute("selectyear", selectyear);
			// 查询日期的月份
			String selectmonth = tableForm.getProperty("selectmonth");
			if (selectmonth == null || "".equals(selectmonth)) {
				selectmonth = "00";
			} else {
				selectmonth = selectmonth.trim();
			} 
			session.setAttribute("selectmonth", selectmonth);
			String selectdate = selectyear+"-"+selectmonth;
			// 保养站内容。
			String storageid = tableForm.getProperty("mugStorages");
			if (storageid == null || "".equals(storageid)) {
				storageid = "%";
			} else {
				storageid = storageid.trim();
			}
			//所属维保分部
			String genReport=tableForm.getProperty("genReport");
			String grcid22=tableForm.getProperty("grcid22");
			String grcid = tableForm.getProperty("grcid");
			if (grcid == null || "".equals(grcid)) {
				if(genReport!= null && (genReport.equals("Y") || "Y".equals(flag))){
					grcid=grcid22;
				}else{
				grcid = "";
				}
			} else {
				grcid = grcid.trim();
			}
			session.setAttribute("mugStorages", storageid);
			List tempList = new ArrayList();
			float _price = 0;
			float _countprice = 0;
			Connection conn = null;
			String sql = null;
			HashMap map = new HashMap();
			if ("0000".equals(selectyear)) {
				map.put("selectyear", "");
				map.put("currentyear", "N");
			}else {
				map.put("currentyear", "Y");
				map.put("selectyear", selectyear);
			}
			if ("00".equals(selectmonth)) {
				map.put("selectmonth", "");	
				map.put("currentmonth", "N");
			}else {
				map.put("currentmonth", "Y");
				map.put("selectmonth", selectmonth);
			}
			try {
				conn = HibernateUtil.getSession().connection();
				sql = "exec SP_MAINMUGCONTRACT_STATUS_QUERY '"+ storageid + "','"+selectdate+"','"+grcid+"'";
				//System.out.println(sql);				
				PreparedStatement pst=null;
				ResultSet rs=null;
				pst=conn.prepareStatement(sql);
				rs=pst.executeQuery();
				int totalsignnum = 0;
				double totalsignPrice =0;
				int totalbackNum =0;
				double totalbackPrice = 0;
				int totalcontinueNum =0;
				double totalcontinuePrice = 0;
				int totalzbfNum =0;
				double totalzbPrice = 0;
				int totalfbNum =0;
				double totalfbPrice = 0;
				int totalsbNum =0;
				double totalsbPrice = 0;
				int totalxjNum =0;
				double totalxjnPrice = 0;
				while(rs.next()){
					Map rs_map = new HashMap();
					String nodename =  rs.getString("nodename");
					rs_map.put("nodename", nodename);
					String grcname =  rs.getString("grcname");
					rs_map.put("grcname", grcname);
					int signnum =rs.getInt("signnum");
					rs_map.put("signnum", signnum);
					double signPrice = rs.getInt("signPrice");
					rs_map.put("signPrice", CommonUtil.formatThousand(String.valueOf(signPrice), 4));
					int backNum =rs.getInt("backNum");
					rs_map.put("backNum", backNum);
					double backPrice = rs.getInt("backPrice");
					rs_map.put("backPrice", CommonUtil.formatThousand(String.valueOf(backPrice), 4));
					int continueNum =rs.getInt("continueNum");
					rs_map.put("continueNum", continueNum);
					double continuePrice = rs.getInt("continuePrice");
					rs_map.put("continuePrice", CommonUtil.formatThousand(String.valueOf(continuePrice), 4));
					int zbfNum =rs.getInt("zbfNum");
					rs_map.put("zbfNum", zbfNum);
					double zbPrice = rs.getInt("zbPrice");
					rs_map.put("zbPrice", CommonUtil.formatThousand(String.valueOf(zbPrice), 4));
					int fbNum =rs.getInt("fbNum");
					rs_map.put("fbNum", fbNum);
					double fbPrice = rs.getInt("fbPrice");
					rs_map.put("fbPrice", CommonUtil.formatThousand(String.valueOf(fbPrice), 4));
					int sbNum =rs.getInt("sbNum");
					rs_map.put("sbNum", sbNum);
					double sbPrice = rs.getInt("sbPrice");
					rs_map.put("sbPrice", CommonUtil.formatThousand(String.valueOf(sbPrice), 4));
					int xjNum =rs.getInt("xjNum");
					rs_map.put("xjNum", xjNum);
					double xjnPrice = rs.getInt("xjnPrice");
					rs_map.put("xjnPrice", CommonUtil.formatThousand(String.valueOf(xjnPrice), 4));
					tempList.add(rs_map);
					totalsignnum+=signnum;
					totalsignPrice+=signPrice;
					totalbackNum += backNum;
					totalbackPrice+= backPrice;
					totalcontinueNum+= continueNum;
					totalcontinuePrice+=continuePrice;
					totalzbfNum+=zbfNum;
					totalzbPrice+=zbPrice;
					totalfbNum+=fbNum;
					totalfbPrice+=fbPrice;
					totalsbNum+=sbNum;
					totalsbPrice+=sbPrice;
					totalxjNum+=xjNum;
					totalxjnPrice+=xjnPrice;					
				}
				request.setAttribute("totalxjnPrice", CommonUtil.formatThousand(String.valueOf(totalxjnPrice), 4));
				request.setAttribute("totalxjNum", totalxjNum);
				request.setAttribute("totalsbPrice", CommonUtil.formatThousand(String.valueOf(totalsbPrice), 4));
				request.setAttribute("totalsbNum", totalsbNum);
				request.setAttribute("totalfbPrice", CommonUtil.formatThousand(String.valueOf(totalfbPrice), 4));
				request.setAttribute("totalfbNum", totalfbNum);
				request.setAttribute("totalzbPrice", CommonUtil.formatThousand(String.valueOf(totalzbPrice), 4));
				request.setAttribute("totalzbfNum", totalzbfNum);
				request.setAttribute("totalcontinuePrice", CommonUtil.formatThousand(String.valueOf(totalcontinuePrice), 4));
				request.setAttribute("totalcontinueNum", totalcontinueNum);
				request.setAttribute("totalbackPrice", CommonUtil.formatThousand(String.valueOf(totalbackPrice), 4));
				request.setAttribute("totalbackNum", totalbackNum);
				request.setAttribute("totalsignPrice", CommonUtil.formatThousand(String.valueOf(totalsignPrice), 4));
				request.setAttribute("totalsignnum", totalsignnum);
				request.setAttribute("dateinfo", map);
				request.setAttribute("price", String.valueOf(_price));
				request.setAttribute("countprice", String.valueOf(_countprice));
				request.setAttribute("report_list", tempList);
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}

			ActionForward forward = null;
			request.setAttribute("grcid22", grcid);
			session.setAttribute("totalprice", String.valueOf(_countprice));
			if (!"Y".equals(flag)) {
				session.setAttribute("result", tempList);
			}

			if (tableForm.getProperty("genReport") != null
					&& tableForm.getProperty("genReport").equals("Y")
					|| "Y".equals(flag)) {
				try {
					response = toExcelRecord(tempList, request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				forward = mapping.findForward("returnList");
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
		public HttpServletResponse toExcelRecord(List resultList,
				HttpServletRequest request, HttpServletResponse response) throws IOException {			
		XSSFWorkbook wb = new XSSFWorkbook();
		//配置表头的共用单元格样式
		XSSFCellStyle cs = wb.createCellStyle();
		cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);//左右居左
		cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
		cs.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(XSSFCellStyle.BORDER_THIN);
		XSSFCellStyle cc = wb.createCellStyle();
		cc.setAlignment(XSSFCellStyle.ALIGN_RIGHT);//左右居中
		cc.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
		XSSFFont f  = wb.createFont();
		f.setFontHeightInPoints((short) 12);//字号
		f.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);//加粗
		//cs.setFont(f);
		int rowlistLen=resultList.size();		
		try{
			String headstr="站别,所属维保分部,新签台数,新签金额,退保台数,退保金额,续保台数,续保金额,自保收费台数,自保收费金额,委托发包台数,委托发包金额,自保免费台数,自保免费金额,台数小计,金额小计";
			String key1str="nodename,grcname,signnum,signPrice,backNum,backPrice,continueNum,continuePrice,zbfNum,zbPrice,fbNum,fbPrice,sbNum,sbPrice,xjNum,xjnPrice";			
			String[] headName = headstr.split(",");
			int headNameLen = headName.length;//表头个数	
			int row0lineNo=0;
			int rowno=0;
			XSSFSheet sheet = wb.createSheet();
			wb.setSheetName(0, "保养情况汇总表报表");	
			int row00 = sheet.addMergedRegion(new CellRangeAddress(0,  1, 0,  1));
			int row01 = sheet.addMergedRegion(new CellRangeAddress(0,  0, 2,  7));			
			int row02 = sheet.addMergedRegion(new CellRangeAddress(0, 0, 8,  15));
			XSSFRow row0 = sheet.createRow( rowno); // 创建表头
			    XSSFCell cell0=null;
				XSSFCell cell00=null;
				XSSFCell cell01=null;
				XSSFCell cell02=null;				
				cell00 = row0.createCell(0);
				cell00.setCellValue("项目");
				cell00.setCellStyle(cs);
				cell01 = row0.createCell(2);
				cell01.setCellValue("本月合同情况");				
				cell01.setCellStyle(cs);	
				cell02 = row0.createCell(8);
				cell02.setCellValue("本月保养电梯情况");				
				cell02.setCellStyle(cs);
				cell02 = row0.createCell(15);
				cell02.setCellStyle(cs);
			rowno+=1;
			row0 = sheet.createRow( rowno); // 创建表头
			int row03 = sheet.addMergedRegion(new CellRangeAddress(1, (short) 1, 2, (short) 3));
			int row04 = sheet.addMergedRegion(new CellRangeAddress(1, (short) 1, 4, (short) 5));
			int row05 = sheet.addMergedRegion(new CellRangeAddress(1, (short) 1, 6, (short) 7));
			int row06 = sheet.addMergedRegion(new CellRangeAddress(1, (short) 1, 8, (short) 9));
			int row07 = sheet.addMergedRegion(new CellRangeAddress(1, (short) 1, 10, (short) 11));
			int row08 = sheet.addMergedRegion(new CellRangeAddress(1, (short) 1, 12, (short) 13));
			int row09 = sheet.addMergedRegion(new CellRangeAddress(1, (short) 1, 14, (short) 15));
			for (int i = 0; i < 1; i++) {
				XSSFCell cell03=null;
				XSSFCell cell04=null;
				XSSFCell cell05=null;
				XSSFCell cell06=null;
				XSSFCell cell07=null;
				XSSFCell cell08=null;
				XSSFCell cell09=null;
				XSSFCell cell031=null;
				XSSFCell cell041=null;
				XSSFCell cell051=null;
				XSSFCell cell061=null;
				XSSFCell cell071=null;
				XSSFCell cell081=null;
				XSSFCell cell091=null;
				cell031 = row0.createCell(0);
				cell031.setCellStyle(cs);
				cell031 = row0.createCell(1);
				cell031.setCellStyle(cs);
				cell03 = row0.createCell(2);
				cell03.setCellValue("新签定保养合同");
				cell03.setCellStyle(cs);
				cell031 = row0.createCell(3);
				cell031.setCellStyle(cs);
				cell04 = row0.createCell(4);
				cell04.setCellValue("退保养合同");				
			    cell04.setCellStyle(cs);
			    cell041 = row0.createCell(5);				
				cell041.setCellStyle(cs);
				cell05 = row0.createCell(6);
				cell05.setCellValue("续签定保养合同");				
				cell05.setCellStyle(cs);
				cell051 = row0.createCell(7);				
				cell051.setCellStyle(cs);
				cell06 = row0.createCell(8);
				cell06.setCellValue("自保收费电梯");
				cell06.setCellStyle(cs);
				cell061 = row0.createCell(9);				
				cell061.setCellStyle(cs);
				cell07 = row0.createCell(10);
				cell07.setCellValue("委托发包电梯");				
			    cell07.setCellStyle(cs);
			    cell071 = row0.createCell(11);				
				cell071.setCellStyle(cs);
				cell08 = row0.createCell(12);
				cell08.setCellValue("自保免费电梯");				
				cell08.setCellStyle(cs);
				cell081 = row0.createCell(13);				
				cell081.setCellStyle(cs);
				cell09 = row0.createCell(14);
				cell09.setCellValue("小计");				
				cell09.setCellStyle(cs);
				cell091 = row0.createCell(15);				
				cell091.setCellStyle(cs);
			}	
			rowno+=1;
			row0 = sheet.createRow( rowno); // 创建第一行
			cell0=null;
			for (int i = 0; i < headNameLen; i++) {				
				cell0 = row0.createCell((short) row0lineNo);
				cell0.setCellValue(headName[i]);
				cell0.setCellStyle(cs);					
				row0lineNo++;
			}
			rowno+=1;
			String[] key1Name = key1str.split(",");
			int key1NameLen = key1Name.length;
			/*输出对应表头的数值*/
			HashMap rowMap=null;
			int rownlineNo=0;
			if(rowlistLen>0){				
				for (int k = 0 ;  k < rowlistLen; k++) {	
					rownlineNo=0;
					row0 = sheet.createRow( rowno);
					rowMap = (HashMap) resultList.get(k);	
					for (int m = 0; m < key1NameLen; m++) {
						cell0 = row0.createCell((short) rownlineNo);
						if (m==2||m==4||m==6||m==8||m==10||m==12||m==14){
							XSSFCellStyle ce = wb.createCellStyle();
							ce.setAlignment(XSSFCellStyle.ALIGN_RIGHT);//左右居中
							ce.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
							ce.setBorderTop(XSSFCellStyle.BORDER_THIN);
							ce.setBorderBottom(XSSFCellStyle.BORDER_THIN);
							ce.setBorderLeft(XSSFCellStyle.BORDER_THIN);
							ce.setBorderRight(XSSFCellStyle.BORDER_THIN);
							cell0.setCellValue(rowMap.get(key1Name[m]) + "");
							cell0.setCellStyle(ce);
						}else {
							XSSFCellStyle ce = wb.createCellStyle();
							ce.setAlignment(XSSFCellStyle.ALIGN_CENTER);//左右居中
							ce.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
							ce.setBorderTop(XSSFCellStyle.BORDER_THIN);
							ce.setBorderBottom(XSSFCellStyle.BORDER_THIN);
							ce.setBorderLeft(XSSFCellStyle.BORDER_THIN);
							ce.setBorderRight(XSSFCellStyle.BORDER_THIN);
							cell0.setCellValue(rowMap.get(key1Name[m]) + "");
							cell0.setCellStyle(ce);
						}
						rownlineNo++;
					}
					rowno++;
				}
				/*输出最后表尾开始*/
				String footstr=request.getAttribute("totalsignnum")+"-"+request.getAttribute("totalsignPrice")+"-"+request.getAttribute("totalbackNum")+"-"+request.getAttribute("totalbackPrice")+"-"+
				request.getAttribute("totalcontinueNum")+"-"+request.getAttribute("totalcontinuePrice")+"-"+request.getAttribute("totalzbfNum")+"-"+request.getAttribute("totalzbPrice")+"-"
				+request.getAttribute("totalfbNum")+"-"+request.getAttribute("totalfbPrice")+"-"+request.getAttribute("totalsbNum")+"-"+request.getAttribute("totalsbPrice")+"-"
				+request.getAttribute("totalxjNum")+"-"+request.getAttribute("totalxjnPrice");
				String[] footname = footstr.split("-");
				XSSFRow rowo = sheet.createRow( (rowno)); // 创建总计行
				int j=2;
				sheet.addMergedRegion(new CellRangeAddress(rowno,rowno,0,1));
				cell0 = rowo.createCell((short) 0);
				cell0.setCellValue("保养总计");
				cell0.setCellStyle(cs);
				cell0 = rowo.createCell((short) 1);
				cell0.setCellStyle(cs);
				for (int i = 0; i < footname.length; i++,j++) {	
					cell0 = rowo.createCell((short) j);
					if (i==3||i==5||i==7||i==9||i==11||i==13||i==15) {
						XSSFCellStyle cd = wb.createCellStyle();
						cd.setAlignment(XSSFCellStyle.ALIGN_RIGHT);//左右居中
						cd.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
						cd.setBorderTop(XSSFCellStyle.BORDER_THIN);
						cd.setBorderBottom(XSSFCellStyle.BORDER_THIN);
						cd.setBorderLeft(XSSFCellStyle.BORDER_THIN);
						cd.setBorderRight(XSSFCellStyle.BORDER_THIN);
						cd.setFont(f);
						cell0.setCellValue(footname[i]);						
						cell0.setCellStyle(cd);	
					}else {
						XSSFCellStyle ce = wb.createCellStyle();
						ce.setAlignment(XSSFCellStyle.ALIGN_CENTER);//左右居中
						ce.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
						ce.setBorderTop(XSSFCellStyle.BORDER_THIN);
						ce.setBorderBottom(XSSFCellStyle.BORDER_THIN);
						ce.setBorderLeft(XSSFCellStyle.BORDER_THIN);
						ce.setBorderRight(XSSFCellStyle.BORDER_THIN);
						ce.setFont(f);
						cell0.setCellValue(footname[i]);
						cell0.setCellStyle(ce);	
					}				
				}	
				rowno++;
				XSSFRow rowoo = sheet.createRow( (rowno)); // 创建最后一行
				for (int i = 0; i < 1; i++) {				
					cell0 = rowoo.createCell(0,3);
					cell0.setCellValue("记录总数为:"+resultList.size()+"条");					
				}	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="
				+ URLEncoder.encode("保养情况汇总表报表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		return response;	
		}
	}