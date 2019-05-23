package com.gzunicorn.struts.action.xjsgg;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.basic.common.CommonUtil;
import com.gzunicorn.common.dao.mssql.DataOperation;
import com.gzunicorn.common.logic.HLBaseDataImpl;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.hibernate.hotlinemanagement.calloutmaster.CalloutMaster;

public class XjsggAction {
	/*
	 * 判断值是否为空
	 */
	public String getsql(String colnum,String value){		
		String sql="";
		if(value!=null && !"".equals(value)){
			sql+=" and "+colnum+" like '%"+value.trim()+"%'";
		}else{
			sql+=" and "+colnum+" like '%'";
		}
		return sql;
	}
	
	/*
	 * 审核，提交等标志
	 * 判断字段值是否为空
	 */
	public String getnullsql(String colnum,String value){		
		String sql="";
		if(value!=null && !"".equals(value)){
			sql+=" and  isnull("+colnum+",'N') like '%"+value.trim()+"%'";
		}else{
			sql+=" and isnull("+colnum+",'N') like '%'";
		}
		return sql;
	}
	
	/*
	 * 获取当前时间
	 * @parament cb
	 */
	public String getdatetime(){		
		String datetime="";
		String date=CommonUtil.getToday();
		String time=CommonUtil.getTodayTime();
		datetime=date+" "+time;
		return datetime;
	}
	/*
	 * 获取流程状态下拉框的值
	 * 
	 */
	public List getProcessState(Session hs){
		List list=new ArrayList();
		List list2=new ArrayList();
		String sql="select typeid,typename from ViewFlowStatus order by orderby ";
		list=hs.createSQLQuery(sql).list();
		for(int i=0;i<list.size();i++){
			Object[] obj=(Object[])list.get(i);
			HashMap hm=new HashMap();
			hm.put("typeid", String.valueOf(obj[0]));
			hm.put("typename", String.valueOf(obj[1]));
			list2.add(hm);
		}
		return list2;
	}
	
	/*
	 * sql获取每个字段的值
	 * table,value1,value2
	 * 
	 *
	 */
	public String getValue(Session hs,String table,String value1,String value2,String value3){
		String value="";
		String sql="Select a."+value1+" from  "+table+" a where a."+value2+"='"+value3+"'";
		List list=hs.createSQLQuery(sql).list();
		if(list!=null && list.size()>0){
			value=(String)list.get(0);
		}
		return value;
	}
   /*
    * hql获得表对象
    * 参数 table 关联字段名 value
    * 
    */
	public List getClasses(Session hs,String table,String billno,String value){
		String hql="select a from "+table+" a where a."+billno+"='"+value+"'";
		List list=hs.createQuery(hql).list();
		return list;
	}
	/*
	 * 查看附件
	 * 
	 * 
	 */
	 public List getfile(Session hs,String table,String filename,String billno){
		 List list=new ArrayList();
		 String sql="select a.*,b.username as UploaderName  from  "+table+" a ,loginuser b "
		 		+ "where a.Uploader = b.userid and a.MaterSid = '"+billno+"' and a.BusTable = '"+filename+"'";
		  list=hs.createSQLQuery(sql).list();
		 return list;
		 
	 }
	 /**
		 * 生成急修主单号
		 * @return
		 */
		public static synchronized String genCalloutMasterNum(){//create the ConsultMaster number
			// 处理标头部分
			String prefix = "BX";

			// 处理日期部分
			SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyyMM");
			String dateStr = simpleFormatter.format(new Date());
			// 处理顺序号部分
			List<CalloutMaster> list = null;
			// 处理顺序号部分
			Transaction tx = null;
			Session session = null;
			String numHead = prefix + dateStr;
			String numEnd = "";
			String hql = "FROM CalloutMaster as cm WHERE cm.calloutMasterNo like '"+ numHead + "%' order by calloutMasterNo desc";
			try {
				session = HibernateUtil.getSession();
				Query query = session.createQuery(hql);
				list = query.list();

				if (list == null || list.isEmpty() || list.size() == 0) {
					// 还没有记录，序列号从0001开始
					numEnd = "0001";
				} else {
					// 如果有纪录,去最新的纪录,然后+1,代码要会自动补前置的0
					String currentNo = ((CalloutMaster) list.get(0)).getCalloutMasterNo();
					currentNo = currentNo.replace(numHead, "");
					numEnd = String.format("%04d",Integer.parseInt(currentNo) + 1);
				}

			}catch (Exception e) {
				e.printStackTrace();
			}
			//System.out.println("急修主单号: "+numHead + numEnd);
			return numHead + numEnd;
		}
		/* 急修流程获
		 * 得处理状态
		 * 
		 */
		public List getselect(Session hs,String table,String billno,String value){
        String hql="select a from Pulldown a where a.id.typeflag='CalloutMaster_HandleStatus' "
        		+ "and a.enabledflag='Y' order by orderby";
			List list=hs.createQuery(hql).list();
			return list;
		}
		/**
		 * 发送短信方法，使用短信猫
		 * @param content
		 * @param phone
		 * @return
		 */
		public static boolean sendMessage(String smsContent, String telNo){
			Boolean finash=true;
			try{
				HLBaseDataImpl bd = new HLBaseDataImpl();
				String dbUser = "admin"; //缺省用户？  
				String dbPwd = "668899";  
				String url="jdbc:odbc:driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ= D:\\短信猫多模通用接口\\GSM_data.mdb;";//你的ACCESS文件位置
			    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			    Connection conn=DriverManager.getConnection(url,dbUser,dbPwd);
			    String s_mob = telNo;//手机号 
			    String s_com = "1";//设备编号 默认1 
			    String s_info = smsContent;//待发送信息内容 
			    String s_style = "0";//0是新信息，1是成功，2是失败 
			    String s_time = bd.getDateTimeStr(bd.addMin(new Date(), 1));// 定时发送时间,不定时发送不用写入 
			    String s_userid = "";//实际发送时间,不用写入
			    String s_client = "";//无用 
			    String s_inputtime = bd.getNowDateTimeStr();//无用 
			    String sql="insert into send_Info (s_mob,s_com,s_info,s_style,s_Inputtime,s_time) values ('" + s_mob+ "', " + s_com + ", '" +s_info+"'," + s_style+ ",'"+ s_inputtime +"','" + s_time+"')"  ;
				conn.prepareStatement(sql).executeUpdate();
				conn.commit();
				//System.out.println("-------------------------------");
				//System.out.println("SMS Send Number:" + telNo);
				//System.out.println("SMS Send Content" + smsContent);
				//System.out.println("-------------------------------");
			}catch(Exception ex){
				//System.out.println("发送短信失败");
				finash=false;
				ex.printStackTrace();
				
			}
			
			
		
			return finash;
		}	
	  
}
