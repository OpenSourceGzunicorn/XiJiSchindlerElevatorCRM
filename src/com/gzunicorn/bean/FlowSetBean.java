package com.gzunicorn.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlowSetBean {
	private String processdefineid;
	private String processdefinename;
	private String version;
	private String processdefinealiasname;
	private String jump;
	private List nodelist=new ArrayList();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public FlowSetBean(List nodelist,List actorlist,List tarlist,List declist,List tranlist){

		HashMap map=null;
		if(nodelist!=null && nodelist.size()>0){
			map=(HashMap)nodelist.get(0);
			this.processdefineid=(String)map.get("processdefineid");
			this.processdefinename=(String)map.get("processdefinename");
			this.version=(String)map.get("version");
			this.processdefinealiasname=(String)map.get("ext1");
			this.jump=(String)map.get("ext2");
			
			Node node=null;
			for(int i=0;i<nodelist.size();i++){
				map=(HashMap)nodelist.get(i);
				node=new Node(map,actorlist,tarlist,declist,tranlist);
				this.nodelist.add(node);
			}
			
		}
	}
	
	

	public String getProcessdefineid() {
		return processdefineid;
	}
	public void setProcessdefineid(String processdefineid) {
		this.processdefineid = processdefineid;
	}
	public String getProcessdefinename() {
		return processdefinename;
	}
	public void setProcessdefinename(String processdefinename) {
		this.processdefinename = processdefinename;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public List getNodelist() {
		return nodelist;
	}
	public String getProcessdefinealiasname() {
		return processdefinealiasname;
	}
	public void setProcessdefinealiasname(String processdefinealiasname) {
		this.processdefinealiasname = processdefinealiasname;
	}
	
	public class Node{
		private String nodeid;
		private String nodeclass;
		private String nodename;
		private String processdefineid;
		private String processdefinename;
		private String version;
		private String des;
		private String orgfilter;
		private String indfilter;
		private String areafilter;
		private String jumpflag;
		private String rem1;
		private String ext1;
		private String ext2;
		private String ext3;
		private String ext4;
		private String ext5;
		
		/**
		 * 判断是固定、还是范围方式
		 */
		private String decflag;
		/**
		 * 判断是否存在配置用户
		 */
		private String actflag;
		
		private List actlist=new ArrayList();
		private List tarlist=new ArrayList();
		private List declist=new ArrayList();
		private List tranlist=new ArrayList();
			
		public Node(HashMap node,List ActList,List TarList,List DecList,List TraList){
			this.setNodeid((String)node.get("nodeid"));
			this.setNodeclass((String)node.get("nodeclass"));
			this.setNodename((String)node.get("nodename"));
			this.setProcessdefineid((String)node.get("processdefineid"));
			this.setProcessdefinename((String)node.get("processdefinename"));
			this.setVersion((String)node.get("version"));
			this.setDes((String)node.get("des"));
			this.setOrgfilter((String)node.get("orgfilter"));
			this.setIndfilter((String)node.get("indfilter"));
			this.setAreafilter((String)node.get("areafilter"));
			this.setJumpflag((String)node.get("jumpflag"));
			this.setRem1((String)node.get("rem1"));
			this.setExt1((String)node.get("ext1"));
			this.setExt2((String)node.get("ext2"));
			this.setExt3((String)node.get("ext3"));
			this.setExt4((String)node.get("ext4"));
			this.setExt5((String)node.get("ext5"));
			
			if(this.getNodeclass()!=null && this.getNodeclass().equalsIgnoreCase("D")){
				setD(DecList,TraList);
			}else if(this.getNodeclass()!=null && this.getNodeclass().equalsIgnoreCase("K")){
				setK(ActList,TarList);
			}
		}
		
		private void setD(List DecList,List TraList){
			if(DecList!=null && DecList.size()>0){
				HashMap d=null;
				for(int i=0;i<DecList.size();i++){
					d=(HashMap)DecList.get(i);
					if(this.nodeid.equalsIgnoreCase((String)d.get("nodeid"))){
						this.setDecflag((String)d.get("flag"));
						this.declist.add(new Dec(d,TraList));
					}
				}
			}
			
			if(TraList!=null && TraList.size()>0){
				HashMap d=null;
				for(int i=0;i<TraList.size();i++){
					d=(HashMap)TraList.get(i);
					if(this.nodeid.equalsIgnoreCase((String)d.get("nodeid"))){
						this.tranlist.add(d);
					}
				}
			}
		}
		
		private void setK(List ActList,List TarList){
			boolean tag=true;
			if(ActList!=null && ActList.size()>0){
				HashMap d=null;
				for(int i=0;i<ActList.size();i++){
					d=(HashMap)ActList.get(i);
					if(this.nodeid.equalsIgnoreCase((String)d.get("nodeid"))){
						this.actlist.add(d);
						tag=false;
					}
				}
			}
			if(tag){
				this.setActflag("0");
			}else{
				this.setActflag("1");
			}
			
			tag=true;
			HashMap map=null;
			if(TarList!=null && TarList.size()>0){
				for(int i=0;i<TarList.size();i++){
					map=(HashMap)TarList.get(i);
					if(this.nodeid.equalsIgnoreCase((String)map.get("nodeid"))){
						this.tarlist.add(map);
						tag=false;
						break;
					}
				}
			}
			if(tag){
				map=new HashMap();
				map.put("nodeid",this.nodeid+"");
				map.put("actionname","");
				map.put("des","");
				map.put("url1","");
				map.put("param1","");
				map.put("target1","");
				map.put("url2","");
				map.put("param2","");
				map.put("target2","");
				map.put("url3","");
				map.put("param3","");
				map.put("target3","");
				map.put("ext1","");
				map.put("ext2","");
				map.put("ext3","");
				map.put("ext4","");
				map.put("ext5","");
				
				this.tarlist.add(map);
			}

		}
		
		public String getAreafilter() {
			return areafilter;
		}
		public void setAreafilter(String areafilter) {
			this.areafilter = areafilter;
		}
		public String getDes() {
			return des;
		}
		public void setDes(String des) {
			this.des = des;
		}
		public String getExt1() {
			return ext1;
		}
		public void setExt1(String ext1) {
			this.ext1 = ext1;
		}
		public String getExt2() {
			return ext2;
		}
		public void setExt2(String ext2) {
			this.ext2 = ext2;
		}
		public String getExt3() {
			return ext3;
		}
		public void setExt3(String ext3) {
			this.ext3 = ext3;
		}
		public String getExt4() {
			return ext4;
		}
		public void setExt4(String ext4) {
			this.ext4 = ext4;
		}
		public String getExt5() {
			return ext5;
		}
		public void setExt5(String ext5) {
			this.ext5 = ext5;
		}
		public String getIndfilter() {
			return indfilter;
		}
		public void setIndfilter(String indfilter) {
			this.indfilter = indfilter;
		}
		public String getJumpflag() {
			return jumpflag;
		}
		public void setJumpflag(String jumpflag) {
			this.jumpflag = jumpflag;
		}
		public String getNodeclass() {
			return nodeclass;
		}
		public void setNodeclass(String nodeclass) {
			this.nodeclass = nodeclass;
		}
		public String getNodeid() {
			return nodeid;
		}
		public void setNodeid(String nodeid) {
			this.nodeid = nodeid;
		}
		public String getNodename() {
			return nodename;
		}
		public void setNodename(String nodename) {
			this.nodename = nodename;
		}
		public String getOrgfilter() {
			return orgfilter;
		}
		public void setOrgfilter(String orgfilter) {
			this.orgfilter = orgfilter;
		}
		public String getProcessdefineid() {
			return processdefineid;
		}
		public void setProcessdefineid(String processdefineid) {
			this.processdefineid = processdefineid;
		}
		public String getProcessdefinename() {
			return processdefinename;
		}
		public void setProcessdefinename(String processdefinename) {
			this.processdefinename = processdefinename;
		}
		public String getRem1() {
			return rem1;
		}
		public void setRem1(String rem1) {
			this.rem1 = rem1;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}

		public String getDecflag() {
			return decflag;
		}

		public void setDecflag(String decflag) {
			this.decflag = decflag;
		}

		public List getActlist() {
			return actlist;
		}

		public void setActlist(List actlist) {
			this.actlist = actlist;
		}

		public List getDeclist() {
			return declist;
		}

		public void setDeclist(List declist) {
			this.declist = declist;
		}

		public List getTarlist() {
			return tarlist;
		}

		public void setTarlist(List tarlist) {
			this.tarlist = tarlist;
		}

		public List getTranlist() {
			return tranlist;
		}

		public void setTranlist(List tranlist) {
			this.tranlist = tranlist;
		}

		public String getActflag() {
			return actflag;
		}

		public void setActflag(String actflag) {
			this.actflag = actflag;
		}
		
	}

	public class Dec{
		private String nodeid;
		private String flag;
		private String selpath;
		private String minqty;
		private String maxqty;
		private String tranpath;
		private String ext1;
		private String ext2;
		private String ext3;
		private String ext4;
		private String ext5;
		private String ext6;
		private String ext7;
		private String ext8;
		private String ext9;
		private String ext10;
		
		private List tranlist=new ArrayList();
		
		public Dec(HashMap dec,List TraList){
			this.setNodeid((String)dec.get("nodeid"));
			this.setFlag((String)dec.get("flag"));
			this.setSelpath((String)dec.get("selpath"));
			this.setMinqty((String)dec.get("minqty"));
			this.setMaxqty((String)dec.get("maxqty"));
			this.setTranpath((String)dec.get("tranpath"));
			
			this.setExt1((String)dec.get("ext1"));
			this.setExt2((String)dec.get("ext2"));
			this.setExt3((String)dec.get("ext3"));
			this.setExt4((String)dec.get("ext4"));
			this.setExt5((String)dec.get("ext6"));
			this.setExt5((String)dec.get("ext7"));
			this.setExt5((String)dec.get("ext8"));
			this.setExt5((String)dec.get("ext9"));
			this.setExt5((String)dec.get("ext10"));
			
			if(TraList!=null && TraList.size()>0){
				HashMap tran=null;
				for(int i=0;i<TraList.size();i++){
					tran=(HashMap)TraList.get(i);
					if(this.getNodeid()!=null && this.getNodeid().equalsIgnoreCase((String)tran.get("nodeid"))){
						this.tranlist.add(tran);
					}
				}
			}
		}

		public String getExt1() {
			return ext1;
		}

		public void setExt1(String ext1) {
			this.ext1 = ext1;
		}

		public String getExt10() {
			return ext10;
		}

		public void setExt10(String ext10) {
			this.ext10 = ext10;
		}

		public String getExt2() {
			return ext2;
		}

		public void setExt2(String ext2) {
			this.ext2 = ext2;
		}

		public String getExt3() {
			return ext3;
		}

		public void setExt3(String ext3) {
			this.ext3 = ext3;
		}

		public String getExt4() {
			return ext4;
		}

		public void setExt4(String ext4) {
			this.ext4 = ext4;
		}

		public String getExt5() {
			return ext5;
		}

		public void setExt5(String ext5) {
			this.ext5 = ext5;
		}

		public String getExt6() {
			return ext6;
		}

		public void setExt6(String ext6) {
			this.ext6 = ext6;
		}

		public String getExt7() {
			return ext7;
		}

		public void setExt7(String ext7) {
			this.ext7 = ext7;
		}

		public String getExt8() {
			return ext8;
		}

		public void setExt8(String ext8) {
			this.ext8 = ext8;
		}

		public String getExt9() {
			return ext9;
		}

		public void setExt9(String ext9) {
			this.ext9 = ext9;
		}

		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}

		public String getMaxqty() {
			return maxqty;
		}

		public void setMaxqty(String maxqty) {
			this.maxqty = maxqty;
		}

		public String getMinqty() {
			return minqty;
		}

		public void setMinqty(String minqty) {
			this.minqty = minqty;
		}

		public String getNodeid() {
			return nodeid;
		}

		public void setNodeid(String nodeid) {
			this.nodeid = nodeid;
		}

		public String getSelpath() {
			return selpath;
		}

		public void setSelpath(String selpath) {
			this.selpath = selpath;
		}

		public String getTranpath() {
			return tranpath;
		}

		public void setTranpath(String tranpath) {
			this.tranpath = tranpath;
		}

		public List getTranlist() {
			return tranlist;
		}
	}

	public String getJump() {
		return jump;
	}
	public void setJump(String jump) {
		this.jump = jump;
	}
	
	
}
