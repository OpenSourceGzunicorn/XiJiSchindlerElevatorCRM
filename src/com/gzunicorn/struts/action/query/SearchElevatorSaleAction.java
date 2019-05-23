package com.gzunicorn.struts.action.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.elevatorsales.ElevatorSalesInfo;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;

import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class SearchElevatorSaleAction extends DispatchAction {

	Log log = LogFactory.getLog(SearchElevatorSaleAction.class);
	
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

		/** **********开始用户权限过滤*********** */
		//SysRightsUtil.filterModuleRight(request, response, SysRightsUtil.NODE_ID_FORWARD + "", null);
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
		
		request.setAttribute("navigator.location","查找 >> 电梯销售信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		HTMLTableCache cache = new HTMLTableCache(session, "searchElevatorSaleList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchElevatorSale");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("salesContractNo");
		table.setIsAscending(true);
		cache.updateTable(table);

		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		}else if (action.equals("Submit")) {
			cache.loadForm(tableForm);
		} else { 
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String salesContractNo = tableForm.getProperty("salesContractNo"); //销售合同号

		Session hs = null; 
		Query query = null;
		try {

			hs = HibernateUtil.getSession();
			
			String sql = "select distinct salesContractNo,salesContractName,deliveryAddress from ElevatorSalesInfo e where enabledFlag='Y'";
			
			if (salesContractNo != null && !salesContractNo.equals("")) {
				sql += " and salesContractNo like '%"+salesContractNo.trim()+"%'";
			}

			if (table.getIsAscending()) {
				sql += " order by "+ table.getSortColumn() +" asc";
			} else {
				sql += " order by "+ table.getSortColumn() +" desc";
			}
			
			query = hs.createSQLQuery(sql);
			table.setVolume(query.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);
			List list = query.list();
			List elevatorSaleList = new ArrayList();
			for (Object object : list) {
				Object[] values = (Object[])object;
				Map map = new HashMap();
				map.put("salesContractNo", values[0]); //销售合同号
				map.put("salesContractName", values[1]); //销售合同名称
				map.put("deliveryAddress", values[2]); //发货地址
				elevatorSaleList.add(map);
			}

			table.addAll(elevatorSaleList);
			session.setAttribute("searchElevatorSaleList", table);
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
		forward = mapping.findForward("searchElevatorSaleList");
		
		return forward;
	}
	/**
	 * 维改报价/维保报价/维保合同使用
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord2(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("navigator.location","查找 >> 电梯主要信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		HTMLTableCache cache = new HTMLTableCache(session, "searchElevatorList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchElevator");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("elevatorNo");
		table.setIsAscending(true);
		cache.updateTable(table);
		
		if (action.equals(ServeTableForm.NAVIGATE)
				|| action.equals(ServeTableForm.SORT)) {
			cache.loadForm(tableForm);
		}else if (action.equals("Submit")) {
			cache.loadForm(tableForm);
		} else { 
			table.setFrom(0);
		}
		cache.saveForm(tableForm);

		String elevatorNo = tableForm.getProperty("elevatorNo"); //电梯编号
		String elevatorType = tableForm.getProperty("elevatorType"); //电梯类型		
		
		String elevatorNos = request.getParameter("elevatorNos"); //已选择的电梯编号集合	
		String isOutsideFootball = tableForm.getProperty("isOutsideFootball");//是否外揽
		String salesContractNo = tableForm.getProperty("contractNo");//合同编号
		String salesContractName = tableForm.getProperty("salesContractName");//合同名称
		String state=request.getParameter("state");
		
		if(elevatorNos == null || elevatorNos.trim().equals("")){
			elevatorNos=tableForm.getProperty("elevatorNos");
		}else{
			tableForm.setProperty("elevatorNos",elevatorNos);
		}
		if(state == null || state.trim().equals("")){
			state=tableForm.getProperty("state");
		}else{
			tableForm.setProperty("state", state);
		}
		
		Session hs = null; 
		Query query = null;
		try {

			hs = HibernateUtil.getSession();
			String sqlStr="";
             if(state!=null&&!state.trim().equals("")){
               sqlStr="";
	            if(state=="WGBJ"||state.equals("WGBJ")){
	            	/*维改报价*/
            	   sqlStr+=" and ElevatorNo not in (select sd.ElevatorNo from ServicingContractQuoteMaster s,ServicingContractQuoteDetail sd" +
            	   		" where s.BillNo=sd.BillNo and (s.Status='-1' or s.Status='100') )";
	            }else if(state=="WBBJ"||state.equals("WBBJ")){
	            	/*维保报价 ====排除虚拟的电梯编号  and md.ElevatorNo<>'0000000000'*/
	            	sqlStr+=" and ElevatorNo not in (select md.ElevatorNo from MaintContractQuoteMaster m,MaintContractQuoteDetail md " +
	            			"where (m.Status='-1' or m.Status='100') and m.billNo=md.billNo and md.ElevatorNo<>'0000000000')";
	            	//去掉在保的电梯
	            	sqlStr+=" and ElevatorNo not in (select b.elevatorNo from MaintContractMaster a,MaintContractDetail b " +
	            			"where a.billNo=b.billNo and a.contractStatus in('ZB','XB'))";
	            }else if(state=="WB"||state.equals("WB")){
	            	/*维保*/
	            	sqlStr+=" and ElevatorNo not in (select ElevatorNo from MaintContractDetail union select md.ElevatorNo" +
	            			" from MaintContractQuoteMaster m,MaintContractQuoteDetail md " +
	            			"where (m.Status='-1' or m.Status='100') and m.billNo=md.billNo and md.ElevatorNo<>'0000000000')"; 
	            	//去掉已建合同的电梯
	            	sqlStr+=" and ElevatorNo not in (select b.elevatorNo from MaintContractMaster a,MaintContractDetail b " +
	            			"where a.billNo=b.billNo)";
	            }
             }
			String sql = "select * from ElevatorSalesInfo a " +
					" where enabledFlag='Y'" +sqlStr;
			
			if (elevatorNos != null && !elevatorNos.equals("")) {
				sql += " and elevatorNo not in ("+elevatorNos.replace("|", "'")+")";
			}			
			if (elevatorNo != null && !elevatorNo.equals("")) {
				sql += " and elevatorNo like '%"+elevatorNo.trim()+"%'";
			}
			if (elevatorType != null && !elevatorType.equals("")) {
				sql += " and elevatorType = '"+elevatorType.trim()+"'";
			}
			if (salesContractNo != null && !salesContractNo.equals("")) {
				sql += " and salesContractNo like '%"+salesContractNo.trim()+"%'";
			}
			if (salesContractName != null && !salesContractName.equals("")) {
				sql += " and salesContractName like '%"+salesContractName.trim()+"%'";
			}
			
			if (isOutsideFootball != null && !isOutsideFootball.equals("")) {
				
				if(isOutsideFootball.trim()=="Y"||"Y".equals(isOutsideFootball.trim()))
				{
					sql += " and a.isOutsideFootball = '"+isOutsideFootball.trim()+"'";
				}else {
					sql += " and (a.isOutsideFootball is null or a.isOutsideFootball = 'N')";
				}
			}
					
			if (table.getIsAscending()) {
				sql += " order by "+ table.getSortColumn() +" asc";
			} else {
				sql += " order by "+ table.getSortColumn() +" desc";
			}

			//System.out.println(">>>>"+sql);

			query = hs.createSQLQuery(sql).addEntity("a",ElevatorSalesInfo.class);
			

			table.setVolume(query.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List searchElevatorList = query.list();
			if(searchElevatorList!=null && searchElevatorList.size()>0){
				for(int i=0;i<searchElevatorList.size();i++){
					ElevatorSalesInfo eleinfo=(ElevatorSalesInfo)searchElevatorList.get(i);
					String eleno=eleinfo.getElevatorNo();
					HashMap remap=this.getElevatorSignWay(eleno);
					eleinfo.setR1((String)remap.get("signWay"));
					eleinfo.setR2((String)remap.get("signWayName"));
				}
			}
			

			table.addAll(searchElevatorList);
			session.setAttribute("searchElevatorList", table);
			request.setAttribute("elevatorTypeList", bd.getPullDownList("ElevatorSalesInfo_type"));
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
		forward = mapping.findForward("searchElevatorList");
		
		return forward;
	}
	
	
	@SuppressWarnings("unchecked")
	public ActionForward toSearchRecord3(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("navigator.location","查找 >> 电梯主要信息");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();
		
		HTMLTableCache cache = new HTMLTableCache(session, "searchElevatorList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fSearchElevator2");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		table.setSortColumn("elevatorNo");
		table.setIsAscending(true);
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

		String elevatorNo = tableForm.getProperty("elevatorNo"); //电梯编号
		String elevatorType = tableForm.getProperty("elevatorType"); //电梯类型		
		
		String elevatorNos = request.getParameter("elevatorNos"); //已选择的电梯编号集合	
		String isOutsideFootball = tableForm.getProperty("isOutsideFootball");//是否外揽
		String state=request.getParameter("state");
		if(elevatorNos == null || elevatorNos.trim().equals("")){
			elevatorNos=tableForm.getProperty("elevatorNos");
		}else{
			tableForm.setProperty("elevatorNos",elevatorNos);
		}
		if(state == null || state.trim().equals("")){
			state=tableForm.getProperty("state");
		}else{
			tableForm.setProperty("state", state);
		}
		
		Session hs = null; 
		Query query = null;
		try {

			hs = HibernateUtil.getSession();
			String sqlStr="";
             if(state!=null&&!state.trim().equals("")){
               sqlStr=" and ElevatorNo not in (";
	            if(state=="WGBJ"||state.equals("WGBJ")){
	            	   sqlStr+="select sd.ElevatorNo from ServicingContractQuoteMaster s,ServicingContractQuoteDetail sd where s.BillNo=sd.BillNo and (s.Status='-1' or s.Status='100')";/*维改报价*/
	            }else if(state=="WBBJ"||state.equals("WBBJ")){
	            	sqlStr+="select md.ElevatorNo from MaintContractQuoteMaster m,MaintContractQuoteDetail md where (m.Status='-1' or m.Status='100') and m.billNo=md.billNo";/*维保报价*/
	            }else if(state=="WB"||state.equals("WB")){
	            	sqlStr+="select ElevatorNo from MaintContractDetail union select md.ElevatorNo from MaintContractQuoteMaster m,MaintContractQuoteDetail md where (m.Status='-1' or m.Status='100') and m.billNo=md.billNo"; /*维保*/
	            }
	            sqlStr+=")";
             }
			String sql = "select * from ElevatorSalesInfo a " +
					" where enabledFlag='Y'" +sqlStr;
			
			if (elevatorNos != null && !elevatorNos.equals("")) {
				sql += " and elevatorNo not in ("+elevatorNos.replace("|", "'")+")";
			}			
			if (elevatorNo != null && !elevatorNo.equals("")) {
				sql += " and elevatorNo like '%"+elevatorNo.trim()+"%'";
			}
			if (elevatorType != null && !elevatorType.equals("")) {
				sql += " and elevatorType = '"+elevatorType.trim()+"'";
			}
			
			if (isOutsideFootball != null && !isOutsideFootball.equals("")) {
				
				if(isOutsideFootball.trim()=="Y"||"Y".equals(isOutsideFootball.trim()))
				{
					sql += " and a.isOutsideFootball = '"+isOutsideFootball.trim()+"'";
				}else {
					sql += " and (a.isOutsideFootball is null or a.isOutsideFootball = 'N')";
				}
			}
			
					
			if (table.getIsAscending()) {
				sql += " order by "+ table.getSortColumn() +" asc";
			} else {
				sql += " order by "+ table.getSortColumn() +" desc";
			}
			/*//System.out.println(sql);
			//System.out.println("000"+isOutsideFootball+"000");*/
			query = hs.createSQLQuery(sql).addEntity("a",ElevatorSalesInfo.class);
			

			table.setVolume(query.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List searchElevatorList = query.list();

			table.addAll(searchElevatorList);
			session.setAttribute("searchElevatorList", table);
			request.setAttribute("elevatorTypeList", bd.getPullDownList("ElevatorSalesInfo_type"));
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
		forward = mapping.findForward("searchElevatorList");
		
		return forward;
	}
	/**
	 * 根据电梯编号，获取签署方式
	 * @param hs
	 * @param ElevatorNo
	 * @return
	 */
	public HashMap getElevatorSignWay(String ElevatorNo){
		HashMap reMap = new HashMap();
		
		/**
		 * 电梯签署方式	  签署过程		系统自动判断方法
			新免保	【无-免保】		【系统检测新增免保合同所包含的电梯】
			新收费	【无-收费】		【报价申请中所包含的之前没有维保过的电梯】
			外揽		【无-收费】		【报价申请中所包含的之前没有维保过的电梯（从外揽电梯基础信息中选出了的电梯）】
			恢复		【退保-收费】	【报价申请中所包含的之前退保过的电梯】
			续签		【(新收费, 续签, 恢复, 外揽, 转换)-收费】	【直接点击“续签”带过去的电梯，和点击“合同变更报价申请”从收费合同中带过来的电梯】
			转换		【新免保-收费】	【“合同变更报价申请”中，从免保合同带过来的电梯】
			
			//20170613修改的
			上一个签署方式						签署方式
			【无-自产收费】						恢复
			【无-外揽收费】						外揽新签
			【无-代理商委托质保】					新免保
			【无-自产免保】						新免保
			【新免保，自产免保-自产收费】				转换
			【恢复，转换，自产续签，自产收费-自产收费】	自产续签
			【新免保，代理商委托做质保-自产收费	】		转换
			【外揽新签，外揽续签，外揽收费-外揽收费】		外揽续签
			【退保-自产收费】						恢复 HF
			【退保-外揽收费】						外揽新签
			
						HF==恢复	
						TB==退保	
						WL==外揽新签	
						WLXB==外揽续签
						XB==自产续签	
						XQ==新免保	
						XS==新收费	

		 */
		Session hs=null;
		try{
			hs = HibernateUtil.getSession();
			
			int rowid=0;
			//取维保结束日期最大的上一份合同
			List list=hs.createQuery("select rowid from MaintContractDetail where ElevatorNo='"+ElevatorNo+"' order by MainEDate desc").list();
			if(list!=null && list.size()>0){
				rowid=Integer.valueOf(list.get(0).toString());
			}
			
			String sqlmcd="select isnull(e.IsOutsideFootball,'') as IsOutsideFootball,"
					+ "isnull(a.IsSurrender,'') as IsSurrender,a.SignWay,m.ContractStatus "
					+ "from MaintContractDetail a left join ElevatorSalesInfo e on a.ElevatorNo=e.ElevatorNo,"
					+ "MaintContractMaster m "
					+ "where a.BillNo=m.BillNo and a.elevatorNo='"+ElevatorNo+"' and a.rowid='"+rowid+"'";
			List detailList=hs.createSQLQuery(sqlmcd).list();
			if(detailList!=null && detailList.size()>0){
				Object[] obj=(Object[])detailList.get(0);
				String isOutsideFootball=(String)obj[0];//是否外揽
				String isSurrender=(String)obj[1];//是否退保
				String signWay=(String)obj[2];//上一次签署方式
				String ContractStatus=(String)obj[3];//合同是否退保TB
				
				if((isSurrender!=null && isSurrender.equals("Y")) 
						|| (signWay!=null && signWay.equals("TB"))
						|| (ContractStatus!=null && ContractStatus.equals("TB"))){
					if(isOutsideFootball!=null && isOutsideFootball.equals("Y")){
						//退保=>外揽新签
						reMap.put("signWay", "WL");
						reMap.put("signWayName", bd.getName("Pulldown", "pullname", "id.pullid", "WL"));
					}else{
						//退保=>恢复
						reMap.put("signWay", "HF");
						reMap.put("signWayName", bd.getName("Pulldown", "pullname", "id.pullid", "HF"));
					}
				}else if(signWay!=null && signWay.equals("XQ")){
					//新免保=>转换
					reMap.put("signWay", "ZH");
					reMap.put("signWayName", bd.getName("Pulldown", "pullname", "id.pullid", "ZH"));
				}else{
					if(signWay!=null && (signWay.equals("WL") || signWay.equals("WLXB"))){
						//外揽新签，外揽续签=>外揽续签
						reMap.put("signWay", "WLXB");
						reMap.put("signWayName", bd.getName("Pulldown", "pullname", "id.pullid", "WLXB"));
					}else{
						//恢复，转换，自产续签=>自产续签
						reMap.put("signWay", "XB");
						reMap.put("signWayName", bd.getName("Pulldown", "pullname", "id.typeflag='MaintContractDetail_SignWay' and a.id.pullid", "XB"));
					}
				}
			}else{
				List elevatorList=hs.createQuery("from ElevatorSalesInfo where elevatorNo='"+ElevatorNo+"'").list();
				if(elevatorList!=null && elevatorList.size()>0){
					ElevatorSalesInfo elevator=(ElevatorSalesInfo) elevatorList.get(0);
					if(elevator.getIsOutsideFootball()!=null && elevator.getIsOutsideFootball().equals("Y")){
						//无=>外揽新签
						reMap.put("signWay", "WL");
						reMap.put("signWayName", bd.getName("Pulldown", "pullname", "id.pullid", "WL"));
					}else{
						//无=>恢复
						reMap.put("signWay", "HF");
						reMap.put("signWayName", bd.getName("Pulldown", "pullname", "id.pullid", "HF"));
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		
		return reMap;
	}
	/**
	 * 根据电梯编号，获取签署方式
	 * @param hs
	 * @param ElevatorNo
	 * @return
	 */
	public HashMap getElevatorSignWay_old(String ElevatorNo){
		HashMap reMap = new HashMap();
		
		/**
		 * 电梯签署方式	  签署过程		系统自动判断方法
			新免保	【无-免保】		【系统检测新增免保合同所包含的电梯】
			新收费	【无-收费】		【报价申请中所包含的之前没有维保过的电梯】
			外揽		【无-收费】		【报价申请中所包含的之前没有维保过的电梯（从外揽电梯基础信息中选出了的电梯）】
			恢复		【退保-收费】	【报价申请中所包含的之前退保过的电梯】
			续签		【(新收费, 续签, 恢复, 外揽, 转换)-收费】	【直接点击“续签”带过去的电梯，和点击“合同变更报价申请”从收费合同中带过来的电梯】
			转换		【新免保-收费】	【“合同变更报价申请”中，从免保合同带过来的电梯】
		 */
		Session hs=null;
		try{
			hs = HibernateUtil.getSession();
			
			int rowid=0;
			//取维保结束日期最大的上一份合同
			List list=hs.createQuery("select rowid from MaintContractDetail where ElevatorNo='"+ElevatorNo+"' order by MainEDate desc").list();
			if(list!=null && list.size()>0){
				rowid=Integer.valueOf(list.get(0).toString());
			}
			
			List<MaintContractDetail> detailList=hs.createQuery("from MaintContractDetail where elevatorNo='"+ElevatorNo+"' and rowid='"+rowid+"'").list();
			if(detailList!=null && detailList.size()>0){
				MaintContractDetail detail=(MaintContractDetail)detailList.get(0);
				
				if(detail.getIsSurrender()!=null && detail.getIsSurrender().equals("Y")){
					//退保=>恢复
					reMap.put("signWay", "HF");
					reMap.put("signWayName", bd.getName("Pulldown", "pullname", "id.pullid", "HF"));
				}else if(detail.getSignWay()!=null && detail.getSignWay().equals("XQ")){
					//新免保=>转换
					reMap.put("signWay", "ZH");
					reMap.put("signWayName", bd.getName("Pulldown", "pullname", "id.pullid", "ZH"));
				}else{
					//续签
					reMap.put("signWay", "XB");
					reMap.put("signWayName", bd.getName("Pulldown", "pullname", "id.pullid", "XB"));
				}
			}else{
				List elevatorList=hs.createQuery("from ElevatorSalesInfo where elevatorNo='"+ElevatorNo+"'").list();
				if(elevatorList!=null && elevatorList.size()>0){
					ElevatorSalesInfo elevator=(ElevatorSalesInfo) elevatorList.get(0);
					if(elevator.getIsOutsideFootball()!=null && elevator.getIsOutsideFootball().equals("Y")){
						//外揽
						reMap.put("signWay", "WL");
						reMap.put("signWayName", bd.getName("Pulldown", "pullname", "id.pullid", "WL"));
					}else{
						//新收费
						reMap.put("signWay", "XS");
						reMap.put("signWayName", bd.getName("Pulldown", "pullname", "id.pullid", "XS"));
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}
		
		return reMap;
	}

}
