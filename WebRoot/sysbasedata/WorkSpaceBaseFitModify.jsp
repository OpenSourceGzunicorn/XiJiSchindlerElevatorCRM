<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<br>
<html:errors/>
<html:form action="/workSpaceBaseFitAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id"/>
<table width="99%" border="0" align="center" cellpadding="2" cellspacing="0" class="tb">
  
  <tr>
    <td width="15%" class="wordtd"><bean:message key="workspacebasefit.wskey" />£∫</td>
    <td width="35%"><html:text property="wskey" styleClass="default_input" size="64" maxlength="64" /><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="randommaintaskmaster.title"/>:</td>
    <td><html:text property="title" styleClass="default_input" size="64" maxlength="64" /><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="workspacebasefit.link"/>:</td>
    <td><html:text property="link" styleClass="default_input" size="64" maxlength="64" /><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="workspacebasefit.tip" />:</td>
    <td><html:text property="tip" styleClass="default_input" size="64" maxlength="64" /><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="DivId" />:</td>
    <td><html:text property="divid" styleClass="default_input" size="64" maxlength="64" /><font color="red">*</font></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="JSfuName" />:</td>
    <td><html:text property="jsfuname" styleClass="default_input" size="64" maxlength="64" /><font color="red">*</font></td>
  </tr>
  <tr>
    <td width="15%" class="wordtd">≈≈–Ú∫≈£∫</td>
    <td width="35%"> <html:text property="numno" styleClass="default_input" size="64" maxlength="64"/></td>
  </tr>
  <tr>        
    <td width="15%" class="wordtd"><bean:message key="workspacebasefit.enabledflag" />£∫</td>
    <td width="35%" class="inputtd"><html:radio property="enabledflag" value="Y"/>
        <bean:message key="enabledflag.yes"/>
      <html:radio property="enabledflag" value="N"/>
      <bean:message key="enabledflag.no"/></td>
  </tr>
  </table>
</html:form>
<html:javascript formName="workSpaceBaseFitForm"/>