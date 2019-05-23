<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
	<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<html:errors />
<br>
<html:form action="/ServeTable.do">
	<table>
		<tr >
		    <td>
			  &nbsp;&nbsp;<bean:message key="accessoriesRequisition.singleNo"/>
			  :
			</td>
		    <td>
			  <html:text property="property(singleNo)" size="15"></html:text>
			</td>
			<td>
			  &nbsp;&nbsp;µçÌÝ±àºÅ
			  :
			</td>
		    <td>
			  <html:text property="property(elevatorNo)" size="15"></html:text>
			</td>
		    <td>
			  &nbsp;&nbsp;<bean:message key="accessoriesRequisition.operId"/>
			  :
			</td>
		    <td>
			  <html:text property="property(operId)" size="15"></html:text>
			</td>
			<td>
		  <bean:message key="technologySupport.date"/>
			:
			</td>
		   <td>
		   		<html:text property="property(sdate1)" styleClass="Wdate" size="12" styleId="sdate1"  onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
				- 
				<html:text property="property(edate1)" styleClass="Wdate" size="12" styleId="edate1" onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
			</td>
			
			
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
<br>	 
	 <table:table id="guiAccessoriesRequisition" name="accessoriesRequisitionMaintenanceList">
		<logic:iterate id="element" name="accessoriesRequisitionMaintenanceList">
			<table:define id="c_cb">
			 <bean:define id="appNo" name="element" property="appNo" />  
				  <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=appNo.toString()%>" />
			
			</table:define>
			<table:define id="c_appNo">
				<bean:write name="element" property="appNo" />
			</table:define>
			 <table:define id="c_singleNo">
			 <a href="<html:rewrite page='/accessoriesRequisitionMaintenanceAction.do'/>?method=toPrepareUpdateRecord&id=<bean:write name="element"  property="appNo"/>">
				<bean:write name="element" property="singleNo" />	
			 </a>		
			</table:define> 				
			<table:define id="c_elevatorNo">
				<bean:write name="element" property="elevatorNo" />
			</table:define> 		
			<table:define id="c_handleStatus">
				<bean:write name="element" property="handleStatus" />
			</table:define> 
			<table:define id="c_oldNo">
				<bean:write name="element" property="oldNo" />
			</table:define> 		
			 <table:define id="c_newNo">
				<bean:write name="element" property="newNo" />
			</table:define> 		
			<table:define id="c_operId">
				<bean:write name="element" property="operId" />				
			</table:define>		
			<table:define id="c_personInCharge">
				<bean:write name="element" property="personInCharge" />
			</table:define>
			<table:define id="c_warehouseManager">
				<bean:write name="element" property="warehouseManager" />
			</table:define>
			<table:define id="c_maintDivision">
				<bean:write name="element" property="maintDivision" />
			</table:define>
			<table:define id="c_maintStation">
				<bean:write name="element" property="maintStation" />
			</table:define>
			<table:define id="c_operDate">
				<bean:write name="element" property="operDate" />
			</table:define>
			
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>