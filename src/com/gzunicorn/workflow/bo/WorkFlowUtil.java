package com.gzunicorn.workflow.bo;

import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.gzunicorn.common.dao.DBInterface;
import com.gzunicorn.common.dao.ObjectAchieveFactory;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.Msg;
import com.gzunicorn.hibernate.jbpmmanager.Jbpmtokenflow;
public class WorkFlowUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 取指定流程角色用户
	 * @param FlowRole
	 * @param RefUserId
	 * @param FlowType: 0_agentgoodsplan
	 * @param WriteFlag
	 * @return
	 */
	public List getAssignmentUser(String FlowRole,String RefUserId,int FlowType,String WriteFlag){
		DBInterface dbi=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
		List rslist=null;
		Session hs=null;
		try{
			hs=HibernateUtil.getSession();
			Connection con=hs.connection();
			dbi.setCon(con);
			String sql="Sp_FetchFlowRoleUser '"+FlowRole+"','"+RefUserId+"',"+FlowType+",'"+WriteFlag+"'";
			rslist=dbi.queryToList(sql);
		}catch(Exception e){
			DebugUtil.print(e);
		}finally{
			hs.close();
		}
		return rslist;
	}
	/**
	 * 删除指定的流程
	 * @param form
	 * @param request
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	public Msg toDeleteToken(ActionForm form,HttpServletRequest request,JbpmConfiguration config,JbpmContext jbpmContext,HashMap arg)throws Exception {
		Msg msg=new Msg(Msg.msg_suc,"");
		long token=-1;
		token=Long.parseLong((String)arg.get("tokenid"));
		try{	
			//System.out.println("token="+token);
			if(token!=-1){
				Token tok=jbpmContext.getToken(token);
				if(tok!=null){
					ProcessInstance pi=tok.getProcessInstance();				
					//System.out.println("pid="+pi.getId());
				    jbpmContext.getGraphSession().deleteProcessInstance(pi.getId());
				}
			}
		}catch(Exception e){
			msg.setMsg(Msg.msg_exc,e.getMessage());
			DebugUtil.print(e);
		}
		return msg;
	}
	
	/**
	 * 通过抛异常来判断是否删除成功
	 * @param tokenid
	 * @param jbpmContext
	 * @param arg
	 * @throws Exception
	 */
	public void toDeleteProcessInstance(long tokenid,JbpmContext jbpmContext,HashMap arg)throws Exception {
		ProcessInstance pi=jbpmContext.getToken(tokenid).getProcessInstance();
//		GraphSession gs=jbpmContext.getGraphSession();
		jbpmContext.getGraphSession().deleteProcessInstance(pi.getId());
	}
	/**
	 * 应用：流程审批时，可选流向
	 * flag=1,根据taskid 取当前实例的有效流向.
	 * flag=2,根据ti 取当前实例的有效流向.
	 * 
	 * 注：ti2.isOpen()不能少，原因：当ti2是关闭时，ti2.getAvailableTransitions()返回的是非当前实例的Transition
	 * @param jbpmContext
	 * @param taskid
	 * @param ti
	 * @param flag
	 * @return
	 */
	public List getTaskInstanceTransition(JbpmContext jbpmContext,long taskid,TaskInstance ti,int flag){
		List rs=null;
		switch(flag){
		case 1:
			if(jbpmContext!=null){
				TaskInstance ti2=jbpmContext.getTaskInstance(taskid);
				if(ti2!=null && ti2.isOpen()){//注：ti2.isOpen()不能少，原因：当ti2是关闭时，ti2.getAvailableTransitions()返回的是非当前实例的Transition
					List avatran=ti2.getAvailableTransitions();
					if(avatran!=null && avatran.size()>0){
						rs=new ArrayList();
						HashMap trans=null;
						for(int i=0;i<avatran.size();i++){
							Transition tran=(Transition)avatran.get(i);
							trans=new HashMap();
							trans.put("tranid",tran.getId());
							trans.put("tranname",tran.getName());
							rs.add(trans);
						}
					}
				}
			}
			break;
		case 2:
			if(ti!=null && ti.isOpen()){
				List tranList=ti.getAvailableTransitions();
				if(tranList!=null && tranList.size()>0){
					rs=new ArrayList();
					HashMap trans=null;
					for(int i=0;i<tranList.size();i++){
						Transition tran=(Transition)tranList.get(i);
						trans=new HashMap();
						trans.put("tranid",tran.getId());
						trans.put("tranname",tran.getName());
						rs.add(trans);
					}
				}
			}
			break;
		}
		return rs;
	}
	/**
	 * 应用：返回审批时指定的流向
	 * 从列表中返回指定的流向
	 * @param tranid
	 * @param ti
	 * @return
	 */
