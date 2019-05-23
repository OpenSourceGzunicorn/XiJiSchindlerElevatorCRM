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
<html:hidden property="property(msIds)"/>
<%-- <html:hidden property="QualityNos"/> --%>
	<table>
		<tr>
			<td>
				评分明细代码
				:
			</td>
			<td>
				<html:text property="property(detailId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				评分明细名称
				:
			</td>
			<td>
				<html:text property="property(detailName)" size="40" styleClass="default_input" />
			</td>
			<html:hidden property="property(genReport)" styleId="genReport" />
		</tr>
	</table>
	<br>
	<table:table id="guiSearchMarkingScoreRegisterDetail" name="searchMarkingScoreRegisterDetailList">
		<logic:iterate id="element" name="searchMarkingScoreRegisterDetailList">
			<table:define id="c_cb">
			<bean:define id="detailId" name="element" property="detailId" />
				<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=detailId.toString()%>" />
				<html:hidden name="element" property="detailId" styleId="detailId"/>
				<html:hidden name="element" property="detailName" styleId="detailName"/>  
				<html:hidden name="element" property="markingScore.msId" styleId="msId"/>	
			</table:define>
			<table:define id="c_detailId">
				<bean:write name="element" property="detailId" />
			</table:define>
			<table:define id="c_detailName">
				<bean:write name="element" property="detailName" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>