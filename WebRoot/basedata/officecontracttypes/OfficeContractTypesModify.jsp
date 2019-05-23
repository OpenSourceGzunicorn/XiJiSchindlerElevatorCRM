<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/officeContractTypesAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${officeContractTypesBean.octId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="officecontracttypes.octId"/>:</td>
    <td>
      <bean:write name="officeContractTypesBean" property="octId"/>
      <html:hidden name="officeContractTypesBean" property="octId"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="officecontracttypes.octName"/>:</td>
    <td><html:text name="officeContractTypesBean" property="octName" size="60" styleClass="default_input"/><font color="red">*</font></td>
  <tr>
    <td class="wordtd"><bean:message key="officecontracttypes.enabledflag"/>:</td>
    <td>
	  <html:radio name="officeContractTypesBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="officeContractTypesBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="officecontracttypes.rem"/>:</td>
    <td><html:textarea name="officeContractTypesBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="officeContractTypesForm"/>