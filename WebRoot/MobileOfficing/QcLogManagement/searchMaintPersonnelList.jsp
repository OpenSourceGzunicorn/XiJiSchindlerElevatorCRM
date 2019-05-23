<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/ServeTable.do">
	<div style="overflow: scroll;width: 900px;height: 400px">
		<table:table id="guiMaintPersonnel" name="searchMaintPersonnelList">
			<logic:iterate id="element" name="searchMaintPersonnelList">
				<table:define id="c_cb">
					<html:checkbox property="checkBoxList(ids)" styleId="ids" value="${element.username}" />
					<html:hidden name="element" property="username" styleId="username"/>
					<html:hidden name="element" property="phone" styleId="phone"/>
					<html:hidden name="element" property="MaintPersonnel" styleId="MaintPersonnel"/>
				</table:define>
				<table:define id="c_userName">
					<bean:write name="element" property="username" />
				</table:define>
				<table:define id="c_phone">
					<bean:write name="element" property="phone" />
				</table:define>
				<table:tr />
			</logic:iterate>
		</table:table>
	</div>
</html:form>