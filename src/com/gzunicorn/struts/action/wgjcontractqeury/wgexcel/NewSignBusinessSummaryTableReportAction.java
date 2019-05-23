package com.gzunicorn.struts.action.wgjcontractqeury.wgexcel;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.util.Region;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * 新签业务汇总表
 * 
 * @author Administrator
 * 
 */
public class NewSignBusinessSummaryTableReportAction extends DispatchAction {

	Log log = LogFactory
			.getLog("NewSignBusinessSummaryTableReportAction.class");
	BaseDataImpl bd = new BaseDataImpl();

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		/************ 开始用户权限过滤 ************/
		SysRightsUtil.filterModuleRight(request, response,
				SysRightsUtil.NODE_ID_FORWARD + "newsignbusinesssummarytable",
				null);
		/************ 结束用户权限过滤 ************/

		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchCondition";
		}
		return dispatchMethod(mapping, form, request, response, name);
	}

	/**
	 * 查询界面
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
		request.setAttribute("navigator.location", "签订业务汇总表 >> 查询");
//		List mugStorages = new ArrayList();
		Session hs = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = null;
		try {
			userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			hs = HibernateUtil.getSession();
//			mugStorages = Grcnamelist1.getStorageName(hs,userInfo.getUserID());
			request.setAttribute("grcidlist", Grcnamelist1.getgrcnamelist(hs,userInfo.getUserID()));
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
//		request.setAttribute("mugStorages", mugStorages);
		return mapping.findForward("toCondition");
	}

	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		ServeTableForm tableForm = (ServeTableForm) form;
		// HttpSession session = request.getSession();
		HashMap conditionmap = new HashMap();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = null;

		// 获取前台查询条件
		// String contractid = (String)
		// tableForm.getProperty("contractid");//合同编号
		String lotdates = (String) tableForm.getProperty("lotdates");// 签订日期（始）
		String lotdatee = (String) tableForm.getProperty("lotdatee");// 签订日期（终）
		// String custname = (String) tableForm.getProperty("custname");//
		// 甲方单位名称
		String username = (String) tableForm.getProperty("username");// 业务员名称
		// String nodeid = (String) tableForm.getProperty("nodeid");// 报修源
		String grcid = (String) tableForm.getProperty("grcid");// 所属维保分部代码
		// 将查询条件存放在map中，用于查看页面的导出excel操作时查询数据
		// conditionmap.put("contractid", contractid);
		conditionmap.put("lotdates", lotdates);
		conditionmap.put("lotdatee", lotdatee);
		// conditionmap.put("custname", custname);
		conditionmap.put("username", username);
		// conditionmap.put("nodeid", nodeid);
		conditionmap.put("grcid", grcid);
		conditionmap.put("timerange", "N");
		if ((null != lotdates && !"".equals(lotdates.trim()))
				|| (null != lotdatee && !"".equals(lotdatee.trim()))) {
			conditionmap.put("timerange", "Y");
		}
		request.setAttribute("conditionmap", conditionmap);

		// if (contractid == null || "".equals(contractid.trim())) {
		// contractid = "%";
		// } else {
		// contractid = "%"+contractid.trim()+"%";
		// }
		if (lotdates == null || "".equals(lotdates.trim())) {
			lotdates = "0000-00-00";
		} else {
			lotdates = lotdates.trim();
		}
		if (lotdatee == null || "".equals(lotdatee.trim())) {
			lotdatee = "9999-99-99";
		} else {
			lotdatee = lotdatee.trim();
		}
		// if (custname == null || "".equals(custname.trim())) {
		// custname = "%";
		// } else {
		// custname = "%"+custname.trim()+"%";
		// }
		if (username == null || "".equals(username.trim())) {
			username = "%";
		} else {
			username = "%" + username.trim() + "%";
		}
		// if (nodeid == null || "".equals(nodeid.trim())) {
		// nodeid = "%";
		// } else {
		// nodeid = nodeid.trim();
		// }
		if (grcid == null || "".equals(grcid.trim())) {
			grcid = "%";
		} else {
			grcid = grcid.trim();
		}

		List tempList = new ArrayList();
		HashMap map = null;
		Session hs = null;
		String sql = "";
		// double nowfee = 0;
		int count = 0;
		try {
			hs = HibernateUtil.getSession();
			sql = "EXEC SP_NEW_SIGN_BUSINESS_SUMMARY_TABLE '" + lotdates
					+ "','" + lotdatee + "','" + username +"','"+grcid+"'" ;

			DebugUtil.println(sql);
			ResultSet rs = hs.connection().createStatement().executeQuery(sql);
			while (rs.next()) {
				count++;
				map = new HashMap();
				map.put("xuhao", count);
				map.put("operationid", rs.getString("operationid"));// 业务员
				map.put("username", rs.getString("username"));// 业务员名称
				map.put("wnum", rs.getString("wnum"));// 维修台数
				map.put("wamt", rs.getString("wamt"));// 维修金额
				map.put("gnum", rs.getString("gnum"));// 改造台数
				map.put("gamt", rs.getString("gamt"));// 改造金额
				map.put("jnum", rs.getString("jnum"));// 加装台数
				map.put("jamt", rs.getString("jamt"));// 加装金额
				map.put("bnum", rs.getString("bnum"));// 保养台数
				map.put("bamt", rs.getString("bamt"));// 保养金额
				map.put("grcname", rs.getString("grcname"));//所属维保分部
				tempList.add(map);
			}
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			hs.close();
		}

		// NumberFormat nf = new DecimalFormat("###,###.00");
		// request.setAttribute("nowfee", nf.format(nowfee));

		ActionForward forward = null;
		if (null != tableForm.getProperty("genReport")
				&& "Y".equals(tableForm.getProperty("genReport"))) {
			try {
				response = toExcelRecord(tempList, request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			request.setAttribute("count", count);// 查询结果集
			request.setAttribute("resultList", tempList);// 查询结果集
			forward = mapping.findForward("toList");
		}
		return forward;
	}

	/**
	 * 导出查询数据到Excel
	 * 
	 * @param resultList
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public HttpServletResponse toExcelRecord(List resultList,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		HSSFWorkbook wb = new HSSFWorkbook();

		// 配置表头的共用单元格样式
		HSSFCellStyle cs = wb.createCellStyle();
		cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		HSSFFont f = wb.createFont();
		f.setFontHeightInPoints((short) 11);// 字号
		f.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);// 加粗
		cs.setFont(f);
		cs.setBorderTop(HSSFCellStyle.BORDER_THIN);// 设置上边框显示
		cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 设置下边框显示
		cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 设置左边框显示
		cs.setBorderRight(HSSFCellStyle.BORDER_THIN);// 设置右边框显示

		// 配置表单内容的单元格样式
		HSSFCellStyle cc = wb.createCellStyle();
		cc.setDataFormat((short) 0x31);// HSSFDataFormat的数据格式 文本格式
		cc.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		cc.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		// cc.setBorderTop(HSSFCellStyle.BORDER_THIN);
		// cc.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// cc.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// cc.setBorderRight(HSSFCellStyle.BORDER_THIN);

		int rowlistLen = resultList.size();
		try {
			String headstr = "序号,业务员,所属维保分部,新签订维修合同,'',新签订改造合同,'',新签订加装合同,'',新签订保养合同,''";
			String head1str = "'','','',数量,金额,数量,金额,数量,金额,数量,金额";
			String key1str = "xuhao,username,grcname,wnum,wamt,gnum,gamt,jnum,jamt,bnum,bamt";

			int rowno = 0;
			HSSFSheet sheet = wb.createSheet();
			wb.setSheetName(0, "维改新签业务汇总表");

			/* 输出第一行表头开始 */
			HSSFRow row0 = sheet.createRow( rowno); // 创建第一行
			HSSFCell cell0 = null;
			String[] headName = headstr.split(",");
			int headNameLen = headName.length;
			int colcount = 0;
			for (int i = 0; i < headNameLen; i++) {
				cell0 = row0.createCell((short) i);
				String headname2 = headName[i];
				cell0.setCellValue(headname2);
				cell0.setCellStyle(cs);

				if (headname2.equals("序号")) {
					// 四个参数代表的意思是： 分别代表起始行、起始列、结束行、结束列 (跨行)
					sheet.addMergedRegion(new Region((short) 0, (short) 0,
							(short) 1, (short) 0));
				} else if (headname2.equals("业务员")) {
					// 四个参数代表的意思是： 分别代表起始行、起始列、结束行、结束列 (跨行)
					sheet.addMergedRegion(new Region((short) 0, (short) 1,
							(short) 1, (short) 1));
				}else if (headname2.equals("所属维保分部")) {
					// 四个参数代表的意思是： 分别代表起始行、起始列、结束行、结束列 (跨行)
					sheet.addMergedRegion(new Region((short) 0, (short) 2,
							(short) 1, (short) 2));
				} else if (headname2.indexOf("新签订") > -1) {
					// 四个参数代表的意思是： 分别代表起始行、起始列、结束行、结束列 (跨列)
					sheet.addMergedRegion(new Region((short) 0,
							(short) colcount, (short) 0, (short) (colcount + 1)));
				}
				colcount++;
			}
			rowno++;

			/* 输出第二行表头开始 */
			String[] head1Name = head1str.split(",");
			int head1NameLen = head1Name.length;
			HSSFRow row1 = sheet.createRow( rowno); // 创建第二行
			HSSFCell cell1 = null;
			for (int i = 0; i < head1NameLen; i++) {
				cell1 = row1.createCell((short) i);
				cell1.setCellValue(head1Name[i]);
				cell1.setCellStyle(cs);
			}
			rowno++;

			// 输出对应表头的数值
			String[] key1Name = key1str.split(",");
			int key1NameLen = key1Name.length;
			HashMap rowMap = null;
			HSSFRow row2 = null;
			HSSFCell cell2 = null;
			int rownlineNo = 0;
			if (rowlistLen > 0) {
				for (int k = 0; k < rowlistLen; k++) {
					rownlineNo = 0;
					row2 = sheet.createRow( rowno);
					rowMap = (HashMap) resultList.get(k);
					for (int m = 0; m < key1NameLen; m++) {
						cell2 = row2.createCell((short) rownlineNo);
						if (rowMap.get(key1Name[m]) != null) {
							if ("xuhao".equals(key1Name[m])
									|| "username".equals(key1Name[m]) 
									|| "grcname".equals(key1Name[m])) {
								cell2.setCellValue(rowMap.get(key1Name[m]) + "");
								cell2.setCellStyle(cc);
							} else {
								cell2.setCellValue(Double.valueOf(rowMap
										.get(key1Name[m]) + ""));
							}
						}
						rownlineNo++;
					}
					rowno++;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="
				+ URLEncoder.encode("新签业务汇总表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		return response;
	}

}
