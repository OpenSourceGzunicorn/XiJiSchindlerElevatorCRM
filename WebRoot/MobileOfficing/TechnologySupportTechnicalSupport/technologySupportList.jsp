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
		    <td nowrap="nowrap">
			  <bean:message key="technologySupport.singleNo"/>
			  :
			</td>
		    <td nowrap="nowrap">
			  <html:text property="property(singleNo)" size="15"></html:text>
			</td>
		    
		    <td nowrap="nowrap">
			  &nbsp;&nbsp;<bean:message key="technologySupport.assignUser"/>
			  :
			</td>
		    <td nowrap="nowrap">
			  <html:text property="property(assignUser)" size="15"></html:text>
			</td>
		  
			<td nowrap="nowrap">
			  &nbsp;&nbsp;<bean:message key="technologySupport.assignUserTel"/>
			  :
			</td>
		    <td nowrap="nowrap">
			  <html:text property="property(assignUserTel)" size="15"></html:text>
			</td>
			
			<td nowrap="nowrap">
			 <bean:message key="technologySupport.date"/>
			:
			</td>
		  
		  <td  nowrap="nowrap">
		   		<html:text property="property(sdate1)" styleClass="Wdate" size="12" styleId="sdate1"  onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
				- 
				<html:text property="property(edate1)" styleClass="Wdate" size="12" styleId="edate1" onfocus="WdatePicker({isShowClear:true,readOnly:true})"></html:text>
			</td>
			
			<td nowrap="nowrap">&nbsp;&nbsp;Î¬±£·Ö²¿:<html:select property="property(maintDivision)">
				    <html:options collection="CompanyList" property="grcid"
						labelProperty="grcname" />
				</html:select>
			</td>
			
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
<br>
	 <table:table id="guiTechnologySupport" name="technologySupportTechnicalSupportList">
		<logic:iterate id="element" name="technologySupportTechnicalSupportList">
			<table:define id="c_cb">
			 
			 <bean:define id="billno" name="element" property="billno" />  
				  <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=billno.toString()%>" />
			
			</table:define>
			 <table:define id="c_singleNo">
			 <a href="<html:rewrite page='/technologySupportTechnicalSupportAction.do'/>?method=toPrepareUpdateRecord&id=<bean:write name="element"  property="billno"/>">
				<bean:write name="element" property="singleNo" />	
			 </a>	
			 </table:define>
			 <table:define id="c_assignUser">			 
				<bean:write name="element" property="assignUser" />	
		
			</table:define>
			
			<table:define id="c_assignUserTel">
				<bean:write name="element" property="assignUserTel" />
			</table:define> 
			
			 <table:define id="c_maintDivision">
				<bean:write name="element" property="maintDivision" />
			</table:define> 
			
			<table:define id="c_maintStation">
				<bean:write name="element" property="maintStation" />				
			</table:define>
			
			<table:define id="c_proStatus">
				<bean:write name="element" property="proStatus" />
			</table:define>
			
			<table:define id="c_operDate">
				<bean:write name="element" property="operDate" />
			</table:define>
			
			
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>