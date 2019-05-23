<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='ExtCSS'/>" />
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
<script>

var panals=new Array();
//go to appoint post
function gotoPost(did,po){
		if(panals[did].collapsed){
			panals[did].expand(true);
		}
		//Ext.expand.show();
		document.getElementById("hida").href=po;
		document.getElementById("hida").click();
}

var dbug=false;
//show debug msg
function debug(meth,par){
	if(dbug){
		var msg=document.getElementById("debugmsg");
		if(msg!=null){
			msg.innerHTML+=msg.innerHTML+"<br>"+meth+":"+par;
		}
	}
}
/////////////////////////////////////////////////////////////////////////////////

var showSpinner=function(){
	if(!Ext.Msg.isVisible()){
		Ext.Msg.wait('任务正在处理中，请耐心等候^_^', '提示',{text:"处理中...",value:0.8});
	}
} 
var closeSpinner=function(){
	if(Ext.Msg.isVisible()){
		Ext.Msg.hide();
	}
} 
var excSpinner=function(){
	if(Ext.Msg.isVisible()){
		Ext.Msg.hide();
	}
}
//set ajax process event
Ext.Ajax.on('beforerequest', this.showSpinner, this);
Ext.Ajax.on('requestcomplete', this.closeSpinner, this);
Ext.Ajax.on('requestexception ', this.excSpinner, this);

