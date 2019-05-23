<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<br>
<html:errors/>
<html:form action="/loginuserAction.do?method=toAddRecord">
<table width="100%" class="tb">
<html:hidden property="isreturn"/>  
  <tr>
    <td class="wordtd"><bean:message key="loginuser.userid" />:</td>
    <td><html:text property="userid" maxlength="64" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="loginuser.username"/>:</td>
    <td><html:text property="username" maxlength="127" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="loginuser.password"/>:</td>
    <td><html:password property="passwd" maxlength="127" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
   <tr>
	<td class="wordtd"><bean:message key="loginuser.class1id"/>:</td>
    <td>
	<html:select property="class1id">
      <html:options collection="class1List" property="class1id" labelProperty="class1name"/>
	</html:select><font color="red">*</font>
	</td> 
  </tr>	
    <tr><td class="wordtd">所属分部:</td>
  <td><html:select property="grcid" styleId="grcid" styleClass="default_input" onchange="setStorageidSelect('storageid')">
  <html:options collection="grcidlist" property="grcid" labelProperty="grcname"/></html:select><font color="red">*</font></td>
  </tr>
  
  <tr>
	<td class="wordtd"><bean:message key="loginuser.storageid"/>:</td>
    <td>
	<html:select property="storageid" styleId="storageid">
      <html:options collection="storageidList" property="storageid" labelProperty="storagename"/>
	</html:select><font color="red">*</font>
	</td> 
  </tr>	
  <tr>
    <td class="wordtd"><bean:message key="loginuser.roleid"/>:</td>
    <td>
  	<html:select property="roleid" styleClass="default_input">
		<html:options collection="roleList" property="roleid" labelProperty="rolename"/>
  	</html:select>
  	<font color="red">*</font>
	</td>
  </tr>
  <%--tr>
	<td class="wordtd"><bean:message key="loginuser.dutyid"/>:</td>
    <td>
	<html:select property="dutyid">
      <html:options collection="viewdutyList" property="dutyid" labelProperty="dutyname"/>
	</html:select><font color="red">*</font>
	</td> 
  </tr--%>	
  <tr>
    <td class="wordtd"><bean:message key="loginuser.phone"/>:</td>
    <td><html:text property="phone" maxlength="64" styleClass="default_input"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="loginuser.fax"/>:</td>
    <td><html:text property="fax" maxlength="64" styleClass="default_input"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="loginuser.email"/>:</td>
    <td><html:text property="email" maxlength="64" styleClass="default_input"/></td>
  </tr>
  <tr>
    <td class="wordtd">身份证号码:</td>
    <td><html:text property="idcardno" maxlength="64" styleClass="default_input"/></td>
  </tr>
  <tr>
    <td class="wordtd">手机IMEI码:</td>
    <td><html:text property="phoneimei" maxlength="64" styleClass="default_input"/></td>
  </tr>

  <tr>
    <td class="wordtd"><bean:message key="loginuser.enabledflag"/>:</td>
    <td class="inputtd"><html:radio property="enabledflag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="enabledflag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="loginuser.rem1"/>:</td>
    <td><html:textarea property="rem1" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="loginuserForm"/>