<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/termSecurityRisksAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${termSecurityRisksBean.tsrId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="termsecurityrisks.tsrId"/>:</td>
    <td>
      <bean:write name="termSecurityRisksBean" property="tsrId"/>
      <html:hidden name="termSecurityRisksBean" property="tsrId"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="termsecurityrisks.tsrDetail"/>:</td>
    <td><html:textarea name="termSecurityRisksBean" property="tsrDetail" rows="2" cols="60" styleClass="default_textarea"/><font color="red">*</font></td>
  <tr>
    <td class="wordtd"><bean:message key="termsecurityrisks.enabledflag"/>:</td>
    <td>
	  <html:radio name="termSecurityRisksBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="termSecurityRisksBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="termsecurityrisks.rem"/>:</td>
    <td><html:textarea name="termSecurityRisksBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="termSecurityRisksForm"/>