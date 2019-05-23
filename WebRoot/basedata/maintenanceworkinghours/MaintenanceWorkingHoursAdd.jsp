<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<br>
<html:errors/>
<html:form action="/maintenanceWorkingHoursAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" class="tb">
  <tr>
    <td width="20%" class="wordtd"><bean:message key="maintenanceworkinghours.elevatorType"/>:</td>
    <td>
          <html:select property="elevatorType">
	          <html:option value=""><bean:message key="pageword.all"/></html:option> 
              <html:options collection="maintenanceWorkingHoursTypeList" property="id.pullid" labelProperty="pullname"/>
	      </html:select>
		<font color="red">*</font></td>
  </tr>
    
  <tr>
    <td width="20%" class="wordtd"><bean:message key="maintenanceworkinghours.floor"/>:</td>
   <td width="80%"><html:text property="floor" maxlength="255" size="30" styleClass="default_input" onblur="onlyNumber(this)" onkeypress="f_check_number2()"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.halfMonth"/>:</td>
   <td ><html:text property="halfMonth" maxlength="255" size="30" styleClass="default_input" onblur="onlyNumber(this)" onkeypress="f_check_number2()"/><font color="red">*</font></td>
    </tr>
    <tr>
     <td class="wordtd"><bean:message key="maintenanceworkinghours.quarter"/>:</td>
   <td ><html:text property="quarter" maxlength="255" size="30" styleClass="default_input" onblur="onlyNumber(this)" onkeypress="f_check_number2()"/><font color="red">*</font></td>
  </tr>
   <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.halfYear"/>:</td>
   <td><html:text property="halfYear" maxlength="255" size="30" styleClass="default_input" onblur="onlyNumber(this)" onkeypress="f_check_number2()"/><font color="red">*</font></td>
    </tr><tr>
     <td class="wordtd"><bean:message key="maintenanceworkinghours.yearDegree"/>:</td>
   <td><html:text property="yearDegree" maxlength="255" size="30" styleClass="default_input" onblur="onlyNumber(this)" onkeypress="f_check_number2()"/><font color="red">*</font></td>
  </tr>
  
  <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.enabledflag"/>:</td>
    <td class="inputtd"><html:radio property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="enabledFlag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="maintenanceworkinghours.rem"/>:</td>
    <td><html:textarea property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="maintenanceWorkingHoursForm"/>