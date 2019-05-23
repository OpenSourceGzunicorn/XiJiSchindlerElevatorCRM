<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/receivablesNameAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="ReceivablesName.recId"/>:</td>
    <td><html:text property="recId" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ReceivablesName.recName"/>:</td>
    <td><html:text property="recName" size="60" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ReceivablesName.enabledflag"/>:</td>
    <td>
	  <html:radio property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ReceivablesName.rem"/>:</td>
    <td><html:textarea property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="receivablesNameForm"/>