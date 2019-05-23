<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/principalAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<table width="100%" class="tb">	
  <tr>
    <td class="wordtd"><bean:message key="principal.principalId"/>:</td>
    <td><html:text property="principalId" styleClass="default_input"/><font color="red">*</font></td>
  </tr>	
  <tr>
    <td class="wordtd"><bean:message key="principal.principalName"/>:</td>
    <td><html:text property="principalName" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="principal.phone"/>:</td>
    <td><html:text property="phone" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="principal.enabledFlag"/>:</td>
    <td>
      <html:radio property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="principal.rem"/>:</td>
    <td><html:text property="rem" styleClass="default_input"/></td>
  </tr>
</table>
</html:form>
