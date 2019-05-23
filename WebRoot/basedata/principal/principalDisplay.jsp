<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="principalBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="principal.principalId"/>:</td>
    <td><bean:write name="principalBean" scope="request" property="principalId"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="principal.principalName"/>:</td>
    <td><bean:write name="principalBean" scope="request" property="principalName"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="principal.phone"/>:</td>
    <td><bean:write name="principalBean" scope="request" property="phone"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="principal.custLevel"/>:</td>
    <td><bean:write name="principalBean" scope="request" property="custLevel"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="principal.enabledFlag"/>:</td>
    <td>
	<logic:match name="principalBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="principalBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="principal.rem"/>:</td>
    <td><bean:write name="principalBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>