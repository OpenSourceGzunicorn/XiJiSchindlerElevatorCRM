<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/specialRequirementsAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${handoverElevatorSpecialClaimBean.scId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
    <td class="wordtd">特殊要求代码:</td>
    <td><bean:write name="handoverElevatorSpecialClaimBean" scope="request" property="scId"/>
    <html:hidden name="handoverElevatorSpecialClaimBean" property="scId" styleId="scId"/>
    </td>
  </tr>
  <tr>
    <td class="wordtd">特殊要求名称:</td>
    <td><html:text name="handoverElevatorSpecialClaimBean" property="scName" styleId="scName"/><font color="red">*</font></td>
  </tr> 
  <tr>
    <td class="wordtd"><bean:message key="hotlinemotherboardtype.enabledFlag"/>:</td>
    <td>
	 <html:radio name="handoverElevatorSpecialClaimBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="handoverElevatorSpecialClaimBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
	<td class="wordtd"><bean:message key="hotlinemotherboardtype.rem"/>:</td>
    <td colspan="3"><html:textarea name="handoverElevatorSpecialClaimBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>