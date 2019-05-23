package com.gzunicorn.struts.action.maintenanceworkplanmanager;


import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
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
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.SysRightsUtil;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractdetail.MaintContractDetail;
import com.gzunicorn.hibernate.engcontractmanager.maintcontractmaster.MaintContractMaster;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplandetail.MaintenanceWorkPlanDetail;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplanmaster.MaintenanceWorkPlanMaster;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;
import com.zubarev.htmltable.DefaultHTMLTable;
import com.zubarev.htmltable.HTMLTableCache;
import com.zubarev.htmltable.action.ServeTableForm;


public class MaintenanceWorkPlanTurnAction extends DispatchAction {

	Log log = LogFactory.getLog(MaintenanceWorkPlanTurnAction.class);

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
				SysRightsUtil.NODE_ID_FORWARD + "maintenanceworkplanturn", null);
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

		    request.setAttribute("navigator.location", " 维保合同作业计划转派 >> 查询列表");		
			ActionForward forward = null;
			HttpSession session = request.getSession();
			ViewLoginUserInfo userInfo = (ViewLoginUserInfo) session
					.getAttribute(SysConfig.LOGIN_USER_INFO);
			DynaActionForm dform = (DynaActionForm) form;
			String action = dform.toString();
				
			String elevatorNo = (String) dform.get("elevatorNo");//电梯编号
			String maintContractNo = (String) dform.get("maintContractNo");//维保合同号
			String projectName = (String) dform.get("projectName");//项目名称
			String salesContractNo = (String) dform.get("salesContractNo");//销售合同号
			String sdate1=(String) dform.get("sdate1");//下达日期开始
			String edate1=(String) dform.get("edate1");//下达日期结束
			String maintPersonnel=(String) dform.get("maintPersonnel");// 原维保工
			String mainStation=(String) dform.get("mainStation");//所属维保站
			
			if((sdate1==null || sdate1.trim().equals("")) && (edate1==null || edate1.trim().equals(""))){
				String day=DateUtil.getNowTime("yyyy-MM-dd");//当前日期
				String day1=DateUtil.getDate(day, "MM", -1);//当前日期月份加1 。
				sdate1=day1;
				edate1=day;
				dform.set("sdate1", day1);
				dform.set("edate1", day);
			}
			
			List mslist= bd.getMaintStationList2(userInfo,"%");
			request.setAttribute("mainStationList",mslist);
				
				Session hs = null;
				try {
					hs = HibernateUtil.getSession();

	                String sql="exec sp_MaintenanceWorkPlanTurn '"+elevatorNo.trim()+"','"+maintContractNo.trim()+"',"
	                		+ "'"+projectName.trim()+"','"+salesContractNo.trim()+"','"+maintPersonnel.trim()+"',"
	                		+ "'"+mainStation.trim()+"','"+sdate1.trim()+"','"+edate1.trim()+"'";
	                
	                //System.out.println("维保合同作业计划转派>>>>"+sql);

					ResultSet rs=hs.connection().prepareCall(sql).executeQuery();
					List maintenanceTurnList = new ArrayList();

					while(rs.next()){
						HashMap map=new HashMap();
						
						map.put("maintContractNo", rs.getString("maintContractNo"));//维保合同号
						map.put("taskSubDate", rs.getString("taskSubDate"));//下达日期
						map.put("rowid", rs.getString("rowid"));
						map.put("salesContractNo", rs.getString("salesContractNo"));//销售合同号
						map.put("projectName", rs.getString("projectName"));//项目名称
						map.put("elevatorNo", rs.getString("elevatorNo"));//电梯编号
						map.put("mainSdate", rs.getString("mainSdate"));//维保开始时间
						map.put("shippedDate", rs.getString("shippedDate"));//维保计划开始时间
						map.put("mainEdate", rs.getString("mainEdate"));//维保结束时间
						map.put("mainStation", rs.getString("mainStation"));
						map.put("assignedMainStation", rs.getString("assignedMainStation"));//维保站
						map.put("maintPersonnel", rs.getString("maintPersonnel"));//原维保工
						map.put("zpdate", rs.getString("zpdate"));//作业转派日期
						map.put("zpoperid", rs.getString("zpoperid"));//转派人
						map.put("zpopername", rs.getString("zpopername"));//转派人
						
						maintenanceTurnList.add(map);
					}

					//request.setAttribute("nowdate", CommonUtil.getNowTime("yyyy-MM-dd"));
					request.setAttribute("maintenanceTurnList", maintenanceTurnList);
					request.setAttribute("maintenanceTurnListSize", maintenanceTurnList.size());

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
				forward = mapping.findForward("maintenanceTurnList");
			
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
	
	
	public static String getParentStorageID(String storageID)
	{
		Session hs = null;
		try {
			hs=HibernateUtil.getSession();
		    String sql="select case when ParentStorageID='0' then  StorageID else ParentStorageID end as StorageID from StorageId where StorageID='"+storageID+"'";
		    List rs=hs.createSQLQuery(sql).list();
		    if(rs!=null&&rs.size()>0)
		    {
		    	storageID=(String)rs.get(0);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
        	if(hs != null){
				hs.close();
			}
        }
		
		return storageID;
	}
	
	@SuppressWarnings("unchecked")
	public void getMaintPersonnel(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		String mainStation = request.getParameter("mainStation"); //维保站

		JSONArray jsonArr = new JSONArray();
		JSONObject json = null;
		if(mainStation != null && !mainStation.equals("")){

			Session hs = null;
			Query query = null;
			try {
				hs = HibernateUtil.getSession();
				
				String sql = "select lu.UserID,lu.UserName from Loginuser lu "
						+ "where lu.StorageID like '"+mainStation+"%' and lu.RoleID in('A50','A49') and enabledflag='Y'";
				List list =hs.createSQLQuery(sql).list();
				List MaintPersonnelList = new ArrayList();

				if(list!=null&&list.size()>0)
				{
					for(int j = 0;j<list.size();j++)
					{
						Object[] objects = (Object[])list.get(j);
						json = new JSONObject();
						json.put("id", String.valueOf(objects[0]));// 维保合同号
						json.put("name", String.valueOf(objects[1]));// 销售合同号
						jsonArr.put(json);	
					}						
				}

				ServletOutputStream stream = response.getOutputStream();
				stream.write(jsonArr.toString().replace("null", "").getBytes("UTF-8"));
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally { 
				if(hs != null){
					hs.close();
				}
			}
			

			
		}
    }

	/**
	 * 点击保存的方法
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward toSave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		ActionErrors errors = new ActionErrors();				
		ActionForward forward = null;
		DynaActionForm dform = (DynaActionForm) form;
		String[] isBoxs=request.getParameterValues("isBox");//确认选择复选框
		String[] rowids=request.getParameterValues("rowid");//选择记录
		String[] zpdates=request.getParameterValues("zpdate");//选择的转派日期
		String[] hzpdates=request.getParameterValues("hzpdate");//隐藏的转派日期
		String[] zpoperids=request.getParameterValues("zpoperid");//选择维保工`
		
		Session hs = null;
		Transaction tx = null;
		HttpSession session = request.getSession();
		ViewLoginUserInfo userInfo = (ViewLoginUserInfo)session.getAttribute(SysConfig.LOGIN_USER_INFO);
		try{
			hs = HibernateUtil.getSession();
			tx = hs.beginTransaction();
			if(isBoxs.length>0)
            {
            	for(int i=0;i<isBoxs.length;i++)
            	{
            		if(isBoxs[i].equals("Y"))
            		{
            			MaintContractDetail mcd=(MaintContractDetail) hs.get(MaintContractDetail.class,Integer.valueOf(rowids[i]));
            			String sql="from MaintenanceWorkPlanMaster m where m.rowid="+Integer.valueOf(rowids[i]);
            			List list=hs.createQuery(sql).list();
            			MaintenanceWorkPlanMaster m=null;
            			if(list!=null&&list.size()>0){
            				m=(MaintenanceWorkPlanMaster) list.get(0);
            			}
            			
            			m.setZpdate(zpdates[i]);
            			m.setZpoperdate(CommonUtil.getNowTime());
            			m.setZpoperid(userInfo.getUserID());
            			m.setMaintPersonnel(mcd.getMaintPersonnel());
            			hs.update(m);
            			
            			mcd.setMaintPersonnel(zpoperids[i]);
            			hs.update(mcd);
            			
            			/**
            			 * 保养计划转派时候，如果转派日期是2017.9.10，则该日期之后，离2017.9.10最近的一次计划，就往前挪到2017.9.10日做保养，
            			 * 之后的计划也都往前顺延。 且不管之前使用14天逻辑排的，还是按15天逻辑排的，转派后每次计划的间隔都是14天。
            			 * 
            			 * 比如应该是10-3去做保养，但是要转派的那个人需要10-9去做，那么10-3的保养覆盖不上，能不能覆盖掉10-3号的这次保养.
            			 */
            			String maintdate=zpdates[i];
            			String hmaintdate=hzpdates[i];

            			String hql="from MaintenanceWorkPlanDetail where billno='"+m.getBillno()+"' "
            					+ " and maintDate >= '"+hmaintdate+"'"
            					+ " and isnull(handleStatus,'')=''"//已经接收,已到场,已完工的，不能转派
            					+ " order by maintDate";
            			//System.out.println(hql);
            			List listup=hs.createQuery(hql).list();
            			if(listup!=null && listup.size()>0){
            				MaintenanceWorkPlanDetail mwpd=null;
            				
            				for(int u=0;u<listup.size();u++){
            					mwpd=(MaintenanceWorkPlanDetail)listup.get(u);

            					mwpd.setMaintPersonnel(zpoperids[i]);
            					mwpd.setMaintDate(maintdate);
            					mwpd.setWeek(DateUtil.getWeek(maintdate).substring(2, 3));
            					hs.update(mwpd);
            					
            					maintdate=DateUtil.getDate(maintdate, "dd", 14);//间隔14天
            				}
            			}
            			
            			/**
            			String hql="Update MaintenanceWorkPlanDetail set maintPersonnel='"+zpoperids[i]+"' "
            					+ " where billno='"+m.getBillno()+"' "
            					+ " and maintDate >= '"+zpdates[i]+"'"
            					+ " and isnull(handleStatus,'')=''";//已经接收,已到场,已完工的，不能转派
            			System.out.println(hql);
            			hs.connection().prepareStatement(hql).executeUpdate();
            			*/
            			
            			hs.flush();
            		}
            	}	
            }
			tx.commit();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("update.success"));
		} catch (Exception e1) {
			try {
				tx.rollback();
			} catch (HibernateException e3) {
				log.error(e3.getMessage());
				DebugUtil.print(e3, "Hibernate Transaction rollback error!");
			}
			e1.printStackTrace();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.string","保存失败！"));
		} finally {
			try {
				hs.close();
			} catch (HibernateException hex) {
				log.error(hex.getMessage());
				DebugUtil.print(hex, "Hibernate close error!");
			}
		}

		forward = mapping.findForward("returnList");

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		return forward;
	}




}