//go to web service get data
var toHandleTask=function(tid,ttype,fid){
	debug("toHandleTask",tid+"	"+ttype+"	"+fid);
	stepDownTimes();//更新周期
	var m='toHandleTask';
	if(ttype==3){
		m='getTaskDetailList';
	}
	var req=Ext.Ajax.request({
   		url: '/XJSCRM/myTaskOaSearchAction.do',
   		success: someFn,
   		failure: otherFn,
   		headers: {
       		'my-header': 'foo'
   		},
   		params: { foo: 'bar',taskid:tid,tasktype:ttype,method:m,ajax:'1',flowid:fid }
	});
		
}
//ajax success to do function
var someFn=function(req ){
	debug("someFn","");
	
	var xmlobj = req.responseXML;
	var msg = xmlobj.documentElement.selectNodes("msg");
	if(msg!=null && msg[0].getAttribute("code")!=null && msg[0].getAttribute("code")=="0"){
		var taskrow=xmlobj.getElementsByTagName("row");
		if(taskrow!=null){
			var tasklist=new Array();
			for(var i=0;i<taskrow.length;i++){
				var row=taskrow[i].childNodes;
				tasklist[i]=new Array(7);
				for(var j=0;j<row.length;j++){
					var col=row[j];
					if(row[j].getAttribute("name")!=null && row[j].getAttribute("name")=="taskid" ){
						tasklist[i][0]=row[j].getAttribute("value");
					}else if(row[j].getAttribute("name")!=null && row[j].getAttribute("name")=="flowid" ){
						tasklist[i][1]=row[j].getAttribute("value");
					}else if(row[j].getAttribute("name")!=null && row[j].getAttribute("name")=="taskop" ){
						tasklist[i][2]=row[j].getAttribute("value");
					}else if(row[j].getAttribute("name")!=null && row[j].getAttribute("name")=="taskrs" ){
						tasklist[i][3]=row[j].getAttribute("value");
					}else if(row[j].getAttribute("name")!=null && row[j].getAttribute("name")=="taskinfo" ){
						tasklist[i][4]=row[j].getAttribute("value");
					}else if(row[j].getAttribute("name")!=null && row[j].getAttribute("name")=="isshow" ){
						tasklist[i][5]=row[j].getAttribute("value");
					}else if(row[j].getAttribute("name")!=null && row[j].getAttribute("name")=="taskdetail" ){
						tasklist[i][6]=row[j].getAttribute("value");
					}
				}
			}
			//update info
			for(var k=0;k<tasklist.length;k++){
				handleTask(tasklist[k][0],tasklist[k][1],tasklist[k][2],tasklist[k][3],tasklist[k][4],tasklist[k][5],tasklist[k][6]);
			}
		}
	}else{
		//alert(msg[0].getAttribute("message"));
		Ext.Msg.alert('提示', msg[0].getAttribute("message"));
		
	}
}
//ajax not success to do function
var otherFn=function(response,options){
	if(Ext.Msg.isVisible()){
		Ext.Msg.hide();
	}
}
//show or hidden task detail
function showhid(tabobj,flag,index){
	debug("showhid",tabobj+"	"+flag+"	"+index);
	stepDownTimes();//更新周期
	for(var i=0;i<tabobj.children.length;i++){
		var tchild=tabobj.children[i];
		if(i==index){
			for(var j=0;j<tchild.children.length;j++){
				if(flag=="s"){
					if(tchild.children[j].style.display=="none"){
						tchild.children[j].style.display="";
					}
				}else{
					if(tchild.children[j].style.display==""){
						tchild.children[j].style.display="none";
					}
				}
			}
		}
	}	
}
//update result to page
function handleTask(tid,flow,top,trs,tinfo,isshow,tdetail){
	debug("handleTask",tid+"	"+flow+"	"+top+"	"+trs+"	"+tinfo+"	"+isshow+"	"+tdetail);
	if(trs=="topub"){
		var taskid=document.getElementById("task_id_"+tid);
		if(taskid!=null){
			var tab=taskid.parentNode.parentNode.parentNode.parentNode;
			var pub=tab.getElementsByTagName("tbody")[0];
			var row=taskid.parentNode.parentNode.rowIndex;
			
			var childi=taskid.parentNode.parentNode.cloneNode(true);
			var childi1=taskid.parentNode.parentNode.nextSibling.cloneNode(true);

			var di=childi.getElementsByTagName("div");
			var show=di[0].innerHTML;
			di[0].innerHTML=di[1].innerHTML;
			di[1].innerHTML=show;
						
			tab.deleteRow(row+1);
			tab.deleteRow(row);

			pub.appendChild(childi);
			pub.appendChild(childi1);

			//pub.insertBefore(childi);
			//pub.insertBefore(childi1);
			//更新任务数据
			var pubqty0=document.getElementById("M_Flow_"+flow+"_Title_PubQty").innerHTML;
			var priqty0=document.getElementById("M_Flow_"+flow+"_Title_PriQty").innerHTML;

			priqty0=parseInt(priqty0)-1>=0?parseInt(priqty0)-1:0;
			pubqty0=parseInt(pubqty0)+1;
			
			document.getElementById("M_Flow_"+flow+"_Title_PubQty").innerHTML=pubqty0;
			document.getElementById("M_Flow_"+flow+"_Title_PriQty").innerHTML=priqty0;
			document.getElementById("M_Flow_"+flow+"_Title_PubQty_1").innerHTML=pubqty0;
			document.getElementById("M_Flow_"+flow+"_Title_PriQty_1").innerHTML=priqty0;

		}
	}else if(trs=="topri"){
		var taskid=document.getElementById("task_id_"+tid);
		if(taskid!=null){
			var tab=taskid.parentNode.parentNode.parentNode.parentNode;
			var pri=tab.getElementsByTagName("thead")[0];
			var row=taskid.parentNode.parentNode.rowIndex;

			var childi=taskid.parentNode.parentNode.cloneNode(true);
			var childi1=taskid.parentNode.parentNode.nextSibling.cloneNode(true);

			var di=childi.getElementsByTagName("div");
			var show=di[0].innerHTML;
			di[0].innerHTML=di[1].innerHTML;
			di[1].innerHTML=show;
						
			tab.deleteRow(row+1);
			tab.deleteRow(row);

			pri.appendChild(childi);
			pri.appendChild(childi1);
			
			//pri.insertBefore(childi);
			//pri.insertBefore(childi1);
			//更新任务数据
			var pubqty0=document.getElementById("M_Flow_"+flow+"_Title_PubQty").innerHTML;
			var priqty0=document.getElementById("M_Flow_"+flow+"_Title_PriQty").innerHTML;

			pubqty0=parseInt(pubqty0)-1>=0?parseInt(pubqty0)-1:0;
			priqty0=parseInt(priqty0)+1;
			
			document.getElementById("M_Flow_"+flow+"_Title_PubQty").innerHTML=pubqty0;
			document.getElementById("M_Flow_"+flow+"_Title_PriQty").innerHTML=priqty0;
			document.getElementById("M_Flow_"+flow+"_Title_PubQty_1").innerHTML=pubqty0;
			document.getElementById("M_Flow_"+flow+"_Title_PriQty_1").innerHTML=priqty0;

		}
	}else if(trs=="todel"){
		var taskid=document.getElementById("task_id_"+tid);
		if(taskid!=null){
			var tab=taskid.parentNode.parentNode.parentNode.parentNode;
			var row=taskid.parentNode.parentNode.rowIndex;
			if(isshow!=null && isshow=="Y"){
				//alert("tinfo="+tinfo);
				Ext.Msg.alert('提示', tinfo);
			}
			tab.deleteRow(row+1);
			tab.deleteRow(row);
			if(top=="2"){
				//更新任务数据
				//var pubqty0=document.getElementById("M_Flow_"+flow+"_Title_PubQty").innerHTML;
				var priqty0=document.getElementById("M_Flow_"+flow+"_Title_PriQty").innerHTML;
				var allqty0=document.getElementById("M_Flow_"+flow+"_Title_AllQty").innerHTML;
				
				priqty0=parseInt(priqty0)-1>=0?parseInt(priqty0)-1:0;
				allqty0=parseInt(allqty0)-1>=0?parseInt(allqty0)-1:0;
				
				document.getElementById("M_Flow_"+flow+"_Title_PriQty").innerHTML=priqty0;
				document.getElementById("M_Flow_"+flow+"_Title_PriQty_1").innerHTML=priqty0;
				document.getElementById("M_Flow_"+flow+"_Title_AllQty").innerHTML=allqty0;
				
			}else if(top=="1"){
				//更新任务数据
				var pubqty0=document.getElementById("M_Flow_"+flow+"_Title_PubQty").innerHTML;
				var allqty0=document.getElementById("M_Flow_"+flow+"_Title_AllQty").innerHTML;
	
				pubqty0=parseInt(pubqty0)-1>=0?parseInt(pubqty0)-1:0;
				allqty0=parseInt(allqty0)-1>=0?parseInt(allqty0)-1:0;
				
				document.getElementById("M_Flow_"+flow+"_Title_PubQty").innerHTML=pubqty0;
				document.getElementById("M_Flow_"+flow+"_Title_PubQty_1").innerHTML=pubqty0;
				document.getElementById("M_Flow_"+flow+"_Title_AllQty").innerHTML=allqty0;
			}
		}
	}else if(trs=="todetail"){
		var taskdetail=document.getElementById("task_detail_"+tid);
		if(taskdetail!=null){
			taskdetail.innerHTML=tdetail;
		}
	}
}

