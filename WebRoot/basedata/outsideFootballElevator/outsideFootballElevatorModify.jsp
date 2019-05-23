<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.hibernate.basedata.elevatorsales.ElevatorSalesInfo" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<br>
<html:errors/>
<html:form action="/outsideFootballElevatorAction.do?method=toUpdateRecord" enctype="multipart/form-data">
<html:hidden property="isreturn"/>
<html:hidden property="id" value='${outsideFootballElevatorBean.elevatorNo}'/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.elevatorNo"/>:</td>
    <td>${outsideFootballElevatorBean.elevatorNo}<html:hidden property="outsideFootballElevatorBean.elevatorNo" value="${outsideFootballElevatorBean.elevatorNo}"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.elevatorType"/>:</td>
    <td colspan="2">
      <logic:match name="outsideFootballElevatorBean" property="elevatorType" value="T">直梯</logic:match>
      <logic:match name="outsideFootballElevatorBean" property="elevatorType" value="F">扶梯</logic:match>
      <html:hidden property="outsideFootballElevatorBean.elevatorType" value="${outsideFootballElevatorBean.elevatorType}"/>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.salesContractNo"/>:</td>
    <td>${outsideFootballElevatorBean.salesContractNo}<html:hidden property="outsideFootballElevatorBean.salesContractNo" value="${outsideFootballElevatorBean.salesContractNo}"/>
    </td>
    <td class="wordtd"><bean:message key="elevatorSale.salesContractName"/>:</td>
    <td colspan="2">${outsideFootballElevatorBean.salesContractName}<html:hidden property="outsideFootballElevatorBean.salesContractName" value="${outsideFootballElevatorBean.salesContractName}"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.salesContractType"/>:</td>
    <td>${outsideFootballElevatorBean.salesContractType}<html:hidden property="outsideFootballElevatorBean.salesContractType" value="${outsideFootballElevatorBean.salesContractType}"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.inspectDate"/>:</td>
    <td colspan="2">${outsideFootballElevatorBean.inspectDate}<html:hidden property="outsideFootballElevatorBean.inspectDate" value="${outsideFootballElevatorBean.inspectDate}" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.elevatorParam"/>:</td>
    <td>${outsideFootballElevatorBean.elevatorParam}<html:hidden property="outsideFootballElevatorBean.elevatorParam" value="${outsideFootballElevatorBean.elevatorParam}"/></td>
    <td class="wordtd">
      <bean:message key="elevatorSale.floor"/>/<bean:message key="elevatorSale.stage"/>/<bean:message key="elevatorSale.door"/>:
    </td>
    <td colspan="2">
      ${outsideFootballElevatorBean.floor}/${outsideFootballElevatorBean.stage}/${outsideFootballElevatorBean.door}
      <html:hidden property="outsideFootballElevatorBean.floor" value="${outsideFootballElevatorBean.floor}"/>
      <html:hidden property="outsideFootballElevatorBean.stage" value="${outsideFootballElevatorBean.stage}"/>   
      <html:hidden property="outsideFootballElevatorBean.door" value="${outsideFootballElevatorBean.door}"/>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.weight"/>:</td>
    <td>${outsideFootballElevatorBean.weight}<html:hidden property="outsideFootballElevatorBean.weight" value="${outsideFootballElevatorBean.weight}"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.speed"/>:</td>
    <td colspan="2">${outsideFootballElevatorBean.speed}<html:hidden property="outsideFootballElevatorBean.speed" value="${outsideFootballElevatorBean.speed}"/></td>
  </tr>
  <tr>
   	<td class="wordtd"><bean:message key="elevatorSale.high"/>:</td>
    <td>${outsideFootballElevatorBean.high}<html:hidden property="outsideFootballElevatorBean.high" value="${outsideFootballElevatorBean.high}"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.seriesName"/>:</td>
    <td colspan="2">${outsideFootballElevatorBean.seriesName}<html:hidden property="outsideFootballElevatorBean.seriesName" value="${outsideFootballElevatorBean.seriesName}"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.operationName"/>:</td>
    <td><html:text property="outsideFootballElevatorBean.operationName" value="${elevatorSaleBean.operationName}"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.operationPhone"/>:</td>
    <td colspan="2"><html:text property="outsideFootballElevatorBean.operationPhone" value="${outsideFootballElevatorBean.operationPhone}"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.department"/>:</td>
    <td><html:text property="outsideFootballElevatorBean.department" value="${outsideFootballElevatorBean.department}" size="30"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.dealer"/>:</td>
    <td colspan="2"><html:text property="outsideFootballElevatorBean.dealer" value="${outsideFootballElevatorBean.dealer}" size="50"/></td>
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="elevatorSale.useUnit"/>:</td>
    <td><html:text property="outsideFootballElevatorBean.useUnit" value="${outsideFootballElevatorBean.useUnit}" size="50"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.deliveryAddress"/>:</td>
    <td colspan="2"><html:text property="outsideFootballElevatorBean.deliveryAddress" value="${outsideFootballElevatorBean.deliveryAddress}" size="50"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.enabledFlag"/>:</td>
    <td>
      <html:radio property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
    </td>
    <td class="wordtd" >扶梯类型:</td>
	<td>
	<logic:equal name="outsideFootballElevatorBean" property="elevatorType" value="F">
	<html:select property="seriesId">
	    <%-- <html:option value="">请选择</html:option> --%>
	   <html:options collection="seriesIdList" property="id.pullid" labelProperty="pullname"/>  
	  </html:select>
	  </logic:equal>
	</td>
  </tr>
  <tr>
  <td class="wordtd">附件上传：</td>
  <td colspan="4"><input type="file" name="fileName" id="fileName" class="default_input"/>  
    
    <logic:notEmpty name="outsideFootballElevatorBean" property="configuring">
      <span>		
        <a style="cursor:hand;text-decoration: underline;color: blue;" name="appendix" onclick="downloadFile('${outsideFootballElevatorBean.configuring}','ElevatorSalesInfo')"><bean:message key="qualitycheckmanagement.check"/></a>
        <a style="cursor:hand;text-decoration: underline;color: blue;" onclick="deleteFile('ElevatorSalesInfo','${outsideFootballElevatorBean.elevatorNo}','${outsideFootballElevatorBean.configuring}',this)"><bean:message key="delete.button.value"/></a>
      </span>
	</logic:notEmpty>
  </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.rem"/>:</td>
    <td colspan="4"><html:textarea property="outsideFootballElevatorBean.rem" value="${outsideFootballElevatorBean.rem}" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>

