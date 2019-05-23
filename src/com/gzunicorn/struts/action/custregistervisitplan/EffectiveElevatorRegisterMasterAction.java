package com.gzunicorn.struts.action.custregistervisitplan;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.apache.struts.util.MessageResources;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.JSONUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.basedata.customer.Customer;
import com.gzunicorn.hibernate.custregistervisitplan.LostElevatorMaintain.LostElevatorMaintain;
import com.gzunicorn.hibernate.custregistervisitplan.LostElevatorMaintainDetail.LostElevatorMaintainDetail;
import com.gzunicorn.hibernate.custregistervisitplan.custreturnregisterdetail.CustReturnRegisterDetail;
import com.gzunicorn.hibernate.custregistervisitplan.custreturnregisterlssue.CustReturnRegisterLssue;
import com.gzunicorn.hibernate.custregistervisitplan.custreturnregistermaster.CustReturnRegisterMaster;
import com.gzunicorn.hibernate.custregistervisitplan.effectiveelevatormaintain.EffectiveElevatorMaintain;
import com.gzunicorn.hibernate.custregistervisitplan.effectiveelevatorregistermaster.EffectiveElevatorRegisterMaster;
import com.gzunicorn.hibernate.custregistervisitplan.lostelevatorregisterdetail.LostElevatorRegisterDetail;
import com.gzunicorn.hibernate.custregistervisitplan.lostelevatorregisterlssue.LostElevatorRegisterLssue;
import com.gzunicorn.hibernate.custregistervisitplan.lostelevatorregistermaster.LostElevatorRegisterMaster;
import com.gzunicorn.hibernate.custregistervisitplan.returningmaintain.ReturningMaintain;
import com.gzunicorn.hibernate.custregistervisitplan.returningmaintaindetail.ReturningMaintainDetail;
import com.gzunicorn.hibernate.engcontractmanager.lostelevatorreport.LostElevatorReport;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;


public class EffectiveElevatorRegisterMasterAction extends DispatchAction {

	Log log = LogFactory.getLog(EffectiveElevatorRegisterMasterAction.class);

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
		SysRightsUtil.filterModuleRight(request, response,
				SysRightsUtil.NODE_ID_FORWARD + "effectiveelevatorregistermaster", null);
		/** **********结束用户权限过滤*********** */

		// Set default method is toSearchRecord
		String name = request.getParameter("method");
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

		request.setAttribute("navigator.location", "有价值客户回访管理  >> 查询列表");
		ActionForward forward = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		DynaActionForm dform = (DynaActionForm) form;
		String action = dform.toString();

