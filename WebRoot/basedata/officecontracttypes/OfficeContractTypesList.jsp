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
				<bean:message key="officecontracttypes.octId" />
				:
			</td>
			<td>
				<html:text property="property(octId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="officecontracttypes.octName" />
				:
			</td>
			<td>
				<html:text property="property(octName)" size="40" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="officecontracttypes.enabledflag" />
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
	<table:table id="guiOfficeContractTypes" name="officeContractTypesList">
		<logic:iterate id="element" name="officeContractTypesList">
			<table:define id="c_cb">
			<bean:define id="octId" name="element" property="octId" />
				<%--  <html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox> --%>
				
				 <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=octId.toString()%>" />
			</table:define>
			<table:define id="c_octId">
				<a href="<html:rewrite page='/officeContractTypesAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="octId"/>">
				<bean:write name="element" property="octId" />
				</a>
			</table:define>
			<table:define id="c_octName">
				<bean:write name="element" property="octName" />
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