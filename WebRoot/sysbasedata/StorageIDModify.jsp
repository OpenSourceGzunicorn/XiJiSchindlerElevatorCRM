<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<%@ page import="com.gzunicorn.common.util.*" %>
<%@ page import="com.gzunicorn.hibernate.sysmanager.Company" %>
<br>
<html:errors/>
<html:form action="/storageidAction.do?method=toUpdateRecord">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
<html:hidden property="isreturn"/>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="storageid.storageid"/>:</td>
    <td width="80%" class="inputtd"><html:hidden property="id"/><html:text property="storageid" readonly="true" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.storagename"/>:</td>
    <td class="inputtd"><html:text property="storagename" size="50" maxlength="127" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="storageid.comid"/>:</td>
    <td>
	<html:select property="comid">
	  <html:option value=""><bean:message key="pageword.pleaseselect"/></html:option> 
      <html:options collection="viewcompanyList" property="comid" labelProperty="comfullname"/>
	</html:select>
	<font color="red">*</font>
	</td> 
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="storageid.parentstorageid"/>:</td>
    <td>
	<html:select property="parentstorageid">
	  <html:option value=""><bean:message key="pageword.pleaseselect"/></html:option> 
      <html:options collection="viewstoragerefList" property="storageid" labelProperty="storagename"/>
	</html:select>
	<font color="red">*</font>
	</td> 
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.storagehead"/>:</td>
    <td class="inputtd"><html:text property="storagehead" maxlength="32" styleClass="default_input"/></td>
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="storageid.storagetype"/>:</td>
    <td>
	<html:select property="storagetype">
      <html:options collection="viewstoragetypeList" property="typeid" labelProperty="typename"/>
	</html:select>
	</td> 
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.phone"/>:</td>
    <td class="inputtd"><html:text property="phone" maxlength="64" styleClass="default_input"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.fax"/>:</td>
    <td class="inputtd"><html:text property="fax" maxlength="64" styleClass="default_input"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.email"/>:</td>
    <td class="inputtd"><html:text property="email" maxlength="64" styleClass="default_input"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.enabledflag"/>:</td>
    <td class="inputtd"><html:radio property="enabledflag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="enabledflag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="storageid.rem"/>:</td>
    <td class="inputtd"><html:textarea property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="storageidForm"/>