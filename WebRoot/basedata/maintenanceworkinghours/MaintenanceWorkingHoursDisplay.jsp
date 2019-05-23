<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="maintenanceWorkingHoursBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td width="20%" class="wordtd"><bean:message key="maintenanceworkinghours.elevatorType"/>:</td>
    <td width="80%" class="inputtd"><bean:write name="maintenanceWorkingHoursBean" scope="request" property="r1"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.floor"/>:</td>
    <td class="inputtd"><bean:write name="maintenanceWorkingHoursBean" scope="request" property="id.floor"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.halfMonth"/>:</td>
    <td class="inputtd"><bean:write name="maintenanceWorkingHoursBean" scope="request" property="halfMonth"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.quarter"/>:</td>
    <td class="inputtd"><bean:write name="maintenanceWorkingHoursBean" scope="request" property="quarter"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.halfYear"/>:</td>
    <td class="inputtd"><bean:write name="maintenanceWorkingHoursBean" scope="request" property="halfYear"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.yearDegree"/>:</td>
    <td class="inputtd"><bean:write name="maintenanceWorkingHoursBean" scope="request" property="yearDegree"/></td>
  </tr>
  
  <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.enabledflag"/>:</td>
    <td class="inputtd">
	<logic:match name="maintenanceWorkingHoursBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="maintenanceWorkingHoursBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.rem"/>:</td>
    <td class="inputtd"><bean:write name="maintenanceWorkingHoursBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>