<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="companyBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td width="20%" class="wordtd"><bean:message key="company.comid"/>:</td>
    <td width="80%" class="inputtd"><bean:write name="companyBean" scope="request" property="comid"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="company.comname"/>:</td>
    <td class="inputtd"><bean:write name="companyBean" scope="request" property="comname"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="company.comfullname"/>:</td>
    <td class="inputtd"><bean:write name="companyBean" scope="request" property="comfullname"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="company.comtyped"/>:</td>
    <td class="inputtd"><bean:write name="companyBean" scope="request" property="comtype"/></td>
  </tr>

  <tr>
    <td class="wordtd"><bean:message key="company.linkman"/>:</td>
    <td class="inputtd"><bean:write name="companyBean" scope="request" property="linkman"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="company.linkmantel"/>:</td>
    <td class="inputtd"><bean:write name="companyBean" scope="request" property="linkmantel"/></td>
  </tr>
  
  <tr>
    <td class="wordtd"><bean:message key="company.address"/>:</td>
    <td class="inputtd"><bean:write name="companyBean" scope="request" property="address"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="company.enabledflag"/>:</td>
    <td class="inputtd">
	<logic:match name="companyBean" property="enabledflag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="companyBean" property="enabledflag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="company.rem"/>:</td>
    <td class="inputtd"><bean:write name="companyBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>