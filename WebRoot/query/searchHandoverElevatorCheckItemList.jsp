<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<html:errors />

<html:form action="/ServeTable.do">
  <table>
		<tr>
			<td>
				&nbsp;&nbsp;
				<bean:message key="handoverelevatorcheckitem.checkItem" />
				:
			</td>
			<td>
				<html:text property="property(checkItem)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="handoverelevatorcheckitem.issueCoding" />
				:
			</td>
			<td>
				<html:text property="property(issueCoding)" styleClass="default_input" />
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;&nbsp;
				<bean:message key="handoverelevatorcheckitem.issueContents" />
				:
			</td>
			<td>
				<html:text property="property(issueContents)" size="40" styleClass="default_input" />
			</td>
			<td>
			&nbsp;&nbsp;
				<bean:message key="handoverelevatorcheckitem.examType" />
				:
			</td>
			<td>
			  	<html:select property="property(examType)" styleClass="default_input">
			  		<html:option value=""> È«²¿</html:option>
					<html:options collection="examTypeList" property="id.pullid" labelProperty="pullname"/>
			  	</html:select>
			</td>
		</tr>
			<html:hidden property="property(genReport)" styleId="genReport" />
			<html:hidden property="property(elevatorType)" styleId="elevatorType" />
			<html:hidden property="property(checkItems)" styleId="checkItems" />
			<html:hidden property="property(examTypes)" styleId="examTypes" />
			<html:hidden property="property(issueCodings)" styleId="issueCodings" />
	</table>
  <br>
  <div  id="divid" style="overflow-y:auto; overflow-x:auto;width:100%; height:340px;">
  <table:table id="guiSearchHandoverElevatorCheckItem" name="searchHandoverElevatorCheckItemList">
    <logic:iterate id="element" name="searchHandoverElevatorCheckItemList">
      <table:define id="c_cb">
        <bean:define id="issueCoding" name="element" property="issueCoding" />
        <html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=issueCoding.toString()%>" />
        <html:hidden name="element" property="examType" styleId="examType"/>
        <html:hidden name="element" property="examTypeName" styleId="examTypeName"/>
        <html:hidden name="element" property="checkItem" styleId="checkItem"/>
        <html:hidden name="element" property="issueCoding" styleId="issueCoding"/>
		<html:hidden name="element" property="issueContents1" styleId="issueContents1"/>
		<html:hidden name="element" property="itemgroup" styleId="itemgroup"/>
      </table:define>
      	<table:define id="c_examType">
			<bean:write name="element" property="examTypeName" />
		</table:define>
		<table:define id="c_checkItem">
			<bean:write name="element" property="checkItem" />
		</table:define>
		<table:define id="c_issueCoding">
			<bean:write name="element" property="issueCoding" />
		</table:define>
		<table:define id="c_elevatorType">
			<bean:write name="element" property="elevatorType" />
		</table:define>
		<table:define id="c_issueContents">
			<bean:write name="element" property="issueContents1" />
		</table:define>
      <table:tr />
    </logic:iterate>
  </table:table>
  </div>
</html:form>