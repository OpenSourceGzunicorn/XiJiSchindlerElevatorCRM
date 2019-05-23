<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='calendarCSS'/>">
<script language="javascript" src="<html:rewrite forward='calendarJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>

<br>
<html:errors/>
<html:form action="/WorkSpaceRoleConfigAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<input type="hidden" id="roleid" name="roleid" value="<bean:write name="role" property="roleid" />" />
<input type="hidden" id="rolename" name="rolename" value="<bean:write name="role" property="rolename" />" />
<%--主信息--%>
<logic:present name="role">
<table width="99%" border="0" align="center" cellpadding="2" cellspacing="0" class="tb">
  <tr>
    <td width="15%" class="wordtd">
    	<bean:message key="role.roleid" />：
    </td>
    <td width="35%">
	    <bean:write name="role" property="roleid"/>
    </td>
    <td width="15%" class="wordtd">
    	<bean:message key="role.name" />： 
    </td>
    <td width="35%">
	    <bean:write name="role" property="rolename"/>     
    </td>
  </tr>
</table>
</logic:present>


<div style="font-size:1px; height:1px;"></div>
<div style="font-size:1px; height:1px;"></div>
<div style="font-size:1px; height:1px;"></div>
<br>

<table width="99%" height="24" border="0" align="center" cellpadding="2" cellspacing="0" class="tb">

<tr>
  	<td width="15%" class="wordtd">工作区基本属性</td><td></td>
</tr>
</table>

<div style="font-size:1px; height:1px;"></div>
<div style="font-size:1px; height:1px;"></div>
<table width="99%" border="0" cellpadding="2" cellspacing="0" id="configList" name="configList" class="tb" align="center">
	<thead>
  		<tr>
  			<td class="oddRow5"><bean:message key="Track.jnlno"/></td> 
  			<td class="oddRow5"><bean:message key="contract.filetitle"/></td>
  		</tr>
  		<logic:present name="initlist">
		<logic:iterate name="initlist" id="element" >
  			 <tr>
	  			<td class="wordtd1_td1">
	  				<input name="wsid" id="wsid" value="<bean:write name="element" property="wsid"/>" class="inputTableData" readonly="true" style="width:150"/>	
	  			</td>
	  			<td class="wordtd1_td1">
	  				<input name="title" id="title" value="<bean:write name="element" property="title"/>" class="inputTableData" readonly="true" />	
	  			</td>
	  		</tr>
		</logic:iterate>
		</logic:present>
  	</thead>
</table>
</html:form>




