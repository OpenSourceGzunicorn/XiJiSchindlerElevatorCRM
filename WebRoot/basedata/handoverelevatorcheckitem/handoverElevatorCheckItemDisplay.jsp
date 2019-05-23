<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="handoverElevatorCheckItemBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="handoverelevatorcheckitem.examType"/>:</td>
    <td><bean:write name="handoverElevatorCheckItemBean" scope="request" property="id.examType"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="handoverelevatorcheckitem.checkItem"/>:</td>
    <td><bean:write name="handoverElevatorCheckItemBean" scope="request" property="id.checkItem"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="handoverelevatorcheckitem.issueCoding"/>:</td>
    <td><bean:write name="handoverElevatorCheckItemBean" scope="request" property="id.issueCoding"/></td>
  </tr> 
  <tr>
    <td class="wordtd"><bean:message key="handoverelevatorcheckitem.issueContents"/>:</td>
    <td><bean:write name="handoverElevatorCheckItemBean" scope="request" property="issueContents"/></td>
  </tr>  
  <tr>
    <td class="wordtd">电梯类型:</td>
    <td>${handoverElevatorCheckItemBean.elevatorType=='T'?'直梯':'扶梯'}</td>
  </tr> 
   <tr>
    <td class="wordtd">排序号:</td>
    <td><bean:write name="handoverElevatorCheckItemBean" scope="request" property="orderby"/></td>
  </tr> 
   <tr>
    <td class="wordtd">小组编号:</td>
    <td><bean:write name="handoverElevatorCheckItemBean" scope="request" property="itemgroup"/></td>
  </tr> 
  <tr>
    <td class="wordtd"><bean:message key="handoverelevatorcheckitem.enabledFlag"/>:</td>
    <td>
	<logic:match name="handoverElevatorCheckItemBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="handoverElevatorCheckItemBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="handoverelevatorcheckitem.rem"/>:</td>
    <td><bean:write name="handoverElevatorCheckItemBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>