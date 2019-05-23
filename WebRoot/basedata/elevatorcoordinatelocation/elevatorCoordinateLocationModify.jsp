<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/elevatorCoordinateLocationAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb" >
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.elevatorNo"/>:</td>
    <td>
      <bean:write name="elevatorCoordinateLocationBean" property="elevatorNo"/>
      <html:hidden name="elevatorCoordinateLocationBean" property="elevatorNo"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.elevatorLocation"/>:</td>
    <td><bean:write name="elevatorCoordinateLocationBean" property="elevatorLocation" /></td>
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.beginLongitude"/>:</td>
    <td><bean:write name="elevatorCoordinateLocationBean" property="beginLongitude"/></td>
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.endLongitude"/>:</td>
    <td><bean:write name="elevatorCoordinateLocationBean" property="endLongitude"/></td>
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.beginDimension"/>:</td>
    <td><bean:write name="elevatorCoordinateLocationBean" property="beginDimension"/></td>
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.endDimension"/>:</td>
    <td><bean:write name="elevatorCoordinateLocationBean" property="endDimension" /></td>
  </tr>
  <tr>
    <td class="wordtd">项目名称及楼栋号:</td>
    <td><html:text name="elevatorCoordinateLocationBean" property="rem" styleId="rem" size="60" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
	<td class="wordtd">录入人:</td>
    <td ><bean:write name="elevatorCoordinateLocationBean" scope="request" property="operId"/></td>
  </tr>
  <tr>
	<td class="wordtd">录入日期:</td>
    <td ><bean:write name="elevatorCoordinateLocationBean" scope="request" property="operDate"/></td>
  </tr>
</table>
</html:form>


