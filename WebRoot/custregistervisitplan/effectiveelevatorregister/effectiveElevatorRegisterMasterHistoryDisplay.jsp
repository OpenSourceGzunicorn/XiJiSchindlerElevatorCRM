<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/effectiveElevatorRegisterMasterAction.do">
<table width="100%" border="0" cellspacing="1" cellpadding="1" class="tableclass" >  
	<tr class="headerRow3" align="center" height="20">
	<td></td>
	<td><bean:message key="custreturnregister.companyName" /></td>
	<td><bean:message key="custreturnregister.contacts" /></td>
	<td><bean:message key="custreturnregister.contactPhone" /></td>
	<td><bean:message key="custreturnregister.operDate" /></td>
	<%-- <td><bean:message key="custreturnregister.isProblem" /></td>
	<td><bean:message key="custreturnregister.isHandleResult" /></td> --%>	
	<td><bean:message key="custreturnregister.isHandle" /></td>
	<td><bean:message key="custreturnregister.ministerHandle" /></td>
	<%-- <td><bean:message key="custreturnregisterhand.maintDivision" /></td> --%>	
	</tr>
	<logic:present name="custReturnRegisterHistoryList">
	
	<logic:present name="custReturnRegisterHistoryListSize">
	
	<logic:match name="custReturnRegisterHistoryListSize" value="0">
			<TR class=noItems ><TD colSpan=11>Ã»¼ÇÂ¼ÏÔÊ¾! </TD></TR>
	</logic:match>
	<logic:notMatch name="custReturnRegisterHistoryListSize"  value="0">	
	<logic:iterate id="element" name="custReturnRegisterHistoryList">

	<tr Class='<bean:write name="element" property="r2"/>' align="center" height="20">
	<td>
	  <bean:define id="billno" name="element" property="billno" />  		
		<html:radio property="ids" styleId="ids" value="<%=billno.toString()%>" />
	</td>
	<td>
		 <a href="<html:rewrite page='/effectiveElevatorRegisterMasterAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billno"/>&isreturn=2">
		<bean:write name="element" property="companyName" />
		</a>
	</td>
	<td><bean:write name="element" property="contacts" /></td>
	<td><bean:write name="element" property="contactPhone" /></td>
	<td><bean:write name="element" property="operDate" /></td>
	<td>
	<logic:match name="element" property="handleId" value="Y">
		<bean:message key="pageword.yes" />
	</logic:match>
	<logic:match name="element" property="handleId" value="N">
		<bean:message key="pageword.no" />
	</logic:match>
	</td>
	<td>
	<logic:match name="element" property="ministerHandle" value="Y">
		<bean:message key="pageword.yes" />
	</logic:match> <logic:match name="element" property="ministerHandle"
		value="N">
		<bean:message key="pageword.no" />
	</logic:match>
	</td>
	
	<%-- <td><bean:write name="element" property="maintDivision" /></td> --%>
	</tr>	
	
	</logic:iterate>
	
	</logic:notMatch>
	</logic:present>
	</logic:present>
</table>
</html:form>