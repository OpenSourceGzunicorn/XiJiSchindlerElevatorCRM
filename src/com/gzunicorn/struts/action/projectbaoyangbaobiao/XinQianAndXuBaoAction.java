package com.gzunicorn.struts.action.projectbaoyangbaobiao;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
import org.apache.struts.actions.DispatchAction;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.logic.DataOperation;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.action.ServeTableForm;

public class XinQianAndXuBaoAction extends DispatchAction {
	/**
	 * 新签与继保明细报表
	 * 
	 * @author Administrator
	 * 
	 */
		Log log = LogFactory.getLog("XinQinAndXuBaoAction.class");

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
			ActionForward forward = dispatchMethod(mapping, form, request,response, name);
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
			request.setAttribute("navigator.location", "在保与续保明细报表 >> 查询");
			HttpSession session=request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			List grcidlist=new ArrayList();
			List mugStorages = null;
			Session hs = null;
			try {
				hs = HibernateUtil.getSession();
				grcidlist=Grcnamelist1.getgrcnamelist(hs,userInfo.getUserID());
				mugStorages=Grcnamelist1.getStorageName(hs,userInfo.getUserID());//保养站
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
			request.setAttribute("grcidlist",grcidlist);
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
			//合同号
			String contractid = tableForm.getProperty("contractid");
			if (contractid == null || "".equals(contractid)) {
				contractid = "%";
			} else {
				contractid = "%"+contractid.trim()+"%";
			}
			session.setAttribute("contractid", contractid);
			//在保、续保
			String contractstatus = tableForm.getProperty("contractstatus");
			if (contractstatus == null || "".equals(contractstatus)) {
				contractstatus = "%";
			} else {
				contractstatus = contractstatus.trim();
			}
			session.setAttribute("contractstatus", contractstatus);

			// 电梯编号
			String elevatorno = tableForm.getProperty("elevatorno");
			if (elevatorno == null || "".equals(elevatorno)) {
				elevatorno = "%";
			} else {
				elevatorno = "%"+elevatorno.trim()+"%";
			}
			session.setAttribute("elevatorno", elevatorno);
			// 甲方单位
			String custname = tableForm.getProperty("custname");
			if (custname == null || "".equals(custname)) {
				custname = "%";
			} else {
				custname = "%"+custname.trim()+"%";
			}
			session.setAttribute("custname", custname);
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
				if(genReport!= null && (genReport.equals("Y") || genReport.equals("C") || "Y".equals(flag))){
					grcid=grcid22;
				}else{
				grcid = "";
				}
			} else {
				grcid = grcid.trim();
			}
			session.setAttribute("mugStorages", storageid);
			
			
			List tempList = new ArrayList();
			float _countpremoneyprice = 0;
			float _yearcheckprice = 0;
			String countnum = "0";
			Connection conn = null;
			String sql = null;
			HashMap map = new HashMap();

