<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/entrustCustomerAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${entrustCustomerBean.companyId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="customer.companyId"/>:</td>
    <td>
      <bean:write name="entrustCustomerBean" property="companyId"/>
      <html:hidden name="entrustCustomerBean" property="companyId"/>
    </td>     
    <td class="wordtd"><bean:message key="customer.companyName"/>:</td>
    <td><html:text name="entrustCustomerBean" property="companyName" size="50" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.address"/>:</td>
    <td><html:text name="entrustCustomerBean" property="address" size="50" styleClass="default_input"/><font color="red">*</font></td>
  	<td class="wordtd"><bean:message key="customer.invoiceName"/>:</td>
    <td><html:text name="entrustCustomerBean" property="invoiceName" size="50" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.legalPerson"/>:</td>
    <td><html:text name="entrustCustomerBean" property="legalPerson" styleClass="default_input"/><font color="red">*</font></td>
    <td class="wordtd"><bean:message key="customer.client"/>:</td>
    <td><html:text name="entrustCustomerBean" property="client" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.contacts"/>:</td>
    <td><html:text name="entrustCustomerBean" property="contacts" styleClass="default_input"/><font color="red">*</font></td>
    <td class="wordtd"><bean:message key="customer.contactPhone"/>:</td>
    <td><html:text name="entrustCustomerBean" property="contactPhone" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="principal.principalName"/>:</td>
    <td>
      <html:hidden name="entrustCustomerBean" property="principalId"/>
      <html:text name="entrustCustomerBean" property="principalName" styleClass="default_input"/><font color="red">*</font>
      <!--<input type="button" value=".." onclick="openNewWindow()"/>-->    
    </td>
    <td class="wordtd"><bean:message key="principal.phone"/>:</td>
    <td><html:text name="entrustCustomerBean" property="principalPhone" styleClass="default_input"/><font color="red">*</font></td>
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.fax"/>:</td>
    <td><html:text name="entrustCustomerBean" property="fax" styleClass="default_input"/></td>
    <td class="wordtd"><bean:message key="customer.postCode"/>:</td>
    <td><html:text name="entrustCustomerBean" property="postCode" styleClass="default_input"/></td>    
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.bank"/>:</td>
    <td><html:text name="entrustCustomerBean" property="bank" size="50" styleClass="default_input"/></td>
    <td class="wordtd"><bean:message key="customer.accountHolder"/>:</td>
    <td><html:text name="entrustCustomerBean" property="accountHolder"  size="50" styleClass="default_input"/></td>    
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.account"/>:</td>
    <td><html:text name="entrustCustomerBean" property="account" styleClass="default_input"/></td>
    <td class="wordtd"><bean:message key="customer.taxId"/>:</td>
    <td><html:text name="entrustCustomerBean" property="taxId" styleClass="default_input"/></td>    
  </tr>    
  <tr>
    <td class="wordtd"><bean:message key="customer.custLevel"/>:</td>
    <td><bean:write name="entrustCustomerBean" property="custLevel"/></td> 
    <td class="wordtd"><bean:message key="customer.enabledFlag"/>:</td>
    <td>
	  <html:radio name="entrustCustomerBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="entrustCustomerBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd">安装资质级别:</td>
    <td><html:text name="entrustCustomerBean" property="qualiLevelAz" styleClass="default_input"/></td>
    <td class="wordtd">维保资质级别:</td>
    <td><html:text name="entrustCustomerBean" property="qualiLevelWb" styleClass="default_input"/></td>    
  </tr> 
  <tr>
    <td class="wordtd">维改资质级别:</td>
    <td><html:text name="entrustCustomerBean" property="qualiLevelWg" styleClass="default_input"/></td>
    <td class="wordtd"></td>
    <td></td>    
  </tr> 
  <tr>
    <td class="wordtd"><bean:message key="customer.rem"/>:</td>
    <td colspan="3"><html:textarea name="entrustCustomerBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="customerForm"/>