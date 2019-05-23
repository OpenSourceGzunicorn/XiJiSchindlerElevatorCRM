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
				<bean:message key="city.cityId" />
				:
			</td>
			<td>
				<html:text property="property(cityId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="city.cityName" />
				:
			</td>
			<td>
				<html:text property="property(cityName)"
					styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				À˘ Ù °∑›
				:
			</td>
			<td>
				<html:text property="property(province)"
					styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="city.enabledflag" />
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
	<table:table id="guiCity" name="cityList">
		<logic:iterate id="element" name="cityList">
			<table:define id="c_cb">
				<%--html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="cityId" />
				</html:multibox--%>
				<bean:define id="cityid" name="element" property="cityId" />
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=cityid.toString()%>" />
			</table:define>
			<table:define id="c_cityId">
				<a href="<html:rewrite page='/cityAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="cityId"/>">
				<bean:write	name="element" property="cityId" />
				</a>
			</table:define>
			<table:define id="c_cityName">
				<bean:write name="element" property="cityName" />
			</table:define>
			<table:define id="c_province">
				<bean:write name="element" property="province.provinceName" />
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