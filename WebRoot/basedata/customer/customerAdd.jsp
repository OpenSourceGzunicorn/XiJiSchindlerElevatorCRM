<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<script language="javascript" src="<html:rewrite forward="jq.js"/>"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/customerAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
    <td height="23" colspan="4">&nbsp;<b>客户信息</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.companyId"/>:</td>
    <td>
    ${companyId}
    <html:hidden property="companyId" value="${companyId}"/>
    </td>
    <td class="wordtd"><bean:message key="customer.companyName"/>:</td>
    <td><input type="text" name="companyName" id="companyName" size="50" class="default_input" onblur="chufa();"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.address"/>:</td>
    <td colspan="3"><html:text property="address" size="60" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.legalPerson"/>:</td>
    <td><html:text property="legalPerson" styleClass="default_input"/><font color="red">*</font></td>
    <td class="wordtd"><bean:message key="customer.client"/>:</td>
    <td><html:text property="client" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.contacts"/>:</td>
    <td><html:text property="contacts" styleClass="default_input"/><font color="red">*</font></td>
    <td class="wordtd"><bean:message key="customer.contactPhone"/>:</td>
    <td><html:text property="contactPhone" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="principal.principalName"/>:</td>
    <td>
      <html:hidden property="principalId"/>
      <html:text property="principalName" styleClass="default_input"/><font color="red">*</font>
      <!--<input type="button" value=".." onclick="openNewWindow()"/> -->  
    </td>
    <td class="wordtd"><bean:message key="principal.phone"/>:</td>
    <td><html:text property="principalPhone" styleClass="default_input"/><font color="red">*</font></td>
  </tr>  
  <tr>
    <td class="wordtd"><bean:message key="customer.fax"/>:</td>
    <td><html:text property="fax" styleClass="default_input"/></td>
    <td class="wordtd"><bean:message key="customer.postCode"/>:</td>
    <td><html:text property="postCode" styleClass="default_input"/></td>    
  </tr>      
  <tr>
    <td class="wordtd"><bean:message key="customer.enabledFlag"/>:</td>
    <td>
	  <html:radio property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
	<td class="wordtd"></td>
    <td></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.rem"/>:</td>
    <td colspan="3"><html:textarea property="rem" rows="3" cols="60" styleClass="default_textarea"/></td>
  </tr>
  
  <tr>
    <td height="23" colspan="4">&nbsp;<b>开票信息</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.invoiceName"/>:</td>
    <td><input type="text" name="invoiceName" id="invoiceName" size="50" class="default_input" /><font color="red">*</font></td>
    <td class="wordtd">开票类型:</td>
    <td>
    	<html:select property="r1" styleId="r1">
    		<html:option value="P">普票</html:option>
    		<html:option value="Z">专票</html:option>
    	</html:select><font color="red">*</font>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.bank"/>:</td>
    <td><html:text property="bank" size="50" styleClass="default_input"/></td>   
    <td class="wordtd"><bean:message key="customer.account"/>:</td>
    <td><html:text property="account" size="50"  styleClass="default_input"/></td>
  </tr>  
  <tr>
    <td class="wordtd">地址、电话:</td>
    <td><html:text property="accountHolder"  size="50" styleClass="default_input"/></td> 
    <td class="wordtd">纳税人识别号:</td>
    <td><html:text property="taxId" size="50" styleClass="default_input"/></td>    
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