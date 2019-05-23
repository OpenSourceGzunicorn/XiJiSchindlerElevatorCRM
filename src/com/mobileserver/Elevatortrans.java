package com.mobileserver;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
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
import com.gzunicorn.hibernate.basedata.handoverelevatorcheckitem.HandoverElevatorCheckItem;
import com.gzunicorn.hibernate.infomanager.elevatortransfercasebhtype.ElevatorTransferCaseBhType;
import com.gzunicorn.hibernate.infomanager.elevatortransfercaseprocess.ElevatorTransferCaseProcess;
import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;
import com.gzunicorn.hibernate.infomanager.handoverelevatorcheckfileinfo.HandoverElevatorCheckFileinfo;
import com.gzunicorn.hibernate.infomanager.handoverelevatorcheckitemregister.HandoverElevatorCheckItemRegister;
import com.gzunicorn.hibernate.infomanager.handoverelevatorspecialregister.HandoverElevatorSpecialRegister;
import com.gzunicorn.hibernate.sysmanager.Loginuser;
import com.gzunicorn.hibernate.sysmanager.pulldown.Pulldown;
import com.gzunicorn.workflow.bo.JbpmExtBridge;

/**
 * 手机APP端调用，安装维保交接电梯登记类
 * @author Elevatortrans
 */
@Path("/Elevatortrans")
public class Elevatortrans {
	
	BaseDataImpl bd = new BaseDataImpl();
	
