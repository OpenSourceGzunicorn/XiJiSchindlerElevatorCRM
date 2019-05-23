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
<html:hidden property="property(rowidstr)"/>
	<table>
		<tr>
			<td>
				&nbsp;&nbsp;
				<bean:message key="elevatorArchivesInfo.elevatorNo" />
				:
			</td>
			<td>
				<html:text property="property(elevatorNo)" styleClass="default_input" />
			</td>
			<td>
				<bean:message key="elevatorArchivesInfo.maintContractNo" />
				:
			</td>
			<td>
				<html:text property="property(maintContractNo)" styleClass="default_input" />
			</td>
			<td>
				<bean:message key="elevatorArchivesInfo.salesContractNo" />
				:
			</td>
			<td>
				<html:text property="property(salesContractNo)" styleClass="default_input" />
			</td>
		</tr>
		<tr>
			<td>
			&nbsp;&nbsp;
				维保工
				:
			</td>
			<td>
				<html:text property="property(maintPersonnel)" styleClass="default_input"/>
			</td>
			<td>
				<bean:message key="elevatorArchivesInfo.maintDivision" />
				:
			</td>
			<td>
				<html:select property="property(maintDivision)" styleId="maintdivision" onchange="Evenmore(this,'maintstation')">
					<html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
        		</html:select>
			</td>
			<td>
				维保站
				:
			</td>
			<td>
				<html:select property="property(maintStation)" styleId="maintstation" >
					<html:option value="">全部</html:option>
					<logic:present name="mainStationList">
						<html:options collection="mainStationList" property="storageid" labelProperty="storagename"/>
					</logic:present>
        		</html:select>
			</td>
			</tr>
			<tr>
			<td>&nbsp;&nbsp;
				<bean:message key="elevatorArchivesInfo.projectName" />
				:
			</td>
			<td>
				<html:text property="property(projectName)" styleClass="default_input" size="30"/>
			</td>
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<br>
	<table:table id="guiSearchMaintContractMaster" name="maintContractMasterList">
		<logic:iterate id="element" name="maintContractMasterList">
			<table:define id="c_cb">
			<bean:define id="elevatorNo" name="element" property="elevatorNo" />
				<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=elevatorNo .toString()%>" />
				<html:hidden name="element" property="elevatorNo" styleId="elevatorNo"/>
				<html:hidden name="element" property="elevatorTypeName" styleId="elevatorTypeName"/>
				<html:hidden name="element" property="maintContractNo" styleId="maintContractNo"/>
				<html:hidden name="element" property="salesContractNo" styleId="salesContractNo"/> 
				<html:hidden name="element" property="projectName" styleId="projectName"/> 
				<html:hidden name="element" property="maintDivision" styleId="maintDivision"/> 
				<html:hidden name="element" property="maintStation" styleId="maintStation"/> 
				<html:hidden name="element" property="maintPersonnel" styleId="maintPersonnel"/>
				<html:hidden name="element" property="maintPersonnelName" styleId="maintPersonnelName"/>
				<html:hidden name="element" property="personnelPhone" styleId="personnelPhone"/> 
				<html:hidden name="element" property="rowid" styleId="rowid"/>
				<html:hidden name="element" property="elevatorType" styleId="elevatorType"/>  
			</table:define>
			<table:define id="c_elevatorNo">
				<bean:write name="element" property="elevatorNo" />
			</table:define>
			<table:define id="c_elevatorType">
				<bean:write name="element" property="elevatorTypeName" />
			</table:define>
			<table:define id="c_maintContractNo">
				<bean:write name="element" property="maintContractNo" />
			</table:define>
			<table:define id="c_salesContractNo">
				<bean:write name="element" property="salesContractNo" />
			</table:define>
			<table:define id="c_projectName">
				<bean:write name="element" property="projectName" />
			</table:define>
			<table:define id="c_maintDivision">
				<bean:write name="element" property="maintDivision" />
			</table:define>
			<table:define id="c_maintStation">
				<bean:write name="element" property="maintStation" />
			</table:define>
			<table:define id="c_maintPersonnel">
				<bean:write name="element" property="maintPersonnelName" />
			</table:define>
			<table:define id="c_personnelPhone">
				<bean:write name="element" property="personnelPhone" />
			</table:define>
			<table:define id="c_checksDateTime">
				<bean:write name="element" property="checksDateTime" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>