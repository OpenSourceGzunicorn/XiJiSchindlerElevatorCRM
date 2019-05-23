package com.mobileserver;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
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

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;

/**
 * 手机APP端调用，厂检厂检反馈日志类
 * @author Crunchify
 */
@Path("/EtcLogmanage")
public class EtcLogmanage {
	
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
	 * 厂检反馈日志-列表
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

		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>厂检反馈日志-列表");
		
		try{
			session = HibernateUtil.getSession();
			//tx = session.beginTransaction();
			String sql ="select rowid,operid,operdate,contractno,projectname"
					+ " from TransferCaseRegisterLog where operId='"+userid+"' ";
			
			if (sdate != null && !"".equals(sdate)) {
				sql += " and operDate >= '"+sdate.trim()+" 00:00:00'";
			}
			if (edate != null && !"".equals(edate)) {
				sql += " and operDate <= '"+edate.trim()+" 99:99:99'";
			}
		
			sql +=" order by operDate desc";
			
			//System.out.println(">>>>"+sql);
			
			List selectList=session.createSQLQuery(sql).list();
			if(selectList!=null&&selectList.size()>0){
				for(int i=0;i<selectList.size();i++){
					Object[] objs=(Object[]) selectList.get(i);
					JSONObject jsonObject=new JSONObject();
            	  	jsonObject.put("rowid", objs[0]);
            	  	jsonObject.put("operid", objs[1]);
            	  	jsonObject.put("opername",bd.getName(session, "Loginuser", "username", "userid", (String)objs[1]));
            	  	jsonObject.put("operdate", objs[2]);
            	  	jsonObject.put("contractno", objs[3]);
            	  	jsonObject.put("projectname", objs[4]);

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
	 * 厂检反馈日志-查看
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

		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>厂检反馈日志-查看"); 
		
		try{
			session = HibernateUtil.getSession();
			//tx = session.beginTransaction();
			String sql ="select operid,operdate,contractno,projectname,"
					+ "inscompanyname,phnum,iscjwx,iszj,xcfkwt,workcontent,rem"
					+ " from TransferCaseRegisterLog  where rowid="+rowed;

			List selectList=session.createSQLQuery(sql).list();
			if(selectList!=null&&selectList.size()>0){
					Object[] objs =(Object[]) selectList.get(0);
					JSONObject jsonObject=new JSONObject();
            	  	jsonObject.put("rowid", rowed);
            	  	jsonObject.put("operid", objs[0]);
            	  	jsonObject.put("opername",bd.getName(session, "Loginuser", "username", "userid", (String)objs[0]));
            	  	jsonObject.put("operdate", objs[1]);
            	  	jsonObject.put("contractno", objs[2]);
            	  	jsonObject.put("projectname", objs[3]);
            	  	jsonObject.put("inscompanyname", objs[4]);
            	  	jsonObject.put("phnum", objs[5]);
            	  	jsonObject.put("iscjwx", objs[6]);
            	  	jsonObject.put("iszj", objs[7]);
            	  	jsonObject.put("xcfkwt", objs[8]);
            	  	jsonObject.put("workcontent", objs[9]);
            	  	jsonObject.put("rem", objs[10]);
            	  	
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
	 * 	厂检反馈日志-新建
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/fkrzadd")
	@Produces("application/json")
	public Response saveLogManage (@FormParam("data") JSONObject data ) throws JSONException{
		Session session = null;
		Transaction tx = null;

		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>厂检反馈日志-保存");
		
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			String operid=(String) data.get("operid");
			String operdate=(String) data.get("operdate");
			String contractno=(String) data.get("contractno");
			String projectname=(String) data.get("projectname");
			String inscompanyname=(String) data.get("inscompanyname");
			String phnum=(String) data.get("phnum");
			String iscjwx=(String) data.get("iscjwx");
			String iszj=(String) data.get("iszj");
			String xcfkwt=(String) data.get("xcfkwt");
			String workcontent=(String) data.get("workcontent");
			String rem=(String) data.get("rem");
			
			String sqlins="insert into TransferCaseRegisterLog(operid,operdate,contractno,projectname,"
					+ "inscompanyname,phnum,iscjwx,iszj,xcfkwt,workcontent,rem) "
					+ "values('"+operid+"','"+operdate+"','"+contractno+"','"+projectname+"',"
					+ "'"+inscompanyname+"','"+phnum+"','"+iscjwx+"','"+iszj+"','"+xcfkwt+"','"+workcontent+"','"+rem+"')";
			session.connection().prepareStatement(sqlins).execute();
			
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
