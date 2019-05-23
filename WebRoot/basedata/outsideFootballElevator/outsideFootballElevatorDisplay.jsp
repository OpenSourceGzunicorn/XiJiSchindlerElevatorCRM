<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="outsideFootballElevatorBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.elevatorNo"/>:</td>
    <td>
      <bean:write name="outsideFootballElevatorBean" scope="request" property="elevatorNo"/>
      <html:hidden property="id" value='${outsideFootballElevatorBean.elevatorNo}'/>
    </td>
    <td class="wordtd"><bean:message key="elevatorSale.elevatorType"/>:</td>
    <td>
      <logic:match name="outsideFootballElevatorBean" property="elevatorType" value="T">直梯</logic:match>
      <logic:match name="outsideFootballElevatorBean" property="elevatorType" value="F">扶梯</logic:match>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.salesContractNo"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="salesContractNo"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.salesContractName"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="salesContractName"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.salesContractType"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="salesContractType"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.inspectDate"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="inspectDate"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.elevatorParam"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="elevatorParam"/></td>
    <td class="wordtd">
      <bean:message key="elevatorSale.floor"/>/<bean:message key="elevatorSale.stage"/>/<bean:message key="elevatorSale.door"/>/:
    </td>
    <td>
      ${outsideFootballElevatorBean.floor}/${outsideFootballElevatorBean.stage}/${outsideFootballElevatorBean.door}
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.weight"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="weight"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.speed"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="speed"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.seriesName"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="seriesName"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.high"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="high"/></td>    
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.operationName"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="operationName"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.operationPhone"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="operationPhone"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.department"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="department"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.dealer"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="dealer"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.useUnit"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="useUnit"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.deliveryAddress"/>:</td>
    <td><bean:write name="outsideFootballElevatorBean" scope="request" property="deliveryAddress"/></td> 
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.enabledFlag"/>:</td>
    <td>
	<logic:match name="outsideFootballElevatorBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="outsideFootballElevatorBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
	<td class="wordtd">扶梯类型：</td>
	<logic:equal name="outsideFootballElevatorBean" property="seriesId"value="FTP"><td>普通扶梯</td></logic:equal>
	<logic:equal name="outsideFootballElevatorBean" property="seriesId"value="FTG"><td>公共扶梯</td></logic:equal>
  </tr>
  <tr>
  <td class="wordtd">附件:</td>
	<td>
	<logic:notEmpty name="outsideFootballElevatorBean" property="configuring">
		<a style="cursor:hand;text-decoration: underline;color: blue;" name="appendix" onclick="downloadFile('${outsideFootballElevatorBean.configuring}','ElevatorSalesInfo')"><bean:message key="qualitycheckmanagement.check"/></a>
	</logic:notEmpty>
	</td>
	<td class="wordtd"></td>
	<td></td>
	
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.rem"/>:</td>
    <td colspan="3"><bean:write name="outsideFootballElevatorBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>