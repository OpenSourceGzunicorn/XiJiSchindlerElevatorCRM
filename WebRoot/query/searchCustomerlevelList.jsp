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
		<tr >
		    <td>
			  &nbsp;&nbsp;<bean:message key="customerlevel.companyName"/>
			  :
			</td>
		    <td>
			  <html:text property="property(companyName)" size="35"></html:text>
			</td>
		  
			<td>&nbsp;&nbsp;<bean:message key="customerlevel.maintDivision"/>
			:<html:select property="property(maintDivision)"
					onchange="Evenmore(this,'assignedMainStation','td1')"
					onfocus="Evenmore(this,'assignedMainStation','td1')">
					<html:option value=""><bean:message key="pageword.pleaseselect"/></html:option>
				    <html:options collection="CompanyList" property="comid"
						labelProperty="comname" />
				</html:select>
			</td>&nbsp;
			<td id="td1">
			&nbsp;&nbsp;<bean:message key="customerlevel.mainStation"/>
			:<html:select property="property(assignedMainStation)" styleId="assignedMainStation">
				<html:option value=""><bean:message key="pageword.pleaseselect"/></html:option>
				  <logic:present name="mainStationList">
				  <html:options collection="mainStationList" property="storageid" labelProperty="storagename"/>
				 </logic:present>
				</html:select>
			</td>
			</td>
			
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
<br>
	<table:table id="guiCustomerLevel" name="customerLevelSearchList">
		<logic:iterate id="element" name="customerLevelSearchList">
			<table:define id="c_cb">
			 
			 <bean:define id="companyId" name="element" property="companyId" />  
				  <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=companyId.toString()%>" />
		    <html:hidden name="element" property="companyId" styleId="companyId"/>
		    <html:hidden name="element" property="companyName" styleId="companyName"/>
		    <html:hidden name="element" property="principalName" styleId="principalName"/>
		    <html:hidden name="element" property="principalPhone" styleId="principalPhone"/>
		    <html:hidden name="element" property="orderby" styleId="orderby"/>
		    <html:hidden name="element" property="maintDivision" styleId="maintDivision"/>
		    <html:hidden name="element" property="assignedMainStation" styleId="assignedMainStation"/>
            <html:hidden name="element" property="maintDivisionName" styleId="maintDivisionName"/>
		    <html:hidden name="element" property="assignedMainStationName" styleId="assignedMainStationName"/>
			
			
			
			</table:define>
			 <table:define id="c_companyName">
				<bean:write name="element" property="companyName" />			
			</table:define> 
			
			<table:define id="c_principalName">
				<bean:write name="element" property="principalName" />
			</table:define> 
			
			 <table:define id="c_principalPhone">
				<bean:write name="element" property="principalPhone" />
			</table:define> 
			
			<table:define id="c_orderby">
				<bean:write name="element" property="orderby" />				
			</table:define>
			<table:define id="c_maintDivision">
				<bean:write name="element" property="maintDivisionName" />
			</table:define>

			<table:define id="c_assignedMainStation">
				<bean:write name="element" property="assignedMainStationName" />
			</table:define>
			
			<table:define id="c_operDate">
				<bean:write name="element" property="operDate" />
			</table:define>			
			
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>