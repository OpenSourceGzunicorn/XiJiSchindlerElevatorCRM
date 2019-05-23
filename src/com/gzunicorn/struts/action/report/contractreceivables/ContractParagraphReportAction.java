package com.gzunicorn.struts.action.report.contractreceivables;


import java.io.IOException;
import java.net.URLEncoder;
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
import com.gzunicorn.hibernate.contractpayment.contractinvoicemanage.ContractInvoiceManage;
import com.gzunicorn.hibernate.contractpayment.contractparagraphmanage.ContractParagraphManage;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

/**
 * 合同收款管理=>来款明细报表
 * @author Lijun
 *
 */
public class ContractParagraphReportAction extends DispatchAction {

	Log log = LogFactory.getLog(ContractParagraphReportAction.class);

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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "contractparagraphreport", null);
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

		request.setAttribute("navigator.location", " 来款明细报表 >> 查询");
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

		forward = mapping.findForward("contractParagraphReportSearch");		
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

		request.setAttribute("navigator.location", " 来款明细报表 >> 查询结果");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;

		String maintdivision = request.getParameter("maintdivision");
		String maintstation = request.getParameter("maintstation");
		String contractno = request.getParameter("contractno");
		String sdate1 = request.getParameter("sdate1");
		String edate1 = request.getParameter("edate1");
		String sdate2 = request.getParameter("sdate2");
		String edate2 = request.getParameter("edate2");

		HashMap rmap= new HashMap();			
		rmap.put("maintdivision", maintdivision);
		rmap.put("maintstation", maintstation);
		rmap.put("contractno", contractno);
		rmap.put("sdate1", sdate1);
		rmap.put("edate1", edate1);
		rmap.put("sdate2", sdate2);
		rmap.put("edate2", edate2);
		request.setAttribute("rmap", rmap);
		
		Session hs = null;
		try {
			hs = HibernateUtil.getSession();
			
			String hql="select a,b.preDate,b.preMoney,c.comname,s.storagename,ct.companyName "
					+"from ContractParagraphManage a,"
					+"ProContractArfeeMaster b,Storageid s,Company c,Customer ct "
					+"where a.maintStation=s.storageid "
					+"and a.maintDivision=c.comid and ct.companyId=a.companyId and "
					+"a.arfJnlNo=b.jnlNo and a.auditStatus='Y' ";//来款已审核的
			if(maintdivision!=null && !maintdivision.trim().equals("")){
				hql+=" and a.maintDivision like '"+maintdivision.trim()+"'";
			}
			if(maintstation!=null && !maintstation.trim().equals("")){
				hql+=" and a.maintStation like '"+maintstation.trim()+"'";
			}
			if(contractno!=null && !contractno.trim().equals("")){
				hql+=" and a.contractNo like '%"+contractno.trim()+"%'";
			}

			if(sdate1!=null && !sdate1.trim().equals("")){
				hql+=" and a.paragraphDate >='"+sdate1.trim()+"'";
			}
			if(edate1!=null && !edate1.trim().equals("")){
				hql+=" and a.paragraphDate <='"+edate1.trim()+"'";
			}
			if(sdate2!=null && !sdate2.trim().equals("")){
				hql+=" and a.operDate >='"+sdate2.trim()+" 00:00:00'";
			}
			if(edate2!=null && !edate2.trim().equals("")){
				hql+=" and a.operDate <='"+edate2.trim()+" 99:99:99'";
			}
			hql+=" order by a.contractNo,a.paragraphDate";
			
			//System.out.println("来款明细报表>>>>"+hql);

            List reList=hs.createQuery(hql).list();
            double totalnum=0;
            double totalnum2=0;
            List reportList=new ArrayList();
            if(reList!=null && reList.size()>0){
            	ContractParagraphManage cpm=null;
            	for(int i=0;i<reList.size();i++){
            		Object[] obj=(Object[])reList.get(i);
            		cpm=(ContractParagraphManage)obj[0];
            		
            		HashMap map= new HashMap();

            		map.put("BillNo", cpm.getBillNo());//合同流水号
            		map.put("ContractType", cpm.getContractType());//合同类型
                	map.put("MaintDivision", cpm.getMaintDivision());//所属分部门代码
                	map.put("ComName", obj[3].toString());//所属分部门
                	map.put("MaintStation", cpm.getMaintStation());//所属维保站代码
                	map.put("StorageName", obj[4].toString());//所属维保站
                	map.put("ContractNo", cpm.getContractNo());//合同号
                	map.put("CompanyName", obj[5].toString());//甲方单位名称
                	map.put("PreDate", obj[1].toString());//应收款日期
                	map.put("PreMoney", obj[2].toString());//应收款金额
                	map.put("ParagraphDate", cpm.getParagraphDate());//来款日期
                	map.put("ParagraphMoney", String.valueOf(cpm.getParagraphMoney()));//来款金额
                	map.put("ParagraphDate2", cpm.getOperDate());//来款录入日期

                	double money=Double.parseDouble(obj[2].toString());
                	double money2=cpm.getParagraphMoney();//来款金额
                	
                	totalnum+=money;
                	totalnum2+=money2;

                	reportList.add(map);
            	}
            }
            
            HashMap hmap=new HashMap();
            hmap.put("totalnum", "应收款金额合计:"+df.format(totalnum)+"  来款金额合计:"+df.format(totalnum2));
            
            if (dform.get("genReport") != null && dform.get("genReport").equals("Y")) {
            	//导出excel
    			response = toExcelRecord(response,reportList,hmap);
    			forward = mapping.findForward("exportExcel");
    		}else{
                if(reportList.size()>0 && reportList.size()<3001){
                	request.setAttribute("cpmReportList", reportList);
                }else if(reportList.size()>3000){
                	request.setAttribute("showinfostr", "查询数据超出三千行记录，请导出EXCEL查看。");
                }else{
                	request.setAttribute("showinfostr", "没有记录显示！");
                }
                request.setAttribute("totalhmap", hmap);
                
    			forward = mapping.findForward("contractParagraphReportList");	
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

		String[] titlename={"所属分部","所属维保站","维保合同号","甲方单位名称","应收款日期","应收款金额","来款日期","来款录入日期","来款金额"};
		String[] titleid={"ComName","StorageName","ContractNo","CompanyName","PreDate","PreMoney","ParagraphDate","ParagraphDate2","ParagraphMoney"};
		
		XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("来款明细报表");
        
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
				    if(titleid[c].equals("PreMoney") || titleid[c].equals("ParagraphMoney")){
				    	cell.setCellValue(Double.parseDouble((String)map.get(titleid[c])));
				    }else{
				    	cell.setCellValue((String)map.get(titleid[c]));
				    }
				}
			}
		}
		//参数：起始行号，终止行号， 起始列号，终止列号
		sheet.addMergedRegion(new CellRangeAddress(toolnum, toolnum, 0, 6));
		XSSFRow row1 = sheet.createRow(toolnum);
		XSSFCell cell1 = row1.createCell((short)0);
		cell1.setCellValue((String)hmap.get("totalnum"));
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("来款明细报表", "utf-8") + ".xlsx");
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