var times=0;
//reflash page 
var reflash=function(){
	debug("reflash","");
	if(times>=2){
		window.location.href="<html:rewrite page='/myTaskOaSearchAction.do'/>?method=toMyTask";
		/*var au=document.getElementById("autoReflash");
		if(au!=null){
			au.click();
		}*/
	}else{
		times++;
	}
}
function stepDownTimes(){
	times=times-1>0?times-1:0;
}
window.setInterval(reflash,60*60*1000);//单位是毫秒
//window.setInterval(reflash,15*1000);


</script>

<body>
<%--a href="http://localhost:8080<html:rewrite page='/myTaskOaSearchAction.do'/>?method=toMyTask" id="autoReflash" target="_self"></a--%>
<div align="center">
	<logic:present name="TaskList">
	<logic:iterate name="TaskList" id="item">
	<div id="M_Flow_<bean:write name="item" property="flowid"/>_Show"></div>
	</logic:iterate>
	</logic:present>
	<logic:notPresent name="TaskList">
	<div id="NoTaskList"></div>
	</logic:notPresent>
</div>	
<div id="tempList" style="display:none">
<logic:present name="TaskList">
<logic:iterate name="TaskList" id="item">
	<div id="M_Flow_<bean:write name='item' property='flowid'/>_Title">
		<table style="font-size:12px" align="left">
			<tr><td width="50%">流程：<bean:write name='item' property='flowname'/></td>
				<td nowrap>总数：[<span id="M_Flow_<bean:write name='item' property='flowid'/>_Title_AllQty" ><bean:write name='item' property='allqty'/></span>]&nbsp;&nbsp;</td>
				<td nowrap>个人：[<span id="M_Flow_<bean:write name='item' property='flowid'/>_Title_PriQty" class="STYLE5"><bean:write name='item' property='priqty'/></span>]&nbsp;&nbsp;</td>
				<td nowrap>共享：[<span id="M_Flow_<bean:write name='item' property='flowid'/>_Title_PubQty" class="STYLE8"><bean:write name='item' property='pubqty'/></span>]&nbsp;&nbsp;</td>
			</tr>
		</table>
	</div>
	<div id="M_Flow_<bean:write name='item' property='flowid'/>">
		<table width="100%">
			<tr>
				<td>
				<div id="M_Flow_<bean:write name='item' property='flowid'/>_Task">					
						<table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" class="tb" id="M_Flow_<bean:write name='item' property='flowid'/>_Table" name="M_Flow_<bean:write name='item' property='flowid'/>_Table">
						 <thead>
						 <tr style="display:block">
							<td width="45%" class="wordtd"><div align="center">任务名称</div></td>
							<td width="30%" class="wordtd"><div align="center">创建时间</div></td>
							<td width="25%" class="wordtd"><div align="center">操　　作</div></td>
						  </tr>
						  <tr style="display:block">
							<td colspan="3"><div style=" width:100%;background-color:#D3E0D7 ">个人任务&nbsp;:&nbsp;[<span class="STYLE5" id="M_Flow_<bean:write name='item' property='flowid'/>_Title_PriQty_1"><bean:write name='item' property='priqty'/></span>]
							<span ><a href="javascript:showhid(M_Flow_<bean:write name='item' property='flowid'/>_Table,'s',0)">展开明细</a>&nbsp;/&nbsp;<a href="javascript:showhid(M_Flow_<bean:write name='item' property='flowid'/>_Table,'h',0)">隐藏明细</a></span></div>
							</td>
						  </tr>
						 
						  <logic:iterate name="item" property="privatelist" id="pri">
						  <tr style="display:block">
							<td nowrap><input type="hidden" name="taskid_" id="task_id_<bean:write name='pri' property='taskid'/>" value="<bean:write name='pri' property='taskid'/>">&nbsp;&nbsp;<bean:write name='pri' property='taskname2'/></td>
							<td nowrap><bean:write name='pri' property='createdate'/></td>
							<td nowrap>
								<div style="display:compact">
									&nbsp;<a href="<bean:write name='pri' property='priurl'/>" target="Approve" title="我的任务，立即处理！"><img src="<html:rewrite page='/common/images/toolbar/pritask.gif'/>" border="0"/></a>&nbsp;
									&nbsp;<a href="javascript:toHandleTask(<bean:write name='pri' property='taskid'/>,2,'<bean:write name='item' property='flowid'/>')" target="_self" title="撤消个人任务，退回共享任务！"><img src="<html:rewrite page='/common/images/toolbar/disk_default.gif'/>" border="0"/></a>&nbsp;
									&nbsp;<a href="<bean:write name='pri' property='processurl'/>" target="_blank" title="<bean:message key="toolbar.viewflow"/>"><img src="<html:rewrite page='/common/images/toolbar/viewvol.gif'/>" border="0"/></a>&nbsp;
								</div>
								<div style="display:none">
									&nbsp;<a href="javascript:toHandleTask(<bean:write name='pri' property='taskid'/>,1,'<bean:write name='item' property='flowid'/>')" title="共享任务，我来处理！"><img src="<html:rewrite page='/common/images/toolbar/pubtask.gif'/>" border="0"/></a>&nbsp;
									&nbsp;<a href="<bean:write name='pri' property='processurl'/>" target="_blank" title="<bean:message key="toolbar.viewflow"/>"><img src="<html:rewrite page='/common/images/toolbar/viewvol.gif'/>" border="0"/></a>&nbsp;
								</div>
							</td>
						  </tr>
						  <tr style="display:none">
						  	<td colspan="3"><div id="task_detail_<bean:write name='pri' property='taskid'/>" style="display:table; height:50px; width:100%; overflow:auto;background-color:#E6F2FF" align="left"></div><td>
						  </tr>
						  </logic:iterate>
						  
						 </thead>
						 <tbody>
						  <tr style="display:block">
							<td colspan="3"><div style=" width:100%; background-color:#D3E0D7">共享任务(我来处理)&nbsp;:&nbsp;[<span class="STYLE8" id="M_Flow_<bean:write name='item' property='flowid'/>_Title_PubQty_1"><bean:write name='item' property='pubqty'/></span>]
							<span ><a href="javascript:showhid(M_Flow_<bean:write name='item' property='flowid'/>_Table,'s',1)">展开明细</a>&nbsp;/&nbsp;<a href="javascript:showhid(M_Flow_<bean:write name='item' property='flowid'/>_Table,'h',1)">隐藏明细</a></span></div>
							</td>
						  </tr>
						  
						  <logic:iterate name="item" property="publiclist" id="pub">
						  <tr style="display:block">
							<td width="45%" nowrap td><input type="hidden" name="taskid_" id="task_id_<bean:write name='pub' property='taskid'/>" value="<bean:write name='pub' property='taskid'/>">&nbsp;&nbsp;<bean:write name='pub' property='taskname2'/></td>
							<td width="30%" nowrap td><bean:write name='pub' property='createdate'/></td>
							<td width="25%" nowrap td>
								<div style="display:compact">
									&nbsp;<a href="javascript:toHandleTask(<bean:write name='pub' property='taskid'/>,1,'<bean:write name='item' property='flowid'/>')" title="共享任务，我来处理！"><img src="<html:rewrite page='/common/images/toolbar/pubtask.gif'/>" border="0"/></a>&nbsp;
									&nbsp;<a href="<bean:write name='pub' property='processurl'/>" target="_blank" title="<bean:message key="toolbar.viewflow"/>"><img src="<html:rewrite page='/common/images/toolbar/viewvol.gif'/>" border="0"/></a>&nbsp;
								</div>
								<div style="display:none">
									&nbsp;<a href="<bean:write name='pub' property='priurl'/>" target="Approve" title="我的任务，立即处理！"><img src="<html:rewrite page='/common/images/toolbar/pritask.gif'/>" border="0"/></a>&nbsp;
									&nbsp;<a href="javascript:toHandleTask(<bean:write name='pub' property='taskid'/>,2,'<bean:write name='item' property='flowid'/>')" target="_self" title="撤消个人任务，退回共享任务！"><img src="<html:rewrite page='/common/images/toolbar/disk_default.gif'/>" border="0"/></a>&nbsp;
									&nbsp;<a href="<bean:write name='pub' property='processurl'/>" target="_blank" title="<bean:message key="toolbar.viewflow"/>"><img src="<html:rewrite page='/common/images/toolbar/viewvol.gif'/>" border="0"/></a>&nbsp;
								</div>
							</td>
						  </tr>
						  <tr style="display:none">
						  	<td colspan="3"><div id="task_detail_<bean:write name='pub' property='taskid'/>" style="display:table; height:50px; width:100%; overflow:auto;background-color:#E6F2FF" align="left">
						  						
						  	</div><td>
						  </tr>
						  </logic:iterate>
						  
						  </tbody>
						</table>
					</div>
				<div style="height:3px">&nbsp;</div>
			</td></td></table>
		</div>
