<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css"
	href="<html:rewrite forward='formCSS'/>">
	<script language="javascript" defer="defer" src="<html:rewrite forward='DatePickerJS'/>"></script>
<html:errors />

<html:form action="/ServeTable.do">
	<table>
		<tr >
          	<td>
			  &nbsp;&nbsp;录入人:
			</td>
		    <td>
			  <html:text property="property(openid)"/>
			</td>
			<td>
			&nbsp;&nbsp;录入日期:
			</td>
		   <td>
		   		<html:text property="property(sdate1)" styleClass="Wdate" size="12" styleId="sdate1"  onfocus="WdatePicker({isShowClear:true,readOnly:true})"/>
				- 
				<html:text property="property(edate1)" styleClass="Wdate" size="12" styleId="edate1" onfocus="WdatePicker({isShowClear:true,readOnly:true})"/>
			</td>
  			<td>
			  &nbsp;&nbsp;维保站:
			</td>
		    <td>
				<html:select property="property(maintStation)" styleId="maintStation" >
				    <html:options collection="mainStationList" property="storageid" labelProperty="storagename" />
				</html:select>
			</td>
			<td>
			  &nbsp;&nbsp;电梯编号:
			</td>
		    <td>
			  <html:text property="property(elevatorNo)"/>
			</td>
			
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
		<tr>
		</tr>
		
	</table>
<br>	 

	<table:table id="guiQcLogManagement" name="qclogManagementList">
		<logic:iterate id="element" name="qclogManagementList">
			<table:define id="c_cb">
			 
			 <bean:define id="rowid" name="element" property="rowid" />  
				  <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=rowid.toString()%>" />
			</table:define>
			<table:define id="c_operId">
				<a href="<html:rewrite page='/qcLogManagementAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="rowid"/>">
				<bean:write name="element" property="operId" />
			    </a>
			</table:define>
			<table:define id="c_operDate">
				<bean:write name="element" property="operDate" />
			</table:define>		
			 <table:define id="c_maintStation">
				<bean:write name="element" property="maintStation" />
			</table:define> 	
			<table:define id="c_maintPersonnel">
				<bean:write name="element" property="maintPersonnel" />
			</table:define>
			<table:define id="c_elevatorNo">
				<bean:write name="element" property="elevatorNo" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>