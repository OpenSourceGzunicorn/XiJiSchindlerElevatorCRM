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
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<br>
	<table:table id="guiSearchTermSecurityRisks" name="searchTermSecurityRisksList">
		<logic:iterate id="element" name="searchTermSecurityRisksList">
			<table:define id="c_cb">
			<bean:define id="tsrId" name="element" property="tsrId" />
				<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=tsrId .toString()%>" />
				<html:hidden name="element" property="tsrId" styleId="tsrId"/>
				<html:hidden name="element" property="tsrDetail" styleId="tsrDetail"/>  	
			</table:define>
			<table:define id="c_tsrId">
				<bean:write name="element" property="tsrId" />
			</table:define>
			<table:define id="c_tsrDetail">
				<bean:write name="element" property="tsrDetail" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>