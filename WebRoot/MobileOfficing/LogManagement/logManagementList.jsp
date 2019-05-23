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
           <td >&nbsp;&nbsp;<bean:message key="logManagement.maintDivision"/>
			:
			</td><td>
				<html:select property="property(maintDivision)" onchange="Evenmore(this,'assignedMainStation','td1')">
				    <html:options collection="CompanyList" property="grcid" labelProperty="grcname" />
				</html:select>
			</td>		 
          	<td id="td1" colspan="2">
			&nbsp;&nbsp;<bean:message key="logManagement.maintStation"/>
			:
				<html:select property="property(assignedMainStation)" styleId="assignedMainStation" >
				    <html:options collection="mainStationList" property="storageid" labelProperty="storagename" />
				</html:select>
			</td>
  			<td>
			  &nbsp;&nbsp;
			  <bean:message key="logManagement.salesContractNo"/>
			  :
			</td>
		    <td>
			  <html:text property="property(salesContractNo)" size="15"></html:text>
			</td>
			</tr>
<tr>
		    <td colspan="1">
			  &nbsp;&nbsp;<bean:message key="logManagement.projectName"/>
			  :
			</td>
		    <td colspan="3">
			  <html:text property="property(projectName)" size="38"></html:text>
			</td>
			<td>
		&nbsp;&nbsp;&nbsp;&nbsp;
		 <bean:message key="logManagement.operDate"/>
			:
			</td>
		   <td>
		   		<html:text property="property(sdate1)" styleClass="Wdate" size="12" styleId="sdate1"  onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
				- 
				<html:text property="property(edate1)" styleClass="Wdate" size="12" styleId="edate1" onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
			</td>
			
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
		<tr>
		    <td colspan="1">
			  &nbsp;&nbsp;¬º»Î»À
			  :
			</td>
		    <td colspan="3">
			  <html:text property="property(openid)"></html:text>
			</td>
		</tr>
		
	</table>
<br>	 

	<table:table id="guiLogManagement" name="logManagementList">
		<logic:iterate id="element" name="logManagementList">
			<table:define id="c_cb">
			 
			 <bean:define id="rowid" name="element" property="rowid" />  
				  <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=rowid.toString()%>" />
			
			</table:define>

			 				
			<table:define id="c_maintDivision">
				<bean:write name="element" property="maintDivision" />
			</table:define> 		
			 <table:define id="c_maintStation">
				<bean:write name="element" property="maintStation" />
			</table:define> 		
			<table:define id="c_salesContractNo">
				<bean:write name="element" property="salesContractNo" />				
			</table:define>		
			<table:define id="c_projectName">
				<bean:write name="element" property="projectName" />
			</table:define>
			<table:define id="c_operId">
				
				<a href="<html:rewrite page='/logManagementAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="rowid"/>">
				<bean:write name="element" property="operId" />
			    </a>
			</table:define>
			<table:define id="c_operDate">
				<bean:write name="element" property="operDate" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>