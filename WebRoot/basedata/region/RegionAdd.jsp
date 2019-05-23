<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<br>
<html:errors/>
<html:form action="/regionAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" class="tb">

  <tr>
    <td width="20%" class="wordtd"><bean:message key="region.regionName"/>:</td>
    <td width="80%"><html:text property="regionName" maxlength="255" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="city.cityName"/>:</td>
    <td width="80%">
    	<html:select property="cityId">
	  	<html:option value=""><bean:message key="pageword.pleaseselect"/></html:option> 
     	<html:options collection="cityList" property="cityId" labelProperty="cityName"/>
		</html:select><font color="red">*</font>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="region.enabledflag"/>:</td>
    <td class="inputtd"><html:radio property="enabledflag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="enabledflag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="region.rem"/>:</td>
    <td width="80%"><html:text property="rem" maxlength="255" styleClass="default_input"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="regionForm"/>