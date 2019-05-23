package com.gzunicorn.workflow.decision;

import org.hibernate.Session;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.DecisionHandler;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.workflow.bo.JbpmExtBridge;


public class JumpDecisionHandler implements DecisionHandler{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * 根据费用类型，跳转到不同分支
	 * 
	 * 根据是否需会签及费用大类，选择不同的分支
	 */
	
	public String decide(ExecutionContext arg0) throws Exception {
		String rs="";
		ProcessBean pb=(ProcessBean)arg0.getContextInstance().getVariable(WorkFlowConfig.Flow_Bean);
		
		ProcessDefinition pd=arg0.getProcessDefinition();
		JbpmExtBridge jbpmExtBridge=new JbpmExtBridge(null);
		
		String[] jump=jbpmExtBridge.Jump(pd.getName(),pb.getApplyuserid(),null,pd.getVersion());
		
		if(jump!=null && jump[0].compareToIgnoreCase("0")>0){
			Transition leavingTransition=arg0.getNode().getLeavingTransition(jump[5]);
			if(leavingTransition!=null){
		        //动态创建跳级流向   
		        leavingTransition=new Transition(jump[5]);   
		        leavingTransition.setProcessDefinition(pd);
		        
		        //指定该转向的目的节点是c   
		        Node fromNode=pd.getNode(jump[3]);	//from
		        Node toNode=pd.getNode(jump[5]);		//to
		       
		        leavingTransition.setFrom(fromNode);   
		        leavingTransition.setTo(toNode);
		        
		        //为当前节点添加这个转向.   
		        fromNode.addLeavingTransition(leavingTransition);   
			}
			if(leavingTransition!=null){
				rs=leavingTransition.getName();
			}
		}
		return rs;
	}

}
