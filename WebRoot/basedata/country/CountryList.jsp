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
				<bean:message key="country.countryId" />
				:
			</td>
			<td>
				<html:text property="property(countryId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="country.countryName" />
				:
			</td>
			<td>
				<html:text property="property(countryName)"
					styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="country.enabledflag" />
				:
			</td>
			<td>
				<html:select property="property(enabledflag)">
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
	<table:table id="guiCountry" name="countryList">
		<logic:iterate id="element" name="countryList">
			<table:define id="c_cb">
				<%--html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="countryId" />
				</html:multibox--%>
				<bean:define id="countryid" name="element" property="countryId" />
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=countryid.toString()%>" />
			</table:define>
			<table:define id="c_countryId">
				<a href="<html:rewrite page='/countryAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="countryId"/>">
				<bean:write	name="element" property="countryId" />
				</a>
			</table:define>
			<table:define id="c_countryName">
				<bean:write name="element" property="countryName" />
			</table:define>
			<table:define id="c_enabledflag">
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