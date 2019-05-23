<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/customerAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${customerBean.companyId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
<tr>
    <td height="23" colspan="4">&nbsp;<b>客户信息</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.companyId"/>:</td>
    <td>
      <bean:write name="customerBean" property="companyId"/>
      <html:hidden name="customerBean" property="companyId"/>
    </td>     
    <td class="wordtd"><bean:message key="customer.companyName"/>:</td>
    <td><html:text name="customerBean" property="companyName" size="50" styleClass="default_input" onblur="chufa();"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.address"/>:</td>
    <td colspan="3"><html:text name="customerBean" property="address" size="50" styleClass="default_input"/><font color="red">*</font></td>
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
    <td colspan="3"><html:textarea name="customerBean" property="rem" rows="3" cols="60" styleClass="default_textarea"/></td>
  </tr>
  
  <tr>
    <td height="23" colspan="4">&nbsp;<b>开票信息</td>
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.invoiceName"/>:</td>
    <td><html:text name="customerBean" property="invoiceName" size="50" styleClass="default_input"/><font color="red">*</font></td>
  	<td class="wordtd">开票类型:</td>
    <td>
    	<html:select name="customerBean" property="r1" styleId="r1">
    		<html:option value="P">普票</html:option>
    		<html:option value="Z">专票</html:option>
    	</html:select><font color="red">*</font>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.bank"/>:</td>
    <td><html:text name="customerBean" property="bank" styleId="bank" size="50" styleClass="default_input"/></td>   
    <td class="wordtd"><bean:message key="customer.account"/>:</td>
    <td><html:text name="customerBean" property="account" styleId="account" size="50"  styleClass="default_input"/></td>
  </tr>  
  <tr>
    <td class="wordtd">地址、电话:</td>
    <td><html:text name="customerBean" property="accountHolder" styleId="accountHolder" size="50" styleClass="default_input"/></td> 
    <td class="wordtd">纳税人识别号:</td>
    <td><html:text name="customerBean" property="taxId" styleId="taxId" size="50" styleClass="default_input"/></td>    
  </tr>
</table>
</html:form>
<html:javascript formName="customerForm"/>
 <script type="text/javascript">
    function chufa(){
    	var companyNames = document.getElementsByName("companyName");
    	document.getElementsByName("invoiceName")[0].value=companyNames[0].value;
    	
    }
 </script>
 
 