<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>

<html:form action="/repairTasksRegistrationAction.do?method=toTechnologySupportRecord" styleId="Addform" enctype="multipart/form-data">
<html:hidden property="isreturn" styleId="isreturn"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="technologySupport.maintDivision"/>:</td>
    <td style="width: 30%"><bean:write name="mapBean" scope="request" property="maintDivision"/></td>
    <td class="wordtd"><bean:message key="technologySupport.maintStation"/>:</td>
    <td style="width: 30%"><bean:write name="mapBean" scope="request" property="maintStation"/></td>
 </tr>
  <tr>
  <td class="wordtd">¼±ÐÞ±àºÅ:</td>
  <td><bean:write name="mapBean" scope="request" property="singleNo"/>
  <input type="hidden" name="singleNo" id="singleNo" value="${mapBean.singleNo}"></td>
  <td class="wordtd">ÉêÇëÈÕÆÚ:</td>
   <td><bean:write name="mapBean" scope="request" property="operDate"/>
   <input type="hidden" name="operDate" id="operDate" value="${mapBean.operDate}">
   </td>
  </tr>
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.assignUser"/>:</td>
    <td><bean:write name="mapBean"scope="request" property="operId"/></td>
    <td class="wordtd"><bean:message key="technologySupport.assignUserTel"/>:</td>
    <td><bean:write name="mapBean" scope="request" property="operTel"/></td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.hmtId"/>:</td>
    <td><bean:write name="mapBean" scope="request" property="hmtIdName"/>
    <input type="hidden" name="hmtId" id="hmtId" value="${mapBean.hmtId }">
    </td>
    <td class="wordtd"><bean:message key="technologySupport.faultCode"/>:</td>
    <td><html:text name="mapBean" property="faultCode" styleId="faultCode"/><font style="color: red">*</font></td>
 </tr>
 <tr>
  <td class="wordtd"><bean:message key="technologySupport.faultStatus"/>:</td>
  <td colspan="3"><textarea rows="2" cols="40" class="default_textarea" name="faultStatus" id="faultStatus" >${mapBean.faultStatus }</textarea><font style="color: red">*</font></td>
 </tr>
 <tr>
 <td class="wordtd">¸½¼þ:</td><td colspan="3">
<table width="500px" id="fileList" class="tb">
<tr class="wordtd"><td width="5%" align="center"><input type="checkbox" onclick="checkTableFileAll(this)"></td>
<td align="left"><input type="button" name="toaddrow" value="+" onclick="AddRow(this)"/>
&nbsp;<input type="button" name='todelrow' value="-" onclick="deleteFileRow(this)">
</td></tr>
</table><br>
 </td>
 </tr>
 </table>
 </html:form>