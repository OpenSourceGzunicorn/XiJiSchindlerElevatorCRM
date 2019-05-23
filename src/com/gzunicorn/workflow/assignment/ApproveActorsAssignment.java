package com.gzunicorn.workflow.assignment;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.workflow.bo.WorkFlowUtil;

public class ApproveActorsAssignment implements AssignmentHandler {
	Log log = LogFactory.getLog(ApproveActorsAssignment.class);
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
			List userList=wfu.getNodeActors(nodeid,RefUserId,pb.getApplydeptid(),"0",null);
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
