package com.gzunicorn.workflow.event;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.WorkFlowConfig;

public class SignEnterEventHandler implements ActionHandler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void execute(ExecutionContext context) throws Exception {
		TaskMgmtInstance tmi=context.getTaskMgmtInstance();		
		Token token = context.getToken();   
        TaskNode taskNode = (TaskNode) context.getNode();  
        
        ProcessBean pb=(ProcessBean)context.getContextInstance().getVariable(WorkFlowConfig.Flow_Bean);
		pb.initSignselpath();	//初始化会签人员选择的路径	
		String[] actors= pb.getSignActors();
		
		//设置流程属性
        pb.setNodeid(taskNode.getId());
		pb.setNodename(taskNode.getName());
		pb.setStatus(WorkFlowConfig.State_Approve);
		
        Task task=null;
        Set set=taskNode.getTasks();
        if(set!=null){
        	Iterator it=set.iterator();
        	if(it!=null && it.hasNext()){
        		task=(Task)it.next();
        	}
        }

        TaskInstance ti=null;
		if(actors!=null && actors.length>0){		
			String[] act=null;
			for(int i=0;i<actors.length;i++){
				ti=tmi.createTaskInstance(task, token);
				act=getActors(actors[i]);
				if(act!=null && act.length==1){
					ti.setActorId(act[0]);
				}else if(act!=null && act.length>1){
					ti.setPooledActors(act);
				}
			}
		}else{
			
			DebugUtil.print("signactors is null,auto leave this node! ");
			ti=tmi.createTaskInstance(task, token);
			context.leaveNode(getTran(taskNode.generateNextLeavingTransitionName(),ti.getAvailableTransitions()));	
		}
		
	}
	
	private String[] getActors(String actors){
		String[] act=null;
		if(actors!=null && actors.trim().length()>0){
			if(actors.endsWith(",")){
				actors=actors.substring(0,actors.length()-1);
			}
			act=actors.split(",");
		}
		return act;
	}
	private String getTran(String deftran,List trans){
		String tra=null;
		if(deftran!=null){
			tra=deftran;
		}else{
			Transition tr=null;
			for(int i=0;i<trans.size();i++){
				tr=(Transition)trans.get(i);
				if(tr!=null && tr.getName()!=null){
					tra=tr.getName();
					break;
				}
			}
		}
		////System.out.println("trans : "+tra);
		return tra;
	}
	
	private String getTran_old(String deftran,Object[] trans){
		String tra=null;
		if(deftran!=null){
			tra=deftran;
		}else{
			Transition tr=null;
			for(int i=0;i<trans.length;i++){
				tr=(Transition)trans[i];
				if(tr!=null && tr.getName()!=null){
					tra=tr.getName();
					break;
				}
			}
		}
		////System.out.println("trans : "+tra);
		return tra;
	}
	

}
