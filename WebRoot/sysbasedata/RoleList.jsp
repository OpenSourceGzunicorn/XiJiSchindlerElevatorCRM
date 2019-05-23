<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/html-table.tld" prefix="table" %>
<link rel="stylesheet" type="text/css" href="<html:rewrite forward='formCSS'/>">
<html:errors/>
<br>
	<html:form action="/ServeTable.do">
	<table>
	  <tr>
		<td><bean:message key="role.roleid"/>:</td><td><html:text property="property(roleid)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;<bean:message key="role.rolename"/>:</td><td><html:text property="property(rolename)" styleClass="default_input"/></td>
		<td>&nbsp;&nbsp;<bean:message key="role.enabledflag"/>:</td><td><html:select property="property(enabledflag)"><html:option value=""><bean:message key="pageword.all"/></html:option><html:option value="Y"><bean:message key="pageword.yes"/></html:option><html:option value="N"><bean:message key="pageword.no"/></html:option></html:select></td>
	  	<html:hidden property="property(genReport)" styleId="genReport"/>
	  </tr>
	</table>
	<br>
    <table:table id="guiRole" name="roleList">
      <logic:iterate id="element" name="roleList">
		<table:define id="c_cb">
        	<%--html:multibox property="checkBoxList(ids)" styleId="ids">
         		<bean:write name="element" property="roleid"/>
        	</html:multibox--%>
        	<bean:define id="roleid" name="element" property="roleid" />
			<html:radio property="checkBoxList(ids)" styleId="ids" value="<%=roleid.toString()%>" />
		</table:define>
		<table:define id="c_roleid">
        <a href="<html:rewrite page='/roleAction.do'/>?method=toDisplayRecord&id=<bean:write name="element"  property="roleid"/>"><bean:write name="element"  property="roleid"/></a>
		</table:define>
		<table:define id="c_rolename">
        <bean:write name="element" property="rolename"/>
		</table:define>
		<table:define id="c_moduleid">
        <bean:write name="element" property="moduleid"/>
		</table:define>
		<table:define id="c_allowflag">
        <bean:write name="element" property="allowflag"/>
		</table:define>
		<table:define id="c_enabledflag">
		<logic:match name="element" property="enabledflag" value="Y">
		<bean:message key="pageword.yes"/>
		</logic:match>
		<logic:match name="element" property="enabledflag" value="N">
		<bean:message key="pageword.no"/>
		</logic:match>
		</table:define>
        <table:tr/>
      </logic:iterate>
    </table:table>
 </html:form>