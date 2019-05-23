package com.gzunicorn.struts.action.contractpayment;

import java.io.IOException;
import java.text.DecimalFormat;
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
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.contractpayment.contractinvoicemanage.ContractInvoiceManage;
import com.gzunicorn.hibernate.contractpayment.procontractarfeemaster.ProContractArfeeMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;

public class ContractPaymentQueryAction extends DispatchAction {

	Log log = LogFactory.getLog(ContractPaymentQueryAction.class);
	
	BaseDataImpl bd = new BaseDataImpl();
	DecimalFormat df = new DecimalFormat("##.##"); 
	
	/**
	 * 合同收款查询 
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


		// Set default method is toSearchRecord
		String name = request.getParameter("method");
		
		/** **********开始用户权限过滤*********** */
		SysRightsUtil.filterModuleRight(request, response,SysRightsUtil.NODE_ID_FORWARD + "contractpaymentquery", null);
		/** **********结束用户权限过滤*********** */
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
		
		request.setAttribute("navigator.location","合同收款查询 >> 查询列表");		
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ServeTableForm tableForm = (ServeTableForm) form;
		String action = tableForm.getAction();

		if (tableForm.getProperty("genReport") != null
				&& !tableForm.getProperty("genReport").equals("")) {
			try {
				response = toExcelRecord(form,request,response);
			} catch (IOException e) {
				e.printStackTrace();
			}
			forward = mapping.findForward("exportExcel");
			tableForm.setProperty("genReport","");

		} else {
			HTMLTableCache cache = new HTMLTableCache(session, "contractPaymentQueryList");

			DefaultHTMLTable table = new DefaultHTMLTable();
			table.setMapping("fContractPaymentQuery");
			table.setLength(SysConfig.HTML_TABLE_LENGTH);
			cache.updateTable(table);
			table.setSortColumn("contractNo");
			table.setIsAscending(true);
			cache.updateTable(table);
			/** 该方法是记住了历史查询条件*/
			if (action.equals(ServeTableForm.NAVIGATE) || action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else if (action.equals("Submit")) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			if (action.equals(ServeTableForm.NAVIGATE)
					|| action.equals(ServeTableForm.SORT)) {
				cache.loadForm(tableForm);
			} else {
				table.setFrom(0);
			}
			cache.saveForm(tableForm);
			
			String contractType = tableForm.getProperty("contractType");// 合同类型
			String contractNo = tableForm.getProperty("contractNo");// 合同号
			String companyId = tableForm.getProperty("companyId");// 甲方单位
			String contractTotal = tableForm.getProperty("contractTotal");// 
			String maintDivision=tableForm.getProperty("maintDivision");//
			String maintStation=tableForm.getProperty("maintStation");//
			
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

				String sql = "select a.billno billno,a.maintContractNo contractNo,a.busType contractType,a.contractTotal contractTotal,e.companyName companyId," +
						"d.preMoney preMoney,c.invoiceMoney invoiceMoney,b.paragraphMoney paragraphMoney,a.maintDivision maintDivision,"
						+ "(isnull(invoiceMoney,0)-isnull(paragraphMoney,0)) arrearsMoney," +
						"s.storagename maintStation from Compact_view a "+
						"left join (select BillNo,SUM(paragraphMoney) paragraphMoney from ContractParagraphManage group by BillNo) b on a.billno=b.BillNo "+
						"left join (select BillNo,SUM(InvoiceMoney) invoiceMoney from ContractInvoiceManage group by BillNo) c on a.billno=c.BillNo "+
						"left join (select BillNo,SUM(PreMoney) preMoney from ProContractARFeeMaster group by BillNo) d on a.billno=d.BillNo,"
						+ "Customer e,StorageID s where s.StorageID=a.maintStation and a.companyID=e.CompanyID"+
						" and a.billno in(select distinct BillNo from ProContractARFeeMaster where auditStatus='Y')";
				
				if (contractType != null && !contractType.equals("")) {
					sql += " and a.busType like '%"+contractType.trim()+"%'";
				}
				if (contractNo != null && !contractNo.equals("")) {
					sql += " and a.maintContractNo like '%"+contractNo.trim()+"%'";
				}
				if (companyId != null && !companyId.equals("")) {
					sql += " and e.companyName like '%"+companyId.trim()+"%'";
				}
				if (contractTotal != null && !contractTotal.equals("")) {
					sql+=" and contractTotal="+Double.valueOf(contractTotal);
				}
				if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and maintDivision like '"+maintDivision.trim()+"'";
				}
				if (maintStation != null && !maintStation.equals("")) {
					sql += " and (s.storagename like '%"+maintStation.trim()+"%' or s.StorageID like '%"+maintStation.trim()+"%')";
				}
				if (table.getIsAscending()) {
					sql += " order by "+ table.getSortColumn() +" desc";
				} else {
					sql += " order by "+ table.getSortColumn() +" asc";
				}
				
				//System.out.println(">>>>"+sql);
				
				query = hs.createSQLQuery(sql);
				table.setVolume(query.list().size());// 查询得出数据记录数;

				// 得出上一页的最后一条记录数号;
				query.setFirstResult(table.getFrom()); // pagefirst
				query.setMaxResults(table.getLength());

				cache.check(table);
				
				double preTotal=0;
				double invoiceTotal=0;
				double paragraphTotal=0;
				double arraearTotal=0;
				String billnostr="";
				
				List list = query.list();
				List contractPaymentQueryList = new ArrayList();
				for (Object object : list) {
					Object[] objs = (Object[])object;
					Map master=new HashMap();
					billnostr+=objs[0].toString().trim()+",";
					
					master.put("billno", objs[0]);
					master.put("contractNo", objs[1]);
					master.put("contractType", objs[2]);
					master.put("contractTotal", objs[3]);
					master.put("companyId", objs[4]);
					double preMoney1=objs[5]==null ? 0 : Double.valueOf(objs[5].toString());
					master.put("preMoney", df.format(preMoney1<0 ? 0 : preMoney1));
					double invoiceMoney=objs[6]==null ? 0 : Double.valueOf(objs[6].toString());
					master.put("invoiceMoney", df.format(invoiceMoney<0 ? 0 : invoiceMoney));
					double paragraphMoney=objs[7]==null ? 0 : Double.valueOf(objs[7].toString());
					master.put("paragraphMoney", df.format(paragraphMoney<0 ? 0 : paragraphMoney));
					
					master.put("maintDivision", bd.getName(hs,"Company", "comname", "comid",objs[8].toString()));
					master.put("maintStation", objs[10].toString());
					
					double arrearsMoney=objs[9]==null ? 0 : Double.valueOf(objs[9].toString());
					master.put("arrearsMoney", df.format(arrearsMoney<0 ? 0 : arrearsMoney));
					contractPaymentQueryList.add(master);
					preTotal+=objs[5]==null ? 0 : Double.valueOf(objs[5].toString());
					invoiceTotal+=objs[6]==null ? 0 : Double.valueOf(objs[6].toString());
					paragraphTotal+=objs[7]==null ? 0 : Double.valueOf(objs[7].toString());
					arraearTotal+=objs[9]==null ? 0 : Double.valueOf(objs[9].toString());
				}

				double preMoney=0;
				if(billnostr!=null && !billnostr.trim().equals("")){
					
					billnostr=billnostr.substring(0,billnostr.length()-1);
					billnostr=billnostr.replaceAll(",", "','");
					//逾期应收款金额
					String yqstr="select isnull(SUM(PreMoney),0) from ProContractARFeeMaster" +
							" where PreDate<'"+CommonUtil.getToday()+"' and BillNo in('"+billnostr+"')";
					//System.out.println(">>>>"+yqstr);
					List preList=hs.createSQLQuery(yqstr).list();
					if(preList!=null && preList.size()>0){
						preMoney=Double.valueOf(preList.get(0).toString());
					}
				}
				table.addAll(contractPaymentQueryList);
				session.setAttribute("contractPaymentQueryList", table);
				/*// 合同性质下拉框列表
				request.setAttribute("contractNatureOfList", bd.getPullDownList("MaintContractMaster_ContractNatureOf"));*/
				// 分部下拉框列表
				request.setAttribute("maintDivisionList", maintDivisionList);
				request.setAttribute("preTotal", df.format(preTotal<0 ? 0 : preTotal));
				request.setAttribute("invoiceTotal", df.format(invoiceTotal<0 ? 0 : invoiceTotal));
				request.setAttribute("paragraphTotal", df.format(paragraphTotal<0 ? 0 : paragraphTotal));
				request.setAttribute("arraearTotal", df.format(arraearTotal<0 ? 0 : arraearTotal));
				request.setAttribute("pre", df.format((preMoney-paragraphTotal)<0 ? 0 : preMoney-paragraphTotal));

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (HibernateException e1) {
				e1.printStackTrace();
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
			forward = mapping.findForward("contractPaymentQueryList");
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
		
		request.setAttribute("navigator.location","合同收款查询 >> 查看");
		ActionForward forward = null;
		ActionErrors errors = new ActionErrors();
		
		display(form, request, errors, "display");
		
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		request.setAttribute("display", "yes");
		forward = mapping.findForward("contractPaymentQueryDisplay");
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
		
		return response;
	}

	public void display(ActionForm form, HttpServletRequest request ,ActionErrors errors ,String flag){
		
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;		
		String id = request.getParameter("id");
		if(id==null || "".equals(id)){
			id=(String) request.getAttribute("id");
		}
		Session hs = null;
		if (id != null && !id.equals("")) {
			try {
				hs = HibernateUtil.getSession();
				String sql="select a.billno,a.maintContractNo,a.busType,a.contractTotal,e.companyName,d.preMoney,"
						+ "c.invoiceMoney,b.paragraphMoney,a.maintDivision,a.maintStation " +
						"from ViewCompact a "+
						"left join (select BillNo,SUM(paragraphMoney) paragraphMoney from ContractParagraphManage group by BillNo) b on a.billno=b.BillNo "+
						"left join (select BillNo,SUM(invoiceMoney) invoiceMoney from ContractInvoiceManage group by BillNo) c on a.billno=c.BillNo "+
						"left join (select BillNo,SUM(preMoney) preMoney from ProContractArfeeMaster group by BillNo) d on a.billno=d.BillNo,Customer e " +
						"where a.companyID=e.CompanyID and a.billno='"+id.trim()+"'";
				Query query = hs.createSQLQuery(sql);
				List list = query.list();
				if (list != null && list.size() >= 0) {
					Map constractBean=new HashMap();
					for(Object object : list){
						Object[] objs=(Object[])object;
						constractBean.put("billno", objs[0]);
						constractBean.put("contractNo", objs[1]);
						constractBean.put("contractType", objs[2]);
						constractBean.put("contractTotal", objs[3]);
						constractBean.put("companyId", objs[4]);
						constractBean.put("preMoney", df.format(objs[5]==null ? 0 : Double.valueOf(objs[5].toString())));
						constractBean.put("amount", df.format(objs[6]==null ? 0 : Double.valueOf(objs[6].toString())));
						constractBean.put("invoiceTotal", df.format(objs[7]==null ? 0 : Double.valueOf(objs[7].toString())));
						constractBean.put("noInvoiceTotal", df.format((objs[3]==null ? 0 : Double.valueOf(objs[3].toString()))-(objs[7]==null ? 0 : Double.valueOf(objs[7].toString()))));
						constractBean.put("arrearsMoney", df.format((objs[5]==null ? 0 : Double.valueOf(objs[5].toString()))-(objs[6]==null ? 0 : Double.valueOf(objs[6].toString()))));

						constractBean.put("maintDivision", bd.getName(hs, "Company", "comname", "comid",objs[8].toString()));
						constractBean.put("maintStation",  bd.getName(hs, "Storageid", "storagename", "storageid",objs[9].toString()));
					}
					sql="from ProContractArfeeMaster where billNo='"+id.trim()+"' order by jnlNo";
					List proList=hs.createQuery(sql).list();
					if(proList!=null && proList.size()>0){
						for(int i=0;i<proList.size();i++){
							ProContractArfeeMaster pro=(ProContractArfeeMaster)proList.get(i);
							pro.setRecName(bd.getName_Sql("ReceivablesName", "recName", "recId", pro.getRecName()));
						}
					}
					sql="from ContractInvoiceManage where billNo='"+id.trim()+"' order by ARF_JnlNo";
					List invoiceList=hs.createQuery(sql).list();
					if(invoiceList!=null && invoiceList.size()>0){
						for(int i=0;i<invoiceList.size();i++){
							ContractInvoiceManage invoice=(ContractInvoiceManage)invoiceList.get(i);
							invoice.setInvoiceType(bd.getName("InvoiceType", "inTypeName", "inTypeId", invoice.getInvoiceType()));
						}
					}
					sql="from ContractParagraphManage where billNo='"+id.trim()+"' order by ARF_JnlNo";
					List paragraphList=hs.createQuery(sql).list();
					/**
					if(paragraphList!=null && paragraphList.size()>0){
						for(int i=0;i<paragraphList.size();i++){
							ContractParagraphManage paragraph=(ContractParagraphManage)paragraphList.get(i);
							
						}
					}
					*/
					request.setAttribute("contractBean", constractBean);
					request.setAttribute("proList", proList);
					request.setAttribute("invoiceList", invoiceList);
					request.setAttribute("paragraphList", paragraphList);
					
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("display.recordnotfounterror"));
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
		
	}


	/**
	 * 保存数据方法
	 * @param form
	 * @param request
	 * @return ActionErrors
	 *//*
	public void addOrUpdate(ActionForm form, HttpServletRequest request,ActionErrors errors) {
		
		DynaActionForm dform = (DynaActionForm) form;

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		ProContractArfeeMaster master = null;
		String id = (String) dform.get("id");
		String submitType=(String)dform.get("submitType");
		String jnlNo = null;

		Session hs = null;
		Transaction tx = null;
			
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			
			if (id != null && !id.equals("")) { // 修改		
				master = (ProContractArfeeMaster) hs.get(ProContractArfeeMaster.class, id);
				jnlNo = master.getJnlNo();
			} else { // 新增
				master = new ProContractArfeeMaster();
				
				String todayDate = CommonUtil.getToday();
				String year = todayDate.substring(2,4);
				jnlNo = CommonUtil.getBillno(year,"ProContractArfeeMaster", 1)[0];// 生成流水号
			}
			
			BeanUtils.populate(master, dform.getMap()); // 复制所有属性
			
			master.setJnlNo(jnlNo);// 流水号	
			master.setSubmitType(submitType);
			master.setAuditStatus("N");
			hs.save(master);
		
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
		
	}*/
		
}	