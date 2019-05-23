<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/hotlineFaultClassificationAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${hotlineFaultClassificationBean.hfcId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="hotlinefaultclassification.hfcId"/>:</td>
    <td>
      <bean:write name="hotlineFaultClassificationBean" property="hfcId"/>
      <html:hidden name="hotlineFaultClassificationBean" property="hfcId"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="hotlinefaultclassification.hfcName"/>:</td>
    <td><html:text name="hotlineFaultClassificationBean" property="hfcName" size="50" styleClass="default_input"/><font color="red">*</font></td>
  </tr>   
  <tr>
    <td class="wordtd"><bean:message key="hotlinefaultclassification.enabledFlag"/>:</td>
    <td>
	  <html:radio name="hotlineFaultClassificationBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="hotlineFaultClassificationBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="hotlinefaultclassification.rem"/>:</td>
    <td><html:textarea name="hotlineFaultClassificationBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="hotlineFaultClassificationForm"/>