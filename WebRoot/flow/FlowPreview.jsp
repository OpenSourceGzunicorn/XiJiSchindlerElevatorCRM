<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<br>
<html:errors/>
<html:form action="/loginUserAction.do?method=toUpdateRecord">
<table width="100%" class="tb">
<html:hidden property="isreturn"/>  
  <tr>
    <td width="15%" class="wordtd"><bean:message key="loginuser.userid" />£º</td>
    <td width="85%">
	<html:hidden property="id" />
	<html:text property="userid" styleClass="default_input" /><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="loginuser.username"/>£º</td>
    <td><html:text property="username" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="loginuser.password"/>£º</td>
    <td><html:hidden property="hidpwd"/><html:password property="passwd" styleClass="default_input" styleId="passwd" disabled="true"/>&nbsp;<input type="checkbox" name="pwdbox" onclick="modpwd()"><bean:message key="toolbar.modify"/></td>
  </tr>
  
  
  <html:hidden property="deptid" value="01"/>
  <html:hidden property="areaid" value="100"/>
  <html:hidden property="deflanguage" value="zh_CN"/>

  
  <tr>
    <td class="wordtd"><bean:message key="loginuser.roleid"/>£º</td>
    <td>
  	<html:select property="roleid" styleClass="default_input">
		<html:options collection="roleList" property="id.listid" labelProperty="listname"/>
  	</html:select>
	</td>
  </tr>
  <tr>
    <td class="wordtd">Ö°Î»£º</td>
    <td>
  	<html:select property="class1" styleClass="default_input">
		<html:options collection="ClassList" property="id.listid" labelProperty="listname"/>
  	</html:select>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="loginuser.enabledflag"/>£º</td>
    <td class="inputtd"><html:radio property="enabledflag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="enabledflag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="loginuser.login"/>£º</td>
    <td class="inputtd"><html:radio property="loginflag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="loginflag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
 
  
  <tr>
    <td class="wordtd"><bean:message key="loginuser.rem1"/>£º</td>
    <td><html:textarea property="rem1" cols="50" styleClass="default_input"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="loginUserForm"/>
<script>
function modpwd(){
//alert(document.loginUserForm.pwdbox.checked);

	if(document.loginUserForm.pwdbox.checked==true){
		document.loginUserForm.passwd.disabled=false;
		document.loginUserForm.passwd.value="";
	}else{
	//alert(document.loginUserForm.passwd.value);
		document.loginUserForm.passwd.value="";
		document.loginUserForm.passwd.disabled=true;
	}
}
</script>