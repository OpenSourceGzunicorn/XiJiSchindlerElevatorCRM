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
				<bean:message key="hotlinefaulttype.hftId" />
				:
			</td>
			<td>
				<html:text property="property(hftId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="hotlinefaulttype.hftDesc" />
				:
			</td>
			<td>
				<html:text property="property(hftDesc)" size="40" styleClass="default_input" />
			</td>
			
			<td>
				&nbsp;&nbsp;
				<bean:message key="hotlinefaulttype.enabledflag" />
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
	<table:table id="guiHotlineFaultType" name="hotlineFaultTypeList">
		<logic:iterate id="element" name="hotlineFaultTypeList">
			<table:define id="c_cb">
				<%--html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox--%>
				<bean:define id="hftId" name="element" property="hftId" />
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=hftId.toString()%>" />
			</table:define>
			<table:define id="c_hftId">
				<a href="<html:rewrite page='/hotlineFaultTypeAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="hftId"/>">
				<bean:write name="element" property="hftId" />
				</a>
			</table:define>
			<table:define id="c_hftDesc">
				<bean:write name="element" property="hftDesc" />
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