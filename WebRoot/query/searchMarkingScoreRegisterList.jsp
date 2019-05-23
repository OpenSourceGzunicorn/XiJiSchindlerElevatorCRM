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
<html:hidden property="property(QualityNos)"/>
<html:hidden property="property(elevatorType)"/>

	<table>
		<tr>
			<td>
				评分代码
				:
			</td>
			<td>
				<html:text property="property(msId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				评分名称
				:
			</td>
			<td>
				<html:text property="property(msName)" size="40" styleClass="default_input" />
			</td>
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<br>
	<table:table id="guiSearchMarkingScoreRegister" name="searchMarkingScoreRegisterList">
		<logic:iterate id="element" name="searchMarkingScoreRegisterList">
			<table:define id="c_cb">
			<bean:define id="msId" name="element" property="msId" />
				<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=msId.toString()%>" />
				<html:hidden name="element" property="msId" styleId="msId"/>
				<html:hidden name="element" property="msName" styleId="msName"/>  
				<html:hidden name="element" property="fraction" styleId="fraction"/>	
			</table:define>
			<table:define id="c_msId">
				<bean:write name="element" property="msId" />
			</table:define>
			<table:define id="c_msName">
				<bean:write name="element" property="msName" />
			</table:define>
			 <table:define id="c_elevatorType">
				<bean:write name="element" property="elevatorType" />
			</table:define> 
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>