package com.gzunicorn.workflow.event;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.taskmgmt.exe.TaskMgmtInstance;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.workflow.bo.WorkFlowUtil;

public class ApproveAdministationEnterEventHandler implements ActionHandler{

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
		
		//设置流程属性
        pb.setNodeid(taskNode.getId());
		pb.setNodename(taskNode.getName());
		pb.setStatus(WorkFlowConfig.State_Approve);
		
		String RefUserId=pb.getApplyuserid();
		Long nodeid=context.getNode().getId();
		WorkFlowUtil wfu=new WorkFlowUtil();
		HashMap map = new HashMap();
		map.put("refclass1id",pb.getPro("refclass1id"));
		//System.out.println(pb.getPro("checkclass1id"));
		List userList=wfu.getNodeAdminstrationActors(nodeid,RefUserId,pb.getApplydeptid(),"0",map);
		
        Task task=null;
        Set set=taskNode.getTasks();
        if(set!=null){
        	Iterator it=set.iterator();
        	if(it!=null && it.hasNext()){
        		task=(Task)it.next();
        	}
        }

        TaskInstance ti=null;
        boolean tag=false;
		if(userList!=null && userList.size()>0){		
			String[] user=new String[userList.size()];
			HashMap use=null;
			
			for(int i=0;i<userList.size();i++){
				use=(HashMap)userList.get(i);
				user[i]=(String)use.get("userid");
				
				if(RefUserId!=null && RefUserId.equalsIgnoreCase(user[i])){
					tag=true;
					break;
				}
			}
			if(tag){
				DebugUtil.println(taskNode.getId()+" no actors is null,auto leave this node! ");
				context.leaveNode();
			}else{
				ti=tmi.createTaskInstance(task, token);
				ti.setPooledActors(user);
			}
		}else{
			DebugUtil.println(taskNode.getId()+" no actors is null,auto leave this node! ");
			context.leaveNode();//.leaveNode(getTran(taskNode.generateNextLeavingTransitionName(),ti.getAvailableTransitions()));	
		}
		
	}

}
