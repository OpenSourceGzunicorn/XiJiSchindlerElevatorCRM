package com.gzunicorn.struts.action.engcontractmanager.maintcontract;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
import org.apache.struts.upload.FormFile;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.engcontractmanager.entrustcontractdetail.EntrustContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class MaintContractModifyAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintContractModifyAction.class);

	BaseDataImpl bd = new BaseDataImpl();

	/**
	 * 维保合同修改金额
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
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "maintContractModify", null);
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
		
		request.setAttribute("navigator.location","维保合同修改 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		HTMLTableCache cache = new HTMLTableCache(session, "maintContractModifyList");

		DefaultHTMLTable table = new DefaultHTMLTable();
		table.setMapping("fMaintContractModifyDisplay");
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
		String salesContractNo = tableForm.getProperty("salesContractNo");// 销售合同号
		String elevatorNo = tableForm.getProperty("elevatorNo");// 电梯编号

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
			
			//审核的，新签的，续签的 维保合同。
			String sql = "select a,b.username as attn,p.pullname as contractStatus," +
					" c.comname as maintDivision, d.companyName as companyName,s.storagename as storagename "+
					" from MaintContractMaster a,Loginuser b,Company c,Customer d,Pulldown p,Storageid s " + 
					" where a.attn = b.userid " +
					" and a.maintDivision = c.comid"+
					" and a.maintStation = s.storageid "+
					" and a.contractStatus = p.id.pullid " +
					" and a.submitType = 'Y' and a.auditStatus='Y'"+
					" and a.companyId = d.companyId"+
					" and p.id.typeflag = 'MaintContractMaster_ContractStatus'";
					//" and a.contractStatus in('ZB','XB') ";
			
			if (billNo != null && !billNo.equals("")) {
				sql += " and a.billNo like '%"+billNo.trim()+"%'";
			}
			if (maintContractNo != null && !maintContractNo.equals("")) {
				sql += " and a.maintContractNo like '%"+maintContractNo.trim()+"%'";
			}
			if (companyName != null && !companyName.equals("")) {
				sql += " and d.companyName like '%"+companyName.trim()+"%'";
			}
			if (attn != null && !attn.equals("")) {
				sql += " and a.attn like '%"+attn.trim()+"%'";
			}
			if (maintDivision != null && !maintDivision.equals("")) {
				sql += " and a.maintDivision like '"+maintDivision.trim()+"'";
			}
			if(salesContractNo!=null && !salesContractNo.equals("")){
				sql+=" and a.billNo in (select billNo from MaintContractDetail where salesContractNo like '%"+salesContractNo.trim()+"%')";
			}
			if(elevatorNo!=null && !elevatorNo.equals("")){
				sql+=" and a.billNo in (select billNo from MaintContractDetail where elevatorNo like'"+elevatorNo.trim()+"')";
			}
			
			String order = " order by "+table.getSortColumn();
			
			if (table.getIsAscending()) {
				sql += order + " asc";
			} else {
				sql += order + " desc";
			}
			
			//System.out.println(">>>>>>>"+sql);
			
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
				master.put("contractStatus",master2.getContractStatus());
				 
				master.put("attn", String.valueOf(objs[1]));
				master.put("contractStatusName", String.valueOf(objs[2]));
				master.put("maintDivision", String.valueOf(objs[3]));
				master.put("companyId", String.valueOf(objs[4]));
				master.put("maintStation", String.valueOf(objs[5]));
				
				maintContractAuditList.add(master);
			}

			table.addAll(maintContractAuditList);
			session.setAttribute("maintContractModifyList", table);

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
		forward = mapping.findForward("maintContractModifyList");
		
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
		
		request.setAttribute("navigator.location","维保合同修改 >> 修 改");
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
					/**
					String contractStatus=master.getContractStatus();
					if(contractStatus!=null && (contractStatus.trim().equals("LS") || contractStatus.trim().equals("TB"))){
						contractStatus="N";
					}else{
						contractStatus="Y";
					}
					//退保，历史的只能修改 金额。
					request.setAttribute("isupdate", contractStatus);
					*/
					//退保，历史的只能修改 金额。
					request.setAttribute("isupdate", "Y");
					
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

						detail.setElevatorType(bd.getOptionName(detail.getElevatorType(), elevatorTypeList));
						//if(contractStatus.equals("N")){
						//	detail.setR1(bd.getOptionName(detail.getSignWay(), signWayList));
						//	detail.setElevatorNature(bd.getOptionName(detail.getElevatorNature(), elevatorNatureList));
						//}
					}					
					request.setAttribute("maintContractDetailList", maintContractDetailList);
					request.setAttribute("elevatorNatureList", elevatorNatureList);
					
					// 分部下拉框列表
					request.setAttribute("maintDivisionList", Grcnamelist1.getgrcnamelist("admin"));
					// 维保站下拉列表
					request.setAttribute("maintStationList", bd.getMaintStationList(master.getMaintDivision()));
					
					//维保合同明细的签署方式 
					request.setAttribute("signWayList", signWayList);
										
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
				}
				// 合同状态下拉框列表
				request.setAttribute("contractStatusList", bd.getPullDownList("MaintContractMaster_ContractStatus"));
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

			forward = mapping.findForward("maintContractModifyDisplay");
		}		
		
		saveToken(request); //生成令牌，防止表单重复提交
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}
	
	/**
	 * 修改方法
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

		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
		
		String billNo =(String)dform.get("billNo");
		String contractSdate =(String)dform.get("contractSdate");
		String contractEdate =(String)dform.get("contractEdate");
		String contractPeriod=(String)dform.get("contractPeriod");
		Double contractTotal =(Double)dform.get("contractTotal");
		Double otherFee =(Double)dform.get("otherFee");
		String rem =(String)dform.get("rem");
		String isupdate=request.getParameter("isupdate");
		String mainMode =(String)dform.get("mainMode");//保养方式
		String maintStation =(String)dform.get("maintStation");//所属维保站
		String maintDivision =(String)dform.get("maintDivision");//所属维保站
		String companyId =(String)dform.get("companyId");
		String contractStatus =(String)dform.get("contractStatus");//合同状态

		List mwpmlist=new ArrayList();
		String taskSubFlag="";
		if(billNo != null && !billNo.equals("")){
			
			Session hs = null;
			Transaction tx = null;

			MaintContractMaster master = null;						
			try {
				
				hs = HibernateUtil.getSession();
				tx = hs.beginTransaction();

				master = (MaintContractMaster) hs.get(MaintContractMaster.class, billNo);
				master.setCompanyId(companyId);
				master.setOldContractTotal(master.getContractTotal());
				master.setOldOtherFee(master.getOtherFee());
				master.setContractSdate(contractSdate);//合同开始日期
				master.setContractEdate(contractEdate);//合同结束日期
				master.setContractPeriod(contractPeriod);
				master.setContractTotal(contractTotal);//合同总额
				master.setOtherFee(otherFee);//其他费用
				master.setRem(rem);
				master.setModifyId(userInfo.getUserID());
				master.setModifyDate(CommonUtil.getNowTime());
				master.setMainMode(mainMode);//保养方式
				master.setMaintStation(maintStation);
				master.setMaintDivision(maintDivision);
				master.setContractStatus(contractStatus);
				

				/**==========有服务费的合同增加业务费序号 开始==========*/
				//其它费用大于0
				if(master.getOtherFee()!=null && master.getOtherFee()>0){
					if(master.getR1()==null || master.getR1().equals("")){
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
				}
				/**==========有服务费的合同增加业务费序号 结束==========*/

				hs.save(master);
				
				if(isupdate!=null && isupdate.trim().equals("Y")){
					
					taskSubFlag=master.getTaskSubFlag();//下达标志
					
					String[] rowid=request.getParameterValues("rowid");
					String[] mainEdate=request.getParameterValues("mainEdate");
					String[] isSurrender=request.getParameterValues("isSurrender");
					String[] elevatorNature=request.getParameterValues("elevatorNature");//电梯性质
					String[] signWay=request.getParameterValues("signWay");//签署方式
					String[] assignedMainStation=request.getParameterValues("assignedMainStation");//下达维保站
					String[] elevatorNo=request.getParameterValues("elevatorNo");//电梯编号
					String[] isCertificate=request.getParameterValues("isCertificate");//是否已经回验收合格证
					String[] r4=request.getParameterValues("r4");//是否发台量奖
					String[] r5=request.getParameterValues("r5");//是否为别墅梯
					
					String rowidstr="";
					for(int i=0;i<rowid.length;i++){
						rowidstr+=rowid[i]+",";
						
						MaintContractDetail ecd=(MaintContractDetail)hs.get(MaintContractDetail.class, Integer.valueOf(rowid[i]));
						String mainedatekk=ecd.getMainEdate();
						
						if("Y".equals(isCertificate[i])){
							if(ecd.getCertificateDate()==null){
								ecd.setCertificateDate(CommonUtil.getNowTime());//录入验收合格证日期
							}
							
							//“是否已经回验收合格证” 在“是”的基础上又修改维保结束日期的，才记录更改前日期和更改信息日期。
							if(!ecd.getMainEdate().equals(mainEdate[i])){
								ecd.setR1(ecd.getMainEdate());//维保原始结束日期，最近的日期
								ecd.setR2(CommonUtil.getNowTime());//维保合同修改操作日期
								ecd.setR3(userInfo.getUserID());//修改人
								//ecd.setMainEdate(mainEdate[i]);//更改的维保结束日期
							}
						}else{
							ecd.setR1(null);//维保原始结束日期，最近的日期
							ecd.setR2(null);//维保合同修改操作日期
							ecd.setR3(null);//修改人
							ecd.setCertificateDate(null);//录入验收合格证日期
						}
						
						//维保结束日期不一致，就保存
						if(!ecd.getMainEdate().equals(mainEdate[i])){
							ecd.setMainEdate(mainEdate[i]);//更改的维保结束日期
						}
						ecd.setIsCertificate(isCertificate[i]);
						
						ecd.setIsSurrender(isSurrender[i]);//是否退保
						if(isSurrender[i]!=null && isSurrender[i].trim().equals("Y")){
							ecd.setRealityEdate(mainEdate[i]);//实际结束日期
							
							//不存在，退保日期，才增加退保日期。
							String surrenderdate=ecd.getSurrenderDate();
							if(surrenderdate==null || "".equals(surrenderdate)){
								ecd.setSurrenderDate(CommonUtil.getNowTime());//退保操作日期
								ecd.setSurrenderUser(userInfo.getUserID());//退保人
							}
							
						}else{
							ecd.setRealityEdate(null);//实际结束日期
							ecd.setSurrenderDate(null);
							ecd.setSurrenderUser(null);
						}
						ecd.setElevatorNo(elevatorNo[i]);
						ecd.setAssignedMainStation(assignedMainStation[i]);
						ecd.setElevatorNature(elevatorNature[i]);
						ecd.setSignWay(signWay[i]);
						ecd.setR4(r4[i]);
						ecd.setR5(r5[i]);
						hs.save(ecd);
						
						HashMap hmap=new HashMap();
						hmap.put("rowid", rowid[i]);
						hmap.put("issurrender", isSurrender[i]);
						hmap.put("mainEdate", mainEdate[i]);//新日期
						hmap.put("oldmainEdate", mainedatekk);//旧日期

						mwpmlist.add(hmap);
					}
					
					if(rowidstr!=null && !rowidstr.trim().equals("")){
						rowidstr=rowidstr.substring(0, rowidstr.length()-1);
						//删除页面不存在的明细
						hs.createQuery("delete from MaintContractDetail where billno='"+billNo+"' and rowid not in("+rowidstr+")").executeUpdate();
						
					}
					
				}
				
				//修改 维保作业计划 的电梯编号与维保合同明细的电梯编号一致
				String upsqlk="update a set a.ElevatorNo=b.ElevatorNo "
						+ "from MaintenanceWorkPlanMaster a,MaintContractDetail b "
						+ "where a.rowid=b.rowid and b.billNo in ('"+billNo+"')";
				hs.connection().prepareStatement(upsqlk).executeUpdate();
				
				/***================ 修改应收款的维保站 开始======================**/
				//更新应收款
				String upsql="update ProContractARFeeMaster set "
						+ "maintDivision='"+maintDivision+"',MaintStation='"+maintStation+"',CompanyID='"+companyId+"' "
						+ "where BillNo='"+billNo+"'";
				hs.connection().prepareStatement(upsql).executeUpdate();
				/**
				//更新来款管理
				upsql="update ContractParagraphManage set "
						+ "maintDivision='"+maintDivision+"',MaintStation='"+maintStation+"',CompanyID='"+companyId+"' "
						+ "where BillNo='"+billNo+"'";
				hs.connection().prepareStatement(upsql).executeUpdate();
				//更新开票管理
				upsql="update ContractInvoiceManage set "
						+ "maintDivision='"+maintDivision+"',MaintStation='"+maintStation+"',CompanyID='"+companyId+"' "
						+ "where BillNo='"+billNo+"'";
				hs.connection().prepareStatement(upsql).executeUpdate();
				*/
				/***================ 修改应收款的维保站 结束======================**/
				
				tx.commit();
				
				//下达标志
				if(taskSubFlag!=null && taskSubFlag.equals("Y")){
					for(int i=0;i<mwpmlist.size();i++){
						HashMap hmap=(HashMap)mwpmlist.get(i);
						String rowid2=(String)hmap.get("rowid");
						String mainEdate2=(String)hmap.get("mainEdate");//新维保合同结束日期
						String oldmainEdate2=(String)hmap.get("oldmainEdate");//旧维保合同结束日期
						String issurrender=(String)hmap.get("issurrender");//是否退保
	
						if(issurrender.equals("Y")){
							//删除保养计划
							CommonUtil.deleMaintenanceWorkPlan2(rowid2, mainEdate2, userInfo, errors);
						}else{
							/**维保结束日期不一致,就生成保养计划，*/
							if(!mainEdate2.trim().equals(oldmainEdate2)){
								//生成保养计划
								CommonUtil.toMaintenanceWorkPlan2(rowid2, null, userInfo, errors,mainEdate2);
							}
						}
					}
				}
				if (errors.isEmpty()){
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","保存成功！"));
				}
			} catch (Exception e) {		
				if(tx!=null){
					tx.rollback();
				}
				e.printStackTrace();
				if(errors.isEmpty()){
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","保存失败！"));
				}
			} finally {
				if(hs != null){
					hs.close();				
				}				
			}
		}
			
		if (!errors.isEmpty()){
			this.saveErrors(request, errors);
		}

		return mapping.findForward("returnList");

	}
	/**
	 * 同步方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toSynchRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, HibernateException {
		
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();

		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
			
		Session hs = null;
		Transaction tx = null;
			
		try {
			
			hs = HibernateUtil.getSession();		
			tx = hs.beginTransaction();

			//同步，层,站,门,提升高度,电梯参数,项目名称，保养地址
			String upsql="update a " +
					"set a.Floor=b.Floor,a.Stage=b.Stage,a.Door=b.Door," +
					"a.High=b.High,a.ElevatorParam=b.ElevatorParam,a.ElevatorType=b.ElevatorType," +
					"a.ProjectName=b.SalesContractName,a.MaintAddress=b.DeliveryAddress,"+
					"a.SalesContractNo=b.SalesContractNo " +
					"from MaintContractDetail a,ElevatorSalesInfo b " +
					"where a.ElevatorNo=b.ElevatorNo";
			hs.connection().prepareStatement(upsql).executeUpdate();
			
			tx.commit();
			
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","同步成功！"));

		} catch (Exception e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","同步失败！"));
			
			if(tx!=null){
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if(hs != null){
				hs.close();				
			}				
		}
			
		if (!errors.isEmpty()){
			this.saveErrors(request, errors);
		}

		return mapping.findForward("returnList");

	}
	/**
	 * 同步-最大维保结束日期
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toSynchRecord2(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, HibernateException {
		
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();

		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
			
		Session hs = null;
		Transaction tx = null;
			
		try {
			
			hs = HibernateUtil.getSession();		
			tx = hs.beginTransaction();

			//合同结束日期同步为电梯保养结束日期的最大值
			String upsql="update a set a.ContractEDate=b.MainEDate "
					+ "from MaintContractMaster a,"
					+ "(select billno,MAX(MainEDate) as MainEDate from MaintContractDetail group by BillNo) b "
					+ "where a.BillNo=b.BillNo";
			hs.connection().prepareStatement(upsql).executeUpdate();
			
			tx.commit();
			
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","同步成功！"));

		} catch (Exception e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","同步失败！"));
			
			if(tx!=null){
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if(hs != null){
				hs.close();				
			}				
		}
			
		if (!errors.isEmpty()){
			this.saveErrors(request, errors);
		}

		return mapping.findForward("returnList");

	}
	
	/**
	 * 改成未下达状态
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
			throws IOException, ServletException, HibernateException {
		
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();

		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		String id = request.getParameter("id");
			
		Session hs = null;
		Transaction tx = null;
			
		if(id!=null && !id.trim().equals("")){
			try {
				
				hs = HibernateUtil.getSession();		
				tx = hs.beginTransaction();
				
				//根据流水号更改明细
				String upsql2="update a set a.AssignedSign=null,a.AssignedSignFlag=null,a.AssignedMainStation=null,"
						+ "a.AssignedSignDate=null,a.ReturnReason=null,a.MaintPersonnel=null,"
						+ "a.DelayEDate=null,a.RealityEDate=null "
						+ "from MaintContractDetail a where a.billno in('"+id.trim()+"')";
				hs.connection().prepareStatement(upsql2).executeUpdate();
				
				//根据流水号更改主信息 修为为未下达
				String upsql="update MaintContractMaster "
						+ "set TaskUserID=null,TaskSubFlag='N',TaskSubDate=null,TaskRem=null "
						+ "where billno in('"+id.trim()+"')";
				hs.connection().prepareStatement(upsql).executeUpdate();
				
				//删除维保作业计划书明细
				String upsql3="delete d from MaintenanceWorkPlanDetail d,MaintenanceWorkPlanMaster a,MaintContractDetail b "
						+ "where d.billno=a.billno and a.rowid=b.rowid and b.BillNo in('"+id.trim()+"')";
				hs.connection().prepareStatement(upsql3).executeUpdate();
				
				//删除维保作业计划书主表
				String upsql4="delete a from MaintenanceWorkPlanMaster a,MaintContractDetail b "
						+ "where a.rowid=b.rowid and b.BillNo in('"+id.trim()+"')";
				hs.connection().prepareStatement(upsql4).executeUpdate();
				
				tx.commit();
				
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","改成未下达状态 成功！"));
	
			} catch (Exception e) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","改成未下达状态 失败！"));
				
				if(tx!=null){
					tx.rollback();
				}
				e.printStackTrace();
			} finally {
				if(hs != null){
					hs.close();				
				}				
			}
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
			MaintContractMaster master = (MaintContractMaster) hs.get(MaintContractMaster.class, id);
			if (master != null) {
				hs.createQuery("delete from MaintContractDetail where billno='"+id+"'").executeUpdate();
				//维保合同退保丢梯报告
				hs.createQuery("delete from LostElevatorReport where billno='"+id+"'").executeUpdate();
				
				hs.delete(master);
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
			e2.printStackTrace();
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
	
	@SuppressWarnings("unchecked")
	public ActionForward toPrepareImportRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		request.setAttribute("navigator.location","维保合同修改 >> 导入");
		DynaActionForm dform = (DynaActionForm) form;
		if (request.getAttribute("error") == null
				|| request.getAttribute("error").equals("")) {
			dform.initialize(mapping);
		}
		return mapping.findForward("maintContractModifyImport");
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
	
	public ActionForward toImportRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		FormFile file = (FormFile) dform.get("file"); //获取上传文件
		String fileName = file.getFileName();
		String fileFromt=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()); //获取上传文件的后缀名
		
		InputStream in = null;
		Session hs = null;
		Transaction tx = null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		StringBuffer reStr = new StringBuffer(); //错误返回信息
	
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if (fileFromt!=null && fileFromt.equals("xlsx")) {//excel 2007
				
				in = file.getInputStream();
				XSSFWorkbook wb = new XSSFWorkbook(in);
				XSSFSheet sheet = wb.getSheetAt(0);
				XSSFRow row = null;			
				
				int rowSum = sheet.getLastRowNum()+1; //最大行数
				
				String userid=userInfo.getUserID();
				String today=CommonUtil.getNowTime();

				List implist=new ArrayList();
				HashMap hmap=null;
				for(int rowNum = 1; rowNum < rowSum; rowNum++){
					row = sheet.getRow(rowNum);	
					
				    String elevatorNo=cellValueToString(row, 0, reStr,"");//电梯编号
				    String mainEDate=cellValueToString(row, 1, reStr,",格式为(1970-01-01)");//维保结束日期
				    if(reStr != null && reStr.length() > 0){
						break;
					}
				    
				    hmap=new HashMap();
				    hmap.put("elevatorNo", elevatorNo);
				    hmap.put("mainEDate", mainEDate);
				    implist.add(hmap);

				}
	
				//维保合同修改的导入功能，增加将“是否已经回验收合格证”改为‘是’并记录“录入验收合格证日期”为当前日期。
				if(reStr == null || reStr.length() == 0){
					String sqldel="update MaintContractDetail "
							+ "set R1=MainEDate,R2=?,R3=?,MainEDate=?,isCertificate=?,CertificateDate=? "
							+ "where ElevatorNo=?";
					pstmt=hs.connection().prepareStatement(sqldel);
					for(int i=0;i<implist.size();i++){
						hmap=(HashMap)implist.get(i);
						pstmt.setString(1, today); 
						pstmt.setString(2, userid);  
						pstmt.setString(3, (String)hmap.get("mainEDate")); 
						pstmt.setString(4, "Y"); 
						pstmt.setString(5, today); 
						pstmt.setString(6, (String)hmap.get("elevatorNo")); 
						pstmt.addBatch(); 
					}
					pstmt.executeBatch();
					hs.flush();
					

					tx.commit();
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","上传成功！"));
					
					for(int i=0;i<implist.size();i++){
						hmap=(HashMap)implist.get(i);
						String eleno=(String)hmap.get("elevatorNo");
						String mainEdate2="";
						String oldmainEdate2="";
						String rowid2="";
						
						String sqlc="select rowid,MainEDate,isnull(R1,MainEDate) as oldMainEDate "
								+ "from MaintContractDetail "
								+ "where rowid in"
								+ "(select MAX(rowid) from MaintContractDetail where ElevatorNo='"+eleno+"')";
						rs=hs.connection().prepareStatement(sqlc).executeQuery();
						if(rs.next()){
							rowid2=rs.getString("rowid");
							mainEdate2=rs.getString("MainEDate");
							oldmainEdate2=rs.getString("oldMainEDate");
							/**维保结束日期不一致,就生成保养计划，*/
							if(!mainEdate2.trim().equals(oldmainEdate2)){
								//生成保养计划
								CommonUtil.toMaintenanceWorkPlan2(rowid2, null, userInfo, errors,mainEdate2);
							}
						}
					}
				} else {
					request.setAttribute("reStr", reStr);//错误返回信息
				}
			
			}

		} catch (Exception e2) {
			e2.printStackTrace();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","上传失败！"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		ActionForward forward = mapping.findForward("returnImport");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}

		return forward;
	}
	
	
	
	/**
	 * 返回单元格字符串值
	 * @param XSSFRow 行对象
	 * @param cellNum 所在列数
	 * @param reStr 错误信息  
	 * @return String
	 */
	public String cellValueToString(XSSFRow row, int cellNum, StringBuffer reStr,String info){
		String value = "";
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) { 
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)不能为空;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			value = cell.getStringCellValue();
		//} else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
		//	value = String.valueOf((int) cell.getNumericCellValue());
		}else {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)数据格式必须为字符串"+info+";<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		return value.trim();
	}
	/**
	 * 返回单元格字符串值
	 * @param XSSFRow 行对象
	 * @param cellNum 所在列数
	 * @param reStr 错误信息  
	 * @return String
	 */
	public String cellValueToString(XSSFRow row, int cellNum){
		String value = "";
		XSSFCell cell = row.getCell(cellNum);
		try{
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			value = cell.getStringCellValue();
		}
		}catch (Exception e){
			value = "";
		}
		return value.trim();
	}
	/**
	 * 返回单元格整型值
	 * @param XSSFRow 行对象
	 * @param cellNum 所在列数
	 * @param reStr 错误信息  
	 * @return String
	 */
	public int cellValueToInt(XSSFRow row, int cellNum, StringBuffer reStr){
		int value = 0;
		XSSFCell cell = row.getCell(cellNum);
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)为空;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		} else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			value = (int) cell.getNumericCellValue();
		} else {
			reStr.append("单元格(" + (row.getRowNum() + 1) + "行，"+ getCellChar(cellNum) + "列)数据格式必须为数值;<br>&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		return value;
	}

	/**
	 * 将单元格列数转换为大写字母
	 * @param cellNum 列数 
	 * @return char
	 */
	public char getCellChar(int cellNum){
		return (char) (cellNum + 65);
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
//				String hql="select a from Storageid a where a.comid='"+comid+"' " +
//						"and a.storagetype=1 and a.parentstorageid='0' and a.enabledflag='Y'";
//				List list=hs.createQuery(hql).list();
				List list=bd.getMaintStationList(comid);
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
