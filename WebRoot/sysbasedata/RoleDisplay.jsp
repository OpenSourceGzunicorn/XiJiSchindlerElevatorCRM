<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="roleBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td width="20%" class="wordtd"><bean:message key="role.roleid"/>:</td>
    <td width="80%" class="inputtd"><bean:write name="roleBean" scope="request" property="roleid"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="role.rolename"/>:</td>
    <td class="inputtd"><bean:write name="roleBean" scope="request" property="rolename"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="role.moduleid"/>:</td>
    <td class="inputtd"><bean:write name="roleBean" scope="request" property="moduleid"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="role.allowflag"/>:</td>
    <td class="inputtd"><bean:write name="roleBean" scope="request" property="allowflag"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="role.enabledflag"/>:</td>
    <td class="inputtd">
	<logic:match name="roleBean" property="enabledflag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="roleBean" property="enabledflag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="role.rem1"/>:</td>
    <td class="inputtd"><bean:write name="roleBean" scope="request" property="rem1"/></td>
  </tr>
</table>
</logic:present>