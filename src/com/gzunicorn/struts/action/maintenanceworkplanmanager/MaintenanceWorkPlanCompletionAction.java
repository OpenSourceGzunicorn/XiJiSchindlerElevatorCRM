package com.gzunicorn.struts.action.maintenanceworkplanmanager;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.apache.struts.util.MessageResources;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import sun.misc.BASE64Encoder;

import com.gzunicorn.common.dao.mssql.DataOperation;
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
import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;
import com.gzunicorn.hibernate.infomanager.qualitycheckmanagement.QualityCheckManagement;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplandetail.MaintenanceWorkPlanDetail;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplanfileinfo.MaintenanceWorkPlanFileinfo;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplanmaster.MaintenanceWorkPlanMaster;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;


public class MaintenanceWorkPlanCompletionAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintenanceWorkPlanCompletionAction.class);

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
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String name = request.getParameter("method");
		if(!"toDownloadFileRecord".equals(name) && !"toDisplayRecord".equals(name)){
			/** **********开始用户权限过滤*********** */
			SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "maintenanceworkplancompletion", null);
			/** **********结束用户权限过滤*********** */
		}

		// Set default method is toSearchRecord
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

		request.setAttribute("navigator.location", " 维保计划完成情况>> 查询列表");		
			ActionForward forward = null;
			
			HttpSession session = request.getSession();
			ServeTableForm tableForm = (ServeTableForm) form;
			String action = tableForm.getAction();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			
			if (tableForm.getProperty("genReport") != null
					&& !tableForm.getProperty("genReport").equals("")) {
				try {
					response = toExcelRecord(form,request,response);
				} catch (Exception e) {
					e.printStackTrace();
				}
				forward = mapping.findForward("exportExcel");
				tableForm.setProperty("genReport","");

			} else {
				HTMLTableCache cache = new HTMLTableCache(session, "maintenanceWorkPlanCompletionList");
				DefaultHTMLTable table = new DefaultHTMLTable();
				table.setMapping("fMaintenanceWorkPlanCompletion");
				table.setLength(SysConfig.HTML_TABLE_LENGTH);
				cache.updateTable(table);
				table.setSortColumn("numno");
				table.setIsAscending(true);
				cache.updateTable(table);
				if (action.equals(ServeTableForm.NAVIGATE)
						|| action.equals(ServeTableForm.SORT)) {
					cache.loadForm(tableForm);
				} else if (action.equals("Submit")) {
					cache.loadForm(tableForm);
				} else {
					table.setFrom(0);
				}
				cache.saveForm(tableForm);
				
				String mainStation = tableForm.getProperty("assignedMainStation");//维保站
				//if(mainStation==null || mainStation.trim().equals("")){
				//	mainStation="%";
				//}
				String maintPersonnel = tableForm.getProperty("maintPersonnel");//维保工
				if(maintPersonnel==null || maintPersonnel.trim().equals("")){
					maintPersonnel="%";
				}
				String r5 = tableForm.getProperty("r5");//参与维保工
				if(r5==null || r5.trim().equals("")){
					r5="%";
				}
				String singleno = tableForm.getProperty("singleno");//保养单号
				if(singleno==null || singleno.trim().equals("")){
					singleno="%";
				}
				String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号
				if(elevatorNo==null || elevatorNo.trim().equals("")){
					elevatorNo="%";
				}
				
				String sdate1=(String) tableForm.getProperty("sdate1");
				String edate1=(String) tableForm.getProperty("edate1");
				String auditType=(String) tableForm.getProperty("auditType");
				if(auditType==null || auditType.trim().equals("")){
					auditType="N";
					tableForm.setProperty("auditType",auditType);
				}
				
				if((sdate1==null || sdate1.trim().equals("")) 
						&& (edate1==null || edate1.trim().equals(""))){
					String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
					String day1=DateUtil.getDate(day, "MM", -1);//当前日期月份加1 。
					String day2=DateUtil.getDate(day, "MM", +1);//当前日期月份加1 。
					sdate1=day1;
					edate1=day2;
					tableForm.setProperty("sdate1", day1);
					tableForm.setProperty("edate1", day2);
				}

				Session hs = null;
				String order="";
				try {

					hs = HibernateUtil.getSession();
					
					String showmt="Y";
					//检查是否包含 【A03  维保经理，维保站文员 A48, 安装维保分总  068 】  只能看自己维保站下面的数据
					String sqlss="select * from view_mainstation where roleid='"+userInfo.getRoleID()+"'";
					List vmlist=hs.createSQLQuery(sqlss).list();
					List mainslist=new ArrayList();
					if(vmlist!=null && vmlist.size()>0){
						showmt="N";
						Storageid storid=new Storageid();
						 storid.setStorageid(userInfo.getStorageId());
						 storid.setStoragename(userInfo.getStorageName());
						 mainslist.add(0,storid);
					}else{
						mainslist=bd.getMaintStationList(userInfo.getComID());
					}
					request.setAttribute("showmt", showmt);
					request.setAttribute("mainStationList", mainslist);
					
					//给个默认的维保站，第一次进入此方法。
					if(mainStation == null || mainStation.trim().equals("")){
						Storageid storid=(Storageid)mainslist.get(0);
						mainStation=storid.getStorageid();
						tableForm.setProperty("assignedMainStation", mainStation);//默认维保站
					}
					
					if (table.getIsAscending()) {
						order=table.getSortColumn()+" desc";
					} else {
						order=table.getSortColumn();
					}
					
					String sqlk="EXEC SP_SELECT_MAINTENANCEWORKPLANMASTER "
							+ "'"+singleno.trim()+"','"+elevatorNo.trim()+"','"+maintPersonnel.trim()+"','"+r5.trim()+"',"
							+ "'"+mainStation.trim()+"','"+auditType.trim()+"','"+sdate1.trim()+"','"+edate1.trim()+"',"
							+ (table.getFrom()+1)+","+(table.getFrom()+table.getLength())+",'"+order.trim()+"' ";
					System.out.println(">>>"+sqlk);
					ResultSet rs=hs.connection().prepareCall(sqlk).executeQuery();
					
					int countnum=0;
					List maintenanceWorkPlanCompletionList = new ArrayList();
					while(rs.next()){
						countnum=rs.getInt("countnum");
						
						HashMap map= new HashMap();
						map.put("numno", rs.getString("numno"));
						map.put("singleno", rs.getString("singleno"));
						map.put("elevatorNo", rs.getString("elevatorNo"));
						map.put("storagename", rs.getString("storagename"));
						map.put("username", rs.getString("username"));
						map.put("phone", rs.getString("phone"));
						map.put("projectName", rs.getString("projectName"));
						map.put("maintType", rs.getString("maintType"));
						map.put("maintDate", rs.getString("maintDate"));
						map.put("receivingTime", rs.getString("receivingTime"));
						map.put("maintStartTime", rs.getString("maintStartTime"));
						map.put("maintEndTime", rs.getString("maintEndTime"));
						
						map.put("maintScore", rs.getString("maintScore"));
						map.put("byAuditOperid", rs.getString("byAuditOperid"));
						map.put("auditType", rs.getString("auditType"));
						map.put("r5id", rs.getString("r5"));
						map.put("r5name", rs.getString("r5name"));
						map.put("isred", rs.getString("isred"));
						
						maintenanceWorkPlanCompletionList.add(map);
					}
					
					table.setVolume(countnum);// 查询得出数据记录数;
					cache.check(table);

					table.addAll(maintenanceWorkPlanCompletionList);
					session.setAttribute("maintenanceWorkPlanCompletionList", table);
					
				} catch (DataStoreException e) {
					e.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					try {
						hs.close();
					} catch (HibernateException hex) {
						log.error(hex.getMessage());
						DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
					}
				}
				
				forward = mapping.findForward("maintenanceWorkPlanCompletionList");
			}
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
		request.setAttribute("navigator.location",
				messages.getMessage(locale, navigation));
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
		
		request.setAttribute("navigator.location"," 维保计划完成情况 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		String id = request.getParameter("id");	

		Session hs = null;
		MaintenanceWorkPlanDetail mwpd=null;
		HashMap map=new HashMap();
		try{
			
			//查看界面显示关闭按钮
			String isopenshow = request.getParameter("isopenshow");
			if(isopenshow!=null && isopenshow.equals("Yes")){
				request.setAttribute("isopenshow", isopenshow);
			}
			
			hs = HibernateUtil.getSession();
			
			String sql="select mwpd,mcm.maintContractNo,mcd.projectName,si.storagename,lu.username,lu.phone,mcm.billNo," +
					"mcd.elevatorNo,mcd.maintAddress,mwpm.billno,mcd.elevatorType "
					+"from  MaintenanceWorkPlanDetail mwpd,MaintenanceWorkPlanMaster mwpm," 
					+"MaintContractDetail mcd,MaintContractMaster mcm,Loginuser lu,Storageid si "
					+ "where mcd.billNo=mcm.billNo "
					+ " and si.storageid=mcd.assignedMainStation "
					+ " and lu.userid=mwpd.maintPersonnel "
					+ " and mcd.rowid=mwpm.rowid "
					+ " and mwpd.maintenanceWorkPlanMaster.billno=mwpm.billno ";
			
			if(isopenshow!=null && isopenshow.equals("Yes")){
				String singleno = request.getParameter("singleno");
				sql+= " and mwpd.singleno ='"+singleno+"'";
			}else{
				sql+= " and mwpd.numno ="+id;
			}
					
	        List list = hs.createQuery(sql).list();
	        
	       if(list!=null&&list.size()>0)
	       {
	    	   Object[] objects=(Object[]) list.get(0);
	    	   mwpd=(MaintenanceWorkPlanDetail) objects[0];
	    	   mwpd.setReceivingPerson(bd.getName(hs, "Loginuser", "username", "userid", mwpd.getReceivingPerson()));
	    	   mwpd.setByAuditOperid(bd.getName(hs, "Loginuser", "username", "userid", mwpd.getByAuditOperid()));
	    	   mwpd.setDjOperId2(bd.getName(hs, "Loginuser", "username", "userid", mwpd.getDjOperId2()));
	    	   map.put("maintContractNo", (String)objects[1]);
	    	   map.put("projectName", (String)objects[2]);
	    	   map.put("storagename", (String)objects[3]);
	    	   map.put("username", (String)objects[4]);
	    	   map.put("phone", (String)objects[5]);
	    	   map.put("billno", (String)objects[6]);
	    	   map.put("elevatorNo", (String)objects[7]);
	    	   map.put("maintAddress", (String)objects[8]);
	    	   map.put("elevatorType", (String)objects[10]);
	    	   
	    	   String sMaintEndTime=this.togetAuditCircu(hs,mwpd,(String)objects[9]);
	    	   map.put("sMaintEndTime", sMaintEndTime);//上次保养时间
	    	   /**
	    	   	String r5=mwpd.getR5();//保养参与人员
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
	          	map.put("r5name", mwpd.getR5());//参与维保工
	          	map.put("byrem", mwpd.getR4());//保养备注
	   		
	    	   //保养项目明细
	    	   	String hql ="from  MaintainProjectInfoWork mpiw where mpiw.singleno='"+mwpd.getSingleno()+"' order by mpiw.orderby,mpiw.maintItem";			
			    List infoList=hs.createQuery(hql).list();
			    if(infoList!=null && infoList.size()>0)
				{
					request.setAttribute("workinfoList", infoList);
				}	
	       }	
	        request.setAttribute("mapBean", map);
			request.setAttribute("maintenanceWorkPlanDetailBean", mwpd);
			
			String CSheight = PropertiesUtil.getProperty("CSheight");//签名高度
			String CSwidth = PropertiesUtil.getProperty("CSwidth");//签名宽度
			String CIheight = PropertiesUtil.getProperty("CIheight");//照片高度
			String CIwidth = PropertiesUtil.getProperty("CIwidth");//照片宽度
			request.setAttribute("CSheight",CSheight);
			request.setAttribute("CSwidth", CSwidth);
			request.setAttribute("CIheight", CIheight);
			request.setAttribute("CIwidth", CIwidth);
			
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
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("display", "yes");
		forward = mapping.findForward("maintenanceWorkPlanCompletionDisplay");
		return forward;
	}

	/**
	 * 文件下载方法
	 * @param response
	 * @param localPath 本地磁盘文件完整路径 如:(D:/WebProjects/xxxxxx2010年节假日.jpg)
	 * @param loname  文件逻辑名称 如:(2010年节假日.jpg)
	 * @throws IOException
	 */
	public void toDownLoadFiles(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) 
		throws IOException {
		/*
		 * 取得文件:文件id+文件路径+文件名+流 文件id=通过formbean取得 文件路径=通过取得配置文件的方法得到
		 * 文件名称=通过数据库得到 流=io
		 */
		Session hs = null;

		String filesid =request.getParameter("filesid");//流水号
		
		String localPath="";
		String oldname="";
		
		String folder = request.getParameter("folder");		//文件夹
		if(null == folder || "".equals(folder)){
			folder ="MaintenanceWorkPlan.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		
		try {
			hs = HibernateUtil.getSession();
			String sqlfile="select a from MaintenanceWorkPlanFileinfo a where a.fileSid='"+filesid+"'";
			List list2=hs.createQuery(sqlfile).list();
            
			if(list2!=null && list2.size()>0){
				MaintenanceWorkPlanFileinfo fp=(MaintenanceWorkPlanFileinfo)list2.get(0);
				String newName=fp.getNewFileName();
				oldname=fp.getOldFileName();
				String root=folder;//上传目录
				localPath = root+"/"+fp.getFilePath()+"/"+newName;
			}
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream fos = null;
		InputStream fis = null;

		response.setContentType("application/x-msdownload");
		response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(oldname, "utf-8"));

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
	}

	/**
	 * 维保计划查看方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toMaintenanceWorkPlanDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		request.setAttribute("navigator.location", "维保作业计划 >> 查看");

		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		MaintenanceWorkPlanMaster mwp = null;
		HashMap map= new HashMap();
		List mwpList = null;
		Object object=null;
		List mwpdList=new ArrayList();
		String maintPersonnel =null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				String sql="select mcm,mcd,l.username,si.storagename,c.comname"
						+ " from MaintenanceWorkPlanMaster mwpm,MaintContractDetail mcd,"
						+ " MaintContractMaster mcm,Loginuser l,Storageid si,Company c "
						+ " where c.comid=maintDivision"
						+ " and si.storageid=mcd.assignedMainStation"
						+ " and l.userid=mcd.maintPersonnel"
						+ " and mwpm.rowid=mcd.rowid"
						+ " and mcd.billNo=mcm.billNo"
						+ " and mwpm.billno= "+id;
				
				mwpList = hs.createQuery(sql).list();
				if(mwpList!=null&&mwpList.size()>0)
				{
					Object[] objects= (Object[]) mwpList.get(0);
                    map.putAll(BeanUtils.describe(objects[0]));
                    map.putAll(BeanUtils.describe(objects[1]));
                    maintPersonnel=(String) map.get("maintPersonnel");
                    map.put("maintPersonnelName", objects[2]);
                    map.put("assignedMainStation", objects[3]);
                    map.put("maintDivision", objects[4]);
					object=map;
				}
				
				String sql1="from MaintenanceWorkPlanDetail mwpd where mwpd.maintenanceWorkPlanMaster.billno="+id;
				List list=hs.createQuery(sql1).list();
				  if(list!=null&&list.size()>0)
				  {
					  for(int i=0;i<list.size();i++)
					  {
						  MaintenanceWorkPlanDetail mwpd= (MaintenanceWorkPlanDetail) list.get(i);
						  HashMap map1 =new HashMap();
						  map1.put("singleno", mwpd.getSingleno());
						  map1.put("maintDate", mwpd.getMaintDate());
						  map1.put("week", mwpd.getWeek());
						  if(mwpd.getMaintType()!=null&&!mwpd.getMaintType().equals(""))
						  {
							  if(mwpd.getMaintType().trim().equals("halfMonth"))
							  {
								  map1.put("maintType", "半月保养"); 
								 // map1.put("style","oddRow3");
							  
							  }
							  if(mwpd.getMaintType().trim().endsWith("quarter"))
							  {
								  map1.put("maintType", "季度保养");
								 // map1.put("style","oddRow3");
							  } 
							  if(mwpd.getMaintType().trim().endsWith("halfYear"))
							  {
								  map1.put("maintType", "半年保养");
								 // map1.put("style","oddRow3");
							  }
							  if(mwpd.getMaintType().trim().equals("yearDegree"))					          
						      {
								  map1.put("maintType", "年度保养");
								 // map1.put("style","oddRow3");
						      }
						  }

						  map1.put("maintDateTime", mwpd.getMaintDateTime());
						  
						  if(mwpd.getMaintEndTime()!=null&&!mwpd.getMaintEndTime().equals(""))
						  {
						  map1.put("maintSETime", mwpd.getMaintStartTime()+"~"+mwpd.getMaintEndTime());
						  }
						  else
						  {
							  map1.put("maintSETime", mwpd.getMaintStartTime()); 
						  }
						  if((i+1)%2==0)
							{map1.put("style","oddRow3");}
							else
							{map1.put("style","evenRow3");}
						  String sql2="select maintDateTime from MaintContractDetail mcd,MaintenanceWorkPlanMaster mwpm,MaintenanceWorkPlanDetail mwpd where mcd.rowid=mwpm.rowid "
						  		+ "and mwpm.billno=mwpd.billno and mcd.MaintPersonnel = '"+maintPersonnel+"' and mwpd.MaintDate ='"+mwpd.getMaintDate()+"'";
						 
						  int sumMaintDateTime=0;
						 List maintDateTimeList=hs.createSQLQuery(sql2).list();
						 if(maintDateTimeList!=null && maintDateTimeList.size()>0)
						 {
							for(int i1=0;i1<maintDateTimeList.size();i1++)
							{
								String  mdt=(String) maintDateTimeList.get(i1);
								sumMaintDateTime+=Integer.valueOf(mdt.trim());
								
							}
						 }
						  
						 map1.put("sumMaintDateTime", sumMaintDateTime);
						  mwpdList.add(map1); 
					  }
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
			
			request.setAttribute("mwpBean",
					object);
			request.setAttribute("mwpList",
					mwpdList);
			forward = mapping.findForward("toMaintenanceWorkPlan");

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}


	
	/**
	 * 跳转到修改页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws DataStoreException {
		
		request.setAttribute("navigator.location","维保计划完成情况 >> 审核");	
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		if(id==null||id.equals(""))
		{
			id=(String) request.getAttribute("id");
		}
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		MaintenanceWorkPlanDetail mwpd=null;
		HashMap map=new HashMap();
		try{
			hs = HibernateUtil.getSession();
			String sql="select mwpd,mcm.maintContractNo,mcd.projectName,si.storagename,lu.username,lu.phone," +
					"mcm.billNo,mcd.elevatorNo,mwpm.billno,mcd.maintAddress,mcd.elevatorType "
					+ "from  MaintenanceWorkPlanDetail mwpd,MaintenanceWorkPlanMaster mwpm,MaintContractDetail mcd,MaintContractMaster mcm,Loginuser lu,Storageid si "
					+ "where mcd.billNo=mcm.billNo "
					+ " and si.storageid=mcd.assignedMainStation "
					+ " and lu.userid=mwpd.maintPersonnel "
					+ " and mcd.rowid=mwpm.rowid "
					+ " and mwpd.maintenanceWorkPlanMaster.billno=mwpm.billno "
					+ " and mwpd.numno ="+id;
	        List list = hs.createQuery(sql).list();
	        
	       if(list!=null&&list.size()>0)
	       {
	    	   Object[] objects=(Object[]) list.get(0);
	    	   mwpd=(MaintenanceWorkPlanDetail) objects[0];
	    	   mwpd.setReceivingPerson(bd.getName(hs, "Loginuser", "username", "userid", mwpd.getReceivingPerson()));
	    	   
	    	   String str=mwpd.getIsInvoice();
	    	  if(mwpd.getByAuditOperid()==null||"".equals(mwpd.getByAuditOperid()))
	    	  {
	    		  mwpd.setByAuditOperid(userInfo.getUserName());
		    	  mwpd.setByAuditDate(CommonUtil.getNowTime());
	    	  }else
	    	  {
	    		  mwpd.setByAuditOperid(bd.getName(hs, "Loginuser", "username", "userid", mwpd.getByAuditOperid()));
	    		  request.setAttribute("display", "yes");
	    	  }
	    	   if(str==null||str.equals(""))
	    	   {
	    		   mwpd.setIsInvoice("N");
	    	   }
	    	   
	    	   if(mwpd.getMaintScore()==null || mwpd.getMaintScore().equals("")){
		    	   //计算所用时长,计算 距离评分，所用时长评分，保养得分
		    	   this.toCalculation(mwpd);
	    	   }
	    	   
	    	   //获取上一次保养时间,
	    	   String sMaintEndTime=this.togetAuditCircu(hs,mwpd,(String)objects[8]);
	    	   
	    	   map.put("sMaintEndTime", sMaintEndTime);
	    	   map.put("maintContractNo", (String)objects[1]);
	    	   map.put("projectName", (String)objects[2]);
	    	   map.put("storagename", (String)objects[3]);
	    	   map.put("username", (String)objects[4]);
	    	   map.put("phone", (String)objects[5]);
	    	   map.put("billno", (String)objects[6]);
	    	   map.put("elevatorNo", (String)objects[7]);
	    	   map.put("maintAddress", (String)objects[9]);
	    	   map.put("elevatorType", (String)objects[10]);
	    	   /**
	    		String r5=mwpd.getR5();//保养参与人员
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
	          	map.put("r5name", mwpd.getR5());//保养参与人员
	          	map.put("byrem", mwpd.getR4());//保养备注
	   		
	    	   //保养项目明细
	    	   	String hql ="from  MaintainProjectInfoWork mpiw where mpiw.singleno='"+mwpd.getSingleno()+"' order by mpiw.orderby,mpiw.maintItem";			
			    List infoList=hs.createQuery(hql).list();
			    if(infoList!=null && infoList.size()>0)
				{
					request.setAttribute("workinfoList", infoList);
				}	
	       }	
	        request.setAttribute("mapBean", map);
			request.setAttribute("maintenanceWorkPlanDetailBean", mwpd);
			
			String CSheight = PropertiesUtil.getProperty("CSheight");//签名高度
			String CSwidth = PropertiesUtil.getProperty("CSwidth");//签名宽度
			String CIheight = PropertiesUtil.getProperty("CIheight");//照片高度
			String CIwidth = PropertiesUtil.getProperty("CIwidth");//照片宽度
			request.setAttribute("CSheight",CSheight);
			request.setAttribute("CSwidth", CSwidth);
			request.setAttribute("CIheight", CIheight);
			request.setAttribute("CIwidth", CIwidth);
			
		} catch (HibernateException e1) {
			e1.printStackTrace();
	     } finally {
			try {
				hs.close();
		 } catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil
						.print(hex, "HibernateUtil Hibernate Session ");
			}
	    }	
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}		
		
		forward = mapping.findForward("maintenanceWorkPlanCompletionModify");
		
		return forward;
	}

	

	/**
	 * 点击修改的方法
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

		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;
		DynaActionForm dform = (DynaActionForm) form;
		toUpdate(form,request,errors);// 新增或修改记录
		
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("auditing.succeed"));
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("auditing.succeed"));
				} else {
					request.setAttribute("error", "Yes");
				}
				request.setAttribute("id", request.getParameter("numno"));
				forward = mapping.findForward("returnModify");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}


	/**
	 * 保存数据方法
	 * @param form
	 * @param request
	 * @return ActionErrors
	 */
	public void toUpdate(ActionForm form, HttpServletRequest request,ActionErrors errors) {
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);	
		String numno =request.getParameter("numno");
		String rem =request.getParameter("rem");//备注
		String usedDuration =request.getParameter("usedDuration");//所用时长
		String dateScore =request.getParameter("dateScore");//所用时长评分
		String distanceScore =request.getParameter("distanceScore");//距离评分
		String maintScore =request.getParameter("maintScore");//保养得分

		String fittingsReplace =request.getParameter("fittingsReplace");//配件更换情况
		String isInvoice =request.getParameter("isInvoice");//是否有单据
		
		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if(numno!=null&&!numno.equals(""))
			{
				MaintenanceWorkPlanDetail mwpd=(MaintenanceWorkPlanDetail) hs.get(MaintenanceWorkPlanDetail.class, Integer.valueOf(numno));
				if(mwpd!=null)
				{
					mwpd.setUsedDuration(Double.parseDouble(usedDuration));
					mwpd.setDateScore(Double.parseDouble(dateScore));
					mwpd.setDistanceScore(Double.parseDouble(distanceScore));
					mwpd.setMaintScore(Double.parseDouble(maintScore));
					mwpd.setRem(rem);
					mwpd.setFittingsReplace(fittingsReplace);
					mwpd.setIsInvoice(isInvoice);
					mwpd.setByAuditOperid(userInfo.getUserID());
					mwpd.setByAuditDate(CommonUtil.getNowTime());
					mwpd.setDjOperId2(userInfo.getUserID());
					mwpd.setDjOperDate2(CommonUtil.getNowTime());
					hs.update(mwpd);
				}
			}
			tx.commit();
		} catch (Exception e1) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			try {
				tx.rollback();
			} catch (HibernateException e2) {
				log.error(e2.getMessage());
				DebugUtil.print(e2, "Hibernate Transaction rollback error!");
			}
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Insert error!");
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}
		
	 }

	
	//获取上一次保养时间
	private String togetAuditCircu(Session hs, MaintenanceWorkPlanDetail mwpd,String str) {

 	    //获取上一次保养时间
		String sMaintEndTime="";
//		String sql="select mwpd.maintEndTime from MaintenanceWorkPlanDetail mwpd" +
//				" where mwpd.numno=(select numno-1 from MaintenanceWorkPlanDetail " +
//				"where numno='"+mwpd.getNumno()+"' and billno='"+str+"' " +
//				"and numno !=(select MIN(numno)from MaintenanceWorkPlanDetail where billno='"+str+"'))";
		
		String sql="select max(maintEndTime) from MaintenanceWorkPlanDetail "
				+ "where  billno='"+str+"' "
				+ "and MaintDate=(select MAX(MaintDate) from MaintenanceWorkPlanDetail "
				+ "where  billno='"+str+"' and MaintDate<'"+mwpd.getMaintDate()+"')";
		
		//System.out.println(">>>"+sql);
		List list=hs.createSQLQuery(sql).list();
		if(list!=null&&list.size()>0){
			sMaintEndTime=(String) list.get(0);
	    }
	    return sMaintEndTime;
	}
	
	//计算所用时长，所用时长评分,距离评分，保养得分
	private void toCalculation(MaintenanceWorkPlanDetail mwpd){
		
		//计算所用时长
		double usedDuration=CommonUtil.getMinute(mwpd.getMaintStartTime(), mwpd.getMaintEndTime());
		//暂停时长
		double stopduration=0;
		if(mwpd.getStopTime()!=null && !mwpd.getStopTime().equals("")){
			stopduration=CommonUtil.getMinute(mwpd.getStopTime(), mwpd.getRestartTime());
		}
		//所用时长减去暂停时长
		usedDuration=usedDuration-stopduration;
 	    mwpd.setUsedDuration(usedDuration);
 	    
 	    /**所用时长评分
 	      	所用时长评分标准						分数
 	      0.9标准工时~ 2标准工时					10分
 	      0.8标准工时~0.9标准工时 或者 2标准工时~3标准工时	8分
 	      0.7标准工时~0.8标准工时 或者 3标准工时~4标准工时	7分
 	      0.6标准工时~0.7标准工时 或者 4标准工时~5标准工时	6分
 	      15分钟 到 0.6标准工时						5分
 	      15分钟以下 或者 5标准工时以上					0分
 	     */
 	    double datescore=0d;//所用时长评分
 	    double maintdatetime=Double.parseDouble(mwpd.getMaintDateTime());//标准保养时间
 	    double score=usedDuration/maintdatetime;
 	    if(score>=0.9 && usedDuration<=(maintdatetime*2)){
 	    	datescore=10;//0.9标准工时~ 2标准工时
 	    }else if(score>=0.8 && score<0.9){
 	    	datescore=8;//0.8标准工时~0.9标准工时
 	    }else if(usedDuration>(maintdatetime*2) && usedDuration<=(maintdatetime*3)){
 	    	datescore=8;//2标准工时~3标准工时
 	    }else if(score>=0.7 && score<0.8){
 	    	datescore=7;//0.7标准工时~0.8标准工时
 	    }else if(usedDuration>(maintdatetime*3) && usedDuration<=(maintdatetime*4)){
 	    	datescore=7;//3标准工时~4标准工时
 	    }else if(score>=0.6 && score<0.7){
 	    	datescore=6;//0.6标准工时~0.7标准工时
 	    }else if(usedDuration>(maintdatetime*4) && usedDuration<=(maintdatetime*5)){
 	    	datescore=6;//4标准工时~5标准工时
 	    }else if(usedDuration>=15 && score<0.6){
 	    	datescore=5;//15分钟 到 0.6标准工时
 	    }else{
 	    	datescore=0;
 	    }
 	    mwpd.setDateScore(datescore);//所用时长评分
 	    
 	    /**距离评分   [两次距基点距离平均值] 
		  	距离评分标准	分数
			0-200m	10分
			200-500	5分
			500米以上	0分
	     */
 	    double distancescore=0;
 	    double startdistance=mwpd.getStartDistance();//保养开始距离(米)
 	    double enddistance=mwpd.getEndDistance();//保养结束距离(米)
 	    int avgnum=2;
 	    double stopDistance=0;//暂停距离(米)
 	    if(mwpd.getStopDistance()!=null){
 	    	stopDistance=mwpd.getStopDistance();
 	    	avgnum++;
 	    }
 	    double restartDistance=0;//复工距离(米)
 	    if(mwpd.getRestartDistance()!=null){
 	    	restartDistance=mwpd.getRestartDistance();
 	    	avgnum++;
 	    }
 	    double distance=(startdistance+enddistance+stopDistance+restartDistance)/avgnum;//平均值
 	    
 	    if(distance>=0 && distance<=200){
 	    	distancescore=10;//0-200m	10分
 	    }else if(distance>200 && distance<=500){
 	    	distancescore=5;//200-500	5分
 	    }else{
 	    	distancescore=0;
 	    }
 	    mwpd.setDistanceScore(distancescore);//距离评分
 	    
 	    /**保养得分 
 	     * 任一一项得分为0分，则总分为0分。
 	     * 60%时间评分+40%距离评分
 	     */
 	    double maintscore=0;
 	    if(datescore>0 && distancescore>0){
 	    	maintscore=datescore*0.6+distancescore*0.4;
 	    }
 	    mwpd.setMaintScore(maintscore);//保养得分 
 	    
	}

	/**
	 * 跳转到修改页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareUpdate2Record(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws DataStoreException {
		
		request.setAttribute("navigator.location","维保计划完成情况 >> 单据录入");	
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		if(id==null||id.equals(""))
		{
			id=(String) request.getAttribute("id");
		}
		ActionErrors errors = new ActionErrors();		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		MaintenanceWorkPlanDetail mwpd=null;
		HashMap map=new HashMap();
		try{
			hs = HibernateUtil.getSession();
			String sql="select mwpd,mcm.maintContractNo,mcd.projectName,si.storagename,lu.username,lu.phone," +
					"mcm.billNo,mcd.elevatorNo,mwpm.billno,mcd.maintAddress,mcd.elevatorType "
					+ "from  MaintenanceWorkPlanDetail mwpd,MaintenanceWorkPlanMaster mwpm,MaintContractDetail mcd,MaintContractMaster mcm,Loginuser lu,Storageid si "
					+ "where mcd.billNo=mcm.billNo "
					+ " and si.storageid=mcd.assignedMainStation "
					+ " and lu.userid=mwpd.maintPersonnel "
					+ " and mcd.rowid=mwpm.rowid "
					+ " and mwpd.maintenanceWorkPlanMaster.billno=mwpm.billno "
					+ " and mwpd.numno ="+id;
	        List list = hs.createQuery(sql).list();
	        
	       if(list!=null&&list.size()>0)
	       {
	    	   Object[] objects=(Object[]) list.get(0);
	    	   mwpd=(MaintenanceWorkPlanDetail) objects[0];
	    	   mwpd.setReceivingPerson(bd.getName(hs, "Loginuser", "username", "userid", mwpd.getReceivingPerson()));
	    	   mwpd.setByAuditOperid(bd.getName(hs, "Loginuser", "username", "userid", mwpd.getByAuditOperid()));
	    	   mwpd.setDjOperId2(userInfo.getUserName());
	    	   mwpd.setDjOperDate2(CommonUtil.getNowTime());
    	  
	    	 	 String str=mwpd.getIsInvoice();
	    	   if(str==null||str.equals(""))
	    	   {
	    		   mwpd.setIsInvoice("N");
	    	   }
	    	   
	    	   //获取上一次保养时间,
	    	   String sMaintEndTime=this.togetAuditCircu(hs,mwpd,(String)objects[8]);
	    	   
	    	   map.put("sMaintEndTime", sMaintEndTime);
	    	   map.put("maintContractNo", (String)objects[1]);
	    	   map.put("projectName", (String)objects[2]);
	    	   map.put("storagename", (String)objects[3]);
	    	   map.put("username", (String)objects[4]);
	    	   map.put("phone", (String)objects[5]);
	    	   map.put("billno", (String)objects[6]);
	    	   map.put("elevatorNo", (String)objects[7]);
	    	   map.put("maintAddress", (String)objects[9]);
	    	   map.put("elevatorType", (String)objects[10]);
	    	   /**
	    		String r5=mwpd.getR5();//保养参与人员
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
	          	map.put("r5name", mwpd.getR5());//保养参与人员
	          	map.put("byrem", mwpd.getR4());//保养备注
	   		
	    	   //保养项目明细
	    	   	String hql ="from  MaintainProjectInfoWork mpiw where mpiw.singleno='"+mwpd.getSingleno()+"' order by mpiw.orderby,mpiw.maintItem";			
			    List infoList=hs.createQuery(hql).list();
			    if(infoList!=null && infoList.size()>0)
				{
					request.setAttribute("workinfoList", infoList);
				}	
	       }	
	        request.setAttribute("mapBean", map);
			request.setAttribute("maintenanceWorkPlanDetailBean", mwpd);
			
			String CSheight = PropertiesUtil.getProperty("CSheight");//签名高度
			String CSwidth = PropertiesUtil.getProperty("CSwidth");//签名宽度
			String CIheight = PropertiesUtil.getProperty("CIheight");//照片高度
			String CIwidth = PropertiesUtil.getProperty("CIwidth");//照片宽度
			request.setAttribute("CSheight",CSheight);
			request.setAttribute("CSwidth", CSwidth);
			request.setAttribute("CIheight", CIheight);
			request.setAttribute("CIwidth", CIwidth);
			
		} catch (Exception e1) {
			e1.printStackTrace();
	     } finally {
			try {
				hs.close();
		 } catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil
						.print(hex, "HibernateUtil Hibernate Session ");
			}
	    }	
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}		
		
		forward = mapping.findForward("maintenanceWorkPlanCompletionModify2");
		
		return forward;
	}

	/**
	 * 点击修改的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toUpdate2Record(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	
		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;
		DynaActionForm dform = (DynaActionForm) form;
		toUpdate2(form,request,errors);// 新增或修改记录
		
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				request.setAttribute("id", request.getParameter("numno"));
				forward = mapping.findForward("returnModify2");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * 保存数据方法
	 * @param form
	 * @param request
	 * @return ActionErrors
	 */
	public void toUpdate2(ActionForm form, HttpServletRequest request,ActionErrors errors) {
		
		DynaActionForm dform = (DynaActionForm) form;
	
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);	
		String numno =request.getParameter("numno");
	
		String fittingsReplace =request.getParameter("fittingsReplace");//配件更换情况
		String isInvoice =request.getParameter("isInvoice");//是否有单据
		
		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if(numno!=null&&!numno.equals(""))
			{
				MaintenanceWorkPlanDetail mwpd=(MaintenanceWorkPlanDetail) hs.get(MaintenanceWorkPlanDetail.class, Integer.valueOf(numno));
				if(mwpd!=null){
					mwpd.setFittingsReplace(fittingsReplace);
					mwpd.setIsInvoice(isInvoice);
					mwpd.setDjOperId2(userInfo.getUserID());
					mwpd.setDjOperDate2(CommonUtil.getNowTime());
					hs.update(mwpd);
				}
			}
			tx.commit();
		} catch (Exception e1) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			try {
				tx.rollback();
			} catch (HibernateException e2) {
				log.error(e2.getMessage());
				DebugUtil.print(e2, "Hibernate Transaction rollback error!");
			}
			e1.printStackTrace();
			log.error(e1.getMessage());
			DebugUtil.print(e1, "Hibernate region Insert error!");
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}
		
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
			folder ="MaintenanceWorkPlanDetail.file.upload.folder";
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
	public ActionForward toPreparePrintRecord(ActionMapping mapping,ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	ActionErrors errors = new ActionErrors();
    	HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Connection conn = null;
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		List etcpList=new ArrayList();
		List hecirList=new ArrayList();
		MaintenanceWorkPlanDetail mwpd=null;
		HashMap map=new HashMap();
		if (id != null && !id.equals("")) {			
			try {
				hs = HibernateUtil.getSession();
				DataOperation op = new DataOperation();
				conn = (Connection) hs.connection();
				op.setCon(conn);
				String sql="select mwpd,mcm.maintContractNo,mcd.projectName,si.storagename,lu.username,lu.phone,mcm.billNo," +
						"mcd.elevatorNo,mcd.maintAddress,mwpm.billno,mcd.elevatorType "
						+"from  MaintenanceWorkPlanDetail mwpd,MaintenanceWorkPlanMaster mwpm," 
						+"MaintContractDetail mcd,MaintContractMaster mcm,Loginuser lu,Storageid si "
						+ "where mcd.billNo=mcm.billNo "
						+ " and si.storageid=mcd.assignedMainStation "
						+ " and lu.userid=mwpd.maintPersonnel "
						+ " and mcd.rowid=mwpm.rowid "
						+ " and mwpd.maintenanceWorkPlanMaster.billno=mwpm.billno "
						+ " and mwpd.numno ="+id;
				Query query = hs.createQuery(sql);
				List list = query.list();
			       if(list!=null && list.size()>0)
			       {
			    	   Object[] objects=(Object[]) list.get(0);
			    	   mwpd=(MaintenanceWorkPlanDetail) objects[0];
			    	   String CompanyID=bd.getName("MaintContractMaster", "companyId", "maintContractNo",(String)objects[1]);
			    	   mwpd.setR3(bd.getName(hs, "Customer", "companyName", "companyId",CompanyID));
			    	   mwpd.setR2(bd.getName(hs, "ElevatorCoordinateLocation","rem", "elevatorNo",(String)objects[7]));
			    	  // mwpd.setDjOperId2(bd.getName(hs, "Loginuser", "username", "userid", mwpd.getDjOperId2()));
			    	   map.put("maintContractNo", (String)objects[1]);
			    	   map.put("projectName", (String)objects[2]);
			    	   map.put("storagename", (String)objects[3]);
			    	   map.put("username", (String)objects[4]);
			    	   map.put("phone", (String)objects[5]);
			    	   map.put("billno", (String)objects[6]);
			    	   map.put("elevatorNo", (String)objects[7]);
			    	   map.put("maintAddress", (String)objects[8]);
			    	   map.put("elevatorType", (String)objects[10]);
			    	   
			    	   if(mwpd.getMaintType().equals("halfMonth")){
			    		   mwpd.setMaintType("半月保养");
							}
						if(mwpd.getMaintType().equals("quarter")){
							mwpd.setMaintType("季度保养");
							}
						if(mwpd.getMaintType().equals("halfYear")){
							mwpd.setMaintType("半年保养");
							}
						if(mwpd.getMaintType().equals("yearDegree")){
							mwpd.setMaintType("年度保养");
							}
			    	   String sMaintEndTime=this.togetAuditCircu(hs,mwpd,(String)objects[9]);
			    	   map.put("sMaintEndTime", sMaintEndTime);//上次保养时间
			    	   /**
			    	   String r5=mwpd.getR5();//保养参与人员
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
			          	map.put("r5name", mwpd.getR5());//保养参与人员
			          	map.put("byrem", mwpd.getR4());//保养备注
			    	   
			    	   etcpList.add(map);
			    	   //保养项目明细
			    	   	String sql1 ="select * from  MaintainProjectInfoWork mpiw where mpiw.singleno='"+mwpd.getSingleno()+"' order by mpiw.orderby,mpiw.maintItem";			
			    	   	hecirList =op.queryToList(sql1);
					
					//对barCodeList、noticeList操作
					BarCodePrint dy = new BarCodePrint();
					List barCodeList = new ArrayList();
					barCodeList.add(mwpd);
					barCodeList.add(hecirList);
					barCodeList.add(etcpList);
					dy.toPrintTwoRecord7(request,response, barCodeList,id);
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
	 * 删除记录
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

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			String id = request.getParameter("id");	

			String delsql="update MaintenanceWorkPlanDetail set "
					+ "handlestatus=null,IsTransfer=null,TransferDate=null,"
					+ "ReceivingPerson=null,ReceivingPhone=null,ReceivingDate=null,"
					+ "singleno=null,ReceivingTime=null,MaintStartTime=null,"
					+ "BeginLongitude=null,BeginDimension=null,MaintStartAddres=null,MaintEndTime=null,"
					+ "EndLongitude=null,EndDimension=null,MaintEndAddres=null,UsedDuration=null,AuditCircu=null,"
					+ "Rem=null,FittingsReplace=null,IsInvoice=null,byAuditOperid=null,byAuditDate=null,"
					+ "R1=null,R2=null,R3=null,R4=null,R5=null,R6=null,R7=null,"
					+ "R8=null,R9=null,R10=null,djOperId2=null,djOperDate2=null,StartDistance=null,"
					+ "EndDistance=null,DistanceScore=null,DateScore=null,MaintScore=null,"
					+ "StopTime=null,StopLongitude=null,StopDimension=null,StopAddres=null,"
					+ "StopDistance=null,RestartTime=null,RestartLongitude=null,RestartDimension=null,"
					+ "RestartAddres=null,RestartDistance=null,BeginLongitudeGPS=null,BeginDimensionGPS=null,"
					+ "EndLongitudeGPS=null,EndDimensionGPS=null,StopLongitudeGPS=null,StopDimensionGPS=null,"
					+ "RestartLongitudeGPS=null,RestartDimensionGPS=null,CustomerSignature=null,CustomerImage=null "
					+ "where numno in("+id+")";
			hs.connection().prepareStatement(delsql).execute();

			tx.commit();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","清除保养状态成功！"));
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","清除保养状态失败！"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		}  finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
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
	 * 删除记录
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDeleteRecord2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			String id = request.getParameter("id");	

			String delsql="delete from MaintenanceWorkPlanDetail where numno in("+id+")";
			hs.connection().prepareStatement(delsql).execute();

			tx.commit();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","删除保养计划成功！"));
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","删除保养计划失败！"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		}  finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
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

	public HttpServletResponse toExcelRecord(ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ServeTableForm tableForm = (ServeTableForm) form;

		String mainStation = tableForm.getProperty("assignedMainStation");//维保站
		if(mainStation==null || mainStation.trim().equals("")){
			mainStation="%";
		}
		String maintPersonnel = tableForm.getProperty("maintPersonnel");//维保工
		if(maintPersonnel==null || maintPersonnel.trim().equals("")){
			maintPersonnel="%";
		}
		String r5 = tableForm.getProperty("r5");//参与维保工
		if(r5==null || r5.trim().equals("")){
			r5="%";
		}
		String singleno = tableForm.getProperty("singleno");//保养单号
		if(singleno==null || singleno.trim().equals("")){
			singleno="%";
		}
		String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号
		if(elevatorNo==null || elevatorNo.trim().equals("")){
			elevatorNo="%";
		}
		
		String sdate1=(String) tableForm.getProperty("sdate1");
		String edate1=(String) tableForm.getProperty("edate1");
		String auditType=(String) tableForm.getProperty("auditType");
		if(sdate1==null || sdate1.trim().equals("")){
			sdate1="0000-00-00";
		}
		if(edate1==null || edate1.trim().equals("")){
			edate1="9999-99-99";
		}

		Session hs = null;
		XSSFWorkbook wb = new XSSFWorkbook();
		
		try{
			hs = HibernateUtil.getSession();

			String sqlk="EXEC SP_SELECT_MAINTENANCEWORKPLANMASTER "
					+ "'"+singleno.trim()+"','"+elevatorNo.trim()+"','"+maintPersonnel.trim()+"','"+r5.trim()+"',"
					+ "'"+mainStation.trim()+"','"+auditType.trim()+"','"+sdate1.trim()+"','"+edate1.trim()+"',"
					+ "0,0,'maintDate' ";
			//System.out.println(">>>"+sqlk);
			ResultSet rs=hs.connection().prepareCall(sqlk).executeQuery();
			
	
			String[] titlename={"电梯编号","保养单号","维保站","维保工","联系电话","项目名称","保养类型",
					"保养计划日期","保养接收时间","保养开始时间","保养结束时间","保养得分","保养人员"};
			String[] titleid={"elevatorNo","singleno","storagename","username","phone","projectName","maintTypeName",
					"maintDate","receivingTime","maintStartTime","maintEndTime","maintScore","r5name"};
			
	        XSSFSheet sheet = wb.createSheet("维保计划完成情况");
	        
	        //创建单元格样式
	        XSSFCellStyle cs = wb.createCellStyle();
	        cs.setAlignment(XSSFCellStyle.ALIGN_CENTER);//左右居中
	        cs.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);//上下居中
	        XSSFFont f  = wb.createFont();
	        f.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 字体加粗
	        cs.setFont(f);
	        
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
			XSSFRow row = null;
			XSSFCell cell =null;
			int j=0;
			while(rs.next()) {
				// 创建Excel行，从0行开始
				row = sheet.createRow(j+1);
				for(int c=0;c<titleid.length;c++){
				    cell = row.createCell((short)c);
					//cell.setEncoding(HSSFCell.ENCODING_UTF_16);

			    	String valstr=rs.getString(titleid[c]);
				    if(titleid[c].equals("maintScore")){
				    	if(valstr!=null && !valstr.trim().equals("")){
					    	cell.setCellValue(Double.parseDouble(valstr));
				    	}else{
				    		cell.setCellValue("");
				    	}
				    }else{
				    	cell.setCellValue(valstr);
				    }
				    
				}
				j++;
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="+URLEncoder.encode("维保计划完成情况", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		
		return response;
	}
	/**
	 * Method toPrepareDeleteRecord execute,prepare data for delete page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */

	public ActionForward toPrepareDeleteRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response){

		request.setAttribute("navigator.location", "维保计划完成情况>> 批量删除");

		
		request.setAttribute("display", "N");
		request.setAttribute("deletemore", "Y");
		return mapping.findForward("maintenanceWorkPlanCompletionDelete");
	}
	/**
	 * 删除记录
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toDeleteMoreRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		Session hs = null;
		Transaction tx = null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			String elevatorNo = request.getParameter("elevatorNo");	
			String sdate = request.getParameter("sdate");	
			String edate = request.getParameter("edate");	

			String delsql="delete from MaintenanceWorkPlanDetail where billno in(select billno from MaintenanceWorkPlanMaster where elevatorNo='"+elevatorNo+"')";
			if(sdate!=null&&!"".equals(sdate)){
				delsql=delsql+" and maintDate>='"+sdate+"'";
			}
			if(edate!=null&&!"".equals(edate)){
				delsql=delsql+" and maintDate<='"+edate+"'";
			}
			hs.connection().prepareStatement(delsql).execute();

			tx.commit();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","批量删除保养计划成功！"));
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","批量删除保养计划失败！"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
		}  finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
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
}
