<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/ServeTable.do">
	<table>
		<tr>
			<td>
				<bean:message key="ReceivablesName.recId" />
				:
			</td>
			<td>
				<html:text property="property(recId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="ReceivablesName.recName" />
				:
			</td>
			<td>
				<html:text property="property(recName)" size="40" styleClass="default_input" />
			</td>
			
			<td>
				&nbsp;&nbsp;
				<bean:message key="ReceivablesName.enabledflag" />
				:
			</td>
			<td>
				<html:select property="property(enabledFlag)">
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
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<br>
	<table:table id="guiReceivablesName" name="receivablesNameList">
		<logic:iterate id="element" name="receivablesNameList">
			<table:define id="c_cb">
				<%--html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox--%>
				<bean:define id="recId" name="element" property="recId" />
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=recId.toString()%>" />
			</table:define>
			<table:define id="c_recId">
				<a href="<html:rewrite page='/receivablesNameAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="recId"/>">
				<bean:write name="element" property="recId" />
				</a>
			</table:define>
			<table:define id="c_recName">
				<bean:write name="element" property="recName" />
			</table:define>
			<table:define id="c_operId">
				<bean:write name="element" property="operId" />
			</table:define>
			<table:define id="c_operDate">
				<bean:write name="element" property="operDate" />
			</table:define>
			<table:define id="c_enabledFlag">
				<logic:match name="element" property="enabledFlag" value="Y">
					<bean:message key="pageword.yes" />
				</logic:match>
				<logic:match name="element" property="enabledFlag" value="N">
					<bean:message key="pageword.no" />
				</logic:match>
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>