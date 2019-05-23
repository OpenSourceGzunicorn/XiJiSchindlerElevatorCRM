<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/maintenanceWorkingHoursAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${maintenanceWorkingHoursIdBean.elevatorType}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.elevatorType"/>:</td>
    <td>
      <bean:write name="maintenanceWorkingHoursBean" property="r1"/>
      <html:hidden name="maintenanceWorkingHoursIdBean" property="elevatorType"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="maintenanceworkinghours.floor"/>:</td>
    <td><bean:write name="maintenanceWorkingHoursIdBean" property="floor"/>
     <html:hidden name="maintenanceWorkingHoursIdBean" property="floor"/>
  
  </tr>
   <tr>
  <td class="wordtd"><bean:message key="maintenanceworkinghours.halfMonth"/>:</td>
    <td><html:text name="maintenanceWorkingHoursBean" property="halfMonth" size="20" styleClass="default_input" onblur="onlyNumber(this)" onkeypress="f_check_number2()"/><font color="red">*</font></td>
  
  </tr>
   <tr>
  <td class="wordtd"><bean:message key="maintenanceworkinghours.quarter"/>:</td>
    <td><html:text name="maintenanceWorkingHoursBean" property="quarter" size="20" styleClass="default_input" onblur="onlyNumber(this)" onkeypress="f_check_number2()"/><font color="red">*</font></td>
  
  </tr>
   <tr>
  <td class="wordtd"><bean:message key="maintenanceworkinghours.halfYear"/>:</td>
    <td><html:text name="maintenanceWorkingHoursBean" property="halfYear" size="20" styleClass="default_input" onblur="onlyNumber(this)" onkeypress="f_check_number2()"/><font color="red">*</font></td>
  
  </tr>
   <tr>
  <td class="wordtd"><bean:message key="maintenanceworkinghours.yearDegree"/>:</td>
    <td><html:text name="maintenanceWorkingHoursBean" property="yearDegree" size="20" styleClass="default_input" onblur="onlyNumber(this)" onkeypress="f_check_number2()"/><font color="red">*</font></td>
  
  </tr>
  
  <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.enabledflag"/>:</td>
    <td>
	  <html:radio name="maintenanceWorkingHoursBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="maintenanceWorkingHoursBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.rem"/>:</td>
    <td><html:textarea name="maintenanceWorkingHoursBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="maintenanceWorkingHoursForm"/>