			try {
				conn = HibernateUtil.getSession().connection();
				
				if ( genReport!= null && genReport.equals("C")) {
					sql = "exec SP_MAINCONTRACT_SINGORSB_QUERY_2 '" + contractid + "','"
							+ custname + "','" + storageid + "','"+elevatorno+"','"+contractstatus+"','"+grcid+"'" ;
				}else{
					sql = "exec SP_MAINCONTRACT_SINGORSB_QUERY '" + contractid + "','"
							+ custname + "','" + storageid + "','"+elevatorno+"','"+contractstatus+"','"+grcid+"'" ;
				}
				//System.out.println(sql);
				
				DataOperation op = new DataOperation();
				op.setCon(conn);
				tempList = op.getSPList(sql);
				List list = new ArrayList();
				String _newprice = null;
				String _newallprice = null;
				String _newyearcheckprice=null;
				
				int xuhao=0;
				for (Iterator it = tempList.iterator(); it.hasNext();) {
					HashMap _map = (HashMap) it.next();
					xuhao++;
					_map.put("xuhao",xuhao);
					
					if(genReport!= null && (genReport.equals("C"))){
						
					}else{
						if(!"合计".equals(String.valueOf(_map.get("serviceno")))){							
							if (_map.get("allprice") != null){
								_countpremoneyprice += Float.parseFloat((String) _map.get("allprice"));
								_newallprice = CommonUtil.formatThousand(_map.get("allprice").toString(),2);
							}
							_map.put("allprice",_newallprice);
							list.add(_map);	
						}else{
							countnum = String.valueOf(_map.get("r18"));
						}
					}
					
				}

            request.setAttribute("report_list", list);
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
			if (!"Y".equals(flag)) {
				session.setAttribute("result", tempList);
			}

			if ( genReport!= null && genReport.equals("Y") || "Y".equals(flag)) {
				try {
					response = toExcelRecord(tempList, request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if ( genReport!= null && genReport.equals("C") || "Y".equals(flag)) {
				try {
					response = toExcelRecord2(tempList, request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				forward = mapping.findForward("returnList");
			}
			return forward;
		}
		
		/**
		 * 按合同号导出EXCEL
		 * @param resultList
		 * @param request
		 * @param response
		 * @return
		 * @throws IOException
		 */
		public HttpServletResponse toExcelRecord2(List resultList,
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
			cs.setBorderTop(XSSFCellStyle.BORDER_THIN);
			cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cs.setBorderRight(XSSFCellStyle.BORDER_THIN);
			
//			XSSFCellStyle cc = wb.createCellStyle();
//			cc.setAlignment(XSSFCellStyle.ALIGN_RIGHT);//左右居右
//			cc.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
//			cc.setBorderTop(XSSFCellStyle.BORDER_THIN);
//			cc.setBorderBottom(XSSFCellStyle.BORDER_THIN);
//			cc.setBorderLeft(XSSFCellStyle.BORDER_THIN);
//			cc.setBorderRight(XSSFCellStyle.BORDER_THIN);
			
			int rowlistLen=resultList.size();	
			try{
				String headstr="序号,合同号,站别,甲方单位,保养月份,合同总额,开始日期,到期日期,新签/续保,所属维保分部";
				String key1str="xuhao,contractid,nodename,custname,r18,allprice,mugstartdate,mugenddate,r1,grcname";
				
				String[] headName = headstr.split(",");
				int headNameLen = headName.length;//表头个数		
				int row0lineNo=0;
				int rowno=0;
				XSSFSheet sheet = wb.createSheet();
				wb.setSheetName(0, "新签与继保明细报表按合同导出");										
				/*输出第一行表头开始*/
				XSSFRow row0 = sheet.createRow( rowno); // 创建第一行
				XSSFCell cell0=null;
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
				int countsum=0;
				if(rowlistLen>0){				
					for (int k = 0 ;  k < rowlistLen; k++) {
						rowMap = (HashMap) resultList.get(k);
							rownlineNo=0;
							row0 = sheet.createRow( rowno);
							for (int m = 0; m < key1NameLen; m++) {
								cell0 = row0.createCell((short) rownlineNo);
								cell0.setCellValue(rowMap.get(key1Name[m]) + "");
								cell0.setCellStyle(cs);
								rownlineNo++;
							}
							rowno++;
							countsum++;
						}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-disposition", "offline; filename="
					+ URLEncoder.encode("新签与继保明细报表按合同导出", "utf-8") + ".xlsx");
			wb.write(response.getOutputStream());			
			return response;	
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
		cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);//左右居中
		cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
		XSSFFont f  = wb.createFont();
		f.setFontHeightInPoints((short) 11);//字号
		f.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);//加粗
		cs.setFont(f);
		cs.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cs.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cs.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cs.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		XSSFCellStyle cc = wb.createCellStyle();
		cc.setAlignment(XSSFCellStyle.ALIGN_RIGHT);//左右居右
		cc.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
		cc.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cc.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cc.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cc.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		int rowlistLen=resultList.size();	
		try{
			String headstr="合同号,站别,甲方单位,电梯编号,保养月份,合同总额,开始日期,到期日期,所属维保分部";
			String key1str="contractid,nodename,custname,elevatorno,r18,allprice,mugstartdate,mugenddate,grcname";
			
			String[] headName = headstr.split(",");
			int headNameLen = headName.length;//表头个数		
			int row0lineNo=0;
			int rowno=0;
			XSSFSheet sheet = wb.createSheet();
			wb.setSheetName(0, "新签与继保明细报表");										
			/*输出第一行表头开始*/
			XSSFRow row0 = sheet.createRow( rowno); // 创建第一行
			XSSFCell cell0=null;
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
			int countsum=0;
			if(rowlistLen>0){				
				for (int k = 0 ;  k < rowlistLen; k++) {
					rowMap = (HashMap) resultList.get(k);
						rownlineNo=0;
						row0 = sheet.createRow( rowno);
						for (int m = 0; m < key1NameLen; m++) {
							cell0 = row0.createCell((short) rownlineNo);
							cell0.setCellValue(rowMap.get(key1Name[m]) + "");
							cell0.setCellStyle(cs);
							rownlineNo++;
						}
						rowno++;
						countsum++;
					}
				}												
		}catch(Exception e){
			e.printStackTrace();
		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="
				+ URLEncoder.encode("新签与继保明细报表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());			
		return response;	
		}
		
		
		
	}