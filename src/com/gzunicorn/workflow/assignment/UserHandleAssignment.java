package com.gzunicorn.workflow.assignment;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;

public class UserHandleAssignment implements AssignmentHandler{
	/**
	 * 找出可以审批这张单的人
	 */
	public void assign(Assignable arg0, ExecutionContext arg1) throws Exception {
		String agentid=(String)arg1.getContextInstance().getVariable("agentid");
		//System.out.println("UserHandleAssignment  agentid="+agentid);
		String[] user= {"admin"};
		arg0.setPooledActors(user);
		
	}
}
