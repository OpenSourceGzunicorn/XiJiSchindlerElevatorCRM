<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="invoiceTypeBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="InvoiceType.inTypeId"/>:</td>
    <td><bean:write name="invoiceTypeBean" scope="request" property="inTypeId"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="InvoiceType.inTypeName"/>:</td>
    <td  colspan="3"><bean:write name="invoiceTypeBean" scope="request" property="inTypeName"/></td>
  </tr> 
  <tr>
    <td class="wordtd"><bean:message key="InvoiceType.enabledflag"/>:</td>
    <td>
	<logic:match name="invoiceTypeBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="invoiceTypeBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="InvoiceType.rem"/>:</td>
    <td colspan="3"><bean:write name="invoiceTypeBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>