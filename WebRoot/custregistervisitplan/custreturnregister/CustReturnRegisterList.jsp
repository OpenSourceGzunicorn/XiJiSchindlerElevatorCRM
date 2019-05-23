<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/ServeTable.do">
	<table>
		<tr>	
			<td>
				&nbsp;&nbsp;合同联系人
				:
			</td>
			<td>
				<html:text property="property(contacts)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;合同联系人电话
				:
			</td>
			<td>
				<html:text property="property(contactPhone)" styleClass="default_input" />
			</td>
				
			<td>&nbsp;&nbsp;&nbsp;&nbsp;合同号
				:
			</td>
			<td>
				<html:text property="property(selmaintContractNo)" styleClass="default_input"/>
			</td>
			<td>
				&nbsp;&nbsp;回访标记
				:
			</td>
			<td>
				<html:select property="property(reMark)">
					<html:option value="Y">
						<bean:message key="pageword.yes" />
					</html:option>
					<html:option value="N">
						<bean:message key="pageword.no" />
					</html:option>
				</html:select>
			</td>
			</tr><tr>
			<td>
				&nbsp;&nbsp;<bean:message key="custreturnregister.isProblem" />
				:
			</td>
			<td>
				<html:select property="property(isProblem)">
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
			</td>
			<td>
				&nbsp;&nbsp;<bean:message key="custreturnregister.isHandle" />
				:
			</td>
			<td>
				<html:select property="property(handleId)">
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
			</td>
			<td>
				&nbsp;&nbsp;<bean:message key="custreturnregister.maintDivision" />
				:
			</td>
			<td>
				<html:select property="property(maintDivision)">
		        <html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
                </html:select>
			</td>
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<!-- <div style="height:380px;overflow:scroll;"> -->
	<table:table id="guiCustReturnRegister" name="custReturnRegisterList">
		<logic:iterate id="element" name="custReturnRegisterList">
			<table:define id="c_cb">
			 	<bean:define id="billno1" name="element" property="billno1" />  
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=billno1.toString()%>" />
				<input type="hidden" name="ishandleValue" value='<bean:write name="element" property="handleId"/>'>		
				<input type="hidden" name="toReturnResult" value='<bean:write name="element"  property="billno"/>'>
				<input type="hidden" name="isReturnResult" value='<bean:write name="element"  property="returnResult"/>'>	
			    <input type="hidden" name="contactPhone2" id="contactPhone2" value="${element.contactPhone }" >
			    <input type="hidden" name="operDate" value='<bean:write name="element"  property="operDate"/>'>	
			    <input type="hidden" name="isProblemkk" value='<bean:write name="element"  property="isProblem"/>'>	
			</table:define>  
			<table:define id="c_reOrder">
				<logic:empty name="element" property="billno" >
					<bean:write name="element" property="reOrder" />
				</logic:empty> 
				<logic:notEmpty name="element" property="billno">
					<a href="<html:rewrite page='/custReturnRegisterAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billno"/>">
						<bean:write name="element" property="reOrder" />
					</a>
				</logic:notEmpty>
			</table:define> 
			<table:define id="c_reMark">
				<logic:equal name="element" property="reMark" value="Y">
					<bean:message key="pageword.yes" />
				</logic:equal>
				<logic:equal name="element" property="reMark" value="N">
					<bean:message key="pageword.no" />
				</logic:equal>
				<html:hidden name="element" property="reMark"/>
			</table:define> 
			<table:define id="c_contacts">
				<bean:write name="element" property="contacts" />
			</table:define>
			<table:define id="c_contactPhone">
				<bean:write name="element" property="contactPhone" />
			</table:define>
			<table:define id="c_maxoperDate">
				<bean:write name="element" property="operDate" />
			</table:define>
			<table:define id="c_isProblem">
				<bean:write name="element" property="isProblemName" />
			</table:define>
			<table:define id="c_isHandleResult">
				<bean:write name="element" property="returnResult" />
			</table:define>
			<table:define id="c_isHandle">
				<bean:write name="element" property="handleId" />
			</table:define>
			<table:define id="c_ministerHandle">
				<bean:write name="element" property="ministerHandle" />
			</table:define>
			<table:define id="c_maintDivision">
				<bean:write name="element" property="maintDivision" />
			</table:define>
			<table:define id="c_rem">
				<bean:write name="element" property="rem" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
	<!-- </div> -->
</html:form>