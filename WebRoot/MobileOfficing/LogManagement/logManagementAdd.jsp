<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/logManagementAction.do?method=toAddRecord" >
<html:hidden property="isreturn"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="logManagement.maintDivision"/>:</td>
    <td style="width: 35%">${comName }</td>
    <td class="wordtd"><bean:message key="logManagement.maintStation"/>:</td>
    <td style="width: 35%">${storageName }</td>
 </tr>
  <tr>
    <td class="wordtd"><bean:message key="logManagement.operId"/>:</td>
    <td style="width: 35%">${userName }</td>
    <td class="wordtd"><bean:message key="logManagement.operDate"/>:</td>
    <td style="width: 35%">${opendate }<input type="hidden" name="opendate" value="${opendate }"></td>
 </tr>
  <tr>
    <td class="wordtd"><bean:message key="logManagement.salesContractNo"/>:</td>
    <td style="width: 35%">
    <input type="text" name="salesContractNo" id="salesContractNo"><font color="red">*</font>
    </td>
    <td class="wordtd"><bean:message key="logManagement.projectName"/>:</td>
    <td style="width: 35%">
    <input type="text" name="projectName" id="projectName"><font color="red">*</font>
    </td>
 </tr>
 </table>
 <br/>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
 <tr>
   <td class="wordtd" colspan="4" style="text-align: center;"><bean:message key="logManagement.workContent"/></td>
 </tr>
 <tr>
     <td colspan="4" ><textarea rows="5" cols="150" class="default_textarea_noborder" name="workContent" id="workContent"></textarea></td>
 </tr>
 </table>
</html:form>