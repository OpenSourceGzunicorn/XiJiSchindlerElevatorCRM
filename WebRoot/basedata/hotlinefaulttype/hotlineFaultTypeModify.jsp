<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/hotlineFaultTypeAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${hotlineFaultTypeBean.hftId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="hotlinefaulttype.hftId"/>:</td>
    <td>
      <bean:write name="hotlineFaultTypeBean" property="hftId"/>
      <html:hidden name="hotlineFaultTypeBean" property="hftId"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="hotlinefaulttype.hftDesc"/>:</td>
    <td><html:text name="hotlineFaultTypeBean" property="hftDesc" size="50" styleClass="default_input"/><font color="red">*</font></td>
  </tr>   
  <tr>
    <td class="wordtd"><bean:message key="hotlinefaulttype.enabledflag"/>:</td>
    <td>
	  <html:radio name="hotlineFaultTypeBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="hotlineFaultTypeBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="hotlinefaulttype.rem"/>:</td>
    <td><html:textarea name="hotlineFaultTypeBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="hotlineFaultTypeForm"/>