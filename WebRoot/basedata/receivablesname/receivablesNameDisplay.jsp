<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="receivablesNameBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="ReceivablesName.recId"/>:</td>
    <td><bean:write name="receivablesNameBean" scope="request" property="recId"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ReceivablesName.recName"/>:</td>
    <td  colspan="3"><bean:write name="receivablesNameBean" scope="request" property="recName"/></td>
  </tr> 
  <tr>
    <td class="wordtd"><bean:message key="ReceivablesName.enabledflag"/>:</td>
    <td>
	<logic:match name="receivablesNameBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="receivablesNameBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="ReceivablesName.rem"/>:</td>
    <td colspan="3"><bean:write name="receivablesNameBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>