</logic:iterate>
</logic:present>
</div>
<div id="debugmsg"></div>
<script>
//create class todolist
var ext=Ext.onReady(function(){
	var i=0;
	<logic:present name="TaskList">
	<logic:iterate name="TaskList" id="item2">

    panals[panals.length] = new Ext.Panel({
        title: document.getElementById("M_Flow_<bean:write name='item2' property='flowid'/>_Title").innerHTML,
        titleCollapse : true,//点击 title 其他地方也可以缩放
        collapsed : true,//false 不展开
        collapsible:true,
		autoScroll:true,
		//collapsed :true,
        renderTo: 'M_Flow_<bean:write name="item2" property="flowid"/>_Show',
        html: document.getElementById('M_Flow_<bean:write name="item2" property="flowid"/>').innerHTML
    });
	</logic:iterate>
	</logic:present>
	document.getElementById('tempList').innerHTML="";
	<logic:notPresent name="TaskList">
		new Ext.Panel({
        title: '无待办任务',
        titleCollapse : true,//点击 title 其他地方也可以缩放
        collapsible:true,
		autoScroll:true,
		collapsed :true,
        renderTo: 'NoTaskList',
        html: ''
    	});
	</logic:notPresent>
});

//fetch task detail
var getDetail=function(){
	var taskid="";
	<logic:present name="Taskes">
		taskid='<bean:write name="Taskes"/>';
	</logic:present>
	
	if(taskid!=null && taskid!=""){
		toHandleTask(taskid,3,'');
	}
}
window.setTimeout(getDetail,800);
</script>
</body>
</html>