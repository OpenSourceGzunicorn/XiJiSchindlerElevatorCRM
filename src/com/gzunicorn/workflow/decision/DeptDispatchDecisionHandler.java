package com.gzunicorn.workflow.decision;

import org.hibernate.Session;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.DecisionHandler;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
import com.gzunicorn.hibernate.sysmanager.Storageid;
import com.gzunicorn.workflow.bo.JbpmExtBridge;


public class DeptDispatchDecisionHandler implements DecisionHandler{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * 部门分类跳转
	 * 
	 */
	
	public String decide(ExecutionContext arg0) throws Exception {
		String rs="";
		ProcessBean pb=(ProcessBean)arg0.getContextInstance().getVariable(WorkFlowConfig.Flow_Bean);
		
		Session hs=null;
		Storageid dept=null;
		try{
			hs=HibernateUtil.getSession();
			dept=(Storageid)hs.get(Storageid.class,pb.getApplydeptid());
			
			if(dept!=null && dept.getStoragetype()!=""){
				rs=pb.getDecisionTran2(arg0.getNode().getId(),dept.getStoragetype(),0,0);
				if(rs==null){
					rs="";
				}
			}
		}catch(Exception e){
			DebugUtil.print(e);
		}finally{
			hs.close();
		}
		return rs;
	}

}
