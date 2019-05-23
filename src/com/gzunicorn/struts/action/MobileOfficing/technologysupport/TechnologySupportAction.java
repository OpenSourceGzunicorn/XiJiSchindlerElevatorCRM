package com.gzunicorn.struts.action.MobileOfficing.technologysupport;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;










import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;










import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.mobileofficeplatform.technologysupport.TechnologySupport;
import com.gzunicorn.hibernate.mobileofficeplatform.technologysupportfiles.TechnologySupportFiles;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class TechnologySupportAction extends DispatchAction {

	Log log = LogFactory.getLog(TechnologySupportAction.class);
	
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
	 * 申请技术支持管理
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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "technologysupport", null);
		/** **********结束用户权限过滤*********** */

		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			ActionForward forward = super.execute(mapping, form, request,response);
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
		
		request.setAttribute("navigator.location","申请技术支持管理 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();	
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		request.setAttribute("userroleid", userInfo.getRoleID());
		
			HTMLTableCache cache = new HTMLTableCache(session, "technologySupportList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fTechnologySupport");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("ts.SingleNo");
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
			String singleNo = tableForm.getProperty("singleNo");
			String maintDivision = tableForm.getProperty("maintDivision");
			String assignUser = tableForm.getProperty("assignUser");
			String hproStatus = tableForm.getProperty("hproStatus");//处理状态
			
			String sdate1=tableForm.getProperty("sdate1");
			String edate1=tableForm.getProperty("edate1");
			
			String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
			String day1=DateUtil.getDate(day, "MM", -1);//当前日期月份加1 。
			if(sdate1==null || sdate1.trim().equals("")){
				sdate1=day1;
				tableForm.setProperty("sdate1",sdate1);
			}
			if(edate1==null || edate1.trim().equals("")){
				edate1=day;
				tableForm.setProperty("edate1",edate1);
			}
			
			
			String mainStation=null;
			if (!userInfo.getComID().equals("00")) {
				maintDivision = userInfo.getComID();
				if (userInfo.getStorageId() != null ) {
					mainStation = userInfo.getStorageId();
				}
			}
			if(userInfo.getRoleID().equals("A50")){
				assignUser=userInfo.getUserID();
			}
			
			
			Session hs = null;
			Query query=null;
			String order="";
			try {

				hs = HibernateUtil.getSession();
				String[] colNames = {
						"ts.billno as billno",
						"c.ComFullName as maintDivision",
						"si.storagename as maintStation",
						"lu.UserName as assignUser",
						"ts.hmtId as hmtId",
						"ts.faultCode as faultCode",
						"ts.assignUserTel as assignUserTel",
		                "ts.faultStatus as faultStatus",
		                "ts.operDate as operDate",
		                "mslu.UserName as msprocessPeople",
		                "mmlu.UserName as mmprocessPeople",
		                "tslu.UserName as tsprocessPeople",
		                "ts.proStatus as proStatus",
		                "ts.SingleNo as singleNo"
				};
				String sql="select "+StringUtils.join(colNames, ",")+"  from TechnologySupport ts "
						+ "left join LoginUser mslu on mslu.UserID=ts.MSProcessPeople "
						+ "left join LoginUser mmlu on mmlu.UserID=ts.MMProcessPeople "
						+ "left join LoginUser tslu on tslu.UserID=ts.TSProcessPeople,"
						+ "HotlineMotherboardType hmt,LoginUser lu,Company c,Storageid si "
						+ "where c.ComID=ts.MaintDivision and si.StorageID=ts.MaintStation "
						+ "and hmt.hmtId=ts.hmtId and ts.AssignUser=lu.UserID";
				
				if (hproStatus != null && !hproStatus.equals("")) {
					sql+=" and ts.proStatus like '"+hproStatus.trim()+"'";	
				}
				if (singleNo != null && !singleNo.equals("")) {
					sql+=" and ts.SingleNo like '%"+singleNo.trim()+"%'";	
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql+=" and ts.maintDivision like '"+maintDivision.trim()+"'";	
				}
				if (mainStation != null && !mainStation.equals("")) {
					sql+=" and ts.maintStation like '"+mainStation.trim()+"%'";
				}			
				if (sdate1 != null && !sdate1.equals("")) {
					sql+=" and ts.operDate >= '"+sdate1.trim()+" 00:00:00"+"'";
				}	
				if (edate1 != null && !edate1.equals("")) {
					sql+=" and ts.operDate <= '"+edate1.trim()+" 23:59:59"+"'";
				}	
				
				if (assignUser != null && !assignUser.equals("")) {
					sql+=" and (lu.UserID like '"+assignUser.trim()+"%' or lu.UserName like '%"+assignUser.trim()+"%')";
				}
				if (table.getIsAscending()) {
					order+=" order by "+table.getSortColumn();
				} else {
					order+=" order by "+table.getSortColumn()+" desc";
				}
				
				
				query=hs.createSQLQuery(sql+order);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);

				List list=query.list();
				List technologySupportList = new ArrayList();
				for(int j=0;j<colNames.length;j++)
                {
                	colNames[j]=colNames[j].split(" as ")[1];
                }
                List maintenanceReceiptList=new ArrayList();
                if(list!=null &&list.size()>0)
                {
                	for(int i=0;i<list.size();i++)
                	{
                	    Object[] objects=(Object[]) list.get(i);
                		HashMap map= new HashMap();
                    	for(int j=0;j<colNames.length;j++)
    					{
                    		map.put(colNames[j],objects[j]);
    					}
                    	String proStatus = (String)map.get("proStatus");
						if (proStatus.trim().equals("1")) {
							map.put("proStatus", "站长处理");
						} else if (proStatus.trim().equals("2")) {
							map.put("proStatus", "经理处理");
						} else if (proStatus.trim().equals("3")) {
							map.put("proStatus", "技术支持处理");
						}else if (proStatus.trim().equals("4")) {
							map.put("proStatus", "处理完成");
						}else {
							map.put("proStatus", null);
						}
                    	technologySupportList.add(map);
                	}
                }
				
				
				
				table.addAll(technologySupportList);
				request.setAttribute("CompanyList", Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
				session.setAttribute("technologySupportList", table);	
			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (HibernateException e1) {

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
			forward = mapping.findForward("technologySupportList");
		
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
		request.setAttribute("navigator.location", messages.getMessage(locale,
				navigation));
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
		
		request.setAttribute("navigator.location","申请技术支持管理 >> 查看");

		ActionForward forward = null;
		String id = (String) dform.get("id");
		Session hs = null;
		TechnologySupport technologySupport = null;
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from TechnologySupport ts where ts.billno = :billno");
				query.setString("billno", id);
				List list = query.list();
				if (list != null && list.size() > 0) {
					technologySupport = (TechnologySupport) list.get(0);
					technologySupport.setHmtId(bd.getName(hs, "HotlineMotherboardType", "hmtName", "hmtId",technologySupport.getHmtId()));
					technologySupport.setMaintDivision(bd.getName(hs, "Company", "ComFullName", "ComID", technologySupport.getMaintDivision()));
					technologySupport.setMaintStation(bd.getName(hs, "StorageID", "StorageName", "StorageID", technologySupport.getMaintStation()));
					technologySupport.setAssignUser(bd.getName(hs, "LoginUser", "UserName", "UserID", technologySupport.getAssignUser()));
					technologySupport.setMsprocessPeople(bd.getName(hs, "LoginUser", "UserName", "UserID", technologySupport.getMsprocessPeople()));
					technologySupport.setMmprocessPeople(bd.getName(hs, "LoginUser", "UserName", "UserID", technologySupport.getMmprocessPeople()));
					technologySupport.setTsprocessPeople(bd.getName(hs, "LoginUser", "UserName", "UserID", technologySupport.getTsprocessPeople()));
					String i=technologySupport.getMmisResolve();
					if(i!=null&&!i.equals(""))
					{
					if(i.equals("Y"))
					{
						technologySupport.setMmisResolve("已解决");
					}else{
						technologySupport.setMmisResolve("未解决");
					}
				    }
					String i1=technologySupport.getMsisResolve();
					if(i1!=null&&!i1.equals(""))
					{
					if(i1.equals("Y"))
					{
						technologySupport.setMsisResolve("已解决");
					}else{
						technologySupport.setMsisResolve("未解决");
					}
				    }
				
				} 
				
				if (technologySupport == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (HibernateException e1) {
				e1.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}

			request.setAttribute("display", "yes");
			request.setAttribute("technologySupportBean", technologySupport);
			forward = mapping.findForward("technologySupportDisplay");

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
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
		
		String folder = request.getParameter("folder");		//文件夹
		if(null == folder || "".equals(folder)){
			folder ="TechnologySupport.file.upload.folder";
		}
		folder = PropertiesUtil.getProperty(folder);
		
		try {
			hs = HibernateUtil.getSession();
			
			String sql="from TechnologySupport t where t.billno ='"+filesid.trim()+"'";
			List list2=hs.createQuery(sql).list();
			String gzimage="";
			if(list2!=null && list2.size()>0){
				TechnologySupport ts=(TechnologySupport)list2.get(0);
				gzimage=ts.getGzImage();
				localPath = folder+gzimage;
			}
		
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			OutputStream fos = null;
			InputStream fis = null;
	
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(gzimage, "utf-8"));
	
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
		List list2=new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='GBK'?>");
		sb.append("<root>");
		try {
			hs=HibernateUtil.getSession();
			if(comid!=null && !"".equals(comid)){
			String hql="select a from Storageid a,Company b where a.comid = b.comid and a.comid="+comid+" and a.storagetype=1";
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
				// TODO Auto-generated catch block
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
	 * 删除
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

			TechnologySupport support = (TechnologySupport) hs.get(TechnologySupport.class, (String) dform.get("id"));
			if (support != null) {
				hs.delete(support);
				 errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.succeed"));
			}
			tx.commit();
		} catch (Exception e2) {
			e2.printStackTrace();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
			}
		} finally {
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
