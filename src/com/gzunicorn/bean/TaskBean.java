package com.gzunicorn.bean;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.gzunicorn.common.util.DebugUtil;


public class TaskBean implements Serializable{
	
	private static String taskvm="MyTask.vm";
	
	private String flowid;
	private String flowname;
	private List privatelist;
	private List publiclist;
	
//	private String taskid;
//	private String taskname;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public TaskBean(){
		this.privatelist=new ArrayList();
		this.publiclist=new ArrayList();
	}
	public void setFlow(String flowid,String flowname){
		this.flowid=flowid;
		this.flowname=flowname;
	}
	public void addTask(HashMap task,String pubUrl,String processUrl){
		if(task!=null){
			
			task.put("processurl",processUrl+"&tokenid="+task.get("token")+"&flowname="+task.get("flowname"));
			task.put("priurl",task.get("flowurl")+"&tokenid="+task.get("token")+"&taskid="+task.get("taskid")+"&taskname="+task.get("taskname")+"&taskname2="+task.get("taskname2")+"&flowname="+task.get("flowname"));
			task.put("puburl",pubUrl+"&taskid="+task.get("taskid"));
			if(task.get("actorid")!=null && task.get("actorid").toString().length()>0){//个人任务
				//task.put("flowurl",task.get("flowurl")+"&tokenid="+task.get("token")+"&taskid="+task.get("taskid")+"&taskname="+task.get("taskname")+"&taskname2="+task.get("taskname2")+"&flowname="+task.get("flowname"));
				privatelist.add(task);
			}else{//共享任务
//				task.put("flowurl",pubUrl+"&taskid="+task.get("taskid"));
				publiclist.add(task);
			}
		}
	}
//	public void addTask(String taskid,String taskname,String des,String createdate,String actorid,String tokenid){
//		
//	}
	public String getHtml(){
		String html="";
		try {
			VelocityContext context = new VelocityContext();
			context.put("flowid",this.flowid);
			context.put("flowname",this.flowname);
			context.put("privatelist",this.privatelist);
			context.put("publiclist",this.publiclist);
	
	    	StringWriter sw=new StringWriter();
    		Properties properties=new Properties();		    		
    		properties.setProperty(Velocity.RESOURCE_LOADER,"class");
    		properties.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

    		Velocity.init(properties);
	    		
			Template template = Velocity.getTemplate(taskvm,"GBK");
			if(template!=null){
				template.merge(context,sw);
			}
    		html=sw.toString();
		}catch (Exception e) {
			DebugUtil.print(e);
		}
		return html;
	}
	public String getFlowid() {
		return flowid;
	}
	public void setFlowid(String flowid) {
		this.flowid = flowid;
	}
	public String getFlowname() {
		return flowname;
	}
	public void setFlowname(String flowname) {
		this.flowname = flowname;
	}
	
	public int getPriqty() {
		return this.privatelist.size();
	}
	public int getPubqty() {
		return this.publiclist.size();
	}
	public int getAllqty() {
		return this.privatelist.size()+this.publiclist.size();
	}
	public List getPrivatelist() {
		return privatelist;
	}
	public List getPubliclist() {
		return publiclist;
	}

}
