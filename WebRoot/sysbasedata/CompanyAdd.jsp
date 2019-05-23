<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>

<br>
<html:errors/>
<html:form action="/companyAction.do?method=toAddRecord">
<html:hidden property="isreturn"/>
<table width="100%" class="tb">
  <tr>
    <td width="20%" class="wordtd"><bean:message key="company.comid"/>:</td>
    <td width="80%"><html:text property="comid" maxlength="64" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="company.comname"/>:</td>
    <td><html:text property="comname" maxlength="127" size="50" styleClass="default_input"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="company.comfullname"/>:</td>
    <td><html:text property="comfullname" maxlength="127" size="50" styleClass="default_input"/></td>
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="company.comtyped"/>:</td>
    <td>
	<html:select property="comtype">
	  <html:option value=""><bean:message key="pageword.pleaseselect"/></html:option> 
      <html:options collection="viewcompanytypeList" property="comtypeid" labelProperty="comtypename"/>
	</html:select><font color="red">*</font>
	</td> 
  </tr>	
  <tr>
    <td class="wordtd"><bean:message key="company.linkman"/>:</td>
    <td><html:text property="linkman" maxlength="32" styleClass="default_input"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="company.linkmantel"/>:</td>
    <td><html:text property="linkmantel" maxlength="64" styleClass="default_input"/></td>
  </tr>

  <tr>
    <td class="wordtd"><bean:message key="company.address"/>:</td>
    <td><html:text property="address" maxlength="127" size="50" styleClass="default_input"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="company.enabledflag"/>:</td>
    <td class="inputtd"><html:radio property="enabledflag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="enabledflag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="company.rem"/>:</td>
    <td><html:textarea property="rem" rows="3" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="companyForm"/>