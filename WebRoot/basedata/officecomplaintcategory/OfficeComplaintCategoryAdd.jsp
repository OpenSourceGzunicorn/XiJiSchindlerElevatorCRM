<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<br>
<html:errors/>
<html:form action="/officeComplaintCategoryAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" class="tb">
  <tr>
    <td width="20%" class="wordtd"><bean:message key="officecomplaintcategory.occId"/>:</td>
    <td width="80%"><html:text property="occId" maxlength="255" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="officecomplaintcategory.occName"/>:</td>
   <td width="80%"><html:text property="occName" maxlength="255" size="60" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="officecomplaintcategory.enabledflag"/>:</td>
    <td class="inputtd"><html:radio property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="enabledFlag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="officecomplaintcategory.rem"/>:</td>
    <td><html:textarea property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="officeComplaintCategoryForm"/>