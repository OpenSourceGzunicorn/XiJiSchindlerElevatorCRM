<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="hotlineFaultTypeBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="hotlinefaulttype.hftId"/>:</td>
    <td><bean:write name="hotlineFaultTypeBean" scope="request" property="hftId"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="hotlinefaulttype.hftDesc"/>:</td>
    <td  colspan="3"><bean:write name="hotlineFaultTypeBean" scope="request" property="hftDesc"/></td>
  </tr> 
  <tr>
    <td class="wordtd"><bean:message key="hotlinefaulttype.enabledflag"/>:</td>
    <td>
	<logic:match name="hotlineFaultTypeBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="hotlineFaultTypeBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="hotlinefaulttype.rem"/>:</td>
    <td colspan="3"><bean:write name="hotlineFaultTypeBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>