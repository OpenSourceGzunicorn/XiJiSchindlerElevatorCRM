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
				<bean:message key="handoverelevatorcheckitem.examType" />
				:
			</td>
			<td>
			  	<html:select property="property(examType)" styleClass="default_input">
			  		<html:option value=""> <bean:message key="XJSCRM.zhz" /></html:option>
					<html:options collection="examTypeList" property="id.pullid" labelProperty="pullname"/>
			  	</html:select>
			</td> 
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
			电梯类型
			:
			</td>
			<td><html:select property="property(elevatorType)">
					<html:option value="">
						全部
					</html:option>
					<html:options collection="elevatorTypeList" property="id.pullid" labelProperty="pullname"/>  
				</html:select>
			</td>
			
			<td>
				&nbsp;&nbsp;
				<bean:message key="handoverelevatorcheckitem.enabledFlag" />
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
	<table:table id="guiHandoverElevatorCheckItem" name="handoverElevatorCheckItemList">
		<logic:iterate id="element" name="handoverElevatorCheckItemList">
			<table:define id="c_cb">
				<%--html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox--%>
				<bean:define id="issueCoding" name="element" property="id.issueCoding" />
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=issueCoding.toString()%>" />
				
				<input name="Type" id="Type" type="hidden" value="<bean:write name="element" property="id.examType"/>" />
				<input name="Item" id="Item" type="hidden" value="<bean:write name="element" property="id.checkItem"/>" />
				<input name="Coding" id="Coding" type="hidden" value="<bean:write name="element" property="id.issueCoding"/>" />
			</table:define>
			<table:define id="c_examType">
			<a href="<html:rewrite page='/handoverElevatorCheckItemAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="id.issueCoding"/>&type=<bean:write name="element"  property="id.examType"/>&item=<bean:write name="element"  property="id.checkItem"/>">
				<bean:write name="element" property="id.examType" />
				</a>
			</table:define>
			<table:define id="c_checkItem">
				<bean:write name="element" property="id.checkItem" />
			</table:define>
			<table:define id="c_issueCoding">
				<bean:write name="element" property="id.issueCoding" />
			</table:define>
			<table:define id="c_issueContents">
				<bean:write name="element" property="issueContents" />
			</table:define>			
			<table:define id="c_elevatorType">			
				<logic:match name="element" property="elevatorType" value="T">
					直梯
				</logic:match>
				<logic:match name="element" property="elevatorType" value="F">
					扶梯
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