		if (dform.get("genReport") != null
				&& !dform.get("genReport").equals("")) {
			try {

				response = toExcelRecord(form, request, response);
				forward = mapping.findForward("exportExcel");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			
			String companyName = (String) dform.get("scompanyName");
			String contacts = (String) dform.get("contacts2");
			//String isProblem = (String) dform.get("isProblem");
			String isHandle = (String) dform.get("handleId");
			//String maintDivision = (String) dform.get("maintDivision");
			String contactPhone=(String)dform.get("contactPhone2");
			
			/*if(!userInfo.getComID().equals("00")){
				if(maintDivision == null || maintDivision.trim().equals("")){
					maintDivision = userInfo.getComID();
				}
				}*/
			
			Session hs = null;
			Query query = null;
			List custReturnRegisterList = new ArrayList();
			try {
				hs = HibernateUtil.getSession();
				String sql = "select * from (select c1.billno1,c1.ReOrder,c1.ReMark,c1.CompanyName,c1.Contacts,"
						+ "c1.ContactPhone,c2.OperDate,c2.handleID,c2.handleResult,c2.ReturnResult,"
						+ "c2.billno,c2.MinisterHandle,isnull(c2.Rem,'') rem1 "
						+ "from(select rm.companyName,rm.contacts,rm.contactPhone,rm.reOrder,rm.reMark,rm.billno billno1 "
						+ "from EffectiveElevatorMaintain rm where rm.enabledFlag='Y') as c1 "
						+ "left join (select * from (select row_number() over(partition by crrm2.companyName order by crrm2.operDate desc) as rownum,crrm2.* "
						+ "from EffectiveElevatorRegisterMaster crrm2) as T where T.rownum = 1) as c2 on c1.companyName=c2.companyName) as c3 "
						+ "where 1=1 ";				
				if (companyName != null && !companyName.equals("")) {
					sql += " and c3.companyName like '%" + companyName.trim()+ "%'";
				}
				if (contacts != null && !contacts.equals("")) {
					sql += " and c3.contacts like '%" + contacts.trim() + "%'";
				}
				if (contactPhone != null && !contactPhone.equals("")) {
					sql += " and c3.contactPhone like '%" + contactPhone.trim() + "%'";
				}
				if (isHandle != null && !isHandle.equals("")) {
					if (isHandle.equals("Y")) {
						sql += " and c3.handleId not like '' and c3.handleId is not  null";
					}
					if (isHandle.equals("N")) {
						sql += " and isnull(c3.handleId,'')=''";
					}
				}
				/*if (isProblem != null && !isProblem.equals("")) {
					sql += " and c3.isProblem like '%" + isProblem + "%'";
				}*/
				/*if (maintDivision != null && !maintDivision.equals("")) {
					sql += " and isnull(c3.maintDivision,'') like '%" + maintDivision
							+ "%'";
				}*/

				String order = " order by c3.ReOrder";
				
				//System.out.println(sql);
//				hs.connection().prepareStatement(sql).executeQuery()
				List list = hs.createSQLQuery(sql + order).list();

				if (list != null && list.size() > 0) {
					int len = list.size();
					for (int i = 0; i < len; i++) {
						Object[] object = (Object[]) list.get(i);
						HashMap map = new HashMap();
						
						map.put("billno1", object[0]);
						map.put("reOrder", (Integer) object[1]);
						map.put("reMark", (Character) object[2]);// 是否回访
						map.put("companyName", (String) object[3]);// 回访单位
						map.put("contacts", (String) object[4]);// 回访联系人
						map.put("contactPhone", (String) object[5]);// 回访联系电话
						map.put("operDate", (String) object[6]);// 上次回访日期
						map.put("rem", object[12]);
						String date = (String) object[6];
						if (date != null) {
							String handleId = (String) object[7];
							if (handleId != null && !handleId.equals("")) {
								map.put("handleId", "是");// 是否处理
							} else {
								map.put("handleId", "否");// 是否处理
							}
							map.put("handleResult", object[8]);// 处理结果

							String returnResult = (String) object[9];
							if (returnResult != null
									&& !returnResult.equals("")) {
								if(returnResult.equals("Y"))
								{
									map.put("returnResult", "是");// 回访结果
								}else
								{
									map.put("returnResult", "否");// 回访结果
								}
							} else {
								map.put("returnResult", null);
							}
							map.put("billno", (String) object[10]);
							char cr = (Character) object[11];
							if (cr == 'Y') {
								map.put("ministerHandle", "是");
							} else {
								map.put("ministerHandle", "否");
							}

						} else {
							map.put("isProblem", null);
							map.put("handleId", null);
							map.put("handleResult", null);
							map.put("returnResult", null);
							map.put("billno", null);
							map.put("maintDivision", null);
							map.put("ministerHandle", null);
						}
						if((i+1)%2==0)
						{
							map.put("style", "oddRow3");
						}
						else
						{
							map.put("style", "evenRow3");
						}
						
						custReturnRegisterList.add(map);
					}
				}
				
				

                sql="select ComId,ComFullName from Company where enabledflag='Y' order by ComId";
				List maintList= hs.createSQLQuery(sql).list();
				
                
                
                request.setAttribute("custReturnRegisterListSize", custReturnRegisterList.size());
				//request.setAttribute("maintDivisionList",Grcnamelist1.getgrcnamelist(userInfo.getUserID()));
				request.setAttribute("custReturnRegisterList",custReturnRegisterList);

			} catch (DataStoreException e) {
				e.printStackTrace();
			} catch (HibernateException e1) {

				e1.printStackTrace();
			} /*catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/ finally {
				try {
					hs.close();
					// HibernateSessionFactory.closeSession();
				} catch (HibernateException hex) {
					log.error(hex.getMessage());
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			forward = mapping.findForward("effectiveElevatorRegisterMasterList");
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
	 * 点击查看历史方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toHistoryDisplayRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		request.setAttribute("navigator.location", "有价值客户回访管理 >> 历史查看");

		ActionForward forward = null;

		String id = request.getParameter("id");
		Session hs = null;
		if (id != null) {
			try {
				//System.out.println(">>>"+id);
				//使用URL解码
				id=URLDecoder.decode(id,"UTF-8");
				//System.out.println(">>>==="+id);
				
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from EffectiveElevatorRegisterMaster crrm where crrm.companyName = :companyName order by crrm.operDate desc");
				query.setString("companyName", id);
				List list = query.list();
				List<EffectiveElevatorRegisterMaster> crrmList = new ArrayList<EffectiveElevatorRegisterMaster>();

				if (list != null && list.size() > 0) {
					int len = list.size();
					for (int i = 0; i < len; i++) {
						EffectiveElevatorRegisterMaster crrm = (EffectiveElevatorRegisterMaster) list.get(i);
						String ishandle = crrm.getHandleId();
						if (ishandle != null && !"".equals(ishandle)) {
							crrm.setHandleId("Y");// 是否处理
						} else {
							crrm.setHandleId("N");
						}
						String returnResult = crrm.getReturnResult();
						if(returnResult==null || returnResult.equals(""))
						{
							crrm.setReturnResult("N");
						}
						if((i+1)%2==0)
						{
							crrm.setR2("oddRow3");
							
						}
						else
						{
							crrm.setR2("evenRow3");
						}
						
						crrmList.add(crrm);

					}
				}
				 request.setAttribute("custReturnRegisterHistoryListSize", list.size());
				 request.setAttribute("custReturnRegisterHistoryList", crrmList);

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
			forward = mapping.findForward("effectiveElevatorRegisterMasterHistoryDisplay");
		}
		return forward;
	}


	
	/**
	 * 点击查看方法
	 * 
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
		request.setAttribute("navigator.location", "有价值客户回访管理 >> 查看");

		ActionForward forward = null;
		String id = (String) dform.get("id");
		String isreturn = (String) dform.get("isreturn");
		Session hs = null;
		EffectiveElevatorRegisterMaster master = null;
		List<LostElevatorRegisterDetail> detailList = null;
		List detailsList=new ArrayList();
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				Query query = hs.createQuery("from EffectiveElevatorRegisterMaster crrm where crrm.billno = :billno");
				query.setString("billno", id);
				List list = query.list();

				if (list != null && list.size() > 0) {
					master=(EffectiveElevatorRegisterMaster) list.get(0);
					if (isreturn != null) {
						master.setR4(isreturn);
					}
					
					if(master.getHandleId()!=null && !"".equals(master.getHandleId())){
						master.setR2(bd.getName("Loginuser", "username", "userid", master.getHandleId()));
					}
					if(master.getOperId()!=null && !"".equals(master.getOperId())){
						master.setR3(bd.getName("Loginuser", "username", "userid", master.getOperId()));
					}
				} else {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"display.recordnotfounterror"));
				}

				if (master == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"display.recordnotfounterror"));
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
			request.setAttribute("custReturnRegisterMasterBean",
					master);
			//request.setAttribute("custReturnRegisterDetailList",detailsList);
			// CustReturnRegisterLssues
			forward = mapping.findForward("effectiveElevatorRegisterMasterDisplay");

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	/**
	 * 跳转到开始回访页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */

	@SuppressWarnings("unchecked")
	public ActionForward toPrepareAddRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		DynaActionForm dform = (DynaActionForm) form;

		ActionErrors errors = new ActionErrors();
		request.setAttribute("navigator.location", "有价值客户回访管理 >> 开始回访");

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);

		ActionForward forward = null;

		String id = (String) dform.get("id");
//		if (id == null) {
//			id = (String) request.getAttribute("id");
//		}
 
		List custReturnRegisterList = new ArrayList();

		Session hs = null;
		//List custReturnRegisterLssueLssueSort = null;
		EffectiveElevatorMaintain maintain=null;
		//LostElevatorReport contract=null;

		if (id != null) {
			////System.out.println(id);
			if (request.getAttribute("error") == null
					|| request.getAttribute("error").equals("")) {
				try {
					 hs = HibernateUtil.getSession();
					 maintain=(EffectiveElevatorMaintain) hs.get(EffectiveElevatorMaintain.class, id.trim());
					 if (maintain != null) {

						HashMap map = new HashMap();
						map.put("username", userInfo.getUserName());
						map.put("operId", userInfo.getUserID());
						map.put("operDate",DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
						
						map.put("companyName", maintain.getCompanyName());
						map.put("contacts", maintain.getContacts());
						map.put("contactPhone", maintain.getContactPhone());
						map.put("companyId",maintain.getCompanyId());
						
						custReturnRegisterList.add(map);
					}else{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"display.recordnotfounterror"));
					}
					 if (maintain == null) {
						 errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								 "display.recordnotfounterror"));
					 }
				} catch (DataStoreException e) {
					e.printStackTrace();
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
			}

