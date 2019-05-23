<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ page import="com.gzunicorn.hibernate.basedata.elevatorsales.ElevatorSalesInfo" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<br>
<html:errors/>
<html:form action="/elevatorSaleAction.do?method=toUpdateRecord" enctype="multipart/form-data">
<html:hidden property="isreturn"/>
<html:hidden property="id" value='${elevatorSaleBean.elevatorNo}'/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.elevatorNo"/>:</td>
    <td>${elevatorSaleBean.elevatorNo}<html:hidden property="elevatorSaleBean.elevatorNo" value="${elevatorSaleBean.elevatorNo}"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.elevatorType"/>:</td>
    <td >
      <logic:match name="elevatorSaleBean" property="elevatorType" value="T">直梯</logic:match>
      <logic:match name="elevatorSaleBean" property="elevatorType" value="F">扶梯</logic:match>
      <html:hidden property="elevatorSaleBean.elevatorType" value="${elevatorSaleBean.elevatorType}"/>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.salesContractNo"/>:</td>
    <td>${elevatorSaleBean.salesContractNo}<html:hidden property="elevatorSaleBean.salesContractNo" value="${elevatorSaleBean.salesContractNo}"/>
    </td>
    <td class="wordtd"><bean:message key="elevatorSale.salesContractName"/>:</td>
    <td >${elevatorSaleBean.salesContractName}<html:hidden property="elevatorSaleBean.salesContractName" value="${elevatorSaleBean.salesContractName}"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.salesContractType"/>:</td>
    <td>${elevatorSaleBean.salesContractType}<html:hidden property="elevatorSaleBean.salesContractType" value="${elevatorSaleBean.salesContractType}"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.inspectDate"/>:</td>
    <td >${elevatorSaleBean.inspectDate}<html:hidden property="elevatorSaleBean.inspectDate" value="${elevatorSaleBean.inspectDate}" styleClass="Wdate" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.elevatorParam"/>:</td>
    <td>${elevatorSaleBean.elevatorParam}<html:hidden property="elevatorSaleBean.elevatorParam" value="${elevatorSaleBean.elevatorParam}"/></td>
    <td class="wordtd">
      <bean:message key="elevatorSale.floor"/>/<bean:message key="elevatorSale.stage"/>/<bean:message key="elevatorSale.door"/>:
    </td>
    <td >
      ${elevatorSaleBean.floor}/${elevatorSaleBean.stage}/${elevatorSaleBean.door}
      <html:hidden property="elevatorSaleBean.floor" value="${elevatorSaleBean.floor}"/>
      <html:hidden property="elevatorSaleBean.stage" value="${elevatorSaleBean.stage}"/>   
      <html:hidden property="elevatorSaleBean.door" value="${elevatorSaleBean.door}"/>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.weight"/>:</td>
    <td>${elevatorSaleBean.weight}<html:hidden property="elevatorSaleBean.weight" value="${elevatorSaleBean.weight}"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.speed"/>:</td>
    <td >${elevatorSaleBean.speed}<html:hidden property="elevatorSaleBean.speed" value="${elevatorSaleBean.speed}"/></td>
  </tr>
  <tr>
   	<td class="wordtd"><bean:message key="elevatorSale.high"/>:</td>
    <td>${elevatorSaleBean.high}<html:hidden property="elevatorSaleBean.high" value="${elevatorSaleBean.high}"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.seriesName"/>:</td>
    <td >${elevatorSaleBean.seriesName}<html:hidden property="elevatorSaleBean.seriesName" value="${elevatorSaleBean.seriesName}"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.operationName"/>:</td>
    <td><html:text property="elevatorSaleBean.operationName" value="${elevatorSaleBean.operationName}"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.operationPhone"/>:</td>
    <td ><html:text property="elevatorSaleBean.operationPhone" value="${elevatorSaleBean.operationPhone}"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.department"/>:</td>
    <td><html:text property="elevatorSaleBean.department" value="${elevatorSaleBean.department}" size="30"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.dealer"/>:</td>
    <td ><html:text property="elevatorSaleBean.dealer" value="${elevatorSaleBean.dealer}" size="50"/></td>
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="elevatorSale.useUnit"/>:</td>
    <td><html:text property="elevatorSaleBean.useUnit" value="${elevatorSaleBean.useUnit}" size="50"/></td>
    <td class="wordtd"><bean:message key="elevatorSale.deliveryAddress"/>:</td>
    <td ><html:text property="elevatorSaleBean.deliveryAddress" value="${elevatorSaleBean.deliveryAddress}" size="50"/></td>
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
      <html:radio property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
    </td>
    <td class="wordtd" >扶梯类型:</td>
	<td>
	<logic:equal name="elevatorSaleBean" property="elevatorType" value="F">
		<html:select property="seriesId">
		    <%-- <html:option value="">请选择</html:option> --%>
		   <html:options collection="seriesIdList" property="id.pullid" labelProperty="pullname"/>  
	  </html:select>
	  </logic:equal>
	</td>
  </tr>
  <tr>
    <td class="wordtd">是否在保:</td>
    <td>
    	${elevatorSaleBean.r1 }
		<%-- select name="r1" property="r1" >
			<option value="">请选择</option>
			<option value="是" <logic:equal name="elevatorSaleBean" property="r1" value="是">selected="selected"</logic:equal>>是</option>
			<option value="否" <logic:equal name="elevatorSaleBean" property="r1" value="否">selected="selected"</logic:equal>>否</option>
		</select--%>
    </td>
    <td class="wordtd">所属省份:</td>
	<td><input type="text" name="r2" value="${elevatorSaleBean.r2 }"></td>
  </tr>
  <tr>
    <td class="wordtd">所属市:</td>
    <td><input type="text" name="r3" value="${elevatorSaleBean.r3 }"></td>
    <td class="wordtd">所属县/区:</td>
	<td><input type="text" name="r4" value="${elevatorSaleBean.r4 }" size="50"></td>
  </tr>
  <tr>
   <td class="wordtd">附件上传:</td>
	<td colspan="3"><input type="file" name="fileName" id="fileName" class="default_input" size="40"/>  
    
    <logic:notEmpty name="elevatorSaleBean" property="configuring">
      <span>		
        <a style="cursor:hand;text-decoration: underline;color: blue;" name="appendix" onclick="downloadFile('${elevatorSaleBean.configuring}','ElevatorSalesInfo')"><bean:message key="qualitycheckmanagement.check"/></a>
        <a style="cursor:hand;text-decoration: underline;color: blue;" onclick="deleteFile('ElevatorSalesInfo','${elevatorSaleBean.elevatorNo}','${elevatorSaleBean.configuring}',this)"><bean:message key="delete.button.value"/></a>
      </span>
	</logic:notEmpty>
  </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="elevatorSale.rem"/>:</td>
    <td colspan="3"><html:textarea property="elevatorSaleBean.rem" value="${elevatorSaleBean.rem}" rows="3" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>

