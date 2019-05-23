<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/cityAction.do?method=toUpdateRecord">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
	<html:hidden property="isreturn"/>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="city.cityId"/>:</td>
    <td width="80%" class="inputtd"><html:hidden property="id"/><html:text property="cityId" readonly="true" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="city.cityName"/>:</td>
    <td class="inputtd"><html:text property="cityName" maxlength="255" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="country.countryName"/>:</td>
    <td width="80%">
    	<html:select property="provinceId">
	  	<html:option value=""><bean:message key="pageword.pleaseselect"/></html:option> 
     	<html:options collection="provinceList" property="provinceId" labelProperty="provinceName"/>
		</html:select><font color="red">*</font>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="city.enabledflag"/>:</td>
    <td class="inputtd"><html:radio property="enabledflag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="enabledflag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="city.rem"/>:</td>
    <td class="inputtd"><html:text property="rem" maxlength="255" styleClass="default_input"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="cityForm"/>