package com.mobileserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import com.gzunicorn.file.HandleFile;
import com.gzunicorn.file.HandleFileImpA;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferDebugFileinfo;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferFeedback;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferFeedbackFileinfo;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferFileTypes;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferFileinfo;
import com.gzunicorn.hibernate.contracttransfer.ContractTransferMaster;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.workflow.bo.JbpmExtBridge;

/**
 * 手机APP端调用，合同交接资料上传类
 * @author Crunchify 
 */
@Path("/ContractTransfer")
public class ContractTransfer {

	/**
	 * 合同交接资料上传-列表
	 * @param userid
	 * @param elevatorno
	 * @param processstatus
	 * @return
	 */
	@POST
	@Path("/list")
	@Produces("application/json")
	public Response getList(@FormParam("data") JSONObject data){
		Session session = null;
		Loginuser user=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>合同交接资料上传-列表");
		
		try{
			String userid=(String) data.get("userid");
			String elevatorno =(String) data.get("elevatorno");//电梯编号
			String maintcontractno =(String) data.get("maintcontractno");//维保合同号
			String salescontractno =(String) data.get("salescontractno");//销售合同号
			//String companyid =(String) data.get("companyid");//甲方单位
			String transfesubmittype =(String) data.get("transfesubmittype");//上传提交标志
			//int pageno= (Integer) data.get("pageno");  //当前总数量
			
			session = HibernateUtil.getSession();
			
			List userList=session.createQuery("from Loginuser where userid ='"+userid+"'").list();
			if(userList!=null&&userList.size()>0){
				user=(Loginuser) userList.get(0);
			}
			String roleid=user.getRoleid();

			String sql ="select a.billno,a.companyid,d.companyName,a.maintcontractno,a.salescontractno,"
					+ "a.elevatorno,a.maintdivision,c.comname,a.maintstation,s.storagename,"
					+ "a.contractsdate,a.contractedate,a.operid,b.username,a.operdate,a.TransfeSubmitType,"
					+ "a.auditStatus,isnull(cb.OperDate,'') as cboperdate,"
					+ "isnull(a.AuditRem,'') as auditrem,isnull(a.AuditRem2,'') as auditrem2,"
					+ "isnull(a.isTrans,'N') as isTrans,isnull(a.AuditStatus2,'N') as AuditStatus2 "
					+ "from ContractTransferMaster a "
					+ "left join ContractTransferFeedback cb on a.BillNo=cb.BillNo "
					+ "and cb.OperDate=(select max(cfb.OperDate) from ContractTransferFeedback cfb where a.BillNo=cfb.BillNo),"
					+ "Loginuser b,Customer d,Company c,Storageid s "
					+ "where a.operId=b.userid and a.companyId=d.companyId "
					+ "and a.maintDivision=c.comid and a.maintStation=s.storageid and a.submitType='Y' ";
			
			if(roleid.trim().equals("A03")){
				//A03  维保经理,未转派的。
				sql += "and isnull(a.isTrans,'N')='N' and a.maintStation='"+user.getStorageid()+"' ";
			}else{
				//A50  维保工  ，维保经理转派给自己的
				sql += "and isnull(a.isTrans,'N')='Y' and isnull(a.wbgTransfeId,'')='"+userid+"' ";
			}
			
            if(!"".equals(elevatorno)){
            	sql +=" and a.elevatorNo like '%"+elevatorno.trim()+"%'";
            }
            if(!"".equals(maintcontractno)){
            	sql +=" and a.maintContractNo like '%"+maintcontractno.trim()+"%'";
            }
            if(!"".equals(salescontractno)){
            	sql +=" and a.salesContractNo like '%"+salescontractno.trim()+"%'";
            }
            if(!"".equals(transfesubmittype) && !"%".equals(transfesubmittype) && !transfesubmittype.trim().equals("S")){
            	sql+=" and isnull(a.transfeSubmitType,'N') like '"+transfesubmittype.trim()+"' "
            	   + " and isnull(a.auditStatus,'N')='N' ";
			}
			if(!"".equals(transfesubmittype) && !"%".equals(transfesubmittype) && transfesubmittype.trim().equals("S")){
				sql+=" and isnull(a.auditStatus,'N') like 'Y' ";
			}
			sql +=" order by a.billNo";
			
			//System.out.println(sql);
			
			List selectList=session.createSQLQuery(sql).list();
			if(selectList!=null&&selectList.size()>0){
				for(int i=0;i<selectList.size();i++){
					Object[] objs =(Object[]) selectList.get(i);
					JSONObject jsonObject=new JSONObject();
					
					jsonObject.put("billno",objs[0]);//流水号
					jsonObject.put("companyid",objs[1]);//甲方单位代码
            	  	jsonObject.put("companyname", objs[2]);
					jsonObject.put("maintcontractno",objs[3]);//维保合同号
					jsonObject.put("salescontractno",objs[4]);//销售合同号
					jsonObject.put("elevatorno",objs[5]);//电梯编号
					jsonObject.put("maintdivision",objs[6]);//所属维保分部
            		jsonObject.put("maintdivisionname",objs[7]);
					jsonObject.put("maintstation",objs[8]);//所属维保站
            	  	jsonObject.put("maintstationname",objs[9]);
					jsonObject.put("contractsdate",objs[10]);//合同开始日期
					jsonObject.put("contractedate",objs[11]);//合同结束日期
					jsonObject.put("operid",objs[12]);//派工人
            	  	jsonObject.put("username",objs[13]);
					jsonObject.put("operdate",objs[14]);//派工日期

					String tstname="";
            	  	String tstid=objs[15].toString();
					String auditStatus=objs[16].toString();
					if("Y".equals(auditStatus)){
						tstid="S";
						tstname="已审核";
					}else{
	            	  	if("Y".equals(tstid)){
	            	  		tstname="已提交";
	            	  	}else if("R".equals(tstid)){
	            	  		tstname="驳回";
	            	  	}else{
	            	  		tstname="未提交";
	            	  	}
					}
					jsonObject.put("transfesubmittype",tstid);//上传提交标志
            	  	jsonObject.put("transfesubmittypename", tstname);
            	  	jsonObject.put("cboperdate", objs[17]);
            	  	jsonObject.put("auditrem", objs[18]);//审核驳回意见
            	  	
            	  	String auditrem2=objs[19].toString();
            	  	String isTrans=objs[20].toString();
            	  	String AuditStatus2=objs[21].toString();
            	  	if("R".equals(tstid) && "Y".equals(isTrans) && "N".equals(AuditStatus2)){
            	  		//如果是驳回的，并且是转派的，并且是经理文员驳回的
            	  		jsonObject.put("auditrem", auditrem2);
            	  	}
            	  	
            	  	
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
	 * 合同交接资料上传-批量选择列表  batch
	 * @param userid
	 * @param elevatorno
	 * @param processstatus
	 * @return
	 */
	@POST
	@Path("/batchlist")
	@Produces("application/json")
	public Response getBatchList(@FormParam("data") JSONObject data){
		Session session = null;
		Connection conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		Loginuser user=null;
		
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>合同交接资料上传-批量选择列表");
		
		try{
			String userid=(String) data.get("userid");
			String elevatorno =(String) data.get("elevatorno");//电梯编号，维保合同号，销售合同号
			String maintcontractno =(String) data.get("maintcontractno");//维保合同号
			String salescontractno =(String) data.get("salescontractno");//销售合同号
			String billnoarr =(String) data.get("billnoarr");//销售合同号
			String operation_batch =(String) data.get("operation_batch");//批量操作
			
			//System.out.println(operation_batch);
			
			//int pageno= (Integer) data.get("pageno");  //当前总数量
			
			session = HibernateUtil.getSession();
			conn=session.connection();
			
			List userList=session.createQuery("from Loginuser where userid ='"+userid+"'").list();
			if(userList!=null&&userList.size()>0){
				user=(Loginuser) userList.get(0);
			}
			String roleid=user.getRoleid();

			//派工已提交的，上传未提交的，并且没有上传附件的。将附件类型 ‘多行拼成一行’
			String sql ="select a.billno,d.companyname,a.maintcontractno,a.salescontractno,a.elevatorno,"
					
					+ "(select jnlno= stuff((select ',' +jnlno from ContractTransferFileTypes t,pulldown b "
					+ "where t.BillNo=at.BillNo and t.FileType = b.pullid and b.typeflag = 'ContractFileTypes_FileType' order by b.orderby for xml path('')),1,1,'') "
					+ "from ContractTransferFileTypes at where at.BillNo=a.billNo group by billNo) as jnlnostr,"
					
					+ "(select FileType= stuff((select ',' +FileType from ContractTransferFileTypes t,pulldown b "
					+ "where t.BillNo=at.BillNo and t.FileType = b.pullid and b.typeflag = 'ContractFileTypes_FileType' order by b.orderby for xml path('')),1,1,'') "
					+ "from ContractTransferFileTypes at where at.BillNo=a.billNo group by billNo) as filetypestr, "
					
					+ "(select FileTypeName= stuff((select ',' +pullname from ContractTransferFileTypes t,pulldown b "
					+ "where t.BillNo=at.BillNo and t.FileType = b.pullid and b.typeflag = 'ContractFileTypes_FileType' order by b.orderby for xml path('')),1,1,'') "
					+ "from ContractTransferFileTypes at where at.BillNo=a.billNo group by billNo) as filetypenamestr "
					
					+ "from ContractTransferMaster a,Loginuser b,Customer d,Company c,Storageid s "
					+ "where a.operId=b.userid and a.companyId=d.companyId "
					+ "and a.maintDivision=c.comid and a.maintStation=s.storageid "
					+ "and a.submitType='Y' and a.TransfeSubmitType='N' ";

			if(roleid.trim().equals("A03")){
				//A03  维保经理,未转派的。
				sql += "and isnull(a.isTrans,'N')='N' and a.maintStation='"+user.getStorageid()+"' ";
			}else{
				//A50  维保工  ，维保经理转派给自己的
				sql += "and isnull(a.isTrans,'N')='Y' and isnull(a.wbgTransfeId,'')='"+userid+"' ";
			}
					
			if(!"".equals(operation_batch) && "批量上传".equals(operation_batch)){	
				//检查是否上传了附件
				sql += "and a.BillNo not in(select ct.BillNo from ContractTransferFileinfo cf,ContractTransferFileTypes ct where cf.jnlno=ct.jnlno and ct.BillNo=a.BillNo) ";
			}
			if(!"".equals(billnoarr)){
				billnoarr=billnoarr.replaceAll(",", "','");
            	sql +=" and a.billno not in('"+billnoarr+"')";
            }
			if(!"".equals(elevatorno)){
            	sql +=" and a.elevatorNo like '%"+elevatorno.trim()+"%'";
            }
            if(!"".equals(maintcontractno)){
            	sql +=" and a.maintContractNo like '%"+maintcontractno.trim()+"%'";
            }
            if(!"".equals(salescontractno)){
            	sql +=" and a.salesContractNo like '%"+salescontractno.trim()+"%'";
            }
			sql +=" order by a.billNo";
			
			//System.out.println(sql);
			
			ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            
            int i=0;
			while(rs.next()){
				JSONObject jsonObject=new JSONObject();
				
				jsonObject.put("billno",rs.getString("billno"));//流水号
				jsonObject.put("companyname",rs.getString("companyname"));//甲方单位
        	  	jsonObject.put("maintcontractno", rs.getString("maintcontractno"));//维保合同号
				jsonObject.put("salescontractno",rs.getString("salescontractno"));//销售合同号
				jsonObject.put("elevatorno",rs.getString("elevatorno"));//电梯编号
				jsonObject.put("jnlnostr",rs.getString("jnlnostr"));//附件类型流水号
				jsonObject.put("filetypestr",rs.getString("filetypestr"));//附件类型
				jsonObject.put("filetypenamestr",rs.getString("filetypenamestr"));//附件类型
        	  	
        		jobiArray.put(i, jsonObject);
        		i++;
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
	 * 合同交接资料上传-查看
	 * @param userid
	 * @param billno
	 * @return
	 */
	@POST
	@Path("/display")
	@Produces("application/json")
	public Response getDisplay(@FormParam("data") JSONObject data){
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>合同交接资料上传-进入查看编辑页面");
		try{
			session = HibernateUtil.getSession();
			
			String userid=(String) data.get("userid");
			String billno =(String) data.get("billno");
			
			JSONObject object=new JSONObject();
			object.put("billno",billno);//流水号
			//合同交接资料附件类型表
			String sql="select ct.jnlno,ct.billNo,ct.fileType,p.pullname,"
					+ "(select count(cf.jnlno) from ContractTransferFileinfo cf where cf.jnlno=ct.jnlno) as countnum "
					+ "from ContractTransferFileTypes ct,Pulldown p "
					+ "where ct.fileType=p.pullid and p.enabledflag='Y' "
					+ "and p.typeflag='ContractFileTypes_FileType' "
					+ "and ct.billNo='"+billno.trim()+"' "
					+ "order by p.orderby";
			//System.out.println(sql);
			JSONArray detaillist=new JSONArray();
			List qList=session.createSQLQuery(sql).list();
			if(qList!=null && qList.size()>0){
	            for(int i=0;i<qList.size();i++){
	            	
	            	Object[] ojbs=(Object[]) qList.get(i);
	            	
	        	  	JSONObject jsonObject=new JSONObject();
	        	  	String jnlno=ojbs[0].toString();
	        	  	jsonObject.put("jnlno", jnlno);
	        	  	jsonObject.put("billno", ojbs[1]);
	        	  	jsonObject.put("filetype", ojbs[2]);
	        	  	jsonObject.put("filetypename",ojbs[3]);
	        	  	
	        	  	//显示是否已上传附件
	        	  	String counum=ojbs[4].toString();
	        	  	String subname="";
	        	  	if(!"0".equals(counum)){
	        	  		subname="已上传附件";
	        	  	}
	        	  	jsonObject.put("sub",subname);
	
	        	  	detaillist.put(i, jsonObject);
			    }
	            object.put("detaillist",detaillist);
			}
            //合同交接资料反馈表
            sql="select a.jnlno,a.BillNo,a.OperId,a.OperDate,a.TransferRem,"
            		+ "l.username,isnull(a.FeedbackTypeId,'')  as FeedbackTypeId,"
            		+ "isnull(c.FeedbackTypeName,'') as FeedbackTypeName "
					+ "from ContractTransferFeedback a "
					+ "left join ContractTransferFeedbackType c on a.FeedbackTypeId=c.FeedbackTypeId,"
					+ "Loginuser l "
					+ "where  a.operId=l.userid and a.billNo='"+billno.trim()+"' "
					+ "order by a.operDate desc";
			//System.out.println(sql);
            JSONArray detaillist2=new JSONArray();
			List selectList=session.createSQLQuery(sql).list();
			if(selectList!=null && selectList.size()>0){
				for(int i=0;i<selectList.size();i++){
					
					Object[] objs =(Object[]) selectList.get(i);
					
					JSONObject jsonObject=new JSONObject();
					
					jsonObject.put("jnlno",objs[0]);//流水号
					jsonObject.put("billno",objs[1]);//流水号
					jsonObject.put("operid",objs[2]);//反馈人
					jsonObject.put("operdate",objs[3]);//反馈日期
					jsonObject.put("transferrem",objs[4]);//反馈内容
					jsonObject.put("opername",objs[5]);//反馈人
					jsonObject.put("feedbacktypeid",objs[6]);//反馈类型代码
					jsonObject.put("feedbacktypename",objs[7]);//反馈类型名称

					detaillist2.put(i, jsonObject);
				}
				object.put("detaillist2",detaillist2);
			}
			
			
			JSONArray debugfilelist=new JSONArray();
			//读取下载调试单路径
			String pathk="D:\\contract\\下载调试单路径.txt";
			BufferedReader readerk= new BufferedReader(new FileReader(pathk));
			String downloadaddrk=readerk.readLine();
			readerk.close();
			
			//派工上传的调试单
			String hqlk="from ContractTransferDebugFileinfo where billNo='"+billno.trim()+"'";
			List fileList=session.createQuery(hqlk).list();
			//System.out.println(">>>"+hql);
			int f=0;
			if(fileList!=null && fileList.size()>0){
				ContractTransferDebugFileinfo ctfdf=null;
				for(int j=0;j<fileList.size();j++){
					ctfdf=(ContractTransferDebugFileinfo)fileList.get(j);

					JSONObject objf=new JSONObject();
					objf.put("oldfilename", ctfdf.getOldFileName());
					objf.put("downloadaddrk", downloadaddrk+ctfdf.getFileSid()+"&filetype=1");
					
					debugfilelist.put(f,objf);
					f++;
				}
			}
			//其他系统同步的调试单
			String sqld="select a.FileSid,a.OldFileName from DebugSheetFileInfo a,ContractTransferMaster b "
					+ "where a.ElevatorNo=b.ElevatorNo and billNo='"+billno.trim()+"'";
			List fileList2=session.createSQLQuery(sqld).list();
			//System.out.println(">>>"+sql);
			if(fileList2!=null && fileList2.size()>0){
				for(int j=0;j<fileList2.size();j++){
					Object[] objs=(Object[])fileList2.get(j);

					JSONObject objf=new JSONObject();
					objf.put("oldfilename", objs[1]);
					objf.put("downloadaddrk", downloadaddrk+objs[0].toString()+"&filetype=2");
					
					debugfilelist.put(f,objf);
					f++;
				}
			}
			object.put("debugfilelist",debugfilelist);
			
			//读取下载厂检通知单路径
			String path="D:\\contract\\下载厂检通知单路径.txt";
			BufferedReader reader= new BufferedReader(new FileReader(path));
			String downloadaddr=reader.readLine();
			reader.close();
			//查询安装维保交接电梯情况，是否有该电梯。处理状态 【2：已登记已提交，3：已审核】
			String sqlsel="select a.billno,a.ProcessStatus "
					+ "from ElevatorTransferCaseRegister a,ContractTransferMaster b "
					+ "where a.ElevatorNo=b.ElevatorNo "
					+ "and b.billno='"+billno.trim()+"' "
					+ "and a.ProcessStatus in('2','3') "
					+ "order by a.CheckNum desc";
			List sellist=session.createSQLQuery(sqlsel).list();
			
			if(sellist!=null && sellist.size()>0){
				Object[] obj=(Object[])sellist.get(0);
				downloadaddr+=obj[0].toString();
			}else{
				downloadaddr="";
			}
			object.put("downloadaddr",downloadaddr);
			
			jobiArray.put(0, object);
            
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
	 * 合同交接资料上传-创建，批量保存 batch
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	@POST
	@Path("/savenew")
	@Produces("application/json")
	public Response saveNew (@FormParam("data")JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;
		ContractTransferMaster ctm =null;
		ContractTransferFileinfo fileInfo=null;
		ContractTransferFileTypes ctft=null;

		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		JbpmExtBridge jbpmExtBridge=null;
	
		System.out.println(">>>>>>>>合同交接资料上传-保存主信息");
		try{
			session=HibernateUtil.getSession();
			tx=session.beginTransaction();
			
			String billno=(String) data.get("billno");//流水号
			String userid=(String) data.get("userid");//上传人
			String transfesubmittype=(String) data.get("transfesubmittype");//提交标志
			
			String[] billnoarr=billno.split(",");//批量保存驳回
			String transfedate=CommonUtil.getNowTime();//上传时间
			
			for(int b=0;b<billnoarr.length;b++){
				ctm =(ContractTransferMaster)session.get(ContractTransferMaster.class, billnoarr[b]);
				ctm.setTransfeId(userid);
				ctm.setTransfeDate(transfedate);
				ctm.setTransfeSubmitType(transfesubmittype);
				
				ctm.setAuditOperid2("");
				ctm.setAuditDate2("");
				ctm.setAuditStatus2("N");
				ctm.setAuditRem2("");
				
				session.save(ctm);
			}
			
			//批量保存明细 ,判断 JSONObject 是否包含  detaillist
			if(data.has("detaillist")){
				String folder="ContractTransferFileinfo.file.upload.folder";
				folder = PropertiesUtil.getProperty(folder).trim();
				
				JSONArray detaillist=(JSONArray) data.get("detaillist");//附件类型
				for(int i=0;i<detaillist.length();i++){
					JSONObject objk=(JSONObject) detaillist.get(i);
					
					String filetype=(String) objk.get("filetype");
					JSONArray imagelist=(JSONArray)objk.get("imglist");
					//合同交接资料附件表
					if(imagelist!=null && imagelist.length()>0){
						String curdate=DateUtil.getDateTime("yyyyMMddHHmmss");
						for(int j=0;j<imagelist.length();j++){
							JSONObject object=(JSONObject) imagelist.get(j);
							String imgpic=(String) object.get("ImgBase64");//流水号
							String[] imgpicarr=imgpic.split(",");
							
							byte[] image=new BASE64Decoder().decodeBuffer(imgpicarr[1]);
							String newfilename=userid+"_"+curdate+"_"+i+"_"+j+".jpg";
							String filepath=CommonUtil.getNowTime("yyyy-MM-dd")+"/";
		
							//保存图片
							File f=new File(folder+"/"+filepath);
							f.mkdirs();
							FileOutputStream fos=new FileOutputStream(folder+"/"+filepath+newfilename);
							fos.write(image);
							fos.flush();
							fos.close();
							
							for(int k=0;k<billnoarr.length;k++){
								String billnostr=billnoarr[k];
								
								String sqld="from ContractTransferFileTypes where billNo='"+billnostr+"' and fileType='"+filetype+"'";
								List relist=session.createQuery(sqld).list();
								ctft=(ContractTransferFileTypes)relist.get(0);

								System.out.println(">>>>>>>>billno="+billnostr+";filetype="+filetype+";jnlno="+ctft.getJnlno());
								
								//保存图片信息到数据库
								fileInfo=new ContractTransferFileinfo();
								fileInfo.setJnlno(ctft.getJnlno());
								fileInfo.setOldFileName(newfilename);
								fileInfo.setNewFileName(newfilename);
								fileInfo.setFileSize(Double.valueOf(0));
								fileInfo.setFilePath(filepath);
								fileInfo.setFileFormat("-");
								fileInfo.setUploadDate(transfedate);
								fileInfo.setUploader(userid);
								session.save(fileInfo);
								
							}
						}
					}
				}
				
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
	 * 保存图片信息合同交接资料附件表,批量保存上传图片 batch
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/imagesave")
	@Produces("application/json")
	public Response saveImageInfo (@FormParam("data") JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;
	
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []

		System.out.println(">>>>>>>>合同交接资料上传-保存合同交接资料附件表图片");
		
		ContractTransferFileinfo fileInfo=null;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			String userid=(String) data.get("userid");
			String jnlno =(String) data.get("jnlno");
			JSONArray imagelist=(JSONArray) data.get("imagelist");//上传图片
			
			String[] jnlnoarr=jnlno.split(",");//批量保存驳回
			
			String folder="ContractTransferFileinfo.file.upload.folder";
			folder = PropertiesUtil.getProperty(folder).trim();
			String transfedate=CommonUtil.getNowTime();//上传时间
			
			for(int b=0;b<jnlnoarr.length;b++){
				System.out.println(">>>>>>>>jnlno = "+jnlnoarr[b]);
				//合同交接资料附件表
				if(imagelist!=null && imagelist.length()>0){
					String curdate=DateUtil.getDateTime("yyyyMMddHHmmss");
					for(int j=0;j<imagelist.length();j++){
						JSONObject object=(JSONObject) imagelist.get(j);
						String imgpic=(String) object.get("imgpic");//流水号
						
						byte[] image=new BASE64Decoder().decodeBuffer(imgpic);
						String newfilename=userid+"_"+curdate+"_"+b+"_"+j+".jpg";
						String filepath=CommonUtil.getNowTime("yyyy-MM-dd")+"/";
	
						//保存图片
						File f=new File(folder+"/"+filepath);
						f.mkdirs();
						FileOutputStream fos=new FileOutputStream(folder+"/"+filepath+newfilename);
						fos.write(image);
						fos.flush();
						fos.close();
						
						//保存图片信息到数据库
						fileInfo=new ContractTransferFileinfo();
						fileInfo.setJnlno(jnlnoarr[b]);
						fileInfo.setOldFileName(newfilename);
						fileInfo.setNewFileName(newfilename);
						fileInfo.setFileSize(Double.valueOf(0));
						fileInfo.setFilePath(filepath);
						fileInfo.setFileFormat("-");
						fileInfo.setUploadDate(transfedate);
						fileInfo.setUploader(userid);
						session.save(fileInfo);
					}
				}
			}
			tx.commit();
			
			//返回 安装维保交接电梯情况登记项目 的 流水号
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
	 * 获取合同交接资料附件表
	 * @param userid
	 * @param elevatortype
	 * @param issuecoding
	 * @return
	 */
	@POST
	@Path("/getimagelist")
	@Produces("application/json")
	public Response getImageList(@FormParam("data") JSONObject data){
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray imgarr=new JSONArray();//代表数组 []
		
		ContractTransferFileinfo cttfile=null;
		BASE64Encoder base=new BASE64Encoder();
		
		System.out.println(">>>>>>>>合同交接资料上传-获取合同交接资料附件表图片");
		
		//保存文件路径
		String folder="ContractTransferFileinfo.file.upload.folder";
		folder = PropertiesUtil.getProperty(folder).trim();
		try{
			
			String userid=(String) data.get("userid");
			String jnlno =(String) data.get("jnlno");
			
			session = HibernateUtil.getSession();

    	  	String hql="from ContractTransferFileinfo where jnlno='"+jnlno.trim()+"'";
			List fileList=session.createQuery(hql).list();
			//System.out.println(">>>"+hql);
			
			if(fileList!=null&&fileList.size()>0){
				for(int j=0;j<fileList.size();j++){
					cttfile=(ContractTransferFileinfo)fileList.get(j);
					String filepath=folder+"/"+cttfile.getFilePath()+cttfile.getNewFileName();
					//将图片转换为二进制流
					byte[] imgbyte=CommonUtil.imageToByte(filepath);
					
					JSONObject objf=new JSONObject();
					objf.put("filesid", cttfile.getFileSid());
					objf.put("oldfilename", cttfile.getOldFileName());
					objf.put("imgpic", base.encode(imgbyte));//将二进制流加密
					
					imgarr.put(j,objf);
				}
			}
			
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
      		rejson.put("data", imgarr);
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

		System.out.println(">>>>>>>>合同交接资料上传-删除合同交接资料附件表图片");
		
		ContractTransferFileinfo ctfile=null;
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			String userid=(String) data.get("userid");
			String filesid =(String) data.get("filesid");

			//保存文件路径 合同交接资料附件表
			String folder="ContractTransferFileinfo.file.upload.folder";
			folder = PropertiesUtil.getProperty(folder).trim();

			if(filesid!=null && filesid.length()>0){
				ctfile=(ContractTransferFileinfo) session.get(ContractTransferFileinfo.class,Integer.valueOf(filesid));
				String filepath=ctfile.getFilePath();
				String filename=ctfile.getNewFileName();
				session.delete(ctfile);
				session.flush();
				
				HandleFile hf = new HandleFileImpA();
				String localpath=folder+"/"+filepath+filename;
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
	
	/**
	 * 合同交接资料上传-获取历史反馈图片
	 * @param userid
	 * @param elevatortype
	 * @param issuecoding
	 * @return markingscore
	 */
	@POST
	@Path("/getfkimage")
	@Produces("application/json")
	public Response getFkImage(@FormParam("data") JSONObject data){
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray imgarr=new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>合同交接资料上传-获取历史反馈图片");
		
		String folder="ContractTransferFeedbackFileinfo.file.upload.folder";
		folder = PropertiesUtil.getProperty(folder).trim();
		
		ContractTransferFeedbackFileinfo ctffile=null;
		BASE64Encoder base=new BASE64Encoder();
		try{
			session = HibernateUtil.getSession();
			
			String userid=(String) data.get("userid");
			String jnlno=(String) data.get("jnlno");//流水号

			String hql="from ContractTransferFeedbackFileinfo h where h.jnlno ='"+jnlno+"' ";
			//System.out.println(hql);
			
			List fileList=session.createQuery(hql).list();

			if(fileList!=null&&fileList.size()>0){
				for(int j=0;j<fileList.size();j++){
					ctffile=(ContractTransferFeedbackFileinfo)fileList.get(j);
					String filepath=folder+"/"+ctffile.getFilePath()+ctffile.getNewFileName();
					//将图片转换为二进制流
					byte[] imgbyte=CommonUtil.imageToByte(filepath);
					
					JSONObject objf=new JSONObject();
					objf.put("filesid", ctffile.getFileSid());
					objf.put("oldfilename", ctffile.getOldFileName());
					objf.put("imgpic", base.encode(imgbyte));//将二进制流加密
					
					imgarr.put(j,objf);
				}
			}
			
			json.put("code", "200");
  			json.put("info", "OK");
            rejson.put("status", json);
      		rejson.put("data", imgarr);
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
	 * 合同交接资料上传-保存反馈进度，批量保存反馈 batch
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	@POST
	@Path("/savefknew")
	@Produces("application/json")
	public Response saveFkNew (@FormParam("data")JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;
		ContractTransferFeedback ctf = null;
		ContractTransferFeedbackFileinfo fileInfo=null;

		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>合同交接资料上传-保存反馈进度");
		
		try{
			session=HibernateUtil.getSession();
			tx=session.beginTransaction();
			
			String billno=(String) data.get("billno");//流水号
			String userid=(String) data.get("userid");//反馈人
			String feedbacktypeid=(String) data.get("feedbacktypeid");//反馈问题类型
			String transferrem=(String) data.get("transferrem");//反馈内容
			JSONArray imagelist=(JSONArray) data.get("imagelist");//图片数组
			
			String[] billnoarr=billno.split(",");//批量保存反馈
			
			//保存图片
			String folder="ContractTransferFeedbackFileinfo.file.upload.folder";
			folder = PropertiesUtil.getProperty(folder).trim();
			String opdate=CommonUtil.getNowTime();//反馈时间
			List piclist=new ArrayList();
			//合同交接资料反馈附件表
			if(imagelist!=null && imagelist.length()>0){
				String curdate=DateUtil.getDateTime("yyyyMMddHHmmss");//当前日期时间
				for(int j=0;j<imagelist.length();j++){
					JSONObject object=(JSONObject) imagelist.get(j);
					String imgpic=(String) object.get("imgpic");//流水号
					
					byte[] image=new BASE64Decoder().decodeBuffer(imgpic);
					String newfilename=userid+"_"+curdate+"_"+j+".jpg";
					String filepath=CommonUtil.getNowTime("yyyy-MM-dd")+"/";

					//保存图片
					File f=new File(folder+"/"+filepath);
					f.mkdirs();
					FileOutputStream fos=new FileOutputStream(folder+"/"+filepath+newfilename);
					fos.write(image);
					fos.flush();
					fos.close();
					
					HashMap hm=new HashMap();
					hm.put("newfilename", newfilename);
					hm.put("filepath", filepath);
					
					piclist.add(hm);
					
				}
			}
			
			//批量保存
			for(int b=0;b<billnoarr.length;b++){
				String jnlno = CommonUtil.getBillno(CommonUtil.getToday().substring(2,4),"ContractTransferFeedback", 1)[0];
				ctf =new ContractTransferFeedback();
				ctf.setBillNo(billnoarr[b]);
				ctf.setJnlno(jnlno);
				ctf.setOperId(userid);
				ctf.setOperDate(opdate);
				ctf.setFeedbacktypeid(feedbacktypeid);
				ctf.setTransferRem(transferrem);
				session.save(ctf);

				System.out.println(">>>>>>>>billno = "+billnoarr[b]+";jnlno="+jnlno);
				
				//合同交接资料反馈附件表
				if(piclist!=null && piclist.size()>0){
					for(int j=0;j<piclist.size();j++){
						HashMap hmk=(HashMap)piclist.get(j);
						String newfilename=(String)hmk.get("newfilename");
						String filepath=(String)hmk.get("filepath");
						//保存图片信息到数据库
						fileInfo=new ContractTransferFeedbackFileinfo();
						fileInfo.setJnlno(jnlno);
						fileInfo.setOldFileName(newfilename);
						fileInfo.setNewFileName(newfilename);
						fileInfo.setFileSize(Double.valueOf(0));
						fileInfo.setFilePath(filepath);
						fileInfo.setFileFormat("-");
						fileInfo.setUploadDate(opdate);
						fileInfo.setUploader(userid);
						session.save(fileInfo);
						
					}
				}
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
	 * 点击驳回按钮，批量驳回 batch 
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/savebh")
	@Produces("application/json")
	public Response saveBh (@FormParam("data") JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;
		ContractTransferMaster ctm=null;

		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>合同交接资料上传-驳回保存");
		
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			String userid=(String) data.get("userid");
			String billno=(String) data.get("billno");
			String transferrem=(String) data.get("transferrem");//驳回原因
			
			String[] billnoarr=billno.split(",");//批量保存驳回
			String transfedate=CommonUtil.getNowTime();//上传时间
			
			//批量保存
			for(int b=0;b<billnoarr.length;b++){
				System.out.println(">>>>>>>>billno = "+billnoarr[b]);
				ctm=(ContractTransferMaster) session.get(ContractTransferMaster.class, billnoarr[b]);
				ctm.setSubmitType("R");
				ctm.setTransfeId(userid);
				ctm.setTransfeDate(transfedate);
				ctm.setTransferRem(transferrem);
				
				ctm.setIsTrans("N");//是否转派
				ctm.setIsTransDate("");
				ctm.setIsTransId("");
				ctm.setWbgTransfeId("");
				
	            session.update(ctm);
	            
	            //删除 合同交接资料附件表
	            String delsql="delete ContractTransferFileinfo h "
	            		+ "where h.jnlno in(select jnlno from ContractTransferFileTypes h where h.billNo='"+billnoarr[b]+"')";
	            session.createQuery(delsql).executeUpdate();
	            //删除 合同交接资料反馈附件表
	            String delsql2="delete ContractTransferFeedbackFileinfo h "
	            		+ "where h.jnlno in(select jnlno from ContractTransferFeedback h where h.billNo='"+billnoarr[b]+"')";
	            session.createQuery(delsql2).executeUpdate();
	            //删除 合同交接资料反馈表
	            String delsql3="delete ContractTransferFeedback h where h.billNo='"+billnoarr[b]+"'";
	            session.createQuery(delsql3).executeUpdate();
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
	
	/**
	 * 合同交接资料上传-获取调试单
	 * @param userid
	 * @param billno
	 * @return
	 */
	@POST
	@Path("/debugfilelist")
	@Produces("application/json")
	public Response getDebugFileList(@FormParam("data") JSONObject data){
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>合同交接资料上传-获取调试单");
		try{
			session = HibernateUtil.getSession();
			
			String userid=(String) data.get("userid");
			String billno =(String) data.get("billno");

			//读取下载调试单路径
			String pathk="D:\\contract\\下载调试单路径.txt";
			BufferedReader readerk= new BufferedReader(new FileReader(pathk));
			String downloadaddrk=readerk.readLine();
			readerk.close();
			
			//派工上传的调试单
			String hqlk="from ContractTransferDebugFileinfo where billNo='"+billno.trim()+"'";
			List fileList=session.createQuery(hqlk).list();
			//System.out.println(">>>"+hql);
			int f=0;
			if(fileList!=null && fileList.size()>0){
				ContractTransferDebugFileinfo ctfdf=null;
				for(int j=0;j<fileList.size();j++){
					ctfdf=(ContractTransferDebugFileinfo)fileList.get(j);

					JSONObject objf=new JSONObject();
					objf.put("oldfilename", ctfdf.getOldFileName());
					objf.put("downloadaddrk", downloadaddrk+ctfdf.getFileSid()+"&filetype=1");
					
					jobiArray.put(f,objf);
					f++;
				}
			}
			//其他系统同步的调试单
			String sqld="select a.FileSid,a.OldFileName from DebugSheetFileInfo a,ContractTransferMaster b "
					+ "where a.ElevatorNo=b.ElevatorNo and billNo='"+billno.trim()+"'";
			List fileList2=session.createSQLQuery(sqld).list();
			//System.out.println(">>>"+sql);
			if(fileList2!=null && fileList2.size()>0){
				for(int j=0;j<fileList2.size();j++){
					Object[] objs=(Object[])fileList2.get(j);

					JSONObject objf=new JSONObject();
					objf.put("oldfilename", objs[1]);
					objf.put("downloadaddrk", downloadaddrk+objs[0].toString()+"&filetype=2");
					
					jobiArray.put(f,objf);
					f++;
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
	 * 合同交接资料上传-获取反馈类型
	 * @param userid
	 * @param billno
	 * @return
	 */
	@POST
	@Path("/feedbacktypelist")
	@Produces("application/json")
	public Response getFeedbackTypeList(@FormParam("data") JSONObject data){
		Session session = null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>合同交接资料上传-获取反馈类型");
		try{
			session = HibernateUtil.getSession();
			
			String userid=(String) data.get("userid");
			String feedbacktypename =(String) data.get("feedbacktypename");
			if("".equals(feedbacktypename)){
				feedbacktypename="%";
			}
			
			//合同交接资料反馈类型表
            String sql="select FeedbackTypeId,FeedbackTypeName from ContractTransferFeedbackType "
            		+ "where FeedbackTypeName like '%"+feedbacktypename+"%' and EnabledFlag='Y'";
            //System.out.println(sql);
			List typeList=session.createSQLQuery(sql).list();
			if(typeList!=null && typeList.size()>0){
				for(int i=0;i<typeList.size();i++){
					
					Object[] objs =(Object[]) typeList.get(i);
					
					JSONObject jsonObject=new JSONObject();
					
					jsonObject.put("feedbacktypeid",objs[0]);//反馈类型代码
					jsonObject.put("feedbacktypename",objs[1]);//反馈类型名称

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
            } catch (HibernateException hex) {
                DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
            }

        }

		
		return Response.status(200).entity(rejson.toString()).build();
	}
	/**
	 * 点击转派按钮，批量转派 batch 
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/savezp")
	@Produces("application/json")
	public Response saveZp(@FormParam("data") JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;
		ContractTransferMaster ctm=null;

		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>合同交接资料上传-转派保存");
		
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			String userid=(String) data.get("userid");
			String billno=(String) data.get("billno");
			String wbgtransfeid=(String) data.get("wbgtransfeid");//转派的维保工
			
			String[] billnoarr=billno.split(",");//批量保存
			String curdate=CommonUtil.getNowTime();//上传时间
			
			//批量保存
			for(int b=0;b<billnoarr.length;b++){
				System.out.println(">>>>>>>>billno = "+billnoarr[b]);
				ctm=(ContractTransferMaster) session.get(ContractTransferMaster.class, billnoarr[b]);
				ctm.setWbgTransfeId(wbgtransfeid);
				ctm.setIsTrans("Y");
				ctm.setIsTransId(userid);
				ctm.setIsTransDate(curdate);
	            session.update(ctm);
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
	/**
	 * 合同交接资料上传-获取维保工
	 * @param userid
	 * @param rowed
	 * @return
	 */
	@POST
	@Path("/getmaintpersonnel")
	@Produces("application/json")
	public Response getMaintPersonnel(@FormParam("data") JSONObject data){
		Session session = null;
		Loginuser user=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>合同交接资料上传-获取维保工"); 
		
		try{
			String userid=(String) data.get("userid");
			
			session = HibernateUtil.getSession();
			
			List userList=session.createQuery("from Loginuser where userid ='"+userid+"'").list();
			if(userList!=null&&userList.size()>0){
				user=(Loginuser) userList.get(0);
			}
			
			//维保工A50,维保站长A49,维保经理 A03，维修技术员A53 
			String sql ="select UserID,UserName,phone from LoginUser "
					+ "where StorageID like '"+user.getStorageid()+"%' and EnabledFlag='Y' "
					+ "and RoleID in('A50','A49','A53')";
			//System.out.println(">>>>"+sql); 
			List selectList=session.createSQLQuery(sql).list();
			if(selectList!=null&&selectList.size()>0){
				for(int i=0;i<selectList.size();i++){
					Object[] objs =(Object[]) selectList.get(i);
					JSONObject jsonObject=new JSONObject();
            	  	jsonObject.put("newzpuserid", objs[0]);
            	  	jsonObject.put("newzpusername", objs[1]);
            	  	jsonObject.put("newzpusertel",objs[2]);
            	  	
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
	 * 合同交接资料上传-审核列表
	 * @param userid
	 * @param elevatorno
	 * @param processstatus
	 * @return
	 */
	@POST
	@Path("/auditlist")
	@Produces("application/json")
	public Response getAuditList(@FormParam("data") JSONObject data){
		Session session = null;
		Loginuser user=null;
		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>合同交接资料上传审核-列表");
		
		try{
			String userid=(String) data.get("userid");
			String elevatorno =(String) data.get("elevatorno");//电梯编号
			String maintcontractno =(String) data.get("maintcontractno");//维保合同号
			String salescontractno =(String) data.get("salescontractno");//销售合同号
			//String companyid =(String) data.get("companyid");//甲方单位
			String transfesubmittype =(String) data.get("transfesubmittype");//上传提交标志
			//int pageno= (Integer) data.get("pageno");  //当前总数量
			
			session = HibernateUtil.getSession();
			
			List userList=session.createQuery("from Loginuser where userid ='"+userid+"'").list();
			if(userList!=null&&userList.size()>0){
				user=(Loginuser) userList.get(0);
			}
			String roleid=user.getRoleid();

			String sql ="select a.billno,a.companyid,d.companyName,a.maintcontractno,a.salescontractno,"
					+ "a.elevatorno,a.maintdivision,c.comname,a.maintstation,s.storagename,"
					+ "a.contractsdate,a.contractedate,a.operid,b.username,a.operdate,a.TransfeSubmitType,"
					+ "a.auditStatus,isnull(cb.OperDate,'') as cboperdate,"
					+ "isnull(a.AuditRem,'') as auditrem,isnull(a.AuditRem2,'') as auditrem2,"
					+ "isnull(a.isTrans,'N') as isTrans,isnull(a.AuditStatus2,'N') as AuditStatus2 "
					+ "from ContractTransferMaster a "
					+ "left join ContractTransferFeedback cb on a.BillNo=cb.BillNo "
					+ "and cb.OperDate=(select max(cfb.OperDate) from ContractTransferFeedback cfb where a.BillNo=cfb.BillNo),"
					+ "Loginuser b,Customer d,Company c,Storageid s "
					+ "where a.operId=b.userid and a.companyId=d.companyId "
					+ "and a.maintDivision=c.comid and a.maintStation=s.storageid and a.submitType='Y' "
					+ "and isnull(a.isTrans,'N')='Y' and isnull(a.transfeSubmitType,'N')='Y' "
					+ "and a.maintStation='"+user.getStorageid()+"' ";
			
            if(!"".equals(elevatorno)){
            	sql +=" and a.elevatorNo like '%"+elevatorno.trim()+"%'";
            }
            if(!"".equals(maintcontractno)){
            	sql +=" and a.maintContractNo like '%"+maintcontractno.trim()+"%'";
            }
            if(!"".equals(salescontractno)){
            	sql +=" and a.salesContractNo like '%"+salescontractno.trim()+"%'";
            }
            if(!"".equals(transfesubmittype)){
            	sql +=" and isnull(a.AuditStatus2,'N') like '"+transfesubmittype.trim()+"'";
            }

			sql +=" order by a.billNo";
			
			//System.out.println(sql);
			
			List selectList=session.createSQLQuery(sql).list();
			if(selectList!=null&&selectList.size()>0){
				for(int i=0;i<selectList.size();i++){
					Object[] objs =(Object[]) selectList.get(i);
					JSONObject jsonObject=new JSONObject();
					
					jsonObject.put("billno",objs[0]);//流水号
					jsonObject.put("companyid",objs[1]);//甲方单位代码
            	  	jsonObject.put("companyname", objs[2]);
					jsonObject.put("maintcontractno",objs[3]);//维保合同号
					jsonObject.put("salescontractno",objs[4]);//销售合同号
					jsonObject.put("elevatorno",objs[5]);//电梯编号
					jsonObject.put("maintdivision",objs[6]);//所属维保分部
            		jsonObject.put("maintdivisionname",objs[7]);
					jsonObject.put("maintstation",objs[8]);//所属维保站
            	  	jsonObject.put("maintstationname",objs[9]);
					jsonObject.put("contractsdate",objs[10]);//合同开始日期
					jsonObject.put("contractedate",objs[11]);//合同结束日期
					jsonObject.put("operid",objs[12]);//派工人
            	  	jsonObject.put("username",objs[13]);
					jsonObject.put("operdate",objs[14]);//派工日期

					String tstname="";
            	  	String tstid=objs[15].toString();
            	  	
					jsonObject.put("transfesubmittype",tstid);//上传提交标志
            	  	jsonObject.put("transfesubmittypename", tstname);
            	  	jsonObject.put("cboperdate", objs[17]);
            	  	//jsonObject.put("auditrem", objs[18]);//审核驳回意见

            	  	jsonObject.put("auditrem", objs[19]);//审核驳回意见
            	  	String auditstatus2name="";
            	  	String AuditStatus2=objs[21].toString();
            	  	if("Y".equals(AuditStatus2)){
            	  		auditstatus2name="已审核";
            	  	}else{
            	  		auditstatus2name="未审核";
            	  	}
            	  	jsonObject.put("auditstatus2", AuditStatus2);
            	  	jsonObject.put("auditstatus2name", auditstatus2name);

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
	 * 点击转派按钮，批量转派 batch 
	 * @param data
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Path("/saveaudit")
	@Produces("application/json")
	public Response saveAudit(@FormParam("data") JSONObject data) throws JSONException{
		Session session = null;
		Transaction tx = null;
		ContractTransferMaster ctm=null;

		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []
		
		System.out.println(">>>>>>>>合同交接资料上传审核-保存");
		
		try{
			session = HibernateUtil.getSession();
			tx = session.beginTransaction();
			
			String userid=(String) data.get("userid");
			String billno=(String) data.get("billno");
			String auditstatus2=(String) data.get("auditstatus2");//审核状态
			String auditrem2=(String) data.get("auditrem2");//审核意见
			
			String curdate=CommonUtil.getNowTime();//上传时间

			ctm=(ContractTransferMaster) session.get(ContractTransferMaster.class, billno);
			ctm.setAuditOperid2(userid);
			ctm.setAuditDate2(curdate);
			ctm.setAuditRem2(auditrem2);
			
			if("R".equals(auditstatus2)){
				ctm.setTransfeSubmitType("R");
				ctm.setAuditStatus2("N");
			}else{
				ctm.setAuditStatus2(auditstatus2);
			}
            session.update(ctm);

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
