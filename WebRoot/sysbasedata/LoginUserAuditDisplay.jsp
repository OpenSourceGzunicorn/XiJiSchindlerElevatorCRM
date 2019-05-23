<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<br>
<html:errors/>

<html:form action="/loginUserAuditAction.do?method=toAuditRecord">
<html:hidden property="isreturn" styleId="isreturn"/>
<logic:present name="loginuserBean">
<table width="100%" class="tb"> 
  <tr>
    <td width="25%" class="wordtd"><bean:message key="loginuser.userid" />:</td>
    <td>
    	<html:hidden name="loginuserBean" property="userid" write="true"/>
    </td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="loginuser.username"/>:</td>
    <td><bean:write name="loginuserBean" property="username" scope="request" /></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="loginuser.class1id"/>:</td>
    <td>
  	<bean:write name="loginuserBean" property="class1id" scope="request"/>
	</td>
  </tr>
    <tr>
  <td class="wordtd">所属分部:</td>
  <td>${loginuserBean.grcid}</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="loginuser.storageid"/>:</td>
    <td>
  	<bean:write name="loginuserBean" property="storageid" scope="request"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="loginuser.roleid"/>:</td>
    <td>
  	<bean:write name="loginuserBean" property="roleid" scope="request"/>
	</td>
  </tr>
  <%--tr>
    <td class="wordtd"><bean:message key="loginuser.dutyid"/>:</td>
    <td>
  	<bean:write name="loginuserBean" property="dutyid" scope="request"/>
	</td>
  </tr--%>

  <tr>
    <td class="wordtd"><bean:message key="loginuser.fax"/>:</td>
    <td>
  	<bean:write name="loginuserBean" property="fax" scope="request"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd">手机IMEI码:</td>
    <td><bean:write name="loginuserBean" property="phoneimei" scope="request"/></td>

  </tr>
    <tr>
    <td class="wordtd"><bean:message key="loginuser.enabledflag"/>:</td>
    <td class="inputtd">
	<logic:match property="enabledflag" name="loginuserBean" value="Y">
	<bean:message key="pageword.yes"/>
	</logic:match>
	<logic:match property="enabledflag" name="loginuserBean" value="N">
	<bean:message key="pageword.no"/>
	</logic:match>
   </td>
  </tr>
    <tr>
    <td class="wordtd"><bean:message key="loginuser.rem1"/>:</td>
    <td><bean:write property="rem1" name="loginuserBean" scope="request"/></td>
  </tr>
  </table>
  </br>
  </br>
  <table width="100%" class="tb"> 
  <tr height="23">
  <td colspan="4"><b>审核信息</b></td>
  </tr>
    <tr>
    <td width="25%" class="wordtd">旧<bean:message key="loginuser.phone"/>:</td>
    <td width="35%">
  	<bean:write name="loginuserBean" property="phone" scope="request"/>
	</td>
	    <td width="25%" class="wordtd">新电话号码:</td>
    <td >
  	<bean:write name="loginuserBean" property="newphone" scope="request"/>
	</td>
  </tr>
    <tr>
    <td width="25%" class="wordtd">旧<bean:message key="loginuser.email"/>:</td>
    <td width="35%">
  	<bean:write name="loginuserBean" property="email" scope="request"/>
	</td>
	   <td width="25%" class="wordtd">新电子邮件:</td>
    <td >
  	<bean:write name="loginuserBean" property="newemail" scope="request"/>
	</td>
  </tr>
  <tr>
    <td width="25%" class="wordtd">旧身份证号码:</td>
    <td width="35%"><bean:write name="loginuserBean" property="idcardno" scope="request"/></td>
     <td  width="25%" class="wordtd">新身份证号码:</td>
    <td ><bean:write name="loginuserBean" property="nweidcardno" scope="request"/></td>
  </tr>
  </table>
  </br>
<table class="dynamictable tb" width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
		    <td class="wordtd" width="25%">审核人</td>
			<td width="35%">${loginuserBean.auditoperid }</td> 
			<td class="wordtd" width="25%">审核日期</td>
			<td >${loginuserBean.auditdate }</td>
		</tr>
				
</table>
</logic:present>
</html:form>
