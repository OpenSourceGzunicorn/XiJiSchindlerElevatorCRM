package com.gzunicorn.workflow.assignment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.util.WorkFlowConfig;

public class AppointActorsAssignment implements AssignmentHandler {
	Log log = LogFactory.getLog(AppointActorsAssignment.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void assign(Assignable arg0, ExecutionContext arg1) throws Exception {
		ProcessBean pb=(ProcessBean)arg1.getContextInstance().getVariable(WorkFlowConfig.Flow_Bean);
		String[] actor=null;
		if(pb!=null){
			actor=pb.getAppointActors();
		}else{
			log.warn("Flow Bean Is null! the TaskInstance is "+arg1.getTaskInstance().getId());
		}
		if(actor!=null && actor.length>0){
			if(actor.length==1 && WorkFlowConfig.Flow_IsAutoActor){//当actors为1时，是否直接设为个人任务
				arg0.setActorId(actor[0]);
			}else{
				arg0.setPooledActors(actor);
			}
		}else{
			log.warn("Actor is null! the TaskInstance is "+arg1.getTaskInstance().getId());
		}
		//arg0.setActorId("admin");
		
		
	}
	
	public void assign_old(Assignable arg0, ExecutionContext arg1) throws Exception {
		String actors=(String)arg1.getContextInstance().getVariable(WorkFlowConfig.Appoint_Actors);
		String[] actor=null;
		if(actors!=null){
			actor=actors.split(",");
		}
		if(actor!=null && actor.length>0){
			if(actor.length==1 && WorkFlowConfig.Flow_IsAutoActor){//当actors为1时，是否直接设为个人任务
				arg0.setActorId(actor[0]);
			}else{
				arg0.setPooledActors(actor);
			}
		}
		arg0.setActorId("admin");
		
		
	}

}
