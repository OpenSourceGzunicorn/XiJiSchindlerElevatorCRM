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
				<bean:message key="region.regionId" />
				:
			</td>
			<td>
				<html:text property="property(regionId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="region.regionName" />
				:
			</td>
			<td>
				<html:text property="property(regionName)"
					styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				À˘ Ù≥« –
				:
			</td>
			<td>
				<html:text property="property(city)"
					styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="region.enabledflag" />
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
	<table:table id="guiRegion" name="regionList">
		<logic:iterate id="element" name="regionList">
			<table:define id="c_cb">
				<%--html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox--%>
				<bean:define id="regionid" name="element" property="regionId" />
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=regionid.toString()%>" />
			</table:define>
			<table:define id="c_regionId">
				<a href="<html:rewrite page='/regionAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="regionId"/>">
				<bean:write name="element" property="regionId" />
				</a>
			</table:define>
			<table:define id="c_regionName">
				<bean:write name="element" property="regionName" />
			</table:define>
			<table:define id="c_city">
				<bean:write name="element" property="city.cityName" />
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