//	public Transition getSelTransition(long tranid,TaskInstance ti){
//		Transition tr=null;
//		if(ti!=null && ti.isOpen()){
//			List tranList=ti.getAvailableTransitions();
//			if(tranList!=null && tranList.size()>0){
//				Transition tran=null;
//				for(int i=0;i<tranList.size();i++){
//					tran=(Transition)tranList.get(i);
//					if(tran.getId()==tranid){
//						tr=tran;
//						break;
//					}
//				}
//			}
//		}
//		return tr;
//	}
	/**
	 * 根据node id　取出Actors User
	 * @param NodeId
	 * @param RefUserId
	 * @param Flag
	 * @param other
	 * @return
	 */
	public List getNodeActors(Long NodeId,String RefUserId,String RefDeptId,String Flag,HashMap other){
		DBInterface dbi=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
		List rslist=null;
		Session hs=null;
		try{
			hs=HibernateUtil.getSession();
			Connection con=hs.connection();
			dbi.setCon(con);
			String sql="Sp_JbpmGetNodeActors "+NodeId+",'"+RefUserId+"','"+RefDeptId+"'";
			rslist=dbi.queryToList(sql);
		}catch(Exception e){
			DebugUtil.print(e);
		}finally{
			hs.close();
		}
		return rslist;
	}
	/**
	 * 终止流程实例,异常由调用方处理，是否要rollback
	 * @param processInstanceId_TokenId
	 * @param jbpmContext
	 * @param flag,0:processInstanceId,1:tokenId
	 * @param arg
	 * @return
	 */
	public void endProcessInstance(long[] Ids,JbpmContext jbpmContext,int flag,HashMap arg){
		switch(flag){
		case 0:	//processInstanceId
			if(Ids!=null && Ids.length>0){
				for(int i=0;i<Ids.length;i++){
					jbpmContext.getProcessInstance(Ids[0]).end();
				}
			}
			break;
		case 1:	//tokenId
			if(Ids!=null && Ids.length>0){
				for(int i=0;i<Ids.length;i++){
					jbpmContext.getToken(Ids[0]).getProcessInstance().end();
				}
			}
			break;
		}
	}
	
	/**
	 * 根据node id　取出Actors User(人事行政管理 营销管理部/市场推广部/产品企划部经理 审批前面节点专用)
	 * @param NodeId
	 * @param RefUserId
	 * @param Flag
	 * @param other
	 * @return
	 * @author cwy 2009-04-02
	 */
	public List getNodeAdminstrationActors(Long NodeId,String RefUserId,String RefDeptId,String Flag,HashMap other){
		DBInterface dbi=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
		List rslist=null;
		Session hs=null;
		String refclass1id = other.get("refclass1id").toString();
		try{
			hs=HibernateUtil.getSession();
			Connection con=hs.connection();
			dbi.setCon(con);
			String sql="Sp_JbpmAdminstrationNodeActors "+NodeId+",'"+refclass1id+"','"+RefDeptId+"'";
			rslist=dbi.queryToList(sql);
		}catch(Exception e){
			DebugUtil.print(e);
		}finally{
			hs.close();
		}
		return rslist;
	}
	
	/**
	 * 根据职位　取出Actors User(人事行政管理 营销管理部/市场推广部/产品企划部经理 节点专用)
	 * @param NodeId
	 * @param RefUserId
	 * @param Flag
	 * @param other
	 * @return
	 * @author cwy 2009-04-02
	 */
	public List getNodeAdminCheckActors(Long NodeId,String RefUserId,String RefDeptId,String Flag,HashMap other){
		DBInterface dbi=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
		List rslist=null;
		Session hs=null;
		String checkclass1id = other.get("checkclass1id").toString();
		try{
			hs=HibernateUtil.getSession();
			Connection con=hs.connection();
			dbi.setCon(con);
			String sql="Sp_JbpmAdminCheckNodeActors '"+checkclass1id+"','"+Flag+"'";
			rslist=dbi.queryToList(sql);
		}catch(Exception e){
			DebugUtil.print(e);
		}finally{
			hs.close();
		}
		return rslist;
	}
	
	/**
	 * 根据申请人所在部门取出其科长用户(人事行政管理 科长 审核节点专用)
	 * @param NodeId
	 * @param RefUserId
	 * @param Flag
	 * @param other
	 * @return
	 * @author cwy 2009-04-20
	 */
	public List getNodeAdminSectionActors(Long NodeId,String RefUserId,String RefDeptId,String Flag,HashMap other){
		DBInterface dbi=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
		List rslist=null;
		Session hs=null;
		//String checkclass1id = other.get("checkclass1id").toString();
		try{
			hs=HibernateUtil.getSession();
			Connection con=hs.connection();
			dbi.setCon(con);
			String sql="Sp_JbpmAdminSectionNodeActors '"+RefUserId+"','"+Flag+"'";
			rslist=dbi.queryToList(sql);
		}catch(Exception e){
			DebugUtil.print(e);
		}finally{
			hs.close();
		}
		return rslist;
	}
	
	/**
	 * 根据申请职位(refclass1id)　取出Actors User(人事专员确认 审批节点专用)
	 * @param NodeId
	 * @param RefUserId
	 * @param Flag
	 * @param other
	 * @return
	 * @author cwy 2009-05-08
	 */
	public List getNodeAdminCommissionerActors(Long NodeId,String RefUserId,String RefDeptId,String Flag,HashMap other){
		DBInterface dbi=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
		List rslist=null;
		Session hs=null;
		String refclass1id = other.get("refclass1id").toString();
		try{
			hs=HibernateUtil.getSession();
			Connection con=hs.connection();
			dbi.setCon(con);
			String sql="Sp_JbpmAdminCommonissionerNodeActors '"+refclass1id+"','"+RefDeptId+"'";
			rslist=dbi.queryToList(sql);
		}catch(Exception e){
			DebugUtil.print(e);
		}finally{
			hs.close();
		}
		return rslist;
	}
}
