package com.gzunicorn.common.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.gzunicorn.common.util.DebugUtil;
import com.gzunicorn.common.util.Msg;


/**
 * xml格式
 * <?xml version='1.0' " + "encoding='GBK'?>
 * <root>
 * <msg code='0' message='操作成功'/>
 * <dataset>
 * 		<row id='1' name='master'>
 * 			<col id='1' name='codeid' value='c1'/>
 * 			<col id='n' name='codeid' value='cn'/>
 * 			<child>
 * 				<row id='1' name='master'>
 * 					<col id='1' name='codeid' value='c1'/>
 * 					<col id='n' name='codeid' value='cn'/>
 * 					<child>
 * 						...
 * 					</child>
 * 				</row>
 * 			</child>
 * 		</row>
 * </dataset>
 * </root>
 * 
 * 一般可限定为两层
 */
public class MakeUpXML4 {
	private String root="root";
	private String version = "1.0";
	private String encoding = "UTF-8";
	private String header = "";
	private List rows=new ArrayList();
	
	private String msgid=null;
	private String msginfo=null;
	
	public static String MsgTag="msg";
	public static String DataSetTag="dataset";
	public static String RowTag="row";
	public static String ColTag="col";
	public static String ChildTag="child";
	
	public MakeUpXML4(){
		this.header="<?xml version='" + version + "' " + "encoding='" + encoding + "'?>";
	}
	public MakeUpXML4(String root,String version,String encoding){
		this.root=root==null?this.root:root;
		this.version=version==null?this.version:version;
		this.encoding=encoding==null?this.encoding:encoding;
		this.header="<?xml version='" + version + "' " + "encoding='" + encoding + "'?>";
	}
	public MakeUpXML4(String head){
		this.header=head;
	}
	public MakeUpXML4(InputStream in){
		try{
			this.unpackXml(in);
		}catch(Exception e){
			this.setMsgid(Msg.msg_exc+"");
			this.setMsginfo("unpackXML Exception:"+e.getMessage());
			DebugUtil.print(e);
		}
	}
	/**
	 * 初始化
	 *
	 */
	public void init(){
		this.root="root";
		this.version = "1.0";
		this.encoding = "GBK";
		this.header = "";
		this.rows=new ArrayList();
		
		this.msgid=null;
		this.msginfo=null;
	}
	public RowBean newRowBean(String rname){
		return new RowBean(rname);
	}
	/**
	 * 取生成后的xml
	 * @return
	 */
	public String getXml(){
		StringBuffer xml=new StringBuffer();
		xml.append(this.header);
		xml.append("<"+this.root+">").append(megerToXml(this.rows)).append("</"+this.root+">");
		return xml.toString();
	}
	/**
	 * 取生成后的xml
	 * @return
	 */
	public String getXml(String mscode,String msmsg){
		StringBuffer xml=new StringBuffer();
		xml.append(this.header);
		xml.append("<"+this.root+">");
		xml.append("<msg code='").append(mscode).append("' message='").append(this.formatStr(msmsg)).append("'/>");
		xml.append("<dataset>").append(megerToXml(this.rows)).append("</dataset>");
		xml.append("</"+this.root+">");
		return xml.toString();
	}
	/**
	 * 增加一行
	 * @param rb
	 */
	public void addRow(RowBean rb){
		this.rows.add(rb);
	}
	/**
	 * 利用递归算法整合生成xml
	 * @param rows
	 * @return
	 */
	private String megerToXml(List rows){
		if(rows!=null && rows.size()>0){
			RowBean rb=null;
			ColBean cb=null;
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<rows.size();i++){
				rb=(RowBean)rows.get(i);
				sb.append("<row id='").append(i).append("' name='").append(rb.getRname()).append("'>");
				if(rb.getCols()!=null && rb.getCols().size()>0){
					for(int j=0;j<rb.getCols().size();j++){
						cb=(ColBean)rb.getCols().get(j);
						sb.append("<col id='").append(j).append("' name='").append(cb.getCname()).append("' value='").append(cb.getCvalue()).append("'/>");
					}
				}
				if(rb.getChild()!=null && rb.getChild().size()>0){
					sb.append("<child>");
					sb.append(megerToXml(rb.getChild()));
					sb.append("</child>");
				}
				sb.append("</row>");
			}
			return sb.toString();
		}else{
			return "";
		}
	}
	/**
	 * xml转化为MakeUpXML44对象
	 * @param in
	 * @throws JDOMException
	 * @throws IOException 
	 */
	public void unpackXml(InputStream in) throws JDOMException, IOException{
		this.init();
		if(in!=null){
			SAXBuilder sex=new SAXBuilder();
			Document doc=sex.build(in);
			Element root=doc.getRootElement();
			Element msg=root.getChild(MsgTag);
			if(msg!=null){
				this.msgid=msg.getAttributeValue("code");
				this.msginfo=msg.getAttributeValue("message");
			}
			Element dataset=root.getChild(DataSetTag);
			if(dataset!=null && dataset.getChildren(RowTag)!=null && dataset.getChildren(RowTag).size()>0){
				this.rows=unpackXML(dataset.getChildren(RowTag));
			}
		}else{
			this.setMsgid(com.gzunicorn.common.util.Msg.msg_fal+"");
			this.setMsginfo("无效XML InputStream!");
		}
	}
	
	/**
	 * 递归转化Row
	 * @param rowList
	 * @return
	 */
	private List unpackXML(List rowList){
		List rsList=new ArrayList();
		if(rowList!=null && rowList.size()>0){
			Element row=null;
			Element col=null;
			RowBean rb=null;
			for(int i=0;i<rowList.size();i++){
				row=(Element)rowList.get(i);
				if(row!=null){
					rb=new RowBean();
					rb.setRname(row.getAttributeValue("name"));
					List colList=row.getChildren(ColTag);
					for(int j=0;j<colList.size();j++){
						col=(Element)colList.get(j);
						if(col!=null){
							rb.addCol(col.getAttributeValue("name"),col.getAttributeValue("value"));
						}
					}
					//递归child
					Element child=row.getChild(ChildTag);
					if(child!=null && child.getChildren(RowTag)!=null){
						rb.setChild(unpackXML(child.getChildren(RowTag)));
					}
				}
				rsList.add(rb);
			}
		}
		return rsList;
	}
	
	
