package com.mobileserver;

/**
 * 手机APP端调用，维保作业处理类
 * @author Crunchify
 */
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.hibernate.MaintainProjectInfo;
import com.gzunicorn.hibernate.basedata.elevatorcoordinatelocation.ElevatorCoordinateLocation;
import com.gzunicorn.hibernate.maintainprojectinfowork.MaintainProjectInfoWork;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplandetail.MaintenanceWorkPlanDetail;
import com.gzunicorn.hibernate.sysmanager.Loginuser;

@Path("/Maintcontract")
public class Maintcontract {
	/**
	 * 维保作业--信息列表
	 * @param JSONObject data
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("/bylist/{data}")
	@Produces("application/json")
	public Response bylist(
			@PathParam("data") JSONObject data) throws JSONException {
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		String jsonSrt="";
		
		System.out.println(">>>>>>>>保养作业-列表");
		
		try{
			String userid = (String) data.get("userid");
			String state = (String) data.get("state");
			String sdate = (String) data.get("workstardate");
			String edate= (String) data.get("workenddate");
			
			if(sdate==null || sdate.trim().equals("")){
				sdate=CommonUtil.getNowTime("yyyy-MM-dd");
			}
			if(edate==null || edate.trim().equals("")){
				edate=CommonUtil.getNowTime("yyyy-MM-dd");
			}
			
			session = HibernateUtil.getSession();
			
			//检查是否有保养中的电梯，排除暂停的,'4'
			String cjsql="select numno,HandleStatus from MaintenanceWorkPlanDetail " +
					"where isnull(HandleStatus,'') in('2') and maintPersonnel = '"+userid+"' ";
			List checklist=session.createSQLQuery(cjsql).list();
			String isdoc="N";
			if(checklist!=null && checklist.size()>0){
				isdoc="Y";
			}
			
			String sql="select mwp.Numno,mwp.Singleno,isnull(mwp.HandleStatus,'') as HandleStatus,"
					+"mwp.MaintDate,mwp.Week,mwp.MaintType,"
					+"mcd.elevatorNo,mcd.maintAddress,"
					+"isnull(e.beginLongitude,0) as beginLongitude,"
					+"isnull(e.beginDimension,0) as beginDimension,"
					+"isnull(e.rem,'') as maintrem," +
					"isnull(mwp.StopTime,'') as stoptime "
					+"from MaintenanceWorkPlanDetail mwp,"
					+"MaintenanceWorkPlanMaster mwpm " 
					+"left join ElevatorCoordinateLocation e on e.elevatorNo=mwpm.elevatorNo," 
					+"MaintContractDetail mcd "
					+ "where mwpm.billno=mwp.billno "
					+ "and mcd.rowid=mwpm.rowid and mwp.maintPersonnel = '"+userid+"' "
					+ "and mwpm.checkflag='Y'";
			if(state!=null && !"".equals(state)){
				if(Integer.valueOf(state)==0){
					sql+=" and (mwp.handleStatus like '"+state+"' or mwp.handleStatus is null)";
				}else{
					sql+=" and mwp.handleStatus like '"+state+"'";
				}
			}
			
			if(sdate!=null&&!sdate.equals("")){
				sql+=" and mwp.maintDate >= '"+sdate+"'";
			}
			if(edate!=null&&!edate.equals("")){
				sql+=" and mwp.maintDate <= '"+edate+"'";
			}
			
			sql+=" order by mwp.maintDate";
			
			//System.out.println(">>>>"+sql);

			Query query = session.createSQLQuery(sql);
            ArrayList mwpList = (ArrayList) query.list();
            if (mwpList != null && mwpList.size()>0 ) {
                 for(int i=0;i<mwpList.size();i++)
                 {
                	 Object[] objects=(Object[]) mwpList.get(i);
                	 
                	 JSONObject jsonObject = new JSONObject(); 
                	 jsonObject.put("numno", objects[0]);
                	 jsonObject.put("singleno", objects[1]);

                	 String stoptime=(String)objects[11];//暂停时间
                	 
                	 // 0 已转派，1 已接收，2  已到场，3 已完工，4  暂停，5  复工
                	 String handlestatus=objects[2].toString();
                	 if(handlestatus!=null&& !handlestatus.equals("")){
	                	 if(handlestatus.equals("1") || handlestatus=="1"){
	                		 jsonSrt="[{'name':'到场','url':'http://127.0.0.1/完工'}]";
	                		 JSONArray nextstate = new JSONArray(jsonSrt);
	                		 jsonObject.put("handlestatus","已接收");
	                		 jsonObject.put("nextstate",nextstate);
	                	 }else if(handlestatus.equals("2") || handlestatus=="2"){
	                		 if(stoptime!=null && !stoptime.trim().equals("")){
	                			 jsonSrt="[{'name':'完工','url':'http://127.0.0.1/完工'}]";
	                		 }else{
	                			 jsonSrt="[{'name':'完工','url':'http://127.0.0.1/完工'},{'name':'','url':'http://127.0.0.1/转派'},{'name':'暂停','url':'http://127.0.0.1/暂停'}]"; 	 
	                		 }
	                		 JSONArray nextstate = new JSONArray(jsonSrt);
	                		 jsonObject.put("handlestatus","保养中"); 
	                		 jsonObject.put("nextstate",nextstate);
	                	 }else if(handlestatus.equals("3") || handlestatus=="3"){
	                		 jsonSrt="[]";
	                		 JSONArray nextstate = new JSONArray(jsonSrt);
	                		 jsonObject.put("handlestatus","已完工"); 
	                		 jsonObject.put("nextstate",nextstate);
	                	 }else if(handlestatus.equals("4") || handlestatus=="4"){
	                		 jsonSrt="[{'name':'复工','url':'http://127.0.0.1/完工'}]";
	                		 JSONArray nextstate = new JSONArray(jsonSrt);
	                		 jsonObject.put("handlestatus","已暂停"); 
	                		 jsonObject.put("nextstate",nextstate);
	                	 }else{
	                		 jsonSrt="[{'name':'接收','url':'http://127.0.0.1/接收'},{'name':'转派','url':'http://127.0.0.1/转派'}]";
	                		 JSONArray nextstate = new JSONArray(jsonSrt);
	                		 jsonObject.put("handlestatus","未接收");
	                		 jsonObject.put("nextstate",nextstate);
	                	 }
                	 }else{
                		 jsonSrt="[{'name':'接收','url':'http://127.0.0.1/接收'},{'name':'转派','url':'http://127.0.0.1/转派'}]";
                		 JSONArray nextstate = new JSONArray(jsonSrt);
                		 jsonObject.put("handlestatus","未接收");
                		 jsonObject.put("nextstate",nextstate);
                	 }
                	 jsonObject.put("maintdate", objects[3]);
                	 jsonObject.put("week", objects[4]);
                	 jsonObject.put("elevatorno", objects[6]);
                	 jsonObject.put("maintaddress", objects[7]);

                	 //jsonObject.put("mainttype", objects[5]);
                	 String maintType=(String)objects[5];
                	 if(maintType.trim().equals("halfMonth")){
                		 jsonObject.put("mainttype", "半月保养"); 
                	 }else if(maintType.trim().equals("quarter")){
                		 jsonObject.put("mainttype", "季度保养"); 
                	 }else if(maintType.trim().equals("halfYear")){
                		 jsonObject.put("mainttype", "半年保养"); 
                	 }else if(maintType.trim().equals("yearDegree")){
                		 jsonObject.put("mainttype", "年度保养"); 
                	 }
                	 
                	//电梯基础坐标
                	 jsonObject.put("elongitude", objects[8]);//经度
                	 jsonObject.put("elatitude", objects[9]);//纬度
                	 jsonObject.put("maintrem", objects[10]);//备注
                	 
                	 jsonObject.put("isdoc", isdoc);//是否到场
                	 
                	 jobiArray.put(i, jsonObject);   
                 } 
                 json.put("code", "200");
      			 json.put("info", "OK");
                 rejson.put("status", json);
          		 rejson.put("data", jobiArray);
		      }
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
	 * 维保作业--详细信息
	 * @param String userid
 	 * @param String numno
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("/bydetail/{userid}/{numno}")
	@Produces("application/json")
	public Response bydetail(
			@PathParam("userid") String userid,
			@PathParam("numno") String numno) throws JSONException {
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>保养作业-详细信息");
		
		try{
			session = HibernateUtil.getSession();
			
			String sql="select mwp,mcd.elevatorNo,mcd.maintAddress,c.companyId,c.companyName,mwpm.billno,mcd.elevatorType "
					+ "from MaintenanceWorkPlanDetail mwp,MaintenanceWorkPlanMaster mwpm,"
					+ "MaintContractDetail mcd,MaintContractMaster mcm,Customer c "
					+ "where mwpm.billno=mwp.maintenanceWorkPlanMaster.billno and mwpm.rowid=mcd.rowid "
					+ "and mcm.billNo=mcd.billNo and mcm.companyId=c.companyId and mwp.numno='"+numno+"' "
					+ "and mwp.maintPersonnel = '"+userid+"'";
			Query query = session.createQuery(sql);
            ArrayList mwpList = (ArrayList) query.list();
             //判断用户是否正确
            if (mwpList != null && mwpList.size()>0 ) {
                 
                	 Object[] objects=(Object[]) mwpList.get(0);
                	 MaintenanceWorkPlanDetail mwp=(MaintenanceWorkPlanDetail) objects[0];
                	 JSONObject jsonObject = new JSONObject(); 
                	 jsonObject.put("numno", mwp.getNumno());
                	 jsonObject.put("singleno", mwp.getSingleno());
                	 jsonObject.put("elevatorno", objects[1]);
                	 jsonObject.put("handlestatus", mwp.getHandleStatus());
                	 
                	 jsonObject.put("companyid", objects[3]);
                	 jsonObject.put("companyname", objects[4]);
                	 
                	 //获取上一次保养完工日期
                	 String lastmaintdate ="";
//                	 sql="select mwpd.maintEndTime from MaintenanceWorkPlanDetail mwpd "
//                	 	+ "where mwpd.numno=(select numno-1 from MaintenanceWorkPlanDetail where numno='"+mwp.getNumno()+"' "
//                	 	+ "and billno='"+(String) objects[5]+"' and numno !=(select MIN(numno)from MaintenanceWorkPlanDetail "
//                	 	+ "where billno='"+(String) objects[5]+"'))";
                	 
                	sql="select max(maintEndTime) from MaintenanceWorkPlanDetail "
             				+ "where  billno='"+(String) objects[5]+"' "
             				+ "and MaintDate=(select MAX(MaintDate) from MaintenanceWorkPlanDetail "
             				+ "where  billno='"+(String) objects[5]+"' and MaintDate<'"+mwp.getMaintDate()+"')";
                	 
             		 List list=session.createSQLQuery(sql).list();
             	     if(list!=null && list.size()>0){
             	    	lastmaintdate=(String) list.get(0);
             	    	if(lastmaintdate!=null && !"".equals(lastmaintdate)){
             	    		lastmaintdate=lastmaintdate.substring(0, 10);	
             	    	}
             	     }
                	 jsonObject.put("lastmaintdate", lastmaintdate);
                	 
                	 jsonObject.put("maintaddress", objects[2]);
                	 jsonObject.put("maintdate", mwp.getMaintDate());
                	 jsonObject.put("week", mwp.getWeek());
                	 jsonObject.put("mainttype", mwp.getMaintType());
                	 jsonObject.put("receivingtime", mwp.getReceivingTime());
                	 jsonObject.put("maintstarttime", mwp.getMaintStartTime());
                	 jsonObject.put("maintendtime", mwp.getMaintEndTime());
                	 jsonObject.put("maintstartaddress", mwp.getMaintStartAddres());
                	 jsonObject.put("maintendaddress", mwp.getMaintEndAddres());
                	 jsonObject.put("maintDateTime", mwp.getMaintDateTime());//标准保养时间
                	 jsonObject.put("stoptime", mwp.getStopTime());
                	 jsonObject.put("stopaddres", mwp.getStopAddres());
                	 jsonObject.put("restarttime", mwp.getRestartTime());
                	 jsonObject.put("restartaddres", mwp.getRestartAddres());
                	 /*
                	 String r5=mwp.getR5();//保养参与人员
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
                	 jsonObject.put("engcyuserid", mwp.getR5());
                	 jsonObject.put("engcyusername", mwp.getR5());//保养参与人员
                	 jsonObject.put("byrem", mwp.getR4());//保养备注
                	 

                	//检查是否有保养中的电梯,排除暂停的,'4'
         			String cjsql="select numno,HandleStatus from MaintenanceWorkPlanDetail " +
         					"where HandleStatus in('2') and maintPersonnel = '"+userid+"' ";
         			List checklist=session.createSQLQuery(cjsql).list();
         			if(checklist!=null && checklist.size()>0){
                		 jsonObject.put("isdoc", "Y");
                	 }else{
                		 jsonObject.put("isdoc", "N");
                	 }
                	 
                	 //电梯基础坐标
                	 String jcsql="from ElevatorCoordinateLocation where elevatorNo='"+objects[1].toString()+"'";
                	 List jclist=session.createQuery(jcsql).list();
                	 if(jclist!=null && jclist.size()>0){
                		 ElevatorCoordinateLocation ecl=(ElevatorCoordinateLocation) jclist.get(0);

                		 if(ecl.getBeginLongitude()==null){
                    		 jsonObject.put("elongitude", 0);//经度
                		 }else{
                    		 jsonObject.put("elongitude", ecl.getBeginLongitude());//经度
                		 }
                		 if(ecl.getBeginDimension()==null){
                        	 jsonObject.put("elatitude", 0);//纬度
                		 }else{
                        	 jsonObject.put("elatitude", ecl.getBeginDimension());//纬度
                		 }
                		 jsonObject.put("maintrem", ecl.getRem());//项目名称及楼栋号
                	 }else{
                    	 jsonObject.put("elongitude", 0);//经度
                    	 jsonObject.put("elatitude", 0);//纬度
                    	 jsonObject.put("maintrem", "");//项目名称及楼栋号
                	 }
                	 
                	 String maintType=mwp.getMaintType();
                	 if(maintType.trim().equals("halfMonth")){
                		 jsonObject.put("maintType", "半月保养"); 
                	 }else if(maintType.trim().equals("quarter")){
                		 jsonObject.put("maintType", "季度保养"); 
                	 }else if(maintType.trim().equals("halfYear")){
                		 jsonObject.put("maintType", "半年保养"); 
                	 }else if(maintType.trim().equals("yearDegree")){
                		 jsonObject.put("maintType", "年度保养");
                	 }
                	 
                	 /**=====================读取下载电梯保养记录单路径=========================*/
      				String path="D:\\contract\\下载电梯保养记录单路径.txt";
      				BufferedReader reader= new BufferedReader(new FileReader(path));
      				String downloadaddr=reader.readLine();
      				reader.close();
      				//String downloadaddr="http://10.10.0.5:8080/XJSCRM/PrintMaintenanceWorkPlanServlet?id=";//测试
      				//String downloadaddr="http://www.xjelevator.com:9000/XJSCRM/PrintMaintenanceWorkPlanServlet?id=";//正式
      				if(downloadaddr!=null && !downloadaddr.trim().equals("")){
      					jsonObject.put("downloadaddr", downloadaddr.trim()+mwp.getNumno());//电梯保养单记录下载地址
      				}else{
      					jsonObject.put("downloadaddr", "");//下载电梯保养记录单路径
      				}
                 	 
