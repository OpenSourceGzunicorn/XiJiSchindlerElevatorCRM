<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@ taglib uri="/WEB-INF/tld/jbpm.tld" prefix="jbpm" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title><bean:write name="FlowName"/>--<bean:message key="web.viewflow"/></title>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
</head>
<body>
<%
long tokenid=Long.parseLong(request.getParameter("tokenid"));
//System.out.println("tokenid="+tokenid);
if(tokenid!=-1){
 %>
<%--logic:present name="ViewPower"--%>
	<%--logic:equal name="ViewPower" value="Y">
		<jbpm:processimageToken token="<%=tokenid %>"/>
	</logic:equal>
	<logic:notEqual name="ViewPower" value="Y">
		you had not enough power!!!
	</logic:notEqual--%>
<%--/logic:present--%>
<br/>
<table width="95%" border="0" align="center" cellpadding="2" cellspacing="0" class="tb">
<tr>
	<td width="15%" class="wordtd">流程名称：</td>
	<td>
	<div align="center"><bean:write name="FlowName"/></div>
</td></tr>
<tr>
	<td class="wordtd">审批流程：</td>
	<td>
	<div style="width:100%; height:500px; overflow:auto"><jbpm:processimageToken token="<%=tokenid %>"/></div>
</td></tr>

<logic:present name="ActorList">

　<logic:iterate name="ActorList" id="item">
  <tr>
    <td class="wordtd">审批环节：</td>
    <td><bean:write name="item" property="taskname"/></td>    
  </tr>
  <tr>  
    <td  class="wordtd">当前处理人：</td>
    <td ><logic:iterate name="item" property="li0" id="item0">
    		<bean:write name="item0" property="username"/>(<bean:write name="item0" property="userid"/>)&nbsp;,&nbsp;
    	</logic:iterate>&nbsp;
    </td>
  </tr>
  <tr>
    <td  class="wordtd">候选人：</td>
    <td ><logic:iterate name="item" property="li1" id="item1">
    		<bean:write name="item1" property="username"/>(<bean:write name="item1" property="userid"/>)&nbsp;,&nbsp;
    	</logic:iterate>
    	&nbsp;
    </td>
  </tr>
  <tr>  
    <td colspan="2">&nbsp;</td>
  </tr>
  </logic:iterate>

</logic:present> 

</table>
<%}else{%>
<div height="200px">&nbsp;</div>
<div align="center">抱歉！找不到相关的流程信息 </div>
<%}%>
</body>
</html>