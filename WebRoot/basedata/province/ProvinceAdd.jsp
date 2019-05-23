<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<br>
<html:errors/>
<html:form action="/provinceAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" class="tb">

  <tr>
    <td width="20%" class="wordtd"><bean:message key="province.provinceName"/>:</td>
    <td width="80%"><html:text property="provinceName" maxlength="255" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="country.countryName"/>:</td>
    <td width="80%">
    	<html:select property="countryId">
	  	<html:option value=""><bean:message key="pageword.pleaseselect"/></html:option> 
     	<html:options collection="countryList" property="countryId" labelProperty="countryName"/>
		</html:select><font color="red">*</font>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="province.enabledflag"/>:</td>
    <td class="inputtd"><html:radio property="enabledflag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="enabledflag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="province.rem"/>:</td>
    <td width="80%"><html:text property="rem" maxlength="255" styleClass="default_input"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="provinceForm"/>