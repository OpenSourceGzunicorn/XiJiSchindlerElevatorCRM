package com.gzunicorn.common.servlet;

import java.io.IOException;
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
import com.gzunicorn.hibernate.infomanager.elevatortransfercaseregister.ElevatorTransferCaseRegister;
import com.gzunicorn.hibernate.infomanager.handoverelevatorspecialregister.HandoverElevatorSpecialRegister;

/**
 * 下载厂检通知单
 */
public class PrintDisposeServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PrintDisposeServlet() {
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
		this.toPrintDisposeRecord(request, response);
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
		this.toPrintDisposeRecord(request, response);
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
	 * 下载厂检通知单
	 */
	private void toPrintDisposeRecord(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		BaseDataImpl bd = new BaseDataImpl();
		
		Session hs = null;	
		String id = request.getParameter("id");
		System.out.println(">>>>开始打印厂检通知单 "+id);
		
		//List etcpList=new ArrayList();
		if (id != null && !id.equals("")) {			
			try {
				hs = HibernateUtil.getSession();
				
				//只获取 已登记已提交，已审核的数据。
				Query query = hs.createQuery("from ElevatorTransferCaseRegister " +
						"where billNo = '"+id.trim()+"' and processStatus in('2','3')");
				List list = query.list();
				if (list != null && list.size() > 0) {

					// 主信息
					ElevatorTransferCaseRegister register = (ElevatorTransferCaseRegister) list.get(0);															
					register.setAuditor(bd.getName(hs, "Loginuser","username", "userid",register.getAuditor()));// 经办人
					register.setOperId(bd.getName(hs, "Loginuser","username", "userid",register.getOperId()));
					register.setDepartment(bd.getName_Sql("Company", "comname", "comid", register.getDepartment()));
					register.setStaffName(bd.getName(hs, "Loginuser","username", "userid",register.getStaffName()));
					register.setR1(register.getFloor()+"/"+register.getStage()+"/"+register.getDoor());
					
					//安装维保交接电梯情况登记项目
					List hecirList=new ArrayList();
					String sql = "select p.pullname examType,b.checkItem checkItem,b.issueCoding issueCoding,b.issueContents IssueContents1,"
							+ "b.rem rem,b.isRecheck isRecheck,b.isDelete isDelete,b.jnlno jnlno,b.deleteRem,isnull(b.isyzg,'') "
							+ "from HandoverElevatorCheckItemRegister b ,pulldown p "
							+ "where p.pullid=b.examType "
							+ " and p.typeflag='HandoverElevatorCheckItem_ExamType' "
							+ " and b.billno='"+id+"' order by b.isRecheck desc,p.orderby,b.issueCoding ";		
					//System.out.println(sql);
					query = hs.createSQLQuery(sql);	
					List list1 = query.list();
					if(list1!=null&&list1.size()>0){
					for (Object object : list1) {
						Object[] values=(Object[])object;
						Map map=new HashMap();
						map.put("examType", values[0]);
						map.put("checkItem", values[1]);
						map.put("issueCoding", values[2]);
						map.put("issueContents1", values[3]);
						map.put("issueContents", values[4]);
						map.put("isRecheck", values[5]);
						map.put("deleteRem", values[8]);
						if("Y".equals(values[9])){
							map.put("isyzg","已整改");
						}else{
							map.put("isyzg", "");
						}
						
						String hql="from HandoverElevatorCheckFileinfo h where h.handoverElevatorCheckItemRegister.jnlno ='"+(String)values[7]+"' ";
						List fileList=hs.createQuery(hql).list();
						if(fileList!=null&&fileList.size()>0){
							map.put("fileList", fileList);
						}
						if(!values[6].equals("Y")){
							hecirList.add(map);
						}
					 }
					}
					//安装维保交接电梯情况特殊要求
					sql="select r,c.scName from HandoverElevatorSpecialClaim c,HandoverElevatorSpecialRegister r " +
							"where r.scId=c.scId and r.elevatorTransferCaseRegister.billno='"+id+"'";
					List isok_scNameList=hs.createQuery(sql).list();
					List specialRegisters =new ArrayList();
					if(isok_scNameList!=null&&list1.size()>0)
					{
						for(int i=0;i<isok_scNameList.size();i++){
							Object[] objects=(Object[]) isok_scNameList.get(i);
							HandoverElevatorSpecialRegister r=(HandoverElevatorSpecialRegister) objects[0];
							if(r.getIsOk().trim().equals("Y")){
								specialRegisters.add((String)objects[1]);	
							}
						}
					}
					
					//对barCodeList、noticeList操作
					BarCodePrint dy = new BarCodePrint();
					List barCodeList = new ArrayList();
					barCodeList.add(register);
					barCodeList.add(hecirList);
					barCodeList.add(specialRegisters);
					dy.toPrintTwoRecord4(request,response, barCodeList,id,"Y"); 
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
}
