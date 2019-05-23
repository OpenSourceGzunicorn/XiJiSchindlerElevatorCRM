package com.gzunicorn.workflow.assignment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.dao.DBInterface;
import com.gzunicorn.common.dao.ObjectAchieveFactory;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.SysConfig;
import com.gzunicorn.common.util.WorkFlowConfig;

public class BaoXiuActorsAssignment implements AssignmentHandler{
	
	Log log = LogFactory.getLog(BaoXiuActorsAssignment.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void assign(Assignable arg0, ExecutionContext arg1) throws Exception {
		ProcessBean pb=(ProcessBean)arg1.getContextInstance().getVariable(WorkFlowConfig.Flow_Bean);
		String[] user=null;
		if(pb!=null){ 
			if(pb.getUserid()!=null && pb.getApplyuserid()!=null && !pb.getUserid().equalsIgnoreCase(pb.getApplyuserid())){
				user=new String[2];
				user[0]=pb.getUserid();
				user[1]=pb.getApplyuserid();
			}else{
				String r2=pb.getPro("byzz").toString();
				
//				pb.get
				user=new String[1];
				user[0]=r2;
//				String _user="";
//				if(pb.getUserid()!=null){
//					user[0]=pb.getUserid();
//				}else if(pb.getApplyuserid()!=null){
//					user[0]=pb.getApplyuserid();
//				}else{
//					log.warn("Actor is null! the TaskInstance is "+arg1.getTaskInstance().getId());
//				}
//				_user=user[0];
//				List rslist=null;
//				DBInterface dbi=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
//				Session hs=null;
//				try{
//					hs=HibernateUtil.getSession();
//					Connection con=hs.connection();
//					dbi.setCon(con);
//					String sql="select t.r2 from ThreeContract t where t.tokenid="+tokenid;
//					rslist=dbi.queryToList(sql);
//					if(rslist!=null&&rslist.size()!=0){
//						Map map=(Map) rslist.get(0);
//						if(map.get("r2")!=null){
//							user[0]=map.get("r2").toString();
//						}
//						
//					}
//				}catch(Exception e){
//					DebugUtil.print(e);
//				}finally{
//					hs.close();
//				}
				
			}
			if(user!=null){
				if(user.length==1 && WorkFlowConfig.Flow_IsAutoActor){//当actors为1时，是否直接设为个人任务
					arg0.setActorId(user[0]);
				}else{
					arg0.setPooledActors(user);
				}
			}
		}else{
			log.warn("Flow Bean Is null! the TaskInstance is "+arg1.getTaskInstance().getId());
		}
		
	}

}
