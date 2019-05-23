package com.gzunicorn.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.gzunicorn.common.logic.BaseDataImpl;
import com.gzunicorn.common.pdfprint.BarCodePrint;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.hibernate.basedata.hotlinefaulttype.HotlineFaultType;
import com.gzunicorn.hibernate.hotlinemanagement.calloutmaster.CalloutMaster;
import com.gzunicorn.hibernate.hotlinemanagement.calloutprocess.CalloutProcess;
import com.gzunicorn.hibernate.sysmanager.Loginuser;

/**
 * 下载电梯维修记录单
 */
public class PrintCalloutProcessServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PrintCalloutProcessServlet() {
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
		this.toPrintCalloutProcessRecord(request, response);
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
		this.toPrintCalloutProcessRecord(request, response);
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
	 * 下载电梯维修记录单
	 */
	private void toPrintCalloutProcessRecord(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		BaseDataImpl bd = new BaseDataImpl();
		
		Session hs = null;	
		String id = request.getParameter("id");
		System.out.println(">>>>开始打印电梯维修记录单 "+id);
		
		if (id != null && !id.equals("")) {			
			try {
				hs = HibernateUtil.getSession();
				
				Query query = hs.createQuery("from CalloutMaster where calloutMasterNo = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {

					// 主信息
					CalloutMaster CM = (CalloutMaster) list.get(0);	
					CM.setRepairMode(CM.getRepairMode().equals("1")?"被动急修":"主动急修");
					CM.setCompanyId(bd.getName(hs, "Customer", "companyName", "companyId",CM.getCompanyId()));
					CM.setIsTrap(CM.getIsTrap().equals("Y")?"困人":"非困人");
					CM.setProjectAddress(bd.getName("ElevatorCoordinateLocation", "rem", "elevatorNo", CM.getElevatorNo()));//项目名称及楼栋号
					
					Query query1 = hs.createQuery("from CalloutProcess where calloutMasterNo = '"+id.trim()+"'");	
					List list1 = query1.list();
					CalloutProcess CP = (CalloutProcess) list1.get(0);
					CP.setAssignObject2(bd.getName(hs, "Loginuser","username", "userid",CP.getAssignObject2()));
					/**
					String hftid=CP.getHftId();//故障类型
		           	 String hftdesc="";
		           	 if(hftid!=null && !hftid.trim().equals("")){
		           		 String sqls="select a from HotlineFaultType a where a.hftId in('"+hftid.replaceAll(",", "','")+"')";
		           		 List loginlist=hs.createQuery(sqls).list();
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
		           	CP.setHftId(hftdesc);
		           	*/
					/**
		           	String r5=CP.getR5();//急修参与人员
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
		           	CP.setR5(r5name);
					*/
					BarCodePrint dy = new BarCodePrint();
					List barCodeList = new ArrayList();
					barCodeList.add(CM);
					barCodeList.add(CP);

					dy.toPrintTwoRecord5(request,response, barCodeList,id);
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

}
