package com.gzunicorn.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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
import com.gzunicorn.hibernate.infomanager.qualitycheckmanagement.QualityCheckManagement;

/**
 * 下载维保质量检查单
 */
public class PrintQualityCheckServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PrintQualityCheckServlet() {
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
		this.toPrintQualityCheckRecord(request, response);
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

		//System.out.println(">>>>doGet");
		this.toPrintQualityCheckRecord(request, response);
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
	 * 下载维保质量检查单
	 */
	private void toPrintQualityCheckRecord(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		BaseDataImpl bd = new BaseDataImpl();
		
		Session hs = null;	
		Connection conn = null;
		String id = request.getParameter("id");
		System.out.println(">>>>开始打印维保质量检查单 "+id);
		
		if (id != null && !id.equals("")) {			
			try {
				hs = HibernateUtil.getSession();
				DataOperation op = new DataOperation();
				conn = (Connection) hs.connection();
				op.setCon(conn);
				
				Query query = hs.createQuery("from QualityCheckManagement where billno = '"+id.trim()+"'");
				List list = query.list();
				if (list != null && list.size() > 0) {

					// 主信息
					QualityCheckManagement QM = (QualityCheckManagement) list.get(0);	
					String CompanyID=bd.getName("MaintContractMaster", "companyId", "maintContractNo",QM.getMaintContractNo());
					QM.setMaintPersonnel(bd.getName(hs, "Loginuser","username", "userid",QM.getMaintPersonnel()));// 报修人
					QM.setR2(bd.getName(hs, "ElevatorCoordinateLocation","rem", "elevatorNo",QM.getElevatorNo()));
					QM.setR3(bd.getName(hs, "Customer", "companyName", "companyId",CompanyID));
					QM.setR4(bd.getName(hs, "Loginuser","username", "userid",QM.getChecksPeople()));//督查人员

					String hecirListsql="select * from MarkingScoreRegister where billno = '"+id.trim()+"' and isDelete='N'";
					List hecirList =op.queryToList(hecirListsql);

					BarCodePrint dy = new BarCodePrint();
					List barCodeList = new ArrayList();
					barCodeList.add(QM);
					barCodeList.add(hecirList);
					dy.toPrintTwoRecord6(request,response, barCodeList,id);
				
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
