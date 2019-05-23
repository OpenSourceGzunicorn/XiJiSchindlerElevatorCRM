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
				<bean:message key="maintenanceworkinghours.elevatorType" />
				:
			</td>	
			<td>
			   <html:select property="property(elevatorType)">
	             <html:option value=""><bean:message key="pageword.all"/></html:option> 
                 <html:options collection="maintenanceWorkingHoursTypeList" property="id.pullid" labelProperty="pullname"/>
	           </html:select>
			</td>
			<td>
				&nbsp;&nbsp;
				 <bean:message key="maintenanceworkinghours.floor" />
				:
			</td>
			<td>
				<html:text property="property(floor)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				 <bean:message key="maintenanceworkinghours.enabledflag" />
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
	<table:table id="guiMaintenanceWorkingHours" name="maintenanceWorkingHoursList">
		<logic:iterate id="element" name="maintenanceWorkingHoursList">
			<table:define id="c_cb">
				<bean:define id="elevatorType" name="element" property="id.elevatorType" />
			  <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=elevatorType.toString()%>" />
			  
			  <input type="hidden" name="elevatorType" value="<bean:write name="element" property="id.elevatorType" />"/>
			  <input type="hidden" name="floor" value="<bean:write name="element" property="id.floor" />"/>
			</table:define>
			<table:define id="c_elevatorType">
			  <a href="<html:rewrite page='/maintenanceWorkingHoursAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="id.elevatorType"/>&floor=<bean:write name="element"  property="id.floor"/>"> 
				<bean:write name="element" property="r1" />		 	
				</a>
			</table:define>
			<table:define id="c_floor">
				<bean:write name="element" property="id.floor" />
			</table:define>
			<table:define id="c_halfMonth">
				<bean:write name="element" property="halfMonth" />
			</table:define>
			<table:define id="c_quarter">
				<bean:write name="element" property="quarter" />
			</table:define>
			<table:define id="c_halfYear">
				<bean:write name="element" property="halfYear" />
			</table:define>
			<table:define id="c_yearDegree">
				<bean:write name="element" property="yearDegree" />
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