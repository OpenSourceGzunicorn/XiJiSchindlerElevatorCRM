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
				<bean:message key="hotlinefaultclassification.hfcId" />
				:
			</td>
			<td>
				<html:text property="property(hfcId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="hotlinefaultclassification.hfcName" />
				:
			</td>
			<td>
				<html:text property="property(hfcName)" size="40" styleClass="default_input" />
			</td>
			
			<td>
				&nbsp;&nbsp;
				<bean:message key="hotlinefaultclassification.enabledFlag" />
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
	<table:table id="guiHotlineFaultClassification" name="hotlineFaultClassificationList">
		<logic:iterate id="element" name="hotlineFaultClassificationList">
			<table:define id="c_cb">
				<%--html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox--%>
				<bean:define id="hfcId" name="element" property="hfcId" />
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=hfcId.toString()%>" />
			</table:define>
			<table:define id="c_hfcId">
				<a href="<html:rewrite page='/hotlineFaultClassificationAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="hfcId"/>">
				<bean:write name="element" property="hfcId" />
				</a>
			</table:define>
			<table:define id="c_hfcName">
				<bean:write name="element" property="hfcName" />
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