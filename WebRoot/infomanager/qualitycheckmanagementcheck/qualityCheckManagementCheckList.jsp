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
				 <bean:message key="elevatorSale.elevatorNo"/>
				:
			</td>
			<td>
			    <html:text property="property(elevatorNo)" styleClass="default_input" ></html:text>			
			</td>		
			<td>
				 <bean:message key="elevatorArchivesInfo.maintContractNo"/>
				:
			</td>
			<td>
				<html:text property="property(maintContractNo)" styleClass="default_input" ></html:text>
			</td>
			<td>
				<bean:message key="elevatorArchivesInfo.projectName"/>
				:
			</td>
			<td colspan="3">
				<html:text property="property(projectName)" styleClass="default_input" size="43" />
			</td>
			<td>
				 维保工
				:
			</td>
			<td>
			    <html:text property="property(maintPersonnel)" styleClass="default_input" />			
			</td>
		</tr>
		<tr>
		    <td>
				 督查人员
				:
			</td>
			<td>
			    <html:text property="property(superviseId)" styleClass="default_input" ></html:text>			
			</td>	
			<td>
				<bean:message key="elevatorArchivesInfo.maintDivision"/>
				:
			</td>
			<td>
				<html:select property="property(maintDivision)" onchange="Evenmore(this,'maintStation')">
					<html:options collection="maintDivisionList" property="grcid" labelProperty="grcname"/>
				</html:select>
			</td>
			<td>
				维保站
				:
			</td>
			<td>
				<html:select property="property(maintStation)" styleId="maintStation">
		    		<html:options collection="mainStationList" property="storageid" labelProperty="storagename"/>
				</html:select>
			</td>
			
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<br>
	<table:table id="guiQualityCheckManagement" name="qualityCheckManagementCheckList">
		<logic:iterate id="element" name="qualityCheckManagementCheckList">
			<table:define id="c_cb">
			 <bean:define id="billno" name="element" property="billno" />  
				<%--  <html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox>  --%>
				
				  <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=billno.toString()%>" />
				  <html:hidden property="List(submitType)" styleId="submitType" value="${element.submitType }"/>
				  <html:hidden property="pstatus" styleId="pstatus" value="${element.processStatus }"/>
				  
			</table:define>
			 <table:define id="c_billno">
				 <a href="<html:rewrite page='/qualityCheckManagementCheckAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billno"/>"> 
				<bean:write name="element" property="billno" />
				</a>
			</table:define> 
			
			<table:define id="c_elevatorNo">
				<bean:write name="element" property="elevatorNo" />
			</table:define> 
			
			 <table:define id="c_maintContractNo">
				<bean:write name="element" property="maintContractNo" />
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
				<bean:write name="element" property="maintPersonnel" />
			</table:define>
			<table:define id="c_superviseId">
				<bean:write name="element" property="superviseId" />
			</table:define>
			<table:define id="c_status">
				<bean:write name="element" property="status" />
			</table:define>
			<table:define id="c_processName">
				<bean:write name="element" property="processName" />
			</table:define>
			<table:define id="c_submitType">
				<logic:match name="element" property="submitType" value="Y">
					<bean:message key="qualitycheckmanagement.submitted"/>
				</logic:match>
				<logic:match name="element" property="submitType" value="N">
					<bean:message key="qualitycheckmanagement.uncommitted"/>
				</logic:match>
			</table:define>
			<table:define id="c_processStatus">
			<html:hidden name="element" property="processStatus"/>
				<logic:match name="element" property="processStatus" value="0">未登记</logic:match>
        		<logic:match name="element" property="processStatus" value="1">已登记未提交</logic:match>
        		<logic:match name="element" property="processStatus" value="2">已登记已提交</logic:match>
        		<logic:match name="element" property="processStatus" value="3">已审核</logic:match>
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>