package com.gzunicorn.workflow.assignment;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

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
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.workflow.bo.WorkFlowUtil;

public class DeptActorsAssignment implements AssignmentHandler{
	
	Log log = LogFactory.getLog(BaoXiuActorsAssignment.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void assign(Assignable arg0, ExecutionContext arg1) throws Exception {
		ProcessBean pb=(ProcessBean)arg1.getContextInstance().getVariable(WorkFlowConfig.Flow_Bean);
		if(pb!=null){
			String RefUserId=pb.getApplyuserid();
			Long nodeid=new Long(arg1.getNode().getId());
			WorkFlowUtil wfu=new WorkFlowUtil();
			String sql="select * from LoginUser t where t.RoleID in ('A57','A60','A61','A91','A92','A92','A94') and t.StorageID='"+pb.getApplydeptid()+"'";
			
			DBInterface dbi=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
			List userList=null;
			Session hs=null;
			try{
				hs=HibernateUtil.getSession();
				Connection con=hs.connection();
				dbi.setCon(con);
//				String sql="Sp_JbpmGetNodeActors "+NodeId+",'"+RefUserId+"','"+RefDeptId+"'";
				userList=dbi.queryToList(sql);
			}catch(Exception e){
				DebugUtil.print(e);
			}finally{
				hs.close();
			}
			
//			List userList=wfu.getNodeActors(nodeid,RefUserId,pb.getApplydeptid(),"0",null);
			if(userList!=null && userList.size()>0){
				if(userList.size()==1 && WorkFlowConfig.Flow_IsAutoActor){//当actors为1时，是否直接设为个人任务
					HashMap use=(HashMap)userList.get(0);
					arg0.setActorId((String)use.get("userid"));
				}else{
					String[] user=new String[userList.size()];
					HashMap use=null;
					for (int i = 0; i < userList.size(); i++) {
						use=(HashMap)userList.get(i);
						user[i]=(String)use.get("userid");
					}
					arg0.setPooledActors(user);
				}
			}else{
				log.warn("Actor is null! the TaskInstance is "+arg1.getTaskInstance().getId());
			}
		}else{
			log.warn("Flow Bean Is null! the TaskInstance is "+arg1.getTaskInstance().getId());
		}
		
	}

}
