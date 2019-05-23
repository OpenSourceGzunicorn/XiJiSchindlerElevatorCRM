<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<br>
<html:errors/>
<html:form action="/customerLevelAction.do?method=toUpdateRecord">
<html:hidden property="isreturn"/>
<html:hidden property="id" value="${customerBean.companyId}"/>
<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb">
  <tr>
    <td class="wordtd_a"><bean:message key="customerlevel.companyName"/>:</td>
    <td width="25%">
      <bean:write name="customerBean" property="companyName"/>
      <html:hidden name="customerBean" property="companyName"/>
      <html:hidden name="customerBean" property="companyId"/>
    </td>     
    <td class="wordtd_a"><bean:message key="customerlevel.principalName"/>:</td>
    <td width="20%">
      <bean:write name="customerBean" property="principalName"/>
    </td>     
    <td class="wordtd_a"><bean:message key="customerlevel.principalPhone"/>:</td>
    <td width="20%">
      <bean:write name="customerBean" property="principalPhone"/>
    </td>     
  </tr>
  <tr>
  <td class="wordtd_a">
  客户等级：
  </td>
  <td colspan="5">
  <html:select property="custLevel">
  <html:option value=""><bean:message key="pageword.pleaseselect"/></html:option>
  <html:option value="KA">KA</html:option>
  <html:option value="A">A</html:option>
  <html:option value="B">B</html:option>
  <html:option value="C">C</html:option>
  </html:select>
  </td>
  </tr>
  </table> 
  <br/>
 <table width="100%" border="0" cellpadding="0"  cellspacing="0" class="tb"> 
  <tr class=headerRow3 align="center">
  <td class="wordtd_header">维保合同号</td>
  <td class="wordtd_header"><bean:message key="customerlevel.no"/></td>
  <td class="wordtd_header"><bean:message key="customerlevel.contractTotal"/></td>
  <td class="wordtd_header"><bean:message key="customerlevel.contractEDate"/></td>
  <td class="wordtd_header"><bean:message key="customerlevel.maintDivision"/></td>
  <td class="wordtd_header"><bean:message key="customerlevel.mainStation"/></td>
  </tr>
  <logic:present name="levelSize">
	
	<logic:match name="levelSize" value="0">
			<TR class=noItems ><TD colSpan=12>没记录显示! </TD></TR>
	</logic:match>
	<logic:notMatch name="levelSize"  value="0">
		<logic:present	name="customerLevelList">
		<logic:iterate id="ele" name="customerLevelList" >
		<tr class='<bean:write name="ele" property="style" />' align="center" height="20">
		<td><a href="<html:rewrite page='/maintContractAction.do'/>?method=toDisplayRecord&typejsp=Yes&id=<bean:write name="ele" property="billNo"/>" target="_blnk"><bean:write name="ele" property="maintContractNo"/></a></td>
        <td><bean:write name="ele" property="no"/></td>
        <td><bean:write name="ele" property="contractTotal"/></td>
        <td><bean:write name="ele" property="contractEDate"/></td>
        <td><bean:write name="ele" property="maintDivision"/></td>
        <td><bean:write name="ele" property="assignedMainStation"/></td>
		</tr>
		</logic:iterate>
		</logic:present>
    </logic:notMatch>
    </logic:present>

</table>


</html:form>