			request.setAttribute("custReturnRegisterList2",custReturnRegisterList);
			//request.setAttribute("custReturnRegisterLssueLssueSort",custReturnRegisterLssueLssueSort);
			//request.setAttribute("contract", contract);
			forward = mapping.findForward("effectiveElevatorRegisterMasterAdd");

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
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

	public ActionForward toAddRecord(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		Session hs = null;
		Transaction tx = null;

		/*String companyId = (String) dform.get("companyId");
		String comId =request.getParameter("comId");
		dform.set("id",companyId);
		String ministerHandle = (String) dform.get("ministerHandle");
		String rem = (String) dform.get("rem");*/
		
		/*String[] wbBillno = request.getParameterValues("wbBillno");
		String[] maintContractNo=request.getParameterValues("maintContractNo");
		String[] lostElevatorDate=request.getParameterValues("lostElevatorDate");
		String[] causeAnalysis=request.getParameterValues("causeAnalysis");
		String[] dtJnlno = request.getParameterValues("dtJnlno");
		String[] lssueDetail = request.getParameterValues("lssueDetail");*/
		/*//String isProblem = "";
		String companyName = "";
		String contacts = "";
		String contactPhone = "";
		int billNoIntger = billNo.length / 3;*/
		/*for (int i = 0; i < lssueDetail.length; i++) {
			isProblem += lssueDetail[i];
		}
		if (isProblem.trim().equals("")) {
			isProblem = "N";
		} else {
			isProblem = "Y";
		}*/
		EffectiveElevatorRegisterMaster master=new EffectiveElevatorRegisterMaster();
		//LostElevatorRegisterDetail detail=null;
		//LostElevatorRegisterLssue lssue=null;
		try {
			hs = HibernateUtil.getSession();
			tx=hs.beginTransaction();
			
			String todayDate = CommonUtil.getToday();
			String year = todayDate.substring(2,4);
			String billno = CommonUtil.getBillno(year,"EffectiveElevatorRegisterMaster", 1)[0];// 生成流水号
			
			BeanUtils.populate(master, dform.getMap());
			master.setBillno(billno);
			hs.save(master);
			/*if(wbBillno!=null && wbBillno.length>0){
				for(int i=0;i<wbBillno.length;i++){
					String jnlno=CommonUtil.getBillno(year, "LostElevatorRegisterDetail", 1)[0];
					detail=new LostElevatorRegisterDetail();
					detail.setJnlno(jnlno);
					detail.setBillno(billno);
					detail.setDtJnlno(dtJnlno[i]);
					detail.setWbBillno(wbBillno[i]);
					detail.setMaintContractNo(maintContractNo[i]);
					detail.setLostElevatorDate(lostElevatorDate[i]);
					detail.setCauseAnalysis(causeAnalysis[i]);
					hs.save(detail);
					lssue=new LostElevatorRegisterLssue();
					lssue.setJnlno(jnlno);
					lssue.setLssueDetail(lssueDetail[i]);
					hs.save(lssue);
				}
			}*/
			
			
			 
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(					
					"custreturnregistercheck.insert.duplicatekeyerror"));
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			log.error(e2.getMessage());
			DebugUtil.print(e2, "Hibernate region Insert error!");
		} catch (Exception e1) {
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

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"custreturnregister.insert.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return addnew page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"custreturnregister.insert.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				
				forward = mapping.findForward("returnAdd");
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
	 * 跳转到回访反馈页面
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward toPrepareUpdateRecord(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		request.setAttribute("navigator.location","有价值客户回访管理 >> 回访反馈");
		DynaActionForm dform = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();

		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
				.getAttribute(SysConfig.LOGIN_USER_INFO);
		
		ActionForward forward = null;
		String id = null;

		if (dform.get("isreturn") != null
				&& ((String) dform.get("isreturn")).equals("N")) {
			id = (String) dform.get("billno");
		} else{
			id = (String) dform.get("id");
		}
		
		Session hs = null;
		EffectiveElevatorRegisterMaster master = null;
		List<LostElevatorRegisterDetail> detailList = null;
		List detailsList=new ArrayList();
		if (id != null) {
			try {
				hs = HibernateUtil.getSession();
				master=(EffectiveElevatorRegisterMaster) hs.get(EffectiveElevatorRegisterMaster.class, id.trim());
				
				String hql2 = "from Customer c where c.companyId ='"
						+ master.getCompanyId().trim()
						+ "'";
				List list1 = hs.createQuery(hql2).list();

				if (list1 != null && list1.size() > 0) {
					Customer customer = (Customer) list1.get(0);
					master.setR1(customer
							.getCompanyName());
				}
				
				//master.setR1(bd.getName("Customer", "companyName", "companyId", master.getCompanyId()));
				
				master.setR2(bd.getName("Loginuser", "username", "userid", master.getHandleId()));
				master.setR3(bd.getName("Loginuser", "username", "userid", master.getOperId()));
				//master.setMaintDivision(bd.getName(hs, "Company", "ComFullName", "ComId", master.getMaintDivision()));
				
				if(master.getReturnResult()==null || "".equals(master.getReturnResult()))
					master.setReturnResult("Y");
				
				/*detailList=hs.createQuery("from LostElevatorRegisterDetail where billno='"+master.getBillno()+"'").list();
				String[] type={"a.id.typeflag='LostElevatorReport_CauseAnalysis'"};
				if(detailList!=null && detailList.size()>0){
					for(LostElevatorRegisterDetail details : detailList){
						HashMap map=new HashMap();
						map.put("maintContractNo", details.getMaintContractNo());
						map.put("lostElevatorDate",details.getLostElevatorDate());
						map.put("causeAnalysis", bd.getName("Pulldown", "pullname", "id.pullid", details.getCauseAnalysis(), type));
						map.put("dtJnlno", details.getDtJnlno());
						List lssueList=hs.createQuery("from LostElevatorRegisterLssue where jnlno='"+details.getJnlno()+"'").list();
						map.put("custReturnRegisterLssues",lssueList);
						detailsList.add(map);
					}
				}*/
				

				if (master == null) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
							"display.recordnotfounterror"));
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
			
			request.setAttribute("custReturnRegisterMasterBean",
					master);
			//request.setAttribute("custReturnRegisterDetailList",detailsList);
			request.setAttribute("contract", "Y");
			request.setAttribute("isfeedback", "Y");
			forward = mapping.findForward("effectiveElevatorRegisterMasterModify");
}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}

	
	/**
	 * 回访反馈
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
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		
		String billno = (String) dform.get("billno");
		dform.set("billno", billno);
		String returnResult = (String) dform.get("returnResult");
		String returnRem =request.getParameter("returnRem");
		EffectiveElevatorRegisterMaster master=null;
		try {
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if(billno!= null&& !billno.trim().equals(""))
				master = (EffectiveElevatorRegisterMaster) hs.get(EffectiveElevatorRegisterMaster.class, billno);
			if (returnResult!= null&& !returnResult.trim().equals("")) {
				master.setReturnResult(returnResult);
				master.setReturnRem(returnRem);
			}
			tx.commit();
		} catch (HibernateException e2) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("custreturnregistercheck.update.duplicatekeyerror"));
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

		ActionForward forward = null;
		String isreturn = request.getParameter("isreturn");
		try {
			if (isreturn != null && isreturn.equals("Y") && errors.isEmpty()) {
				// return list page
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("custreturnregister.update.success"));
				forward = mapping.findForward("returnList");
			} else {
				// return modify page
				if (errors.isEmpty()) {
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("custreturnregister.update.success"));
				} else {
					request.setAttribute("error", "Yes");
				}
				
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

		String naigation = new String();
		ActionForward forward = null;
		HttpSession session = request.getSession();
		DynaActionForm dform = (DynaActionForm) form;
		XSSFWorkbook wb = new XSSFWorkbook();
		List roleList = new ArrayList();

		String companyName = (String) dform.get("companyName");
		String contacts = (String) dform.get("contacts");
		//String isProblem = (String) dform.get("isProblem");
		String isHandle = (String) dform.get("handleId");
		//String maintDivision = (String) dform.get("maintDivision");
		String contactPhone=(String)dform.get("contactPhone");
		/*maintDivision = bd.getName("Company", "comname", "comid", maintDivision);*/

