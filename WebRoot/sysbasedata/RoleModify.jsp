<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/roleAction.do?method=toUpdateRecord">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<html:hidden property="isreturn"/>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="role.roleid"/>:</td>
    <td width="80%" class="inputtd"><html:hidden property="id"/><html:text property="roleid" maxlength="64" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="role.rolename"/>:</td>
    <td class="inputtd"><html:text property="rolename" maxlength="127" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="role.moduleid"/>:</td>
    <td class="inputtd">
  	<html:select property="moduleid" styleClass="default_input">
		<html:options collection="moduleList" property="moduleid" labelProperty="modulename"/>
  	</html:select><font color="red">*</font>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="role.allowflag"/>:</td>
    <td class="inputtd">
  	<html:select property="allowflag" styleClass="default_input">
		<html:options collection="viewallowflagList" property="flagid" labelProperty="flagname"/>
  	</html:select><font color="red">*</font>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="role.enabledflag"/>:</td>
    <td class="inputtd"><html:radio property="enabledflag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="enabledflag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="role.rem1"/>:</td>
    <td class="inputtd"><html:textarea property="rem1" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="roleForm"/>