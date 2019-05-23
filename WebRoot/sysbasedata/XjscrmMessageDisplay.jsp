<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="customerBean">

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="customer.companyId"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="companyId"/></td>
    <td class="wordtd"><bean:message key="customer.companyName"/>:</td>
    <td width="30%"><bean:write name="customerBean" scope="request" property="companyName"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.address"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="address"/></td>
    <td class="wordtd"><bean:message key="customer.invoiceName"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="invoiceName"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.legalPerson"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="legalPerson"/></td>
    <td class="wordtd"><bean:message key="customer.client"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="client"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.contacts"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="contacts"/></td>
    <td class="wordtd"><bean:message key="customer.contactPhone"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="contactPhone"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="principal.principalName"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="principalName"/></td>
    <td class="wordtd"><bean:message key="principal.phone"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="principalPhone"/></td>
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.fax"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="fax"/></td>
    <td class="wordtd"><bean:message key="customer.postCode"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="postCode"/></td>    
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.bank"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="bank"/></td>
    <td class="wordtd"><bean:message key="customer.accountHolder"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="accountHolder"/></td>    
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.account"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="account"/></td>
    <td class="wordtd"><bean:message key="customer.taxId"/>:</td>
    <td><bean:write name="customerBean" scope="request" property="taxId"/></td>    
  </tr>    
  <tr>
    <td class="wordtd"><bean:message key="customer.custLevel"/>:</td>
    <td><bean:write name="customerBean" property="custLevel"/></td> 
    <td class="wordtd"><bean:message key="customer.enabledFlag"/>:</td>
    <td>
	<logic:match name="customerBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="customerBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="customer.rem"/>:</td>
    <td colspan="3"><bean:write name="customerBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>