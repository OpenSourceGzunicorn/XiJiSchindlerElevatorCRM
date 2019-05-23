package com.mobileserver;

/**
 * 手机APP端调用，登录用户处理类
 * @author Crunchify
 */

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zefer.html.doc.p;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.hibernate.sysmanager.Loginuser;

@Path("/Pending")
public class Pending {

	/**
	 * 用于主页显示每日任务
	 * @param userid
	 * @return
	 * @throws JSONException
	 */
	@POST
	@Path("{userid}")
	@Produces("application/json")
	public Response _UserInfo(@PathParam("userid") String userid)
			throws JSONException {
		JSONObject rejson = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		Session session = null;
		Transaction tx = null;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			String hql="select COUNT(mwpd.numno) from MaintenanceWorkPlanDetail mwpd "
					+ "where mwpd.MaintPersonnel='"+userid+"' and mwpd.MaintDate='"+CommonUtil.getNowTime("yyyy-MM-dd")+"' and mwpd.MaintEndTime is null";
			String sql="select COUNT(cp.CalloutMasterNo) from CalloutProcess cp,CalloutMaster cm "
					+ "where  cp.calloutMasterNo=cm.calloutMasterNo and cp.assignObject2='"+userid.trim()+"' and cm.repairTime like '"+CommonUtil.getNowTime("yyyy-MM-dd")+"%' "
							+ "and cm.handleStatus not in ('5','6','7')";
			List list=session.createSQLQuery(hql).list();
			List list2=session.createSQLQuery(sql).list();
			if(list!=null&&list.size()>0){
				String bymissioncount=list.get(0).toString();
				jsonObject.put("right", bymissioncount);
			}else{
				jsonObject.put("right", 0);
			}
			if(list2!=null&&list2.size()>0){
				String jixiucount=list2.get(0).toString();
				jsonObject.put("left", jixiucount);
			}else
			{   
				jsonObject.put("left", 0);
			}
	        jobiArray.put(0, jsonObject);

	        json.put("code", "400");
			json.put("info", "非法用户登录!");
			
			rejson.put("status", json);
			rejson.put("data", jobiArray);
		}catch(Exception ex){
			tx.rollback();
			ex.printStackTrace();
		} finally {
            try {
            	if(session!=null){
            		session.close();
            	}
            } catch (HibernateException hex) {
                DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
            }
        }
		
