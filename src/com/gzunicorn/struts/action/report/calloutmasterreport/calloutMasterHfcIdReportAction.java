package com.gzunicorn.struts.action.report.calloutmasterreport;

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

import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.hotlinefaulttype.HotlineFaultType;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

/**
 * 热线管理模块=>项目(电梯)故障分析表
 * @author Lijun
 *
 */
public class calloutMasterHfcIdReportAction extends DispatchAction {

	Log log = LogFactory.getLog(calloutMasterHfcIdReportAction.class);

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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "calloutmasterhfcidreport", null);
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

		request.setAttribute("navigator.location", " 项目(电梯)故障分析表 >> 查询");
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

		forward = mapping.findForward("calloutMasterHfcIdSearch");		
		return forward;
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
	public ActionForward toSearchResults(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute("navigator.location", " 项目(电梯)故障分析表 >> 查询结果");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;

		String maintdivision = request.getParameter("maintdivision");
		String maintstation = request.getParameter("maintstation");
		String companyid = request.getParameter("companyid");
		if(companyid==null || companyid.trim().equals("")){
			companyid="%";
		}
		String salescontractno = request.getParameter("salescontractno");
		if(salescontractno==null || salescontractno.trim().equals("")){
			salescontractno="%";
		}
		String elevatorNo = request.getParameter("elevatorNo");
		String serviceobjects = request.getParameter("serviceobjects");
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
		rmap.put("companyid", companyid);
		rmap.put("salescontractno", salescontractno);
		rmap.put("elevatorNo", elevatorNo);
		rmap.put("serviceobjects", serviceobjects);
		rmap.put("sdate1", sdate1);
		rmap.put("edate1", edate1);
		request.setAttribute("rmap", rmap);
		
		Session hs = null;
		ResultSet rs=null;
		Connection conn = null;
		try {
			hs = HibernateUtil.getSession();

            HashMap hmap=new HashMap();
    		String[] titlename={"急修编号","维保分部","维保站","销售合同号","报修单位名称","报修时间","报修电梯编号","报修方式",
    				"是否困人","故障类型","主板类型","故障代码","维修描述","故障分类","是否技术支持","坏件品牌型号或其它备注"};
    		String[] titleid={"calloutmasterno","comname","storagename","salescontractno","companyname","repairtime","elevatorno",
    				"repairmodename","istrapname","hftidname","hmtidname","faultcode","processdesc","hfcidname","serviceobjectsname","ServiceRem"};
    		hmap.put("titlename", titlename);
    		hmap.put("titleid", titleid);
    		hmap.put("tlen", titleid.length+1);
    		
    		//故障类型
     		String sqls2="select a from HotlineFaultType a ";
      		List hftlist=hs.createQuery(sqls2).list();
			
			String hql="exec sp_HfcIdReport '"+maintdivision.trim()+"','"+maintstation.trim()+"','"
						+companyid.trim()+"','"+salescontractno.trim()+"','"+elevatorNo.trim()+"','"
						+serviceobjects.trim()+"','"+sdate1.trim()+"','"+edate1.trim()+"'";
			System.out.println("项目(电梯)故障分析表>>>>"+hql);
			
			rs=hs.connection().prepareCall(hql).executeQuery();
			
            List reportList=new ArrayList();
            List sjfxlist=new ArrayList();
            List hftidlist=new ArrayList();
            List hmtidlist=new ArrayList();
            
            double sbkrlen=0;//设备故障困人次数
            double krlen=0;//困人次数
            double zdlen=0;//主动维修次数
            double gzcs=0;//设备故障次数
            
            while(rs.next()){
            	HashMap map= new HashMap();
            	
            	String results=rs.getString("results");
            	
            	//明细信息
            	if(results!=null && results.trim().equals("1")){
	            	for(int i=0;i<titleid.length;i++){
	            		map.put(titleid[i], rs.getString(titleid[i]));
	            	}
	            	
	            	String hfcid=rs.getString("hfcid");
	            	String kr=rs.getString("IsTrap");
	            	if(kr!=null && kr.trim().equals("Y")){
	            		krlen++;//困人总次数
	            		if(hfcid!=null && hfcid.trim().equals("001")){
	            			sbkrlen++;//001  设备或保养原因 
	            		}
	            	}
	            	String zd=rs.getString("RepairMode");
	            	if(zd!=null && zd.trim().equals("2")){
	            		zdlen++;
	            	}
	            	if(hfcid!=null && hfcid.trim().equals("001")){
	            		gzcs++;//001  设备或保养原因  设备故障次数
            		}
	            	
	            	String hftid=rs.getString("hftid");
	            	String hftdesc="";
		           	if(hftid!=null && !hftid.trim().equals("")){
		           		 String[] hftarr=hftid.split(",");
		           		 for(int h=0;h<hftarr.length;h++){
		           			 for(int l=0;l<hftlist.size();l++){
		           				HotlineFaultType hft=(HotlineFaultType)hftlist.get(l);
		           				String hftid2=hft.getHftId();
		           				 if(hftarr[h].equals(hftid2)){
		           					hftdesc+=hft.getHftDesc()+",";
		           					break;
		           				 }
		           			 }
		           		 }
		           	}
	            	map.put("hftidname",hftdesc);//故障类型名称
	
	            	reportList.add(map);
            	}
            	
            	//故障分类占比
            	if(results!=null && results.trim().equals("2")){
            		sjfxlist.add(rs.getString("name")+"占比="+rs.getDouble("zhanbi")+"%");
            	}
            	//故障类型占比
            	if(results!=null && results.trim().equals("3")){
            		HashMap map2= new HashMap();
                	map2.put("hfcId",rs.getString("id"));//故障类型序号
                	map2.put("hfcIdName",rs.getString("name"));//故障类型名称
                	map2.put("hfcnum",rs.getString("num"));//故障类型次数
                	map2.put("zhanbi",rs.getString("zhanbi")+"%");//占比

                	hftidlist.add(map2);
            	}
            	//主板型号占比
            	if(results!=null && results.trim().equals("4")){
            		HashMap map3= new HashMap();
                	map3.put("hmtIdName",rs.getString("name"));//主板型号
                	map3.put("FaultCode",rs.getString("FCode"));//故障代码	
                	map3.put("fatnum",rs.getString("num"));//次数
                	map3.put("zhanbi",rs.getString("zhanbi")+"%");//占比

                	hmtidlist.add(map3);
            	}
            }

            
            if(reportList!=null && reportList.size()>0){
            	/**================数据分析===============*/
                int rsize=reportList.size();

                int rlts=0;
                if(!sdate1.trim().equals("") && !edate1.trim().equals("") ){
                	rlts=DateUtil.compareDay(sdate1,edate1);
                }
                
                sjfxlist.add(0,"选择的报修时间天数="+rlts+"(天)");
                sjfxlist.add(1,"设备故障次数="+gzcs);
                sjfxlist.add(2,"MTBC=    (天)");
                sjfxlist.add(3,"主动维修占比="+df.format(zdlen/rsize*100)+"%");
                sjfxlist.add(4,"设备故障困人占比="+df.format(sbkrlen/krlen*100)+"%");//设备故障困人次数/困人总次数*100%
                sjfxlist.add(5,"");
               
            }
            hmap.put("sjfxlist", sjfxlist);
            hmap.put("hftidlist", hftidlist);
            hmap.put("hmtidlist", hmtidlist);
            
            if (dform.get("genReport") != null && dform.get("genReport").equals("Y")) {
            	//导出excel
    			response = toExcelRecord(response,reportList,hmap);
    			forward = mapping.findForward("exportExcel");
    		}else{
                if(reportList.size()>0 && reportList.size()<3001){
                	request.setAttribute("calloutMasterHfcIdList", reportList);
                }else if(reportList.size()>3000){
                	request.setAttribute("showinfostr", "查询数据超出三千行记录，请导出EXCEL查看。");
                }else{
                	request.setAttribute("showinfostr", "没有记录显示！");
                }
                request.setAttribute("totalhmap", hmap);
                
    			forward = mapping.findForward("calloutMasterHfcIdList");	
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

		String[] titlename=(String[])hmap.get("titlename");
		String[] titleid=(String[])hmap.get("titleid");
				
		XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("项目(电梯)故障分析表");
        
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
				    cell.setCellValue((String)map.get(titleid[c]));
				}
			}
		}
		
		List sjfxlist=(List)hmap.get("sjfxlist");
		List hftidlist=(List)hmap.get("hftidlist");
		List hmtidlist=(List)hmap.get("hmtidlist");
		
		XSSFSheet sheet2 = wb.createSheet("数据分析");
		
		/**============================数据分析================================*/
		//创建标题
        XSSFRow row02 = sheet2.createRow(0);
        XSSFCell cell02 = row02.createCell((short)0);
		//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell02.setCellValue("序号");
		cell02.setCellStyle(cs);
		
		cell02 = row02.createCell((short)1);
		//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell02.setCellValue("数据分析");
		cell02.setCellStyle(cs);

		//参数：起始行号，终止行号， 起始列号，终止列号
		sheet2.addMergedRegion(new CellRangeAddress(0, 0, 1, 4));
		
		for(int s=0;s<sjfxlist.size();s++){
			row02 = sheet2.createRow(s+1);
			
			cell02 = row02.createCell((short)0);
			//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell02.setCellValue(s+1);
			
			cell02 = row02.createCell((short)1);
			//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell02.setCellValue((String)sjfxlist.get(s));
			
			//参数：起始行号，终止行号， 起始列号，终止列号
			sheet2.addMergedRegion(new CellRangeAddress(s+1, s+1, 1, 4));
		}
		
		/**============================故障类型名称===============================*/
		int count=sjfxlist.size()+2;
		row02 = sheet2.createRow(count);
		
		cell02 = row02.createCell((short)0);
		//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell02.setCellValue("序号");
		cell02.setCellStyle(cs);
		
		cell02 = row02.createCell((short)1);
		//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell02.setCellValue("故障类型名称");
		cell02.setCellStyle(cs);
		
		cell02 = row02.createCell((short)2);
		//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell02.setCellValue("故障类型代码");
		cell02.setCellStyle(cs);
		
		cell02 = row02.createCell((short)3);
		//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell02.setCellValue("次数");
		cell02.setCellStyle(cs);
		
		cell02 = row02.createCell((short)4);
		//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell02.setCellValue("占比");
		cell02.setCellStyle(cs);
		
		for(int h=0;h<hftidlist.size();h++){
			row02 = sheet2.createRow(h+count+1);
			
			HashMap map=(HashMap)hftidlist.get(h);
			
			cell02 = row02.createCell((short)0);
			//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell02.setCellValue(h+1);
			
			cell02 = row02.createCell((short)1);
			//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell02.setCellValue((String)map.get("hfcIdName"));
			
			cell02 = row02.createCell((short)2);
			//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell02.setCellValue((String)map.get("hfcId"));
			
			cell02 = row02.createCell((short)3);
			//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell02.setCellValue(Double.parseDouble((String)map.get("hfcnum")));
			
			cell02 = row02.createCell((short)4);
			//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell02.setCellValue((String)map.get("zhanbi"));
			
		}
		
		/**============================主板型号===================================*/
		count=sjfxlist.size()+hftidlist.size()+4;
		row02 = sheet2.createRow(count);
		
		cell02 = row02.createCell((short)0);
		//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell02.setCellValue("序号");
		cell02.setCellStyle(cs);
		
		cell02 = row02.createCell((short)1);
		//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell02.setCellValue("主板型号");
		cell02.setCellStyle(cs);
		
		cell02 = row02.createCell((short)2);
		//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell02.setCellValue("故障代码");
		cell02.setCellStyle(cs);
		
		cell02 = row02.createCell((short)3);
		//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell02.setCellValue("次数");
		cell02.setCellStyle(cs);
		
		cell02 = row02.createCell((short)4);
		//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell02.setCellValue("占比");
		cell02.setCellStyle(cs);
		
		for(int m=0;m<hmtidlist.size();m++){
			row02 = sheet2.createRow(m+count+1);
			
			HashMap map=(HashMap)hmtidlist.get(m);
			
			cell02 = row02.createCell((short)0);
			//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell02.setCellValue(m+1);
			
			cell02 = row02.createCell((short)1);
			//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell02.setCellValue((String)map.get("hmtIdName"));
			
			cell02 = row02.createCell((short)2);
			//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell02.setCellValue((String)map.get("FaultCode"));
			
			cell02 = row02.createCell((short)3);
			//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell02.setCellValue(Double.parseDouble((String)map.get("fatnum")));
			
			cell02 = row02.createCell((short)4);
			//cell02.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell02.setCellValue((String)map.get("zhanbi"));
			
		}
		
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("项目(电梯)故障分析表", "utf-8") + ".xlsx");
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
