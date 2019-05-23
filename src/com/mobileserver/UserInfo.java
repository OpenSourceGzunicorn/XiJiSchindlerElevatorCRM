package com.mobileserver;

/**
 * 手机APP端调用，登录用户处理类
 * @author Crunchify
 */

import java.util.ArrayList;

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
import com.gzunicorn.common.util.CryptUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.viewmanager.ViewLoginUserInfo;

@Path("/UserInfo")
public class UserInfo {

	BaseDataImpl bd = new BaseDataImpl();
	
	@GET
	@Path("{userid}")
	@Produces("application/json")
	public Response _UserInfo(@PathParam("userid") String userid)
			throws JSONException {

		//System.out.println("手机APP调用==登录名称："+userid);
		
		JSONObject rejson = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		// 根据用户名从数据库中获取用户信息
		jsonObject.put("userid", userid);
        jsonObject.put("username", "");
        jsonObject.put("roleid", "");
        jsonObject.put("rolename", "");
        jsonObject.put("storageid", "");
        jsonObject.put("storagename", "");
        jsonObject.put("comid", "");
        jsonObject.put("comname", "");
        
        jobiArray.put(0, jsonObject);

        json.put("code", "400");
		json.put("info", "非法用户登录!");
		
		rejson.put("status", json);
		rejson.put("data", jobiArray);

		return Response.status(200).entity(rejson.toString()).build();
	}

