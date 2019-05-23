package com.mobileserver;

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
import java.util.HashMap;
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

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.grcnamelist.Grcnamelist1;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.util.CommonUtil;
import com.gzunicorn.common.util.DateUtil;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.PropertiesUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.file.HandleFile;
import com.gzunicorn.file.HandleFileImpA;
import com.gzunicorn.hibernate.basedata.elevatorcoordinatelocation.ElevatorCoordinateLocation;
import com.gzunicorn.hibernate.basedata.markingscore.MarkingScore;
import com.gzunicorn.hibernate.basedata.markingscoredetail.MarkingScoreDetail;
import com.gzunicorn.hibernate.infomanager.markingscoreregister.MarkingScoreRegister;
import com.gzunicorn.hibernate.infomanager.markingscoreregisterdetail.MarkingScoreRegisterDetail;
import com.gzunicorn.hibernate.infomanager.markingscoreregisterfileinfo.MarkingScoreRegisterFileinfo;
import com.gzunicorn.hibernate.infomanager.qualitycheckmanagement.QualityCheckManagement;
import com.gzunicorn.hibernate.infomanager.qualitycheckprocess.QualityCheckProcess;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.workflow.bo.JbpmExtBridge;

/**
 * 手机APP端调用，维保质量抽查登记类
 * @author Crunchify
 */
@Path("/Qualitycheck")
public class Qualitycheck {
	
