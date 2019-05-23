<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<br>
<html:errors/>
<html:form action="/Class1Action.do?method=toUpdateClass1">
<html:hidden property="isreturn"/>
<table width="100%" class="tb">
  
  <tr>
    <td width="20%" class="wordtd"><bean:message key="customer.placeid"/>:</td>
    <td width="80%"><html:hidden property="id"/><html:text property="class1id" maxlength="255" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="customer.placename"/>:</td>
    <td width="80%"><html:text property="class1name" maxlength="255" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  <tr>
    <td width="20%" class="wordtd"><bean:message key="customer.placelevel"/>:</td>
    <td width="80%"><html:text property="levelid" maxlength="255" styleClass="default_input"/><font color="red">*</font></td>
  </tr>
  
  <tr>
    <td class="wordtd"><bean:message key="customer.isVisit"/>:</td>
    <td class="inputtd"><html:radio property="r1" value="Y"/><bean:message key="pageword.yes"/><html:radio property="r1" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.placestart"/>:</td>
    <td class="inputtd"><html:radio property="enabledflag" value="Y"/><bean:message key="pageword.yes"/><html:radio property="enabledflag" value="N"/><bean:message key="pageword.no"/></td>
  </tr>
  <tr>
    <td class="wordtd"><bean:message key="customer.placereml"/>:</td>
    <td><html:textarea property="rem1" rows="5" cols="50"/></td>
  </tr>
</table>
</html:form>
<html:javascript formName="class1Form"/>