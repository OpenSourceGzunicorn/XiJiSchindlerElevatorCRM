package com.gzunicorn.workflow.assignment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.util.WorkFlowConfig;


public class ApplyActorsAssignment implements AssignmentHandler {
	Log log = LogFactory.getLog(ApplyActorsAssignment.class);
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
				user=new String[1];
				if(pb.getUserid()!=null){
					user[0]=pb.getUserid();
				}else if(pb.getApplyuserid()!=null){
					user[0]=pb.getApplyuserid();
				}else{
					log.warn("Actor is null! the TaskInstance is "+arg1.getTaskInstance().getId());
				}
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
	
	public void assign_old(Assignable arg0, ExecutionContext arg1) throws Exception {
		String UserId=(String)arg1.getContextInstance().getVariable(WorkFlowConfig.Flow_IssueMan);
		String RefUserId=(String)arg1.getContextInstance().getVariable(WorkFlowConfig.Flow_RefUserId);
		String[] user=null;
		if(UserId!=null && RefUserId!=null && !UserId.equalsIgnoreCase(RefUserId)){
			user=new String[2];
			user[0]=UserId;
			user[1]=RefUserId;
		}else{
			user=new String[1];
			if(UserId!=null){
				user[0]=UserId;
			}else{
				user[0]=RefUserId;
			}
		}
		if(user!=null){
			if(user.length==1 && WorkFlowConfig.Flow_IsAutoActor){//当actors为1时，是否直接设为个人任务
				arg0.setActorId(user[0]);
			}else{
				arg0.setPooledActors(user);
			}
		}
		
	}

}
