<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="entrustCustomerBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="customer.companyId"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="companyId"/></td>
    <td class="wordtd"><bean:message key="customer.companyName"/>:</td>
    <td width="30%"><bean:write name="entrustCustomerBean" scope="request" property="companyName"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.address"/>:</td>
    <td ><bean:write name="entrustCustomerBean" scope="request" property="address"/></td>
  	<td class="wordtd"><bean:message key="customer.invoiceName"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="invoiceName"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.legalPerson"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="legalPerson"/></td>
    <td class="wordtd"><bean:message key="customer.client"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="client"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.contacts"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="contacts"/></td>
    <td class="wordtd"><bean:message key="customer.contactPhone"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="contactPhone"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="principal.principalName"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="principalName"/></td>
    <td class="wordtd"><bean:message key="principal.phone"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="principalPhone"/></td>
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.fax"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="fax"/></td>
    <td class="wordtd"><bean:message key="customer.postCode"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="postCode"/></td>    
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.bank"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="bank"/></td>
    <td class="wordtd"><bean:message key="customer.accountHolder"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="accountHolder"/></td>    
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.account"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="account"/></td>
    <td class="wordtd"><bean:message key="customer.taxId"/>:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="taxId"/></td>    
  </tr>    
  <tr>
    <td class="wordtd"><bean:message key="customer.custLevel"/>:</td>
    <td><bean:write name="entrustCustomerBean" property="custLevel"/></td> 
    <td class="wordtd"><bean:message key="customer.enabledFlag"/>:</td>
    <td>
	<logic:match name="entrustCustomerBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="entrustCustomerBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
    <td class="wordtd">安装资质级别:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="qualiLevelAz"/></td>
    <td class="wordtd">维保资质级别:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="qualiLevelWb"/></td>    
  </tr> 
  <tr>
    <td class="wordtd">维改资质级别:</td>
    <td><bean:write name="entrustCustomerBean" scope="request" property="qualiLevelWg"/></td>
    <td class="wordtd"></td>
    <td></td>    
  </tr> 
  <tr>
	<td class="wordtd"><bean:message key="customer.rem"/>:</td>
    <td colspan="3"><bean:write name="entrustCustomerBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>