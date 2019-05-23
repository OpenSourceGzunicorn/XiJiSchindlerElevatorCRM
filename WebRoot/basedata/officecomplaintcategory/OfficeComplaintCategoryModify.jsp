<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/officeComplaintCategoryAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${officeComplaintCategoryBean.occId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="officecomplaintcategory.occId"/>:</td>
    <td>
      <bean:write name="officeComplaintCategoryBean" property="occId"/>
      <html:hidden name="officeComplaintCategoryBean" property="occId"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="officecomplaintcategory.occName"/>:</td>
    <td><html:text name="officeComplaintCategoryBean" property="occName" size="60" styleClass="default_input"/><font color="red">*</font></td>
  <tr>
    <td class="wordtd"><bean:message key="officecomplaintcategory.enabledflag"/>:</td>
    <td>
	  <html:radio name="officeComplaintCategoryBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="officeComplaintCategoryBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="officecomplaintcategory.rem"/>:</td>
    <td><html:textarea name="officeComplaintCategoryBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="officeComplaintCategoryForm"/>