package com.gzunicorn.workflow.decision;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.DecisionHandler;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.util.WorkFlowConfig;


public class AppointDecisionHandler implements DecisionHandler{

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
		if(pb.getSelpath() == null || pb.getSelpath().length()==0)
		{
			pb.setSelpath(pb.getPro("selpath").toString());
		}
		if(pb!=null){
			rs=pb.getDecisionTran(arg0.getNode().getId(),0);
		}
		return rs;
	}

}
