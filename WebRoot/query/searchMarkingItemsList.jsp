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
				<bean:message key="markingitems.mtId" />
				:
			</td>
			<td>
				<html:text property="property(mtId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="markingitems.mtName" />
				:
			</td>
			<td>
				<html:text property="property(mtName)" size="40" styleClass="default_input" />
			</td>
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<br>
	<table:table id="guiSearchMarkingItems" name="searchMarkingItemsList">
		<logic:iterate id="element" name="searchMarkingItemsList">
			<table:define id="c_cb">
			<bean:define id="mtId" name="element" property="mtId" />
				<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=mtId.toString()%>" />
				<html:hidden name="element" property="mtId" styleId="mtId"/>
				<html:hidden name="element" property="mtName" styleId="mtName"/>  
				<html:hidden name="element" property="fraction" styleId="fraction"/>	
			</table:define>
			<table:define id="c_mtId">
				<bean:write name="element" property="mtId" />
			</table:define>
			<table:define id="c_mtName">
				<bean:write name="element" property="mtName" />
			</table:define>
			<table:define id="c_fraction">
				<bean:write name="element" property="fraction" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>