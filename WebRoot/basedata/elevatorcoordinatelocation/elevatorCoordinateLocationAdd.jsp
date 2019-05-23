<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<br>
<html:errors/>
<html:form action="/elevatorCoordinateLocationAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.elevatorNo"/>:</td>
    <td><html:text property="elevatorNo" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.elevatorLocation"/>:</td>
    <td><html:text property="elevatorLocation" size="60" styleClass="default_input"/><font color="red">*</font></td>
  </tr> 
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.beginLongitude"/>:</td>
    <td><html:text property="beginLongitude" styleClass="default_input"  onblur="onlyNumber(this)" onkeyup="checkNum(this)"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.endLongitude"/>:</td>
    <td><html:text property="endLongitude" styleClass="default_input"  onblur="onlyNumber(this)" onkeyup="checkNum(this)"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.beginDimension"/>:</td>
    <td><html:text property="beginDimension" styleClass="default_input"  onblur="onlyNumber(this)" onkeyup="checkNum(this)"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.endDimension"/>:</td>
    <td><html:text property="endDimension" styleClass="default_input" onblur="onlyNumber(this)" onkeyup="checkNum(this)"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.enabledflag"/>:</td>
    <td>
	  <html:radio property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.rem"/>:</td>
    <td><html:textarea property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="elevatorCoordinateLocationForm"/>