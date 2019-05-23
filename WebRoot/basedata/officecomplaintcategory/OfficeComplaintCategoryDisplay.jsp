<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="officeComplaintCategoryBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td width="20%" class="wordtd"><bean:message key="officecomplaintcategory.occId"/>:</td>
    <td width="80%" class="inputtd"><bean:write name="officeComplaintCategoryBean" scope="request" property="occId"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="officecomplaintcategory.occName"/>:</td>
    <td class="inputtd"><bean:write name="officeComplaintCategoryBean" scope="request" property="occName"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="officecomplaintcategory.enabledflag"/>:</td>
    <td class="inputtd">
	<logic:match name="officeComplaintCategoryBean" property="enabledFlag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="officeComplaintCategoryBean" property="enabledFlag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="officecomplaintcategory.rem"/>:</td>
    <td class="inputtd"><bean:write name="officeComplaintCategoryBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>