package com.gzunicorn.struts.action.infomanager.servicingcontractmaster;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.ResultSetDynaClass;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractdetail.ServicingContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.servicingcontractmaster.ServicingContractMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.gzunicorn.struts.action.engcontractmanager.servicingcontract.ServicingContractAction;
import com.gzunicorn.struts.action.xjsgg.XjsggAction;

public class ServicingContractMasterTaskAction extends DispatchAction {
	
	Log log = LogFactory.getLog(ServicingContractAction.class);
	XjsggAction xj=new XjsggAction();
	BaseDataImpl bd = new BaseDataImpl();
	DataOperation bo=new DataOperation();
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
		SysRightsUtil.filterModuleRight(request, response,
				SysRightsUtil.NODE_ID_FORWARD + "ServicingContractMasterTask", null);
		/** **********结束用户权限过滤*********** */

		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		if (name == null || name.equals("")) {
			name = "toSearchRecord";
			return dispatchMethod(mapping, form, request, response, name);
		} else {
			if( "arrival".equals(name)){
				name="toAddRecord";
				return dispatchMethod(mapping, form, request, response, name);
			}
			if( "complete".equals(name)){
				name="toAddRecord";
				return dispatchMethod(mapping, form, request, response, name);
			}
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
		HttpSession session=request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform=(DynaActionForm)form;
		ActionForward  Forward=null;
		Session hs=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		String maintDivision2="";
		List list=new ArrayList();
		String typejsp=request.getParameter("typejsp");
		if(typejsp== null){
			typejsp="search";
		}
		List maintDivisionList=Grcnamelist1.getgrcnamelist(userInfo.getUserID());
		if(maintDivisionList!=null && maintDivisionList.size()>0){
			Map map = (Map)maintDivisionList.get(0);
			maintDivision2 = (String)map.get("grcid");
		}
		String busType=request.getParameter("busType");//业务类别
		String maintContractNo=request.getParameter("maintContractNo");//合同号
		String maintDivision=request.getParameter("maintDivision");//所属维保分部
		String finishFlag=request.getParameter("FinishFlag22");//完工标志
		String ForecastDatestatr=request.getParameter("ForecastDatestatr");//预计到货日期开始
		String ForecastDateend=request.getParameter("ForecastDateend");//预计到货日期结束
		try {
			hs=HibernateUtil.getSession();
			String sql="exec ContractMasterTask ";
			if(maintDivision==null || "".equals(maintDivision)){		
			sql+="'"+maintDivision2.trim()+"'";
			}else{
			sql+="'%"+maintDivision.trim()+"%'";
			}		
			sql+=",'"+typejsp.trim()+"'";
			if(busType==null || "".equals(busType)){		
			sql+=",'%'";
			}else{
			sql+=",'"+busType.trim()+"'";
			request.setAttribute("busType", busType);
			}
			if(maintContractNo==null || "".equals(maintContractNo)){
			sql+=",'%'";
			}else{
			sql+=",'%"+maintContractNo.trim()+"%'";
			request.setAttribute("maintContractNo", maintContractNo);
			}
			if(finishFlag==null || "".equals(finishFlag)){
			sql+=",'N'";
			request.setAttribute("finishFlag", "N");
			}else{
			sql+=",'"+finishFlag.trim()+"'";
			request.setAttribute("finishFlag", finishFlag);
			}
			if(ForecastDatestatr==null || "".equals(ForecastDatestatr)){
			sql+=",'0000-00-00'";
			}else{
			sql+=",'"+ForecastDatestatr.trim()+"'";
			request.setAttribute("ForecastDatestatr", ForecastDatestatr);
			}
			if(ForecastDateend==null || "".equals(ForecastDateend)){
			sql+=",'9999-99-99'";
			}else{
			sql+=",'"+ForecastDateend.trim()+"'";
			request.setAttribute("ForecastDateend", ForecastDateend);
			}
			//System.out.println(sql);
			if(typejsp!=null && !"search".equals(typejsp)){	
				if( "arrival".equals(typejsp)){
					request.setAttribute("navigator.location", "预计/确认到货日期  >> 录入信息");
				}else {
					request.setAttribute("navigator.location", "项目完工 >> 录入信息");
				}
				bo.setCon(hs.connection());
				list=bo.queryToList(sql);
				if(list!=null &&list.size()>0){
					for(int i=0;i<list.size();i++){
					HashMap hm=new HashMap();
					hm=(HashMap)list.get(i);
					hm.put("bustype", hm.get("bustype").equals("W")?"维修":"改造");
					hm.put("taskuserid",bd.getName("Loginuser", "username", "userid", hm.get("taskuserid").toString()));
					hm.put("companyid",bd.getName("Customer", "companyName", "companyId", hm.get("companyid").toString()));
					hm.put("maintdivision", bd.getName("Company", "comname", "comid", hm.get("maintdivision").toString()));
					if("complete".equals(typejsp)){
					hm.put("epibolyFlag", "");	
					}
					}
				}
				request.setAttribute("ServicingContractMasterList", list);
			}else{	
				request.setAttribute("navigator.location", "维改任务下达管理  >> 任务列表");
				ps=hs.connection().prepareStatement(sql);			
				rs=ps.executeQuery();
				while(rs.next()){
					request.setAttribute("Arrival", rs.getString("num"));
					request.setAttribute("Complete", rs.getString("numtwo"));
				}	
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}try {
			hs.close();
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			DebugUtil
					.print("DataOperation.queryToList() " + e.getMessage());
		}
		request.setAttribute("typejsp", typejsp);
		request.setAttribute("maintDivisionList",maintDivisionList);
		Forward=mapping.findForward("toArrivalComplete");
		return Forward;
	}
	
	/**
	 * Method toUpdateRecord execute,Update data to database and return list
	 * page or modifiy page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward toAddRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException {
		
		ActionForward forward = null;
		DynaActionForm dform = (DynaActionForm)form;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ActionErrors errors = new ActionErrors();
		String name = request.getParameter("method");
		String[] isboxs=request.getParameterValues("isbox");//页面勾选需要保存的数据
		String[] wgbillno=request.getParameterValues("wgbillno");//流水号
		String[] forecastdate=request.getParameterValues("forecastdate");//预计到货日期
		String[] comfirmdate=request.getParameterValues("comfirmdate");//确认到货日期
		String[] ItemUserId=request.getParameterValues("itemUserId");//项目负责人
		String[] AppWorkDate=request.getParameterValues("appWorkDate");//派工日期	
		String[] EnterArenaDate=request.getParameterValues("enterArenaDate");//进场日期
		String[] EpibolyFlag=request.getParameterValues("epibolyFlag");//外包标志
		String[] FinishFlag=request.getParameterValues("finishFlag");//完工标志
		String[] FinishDate=request.getParameterValues("finishDate");//完工日期
		String[] finishRem=request.getParameterValues("finishRem");//完工备注
		String finishId=userInfo.getUserID();//完工确认人
		
		Session hs = null;
		Transaction tx = null;
		ServicingContractMaster scm=null;
		if(isboxs!=null && isboxs.length>0){
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();
              if(name!=null && "arrival".equals(name)){
				for(int i=0;i<isboxs.length;i++){
					if(isboxs[i]!=null && isboxs[i].equals("Y")){
						scm=(ServicingContractMaster)hs.get(ServicingContractMaster.class, wgbillno[i]);
						scm.setForecastDate(forecastdate[i]);
						scm.setComfirmDate(comfirmdate[i]);
						hs.update(scm);
					}
				}
				}else if(name!=null && "complete".equals(name)){
					for(int i=0;i<isboxs.length;i++){
						if(isboxs[i]!=null && isboxs[i].equals("Y")){
							scm=(ServicingContractMaster)hs.get(ServicingContractMaster.class, wgbillno[i]);
							scm.setItemUserId(ItemUserId[i]);
							scm.setAppWorkDate(AppWorkDate[i]);
							scm.setEnterArenaDate(EnterArenaDate[i]);
							scm.setEpibolyFlag(EpibolyFlag[i]);
							scm.setFinishFlag(FinishFlag[i]);
							scm.setFinishDate(FinishDate[i]);
							scm.setFinishRem(finishRem[i]);
							scm.setFinishId(finishId);
							hs.update(scm);
						}
					}
				}
				tx.commit();
			} catch (Exception e2) {
				try {
					tx.rollback();
				} catch (HibernateException e3) {
					log.error(e3.getMessage());
				}
				e2.printStackTrace();
				log.error(e2.getMessage());
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.fail"));
			} finally {
				try {
					if(hs!=null){
						hs.close();
					}
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "Hibernate close error!");
				}
			}
		}

		
		String isreturn = request.getParameter("isreturn");
		if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
			forward = mapping.findForward("returnList");
		} else {
			if (errors.isEmpty()) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("insert.success"));
			} else {
				request.setAttribute("error", "Yes");
			}
			if(name!=null && "arrival".equals(name)){
			forward = mapping.findForward("returnArrivalList");
			}else{
			forward = mapping.findForward("returnCompleteList");	
			}
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
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
		
		request.setAttribute("navigator.location","维改任务下达 >> 查看");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;			
		String id = request.getParameter("id");
		Session hs = null;
		ServicingContractMaster scm=null;
		Customer ctj=null;
		Customer cty=null;
		List ServicingContractDetailList = new ArrayList();
	   if (id != null) {				
		try {
			hs = HibernateUtil.getSession();
			scm=(ServicingContractMaster)hs.get(ServicingContractMaster.class, id);
			scm.setAuditStatus(scm.getAuditStatus().equals("Y")?"已审核":"未审核");
			scm.setTaskSubFlag(scm.getTaskSubFlag().equals("Y")?"已下达":"未下达");
			scm.setAttn(bd.getName("Loginuser", "username", "userid", scm.getAttn()));
			scm.setMaintDivision(bd.getName("Company", "comname", "comid", scm.getMaintDivision()));
			request.setAttribute("maintStationName", bd.getName("Company", "comname", "comid", scm.getMaintDivision()));
			scm.setAuditOperid(bd.getName("Loginuser", "username", "userid", scm.getAuditOperid()));
			scm.setTaskUserId(bd.getName("Loginuser", "username", "userid", scm.getTaskUserId()));
			//甲方单位
			String companyId=scm.getCompanyId();
			if(companyId!=null && !"".equals(companyId)){
				ctj=(Customer)hs.get(Customer.class, companyId);
			}
			//乙方单位
			String companyId2=scm.getCompanyId2();
			if(companyId2!=null && !"".equals(companyId2)){
				cty=(Customer)hs.get(Customer.class, companyId2);
			}
			
			List list=hs.createQuery("select a from ServicingContractDetail a where a.servicingContractMaster.wgBillno='"+id+"'").list();	
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					ServicingContractDetail scd=(ServicingContractDetail)list.get(i);
					ServicingContractDetailList.add(scd);
				}
			}
			
		} catch (DataStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		
	}else{
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
	}
	request.setAttribute("ServicingContractMasterList", scm);
	request.setAttribute("CustomerList", ctj);
	request.setAttribute("CustomerList2", cty);
	request.setAttribute("ServicingContractDetailList", ServicingContractDetailList);
	request.setAttribute("typejsp", "display");
	if (!errors.isEmpty()) {
		saveErrors(request, errors);
	}
		forward = mapping.findForward("toArrivalComplete");
		return forward;
	}
}
