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
				<bean:message key="termsecurityrisks.tsrId" />
				:
			</td>
			<td>
				<html:text property="property(tsrId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="termsecurityrisks.tsrDetail" />
				:
			</td>
			<td>
				<html:text property="property(tsrDetail)" size="40" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="termsecurityrisks.enabledflag" />
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
	<table:table id="guiTermSecurityRisks" name="termSecurityRisksList">
		<logic:iterate id="element" name="termSecurityRisksList">
			<table:define id="c_cb">
			<bean:define id="tsrId" name="element" property="tsrId" />
				<%--  <html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox> --%>
				
				 <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=tsrId.toString()%>" />
			</table:define>
			<table:define id="c_tsrId">
				<a href="<html:rewrite page='/termSecurityRisksAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="tsrId"/>">
				<bean:write name="element" property="tsrId" />
				</a>
			</table:define>
			<table:define id="c_tsrDetail">
				<bean:write name="element" property="tsrDetail" />
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