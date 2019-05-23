<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/technologySupportMaintStationAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${technologySupportBean.billno}"/>
<html:hidden name="technologySupportBean" property="billno"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
   <tr>
    <td class="wordtd"><bean:message key="technologySupport.maintDivision"/>:</td>
    <td style="width: 30%"><bean:write name="technologySupportBean" scope="request" property="maintDivision"/></td>
    <td class="wordtd"><bean:message key="technologySupport.maintStation"/>:</td>
    <td><bean:write name="technologySupportBean" scope="request" property="maintStation"/></td>
 </tr>
 <tr>
  <td class="wordtd">急修编号:</td>
  <td style="width: 30%"><bean:write name="technologySupportBean" scope="request" property="singleNo"/></td>
  <td class="wordtd">电梯编号:</td>
  <td><bean:write name="technologySupportBean" scope="request" property="elevatorNo"/></td>
  </tr>
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.assignUser"/>:</td>
    <td style="width: 30%"><bean:write name="technologySupportBean"scope="request" property="assignUser"/></td>
    <td class="wordtd"><bean:message key="technologySupport.assignUserTel"/>:</td>
    <td><bean:write name="technologySupportBean" scope="request" property="assignUserTel"/></td>
 </tr>
 <tr>
  <td class="wordtd">申请日期:</td>
   <td style="width: 30%"><bean:write name="technologySupportBean" scope="request" property="operDate"/></td>
    <td class="wordtd"><bean:message key="technologySupport.hmtId"/>:</td>
    <td style="width: 30%"><bean:write name="technologySupportBean" scope="request" property="hmtId"/></td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.faultCode"/>:</td>
    <td style="width: 30%"><bean:write name="technologySupportBean" scope="request" property="faultCode"/></td>
  <td class="wordtd"><bean:message key="technologySupport.faultStatus"/>:</td>
  <td ><bean:write name="technologySupportBean" scope="request" property="faultStatus"/></td>
 </tr>

 <tr>
 <td class="wordtd">故障图片:</td>
 <td colspan="3">
 <a style="cursor:hand;text-decoration: underline;color: blue;" onclick="downloadFile('<bean:write name="technologySupportBean" property="billno"/>')">查看</a>&nbsp;&nbsp;
 </td>
 </tr>
 </table>
 
 
 <br/>
 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.msProcessPeople"/>:</td>
    <td style="width: 30%"><bean:write name="technologySupportBean" scope="request" property="msprocessPeople"/></td>
    <td class="wordtd"><bean:message key="technologySupport.msIsResolve"/>:</td>
    <td style="width: 30%"> <html:select name="technologySupportBean" property="msisResolve">
          <html:option value=""><bean:message key="pageword.pleaseselect"/></html:option>
          <html:option value="Y">已解决</html:option>
          <html:option value="N">未解决</html:option>
         </html:select>
    </td>
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.msProcessRem"/>:</td>
    <td colspan="3" ><html:textarea name="technologySupportBean" property="msprocessRem" styleId="msprocessRem" rows="3" cols="100"  styleClass="default_textarea"></html:textarea></td>
    
 </tr>
 <tr>
    <td class="wordtd"><bean:message key="technologySupport.msProcessDate"/>:</td>
    <td colspan="3" ><bean:write name="technologySupportBean" scope="request" property="msprocessDate"/>
    <html:hidden name="technologySupportBean" property="msprocessDate"/></td>
 </tr>
  </table>
 </html:form>