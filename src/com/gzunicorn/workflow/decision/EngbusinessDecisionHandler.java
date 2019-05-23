package com.gzunicorn.workflow.decision;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.Token;
import org.jbpm.graph.node.DecisionHandler;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.util.WorkFlowConfig;

public class EngbusinessDecisionHandler implements DecisionHandler{
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	/**
	 * business
	 * 
	 * 根据选中的子流程,选择走哪条路径	 
	 **/
	
	public String decide(ExecutionContext context) throws Exception {
		String rs="否";
		ProcessBean pb=(ProcessBean)context.getContextInstance().getVariable(WorkFlowConfig.Flow_Bean);
		Token token = context.getToken();
		String tokenName = token.getName();
		String subApproveResult = pb.getSubApproveResult();
		if(!subApproveResult.trim().equals("")){
			String[] subApproveResultArr = subApproveResult.split(",");
			for(int i = 0 ; i < subApproveResultArr.length ; i ++){
				if(tokenName.indexOf(subApproveResultArr[i])>-1){
					rs = "是";
				}
			}
		}

		return rs;
	}
}
