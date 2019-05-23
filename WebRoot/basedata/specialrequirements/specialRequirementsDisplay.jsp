<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="handoverElevatorSpecialClaimBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd">特殊要求代码:</td>
    <td><bean:write name="handoverElevatorSpecialClaimBean" scope="request" property="scId"/></td>
  </tr>
  <tr>
    <td class="wordtd">特殊要求名称:</td>
    <td><bean:write name="handoverElevatorSpecialClaimBean" scope="request" property="scName"/></td>
  </tr> 
  <tr>
    <td class="wordtd"><bean:message key="hotlinemotherboardtype.enabledFlag"/>:</td>
    <td>
	<logic:match name="handoverElevatorSpecialClaimBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="handoverElevatorSpecialClaimBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="hotlinemotherboardtype.rem"/>:</td>
    <td colspan="3"><bean:write name="handoverElevatorSpecialClaimBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>