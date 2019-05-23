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
				&nbsp;&nbsp;
				±¨ÐÞµçÌÝ±àºÅ
				:
			</td>
			<td>
				<html:text property="property(elevatorNo)" size="40" styleClass="default_input" />
			</td>
		</tr>
	</table>
	<html:hidden property="property(companyId)" styleId="companyId" />
	<br>
	<table:table id="guiCalloutEleno" name="searchCalloutElenoList">
		<logic:iterate id="element" name="searchCalloutElenoList">
			<table:define id="c_cb">
				<bean:define id="elevatorNo" name="element" property="elevatorNo" />
				<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=elevatorNo.toString()%>" />
				<html:hidden name="element" property="elevatorNo" styleId="elevatorNo"/>
			</table:define>
			<table:define id="c_elevatorNo">
				<bean:write name="element" property="elevatorNo" />
			</table:define>
			<table:define id="c_companyId">
				<bean:write name="element" property="companyName" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>