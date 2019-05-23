<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/hotlineMotherboardTypeAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="hotlinemotherboardtype.hmtId"/>:</td>
    <td><html:text property="hmtId" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="hotlinemotherboardtype.hmtName"/>:</td>
    <td><html:text property="hmtName" size="60" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="hotlinemotherboardtype.enabledFlag"/>:</td>
    <td>
	  <html:radio property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="hotlinemotherboardtype.rem"/>:</td>
    <td><html:textarea property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="hotlineMotherboardTypeForm"/>