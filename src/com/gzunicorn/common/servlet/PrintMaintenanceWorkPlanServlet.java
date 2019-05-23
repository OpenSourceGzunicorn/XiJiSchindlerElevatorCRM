package com.gzunicorn.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.pdfprint.BarCodePrint;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.hibernate.maintenanceworkplanmanager.maintenanceworkplandetail.MaintenanceWorkPlanDetail;
import com.gzunicorn.hibernate.sysmanager.Loginuser;

public class PrintMaintenanceWorkPlanServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PrintMaintenanceWorkPlanServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//System.out.println(">>>>doGet");
		this.toPrintMaintenanceWorkPlanRecord(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//System.out.println(">>>>doPost");
		this.toPrintMaintenanceWorkPlanRecord(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

	/**
	 * 下载电梯保养记录单
	 */
	private void toPrintMaintenanceWorkPlanRecord(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		BaseDataImpl bd = new BaseDataImpl();
		
		Session hs = null;	
		Connection conn = null;
		String id = request.getParameter("id");
		System.out.println(">>>>开始打印电梯保养记录单 "+id);
		
		List etcpList=new ArrayList();
		List hecirList=new ArrayList();
		MaintenanceWorkPlanDetail mwpd=null;
		HashMap map=new HashMap();
		
		//List etcpList=new ArrayList();
		if (id != null && !id.equals("")) {			
			try {
				hs = HibernateUtil.getSession();
				DataOperation op = new DataOperation();
				conn = (Connection) hs.connection();
				op.setCon(conn);
				String sql="select mwpd,mcm.maintContractNo,mcd.projectName,si.storagename,lu.username,lu.phone,mcm.billNo," +
						"mcd.elevatorNo,mcd.maintAddress,mwpm.billno,mcd.elevatorType "
						+"from  MaintenanceWorkPlanDetail mwpd,MaintenanceWorkPlanMaster mwpm," 
						+"MaintContractDetail mcd,MaintContractMaster mcm,Loginuser lu,Storageid si "
						+ "where mcd.billNo=mcm.billNo "
						+ " and si.storageid=mcd.assignedMainStation "
						+ " and lu.userid=mwpd.maintPersonnel "
						+ " and mcd.rowid=mwpm.rowid "
						+ " and mwpd.maintenanceWorkPlanMaster.billno=mwpm.billno "
						+ " and mwpd.numno ="+id;
				Query query = hs.createQuery(sql);
				List list = query.list();
			       if(list!=null && list.size()>0)
			       {
			    	   Object[] objects=(Object[]) list.get(0);
			    	   mwpd=(MaintenanceWorkPlanDetail) objects[0];
			    	   String CompanyID=bd.getName("MaintContractMaster", "companyId", "maintContractNo",(String)objects[1]);
			    	   mwpd.setR3(bd.getName(hs, "Customer", "companyName", "companyId",CompanyID));
			    	   mwpd.setR2(bd.getName(hs, "ElevatorCoordinateLocation","rem", "elevatorNo",(String)objects[7]));
			    	  // mwpd.setDjOperId2(bd.getName(hs, "Loginuser", "username", "userid", mwpd.getDjOperId2()));
			    	   map.put("maintContractNo", (String)objects[1]);
			    	   map.put("projectName", (String)objects[2]);
			    	   map.put("storagename", (String)objects[3]);
			    	   map.put("username", (String)objects[4]);
			    	   map.put("phone", (String)objects[5]);
			    	   map.put("billno", (String)objects[6]);
			    	   map.put("elevatorNo", (String)objects[7]);
			    	   map.put("maintAddress", (String)objects[8]);
			    	   map.put("elevatorType", (String)objects[10]);
			    	   
			    	   if(mwpd.getMaintType().equals("halfMonth")){
			    		   mwpd.setMaintType("半月保养");
						}
						if(mwpd.getMaintType().equals("quarter")){
							mwpd.setMaintType("季度保养");
						}
						if(mwpd.getMaintType().equals("halfYear")){
							mwpd.setMaintType("半年保养");
						}
						if(mwpd.getMaintType().equals("yearDegree")){
							mwpd.setMaintType("年度保养");
						}
			    	   String sMaintEndTime=this.togetAuditCircu(hs,mwpd,(String)objects[9]);
			    	   map.put("sMaintEndTime", sMaintEndTime);//上次保养时间
			    	   /**
			    	   String r5=mwpd.getR5();//保养参与人员
			          	String r5name="";
			          	 if(r5!=null && !r5.trim().equals("")){
			          		 String sqls="select a from Loginuser a where a.userid in('"+r5.replaceAll(",", "','")+"')";
			          		 List loginlist=hs.createQuery(sqls).list();
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
			          	map.put("r5name", mwpd.getR5());//保养参与人员
			          	map.put("byrem", mwpd.getR4());//上次保养时间
			          	
			    	   etcpList.add(map);
			    	   //保养项目明细
			    	   String sql1 ="select * from  MaintainProjectInfoWork mpiw where mpiw.singleno='"+mwpd.getSingleno()+"' order by mpiw.orderby,mpiw.maintItem";			
			    	   hecirList =op.queryToList(sql1);
					
			    	   //对barCodeList、noticeList操作
			    	   BarCodePrint dy = new BarCodePrint();
			    	   List barCodeList = new ArrayList();
			    	   barCodeList.add(mwpd);
			    	   barCodeList.add(hecirList);
			    	   barCodeList.add(etcpList);
			    	   dy.toPrintTwoRecord7(request,response, barCodeList,id);
			    	   //register hecirList
				
			       }
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					hs.close();
				} catch (HibernateException hex) {
					DebugUtil.print(hex, "HibernateUtil Hibernate Session ");
				}
			}
			
		}
		
	}
	//获取上一次保养时间
	private String togetAuditCircu(Session hs, MaintenanceWorkPlanDetail mwpd,String str) {

 	    //获取上一次保养时间
		String sMaintEndTime="";
		String sql="select mwpd.maintEndTime from MaintenanceWorkPlanDetail mwpd" +
				" where mwpd.numno=(select numno-1 from MaintenanceWorkPlanDetail " +
				"where numno='"+mwpd.getNumno()+"' and billno='"+str+"' " +
				"and numno !=(select MIN(numno)from MaintenanceWorkPlanDetail where billno='"+str+"'))";
		List list=hs.createSQLQuery(sql).list();
		if(list!=null&&list.size()>0){
			sMaintEndTime=(String) list.get(0);
	    }
	    return sMaintEndTime;
	}
	
}
