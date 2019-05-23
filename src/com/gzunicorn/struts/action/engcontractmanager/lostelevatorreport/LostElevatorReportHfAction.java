package com.gzunicorn.struts.action.engcontractmanager.lostelevatorreport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.engcontractmanager.lostelevatorreport.LostElevatorReport;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

/**
 * 维保合同退保回访结果
 * @author Lijun
 *
 */
public class LostElevatorReportHfAction extends DispatchAction {

	Log log = LogFactory.getLog(LostElevatorReportAuditAction.class);

	BaseDataImpl bd = new BaseDataImpl();

	/**
	 * 维保合同退保分部长审核
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

		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "lostelevatorreporthf", null);
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
		
		request.setAttribute("navigator.location","维保合同退保回访 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "lostElevatorReportHfList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fLostElevatorReportHf");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		cache.updateTable(table);
		table.setSortColumn("jnlno");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		} else {
			table.setFrom(0);
		}
		cache.saveForm(tableForm);
		
		String jnlno = tableForm.getProperty("jnlno");// 流水号
		String maintContractNo = tableForm.getProperty("maintContractNo");// 维保合同号
		String projectName = tableForm.getProperty("projectName");// 甲方单位
		String maintDivision = tableForm.getProperty("maintDivision");// 所属维保站			

		String isfhRem = tableForm.getProperty("isfhRem");//未回访
		if(isfhRem==null || isfhRem.trim().equals("")){
			isfhRem="N";
			tableForm.setProperty("isfhRem",isfhRem);
		}

		//第一次进入页面时根据登陆人初始化所属维保分部
		List maintDivisionList = Grcnamelist1.getgrcnamelist(userInfo.getUserID());
		if(maintDivision == null || maintDivision.equals("")){
			Map map = (Map)maintDivisionList.get(0);
			maintDivision = (String)map.get("grcid");
		}
		
		Session hs = null;
		Query query = null;
		try {
			
			hs = HibernateUtil.getSession();
			
			String sql = "select a,b.comname,c.pullname,s.storagename " +
					" from LostElevatorReport a,Company b,Pulldown c,Storageid s"+
					" where a.maintDivision=b.comid and a.contractNatureOf=c.id.pullid "+
					" and a.submitType='Y' "+//提交的
					" and a.maintStation = s.storageid"+
					" and c.id.typeflag='LostElevatorReport_ContractNatureOf' ";
			
			if (jnlno != null && !jnlno.equals("")) {
				sql += " and a.jnlno like '%"+jnlno.trim()+"%'";
			}
			if (maintContractNo != null && !maintContractNo.equals("")) {
				sql += " and a.maintContractNo like '%"+maintContractNo.trim()+"%'";
			}
			if (projectName != null && !projectName.equals("")) {
				sql += " and a.projectName like '%"+projectName.trim()+"%'";
			}
			if (maintDivision != null && !maintDivision.equals("")) {
				sql += " and a.maintDivision like '"+maintDivision.trim()+"'";
			}
			if (isfhRem != null && !isfhRem.equals("") && !isfhRem.equals("%")) {
				if(isfhRem.trim().equals("Y")){
					sql += " and isnull(a.fhRem,'')<>''";
				}else{
					sql += " and isnull(a.fhRem,'')=''";
				}
			}
			
			String order = " order by ";
			order += table.getSortColumn();
			
			if (table.getIsAscending()) {
				sql += order + " desc";
			} else {
				sql += order + " asc";
			}
			
			query = hs.createQuery(sql);
			table.setVolume(query.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List list = query.list();
			List lostElevatorReportAuditList = new ArrayList();
			for (Object object : list) {
				Object[] objs = (Object[])object;
				LostElevatorReport master = (LostElevatorReport) objs[0];
				master.setMaintDivision(objs[1].toString());
				master.setContractNatureOf(objs[2].toString());
				master.setMaintStation(objs[3].toString());
				
				if(master.getFhRem()==null || master.getFhRem().trim().equals("")){
					master.setFhRem("N");
				}else{
					master.setFhRem("Y");
				}
				
				lostElevatorReportAuditList.add(master);
			}

			table.addAll(lostElevatorReportAuditList);
			session.setAttribute("lostElevatorReportHfList", table);
			// 分部下拉框列表
			request.setAttribute("maintDivisionList", maintDivisionList);

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
		forward = mapping.findForward("lostElevatorReportHfList");
		
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
		
		request.setAttribute("navigator.location","维保合同退保回访 >> 查看");
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;
		DynaActionForm dform = (DynaActionForm) form;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		String id = request.getParameter("id");
		
		Session hs = null;
		LostElevatorReport report=null;
	
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				String sql="select a,b.comname,c.storagename "+
				"from LostElevatorReport a,Company b,Storageid c "+
				"where a.maintDivision=b.comid and a.maintStation=c.storageid "+ 
				" and a.jnlno='"+id+"'";
				Query query = hs.createQuery(sql);
				List list = query.list();
				if (list != null && list.size() > 0) {
					for(Object object : list){
						Object[] objs=(Object[])object;
						report=(LostElevatorReport) objs[0];
						report.setMaintDivision(objs[1].toString());
						report.setMaintStation(objs[2].toString());
						String[] a={"a.id.typeflag='LostElevatorReport_ContractNatureOf'"};
						report.setContractNatureOf(bd.getName("Pulldown", "pullname", "id.pullid", report.getContractNatureOf(), a));
						String[] b={"a.id.typeflag='LostElevatorReport_CauseAnalysis'"};
						//report.setCauseAnalysis(bd.getName("Pulldown", "pullname", "id.pullid", report.getCauseAnalysis(), b));
						report.setR4(bd.getName("Pulldown", "pullname", "id.pullid", report.getCauseAnalysis(), b));
					}
					
					report.setOperId(bd.getName("Loginuser", "username", "userid", report.getOperId()));
					report.setAuditOperid(bd.getName("Loginuser", "username", "userid", report.getAuditOperid()));
					report.setAuditOperid3(bd.getName("Loginuser", "username", "userid", report.getAuditOperid3()));
					
					if(report.getHfOperid()==null || report.getHfOperid().equals("")){
						report.setHfDate(CommonUtil.getNowTime());
						report.setHfOperid(userInfo.getUserName());
					}else{
						report.setHfOperid(bd.getName("Loginuser", "username", "userid", report.getHfOperid()));
					}

				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
	
				//获取已经上传的附件
				LostElevatorReportAction ler=new LostElevatorReportAction();
				List filelst=ler.FileinfoList(hs, id.trim(), "LostElevatorReport");
				request.setAttribute("updatefileList", filelst);
				
				request.setAttribute("causeAnalysisList", bd.getPullDownList("LostElevatorReport_CauseAnalysis"));// 签署方式下拉框列表
				
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

			request.setAttribute("lostElevatorReportBean", report);
			request.setAttribute("display", "yes");
			forward = mapping.findForward("lostElevatorReportHfDisplay");
		}		
		
		saveToken(request); //生成令牌，防止表单重复提交
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * 审核方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toAuditRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, HibernateException {
		
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		
		if(isTokenValid(request, true)){
			
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
			DynaActionForm dform = (DynaActionForm) form;				
			String id = request.getParameter("id"); 		
			String fhRem = String.valueOf(dform.get("fhRem")); // 审核状态
			
			if(id != null && !id.equals("")){
				
				Session hs = null;
				Transaction tx = null;

				LostElevatorReport report = null;						
				try {
					
					hs = HibernateUtil.getSession();		
					tx = hs.beginTransaction();
					report=(LostElevatorReport) hs.get(LostElevatorReport.class, id);
					
					report.setLostElevatorDate((String)dform.get("lostElevatorDate"));//lostElevatorDate 丢梯日期
					report.setCauseAnalysis((String)dform.get("causeAnalysis"));//causeAnalysis 原因分析
					report.setContacts((String)dform.get("contacts"));//contacts 使用单位联系人
					report.setContactPhone((String)dform.get("contactPhone"));//contactPhone 联系电话
					report.setDetailedRem((String)dform.get("detailedRem"));//detailedRem 备注详细原因
					report.setCompeteCompany((String)dform.get("competeCompany"));//competeCompany 竞争单位名称
					report.setRecoveryPlan((String)dform.get("recoveryPlan"));//recoveryPlan 恢复计划
					
					report.setHfDate(CommonUtil.getNowTime());
					report.setHfOperid(userInfo.getUserID());
					report.setFhRem(fhRem);//回访结果
					
					hs.save(report);
					
					tx.commit();
					
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","保存成功！"));
					
				} catch (Exception e) {				
					e.printStackTrace();
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","保存失败！"));
				} finally {
					if(hs != null){
						hs.close();				
					}				
				}
				
			} else {
				if(errors.isEmpty()){
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("maintContract.recordnotfounterror"));
				}
			}	
			
		
		}else{
			saveToken(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("navigator.submit.error"));
		}
		
		if (!errors.isEmpty()){
			this.saveErrors(request, errors);
		}

		return mapping.findForward("returnList");

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
			
			String id = (String) dform.get("id");
			LostElevatorReport master = (LostElevatorReport) hs.get(LostElevatorReport.class, id);
			if (master != null) {
				hs.delete(master);
				//删除数据库附件信息
				String sqldel="delete from ContractFileinfo where MaterSid='"+id+"' and BusTable='LostElevatorReport'";
				hs.connection().prepareStatement(sqldel).execute();
				
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.succeed"));
			}
			tx.commit();
		} catch (Exception e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("delete.foreignkeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Update error!");
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
