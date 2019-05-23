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
				<bean:message key="officecomplaintcategory.occId" />
				:
			</td>
			<td>
				<html:text property="property(occId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="officecomplaintcategory.occName" />
				:
			</td>
			<td>
				<html:text property="property(occName)" size="40" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="officecomplaintcategory.enabledflag" />
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
	<table:table id="guiOfficeComplaintCategory" name="officeComplaintCategoryList">
		<logic:iterate id="element" name="officeComplaintCategoryList">
			<table:define id="c_cb">
			<bean:define id="occId" name="element" property="occId" />
				<%--  <html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox> --%>
				
				 <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=occId.toString()%>" />
			</table:define>
			<table:define id="c_occId">
				<a href="<html:rewrite page='/officeComplaintCategoryAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="occId"/>">
				<bean:write name="element" property="occId" />
				</a>
			</table:define>
			<table:define id="c_occName">
				<bean:write name="element" property="occName" />
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