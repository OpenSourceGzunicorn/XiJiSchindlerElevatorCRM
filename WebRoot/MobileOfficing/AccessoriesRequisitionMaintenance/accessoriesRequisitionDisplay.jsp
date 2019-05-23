<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="accessoriesRequisitionBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.singleNo"/>:</td>
    <td colspan="3"><bean:write name="accessoriesRequisitionBean" scope="request" property="singleNo"/></td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.oldNo"/>:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="oldNo"/></td>
    <td class="wordtd"><bean:message key="accessoriesRequisition.oldImage"/>:</td> <td>
    <a style="cursor:hand;text-decoration: underline;color: blue;" name="appendix" onclick="downloadFile('${accessoriesRequisitionBean.oldImage}')">查看</a>
    </td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.newNo"/>:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="newNo"/></td>
    <td class="wordtd"><bean:message key="accessoriesRequisition.newImage"/>:</td>
    <td>
    <logic:notEmpty name="accessoriesRequisitionBean" property="newImage">
    <bean:define id="newImage" name="accessoriesRequisitionBean" scope="request" property="newImage"></bean:define>
      <a href="#" onclick="downloadFile(<%=newImage.toString()%>)">查看</a>
    </logic:notEmpty>
 </td>
 </tr>
  <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.operId"/>:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="operId"/></td>
    <td class="wordtd"><bean:message key="accessoriesRequisition.operDate"/>:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="operDate"/></td>
 </tr>
 
  <tr>
    <td class="wordtd"><bean:message key="technologySupport.maintDivision"/>:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="maintDivision"/></td>
    <td class="wordtd"><bean:message key="technologySupport.maintStation"/>:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="maintStation"/></td>
 </tr>
 </table>
 <br/>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
 <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.personInCharge"/>:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="personInCharge"/></td>
    <td class="wordtd"><bean:message key="accessoriesRequisition.isAgree"/>:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="isAgree"/></td>
 </tr>
 
  <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.picauditRem"/>:</td>
    <td colspan="3"><bean:write name="accessoriesRequisitionBean" scope="request" property="picauditRem"/></td>
   
 </tr>
 
  <tr>
    <td class="wordtd"><bean:message key="accessoriesRequisition.picauditDate"/>:</td>
    <td colspan="3"><bean:write name="accessoriesRequisitionBean" scope="request" property="picauditDate"/></td>
    
 </tr>
 </table>
 <br>
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
 <tr>
    <td class="wordtd">仓管录入人:</td>
    <td style="width: 35%"><bean:write name="accessoriesRequisitionBean" scope="request" property="warehouseManager"/></td>
    <td class="wordtd">仓管录入日期:</td>
    <td><bean:write name="accessoriesRequisitionBean" scope="request" property="wmdate"/></td>
 </tr>
 </table>
</logic:present>