	BaseDataImpl bd = new BaseDataImpl();
	/**
	 * 维保质量抽查登记-列表
	 * @param userid
	 * @param elevatorno
	 * @param processstatus
	 * @return
	 */
	@POST
	@Path("/wbzllist")
	@Produces("application/json")
	public Response getWbzlList(@FormParam("data") JSONObject data){
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>维保质量抽查登记-列表");
		
		try{
			String userid=(String) data.get("userid");
			String elevatorno =(String) data.get("elevatorno");
			String processstatus =(String) data.get("processstatus");
			
			//System.out.println((Integer) data.get("pageno"));
			
			session = HibernateUtil.getSession();
			//tx = session.beginTransaction();
			String sql ="from QualityCheckManagement q where q.submitType='Y' and q.superviseId='"+userid+"'";
            if(elevatorno!=null&&!elevatorno.trim().equals("")){
            	sql +=" and q.elevatorNo like '%"+elevatorno.trim()+"%'";
            }
            if(processstatus!=null && !processstatus.trim().equals("") && !processstatus.trim().equals("5")){
				sql+=" and q.processStatus like '"+processstatus.trim()+"' ";
			}
			if(processstatus!=null && !processstatus.trim().equals("") && processstatus.trim().equals("5")){
				sql+=" and q.status=0 ";
			}
			sql +=" order by q.billno desc";
			
			//System.out.println(sql);
			
			List selectList=session.createQuery(sql).list();
			if(selectList!=null&&selectList.size()>0){
				for(int i=0;i<selectList.size();i++){
					QualityCheckManagement q =(QualityCheckManagement) selectList.get(i);
					JSONObject jsonObject=new JSONObject();
            	  	jsonObject.put("billno", q.getBillno());
            	  	jsonObject.put("elevatorno", q.getElevatorNo());
            	  	jsonObject.put("maintcontractno", q.getMaintContractNo());
            	  	jsonObject.put("projectname", q.getProjectName());
            	  	//jsonObject.put("maintdivision", q.getBillno());
            	  	//jsonObject.put("maintstation", q.getMaintStation());
            		jsonObject.put("maintdivision",bd.getName(session, "Company", "comfullname", "comid", q.getMaintDivision()));
            	  	jsonObject.put("maintstation",bd.getName(session, "Storageid", "storagename", "storageid",q.getMaintStation()));
            	  	jsonObject.put("maintpersonnel",bd.getName(session, "Loginuser", "username", "userid", q.getMaintPersonnel()) );
            	  	
            	  	String statusid=q.getProcessStatus();
					String statusname="";
            	  	if(statusid.equals("0")){
            	  		statusname="未登记";
            	  	}else if(statusid.equals("1")){
            	  		statusname="已登记未提交";
            	  	}else if(statusid.equals("2")){
            	  		statusname="已登记已提交";
            	  	}else if(statusid.equals("3")){
            	  		statusname="已审核";
            	  	}
            	  	
            	  	int status=q.getStatus();//流程状态
            	  	if(status==0){
						statusname="我的待办";
					}
            	  	jsonObject.put("processstatus", statusname);//处理状态
            	  	
            		jobiArray.put(i, jsonObject);
				}	
			}
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
      		//rejson.put("data", jobiArray);
            rejson.put("data", CommonUtil.Pagination(data, jobiArray));
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
	 * 维保质量抽查登记-查看
	 * @param userid
	 * @param billno
	 * @return
	 */
	@GET
	@Path("/wbzldisplay/{userid}/{billno}")
	@Produces("application/json")
	public Response getWbzlDisplay(
	@PathParam("userid") String userid, 
	@PathParam("billno") String billno
	){
		Session session = null;
		Loginuser user=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>维保质量抽查登记-进入查看编辑页面");
		try{
			session = HibernateUtil.getSession();
			
			List userList=session.createQuery("from Loginuser where userid ='"+userid+"'").list();
			if(userList!=null&&userList.size()>0){
				user=(Loginuser) userList.get(0);
			}
			
			String roleid=user.getRoleid();
			String sql="from QualityCheckManagement q where q.billno ='"+billno.trim()+"'";
			Query query = session.createQuery(sql);
            ArrayList qList = (ArrayList) query.list();
             //cpList是否有值
            if (qList != null && qList.size()>0 ) {
            	
            	QualityCheckManagement q=(QualityCheckManagement) qList.get(0);
            	  	JSONObject jsonObject=new JSONObject();
            	  	jsonObject.put("billno", q.getBillno());
            	  	jsonObject.put("elevatorno", q.getElevatorNo());//电梯编号
            	  	
            	  	String elevatortype=q.getElevatorType();
            	  	String elevatortypename="";
            	  	if(elevatortype.equals("T")){
            	  		elevatortypename="直梯";
					}else if(elevatortype.equals("F")){
						elevatortypename="扶梯";
					}
            	  	jsonObject.put("elevatortype", elevatortype);//电梯类型
            	  	jsonObject.put("elevatortypename", elevatortypename);//电梯类型名称
            	  	jsonObject.put("salescontractno", q.getSalesContractNo());//销售合同号
            	  	jsonObject.put("projectname", q.getProjectName());//项目名称
            	  	jsonObject.put("maintdivision",bd.getName(session, "Company", "comfullname", "comid", q.getMaintDivision()));
            	  	jsonObject.put("maintstation",bd.getName(session, "Storageid", "storagename", "storageid",q.getMaintStation()));
            	  	jsonObject.put("maintpersonnel",bd.getName(session, "Loginuser", "username", "userid", q.getMaintPersonnel()));//维保工
            	  	jsonObject.put("personnelphone", q.getPersonnelPhone());//维保工电话
            	  	jsonObject.put("maintcontractno", q.getMaintContractNo());//维保合同号
            	  	jsonObject.put("superviseid", bd.getName(session, "Loginuser", "username", "userid", q.getSuperviseId()));//现场督查人员
            	  	jsonObject.put("supervisephone", q.getSupervisePhone());//督查人员联系电话
            	  	//jsonObject.put("checkspeople", bd.getName(session, "Loginuser", "username", "userid", q.getChecksPeople()));//抽查人
            	  	jsonObject.put("checksdatetime", q.getChecksDateTime());//抽查时间
            	  	jsonObject.put("totalpoints", q.getTotalPoints());//总得分
            	  	jsonObject.put("scorelevel", q.getScoreLevel());//得分等级
            	  	jsonObject.put("supervopinion", q.getSupervOpinion());//督查意见
            	  	
            	  	String r5=q.getR5();//保养参与人员
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
	               	 jsonObject.put("qualityuserid", r5);
	               	 jsonObject.put("qualityusername", r5name);//保养参与人员
	               	 jsonObject.put("checkrem", q.getR4());//检查备注
	               	 
	               	 //获取电梯地理坐标和位置
	               	ElevatorCoordinateLocation ecl=(ElevatorCoordinateLocation)session.get(ElevatorCoordinateLocation.class, q.getElevatorNo());
	               	if(ecl!=null){
						jsonObject.put("elocation", ecl.getRem());//楼栋号
						jsonObject.put("elelocation", ecl.getElevatorLocation());//电梯位置
	               	 	jsonObject.put("begindimension", ecl.getBeginDimension());//纬度
	               	 	jsonObject.put("beginlongitude", ecl.getBeginLongitude());//经度
	               	}else{
	               		jsonObject.put("elocation", "");//楼栋号
						jsonObject.put("elelocation", "");//电梯位置
	               	 	jsonObject.put("begindimension", "");//纬度
	               	 	jsonObject.put("beginlongitude", "");//经度
	               	}
            	  	
            	  	//读取下载电梯维修记录单路径
    				String path="D:\\contract\\下载维保质量检查单路径.txt";
    				BufferedReader reader= new BufferedReader(new FileReader(path));
    				String downloadaddr=reader.readLine();
    				reader.close();
    				//String downloadaddr="http://10.10.0.5:8080/XJSCRM/PrintQualityCheckServlet?id=";//测试
    				//String downloadaddr="http://www.xjelevator.com:9000/XJSCRM/PrintQualityCheckServlet?id=";//正式
    				if(downloadaddr!=null && !downloadaddr.trim().equals("")){
    					jsonObject.put("downloadaddr", downloadaddr.trim()+q.getBillno());//厂检通知单下载地址
    				}else{
    					jsonObject.put("downloadaddr", "");//厂检通知单下载地址
    				}
            	  	
            	  	/**=====================获取图片=========================*/
     				String folder = PropertiesUtil.getProperty("QualityCheckManagement.file.upload.folder");
     				BASE64Encoder base=new BASE64Encoder();
     				//客户签名
     				if(q.getCustomerSignature()!=null && !q.getCustomerSignature().trim().equals("")){
     					String filepath=folder+q.getCustomerSignature();
     					byte[] imgbyte=CommonUtil.imageToByte(filepath);//将图片转换为二进制流
     					jsonObject.put("customersignature", base.encode(imgbyte));//将二进制流加密
     				}else{
     					jsonObject.put("customersignature", "");
     				}
     				//客户照片
     				if(q.getCustomerImage()!=null && !q.getCustomerImage().trim().equals("")){
     					String filepath=folder+q.getCustomerImage();
     					byte[] imgbyte=CommonUtil.imageToByte(filepath);//将图片转换为二进制流		
     					jsonObject.put("customerimage", base.encode(imgbyte));//将二进制流加密
     				}else{
     					jsonObject.put("customerimage", "");
     				}
     				/**=====================获取图片=========================*/
            	  	
            	  	//维保质量评分表登记主信息
                    String hql ="from MarkingScoreRegister m where m.billno='"+q.getBillno()+"'";
                    JSONArray detaillist=new JSONArray();
                    List mList=session.createQuery(hql).list();
                    if (mList != null && mList.size()>0 ) {
                    	for(int i=0;i<mList.size();i++){
                    		JSONObject mjsonObject=new JSONObject();
                    		MarkingScoreRegister m=(MarkingScoreRegister) mList.get(i);
                    		mjsonObject.put("jnlno", m.getJnlno());//流水号
                    		mjsonObject.put("msid", m.getMsId());//评分代码
                    		mjsonObject.put("msname", m.getMsName());//评分名称
                    		mjsonObject.put("fraction", m.getFraction());//分数
                    		mjsonObject.put("rem", m.getRem());//备注
                    		
                    		 //维保质量评分表登记明细
                    		 JSONArray detaillist2=new JSONArray();
                    		 String hql2="from MarkingScoreRegisterDetail ms where ms.jnlno='"+m.getJnlno()+"'";
                    		 List msList=session.createQuery(hql2).list();
                             if (msList != null && msList.size()>0 ) {
                             	for(int j=0;j<msList.size();j++){
                             		JSONObject msjsonObject=new JSONObject();
                             		MarkingScoreRegisterDetail ms=(MarkingScoreRegisterDetail) msList.get(j);
                            		msjsonObject.put("numno", ms.getNumno());//序号
                            		msjsonObject.put("msid", ms.getMsId());//评分代码
                            		msjsonObject.put("detailid", ms.getDetailId());//评分明细代码
                            		msjsonObject.put("detailname", ms.getDetailName());//评分明细名称
                            		detaillist2.put(j, msjsonObject);
                             	}
                             }
                            mjsonObject.put("detaillist2", detaillist2);
                            
                    		detaillist.put(i, mjsonObject);
                    	}
                    }
                	jsonObject.put("detaillist", detaillist);
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
     * 维保质量抽查登记-获取维保质量评分项
     * @param userid
     * @param data
     * @return
     */
	@POST
	@Path("/markingscore")
	@Produces("application/json")
	public Response getMarkingScore (@FormParam("data") JSONObject data){
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>维保质量抽查登记-获取维保质量评分项");
		try{
			session = HibernateUtil.getSession();
			String msname=(String) data.get("msname");
			String elevatortype =(String) data.get("elevatortype");
			String msidarr =(String) data.get("msidarr");
			
			String sql ="from MarkingScore q where q.enabledFlag='Y' and elevatorType='"+elevatortype+"' ";
			if(msidarr!=null && !msidarr.trim().equals("NULL") && !msidarr.trim().equals("")){
				msidarr=msidarr.replaceAll(",", "','");
				sql+=" and q.msId not in('"+msidarr+"') ";
			}
			if(msname!=null && !"".equals(msname.trim()) && !"NULL".equals(msname.trim())){
				sql+=" and (q.msName like '%"+msname.trim()+"%' or q.msId like '%"+msname.trim()+"%')";
			}
			sql+=" order by q.orderby ";
			List selectList=session.createQuery(sql).list();
			if(selectList!=null&&selectList.size()>0){
				for(int i=0;i<selectList.size();i++){
					MarkingScore m =(MarkingScore) selectList.get(i);
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("msid", m.getMsId());//评分代码
					jsonObject.put("msname", m.getMsName());//评分名称
					jsonObject.put("fraction", m.getFraction());//分数
            		jobiArray.put(i, jsonObject);
				}	
			}
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
            //rejson.put("data", jobiArray);
      		rejson.put("data", CommonUtil.Pagination(data, jobiArray));
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
	 * 维保质量抽查登记-获取维保质量评分明细
	 * @param userid
	 * @param msid
	 * @param detailname
	 * @param data
	 * @return
	 */
	@POST
	@Path("/markingscoredetail")
	@Produces("application/json")
	public Response getMarkingScoreDetail(@FormParam("data") JSONObject data){

		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>维保质量抽查登记-获取维保质量评分明细");
		try{
			session = HibernateUtil.getSession();
			
			String msid=(String) data.get("msid");
			String detailname=(String) data.get("detailname");
			String detailidarr=(String) data.get("detailidarr");
			
			String sql ="from MarkingScoreDetail m where m.markingScore.msId='"+msid.trim()+"' ";
			if(detailidarr!=null && !detailidarr.trim().equals("NULL") && !detailidarr.trim().equals("")){
				detailidarr=detailidarr.trim().replaceAll(",", "','");
				sql+=" and m.detailId not in('"+detailidarr+"') ";
			}
			if(detailname!=null && !detailname.trim().equals("") && !detailname.trim().equals("NULL")){
				sql+=" and (m.detailName like '%"+detailname.trim()+"%' or m.detailId like '%"+detailname.trim()+"%')";
			}
			sql+=" order by m.markingScore.msId ";
			List selectList=session.createQuery(sql).list();
			if(selectList!=null&&selectList.size()>0){
				for(int i=0;i<selectList.size();i++){
					MarkingScoreDetail m =(MarkingScoreDetail) selectList.get(i);
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("msid", m.getMarkingScore().getMsId());//评分代码
					jsonObject.put("detailid", m.getDetailId());//评分明细代码
					jsonObject.put("detailname", m.getDetailName());//评分明细名称
					jobiArray.put(i, jsonObject);
				}	
			}
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
            rejson.put("data", jobiArray);
      		//rejson.put("data", CommonUtil.Pagination(data, jobiArray));
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
	 * 获取维保质量评分项的明细和图片
	 * @param userid
	 * @param elevatortype
	 * @param issuecoding
	 * @return markingscore
	 */
	@GET
	@Path("/markingscoreimgae/{jnlno}")
	@Produces("application/json")
	public Response getImageList(@PathParam("jnlno") String jnlno){
		Session session = null;
		//Loginuser user=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONObject json2 = new JSONObject();//代表对象 {}
		JSONArray returnarr=new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>维保质量抽查登记-获取维保质量评分项的明细和图片");
		
		MarkingScoreRegisterFileinfo hefile=null;
		
		String folder = PropertiesUtil.getProperty("MTSComply.file.upload.folder");
		
		BASE64Encoder base=new BASE64Encoder();
		try{
			session = HibernateUtil.getSession();
			
			/**=====================获取 维保质量评分表登记明细 =========================*/
			JSONArray infoarr=new JSONArray();//代表数组 []
			//维保质量评分表登记明细
	   		 String hql2="from MarkingScoreRegisterDetail ms where ms.jnlno='"+jnlno.trim()+"'";
	   		 List msList=session.createQuery(hql2).list();
	            if (msList != null && msList.size()>0 ) {
	            	for(int i=0;i<msList.size();i++){
	            		JSONObject msjsonObject=new JSONObject();
	            		MarkingScoreRegisterDetail ms=(MarkingScoreRegisterDetail) msList.get(i);
		           		msjsonObject.put("numno", ms.getNumno());//序号
		           		msjsonObject.put("msid", ms.getMsId());//评分代码
		           		msjsonObject.put("detailid", ms.getDetailId());//评分明细代码
		           		msjsonObject.put("detailname", ms.getDetailName());//评分明细名称
		           		infoarr.put(i, msjsonObject);
	            	}
	            	json2.put("infoarr", infoarr);
	            }

			/**=====================获取 维保质量评分表附件表=========================*/
			String hql="from MarkingScoreRegisterFileinfo h where h.jnlno ='"+jnlno+"' ";
			List fileList=session.createQuery(hql).list();

			JSONArray imgarr=new JSONArray();//代表数组 []
			if(fileList!=null&&fileList.size()>0){
				for(int j=0;j<fileList.size();j++){
					hefile=(MarkingScoreRegisterFileinfo)fileList.get(j);
					String filepath=folder+"MarkingScoreRegisterFileinfo"+"/"+hefile.getNewFileName();
					//将图片转换为二进制流
					byte[] imgbyte=CommonUtil.imageToByte(filepath);
					
					JSONObject objf=new JSONObject();
					objf.put("filesid", hefile.getFileSid());
					objf.put("oldfilename", hefile.getOldFileName());
					objf.put("imgpic", base.encode(imgbyte));//将二进制流加密
					imgarr.put(j,objf);
				}
				json2.put("imgarr", imgarr);
			}
			
			returnarr.put(0,json2);
			
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
      		rejson.put("data", returnarr);
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
	 * 保存图片信息 并且返回jnlno流水号
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/azwbimagesave")
	@Produces("application/json")
	public Response saveImageInfo (@FormParam("data") JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;

		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>维保质量抽查登记-保存维保质量评分项的明细和图片");
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			String userid=(String) data.get("userid");//登录用户代码
			String billno=(String) data.get("billno");//主表流水号
			String jnlno=(String) data.get("jnlno");//流水号
			String msid=(String) data.get("msid");//评分代码
			String msname=(String) data.get("msname");//评分名称
			String fraction=(String) data.get("fraction");//分数
			if(fraction==null || fraction.trim().equals("")){
				fraction="0";
			}
			String rem=(String) data.get("rem");//备注
			
			/**==========================维保质量评分表登记主信息==========================*/
			MarkingScoreRegister hecir=null;
			if(jnlno!=null && !jnlno.equals("")){
				//流水号存在就修改
        		hecir = (MarkingScoreRegister) session.get(MarkingScoreRegister.class, jnlno);	
            	hecir.setRem(rem);
            	session.update(hecir);
            	
            	//删除维保质量评分表登记明细
            	session.createQuery("delete MarkingScoreRegisterDetail where jnlno='"+jnlno+"'").executeUpdate();

            }else{
            	//不存在就新建
                hecir = new MarkingScoreRegister();
				String[] jnlnos = CommonUtil.getBillno(CommonUtil.getToday().substring(2,4), "MarkingScoreRegister", 1);	
				jnlno=jnlnos[0];

				hecir.setJnlno(jnlno);
				hecir.setBillno(billno);
				hecir.setMsId(msid);
				hecir.setMsName(msname);
				hecir.setFraction(Double.parseDouble(fraction));
				hecir.setIsDelete("N");
				hecir.setRem(rem);
				session.save(hecir);
            }
			
			/**==================维保质量评分表登记明细==================*/
			JSONArray infolist=(JSONArray) data.get("infolist");//维保质量评分表登记明细
			if(infolist!=null && infolist.length()>0){
				MarkingScoreRegisterDetail msrd=null;
				for(int m=0;m<infolist.length();m++){
					msrd=new MarkingScoreRegisterDetail();
					JSONObject object=(JSONObject) infolist.get(m);
					msrd.setJnlno(jnlno);
					msrd.setMsId((String) object.get("msid"));
					msrd.setDetailId((String) object.get("detailid"));
					msrd.setDetailName((String) object.get("detailname"));
					session.save(msrd);
				}
			}
			
			/**==========================保存图片==========================*/
			JSONArray imagelist=(JSONArray) data.get("imagelist");//上传图片
			if(imagelist!=null && imagelist.length()>0){
				MarkingScoreRegisterFileinfo fileInfo=null;
				String folder = PropertiesUtil.getProperty("MTSComply.file.upload.folder").trim();
				
				String curdate=DateUtil.getDateTime("yyyyMMddHHmmss");
				
				for(int i=0;i<imagelist.length();i++){
					JSONObject object=(JSONObject) imagelist.get(i);
					String imgpic=(String) object.get("imgpic");//流水号
					
					byte[] image=new BASE64Decoder().decodeBuffer(imgpic);
					String newfilename=userid+"_"+curdate+"_"+i+".jpg";
					String filepath="MarkingScoreRegisterFileinfo"+"/";
	
					//保存图片
					File f=new File(folder+filepath);
					f.mkdirs();
					FileOutputStream fos=new FileOutputStream(folder+filepath+newfilename);
					fos.write(image);
					fos.flush();
					fos.close();
					
					//保存图片信息到数据库
					fileInfo=new MarkingScoreRegisterFileinfo();
					fileInfo.setJnlno(jnlno);
					fileInfo.setOldFileName(newfilename);
					fileInfo.setNewFileName(newfilename);
					fileInfo.setFileSize(Double.valueOf(0));
					fileInfo.setFilePath(folder+filepath+newfilename);
					fileInfo.setFileFormat("-");
					fileInfo.setUploadDate(CommonUtil.getNowTime());
					fileInfo.setUploader(userid);
					session.save(fileInfo);
				}
			}
			tx.commit();
			
			//返回  维保质量评分表登记主信息  的 流水号
			JSONObject object=new JSONObject();
			object.put("jnlno", jnlno);
			jobiArray.put(0, object);
			
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
      		rejson.put("data", jobiArray);
		}catch(Exception ex){
			tx.rollback();
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
	 * 维保质量抽查登记-创建
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	@POST
	@Path("/wbzladd")
	@Produces("application/json")
	public Response saveWbzl (@FormParam("data")JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;
		Loginuser user=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		JbpmExtBridge jbpmExtBridge=null;
		
		System.out.println(">>>>>>>>维保质量抽查登记-保存主信息");
		try{
			session=HibernateUtil.getSession();
			tx=session.beginTransaction();
			
			String billno=(String) data.get("billno");//流水号
			String elevatorno=(String) data.get("elevatorno");//电梯编号
			String userid=(String) data.get("userid");//录入人
			String checksdatetime=(String) data.get("checksdatetime");//抽查时间
			//String totalpoints=(String) data.get("totalpoints");//总得分
			//String scorelevel=(String) data.get("scorelevel");//得分等级
			String supervopinion=(String) data.get("supervopinion");//督查意见
			String processstatus=(String) data.get("processstatus");//处理状态
			String submittype=(String) data.get("submittype");//提交标志
			String qualityuserid=(String) data.get("qualityuserid");//参与保养人员
	
			JSONArray detaillist=(JSONArray) data.get("detaillist");//list
			String hecirJnlnos="";
			double fractiontotal=0;
			if(detaillist!=null&&detaillist.length()>0){
				for(int i=0;i<detaillist.length();i++){
					JSONObject object=(JSONObject) detaillist.get(i);
					String jnlno=(String) object.get("jnlno");//流水号
					if(jnlno!=null && !"".equals(jnlno)){
						hecirJnlnos+=i==0 ? jnlno : "','"+jnlno;
				    }
					
					String fractionstr=(String) object.get("fraction");//分数
					if(fractionstr==null || fractionstr.trim().equals("")){
						fractionstr="0";
					}
					fractiontotal+=Double.parseDouble(fractionstr);
				}
			}
			
			/**===============计算得分等级===============*/
			System.out.println(">>>>开始计算得分等级===开始");
			HashMap hmap=this.isQualified(fractiontotal);
			String totalpoints=(String)hmap.get("totalpoints");
			String scorelevel=(String)hmap.get("scorelevel");
			System.out.println(">>>>开始计算得分等级===结束");
			
			
			QualityCheckManagement q =(QualityCheckManagement)session.get(QualityCheckManagement.class, billno);
			q.setElevatorNo(elevatorno);//电梯编号
			q.setChecksPeople(userid);
			q.setChecksDateTime(checksdatetime);
			q.setTotalPoints(Double.valueOf(totalpoints));
			q.setScoreLevel(scorelevel);
			q.setSupervOpinion(supervopinion);
			q.setProcessStatus(processstatus);
	        //q.setOperId(userid);
	        //q.setOperDate(CommonUtil.getNowTime());
	        q.setR5(qualityuserid);
			
	        Integer status=q.getStatus();
			if("Y".equals(submittype)){
				String processDefineID = Grcnamelist1.getProcessDefineID("qualitycheckmanagement", userid);// 流程环节id
				if(processDefineID == null || processDefineID.equals("")){
					System.out.println("手机端登记>>> 未配置审批流程信息，不能启动！");
					throw new Exception();
				}
				
				if(status!=null && status==0){
					//获取流程信息 审批不通过的
					long taskid=0;
					String taskname="";
					String sqlc="select ID_,NAME_ from JBPM_TASKINSTANCE where TOKEN_="+q.getTokenId()+" and isnull(END_,'')=''";
					ResultSet rs=session.connection().prepareStatement(sqlc).executeQuery();
					if(rs.next()){
						taskid=rs.getLong("ID_");
						taskname=rs.getString("NAME_");
					}
					/*=============== 流程审批启动开始 =================*/
					jbpmExtBridge = new JbpmExtBridge();
					HashMap paraMap = new HashMap();
					ProcessBean pd = jbpmExtBridge.getProcessBeanUseTI(taskid);
					
					pd.addAppointActors("");// 将动态添加的审核人清除
					Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "督查组长审核", userid);
					pd = jbpmExtBridge.goToNext(taskid, "提交", userid, paraMap);// 审核
					/*=============== 流程审批启动结束 =================*/
					q.setTokenId(pd.getToken());
					q.setStatus(pd.getStatus());
					q.setProcessName(pd.getNodename());	
					session.save(q);
		
					// 保存审批流程相关信息
					QualityCheckProcess process = new QualityCheckProcess();
					process.setBillno(billno);
					process.setTaskId(pd.getTaskid().intValue());// 任务号
					process.setTaskName(taskname);// 任务名称
					process.setTokenId(pd.getToken());// 流程令牌
					process.setUserId(userid);
					process.setDate1(CommonUtil.getToday());
					process.setTime1(CommonUtil.getTodayTime());
					process.setApproveResult("提交");
					process.setApproveRem("手机处理");
					session.save(process);
					
				}else{
					/**=============== 启动新流程实例开始 ===================**/
					HashMap paraMap = new HashMap();
					jbpmExtBridge=new JbpmExtBridge();
					ProcessBean pd = null;		
					pd = jbpmExtBridge.getPb();
		
					Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "督查组长审核", userid);
					pd=jbpmExtBridge.startProcess(WorkFlowConfig.getProcessDefine(processDefineID),userid,userid,billno,"",paraMap);
					/**==================== 流程结束 =======================**/
					q.setStatus(pd.getStatus());
					q.setTokenId(pd.getToken());
					q.setProcessName("督查组长审核");
				}
			}
			
			/**=======================保存图片=============================*/
			String folder2 = PropertiesUtil.getProperty("QualityCheckManagement.file.upload.folder");
			String filepath2=CommonUtil.getNowTime("yyyy-MM-dd")+"/";
			//保存签名
			String customersignature=(String) data.get("customersignature");
			if(customersignature!=null && !customersignature.trim().equals("")){
				//customersignature=customersignature.replaceAll("data:image/jpeg;base64,", "");//去掉前缀
				String[] signatures=customersignature.split(",");
				if(signatures!=null && signatures.length>1){
					byte[] image=new BASE64Decoder().decodeBuffer(signatures[1]);
					String newfilename=q.getBillno()+"_0.jpg";
					//保存图片
					File f=new File(folder2+filepath2);
					f.mkdirs();
					FileOutputStream fos=new FileOutputStream(folder2+filepath2+newfilename);
					fos.write(image);
					fos.flush();
					fos.close();
					//保存图片信息到数据库
					q.setCustomerSignature(filepath2+newfilename);
				}
			}
			//保存拍照
			String customerimage=(String)data.get("customerimage");
			if(customerimage!=null && !customerimage.trim().equals("")){
				//customerimage=customerimage.replaceAll("data:image/jpeg;base64,", "");//去掉前缀
				String[] cimages=customerimage.split(",");
				if(cimages!=null && cimages.length>1){
					byte[] image=new BASE64Decoder().decodeBuffer(cimages[1]);
					String newfilename=q.getBillno()+"_1.jpg";
					//保存图片
					File f=new File(folder2+filepath2);
					f.mkdirs();
					FileOutputStream fos=new FileOutputStream(folder2+filepath2+newfilename);
					fos.write(image);
					fos.flush();
					fos.close();
					//保存图片信息到数据库
					q.setCustomerImage(filepath2+newfilename);
				}
			}
			/**=======================保存图片=============================*/
			session.save(q);
			
			/**=======================维保质量评分表登记主信息=======================*/
			//先删除界面不存在,先删除数据库数据
			List filelist=null;
			if(!hecirJnlnos.equals("")){
				String filesql="from MarkingScoreRegisterFileinfo h where h.jnlno in(select jnlno from MarkingScoreRegister h where h.billno='"+billno+"' and h.jnlno not in('"+hecirJnlnos+"'))";
				filelist=session.createQuery(filesql).list();
				
				//删除维保质量评分表附件表
				session.createQuery("delete MarkingScoreRegisterFileinfo h where h.jnlno in(select jnlno from MarkingScoreRegister h where h.billno='"+billno+"' and h.jnlno not in('"+hecirJnlnos+"'))").executeUpdate();
				//维保质量评分表登记明细
				session.createQuery("delete MarkingScoreRegisterDetail h where h.jnlno in(select jnlno from MarkingScoreRegister h where h.billno='"+billno+"' and h.jnlno not in('"+hecirJnlnos+"'))").executeUpdate();
				//维保质量评分表登记主信息
				session.createQuery("delete MarkingScoreRegister h where h.billno='"+billno+"' and h.jnlno not in('"+hecirJnlnos+"')").executeUpdate();
			}else{
				String filesql="from MarkingScoreRegisterFileinfo h where h.jnlno in(select jnlno from MarkingScoreRegister h where h.billno='"+billno+"')";
				filelist=session.createQuery(filesql).list();
	
				session.createQuery("delete MarkingScoreRegisterFileinfo h where h.jnlno in(select jnlno from MarkingScoreRegister h where h.billno='"+billno+"')").executeUpdate();
				session.createQuery("delete MarkingScoreRegisterDetail h where h.jnlno in(select jnlno from MarkingScoreRegister h where h.billno='"+billno+"')").executeUpdate();
				session.createQuery("delete MarkingScoreRegister h where h.billno='"+billno+"'").executeUpdate();
			}
			//再删除硬盘的图片
			if(filelist!=null && filelist.size()>0){
				String folder ="MTSComply.file.upload.folder";
				folder = PropertiesUtil.getProperty(folder).trim();
				MarkingScoreRegisterFileinfo msrf=null;
				for(int d=0;d<filelist.size();d++){
					msrf=(MarkingScoreRegisterFileinfo)filelist.get(d);
					
					HandleFile hf = new HandleFileImpA();
					String localpath=folder+"MarkingScoreRegisterFileinfo"+"/"+msrf.getNewFileName();
					hf.delFile(localpath);
				}
			}
			
			//在保存 维保质量评分表登记主信息
			if(detaillist!=null && detaillist.length()>0){
				MarkingScoreRegister msr=null;
				for(int i=0;i<detaillist.length();i++){
					JSONObject jsonObject=(JSONObject) detaillist.get(i);
					String jnlno=(String) jsonObject.get("jnlno");//流水号
					String msid=(String) jsonObject.get("msid");//评分代码
					String msname=(String) jsonObject.get("msname");//评分名称
					String fraction=(String) jsonObject.get("fraction");//分数
					if(fraction==null || fraction.trim().equals("")){
						fraction="0";
					}
					String rem=(String) jsonObject.get("rem");//备注
					
					if(jnlno!=null && !jnlno.equals("")){
						msr=(MarkingScoreRegister)session.get(MarkingScoreRegister.class, jnlno);
						msr.setRem(rem);
						msr.setIsDelete("N");
						session.update(msr);
					}else{
						msr =new MarkingScoreRegister(); 
						msr.setBillno(billno);
						String[] jnlnos = CommonUtil.getBillno(CommonUtil.getToday().substring(2,4), "MarkingScoreRegister", 1);		
						msr.setJnlno(jnlnos[0]);
						msr.setMsId(msid);//评分代码
						msr.setMsName(msname);//评分名称
						msr.setFraction(Double.parseDouble(fraction));//分数
						msr.setRem(rem);//备注
						msr.setIsDelete("N");
						session.save(msr);
					}
				}
			}
	
			tx.commit();
			json.put("code", "200");
			json.put("info", "OK");
	        rejson.put("status", json);
	  		rejson.put("data", jobiArray);
		}catch(Exception ex){
			if (jbpmExtBridge != null) {
				jbpmExtBridge.setRollBack();
			}
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
	        	if(jbpmExtBridge!=null){
					jbpmExtBridge.close();
				}
	        } catch (Exception hex) {
	            DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
	        }
	
	    }
		
		return Response.status(200).entity(rejson.toString()).build();
	}

	/**
	 * 删除文件
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@GET
	@Path("/deletefile/{filesid}")
	@Produces("application/json")
	public Response deleteFile (@PathParam("filesid") String filesid) throws JSONException{
		Session session = null;
		Transaction tx = null;
		//Loginuser user=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []

		System.out.println(">>>>>>>>维保质量抽查登记-删除图片");
		
		MarkingScoreRegisterFileinfo hecf=null;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			String folder ="MTSComply.file.upload.folder";
			folder = PropertiesUtil.getProperty(folder).trim();
			if(filesid!=null && filesid.length()>0){
				
				hecf=(MarkingScoreRegisterFileinfo) session.get(MarkingScoreRegisterFileinfo.class,Integer.valueOf(filesid));
				String filename=hecf.getNewFileName();
				session.delete(hecf);
				session.flush();
				
				HandleFile hf = new HandleFileImpA();
				String localpath=folder+"MarkingScoreRegisterFileinfo"+"/"+filename;
				hf.delFile(localpath);
			}
			
			tx.commit();
			
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
      		rejson.put("data", jobiArray);
		}catch(Exception ex){
			tx.rollback();
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
	
	//检查项计算 总得分，得分等级
	/** 
	  	总得分=100-分值的合计；
	  	--得分等级=根据总得分进行判断：90分以上优秀，80分-90分合格，80分以下不合格
	  	得分等级=80-84分为合格，85-89为良好， 90以上为优秀 【2017-05-19修改】
	*/
	private HashMap isQualified(double fraction){
		HashMap hmap=new HashMap();
		
		String scorelevel="";
		double totalpoints=100-fraction;
		
		if(totalpoints>=90){
			scorelevel="优秀";
		}else if(totalpoints>=85 && totalpoints<90){
			scorelevel="良好";
		}else if(totalpoints>=80 && totalpoints<85){
			scorelevel="合格";
		}else{
			scorelevel="不合格";
		}
		
		hmap.put("totalpoints", String.valueOf(totalpoints));
		hmap.put("scorelevel", scorelevel);
		
		return hmap;
		
	}
	/**
	 * 维保质量抽查登记=维保参与人员
	 * @param String userid
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("/qualitybylist/{billno}")
	@Produces("application/json")
	public Response bypglist(@PathParam("billno") String billno) throws JSONException {
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		System.out.println(">>>>>>>>维保质量抽查登记-维保参与人员");
		
		try{
			session = HibernateUtil.getSession();
			con=session.connection();

			String sql="exec sp_qualityby "+billno;

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
	  
}
