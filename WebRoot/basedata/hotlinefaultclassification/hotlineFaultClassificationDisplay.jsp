<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="hotlineFaultClassificationBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="hotlinefaultclassification.hfcId"/>:</td>
    <td><bean:write name="hotlineFaultClassificationBean" scope="request" property="hfcId"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="hotlinefaultclassification.hfcName"/>:</td>
    <td><bean:write name="hotlineFaultClassificationBean" scope="request" property="hfcName"/></td>
  </tr> 
  <tr>
    <td class="wordtd"><bean:message key="hotlinefaultclassification.enabledFlag"/>:</td>
    <td>
	<logic:match name="hotlineFaultClassificationBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="hotlineFaultClassificationBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="hotlinefaultclassification.rem"/>:</td>
    <td><bean:write name="hotlineFaultClassificationBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>