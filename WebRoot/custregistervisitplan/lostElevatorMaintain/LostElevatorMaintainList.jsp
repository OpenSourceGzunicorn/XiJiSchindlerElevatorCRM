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
		<tr>
		    <td>
				&nbsp;&nbsp;
				 <bean:message key="LostElevatorMaintain.CompanyName" /> 
				:
			</td>
			<td>
			    <html:text property="property(companyName)" size="35"  styleId="companyName" styleClass="default_input" ></html:text>			
			</td>
		    <td>
				&nbsp;&nbsp;
				 <bean:message key="LostElevatorMaintain.Contacts" /> 
				:
			</td>
			<td>
			    <html:text property="property(contacts)" size="35"  styleId="contacts" styleClass="default_input" ></html:text>			
			</td>
		    <td>
				&nbsp;&nbsp;
				 <bean:message key="LostElevatorMaintain.ContactPhone" /> 
				:
			</td>
			<td>
			    <html:text property="property(contactPhone)" size="35"  styleId="contactPhone" styleClass="default_input" ></html:text>			
			</td>
			
			</tr>
			
			<tr>	
			<td>
				&nbsp;&nbsp;
				 <bean:message key="returningmaintain.reMark" />
				:
			</td>
			<td>
				<html:select property="property(reMark)">
					<html:option value="">
						<bean:message key="pageword.all" />
					</html:option>
					<html:option value="Y">
						<bean:message key="pageword.yes" />
					</html:option>
					<html:option value="N">
						<bean:message key="pageword.no" />
					</html:option>
				</html:select>
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="returningmaintain.enabledflag" />
				:
			</td>
			<td>
				<html:select property="property(enabledFlag)">
					<html:option value="">
						<bean:message key="pageword.all" />
					</html:option>
					<html:option value="Y">
						<bean:message key="pageword.yes" />
					</html:option>
					<html:option value="N">
						<bean:message key="pageword.no" />
					</html:option>
				</html:select>
			</td>
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table> 
	<br>
	<table:table id="guiLostElevatorMaintain" name="lostElevatorMaintainList">
		<logic:iterate id="element" name="lostElevatorMaintainList">
			<table:define id="c_cb">
			 <bean:define id="billno" name="element" property="billno" />  			
				  <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=billno.toString()%>" />
			</table:define>
		<table:define id="c_companyName">
				 <a href="<html:rewrite page='/lostElevatorMaintainAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billno"/>"> 
				<bean:write name="element" property="r1" />
				</a>
			</table:define> 
			
			<table:define id="c_contacts">
				<bean:write name="element" property="contacts" />
			</table:define> 
			
			 <table:define id="c_contactPhone">
				<bean:write name="element" property="contactPhone" />
			</table:define> 
			
			<table:define id="c_reOrder">
				<bean:write name="element" property="reOrder" />
			</table:define>
			
			<table:define id="c_reMark">
				<logic:match name="element" property="reMark" value="Y">
					<bean:message key="pageword.yes" />
				</logic:match>
				<logic:match name="element" property="reMark" value="N">
					<bean:message key="pageword.no" />
				</logic:match>
			</table:define>
			
			
			<table:define id="c_enabledFlag">
				<logic:match name="element" property="enabledFlag" value="Y">
					<bean:message key="pageword.yes" />
				</logic:match>
				<logic:match name="element" property="enabledFlag" value="N">
					<bean:message key="pageword.no" />
				</logic:match>
			</table:define>
			
			
			<table:tr />
		</logic:iterate>
	</table:table> 
</html:form>