		Session hs = null;
		Query query = null;
		List custReturnRegisterList = new ArrayList();
		try {

			hs = HibernateUtil.getSession();
			String sql = "select * from"
					+ "(select c1.ReOrder,c1.ReMark,c1.CompanyName,c1.Contacts,c1.ContactPhone,crrm.OperDate,crrm.handleID,crrm.handleResult,crrm.ReturnResult,c1.CompanyID,crrm.billno,crrm.MinisterHandle,isnull(crrm.Rem,'') rem1 "
					+ "from(select c.companyName,c.contacts,c.contactPhone,rm.companyId,rm.reOrder,rm.reMark from EffectiveElevatorMaintain rm ,Customer c where rm.companyId=c.companyId ) as c1 left join EffectiveElevatorRegisterMaster crrm "
					+ "on c1.CompanyID=crrm.CompanyID and crrm.companyId+crrm.operDate in (select crrm2.companyId+MAX(crrm2.OperDate) "
					+ "from EffectiveElevatorRegisterMaster crrm2 group by crrm2.companyId)) as c2 where 1=1";
			if (companyName != null && !companyName.equals("")) {
				sql += " and c2.companyName like '%" + companyName.trim()
						+ "%'";
			}
			if (contacts != null && !contacts.equals("")) {
				sql += " and c2.contacts like '%" + contacts.trim() + "%'";
			}
			if(contactPhone!=null && !contactPhone.equals("")){
				sql+=" and c2.contactPhone like '%"+contactPhone.trim()+"%'";
			}
			if (isHandle != null && !isHandle.equals("")) {
				if (isHandle.equals("Y")) {
					sql += " and handleId not like '' and handleId is not  null";

				}
				if (isHandle.equals("N")) {
					sql += " and isnull(handleId,'')=''";

				}
			}
			/*if (isProblem != null && !isProblem.equals("")) {
				sql += " and c2.isProblem like '%" + isProblem + "%'";
			}*/
			/*if (maintDivision != null && !maintDivision.equals("")) {
				sql += " and c2.maintDivision like '" + maintDivision + "'";
			}*/

			String order = " order by c2.ReOrder";
			List list = hs.createSQLQuery(sql + order).list();

			if (list != null && list.size() > 0) {
				int len = list.size();
				for (int i = 0; i < len; i++) {

					Object[] object = (Object[]) list.get(i);
					HashMap map = new HashMap();
					map.put("reOrder", (Integer) object[0]);
					map.put("reMark", (Character) object[1]);// 是否回访
					map.put("companyName", (String) object[2]);// 回访单位
					map.put("contacts", (String) object[3]);// 回访联系人
					map.put("contactPhone", (String) object[4]);// 回访联系电话
					map.put("operDate", (String) object[5]);// 上次回访日期
					map.put("companyId", (String) object[9]);
					/*Clob clob = (Clob)(object[13]);
					String str=clob.getSubString(1, (int)clob.length());*/
					map.put("rem", object[12]);
					String date = (String) object[5];
					if (date != null) {
						/*char cr = (Character) object[6];
						if (cr == 'Y')// 是否有问题
						{
							map.put("isProblem", "是");
						} else {
							map.put("isProblem", "否");
						}*/
						String handleId = (String) object[6];
						if (handleId != null && !handleId.equals("")) {
							map.put("handleId", "是");// 是否处理
						} else {
							map.put("handleId", "否");// 是否处理
						}
						Object hr = object[7];
						if (hr != null && !hr.equals("")) {
							map.put("handleResult", "是");// 是否解决
						} else {
							map.put("handleResult", "否");
						}
						String returnResult = (String) object[8];
						if (returnResult != null && !returnResult.equals("")) {
							map.put("returnResult", "是");// 回访结果
						} else {
							map.put("returnResult", "否");
						}
						map.put("billno", (String) object[10]);
						//map.put("maintDivision", (String) object[11]);
						char cr = (Character) object[11];
						if (cr == 'Y') {
							map.put("ministerHandle", "是");
						} else {
							map.put("ministerHandle", "否");
						}

					} else {
						map.put("isProblem", null);
						map.put("handleId", null);
						map.put("handleResult", null);
						map.put("returnResult", null);
						map.put("billno", null);
						map.put("maintDivision", null);
						map.put("ministerHandle", null);
					}

					custReturnRegisterList.add(map);
				}
			}

			roleList = custReturnRegisterList;

			XSSFSheet sheet = wb.createSheet("有价值客户回访管理");

			Locale locale = this.getLocale(request);
			MessageResources messages = getResources(request);

			if (roleList != null && !roleList.isEmpty()) {
				int l = roleList.size();
				XSSFRow row0 = sheet.createRow( 0);
				XSSFCell cell0 = row0.createCell((short) 0);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.reOrder"));

				cell0 = row0.createCell((short) 1);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.reMark"));

				cell0 = row0.createCell((short) 2);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.companyName"));

				cell0 = row0.createCell((short) 3);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.contacts"));

				cell0 = row0.createCell((short) 4);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.contactPhone"));

				cell0 = row0.createCell((short) 5);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.maxoperDate"));

				/*cell0 = row0.createCell((short) 6);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.isProblem"));

				cell0 = row0.createCell((short) 6);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.isHandleResult"));*/

				cell0 = row0.createCell((short) 6);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.isHandle"));

				cell0 = row0.createCell((short) 7);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.ministerHandle"));

				/*cell0 = row0.createCell((short) 8);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.maintDivision"));*/
				
				cell0 = row0.createCell((short) 8);
				// cell0.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell0.setCellValue(messages.getMessage(locale,
						"custreturnregister.rem"));