	@GET
	@Path("{userid}/{password}/{phoneimei}")
	@Produces("application/json")
	public Response loginUser(
			@PathParam("userid") String userid,
			@PathParam("password") String password,
			@PathParam("phoneimei") String phoneimei) throws JSONException {
		
		System.out.println("==========手机登录："+userid+" "+phoneimei+" 开始==========");
		Session session = null;
		Transaction tx = null;
		JSONObject rejson = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		try{
			session = HibernateUtil.getSession();
			Query query = session.createQuery("from ViewLoginUserInfo a where a.userID = :userID and a.enabledFlag = :enabledFlag");
			
			query.setString("userID", userid);
            query.setString("enabledFlag","Y");
            ArrayList userList = (ArrayList) query.list();
             //判断用户是否正确
            if (userList == null || userList.isEmpty() || userList.size() == 0) {
                DebugUtil.println("非法用户登录!");
                
                jsonObject.put("userid", userid);
                jsonObject.put("username", "");
                jsonObject.put("roleid", "");
                jsonObject.put("rolename", "");
                jsonObject.put("storageid", "");
                jsonObject.put("storagename", "");
                jsonObject.put("comid", "");
                jsonObject.put("comname", "");
    			
    			jobiArray.put(0, jsonObject);

                json.put("code", "400");
    			json.put("info", "非法用户登录!");
            } else {
            	ViewLoginUserInfo userInfo = (ViewLoginUserInfo) userList.get(0);
                // 判断密码是否正确
                if ( password == null || !new CryptUtil().decode(password, "LO").equals(userInfo.getPasswd())) {
                    DebugUtil.println("用户密码输入错误!");
                    
                    jsonObject.put("userid", userInfo.getUserID());
                    jsonObject.put("username", userInfo.getUserName());
                    jsonObject.put("roleid", "");
                    jsonObject.put("rolename", "");
                    jsonObject.put("storageid", "");
                    jsonObject.put("storagename", "");
                    jsonObject.put("comid", "");
                    jsonObject.put("comname", "");
                    jsonObject.put("phone","");
        			jobiArray.put(0, jsonObject);

                    json.put("code", "400");
        			json.put("info", "密码输入错误!");
                } else {
                	String isok="Y";
                	String phoneimei2=userInfo.getPhoneimei();
                	if(phoneimei2==null || phoneimei2.trim().equals("") 
                			|| phoneimei2.trim().equals("null") || phoneimei2.trim().equals("NULL")){
                		tx = session.beginTransaction();
                		String sqlu="update Loginuser set phoneimei='"+phoneimei.trim()+"' where UserID='"+userid+"'";
                		session.connection().prepareStatement(sqlu).execute();
                		tx.commit();
                	}else{
                		//if(!phoneimei2.trim().equals(phoneimei.trim())){
                		if(phoneimei2.trim().indexOf(phoneimei.trim())>-1){
                			isok="Y";
                		}else{
                			isok="N";
                		}
                	}
                	
            		DebugUtil.println(userInfo.getUserName()+ " 用户登录成功.");
                    
                    jsonObject.put("userid", userInfo.getUserID());
                    jsonObject.put("username", userInfo.getUserName());
                    jsonObject.put("roleid", userInfo.getRoleID());
                    jsonObject.put("rolename", userInfo.getRoleName());
                    jsonObject.put("storageid", userInfo.getStorageId());
                    jsonObject.put("storagename", userInfo.getStorageName());
                    jsonObject.put("comid", userInfo.getComID());
                    jsonObject.put("comname", userInfo.getComName());
                    jsonObject.put("phone",userInfo.getPhone());
                    
                    String competence ="99";

                    json.put("code", "200");
        			json.put("info", "登录成功!");
        			
                    if(userInfo.getRoleID().equals("A03") || userInfo.getRoleID().equals("A50") 
                    		|| userInfo.getRoleID().equals("A49") || userInfo.getRoleID().equals("A53")){
                    	//维保站长A49，维保工A50,维保经理 A03，维修技术员 A53，
                    	competence ="1";
                    }else if(userInfo.getRoleID().equals("A51")){
                        //厂检员
                    	competence ="2";
                    }else if(userInfo.getRoleID().equals("A12")){
                        //督查员 
                    	competence ="3";
                    }else if(userInfo.getRoleID().equals("A01") || userInfo.getRoleID().equals("A04") 
                    		|| userInfo.getRoleID().equals("A11")){
                    	//A01 管理员 ,A04  公司技术支持  ，A11 技术质量安全经理
                    	competence ="0";
                    }else{
                        json.put("code", "400");
            			json.put("info", "无权限使用此应用！");
                    }
                    jsonObject.put("competence", competence);
                    jobiArray.put(0, jsonObject);
                    
                	if(isok.equals("N")){
                		json.put("code", "400");
            			json.put("info", "非法手机设备，无法登录。<br/>请联系管理员！");
                    }
                }
            }
            
            rejson.put("status", json);
    		rejson.put("data", jobiArray);
    		
    		System.out.println("===="+jobiArray.toString());
    		System.out.println("==========手机登录："+userid+" 结束 ==========");
    		
		}catch(Exception ex){
			if(tx!=null){
				tx.rollback();
			}
			ex.printStackTrace();
			
			json.put("code", "400");
  			json.put("info", "登录出现错误！");
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

	
	@POST
	@Path("{userid}/{oldPassword}/{newPassword}")
	@Produces("application/json")
	public Response MyChangePwd(
			@PathParam("userid") String userid,
			@PathParam("oldPassword") String oldPassword,@PathParam("newPassword") String newPassword) throws JSONException {
		//System.out.println("手机APP调用==名称："+userid+" ; 旧密码："+oldPassword+" ; 新密码："+newPassword);
		
		Session session = null;
		Transaction tx=null;
		JSONObject rejson = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>我的资料--修改密码");
		
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			Query query = session.createQuery("from ViewLoginUserInfo a where a.userID = :userID and a.enabledFlag = :enabledFlag");
			query.setString("userID", userid);
            query.setString("enabledFlag","Y");
            ArrayList userList = (ArrayList) query.list();
             //判断用户是否正
            if (userList == null || userList.isEmpty() || userList.size() == 0) {
                DebugUtil.println("非法用户登录!");
                
                jsonObject.put("userid", userid);
                jsonObject.put("username", "");
    			jobiArray.put(0, jsonObject);

                json.put("code", "400");
    			json.put("info", "非法用户登录!");
            } else {
            	ViewLoginUserInfo userInfo = (ViewLoginUserInfo) userList.get(0);
            	Loginuser lu=(Loginuser) session.get(Loginuser.class, userInfo.getUserID());
                // 判断密码是否正确
                if (oldPassword == null || !new CryptUtil().decode(oldPassword, "LO").equals(userInfo.getPasswd())) {
                    
                    jsonObject.put("userid", userInfo.getUserID());
                    jsonObject.put("username", userInfo.getUserName());
        			
        			jobiArray.put(0, jsonObject);

                    json.put("code", "400");
        			json.put("info", "密码输入错误!");
                } else {
                    
                    jsonObject.put("userid", userInfo.getUserID());
                    jsonObject.put("username", userInfo.getUserName());
                   
                    
                    lu.setPasswd(new CryptUtil().decode(newPassword, "LO"));
					
                    session.update(lu);
					tx.commit();
                    jobiArray.put(0, jsonObject);
        			
                    json.put("code", "200");
        			json.put("info", "修改成功!");
                }
            }
            
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
	 * 获取用户信息
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/getuserinfo")
	@Produces("application/json")
	public Response getUserInfo (@FormParam("data") JSONObject data) throws JSONException{
		Session session = null;
		
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []

		System.out.println(">>>>>>>>我的资料--获取用户信息");
		try{
			session = HibernateUtil.getSession();
			
			String userid=(String) data.get("userid");//登录用户代码
			
			Loginuser l =(Loginuser) session.get(Loginuser.class, userid);
			
			JSONObject object=new JSONObject();
			object.put("userid", userid);
			object.put("newphone", l.getPhone());//电话
			object.put("newemail", l.getEmail());//邮箱
			object.put("nweidcardno", l.getIdcardno());//身份证
			jobiArray.put(0, object);

			json.put("code", "200");
			json.put("info", "OK");
	        rejson.put("status", json);
	  		rejson.put("data", jobiArray);
		}catch(Exception ex){
			json.put("code", "400");
			json.put("info", "NOT OK");
	        rejson.put("status", json);
	  		rejson.put("data", jobiArray);
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
	 * 保存用户信息
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/userinfoadd")
	@Produces("application/json")
	public Response userInfoAdd (@FormParam("data") JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;
		
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []

		System.out.println(">>>>>>>>我的资料--保存用户信息");
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			String userid=(String) data.get("userid");//登录用户代码
			String newphone=(String) data.get("newphone");//电话
			String newemail=(String) data.get("newemail");//邮箱
			String nweidcardno=(String) data.get("nweidcardno");//身份证
			
			Loginuser l =(Loginuser) session.get(Loginuser.class, userid);
			l.setNewphone(newphone);
			l.setNewemail(newemail);
			l.setNweidcardno(nweidcardno);
			l.setAuditoperid(null);
			l.setAuditdate(null);
			l.setAuditrem(null);
			session.save(l);
			
			tx.commit();
			
			json.put("code", "200");
			json.put("info", "修改用户资料成功！");
	        rejson.put("status", json);
	  		rejson.put("data", jobiArray);
		}catch(Exception ex){
			if(tx!=null){
				tx.rollback();
			}
			json.put("code", "400");
			json.put("info", "修改用户资料失败！");
	        rejson.put("status", json);
	  		rejson.put("data", jobiArray);
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