package com.gzunicorn.workflow.assignment;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;

import com.gzunicorn.common.util.WorkFlowConfig;

public class UserAssignment implements AssignmentHandler{
	/**
	 * 设置流程发布人
	 * @param arg0
	 * @param arg1
	 * @throws Exception
	 */
	public void assign(Assignable arg0, ExecutionContext arg1) throws Exception {
		String issueUser=(String)arg1.getContextInstance().getVariable(WorkFlowConfig.Flow_IssueMan);
		String refUser=(String)arg1.getContextInstance().getVariable(WorkFlowConfig.Flow_RefUserId);
		if(issueUser!=null && issueUser.equalsIgnoreCase(refUser)){
			arg0.setActorId(issueUser);
		}else{
			String[] use={issueUser,refUser};
			arg0.setPooledActors(use);
		}
	}
}
