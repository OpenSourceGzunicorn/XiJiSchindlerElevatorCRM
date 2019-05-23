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
				<bean:message key="hotlinemotherboardtype.hmtId" />
				:
			</td>
			<td>
				<html:text property="property(hmtId)" styleClass="default_input" />
			</td>
			<td>
				&nbsp;&nbsp;
				<bean:message key="hotlinemotherboardtype.hmtName" />
				:
			</td>
			<td>
				<html:text property="property(hmtName)" size="40" styleClass="default_input" />
			</td>
		</tr>
	</table>
	<br>
	<table:table id="guiHotlineMotherboardType" name="searchCalloutHmtList">
		<logic:iterate id="element" name="searchCalloutHmtList">
			<table:define id="c_cb">
				<%--html:multibox property="checkBoxList(ids)" styleId="ids">
					<bean:write name="element" property="regionId" />
				</html:multibox--%>
				<bean:define id="hmtId" name="element" property="hmtId" />
				<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=hmtId.toString()%>" />
				<html:hidden  name="element" property="hmtId" styleId="hmtId"/>
				<html:hidden name="element" property="hmtName" styleId="hmtName"/>
			</table:define>
			<table:define id="c_hmtId">
				<a href="<html:rewrite page='/hotlineMotherboardTypeAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="hmtId"/>">
				<bean:write name="element" property="hmtId" />
				</a>
			</table:define>
			<table:define id="c_hmtName">
				<bean:write name="element" property="hmtName" />
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