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
				<bean:message key="ElevatorCoordinateLocation.elevatorNo" />
				:
			</td>
			<td>
				<html:text property="property(elevatorNo)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="ElevatorCoordinateLocation.elevatorLocation" />
				:
			</td>
			<td>
				<html:text property="property(elevatorLocation)" size="40" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				项目名称及楼栋号 
				:
			</td>
			<td>
				<html:text property="property(rem)" size="40" styleClass="default_input" />
			</td>
			<%-- 
			<td>
				&nbsp;&nbsp;
				<bean:message key="ElevatorCoordinateLocation.enabledflag" />
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
			--%>
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<br>
	<table:table id="guiElevatorCoordinateLocation" name="elevatorCoordinateLocationList">
		<logic:iterate id="element" name="elevatorCoordinateLocationList">
			<table:define id="c_cb">
				<%--html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox--%>
				<bean:define id="elevatorNo" name="element" property="elevatorNo" />
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=elevatorNo.toString()%>" />
			</table:define>
			<table:define id="c_elevatorNo">
				<a href="<html:rewrite page='/elevatorCoordinateLocationAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="elevatorNo"/>">
				<bean:write name="element" property="elevatorNo" />
				</a>
			</table:define>
			<table:define id="c_elevatorLocation">
				<bean:write name="element" property="elevatorLocation" />
			</table:define>
			<table:define id="c_beginLongitude">
				<bean:write name="element" property="beginLongitude" />
			</table:define>
			<table:define id="c_endLongitude">
				<bean:write name="element" property="endLongitude" />
			</table:define>
			<table:define id="c_beginDimension">
				<bean:write name="element" property="beginDimension" />
			</table:define>
			<table:define id="c_endDimension">
				<bean:write name="element" property="endDimension" />
			</table:define>
			<table:define id="c_rem">
				<bean:write name="element" property="rem" />
			</table:define>
			<%-- 
			<table:define id="c_enabledFlag">
				<logic:match name="element" property="enabledFlag" value="Y">
					<bean:message key="pageword.yes" />
				</logic:match>
				<logic:match name="element" property="enabledFlag" value="N">
					<bean:message key="pageword.no" />
				</logic:match>
			</table:define>
			--%>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>