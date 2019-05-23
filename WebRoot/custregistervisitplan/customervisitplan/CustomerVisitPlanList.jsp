<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<html:errors />
<html:form action="/ServeTable.do">
	<table>
		<tr>
			<td><bean:message key="custreturnregister.companyName" />:
			</td>
			<td><html:text property="property(companyName)" styleClass="default_input" size="33"/></td>
			<td>&nbsp;&nbsp;<bean:message key="customerlevel.principalName" />:
			</td>
			<td><html:text property="property(principalName)" styleClass="default_input"/></td>
			<td >&nbsp;&nbsp;<bean:message key="customerlevel.maintDivision" />:</td>
			<td>
			<html:select
					property="property(maintDivision)"
					onchange="Evenmore(this,'assignedMainStation','td1')" style="width: 80px">
					<html:options collection="CompanyList" property="grcid"
						labelProperty="grcname" />
				</html:select>
			</td>
			<td id="td1" >&nbsp;&nbsp;<bean:message key="customerlevel.mainStation" />:</td>
			<td>
					<html:select property="property(assignedMainStation)" styleId="assignedMainStation">
					<logic:equal name="isselectval" value="N">
						<html:option value=""><bean:message key="pageword.pleaseselect" /></html:option>
					</logic:equal>
					<logic:present name="mainStationList">
						<html:options collection="mainStationList" property="storageid" labelProperty="storagename" />
					</logic:present>
			</html:select>
			</td>
		</tr>

		<tr>
			<td><bean:message key="customerVisitPlan.visitDate" />:
			</td>
			<td>
		   		<html:text property="property(sdate1)" styleClass="Wdate"  size="12" styleId="sdate1"  onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
				- 
				<html:text property="property(edate1)" styleClass="Wdate"  size="12" styleId="edate1" onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
			</td>	

			<td>&nbsp;&nbsp;拜访人员:</td>
			<td><html:text property="property(visitStaff)" styleClass="default_input" /></td>
			<td>&nbsp;&nbsp;拜访职位:</td>
			<td><html:select property="property(visitPosition)">
					<html:option value="">
						<bean:message key="pageword.all" />
					</html:option>
					<html:options collection="class1List" property="class1id"
						labelProperty="class1name" />
				</html:select>
			</td>
			<td>&nbsp;&nbsp;<bean:message key="customerVisitPlan.isVisit" />:</td>
			<td>
				<html:select property="property(isVisit)" style="width: 80px">
					<html:option value="">
						<bean:message key="pageword.all" />
					</html:option>
					<html:option value="Y">
						<bean:message key="pageword.yes" />
					</html:option>
					<html:option value="N">
						<bean:message key="pageword.no" />
					</html:option>
				</html:select> <html:hidden property="property(genReport)" styleId="genReport" />
			</td>
		</tr>

	</table>
	<br >
<table:table id="guiCustomerVisitPlan" name="customerVisitList">
		<logic:iterate id="element" name="customerVisitList">
		     <table:define id="c_cb" >		 
		        <bean:define id="jnlno" name="element" property="jnlno" />  
				  <%-- html:radio property="checkBoxList(ids)" styleId="ids" value="" / --%>
				  <html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=jnlno.toString()%>" />
		    </table:define>
			<table:define id="c_custLevel" >
			<bean:write name="element" property="custLevel"/>
			</table:define>
			<table:define id="c_companyName" >
			<bean:write name="element" property="companyName" />
			</table:define>
			<table:define id="c_principalName">
			<bean:write  name="element" property="principalName" />
			</table:define>
			<table:define id="c_principalPhone" >
			<bean:write name="element" property="principalPhone" />
			</table:define>
			<table:define id="c_maintDivision">
			<bean:write name="element" property="maintDivision" />
			</table:define>
			<table:define id="c_maintStation" >
			<bean:write name="element" property="maintStation" />
			</table:define>
			<table:define id="c_visitDate" >
			<bean:write name="element" property="visitDate" />
			</table:define>
			<table:define id="c_visitPosition">
			<bean:write name="element" property="visitPosition" />
			</table:define>
			<table:define id="c_visitStaff">
			<bean:write name="element" property="visitStaff" />
			</table:define>
			<table:define id="c_rem" >
			<bean:write name="element" property="rem" />
			</table:define>
			<table:define id="c_isVisit">
			<bean:write name="element" property="isVisit" />
			<input type="hidden" name="isVisit" value='<bean:write name="element" property="isVisit" />'>
			</table:define>
		<table:tr />
		</logic:iterate>
</table:table>

</html:form>