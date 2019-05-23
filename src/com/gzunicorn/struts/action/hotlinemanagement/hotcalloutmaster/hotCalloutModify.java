package com.gzunicorn.struts.action.hotlinemanagement.hotcalloutmaster;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.hotlinefaulttype.HotlineFaultType;
import com.gzunicorn.hibernate.hotlinemanagement.calloutmaster.CalloutMaster;
import com.gzunicorn.hibernate.hotlinemanagement.calloutprocess.CalloutProcess;
import com.gzunicorn.hibernate.hotlinemanagement.calloutsms.CalloutSms;
import com.gzunicorn.hibernate.hotlinemanagement.smshistory.SmsHistory;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.struts.action.xjsgg.XjsggAction;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class hotCalloutModify  extends DispatchAction{
	XjsggAction xj=new XjsggAction();
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
	 * 系统调用母方法
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String name = request.getParameter("method");
		
		if(!"toDisplayRecord".equals(name)){
			/** **********开始用户权限过滤*********** */
			SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "hotcalloutmodify", null);
			/** **********结束用户权限过滤*********** */
		}

		// Set default method is toSearchRecord
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
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
		request.setAttribute("navigator.location","急修流程修改 >> 查询列表");		
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
				//response = toExcelRecord(form,request,response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport","");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "hotCalloutModify");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fHotCalloutModify");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("calloutMasterNo");
			table.setIsAscending(false);
			cache.updateTable(table);

			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
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
			if(handleStatus!=null && !"".equals(handleStatus)){
				handleStatus="%"+handleStatus.trim()+"%";
			}else{
				handleStatus="%";
			}
			String submitType ="Y";
//			String submitType = tableForm.getProperty("SubmitType");// 提交标志
//			if(submitType!=null && !"".equals(submitType)){
//				handleStatus="%"+handleStatus.trim()+"%";
//			}else{
//				submitType="%";
//			}
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
				storageidList=bd.getMaintStationList(userInfo.getComID());
				
				String comid=userInfo.getComID();
				if("00".equals(comid)){
					comid="%";
				}
				
				String sql="exec HL_callhotsearch ?,?,?,?,?,?,?,?,?,?,?,?,?";
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
				ps.setString(13, "%");
				rs=ps.executeQuery();
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
            		   
            		   list2.add(hm);  
            	   }
            	   i++;
               }
               
               table.setVolume(i);// 查询得出数据记录数;
			   cache.check(table);
			    list3.addAll(list2);				
				table.addAll(list3);
				session.setAttribute("hotCalloutModify", table);
				
				request.setAttribute("rmList", RepairModeList);
				request.setAttribute("soList", ServiceObjectsList);
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
		} catch (Exception e1) {
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
	 * 点击查看的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
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
		String typejsp = "sh";
		
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
				//cm.setAuditDate(xj.getdatetime());
				request.setAttribute("navigator.location","急修流程修改 >> 修改");
				List HotlineFaultClassificationList=xj.getClasses(hs, "HotlineFaultClassification", "enabledFlag", "Y");
				request.setAttribute("HotlineFaultClassificationList", HotlineFaultClassificationList);
				request.setAttribute("rmList", bd.getPullDownList("CalloutMaster_RepairMode"));
				request.setAttribute("soList", bd.getPullDownList("CalloutMaster_ServiceObjects"));
				
				
			}else if(typejsp!=null && "ps".equals(typejsp)){
				request.setAttribute("navigator.location","急修流程管理 >> 回访审核");
				cm.setSmAuditOperid(bd.getName("Loginuser", "username", "userid", cm.getSmAuditOperid()));
				cm.setAuditOperid(bd.getName("Loginuser", "username", "userid", cm.getAuditOperid()));
				cm.setHfcId(bd.getName("HotlineFaultClassification", "hfcName", "hfcId", String.valueOf(cm.getHfcId())));//故障分类
			}else{
				
				String isSubSM=cm.getIsSubSM();
				if(isSubSM!=null && !"".equals(isSubSM)){
					cm.setIsSubSM(String.valueOf(cm.getIsSubSM()).equals("Y")?"是":"否");
				}else{
					cm.setIsSubSM("");
				}

				String IsSendSms=cm.getIsSendSms();
				if(IsSendSms!=null && !"".equals(IsSendSms)){
					cm.setIsSendSms(String.valueOf(cm.getIsSendSms()).equals("Y")?"已发送":"未发送");
				}else{
					cm.setIsSendSms("");
				}
				
				typejsp="display";
				cm.setSmAuditOperid(bd.getName("Loginuser", "username", "userid", cm.getSmAuditOperid()));
				cm.setAuditOperid(bd.getName("Loginuser", "username", "userid", cm.getAuditOperid()));
				request.setAttribute("navigator.location","急修流程管理 >> 查看");
				cm.setHfcId(bd.getName("HotlineFaultClassification", "hfcName", "hfcId", String.valueOf(cm.getHfcId())));//故障分类
			}
			
			cm.setStophfOperid(bd.getName("Loginuser", "username", "userid", cm.getStophfOperid()));//停梯回访人
			
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
		forward = mapping.findForward("toModify");
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
	
	public ActionForward toUpdateRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		ActionForward  Forward=null;
		ActionErrors errors = new ActionErrors();
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		String CalloutMasterNo= request.getParameter("id");
		String typejsp="shsave";
		String value=request.getParameter("value");		
		String isreturn="Y";
		
		CalloutMaster cm=null;
		CalloutProcess cp=null;
		//CalloutSms cs=null;
		//SmsHistory sh=null;
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
						cp.setIsgzbgs("N");
						cm.setHandleStatus("4");
						hs.save(cp);
						hs.save(cm);
					}else{
						//String isSubSM=request.getParameter("isSubSM");//提交技术质量安全经理
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
						//cm.setIsSendSms(value);
						cm.setAuditRem(auditRem);
						cm.setHfcId(hfcId);
						
						//if(isSubSM!=null && isSubSM.trim().equals("Y")){
						//	cm.setIsSubSM(isSubSM);
						//	cm.setHandleStatus("8");//8 技术质量安全经理
						//}else{
						//	cm.setHandleStatus("6");
						//}
						
						hs.save(cp);
						hs.save(cm);
					    hs.flush();
						//if(value!=null && "Y".equals(value)){
							/******************发送短信开始**************************************					
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
							***********************发送短信结束******************************************/		
						//}
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

		Forward=mapping.findForward("returnList");			

		return Forward;
	}
	
	
}
