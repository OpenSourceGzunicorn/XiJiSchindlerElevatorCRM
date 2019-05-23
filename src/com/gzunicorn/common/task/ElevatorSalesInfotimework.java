package com.gzunicorn.common.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import org.apache.ojb.broker.util.logging.Logger;
import org.apache.ojb.broker.util.logging.LoggerFactory;
import org.apache.struts.actions.DispatchAction;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gzunicorn.common.util.DataStoreException;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.hibernate.elevatorsalesInfo.YJLinkDataBaseAction;

public class ElevatorSalesInfotimework extends DispatchAction implements Job{
	Logger log=LoggerFactory.getLogger(ElevatorSalesInfotimework.class);
	public ElevatorSalesInfotimework() {
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//System.out.println("测试开始！");
		//System.out.println(this.insertworktime());
		//System.out.println("测试结束！");
		// TODO Auto-generated method stub
		
	}
	
	private String insertworktime(){
		String msg="数据导入成功！";
		Session hs=null;
		Session hs2=null;
		Transaction tx=null;
		List grcrmList=null;	
		PreparedStatement ps=null;
		try {

			hs = HibernateUtil.getSession();
			tx=hs.beginTransaction();
		    hs2=YJLinkDataBaseAction.getSession();
		    String sql5="TRUNCATE TABLE ElevatorSalesInfo2";//删除中间表数据
		    hs.connection().prepareStatement(sql5).executeUpdate();
		    
            String sql="select * from ElevatorSalesInfo";
            grcrmList=hs2.createSQLQuery(sql).list();//跨数据库提取的数据
            
            if(grcrmList!=null && grcrmList.size()>0){//插数据进入中间表
            String sql2="insert into ElevatorSalesInfo2 values(?,?,?,?,?,"
            		+ "?,?,?,?,?"
            		+ ",?,?,?,?,?"
            		+ ",?,?,?,?,?,"
            		+ "?,?,?,?,?"
            		+ ",?,?,?,?,?,"
            		+ "?,?,?,?,?,?)";
            ps=hs.connection().prepareStatement(sql2);
            for(int i=0;i<grcrmList.size();i++){
            	Object[] obj=(Object[])grcrmList.get(i);
                ps.setString(1, String.valueOf(obj[0]));
                ps.setString(2, String.valueOf(obj[1]));
                ps.setString(3, String.valueOf(obj[2]));
                ps.setString(4, String.valueOf(obj[3]));
                ps.setString(5, String.valueOf(obj[4]));
                ps.setString(6, String.valueOf(obj[5]));
                ps.setString(7, String.valueOf(obj[6]));
                ps.setInt(8, Integer.valueOf(String.valueOf(obj[7])).intValue());
                ps.setInt(9, Integer.valueOf(String.valueOf(obj[8])).intValue());
                ps.setInt(10, Integer.valueOf(String.valueOf(obj[9])).intValue());               
                ps.setString(11, String.valueOf(obj[10]));
                ps.setString(12, String.valueOf(obj[11]));
                ps.setDouble(13, Double.valueOf(String.valueOf(obj[12])).doubleValue());
                ps.setString(14, String.valueOf(obj[13]));
                ps.setString(15, String.valueOf(obj[14]));
                ps.setString(16, String.valueOf(obj[15]));
                ps.setString(17, String.valueOf(obj[16]));
                ps.setString(18, String.valueOf(obj[17]));
                ps.setString(19, String.valueOf(obj[18]));
                ps.setString(20, String.valueOf(obj[19]));
                ps.setString(21, String.valueOf(obj[20]));
                ps.setString(22, String.valueOf(obj[21]));
                ps.setString(23, String.valueOf(obj[22]));
                ps.setString(24, String.valueOf(obj[23]));
                ps.setString(25, String.valueOf(obj[24]));
                ps.setString(26, String.valueOf(obj[25]));
                ps.setString(27, String.valueOf(obj[26]));
                ps.setString(28, String.valueOf(obj[27]));
                ps.setString(29, String.valueOf(obj[28]));
                ps.setString(30, String.valueOf(obj[29]));
                ps.setDouble(31, Double.valueOf(String.valueOf(obj[30]).equals("null")?"0.0":String.valueOf(obj[30])).doubleValue());
                ps.setDouble(32, Double.valueOf(String.valueOf(obj[31]).equals("null")?"0.0":String.valueOf(obj[31])).doubleValue());
                ps.setDouble(33, Double.valueOf(String.valueOf(obj[32]).equals("null")?"0.0":String.valueOf(obj[32])).doubleValue());
                ps.setInt(34, Integer.valueOf(String.valueOf(obj[33]).equals("null")?"0":String.valueOf(obj[33])).intValue());
                ps.setInt(35, Integer.valueOf(String.valueOf(obj[34]).equals("null")?"0":String.valueOf(obj[34])).intValue());
                ps.setString(36,"");
                ps.addBatch();
                
             }
            ps.executeBatch();
            }
            
            String sql4="exec sp_ElevatorSalesInfoto_two ''";
            hs.connection().prepareStatement(sql4).execute();
            tx.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			msg="数据导入没有成功！";
			e.printStackTrace();
		}
		finally{
			try{
				if(hs!=null){
					hs.close();
					
				}
				if(ps!=null){
					ps.close();
				}
				if(hs2!=null){
					hs2.close();
				}
			}catch (Exception e2) {
				e2.printStackTrace();
	        }
	  }

		return msg;
	}
	public static void main(String[] asg){
		ElevatorSalesInfotimework es=new ElevatorSalesInfotimework();
		try {
			es.execute(null);
		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
