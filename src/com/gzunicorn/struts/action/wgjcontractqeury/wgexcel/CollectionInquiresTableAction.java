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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
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
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * 收款查询表
 * @author Administrator
 *
 */
public class CollectionInquiresTableAction extends DispatchAction {

	Log log = LogFactory.getLog("CollectionInquiresTableAction.class");
	BaseDataImpl bd = new BaseDataImpl();

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/************开始用户权限过滤************/
		SysRightsUtil.filterModuleRight(request,response,SysRightsUtil.NODE_ID_FORWARD + "collectioninquirestable",null);
		/************结束用户权限过滤************/
		
		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchCondition";
		}
		return dispatchMethod(mapping, form, request, response, name);
	}

	/**
	 * 查询界面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toSearchCondition(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("navigator.location", "收款查询表 >> 查询");
		List mugStorages = new ArrayList();
		Session hs = null;
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = null;

		try {
			userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			hs = HibernateUtil.getSession();
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

		return mapping.findForward("toCondition");
	}


	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		ServeTableForm tableForm = (ServeTableForm) form;
		HttpSession session = request.getSession();
		HashMap conditionmap = new HashMap();
		
		//获取前台查询条件
		String contractid = (String) tableForm.getProperty("contractid");//合同编号
		String predates = (String) tableForm.getProperty("predates");// 应收款日期（始）
		String predatee = (String) tableForm.getProperty("predatee");// 应收款日期（终）
		String custname = (String) tableForm.getProperty("custname");// 甲方单位
		String grcid = (String) tableForm.getProperty("grcid");// 所属维保分部代码
		//String contracttype = (String) tableForm.getProperty("contracttype");// 合同类型
		
		//将查询条件存放在map中，用于查看页面的导出excel操作时查询数据
		conditionmap.put("contractid", contractid);
		conditionmap.put("predates", predates);
		conditionmap.put("predatee", predatee);
		conditionmap.put("custname", custname);
		conditionmap.put("grcid", grcid);

		request.setAttribute("conditionmap", conditionmap);
		
		if (contractid == null || "".equals(contractid.trim())) {
			contractid = "%";
		} else {
			contractid = "%"+contractid.trim()+"%";
		}
		if (predates == null || "".equals(predates.trim())) {
			predates = "0000-00-00";
		} else {
			predates = predates.trim();
		}
		if (predatee == null || "".equals(predatee.trim())) {
			predatee = "9999-99-99";
		} else {
			predatee = predatee.trim();
		}
		if (custname == null || "".equals(custname.trim())) {
			custname = "%";
		} else {
			custname = "%"+custname.trim()+"%";
		}
		if (grcid == null || "".equals(grcid.trim())) {
			grcid = "%";
		} else {
			grcid = grcid.trim();
		}
		
		List tempList = new ArrayList();
		HashMap map = null;
		Session hs = null;
		String sql = "";
		int count = 0;
		Double premoney=0.0;//应收金额
		Double nowfee=0.0;//已开票金额
		Double nonowfee=0.0;//未开票金额
		Double billatm=0.0;//已开票已收款金额
		Double billnoatm=0.0;//已开票到期欠款金额
		Double nobillatm=0.0;//未开票到期欠款金额
		Double nobillnoatm=0.0;//已开票非欠款金额
		try {
			hs = HibernateUtil.getSession();
			sql = "EXEC SP_COLLECTION_INQUIRES_TABLE '"+contractid+"','"+predates+"','"+predatee+"','"
				+custname+"','"+grcid+"'" ;
			
			DebugUtil.println(sql);
			ResultSet rs = hs.connection().createStatement().executeQuery(sql);
			while (rs.next()) {
				count++;
				map = new HashMap();
				

				map.put("xuhao", count);
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
				
				tempList.add(map);
			}
			DecimalFormat df=new DecimalFormat("#,##0.00");
			String sj="统计：记录数"+count+"条, "
					+ "应收款金额总计为："+df.format(premoney)+"（元）,"
					+ "已开票金额总计为："+df.format(nowfee)+"（元), "
					+ "未开票金额总计为："+df.format(nonowfee)+"（元）, "
					+ "已开票已收款金额总计为："+df.format(billatm)+"（元）, "
					+ "已开票到期欠款金额总计为："+df.format(billnoatm)+"（元）, "
			        + "未开票到期欠款金额总计为："+df.format(nobillatm)+"（元）, "
			        + "已开票非欠款金额总计为："+df.format(nobillnoatm)+"（元）. ";
			request.setAttribute("sj", sj);
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			hs.close();
		}
		
		//NumberFormat nf = new DecimalFormat("###,###.00");
		//request.setAttribute("nowfee", nf.format(nowfee));
		
		ActionForward forward = null;
		if (null != tableForm.getProperty("genReport") && "Y".equals(tableForm.getProperty("genReport"))) {
			try {
				response = toExcelRecord(tempList, request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			request.setAttribute("count", count);//查询结果集
			request.setAttribute("resultList", tempList);//查询结果集
			forward = mapping.findForward("toList");
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
			Double premoney=0.0;//应收金额
			Double nowfee=0.0;//已开票金额
			Double nonowfee=0.0;//未开票金额
			Double billatm=0.0;//已开票已收款金额
			Double billnoatm=0.0;//已开票到期欠款金额
			Double nobillatm=0.0;//未开票到期欠款金额
			Double nobillnoatm=0.0;//已开票非欠款金额
			XSSFSheet sheet = wb.createSheet();	
			wb.setSheetName(0,"维改收款查询表");
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
							if("premoney".equals(key1Name[j])){
								premoney+=Double.valueOf(value).doubleValue();
							}else if("nowfee".equals(key1Name[j])){
								nowfee+=Double.valueOf(value).doubleValue();
							}else if("nonowfee".equals(key1Name[j])){
								nonowfee+=Double.valueOf(value).doubleValue();
							}else if("billatm".equals(key1Name[j])){
								billatm+=Double.valueOf(value).doubleValue();
							}else if("billnoatm".equals(key1Name[j])){
								billnoatm+=Double.valueOf(value).doubleValue();
							}else if("nobillatm".equals(key1Name[j])){
								nobillatm+=Double.valueOf(value).doubleValue();
							}else if("nobillnoatm".equals(key1Name[j])){
								nobillnoatm+=Double.valueOf(value).doubleValue();
							}
						}
						cell0.setCellStyle(cs);
					}
				}	
			}
			//输出尾部
			DecimalFormat df=new DecimalFormat("#,##0.00");
			XSSFCellStyle cellstyle=wb.createCellStyle();
			cellstyle.setWrapText(true);
			sheet.addMergedRegion(new CellRangeAddress(rowno,rowno+4,0,headNameLen-1));
			row0=sheet.createRow(rowno);
			cell0=row0.createCell(0);
			String sj="统计：记录数"+rowlistLen+"条, "
					+ "应收款金额总计为："+df.format(premoney)+"（元）,"
					+ "已开票金额总计为："+df.format(nowfee)+"（元), "
					+ "未开票金额总计为："+df.format(nonowfee)+"（元）, "
					+ "已开票已收款金额总计为："+df.format(billatm)+"（元）, "
					+ "已开票到期欠款金额总计为："+df.format(billnoatm)+"（元）, "
			        + "未开票到期欠款金额总计为："+df.format(nobillatm)+"（元）, "
			        + "已开票非欠款金额总计为："+df.format(nobillnoatm)+"（元）. ";
			cell0.setCellValue(sj);
			cell0.setCellStyle(cellstyle);
		}catch(Exception e){
			e.printStackTrace();
		}
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="
		+ URLEncoder.encode("维改收款查询表", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());			
		return response;	
		}
		
 }




