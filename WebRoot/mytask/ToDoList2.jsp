<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='ExtCSS'/>" />
<link rel="stylesheet" type="text/css" href="/drp/common/css/news.css" />
<script type="text/javascript" src="<html:rewrite forward='ExtBase'/>"></script>
<script type="text/javascript" src="<html:rewrite forward='ExtAll'/>"></script>

<style type="text/css">
<!--
.STYLE5 {color: #FF0000}
.STYLE6 {color: #0000FF}
.STYLE7 {color: #0099FF}
.STYLE8 {color:#990033}
-->
</style>
</head>

<body>
<br/>
<br/>
&nbsp;&nbsp;模块:<select name="flowid" id="flowid">
				<option value=""><bean:message key="pageword.all"/></option>
			</select>
&nbsp;&nbsp;<bean:message key="message.title"/>:<input type="text" name="topics" size="40" id="topics" class="default_input"/>&nbsp;&nbsp;<input type="button" name="taskButton" id="taskButton" class="default_input" value="查询" onclick="getTaskList()"/>
<div style="font-size:1px; height:1px;"></div>
<div style="font-size:1px; height:1px;"></div>
<div style="font-size:1px; height:1px;"></div>
<div id="ListtaskDiv">
<logic:present name="TaskList">
<logic:iterate name="TaskList" id="flowitem">
	<table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" class="tb">
		<tr><td class="wordtd"><div align="left">&nbsp;&nbsp;<a href="#<bean:write name="flowitem" property="flowid" filter="false"/>"><bean:write name="flowitem" property="flowname" filter="false"/></a>:[<span class="STYLE5"><bean:write name="flowitem" property="qty" filter="false"/></span>]</td></tr>
	
	<logic:iterate name="flowitem" property="list" id="item">
	<logic:equal name="item" property="actorid" value="">
	<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="<bean:write name="item" property="flowurl2" filter="false"/>&tokenid=<bean:write name='item' property='token' filter="false"/>&taskid=<bean:write name='item' property='taskid' filter="false"/>&taskname=<bean:write name='item' property='taskname' filter="false"/>&taskname2=<bean:write name='item' property='taskname2' filter="false"/>&flowname=<bean:write name='item' property='flowname' filter="false"/>&tasktype=1" target="_self" title="<bean:write name="item" property="tipinfo" filter="false"/>"><bean:write name="item" property="showinfo" filter="false"/></a>&nbsp;&nbsp;
		<a href="<bean:write name='item' property='flowurl2' filter="false"/>&tokenid=<bean:write name='item' property='token' filter="false"/>&taskid=<bean:write name='item' property='taskid' filter="false"/>&taskname=<bean:write name='item' property='taskname' filter="false"/>&taskname2=<bean:write name='item' property='taskname2' filter="false"/>&flowname=<bean:write name='item' property='flowname' filter="false"/>&tasktype=1" target="_self" title="我的任务，立即处理！"><img src="<html:rewrite page='/common/images/toolbar/pritask.gif'/>" border="0"/></a>&nbsp;&nbsp;
		<logic:notEqual name='item' property='viewurl' value="">
		<a href="<bean:write name='item' property='viewurl' filter="false"/>&tokenid=<bean:write name='item' property='token' filter="false"/>&flowname=<bean:write name='item' property='flowname' filter="false"/>" target="_blank" title="<bean:message key="toolbar.viewflow"/>"><img src="<html:rewrite page='/common/images/toolbar/viewvol.gif'/>" border="0"/></a>
		</logic:notEqual>
	</td></tr>
	</logic:equal>
	<logic:notEqual name="item" property="actorid" value="">
		<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="<bean:write name="item" property="flowurl2" filter="false"/>&tokenid=<bean:write name='item' property='token' filter="false"/>&taskid=<bean:write name='item' property='taskid' filter="false"/>&taskname=<bean:write name='item' property='taskname' filter="false"/>&taskname2=<bean:write name='item' property='taskname2' filter="false"/>&flowname=<bean:write name='item' property='flowname' filter="false"/>&tasktype=2" target="_self" title="<bean:write name="item" property="tipinfo" filter="false"/>"><bean:write name="item" property="showinfo" filter="false"/></a>&nbsp;&nbsp;
		<a href="<bean:write name='item' property='flowurl2' filter="false"/>&tokenid=<bean:write name='item' property='token' filter="false"/>&taskid=<bean:write name='item' property='taskid' filter="false"/>&taskname=<bean:write name='item' property='taskname' filter="false"/>&taskname2=<bean:write name='item' property='taskname2' filter="false"/>&flowname=<bean:write name='item' property='flowname' filter="false"/>&tasktype=2" target="_self" title="我的任务，立即处理！"><img src="<html:rewrite page='/common/images/toolbar/pritask.gif'/>" border="0"/></a>&nbsp;&nbsp;
		<logic:notEqual name='item' property='viewurl' value="">
		<a href="<bean:write name='item' property='viewurl' filter="false"/>&tokenid=<bean:write name='item' property='token' filter="false"/>&flowname=<bean:write name='item' property='flowname' filter="false"/>" target="_blank" title="<bean:message key="toolbar.viewflow"/>"><img src="<html:rewrite page='/common/images/toolbar/viewvol.gif'/>" border="0"/></a>
		</logic:notEqual>
	</td></tr>
	</logic:notEqual>
	</logic:iterate>
	</table>
	<br/>
</logic:iterate>
</logic:present>
</div>
<div id="taskListDiv"></div>
<logic:present name="Msg">
<logic:notEqual name="Msg" value="">
	<table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" class="tb"><tr><td class="wordtd"><div align="left">&nbsp;&nbsp;<bean:write name="Msg" filter="false"/></div></td></tr></table>
</logic:notEqual>
</logic:present>
</body>
<script language="javascript">
//去掉空格
 String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g,"");}
function getTaskList(){
	try{
		var req=Ext.Ajax.request({
	   		url: '/XJSCRM/myTaskOaSearchAction.do',
	   		success: sunwFn,
	   		failure: funwFn,
	   		headers: {
	       		'my-header': 'foo'
	   		},
	   		params: { foo: 'bar',showtype:'XML',method:'toDoList'}
		});
	}catch(e){
	}
}
//ajax success to do function
var sunwFn=function(req){
	var xmlobjs = req.responseXML;
	var msg=xmlobjs.documentElement.selectNodes("msg");
	var sts="";
	if(msg!=null && msg[0].getAttribute("code")!=null && msg[0].getAttribute("code")=="0"){
		var rowNodes = xmlobjs.getElementsByTagName('dataset');
		var newlist=new Array();
		var timesnum=0;
		if(rowNodes != null && rowNodes.length>0){
			var obj=rowNodes[0].childNodes;
			if(obj!=null){
				var objlen=obj.length;
				if(objlen>0){
						for(var i=0;i<objlen;i++){
							var colNodes =obj[i].childNodes;
							var colslen=colNodes.length;
							newlist[i]=new Array(objlen);
							for(var k=0;k<colslen;k++){
								if(colNodes[k].getAttribute("name")!=null && colNodes[k].getAttribute("name")=="qty" ){
									newlist[i][0]=colNodes[k].getAttribute("value");
								}else if(colNodes[k].getAttribute("name")!=null && colNodes[k].getAttribute("name")=="flowid" ){
									newlist[i][1]=colNodes[k].getAttribute("value");
								}else if(colNodes[k].getAttribute("name")!=null && colNodes[k].getAttribute("name")=="flowname" ){
									newlist[i][2]=colNodes[k].getAttribute("value");
								}else if(colNodes[k].getAttribute("name")!=null && colNodes[k].getAttribute("name")=="alldata" ){
									newlist[i][3]=colNodes[k].getAttribute("value");
								}
							}
						}
						for(var k=0;k<newlist.length;k++){
							sts+=handleTaskList(document.getElementById("flowid"),document.getElementById("topics"),newlist[k][0],newlist[k][1],newlist[k][2],newlist[k][3]);
						}
				}
			}
			
		}
		if(sts!="" && sts.length>0){
			document.getElementById("taskListDiv").innerHTML=sts;
			document.getElementById("ListtaskDiv").style.display="none";
		}else{
			document.getElementById("taskListDiv").innerHTML="<table width='100%' border='0' align='center' cellpadding='2' cellspacing='0' class='tb'>"
					+"<tr><td class='wordtd'><div align='center'>&nbsp;&nbsp;抱歉!查找不到您想要的记录</div></td></tr></table>";
			document.getElementById("ListtaskDiv").style.display="none";
		}
	}else{
		Ext.Msg.alert('提示', msg[0].getAttribute("message"));
	}
}
var funwFn=function(response,options){
	if(Ext.Msg.isVisible()){
		Ext.Msg.hide();
	}
}
//处理过滤数据并组织列表
/**  alldata 格式: aaaa!#bbbbb!#cccc$%$ddddd!#eeeee$%$ffffff **/
function handleTaskList(obj,obj2,qty,flowid,flowname,alldata){
	var strs="";
	var strs2="";
	var strs3="";
	var ks=0;
	var ss="0";
	if(obj!=null && obj2!=null){
		//去掉字符左右空格
		var flowd=obj.value.replace(/(^\s*)|(\s*$)/g,"");
		var topics=obj2.value.replace(/(^\s*)|(\s*$)/g,"");
		splitstr=alldata.split("$%$");
		strs="<table width='100%' border='0' align='center' cellpadding='2' cellspacing='0' class='tb'>";
		for(i=0;i<splitstr.length;i++){
			splitstr2=splitstr[i].split("!#");
			//模块和主题查询条件都不为空
			if(flowd!="" && topics!=""){
				if(flowd==flowid && splitstr2[0].indexOf(topics)>-1){
					ks=ks+1;
					var viewurl=splitstr2[3];
					strs2+="<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+splitstr2[1]+"' target='_self' title='"+splitstr2[2]+"'>"+splitstr2[0]+"</a>&nbsp;&nbsp;&nbsp;" +
						"<a href='"+splitstr2[1]+"' target='_self' title='我的任务，立即处理！'><img src='/XJSCRM/common/images/toolbar/pritask.gif' border='0'/></a>&nbsp;&nbsp;&nbsp;"
					if(viewurl.trim()!=""){
						strs2+="<a href='"+splitstr2[3]+"&amp;tokenid="+splitstr2[4]+"&amp;flowname="+flowname+"' target='_blank' title='<bean:message key="toolbar.viewflow"/>'><img src='/XJSCRM/common/images/toolbar/viewvol.gif' border='0'/></a></td></tr>";
					}
				}
			}else if(flowd!="" && topics==""){//模块不为空,主题为空
				if(flowd==flowid){
					ks=ks+1;
					var viewurl=splitstr2[3];
					strs2+="<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+splitstr2[1]+"' target='_self' title='"+splitstr2[2]+"'>"+splitstr2[0]+"</a>&nbsp;&nbsp;&nbsp;" +
						"<a href='"+splitstr2[1]+"' target='_self' title='我的任务，立即处理！'><img src='/XJSCRM/common/images/toolbar/pritask.gif' border='0'/></a>&nbsp;&nbsp;&nbsp;"
					if(viewurl.trim()!=""){
						strs2+="<a href='"+splitstr2[3]+"&amp;tokenid="+splitstr2[4]+"&amp;flowname="+flowname+"' target='_blank' title='<bean:message key="toolbar.viewflow"/>'><img src='/XJSCRM/common/images/toolbar/viewvol.gif' border='0'/></a></td></tr>";
					}
				}
			}else if(flowd=="" && topics!=""){//模块为空,主题不为空
				if(splitstr2[0].indexOf(topics)>-1){
					var viewurl=splitstr2[3];
					strs2+="<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+splitstr2[1]+"' target='_self' title='"+splitstr2[2]+"'>"+splitstr2[0]+"</a>&nbsp;&nbsp;&nbsp;" +
						"<a href='"+splitstr2[1]+"' target='_self' title='我的任务，立即处理！'><img src='/XJSCRM/common/images/toolbar/pritask.gif' border='0'/></a>&nbsp;&nbsp;&nbsp;"
					if(viewurl.trim()!=""){
						strs2+="<a href='"+splitstr2[3]+"&amp;tokenid="+splitstr2[4]+"&amp;flowname="+flowname+"' target='_blank' title='<bean:message key="toolbar.viewflow"/>'><img src='/XJSCRM/common/images/toolbar/viewvol.gif' border='0'/></a></td></tr>";
					}
						ks=ks+1;
				}
			}else if(flowd=="" && topics==""){//模块为空,主题为空
				var viewurl=splitstr2[3];
				strs2+="<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+splitstr2[1]+"' target='_self' title='"+splitstr2[2]+"'>"+splitstr2[0]+"</a>&nbsp;&nbsp;&nbsp;" +
						"<a href='"+splitstr2[1]+"' target='_self' title='我的任务，立即处理！'><img src='/XJSCRM/common/images/toolbar/pritask.gif' border='0'/></a>&nbsp;&nbsp;&nbsp;"
					if(viewurl.trim()!=""){
						strs2+="<a href='"+splitstr2[3]+"&amp;tokenid="+splitstr2[4]+"&amp;flowname="+flowname+"' target='_blank' title='<bean:message key="toolbar.viewflow"/>'><img src='/XJSCRM/common/images/toolbar/viewvol.gif' border='0'/></a></td></tr>";
					}
				ks=qty;
			}
			
	  	}
	  	//显示汇总行
	  	strs3="<tr><td class='wordtd'><div align='left'>&nbsp;&nbsp;<a href='#'>"+flowname+"</a>:[<span class='STYLE5'>"+ks+"</span>]</div></td></tr>";
		if(strs2=="" || strs2.length==0){
			strs="";
		}else{
			strs=strs+strs3+strs2+"</table><br/>";
		}
	}
	return strs;
}
/*
onKeydUp=function(){
	if(event.keyCode==13){
		querys();
	}
}
function querys(){
	getTaskList();
}
document.onkeydown=onKeydUp;
*/
/***************初始化模块下拉列表************/
function updateOperationIDList()
{
	clearOperationList("flowid");
	var k=0;
	var receivers = document.getElementById("flowid");
	var listid = new Array();
	var listname = new Array();
	<logic:present name="flowList">
	<logic:iterate id="element" name="flowList" >
		 listid[k] = '<bean:write name="element" property="listid"/>';
	 	 listname[k] = '<bean:write name="element" property="listname"/>';
	 	 k++;
	</logic:iterate>
	</logic:present>
	var option = null;
	var len = listid.length;
	for(var i =0;i<len;i++)
	{
		option = document.createElement("OPTION");
		option.text = listname[i];
		option.value = listid[i];
		receivers.add(option);
	}
	if(len==0){
		option = document.createElement("OPTION");
		option.text = "请选择";
		option.value = "";
		receivers.add(option);
	}
}
function clearOperationList(changeList1)
{
	var receivers = document.getElementById(changeList1);
	while(receivers.childNodes.length>0)
	{
		receivers.removeChild(receivers.childNodes[0]);
	}
}
//检查,如果页面上没有代办工作,则将'查询'按钮变为不可用
function checkButtonDisable(){
	<logic:present name="Msg">
		document.getElementById("taskButton").disabled=true;
	</logic:present>
}
//初始化
updateOperationIDList();
checkButtonDisable();
</script>
</html>