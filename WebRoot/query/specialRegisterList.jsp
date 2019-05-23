<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/ServeTable.do">
<html:hidden property="property(QualityNos)"/>
  <table>
    <tr>
      	<td>特殊要求代码:</td>
      	<td><html:text property="property(scId)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;特殊要求名称:</td>
		<td><html:text property="property(scName)" styleClass="default_input"/></td>
		<html:hidden property="property(genReport)" styleId="genReport" />
    </tr>
  </table>
  <br>
  <table:table id="guiSpecialRegister" name="specialRegisterList">
		<logic:iterate id="element" name="specialRegisterList">
			<table:define id="c_cb">
				<%--html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox--%>
				<bean:define id="scId" name="element" property="scId" />
				<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=scId.toString()%>" />
			    <html:hidden name="element" property="scId" styleId="scId"/>
			    <html:hidden name="element" property="scName" styleId="scName"/>
			</table:define>
			<table:define id="c_scId">
				<%-- <a href="<html:rewrite page='/specialRequirementsAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="scId"/>"> --%>
				<bean:write name="element" property="scId" />
				<!-- </a> -->
			</table:define>
			<table:define id="c_scName">
				<bean:write name="element" property="scName" />
			</table:define>
			<table:define id="c_rem">
                   <bean:write name="element" property="rem" />
			</table:define>
			<table:tr />
		</logic:iterate>
	</table:table>
</html:form>