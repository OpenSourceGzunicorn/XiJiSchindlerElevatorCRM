<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>

<html:form action="/${action }.do?method=toAccessoriesRequisitionRecord" styleId="Addform" enctype="multipart/form-data">
<html:hidden property="isreturn" styleId="isreturn"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  
  <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.singleNo"/>:</td>
    <td colspan="3"><bean:write name="mapBean" scope="request" property="singleNo"/>
    <input type="hidden" name="singleNo" id="singleNo" value="${mapBean.singleNo }">
    <input type="hidden" name="id" id="id" value="${id }">
    </td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.oldNo"/>:</td>
    <td style="width: 35%"><html:text name="mapBean"  property="oldNo" styleId="oldNo"></html:text>
    <font color="red">*</font></td>
    <td class="wordtd"><bean:message key="accessoriesRequisition.oldImage"/>:</td> <td>
      <html:file property="newfile" styleClass="default_input" size="30"/>
      <font color="red">*</font>
    </td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.newNo"/>:</td>
    <td style="width: 35%"></td>
    <td class="wordtd"><bean:message key="accessoriesRequisition.newImage"/>:</td>
    <td>
 </td>
 </tr>
  <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.operId"/>:</td>
    <td style="width: 35%"><bean:write name="mapBean" scope="request" property="operId"/></td>
    <td class="wordtd"><bean:message key="accessoriesRequisition.operDate"/>:</td>
    <td><bean:write name="mapBean" scope="request" property="operDate"/>
    <input type="hidden" name="operDate" id="operDate" value="${mapBean.operDate}">
    </td>
 </tr>
 
  <tr>
    <td class="wordtd"><bean:message key="technologySupport.maintDivision"/>:</td>
    <td style="width: 35%"><bean:write name="mapBean" scope="request" property="maintDivision"/></td>
    <td class="wordtd"><bean:message key="technologySupport.maintStation"/>:</td>
    <td><bean:write name="mapBean" scope="request" property="maintStation"/></td>
 </tr>
 </table>
 </html:form>