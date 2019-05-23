<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ page import="com.gzunicorn.common.util.SysConfig" %>

<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors />
<html:form action="custReturnRegisterAction.do?method=toHistoryDisplayRecord">
 <table>  
 <tr>
 	<td>&nbsp;&nbsp;回访日期 :</td>
  	<td><html:text property="operDate" size="15" styleClass="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM'})"/></td>
 </tr>
</table>
<html:hidden property="id" write="false"/>
<br/>
<table width="100%" border="0" cellspacing="1" cellpadding="1" class="tableclass" >  
	<tr class="headerRow3" align="center" height="20">
	<td width="3%"></td>
	<td><bean:message key="custreturnregister.contacts" /></td>
	<td><bean:message key="custreturnregister.contactPhone" /></td>
	<td><bean:message key="custreturnregister.operDate" /></td>
	<td><bean:message key="custreturnregister.isProblem" /></td>
	<td><bean:message key="custreturnregister.isHandleResult" /></td>	
	<td><bean:message key="custreturnregister.isHandle" /></td>
	<td><bean:message key="custreturnregister.ministerHandle" /></td>
	<td><bean:message key="custreturnregisterhand.maintDivision" /></td>	
	</tr>
	<logic:present name="custReturnRegisterHistoryList">
	
	<logic:present name="custReturnRegisterHistoryListSize">
	
	<logic:match name="custReturnRegisterHistoryListSize" value="0">
			<TR class=noItems ><TD colSpan=12>没记录显示! </TD></TR>
	</logic:match>
	<logic:notMatch name="custReturnRegisterHistoryListSize"  value="0">	
	<logic:iterate id="element" name="custReturnRegisterHistoryList">

	<tr Class='<bean:write name="element" property="r2"/>' align="center" height="20">
	<td>
	  <bean:define id="billno" name="element" property="billno" />  		
		<html:radio property="ids" styleId="ids" value="<%=billno.toString()%>" />
	</td>
	<td>
		 <a href="<html:rewrite page='/custReturnRegisterAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billno"/>&isreturn=2">
		<bean:write name="element" property="contacts" />
		</a>
	</td>
	<td><bean:write name="element" property="contactPhone" /></td>
	<td><bean:write name="element" property="operDate" /></td>
	<td>
	<logic:match name="element" property="isProblem" value="Y">
		<bean:message key="pageword.yes" />
	</logic:match> 
	<logic:match name="element" property="isProblem" value="N">
		<bean:message key="pageword.no" />
	</logic:match>
	</td>
	<td>	
	<logic:match name="element" property="returnResult" value="Y">
		<bean:message key="pageword.yes" />
	</logic:match> <logic:match name="element" property="returnResult"
		value="N">
		<bean:message key="pageword.no" />
	</logic:match>
	</td>
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
	
	<td><bean:write name="element" property="maintDivision" /></td>
	</tr>	
	
	</logic:iterate>
	
	</logic:notMatch>
	</logic:present>
	</logic:present>
</table>
</html:form>