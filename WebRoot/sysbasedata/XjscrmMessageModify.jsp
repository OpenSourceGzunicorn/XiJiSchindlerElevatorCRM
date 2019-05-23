<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/customerAction.do?method=toUpdate">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${customerBean.companyId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="customer.companyId"/>:</td>
    <td>
      <bean:write name="customerBean" property="companyId"/>
      <html:hidden name="customerBean" property="companyId"/>
    </td>     
    <td class="wordtd"><bean:message key="customer.companyName"/>:</td>
    <td><html:text name="customerBean" property="companyName" size="50" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.address"/>:</td>
    <td><html:text name="customerBean" property="address" size="50" styleClass="default_input"/><font color="red">*</font></td>
    <td class="wordtd"><bean:message key="customer.invoiceName"/>:</td>
    <td><html:text name="customerBean" property="invoiceName" size="50" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.legalPerson"/>:</td>
    <td><html:text name="customerBean" property="legalPerson" styleClass="default_input"/><font color="red">*</font></td>
    <td class="wordtd"><bean:message key="customer.client"/>:</td>
    <td><html:text name="customerBean" property="client" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.contacts"/>:</td>
    <td><html:text name="customerBean" property="contacts" styleClass="default_input"/><font color="red">*</font></td>
    <td class="wordtd"><bean:message key="customer.contactPhone"/>:</td>
    <td><html:text name="customerBean" property="contactPhone" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="principal.principalName"/>:</td>
    <td>
      <html:hidden name="customerBean" property="principalId"/>
      <html:text name="customerBean" property="principalName" styleClass="default_input"/><font color="red">*</font>
      <!--<input type="button" value=".." onclick="openNewWindow()"/>-->    
    </td>
    <td class="wordtd"><bean:message key="principal.phone"/>:</td>
    <td><html:text name="customerBean" property="principalPhone" styleClass="default_input"/><font color="red">*</font></td>
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.fax"/>:</td>
    <td><html:text name="customerBean" property="fax" styleClass="default_input"/></td>
    <td class="wordtd"><bean:message key="customer.postCode"/>:</td>
    <td><html:text name="customerBean" property="postCode" styleClass="default_input"/></td>    
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.bank"/>:</td>
    <td><html:text name="customerBean" property="bank" size="50" styleClass="default_input"/></td>
    <td class="wordtd"><bean:message key="customer.accountHolder"/>:</td>
    <td><html:text name="customerBean" property="accountHolder" size="50" styleClass="default_input"/></td>    
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.account"/>:</td>
    <td><html:text name="customerBean" property="account" size="50" styleClass="default_input"/></td>
    <td class="wordtd"><bean:message key="customer.taxId"/>:</td>
    <td><html:text name="customerBean" property="taxId" size="50" styleClass="default_input"/></td>    
  </tr>    
  <tr>
    <td class="wordtd"><bean:message key="customer.custLevel"/>:</td>
    <td><bean:write name="customerBean" property="custLevel"/></td> 
    <td class="wordtd"><bean:message key="customer.enabledFlag"/>:</td>
    <td>
	  <html:radio name="customerBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="customerBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.rem"/>:</td>
    <td colspan="3"><html:textarea name="customerBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="customerForm"/>