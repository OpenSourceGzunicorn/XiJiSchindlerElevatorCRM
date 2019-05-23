package com.gzunicorn.workflow.bo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.gzunicorn.bean.ProcessBean;
import com.gzunicorn.common.dao.DBInterface;
import com.gzunicorn.common.dao.ObjectAchieveFactory;
import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.HibernateUtil;
import com.gzunicorn.common.util.WorkFlowConfig;
//-- extends JbpmContext
public class JbpmExtBridge{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JbpmContext jbpmContext=null;
	WorkFlowUtil wfu=new WorkFlowUtil();
	ProcessBean pb=null;

	public JbpmExtBridge() throws Exception {
		JbpmConfiguration config=JbpmConfiguration.getInstance();
    	jbpmContext=config.createJbpmContext();
		if(jbpmContext==null){
			throw new Exception("jbpmContext is null!");
		}
	}
	public JbpmExtBridge(JbpmContext jbpmContext) throws Exception {
		this.jbpmContext=jbpmContext;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 启动新流程
	 *
	 */
//	public ProcessBean startProcess_old(String processName,String UserId,String RefUserId,String processInstanceId,String tip,HashMap arg){
//		ProcessDefinition pd=jbpmContext.getGraphSession().findLatestProcessDefinition(WorkFlowConfig.getProcessDefine(processName));//"baoxiao"
//		ProcessInstance pi=pd.createProcessInstance();
//		pb=new ProcessBean();
//		pb.setUserid(UserId);
//		pb.setApplyuserid(RefUserId);
//		pb.setTablekey(processInstanceId);
//		
//		
//		TaskInstance ti=pi.getTaskMgmtInstance().createStartTaskInstance();
//		ti.setActorId(pb.getUserid());
//		pb.setToken(ti.getToken().getId());
//		pb.setTaskid(ti.getId());
//		
//		if(arg!=null){
//			if(arg.get(ProcessBean.AppointActor)!=null){
//				pb.addAppointActors((String)arg.get(ProcessBean.AppointActor));
//			}
//			if(arg.get(ProcessBean.SignActor)!=null){
//				pb.setSignActors((String[])arg.get(ProcessBean.SignActor));
//			}
//			Iterator it=arg.keySet().iterator();
//			String key=null;
//			while(it.hasNext()){
//				key=(String)it.next();
//				if(key!=null){
//					pb.setPro(key,arg.get(key));
//				}
//			}
//		}
//		ti.getContextInstance().setVariable(WorkFlowConfig.Flow_Bean, pb);
//		
//		ti.end();//提交到下一级
//		return pb;
//	}
	
	public ProcessBean startProcess(String processName,String UserId,String RefUserId,String processInstanceId,String RefDeptId,HashMap arg){
		pb=getPb();
		pb.setUserid(UserId);
		pb.setApplyuserid(RefUserId);
		pb.setTablekey(processInstanceId);
		pb.setApplydeptid(RefDeptId);
		
		if(arg!=null){
			if(arg.get(ProcessBean.AppointActor)!=null){
				pb.addAppointActors((String)arg.get(ProcessBean.AppointActor));
			}
			if(arg.get(ProcessBean.SignActor)!=null){
				pb.setSignActors((String[])arg.get(ProcessBean.SignActor));
			}
			Iterator it=arg.keySet().iterator();
			String key=null;
			while(it.hasNext()){
				key=(String)it.next();
				if(key!=null){
					pb.setPro(key,arg.get(key));
				}
			}
		}
		/*****************************************************************************/
		ProcessDefinition pd=jbpmContext.getGraphSession().findLatestProcessDefinition(WorkFlowConfig.getProcessDefine(processName));//"baoxiao"
		ProcessInstance pi=pd.createProcessInstance();
		TaskInstance ti=pi.getTaskMgmtInstance().createStartTaskInstance();
		ti.setActorId(pb.getUserid());
		pb.setToken(new Long(ti.getToken().getId()));
		pb.setTaskid(new Long(ti.getId()));
		
		//保存流程变量processBean
		ti.getContextInstance().setVariable(WorkFlowConfig.Flow_Bean, pb);
		
		String[] jump=this.Jump(processName,RefUserId,processInstanceId,pd.getVersion());
		if(jump!=null && jump[0].compareToIgnoreCase("0")>0){
	        //动态创建跳级流向   
	        Transition leavingTransition=new Transition("Jump");   
	        leavingTransition.setProcessDefinition(pd);
	        
	        //指定该转向的目的节点是c   
	        Node fromNode=pi.getProcessDefinition().getNode(jump[3]);	//from
	        Node toNode=pi.getProcessDefinition().getNode(jump[5]);		//to
	       
	        leavingTransition.setFrom(fromNode);   
	        leavingTransition.setTo(toNode);
	        
	        //为当前节点添加这个转向.   
	        fromNode.addLeavingTransition(leavingTransition);   
	        //调用这个转向流转流程   
	        ti.end(leavingTransition);
	        //删除这个临时创建的转向   
	        fromNode.removeLeavingTransition(leavingTransition);   
		}else{	
		/*****************************************************************************/
			ti.end();//提交到下一级
		/*****************************************************************************/
		}
		//重新保存，已更新的最新的processbean 信 
		ti.getContextInstance().setVariable(WorkFlowConfig.Flow_Bean, pb);
		return pb;
	}
	/**
	 * 启动流程而不跳级
	 * @param processName
	 * @param UserId
	 * @param RefUserId
	 * @param processInstanceId
	 * @param RefDeptId
	 * @param arg
	 * @return
	 */
	public ProcessBean startProcessNoJump(String processName,String UserId,String RefUserId,String processInstanceId,String RefDeptId,HashMap arg){
		pb=getPb();
		pb.setUserid(UserId);
		pb.setApplyuserid(RefUserId);
		pb.setTablekey(processInstanceId);
		pb.setApplydeptid(RefDeptId);
		
		if(arg!=null){
			if(arg.get(ProcessBean.AppointActor)!=null){
				pb.addAppointActors((String)arg.get(ProcessBean.AppointActor));
			}
			if(arg.get(ProcessBean.SignActor)!=null){
				pb.setSignActors((String[])arg.get(ProcessBean.SignActor));
			}
			Iterator it=arg.keySet().iterator();
			String key=null;
			while(it.hasNext()){
				key=(String)it.next();
				if(key!=null){
					pb.setPro(key,arg.get(key));
				}
			}
		}
		/*****************************************************************************/
		ProcessDefinition pd=jbpmContext.getGraphSession().findLatestProcessDefinition(WorkFlowConfig.getProcessDefine(processName));//"baoxiao"
		ProcessInstance pi=pd.createProcessInstance();
		TaskInstance ti=pi.getTaskMgmtInstance().createStartTaskInstance();
		ti.setActorId(pb.getUserid());
		pb.setToken(new Long(ti.getToken().getId()));
		pb.setTaskid(new Long(ti.getId()));
		
		//保存流程变量processBean
		ti.getContextInstance().setVariable(WorkFlowConfig.Flow_Bean, pb);
		ti.end();//提交到下一级
		//重新保存，已更新的最新的processbean 信息
		ti.getContextInstance().setVariable(WorkFlowConfig.Flow_Bean, pb);
		return pb;
	}
	
	/**
	 * 取当前跳转的from and to 结点
	 * @param processName
	 * @param RefUserId
	 * @param processInstanceId
	 * @param version
	 * @return
	 */
	public String[] Jump(String processName,String RefUserId,String processInstanceId,int version){
		Session hs=null;
		String[] jump={"0","","","","",""};
		try{
			
			hs=HibernateUtil.getSession();
			String sql="exec Sp_JbmpJump '"+processName+"','"+RefUserId+"',"+version;
			//System.out.println(sql);
			ResultSet rs=hs.connection().prepareStatement(sql).executeQuery();
			if(rs.next()){
				jump[0]=rs.getInt("jumpflag")+"";
				jump[1]=rs.getString("jumpmsg");
				jump[2]=rs.getLong("fromid")+"";
				jump[3]=rs.getString("fromname");
				jump[4]=rs.getLong("toid")+"";
				jump[5]=rs.getString("toname");
			}
		}catch(Exception e){
			DebugUtil.print(e);
		}finally{
			hs.close();
		}
		return jump;
	}
	
	/**
	 * 应用：返回审批时指定的流向
	 * 从列表中返回指定的流向
	 * @param tranid
	 * @param ti
	 * @return
	 */
	public Transition getTransitionUseId(long tranid,TaskInstance ti){
		Transition tr=null;
		if(ti!=null && ti.isOpen()){
			List tranList=ti.getAvailableTransitions();
			if(tranList!=null && tranList.size()>0){
				Transition tran=null;
				for(int i=0;i<tranList.size();i++){
					tran=(Transition)tranList.get(i);
					if(tran.getId()==tranid){
						tr=tran;
						break;
					}
				}
			}
		}
		return tr;
	}
	
	/**
	 * Type 0:根据流程定义，node start找; 1:根据task id找;2:根据node id 找,3:TaskInstance
	 * @param type
	 * @param process	流程
	 * @param tasknode  task/node
	 * @return
	 * @throws SQLException 
	 */
	public List getTransition(Connection con,int type,String process,long tasknode) throws SQLException{
		DBInterface db=ObjectAchieveFactory.getDBInterface(ObjectAchieveFactory.MssqlImp);
		db.setCon(con);
		String sql="Sp_JbpmGetTransition "+type+",'"+process+"',"+tasknode;
		return db.queryToList(sql);
		
	}
	/**
	 * 获取流程Bean
	 */
	public ProcessBean getProcessBeanUseTI(long tid){
		TaskInstance ti = jbpmContext.getTaskInstance(tid);
		pb=(ProcessBean)ti.getContextInstance().getVariable(WorkFlowConfig.Flow_Bean);
		return pb;
	}
	
	/**
	 * 推动流程到下一级
	 * @param tid 任务实例id
	 * @param tranname
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public ProcessBean goToNextJump(long tid,String tranname,String userid,HashMap arg) throws Exception{
		TaskInstance ti = jbpmContext.getTaskInstance(tid);
		if(ti!=null && ti.isOpen() && (ti.getActorId()==null || ti.getActorId().equalsIgnoreCase(userid))){
			ti.setActorId(userid);
//			ti.end(tranname);
			pb=(ProcessBean)ti.getContextInstance().getVariable(WorkFlowConfig.Flow_Bean);
			pb.setTaskid(new Long(tid));
			
			if(pb.getStatus().intValue()==WorkFlowConfig.State_ApplyMod){
				ProcessDefinition pd=ti.getContextInstance().getProcessInstance().getProcessDefinition();
				
				String[] jump=this.Jump(pd.getName(),pb.getApplyuserid(),null,pd.getVersion());
				if(jump!=null && jump[0].compareToIgnoreCase("0")>0){
			        //动态创建跳级流向   
			        Transition leavingTransition=new Transition("Jump");   
			        leavingTransition.setProcessDefinition(pd);
			        
			        //指定该转向的目的节点是c   
			        Node fromNode=pd.getNode(jump[3]);	//from
			        Node toNode=pd.getNode(jump[5]);		//to
			       
			        leavingTransition.setFrom(fromNode);   
			        leavingTransition.setTo(toNode);
			        
			        //为当前节点添加这个转向.   
			        fromNode.addLeavingTransition(leavingTransition);   
			        //调用这个转向流转流程   
			        ti.end(leavingTransition);
			        //删除这个临时创建的转向   
			        fromNode.removeLeavingTransition(leavingTransition);   
				}else{	
				/*****************************************************************************/
					ti.end(tranname);//提交到下一级
				/*****************************************************************************/
				}
			}else{
				ti.end(tranname);//提交到下一级
			}
			
			//将改变了的数据，重新序列化到流程引擎（重启后仍能取回变化了的数据）
			ti.getContextInstance().setVariable(WorkFlowConfig.Flow_Bean,pb);
			return pb;
		}else{
			throw new Exception("ti had close or be handler by other");
		}
		
	}
	/**
	 * 流向下一级，不作跳级处理
	 * @param tid
	 * @param tranname
	 * @param userid
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	public ProcessBean goToNext(long tid,String tranname,String userid,HashMap arg) throws Exception{
		TaskInstance ti = jbpmContext.getTaskInstance(tid);
		if(ti!=null && ti.isOpen() && (ti.getActorId()==null || ti.getActorId().equalsIgnoreCase(userid))){
			ti.setActorId(userid);
			ti.end(tranname);
			pb=(ProcessBean)ti.getContextInstance().getVariable(WorkFlowConfig.Flow_Bean);
			pb.setTaskid(new Long(tid));
			//将改变了的数据，重新序列化到流程引擎（重启后仍能取回变化了的数据）
			ti.getContextInstance().setVariable(WorkFlowConfig.Flow_Bean,pb);
			return pb;
		}else{
			throw new Exception("ti had close or be handler by other");
		}
		
	}
	/**
	 * 推动流程到下一级
	 * @param tid　任务实例id
	 * @param tran
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public ProcessBean goToNext(long tid,Transition tran,String userid,HashMap arg) throws Exception{
		TaskInstance ti = jbpmContext.getTaskInstance(tid);
		if(ti!=null && ti.isOpen() && (ti.getActorId()==null || ti.getActorId().equalsIgnoreCase(userid))){
			ti.setActorId(userid);
			ti.end(tran);
			pb=(ProcessBean)ti.getContextInstance().getVariable(WorkFlowConfig.Flow_Bean);
			pb.setTaskid(new Long(tid));
//			将改变了的数据，重新序列化到流程引擎（重启后仍能取回变化了的数据）
			ti.getContextInstance().setVariable(WorkFlowConfig.Flow_Bean,pb);
			return pb;
		}else{
			throw new Exception("ti had close or be handler by other");
		}
	}
	/**
	 * 回滚事务
	 *
	 */
	public void setRollBack(){
		if(jbpmContext!=null){
			jbpmContext.setRollbackOnly();
		}
	}
	/**
	 * 关闭jbpmContext
	 *注意：jbpm在close时才将流程相关的对象提交，因此若在close前发生异常，需显式调用setRollBack才能回滚事务
	 */
	public void close(){
		if(jbpmContext!=null){
			jbpmContext.close();
		}
	}
	/**
	 * 取消流程实例
	 *
	 */
	public void cancelProcessInstance(long tokenid)throws Exception{
		//jbpmContext.getToken(tokenid).getProcessInstance().end();
		
		//统一调用wfu
		long[] ids={tokenid};
		wfu.endProcessInstance(ids,this.jbpmContext,1,null);
	}
	/**
	 * 删除流程实例
	 * @throws Exception 
	 *
	 */
	public void delProcessInstance(long tokenid) throws Exception{
//		ProcessInstance pi=jbpmContext.getToken(tokenid).getProcessInstance();
//		jbpmContext.getGraphSession().deleteProcessInstance(pi.getId());
		
//		统一调用wfu
		wfu.toDeleteProcessInstance(tokenid,this.jbpmContext,null);
	}
	/**
	 * 设置任务候选人
	 * @param taskid
	 * @param actors
	 * @return
	 */
	public boolean setTaskActor(long taskid,String[] actors){
		if(actors!=null && actors.length>0){
			if(actors.length==1){
				this.jbpmContext.getTaskInstance(taskid).setActorId(actors[0]);
			}else{
				this.jbpmContext.getTaskInstance(taskid).setPooledActors(actors);
			}
			return true;
		}
		return false;
	}
	/**
	 * 中止流程
	 * @param tokenid
	 * 流程令牌
	 * @param endnodesname
	 * 结束节点
	 */
	public void SuspensionFlow(long tokenid,String endnodesname){
		
		Token token=jbpmContext.getToken(tokenid);
		Collection<TaskInstance> unfTasks=token.getProcessInstance().getTaskMgmtInstance().getUnfinishedTasks(token);
		ProcessDefinition pd=token.getProcessInstance().getProcessDefinition();
		//动态创建跳级流向
        Transition leavingTransition=new Transition("SuspensionFlow");   
        leavingTransition.setProcessDefinition(pd);
        
        Node fromNode=pd.getNode(token.getNode().getName());
        Node toNode=pd.getNode(endnodesname);
        //指定该转向的目的节点是c   
        leavingTransition.setFrom(fromNode);   
        leavingTransition.setTo(toNode);
        
        //为当前节点添加这个转向.   
        fromNode.addLeavingTransition(leavingTransition);  
        //调用这个转向流转流程   
        for (TaskInstance uti : unfTasks) { 
			uti.end(leavingTransition);
		}
        //删除这个临时创建的转向   
        fromNode.removeLeavingTransition(leavingTransition);
		
	}

	public ProcessBean getPb() {
		if(this.pb==null){
			this.pb=new ProcessBean();
		}
		return this.pb;
	}
	// hotline start 
	public long getTaskMsnId(long tokenid){
		Token token=jbpmContext.getToken(tokenid);
		return token.getProcessInstance().getTaskMgmtInstance().getId();
		 
	}
	// hotline end 
	
}
