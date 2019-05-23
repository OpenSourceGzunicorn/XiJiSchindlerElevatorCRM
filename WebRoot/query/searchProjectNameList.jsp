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
			  &nbsp;&nbsp;<bean:message key="customerVisitFeedback.projectName"/>
			  :
			</td>
		    <td>
			  <html:text property="property(projectName)" size="30" styleClass="default_input" ></html:text>
			</td>
		  <td>
			  &nbsp;&nbsp;<bean:message key="customerVisitFeedback.projectAddress"/>
			  :
			</td>
		    <td>
			  <html:text property="property(projectAddress)" size="40" styleClass="default_input" ></html:text>
			
			<html:hidden property="property(genReport)" styleId="genReport" />
			<html:hidden property="property(jnlno)" styleId="jnlno" />
			</td>	
		</tr>
	</table>
<br>
	<table:table id="guiProjectName" name="projectNameList">
		<logic:iterate id="element" name="projectNameList">
			<table:define id="c_cb">
			 
			  <bean:define id="billNo" name="element" property="billNo" />  
			 <html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=billNo.toString()%>" />
		    <html:hidden name="element" property="billNo" styleId="billNo"/>
		    <html:hidden name="element" property="projectName" styleId="projectName"/>
		    <html:hidden name="element" property="projectAddress" styleId="projectAddress"/>
		    <html:hidden name="element" property="contractSdate" styleId="contractSdate"/>
		    <html:hidden name="element" property="contractEdate" styleId="contractEdate"/>
		    
			</table:define>
			 <table:define id="c_projectName">
				 <bean:write name="element" property="projectName" />		
			</table:define> 
			
			<table:define id="c_projectAddress">
				<bean:write name="element" property="projectAddress" />	
			</table:define> 
			
			 <table:define id="c_contractSdate">
				<bean:write name="element" property="contractSdate" />	
			</table:define> 
			
			<table:define id="c_contractEdate">
					<bean:write name="element" property="contractEdate" />			
			</table:define>
					
			
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>