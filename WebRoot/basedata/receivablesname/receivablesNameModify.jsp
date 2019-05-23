<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/receivablesNameAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${receivablesNameBean.recId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="ReceivablesName.recId"/>:</td>
    <td>
      <bean:write name="receivablesNameBean" property="recId"/>
      <html:hidden name="receivablesNameBean" property="recId"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="ReceivablesName.recName"/>:</td>
    <td><html:text name="receivablesNameBean" property="recName" size="50" styleClass="default_input"/><font color="red">*</font></td>
  </tr>   
  <tr>
    <td class="wordtd"><bean:message key="ReceivablesName.enabledflag"/>:</td>
    <td>
	  <html:radio name="receivablesNameBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="receivablesNameBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="ReceivablesName.rem"/>:</td>
    <td><html:textarea name="receivablesNameBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="receivablesNameForm"/>