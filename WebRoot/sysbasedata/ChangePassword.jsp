<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/changePasswordAction.do?method=toModifyRecord">
<table width="100%" class="tb">
  <tr>
    <td width="20%" class="wordtd"><bean:message key="changepassword.userid" />:</td>
    <td width="80%"><bean:write name="USER_INFO" scope="session" property="userID"/></td>
  </tr>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="changepassword.username" />:</td>
    <td width="80%"><bean:write name="USER_INFO" scope="session" property="userName"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="changepassword.oldpasswd"/>:</td>
    <td><html:password property="oldpasswd" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="changepassword.passwd"/>:</td>
    <td>
  	<html:password property="passwd" styleClass="default_input" /><font color="red">*</font>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="changepassword.repeatpasswd"/>:</td>
    <td>
    <html:password property="repeatpasswd" styleClass="default_input"/><font color="red">*</font>
    </td>
  </tr>
</table>
</html:form>
<html:javascript formName="changePasswordForm"/>
