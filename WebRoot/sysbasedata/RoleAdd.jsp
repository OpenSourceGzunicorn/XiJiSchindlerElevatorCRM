<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<br>
<html:errors/>
<html:form action="/roleAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" class="tb">
  <tr>
    <td width="20%" class="wordtd"><bean:message key="role.roleid" />:</td>
    <td width="80%"><html:text property="roleid" maxlength="64" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="role.rolename"/>:</td>
    <td><html:text property="rolename" maxlength="127" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="role.moduleid"/>:</td>
    <td>
  	<html:select property="moduleid" styleClass="default_input">
		<html:options collection="moduleList" property="moduleid" labelProperty="modulename"/>
  	</html:select><font color="red">*</font>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="role.allowflag"/>:</td>
    <td>
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
    <td><html:textarea property="rem1" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="roleForm"/>