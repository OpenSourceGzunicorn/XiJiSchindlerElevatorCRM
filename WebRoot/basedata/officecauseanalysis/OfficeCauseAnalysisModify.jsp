<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/officeCauseAnalysisAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${officeCauseAnalysisBean.ocaId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd"><bean:message key="officecauseanalysis.ocaId"/>:</td>
    <td>
      <bean:write name="officeCauseAnalysisBean" property="ocaId"/>
      <html:hidden name="officeCauseAnalysisBean" property="ocaId"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd"><bean:message key="officecauseanalysis.ocaName"/>:</td>
    <td><html:text name="officeCauseAnalysisBean" property="ocaName" size="60" styleClass="default_input"/><font color="red">*</font></td>
  <tr>
    <td class="wordtd"><bean:message key="officecauseanalysis.enabledflag"/>:</td>
    <td>
	  <html:radio name="officeCauseAnalysisBean" property="enabledFlag" value="Y"/><bean:message key="pageword.yes"/>
      <html:radio name="officeCauseAnalysisBean" property="enabledFlag" value="N"/><bean:message key="pageword.no"/>
	</td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="officecauseanalysis.rem"/>:</td>
    <td><html:textarea name="officeCauseAnalysisBean" property="rem" rows="5" cols="50" styleClass="default_textarea"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="officeCauseAnalysisForm"/>