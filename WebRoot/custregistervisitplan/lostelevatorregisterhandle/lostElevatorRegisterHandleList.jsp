<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/lostElevatorRegisterHandleAction.do">
	<table>
		<tr>
		    <td>
				&nbsp;&nbsp;
				 <bean:message key="custreturnregister.companyName" /> 
				:
			</td>
			<td>
			    <html:text property="companyName" size="35" styleClass="default_input" ></html:text>			
			</td>		
			<td>
				&nbsp;&nbsp;
				合同联系人
				:
			</td>
			<td>
				<html:text property="contacts" styleClass="default_input" ></html:text>
			</td>
			<td>
				&nbsp;&nbsp;
				合同联系人电话
				:
			</td>
			<td>
				<html:text property="contactPhone" styleClass="default_input" ></html:text>
			</td>
		</tr>
		<tr>
			<%-- <td>
				&nbsp;&nbsp;
				 <bean:message key="custreturnregister.isProblem" />
				:
			</td>
			<td>
				<html:select property="isProblem">
					<html:option value="">
						<bean:message key="pageword.all" />
					</html:option>
					<html:option value="Y">
						<bean:message key="pageword.yes" />
					</html:option>
					<html:option value="N">
						<bean:message key="pageword.no" />
					</html:option>
				</html:select>
			</td> --%>
						
			<td>
				&nbsp;&nbsp;
				<bean:message key="custreturnregister.maintDivision" />
				:
			</td>
			<td>
				<html:select property="maintDivision">
		        <html:option value=""><bean:message key="pageword.all"/></html:option> 
		        <html:options collection="maintDivisionList" property="comId" labelProperty="comFullName"/>
                </html:select>
			</td>
			<html:hidden property="genReport" styleId="genReport" />
		</tr>
	</table>
	<br >
	<table class=tableclass cellSpacing=1 cellPadding=1 width="100%" border=0  id="table1">  
	<tr class=headerRow3>
	<td width=25 rowspan="1" colspan="1"></td>		
	<td rowspan="1" colspan="1"  style="text-align: center;"><bean:message key="custreturnregister.reOrder" /></td>
	<td rowspan="1" colspan="1" style="text-align: center;"><bean:message key="custreturnregister.reMark" /></td>
	<td rowspan="1" colspan="1" style="text-align: center;"><bean:message key="custreturnregister.companyName" /></td>
	<td rowspan="1" colspan="1" style="text-align: center;"><bean:message key="custreturnregister.contacts" /></td>
	<td rowspan="1" colspan="1" style="text-align: center;"><bean:message key="custreturnregister.contactPhone" /></td>
	<td rowspan="1" colspan="1" style="text-align: center;"><bean:message key="custreturnregister.maxoperDate" /></td>
	<%-- <td rowspan="1" colspan="1" style="text-align: center;"><bean:message key="custreturnregister.isProblem" /></td>
	<td rowspan="1" colspan="1" style="text-align: center;"><bean:message key="custreturnregister.isHandleResult" /></td> --%>
	<td rowspan="1" colspan="1" style="text-align: center;"><bean:message key="custreturnregister.isHandle" /></td>
	<td rowspan="1" colspan="1" style="text-align: center;"><bean:message key="custreturnregister.ministerHandle"/></td>		
	<td rowspan="1" colspan="1" style="text-align: center;"><bean:message key="custreturnregisterhand.maintDivision" /></td>
	<td rowspan="1" colspan="1" style="text-align: center;"><bean:message key="custreturnregister.rem" /></td>	
	</tr>
	<logic:present name="custReturnRegisterListSize">
	
	<logic:match name="custReturnRegisterListSize" value="0">
			<TR class=noItems ><TD colSpan=13>没记录显示! </TD></TR>
	</logic:match>
	<logic:notMatch name="custReturnRegisterListSize"  value="0">
				<logic:present name="custReturnRegisterList">
	<logic:iterate id="element" name="custReturnRegisterList">
	
	
	<tr class='<bean:write name="element" property="style" />' align="center" height="20">
	<td >
	  <bean:define id="companyId" name="element" property="companyId" />		
		<html:radio property="ids" styleId="ids" value="<%=companyId.toString()%>" />
		<input type="hidden" name="ishandleValue" value='<bean:write name="element" property="handleId"/>'>		
		<input type="hidden" name="toReturnResult" value='<bean:write name="element"  property="billno"/>'>
		<input type="hidden" name="isReturnResult" value='<bean:write name="element"  property="returnResult"/>'>	
	</td>
	
	<td>
	    <logic:empty name="element" property="billno" >
		<bean:write name="element" property="reOrder" />
	  </logic:empty> 
	  <logic:notEmpty name="element" property="billno">
		<%-- <a
			href="<html:rewrite page='/custReturnRegisterAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billno"/>"> --%>
			<bean:write name="element" property="reOrder" />
		<!-- </a> -->
	  </logic:notEmpty>
	  </td>
	 <td><logic:match name="element" property="reMark" value="Y">
					<bean:message key="pageword.yes" />
	</logic:match>
	<logic:match name="element" property="reMark" value="N">
					<bean:message key="pageword.no" />
	</logic:match></td>
    <td>		 
		<bean:write name="element" property="companyName" />
	</td>
	<td><bean:write name="element" property="contacts" /></td>
	<td><bean:write name="element" property="contactPhone" /></td>
	<td><bean:write name="element" property="operDate" /></td>
	<%-- <td><bean:write name="element" property="isProblem" /></td>
	<td><bean:write name="element" property="returnResult" /></td> --%>
	<td><bean:write name="element" property="handleId" /> </td>
	<td><bean:write name="element" property="ministerHandle" /></td>
	<td><bean:write name="element" property="maintDivision" /></td>
	<td align="left"><bean:write name="element" property="rem" /></td>
	</tr>	
	</logic:iterate>
	</logic:present>

	</logic:notMatch>
</logic:present>
	

	</table>

</html:form>