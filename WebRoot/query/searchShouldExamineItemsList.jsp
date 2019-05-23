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
				<bean:message key="shouldexamineitems.seiid" />
				:
			</td>
			<td>
				<html:text property="property(seiid)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="shouldexamineitems.seiDetail" />
				:
			</td>
			<td>
				<html:text property="property(seiDetail)" size="40" styleClass="default_input" />
			</td>
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<br>
	<table:table id="guiSearchShouldExamineItems" name="searchShouldExamineItemsList">
		<logic:iterate id="element" name="searchShouldExamineItemsList">
			<table:define id="c_cb">
			<bean:define id="seiid" name="element" property="seiid" />
				<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=seiid .toString()%>" />
				<html:hidden name="element" property="seiid" styleId="seiid"/>
				<html:hidden name="element" property="seiDetail" styleId="seiDetail"/>  	
			</table:define>
			<table:define id="c_seiid">
				<bean:write name="element" property="seiid" />
			</table:define>
			<table:define id="c_seiDetail">
				<bean:write name="element" property="seiDetail" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>