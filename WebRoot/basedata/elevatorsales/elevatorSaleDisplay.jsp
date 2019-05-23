<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="elevatorSaleBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.elevatorNo"/>:</td>
    <td>
      <bean:write name="elevatorSaleBean" scope="request" property="elevatorNo"/>
      <html:hidden property="id" value='${elevatorSaleBean.elevatorNo}'/>
    </td>
    <td class="wordtd"><bean:message key="elevatorSale.elevatorType"/>:</td>
    <td>
      <logic:match name="elevatorSaleBean" property="elevatorType" value="T">直梯</logic:match>
      <logic:match name="elevatorSaleBean" property="elevatorType" value="F">扶梯</logic:match>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.salesContractNo"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="salesContractNo"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.salesContractName"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="salesContractName"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.salesContractType"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="salesContractType"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.inspectDate"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="inspectDate"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.elevatorParam"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="elevatorParam"/></td>
    <td class="wordtd">
      <bean:message key="elevatorSale.floor"/>/<bean:message key="elevatorSale.stage"/>/<bean:message key="elevatorSale.door"/>/:
    </td>
    <td>
      ${elevatorSaleBean.floor}/${elevatorSaleBean.stage}/${elevatorSaleBean.door}
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.weight"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="weight"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.speed"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="speed"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.seriesName"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="seriesName"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.high"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="high"/></td>    
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.operationName"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="operationName"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.operationPhone"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="operationPhone"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.department"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="department"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.dealer"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="dealer"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.useUnit"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="useUnit"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.deliveryAddress"/>:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="deliveryAddress"/></td> 
  </tr>
  
  <tr>
    <td class="wordtd">维保条款:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="maintenanceClause"/></td>
    <td class="wordtd">质保期:</td>
    <td><bean:write name="elevatorSaleBean" scope="request" property="warranty"/></td> 
  </tr>
  
  
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.enabledFlag"/>:</td>
    <td>
	<logic:match name="elevatorSaleBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="elevatorSaleBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
	<td class="wordtd">扶梯类型：</td>
	<logic:equal name="elevatorSaleBean" property="seriesId" value="FTP"><td>普通扶梯</td></logic:equal>
	<logic:equal name="elevatorSaleBean" property="seriesId" value="FTG"><td>公共扶梯</td></logic:equal>
	
  </tr>
  <tr>
    <td class="wordtd">是否在保:</td>
    <td>${elevatorSaleBean.r1 }</td>
    <td class="wordtd">所属省份:</td>
	<td>${elevatorSaleBean.r2 }</td>
  </tr>
  <tr>
    <td class="wordtd">所属市:</td>
    <td>${elevatorSaleBean.r3 }</td>
    <td class="wordtd">所属县/区:</td>
	<td>${elevatorSaleBean.r4 }</td>
  </tr>
  <tr><td class="wordtd">附件:</td>
	<td>
	<logic:notEmpty name="elevatorSaleBean" property="configuring">
		<a style="cursor:hand;text-decoration: underline;color: blue;" name="appendix" onclick="downloadFile('${elevatorSaleBean.configuring}','ElevatorSalesInfo')"><bean:message key="qualitycheckmanagement.check"/></a>
	</logic:notEmpty>
	</td>
	<td class="wordtd"></td>
	<td></td>
	
	</tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.rem"/>:</td>
    <td colspan="3"><bean:write name="elevatorSaleBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>