<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="elevatorCoordinateLocationBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.elevatorNo"/>:</td>
    <td><bean:write name="elevatorCoordinateLocationBean" scope="request" property="elevatorNo"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.elevatorLocation"/>:</td>
    <td  ><bean:write name="elevatorCoordinateLocationBean" scope="request" property="elevatorLocation"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.beginLongitude"/>:</td>
    <td  ><bean:write name="elevatorCoordinateLocationBean" scope="request" property="beginLongitude"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.endLongitude"/>:</td>
    <td  ><bean:write name="elevatorCoordinateLocationBean" scope="request" property="endLongitude"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.beginDimension"/>:</td>
    <td  ><bean:write name="elevatorCoordinateLocationBean" scope="request" property="beginDimension"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.endDimension"/>:</td>
    <td  ><bean:write name="elevatorCoordinateLocationBean" scope="request" property="endDimension"/></td>
  </tr> 
  <%-- 
  <tr>
    <td class="wordtd"><bean:message key="ElevatorCoordinateLocation.enabledflag"/>:</td>
    <td>
	<logic:match name="elevatorCoordinateLocationBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="elevatorCoordinateLocationBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  --%>
  <tr>
	<td class="wordtd">项目名称及楼栋号:</td>
    <td ><bean:write name="elevatorCoordinateLocationBean" scope="request" property="rem"/></td>
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
</logic:present>