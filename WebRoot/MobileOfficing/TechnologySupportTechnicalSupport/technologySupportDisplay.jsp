<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<logic:present name="technologySupportBean">
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
    <td class="wordtd"><bean:message key="technologySupport.maintDivision"/>:</td>
    <td style="width: 30%"><bean:write name="technologySupportBean" scope="request" property="maintDivision"/></td>
    <td class="wordtd"><bean:message key="technologySupport.maintStation"/>:</td>
    <td style="width: 30%"><bean:write name="technologySupportBean" scope="request" property="maintStation"/></td>
 </tr>
  <tr>
  <td class="wordtd">¼±ÐÞ±àºÅ:</td>
  <td><bean:write name="technologySupportBean" scope="request" property="singleNo"/></td>
  <td class="wordtd">ÉêÇëÈÕÆÚ:</td>
   <td><bean:write name="technologySupportBean" scope="request" property="operDate"/></td>
  </tr>
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.assignUser"/>:</td>
    <td><bean:write name="technologySupportBean"scope="request" property="assignUser"/></td>
    <td class="wordtd"><bean:message key="technologySupport.assignUserTel"/>:</td>
    <td><bean:write name="technologySupportBean" scope="request" property="assignUserTel"/></td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.hmtId"/>:</td>
    <td><bean:write name="technologySupportBean" scope="request" property="hmtId"/></td>
    <td class="wordtd"><bean:message key="technologySupport.faultCode"/>:</td>
    <td><bean:write name="technologySupportBean" scope="request" property="faultCode"/></td>
 </tr>
 <tr>
  <td class="wordtd"><bean:message key="technologySupport.faultStatus"/>:</td>
  <td colspan="3"><textarea rows="2" cols="100" class="default_textarea_noborder" readonly="readonly"><bean:write name="technologySupportBean" scope="request" property="faultStatus"/></textarea></td>
 </tr>
 <tr>
 <% int i=1; %>
 <td class="wordtd">¸½¼þ:</td><td colspan="3">
 <logic:present name="fileSidList">
 <logic:iterate id="ele" name="fileSidList">
 <a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('${ele.fileSid}')">¸½¼þ<%=i%></a>&nbsp;&nbsp; <%i++; %>
 </logic:iterate>
 </logic:present>
 </td>
 </tr>
 </table>
 <br/>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
 <tr>
    <td class="wordtd" ><bean:message key="technologySupport.msProcessPeople"/>:</td>
    <td style="width: 30%"><bean:write name="technologySupportBean" scope="request" property="msprocessPeople"/></td>
    <td class="wordtd"  ><bean:message key="technologySupport.msIsResolve"/>:</td>
    <td style="width: 30%" > 
    <bean:write name="technologySupportBean" scope="request" property="msisResolve"/>
    </td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.msProcessRem"/>:</td>
    <td colspan="3" ><textarea rows="2" cols="100" class="default_textarea_noborder" readonly="readonly"><bean:write name="technologySupportBean" scope="request" property="msprocessRem"/></textarea></td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.msProcessDate"/>:</td>
    <td colspan="3" ><bean:write name="technologySupportBean" scope="request" property="msprocessDate"/></td>
 </tr>
  </table>
 <br/>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
 <tr>
    <td class="wordtd" ><bean:message key="technologySupport.mmProcessPeople"/>:</td>
    <td style="width: 30%"><bean:write name="technologySupportBean" scope="request" property="mmprocessPeople"/></td>
    <td class="wordtd" ><bean:message key="technologySupport.mmIsResolve"/>:</td>
    <td style="width: 30%" ><bean:write name="technologySupportBean" scope="request" property="mmisResolve"/></td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.mmProcessRem"/>:</td>
    <td colspan="3" ><textarea rows="2" cols="100" class="default_textarea_noborder" readonly="readonly"><bean:write name="technologySupportBean" scope="request" property="mmprocessRem"/></textarea></td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.msProcessDate"/>:</td>
    <td colspan="3" ><bean:write name="technologySupportBean" scope="request" property="mmprocessDate"/></td>
 </tr>
 </table>
 <br/>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
 <tr>
    <td class="wordtd" ><bean:message key="technologySupport.tsProcessPeople"/>:</td>
    <td style="width: 30%"><bean:write name="technologySupportBean" scope="request" property="tsprocessPeople"/></td>
    <td class="wordtd" ><bean:message key="technologySupport.tsProcessDate"/>:</td>
    <td style="width: 30%"><bean:write name="technologySupportBean" scope="request" property="tsprocessDate"/></td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.tsProcessRem"/>:</td>
    <td colspan="3" ><textarea rows="2" cols="100" class="default_textarea_noborder" readonly="readonly"><bean:write name="technologySupportBean" scope="request" property="tsprocessRem"/></textarea></td>
 </tr>
 </table>
</logic:present>