	/**
     * 安装维保交接电梯登记-列表=处理状态
     * @param userid
     * @param elevatorno
     * @return
     */
	@GET
	@Path("/proStatuslist/{userid}")
	@Produces("application/json")
	public Response getProStatuslist(
			@PathParam("userid") String userid
			){

		JSONObject rejson = new JSONObject();
		JSONObject json = new JSONObject();//代表对象 {}
		JSONArray jobiArray = new JSONArray();//代表数组 []

		/** 0：未接收,4：已接收,1：已登记未提交,2：已登记已提交,3：已审核  */
		
		String[] proarr={"0","4","1","2","3"};
		String[] proarrname={"未接收","已接收","已登记未提交","已登记已提交","已审核"};
		try{
			JSONObject object=null;
			for(int i=0;i<proarr.length;i++){
				object=new JSONObject();
				object.put("statusid", proarr[i]);//处理状态
				object.put("statusname", proarrname[i]);//处理状态
				jobiArray.put(i, object);
			}
				
			json.put("code", "200");
			json.put("info", "OK");
	        rejson.put("status", json);
	  		rejson.put("data", jobiArray);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return Response.status(200).entity(rejson.toString()).build();
	}
	
	        /**
	         * 安装维保交接电梯登记-列表
	         * @param userid
	         * @param elevatorno
	         * @return
	         */
			@POST
			@Path("/azwblist")
			@Produces("application/json")
			public Response getAzwbList(@FormParam("data") JSONObject data){
				Session session = null;

				JSONObject rejson = new JSONObject();
				JSONObject json = new JSONObject();//代表对象 {}
				JSONArray jobiArray = new JSONArray();//代表数组 []
				try{

					String userid=(String) data.get("userid");
					String elevatorno =(String) data.get("elevatorno");
					String processstatus =(String) data.get("processstatus");
					
					session = HibernateUtil.getSession();

					String sql="from ElevatorTransferCaseRegister e where e.submitType in('Y','Z') " +
							"and e.staffName='"+userid+"'";
					if(elevatorno!=null&&!elevatorno.trim().equals("")){
						sql+=" and e.elevatorNo like '%"+elevatorno.trim()+"%' ";
					}
					if(processstatus!=null && !processstatus.trim().equals("") && !processstatus.trim().equals("5")){
						sql+=" and e.processStatus like '"+processstatus.trim()+"' ";
					}
					if(processstatus!=null && !processstatus.trim().equals("") && processstatus.trim().equals("5")){
						sql+=" and e.status=0 ";
					}
					
					sql+=" order by e.billno desc";
					
					//System.out.println("安装维保交接电梯登记-列表="+sql);
					
					//读取下载厂检通知单路径
					String path="D:\\contract\\下载厂检通知单路径.txt";
					BufferedReader reader= new BufferedReader(new FileReader(path));
					String downloadaddr=reader.readLine();
					reader.close();
					//String downloadaddr="http://10.10.0.5:8080/XJSCRM/PrintDisposeServlet?id=";//测试
					//String downloadaddr="http://www.xjelevator.com:9000/XJSCRM/PrintDisposeServlet?id=";//正式
					
					List eList=session.createQuery(sql).list();
					if(eList!=null&&eList.size()>0){
						for(int i=0;i<eList.size();i++){
							ElevatorTransferCaseRegister e=(ElevatorTransferCaseRegister) eList.get(i);
							JSONObject object=new JSONObject();
							object.put("billno", e.getBillno());//流水号
							object.put("checknum", e.getCheckNum());//厂检次数
							object.put("elevatorno", e.getElevatorNo());//电梯编号
							object.put("salescontractno", e.getSalesContractNo());//销售合同号
							object.put("projectname", e.getProjectName());//项目名称
							object.put("projectaddress", e.getProjectAddress());//项目地址
							
							int status=e.getStatus();//流程状态
							String statusid=e.getProcessStatus();
							String statusname="";
							if(statusid.equals("0")){
								statusname="未接收";
							}else if(statusid.equals("4")){
								statusname="已接收";
							}else if(statusid.equals("1")){
								statusname="已登记未提交";
							}else if(statusid.equals("2")){
								statusname="已登记已提交";
							}else if(statusid.equals("3")){
								statusname="已审核";
							}
							
							if(status==0){
								statusid="5";
								statusname="我的待办";
							}
							
							object.put("processstatus", statusid);//处理状态
							object.put("processstatusname", statusname);//处理状态
							
							
							if(downloadaddr!=null && !downloadaddr.trim().equals("")){
								object.put("downloadaddr", downloadaddr.trim()+e.getBillno());//厂检通知单下载地址
							}else{
								object.put("downloadaddr", "");//厂检通知单下载地址
							}
							
							jobiArray.put(i, object);
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
			 * 安装维保交接电梯登记-查看
			 * @param userid
			 * @param billno
			 * @return
			 */
			@GET
			@Path("/azwbdisplay/{userid}/{billno}")
			@Produces("application/json")
			public Response getAzwbDisplay(@PathParam("userid") String userid, @PathParam("billno") String billno){
				Session session = null;
				Loginuser user=null;
				JSONObject rejson = new JSONObject();
				JSONObject json = new JSONObject();//代表对象 {}
				JSONArray jobiArray = new JSONArray();//代表数组 []
				try{
					session = HibernateUtil.getSession();

					String sql="from ElevatorTransferCaseRegister e where e.billno='"+billno.trim()+"'";
					List eList=session.createQuery(sql).list();
					if(eList!=null&&eList.size()>0){
						
							ElevatorTransferCaseRegister e=(ElevatorTransferCaseRegister) eList.get(0);
							JSONObject object=new JSONObject();
							object.put("billno", e.getBillno());//流水号
							object.put("checknum", e.getCheckNum());//厂检次数
							object.put("elevatorno", e.getElevatorNo());//电梯编号
							object.put("salescontractno", e.getSalesContractNo());//销售合同号
							object.put("projectname", e.getProjectName());//项目名称
							object.put("projectaddress", e.getProjectAddress());//项目地址
							object.put("processstatus", e.getProcessStatus());//处理状态
							object.put("checktime", e.getCheckTime());//厂检时间
							
							if(e.getCheckDate()==null){
								object.put("checkdate2", "");//实际厂检日期(界面日期)
							}else{
								object.put("checkdate2", e.getCheckDate());//实际厂检日期(界面日期)
							}
							if(e.getCheckTime2()==null){
								object.put("checktime22", "");//实际厂检时间(界面日期)
							}else{
								object.put("checktime22", e.getCheckTime2());//实际厂检时间(界面日期)
							}
							
							object.put("elevatorparam", e.getElevatorParam());//规格型号
							object.put("salescontracttype", e.getSalesContractType());//合同性质
							object.put("floor", e.getFloor());//层
							object.put("stage", e.getStage());//站
							object.put("door", e.getDoor());//门
							object.put("high", e.getHigh());//提升高度
							object.put("weight", e.getWeight());//载重
							object.put("speed", e.getSpeed());//速度
							object.put("isxjs", e.getIsxjs());//是否迅达安装
							object.put("inscompanyname", e.getInsCompanyName());//安装公司名称
							object.put("inslinkphone", e.getInsLinkPhone());//安装公司联系电话
							object.put("department",  bd.getName(session, "Company", "comname", "comid", e.getDepartment()));//所属部门
							object.put("staffname", bd.getName(session, "Loginuser", "username", "userid", e.getStaffName()));//厂检人员名称

							String ctype =e.getContractType();
							String ctypename="";
							if(ctype.equals("XS")){
								ctypename="销售合同";
							}else if(ctype.equals("WG")){
								ctypename="维改合同";
							}
							object.put("contracttype", ctypename);//合同类型
							
							String etype=e.getElevatorType();
							String etypename="";
							if(etype.equals("T")){
								etypename="直梯";
							}else if(etype.equals("F")){
								etypename="扶梯";
							}
							object.put("elevatortype",etype);//电梯类型
							object.put("elevatortypename", etypename);//电梯类型名称
							//新增
							object.put("projectprovince", e.getProjectProvince());//项目省份
							object.put("projectmanager", e.getProjectManager());//项目经理姓名及电话
							object.put("debugpers", e.getDebugPers());//调试人员姓名及电话
							object.put("elevatoraddress", e.getElevatorAddress());//电梯位置
							object.put("receivedate", e.getReceiveDate());//接收日期
							
							object.put("stafflinkphone", e.getStaffLinkPhone());//厂检人员联系电话
							object.put("factorycheckresult", e.getFactoryCheckResult());//厂检结果
							object.put("factorycheckresult2", e.getFactoryCheckResult2());//厂检结果2
							
							String firstInstallation=e.getFirstInstallation();
							if("Y".equals(firstInstallation)){
								firstInstallation="是";
							}else if("N".equals(firstInstallation)){
								firstInstallation="否";
							}else{
								firstInstallation="";
							}
							object.put("firstinstallation", firstInstallation);//是否初次安装
							
							if(e.getR2()==null){
								object.put("r2", "");//厂检分数
							}else{
								object.put("r2", e.getR2());//厂检分数
							}
							
							/**=====================获取图片=========================*/
		     				String folder = PropertiesUtil.getProperty("ElevatorTransferCaseRegister.file.upload.folder");
		     				BASE64Encoder base=new BASE64Encoder();
		     				//客户签名
		     				if(e.getCustomerSignature()!=null && !e.getCustomerSignature().trim().equals("")){
		     					String filepath=folder+e.getCustomerSignature();
		     					byte[] imgbyte=CommonUtil.imageToByte(filepath);//将图片转换为二进制流
		     					object.put("customersignature", base.encode(imgbyte));//将二进制流加密
		     				}else{
		     					object.put("customersignature", "");
		     				}
		     				//客户照片
		     				if(e.getCustomerImage()!=null && !e.getCustomerImage().trim().equals("")){
		     					String filepath=folder+e.getCustomerImage();
		     					byte[] imgbyte=CommonUtil.imageToByte(filepath);//将图片转换为二进制流		
		     					object.put("customerimage", base.encode(imgbyte));//将二进制流加密
		     				}else{
		     					object.put("customerimage", "");
		     				}
		     				/**=====================获取图片=========================*/
								                    
							
							//问题登记项,厂检部长删除的不显示。
						    JSONArray detaillist=new JSONArray();
						    String sql1="select h,p.pullname,isnull(hi.itemgroup,'') "
						    		+ "from HandoverElevatorCheckItemRegister h,Pulldown p,HandoverElevatorCheckItem hi "
						    		+"where  p.id.pullid=h.examType "
						    		+" and h.elevatorTransferCaseRegister.billno='"+billno+"'"
 									+" and hi.id.examType=h.examType and hi.id.checkItem=h.checkItem " 
 									+" and hi.id.issueCoding=h.issueCoding "
                             		//+ " and h.isDelete='N'"
                             		+ " and p.id.typeflag='HandoverElevatorCheckItem_ExamType'" 
                             		+ " order by h.isRecheck desc,p.orderby,h.issueCoding  ";
						    
						    //System.out.println(">>>"+sql1);
						    
							List hList=session.createQuery(sql1).list();
							
							HandoverElevatorCheckItemRegister hec=null;

							if(hList!=null&&hList.size()>0){
								for(int i=0;i<hList.size();i++){
									Object[] objects=(Object[]) hList.get(i);
									
									hec=(HandoverElevatorCheckItemRegister) objects[0];
									JSONObject object2=new JSONObject();
									object2.put("jnlno", hec.getJnlno());//流水号
									object2.put("examtype", hec.getExamType());//检查类型代码
									object2.put("examtypename", (String)objects[1]);//检查类型名称
									object2.put("checkitem", hec.getCheckItem());//检查项目
									object2.put("issuecoding", hec.getIssueCoding());//问题编码
									object2.put("issuecontents", hec.getIssueContents());//问题内容
									object2.put("itemgroup", (String)objects[2]);//小组编号
									object2.put("isrecheck", hec.getIsRecheck());//是否复检
									object2.put("rem", hec.getRem());//备注
									
									String isyzg=hec.getIsyzg();
									if("Y".equals(isyzg)){
										isyzg="已整改";
									}else{
										isyzg="";
									}
									object2.put("isyzg", isyzg);//是否已整改
									
									detaillist.put(i, object2);
								}
							}
							object.put("detaillist",detaillist);
							
							//特殊要求
							JSONArray detaillist2=new JSONArray();
						    String sql2="from HandoverElevatorSpecialRegister h where h.elevatorTransferCaseRegister.billno='"+billno+"'";
							List hsList=session.createQuery(sql2).list();
							
							HandoverElevatorSpecialRegister hes=null;
							if(hsList!=null&&hsList.size()>0){
								for(int i=0;i<hsList.size();i++){
									hes=(HandoverElevatorSpecialRegister) hsList.get(i);
									JSONObject object2=new JSONObject();
									object2.put("jnlno2", hes.getJnlno());//流水号
									object2.put("scid", hes.getScId());//流水号
									object2.put("scname", bd.getName(session, "HandoverElevatorSpecialClaim", "scName", "scId", hes.getScId()));//流水号
									object2.put("isok", hes.getIsOk());//流水号
									//object2.put("appendix", h.get);//附件(多个图片)
									detaillist2.put(i, object2);
								}
								
							}
							object.put("detaillist2",detaillist2);
							jobiArray.put(0, object);
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
			 * 获取交接电梯检查项目
			 * @param userid
			 * @param elevatortype
			 * @param issuecoding
			 * @return
			 */
			@POST
			@Path("/azwbcheckitem")
			@Produces("application/json")
			public Response getCheckItemList(@FormParam("data") JSONObject data){
				Session session = null;
				Loginuser user=null;
				JSONObject rejson = new JSONObject();
				JSONObject json = new JSONObject();//代表对象 {}
				JSONArray jobiArray = new JSONArray();//代表数组 []
				try{
					String userid=(String) data.get("userid");
					String elevatortype =(String) data.get("elevatortype");
					String issuecoding =(String) data.get("issuecoding");
					String issuecodingarr =(String) data.get("issuecodingarr");
					
					session = HibernateUtil.getSession();
				    String sql="select h,p.pullname from HandoverElevatorCheckItem h,Pulldown p " +
				    		"where p.id.pullid=h.id.examType and h.enabledFlag='Y' " +
				    		" and p.id.typeflag='HandoverElevatorCheckItem_ExamType' " +
							" and h.elevatorType ='"+elevatortype.trim()+"'";
				    
				    if(issuecodingarr!=null && !issuecodingarr.trim().equals("NULL") && !issuecodingarr.trim().equals("")){
						issuecodingarr=issuecodingarr.trim().replaceAll(",", "','");
						sql+=" and h.id.issueCoding not in('"+issuecodingarr+"') ";
					}
					if(issuecoding!=null && !issuecoding.trim().equals("NULL") && !issuecoding.trim().equals("")){
						sql+=" and h.id.issueCoding like '%"+issuecoding.trim()+"%'";
					}
				    
				    sql+=" order by h.orderby";
				    List hList=session.createQuery(sql).list();
					if(hList!=null&&hList.size()>0){
						for(int i=0;i<hList.size();i++){
							Object[] objects=(Object[]) hList.get(i);
							HandoverElevatorCheckItem h=(HandoverElevatorCheckItem) objects[0];
							JSONObject object=new JSONObject();
							object.put("examtype", h.getId().getExamType());//检查类型代码
							object.put("examtypename", objects[1]);//检查类型名称
							object.put("checkitem", h.getId().getCheckItem());//检查项目
							object.put("issuecoding", h.getId().getIssueCoding());//问题编码
							object.put("issuecontents", h.getIssueContents());//问题内容
							if(h.getItemgroup()==null){
								object.put("itemgroup", "");//小组编号
							}else{
								object.put("itemgroup", h.getItemgroup());//小组编号
							}
							object.put("isrecheck", "N");//是否复检
							object.put("isyzg", "");//是否已整改
							
							jobiArray.put(i, object);
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
			 * 获取交接电梯检查项目
			 * @param userid
			 * @param elevatortype
			 * @param issuecoding
			 * @return
			 */
			@GET
			@Path("/azwbbhtype/{userid}")
			@Produces("application/json")
			public Response getBhTypeList(
			@PathParam("userid") String userid
			){
				Session session = null;
				Loginuser user=null;
				JSONObject rejson = new JSONObject();
				JSONObject json = new JSONObject();//代表对象 {}
				JSONArray jobiArray = new JSONArray();//代表数组 []
				try{
					session = HibernateUtil.getSession();
					String sql="select p from Pulldown p where p.id.typeflag='ElevatorTransferCaseRegister_BhType' " +
							"and p.enabledflag='Y' order by p.orderby";

				    List hList=session.createQuery(sql).list();
					if(hList!=null&&hList.size()>0){
						for(int i=0;i<hList.size();i++){
							Pulldown h=(Pulldown) hList.get(i);
							JSONObject object=new JSONObject();
							object.put("bhtype", h.getId().getPullid());//驳回分类代码
							object.put("bhtypename", h.getPullname());//驳回分类名称
							jobiArray.put(i, object);
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
			 * 点击接收按钮
			 * @param data
			 * @return
			 * @throws JSONException 
			 */
			@GET
			@Path("/azwbjssave/{data}")
			@Produces("application/json")
			public Response saveJSAzwb (@PathParam("data") JSONObject data) throws JSONException{
				Session session = null;
				Transaction tx = null;
				Loginuser user=null;
				JSONObject rejson = new JSONObject();
				JSONObject json = new JSONObject();//代表对象 {}
				JSONArray jobiArray = new JSONArray();//代表数组 []
				try{
					session = HibernateUtil.getSession();
					String userid=(String) data.get("userid");
					String billno=(String) data.get("billno");
					String receivedate=(String) data.get("receivedate");
					tx = session.beginTransaction();
					ElevatorTransferCaseRegister e=(ElevatorTransferCaseRegister) session.get(ElevatorTransferCaseRegister.class, billno);
					e.setReceiveDate(receivedate);
					e.setProcessStatus("4");//已接收
					
					session.update(e);
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
			 * 点击驳回按钮
			 * @param data
			 * @return
			 * @throws JSONException 
			 */
			@POST
			@Path("/azwbbhsave")
			@Produces("application/json")
			public Response saveBHAzwb (@FormParam("data") JSONObject data) throws JSONException{
				Session session = null;
				Transaction tx = null;
				Loginuser user=null;
				JSONObject rejson = new JSONObject();
				JSONObject json = new JSONObject();//代表对象 {}
				JSONArray jobiArray = new JSONArray();//代表数组 []
				try{
					session = HibernateUtil.getSession();
					tx = session.beginTransaction();
					String userid=(String) data.get("userid");
					String billno=(String) data.get("billno");
					String bhrem=(String) data.get("bhrem");//驳回原因
					String bhdate=(String) data.get("bhdate");//驳回日期
					String bhtype=(String) data.get("bhtype");//驳回分类代码
					String bhtypename=(String) data.get("bhtypename");//驳回分类名称
					
					System.out.println("厂检==>>驳回保存");

					//List userList=session.createQuery("from Loginuser where userid ='"+userid+"'").list();
					//if(userList!=null&&userList.size()>0){
					//	user=(Loginuser) userList.get(0);
					//}
					
					ElevatorTransferCaseRegister e=(ElevatorTransferCaseRegister) session.get(ElevatorTransferCaseRegister.class, billno);
					e.setBhDate(bhdate);
					e.setBhRem(bhrem);
					e.setBhType(bhtype);
					e.setSubmitType("R");
    				e.setProcessStatus("0");
    				e.setReceiveDate("");
                    session.update(e);
                    
                    //安装维保交接电梯情况驳回记录表
                    ElevatorTransferCaseBhType etfcbh=new ElevatorTransferCaseBhType();
    				etfcbh.setBillno(billno);
    				etfcbh.setBhDate(bhdate);
    				etfcbh.setBhRem(bhrem);
					etfcbh.setBhType(bhtype);
					session.save(etfcbh);
                    
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
			 * 点击转派按钮
			 * @param data
			 * @return
			 * @throws JSONException 
			 */
			@GET
			@Path("/azwbzpsave/{data}")
			@Produces("application/json")
			public Response saveZPAzwb (@PathParam("data") JSONObject data) throws JSONException{
				Session session = null;
				Transaction tx = null;
				Loginuser user=null;
				JSONObject rejson = new JSONObject();
				JSONObject json = new JSONObject();//代表对象 {}
				JSONArray jobiArray = new JSONArray();//代表数组 []
				try{
					session = HibernateUtil.getSession();
					tx = session.beginTransaction();
					String userid=(String) data.get("userid");
					
					String billno=(String) data.get("billno");
					String transferid=(String) data.get("transferid");
					//String transferdate=(String) data.get("transferdate");
					String staffid=(String) data.get("staffid");
					String stafflinkphone=(String) data.get("stafflinkphone");

					//List userList=session.createQuery("from Loginuser where userid ='"+userid+"'").list();
					//if(userList!=null&&userList.size()>0){
					//	user=(Loginuser) userList.get(0);
					//}
					
					ElevatorTransferCaseRegister e=(ElevatorTransferCaseRegister) session.get(ElevatorTransferCaseRegister.class, billno);
					e.setTransferId(transferid);//转派人
					e.setTransferDate(CommonUtil.getNowTime());	//转派日期			
                    e.setStaffName(staffid);
                    e.setStaffLinkPhone(stafflinkphone);
                    e.setSubmitType("Z");
                    e.setProcessStatus("0");
                    e.setReceiveDate("");
                    
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
			 * 获取转派的厂检员
			 * @param userid
			 * @return
			 */
			@GET
			@Path("/azwbchecku/{userid}")
			@Produces("application/json")
			public Response getCheckUserList(
			@PathParam("userid") String userid 
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
					String sql="from Loginuser where roleid='A51'";
					List list=session.createQuery(sql).list();
					if(list!=null&&list.size()>0){
						for(int i=0;i<list.size();i++){
							JSONObject jsonObject=new JSONObject();
							Loginuser l=(Loginuser) list.get(i);
							jsonObject.put("stuserid", l.getUserid());//厂检人员代码
							jsonObject.put("stusername", l.getUsername());//厂检人员名称
							jsonObject.put("stphone", l.getPhone());//厂检人员联系电话
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
			
			
			/**
			 * 登记
			 * @param data
			 * @return
			 * @throws JSONException 
			 */
			@POST
			@Path("/azwbadd")
			@Produces("application/json")
			public Response saveAzwb (@FormParam("data") JSONObject data) throws JSONException{
				Session session = null;
				Transaction tx = null;
				JbpmExtBridge jbpmExtBridge=null;

				JSONObject rejson = new JSONObject();
				JSONObject json = new JSONObject();//代表对象 {}
				JSONArray jobiArray = new JSONArray();//代表数组 []
				String processName="";
				try{
					session = HibernateUtil.getSession();
					tx = session.beginTransaction();
					 
					String userid=(String) data.get("userid");//登录用户代码
					String billno=(String) data.get("billno");//流水号
					//String factorycheckresult=(String) data.get("factorycheckresult");//厂检结果
					String processstatus=(String) data.get("processstatus");//处理状态
					String submittype=(String)data.get("submittype");//提交标志
					String elevatoraddress=(String)data.get("elevatoraddress");//电梯位置
					
					JSONArray detaillist=(JSONArray) data.get("detaillist");//厂检检查项
					JSONArray detaillist2=(JSONArray) data.get("detaillist2");//特殊要求项
					
					String checktime=CommonUtil.getNowTime();//厂检时间
					String checkdate=(String)data.get("checkdate");//实际厂检日期(界面日期)
					String checktime2=(String)data.get("checktime2");//实际厂检时间(界面日期)

					
					ElevatorTransferCaseRegister etcr =(ElevatorTransferCaseRegister) session.get(ElevatorTransferCaseRegister.class, billno);
					etcr.setCheckTime(checktime);
					etcr.setCheckDate(checkdate);
					etcr.setCheckTime2(checktime2);
					etcr.setElevatorAddress(elevatoraddress);
					//etcr.setSubmitType(submittype);
					etcr.setProcessStatus(processstatus);
					
					String firstInstallation=etcr.getFirstInstallation();//是否初次安装
					
					//计算是否合格
					String factorycheckresult="合格";
					String cjfs="0";
					String factorycheckresult2="合格";
					System.out.println(">>>>开始计算厂检项是否合格===开始");
					if(detaillist!=null && detaillist.length()>0){
						HashMap hmap=this.isQualified(etcr.getCheckNum(),etcr.getElevatorType(),detaillist,firstInstallation);
						factorycheckresult=(String)hmap.get("result");
						cjfs=(String)hmap.get("cjfs");
						factorycheckresult2=(String)hmap.get("factorycheckresult2");
					}
					System.out.println("factorycheckresult="+factorycheckresult+"; factorycheckresult2="+factorycheckresult2+"; cjfs="+cjfs);
					System.out.println(">>>>开始计算厂检项是否合格===结束");
					
					etcr.setFactoryCheckResult(factorycheckresult);//厂检结果
					etcr.setR2(cjfs);//厂检分数
					etcr.setFactoryCheckResult2(factorycheckresult2);//厂检结果2
					
					Integer status=etcr.getStatus();
					if(status!=null && status==0){
						
						//获取流程信息 审批不通过的
						long taskid=0;
						String taskname="";
						String sqlc="select ID_,NAME_ from JBPM_TASKINSTANCE where TOKEN_="+etcr.getTokenId()+" and isnull(END_,'')=''";
						ResultSet rs=session.connection().prepareStatement(sqlc).executeQuery();
						if(rs.next()){
							taskid=rs.getLong("ID_");
							taskname=rs.getString("NAME_");
						}
						
						//流程处理
						String processDefineID = Grcnamelist1.getProcessDefineID("elevatortransfercaseregister", etcr.getStaffName());
						/*=============== 流程审批启动开始 =================*/
						jbpmExtBridge = new JbpmExtBridge();
						HashMap paraMap = new HashMap();
						ProcessBean pd = jbpmExtBridge.getProcessBeanUseTI(taskid);
						
						pd.addAppointActors("");// 将动态添加的审核人清除。
						if(factorycheckresult.equals("合格")){
							pd.setSelpath("Y");
							processName="关闭审核流程";
							Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "关闭审核流程", "%");// 添加审核人
						}else if(factorycheckresult.equals("不合格")){
							pd.setSelpath("N");
							processName="厂检部长审核";
							Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "厂检部长审核", "%");// 添加审核人
						}
						
						pd = jbpmExtBridge.goToNext(taskid, "提交", userid, paraMap);// 审核
						/**==================== 流程结束 =======================**/
				
						etcr.setStatus(pd.getStatus());
						etcr.setTokenId(pd.getToken());
						etcr.setProcessName(pd.getNodename());
						etcr.setIsDeductions("");
						etcr.setDeductMoney(null);
						etcr.setProcessResult("");
						
						ElevatorTransferCaseProcess process = new ElevatorTransferCaseProcess();
						process.setElevatorTransferCaseRegister(etcr);
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
						//登记保存提交
						if("Y".equals(submittype)){
							String processDefineID = Grcnamelist1.getProcessDefineID("elevatortransfercaseregister", etcr.getStaffName());
							if(processDefineID == null || processDefineID.equals("")){
								System.out.println("手机端登记>>> 未配置审批流程信息，不能启动！");
								throw new Exception();
							}
							/**=============== 启动新流程实例开始 ===================**/
							HashMap paraMap = new HashMap();
							jbpmExtBridge=new JbpmExtBridge();
							ProcessBean pd = jbpmExtBridge.getPb();
							if(factorycheckresult.equals("合格")){
								pd.setSelpath("Y");
								processName="关闭审核流程";
								Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "关闭审核流程", "%");// 添加审核人
							}else if(factorycheckresult.equals("不合格")){
								pd.setSelpath("N");
								processName="厂检部长审核";
								Grcnamelist1.setAllProcessAuditoper(pd, processDefineID, "厂检部长审核", "%");// 添加审核人
							}
							
							pd=jbpmExtBridge.startProcess(WorkFlowConfig.getProcessDefine(processDefineID),userid,userid,billno,"",paraMap);
							/**==================== 流程结束 =======================**/
							etcr.setStatus(pd.getStatus());
							etcr.setTokenId(pd.getToken());
							etcr.setProcessName(pd.getNodename());
							
						}else{
							//String hql="update ElevatorTransferCaseRegister set checkTime='"+checktime+"',factoryCheckResult='"+factorycheckresult+"'" +
							//		",processStatus='"+processstatus+"' where billno='"+billno+"'";
							//session.createQuery(hql).executeUpdate();
						}
					}
					/**=======================保存图片=============================*/
					String folder2 = PropertiesUtil.getProperty("ElevatorTransferCaseRegister.file.upload.folder");
					String filepath2=CommonUtil.getNowTime("yyyy-MM-dd")+"/";
					//保存签名
					String customersignature=(String)data.get("customersignature");
					if(customersignature!=null && !customersignature.trim().equals("")){
						//customersignature=customersignature.replaceAll("data:image/jpeg;base64,", "");//去掉前缀
						String[] signatures=customersignature.split(",");
						if(signatures!=null && signatures.length>1){
							byte[] image=new BASE64Decoder().decodeBuffer(signatures[1]);
							String newfilename=etcr.getBillno()+"_0.jpg";
							//保存图片
							File f=new File(folder2+filepath2);
							f.mkdirs();
							FileOutputStream fos=new FileOutputStream(folder2+filepath2+newfilename);
							fos.write(image);
							fos.flush();
							fos.close();
							//保存图片信息到数据库
							etcr.setCustomerSignature(filepath2+newfilename);
						}
					}
					//保存拍照
					String customerimage=(String)data.get("customerimage");
					if(customerimage!=null && !customerimage.trim().equals("")){
						//customerimage=customerimage.replaceAll("data:image/jpeg;base64,", "");//去掉前缀
						String[] cimages=customerimage.split(",");
						if(cimages!=null && cimages.length>1){
							byte[] image=new BASE64Decoder().decodeBuffer(cimages[1]);
							String newfilename=etcr.getBillno()+"_1.jpg";
							//保存图片
							File f=new File(folder2+filepath2);
							f.mkdirs();
							FileOutputStream fos=new FileOutputStream(folder2+filepath2+newfilename);
							fos.write(image);
							fos.flush();
							fos.close();
							//保存图片信息到数据库
							etcr.setCustomerImage(filepath2+newfilename);
						}
					}
					/**=======================保存图片=============================*/
					
					session.save(etcr);

					/**===================厂检检查项 开始===================*/
					//先删除界面不存在的检查项
					String hecirJnlnos="";
					if(detaillist!=null&&detaillist.length()>0){
						for(int i=0;i<detaillist.length();i++){
							JSONObject object=(JSONObject) detaillist.get(i);
							String jnlno=(String) object.get("jnlno");//流水号
							if(jnlno!=null&&!"".equals(jnlno)){
								hecirJnlnos+=i==0 ? jnlno : "','"+jnlno;
						    }
						}
					}
					
					//先删除数据库数据
					List filelist=null;
					if(!hecirJnlnos.equals("")){
						String filesql="from HandoverElevatorCheckFileinfo h where h.handoverElevatorCheckItemRegister.jnlno in(select jnlno from HandoverElevatorCheckItemRegister h where h.elevatorTransferCaseRegister.billno='"+billno+"' and h.jnlno not in('"+hecirJnlnos+"'))";
						filelist=session.createQuery(filesql).list();
						
						session.createQuery("delete HandoverElevatorCheckFileinfo h where h.handoverElevatorCheckItemRegister.jnlno in(select jnlno from HandoverElevatorCheckItemRegister h where h.elevatorTransferCaseRegister.billno='"+billno+"' and h.jnlno not in('"+hecirJnlnos+"'))").executeUpdate();
						session.createQuery("delete HandoverElevatorCheckItemRegister h where h.elevatorTransferCaseRegister.billno='"+billno+"' and h.jnlno not in('"+hecirJnlnos+"')").executeUpdate();
					}else{
						String filesql="from HandoverElevatorCheckFileinfo h where h.handoverElevatorCheckItemRegister.jnlno in(select jnlno from HandoverElevatorCheckItemRegister h where h.elevatorTransferCaseRegister.billno='"+billno+"')";
						filelist=session.createQuery(filesql).list();

						session.createQuery("delete HandoverElevatorCheckFileinfo h where h.handoverElevatorCheckItemRegister.jnlno in(select jnlno from HandoverElevatorCheckItemRegister h where h.elevatorTransferCaseRegister.billno='"+billno+"')").executeUpdate();
						session.createQuery("delete HandoverElevatorCheckItemRegister h where h.elevatorTransferCaseRegister.billno='"+billno+"'").executeUpdate();
					}
					//再删除硬盘的图片
					if(filelist!=null && filelist.size()>0){
						HandoverElevatorCheckFileinfo hecf=null;
						String folder ="HandoverElevatorCheckItemRegister.file.upload.folder";
						folder = PropertiesUtil.getProperty(folder).trim();
						for(int i=0;i<filelist.size();i++){
							hecf=(HandoverElevatorCheckFileinfo) filelist.get(i);
							
							HandleFile hf = new HandleFileImpA();
							String localpath=folder+"/"+hecf.getFilePath()+hecf.getNewFileName();
							hf.delFile(localpath);
						}
					}
					
					//在保存厂检检查项
					if(detaillist!=null && detaillist.length()>0){
						HandoverElevatorCheckItemRegister hecir=null;
						for(int i=0;i<detaillist.length();i++){
							JSONObject object=(JSONObject) detaillist.get(i);
							String jnlno=(String) object.get("jnlno");//流水号
							String examtype=(String) object.get("examtype");//检查类型代码
							String checkitem=(String) object.get("checkitem");//检查项目
							String issuecoding=(String) object.get("issuecoding");//问题编码
							String issuecontents=(String) object.get("issuecontents");//问题编码
							String rem=(String) object.get("rem");//备注
							if(rem==null){
								rem="";
							}
							
							if(jnlno!=null && !jnlno.equals("")){
		                		hecir = (HandoverElevatorCheckItemRegister) session.get(HandoverElevatorCheckItemRegister.class, jnlno);	
		                    	if(hecir!=null){
			                		hecir.setRem(rem);
			                    	session.update(hecir);
		                    	}
			                }else{
				                hecir = new HandoverElevatorCheckItemRegister();
								String jnlnokk="";
								try {
									String[] billno1 = CommonUtil.getBillno(CommonUtil.getToday().substring(2,4),"HandoverElevatorCheckItemRegister", 1);	
									jnlnokk=billno1[0];
									}catch (Exception e) {
										e.printStackTrace();
									}
								hecir.setJnlno(jnlnokk);
								hecir.setIsDelete("N");
								hecir.setExamType(examtype);
								hecir.setIssueCoding(issuecoding);
								hecir.setCheckItem(checkitem);
								hecir.setIssueContents(issuecontents);
								hecir.setIsRecheck("N");
								hecir.setRem(rem);
								hecir.setElevatorTransferCaseRegister(etcr);
								session.save(hecir);
								//session.flush();
			                }
						}
					}
					/**===================厂检检查项 结束===================*/
					
					//特殊要求项
					HandoverElevatorSpecialRegister hesr=null;
					if(detaillist2!=null&&detaillist2.length()>0){
						for(int i=0;i<detaillist2.length();i++){
							JSONObject object=(JSONObject) detaillist2.get(i);
							String jnlno2 = (String) object.get("jnlno2");	
						    //String scid=(String) object.get("scid");//特殊要求代码
						    //String scname=(String) object.get("scname");//特殊要求
						    String isok=(String) object.get("isok");//是否选中
						    
		            		 hesr=(HandoverElevatorSpecialRegister) session.get(HandoverElevatorSpecialRegister.class,jnlno2) ;
		            		 hesr.setIsOk(isok);
		            	     session.update(hesr);
		            	     //session.flush();
						}
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
					json.put("code", "400");
		  			json.put("info", "NOT OK");
		            rejson.put("status", json);
		      		rejson.put("data", jobiArray);
					ex.printStackTrace();
					
					if (jbpmExtBridge != null) {
						jbpmExtBridge.setRollBack();
					}
				} finally {
		            try {
		            	if(session!=null){
		            		session.close();
		            	}
		            	if(jbpmExtBridge!=null){
							jbpmExtBridge.close();
						}
		            } catch (HibernateException hex) {
		                DebugUtil.print(hex, "HibernateUtil：Hibernate Session 关闭出错！");
		            }
		        }
				
				return Response.status(200).entity(rejson.toString()).build();
			}
			
			//检查项计算是否合格
			/**
			扶梯计算公式：
				======扶梯初检======
				1，有任一重大不合格项则为不合格。
				2，没有重大不合格项，但一般不合格项 大于八项的，也为不合格。
				3，没有重大不合格项，且一般不合格项小于等于八项的为初检合格。
				======扶梯复检（包括2次、3次、4次。。。）去掉漏检项======
				1，没有重大不合格项，且一般不合格项小于等于三项的为复检合格。

			直梯计算公式： 根据小组编码进行不合格数量统计，相同的小组编码就只表示一个不合格项。
				======直梯初检======
				1，有任一重大不合格项则为不合格。
				2，没有重大不合格项，但 一般不合格项 大于18项的，也为不合格。
				3，没有重大不合格项，且一般不合格项小于等于18项的为初检合格。
				======直梯复检（包括2次、3次、4次。。。）去掉漏检项======
				1，没有重大不合格项，且一般不合格项小于等于8项的为复检合格。
				
			厂检分数===计算方法： 根据计算合格不合格的重大项和一般项个数 （满分一百分）-扣除分数
				直梯：一般项一次扣2分，重大项一次扣10分。
				扶梯：一般项一次扣5分，重大项一次扣20分
			 * @throws JSONException 
			*/
			private HashMap isQualified(int checkNum,String elevatorType,JSONArray detaillist,String firstInstallation) throws JSONException{
				String factorycheckresult="合格";
				int zNum=0;
				int yNum=0;
				double cjfs=0;
				String strarr="";
				
				int firstnum=0;

				if(checkNum>1){
					//复检
					for(int i=0;i<detaillist.length();i++){
						JSONObject object=(JSONObject) detaillist.get(i);
						String examtype=(String) object.get("examtype");//检查类型代码
						String itemgroup=(String) object.get("itemgroup");//小组编号
						String isrecheck=(String) object.get("isrecheck");//是否复检
						String isyzg=(String) object.get("isyzg");//是否已整改
						
						System.out.println("复检>>>firstInstallation="+firstInstallation+"; isyzg="+isyzg);
						
						//排除漏检项
						if(isrecheck!=null && isrecheck.trim().equals("Y")){
							//重大不合格项
							if(examtype.equals("ZD")){
								if("Y".equals(firstInstallation) && ("Y".equals(isyzg) || "已整改".equals(isyzg))){
									firstnum++;
								}else{
									zNum+=1;
								}
							}
							//一般不合格项
							if(examtype.equals("YB")){
								if(elevatorType.equals("T")){

									if(!itemgroup.trim().equals("") && strarr.indexOf(itemgroup+",")>-1){
										
									}else{
										if("Y".equals(firstInstallation) && ("Y".equals(isyzg) || "已整改".equals(isyzg))){
											firstnum++;
										}else{
											strarr+=itemgroup+",";
											yNum+=1;
										}
									}
								}else{
									//扶梯
									if("Y".equals(firstInstallation) && ("Y".equals(isyzg) || "已整改".equals(isyzg))){
										firstnum++;
									}else{
										yNum+=1;
									}
								}
							}
							
						}
					}
				}else{
					//初检
					for(int i=0;i<detaillist.length();i++){
						JSONObject object=(JSONObject) detaillist.get(i);
						String examtype=(String) object.get("examtype");//检查类型代码
						String itemgroup=(String) object.get("itemgroup");//小组编号
						String isyzg=(String) object.get("isyzg");//是否已整改
						
						System.out.println("初检>>>firstInstallation="+firstInstallation+"; isyzg="+isyzg);
						
						//重大不合格项
						if(examtype.equals("ZD")){
							if("Y".equals(firstInstallation) && ("Y".equals(isyzg) || "已整改".equals(isyzg))){
								firstnum++;
							}else{
								zNum+=1;
							}
						}
						//一般不合格项
						if(examtype.equals("YB")){
							if(elevatorType.equals("T")){

								if(!itemgroup.trim().equals("") && strarr.indexOf(itemgroup+",")>-1){
									
								}else{
									if("Y".equals(firstInstallation) && ("Y".equals(isyzg) || "已整改".equals(isyzg))){
										firstnum++;
									}else{
										strarr+=itemgroup+",";
										yNum+=1;
									}
								}
							}else{
								//扶梯
								if("Y".equals(firstInstallation) && ("Y".equals(isyzg) || "已整改".equals(isyzg))){
									firstnum++;
								}else{
									yNum+=1;
								}
							}
						}
						
					}
				}
				
				if(checkNum>1){
					//复检
					if(elevatorType.equals("T")){
						if(zNum>=1 || yNum>8){
							factorycheckresult="不合格";
						}else{
							factorycheckresult="合格";
						}
						//直梯：重大项一次扣10分,一般项一次扣2分。
						cjfs=(zNum*10)+(yNum*2);
					}
					if(elevatorType.equals("F")){
						if(zNum>=1 || yNum>3){
							factorycheckresult="不合格";
						}else{
							factorycheckresult="合格";
						}
						//扶梯：重大项一次扣20分,一般项一次扣5分。
						cjfs=(zNum*20)+(yNum*5);
					}
				}else{
					//初检
					if(elevatorType.equals("T")){
						if(zNum>=1 || yNum>18){
							factorycheckresult="不合格";
						}else{
							factorycheckresult="合格";
						}
						//直梯：重大项一次扣10分,一般项一次扣2分。
						cjfs=(zNum*10)+(yNum*2);
					}
					if(elevatorType.equals("F")){
						if(zNum>=1 || yNum>8){
							factorycheckresult="不合格";
						}else{
							factorycheckresult="合格";
						}
						//扶梯：重大项一次扣20分,一般项一次扣5分。
						cjfs=(zNum*20)+(yNum*5);
					}
				}
				
				String factorycheckresult2=factorycheckresult;
				if(firstnum>0 && factorycheckresult=="合格"){
					factorycheckresult2="厂检员监改合格";
				}
				
				HashMap remap=new HashMap();
				remap.put("result", factorycheckresult);
				remap.put("cjfs", String.valueOf(100-cjfs));
				remap.put("factorycheckresult2", factorycheckresult2);
				
				return remap;
				
			}
			

	  /**
		 * 获取检查项的图片附件
		 * @param userid
		 * @param elevatortype
		 * @param issuecoding
		 * @return
		 */
		@GET
		@Path("/azwbcheckitemimgae/{jnlno}")
		@Produces("application/json")
		public Response getImageList(@PathParam("jnlno") String jnlno){
			Session session = null;
			//Loginuser user=null;
			JSONObject rejson = new JSONObject();
			JSONObject json = new JSONObject();//代表对象 {}
			JSONArray imgarr=new JSONArray();//代表数组 []
			
			HandoverElevatorCheckFileinfo hefile=null;
			//保存文件路径
			String folder="HandoverElevatorCheckItemRegister.file.upload.folder";
			folder = PropertiesUtil.getProperty(folder).trim();
			
			BASE64Encoder base=new BASE64Encoder();
			try{
				session = HibernateUtil.getSession();

				String hql="from HandoverElevatorCheckFileinfo h where h.handoverElevatorCheckItemRegister.jnlno ='"+jnlno.trim()+"' ";
				List fileList=session.createQuery(hql).list();
				
				if(fileList!=null&&fileList.size()>0){
					for(int j=0;j<fileList.size();j++){
						hefile=(HandoverElevatorCheckFileinfo)fileList.get(j);
						String filepath=folder+"/"+hefile.getFilePath()+hefile.getNewFileName();
						//将图片转换为二进制流
						byte[] imgbyte=CommonUtil.imageToByte(filepath);
						
						JSONObject objf=new JSONObject();
						objf.put("filesid", hefile.getFileSid());
						objf.put("oldfilename", hefile.getOldFileName());
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
			try{
				session = HibernateUtil.getSession();
				tx = session.beginTransaction();

				String userid=(String) data.get("userid");//登录用户代码
				String billno=(String) data.get("billno");//流水号
				String jnlno=(String) data.get("jnlno");//流水号
				String examtype=(String) data.get("examtype");//检查类型代码
				String checkitem=(String) data.get("checkitem");//检查项目
				String issuecoding=(String) data.get("issuecoding");//问题编码
				String issuecontents=(String) data.get("issuecontents");//问题编码
				String rem=(String) data.get("rem");//备注
				String isyzg=(String) data.get("isyzg");//是否已整改
				
				JSONArray imagelist=(JSONArray) data.get("imagelist");//上传图片
				
				ElevatorTransferCaseRegister etcr =(ElevatorTransferCaseRegister) session.get(ElevatorTransferCaseRegister.class, billno);

				//保存安装维保交接电梯情况登记项目
				HandoverElevatorCheckItemRegister hecir=null;
				if(jnlno!=null && !jnlno.equals("")){
					//流水号存在就修改
            		hecir = (HandoverElevatorCheckItemRegister) session.get(HandoverElevatorCheckItemRegister.class, jnlno);	
                	hecir.setRem(rem);
                	hecir.setIsyzg(isyzg);
                	session.update(hecir);
                }else{
                	//不存在就新建
	                hecir = new HandoverElevatorCheckItemRegister();
					String[] billno1 = CommonUtil.getBillno(CommonUtil.getToday().substring(2,4),"HandoverElevatorCheckItemRegister", 1);	
					jnlno=billno1[0];

					hecir.setJnlno(jnlno);
					hecir.setIsDelete("N");
					hecir.setExamType(examtype);
					hecir.setIssueCoding(issuecoding);
					hecir.setCheckItem(checkitem);
					hecir.setIssueContents(issuecontents);
					hecir.setIsRecheck("N");
					hecir.setRem(rem);
					hecir.setIsyzg(isyzg);
					hecir.setElevatorTransferCaseRegister(etcr);
					session.save(hecir);
					session.flush();
                }
				
				//保存图片
				HandoverElevatorCheckFileinfo fileInfo=null;
				String folder ="HandoverElevatorCheckItemRegister.file.upload.folder";
				folder = PropertiesUtil.getProperty(folder).trim();
				
				String curdate=DateUtil.getDateTime("yyyyMMddHHmmss");
				for(int i=0;i<imagelist.length();i++){
					JSONObject object=(JSONObject) imagelist.get(i);
					String imgpic=(String) object.get("imgpic");//流水号
					
					byte[] image=new BASE64Decoder().decodeBuffer(imgpic);
					String newfilename=userid+"_"+curdate+"_"+i+".jpg";
					String filepath=CommonUtil.getNowTime("yyyy-MM-dd")+"/";

					//保存图片
					File f=new File(folder+"/"+filepath);
					f.mkdirs();
					FileOutputStream fos=new FileOutputStream(folder+"/"+filepath+newfilename);
					fos.write(image);
					fos.flush();
					fos.close();
					
					//保存图片信息到数据库
					fileInfo=new HandoverElevatorCheckFileinfo();
					fileInfo.setHandoverElevatorCheckItemRegister(hecir);
					fileInfo.setOldFileName(newfilename);
					fileInfo.setNewFileName(newfilename);
					fileInfo.setFileSize(Double.valueOf(0));
					fileInfo.setFilePath(filepath);
					fileInfo.setFileFormat("-");
					fileInfo.setUploadDate(CommonUtil.getNowTime());
					fileInfo.setUploader(userid);
					fileInfo.setExt1("N");
					session.save(fileInfo);
					
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
			
			HandoverElevatorCheckFileinfo hecf=null;
			try{
				session = HibernateUtil.getSession();
				tx = session.beginTransaction();
				
				String folder ="HandoverElevatorCheckItemRegister.file.upload.folder";
				folder = PropertiesUtil.getProperty(folder).trim();
				if(filesid!=null && filesid.length()>0){
					
					hecf=(HandoverElevatorCheckFileinfo) session.get(HandoverElevatorCheckFileinfo.class,Integer.valueOf(filesid));
					String filename=hecf.getNewFileName();
					String filepath=hecf.getFilePath();
					session.delete(hecf);
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

		  
}