//	private 
	
	/**
	 * 行数据属性的封装
	 */
	public class RowBean{
		private String rname;
		private List cols=new ArrayList();
		private List child=new ArrayList();
		
		public RowBean(){
			
		}
		public RowBean(String rname){
			this.rname=rname;
		}
		public void addChild(RowBean rb){
			child.add(rb);
		}
		public void addCol(String cname,String cvalue){
			ColBean cb=new ColBean(cname,cvalue);
			cols.add(cb);
		}
		public List getChild() {
			return child;
		}
		public void setChild(List child) {
			this.child = child;
		}
		public String getRname() {
			return rname;
		}
		public void setRname(String rname) {
			this.rname = rname;
		}
		public List getCols() {
			return cols;
		}
		public void setCols(List cols) {
			this.cols = cols;
		}
	}
	/**
	 * 列数据属性的封装
	 */
	public class ColBean{
		private String cname;
		private String cvalue;
		
		public ColBean(){
			
		}
		
		public ColBean(String cname,String cvalue){
			this.cname=cname;
			this.cvalue=cvalue;
		}
		public String getCname() {
			return cname;
		}
		public void setCname(String cname) {
			this.cname = cname;
		}
		public String getCvalue() {
			return formatStr(cvalue);
		}
		public void setCvalue(String cvalue) {
			this.cvalue = cvalue;
		}
		
	}
	/**
	 * &lt; < 小于号 
	 	&gt; > 大于号 
	 	&amp; & 和 
	 	&apos; ' 单引号 
	 	&quot; " 双引号
	 * @param arg
	 * @return
	 */
	private String formatStr(String arg){
		if(arg!=null){
			return arg.replaceAll("&","&amp;").replaceAll("\"","&quot;").replaceAll("'","&apos;").replaceAll("<","&lt;").replaceAll(">","&gt;");//format 双引号
		}else{
			return arg;
		}
	}
	public static String unFormatStr(String arg){
		if(arg!=null){
			return arg.replaceAll("&amp;","&").replaceAll("&quot;","\"").replaceAll("&apos;","'").replaceAll("&lt;","<").replaceAll("&gt;",">");//format 双引号
		}else{
			return arg;
		}
	}
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public String getMsginfo() {
		return msginfo;
	}
	public void setMsginfo(String msginfo) {
		this.msginfo = msginfo;
	}
	public List getRows() {
		return rows;
	}
}
