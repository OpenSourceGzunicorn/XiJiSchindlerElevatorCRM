package com.mobileserver;

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
import com.gzunicorn.hibernate.hotlinemanagement.calloutmaster.CalloutMaster;
import com.gzunicorn.hibernate.hotlinemanagement.calloutprocess.CalloutProcess;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplandetail.MaintenanceWorkPlanDetail;
import com.gzunicorn.hibernate.mobileofficeplatform.logmanagement.LogManagement;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.struts.action.xjsgg.XjsggAction;
/**
 * 手机APP端调用，反馈日志类
 * @author Crunchify
 */
@Path("/Logmanage")
public class Logmanage {
	
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
	 * 反馈日志-列表
	 * @param userid
	 * @param sdate
	 * @param edate
	 * @return
	 */
	@GET
	@Path("/fkrzlist/{userid}/{sdate}/{edate}")
	@Produces("application/json")
	public Response getLogList(
			@PathParam("userid") String userid, 
			@PathParam("sdate") String sdate,
			@PathParam("edate") String edate
			){
		Session session = null;
		Transaction tx = null;
		Loginuser user=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>反馈日志-列表");
		
		try{
			session = HibernateUtil.getSession();
			//tx = session.beginTransaction();
			String sql ="from LogManagement l where l.operId='"+userid+"' ";
			
			if (sdate != null && !"".equals(sdate)) {
				sql += " and l.operDate >= '"+sdate.trim()+" 00:00:00'";
			}
			if (edate != null && !"".equals(edate)) {
				sql += " and l.operDate <= '"+edate.trim()+" 99:99:99'";
			}
		
			sql +=" order by l.operDate desc";
			
			//System.out.println(">>>>"+sql);
			
			List selectList=session.createQuery(sql).list();
			if(selectList!=null&&selectList.size()>0){
				for(int i=0;i<selectList.size();i++){
					LogManagement l =(LogManagement) selectList.get(i);
					JSONObject jsonObject=new JSONObject();
            	  	jsonObject.put("rowid", l.getRowid());
            	  	jsonObject.put("operid", l.getOperId());
            	  	
            	  	jsonObject.put("operName",bd.getName(session, "Loginuser", "username", "userid", l.getOperId()) );
            	  	jsonObject.put("operdate", l.getOperDate());
            	  	jsonObject.put("salescontractno", l.getSalesContractNo());
            	  	jsonObject.put("projectname", l.getProjectName());

            		jobiArray.put(i, jsonObject);
				}	
			}
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
	 * 反馈日志-查看
	 * @param userid
	 * @param rowed
	 * @return
	 */
	@GET
	@Path("/fkrzdetail/{userid}/{rowid}")
	@Produces("application/json")
	public Response getLogDisplay(
	@PathParam("userid") String userid,
	@PathParam("rowid") String rowed
	){
		Session session = null;
		Transaction tx = null;
		Loginuser user=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>反馈日志-查看");
		
		try{
			session = HibernateUtil.getSession();
			//tx = session.beginTransaction();
			String sql ="from LogManagement l where l.rowid="+rowed;

			List selectList=session.createQuery(sql).list();
			if(selectList!=null&&selectList.size()>0){
					LogManagement l =(LogManagement) selectList.get(0);
					JSONObject jsonObject=new JSONObject();
            	  	jsonObject.put("rowid", l.getRowid());
            	  	jsonObject.put("operid", l.getOperId());
            		jsonObject.put("maintdivision",bd.getName(session, "Company", "comfullname", "comid", l.getMaintDivision()));
            	  	jsonObject.put("maintstation",bd.getName(session, "Storageid", "storagename", "storageid",l.getMaintStation()));
            	  	jsonObject.put("operName",bd.getName(session, "Loginuser", "username", "userid", l.getOperId()) );
            	  	jsonObject.put("operdate", l.getOperDate());
            	  	jsonObject.put("salescontractno", l.getSalesContractNo());
            	  	jsonObject.put("projectname", l.getProjectName());
            	  	jsonObject.put("workcontent", l.getWorkContent());
            		jobiArray.put(0, jsonObject);
			}
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
	 * 	反馈日志-新建
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/fkrzadd/{data}")
	@Produces("application/json")
	public Response saveLogManage (
			@PathParam("data")JSONObject data
	) throws JSONException{
		Session session = null;
		Transaction tx = null;
		Loginuser user=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>反馈日志-新建");
		
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			String operid=(String) data.get("operid");//申请人
			String operdate=(String) data.get("operdate");//申请日期
			String salescontractno=CommonUtil.URLDecoder_decode((String) data.get("salescontractno"));//销售合同号
			String projectname=CommonUtil.URLDecoder_decode((String) data.get("projectname"));//项目名称
			String workcontent=CommonUtil.URLDecoder_decode((String) data.get("workcontent"));//工作内容	
			LogManagement l=new LogManagement();	
			List userList=session.createQuery("from Loginuser where userid ='"+operid+"'").list();
			if(userList!=null&&userList.size()>0){
				user=(Loginuser) userList.get(0);
			}
            l.setOperId(operid);
			l.setOperDate(operdate);
			l.setMaintDivision(bd.getName(session, "Storageid", "comid", "storageid",user.getStorageid()));
			l.setMaintStation(user.getStorageid());
			l.setSalesContractNo(salescontractno);
			l.setProjectName(projectname);
			l.setWorkContent(workcontent);
			session.save(l);
			tx.commit();
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
      		rejson.put("data", jobiArray);
		}catch(Exception ex){
			tx.rollback();
			ex.printStackTrace();
			json.put("code", "400");
  			json.put("info", "NOT");
            rejson.put("status", json);
      		rejson.put("data", jobiArray);
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
}
