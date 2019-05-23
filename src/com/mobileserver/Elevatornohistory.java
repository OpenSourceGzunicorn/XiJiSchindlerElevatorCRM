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
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.struts.action.xjsgg.XjsggAction;
/**
 * 手机APP端调用，历史查看类
 * @author Elevatortrans
 */
@Path("/Elevatornohistory")
public class Elevatornohistory {
	
	BaseDataImpl bd = new BaseDataImpl();
	 /* 维保历史 */
            /**
             * 保养历史-列表
             * @param userid
             * @param elevatorno
             * @return
             */
			@GET
			@Path("/bylist/{userid}/{elevatorno}")
			@Produces("application/json")
			public Response getByList(
					@PathParam("userid") String userid, 
					@PathParam("elevatorno") String elevatorno
					){
				Session session = null;
				Transaction tx = null;
				//Loginuser user=null;
				JSONObject rejson = new JSONObject();
				JSONObject json = new JSONObject();//代表对象 {}
				JSONArray jobiArray = new JSONArray();//代表数组 []
				try{
					session = HibernateUtil.getSession();
					//tx = session.beginTransaction();
					String sql="from MaintenanceWorkPlanDetail m where m.maintDate < '"+CommonUtil.getNowTime("yyyy-MM-dd")+"'";
					if(elevatorno!=null&&!"".equals(elevatorno.trim())){
						sql+=" m.maintenanceWorkPlanMaster.elevatorNo ='"+elevatorno.trim()+"'";
					}
					sql +=" order by m.numno";
					List list =session.createQuery(sql).list();
					
					if(list!=null&&list.size()>0){
						for(int i=0;i<list.size();i++){
							MaintenanceWorkPlanDetail m=(MaintenanceWorkPlanDetail) list.get(i);
						    JSONObject jsonObject=new JSONObject();
						    jsonObject.put("numno", m.getNumno());//维保作业计划书明细序号
						    jsonObject.put("elevatorno", m.getMaintenanceWorkPlanMaster().getElevatorNo());//电梯编号
						    jsonObject.put("maintdate", m.getMaintDate());//保养日期
						    jsonObject.put("mainttype", m.getMaintType());//保养类型
						    jobiArray.put(i,jsonObject);
						}
					}
					json.put("code", "200");
		  			json.put("info", "OK");
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
			 * 保养历史-查看
			 * @param userid
			 * @param numno
			 * @return
			 */
			@GET
			@Path("/bydisplay/{userid}/{numno}")
			@Produces("application/json")
			public Response getByDisplay(
					@PathParam("userid") String userid, 
					@PathParam("numno") String numno
					){
				Session session = null;
				Transaction tx = null;
				Loginuser user=null;
				JSONObject rejson = new JSONObject();
				JSONObject json = new JSONObject();//代表对象 {}
				JSONArray jobiArray = new JSONArray();//代表数组 []
				try{
					session = HibernateUtil.getSession();
					//tx = session.beginTransaction();
					String sql="from MaintenanceWorkPlanDetail m where m.numno = '"+numno.trim()+"'";
					List list =session.createQuery(sql).list();
					if(list!=null&&list.size()>0){
						
							MaintenanceWorkPlanDetail m=(MaintenanceWorkPlanDetail) list.get(0);
						    JSONObject jsonObject=new JSONObject();
						   
						    jsonObject.put("numno", m.getNumno());//维保作业计划书明细序号
						    jsonObject.put("elevatorno", m.getMaintenanceWorkPlanMaster().getElevatorNo());//电梯编号
						    jsonObject.put("maintdate", m.getMaintDate());//保养日期
						    jsonObject.put("week", m.getWeek());//星期
						    jsonObject.put("mainttype", m.getMaintType());//保养类型
						    jsonObject.put("maintpersonnel", bd.getName(session, "Loginuser", "username", "userid", m.getMaintPersonnel()));//最终维保工
						    jsonObject.put("rem", m.getRem());//备注
						    jobiArray.put(0,jsonObject);
						
					}
					json.put("code", "200");
		  			json.put("info", "OK");
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
			 * 急修历史-列表
             * @param userid
             * @param elevatorno
             * @return
             */
			@GET
			@Path("/jxlist/{userid}/{elevatorno}/{repairtime}")
			@Produces("application/json")
			public Response getJxList(
					@PathParam("userid") String userid, 
					@PathParam("elevatorno") String elevatorno,
					@PathParam("repairtime") String repairtime){
				Session session = null;
				Transaction tx = null;
				//Loginuser user=null;
				JSONObject rejson = new JSONObject();
				JSONObject json = new JSONObject();//代表对象 {}
				JSONArray jobiArray = new JSONArray();//代表数组 []
				try{
					session = HibernateUtil.getSession();
					//tx = session.beginTransaction();
					String sql="select c,cp from CalloutMaster c,CalloutProcess cp where c.calloutMasterNo=cp.calloutMasterNo and c.repairTime < '"+CommonUtil.getNowTime("yyyy-MM-dd")+"'";
					if(elevatorno!=null&&!"".equals(elevatorno.trim())){
						sql+=" c.elevatorNo ='"+elevatorno.trim()+"'";
					}
					if(repairtime!=null&&!"".equals(repairtime.trim())){
						sql+=" c.repairtime ='"+repairtime.trim()+"'";
					}
					sql +=" order by c.calloutMasterNo";
					List list =session.createQuery(sql).list();

				    if(list!=null&&list.size()>0){
				    	for(int i=0;i<list.size();i++){
				    		JSONObject jsonObject=new JSONObject();
				    		Object[] objects=(Object[]) list.get(i);
				    		
				    		CalloutMaster c=(CalloutMaster) objects[0];
				    		CalloutProcess cp=(CalloutProcess) objects[1];
				    		jsonObject.put("calloutmasterno", c.getCalloutMasterNo());//急修单号
				    		jsonObject.put("elevatorno", c.getElevatorNo());//电梯编号
				    		jsonObject.put("repairtime", c.getRepairTime());//报修日期
				    		jsonObject.put("completetime", cp.getCompleteTime());//维修完成时间 
				    		jobiArray.put(i, jsonObject);
				    	}
				    }
					json.put("code", "200");
		  			json.put("info", "OK");
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
			
			
			 /* 急修历史 */
			
			/**
			 * 急修历史-查看
			 * @param userid
			 * @param numno
			 * @return
			 */
			@GET
			@Path("/jxdisplay/{userid}/{calloutmasterno}")
			@Produces("application/json")
			public Response getJxDisplay(
					@PathParam("userid") String userid, 
					@PathParam("calloutmasterno") String calloutmasterno
					){
				Session session = null;
				Transaction tx = null;
				Loginuser user=null;
				JSONObject rejson = new JSONObject();
				JSONObject json = new JSONObject();//代表对象 {}
				JSONArray jobiArray = new JSONArray();//代表数组 []
				try{
					session = HibernateUtil.getSession();
					//tx = session.beginTransaction();
					String sql="select c,cp from CalloutMaster c,CalloutProcess cp where "
							+ "c.calloutMasterNo=cp.calloutMasterNo "
							+ "and c.calloutMasterNo ='"+calloutmasterno.trim()+"'";
					List list =session.createQuery(sql).list();
				    if(list!=null&&list.size()>0){
				    		JSONObject jsonObject=new JSONObject();
				    		Object[] objects=(Object[]) list.get(0);
				    		
				    		CalloutMaster c=(CalloutMaster) objects[0];
				    		CalloutProcess cp=(CalloutProcess) objects[1];
				    		jsonObject.put("calloutmasterno", c.getCalloutMasterNo());//急修单号
				    		jsonObject.put("elevatorno", c.getElevatorNo());//电梯编号
				    		jsonObject.put("repairtime", c.getRepairTime());//报修日期
				    		jsonObject.put("completetime", cp.getCompleteTime());//维修完成时间 
				    		
				    		jsonObject.put("repairmode", c.getRepairMode());//报修方式
				    		jsonObject.put("repairuser", c.getRepairUser());//报修人
				    		jsonObject.put("repairtel", c.getRepairTel());//报修人电话
				    		jsonObject.put("serviceobjects", c.getServiceObjects());//服务对象
				    		jsonObject.put("companyid", c.getCompanyId());//报修单位
				    		jsonObject.put("projectaddress", c.getProjectAddress());//项目地址
				    		jsonObject.put("repairdesc", c.getRepairDesc());//报修描述
				    		jsonObject.put("processdesc", cp.getProcessDesc());//维修过程描述
				    		jsonObject.put("hftid", cp.getHftId());//故障分类代码
				    		jsonObject.put("hftname", bd.getName(session, "HotlineFaultType", "hftDesc", "hftId", cp.getHftId()));//故障分类名称
				    		jsonObject.put("assignobject2",  bd.getName(session, "Loginuser", "username", "userid", cp.getAssignObject2()));//最终派工对象
				    		jsonObject.put("servicerem", cp.getServiceRem());//维修备注
				    		jsonObject.put("assigntime", cp.getAssignTime());//接收时间
				    		jsonObject.put("arrivedatetime", cp.getArriveDateTime());//到现场时间
				    		jobiArray.put(0, jsonObject);
				    
				    }
					json.put("code", "200");
		  			json.put("info", "OK");
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
			
			
}
