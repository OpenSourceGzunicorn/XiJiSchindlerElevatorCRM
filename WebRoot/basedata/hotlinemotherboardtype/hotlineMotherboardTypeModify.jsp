<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/hotlineMotherboardTypeAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${hotlineMotherboardTypeBean.hmtId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="hotlinemotherboardtype.hmtId"/>:</td>
    <td>
      <bean:write name="hotlineMotherboardTypeBean" property="hmtId"/>
      <html:hidden name="hotlineMotherboardTypeBean" property="hmtId"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="hotlinemotherboardtype.hmtName"/>:</td>
    <td><html:text name="hotlineMotherboardTypeBean" property="hmtName" size="50" styleClass="default_input"/><font color="red">*</font></td>
  </tr>   
  <tr>
    <td class="wordtd"><bean:message key="hotlinemotherboardtype.enabledFlag"/>:</td>
    <td>
	  <html:radio name="hotlineMotherboardTypeBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="hotlineMotherboardTypeBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="hotlinemotherboardtype.rem"/>:</td>
    <td><html:textarea name="hotlineMotherboardTypeBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="hotlineMotherboardTypeForm"/>