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
		   <td>&nbsp;&nbsp;合同联系人:</td>
			<td>
				<html:text property="property(contacts)" styleId="contacts" styleClass="default_input" ></html:text>
			</td>
			<td>&nbsp;&nbsp;合同联系人电话:</td>
			<td>
				<html:text property="property(contactPhone)" styleId="contactPhone" styleClass="default_input" ></html:text>
			</td>		
			<td>&nbsp;&nbsp;&nbsp;&nbsp;合同号:</td>
			<td>
				<html:text property="property(maintContractNo)" styleId="maintContractNo" styleClass="default_input" ></html:text>
			</td>
			
			<td>
				&nbsp;&nbsp;<bean:message key="returningmaintain.reMark" />
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
				&nbsp;&nbsp;<bean:message key="returningmaintain.enabledflag" />
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
	<div style="height:380px;overflow:scroll;">
	<table:table id="guiReturningMaintain" name="returningMaintainList">
		<logic:iterate id="element" name="returningMaintainList">
			<table:define id="c_cb">
			 <bean:define id="billno" name="element" property="billno" />  
				<%--  <html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox>  --%>
				
				  <html:radio property="checkBoxList(ids)" styleId="ids" value="<%=billno.toString()%>" />
			</table:define>  
			
			<table:define id="c_contacts">
			 <a href="<html:rewrite page='/returningMaintainAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="billno"/>"> 
				<bean:write name="element" property="contacts" />
				</a>
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
			
			<table:define id="c_rem">
				<bean:write name="element" property="rem" />
			</table:define>
			
			
			<table:tr />
		</logic:iterate>
	</table:table>
	</div>
</html:form>