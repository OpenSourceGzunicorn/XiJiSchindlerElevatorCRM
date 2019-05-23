<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="storageidBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td width="20%" class="wordtd"><bean:message key="storageid.storageid"/>:</td>
    <td width="80%" class="inputtd"><bean:write name="storageidBean" scope="request" property="storageid"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.storagename"/>:</td>
    <td class="inputtd"><bean:write name="storageidBean" scope="request" property="storagename"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.comid"/>:</td>
    <td class="inputtd"><bean:write name="storageidBean" scope="request" property="comid"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.parentstorageid"/>:</td>
    <td class="inputtd"><bean:write name="storageidBean" scope="request" property="parentstorageid"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.storagehead"/>:</td>
    <td class="inputtd"><bean:write name="storageidBean" scope="request" property="storagehead"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.storagetype"/>:</td>
    <td class="inputtd"><bean:write name="storageidBean" scope="request" property="storagetype"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.phone"/>:</td>
    <td class="inputtd"><bean:write name="storageidBean" scope="request" property="phone"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.fax"/>:</td>
    <td class="inputtd"><bean:write name="storageidBean" scope="request" property="fax"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.email"/>:</td>
    <td class="inputtd"><bean:write name="storageidBean" scope="request" property="email"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.enabledflag"/>:</td>
    <td class="inputtd">
	<logic:match name="storageidBean" property="enabledflag" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match name="storageidBean" property="enabledflag" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.rem"/>:</td>
    <td class="inputtd"><bean:write name="storageidBean" scope="request" property="rem"/></td>
  </tr>
</table>
</logic:present>