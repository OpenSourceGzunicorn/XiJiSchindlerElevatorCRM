<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table"%>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors />
<br>
<html:form action="/ServeTable.do">
  <table>
		<tr>
			<td>
				<bean:message key="hotlinefaulttype.hftId" />
				:
			</td>
			<td>
				<html:text property="property(hftId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="hotlinefaulttype.hftDesc" />
				:
			</td>
			<td>
				<html:text property="property(hftDesc)" size="40" styleClass="default_input" />
			</td>
		</tr>
	</table>
	<br>
	<table:table id="guiHotlineFaultType" name="searchCalloutHftList">
		<logic:iterate id="element" name="searchCalloutHftList">
			<table:define id="c_cb">
				<bean:define id="hftId" name="element" property="hftId" />
				<html:checkbox property="checkBoxList(ids)" styleId="ids" value="<%=hftId.toString()%>" />
				<html:hidden name="element" property="hftId" styleId="hftId"/>
				<html:hidden name="element" property="hftDesc" styleId="hftDesc"/>
			</table:define>
			<table:define id="c_hftId">
				<bean:write name="element" property="hftId" />
			</table:define>
			<table:define id="c_hftDesc">
				<bean:write name="element" property="hftDesc" />
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