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
				<bean:message key="province.provinceId" />
				:
			</td>
			<td>
				<html:text property="property(provinceId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="province.provinceName" />
				:
			</td>
			<td>
				<html:text property="property(provinceName)"
					styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				ËùÊô¹ú¼Ò
				:
			</td>
			<td>
				<html:text property="property(country)"	styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="province.enabledflag" />
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
	<table:table id="guiProvince" name="provinceList">
		<logic:iterate id="element" name="provinceList">
			<table:define id="c_cb">
				<%--html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="provinceId" />
				</html:multibox--%>
				<bean:define id="provinceid" name="element" property="provinceId" />
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=provinceid.toString()%>" />
			</table:define>
			<table:define id="c_provinceId">
				<a href="<html:rewrite page='/provinceAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="provinceId"/>">
				<bean:write name="element" property="provinceId" />
				</a>
			</table:define>
			<table:define id="c_provinceName">
				<bean:write name="element" property="provinceName" />
			</table:define>
			<table:define id="c_country">
				<bean:write name="element" property="country.countryName" />
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