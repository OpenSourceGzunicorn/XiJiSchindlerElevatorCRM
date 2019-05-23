package com.gzunicorn.workflow.event;

import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.exe.ExecutionContext;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.util.WorkFlowConfig;

public class EndPassTacheEventHandler implements ActionHandler{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void execute(ExecutionContext arg0) throws Exception {
		ProcessBean pb=(ProcessBean)arg0.getContextInstance().getVariable(WorkFlowConfig.Flow_Bean);
		Node node=arg0.getNode();
		pb.setNodeid(node.getId());
		pb.setNodename(node.getName());
		pb.setStatus(WorkFlowConfig.State_Pass);
		
	}

}
