package com.gzunicorn.struts.action.hotlinemanagement.hotcalloutmaster;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.pdfprint.BarCodePrint;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.elevatorcoordinatelocation.ElevatorCoordinateLocation;
import com.gzunicorn.hibernate.basedata.hotlinefaulttype.HotlineFaultType;
import com.gzunicorn.hibernate.hotlinemanagement.calloutmaster.CalloutMaster;
import com.gzunicorn.hibernate.hotlinemanagement.calloutprocess.CalloutProcess;
import com.gzunicorn.hibernate.hotlinemanagement.calloutsms.CalloutSms;
import com.gzunicorn.hibernate.hotlinemanagement.smshistory.SmsHistory;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.struts.action.xjsgg.SmsService;
import com.gzunicorn.struts.action.xjsgg.XjsggAction;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class hotCalloutMasterjx  extends DispatchAction{
	XjsggAction xj=new XjsggAction();
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
	 * 系统调用母方法
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		
		String name = request.getParameter("method");
		String typejsp = request.getParameter("typejsp");
		
		if(!"toDownloadFileRecord".equals(name) && !"toDisplayRecord".equals(name) 
				&& (typejsp!=null && !typejsp.equals("display")) ){
			/** **********开始用户权限过滤*********** */
			SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "hotphone", null);
			/** **********结束用户权限过滤*********** */
		}
		if ((typejsp==null || typejsp.equals("")) && (name == null || name.equals(""))) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			if (typejsp!=null && typejsp.equals("add")) {
				name = "toAddRecord";
				return dispatchMethod(mapping, form, request, response, name);
			}else if(typejsp!=null && (typejsp.equals("display")|| typejsp.equals("sh") || typejsp.equals("ps"))){
				name = "toDisplayRecord";
				return dispatchMethod(mapping, form, request, response, name);	
			}else if(typejsp!=null && typejsp.equals("mondity")){
				name = "toUpdateRecord";
				return dispatchMethod(mapping, form, request, response, name);	
			}else if(typejsp!=null && (typejsp.equals("shsave")||typejsp.equals("pssave"))){
				name = "tosaveshps";
				return dispatchMethod(mapping, form, request, response, name);	
			}else if(typejsp!=null &&  typejsp.equals("toSearchRecord")){
				name = "toSearchRecord";
				return dispatchMethod(mapping, form, request, response, name);	
			}				
			ActionForward forward = super.execute(mapping, form, request,response);
			return forward;
		}

	}
	
	
	/**
	 * 急修查询方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toSearchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("navigator.location","急修流程管理 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
        List list2=new ArrayList();
        List list3=new ArrayList();
        List PulldownList=null;
        List RepairModeList=null;
		List ServiceObjectsList=null;
        List storageidList=null;
		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form,request,response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport","");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "CalloutMasterList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fCalloutMaster");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("calloutMasterNo");
			table.setIsAscending(false);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			}else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			}else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			String companyName = tableForm.getProperty("companyId");// 保修单位名称
			if(companyName!=null && !"".equals(companyName)){
				companyName="%"+companyName.trim()+"%";
			}else{
				companyName="%";
			}
			String calloutMasterNo = tableForm.getProperty("calloutMasterNo");// 急修编号
			if(calloutMasterNo!=null && !"".equals(calloutMasterNo)){
				calloutMasterNo="%"+calloutMasterNo.trim()+"%";
			}else{
				calloutMasterNo="%";
			}
			String operId = tableForm.getProperty("operId");// 起草人
			if(operId!=null && !"".equals(operId)){
				operId="%"+operId.trim()+"%";
			}else{
				operId="%";
			}
			String handleStatus = tableForm.getProperty("handleStatus");// 处理状态
			if(handleStatus==null){
				handleStatus="%";
			}

			String submitType = tableForm.getProperty("SubmitType");// 提交标志
			if(submitType!=null && !"".equals(submitType)){
				submitType="%"+submitType.trim()+"%";
			}else{
				submitType="%";
			}
			
			String maintStation = tableForm.getProperty("maintStation");// 维保站
			if(maintStation!=null && !"".equals(maintStation)){
				maintStation="%"+maintStation.trim()+"%";
			}else{
				maintStation="%";
			}
			String repairMode = tableForm.getProperty("repairMode");// 报修方式
			if(repairMode!=null && !"".equals(repairMode)){
				repairMode="%"+repairMode.trim()+"%";
			}else{
				repairMode="%";
			}
			String serviceObjects = tableForm.getProperty("serviceObjects");// 服务对象
			if(serviceObjects!=null && !"".equals(serviceObjects)){
				serviceObjects="%"+serviceObjects.trim()+"%";
			}else{
				serviceObjects="%";
			}

			String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号
			if(elevatorNo!=null && !"".equals(elevatorNo)){
				elevatorNo="%"+elevatorNo.trim()+"%";
			}else{
				elevatorNo="%";
			}
			String r5 = tableForm.getProperty("r5");//维修人员
			if(r5!=null && !"".equals(r5)){
				r5="%"+r5.trim()+"%";
			}else{
				r5="%";
			}
			
			String sdate1=(String) tableForm.getProperty("sdate1");
			String edate1=(String) tableForm.getProperty("edate1");
			if((sdate1==null || sdate1.trim().equals("")) 
					&& (edate1==null || edate1.trim().equals(""))){
				String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
				String day1=DateUtil.getDate(day, "MM", -2);//当前日期月份加1 。
				//String day2=DateUtil.getDate(day, "MM", +1);//当前日期月份加1 。
				sdate1=day1;
				edate1=day;
				tableForm.setProperty("sdate1", day1);
				tableForm.setProperty("edate1", day);
			}
			
			String orderby="";
			 if(table.getIsAscending()){
				 orderby=" order by  case when handleStatus='3' then '0' else '1' end,"+table.getSortColumn()+"";
             }else{
            	 orderby=" order by case when handleStatus='3' then '0' else '1' end,"+table.getSortColumn()+" desc";
             }
				
			Session hs = null;
			Connection con=null;
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {

				hs = HibernateUtil.getSession();
				con=hs.connection();
				
				//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
				String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
				List vmlist=hs.createSQLQuery(sqlss).list();
				if(vmlist!=null && vmlist.size()>0){
					maintStation=userInfo.getStorageId();
				}
				
				String comid=userInfo.getComID();
				if("00".equals(comid)){
					comid="%";
				}
				
				String sql="exec HL_callhotsearch_new ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
				ps=con.prepareStatement(sql);
				ps.setString(1, companyName);
				ps.setString(2, calloutMasterNo);
				ps.setString(3, operId);
				ps.setString(4, handleStatus);
				ps.setString(5, submitType);
				ps.setString(6, maintStation);
				ps.setString(7, repairMode);
				ps.setString(8, serviceObjects);
				ps.setString(9, orderby);
				ps.setString(10, "1");
				ps.setString(11, elevatorNo);
				ps.setString(12, comid);
				ps.setString(13, r5);
				ps.setString(14, sdate1);
				ps.setString(15, edate1);
				rs=ps.executeQuery();
				//System.out.println(">>>"+ps.toString());
				int i=0;
				int FirstResult=table.getFrom();
				int MaxResults=table.getFrom()+table.getLength();
               while(rs.next()){
            	   if(i>=FirstResult && i<MaxResults){
            		   HashMap hm=new HashMap();
            		   hm.put("calloutMasterNo", rs.getString(1));
            		   hm.put("companyName", rs.getString(2));
            		   hm.put("operName", rs.getString(3));
            		   String handle=rs.getString(4);
            		   hm.put("handleStatus", handle);       		   
            		   if(!handle.equals("5") && !handle.equals("6")){
            			   hm.put("handle", "Y");   
            		   }else{
            			   hm.put("handle", "N");   
            		   }        		   
            		   hm.put("handleStatusName", rs.getString("handleStatusname"));
            		   hm.put("isTrap", rs.getString(5).equals("Y")?"困人":"非困人");            		              		        
            		   hm.put("maintStation", rs.getString(6));
            		   hm.put("repairMode", rs.getString(7));
            		   hm.put("serviceObjects", rs.getString(8));
            		   hm.put("completeTime", rs.getString(9));
            		   String isStop=String.valueOf(rs.getString(10));
            		   if(isStop!=null && "Y".equals(isStop)){
            			   hm.put("isStop","停梯" );
            		   }else{
            			   hm.put("isStop", isStop.equals("N")?"非停梯":"");  
            		   }
            		   hm.put("submitType", rs.getString(11).equals("Y")?"已提交":"未提交");
            		   hm.put("operDate", rs.getString(12));
            		   hm.put("elevatorNo", rs.getString("ElevatorNo"));

      	           	   /**
            		   String r5id=rs.getString("r5");//急修参与人员
      	           	   String r5name="";
      	           	   if(r5id!=null && !r5id.trim().equals("")){
      	           		 String sqls="select a from Loginuser a where a.userid in('"+r5id.replaceAll(",", "','")+"')";
      	           		 List loginlist=hs.createQuery(sqls).list();
      	           		 if(loginlist!=null && loginlist.size()>0){
      	           			 for(int l=0;l<loginlist.size();l++){
      	           				 Loginuser login=(Loginuser)loginlist.get(l);
      	           				 if(l==loginlist.size()-1){
      	           					 r5name+=login.getUsername();
      	           				 }else{
      	           					 r5name+=login.getUsername()+",";
      	           				 }
      	           			 }
      	           		 }
      	           	 }
      	           	 */
      	           	 hm.put("r5name", rs.getString("r5name"));//维修人员
            		   
            		 list2.add(hm);  
            	   }
            	   i++;
               }
               
               table.setVolume(i);// 查询得出数据记录数;
			   cache.check(table);
			    list3.addAll(list2);				
				table.addAll(list3);

				//处理状态
				PulldownList=hs.createQuery("select a from Pulldown a where "
						+ "a.id.typeflag='CalloutMaster_HandleStatus' and a.enabledflag='Y' order by orderby").list();
				//报修方式
				RepairModeList=hs.createQuery("select a from Pulldown a where "
						+ "a.id.typeflag='CalloutMaster_RepairMode' and a.enabledflag='Y' order by orderby").list();
				//服务对象
				ServiceObjectsList=hs.createQuery("select a from Pulldown a where "
						+ "a.id.typeflag='CalloutMaster_ServiceObjects' and a.enabledflag='Y' order by orderby").list();
				//维保站
				storageidList=bd.getMaintStationList(userInfo);
				
				request.setAttribute("rmList", RepairModeList);
				request.setAttribute("soList", ServiceObjectsList);
				session.setAttribute("CalloutMasterList", table);
				request.setAttribute("PulldownList", PulldownList);
				request.setAttribute("storageidList", storageidList);
		    } catch (DataStoreException e) {
				e.printStackTrace();
			} catch (Exception e1) {

				e1.printStackTrace();
			} finally {
				try {
					hs.close();
					// HibernateSessionFactory.closeSession();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			forward = mapping.findForward("toList");
		}
		return forward;
}
	
	/**
	 * 新建急修流程所调用的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toPrepareAddRecord(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		request.setAttribute("navigator.location","急修流程管理 >> 新建");
		DynaActionForm dform = (DynaActionForm) form;
		HttpSession session=request.getSession();
		List RepairModeList=null;
		List ServiceObjectsList=null;
		 List storageidList=null;
		Session hs=null;
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		CalloutMaster cm=new CalloutMaster();
		cm.setOperId(userInfo.getUserName());
		cm.setRepairTime(xj.getdatetime());
		cm.setServiceObjects(String.valueOf(request.getAttribute("serviceObjects")));
		request.setAttribute("CalloutMasterList", cm);
		try {
			hs=HibernateUtil.getSession();
			//报修方式
			RepairModeList=hs.createQuery("select a from Pulldown a where "
					+ "a.id.typeflag='CalloutMaster_RepairMode' and a.enabledflag='Y' order by orderby").list();
			//服务对象
			ServiceObjectsList=hs.createQuery("select a from Pulldown a where "
					+ "a.id.typeflag='CalloutMaster_ServiceObjects' and a.enabledflag='Y' order by orderby").list();
			
			//维保站
			storageidList=bd.getMaintStationList(userInfo.getComID());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("rmList", RepairModeList);
		request.setAttribute("soList", ServiceObjectsList);
		request.setAttribute("storageidList", storageidList);
        request.setAttribute("typejsp", "add");
		return mapping.findForward("tohotphone");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	public ActionForward toAddRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);		
		Session hs = null;
		Transaction tx = null;
		CalloutMaster cm = null;
		CalloutSms cs=null;
		SmsHistory sh=null;
		String isreturn=request.getParameter("isreturn");
		String calloutMasterNo="";//急修单号
		String serviceObjects="";
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			cm = new CalloutMaster();	
			String datetime = request.getParameter("repairTime");//报修时间
			String repairMode = request.getParameter("repairMode");//报修方式
			String repairUser = request.getParameter("repairUser");//报修人
			String repairTel = request.getParameter("repairTel");//报修电话
			if(repairUser==null){repairUser="";};
			if(repairTel==null){repairTel="";};
			serviceObjects = request.getParameter("serviceObjects");//服务对象
			String companyId = request.getParameter("companyId");//报修单位
			String projectAddress = request.getParameter("projectAddress");//项目地址
			String salesContractNo = request.getParameter("salesContractNo");//销售合同号
			String elevatorNo = request.getParameter("elevatorNo");//电梯编号
			String elevatorParam = request.getParameter("elevatorParam");//规格型号
			//String maintStation = request.getParameter("maintStation");//维保站
			String maintStation = request.getParameter("assignMaintStation");//维保站
			String assignObject = request.getParameter("assignObject");//派工对象
			String phone = request.getParameter("phone");//派工电话
			String isTrap = request.getParameter("isTrap");//是否困人
			String repairDesc = request.getParameter("repairDesc");//报修描述
			String isSendSms2=request.getParameter("isSendSms2");
			String projectName=request.getParameter("projectName");
			
			calloutMasterNo=XjsggAction.genCalloutMasterNum();//新建急修单号
            cm.setCalloutMasterNo(calloutMasterNo);
            cm.setOperId(userInfo.getUserID());
            cm.setOperDate(datetime);
            cm.setRepairTime(datetime);
            cm.setRepairMode(repairMode);
            cm.setRepairUser(repairUser);
            cm.setRepairTel(repairTel);
            cm.setServiceObjects(serviceObjects);
            cm.setCompanyId(companyId);
            cm.setProjectAddress(projectAddress);
            cm.setSalesContractNo(salesContractNo);
            cm.setElevatorNo(elevatorNo);
            cm.setElevatorParam(elevatorParam);
            cm.setMaintStation(maintStation);
            cm.setAssignObject(assignObject);
            cm.setPhone(phone);
            cm.setIsTrap(isTrap);
            cm.setRepairDesc(repairDesc);
            cm.setIsSendSms2(isSendSms2);
            cm.setProjectName(projectName);
            
            if("Y".equals(isreturn)){
            	cm.setSubmitType("Y");
            	if(assignObject==null || "".equals(assignObject)){
            		cm.setHandleStatus("6");//处理状态	
	            }else{
	            	
	            	/****************************发送短信给维保工 开始**********************************/
	            	System.out.println(">>>发送短信给维保工");
	            	String istraptext="非困人";
	            	if(isTrap!=null && isTrap.equals("Y")){
	            		istraptext="困人";
	            	}
	            	String smsmes="困人情况："+istraptext+"，电梯编号："+elevatorNo+"，项目名称及楼栋号："+projectAddress+"。您保养的电梯有故障，请及时处理！ [西继迅达]";
	            	boolean issms=SmsService.sendSMS(istraptext,elevatorNo,projectAddress,phone);
	            	
	            	//存入历史表
					sh=new SmsHistory();
					sh.setSmsContent(smsmes);
					sh.setSmsSendTime(CommonUtil.getNowTime());
					sh.setSmsTel(phone);
					if(issms){
						sh.setFlag(1);
					}else{
						sh.setFlag(0);	
					}
					hs.save(sh);
					hs.flush();
					/****************************发送短信给维保工 结束**********************************/
	
	            	cm.setHandleStatus("");//处理状态
	            	String username=bd.getName("Loginuser", "username", "userid", assignObject);
		            /****************************发送短信开始**********************************/	
		            if(isSendSms2!=null && "Y".equals(isSendSms2)){
		            	String pattern = "^(13[0-9]|15[01]|153|15[6-9]|180|18[23]|18[5-9])\\d{8}$";
		            	if(repairTel!=null && repairTel.matches(pattern)){
							//String smsContent="西继迅达电梯: 您好,我们已派 维保工"+username+",电话"+phone+" 到现场提供急修服务。请您耐心等待。";
							String smsContent="尊敬的用户，我们已经收到您的报修并通知了维修人员["+username+","+phone+"]，他很快就会到达现场，请您安心等待，不要刻意扒动电梯门。 ";
							String telNo=cm.getRepairTel();
							
							System.out.println(">>>发送安抚短信");
							boolean iscg=true;
							//boolean iscg=SmsService.sendSMS(smsContent, telNo);
							//boolean iscg=XjsggAction.sendMessage(smsContent, telNo);
							
							//存入急修短信表
							String time=xj.getdatetime();
							cs=new CalloutSms();
							cs.setCalloutMasterNo(calloutMasterNo);
							cs.setSmsTel(telNo);//安抚电话
							cs.setSmsSendTime(time);//安抚发送短信时间
							cs.setSmsContent(smsContent);//安抚短信内容
							hs.save(cs);
							hs.flush();
							
							//存入历史表
							sh=new SmsHistory();
							sh.setSmsContent(smsContent);
							sh.setSmsSendTime(time);
							sh.setSmsTel(telNo);
							if(iscg){
								sh.setFlag(1);
							}else{
								sh.setFlag(0);	
							}
							hs.save(sh);
							hs.flush();
		            	}else{
		            		//System.out.println("报修电话不是手机号码，不能发送信息！");
		            		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","报修电话不是手机号码，不能发送信息！"));
		            	}
		            }
		            /***********************发送短信结束************************************/										
		
		         }
            }else if("N".equals(isreturn)){
	            cm.setSubmitType("N");
	            cm.setHandleStatus("");//处理状态
            }
            hs.save(cm);
            hs.flush();
            
            CalloutProcess cp=new CalloutProcess();
            cp.setCalloutMasterNo(calloutMasterNo);
            cp.setAssignObject2(assignObject);
            hs.save(cp);
            
			tx.commit();		
		} catch (Exception e1) {
			tx.rollback();
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Insert error!");
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","保存失败！"));
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = null;
		try {				
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"insert.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
							
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}
		if("Y".equals(isreturn)){
			forward = mapping.findForward("returnList");
		}else if("N".equals(isreturn)){
			request.setAttribute("serviceObjects", serviceObjects);
			forward = mapping.findForward("returnAdd");
		}
		return forward;
	}
	/**
	 * 点击查看的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {		
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		String id = (String) dform.get("id");
		if(id==null && "".equals(id)){
			id=request.getParameter("id");
		}
		String typejsp = request.getParameter("typejsp");
		
		//查看界面显示关闭按钮
		String isopenshow = request.getParameter("isopenshow");
		if(isopenshow!=null && isopenshow.equals("Yes")){
			request.setAttribute("isopenshow", isopenshow);
		}
		HashMap hm=new HashMap();
		Session hs = null;
		CalloutMaster cm=null;
		CalloutProcess cp=null;
		CalloutSms cs=null;
		CalloutSms sms=null;
		String CSheight = PropertiesUtil.getProperty("CSheight");
		String CSwidth = PropertiesUtil.getProperty("CSwidth");
		String CIheight = PropertiesUtil.getProperty("CIheight");
		String CIwidth = PropertiesUtil.getProperty("CIwidth");
	   if (id != null) {				
		try {
			hs = HibernateUtil.getSession();
			cm=(CalloutMaster)hs.get(CalloutMaster.class, id);
			String sqlsel="select "
					+ "l.username as username1,"
					+ "p.pullname as pullname,"
					+ "p1.pullname as pullname1,"
					+ "c.companyName as companyName,"
					+ "s.storagename as storagename,"
					+ "l2.username as username2,"
					+ "e.rem as rem,"
					+ "l3.username as username3,"
					+ "h.hfcName as hfcName,"
					+ "l4.username as username4,"
					+ "l5.username as username5 "
					+ "from CalloutMaster a "
					+ "left join Loginuser l on l.userid=a.operId "
					+ "left join Pulldown p on p.typeflag='CalloutMaster_RepairMode' and p.pullid=a.repairMode "
					+ "left join Pulldown p1 on p1.typeflag='CalloutMaster_ServiceObjects' and p1.pullid=a.serviceObjects "
					+ "left join Customer c on c.companyId=a.companyId "
					+ "left join Storageid s on s.storageid=a.maintStation "
					+ "left join Loginuser l2 on l2.userid=a.assignObject "
					+ "left join ElevatorCoordinateLocation e on e.elevatorNo=a.elevatorNo "
					+ "left join Loginuser l3 on l3.userid=a.auditOperid "
					+ "left join HotlineFaultClassification h on h.hfcId=a.hfcId "
					+ "left join Loginuser l4 on l4.userid=a.smAuditOperid "
					+ "left join Loginuser l5 on l5.userid=a.stophfOperid "
					+ "where a.calloutMasterNo='"+id.trim()+"' ";
			System.out.println(sqlsel);
			
			List cmlist=hs.createSQLQuery(sqlsel).list();
			Object[] cmobj=(Object[])cmlist.get(0);

			cm.setOperId((String)cmobj[0]);
			if(!"sh".equals(typejsp)){
				cm.setRepairMode((String)cmobj[1]);//报修方式
				cm.setServiceObjects((String)cmobj[2]);//服务对象
				cm.setIsTrap(String.valueOf(cm.getIsTrap()).equals("Y")?"困人":"非困人");
			}
			String companyName=(String)cmobj[3];
			if(companyName==null || "".equals(companyName) || "NULL".equals(companyName)){
				companyName=cm.getCompanyId();
			}
			cm.setCompanyId(companyName);
			cm.setMaintStation((String)cmobj[4]);
			cm.setAssignObject((String)cmobj[5]);
			cm.setProjectAddress((String)cmobj[6]);//项目名称及楼栋号
			
			
			if(typejsp!=null && "sh".equals(typejsp)){
				cm.setAuditOperid(userInfo.getUserName());
				cm.setAuditDate(xj.getdatetime());
				request.setAttribute("navigator.location","急修流程管理 >> 急修审核");
				List HotlineFaultClassificationList=xj.getClasses(hs, "HotlineFaultClassification", "enabledFlag", "Y");
				request.setAttribute("HotlineFaultClassificationList", HotlineFaultClassificationList);
				request.setAttribute("rmList", bd.getPullDownList("CalloutMaster_RepairMode"));
				request.setAttribute("soList", bd.getPullDownList("CalloutMaster_ServiceObjects"));
			}else if(typejsp!=null && "ps".equals(typejsp)){
				request.setAttribute("navigator.location","急修流程管理 >> 回访审核");
				cm.setAuditOperid((String)cmobj[7]);
				cm.setHfcId((String)cmobj[8]);//故障分类
			}else{
				typejsp="display";
				cm.setAuditOperid((String)cmobj[7]);
				request.setAttribute("navigator.location","急修流程管理 >> 查看");
				cm.setHfcId((String)cmobj[8]);//故障分类
			}

			cm.setSmAuditOperid((String)cmobj[9]);//安全经理审核
			cm.setStophfOperid((String)cmobj[10]);//停梯回访人
			
			String IsSendSms=cm.getIsSendSms();
			if(IsSendSms!=null && !"".equals(IsSendSms)){
				cm.setIsSendSms(String.valueOf(cm.getIsSendSms()).equals("Y")?"已发送":"未发送");
			}else{
				cm.setIsSendSms("");
			}	
			String isSubSM=cm.getIsSubSM();
			if(isSubSM!=null && !"".equals(isSubSM)){
				cm.setIsSubSM(String.valueOf(cm.getIsSubSM()).equals("Y")?"是":"否");
			}else{
				cm.setIsSubSM("");
			}
			String ServiceAppraisal=cm.getServiceAppraisal();
			if(ServiceAppraisal!=null && !"".equals(ServiceAppraisal)){
				switch(Integer.valueOf(ServiceAppraisal).intValue()){
				case 1:cm.setServiceAppraisal("非常满意");break;
				case 2:cm.setServiceAppraisal("满意");break;
				case 3:cm.setServiceAppraisal("一般");break;
				case 4:cm.setServiceAppraisal("不满意");break;
				case 5:cm.setServiceAppraisal("非常不满意");break;
				}
			}
			String FittingSituation=cm.getFittingSituation();
			if(FittingSituation!=null && !"".equals(FittingSituation)){
				cm.setFittingSituation(String.valueOf(cm.getFittingSituation()).equals("1")?"属实":"不属实");
			}else{
				cm.setFittingSituation("");
			}
			String TollSituation=cm.getTollSituation();
			if(TollSituation!=null && !"".equals(TollSituation)){
				cm.setTollSituation(String.valueOf(cm.getTollSituation()).equals("1")?"属实":"不属实");
			}else{
				cm.setTollSituation("");
			}
			String IsColse=cm.getIsColse();
			if(IsColse!=null && !"".equals(IsColse)){
				cm.setIsColse(String.valueOf(cm.getIsColse()).equals("1")?"关闭":"不关闭");
			}else{
				cm.setIsColse("");
			}

			cp=(CalloutProcess)hs.get(CalloutProcess.class, id);
			String sqlpro="select "
					+ "lu.username as username,"
					+ "lu2.username as username2,"
					+ "h.hmtName as hmtName "
					+ "from CalloutProcess p "
					+ "left join Loginuser lu on lu.userid=p.turnSendId "
					+ "left join Loginuser lu2 on lu2.userid=p.assignUser "
					+ "left join HotlineMotherboardType h on h.hmtId=p.hmtId "
					+ "where p.calloutMasterNo='"+id.trim()+"' ";
			List cplist=hs.createSQLQuery(sqlpro).list();
			
			if(cp!=null){
				Object[] objpro=(Object[])cplist.get(0);
				
				cp.setTurnSendId((String)objpro[0]);
				cp.setAssignUser((String)objpro[1]);
				String IsStop=cp.getIsStop();
				if(!"sh".equals(typejsp)){
					if(IsStop!=null && !"".equals(IsStop)){
						cp.setIsStop(IsStop.equals("Y")?"停梯":"非停梯");
					}else{
						cp.setIsStop("");	
					}
					
					String hftid=cp.getHftId();//故障类型
		           	 String hftdesc="";
		           	 if(hftid!=null && !hftid.trim().equals("")){
		           		 String sqls="select a from HotlineFaultType a where a.hftId in('"+hftid.replaceAll(",", "','")+"')";
		           		 List loginlist=hs.createQuery(sqls).list();
		           		 if(loginlist!=null && loginlist.size()>0){
		           			 for(int l=0;l<loginlist.size();l++){
		           				HotlineFaultType hft=(HotlineFaultType)loginlist.get(l);
		           				 if(l==loginlist.size()-1){
		           					hftdesc+=hft.getHftDesc();
		           				 }else{
		           					hftdesc+=hft.getHftDesc()+",";
		           				 }
		           			 }
		           		 }
		           	 }
		           	cp.setHftId(hftdesc);
					
					cp.setHmtId((String)objpro[2]);//主板类型
				}else{
					String hftid=cp.getHftId();//急修参与人员
		           	 String hftdesc="";
		           	 if(hftid!=null && !hftid.trim().equals("")){
		           		 String sqls="select a from HotlineFaultType a where a.hftId in('"+hftid.replaceAll(",", "','")+"')";
		           		 List loginlist=hs.createQuery(sqls).list();
		           		 if(loginlist!=null && loginlist.size()>0){
		           			 for(int l=0;l<loginlist.size();l++){
		           				HotlineFaultType hft=(HotlineFaultType)loginlist.get(l);
		           				 if(l==loginlist.size()-1){
		           					hftdesc+=hft.getHftDesc();
		           				 }else{
		           					hftdesc+=hft.getHftDesc()+",";
		           				 }
		           			 }
		           		 }
		           	 }
		           	request.setAttribute("hftId", hftdesc);
					
					request.setAttribute("hmtId", (String)objpro[2]);
				}
				
				
				String IsToll=cp.getIsToll();
				if(IsToll!=null && !"".equals(IsToll)){
					cp.setIsToll(String.valueOf(cp.getIsToll()).equals("Y")?"收费":"非收费");
				}else{
					cp.setIsToll("");
				} 
				
				//Double ArriveLongitude=cp.getArriveLongitude();
				//Double ArriveLatitude=cp.getArriveLatitude();
				//Double FninishLongitude=cp.getFninishLongitude();
				//Double FninishLatitude=cp.getFninishLatitude();
				/**
				String r5=cp.getR5();//急修参与人员
	           	 String r5name="";
	           	 if(r5!=null && !r5.trim().equals("")){
	           		 String sqls="select a from Loginuser a where a.userid in('"+r5.replaceAll(",", "','")+"')";
	           		 List loginlist=hs.createQuery(sqls).list();
	           		 if(loginlist!=null && loginlist.size()>0){
	           			 for(int l=0;l<loginlist.size();l++){
	           				 Loginuser login=(Loginuser)loginlist.get(l);
	           				 if(l==loginlist.size()-1){
	           					 r5name+=login.getUsername();
	           				 }else{
	           					 r5name+=login.getUsername()+",";
	           				 }
	           			 }
	           		 }
	           	 }
	           	 */
	           	hm.put("r5name",cp.getR5());//急修参与人员

				hm.put("elevatorLocation",cp.getArriveLocation());
				hm.put("elevatorLocation2", cp.getFninishLocation());
			}
			cs=(CalloutSms)hs.get(CalloutSms.class, id);
			if(cs!=null){
			String ServiceRating=cs.getServiceRating();//用户评价
			if(ServiceRating!=null && !"".equals(ServiceRating)){
				switch(Integer.valueOf(ServiceRating).intValue()){
				case 1:cs.setServiceRating("非常满意");break;
				case 2:cs.setServiceRating("满意");break;
				case 3:cs.setServiceRating("一般");break;
				case 4:cs.setServiceRating("不满意");break;
				case 5:cs.setServiceRating("非常不满意");break;
				}
			}
			}
			sms=(CalloutSms) hs.get(CalloutSms.class, cm.getCalloutMasterNo());
			
			Query queryfiles = hs.createQuery("from CalloutFileinfo ");
			List listfiles = queryfiles.list();
			request.setAttribute("CalloutFileinfo", listfiles);
			
		
		} catch (DataStoreException e) {
				e.printStackTrace();
			}				
		
	}else{
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
	}

	//维保站
	List storageidList=bd.getMaintStationList(userInfo.getComID());
	request.setAttribute("storageidList", storageidList);
	
    request.setAttribute("CalloutMasterList", cm);
    request.setAttribute("CalloutProcessList", cp);
    request.setAttribute("CalloutSmsList", cs);
    request.setAttribute("hashmapbean", hm);
    request.setAttribute("calloutSmsBean", sms);
	request.setAttribute("typejsp",typejsp);
	request.setAttribute("CSheight",CSheight);
	request.setAttribute("CSwidth", CSwidth);
	request.setAttribute("CIheight", CIheight);
	request.setAttribute("CIwidth", CIwidth);
	if (!errors.isEmpty()) {
		saveErrors(request, errors);
	}
		forward = mapping.findForward("tohotphone");
		return forward;
	}
	
	/**
	 * 跳转到修改级别页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","急修流程管理 >> 修改");	
		DynaActionForm dform = (DynaActionForm) form;
		HttpSession session=request.getSession();		
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		String id = (String) dform.get("id");
		if(id==null && "".equals(id)){
			id=request.getParameter("id");
		}
		String error = (String) request.getAttribute("error");
		Session hs = null;
		CalloutMaster cm=null;
		List RepairModeList=null;
		List ServiceObjectsList=null;
		List storageidList=null;
		HashMap hm=null;
		if (id != null) {
				try {
					hs = HibernateUtil.getSession();
					cm=(CalloutMaster)hs.get(CalloutMaster.class, id);
					cm.setOperId(userInfo.getUserName());
					cm.setRepairTime(xj.getdatetime());
					cm.setProjectAddress(bd.getName("ElevatorCoordinateLocation", "rem", "elevatorNo", cm.getElevatorNo()));//项目名称及楼栋号
					
					hm=new HashMap();
					String companyName=bd.getName("Customer", "companyName", "companyId", cm.getCompanyId());
					if(companyName==null || "".equals(companyName)){
						companyName=cm.getCompanyId();
					}
					hm.put("companyName", companyName);//报修单位
					hm.put("storageName", bd.getName("Storageid", "storagename", "storageid", cm.getMaintStation()));//维保站
					//报修方式
					RepairModeList=hs.createQuery("select a from Pulldown a where "
							+ "a.id.typeflag='CalloutMaster_RepairMode' and a.enabledflag='Y' order by orderby").list();
					//服务对象
					ServiceObjectsList=hs.createQuery("select a from Pulldown a where "
							+ "a.id.typeflag='CalloutMaster_ServiceObjects' and a.enabledflag='Y' order by orderby").list();
					//维保站
					storageidList=bd.getMaintStationList(userInfo.getComID());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			
		}else{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
		}
        request.setAttribute("CalloutMasterList",cm);
        request.setAttribute("hashmapbean", hm);
        request.setAttribute("rmList", RepairModeList);
		request.setAttribute("soList", ServiceObjectsList);
		request.setAttribute("storageidList", storageidList);
		request.setAttribute("typejsp", "mondity");
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		forward = mapping.findForward("tohotphone");
		return forward;
	}
	
	/**
	 * 紧急级别修改
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toUpdateRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;
		CalloutMaster cm = null;
		CalloutSms cs=null;
		SmsHistory sh=null;
		String calloutMasterNo=request.getParameter("calloutMasterNo");
		String isreturn=request.getParameter("isreturn");
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();				
			String datetime = request.getParameter("repairTime");//报修时间
			String repairMode = request.getParameter("repairMode");//报修方式
			String repairUser = request.getParameter("repairUser");//报修人
			String repairTel = request.getParameter("repairTel");//报修电话
			String serviceObjects = request.getParameter("serviceObjects");//服务对象
			String companyId = request.getParameter("companyId");//报修单位
			String projectAddress = request.getParameter("projectAddress");//项目地址
			String salesContractNo = request.getParameter("salesContractNo");//销售合同号
			String elevatorNo = request.getParameter("elevatorNo");//电梯编号
			String elevatorParam = request.getParameter("elevatorParam");//规格型号
			//String maintStation = request.getParameter("maintStation");//维保站
			String maintStation = request.getParameter("assignMaintStation");//维保站
			String assignObject = request.getParameter("assignObject");//派工对象
			String phone = request.getParameter("phone");//派工电话
			String isTrap = request.getParameter("isTrap");//是否困人
			String repairDesc = request.getParameter("repairDesc");//报修描述
			String isSendSms2=request.getParameter("isSendSms2");
			
			cm=(CalloutMaster)hs.get(CalloutMaster.class, calloutMasterNo);
            cm.setOperId(userInfo.getUserID());
            cm.setOperDate(datetime);
            cm.setRepairTime(datetime);
            cm.setRepairMode(repairMode);
            cm.setRepairUser(repairUser);
            cm.setRepairTel(repairTel);
            cm.setServiceObjects(serviceObjects);
            cm.setCompanyId(companyId);
            cm.setProjectAddress(projectAddress);
            cm.setSalesContractNo(salesContractNo);
            cm.setElevatorNo(elevatorNo);
            cm.setElevatorParam(elevatorParam);
            cm.setMaintStation(maintStation);
            cm.setAssignObject(assignObject);
            cm.setPhone(phone);
            cm.setIsTrap(isTrap);
            cm.setRepairDesc(repairDesc);
            
            if("Y".equals(isreturn)){
            	cm.setSubmitType("Y");
	            if(assignObject==null ||"".equals(assignObject)){
	                cm.setHandleStatus("6");//处理状态	
		        }else{
		        	/****************************发送短信给维保工 开始**********************************/
	            	System.out.println(">>>发送短信给维保工");
	            	String istraptext="非困人";
	            	if(isTrap!=null && isTrap.equals("Y")){
	            		istraptext="困人";
	            	}
	            	String smsmes="困人情况："+istraptext+"，电梯编号："+elevatorNo+"，项目名称及楼栋号："+projectAddress+"。您保养的电梯有故障，请及时处理！ [西继迅达]";
	            	boolean issms=SmsService.sendSMS(istraptext,elevatorNo,projectAddress,phone);
	            	
	            	//存入历史表
					sh=new SmsHistory();
					sh.setSmsContent(smsmes);
					sh.setSmsSendTime(CommonUtil.getNowTime());
					sh.setSmsTel(phone);
					if(issms){
						sh.setFlag(1);
					}else{
						sh.setFlag(0);	
					}
					hs.save(sh);
					hs.flush();
					/****************************发送短信给维保工 结束**********************************/
	            	
	                cm.setHandleStatus("");//处理状态
	                String username=bd.getName("Loginuser", "username", "userid", assignObject);
	                /***************发送短信开始***********************/
	                if(isSendSms2!=null && "Y".equals(isSendSms2)){
	                	String pattern = "^(13[0-9]|15[01]|153|15[6-9]|180|18[23]|18[5-9])\\d{8}$";
	                	if(repairTel!=null && repairTel.matches(pattern)){
			    			//String smsContent="西继迅达电梯: 您好,我们已派 维保工"+username+",电话"+phone+" 到现场提供急修服务。请您耐心等待。";
							String smsContent="尊敬的用户，我们已经收到您的报修并通知了维修人员["+username+","+phone+"]，他很快就会到达现场，请您安心等待，不要刻意扒动电梯门。 ";
			    			String telNo=cm.getRepairTel();
			    			
			    			System.out.println(">>>发送安抚短信");
			    			boolean iscg=true;
			    			//boolean iscg=SmsService.sendSMS(smsContent, telNo);
			    			//boolean iscg=XjsggAction.sendMessage(smsContent, telNo);
			    			
			    			//存入急修短信表
			    			String time=xj.getdatetime();
			    			cs=new CalloutSms();
			    			cs.setCalloutMasterNo(calloutMasterNo);
			    			cs.setSmsTel(telNo);//安抚电话
			    			cs.setSmsSendTime(time);//安抚发送短信时间
			    			cs.setSmsContent(smsContent);//安抚短信内容
			    			hs.save(cs);
			    			hs.flush();
			    			
			    			//存入历史表
			    			sh=new SmsHistory();
			    			sh.setSmsContent(smsContent);
			    			sh.setSmsSendTime(time);
			    			sh.setSmsTel(telNo);
			    			if(iscg){
			    				sh.setFlag(1);
			    			}else{
			    				sh.setFlag(0);	
			    			}
			    			hs.save(sh);
			    			hs.flush();
			    			/********************发送短信结束****************************************/
	                	}else{
	                		//System.out.println("报修电话不是手机号码，不能发送信息！");
	                		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","报修电话不是手机号码，不能发送信息！"));
	                	}
	                }
	
	            }
            }else if("N".equals(isreturn)){
	            cm.setSubmitType("N");
	            cm.setHandleStatus("");//处理状态
            }
            hs.save(cm);			
            hs.flush();
            
            CalloutProcess cp=(CalloutProcess)hs.get(CalloutProcess.class, calloutMasterNo);
            cp.setCalloutMasterNo(calloutMasterNo);
            cp.setAssignObject2(assignObject);
            hs.save(cp);
			tx.commit();
		} catch (Exception e1) {
			tx.rollback();
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Insert error!");
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"error.string","<font color='red'>保存失败！</font>"));
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"error.string","<font color='red'>数据异常！</font>"));
			}
		}

		ActionForward forward = null;
		try {
			
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"insert.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		if("Y".equals(isreturn)){
			forward = mapping.findForward("returnList");
		}else if("N".equals(isreturn)){
			dform.set("id", calloutMasterNo);
			forward = mapping.findForward("returnMondity");
		}
		return forward;
	}

	/**
	 * 删除紧急级别
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDeleteRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();			
			String id = request.getParameter("id");			
			CalloutProcess cp = (CalloutProcess) hs.get(CalloutProcess.class, id);
			if (cp != null) {
                hs.delete(cp);										
			}
			CalloutMaster cm = (CalloutMaster) hs.get(CalloutMaster.class, id);
			if (cm != null) {
                hs.delete(cm);														
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		} catch (DataStoreException e1) {
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Update error!");

		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}
		if (errors.isEmpty()) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.succeed"));
		}			
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		ActionForward forward = null;
		try {
			forward = mapping.findForward("returnList");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return forward;
	}
	
	/**
	 * Method toSearchRecord execute, to Excel Record 列表查询导出Excel
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException
	 */
	public HttpServletResponse toExcelRecord(ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;

		String companyName = tableForm.getProperty("companyId");// 保修单位名称
		if(companyName!=null && !"".equals(companyName)){
			companyName="%"+companyName+"%";
		}else{
			companyName="%";
		}
		String calloutMasterNo = tableForm.getProperty("calloutMasterNo");// 急修编号
		if(calloutMasterNo!=null && !"".equals(calloutMasterNo)){
			calloutMasterNo="%"+calloutMasterNo+"%";
		}else{
			calloutMasterNo="%";
		}
		String operId = tableForm.getProperty("operId");// 起草人
		if(operId!=null && !"".equals(operId)){
			operId="%"+operId+"%";
		}else{
			operId="%";
		}
		String handleStatus = tableForm.getProperty("handleStatus");// 处理状态
		if(handleStatus==null){
			handleStatus="%";
		}

		String submitType = tableForm.getProperty("SubmitType");// 提交标志
		if(submitType!=null && !"".equals(submitType)){
			submitType="%"+submitType+"%";
		}else{
			submitType="%";
		}
		String maintStation = tableForm.getProperty("maintStation");// 维保站
		if(maintStation!=null && !"".equals(maintStation)){
			maintStation="%"+maintStation+"%";
		}else{
			maintStation="%";
		}
		String repairMode = tableForm.getProperty("repairMode");// 报修方式
		if(repairMode!=null && !"".equals(repairMode)){
			repairMode="%"+repairMode+"%";
		}else{
			repairMode="%";
		}
		String serviceObjects = tableForm.getProperty("serviceObjects");// 服务对象
		if(serviceObjects!=null && !"".equals(serviceObjects)){
			serviceObjects="%"+serviceObjects+"%";
		}else{
			serviceObjects="%";
		}
		
		String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号
		if(elevatorNo!=null && !"".equals(elevatorNo)){
			elevatorNo="%"+elevatorNo.trim()+"%";
		}else{
			elevatorNo="%";
		}
		String r5 = tableForm.getProperty("r5");//维修人员
		if(r5!=null && !"".equals(r5)){
			r5="%"+r5.trim()+"%";
		}else{
			r5="%";
		}
		String sdate1=(String) tableForm.getProperty("sdate1");
		String edate1=(String) tableForm.getProperty("edate1");
		if((sdate1==null || sdate1.trim().equals("")) 
				&& (edate1==null || edate1.trim().equals(""))){
			String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
			String day1=DateUtil.getDate(day, "MM", -2);//当前日期月份加1 。
			//String day2=DateUtil.getDate(day, "MM", +1);//当前日期月份加1 。
			sdate1=day1;
			edate1=day;
		}
		
		String orderby="order by calloutMasterNo desc";
		 
		Session hs = null;
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List list=new ArrayList();
		XSSFWorkbook wb = new XSSFWorkbook();
		try {
			hs = HibernateUtil.getSession();
			
			String comid=userInfo.getComID();
			if("00".equals(comid)){
				comid="%";
			}
			
			String sql="exec HL_callhotsearch_new ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
			con=hs.connection();
			ps=con.prepareStatement(sql);
			ps.setString(1, companyName);
			ps.setString(2, calloutMasterNo);
			ps.setString(3, operId);
			ps.setString(4, handleStatus);
			ps.setString(5, submitType);
			ps.setString(6, maintStation);
			ps.setString(7, repairMode);
			ps.setString(8, serviceObjects);
			ps.setString(9, orderby);
			ps.setString(10, "2");
			ps.setString(11, elevatorNo);
			ps.setString(12, comid);
			ps.setString(13, r5);
			ps.setString(14, sdate1);
			ps.setString(15, edate1);
			rs=ps.executeQuery();
			//System.out.println(ps.toString());
			
			XSSFSheet sheet = wb.createSheet("急修流程管理信息");
			String[] table={"主表单信息","流转主要信息","现场维修信息","维修过程信息","用户回复信息","回访用户信息"};			
			String[] table01={"报修单号","录入人","报修时间","报修方式","报修人","报修电话","服务对象","报修单位","项目名称及楼栋号","销售合同号","电梯编号","规格型号","维保站","派工对象","电话","是否困人","报修描述","维修人员"};					
			String[] table02={"转派时间","转派人","转派电话","派工接受时间","派工接收人","接收人电话"};					
			String[] table03={"到现场时间","设备编号","主板类型","到场经度","到场纬度","电梯位置","故障代码","故障状态","是否停梯"};			
			String[] table04={"维修完成时间","新换配件名称","数量","完工经度","完工纬度","电梯位置","故障类型","是否收费","收费金额","维修过程描述","维修备注","审核人","审核日期","是否发送短信"};			
			String[] table05={"用户服务评价","其他意见"};
			String[] table06={"故障分类","回访用户服务评价","回访配件更换情况","回访收费情况","回访备注","是否关闭回召"};
			String[][] table1={table01,table02,table03,table04,table05,table06};
			String[] keys={"CalloutMasterNo",
					"OperId",
					"RepairTime",
					"RepairMode",
					"RepairUser",
					"RepairTel",
					"ServiceObjects",
					"CompanyID",
					"projectAddress",
					"SalesContractNo",
					"ElevatorNo",
					"ElevatorParam",
					"MaintStation",
					"AssignObject",
					"Phone",
					"IsTrap",
					"RepairDesc",
					"r5name",
					"AuditOperid",
					"AuditDate",
					"IsSendSms",
					"hfcId",
					"ServiceAppraisal",
					"FittingSituation",
					"TollSituation",
					"VisitRem",
					"IsColse",					
					"TurnSendTime",
					"TurnSendId",
					"TurnSendTel",
					"AssignTime",
					"AssignUser",
					"AssignUserTel",					
					"ArriveDateTime",
					"DeviceId",
					"hmtId",
					"ArriveLongitude",
					"ArriveLatitude",
					"ElevatorLocation1",
					"FaultCode",
					"FaultStatus",
					"IsStop",			
					"CompleteTime",
					"NewFittingName",
					"num",
					"FninishLongitude",
					"FninishLatitude",
					"ElevatorLocation2",
					"hftId",
					"IsToll",
					"Money",
					"ProcessDesc",
					"ServiceRem",		
					"ServiceRating",
					"OtherRem"};
			
			int j=table01.length-1;
			int k=0;
			CellRangeAddress region=null;
			//表头
			XSSFRow row0 = sheet.createRow( 0);
			XSSFCell cell0=null;
			XSSFCellStyle CellStyle0=wb.createCellStyle();
			XSSFFont font=wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
			CellStyle0.setFont(font);
			CellStyle0.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中

//		    CellStyle0.setBorderBottom(HSSFCellStyle.BORDER_THICK);// 下边框
//		    CellStyle0.setBorderLeft(HSSFCellStyle.BORDER_THICK);//左边框
//		    CellStyle0.setBorderRight(HSSFCellStyle.BORDER_THICK);//右边框
//		    CellStyle0.setBorderTop(HSSFCellStyle.BORDER_THICK);//上边框
			for(int i=0;i<table.length;i++){	
				cell0=row0.createCell((short)k);
				region= new CellRangeAddress(0, 0, k,j );
				sheet.addMergedRegion(region);
				cell0.setCellValue(table[i]);
				cell0.setCellStyle(CellStyle0);
				k+=table1[i].length;				
				if(i!=table.length-1){
				j+=table1[i+1].length;
				}
			}
			//第二行
			 row0 = sheet.createRow(1);	
			 int q=0;
			 for(int i=0;i<table1.length;i++){				 
				 for(int jj=0;jj<table1[i].length;jj++){
					 cell0 =row0.createCell((short)q);	
					 cell0.setCellValue(table1[i][jj]);

					 q++;
				 }
			 }
			//迭代数据
			 int w=2;
			 while(rs.next()){	
				 row0 = sheet.createRow((w));
				 for(int i=0;i<keys.length;i++){
					XSSFCell cell2=row0.createCell((short)i);
					
					cell2.setCellValue(rs.getString(i+1));
					/**
					if((i+1)==18){
						String r5id=rs.getString(i+1);//急修参与人员
	      	           	String r5name="";
	      	           	if(r5id!=null && !r5id.trim().equals("")){
	      	           		 String sqls="select a from Loginuser a where a.userid in('"+r5id.replaceAll(",", "','")+"')";
	      	           		 List loginlist=hs.createQuery(sqls).list();
	      	           		 if(loginlist!=null && loginlist.size()>0){
	      	           			 for(int l=0;l<loginlist.size();l++){
	      	           				 Loginuser login=(Loginuser)loginlist.get(l);
	      	           				 if(l==loginlist.size()-1){
	      	           					 r5name+=login.getUsername();
	      	           				 }else{
	      	           					 r5name+=login.getUsername()+",";
	      	           				 }
	      	           			 }
	      	           		 }
	      	           	 }
						cell2.setCellValue(r5name);
					}else{
						cell2.setCellValue(rs.getString(i+1));
					}
					*/
				 }
				 w++;
			 }
						
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("急修流程管理信息", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		//response.getOutputStream().flush(); 
		//response.getOutputStream().close(); 
		return response;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	public ActionForward tosaveshps(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		ActionForward  Forward=null;
		ActionErrors errors = new ActionErrors();
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		String CalloutMasterNo= request.getParameter("id");
		String typejsp=request.getParameter("typejsp");
		String value=request.getParameter("value");		
		String isreturn=request.getParameter("isreturn");
		
		CalloutMaster cm=null;
		CalloutProcess cp=null;
		CalloutSms cs=null;
		SmsHistory sh=null;
		Session hs=null;
		Transaction tx=null;
		if(CalloutMasterNo!=null){
			try {
				hs=HibernateUtil.getSession();			
			    tx=hs.beginTransaction();
			    cm=(CalloutMaster)hs.get(CalloutMaster.class, CalloutMasterNo);

				if(typejsp!=null && "shsave".equals(typejsp)){//审核
					cp=(CalloutProcess) hs.get(CalloutProcess.class, cm.getCalloutMasterNo());
					if(isreturn !=null && isreturn.trim().equals("R")){//驳回维修单
						String bhAuditRem=request.getParameter("bhAuditRem");
						cm.setBhAuditRem(bhAuditRem);//审核驳回意见
						cp.setIsgzbgs("N");
						cm.setHandleStatus("4");
						hs.save(cp);
						hs.save(cm);
					}else{
						String isSubSM=request.getParameter("isSubSM");//提交技术质量安全经理
						String auditRem=request.getParameter("auditRem");
						String hfcId=request.getParameter("hfcId");

						String hftId=request.getParameter("hftId");
						String processDesc=request.getParameter("processDesc");
						String serviceRem=request.getParameter("serviceRem");
						String repairMode=request.getParameter("repairMode");
						String repairUser=request.getParameter("repairUser");
						String repairTel=request.getParameter("repairTel");
						String serviceObjects=request.getParameter("serviceObjects");
						String isTrap=request.getParameter("isTrap");
						String repairDesc=request.getParameter("repairDesc");
						
						String deviceId=request.getParameter("deviceId");
						String faultCode=request.getParameter("faultCode");
						String faultStatus=request.getParameter("faultStatus");
						String hmtId=request.getParameter("hmtId");
						String isStop=request.getParameter("isStop");
						String stopTime=request.getParameter("stopTime");
						String stopRem=request.getParameter("stopRem");
						
						cp.setDeviceId(deviceId);
						cp.setFaultCode(faultCode);
						cp.setFaultStatus(faultStatus);
						cp.setHmtId(hmtId);
						cp.setIsStop(isStop);
						cp.setStopTime(stopTime);
						cp.setStopRem(stopRem);
						cp.setHftId(hftId);
						cp.setProcessDesc(processDesc);
						cp.setServiceRem(serviceRem);
						
						cm.setRepairMode(repairMode);
						cm.setRepairTel(repairTel);
						cm.setRepairUser(repairUser);
						cm.setServiceObjects(serviceObjects);
						cm.setIsTrap(isTrap);
						cm.setRepairDesc(repairDesc);
						cm.setAuditOperid(userInfo.getUserID());
						cm.setAuditDate(xj.getdatetime());
						cm.setIsSendSms(value);
						cm.setAuditRem(auditRem);
						cm.setHfcId(hfcId);
						
						if(isSubSM!=null && isSubSM.trim().equals("Y")){
							cm.setIsSubSM(isSubSM);
							cm.setHandleStatus("8");//8 技术质量安全经理
						}else{
							cm.setHandleStatus("6");
						}
						
						hs.save(cp);
						hs.save(cm);
					    hs.flush();
						if(value!=null && "Y".equals(value)){
							/******************发送短信开始**************************************/					
							String smsContent="西继迅达电梯: 您好,我们的维保人员已为您完成了急修服务,请您回复数字01-05对"
									+ "他的该次服务做出评价。01,非常满意  02,满意  03,一般  04,不满意  05,非常不满意。"
									+ "   您也可以回复\"0\"+内容,为我们提出意见和建议。西继迅达将竭诚为您服务。";
							String telNo=cm.getRepairTel();
							
							System.out.println(">>>发送回访短信");
							boolean iscg=true;
							//boolean iscg=SmsService.sendSMS(smsContent, telNo);
							//boolean iscg=XjsggAction.sendMessage(smsContent, telNo);
							
							//存入急修短信表
							String time=xj.getdatetime();
							cs=(CalloutSms)hs.get(CalloutSms.class, CalloutMasterNo);
							cs.setSmsTel2(telNo);//回访电话
							cs.setSmsSendTime2(time);//回访发送短信时间
							cs.setSmsContent2(smsContent);//回访短信内容
							hs.save(cs);
							hs.flush();
							
							//存入历史表
							sh=new SmsHistory();
							sh.setSmsContent(smsContent);
							sh.setSmsSendTime(time);
							sh.setSmsTel(telNo);
							if(iscg){
								sh.setFlag(1);
							}else{
								sh.setFlag(0);	
							}
							hs.save(sh);
							/***********************发送短信结束******************************************/		
						}
					}
				}else if(typejsp!=null && "pssave".equals(typejsp)){ //回访评审
					String serviceAppraisal=request.getParameter("serviceAppraisal");
					String fittingSituation=request.getParameter("fittingSituation");
					String tollSituation=request.getParameter("tollSituation");
					String visitRem=request.getParameter("visitRem");
					String isColse=request.getParameter("isColse");
					cm.setHandleStatus("7");
					cm.setServiceAppraisal(serviceAppraisal);
					cm.setFittingSituation(fittingSituation);
					cm.setTollSituation(tollSituation);
					cm.setVisitRem(visitRem);
					cm.setIsColse(isColse);
					hs.save(cm);
				}

			tx.commit();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","保存失败！"));
			}finally{
				hs.close();
				
			}
			
		}else{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","保存失败！"));
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		if(isreturn!=null && "Y".equals(isreturn)){
			Forward=mapping.findForward("returnList");			
		}else if(isreturn!=null && "R".equals(isreturn)){
			Forward=mapping.findForward("returnList");			
		}else if("N".equals(isreturn)){
			Forward=mapping.findForward("returndisplay");
		}
		return Forward;
	}
	
	public static String getelecadrr(Session hs,String elevatorNo,double longs,double lats){
		String address="";
		String hql="select a from ElevatorCoordinateLocation a where elevatorNo='"+elevatorNo+"'"
				+ " and beginLongitude>= "+longs+" and endLongitude<="+longs+" and beginDimension>="+lats+""
				+ " and endDimension<= "+lats;
		List list=hs.createQuery(hql).list();
		if(list!=null && list.size()>0){
			ElevatorCoordinateLocation ec=(ElevatorCoordinateLocation)list.get(0);
			address=ec.getElevatorLocation();
		}
		return address;
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
	public void toComidStorageIDList(ActionMapping mapping, ActionForm form,
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
		String storageid=request.getParameter("storageid");
		response.setHeader("Content-Type","text/html; charset=GBK");
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='GBK'?>");
		sb.append("<root>");
		try {
			hs=HibernateUtil.getSession();
			if(storageid!=null && !"".equals(storageid)){
				//只出 维保站长A49，维保工A50,维保经理 A03，维修技术员A53
				String hql="select a from Loginuser a where a.storageid like '"+storageid+"%' " +
						"and a.enabledflag='Y' and a.roleid in('A49','A50','A03','A53') order by a.roleid,a.userid";
				List list=hs.createQuery(hql).list();
				if(list!=null && list.size()>0){
					sb.append("<rows>");
					for(int i=0;i<list.size();i++){
						Loginuser sid=(Loginuser)list.get(i);
						sb.append("<cols name='"+sid.getUsername()+"' value='"+sid.getUserid()+"' phone='"+sid.getPhone()+"'>").append("</cols>");
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
	
	/**
	 * 打印通知单
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	/*@SuppressWarnings("unchecked")*/
	public ActionForward toPreparePrintRecord(ActionMapping mapping,ActionForm form,
						HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
			    	ActionErrors errors = new ActionErrors();
			    	HttpSession session = request.getSession();
					ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
					Session hs = null;
					DynaActionForm dform = (DynaActionForm) form;		
					String id = request.getParameter("id");
					List etcpList=new ArrayList();
					if (id != null && !id.equals("")) {			
						try {
							hs = HibernateUtil.getSession();
							
							Query query = hs.createQuery("from CalloutMaster where calloutMasterNo = '"+id.trim()+"'");
							List list = query.list();
							if (list != null && list.size() > 0) {

								// 主信息
								CalloutMaster CM = (CalloutMaster) list.get(0);	
								
								CM.setRepairMode(CM.getRepairMode().equals("1")?"被动急修":"主动急修");
								CM.setCompanyId(bd.getName(hs, "Customer", "companyName", "companyId",CM.getCompanyId()));
								CM.setIsTrap(CM.getIsTrap().equals("Y")?"困人":"非困人");
								CM.setProjectAddress(bd.getName("ElevatorCoordinateLocation", "rem", "elevatorNo", CM.getElevatorNo()));//项目名称及楼栋号

								Query query1 = hs.createQuery("from CalloutProcess where calloutMasterNo = '"+id.trim()+"'");	
								List list1 = query1.list();
								CalloutProcess CP = (CalloutProcess) list1.get(0);
								CP.setAssignObject2(bd.getName(hs, "Loginuser","username", "userid",CP.getAssignObject2()));
								/**
								String hftid=CP.getHftId();//故障类型
					           	 String hftdesc="";
					           	 if(hftid!=null && !hftid.trim().equals("")){
					           		 String sqls="select a from HotlineFaultType a where a.hftId in('"+hftid.replaceAll(",", "','")+"')";
					           		 List loginlist=hs.createQuery(sqls).list();
					           		 if(loginlist!=null && loginlist.size()>0){
					           			 for(int l=0;l<loginlist.size();l++){
					           				HotlineFaultType hft=(HotlineFaultType)loginlist.get(l);
					           				 if(l==loginlist.size()-1){
					           					hftdesc+=hft.getHftDesc();
					           				 }else{
					           					hftdesc+=hft.getHftDesc()+",";
					           				 }
					           			 }
					           		 }
					           	 }
					           	CP.setHftId(hftdesc);
					           	*/
								/**
					           	String r5=CP.getR5();//急修参与人员
					           	 String r5name="";
					           	 if(r5!=null && !r5.trim().equals("")){
					           		 String sqls="select a from Loginuser a where a.userid in('"+r5.replaceAll(",", "','")+"')";
					           		 List loginlist=hs.createQuery(sqls).list();
					           		 if(loginlist!=null && loginlist.size()>0){
					           			 for(int l=0;l<loginlist.size();l++){
					           				 Loginuser login=(Loginuser)loginlist.get(l);
					           				 if(l==loginlist.size()-1){
					           					 r5name+=login.getUsername();
					           				 }else{
					           					 r5name+=login.getUsername()+",";
					           				 }
					           			 }
					           		 }
					           	 }
					           	CP.setR5(r5name);
					           	*/
								//对barCodeList、noticeList操作
								BarCodePrint dy = new BarCodePrint();
								List barCodeList = new ArrayList();
								barCodeList.add(CM);
								barCodeList.add(CP);
	/*							barCodeList.add(specialRegisters);*/
								dy.toPrintTwoRecord5(request,response, barCodeList,id);
								//register hecirList
								
							
						}
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
						
					}		
					
			                  
							
							
					
					
					if (!errors.isEmpty()) {
						saveErrors(request, errors);
					}
			    	return null;
			    }	
	
	/**
	 * 下载文件
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDownloadFileRecord(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)	throws IOException, ServletException {
		
		Session hs = null;

		String filepath = request.getParameter("customerSignature");//流水号
		String filepath1 = request.getParameter("customerImage");
		String localPath="";
		String oldname="";
		
		String folder = request.getParameter("folder");		//文件夹
		if(null == folder || "".equals(folder)){
			folder ="CalloutProcess.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		
		try {
			hs = HibernateUtil.getSession();
				String root=folder;//上传目录
				if(filepath != null && !filepath.equals("")){
					localPath = root+filepath;
				}else{
					localPath = root+filepath1;
				}
				
			
				/*localPath = filepath+newnamefile;*/
				
			/*}*/
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;

		response.setContentType("application/x-msdownload");
		response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(oldname, "utf-8"));
		//OutputStream os = new FileOutputStream(new File(localPath));
		
		fis = new FileInputStream(localPath);
		bis = new BufferedInputStream(fis);
		fos = response.getOutputStream();
		bos = new BufferedOutputStream(fos);

		int bytesRead = 0;
		byte[] buffer = new byte[5 * 1024];
		while ((bytesRead = bis.read(buffer)) != -1) {
			bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
			bos.flush();
		}
		
		if (fos != null) {fos.close();}
		if (bos != null) {bos.close();}
		if (fis != null) {fis.close();}
		if (bis != null) {bis.close();}
		
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {				
				hs.close();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 点击登记停梯回访备注的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toPrepareStophfRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);

		request.setAttribute("navigator.location","急修流程管理 >> 停梯回访");	
		
		String id = (String) dform.get("id");
		if(id==null && "".equals(id)){
			id=request.getParameter("id");
		}
		String typejsp = request.getParameter("typejsp");
		
		HashMap hm=new HashMap();
		Session hs = null;
		CalloutMaster cm=null;
		CalloutProcess cp=null;
		CalloutSms cs=null;
		CalloutSms sms=null;
		String CSheight = PropertiesUtil.getProperty("CSheight");
		String CSwidth = PropertiesUtil.getProperty("CSwidth");
		String CIheight = PropertiesUtil.getProperty("CIheight");
		String CIwidth = PropertiesUtil.getProperty("CIwidth");
	   if (id != null) {				
		try {
			hs = HibernateUtil.getSession();
			cm=(CalloutMaster)hs.get(CalloutMaster.class, id);
			cm.setOperId(bd.getName("Loginuser", "username", "userid", cm.getOperId()));
			
			if(!"sh".equals(typejsp)){
				List cpList=hs.createQuery("select a.pullname from Pulldown a where "
						+ "a.id.typeflag='CalloutMaster_RepairMode' and a.enabledflag='Y' and a.id.pullid='"+cm.getRepairMode()+"'").list();
				if(cpList!=null&&cpList.size()>0){
					cm.setRepairMode(cpList.get(0).toString());
				}
				
				cpList=hs.createQuery("select a.pullname from Pulldown a where "
						+ "a.id.typeflag='CalloutMaster_ServiceObjects' and a.enabledflag='Y' and a.id.pullid='"+cm.getServiceObjects()+"'").list();
				if(cpList!=null&&cpList.size()>0){
					cm.setServiceObjects(cpList.get(0).toString());
				}
				cm.setIsTrap(String.valueOf(cm.getIsTrap()).equals("Y")?"困人":"非困人");
			}
			String companyName=bd.getName("Customer", "companyName", "companyId", cm.getCompanyId());
			if(companyName==null || "".equals(companyName)){
				companyName=cm.getCompanyId();
			}
			cm.setCompanyId(companyName);
			cm.setMaintStation(bd.getName("Storageid", "storagename", "storageid", cm.getMaintStation()));
			cm.setAssignObject(bd.getName("Loginuser", "username", "userid", cm.getAssignObject()));
			cm.setProjectAddress(bd.getName("ElevatorCoordinateLocation", "rem", "elevatorNo", cm.getElevatorNo()));//项目名称及楼栋号
			
			if(typejsp!=null && "sh".equals(typejsp)){
				
				cm.setAuditOperid(bd.getName("Loginuser", "username", "userid", cm.getAuditOperid()));
				List HotlineFaultClassificationList=xj.getClasses(hs, "HotlineFaultClassification", "enabledFlag", "Y");
				request.setAttribute("HotlineFaultClassificationList", HotlineFaultClassificationList);
				request.setAttribute("rmList", bd.getPullDownList("CalloutMaster_RepairMode"));
				request.setAttribute("soList", bd.getPullDownList("CalloutMaster_ServiceObjects"));
			}else if(typejsp!=null && "ps".equals(typejsp)){
				cm.setAuditOperid(bd.getName("Loginuser", "username", "userid", cm.getAuditOperid()));
				cm.setHfcId(bd.getName("HotlineFaultClassification", "hfcName", "hfcId", String.valueOf(cm.getHfcId())));//故障分类
			}else{
				cm.setStophfOperid(userInfo.getUserName());
				cm.setStophfDate(xj.getdatetime());
				cm.setSmAuditOperid(bd.getName("Loginuser", "username", "userid", cm.getSmAuditOperid()));
				typejsp="display";
				cm.setAuditOperid(bd.getName("Loginuser", "username", "userid", cm.getAuditOperid()));
				cm.setHfcId(bd.getName("HotlineFaultClassification", "hfcName", "hfcId", String.valueOf(cm.getHfcId())));//故障分类
			}
			String IsSendSms=cm.getIsSendSms();
			if(IsSendSms!=null && !"".equals(IsSendSms)){
				cm.setIsSendSms(String.valueOf(cm.getIsSendSms()).equals("Y")?"已发送":"未发送");
			}else{
				cm.setIsSendSms("");
			}	
			String isSubSM=cm.getIsSubSM();
			if(isSubSM!=null && !"".equals(isSubSM)){
				cm.setIsSubSM(String.valueOf(cm.getIsSubSM()).equals("Y")?"是":"否");
			}else{
				cm.setIsSubSM("");
			}
			String ServiceAppraisal=cm.getServiceAppraisal();
			if(ServiceAppraisal!=null && !"".equals(ServiceAppraisal)){
				switch(Integer.valueOf(ServiceAppraisal).intValue()){
				case 1:cm.setServiceAppraisal("非常满意");break;
				case 2:cm.setServiceAppraisal("满意");break;
				case 3:cm.setServiceAppraisal("一般");break;
				case 4:cm.setServiceAppraisal("不满意");break;
				case 5:cm.setServiceAppraisal("非常不满意");break;
				}
			}
			String FittingSituation=cm.getFittingSituation();
			if(FittingSituation!=null && !"".equals(FittingSituation)){
				cm.setFittingSituation(String.valueOf(cm.getFittingSituation()).equals("1")?"属实":"不属实");
			}else{
				cm.setFittingSituation("");
			}
			String TollSituation=cm.getTollSituation();
			if(TollSituation!=null && !"".equals(TollSituation)){
				cm.setTollSituation(String.valueOf(cm.getTollSituation()).equals("1")?"属实":"不属实");
			}else{
				cm.setTollSituation("");
			}
			String IsColse=cm.getIsColse();
			if(IsColse!=null && !"".equals(IsColse)){
				cm.setIsColse(String.valueOf(cm.getIsColse()).equals("1")?"关闭":"不关闭");
			}else{
				cm.setIsColse("");
			}           			
			cp=(CalloutProcess)hs.get(CalloutProcess.class, id);
			if(cp!=null){
				cp.setTurnSendId(bd.getName("Loginuser", "username", "userid", String.valueOf(cp.getTurnSendId())));
				cp.setAssignUser(bd.getName("Loginuser", "username", "userid", String.valueOf(cp.getAssignUser())));
				String IsStop=cp.getIsStop();
				if(!"sh".equals(typejsp)){
					if(IsStop!=null && !"".equals(IsStop)){
						cp.setIsStop(IsStop.equals("Y")?"停梯":"非停梯");
					}else{
						cp.setIsStop("");	
					}
					
					//cp.setHftId(bd.getName("HotlineFaultType", "hftDesc", "hftId",String.valueOf(cp.getHftId())));//故障类型
					String hftid=cp.getHftId();//故障类型
		           	 String hftdesc="";
		           	 if(hftid!=null && !hftid.trim().equals("")){
		           		 String sqls="select a from HotlineFaultType a where a.hftId in('"+hftid.replaceAll(",", "','")+"')";
		           		 List loginlist=hs.createQuery(sqls).list();
		           		 if(loginlist!=null && loginlist.size()>0){
		           			 for(int l=0;l<loginlist.size();l++){
		           				HotlineFaultType hft=(HotlineFaultType)loginlist.get(l);
		           				 if(l==loginlist.size()-1){
		           					hftdesc+=hft.getHftDesc();
		           				 }else{
		           					hftdesc+=hft.getHftDesc()+",";
		           				 }
		           			 }
		           		 }
		           	 }
		           	cp.setHftId(hftdesc);
					
					
					cp.setHmtId(bd.getName("HotlineMotherboardType", "hmtName", "hmtId", String.valueOf(cp.getHmtId())));//主板类型
				}else{
					//request.setAttribute("hftId", bd.getName("HotlineFaultType", "hftDesc", "hftId",String.valueOf(cp.getHftId())));
					String hftid=cp.getHftId();//急修参与人员
		           	 String hftdesc="";
		           	 if(hftid!=null && !hftid.trim().equals("")){
		           		 String sqls="select a from HotlineFaultType a where a.hftId in('"+hftid.replaceAll(",", "','")+"')";
		           		 List loginlist=hs.createQuery(sqls).list();
		           		 if(loginlist!=null && loginlist.size()>0){
		           			 for(int l=0;l<loginlist.size();l++){
		           				HotlineFaultType hft=(HotlineFaultType)loginlist.get(l);
		           				 if(l==loginlist.size()-1){
		           					hftdesc+=hft.getHftDesc();
		           				 }else{
		           					hftdesc+=hft.getHftDesc()+",";
		           				 }
		           			 }
		           		 }
		           	 }
		           	request.setAttribute("hftId", hftdesc);
					
					request.setAttribute("hmtId", bd.getName("HotlineMotherboardType", "hmtName", "hmtId", String.valueOf(cp.getHmtId())));
				}
				
				String IsToll=cp.getIsToll();
				if(IsToll!=null && !"".equals(IsToll)){
					cp.setIsToll(String.valueOf(cp.getIsToll()).equals("Y")?"收费":"非收费");
				}else{
					cp.setIsToll("");
				} 
				Double ArriveLongitude=cp.getArriveLongitude();
				Double ArriveLatitude=cp.getArriveLatitude();
				Double FninishLongitude=cp.getFninishLongitude();
				Double FninishLatitude=cp.getFninishLatitude();
				/**
				String r5=cp.getR5();//急修参与人员
	           	 String r5name="";
	           	 if(r5!=null && !r5.trim().equals("")){
	           		 String sqls="select a from Loginuser a where a.userid in('"+r5.replaceAll(",", "','")+"')";
	           		 List loginlist=hs.createQuery(sqls).list();
	           		 if(loginlist!=null && loginlist.size()>0){
	           			 for(int l=0;l<loginlist.size();l++){
	           				 Loginuser login=(Loginuser)loginlist.get(l);
	           				 if(l==loginlist.size()-1){
	           					 r5name+=login.getUsername();
	           				 }else{
	           					 r5name+=login.getUsername()+",";
	           				 }
	           			 }
	           		 }
	           	 }
	           	 */
	           	hm.put("r5name",cp.getR5());//急修参与人员

				hm.put("elevatorLocation",cp.getArriveLocation());
				hm.put("elevatorLocation2", cp.getFninishLocation());
			}
			cs=(CalloutSms)hs.get(CalloutSms.class, id);
			if(cs!=null){
			String ServiceRating=cs.getServiceRating();//用户评价
			if(ServiceRating!=null && !"".equals(ServiceRating)){
				switch(Integer.valueOf(ServiceRating).intValue()){
				case 1:cs.setServiceRating("非常满意");break;
				case 2:cs.setServiceRating("满意");break;
				case 3:cs.setServiceRating("一般");break;
				case 4:cs.setServiceRating("不满意");break;
				case 5:cs.setServiceRating("非常不满意");break;
				}
			}
			}
			sms=(CalloutSms) hs.get(CalloutSms.class, cm.getCalloutMasterNo());
			
			Query queryfiles = hs
					.createQuery("from CalloutFileinfo ");
					List listfiles = queryfiles.list();
					request.setAttribute("CalloutFileinfo", listfiles);
			
		
		} catch (DataStoreException e) {
				e.printStackTrace();
			}				
		
	}else{
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
	}

	//维保站
	List storageidList=bd.getMaintStationList(userInfo.getComID());
	request.setAttribute("storageidList", storageidList);
	
    request.setAttribute("CalloutMasterList", cm);
    request.setAttribute("CalloutProcessList", cp);
    request.setAttribute("CalloutSmsList", cs);
    request.setAttribute("hashmapbean", hm);
    request.setAttribute("calloutSmsBean", sms);
	request.setAttribute("typejsp",typejsp);
	request.setAttribute("CSheight",CSheight);
	request.setAttribute("CSwidth", CSwidth);
	request.setAttribute("CIheight", CIheight);
	request.setAttribute("CIwidth", CIwidth);
	if (!errors.isEmpty()) {
		saveErrors(request, errors);
	}
		forward = mapping.findForward("toStophf");
		return forward;
	}
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	
	public ActionForward tosavestophf(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		ActionForward  Forward=null;
		ActionErrors errors = new ActionErrors();
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		String calloutMasterNo= request.getParameter("calloutMasterNo");
		String stophfRem=request.getParameter("stophfRem");	
		
		CalloutMaster cm=null;

		Session hs=null;
		Transaction tx=null;
		if(calloutMasterNo!=null){
			try {
				hs=HibernateUtil.getSession();			
			    tx=hs.beginTransaction();
			    
			    cm=(CalloutMaster)hs.get(CalloutMaster.class, calloutMasterNo);
				
				cm.setStophfRem(stophfRem);
				cm.setStophfOperid(userInfo.getUserID());
				cm.setStophfDate(xj.getdatetime());
				
				hs.save(cm);
			    hs.flush();

			tx.commit();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
			} catch (Exception e) {
				tx.rollback();
				e.printStackTrace();
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","<font color='red'>保存失败！</font>"));
			}finally{
				hs.close();
			}
			
		}else{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","<font color='red'>保存失败！</font>"));
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		Forward=mapping.findForward("returnList");			

		return Forward;
	}
	
	
}