		return Response.status(200).entity(rejson.toString()).build();
	}

	
	/**
	 * 用于配件申请，技术支持--待办数量
	 * @param userid
	 * @return
	 * @throws JSONException
	 */
	@POST
	@Path("/htdatasta/{userid}")
	@Produces("application/json")
	public Response _Htdatasta(@PathParam("userid") String userid)
			throws JSONException {
		
		Session session = null;
		Transaction tx = null;
		Loginuser user=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
        
		System.out.println(">>>>合同交接资料上传、配件申请，技术支持--待办数量");
		try{

			session = HibernateUtil.getSession();

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("pjnum", "");
            jsonObject.put("jsnum", "");
			
            List userList=session.createQuery("from Loginuser where userid ='"+userid+"'").list();
			if(userList!=null&&userList.size()>0){
				user=(Loginuser) userList.get(0);
			}
			String roleid=user.getRoleid();
			
			/**==================需要在此显示合同交接资料上传（未提交，驳回）待办数量==================*/
			if(roleid.trim().equals("A03")){
				//A03  维保经理,未提交的，未转派的。
				String sqlk="from ContractTransferMaster a "
						+ "where a.submitType='Y' "
						+ "and a.transfeSubmitType in('N','R') "
						+ "and isnull(a.isTrans,'N')='N' "
						+ "and a.maintStation like '"+user.getStorageid().trim()+"'";
				//System.out.println("合同交接资料上传（未提交，驳回）待办数量>>>"+sqlk);
				Query query = session.createQuery(sqlk);
	            ArrayList cList = (ArrayList) query.list();
	            if (cList != null && cList.size()>0 ) {
	    			jsonObject.put("ctfnum", cList.size());
			    }
	            
	            //A03  维保经理,未审核的。
				sqlk="from ContractTransferMaster a "
						+ "where a.submitType='Y' "
						+ "and a.transfeSubmitType in('Y') "
						+ "and isnull(a.isTrans,'N')='Y' "
						+ "and isnull(a.auditStatus2,'N')='N' "
						+ "and a.maintStation like '"+user.getStorageid().trim()+"'";
				//System.out.println("合同交接资料上传审核 待办数量>>>"+sqlk);
				query = session.createQuery(sqlk);
	            cList = (ArrayList) query.list();
	            if (cList != null && cList.size()>0 ) {
	    			jsonObject.put("ctfanum", cList.size());
			    }
	            
			}else if(roleid.trim().equals("A50")){
				//A50  维保工  ，维保经理转派给自己的
				String sqlk="from ContractTransferMaster a "
						+ "where a.submitType='Y' "
						+ "and a.transfeSubmitType in('N','R') "
						+ "and isnull(a.isTrans,'N')='Y' "
						+ "and isnull(a.wbgTransfeId,'')='"+userid.trim()+"'";
				//System.out.println("合同交接资料上传（未提交，驳回）待办数量>>>"+sqlk);
				Query query = session.createQuery(sqlk);
	            ArrayList cList = (ArrayList) query.list();
	            if (cList != null && cList.size()>0 ) {
	    			jsonObject.put("ctfnum", cList.size());
			    }
			}
			
			/**==================需要在此显示配件申请待办数量==================*/
			String sql="from AccessoriesRequisition a where 1=1 ";
			//维保站长A49，维保工A50,维保经理 A03，维修技术员 A53，
			if(roleid.trim().equals("A50") || roleid.trim().equals("A49") || roleid.trim().equals("A53")){
				//A50  维保工  
				sql+=" and a.operId ='"+user.getUserid().trim()+"' and a.handleStatus in('3','6') ";
			}else if(roleid.trim().equals("A03")){
				//A03  维保经理
				sql+=" and ((a.maintStation like '"+user.getStorageid().trim()+"%' and a.handleStatus='1')"
						+ " or (a.operId ='"+user.getUserid().trim()+"' and a.handleStatus in('3','6'))) ";
			}else{
				//没有提示数量
				sql+=" and a.maintStation='' ";
			}
			//System.out.println("配件申请待办数量>>>"+sql);
			Query query = session.createQuery(sql);
            ArrayList tList = (ArrayList) query.list();
            if (tList != null && tList.size()>0 ) {
            	jsonObject.put("pjnum", tList.size());
		    }
            
            /**==================需要在此显示技术支持申请待办数量==================*/
            sql="from TechnologySupport t where 1=1 ";
            if(roleid.trim().equals("A49")){
            	//维保站长
				sql+=" and t.proStatus ='1' and t.maintStation like '"+user.getStorageid().trim()+"' ";
			}else if(roleid.trim().equals("A03")){
				//维保经理
				sql+=" and t.proStatus ='2' and t.maintStation like '"+user.getStorageid().trim()+"%' ";
			}else if(roleid.trim().equals("A04")){
				//公司技术支持
				sql+=" and t.proStatus = '3' ";
			}else{
				//没有提示数量
				sql+=" and t.maintStation = '' ";
			}
            //System.out.println("技术支持申请待办数量>>>"+sql);
			query = session.createQuery(sql);
            tList = (ArrayList) query.list();
            if (tList != null && tList.size()>0 ) {
            	jsonObject.put("jsnum", tList.size());
		    }
            
            
            jobiArray.put(0, jsonObject);
            
            json.put("code", "200");
   			json.put("info", "OK");
            rejson.put("status", json);
       		rejson.put("data", jobiArray);
		}catch(Exception ex){
			ex.printStackTrace();
		} finally {
            try {
            	if(session!=null){
            		session.close();
            	}
            } catch (HibernateException hex) {
                DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
            }

        }
		
		return Response.status(200).entity(rejson.toString()).build();

	}

	/**
	 * 用于主页显示待审批数
	 * @param userid
	 * @return
	 * @throws JSONException
	 */
	@POST
	@Path("/shandqs/{userid}")
	@Produces("application/json")
	public Response _shandqs(@PathParam("userid") String userid)
			throws JSONException {
		
		return null;
	}
}