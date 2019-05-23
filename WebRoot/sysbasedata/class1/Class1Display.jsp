<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="class1Bean">
<table width="100%" class="tb" border="0" cellpadding="0" cellspacing="0">
  
  <tr>
    <td width="20%" class="wordtd"><bean:message key="customer.placeid"/>:</td>
    <td width="80%" class="inputtd"><bean:write name="class1Bean" scope="request" property="class1id"/></td>
  </tr>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="customer.placename"/>:</td>
    <td width="80%" class="inputtd"><bean:write name="class1Bean" scope="request" property="class1name"/></td>
  </tr>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="customer.placelevel"/>:</td>
    <td width="80%" class="inputtd"><bean:write name="class1Bean" scope="request" property="levelid"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.isVisit"/>:</td>
    <td class="inputtd">
	<logic:match name="class1Bean" property="r1" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="class1Bean" property="r1" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.placestart"/>:</td>
    <td class="inputtd">
	<logic:match name="class1Bean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="class1Bean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.placereml"/>:</td>
    <td class="inputtd"><bean:write name="class1Bean" scope="request" property="rem1"/></td>
  </tr>
</table>
</logic:present>