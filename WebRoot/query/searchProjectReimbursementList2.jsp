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
				流水号
				:
			</td>
			<td>
				<html:text property="property(billno)" styleClass="default_input" />
			</td>
			<td>
			&nbsp;&nbsp;
				合同号
				:
			</td>
			<td>
				<html:text property="property(maintContractNo)" styleClass="default_input" />
			</td>
			<td>
			&nbsp;&nbsp;
				合同类型
				:
			</td>
			<td>
				<html:select property="property(busType)">
					<html:option value="">--请选择--</html:option>
					<html:option value="B">保养</html:option>
					<html:option value="W">维修</html:option>
					<html:option value="G">改造</html:option>
				</html:select>
			</td>
			<html:hidden property="property(genReport)" styleId="genReport" />
			<html:hidden property="property(maintStation)"/>
			
		</tr>
	</table>
	<br>
	<table:table id="guiProjectReimbursement2" name="searchProjectReimbursementList2">
		<logic:iterate id="element" name="searchProjectReimbursementList2">
			<table:define id="c_cb">
			<bean:define id="billno" name="element" property="billno" />
				<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=billno .toString()%>" />
				<html:hidden name="element" property="billno" styleId="billno" />
				<html:hidden name="element" property="maintContractNo" styleId="maintContractNo"/>
				<html:hidden name="element" property="busType" styleId="busType"/> 
				<html:hidden name="element" property="num" styleId="num"/>
			</table:define>
			<table:define id="c_billno">
				<bean:write name="element" property="billno" />
			</table:define>
			<table:define id="c_maintContractNo">
				<bean:write name="element" property="maintContractNo" />
			</table:define>
			<table:define id="c_busType">
				<logic:match name="element" property="busType" value="B">保养</logic:match>
				<logic:match name="element" property="busType" value="W">维修</logic:match>
				<logic:match name="element" property="busType" value="G">改造</logic:match>
			</table:define>
			<table:define id="c_num">
				<bean:write name="element" property="num" />
			</table:define>
			<table:define id="c_maintDivision">
				<bean:write name="element" property="maintDivision" />
			</table:define>
			<table:define id="c_maintStation">
				<bean:write name="element" property="storageName" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>