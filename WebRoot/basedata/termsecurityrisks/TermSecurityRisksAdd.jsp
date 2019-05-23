<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<br>
<html:errors/>
<html:form action="/termSecurityRisksAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" class="tb">
  <tr>
    <td width="20%" class="wordtd"><bean:message key="termsecurityrisks.tsrId"/>:</td>
    <td width="80%"><html:text property="tsrId" maxlength="255" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="termsecurityrisks.tsrDetail"/>:</td>
   <td width="80%"><html:textarea property="tsrDetail" rows="2" cols="60" styleClass="default_textarea"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="termsecurityrisks.enabledflag"/>:</td>
    <td class="inputtd"><html:radio property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="enabledFlag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="termsecurityrisks.rem"/>:</td>
    <td><html:textarea property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="termSecurityRisksForm"/>