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
				<bean:message key="officecauseanalysis.ocaId" />
				:
			</td>
			<td>
				<html:text property="property(ocaId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="officecauseanalysis.ocaName" />
				:
			</td>
			<td>
				<html:text property="property(ocaName)" size="40" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="officecauseanalysis.enabledflag" />
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
	<table:table id="guiOfficeCauseAnalysis" name="officeCauseAnalysisList">
		<logic:iterate id="element" name="officeCauseAnalysisList">
			<table:define id="c_cb">
			<bean:define id="ocaId" name="element" property="ocaId" />
				<%--  <html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox> --%>
				
				 <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=ocaId.toString()%>" />
			</table:define>
			<table:define id="c_ocaId">
				<a href="<html:rewrite page='/officeCauseAnalysisAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="ocaId"/>">
				<bean:write name="element" property="ocaId" />
				</a>
			</table:define>
			<table:define id="c_ocaName">
				<bean:write name="element" property="ocaName" />
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