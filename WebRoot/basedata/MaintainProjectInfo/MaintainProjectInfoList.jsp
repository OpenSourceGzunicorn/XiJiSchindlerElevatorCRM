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
				&nbsp;&nbsp;
				维保项目
				:
			</td>
			<td>
				<html:text property="property(maintItem)" styleClass="default_input" size="40"/>
			</td>
			<td>
				&nbsp;&nbsp;
				保养类型
				:
			</td>
			<td>
				<html:select property="property(maintType)">
					<html:option value="">
						全部
					</html:option>
					<html:options collection="maintTypeList" property="id.pullid" labelProperty="pullname"/>  
				</html:select>
			</td>
		
				<td>
			&nbsp;&nbsp;
			电梯类型
			:
			</td>
			<td><html:select property="property(elevatorType)">
					<html:option value="">
						全部
					</html:option>
					<html:options collection="elevatorTypeList" property="id.pullid" labelProperty="pullname"/>  
				</html:select>
			</td>
			
		</tr>
		<tr>
			<td>
				&nbsp;&nbsp;
				维保基本要求
				:
			</td>
			<td>
				<html:text property="property(maintContents)" size="40" styleClass="default_input" />
			</td>
		
			
			<td>
				&nbsp;&nbsp;
				<bean:message key="handoverelevatorcheckitem.enabledFlag" />
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
	<table:table id="guiMaintainProjectInfo" name="MaintainProjectInfoList">
		<logic:iterate id="element" name="MaintainProjectInfoList">
			<table:define id="c_cb">
				<%--html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox--%>
				<%-- <bean:define id="id" name="element" property="numno" /> --%>
				<%-- <html:radio property="checkBoxList(ids)" styleId="ids" value="<bean:write name="element" property="numno"/>" /> --%>
				<bean:define id="id" name="element" property="numno" />
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=id.toString()%>" />
				<%-- <input name="id" id="id" type="hidden" value="<bean:write name="element" property="numno"/>" /> --%>
	<%-- 			<input name="Item" id="Item" type="hidden" value="<bean:write name="element" property="id.checkItem"/>" />
				<input name="Coding" id="Coding" type="hidden" value="<bean:write name="element" property="id.issueCoding"/>" /> --%>
			</table:define>
			<table:define id="c_MaintType">
			
				<bean:write name="element" property="maintType" />
			</table:define>
			<table:define id="c_MaintItem">
				<bean:write name="element" property="maintItem" />
			</table:define>
			<table:define id="c_MaintContents">
				<bean:write name="element" property="maintContents" />
			</table:define>
			<table:define id="c_orderby">
				<a href="<html:rewrite page='/MaintainProjectInfoAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="numno"/>">
				<bean:write name="element" property="orderby" /></a>
				
			</table:define>			
			<table:define id="c_elevatorType">			
				<bean:write name="element" property="elevatorType" />
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