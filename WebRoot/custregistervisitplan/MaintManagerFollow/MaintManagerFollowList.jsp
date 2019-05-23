<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" src="<html:rewrite forward='validator'/>"></script>
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<script language="javascript" src="<html:rewrite forward='pageJS'/>"></script>
<html:errors />
<html:form action="/ServeTable.do">
	<table>
		<tr>
			<td><bean:message key="custreturnregister.companyName" />:</td>
			<td><html:text property="property(companyName)" size="33" styleClass="default_input"></html:text></td>
			<td>&nbsp;&nbsp;<bean:message key="customerlevel.principalName"/>:</td>
			<td><html:text property="property(principalName)" styleClass="default_input"></html:text></td>
		    <td>&nbsp;&nbsp;<bean:message key="customerVisitPlan.visitDate" />:
			</td>
			<td>
		   		<html:text property="property(sdate1)" styleClass="Wdate"  size="12" styleId="sdate1"  onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
				- 
				<html:text property="property(edate1)" styleClass="Wdate"  size="12" styleId="edate1" onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
			 </td>
		</tr>
          <html:hidden property="property(genReport)" styleId="genReport" />
	</table>
	<br>
	<table:table id="guiMaintManagerFollow"
		name="MaintManagerFollowList">
		<logic:iterate id="element" name="MaintManagerFollowList">
			<table:define id="c_cb">
				<bean:define id="jnlno" name="element" property="jnlno" />
				<html:radio property="checkBoxList(ids)" styleId="ids"
					value="<%=jnlno.toString()%>" />
			</table:define>
			<table:define id="c_custLevel">
				<bean:write name="element" property="custLevel" />
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
			<table:define id="c_maintDivision">
				<bean:write name="element" property="maintDivision" />
			</table:define>
			<table:define id="c_maintStation">
				<bean:write name="element" property="maintStation" />
			</table:define>
			<table:define id="c_visitDate">
				<bean:write name="element" property="visitDate" />
			</table:define>
			<table:define id="c_visitPosition">
			<bean:write name="element" property="visitPosition" />
			<html:hidden name="element" property="visitPosition" />
			</table:define>
			<table:define id="c_visitStaff">
			<bean:write name="element" property="visitStaff" />
			</table:define>
			<table:define id="c_rem">
				<bean:write name="element" property="rem" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>

</html:form>