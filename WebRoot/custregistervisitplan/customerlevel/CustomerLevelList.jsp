<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
	<script type="text/javascript" src="<html:rewrite page="/common/javascript/jquery-1.9.1.min.js"/>"></script>
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
			  <html:text property="property(companyName)" size="35" styleClass="default_input"></html:text>
			</td>
		  
			<td>&nbsp;&nbsp;<bean:message key="customerlevel.maintDivision"/>
			:<html:select property="property(maintDivision)"
					onchange="Evenmore(this,'assignedMainStation','td1')">
				    <html:options collection="CompanyList" property="grcid" labelProperty="grcname"/>
				</html:select>
			</td>
			<td id="td1">
			&nbsp;&nbsp;<bean:message key="customerlevel.mainStation"/>
			:<html:select property="property(assignedMainStation)" styleId="assignedMainStation">
				<html:option value=""><bean:message key="pageword.pleaseselect"/></html:option>
				  <logic:present name="mainStationList">
				  <html:options collection="mainStationList" property="storageid" labelProperty="storagename"/>
				 </logic:present>
				</html:select>
			</td>
			
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
<br>
	<table:table id="guiCustomerLevel" name="customerLevelList">
		<logic:iterate id="element" name="customerLevelList">
			<table:define id="c_cb">
			 
			 <bean:define id="companyId" name="element" property="companyId" />  
				  <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=companyId.toString()%>" />
			
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
				<bean:write name="element" property="maintDivision" />
			</table:define>

			<table:define id="c_assignedMainStation">
				<bean:write name="element" property="assignedMainStation" />
			</table:define>
			
			<table:define id="c_operDate">
				<bean:write name="element" property="operDate" />
			</table:define>			
			
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>
<script type="text/javascript">

$(function(){
	var e = document.getElementById("assignedMainStation"); 
	if(e.options.length==2)
	{
		e.options.remove(e.selectedIndex[0]);
	}
});
</script>
