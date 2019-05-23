package com.mobileserver;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.sql.ResultSet;
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
import org.zefer.html.doc.p;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.hibernate.hotlinemanagement.calloutmaster.CalloutMaster;
import com.gzunicorn.hibernate.hotlinemanagement.calloutprocess.CalloutProcess;
import com.gzunicorn.hibernate.mobileofficeplatform.technologysupport.TechnologySupport;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.struts.action.xjsgg.SmsService;
import com.gzunicorn.struts.action.xjsgg.XjsggAction;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 手机APP端调用，技术支持申请类
 * @author Crunchify
 */
@Path("/Technology")
public class Technology {
	
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
	 * 技术支持申请-列表(查询)
	 * @param userid
	 * @param singleno
	 * @param sdate
	 * @param edate
	 * @return
	 */
	@GET
	@Path("/jszclist/{data}")
	@Produces("application/json")
	public Response getTechnologyList(
			@PathParam("data")  JSONObject data
			){
		Session session = null;
		Transaction tx = null;
		Loginuser user=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>技术支持申请-列表(查询)");
		
        try{
        	 String sdate=(String) data.get("sdate");
             String edate=(String) data.get("edate");
             String userid=(String) data.get("userid");
             String singleno=(String) data.get("singleno");

			session = HibernateUtil.getSession();
			
			List userList=session.createQuery("from Loginuser where userid ='"+userid+"'").list();
			if(userList!=null&&userList.size()>0){
				user=(Loginuser) userList.get(0);
			}
			
			//1：站长处理 2：经理处理 3：公司技术支持处理 4：处理完成
			String roleid=user.getRoleid();
			String sql="from TechnologySupport t where 1=1 ";
			if(roleid.trim().equals("A03")){
				//维保经理
				sql+=" and t.proStatus ='2' and t.maintStation like '"+user.getStorageid().trim()+"%'";
			}else if(roleid.trim().equals("A49")){
				//维保站长
				sql+=" and t.proStatus ='1' and t.maintStation like '"+user.getStorageid().trim()+"'";
			}else if(roleid.trim().equals("A50")){
				//维保工
				sql+=" and t.assignUser = '"+userid+"' ";
			}else if(roleid.trim().equals("A04") || roleid.trim().equals("A11")){
				//公司技术支持
				sql+=" and t.proStatus = '3'";
			}else{
				//不给看
				sql+=" and t.maintStation = ''";
			}
				
			if (singleno != null && !"".equals(singleno)){
				sql+=" and t.singleNo like '%"+singleno+"%'";		
			}
			if (sdate != null && !"".equals(sdate)) {
				sql += " and t.operDate >= '"+sdate.trim()+" 00:00:00'";
			}
			if (edate != null && !"".equals(edate)) {
				sql += " and t.operDate <= '"+edate.trim()+" 99:99:99'";
			}
			
			sql +=" order by t.operDate desc";
			Query query = session.createQuery(sql);
            ArrayList tList = (ArrayList) query.list();
             //cpList是否有值
            if (tList != null && tList.size()>0 ) {
            	for(int i=0;i<tList.size();i++){
            	  	TechnologySupport t=(TechnologySupport) tList.get(i);
            	  	JSONObject jsonObject=new JSONObject();
            	  	jsonObject.put("billno", t.getBillno());
            	  	jsonObject.put("singleno", t.getSingleNo());
            	  	jsonObject.put("elevatorno", t.getElevatorNo());
            	  	jsonObject.put("assignuser", t.getAssignUser());
            	  	jsonObject.put("assignuserName", bd.getName(session, "Loginuser", "username", "userid", t.getAssignUser()));
            	  	jsonObject.put("operdate", t.getOperDate());
            	  	jsonObject.put("prostatus", t.getProStatus());
            	  	
            	  	if(t.getProStatus().trim().equals("1")){
            	  		jsonObject.put("prostatusName", "站长处理");
            	  	}else if(t.getProStatus().trim().equals("2")){
            	  		jsonObject.put("prostatusName", "经理处理");
            	  	}else if(t.getProStatus().trim().equals("3")){
            	  		jsonObject.put("prostatusName", "技术支持处理");
            	  	}else if(t.getProStatus().trim().equals("4")){
            	  		jsonObject.put("prostatusName", "处理完成");
            	  	}
            	  	
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
	 * 技术支持申请-查看
	 * @param userid
	 * @param billno
	 * @return
	 */
	@GET
	@Path("/jszcdetail/{data}")
	@Produces("application/json")
	public Response getTechnologyDisplay(@PathParam("data")  JSONObject data){
		Session session = null;
		Transaction tx = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>技术支持申请-查看");
		//Loginuser user=null;
		try{
			String userid=(String)data.get("userid");
			String billno=(String)data.get("billno");
			session = HibernateUtil.getSession();

			String sql="from TechnologySupport t where t.billno ='"+billno.trim()+"'";
			Query query = session.createQuery(sql);
            ArrayList tList = (ArrayList) query.list();
             //cpList是否有值
            if (tList != null && tList.size()>0 ) {
            	
            	  	TechnologySupport t=(TechnologySupport) tList.get(0);
            	  	JSONObject jsonObject=new JSONObject();
            	  	jsonObject.put("billno", t.getBillno());
            	  	jsonObject.put("singleno", t.getSingleNo());
            	  	jsonObject.put("elevatorno", t.getElevatorNo());
            	  	jsonObject.put("assignuser", bd.getName(session, "Loginuser", "username", "userid", t.getAssignUser()));
            	  	jsonObject.put("operdate", t.getOperDate());
            	  	jsonObject.put("prostatus", t.getProStatus());
            	  	if(t.getProStatus().trim().equals("1")){
            	  		jsonObject.put("prostatusName", "站长处理");
            	  	}else if(t.getProStatus().trim().equals("2")){
            	  		jsonObject.put("prostatusName", "经理处理");
            	  	}else if(t.getProStatus().trim().equals("3")){
            	  		jsonObject.put("prostatusName", "公司技术支持处理");
            	  	}else if(t.getProStatus().trim().equals("4")){
            	  		jsonObject.put("prostatusName", "处理完成");
            	  	}
            	  	jsonObject.put("assignusertel", t.getAssignUserTel());
            	  	jsonObject.put("maintdivision",bd.getName(session, "Company", "comfullname", "comid", t.getMaintDivision()));
            	  	jsonObject.put("maintstation",bd.getName(session, "Storageid", "storagename", "storageid",t.getMaintStation()));
            	  	jsonObject.put("hmtid", t.getHmtId());
            	  	jsonObject.put("hmtname",bd.getName(session, "HotlineMotherboardType", "hmtName", "hmtId", t.getHmtId()));
            	  	jsonObject.put("faultcode", t.getFaultCode());
            	  	jsonObject.put("faultstatus", t.getFaultStatus());
            	  	//维保站长处理人
            	  	jsonObject.put("msprocesspeople",bd.getName(session, "Loginuser", "username", "userid", t.getMsprocessPeople()));
            		jsonObject.put("msprocessdate", t.getMsprocessDate());
            		jsonObject.put("msprocessrem", t.getMsprocessRem());
            		 if(t.getMsisResolve()!=null&&!"".equals(t.getMsisResolve())){
             	    	jsonObject.put("msisresolve",  t.getMsisResolve().equals("Y") ? "已解决" : "未解决");	
             	    }
            		//维保经理处理人
            		jsonObject.put("mmprocesspeople", bd.getName(session, "Loginuser", "username", "userid", t.getMmprocessPeople()));
            		jsonObject.put("mmprocessdate", t.getMmprocessDate());
            		jsonObject.put("mmprocessrem", t.getMmprocessRem());
            		if(t.getMmisResolve()!=null&&!"".equals(t.getMmisResolve())){
            	    	jsonObject.put("mmisresolve", t.getMmisResolve().equals("Y") ? "已解决" : "未解决");	
            	    }
            		//公司技术支持处理人
            	    jsonObject.put("tsprocesspeople", bd.getName(session, "Loginuser", "username", "userid", t.getTsprocessPeople()));
            		jsonObject.put("tsprocessdate", t.getTsprocessDate());
            		jsonObject.put("tsprocessrem", t.getTsprocessRem());
            		
            		/**=====================获取图片=========================*/
    				String folder = PropertiesUtil.getProperty("TechnologySupport.file.upload.folder");
    				BASE64Encoder base64=new BASE64Encoder();
    				//故障图片
    				if(t.getGzImage()!=null && !t.getGzImage().trim().equals("")){
    					String filepath=folder+t.getGzImage();
    					byte[] imgbyte=CommonUtil.imageToByte(filepath);//将图片转换为二进制流
    					jsonObject.put("gzimg", base64.encode(imgbyte));//将二进制流加密
    				}else{
    					jsonObject.put("gzimg", "");
    				}
    				/**=====================获取图片=========================*/
    				
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
	 * 技术支持申请-创建
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/jszcadd")
	@Produces("application/json")
	public Response saveTechnology(@FormParam("data") JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		Loginuser user=null;

		System.out.println(">>>>>>>>技术支持申请-创建");
		
		try{
			session=HibernateUtil.getSession();
			tx=session.beginTransaction();
			
			String singleno=(String) data.get("singleno");//急修编号
			String elevatorno=(String) data.get("elevatorno");//电梯编号
			String hmtid=(String) data.get("hmtid");//主板类型代码
			String faultcode= CommonUtil.URLDecoder_decode((String) data.get("faultcode")) ;//故障代码
			String faultstatus=CommonUtil.URLDecoder_decode((String) data.get("faultstatus"));//故障状态
			String assignuser=(String) data.get("assignuser");//申请人
			String assignusertel=(String) data.get("assignusertel");//申请人电话
			//String operdate=(String) data.get("operdate");//申请日期
			
			//故障代码用", ，"分隔
			String faultCodestr="";
			if(faultcode!=null && !faultcode.trim().equals("")){
				String[] faultCodes=faultcode.split(",");
				for(int i=0;i<faultCodes.length;i++){
					String[] faultCodes2=faultCodes[i].split("，");
					for(int j=0;j<faultCodes2.length;j++){
						if(!faultCodes2[j].trim().equals("")){
							if(faultCodes2[j].indexOf("ER")>-1){
								faultCodestr+=faultCodes2[j]+",";
							}else{
								faultCodestr+="ER"+faultCodes2[j]+",";
							}
						}
					}
				}
				faultCodestr=faultCodestr.substring(0,faultCodestr.length()-1);
			}
			
			TechnologySupport t=new TechnologySupport();
			String year1=CommonUtil.getToday().substring(2,4);
			String billno1 = CommonUtil.getBillno(year1,"TechnologySupport", 1)[0];	
			
			List userList=session.createQuery("from Loginuser where userid ='"+assignuser+"'").list();
			if(userList!=null && userList.size()>0){
				user=(Loginuser) userList.get(0);
			}
			String parentstorageid=bd.getName(session, "Storageid", "parentstorageid", "storageid",user.getStorageid());
			
			t.setBillno(billno1);
			t.setSingleNo(singleno);
			t.setElevatorNo(elevatorno);
			t.setHmtId(hmtid);
			t.setFaultCode(faultCodestr);
			t.setFaultStatus(faultstatus);
			t.setAssignUser(assignuser);
			t.setAssignUserTel(assignusertel);
			t.setOperDate(CommonUtil.getNowTime());
			t.setMaintDivision(user.getGrcid());
			t.setMaintStation(user.getStorageid());
			
			String gzimg= (String) data.get("gzimg");//故障图片
			/**=======================保存图片=============================*/
			String folder = PropertiesUtil.getProperty("TechnologySupport.file.upload.folder");
			String filepath=CommonUtil.getNowTime("yyyy-MM-dd")+"/";
			if(gzimg!=null && !gzimg.trim().equals("")){
				//customerimage=customerimage.replaceAll("data:image/jpeg;base64,", "");//去掉前缀
				String[] cimages=gzimg.split(",");
				BASE64Decoder base64=new BASE64Decoder();
				if(cimages!=null && cimages.length>1){
					byte[] image=base64.decodeBuffer(cimages[1]);
					String newfilename=billno1+"_old.jpg";
					//保存图片
					File f=new File(folder+filepath);
					f.mkdirs();
					FileOutputStream fos=new FileOutputStream(folder+filepath+newfilename);
					fos.write(image);
					fos.flush();
					fos.close();
					//保存图片信息到数据库
					t.setGzImage(filepath+newfilename);
				}
			}
			/**=======================保存图片=============================*/
			
			//1：站长处理 2：经理处理 3：公司技术支持处理 4：处理完成
			if(parentstorageid.trim().equals("0")){
				t.setProStatus("2");
			}else{
				t.setProStatus("1");
			}
			session.save(t);
			
			
			//保存数据到急修过程信息表
			CalloutProcess cp=(CalloutProcess)session.get(CalloutProcess.class, singleno);
			cp.setFaultCode(faultCodestr);
			cp.setFaultStatus(faultstatus);
			cp.setHmtId(hmtid);
			session.save(cp);
			
			tx.commit();
			
			/****************************发送短信给维保经理 开始**********************************/
			//A49  维保站长,A03  维保经理
			String sqlqu="";
			String phone="";
			//1：站长处理 2：经理处理 3：公司技术支持处理 4：处理完成
			if(parentstorageid.trim().equals("0")){
				sqlqu="select userid,phone from loginuser where '"+user.getStorageid()+"' like StorageID+'%' and RoleID in('A03')";
			}else{
				sqlqu="select userid,phone from loginuser where '"+user.getStorageid()+"' like StorageID+'%' and RoleID in('A49')";
			}
			
			ResultSet rs=session.connection().prepareStatement(sqlqu).executeQuery();
			if(rs.next()){
				phone=rs.getString("phone");
			}
			
			if(phone!=null && !phone.equals("")){
				boolean issms=SmsService.techSendSMS(phone);
			}
			/****************************发送短信给维保经理 结束**********************************/
			
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
            } catch (Exception hex) {
                DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
            }

        }
		
		return Response.status(200).entity(rejson.toString()).build();
	}
	
	/**
	 * 技术支持申请-处理保存
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/jszccladd")
	@Produces("application/json")
	public Response saveTechnologycl(@FormParam("data") JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []

		System.out.println(">>>>>>>>技术支持申请-处理保存");
		
		try{
			session=HibernateUtil.getSession();
			tx=session.beginTransaction();
			String billno=(String) data.get("billno");//流水号
			String prostatus=(String) data.get("prostatus");//处理状态
			
			String msprocesspeople=(String) data.get("msprocesspeople");//维保站长处理人
			//String msprocessdate=(String) data.get("msprocessdate");//维保站长处理日期
			String msprocessrem=CommonUtil.URLDecoder_decode((String) data.get("msprocessrem"));//维保站长处理意见
			String msisresolve=(String) data.get("msisresolve");//维保站长是否解决
			
			String mmprocesspeople=(String) data.get("mmprocesspeople");//维保经理处理人
			//String mmprocessdate=(String) data.get("mmprocessdate");//维保经理处理日期
			String mmprocessrem=CommonUtil.URLDecoder_decode((String) data.get("mmprocessrem"));//维保经理处理意见
			String mmisresolve=(String) data.get("mmisresolve");//维保经理是否解决
			
			String tsprocesspeople=(String) data.get("tsprocesspeople");//公司技术支持处理人
			//String tsprocessdate=(String) data.get("tsprocessdate");//公司技术支持处理日期
			String tsprocessrem=CommonUtil.URLDecoder_decode((String) data.get("tsprocessrem"));//公司技术支持处理意见
			
			TechnologySupport t=(TechnologySupport) session.get(TechnologySupport.class, billno);
			String prostatusold=t.getProStatus();
			if(t.getProStatus().trim().endsWith("1")||t.getProStatus()=="1"){
				t.setMsprocessPeople(msprocesspeople);
				t.setMsprocessDate(CommonUtil.getNowTime());
				t.setMsprocessRem(msprocessrem);
				t.setMsisResolve(msisresolve);
			}else if(t.getProStatus().trim().endsWith("2")||t.getProStatus()=="2"){
				t.setMmprocessPeople(mmprocesspeople);
				t.setMmprocessDate(CommonUtil.getNowTime());
				t.setMmprocessRem(mmprocessrem);
				t.setMmisResolve(mmisresolve);
			}else if(t.getProStatus().trim().endsWith("3")||t.getProStatus()=="3"){
				 t.setTsprocessPeople(tsprocesspeople);
				 t.setTsprocessDate(CommonUtil.getNowTime());
				 t.setTsprocessRem(tsprocessrem);
			}
		    t.setProStatus(prostatus);
			session.update(t);
			
			/****************************发送短信给维保经理 开始**********************************/
			//站长处理了，就给经理发短信
			if(prostatusold!=null && prostatusold.equals("1")){
				//A49  维保站长,A03  维保经理
				String sqlqu="select userid,phone from loginuser where '"+t.getMaintStation()+"' like StorageID+'%' and RoleID in('A03')";
				String phone="";
				
				ResultSet rs=session.connection().prepareStatement(sqlqu).executeQuery();
				if(rs.next()){
					phone=rs.getString("phone");
				}
				
				if(phone!=null && !phone.equals("")){
					boolean issms=SmsService.techSendSMS(phone);
				}
			}
			/****************************发送短信给维保经理 结束**********************************/
			
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
            } catch (Exception hex) {
                DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
            }

        }

		return Response.status(200).entity(rejson.toString()).build();
	}

	
	/**
	 * 技术支持申请--急修编号
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("/jxbh/{data}")
	@Produces("application/json")
	public Response getgzlxList(
			@PathParam("data") JSONObject data) throws JSONException {
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>技术支持申请--急修编号");
		
		String userid=(String) data.get("userid");
		String calloutMasterNo=CommonUtil.URLDecoder_decode((String) data.get("calloutMasterNo")); 
		
		try{
			session = HibernateUtil.getSession();
			//0 已转派，1 已接收，2 已到场，3 已停梯，4 已完工，5 已录入，6 已审核，7 已关闭，8 安全经理审核
			String sql="select cm from CalloutMaster cm,CalloutProcess cp "
					+ "where cm.calloutMasterNo=cp.calloutMasterNo "
					+ "and cm.handleStatus in ('2','3') "
					+ "and cp.assignObject2 ='"+userid+"'";
			if(calloutMasterNo!=null && !calloutMasterNo.equals("")){
				sql+=" and cm.calloutMasterNo like '%"+calloutMasterNo+"%'";
			}
			
			List list =session.createQuery(sql).list();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					CalloutMaster cNo=(CalloutMaster) list.get(i);
					JSONObject jsonObject = new JSONObject(); 
					jsonObject.put("calloutMasterNo", cNo.getCalloutMasterNo());
					jsonObject.put("elevatorno", cNo.getElevatorNo());
					jobiArray.put(i, jsonObject);
				}
			   }
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
      		rejson.put("data",CommonUtil.Pagination(data, jobiArray));//调用Pagination函数 实现点击"更多"的功能
		}catch(Exception ex){
			ex.printStackTrace();
		} finally {
            try {
            	if(session!=null){
            		session.close();
            	}
            } catch (Exception hex) {
                DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
            }

        }
		
		return Response.status(200).entity(rejson.toString()).build();
	}
	
}
