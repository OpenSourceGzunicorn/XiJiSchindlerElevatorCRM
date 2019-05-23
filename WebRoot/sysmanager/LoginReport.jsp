<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors/>
<br>

	<p align="left"><b><bean:message key="haduser.onlinecount"/>:<bean:write name="count"/></b></p>
    <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tableclass">
		<tr class="headerRow3" align="center" height="20">
			<td><bean:message key="haduser.userid"/></td>
			<td><bean:message key="haduser.username"/></td>
			<td><bean:message key="haduser.rolename"/></td>
			<td>所属部门</td>
			<td>所属分部</td>
			<td><bean:message key="haduser.logintime"/></td>
			<td><bean:message key="haduser.ipaddress"/></td>
			<td><bean:message key="haduser.sessionid"/></td>

		</tr>
	<logic:present name="userList">
      <logic:iterate id="element" name="userList">
		<tr class="evenRow3">
			<td align="left">
				<bean:write name="element" property="userID"/>
			</td>
			<td align="left">
				<bean:write name="element" property="userName"/>
			</td>
			<td align="left">
				<bean:write name="element" property="roleName"/>
			</td>
			<td align="left">
				<bean:write name="element" property="storageName"/>
			</td>
			<td align="left">
				<bean:write name="element" property="comName"/>
			</td>
			<td align="left">
				<bean:write name="element" property="loginDate"/> <bean:write name="element" property="loginTime"/>
			</td>
			<td align="left">
				<bean:write name="element" property="ipAddress"/>
			</td>
			<td align="left">
				<bean:write name="element" property="sessionID"/>
			</td>
		</tr>
      </logic:iterate>
    </logic:present>
    </table>