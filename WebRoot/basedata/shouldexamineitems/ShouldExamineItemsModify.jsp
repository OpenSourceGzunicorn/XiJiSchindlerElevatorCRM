<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/shouldExamineItemsAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${shouldExamineItemsBean.seiid}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="shouldexamineitems.seiid"/>:</td>
    <td>
      <bean:write name="shouldExamineItemsBean" property="seiid"/>
      <html:hidden name="shouldExamineItemsBean" property="seiid"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="shouldexamineitems.seiDetail"/>:</td>
    <td><html:textarea name="shouldExamineItemsBean" property="seiDetail" rows="2" cols="60" styleClass="default_textarea"/><font color="red">*</font></td>
  <tr>
    <td class="wordtd"><bean:message key="termsecurityrisks.enabledflag"/>:</td>
    <td>
	  <html:radio name="shouldExamineItemsBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="shouldExamineItemsBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="termsecurityrisks.rem"/>:</td>
    <td><html:textarea name="shouldExamineItemsBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="shouldExamineItemsForm"/>