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
				<bean:message key="customer.companyId" />
				:
			</td>
			<td>
				<html:text property="property(companyId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="customer.companyName" />
				:
			</td>
			<td>
				<html:text property="property(companyName)" size="40" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="region.enabledflag" />
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
			
			<td>
				&nbsp;&nbsp;
				Î¬±£ºÏÍ¬ºÅ
				:
			</td>
			<td>
				<html:text property="property(maintContractNo)" styleClass="default_input" />
			</td>
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<br>
	<table:table id="guiCustomerM" name="customerListm">
		<logic:iterate id="element" name="customerListm">
			<table:define id="c_cb">
				<%-- <%--html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox--%>
				<bean:define id="companyId" name="element" property="companyId" />
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=companyId.toString()%>" /> 
			</table:define>
			<table:define id="c_companyId">
				<a href="<html:rewrite page='/customerAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="companyId"/>">
				<bean:write name="element" property="companyId" />
				</a>
			</table:define>
			<table:define id="c_companyName">
				<bean:write name="element" property="companyName" />
			</table:define>
			<table:define id="c_contacts">
				<bean:write name="element" property="contacts" />
			</table:define>
			<table:define id="c_contactPhone">
				<bean:write name="element" property="contactPhone" />
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