                 	/**=====================获取图片=========================*/
      				String folder = PropertiesUtil.getProperty("MaintenanceWorkPlanDetail.file.upload.folder");
      				BASE64Encoder base=new BASE64Encoder();
      				//客户签名
      				if(mwp.getCustomerSignature()!=null && !mwp.getCustomerSignature().trim().equals("")){
      					String filepath=folder+mwp.getCustomerSignature();
      					byte[] imgbyte=CommonUtil.imageToByte(filepath);//将图片转换为二进制流
      					jsonObject.put("customersignature", base.encode(imgbyte));//将二进制流加密
      				}else{
      					jsonObject.put("customersignature", "");
      				}
      				//客户照片
      				if(mwp.getCustomerImage()!=null && !mwp.getCustomerImage().trim().equals("")){
      					String filepath=folder+mwp.getCustomerImage();
      					byte[] imgbyte=CommonUtil.imageToByte(filepath);//将图片转换为二进制流		
      					jsonObject.put("customerimage", base.encode(imgbyte));//将二进制流加密
      				}else{
      					jsonObject.put("customerimage", "");
      				}
      				/**=====================获取图片=========================*/
      				
      				/**=====================获取维保项目信息=========================*/
      				JSONArray detaillist=new JSONArray();
      				if(mwp.getHandleStatus()!=null && mwp.getHandleStatus().equals("3")){
      					//已完工的
      					String hql ="select mpiw from  MaintainProjectInfoWork mpiw " +
      							"where mpiw.singleno='"+mwp.getSingleno()+"' " +
      							"order by mpiw.orderby,mpiw.maintItem";			
      				    List infoList=session.createQuery(hql).list();
	      				if(infoList!=null && infoList.size()>0){
	  						for(int i=0;i<infoList.size();i++){
	  							MaintainProjectInfoWork mpif=(MaintainProjectInfoWork) infoList.get(i);
	  							
	  							JSONObject object2=new JSONObject();
	  							object2.put("orderby", mpif.getOrderby());//排序号
	  							object2.put("maintitem", mpif.getMaintItem());//检维保项目
	  							object2.put("maintcontents", mpif.getMaintContents());//维保基本要求
	  							object2.put("ismaintain", mpif.getIsMaintain());//是否保养
	  							
	  							detaillist.put(i, object2);
	  						}
	  					}
      				}else{
      					String maintsql="select a from MaintainProjectInfo a " +
                    	 		"where a.elevatorType='"+objects[6].toString()+"' " +
                    	 		"and maintType='"+maintType+"' and a.enabledFlag='Y' " +
                    	 		"order by a.orderby,a.maintItem";
                    	 List hList=session.createQuery(maintsql).list();
                    	 
                    	 if(hList!=null && hList.size()>0){
    						for(int i=0;i<hList.size();i++){
    							MaintainProjectInfo mpi=(MaintainProjectInfo) hList.get(i);
    							
    							JSONObject object2=new JSONObject();
    							object2.put("orderby", mpi.getOrderby()+"");//排序号
    							object2.put("maintitem", mpi.getMaintItem());//检维保项目
    							object2.put("maintcontents", mpi.getMaintContents());//维保基本要求
    							object2.put("ismaintain", "Y");//是否保养
    							
    							detaillist.put(i, object2);
    						}
    					}
      				}
                	jsonObject.put("detaillist",detaillist);
                	
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
	 * 维保作业--处理
	 * @param JSONObject bydata
	 * @return
	 * @throws JSONException
	 */
	@POST
	@Path("/bysave")
	@Produces("application/json")
	public Response saveHandleStatus(@FormParam("bydata") JSONObject bydata) throws JSONException{
		
		Session session = null;
		Transaction tx = null;
		MaintenanceWorkPlanDetail mwpd=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []

		System.out.println(">>>>>>>>保养作业-处理");
		
		try{
			String userid=(String) bydata.get("userid");//登录用户id
			int numno=(Integer) bydata.get("numno");//维保计划明细行号
			String check_start=(String) bydata.get("check_start");//处理状态
			//String setDate=(String) bydata.get("setDate");//操作时间
			
			//System.out.println("check_start==="+check_start); 

			String elevatorno=(String) bydata.get("elevatorno");//电梯编号
			String iseleno="Y";//新增，是否保存到电梯基础坐标
			
			String address=""; 
			//判断 JSONObject 是否包含  address
			if(bydata.has("address")){
				address=(String) bydata.get("address");//地址
			}
			String rem="";
			//判断 JSONObject 是否包含  maintrem
			if(bydata.has("maintrem")){
				rem=(String) bydata.get("maintrem");//项目名称及楼栋号
			}
			
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			String curdate=DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss");
			
			//电梯基础坐标
			ElevatorCoordinateLocation ecl=null;
	       	 String jcsql="from ElevatorCoordinateLocation where elevatorNo='"+elevatorno+"'";
	       	 List jclist=session.createQuery(jcsql).list();
	       	 if(jclist!=null && jclist.size()>0){
	       		 ecl=(ElevatorCoordinateLocation)jclist.get(0);
	       		 if(ecl.getBeginLongitude()!=null && !ecl.getBeginLongitude().equals("")){
	 	       		iseleno="N";//不变
	       		 }else{
	       			iseleno="M";//修改
	       		 }
	       	 }
	       	//System.out.println(">>>>>>>>保养作业-转派 userid="+userid+";check_start="+check_start+";numno="+numno);
	       	
			String hql="from MaintenanceWorkPlanDetail mwpd where mwpd.numno ='"+numno+"' " +
					"and mwpd.maintPersonnel='"+userid.trim()+"'";
			List list=session.createQuery(hql).list();
			if(list!=null&&list.size()>0){
				mwpd=(MaintenanceWorkPlanDetail) list.get(0);
				
				// 0 已转派，1 已接收，2  已到场，3 已完工，4  暂停，5  复工
				if(check_start.equals("0") || check_start=="0")//转派
				{
				    String newuserid=(String) bydata.get("newuserid");//转派人Id
				    String newusertel=(String) bydata.get("newusertel");//转派人电话
				    //System.out.println(">>>>>>>>保养作业-转派 newuserid="+newuserid+";newusertel="+newusertel);
				    
					mwpd.setIsTransfer("Y");
					mwpd.setTransferDate(curdate);
					mwpd.setMaintPersonnel(newuserid);
					mwpd.setReceivingPerson(newuserid);
					mwpd.setReceivingPhone(newusertel);
					mwpd.setHandleStatus("0");
				}
				else if(check_start.equals("1") || check_start=="1")//接收
				{
					mwpd.setReceivingTime(curdate);
					mwpd.setHandleStatus("1");
				}
				else if(check_start.equals("2") || check_start=="2")//到场
				{
					double gpslon=(Double) bydata.get("gpslon");//经度
					double gpslat=(Double) bydata.get("gpslat");//纬度
					double lon=(Double) bydata.get("lon");//经度
					double lat=(Double) bydata.get("lat");//纬度
					String distance=(String) bydata.get("distance");//距离
					
					String singleno=CommonUtil.getNewSingleno(session, userid);//生成单号
					mwpd.setSingleno(singleno);
					mwpd.setBeginLongitude(lon);
					mwpd.setBeginDimension(lat);
					mwpd.setBeginLongitudeGPS(gpslon);//GPS原始坐标经度
					mwpd.setBeginDimensionGPS(gpslat);//GPS原始坐标纬度
					mwpd.setMaintStartAddres(address);
					mwpd.setMaintStartTime(curdate);
					mwpd.setHandleStatus("2");
					mwpd.setStartDistance(Double.parseDouble(distance));
					
					//保存电梯基础坐标 电梯地理坐标和位置
					if(iseleno.equals("Y")){
						//新增
						ecl=new ElevatorCoordinateLocation();
						ecl.setElevatorNo(elevatorno);
						ecl.setElevatorLocation(address);
						ecl.setBeginLongitude(lon);
						ecl.setEndLongitude(lon);
						ecl.setBeginDimension(lat);
						ecl.setEndDimension(lat);
						ecl.setEnabledFlag("Y");
						ecl.setOperId(userid);
						ecl.setOperDate(curdate);
						session.save(ecl);
					}else if(iseleno.equals("M")){
						//修改
						ecl.setElevatorLocation(address);
						ecl.setBeginLongitude(lon);
						ecl.setEndLongitude(lon);
						ecl.setBeginDimension(lat);
						ecl.setEndDimension(lat);
						ecl.setOperId(userid);
						ecl.setOperDate(curdate);
						session.update(ecl);
					}
				}
				else if(check_start.equals("3") || check_start=="3")//完工
				{
					double gpslon=(Double) bydata.get("gpslon");//经度
					double gpslat=(Double) bydata.get("gpslat");//纬度
					double lon=(Double) bydata.get("lon");//经度
					double lat=(Double) bydata.get("lat");//纬度
					String distance=(String) bydata.get("distance");//距离
					String engcyusername=(String) bydata.get("engcyusername");//保养参与人员名称
					String byrem=(String) bydata.get("byrem");//保养参与人员
					
					mwpd.setEndLongitude(lon);
					mwpd.setEndDimension(lat);
					mwpd.setEndLongitudeGPS(gpslon);//GPS原始坐标经度
					mwpd.setEndDimensionGPS(gpslat);//GPS原始坐标纬度
					mwpd.setMaintEndAddres(address);
					mwpd.setMaintEndTime(curdate);
					mwpd.setHandleStatus("3");
					mwpd.setEndDistance(Double.parseDouble(distance));
					mwpd.setR4(byrem);
					mwpd.setR5(engcyusername);
					
					//计算所用时长,计算 距离评分，所用时长评分，保养得分
					this.toCalculation(mwpd);
					
					/**=======================保存图片=============================*/
					String folder = PropertiesUtil.getProperty("MaintenanceWorkPlanDetail.file.upload.folder");
					String filepath=CommonUtil.getNowTime("yyyy-MM-dd")+"/";
					//保存签名
					String customersignature=(String)bydata.get("customersignature");
					if(customersignature!=null && !customersignature.trim().equals("")){
						//customersignature=customersignature.replaceAll("data:image/jpeg;base64,", "");//去掉前缀
						String[] signatures=customersignature.split(",");
						if(signatures!=null && signatures.length>1){
							byte[] image=new BASE64Decoder().decodeBuffer(signatures[1]);
							String newfilename=mwpd.getSingleno()+"_0.jpg";
							//保存图片
							File f=new File(folder+filepath);
							f.mkdirs();
							FileOutputStream fos=new FileOutputStream(folder+filepath+newfilename);
							fos.write(image);
							fos.flush();
							fos.close();
							//保存图片信息到数据库
							mwpd.setCustomerSignature(filepath+newfilename);
						}
					}
					//保存拍照
					String customerimage=(String)bydata.get("customerimage");
					if(customerimage!=null && !customerimage.trim().equals("")){
						//customerimage=customerimage.replaceAll("data:image/jpeg;base64,", "");//去掉前缀
						String[] cimages=customerimage.split(",");
						if(cimages!=null && cimages.length>1){
							byte[] image=new BASE64Decoder().decodeBuffer(cimages[1]);
							String newfilename=mwpd.getSingleno()+"_1.jpg";
							//保存图片
							File f=new File(folder+filepath);
							f.mkdirs();
							FileOutputStream fos=new FileOutputStream(folder+filepath+newfilename);
							fos.write(image);
							fos.flush();
							fos.close();
							//保存图片信息到数据库
							mwpd.setCustomerImage(filepath+newfilename);
						}
					}
					/**=======================保存图片=============================*/
					
					/**=======================保存维保项目明细=============================*/
					JSONArray detaillist=(JSONArray) bydata.get("detaillist");//维保项目明细
					if(detaillist!=null && detaillist.length()>0){
						
						//先删除 暂停时，保存的保养项目信息
						//String sql3 = "delete from MaintainProjectInfoWork where Singleno='"+mwpd.getSingleno()+"'";
						//session.connection().prepareStatement(sql3).executeUpdate();
						
						for(int i=0;i<detaillist.length();i++){
							JSONObject object=(JSONObject) detaillist.get(i);
							String orderby = (String) object.get("orderby");//排序号
							String maintitem = (String) object.get("maintitem");//维保项目
							String maintcontents = (String) object.get("maintcontents");//维保基本要求
							String ismaintain = (String) object.get("ismaintain");//是否保养
							
							MaintainProjectInfoWork mpiw=new MaintainProjectInfoWork();
							mpiw.setSingleno(mwpd.getSingleno());//保养单号
							mpiw.setOrderby(new Integer(orderby));
							mpiw.setMaintItem(maintitem);
							mpiw.setMaintContents(maintcontents);
							mpiw.setIsMaintain(ismaintain);
							session.save(mpiw);
						}
					}
					
					//保存电梯基础坐标 电梯地理坐标和位置
					if(iseleno.equals("Y")){
						//新增
						ecl=new ElevatorCoordinateLocation();
						ecl.setElevatorNo(elevatorno);
						ecl.setElevatorLocation(address);
						ecl.setBeginLongitude(lon);
						ecl.setEndLongitude(lon);
						ecl.setBeginDimension(lat);
						ecl.setEndDimension(lat);
						ecl.setRem(rem);
						ecl.setEnabledFlag("Y");
						ecl.setOperId(userid);
						ecl.setOperDate(curdate);
						session.save(ecl);
					}else if(iseleno.equals("M")){
						//修改
						ecl.setElevatorLocation(address);
						ecl.setBeginLongitude(lon);
						ecl.setEndLongitude(lon);
						ecl.setBeginDimension(lat);
						ecl.setEndDimension(lat);
						ecl.setRem(rem);
						ecl.setOperId(userid);
						ecl.setOperDate(curdate);
						session.update(ecl);
					}else{
						ecl.setRem(rem);
						//ecl.setOperId(userid);
						//ecl.setOperDate(curdate);
						session.update(ecl);
					}
				}
				else if(check_start.equals("4") || check_start=="4")//暂停
				{
					double gpslon=(Double) bydata.get("gpslon");//经度
					double gpslat=(Double) bydata.get("gpslat");//纬度
					double lon=(Double) bydata.get("lon");//经度
					double lat=(Double) bydata.get("lat");//纬度
					String distance=(String) bydata.get("distance");//距离

					mwpd.setHandleStatus("4");
					mwpd.setStopTime(curdate);
					mwpd.setStopLongitude(lon);
					mwpd.setStopDimension(lat);
					mwpd.setStopLongitudeGPS(gpslon);//GPS原始坐标经度
					mwpd.setStopDimensionGPS(gpslat);//GPS原始坐标纬度
					mwpd.setStopAddres(address);
					mwpd.setStopDistance(Double.parseDouble(distance));
				}
				else if(check_start.equals("5") || check_start=="5")//复工
				{
					//暂停，复工
					double gpslon=(Double) bydata.get("gpslon");//经度
					double gpslat=(Double) bydata.get("gpslat");//纬度
					double lon=(Double) bydata.get("lon");//经度
					double lat=(Double) bydata.get("lat");//纬度
					String distance=(String) bydata.get("distance");//距离

					mwpd.setHandleStatus("2");
					mwpd.setRestartTime(curdate);
					mwpd.setRestartLongitude(lon);
					mwpd.setRestartDimension(lat);
					mwpd.setRestartLongitudeGPS(gpslon);//GPS原始坐标经度
					mwpd.setRestartDimensionGPS(gpslat);//GPS原始坐标纬度
					mwpd.setRestartAddres(address);
					mwpd.setRestartDistance(Double.parseDouble(distance));
				}

				session.update(mwpd);
			}
			tx.commit();

			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
      		rejson.put("data", jobiArray);
		}catch(Exception ex){
			if(tx!=null){
				tx.rollback();
			}
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
	 * 维保派工
	 * @param String userid
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("/bypglist/{userid}")
	@Produces("application/json")
	public Response bypglist(@PathParam("userid") String userid) throws JSONException {
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		System.out.println(">>>>>>>>保养作业-派工人员");
		
		try{
			session = HibernateUtil.getSession();
			con=session.connection();

			String sql="exec C_pgList "+userid;

			ps=con.prepareStatement(sql);
			rs=ps.executeQuery();
			int i=0;
           while(rs.next()){
          	 JSONObject jsonObject = new JSONObject(); 
          	 jsonObject.put("newuserid",rs.getString("userid"));
          	 jsonObject.put("newusername",rs.getString("username"));
          	 jsonObject.put("newusertel",rs.getString("usertel"));
          	 jsonObject.put("storageid", rs.getString("storageid"));
          	 jsonObject.put("storagename",rs.getString("storagename"));
          	 jsonObject.put("comid", rs.getString("comid"));
          	 jsonObject.put("comname",rs.getString("comname"));
          	 jsonObject.put("rolename",rs.getString("rolename"));
             jobiArray.put(i, jsonObject); 
        	   i++;
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
	
	//计算所用时长，所用时长评分,距离评分，保养得分
	private void toCalculation(MaintenanceWorkPlanDetail mwpd){
		
		//计算所用时长
		double usedDuration=CommonUtil.getMinute(mwpd.getMaintStartTime(), mwpd.getMaintEndTime());
		//暂停时长
		double stopduration=0;
		if(mwpd.getStopTime()!=null && !mwpd.getStopTime().equals("")){
			stopduration=CommonUtil.getMinute(mwpd.getStopTime(), mwpd.getRestartTime());
		}
		//所用时长减去暂停时长
		usedDuration=usedDuration-stopduration;
 	    mwpd.setUsedDuration(usedDuration);
 	    
 	    /**所用时长评分
 	      	所用时长评分标准						分数
 	      0.9标准工时~ 2标准工时					10分
 	      0.8标准工时~0.9标准工时 或者 2标准工时~3标准工时	8分
 	      0.7标准工时~0.8标准工时 或者 3标准工时~4标准工时	7分
 	      0.6标准工时~0.7标准工时 或者 4标准工时~5标准工时	6分
 	      15分钟 到 0.6标准工时						5分
 	      15分钟以下 或者 5标准工时以上					0分
 	     */
 	    double datescore=0d;//所用时长评分
 	    double maintdatetime=Double.parseDouble(mwpd.getMaintDateTime());//标准保养时间
 	    double score=usedDuration/maintdatetime;
 	    if(score>=0.9 && usedDuration<=(maintdatetime*2)){
 	    	datescore=10;//0.9标准工时~ 2标准工时
 	    }else if(score>=0.8 && score<0.9){
 	    	datescore=8;//0.8标准工时~0.9标准工时
 	    }else if(usedDuration>(maintdatetime*2) && usedDuration<=(maintdatetime*3)){
 	    	datescore=8;//2标准工时~3标准工时
 	    }else if(score>=0.7 && score<0.8){
 	    	datescore=7;//0.7标准工时~0.8标准工时
 	    }else if(usedDuration>(maintdatetime*3) && usedDuration<=(maintdatetime*4)){
 	    	datescore=7;//3标准工时~4标准工时
 	    }else if(score>=0.6 && score<0.7){
 	    	datescore=6;//0.6标准工时~0.7标准工时
 	    }else if(usedDuration>(maintdatetime*4) && usedDuration<=(maintdatetime*5)){
 	    	datescore=6;//4标准工时~5标准工时
 	    }else if(usedDuration>=15 && score<0.6){
 	    	datescore=5;//15分钟 到 0.6标准工时
 	    }else{
 	    	datescore=0;
 	    }
 	    mwpd.setDateScore(datescore);//所用时长评分
 	    
 	    /**距离评分   [两次距基点距离平均值] 
		  	距离评分标准	分数
			0-200m	10分
			200-500	5分
			500米以上	0分
	     */
 	    double distancescore=0;
 	    double startdistance=0;//保养开始距离(米)
 	    if(mwpd.getStartDistance()!=null){
 	    	startdistance=mwpd.getStartDistance();
 	    }
 	    double enddistance=0;//保养结束距离(米)
 	    if(mwpd.getEndDistance()!=null){
 	    	enddistance=mwpd.getEndDistance();//保养结束距离(米)
 	    }
 	    int avgnum=2;
 	    double stopDistance=0;//暂停距离(米)
 	    if(mwpd.getStopDistance()!=null){
 	    	stopDistance=mwpd.getStopDistance();
 	    	avgnum++;
 	    }
 	    double restartDistance=0;//复工距离(米)
 	    if(mwpd.getRestartDistance()!=null){
 	    	restartDistance=mwpd.getRestartDistance();
 	    	avgnum++;
 	    }
 	    double distance=(startdistance+enddistance+stopDistance+restartDistance)/avgnum;//平均值
 	    
 	    if(distance>=0 && distance<=200){
 	    	distancescore=10;//0-200m	10分
 	    }else if(distance>200 && distance<=500){
 	    	distancescore=5;//200-500	5分
 	    }else{
 	    	distancescore=0;
 	    }
 	    mwpd.setDistanceScore(distancescore);//距离评分
 	    
 	    /**保养得分 
 	     * 任一一项得分为0分，则总分为0分。
 	     * 60%时间评分+40%距离评分
 	     */
 	    double maintscore=0;
 	    if(datescore>0 && distancescore>0){
 	    	maintscore=datescore*0.6+distancescore*0.4;
 	    }
 	    mwpd.setMaintScore(maintscore);//保养得分 
 	    
	}
	
}
