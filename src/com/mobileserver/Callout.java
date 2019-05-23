package com.mobileserver;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.stream.FileImageInputStream;
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

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.hibernate.basedata.hotlinefaulttype.HotlineFaultType;
import com.gzunicorn.hibernate.basedata.hotlinemotherboardtype.HotlineMotherboardType;
import com.gzunicorn.hibernate.hotlinemanagement.calloutmaster.CalloutMaster;
import com.gzunicorn.hibernate.hotlinemanagement.calloutprocess.CalloutProcess;
import com.gzunicorn.hibernate.hotlinemanagement.smshistory.SmsHistory;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.struts.action.xjsgg.SmsService;
import com.gzunicorn.struts.action.xjsgg.XjsggAction;

/**
 * 手机APP端调用，故障处理类
 * @author Crunchify
 */
@Path("/Callout")
public class Callout {
	
	BaseDataImpl bd = new BaseDataImpl();
	
    /**  
	 * 急修作业--信息列表
	 * @param JSONObject data
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("/sxlist/{data}")
	@Produces("application/json")
	public Response getCalloutList(
			@PathParam("data") JSONObject data) throws JSONException {
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		String jsonSrt="";
		
		System.out.println(">>>>>>>>急修作业--信息列表");
		try{
			
			String userid =(String) data.get("userid");
			String workstardate=(String) data.get("workstardate");
			String workenddate =(String) data.get("workenddate");
			
			if(workstardate==null || workstardate.trim().equals("")){
				workstardate=CommonUtil.getNowTime("yyyy-MM-dd");
			}
			if(workenddate==null || workenddate.trim().equals("")){
				workenddate=CommonUtil.getNowTime("yyyy-MM-dd");
			}
			session = HibernateUtil.getSession();

			String sql="select c.* from app_sxList c where c.assignobject = '"+userid.trim()+"'";
			if (workstardate != null && !"".equals(workstardate)) {
				sql += " and c.repairTime >= '"+workstardate.trim()+" 00:00:00'";
			}
			if (workenddate != null && !"".equals(workenddate)) {
				sql += " and c.repairTime <= '"+workenddate.trim()+" 99:99:99'";
			}
			
			sql +=" order by c.repairTime desc";
			
			//System.out.println(sql);
			
			Query query = session.createSQLQuery(sql);
            ArrayList cpList = (ArrayList) query.list();

            if (cpList != null && cpList.size()>0 ) {
            	
            	for(int i=0;i<cpList.size();i++){
            	
                  Object[] objects=(Object[]) cpList.get(i);
                  String calloutMasterNo =(String) objects[0];
                  String elevatorNo =(String) objects[1];
                  String companyName =(String) objects[2];
                  String projectAddress =(String) objects[3];
                  String isTrap =(String) objects[4];
                  String repairTime =(String) objects[5];
                  String submitType =(String) objects[6];
            	  String handleStatus =(String) objects[7];
            	  String isgzbgs =(String) objects[8];
            	  String assignobject =(String) objects[9];
            	  String stoprem =(String) objects[10];
            	  JSONObject jsonObject = new JSONObject(); 
             	 
            	  if(submitType.trim()=="Y"||submitType.trim().equals("Y")){
            	  
             	if(handleStatus!=null&& !handleStatus.trim().trim().equals("")){
             		if(handleStatus.trim().equals("0")){
             			jsonSrt="[{'name':'接收','url':'http://127.0.0.1/接收'},{'name':'转派','url':'http://127.0.0.1/转派'}]";
               		    JSONArray nextstate = new JSONArray(jsonSrt);
               		    jsonObject.put("handlestatus","未接收");
               		    jsonObject.put("nextstate",nextstate);
	             	 }else if(handleStatus.trim().equals("1")){
	             		jsonSrt="[{'name':'到场','url':'http://127.0.0.1/到场'}]";
	           		    JSONArray nextstate = new JSONArray(jsonSrt);
	           		    jsonObject.put("handlestatus","已接收");
	           		    jsonObject.put("nextstate",nextstate);
	             	 }else if(handleStatus.trim().equals("2")){
	             		jsonSrt="[{'name':'停梯','url':'http://127.0.0.1/停梯'},{'name':'完工','url':'http://127.0.0.1/完工'}]";
	           		    JSONArray nextstate = new JSONArray(jsonSrt);
	           		    jsonObject.put("handlestatus","已到场");
	           		    jsonObject.put("nextstate",nextstate);
	             	 }else if(handleStatus.trim().equals("3")){
	             		jsonSrt="[{'name':'完工','url':'http://127.0.0.1/完工'}]";
	             		 JSONArray nextstate = new JSONArray(jsonSrt);
	             		 jsonObject.put("handlestatus","已停梯"); 
	             		 jsonObject.put("nextstate",nextstate);
	             	 }else if(handleStatus.trim().equals("4")&&(isgzbgs==null||isgzbgs.trim().equals(""))){
	             		 jsonSrt="[{'name':'录入报告书','url':'http://127.0.0.1/录入报告书'}]";
	             		 JSONArray nextstate = new JSONArray(jsonSrt);
	             		 jsonObject.put("handlestatus","已完工"); 
	             		 jsonObject.put("nextstate",nextstate); 
	             	 }else{
	             		jsonSrt="[]";
	             		 JSONArray nextstate = new JSONArray(jsonSrt);
	             		 jsonObject.put("handlestatus","已完工"); 
	             		 jsonObject.put("nextstate",nextstate); 
	             	 }
             		}else{
             		jsonSrt="[{'name':'接收','url':'http://127.0.0.1/接收'},{'name':'转派','url':'http://127.0.0.1/转派'}]";
           		    JSONArray nextstate = new JSONArray(jsonSrt);
           		    jsonObject.put("handlestatus","未接收");
           		    jsonObject.put("nextstate",nextstate);
             		}
            	 }else{
            		 jsonSrt="[{'name':'修改','url':'http://127.0.0.1/修改'},{'name':'提交','url':'http://127.0.0.1/提交'}]";
            		    JSONArray nextstate = new JSONArray(jsonSrt);
            		    jsonObject.put("handlestatus","未提交");
            		    jsonObject.put("nextstate",nextstate); 
            	 }
            	  
            	 jsonObject.put("calloutmasterno", calloutMasterNo.trim());
            	 jsonObject.put("elevatorno", elevatorNo.trim());
            	 jsonObject.put("companyName", companyName.trim());
            	 jsonObject.put("projectaddress",projectAddress.trim());
            	 
            	 if(isTrap.equals("Y")){
            		 jsonObject.put("istrap","困人");
            	 }else{
            		 jsonObject.put("istrap","非困人");
            	 }
             	 jsonObject.put("repairtime", repairTime.trim().subSequence(0, 10));
            	 jsonObject.put("stoprem",stoprem);
             	 
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
	 * 急修作业--详细信息
	 * @param String userid
	 * @param String trno
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("/sxdetail/{userid}/{trno}")
	@Produces("application/json")
	public Response getCalloutDisplay(
			@PathParam("userid") String userid,
			@PathParam("trno") String trno) throws JSONException {
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>急修作业--详细信息");
		try{
			session = HibernateUtil.getSession();
			String sql="select cp,cm,lu.username,s.storagename "
					+ "from CalloutProcess cp,CalloutMaster cm, "
					+ "Loginuser lu,Storageid s "
					+ "where s.storageid=cm.maintStation and cp.assignObject2=lu.userid "
					+ "and cp.calloutMasterNo=cm.calloutMasterNo "
					+ "and (cp.assignObject2='"+userid.trim()+"' or cm.assignObject ='"+userid.trim()+"') "
					+ "and cm.calloutMasterNo = '"+trno+"'";
			Query query = session.createQuery(sql);
            ArrayList cpList = (ArrayList) query.list();
             //cpList是否有值
            if (cpList != null && cpList.size()>0 ) {
            	Object[] objects=(Object[]) cpList.get(0);
                CalloutProcess cp=(CalloutProcess) objects[0];
                CalloutMaster cm=(CalloutMaster) objects[1];
          	  	JSONObject jsonObject = new JSONObject(); 
          	 
			jsonObject.put("calloutmasterno", cm.getCalloutMasterNo());
			jsonObject.put("handlestatus", cm.getHandleStatus());
			jsonObject.put("repairtime", cm.getRepairTime());
			
			if(cm.getRepairMode()=="1"||"1".equals(cm.getRepairMode())){
				jsonObject.put("repairmode", "被动急修");
			}
			if(cm.getRepairMode()=="2"||"2".equals(cm.getRepairMode())){
				jsonObject.put("repairmode", "主动急修");
			}
			if(cm.getServiceObjects()=="1"||"1".equals(cm.getServiceObjects())){
				jsonObject.put("serviceobjects", "自保电梯");
			}
			if(cm.getServiceObjects()=="2"||"2".equals(cm.getServiceObjects())){
				jsonObject.put("serviceobjects", "外揽电梯");
			}
			if(cm.getServiceObjects()=="3"||"3".equals(cm.getServiceObjects())){
				jsonObject.put("serviceobjects", "非我方保养");
			}
			if(cm.getServiceObjects()=="4"||"4".equals(cm.getServiceObjects())){
				jsonObject.put("serviceobjects", "技术支持");
			}
			
			String companyName=bd.getName("Customer", "companyName", "companyId", cm.getCompanyId());
			if(companyName==null || companyName.trim().equals("")){
				companyName=cm.getCompanyId();
			}
					
			jsonObject.put("elevatorno", cm.getElevatorNo());
			jsonObject.put("repairuser", cm.getRepairUser());
			jsonObject.put("repairtel", cm.getRepairTel());
			jsonObject.put("companyid", cm.getCompanyId());
			jsonObject.put("companyName", companyName);
			jsonObject.put("projectaddress", cm.getProjectAddress());
			jsonObject.put("salescontractno", cm.getSalesContractNo());
			jsonObject.put("elevatorparam", cm.getElevatorParam());
			jsonObject.put("maintstation", cm.getMaintStation());
			jsonObject.put("storageName", (String)objects[3]);
			jsonObject.put("assignobjectName",(String)objects[2]);
			jsonObject.put("assignedMainStation", cm.getMaintStation());
			jsonObject.put("salesContractNo", cm.getSalesContractNo());
			jsonObject.put("operdate", cm.getOperDate());		
			jsonObject.put("assignobject", cp.getAssignObject2());
			jsonObject.put("phone", cm.getPhone());
			String tel=cp.getTurnSendTel();
			if(tel!=null){
			jsonObject.put("phone", tel);	
		    }
			String istrap=cm.getIsTrap();
			if(istrap!=null && istrap.trim().equals("Y")){
				istrap="困人";
			}else{
				istrap="非困人";
			}
			jsonObject.put("istrap",istrap);
			jsonObject.put("stoprem",cp.getStopRem());//停梯备注
			
			if(cp.getArriveDate()==null){
				jsonObject.put("arrivedate2", "");
			}else{
				jsonObject.put("arrivedate2", cp.getArriveDate());
			}
			if(cp.getArriveTime()==null){
				jsonObject.put("arrivetime2", "");
			}else{
				jsonObject.put("arrivetime2", cp.getArriveTime());
			}
			
			jsonObject.put("repairdesc", cm.getRepairDesc());
			jsonObject.put("auser", (String)objects[3]);
			jsonObject.put("atime", cp.getArriveDateTime());
			jsonObject.put("areamrk", cp.getArriveLocation());
			jsonObject.put("suser", (String)objects[3]);
			jsonObject.put("stime", cp.getStopTime());
			jsonObject.put("sreamrk", cp.getStopLocation());
			jsonObject.put("fuser", (String)objects[3]);
			jsonObject.put("ftime", cp.getCompleteTime());
			jsonObject.put("fremark", cp.getFninishLocation());
			jsonObject.put("isCanTurn", "N");
			 if(cm.getHandleStatus()==null||cm.getHandleStatus().trim().equals("")||cm.getHandleStatus().trim().equals("0")){
				jsonObject.put("isCanTurn", "Y");
//				jsonObject.put("assignObject", "");
//				jsonObject.put("phone", "");
//				jsonObject.put("assignobjectName","");
			 }
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
	 * 急修作业--新增急修任务(弹出框)
	 * @param JSONObject data
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("/companList/{data}")
	@Produces("application/json")
	public Response getCompanList(
			@PathParam("data") JSONObject data) throws JSONException {
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>急修作业--新增急修任务(保修单位弹出框)");
		
		try{
			session = HibernateUtil.getSession();
			String userid=(String) data.get("userid");
			String elevatorNo=CommonUtil.URLDecoder_decode((String) data.get("elevatorno"));
            String stat=(String) data.get("stat");
            String companyid=CommonUtil.URLDecoder_decode((String) data.get("companyid")); 
            String sqlsrt="";
            
            if(stat=="elevatorno"||stat.equals("elevatorno")){
            	sqlsrt=" distinct b.elevatorNo,b.elevatorParam,b.salesContractNo,b.assignedMainStation,"
            			+ "d.storageName,isnull(ec.rem,'') as MaintAddress,";
			}else{
				
				sqlsrt=" distinct ";
			}
            //已下达保养站，排除是否接收
			String sql="select "+sqlsrt+"a.companyId,c.companyName"
					+ " from MaintContractMaster as a,Customer as c,"
					+ "MaintContractDetail as b left join Storageid as d on b.assignedMainStation=d.storageId "
					+ "left join ElevatorCoordinateLocation ec on ec.ElevatorNo=b.ElevatorNo "
					+ "where a.billNo=b.billNo and (a.contractStatus='XB'or a.contractStatus='ZB' ) "
					//+ "and isnull(b.maintPersonnel,'') != ''  "
					+ "and a.companyId =c.companyId "
					+ "and b.AssignedMainStation like "
					+ "(select case when s.ParentStorageID=0 then s.StorageID else s.ParentStorageID end " +
					"from LoginUser l,Storageid s where l.StorageID=s.StorageID and l.UserID='"+userid.trim()+"')";
			
			if(companyid!=null && !companyid.equals("") && !companyid.equals("undefined")){
				sql +=" and (a.companyId like '%"+companyid.trim()+"%' or c.companyName like '%"+companyid.trim()+"%'" +
						" or b.ElevatorNo like '%"+companyid.trim()+"%' )";
			}
			if(elevatorNo!=null && !elevatorNo.equals("")){
				sql +=" and b.elevatorNo like '%"+elevatorNo+"%'";
			}
			
			//sSystem.out.println(sql);
			
			List list =session.createSQLQuery(sql).list();
			if(list!=null&&list.size()>0){
			
				for(int i=0;i<list.size();i++){
					Object[] objects=(Object[]) list.get(i);
					JSONObject jsonObject = new JSONObject(); 
					
					if(stat=="elevatorno"|| stat.equals("elevatorno")){
						
						jsonObject.put("elevatorNo", objects[0]);
						jsonObject.put("elevatorParam", objects[1]);
						jsonObject.put("salesContractNo", objects[2]);
						jsonObject.put("assignedMainStation", objects[3]);
						jsonObject.put("storageName", objects[4]);
						jsonObject.put("addr", objects[5]);

					}else{
						jsonObject.put("companyId", objects[0]);
						jsonObject.put("companyName", objects[1]);
					}
					jobiArray.put(i, jsonObject);
					
				}
				
			   }
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
            //rejson.put("data", jobiArray);
      		rejson.put("data", CommonUtil.Pagination(data, jobiArray));//调用Pagination函数 实现点击"更多"的功能
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
	
	/**  
	 * 急修作业--转派与派工
	 * @param String userid
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("/sxzhuanpai/{userid}")
	@Produces("application/json")
	public Response sxzhuanpai(
			@PathParam("userid") String userid) throws JSONException {
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		try{
			System.out.println(">>>>>>>>急修作业--选择维保工");
			session = HibernateUtil.getSession();
			int p=0;
			
			//维保站长A49，维保工A50,维保经理 A03，维修技术员A53
			String sql="select l.UserID,l.UserName,l.Phone,l.StorageID,s.StorageName,r.rolename "
					+ "from LoginUser l,StorageID s,role r where l.enabledflag='Y' and l.RoleID in('A49','A50','A03','A53') "
					+ "and s.StorageID=l.StorageID and l.roleid=r.roleid and l.StorageID like ("
					+ "select case when s.ParentStorageID=0 then s.StorageID else s.ParentStorageID "
					+ "end  from StorageID s where s.StorageID like "
					+ "(select StorageID from LoginUser where UserID='"+userid+"'))+'%' ";
			List list =session.createSQLQuery(sql).list();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Object[] objects=(Object[]) list.get(i);
					JSONObject jsonObject = new JSONObject(); 
						jsonObject.put("dutyuserid", objects[0]);
						jsonObject.put("dutyusername", objects[1]);
						jsonObject.put("linkphone", objects[2]);
						jsonObject.put("storageid", objects[3]);
						jsonObject.put("storagename", objects[4]);
						jsonObject.put("rolename", objects[5]);
						jobiArray.put(p, jsonObject);
						p++;
				}
			}
			
			//技术质量安全经理 A11 
			String sql2="select a.UserID,a.UserName,a.Phone,a.StorageID,s.StorageName,r.rolename  "
					+ "from LoginUser a,LoginUser b,StorageID s,role r "
					+ "where a.RoleID='A11' and a.roleid=r.roleid  and a.StorageID=s.StorageID "
					+ "and a.grcid=b.grcid and a.EnabledFlag='Y' and b.UserID='"+userid+"'";
			List list2 =session.createSQLQuery(sql2).list();
			if(list2!=null && list2.size()>0){
				for(int i=0;i<list2.size();i++){
					Object[] objects=(Object[]) list2.get(i);
					JSONObject jsonObject = new JSONObject(); 
					jsonObject.put("dutyuserid", objects[0]);
					jsonObject.put("dutyusername", objects[1]);
					jsonObject.put("linkphone", objects[2]);
					jsonObject.put("storageid", objects[3]);
					jsonObject.put("storagename", objects[4]);
					jsonObject.put("rolename", objects[5]);
					jobiArray.put(p, jsonObject);
					p++;
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
            } catch (Exception hex) {
                DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
            }

        }
		
		return Response.status(200).entity(rejson.toString()).build();
	}
	
	/**  
	 * 急修作业--处理操作
	 * @param JSONObject data
	 * @return
	 * @throws JSONException
	 */
	@POST
	@Path("/sxscens")
	@Produces("application/json")
	public Response sxscens(@FormParam("data") JSONObject data) throws JSONException {
		
		Session session = null;
		Transaction tx = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		CalloutMaster cm = null;
		CalloutProcess cp=null;
		
		System.out.println(">>>>>>>>急修作业--保存");
		
		try{
			session=HibernateUtil.getSession();
			tx=session.beginTransaction();
			String act=(String) data.get("act");//操作
			String calloutmasterno=(String) ((JSONObject) data.get("tr")).get("calloutmasterno");//: 急修单号,
			String userid=(String) data.get("userid");//录入人
			String repairuser=(String) ((JSONObject) data.get("tr")).get("repairuser");//: 报修人,
			String repairtel=(String) ((JSONObject) data.get("tr")).get("repairtel");//="";// 报修人电话,
			
			cm =(CalloutMaster) session.get(CalloutMaster.class, calloutmasterno);
			cp =(CalloutProcess) session.get(CalloutProcess.class, calloutmasterno);
			
			if(act.equals("保存")){
				//DebugUtil.println("手机创建急修单: "+data.toString());
				
				//手机新建急修
				cm = new CalloutMaster();
				cp = new CalloutProcess();
				String calloutMasterNo=XjsggAction.genCalloutMasterNum();//新建急修单号
	            cm.setCalloutMasterNo(calloutMasterNo);
	            cp.setCalloutMasterNo(calloutMasterNo);

	            cm_cpSave(cm,cp,data);
	            cm.setIsSendSms2("N");//不发送安抚短信
		        cm.setSubmitType("Y");
		        cm.setR1("Y");//是否手机创建
		        
		        //默认接收
		        //cp.setAssignUser(userid);
				//cp.setAssignTime(CommonUtil.getNowTime());
				//cp.setAssignUserTel((String) ((JSONObject) data.get("tr")).get("phone"));
				//cm.setHandleStatus("1");

	            session.save(cm);
	            session.save(cp);
	            
	            /****************************发送短信给维保工 开始**********************************/
		        String istrap=cm.getIsTrap();// 是否困人(N非困人,Y困人)
		        String istraptext="非困人";
            	if(istrap!=null && istrap.equals("Y")){
            		istraptext="困人";
            	}
            	//发送短信给维保工
            	System.out.println(">>>发送短信给维保工");
            	String smsmes="困人情况："+istraptext+"，电梯编号："+cm.getElevatorNo()+"，项目名称及楼栋号："+cm.getProjectAddress()+"。您保养的电梯有故障，请及时处理！ [西继迅达]";
            	boolean issms=SmsService.sendSMS(istraptext,cm.getElevatorNo(),cm.getProjectAddress(),cm.getPhone());

	            SmsHistory sh=new SmsHistory();
				sh.setSmsContent(smsmes);
				sh.setSmsSendTime(CommonUtil.getNowTime());
				sh.setSmsTel(cm.getPhone());
				if(issms){
					sh.setFlag(1);
				}else{
					sh.setFlag(0);	
				}
				session.save(sh);
				/****************************发送短信给维保工 结束**********************************/
			}else 
				/*流程处理操作*/
			if(act.equals("接收")||act=="接收"){
				cp.setAssignUser(userid);
				cp.setAssignTime(CommonUtil.getNowTime());
				if(cm.getAssignObject().equals(cp.getAssignObject2())){
				   cp.setAssignUserTel(cm.getPhone());
				}else{
					cp.setAssignUserTel(cp.getTurnSendTel());
				}
				
				cm.setHandleStatus("1");
				session.update(cp);
				session.update(cm);
			}else if(act.equals("转派")||act=="转派"){
				cp.setTurnSendTime(CommonUtil.getNowTime());
				cp.setTurnSendId(repairuser);
				cp.setTurnSendTel(repairtel);
				cp.setAssignObject2(repairuser);
				cm.setHandleStatus("0");
				session.update(cp);
			}else if(act.equals("到场")||act=="到场"){

				Double gpslon=(Double) ((JSONObject) data.get("tr")).get("gpslon");
				Double gpslat=(Double) ((JSONObject) data.get("tr")).get("gpslat");
				Double lon=(Double) ((JSONObject) data.get("tr")).get("lon");
				Double lat=(Double) ((JSONObject) data.get("tr")).get("lat");
				String addr=(String) ((JSONObject) data.get("tr")).get("addr");
				
				String arrivedate=(String) ((JSONObject) data.get("tr")).get("arrivedate");//到现场日期(页面日期)
				String arrivetime=(String) ((JSONObject) data.get("tr")).get("arrivetime");//到现场时间(页面时间)
				
				cp.setArriveDate(arrivedate);
				cp.setArriveTime(arrivetime);
				cp.setArriveDateTime(CommonUtil.getNowTime());//系统后台的到场时间
				cp.setArriveLongitude(lon);
				cp.setArriveLatitude(lat);
				cp.setArriveLongitudeGPS(gpslon);//GPS原始坐标经度
				cp.setArriveLatitudeGPS(gpslat);//GPS原始坐标纬度
				cp.setArriveLocation(addr);	
				cm.setHandleStatus("2");
				session.update(cp);
				session.update(cm);
			}else if(act.equals("停梯")||act=="停梯"){
				
				Double gpslon=(Double) ((JSONObject) data.get("tr")).get("gpslon");
				Double gpslat=(Double) ((JSONObject) data.get("tr")).get("gpslat");
				Double lon=(Double) ((JSONObject) data.get("tr")).get("lon");
				Double lat=(Double) ((JSONObject) data.get("tr")).get("lat");
				String addr=(String) ((JSONObject) data.get("tr")).get("addr");
				String stoprem=(String) ((JSONObject) data.get("tr")).get("stoprem");//停梯备注
				
				cp.setStopTime(CommonUtil.getNowTime());
				cp.setStopLongitude(lon);
				cp.setStopLatitude(lat);
				cp.setStopLongitudeGPS(gpslon);//GPS原始坐标经度
				cp.setStopLatitudeGPS(gpslat);//GPS原始坐标纬度
				cp.setStopLocation(addr);
				cp.setIsStop("Y");
				cp.setStopRem(stoprem);//停梯备注
				cm.setHandleStatus("3");
				session.update(cp);
				session.update(cm);
			}else if(act.equals("完工")||act=="完工"){

				Double gpslon=(Double) ((JSONObject) data.get("tr")).get("gpslon");
				Double gpslat=(Double) ((JSONObject) data.get("tr")).get("gpslat");
				Double lon=(Double) ((JSONObject) data.get("tr")).get("lon");
				Double lat=(Double) ((JSONObject) data.get("tr")).get("lat");
				String addr=(String) ((JSONObject) data.get("tr")).get("addr");

				cp.setCompleteTime(CommonUtil.getNowTime());
				cp.setFninishLongitude(lon);
				cp.setFninishLatitude(lat);
				cp.setFninishLongitudeGPS(gpslon);//GPS原始坐标经度
				cp.setFninishLatitudeGPS(gpslat);//GPS原始坐标纬度
				cp.setFninishLocation(addr);
				cm.setHandleStatus("4");
				session.update(cp);
				session.update(cm);
			}
			
			tx.commit();
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
      		rejson.put("data", jobiArray);
		}catch(Exception ex){
			tx.rollback();
			ex.printStackTrace();
			json.put("code", "400");
  			json.put("info", "保存失败");
            rejson.put("status", json);
      		rejson.put("data", jobiArray);
      		
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
	
	/**  
	 * 用于新建修改保存数据用
	 * @param CalloutMaster cm
	 * @param CalloutProcess cp
	 * @param JSONObject data
	 * @throws Exception
	 */
	public static void cm_cpSave(CalloutMaster cm,CalloutProcess cp,JSONObject data) throws Exception{
		String userid=(String) data.get("userid");//录入人
		String act=(String) data.get("act");//操作
		String repairmode=(String) data.get("repairmode"); //报修方式
		String serviceobjects=(String) data.get("serviceobjects");//服务对象
		String istrap=(String) data.get("istrap");// 是否困人(N非困人,Y困人)
		JSONObject tr=(JSONObject) data.get("tr");
		String repairtime=(String) tr.get("repairtime");//: 报修时间,
		String repairuser=(String) tr.get("repairuser");//: 报修人,
		String repairtel=(String) tr.get("repairtel");//="";// 报修人电话,
		String companyid=(String) tr.get("companyid");// 报修单位代码,
		String projectaddress=(String) tr.get("projectaddress");// 项目地址,
		String elevatorno=(String) tr.get("elevatorno");// 电梯编号,

		String salescontractno=(String) tr.get("salesContractNo");// 销售合同号
		//进行解码,下面进行解密  
		byte[] salebyte=new BASE64Decoder().decodeBuffer(salescontractno);
		String salescontractno2=new String(salebyte);
		
		String elevatorparam=(String) tr.get("elevatorparam");// 规格型号,
		//进行解码,下面进行解密  
		byte[] elebyte=new BASE64Decoder().decodeBuffer(elevatorparam);
		String elevatorparam2=new String(elebyte);
		
		String maintstation=(String) tr.get("assignedMainStation");// 所属维保站代码,
		String assignobject=(String) tr.get("assignobject");// 派工对象代码,
		String phone=(String) tr.get("phone");// 电话,
		String repairdesc=(String) tr.get("repairdesc");// 报修描述
        
		cm.setAssignObject(assignobject);
		cm.setPhone(phone);
		cm.setOperId(userid);
        cm.setOperDate(CommonUtil.getNowTime());
        cm.setRepairTime(repairtime);
        cm.setRepairMode(repairmode);;
        cm.setRepairUser(repairuser);
        cm.setRepairTel(repairtel);
        cm.setServiceObjects(serviceobjects);
        cm.setCompanyId(companyid);
        cm.setProjectAddress(projectaddress);
        cm.setSalesContractNo(salescontractno2);
        cm.setElevatorNo(elevatorno);
        cm.setElevatorParam(elevatorparam2);
        cm.setMaintStation(maintstation);
        cm.setIsTrap(istrap);
        cm.setRepairDesc(repairdesc);
        cm.setHandleStatus("");//处理状态
        
        cp.setAssignObject2(assignobject);
        
	}
	
    /** 
     * 故障报告书--信息列表
     * @param data
     * @return
     * @throws JSONException
     */
	@GET
	@Path("/gzlist/{data}")
	@Produces("application/json")
	public Response getCalloutbgList(
			@PathParam("data") JSONObject data) throws JSONException {
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		String jsonSrt="";
		
		System.out.println(">>>>>>>>故障报告书--信息列表");
		try{
			
			String userid =(String) data.get("userid");
			String startrptime=(String) data.get("startrptime");
			String endrptime =(String) data.get("endrptime");
			
			if(startrptime==null || startrptime.trim().equals("")){
				startrptime=CommonUtil.getNowTime("yyyy-MM-dd");
			}
			if(endrptime==null || endrptime.trim().equals("")){
				endrptime=CommonUtil.getNowTime("yyyy-MM-dd");
			}

			session = HibernateUtil.getSession();
			
			String sql="select c.* from app_sxList c where c.assignobject = '"+userid.trim()+"'" +
					" and c.handleStatus in ('4','5','6','7')";
			if (startrptime != null && !"".equals(startrptime)) {
				sql += " and c.repairTime >= '"+startrptime.trim()+" 00:00:00'";
			}
			if (endrptime != null && !"".equals(endrptime)) {
				sql += " and c.repairTime <= '"+endrptime.trim()+" 99:99:99'";
			}
			
			sql +=" order by c.repairTime desc";
			//System.out.println(sql);
			Query query = session.createSQLQuery(sql);
            ArrayList cpList = (ArrayList) query.list();
             //mwpList是否有值
            if (cpList != null && cpList.size()>0 ) {
            	
            	for(int i=0;i<cpList.size();i++){
            	
	              Object[] objects=(Object[]) cpList.get(i);
	              String calloutMasterNo =(String) objects[0];
	              String elevatorNo =(String) objects[1];
	              String companyName =(String) objects[2];
	              String projectAddress =(String) objects[3];
	              String isTrap =(String) objects[4];
	              String repairTime =(String) objects[5];
	              String submitType =(String) objects[6];
              	  String handleStatus =(String) objects[7];
              	  String isgzbgs =(String) objects[8];
            	  String assignObject =(String) objects[9];
            	  
            	  JSONObject jsonObject = new JSONObject(); 
            	   if(handleStatus.trim().equals("4")){
                    	 jsonSrt="[{'name':'保存','url':'http://127.0.0.1/保存'},{'name':'提交','url':'http://127.0.0.1/提交'}]";
	             		 JSONArray nextstate = new JSONArray(jsonSrt);
	             		 jsonObject.put("handlestatus","未提交"); 
	             		 jsonObject.put("nextstate",nextstate); 
            	  }else if(handleStatus.trim().equals("5")||handleStatus.trim()=="5"){
                 	     jsonSrt="[]";
	             		 JSONArray nextstate = new JSONArray(jsonSrt);
	             		 jsonObject.put("handlestatus","已提交"); 
	             		 jsonObject.put("nextstate",nextstate); 
         	      }else if(handleStatus.trim().equals("6")||handleStatus.trim()=="6"){
                 	     jsonSrt="[]";
	             		 JSONArray nextstate = new JSONArray(jsonSrt);
	             		 jsonObject.put("handlestatus","已审核"); 
	             		 jsonObject.put("nextstate",nextstate); 
         	      }else{
         	    	     jsonSrt="[]";
             		     JSONArray nextstate = new JSONArray(jsonSrt);
             		     jsonObject.put("handlestatus","已关闭"); 
             		     jsonObject.put("nextstate",nextstate); 
         	      }
            	  
            	 jsonObject.put("calloutmasterno", calloutMasterNo.trim());
              	 jsonObject.put("elevatorno", elevatorNo.trim());
              	 jsonObject.put("companyName", companyName.trim());
              	 jsonObject.put("projectaddress",projectAddress.trim());
                 if(isTrap.equals("Y")){
           		 jsonObject.put("istrap","困人");
           	     }else{
           		 jsonObject.put("istrap","非困人");
           	     }
               	 jsonObject.put("repairtime", repairTime.trim().subSequence(0, 10));
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
	 * 故障报告书--详细信息
	 * @param userid
	 * @param trno
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("/gzdetail/{userid}/{trno}")
	@Produces("application/json")
	public Response getCalloubgtDisplay(
			@PathParam("userid") String userid,
			@PathParam("trno") String trno) throws JSONException {
		
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>故障报告书--详细信息");
		
		try{
			session = HibernateUtil.getSession();
			String sql="select cp,cm,lu.username,s.storagename "
					+ "from CalloutProcess cp,CalloutMaster cm,"
					+ "Loginuser lu,Storageid s "
					+ "where s.storageid=cm.maintStation and cp.assignObject2=lu.userid "
					+ "and cp.calloutMasterNo=cm.calloutMasterNo "
					+ "and (cp.assignObject2='"+userid.trim()+"' or cm.assignObject ='"+userid.trim()+"') "
					+ "and cm.calloutMasterNo = '"+trno+"'";
			Query query = session.createQuery(sql);
            ArrayList cpList = (ArrayList) query.list();
             //cpList是否有值
	         if (cpList != null && cpList.size()>0 ) {
	            Object[] objects=(Object[]) cpList.get(0);
	            CalloutProcess cp=(CalloutProcess) objects[0];
	            CalloutMaster cm=(CalloutMaster) objects[1];
	          	JSONObject jsonObject = new JSONObject(); 
	          	
	          	String companyName=bd.getName("Customer", "companyName", "companyId", cm.getCompanyId());
				if(companyName==null || companyName.trim().equals("")){
					companyName=cm.getCompanyId();
				}
	          	 
				jsonObject.put("calloutmasterno", cm.getCalloutMasterNo());
				jsonObject.put("elevatorno", cm.getElevatorNo());
				jsonObject.put("companyid", cm.getCompanyId());
				jsonObject.put("companyName", companyName);
				jsonObject.put("projectaddress", cm.getProjectAddress());
				jsonObject.put("assignobjectName",(String)objects[2]);
				jsonObject.put("phone", cp.getTurnSendTel());
				jsonObject.put("bhauditrem", cm.getBhAuditRem());//审核驳回意见
				
				//当设备编号为空时，默认为电梯编号
				String deviceid=cp.getDeviceId();
				if(deviceid==null || deviceid.trim().equals("")){
					deviceid=cm.getElevatorNo();
				}
				jsonObject.put("deviceId", deviceid);
				jsonObject.put("faultCode", cp.getFaultCode());
				jsonObject.put("faultStatus", cp.getFaultStatus());
				jsonObject.put("hmtId", cp.getHmtId());
				jsonObject.put("newFittingName", cp.getNewFittingName());
				jsonObject.put("hftId", cp.getHftId());
				jsonObject.put("money", cp.getMoney());
				jsonObject.put("processdesc", cp.getProcessDesc());
				jsonObject.put("servicerem", cp.getServiceRem());
				jsonObject.put("hmtname",  bd.getName(session, "HotlineMotherboardType", "hmtName", "hmtId", cp.getHmtId()));
				//jsonObject.put("hftdesc", bd.getName(session, "HotlineFaultType", "hftDesc", "hftId", cp.getHftId()));
				jsonObject.put("istoll", cp.getIsToll());
				jsonObject.put("isgzbgs", cp.getIsgzbgs());
				
				String hftid=cp.getHftId();//故障类型
	           	 String hftdesc="";
	           	 if(hftid!=null && !hftid.trim().equals("")){
	           		 String sqls="select a from HotlineFaultType a where a.hftId in('"+hftid.replaceAll(",", "','")+"')";
	           		 List loginlist=session.createQuery(sqls).list();
	           		 if(loginlist!=null && loginlist.size()>0){
	           			 for(int l=0;l<loginlist.size();l++){
	           				HotlineFaultType hft=(HotlineFaultType)loginlist.get(l);
	           				 if(l==loginlist.size()-1){
	           					hftdesc+=hft.getHftDesc();
	           				 }else{
	           					hftdesc+=hft.getHftDesc()+",";
	           				 }
	           			 }
	           		 }
	           	 }
	           	jsonObject.put("hftdesc", hftdesc);
				/**
				String r5=cp.getR5();//急修参与人员
	           	 String r5name="";
	           	 if(r5!=null && !r5.trim().equals("")){
	           		 String sqls="select a from Loginuser a where a.userid in('"+r5.replaceAll(",", "','")+"')";
	           		 List loginlist=session.createQuery(sqls).list();
	           		 if(loginlist!=null && loginlist.size()>0){
	           			 for(int l=0;l<loginlist.size();l++){
	           				 Loginuser login=(Loginuser)loginlist.get(l);
	           				 if(l==loginlist.size()-1){
	           					 r5name+=login.getUsername();
	           				 }else{
	           					 r5name+=login.getUsername()+",";
	           				 }
	           			 }
	           		 }
	           		 
	           	 }
	           	 */
	           	 jsonObject.put("engcyuserid", cp.getR5());//急修参与人员
	           	 jsonObject.put("engcyusername", cp.getR5());//急修参与人员

				//读取下载电梯维修记录单路径
				String path="D:\\contract\\下载电梯维修记录单路径.txt";
				BufferedReader reader= new BufferedReader(new FileReader(path));
				String downloadaddr=reader.readLine();
				reader.close();
				//String downloadaddr="http://10.10.0.5:8080/XJSCRM/PrintCalloutProcessServlet?id=";//测试
				//String downloadaddr="http://www.xjelevator.com:9000/XJSCRM/PrintCalloutProcessServlet?id=";//正式
				if(downloadaddr!=null && !downloadaddr.trim().equals("")){
					jsonObject.put("downloadaddr", downloadaddr.trim()+cp.getCalloutMasterNo());//厂检通知单下载地址
				}else{
					jsonObject.put("downloadaddr", "");//厂检通知单下载地址
				}
				
				/**=====================获取图片=========================*/
				String folder = PropertiesUtil.getProperty("CalloutProcess.file.upload.folder");
				BASE64Encoder base=new BASE64Encoder();
				//客户签名
				if(cp.getCustomerSignature()!=null && !cp.getCustomerSignature().trim().equals("")){
					String filepath=folder+cp.getCustomerSignature();
					byte[] imgbyte=CommonUtil.imageToByte(filepath);//将图片转换为二进制流
					jsonObject.put("customersignature", base.encode(imgbyte));//将二进制流加密
				}else{
					jsonObject.put("customersignature", "");
				}
				//客户照片
				if(cp.getCustomerImage()!=null && !cp.getCustomerImage().trim().equals("")){
					String filepath=folder+cp.getCustomerImage();
					byte[] imgbyte=CommonUtil.imageToByte(filepath);//将图片转换为二进制流		
					jsonObject.put("customerimage", base.encode(imgbyte));//将二进制流加密
				}else{
					jsonObject.put("customerimage", "");
				}
				/**=====================获取图片=========================*/
				
				if(cm.getHandleStatus()==null || cm.getHandleStatus().trim().equals("") || cm.getHandleStatus().trim().equals("0")){
					jsonObject.put("isCanTurn", "Y");
				}
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
	 * 故障报告书--保存
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	@POST
	@Path("/gzbgadd")
	@Produces("application/json")
	public Response saveCalloutgz(@FormParam("data") JSONObject data) throws JSONException {
		
		System.out.println(">>>>>>>>故障报告书--保存");
		
		String usetid=(String) data.get("userid");
		String servicerem=(String) ((JSONObject) data.get("rp")).get("servicerem");
		String processdesc=(String) ((JSONObject) data.get("rp")).get("processdesc");//维修过程描述
		String faultStatus=(String) ((JSONObject) data.get("rp")).get("faultStatus");//故障状态
		String hftId=(String) ((JSONObject) data.get("rp")).get("hftId");
		String newFittingName=(String) ((JSONObject) data.get("rp")).get("newFittingName");
		String calloutmasterno=(String) ((JSONObject) data.get("rp")).get("calloutmasterno");
		String money=(String) ((JSONObject) data.get("rp")).get("money");
		String faultCode=(String) ((JSONObject) data.get("rp")).get("faultCode");//故障代码
		String deviceId=(String) ((JSONObject) data.get("rp")).get("deviceId");
		String istoll=(String) ((JSONObject) data.get("rp")).get("istoll");
		String hmtId=(String) ((JSONObject) data.get("rp")).get("hmtId");
		String status=(String) ((JSONObject) data.get("rp")).get("status");//0 保存，1提交

		String engcyusername=(String) ((JSONObject) data.get("rp")).get("engcyusername");//维修人员
		
		
		//故障代码用", ，"分隔
		String faultCodestr="";
		if(faultCode!=null && !faultCode.trim().equals("")){
			String[] faultCodes=faultCode.split(",");
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
		
		Session session = null;
		Transaction tx = null;
		CalloutMaster cm=null;
		CalloutProcess cp =null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		try{
			session = HibernateUtil.getSession();
			tx=session.beginTransaction();
			cm =(CalloutMaster) session.get(CalloutMaster.class, calloutmasterno);
			cp =(CalloutProcess) session.get(CalloutProcess.class, calloutmasterno);
			cp.setServiceRem(servicerem);
			cp.setProcessDesc(processdesc);
			cp.setFaultStatus(faultStatus);
			cp.setHftId(hftId);
			cp.setHmtId(hmtId);
			cp.setNewFittingName(newFittingName);
			cp.setIsToll(istoll);
			if(istoll.trim().equals("Y")){
				cp.setMoney(Double.valueOf(money));
			}
			cp.setFaultCode(faultCodestr);//故障代码
			cp.setDeviceId(deviceId);

			if(status.equals("1")){
				cm.setHandleStatus("5");
			 	cp.setIsgzbgs("Y");
			}else{
				cp.setIsgzbgs("N");	
			}
			
			cp.setR5(engcyusername);
			
			/**=======================保存图片=============================*/
			String folder = PropertiesUtil.getProperty("CalloutProcess.file.upload.folder");
			String filepath=CommonUtil.getNowTime("yyyy-MM-dd")+"/";
			//保存签名
			String customersignature=(String) ((JSONObject) data.get("rp")).get("customersignature");
			if(customersignature!=null && !customersignature.trim().equals("")){
				//customersignature=customersignature.replaceAll("data:image/jpeg;base64,", "");//去掉前缀
				String[] signatures=customersignature.split(",");
				if(signatures!=null && signatures.length>1){
					byte[] image=new BASE64Decoder().decodeBuffer(signatures[1]);
					String newfilename=calloutmasterno+"_0.jpg";
					//保存图片
					File f=new File(folder+filepath);
					f.mkdirs();
					FileOutputStream fos=new FileOutputStream(folder+filepath+newfilename);
					fos.write(image);
					fos.flush();
					fos.close();
					//保存图片信息到数据库
					cp.setCustomerSignature(filepath+newfilename);
				}
			}
			//保存拍照
			String customerimage=(String) ((JSONObject) data.get("rp")).get("customerimage");
			if(customerimage!=null && !customerimage.trim().equals("")){
				//customerimage=customerimage.replaceAll("data:image/jpeg;base64,", "");//去掉前缀
				String[] cimages=customerimage.split(",");
				if(cimages!=null && cimages.length>1){
					byte[] image=new BASE64Decoder().decodeBuffer(cimages[1]);
					String newfilename=calloutmasterno+"_1.jpg";
					//保存图片
					File f=new File(folder+filepath);
					f.mkdirs();
					FileOutputStream fos=new FileOutputStream(folder+filepath+newfilename);
					fos.write(image);
					fos.flush();
					fos.close();
					//保存图片信息到数据库
					cp.setCustomerImage(filepath+newfilename);
				}
			}
			/**=======================保存图片=============================*/
			
			session.update(cm);
			session.update(cp);
			tx.commit();
          	json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
      		rejson.put("data", jobiArray);
		}catch(Exception ex){
			tx.rollback();
			ex.printStackTrace();
			json.put("code", "400");
  			json.put("info", "NOT OK");
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

	/**
	 * 故障报告书--主板类型
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("/zblx/{data}")
	@Produces("application/json")
	public Response getzblxList(
			@PathParam("data") JSONObject data) throws JSONException {
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		String hmtName=CommonUtil.URLDecoder_decode((String) data.get("hmtname")); 
		hmtName = hmtName.replaceAll("::", "/");
		
		System.out.println(">>>>>>>>故障报告书--主板类型");
		try{
			session = HibernateUtil.getSession();
			String sql="from HotlineMotherboardType h "
					+ "where h.enabledFlag='Y' "
					+ "and (h.hmtName like '%"+hmtName+"%' or h.hmtId like '%"+hmtName+"%' ) "
					+ "order by h.hmtId ";
			//System.out.println(sql);
			List list =session.createQuery(sql).list();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					HotlineMotherboardType h=(HotlineMotherboardType) list.get(i);
					JSONObject jsonObject = new JSONObject(); 
						jsonObject.put("hmtid", h.getHmtId());
						jsonObject.put("hmtname", h.getHmtName());
						jobiArray.put(i, jsonObject);
				}
			   }
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
            //rejson.put("data",jobiArray);
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
	
	/**
	 * 故障报告书--故障类型
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("/gzlx/{data}")
	@Produces("application/json")
	public Response getgzlxList(
			@PathParam("data") JSONObject data) throws JSONException {
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		String hftdesc=CommonUtil.URLDecoder_decode((String) data.get("hftdesc")); 
		
		System.out.println(">>>>>>>>故障报告书--故障类型");
		try{
			session = HibernateUtil.getSession();
			String sql="from HotlineFaultType h "
					+ "where h.enabledFlag='Y' "
					+ "and (h.hftDesc like '%"+hftdesc+"%' or h.hftId like '%"+hftdesc+"%' ) "
					+ "order by h.hftId ";
			List list =session.createQuery(sql).list();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					HotlineFaultType h=(HotlineFaultType) list.get(i);
					JSONObject jsonObject = new JSONObject(); 
						jsonObject.put("hftid", h.getHftId());
						jsonObject.put("hftdesc", h.getHftId()+" "+h.getHftDesc());
						jobiArray.put(i, jsonObject);
				}
			   }
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
      		//rejson.put("data",jobiArray);
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