				for (int i = 0; i < l; i++) {
					HashMap map = (HashMap) roleList.get(i);
					// 创建Excel行，从0行开始
					XSSFRow row = sheet.createRow( i + 1);

					// 创建Excel列
					XSSFCell cell = row.createCell((short) 0);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((Integer) map.get("reOrder"));

					cell = row.createCell((short) 1);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					char cr = (Character) map.get("reMark");
					String str = cr + "";
					cell.setCellValue(CommonUtil.tranEnabledFlag(str.trim()));

					cell = row.createCell((short) 2);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("companyName"));

					cell = row.createCell((short) 3);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("contacts"));

					cell = row.createCell((short) 4);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("contactPhone"));

					cell = row.createCell((short) 5);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("operDate"));
					////System.out.println( map.get("operDate"));

					/*cell = row.createCell((short) 6);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("isProblem"));

					cell = row.createCell((short) 7);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("returnResult"));*/

					cell = row.createCell((short) 6);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("handleId"));
					////System.out.println( map.get("handleId"));

					cell = row.createCell((short) 7);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("ministerHandle"));
					////System.out.println( map.get("ministerHandle"));

					/*cell = row.createCell((short) 8);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(bd.getName(hs, "Company", "comfullname", "comid", (String) map.get("maintDivision")));*/
					
					cell = row.createCell((short) 8);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue((String) map.get("rem"));
					////System.out.println( map.get("rem"));

				}
			}

		} catch (DataStoreException e) {
			e.printStackTrace();
		} catch (HibernateException e1) {
			e1.printStackTrace();
		}/* catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/ finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
			}
		}

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "offline; filename="
				+ URLEncoder.encode("有价值客户回访管理", "utf-8") + ".xlsx");
		wb.write(response.getOutputStream());
		return response;
	}

	/**
	 * toGetCustReturnRegisterDetail, toList
	 * 
	 * @param billno
	 * @param Session
	 * @return toList
	 * @throws IOException
	 */
	/*public List toGetCustReturnRegisterDetail(String billno, Session hs) {
		List list = new ArrayList();
		String sql = "select distinct mcd.projectName,mcm.maintContractNo,mcd.salesContractNo "
				+ "from MaintContractMaster mcm,MaintContractDetail mcd,CustReturnRegisterDetail crrd "
				+ "where mcm.billNo=mcd.billNo and crrd.wb_Billno=mcm.billNo and crrd.billno = '"
				+ billno.trim()+"'";
		String sql1 = "select distinct crrd.jnlno "
				+ "from CustReturnRegisterDetail crrd "
				+ "where crrd.billno = '"
				+ billno.trim()+"'";
		List list1 = hs.createSQLQuery(sql).list();
		List list2 = hs.createSQLQuery(sql1).list();
		String sql="from CustReturnRegisterDetail where billno='"+billno.trim()+"'";
		List list1=hs.createQuery(sql).list();
//		if (list1 != null && list1.size() > 0 && list2 != null && list2.size() > 0 && list2.size()==list1.size()) {
		if(list1!=null && list1.size()>0){
			int leg = list1.size();
			for (int i = 0; i < leg; i++) {
				CustReturnRegisterDetail detail = (CustReturnRegisterDetail) list1.get(i);

				HashMap hmap = new HashMap();
				hmap.put("contractSdate", detail.getContractSdate());
				hmap.put("maintContractNo", detail.getMaintContractNo());
				hmap.put("contractEdate", detail.getContractEdate());
				hmap.put("jnlno", detail.getJnlno());
				String sql2 = "from CustReturnRegisterLssue where jnlno= '"
						+ detail.getJnlno() + "'";
				List custReturnRegisterLssueList = hs.createQuery(sql2).list();
				String[] condition={"a.id.typeflag='CustReturnRegisterMaster'"};
				if(custReturnRegisterLssueList!=null && custReturnRegisterLssueList.size()>0){
					for(Object object : custReturnRegisterLssueList){
						CustReturnRegisterLssue lssue=(CustReturnRegisterLssue) object;
						lssue.setLssueSort(bd.getName("Pulldown", "pullname", "id.pullid", lssue.getLssueSort(), condition));
					}
				}
				hmap.put("custReturnRegisterLssues",
						custReturnRegisterLssueList);
				list.add(hmap);
			}
		}
		return list;
	}*/

}
