package com.gzunicorn.struts.action.engcontractmanager.maintcontract;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.commons.beanutils.BeanUtils;
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
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SqlBeanUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.infomanager.elevatorarchivesinfo.ElevatorArchivesInfo;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class MaintContractAuditAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintContractAuditAction.class);

	BaseDataImpl bd = new BaseDataImpl();

	/**
	 * 维保合同审核
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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "maintcontractaudit", null);
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
		
		request.setAttribute("navigator.location","维保合同审核 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "maintContractAuditList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fMaintContractAudit");
		table.setLength(SysConfig.HTML_TABLE_LENGTH);
		cache.updateTable(table);
		table.setSortColumn("a.billNo");
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
		
		String billNo = tableForm.getProperty("billNo");// 流水号
		String maintContractNo = tableForm.getProperty("maintContractNo");// 维保合同号
		String companyName = tableForm.getProperty("companyName");// 甲方单位
		String attn = tableForm.getProperty("attn");// 经办人
		String maintDivision = tableForm.getProperty("maintDivision");// 所属维保站			
		String contractStatus = tableForm.getProperty("contractStatus");// 合同状态			
//		String submitType = tableForm.getProperty("submitType");// 提交标志
		String auditStatus = tableForm.getProperty("auditStatus");// 审核状态
		String taskSubFlag = tableForm.getProperty("taskSubFlag");// 任务下达标志
		String salesContractNo = tableForm.getProperty("salesContractNo");// 销售合同号
		String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号
		String maintAddress = tableForm.getProperty("maintAddress");//使用单位名称

		if(auditStatus == null){
			auditStatus = "N";
			tableForm.setProperty("auditStatus", "N");
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
			//查询维保到期合同   根据合同结束日期提前3个月提醒
			String today=DateUtil.getNowTime("yyyy-MM-dd");
			String datestr=DateUtil.getDate(today, "MM", 3);
			tableForm.setProperty("hiddatestr",datestr);
			
			hs = HibernateUtil.getSession();
			
			String sql = "select a,b.username as attn,p.pullname as contractStatus," +
					" c.comname as maintDivision, d.companyName as companyName,s.storagename as storagename "+
					" from MaintContractMaster a,Loginuser b,Company c,Customer d,Pulldown p,Storageid s " + 
					" where a.attn = b.userid " +
					" and a.maintDivision = c.comid"+
					" and a.maintStation = s.storageid "+
					" and a.contractStatus = p.id.pullid "+
					" and a.submitType = 'Y' "+
					" and a.companyId = d.companyId"+
					" and p.id.typeflag = 'MaintContractMaster_ContractStatus'";
			
			if (billNo != null && !billNo.equals("")) {
				sql += " and a.billNo like '%"+billNo.trim()+"%'";
			}
			if (maintContractNo != null && !maintContractNo.equals("")) {
				sql += " and a.maintContractNo like '%"+maintContractNo.trim()+"%'";
			}
			if (companyName != null && !companyName.equals("")) {
				sql += " and a.companyName like '%"+companyName.trim()+"%'";
			}
			if (attn != null && !attn.equals("")) {
				sql += " and a.attn like '%"+attn.trim()+"%'";
			}
			if (maintDivision != null && !maintDivision.equals("")) {
				sql += " and a.maintDivision like '"+maintDivision.trim()+"'";
			}
			if (contractStatus != null && !contractStatus.equals("")) {
				sql += " and a.contractStatus like '"+contractStatus.trim()+"'";
			}
			if (auditStatus != null && !auditStatus.equals("")) {
				sql += " and a.auditStatus like '"+auditStatus.trim()+"'";
			}
			if (taskSubFlag != null && !taskSubFlag.equals("")) {
				sql += " and a.taskSubFlag like '"+taskSubFlag.trim()+"'";
			}
			if(salesContractNo!=null && !salesContractNo.equals("")){
				sql+=" and a.billNo in (select billNo from MaintContractDetail where salesContractNo like '%"+salesContractNo.trim()+"%')";
			}
			if(elevatorNo!=null && !elevatorNo.equals("")){
				sql+=" and a.billNo in (select billNo from MaintContractDetail where elevatorNo like'"+elevatorNo.trim()+"')";
			}
			if (maintAddress != null && !maintAddress.equals("")) {
				sql += " and a.billNo in(select distinct billNo from MaintContractDetail where maintAddress like '%"+maintAddress.trim()+"%')";
			}
			
			String order = " order by "+table.getSortColumn();
			
			if (table.getIsAscending()) {
				sql += order + " asc";
			} else {
				sql += order + " desc";
			}
			
			//System.out.println(">>>"+sql);
			
			query = hs.createQuery(sql);
			table.setVolume(query.list().size());// 查询得出数据记录数;

			// 得出上一页的最后一条记录数号;
			query.setFirstResult(table.getFrom()); // pagefirst
			query.setMaxResults(table.getLength());

			cache.check(table);

			List list = query.list();
			List maintContractAuditList = new ArrayList();
			for (Object object : list) {
				Object[] objs = (Object[])object;
				MaintContractMaster master2 = (MaintContractMaster) objs[0];
				
				Map master=new HashMap();
				master.put("billNo",master2.getBillNo());
				master.put("maintContractNo",master2.getMaintContractNo());
				master.put("contractSdate",master2.getContractSdate());
				master.put("contractEdate",master2.getContractEdate());
				master.put("warningStatus",master2.getWarningStatus());
				master.put("submitType",master2.getSubmitType());
				master.put("auditStatus",master2.getAuditStatus());
				master.put("taskSubFlag",master2.getTaskSubFlag());
				 
				master.put("attn", String.valueOf(objs[1]));
				master.put("contractStatus", String.valueOf(objs[2]));
				master.put("maintDivision", String.valueOf(objs[3]));
				master.put("companyId", String.valueOf(objs[4]));
				master.put("maintStation", String.valueOf(objs[5]));
				
				maintContractAuditList.add(master);
			}

			table.addAll(maintContractAuditList);
			session.setAttribute("maintContractAuditList", table);
			// 合同性质下拉框列表
			request.setAttribute("contractNatureOfList", bd.getPullDownList("MaintContractMaster_ContractNatureOf"));
			// 合同状态下拉框列表
			request.setAttribute("contractStatusList", bd.getPullDownList("MaintContractMaster_ContractStatus"));
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
		forward = mapping.findForward("maintContractAuditList");
		
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
		
		request.setAttribute("navigator.location","维保合同审核 >> 查看");
		ActionErrors errors = new ActionErrors();
		ActionForward forward = null;
		//DynaActionForm dform = (DynaActionForm) form;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		String id = request.getParameter("id");
		
		Session hs = null;
		Transaction tx = null;
		List maintContractDetailList = null;

		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();//lijun add 20160430
				
				Query query = hs.createQuery("from MaintContractMaster where billNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {

					List contractNatureOfList = bd.getPullDownList("MaintContractMaster_ContractNatureOf");// 合同性质下拉列表
					List signWayList = bd.getPullDownList("MaintContractDetail_SignWay");// 签署方式下拉列表
					List elevatorTypeList = bd.getPullDownList("ElevatorSalesInfo_type");// 电梯类型下拉列表
					List elevatorNatureList = bd.getPullDownList("MaintContractDetail_ElevatorNature ");// 电梯性质下拉列表
					
					// 主信息
					MaintContractMaster master = (MaintContractMaster) list.get(0);					
					master.setAttn(bd.getName(hs, "Loginuser","username", "userid",master.getAttn()));// 经办人
					master.setContractNatureOf(bd.getOptionName(master.getContractNatureOf(), contractNatureOfList));// 合同性质
					request.setAttribute("maintDivisionName", bd.getName(hs, "Company", "comname", "comid",master.getMaintDivision()));
					request.setAttribute("maintStationName", bd.getName(hs, "Storageid", "storagename", "storageid", master.getMaintStation()));

					master.setOperId(userInfo.getUserName());
					master.setTaskUserId(bd.getName(hs, "Loginuser","username", "userid",master.getTaskUserId()));//任务下达人
					
					String auditStatus=master.getAuditStatus();
					if(auditStatus!=null && auditStatus.trim().equals("Y")){
						master.setAuditOperid(bd.getName(hs, "Loginuser", "username", "userid",master.getAuditOperid()));//审核人
					}else{
						//审核信息
						master.setAuditOperid(userInfo.getUserName());
						master.setAuditDate(CommonUtil.getNowTime());
					}
					
					//付款方式
					String pmastr=master.getPaymentMethod();
					List pdlist=bd.getPullDownAllList("MaintContractQuoteMaster_PaymentMethodApply");
					String pmaname=bd.getOptionName(pmastr, pdlist);
					master.setR4(pmaname);
					//maintContractBean.setPaymentMethod(pmastr);//付款方式
					//合同附件内容申请
					String ccastrname="";
					String ccastr=master.getContractTerms();
					if(ccastr!=null && !ccastr.trim().equals("")){
						List ccalist=bd.getPullDownAllList("MaintContractQuoteMaster_ContractContentApply");
						String[] ccarr=ccastr.split(",");
						for(int i=0;i<ccarr.length;i++){
							if(i==ccarr.length-1){
								ccastrname+=bd.getOptionName(ccarr[i].trim(), ccalist);
							}else{
								ccastrname+=bd.getOptionName(ccarr[i].trim(), ccalist)+"，";
							}
						}
					}
					master.setR5(ccastrname);
					//master.setContractTerms(ccastr);//附加合同条款
					
					request.setAttribute("maintContractBean", master);					
					
					// 甲方单位信息
					Customer companyA = (Customer) hs.get(Customer.class,master.getCompanyId());
					request.setAttribute("companyA",companyA);
					
					// 乙方方单位信息
					Customer companyB = (Customer) hs.get(Customer.class,master.getCompanyId2());
					request.setAttribute("companyB",companyB);
					
					// 明细列表
					query = hs.createQuery("from MaintContractDetail where billNo = '"+id+"'");
					maintContractDetailList = query.list();
					for (Object object : maintContractDetailList) {
						MaintContractDetail detail = (MaintContractDetail)object;
						detail.setSignWay(bd.getOptionName(detail.getSignWay(), signWayList));
						detail.setElevatorType(bd.getOptionName(detail.getElevatorType(), elevatorTypeList));
						detail.setElevatorNature(bd.getOptionName(detail.getElevatorNature(), elevatorNatureList));
					}					
					request.setAttribute("maintContractDetailList", maintContractDetailList);
					// 维保站下拉列表
					request.setAttribute("maintStationList", bd.getMaintStationList(userInfo.getComID()));
										
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(tx!=null){
					tx.rollback();//lijun add 20160430
				}
				try {
					hs.close();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}

			request.setAttribute("display", "yes");
			request.setAttribute("auditdisplay", "yes");
			forward = mapping.findForward("maintContractAuditDisplay");
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
			String auditStatus = String.valueOf(dform.get("auditStatus")); // 审核状态
			String submitType = "N".equals(auditStatus) ? "R" : "Y"; // 是否驳回
			String auditRem = String.valueOf(dform.get("auditRem")); // 审核意见
			//String[] rowids = request.getParameterValues("rowid"); // 合同电梯明细行号
			if(id != null && !id.equals("")){
				
				Session hs = null;
				Transaction tx = null;
				Query query = null;
				String historyBillNo="";
				String xqtype="";
				MaintContractMaster master = null;						
				try {
					
					hs = HibernateUtil.getSession();		
					tx = hs.beginTransaction();
	
					master = (MaintContractMaster) hs.get(MaintContractMaster.class, id);
					master.setSubmitType(submitType); // 提交标志
					master.setWorkisdisplay(null);
					master.setWorkisdisplay2(null);
					
					if("Y".equals(submitType)){
						String auditdate=CommonUtil.getNowTime();
						master.setAuditStatus(auditStatus); // 审核状态
						master.setAuditOperid(userInfo.getUserID()); // 审核人
						master.setAuditDate(auditdate); // 审核时间
						master.setAuditRem(auditRem); // 审核意见
						
						/**==========有服务费的合同增加业务费序号 开始==========*/
						//其它费用大于0
						if(master.getOtherFee()!=null && master.getOtherFee()>0){

							//业务费序号以 年份+序号 来排序（2017-0001）
							String year = CommonUtil.getNowTime("yyyy");
							String prefix = year+"-";
							String suffix="";
							String r1str = CommonUtil.genNo("MaintContractMaster", "r1", prefix, suffix, 4,"r1");
							master.setR1(r1str);
							
							//业务费序号排序
							int r9=1;
							String r9str="select a from MaintContractMaster a where isnull(a.otherFee,0)>0 order by a.r9 desc";
							List r9list=hs.createQuery(r9str).list();
							if(r9list!=null && r9list.size()>0){
								MaintContractMaster mcm=(MaintContractMaster)r9list.get(0);
								if(mcm.getR9()!=null && mcm.getR9()>0){
									r9=mcm.getR9()+1;
								}
							}
							master.setR9(r9);
							
						}
						/**==========有服务费的合同增加业务费序号 结束==========*/
						
						String realityEdate=CommonUtil.getToday();
						
						historyBillNo=master.getHistoryBillNo();
						xqtype=master.getXqType();
						if(xqtype==null || xqtype.equals("")){
							xqtype="ALL";
						}
						if(historyBillNo!=null && !"".equals(historyBillNo) && "ALL".equals(xqtype)){
							//全部续签
							historyBillNo=historyBillNo.replaceAll(",", "','");
							
							//修改上一份维保合同的合同状态
							String sqlm="update MaintContractMaster set contractStatus='LS' where billno in('"+historyBillNo+"')";
							hs.connection().prepareStatement(sqlm).executeUpdate();
							
							//退保
							String tbsql="update MaintContractDetail " +
									"set RealityEdate=(case when '"+realityEdate+"'>MainEDate then '"+realityEdate+"' else MainEDate end)," +
									"SurrenderDate='"+auditdate+"',SurrenderUser='"+userInfo.getUserID()+"',IsSurrender='Y'" +
									" where billNo in ('"+historyBillNo+"') and isnull(isSurrender,'N')='N' " +
									" and elevatorNo not in(select elevatorNo from MaintContractDetail where billNo='"+id+"')";
							hs.connection().prepareStatement(tbsql).executeUpdate();
							
						}else if(historyBillNo!=null && !"".equals(historyBillNo) && "PART".equals(xqtype)){
							//部分续签
							String upsql="update MaintContractDetail set ElevatorStatus='LS'"
									+ " where billNo = '"+historyBillNo+"' "
									+ "and ElevatorNo in(select ElevatorNo from MaintContractDetail where billNo = '"+id+"')";
							hs.connection().prepareStatement(upsql).executeUpdate();
							
							//检查上一份合同的电梯编号是否全部为历史
							upsql="select billNo from MaintContractDetail where billNo = '"+historyBillNo+"' and ISNULL(ElevatorStatus,'')=''";
							List hislis=hs.createSQLQuery(upsql).list();
							if(hislis!=null && hislis.size()>0){
								
							}else{
								//修改上一份维保合同的合同状态设置为历史
								upsql="update MaintContractMaster set ContractStatus='LS' where BillNo='"+historyBillNo+"'";
								hs.connection().prepareStatement(upsql).executeUpdate();
							}
							
						}
						
						// 退保标志,退保人，退保日期，延保日期  设置为null
						String updqlkk="update MaintContractDetail set "
								+ "IsSurrender=null,SurrenderUser=null,SurrenderDate=null,DelayEDate=null "
								+ "where BillNo='"+id+"'";
						hs.connection().prepareStatement(updqlkk).executeUpdate();
						
						//删除电梯档案信息已经存在的电梯编号
						String delsql="delete a from ElevatorArchivesInfo a,MaintContractMaster m,MaintContractDetail d "
								+ "where a.ElevatorNo=d.ElevatorNo and m.BillNo=d.BillNo "
								+ "and m.BillNo='"+master.getBillNo()+"'";
						hs.connection().prepareStatement(delsql).executeUpdate();
						
						//电梯档案信息
						query = hs.createQuery("from MaintContractDetail where billNo = '"+master.getBillNo()+"'"); 
						List list = query.list();
						ElevatorArchivesInfo eaInfo = null;
						MaintContractDetail mcDetail = null;
						for (Object object : list) {
							mcDetail = (MaintContractDetail) object;
							eaInfo = new ElevatorArchivesInfo();
							
							eaInfo.setMaintContractNo(master.getMaintContractNo());//维保合同号
							eaInfo.setMaintDivision(master.getMaintDivision());//分部
		 					eaInfo.setMaintStation(master.getMaintStation());//维保站
							eaInfo.setSalesContractNo(mcDetail.getSalesContractNo());//销售合同号
		 					eaInfo.setProjectName(mcDetail.getProjectName());//购买单位名称
							eaInfo.setProjectAddress(mcDetail.getMaintAddress());//使用单位名称
							
							eaInfo.setElevatorNo(mcDetail.getElevatorNo());
							eaInfo.setElevatorType(mcDetail.getElevatorType());
							eaInfo.setElevatorParam(mcDetail.getElevatorParam());
							eaInfo.setFloor(mcDetail.getFloor());
							eaInfo.setStage(mcDetail.getStage());
							eaInfo.setDoor(mcDetail.getDoor());
							eaInfo.setHigh(mcDetail.getHigh());
							
							eaInfo.setOperId(userInfo.getUserID());
							eaInfo.setOperDate(CommonUtil.getNowTime());
							hs.save(eaInfo);
						}
					}

					hs.save(master);
					tx.commit();
					
					if("Y".equals(submitType)){
						if(historyBillNo!=null && !"".equals(historyBillNo)){
							//删除退保的保养 工作计划
							CommonUtil.deleMaintenanceWorkPlan(historyBillNo, master.getBillNo(), "", userInfo, errors);
						}
					}
				} catch (Exception e) {		
					if(tx!=null){
						tx.rollback();
					}
					e.printStackTrace();
					if(errors.isEmpty()){
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","审核失败！"));
					}
				} finally {
					if(hs != null){
						hs.close();				
					}				
				}
				
			} else {
				if(errors.isEmpty()){
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","审核失败！"));
				}
			}	
			
			if(errors.isEmpty()){			
				if("N".equals(submitType)){
					//提示“合同驳回成功!”
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("contract.toreback.success")); 
				}else{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("auditing.succeed")); //提示“审核成功！”
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

}
