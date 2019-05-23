package com.mobileserver;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.gzunicorn.file.HandleFile;
import com.gzunicorn.file.HandleFileImpA;
import com.gzunicorn.hibernate.mobileofficeplatform.accessoriesrequisition.AccessoriesRequisition;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.wbfileinfo.Wbfileinfo;
import com.gzunicorn.struts.action.xjsgg.SmsService;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/**
 * 手机APP端调用，配件申请类
 * @author Crunchify
 */
@Path("/Requisition")
public class Requisition {
	
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
	 * 配件申请-列表(查询)
	 * @param userid
	 * @param singleno
	 * @param sdate
	 * @param edate
	 * @return
	 */
	@GET
	@Path("/pjsqlist/{data}")
	@Produces("application/json")
	public Response getReqList(
			@PathParam("data") JSONObject data 
			){
		Session session = null;
		Transaction tx = null;
		Loginuser user=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
        
		System.out.println(">>>>>>>>配件申请--信息列表");
		
		try{
			String sdate=(String) data.get("sdate");
	        String edate=(String) data.get("edate");
	        String userid=(String) data.get("userid");
	        String singleno=(String) data.get("singleno");
	        String elevatorno=(String) data.get("elevatorno");//电梯编号
	        String handlestatus=(String) data.get("handlestatus");//处理状态

			session = HibernateUtil.getSession();
			
			List userList=session.createQuery("from Loginuser where userid ='"+userid+"'").list();
			if(userList!=null&&userList.size()>0){
				user=(Loginuser) userList.get(0);
			}
			
			String roleid=user.getRoleid();
			String sql="from AccessoriesRequisition a where 1=1 ";
			//维保站长A49，维保工A50,维保经理 A03，维修技术员 A53，
			if(roleid.trim().equals("A50") || roleid.trim().equals("A49") || roleid.trim().equals("A53")){
				//A50  维保工  
				sql+=" and a.operId ='"+user.getUserid().trim()+"' ";
			}else if(roleid.trim().equals("A03")){
				//A03  维保经理
				sql+=" and ((a.maintStation like '"+user.getStorageid().trim()+"%' and a.handleStatus='1') "
						+ " or (a.handleStatus='3' and a.operId='"+userid+"')) ";
			}else{
				//不给看
				sql+=" and a.maintStation like '' ";	
			}
			if (singleno != null && !"".equals(singleno)){
				sql+=" and a.singleNo like '%"+singleno+"%'";		
			}
			if (elevatorno != null && !"".equals(elevatorno)){
				sql+=" and a.elevatorNo like '%"+elevatorno+"%'";		
			}
			if (handlestatus != null && !"".equals(handlestatus)){
				sql+=" and a.handleStatus like '"+handlestatus+"'";		
			}
			if (sdate != null && !"".equals(sdate)) {
				sql += " and a.operDate >= '"+sdate.trim()+" 00:00:00'";
			}
			if (edate != null && !"".equals(edate)) {
				sql += " and a.operDate <= '"+edate.trim()+" 99:99:99'";
			}
			
			if(roleid.trim().equals("A50") || roleid.trim().equals("A49") 
					|| roleid.trim().equals("A53") || roleid.trim().equals("A03")){
				//维保工登录的，列表显示为 '维保工确认'排在前面,维保经理排在后面。
				sql +=" order by case when a.handleStatus='3' then 0 "
						+ "when a.handleStatus='1' then 1 else 2 end, "
						+ "a.operDate desc";
	  		}else{
	  			sql +=" order by a.operDate desc";
	  		}

			//System.out.println(">>>"+sql);
			
			Query query = session.createQuery(sql);
            ArrayList tList = (ArrayList) query.list();
             //cpList是否有值
            if (tList != null && tList.size()>0 ) {
            	for(int i=0;i<tList.size();i++){
            		AccessoriesRequisition a=(AccessoriesRequisition) tList.get(i);
            	  	JSONObject jsonObject=new JSONObject();
            	  	
            	  	String hstatus=a.getHandleStatus();
            	  	String hstatusname="";
            	  	//处理状态 【1 维保负责人审核，2 配件库管理员审核，3 维保工确认，4 旧件退回，5 已关闭，6 终止】
            	  	if(hstatus!=null && hstatus.trim().equals("1")){
            	  		if(roleid.trim().equals("A03")){
            	  			hstatusname="<b>维保负责人审核</b>";
            	  		}else{
            	  			hstatusname="维保负责人审核";
            	  		}
            	  	}else if(hstatus!=null && hstatus.trim().equals("2")){
            	  		hstatusname="配件库管理员审核";
            	  	}else if(hstatus!=null && hstatus.trim().equals("3")){
            	  		if(roleid.trim().equals("A50") || roleid.trim().equals("A49") || roleid.trim().equals("A53")){
            	  			hstatusname="<b>维保工确认</b>";
            	  		}else if(roleid.trim().equals("A03") && userid.equals(a.getOperId())){
            	  			hstatusname="<b>维保工确认</b>";
            	  		}else{
            	  			hstatusname="维保工确认";
            	  		}
            	  	}else if(hstatus!=null && hstatus.trim().equals("4")){
            	  		hstatusname="旧件退回";
            	  	}else if(hstatus!=null && hstatus.trim().equals("5")){
            	  		hstatusname="已关闭";
            	  	}else if(hstatus!=null && hstatus.trim().equals("6")){
            	  		hstatusname="终止";
            	  	}
            	  	
            	  	jsonObject.put("handlestatus", hstatus);
            	  	jsonObject.put("hstatusname", hstatusname);
            	  	jsonObject.put("appno", a.getAppNo());
            	  	jsonObject.put("oldno", a.getOldNo());
            	  	jsonObject.put("singleno", a.getSingleNo());
            	  	jsonObject.put("elevatorno", a.getElevatorNo());
            	  	jsonObject.put("operid", a.getOperId());
            	  	jsonObject.put("operName", bd.getName(session, "Loginuser", "username", "userid", a.getOperId()));
            	  	jsonObject.put("operdate", a.getOperDate());
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
	 * 配件申请-查看
	 * @param userid
	 * @param appno
	 * @return
	 */
	@GET
	@Path("/pjsqdetail/{userid}/{appno}")
	@Produces("application/json")
	public Response getReqDisplay(@PathParam("userid") String userid,@PathParam("appno") String appno){
		Session session = null;
		Transaction tx = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		Loginuser user=null;
		

		System.out.println(">>>>>>>>配件申请--查看");
		
		try{
			session = HibernateUtil.getSession();
			
			String sql="select a,l.username,l.phone,c.comname,s.storagename"
					+ " from AccessoriesRequisition a,Loginuser l,Company c,Storageid s "
					+ " where a.operId=l.userid and a.maintDivision=c.comid and a.maintStation=s.storageid"
					+ " and a.appNo ='"+appno.trim()+"' ";
			//System.out.println(">>>"+sql);
			Query query = session.createQuery(sql);
            ArrayList tList = (ArrayList) query.list();
             //cpList是否有值
            if (tList != null && tList.size()>0 ) {
            		Object[] objs=(Object[]) tList.get(0);
            	  	AccessoriesRequisition a=(AccessoriesRequisition) objs[0];
            	  	JSONObject jsonObject=new JSONObject();
            	  	jsonObject.put("appno", a.getAppNo());//li
            	  	jsonObject.put("singleno", a.getSingleNo());
            	  	jsonObject.put("elevatorno", a.getElevatorNo());
            	  	
                	String elerem=bd.getName(session, "ElevatorCoordinateLocation", "Rem", "ElevatorNo",a.getElevatorNo());
            	  	jsonObject.put("elenorem", elerem);//项目名称及楼栋号
            	  	
            	  	jsonObject.put("operid", a.getOperId());
            	  	jsonObject.put("operName", objs[1]);
            	  	jsonObject.put("operdate", a.getOperDate());
            	  	jsonObject.put("operphone",objs[2]);
            	  	jsonObject.put("oldno", a.getOldNo());//旧件编号
            	  	jsonObject.put("newno", a.getNewNo());//新件编号
            	  	jsonObject.put("r4", a.getR4());//故障描述
            	  	jsonObject.put("r5", a.getR5());//备件名称及型号/备注
            	  	jsonObject.put("r7", a.getR7());//申请次数
            	  	jsonObject.put("maintdivision",objs[3]);
            	  	jsonObject.put("maintstation",objs[4]);
            	  	jsonObject.put("personincharge", a.getPersonInCharge());//维保负责人
            	  	jsonObject.put("personinchargename", bd.getName(session, "Loginuser", "username", "userid", a.getPersonInCharge()));
            	  	jsonObject.put("picauditrem", a.getPicauditRem());//维保负责人审核意见
            	  	jsonObject.put("picauditdate", a.getPicauditDate());//维保负责人审核日期
            	  	
            	  	if(a.getIsAgree()!=null && !a.getIsAgree().trim().equals("")){
            	  		jsonObject.put("isagree", a.getIsAgree().trim().equals("N") ? "不同意" : "同意");//是否同意
            	  	}else{
            	  		jsonObject.put("isagree", "");//是否同意
            	  	}
            	  	jsonObject.put("warehousemanager", a.getWarehouseManager());//仓管录入人
            	  	jsonObject.put("warehousemanagerName", bd.getName(session, "Loginuser", "username", "userid", a.getWarehouseManager()));//仓管录入人
            	  	jsonObject.put("wmdate", a.getWmdate());//仓管录入日期
            	  	
            	  	/**=====================获取图片=========================*/
    				String folder = PropertiesUtil.getProperty("AccessoriesRequisitionReport.file.upload.folder");
    				BASE64Encoder base64=new BASE64Encoder();
    				//旧件图片
    				JSONArray olgimgarr=new JSONArray();//代表数组 []
    				List olgimglist=bd.getWbFileInfoList(session,"AccessoriesRequisition_OldImage",a.getAppNo());
    				if(olgimglist!=null&&olgimglist.size()>0){
    					for(int j=0;j<olgimglist.size();j++){
    						Wbfileinfo wbfile=(Wbfileinfo)olgimglist.get(j);
    						String filepath=folder+wbfile.getFilePath()+wbfile.getNewFileName();
    						//将图片转换为二进制流
    						byte[] imgbyte=CommonUtil.imageToByte(filepath);
    						
    						JSONObject objf=new JSONObject();
    						objf.put("filesid", wbfile.getFileSid());
    						objf.put("imgname", wbfile.getOldFileName());
    						objf.put("imgpic", base64.encode(imgbyte));//将二进制流加密
    						
    						olgimgarr.put(j,objf);
    					}
    				}
    				jsonObject.put("olgimgarr", olgimgarr);
    				/**
    				if(a.getOldImage()!=null && !a.getOldImage().trim().equals("")){
    					String filepath=folder+a.getOldImage();
    					byte[] imgbyte=CommonUtil.imageToByte(filepath);//将图片转换为二进制流
    					jsonObject.put("oldimage", base64.encode(imgbyte));//将二进制流加密
    				}else{
    					jsonObject.put("oldimage", "");
    				}
    				*/
    				//新件图片
    				JSONArray newimgarr=new JSONArray();//代表数组 []
    				List newimglist=bd.getWbFileInfoList(session,"AccessoriesRequisition_NewImage",a.getAppNo());
    				if(newimglist!=null && newimglist.size()>0){
    					for(int j=0;j<newimglist.size();j++){
    						Wbfileinfo wbfile=(Wbfileinfo)newimglist.get(j);
    						String filepath=folder+wbfile.getFilePath()+wbfile.getNewFileName();
    						//将图片转换为二进制流
    						byte[] imgbyte=CommonUtil.imageToByte(filepath);
    						
    						JSONObject objf=new JSONObject();
    						objf.put("filesid", wbfile.getFileSid());
    						objf.put("imgname", wbfile.getOldFileName());
    						objf.put("imgpic", base64.encode(imgbyte));//将二进制流加密
    						
    						newimgarr.put(j,objf);
    					}
    				}
    				jsonObject.put("newimgarr", newimgarr);
    				/**
    				if(a.getNewImage()!=null && !a.getNewImage().trim().equals("")){
    					String filepath=folder+a.getNewImage();
    					byte[] imgbyte=CommonUtil.imageToByte(filepath);//将图片转换为二进制流		
    					jsonObject.put("newimage", base64.encode(imgbyte));//将二进制流加密
    				}else{
    					jsonObject.put("newimage", "");
    				}
    				*/
    				//开票信息图片
    				JSONArray invoiceImagearr=new JSONArray();//代表数组 []
    				List invoiceImagelist=bd.getWbFileInfoList(session,"AccessoriesRequisition_invoiceImage",a.getAppNo());
    				if(invoiceImagelist!=null && invoiceImagelist.size()>0){
    					for(int j=0;j<invoiceImagelist.size();j++){
    						Wbfileinfo wbfile=(Wbfileinfo)invoiceImagelist.get(j);
    						String filepath=folder+wbfile.getFilePath()+wbfile.getNewFileName();
    						//将图片转换为二进制流
    						byte[] imgbyte=CommonUtil.imageToByte(filepath);
    						
    						JSONObject objf=new JSONObject();
    						objf.put("filesid", wbfile.getFileSid());
    						objf.put("imgname", wbfile.getOldFileName());
    						objf.put("imgpic", base64.encode(imgbyte));//将二进制流加密
    						
    						invoiceImagearr.put(j,objf);
    					}
    				}
    				jsonObject.put("invoiceimgarr", invoiceImagearr);
    				/**=====================获取图片=========================*/
    				
    				String hstatus=a.getHandleStatus();
            	  	String hstatusname="";
            	  	//处理状态 【1 维保负责人审核，2 配件库管理员审核，3 维保工确认，4 旧件退回，5 已关闭】
            	  	if(hstatus!=null && hstatus.trim().equals("1")){
            	  		hstatusname="维保负责人审核";
            	  	}else if(hstatus!=null && hstatus.trim().equals("2")){
            	  		hstatusname="配件库管理员审核";
            	  	}else if(hstatus!=null && hstatus.trim().equals("3")){
            	  		hstatusname="维保工确认";
            	  	}else if(hstatus!=null && hstatus.trim().equals("4")){
            	  		hstatusname="旧件退回";
            	  	}else if(hstatus!=null && hstatus.trim().equals("5")){
            	  		hstatusname="已关闭";
            	  	}else if(hstatus!=null && hstatus.trim().equals("6")){
            	  		hstatusname="终止";
            	  	}
            	  	jsonObject.put("handlestatus", hstatus);
            	  	jsonObject.put("hstatusname", hstatusname);
            	  	
            	  	if(a.getIsCharges()!=null && !a.getIsCharges().trim().equals("")){
            	  		String ischargesname="";
            	  		if(a.getIsCharges().trim().equals("N")){
            	  			ischargesname="免费";
            	  		}else if(a.getIsCharges().trim().equals("Y")){
            	  			ischargesname="收费";
            	  		}
            	  		jsonObject.put("ischarges", ischargesname);
            	  	}else{
            	  		jsonObject.put("ischarges", "");
            	  	}
            	  	if(a.getMoney1()!=null){
            	  		jsonObject.put("money1", a.getMoney1()+"");
            	  	}else{
            	  		jsonObject.put("money1", "");
            	  	}
            	  	jsonObject.put("wmrem", a.getWmRem());
            	  	
            	  	if(a.getWmIsAgree()!=null && !a.getWmIsAgree().trim().equals("")){
            	  		jsonObject.put("wmisagree", a.getWmIsAgree().trim().equals("N") ? "不同意" : "同意");
            	  	}else{
            	  		jsonObject.put("wmisagree", "");
            	  	}

            	  	if(a.getWmIsCharges()!=null && !a.getWmIsCharges().trim().equals("")){
            	  		String wmischargesname="";
            	  		if(a.getWmIsCharges().trim().equals("N")){
            	  			wmischargesname="免费";
            	  		}else if(a.getWmIsCharges().trim().equals("Y")){
            	  			wmischargesname="收费";
            	  		}
            	  		jsonObject.put("wmischarges", wmischargesname);
            	  	}else{
            	  		jsonObject.put("wmischarges", "");
            	  	}
            	  	//1: 分库直接领取,2: 总库调拨
            	  	if(a.getWmPayment()!=null && !a.getWmPayment().trim().equals("")){
            	  		String wmpaymentname="";
            	  		if(a.getWmPayment().trim().equals("1")){
            	  			wmpaymentname="分库直接领取";
            	  		}else if(a.getWmPayment().trim().equals("2")){
            	  			wmpaymentname="总库调拨";
            	  		}else if(a.getWmPayment().trim().equals("3")){
            	  			wmpaymentname="外购";
            	  		}
            	  		jsonObject.put("wmpayment", wmpaymentname);
            	  	}else{
            	  		jsonObject.put("wmpayment", "");
            	  	}
            	  	if(a.getMoney2()!=null){
            	  		jsonObject.put("money2", a.getMoney2()+"");
            	  	}else{
            	  		jsonObject.put("money2", "");//收费金额
            	  	}
            	  	jsonObject.put("invoicetype", a.getInvoicetype());//发票类型
            	  	jsonObject.put("instock", a.getInstock());//备件库是否有库存
            	  	jsonObject.put("expressno", a.getExpressNo());
            	  	jsonObject.put("expressname", a.getExpressName());//甲方开票名称
            	  	jsonObject.put("yjaddress", a.getYjaddress());//邮寄地址及电话

            	  	if(a.getJjReturn()!=null && !a.getJjReturn().trim().equals("")){
            	  		String jjname="";
            	  		if(a.getJjReturn().trim().equals("Y")){
            	  			jjname="已退回";
            	  		}else if(a.getJjReturn().trim().equals("N")){
            	  			jjname="未退回";
            	  		}
            	  		jsonObject.put("jjreturn", jjname);
            	  	}else{
            	  		jsonObject.put("jjreturn", "");
            	  	}
            	  	jsonObject.put("jjresult", a.getJjResult()==null?"":a.getJjResult());
            	  	jsonObject.put("jjoperid", bd.getName(session, "Loginuser", "username", "userid", a.getJjOperId()));
            	  	jsonObject.put("jjoperdate", a.getJjOperDate()==null?"":a.getJjOperDate());
            	  	
            	  	if(a.getR1()!=null&& !a.getR1().equals("")){
						 if(a.getR1().equals("Y") || a.getR1()=="Y"){
							 jsonObject.put("r1","有");
						 }else{
							 jsonObject.put("r1","无");
						 }
					}
					if(a.getR3()!=null&& !a.getR3().equals("")){
						 if(a.getR3().equals("Y") || a.getR3()=="Y"){
							 jsonObject.put("r3","有");
						 }else{
							 jsonObject.put("r3","无");
						 }
					}
					if(a.getIsConfirm()!=null&& !a.getIsConfirm().equals("")){
						 if(a.getIsConfirm().equals("Y") || a.getIsConfirm()=="Y"){
							 jsonObject.put("isconfirm","已更换");
						 }else{
							 jsonObject.put("isconfirm","入备件库");
						 }
					}
					
            	  //有偿/免保 [FREE - 免保,PAID - 有偿],
            	  	HashMap hmap=new HashMap();
        			String sqlk="select md.ElevatorNo,mm.MainMode,mm.ContractEDate,mm.BillNo "
        					+ "from MaintContractDetail md ,MaintContractMaster mm "
        					+ "where mm.BillNo=md.BillNo and mm.contractStatus in('XB','ZB') "
        					+ "and md.ElevatorNo='"+a.getElevatorNo()+"'";
        			//System.out.println(">>>>"+sqlk);
        			List krelist=session.createSQLQuery(sqlk).list();
        			if(krelist!=null && krelist.size()>0){
        				Object[] obj=(Object[])krelist.get(0);
                	  	
                	  	String r2=(String)obj[1];
                	  	if(r2!=null && r2.equals("PAID")){
                	  		r2="有偿";
                	  	}else if(r2!=null && r2.equals("FREE")){
                	  		r2="免保";
                	  	}
        				jsonObject.put("mainmode", r2);//有偿/免保
        				jsonObject.put("contractedate", (String)obj[2]);//合同到期日期
        			}else{
        				jsonObject.put("mainmode", "");//有偿/免保
        				jsonObject.put("contractedate", "");//合同到期日期
        			}
        			
        			jsonObject.put("invoiceimage", a.getInvoiceImage()==null?"":a.getInvoiceImage());//开票附件图片
        			jsonObject.put("invoicetype", a.getInvoicetype()==null?"":a.getInvoicetype());//发票类型
        			jsonObject.put("instock", a.getInstock()==null?"":a.getInstock());//备件库是否有库存
            	  	
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
	 * 配件申请-创建
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/pjsqadd")
	@Produces("application/json")
	public Response saveRequisition(@FormParam("data") JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		Loginuser user=null;
		Wbfileinfo wbinfo=null;

		System.out.println(">>>>>>>>配件申请--创建");
		
		try{
			session=HibernateUtil.getSession();
			tx=session.beginTransaction();
			String singleno=(String) data.get("singleno");//急修/保养单号
			String elevatorno=(String) data.get("elevatorno");//电梯编号
			String r4=(String) data.get("r4");//故障描述
			String r5=(String) data.get("r5");//备件名称及型号/备注
			String ischarges=(String) data.get("ischarges");//初步判断是否收费
			String expressname=(String) data.get("expressname");//甲方开票名称
			String yjaddress=(String) data.get("yjaddress");//邮寄地址及电话

			String money2=(String) data.get("money2");//收费金额
			String invoicetype=(String) data.get("invoicetype");//发票类型
			String instock=(String) data.get("instock");//备件库是否有库存

			String oldno=  CommonUtil.URLDecoder_decode((String) data.get("oldno"));//旧件编号
			String operid=(String) data.get("operid");//申请人
			String appno=(String) data.get("appno");//申请流水号
			

			List userList=session.createQuery("from Loginuser where userid ='"+operid+"'").list();
			if(userList!=null&&userList.size()>0){
				user=(Loginuser) userList.get(0);
			}
			
			AccessoriesRequisition a=null;
			String billno1 = "";
			if(!"".equals(appno)){
				a=(AccessoriesRequisition) session.get(AccessoriesRequisition.class, appno);
				billno1=appno;
				
				a.setPersonInCharge(null);//维保负责人
				a.setIsAgree(null);//维保是否同意
				a.setPicauditRem(null);//维保负责人审核意见
				a.setPicauditDate(null);//维保负责人审核日期
				a.setWmuserId(null);//仓管处理人
				a.setWarehouseManager(null);//仓管审核人
				a.setWmdate(null);//仓管审核日期
				a.setWmRem(null);//仓管审核意见
				a.setWmIsAgree(null);//仓管是否同意
				a.setWmIsCharges(null);//仓管是否收费
				a.setWmPayment(null);//仓管领取方式
			}else{
				a=new AccessoriesRequisition();
				String year1=CommonUtil.getToday().substring(2,4);
				billno1 = CommonUtil.getBillno(year1,"AccessoriesRequisition", 1)[0];	

				a.setAppNo(billno1);
			}

			//处理状态 【1 维保负责人审核，2 配件库管理员审核，3 维保工确认，4 旧件退回，5 已关闭，6 终止】
			a.setHandleStatus("1");
			a.setSingleNo(singleno);
			a.setElevatorNo(elevatorno);
			a.setIsCharges(ischarges);
			a.setOldNo(oldno);
            a.setOperId(operid);
			a.setOperDate(CommonUtil.getNowTime());
			a.setMaintDivision(user.getGrcid());//所属公司
			a.setMaintStation(user.getStorageid());//所属维保站
			a.setR5(r5);//备件名称及型号/备注
			a.setR4(r4);//故障描述
			a.setOldImage("");
			a.setNewImage("");
			a.setExpressName(expressname);//甲方开票名称
			a.setYjaddress(yjaddress);//邮寄地址及电话
			
			a.setInstock(instock);
			a.setInvoicetype(invoicetype);
			a.setMoney2(money2);

			String folder = PropertiesUtil.getProperty("AccessoriesRequisitionReport.file.upload.folder");
			/**=======================保存旧件图片=============================*/
			JSONArray oldimagearr= (JSONArray) data.get("oldimagearr");//旧件图片
			if(oldimagearr!=null && oldimagearr.length()>0){
				String curdate=DateUtil.getDateTime("yyyyMMddHHmmss");
				String filepath="AccessoriesRequisition_OldImage/"+CommonUtil.getNowTime("yyyy-MM-dd")+"/";
				for(int i=0;i<oldimagearr.length();i++){
					JSONObject object=(JSONObject) oldimagearr.get(i);
					String imgpic=(String) object.get("imgpic");//流水号
					
					String newfilename=operid+"_"+curdate+"_"+i+".jpg";
					//customerimage=customerimage.replaceAll("data:image/jpeg;base64,", "");//去掉前缀
					String[] cimages=imgpic.split(",");
					BASE64Decoder base64=new BASE64Decoder();
					if(cimages!=null && cimages.length>1){
						byte[] image=base64.decodeBuffer(cimages[1]);
						//保存图片
						File f=new File(folder+filepath);
						f.mkdirs();
						FileOutputStream fos=new FileOutputStream(folder+filepath+newfilename);
						fos.write(image);
						fos.flush();
						fos.close();
						
						//保存图片信息到数据库
						wbinfo=new Wbfileinfo();
						wbinfo.setBusTable("AccessoriesRequisition_OldImage");
						wbinfo.setMaterSid(billno1);
						wbinfo.setOldFileName(newfilename);
						wbinfo.setNewFileName(newfilename);
						wbinfo.setFilePath(filepath);
						wbinfo.setFileFormat("image/pjpeg");
						wbinfo.setFileSize(0d);
						wbinfo.setUploadDate(CommonUtil.getNowTime());
						wbinfo.setUploader(operid);
						wbinfo.setRemarks("配件申请，上传旧件附件");
						session.save(wbinfo);
					}
				}
			}
			/**=======================保存旧件图片=============================*/
			
			/**=======================保存开票图片=============================*/
			JSONArray invoiceimagearr= (JSONArray) data.get("invoiceimagearr");//开票图片
			if(invoiceimagearr!=null && invoiceimagearr.length()>0){
				String curdate=DateUtil.getDateTime("yyyyMMddHHmmss");
				String filepath="AccessoriesRequisition_InvoiceImage/"+CommonUtil.getNowTime("yyyy-MM-dd")+"/";
				for(int i=0;i<invoiceimagearr.length();i++){
					JSONObject object=(JSONObject) invoiceimagearr.get(i);
					String imgpic=(String) object.get("imgpic");//流水号
					
					String newfilename=operid+"_"+curdate+"_"+i+".jpg";
					//customerimage=customerimage.replaceAll("data:image/jpeg;base64,", "");//去掉前缀
					String[] cimages=imgpic.split(",");
					BASE64Decoder base64=new BASE64Decoder();
					if(cimages!=null && cimages.length>1){
						byte[] image=base64.decodeBuffer(cimages[1]);
						//保存图片
						File f=new File(folder+filepath);
						f.mkdirs();
						FileOutputStream fos=new FileOutputStream(folder+filepath+newfilename);
						fos.write(image);
						fos.flush();
						fos.close();
						
						//保存图片信息到数据库
						wbinfo=new Wbfileinfo();
						wbinfo.setBusTable("AccessoriesRequisition_invoiceImage");
						wbinfo.setMaterSid(billno1);
						wbinfo.setOldFileName(newfilename);
						wbinfo.setNewFileName(newfilename);
						wbinfo.setFilePath(filepath);
						wbinfo.setFileFormat("image/pjpeg");
						wbinfo.setFileSize(0d);
						wbinfo.setUploadDate(CommonUtil.getNowTime());
						wbinfo.setUploader(operid);
						wbinfo.setRemarks("配件申请，上传开票详情附件");
						session.save(wbinfo);
					}
				}
			}
			/**=======================保存开票详情图片=============================*/
			
			//申请次数
			String sqlc="select r7 from AccessoriesRequisition where SingleNo='"+singleno+"'";
			List relist=session.createSQLQuery(sqlc).list();
			if(relist!=null && relist.size()>0){
				a.setR7(relist.size()+1);
			}else{
				a.setR7(1);
			}

			session.save(a);
			tx.commit();
			
			/****************************发送短信给维保经理 开始**********************************/
			//A49  维保站长,A03  维保经理
			String sqlqu="select userid,phone from loginuser where '"+user.getStorageid()+"' like StorageID+'%' and RoleID in('A03')";
			String phone="";
			
			ResultSet rs=session.connection().prepareStatement(sqlqu).executeQuery();
			if(rs.next()){
				phone=rs.getString("phone");
			}
			
			if(phone!=null && !phone.equals("")){
				boolean issms=SmsService.compSendSMS(phone);
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
	 * 配件申请-处理保存
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/pjsqcladd")
	@Produces("application/json")
	public Response saveRequisitioncl(@FormParam("data") JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		Loginuser user=null;
		Wbfileinfo wbinfo=null;

		System.out.println(">>>>>>>>配件申请--处理保存");
		
		try{
			session=HibernateUtil.getSession();
			tx=session.beginTransaction();
			
			String hstatus=(String) data.get("hstatus");//z状态
			String appno=(String) data.get("appno");//流水号
			//String singleno=(String) data.get("singleno");//急修/保养单号
			String personincharge=(String) data.get("personincharge");//维保负责人
			String picauditrem=CommonUtil.URLDecoder_decode((String) data.get("picauditrem"));//维保负责人审核意见
			String isagree=(String) data.get("isagree");//是否同意
			//String ischarges=(String) data.get("ischarges");//维保是否收费
			String instock=(String) data.get("instock");//备件库是否有库存
			String isconfirm=(String) data.get("isconfirm");//维保工确认更换/入备件库
			String newno2=(String) data.get("newno2");//新件编号
			
			//[1 维保负责人审核，2 配件库管理员审核，3 维保工确认，4 旧件退回，5 已关闭，6 终止]
			AccessoriesRequisition a=(AccessoriesRequisition) session.get(AccessoriesRequisition.class, appno);
			if(hstatus!=null && hstatus.trim().equals("1")){
				//维保负责人审核
				a.setPersonInCharge(personincharge);
				a.setPicauditDate(CommonUtil.getNowTime());
				a.setPicauditRem(picauditrem);
				a.setIsAgree(isagree);
				//a.setIsCharges(ischarges);
				a.setInstock(instock);
				
				if(isagree!=null && isagree.trim().equals("N")){
					a.setHandleStatus("6");//不同意就终止
				}else{
					a.setHandleStatus("2");//配件库管理员审核
					//设置配件审核人,根据维保站和是否收费获取配件管理员，
					String sqlc="select a.OperId from WarehouseManager a,loginuser b where (a.MaintStation='"+a.getMaintStation()+"'"
							+ " or a.MaintStation in(select ParentStorageID from StorageID where StorageID='"+a.getMaintStation()+"')) "
							+ "and a.IsCharges='"+a.getIsCharges()+"' and a.OperId=b.UserID and b.EnabledFlag='Y'";
					ResultSet rs=session.connection().prepareStatement(sqlc).executeQuery();
					if(rs.next()){
						a.setWmuserId(rs.getString("OperId"));
					}
				}
			}else{
				a.setNewNo(newno2);//新件编号
				a.setIsConfirm(isconfirm);//维保工确认更换/入备件库[Y: 已更换 , N: 入备件库]
				//免费，且已更换的才会到旧件退回处来确认。
				if(a.getIsCharges()!=null && a.getIsCharges().trim().equals("N") && isconfirm.trim().equals("Y")){
					a.setHandleStatus("4");//旧件退回					
				}else{
					a.setHandleStatus("5");//关闭
				}
				
				//04003 许昌东维保站，04001 许昌西维保站的，配件申请单出库办理 默认为已经出库,2017-08-28增加
				String sqlkk="select StorageID from StorageID "
						+ "where (StorageID='"+a.getMaintStation()+"' or ParentStorageID ='"+a.getMaintStation()+"') "
						+ "and (StorageID in('04001','04003') or ParentStorageID in('04001','04003'))";
				List kklist=session.createSQLQuery(sqlkk).list();
				if(kklist!=null && kklist.size()>0){
					a.setCkiswc("Y");
					a.setCkrem("系统自动完成出库。");
					a.setCkoperid("admin");
					a.setCkdate(CommonUtil.getNowTime());
				}
				
				JSONArray newimagearr= (JSONArray) data.get("newimagearr");//新件图片
				/**=======================保存新件图片=============================*/
				String folder = PropertiesUtil.getProperty("AccessoriesRequisitionReport.file.upload.folder");
				if(newimagearr!=null && newimagearr.length()>0){
					String curdate=DateUtil.getDateTime("yyyyMMddHHmmss");
					String filepath="AccessoriesRequisition_NewImage/"+CommonUtil.getNowTime("yyyy-MM-dd")+"/";
					for(int i=0;i<newimagearr.length();i++){
						JSONObject object=(JSONObject) newimagearr.get(i);
						String imgpic=(String) object.get("imgpic");//流水号
						
						String newfilename=a.getAppNo()+"_"+curdate+"_"+i+".jpg";
						//customerimage=customerimage.replaceAll("data:image/jpeg;base64,", "");//去掉前缀
						String[] cimages=imgpic.split(",");
						BASE64Decoder base64=new BASE64Decoder();
						if(cimages!=null && cimages.length>1){
							byte[] image=base64.decodeBuffer(cimages[1]);
							//保存图片
							File f=new File(folder+filepath);
							f.mkdirs();
							FileOutputStream fos=new FileOutputStream(folder+filepath+newfilename);
							fos.write(image);
							fos.flush();
							fos.close();
							
							//保存图片信息到数据库
							wbinfo=new Wbfileinfo();
							wbinfo.setBusTable("AccessoriesRequisition_NewImage");
							wbinfo.setMaterSid(a.getAppNo());
							wbinfo.setOldFileName(newfilename);
							wbinfo.setNewFileName(newfilename);
							wbinfo.setFilePath(filepath);
							wbinfo.setFileFormat("image/pjpeg");
							wbinfo.setFileSize(0d);
							wbinfo.setUploadDate(CommonUtil.getNowTime());
							wbinfo.setUploader(a.getOperId());
							wbinfo.setRemarks("配件申请，上传新件附件");
							session.save(wbinfo);
						}
					}
				}
				/**=======================保存新件图片=============================*/
			}
			session.update(a);
			
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
	 * 急修/保养编号
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Path("/singleno/{data}")
	@Produces("application/json")
	public Response getgzlxList(
			@PathParam("data") JSONObject data) throws JSONException {
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		String singleno=CommonUtil.URLDecoder_decode((String) data.get("singleno"));
		String userid=CommonUtil.URLDecoder_decode((String) data.get("userid"));

		System.out.println(">>>>>>>>配件申请--急修/保养编号");
		
		try{
			session = HibernateUtil.getSession();
			String sql="select a.singleno,a.ElevatorNo,a.rem from "
					+ "(select cm.CalloutMasterNo as singleno,cm.ElevatorNo,isnull(ecl2.rem,'') as rem "
					+ "from CalloutMaster cm left join ElevatorCoordinateLocation ecl2 on cm.ElevatorNo=ecl2.ElevatorNo,"
					+ "CalloutProcess cp "
					+ "where HandleStatus in ('2','3') "
					+ "and cp.CalloutMasterNo=cm.CalloutMasterNo and cp.AssignObject2 ='"+userid+"' "
					+ "union all "
					+ "select b.singleno,m.ElevatorNo,isnull(ecl.rem,'') as rem "
					+ "from MaintenanceWorkPlanDetail b,MaintenanceWorkPlanMaster a,"
					+ "MaintContractDetail m left join ElevatorCoordinateLocation ecl on m.ElevatorNo=ecl.ElevatorNo "
					+ "where a.billno=b.billno and a.rowid=m.rowid and b.HandleStatus='2' and b.MaintPersonnel='"+userid+"') a "
					+ "where 1=1 ";
			if(singleno!=null && !singleno.trim().equals("")){
				sql+=" and a.singleno like '%"+singleno.trim()+"%'";
			}
			//System.out.println(">>"+sql);
			List list =session.createSQLQuery(sql).list();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					Object[] obj=(Object[])list.get(i);
					JSONObject jsonObject = new JSONObject(); 
					jsonObject.put("singleno", obj[0]);
					jsonObject.put("elevatorno", obj[1]);
					jsonObject.put("elenorem", obj[2]);
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
	/**
	 * 删除文件
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/deletefile")
	@Produces("application/json")
	public Response deleteFile(@FormParam("data") JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;

		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []

		System.out.println(">>>>>>>>配件申请--删除图片");
		
		Wbfileinfo wbfile=null;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			String filesid =(String) data.get("filesid");
			String imagedt=(String) data.get("imagedt");//删除类型  1=旧件图片，4=开票信息图片
			
			System.out.println(">>>>>>>>filesid="+filesid);

			//保存文件路径 合同交接资料附件表
			String folder = PropertiesUtil.getProperty("AccessoriesRequisitionReport.file.upload.folder");

			if(filesid!=null && filesid.length()>0){
				wbfile=(Wbfileinfo) session.get(Wbfileinfo.class,Integer.parseInt(filesid));
				String filepath=folder+wbfile.getFilePath()+wbfile.getNewFileName();
				session.delete(wbfile);
				session.flush();
				
				HandleFile hf = new HandleFileImpA();
				hf.delFile(filepath);
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
}
