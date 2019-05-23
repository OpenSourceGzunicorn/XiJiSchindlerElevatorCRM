<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/invoiceTypeAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${invoiceTypeBean.inTypeId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="InvoiceType.inTypeId"/>:</td>
    <td>
      <bean:write name="invoiceTypeBean" property="inTypeId"/>
      <html:hidden name="invoiceTypeBean" property="inTypeId"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="InvoiceType.inTypeName"/>:</td>
    <td><html:text name="invoiceTypeBean" property="inTypeName" size="50" styleClass="default_input"/><font color="red">*</font></td>
  </tr>   
  <tr>
    <td class="wordtd"><bean:message key="InvoiceType.enabledflag"/>:</td>
    <td>
	  <html:radio name="invoiceTypeBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="invoiceTypeBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="InvoiceType.rem"/>:</td>
    <td><html:textarea name="invoiceTypeBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="invoiceTypeForm"/>