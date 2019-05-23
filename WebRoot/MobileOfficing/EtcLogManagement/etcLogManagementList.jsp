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
			  &nbsp;&nbsp;合同号:
			</td>
		    <td>
			  <html:text property="property(contractNo)"/>
			</td>
			</tr>
			<tr>
			<td>
			  &nbsp;&nbsp;项目名称:
			</td>
		    <td>
			  <html:text property="property(projectName)" size="35"/>
			</td>
		    <td>
			  &nbsp;&nbsp;安装单位:
			</td>
		    <td>
			  <html:text property="property(insCompanyName)" size="35"/>
			</td>
			
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
		<tr>
		</tr>
		
	</table>
<br>	 

	<table:table id="guiEtcLogManagement" name="etclogManagementList">
		<logic:iterate id="element" name="etclogManagementList">
			<table:define id="c_cb">
			 
			 <bean:define id="rowid" name="element" property="rowid" />  
				  <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=rowid.toString()%>" />
			</table:define>
			<table:define id="c_operId">
				<a href="<html:rewrite page='/etcLogManagementAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="rowid"/>">
				<bean:write name="element" property="operId" />
			    </a>
			</table:define>
			<table:define id="c_operDate">
				<bean:write name="element" property="operDate" />
			</table:define>		
			 <table:define id="c_contractNo">
				<bean:write name="element" property="contractNo" />
			</table:define> 	
			<table:define id="c_projectName">
				<bean:write name="element" property="projectName" />
			</table:define>
			<table:define id="c_InsCompanyName">
				<bean:write name="element" property="insCompanyName" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>