



<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/TreeShow.tld" prefix="TreeShow" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='calendarJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='AJAX'/>"></script>
<html:html locale="true">
<html:base/>
<head>
<title><bean:message key="app.title"/>
</title>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='dTreeCSS'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='publicCSS'/>">
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='queryCSS'/>">
<SCRIPT LANGUAGE=javascript src="<html:rewrite forward='dTreeJS'/>"></SCRIPT>

<SCRIPT LANGUAGE=javascript>


function hidden(){
  window.parent.main.cols="15,0,*,15";
}
function hidden1(){
  //alert("hi");
  window.parent.rmain.rows="20,0,*";
}

//window.attachEvent("onload",window_onload);

</SCRIPT>
<style>
.tree_body{border-bottom:black 1px solid;}
.tree_header{cursor:hand;width:100%;height:18;padding-top:2px;background :#b8c4f4;BORDER: #646c94 1px solid;}
.node{background-color:white;}
.tree_div{padding-left:8px;}
</style>
</head>
<body leftmargin="0" topmargin="0" class="tree_body">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
  <td>
    <div class="tree_header" align="center">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
     <tr>
      <td width="100%">
      <div width="100%" align="center" onClick="javascript:hidden1();">
		   展开/闭合↑
      </div>
      </td>
     
     </tr>
    </table>
    </div>
    <br>
  </td>
  </tr>
  
  <tr>
  <td>
  
   <div id="divdutylist" name="divdutylist">
    <div align="center"> 
    <table width="90%" border="0" cellspacing="0" cellpadding="0">
    <tr><td width="50%" valign="top">
    <table width="85%">
    <tr bgcolor="#B8C4F4">
      <td nowrap colspan="4" align="center"><b>任务列表</b></td>
    </tr>
    <tr>
      <td nowrap><div align="center">序号</div></td>
      <td nowrap><div align="center">任务名称</div></td>
      <td nowrap><div align="center">任务数量(个)</div></td>
      <td nowrap><div align="center">位置</div></td>
    </tr>
  
    <%int i=1;%>
    <logic:iterate id="duty" name="dutyList">
    
    <tr bgcolor="#FFFFFF" class="title_h3">
    <td nowrap bgcolor="#FFFFFF"><div align="center">&nbsp;<%=i%></div></td>
    <td nowrap bgcolor="#FFFFFF"><div align="center"><bean:write name="duty" property="nodename"/></div></td>
    <td nowrap bgcolor="#FFFFFF"><div align="center"><bean:write name="duty" property="dutycount"/></div></td>
    <td nowrap bgcolor="#FFFFFF"><div align="center"><a href="<bean:write name="duty" property="nodeurl"/>" target="rightFrame">跳转</a></div></td>
    </tr>	
    
    <%i++;%>
    </logic:iterate>
  </table>
  </td>
  
  <td width="40%"  valign="top">
  <table width="80%">
  <tr bgcolor="#B8C4F4">
  	  <td nowrap align="center" colspan="2"><b>信息发布</b></td>
  </tr>
  <tr>
  <td nowrap bgcolor="#FFFFFF">
  <MARQUEE direction=up height=100 onmouseout=this.start() onmouseover=this.stop() scrollAmount=1 scrollDelay=100 width="360">
  <table width="100%">
  <%int k=1;%>
  <logic:iterate id="message" name="messageList">
  <tr>
    <td nowrap><div align="left">&nbsp;<%=k%></div></td>
    <td nowrap title="<bean:write name="message" property="promulgatetitle"/>(<bean:write name="message" property="promulgatedate"/>)"><div align="left"><a href="../dutysearchAction.do?method=toDisplayRecord&messid=<bean:write name="message" property="billno"/>" target="rightFrame"><bean:write name="message" property="promulgatetitle"/>(<bean:write name="message" property="promulgatedate"/>)</div></td>
  </tr>	
  <%k++;%>
  </logic:iterate>
  
  </table>
  </MARQUEE>
  </td>
  </tr>
  </table>

  
  </div>
  </div>
  </td>
  </tr>
</table> 
</body>

</html:html>

<script>

function getDetailInfo()
{
	    var detailRequest = new AjaxRequest();

		var url = "<html:rewrite page='/dutysearchAction.do'/>?method=toGetDetailMehtod";
		
		detailRequest.url = url;
		detailRequest.mehod="POST";
		detailRequest.onComplete = setDetailInfo;
		detailRequest.process();

}

setDetailInfo = function(req)
{

	var duty=req.responseText;
	//alert("duty="+duty);
	var di=document.getElementById("divdutylist");
	//alert(di.id);
	di.innerHTML=duty;
}
//var time = 60*60*1000;
var timename=setInterval("getDetailInfo();",60*60*1000);
</script>